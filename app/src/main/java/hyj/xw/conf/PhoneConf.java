package hyj.xw.conf;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.model.PhoneInfo;
import hyj.xw.util.FileUtil;

/**
 * Created by Administrator on 2018/1/4.
 */

public class PhoneConf {
    static  String str;
    static {
        File file = new File("/sdcard/A_hyj_008data");
        File[] files = file.listFiles();
        String imporFileName = files[files.length - 1].getPath();//获取最新文件名
        System.out.println("--->导入文件：" + imporFileName);

         str = FileUtil.readAll(imporFileName);
    }
   public static PhoneInfo createPhoneInfo(int index){
       System.out.println("str-->" + str);
       List<Wx008Data> datas = JSON.parseArray(str, Wx008Data.class);
       int i=0;
       for(Wx008Data data:datas){
           System.out.println("datas-->"+data.getCnNum()+" "+data.getPhone()+" "+i);
           i = i+1;
           data.setPhoneInfo(data.getDatas());
       }
       Wx008Data wx008Data = datas.get(index);
       SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       System.out.println("datas-->"+JSON.toJSONString(datas));
       System.out.println("teime-->"+simpleDateFormat.format(wx008Data.getCreateTime()));
       return wx008Data.getPhoneInfo();
   }
}
