package hyj.xw.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hyj.xw.util.LogUtil;
import hyj.xw.util.OkHttpUtil;

import static hyj.xw.util.OkHttpUtil.okHttpGet;

/**
 * Created by Administrator on 2017/12/27.
 */

public class SmsReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("--->onReceive");
        Bundle bundle = intent.getExtras();
        SmsMessage msg = null;
        if (null != bundle) {
            Object[] smsObj = (Object[]) bundle.get("pdus");
            for (Object object : smsObj) {
                msg = SmsMessage.createFromPdu((byte[]) object);
                String num = msg.getOriginatingAddress();
                String msgContent =  msg.getDisplayMessageBody();

                LogUtil.d("SmsReciver","num:"+num+"  msgContent:"+msgContent);

                //在这里写自己的逻辑
                if (msgContent.contains("微信")) {
                    final String validCode = regString(msgContent,"[\\d]{6}",0);
                    LogUtil.d("validCode",validCode);
                    if(!"".equals(validCode)){
                        //http线程
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String HOST = "120.78.134.230:80";
                                String url = "http://"+HOST+"/sendValidCode?code="+validCode;
                                LogUtil.d("url",url);
                                String str = null;
                                try {
                                    str = okHttpGet(url);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                LogUtil.d("resBody",str);
                            }
                        }).start();
                    }
                }

            }
        }
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
