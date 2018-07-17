package hyj.xw.modelHttp;

/**
 * 维护结果
 */
public class MaintainResultVO {

    private Long id;
    /**
     * 登录结果状态，0 正常 1 密码错误 2 帐号的使用存在异常 3 操作频率过快 4 登录环境异常   5 新设备   6 外挂 7 长期未登录  8 批量注册  98 已售，99 作废
     */
    private Integer dieFlag;

    /**
     * 登录失败原因
     */
    private String expMsg;

    /**
     * 登录ip
     */
    private String ip;

    public MaintainResultVO(Long id,Integer dieFlag,String expMsg, String ip){
        this.id = id;
        this.dieFlag = dieFlag;
        this.expMsg = expMsg;
        this.ip = ip;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDieFlag() {
        return dieFlag;
    }

    public void setDieFlag(Integer dieFlag) {
        this.dieFlag = dieFlag;
    }

    public String getExpMsg() {
        return expMsg;
    }

    public void setExpMsg(String expMsg) {
        this.expMsg = expMsg;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
