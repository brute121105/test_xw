package hyj.xw;

import android.content.Intent;
import android.content.IntentFilter;
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

import java.util.Date;
import java.util.List;

import hyj.xw.activity.ApiSettingActivity;
import hyj.xw.activity.AutoLoginSettingActivity;
import hyj.xw.common.CommonConstant;
import hyj.xw.conf.PhoneConf;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.model.LitePalModel.AppConfig;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.service.SmsReciver;
import hyj.xw.test.GetPhoneInfoUtil;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.DaoUtil;
import hyj.xw.util.FileUtil;
import hyj.xw.util.GetPermissionUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.OkHttpUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editText;
    EditText cnNumEditText;
    CheckBox isFeedCheckBox;
    CheckBox loginSucessPauseCheckBox;

    private String[] phoneStrs;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GetPermissionUtil.getReadAndWriteContactPermision(this,MainActivity.this);


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
        spinner = (Spinner) findViewById(R.id.Spinner01);
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, phoneStrs);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);
        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
        //设置默认值
        spinner.setVisibility(View.VISIBLE);
        String loginIndex = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX);
        loginIndex = TextUtils.isEmpty(loginIndex)||Integer.parseInt(loginIndex)>phoneStrs.length-1?"0":loginIndex;
        spinner.setSelection(Integer.parseInt(loginIndex),true);//spinner下拉框默认值

         /*
        下拉框数据结束
        */

        Button openAssitBtn = (Button)this.findViewById(R.id.open_assist);
        Button autoLoginBtn = (Button)this.findViewById(R.id.auto_login);
        Button importBakDataBtn = (Button)this.findViewById(R.id.importBakData);
        Button clearAppDataBtn = (Button)this.findViewById(R.id.clearAppData);
        Button apiSettingBtn = (Button)this.findViewById(R.id.apiSetting);
        openAssitBtn.setOnClickListener(this);
        autoLoginBtn.setOnClickListener(this);
        importBakDataBtn.setOnClickListener(this);
        clearAppDataBtn.setOnClickListener(this);
        apiSettingBtn.setOnClickListener(this);

    }


    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            String spinnerValue = phoneStrs[arg2];
            //截取spiiner的手机号保存到数据库
            AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_START_LOGINACCOUNT,spinnerValue.substring(spinnerValue.indexOf("-")+1,spinnerValue.indexOf(" ")));
            AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_START_LOGIN_INDEX,spinnerValue.substring(0,spinnerValue.indexOf("-")));
        }
        public void onNothingSelected(AdapterView<?> arg0) {
        }
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

    public void testMethod1(){
           System.out.println("--file dir-->");
           System.out.println("--file dir-->"+this.getFilesDir().getPath());
        //FileUtil.copyFolder(srcFile,destFile);
        //localConnectivityManager.setAirplaneMode(false);
    }


    @Override
    public void onClick(View view) {
        System.out.println("vie --clic000--->"+view.getId());
        switch (view.getId()){
            case R.id.importBakData:
                //Toast.makeText(this, "已删除：" + DaoUtil.deleteAll() + "条", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "已导入数据：" + PhoneConf.importData() + "条", Toast.LENGTH_LONG).show();
                break;
            case R.id.auto_login:
                Wx008Data datas0 =  DaoUtil.findOne008NullDatas();
                System.out.println("findOne008NullDatas---->"+JSON.toJSONString(datas0));
                testMethod();
                startActivity(new Intent(MainActivity.this,AutoLoginSettingActivity.class));
                break;
            case R.id.open_assist:
              /*  int i = DaoUtil.setLoginWxidDataTo008NullData("0084945791703","hn027434","84");
                System.out.println("setLoginDataTo008NullData--->"+i);11-13  24-30
                DaoUtil.setLoginWxidDataTo008NullData("0084945755941","yp264015","84");
                DaoUtil.setLoginWxidDataTo008NullData("0084945783615","go615450","84");
                DaoUtil.setLoginWxidDataTo008NullData("0084945790923","yf055696","84");
                DaoUtil.setLoginWxidDataTo008NullData("00841265152124","hd671732","84");
                DaoUtil.setLoginWxidDataTo008NullData("0084945763440","sw240116","84");
                DaoUtil.setLoginWxidDataTo008NullData("00841289978297","pu949852","84");*/

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
                startActivity(new Intent(MainActivity.this, ApiSettingActivity.class));
                break;
        }
    }


}
