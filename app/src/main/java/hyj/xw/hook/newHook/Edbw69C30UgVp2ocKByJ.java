package hyj.xw.hook.newHook;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Random;
import java.util.UUID;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Administrator on 2018/4/2.
 */

public class Edbw69C30UgVp2ocKByJ extends DHHdslt4SqYQ1hSj1a4Y
{
    private static String[] O00000o = { "Adreno (TM) 330|Qualcomm", "Adreno (TM) 130|Qualcomm", "Adreno (TM) 200|Qualcomm", "Adreno (TM) 205|Qualcomm", "Adreno (TM) 220|Qualcomm", "Adreno (TM) 225|Qualcomm", "Adreno (TM) 320|Qualcomm", "PowerVR SGX 544MP|Imagination Technologies", "PowerVR SGX 530MP|Imagination Technologies", "PowerVR SGX 554MP4|Imagination Technologies", "PowerVR SGX 543MP3|Imagination Technologies", "PowerVR SGX 543MP4|Imagination Technologies", "Mail-400 MP|ARM", "Mail-450 MP|ARM", "Mail-460 MP|ARM" };
    //private String O00000oO = "3.4.5-" + com.money.O00000Oo.Edbw69C30UgVp2ocKByJ.O0000Oo0(8);
    private String O00000oO = "3.4.5-6";
    private String O00000oo = UUID.randomUUID().toString().toUpperCase();

    public Edbw69C30UgVp2ocKByJ(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, NewPhoneInfo paramPhoneInfo)
    {
        super(paramLoadPackageParam, paramPhoneInfo);
        Object[] arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = ActivityManager.MemoryInfo.class.getName();
        O000000o("android.app.ActivityManager", "getMemoryInfo", arrayOfObject1);
        O000000o("android.content.pm.ApplicationInfo", "getApplicationInfo", new Object[0]);
        O000000o("com.android.internal.os.PowerProfile", "getAveragePower");
        O000000o("com.google.android.gles_jni.GLImpl", "glGetString");
        O000000o("android.net.TrafficStats", "getMobileRxBytes");
        O000000o("android.net.TrafficStats", "getMobileTxBytes");
        O000000o("android.net.TrafficStats", "getTotalTxBytes");
        O000000o("android.net.TrafficStats", "getTotalRxBytes");
        String str = Intent.class.getName();
        Object[] arrayOfObject2 = new Object[2];
        arrayOfObject2[0] = String.class;
        arrayOfObject2[1] = Integer.TYPE;
        O000000o(str, "getIntExtra", arrayOfObject2);
        O000000o(SystemClock.class.getName(), "elapsedRealtime", new Object[0]);
        O000000o(File.class.getName(), "lastModified", new Object[0]);
        O000000o("java.lang.System", "getProperty");
        O000000o(Window.class.getName(), "getAttributes", new Object[0]);
        O000000o(SystemClock.class.getName(), "elapsedRealtime", new Object[0]);
        O000000o("com.google.android.gms.ads.identifier.AdvertisingIdClient.Info", "getId");
    }


    @Override
    protected void afterHookedMethod(XC_MethodHook.MethodHookParam xc_MethodHook$MethodHookParam) throws Throwable {
        int i = 0;
        super.afterHookedMethod(xc_MethodHook$MethodHookParam);
        final String name = xc_MethodHook$MethodHookParam.method.getName();
        if ("getMemoryInfo".equals(name)) {
            final ActivityManager.MemoryInfo activityManager$MemoryInfo = (ActivityManager.MemoryInfo)xc_MethodHook$MethodHookParam.args[0];
            if (this.O00000o0.getMemTotal() > 0L && this.O00000o0.getMemTotal() > 0L) {
                XposedHelpers.setLongField((Object)activityManager$MemoryInfo, "totalMem", this.O00000o0.getMemTotal());
                XposedHelpers.setLongField((Object)activityManager$MemoryInfo, "availMem", this.O00000o0.getMemAvailable());
            }
        }
        else {
            if ("getApplicationInfo".equals(name)) {
                final ApplicationInfo result = (ApplicationInfo)xc_MethodHook$MethodHookParam.getResult();
                for (Field[] declaredFields = result.getClass().getDeclaredFields(); i < declaredFields.length; ++i) {
                    final Field field = declaredFields[i];
                    if ("primaryCpuAbi".equals(field.getName())) {
                        if (!TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildAbi())) {
                            XposedHelpers.setObjectField((Object)result, "primaryCpuAbi", (Object)this.O00000o0.getBuildAbi());
                        }
                    }
                    else if ("secondaryCpuAbi".equals(field.getName()) && !TextUtils.isEmpty((CharSequence)this.O00000o0.getBuildAbi2())) {
                        XposedHelpers.setObjectField((Object)result, "secondaryCpuAbi", (Object)this.O00000o0.getBuildAbi2());
                    }
                }
                xc_MethodHook$MethodHookParam.setResult((Object)result);
                return;
            }
            if ("getAveragePower".equals(name)) {
                xc_MethodHook$MethodHookParam.setResult((Object)(1000 + new Random().nextInt(500)));
                return;
            }
            if ("glGetString".equals(name)) {
               /* final int intValue = (int)xc_MethodHook$MethodHookParam.args[0];
                final String[] split = \u93b4\u621d\u6093\u93b0\u5fcb.\u9518\u630e\u579c\u93c4\u5ea3\u6ae7\u6d5c\u55d0[new Random().nextInt(\u93b4\u621d\u6093\u93b0\u5fcb.\u9518\u630e\u579c\u93c4\u5ea3\u6ae7\u6d5c\u55d0.length)].split("|");
                if (intValue == 7937) {
                    xc_MethodHook$MethodHookParam.setResult((Object)split[0]);
                }
                if (intValue == 7936) {
                    xc_MethodHook$MethodHookParam.setResult((Object)split[1]);
                }*/
            }
            else {
                if ("getMobileRxBytes".equals(name)) {
                    xc_MethodHook$MethodHookParam.setResult((Object)(1000000 + new Random().nextInt(999999)));
                    return;
                }
                if ("getMobileTxBytes".equals(name)) {
                    xc_MethodHook$MethodHookParam.setResult((Object)new Random().nextInt(999999));
                    return;
                }
                if ("getTotalTxBytes".equals(name)) {
                    xc_MethodHook$MethodHookParam.setResult((Object)(200000000 + new Random().nextInt(200000000)));
                    return;
                }
                if ("getTotalRxBytes".equals(name)) {
                    xc_MethodHook$MethodHookParam.setResult((Object)(100000000 + new Random().nextInt(100000000)));
                    return;
                }
                if ("getIntExtra".equals(name)) {
                    final Intent intent = (Intent)xc_MethodHook$MethodHookParam.thisObject;
                    if (!TextUtils.isEmpty((CharSequence)intent.getAction()) && "android.intent.action.BATTERY_CHANGED".equals(intent.getAction()) && "level".equals(xc_MethodHook$MethodHookParam.args[0])) {
                        xc_MethodHook$MethodHookParam.setResult((Object)this.O00000o0.getBatteryLevel());
                    }
                }
                else if ("lastModified".equals(name)) {
                    final File file = (File)xc_MethodHook$MethodHookParam.thisObject;
                    if (file != null && file.getAbsolutePath().contains("data/app/")) {
                        Log.d("OtherHook", "lastModified ---> " + xc_MethodHook$MethodHookParam.getResult());
                    }
                }
                else if ("getProperty".equals(name)) {
                    final String s = (String)xc_MethodHook$MethodHookParam.args[0];
                    if (s.equals("os.arch")) {
                        xc_MethodHook$MethodHookParam.setResult((Object)"armv7l");
                    }
                    if (s.equals("os.version")) {
                        xc_MethodHook$MethodHookParam.setResult((Object)this.O00000oO);
                    }
                    if (s.equals("user.name")) {
                        xc_MethodHook$MethodHookParam.setResult((Object)"");
                    }
                    if (s.equals("java.class.path")) {
                        xc_MethodHook$MethodHookParam.setResult((Object)((String)xc_MethodHook$MethodHookParam.getResult()).replaceAll("XposedBridge.jar", ""));
                    }
                }
                else {
                    if ("getAttributes".equals(name)) {
                        final WindowManager.LayoutParams result2 = (WindowManager.LayoutParams)xc_MethodHook$MethodHookParam.getResult();
                        result2.screenBrightness = (float)(0.5 + 0.5 * Math.random());
                        xc_MethodHook$MethodHookParam.setResult((Object)result2);
                        return;
                    }
                    if ("elapsedRealtime".equals(name)) {
                        xc_MethodHook$MethodHookParam.setResult((Object)(1990000L + (System.currentTimeMillis() - this.O00000o0.getBuildUtc())));
                        return;
                    }
                    if ("getId".equals(name)) {
                        xc_MethodHook$MethodHookParam.setResult((Object)this.O00000oo);
                        return;
                    }
                    if ("getStackTrace".equals(name) && xc_MethodHook$MethodHookParam.getResult() instanceof StackTraceElement[]) {
                        final StackTraceElement[] array = (StackTraceElement[])xc_MethodHook$MethodHookParam.getResult();
                        final LinkedHashMap<Integer, StackTraceElement> linkedHashMap = new LinkedHashMap<Integer, StackTraceElement>();
                        final int length = array.length;
                        int j = 0;
                        int n = 0;
                        int n2 = 0;
                        while (j < length) {
                            final StackTraceElement stackTraceElement = array[j];
                            int n3;
                            if (!stackTraceElement.getClassName().equals("de.robv.android.xposed.XposedBridge")) {
                                linkedHashMap.put(n2, stackTraceElement);
                                n3 = n2 + 1;
                            }
                            else {
                                n3 = n2;
                            }
                            if (n != 0 && "getStackTrace".equals(stackTraceElement.getMethodName())) {
                                --n3;
                                linkedHashMap.remove(n3);
                            }
                            if ("getStackTrace".equals(stackTraceElement.getMethodName())) {
                                n = 1;
                            }
                            ++j;
                            n2 = n3;
                        }
                        final StackTraceElement[] result3 = new StackTraceElement[n2];
                        for (int k = 0; k < n2; ++k) {
                            result3[k] = linkedHashMap.get(k);
                        }
                        xc_MethodHook$MethodHookParam.setResult((Object)result3);
                    }
                }
            }
        }
    }


    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam) throws Throwable {
        super.beforeHookedMethod(paramMethodHookParam);
        String str1 = paramMethodHookParam.method.getName();
        if (("forName".equals(str1)) || ("loadClass".equals(str1)))
        {
            String str2 = (String)paramMethodHookParam.args[0];
            if ((str2 != null) && (str2.contains("de.robv.android.xposed")))
                paramMethodHookParam.setThrowable(new ClassNotFoundException());
        }
    }
}
