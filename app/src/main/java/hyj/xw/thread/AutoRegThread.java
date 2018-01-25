package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;

import hyj.xw.BaseThread;
import hyj.xw.common.CommonConstant;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.model.AccessibilityParameters;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.util.AutoUtil;
import hyj.xw.util.DaoUtil;
import hyj.xw.util.FileUtil;
import hyj.xw.util.LogUtil;
import hyj.xw.util.NodeActionUtil;
import hyj.xw.util.ParseRootUtil;

/**
 * Created by Administrator on 2017/12/14.
 */

public class AutoRegThread extends BaseThread {
    private  int countRootNull =0;
    public  final String TAG = this.getClass().getSimpleName();
    public AutoRegThread(AccessibilityService context, Map<String, String> record, AccessibilityParameters parameters){
        super(context,record,parameters);
        intiParam();
    }
    List<Wx008Data> wx008Datas;
    Wx008Data currentWx008Data;
    int loginIndex;
    String cnNum;
    private void intiParam(){
        AutoUtil.recordAndLog(record,"init");
        wx008Datas = DaoUtil.getWx008Datas();
        loginIndex = Integer.parseInt(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX));
        cnNum = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_CN_NUM);
    }
    @Override
    public Object call() {
        while (true){
            try {
            AutoUtil.sleep(500);
            LogUtil.d(TAG,Thread.currentThread().getName()+" "+record+" cnNum:"+cnNum);

            if(AutoUtil.checkAction(record,"wx登陆完成"))  return null;
            if(parameters.getIsStop()==1){
                LogUtil.d(TAG,"暂停....");
                continue;
            }
            //保持屏幕常亮
            AutoUtil.wake();

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

            NodeActionUtil.doClickByNodePathAndText(root,"微信无响应。要将其关闭吗？|确定","01","等待",record,"exception",500);

            ParseRootUtil.debugRoot(root);
            autoRegConfig(root,record);

            }catch (Exception e){
              LogUtil.logError(e);
            }

        }
    }

    //自动登录配置
    public boolean autoRegConfig(AccessibilityNodeInfo root,Map<String,String> record) {
        NodeActionUtil.doClickByNodePathAndText(root, "注册|语言", "00", "注册", record, "wx点击注册1", 500);
        if(cnNum!=null&&!"86".equals(cnNum)){
            selsectCn(root,cnNum);
        }
        if(AutoUtil.checkAction(record,"wx选择国家")){
            NodeActionUtil.doInputByNodePathAndText(root,"点击上面的“注册”按钮|国家/地区","00201","nname",record,"wx输入昵称",500);
            NodeActionUtil.doInputByNodePathAndText(root,"点击上面的“注册”按钮|国家/地区","00231","8970358754",record,"wx输入手机",500);
            NodeActionUtil.doInputByNodePathAndText(root,"点击上面的“注册”按钮|国家/地区","00241","wwww12345",record,"wx输入密码",500);
            NodeActionUtil.doClickByNodePathAndText(root, "点击上面的“注册”按钮|国家/地区", "0025", "注册", record, "wx点击注册2", 500);
        }
        NodeActionUtil.doClickByNodePathAndText(root, "微信隐私保护指引|不同意", "04", "同意", record, "wx点击同意", 500);
        NodeActionUtil.doClickByNodePathAndText(root, "为了你的帐号安全|安全校验", "000003", "开始", record, "wx点击开始安全校验", 500);
        return false;
    }

    boolean clickFlag = false;
    private void selsectCn(AccessibilityNodeInfo root,String cn_num){
        System.out.println("cn_num--->"+cn_num);
        if(!"86".equals(cn_num)){
            //点击进入国家列表
            AccessibilityNodeInfo cn1 = AutoUtil.findNodeInfosByText(root,"国家/地区");
            if(cn1!=null){
                AccessibilityNodeInfo cnNode1 = ParseRootUtil.getNodePath(root,"00221");
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

}
