package hyj.xw.task;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import hyj.xw.common.CommonConstant;
import hyj.xw.common.FilePathCommon;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.modelHttp.Apk;
import hyj.xw.util.AutoUtil;
import hyj.xw.view.CommonProgressDialog;


/**
 * Created by Administrator on 2018/8/7 0007.
 */

public class DownloadTask extends AsyncTask<String, Integer, String> {

    private CommonProgressDialog pBar;
    private PowerManager.WakeLock mWakeLock;
    private Context context;
    private String downloadPath;
    private String apkType;
    private Apk apk;
    //private static String downLoadAPkName = "testDownLoad";

    public DownloadTask(Context context,CommonProgressDialog pBar,String downloadPath,String apkType,Apk apk) {
        this.context = context;
        this.pBar = pBar;
        this.downloadPath = downloadPath;
        this.apkType = apkType;
        this.apk = apk;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        System.out.println("main-->doInBackground sUrl:"+sUrl[0]);
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        File file = null;
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("token",AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_WY_TOKEN));
            connection.connect();
            System.out.println("main-->getResponseCode:"+connection.getResponseCode());
            System.out.println("main-->getResponseMessage:"+connection.getResponseMessage());
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                String res = "Server returned HTTP " + connection.getResponseCode() + " "+ connection.getResponseMessage();
                return res;
            }
            int fileLength = connection.getContentLength();
            System.out.println("main-->fileLength:"+fileLength);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                file = new File(downloadPath);
                if (!file.exists()) {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                }
            } else {
                //Toast.makeText(MainActivity.this, "sd卡未挂载", Toast.LENGTH_LONG).show();
            }
            input = connection.getInputStream();
            output = new FileOutputStream(file);
            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);

            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return e.toString();

        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }
            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context
                .getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        if(pBar!=null) pBar.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        if(pBar!=null){
            pBar.setIndeterminate(false);
            pBar.setMax(100);
            pBar.setProgress(progress[0]);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if("1".equals(apkType)){
            update();
        }else {
            if(pBar!=null) pBar.cancel();
            File file = new File(FilePathCommon.downAPk1Path);
            if(file!=null&&file.length()>0){
                /*AutoUtil.execShell("cp /sdcard/hyj.autooperation.test /data/local/tmp/");
                AutoUtil.execShell("chmod 777 /data/local/tmp/hyj.autooperation.test");
                AutoUtil.execShell("pm install -r \"/data/local/tmp/hyj.autooperation.test\"");*/

                System.out.println("doAction-->附件下载完成并生产文件");
                AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_UIAUTO_VERSION,apk.getVersionCode()+"");
                //apk.setEndInstallApk(true);

                //AutoUtil.execShell("am instrument -w -r   -e debug false -e class hyj.autooperation.ExampleInstrumentedTest#installTest hyj.autooperation.test/android.support.test.runner.AndroidJUnitRunner");

            }

        }
    }

    private void update() {
        //安装应用
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(downloadPath)),"application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
