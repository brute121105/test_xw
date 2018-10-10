package hyj.xw.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wx.wyyk.netty.commons.HelpLoginMsg;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hyj.xw.GlobalApplication;
import hyj.xw.GlobalValue;
import hyj.xw.aw.sysFileRp.CreatePhoneEnviroment;
import hyj.xw.common.CommonConstant;
import hyj.xw.common.FilePathCommon;
import hyj.xw.conf.PhoneConf;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.hook.Phone;
import hyj.xw.hook.newHook.NewPhoneInfo;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.modelHttp.Device;
import hyj.xw.modelHttp.MaintainResultVO;
import hyj.xw.task.StartAutoTask;
import hyj.xw.thread.AutoStopThread;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.ContactUtil;
import hyj.xw.util.DaoUtil;
import hyj.xw.util.FileUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.OkHttpUtil;
import hyj.xw.util.StringUtilHyj;

import static hyj.xw.util.OkHttpUtil.okHttpGet;



public class UiAutoReciver extends BroadcastReceiver {

    String isLocalSettingValue ;
    HttpRequestService httpRequestService ;
    int loginIndex;
    List<Wx008Data> wx008Datas=null ;
    //Device device = null;
    Wx008Data currentWx008Data=null;
    long activeTimeLength=0;
    String token=null;
    StartUiautoThread startUiautoThread = new StartUiautoThread();
    StartChangeIpThread startChangeIpThread  = new StartChangeIpThread();

    public void init(){
        token = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_WY_TOKEN);
        isLocalSettingValue = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_LOCAL_SETTING);
        httpRequestService = new HttpRequestService(1);
        loginIndex=0;
        activeTimeLength=0;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if(token==null){
            init();
        }
        System.out.println("UiAutoReciver doAction--->onReceive");
        final String tag = intent.getStringExtra("tag");
        final String param = intent.getStringExtra("param");
        final String callNumber = intent.getStringExtra("callNumber");
        final String calledNumber = intent.getStringExtra("calledNumber");
        final String smsContent = intent.getStringExtra("content");
        final String wxid = intent.getStringExtra("wxid");
        System.out.println("UiAutoReciver doAction-->tag:"+tag+" param:"+param+" callNumber:"+callNumber+" calledNumber:"+calledNumber+" smsContent:"+smsContent+ "wxid:"+wxid);

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){
                    try {
                        /**
                         * 发送短信
                         */
                        if(!TextUtils.isEmpty(callNumber)){
                            String wx008DataSstr = FileUtil.readAllUtf8(FilePathCommon.wx008DataFilePath);
                            Wx008Data currentWx008Data = JSON.parseObject(wx008DataSstr,Wx008Data.class);
                            if(currentWx008Data == null) continue;
                            String res = httpRequestService.sendSms(callNumber,calledNumber,smsContent);
                            System.out.println("UiAutoReciver main-->doAction--->发送短信返回res:"+res);
                            if(res.contains("提交成功")){
                                saveRegData2Server(currentWx008Data);
                            }
                            return;
                        }else if("getIp".equals(param)){//获取ip并告诉对方
                            Device device = getDeviceConfig();
                            if(device==null){
                                System.out.println("UiAutoReciver doAction--->getIp device device null");
                                AutoUtil.sleep(3000);
                                continue;
                            }
                            String ip = getIp();
                            device.setLastIpAddress(ip);
                            saveDeviceConfig(device);
                            return;
                        }else if("endChangeip".equals(param)){//收到对方修改ip完成，开启uiauto
                            Device device = getDeviceConfig();
                            if(device==null){
                                System.out.println("UiAutoReciver doAction--->getIp device device null");
                                AutoUtil.sleep(3000);
                                continue;
                            }

                            device.setChangeIp(1);
                            saveDeviceConfig(device);
                            startUiautoThread = null;
                            startUiautoThread = new StartUiautoThread();
                            startUiautoThread.start();
                            return;
                        } else if("addFriend".equals(param)){
                            String friends = "添加好友"+GlobalValue.data008Friends+"成功";
                            System.out.println("UiAutoReciver doAction--->添加好友GlobalValue.friends:"+friends);
                            String res = httpRequestService.setFriendsNull(GlobalValue.data008Id,friends);
                            System.out.println("UiAutoReciver doAction--->添加好友res:"+res);
                            return;
                        }else if(param!=null&&param.contains("updateMaintainResultVO")){
                            String maintainResultVOStr = FileUtil.readAllUtf8(FilePathCommon.loginMaintainResultTxt);
                            System.out.println("UiAutoReciver doAction--->读取MaintainResultVOStr:"+maintainResultVOStr);
                            MaintainResultVO maintainResultVO = JSONObject.parseObject(maintainResultVOStr,MaintainResultVO.class);
                            if(maintainResultVO!=null){
                                maintainResultVO.setIp(GlobalValue.ip);
                                maintainResultVO.setId(GlobalValue.data008Id);
                                if(GlobalValue.device.getRunType()==2){
                                    updateMaintain(maintainResultVO);
                                }else if(GlobalValue.device.getRunType()==1){
                                    if(maintainResultVO.getExpMsg()!=null&&maintainResultVO.getExpMsg().contains("本次登录已失效")){
                                        updateMaintain(maintainResultVO);
                                    }
                                    System.out.println("UiAutoReciver main-->doAction--->zc完成："+maintainResultVO.getExpMsg()+" GlobalValue.data008Phone:"+GlobalValue.data008Phone);
                                    String res1 = httpRequestService.updateRegStatus(GlobalValue.data008Phone,maintainResultVO.getExpMsg());
                                    System.out.println("UiAutoReciver main-->doAction--->更新手机注册状态res："+res1);
                                }
                            }

                            return;
                        }else if(param!=null&&param.contains("assistant")){
                            String[] strArr = param.split("-");
                            HelpLoginMsg helpLoginMsg = new HelpLoginMsg();
                            helpLoginMsg.setPhone(strArr[1]);
                            helpLoginMsg.setCode(strArr[2]);
                            System.out.println("UiAutoReciver doAction--->发送辅助验证req:"+JSON.toJSONString(helpLoginMsg));
                            String res = httpRequestService.helpLogin(JSON.toJSONString(helpLoginMsg));
                            System.out.println("UiAutoReciver doAction--->发送辅助验证res:"+res);
                            return;

                        }else if(param!=null&&param.contains("setNewPwdSucess")){
                            String[] strArr = param.split("-");
                            Wx008Data wx008Data = new Wx008Data();
                            wx008Data.setId(GlobalValue.data008Id);
                            wx008Data.setNewWxPwd(strArr[1]);
                            String json = JSON.toJSONString(wx008Data);
                            System.out.println("doAction--->req更新新密码："+json);
                            String res = httpRequestService.uploadPhoneData(json);
                            System.out.println("doAction--->res更新新密码："+res);
                            return;

                        }
                        else if(!TextUtils.isEmpty(wxid)){
                            Wx008Data wx008Data = new Wx008Data();
                            wx008Data.setId(GlobalValue.data008Id);
                            wx008Data.setWxid19(wxid);
                            String json = JSON.toJSONString(wx008Data);
                            System.out.println("UiAutoReciver main-->doAction--->上传wxid json："+json);
                            String res = httpRequestService.uploadPhoneData(json);
                            System.out.println("UiAutoReciver main-->doAction--->上传wxid res："+res);
                            return;
                        }else if("next".equals(tag)||"retry".equals(tag)){
                            refreshUiautoReveiverTime();
                            File file = new File(FilePathCommon.downAPk2Path);//监测新版本更新
                            if(file.exists()){
                                System.out.println("UiAutoReciver main-->doAction--->存在新版本文件");
                                installUiauto();
                            }else {
                                System.out.println("UiAutoReciver main-->doAction--->不存在新版本文件");
                            }

                            currentWx008Data = null;
                            String setWxDataResult = setWx008Data(tag);//获取008数据,如果辅助登录，修改远程数据
                            if(!"".equals(setWxDataResult)||currentWx008Data==null){
                                System.out.println("UiAutoReciver doAction--->获取008数据为空  setWx008DataResult:"+setWxDataResult+" 休眠10秒，停止autooperation 启动hyj.xw");
                                AutoUtil.execShell("am force-stop hyj.autooperation");
                                AutoUtil.startAppByPackName("hyj.xw","hyj.xw.MainActivity");
                                AutoUtil.sleep(3000);
                                AutoUtil.showToastByRunnable(GlobalApplication.getContext(),setWxDataResult);
                                AutoUtil.sleep(5000);
                                continue;
                            }
                            AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"关闭、清除数据");
                            AutoUtil.killAndClearWxData();

                            System.out.println("UiAutoReciver doAction--->删除联系人");
                            AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"删除联系人");
                            ContactUtil.deleteAll();//删除联系人
                            System.out.println("UiAutoReciver doAction--->随机生成联系人");
                            AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"随机生成联系人");
                            ContactUtil.createContactByNum();//随机生成联系人


                            AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"设置手机环境setEnviroment");
                            System.out.println("UiAutoReciver doAction--->设置手机环境setEnviroment hookType:"+GlobalValue.device.getHookType());
                            if(GlobalValue.device.getHookType()==2){
                                set008Environment(currentWx008Data);
                            }else if(GlobalValue.device.getHookType()==3){

                            }else if(GlobalValue.device.getHookType()==1){
                                setEnviroment(currentWx008Data);//修改hook文件
                            }
                            String currentWx008DataStr = JSON.toJSONString(currentWx008Data);
                            FileUtil.writeContent2FileForceUtf8(FilePathCommon.wx008DataFilePath,currentWx008DataStr);//写入008j数据，供对方用
                            FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"done");
                            saveDeviceConfig(GlobalValue.getDeviceAndSomeParams());
                            System.out.println("UiAutoReciver main-->doAction--->currentWx008Data对象、device对象、环境done标志写入文件");
                            startChangeIpThread = null;
                            startChangeIpThread = new StartChangeIpThread();
                            startChangeIpThread.start();
                            return;
                        }
                    }catch (Exception e){
                        System.out.println("doActino--->UiAutoReciver全局异常");
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    //每30s刷新一次，记录活跃状态
    public void refreshUiautoReveiverTime(){
        long currentTime = System.currentTimeMillis();
        if(GlobalValue.uiautoReveiverRefreshTime==null||currentTime-GlobalValue.uiautoReveiverRefreshTime>30000){
            System.out.println("UiAutoReciver doAction--->30s刷新uiautoReveiverRefreshTime时间");
            GlobalValue.uiautoReveiverRefreshTime = currentTime;
        }
    }

    public void updateMaintain(MaintainResultVO maintainResultVO){
        String json = JSON.toJSONString(maintainResultVO);
        System.out.println("UiAutoReciver main-->doAction-->updateMaintain修改维护状态req:"+json);
        String res = httpRequestService.updateMaintainStatus(json);
        System.out.println("UiAutoReciver main-->doAction--->updateMaintain修改维护状态res："+res);
    }

    /*public void updateMaintainStatus(){
        MaintainResultVO maintainResultVO = createMaintainResult(device);
        String json = JSON.toJSONString(maintainResultVO);
        System.out.println("UiAutoReciver main-->doAction-->修改维护状态req:"+json);
        String res = httpRequestService.updateMaintainStatus(json);
        System.out.println("UiAutoReciver main-->doAction--->修改维护状态res："+res);
        maintainResultVO = null;
        json = null;
    }
*/
    public void saveRegData2Server(Wx008Data currentWx008Data){
        //String phoneStrs = FileUtil.readAllUtf8(FilePathCommon.device008TxtPath);
        currentWx008Data.setRegIp(GlobalValue.ip);
        currentWx008Data.setId(null);
        //currentWx008Data.setPhoneStrs(phoneStrs);
        String json = JSON.toJSONString(currentWx008Data);
        System.out.println("UiAutoReciver main-->doAction--->发送短信成功上传数据currentWx008Data："+json);
        if(!TextUtils.isEmpty(currentWx008Data.getPhone())){
            String res = httpRequestService.uploadPhoneData(json);
            if(!"".equals(res)&&AutoUtil.isValidLong(res)){//返回更新成功id，update wxid用到
                GlobalValue.data008Id = Long.parseLong(res);
                currentWx008Data.setId(Long.parseLong(res));
                String currentWx008DataStr = JSON.toJSONString(currentWx008Data);
                FileUtil.writeContent2FileForceUtf8(FilePathCommon.wx008DataFilePath,currentWx008DataStr);//写入008j数据，供对方用
                currentWx008DataStr = null;
            }
            System.out.println("UiAutoReciver main-->doAction--->发送短信成功上传数据res："+res);
            //int cn = currentWx008Data.updateAll("phone=?",currentWx008Data.getPhone());
            //System.out.println("main-->doAction--->更新phoneStrs到数据库："+cn);
        }
    }

    public String getIp(){
        String ipUrl = "http://pv.sohu.com/cityjson?ie=utf-8";
        String ipStr = "";
        try {
            ipStr = OkHttpUtil.okHttpGetByToken(ipUrl,token);
        } catch (IOException e) {
            System.out.println("doAction--->uiautoReceiver getIp异常，启动hyj.xw");
            AutoUtil.startAppByPackName("hyj.xw","hyj.xw.MainActivity");
            e.printStackTrace();
        }
        System.out.println("UiAutoReciver main-->doActioni--->res ipStr:"+ipStr);
        String ip = "失败";
        if(ipStr.contains("广西")){
            ip = "广西";
        }else if(ipStr.contains("cip")){
            JSONObject jsonObject = JSONObject.parseObject(ipStr.substring(ipStr.indexOf("{"),ipStr.indexOf("}")+1));
            ip = jsonObject.getString("cname")+jsonObject.getString("cip");
        }
        GlobalValue.ip = ip;
        //updateDeviceConfigIp(ip);
        return ip;
    }

    public void updateWxid(Wx008Data currentWx008Data,Device device){
        ///currentWx008Data.setWxid19(device.getWxid());
        //int cn = currentWx008Data.updateAll("phone=?",currentWx008Data.getPhone());
        System.out.println("UiAutoReciver main-->doAction---> wxid:"+device.getWxid());
        device.setWxid("");
        saveDeviceConfig(device);
    }

    /*public MaintainResultVO createMaintainResult(Device device){
        String expMsg = device.getLoginResult();
        System.out.println("UiAutoReciver doAction--->gloabal ip:"+GlobalValue.ip+" gloabal data008Id:"+GlobalValue.data008Id);
        //MaintainResultVO maintainResultVO = new MaintainResultVO(currentWx008Data.getId(),device.getDieFlag(),expMsg,device.getIpAddress());
        MaintainResultVO maintainResultVO = new MaintainResultVO(GlobalValue.data008Id,device.getDieFlag(),expMsg,GlobalValue.ip);
        return maintainResultVO;
    }*/

    public void installUiauto(){
        System.out.println("doAction--->即将开始安装auto-------------------------");
        AutoUtil.execShell("cp /sdcard/hyj.autooperation.test /data/local/tmp/");
        AutoUtil.execShell("chmod 777 /data/local/tmp/hyj.autooperation.test");
        AutoUtil.execShell("pm install -r \"/data/local/tmp/hyj.autooperation.test\"");
        File file = new File(FilePathCommon.downAPk2Path);
        if(file.exists()) {
            GlobalValue.isHaveNewAttach = false;
            boolean flag = file.delete();
            System.out.println("UiAutoReciver doAction--->删除auto："+flag);
        }
    }

    public String setWx008Data(String tag){
        String result = "";
        try {
            System.out.println("doAction-->setWx008Data GlobalValue.device:"+JSON.toJSONString(GlobalValue.device));
            if(GlobalValue.device==null){
                AutoStopThread stopThread = AutoStopThread.getInstance();
                //stopThread.clearInstance();
                //stopThread = AutoStopThread.getInstance();
                stopThread.start();
                AutoUtil.sleep(5000);
                System.out.println("doAction--->GlobalValue.device is null,restart AutoStopThread");
                return "GlobalValue.device is null";
            }
            if(1==GlobalValue.device.getRunType()){//注册
                if(tag.equals("next")||currentWx008Data==null){
                    String phone = httpRequestService.getPhone("");
                    if(TextUtils.isEmpty(phone)){
                        currentWx008Data =  null;
                        return "获取手机号失败";
                    }
                    GlobalValue.data008Phone = phone;
                    if(GlobalValue.device.getHookType()==2){//008改机方式
                        currentWx008Data = PhoneConf.createRegDataByPhoneAndDeviceTxt(phone); //008机型数据在发送短信成功后获取
                    }else if(GlobalValue.device.getHookType()==3){//变色龙
                        FileUtil.writeContent2FileForceUtf8(FilePathCommon.bslStrTxt,"");
                        AutoUtil.startAppByShell("app.aptx.chamelemon/.activity.LaunchActivity");
                        String url = "http://localhost:1888/cmd?fun=usercreate";
                        String res = okHttpGet(url);
                        System.out.println("UiAutoReciver main-->doAction--->请求变色龙改机res："+res);
                        if(res.contains("true")){
                            String bslStr = FileUtil.readAllUtf8(FilePathCommon.bslStrTxt);
                            System.out.println("UiAutoReciver main-->doAction--->读取变色龙str："+bslStr);
                            if("".equals(bslStr))  return "变色龙改机失败";
                            currentWx008Data = PhoneConf.createRegDataByBslStrt(phone,bslStr);
                        }
                        System.out.println("doAction--->请求变色龙新机res:"+res);
                    }else if(GlobalValue.device.getHookType()==1){
                        System.out.println("UiAutoReciver main-->doAction--->生成内部改机数据");
                        currentWx008Data = PhoneConf.createRegDataByPhone(phone);
                    }
                    currentWx008Data.setRegDevice(GlobalValue.device.getNum());
                    currentWx008Data.setRegHookType(GlobalValue.device.getHookType());
                    //currentWx008Data.save();
                    System.out.println("UiAutoReciver main-->doAction--->获取一份新改机wxData并保存");
                }
            }else if(2==GlobalValue.device.getRunType()) {//养号
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
                    System.out.println("UiAutoReciver doAction--->获取本地维护数据:"+JSON.toJSONString(currentWx008Data));
                }else {
                    currentWx008Data = httpRequestService.getMaintainData();
                    if(currentWx008Data==null){
                        result = "获取维护数据失败 或 数据没有置入维护界面";
                        System.out.println("UiAutoReciver doAction--->"+result);
                    }else {
                        GlobalValue.data008Id = currentWx008Data.getId();
                        GlobalValue.data008Friends = currentWx008Data.getFriends();
                        System.out.println("doAction--->获取远程维护数据:"+JSON.toJSONString(currentWx008Data));
                        System.out.println("UiAutoReciver doAction--->获取远程维护数据成功");

                        //如果是辅助登录 && 没有生成新008数据,生成一份新的改机数据，并修改远程
                        if(GlobalValue.device.getAssistant()==2&&TextUtils.isEmpty(currentWx008Data.getPhoneStrs())){
                            Wx008Data newCurrentWx008Data = PhoneConf.createRegDataByPhoneAndDeviceTxt(currentWx008Data.getPhone());
                            newCurrentWx008Data.setCreateTime(null);
                            newCurrentWx008Data.setId(currentWx008Data.getId());
                            newCurrentWx008Data.setPhone(currentWx008Data.getPhone());
                            newCurrentWx008Data.setWxPwd(currentWx008Data.getWxPwd());
                            newCurrentWx008Data.setWxId(currentWx008Data.getWxId());
                            newCurrentWx008Data.setWxid19(currentWx008Data.getWxid19());
                            newCurrentWx008Data.setNickName(currentWx008Data.getNickName());
                            if(currentWx008Data.getPhoneStrs()!=null&&currentWx008Data.getPhoneStrs().contains("getString")){
                                newCurrentWx008Data.setPhoneStrs(currentWx008Data.getPhoneStrs());
                            }
                            String res = httpRequestService.uploadPhoneData(JSON.toJSONString(newCurrentWx008Data));
                            System.out.println("doAction--->辅助登录,修改远程数据res："+res);
                            if(!"".equals(res)&&AutoUtil.isValidLong(res)){
                                currentWx008Data = newCurrentWx008Data;
                                System.out.println("doAction--->辅助登录,生成一份新的改机数据，修改远程数据成功 newCurrentWx008Data:"+JSON.toJSONString(currentWx008Data));
                            }else {
                                result = "辅助登录,生成一份新的改机数据，修改远程数据失败";
                            }
                        }else {
                            System.out.println("doAction---->不是辅助，没有生成新数据 currentWx008Data.getPhoneStrs()："+currentWx008Data.getPhoneStrs());
                        }

                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("UiAutoReciver doAction---Exception setWx008Data");
        }
        return result;
    }

    private void setEnviroment(Wx008Data currentWx008Data){
        System.out.println("doAction--->setEnviroment内部改机");
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
                System.out.println("UiAutoReciver main-->doAction--->npi数据");
                data008Str = PhoneConf.phoneStr2008Str(currentWx008Data.getPhoneStrsAw());//内部改机数据转008原始数据
                //System.out.println("main-->doAction--->npi数据phoneStr2008Str："+data008Str);
            }
            File file = new File(FilePathCommon.device008TxtPath);
            if(file.exists()) file.delete();
            FileUtil.writeContent2FileForceUtf8(FilePathCommon.device008TxtPath,data008Str);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("UiAutoReciver main-->doAction---Exception set008Environment");
        }
    }
    public  Device getDeviceConfig(){
        try {
            String srConfigStr = FileUtil.readAllUtf8(FilePathCommon.startRunninConfigTxtPath);
            if(TextUtils.isEmpty(srConfigStr)) return null;
            Device srConfig = JSONObject.parseObject(srConfigStr,Device.class);
            return srConfig;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("UiAutoReciver main-->doAction---getDeviceConfig exception");
        }
        return null;
    }
    public  void saveDeviceConfig(Device device){
        if(device!=null){
            String deviceStr =  JSON.toJSONString(device);
            FileUtil.writeContent2FileForceUtf8(FilePathCommon.startRunninConfigTxtPath,deviceStr);
            deviceStr = null;
        }
    }

    class StartChangeIpThread extends Thread{
        @Override
        public void run() {
            System.out.println("UiAutoReciver main--doAction-->StartChangeIpThread");
            AutoUtil.execShell("am instrument -w -r   -e debug false -e class hyj.autooperation.ExampleInstrumentedTest#changeIp hyj.autooperation.test/android.support.test.runner.AndroidJUnitRunner");
        }
    }

    //开启uiauto线程类，ip修改完后触发
    class StartUiautoThread extends Thread{
        @Override
        public void run() {
            System.out.println("UiAutoReciver StartUiautoThread--doAction-->开启StartUiautoThread");
            AutoUtil.execShell("am instrument -w -r   -e debug false -e class hyj.autooperation.ExampleInstrumentedTest#useAppContext hyj.autooperation.test/android.support.test.runner.AndroidJUnitRunner");
        }
    }
}
