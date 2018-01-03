package hyj.xw.hook;

import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;


import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class Phone {
    public Phone(XC_LoadPackage.LoadPackageParam sharePkgParam) {
        Telephony(sharePkgParam);
        hookBuild();
    }



    public void Telephony(XC_LoadPackage.LoadPackageParam loadPkgParam) {

        String TelePhone = "android.telephony.TelephonyManager";

        HookTelephony(TelePhone,loadPkgParam, "getDeviceId","357750051534005");  //序列号
        //android_id
        try {
            XposedHelpers.findAndHookMethod("android.provider.Settings.Secure", loadPkgParam.classLoader, "getString",ContentResolver.class, String.class, new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(MethodHookParam param)
                        throws Throwable {
                    if (param.args[1].equals(Settings.Secure.ANDROID_ID)) {
                        param.setResult("f417a89e15156b68");
                    }
                }
            });
        } catch (Exception ex) {
            XposedBridge.log(" Android ID 错误: " + ex.getMessage());
        }
        HookTelephony(TelePhone,loadPkgParam, "getLine1Number","8970365698");//手机号码
        HookTelephony(TelePhone,loadPkgParam, "getSimSerialNumber","89860199166464098467");//手机卡序列号
        HookTelephony(TelePhone,loadPkgParam, "getSubscriberId","460019916646409");//IMSI

        HookTelephony(TelePhone,loadPkgParam, "getSimCountryIso","cn");  //手机卡国家
        HookTelephony(TelePhone,loadPkgParam, "getSimOperator","46001");//运营商
        HookTelephony(TelePhone,loadPkgParam, "getSimOperatorName","中国联通");//运营商名字
        HookTelephony(TelePhone,loadPkgParam, "getNetworkCountryIso","cn");//国家iso代码

        HookTelephony(TelePhone,loadPkgParam, "getNetworkOperator","46001");  //网络运营商类型
        HookTelephony(TelePhone,loadPkgParam, "getNetworkOperatorName","中国联通");//网络类型名
        HookTelephony(TelePhone,loadPkgParam, "getNetworkType",3);//网络类型
        HookTelephony(TelePhone,loadPkgParam, "getPhoneType",1);//手机类型
        HookTelephony(TelePhone,loadPkgParam, "getSimState",1);//手机卡状态

        //HookTelephony(TelePhone,loadPkgParam, "getSimState",11);//mac地址
        //HookTelephony(TelePhone,loadPkgParam, "getSimState",11);//无线路由器名称
        //HookTelephony(TelePhone,loadPkgParam, "getSimState",11);//无线路由器地址

        try {
           /* XposedHelpers.findField(Build.VERSION.class, "RELEASE").set(null,"6.0");//系统版本 6.0.1
            XposedHelpers.findField(Build.VERSION.class, "SDK").set(null,"23");//系统版本值
            XposedHelpers.findField(Build.VERSION.class, "SDK_INT").set(null,"23");

            //XposedHelpers.findField(Build.class, "BRAND").set(null,"111c1b07d4ca");//系统架构
            //XposedHelpers.findField(Build.class, "BRAND").set(null,"111c1b07d4ca");//屏幕分辨率
            //XposedHelpers.findField(Build.class, "BRAND").set(null,"111c1b07d4ca");//固件版本
            XposedHelpers.findField(Build.class, "BRAND").set(null,"SAMSUNG");//品牌
            XposedHelpers.findField(Build.class, "MODEL").set(null,"SAMSUNG GT-I9205");//型号
            XposedHelpers.findField(Build.class, "ID").set(null,"e3a9a0b04644");//ID
            XposedHelpers.findField(Build.class, "DISPLAY").set(null,"d372c88edfdf");//DISPLAY;
            XposedHelpers.findField(Build.class, "PRODUCT").set(null,"GT-I9205");//产品名
            XposedHelpers.findField(Build.class, "MANUFACTURER").set(null,"SAMSUNG");//制造商
            XposedHelpers.findField(Build.class, "DEVICE").set(null,"SAMSUNG GT-I9205");//设备名
            //XposedHelpers.findField(Build.class, "DEVICE").set(null,"111c1b07d4ca");//cpu型号 ---？
            XposedHelpers.findField(Build.class, "HARDWARE").set(null,"qcom");//硬件
            XposedHelpers.findField(Build.class, "FINGERPRINT").set(null,"Xiaomi/kenzo/kenzo:5.1.1/LMY47V/V7.1.9.0.LHOCNCL:user/release-keys");//指纹
            XposedHelpers.findField(Build.class, "SERIAL").set(null,"1ff12731");//串口序列号；
            //XposedHelpers.findField(Build.class, "SERIAL").set(null,"111c1b07d4ca");//蓝牙地址*/

            hookBuild("ro.build.version.release","6.0");//系统版本 6.0.1
            hookBuild("ro.build.version.sdk","23");//系统版本值
            hookBuild("ro.product.brand","SAMSUNG");;//品牌

            hookBuild("ro.product.model","SAMSUNG GT-I9205");//型号
            hookBuild("ro.build.id","e3a9a0b04644");//ID
            hookBuild("ro.build.display.id","d372c88edfdf");//display
            hookBuild("ro.product.name","GT-I9205");//产品名
            hookBuild("ro.build.display.manufacturer","SAMSUNG");//制造商
            hookBuild("ro.build.display.device","AMSUNG GT-I9205");//设备名
            hookBuild("ro.hardware","qcom");//硬件
            hookBuild("ro.build.fingerprint","Xiaomi/kenzo/kenzo:5.1.1/LMY47V/V7.1.9.0.LHOCNCL:user/release-keys");//指纹
            hookBuild("ro.serialno","1ff12731");//串口序列号；


        }catch (Exception e){
            XposedBridge.log(" BuilProp 错误: " + e.getMessage());
        }


    }

    private void HookTelephony(String hookClass, XC_LoadPackage.LoadPackageParam loadPkgParam,
                               String funcName, final String value) {
        try {
            XposedHelpers.findAndHookMethod(hookClass,
                    loadPkgParam.classLoader, funcName, new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.afterHookedMethod(param);
                            System.out.println("--->hook String phone");
                            param.setResult(value);
                        }

                    });
        } catch (Exception e) {

        }
    }
    private void HookTelephony(String hookClass, XC_LoadPackage.LoadPackageParam loadPkgParam,
                               String funcName, final int value) {
        try {
            XposedHelpers.findAndHookMethod(hookClass,
                    loadPkgParam.classLoader, funcName, new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.afterHookedMethod(param);
                            System.out.println("--->hook int phone");
                            param.setResult(value);
                        }

                    });
        } catch (Exception e) {

        }
    }

    private void hookBuild(final String params1 ,final String value){

        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            if(cls != null){
                for (Member mem : cls.getDeclaredMethods()) {
                    XposedBridge.hookMethod(mem, new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(MethodHookParam param)
                                throws Throwable {
                            super.beforeHookedMethod(param);
                            if (param.args.length > 0 && param.args[0] != null && param.args[0].equals(params1)) {
                                System.out.println("--->hook build "+value);
                                param.setResult(value);
                            }
                        }
                    });
                }
            }

        } catch (ClassNotFoundException e) {
            XposedBridge.log(" DESCRIPTION 错误: " + e.getMessage());
        }

    }

    private void hookBuild()
    {
        XposedHelpers.setStaticObjectField(Build.class, "BOOTLOADER", "unkown");
            XposedHelpers.setStaticObjectField(Build.class, "BRAND","SAMSUNG");
            XposedHelpers.setStaticObjectField(Build.class, "MODEL","SAMSUNG GT-I9205");
            XposedHelpers.setStaticObjectField(Build.class, "DISPLAY","d372c88edfdf");
            XposedHelpers.setStaticObjectField(Build.class, "PRODUCT","GT-I9205");
            XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER","SAMSUNG");
            XposedHelpers.setStaticObjectField(Build.class, "DEVICE","111c1b07d4ca");
            XposedHelpers.setStaticObjectField(Build.VERSION.class, "RELEASE","6.0");
            XposedHelpers.setStaticObjectField(Build.VERSION.class, "SDK","23");
            XposedHelpers.setStaticObjectField(Build.class, "FINGERPRINT","Xiaomi/kenzo/kenzo:5.1.1/LMY47V/V7.1.9.0.LHOCNCL:user/release-keys");
            XposedHelpers.setStaticObjectField(Build.class, "HARDWARE","qcom");
            XposedHelpers.setStaticObjectField(Build.class, "ID","e3a9a0b04644");

    }

}