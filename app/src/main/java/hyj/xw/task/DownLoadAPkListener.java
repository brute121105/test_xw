package hyj.xw.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;

import hyj.xw.R;
import hyj.xw.view.CommonProgressDialog;


/**
 * Created by Administrator on 2018/8/7 0007.
 */

public class DownLoadAPkListener implements DialogInterface.OnClickListener {
    private CommonProgressDialog pBar;
    private Context context;
    String url = "http://120.78.134.230:8080/apk/download/2";
    public DownLoadAPkListener(Context context){
        this.context = context;
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
        final DownloadTask downloadTask = new DownloadTask(context,pBar);
        downloadTask.execute(url);
        pBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });

    }

    // 获取更新版本号
    public void checkVersion() {
        int currentVersion = getVersion(context);
        String newversion = "2.1";//更新新的版本号
        String content = "更新版本";
        double newversioncode = Double.parseDouble(newversion);
        int cc = (int) (newversioncode);
        System.out.println("newversioncode:"+newversioncode+" cc:"+cc+" currentVersion:"+currentVersion);
        if (cc != currentVersion) {
            if (currentVersion < cc) {
                showDwnloadApkDialog(content);
            }
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

    public int getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(),0);
            String version = info.versionName;
            int versioncode = info.versionCode;
            return versioncode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
