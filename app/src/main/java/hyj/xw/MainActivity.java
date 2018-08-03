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
import java.io.FileInputStream;
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
import hyj.xw.conf.ImpExpData;
import hyj.xw.conf.PhoneConf;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.flowWindow.MyWindowManager;
import hyj.xw.hook.newHook.NewPhoneInfo;
import hyj.xw.model.DeviceInfo;
import hyj.xw.model.LitePalModel.AppConfig;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.model.StartRunningConfig;
import hyj.xw.modelHttp.Device;
import hyj.xw.modelHttp.MaintainResultVO;
import hyj.xw.modelHttp.PhoneQueryVO;
import hyj.xw.modelHttp.ResponseData;
import hyj.xw.service.HttpRequestService;
import hyj.xw.service.SmsReciver;
import hyj.xw.thread.MonitorStatusThread;
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
    EditText hostEditText;
    EditText deviceEditText;
    EditText usernameEditText;
    EditText passwordEditText;
    CheckBox isAirChangeIpCheckBox;
    CheckBox isFeedCheckBox;
    CheckBox loginSucessPauseCheckBox;
    CheckBox isLocalSettingCheckBox;
    CheckBox gj008CheckBox;

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
        //服务器配置
        isLocalSettingCheckBox = (CheckBox) this.findViewById(R.id.isLocalSetting);
        String isLocalSettingValue = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_LOCAL_SETTING);
        isLocalSettingCheckBox.setChecked("0".equals(isLocalSettingValue) || TextUtils.isEmpty(isLocalSettingValue) ? false : true);
        isLocalSettingCheckBox.setOnClickListener(this);
        //登录成功暂停
        loginSucessPauseCheckBox = (CheckBox) this.findViewById(R.id.loginSucessPause);
        loginSucessPauseCheckBox.setChecked("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_LOGIN_PAUSE)) ? true : false);
        loginSucessPauseCheckBox.setOnClickListener(this);
        //008改机
        gj008CheckBox = (CheckBox) this.findViewById(R.id.gj008);
        gj008CheckBox.setChecked("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_GJ008)) ? true : false);
        gj008CheckBox.setOnClickListener(this);
        //综合参数
        editText = (EditText) findViewById(R.id.ext);
        String c = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_EXT);
        editText.setText(TextUtils.isEmpty(c) ? "0" : c);
        //服务器地址
        hostEditText = (EditText) findViewById(R.id.host);
        String hostValue = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_HOST);
        hostEditText.setText(TextUtils.isEmpty(hostValue) ? "120.78.134.230:8080" : hostValue);
        //设备编号
        deviceEditText = (EditText) findViewById(R.id.deviceNum);
        String deviceNum = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_DEVICE);
        deviceEditText.setText(TextUtils.isEmpty(deviceNum) ? "" : deviceNum);
        //帐号
        usernameEditText = (EditText) findViewById(R.id.username);
        String username = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_USERNAME);
        usernameEditText.setText(TextUtils.isEmpty(username) ? "" : username);
        //密码
        passwordEditText = (EditText) findViewById(R.id.password);
        String password = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_PWD);
        passwordEditText.setText(TextUtils.isEmpty(password) ? "" : password);
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
        //测试连接
        Button testConn = (Button) this.findViewById(R.id.test_conn);
        testConn.setOnClickListener(this);
        //开始运行uiAuto
        Button startUiAuto = (Button) this.findViewById(R.id.start_uiAuto);
        startUiAuto.setOnClickListener(this);

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
        //主机地址
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_HOST, hostEditText.getText().toString());
        //本地配置
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_LOCAL_SETTING, isLocalSettingCheckBox.isChecked() ? "1" : "0");
        //008改机
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_GJ008, gj008CheckBox.isChecked() ? "1" : "0");
        //设备编号
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_DEVICE, deviceEditText.getText().toString());
        //帐号
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_USERNAME, usernameEditText.getText().toString());
        //密码
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_PWD, passwordEditText.getText().toString());

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

    public  Device getDeviceConfig(){
        String srConfigStr = FileUtil.readAllUtf8(FilePathCommon.startRunninConfigTxtPath);
        Device srConfig = JSONObject.parseObject(srConfigStr,Device.class);
        return srConfig;
    }
    public  void saveDeviceConfig(Device device){
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.startRunninConfigTxtPath, JSON.toJSONString(device));
    }

    public void testMethod() {
        set008Data();
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

    private void delAllScreenshots(){
        String  path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Screenshots";
        File file = new File(path);
        if(file.exists()){
            boolean flag = file.delete();
            System.out.println("del Screenshots-->"+flag);
        }else {
            System.out.println("del Screenshots--> is null");
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
            case R.id.test_conn:
                AutoUtil.getRunningApp();
                testConn();
                break;
            case R.id.start_uiAuto:
                startUiAuto();
                break;
        }
    }

    public void testConn(){
        save();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String deviceNum = deviceEditText.getText().toString();
                    if(TextUtils.isEmpty(deviceNum)){
                        AutoUtil.showToastByRunnable(MainActivity.this,"设备编号不能为空");
                        return;
                    }
                    HttpRequestService httpRequestService = new HttpRequestService();
                    String loginRes = httpRequestService.login();
                    if(!"成功".equals(loginRes)){
                        AutoUtil.showToastByRunnable(MainActivity.this,loginRes);
                        return;
                    }
                    String res = httpRequestService.deviceConnectServer();
                    AutoUtil.showToastByRunnable(MainActivity.this,res);
                }catch (Exception e){
                    AutoUtil.showToastByRunnable(MainActivity.this,"参数异常，核对填写是否正确");
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public String initDeviceConfig2Txt(){
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.fkFilePath,"");
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"");
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.startRunninConfigTxtPath,"");
        String result = "";
        Device device = null;
        if(isLocalSettingCheckBox.isChecked()){
            //本地配置
            device = new Device();
            device.setRunState(1);
            device.setRunType(isFeedCheckBox.isChecked()?2:1);//养号 注册
            device.setChangeIpMode(isAirChangeIpCheckBox.isChecked()?2:1);
            device.setHookType(gj008CheckBox.isChecked()?2:1);
            FileUtil.writeContent2FileForceUtf8(FilePathCommon.stopTxtPath,"1");//暂停标志 1、正常；2、暂停
            device.setSendFriends(1);
            device.setExtractWxId(2);
            device.setAddFriend(1);
            device.setHookType(2);
            device.setChangeIp(1);//重置状态 状态为2是修改ip

        }else {
            //服务器获取配置
            String deviceNum = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_DEVICE);
            if(TextUtils.isEmpty(deviceNum)){
                result = "设备编号不能为空";
                AutoUtil.showToastByRunnable(MainActivity.this,result);
                return result;
            }
            HttpRequestService httpRequestService = new HttpRequestService();
            String res = httpRequestService.getStartConifgFromServer(deviceNum);
            if(TextUtils.isEmpty(res)||!res.contains("data")){
                result = "1、获取服务器配置信息失败\n"+res;
                AutoUtil.showToastByRunnable(MainActivity.this,result);
                return result;
            }
            ResponseData responseData = JSONObject.parseObject(res,ResponseData.class);
            System.out.println("OkHttpUtil getStartConifgFromServer responseData--->"+JSON.toJSONString(responseData));
            device = JSONObject.parseObject(responseData.getData(),Device.class);
            if(device==null||TextUtils.isEmpty(device.getNum())){
                result = "2、获取服务器配置信息失败\n"+res;
                AutoUtil.showToastByRunnable(MainActivity.this,result);
                return result;
            }
            System.out.println("OkHttpUtil getStartConifgFromServer device--->"+JSON.toJSONString(device));
            device.setHost("http://"+AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_HOST));
            device.setToken(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_WY_TOKEN));
            device.setUsername(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_USERNAME));
            device.setChangeIp(1);//重置状态 状态为2是修改ip
        }
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.stopTxtPath,device.getRunState()+"");//暂停标志 1、正常；2、暂停
        saveDeviceConfig(device);
        return result;
    }

    public void startUiAuto(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("main-->start========");
                //初始化配置信息
                String result = initDeviceConfig2Txt();
                if(!"".equals(result)) return;
                AutoUtil.execShell("am force-stop hyj.autooperation");
                FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"retry");//next登录下一个，retry新登录,首次开启也是retry
                //AutoUtil.execShell("am instrument -w -r   -e debug false -e class hyj.autooperation.ExampleInstrumentedTest#useAppContext hyj.autooperation.test/android.support.test.runner.AndroidJUnitRunner");
            }
        }).start();

        class StartUiautoThread extends Thread{
            @Override
            public void run() {
                System.out.println("main--doAction-->开启StartUiautoThread");
                AutoUtil.execShell("am instrument -w -r   -e debug false -e class hyj.autooperation.ExampleInstrumentedTest#useAppContext hyj.autooperation.test/android.support.test.runner.AndroidJUnitRunner");
            }
        }

        class StartChangeIpThread extends Thread{
            @Override
            public void run() {
                System.out.println("main--doAction-->StartChangeIpThread");
                AutoUtil.execShell("am instrument -w -r   -e debug false -e class hyj.autooperation.ExampleInstrumentedTest#changeIp hyj.autooperation.test/android.support.test.runner.AndroidJUnitRunner");
            }
        }

        //暂停标准监测
        class StopThread extends Thread{
            @Override
            public void run() {
                HttpRequestService httpRequestService = new HttpRequestService();
                String deviceNum = deviceEditText.getText().toString();
                int coutDisNet=0;
                while (true){
                    System.out.println("StopThread-->"+coutDisNet);
                    AutoUtil.sleep(1500);
                    try {
                        String res = httpRequestService.getStartConifgFromServer(deviceNum);
                        if("".equals(res)){
                            coutDisNet = coutDisNet+1;
                            if(coutDisNet>40){
                                coutDisNet = 0;
                                System.out.println("HttpRequestService getPhone--doAction-->连接异常飞行");
                                AutoUtil.execShell("svc wifi disable");
                                AutoUtil.sleep(5000);
                                AutoUtil.execShell("svc wifi enable");
                                AutoUtil.startAppByPackName("hyj.xw","hyj.xw.MainActivity");
                            }
                            continue;
                        }
                        coutDisNet = 0;
                        ResponseData responseData = JSONObject.parseObject(res,ResponseData.class);
                        Device device = JSONObject.parseObject(responseData.getData(),Device.class);
                        if(3==device.getRunState()){//停止
                            System.out.println("doAction--->停止am force-stop hyj.autooperation");
                            AutoUtil.execShell("am force-stop hyj.autooperation");
                            AutoUtil.sleep(2000);
                            continue;
                        }
                        FileUtil.writeContent2FileForceUtf8(FilePathCommon.stopTxtPath,device.getRunState()+"");//暂停标志 1、正常；2、暂停
                        System.out.println("StopThread-->runState:"+device.getRunState());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

       new Thread(new Runnable() {
           HttpRequestService httpRequestService = new HttpRequestService();
           int loginIndex=0;
           List<Wx008Data> wx008Datas=null ;
           Device device = null;
           Wx008Data currentWx008Data=null;
           @Override
           public void run() {
               if(!isLocalSettingCheckBox.isChecked()) new StopThread().start();
               //delAllFile();
               while (true){
                   try {
                       AutoUtil.sleep(1000);
                       device = getDeviceConfig();
                       String tag = FileUtil.readAllUtf8(FilePathCommon.setEnviromentFilePath);
                       System.out.println(Thread.currentThread().getName()+"main-->currentSrc:"+JSON.toJSONString(device)+" 当前tag:"+tag);
                       /**
                        * 回写登录结果
                        */
                       if(!TextUtils.isEmpty(device.getLoginResult())){
                           currentWx008Data.setExpMsg(device.getLoginResult());
                           //if(!TextUtils.isEmpty(device.getWxid())) currentWx008Data.setWxid19(device.getWxid());
                           int cn = DaoUtil.updateExpMsg(currentWx008Data,currentWx008Data.getExpMsg()+"-"+AutoUtil.getCurrentDate());
                           String recordTxt = loginIndex+" msg:"+currentWx008Data.getExpMsg()+" "+currentWx008Data.getPhone()+" "+currentWx008Data.getWxPwd()+" ip:"+device.getIpAddress();
                           LogUtil.login("",recordTxt);
                           System.out.println("main-->doAction--->main-->updateExpMsg:"+device.getLoginResult()+" cn:"+cn+" recordTxt:"+recordTxt);
                           if(!isLocalSettingCheckBox.isChecked()){//服务器
                               if(device.getRunType()==2){
                                   MaintainResultVO maintainResultVO = createMaintainResult(currentWx008Data,device);
                                   String json = JSON.toJSONString(maintainResultVO);
                                   System.out.println("main-->doAction-->修改维护状态req:"+json);
                                   String res = httpRequestService.updateMaintainStatus(json);
                                   System.out.println("main-->doAction--->修改维护状态res："+res);
                               }else if(device.getRunType()==1){
                                   String loginResult = device.getLoginResult();
                                   System.out.println("main-->doAction--->zc完成："+loginResult);
                                   if(loginResult.contains("success")){//zc成功
                                       currentWx008Data.setId(null);
                                       String json = JSON.toJSONString(currentWx008Data);
                                       System.out.println("main-->doAction--->注册完成上传数据currentWx008Data："+json);
                                       String res = httpRequestService.uploadPhoneData(json);
                                       if(!"".equals(res)){//返回更新成功id，update wxid用到
                                           currentWx008Data.setId(Long.parseLong(res));
                                       }
                                       System.out.println("main-->doAction--->注册完成上传数据res："+res);
                                   }
                                   String res1 = httpRequestService.updateRegStatus(currentWx008Data.getPhone(),loginResult);
                                   System.out.println("main-->doAction--->更新手机注册状态res："+res1);
                               }
                           }
                           device.setLoginResult("");
                           saveDeviceConfig(device);
                       }else if(!TextUtils.isEmpty(device.getWxid())){
                           if(!isLocalSettingCheckBox.isChecked()){
                               Wx008Data wx008Data = new Wx008Data();
                               wx008Data.setId(currentWx008Data.getId());
                               wx008Data.setWxid19(device.getWxid());
                               String json = JSON.toJSONString(wx008Data);
                               System.out.println("main-->doAction--->上传wxid json："+json);
                               String res = httpRequestService.uploadPhoneData(json);
                               System.out.println("main-->doAction--->上传wxid res："+res);
                           }
                           updateWxid(currentWx008Data,device);//更新wxid
                       }else if("1".equals(device.getLastIpAddress())){
                           String ip = getIp();
                           device.setLastIpAddress(ip);
                           saveDeviceConfig(device);
                       }else if(!TextUtils.isEmpty(device.getCallNumber())){
                           String res = httpRequestService.sendSms(device.getCallNumber(),device.getCalledNumber(),device.getContent());
                           System.out.println("main-->doAction--->发送短信返回res:"+res);
                           device.setCallNumber("");
                           saveDeviceConfig(device);
                       }else if(device.getChangeIp()==2){
                           device.setChangeIp(1);
                           saveDeviceConfig(device);
                           new StartUiautoThread().start();
                       }

                       /**
                        * 获取008数据并 环境设置标志
                        */
                       if("next".equals(tag)||"retry".equals(tag)){
                           AutoUtil.killAndClearWxData();
                           String setWxDataResult = setWx008Data(tag);//获取008数据
                           if(!"".equals(setWxDataResult)||currentWx008Data==null){
                               System.out.println("doAction--->setWx008DataResult:"+setWxDataResult);
                               continue;
                           }
                           if(device.getHookType()==2){
                               set008Environment(currentWx008Data);
                           }else {
                               setEnviroment(currentWx008Data);//修改hook文件
                           }
                           FileUtil.writeContent2FileForceUtf8(FilePathCommon.wx008DataFilePath,JSON.toJSONString(currentWx008Data));//写入008j数据，供对方用
                           FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"done");
                           System.out.println("main-->doAction--->mainActivity环境和currentData已准备，写入done标志完成");
                           //new StartUiautoThread().start();
                           new StartChangeIpThread().start();
                       }


                   }catch (Exception e){
                       System.out.println("main-->doAction-->全局异常 mainActivity");
                       e.printStackTrace();
                   }
               }

           }
           public String getIp(){
               String ipUrl = "http://pv.sohu.com/cityjson?ie=utf-8";
               String ipStr = null;
               try {
                   ipStr = OkHttpUtil.okHttpGet(ipUrl);
               } catch (IOException e) {
                   AutoUtil.startAppByPackName("hyj.xw","hyj.xw.MainActivity");
                   e.printStackTrace();
               }
               System.out.println("main-->doActioni--->res ipStr:"+ipStr);
               String ip = "失败";
               if(ipStr.contains("广东")){
                   ip = "广东";
               }else if(ipStr.contains("cip")){
                   JSONObject jsonObject = JSONObject.parseObject(ipStr.substring(ipStr.indexOf("{"),ipStr.indexOf("}")+1));
                   ip = jsonObject.getString("cname")+jsonObject.getString("cip");
               }
               //updateDeviceConfigIp(ip);
               return ip;
           }

           public void updateWxid(Wx008Data currentWx008Data,Device device){
               currentWx008Data.setWxid19(device.getWxid());
               int cn = currentWx008Data.updateAll("phone=?",currentWx008Data.getPhone());
               System.out.println("main-->doAction--->更新cn:"+cn+" wxid:"+device.getWxid());
               device.setWxid("");
               saveDeviceConfig(device);
           }

           public MaintainResultVO createMaintainResult(Wx008Data currentWx008Data,Device device){
               String expMsg = device.getLoginResult();
               int dieFlag = 0;
               if(expMsg.contains("密码错误")){
                   dieFlag = 1;
               }else if(expMsg.contains("帐号的使用存在异常")){
                   dieFlag = 2;
               }else if(expMsg.contains("操作频率过快")){
                   dieFlag = 3;
               }else if(expMsg.contains("登录环境异常")){
                   dieFlag = 4;
               }else if(expMsg.contains("新设备")){
                   dieFlag = 5;
               }else if(expMsg.contains("外挂")){
                   dieFlag = 6;
               }else if(expMsg.contains("长期未登录")){
                   dieFlag = 7;
               }else if(expMsg.contains("该微信帐号因批量")){
                   dieFlag = 8;
               }else if(expMsg.contains("已售")){
                   dieFlag = 98;
               }else if(expMsg.contains("作废")){
                   dieFlag = 99;
               }
               MaintainResultVO maintainResultVO = new MaintainResultVO(currentWx008Data.getId(),dieFlag,expMsg,device.getIpAddress());
               return maintainResultVO;
           }


           public String setWx008Data(String tag){
               String result = "";
               try {
                   if(1==device.getRunType()){
                       if(tag.equals("next")||currentWx008Data==null){
                           String phone = httpRequestService.getPhone("");
                           if(TextUtils.isEmpty(phone)){
                               return "获取手机号失败";
                           }
                           if(device.getHookType()==2){
                               String dataStr008 = FileUtil.readAllUtf8(FilePathCommon.device008TxtPath);
                               System.out.println("main-->doAction--->获取008device.txt数据dataStr008:"+dataStr008);
                               currentWx008Data = PhoneConf.createRegDataByPhoneAndDeviceTxt(phone,dataStr008);
                           }else {
                               System.out.println("main-->doAction--->生成内部改机数据");
                               currentWx008Data = PhoneConf.createRegDataByPhone(phone);
                           }
                           currentWx008Data.save();
                           System.out.println("main-->doAction--->获取一份新改机wxData并保存");
                       }
                   }else if(2==device.getRunType()) {
                       if(isLocalSettingCheckBox.isChecked()){
                           if(currentWx008Data==null){
                               loginIndex = Integer.parseInt(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX));
                               wx008Datas = DaoUtil.getWx008Datas();
                               currentWx008Data = wx008Datas.get(loginIndex);
                           }else if(tag.equals("next")){
                               loginIndex = loginIndex+1;
                               AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_START_LOGIN_INDEX,loginIndex+"");
                               currentWx008Data = wx008Datas.get(loginIndex);
                           }
                           System.out.println("doAction--->获取本地维护数据:"+JSON.toJSONString(currentWx008Data));
                       }else {
                           currentWx008Data = httpRequestService.getMaintainData();
                           if(currentWx008Data==null) System.out.println("doAction--->获取维护数据失败 或 数据没有置入维护界面");
                           System.out.println("doAction--->获取远程维护数据:"+JSON.toJSONString(currentWx008Data));
                       }

                   }
               }catch (Exception e){
                   e.printStackTrace();
                   System.out.println("doAction---Exception setWx008Data");
               }
               return result;
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
    }
    private void set008Environment(Wx008Data currentWx008Data){
        try {
            String data008Str = currentWx008Data.getPhoneStrs();//008原始数据
            if(!TextUtils.isEmpty(currentWx008Data.getPhoneStrsAw())&&currentWx008Data.getPhoneStrsAw().contains("androidId")){
                System.out.println("main-->doAction--->npi数据");
                data008Str = PhoneConf.phoneStr2008Str(currentWx008Data.getPhoneStrsAw());//内部改机数据转008原始数据
                System.out.println("main-->doAction--->npi数据phoneStr2008Str："+data008Str);
            }
            File file = new File(FilePathCommon.device008TxtPath);
            if(file.exists()) file.delete();
            FileUtil.writeContent2FileForceUtf8(FilePathCommon.device008TxtPath,data008Str);
            String strs = FileUtil.readAllUtf8(FilePathCommon.device008TxtPath);
            System.out.println("main-->doAction-->008 str strs:"+strs);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("main-->doAction---Exception set008Environment");
        }
    }
    private void set008Data(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpRequestService service = new HttpRequestService();
                String res = service.getPhone("");
                System.out.println("res-->"+res);
            }
        }).start();
    }

}
