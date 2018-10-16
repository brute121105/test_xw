package hyj.xw;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
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

import java.io.File;
import java.io.IOException;

import hyj.xw.activity.ApiSettingActivity;
import hyj.xw.activity.AppSettingActivity;
import hyj.xw.activity.AutoLoginSettingActivity;
import hyj.xw.activity.DataImpExpActivity;
import hyj.xw.activity.ServerConfigActivity;
import hyj.xw.common.CommonConstant;
import hyj.xw.common.FilePathCommon;
import hyj.xw.conf.PhoneConf;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.flowWindow.MyWindowManager;
import hyj.xw.model.DeviceInfo;
import hyj.xw.model.LitePalModel.AppConfig;
import hyj.xw.modelHttp.Apk;
import hyj.xw.modelHttp.Device;
import hyj.xw.modelHttp.ResponseData;
import hyj.xw.service.HttpRequestService;
import hyj.xw.service.SmsReciver;
import hyj.xw.task.DownLoadAPkListener;
import hyj.xw.thread.AutoStopThread;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.ContactUtil;
import hyj.xw.util.DeviceParamUtil;
import hyj.xw.util.FileUtil;
import hyj.xw.util.GetFutureResultUtil;
import hyj.xw.util.GetPermissionUtil;
import hyj.xw.util.OkHttpUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText editText;
    EditText cnNumEditText;
    CheckBox isAirChangeIpCheckBox;
    CheckBox isFeedCheckBox;
    CheckBox loginSucessPauseCheckBox;
    CheckBox isLocalSettingCheckBox;
    CheckBox gj008CheckBox;
    CheckBox lockBtnBox;
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

        //配置连接
        Button serverConfigConnect = (Button) this.findViewById(R.id.bth_serverConfig);
        serverConfigConnect.setOnClickListener(this);


        //申请root1
        Button reqRoot = (Button) this.findViewById(R.id.reqRoot);
        reqRoot.setOnClickListener(this);
        //申请root2
        Button reqRoot2 = (Button) this.findViewById(R.id.reqRoot2);
        reqRoot2.setOnClickListener(this);
        //附件下载
        Button downloadAttach = (Button) this.findViewById(R.id.downloadAttach);
        downloadAttach.setOnClickListener(this);
        //版本更新
        Button updateApk = (Button) this.findViewById(R.id.updateApk);
        updateApk.setOnClickListener(this);

        //锁定按钮
        lockBtnBox = (CheckBox) this.findViewById(R.id.lock_btn);
        lockBtnBox.setOnClickListener(this);

        //登录连接服务器并启动脚本
        AutoStopThread stopThread = AutoStopThread.getInstance();
        if(!stopThread.isAlive()){
            stopThread.start();
        }
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
        //ext
        AppConfig config = new AppConfig(CommonConstant.APPCONFIG_EXT, editText.getText().toString());
        AppConfigDao.saveOrUpdate(config);
        //国别
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_CN_NUM, cnNumEditText.getText().toString());
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_AIR_CHANGE_IP, isAirChangeIpCheckBox.isChecked() ? "1" : "0");

        //本地配置
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_LOCAL_SETTING, isLocalSettingCheckBox.isChecked() ? "1" : "0");
        //008改机
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_GJ008, gj008CheckBox.isChecked() ? "1" : "0");

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


    public  void saveDeviceConfig(Device device){
        if(device!=null){
            FileUtil.writeContent2FileForceUtf8(FilePathCommon.startRunninConfigTxtPath, JSON.toJSONString(device));
        }
    }

    public void testMethod() {

    }

    public void clearAppData() {
        AutoUtil.execShell("am force-stop hyj.autooperation");
        //AutoUtil.clearAppData();
        Toast.makeText(MainActivity.this, "已执行", Toast.LENGTH_LONG).show();
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
                //startActivity(new Intent(MainActivity.this, YhSettingActivity.class));
                break;
            case R.id.btn_data_impExp:
                startActivity(new Intent(MainActivity.this, DataImpExpActivity.class));
                break;
            case R.id.btn_kill_app:
                System.out.println("doAction---");
                AutoUtil.execShell("am force-stop hyj.xw");
                AutoUtil.killApp();
                break;
            case R.id.reqRoot:
                reqRoot();
                break;
            case R.id.reqRoot2:
                reqRoot2();
                break;
            case R.id.downloadAttach:
                downloadAttach(false);
                break;
            case R.id.updateApk:
                updateApk();
                break;
            case R.id.bth_serverConfig:
                startActivity(new Intent(MainActivity.this, ServerConfigActivity.class));
                break;
            case R.id.lock_btn:
                String str = FileUtil.readAllUtf8(FilePathCommon.device008TxtPath);
                System.out.println("doAction--->str:"+str);
                lockBtn();
                break;
        }
    }
    //锁定按钮
    public void lockBtn(){
        //AutoUtil.killRunningApp();
        Button serverConfigConnect = (Button) this.findViewById(R.id.bth_serverConfig);//配置连接
        Button clearAppData = (Button) this.findViewById(R.id.clearAppData);//停止脚本
        Button btn_kill_app = (Button) this.findViewById(R.id.btn_kill_app);//关闭
        Button reqRoot = (Button) this.findViewById(R.id.reqRoot);//申请root1
        Button reqRoot2 = (Button) this.findViewById(R.id.reqRoot2);//申请root2
        Button downloadAttach = (Button) this.findViewById(R.id.downloadAttach);//附件下载
        Button updateApk = (Button) this.findViewById(R.id.updateApk);//附件下载
        if(lockBtnBox.isChecked()){
             serverConfigConnect.setClickable(false);
             clearAppData.setClickable(false);
             btn_kill_app.setClickable(false);
             reqRoot.setClickable(false);
             reqRoot2.setClickable(false);
             downloadAttach.setClickable(false);
             updateApk.setClickable(false);
         }else {
            serverConfigConnect.setClickable(true);
            clearAppData.setClickable(true);
            btn_kill_app.setClickable(true);
            reqRoot.setClickable(true);
            reqRoot2.setClickable(true);
            downloadAttach.setClickable(true);
            updateApk.setClickable(true);
         }
    }
    public void downloadAttach(boolean isAlertWindow){
        Apk apk = GetFutureResultUtil.checkVersion1("2");
        if(apk!=null){
            DownLoadAPkListener downLoadAPkListener = new DownLoadAPkListener(this,FilePathCommon.downAPk2Path,"2",apk);
            downLoadAPkListener.downloadAttach(isAlertWindow);
            AutoUtil.sleep(5000);
        }
    }
    public void reqRoot(){
        AutoUtil.clickXY(0,0);
        Toast.makeText(this,"root1已发送请求，请到安全中心设置azy获取root", Toast.LENGTH_SHORT).show();
    }
    public void reqRoot2(){
        FileUtil.copyAssetFile("hyj.autooperation.test","/sdcard/");
        FileUtil.copyAssetFile("hyj.autooperation","/sdcard/");


        AutoUtil.execShell("cp /sdcard/hyj.autooperation /data/local/tmp/");
        AutoUtil.execShell("chmod 777 /data/local/tmp/hyj.autooperation");
        AutoUtil.execShell("pm install -r \"/data/local/tmp/hyj.autooperation\"");

        installUiauto();
        AutoUtil.execShell("am instrument -w -r   -e debug false -e class hyj.autooperation.ExampleInstrumentedTest#installTest hyj.autooperation.test/android.support.test.runner.AndroidJUnitRunner");
        Toast.makeText(this,"root2已发送请求，请到安全中心设置autooperation获取root", Toast.LENGTH_SHORT).show();

    }
    public void installUiauto(){
        System.out.println("doAction--->即将开始安装auto-------------------------");
        AutoUtil.execShell("cp /sdcard/hyj.autooperation.test /data/local/tmp/");
        AutoUtil.execShell("chmod 777 /data/local/tmp/hyj.autooperation.test");
        AutoUtil.execShell("pm install -r \"/data/local/tmp/hyj.autooperation.test\"");
        File file = new File(FilePathCommon.downAPk2Path);
        if(file.exists()) {
            boolean flag = file.delete();
            System.out.println("doAction--->删除auto："+flag);
        }
    }
    //更新app
    public void updateApk(){
        Apk apk = GetFutureResultUtil.checkVersion1("1");
        if(apk!=null){
            DownLoadAPkListener downLoadAPkListener = new DownLoadAPkListener(this,FilePathCommon.downAPk1Path,"1",apk);
            downLoadAPkListener.checkVersion();
        }
    }




    //开启uiauto线程类，ip修改完后触发
    class StartUiautoThread extends Thread{
        @Override
        public void run() {
            System.out.println("main--doAction-->开启StartUiautoThread");
            AutoUtil.execShell("am instrument -w -r   -e debug false -e class hyj.autooperation.ExampleInstrumentedTest#useAppContext hyj.autooperation.test/android.support.test.runner.AndroidJUnitRunner");
        }
    }






}
