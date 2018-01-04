package hyj.xw.hook;

import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;

import java.lang.reflect.Method;

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
        O000000o("android.os.SystemProperties", "set");
        O000000o("android.os.SystemProperties", "get");
        O000000o(Settings.System.class.getName(), "putStringForUser");
        O000000o(Settings.Secure.class.getName(), "putStringForUser");
        O000000o(Settings.Global.class.getName(), "putStringForUser");
        O000000o(Settings.System.class.getName(), "getStringForUser");
        O000000o(Settings.Secure.class.getName(), "getStringForUser");
        O000000o(Settings.Global.class.getName(), "getStringForUser");
    }

    private void O000000o() {
        XposedHelpers.setStaticObjectField(Build.class, "BOOTLOADER", "unkown");
        XposedHelpers.setStaticObjectField(Build.class, "BRAND", this.O00000o0.getBrand());
        XposedHelpers.setStaticObjectField(Build.class, "MODEL", this.O00000o0.getModel());
        XposedHelpers.setStaticObjectField(Build.class, "DISPLAY", this.O00000o0.getDisplay());
        XposedHelpers.setStaticObjectField(Build.class, "PRODUCT", this.O00000o0.getProductName());
        XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER", this.O00000o0.getManufacturer());
        XposedHelpers.setStaticObjectField(Build.class, "DEVICE", this.O00000o0.getDevice());
        XposedHelpers.setStaticObjectField(Build.VERSION.class, "RELEASE", this.O00000o0.getRelease());
        XposedHelpers.setStaticObjectField(Build.VERSION.class, "SDK", this.O00000o0.getSdk());
        XposedHelpers.setStaticObjectField(Build.class, "FINGERPRINT", this.O00000o0.getFingerprint());
        XposedHelpers.setStaticObjectField(Build.class, "HARDWARE", this.O00000o0.getHardware());
        XposedHelpers.setStaticObjectField(Build.class, "ID", this.O00000o0.getBuildId());
        XposedHelpers.setStaticObjectField(Build.class, "SERIAL", this.O00000o0.getSerialno());
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam) {
        String str1 = paramMethodHookParam.method.getName();
        String str2="";
        if ("get".startsWith(str1)) {
            str2 = (String) paramMethodHookParam.args[0];
            if ("ro.product.model".equals(str2)) {
                paramMethodHookParam.setResult(this.O00000o0.getModel());
            }
            if ("ro.build.id".equals(str2)) {
                paramMethodHookParam.setResult(this.O00000o0.getBuildId());
            }
            if ("ro.build.display.id".equals(str2)) {
                paramMethodHookParam.setResult(this.O00000o0.getDisplay());
            }
            if ("ro.product.brand".equals(str2)) {
                paramMethodHookParam.setResult(this.O00000o0.getBrand());
            }
            if ("ro.product.device".equals(str2)) {
                paramMethodHookParam.setResult(this.O00000o0.getDevice());
            }
            if ("ro.product.name".equals(str2)) {
                paramMethodHookParam.setResult(this.O00000o0.getProductName());
            }
            if ("ro.product.manufacturer".equals(str2)) {
                paramMethodHookParam.setResult(this.O00000o0.getManufacturer());
            }
            if ("ro.hardware".equals(str2)) {
                paramMethodHookParam.setResult(this.O00000o0.getHardware());
            }
            if ("ro.serialno".equals(str2)) {
                paramMethodHookParam.setResult(this.O00000o0.getSerialno());
            }
            if ("ro.build.version.release".equals(str2)) {
                paramMethodHookParam.setResult(this.O00000o0.getRelease());
            }
            if ("ro.build.version.sdk".equals(str2)) {
                paramMethodHookParam.setResult(this.O00000o0.getSdk());
            }

            if ("ro.build.fingerprint".equals(str2)) {
                paramMethodHookParam.setResult(this.O00000o0.getFingerprint());
            }
            if ("getStringForUser".equals(str1)) {
                if ("android_id".equals(paramMethodHookParam.args[1]))
                    paramMethodHookParam.setResult(this.O00000o0.getAndroidId());
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
