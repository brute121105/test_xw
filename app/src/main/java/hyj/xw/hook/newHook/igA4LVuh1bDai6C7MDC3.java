package hyj.xw.hook.newHook;

import android.text.TextUtils;

import java.io.File;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Administrator on 2018/4/3.
 */

public class igA4LVuh1bDai6C7MDC3 extends DHHdslt4SqYQ1hSj1a4Y
{
    public igA4LVuh1bDai6C7MDC3(XC_LoadPackage.LoadPackageParam paramLoadPackageParam,NewPhoneInfo paramPhoneInfo)
    {
        super(paramLoadPackageParam, paramPhoneInfo);
        O000000o("libcore.io.IoBridge", "open");
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam) throws Throwable {
        super.beforeHookedMethod(paramMethodHookParam);
        if ("open".equals(paramMethodHookParam.method.getName()))
        {
            //System.out.println("open-->2"+paramMethodHookParam.args[0]);
            String str = pU9KwKC2ppIRCaiFhaek.O000000o((String)paramMethodHookParam.args[0]);
            if (!TextUtils.isEmpty(str)){
                //System.out.println("open-->1"+str);
                paramMethodHookParam.args[0] = (PathFileUtil.str10 + File.separator + str);
            }
        }
    }
}