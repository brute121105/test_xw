package hyj.xw.util;

import org.litepal.crud.DataSupport;

import java.util.List;

import hyj.xw.model.LitePalModel.Wx008Data;


/**
 * Created by asus on 2017/11/26.
 */

public class DaoUtil {
    public static List<Wx008Data> getWx008Datas(){
         List<Wx008Data> wx008Datas = DataSupport.where("(expMsg  not like ? and expMsg  not like ?) or expMsg is null","%被限制登录%","%保护状态%").order("createTime asc").find(Wx008Data.class);
        //List<Wx008Data> wx008Datas = DataSupport.where("(expMsg  like ? or expMsg  like ?)","%被限制登录%","%保护状态%").order("createTime asc").find(Wx008Data.class);
        //List<Wx008Data> wx008Datas = DataSupport.where("(expMsg  like ? or expMsg  like ?) and cnNum=?","%被限制登录%","%保护状态%","63").order("createTime asc").find(Wx008Data.class);
        //List<Wx008Data> wx008Datas = DataSupport.where("cnNum is null and wxId is not null and (expMsg  like ? or expMsg  like ?)","%被限制登录%","%保护状态%").order("createTime asc").find(Wx008Data.class);

        return wx008Datas;
    }

    public static Wx008Data findByPhone(String phone){
        List<Wx008Data> wx008Datas = DataSupport.where("phone=?",phone).find(Wx008Data.class);
        if(wx008Datas!=null&&wx008Datas.size()==1)
            return wx008Datas.get(0);
        else
            return null;
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
