package hyj.xw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import hyj.xw.R;
import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;

public class YhSettingActivity extends AppCompatActivity implements View.OnClickListener{

    CheckBox isSendFrCheckBox;
    CheckBox isAfByWxidCheckBox;
    CheckBox isRcFriendCheckBox;
    CheckBox isChanePwdCheckBox;
    CheckBox isSetWxisCheckBox;
    CheckBox isSmjqCheckBox;
    CheckBox isReplacePhoneCheckBox;

    EditText newPwdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yh_setting);

        //添加好友
        isAfByWxidCheckBox = (CheckBox)this.findViewById(R.id.isAfByWxid);
        isAfByWxidCheckBox.setChecked("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_AF_BY_WXID))?true:false);
        isAfByWxidCheckBox.setOnClickListener(this);
        //发圈
        isSendFrCheckBox = (CheckBox)this.findViewById(R.id.isSendFr);
        isSendFrCheckBox.setChecked("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_SEND_FR))?true:false);
        isSendFrCheckBox.setOnClickListener(this);
        //自动通过好友
        isRcFriendCheckBox = (CheckBox)this.findViewById(R.id.isRcFriend);
        isRcFriendCheckBox.setChecked("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_RC_FRIEND))?true:false);
        isRcFriendCheckBox.setOnClickListener(this);
        //修改密码
        isChanePwdCheckBox = (CheckBox)this.findViewById(R.id.isChanePwd);
        isChanePwdCheckBox.setChecked("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_CHANGE_PWD))?true:false);
        isChanePwdCheckBox.setOnClickListener(this);
        //设置微信号
        isSetWxisCheckBox = (CheckBox)this.findViewById(R.id.isSetWxis);
        isSetWxisCheckBox.setChecked("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_SET_WXID))?true:false);
        isSetWxisCheckBox.setOnClickListener(this);
        //扫码加群
        isSmjqCheckBox = (CheckBox)this.findViewById(R.id.isSmjq);
        isSmjqCheckBox.setChecked("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_SMJQ))?true:false);
        isSmjqCheckBox.setOnClickListener(this);
        //解绑手机
        isReplacePhoneCheckBox = (CheckBox)this.findViewById(R.id.isReplacePhone);
        isReplacePhoneCheckBox.setChecked("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_REP_PHONE))?true:false);
        isReplacePhoneCheckBox.setOnClickListener(this);

        newPwdEditText = (EditText)findViewById(R.id.et_new_pwd);
        String pwd = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_NEW_PWD);
        if(!TextUtils.isEmpty(pwd))  newPwdEditText.setText(pwd);

        Button afBtn = (Button)this.findViewById(R.id.btn_af_setting);
        afBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.isAfByWxid:
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_AF_BY_WXID,isAfByWxidCheckBox.isChecked()?"1":"0");
                break;
            case R.id.isSendFr:
                System.out.println("isSendFrCheckBox.isChecked()-->"+isSendFrCheckBox.isChecked());
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_SEND_FR,isSendFrCheckBox.isChecked()?"1":"0");
                break;
            case R.id.isRcFriend:
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_RC_FRIEND,isRcFriendCheckBox.isChecked()?"1":"0");
                break;
            case R.id.isChanePwd:
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_CHANGE_PWD,isChanePwdCheckBox.isChecked()?"1":"0");
                break;
            case R.id.isSetWxis:
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_SET_WXID,isSetWxisCheckBox.isChecked()?"1":"0");
                break;
            case R.id.isSmjq:
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_SMJQ,isSmjqCheckBox.isChecked()?"1":"0");
                break;
            case R.id.isReplacePhone:
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_IS_REP_PHONE,isReplacePhoneCheckBox.isChecked()?"1":"0");
                break;
            case R.id.btn_af_setting:
                startActivity(new Intent(YhSettingActivity.this, AfSettingActivity.class));
                break;
        }

    }
    private void save(){
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_NEW_PWD,newPwdEditText.getText().toString());
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
