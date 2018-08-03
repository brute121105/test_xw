package hyj.xw.api;


import java.io.IOException;

import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.model.PhoneApi;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.OkHttpUtil;

/**
 * Created by Administrator on 2017/8/15.
 */

public class GetPhoneAndValidCodeThread implements Runnable{
    PhoneApi pa;
    public GetPhoneAndValidCodeThread(PhoneApi pa){
        this.pa = pa;
        init();
    }
    String apiId,pwd,pjId,ext;
    private void init(){
        apiId = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_ID);
        pwd = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_PWD);
        pjId = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_PROJECT_ID);
        ext = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_EXT);
        pa.setApiId(apiId);
        pa.setPwd(pwd);
        pa.setPjId(pjId);
        //AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_API_TOKEN,"9c4e0607f85794e704b84ad10fd07691");
        //解绑手机号默认先从数据库取信息
        pa.setToken(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_TOKEN));
        pa.setPhone(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_PHONEE));
        pa.setValidCode(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_PHONE_CODE));
        if("6051".equals(ext)){//6051重新登录 6050取数据库
            pa.setToken(null);
        }
        pa.setPhoneIsAvailavle(false);
        pa.setValidCodeIsAvailavle(false);
        LogUtil.d("GetPhoneAndValidCodeThread","apiId:"+apiId+" pwd:"+pwd+" pjId:"+pjId);
    }
    @Override
    public void run() {
        while (true){
            try {
            AutoUtil.sleep(4000);
            LogUtil.d("GetPhoneAndValidCodeThread","-->获取号码线程运行..."+Thread.currentThread().getName()+" phone:"+pa.isPhoneIsAvailavle()+" validCode:"+pa.isValidCodeIsAvailavle()+" isSendMsg:"+pa.isSendMsg());
            if(pa.getToken()==null){
                /*pa.setApiId("52922-akx");
                pa.setPwd("aa105105");
                pa.setPjId("1296");*/
                String token = login(pa.getApiId(),pa.getPwd());
                if(token.length()==32){
                    pa.setToken(token);
                    AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_API_TOKEN,token);
                    LogUtil.d("GetPhoneAndValidCodeThread","token获取成功："+token);
                }else {
                    LogUtil.d("GetPhoneAndValidCodeThread","token获取失败："+token);
                }
            }
            if(pa.getToken()!=null&&!pa.getToken().contains("error")){
                if(!pa.isPhoneIsAvailavle()){
                    String phone = getPhone(pa.getApiId(),pa.getToken(),pa.getPjId());
                    LogUtil.d("GetPhoneAndValidCodeThread","phoneBody:"+phone);
                    if(phone.matches("[\\d]{11}")){
                        pa.setPhone(phone);
                        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_API_PHONEE,phone);//持久化
                        pa.setPhoneIsAvailavle(true);
                    }
                }

               if(pa.isPhoneIsAvailavle()&&!pa.isValidCodeIsAvailavle()){
                    String validCode = getValidCode(pa.getApiId(),pa.getPhone(),pa.getToken(),pa.getPjId());
                    LogUtil.d("GetPhoneAndValidCodeThread","validCodeBody："+validCode);
                    if(validCode.matches("[\\d]{4,8}")){
                        pa.setValidCode(validCode);
                        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_API_PHONE_CODE,validCode);//持久化
                        pa.setValidCodeIsAvailavle(true);
                    }
                }
                /*//发送短信
                if(pa.isSendMsg()){
                    String status = sendMsg(pa.getApiId(),pa.getToken(),pa.getPjId(),pa.getPhone(),pa.getMsg(),"5");
                    pa.setStatus(status);
                    pa.setSendMsg(false);
                }
                //获取短信发送状态
                if(pa.getStatus()!=null){
                    String body = getSendStatus(pa.getPjId(),pa.getToken(),pa.getPjId(),pa.getPhone(),pa.getStatus());
                    if("succ".equals(body)){
                        pa.setStatus(null);
                    }
                }*/

            }
            }catch (Exception e){
                LogUtil.logError(e);
            }
        }

    }

    public String login(String apiId,String pwd){
        String token = "";
        String url = "http://api.jyzszp.com/Api/index/userlogin?uid="+apiId+"&pwd="+pwd;
        String body = null;
        try {
            body = OkHttpUtil.okHttpGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("GetPhoneAndValidCodeThread login url hyj-->"+url);
        System.out.println("GetPhoneAndValidCodeThread login body hyj-->"+body);
        String[] strs = body.split("\\|");
        if(strs.length==3&&strs[1].equals(apiId)){
            token = strs[2];
        }
        return token;
    }
    public String getPhone(String apiId,String token,String pjId){
        String phone = "";
        String phones = null;
        try {
            phones = OkHttpUtil.okHttpGet("http://api.jyzszp.com/Api/index/getMobilenum?pid="+pjId+"&uid="+apiId+"&token="+token+"&mobile=&size=1");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("GetPhoneAndValidCodeThread hyj--phoneBody hyj-->"+phones);
        //LogUtil.d("phoneBody",phones);
        String[] strs = phones.split("\\|");
        if(strs!=null&&strs.length==2&&strs[0].matches("[\\d]{11}")){
            phone = strs[0];
        }
        return phone;
    }
    public  String sendMsg(String apiId,String token,String pjid,String phoneNum,String msg,String devId){
        String status = "";
        String url = "http://api.jyzszp.com/Api/index/sendSms?uid="+apiId+"&token="+token+"&pid="+pjid+"&mobile="+phoneNum+"&content="+msg+"&author_uid="+devId;
        System.out.println("sendMsg url hyj-->"+url);
        String body = null;
        try {
            body = OkHttpUtil.okHttpGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("GetPhoneAndValidCodeThread hyj sendMsg body hyj-->"+body);
        if(body.indexOf("succ")>-1){
            status = body.substring(body.indexOf("|")+1);
        }
        System.out.println("GetPhoneAndValidCodeThread hyj sendMsg status hyj-->"+status);
        return status;
    }
    public String getSendStatus(String apiId,String token,String pjid,String phoneNum,String status){
        String url= "http://api.jyzszp.com/Api/index/getSmsStatus?uid="+apiId+"&token="+token+"&pid="+pjid+"&mobile="+phoneNum+"&id="+status;
        System.out.println("hyj getSendStatus url hyj-->"+url);
        String body = null;
        try {
            body = OkHttpUtil.okHttpGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("hyj getSendStatus body hyj-->"+body);
        return body;
    }
    public String getValidCode(String apiId,String phone,String token,String pjId){
        String validCode = "";
        String url = "http://api.jyzszp.com/Api/index/getVcodeAndReleaseMobile?uid="+apiId+"&token="+token+"&mobile="+phone+"&pid="+pjId;
        LogUtil.d("GetPhoneAndValidCodeThread getValidCode url",url);
        String veryCodeBody = null;
        try {
            veryCodeBody = OkHttpUtil.okHttpGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.d("GetPhoneAndValidCodeThread veryCodeBody",veryCodeBody);
        String[] codeStr = veryCodeBody.split("\\|");
        if(codeStr!=null&&codeStr.length==3&&codeStr[0].matches("[\\d]{11}")){
            validCode = veryCodeBody.split("\\|")[1];
        }else{
            System.out.println("GetPhoneAndValidCodeThread hyj veryCodeBod:y"+veryCodeBody);
        }
        return validCode;
    }
}
