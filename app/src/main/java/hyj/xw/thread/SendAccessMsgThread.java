package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSONObject;
import com.wx.wyyk.netty.client.NettyClient;
import com.wx.wyyk.netty.commons.HelpLoginMsg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import hyj.xw.BaseThread;
import hyj.xw.cache.CacheMsg;
import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.model.AccessibilityParameters;
import hyj.xw.modelHttp.ExpPntMessage;
import hyj.xw.service.HttpRequestService;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.ParseRootUtil;
import hyj.xw.util.WindowOperationUtil;

/**
 * 辅助发送消息，适配wx 6.6.6
 */
public class SendAccessMsgThread extends BaseThread {

    public  final String TAG = this.getClass().getSimpleName();
    public SendAccessMsgThread(AccessibilityService context, Map<String, String> record, AccessibilityParameters parameters){
        super(context,record,parameters);
    }


    private static final String resId1 = "com.tencent.mm:id/apx";//发送内容历史记录节点
    private static final String resId2 = "com.tencent.mm:id/apw";//发送内容右边时间节点

    HelpLoginMsg helpLoginMsg = null;
    String doSendAction = "init";

    @Override
    public Object call()  {
        while (true){
            try {
                AutoUtil.sleep(1000);
                LogUtil.d(TAG,Thread.currentThread().getName()+" doSendAction:"+doSendAction+" helpLoginMsg:"+helpLoginMsg);
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

                ParseRootUtil.debugRoot(root);
                //确保init状态是在主界面
                if("init".equals(doSendAction)){
                    back2MainPage(root);
                }
                /**
                 * 1、获取服务端消息
                 * 2、执行发送脚本
                 */
                if(helpLoginMsg==null){
                    AutoUtil.sleep(1000);
                    helpLoginMsg = CacheMsg.getHelpLoginMsg();//1、获取服务端消息
                    if(helpLoginMsg==null){
                        System.out.println("doAction---->没有获取服务器微信消息helpLoginMsg");
                    }
                }else {
                    doSend(root);//2、执行发送脚本
                }


            }catch (Exception e){
                e.printStackTrace();
                LogUtil.logError(e);
            }
        }
    }

    public boolean doSend(AccessibilityNodeInfo root){
        //1、点击搜索圈
        if("init".equals(doSendAction)){
            AccessibilityNodeInfo nodeInfo1 = ParseRootUtil.getNodePath(root,"006");//搜索圈
            if(nodeInfo1==null){
                nodeInfo1 = ParseRootUtil.getNodePath(root,"06");//搜索圈
            }
            if(nodeInfo1!=null&&(nodeInfo1.getContentDescription()+"").equals("搜索")){
                if(WindowOperationUtil.performClickTest(nodeInfo1)) doSendAction = "点击搜索";
            }
        }

        //2、输入搜索内容
        AccessibilityNodeInfo nodeInfo2 = ParseRootUtil.getNodePath(root,"02");//搜索内容输入框
        if(nodeInfo2!=null&&nodeInfo2.getText()!=null&&nodeInfo2.getText().toString().equals("搜索")){
            if(WindowOperationUtil.performSetTextTest(nodeInfo2,helpLoginMsg.getPhone())){
                doSendAction = "输入搜索内容";
            }
        }
        //3、点击搜索结果
        if("输入搜索内容".equals(doSendAction)){
            AccessibilityNodeInfo nodeInfo3 = AutoUtil.findNodeInfosByText(root,"微信号: "+helpLoginMsg.getPhone());
            if(WindowOperationUtil.performClickTest(nodeInfo3)){
                doSendAction = "点击搜索结果";
            }else {//结果未搜索到
                AccessibilityNodeInfo nodeInfo31 = AutoUtil.findNodeInfosByText(root,"查找手机/QQ号:"+helpLoginMsg.getPhone());
                AccessibilityNodeInfo nodeInfo32 = AutoUtil.findNodeInfosByText(root,"查找微信号:"+helpLoginMsg.getPhone());
                if(nodeInfo31!=null||nodeInfo32!=null){
                    System.out.println("doAction--->未搜索到微信号，微信号可能没添加好友");
                    back2MainPage(root);//确保100%返回主界面
                    doSendAction = "点击返回主界面";
                }

            }
        }

        //4、输入发送内容
        AccessibilityNodeInfo nodeInfo4 = ParseRootUtil.getNodePath(root,"0004010");//发送消息输入框
        if(WindowOperationUtil.performSetTextTest(nodeInfo4,helpLoginMsg.getCode())) doSendAction = "输入发送内容";

        //5、点击发送
        AccessibilityNodeInfo nodeInfo7 = AutoUtil.findNodeInfosByText(root,"发送");
        boolean flag = WindowOperationUtil.performClickTest(nodeInfo7);
        if(flag) doSendAction = "点击发送";

        //6、如果点击发送为true，点击左上角返回主界面 2次
        if(flag){
            back2MainPage(root);//确保100%返回主界面
            doSendAction = "点击返回主界面";
        }

        //7、初始化doSendAction为init 判断发送内容是否成功，目前只判断 是否有正在发送字眼，有则发送不成功
        //8、清空helpLoginMsg=null
        if("点击返回主界面".equals(doSendAction)){
            String allText = ParseRootUtil.getCurrentViewAllTexts(root);
            if(allText.contains("通讯录")){
                doSendAction = "init";
                helpLoginMsg = null;
                List<AccessibilityNodeInfo> nodes1 = root.findAccessibilityNodeInfosByViewId(resId1);
                List<AccessibilityNodeInfo> nodes2 = root.findAccessibilityNodeInfosByViewId(resId2);
                if(nodes1.size()>0&&nodes2.size()>0){
                    String text1 = nodes1.get(0).getText()+"";
                    String text2 = nodes2.get(0).getText()+"";
                    System.out.println("doAction-->text:"+text1+" text2:"+text2);
                    if(helpLoginMsg.getCode().equals(text1)&&!text2.contains("发送中")){
                        System.out.println("doAction--->发送成功");
                        return true;
                    }else {
                        System.out.println("doAction--->发送失败1");
                        return false;
                    }
                }else {
                    System.out.println("doAction--->发送失败2");
                    return false;
                }
            }
        }
        return false;
    }
    //点击左上角返回
    public void back2MainPage(AccessibilityNodeInfo root){
        if(!root.getPackageName().toString().contains("tencent")) return;
        String allText = ParseRootUtil.getCurrentViewAllTexts(root);
        while (!allText.contains("通讯录")){
            AutoUtil.clickXY(67,133);//点击左上角返回,避免上次返回不成功
            System.out.println("doAction--->点击了（67,133）点击返回主界面");
            root = context.getRootInActiveWindow();
            if(root==null){
                AutoUtil.sleep(500);
                continue;
            }
            allText = ParseRootUtil.getCurrentViewAllTexts(root);
            AutoUtil.sleep(1000);
        }
    }
}
