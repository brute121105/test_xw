package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSON;

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
import hyj.xw.util.FileUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.NodeActionUtil;
import hyj.xw.util.ParseRootUtil;
import hyj.xw.util.WindowOperationUtil;

/**
 * Created by Administrator on 2018/06/10.
 */

public class AutoOperatonThread extends BaseThread {
    public  final String TAG = this.getClass().getSimpleName();
    private int countRootNull =0;
    private int actionNo=0;
    private Map<Integer,List<WindowNodeInfo>> wInfoMap;
    private int loginIndex,endLoginIndex;//登录序号
    private String isLoginSucessPause;//登录成功是否暂停
    private List<Wx008Data> wx008Datas;
    private Wx008Data currentWx008Data;
    private String extValue,isAirChangeIp,isLoginByPhone;
    public AutoOperatonThread(AccessibilityService context, Map<String, String> record, AccessibilityParameters parameters){
        super(context,record,parameters);
        intiParam();
    }
    private void intiParam(){
        wInfoMap = WindowNodeInfoConf.getWinfoMap();
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
                AutoUtil.sleep(1500);
                LogUtil.d(TAG,Thread.currentThread().getName()+" actionNo:"+actionNo);
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

                ParseRootUtil.debugRoot(root);

                List<WindowNodeInfo> wInfos = wInfoMap.get(actionNo);//获取当前需要需要执行动作
                setNodeInputText(wInfos,currentWx008Data);//设置输入框文本
                System.out.println("winfo-->"+ JSON.toJSONString(wInfos));
                if(doActions(root,wInfos)){
                    if(wInfoMap.keySet().size()-1>actionNo){
                        ++actionNo;
                    }
                }else {//当前动作执行失败后处理

                }

            }catch (Exception e){
              LogUtil.logError(e);
            }
        }
    }

    public  boolean doActions(AccessibilityNodeInfo root,List<WindowNodeInfo> wInfos){
        boolean flag = false;
        if(wInfos!=null&&wInfos.size()>0){
            for(WindowNodeInfo info:wInfos){
                flag = doAction(root,info);
                System.out.println("doActions-->:"+info.getActionDesc()+flag);
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
            clearEnviroment();//清除并准备改机环境
            flag = true;
        }else if(CommonConstant.APPCONFIG_VLS.equals(info.getActionDesc())){
            if(validLoginSucc(root)){//判断登录成功
                flag = true;
            }
        }else if(CommonConstant.APPCONFIG_VEVN.equals(info.getActionDesc())){
            if(validEnviroment()){//判断改机成功
                flag = true;
            }
        }
        else if(1==info.getNodeType()){//按钮控件
            flag = WindowOperationUtil.performClick(WindowOperationUtil.getNodeByInfo(root,info),info);
        }else if(2==info.getNodeType()){//输入框控件
            flag = WindowOperationUtil.performSetText(WindowOperationUtil.getNodeByInfo(root,info),info);
        }
        return flag;
    }

    private boolean validLoginSucc(AccessibilityNodeInfo root){
        boolean flag = false;
        if(NodeActionUtil.isContainsStrs(root,"通讯录|发现|我")){
            int cn = DaoUtil.updateExpMsg(currentWx008Data,"登录成功-"+AutoUtil.getCurrentDate());
            System.out.println("excpMsg-->"+cn);
            LogUtil.login(loginIndex+" success",currentWx008Data.getPhone()+" "+currentWx008Data.getWxId()+" "+currentWx008Data.getWxPwd()+" ip:"+record.remove("ipMsg"));
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

        NewPhoneInfo pi = null;
        if(!TextUtils.isEmpty(currentWx008Data.getPhoneStrsAw())){//aw数据
            pi = JSON.parseObject(currentWx008Data.getPhoneStrsAw(),NewPhoneInfo.class);
        }else {
            pi = PhoneConf.xw2awData(currentWx008Data);
        }
        pi.setCpuName(pi.getCpuName().trim().toLowerCase());
        CreatePhoneEnviroment.create(GlobalApplication.getContext(),pi);
        FileUtil.writeContent2FileForceUtf8(FilePathCommon.baseAppPathAW,FilePathCommon.npiFileName, JSON.toJSONString(pi));

        //飞行模式
        if("1".equals(isAirChangeIp)){
            AutoUtil.showToastByRunnable(GlobalApplication.getContext(),"开启飞行模式");
            AutoUtil.setAriplaneMode(1000);
        }
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
                    info.setInputText(currentWx008Data.getWxPwd());
                }
            }
        }
    }

}
