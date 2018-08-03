package hyj.xw.thread;

import android.text.TextUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;

import hyj.xw.util.AutoUtil;
import hyj.xw.util.OkHttpUtil;
import hyj.xw.util.StringUtilHyj;

/**
 * Created by asus on 2018/2/3.
 */

public class GetOrUpdateServerStatusThread implements Callable<String> {
    String url = "http://120.78.134.230/qr/getIsNewQr?phone=123";
    Map<String,String> record;
    String status;//1已经生成二维码 3 可以生成二维码 2登录pc端成功
    public GetOrUpdateServerStatusThread(Map<String,String> record){
        this.record = record;
    }
    public GetOrUpdateServerStatusThread(String status){
        this.status = status;
    }
    @Override
    public String  call() {
        String status= null;
        try {
            status = OkHttpUtil.okHttpGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("GetOrUpdateServerStatusThread status get-->"+status);
        return status;
    }
}
