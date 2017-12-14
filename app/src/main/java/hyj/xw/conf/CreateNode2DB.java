package hyj.xw.conf;


import com.alibaba.fastjson.JSON;

import java.util.List;

import hyj.xw.model.LitePalModel.WxNode;

/**
 * Created by Administrator on 2017/12/14.
 */

public class CreateNode2DB {

    public static void create(){
        List<WxNode> nodeList = NodeConf.getWxStaticNode();
        for(WxNode node:nodeList){
            System.out.println("save node:"+node.save()+"  --"+ JSON.toJSONString(node));
        }
    }
}
