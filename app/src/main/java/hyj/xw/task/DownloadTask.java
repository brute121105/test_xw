package hyj.xw.task;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import hyj.xw.view.CommonProgressDialog;


/**
 * Created by Administrator on 2018/8/7 0007.
 */

public class DownloadTask extends AsyncTask<String, Integer, String> {

    private CommonProgressDialog pBar;
    private PowerManager.WakeLock mWakeLock;
    private Context context;
    private static String downLoadAPkName = "testDownLoad";

    public DownloadTask(Context context,CommonProgressDialog pBar) {
        this.context = context;
        this.pBar = pBar;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        System.out.println("doAction-->sUrl:"+sUrl);
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        File file = null;
        try {
            String apkUrl = "http://120.78.134.230:8080/apk/download/2";
            //String apkUrl = "http://ucan.25pp.com/Wandoujia_web_seo_baidu_homepage.apk";
            //URL url = new URL(sUrl[0]);
            URL url = new URL(apkUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMCIsIm5hbWUiOiJhZG1pbjAxICIsIm5pY2tuYW1lIjoiIiwiYXZhdGFyIjoiIiwiaWF0IjoxNTMzNTM1OTc4LCJleHAiOjE1NDEzMTE5Nzh9._WYJ2qDz4bWUeoAARs6SBwOEjOFe-TEQSHDq0aPTnrmel1Av3it6xlvkGRmF2oPWCO7E96IB-tBYd0FA_WHi7g");
            connection.connect();
            System.out.println("doAction-->getResponseCode:"+connection.getResponseCode());
            System.out.println("doAction-->getResponseMessage:"+connection.getResponseMessage());
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                String res = "Server returned HTTP " + connection.getResponseCode() + " "+ connection.getResponseMessage();
                return res;
            }
            int fileLength = connection.getContentLength();
            System.out.println("doA-->fileLength:"+fileLength);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                file = new File(Environment.getExternalStorageDirectory(),downLoadAPkName);
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
        pBar.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        pBar.setIndeterminate(false);
        pBar.setMax(100);
        pBar.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        update();
    }

    private void update() {
        //安装应用
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), downLoadAPkName)),"application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
