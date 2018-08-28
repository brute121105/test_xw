package hyj.xw.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;

import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.model.PhoneInfo;
import hyj.xw.model.PixPoint;
import hyj.xw.modelHttp.Device;
import hyj.xw.modelHttp.PhoneQueryVO;
import hyj.xw.modelHttp.ResponseData;
import hyj.xw.modelHttp.SendSmsVO;
import hyj.xw.util.AutoUtil;
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
    String host;
    String deviceNum;
    String username;
    String password;
    String token;
    public HttpRequestService(int tag){
        //host = "http://lizq.ngrok.xiaomiqiu.cn";
        System.out.println("AppConfigDao-->new HttpRequestService "+tag);
        host = "http://"+AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_HOST);
        deviceNum = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_DEVICE);
        username = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_USERNAME);
        password = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_PWD);
        token = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_WY_TOKEN);
    }

    public String login(){
        String result = "连接失败";
        String url = host+"/user/login";
        String postBody = "{\"name\":\""+username+"\",\"password\":\""+password+"\"}";//json数据.
        System.out.println("HttpRequestService postBody---->"+postBody);
        String res  = null;
        try {
            res = OkHttpUtil.okHttpPostBodyByToken(url,postBody,token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HttpRequestService res---->"+res);
        if(res.contains("success")){
            ResponseData responseData = JSONObject.parseObject(res,ResponseData.class);
            if("成功".equals(responseData.getMsg())){
                JSONObject jsonObject = JSONObject.parseObject(responseData.getData());
                String token = jsonObject.getString("token");
                System.out.println("HttpRequestService token---->"+token);
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_WY_TOKEN, token);
            }
            result = responseData.getMsg();;
        }
        System.out.println("HttpRequestService result---->"+result);
       return result;
    }
    //获取手机号
    public String getPhone(String domain){
        String result = "";
        try {
            PhoneQueryVO phoneQueryVO = new PhoneQueryVO(username,deviceNum);
            phoneQueryVO.setDomain(domain);
            String reqBody = JSON.toJSONString(phoneQueryVO);
            String url =host+"/phone/query";
            System.out.println("HttpRequestService getPhone url-->"+url);
            System.out.println("HttpRequestService getPhone reqBody-->"+reqBody);
            String res = OkHttpUtil.okHttpPostBodyByToken(url,reqBody,token);
            System.out.println("HttpRequestService getPhone res-->"+res);
            if(res.contains("phone")){
                JSONObject jsonObject = getJSONObjectData(res);
                result = jsonObject.getString("phone");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("HttpRequestService getPhone result-->"+result);
        return result;
    }

    //发送短信
    public String sendSms(String callNumber,String calledNumber,String content){
        String result = "";
        try {
            String url =host+"/phone/sendsms";
            System.out.println("HttpRequestService sendSms url-->"+url);
            SendSmsVO sendSmsVO = new SendSmsVO(callNumber,calledNumber,content);
            String postBody = JSON.toJSONString(sendSmsVO);
            System.out.println("HttpRequestService sendSms postBody-->"+postBody);
            String res = OkHttpUtil.okHttpPostBodyByToken(url,postBody,token);
            System.out.println("Test HttpRequestService sendSms res-->"+res);
            if(res.contains("success")){
                ResponseData responseData = JSONObject.parseObject(res,ResponseData.class);
                result = responseData.getData();
                /*if(responseData.isSuccess()){
                    result = "成功";
                }*/
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Test HttpRequestService sendSms result-->"+result);
        return result;
    }

    //更新注册状态
    public String updateRegStatus(String phone,String loginResult){
        String result = "";
        int status=0;
        if(loginResult.contains("success")){
            status = 1;
        }else if(loginResult.contains("regExp")){
            if(loginResult.contains("二维码")){
                status = 2;
            }else if(loginResult.contains("改机失败")) {
                status = 3;
            }else if(loginResult.contains("发送短信失败或超过最大尝试次数")) {
                status = 4;
            }else{
                status = 9;
            }
        }
        try {
            String url =host+"/phone/update-reg-status/"+phone+"/"+status;
            System.out.println("Test HttpRequestService updateRegStatus url-->"+url);
            result = OkHttpUtil.okHttpPostBodyByToken(url,getTestJSON(),token);
            System.out.println("Test HttpRequestService updateRegStatus res-->"+result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
    public String getTestJSON(){
        String json = "{\"a\":1}";
        return json;
    }

    //获取维护数据
    public Wx008Data getMaintainData(){
        Wx008Data wx008Data = null;
        try {
            String url =host+"/wxdata/get-maintain/"+deviceNum;
            System.out.println("HttpRequestService getMaintainData url-->"+url);
            String res = OkHttpUtil.okHttpGetByToken(url,token);
            System.out.println("HttpRequestService getMaintainData res-->"+res);
            if(res.contains("data")){
                ResponseData responseData = JSONObject.parseObject(res,ResponseData.class);
                wx008Data = JSONObject.parseObject(responseData.getData(),Wx008Data.class);
                System.out.println("HttpRequestService getMaintainData wx008Data-->"+ JSON.toJSONString(wx008Data));
                wx008Data.setPhoneInfo1(new PhoneInfo());
            }
        }/*catch (ConnectException ce){
            ce.printStackTrace();
            System.out.println("HttpRequestService getMaintainData--doAction-->连接异常飞行");
            AutoUtil.execShell("svc wifi disable");
            AutoUtil.sleep(5000);
            AutoUtil.execShell("svc wifi enable");
        }*/catch (Exception e){
            e.printStackTrace();
        }
        return wx008Data;
    }
    //反馈维护结果
    public String updateMaintainStatus(String json){
        String res = "";
        try {
            String url =host+"/wxdata/complete-maintain";
            System.out.println("HttpRequestService updateMaintainStatus url-->"+url);
            res = OkHttpUtil.okHttpPostBodyByToken(url,json,token);
            System.out.println("HttpRequestService updateMaintainStatus res-->"+res);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    //设备连接
    public String deviceConnectServer(){
        String res = "";
        try {
            String url =host+"/device/connect";
            System.out.println("HttpRequestService deviceConnectServer url--->"+url);
            Device device = new Device();
            device.setNum(deviceNum);
            res = OkHttpUtil.okHttpPostBodyByToken(url,JSON.toJSONString(device),token);
            System.out.println("HttpRequestService deviceConnectServer --->"+res);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }
    //获取设备配置
    public String getStartConifgFromServer(String deviceNum){
        String res = "";
        try {
            String url =host+"/device/num/"+deviceNum;
            System.out.println("HttpRequestService getStartConifgFromServer url--->"+url);
            res = OkHttpUtil.okHttpGetByToken(url,token);
            System.out.println("HttpRequestService getStartConifgFromServer res--->"+res);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    //版本检测
    public String checkUpdate(String apkType,int version){
        String res = "";
        try {
            String url =host+"/apk/check-update?apkType="+apkType+"&versionCode="+version;
            System.out.println("HttpRequestService checkUpdate url--->"+url);
            res = OkHttpUtil.okHttpGetByToken(url,token);
            System.out.println("HttpRequestService checkUpdate res--->"+res);
            if(res.contains("success")){
                ResponseData responseData = JSONObject.parseObject(res,ResponseData.class);
                if(responseData.isSuccess()){
                    res = responseData.getData();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    //获取异常消息
    public String getOneExpMsg(){
        String res = "";
        try {
            String url =host+"/message/getone";
            res = OkHttpUtil.okHttpGetByToken(url,token);
            if(res.contains("data")){
                ResponseData responseData = JSONObject.parseObject(res,ResponseData.class);
                res = responseData.getData();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    //管家接受消息并推送到服务器
    public String gjQuery(String recMsg){
        String res = "";
        try {
            String url =host+"/query/"+recMsg;
            res = OkHttpUtil.okHttpGetByToken(url,token);
            if(res.contains("data")){
                ResponseData responseData = JSONObject.parseObject(res,ResponseData.class);
                res = responseData.getData();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }




    public String uploadPhoneData(String wx008DataStr){
        String result = "";
        try {
            String url =host+"/wxdata/save";
            System.out.println("提交数据 HttpRequestService uploadPhoneData wx008DataStr--->"+wx008DataStr);
            System.out.println("HttpRequestService uploadPhoneData url--->"+url);
            String res = OkHttpUtil.okHttpPostBodyByToken(url,wx008DataStr,token);
            System.out.println("HttpRequestService uploadPhoneData res--->"+res);
            if(res.contains("data")){
                ResponseData responseData = JSONObject.parseObject(res,ResponseData.class);
                result = responseData.getData();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public String uploadPhoneDataReturnAll(String wx008DataStr){
        String res = "";
        try {
            String url =host+"/wxdata/save";
            System.out.println("提交数据 HttpRequestService uploadPhoneData wx008DataStr--->"+wx008DataStr);
            System.out.println("HttpRequestService uploadPhoneData url--->"+url);
            res = OkHttpUtil.okHttpPostBodyByToken(url,wx008DataStr,token);
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public String setFriendsNull(long id,String friends){
        String res = "";
        String url =host+"/wxdata/setfriends/"+id+"?friends="+friends;
        try {
            System.out.println("TestHttpRequestService setFriendsNull url--->"+url);
            res = OkHttpUtil.okHttpPostBodyByToken(url,getTestJSON(),token);
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
