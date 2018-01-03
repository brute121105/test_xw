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

        HookTelephony(TelePhone,loadPkgParam, "getDeviceId","868062024507544");  //序列号
        //android_id
        try {
            XposedHelpers.findAndHookMethod("android.provider.Settings.Secure", loadPkgParam.classLoader, "getString",ContentResolver.class, String.class, new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(MethodHookParam param)
                        throws Throwable {
                    if (param.args[1].equals(Settings.Secure.ANDROID_ID)) {
                        param.setResult("1c6ac00087a21cfc");
                    }
                }
            });
        } catch (Exception ex) {
            XposedBridge.log(" Android ID 错误: " + ex.getMessage());
        }
        HookTelephony(TelePhone,loadPkgParam, "getLine1Number","14782730591");//手机号码
        HookTelephony(TelePhone,loadPkgParam, "getSimSerialNumber","89860313540282921804");//手机卡序列号
        HookTelephony(TelePhone,loadPkgParam, "getSubscriberId","460031354028292");//IMSI

        HookTelephony(TelePhone,loadPkgParam, "getSimCountryIso","cn");  //手机卡国家
        HookTelephony(TelePhone,loadPkgParam, "getSimOperator","46003");//运营商
        HookTelephony(TelePhone,loadPkgParam, "getSimOperatorName","中国电信");//运营商名字
        HookTelephony(TelePhone,loadPkgParam, "getNetworkCountryIso","cn");//国家iso代码

        HookTelephony(TelePhone,loadPkgParam, "getNetworkOperator","46003");  //网络运营商类型
        HookTelephony(TelePhone,loadPkgParam, "getNetworkOperatorName","中国电信");//网络类型名
        HookTelephony(TelePhone,loadPkgParam, "getNetworkType",4);//网络类型
        HookTelephony(TelePhone,loadPkgParam, "getPhoneType",2);//手机类型
        HookTelephony(TelePhone,loadPkgParam, "getSimState",1);//手机卡状态

        //HookTelephony(TelePhone,loadPkgParam, "getSimState",11);//mac地址
        //HookTelephony(TelePhone,loadPkgParam, "getSimState",11);//无线路由器名称
        //HookTelephony(TelePhone,loadPkgParam, "getSimState",11);//无线路由器地址

        try {

            hookBuild("ro.build.version.release","5.0.2");//系统版本 6.0.1
            hookBuild("ro.build.version.sdk","21");//系统版本值
            hookBuild("ro.product.brand","Letv");;//品牌

            hookBuild("ro.product.model","X900+");//型号
            hookBuild("ro.build.id","74f8d389ecd5");//ID
            hookBuild("ro.build.display.id","c5cc02f77432");//display
            hookBuild("ro.product.name","乐视超级手机Max");//产品名
            hookBuild("ro.build.display.manufacturer","Letv");//制造商
            hookBuild("ro.build.display.device","max1");//设备名
            hookBuild("ro.hardware","qcom");//硬件
            hookBuild("ro.build.fingerprint","Letv/max1/max1:5.0.2/CEXCNFN5000408171S/22:user/release-keys");//指纹
            hookBuild("ro.serialno","ef7f6ce1");//串口序列号；


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
            XposedHelpers.setStaticObjectField(Build.VERSION.class, "RELEASE","5.0.2");
            XposedHelpers.setStaticObjectField(Build.VERSION.class, "SDK","21");
        XposedHelpers.setStaticObjectField(Build.class, "BRAND","Letv");
        XposedHelpers.setStaticObjectField(Build.class, "MODEL","X900+");
        XposedHelpers.setStaticObjectField(Build.class, "ID","74f8d389ecd5");
        XposedHelpers.setStaticObjectField(Build.class, "DISPLAY","c5cc02f77432");
            XposedHelpers.setStaticObjectField(Build.class, "PRODUCT","乐视超级手机Max");
            XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER","Letv");
            XposedHelpers.setStaticObjectField(Build.class, "DEVICE","max1");
        XposedHelpers.setStaticObjectField(Build.class, "HARDWARE","qcom");
            XposedHelpers.setStaticObjectField(Build.class, "FINGERPRINT","Letv/max1/max1:5.0.2/CEXCNFN5000408171S/22:user/release-keys");
            XposedHelpers.setStaticObjectField(Build.class, "SERIAL","ef7f6ce1");

        XposedHelpers.setStaticObjectField(Build.class, "BOOTLOADER", "unkown");

    }

}