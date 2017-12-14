package hyj.xw.dao;


import com.alibaba.fastjson.JSON;

import org.litepal.crud.DataSupport;

import java.util.List;

import hyj.xw.model.LitePalModel.WxNode;

/**
 * Created by Administrator on 2017/12/14.
 */

public class WxNodeDao {
    public static List<WxNode> findAllNode(){
        List<WxNode> lsit = DataSupport.findAll(WxNode.class);
        System.out.println("WxNodeDao findAllNode-->"+ JSON.toJSONString(lsit));
        return lsit;
    }
}
