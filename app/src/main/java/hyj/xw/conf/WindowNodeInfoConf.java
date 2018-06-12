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
    public static List<WindowNodeInfo> getWindowNodeInfo(){
        List<WindowNodeInfo> infos = new ArrayList<WindowNodeInfo>();
        infos.add(new WindowNodeInfo("养号",0,CommonConstant.APPCONFIG_CEVN));//清除并准备改机环境
        infos.add(new WindowNodeInfo("养号",1,CommonConstant.APPCONFIG_APM));//飞行模式
        infos.add(new WindowNodeInfo("养号",2, CommonConstant.APPCONFIG_SWX));//启动微信
        infos.add(new WindowNodeInfo("养号","注册|登录",1,3,"点击登录1","登录",""));
        infos.add(new WindowNodeInfo("养号","窗口文本",1,4,"点击用微信号登录","用微信号/QQ号/邮箱登录",""));
        infos.add(new WindowNodeInfo("养号",5,CommonConstant.APPCONFIG_VEVN));//判断改机成功
        infos.add(new WindowNodeInfo("养号","窗口文本",2,6.1,"输入账号","","00211"));
        infos.add(new WindowNodeInfo("养号","窗口文本",2,6.2,"输入密码","","00221"));
        infos.add(new WindowNodeInfo("养号","窗口文本",1,6.3,"点击登录2","登录",""));
        infos.add(new WindowNodeInfo("养号",7.1,CommonConstant.APPCONFIG_VLS));//判断登录成功
        infos.add(new WindowNodeInfo("养号","窗口文本",3,7.2,"登录异常","新设备",""));
        infos.add(new WindowNodeInfo("养号","窗口文本",3,7.3,"登录异常","登录环境异常",""));
        infos.add(new WindowNodeInfo("养号","窗口文本",3,7.4,"登录异常","密码错误",""));
        infos.add(new WindowNodeInfo("养号","窗口文本",3,7.5,"登录异常","刷公众号",""));
        infos.add(new WindowNodeInfo("异常界面","窗口文本",1,7.1,"点击完成应急联系人","完成",""));
        return infos;
    }

    public static Map<Integer,List<WindowNodeInfo>> getWinfoMapByOperation(String operation){
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
    }
}
