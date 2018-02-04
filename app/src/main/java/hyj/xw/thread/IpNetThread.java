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

public class IpNetThread extends Thread {
    Map<String,String> record;
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
                String ipBody= OkHttpUtil.okHttpGet("http://www.ip.cn/");
                System.out.println("IpNetThread ipBody-->"+ipBody);
                ipMsg = StringUtilHyj.getIp(ipBody).trim();
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
