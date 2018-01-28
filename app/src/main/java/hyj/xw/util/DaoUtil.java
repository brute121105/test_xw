package hyj.xw.util;

import com.alibaba.fastjson.JSON;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

import hyj.xw.model.LitePalModel.Wx008Data;


/**
 * Created by asus on 2017/11/26.
 */

public class DaoUtil {
    public static List<Wx008Data> getWx008Datas(){
         //List<Wx008Data> wx008Datas = DataSupport.where("(expMsg  not like ? and expMsg  not like ?) or expMsg is null","%被限制登录%","%保护状态%").order("createTime asc").find(Wx008Data.class);
        //List<Wx008Data> wx008Datas = DataSupport.where("(expMsg  like ? or expMsg  like ?)","%被限制登录%","%保护状态%").order("createTime asc").find(Wx008Data.class);
        //List<Wx008Data> wx008Datas = DataSupport.where("(expMsg  like ? or expMsg  like ?) and cnNum=?","%被限制登录%","%保护状态%","63").order("createTime asc").find(Wx008Data.class);
        //List<Wx008Data> wx008Datas = DataSupport.where("cnNum is null and wxId is not null and (expMsg  like ? or expMsg  like ?)","%被限制登录%","%保护状态%").order("createTime asc").find(Wx008Data.class);
        List<Wx008Data> wx008Datas = findByDataByColumn("dataFlag","008");

        return wx008Datas;
    }

    public static Wx008Data findByPhone(String phone){
        List<Wx008Data> wx008Datas = DataSupport.where("phone=?",phone).find(Wx008Data.class);
        if(wx008Datas!=null&&wx008Datas.size()==1)
            return wx008Datas.get(0);
        else
            return null;
    }

    public static void findByDataFlag(String dataFlag){
        List<Wx008Data> wx008Datas = DataSupport.where("dataFlag=?",dataFlag).find(Wx008Data.class);
        for(Wx008Data data:wx008Datas){
            System.out.println("fetch008data--->"+ JSON.toJSONString(data));
        }

    }

    public static List<Wx008Data> findByDataByColumn(String columnName,String value){
        List<Wx008Data> wx008Datas = DataSupport.where(columnName+"=?",value).find(Wx008Data.class);
        for(Wx008Data data:wx008Datas){
            System.out.println("findByDataByColumn--->"+ JSON.toJSONString(data));
        }
        return wx008Datas;
    }

    public static List<Wx008Data> find008nullDatas(){
        List<Wx008Data> wx008Datas = DataSupport.where("phone is null and wxId is null").order("createTime asc").find(Wx008Data.class);
        return wx008Datas;
    }

    public static Wx008Data findOne008NullDatas(){
        Wx008Data wx008Data = null;
        List<Wx008Data> wx008Datas = find008nullDatas();
        if(wx008Datas!=null&&wx008Datas.size()>0){
            wx008Data = wx008Datas.get(0);
        }
        return wx008Data;
    }

    public static int setLoginPhoneDataTo008NullData(String phone,String pwd,String cnNum){
          int updateCn = 0;
          Wx008Data wx008Data =  DaoUtil.findOne008NullDatas();
               if(wx008Data!=null){
                   wx008Data.setPhone(phone);
                   wx008Data.setWxPwd(pwd);
                   wx008Data.setCnNum(cnNum);
                   wx008Data.setCreateTime(new Date());
                   updateCn = wx008Data.updateAll("guid=?",wx008Data.getGuid());
                }
        return updateCn;
    }
    public static int setLoginWxidDataTo008NullData(String wxid,String pwd,String cnNum){
        int updateCn = 0;
        Wx008Data wx008Data =  DaoUtil.findOne008NullDatas();
        if(wx008Data!=null){
            wx008Data.setWxId(wxid);
            wx008Data.setWxPwd(pwd);
            wx008Data.setCnNum(cnNum);
            wx008Data.setCreateTime(new Date());
            updateCn = wx008Data.updateAll("guid=?",wx008Data.getGuid());
        }
        return updateCn;
    }


    public static Integer getLoginFailNum(){
        List<Wx008Data> wx008Datas = DataSupport.where("loginState='0'").find(Wx008Data.class);
        if(wx008Datas==null)
            return null;
        else
            return wx008Datas.size();
    }

    public static int deleteAll(){
        return DataSupport.deleteAll(Wx008Data.class);
    }
}
