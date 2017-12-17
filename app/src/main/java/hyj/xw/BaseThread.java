package hyj.xw;

import android.accessibilityservice.AccessibilityService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Administrator on 2017/12/14.
 */

public abstract class BaseThread implements Callable<Object> {
    public Map<String,Object> parameters;
    public Map<String,String> record;
    public AccessibilityService context;

    public BaseThread(){
    }
    public BaseThread(AccessibilityService context, Map<String,String> record,Map<String,Object> parameters) {
        this.record = record;
        this.context = context;
        this.parameters = parameters;
    }

}
