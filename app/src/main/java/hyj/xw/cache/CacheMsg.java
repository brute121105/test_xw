package hyj.xw.cache;

import com.wx.wyyk.netty.commons.HelpLoginMsg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/9/18 0018.
 */

public class CacheMsg {

    public static List<HelpLoginMsg> helpLoginMsgs = Collections.synchronizedList(new ArrayList<HelpLoginMsg>());

    public static HelpLoginMsg getHelpLoginMsg(){
        if(helpLoginMsgs.size()==0) return null;
        System.out.println("doAction--->获取前长度helpLoginMsgs："+helpLoginMsgs.size());
        HelpLoginMsg helpLoginMsg = helpLoginMsgs.remove(helpLoginMsgs.size()-1);
        System.out.println("doAction--->获取后长度helpLoginMsgs："+helpLoginMsgs.size());
        return helpLoginMsg;

    }

    public static void addHelpLoginMsg(HelpLoginMsg helpLoginMsg){
        helpLoginMsgs.add(helpLoginMsg);
    }
}
