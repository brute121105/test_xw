package hyj.xw;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.factory.ThreadFactory;
import hyj.xw.flowWindow.MyWindowManager;
import hyj.xw.model.AccessibilityParameters;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.ParseRootUtil;

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
        AutoUtil.execShell("input keyevent 3");
        parameters.setIsStop(0);

        //executorService.submit(ThreadFactory.getThread("monitorMessageThread",this,record,parameters));
        //executorService.submit(ThreadFactory.getThread("autoOperation",this,record,parameters));
        //executorService.submit(ThreadFactory.getThread("recover008Data",this,record,parameters));
        executorService.submit(ThreadFactory.getThread("sendAccessMsgThread",this,record,parameters));



        //2018年8月3日15:58:16 住宿
        /*parameters.setStartLoginIndex(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX));
        System.out.println("008-->extValue:"+extValue);
        //养号
        if("008".equals(extValue)){//生成数据
            executorService.submit(ThreadFactory.getThread("fetch008Data",this,record,parameters));
            AutoUtil.startAppByPackName("com.soft.apk008v","com.soft.apk008.LoadActivity");
        }else if("0081".equals(extValue)){//抓取历史
            executorService.submit(ThreadFactory.getThread("fetch008DataHistory",this,record,parameters));
            AutoUtil.startAppByPackName("com.soft.apk008v","com.soft.apk008.LoadActivity");
        }else if("0082---".equals(extValue)){//还原008数据
            executorService.submit(ThreadFactory.getThread("recover008Data",this,record,parameters));
            AutoUtil.startAppByPackName("com.soft.apk008v","com.soft.apk008.LoadActivity");
        }else{
            //executorService.submit(ThreadFactory.getThread("feed",this,record,parameters));
            executorService.submit(ThreadFactory.getThread("autoOperation",this,record,parameters));
            if(extValue.contains("0082")){
                executorService.submit(ThreadFactory.getThread("recover008Data",this,record,parameters));
            }
        }*/


        //很早前注释
        //AutoUtil.startWx();
       /* if("008".equals(extValue)){
            executorService.submit(ThreadFactory.getThread("fetch008Data",this,record,parameters));
            AutoUtil.startAppByPackName("com.soft.apk008v","com.soft.apk008.LoadActivity");
        }else if("001".equals(extValue)){
            executorService.submit(ThreadFactory.getThread("login",this,record,parameters));
        }else if("test".equals(extValue)){
            executorService.submit(ThreadFactory.getThread("test",this,record,parameters));
            AutoUtil.startWx();
        }
        else {
            *//**
             * 非综合参数动作
             *//*
            if("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_FEED))){
                //养号
                executorService.submit(ThreadFactory.getThread("feed",this,record,parameters));
                AutoUtil.startWx();
            }else{
                //注册
                executorService.submit(ThreadFactory.getThread("reg",this,record,parameters));
                //executorService.submit(ThreadFactory.getThread("alzAPI",this,record,parameters));
                //AutoUtil.startWx();
            }
        }*/

    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {

        LogUtil.d("AccessibilityService","accessibilityEvent:"+accessibilityEvent.getEventType());
        MyWindowManager.updateFlowMsg(getFlowMsg(record));
        AccessibilityNodeInfo root = getRootInActiveWindow();
        if(root!=null){
            AccessibilityNodeInfo node = ParseRootUtil.getNodePath(root,"00221");
            if(node!=null){
                System.out.println("node--->"+node.getContentDescription());
                /*Rect rect = new Rect();
                node.getBoundsInScreen(rect);
                System.out.println(" Rect node--->"+rect.centerX()+" y:"+rect.centerY());
                AutoUtil.clickXY(rect.centerX(),rect.centerY());*/
            }
        }

    }
    private String getFlowMsg(Map<String,String> record){
        String total = record.get("total");
        String loginIndex = record.get("loginIndex");
        String msg1 = loginIndex+"/"+total;
        String msg = msg1+"\n"+record.get("recordAction");
        return msg;
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
