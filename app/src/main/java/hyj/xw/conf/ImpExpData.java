package hyj.xw.conf;

import com.alibaba.fastjson.JSON;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hyj.xw.common.FilePathCommon;
import hyj.xw.hook.newHook.NewPhoneInfo;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.service.HttpRequestService;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.FileUtil;
import hyj.xw.util.OkHttpUtil;

/**
 * Created by Administrator on 2018/4/10.
 */

public class ImpExpData {
    public static Map<String,Object> importAData()  {
        int countAll=0,countExist=0,countSucc=0;
        Map<String,Object> result = new HashMap<String,Object>();
        System.out.println("importAData-->0");
        File file = new File(FilePathCommon.importDataAPath);
        if(file.exists()){
            File[] files = file.listFiles();
            if(file!=null&&files.length>0){
                countAll = files.length;
                for(File f:files){
                    String json = FileUtil.readAllUtf8(FilePathCommon.importDataAPath+f.getName());
                    System.out.println("ImpExpData json-->"+json);
                    NewPhoneInfo npi = JSON.parseObject(json,NewPhoneInfo.class);
                    Wx008Data wx008Data = createImportAwData(npi);
                    List<Wx008Data> getData = DataSupport.where("phone=?", wx008Data.getPhone()).find(Wx008Data.class);
                    if(getData!=null&&getData.size()>0){//判断已存在不导入
                        countExist = countExist+1;
                    }else {
                        if(wx008Data.save()){//导入成功
                            countSucc = countSucc+1;
                        }
                    }
                }
            }
        }
        result.put("countAll",countAll);
        result.put("countSucc",countSucc);
        result.put("countExist",countExist);
        System.out.println("importAData-->result:"+result);
        return result;
    }
    public static Wx008Data createImportAwData(NewPhoneInfo npi){
        Wx008Data currentWx008Data = new Wx008Data();
        currentWx008Data.setGuid(AutoUtil.getUUID());
        currentWx008Data.setPhone(npi.getRgPhoneNo());
        currentWx008Data.setWxid19(npi.getRgWxId());
        currentWx008Data.setWxPwd(npi.getRgWxPasswd());
        currentWx008Data.setCnNum(npi.getRgAreaode());
        currentWx008Data.setNickName(npi.getRgWxName());
        currentWx008Data.setCreateTime(new Date(npi.getRgTime()));
        currentWx008Data.setDataFlag("009");//009代表Aw数据
        currentWx008Data.setRgWxStatus(npi.getRgWxStatus());
        currentWx008Data.setPhoneStrsAw(JSON.toJSONString(npi));
        System.out.println("create008Data-->"+JSON.toJSONString(currentWx008Data));
        return currentWx008Data;
    }
    public static Map<String,Object> import008DataAdd2()  {
        int countAll=0,countExist=0,countSucc=0;
        Map<String,Object> result = new HashMap<String,Object>();
        System.out.println("importAData-->0");
        File file = new File(FilePathCommon.sl008DataPath);
        if(file.exists()){
            File[] files = file.listFiles();
            if(file!=null&&files.length>0){
                countAll = files.length;
                for(File f:files){
                    String json = FileUtil.readAll(FilePathCommon.sl008DataPath+f.getName());
                    String phone =f.getName().substring(0,11);
                    String pwd = f.getName().substring(f.getName().indexOf(",")+1,f.getName().indexOf("."));
                    System.out.println("sl008Data-->"+json);
                    System.out.println("sl008Data-->phone:"+phone+" pwd:"+pwd);
                    Wx008Data wx008Data = PhoneConf.createNew008DataStr(phone,pwd,"86","010",json);
                    System.out.println("sl008Data-->"+JSON.toJSONString(wx008Data));
                    List<Wx008Data> getData = DataSupport.where("phone=?", wx008Data.getPhone()).find(Wx008Data.class);
                    if(getData!=null&&getData.size()>0){//判断已存在不导入
                        countExist = countExist+1;
                    }else {
                        if(wx008Data.save()){//导入成功
                            countSucc = countSucc+1;
                        }
                    }
                }
            }
        }
        result.put("countAll",countAll);
        result.put("countSucc",countSucc);
        result.put("countExist",countExist);
        System.out.println("importAData-->result:"+result);
        return result;
    }
    /*public static Map<String,Object> import008DataAdd()  {
        int countAll=0,countExist=0,countSucc=0;
        Map<String,Object> result = new HashMap<String,Object>();
        System.out.println("importAData-->0");
        File file = new File(FilePathCommon.sl008DataPath);
        if(file.exists()){
            File[] files = file.listFiles();
            if(file!=null&&files.length>0){
                countAll = files.length;
                Map<String,String> pwds = getSl008Pwd();
                for(File f:files){
                    String json = FileUtil.readAllUtf8(FilePathCommon.sl008DataPath+f.getName());
                    NewPhoneInfo npi = PhoneConf.createPhoneDataFromSl008(json);
                    System.out.println("npi-->"+JSON.toJSONString(npi));
                    Wx008Data wx008Data = PhoneConf.createNew008Data(npi.getLine1Number(),pwds.get(npi.getLine1Number()),"86","010",JSON.toJSONString(npi));
                    System.out.println("sl008Data-->"+JSON.toJSONString(wx008Data));
                    List<Wx008Data> getData = DataSupport.where("phone=?", wx008Data.getPhone()).find(Wx008Data.class);
                    if(getData!=null&&getData.size()>0){//判断已存在不导入
                        countExist = countExist+1;
                    }else {
                        if(wx008Data.save()){//导入成功
                            countSucc = countSucc+1;
                        }
                    }
                }
            }
        }
        result.put("countAll",countAll);
        result.put("countSucc",countSucc);
        result.put("countExist",countExist);
        System.out.println("importAData-->result:"+result);
        return result;
    }*/
    public static Map<String,Object> import008Data()  {
        int countAll=0,countExist=0,countSucc=0;
        Map<String,Object> result = new HashMap<String,Object>();
        System.out.println("importAData-->0");
        File file = new File(FilePathCommon.sl008DataPath);
        if(file.exists()){
            File[] files = file.listFiles();
            if(file!=null&&files.length>0){
                countAll = files.length;
                for(File f:files){
                    String json = FileUtil.readAllUtf8(FilePathCommon.sl008DataPath+f.getName());
                    System.out.println("import008Data json:"+json);
                    String phone = f.getName().substring(0,11);
                    System.out.println("import008Data phone:"+phone);
                    Wx008Data wx008Data = new Wx008Data();
                    wx008Data.setPhoneStrs(json);
                    int cn = wx008Data.updateAll("phone=?",phone);
                    System.out.println("import008Data cn:"+cn);
                }
            }
        }
        result.put("countAll",countAll);
        result.put("countSucc",countSucc);
        result.put("countExist",countExist);
        System.out.println("importAData-->result:"+result);
        return result;
    }
    private static Map<String,String> getSl008Pwd(){
        Map<String,String> result = new HashMap<String,String>();
        List<String> pwds = FileUtil.read008Data(FilePathCommon.sl008DataPwdPath);
        for(String pwd:pwds){
            if(pwd.contains(",")){
                String[] arr = pwd.split(",");
                if(arr.length==2){
                    result.put(arr[0].trim(),arr[1].trim());
                }
            }
        }
        return result;
    }

    //获取008账号密码
    public static void getPhonePwd008(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> pwds = FileUtil.read008Data(FilePathCommon.phonePwd008Txt);
                HttpRequestService service = new HttpRequestService(3);
                int i = 0;
                for(String pwd:pwds){
                    ++i;
                    if(i<467) continue;
                    String[] arr = pwd.split(",");
                    String phone = arr[0],password = arr[1];
                    String url = "http://43.226.47.2/getphone.php?tel="+phone;
                    System.out.println(i+" doAction----phone:"+phone+" password:"+password+" url:"+url);
                    try {
                        String phoneStr008 = OkHttpUtil.okHttpGet(url);
                        if(phoneStr008.contains(phone)){
                            System.out.println(i+" doAction 008 数据获取成功res-->"+phoneStr008);
                            Wx008Data wx008Data = PhoneConf.createReg008DataDeviceTxt2("",phone,password,"86",phoneStr008);
                            String uploadRes  = service.uploadPhoneData(JSON.toJSONString(wx008Data));
                            System.out.println(i+" doAction-->上传数据："+uploadRes);
                        }else {
                            System.out.println(i+" doAction 008 数据获取失败res-->"+phoneStr008);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
