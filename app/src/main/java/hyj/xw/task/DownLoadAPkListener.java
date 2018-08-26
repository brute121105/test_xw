package hyj.xw.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.LayoutInflater;

import org.json.JSONObject;

import hyj.xw.R;
import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.modelHttp.Apk;
import hyj.xw.service.HttpRequestService;
import hyj.xw.util.AutoUtil;
import hyj.xw.view.CommonProgressDialog;


/**
 * Created by Administrator on 2018/8/7 0007.
 */

public class DownLoadAPkListener implements DialogInterface.OnClickListener {
    private CommonProgressDialog pBar=null;
    private Context context;
    private String downloadPath;
    private String apkType;
    //private Long newVersionCode;
    private Apk apk;
    String apkDownloadUrl = "http://"+AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_HOST)+"/apk/download/";
    public DownLoadAPkListener(Context context,String downloadPath,String apkType,Apk apk){
        this.downloadPath = downloadPath;
        this.context = context;
        this.apkType = apkType;
        this.apk = apk;
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
        pBar = new CommonProgressDialog(context);
        pBar.setCanceledOnTouchOutside(false);
        pBar.setTitle("正在下载");
        pBar.setCustomTitle(LayoutInflater.from(context).inflate(R.layout.title_dialog, null));
        pBar.setMessage("正在下载");
        pBar.setIndeterminate(true);
        pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pBar.setCancelable(true);
        final DownloadTask downloadTask = new DownloadTask(context,pBar,downloadPath,apkType,apk);
        downloadTask.execute(apkDownloadUrl);
        pBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });

    }

    // 获取更新版本号
    public void checkVersion(){
        //int currentVersion = getVersion(context);
        //System.out.println("doAction--->currentVersion:"+currentVersion);
        apkDownloadUrl = apkDownloadUrl+apk.getId();
        //System.out.println("doAction--->apkDownloadUrl:"+apkDownloadUrl);
        String content = "版本"+apk.getVersionCode()+"\n发布时间"+apk.getCreateTime();
        showDwnloadApkDialog(content);
    }

    public void downloadAttach(boolean isAlertWindow){
        apkDownloadUrl = apkDownloadUrl+apk.getId();
        //System.out.println("doAction--->apkDownloadUrl:"+apkDownloadUrl);
        String content = "附件版本"+apk.getVersionCode()+"\n发布时间"+String.valueOf(apk.getCreateTime());
        System.out.println("main--->content:"+content);
        if(isAlertWindow){
            showDwnloadApkDialog(content);
        }else {
            DownloadTask downloadTask = new DownloadTask(context,pBar,downloadPath,apkType,apk);
            downloadTask.execute(apkDownloadUrl);
        }
    }


    public void showDwnloadApkDialog(String content) {
        new android.app.AlertDialog.Builder(context)
                .setTitle("版本更新")
                .setMessage(content)
                .setPositiveButton("更新",this)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


}
