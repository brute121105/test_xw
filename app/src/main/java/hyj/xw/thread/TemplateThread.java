package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Map;

import hyj.xw.BaseThread;
import hyj.xw.model.AccessibilityParameters;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.NodeActionUtil;
import hyj.xw.util.ParseRootUtil;

/**
 * Created by Administrator on 2017/12/14.
 */

public class TemplateThread extends BaseThread {
    private  int countRootNull =0;
    public  final String TAG = this.getClass().getSimpleName();
    public TemplateThread(AccessibilityService context, Map<String, String> record, AccessibilityParameters parameters){
        super(context,record,parameters);
    }
    private void intiParam(){

    }
    @Override
    public Object call() {
        while (true){
            try {
            AutoUtil.sleep(1500);
            LogUtil.d(TAG,Thread.currentThread().getName());
            if(parameters.getIsStop()==1){
                LogUtil.d(TAG,"暂停....");
                continue;
            }
            //保持屏幕常亮
            AutoUtil.wake();

            AccessibilityNodeInfo root = context.getRootInActiveWindow();
            //roor超过5次为空，启动wx
            if(root==null){
                LogUtil.d(TAG,"root is null");
                AutoUtil.sleep(1500);
                countRootNull  = countRootNull+1;
                if(countRootNull>5){
                    LogUtil.d(TAG,"root is max num:getPackageName1:");
                    AutoUtil.startWx();
                }
                continue;
            }
            //不在wx界面，启动wx
            if(root.getPackageName().toString().indexOf("tencent")==-1){
                LogUtil.d(TAG,"not in the weixin view getPackageName2:"+root.getPackageName());
                AutoUtil.startWx();
            }
            NodeActionUtil.doClickByNodePathAndText(root,"微信无响应。要将其关闭吗？|确定","01","等待",record,"exception",500);

            ParseRootUtil.debugRoot(root);
                
            }catch (Exception e){
              LogUtil.logError(e);
            }

        }
    }

}
