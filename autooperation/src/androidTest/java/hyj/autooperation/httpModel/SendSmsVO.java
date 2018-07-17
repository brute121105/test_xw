package hyj.autooperation.httpModel;


import java.io.Serializable;

public class SendSmsVO implements Serializable {

    /**
     * 主叫手机号码，用于发送短信的手机号码，由系统平台方分配
     */
    private String callNumber;

    /**
     * 被叫号码，接收短信的号码
     */
    private String calledNumber;

    /**
     * 上行短信内容，由系统平台方分配
     */
    private String content;

    public SendSmsVO(String callNumber,String calledNumber,String content){
        this.callNumber = callNumber;
        this.calledNumber = calledNumber;
        this.content = content;
    }

    private static final long serialVersionUID = 1L;

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getCalledNumber() {
        return calledNumber;
    }

    public void setCalledNumber(String calledNumber) {
        this.calledNumber = calledNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
