package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSON;

import java.io.File;
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
import hyj.xw.model.PhoneInfo;
import hyj.xw.model.WindowNodeInfo;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.DaoUtil;
import hyj.xw.util.FileUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.NodeActionUtil;
import hyj.xw.util.ParseRootUtil;
import hyj.xw.util.WindowOperationUtil;

import static android.R.attr.label;

/**
 * Created by Administrator on 2018/06/10.
 */

public class AutoOperatonThread extends BaseThread {
    public  final String TAG = this.getClass().getSimpleName();
    private int countRootNull =0;
    private int actionNo=0;
    private int loopNum =0;//标志循环到底部次数
    private Map<Integer,List<WindowNodeInfo>> wInfoMap,exceptionWInfoMap;
    private int loginIndex,endLoginIndex;//登录序号
    private String isLoginSucessPause;//登录成功是否暂停
    private List<Wx008Data> wx008Datas;
    private Wx008Data currentWx008Data;
    private String extValue,isAirChangeIp,isLoginByPhone;
    private boolean isStartAirPlaneMode = false;
    public AutoOperatonThread(AccessibilityService context, Map<String, String> record, AccessibilityParameters parameters){
        super(context,record,parameters);
        intiParam();
    }
    private void intiParam(){
        AutoUtil.recordAndLog(record,"init");
        wInfoMap = WindowNodeInfoConf.getWinfoMapByOperation("养号");
        exceptionWInfoMap = WindowNodeInfoConf.getWinfoMapByOperation("异常界面");
        wx008Datas = DaoUtil.getWx008Datas();
        loginIndex = Integer.parseInt(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX));
        isLoginSucessPause = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_LOGIN_PAUSE);
        extValue = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_EXT);
        isAirChangeIp = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_AIR_CHANGE_IP);
        isLoginByPhone = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_IS_LOGIN_BY_PHONE);
        currentWx008Data = wx008Datas.get(loginIndex);
    }
    @Override
    public Object call() {
        while (true){
            try {
                AutoUtil.sleep(500);
                LogUtil.d(TAG,Thread.currentThread().getName()+" actionNo:"+actionNo+" record:"+record);
                if(parameters.getIsStop()==1){
                    LogUtil.d(TAG,"暂停....");
                    continue;
                }
                //保持屏幕常亮
                AutoUtil.wake();

                AccessibilityNodeInfo root = context.getRootInActiveWindow();
                //roor超过5次为空，启动wx
                if(root==null){
                    continue;
                }
                if(NodeActionUtil.isContainsStrs(root,"数据...")||NodeActionUtil.isContainsStrs(root,"登录...")) continue;

                ParseRootUtil.debugRoot(root);
                System.out.println("getPackageName--->"+root.getPackageName());

                List<WindowNodeInfo> wInfos = wInfoMap.get(actionNo);//获取当前执行动作
                System.out.println("wInfos-->"+JSON.toJSONString(wInfos));
                //处理清除数据失败
                setNodeInputText(wInfos,currentWx008Data);//设置输入框文本
                if(doActions(root,wInfos)){
                    if(CommonConstant.APPCONFIG_APM.equals(wInfos.get(0).getActionDesc())&&!waitAriplaneModeSuc(root)) continue;//飞行模式没完成，继续
                    String msg = getExpMsg(wInfos);//捕获处理异常界面消息
                    if(wInfoMap.keySet().size()-1==actionNo||!msg.contains("success")){
                        doLoginFinish(msg);
                    }else {
                        ++actionNo;
                    }
                    loopNum = 0;
                    continue;
                }
                //如果改机不成功,重新登录
                if(!validExist008(wInfoMap)&&CommonConstant.APPCONFIG_VEVN.equals(wInfos.get(0).getActionDesc())){
                    actionNo = 0;
                    initWInfoFlag(wInfoMap);
                    continue;
                }
                //c处理随机异常界面
                if(doActions(root,exceptionWInfoMap.get(actionNo))){
                    continue;
                }
                //所有事件操作loopNum次数为false，重新轮询一遍(只针对点击、输入事件)
                ++loopNum;
                System.out.println("loopNum--->"+loopNum);
                if(wInfos.get(wInfos.size()-1).getNodeType()>0&&loopNum>2){//如果是点击界面&&loopNum>2
                    for(Integer key:wInfoMap.keySet()){
                        List<WindowNodeInfo> wInfos1 = wInfoMap.get(key);
                        if(wInfos1.get(0).getNodeType()>0&&doActions(root,wInfos1)){
                            String msg = getExpMsg(wInfos1);//捕获处理异常界面消息
                            System.out.println("key loopNum--->"+key+" msg:"+msg);
                            if(key==wInfoMap.keySet().size()-1||!"success".equals(msg)){
                                doLoginFinish(msg);
                            }else {
                                actionNo = key+1;
                            }
                            break;
                        }
                    }
                }

            }catch (Exception e){
              LogUtil.logError(e);
            }
        }
    }

    private void doLoginFinish(String msg){
        actionNo=0;
        initWInfoFlag(wInfoMap);
        doNextIndexAndRecord2DB();
        currentWx008Data = wx008Datas.get(loginIndex);
        int cn = DaoUtil.updateExpMsg(currentWx008Data,msg+"-"+AutoUtil.getCurrentDate());
        System.out.println("excpMsg-->"+cn);
        LogUtil.login(loginIndex+" msg:"+msg,currentWx008Data.getPhone()+" "+currentWx008Data.getWxId()+" "+currentWx008Data.getWxPwd()+" ip:"+record.remove("ipMsg"));
    }

    public  boolean doActions(AccessibilityNodeInfo root,List<WindowNodeInfo> wInfos){
        boolean flag = false;
        if(wInfos!=null&&wInfos.size()>0){
            if(wInfos.get(0).getNodeType()>0&&root.getPackageName().toString().indexOf("miui")>-1) return flag;//点击事件，监听到不符合应用包
            for(int i=0,l=wInfos.size();i<l;i++){
                flag = doAction(root,wInfos.get(i));
                System.out.println("doActions-->:"+wInfos.get(i).getActionMsg()+flag);
                if(flag){
                    if("登录异常".equals(wInfos.get(i).getActionDesc())
                            ||(i<wInfos.size()-1&&"登录异常".equals(wInfos.get(i+1).getActionDesc()))){
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
            if(validExist008(wInfoMap)){
                flag = true;
            }else if(validEnviroment()){//判断改机成功
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
        }
        info.setActionResultFlag(flag);//修改执行结果标识
        return flag;
    }

    //等待飞行模式开启成功
    private boolean waitAriplaneModeSuc(AccessibilityNodeInfo root){
        boolean flag = false;
        if(WindowOperationUtil.findNodeInfosByText(root,"SIM卡工具包")!=null){
             flag = WindowOperationUtil.performClickTest(WindowOperationUtil.findNodeInfosByText(root,"确定"));
        }
        return flag;
    }

    private boolean validLoginSucc(AccessibilityNodeInfo root){
        boolean flag = false;
        if(NodeActionUtil.isContainsStrs(root,"通讯录|发现|我")){
            //登录成功&开启登录成功暂停 修改暂停标识为1
            if("1".equals(isLoginSucessPause)){
                parameters.setIsStop(1);
            }
            flag = true;
        }
        return flag;
    }

    //判断改机是否成功
    private boolean validEnviroment(){
        String phoneTag = FileUtil.readAllUtf8(FilePathCommon.phoneTagPath);
        System.out.println("phoneTag-->"+phoneTag);
        if(!phoneTag.equals(TextUtils.isEmpty(currentWx008Data.getPhone())?currentWx008Data.getWxId():currentWx008Data.getPhone())){
            LogUtil.login(loginIndex+" exception change phone fail",currentWx008Data.getPhone()+" "+currentWx008Data.getWxId()+" "+currentWx008Data.getWxPwd()+" ip:"+record.remove("ipMsg"));
            return false;
        }else {
            return true;
        }
    }

    private void clearEnviroment(){
        AutoUtil.clearAppData();
        AutoUtil.sleep(2000);

        /*NewPhoneInfo pi = null;
        if(!TextUtils.isEmpty(currentWx008Data.getPhoneStrsAw())){//aw数据
            pi = JSON.parseObject(currentWx008Data.getPhoneStrsAw(),NewPhoneInfo.class);
        }else {
            pi = PhoneConf.xw2awData(currentWx008Data);
        }
        pi.setCpuName(pi.getCpuName().trim().toLowerCase());
        CreatePhoneEnviroment.create(GlobalApplication.getContext(),pi);
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.baseAppPathAW,FilePathCommon.npiFileName, JSON.toJSONString(pi));*/
        String hookPhoneDataStr="";
        currentWx008Data = wx008Datas.get(loginIndex);
        if(TextUtils.isEmpty(currentWx008Data.getPhoneStrs())){//旧008数据
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
        FileUtil.writeContent2FileForce("/sdcard/A_hyj_json/","phone.txt",hookPhoneDataStr);

    }

    private void setNodeInputText(List<WindowNodeInfo> wInfos,Wx008Data currentWx008Data){
        for(WindowNodeInfo info:wInfos){
            info.setCurrentWx008Data(currentWx008Data);
            if(info.getNodeType()==2){
                if("输入账号".equals(info.getActionDesc())){
                    String account = "";
                    if(!TextUtils.isEmpty(currentWx008Data.getWxId())){
                        account = currentWx008Data.getWxId();
                    }else if(!TextUtils.isEmpty(currentWx008Data.getWxid19())){
                        account = currentWx008Data.getWxid19();
                    }else if(!TextUtils.isEmpty(currentWx008Data.getPhone())){
                        account = currentWx008Data.getPhone();
                    }
                    info.setInputText(account);
                }else if("输入密码".equals(info.getActionDesc())){
                    info.setInputText(TextUtils.isEmpty(currentWx008Data.getWxPwd())?"NULLNULL":currentWx008Data.getWxPwd());
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
    private String getExpMsg(List<WindowNodeInfo> wInfos){
        String expMsg = "success";
        if(wInfos!=null&&wInfos.size()>0){
            for(WindowNodeInfo wInfo:wInfos){
                if("登录异常".equals(wInfo.getActionDesc())&&wInfo.isActionResultFlag()){
                    expMsg = wInfo.getNodeText();
                }
            }
        }
        return expMsg;
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
