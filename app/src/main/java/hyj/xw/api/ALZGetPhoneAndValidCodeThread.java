package hyj.xw.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hyj.xw.BaseThread;
import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.model.PhoneApi;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.OkHttpUtil;

/**
 * Created by Administrator on 2017/8/15.
 */

public class ALZGetPhoneAndValidCodeThread extends BaseThread{
    public  final String TAG = this.getClass().getSimpleName();
    PhoneApi pa;
    public ALZGetPhoneAndValidCodeThread(PhoneApi pa){
        this.pa = pa;
        init();
    }
    String apiId,pwd,pjId;
    private void init(){
        apiId = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_ID);
        pwd = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_PWD);
        pjId = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_PROJECT_ID);
    }

    @Override
    public Object call() {
        while (true){
            AutoUtil.sleep(4000);
            LogUtil.d(TAG,"-->获取号码线程运行..."+Thread.currentThread().getName()+" phone:"+pa.isPhoneIsAvailavle()+" validCode:"+pa.isValidCodeIsAvailavle()+" isSendMsg:"+pa.isSendMsg());
            if(pa.getToken()==null){
                String token = login(apiId,pwd);
                pa.setToken(token);
                LogUtil.d(TAG,"token获取成功："+token);
            }
            if(pa.getToken()!=null){
                if(!pa.isPhoneIsAvailavle()){
                    String phone = getPhone(apiId,pa.getToken(),pjId);
                    LogUtil.d(TAG,"phoneBody:"+phone);
                    if(phone.matches("[\\d]{11}")){
                        pa.setPhone(phone);
                        pa.setPhoneIsAvailavle(true);
                    }
                }

               if(pa.isPhoneIsAvailavle()&&!pa.isValidCodeIsAvailavle()){
                    String validCode = getValidCode(pa.getApiId(),pa.getPhone(),pa.getToken(),pa.getPjId());
                    LogUtil.d(TAG,"validCodeBody："+validCode);
                    if(validCode.matches("[\\d]{4,8}")){
                        pa.setValidCode(validCode);
                        pa.setValidCodeIsAvailavle(true);
                    }
                }
               /* //发送短信
                if(pa.isSendMsg()){
                    String status = phoneService.sendMsg(pa.getApiId(),pa.getToken(),pa.getPjId(),pa.getPhone(),pa.getMsg(),"5");
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
        }
    }
    public String login(String apiId,String pwd){
        String token = "";
        //String url = "http://api.jyzszp.com/Api/index/loginIn?uid="+apiId+"&pwd="+pwd;
        String url ="http://api.xingjk.cn/api/do.php?action=loginIn&name="+apiId+"&password="+pwd;
        LogUtil.d(TAG,"loginUrl:"+url);
        String body = OkHttpUtil.okHttpGet(url);
        LogUtil.d(TAG,"loginBody:"+body);
        String[] strs = body.split("\\|");
        if(strs.length==2&&strs[0].equals("1")){
            token = strs[1];
        }
        return token;
    }
    public String getPhone(String apiId,String token,String pjId){
        String phone = "";
        //String phones = OkHttpUtil.okHttpGet("http://api.jyzszp.com/Api/index/getMobilenum?pid="+pjId+"&uid="+apiId+"&token="+token+"&mobile=&size=1");
        String phones = OkHttpUtil.okHttpGet("http://api.xingjk.cn/api/do.php?action=getPhone&sid="+pjId+"&token="+token);
        System.out.println(" TAG phoneBody-->"+phones);
        //LogUtil.d("phoneBody",phones);
        String[] strs = phones.split("\\|");
        if(strs!=null&&strs.length==2&&strs[1].matches("[\\d]{11}")){
            phone = strs[1];
        }
        return phone;
    }
    public String getValidCode(String apiId,String phone,String token,String pjId){
        String validCode = "";
        //String veryCodeBody = OkHttpUtil.okHttpGet("http://api.jyzszp.com/Api/index/getVcodeAndReleaseMobile?uid="+apiId+"&token="+token+"&mobile="+phone+"&pid="+pjId);
        String veryCodeBody = OkHttpUtil.okHttpGet("http://api.xingjk.cn/api/do.php?action=getMessage&sid="+pjId+"&phone="+phone+"&token="+token);
        //LogUtil.d("veryCodeBody",veryCodeBody);
        String[] codeStr = veryCodeBody.split("\\|");
        if(codeStr!=null&&codeStr.length==2&&codeStr[0].matches("1")){
            validCode = veryCodeBody.split("\\|")[1];
            validCode = regString(validCode,"[\\d]{6}",0);
        }else{
            System.out.println("TAG veryCodeBod:y"+veryCodeBody);
        }
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
