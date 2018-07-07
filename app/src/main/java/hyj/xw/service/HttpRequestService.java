package hyj.xw.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;

import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.model.PhoneInfo;
import hyj.xw.model.PixPoint;
import hyj.xw.modelHttp.Device;
import hyj.xw.modelHttp.ResponseData;
import hyj.xw.util.OkHttpUtil;
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
    //String host = "http://192.168.1.5:8080";
    String host = "http://lizq.ngrok.xiaomiqiu.cn";
    String deviceNum = "A1001";
    public Wx008Data getMaintainData(){
        Wx008Data wx008Data = null;
        try {
            String url =host+"/wxdata/get-maintain/"+deviceNum;
            System.out.println("HttpRequestService getMaintainData url-->"+url);
            String res = OkHttpUtil.okHttpGet(url);
            System.out.println("HttpRequestService getMaintainData res-->"+res);
            ResponseData responseData = JSONObject.parseObject(res,ResponseData.class);
            wx008Data = JSONObject.parseObject(responseData.getData(),Wx008Data.class);
            System.out.println("HttpRequestService getMaintainData wx008Data-->"+ JSON.toJSONString(wx008Data));
            wx008Data.setPhoneInfo1(new PhoneInfo());
        }catch (Exception e){
            e.printStackTrace();
        }
        return wx008Data;
    }
    public void updateMaintainStatus(long id,int dieFlag){
        String url =host+"/wxdata/complete-maintain/"+id+"/"+dieFlag;
        System.out.println("HttpRequestService updateMaintainStatus url-->"+url);
        PixPoint pixPoint = new PixPoint();
        pixPoint.setB(99);
        String res = OkHttpUtil.okHttpPostBody(url,JSON.toJSONString(pixPoint));
        System.out.println("HttpRequestService updateMaintainStatus res-->"+res);
    }

    public String deviceConnectServer(String deviceNum){
        String url =host+"/device/connect";
        System.out.println("OkHttpUtil deviceConnectServer url--->"+url);
        Device device = new Device();
        device.setNum(deviceNum);
        String res = OkHttpUtil.okHttpPostBody(url,JSON.toJSONString(device));
        System.out.println("OkHttpUtil deviceConnectServer --->"+res);
        return res;
    }
    public String getStartConifgFromServer(String deviceNum){
        String url =host+"/device/num/"+deviceNum;
        System.out.println("OkHttpUtil getStartConifgFromServer url--->"+url);
        String res = OkHttpUtil.okHttpGet(url);
        System.out.println("OkHttpUtil getStartConifgFromServer res--->"+res);
        return res;
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
