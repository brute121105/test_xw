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

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam)
    {
        int i;
        if ("getIntProperty".equals(paramMethodHookParam.method.getName()))
        {
            i = ((Integer)paramMethodHookParam.args[0]).intValue();
            if (i != 2){
                if (i == 4)
                    paramMethodHookParam.setResult(Integer.valueOf(this.O00000o0.getBatteryLevel()));
            }else {
                paramMethodHookParam.setResult(Integer.valueOf(-306394));
            }
        }

    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam) throws Throwable {
        super.beforeHookedMethod(paramMethodHookParam);
    }
}