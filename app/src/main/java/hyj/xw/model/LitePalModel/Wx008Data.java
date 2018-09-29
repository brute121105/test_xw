package hyj.xw.model.LitePalModel;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.litepal.crud.DataSupport;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import hyj.xw.hook.newHook.NewPhoneInfo;
import hyj.xw.model.PhoneInfo;

/**
 * Created by Administrator on 2017/8/22.
 */

public class Wx008Data  extends DataSupport{
    private PhoneInfo phoneInfo = new PhoneInfo();
    private Long id;
    private String phoneStrs;
    private String phoneStrsAw;//aw数据
    private String phoneStrs008Json;//008json数据
    private String rgWxStatus;//aw状态
    private String guid;
    private String datas;
    private String phone;
    private String wxId;
    private String wxPwd;//属性名称为pwd无法保存？
    private String newWxPwd;
    private String expMsg;//登录失败原因
    private int dieFlag;//0正常 1账号异常 2 操作频率过快 3 登录环境异常 长期未登录  批量注册 4手机不在身边
    private String cnNum;//国别区号
    private String loginState;//登陆状态 0 登陆不成功 1 登陆成功
    private String friends;//好友
    private Date createTime;
    private Date lastLoginTime;
    private String dataFlag;//007 008 009  抓取008数据  010 转换神龙008数据 011最新注册
    private String nickName;
    private String replacePhone;
    private String wxid19;
    private String dataType;//0所有 1早期的477个

    private String regDevice;//注册设备
    private String regIp;//注册ip
    private Integer regHookType;//注册改机类型

    public String getNewWxPwd() {
        return newWxPwd;
    }

    public void setNewWxPwd(String newWxPwd) {
        this.newWxPwd = newWxPwd;
    }

    public String getPhoneStrs008Json() {
        return phoneStrs008Json;
    }

    public void setPhoneStrs008Json(String phoneStrs008Json) {
        this.phoneStrs008Json = phoneStrs008Json;
    }

    public String getRegDevice() {
        return regDevice;
    }

    public void setRegDevice(String regDevice) {
        this.regDevice = regDevice;
    }

    public String getRegIp() {
        return regIp;
    }

    public void setRegIp(String regIp) {
        this.regIp = regIp;
    }

    public Integer getRegHookType() {
        return regHookType;
    }

    public void setRegHookType(Integer regHookType) {
        this.regHookType = regHookType;
    }

    public void setPhoneInfo(PhoneInfo phoneInfo) {
        this.phoneInfo = phoneInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataType() {
        return dataType;
    }


    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Wx008Data(){
    }



    public List<String> getSl008To008Datas(String phoneStrsAw){
        NewPhoneInfo npi = JSONObject.parseObject(phoneStrsAw,NewPhoneInfo.class);
        String[] arr = {"从网络获取数据","序列号",npi.getDeviceId(),"android_id",npi.getAndroidId(),"手机号码",npi.getLine1Number(),"手机卡序列号",npi.getSimSerialNumber(),
                "IMSI",npi.getSubscriberId(),"手机卡国家","cn","运营商",npi.getSimOperator(),"运营商名字",npi.getSimOperatorName(),"国家iso代码","cn",
                "网络运营商类型",npi.getNetworkOperator(),"网络类型名",npi.getNetworkOperatorName(),"网络类型",npi.getNetworkType()+"","手机类型",npi.getPhoneType()+"","手机卡状态",npi.getSimState()+"",
                "mac地址",npi.getMacAddress(),"无线路由器名","","无线路由器地址","","系统版本",npi.getBuildRelease(),"系统版本值",npi.getBuildSdk(),
                "系统架构（请保留下划线，下划线隔开的为两个数据）",npi.getBuildAbi()+"_"+npi.getBuildAbi2(),
                "屏幕分辨率","1080x1920","固件版本",npi.getBuildRadioVersion(),"品牌",npi.getBuildBrand(),"型号",npi.getBuildModel(),"ID",npi.getBuildId(),"DISPLAY",npi.getDisplayId(),
                "产品名",npi.getBuildProduct(),"制造商",npi.getBuildManufacturer(),"设备名",npi.getBuildDevice(),"cpu型号",npi.getCpuName(),"硬件",npi.getBuildHardware(),
                "指纹",npi.getBuildFingerprint(),"串口序列号",npi.getSerialno(),"蓝牙地址",npi.getIpAddress(),"GPU","null","GPU厂商","null","内网ip（wifi可用）","null","位置模拟","基站定位（需要关闭gps）","gps定位(修改定位成功的gps数据)","gps定位(修改定位成功的gps数据)+禁用基站","禁用基站","系统默认","基站位置","3903310032579","设置","gps位置","null","设置","联网方式（vip可用）","wifi连接","数据流量","系统默认",null,null};
        return Arrays.asList(arr);
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

        phoneInfo.setMacAddress(strs[31+i]);
        phoneInfo.setRouteName(strs[33+i]);
        phoneInfo.setRouteAddress(strs[35+i]);

        phoneInfo.setRelease(strs[37+i]);
        phoneInfo.setSdk(strs[39+i]);

        //系统架构
        String xtjg = strs[41+i];
        if(xtjg.contains("_")){
            String[] xtjgs = xtjg.split("_");
            if(xtjgs!=null&&xtjgs.length>0){
                phoneInfo.setCPU_ABI(xtjgs[0]);
                if(xtjgs.length>1){
                    phoneInfo.setCPU_ABI2(xtjgs[1]);
                }else {
                    phoneInfo.setCPU_ABI2("");
                }
            }
        }

        phoneInfo.setRadioVersion(strs[45+i]);//固定版本

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
        phoneInfo.setBlueAddress(strs[69+i]);


        String tags = createTags();
        phoneInfo.setBUILD_TAGS(tags);
        phoneInfo.setBUILD_TYPE(tags);
        phoneInfo.setBUILD_USER(tags);

    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    private  String createTags() {
        String str = "";
        for(int i=0;i<12;i++){
            str = str+getRandomAbc();
        }
        return str;

    }
    private  String getRandomAbc() {
        String chars = "abcdgijktuxyz12365987";
        String str = chars.charAt((int)(Math.random() * 20))+"";
        return str;
    }

    public String getPhoneStrs() {
        return phoneStrs;
    }

    public void setPhoneStrs(String phoneStrs) {
        this.phoneStrs = phoneStrs;
    }

    public PhoneInfo getPhoneInfo() {
        return phoneInfo;
    }

    public void setPhoneInfo1(PhoneInfo phoneInfo) {
        this.phoneInfo = phoneInfo;
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

    public String getDataFlag() {
        return dataFlag;
    }

    public void setDataFlag(String dataFlag) {
        this.dataFlag = dataFlag;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getReplacePhone() {
        return replacePhone;
    }

    public void setReplacePhone(String replacePhone) {
        this.replacePhone = replacePhone;
    }

    public String getWxid19() {
        return wxid19;
    }

    public void setWxid19(String wxid19) {
        this.wxid19 = wxid19;
    }

    public String getPhoneStrsAw() {
        return phoneStrsAw;
    }

    public void setPhoneStrsAw(String phoneStrsAw) {
        this.phoneStrsAw = phoneStrsAw;
    }

    public String getRgWxStatus() {
        return rgWxStatus;
    }

    public void setRgWxStatus(String rgWxStatus) {
        this.rgWxStatus = rgWxStatus;
    }

    //008数据以PhoneInfo格式放到phoneStrs
    public void setPhoneStrBy008Datas(){
        this.setPhoneInfo(this.getDatas());
        this.setPhoneStrs(JSON.toJSONString(this.getPhoneInfo()));
    }

}
