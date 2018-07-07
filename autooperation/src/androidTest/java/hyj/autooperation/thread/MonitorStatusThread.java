package hyj.autooperation.thread;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import hyj.autooperation.common.FilePathCommon;
import hyj.autooperation.httpModel.Device;
import hyj.autooperation.util.FileUtil;
import hyj.autooperation.util.OkHttpUtil;


/**
 * Created by Administrator on 2018/7/6 0006.
 */

public class MonitorStatusThread extends Thread {
    String host = "http://lizq.ngrok.xiaomiqiu.cn";
    public MonitorStatusThread(){
    }
    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            deviceConnectServer();
        }
    }

    public String deviceConnectServer(){
        String url =host+"/device/connect";
        Device device = new Device();
        device.setNum("A001");
        String res = OkHttpUtil.okHttpPostBody(url,JSON.toJSONString(device));
        System.out.println("test uiAUto OkHttpUtil deviceConnectServer url--->"+url);
        System.out.println("OkHttpUtil deviceConnectServer --->"+res);
        return res;
    }




}
