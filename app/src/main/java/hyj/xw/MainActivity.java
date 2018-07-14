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
import hyj.xw.conf.PhoneConf;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.flowWindow.MyWindowManager;
import hyj.xw.hook.newHook.NewPhoneInfo;
import hyj.xw.model.DeviceInfo;
import hyj.xw.model.LitePalModel.AppConfig;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.model.StartRunningConfig;
import hyj.xw.modelHttp.Device;
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
        hostEditText.setText(TextUtils.isEmpty(hostValue) ? "192.168.1.5:8080" : hostValue);
        //设备编号
        deviceEditText = (EditText) findViewById(R.id.deviceNum);
        String deviceNum = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_DEVICE);
        deviceEditText.setText(TextUtils.isEmpty(deviceNum) ? "" : deviceNum);
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
        //主机地址
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_HOST, hostEditText.getText().toString());
        //本地配置
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_LOCAL_SETTING, isLocalSettingCheckBox.isChecked() ? "1" : "0");
        //008改机
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_GJ008, gj008CheckBox.isChecked() ? "1" : "0");
        //设备编号
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_DEVICE, deviceEditText.getText().toString());

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

    String host = "http://192.168.1.5:8080";
    public void testMethod() {
        Wx008Data wx008Data = new Wx008Data();
        wx008Data.setPhone("13384081569");
        set008Environment(wx008Data);
       /* FileUtil.writeContent2FileForceUtf8(FilePathCommon.fkFilePath,"");
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"");
        //delAllFile();
        final Device device = new Device();
        device.setChangeIpMode(1);
        saveDeviceConfig(device);
*/

        /*new MonitorStatusThread().start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("main-->start========");
                AutoUtil.execShell("am force-stop hyj.autooperation");
                AutoUtil.execShell("am instrument -w -r   -e debug false -e class hyj.autooperation.ExampleInstrumentedTest#useAppContext hyj.autooperation.test/android.support.test.runner.AndroidJUnitRunner");
            }
        }).start();

        final HttpRequestService httpRequestService = new HttpRequestService();


        new Thread(new Runnable() {
            int loginIndex=0;
            @Override
            public void run() {
                Device device1 = getDeviceConfig();
                List<Wx008Data> wx008Datas=null ;
                Wx008Data currentWx008Data=null;//当前运行wx数据
                if(1==device1.getRunType()){//注册
                    System.out.println("main-->doAction---> 开始..获取一份新改机wxData并保存");
                    currentWx008Data = PhoneConf.createRegData();;//当前运行wx数据
                    currentWx008Data.save();
                }else if(2==device1.getRunType()) {//养号
                    if(isLocalSettingCheckBox.isChecked()){
                        loginIndex = Integer.parseInt(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX));
                        wx008Datas = DaoUtil.getWx008Datas();
                        currentWx008Data = wx008Datas.get(loginIndex);
                    }else {
                        currentWx008Data = httpRequestService.getMaintainData();
                    }
                }
                while (true){
                    String tag = FileUtil.readAllUtf8(FilePathCommon.setEnviromentFilePath);
                    System.out.println("main-->currentSrc:"+JSON.toJSONString(device1)+" 监听环境设置标志:"+tag);
                    *//**
                     * 环境设置标志
                     *//*
                    if("next".equals(tag)||"retry".equals(tag)){
                        if("注册".equals(1==device1.getRunType())){
                            if(tag.equals("next")){
                                currentWx008Data = PhoneConf.createRegData();
                                currentWx008Data.save();
                                System.out.println("main-->doAction--->获取一份新改机wxData并保存");
                            }
                        }else if("养号".equals(2==device1.getRunType())) {
                            if(tag.equals("next")){
                                if(isLocalSettingCheckBox.isChecked()){
                                    doNextIndexAndRecord2DB();
                                    currentWx008Data = wx008Datas.get(loginIndex);
                                    System.out.println("main-->doAction--->loginIndex+1 获取下一个");
                                }else {
                                    currentWx008Data = httpRequestService.getMaintainData();
                                }
                            }
                        }
                        setEnviroment(currentWx008Data);//修改hook文件
                        FileUtil.writeContent2FileForceUtf8(FilePathCommon.wx008DataFilePath,JSON.toJSONString(currentWx008Data));//写入008j数据，供对方用
                        FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"done");
                        System.out.println("main-->doAction--->mainActivity环境和currentData已准备，写入done标志完成");
                    }
                     device1 = getDeviceConfig();
                     if(!TextUtils.isEmpty(device1.getLoginResult())){
                         if(isLocalSettingCheckBox.isChecked()){//本地
                             currentWx008Data.setExpMsg(device1.getLoginResult());
                             int cn = DaoUtil.updateExpMsg(currentWx008Data,currentWx008Data.getExpMsg()+"-"+AutoUtil.getCurrentDate());
                             System.out.println("main-->updateExpMsg:"+device1.getLoginResult()+" cn:"+cn);
                         }else {//服务器
                             httpRequestService.updateMaintainStatus(currentWx008Data.getId(),0);
                         }
                         device1.setLoginResult("");
                         saveDeviceConfig(device1);
                     }
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

        }).start();*/
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
   /* private void delAllFile(){
       *//* File file = new File(FilePathCommon.fkScreenShotPath);
        if(file.exists()){
            file.delete();
        }*//*
        String  path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath()+"/Screenshots";
        File[] files = new File(path).listFiles();
        if(files!=null&&files.length>0){
            for(File file:files){
                System.out.println("main-->删除文件："+file.getName()+" "+file.delete());
            }
        }
    }*/
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
                testConn();
                break;
            case R.id.start_uiAuto:
                startUiAuto();
                break;
        }
    }

    public void testConn(){
        delAllScreenshots();
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("testConn OkHttpUtil deviceConnectServer url--->2");
                String deviceNum = deviceEditText.getText().toString();
                if(TextUtils.isEmpty(deviceNum)){
                    AutoUtil.showToastByRunnable(MainActivity.this,"设备编号不能为空");
                    return;
                }
                HttpRequestService httpRequestService = new HttpRequestService();
                String res = httpRequestService.deviceConnectServer(deviceNum);
                System.out.println("testConn OkHttpUtil deviceConnectServer res--->"+res);
                AutoUtil.showToastByRunnable(MainActivity.this,res);
                System.out.println("testConn OkHttpUtil deviceConnectServer url--->2");
            }
        }).start();
    }

    public void initDeviceConfig2Txt(){
        Device device = null;
        if(isLocalSettingCheckBox.isChecked()){
            //本地配置
            device = new Device();
            device.setRunState(1);
            device.setRunType(isFeedCheckBox.isChecked()?2:1);//养号 注册
            device.setChangeIpMode(isAirChangeIpCheckBox.isChecked()?2:1);
            device.setIs008Gj(gj008CheckBox.isChecked()?1:0);

        }else {
            //服务器获取配置
            String deviceNum = deviceEditText.getText().toString();
            if(TextUtils.isEmpty(deviceNum)){
                AutoUtil.showToastByRunnable(MainActivity.this,"设备编号不能为空");
                return;
            }
            HttpRequestService httpRequestService = new HttpRequestService();
            String res = httpRequestService.getStartConifgFromServer(deviceNum);
            if(TextUtils.isEmpty(res)||res.contains("data")){
                AutoUtil.showToastByRunnable(MainActivity.this,"1、获取服务器配置信息失败\n"+res);
                return;
            }
            ResponseData responseData = JSONObject.parseObject(res,ResponseData.class);
            device = JSONObject.parseObject(responseData.getData(),Device.class);
            if(device==null||TextUtils.isEmpty(device.getNum())){
                AutoUtil.showToastByRunnable(MainActivity.this,"2、获取服务器配置信息失败\n"+res);
                return;
            }
            System.out.println("OkHttpUtil getStartConifgFromServer device--->"+JSON.toJSONString(device));
        }
        saveDeviceConfig(device);
    }

    public void startUiAuto(){
        //初始化配置信息
        initDeviceConfig2Txt();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("main-->start========");
                AutoUtil.execShell("am force-stop hyj.autooperation");
                AutoUtil.execShell("am instrument -w -r   -e debug false -e class hyj.autooperation.ExampleInstrumentedTest#useAppContext hyj.autooperation.test/android.support.test.runner.AndroidJUnitRunner");
            }
        }).start();

       new Thread(new Runnable() {
           HttpRequestService httpRequestService = new HttpRequestService();
           int loginIndex=0;
           List<Wx008Data> wx008Datas=null ;
           Device device = null;
           Wx008Data currentWx008Data=null;
           @Override
           public void run() {
               FileUtil.writeContent2FileForceUtf8(FilePathCommon.fkFilePath,"");
               FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"");
               //delAllFile();
               while (true){
                   try {
                       AutoUtil.sleep(1000);
                       device = getDeviceConfig();
                       String tag = FileUtil.readAllUtf8(FilePathCommon.setEnviromentFilePath);
                       System.out.println("main-->currentSrc:"+JSON.toJSONString(device)+" 当前tag:"+tag);
                       /**
                        * 获取008数据并 环境设置标志
                        */
                       if("next".equals(tag)||"retry".equals(tag)){
                           setWx008Data(tag);
                           if(device.getIs008Gj()==1){
                               set008Environment(currentWx008Data);
                           }else {
                               setEnviroment(currentWx008Data);//修改hook文件
                           }
                           FileUtil.writeContent2FileForceUtf8(FilePathCommon.wx008DataFilePath,JSON.toJSONString(currentWx008Data));//写入008j数据，供对方用
                           FileUtil.writeContent2FileForceUtf8(FilePathCommon.setEnviromentFilePath,"done");
                           System.out.println("main-->doAction--->mainActivity环境和currentData已准备，写入done标志完成");
                       }
                       /**
                        * 回写登录结果
                        */
                       if(!TextUtils.isEmpty(device.getLoginResult())){
                           if(isLocalSettingCheckBox.isChecked()){//本地
                               currentWx008Data.setExpMsg(device.getLoginResult());
                               if(TextUtils.isEmpty(currentWx008Data.getWxid19())
                                       ||(currentWx008Data.getWxid19()!=null&&!currentWx008Data.getWxid19().contains("wxid_"))){
                                   currentWx008Data.setWxid19(device.getWxid());
                               }
                               int cn = DaoUtil.updateExpMsg(currentWx008Data,currentWx008Data.getExpMsg()+"-"+AutoUtil.getCurrentDate());
                               LogUtil.login(loginIndex+" msg:"+currentWx008Data.getExpMsg(),currentWx008Data.getPhone()+" "+currentWx008Data.getWxId()+" "+currentWx008Data.getWxPwd()+" ip:"+device.getIpAddress());
                               System.out.println("doAction--->main-->updateExpMsg:"+device.getLoginResult()+" cn:"+cn);
                           }else {//服务器
                               httpRequestService.updateMaintainStatus(currentWx008Data.getId(),0);
                           }
                           device.setLoginResult("");
                           saveDeviceConfig(device);
                       }
                   }catch (Exception e){
                       System.out.println("main-->全局异常");
                       e.printStackTrace();
                   }
               }

           }


           public void setWx008Data(String tag){
               if(1==device.getRunType()){
                   if(tag.equals("next")||currentWx008Data==null){
                       currentWx008Data = PhoneConf.createRegData();
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
                   }else {
                       currentWx008Data = httpRequestService.getMaintainData();
                   }

               }
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
       File[] files = FileUtil.readFileInFolderByPath(FilePathCommon.sl008DataPath);
       for(File file:files){
           System.out.println("main-->008 str fileName:"+file.getName());
           if(file.getName().equals(currentWx008Data.getPhone()+".txt")){
               String str = FileUtil.readAllUtf8ByFile(file);
               FileUtil.writeContent2FileForceUtf8(FilePathCommon.device008TxtPath,str);
               String strs = FileUtil.readAllUtf8(FilePathCommon.device008TxtPath);
               System.out.println("main-->008 str strs:"+strs);
               break;
           }
       }
    }
}
