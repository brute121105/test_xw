package hyj.xw.conf;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.model.PhoneInfo;
import hyj.xw.util.AutoUtil;
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
            if(!TextUtils.isEmpty(wx008Datas.get(i).getWxid19())&&TextUtils.isEmpty(wxid)){
                wxid = wx008Datas.get(i).getWxid19().substring(0,10);
            }
            String showMsg = i + "-" + wxid + " " + time + " " + (cn == null ? "86" : cn);
            datas.add(showMsg);

            System.out.println(showMsg+" --nickName:"+wx008Datas.get(i).getNickName()+" repPhone:"+wx008Datas.get(i).getReplacePhone()+" pwd:"+wx008Datas.get(i).getWxPwd()+" phone:"+wx008Datas.get(i).getPhone()+" wxid:"+wx008Datas.get(i).getWxid19());
            //删除测试
            Wx008Data wd = wx008Datas.get(i);

            System.out.println(i+" phoneStr wd-->"+JSON.toJSONString(wd));
            //插入008数据到phoneStr字段
            /*System.out.println(i+" phoneStr wd-->"+JSON.toJSONString(wd));
            wd.setPhoneStrBy008Datas();
            System.out.println(i+" phoneStr save-->"+wd.save());*/

            ///删除测试
           /* String wxid1 = wd.getWxId(),phone1 = wd.getPhone();
             if(i>99){
                 int cn1 = wd.delete();
                 System.out.println("cn1--->"+cn1);
             }*/
            //修改操作
            /* if(i>1088&&i<1119){
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

        phoneInfo.setDeviceId("8631840"+createNum(8));
        phoneInfo.setAndroidId("0d160be2"+createZmAndNum(8));
        phoneInfo.setLineNumber("579783111");
        phoneInfo.setSimSerialNumber("898600"+createNum(14));
        phoneInfo.setSubscriberId("4600232"+createNum(8));
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


        phoneInfo.setBuildId("6462"+createZmAndNum(8));
        phoneInfo.setDisplay("9c86"+createZmAndNum(8));

        String[] device = getRandmoDevice();
        phoneInfo.setBrand(device[0]);//SAMSUNG
        phoneInfo.setModel(device[0]+" "+device[1]);//SAMSUNG GT-S6812C
        phoneInfo.setProductName(device[1]);//GT-S6812C
        phoneInfo.setManufacturer(device[0]);//SAMSUNG
        phoneInfo.setDevice(device[0]+" "+device[1]);//SAMSUNG GT-S6812C
        phoneInfo.setHardware("qcom");//qcom
        phoneInfo.setFingerprint(device[2]);
        //smartisan/msm8916_32/msm8916_32:4.4.4/KTU84P:user/dev-keys
        phoneInfo.setSerialno("6f4b"+createNum(4));
        phoneInfo.setBlueAddress("cb:c3:b0:"+createZmAndNum(2)+":1a:"+createNum(2));


        String tags = createTags();
        phoneInfo.setBUILD_TAGS(tags);
        phoneInfo.setBUILD_TYPE(tags);
        phoneInfo.setBUILD_USER(tags);
        System.out.println("createPhoneInfo-->"+JSON.toJSONString(phoneInfo));
        return phoneInfo;
    }

    private static String[] getRandmoDevice(){
        List<String[]> devices = new ArrayList<String[]>();
        devices.add(new String[]{"SAMSUNG","GT-S6812C","smartisan/msm"+createNum(4)+"_32/msm"+createNum(4)+"_32:4.4.4/KTU84P:user/dev-keys"});
        devices.add(new String[]{"HTC","X720d","GiONEE/W900S/GiONEE_BFL7506A:5.1/LMY47D/"+createNum(10)+":user/release-keys"});
        devices.add(new String[]{"Coolpad","8730","nubia/NX"+createNum(3)+"J/NX"+createNum(3)+"J:5.0.2/LRX22G/eng.nubia.20151117.160521:user/release-keys"});
        devices.add(new String[]{"SONY","ST26i","Letv/Le1Pro_CN/x1:5.0.2/BBXCNOP"+createNum(10)+"S/128:user/release-keys"});
        devices.add(new String[]{"ZTE","N980","ZTE/N918St/N918St:4.4.4/KTU84P/eng."+createZmAndNum(5)+".20141120.203922:user/release-keys"});


        return devices.get((int)(Math.random() * devices.size()));
    }

    public static Wx008Data create008Data(String phone,String pwd,String cnNum){
        Wx008Data currentWx008Data = new Wx008Data();
        currentWx008Data.setGuid(AutoUtil.getUUID());
        currentWx008Data.setPhone(phone);
        currentWx008Data.setWxId(phone);
        currentWx008Data.setWxPwd(pwd);
        currentWx008Data.setCnNum(cnNum);
        currentWx008Data.setCreateTime(new Date());
        currentWx008Data.setDataType(PhoneSetting.getConfigSettingByKey("插入数据编号"));
        currentWx008Data.setPhoneStrs(JSON.toJSONString(createPhoneInfo()));
        System.out.println("create008Data-->"+JSON.toJSONString(currentWx008Data));
        return currentWx008Data;
    }

    private  static String createTags() {
        String str = "";
        for(int i=0;i<12;i++){
            str = str+getRandomAbc();
        }
        return str;

    }
    private  static String getRandomAbc() {
        String chars = "abcdef012365987";
        String str = chars.charAt((int)(Math.random() * 15))+"";
        return str;
    }
    private  static String createRandom123() {
        String chars = "0123456789";
        String str = chars.charAt((int)(Math.random() * 10))+"";
        return str;
    }
    private static String createZmAndNum(int num){
        String str = "";
        for(int i=0;i<num;i++){
            str = str+getRandomAbc();
        }
        return str;
    }
    private static String createNum(int num){
        String str = "";
        for(int i=0;i<num;i++){
            str = str+createRandom123();
        }
        return str;
    }
    public static List<String> getAddFrWx(){
        List<String> wxids = new ArrayList<>();
       /* wxids.add("ehg333");
        wxids.add("ehg999");*/
        /*wxids.add("w222qu");
        wxids.add("w234wr");*/
        /*wxids.add("w456mb");
        wxids.add("w444mb");*/
       /* wxids.add("w555mb");
        wxids.add("w666mb");*/
       /*wxids.add("w777mb");
        wxids.add("w888mb");*/
       /* wxids.add("w333mb");
        wxids.add("w222mb");*/

        /*wxids.add("w333wc");
        wxids.add("w444wc");*/
       /* wxids.add("w666wc");
        wxids.add("w777wc");*/
        /*wxids.add("w222wt");
        wxids.add("w333wt");*/
       /* wxids.add("w444wt");
        wxids.add("w555hb");*/
        String afs = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_AFS);
        if(afs.contains("\n")){
            String[] strs = afs.split("\n");
            for(String wx:strs){
                if(!TextUtils.isEmpty(wx.trim())){
                    wxids.add(wx);
                }
            }
        }
        Log.i("getAddFrWx-->",JSON.toJSONString(wxids));
        return wxids;
    }
}
