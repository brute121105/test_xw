package hyj.xw.test;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.JSON;

import java.util.List;

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
           String packageName = "com.tencent.mm";
           PackageInfo pInfo =  pm.getPackageInfo(packageName,PackageManager.GET_PERMISSIONS);//没有此包名会报错 hook钱替换传入参数
           ApplicationInfo apInfo = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
           List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
           System.out.println(tag+" getPackageInfo--->"+ JSON.toJSONString(pInfo));
           System.out.println(tag+" getApplicationInfo--->"+ JSON.toJSONString(apInfo));
           System.out.println(tag+" getInstalledPackages--->"+ JSON.toJSONString(installedPackages));
       } catch (PackageManager.NameNotFoundException e) {
           LogUtil.logError(e);
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

        ConnectivityManager connectivity = (ConnectivityManager)GlobalApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        String subTypeName = connectivity.getActiveNetworkInfo().getSubtypeName();
        int subtype = connectivity.getActiveNetworkInfo().getSubtype();
        System.out.println(tag+"--->subTypeName->"+subTypeName);
        System.out.println(tag+"--->subtype->"+subtype);

        getHideInfo();
    }

    // Mac地址
    private static String getLocalMac(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }
}
