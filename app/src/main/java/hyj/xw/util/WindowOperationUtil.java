package hyj.xw.util;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

import hyj.xw.GlobalApplication;
import hyj.xw.aw.sysFileRp.CreatePhoneEnviroment;
import hyj.xw.common.FilePathCommon;
import hyj.xw.conf.PhoneConf;
import hyj.xw.hook.newHook.NewPhoneInfo;
import hyj.xw.model.WindowNodeInfo;

/**
 * Created by Administrator on 2018/6/10 0010.
 */

public class WindowOperationUtil {

   /* public static boolean doActions(AccessibilityNodeInfo root,List<WindowNodeInfo> wInfos){
        boolean flag = false;
        if(wInfos!=null&&wInfos.size()>0){
            for(WindowNodeInfo info:wInfos){
                flag = doAction(root,info);
                System.out.println("doActions-->:"+info.getActionDesc()+flag);
            }
        }
        return flag;
    }
    public static boolean doAction(AccessibilityNodeInfo root,WindowNodeInfo info){
        boolean flag = false;
        if("启动微信".equals(info.getActionDesc())){
            startWx();//启动微信
            flag = true;
        }else if("清除并准备改机环境".equals(info.getActionDesc())){
            AutoUtil.clearAppData();
            AutoUtil.sleep(2000);

            NewPhoneInfo pi = null;
            if(!TextUtils.isEmpty(info.getCurrentWx008Data().getPhoneStrsAw())){//aw数据
                pi = JSON.parseObject(info.getCurrentWx008Data().getPhoneStrsAw(),NewPhoneInfo.class);
            }else {
                pi = PhoneConf.xw2awData(info.getCurrentWx008Data());
            }
            pi.setCpuName(pi.getCpuName().trim().toLowerCase());
            CreatePhoneEnviroment.create(GlobalApplication.getContext(),pi);
            FileUtil.writeContent2FileForceUtf8(FilePathCommon.baseAppPathAW,FilePathCommon.npiFileName, JSON.toJSONString(pi));
            flag = true;
        }else if("判断登录成功".equals(info.getActionDesc())){
            if(NodeActionUtil.isContainsStrs(root,"通讯录|发现|我")){
                int cn = DaoUtil.updateExpMsg(info.getCurrentWx008Data(),"登录成功-"+AutoUtil.getCurrentDate());
                System.out.println("excpMsg-->"+cn);
                //LogUtil.login(loginIndex+" success",info.getCurrentWx008Data().getPhone()+" "+info.getCurrentWx008Data().getWxId()+" "+currentWx008Data.getWxPwd()+" ip:"+record.remove("ipMsg"));
            }
        }
        else if(1==info.getNodeType()){//按钮控件
            flag = performClick(getNodeByInfo(root,info),info);
        }else if(2==info.getNodeType()){//输入框控件
            flag = performSetText(getNodeByInfo(root,info),info);
        }
        return flag;
    }*/
    public static boolean performSetText(AccessibilityNodeInfo nodeInfo,WindowNodeInfo info){
        boolean flag = false;
        if(nodeInfo == null) return flag;
        if(nodeInfo.isEditable()){
            flag = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,createBuddleText(info.getInputText()));
        }else{
            flag = performSetText(nodeInfo.getParent(),info);
        }
        return flag;
    }
    //创建buggle文本
    public static Bundle createBuddleText(String inputText){
        Bundle inputContent = new Bundle();
        inputContent.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,inputText);
        return inputContent;
    }

    //执行点击、记录下次操作、并打印日志、休眠
    public static boolean performClick(AccessibilityNodeInfo nodeInfo,WindowNodeInfo info) {
        boolean isClick = false;
        if(nodeInfo == null)  return false;
        if(nodeInfo.isClickable()) {
            isClick = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            sleep(info.getActionSleepMs());
        } else {
            isClick = performClick(nodeInfo.getParent(),info);
        }
        return isClick;
    }
    public static boolean performClickTest(AccessibilityNodeInfo nodeInfo) {
        boolean isClick = false;
        if(nodeInfo == null)  return false;
        if(nodeInfo.isClickable()) {
            isClick = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            isClick = performClickTest(nodeInfo.getParent());
        }
        return isClick;
    }

    //休眠毫秒
    public static  void sleep(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static AccessibilityNodeInfo getNodeByInfo(AccessibilityNodeInfo root,WindowNodeInfo info){
        AccessibilityNodeInfo node = null;
        if(!TextUtils.isEmpty(info.getNodeId())){
            node = findNodeInfosById(root,info.getNodeId());
            info.setFindNodeResult("根据ID查找"+(node==null));
        }else if(!TextUtils.isEmpty(info.getNodeText())){
            node = findNodeInfosByText(root,info.getNodeText());
            info.setFindNodeResult("根据Text查找"+(node==null));
        }
        if(node==null&&!TextUtils.isEmpty(info.getNodePath())){
            node = ParseRootUtil.getNodePath(root,info.getNodePath());
            info.setFindNodeResult("根据Path查找"+(node==null));
        }
        System.out.println("getNodeByInfo--->"+node);
        return node;
    }
    //通过文本查找节点
    public static AccessibilityNodeInfo findNodeInfosByText(AccessibilityNodeInfo nodeInfo, String text) {
        if(nodeInfo==null) return null;
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
        if(list != null&&list.size()>0){
            for(AccessibilityNodeInfo node:list){
                if(text.equals(node.getText()+"")){
                    return node;
                }
            }
        }
        return null;
    }
    //通过文本查找节点
    public static AccessibilityNodeInfo findNodeInfosById(AccessibilityNodeInfo nodeInfo, String id) {
        if(nodeInfo==null) return null;
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(id);
        if(list == null || list.isEmpty()) return null;
        return list.get(0);
    }

    public static void startWx(){
        startAppByPackName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
    }
    public static void startAppByPackName(String packageName,String activity){
        Intent intent = new Intent();
        ComponentName cmp=new ComponentName(packageName,activity);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        GlobalApplication.getContext().startActivity(intent);
    }
}
