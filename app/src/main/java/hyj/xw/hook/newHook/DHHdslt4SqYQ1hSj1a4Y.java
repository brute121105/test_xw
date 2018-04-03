package hyj.xw.hook.newHook;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by asus on 2018/3/24.
 */

public abstract class DHHdslt4SqYQ1hSj1a4Y extends XC_MethodHook {
        protected String O000000o;
        protected ClassLoader O00000Oo;
        protected NewPhoneInfo O00000o0;

        public DHHdslt4SqYQ1hSj1a4Y(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, NewPhoneInfo paramPhoneInfo)
        {
            this.O000000o = paramLoadPackageParam.packageName;
            this.O00000Oo = paramLoadPackageParam.classLoader;
            this.O00000o0 = paramPhoneInfo;
        }

    @Override
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
    }

    public void O000000o(String paramString1, String paramString2)
    {
        try
        {
            for (Method localMethod : XposedHelpers.findClass(paramString1, this.O00000Oo).getDeclaredMethods())
                if ((localMethod.getName().equals(paramString2)) && (!Modifier.isAbstract(localMethod.getModifiers())))
                {
                    localMethod.setAccessible(true);
                    XposedBridge.hookMethod(localMethod, this);
                }
        }
        catch (Throwable localThrowable)
        {
            Log.e("Xhook", "addHookWithOnlyMethodName Exception " + paramString1);
        }
    }

    public void O000000o(String paramString1, String paramString2, Object[] paramArrayOfObject)
    {
        try
        {
            Object[] arrayOfObject = new Object[1 + paramArrayOfObject.length];
            for (int i = 0; i < arrayOfObject.length; i++)
            {
                if (i == -1 + arrayOfObject.length)
                {
                    arrayOfObject[(-1 + arrayOfObject.length)] = this;
                    XposedHelpers.findAndHookMethod(paramString1, this.O00000Oo, paramString2, arrayOfObject);
                    return;
                }
                arrayOfObject[i] = paramArrayOfObject[i];
            }
        }
        catch (Throwable localThrowable)
        {
            Log.e("Xhook", "addHookMethodWithParms Exception " + paramString1);
        }
    }

    public void O000000o(String paramString, Object[] paramArrayOfObject)
    {
        try
        {
            Object[] arrayOfObject = new Object[1 + paramArrayOfObject.length];
            for (int i = 0; i < arrayOfObject.length; i++)
            {
                if (i == -1 + arrayOfObject.length)
                {
                    arrayOfObject[(-1 + arrayOfObject.length)] = this;
                    XposedHelpers.findAndHookConstructor(paramString, this.O00000Oo, arrayOfObject);
                    return;
                }
                arrayOfObject[i] = paramArrayOfObject[i];
            }
        }
        catch (Throwable localThrowable)
        {
           Log.e("Xhook", "addHookConWithParms Exception " + paramString);
        }
    }

    public void O00000o0(String paramString)
    {
        try
        {
            for (Constructor localConstructor : XposedHelpers.findClass(paramString, this.O00000Oo).getDeclaredConstructors())
                if (Modifier.isPublic(localConstructor.getModifiers()))
                    XposedBridge.hookMethod(localConstructor, this);
        }
        catch (Throwable localThrowable)
        {
            Log.e("Xhook", "addHookConOnly exception", localThrowable);
        }
    }
}
