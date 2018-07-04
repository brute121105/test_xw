package hyj.xw.modelHttp;

/**
 * Created by Administrator on 2018/7/3 0003.
 */

public class ResponseBase {
    private boolean success;
    private String msg;
    private int code;
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
}
