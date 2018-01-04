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
   public static PhoneInfo createPhoneInfo(){
       System.out.println("str-->" + str);
       List<Wx008Data> datas = JSON.parseArray(str, Wx008Data.class);
       System.out.println("datas-->"+JSON.toJSONString(datas));
       for(Wx008Data data:datas){
           data.setPhoneInfo(data.getDatas());
       }
       System.out.println("datas1-->"+JSON.toJSONString(datas));
      // PhoneInfo phoneInfo = wx008Data.getPhoneInfo();
       //Wx008Data wx008Data = datas.get(55);
       Wx008Data wx008Data = datas.get(50);
       System.out.println("wx008Data-->"+JSON.toJSONString(wx008Data));
       SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       System.out.println("teime-->"+simpleDateFormat.format(wx008Data.getCreateTime()));
       return wx008Data.getPhoneInfo();
   }
}
