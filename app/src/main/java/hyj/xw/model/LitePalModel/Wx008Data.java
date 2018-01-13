package hyj.xw.model.LitePalModel;


import com.alibaba.fastjson.JSONObject;

import java.util.Date;

import hyj.xw.model.PhoneInfo;

/**
 * Created by Administrator on 2017/8/22.
 */

public class Wx008Data  {
    private PhoneInfo phoneInfo = new PhoneInfo();
    private String datas;
    private String phone;
    private String wxId;
    private String wxPwd;//属性名称为pwd无法保存？
    private String expMsg;
    private int dieFlag;//0正常 1账号异常 2 操作频率过快 3 登录环境异常 长期未登录  批量注册 4手机不在身边
    private String cnNum;//国别区号
    private String loginState;//登陆状态 0 登陆不成功 1 登陆成功
    private String friends;//好友
    private Date createTime;
    private Date lastLoginTime;

    public Wx008Data(){
    }



    public void setPhoneInfo(String datas){
        String[] strs = JSONObject.parseObject(datas,String[].class);
        int i=0;
        if(!strs[1].contains("历史记录")){//红米2s提取的008数据
            i = -1;
        }
        phoneInfo.setDeviceId(strs[3+i]);
        phoneInfo.setAndroidId(strs[5+i]);
        phoneInfo.setLineNumber(strs[7+i]);
        phoneInfo.setSimSerialNumber(strs[9+i]);
        phoneInfo.setSubscriberId(strs[11+i]);
        phoneInfo.setSimCountryIso(strs[13+i]);

        phoneInfo.setSimOperator(strs[15+i]);
        phoneInfo.setSimOperatorName(strs[17+i]);
        phoneInfo.setNetworkCountryIso(strs[19+i]);
        phoneInfo.setNetworkOperator(strs[21+i]);
        phoneInfo.setNetworkOperatorName(strs[23+i]);

        phoneInfo.setNetworkType(Integer.parseInt(strs[25+i]));
        phoneInfo.setPhoneType(Integer.parseInt(strs[27+i]));
        phoneInfo.setSimState(Integer.parseInt(strs[29+i]));

        phoneInfo.setRelease(strs[37+i]);
        phoneInfo.setSdk(strs[39+i]);
        phoneInfo.setBrand(strs[47+i]);
        phoneInfo.setModel(strs[49+i]);
        phoneInfo.setBuildId(strs[51+i]);
        phoneInfo.setDisplay(strs[53+i]);

        phoneInfo.setProductName(strs[55+i]);
        phoneInfo.setManufacturer(strs[57+i]);
        phoneInfo.setDevice(strs[59+i]);
        phoneInfo.setHardware(strs[63+i]);
        phoneInfo.setFingerprint(strs[65+i]);
        phoneInfo.setSerialno(strs[67+i]);

    }

    public PhoneInfo getPhoneInfo() {
        return phoneInfo;
    }


    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public String getLoginState() {
        return loginState;
    }

    public void setLoginState(String loginState) {
        this.loginState = loginState;
    }

    public String getCnNum() {
        return cnNum;
    }

    public void setCnNum(String cnNum) {
        this.cnNum = cnNum;
    }

    public String getWxPwd() {
        return wxPwd;
    }

    public void setWxPwd(String wxPwd) {
        this.wxPwd = wxPwd;
    }

    public String getExpMsg() {
        return expMsg;
    }

    public void setExpMsg(String expMsg) {
        this.expMsg = expMsg;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String datas) {
        this.datas = datas;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWxId() {
        return wxId;
    }

    public void setWxId(String wxId) {
        this.wxId = wxId;
    }


    public int getDieFlag() {
        return dieFlag;
    }

    public void setDieFlag(int dieFlag) {
        this.dieFlag = dieFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
