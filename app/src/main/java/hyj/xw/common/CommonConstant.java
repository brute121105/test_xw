package hyj.xw.common;

/**
 * Created by Administrator on 2017/12/14.
 */

public class CommonConstant {
    public static String AUTOLOGINTHREAD = "autoLogin";
    public static String APPCONFIG_LOGIN_ACCOUNT = "loginAccount";
    public static String APPCONFIG_EXT = "ext"; //008提取008数据 001自动切换登录  601设置微信号 602修改密码  603扫描登录pc 604微信号搜索昵称
    //6051 换绑手机（重新登录获取接码平台token）6050 换绑手机（本地数据库获取token） 606扫码加群 607发朋友圈  608筷子挂机test开发模式 609微信号搜索添加指定好友  611自动通过好友添加请求
    //610取关公总号  612获取昵称
    public static String APPCONFIG_START_LOGINACCOUNT = "startLoginAccount";//开始登陆号码
    public static String APPCONFIG_START_LOGIN_INDEX= "startLoginIndex";//开始登陆序号
    public static String APPCONFIG_END_LOGIN_INDEX= "endLoginIndex";
    public static String APPCONFIG_END_LOGINACCOUNT = "endLoginAccount";//开始登陆号码

    public static String APPCONFIG_IS_FEED = "isfeed";//养号
    public static String APPCONFIG_IS_AF_BY_WXID = "isAfByWxid";//添加好友
    public static String APPCONFIG_IS_SEND_FR = "isSendFr";//发圈
    public static String APPCONFIG_IS_RC_FRIEND = "isRcFriend";//自动通过好友
    public static String APPCONFIG_IS_CHANGE_PWD = "isChanePwd";//修改密码
    public static String APPCONFIG_IS_SET_WXID = "isSetWxis";//设置微信号
    public static String APPCONFIG_IS_SMJQ = "isSmjq";//扫码加群
    public static String APPCONFIG_IS_REP_PHONE = "isReplacePhone";//解绑手机
    public static String APPCONFIG_IS_LOGIN_BY_PHONE = "isLoginByPhone";//手机号登录

    public static String APPCONFIG_NEW_PWD = "newPwd";//修改密码新密码

    public static String APPCONFIG_IS_AIR_CHANGE_IP = "isAirChangeIp";//飞行模式换ip
    public static String APPCONFIG_IS_LOGIN_PAUSE = "isLoginSuccessPause";//登录成功暂停
    public static String APPCONFIG_CN_NUM = "cnNum";//国别
    public static String APPCONFIG_API_ID = "apiId";//api账号
    public static String APPCONFIG_API_PWD = "apiPwd";//api密码
    public static String APPCONFIG_API_TYPE = "apiType";//api平台
    public static String APPCONFIG_API_PROJECT_ID = "apiProjectId";//api项目id
    public static String APPCONFIG_API_PHONEE = "apiPhone";//api取得手机号
    public static String APPCONFIG_API_PHONE_CODE = "apiPhoneCode";//api验证码
    public static String APPCONFIG_API_TOKEN = "apiToken";//api项目id
    public static String APPCONFIG_DELETE_DATA_INDEX = "deleteByIndex";//删除所有数据 格式 5-20

    public static String APPCONFIG_AFS = "addFriendWxids";//添加好友微信号

    public static String APPCONFIG_SWX = "启动微信";//启动微信
    public static String APPCONFIG_CEVN = "清除并准备改机环境";//清除并准备改机环境
    public static String APPCONFIG_VLS= "判断登录成功";//判断登录成功
    public static String APPCONFIG_VEVN= "判断改机成功";//判断改机成功
    public static String APPCONFIG_APM= "开启飞行模式";
    public static String APPCONFIG_VPN= "连接VPN";
    public static String APPCONFIG_008= "设置008";
    public static String APPCONFIG_SENDMSG= "发送短信";

    public static int index=0;
}
