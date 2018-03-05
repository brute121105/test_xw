package hyj.xw.hook.util;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Administrator on 2017/11/16.
 */

public class HookUtil {
    public static Object getFieldObjByFieldName(String clssName, String fieldName, XC_LoadPackage.LoadPackageParam loadPackageParam,XC_MethodHook.MethodHookParam param){
        Class c= null;
        try {
            c = loadPackageParam.classLoader.loadClass(clssName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Field field= null;
        try {
            field = c.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        field.setAccessible(true);
        Object obj= null;
        try {
            obj = field.get(param.thisObject);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }


}
