package hyj.xw.conf;

import com.alibaba.fastjson.JSON;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hyj.xw.common.FilePathCommon;
import hyj.xw.hook.newHook.NewPhoneInfo;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.FileUtil;

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
}
