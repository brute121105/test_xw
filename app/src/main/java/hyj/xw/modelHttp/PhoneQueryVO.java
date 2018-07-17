package hyj.xw.modelHttp;


import android.graphics.Point;

public class PhoneQueryVO {
    private String user;
    private String domain;
    private String areaCode;
    private String device;

    public PhoneQueryVO(String user,String device){
        this.user = user;
        this.device = device;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
