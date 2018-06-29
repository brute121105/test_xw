package hyj.xw.common;

import android.os.Environment;

/**
 * Created by Administrator on 2018/4/10.
 */

public class FilePathCommon {

    public static final String baseAppPath;
    public static final String baseAppPathAW;
    public static final String npiFileName;
    public static final String importDataAPath;
    public static final String dataBakPath;
    public static final String phoneTagPath;
    public static final String importData008Path;
    public static final String sl008DataPath;
    public static final String sl008DataPwdPath;
    public static final String fkFilePath;
    public static final String fkScreenShotPath;
    static {
        baseAppPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/azy";
        baseAppPathAW= Environment.getExternalStorageDirectory().getAbsolutePath()+"/.money/";
        npiFileName = "PhoneInfo.aw";
        importDataAPath=baseAppPath+"/dataA/";
        dataBakPath=baseAppPath+"/dataBF/";
        importData008Path=baseAppPath+"/data008/";
        sl008DataPath=baseAppPath+"/data008/下载机型/";
        sl008DataPwdPath=baseAppPath+"/data008/pwd.txt";
        phoneTagPath=baseAppPath+"/hk/phoneTag.txt";
        fkFilePath = baseAppPath+"/fangkuai.txt";
        fkScreenShotPath = baseAppPath+"/fangkuai.png";
    }
}
