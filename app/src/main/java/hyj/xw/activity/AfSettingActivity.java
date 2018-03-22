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

public class AfSettingActivity extends AppCompatActivity implements View.OnClickListener{

    EditText afsEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_af_setting);

        //添加的微信号
        afsEt = (EditText)findViewById(R.id.et_afs);
        String adfDefaultValue = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_AFS);
        if(!TextUtils.isEmpty(adfDefaultValue)){
            afsEt.setText(adfDefaultValue);
        }
        //返回
        Button afBackBtn = (Button)this.findViewById(R.id.btn_af_back);
        afBackBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_af_back:
                save();
                finish();
                break;
        }

    }
    private void save(){
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_AFS,afsEt.getText().toString());
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
