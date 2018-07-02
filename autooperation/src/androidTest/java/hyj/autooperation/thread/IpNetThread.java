package hyj.autooperation.thread;

import android.text.TextUtils;

import java.util.Map;

import hyj.autooperation.util.OkHttpUtil;


/**
 * Created by asus on 2018/2/3.
 */
//http://pv.sohu.com/cityjson?ie=utf-8
//    http://www.ip.cn/
public class IpNetThread extends Thread {
    String ipUrl = "http://192.168.1.5/user/login";
    public IpNetThread(){
    }
    @Override
    public void run() {
        super.run();
        String ipMsg = "";
        int cn = 0;
        String postBody = "{\"name\":\"lisi\",\"password\":\"李四\"}";//json数据.
        String res  = OkHttpUtil.okHttpPostBody(ipUrl,postBody);
        System.out.println("res-->"+res);
    }
}
