package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hyj.xw.AccessibilityConfig.LoginSuccessActionConfig;
import hyj.xw.BaseThread;
import hyj.xw.GlobalApplication;
import hyj.xw.api.GetPhoneAndValidCodeThread;
import hyj.xw.aw.sysFileRp.CreatePhoneEnviroment;
import hyj.xw.common.CommonConstant;
import hyj.xw.common.FilePathCommon;
import hyj.xw.conf.PhoneConf;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.hook.newHook.NewPhoneInfo;
import hyj.xw.model.AccessibilityParameters;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.model.PhoneApi;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.DaoUtil;
import hyj.xw.util.FileUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.NodeActionUtil;
import hyj.xw.util.ParseRootUtil;

/**
 * Created by Administrator on 2017/12/14.
 */

public class AutoFeedThread extends BaseThread {
    private  int countRootNull =0;
    private int loginIndex,endLoginIndex;//登录序号
    private String isLoginSucessPause;//登录成功是否暂停
    public  final String TAG = this.getClass().getSimpleName();
    public AutoFeedThread(AccessibilityService context, Map<String, String> record, AccessibilityParameters parameters){
        super(context,record,parameters);
        intiParam();
    }
    List<Wx008Data> wx008Datas;
    Wx008Data currentWx008Data;
    String extValue,isAirChangeIp,isLoginByPhone;
    PhoneApi pa = new PhoneApi();
    private void intiParam(){
        AutoUtil.recordAndLog(record,"init");
        //AutoUtil.recordAndLog(record,"recfnd");
        //AutoUtil.startWx();
        wx008Datas = DaoUtil.getWx008Datas();
        loginIndex = Integer.parseInt(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX));
        isLoginSucessPause = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_LOGIN_PAUSE);
        extValue = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_EXT);
        isAirChangeIp = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_AIR_CHANGE_IP);
        isLoginByPhone = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_LOGIN_BY_PHONE);
        if(extValue.contains("605")){//605换绑手机，需接吗
            new Thread(new GetPhoneAndValidCodeThread(pa)).start();//玉米
        }
    }
    @Override
    public Object call() {
        while (true){
            try {
            //记录数据，悬浮框显示
            recordFlowInfo(wx008Datas,loginIndex);
            AutoUtil.sleep(500);
            LogUtil.d(TAG,Thread.currentThread().getName()+" "+record+" loginIndex:"+loginIndex+" isLoginSucessPause:"+isLoginSucessPause+" isLoginByPhone:"+isLoginByPhone);
            if(currentWx008Data!=null){
                LogUtil.d(TAG,"wxid:"+currentWx008Data.getWxId()+" pwd:"+currentWx008Data.getWxPwd());
                System.out.println("currentWx008Data-->"+JSON.toJSONString(currentWx008Data));
            }

            //保持屏幕常亮
            AutoUtil.wake();


                if(parameters.getIsStop()==1){
                    LogUtil.d(TAG,"暂停....");
                    continue;
                }

            AccessibilityNodeInfo root = context.getRootInActiveWindow();
            //roor超过5次为空，启动wx
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
                ParseRootUtil.debugRoot(root);

            //不在wx界面，启动wx
            AutoUtil.doNotInCurrentView(root,record);

            NodeActionUtil.doClickByNodePathAndText(root,"微信无响应。要将其关闭吗？|确定","01","等待",record,"exception",500);


                //登录完成所有号退出
                if(AutoUtil.actionContains(record,"wx登陆完成")) {
                    NodeActionUtil.doClickByNodePathAndText(root,"通讯录|发现","030","发现",record,record.get("recordAction"),500);
                    return null;
                }
                if(AutoUtil.checkAction(record,"init")||AutoUtil.checkAction(record,"wx登陆成功")||AutoUtil.checkAction(record,"wx登陆异常")||AutoUtil.checkAction(record,"debug")
                        ||AutoUtil.checkAction(record,"wx改机失败")){

                    currentWx008Data = wx008Datas.get(loginIndex);
                   /* if(!"null".equals(currentWx008Data.getExpMsg())&&!TextUtils.isEmpty(currentWx008Data.getExpMsg())&&currentWx008Data.getExpMsg().indexOf("登录成功")==-1){
                        doNextIndexAndRecord2DB();
                        continue;
                    }*/

                    if(!AutoUtil.checkAction(record,"debug")){
                        AutoUtil.clearAppData();
                        AutoUtil.sleep(2000);
                        isSelectFail = false;
                        LogUtil.d(TAG,"清除app数据");
                        AutoUtil.recordAndLog(record,"wx清除app数据");
                        //FileUtil.writeContent2FileForce("/sdcard/A_hyj_json/","wxid.txt","");
                    }
                    String hookPhoneDataStr="";

                   /*if(TextUtils.isEmpty(currentWx008Data.getPhoneStrs())){//旧008数据
                        currentWx008Data.setPhoneInfo(currentWx008Data.getDatas());
                        hookPhoneDataStr = JSON.toJSONString(currentWx008Data.getPhoneInfo());
                    }else {//自己注册
                        hookPhoneDataStr = currentWx008Data.getPhoneStrs();
                        PhoneInfo opi = JSON.parseObject(hookPhoneDataStr, PhoneInfo.class);
                        if(TextUtils.isEmpty(opi.getCPU_ABI())){
                            opi.setCPU_ABI("armeabi-v7a");
                        }
                       if(TextUtils.isEmpty(opi.getCPU_ABI2())){
                           opi.setCPU_ABI2("armeabi");
                       }
                       hookPhoneDataStr = JSON.toJSONString(opi);
                        System.out.println("--phoneStr:"+hookPhoneDataStr);
                    }
*/
                    if(!AutoUtil.checkAction(record,"debug")){

                        NewPhoneInfo pi = null;
                        if(!TextUtils.isEmpty(currentWx008Data.getPhoneStrsAw())){//aw数据
                            pi = JSON.parseObject(currentWx008Data.getPhoneStrsAw(),NewPhoneInfo.class);
                        }else {
                            pi = PhoneConf.xw2awData(currentWx008Data);
                        }
                        pi.setCpuName(pi.getCpuName().trim().toLowerCase());
                        System.out.println(" auto pi-->"+JSON.toJSONString(pi));
                        CreatePhoneEnviroment.create(GlobalApplication.getContext(),pi);
                        FileUtil.writeContent2FileForceUtf8(FilePathCommon.baseAppPathAW,FilePathCommon.npiFileName, JSON.toJSONString(pi));
                        //FileUtil.writeContent2FileForceUtf8("/sdcard/A_hyj_json/a1/","PhoneInfo.aw", JSON.toJSONString(pi));

                        //覆盖式写入文件
                        //FileUtil.writeContent2FileForce("/sdcard/A_hyj_json/","phone.txt",hookPhoneDataStr);
                        //读取文件
                        //String con = FileUtil.readAll("/sdcard/A_hyj_json/phone.txt");
                        //System.out.println("phoneInfo---->"+con);
                        //飞行模式
                        if("1".equals(isAirChangeIp)){
                            AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"开启飞行模式");
                            //new SetAirPlaneModeThread(500).start();
                            AutoUtil.setAriplaneMode(1000);
                        }
                        AutoUtil.sleep(5000);
                        AutoUtil.startWx();
                        AutoUtil.recordAndLog(record,"wx飞行模式&清除数据后启动微信");
                        //记录ip
                        recordIp();
                    }
                }

                //自动登陆
                if(AutoUtil.actionContains(record,"wx")||AutoUtil.checkAction(record,"init")||AutoUtil.checkAction(record,"debug")){
                    autoLoginConfig(root,record);
                    exceptionConfig(root,record);
                    //maihao(root,record);
                }

                //设置密码
                if(AutoUtil.actionContains(record,"SetPwdThread")){
                    LogUtil.d(TAG,"SetPwdThread-- "+record+" loginIndex:"+loginIndex+" isLoginSucessPause:"+isLoginSucessPause);
                    LoginSuccessActionConfig.doSetPwdAction(root,record,currentWx008Data);
                }

                //扫码登录pc
                if(AutoUtil.actionContains(record,"loginPc")){
                    LogUtil.d(TAG,"loginPc-- "+record+" loginIndex:"+loginIndex+" isLoginSucessPause:"+isLoginSucessPause);
                    LoginSuccessActionConfig.doLoginPc(root,record,currentWx008Data);
                }
                //微信号搜索昵称
                if(AutoUtil.actionContains(record,"loginSNName")){
                    LogUtil.d(TAG,"loginSNName-- "+record+" loginIndex:"+loginIndex+" isLoginSucessPause:"+isLoginSucessPause);
                    LoginSuccessActionConfig.doLoginAndSearchNickName(root,record,currentWx008Data,wx008Datas,context);
                }
                //登录成功换绑手机号
                if(AutoUtil.actionContains(record,"ReplacePhoneThread")){
                    LogUtil.d(TAG,"ReplacePhoneThread-- "+record+" loginIndex:"+loginIndex+" isLoginSucessPause:"+isLoginSucessPause);
                    LoginSuccessActionConfig.doReplacePhone(root,record,currentWx008Data,pa,context);
                }
                //扫码加群
                if(AutoUtil.actionContains(record,"saoma")){
                    LogUtil.d(TAG,"loginSNName-- "+record+" loginIndex:"+loginIndex+" isLoginSucessPause:"+isLoginSucessPause);
                    LoginSuccessActionConfig.doScan(root,record,currentWx008Data);
                }
                //发圈
                if(AutoUtil.actionContains(record,"sendFr")){
                    LogUtil.d(TAG,"sendFr-- "+record+" loginIndex:"+loginIndex+" isLoginSucessPause:"+isLoginSucessPause);
                    LoginSuccessActionConfig.sendFr(root,record,currentWx008Data);
                }
                //刷阅读
                if(AutoUtil.actionContains(record,"kzgz")){
                    LogUtil.d(TAG,"kzgz-- "+record+" loginIndex:"+loginIndex+" isLoginSucessPause:"+isLoginSucessPause);
                    LoginSuccessActionConfig.kzgz(root,record,currentWx008Data,context);
                }
                //微信id添加好友
                if(AutoUtil.actionContains(record,"af")){
                    LogUtil.d(TAG,"af-- "+record+" loginIndex:"+loginIndex+" isLoginSucessPause:"+isLoginSucessPause);
                    LoginSuccessActionConfig.wxidaf(root,record,currentWx008Data,context);
                }
                //取关公众号
                if(AutoUtil.actionContains(record,"qggzh")){
                    LogUtil.d(TAG,"qggzh-- "+record+" loginIndex:"+loginIndex+" isLoginSucessPause:"+isLoginSucessPause);
                    LoginSuccessActionConfig.qggzh(root,record,currentWx008Data,context);
                }
                //recfnd通过好友
                if(AutoUtil.actionContains(record,"recfnd")){
                    LogUtil.d(TAG,"recfnd-- "+record+" loginIndex:"+loginIndex+" isLoginSucessPause:"+isLoginSucessPause);
                    LoginSuccessActionConfig.recfnd(root,record,currentWx008Data,context);
                }
                //获取昵称
                if(AutoUtil.actionContains(record,"getNickName")){
                    LogUtil.d(TAG,"getNickName-- "+record+" loginIndex:"+loginIndex+" isLoginSucessPause:"+isLoginSucessPause);
                    LoginSuccessActionConfig.getNickName(root,record,currentWx008Data,context,parameters);
                }


            }catch (Exception e){
              AutoUtil.recordAndLog(record,"抛出异常");
              LogUtil.logError(e);
            }

        }
    }


    //自动登录配置
    boolean dragFlag = true;//滑动滑块标记
    public boolean autoLoginConfig(AccessibilityNodeInfo root,Map<String,String> record){
        if(NodeActionUtil.isContainsStrs(root,"登录|注册|语言")){
            AutoUtil.performClick(AutoUtil.findNodeInfosByText(root,"登录"),record,"wx点击登陆1",500);
        }
        //丢弃，污点语言按钮
        //NodeActionUtil.doClickByNodePathAndText(root,"注册|语言","00","登录",record,"wx点击登陆1",500);

        boolean flag = false;
        String wxid = (currentWx008Data.getWxId()!=null&&currentWx008Data.getWxId().length()==6)?currentWx008Data.getWxId():currentWx008Data.getWxid19();
        String pwd = currentWx008Data.getWxPwd();
        //微信号为空用手机号登陆
        if("1".equals(isLoginByPhone)||TextUtils.isEmpty(wxid)){
            String cnNum = currentWx008Data.getCnNum();
            if(cnNum!=null&&!"86".equals(cnNum)){
                selsectCn(root,cnNum);
            }
            if("86".equals(cnNum)||cnNum==null||AutoUtil.checkAction(record,"wx选择国家")||AutoUtil.checkAction(record,"wx输入手机号")){
                String phone = currentWx008Data.getPhone();
                if("255".equals(cnNum)&&"255".equals(phone.substring(0,3))){
                    phone = phone.substring(3);
                }
                NodeActionUtil.doInputByNodePathAndText(root,"国家/地区|下一步|请填写手机号","00221",phone,record,"wx输入手机号",500);
                NodeActionUtil.doClickByNodePathAndText(root,"国家/地区|下一步|请填写手机号","0024","下一步",record,"wx下一步",1000);

            }
            NodeActionUtil.doInputByNodePathAndText(root,"手机号登录|用短信验证码登录","00231",pwd,record,"wx输入密码",1000);
            NodeActionUtil.doClickByNodePathAndText(root,"手机号登录|用短信验证码登录","0025","登录",record,"wx点击登录",1500);
           /* if(!NodeActionUtil.doInputByNodePathAndText(root,"手机号登录|用短信验证码登录","00331",pwd,record,"wx输入密码",1000)){

            }*/
            if(!NodeActionUtil.doClickByNodePathAndText(root,"手机号登录|用短信验证码登录","0035","登录",record,"wx点击登录",3000)){

            }
        }else {
            NodeActionUtil.doClickByNodePathAndText(root,"请填写手机号|手机号登录","0033","用微信号/QQ号/邮箱登录",record,"wx点击微信号/QQ号/邮箱登录");
            if(NodeActionUtil.isContainsStrs(root,"请填写微信号/QQ号/邮箱|微信号/QQ/邮箱登录")){
                String phoneTag = FileUtil.readAllUtf8(FilePathCommon.phoneTagPath);
                System.out.println("phoneTag-->"+phoneTag);
               if(!phoneTag.equals(TextUtils.isEmpty(currentWx008Data.getPhone())?currentWx008Data.getWxId():currentWx008Data.getPhone())){
                    System.out.println("phoneTag-->noe eq");
                    LogUtil.login(loginIndex+" exception change phone fail",currentWx008Data.getPhone()+" "+currentWx008Data.getWxId()+" "+currentWx008Data.getWxPwd()+" ip:"+record.remove("ipMsg"));
                    AutoUtil.recordAndLog(record,"wx改机失败");
                    return false;
                }
            }
            /**
             * 6.5.16版本
             */
           /* NodeActionUtil.doInputByNodePathAndText(root,"请填写微信号/QQ号/邮箱|微信号/QQ/邮箱登录","00311",wxid,record,"wx输入微信号",1500);
            NodeActionUtil.doInputByNodePathAndText(root,wxid+"|微信号/QQ/邮箱登录","00321",pwd,record,"wx输入密码",1500);
            NodeActionUtil.doClickByNodePathAndText(root,wxid+"|微信号/QQ/邮箱登录","0034","登录",record,"wx点击登录",3000);*/
            /**
             * 6.6.1版本
             */
            NodeActionUtil.doInputByNodePathAndText(root,"请填写微信号/QQ号/邮箱|微信号/QQ/邮箱登录","00211",wxid,record,"wx输入微信号",1500);
            NodeActionUtil.doInputByNodePathAndText(root,wxid+"|微信号/QQ/邮箱登录","00221",pwd,record,"wx输入密码",1500);
            NodeActionUtil.doClickByNodePathAndText(root,wxid+"|微信号/QQ/邮箱登录","0024","登录",record,"wx点击登录",1500);

        }
        //过滑块
        if(NodeActionUtil.isWindowContainStr(root,"拖动下方滑块完成拼图")&&dragFlag){
            AutoUtil.execShell("input swipe 231 711 830 711");
            AutoUtil.sleep(1000);
            AutoUtil.execShell("input swipe 231 1029 830 1029");
            AutoUtil.sleep(3000);
            dragFlag = false;
            return true;
        }
        if(NodeActionUtil.isWindowContainStr(root,"拖动下方滑块完成拼图")&&!dragFlag){
            AutoUtil.execShell("input swipe 231 1029 897 1029");
            AutoUtil.sleep(1000);
            AutoUtil.execShell("input swipe 231 711 897 711");
            //AutoUtil.execShell("input swipe 231 841");
            AutoUtil.sleep(3000);
            dragFlag = true;
        }

        NodeActionUtil.doClickByNodePathAndText(root,"提示|看看手机通讯录里谁在使用微信","02","否",record,"wx否通讯录",500);
        NodeActionUtil.doClickByNodePathAndText(root,"应急联系人|返回","002","完成",record,"wx完成应急联系人",500);

        //判断登陆成功
        if(NodeActionUtil.isContainsStrs(root,"通讯录|发现|我") &&!AutoUtil.checkAction(record,"wx飞行模式&清除数据后启动微信")){
            if(NodeActionUtil.isContainsStrs(root,"通讯录|发现|我")){
                /**
                 * 登录成功判断是否有其他动作
                 */
                int cn = DaoUtil.updateExpMsg(currentWx008Data,"登录成功-"+AutoUtil.getCurrentDate());
                System.out.println("excpMsg-->"+cn);
                AutoUtil.recordAndLog(record,getLoginSeccessThenDoAction(extValue));
                LogUtil.login(loginIndex+" success",currentWx008Data.getPhone()+" "+currentWx008Data.getWxId()+" "+currentWx008Data.getWxPwd()+" ip:"+record.remove("ipMsg"));
            }
           //登录成功&开启登录成功暂停 修改暂停标识为1
            if("1".equals(isLoginSucessPause)){
                parameters.setIsStop(1);
            }
            //AutoUtil.sleep(1000);
            //登陆成功或失败序号加1
            doNextIndexAndRecord2DB();
            return flag;
        }

        //判断登陆异常
        if(AutoUtil.checkAction(record,"wx点击登录")){
            String excpMsg = "";
            if(NodeActionUtil.isWindowContainStr(root,"限制登录")
                    ||NodeActionUtil.isWindowContainStr(root,"密码错误")
                    ||NodeActionUtil.isWindowContainStr(root,"长期没有使用，已被回收")
                    ||NodeActionUtil.isWindowContainStr(root,"该帐号长期未登录，为保")
                    ||NodeActionUtil.isWindowContainStr(root,"超出验证频率限制")
                    ){
                AutoUtil.recordAndLog(record,"wx登陆异常");
                excpMsg = NodeActionUtil.getTextByNodePath(root,"00");
                if(NodeActionUtil.isWindowContainStr(root,"超出验证频率限制")){
                    excpMsg="超出验证频率限制";
                }
                if(TextUtils.isEmpty(excpMsg)||"null".equals(excpMsg)){
                    excpMsg= ParseRootUtil.getCurrentViewAllTexts(root);
                }

            }else if(NodeActionUtil.isWindowContainStr(root,"你正在一台新设备登录微信")){
                AutoUtil.recordAndLog(record,"wx登陆异常");
                excpMsg= "新设备登录微信";
            }
            if(AutoUtil.checkAction(record,"wx登陆异常")){
                int cn = DaoUtil.updateExpMsg(currentWx008Data,excpMsg+"-"+AutoUtil.getCurrentDate());
                System.out.println("excpMsg-->"+cn);
                LogUtil.login(loginIndex+" fail",currentWx008Data.getPhone()+" "+currentWx008Data.getWxId()+" "+currentWx008Data.getWxPwd()+" -"+excpMsg+" ip:"+record.remove("ipMsg"));
                //登陆成功或失败序号加1
                doNextIndexAndRecord2DB();
            }
            System.out.println("excpMsg-->"+excpMsg);
        }
        return flag;
    }

    private void doNextIndexAndRecord2DB(){
        int endLoginIndex = Integer.parseInt(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_END_LOGIN_INDEX));
        if(loginIndex==wx008Datas.size()-1||loginIndex==endLoginIndex){
            AutoUtil.recordAndLog(record,"wx登陆完成序号【"+this.parameters.getStartLoginIndex()+"-"+endLoginIndex+"】");
            return;
        }
        System.out.println("errRecord-->"+record);
        System.out.println("errRecord loginIndex-->"+loginIndex);
        loginIndex = loginIndex+1;
        AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_START_LOGIN_INDEX,loginIndex+"");
    }



    //异常情况处理
    public void exceptionConfig(AccessibilityNodeInfo root,Map<String,String> record){
        NodeActionUtil.doClickByNodePathAndText(root,"SIM卡工具包|尊敬的用户","03","确定",record,"exception确定尊敬的用户",500);
        NodeActionUtil.doClickByNodePathAndText(root,"有人正通过微信密码在|修改密码","01","忽略",record,"wx忽略设备登录你的微信",500);
        NodeActionUtil.doClickByNodePathAndText(root,"玩一个小游戏才是正经事|开始游戏","0020","进入微信",record,"wx进入微信",500);
        NodeActionUtil.doClickByNodePathAndText(root,"我已了解，暂不验证|马上验证手机","003","我已了解，暂不验证",record,"wx我已了解，暂不验证",500);
    }

    //maihao
    boolean isSelectFail=false;
    public void maihao(AccessibilityNodeInfo root,Map<String,String> record){
        NodeActionUtil.doClickByNodePathAndText(root,"为了你的帐号安全|取消","02","确定",record,"wx为了你的帐号安全确定");
        NodeActionUtil.doClickByNodePathAndText(root,"你在新手机登录微信|取消","02","确定",record,"wx为了你的帐号安全确定");
        NodeActionUtil.doClickByNodePathAndText(root,"请选出你的微信曾经绑定过的手机号|验证身份","06",null,record,"wx请选出你的微信曾经绑定过的手机号");
        NodeActionUtil.doClickByNodePathAndText(root,"请关闭页面重新登录|验证通过","000003",null,record,"wx为了你的帐号安全确定");
        AccessibilityNodeInfo node = ParseRootUtil.getNodePath(root,"000006");
        System.out.println("node6-->"+node);
        if(node!=null&&node.getContentDescription().toString().contains("以上都不是")){
            System.out.println("node61-->"+node);
            boolean flag = false;
            if(NodeActionUtil.isWindowContainStr(root,"请选择你最近一次登录设备的名称")){
                //如果已经失败过一次，返回上一步
                if(isSelectFail){
                    AutoUtil.performBack(context,record,"wx避开返回上一步");
                    return;
                }
                //情况1--iPhone OS
                for(int i=1;i<6;i++){
                    AccessibilityNodeInfo node00 = ParseRootUtil.getNodePath(root,"00000"+i);
                    if(node00.getContentDescription().toString().contains("iPhone OS")){
                        AutoUtil.performClick(node00,record,"wxiPhone OS");
                        flag = true;
                        break;
                    }
                }
                //情况2--iOS
                if(!flag){
                    for(int i=1;i<6;i++){
                        AccessibilityNodeInfo node00 = ParseRootUtil.getNodePath(root,"00000"+i);
                        if(node00.getContentDescription().toString().contains("iOS")){
                            AutoUtil.performClick(node00,record,"wxiPhone OS");
                            flag = true;
                            break;
                        }
                    }
                }
                //情况3--iPhone
                if(!flag){
                    for(int i=1;i<6;i++){
                        AccessibilityNodeInfo node00 = ParseRootUtil.getNodePath(root,"00000"+i);
                        if(node00.getContentDescription().toString().replaceAll("\\n","").equals("iPhone")){
                            AutoUtil.performClick(node00,record,"wxiPhone OS");
                            flag = true;
                            break;
                        }
                    }
                }
                if(!flag){
                    AutoUtil.performClick(node,record,"wx以上都不是");
                }

            }else {
                System.out.println("node62-->"+node);
                AutoUtil.performClick(node,record,"wx以上都不是");
               if(AutoUtil.checkAction(record,"wx以上都不是")){
                    AccessibilityNodeInfo node1 = ParseRootUtil.getNodePath(root,"000008");
                    AutoUtil.performClick(node1,record,"wx以上都不是下一步");
                }

            }

        }

        if(AutoUtil.checkAction(record,"wx以上都不是")||AutoUtil.checkAction(record,"wxiPhone OS")||AutoUtil.checkAction(record,"wx以上都不是下一步")){
            AccessibilityNodeInfo node1 = ParseRootUtil.getNodePath(root,"000008");
            boolean cb = AutoUtil.performClick(node1,record,"wx以上都不是下一步");
            System.out.println("cb-->"+cb+" node1:"+node1);
        }
        if(NodeActionUtil.doClickByNodePathAndDesc(root,"验证身份|验证失败","000003","关闭页面",record,"wx验证失败关闭页面",500)){
            isSelectFail = true;
        }
    }


    boolean clickFlag = false;
    private void selsectCn(AccessibilityNodeInfo root,String cn_num){
        System.out.println("cn_num--->"+cn_num);
        if(!"86".equals(cn_num)){
            //点击进入国家列表
            AccessibilityNodeInfo cn1 = AutoUtil.findNodeInfosByText(root,"国家/地区");
            if(cn1!=null){
                AccessibilityNodeInfo cnNode1 = ParseRootUtil.getNodePath(root,"00311");
                if(cnNode1!=null&&"中国（+86）".equals(cnNode1.getText()+"")&&!AutoUtil.checkAction(record,"wx点击国家地区")){
                    AutoUtil.performClick(cnNode1,record,"wx点击国家地区");
                }
                return;
            }
            //国家号码遍历查找
            if(AutoUtil.checkAction(record,"wx点击国家地区")||AutoUtil.checkAction(record,"wx下滚")){
                if("62".equals(cn_num)&&!clickFlag){
                    AutoUtil.sleep(500);
                    AutoUtil.clickXY(1043,1768);
                    clickFlag = true;
                }else if(("233".equals(cn_num)||"60".equals(cn_num))&&!clickFlag){
                    AutoUtil.sleep(500);
                    AutoUtil.clickXY(1043,888);
                    clickFlag = true;
                }

                List<AccessibilityNodeInfo> cnNumNodes =  root.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/ip");//国家数字号节点
                System.out.println("cnNode text cnNumNodes-->"+cnNumNodes);
                if(cnNumNodes!=null&&cnNumNodes.size()>0){
                    for(AccessibilityNodeInfo cnNode:cnNumNodes){
                        //找到目标，点击
                        if(cn_num.equals(cnNode.getText()+"")){
                            AutoUtil.performClick(cnNode,record,"wx选择国家",3000);
                            clickFlag = false;
                            return;

                        }
                        System.out.println("cnNode text-->"+cnNode.getText());
                    }
                }

                AccessibilityNodeInfo listViewNode = AutoUtil.findNodeInfosById(root,"com.tencent.mm:id/i9");
                AutoUtil.performScroll(listViewNode,record,"wx下滚");

            }
        }

    }

    //判断登录成功需执行什么动作
    public String getLoginSeccessThenDoAction(String extValue){
        String action = "wx登陆成功";
        if("601".equals(extValue)){
            action="601SetWxidThread设置微信号";
            new Thread(new SetWxidThread(context,record,currentWx008Data)).start();
        }else {
            action = getActionByCodeNum(extValue);
        }
        return action;
    }

    public String getActionByCodeNum(String extValue){
        String isSendFr = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_SEND_FR);
        if("1".equals(isSendFr)){
            return "sendFr发圈";
        }else if("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_AF_BY_WXID))){
            return "af添加好友";
        }else if("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_RC_FRIEND))){
            return "recfnd通过好友";
        }else if("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_CHANGE_PWD))){
            return "SetPwdThread设置密码";
        }else if("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_SET_WXID))){
            return "SetWxidThread设置微信号";
        }else if("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_SMJQ))){
            return "saoma扫码加群";
        }else if("1".equals(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_REP_PHONE))){
            return "ReplacePhoneThread换绑手机";
        }

        Map<String,String> actions = new HashMap<String,String>();
        actions.put("602","SetPwdThread设置密码");
        actions.put("603","loginPc扫码登录");
        actions.put("604","loginSNName搜索微信号");
        actions.put("6051","ReplacePhoneThread换绑手机");
        actions.put("6050","ReplacePhoneThread换绑手机");
        actions.put("606","saoma扫码加群");
        actions.put("607","sendFr发圈");
        actions.put("608","kzgz挂机");
        actions.put("609","af添加好友");
        actions.put("610","qggzh取关公总号");
        actions.put("611","recfnd通过好友");
        actions.put("612","getNickName获取昵称");
        actions.put("609603","af添加好友&扫码登录");
        if(actions.containsKey(extValue)){
            return actions.get(extValue);
        }else {
            return "wx登陆成功";
        }

    }

    //记录悬浮框信息
    public void recordFlowInfo(List<Wx008Data> wx008Datas,int loginIndex){
        record.put("total",wx008Datas==null?"0":wx008Datas.size()+"");//记录总数，悬浮框显示
        record.put("loginIndex",loginIndex+"");
    }

    //记录外网ip
    public void recordIp(){
        if(record.get("ipMsg")==null){
            System.out.println("recordIp--->开始记录ip");
            record.put("ipMsg","开启ip记录线程");
            new IpNetThread(record).start();
        }
    }


}
