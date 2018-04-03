package hyj.xw.hook.newHook;

import android.text.TextUtils;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by asus on 2018/3/24.
 */

public class GoicqOVehRY68RCP7lXb extends DHHdslt4SqYQ1hSj1a4Y {
    public GoicqOVehRY68RCP7lXb(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, NewPhoneInfo paramPhoneInfo) {
        super(paramLoadPackageParam, paramPhoneInfo);
        O000000o("android.os.Build", "getString");
        O000000o("android.os.Build", "getRadioVersion");
    }
    @Override
    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam) throws Throwable {
        final String name = paramMethodHookParam.method.getName();
        if ("getRadioVersion".equals(name)) {
            if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildRadioVersion())) {
                paramMethodHookParam.setResult((Object)this.O00000o0.getBuildRadioVersion());
            }
        }
        else if ("getString".equals(name)) {
            final String s = (String)paramMethodHookParam.args[0];
            if ("ro.product.model".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildModel())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getBuildModel());
                }
            }
            else if ("ro.build.id".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildId())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getBuildId());
                }
            }
            else if ("ro.build.display.id".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getDisplayId())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getDisplayId());
                }
            }
            else if ("ro.build.type".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildType())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getBuildType());
                }
            }
            else if ("ro.build.user".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildUser())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getBuildUser());
                }
            }
            else if ("ro.build.host".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildHost())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getBuildHost());
                }
            }
            else if ("ro.build.tags".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildTags())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getBuildTags());
                }
            }
            else if ("ro.product.brand".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildTags())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getBuildBrand());
                }
            }
            else if ("ro.product.device".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildDevice())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getBuildDevice());
                }
            }
            else if ("ro.product.board".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildBoard())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getBuildBoard());
                }
            }
            else if ("ro.product.name".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildName())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getBuildName());
                }
            }
            else if ("ro.product.manufacturer".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildManufacturer())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getBuildManufacturer());
                }
            }
            else if ("ro.hardware".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildHardware())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getBuildHardware());
                }
            }
            else if ("ro.serialno".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildSerialno())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getBuildSerialno());
                }
            }
            else if ("ro.build.version.incremental".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildIncremental())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getBuildIncremental());
                }
            }
            else if ("ro.build.version.release".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildRelease())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getBuildRelease());
                }
            }
            else if ("ro.build.version.sdk".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildSdk())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getBuildSdk());
                }
            }
            else if ("ro.build.version.codename".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildCodename())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getBuildCodename());
                }
            }
            else if ("ro.build.fingerprint".equals(s)) {
                if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildFingerprint())) {
                    paramMethodHookParam.setResult((Object)this.O00000o0.getBuildFingerprint());
                }
            }
            else if ("ro.build.date.utc".equals(s) && this.O00000o0.getBuildUtc() > 0L) {
                paramMethodHookParam.setResult((Object)this.O00000o0.getBuildUtc());
            }
        }
        super.afterHookedMethod(paramMethodHookParam);
    }

    @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
    }

}
