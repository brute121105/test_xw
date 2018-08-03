package hyj.xw.thread;

import java.io.IOException;

import hyj.xw.util.LogUtil;
import hyj.xw.util.OkHttpUtil;

import static hyj.xw.util.OkHttpUtil.okHttpGet;

/**
 * Created by Administrator on 2017/8/15.
 */

public class DieThread implements Runnable{


    @Override
    public void run() {
        LogUtil.d("die","die thread..");
        String url = "http://120.78.134.230/androidServer/test?data=dsf好的DieThread";
        String str = null;
        try {
            str = okHttpGet(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtil.d("die response","die response str:"+str);
    }

}
