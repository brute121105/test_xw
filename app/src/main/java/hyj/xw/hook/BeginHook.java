package hyj.xw.hook;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import hyj.xw.model.PhoneInfo;

/**
 * Created by Administrator on 2018/1/4.
 */

public class BeginHook {
    public static void hk(XC_LoadPackage.LoadPackageParam lpparam, PhoneInfo phoneInfo){
        new XBuildHook(lpparam,phoneInfo);
        new XBuildHook2(lpparam,phoneInfo);
        new TechnologyHook(lpparam,phoneInfo);

    }
}
