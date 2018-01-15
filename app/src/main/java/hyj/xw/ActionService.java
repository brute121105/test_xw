package hyj.xw;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hyj.xw.factory.ThreadFactory;
import hyj.xw.model.AccessibilityParameters;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.LogUtil;

/**
 * Created by Administrator on 2017/12/14.
 */

public class ActionService  extends AccessibilityService {
    public AccessibilityParameters parameters = new AccessibilityParameters();
    Map<String,String> record = new HashMap<String,String>();
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        parameters.setIsStop(0);
        //executorService.submit(ThreadFactory.getThread("test",this,record,parameters));
        executorService.submit(ThreadFactory.getThread("login",this,record,parameters));
        //AutoUtil.startWx();
    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        LogUtil.d("AccessibilityService","accessibilityEvent:"+accessibilityEvent.getEventType());

    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        LogUtil.d("AccessibilityService","KeyEvent:"+keyCode);
        if(keyCode==24){
            parameters.setIsStop(0);
        }
        if(keyCode==25){
            parameters.setIsStop(1);
        }
        return super.onKeyEvent(event);
    }

    @Override
    public void onInterrupt() {

    }

}
