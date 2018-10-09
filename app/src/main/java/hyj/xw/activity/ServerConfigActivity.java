package hyj.xw.activity;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;

import hyj.xw.GlobalApplication;
import hyj.xw.GlobalValue;
import hyj.xw.MainActivity;
import hyj.xw.R;
import hyj.xw.common.CommonConstant;
import hyj.xw.common.FilePathCommon;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.model.DeviceInfo;
import hyj.xw.modelHttp.Apk;
import hyj.xw.modelHttp.Device;
import hyj.xw.modelHttp.ResponseData;
import hyj.xw.service.HttpRequestService;
import hyj.xw.task.DownLoadAPkListener;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.ContactUtil;
import hyj.xw.util.DeviceParamUtil;
import hyj.xw.util.FileUtil;
import hyj.xw.util.GetFutureResultUtil;

public class ServerConfigActivity extends AppCompatActivity implements View.OnClickListener{

    EditText hostEditText;
    EditText deviceEditText;
    EditText usernameEditText;
    EditText passwordEditText;
    private String[] serverConfigArr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_config);

        createServerConfig();

        //服务器地址
        hostEditText = (EditText) findViewById(R.id.host);
        String hostValue = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_HOST);
        hostEditText.setText(TextUtils.isEmpty(hostValue) ? serverConfigArr==null?"":serverConfigArr[0] : hostValue);
        //设备编号
        deviceEditText = (EditText) findViewById(R.id.deviceNum);
        String deviceNum = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_DEVICE);
        deviceEditText.setText(TextUtils.isEmpty(deviceNum) ? serverConfigArr==null||serverConfigArr[3].equals("deviceNO")?"":serverConfigArr[3] : deviceNum);
        //帐号
        usernameEditText = (EditText) findViewById(R.id.username);
        String username = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_USERNAME);
        usernameEditText.setText(TextUtils.isEmpty(username) ? serverConfigArr==null?"":serverConfigArr[1] : username);
        //密码
        passwordEditText = (EditText) findViewById(R.id.password);
        String password = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_PWD);
        passwordEditText.setText(TextUtils.isEmpty(password) ? serverConfigArr==null?"":serverConfigArr[2] : password);

        //测试连接
        Button testConn = (Button) this.findViewById(R.id.test_conn);
        testConn.setOnClickListener(this);

        //开始运行uiAuto
        Button startUiAuto = (Button) this.findViewById(R.id.start_uiAuto);
        startUiAuto.setOnClickListener(this);

        //返回
        Button back = (Button)this.findViewById(R.id.btn_serverConfig_back);
        back.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.test_conn:
                testConn();
                break;
            case R.id.start_uiAuto:
                startUiAuto();
                break;
            case R.id.btn_serverConfig_back:
                finish();
                break;
        }
    }
    public void save(){
        //主机地址
        String host = hostEditText.getText().toString();
        if(!TextUtils.isEmpty(host)&&!host.contains(":")){
            host = host+":8080";
        }
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_HOST, host);
        //设备编号
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_DEVICE, deviceEditText.getText().toString());
        //帐号
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_USERNAME, usernameEditText.getText().toString());
        //密码
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_PWD, passwordEditText.getText().toString());

    }
    public void testConn(){
        save();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(stopThread.isAlive()){
                        AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"已经连接");
                        return;
                    }
                    String deviceNum = deviceEditText.getText().toString();
                    if(TextUtils.isEmpty(deviceNum)){
                        AutoUtil.showToastByRunnable(ServerConfigActivity.this,"设备编号不能为空");
                        return;
                    }
                    HttpRequestService httpRequestService = new HttpRequestService(2);
                    String loginRes = httpRequestService.login();
                    AutoUtil.showToastByRunnable(ServerConfigActivity.this,"登录->"+loginRes);
                    if(!"成功".equals(loginRes)){
                        return;
                    }
                    String res = httpRequestService.deviceConnectServer();
                    if(res.contains("成功")){
                        startUiAuto();
                    }else {
                        AutoUtil.showToastByRunnable(ServerConfigActivity.this," 连接失败："+res);
                    }
                }catch (Exception e){
                    AutoUtil.showToastByRunnable(ServerConfigActivity.this,"连接异常Exception，检查网络是否接通，或断开wifi重连");
                    e.printStackTrace();
                }
            }
        }).start();
    }
    StopThread stopThread = new StopThread();

    public void startUiAuto(){
        stopThread.interrupt();
        if(!stopThread.isAlive()){
            //先检测版本号，监测到版本低，需要更新，先更新，更新完保存版本号再启动
            //初始化各项
            System.out.println("main-->start========");
            AutoUtil.execShell("am force-stop hyj.autooperation");
            //初始化配置信息
            stopThread.start();

        }else {
            AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"已经连接");
        }
    }
    //修改ip线程类，tag为next和retry时触发，轮训检测
    class StartChangeIpThread extends Thread{
        @Override
        public void run() {
            System.out.println("main--doAction-->StartChangeIpThread");
            AutoUtil.execShell("am instrument -w -r   -e debug false -e class hyj.autooperation.ExampleInstrumentedTest#changeIp hyj.autooperation.test/android.support.test.runner.AndroidJUnitRunner");
        }
    }

    //暂停标准轮训监测
    class StopThread extends Thread{
        @Override
        public void run() {
            boolean isFistConnect = true;//是否第一次连接
            AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"开始连接...");
            GlobalValue.uiautoReveiverRefreshTime = System.currentTimeMillis();
            String result = initDeviceConfig2Txt();
            if(!"".equals(result)) return;
            FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"retry");//next登录下一个，retry新登录,首次开启也是retry

            downloadAttach(false);
            HttpRequestService httpRequestService = new HttpRequestService(10);
            String deviceNum = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_DEVICE);
            int coutDisNet=0;
            long lastTime = System.currentTimeMillis();//每30秒监测刷新状态
            long activeTimeLength=0;
            while (true){
                System.out.println("StopThread--"+Thread.currentThread().getName()+"-->"+coutDisNet+" activeTimeLength:"+activeTimeLength);
                if(!isFistConnect) AutoUtil.sleep(2500);
                try {
                    /**
                     * 每隔30秒读取device配置文件查看刷新时间，如果超时，发送广播唤醒服务
                     */
                    long currentTime = System.currentTimeMillis();
                    if(currentTime-lastTime>30000){
                        lastTime = currentTime;
                        Device device = getDeviceConfig();
                        System.out.println("StopThread--doAction--->30s心跳检测状态、是否有最新版本 device.getRefreshTime:");
                        if(device.getRefreshTime()!=null){
                            activeTimeLength = currentTime - device.getRefreshTime();
                            if(activeTimeLength>2*60000&&currentTime - GlobalValue.uiautoReveiverRefreshTime>2*60000){//超过2分钟，重试
                                String msg = "StopThread--doAction--->超时，超过2*60000，发送广播tag：retry";
                                System.out.println(msg);
                                String tag = "retry";
                                AutoUtil.execShell("am broadcast -a hyj.auto.test --es tag \""+tag+"\"");
                            }

                        }else if(currentTime - GlobalValue.uiautoReveiverRefreshTime>5*60000){//超过5分钟，重试
                            String msg = "StopThread--doAction--->超时，超过5*60000，发送广播tag：retry";
                            System.out.println(msg);
                            String tag = "retry";
                            AutoUtil.execShell("am broadcast -a hyj.auto.test --es tag \""+tag+"\"");
                        }
                        downloadAttach(false);
                    }

                    if(AutoUtil.isNetworkConnected()){
                        String res = httpRequestService.getStartConifgFromServer(deviceNum);
                        System.out.println("StopThread-->获取到服务器状态 getStartConifgFromServer res:"+res);
                        if("".equals(res)){
                            coutDisNet = coutDisNet+1;
                            if(coutDisNet>12){
                                coutDisNet = 0;
                                System.out.println("StopThread--HttpRequestService getPhone--doAction-->连接异常飞行");
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
                            System.out.println("StopThread-->获取到服务器状态 runState:"+device.getRunState());
                            if(6==device.getRunState()){//杀死脚本
                                System.out.println("doAction--StopThread--->获取到状态6，杀死脚本");
                                System.out.println("doAction--->停止am force-stop hyj.autooperation");
                                AutoUtil.execShell("am force-stop hyj.autooperation");
                                AutoUtil.execShell("am force-stop hyj.xw");
                                AutoUtil.sleep(2000);
                                continue;
                            }if(4==device.getRunState()){//重启手机
                                System.out.println("doAction--StopThread--->获取到状态4，重启手机");
                                AutoUtil.execShell("reboot");
                            }if(5==device.getRunState()){//启动脚本

                                System.out.println("StopThread--UiAutoReciver doAction--->删除联系人");
                                AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"删除联系人");
                                ContactUtil.deleteAll();//删除联系人

                                System.out.println("doAction--StopThread--->获取到状态5，启动脚本");
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
                    AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"StopThread Exception状态异常");
                    e.printStackTrace();
                }
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
            DownLoadAPkListener downLoadAPkListener = new DownLoadAPkListener(this,FilePathCommon.downAPk2Path,"2",apk);
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
            AutoUtil.showToastByRunnable(ServerConfigActivity.this,result);
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
        System.out.println("OkHttpUtil getStartConifgFromServer responseData--->"+JSON.toJSONString(responseData));
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


    public void createServerConfig(){
        File file = new File(FilePathCommon.serverConfigTxtPath);
        if(!file.exists()){
            try {
                boolean flag = file.createNewFile();
                System.out.println("doAction--->创建服务器配置文件 "+flag);
                if(flag){
                    String configStr = "192.168.1.1----username----password----deviceNO";
                    FileUtil.writeContent2FileForceUtf8(FilePathCommon.serverConfigTxtPath,configStr);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("doAction--->服务器配置文件已存在");
            String configStr = FileUtil.readAllUtf8(FilePathCommon.serverConfigTxtPath);
            if(!configStr.contains("192.168.1.1")){
                serverConfigArr = configStr.split("----");
            }
        }
    }

    @Override
    protected void onDestroy() {
        save();
        super.onDestroy();
    }


    @Override
    protected void onStop() {
        save();
        super.onStop();
    }
}
