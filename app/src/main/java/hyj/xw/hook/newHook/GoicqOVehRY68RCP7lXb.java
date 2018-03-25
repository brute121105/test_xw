package hyj.xw.hook.newHook;

import android.text.TextUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by asus on 2018/3/24.
 */

public class GoicqOVehRY68RCP7lXb extends DHHdslt4SqYQ1hSj1a4Y {
    public GoicqOVehRY68RCP7lXb(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, PhoneInfo paramPhoneInfo) {
        super(paramLoadPackageParam, paramPhoneInfo);
        O000000o("android.os.Build", "getString");
        O000000o("android.os.Build", "getRadioVersion");
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam) throws Throwable {
        super.afterHookedMethod(paramMethodHookParam);
        String str1 = paramMethodHookParam.method.getName();
        if ("getRadioVersion".equals(str1))
            if (!TextUtils.isEmpty(this.O00000o0.getBuildRadioVersion()))
                paramMethodHookParam.setResult(this.O00000o0.getBuildRadioVersion());

        if ("getString".equals(str1))
        {
            String str2 = (String)paramMethodHookParam.args[0];
            if ("ro.product.model".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildModel()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildModel());
            }
            else if ("ro.build.id".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildId()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildId());
            }
            else if ("ro.build.display.id".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getDisplayId()))
                    paramMethodHookParam.setResult(this.O00000o0.getDisplayId());
            }
            else if ("ro.build.type".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildType()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildType());
            }
            else if ("ro.build.user".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildUser()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildUser());
            }
            else if ("ro.build.host".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildHost()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildHost());
            }
            else if ("ro.build.tags".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildTags()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildTags());
            }
            else if ("ro.product.brand".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildTags()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildBrand());
            }
            else if ("ro.product.device".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildDevice()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildDevice());
            }
            else if ("ro.product.board".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildBoard()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildBoard());
            }
            else if ("ro.product.name".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildName()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildName());
            }
            else if ("ro.product.manufacturer".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildManufacturer()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildManufacturer());
            }
            else if ("ro.hardware".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildHardware()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildHardware());
            }
            else if ("ro.serialno".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildSerialno()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildSerialno());
            }
            else if ("ro.build.version.incremental".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildIncremental()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildIncremental());
            }
            else if ("ro.build.version.release".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildRelease()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildRelease());
            }
            else if ("ro.build.version.sdk".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildSdk()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildSdk());
            }
            else if ("ro.build.version.codename".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildCodename()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildCodename());
            }
            else if ("ro.build.fingerprint".equals(str2))
            {
                if (!TextUtils.isEmpty(this.O00000o0.getBuildFingerprint()))
                    paramMethodHookParam.setResult(this.O00000o0.getBuildFingerprint());
            }
            else if (("ro.build.date.utc".equals(str2)) && (this.O00000o0.getBuildUtc() > 0L))
                paramMethodHookParam.setResult(Long.valueOf(this.O00000o0.getBuildUtc()));
        }
    }
    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam) throws Throwable {
        super.beforeHookedMethod(paramMethodHookParam);
    }
}
