package hyj.autooperation.common;

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
    public static final String setEnviromentFilePath;//next登录下一个，retry新登录,首次开启也是retry,done写入完成
    public static final String wx008DataFilePath;
    public static final String startRunninConfigTxtPath;
    public static final String stopTxtPath;
    public static final String logErrorPath;
    public static final String fkPngPath;
    public static final String device008TxtPath;
    public static final String loginMaintainResultTxt;

    static {
        baseAppPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/azy";
        baseAppPathAW= Environment.getExternalStorageDirectory().getAbsolutePath()+"/.money/";
        npiFileName = "PhoneInfo.aw";
        importDataAPath=baseAppPath+"/dataA/";
        dataBakPath=baseAppPath+"/dataBF/";
        logErrorPath=baseAppPath+"/error/";
        fkPngPath=baseAppPath+"/fkPng";
        importData008Path=baseAppPath+"/data008/";
        sl008DataPath=baseAppPath+"/data008/下载机型/";
        sl008DataPwdPath=baseAppPath+"/data008/pwd.txt";
        phoneTagPath=baseAppPath+"/hk/phoneTag.txt";
        fkFilePath = baseAppPath+"/fangkuai.txt";
        fkScreenShotPath = baseAppPath+"/fangkuai.png";
        setEnviromentFilePath = baseAppPath+"/enviroment.txt";
        wx008DataFilePath = baseAppPath+"/wx008Data.txt";
        startRunninConfigTxtPath = baseAppPath+"/startRunningConfig.txt";
        loginMaintainResultTxt = baseAppPath+"/loginMaintainResult.txt";
        stopTxtPath = baseAppPath+"/stop.txt";
        device008TxtPath = "/sdcard/.system/xposeDevice.txt";
    }
}
