package hyj.autooperation.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;

import hyj.autooperation.httpModel.Device;
import hyj.autooperation.httpModel.ResponseData;
import hyj.autooperation.httpModel.SendSmsVO;
import hyj.autooperation.model.Wx008Data;
import hyj.autooperation.util.OkHttpUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/7/5 0005.
 */

public class HttpRequestService {
    String host;
    String deviceNum;
    String username;
    String token;
    public HttpRequestService(Device device){
        host = device.getHost();
        deviceNum = device.getNum();
        username =device.getUsername();
        token = device.getToken();
    }

    //发送短信
    public String sendSms(String callNumber,String calledNumber,String content){
        String result = "";
        try {
            String url =host+"/phone/sendsms";
            System.out.println("Test HttpRequestService sendSms url-->"+url+" t:"+token);
            SendSmsVO sendSmsVO = new SendSmsVO(callNumber,calledNumber,content);
            String postBody = JSON.toJSONString(sendSmsVO);
            System.out.println("Test HttpRequestService sendSms postBody-->"+postBody);
            String res = OkHttpUtil.okHttpPostBody(token,url,postBody);
            System.out.println("Test HttpRequestService sendSms res-->"+res);
            if(res.contains("success")){
                ResponseData responseData = JSONObject.parseObject(res,ResponseData.class);
                if(responseData.isSuccess()){
                    result = "成功";
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Test HttpRequestService sendSms result-->"+result);
        return result;
    }

    public String uploadPhoneData(Wx008Data wx008Data){
        String res = "";
        String wx008DataStr = JSON.toJSONString(wx008Data);
        String url =host+"/wxdata/save";
        try {
            System.out.println("提交数据 TestHttpRequestService uploadPhoneData wx008DataStr--->"+wx008DataStr);
            System.out.println("TestHttpRequestService uploadPhoneData url--->"+url);
            res = OkHttpUtil.okHttpPostBody(token,url,wx008DataStr);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("TestHttpRequestService uploadPhoneData res--->"+res);
        return res;
    }

    public String setFriendsNull(long id){
        String res = "";
        String url =host+"/wxdata/setfriends/"+id+"?friends=";
        try {
            System.out.println("TestHttpRequestService setFriendsNull url--->"+url);
            res = OkHttpUtil.okHttpPostBody(token,url,getTestJSON());
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("TestHttpRequestService setFriendsNull res--->"+res);
        return res;
    }


    public JSONObject getJSONObjectData(String json){
        ResponseData responseData = JSONObject.parseObject(json,ResponseData.class);
        JSONObject jsonObject = JSONObject.parseObject(responseData.getData());
        return jsonObject;
    }

    public String getTestJSON(){
        String json = "{\"a\":1}";
        return json;
    }

    /**
     * 上传图片
     */
    //File file = new File(FilePathCommon.fkScreenShotPath);
                    /*File file = waitAndGetFile();
                    if(file!=null){
                          System.out.println("doA main res-->fileName:"+file.getName()+" length:"+file.length());
                          String host = "http://192.168.1.5";
                          String url =host+"/commons/pic-loc";
                          String res = OkHttpUtil.upload(url,file);
                          System.out.println("doA main OkHttpUtil res-->"+res);
                          if(res.contains("data")){
                              FileUtil.writeContent2FileForceUtf8(FilePathCommon.fkFilePath, res);
                              boolean flag = file.delete();
                              System.out.println("main res 删除："+flag);
                          }
                      }else {
                          System.out.println("doA main res-->file is null");
                      }*/


    //上次图片
    public static String uploadImage(String url,File file){
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
}
