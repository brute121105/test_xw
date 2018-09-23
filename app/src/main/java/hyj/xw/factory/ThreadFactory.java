package hyj.xw.factory;

import android.accessibilityservice.AccessibilityService;

import java.util.Map;

import hyj.xw.BaseThread;
import hyj.xw.api.ALZGetPhoneAndValidCodeThread;
import hyj.xw.model.AccessibilityParameters;
import hyj.xw.thread.AutoFeedThread;
import hyj.xw.thread.AutoLoginThread;
import hyj.xw.thread.AutoOperatonThread;
import hyj.xw.thread.AutoRegThread;
import hyj.xw.thread.Fetch008DataHistoryThread;
import hyj.xw.thread.Fetch008DataThread;
import hyj.xw.thread.MonitorMessageThread;
import hyj.xw.thread.Recover008DataThread;
import hyj.xw.thread.SendAccessMsgThread;
import hyj.xw.thread.TestThread;

/**
 * Created by Administrator on 2017/12/14.
 */

public class ThreadFactory {

    public static BaseThread getThread(String name, AccessibilityService context, Map<String,String> record,AccessibilityParameters parameters){
        switch (name){
            case "autoOperation":
                return new AutoOperatonThread(context,record,parameters);
            case "login":
                return new AutoLoginThread(context,record,parameters);
            case "test":
                return new TestThread(context,record,parameters);
            case "feed":
                return new AutoFeedThread(context,record,parameters);
            case "reg":
                return new AutoRegThread(context,record,parameters);
            case "alzAPI":
                return new ALZGetPhoneAndValidCodeThread(parameters.getPa());
            case "fetch008Data":
                return new Fetch008DataThread(context,record,parameters);
            case "fetch008DataHistory":
                return new Fetch008DataHistoryThread(context,record,parameters);
            case "recover008Data":
                return new Recover008DataThread(context,record,parameters);
            case "monitorMessageThread":
                return new MonitorMessageThread(context,record,parameters);
            case "sendAccessMsgThread":
                return new SendAccessMsgThread(context,record,parameters);
        }
        throw new RuntimeException("【"+name+"】没有相关服务！");

    }
}
