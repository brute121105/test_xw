package hyj.xw.conf;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.model.PhoneInfo;
import hyj.xw.util.DaoUtil;
import hyj.xw.util.FileUtil;

/**
 * Created by Administrator on 2018/1/4.
 */

public class PhoneConf {
    static  String str;
    static {
        File file = new File("/sdcard/A_hyj_008data");
        File[] files = file.listFiles();
        if(files!=null&&files.length>0){
            String imporFileName = files[files.length - 1].getPath();//获取最新文件名
            System.out.println("--->导入文件：" + imporFileName);
            str = FileUtil.readAll(imporFileName);
        }
    }
   public static PhoneInfo createPhoneInfo(int index){
       System.out.println("str-->" + str);
       List<Wx008Data> datas = JSON.parseArray(str, Wx008Data.class);
       int i=0;
       for(Wx008Data data:datas){
           i = i+1;
           data.setPhoneInfo(data.getDatas());
       }
       Wx008Data wx008Data = datas.get(index);
       SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       System.out.println("datas-->"+JSON.toJSONString(datas));
       System.out.println("teime-->"+simpleDateFormat.format(wx008Data.getCreateTime()));
       return wx008Data.getPhoneInfo();
   }

   public static int importData(){
        List<Wx008Data> datas = JSON.parseArray(str, Wx008Data.class);
        int successCount = 0;
        for (Wx008Data data : datas) {
            List<Wx008Data> getData=null;
            if(!TextUtils.isEmpty(data.getWxId())){
                getData = DataSupport.where("wxId=?", data.getWxId()).find(Wx008Data.class);
            }else if(!TextUtils.isEmpty(data.getPhone())) {
                getData = DataSupport.where("phone=?", data.getPhone()).find(Wx008Data.class);
            }else {//解决无法导入008空数据
                getData = DataSupport.where("datas=?", data.getDatas()).find(Wx008Data.class);
            }
            if (getData == null || getData.size() == 0) {
                if (data.save()) {
                    successCount = successCount + 1;
                }
            }
           /* if("pqk473".equals(data.getWxId())||"cyv472".equals(data.getWxId())){
            }*/
        }
        return successCount;
    }

    public static String[] getAllPhoneList() {

        List<Wx008Data> wx008Datas = DaoUtil.getWx008Datas();
        List<String> datas = new ArrayList<String>();
        for (int i = 0, l = wx008Datas.size(); i < l; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-HH");
            String time = sdf.format(wx008Datas.get(i).getCreateTime());
            String lastLoginTime = sdf.format(wx008Datas.get(i).getCreateTime());
            String cn = wx008Datas.get(i).getCnNum();
            //序号-账号-ac时间 国家 上次登录时间
            String wxid = wx008Datas.get(i).getWxId();
            if(TextUtils.isEmpty(wxid)){
                wxid = wx008Datas.get(i).getPhone();
            }
            String showMsg = i + "-" + wxid + " " + time + " " + (cn == null ? "86" : cn);
            datas.add(showMsg);

            System.out.println(showMsg+" --nickName:"+wx008Datas.get(i).getNickName()+" repPhone:"+wx008Datas.get(i).getReplacePhone()+" pwd:"+wx008Datas.get(i).getWxPwd()+" phone:"+wx008Datas.get(i).getPhone());
            //删除测试
            Wx008Data wd = wx008Datas.get(i);

            System.out.println(i+" phoneStr wd-->"+JSON.toJSONString(wd));
            //插入008数据到phoneStr字段
            /*System.out.println(i+" phoneStr wd-->"+JSON.toJSONString(wd));
            wd.setPhoneStrBy008Datas();
            System.out.println(i+" phoneStr save-->"+wd.save());*/

           /*
            String wxid1 = wd.getWxId(),phone1 = wd.getPhone();
             if("4422635145".equals(wxid1)||"4305623539".equals(wxid1)){
                 int cn1 = wd.delete();
                 System.out.println("cn1--->"+cn1);
             }
            System.out.println("wxid-->"+wxid1+" phone1-->"+phone1);*/
            //修改操作
            /*if(i>1088&&i<1119){
                System.out.println(i+ " phone-->"+wd.getPhone());
                String newPwd = "www23"+wd.getPhone().substring(wd.getPhone().length()-3);
                wd.setWxPwd(newPwd);

                int cn1 = wd.updateAll("guid=?",wd.getGuid());
                System.out.println("cn1-->"+cn1+" newPwd:"+newPwd);
            }*/
        }
        return datas.toArray(new String[datas.size()]);
    }

    public static PhoneInfo createPhoneInfo(){
        PhoneInfo phoneInfo = new PhoneInfo();

        phoneInfo.setDeviceId("863184025089311");
        phoneInfo.setAndroidId("0d160be274c5ad11");
        phoneInfo.setLineNumber("579783111");
        phoneInfo.setSimSerialNumber("89860032077034774211");
        phoneInfo.setSubscriberId("460023207703411");
        phoneInfo.setSimCountryIso("cn");

        phoneInfo.setSimOperator("46002");
        phoneInfo.setSimOperatorName("China Mobile");
        phoneInfo.setNetworkCountryIso("cn");
        phoneInfo.setNetworkOperator("46002");
        phoneInfo.setNetworkOperatorName("China Mobile");

        phoneInfo.setNetworkType(8);
        phoneInfo.setPhoneType(1);
        phoneInfo.setSimState(1);

        phoneInfo.setRelease("6.0");
        phoneInfo.setSdk("23");

        //系统架构
        phoneInfo.setCPU_ABI("armeabi-v7a");
        phoneInfo.setCPU_ABI2("armeabi");

        phoneInfo.setRadioVersion("MOLY.LR9.W1414A.MD.LWTG.MP.V2.P27, 2014/09/17 14:58");//固定版本

        phoneInfo.setBrand("lenovo");
        phoneInfo.setModel("lenovo A788t");
        phoneInfo.setBuildId("6462aa34c01a");
        phoneInfo.setDisplay("9c86b49441b1");

        phoneInfo.setProductName("A788t");
        phoneInfo.setManufacturer("lenovo");
        phoneInfo.setDevice("lenovo A788t");
        phoneInfo.setHardware("mt6582");
        phoneInfo.setFingerprint("Lenovo/A360t/A360t:4.4.2/KOT49H/A360t_USR_S224_140911:user/release-keys");
        phoneInfo.setSerialno("6f4b2411");
        phoneInfo.setBlueAddress("cb:c3:b0:dd:1a:11");


        String tags = createTags();
        phoneInfo.setBUILD_TAGS(tags);
        phoneInfo.setBUILD_TYPE(tags);
        phoneInfo.setBUILD_USER(tags);
        return phoneInfo;
    }
    private  static String createTags() {
        String str = "";
        for(int i=0;i<12;i++){
            str = str+getRandomAbc();
        }
        return str;

    }
    private  static String getRandomAbc() {
        String chars = "abcdgijktuxyz12365987";
        String str = chars.charAt((int)(Math.random() * 20))+"";
        return str;
    }
}
