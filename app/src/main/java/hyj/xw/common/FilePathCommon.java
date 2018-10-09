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
    public static final String setEnviromentFilePath;
    public static final String wx008DataFilePath;
    public static final String startRunninConfigTxtPath;
    public static final String device008TxtPath;
    public static final String stopTxtPath;
    public static final String logPath;
    public static final String downAPk1Path;
    public static final String downAPk2Path;
    public static final String serverConfigTxtPath;
    public static final String loginMaintainResultTxt;
    public static final String refreshTimeTxt;
    public static final String bslStrTxt;
    public static final String phonePwd008Txt;//008账号密码txt文件
    public static final String phonePwd008TxtFail;//008账号密码txt文件
    public static final String data008Path;//008账号密码txt文件

    public static final String sendAccessLogDir;
    static {
        baseAppPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/azy";
        baseAppPathAW= Environment.getExternalStorageDirectory().getAbsolutePath()+"/.money/";
        npiFileName = "PhoneInfo.aw";
        importDataAPath=baseAppPath+"/dataA/";
        dataBakPath=baseAppPath+"/dataBF/";
        importData008Path=baseAppPath+"/data008/";
        sl008DataPath=baseAppPath+"/data008/下载机型/";
        data008Path=baseAppPath+"/data008";
        sl008DataPwdPath=baseAppPath+"/data008/pwd.txt";
        phonePwd008Txt=baseAppPath+"/data008/phonePwd.txt";
        phonePwd008TxtFail=baseAppPath+"/data008/上传失败.txt";
        phoneTagPath=baseAppPath+"/hk/phoneTag.txt";
        fkFilePath = baseAppPath+"/fangkuai.txt";
        fkScreenShotPath = baseAppPath+"/fangkuai.png";
        stopTxtPath = baseAppPath+"/stop.txt";
        setEnviromentFilePath = baseAppPath+"/enviroment.txt";
        wx008DataFilePath = baseAppPath+"/wx008Data.txt";
        startRunninConfigTxtPath = baseAppPath+"/startRunningConfig.txt";
        loginMaintainResultTxt = baseAppPath+"/loginMaintainResult.txt";
        refreshTimeTxt = baseAppPath+"/refreshTime.txt";
        device008TxtPath = "/sdcard/.system/xposeDevice.txt";
        logPath = baseAppPath+"/log/";
        downAPk1Path = "/sdcard/azyfile";
        downAPk2Path = "/sdcard/hyj.autooperation.test";
        serverConfigTxtPath = "/sdcard/serverConfig.txt";
        bslStrTxt = baseAppPath+"/bslStrTxt.txt";

        sendAccessLogDir = baseAppPath+"/sendAccessMsgLogs/";
    }
}
