package hyj.xw;

import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
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

import org.litepal.crud.DataSupport;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import hyj.xw.activity.ApiSettingActivity;
import hyj.xw.activity.AppSettingActivity;
import hyj.xw.activity.AutoLoginSettingActivity;
import hyj.xw.activity.YhSettingActivity;
import hyj.xw.common.CommonConstant;
import hyj.xw.conf.PhoneConf;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.flowWindow.MyWindowManager;
import hyj.xw.model.DeviceInfo;
import hyj.xw.model.LitePalModel.AppConfig;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.service.SmsReciver;
import hyj.xw.test.GetPhoneInfoUtil;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.DaoUtil;
import hyj.xw.util.DeviceParamUtil;
import hyj.xw.util.FileUtil;
import hyj.xw.util.GetPermissionUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.OkHttpUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    EditText editText;
    EditText cnNumEditText;
    CheckBox isFeedCheckBox;
    CheckBox loginSucessPauseCheckBox;

    private String[] phoneStrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AutoUtil.clickXY(0,0);
        GetPermissionUtil.getReadAndWriteContactPermision(this,MainActivity.this);
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
        if (Build.VERSION.SDK_INT < 23) {
            MyWindowManager.createSmallWindow(getApplicationContext());
            MyWindowManager.createSmallWindow2(getApplicationContext());
        }


        //养号
        isFeedCheckBox = (CheckBox)this.findViewById(R.id.isFeed);
        isFeedCheckBox.setChecked("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_FEED))?true:false);
        isFeedCheckBox.setOnClickListener(this);
        //登录成功暂停
        loginSucessPauseCheckBox = (CheckBox)this.findViewById(R.id.loginSucessPause);
        loginSucessPauseCheckBox.setChecked("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_LOGIN_PAUSE))?true:false);
        loginSucessPauseCheckBox.setOnClickListener(this);
        //综合参数
        editText = (EditText)findViewById(R.id.ext);
        String c = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_EXT);
        editText.setText(TextUtils.isEmpty(c)?"0":c);
        //国别
        cnNumEditText = (EditText)findViewById(R.id.cnNum);
        String cnNum = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_CN_NUM);
        cnNumEditText.setText(TextUtils.isEmpty(cnNum)?"86":cnNum);

       /*
        下拉框数据开始
        */
        phoneStrs = PhoneConf.getAllPhoneList();
        Spinner spinner = (Spinner) findViewById(R.id.Spinner01);
        setSpnnierStyleAndContents(spinner,phoneStrs);
        //设置默认值
        String loginIndex = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX);
        loginIndex = TextUtils.isEmpty(loginIndex)||Integer.parseInt(loginIndex)>phoneStrs.length-1?"0":loginIndex;
        spinner.setSelection(Integer.parseInt(loginIndex),true);//spinner下拉框默认值*/

        Spinner spinner02 = (Spinner) findViewById(R.id.Spinner02);
        setSpnnierStyleAndContents(spinner02,phoneStrs);
        //设置默认值
        String endLoginIndex = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_END_LOGIN_INDEX);
        endLoginIndex = TextUtils.isEmpty(endLoginIndex)||Integer.parseInt(endLoginIndex)>phoneStrs.length-1?"0":endLoginIndex;
        spinner02.setSelection(Integer.parseInt(endLoginIndex),true);//spinner下拉框默认值*/

       /* //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phoneStrs);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(this);
        //设置默认值
        spinner.setVisibility(View.VISIBLE);
        String loginIndex = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX);
        loginIndex = TextUtils.isEmpty(loginIndex)||Integer.parseInt(loginIndex)>phoneStrs.length-1?"0":loginIndex;
        spinner.setSelection(Integer.parseInt(loginIndex),true);//spinner下拉框默认值*/
         /*
        下拉框数据结束
        */

        Button openAssitBtn = (Button)this.findViewById(R.id.open_assist);
        Button autoLoginBtn = (Button)this.findViewById(R.id.auto_login);
        Button importBakDataBtn = (Button)this.findViewById(R.id.importBakData);
        Button exportBakDataBtn = (Button)this.findViewById(R.id.exportBakData);
        Button clearAppDataBtn = (Button)this.findViewById(R.id.clearAppData);
        Button apiSettingBtn = (Button)this.findViewById(R.id.apiSetting);
        Button del_upload_fileBtn = (Button)this.findViewById(R.id.del_upload_file);
        Button yhBtn = (Button)this.findViewById(R.id.btn_yh_setting);
        openAssitBtn.setOnClickListener(this);
        autoLoginBtn.setOnClickListener(this);
        importBakDataBtn.setOnClickListener(this);
        exportBakDataBtn.setOnClickListener(this);
        clearAppDataBtn.setOnClickListener(this);
        apiSettingBtn.setOnClickListener(this);
        del_upload_fileBtn.setOnClickListener(this);
        yhBtn.setOnClickListener(this);

        //AutoUtil.addPhoneContacts("zz","12365489658");
       // AutoUtil.addPhoneContacts("zz1","12365489658");
        //getSysLanguage();
        //DeviceParamUtil.getAwPhoneInfo();
        PhoneConf.getAddFrWx();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String spinnerValue = phoneStrs[i];
        switch (adapterView.getId()){
            case R.id.Spinner01:
                //截取spiiner的手机号保存到数据库
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_START_LOGINACCOUNT,spinnerValue.substring(spinnerValue.indexOf("-")+1,spinnerValue.indexOf(" ")));
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_START_LOGIN_INDEX,spinnerValue.substring(0,spinnerValue.indexOf("-")));
                break;
            case R.id.Spinner02:
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_END_LOGINACCOUNT,spinnerValue.substring(spinnerValue.indexOf("-")+1,spinnerValue.indexOf(" ")));
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_END_LOGIN_INDEX,spinnerValue.substring(0,spinnerValue.indexOf("-")));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    /**
     * 设置spinner样式和 下拉内容
     * @param spinner
     * @param contents 下拉内容，String[]数组
     */
    private void setSpnnierStyleAndContents(Spinner spinner,String[] contents){
        //将可选内容与ArrayAdapter连接起来
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,contents);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(this);
        //设置默认值
        spinner.setVisibility(View.VISIBLE);
    }

/*
    //使用数组形式操作
    class SpinnerSelectedListener  {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            String spinnerValue = phoneStrs[arg2];
            //截取spiiner的手机号保存到数据库
            AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_START_LOGINACCOUNT,spinnerValue.substring(spinnerValue.indexOf("-")+1,spinnerValue.indexOf(" ")));
            AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_START_LOGIN_INDEX,spinnerValue.substring(0,spinnerValue.indexOf("-")));
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }*/


    @Override
    public void onStart() {

        final String phone = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_EXT);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String HOST = "120.78.134.230:80";
                String url = "http://"+HOST+"/sendValidCode?phone="+phone;
                LogUtil.d("url",url);
                String str = OkHttpUtil.okHttpGet(url);
                LogUtil.d("resBody",str);
            }
        }).start();


        System.out.println("---> onstart");
        System.out.println("手机号码-->"+phone);
        super.onStart();
        SmsReciver receiver = new SmsReciver();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(receiver, filter);

    }

    private void save(){
        AppConfig config = new AppConfig(CommonConstant.APPCONFIG_EXT,editText.getText().toString());
        AppConfigDao.saveOrUpdate(config);
        //国别
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_CN_NUM,cnNumEditText.getText().toString());
        if(isFeedCheckBox.isChecked()){
            FileUtil.writeContentToJsonTxt("isFeedStatus.txt","1");
        }else {
            FileUtil.writeContentToJsonTxt("isFeedStatus.txt","0");
        }

    }
    @Override
    protected void onStop() {
        save();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        save();
        super.onDestroy();
    }
    public void testMethod(){
        GetPhoneInfoUtil.getPhoneInfo();
    }

    public void clearAppData(){
        AutoUtil.clearAppData();
        Toast.makeText(MainActivity.this, "清除完成",Toast.LENGTH_LONG).show();

        String phone = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGINACCOUNT);
        Wx008Data wx008Data = DaoUtil.findByPhone(phone);
        wx008Data.setPhoneInfo(wx008Data.getDatas());

        //覆盖式写入文件
        FileUtil.writeContent2FileForce("/sdcard/A_hyj_json/","phone.txt", JSON.toJSONString(wx008Data.getPhoneInfo()));
        //读取文件
        String con = FileUtil.readAll("/sdcard/A_hyj_json/phone.txt");
        System.out.println("phoneInfo---->"+con);


    }


    @Override
    public void onClick(View view) {
        System.out.println("vie --clic000--->"+view.getId());
        switch (view.getId()){
            case R.id.importBakData:
                //Toast.makeText(this, "已删除：" + DaoUtil.deleteAll() + "条", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "已导入数据：" + PhoneConf.importData() + "条", Toast.LENGTH_LONG).show();
                break;
            case R.id.exportBakData:
                List<Wx008Data> datas = DataSupport.findAll(Wx008Data.class);
                //setPhoneInfo1置为空，解决导出后，导入失败
                for(Wx008Data data:datas){
                    data.setPhoneInfo1(null);
                }
                if (datas != null && datas.size() > 0) {
                    LogUtil.export("/sdcard/A_hyj_008data/", JSON.toJSONString(datas));
                    Toast.makeText(this, "已导出数据：" + datas.size() + "条", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "没有可导出数据", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.auto_login:
                //PhoneConf.createPhoneInfo();
                /*isVpnConnected();
                getSysLanguage();
                FileUtil.readContentToJsonTxt("isFeedStatus.txt");*/
                //PhoneConf.create008Data("1230","www456","60");
                //createRegData();
                //importGoumai();
                //testMethod();
                startActivity(new Intent(MainActivity.this,AutoLoginSettingActivity.class));
                break;
            case R.id.open_assist:
                //new Thread(new GetPhoneAndValidCodeThread(new PhoneApi())).start();
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                break;
            case R.id.clearAppData:
                clearAppData();
                break;
            case R.id.isFeed:
                System.out.println("isFeedCheckBox.isChecked()-->"+isFeedCheckBox.isChecked());
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_FEED,isFeedCheckBox.isChecked()?"1":"0");
                break;
            case R.id.loginSucessPause:
                System.out.println("loginSucessPauseCheckBox.isChecked()-->"+loginSucessPauseCheckBox.isChecked());
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_LOGIN_PAUSE,loginSucessPauseCheckBox.isChecked()?"1":"0");
                break;
            case R.id.apiSetting:

                DeviceInfo deviceInfo = DeviceParamUtil.getDeviceInfo();
                System.out.println("deviceInfo-->"+JSON.toJSONString(deviceInfo));
                createData();

                /*DaoUtil.updatePwd("bfn347","www23347");
                DaoUtil.updatePwd("vit894","www23894");
                DaoUtil.updatePwd("fti468","www23468");
                DaoUtil.updatePwd("nht277","www23277");
                DaoUtil.updatePwd("whr848","www23848");
                DaoUtil.updatePwd("ynq758","www23758");
                DaoUtil.updatePwd("tfx363","www23363");
                DaoUtil.updatePwd("rzk466","www23466");
                DaoUtil.updatePwd("pii986","www23986");
                DaoUtil.updatePwd("hkx745","www23745");
                DaoUtil.updatePwd("tfk385","www23385");*/
                //new IpNetThread().start();
                startActivity(new Intent(MainActivity.this, ApiSettingActivity.class));
                break;
            case R.id.del_upload_file:
                startActivity(new Intent(MainActivity.this, AppSettingActivity.class));
                break;
            case R.id.btn_yh_setting:
                startActivity(new Intent(MainActivity.this, YhSettingActivity.class));
                break;
        }
    }


    public void createData(){
        Wx008Data data =  DaoUtil.findByPhone("1133376132");
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
    private void createRegData(){
        ContentValues values = new ContentValues();
        values.put("dataType","1");
        int cn = DataSupport.updateAll(Wx008Data.class,values);
        System.out.println("cn-->"+cn);
        /*Wx008Data currentWx008Data = new Wx008Data();
        currentWx008Data.setGuid(AutoUtil.getUUID());
        currentWx008Data.setPhone("8973807928");
        currentWx008Data.setWxPwd("wwww12345");
        currentWx008Data.setCnNum("62");
        currentWx008Data.setCreateTime(new Date());
        PhoneInfo phoneInfo = PhoneConf.createPhoneInfo();
        phoneInfo.setLineNumber("8973807928");//获取到的手机号码
        currentWx008Data.setPhoneStrs(JSON.toJSONString(phoneInfo));
        int cn = currentWx008Data.updateAll("phone=?","8973807928");
        System.out.println("cu-->"+cn);*/

    }
    public boolean isVpnConnected() {
        try {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if(niList != null) {
                for (NetworkInterface intf : Collections.list(niList)) {
                    if(!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    System.out.println("intf-->"+intf.getName());
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())){
                        return true;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }
    String xw = "{\"applicationInfo\":{\"banner\":0,\"baseCodePath\":\"/data/app/hyj.xw-1/base.apk\",\"baseResourcePath\":\"/data/app/hyj.xw-1/base.apk\",\"className\":\"hyj.xw.GlobalApplication\",\"codePath\":\"/data/app/hyj.xw-1\",\"compatibleWidthLimitDp\":0,\"dataDir\":\"/data/data/hyj.xw\",\"descriptionRes\":0,\"enabled\":true,\"enabledSetting\":0,\"flags\":13155910,\"flagsEx\":0,\"icon\":2130903040,\"installLocation\":-1,\"labelRes\":2131165218,\"largestWidthLimitDp\":0,\"logo\":0,\"nativeLibraryDir\":\"/data/app/hyj.xw-1/lib/arm64\",\"nativeLibraryRootDir\":\"/data/app/hyj.xw-1/lib\",\"nativeLibraryRootRequiresIsa\":true,\"packageName\":\"hyj.xw\",\"processName\":\"hyj.xw\",\"publicSourceDir\":\"/data/app/hyj.xw-1/base.apk\",\"requiresSmallestWidthDp\":0,\"resourcePath\":\"/data/app/hyj.xw-1\",\"scanPublicSourceDir\":\"/data/app/hyj.xw-1\",\"scanSourceDir\":\"/data/app/hyj.xw-1\",\"seinfo\":\"default\",\"showUserIcon\":-10000,\"sourceDir\":\"/data/app/hyj.xw-1/base.apk\",\"targetSdkVersion\":25,\"taskAffinity\":\"hyj.xw\",\"theme\":2131296420,\"uiOptions\":0,\"uid\":10134,\"versionCode\":1},\"coreApp\":false,\"firstInstallTime\":1521727262902,\"installLocation\":-1,\"lastUpdateTime\":1521729450721,\"packageName\":\"hyj.xw\",\"requiredForAllUsers\":false,\"sharedUserLabel\":0,\"versionCode\":1,\"versionName\":\"1.0\"}";
    private void getSysLanguage(){
        System.out.println("versionCode-->22");
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
            System.out.println("versionCode-->"+JSON.toJSONString(pi));
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("versionCode-->"+versionCode);
        PackageInfo newPi =JSON.parseObject(xw,PackageInfo.class);
        System.out.println("new versionCode-->"+newPi.versionName);
        System.out.println("new versionCode-->"+JSON.toJSONString(newPi));


    }
}
