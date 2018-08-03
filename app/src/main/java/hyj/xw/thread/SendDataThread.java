package hyj.xw.thread;

import java.io.IOException;

import hyj.xw.util.AutoUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.OkHttpUtil;

/**
 * Created by Administrator on 2017/8/15.
 */

public class SendDataThread implements Runnable{

    private String data;
    int index = 0;

    public SendDataThread(String data){
        this.data = data;
    }


    @Override
    public void run() {
        while (true){
            AutoUtil.sleep(300);
            LogUtil.d("die","die thread..");

            String url = "http://120.78.134.230/androidServer/test?data="+data+" "+index;
            String str = null;
            try {
                str = OkHttpUtil.okHttpGet(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            LogUtil.d("die response","die response str:"+str);
            if(!"OK".equals(str)){
                LogUtil.d("sendThreadData","发送失败："+data+" "+index);
            }
            index = index+1;

        }

    }

}
