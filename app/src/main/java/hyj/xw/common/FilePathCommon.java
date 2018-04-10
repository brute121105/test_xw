package hyj.xw.common;

import android.os.Environment;

/**
 * Created by Administrator on 2018/4/10.
 */

public class FilePathCommon {

    public static final String baseAppPath;
    public static final String importDataAPath;
    public static final String phoneTagPath;
    static {
        baseAppPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/azy";
        importDataAPath=baseAppPath+"/dataA";
        phoneTagPath=baseAppPath+"/hk/phoneTag.txt";
    }
}
