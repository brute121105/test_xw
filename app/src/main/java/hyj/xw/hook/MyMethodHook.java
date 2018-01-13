package hyj.xw.hook;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import hyj.xw.model.PhoneInfo;
import hyj.xw.util.LogUtil;

/**
 * Created by Administrator on 2018/1/4.
 */


public abstract class MyMethodHook extends XC_MethodHook {
    private static String tag = "MyMethodHook";
    protected String packageName;
    protected ClassLoader classLoader;
    protected PhoneInfo phoneInfo;

    public MyMethodHook(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, PhoneInfo paramPhoneInfo) {
        this.packageName = paramLoadPackageParam.packageName;
        this.classLoader = paramLoadPackageParam.classLoader;
        this.phoneInfo = paramPhoneInfo;
    }

    //用方法名，不需要知道方法里的参数
    public void hookMethod(String paramString1, String paramString2) {
        try {
            for (Method localMethod : XposedHelpers.findClass(paramString1, this.classLoader).getDeclaredMethods()){
                System.out.println("hookMethod methodName -->"+localMethod.getName());
                if ((localMethod.getName().equals(paramString2)) && (!Modifier.isAbstract(localMethod.getModifiers()))) {
                    localMethod.setAccessible(true);
                    XposedBridge.hookMethod(localMethod, this);
                }
            }
        } catch (Throwable localThrowable) {
            LogUtil.d(tag," hookMethod excepton clsName:"+paramString1+"  methodName:"+paramString2);
        }
    }

    //用方法名和参数，需要知道方法里的参数
    public void hoodMethodByParms(String paramString1, String paramString2, Object[] paramArrayOfObject) {
        try {
            Object[] arrayOfObject = new Object[1 + paramArrayOfObject.length];
            for (int i = 0; i < arrayOfObject.length; i++) {
                if (i == -1 + arrayOfObject.length) {
                    arrayOfObject[(-1 + arrayOfObject.length)] = this;
                    XposedHelpers.findAndHookMethod(paramString1, this.classLoader, paramString2, arrayOfObject);
                    return;
                }
                arrayOfObject[i] = paramArrayOfObject[i];
            }
        } catch (Throwable localThrowable) {
            LogUtil.d(tag," hoodMethodByParms excepton clsName:"+paramString1+"  methodName:"+paramString1);
        }
    }

    public void hookConstructorByParms(String paramString, Object[] paramArrayOfObject) {
        try {
            Object[] arrayOfObject = new Object[1 + paramArrayOfObject.length];
            for (int i = 0; i < arrayOfObject.length; i++) {
                if (i == -1 + arrayOfObject.length) {
                    arrayOfObject[(-1 + arrayOfObject.length)] = this;
                    XposedHelpers.findAndHookConstructor(paramString, this.classLoader, arrayOfObject);
                    return;
                }
                arrayOfObject[i] = paramArrayOfObject[i];
            }
        } catch (Throwable localThrowable) {
            // FVKjWjKo1YaG6p5uD2qz.O00000Oo("Xhook", "addHookConWithParms Exception " + paramString);
        }
    }

    public void hookConstructor(String paramString) {
        try {
            for (Constructor localConstructor : XposedHelpers.findClass(paramString, this.classLoader).getDeclaredConstructors())
                if (Modifier.isPublic(localConstructor.getModifiers()))
                    XposedBridge.hookMethod(localConstructor, this);
        } catch (Throwable localThrowable) {
            //FVKjWjKo1YaG6p5uD2qz.O000000o("Xhook", "addHookConOnly exception", localThrowable);
        }
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam) {

    }
}

