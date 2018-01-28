package hyj.xw.model;

/**
 * Created by Administrator on 2017/8/15.
 */

public class PhoneApi {
    private String apiId;
    private String pwd;
    private String pjId;
    private String token;
    private String phone;
    private String regSuccessphone;
    private String validCode;
    private String msg;
    private String status;
    private String cnNum;
    private String phoneId;
    private String releasPhone;


    private int waitValicodeTime;

    private String zcPwd;
    private boolean phoneIsAvailavle=false;
    private boolean validCodeIsAvailavle=false;
    private boolean isSendMsg=false;

    public PhoneApi(){

    }

    public PhoneApi(String apiId, String pwd, String pjId, String zcPwd, String cnNum) {
        this.apiId = apiId;
        this.pwd = pwd;
        this.zcPwd = zcPwd;
        this.pjId = pjId;
        this.cnNum = cnNum;

    }



    public String getReleasPhone() {
        return releasPhone;
    }

    public void setReleasPhone(String releasPhone) {
        this.releasPhone = releasPhone;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getCnNum() {
        return cnNum;
    }

    public void setCnNum(String cnNum) {
        this.cnNum = cnNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSendMsg() {
        return isSendMsg;
    }

    public void setSendMsg(boolean sendMsg) {
        isSendMsg = sendMsg;
    }

    public String getRegSuccessphone() {
        return regSuccessphone;
    }

    public void setRegSuccessphone(String regSuccessphone) {
        this.regSuccessphone = regSuccessphone;
    }

    public int getWaitValicodeTime() {
        return waitValicodeTime;
    }

    public void setWaitValicodeTime(int waitValicodeTime) {
        this.waitValicodeTime = waitValicodeTime;
    }

    public String getZcPwd() {
        return zcPwd;
    }

    public void setZcPwd(String zcPwd) {
        this.zcPwd = zcPwd;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getPwd() {
           return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPjId() {
        return pjId;
    }

    public void setPjId(String pjId) {
        this.pjId = pjId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getValidCode() {
        return validCode;
    }

    public void setValidCode(String validCode) {
        this.validCode = validCode;
    }

    public boolean isPhoneIsAvailavle() {
        return phoneIsAvailavle;
    }

    public void setPhoneIsAvailavle(boolean phoneIsAvailavle) {
        this.phoneIsAvailavle = phoneIsAvailavle;
    }

    public boolean isValidCodeIsAvailavle() {
        return validCodeIsAvailavle;
    }

    public void setValidCodeIsAvailavle(boolean validCodeIsAvailavle) {
        this.validCodeIsAvailavle = validCodeIsAvailavle;
    }
}
