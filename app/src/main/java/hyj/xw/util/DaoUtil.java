package hyj.xw.util;

import android.os.Environment;
import android.text.TextUtils;

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

        //List<Wx008Data> wx008Datas = findByDataByColumn("dataFlag","008");
        List<Wx008Data> wx008Datas = findByDataBydataFlag();
        //List<Wx008Data> wx008Datas = DataSupport.findAll(Wx008Data.class);
        return wx008Datas;
    }
    public static List<Wx008Data> findByDataBydataFlag(){
         //List<Wx008Data> wx008Datas = DataSupport.where("(dataFlag=? or dataFlag=? or dataFlag=?) and wxPwd is not null","007","008","009").order("createTime asc").find(Wx008Data.class);

          List<Wx008Data> wx008Datas = DataSupport.where("(dataFlag=? or dataFlag=? or dataFlag=?) and dieFlag!=? and (expMsg  like 'success%' or expMsg  like '登录成功%' or expMsg is null)  and wxPwd is not null","007","008","009","991").order("createTime asc").find(Wx008Data.class);

        //List<Wx008Data> wx008Datas = DataSupport.where("(dataFlag=? or dataFlag=? or dataFlag=?) and dieFlag=0","007","008","009").order("createTime asc").find(Wx008Data.class);
        //List<Wx008Data> wx008Datas = DataSupport.where("(dataFlag=? or dataFlag=? or dataFlag=?) and expMsg like '登录成功%'","007","008","009").order("createTime asc").find(Wx008Data.class);
        //List<Wx008Data> wx008Datas = DataSupport.where("(dataFlag=? or dataFlag=? or dataFlag=?) and expMsg like 'success1%'","007","008","009").order("createTime asc").find(Wx008Data.class);
        //List<Wx008Data> wx008Datas = DataSupport.where("(dataFlag=? or dataFlag=? or dataFlag=?) and (expMsg like '新设备登录%' or expMsg like '登录成功%')","007","008","009").order("createTime asc").find(Wx008Data.class);

        return wx008Datas;
    }

    public static List<Wx008Data> findSth(){
        List<Wx008Data> wx008Datas = DataSupport.where("phone is not null and wxId is null and dataFlag=?","008").order("createTime asc").find(Wx008Data.class);
        for(Wx008Data wx008Data:wx008Datas){
            System.out.println("--->"+JSON.toJSONString(wx008Data));
        }
        return wx008Datas;
    }

    public static Wx008Data findByPhone(String phone){
        List<Wx008Data> wx008Datas = DataSupport.where("phone=?",phone).find(Wx008Data.class);
        if(wx008Datas!=null&&wx008Datas.size()==1)
            return wx008Datas.get(0);
        else
            return null;
    }
    public static Wx008Data findByWxNumOrWxid(String wx){
        List<Wx008Data> wx008Datas = DataSupport.where("wxId=? or wxid19=?",wx,wx).find(Wx008Data.class);
        if(wx008Datas!=null&&wx008Datas.size()==1)
            return wx008Datas.get(0);
        else
            return null;
    }

    public static Integer findByNickName(String name){
        List<Wx008Data> wx008Datas = DataSupport.where("nickName=?",name).find(Wx008Data.class);
        if(wx008Datas!=null)
            return wx008Datas.size();
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
            wx008Data.setPhone(wxid);//wxid当phone
            wx008Data.setWxId(wxid);
            wx008Data.setWxPwd(pwd);
            wx008Data.setCnNum(cnNum);
            wx008Data.setCreateTime(new Date());
            updateCn = wx008Data.updateAll("guid=?",wx008Data.getGuid());
        }
        return updateCn;
    }

    public static int updatePwd(Wx008Data wx008Data,String pwd){
        wx008Data.setWxPwd(pwd);
        String guid = wx008Data.getGuid();
        int cn=-2 ;
        if(TextUtils.isEmpty(guid)){
            cn = wx008Data.updateAll("phone=?",wx008Data.getPhone());
        }else {
            cn = wx008Data.updateAll("guid=?",wx008Data.getGuid());
        }
        return cn;
    }

    public static int updateNickName(Wx008Data wx008Data,String nickName){
        wx008Data.setNickName(nickName);
        String wxid = wx008Data.getWxId();
        int cn=-2 ;
        if(!TextUtils.isEmpty(wxid)){
            cn = wx008Data.updateAll("wxId=?",wx008Data.getWxId());
        }else {
            cn = wx008Data.updateAll("phone=?",wx008Data.getPhone());
        }
        return cn;
    }

    public static int updateExpMsg(Wx008Data wx008Data,String expMsg){
        wx008Data.setExpMsg(expMsg);
        String wxid = wx008Data.getWxId();
        int cn=-1 ;
        if(!TextUtils.isEmpty(wxid)){
            cn = wx008Data.updateAll("wxId=?",wx008Data.getWxId());
        }else {
            cn = wx008Data.updateAll("phone=?",wx008Data.getPhone());
        }
        return cn;
    }

    public static int updateRepPhone(Wx008Data wx008Data,String phone){
        wx008Data.setReplacePhone(phone);
        String wxid = wx008Data.getWxId();
        int cn=-2 ;
        if(!TextUtils.isEmpty(wxid)){
            cn = wx008Data.updateAll("wxId=?",wx008Data.getWxId());
        }else {
            cn = wx008Data.updateAll("phone=?",wx008Data.getPhone());
        }
        return cn;
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

    public static void updatePwd(String wxid,String pwd){
        Wx008Data wx008Data = new Wx008Data();
        wx008Data.setWxPwd(pwd);
        int cn = wx008Data.updateAll("wxId=?",wxid);
        System.out.println("更新记录-->cn:"+cn+" wxid:"+wxid+" pwd:"+pwd);
    }


    public static void updatePwdByPhone(String phone,String pwd){
        Wx008Data wx008Data = new Wx008Data();
        wx008Data.setWxPwd(pwd);
        int cn = wx008Data.updateAll("phone=?",phone);
        System.out.println("更新记录-->cn:"+cn+" phone:"+phone+" pwd:"+pwd);
    }

    //读取azy文件的账号密码，更新到数据库
    public static void readFilePhoneAndPwdUpdate2Db(){
      List<String> strs = FileUtil.read008Data(Environment.getExternalStorageDirectory().getAbsolutePath()+"/azy.txt");
        if(strs!=null&&strs.size()>0){
            for(String str:strs){
                if(str.indexOf(",")>-1){
                    String[] arr = str.split(",");
                    if(arr.length>1){
                        updatePwdByPhone(arr[0],arr[1]);
                    }
                }
            }
        }
    }

    //008原始数据提取手机号到 phone字段
    public static  void getPhoneFromDataStrAndSet(){
        List<Wx008Data> wx008Datas = DataSupport.findAll(Wx008Data.class);
        if(wx008Datas!=null&&wx008Datas.size()>0){
            for(Wx008Data data:wx008Datas){
                data.setPhoneInfo(data.getDatas());
                if(TextUtils.isEmpty(data.getPhone())){
                    data.setPhone(data.getPhoneInfo().getLineNumber());
                    boolean fl = data.save();
                    System.out.println("fl-->"+fl);
                }
            }
        }
    }
}
