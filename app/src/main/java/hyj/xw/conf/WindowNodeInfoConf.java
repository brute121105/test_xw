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
       infos.add(new WindowNodeInfo("养号","a",CommonConstant.APPCONFIG_CEVN));//清除并准备改机环境
       //infos.add(new WindowNodeInfo("养号","b,CommonConstant.APPCONFIG_APM));//飞行模式
       infos.add(new WindowNodeInfo("养号","b",CommonConstant.APPCONFIG_VPN));//打开VPN界面
       infos.add(new WindowNodeInfo("养号","c",CommonConstant.APPCONFIG_008));//设置008
       infos.add(new WindowNodeInfo("养号","d", CommonConstant.APPCONFIG_SWX));//启动微信
       infos.add(new WindowNodeInfo("养号","注册|登录",1,"e","点击登录1","登录",""));
       infos.add(new WindowNodeInfo("养号","窗口文本",1,"f","点击用微信号登录","用微信号/QQ号/邮箱登录",""));
       infos.add(new WindowNodeInfo("养号","g",CommonConstant.APPCONFIG_VEVN));//判断改机成功
       infos.add(new WindowNodeInfo("养号","窗口文本",2,"h","输入账号","","00211"));
       infos.add(new WindowNodeInfo("养号","窗口文本",2,"h","输入密码","","00221"));
       infos.add(new WindowNodeInfo("养号","窗口文本",1,"h","点击登录2","登录",""));
       infos.add(new WindowNodeInfo("养号","g",CommonConstant.APPCONFIG_VLS));//判断登录成功
       infos.add(new WindowNodeInfo("养号","窗口文本",3,"g","登录异常","新设备",""));
       infos.add(new WindowNodeInfo("养号","窗口文本",3,"g","登录异常","登录环境异常",""));
       infos.add(new WindowNodeInfo("养号","窗口文本",3,"g","登录异常","密码错误",""));
       infos.add(new WindowNodeInfo("养号","窗口文本",3,"g","登录异常","刷公众号",""));
       infos.add(new WindowNodeInfo("养号","窗口文本",3,"g","登录异常","打招呼存在异常",""));
       infos.add(new WindowNodeInfo("异常界面","看看手机通讯录",1,"g","点击","否",""));
       infos.add(new WindowNodeInfo("异常界面","窗口文本",1,"g","点击完成应急联系人","完成",""));
       return infos;
   }

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
