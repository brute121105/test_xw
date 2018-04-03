package hyj.xw.hook.newHook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by asus on 2018/3/24.
 */

public class qgZSyjGOVNPGbBZt5NUv extends DHHdslt4SqYQ1hSj1a4Y
{
    public qgZSyjGOVNPGbBZt5NUv(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, NewPhoneInfo paramPhoneInfo)
    {
        super(paramLoadPackageParam, paramPhoneInfo);
        O000000o("android.os.BatteryManager", "getIntProperty");
    }

    @Override
    protected void afterHookedMethod(XC_MethodHook.MethodHookParam xc_MethodHook$MethodHookParam) throws Throwable {
        if ("getIntProperty".equals(xc_MethodHook$MethodHookParam.method.getName())) {
            final int intValue = (int)xc_MethodHook$MethodHookParam.args[0];
            if (intValue == 2) {
                xc_MethodHook$MethodHookParam.setResult((Object)(-306394));
            }
            else if (intValue == 4) {
                xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBatteryLevel());
            }
        }
        super.afterHookedMethod(xc_MethodHook$MethodHookParam);
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam) throws Throwable {
        super.beforeHookedMethod(paramMethodHookParam);
    }
}