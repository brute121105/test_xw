package hyj.autooperation;

import android.accessibilityservice.AccessibilityService;
import android.support.test.uiautomator.UiDevice;

import java.util.Map;
import java.util.concurrent.Callable;




public abstract class BaseThread implements Callable<Object> {
    public Map<String,String> record;
    public UiDevice mDevice;

    public BaseThread(){
    }
    public BaseThread( Map<String,String> record,UiDevice mDevice) {
        this.record = record;
        this.mDevice =  mDevice;
    }

}
