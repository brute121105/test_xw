package hyj.xw.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hyj.xw.model.WindowNodeInfo;

/**
 * Created by Administrator on 2018/6/10 0010.
 */

public class WindowNodeInfoConf {
    public static List<WindowNodeInfo> getWindowNodeInfo(){
        List<WindowNodeInfo> infos = new ArrayList<WindowNodeInfo>();
        infos.add(new WindowNodeInfo("养号",0,"启动微信"));
        infos.add(new WindowNodeInfo("养号","窗口文本",1,1,"点击登录1","登录",""));
        infos.add(new WindowNodeInfo("养号","窗口文本",1,2,"点击用微信号登录","用微信号/QQ号/邮箱登录",""));
        infos.add(new WindowNodeInfo("养号","窗口文本",2,3.1,"输入账号","","00211"));
        infos.add(new WindowNodeInfo("养号","窗口文本",2,3.2,"输入密码","","00221"));
        infos.add(new WindowNodeInfo("养号","窗口文本",1,3.3,"点击登录2","登录",""));
        return infos;
    }

    public static Map<Integer,List<WindowNodeInfo>> getWinfoMap(){
        Map<Integer,List<WindowNodeInfo>> map  = new HashMap<Integer,List<WindowNodeInfo>>();
        List<WindowNodeInfo> infos = getWindowNodeInfo();
        for(WindowNodeInfo info:infos){
            int key = (int)info.getActionNo();
            if(map.get(key)==null){
                map.put(key,new ArrayList<WindowNodeInfo>());
            }
            map.get(key).add(info);
        }
        return map;
    }
}
