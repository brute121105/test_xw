package hyj.xw.conf;

import java.util.ArrayList;
import java.util.List;

import hyj.xw.model.LitePalModel.WxNode;

/**
 * Created by Administrator on 2017/12/14.
 */

public class NodeConf {
    static  List<WxNode> wxNodeList = new ArrayList<WxNode>();
    static {
        wxNodeList.add(new WxNode("微信","WX"));
        wxNodeList.add(new WxNode("通讯录","TXL"));
        wxNodeList.add(new WxNode("发现","FX"));
        wxNodeList.add(new WxNode("我","W"));
    }
    public static List<WxNode> getWxStaticNode(){
          return wxNodeList;
    }
}
