package hyj.autooperation.util;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/15.
 */

public class OkHttpUtil {
    //http get请求
    public static String okHttpGet(String url){
        String reponseData = "";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            byte[]  reponseBytes = response.body().bytes();
            reponseData = new String(reponseBytes,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reponseData;
    }
    //http post请求
    public static String okHttpPost(String url,Map<String,String> params){
        String reponseData = "";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        //参数组装
        for(String name :params.keySet()){
            builder.add(name,params.get(name));
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = mOkHttpClient.newCall(request).execute();
            reponseData = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  reponseData;

    }
}
