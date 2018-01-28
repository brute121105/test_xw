package hyj.xw.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import hyj.xw.R;
import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;

public class ApiSettingActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText apiIdEdiText;
    private EditText apiPwdEdiText;
    private EditText apiPjIdEdiText;
    private EditText apiTypeEdiText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_setting);


        //api平台标识
        apiTypeEdiText = (EditText)findViewById(R.id.api_type);
        String apiType = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_TYPE);
        apiTypeEdiText.setText(TextUtils.isEmpty(apiType)?"1":apiType);
        //api账号
        apiIdEdiText = (EditText)findViewById(R.id.api_id);
        String apiId = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_ID);
        apiIdEdiText.setText(TextUtils.isEmpty(apiId)?"brute121105":apiId);
        //api密码
        apiPwdEdiText = (EditText)findViewById(R.id.api_pwd);
        String apiPwd = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_PWD);
        apiPwdEdiText.setText(TextUtils.isEmpty(apiPwd)?"huang121105":apiPwd);
        //api项目id
        apiPjIdEdiText = (EditText)findViewById(R.id.api_project_id);
        String apiPjId = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_PROJECT_ID);
        apiPjIdEdiText.setText(TextUtils.isEmpty(apiPjId)?"6":apiPjId);

        //返回按钮
        Button testBtn = (Button)findViewById(R.id.api_test);
        testBtn.setOnClickListener(this);
        //测试按钮
        Button backBtn = (Button)findViewById(R.id.api_back);
        backBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.api_back:
                finish();
                saveParams();
                break;
            case R.id.api_test:
                break;
        }
    }

    private void saveParams(){
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_API_TYPE,apiTypeEdiText.getText().toString());
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_API_ID,apiIdEdiText.getText().toString());
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_API_PWD,apiPwdEdiText.getText().toString());
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_API_PROJECT_ID,apiPjIdEdiText.getText().toString());

    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("save api--->onPause");
        saveParams();
    }
}
