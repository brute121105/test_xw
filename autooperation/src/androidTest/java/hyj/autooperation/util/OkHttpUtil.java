package hyj.autooperation.util;

import android.content.pm.LauncherApps;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import hyj.autooperation.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/15.
 */

public class OkHttpUtil {
    //http get请求 请求外部，不带token
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

    //http get请求
    public static String okHttpGet(String token,String url){
        String reponseData = "";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        try {
            Request request = new Request.Builder()
                    .addHeader("token",token)
                    .url(url)
                    .build();
            Response response = mOkHttpClient.newCall(request).execute();
            byte[]  reponseBytes = response.body().bytes();
            reponseData = new String(reponseBytes,"utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reponseData;
    }
    //http post请求
    public static String okHttpPostBody(String token,String url,String postBody){
        String reponseData = "";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //FormBody.Builder builder = new FormBody.Builder();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        try {
            RequestBody requestBody = RequestBody.create(JSON, postBody);
            Request request = new Request.Builder()
                    .addHeader("token",token)
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = mOkHttpClient.newCall(request).execute();
            reponseData = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  reponseData;
    }

}




