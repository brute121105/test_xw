package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import hyj.xw.BaseThread;
import hyj.xw.GlobalApplication;
import hyj.xw.aw.sysFileRp.CreatePhoneEnviroment;
import hyj.xw.common.CommonConstant;
import hyj.xw.common.FilePathCommon;
import hyj.xw.conf.PhoneConf;
import hyj.xw.conf.WindowNodeInfoConf;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.hook.newHook.NewPhoneInfo;
import hyj.xw.model.AccessibilityParameters;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.model.WindowNodeInfo;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.DaoUtil;
import hyj.xw.util.DragImageUtil2;
import hyj.xw.util.FileUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.NodeActionUtil;
import hyj.xw.util.OkHttpUtil;
import hyj.xw.util.ParseRootUtil;
import hyj.xw.util.WindowOperationUtil;

import static android.R.attr.borderlessButtonStyle;
import static android.R.attr.label;

/**
 * Created by Administrator on 2018/06/10.
 */

public class AutoOperatonThread extends BaseThread {
    public  final String TAG = this.getClass().getSimpleName();
    private int countRootNull =0;
    private int countSendMsgNum =0;
    private int actionNo=0;//记录点击所处在位置
    private int loopNum =0;//标志循环到底部次数
    private Map<Integer,List<WindowNodeInfo>> wInfoMap;
    private int loginIndex,endLoginIndex;//开始、结束 登录序号
    private String isLoginSucessPause;//登录成功是否暂停
    private List<Wx008Data> wx008Datas;
    private Wx008Data currentWx008Data;//当前运行wx数据
    private String extValue,isAirChangeIp,isLoginByPhone;
    private boolean isStartAirPlaneMode = false;
    private List<String> loginSussDos=new ArrayList<String>();
    private String operation;//操作,注册、养号...
    private String lastWindowText;//上次窗口文本
    private int countSameCurrentWindowCum=0;//记录一直处在当前界面的次数
    public AutoOperatonThread(AccessibilityService context, Map<String, String> record, AccessibilityParameters parameters){
        super(context,record,parameters);
        intiParam();
    }
    private void intiParam(){
        AutoUtil.recordAndLog(record,"init");
        //loginSussDos.add("注册");
        loginSussDos.add("养号");
        loginSussDos.add("加好友");
        //loginSussDos.add("提取wxid");
        //loginSussDos.add("发圈");
        //loginSussDos.add("关手机号搜索");
        /*
        loginSussDos.add("修改密码");*/
        operation = loginSussDos.get(0);
        wInfoMap = WindowNodeInfoConf.getWinfoMapByOperation(operation);
        System.out.println("wInfoMap-->"+JSON.toJSONString(wInfoMap));
        wx008Datas = DaoUtil.getWx008Datas();
        loginIndex = Integer.parseInt(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX));
        isLoginSucessPause = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_LOGIN_PAUSE);
        extValue = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_EXT);
        isAirChangeIp = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_AIR_CHANGE_IP);
        isLoginByPhone = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_LOGIN_BY_PHONE);
        if(loginSussDos.get(0).equals("注册")){
            currentWx008Data = PhoneConf.createRegData();
            currentWx008Data.save();
        }else {
            currentWx008Data = wx008Datas.get(loginIndex);
        }
    }
    @Override
    public Object call() {
        while (true){
            try {
                AutoUtil.sleep(500);
                LogUtil.d(TAG,"loginIndex:"+loginIndex+Thread.currentThread().getName()+" actionNo:"+actionNo+" record:"+record+" operation:"+operation);
                //保持屏幕常亮
                AutoUtil.wake();
                AccessibilityNodeInfo root = context.getRootInActiveWindow();
                //roor超过5次为空，启动wx
                if(root==null){
                    continue;
                }


                //判断窗口静止时间
                String newCurrentWindowText = ParseRootUtil.getCurrentViewAllTexts(root);
                if(newCurrentWindowText.equals(lastWindowText)){
                    countSameCurrentWindowCum = countSameCurrentWindowCum+1;
                    System.out.println("countSameCurrentWindowCum--->"+countSameCurrentWindowCum);
                }else {
                    countSameCurrentWindowCum = 0;
                    lastWindowText = newCurrentWindowText;
                }


                if(newCurrentWindowText.indexOf("载入数据...")>-1||newCurrentWindowText.indexOf("登录...")>-1
                        ||newCurrentWindowText.indexOf("|progressBar|")>-1||newCurrentWindowText.indexOf("|加载中|")>-1) continue;
                //if(NodeActionUtil.isContainsStrs(root,"数据...")||NodeActionUtil.isContainsStrs(root,"登录...")) continue;
                ParseRootUtil.debugRoot(root);
                if(parameters.getIsStop()==1){
                    LogUtil.d(TAG,"暂停....");
                    continue;
                }
                System.out.println("getPackageName--->"+root.getPackageName());

                /*boolean flag = doAddFriend();
                System.out.println("doAction-->发送结果："+flag);
                if(true) continue;*/

                List<WindowNodeInfo> wInfos = wInfoMap.get(actionNo);//获取当前执行动作
                System.out.println("wInfos--->"+JSON.toJSONString(wInfos));
                //处理清除数据失败
                if("养号".equals(operation)){
                    setNodeInputText(wInfos,currentWx008Data);//设置输入框文本
                }else if("修改密码".equals(operation)){
                    setNodeInputTexChangePwd(wInfos,currentWx008Data);
                }else if("发圈".equals(operation)){
                    setSendFrContent(wInfos,currentWx008Data);
                }else if("注册".equals(operation)){
                    setRegContent(wInfos,currentWx008Data);
                }
                //if(1==1) continue;

                if(doActions(root,wInfos)){
                    if(CommonConstant.APPCONFIG_APM.equals(wInfos.get(0).getActionDesc())&&!waitAriplaneModeSuc(root)) continue;//飞行模式后网络没恢复，返回继续等待
                    String msg = getExpMsg(wInfos,root);//捕获处理异常界面消息
                    if(msg.contains("随机界面点击")) continue;//随机界面点击 actionNo不递增
                    if(wInfoMap.keySet().size()-1==actionNo||msg.contains("登录异常")){
                        doLoginFinish(msg);
                    }else {
                        ++actionNo;
                    }
                    loopNum = 0;
                    continue;
                }else if(!validExist008(wInfoMap)&&CommonConstant.APPCONFIG_VEVN.equals(wInfos.get(0).getActionDesc())){//校验改机是否成功，008改机不校验
                    //如果改机不成功, actionNo = 0 重新登
                    actionNo = 0;
                    initWInfoFlag(wInfoMap);
                    continue;
                }else {// 点击不成功，actionNo-1
                    //if(wInfos.get(0).getNodeType()==0||wInfos.get(0).getNodeType()==5) continue;//只处理点击动作
                    if(wInfos.get(0).getActionDesc().equals(CommonConstant.APPCONFIG_CEVN)||wInfos.get(0).getActionDesc().equals(CommonConstant.APPCONFIG_APM)||wInfos.get(0).getActionDesc().equals(CommonConstant.APPCONFIG_008)
                            ||wInfos.get(0).getActionDesc().equals(CommonConstant.APPCONFIG_VPN)||wInfos.get(0).getActionDesc().equals(CommonConstant.APPCONFIG_SWX)
                            ||wInfos.get(0).getActionDesc().equals(CommonConstant.APPCONFIG_GHK)) continue;
                    ++loopNum;
                    System.out.println("loopNum--->"+loopNum);
                    if(loopNum>5&&actionNo>1){
                        List<WindowNodeInfo> tempWInfos = wInfoMap.get(actionNo-1);
                        while (tempWInfos!=null){
                            if(tempWInfos.get(0).getActionDesc().equals(CommonConstant.APPCONFIG_SWX)){
                                actionNo = wInfoMap.keySet().size()-1;
                                break;
                            }else {
                                actionNo = actionNo-1;//执行不成功，返回上一级动作
                                if(tempWInfos.get(0).getNodeType()>0&&!tempWInfos.get(0).getActionDesc().equals("点击已发送短信下一步")){
                                    break;
                                }else {
                                    tempWInfos = wInfoMap.get(actionNo-1);
                                }
                            }
                        }
                    }
                }

                //执行动作后返回初始界面
                if(!operation.equals(loginSussDos.get(0))&&actionNo==0&&root.getPackageName().toString().contains("tencent")){
                    if(AutoUtil.findNodeInfosByText(root,"我")==null){
                        WindowOperationUtil.performBack(context);
                        System.out.println("performBack--->");
                    }
                }

            }catch (Exception e){
              LogUtil.logError(e);
            }
        }
    }

    private void doLoginFinish(String msg){
        actionNo=0;
        countSendMsgNum=0;
        initWInfoFlag(wInfoMap);
        if(loginSussDos.indexOf(operation)==loginSussDos.size()-1||msg.contains("登录异常")){//登录成功没有其他operation，登录下一个
            operation = loginSussDos.get(0);
            wInfoMap = WindowNodeInfoConf.getWinfoMapByOperation(operation);//重新养号或注册
            int cn = DaoUtil.updateExpMsg(currentWx008Data,msg+"-"+AutoUtil.getCurrentDate());
            System.out.println("excpMsg-->"+cn);
            LogUtil.login(loginIndex+" msg:"+msg,currentWx008Data.getPhone()+" "+currentWx008Data.getWxId()+" "+currentWx008Data.getWxPwd()+" ip:"+record.remove("ipMsg"));
            if(loginSussDos.get(0).equals("注册")){
                currentWx008Data = PhoneConf.createRegData();
                currentWx008Data.save();
            }else {
                doNextIndexAndRecord2DB();
                currentWx008Data = wx008Datas.get(loginIndex);
            }
        }else {
            operation = loginSussDos.get(loginSussDos.indexOf(operation)+1);
            wInfoMap = WindowNodeInfoConf.getWinfoMapByOperation(operation);
        }
    }

    public  boolean doActions(AccessibilityNodeInfo root,List<WindowNodeInfo> wInfos){
        boolean flag = false;
        if(wInfos!=null&&wInfos.size()>0){
            if(wInfos.get(0).getNodeType()>0&&root.getPackageName().toString().indexOf("miui")>-1) return false;//点击事件，监听到不符合应用包
            if(wInfos.get(wInfos.size()-1).isActionResultFlag()&&wInfos.get(wInfos.size()-1).getRetryFlag()==1) return false;//点击了一次不再点击
            for(int i=0,l=wInfos.size();i<l;i++){
                flag = doAction(root,wInfos.get(i));
                System.out.println("doActions-->:"+wInfos.get(i).getActionMsg()+flag);
                if(flag){//如果点击 点击为 true
                    if("登录异常".equals(wInfos.get(i).getActionDesc())
                            ||(i<wInfos.size()-1&&("登录异常".equals(wInfos.get(i+1).getActionDesc())||wInfos.get(i+1).getActionDesc().contains("随机界面点击")))
                    ){//如果是点击异常true 或 判断登录成功true 直接返回，不需继续执行
                        return flag;
                    }
                }
            }
        }
        return flag;
    }
    public  boolean doAction(AccessibilityNodeInfo root,WindowNodeInfo info){
        boolean flag = false;
        if(CommonConstant.APPCONFIG_SWX.equals(info.getActionDesc())){
            WindowOperationUtil.startWx();//启动微信
            flag = true;
        }else if(CommonConstant.APPCONFIG_CEVN.equals(info.getActionDesc())){
            File file = new File("/data/data/com.tencent.mm/MicroMsg");
            System.out.println("file--->"+file.isDirectory());
            if(file.isDirectory()){//判断是否删除
                clearEnviroment();//清除并准备改机环境
            }else {
                flag = true;
            }
        }else if(CommonConstant.APPCONFIG_VLS.equals(info.getActionDesc())){
            if(validLoginSucc(root)){//判断登录成功
                flag = true;
            }
        }else if(CommonConstant.APPCONFIG_VEVN.equals(info.getActionDesc())){
            if(info.isActionResultFlag()||validExist008(wInfoMap)||validEnviroment()){//判断改机成功 上校次校验为true不再校验，008不校验
                flag = true;
            }
        }else if(CommonConstant.APPCONFIG_APM.equals(info.getActionDesc())){
             if(!info.isActionResultFlag()){//开启飞行模式,只开启一次默认为开启成功
                 AutoUtil.setAriplaneMode(1000);
             }
            flag = true;
        }else if(info.getNodeType()>0){
            //windowsText不为空，校验
            if(TextUtils.isEmpty(info.getWindowText())||"窗口文本".equals(info.getWindowText())
                    ||(!"窗口文本".equals(info.getWindowText())&&NodeActionUtil.isContainsStrs(root,info.getWindowText()))
                    ){
                if(1==info.getNodeType()){//按钮控件
                    flag = WindowOperationUtil.performClick(WindowOperationUtil.getNodeByInfo(root,info),info);
                }else if(2==info.getNodeType()){//输入框控件
                    flag = WindowOperationUtil.performSetText(WindowOperationUtil.getNodeByInfo(root,info),info);
                }else if(3==info.getNodeType()){//异常窗口
                    flag = NodeActionUtil.isWindowContainStr(root,info.getNodeText());
                }else if(4==info.getNodeType()){//开关按钮
                    flag = WindowOperationUtil.performClickByRect(WindowOperationUtil.getNodeByInfo(root,info),info);
                }else if(5==info.getNodeType()){//文本，根据所给的文本内容，判断本窗口是否有该文本
                    if(WindowOperationUtil.getNodeByInfo(root,info)!=null){
                        flag = true;
                    }
                }else if(6==info.getNodeType()){//按钮控件
                    flag = WindowOperationUtil.performLongClick(WindowOperationUtil.getNodeByInfo(root,info),info);
                }else if(7==info.getNodeType()){//获取指定路径节点文本，并存入inputText
                    AccessibilityNodeInfo node = WindowOperationUtil.getNodeByInfo(root,info);
                    if(node!=null&&!TextUtils.isEmpty(node.getText())){
                        info.setInputText(node.getText()+"");
                        flag = true;
                    }
                }
            }
        }else if(CommonConstant.APPCONFIG_VPN.equals(info.getActionDesc())){
            if(!info.isActionResultFlag()&&doVPN(root)){
                flag = true;
            }
        }else if(CommonConstant.APPCONFIG_008.equals(info.getActionDesc())){
            if(!info.isActionResultFlag()&&set008Data(root)){
                flag = true;
            }
        }else if(CommonConstant.APPCONFIG_GHK.equals(info.getActionDesc())){
            if(!info.isActionResultFlag()&&NodeActionUtil.isContainsStrs(root,"拖动下方滑块完成拼图")){
                File pic = new File(FilePathCommon.fkScreenShotPath);
                boolean isDelPicFlag = pic.delete();
                while (pic==null||isDelPicFlag){//删除图片后等待jsdroid生成新截图
                    AutoUtil.sleep(1000);
                    System.out.println("doA--删除图片后等待jsdroid生成新截图 ");
                    Bitmap bi = BitmapFactory.decodeFile(FilePathCommon.fkScreenShotPath);
                    if(bi!=null){
                        System.out.println("doA--生成新截图 ");
                        int pic2X = DragImageUtil2.getPic2LocX(bi);
                        String swipeParamsStr = createSwipeParams(235,pic2X,1029);
                        FileUtil.writeContent2FileForceUtf8(FilePathCommon.fkFilePath,swipeParamsStr);
                        System.out.println("doA--写入拖动距离："+swipeParamsStr);
                        flag = true;
                        AutoUtil.sleep(5000);
                        break;
                    }
                }
            }
        }else if("开始加好友".equals(info.getActionDesc())){
            doAddFriend();
        }
        info.setActionResultFlag(flag);//修改执行结果标识
        return flag;
    }

    String doAddFriendAction = "";
    private boolean doAddFriend(){
        doAddFriendAction = "init";
        AutoUtil.execShell("am start -n com.tencent.mm/.plugin.subapp.ui.pluginapp.AddMoreFriendsUI");
        while (true){
            AutoUtil.sleep(1000);
            AccessibilityNodeInfo root = context.getRootInActiveWindow();
            if(root==null){
                AutoUtil.sleep(1000);
                continue;
            }
            ParseRootUtil.debugRoot(root);
            AccessibilityNodeInfo nodeInfo1 = AutoUtil.findNodeInfosByText(root,"微信号/QQ号/手机号");
            if(nodeInfo1!=null){
                Rect rect = new Rect();
                nodeInfo1.getBoundsInScreen(rect);
                AutoUtil.clickXY(rect.centerX(),rect.centerY());
            }
            WindowOperationUtil.performClickTest(nodeInfo1);
            AccessibilityNodeInfo nodeInfo2 = ParseRootUtil.getNodePath(root,"002");
            WindowOperationUtil.performSetTextTest(nodeInfo2,"fz2018802");
            AccessibilityNodeInfo nodeInfo3 = ParseRootUtil.getNodePath(root,"004000");
            if(nodeInfo3!=null&&nodeInfo3.getText().toString().contains("搜索")){
               WindowOperationUtil.performClickTest(nodeInfo3);
            }
            AccessibilityNodeInfo nodeInfo4 = AutoUtil.findNodeInfosByText(root,"添加到通讯录");
            WindowOperationUtil.performClickTest(nodeInfo4);

            AccessibilityNodeInfo nodeInfo5 = AutoUtil.findNodeInfosByText(root,"发消息");
            WindowOperationUtil.performClickTest(nodeInfo5);

            AccessibilityNodeInfo nodeInfo6 = ParseRootUtil.getNodePath(root,"0001010");//消息发送输入框
            WindowOperationUtil.performSetTextTest(nodeInfo6,"12345678");

            AccessibilityNodeInfo nodeInfo7 = AutoUtil.findNodeInfosByText(root,"发送");
            boolean flag = WindowOperationUtil.performClickTest(nodeInfo7);
            if(flag){
                AccessibilityNodeInfo nodeInfo8 = ParseRootUtil.getNodePath(root,"0010");//左上角返回
                Rect rect = new Rect();
                nodeInfo8.getBoundsInScreen(rect);
                AutoUtil.clickXY(rect.centerX(),rect.centerY());
                doAddFriendAction = "点击返回主界面";
                AutoUtil.sleep(5000);
                continue;
            }

            if("点击返回主界面".equals(doAddFriendAction)){//判断是否发送成功
                List<AccessibilityNodeInfo> nodes1 = root.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/apx");
                List<AccessibilityNodeInfo> nodes2 = root.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/apw");
                if(nodes1.size()>0&&nodes2.size()>0){
                    String text1 = nodes1.get(0).getText()+"";
                    String text2 = nodes2.get(0).getText()+"";
                    System.out.println("doAction-->text:"+text1+" text2:"+text2);
                    if("12345678".equals(text1)&&!text2.contains("发送中")){
                        System.out.println("doAction--->添加成功");
                        return true;
                    }else {
                        return false;
                    }
                }else {
                    return false;
                }
            }

        }
    }

    /**
     * @param startX 拖动起始startX位置
     * @param endX 拖动到endX位置
     * @param locY 拖动Y方向
     * @return 方块拖动距离
     */
    private String createSwipeParams(int startX,int endX,int locY){
        String swipeParamsStr = "";
        StringBuilder sb = new StringBuilder();
        sb.append(startX+",");
        sb.append(locY);
        return swipeParamsStr;
    }

    //等待飞行模式开启成功
    private boolean waitAriplaneModeSuc(AccessibilityNodeInfo root){
        boolean flag = false;
       /* if(WindowOperationUtil.findNodeInfosByText(root,"SIM卡工具包")!=null){
             flag = WindowOperationUtil.performClickTest(WindowOperationUtil.findNodeInfosByText(root,"确定"));
        }*/
        flag = AutoUtil.isNetworkConnected();
        return flag;
    }

    private boolean validLoginSucc(AccessibilityNodeInfo root){
        boolean flag = false;
        if(NodeActionUtil.isContainsStrs(root,"通讯录|发现|我")&&!NodeActionUtil.isContainsStrs(root,"Weixin Privacy Protection Guideline")){
            //登录成功&开启登录成功暂停 修改暂停标识为1
            if("1".equals(isLoginSucessPause)){
                parameters.setIsStop(1);
            }
            flag = true;
        }
        return flag;
    }

    //判断改机是否成功 改机成功返回true
    private boolean validEnviroment(){
        String phoneTag = FileUtil.readAllUtf8(FilePathCommon.phoneTagPath);
        String phoneTag008 = TextUtils.isEmpty(currentWx008Data.getPhone())?currentWx008Data.getWxId():currentWx008Data.getPhone();
        System.out.println("phoneTag-->"+phoneTag+" phoneTag008:"+phoneTag008);
        if(!phoneTag.equals(phoneTag008)){
            LogUtil.login(loginIndex+" exception change phone fail",currentWx008Data.getPhone()+" "+currentWx008Data.getWxId()+" "+currentWx008Data.getWxPwd()+" ip:"+record.remove("ipMsg"));
            return false;
        }else {
            return true;
        }
    }

    private void clearEnviroment(){
        AutoUtil.clearAppData();
        AutoUtil.sleep(2000);

        NewPhoneInfo pi = null;
        if(!TextUtils.isEmpty(currentWx008Data.getPhoneStrsAw())){//aw数据
            pi = JSON.parseObject(currentWx008Data.getPhoneStrsAw(),NewPhoneInfo.class);
            if(TextUtils.isEmpty(pi.getRgPhoneNo())){
                pi.setRgPhoneNo(pi.getLine1Number());
            }
        }else {
            pi = PhoneConf.xw2awData(currentWx008Data);
        }
        pi.setCpuName(pi.getCpuName().trim().toLowerCase());
        CreatePhoneEnviroment.create(GlobalApplication.getContext(),pi);
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.baseAppPathAW,FilePathCommon.npiFileName, JSON.toJSONString(pi));

    }

    private void setNodeInputText(List<WindowNodeInfo> wInfos,Wx008Data currentWx008Data){
        for(WindowNodeInfo info:wInfos){
            //info.setCurrentWx008Data(currentWx008Data);
            if(info.getNodeType()==2){
                if("输入账号".equals(info.getActionDesc())){
                    String account = "";
                    if(!TextUtils.isEmpty(currentWx008Data.getWxid19())){
                        account = currentWx008Data.getWxid19();
                    }else if(!TextUtils.isEmpty(currentWx008Data.getWxId())){
                        account = currentWx008Data.getWxId();
                    } else if(!TextUtils.isEmpty(currentWx008Data.getPhone())){
                        account = currentWx008Data.getPhone();
                    }
                    info.setInputText(account);
                }else if("输入密码".equals(info.getActionDesc())){
                    info.setInputText(TextUtils.isEmpty(currentWx008Data.getWxPwd())?"NULLNULL":currentWx008Data.getWxPwd());
                }
            }
        }
    }
    private void setNodeInputTexChangePwd(List<WindowNodeInfo> wInfos,Wx008Data currentWx008Data){
        for(WindowNodeInfo info:wInfos){
            if(info.getNodeType()==2){
                if("填写原密码".equals(info.getActionDesc())){
                    info.setInputText(currentWx008Data.getWxPwd());
                }else if("填写新密码".equals(info.getActionDesc())||"再次填写确认".equals(info.getActionDesc())){
                    info.setInputText(getNewPwd(currentWx008Data.getPhone()));
                }
            }
        }
    }
    private void setSendFrContent(List<WindowNodeInfo> wInfos,Wx008Data currentWx008Data){
        for(WindowNodeInfo info:wInfos){
            if(info.getNodeType()==2){
                if("输入发圈内容".equals(info.getActionDesc())){
                    info.setInputText(currentWx008Data.getPhone()+"tt");
                }
            }
        }
    }
    private void setRegContent(List<WindowNodeInfo> wInfos,Wx008Data currentWx008Data){
        for(WindowNodeInfo info:wInfos){
            if(info.getNodeType()==2){
                if("输入昵称".equals(info.getActionDesc())){
                    info.setInputText(currentWx008Data.getNickName());
                }else if("输入手机号".equals(info.getActionDesc())){
                    info.setInputText(currentWx008Data.getPhone());
                }else if("输入密码".equals(info.getActionDesc())){
                    info.setInputText(currentWx008Data.getWxPwd());
                }
            }
        }
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
    //重置动作执行标志位
    private void initWInfoFlag(Map<Integer,List<WindowNodeInfo>> wInfoMap){
        for(Integer key:wInfoMap.keySet()){
            List<WindowNodeInfo> wInfos = wInfoMap.get(key);
            for(WindowNodeInfo wInfo:wInfos){
                wInfo.setActionResultFlag(false);
            }
        }
    }
    //判断是否008改机
    private boolean validExist008(Map<Integer,List<WindowNodeInfo>> wInfoMap){
        for(Integer key:wInfoMap.keySet()){
            List<WindowNodeInfo> wInfos = wInfoMap.get(key);
            for(WindowNodeInfo wInfo:wInfos){
                if(CommonConstant.APPCONFIG_008.equals(wInfo.getActionDesc())){
                    return true;
                }
            }
        }
        return false;
    }
    //捕获登录异常界面消息
    private String getExpMsg(List<WindowNodeInfo> wInfos,AccessibilityNodeInfo root){
        String expMsg = "success";
        if(wInfos!=null&&wInfos.size()>0){
            for(WindowNodeInfo wInfo:wInfos){
                if(wInfo.isActionResultFlag()){//如果点击登录异常 和 随机界面为true
                    if("登录异常".equals(wInfo.getActionDesc())){
                        expMsg = "登录异常-"+wInfo.getNodeText();
                        break;
                    }else if("随机界面".equals(wInfo.getActionDesc())){
                        expMsg="随机界面点击";
                        break;
                    }else if("判断密码设置成功".equals(wInfo.getActionDesc())){//密码设置成功，数据修改
                        String newPwd = getNewPwd(currentWx008Data.getPhone());
                        int cn = DaoUtil.updatePwd(currentWx008Data,newPwd);
                        System.out.println("SetPwdThread-->cn:"+ cn+" wInfo.getInputText():"+newPwd);
                    }else if("点击发送短信".equals(wInfo.getActionDesc())){//发送短信
                        AutoUtil.sleep(3000);
                        AutoUtil.startWx();
                        System.out.println("doActions启动微信");
                        String resMsg = sendPhoneMsg(root);
                        if("发送失败".equals(resMsg)){
                            expMsg = "登录异常-发送短信失败";
                            break;
                        }
                    }else if("点击已发送短信下一步".equals(wInfo.getActionDesc())){//发送短信
                        countSendMsgNum = countSendMsgNum + 1;
                        if(countSendMsgNum>WindowNodeInfoConf.MaxSendMsgNum){
                            expMsg = "登录异常-tx未收到短信";
                            break;
                        }
                    }else if("获取nodeText".equals(wInfo.getActionDesc())){
                        String text = wInfo.getInputText();
                        if(text.indexOf("wxid_")>-1){
                            String wxid = text.substring(text.indexOf("wxid_"));
                            currentWx008Data.setWxid19(wxid);
                            System.out.println("wxid inputText--->"+wxid);
                        }
                        System.out.println("inputText--->"+text);
                    }else if("添加好友完成".equals(wInfo.getActionDesc())){
                        currentWx008Data.setFriends(currentWx008Data.getFriends()+"bbb");
                        int cn = currentWx008Data.updateAll("phone=?","");
                        //System.out.println("doAction--->添加好友完成 cn:"+cn);
                        System.out.println("doAction--->添加好友完成");

                    }
                }
            }
        }
        return expMsg;
    }
    private String sendPhoneMsg(AccessibilityNodeInfo root){
        String resMsg = "发送失败";
        String callNumber=currentWx008Data.getPhone();
        AccessibilityNodeInfo contentNode = WindowOperationUtil.getNodeByInfo(root,WindowNodeInfoConf.zcSendMsgContentWni);
        AccessibilityNodeInfo phoneNode = WindowOperationUtil.getNodeByInfo(root,WindowNodeInfoConf.zcSendMsgCalledPhoneWni);
        String content="";
        String calledNumber="";
        if(contentNode!=null&&phoneNode!=null&&contentNode.getText().toString().contains("发送 ")&&phoneNode.getText().toString().contains("到 ")){
            String contentNodeValue = contentNode.getText().toString();
            String phoneNodeValue = phoneNode.getText().toString();
            content = contentNodeValue.substring(contentNodeValue.indexOf(" ")+1);
            calledNumber = phoneNodeValue.substring(phoneNodeValue.indexOf(" ")+1);
            String url = WindowNodeInfoConf.sendPhoneMsgUrl+"&callNumber="+callNumber+"&calledNumber="+calledNumber+"&content="+content;
            System.out.println("doActions sendPhoneMsg url-->"+url);
            String resBody = null;
            try {
                resBody = OkHttpUtil.okHttpGet(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("doActions resBody sendPhoneMsg url-->"+resBody);
            if(resBody.contains("提交成功")){
                resMsg = "提交成功";
            }
        }
        return resMsg;
    }
    private String getNewPwd(String phone){
        return "www23"+phone.substring(phone.length()-3);
    }

    private boolean doVPN(AccessibilityNodeInfo root){
        if(AutoUtil.checkAction(record,"init")||AutoUtil.checkAction(record,"008保存数据")){
            AutoUtil.recordAndLog(record,"st设置VPN");
            AutoUtil.opentActivity(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
            AutoUtil.sleep(500);
        }
        AccessibilityNodeInfo linkText = AutoUtil.findNodeInfosById(context.getRootInActiveWindow(),"android:id/summary");
        if(linkText!=null&&(linkText.getText().toString().equals("正在连接...")||linkText.getText().toString().equals("Connecting…"))){
            System.out.println("hyj--->正在连接..");
            AutoUtil.sleep(1000);
            return false;
        }
        if(AutoUtil.checkAction(record,"st点击连接")){
            AccessibilityNodeInfo link =AutoUtil.findNodeInfosByText(context.getRootInActiveWindow(),"已连接");
            AccessibilityNodeInfo link1 =AutoUtil.findNodeInfosByText(context.getRootInActiveWindow(),"Connected");
            if(link!=null||link1!=null){
                AutoUtil.recordAndLog(record,"wx连接成功");
                //AutoUtil.startAppByPackName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                return true;
            }
            AccessibilityNodeInfo linkText5 = AutoUtil.findNodeInfosById(context.getRootInActiveWindow(),"android:id/summary");
            if(linkText5!=null){
                System.out.println("linkText5-->"+linkText5.getText());
            }
            if(linkText5!=null&&(linkText5.getText().toString().equals("PPTP VPN")||linkText5.getText().toString().equals("失败")||linkText5.getText().toString().equals("Unsuccessful"))){
                AutoUtil.clickXY(522,738);
                AutoUtil.recordAndLog(record,"st点击连接");
                AutoUtil.sleep(2000);
                return false;
            }
        }

        clickTextXY1(514,425,"st点击VPN","miui:id/action_bar_title","无线和网络",800);
        clickTextXY1(514,425,"st点击VPN","miui:id/action_bar_title","Wireless & networks",800);

        if(AutoUtil.checkAction(record,"st点击VPN")||AutoUtil.checkAction(record,"st弹出")||AutoUtil.checkAction(record,"st设置VPN")||AutoUtil.checkAction(record,"wx连接成功")){

            AccessibilityNodeInfo linkText4 = AutoUtil.findNodeInfosById(context.getRootInActiveWindow(),"android:id/summary");
            if(linkText4!=null){
                if(linkText4.getText().toString().equals("已连接")||linkText4.getText().toString().equals("Connected")){
                    AutoUtil.clickXY(522,738);
                    AutoUtil.recordAndLog(record,"st弹出");
                    AutoUtil.sleep(1500);

                }else if (linkText4.getText().toString().equals("PPTP VPN")||linkText4.getText().toString().equals("失败")||linkText4.getText().toString().equals("Unsuccessful")){
                    AutoUtil.clickXY(522,738);
                    AutoUtil.recordAndLog(record,"st点击连接");
                    AutoUtil.sleep(2000);
                }
            }
        }
        if(AutoUtil.checkAction(record,"st弹出")){
            AccessibilityNodeInfo dkNode = AutoUtil.findNodeInfosByText(context.getRootInActiveWindow(),"断开连接");
            AccessibilityNodeInfo dkNode1 = AutoUtil.findNodeInfosByText(context.getRootInActiveWindow(),"Disconnect");
            if(dkNode!=null||dkNode1!=null){
                AutoUtil.clickXY(756,1792);
                AutoUtil.recordAndLog(record,"st断开");
                System.out.println("hyj--->断开连接等待");
                AutoUtil.sleep(2000);
            }
            if(AutoUtil.checkAction(record,"st断开")){
                AutoUtil.clickXY(522,738);
                AutoUtil.recordAndLog(record,"st点击连接");
                AutoUtil.sleep(2000);
            }
        }
        return false;
    }
    //先判断所在页面，在点击操作
    private void clickTextXY1(int x,int y,String action,String titleId,String title,int milliSeconds){
        AccessibilityNodeInfo root = context.getRootInActiveWindow();
        if(root==null){
            LogUtil.d("myService",title+"is null");
            return;
        }
        AccessibilityNodeInfo titleNode = AutoUtil.findNodeInfosById(root,titleId);
        if(titleNode!=null&&titleNode.getText().toString().contains(title)){
            AutoUtil.execShell("input tap "+x+" "+y);
            AutoUtil.recordAndLog(record,action);
            AutoUtil.sleep(milliSeconds);
        }
    }

    private boolean set008Data(AccessibilityNodeInfo root){
        if(!root.getPackageName().toString().contains("008")){
            AutoUtil.startAppByPackName("com.soft.apk008v","com.soft.apk008.LoadActivity");
        }
        if(AutoUtil.findNodeInfosByText(root,"免费续费")!=null){
            AutoUtil.clickXY(550,650);
            return false;
        }

        AccessibilityNodeInfo list = AutoUtil.findNodeInfosById(root,"com.soft.apk008v:id/set_value_con");
        if(list!=null){
            System.out.println("--list-getChildCount->"+list.getChildCount());
        }
        if(list!=null&&list.getChildCount()>90){
            AutoUtil.sleep(3000);

            List<String> dataStrs;
            if("010".equals(currentWx008Data.getDataFlag())){
                dataStrs = currentWx008Data.getSl008To008Datas(currentWx008Data.getPhoneStrsAw());
            }else {
                dataStrs =  JSON.parseArray(currentWx008Data.getDatas(),String.class);
            }
            for(int i=1;i<91;i++){
                if(list.getChild(i).isEditable()){
                    String data;
                    if(dataStrs.get(1).contains("历史记录")){//红米2s提取的008数据
                        data  = dataStrs.get(i+1);
                    }else {
                        data  = dataStrs.get(i);
                    }
                    System.out.println("-rr->"+i+" "+data);
                    AutoUtil.performSetText(list.getChild(i),data,record,"008写入"+i+" "+data);
                }
            }
            AutoUtil.recordAndLog(record,"008写入数据完成");
            if(AutoUtil.checkAction(record,"008写入数据完成")){
                AccessibilityNodeInfo save =AutoUtil.findNodeInfosByText(root,"保存");
                AutoUtil.performClick(save,record,"008保存数据",3500);
                //AutoUtil.recordAndLog(record,"wx启动微信");
                return true;
            }
        }
        return false;
    }

}
