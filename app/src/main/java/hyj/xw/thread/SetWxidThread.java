package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Map;

import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.ParseRootUtil;


/**
 * Created by asus on 2017/8/20.
 */

public class SetWxidThread implements Runnable {
    public static final String TAG = "SetWxidThread";
    AccessibilityService context;
    Map<String,String> record;
    Wx008Data currentWx008Data;
    public SetWxidThread(AccessibilityService context, Map<String,String> record, Wx008Data currentWx008Data){
        this.context = context;
        this.record = record;
        this.currentWx008Data = currentWx008Data;
    }
    String wxid="";
    @Override
    public void run() {
        while (true){
            AutoUtil.sleep(1500);
            if(!AutoUtil.actionContains(record,"SetWxidThread")&&!AutoUtil.checkAction(record,"1")) continue;

            LogUtil.d("SetWxidThread","【SetWxidThread...】"+Thread.currentThread().getName()+" phone:"+record.get("phone"));
            AccessibilityNodeInfo root = context.getRootInActiveWindow();
            if(root==null){
                LogUtil.d("SetWxidThread","SetWxidThread root is null");
                AutoUtil.sleep(500);
                continue;
            }
            ParseRootUtil.debugRoot(root);

            if(root.getPackageName().toString().indexOf("tencent")==-1) continue;


            AccessibilityNodeInfo node0 = ParseRootUtil.getNodeByPathAndText(root,"0020","你的微信号成功设置为");
            if(node0!=null){
                LogUtil.d("【SetWxidThread更新微信号】",wxid);
                currentWx008Data.setWxId(wxid);
                if(currentWx008Data.updateAll("guid=?",currentWx008Data.getGuid())==1){
                    AutoUtil.recordAndLog(record,"wx登陆成功");
                    LogUtil.d("【SetWxidThread更新微信号成功】",wxid);
                    return;
                };
            }

            if(!AutoUtil.checkAction(record,"SetWxidThread点击设置")){
                AccessibilityNodeInfo node1 = ParseRootUtil.getNodeByPathAndText(root,"040","我");
                AutoUtil.performClick(node1,record,"SetWxidThread点击我");
            }

            AccessibilityNodeInfo node22 = ParseRootUtil.getNodeByPathAndText(root,"00470","设置");
            AccessibilityNodeInfo node2 = ParseRootUtil.getNodeByPathAndText(root,"00570","设置");
            AccessibilityNodeInfo node21 = ParseRootUtil.getNodeByPathAndText(root,"00670","设置");
            AutoUtil.performClick(node2,record,"SetWxidThread点击设置");
            AutoUtil.performClick(node21,record,"SetWxidThread点击设置");
            AutoUtil.performClick(node22,record,"SetWxidThread点击设置");

            AccessibilityNodeInfo node3 = ParseRootUtil.getNodeByPathAndText(root,"00260","帐号与安全");
            AutoUtil.performClick(node3,record,"SetWxidThread点击账号安全");

            AccessibilityNodeInfo node4 = ParseRootUtil.getNodeByPathAndText(root,"00210","微信号");
            AccessibilityNodeInfo node41 = ParseRootUtil.getNodePath(root,"00211");
            LogUtil.d("SetWxidThread node4",node4+"");
            LogUtil.d("SetWxidThread node41",node41+"");
            if(node4!=null&&node41!=null&&!"未设置".equals(node41.getText()+"")){
                //已经设置，但是wxid没记录到数据库
                AccessibilityNodeInfo wxidNode = ParseRootUtil.getNodePath(root,"00211");
                if(wxidNode!=null&&(wxidNode.getText()+"").length()>5&&currentWx008Data.getWxId()==null){
                    String wx = wxidNode.getText()+"";
                    currentWx008Data.setWxId(wx);
                    if(currentWx008Data.updateAll("guid=?",currentWx008Data.getGuid())==1){
                        LogUtil.d("【SetWxidThread更新微信号成功-原本未插入数据库】",wxid);
                    };
                }
                AutoUtil.recordAndLog(record,"wx登陆成功");
                return;
            }
            AutoUtil.performClick(node4,record,"SetWxidThread点击微信号");

            if(AutoUtil.checkAction(record,"SetWxidThread点击微信号")||AutoUtil.checkAction(record,"SetWxidThread点击确认微信号已存在")){
                AccessibilityNodeInfo node5 = ParseRootUtil.getNodePath(root,"0032");
                wxid = createWxid();
                LogUtil.d("SetWxidThread输入微信号",wxid);
                AutoUtil.performSetText(node5,wxid,record,"SetWxidThread输入微信号");

            }

            if(AutoUtil.checkAction(record,"SetWxidThread输入微信号")){
                AccessibilityNodeInfo node6 = ParseRootUtil.getNodePath(root,"002");
                AutoUtil.performClick(node6,record,"SetWxidThread保存微信号");
            }

            AccessibilityNodeInfo node7 = ParseRootUtil.getNodeByPathAndText(root,"03","确定");
            AutoUtil.performClick(node7,record,"SetWxidThread点击确认微信号");

            AccessibilityNodeInfo node8 = ParseRootUtil.getNodeByPathAndText(root,"01","确定");
            AutoUtil.performClick(node8,record,"SetWxidThread点击确认微信号已存在");

        }
    }

    private int getNextNum(int num){
        num = num+1;
        while (String.valueOf(num).indexOf("0")>-1||String.valueOf(num).indexOf("1")>-1){
            num = num+1;
        }
        return num;
    }

    private  String createAEOU() {
        String chars = "aeiu";
        String str = chars.charAt((int)(Math.random() * 4))+"";
        return str;
    }

    private  String createZM() {
        String chars = "abcdefghijkmnpqrstuvwxyz";
        String str = chars.charAt((int)(Math.random() * 24))+"";
        return str;
    }
    private  String createSJ() {
        String chars = "23456789";
        String str = chars.charAt((int)(Math.random() * 8))+"";
        return str;
    }

    private  String createWxid() {
        String str = "";
        str = createZM()+createZM()+createZM()+createSJ()+createSJ()+createSJ();
        return str;
    }

}
