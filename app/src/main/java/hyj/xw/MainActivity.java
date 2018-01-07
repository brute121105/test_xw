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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.io.DataOutputStream;
import java.io.File;
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
import hyj.xw.model.PhoneInfo;
import hyj.xw.service.SmsReciver;
import hyj.xw.test.GetPhoneInfoUtil;
import hyj.xw.util.FileUtil;
import hyj.xw.util.GetPermissionUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.OkHttpUtil;

public class MainActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetPermissionUtil.getReadAndWriteContactPermision(this,MainActivity.this);

        Button openAssitBtn = (Button)this.findViewById(R.id.open_assist);
        openAssitBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                testMethod();
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                //Toast.makeText(MainActivity.this, "打开启权限，才能运行", Toast.LENGTH_LONG).show();
            }
        });

        Button autoLoginBtn = (Button)this.findViewById(R.id.auto_login);
        autoLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                GetPhoneInfoUtil.getPhoneInfo();
                startActivity(new Intent(MainActivity.this,AutoLoginSettingActivity.class));
            }
        });
        //综合参数
        editText = (EditText)findViewById(R.id.ext);
        String c = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_EXT);
        editText.setText(TextUtils.isEmpty(c)?"0":c);

        CommonConstant.index = Integer.parseInt(editText.getText().toString());
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

        int index  = Integer.parseInt(editText.getText().toString());
        System.out.println("index--->"+index);
        PhoneInfo phoneInfo = PhoneConf.createPhoneInfo(index);
        FileUtil.writeContent2FileForce("/sdcard/A_hyj_json/","phone.txt", JSON.toJSONString(phoneInfo));
        String con = FileUtil.readAll("/sdcard/A_hyj_json/phone.txt");
        System.out.println("写入文件后读取phoneInfo---->"+con);

        exeShell("pm clear com.tencent.mm" );
        killPro(this, "com.tencent.mm");
        Toast.makeText(MainActivity.this, "清除完成",Toast.LENGTH_LONG).show();
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





}
