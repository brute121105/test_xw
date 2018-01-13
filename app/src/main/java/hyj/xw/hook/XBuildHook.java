package hyj.xw.hook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import hyj.xw.model.PhoneInfo;

/**
 * Created by Administrator on 2018/1/4.
 */

public class XBuildHook extends MyMethodHook {
    public XBuildHook(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, PhoneInfo paramPhoneInfo)
    {
        super(paramLoadPackageParam, paramPhoneInfo);
        hookMethod("android.os.Build", "getString");
        //O000000o("android.os.Build", "getRadioVersion");
    }
    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam)
    {
        String str1 = paramMethodHookParam.method.getName();
       // while (true)
        //{
            super.afterHookedMethod(paramMethodHookParam);
            if ("getString".equals(str1))
            {
                String str2 = (String)paramMethodHookParam.args[0];
                if ("ro.product.model".equals(str2))
                {
                        paramMethodHookParam.setResult(this.phoneInfo.getModel());
                }
                else if ("ro.build.id".equals(str2))
                {
                        paramMethodHookParam.setResult(this.phoneInfo.getBuildId());
                }
                else if ("ro.build.display.id".equals(str2))
                {
                        paramMethodHookParam.setResult(this.phoneInfo.getDisplay());
                }

                else if ("ro.product.brand".equals(str2))
                {
                        paramMethodHookParam.setResult(this.phoneInfo.getBrand());
                }
                else if ("ro.product.device".equals(str2))
                {
                        paramMethodHookParam.setResult(this.phoneInfo.getDevice());
                }

                else if ("ro.product.name".equals(str2))
                {
                        paramMethodHookParam.setResult(this.phoneInfo.getProductName());
                }
                else if ("ro.product.manufacturer".equals(str2))
                {
                        paramMethodHookParam.setResult(this.phoneInfo.getManufacturer());
                }
                else if ("ro.hardware".equals(str2))
                {
                        paramMethodHookParam.setResult(this.phoneInfo.getHardware());
                }
                else if ("ro.serialno".equals(str2))
                {
                        paramMethodHookParam.setResult(this.phoneInfo.getSerialno());
                }

                else if ("ro.build.version.release".equals(str2))
                {
                        paramMethodHookParam.setResult(this.phoneInfo.getRelease());
                }
                else if ("ro.build.version.sdk".equals(str2))
                {
                        paramMethodHookParam.setResult(this.phoneInfo.getSdk());
                }

                else if ("ro.build.fingerprint".equals(str2))
                {
                        paramMethodHookParam.setResult(this.phoneInfo.getFingerprint());
                }

            }
        //}
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam)
    {
        try {
            super.beforeHookedMethod(paramMethodHookParam);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

}
