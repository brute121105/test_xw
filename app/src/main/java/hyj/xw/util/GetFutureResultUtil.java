package hyj.xw.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import hyj.xw.GlobalApplication;
import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.modelHttp.Apk;
import hyj.xw.thread.CheckVersionCodeThread;
import hyj.xw.thread.GetOrUpdateServerStatusThread;

/**
 * Created by Administrator on 2018/8/9 0009.
 */

public class GetFutureResultUtil {

    public static Apk checkVersion1(String apkType){
        String uiautoVersionCodeStr = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_UIAUTO_VERSION);
        int currentVersionCode = Integer.parseInt(TextUtils.isEmpty(uiautoVersionCodeStr) ? "0" : uiautoVersionCodeStr);
        if("1".equals(apkType)){
            currentVersionCode = getVersion();
        }
        System.out.println("main--->currentVersionCode:"+currentVersionCode);
        CheckVersionCodeThread thread = new CheckVersionCodeThread(apkType,currentVersionCode);
        FutureTask<String> futureTask = thread.startThreadByFuture();
        String res = getFutureResult(futureTask);
        System.out.println("main--->checkUpdateRes:"+res);
        if("".equals(res)){
            AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"请求服务异常");
            System.out.println("main--->downloadAttach:请求服务异常");
            return null;
        }
        if(null==res){
            if("1".equals(apkType)){
                AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"当前是最新版本 "+getVersionName());
            }
            System.out.println("main--->downloadAttach:当前是最新版本");
            return null;
        }
        if(!res.contains("versionCode")){
            AutoUtil.showToastByRunnable(GlobalApplication.getContext(),res);
            System.out.println("main--->downloadAttach:res");
            return null;
        }
        Apk apk = com.alibaba.fastjson.JSONObject.parseObject(res,Apk.class);
        return apk;
    }

    public static String getFutureResult(FutureTask<String> futureTask){
        String res = "";
        try {
            res  = futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static int getVersion() {
        try {
            PackageManager manager = GlobalApplication.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(GlobalApplication.getContext().getPackageName(),0);
            String version = info.versionName;
            int versioncode = info.versionCode;
            return versioncode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getVersionName() {
        try {
            PackageManager manager = GlobalApplication.getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(GlobalApplication.getContext().getPackageName(),0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "获取版本失败";
    }


    public static String getServerStatus(){
        String status = "";
        FutureTask<String> futureTask = startThreadByFuture("0");
        try {
            status  = futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            LogUtil.logError(e);
        } catch (ExecutionException e) {
            e.printStackTrace();
            LogUtil.logError(e);
        }
        return status;
    }

    public static  FutureTask<String> startThreadByFuture(String status){
        GetOrUpdateServerStatusThread thread = new GetOrUpdateServerStatusThread(status);
        FutureTask<String> futureTask = new FutureTask<>(thread);
        new Thread(futureTask).start();
        return futureTask;
    }

}
