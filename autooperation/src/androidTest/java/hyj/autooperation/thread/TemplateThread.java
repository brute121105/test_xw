package hyj.autooperation.thread;

import android.accessibilityservice.AccessibilityService;
import android.support.test.uiautomator.UiDevice;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Map;

import hyj.autooperation.BaseThread;
import hyj.autooperation.util.AutoUtil;
import hyj.autooperation.util.LogUtil;


/**
 * Created by Administrator on 2017/12/14.
 */

public class TemplateThread extends BaseThread {
    private  int countRootNull =0;
    public  final String TAG = this.getClass().getSimpleName();
    public TemplateThread(Map<String, String> record,UiDevice mDevice){
        super(record,mDevice);
    }
    private void intiParam(){

    }
    @Override
    public Object call() {
        while (true){
            try {
                AutoUtil.sleep(1500);
                LogUtil.d(TAG,Thread.currentThread().getName());
                System.out.println("home-->");
                mDevice.pressHome();
            }catch (Exception e){
                LogUtil.logError(e);
            }

        }
    }

}
