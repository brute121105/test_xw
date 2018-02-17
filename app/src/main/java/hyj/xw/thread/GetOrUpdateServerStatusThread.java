package hyj.xw.thread;

import android.text.TextUtils;

import java.util.Map;
import java.util.concurrent.Callable;

import hyj.xw.util.AutoUtil;
import hyj.xw.util.OkHttpUtil;
import hyj.xw.util.StringUtilHyj;

/**
 * Created by asus on 2018/2/3.
 */

public class GetOrUpdateServerStatusThread implements Callable<String> {
    String url = "http://120.78.134.230/androidServer/status";
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
        if("0".equals(status)){
            String status= OkHttpUtil.okHttpGet(url);
            System.out.println("GetOrUpdateServerStatusThread status get-->"+status);
            return status;
        }else {
            String status= OkHttpUtil.okHttpGet(url+"?status="+this.status);
            System.out.println("GetOrUpdateServerStatusThread status update-->"+status);
            return status;
        }
    }
}
