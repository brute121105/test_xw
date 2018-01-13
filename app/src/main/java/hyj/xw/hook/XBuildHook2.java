package hyj.xw.hook;

import android.os.Build;
import android.provider.Settings;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import hyj.xw.model.PhoneInfo;

/**
 * Created by Administrator on 2018/1/4.
 */


public class XBuildHook2 extends MyMethodHook {
    public XBuildHook2(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, PhoneInfo paramPhoneInfo) {
        super(paramLoadPackageParam, paramPhoneInfo);
        hookMethod("android.os.SystemProperties", "set");
        hookMethod("android.os.SystemProperties", "get");
        hookMethod(Settings.System.class.getName(), "putStringForUser");
        hookMethod(Settings.Secure.class.getName(), "putStringForUser");
        hookMethod(Settings.Global.class.getName(), "putStringForUser");
        hookMethod(Settings.System.class.getName(), "getStringForUser");
        hookMethod(Settings.Secure.class.getName(), "getStringForUser");
        hookMethod(Settings.Global.class.getName(), "getStringForUser");
    }

    private void O000000o() {
        XposedHelpers.setStaticObjectField(Build.class, "BOOTLOADER", "unkown");
        XposedHelpers.setStaticObjectField(Build.class, "BRAND", this.phoneInfo.getBrand());
        XposedHelpers.setStaticObjectField(Build.class, "MODEL", this.phoneInfo.getModel());
        XposedHelpers.setStaticObjectField(Build.class, "DISPLAY", this.phoneInfo.getDisplay());
        XposedHelpers.setStaticObjectField(Build.class, "PRODUCT", this.phoneInfo.getProductName());
        XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER", this.phoneInfo.getManufacturer());
        XposedHelpers.setStaticObjectField(Build.class, "DEVICE", this.phoneInfo.getDevice());
        XposedHelpers.setStaticObjectField(Build.VERSION.class, "RELEASE", this.phoneInfo.getRelease());
        XposedHelpers.setStaticObjectField(Build.VERSION.class, "SDK", this.phoneInfo.getSdk());
        XposedHelpers.setStaticObjectField(Build.class, "FINGERPRINT", this.phoneInfo.getFingerprint());
        XposedHelpers.setStaticObjectField(Build.class, "HARDWARE", this.phoneInfo.getHardware());
        XposedHelpers.setStaticObjectField(Build.class, "ID", this.phoneInfo.getBuildId());
        XposedHelpers.setStaticObjectField(Build.class, "SERIAL", this.phoneInfo.getSerialno());
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam) {
        String str1 = paramMethodHookParam.method.getName();
        String str2="";
        if ("get".startsWith(str1)) {
            str2 = (String) paramMethodHookParam.args[0];
            if ("ro.product.model".equals(str2)) {
                paramMethodHookParam.setResult(this.phoneInfo.getModel());
            }
            if ("ro.build.id".equals(str2)) {
                paramMethodHookParam.setResult(this.phoneInfo.getBuildId());
            }
            if ("ro.build.display.id".equals(str2)) {
                paramMethodHookParam.setResult(this.phoneInfo.getDisplay());
            }
            if ("ro.product.brand".equals(str2)) {
                paramMethodHookParam.setResult(this.phoneInfo.getBrand());
            }
            if ("ro.product.device".equals(str2)) {
                paramMethodHookParam.setResult(this.phoneInfo.getDevice());
            }
            if ("ro.product.name".equals(str2)) {
                paramMethodHookParam.setResult(this.phoneInfo.getProductName());
            }
            if ("ro.product.manufacturer".equals(str2)) {
                paramMethodHookParam.setResult(this.phoneInfo.getManufacturer());
            }
            if ("ro.hardware".equals(str2)) {
                paramMethodHookParam.setResult(this.phoneInfo.getHardware());
            }
            if ("ro.serialno".equals(str2)) {
                paramMethodHookParam.setResult(this.phoneInfo.getSerialno());
            }
            if ("ro.build.version.release".equals(str2)) {
                paramMethodHookParam.setResult(this.phoneInfo.getRelease());
            }
            if ("ro.build.version.sdk".equals(str2)) {
                paramMethodHookParam.setResult(this.phoneInfo.getSdk());
            }

            if ("ro.build.fingerprint".equals(str2)) {
                paramMethodHookParam.setResult(this.phoneInfo.getFingerprint());
            }
            if ("getStringForUser".equals(str1)) {
                if ("android_id".equals(paramMethodHookParam.args[1]))
                    paramMethodHookParam.setResult(this.phoneInfo.getAndroidId());
                while (true) {
                    if ("adb_enabled".equals(paramMethodHookParam.args[1]))
                        paramMethodHookParam.setResult(Integer.valueOf(0));
                    else if ("data_roaming".equals(paramMethodHookParam.args[1]))
                        paramMethodHookParam.setResult(Integer.valueOf(0));
                }
            }
        }
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam) {
        try {
            super.beforeHookedMethod(paramMethodHookParam);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        O000000o();
    }
}
