package hyj.xw.hook.newHook;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2018/4/3.
 */

public class PathFileUtil {


       public static final String str15 = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String str10 =str15 + "/.money";
    public static final String str13 =str10 + File.separator + "backup";
    public static final String str11 =str10 + File.separator + "qrCodes";
    public static final String str5 =str10 + File.separator + "headPic";
    public static final String str1 =str15 + "/download";
    public static final String str4 =str1 + "/predownload";
    public static final String str14 =str10 + "/record";
    public static final String str6 =str10 + "/vpnlines";
    public static final String str7 =str10 + "/emails";
    public static final String str12 =str10 + "/wxasqr.jpg";
    public static final String str2 =str10 + "/wxqr.jpg";
    public static final String str3 =str10 + "/ERROR/";
    public static final String str9 =str10 + "/PhoneInfo.aw";
    public static final String str8 =str10 + "/ComInfo.aw";
}
