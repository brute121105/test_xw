package hyj.xw;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hyj.xw.factory.ThreadFactory;
import hyj.xw.util.AutoUtil;

/**
 * Created by Administrator on 2017/12/14.
 */

public class ActionService  extends AccessibilityService {

    Map<String,String> record = new HashMap<String,String>();
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();

        executorService.submit(ThreadFactory.getThread("test",this,record));
        //executorService.submit(ThreadFactory.getThread("login",this,record));

        AutoUtil.startWx();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

    }

    @Override
    public void onInterrupt() {

    }
}
