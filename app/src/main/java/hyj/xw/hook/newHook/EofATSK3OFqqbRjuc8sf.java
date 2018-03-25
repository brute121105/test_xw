package hyj.xw.hook.newHook;

import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.view.inputmethod.InputMethodInfo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by asus on 2018/3/24.
 */

public class EofATSK3OFqqbRjuc8sf extends DHHdslt4SqYQ1hSj1a4Y
{
    public EofATSK3OFqqbRjuc8sf(XC_LoadPackage.LoadPackageParam paramLoadPackageParam, PhoneInfo paramPhoneInfo)
    {
        super(paramLoadPackageParam, paramPhoneInfo);
        O000000o("android.app.ApplicationPackageManager", "getPackageArchiveInfo");
        O000000o("android.app.ApplicationPackageManager", "queryIntentActivities");
        O000000o("android.app.ApplicationPackageManager", "queryIntentServices");
        O000000o("android.app.ApplicationPackageManager", "queryBroadcastReceivers");
        O000000o("android.content.pm.PackageManager", "getPackageArchiveInfo");
        Object[] arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = Integer.TYPE.getName();
        O000000o("android.app.ApplicationPackageManager", "getInstalledPackages", arrayOfObject1);
        Object[] arrayOfObject2 = new Object[2];
        arrayOfObject2[0] = String.class.getName();
        arrayOfObject2[1] = Integer.TYPE.getName();
        O000000o("android.app.ApplicationPackageManager", "getPackageInfo", arrayOfObject2);
        Object[] arrayOfObject3 = new Object[2];
        arrayOfObject3[0] = String.class.getName();
        arrayOfObject3[1] = Integer.TYPE.getName();
        O000000o("android.app.ApplicationPackageManager", "getApplicationInfo", arrayOfObject3);
        Object[] arrayOfObject4 = new Object[1];
        arrayOfObject4[0] = Integer.TYPE.getName();
        O000000o("android.app.ApplicationPackageManager", "getInstalledApplications", arrayOfObject4);
        O000000o("android.app.ActivityManager", "getRunningAppProcesses", new Object[0]);
        Object[] arrayOfObject5 = new Object[1];
        arrayOfObject5[0] = Integer.TYPE.getName();
        O000000o("android.app.ActivityManager", "getRunningServices", arrayOfObject5);
        Object[] arrayOfObject6 = new Object[1];
        arrayOfObject6[0] = Integer.TYPE.getName();
        O000000o("android.app.ActivityManager", "getRunningTasks", arrayOfObject6);
        Object[] arrayOfObject7 = new Object[2];
        arrayOfObject7[0] = Integer.TYPE.getName();
        arrayOfObject7[1] = Integer.TYPE.getName();
        O000000o("android.app.ActivityManager", "getRecentTasks", arrayOfObject7);
        O000000o("android.view.inputmethod.InputMethodManager", "getInputMethodList");
        O000000o("android.view.inputmethod.InputMethodManager", "getEnabledInputMethodList");
    }

    private boolean O000000o(String paramString)
    {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add("com.cyjh.mobileanjian.input.inputkb");
        localArrayList.add("net.aisence.Touchelper.IME");
        localArrayList.add("com.touchsprite.android.core.TSInputMethod");
        localArrayList.add("com.scriptelf.IME");
        return localArrayList.contains(paramString);
    }

    private boolean O00000Oo(String paramString)
    {
        List localList = this.O00000o0.getHidePkgs();
        localList.add("com.bluestacks.appsettings");
        localList.add("com.bluestacks.settings");
        localList.add("com.bluestacks.bluestackslocationprovider");
        localList.add("de.robv.android.xposed.installer");
        localList.add("pro.burgerz.wsm.manager");
        localList.add("com.tomcat.bjds");
        localList.remove(this.O000000o);
        return localList.contains(paramString);
    }

    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam)
    {
        String str1 = paramMethodHookParam.method.getName();
        if ("getInstalledApplications".equals(str1))
        {
            List localList8 = (List)paramMethodHookParam.getResult();
            ArrayList localArrayList8 = new ArrayList();
            Iterator localIterator8 = localList8.iterator();
            while (localIterator8.hasNext())
            {
                ApplicationInfo localApplicationInfo = (ApplicationInfo)localIterator8.next();
                String str9 = localApplicationInfo.packageName;
                if (O00000Oo(str9)){
                    //FVKjWjKo1YaG6p5uD2qz.O000000o("HidePkgHook", "Hide package: " + str9);
                }
                else
                    localArrayList8.add(localApplicationInfo);
            }
            paramMethodHookParam.setResult(localArrayList8);
        }


        if ("getRunningAppProcesses".equals(str1))
        {
            List localList7 = (List)paramMethodHookParam.getResult();
            ArrayList localArrayList7 = new ArrayList();
            Iterator localIterator7 = localList7.iterator();
            while (localIterator7.hasNext())
            {
                ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo)localIterator7.next();
                String str8 = localRunningAppProcessInfo.processName;
                if (O00000Oo(str8)){
                    //FVKjWjKo1YaG6p5uD2qz.O000000o("HidePkgHook", "Hide process: " + str8);
                }
                else
                    localArrayList7.add(localRunningAppProcessInfo);
            }
            paramMethodHookParam.setResult(localArrayList7);
        }
        else if ("getInstalledPackages".equals(str1))
        {
            List localList6 = (List)paramMethodHookParam.getResult();
            ArrayList localArrayList6 = new ArrayList();
            Iterator localIterator6 = localList6.iterator();
            while (localIterator6.hasNext())
            {
                PackageInfo localPackageInfo3 = (PackageInfo)localIterator6.next();
                String str7 = localPackageInfo3.packageName;
                if (O00000Oo(str7)){
                    //FVKjWjKo1YaG6p5uD2qz.O000000o("HidePkgHook", "Hide package: " + str7);
                }
                else
                    localArrayList6.add(localPackageInfo3);
            }
            paramMethodHookParam.setResult(localArrayList6);
        }
        else if ("getRunningServices".equals(str1))
        {
            List localList5 = (List)paramMethodHookParam.getResult();
            ArrayList localArrayList5 = new ArrayList();
            Iterator localIterator5 = localList5.iterator();
            while (localIterator5.hasNext())
            {
                ActivityManager.RunningServiceInfo localRunningServiceInfo = (ActivityManager.RunningServiceInfo)localIterator5.next();
                String str6 = localRunningServiceInfo.process;
                if (O00000Oo(str6)){
                    //FVKjWjKo1YaG6p5uD2qz.O000000o("HidePkgHook", "Hid service: " + str6);
                }
                else
                    localArrayList5.add(localRunningServiceInfo);
            }
            paramMethodHookParam.setResult(localArrayList5);
        }
        else if ("getRunningTasks".equals(str1))
        {
            List localList4 = (List)paramMethodHookParam.getResult();
            ArrayList localArrayList4 = new ArrayList();
            Iterator localIterator4 = localList4.iterator();
            while (localIterator4.hasNext())
            {
                ActivityManager.RunningTaskInfo localRunningTaskInfo = (ActivityManager.RunningTaskInfo)localIterator4.next();
                if (localRunningTaskInfo.baseActivity != null)
                {
                    String str5 = localRunningTaskInfo.baseActivity.flattenToString();
                    if (O00000Oo(str5)){
                        //FVKjWjKo1YaG6p5uD2qz.O000000o("HidePkgHook", "Hid task: " + str5);
                    }
                    else
                        localArrayList4.add(localRunningTaskInfo);
                }
            }
            paramMethodHookParam.setResult(localArrayList4);
        }
        else if ("getRecentTasks".equals(str1))
        {
            List localList3 = (List)paramMethodHookParam.getResult();
            ArrayList localArrayList3 = new ArrayList();
            Iterator localIterator3 = localList3.iterator();
            while (localIterator3.hasNext())
            {
                ActivityManager.RecentTaskInfo localRecentTaskInfo = (ActivityManager.RecentTaskInfo)localIterator3.next();
                String str4 = localRecentTaskInfo.baseIntent.getComponent().getPackageName();
                if (O00000Oo(str4)){
                    // FVKjWjKo1YaG6p5uD2qz.O000000o("HidePkgHook", "Hid task: " + str4);
                }
                else
                    localArrayList3.add(localRecentTaskInfo);
            }
            paramMethodHookParam.setResult(localArrayList3);
        }
        else if (("getInputMethodList".equals(str1)) || ("getEnabledInputMethodList".equals(str1)))
        {
            List localList1 = (List)paramMethodHookParam.getResult();
            ArrayList localArrayList1 = new ArrayList();
            Iterator localIterator1 = localList1.iterator();
            while (localIterator1.hasNext())
            {
                InputMethodInfo localInputMethodInfo = (InputMethodInfo)localIterator1.next();
                String str2 = localInputMethodInfo.getServiceName();
                if (O000000o(str2)){
                    //FVKjWjKo1YaG6p5uD2qz.O000000o("HidePkgHook", "Hid InputMethodInfo: " + str2);
                }
                else
                    localArrayList1.add(localInputMethodInfo);
            }
            paramMethodHookParam.setResult(localArrayList1);
        }
        else if ("getPackageArchiveInfo".equals(str1))
        {
            PackageInfo localPackageInfo2 = (PackageInfo)paramMethodHookParam.getResult();
            if ((localPackageInfo2 != null) && (localPackageInfo2.applicationInfo != null) && (O00000Oo(localPackageInfo2.applicationInfo.packageName)))
                paramMethodHookParam.setResult(null);
        }
        else if (("queryIntentActivities".equals(str1)) || ("queryBroadcastReceivers".equals(str1)) || ("queryIntentServices".equals(str1)))
        {
            List localList2 = (List)paramMethodHookParam.getResult();
            if ((localList2 != null) && (localList2.size() > 0))
            {
                ArrayList localArrayList2 = new ArrayList();
                Iterator localIterator2 = localList2.iterator();
                ResolveInfo localResolveInfo;
                while (localIterator2.hasNext())
                {
                    localResolveInfo = (ResolveInfo)localIterator2.next();
                    if (localResolveInfo.activityInfo != null)
                    {
                        String str3 = localResolveInfo.activityInfo.packageName;
                        if (!O00000Oo(str3)){
                            localArrayList2.add(localResolveInfo);
                            paramMethodHookParam.setResult(localArrayList2);
                            break;
                        }

                    }
                }

            }
        }
        else if ("getPackageArchiveInfo".equals(str1))
        {
            PackageInfo localPackageInfo1 = (PackageInfo)paramMethodHookParam.getResult();
            if ((localPackageInfo1 != null) && (localPackageInfo1.applicationInfo != null) && (O00000Oo(localPackageInfo1.applicationInfo.packageName)))
                paramMethodHookParam.setResult(null);
        }
    }

    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramMethodHookParam)
    {
        String str = paramMethodHookParam.method.getName();
        if ((("getPackageInfo".equals(str)) || ("getApplicationInfo".equals(str))) && (paramMethodHookParam.args[0] != null) && (O00000Oo(String.valueOf(paramMethodHookParam.args[0]))))
            paramMethodHookParam.args[0] = "com.android.settings";
    }
}