package hyj.xw.conf;

import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;

/**
 * Created by asus on 2018/3/6.
 */

public class PhoneSetting {
    public static String getConfigSettingByKey(String key){
        String str = "";
        String settingStr = AppConfigDao.findAcountsStrByCode(CommonConstant.APPCONFIG_LOGIN_ACCOUNT);
        str = settingStr.substring(settingStr.indexOf(key));
        if(str.contains("\n")){
            str = str.substring(str.indexOf(":")+1,str.indexOf("\n")).trim();
        }else {
            str = str.substring(str.indexOf(":")+1).trim();
        }
        System.out.println(key+" getConfigSettingByKey-->"+str);
        return str;
    }
}
