package hyj.xw;

import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import hyj.xw.activity.ApiSettingActivity;
import hyj.xw.activity.AppSettingActivity;
import hyj.xw.activity.AutoLoginSettingActivity;
import hyj.xw.activity.DataImpExpActivity;
import hyj.xw.activity.YhSettingActivity;
import hyj.xw.aw.sysFileRp.CreatePhoneEnviroment;
import hyj.xw.common.CommonConstant;
import hyj.xw.common.FilePathCommon;
import hyj.xw.conf.PhoneConf;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.flowWindow.MyWindowManager;
import hyj.xw.hook.newHook.NewPhoneInfo;
import hyj.xw.model.DeviceInfo;
import hyj.xw.model.LitePalModel.AppConfig;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.model.StartRunningConfig;
import hyj.xw.service.SmsReciver;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.DaoUtil;
import hyj.xw.util.DeviceParamUtil;
import hyj.xw.util.FileUtil;
import hyj.xw.util.GetPermissionUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.OkHttpUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText editText;
    EditText cnNumEditText;
    CheckBox isAirChangeIpCheckBox;
    CheckBox isFeedCheckBox;
    CheckBox loginSucessPauseCheckBox;

    private String[] phoneStrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetPermissionUtil.getReadAndWriteContactPermision(this, MainActivity.this);
        /*if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
            } else {
                //return;
                //TODO do something you need
            }
        }*/
        FileUtil.createFilePath(FilePathCommon.baseAppPath);
        FileUtil.createFilePath(FilePathCommon.importDataAPath);
        FileUtil.createFilePath(FilePathCommon.dataBakPath);
        FileUtil.createFilePath(FilePathCommon.importData008Path);
        if (Build.VERSION.SDK_INT < 23) {
            MyWindowManager.createSmallWindow(getApplicationContext());
            MyWindowManager.createSmallWindow2(getApplicationContext());
        }

        //飞行模式换ip
        isAirChangeIpCheckBox = (CheckBox) this.findViewById(R.id.isAirChangeIp);
        String isAirChangeIp = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_AIR_CHANGE_IP);
        isAirChangeIpCheckBox.setChecked("1".equals(isAirChangeIp) || TextUtils.isEmpty(isAirChangeIp) ? true : false);
        isAirChangeIpCheckBox.setOnClickListener(this);
        //养号
        isFeedCheckBox = (CheckBox) this.findViewById(R.id.isFeed);
        String ifFeed = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_FEED);
        isFeedCheckBox.setChecked("1".equals(ifFeed) || TextUtils.isEmpty(ifFeed) ? true : false);
        isFeedCheckBox.setOnClickListener(this);
        //登录成功暂停
        loginSucessPauseCheckBox = (CheckBox) this.findViewById(R.id.loginSucessPause);
        loginSucessPauseCheckBox.setChecked("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_LOGIN_PAUSE)) ? true : false);
        loginSucessPauseCheckBox.setOnClickListener(this);
        //综合参数
        editText = (EditText) findViewById(R.id.ext);
        String c = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_EXT);
        editText.setText(TextUtils.isEmpty(c) ? "0" : c);
        //国别
        cnNumEditText = (EditText) findViewById(R.id.cnNum);
        String cnNum = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_CN_NUM);
        cnNumEditText.setText(TextUtils.isEmpty(cnNum) ? "86" : cnNum);

       /*
        下拉框数据开始
        */
        phoneStrs = PhoneConf.getAllPhoneList();
        //spinner
        Spinner spinner = (Spinner) findViewById(R.id.Spinner01);
        setSpnnierStyleAndContents(spinner, phoneStrs);
        //设置默认值
        String loginIndex = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX);
        loginIndex = TextUtils.isEmpty(loginIndex) || Integer.parseInt(loginIndex) > phoneStrs.length - 1 ? "0" : loginIndex;
        spinner.setSelection(Integer.parseInt(loginIndex), true);//spinner下拉框默认值*/

        //spinner02
        Spinner spinner02 = (Spinner) findViewById(R.id.Spinner02);
        setSpnnierStyleAndContents(spinner02, phoneStrs);
        //设置默认值
        String endLoginIndex = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_END_LOGIN_INDEX);
        endLoginIndex = TextUtils.isEmpty(endLoginIndex) || Integer.parseInt(endLoginIndex) > phoneStrs.length - 1 ? String.valueOf(phoneStrs.length - 1) : endLoginIndex;
        spinner02.setSelection(Integer.parseInt(endLoginIndex), true);//spinner下拉框默认值*/
         /*
        下拉框数据结束
        */

        Button openAssitBtn = (Button) this.findViewById(R.id.open_assist);
        Button autoLoginBtn = (Button) this.findViewById(R.id.auto_login);
        Button clearAppDataBtn = (Button) this.findViewById(R.id.clearAppData);
        Button apiSettingBtn = (Button) this.findViewById(R.id.apiSetting);
        Button del_upload_fileBtn = (Button) this.findViewById(R.id.del_upload_file);
        Button yhBtn = (Button) this.findViewById(R.id.btn_yh_setting);
        Button btn_data_impExp = (Button) this.findViewById(R.id.btn_data_impExp);
        openAssitBtn.setOnClickListener(this);
        autoLoginBtn.setOnClickListener(this);
        clearAppDataBtn.setOnClickListener(this);
        apiSettingBtn.setOnClickListener(this);
        del_upload_fileBtn.setOnClickListener(this);
        yhBtn.setOnClickListener(this);
        btn_data_impExp.setOnClickListener(this);
        Button killAppBtn = (Button) this.findViewById(R.id.btn_kill_app);
        killAppBtn.setOnClickListener(this);

        //AutoUtil.addPhoneContacts("zz","12365489658");
        // AutoUtil.addPhoneContacts("zz1","12365489658");
        //getSysLanguage();
        //DeviceParamUtil.getAwPhoneInfo();
        //PhoneConf.getAddFrWx();
        //System.out.println("mac-->"+AutoUtil.getLocalMacAddress());
        //GetPhoneInfoUtil.getEnvironmentAwData();
        //DaoUtil.readFilePhoneAndPwdUpdate2Db();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String spinnerValue = phoneStrs[i];
        switch (adapterView.getId()) {
            case R.id.Spinner01:
                //截取spiiner的手机号保存到数据库
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_START_LOGINACCOUNT, spinnerValue.substring(spinnerValue.indexOf("-") + 1, spinnerValue.indexOf(" ")));
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_START_LOGIN_INDEX, spinnerValue.substring(0, spinnerValue.indexOf("-")));
                break;
            case R.id.Spinner02:
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_END_LOGINACCOUNT, spinnerValue.substring(spinnerValue.indexOf("-") + 1, spinnerValue.indexOf(" ")));
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_END_LOGIN_INDEX, spinnerValue.substring(0, spinnerValue.indexOf("-")));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * 设置spinner样式和 下拉内容
     *
     * @param spinner
     * @param contents 下拉内容，String[]数组
     */
    private void setSpnnierStyleAndContents(Spinner spinner, String[] contents) {
        //将可选内容与ArrayAdapter连接起来
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, contents);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(this);
        //设置默认值
        spinner.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {

        final String phone = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_EXT);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String HOST = "120.78.134.230:80";
                String url = "http://" + HOST + "/sendValidCode?phone=" + phone;
                LogUtil.d("url", url);
                String str = OkHttpUtil.okHttpGet(url);
                LogUtil.d("resBody", str);
            }
        }).start();


        System.out.println("---> onstart");
        System.out.println("手机号码-->" + phone);
        super.onStart();
        SmsReciver receiver = new SmsReciver();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(receiver, filter);

    }

    private void save() {
        AppConfig config = new AppConfig(CommonConstant.APPCONFIG_EXT, editText.getText().toString());
        AppConfigDao.saveOrUpdate(config);
        //国别
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_CN_NUM, cnNumEditText.getText().toString());
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_AIR_CHANGE_IP, isAirChangeIpCheckBox.isChecked() ? "1" : "0");

    }

    @Override
    protected void onStop() {
        save();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        System.out.println("main--am force-stop hyj.autooperation");
        AutoUtil.execShell("am force-stop hyj.autooperation");
        save();
        super.onDestroy();
    }

    public  StartRunningConfig getStartRunningConfig(){
        String srConfigStr = FileUtil.readAllUtf8(FilePathCommon.startRunninConfigTxtPath);
        StartRunningConfig srConfig = JSONObject.parseObject(srConfigStr,StartRunningConfig.class);
        return srConfig;
    }
    public  void saveStartRunningConfig(StartRunningConfig srConfig){
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.startRunninConfigTxtPath, JSON.toJSONString(srConfig));
    }

    public void testMethod() {
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.fkFilePath,"");
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"");
        delAllFile();
        StartRunningConfig srConfig = new StartRunningConfig();
        srConfig.setConnNetType(1);
        List<String> otherOperationNames = new ArrayList<String>();
        otherOperationNames.add("注册");
        //otherOperationNames.add("养号");
        otherOperationNames.add("发圈");
        otherOperationNames.add("提取wxid");
        srConfig.setOtherOperationNames(otherOperationNames);
        saveStartRunningConfig(srConfig);
        //FileUtil.writeContent2FileForceUtf8(FilePathCommon.startRunninConfigTxtPath,JSON.toJSONString(srConfig));
        new Thread(new Runnable() {
            @Override
            public void run() {
               /* String url = "http://192.168.1.5/commons/pic-loc";
                File file = new File("/sdcard/fangkuai.png");
                String res = OkHttpUtil.upload(url,file);
                System.out.println("res-->"+res);
                FkResponseBody frb = JSON.parseObject(res,FkResponseBody.class);
                System.out.println("res-->frb "+JSON.toJSONString(frb));*/
                System.out.println("main-->start========");
                AutoUtil.execShell("am force-stop hyj.autooperation");
                AutoUtil.execShell("am instrument -w -r   -e debug false -e class hyj.autooperation.ExampleInstrumentedTest#useAppContext hyj.autooperation.test/android.support.test.runner.AndroidJUnitRunner");
            }
        }).start();

        new Thread(new Runnable() {
            int loginIndex = Integer.parseInt(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX));
            StartRunningConfig currentSrc = getStartRunningConfig();
            @Override
            public void run() {
                List<Wx008Data> wx008Datas = DaoUtil.getWx008Datas();
                Wx008Data currentWx008Data=null;//当前运行wx数据
                while (true){
                    AutoUtil.sleep(1000);
                    String tag = FileUtil.readAllUtf8(FilePathCommon.setEnviromentFilePath);
                    System.out.println("main-->currentSrc:"+JSON.toJSONString(currentSrc)+" 监听环境设置标志:"+tag);
                    /**
                     * 环境设置标志
                     */
                    if("next".equals(tag)||"retry".equals(tag)){
                        if("注册".equals(currentSrc.getOtherOperationNames().get(0))){
                            currentWx008Data = PhoneConf.createRegData();
                            currentWx008Data.save();
                            System.out.println("main-->doAction--->获取一份新改机wxData并保存");
                        }else if("养号".equals(currentSrc.getOtherOperationNames().get(0))) {
                            if(tag.equals("next")){
                                doNextIndexAndRecord2DB();
                                System.out.println("main-->doAction--->loginIndex+1 获取下一个");
                            }
                            currentWx008Data = wx008Datas.get(loginIndex);
                        }
                        setEnviroment(currentWx008Data);//修改hook文件
                        FileUtil.writeContent2FileForceUtf8(FilePathCommon.wx008DataFilePath,JSON.toJSONString(currentWx008Data));//写入008j数据，供对方用
                        FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"done");
                        System.out.println("main-->doAction--->mainActivity环境和currentData已准备，写入done标志完成");
                    }
                     currentSrc = getStartRunningConfig();
                     if(!TextUtils.isEmpty(currentSrc.getLoginResult())){
                         currentWx008Data.setExpMsg(currentSrc.getLoginResult());
                         int cn = DaoUtil.updateExpMsg(currentWx008Data,currentWx008Data.getExpMsg()+"-"+AutoUtil.getCurrentDate());
                         System.out.println("main-->updateExpMsg:"+currentSrc.getLoginResult()+" cn:"+cn);
                         currentSrc.setLoginResult("");
                         saveStartRunningConfig(currentSrc);
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
                }
            }

            private File waitAndGetFile(){
                String  path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Screenshots";
                File picFile = null;
                File[] files = new File(path).listFiles();
                if(files!=null&&files.length>0){
                    picFile = files[files.length-1];
                }
                return picFile;
            }

            private void doNextIndexAndRecord2DB(){
                int endLoginIndex = Integer.parseInt(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_END_LOGIN_INDEX));
                System.out.println("errRecord loginIndex-->"+loginIndex);
                loginIndex = loginIndex+1;
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_START_LOGIN_INDEX,loginIndex+"");
            }
            private void setEnviroment(Wx008Data currentWx008Data){
                NewPhoneInfo pi = null;
                if(!TextUtils.isEmpty(currentWx008Data.getPhoneStrsAw())){//aw数据
                    pi = JSON.parseObject(currentWx008Data.getPhoneStrsAw(),NewPhoneInfo.class);
                    if(TextUtils.isEmpty(pi.getRgPhoneNo())){
                        pi.setRgPhoneNo(pi.getLine1Number());
                    }
                }else {
                    pi = PhoneConf.xw2awData(currentWx008Data);
                }
                pi.setCpuName(pi.getCpuName().trim().toLowerCase());
                CreatePhoneEnviroment.create(GlobalApplication.getContext(),pi);
                FileUtil.writeContent2FileForceUtf8(FilePathCommon.baseAppPathAW,FilePathCommon.npiFileName, JSON.toJSONString(pi));
            }

        }).start();
        /*List<String> phones = FileUtil.read008Data("/sdcard/brute9.txt");
        for (String phone : phones) {
            phone = phone.substring(phone.indexOf("--") + 2);
            System.out.println("wxid-->" + phone);
            Wx008Data wd = DaoUtil.findByWxNumOrWxid(phone);
            if (wd == null) continue;
            wd.setDieFlag(888);
            int cn = wd.updateAll("wxId=? or wxid19=?", phone, phone);
            if (cn > 0) {
                System.out.println("cn-->" + cn + " wxid:" + phone);
            }
        }*/
    }
    private void delAllFile(){
       /* File file = new File(FilePathCommon.fkScreenShotPath);
        if(file.exists()){
            file.delete();
        }*/
        String  path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Screenshots";
        File[] files = new File(path).listFiles();
        if(files!=null&&files.length>0){
            for(File file:files){
                System.out.println("main-->删除文件："+file.getName()+" "+file.delete());
            }
        }
    }


    public void clearAppData() {
        AutoUtil.execShell("am force-stop hyj.autooperation");
        AutoUtil.clearAppData();
        Toast.makeText(MainActivity.this, "清除完成", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View view) {
        System.out.println("vie --clic000--->" + view.getId());
        switch (view.getId()) {
            case R.id.auto_login:
                startActivity(new Intent(MainActivity.this, AutoLoginSettingActivity.class));
                break;
            case R.id.open_assist:
                String startStr = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX);
                String endStr = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_END_LOGIN_INDEX);
                int start = Integer.parseInt(TextUtils.isEmpty(startStr) ? "0" : startStr);
                int end = Integer.parseInt(TextUtils.isEmpty(endStr) ? "0" : endStr);
                if (start > end) {
                    Toast.makeText(this, "已设置的开始序号【" + start + "】不能大于结束序号【" + end + "】", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "已设置登录序号【" + start + "-" + end + "】", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                }
                break;
            case R.id.clearAppData:
                clearAppData();
                break;
            case R.id.isFeed:
                System.out.println("isFeedCheckBox.isChecked()-->" + isFeedCheckBox.isChecked());
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_FEED, isFeedCheckBox.isChecked() ? "1" : "0");
                break;
            case R.id.isAirChangeIp:
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_AIR_CHANGE_IP, isAirChangeIpCheckBox.isChecked() ? "1" : "0");
                break;
            case R.id.loginSucessPause:
                System.out.println("loginSucessPauseCheckBox.isChecked()-->" + loginSucessPauseCheckBox.isChecked());
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_LOGIN_PAUSE, loginSucessPauseCheckBox.isChecked() ? "1" : "0");
                break;
            case R.id.apiSetting:
                DeviceInfo deviceInfo = DeviceParamUtil.getDeviceInfo();
                System.out.println("deviceInfo-->" + JSON.toJSONString(deviceInfo));
                createData();
                startActivity(new Intent(MainActivity.this, ApiSettingActivity.class));
                break;
            case R.id.del_upload_file://其他操作
                startActivity(new Intent(MainActivity.this, AppSettingActivity.class));
                break;
            case R.id.btn_yh_setting:
                testMethod();
                //startActivity(new Intent(MainActivity.this, YhSettingActivity.class));
                break;
            case R.id.btn_data_impExp:
                startActivity(new Intent(MainActivity.this, DataImpExpActivity.class));
                break;
            case R.id.btn_kill_app:
                AutoUtil.killApp();
                break;
        }
    }


    public void createData() {
        Wx008Data data = DaoUtil.findByPhone("1133376132");
       /* data.setDatas("");
        data.setWxId("");
        int cn = data.updateAll("phone=?",data.getPhone());
        System.out.println("cn--->"+cn);*/
        /*data.setPhone("1133376132");
        data.setWxPwd("www12345");
        data.setCnNum("60");
        data.setCreateTime(new Date());*/
       /* boolean flag = data.save();
        System.out.println("flag--->"+flag);*/
    }

    private void createRegData() {
        ContentValues values = new ContentValues();
        values.put("dataType", "1");
        int cn = DataSupport.updateAll(Wx008Data.class, values);
        System.out.println("cn-->" + cn);
        /*Wx008Data currentWx008Data = new Wx008Data();
        currentWx008Data.setGuid(AutoUtil.getUUID());
        currentWx008Data.setPhone("8973807928");
        currentWx008Data.setWxPwd("wwww12345");
        currentWx008Data.setCnNum("62");
        currentWx008Data.setCreateTime(new Date());
        NewPhoneInfo phoneInfo = PhoneConf.createPhoneInfo();
        phoneInfo.setLineNumber("8973807928");//获取到的手机号码
        currentWx008Data.setPhoneStrs(JSON.toJSONString(phoneInfo));
        int cn = currentWx008Data.updateAll("phone=?","8973807928");
        System.out.println("cu-->"+cn);*/

    }

    public boolean isVpnConnected() {
        try {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                for (NetworkInterface intf : Collections.list(niList)) {
                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    System.out.println("intf-->" + intf.getName());
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())) {
                        return true;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

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
