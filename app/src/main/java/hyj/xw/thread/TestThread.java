package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;
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

public class TestThread extends BaseThread {

    public  final String TAG = this.getClass().getSimpleName();
    public TestThread(AccessibilityService context, Map<String, String> record,AccessibilityParameters parameters){
        super(context,record,parameters);
    }
    @Override
    public Object call()  {
        while (true){
            AutoUtil.sleep(2000);
            LogUtil.d(TAG,Thread.currentThread().getName());
            AccessibilityNodeInfo root = context.getRootInActiveWindow();
            if(root==null){
                LogUtil.d(TAG,"root is null");
                AutoUtil.sleep(500);
                continue;
            }

            if(parameters.getIsStop()==1){
                LogUtil.d(TAG,"暂停....");
                continue;
            }

            System.out.println("deb==================================");
             ParseRootUtil.debugRoot(root);
            //ParseRootUtil.getCurrentViewAllNode(root);

            NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","030","发现",record,"sendFr点击发现",500);
            NodeActionUtil.doClickByNodePathAndText(root,"扫一扫|摇一摇","00310","朋友圈",record,"sendFr点击朋友圈",500);
            NodeActionUtil.doClickByNodePathAndText(root,"轻触更换主题照片|朋友圈封面，再点一次可以改封面","00310","朋友圈",record,"sendFr点击朋友圈",500);
            if(AutoUtil.checkAction(record,"sendFr点击朋友圈")&&NodeActionUtil.isWindowContainStr(root,"朋友圈封面")){
                AccessibilityNodeInfo node6 = ParseRootUtil.getNodePath(root,"002");
                System.out.println("node6-->"+node6);
                node6.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
            }
            NodeActionUtil.doClickByNodePathAndText(root,"长按拍照按钮发文字，为内部体验功能。后续版本可能取消，也有可能保留，请勿过于依赖此方法。|我知道了","001","我知道了",record,"sendFr点击我知道了",500);
            NodeActionUtil.doInputByNodePathAndText(root,"这一刻的想法...|谁可以看","0000","tt3",record,"sendFr输入发送内容",1000);
            NodeActionUtil.doClickByNodePathAndText(root,"这一刻的想法...|谁可以看","03","发送",record,"sendFr点击发送",500);



        }
    }

}
