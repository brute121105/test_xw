package hyj.xw;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import hyj.xw.hook.Phone;
import hyj.xw.hook.util.HookWxUtil;

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
            new Phone(lpparam);
        }

       /* if(packageName.equals(PACKAGE_NAME)){
            System.out.println("--->"+PACKAGE_NAME);
             new Phone(lpparam);

        }*/

        if(packageName.equals(PACKAGE_NAME)){
            HookWxUtil.hoodPyq(lpparam);
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
