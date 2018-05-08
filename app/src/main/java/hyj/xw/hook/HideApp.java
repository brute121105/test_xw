package hyj.xw.hook;

import android.app.ActivityManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import hyj.xw.util.LogUtil;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class HideApp {
    private static String tag= "HideApp";
    XC_LoadPackage.LoadPackageParam sharePkgParam;

    public HideApp(XC_LoadPackage.LoadPackageParam sharePkgParam){
        this.sharePkgParam = sharePkgParam;

        //待验证是否生效
        hidePackageInfo(sharePkgParam);
        //影响答题登录
        //hideApplicationInfo(sharePkgParam)

        hideInstalledApplications(sharePkgParam);
        hideInstalledPackages(sharePkgParam);
        hideRecentTasks(sharePkgParam);
        hideRunningTasks(sharePkgParam);
        hideRunningAppProcesses(sharePkgParam);
        hideRunningServices(sharePkgParam);

    }


    public static void hideInstalledApplications(XC_LoadPackage.LoadPackageParam sharePkgParam){
        System.out.println("getInstalledApplications--xposde-->hideApp");
        try {
            XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager",sharePkgParam.classLoader,"getInstalledApplications", int.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param)
                        throws Throwable {
                    List localList8 = (List)param.getResult();
                    System.out.println("getInstalledApplications--xposde-->"+JSON.toJSONString(localList8));
                    ArrayList localArrayList8 = new ArrayList();
                    Iterator localIterator8 = localList8.iterator();
                    while (localIterator8.hasNext()) {
                        ApplicationInfo localApplicationInfo = (ApplicationInfo)localIterator8.next();
                        String str9 = localApplicationInfo.packageName;
                        System.out.println("xpose package--->"+str9);
                        if (isContainPackage(str9)){
                            System.out.println("xpose package hide--->"+str9);
                            LogUtil.d(tag, "Hide package: " + str9);
                        }else{
                            localArrayList8.add(localApplicationInfo);
                        }
                    }
                    param.setResult(localArrayList8);
                }
            });
        } catch (Exception ex) {
            XposedBridge.log(" hideApp  错误: " + ex.getMessage());
        }
    }
    public static void hideInstalledPackages(XC_LoadPackage.LoadPackageParam sharePkgParam){
        System.out.println("hideInstalledPackages--xposde-->hideApp");
        try {
            XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager",sharePkgParam.classLoader,"getInstalledPackages", int.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam paramMethodHookParam)
                        throws Throwable {
                    List localList6 = (List)paramMethodHookParam.getResult();
                    ArrayList localArrayList6 = new ArrayList();
                    Iterator localIterator6 = localList6.iterator();
                    while (localIterator6.hasNext())
                    {
                        PackageInfo localPackageInfo3 = (PackageInfo)localIterator6.next();
                        String str7 = localPackageInfo3.packageName;
                        if (isContainPackage(str7)){
                            LogUtil.d(tag, "hideInstalledPackages package: " + str7);
                        }else{
                            localArrayList6.add(localPackageInfo3);
                        }
                    }
                    paramMethodHookParam.setResult(localArrayList6);
                }
            });
        } catch (Exception ex) {
            XposedBridge.log(" hideApp  错误: " + ex.getMessage());
        }
    }
    //static String xw = "{\"applicationInfo\":{\"banner\":0,\"baseCodePath\":\"/data/app/hyj.xw-1/base.apk\",\"baseResourcePath\":\"/data/app/hyj.xw-1/base.apk\",\"className\":\"hyj.xw.GlobalApplication\",\"codePath\":\"/data/app/hyj.xw-1\",\"compatibleWidthLimitDp\":0,\"dataDir\":\"/data/data/hyj.xw\",\"descriptionRes\":0,\"enabled\":true,\"enabledSetting\":0,\"flags\":13155910,\"flagsEx\":0,\"icon\":2130903040,\"installLocation\":-1,\"labelRes\":2131165218,\"largestWidthLimitDp\":0,\"logo\":0,\"nativeLibraryDir\":\"/data/app/hyj.xw-1/lib/arm64\",\"nativeLibraryRootDir\":\"/data/app/hyj.xw-1/lib\",\"nativeLibraryRootRequiresIsa\":true,\"packageName\":\"hyj.xw\",\"processName\":\"hyj.xw\",\"publicSourceDir\":\"/data/app/hyj.xw-1/base.apk\",\"requiresSmallestWidthDp\":0,\"resourcePath\":\"/data/app/hyj.xw-1\",\"scanPublicSourceDir\":\"/data/app/hyj.xw-1\",\"scanSourceDir\":\"/data/app/hyj.xw-1\",\"seinfo\":\"default\",\"showUserIcon\":-10000,\"sourceDir\":\"/data/app/hyj.xw-1/base.apk\",\"targetSdkVersion\":25,\"taskAffinity\":\"hyj.xw\",\"theme\":2131296420,\"uiOptions\":0,\"uid\":10134,\"versionCode\":1},\"coreApp\":false,\"firstInstallTime\":1521727262902,\"installLocation\":-1,\"lastUpdateTime\":1521729450721,\"packageName\":\"hyj.xw\",\"requiredForAllUsers\":false,\"sharedUserLabel\":0,\"versionCode\":666,\"versionName\":\"1.0\"}";
   // static String mm = "{\"applicationInfo\":{\"banner\":0,\"baseCodePath\":\"/data/app/com.tencent.mm-2/base.apk\",\"baseResourcePath\":\"/data/app/com.tencent.mm-2/base.apk\",\"className\":\"com.tencent.mm.app.Application\",\"codePath\":\"/data/app/com.tencent.mm-2\",\"compatibleWidthLimitDp\":0,\"dataDir\":\"/data/data/com.tencent.mm\",\"descriptionRes\":0,\"enabled\":true,\"enabledSetting\":0,\"flags\":9977412,\"flagsEx\":0,\"icon\":2130838423,\"installLocation\":0,\"labelRes\":2131237746,\"largestWidthLimitDp\":0,\"logo\":0,\"nativeLibraryDir\":\"/data/app/com.tencent.mm-2/lib/arm\",\"nativeLibraryRootDir\":\"/data/app/com.tencent.mm-2/lib\",\"nativeLibraryRootRequiresIsa\":true,\"packageName\":\"com.tencent.mm\",\"primaryCpuAbi\":\"armeabi\",\"processName\":\"com.tencent.mm\",\"publicSourceDir\":\"/data/app/com.tencent.mm-2/base.apk\",\"requiresSmallestWidthDp\":0,\"resourcePath\":\"/data/app/com.tencent.mm-2\",\"scanPublicSourceDir\":\"/data/app/com.tencent.mm-2\",\"scanSourceDir\":\"/data/app/com.tencent.mm-2\",\"seinfo\":\"default\",\"showUserIcon\":-10000,\"sourceDir\":\"/data/app/com.tencent.mm-2/base.apk\",\"targetSdkVersion\":23,\"taskAffinity\":\"com.tencent.mm\",\"theme\":2131492924,\"uiOptions\":0,\"uid\":10133,\"versionCode\":1220},\"coreApp\":false,\"firstInstallTime\":1521725802395,\"installLocation\":0,\"lastUpdateTime\":1521726050989,\"packageName\":\"com.tencent.mm\",\"requiredForAllUsers\":false,\"sharedUserLabel\":0,\"versionCode\":1220,\"versionName\":\"6.6.1\"}";
    //static String mm = "{\"applicationInfo\":{\"banner\":0,\"baseCodePath\":\"/data/app/com.tencent.mm-2/base.apk\",\"baseResourcePath\":\"/data/app/com.tencent.mm-2/base.apk\",\"className\":\"com.tencent.mm.app.Application\",\"codePath\":\"/data/app/com.tencent.mm-2\",\"compatibleWidthLimitDp\":0,\"dataDir\":\"/data/data/com.tencent.mm\",\"descriptionRes\":0,\"enabled\":true,\"enabledSetting\":0,\"flags\":9977412,\"flagsEx\":0,\"icon\":2130838423,\"installLocation\":0,\"labelRes\":2131237746,\"largestWidthLimitDp\":0,\"logo\":0,\"nativeLibraryDir\":\"/data/app/com.tencent.mm-2/lib/arm\",\"nativeLibraryRootDir\":\"/data/app/com.tencent.mm-2/lib\",\"nativeLibraryRootRequiresIsa\":true,\"packageName\":\"com.tencent.mm\",\"primaryCpuAbi\":\"armeabi\",\"processName\":\"com.tencent.mm\",\"publicSourceDir\":\"/data/app/com.tencent.mm-2/base.apk\",\"requiresSmallestWidthDp\":0,\"resourcePath\":\"/data/app/com.tencent.mm-2\",\"scanPublicSourceDir\":\"/data/app/com.tencent.mm-2\",\"scanSourceDir\":\"/data/app/com.tencent.mm-2\",\"seinfo\":\"default\",\"showUserIcon\":-10000,\"sourceDir\":\"/data/app/com.tencent.mm-2/base.apk\",\"targetSdkVersion\":23,\"taskAffinity\":\"com.tencent.mm\",\"theme\":2131492924,\"uiOptions\":0,\"uid\":10133,\"versionCode\":1220},\"coreApp\":false,\"firstInstallTime\":1521725802395,\"installLocation\":0,\"lastUpdateTime\":1521726050989,\"packageName\":\"com.tencent.mm\",\"requiredForAllUsers\":false,\"sharedUserLabel\":0,\"versionCode\":1220,\"versionName\":\"6.6.166\"}";

    public static void hidePackageInfo(XC_LoadPackage.LoadPackageParam sharePkgParam){
        System.out.println("ApplicationPackageManager hidePackageInfo--xposde-->hideApp");
        try {
            XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager",sharePkgParam.classLoader,"getPackageInfo",String.class,int.class, new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    if(param.args[0]!=null&&"com.tencent.mm".equals(param.args[0].toString())){
                        //PackageInfo newPi =JSON.parseObject(mm,PackageInfo.class);
                        PackageInfo pi = (PackageInfo)param.getResult();
                        System.out.println("old pipi-->"+pi);
                        String strPi = JSON.toJSONString(pi).replaceAll("1120","1220").replaceAll("6.5.16","6.6.1");
                        //String strPi = JSON.toJSONString(pi).replaceAll("1060","1220").replaceAll("6.5.8","6.6.1");
                        System.out.println("strPi pipi-->"+strPi);
                        PackageInfo newPi =JSON.parseObject(strPi,PackageInfo.class);
                        System.out.println("json pipi-->"+JSON.toJSONString(newPi));
                        param.setResult(newPi);
                    }else if(param.args[0]!=null&&"hyj.xw".equals(param.args[0].toString())){
                        System.out.println("versionCode-->hook metod");
                        //PackageInfo newPi =JSON.parseObject(xw,PackageInfo.class);
                       // param.setResult(newPi);
                    }
                }

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    System.out.println("aram.args[0]--->"+param.args[0].toString());
                    if(param.args[0]!=null&&isContainPackage(param.args[0].toString())){
                        param.args[0] = "com.android.calendar";
                    }
                }
            });
        } catch (Exception ex) {
            XposedBridge.log(" hideApp  错误: " + ex.getMessage());
        }
    }

    public static void hidePackageInfo1(XC_LoadPackage.LoadPackageParam sharePkgParam){
        System.out.println("ApplicationPackageManager hidePackageInfo--xposde-->hideApp");
        try {
            XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager",sharePkgParam.classLoader,"getPackageInfo",String.class,int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    System.out.println("aram.args[0]--->"+param.args[0].toString());
                    if(param.args[0]!=null&&isContainPackage(param.args[0].toString())){
                        param.args[0] = "com.android.calendar";
                    }
                }
            });
        } catch (Exception ex) {
            XposedBridge.log(" hideApp  错误: " + ex.getMessage());
        }
    }

    public static void hideApplicationInfo(XC_LoadPackage.LoadPackageParam sharePkgParam){
        System.out.println("hideApplicationInfo--xposde-->hideApp");
        try {
            XposedHelpers.findAndHookMethod("android.app.ApplicationPackageManager",sharePkgParam.classLoader,"getApplicationInfo",String.class,int.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    param.args[0] = "com.android.calendar";
                }
            });
        } catch (Exception ex) {
            XposedBridge.log(" hideApp  错误: " + ex.getMessage());
        }
    }

    public static void hideRecentTasks(XC_LoadPackage.LoadPackageParam sharePkgParam){
        System.out.println("hideRecentTasks--xposde-->hideApp");
        try {
            XposedHelpers.findAndHookMethod("android.app.ActivityManager",sharePkgParam.classLoader,"getRecentTasks",int.class,int.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam paramMethodHookParam)
                        throws Throwable {
                    List localList3 = (List)paramMethodHookParam.getResult();
                    ArrayList localArrayList3 = new ArrayList();
                    Iterator localIterator3 = localList3.iterator();
                    while (localIterator3.hasNext())
                    {
                        ActivityManager.RecentTaskInfo localRecentTaskInfo = (ActivityManager.RecentTaskInfo)localIterator3.next();
                        String str4 = localRecentTaskInfo.baseIntent.getComponent().getPackageName();
                        if (isContainPackage(str4)){
                            LogUtil.d(tag, "hideRecentTasks package: " + str4);
                        }else{
                            localArrayList3.add(localRecentTaskInfo);
                        }
                    }
                    paramMethodHookParam.setResult(localArrayList3);
                }
            });
        } catch (Exception ex) {
            XposedBridge.log(" hideApp  错误: " + ex.getMessage());
        }
    }

    public static void hideRunningTasks(XC_LoadPackage.LoadPackageParam sharePkgParam){
        System.out.println("hideRunningTasks--xposde-->hideApp");
        try {
            XposedHelpers.findAndHookMethod("android.app.ActivityManager",sharePkgParam.classLoader,"getRunningTasks",int.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam paramMethodHookParam)
                        throws Throwable {
                    List localList4 = (List)paramMethodHookParam.getResult();
                    ArrayList localArrayList4 = new ArrayList();
                    Iterator localIterator4 = localList4.iterator();
                    while (localIterator4.hasNext())
                    {
                        ActivityManager.RunningTaskInfo localRunningTaskInfo = (ActivityManager.RunningTaskInfo)localIterator4.next();
                        if (localRunningTaskInfo.baseActivity != null)
                        {
                            String str5 = localRunningTaskInfo.baseActivity.flattenToString();
                            if (isContainPackage(str5))
                                LogUtil.d(tag, "hideRunningTasks package: " + str5);
                            else
                                localArrayList4.add(localRunningTaskInfo);
                        }
                    }
                    paramMethodHookParam.setResult(localArrayList4);
                }
            });
        } catch (Exception ex) {
            XposedBridge.log(" hideApp  错误: " + ex.getMessage());
        }
    }

    public static void hideRunningAppProcesses(XC_LoadPackage.LoadPackageParam sharePkgParam){
        System.out.println("hideRunningAppProcesses--xposde-->hideApp");
        try {
            XposedHelpers.findAndHookMethod("android.app.ActivityManager",sharePkgParam.classLoader,"getRunningAppProcesses", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam paramMethodHookParam)
                        throws Throwable {
                    List localList7 = (List)paramMethodHookParam.getResult();
                    ArrayList localArrayList7 = new ArrayList();
                    Iterator localIterator7 = localList7.iterator();
                    while (localIterator7.hasNext())
                    {
                        ActivityManager.RunningAppProcessInfo localRunningAppProcessInfo = (ActivityManager.RunningAppProcessInfo)localIterator7.next();
                        String str8 = localRunningAppProcessInfo.processName;
                        if (isContainPackage(str8))
                            LogUtil.d(tag, "hideRunningAppProcesses package: " + str8);
                        else
                            localArrayList7.add(localRunningAppProcessInfo);
                    }
                    paramMethodHookParam.setResult(localArrayList7);
                }
            });
        } catch (Exception ex) {
            XposedBridge.log(" hideApp  错误: " + ex.getMessage());
        }
    }

    public static void hideRunningServices(XC_LoadPackage.LoadPackageParam sharePkgParam){
        System.out.println("hideRunningServices--xposde-->hideApp");
        try {
            XposedHelpers.findAndHookMethod("android.app.ActivityManager",sharePkgParam.classLoader,"getRunningServices",int.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam paramMethodHookParam)
                        throws Throwable {
                    List localList5 = (List)paramMethodHookParam.getResult();
                    ArrayList localArrayList5 = new ArrayList();
                    Iterator localIterator5 = localList5.iterator();
                    while (localIterator5.hasNext())
                    {
                        ActivityManager.RunningServiceInfo localRunningServiceInfo = (ActivityManager.RunningServiceInfo)localIterator5.next();
                        String str6 = localRunningServiceInfo.process;
                        if (isContainPackage(str6))
                            LogUtil.d(tag, "hideRunningServices package: " + str6);
                        else
                            localArrayList5.add(localRunningServiceInfo);
                    }
                    paramMethodHookParam.setResult(localArrayList5);
                }
            });
        } catch (Exception ex) {
            XposedBridge.log(" hideApp  错误: " + ex.getMessage());
        }
    }



    private static boolean isContainPackage(String paramString)
    {
        if(paramString==null)  return true;
        return paramString==null||paramString.contains("miui")||paramString.contains("xiaomi")||paramString.contains("008")||paramString.contains("root")
                ||paramString.contains("xposed");
    }
}