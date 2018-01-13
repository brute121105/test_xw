package hyj.xw.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import hyj.xw.R;
import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.model.LitePalModel.AppConfig;

public class AutoLoginSettingActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_login_setting2);

        editText = (EditText)findViewById(R.id.appconfig_accounts);
        editText.setText(AppConfigDao.findAcountsStrByCode(CommonConstant.APPCONFIG_LOGIN_ACCOUNT));

        Button backBtn = (Button)findViewById(R.id.autoLoginSet_back);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                save();
                finish();
            }
        });


    }
    private void save(){
        AppConfig config = new AppConfig(CommonConstant.APPCONFIG_LOGIN_ACCOUNT,editText.getText().toString().replaceAll("-,|-，","-huang121105,"));
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
}
