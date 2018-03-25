package hyj.xw.hook.newHook;

import android.content.ContentResolver;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by asus on 2018/3/24.
 */

public class iJzwYzrQSz7shvm8fphs extends DHHdslt4SqYQ1hSj1a4Y
{
    public iJzwYzrQSz7shvm8fphs(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, PhoneInfo paramPhoneInfo)
    {
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

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam)
    {
        String str1 = paramMethodHookParam.method.getName();
        String str2;
        if ("get".startsWith(str1))
        {
            str2 = (String)paramMethodHookParam.args[0];
            if ("ro.product.model".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildModel()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildModel());
            }

        if ("ro.build.id".equals(str2))
        {
            if (TextUtils.isEmpty(this.O00000o0.getBuildId()))
             paramMethodHookParam.setResult(this.O00000o0.getBuildId());
        }
        if ("ro.build.display.id".equals(str2))
        {
            if (TextUtils.isEmpty(this.O00000o0.getDisplayId()))
            paramMethodHookParam.setResult(this.O00000o0.getDisplayId());
        }
        if ("ro.build.type".equals(str2))
        {
            if (TextUtils.isEmpty(this.O00000o0.getBuildType()))
            paramMethodHookParam.setResult(this.O00000o0.getBuildType());
        }
        if ("ro.build.user".equals(str2))
        {
            if (TextUtils.isEmpty(this.O00000o0.getBuildUser()))
            paramMethodHookParam.setResult(this.O00000o0.getBuildUser());
        }
        if ("ro.build.host".equals(str2))
        {
            if (TextUtils.isEmpty(this.O00000o0.getBuildHost()))
            paramMethodHookParam.setResult(this.O00000o0.getBuildHost());
        }
        if ("ro.build.tags".equals(str2))
        {
            if (TextUtils.isEmpty(this.O00000o0.getBuildTags()))
            paramMethodHookParam.setResult(this.O00000o0.getBuildTags());
        }
        if ("ro.product.brand".equals(str2))
        {
            if (TextUtils.isEmpty(this.O00000o0.getBuildTags()))
            paramMethodHookParam.setResult(this.O00000o0.getBuildBrand());
        }
        if ("ro.product.device".equals(str2))
        {
            if (TextUtils.isEmpty(this.O00000o0.getBuildDevice()))
            paramMethodHookParam.setResult(this.O00000o0.getBuildDevice());
        }
        if ("ro.product.board".equals(str2))
        {
            if (TextUtils.isEmpty(this.O00000o0.getBuildBoard()))
            paramMethodHookParam.setResult(this.O00000o0.getBuildBoard());
        }
        if ("ro.product.name".equals(str2))
        {
            if (TextUtils.isEmpty(this.O00000o0.getBuildName()))
            paramMethodHookParam.setResult(this.O00000o0.getBuildName());
        }
        if ("ro.product.manufacturer".equals(str2))
        {
            if (TextUtils.isEmpty(this.O00000o0.getBuildManufacturer()))
            paramMethodHookParam.setResult(this.O00000o0.getBuildManufacturer());
        }
        if ("ro.hardware".equals(str2))
        {
            if (TextUtils.isEmpty(this.O00000o0.getBuildHardware()))
             paramMethodHookParam.setResult(this.O00000o0.getBuildHardware());
        }
        if ("ro.serialno".equals(str2))
        {
            if (TextUtils.isEmpty(this.O00000o0.getBuildSerialno()))
             paramMethodHookParam.setResult(this.O00000o0.getBuildSerialno());
        }
        if ("ro.build.version.incremental".equals(str2))
        {
            if (TextUtils.isEmpty(this.O00000o0.getBuildIncremental()))
               paramMethodHookParam.setResult(this.O00000o0.getBuildIncremental());
        }
        if ("ro.build.version.release".equals(str2))
        {
            if (TextUtils.isEmpty(this.O00000o0.getBuildRelease()))
                paramMethodHookParam.setResult(this.O00000o0.getBuildRelease());
        }
        if ("ro.build.version.sdk".equals(str2))
        {
            if (TextUtils.isEmpty(this.O00000o0.getBuildSdk()))
                paramMethodHookParam.setResult(this.O00000o0.getBuildSdk());
        }
        if ("ro.build.version.codename".equals(str2))
        {
            if (TextUtils.isEmpty(this.O00000o0.getBuildCodename()))
                paramMethodHookParam.setResult(this.O00000o0.getBuildCodename());
        }
        if ("ro.build.fingerprint".equals(str2))
        {
            if (TextUtils.isEmpty(this.O00000o0.getBuildFingerprint()))
                paramMethodHookParam.setResult(this.O00000o0.getBuildFingerprint());
        }
        if ((!"ro.build.date.utc".equals(str2)) || (this.O00000o0.getBuildUtc() <= 0L))
            paramMethodHookParam.setResult(Long.valueOf(this.O00000o0.getBuildUtc()));
        if ("getStringForUser".equals(str1))
        {
            if ("android_id".equals(paramMethodHookParam.args[1]))
                paramMethodHookParam.setResult(this.O00000o0.getAndroidId());

            if ("adb_enabled".equals(paramMethodHookParam.args[1]))
                paramMethodHookParam.setResult(Integer.valueOf(0));
            else if ("data_roaming".equals(paramMethodHookParam.args[1]))
                paramMethodHookParam.setResult(Integer.valueOf(0));
        }
        }
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam) throws Throwable {
        super.beforeHookedMethod(paramMethodHookParam);
        Class[] arrayOfClass;
        Method localMethod;
        Object[] arrayOfObject;
        String str2;
        String str3;


        String str1 = paramMethodHookParam.method.getName();
        Class localClass = paramMethodHookParam.method.getDeclaringClass();
        if ("putStringForUser".equals(str1)) {
            arrayOfClass = new Class[3];
            arrayOfClass[0] = ContentResolver.class;
            arrayOfClass[1] = String.class;
            arrayOfClass[2] = Integer.TYPE;
            localMethod = localClass.getDeclaredMethod("getStringForUser", arrayOfClass);
            arrayOfObject = new Object[3];
            arrayOfObject[0] = paramMethodHookParam.args[0];
            arrayOfObject[1] = paramMethodHookParam.args[1];
            arrayOfObject[2] = paramMethodHookParam.args[3];
            str2 = (String) localMethod.invoke(localClass, arrayOfObject);
            str3 = localClass.getSimpleName() + "," + paramMethodHookParam.args[1] + "," + paramMethodHookParam.args[2] + "," + str2;
        }

        O000000o();
    }
    }
