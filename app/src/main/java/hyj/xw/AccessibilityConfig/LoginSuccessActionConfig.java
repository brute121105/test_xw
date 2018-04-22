package hyj.xw.AccessibilityConfig;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import hyj.xw.common.CommonConstant;
import hyj.xw.common.WxNickNameConstant;
import hyj.xw.conf.PhoneConf;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.model.AccessibilityParameters;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.model.PhoneApi;
import hyj.xw.thread.GetOrUpdateServerStatusThread;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.DaoUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.NodeActionUtil;
import hyj.xw.util.ParseRootUtil;

/**
 * Created by Administrator on 2018/2/2.
 */
//NodeActionUtil.doInputByNodePathAndText(root,"手机号登录|用短信验证码登录","00331","123",record,"wx输入密码",500);
//NodeActionUtil.doClickByNodePathAndText(root,"手机号登录|用短信验证码登录","0035","登录",record,"wx点击登录",3000);

public class LoginSuccessActionConfig {

    public static int waitLoginSuccessCn=0;

    //修改密码
    public static void doSetPwdAction(AccessibilityNodeInfo root, Map<String, String> record,Wx008Data currentWx008Data){
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","040","我",record,"SetPwdThread点击我",500);
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","00490","设置",record,"SetPwdThread点击设置");
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","00690","设置",record,"SetPwdThread点击设置");
        NodeActionUtil.doClickByNodePathAndText(root,"勿扰模式|聊天|通用","00260","帐号与安全",record,"SetPwdThread帐号与安全");
        NodeActionUtil.doClickByNodePathAndText(root,"邮件地址|未绑定|声音锁","00270","微信密码",record,"SetPwdThread微信密码");
        NodeActionUtil.doClickByNodePathAndText(root,"应急联系人|微信安全中心","00240","微信密码",record,"SetPwdThread微信密码");
        NodeActionUtil.doInputByNodePathAndText(root,"为保障你的数据安全，修改密码前请填写原密码。|取消","001",currentWx008Data.getWxPwd(),record,"SetPwdThread输入密码",1000);
        NodeActionUtil.doClickByNodePathAndText(root,"为保障你的数据安全，修改密码前请填写原密码。|取消","02","确定",record,"SetPwdThread输入密码确定",1000);
        /*String phone = TextUtils.isEmpty(currentWx008Data.getWxId())?currentWx008Data.getPhone():currentWx008Data.getWxId();
        System.out.println("SetPwdThread-->phone:"+phone+" ,currentWx008Data.getWxPwd():"+currentWx008Data.getWxPwd());
        String newPwd = "www23"+phone.substring(phone.length()-3);*/
        String newPwd = getNewPwd();
        NodeActionUtil.doInputByNodePathAndText(root,"设置密码|完成|设置微信密码","0034",newPwd,record,"SetPwdThread输入密码1",1000);
        NodeActionUtil.doInputByNodePathAndText(root,"设置密码|完成|设置微信密码","0036",newPwd,record,"SetPwdThread输入密码2",1000);
        /*if(AutoUtil.checkAction(record,"SetPwdThread输入密码2")){
            AutoUtil.recordAndLog(record,"wx登陆成功");
            return;
        }*/
        NodeActionUtil.doClickByNodePathAndText(root,"设置密码|完成|设置微信密码","002","完成",record,"SetPwdThread设置微信密码完成",1000);
        if(AutoUtil.checkAction(record,"SetPwdThread设置微信密码完成")){
            //List<Wx008Data> ds1 = DaoUtil.findByDataByColumn("guid",currentWx008Data.getGuid());
            //System.out.println("SetPwdThread-->findByDataByColumn1:"+ JSON.toJSONString(ds1));
            System.out.println("SetPwdThread-->newPwd:"+newPwd+" old:"+currentWx008Data.getWxPwd()+" wxid:"+currentWx008Data.getWxId());
            int cn = DaoUtil.updatePwd(currentWx008Data,newPwd);
            System.out.println("SetPwdThread-->cn:"+ cn);
            if(cn==1){
                System.out.println("SetPwdThread-->updatePwd success");
                AutoUtil.recordAndLog(record,"wx登陆成功");
                //List<Wx008Data> ds2 = DaoUtil.findByDataByColumn("guid",currentWx008Data.getGuid());
                //System.out.println("SetPwdThread-->findByDataByColumn2:"+ JSON.toJSONString(ds2));
                return;

            }else {
                System.out.println("SetPwdThread-->updatePwd fail cn:"+cn);
            }
        }
    }
    public static String getNewPwd(){
        return AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_NEW_PWD);
    }
    //扫码登录pc端
    static boolean  isStartLogin=false;
    public static void doLoginPc(AccessibilityNodeInfo root, Map<String, String> record,Wx008Data currentWx008Data){
        if(!isStartLogin){
            String status = getServerStatus();
            System.out.println("doLoginPc GetOrUpdateServerStatusThread status--->"+status);
            if("1".equals(status)){//如果二维码没刷新，返回
                isStartLogin = true;
            }
        }
        if(!isStartLogin) return;

        if(NodeActionUtil.isWindowContainStr(root,"Windows 微信已登录")||NodeActionUtil.isWindowContainStr(root,"iPad 微信已登录")){
            waitLoginSuccessCn = 0;
            AutoUtil.recordAndLog(record,"wx登陆成功");
            isStartLogin = false;
            return;
        }else if(AutoUtil.checkAction(record,"loginPc点击登录确认")){//超过一定时间没登录成功重新扫
            System.out.println("waitLoginSuccessCn-->"+waitLoginSuccessCn);
            waitLoginSuccessCn = waitLoginSuccessCn+1;
            if(waitLoginSuccessCn>40){
                waitLoginSuccessCn= 0;
                AutoUtil.recordAndLog(record,"wx登陆成功");
                System.out.println("waitLoginSuccessCn recordAndLog-->"+waitLoginSuccessCn);
                return;
            }
        }
        if(AutoUtil.checkAction(record,"loginPc点击登录确认")) return;
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","030","发现",record,"loginPc点击发现",500);
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","00430","扫一扫",record,"loginPc点击扫一扫",500);
        NodeActionUtil.doClickByNodePathAndText(root,"Windows 微信登录确认|取消登录","002","登录",record,"loginPc点击登录确认",1500);
        NodeActionUtil.doClickByNodePathAndText(root,"iPad 微信登录确认|取消登录","002","登录",record,"loginPc点击登录确认",1500);

    }

    public static String getServerStatus(){
        String status = "";
        FutureTask<String> futureTask = startThreadByFuture("0");
        try {
            status  = futureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            LogUtil.logError(e);
        } catch (ExecutionException e) {
            e.printStackTrace();
            LogUtil.logError(e);
        }
        return status;
    }

    public static  FutureTask<String> startThreadByFuture(String status){
        GetOrUpdateServerStatusThread thread = new GetOrUpdateServerStatusThread(status);
        FutureTask<String> futureTask = new FutureTask<>(thread);
        new Thread(futureTask).start();
        return futureTask;
    }

    //登录搜索昵称
    static int searIndex=27;
    static String wxid = "";
    static int searNum=0;
    public static void doLoginAndSearchNickName(AccessibilityNodeInfo root, Map<String, String> record,Wx008Data currentWx008Data,List<Wx008Data> wx008Datas,AccessibilityService context){
        if("".equals(wxid)){//nickName为空和不是当期wxid
            Wx008Data sData = wx008Datas.get(searIndex);
            //while (!TextUtils.isEmpty(sData.getNickName())){
            while ("微信号搜索异常".equals(sData.getNickName())){
                searIndex = searIndex+1;
                sData = wx008Datas.get(searIndex);
                System.out.println("1 searIndex-->"+searIndex);
            }

            wxid = sData.getWxId();
            System.out.println("searIndex--wxid-->"+wxid);
            while(TextUtils.isEmpty(wxid)||wxid.equals(currentWx008Data.getWxId())){
                searIndex = searIndex+1;
                wxid = wx008Datas.get(searIndex).getWxId();
                System.out.println("2 searIndex2-->"+searIndex);
            }
        }

        if(AutoUtil.checkAction(record,"loginSNName完成一个返回")||AutoUtil.checkAction(record,"loginSNName点击确认被搜帐号状态异常")
                ||AutoUtil.checkAction(record,"loginSNName用户不存在确定")){
            NodeActionUtil.doInputByNodePathAndText(root,"文章、朋友圈、小说、音乐和表情等|清除","02",wxid,record,"loginSNName输入微信号",1000);
            NodeActionUtil.doInputByNodePathAndText(root,"搜索指定内容|朋友圈","02",wxid,record,"loginSNName输入微信号",1000);

        }
        NodeActionUtil.doClickByNodePathAndDesc(root,"通讯录|发现","06","搜索",record,"loginSNName点击搜索",1500);
        NodeActionUtil.doInputByNodePathAndText(root,"搜索指定内容|朋友圈","02",wxid,record,"loginSNName输入微信号",500);
        NodeActionUtil.doClickByNodePathAndText(root,"文章、朋友圈、小说、音乐和表情等|清除","05000","查找微信号:"+wxid,record,"loginSNName点击查找",1500);
        NodeActionUtil.doClickByNodePathAndText(root,"文章、朋友圈、小说、音乐和表情等|清除","05000","查找手机/QQ号:"+wxid,record,"loginSNName点击查找",1500);
        NodeActionUtil.doClickByNodePathAndText(root,"小程序、公众号、文章、朋友圈和表情等|清除","05000","查找微信号:"+wxid,record,"loginSNName点击查找",1500);
        NodeActionUtil.doClickByNodePathAndText(root,"小程序、公众号、文章、朋友圈和表情等|清除","05000","查找手机/QQ号:"+wxid,record,"loginSNName点击查找",1500);

        //AutoUtil.performClick(AutoUtil.findNodeInfosByText(root,"添加到通讯录"),record,"loginSNName点击添加到通讯录");
        //NodeActionUtil.doClickByNodePathAndText(root,"你需要发送验证申请，等对方通过|验证申请","002","发送",record,"loginSNName点击发送",1500);
        if(AutoUtil.checkAction(record,"loginSNName点击查找")&&(NodeActionUtil.isWindowContainStr(root,"添加到通讯录")||NodeActionUtil.isWindowContainStr(root,"设置备注和标签"))){
            AccessibilityNodeInfo nickNode = ParseRootUtil.getNodePath(root,"0031010");
            if(nickNode!=null&&!TextUtils.isEmpty(nickNode.getText())){
                if(searNum<9){
                    String nickName = nickNode.getText().toString();
                    int cn = DaoUtil.updateNickName(wx008Datas.get(searIndex),nickName);
                    System.out.println("nickName update--> wxid:"+wx008Datas.get(searIndex).getWxId()+" nickName:"+nickName+" cn:"+cn+" searIndex:"+searIndex+" searchNum:"+searNum);
                    AutoUtil.performBack(context,record,"loginSNName完成一个返回");
                    searNum = searNum+1;
                    searIndex = searIndex+1;
                }else {
                    searNum= 0;
                    AutoUtil.recordAndLog(record,"wx登陆成功");
                }
                wxid = "";
            }
        }

        if(NodeActionUtil.isWindowContainStr(root,"被搜帐号状态异常")||NodeActionUtil.isWindowContainStr(root,"用户不存在")){
            NodeActionUtil.doClickByNodePathAndText(root,"被搜帐号状态异常|确定","01","确定",record,"loginSNName点击确认被搜帐号状态异常",500);
            NodeActionUtil.doClickByNodePathAndText(root,"用户不存在|确定","01","确定",record,"loginSNName用户不存在确定",500);
            if(searNum<9){
                int cn = DaoUtil.updateNickName(wx008Datas.get(searIndex),"微信号搜索异常");
                System.out.println("nickName update--> wxid:"+wx008Datas.get(searIndex).getWxId()+" cn:"+cn+" searIndex:"+searIndex+" searchNum:"+searNum);
                searNum = searNum+1;
                searIndex = searIndex+1;
            }else {
                searNum= 0;
                AutoUtil.recordAndLog(record,"wx登陆成功");
            }
            wxid = "";

        }

    }

    //换绑手机号
    //static String phone ="17056299534",code="112233";
    static int countWaitValiCodeCn=0;
    static int countRepNum=0;//同一个手机号换绑的个数
    public static void doReplacePhone(AccessibilityNodeInfo root, Map<String, String> record,Wx008Data currentWx008Data,PhoneApi pa,AccessibilityService context){


        if(AutoUtil.checkAction(record,"ReplacePhoneThread换绑手机")){
            AccessibilityNodeInfo node2 = AutoUtil.findNodeInfosByText(root,"我");
            AutoUtil.performClick(node2,record,"ReplacePhoneThread点击我");
        }

        if(AutoUtil.checkAction(record,"ReplacePhoneThread点击我")){
            AccessibilityNodeInfo node3 = AutoUtil.findNodeInfosByText(root,"设置");
            AutoUtil.performClick(node3,record,"ReplacePhoneThread设置");
        }

        if(AutoUtil.checkAction(record,"ReplacePhoneThread设置")){
            AccessibilityNodeInfo node4 = AutoUtil.findNodeInfosByText(root,"帐号与安全");
            AutoUtil.performClick(node4,record,"ReplacePhoneThread帐号与安全");
        }

        NodeActionUtil.doClickByNodePathAndText(root,"帐号与安全|帐号|QQ号","00230","手机号",record,"ReplacePhoneThread手机号",500);

       /* if(AutoUtil.checkAction(record,"ReplacePhoneThread帐号与安全")){
            AccessibilityNodeInfo node5 = AutoUtil.findNodeInfosByText(root,"手机号");
            AutoUtil.performClick(node5,record,"ReplacePhoneThread手机号");
        }*/

        if(AutoUtil.checkAction(record,"ReplacePhoneThread手机号")){
            AccessibilityNodeInfo node6 = AutoUtil.findNodeInfosByText(root,"更换手机号");
            AutoUtil.performClick(node6,record,"ReplacePhoneThread更换手机号");
        }

        if(AutoUtil.checkAction(record,"ReplacePhoneThread更换手机号")){
            if(!pa.isPhoneIsAvailavle()) return;
            AccessibilityNodeInfo node7 = ParseRootUtil.getNodePath(root,"0032");
            AutoUtil.performSetText(node7, AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_PHONEE),record,"ReplacePhoneThread输入手机号");
            return;
        }

        if(AutoUtil.checkAction(record,"ReplacePhoneThread输入手机号")){
            AccessibilityNodeInfo node8 = AutoUtil.findNodeInfosByText(root,"下一步");
            AutoUtil.performClick(node8,record,"ReplacePhoneThread下一步");
            return;
        }

        if(NodeActionUtil.isContainsStrs(root,"填写验证码|请输入验证码")){
            System.out.println("GetPhoneAndValidCodeThread ReplacePhoneThread -->countWaitValiCodeCn:"+countWaitValiCodeCn);
            if(AutoUtil.checkAction(record,"ReplacePhoneThread验证码不正确确定"))  AutoUtil.sleep(2000);
            if(!pa.isValidCodeIsAvailavle()){
                countWaitValiCodeCn = countWaitValiCodeCn+1;
                if(countWaitValiCodeCn>40){
                    AutoUtil.performBack(context,record,"ReplacePhoneThread验证码收不到返回上一步");
                    countWaitValiCodeCn = 0;
                    pa.setPhoneIsAvailavle(false);
                    countRepNum = 0;
                }
                return;
            }
            countWaitValiCodeCn = 0;
            AccessibilityNodeInfo node7 = ParseRootUtil.getNodePath(root,"0021");
            AutoUtil.performSetText(node7,AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_PHONE_CODE),record,"ReplacePhoneThread输入验证码");
        }

        if(AutoUtil.checkAction(record,"ReplacePhoneThread输入验证码")){
            AccessibilityNodeInfo node8 = AutoUtil.findNodeInfosByText(root,"下一步");
            AutoUtil.performClick(node8,record,"ReplacePhoneThread输入验证码下一步");
        }

        if(AutoUtil.checkAction(record,"ReplacePhoneThread输入验证码下一步")){
            AccessibilityNodeInfo node8 = AutoUtil.findNodeInfosByText(root,"完成");
            if(node8!=null){
                int cn = DaoUtil.updateRepPhone(currentWx008Data,AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_PHONEE));
                countRepNum = countRepNum+1;
                System.out.println("GetPhoneAndValidCodeThread ReplacePhoneThread cn-->"+cn+" phone:"+AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_PHONEE)+" wxid:"+currentWx008Data.getWxId()+" countRepNum:"+countRepNum);
                if(countRepNum<7){
                    AutoUtil.recordAndLog(record,"wx登陆成功");
                }else {//第7个换绑两次
                    NodeActionUtil.doClickByNodePathAndText(root,"绑定手机号|你已停用了手机通讯录匹配","0021","完成",record,"ReplacePhoneThread点击完成，再次换绑当前手机",500);
                    pa.setValidCodeIsAvailavle(false);
                    pa.setPhoneIsAvailavle(false);
                    countRepNum = 0;
                }
            }
        }

        AccessibilityNodeInfo node9 = AutoUtil.findNodeInfosByText(root,"验证码不正确，请重新输入");
        if(node9!=null){
            AutoUtil.sleep(2000);
            pa.setValidCodeIsAvailavle(false);
            pa.setPhoneIsAvailavle(false);
            AccessibilityNodeInfo node10 = AutoUtil.findNodeInfosByText(root,"确定");
            AutoUtil.performClick(node10,record,"ReplacePhoneThread验证码不正确确定",1000);
            AutoUtil.performBack(context,record,"ReplacePhoneThread验证码不正确确定返回上一步");
        }
    }

    //扫码加群
    public static void doScan(AccessibilityNodeInfo root, Map<String, String> record,Wx008Data currentWx008Data){
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","030","发现",record,"kzgz点击我");
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","00330","扫一扫",record,"saoma点击扫一扫");
        NodeActionUtil.doClickByNodePathAndDesc(root,"我的二维码|封面|街景","04","更多",record,"saoma点击更多04",100);
        NodeActionUtil.doClickByNodePathAndDesc(root,"我的二维码|封面|街景","05","更多",record,"saoma点击更多05",100);
        NodeActionUtil.doClickByNodePathAndText(root,"将扫一扫添加到桌面|从相册选取二维码","01010","从相册选取二维码",record,"saoma点击从相册选取二维码");
        NodeActionUtil.doClickByNodePathAndText(root,"所有图片|图片|拍摄照片","0210",null,record,"saoma选择第一张图片");
        NodeActionUtil.doClickByNodePathAndDesc(root,"确认加入群聊|加入该群聊","000004","加入该群聊",record,"saoma加入该群聊",2000);
        if(NodeActionUtil.isWindowContainStr(root,"切换到键盘")||NodeActionUtil.isWindowContainStr(root,"切换到按住说话")){
            AutoUtil.recordAndLog(record,"wx登陆成功");
        }
    }

    //发圈
    public static void sendFr(AccessibilityNodeInfo root, Map<String, String> record,Wx008Data currentWx008Data){
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","030","发现",record,"sendFr点击发现",0);
        NodeActionUtil.doClickByNodePathAndText(root,"扫一扫|摇一摇","00310","朋友圈",record,"sendFr点击朋友圈",0);
        NodeActionUtil.doClickByNodePathAndText(root,"轻触更换主题照片|朋友圈封面，再点一次可以改封面","00310","朋友圈",record,"sendFr点击朋友圈",0);
        if(AutoUtil.checkAction(record,"sendFr点击朋友圈")&&NodeActionUtil.isWindowContainStr(root,"朋友圈封面")){
            AccessibilityNodeInfo node6 = ParseRootUtil.getNodePath(root,"002");
            System.out.println("node6-->"+node6);
            node6.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
        }
        NodeActionUtil.doClickByNodePathAndText(root,"长按拍照按钮发文字，为内部体验功能。后续版本可能取消，也有可能保留，请勿过于依赖此方法。|我知道了","001","我知道了",record,"sendFr点击我知道了",500);
        NodeActionUtil.doInputByNodePathAndText(root,"这一刻的想法...|谁可以看","0000", WxNickNameConstant.getName1(),record,"sendFr输入发送内容",0);
        if(AutoUtil.checkAction(record,"sendFr输入发送内容")){
            NodeActionUtil.doClickByNodePathAndText(root,"这一刻的想法...|谁可以看","03","发送",record,"sendFr点击发送",0);
        }
        if(AutoUtil.checkAction(record,"sendFr点击发送")){
            AutoUtil.sleep(3000);
            AutoUtil.recordAndLog(record,"wx登陆成功");
        }
    }
    //筷子挂机
    static int countReadNum=0;
    public static void kzgz(AccessibilityNodeInfo root, Map<String, String> record,Wx008Data currentWx008Data,AccessibilityService context){
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","040","我",record,"kzgz点击我",500);
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","00550","收藏",record,"kzgz点击收藏",500);
        NodeActionUtil.doClickByNodePathAndDesc(root,"我的收藏|没有任何收藏","003","添加收藏",record,"kzgz添加收藏",500);
        NodeActionUtil.doClickByNodePathAndText(root,"我的收藏|添加收藏","00422",null,record,"kzgz点击进入收藏内容",500);
        //NodeActionUtil.doClickByNodePathAndText(root,"笔记详情|更多功能按钮","00000",null,record,"kzgz点击url",500);
        if(NodeActionUtil.isWindowContainStr(root,"笔记详情")){
             if(NodeActionUtil.isWindowContainStr(root,"http")){
                 AutoUtil.clickXY(530,415);
                 AutoUtil.sleep(3000);
            }else {
                 String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6b88086a9cba0ec7&redirect_uri=http%3A%2F%2Fwww.gonijia.net%2Fstay%2FApi%2Fauth_task1&response_type=code&scope=snsapi_base&state=brute61#wechat_redirect";
                 if(NodeActionUtil.doInputByNodePathAndText(root,"笔记详情|更多功能按钮","00000",url,record,"kzgz填写url",500) ){
                     AutoUtil.clickXY(530,415);
                     AutoUtil.performBack(context,record,"kzgz填写url返回1");
                     AutoUtil.performBack(context,record,"kzgz填写url返回2");
                 }
             }
        }
        if(NodeActionUtil.isWindowContainStr(root,"网页由 mp.weixin.qq.com 提供")){
            AccessibilityNodeInfo node = ParseRootUtil.getNodePath(root,"0031");
            if(node!=null){
                System.out.println("gzhText2-->"+node.getText());
                AutoUtil.inputSwipe(530,1468,530,488);
                AutoUtil.sleep(600);
                AutoUtil.inputSwipe(530,1468,530,488);
                countReadNum = countReadNum+1;
                if(countReadNum>14){
                    AutoUtil.recordAndLog(record,"wx登陆成功");
                    countReadNum=0;
                    return;
                }
                AutoUtil.performBack(context,record,"kzgz填写url返回,阅读篇数："+countReadNum);
            }
        }
    }
    //添加好友
    static List<String> addFrWxids = PhoneConf.getAddFrWx();
    static int addFrWxidNum=0;
    static int addDfNum=0;//添加对方个数
    public static void wxidaf(AccessibilityNodeInfo root, Map<String, String> record,Wx008Data currentWx008Data,AccessibilityService context){
        String addFrWxid = addFrWxids.get(addFrWxidNum);

        if((AutoUtil.checkAction(record,"af发送好友请求")&&NodeActionUtil.isWindowContainStr(root,"添加到通讯录"))
                ||AutoUtil.checkAction(record,"af用户不存在确定")||AutoUtil.checkAction(record,"af点击确认被搜帐号状态异常")
                ||AutoUtil.checkAction(record,"af发送聊天内容")
                ||(AutoUtil.actionContains(record,"af点击查找")&&NodeActionUtil.isContainsStrs(root,"发消息"))){
            addFrWxidNum = addFrWxidNum+1;
            Log.i("addFrWxidNum-->",addFrWxidNum+"");
            if(addFrWxidNum==addFrWxids.size()){
                addFrWxidNum = 0;
                addDfNum = addDfNum+1;
                saveAfsToDb(currentWx008Data);
                String extValue = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_EXT);
                if("609603".equals(extValue)){
                    AutoUtil.performBack(context,record,"返回1");
                    AutoUtil.sleep(1000);
                    AutoUtil.performBack(context,record,"返回2");
                    AutoUtil.recordAndLog(record,"loginPc扫码登录");
                }else {
                    AutoUtil.recordAndLog(record,"wx登陆成功");
                }
            }else {
                AutoUtil.performBack(context,record,"af完成一个返回");
            }
            return;
        }

        if(AutoUtil.checkAction(record,"af完成一个返回")){
            NodeActionUtil.doInputByNodePathAndText(root,"文章、朋友圈、小说、音乐和表情等|清除","02",addFrWxid,record,"af输入微信号",0);
        }
        NodeActionUtil.doClickByNodePathAndDesc(root,"通讯录|发现","06","搜索",record,"af点击搜索",0);
        NodeActionUtil.doInputByNodePathAndText(root,"搜索指定内容|朋友圈|公众号","02",addFrWxid,record,"af输入微信号",0);
        if(!NodeActionUtil.doClickByNodePathAndText(root,"文章、朋友圈、小说、音乐和表情等|清除","05000","查找微信号:"+addFrWxid,record,"af点击查找"+addDfNum,0)){
            NodeActionUtil.doClickByNodePathAndText(root,"小程序、公众号、文章、朋友圈和表情等|清除","05000","查找微信号:"+addFrWxid,record,"af点击查找"+addDfNum,0);
        }
        if(!NodeActionUtil.doClickByNodePathAndText(root,"文章、朋友圈、小说、音乐和表情等|清除","05000","查找手机/QQ号:"+addFrWxid,record,"af点击查找"+addDfNum,0)){
            NodeActionUtil.doClickByNodePathAndText(root,"设置备注和标签|添加到通讯录","00330","添加到通讯录",record,"af添加到通讯录"+addDfNum,0);
        }
        NodeActionUtil.doClickByNodePathAndText(root,"设置朋友圈权限|为朋友设置备注","002","发送",record,"af发送好友请求",0);

        //情形二，对方无需好友验证
        NodeActionUtil.doClickByNodePathAndText(root,"设置备注和标签|视频聊天","00350","发消息",record,"af已添加发消息",0);
        NodeActionUtil.doInputByNodePathAndText(root,"切换到按住说话|表情","000101",currentWx008Data.getPhone(),record,"af输入聊天内容",0);
        NodeActionUtil.doClickByNodePathAndText(root,"切换到按住说话|表情","000103","发送",record,"af发送聊天内容",0);

        if(NodeActionUtil.isWindowContainStr(root,"被搜帐号状态异常")||NodeActionUtil.isWindowContainStr(root,"用户不存在")){
            NodeActionUtil.doClickByNodePathAndText(root,"被搜帐号状态异常|确定","01","确定",record,"af点击确认被搜帐号状态异常",0);
            NodeActionUtil.doClickByNodePathAndText(root,"用户不存在|确定","01","确定",record,"af用户不存在确定",0);
        }

    }
    public static void saveAfsToDb(Wx008Data currentWx008Data){
        String friends = JSON.toJSONString(PhoneConf.getAddFrWx());
        currentWx008Data.setFriends(friends);
      if(!TextUtils.isEmpty(currentWx008Data.getWxId())){
          int cn = currentWx008Data.updateAll("wxId=?",currentWx008Data.getWxId());
          System.out.println("saveAfsToDb wxId--->"+cn+" friends:"+friends);
      }else if(!TextUtils.isEmpty(currentWx008Data.getWxid19())){
          int cn = currentWx008Data.updateAll("wxid19=?",currentWx008Data.getWxid19());
          System.out.println("saveAfsToDb wxid19--->"+cn+" friends:"+friends);
      }
    }

    //取关公众号
    static int clickGzhIndex=0;
    static int countGzgNum=0;
    public static void qggzh(AccessibilityNodeInfo root, Map<String, String> record,Wx008Data currentWx008Data,AccessibilityService context){
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","020","通讯录",record,"qggzh点击通讯录",0);
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","0010300","公众号",record,"qggzh点击公众号",0);
        List<AccessibilityNodeInfo> nodes = root.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/v9");
        Log.i("qggzh-->nodes:",nodes+"");
        if(NodeActionUtil.isContainsStrs(root,"公众号|搜索|添加")){
            if(nodes!=null&&nodes.size()>0&&clickGzhIndex<nodes.size()){
                AccessibilityNodeInfo node =  nodes.get(clickGzhIndex);
                Log.i("qggzh-->node:",node+"");
                AutoUtil.performClick(node,record,"qggzh点击",0);
                clickGzhIndex = clickGzhIndex+1;
            }
        }
        NodeActionUtil.doClickByNodePathAndDesc(root,"聊天信息|返回","003","聊天信息",record,"qggzh右上1",0);
        NodeActionUtil.doClickByNodePathAndDesc(root,"头像|功能介绍","002","更多",record,"qggzh右上更多",0);
        NodeActionUtil.doClickByNodePathAndText(root,"推荐给朋友|清空内容","01030","不再关注",record,"qggzh点击不再关注1",0);
        NodeActionUtil.doClickByNodePathAndText(root,"不再关注|取消","02","不再关注",record,"qggzh点击不再关注2",0);
        if(AutoUtil.checkAction(record,"qggzh点击公众号")){
            clickGzhIndex = 0;
        }
        if(NodeActionUtil.isWindowContainStr(root,"要求使用你的地理位置")){
            NodeActionUtil.doClickByNodePathAndText(root,"确定|提示","02","取消",record,"qggzh点击取消地理位置",0);
        }
        if(AutoUtil.checkAction(record,"qggzh点击不再关注2")){
            countGzgNum = countGzgNum+1;
            Log.i("qggzh-->countGzgNum:",countGzgNum+"");
        }
        if(NodeActionUtil.isWindowContainStr(root,"0个公众号")||countGzgNum>14){
            clickGzhIndex = 0;
            countGzgNum = 0;
            AutoUtil.recordAndLog(record,"wx登陆成功");
        }
    }

    //自动通过好友
    static boolean isScrollForward =true;
    static int rcFrNum=0;
    public static void recfnd(AccessibilityNodeInfo root, Map<String, String> record,Wx008Data currentWx008Data,AccessibilityService context){
        String id1 = "com.tencent.mm:id/b8d";//朋友推荐及下方
        String id2 = "com.tencent.mm:id/b8v";//ListView 接受好友区
        AccessibilityNodeInfo node2 = AutoUtil.findNodeInfosByText(root,"接受");
        AccessibilityNodeInfo node3 = AutoUtil.findNodeInfosByText(root,"已添加");
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","020","通讯录",record,"recfnd点击通讯录",0);
        AccessibilityNodeInfo node1 = AutoUtil.findNodeInfosById(root,id1);
        if(AutoUtil.performClick(node1,record,"recfnd点击通讯录2")) return;
        if(AutoUtil.performClick(node2,record,"recfnd接受")) return;
        if(NodeActionUtil.doClickByNodePathAndText(root,"去验证|取消","02","去验证",record,"recfnd点击去验证",0)) return;
        NodeActionUtil.doClickByNodePathAndText(root,"通过验证|加入黑名单","00350","通过验证",record,"recfnd点击通过验证",0);
        if(NodeActionUtil.doClickByNodePathAndText(root,"通过验证|加入黑名单","00360","通过验证",record,"recfnd点击通过验证",0)) return;
        //NodeActionUtil.doClickByNodePathAndText(root,"为朋友设置备注|设置朋友圈权限","0031",null,record,"recfnd填入备注",0);
        if(NodeActionUtil.doClickByNodePathAndText(root,"为朋友设置备注|设置朋友圈权限","002","完成",record,"recfnd点击完成"+rcFrNum,0)) return;
        if(NodeActionUtil.isContainsStrs(root,"设置备注和标签|头像")){
            AutoUtil.performBack(context,record,"recfnd back1");
            rcFrNum = rcFrNum+1;
            return;
         }
        if(NodeActionUtil.isWindowContainStr(root,"还没有推荐的朋友")){
            AutoUtil.recordAndLog(record,"wx登陆成功");
            return;
        }
        if(node2==null&&node3!=null){//符合滚动条件
            AccessibilityNodeInfo listNode = AutoUtil.findNodeInfosById(root,id2);
            if(!isScrollForward){
               if(!AutoUtil.performScrollBack(listNode,record,"recfnd上滚")){
                   AutoUtil.recordAndLog(record,"wx登陆成功");
                   rcFrNum = 0;
                   isScrollForward = true;
                   return;
               }
            }
            if(isScrollForward){//符合下滚条件
                isScrollForward = AutoUtil.performScroll(listNode,record,"recfnd下滚");
                AutoUtil.sleep(500);
                return;
            }
        }
    }
    //获取昵称
    public static void getNickName(AccessibilityNodeInfo root, Map<String, String> record,Wx008Data currentWx008Data,AccessibilityService context,AccessibilityParameters parameters){
        NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","040","我",record,"getNickName点击我",500);
        NodeActionUtil.doClickByNodePathAndDesc(root,"通讯录|发现","00611","查看二维码",record,"getNickName点击查看二维码",500);
        NodeActionUtil.doClickByNodePathAndDesc(root,"通讯录|发现","000611","查看二维码",record,"getNickName点击查看二维码",500);
        NodeActionUtil.doClickByNodePathAndDesc(root,"通讯录|发现","00511","查看二维码",record,"getNickName点击查看二维码",500);
        NodeActionUtil.doClickByNodePathAndDesc(root,"通讯录|发现","000511","查看二维码",record,"getNickName点击查看二维码",500);

        if(NodeActionUtil.isContainsStrs(root,"扫一扫上面的二维码图案")){
            String nickName = NodeActionUtil.getTextByNodePath(root,"0030");
            if(!TextUtils.isEmpty(nickName)){
                int cn = DaoUtil.updateNickName(currentWx008Data,nickName);
                System.out.println("getNickName-->"+nickName+" cn:"+cn);
                AutoUtil.performBack(context,record,"wx登陆成功");
                //parameters.setIsStop(1);
            }
        }
    }
}
