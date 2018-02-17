package hyj.xw.AccessibilityConfig;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.model.PhoneApi;
import hyj.xw.thread.GetOrUpdateServerStatusThread;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.DaoUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.NodeActionUtil;
import hyj.xw.util.ParseRootUtil;
import hyj.xw.util.StringUtilHyj;

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
        NodeActionUtil.doInputByNodePathAndText(root,"为保障你的数据安全，修改密码前请填写原密码。|取消","01",currentWx008Data.getWxPwd(),record,"SetPwdThread输入密码",1000);
        NodeActionUtil.doClickByNodePathAndText(root,"为保障你的数据安全，修改密码前请填写原密码。|取消","03","确定",record,"SetPwdThread输入密码确定",1000);
        String phone = TextUtils.isEmpty(currentWx008Data.getWxId())?currentWx008Data.getPhone():currentWx008Data.getWxId();
        System.out.println("SetPwdThread-->phone:"+phone+" ,currentWx008Data.getWxPwd():"+currentWx008Data.getWxPwd());
        String newPwd = "www23"+phone.substring(phone.length()-3);
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
    //扫码登录pc端
    public static void doLoginPc(AccessibilityNodeInfo root, Map<String, String> record,Wx008Data currentWx008Data){
        String status = getServerStatus();
        System.out.println("doLoginPc status--->"+status);
        if(!"3".equals(status)&&!"1".equals(status)&&AutoUtil.checkAction(record,"loginPc扫码登录")){
            startThreadByFuture("3");//告诉对方可以生成二维码
        }
        if(!"1".equals(status)){//如果二维码没没出现，返回
            return;
        }
        if(NodeActionUtil.isWindowContainStr(root,"Windows 微信已登录")||NodeActionUtil.isWindowContainStr(root,"iPad 微信已登录")){
            waitLoginSuccessCn = 0;
            AutoUtil.recordAndLog(record,"wx登陆成功");
            startThreadByFuture("2");
            return;
        }else if(AutoUtil.checkAction(record,"loginPc点击登录确认")){//超过一定时间没登录成功重新扫
            System.out.println("waitLoginSuccessCn-->"+waitLoginSuccessCn);
            waitLoginSuccessCn = waitLoginSuccessCn+1;
            if(waitLoginSuccessCn>40){
                waitLoginSuccessCn= 0;
                startThreadByFuture("2");
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
    static int searIndex=0;
    static String wxid = "";
    static int searNum=0;
    public static void doLoginAndSearchNickName(AccessibilityNodeInfo root, Map<String, String> record,Wx008Data currentWx008Data,List<Wx008Data> wx008Datas,AccessibilityService context){
        if("".equals(wxid)){//nickName为空和不是当期wxid
            Wx008Data sData = wx008Datas.get(searIndex);
            while (!TextUtils.isEmpty(sData.getNickName())){
                searIndex = searIndex+1;
                sData = wx008Datas.get(searIndex);
            }

            wxid = sData.getWxId();
            while(TextUtils.isEmpty(wxid)||wxid.equals(currentWx008Data.getWxId())){
                searIndex = searIndex+1;
                wxid = wx008Datas.get(searIndex).getWxId();
            }
        }

        if(AutoUtil.checkAction(record,"loginSNName完成一个返回")||AutoUtil.checkAction(record,"loginSNName点击确认被搜帐号状态异常")){
            NodeActionUtil.doInputByNodePathAndText(root,"文章、朋友圈、小说、音乐和表情等|清除","02",wxid,record,"loginSNName输入微信号",1000);
        }
        NodeActionUtil.doClickByNodePathAndDesc(root,"通讯录|发现","06","搜索",record,"loginSNName点击搜索",1500);
        NodeActionUtil.doInputByNodePathAndText(root,"搜索指定内容|朋友圈|资讯","02",wxid,record,"loginSNName输入微信号",500);
        NodeActionUtil.doClickByNodePathAndText(root,"文章、朋友圈、小说、音乐和表情等|清除","05000","查找微信号:"+wxid,record,"loginSNName点击查找",1500);
        //AutoUtil.performClick(AutoUtil.findNodeInfosByText(root,"添加到通讯录"),record,"loginSNName点击添加到通讯录");
        //NodeActionUtil.doClickByNodePathAndText(root,"你需要发送验证申请，等对方通过|验证申请","002","发送",record,"loginSNName点击发送",1500);
        if(AutoUtil.checkAction(record,"loginSNName点击查找")&&NodeActionUtil.isWindowContainStr(root,"添加到通讯录")){
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

        if(NodeActionUtil.isWindowContainStr(root,"被搜帐号状态异常")){
            NodeActionUtil.doClickByNodePathAndText(root,"被搜帐号状态异常|确定","01","确定",record,"loginSNName点击确认被搜帐号状态异常",500);
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
            System.out.println("GetPhoneAndValidCodeThread ReplacePhoneThread -->countWaitValiCodeCn:"+countWaitValiCodeCn);
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
}
