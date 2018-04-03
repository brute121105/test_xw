package hyj.xw.hook.newHook;

import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Administrator on 2018/4/2.
 */

public class B3ZVrvfLQhtD1BGEtWg extends DHHdslt4SqYQ1hSj1a4Y
{

    public B3ZVrvfLQhtD1BGEtWg(XC_LoadPackage.LoadPackageParam xc_LoadPackage$LoadPackageParam, NewPhoneInfo phoneInfo) {
        super(xc_LoadPackage$LoadPackageParam, phoneInfo);
    if (phoneInfo.getHeight() == 0) {
        //final String[] split = \u6fb6.O000000o[new Random().nextInt(\u6fb6.O000000o.length)].split("&");
        phoneInfo.setHeight(1200);
        phoneInfo.setWidth(720);
    }
    this.O000000o(Display.class.getName(), "getMetrics", new Object[] { DisplayMetrics.class.getName() });
    this.O000000o(Display.class.getName(), "getWidth", new Object[0]);
    this.O000000o(Display.class.getName(), "getHeight", new Object[0]);
    this.O000000o(Resources.class.getName(), "getDisplayMetrics", new Object[0]);
}

    @Override
    protected void afterHookedMethod(XC_MethodHook.MethodHookParam xc_MethodHook$MethodHookParam) {
        final String name = xc_MethodHook$MethodHookParam.method.getName();
        if ("getWidth".equals(name)) {
            if (this.O00000o0.getWidth() > 0) {
                xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getWidth());
            }
        }
        else if ("getHeight".equals(name)) {
            if (this.O00000o0.getHeight() > 0) {
                xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getHeight());
            }
        }
        else if ("getDisplayMetrics".equals(name)) {
            if (this.O00000o0.getWidth() > 0 && this.O00000o0.getHeight() > 0) {
                final DisplayMetrics result = (DisplayMetrics)xc_MethodHook$MethodHookParam.getResult();
                result.heightPixels = this.O00000o0.getHeight();
                result.widthPixels = this.O00000o0.getWidth();
                result.densityDpi = this.O00000o0.getDensityDpi();
                xc_MethodHook$MethodHookParam.setResult((Object)result);
            }
        }
        else if ("getMetrics".equals(name)) {
            final Object o = xc_MethodHook$MethodHookParam.args[0];
            if (this.O00000o0.getWidth() > 0 && this.O00000o0.getHeight() > 0) {
                ((DisplayMetrics)o).widthPixels = this.O00000o0.getWidth();
                ((DisplayMetrics)o).heightPixels = this.O00000o0.getHeight();
                ((DisplayMetrics)o).densityDpi = this.O00000o0.getDensityDpi();
            }
        }
    }
}
