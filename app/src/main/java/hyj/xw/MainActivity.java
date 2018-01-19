package hyj.xw;

import android.app.ActivityManager;
import android.content.Context;
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

import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import hyj.xw.activity.AutoLoginSettingActivity;
import hyj.xw.common.CommonConstant;
import hyj.xw.conf.PhoneConf;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.model.LitePalModel.AppConfig;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.service.SmsReciver;
import hyj.xw.test.GetPhoneInfoUtil;
import hyj.xw.util.DaoUtil;
import hyj.xw.util.FileUtil;
import hyj.xw.util.GetPermissionUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.OkHttpUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editText;
    CheckBox isFeedCheckBox;

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
        isFeedCheckBox.setOnClickListener(this);
        isFeedCheckBox.setChecked("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_FEED))?true:false);
        //综合参数
        editText = (EditText)findViewById(R.id.ext);
        String c = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_EXT);
        editText.setText(TextUtils.isEmpty(c)?"0":c);

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
         /*
        下拉框数据结束
        */

        Button openAssitBtn = (Button)this.findViewById(R.id.open_assist);
        Button autoLoginBtn = (Button)this.findViewById(R.id.auto_login);
        Button importBakDataBtn = (Button)this.findViewById(R.id.importBakData);
        Button clearAppDataBtn = (Button)this.findViewById(R.id.clearAppData);
        openAssitBtn.setOnClickListener(this);
        autoLoginBtn.setOnClickListener(this);
        importBakDataBtn.setOnClickListener(this);
        clearAppDataBtn.setOnClickListener(this);

    }


    //使用数组形式操作
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            String spinnerValue = phoneStrs[arg2];
            //截取spiiner的手机号保存到数据库
            String phone = spinnerValue.substring(spinnerValue.indexOf("-")+1,spinnerValue.indexOf(" "));
            AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_START_LOGINACCOUNT,phone);
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

        exeShell("am force-stop com.tencent.mm" );
        killPro(this, "com.tencent.mm");
        exeShell("pm clear com.tencent.mm" );

        exeShell("rm -r -f /data/data/com.tencent.mm/MicroMsg" );
        exeShell("rm -r -f /data/data/com.tencent.mm/app_cache" );
        exeShell("rm -r -f /data/data/com.tencent.mm/app_dex" );
        exeShell("rm -r -f /data/data/com.tencent.mm/app_font" );
        exeShell("rm -r -f /data/data/com.tencent.mm/app_lib" );
        exeShell("rm -r -f /data/data/com.tencent.mm/app_recover_lib" );
        exeShell("rm -r -f /data/data/com.tencent.mm/app_tbs" );
        exeShell("rm -r -f /data/data/com.tencent.mm/cache" );
        exeShell("rm -r -f /data/data/com.tencent.mm/databases" );
        exeShell("rm -r -f /data/data/com.tencent.mm/face_detect" );
        exeShell("rm -r -f /data/data/com.tencent.mm/files" );
        exeShell("rm -r -f /data/data/com.tencent.mm/shared_prefs" );
        exeShell("rm -r -f /sdcard/tencent" );
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

    public static boolean killPro(Context paramContext, String paramString)
    {
        List localList = ((ActivityManager)paramContext.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
        ArrayList localArrayList = new ArrayList();
        new HashMap();
        Iterator localIterator = localList.iterator();
        while (true)
        {
            if (!localIterator.hasNext())
                return false;
            ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo)localIterator.next();
            if (localRunningAppProcessInfo.uid > 1000)
            {
                localArrayList.add(localRunningAppProcessInfo);
                String[] arrayOfString = localRunningAppProcessInfo.pkgList;
                int i = arrayOfString.length;
                for (int j = 0; j < i; j++)
                    if (arrayOfString[j].equals(paramString))
                    {
                        exeShell("kill " + localRunningAppProcessInfo.pid);
                        ((ActivityManager)paramContext.getSystemService(Context.ACTIVITY_SERVICE)).killBackgroundProcesses(paramString);
                        return true;
                    }
            }
        }
    }


    public static boolean a = false;
    private static DataOutputStream b;
    private static Process c;
    private static InputStream d;

    public static String exeShell(String paramString)
    {
        try
        {
            if (b == null)
            {
                c = Runtime.getRuntime().exec("su");
                b = new DataOutputStream(c.getOutputStream());
                d = c.getInputStream();
            }
            b.writeBytes(paramString + "\n");
            b.flush();
            return "";
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return "";
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
                testMethod();
                startActivity(new Intent(MainActivity.this,AutoLoginSettingActivity.class));
                break;
            case R.id.open_assist:
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                break;
            case R.id.clearAppData:
                clearAppData();
                break;
            case R.id.isFeed:
                System.out.println("isFeedCheckBox.isChecked()-->"+isFeedCheckBox.isChecked());
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_FEED,isFeedCheckBox.isChecked()?"1":"0");
                break;
        }
    }


}
