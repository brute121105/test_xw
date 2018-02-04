package hyj.xw.util;

import android.accessibilityservice.AccessibilityService;
import android.app.KeyguardManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import hyj.xw.GlobalApplication;


/**
 * Created by asus on 2017/5/13.
 */

public class AutoUtil {
    //休眠毫秒
    public static  void sleep(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static boolean performSetText(AccessibilityNodeInfo nodeInfo,String text,Map<String,String> record,String recordAction){
        boolean flag = false;
        if(nodeInfo == null) return flag;
        if(nodeInfo.isEditable()){
            flag = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT,createBuddleText(text));
        }else{
            flag = performSetText(nodeInfo.getParent(),text,record,recordAction);
        }
        recordAndLog(record,recordAction);
        return flag;
    }
    //执行点击、记录下次操作、并打印日志、休眠
    public static boolean performClick(AccessibilityNodeInfo nodeInfo,Map<String,String> record, String recordAction, long ms) {
        boolean isClick = false;
        if(nodeInfo == null)  return false;
        if(nodeInfo.isClickable()) {
            isClick = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            if(!recordAction.contains("exception")){
                recordAndLog(record,recordAction);
            }
            sleep(ms);
        } else {
            isClick = performClick(nodeInfo.getParent(),record,recordAction,ms);
        }
        return isClick;
    }
    //执行点击、记录下次操作、并打印日志
    public static boolean performClick(AccessibilityNodeInfo nodeInfo,Map<String,String> record, String recordAction) {
        boolean isClick = false;
        if(nodeInfo == null)  return false;
        if(nodeInfo.isClickable()) {
            isClick = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            recordAndLog(record,recordAction);
        } else {
            isClick = performClick(nodeInfo.getParent(),record,recordAction);
        }
        return isClick;
    }
    public static void performScroll(AccessibilityNodeInfo nodeInfo,Map<String,String> record, String recordAction) {
        if(nodeInfo == null)  return;
        if(nodeInfo.isScrollable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            recordAndLog(record,recordAction);
        } else {
            performScroll(nodeInfo.getParent(),record,recordAction);
        }
    }
    //执行点击、记录下次操作、并打印日志
    public static void performClickAndExpect(AccessibilityNodeInfo nodeInfo,Map<String,String> record, String recordAction
            ,AccessibilityService context,AccessibilityNodeInfo rootNode,String id,String text) {
        performClick(nodeInfo,record,recordAction);
        if(rootNode==null) return;
        rootNode = context.getRootInActiveWindow();
        if(id!=null&&text!=null){

        }

    }
    //通过文本查找节点
    public static AccessibilityNodeInfo findNodeInfosByText(AccessibilityNodeInfo nodeInfo, String text) {
        if(nodeInfo==null) return null;
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
        if(list == null || list.isEmpty()) return null;
        return list.get(0);
    }
    //通过文本查找节点
    public static AccessibilityNodeInfo findNodeInfosById(AccessibilityNodeInfo nodeInfo, String id) {
        if(nodeInfo==null) return null;
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(id);
        if(list == null || list.isEmpty()) return null;
        return list.get(0);
    }
    //返回
    public static void performBack(AccessibilityService service,Map<String,String> record, String recordAction) {
        if(service == null)  return;
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        recordAndLog(record,recordAction);
        //record.put("recordAction",recordAction);
        //System.out.println("------>"+record);
    }
    //根据id和text查找节点
    public static AccessibilityNodeInfo fineNodeByIdAndText(AccessibilityNodeInfo nodeInfo,String id,String text){
        AccessibilityNodeInfo result = null;
        if(nodeInfo==null) return result;
        List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByViewId(id);
        if(nodes == null || nodes.isEmpty()) return result;
        for(AccessibilityNodeInfo node:nodes){
            String name = node.getText()+"";
            if(name.equals(text)){
                result = node;
                break;
            }
        }
        return result;
    }
    //根据id和text查找节点,并确认是否获取到，此方法用于当前窗口发生变化
    public static AccessibilityNodeInfo fineNodeByIdAndTextCheck(AccessibilityNodeInfo node,String id,String text,
                                             AccessibilityService context,Map<String,String> record, String recordAction){
        AccessibilityNodeInfo result = null;
        int count=0;
        while (count<6){
            node = context.getRootInActiveWindow();
            if(text==null){
                result = findNodeInfosById(node,id);
            }else if(id==null){
                result = findNodeInfosByText(node,text);
            }else{
                result = fineNodeByIdAndText(node,id,text);
            }
            if(result!=null)
                break;
            count = count +1;
            sleep(500*count);
        }
        if(result==null)
            recordAndLog(record,recordAction);
        return result;
    }
    //获取聊天窗口内容
    public static String getChatWindowMsg(List<AccessibilityNodeInfo> receviceMsgs,int msgNum){
        String rMsg = "";
        if(receviceMsgs!=null){
            //判断当前屏幕是否显示完新接受的消息，显示不全需要滚屏
            if(receviceMsgs.size()>=msgNum){
                for(int j=msgNum;j>0;j--){
                    rMsg=rMsg+receviceMsgs.get(receviceMsgs.size()-j).getText()+"\n";
                }
                System.out.println("收到信息为-->"+rMsg);
            }else{
                //需要滚动屏幕
            }
        }
        return rMsg;
    }
    //创建buggle文本
    public static Bundle createBuddleText(String inputText){
        Bundle inputContent = new Bundle();
        inputContent.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,inputText);
        return inputContent;
    }
    public static void createPaste(String text){
        ClipboardManager clipboard = (ClipboardManager) GlobalApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text", text);
        clipboard.setPrimaryClip(clip);
    }
    public static void createPasteInThread(String text){
        ClipboardManager clipboard = (ClipboardManager) GlobalApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("text", text);
        clipboard.setPrimaryClip(clip);
    }
    public static void createPasteInHandler(final AccessibilityNodeInfo phoneNode,final String text,final Map<String,String> record, final String recordAction){
        if(phoneNode==null) return;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                createPaste(text);
                phoneNode.performAction(AccessibilityNodeInfo.ACTION_PASTE);
                recordAndLog(record,recordAction);
            }
        });
    }
    //返回执行状态并打印日志
    public static void recordAndLog(Map<String,String> record, String recordAction){
        record.put("recordAction",recordAction);
        LogUtil.d("record",recordAction);
    }
    //核对状态
    public static boolean checkAction(Map<String,String> record, String recordAction){
        if(recordAction.equals(record.get("recordAction")))
            return true;
        return false;
    }
    //核对状态
    public static boolean actionContains(Map<String,String> record, String str){
        if(record.get("recordAction").contains(str))
            return true;
        return false;
    }

    public static void  execShell(String cmd){
        try {
            Process process  = Runtime.getRuntime().exec("su");
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
           try {
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                process.destroy();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void clickXY(int x,int y){
        execShell("input tap "+x+" "+y);
    }
    public static void inputText(String text){
        execShell("input text "+text);
    }
    public static void inputSwipe(int x1,int y1,int x2,int y2){
        execShell("input swipe "+x1+" "+y1+" "+x2+" "+y2);
    }

    public static void showToastByRunnable(final Context context, final CharSequence text) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
            }
        });
    }
    public static void startAppByPackName(String packageName,String activity){
        Intent intent = new Intent();
        ComponentName cmp=new ComponentName(packageName,activity);
        //ComponentName cmp=new ComponentName("com.soft.apk008v","com.soft.apk008.LoadActivity");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        GlobalApplication.getContext().startActivity(intent);
    }
    public static void startSysSetting(){
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        GlobalApplication.getContext().startActivity(intent);
    }
    public static void opentActivity(String acvityName){
        Intent intent = new Intent(acvityName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        GlobalApplication.getContext().startActivity(intent);
    }
    public static void startVPN(){
        Intent intent = new Intent(Settings.ACTION_VPN_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        GlobalApplication.getContext().startActivity(intent);
    }

    public static void startWx(){
        startAppByPackName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
    }

    public  static void  wakeAndUnlock()
    {
        KeyguardManager km;
        KeyguardManager.KeyguardLock kl;
        PowerManager pm;
        PowerManager.WakeLock wl;
        if(true)
        {
            System.out.println("--->锁屏");
            //获取电源管理器对象
            pm=(PowerManager) GlobalApplication.getContext().getSystemService(Context.POWER_SERVICE);
            //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
            wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            //点亮屏幕
            wl.acquire();

            //得到键盘锁管理器对象
            km= (KeyguardManager)GlobalApplication.getContext().getSystemService(Context.KEYGUARD_SERVICE);
            kl = km.newKeyguardLock("unLock");
            //解锁
            kl.disableKeyguard();
        }else {
            System.out.println("--->亮屏");
        }

    }

    public  static void  wake()
    {
        PowerManager pm;
        PowerManager.WakeLock wl;
        if(!isScreenOn())
        {
            System.out.println("--->点亮锁屏");
            //获取电源管理器对象
            pm=(PowerManager) GlobalApplication.getContext().getSystemService(Context.POWER_SERVICE);
            //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
            wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            //点亮屏幕
            wl.acquire();
            //wl.release();
        }else {
            System.out.println("--->已经是亮屏");
        }

    }
    public  static void  lock()
    {
        PowerManager pm;
        PowerManager.WakeLock wl;
        if(isScreenOn())
        {
            System.out.println("--->熄灭锁屏");
            //获取电源管理器对象
            pm=(PowerManager) GlobalApplication.getContext().getSystemService(Context.POWER_SERVICE);
            //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
            wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            //点亮屏幕
            wl.acquire();
            wl.release();
        }else {
            System.out.println("--->已经是熄灭锁屏");
        }

    }
    public final static boolean isScreenLocked() {
        KeyguardManager mKeyguardManager = (KeyguardManager) GlobalApplication.getContext().getSystemService(GlobalApplication.getContext().KEYGUARD_SERVICE);
        //PowerManager pm = (PowerManager)  GlobalApplication.getContext().getSystemService(Context.POWER_SERVICE);
        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }

    public final static boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) GlobalApplication.getContext().getSystemService(Context.POWER_SERVICE);
        return powerManager.isScreenOn();
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public static List<String> getSomeMsgs(){
        List<String> list = new ArrayList<String>();
        list.add("你好.");
        list.add("在。。");
        list.add("可以的。。");
        list.add("我不介意。。");
        list.add("没有讲过。。");
        list.add("是不是");
        list.add("应该不是");
        list.add("什么时候");
        list.add("大概今晚9点左右");
        list.add("我会按时");
        list.add("嗯，好");
        list.add("今晚去不去");
        list.add("同学过来");
        list.add("好久没去");
        list.add("是这个时候");
        list.add("早点回来");
        list.add("出发了");
        list.add("可以没有");
        return list;
    }

    public static void  stopApp(String packageName){
        execShell("am force-stop "+packageName);
    }

    public static void clearAppData(){
        execShell("am force-stop com.tencent.mm" );
        execShell("pm clear com.tencent.mm" );
        execShell("rm -r -f /data/data/com.tencent.mm/MicroMsg" );
        execShell("rm -r -f /data/data/com.tencent.mm/app_cache" );
        execShell("rm -r -f /data/data/com.tencent.mm/app_dex" );
        execShell("rm -r -f /data/data/com.tencent.mm/app_font" );
        execShell("rm -r -f /data/data/com.tencent.mm/app_lib" );
        execShell("rm -r -f /data/data/com.tencent.mm/app_recover_lib" );
        execShell("rm -r -f /data/data/com.tencent.mm/app_tbs" );
        execShell("rm -r -f /data/data/com.tencent.mm/cache" );
        execShell("rm -r -f /data/data/com.tencent.mm/databases" );
        execShell("rm -r -f /data/data/com.tencent.mm/face_detect" );
        execShell("rm -r -f /data/data/com.tencent.mm/files" );
        execShell("rm -r -f /data/data/com.tencent.mm/shared_prefs" );
        execShell("rm -r -f /sdcard/tencent" );
    }

    //开启-休眠ms-关闭飞行模式
    public static void setAriplaneMode(long ms){
        execShell("settings put global airplane_mode_on 1 \n am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true" );
        sleep(ms);
        execShell("settings put global airplane_mode_on 0 \n am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false");
    }

    public static String getUUID(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }


    //获取网络状态
     public static boolean isNetworkConnected() {
         ConnectivityManager mConnectivityManager = (ConnectivityManager) GlobalApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
         if (mNetworkInfo != null) {
             return mNetworkInfo.isAvailable();
         }
         return false;
     }
}
