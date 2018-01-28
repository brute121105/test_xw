package hyj.xw.util;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.Map;

/**
 * Created by asus on 2017/12/17.
 */

public class NodeActionUtil {

    //窗口包含当期全部字符str2，有一个不满足就返回false
    public static boolean isContainsStrs(AccessibilityNodeInfo root,String str2){
        String str1 = ParseRootUtil.getCurrentViewAllTexts(root);
        boolean flag = true;
        String[] str2s = str2.split("\\|");
        for(String str :str2s){
            if(!str1.contains(str)){
                flag = false;
                break;
            }
        }
        return flag;
    }
    //窗口只要包含就返回true
    public static boolean isWindowContainStr(AccessibilityNodeInfo root,String str2){
        String str1 = ParseRootUtil.getCurrentViewAllTexts(root);
        return str1.contains(str2);
    }

    public static boolean doClickByNodePathAndText(AccessibilityNodeInfo root,String str2,String nodePath,String nodeText,Map<String,String> record,String action,long ms){
        boolean isClick = false;
        boolean isContainsStrs = isContainsStrs(root,str2);
        AccessibilityNodeInfo nodeInfo = null;
        if(nodeText!=null){
            nodeInfo = ParseRootUtil.getNodeByPathAndText(root,nodePath,nodeText);
        }
        if(nodeInfo==null){
            nodeInfo = ParseRootUtil.getNodePath(root,nodePath);
        }
        if(nodeInfo==null){
             LogUtil.d("NodeActionUtil","doClickByNodePathAndText nodeText获取-->"+nodeText);
            nodeInfo = AutoUtil.findNodeInfosByText(root,nodeText);
        }
        if(isContainsStrs){
            isClick = AutoUtil.performClick(nodeInfo,record,action,ms);
        }
         LogUtil.d("NodeActionUtil","doClickByNodePathAndText -->"+str2);
         LogUtil.d("NodeActionUtil","doClickByNodePathAndText isContainsStrs -->"+isContainsStrs);
         LogUtil.d("NodeActionUtil","doClickByNodePathAndText nodeInfo -->"+nodeInfo);
         LogUtil.d("NodeActionUtil","doClickByNodePathAndText isclick -->"+isClick+" "+action);
        return isClick;
    }
    public static boolean doBack(AccessibilityService context,AccessibilityNodeInfo root, String str2, Map<String,String> record, String action, long ms){
        boolean isClick = false;
        boolean isContainsStrs = isContainsStrs(root,str2);
        if(isContainsStrs){
            AutoUtil.performBack(context,record,action);
        }
         LogUtil.d("NodeActionUtil","doClickByNodePathAndText -->"+str2);
         LogUtil.d("NodeActionUtil","doClickByNodePathAndText isContainsStrs -->"+isContainsStrs);
        return isClick;
    }
    public static boolean doClickByNodePathAndText(AccessibilityNodeInfo root,String str2,String nodePath,String nodeText,Map<String,String> record,String action){
        boolean isClick = false;
        boolean isContainsStrs = isContainsStrs(root,str2);
        AccessibilityNodeInfo nodeInfo = null;
        if(nodeText!=null){
            nodeInfo = ParseRootUtil.getNodeByPathAndText(root,nodePath,nodeText);
        }else {
            nodeInfo = ParseRootUtil.getNodePath(root,nodePath);
        }
        if(nodeInfo==null){
            LogUtil.d("NodeActionUtil","doClickByNodePathAndText nodeText获取-->"+nodeText);
            nodeInfo = AutoUtil.findNodeInfosByText(root,nodeText);
        }
        if(isContainsStrs){
            isClick = AutoUtil.performClick(nodeInfo,record,action);
        }
         LogUtil.d("NodeActionUtil","doClickByNodePathAndText -->"+str2);
         LogUtil.d("NodeActionUtil","doClickByNodePathAndText isContainsStrs -->"+isContainsStrs);
         LogUtil.d("NodeActionUtil","doClickByNodePathAndText nodeInfo -->"+nodeInfo);
         LogUtil.d("NodeActionUtil","doClickByNodePathAndText isclick -->"+isClick+" "+action);
        return isClick;
    }
    public static boolean doInputByNodePathAndText(AccessibilityNodeInfo root,String str2,String nodePath,String inputStr,Map<String,String> record,String action,long ms){
        boolean isInput = false;
        boolean isContainsStrs = isContainsStrs(root,str2);
        AccessibilityNodeInfo nodeInfo = ParseRootUtil.getNodePath(root,nodePath);
        if(isContainsStrs){
            isInput = AutoUtil.performSetText(nodeInfo,inputStr,record,action);
            if(isInput){
                AutoUtil.sleep(ms);
            }
        }
         LogUtil.d("NodeActionUtil","doClickByNodePathAndText -->"+str2);
         LogUtil.d("NodeActionUtil","doClickByNodePathAndText isContainsStrs -->"+isContainsStrs);
         LogUtil.d("NodeActionUtil","doClickByNodePathAndText nodeInfo -->"+nodeInfo);
         LogUtil.d("NodeActionUtil","doClickByNodePathAndText isclick -->"+isInput+" "+action+"  >"+inputStr);
        return isInput;
    }

   public static String getTextByNodePath(AccessibilityNodeInfo root,String nodePath){
       String str ="";
       AccessibilityNodeInfo node = ParseRootUtil.getNodePath(root,nodePath);
       if(node!=null){
           str = node.getText()+"";
       }
       return str;
   }
}
