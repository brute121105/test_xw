package hyj.xw.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hyj.xw.common.CommonConstant;
import hyj.xw.model.WindowNodeInfo;

/**
 * Created by Administrator on 2018/6/10 0010.
 */

public class WindowNodeInfoConf {
    public static final int MaxSendMsgNum=3;
    public static final String sendPhoneMsgUrl = "http://112.124.31.14:8060/SendSms?token=87BBCED60A9A4801AE8E270E030DCF93289E02C6BAACCD94&requestid=fujiantest";
    public static WindowNodeInfo zcSendMsgContentWni;//发送短信内容
    public static WindowNodeInfo zcSendMsgCalledPhoneWni;//发送目标号码
    static {
        zcSendMsgContentWni = new WindowNodeInfo("","","0022","");
        zcSendMsgCalledPhoneWni =new WindowNodeInfo("","","0023","");
    }
   /* public static List<WindowNodeInfo> getWindowNodeInfo(){
        List<WindowNodeInfo> infos = new ArrayList<WindowNodeInfo>();
        infos.add(new WindowNodeInfo("养号",0,CommonConstant.APPCONFIG_CEVN));//清除并准备改机环境
        //infos.add(new WindowNodeInfo("养号",1,CommonConstant.APPCONFIG_APM));//飞行模式
        infos.add(new WindowNodeInfo("养号",1,CommonConstant.APPCONFIG_VPN));//打开VPN界面
        infos.add(new WindowNodeInfo("养号",2,CommonConstant.APPCONFIG_008));//设置008
        infos.add(new WindowNodeInfo("养号",3, CommonConstant.APPCONFIG_SWX));//启动微信
        infos.add(new WindowNodeInfo("养号","注册|登录",1,4,"点击登录1","登录",""));
        infos.add(new WindowNodeInfo("养号","窗口文本",1,5,"点击用微信号登录","用微信号/QQ号/邮箱登录",""));
        infos.add(new WindowNodeInfo("养号",6,CommonConstant.APPCONFIG_VEVN));//判断改机成功
        infos.add(new WindowNodeInfo("养号","窗口文本",2,7.1,"输入账号","","00211"));
        infos.add(new WindowNodeInfo("养号","窗口文本",2,7.2,"输入密码","","00221"));
        infos.add(new WindowNodeInfo("养号","窗口文本",1,7.3,"点击登录2","登录",""));
        infos.add(new WindowNodeInfo("养号",8.1,CommonConstant.APPCONFIG_VLS));//判断登录成功
        infos.add(new WindowNodeInfo("养号","窗口文本",3,8.2,"登录异常","新设备",""));
        infos.add(new WindowNodeInfo("养号","窗口文本",3,8.3,"登录异常","登录环境异常",""));
        infos.add(new WindowNodeInfo("养号","窗口文本",3,8.4,"登录异常","密码错误",""));
        infos.add(new WindowNodeInfo("养号","窗口文本",3,8.5,"登录异常","刷公众号",""));
        infos.add(new WindowNodeInfo("养号","窗口文本",3,8.6,"登录异常","打招呼存在异常",""));
        infos.add(new WindowNodeInfo("异常界面","看看手机通讯录",1,8.1,"点击","否",""));
        infos.add(new WindowNodeInfo("异常界面","窗口文本",1,8.2,"点击完成应急联系人","完成",""));
        return infos;
    }*/
   public static List<WindowNodeInfo> getWindowNodeInfo(){
       List<WindowNodeInfo> infos = new ArrayList<WindowNodeInfo>();
       infos.add(new WindowNodeInfo("注册","a",CommonConstant.APPCONFIG_CEVN));//清除并准备改机环境
       infos.add(new WindowNodeInfo("注册","b",CommonConstant.APPCONFIG_APM));//飞行模式
       //infos.add(new WindowNodeInfo("注册","b",CommonConstant.APPCONFIG_VPN));//打开VPN界面
       infos.add(new WindowNodeInfo("注册","d", CommonConstant.APPCONFIG_SWX));//启动微信
       infos.add(new WindowNodeInfo("注册","注册|登录",1,"e","点击注册1","注册",""));
       infos.add(new WindowNodeInfo("注册","窗口文本",2,"f","输入昵称","","00211"));
       infos.add(new WindowNodeInfo("注册","窗口文本",2,"f","输入手机号","","00241"));
       infos.add(new WindowNodeInfo("注册","窗口文本",2,"f","输入密码","","00251"));
       WindowNodeInfo wni = new WindowNodeInfo("注册","手机号注册",1,"f","点击注册2","注册","");
       wni.setRetryFlag(1);
       infos.add(wni);
       infos.add(new WindowNodeInfo("注册","窗口文本",1,"g","点击同意条款","","00000-224"));
       infos.add(new WindowNodeInfo("注册","窗口文本",1,"g","点击下一步","","00000-225"));
       infos.add(new WindowNodeInfo("注册","安全校验",1,"h","点击开始安全校验","","000003"));
       infos.add(new WindowNodeInfo("注册","z",CommonConstant.APPCONFIG_VEVN));//判断改机成功
       infos.add(new WindowNodeInfo("注册","b",CommonConstant.APPCONFIG_GHK));//过滑块

       infos.add(new WindowNodeInfo("注册","窗口文本",3,"i","登录异常","联系符合以下条件的微信用户",""));
       WindowNodeInfo wni2 =new WindowNodeInfo("注册","发送短信后请回到本界面继续下一步",1,"k","点击发送短信","发送短信","");
       wni2.setRetryFlag(1);
       infos.add(wni2);
       infos.add(new WindowNodeInfo("注册","发送短信后请回到本界面继续下一步",1,"L","点击已发送短信下一步","已发送短信，下一步",""));
       infos.add(new WindowNodeInfo("注册","m",CommonConstant.APPCONFIG_VLS));//判断登录成功
       infos.add(new WindowNodeInfo("注册","窗口文本",1,"m","随机界面点击-不是我的，继续注册","不是我的，继续注册",""));
       infos.add(new WindowNodeInfo("注册","窗口文本",3,"m","登录异常","相同手机号不可频繁重复注册微信帐号",""));


       infos.add(new WindowNodeInfo("养号","a",CommonConstant.APPCONFIG_CEVN));//清除并准备改机环境
       infos.add(new WindowNodeInfo("养号","b",CommonConstant.APPCONFIG_APM));//飞行模式 设置了3次登录才修改飞行模式
       //infos.add(new WindowNodeInfo("养号","b",CommonConstant.APPCONFIG_VPN));//打开VPN界面
       //infos.add(new WindowNodeInfo("养号","c",CommonConstant.APPCONFIG_008));//设置008
       infos.add(new WindowNodeInfo("养号","d", CommonConstant.APPCONFIG_SWX));//启动微信
       infos.add(new WindowNodeInfo("养号","注册|登录",1,"e","点击登录1","登录",""));
       infos.add(new WindowNodeInfo("养号","窗口文本",1,"f","点击用微信号登录","用微信号/QQ号/邮箱登录",""));
       infos.add(new WindowNodeInfo("养号","g",CommonConstant.APPCONFIG_VEVN));//判断改机成功
       /*infos.add(new WindowNodeInfo("养号","窗口文本",2,"h","输入账号","","00211"));
       infos.add(new WindowNodeInfo("养号","窗口文本",2,"h","输入密码","","00221"));*/
       infos.add(new WindowNodeInfo("养号","窗口文本",2,"h","输入账号","","00311"));
       infos.add(new WindowNodeInfo("养号","窗口文本",2,"h","输入密码","","00321"));
       infos.add(new WindowNodeInfo("养号","窗口文本",1,"h","点击登录2","登录",""));
       infos.add(new WindowNodeInfo("养号","g",CommonConstant.APPCONFIG_VLS));//判断登录成功
       infos.add(new WindowNodeInfo("养号","窗口文本",3,"g","登录异常","新设备",""));
       infos.add(new WindowNodeInfo("养号","窗口文本",3,"g","登录异常","登录环境异常",""));
       infos.add(new WindowNodeInfo("养号","窗口文本",3,"g","登录异常","帐号的使用存在异常",""));
       infos.add(new WindowNodeInfo("养号","窗口文本",3,"g","登录异常","密码错误",""));
       infos.add(new WindowNodeInfo("养号","窗口文本",3,"g","登录异常","刷公众号",""));
       infos.add(new WindowNodeInfo("养号","窗口文本",3,"g","登录异常","打招呼存在异常",""));
       infos.add(new WindowNodeInfo("养号","窗口文本",3,"g","登录异常","长期未登录",""));
       infos.add(new WindowNodeInfo("养号","窗口文本",3,"g","登录异常","该微信帐号因使用了微信外挂",""));
       infos.add(new WindowNodeInfo("养号","看看手机通讯录",1,"g","随机界面点击-否","否",""));
       infos.add(new WindowNodeInfo("养号","窗口文本",1,"g","随机界面点击-完成应急联系人","完成",""));
       infos.add(new WindowNodeInfo("关手机号搜索","窗口文本",1,"a","点击我","我",""));
       infos.add(new WindowNodeInfo("关手机号搜索","窗口文本",1,"b","点击设置","设置",""));
       infos.add(new WindowNodeInfo("关手机号搜索","窗口文本",1,"c","点击隐私","隐私",""));
       infos.add(new WindowNodeInfo("关手机号搜索","窗口文本",1,"d","点击添加我的方式","添加我的方式",""));
       infos.add(new WindowNodeInfo("关手机号搜索","关闭后，其他用户将不能",4,"e","点击手机号","","00221","已开启"));

       infos.add(new WindowNodeInfo("发圈","窗口文本",1,"a","点击发现","发现",""));
       infos.add(new WindowNodeInfo("发圈","窗口文本",1,"b","点击朋友圈","朋友圈",""));
       infos.add(new WindowNodeInfo("发圈","朋友圈封面",6,"c","长按相机","","002"));
       infos.add(new WindowNodeInfo("发圈","窗口文本",1,"d","点击我知道了","我知道了",""));
       infos.add(new WindowNodeInfo("发圈","这一刻的想法...",2,"e","输入发圈内容","","0000"));
       infos.add(new WindowNodeInfo("发圈","窗口文本",1,"e","点击发表","发表",""));

       infos.add(new WindowNodeInfo("修改密码","窗口文本",1,"a","点击我","我",""));
       infos.add(new WindowNodeInfo("修改密码","窗口文本",1,"b","点击设置","设置",""));
       infos.add(new WindowNodeInfo("修改密码","窗口文本",1,"c","点击帐号与安全","帐号与安全",""));
       infos.add(new WindowNodeInfo("修改密码","查看二维码",7,"x","获取nodeText","","00510"));
       infos.add(new WindowNodeInfo("修改密码","窗口文本",1,"d","点击微信密码","微信密码",""));
       infos.add(new WindowNodeInfo("修改密码","窗口文本",2,"e","填写原密码","","0034"));
       infos.add(new WindowNodeInfo("修改密码","窗口文本",2,"e","填写新密码","","0036"));
       infos.add(new WindowNodeInfo("修改密码","窗口文本",2,"e","再次填写确认","","0038"));
       infos.add(new WindowNodeInfo("修改密码","窗口文本",1,"e","点击完成","完成",""));
       infos.add(new WindowNodeInfo("修改密码","窗口文本",5,"f","判断密码设置成功","微信密码设置成功，以后可通过手机号+微信密码登录微信",""));

       infos.add(new WindowNodeInfo("提取wxid","窗口文本",1,"a","点击我","我",""));
       infos.add(new WindowNodeInfo("提取wxid","微信号：wxid",7,"b","获取nodeText","","00610"));

       infos.add(new WindowNodeInfo("加好友","a","开始加好友"));//清除并准备改机环境

       return infos;
   }

    //排序，分组
    public static Map<Integer,List<WindowNodeInfo>> getWinfoMapByOperation(String operation){
        Map<Integer,List<WindowNodeInfo>> map  = new HashMap<Integer,List<WindowNodeInfo>>();
        List<WindowNodeInfo> infos = getWindowNodeInfo();
        int key = 0;
        for(WindowNodeInfo info:infos){
            if(!operation.equals(info.getOperation())) continue;
            if(map.get(key)==null){
                map.put(key,new ArrayList<WindowNodeInfo>());
            }
            List<WindowNodeInfo> mapInfos = map.get(key);
            if(mapInfos.size()==0||mapInfos.get(mapInfos.size()-1).getActionGroupTag().equals(info.getActionGroupTag())){
                info.setActionNo(key);
                map.get(key).add(info);
            }else{
                ++key;
                info.setActionNo(key);
                map.put(key,new ArrayList<WindowNodeInfo>());
                map.get(key).add(info);
            }
        }
        return map;
    }

    /*public static Map<Integer,List<WindowNodeInfo>> getWinfoMapByOperation(String operation){
        Map<Integer,List<WindowNodeInfo>> map  = new HashMap<Integer,List<WindowNodeInfo>>();
        List<WindowNodeInfo> infos = getWindowNodeInfo();
        for(WindowNodeInfo info:infos){
            if(!operation.equals(info.getOperation())) continue;
            int key = (int)info.getActionNo();
            if(map.get(key)==null){
                map.put(key,new ArrayList<WindowNodeInfo>());
            }
            map.get(key).add(info);
        }
        return map;
    }*/
}
