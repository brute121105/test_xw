package hyj.xw.hook.newHook;

import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by asus on 2018/3/24.
 */

public class iJzwYzrQSz7shvm8fphs extends DHHdslt4SqYQ1hSj1a4Y
{
    public iJzwYzrQSz7shvm8fphs(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, NewPhoneInfo paramPhoneInfo)
    {
        super(paramLoadPackageParam, paramPhoneInfo);
         //Log.i("hookaw fphs1", JSON.toJSONString(paramPhoneInfo));
        O000000o("android.os.SystemProperties", "set");
        O000000o("android.os.SystemProperties", "get");
        O000000o(Settings.System.class.getName(), "putStringForUser");
        O000000o(Settings.Secure.class.getName(), "putStringForUser");
        O000000o(Settings.Global.class.getName(), "putStringForUser");
        O000000o(Settings.System.class.getName(), "getStringForUser");
        O000000o(Settings.Secure.class.getName(), "getStringForUser");
        O000000o(Settings.Global.class.getName(), "getStringForUser");
    }

    private void O000000o()
    {
        XposedHelpers.setStaticObjectField(Build.class, "BOOTLOADER", "unkown");
        if (!TextUtils.isEmpty(this.O00000o0.getBuildBrand()))
            XposedHelpers.setStaticObjectField(Build.class, "BRAND", this.O00000o0.getBuildBrand());
        if (!TextUtils.isEmpty(this.O00000o0.getBuildBoard()))
            XposedHelpers.setStaticObjectField(Build.class, "BOARD", this.O00000o0.getBuildBoard());
        if (!TextUtils.isEmpty(this.O00000o0.getBuildModel()))
            XposedHelpers.setStaticObjectField(Build.class, "MODEL", this.O00000o0.getBuildModel());
        if (!TextUtils.isEmpty(this.O00000o0.getDisplayId()))
                XposedHelpers.setStaticObjectField(Build.class, "DISPLAY", this.O00000o0.getDisplayId());
        if (!TextUtils.isEmpty(this.O00000o0.getBuildProduct()))
            XposedHelpers.setStaticObjectField(Build.class, "PRODUCT", this.O00000o0.getBuildProduct());
        if (!TextUtils.isEmpty(this.O00000o0.getBuildManufacturer()))
            XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER", this.O00000o0.getBuildManufacturer());
        if (!TextUtils.isEmpty(this.O00000o0.getBuildDevice()))
            XposedHelpers.setStaticObjectField(Build.class, "DEVICE", this.O00000o0.getBuildDevice());
        if (!TextUtils.isEmpty(this.O00000o0.getBuildRelease()))
            XposedHelpers.setStaticObjectField(Build.VERSION.class, "RELEASE", this.O00000o0.getBuildRelease());
        if (!TextUtils.isEmpty(this.O00000o0.getBuildSdk()))
            XposedHelpers.setStaticObjectField(Build.VERSION.class, "SDK", this.O00000o0.getBuildSdk());
        if (!TextUtils.isEmpty(this.O00000o0.getBuildFingerprint()))
            XposedHelpers.setStaticObjectField(Build.class, "FINGERPRINT", this.O00000o0.getBuildFingerprint());
        if (!TextUtils.isEmpty(this.O00000o0.getBuildHardware()))
            XposedHelpers.setStaticObjectField(Build.class, "HARDWARE", this.O00000o0.getBuildHardware());
        if (!TextUtils.isEmpty(this.O00000o0.getBuildCodename()))
            XposedHelpers.setStaticObjectField(Build.VERSION.class, "CODENAME", this.O00000o0.getBuildCodename());
        if (!TextUtils.isEmpty(this.O00000o0.getBuildIncremental()))
            XposedHelpers.setStaticObjectField(Build.VERSION.class, "INCREMENTAL", this.O00000o0.getBuildIncremental());
        if (!TextUtils.isEmpty(this.O00000o0.getBuildHost()))
            XposedHelpers.setStaticObjectField(Build.class, "HOST", this.O00000o0.getBuildHost());
        if (!TextUtils.isEmpty(this.O00000o0.getBuildId()))
            XposedHelpers.setStaticObjectField(Build.class, "ID", this.O00000o0.getBuildId());
        if (!TextUtils.isEmpty(this.O00000o0.getBuildType()))
            XposedHelpers.setStaticObjectField(Build.class, "TYPE", this.O00000o0.getBuildType());
        if (!TextUtils.isEmpty(this.O00000o0.getBuildUser()))
            XposedHelpers.setStaticObjectField(Build.class, "USER", this.O00000o0.getBuildUser());
        if (!TextUtils.isEmpty(this.O00000o0.getBuildSerialno()))
            XposedHelpers.setStaticObjectField(Build.class, "SERIAL", this.O00000o0.getBuildSerialno());
        if (!TextUtils.isEmpty(this.O00000o0.getBuildTags()))
            XposedHelpers.setStaticObjectField(Build.class, "TAGS", this.O00000o0.getBuildTags());
        if (this.O00000o0.getBuildUtc() > 0L)
            XposedHelpers.setStaticObjectField(Build.class, "TIME", Long.valueOf(this.O00000o0.getBuildUtc()));
        if (!TextUtils.isEmpty(this.O00000o0.getBuildAbi()))
            XposedHelpers.setStaticObjectField(Build.class, "CPU_ABI", this.O00000o0.getBuildAbi());
        if (!TextUtils.isEmpty(this.O00000o0.getBuildAbi2()))
            XposedHelpers.setStaticObjectField(Build.class, "CPU_ABI2", this.O00000o0.getBuildAbi2());
    }
    @Override
    protected void afterHookedMethod(final XC_MethodHook.MethodHookParam xc_MethodHook$MethodHookParam) throws Throwable {
        final String name = xc_MethodHook$MethodHookParam.method.getName();
        if ("get".startsWith(name)) {
            final String s = (String)xc_MethodHook$MethodHookParam.args[0];
            if ("ro.product.model".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildModel())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildModel());
                }
            }
            else if ("ro.build.id".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildId())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildId());
                }
            }
            else if ("ro.build.display.id".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getDisplayId())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getDisplayId());
                }
            }
            else if ("ro.build.type".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildType())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildType());
                }
            }
            else if ("ro.build.user".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildUser())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildUser());
                }
            }
            else if ("ro.build.host".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildHost())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildHost());
                }
            }
            else if ("ro.build.tags".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildTags())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildTags());
                }
            }
            else if ("ro.product.brand".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildTags())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildBrand());
                }
            }
            else if ("ro.product.device".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildDevice())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildDevice());
                }
            }
            else if ("ro.product.board".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildBoard())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildBoard());
                }
            }
            else if ("ro.product.name".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildName())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildName());
                }
            }
            else if ("ro.product.manufacturer".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildManufacturer())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildManufacturer());
                }
            }
            else if ("ro.hardware".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildHardware())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildHardware());
                }
            }
            else if ("ro.serialno".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildSerialno())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildSerialno());
                }
            }
            else if ("ro.build.version.incremental".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildIncremental())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildIncremental());
                }
            }
            else if ("ro.build.version.release".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildRelease())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildRelease());
                }
            }
            else if ("ro.build.version.sdk".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildSdk())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildSdk());
                }
            }
            else if ("ro.build.version.codename".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildCodename())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildCodename());
                }
            }
            else if ("ro.build.fingerprint".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildFingerprint())) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildFingerprint());
                }
            }
            else if ("ro.build.date.utc".equals(s)) {
                if (this.O00000o0.getBuildUtc() > 0L) {
                    xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBuildUtc());
                }
            }
            else if ("gsm.operator.numeric".equals(s) && !TextUtils.isEmpty((CharSequence)this.O00000o0.getSimOperator())) {
                xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getSimOperator());
            }
            Log.i("SysPropertyHook", "SystemProperties get : " + xc_MethodHook$MethodHookParam.args[0] + " -> " + xc_MethodHook$MethodHookParam.getResult());
        }
        else if ("getStringForUser".equals(name)) {
            if ("android_id".equals(xc_MethodHook$MethodHookParam.args[1])) {
                xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getAndroidId());
            }
            else if ("adb_enabled".equals(xc_MethodHook$MethodHookParam.args[1])) {
                xc_MethodHook$MethodHookParam.setResult((Object)0);
            }
            else if ("data_roaming".equals(xc_MethodHook$MethodHookParam.args[1])) {
                xc_MethodHook$MethodHookParam.setResult((Object)0);
            }
            Log.i("SysPropertyHook", "Settings getStringForUser : " + xc_MethodHook$MethodHookParam.args[1] + " , " + xc_MethodHook$MethodHookParam.getResult());
        }
        else if ("set".equals(name)) {
            Log.i("SysPropertyHook", "SystemProperties set : " + xc_MethodHook$MethodHookParam.args[0] + " , " + xc_MethodHook$MethodHookParam.args[1]);
        }
        super.afterHookedMethod(xc_MethodHook$MethodHookParam);
    }
    @Override
   protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam) throws Throwable {
        super.beforeHookedMethod(paramMethodHookParam);
        String str1 = paramMethodHookParam.method.getName();
        Class localClass = paramMethodHookParam.method.getDeclaringClass();
        if ("putStringForUser".equals(str1)) {
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = ContentResolver.class;
            arrayOfClass[1] = String.class;
            arrayOfClass[2] = Integer.TYPE;
            Method localMethod = localClass.getDeclaredMethod("getStringForUser", arrayOfClass);
            Object[] arrayOfObject = new Object[3];
            arrayOfObject[0] = paramMethodHookParam.args[0];
            arrayOfObject[1] = paramMethodHookParam.args[1];
            arrayOfObject[2] = paramMethodHookParam.args[3];
            String str2 = (String) localMethod.invoke(localClass, arrayOfObject);
            String str3 = localClass.getSimpleName() + "," + paramMethodHookParam.args[1] + "," + paramMethodHookParam.args[2] + "," + str2;
            Log.i("SysPropertyHook"," Settings putStringForUser : "+str3);
        }else if("get".equals(str1)){
            O000000o();
        }
    }
    }
