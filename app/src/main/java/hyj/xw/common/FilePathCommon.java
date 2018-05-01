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
    static {
        baseAppPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/azy";
        baseAppPathAW= Environment.getExternalStorageDirectory().getAbsolutePath()+"/.money/";
        npiFileName = "PhoneInfo.aw";
        importDataAPath=baseAppPath+"/dataA/";
        dataBakPath=baseAppPath+"/dataBF/";
        phoneTagPath=baseAppPath+"/hk/phoneTag.txt";
    }
}
