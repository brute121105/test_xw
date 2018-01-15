package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hyj.xw.BaseThread;
import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.NodeActionUtil;
import hyj.xw.util.ParseRootUtil;

/**
 * Created by Administrator on 2017/12/14.
 */

public class AutoLoginThread  extends BaseThread {
    private  int countRootNull =0;
    private  int SendMsgNum;
    public  final String TAG = this.getClass().getSimpleName();
    List<String> chatWxids = new ArrayList<String>();
    public AutoLoginThread(AccessibilityService context,Map<String, String> record,Map<String,Object> parameters){
        super(context,record,parameters);
        AutoUtil.recordAndLog(record,"init");
        chatWxids.add("mm77375");
        intiParam();
    }
    int loginIndex=-1,sendMsgCount,autoChatWxidIndex;
    private void intiParam(){
        SendMsgNum = Integer.parseInt(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_EXT));
        LogUtil.d("SendMsgNum",SendMsgNum+"");
    }
    List<String[]> acts = AppConfigDao.findAcountsListByCode(CommonConstant.APPCONFIG_LOGIN_ACCOUNT);
    @Override
    public Object call() {
        while (true){
            try {
            AutoUtil.sleep(1500);
            LogUtil.d(TAG,Thread.currentThread().getName());
                AutoUtil.wake();

            AccessibilityNodeInfo root = context.getRootInActiveWindow();
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
            if(NodeActionUtil.doClickByNodePathAndText(root,"请选择要使用的应用|取消","020","微信",record,record.get("recordAction"),3000)) continue;
            if(root.getPackageName().toString().indexOf("tencent")==-1){
                LogUtil.d(TAG,"not in the weixin view getPackageName2:"+root.getPackageName());
                AutoUtil.startWx();
            }

            ParseRootUtil.debugRoot(root);
             //ParseRootUtil.getCurrentViewAllNode(root);
             //autoChat(root,record,chatWxids.get(autoChatWxidIndex));
             autoLogin(root,record);
            }catch (Exception e){
              LogUtil.logError(e);
            }

        }
    }
    public void autoChat(AccessibilityNodeInfo root,Map<String,String> record,String wxid){
        if(AutoUtil.actionContains(record,"autoChat")||AutoUtil.actionContains(record,"init")){
            if(sendMsgCount<SendMsgNum){
                if(autoChatConfig(root,record,wxid)){
                    sendMsgCount = sendMsgCount+1;
                }
            }else{
                if(NodeActionUtil.isContainsStrs(root,"通讯录|发现")){
                    System.out.println("---->autoChatWxidIndex:"+autoChatWxidIndex);
                    System.out.println("---->autoChatWxidIndex:"+(chatWxids.size()-1));
                    if(autoChatWxidIndex<chatWxids.size()-1){
                        autoChatWxidIndex = autoChatWxidIndex+1;
                        sendMsgCount=0;
                    }else {
                        AutoUtil.recordAndLog(record,"autoLogin");
                        sendMsgCount=0;
                        autoChatWxidIndex =0;
                        return;
                    }
                }
                NodeActionUtil.doBack(context,root,"切换到按住说话|表情",record,"autoChat7返回上一级",2000);
            }
        }
    }

    public void autoLogin(AccessibilityNodeInfo root,Map<String,String> record){
        if(AutoUtil.actionContains(record,"autoLogin")||AutoUtil.actionContains(record,"init")||AutoUtil.actionContains(record,"autoChat")){
            if(AutoUtil.checkAction(record,"autoLogin8点击微信号/QQ号/邮箱登录")||loginIndex==-1){
                loginIndex = (loginIndex==acts.size()-1)?0:(loginIndex+1);
            }
            if(NodeActionUtil.isContainsStrs(root,"通讯录|发现")&&AutoUtil.checkAction(record,"autoLogin11点击登录")){
                AutoUtil.recordAndLog(record,"autoChat");
                return;
            }
            autoLoginConfig(root,record);
        }
    }
    //自动登录配置
    public boolean autoLoginConfig(AccessibilityNodeInfo root,Map<String,String> record){
        String wxid = acts.get(loginIndex)[0];
        String pwd = acts.get(loginIndex)[1];
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","040","我",record,"autoLogin1点击我",500);
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","00490","设置",record,"autoLogin2点击设置");
        NodeActionUtil.doClickByNodePathAndText(root,"勿扰模式|帐号与安全","002120","退出",record,"autoLogin3点击退出1");
        NodeActionUtil.doClickByNodePathAndText(root,"退出登录|关闭微信","0000","退出登录",record,"autoLogin4点击退出2");
        NodeActionUtil.doClickByNodePathAndText(root,"取消|退出后不会删除","02","退出",record,"autoLogin5点击退出3");
        NodeActionUtil.doClickByNodePathAndText(root,"找回密码|紧急冻结|密码","05","更多",record,"autoLogin6点击更多");
        NodeActionUtil.doClickByNodePathAndText(root,"注册|微信安全中心","01000","切换帐号",record,"autoLogin7点击切换帐号");
        NodeActionUtil.doClickByNodePathAndText(root,"请填写手机号|手机号登录","0033","用微信号/QQ号/邮箱登录",record,"autoLogin8点击微信号/QQ号/邮箱登录");
        boolean flag = false;
        if(!AutoUtil.checkAction(record,"autoLogin11点击登录")){
            NodeActionUtil.doInputByNodePathAndText(root,"请填写微信号/QQ号/邮箱|微信号/QQ/邮箱登录","00211",wxid,record,"autoLogin9输入微信号",500);
            NodeActionUtil.doInputByNodePathAndText(root,wxid+"|微信号/QQ/邮箱登录","00221",pwd,record,"autoLogin10输入密码",1000);
            flag =  NodeActionUtil.doClickByNodePathAndText(root,wxid+"|微信号/QQ/邮箱登录","0024","登录",record,"autoLogin11点击登录",3000);
        }
        NodeActionUtil.doClickByNodePathAndText(root,"玩一个小游戏才是正经事|开始游戏","0020","进入微信",record,"autoLogin12进入微信",500);
        return flag;
    }
    //自动聊天配置
    public boolean autoChatConfig(AccessibilityNodeInfo root,Map<String,String> record,String wxid){
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","06",null,record,"autoChat1点击搜索",500);
        NodeActionUtil.doInputByNodePathAndText(root,"搜索指定内容|资讯","02",wxid,record,"autoChat2输入搜索内容",500);
        NodeActionUtil.doClickByNodePathAndText(root,"查找微信号|搜一搜","05400","查找微信号:"+wxid,record,"autoChat3点击查找微信号",500);
        NodeActionUtil.doClickByNodePathAndText(root,"设置备注和标签|视频聊天","00360","发消息",record,"autoChat4点击发消息",500);
        NodeActionUtil.doClickByNodePathAndText(root,"切换到键盘|按住说话","000100","切换到键盘",record,"autoChat6点击切换到键盘",500);
        NodeActionUtil.doInputByNodePathAndText(root,"切换到按住说话|表情","000101",System.currentTimeMillis()+"",record,"autoChat5输入聊天内容",500);

        return NodeActionUtil.doClickByNodePathAndText(root,"切换到按住说话|表情","000103","发送",record,"autoChat6点击发送",500);
    }

}
