package hyj.autooperation.httpModel;

/**
 * Created by Administrator on 2018/7/5 0005.
 */

public class Device {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 编号
     */
    private String num;

    /**
     * 在线状态：1、离线，2、在线
     */
    private Integer onlineState;

    /**
     * 运行类型：1、注册；2、养号
     */
    private Integer runType;

    /**
     * 运行状态：1、正常；2、暂停
     */
    private Integer runState;

    /**
     * 是否检测ip：1、检测；2、不检测
     */
    private Integer testIp;

    /**
     * 切换ip的方式：1、VPN切换，2、飞行模式切换，3、不切换
     */
    private Integer changeIpMode;

    /**
     * vpn地址
     */
    private String vpnUrl;

    /**
     * vpn账号
     */
    private String vpnAccount;

    /**
     * vpn密码
     */
    private String vpnPassword;

    /**
     * 团队：1、不选中；2、选中
     */
    private Integer team;

    /**
     * 新闻：1、不选中；2、选中
     */
    private Integer news;

    /**
     * 是否发圈：1、不发圈；2、发圈
     */
    private Integer sendFriends;

    /**
     * 性别：1、男；2、女
     */
    private Integer gender;

    /**
     * 是否签名：1、不签名；2、签名
     */
    private Integer signa;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 是否浏览朋友圈：1、不浏览；2、浏览
     */
    private Integer browseFriends;

    /**
     * 是否关闭验证：1、不关闭；2、关闭
     */
    private Integer closeVerify;

    /**
     * 朋友
     */
    private String friend;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 备注
     */
    private String remark;

    private String loginResult;

    private String wxid;

    private String ipAddress;//本次ip
    private String lastIpAddress;//交互ip，1 mainactivity 获取ip， 长度大于1 mainactivity写入ip

    /**
     * 加好友，1不加好友 2 加好友
     */
    private Integer addFriend;


    public Integer getAddFriend() {
        return addFriend;
    }

    public void setAddFriend(Integer addFriend) {
        this.addFriend = addFriend;
    }

    public String getLastIpAddress() {
        return lastIpAddress;
    }

    public void setLastIpAddress(String lastIpAddress) {
        this.lastIpAddress = lastIpAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    private int hookType;//1 内部 2 008

    /**
     * 提取微信id：1、不提取，2、提取
     */
    private Integer extractWxId;

    private String host;//后台地址
    private String token;
    private String username;//api用户名

    private String callNumber;
    private String calledNumber;
    private String content;

    private Integer changeIp;//1 不改变ip，2 执行改变ip


    public Integer getChangeIp() {
        return changeIp;
    }

    public void setChangeIp(Integer changeIp) {
        this.changeIp = changeIp;
    }

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getHookType() {
        return hookType;
    }

    public void setHookType(int hookType) {
        this.hookType = hookType;
    }

    public Integer getExtractWxId() {
        return extractWxId;
    }

    public void setExtractWxId(Integer extractWxId) {
        this.extractWxId = extractWxId;
    }

    public String getWxid() {
        return wxid;
    }

    public void setWxid(String wxid) {
        this.wxid = wxid;
    }

    public String getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(String loginResult) {
        this.loginResult = loginResult;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Integer getOnlineState() {
        return onlineState;
    }

    public void setOnlineState(Integer onlineState) {
        this.onlineState = onlineState;
    }

    public Integer getRunType() {
        return runType;
    }

    public void setRunType(Integer runType) {
        this.runType = runType;
    }

    public Integer getRunState() {
        return runState;
    }

    public void setRunState(Integer runState) {
        this.runState = runState;
    }

    public Integer getTestIp() {
        return testIp;
    }

    public void setTestIp(Integer testIp) {
        this.testIp = testIp;
    }

    public Integer getChangeIpMode() {
        return changeIpMode;
    }

    public void setChangeIpMode(Integer changeIpMode) {
        this.changeIpMode = changeIpMode;
    }

    public String getVpnUrl() {
        return vpnUrl;
    }

    public void setVpnUrl(String vpnUrl) {
        this.vpnUrl = vpnUrl;
    }

    public String getVpnAccount() {
        return vpnAccount;
    }

    public void setVpnAccount(String vpnAccount) {
        this.vpnAccount = vpnAccount;
    }

    public String getVpnPassword() {
        return vpnPassword;
    }

    public void setVpnPassword(String vpnPassword) {
        this.vpnPassword = vpnPassword;
    }

    public Integer getTeam() {
        return team;
    }

    public void setTeam(Integer team) {
        this.team = team;
    }

    public Integer getNews() {
        return news;
    }

    public void setNews(Integer news) {
        this.news = news;
    }

    public Integer getSendFriends() {
        return sendFriends;
    }

    public void setSendFriends(Integer sendFriends) {
        this.sendFriends = sendFriends;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getSigna() {
        return signa;
    }

    public void setSigna(Integer signa) {
        this.signa = signa;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getBrowseFriends() {
        return browseFriends;
    }

    public void setBrowseFriends(Integer browseFriends) {
        this.browseFriends = browseFriends;
    }

    public Integer getCloseVerify() {
        return closeVerify;
    }

    public void setCloseVerify(Integer closeVerify) {
        this.closeVerify = closeVerify;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
