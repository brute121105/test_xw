package hyj.autooperation.util;

import android.accessibilityservice.AccessibilityService;
import android.app.KeyguardManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import hyj.autooperation.GlobalApplication;


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
    public static boolean performScroll(AccessibilityNodeInfo nodeInfo,Map<String,String> record, String recordAction) {
        boolean isClick = false;
        if(nodeInfo == null)  return false;
        if(nodeInfo.isScrollable()) {
            isClick = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            recordAndLog(record,recordAction);
        } else {
            isClick = performScroll(nodeInfo.getParent(),record,recordAction);
        }
        return isClick;
    }

    public static boolean performScrollBack(AccessibilityNodeInfo nodeInfo,Map<String,String> record, String recordAction) {
        boolean isClick = false;
        if(nodeInfo == null)  return false;
        if(nodeInfo.isScrollable()) {
            isClick = nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
            recordAndLog(record,recordAction);
        } else {
            isClick = performScroll(nodeInfo.getParent(),record,recordAction);
        }
        return isClick;
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
    public static void startAppByPackName(Context context,String packageName,String activity){
        Intent intent = new Intent();
        ComponentName cmp=new ComponentName(packageName,activity);
        //ComponentName cmp=new ComponentName("com.soft.apk008v","com.soft.apk008.LoadActivity");
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(cmp);
        context.startActivity(intent);
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

    public final static boolean isScreenLocked() {
        KeyguardManager mKeyguardManager = (KeyguardManager) GlobalApplication.getContext().getSystemService(GlobalApplication.getContext().KEYGUARD_SERVICE);
        //PowerManager pm = (PowerManager)  GlobalApplication.getContext().getSystemService(Context.POWER_SERVICE);
        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }

    public final static boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) GlobalApplication.getContext().getSystemService(Context.POWER_SERVICE);
        return powerManager.isScreenOn();
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


    public static void  stopApp(String packageName){
        execShell("am force-stop "+packageName);
    }

    public static void clearAppData(){
        execShell("am force-stop com.tencent.mm" );
        AutoUtil.sleep(2000);
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

    public static void addPhoneContacts(String name, String phone){

        //首先插入空值，再得到rawContactsId ，用于下面插值
        ContentValues values = new ContentValues();
        //insert a null value
        Uri rawContactUri = GlobalApplication.getResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactsId = ContentUris.parseId(rawContactUri);

        //往刚才的空记录中插入姓名
        values.clear();
        //A reference to the _ID that this data belongs to
        values.put(ContactsContract.CommonDataKinds.StructuredName.RAW_CONTACT_ID, rawContactsId);
        //"CONTENT_ITEM_TYPE" MIME type used when storing this in data table
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        //The name that should be used to display the contact.
        values.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, name);
        //insert the real values
        GlobalApplication.getResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        //插入电话
        values.clear();
        values.put(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID, rawContactsId);
        //String "Data.MIMETYPE":The MIME type of the item represented by this row
        //String "CONTENT_ITEM_TYPE": MIME type used when storing this in data table.
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone);
        GlobalApplication.getResolver().insert(ContactsContract.Data.CONTENT_URI, values);
    }

    public static void killApp(){
        AutoUtil.execShell("am force-stop hyj.xw");
        AutoUtil.execShell("am force-stop hyj.weixin_008");
        AutoUtil.execShell("am force-stop com.soft.apk008v");
    }
    public static String getLocalMacAddress() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);


            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

    public static String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(new Date());
        return dateTime;
    }

    public static void killAndClearWxData(){
        System.out.println("main-->doAction-->关闭、清楚数据");
        List<String> cmds = getKillAndClearWxCmds();
        for(String cmd:cmds){
            execShell(cmd);
        }
    }
    public static List<String> getKillAndClearWxCmds(){
        List<String> cmds = new ArrayList<String>();
        cmds.add("am force-stop com.tencent.mm" );
        cmds.add("pm clear com.tencent.mm" );
        //cmds.add("rm -r -f /data/data/com.tencent.mm/MicroMsg" );
        //cmds.add("rm -r -f /data/data/com.tencent.mm/app_cache" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/app_dex" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/app_font" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/app_lib" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/app_recover_lib" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/app_tbs" );
        //cmds.add("rm -r -f /data/data/com.tencent.mm/cache" );
        //cmds.add("rm -r -f /data/data/com.tencent.mm/databases" );
        //cmds.add("rm -r -f /data/data/com.tencent.mm/face_detect" );
        cmds.add("rm -r -f /data/data/com.tencent.mm/files" );
        //cmds.add("rm -r -f /data/data/com.tencent.mm/shared_prefs" );
        cmds.add("rm -r -f /sdcard/tencent" );
        return cmds;
    }

    public static void amStartWxActivity(String activiyName){
        //execShell("am start -n com.tencent.mm/.plugin.sns.ui.SnsTimeLineUI");
        execShell("am start -n com.tencent.mm/"+activiyName);
    }

    //手机号第 3 4 位 对应 zm数组下标的字母+ 手机号前 6位倒序
    public  static String createPwdByPhone(String phone){
        if(phone==null||phone.length()<7) return "null";
        String[] zm = {"c","v","b","k","p","y","h","f","g","m"};
        //String pwd = zm[Integer.parseInt(phone.substring(2,3))]+zm[Integer.parseInt(phone.substring(3,4))];
        String pwd = zm[Integer.parseInt(phone.substring(4,5))]+zm[Integer.parseInt(phone.substring(5,6))];
        for(int i=phone.length()-1;i>=phone.length()-6;i--){
            pwd = pwd +phone.charAt(i);
        }
        return pwd;
    }
}
