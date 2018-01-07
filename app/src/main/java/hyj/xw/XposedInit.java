package hyj.xw;

import android.os.Bundle;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import hyj.xw.conf.PhoneConf;
import hyj.xw.hook.BeginHook;
import hyj.xw.hook.HideApp;
import hyj.xw.hook.Phone;
import hyj.xw.hook.TechnologyHook;
import hyj.xw.hook.XBuildHook;
import hyj.xw.hook.XBuildHook2;
import hyj.xw.model.PhoneInfo;

import static de.robv.android.xposed.XposedBridge.log;

/**
 * XposedInit
 *
 * @author wrbug
 * @since 2017/4/20
 */
public class XposedInit implements IXposedHookLoadPackage {
    private static String PACKAGE_NAME = "com.tencent.mm";

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        String packageName = lpparam.packageName;
        System.out.println("hyj xw hyj-->"+packageName);

        if("hyj.xw".equals(packageName)){
            new HideApp(lpparam);
            new Phone(lpparam);

        }

        if(packageName.equals(PACKAGE_NAME)){
            System.out.println("--->"+PACKAGE_NAME);
             new HideApp(lpparam);
             new Phone(lpparam);
             //new HideApp(lpparam);
        }


         /* XposedHelpers.findAndHookMethod("hyj.weixin_008.MainActivity", lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Class c=lpparam.classLoader.loadClass("hyj.weixin_008.MainActivity");
                        Field field=c.getDeclaredField("wxPwd");
                        field.setAccessible(true);
                        //param.thisObject 为执行该方法的对象，在这里指MainActivity
                        EditText textView= (EditText) field.get(param.thisObject);
                        System.out.println("hyj-->"+textView.getText());
                    }
                });*/
    }
}
