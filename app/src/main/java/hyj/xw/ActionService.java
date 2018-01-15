package hyj.xw;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hyj.xw.factory.ThreadFactory;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.LogUtil;

/**
 * Created by Administrator on 2017/12/14.
 */

public class ActionService  extends AccessibilityService {
    public Map<String,Object> parameters;
    Map<String,String> record = new HashMap<String,String>();
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        //executorService.submit(ThreadFactory.getThread("test",this,record,parameters));
        //executorService.submit(ThreadFactory.getThread("login",this,record,parameters));
        //AutoUtil.startWx();
    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        LogUtil.d("AccessibilityService","accessibilityEvent:"+accessibilityEvent.getContentDescription());

    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        LogUtil.d("AccessibilityService","KeyEvent:"+event.getKeyCode());
        return super.onKeyEvent(event);
    }

    @Override
    public void onInterrupt() {

    }

}
