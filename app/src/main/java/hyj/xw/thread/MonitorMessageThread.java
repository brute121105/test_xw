package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;


import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import hyj.xw.BaseThread;
import hyj.xw.model.AccessibilityParameters;
import hyj.xw.modelHttp.ExpPntMessage;
import hyj.xw.service.HttpRequestService;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.ParseRootUtil;



public class MonitorMessageThread extends BaseThread {

    public  final String TAG = this.getClass().getSimpleName();
    public MonitorMessageThread(AccessibilityService context, Map<String, String> record, AccessibilityParameters parameters){
        super(context,record,parameters);
    }
    public static final String groupName = "微移管家";//群名称
    public static final String friendNameNodeId = "com.tencent.mm:id/apv";//好友名称
    public static final String setSendTextNodeId = "com.tencent.mm:id/aaa";//输入文本节点
    public static final String setSendTextNodeId1 = "com.tencent.mm:id/aac";//表情按钮，如果输入发送内容没出现发送按钮，点击它
    public static final String receiveMsgLinearLayoutNodesId = "com.tencent.mm:id/apt";//列表android.widget.ListView的直接下级android.widget.LinearLayout
    public static final String receiveMsgRedNodeId = "com.tencent.mm:id/jj";//消息红色点
    public static final String receiveMsgNodeId = "com.tencent.mm:id/apx";//消息内容节点

    public  List<String> sendMsgList = Collections.synchronizedList(new ArrayList<String>());
    @Override
    public Object call()  {
        new GetMsgThread().start();
        while (true){
            try {
                AutoUtil.sleep(1000);
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
                //ParseRootUtil.debugRoot(root);
                if(AutoUtil.findNodeInfosById(root,setSendTextNodeId)!=null){
                    AutoUtil.clickXY(67,133);//点击左上角返回,避免上次返回不成功
                    System.out.println("doAction-->补充返回click 67,133");
                }

                //收到消息并回复
                receiveMsgAndReply(root);
                //获取到服务器的信息，发送到群里
                if(sendMsgList.size()>0){
                    AccessibilityNodeInfo nodeInfo = findNodeByIdInNodesText(root,groupName);
                    System.out.println("doActin--size:"+sendMsgList.size());
                    clickNodeAndSendMsg(nodeInfo,sendMsgList.remove(0));
                    continue;
                }

                List<AccessibilityNodeInfo> nodeInfos = root.findAccessibilityNodeInfosByViewId(receiveMsgNodeId);
                if(nodeInfos.size()>0){
                    for(AccessibilityNodeInfo nodeInfo:nodeInfos){
                        System.out.println("nodeInfo-->"+nodeInfo.getText());
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
                LogUtil.logError(e);
            }
        }
    }

    public void receiveMsgAndReply(AccessibilityNodeInfo root){
        List<AccessibilityNodeInfo> newMsgs = root.findAccessibilityNodeInfosByViewId(receiveMsgLinearLayoutNodesId);//接受到新信息
        if(newMsgs!=null&&newMsgs.size()>0){
            for(AccessibilityNodeInfo newMsg:newMsgs){
                if(AutoUtil.findNodeInfosById(newMsg,receiveMsgRedNodeId)!=null){//只看消息红色点
                    String recMsg = getRecMsg(newMsg);//接受到消息内容
                    replyByRecMsg(newMsg,recMsg);//根据收到消息点击回复
                    return;
                }
            }
        }
    }

    //nodeInfo为listView的直接下级可点击节点
   public void replyByRecMsg(AccessibilityNodeInfo nodeInfo,String recMsg){
       System.out.println("doActioin--recMsg-->"+recMsg);
       String replyContent = "";
       if("1".equals(recMsg)){
           replyContent = "1";
       }else if("2".equals(recMsg)){
           replyContent = "2";
       }else if("exit".equals(recMsg)){
           AutoUtil.execShell("input keyevent 3");
       }
       if(!"".equals(replyContent)){
           clickNodeAndSendMsg(nodeInfo,replyContent);
       }
   }
    public String getRecMsg(AccessibilityNodeInfo nodeInfo){
        AccessibilityNodeInfo recMsgNode = AutoUtil.findNodeInfosById(nodeInfo,receiveMsgNodeId);
        if(recMsgNode==null) return "";
        String recMsg = recMsgNode.getText()+"";
        if(!recMsg.contains(": ")) return "";
        String realMsg = recMsg.substring(recMsg.lastIndexOf(":")+1).trim();
        return realMsg.trim();
    }

    public void clickNodeAndSendMsg(AccessibilityNodeInfo nodeInfo,String sendContent){
        if(AutoUtil.performClick(nodeInfo,record,"点击即将发送消息好友名称")){
            AccessibilityNodeInfo setTextNode = AutoUtil.findNodeInfosById(context.getRootInActiveWindow(),setSendTextNodeId);
            while (setTextNode==null){
                AutoUtil.sleep(200);
                setTextNode = AutoUtil.findNodeInfosById(context.getRootInActiveWindow(),setSendTextNodeId);
            }
            if(AutoUtil.performSetText(setTextNode,sendContent,record,"输入文本:"+sendContent)){
                boolean isSend = AutoUtil.performClick(AutoUtil.findNodeInfosByText(context.getRootInActiveWindow(),"发送"),record,"点击发送",500);
                while (!isSend){
                    AutoUtil.performClick(AutoUtil.findNodeInfosById(context.getRootInActiveWindow(),setSendTextNodeId1),record,"点击表情按钮" ,200);
                    isSend =AutoUtil.performClick(AutoUtil.findNodeInfosByText(context.getRootInActiveWindow(),"发送"),record,"点击发送",500);
                    //sendMsg="";
                }
                AutoUtil.clickXY(67,133);//点击左上角返回
            }
            AutoUtil.sleep(200);
        }

    }

    public boolean clickNodeByText(AccessibilityNodeInfo root,String nodeText){
        AccessibilityNodeInfo nodeInfo = AutoUtil.findNodeInfosByText(root,nodeText);
        System.out.println("doAction-->"+nodeInfo);
        return AutoUtil.performClick(nodeInfo,record,"点击"+nodeText);
    }

    public AccessibilityNodeInfo findNodeByIdInNodesText(AccessibilityNodeInfo root,String text){
        List<AccessibilityNodeInfo> nodes = root.findAccessibilityNodeInfosByViewId(friendNameNodeId);
        if(nodes!=null&&nodes.size()>0){
            for(AccessibilityNodeInfo node:nodes){
                if(text.equals(node.getText()+"")){
                    return node;
                }
            }
        }
        return null;
    }

    class GetMsgThread extends Thread{
        HttpRequestService service = new HttpRequestService();
        @Override
        public void run() {
            while (true){
                AutoUtil.sleep(5000);
                if(parameters.getIsStop()==1){
                    LogUtil.d(TAG,"暂停....");
                    continue;
                }
                String res = service.getOneExpMsg();
                System.out.println("doAction-->"+res);
                if(!"".equals(res)){
                    ExpPntMessage expPntMessage = JSONObject.parseObject(res,ExpPntMessage.class);
                    if(!TextUtils.isEmpty(expPntMessage.getMessage())){
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        String dateTime = sdf.format(expPntMessage.getCreateTime());
                        String sendMsg  = dateTime+" "+expPntMessage.getUser()+" "+expPntMessage.getMessage();
                        sendMsgList.add(sendMsg);
                    }
                }
            }
        }
    }
}
