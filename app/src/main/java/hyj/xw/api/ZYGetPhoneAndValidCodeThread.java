package hyj.xw.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.model.PhoneApi;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.OkHttpUtil;


/**
 * Created by Administrator on 2017/8/15.
 */

public class ZYGetPhoneAndValidCodeThread implements Runnable{
    PhoneApi pa;
    public ZYGetPhoneAndValidCodeThread(PhoneApi pa){
        this.pa = pa;
        init();
    }

    String apiId,pwd,pjId;
    private void init(){
        apiId = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_ID);
        pwd = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_PWD);
        pjId = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_PROJECT_ID);
        pa.setApiId(apiId);
        pa.setPwd(pwd);
        pa.setPjId(pjId);
    }
    @Override
    public void run() {
        while (true){
            try {
            AutoUtil.sleep(5000);
            LogUtil.d("ZYGetPhoneAndValidCodeThread","【-->ZYGetPhoneAndValidCodeThread获取号码线程运行...】"+Thread.currentThread().getName()+" phone:"+pa.isPhoneIsAvailavle()+" validCode:"+pa.isValidCodeIsAvailavle()+" isSendMsg:"+pa.isSendMsg());
            if(pa.getToken()==null){
                String token = login(pa.getApiId(),pa.getPwd());
                pa.setToken(token);
                LogUtil.d("ZYGetPhoneAndValidCodeThread","token获取成功："+token);
                cancelAllRecv(token);
            }
            if(pa.getToken()!=null){
                if(!pa.isPhoneIsAvailavle()){
                    JSONObject phoneObj = getPhone(pa.getToken(),pa.getPjId());
                    String phone = phoneObj.getString("Phone");
                    String MSGID = phoneObj.getString("MSGID");
                    LogUtil.d("ZYGetPhoneAndValidCodeThread","phoneBody:"+phone);
                    if(phone.matches("[\\d]{4,20}")){
                        pa.setPhoneId(MSGID);
                        pa.setPhone(phone);
                        pa.setPhoneIsAvailavle(true);
                    }
                }

               if(pa.isPhoneIsAvailavle()&&!pa.isValidCodeIsAvailavle()){
                    String validCode = getValidCode(pa.getApiId(),pa.getPhoneId(),pa.getToken(),pa.getPjId());
                    LogUtil.d("ZYGetPhoneAndValidCodeThread","validCodeBody："+validCode);
                    if(validCode.matches("[\\d]{4,8}")){
                        pa.setValidCode(validCode);
                        pa.setValidCodeIsAvailavle(true);
                    }
                }

                //释放手机号码
                if(pa.getReleasPhone()!=null){
                    LogUtil.d("ZYGetPhoneAndValidCodeThread","【释放手机号】："+pa.getReleasPhone());
                    releasePhone(pa.getToken(),pa.getReleasPhone());
                    pa.setReleasPhone(null);
                }

                /*//发送短信
                if(pa.isSendMsg()){
                    String status = phoneService.sendMsg(pa.getToken(),pa.getPhone(),pa.getMsg());
                    pa.setStatus(status);
                    pa.setSendMsg(false);
                }
               //获取短信发送状态
                if(pa.getStatus()!=null){
                    String body = phoneService.getSendStatus(pa.getPjId(),pa.getToken(),pa.getPjId(),pa.getPhone(),pa.getStatus());
                    if("succ".equals(body)){
                        pa.setStatus(null);
                    }
                }*/

            }

            }catch (Exception e){
                System.out.println("----Thread error");
                e.printStackTrace();
            }
        }
    }

    //针对吸吸码蝗
    public String cancelAllRecv(String token){
        String url ="http://zhiyuan.quanhuini.com/AllRelease?Token="+token;
        LogUtil.d("ZYGetPhoneAndValidCodeThread cancelAllRecvUrl",url);
        String cancelAllRecvBody = null;
        try {
            cancelAllRecvBody = OkHttpUtil.okHttpGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.d("ZYGetPhoneAndValidCodeThread cancelAllRecvBody",cancelAllRecvBody);
        return cancelAllRecvBody;
    }

    private void releasePhone(String token,String phone){
        String url  = "http://zhiyuan.quanhuini.com/ReleasePhone?Token="+token+"&MSGID="+phone;
        LogUtil.d("ZYGetPhoneAndValidCodeThread releasePhone",url);
        String releasePhoneBody = null;
        try {
            releasePhoneBody = OkHttpUtil.okHttpGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.d("ZYGetPhoneAndValidCodeThread releasePhone",releasePhoneBody);
    }

    public String login(String apiId,String pwd) throws IOException {
        String token = "";
        String url ="http://zhiyuan.quanhuini.com/Login?User="+apiId+"&Password="+pwd+"&Logintype=0";
        LogUtil.d(" ZYGetPhoneAndValidCodeThread loginBody url",url);
        String body = OkHttpUtil.okHttpGet(url);
        LogUtil.d(" ZYGetPhoneAndValidCodeThread loginBody",body);
        JSONObject jb = JSON.parseObject(body);
        if("0".equals(jb.getString("code"))){
            token = jb.getJSONObject("data").getString("Token");

        }
        return token;
    }


    public JSONObject getPhone( String token, String pjId){

        JSONObject phone = null;
        String url = "http://zhiyuan.quanhuini.com/GetPhoneNumber?Token="+token+"&ItemId="+pjId+"&Phone=&Operator=0&Developer=brute121105";
        String phones = null;
        try {
            phones = OkHttpUtil.okHttpGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("-->hyj ZYGetPhoneAndValidCodeThread getPhone url-->"+url);
        System.out.println("-->hyj ZYGetPhoneAndValidCodeThread phoneBody-->"+phones);
        //LogUtil.d("phoneBody",phones);
        JSONObject jb = JSON.parseObject(phones);
        if("0".equals(jb.getString("code"))){
            phone = jb.getJSONObject("data");

        }
        System.out.println("-->hyj ZYGetPhoneAndValidCodeThread getPhone-->"+phone);
        return phone;
    }
    public String getValidCode(String apiId,String phone,String token,String pjId){
        String validCode = "";
        String veryCodeBody = null;
        try {
            veryCodeBody = OkHttpUtil.okHttpGet("http://zhiyuan.quanhuini.com/GetMessage?Token="+token+"&MSGID="+phone);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(" ZYGetPhoneAndValidCodeThread hyj veryCodeBody-->"+veryCodeBody);
        JSONObject jb = JSON.parseObject(veryCodeBody);
        if("0".equals(jb.getString("code"))){
            phone = jb.getString("data");
            System.out.println("hyj veryCodeBody data-->"+phone);
            validCode = regString(phone,"[\\d]{4,10}",0);
            System.out.println("ZYGetPhoneAndValidCodeThread hyj veryCodeBody code-->"+phone);
        }
        System.out.println("ZYGetPhoneAndValidCodeThread hyj veryCodeBody-->"+veryCodeBody);

        return validCode;
    }

    public static Matcher createMatcher(String matchStr, String reg) {
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(matchStr);
        return m;
    }

    public static String regString(String str, String reg, int groupNum) {
        String resultString = "";
        Matcher m = createMatcher(str, reg);
        if (m.find()) {
            resultString = m.group(groupNum);
        }
        return resultString;
    }
}
