package hyj.xw.thread;

import android.text.TextUtils;

import java.util.Map;

import hyj.xw.util.AutoUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.OkHttpUtil;
import hyj.xw.util.StringUtilHyj;

/**
 * Created by asus on 2018/2/3.
 */
//http://pv.sohu.com/cityjson?ie=utf-8
//    http://www.ip.cn/
public class IpNetThread extends Thread {
    Map<String,String> record;
    String ipUrl = "http://pv.sohu.com/cityjson?ie=utf-8";
    public  IpNetThread(Map<String,String> record){
        this.record = record;
    }
    @Override
    public void run() {
        super.run();
        String ipMsg = "";
        int cn = 0;
        while (cn<2){
            if(!AutoUtil.isNetworkConnected()){
                AutoUtil.sleep(2000);
                System.out.println("IpNetThread--->等待网络连接");
                continue;
            }

            try {
                /*String ipBody= OkHttpUtil.okHttpGet(ipUrl);
                ipMsg = StringUtilHyj.getIp(ipBody).trim();*/
                String ipBody= OkHttpUtil.okHttpGet(ipUrl);
                System.out.println("IpNetThread ipBody-->"+ipBody);
                if(ipBody.contains("{")){
                    ipMsg = ipBody.substring(ipBody.indexOf("{"));
                }
                System.out.println("IpNetThread ipMsg-->"+ipMsg);
                if(TextUtils.isEmpty(ipMsg)){
                    cn = cn+1;
                }else {
                    record.put("ipMsg",ipMsg);
                    System.out.println("IpNetThread ip-->"+ipMsg);
                    break;
                }
            }catch (Exception e){
                cn = cn+1;
                record.put("ipMsg","记录ip失败cn:"+cn);
            }
        }
    }
}
