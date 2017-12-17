package hyj.xw;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import hyj.xw.activity.AutoLoginSettingActivity;
import hyj.xw.common.CommonConstant;
import hyj.xw.conf.CreateNode2DB;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.dao.WxNodeDao;
import hyj.xw.model.LitePalModel.AppConfig;
import hyj.xw.model.LitePalModel.WxNode;
import hyj.xw.util.DeviceParamUtil;
import hyj.xw.util.GetPermissionUtil;
import hyj.xw.util.LogUtil;

public class MainActivity extends AppCompatActivity {

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
