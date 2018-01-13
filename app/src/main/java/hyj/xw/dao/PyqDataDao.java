package hyj.xw.dao;


import com.alibaba.fastjson.JSON;

import org.litepal.crud.DataSupport;

import java.util.List;

import hyj.xw.model.LitePalModel.PyqData;

/**
 * Created by Administrator on 2017/12/14.
 */

public class PyqDataDao {
    public static List<PyqData> findAll(){
        List<PyqData> lsit = DataSupport.findAll(PyqData.class);
        System.out.println("PyqData findAll-->"+ JSON.toJSONString(lsit));
        return lsit;
    }
}
