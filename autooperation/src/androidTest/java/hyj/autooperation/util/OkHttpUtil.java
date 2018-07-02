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
    static String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwibmlja25hbWUiOiLnrqHnkIblkZgiLCJhdmF0YXIiOiIyMDE4MDcwMjA0NTAyMy5wbmciLCJpYXQiOjE1MzA0NzgyMzMsImV4cCI6MTUzODI1NDIzM30.2Ji5dmWTpKKZAW15vli7Of4ggjgzvB5zPFq7PlpsP1GkTG-F0U6Joqsu_HkSEbl5iIwpqT3hY-J5fpuPgOwOAA";
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

    //http post请求
    public static String okHttpPostBody(String url,String postBody){
        String reponseData = "";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        RequestBody requestBody = RequestBody.create(JSON, postBody);
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

    //上次图片
    public static String upload(String url,File file){
        String reponseData = "";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file","head_image",fileBody)
                .addFormDataPart("name","file");

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwibmlja25hbWUiOiLnrqHnkIblkZgiLCJhdmF0YXIiOiIyMDE4MDcwMjA0NTAyMy5wbmciLCJpYXQiOjE1MzA0NzgyMzMsImV4cCI6MTUzODI1NDIzM30.2Ji5dmWTpKKZAW15vli7Of4ggjgzvB5zPFq7PlpsP1GkTG-F0U6Joqsu_HkSEbl5iIwpqT3hY-J5fpuPgOwOAA")
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

    public static void httpUpload(String url,File file){
        String boundary="-------------------------7e020233150564";//编节符
        String prefix="--";//前缀 上传时需要多出两个-- 一定需要注意！！！
        String end="\r\n";//这里也需要注意，在html协议中，用 “/r/n” 换行，而不是 “/n”。
        try {
            URL http=new URL(url);
            HttpURLConnection conn= (HttpURLConnection) http.openConnection();
            conn.setRequestProperty("token",token);
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setDoInput(true);//准许向服务器读数据
            conn.setDoOutput(true);//准许向服务器写入数据
            conn.setRequestProperty("Content-Type","multipart/form-data;boundary="+boundary);
            conn.setRequestProperty("name","file");
            //创建一个输出流对象，
            DataOutputStream out=new DataOutputStream(conn.getOutputStream());

            //向服务器写入数据  这里就需要完全根据以上协议格式来写，需要仔细，避免出错。
            out.writeBytes(prefix+boundary+end);//这是第一行  并回车换行
            //这是第二行，文件名和对应服务器的
            //out.writeBytes("name=\"file\";"+end);//这是第二行
            out.writeBytes(end);//空一行
            //以下写入图片
            FileInputStream fileInputStream=new FileInputStream(file);
            byte[]b=new byte[1024*4];//缓冲区
            int len;
            //循环读数据
            while((len=fileInputStream.read(b))!=-1){
                out.write(b, 0, len);
            }
            //写完数据后 回车换行
            out.writeBytes(end);
            out.writeBytes(prefix + boundary + prefix + end);
            out.flush();//清空

            //创建一个输入流对象  获取返回的信息  是否上传成功
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb=new StringBuffer();
            String str;
            while((str=reader.readLine())!=null){
                sb.append(str);
            }
            //关闭流信息
            if(out!=null)out.close();
            if(reader!=null)reader.close();

            System.out.print("OkHttpUtil 1返回结果："+sb.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //文件上传到服务器
    public static void uploadMultiFile(final String url,final String filePath,final String fileName) {
        //开启子线程执行上传，避免主线程堵塞
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(filePath, fileName);
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file",fileName, fileBody)
                        .addFormDataPart("name",fileName)//name是对方接收的另一个参数，文件名
                        .build();
                Request request = new Request.Builder()
                        .addHeader("token",token)
                        .header("token",token)
                        .url(url)
                        .post(requestBody)
                        .build();
                final OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
                OkHttpClient okHttpClient  = httpBuilder
                        //设置超时
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(15, TimeUnit.SECONDS)
                        .build();
                  okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        System.out.println("OkHttpUtil new res-->失败-");
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        System.out.println("OkHttpUtil new res-->"+response.body().string());
                    }
                });
            }
        }).start();
    }
}




