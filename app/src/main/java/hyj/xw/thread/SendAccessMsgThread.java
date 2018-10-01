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
import hyj.xw.common.FilePathCommon;
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
    String lastAction  = "";
    int countSameActionNum = 0;//记录重复动作次数

    @Override
    public Object call()  {
        openLongConn();
        while (true){
            try {
                AutoUtil.sleep(500);
                LogUtil.d(TAG,Thread.currentThread().getName()+" doSendAction:"+doSendAction+" helpLoginMsg:"+helpLoginMsg+" countSameActionNum:"+countSameActionNum);
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
                /**
                 * 记录重复动作
                 */
                if(!"init".equals(doSendAction)&&lastAction.equals(doSendAction)){
                    countSameActionNum = countSameActionNum+1;
                    if(countSameActionNum>30){
                        countSameActionNum = 0;
                        doSendAction = "init";//超时，复位
                        LogUtil.recordDoActionByHour(FilePathCommon.sendAccessLogDir,"countSameActionNum超过30，重置为init");
                    }
                }else {
                    lastAction = doSendAction;
                    countSameActionNum = 0;
                }


                 //ParseRootUtil.debugRoot(root);
                 //if(true) continue;
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

    private void openLongConn(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String deviceNum = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_DEVICE);
                String host = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_HOST);
                String fzWxNum = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_FZWXNUM);
                if(host.contains(":")){
                    host = host.substring(0,host.indexOf(":"));
                }
                String msg = "doAction--->deviceNum:"+deviceNum+" host:"+host+" 辅助号："+fzWxNum;
                LogUtil.recordDoActionByHour(FilePathCommon.sendAccessLogDir,msg);
                NettyClient client = new NettyClient(host, 8000, deviceNum,fzWxNum);
                try {
                    client.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public boolean doSend(AccessibilityNodeInfo root){

        //处理异常，点击搜索结果手出来多条记录
        if("点击搜索结果".equals(doSendAction)){
            AccessibilityNodeInfo nodeInfo = ParseRootUtil.getNodePath(root,"00500");// "xxx"的记录 节点
            if(nodeInfo!=null&&nodeInfo.getText()!=null&&nodeInfo.getText().toString().contains("的记录")){
                AutoUtil.clickXY(540,445);//点击搜索出来第一个聊天记录
                LogUtil.recordDoActionByHour(FilePathCommon.sendAccessLogDir,"处理异常，点击搜索出来多个结果");
            }
        }

        //1、点击搜索圈
        if("init".equals(doSendAction)){
            AccessibilityNodeInfo nodeInfo1 = ParseRootUtil.getNodePath(root,"006");//搜索圈
            if(nodeInfo1==null){
                nodeInfo1 = ParseRootUtil.getNodePath(root,"06");//搜索圈
            }
            if(nodeInfo1!=null&&(nodeInfo1.getContentDescription()+"").equals("搜索")){
                if(WindowOperationUtil.performClickTest(nodeInfo1)){
                    doSendAction = "点击搜索";
                    LogUtil.recordDoActionByHour(FilePathCommon.sendAccessLogDir,doSendAction);
                }
            }
        }

        //2、输入搜索内容
        AccessibilityNodeInfo nodeInfo2 = ParseRootUtil.getNodePath(root,"02");//搜索内容输入框
        if(nodeInfo2!=null&&nodeInfo2.getText()!=null&&nodeInfo2.getText().toString().equals("搜索")){
            if(WindowOperationUtil.performSetTextTest(nodeInfo2,helpLoginMsg.getPhone())){
                doSendAction = "输入搜索内容";
                LogUtil.recordDoActionByHour(FilePathCommon.sendAccessLogDir,doSendAction);
            }
        }
        //3、点击搜索结果
        if("输入搜索内容".equals(doSendAction)){
            AccessibilityNodeInfo nodeInfo3 = AutoUtil.findNodeInfosByText(root,"微信号: "+helpLoginMsg.getPhone());
            AccessibilityNodeInfo nodeInfo4 = AutoUtil.findNodeInfosByText(root,"聊天记录");
            AccessibilityNodeInfo nodeInfo5 = AutoUtil.findNodeInfosByText(root,"联系人");
            AccessibilityNodeInfo nodeInfo6 = AutoUtil.findNodeInfosByText(root,"最常使用");
            if(WindowOperationUtil.performClickTest(nodeInfo3)){
                doSendAction = "点击搜索结果";
                LogUtil.recordDoActionByHour(FilePathCommon.sendAccessLogDir,doSendAction);
            }else if(nodeInfo4!=null||nodeInfo5!=null||nodeInfo6!=null){
                AutoUtil.clickXY(540,445);//点击搜索出来第一个聊天记录
                doSendAction = "点击搜索结果";
                LogUtil.recordDoActionByHour(FilePathCommon.sendAccessLogDir,doSendAction);
            }
            else {//结果未搜索到

                AccessibilityNodeInfo nodeInfo31 = AutoUtil.findNodeInfosByText(root,"查找手机/QQ号:"+helpLoginMsg.getPhone());
                AccessibilityNodeInfo nodeInfo32 = AutoUtil.findNodeInfosByText(root,"查找微信号:"+helpLoginMsg.getPhone());
                if(nodeInfo31!=null||nodeInfo32!=null){
                    LogUtil.recordDoActionByHour(FilePathCommon.sendAccessLogDir,"未搜索到微信号，微信号可能没添加好友");
                    back2MainPage(root);//确保100%返回主界面
                    doSendAction = "点击返回主界面";
                    LogUtil.recordDoActionByHour(FilePathCommon.sendAccessLogDir,doSendAction);
                }

            }
        }



        //4、输入发送内容
        AccessibilityNodeInfo nodeInfo4 = ParseRootUtil.getNodePath(root,"0004010");//发送消息输入框
        if(nodeInfo4==null){
            nodeInfo4 = ParseRootUtil.getNodePath(root,"000410");//发送消息输入框
        }
        if(nodeInfo4==null){
            nodeInfo4 = ParseRootUtil.getNodePath(root,"000110");//发送消息输入框
        }
        if(nodeInfo4==null){
            nodeInfo4 = AutoUtil.findNodeInfosById(root,"com.tencent.mm:id/aep");//发送消息输入框 6.7.2版本
        }

        if(nodeInfo4==null){
            nodeInfo4 = ParseRootUtil.getNodePath(root,"00007010");//发送消息输入框
        }
        if(nodeInfo4==null){
            nodeInfo4 = ParseRootUtil.getNodePath(root,"0000710");//发送消息输入框
        }
        if(WindowOperationUtil.performSetTextTest(nodeInfo4,helpLoginMsg.getCode())) {
            doSendAction = "输入发送内容";
            LogUtil.recordDoActionByHour(FilePathCommon.sendAccessLogDir,doSendAction);
        }

        //if(true) return false;

        //5、点击发送
        AccessibilityNodeInfo nodeInfo7 = AutoUtil.findNodeInfosByText(root,"发送");
        boolean flag = WindowOperationUtil.performClickTest(nodeInfo7);
        if(flag) {
            doSendAction = "点击发送";
            LogUtil.recordDoActionByHour(FilePathCommon.sendAccessLogDir,doSendAction);
        }

        //6、如果点击发送为true，点击左上角返回主界面 2次
        if(flag){
            back2MainPage(root);//确保100%返回主界面
            doSendAction = "点击返回主界面";
            LogUtil.recordDoActionByHour(FilePathCommon.sendAccessLogDir,doSendAction);
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
                        LogUtil.recordDoActionByHour(FilePathCommon.sendAccessLogDir,"发送成功");
                        return true;
                    }else {
                        LogUtil.recordDoActionByHour(FilePathCommon.sendAccessLogDir,"发送失败1");
                        return false;
                    }
                }else {
                    LogUtil.recordDoActionByHour(FilePathCommon.sendAccessLogDir,"发送失败2");
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
        while (!allText.contains("发现")){
            AutoUtil.clickXY(67,133);//点击左上角返回,避免上次返回不成功
            LogUtil.recordDoActionByHour(FilePathCommon.sendAccessLogDir,"点击了（67,133）点击返回主界面");
            root = context.getRootInActiveWindow();
            if(root==null){
                AutoUtil.sleep(500);
                continue;
            }
            allText = ParseRootUtil.getCurrentViewAllTexts(root);
            AutoUtil.sleep(1000);
        }
    }

    public HelpLoginMsg getHelpLoginMsg(){
        HelpLoginMsg helpLoginMsg = new HelpLoginMsg();
        helpLoginMsg.setPhone("");
        return helpLoginMsg;
    }
}
