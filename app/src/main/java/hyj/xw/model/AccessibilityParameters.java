package hyj.xw.model;

/**
 * Created by asus on 2018/1/15.
 */

public class AccessibilityParameters {
    private int isStop;//暂停标志

    private PhoneApi pa = new PhoneApi();

    public PhoneApi getPa() {
        return pa;
    }

    public void setPa(PhoneApi pa) {
        this.pa = pa;
    }

    public int getIsStop() {
        return isStop;
    }

    public void setIsStop(int isStop) {
        this.isStop = isStop;
    }
}
