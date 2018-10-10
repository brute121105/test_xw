package hyj.xw.thread;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import hyj.xw.GlobalApplication;
import hyj.xw.GlobalValue;
import hyj.xw.activity.ServerConfigActivity;
import hyj.xw.common.CommonConstant;
import hyj.xw.common.FilePathCommon;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.modelHttp.Apk;
import hyj.xw.modelHttp.Device;
import hyj.xw.modelHttp.ResponseData;
import hyj.xw.service.HttpRequestService;
import hyj.xw.task.DownLoadAPkListener;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.ContactUtil;
import hyj.xw.util.FileUtil;
import hyj.xw.util.GetFutureResultUtil;

/**
 * Created by Administrator on 2018/10/9 0009.
 */


public class AutoStopThread extends Thread{
    private static AutoStopThread uniqueInstance = null;
    private AutoStopThread() {
    }
    public static AutoStopThread getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new AutoStopThread();
        }
        return uniqueInstance;
    }
    public void clearInstance(){
        uniqueInstance = null;
    }

    private boolean isShutDown = false;
    //关闭线程，isShutDown设为true关闭。关闭有延时，需要等待while循环执行return才关闭
    public void shutDown(){
        isShutDown = true;
    }
    //判断线程是否已经关闭，isShutDown 为false 表示关闭
    public boolean isAlreadyShutDown(){
        return !isShutDown;
    }

    @Override
    public void run() {
        /**
         * 连接前登录校验
         */
        HttpRequestService httpRequestService = new HttpRequestService(10);
        String deviceNum = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_DEVICE);
        if(TextUtils.isEmpty(deviceNum)){
            AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"设备编号不能为空");
            return;
        }
        try {
            String loginRes = httpRequestService.login();
            AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"登录-->"+loginRes);
            if(!"成功".equals(loginRes)){
                return;
            }
            String res = httpRequestService.deviceConnectServer();
            if(!res.contains("成功")){
                AutoUtil.showToastByRunnable(GlobalApplication.getContext()," 连接失败："+res);
                return;
            }
        }catch (Exception e){
            AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"连接异常Exception，检查网络是否接通，或断开wifi重连");
            e.printStackTrace();
        }

        /**
         * 开始连接
         */
        boolean isFistConnect = true;//是否第一次连接
        AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"开始连接...");
        GlobalValue.uiautoReveiverRefreshTime = System.currentTimeMillis();
        String result = initDeviceConfig2Txt();
        if(!"".equals(result)) return;
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"retry");//next登录下一个，retry新登录,首次开启也是retry
        downloadAttach(false);//下载最新版本脚本
        int coutDisNet=0;
        long lastTime = System.currentTimeMillis();//每30秒监测刷新状态
        long activeTimeLength=0;
        while (true){
            if(isShutDown){
                isShutDown = false;
                System.out.println("doAction--->结束线程");
                return;
            }
            System.out.println("AutoAutoStopThread--"+Thread.currentThread().getName()+"-->"+coutDisNet+" activeTimeLength:"+activeTimeLength);
            if(!isFistConnect) AutoUtil.sleep(2500);
            try {
                /**
                 * 每隔30秒读取device配置文件查看刷新时间，如果超时，发送广播唤醒服务
                 */
                long currentTime = System.currentTimeMillis();
                if(currentTime-lastTime>30000){
                    lastTime = currentTime;
                    Device device = getDeviceConfig();
                    System.out.println("AutoStopThread--doAction--->30s心跳检测状态、是否有最新版本 device.getRefreshTime:");
                    if(device.getRefreshTime()!=null){
                        activeTimeLength = currentTime - device.getRefreshTime();
                        if(activeTimeLength>2*60000&&currentTime - GlobalValue.uiautoReveiverRefreshTime>2*60000){//超过2分钟，重试
                            String msg = "AutoStopThread--doAction--->超时，超过2*60000，发送广播tag：retry";
                            System.out.println(msg);
                            String tag = "retry";
                            AutoUtil.execShell("am broadcast -a hyj.auto.test --es tag \""+tag+"\"");
                        }

                    }else if(currentTime - GlobalValue.uiautoReveiverRefreshTime>5*60000){//超过5分钟，重试
                        String msg = "AutoStopThread--doAction--->超时，超过5*60000，发送广播tag：retry";
                        System.out.println(msg);
                        String tag = "retry";
                        AutoUtil.execShell("am broadcast -a hyj.auto.test --es tag \""+tag+"\"");
                    }
                    downloadAttach(false);
                }

                if(AutoUtil.isNetworkConnected()){
                    String res = httpRequestService.getStartConifgFromServer(deviceNum);
                    System.out.println("AutoStopThread-->获取到服务器状态 getStartConifgFromServer res:"+res);
                    if("".equals(res)){
                        coutDisNet = coutDisNet+1;
                        if(coutDisNet>12){
                            coutDisNet = 0;
                            System.out.println("AutoStopThread--HttpRequestService getPhone--doAction-->连接异常飞行");
                            AutoUtil.execShell("svc wifi disable");
                            AutoUtil.sleep(5000);
                            AutoUtil.execShell("svc wifi enable");
                            AutoUtil.startAppByPackName("hyj.xw","hyj.xw.MainActivity");
                            AutoUtil.sleep(3000);
                            new StartChangeIpThread().start();
                        }
                        continue;
                    }
                    coutDisNet = 0;
                    if(!res.contains("成功")){
                        AutoUtil.showToastByRunnable(GlobalApplication.getContext()," 获取设备状态信息:"+res);
                    }
                    if(isFistConnect){
                        isFistConnect = false;
                        AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"连接成功");
                    }
                    ResponseData responseData = JSONObject.parseObject(res,ResponseData.class);
                    Device device = JSONObject.parseObject(responseData.getData(),Device.class);
                    if(device!=null){
                        GlobalValue.device = device;
                        System.out.println("AutoStopThread-->获取到服务器状态 runState:"+device.getRunState());
                        if(6==device.getRunState()){//杀死脚本
                            System.out.println("doAction--AutoStopThread--->获取到状态6，杀死脚本");
                            System.out.println("doAction--->停止am force-stop hyj.autooperation");
                            AutoUtil.execShell("am force-stop hyj.autooperation");
                            AutoUtil.execShell("am force-stop hyj.xw");
                            AutoUtil.sleep(2000);
                            continue;
                        }if(4==device.getRunState()){//重启手机
                            System.out.println("doAction--AutoStopThread--->获取到状态4，重启手机");
                            AutoUtil.execShell("reboot");
                        }if(5==device.getRunState()){//启动脚本

                            System.out.println("AutoStopThread--UiAutoReciver doAction--->删除联系人");
                            AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"删除联系人");
                            ContactUtil.deleteAll();//删除联系人

                            System.out.println("doAction--AutoStopThread--->获取到状态5，启动脚本");
                            String tag = "retry";
                            AutoUtil.execShell("am broadcast -a hyj.auto.test --es tag \""+tag+"\"");
                        }
                        //标志停止状态，告知对方
                        FileUtil.writeContent2FileForceUtf8(FilePathCommon.stopTxtPath,device.getRunState()+"");//暂停标志 1、正常；2、暂停
                    }
                }else {
                    AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"失败，网络连接不通");
                }
            }catch (Exception e){
                //AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"网络连接波动...");
                e.printStackTrace();
            }
        }
    }


    public  Device getDeviceConfig(){
        String srConfigStr = FileUtil.readAllUtf8(FilePathCommon.startRunninConfigTxtPath);
        Device srConfig = JSONObject.parseObject(srConfigStr,Device.class);
        return srConfig;
    }

    public void downloadAttach(boolean isAlertWindow){
        Apk apk = GetFutureResultUtil.checkVersion1("2");
        if(apk!=null){
            DownLoadAPkListener downLoadAPkListener = new DownLoadAPkListener(GlobalApplication.getContext(),FilePathCommon.downAPk2Path,"2",apk);
            downLoadAPkListener.downloadAttach(isAlertWindow);
            AutoUtil.sleep(5000);
        }
    }

    public String initDeviceConfig2Txt(){
        String isLocalSettingValue = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_LOCAL_SETTING);
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.fkFilePath,"");
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"");
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.startRunninConfigTxtPath,"");
        /*File file = new File(FilePathCommon.downAPk2Path);
        if(file.exists()) file.delete();*/
        String result = "";
        Device device = null;

        //服务器获取配置
        String deviceNum = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_DEVICE);
        if(TextUtils.isEmpty(deviceNum)){
            result = "设备编号不能为空";
            AutoUtil.showToastByRunnable(GlobalApplication.getContext(),result);
            return result;
        }
        HttpRequestService httpRequestService = new HttpRequestService(9);
        String res = httpRequestService.getStartConifgFromServer(deviceNum);
        if(TextUtils.isEmpty(res)||!res.contains("data")){
            result = "1、获取服务器配置信息失败\n"+res;
            AutoUtil.showToastByRunnable(GlobalApplication.getContext(),result);
            return result;
        }
        ResponseData responseData = JSONObject.parseObject(res,ResponseData.class);
        System.out.println("OkHttpUtil getStartConifgFromServer responseData--->"+ JSON.toJSONString(responseData));
        device = JSONObject.parseObject(responseData.getData(),Device.class);
        if(device==null||TextUtils.isEmpty(device.getNum())){
            result = "2、获取服务器配置信息失败\n"+res;
            AutoUtil.showToastByRunnable(GlobalApplication.getContext(),result);
            return result;
        }
        if(device.getHookType()==0){
            result = "中控未设置改机方式";
            AutoUtil.showToastByRunnable(GlobalApplication.getContext(),result);
            return result;
        }
        System.out.println("OkHttpUtil getStartConifgFromServer device--->"+JSON.toJSONString(device));
        device.setChangeIp(1);//重置状态 状态为2是修改ip

        FileUtil.writeContent2FileForceUtf8(FilePathCommon.stopTxtPath,device.getRunState()+"");//暂停标志 1、正常；2、暂停
        GlobalValue.device  = device;
        return result;
    }
    //修改ip线程类，tag为next和retry时触发，轮训检测
    class StartChangeIpThread extends Thread{
        @Override
        public void run() {
            System.out.println("main--doAction-->StartChangeIpThread");
            AutoUtil.execShell("am instrument -w -r   -e debug false -e class hyj.autooperation.ExampleInstrumentedTest#changeIp hyj.autooperation.test/android.support.test.runner.AndroidJUnitRunner");
        }
    }
}

