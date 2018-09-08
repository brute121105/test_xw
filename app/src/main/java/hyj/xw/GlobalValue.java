package hyj.xw;

import hyj.xw.modelHttp.Device;

/**
 * Created by Administrator on 2018/8/28 0028.
 */

public class GlobalValue {
    public static String ip = "";
    public static Long data008Id = null;
    public static String data008Friends = "";
    public static String data008Phone = "";
    //public static int deviceRunType;
    //public static int deviceHookType;
    //public static String deviceNum = "";
    public static Long uiautoReveiverRefreshTime;
    public static boolean isHaveNewAttach= false;//是否有uiautomator脚本下载到本地

    public static Device device = null;

    public static Device getDeviceAndSomeParams(){
        device.setChangeIp(1);
        return device;
    }
}
