package hyj.xw.thread;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import hyj.xw.common.FilePathCommon;
import hyj.xw.model.StartRunningConfig;
import hyj.xw.modelHttp.Device;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.FileUtil;
import hyj.xw.util.OkHttpUtil;


/**
 * Created by Administrator on 2018/7/6 0006.
 */

public class MonitorStatusThread  extends Thread {
    String host = "http://lizq.ngrok.xiaomiqiu.cn";
    public MonitorStatusThread(){
    }
    @Override
    public void run() {
        while (true){
            AutoUtil.sleep(1000);
            deviceConnectServer();
        }
    }

    public String deviceConnectServer(){
        String url =host+"/device/connect";
        Device device = new Device();
        device.setNum("A001");
        String res = null;
        try {
            res = OkHttpUtil.okHttpPostBody(url, JSON.toJSONString(device));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("OkHttpUtil deviceConnectServer url--->"+url);
        System.out.println("OkHttpUtil deviceConnectServer --->"+res);
        return res;
    }

    public StartRunningConfig getStartRunningConfig(){
        String srConfigStr = FileUtil.readAllUtf8(FilePathCommon.startRunninConfigTxtPath);
        StartRunningConfig srConfig = JSONObject.parseObject(srConfigStr,StartRunningConfig.class);
        return srConfig;
    }
    public  void saveStartRunningConfig(StartRunningConfig srConfig){
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.startRunninConfigTxtPath, JSON.toJSONString(srConfig));
    }


}
