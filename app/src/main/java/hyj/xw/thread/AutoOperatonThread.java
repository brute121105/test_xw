package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

import hyj.xw.BaseThread;
import hyj.xw.common.CommonConstant;
import hyj.xw.conf.WindowNodeInfoConf;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.model.AccessibilityParameters;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.model.WindowNodeInfo;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.DaoUtil;
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
                List<AccessibilityNodeInfo> list = root.findAccessibilityNodeInfosByText("登录");
                /*if(list.size()==1){
                    list.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }*/

                System.out.println(list.size()+" list-->"+list);
                List<WindowNodeInfo> wInfos = wInfoMap.get(actionNo);
                setNodeInputText(wInfos,currentWx008Data);//设置输入框文本
                System.out.println("winfo-->"+ JSON.toJSONString(wInfos));
                if(WindowOperationUtil.doActions(root,wInfos)){
                    if(wInfoMap.keySet().size()-1==actionNo){
                        actionNo=0;
                    }else {
                        ++actionNo;
                    }
                }

            }catch (Exception e){
              LogUtil.logError(e);
            }
        }
    }

    private void setNodeInputText(List<WindowNodeInfo> wInfos,Wx008Data currentWx008Data){
        for(WindowNodeInfo info:wInfos){
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
