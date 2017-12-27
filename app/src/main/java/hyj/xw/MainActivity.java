package hyj.xw;

import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import hyj.xw.activity.AutoLoginSettingActivity;
import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.model.LitePalModel.AppConfig;
import hyj.xw.service.SmsReciver;
import hyj.xw.util.DeviceParamUtil;
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
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                Toast.makeText(MainActivity.this, "打开启权限，才能运行", Toast.LENGTH_LONG).show();
                testMethod();
            }
        });

        Button autoLoginBtn = (Button)this.findViewById(R.id.auto_login);
        autoLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AutoLoginSettingActivity.class));
            }
        });
        //综合参数
        editText = (EditText)findViewById(R.id.ext);
        editText.setText(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_EXT));


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
        //CreateNode2DB.create();
        LogUtil.d("tt","tt");
        //WxNodeDao.findAllNode();
        //AppConfig config = new AppConfig(CommonConstant.APPCONFIG_LOGIN_ACCOUNT,"nnk4869-,szinfo0002-huang121105".replaceAll("-,|-，","-huang121105,"));
        //AppConfigDao.saveOrUpdate(config);
        //AppConfigDao.findAcountsListByCode(CommonConstant.APPCONFIG_LOGIN_ACCOUNT);
    }

}
