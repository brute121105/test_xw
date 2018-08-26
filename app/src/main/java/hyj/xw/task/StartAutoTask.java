package hyj.xw.task;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.FutureTask;

import hyj.xw.GlobalApplication;
import hyj.xw.MainActivity;
import hyj.xw.aw.sysFileRp.CreatePhoneEnviroment;
import hyj.xw.common.CommonConstant;
import hyj.xw.common.FilePathCommon;
import hyj.xw.conf.PhoneConf;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.hook.newHook.NewPhoneInfo;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.modelHttp.Apk;
import hyj.xw.modelHttp.Device;
import hyj.xw.modelHttp.MaintainResultVO;
import hyj.xw.modelHttp.ResponseData;
import hyj.xw.service.HttpRequestService;
import hyj.xw.thread.CheckVersionCodeThread;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.ContactUtil;
import hyj.xw.util.DaoUtil;
import hyj.xw.util.FileUtil;
import hyj.xw.util.GetFutureResultUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.OkHttpUtil;

/**
 * Created by Administrator on 2018/8/23 0023.
 */

public class StartAutoTask extends Thread {

    public StartAutoTask(){
        init();
        System.out.println("doAction StartAutoTask init()");
    }

    String isLocalSettingValue ;
    HttpRequestService httpRequestService ;
    int loginIndex;
    List<Wx008Data> wx008Datas=null ;
    Device device = null;
    Wx008Data currentWx008Data=null;
    long activeTimeLength=0;
    String token;

    public void init(){
        token = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_WY_TOKEN);
        isLocalSettingValue = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_LOCAL_SETTING);
        httpRequestService = new HttpRequestService(1);
        loginIndex=0;
        activeTimeLength=0;
    }

    @Override
    public void run() {
        System.out.println("doAction StartAutoTask-->start========");
        //初始化配置信息
        String result = initDeviceConfig2Txt();
        if(!"".equals(result)) return;
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"retry");//next登录下一个，retry新登录,首次开启也是retry
        if("0".equals(isLocalSettingValue)){
            new StopThread().start();
        }
        StartUiautoThread startUiautoThread = new StartUiautoThread();
        StartChangeIpThread startChangeIpThread = new StartChangeIpThread();
        //delAllFile();
        while (true){
            try {
                AutoUtil.sleep(1000);
                device = getDeviceConfig();
                String tag = FileUtil.readAllUtf8(FilePathCommon.setEnviromentFilePath);
                if(device.getRefreshTime()!=null) activeTimeLength = System.currentTimeMillis() - device.getRefreshTime();
                System.out.println(Thread.currentThread().getName()+"main-->activeTimeLength:"+activeTimeLength+" currentSrc:"+ JSON.toJSONString(device)+" 当前tag:"+tag);
                if(device==null) continue;
                if(device.getRunState()!=1){
                    System.out.println("doAction--main--暂停或停止");
                    continue;
                }
                if(activeTimeLength>2*60000){//超过5分钟，重试
                    tag = "retry";
                    String msg = "doAction--->超时，超过2*60000，重置状态tag：retry";
                    System.out.println(msg);
                    LogUtil.log(msg);
                }
                /**
                 * 回写登录结果
                 */
                if(!TextUtils.isEmpty(device.getLoginResult())){
                    currentWx008Data.setExpMsg(device.getLoginResult());
                    //int cn = DaoUtil.updateExpMsg(currentWx008Data,currentWx008Data.getExpMsg()+"-"+AutoUtil.getCurrentDate());
                    //String recordTxt = loginIndex+" msg:"+currentWx008Data.getExpMsg()+" "+currentWx008Data.getPhone()+" "+currentWx008Data.getWxPwd()+" ip:"+device.getIpAddress();
                    //LogUtil.login("",recordTxt);
                    //System.out.println("main-->doAction--->main-->updateExpMsg:"+device.getLoginResult()+" cn:"+cn+" recordTxt:"+recordTxt);
                    System.out.println("main-->doAction--->main-->updateExpMsg:"+device.getLoginResult());
                    if("0".equals(isLocalSettingValue)){//服务器
                        if(device.getRunType()==2){
                            updateMaintainStatus();
                        }else if(device.getRunType()==1){
                            String loginResult = device.getLoginResult();
                            if("本次登录已失效".equals(loginResult)){
                                updateMaintainStatus();
                            }else {
                                System.out.println("main-->doAction--->zc完成："+loginResult);
                                String res1 = httpRequestService.updateRegStatus(currentWx008Data.getPhone(),loginResult);
                                System.out.println("main-->doAction--->更新手机注册状态res："+res1);
                            }
                        }
                    }
                    device.setLoginResult("");
                    saveDeviceConfig(device);
                }else if(!TextUtils.isEmpty(device.getWxid())&&currentWx008Data.getId()!=null){
                    if("0".equals(isLocalSettingValue)){
                        Wx008Data wx008Data = new Wx008Data();
                        wx008Data.setId(currentWx008Data.getId());
                        wx008Data.setWxid19(device.getWxid());
                        String json = JSON.toJSONString(wx008Data);
                        System.out.println("main-->doAction--->上传wxid json："+json);
                        String res = httpRequestService.uploadPhoneData(json);
                        System.out.println("main-->doAction--->上传wxid res："+res);
                        wx008Data = null;
                        json = null;
                    }
                    updateWxid(currentWx008Data,device);//更新wxid
                }else if("1".equals(device.getLastIpAddress())){
                    String ip = getIp();
                    device.setLastIpAddress(ip);
                    saveDeviceConfig(device);
                }else if(!TextUtils.isEmpty(device.getCallNumber())){
                    String res = httpRequestService.sendSms(device.getCallNumber(),device.getCalledNumber(),device.getContent());
                    System.out.println("main-->doAction--->发送短信返回res:"+res);
                    if(res.contains("提交成功")){
                        saveRegData2Server();
                    }
                    device.setCallNumber("");
                    saveDeviceConfig(device);
                }else if(device.getChangeIp()==2){
                    device.setChangeIp(1);
                    saveDeviceConfig(device);
                    startUiautoThread = null;
                    startUiautoThread = new StartUiautoThread();
                    startUiautoThread.start();
                    //new StartUiautoThread().start();
                }

                /**
                 * 获取008数据并 环境设置标志
                 */
                if("next".equals(tag)||"retry".equals(tag)){
                    currentWx008Data = null;
                    AutoUtil.killAndClearWxData();
                    String setWxDataResult = setWx008Data(tag);//获取008数据
                    if(!"".equals(setWxDataResult)||currentWx008Data==null){
                        System.out.println("doAction--->setWx008DataResult:"+setWxDataResult);
                        continue;
                    }
                    ContactUtil.deleteAll();//删除联系人
                    ContactUtil.createContactByNum();//随机生成联系人
                    if(device.getHookType()==2){
                        set008Environment(currentWx008Data);
                    }else {
                        setEnviroment(currentWx008Data);//修改hook文件
                    }
                    String currentWx008DataStr = JSON.toJSONString(currentWx008Data);
                    FileUtil.writeContent2FileForceUtf8(FilePathCommon.wx008DataFilePath,currentWx008DataStr);//写入008j数据，供对方用
                    currentWx008DataStr = null;
                    FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"done");
                    System.out.println("main-->doAction--->mainActivity环境和currentData已准备，写入done标志完成");
                    device.setRefreshTime(System.currentTimeMillis());
                    saveDeviceConfig(device);
                    File file = new File(FilePathCommon.downAPk2Path);//监测新版本更新
                    if(file.exists()){
                        installUiauto();
                    }
                    startChangeIpThread = null;
                    startChangeIpThread = new StartChangeIpThread();
                    startChangeIpThread.start();
                    //new StartChangeIpThread().start();
                }
                device = null;

            }catch (Exception e){
                System.out.println("main-->doAction-->全局异常 mainActivity");
                e.printStackTrace();
            }
        }
    }

    public void updateMaintainStatus(){
        MaintainResultVO maintainResultVO = createMaintainResult(currentWx008Data,device);
        String json = JSON.toJSONString(maintainResultVO);
        System.out.println("main-->doAction-->修改维护状态req:"+json);
        String res = httpRequestService.updateMaintainStatus(json);
        System.out.println("main-->doAction--->修改维护状态res："+res);
        maintainResultVO = null;
        json = null;
    }

    public void saveRegData2Server(){
        //String phoneStrs = FileUtil.readAllUtf8(FilePathCommon.device008TxtPath);
        currentWx008Data.setRegIp(device.getIpAddress());
        currentWx008Data.setId(null);
        //currentWx008Data.setPhoneStrs(phoneStrs);
        String json = JSON.toJSONString(currentWx008Data);
        System.out.println("main-->doAction--->发送短信成功上传数据currentWx008Data："+json);
        if(!TextUtils.isEmpty(currentWx008Data.getPhone())){
            String res = httpRequestService.uploadPhoneData(json);
            if(!"".equals(res)&&AutoUtil.isValidLong(res)){//返回更新成功id，update wxid用到
                currentWx008Data.setId(Long.parseLong(res));
            }
            System.out.println("main-->doAction--->发送短信成功上传数据res："+res);
            int cn = currentWx008Data.updateAll("phone=?",currentWx008Data.getPhone());
            System.out.println("main-->doAction--->更新phoneStrs到数据库："+cn);
        }
    }

    public String getIp(){
        String ipUrl = "http://pv.sohu.com/cityjson?ie=utf-8";
        String ipStr = null;
        try {
            ipStr = OkHttpUtil.okHttpGetByToken(ipUrl,token);
        } catch (IOException e) {
            AutoUtil.startAppByPackName("hyj.xw","hyj.xw.MainActivity");
            e.printStackTrace();
        }
        System.out.println("main-->doActioni--->res ipStr:"+ipStr);
        String ip = "失败";
        if(ipStr.contains("广东")){
            ip = "广东";
        }else if(ipStr.contains("cip")){
            JSONObject jsonObject = JSONObject.parseObject(ipStr.substring(ipStr.indexOf("{"),ipStr.indexOf("}")+1));
            ip = jsonObject.getString("cname")+jsonObject.getString("cip");
        }
        //updateDeviceConfigIp(ip);
        return ip;
    }

    public void updateWxid(Wx008Data currentWx008Data,Device device){
        ///currentWx008Data.setWxid19(device.getWxid());
        //int cn = currentWx008Data.updateAll("phone=?",currentWx008Data.getPhone());
        System.out.println("main-->doAction---> wxid:"+device.getWxid());
        device.setWxid("");
        saveDeviceConfig(device);
    }

    public MaintainResultVO createMaintainResult(Wx008Data currentWx008Data,Device device){
        String expMsg = device.getLoginResult();
        int dieFlag = 0;
        if(expMsg.contains("密码错误")){
            dieFlag = 1;
        }else if(expMsg.contains("帐号的使用存在异常")||expMsg.contains("系统检测到你的帐号有异常")){
            dieFlag = 2;
        }else if(expMsg.contains("操作频率过快")){
            dieFlag = 3;
        }else if(expMsg.contains("登录环境异常")){
            dieFlag = 4;
        }else if(expMsg.contains("新设备")){
            dieFlag = 5;
        }else if(expMsg.contains("外挂")){
            dieFlag = 6;
        }else if(expMsg.contains("长期未登录")||expMsg.contains("长期没有使用")){
            dieFlag = 7;
        }else if(expMsg.contains("该微信帐号因批量")){
            dieFlag = 8;
        }else if(expMsg.contains("本次登录已失效")){
            dieFlag = 9;
        }else if(expMsg.contains("已售")){
            dieFlag = 98;
        }else if(expMsg.contains("作废")){
            dieFlag = 99;
        }
        MaintainResultVO maintainResultVO = new MaintainResultVO(currentWx008Data.getId(),dieFlag,expMsg,device.getIpAddress());
        return maintainResultVO;
    }


    public String setWx008Data(String tag){
        String result = "";
        try {
            if(1==device.getRunType()){//注册
                if(tag.equals("next")||currentWx008Data==null){
                    String phone = httpRequestService.getPhone("");
                    if(TextUtils.isEmpty(phone)){
                        currentWx008Data =  null;
                        AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"获取手机号失败");
                        return "获取手机号失败";
                    }
                    if(device.getHookType()==2){//008改机方式
                        currentWx008Data = PhoneConf.createRegDataByPhoneAndDeviceTxt(phone); //008机型数据在发送短信成功后获取
                    }else {
                        System.out.println("main-->doAction--->生成内部改机数据");
                        currentWx008Data = PhoneConf.createRegDataByPhone(phone);
                    }
                    currentWx008Data.setRegDevice(device.getNum());
                    currentWx008Data.setRegHookType(device.getHookType());
                    currentWx008Data.save();
                    System.out.println("main-->doAction--->获取一份新改机wxData并保存");
                }
            }else if(2==device.getRunType()) {//养号
                if("1".equals(isLocalSettingValue)){
                    if(currentWx008Data==null){
                        loginIndex = Integer.parseInt(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX));
                        wx008Datas = DaoUtil.getWx008Datas();
                        currentWx008Data = wx008Datas.get(loginIndex);
                    }else if(tag.equals("next")){
                        loginIndex = loginIndex+1;
                        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_START_LOGIN_INDEX,loginIndex+"");
                        currentWx008Data = wx008Datas.get(loginIndex);
                    }
                    System.out.println("doAction--->获取本地维护数据:"+JSON.toJSONString(currentWx008Data));
                }else {
                    AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"获取维护数据失败");
                    currentWx008Data = httpRequestService.getMaintainData();
                    if(currentWx008Data==null) System.out.println("doAction--->获取维护数据失败 或 数据没有置入维护界面");
                    //System.out.println("doAction--->获取远程维护数据:"+JSON.toJSONString(currentWx008Data));
                    System.out.println("doAction--->获取远程维护数据成功");
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("doAction---Exception setWx008Data");
        }
        return result;
    }


    private void setEnviroment(Wx008Data currentWx008Data){
        NewPhoneInfo pi = null;
        if(!TextUtils.isEmpty(currentWx008Data.getPhoneStrsAw())){//aw数据
            pi = JSON.parseObject(currentWx008Data.getPhoneStrsAw(),NewPhoneInfo.class);
            if(TextUtils.isEmpty(pi.getRgPhoneNo())){
                pi.setRgPhoneNo(pi.getLine1Number());
            }
        }else {
            pi = PhoneConf.xw2awData(currentWx008Data);
        }
        pi.setCpuName(pi.getCpuName().trim().toLowerCase());
        CreatePhoneEnviroment.create(GlobalApplication.getContext(),pi);
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.baseAppPathAW,FilePathCommon.npiFileName, JSON.toJSONString(pi));
    }

    private void set008Environment(Wx008Data currentWx008Data){
        try {
            String data008Str = currentWx008Data.getPhoneStrs();//008原始数据
            if(!TextUtils.isEmpty(currentWx008Data.getPhoneStrsAw())&&currentWx008Data.getPhoneStrsAw().contains("androidId")){
                System.out.println("main-->doAction--->npi数据");
                data008Str = PhoneConf.phoneStr2008Str(currentWx008Data.getPhoneStrsAw());//内部改机数据转008原始数据
                //System.out.println("main-->doAction--->npi数据phoneStr2008Str："+data008Str);
            }
            File file = new File(FilePathCommon.device008TxtPath);
            if(file.exists()) file.delete();
            FileUtil.writeContent2FileForceUtf8(FilePathCommon.device008TxtPath,data008Str);
            //String strs = FileUtil.readAllUtf8(FilePathCommon.device008TxtPath);
            data008Str = null;
            //System.out.println("main-->doAction-->008 str strs:"+strs);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("main-->doAction---Exception set008Environment");
        }
    }

    public  Device getDeviceConfig(){
        String srConfigStr = FileUtil.readAllUtf8(FilePathCommon.startRunninConfigTxtPath);
        Device srConfig = JSONObject.parseObject(srConfigStr,Device.class);
        return srConfig;
    }
    public  void saveDeviceConfig(Device device){
        if(device!=null){
            String deviceStr =  JSON.toJSONString(device);
            FileUtil.writeContent2FileForceUtf8(FilePathCommon.startRunninConfigTxtPath,deviceStr);
            deviceStr = null;
        }
    }

    public void installUiauto(){
        System.out.println("doAction--->即将开始安装auto-------------------------");
        AutoUtil.execShell("cp /sdcard/hyj.autooperation.test /data/local/tmp/");
        AutoUtil.execShell("chmod 777 /data/local/tmp/hyj.autooperation.test");
        AutoUtil.execShell("pm install -r \"/data/local/tmp/hyj.autooperation.test\"");
        File file = new File(FilePathCommon.downAPk2Path);
        if(file.exists()) {
            boolean flag = file.delete();
            System.out.println("doAction--->删除auto："+flag);
        }
    }

    public String initDeviceConfig2Txt(){
        String isLocalSettingValue = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_LOCAL_SETTING);
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.fkFilePath,"");
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"");
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.startRunninConfigTxtPath,"");
        File file = new File(FilePathCommon.downAPk2Path);
        if(file.exists()) file.delete();
        String result = "";
        Device device = null;
        if("1".equals(isLocalSettingValue)){


        }else {
            //服务器获取配置
            String deviceNum = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_DEVICE);
            if(TextUtils.isEmpty(deviceNum)){
                result = "设备编号不能为空";
                AutoUtil.showToastByRunnable(GlobalApplication.getContext(),result);
                return result;
            }
            //HttpRequestService httpRequestService = new HttpRequestService();
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
            device.setHost("http://"+AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_HOST));
            device.setToken(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_WY_TOKEN));
            device.setUsername(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_USERNAME));
            device.setChangeIp(1);//重置状态 状态为2是修改ip
        }
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.stopTxtPath,device.getRunState()+"");//暂停标志 1、正常；2、暂停
        saveDeviceConfig(device);
        return result;
    }

    //开启uiauto线程类，ip修改完后触发
    class StartUiautoThread extends Thread{
        @Override
        public void run() {
            System.out.println("main--doAction-->开启StartUiautoThread");
            AutoUtil.execShell("am instrument -w -r   -e debug false -e class hyj.autooperation.ExampleInstrumentedTest#useAppContext hyj.autooperation.test/android.support.test.runner.AndroidJUnitRunner");
        }
    }
    //修改ip线程类，tag为next和retry时触发，轮训检测
   /* public void startChangeIpByShell(){
        AutoUtil.execShell("am instrument -w -r   -e debug false -e class hyj.autooperation.ExampleInstrumentedTest#changeIp hyj.autooperation.test/android.support.test.runner.AndroidJUnitRunner");
    }*/
    class StartChangeIpThread extends Thread{
        @Override
        public void run() {
            System.out.println("main--doAction-->StartChangeIpThread");
            AutoUtil.execShell("am instrument -w -r   -e debug false -e class hyj.autooperation.ExampleInstrumentedTest#changeIp hyj.autooperation.test/android.support.test.runner.AndroidJUnitRunner");
        }
    }

    class StopThread extends Thread{
        @Override
        public void run() {
            //HttpRequestService httpRequestService = new HttpRequestService();
            StartChangeIpThread startChangeIpThread;
            //String deviceNum = deviceEditText.getText().toString();
            String deviceNum = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_DEVICE);
            int coutDisNet=0;
            while (true){
                System.out.println("StopThread-->"+coutDisNet);
                AutoUtil.sleep(1500);
                try {
                    String res = httpRequestService.getStartConifgFromServer(deviceNum);
                    if("".equals(res)){
                        coutDisNet = coutDisNet+1;
                        if(coutDisNet>12){
                            coutDisNet = 0;
                            System.out.println("HttpRequestService getPhone--doAction-->连接异常飞行");
                            AutoUtil.execShell("svc wifi disable");
                            AutoUtil.sleep(5000);
                            AutoUtil.execShell("svc wifi enable");
                            AutoUtil.startAppByPackName("hyj.xw","hyj.xw.MainActivity");
                            AutoUtil.sleep(3000);
                            //startChangeIpByShell();
                            startChangeIpThread = null;
                            startChangeIpThread = new StartChangeIpThread();
                            startChangeIpThread.start();
                            //new StartChangeIpThread().start();
                        }
                        continue;
                    }
                    coutDisNet = 0;
                    ResponseData responseData = JSONObject.parseObject(res,ResponseData.class);
                    Device device = JSONObject.parseObject(responseData.getData(),Device.class);
                    System.out.println("StopThread-->runState:"+device.getRunState());
                    if(3==device.getRunState()){//停止
                        System.out.println("doAction--->停止am force-stop hyj.autooperation");
                        AutoUtil.execShell("am force-stop hyj.autooperation");
                        AutoUtil.execShell("am force-stop hyj.xw");
                        AutoUtil.sleep(2000);
                        continue;
                    }if(4==device.getRunState()){//重启手机
                        System.out.println("StopThread--->重启手机");
                        AutoUtil.execShell("reboot");
                    }
                    FileUtil.writeContent2FileForceUtf8(FilePathCommon.stopTxtPath,device.getRunState()+"");//暂停标志 1、正常；2、暂停
                    downloadAttach(false);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        public void downloadAttach(boolean isAlertWindow){
            Apk apk = checkVersion1("2");
            if(apk!=null){
                System.out.println("main--->发现新版本");
                DownLoadAPkListener downLoadAPkListener = new DownLoadAPkListener(GlobalApplication.getContext(),FilePathCommon.downAPk2Path,"2",apk);
                downLoadAPkListener.downloadAttach(isAlertWindow);
                AutoUtil.sleep(5000);
            }
        }

        public  Apk checkVersion1(String apkType){
            String uiautoVersionCodeStr = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_UIAUTO_VERSION);
            int currentVersionCode = Integer.parseInt(TextUtils.isEmpty(uiautoVersionCodeStr) ? "0" : uiautoVersionCodeStr);
            if("1".equals(apkType)){
                currentVersionCode = getVersion();
            }
            System.out.println("main--->currentVersionCode:"+currentVersionCode);
            String res = httpRequestService.checkUpdate(apkType,currentVersionCode);
            System.out.println("main--->checkUpdateRes:"+res);
            if("".equals(res)){
                AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"请求服务异常");
                System.out.println("main--->downloadAttach:请求服务异常");
                return null;
            }
            if(null==res){
                if("1".equals(apkType)){
                    AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"当前是最新版本");
                }
                System.out.println("main--->downloadAttach:当前是最新版本");
                return null;
            }
            if(!res.contains("versionCode")){
                AutoUtil.showToastByRunnable(GlobalApplication.getContext(),res);
                System.out.println("main--->downloadAttach:res");
                return null;
            }
            Apk apk = com.alibaba.fastjson.JSONObject.parseObject(res,Apk.class);
            return apk;
        }
        public  int getVersion() {
            try {
                PackageManager manager = GlobalApplication.getContext().getPackageManager();
                PackageInfo info = manager.getPackageInfo(GlobalApplication.getContext().getPackageName(),0);
                String version = info.versionName;
                int versioncode = info.versionCode;
                return versioncode;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    }


}
