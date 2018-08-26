package hyj.xw.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;
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
    //static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwibmlja25hbWUiOiLnrqHnkIblkZgiLCJhdmF0YXIiOiIyMDE4MDcwMjA0NTAyMy5wbmciLCJpYXQiOjE1MzA0NzgyMzMsImV4cCI6MTUzODI1NDIzM30.2Ji5dmWTpKKZAW15vli7Of4ggjgzvB5zPFq7PlpsP1GkTG-F0U6Joqsu_HkSEbl5iIwpqT3hY-J5fpuPgOwOAA";
    //static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwibmlja25hbWUiOiLnrqHnkIblkZgiLCJhdmF0YXIiOiIyMDE4MDcwMjA0NTAyMy5wbmciLCJpYXQiOjE1MzE1NzY2MDMsImV4cCI6MTUzOTM1MjYwM30.cJebY9np-nObKxVJhBnQDPl1zJZJdpqO5h9LHAU5XJRCDeQW_ghblrqnlrGj6gQZ97G-wvi7uUc0mIU3bOV8ug";


   /*public static String getToken(){
       return AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_WY_TOKEN);
   }*/
    //http get请求
    public static String okHttpGetByToken(String url,String token) throws IOException {
        String reponseData = "";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("token",token)
                .url(url)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        byte[]  reponseBytes = response.body().bytes();
        reponseData = new String(reponseBytes,"utf-8");
        return reponseData;
    }

    public static String okHttpGet(String url) throws IOException {
        String reponseData = "";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        byte[]  reponseBytes = response.body().bytes();
        reponseData = new String(reponseBytes,"utf-8");
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
        try {
            RequestBody requestBody = builder.build();
            Request request = new Request.Builder()
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

    //http post请求
    public static String okHttpPostBodyByToken(String url,String postBody,String token) throws IOException {
        String reponseData = "";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        RequestBody requestBody = RequestBody.create(JSON, postBody);
        Request request = new Request.Builder()
                .addHeader("token",token)
                .url(url)
                .post(requestBody)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        reponseData = response.body().string();
        return  reponseData;
    }

    //上次图片
    /*public static String upload(String url,File file){
        String reponseData = "";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file","fk.png",fileBody)
                .addFormDataPart("name","file");

        RequestBody requestBody = builder.build();
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("token",getToken())
                    .post(requestBody)
                    .build();
            Response response = mOkHttpClient.newCall(request).execute();
            reponseData = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  reponseData;

    }*/
}
