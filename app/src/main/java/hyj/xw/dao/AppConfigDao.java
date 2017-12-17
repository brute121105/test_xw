package hyj.xw.dao;


import com.alibaba.fastjson.JSON;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hyj.xw.model.LitePalModel.AppConfig;

/**
 * Created by Administrator on 2017/12/14.
 */

public class AppConfigDao {
    private static final String TAG = "AppConfigDao-->";
    public static List<AppConfig> findAll(){
        List<AppConfig> lsit = DataSupport.findAll(AppConfig.class);
        System.out.println(TAG+" findAll-->"+ JSON.toJSONString(lsit));
        return lsit;
    }

    public static String findAcountsStrByCode(String code){
        List<AppConfig> list =DataSupport.where("configCode=?",code).find(AppConfig.class);
        String content = "";
        if(list!=null&&list.size()==1){
            content = list.get(0).getConfigContent();
        }
        System.out.println(TAG+" findAcountsStrByCode-->"+ JSON.toJSONString(content));
        return content;
    }
    public static List<String[]> findAcountsListByCode(String code){
        List<String[]> acounts = new ArrayList<String[]>();
        List<AppConfig> list =DataSupport.where("configCode=?",code).find(AppConfig.class);
        if(list!=null&&list.size()==1){
            String content = list.get(0).getConfigContent();
            for(String str:content.split(",|，")){
                acounts.add(str.split("-"));
            }

        }
        System.out.println(TAG+" findAcountsListByCode-->"+ JSON.toJSONString(acounts));
        return acounts;
    }
    //修改操作，以configCode为查询条件
    public static void saveOrUpdate(AppConfig config){
        List<AppConfig> configs =  DataSupport.where("configCode=?",config.getConfigCode()).find(AppConfig.class);
        if(configs!=null&&configs.size()==1){
            config.setModifyTime(new Date());
            int updateCount = config.updateAll("configCode=?",config.getConfigCode());
            System.out.println(TAG+" update success ,count:"+updateCount+" config:"+JSON.toJSONString(config));
        }else{
            config.setCreateTime(new Date());
            if(config.save()){
                System.out.println(TAG+" save success:"+" config:"+JSON.toJSONString(config));
            }
        }

    }
}
