package hyj.xw.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.JSON;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import hyj.xw.GlobalApplication;
import hyj.xw.hook.newHook.NewPhoneInfo;
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
           //System.out.println(tag+" getInstalledApplications--->"+ install.packageName);
       }

       try {
           String packageName = "com.tencent.mm";
           PackageInfo pInfo =  pm.getPackageInfo(packageName,PackageManager.GET_PERMISSIONS);//没有此包名会报错 hook钱替换传入参数
           System.out.println(tag+" getPackageInfo--->"+ JSON.toJSONString(pInfo));
           //ApplicationInfo apInfo = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
           //List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
           //System.out.println(tag+" getApplicationInfo--->"+ JSON.toJSONString(apInfo));
           //System.out.println(tag+" getInstalledPackages--->"+ JSON.toJSONString(installedPackages));
       } catch (PackageManager.NameNotFoundException e) {
           LogUtil.logError(e);
       }
   }

    public static void getPhoneInfo(){

        //getHideInfo();

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

        /*ConnectivityManager connectivity = (ConnectivityManager)GlobalApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        String subTypeName = connectivity.getActiveNetworkInfo().getSubtypeName();
        int subtype = connectivity.getActiveNetworkInfo().getSubtype();
        System.out.println(tag+"--->subTypeName->"+subTypeName);
        System.out.println(tag+"--->subtype->"+subtype);*/

    }

    // Mac地址
    private static String getLocalMac(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    public static NewPhoneInfo getEnvironmentAwData(){
        TelephonyManager phone = (TelephonyManager) GlobalApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        NewPhoneInfo npi = new NewPhoneInfo();
        npi.setBuildAbi(Build.CPU_ABI);
        npi.setBuildAbi2(Build.CPU_ABI2);
        npi.setBuildBoard(Build.BOARD);
        npi.setBuildBrand(Build.BRAND);
        npi.setBuildCodename(Build.VERSION.CODENAME);
        npi.setBuildDescription("");
        npi.setBuildDevice(Build.DEVICE);
        npi.setBuildFingerprint(Build.FINGERPRINT);
        npi.setBuildHardware(Build.HARDWARE);
        npi.setBuildHost(Build.HOST);
        npi.setBuildId(Build.ID);
        npi.setBuildIncremental(Build.VERSION.INCREMENTAL);
        npi.setBuildManufacturer(Build.MANUFACTURER);
        npi.setBuildModel(Build.MODEL);
        npi.setBuildName(Build.MANUFACTURER);//BuildName 字段对应Build.MANUFACTURER 或  Build.BRANDB，开始对应Build.PRODUCT 无法登陆
        npi.setBuildProduct(Build.PRODUCT);
        npi.setBuildRadioVersion(Build.getRadioVersion());
        npi.setBuildRelease(Build.VERSION.RELEASE);
        npi.setBuildSdk( Build.VERSION.SDK);
        npi.setSerialno(Build.SERIAL);
        npi.setBuildTags(Build.TAGS);
        npi.setType(1);//
        npi.setBuildUser(Build.USER);
        npi.setBuildUtc(1467439854767L);
        String androidId = Settings.Secure.getString(
                GlobalApplication.getResolver(), Settings.Secure.ANDROID_ID);//android_id
        String macAddress = getLocalMac(GlobalApplication.getContext());//mac地址
        WifiManager wm=(WifiManager)GlobalApplication.getContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wi=wm.getConnectionInfo();
        npi.setBSSID(wi.getBSSID());
        npi.setIpAddress("192.168.1.2");
        npi.setP2p0Mac(macAddress);
        npi.setSSID(wi.getSSID());


        npi.setAndroidId(androidId);
        npi.setBatteryLevel(60);
        npi.setBtAddress("");
        npi.setBtName("");

        npi.setCpuName(getCpuName());
        npi.setDensityDpi(480);

        npi.setDeviceId(phone.getDeviceId());
        npi.setDeviceSvn("29");
        npi.setDisplayId(Build.DISPLAY);//
        npi.setExtraInfo("");
        npi.setLine1Number(phone.getLine1Number());
        npi.setRgPhoneNo(phone.getLine1Number());
        npi.setRgWxPasswd("");
        npi.setMacAddress(macAddress);
        npi.setNetworkClass(phone.getNetworkType());
        npi.setNetworkCountryIso(phone.getNetworkCountryIso());
        npi.setNetworkOperator(phone.getNetworkOperator());
        npi.setNetworkOperatorName(phone.getNetworkOperatorName());
        npi.setNetworkType(phone.getNetworkType());
        npi.setNetworkTypeName("LTE");
        npi.setPhoneType(phone.getPhoneType());
        npi.setSerialno(Build.SERIAL);
        npi.setSimCountryIso(phone.getSimCountryIso());
        npi.setSimOperator(phone.getSimOperator());
        npi.setSimOperatorName(phone.getSimOperatorName());
        npi.setSimSerialNumber(phone.getSimSerialNumber());
        npi.setSimState(phone.getSimState());
        npi.setSubscriberId(phone.getSubscriberId());
        npi.setRgTime(new Date().getTime());

        System.out.println("getEnvironmentAwData--->"+JSON.toJSONString(npi));
        return npi;
    }

    public static String getCpuName(){
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr);
            while ((str2=localBufferedReader.readLine()) != null) {
                if (str2.contains("Hardware")) {
                    return str2.split(":")[1];
                }
            }
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return null;

    }
}
