package hyj.xw;

import android.accessibilityservice.AccessibilityService;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;
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
    //综合参数
    String extValue = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_EXT);

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        parameters.setIsStop(0);
        System.out.println("008-->extValue:"+extValue);
        //AutoUtil.startWx();
        if("008".equals(extValue)){
            executorService.submit(ThreadFactory.getThread("fetch008Data",this,record,parameters));
        }else {
            /**
             * 非综合参数动作
             */
            if("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_FEED))){
                //养号
                executorService.submit(ThreadFactory.getThread("feed",this,record,parameters));
                AutoUtil.startWx();
            }else{
                //注册
                executorService.submit(ThreadFactory.getThread("reg",this,record,parameters));
                executorService.submit(ThreadFactory.getThread("alzAPI",this,record,parameters));
                AutoUtil.startWx();
            }
        }

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
