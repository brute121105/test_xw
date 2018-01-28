package hyj.xw.conf;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        String imporFileName = files[files.length - 1].getPath();//获取最新文件名
        System.out.println("--->导入文件：" + imporFileName);

         str = FileUtil.readAll(imporFileName);
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
            //data.setPhoneInfo(data.getDatas());
            List<Wx008Data> getData = DataSupport.where("phone=?", data.getPhone()).find(Wx008Data.class);
            if (getData == null || getData.size() == 0) {
                if (data.save()) {
                    successCount = successCount + 1;
                }
            }
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
            if(wxid==null){
                wxid = wx008Datas.get(i).getPhone();
            }
            datas.add(i + "-" + wxid + " " + time + " " + (cn == null ? "86" : cn) + " \n" + lastLoginTime);
        }

        return datas.toArray(new String[datas.size()]);
    }
}
