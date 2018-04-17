package hyj.xw;

import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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

import hyj.xw.activity.ApiSettingActivity;
import hyj.xw.activity.AppSettingActivity;
import hyj.xw.activity.AutoLoginSettingActivity;
import hyj.xw.activity.DataImpExpActivity;
import hyj.xw.activity.YhSettingActivity;
import hyj.xw.aw.util.BuildFileUtil;
import hyj.xw.common.CommonConstant;
import hyj.xw.common.FilePathCommon;
import hyj.xw.conf.PhoneConf;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.flowWindow.MyWindowManager;
import hyj.xw.hook.newHook.NewPhoneInfo;
import hyj.xw.model.DeviceInfo;
import hyj.xw.model.LitePalModel.AppConfig;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.service.SmsReciver;
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
    CheckBox isAirChangeIpCheckBox;
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
        FileUtil.createFilePath(FilePathCommon.baseAppPath);
        FileUtil.createFilePath(FilePathCommon.importDataAPath);
        FileUtil.createFilePath(FilePathCommon.dataBakPath);
        if (Build.VERSION.SDK_INT < 23) {
            MyWindowManager.createSmallWindow(getApplicationContext());
            MyWindowManager.createSmallWindow2(getApplicationContext());
        }

        //飞行模式换ip
        isAirChangeIpCheckBox = (CheckBox)this.findViewById(R.id.isAirChangeIp);
        String isAirChangeIp = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_AIR_CHANGE_IP);
        isAirChangeIpCheckBox.setChecked("1".equals(isAirChangeIp)||TextUtils.isEmpty(isAirChangeIp)?true:false);
        isAirChangeIpCheckBox.setOnClickListener(this);
        //养号
        isFeedCheckBox = (CheckBox)this.findViewById(R.id.isFeed);
        String ifFeed = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_FEED);
        isFeedCheckBox.setChecked("1".equals(ifFeed)||TextUtils.isEmpty(ifFeed)?true:false);
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
        //spinner
        Spinner spinner = (Spinner) findViewById(R.id.Spinner01);
        setSpnnierStyleAndContents(spinner,phoneStrs);
        //设置默认值
        String loginIndex = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX);
        loginIndex = TextUtils.isEmpty(loginIndex)||Integer.parseInt(loginIndex)>phoneStrs.length-1?"0":loginIndex;
        spinner.setSelection(Integer.parseInt(loginIndex),true);//spinner下拉框默认值*/

        //spinner02
        Spinner spinner02 = (Spinner) findViewById(R.id.Spinner02);
        setSpnnierStyleAndContents(spinner02,phoneStrs);
        //设置默认值
        String endLoginIndex = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_END_LOGIN_INDEX);
        endLoginIndex = TextUtils.isEmpty(endLoginIndex)||Integer.parseInt(endLoginIndex)>phoneStrs.length-1?String.valueOf(phoneStrs.length-1):endLoginIndex;
        spinner02.setSelection(Integer.parseInt(endLoginIndex),true);//spinner下拉框默认值*/
         /*
        下拉框数据结束
        */

        Button openAssitBtn = (Button)this.findViewById(R.id.open_assist);
        Button autoLoginBtn = (Button)this.findViewById(R.id.auto_login);
        Button clearAppDataBtn = (Button)this.findViewById(R.id.clearAppData);
        Button apiSettingBtn = (Button)this.findViewById(R.id.apiSetting);
        Button del_upload_fileBtn = (Button)this.findViewById(R.id.del_upload_file);
        Button yhBtn = (Button)this.findViewById(R.id.btn_yh_setting);
        Button btn_data_impExp = (Button)this.findViewById(R.id.btn_data_impExp);
        openAssitBtn.setOnClickListener(this);
        autoLoginBtn.setOnClickListener(this);
        clearAppDataBtn.setOnClickListener(this);
        apiSettingBtn.setOnClickListener(this);
        del_upload_fileBtn.setOnClickListener(this);
        yhBtn.setOnClickListener(this);
        btn_data_impExp.setOnClickListener(this);
        Button killAppBtn = (Button)this.findViewById(R.id.btn_kill_app);
        killAppBtn.setOnClickListener(this);

        //AutoUtil.addPhoneContacts("zz","12365489658");
       // AutoUtil.addPhoneContacts("zz1","12365489658");
        //getSysLanguage();
        //DeviceParamUtil.getAwPhoneInfo();
        PhoneConf.getAddFrWx();
        //System.out.println("mac-->"+AutoUtil.getLocalMacAddress());
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
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_AIR_CHANGE_IP,isAirChangeIpCheckBox.isChecked()?"1":"0");

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
    public void testMethod()  {
        /*List<NewPhoneInfo> npis =  BuildFileUtil.getBuildPhoneInfo();
        for(NewPhoneInfo npi:npis){
            System.out.println("npi-->"+JSON.toJSONString(npi));
        }*/
        NewPhoneInfo npi = BuildFileUtil.createOneDevice("112233");
        System.out.println("npi-->"+JSON.toJSONString(npi));

        //ActivityManager localActivityManager = (ActivityManager)GlobalApplication.getContext1().getSystemService(Context.ACTIVITY_SERVICE);
        //localActivityManager.forceStopPackage("hyj.xw");

        /*File localFile = new File(FilePathCommon.baseAppPath, "wx.db");
        ParcelFileDescriptor localParcelFileDescriptor = null;
        try {
            localParcelFileDescriptor = ParcelFileDescriptor.open(localFile,ParcelFileDescriptor.MODE_WRITE_ONLY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList localArrayList = new ArrayList();
        localArrayList.add("com.tencent.mm");
        IBackupManager localIBackupManager = IBackupManager.Stub.asInterface(ServiceManager.getService("backup"));
        try {
            localIBackupManager.fullBackup(localParcelFileDescriptor, true, false, false, false, false, false, true, (String[])localArrayList.toArray(new String[localArrayList.size()]));
        } catch (RemoteException e) {
            e.printStackTrace();
        }*/


        /*List<Wx008Data> wx008Datas = DaoUtil.getWx008Datas();
        Wx008Data  data = wx008Datas.get(105);
        LogUtil.d("testMethod data",JSON.toJSONString(data));
        NewPhoneInfo pi = PhoneConf.xw2awData(data);
        LogUtil.d("testMethod NewPhoneInfo",JSON.toJSONString(pi));
        CreatePhoneEnviroment.create(GlobalApplication.getContext(),pi);
        FileUtil.writeContent2FileForceUtf8("/sdcard/A_hyj_json/a1/","PhoneInfo.aw", JSON.toJSONString(pi));
        //Log.i("testMethod-->",JSON.toJSONString(pi));
        String con = FileUtil.readAllUtf8("/sdcard/A_hyj_json/a1/PhoneInfo.aw");
        LogUtil.d("testMethod con",con);*/
        //LogUtil.d("testMethod json",JSON.toJSONString(pi));
        //GetpropRp.doRp(GlobalApplication.getContext(),pi);
       // String con1 = FileUtil.readAll1(PathFileUtil.str10+ File.separator+"getprop");
        //LogUtil.d("testMethod con1",con1);
        //GetPhoneInfoUtil.getPhoneInfo();

    }

    public void clearAppData(){
      /*  String con = FileUtil.readAllUtf8("/sdcard/A_hyj_json/a1/PhoneInfo.aw");
        LogUtil.d("testMethod con",con);
        NewPhoneInfo pi = JSON.parseObject(con,NewPhoneInfo.class);
        CreatePhoneEnviroment.create(GlobalApplication.getContext(),pi);*/

        AutoUtil.clearAppData();
        Toast.makeText(MainActivity.this, "清除完成",Toast.LENGTH_LONG).show();

       /* String phone = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGINACCOUNT);
        Wx008Data wx008Data = DaoUtil.findByPhone(phone);
        wx008Data.setPhoneInfo(wx008Data.getDatas());

        //覆盖式写入文件
        FileUtil.writeContent2FileForce("/sdcard/A_hyj_json/","phone.txt", JSON.toJSONString(wx008Data.getPhoneInfo()));*/
        //读取文件
      /*  String con = FileUtil.readAll("/sdcard/A_hyj_json/phone.txt");
        System.out.println("phoneInfo---->"+con);*/


    }


    @Override
    public void onClick(View view) {
        System.out.println("vie --clic000--->"+view.getId());
        switch (view.getId()){
            case R.id.auto_login:
                startActivity(new Intent(MainActivity.this,AutoLoginSettingActivity.class));
                break;
            case R.id.open_assist:
                testMethod();
                /*DialogUtil dialogUtil = new DialogUtil();
                dialogUtil.show("确认修改吗?", new DialogButtonListener() {
                    @Override
                    public void sure() {
                        System.out.println("DialogUtil00");
                        //ToastUtil.show("点击了确认");
                    }

                    @Override
                    public void cancel() {
                        System.out.println("DialogUtil11");
                        // ToastUtil.show("点击了取消");
                    }
                });*/

                int start = Integer.parseInt(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX));
                int end = Integer.parseInt(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_END_LOGIN_INDEX));
                if(start>end){
                    Toast.makeText(this, "已设置的开始序号【"+start+"】不能大于结束序号【"+end+"】", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(this, "已设置登录序号【"+start+"-"+end+"】", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                }
                break;
            case R.id.clearAppData:
                clearAppData();
                break;
            case R.id.isFeed:
                System.out.println("isFeedCheckBox.isChecked()-->"+isFeedCheckBox.isChecked());
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_FEED,isFeedCheckBox.isChecked()?"1":"0");
                break;
            case R.id.isAirChangeIp:
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_AIR_CHANGE_IP,isAirChangeIpCheckBox.isChecked()?"1":"0");
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
            case R.id.btn_data_impExp:
                startActivity(new Intent(MainActivity.this, DataImpExpActivity.class));
                break;
            case R.id.btn_kill_app:
                AutoUtil.killApp();
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
        NewPhoneInfo phoneInfo = PhoneConf.createPhoneInfo();
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
