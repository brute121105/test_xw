package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Map;

import hyj.xw.BaseThread;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.ParseRootUtil;

/**
 * Created by Administrator on 2017/12/14.
 */

public class TestThread extends BaseThread {

    public  final String TAG = this.getClass().getSimpleName();
    public TestThread(AccessibilityService context, Map<String, String> record,Map<String,Object> parameters){
        super(context,record,parameters);
    }
    @Override
    public Object call() throws Exception {
        while (true){
            AutoUtil.sleep(3000);
            LogUtil.d(TAG,Thread.currentThread().getName());
            AccessibilityNodeInfo root = context.getRootInActiveWindow();
            if(root==null){
                LogUtil.d(TAG,"root is null");
                AutoUtil.sleep(500);
                continue;
            }
            System.out.println("deb==================================");
            //ParseRootUtil.debugRoot(root);
            ParseRootUtil.getCurrentViewAllNode(root);

        }
    }

}
