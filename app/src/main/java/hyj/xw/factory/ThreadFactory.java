package hyj.xw.factory;

import android.accessibilityservice.AccessibilityService;

import java.util.Map;

import hyj.xw.BaseThread;
import hyj.xw.model.AccessibilityParameters;
import hyj.xw.thread.AutoLoginThread;
import hyj.xw.thread.TestThread;

/**
 * Created by Administrator on 2017/12/14.
 */

public class ThreadFactory {

    public static BaseThread getThread(String name, AccessibilityService context, Map<String,String> record,AccessibilityParameters parameters){
        switch (name){
            case "login":
                return new AutoLoginThread(context,record,parameters);
            case "test":
                return new TestThread(context,record,parameters);

        }
        throw new RuntimeException("【"+name+"】没有相关服务！");

    }
}
