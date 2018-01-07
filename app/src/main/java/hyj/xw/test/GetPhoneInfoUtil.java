package hyj.xw.test;

import android.app.ActivityManager;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;

import hyj.xw.GlobalApplication;
import hyj.xw.util.LogUtil;

/**
 * Created by asus on 2018/1/6.
 */

public class GetPhoneInfoUtil {
    private static String tag = "GetPhoneInfoUtil";

   public static void getHideInfo(){
       PackageManager pm = GlobalApplication.getContext().getPackageManager();
       List<ApplicationInfo> installs = pm.getInstalledApplications(0);
       for(ApplicationInfo install :installs){
           System.out.println(tag+" getInstalledApplications--->"+ install.packageName);
       }

       try {
           String packageName = "de.robv.android.xposed.installer";
           PackageInfo pInfo =  pm.getPackageInfo(packageName,PackageManager.GET_PERMISSIONS);//没有此包名会报错 hook钱替换传入参数
           ApplicationInfo apInfo = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
           List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
           for(PackageInfo packageInfo :installedPackages){
               System.out.println(tag+" getInstalledPackages--->"+ packageInfo.packageName);
           }
           System.out.println(tag+" getPackageInfo--->"+ JSON.toJSONString(pInfo));
           System.out.println(tag+" getApplicationInfo--->"+ JSON.toJSONString(apInfo));
           ;
       } catch (PackageManager.NameNotFoundException e) {
           LogUtil.logError(e);
       }

       ActivityManager am = (ActivityManager)GlobalApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
       List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
       List<ActivityManager.RunningServiceInfo> runningServices =  am.getRunningServices(200);
       for(ActivityManager.RunningServiceInfo runningService : runningServices) {
           System.out.println(tag+" getRunningServices--->"+ JSON.toJSONString(runningService));
       }
       List<ActivityManager.RunningTaskInfo> runningTasks  = am.getRunningTasks(200);
       for(ActivityManager.RunningTaskInfo runningTask : runningTasks) {
           System.out.println(tag+" getRunningTasks--->"+ JSON.toJSONString(runningTask));
       };
       List<ActivityManager.RecentTaskInfo> recentTasks  =am.getRecentTasks(64, ActivityManager.RECENT_IGNORE_UNAVAILABLE);
       for(ActivityManager.RecentTaskInfo recentTask : recentTasks) {
           System.out.println(tag+" getRecentTasks--->"+ JSON.toJSONString(recentTask));
       };
       for(ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
           System.out.println(tag+" getRunningAppProcesses--->"+ JSON.toJSONString(processInfo));
       }
   }

    public static void getPhoneInfo(){
        TelephonyManager phone = (TelephonyManager) GlobalApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = phone.getDeviceId(); //序列号
        String tel = phone.getLine1Number();//手机号码
        String imei = phone.getSimSerialNumber();//手机号序列号
        String imsi = phone.getSubscriberId();//IMSI
        String getSimCountryIso = phone.getSimCountryIso();//手机卡国家
        String getSimOperator = phone.getSimOperator();//运营商
        String getSimOperatorName = phone.getSimOperatorName();//运营商名字
        String getNetworkCountryIso = phone.getNetworkCountryIso();//国家iso代码
        String getNetworkOperator = phone.getNetworkOperator();//网络运营商类型
        String getNetworkOperatorName = phone.getNetworkOperatorName();//网络类型名
        int getNetworkType = phone.getNetworkType();//网络类型
        int getPhoneType = phone.getPhoneType();//手机类型
        int getSimState = phone.getSimState();//手机卡状态
        //getMacAddress

        String systemVersion =  android.os.Build.VERSION.RELEASE;//系统版本 6.0.1
        int sdk =  Build.VERSION.SDK_INT;//系统版本值
        String sdk1 =  Build.VERSION.SDK;//系统版本值
        String getDeviceBrand = android.os.Build.BRAND;//品牌
        String getSystemModel =  android.os.Build.MODEL;//型号
        String ID = android.os.Build.ID;//ID
        String DISPLAY = android.os.Build.DISPLAY;
        String PRODUCT = Build.PRODUCT;//产品名
        String MANUFACTURER = Build.MANUFACTURER;//制造商
        String DEVICE = Build.DEVICE;//设备名
        String CPU_ABI = Build.CPU_ABI;//cpu型号 ---？
        String HARDWARE = Build.HARDWARE;//硬件
        String FINGERPRINT = Build.FINGERPRINT;//指纹
        String SERIAL = Build.SERIAL;//串口序列号；
        String RADIO = Build.RADIO;//蓝牙地址?

        String androidId = Settings.Secure.getString(
                GlobalApplication.getResolver(), Settings.Secure.ANDROID_ID);//android_id
        String macAddress = getLocalMac(GlobalApplication.getContext());//mac地址

        System.out.println(tag+"--->macAddress->"+macAddress);

        System.out.println(tag+"--->序列号 deviceId->"+deviceId);
        System.out.println(tag+"--->androidId->"+androidId);
        System.out.println(tag+"--->手机号码 tel->"+tel);
        System.out.println(tag+"--->手机号序列号 imei->"+imei);
        System.out.println(tag+"--->imsi->"+imsi);
        System.out.println(tag+"--->手机卡国家 getSimCountryIso->"+getSimCountryIso);
        System.out.println(tag+"--->运营商 getSimOperator->"+getSimOperator);
        System.out.println(tag+"--->运营商名字 getSimOperatorName->"+getSimOperatorName);
        System.out.println(tag+"--->国家iso代码 getNetworkCountryIso->"+getNetworkCountryIso);
        System.out.println(tag+"--->网络运营商类型 getNetworkOperator->"+getNetworkOperator);
        System.out.println(tag+"--->网络类型名 getNetworkOperatorName->"+getNetworkOperatorName);
        System.out.println(tag+"--->网络类型 getNetworkType->"+getNetworkType);
        System.out.println(tag+"--->手机类型 getPhoneType->"+getPhoneType);
        System.out.println(tag+"--->手机卡状态 getSimState->"+getSimState);

        System.out.println(tag+"--->系统版本 systemVersion->"+systemVersion);
        System.out.println(tag+"--->系统版本值 sdk->"+sdk +" "+sdk1);
        System.out.println(tag+"--->型号 getSystemModel->"+getSystemModel);
        System.out.println(tag+"--->品牌 getDeviceBrand->"+getDeviceBrand);
        System.out.println(tag+"--->ID->"+ID);
        System.out.println(tag+"--->DISPLAY->"+DISPLAY);
        System.out.println(tag+"--->产品名 PRODUCT->"+PRODUCT);
        System.out.println(tag+"--->制造商 MANUFACTURER->"+MANUFACTURER);
        System.out.println(tag+"--->设备名 DEVICE->"+DEVICE);
        System.out.println(tag+"--->硬件 HARDWARE->"+HARDWARE);
        System.out.println(tag+"--->指纹 FINGERPRINT->"+FINGERPRINT);
        System.out.println(tag+"--->串口序列号 SERIAL->"+SERIAL);

        System.out.println(tag+"--->Build.RADIO->"+Build.RADIO);
        System.out.println(tag+"--->Build.CPU_ABI->"+Build.CPU_ABI);//系统架构1
        System.out.println(tag+"--->Build.CPU_ABI2->"+Build.CPU_ABI2);//系统架构2
        System.out.println(tag+"--->Build.BOARD->"+Build.BOARD);
        System.out.println(tag+"--->Build.BOOTLOADER->"+Build.BOOTLOADER);
        System.out.println(tag+"--->Build.getRadioVersion->"+Build.getRadioVersion());//固定版本
        System.out.println(tag+"--->Build.TAGS->"+Build.TAGS);
        System.out.println(tag+"--->Build.TYPE->"+Build.TYPE);
        System.out.println(tag+"--->Build.USER->"+Build.USER);



        ConnectivityManager connectivity = (ConnectivityManager)GlobalApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        String subTypeName = connectivity.getActiveNetworkInfo().getSubtypeName();
        int subtype = connectivity.getActiveNetworkInfo().getSubtype();
        System.out.println(tag+"--->subTypeName->"+subTypeName);
        System.out.println(tag+"--->subtype->"+subtype);


        getWifiInfo();
        getHideInfo();
    }
   //WifiInfo暂不处理，NetworkInterface必须处理 NetworkInfo getBluetoothAddress
    public static void getWifiInfo(){
        WifiManager wifi = (WifiManager) GlobalApplication.getContext().getSystemService(Context.WIFI_SERVICE);
        System.out.println(tag+"--->getWifiState->"+wifi.getWifiState());
        System.out.println(tag+"--->isWifiEnabled->"+wifi.isWifiEnabled());
        System.out.println(tag+"--->getDhcpInfo->"+JSON.toJSONString(wifi.getDhcpInfo()));
        System.out.println(tag+"--->getScanResults->"+JSON.toJSONString(wifi.getScanResults()));
        WifiInfo info = wifi.getConnectionInfo();
        System.out.println(tag+"--->getBSSID->"+info.getBSSID());
        System.out.println(tag+"--->getHiddenSSID->"+info.getHiddenSSID());
        System.out.println(tag+"--->getIpAddress->"+info.getIpAddress());
        System.out.println(tag+"--->getFrequency->"+info.getFrequency());
        System.out.println(tag+"--->getLinkSpeed->"+info.getLinkSpeed());
        System.out.println(tag+"--->getMacAddress->"+info.getMacAddress());
        System.out.println(tag+"--->getNetworkId->"+info.getNetworkId());
        System.out.println(tag+"--->getRssi->"+info.getRssi());
        System.out.println(tag+"--->getSSID->"+info.getSSID());
        System.out.println(tag+"--->getSupplicantState->"+JSON.toJSONString(info.getSupplicantState()));


        try {
            String getLocalIpAddress = getLocalIpAddress();
            System.out.println(tag+"--->getHostAddress->"+getLocalIpAddress);
            NetworkInterface ne= NetworkInterface.getByInetAddress(InetAddress.getByName(getLocalIpAddress));
            byte[] mac = ne.getHardwareAddress();
            System.out.println(tag+"--->mac->"+mac);
            System.out.println(tag+"--->getName->"+ne.getName());
            System.out.println(tag+"--->getDisplayName->"+ne.getDisplayName());
            System.out.println(tag+"--->getHardwareAddress->"+byte2hex(ne.getHardwareAddress()));
            System.out.println(tag+"--->getInetAddresses->"+JSON.toJSONString(ne.getInetAddresses()));
            System.out.println(tag+"--->getInterfaceAddresses->"+JSON.toJSONString(ne.getInterfaceAddresses()));
            System.out.println(tag+"--->getParent->"+JSON.toJSONString(ne.getParent()));
            System.out.println(tag+"--->getSubInterfaces->"+JSON.toJSONString(ne.getSubInterfaces()));
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        ConnectivityManager mConnMgr = (ConnectivityManager) GlobalApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mMobile = mConnMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo activeNetworkInfo = mConnMgr.getActiveNetworkInfo();
        System.out.println(tag+"--->getDetailedState->"+JSON.toJSONString(activeNetworkInfo.getDetailedState()));
        System.out.println(tag+"--->getReason->"+activeNetworkInfo.getReason());
        System.out.println(tag+"--->getSubtypeName->"+activeNetworkInfo.getSubtypeName());
        System.out.println(tag+"--->getSubtype->"+activeNetworkInfo.getSubtype());
        System.out.println(tag+"--->getExtraInfo->"+activeNetworkInfo.getExtraInfo());
        System.out.println(tag+"--->getState->"+JSON.toJSONString(activeNetworkInfo.getState()));
        System.out.println(tag+"--->getType->"+activeNetworkInfo.getType());
        System.out.println(tag+"--->getTypeName->"+activeNetworkInfo.getTypeName());

        System.out.println(tag+"--->getBluetoothAddress->"+getBluetoothAddress());
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        System.out.println(tag+"--->getBluetoothAddress1->"+ba.getAddress());
        System.out.println(tag+"--->getBluetoothAddress getName->"+ba.getName());
        System.out.println(tag+"--->getBluetoothAddress getState->"+ba.getState());
        System.out.println(tag+"--->getBluetoothAddress getBondedDevices->"+JSON.toJSONString(ba.getBondedDevices()));
        System.out.println(tag+"--->getBluetoothAddress getScanMode->"+JSON.toJSONString(ba.getScanMode()));

        Set<BluetoothDevice> devices = ba.getBondedDevices();
        System.out.println(tag+"--->BluetoothDevice devices->"+devices);
        for (BluetoothDevice device : devices) {
            System.out.println(tag+"--->BluetoothDevice getName->"+device.getName() + " getAddress: " + device.getAddress());
        }



    }

    public static  String byte2hex(byte[] b) {
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs = hs.append("0").append(stmp);
            else {
                hs = hs.append(stmp);
            }
        }
        return String.valueOf(hs);
    }

    //获取本地IP
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
          ex.printStackTrace();
        }
        return null;
    }

    // Mac地址
    private static String getLocalMac(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public static String getBluetoothAddress() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Field field = bluetoothAdapter.getClass().getDeclaredField("mService");
            // 参数值为true，禁用访问控制检查
            field.setAccessible(true);
            Object bluetoothManagerService = field.get(bluetoothAdapter);
            if (bluetoothManagerService == null) {
                return null;
            }
            Method method = bluetoothManagerService.getClass().getMethod("getAddress");
            Object address = method.invoke(bluetoothManagerService);
            if (address != null && address instanceof String) {

                return (String) address;
            } else {
                return null;
            }

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
