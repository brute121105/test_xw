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

public class AutoFeedThread extends BaseThread {
    private  int countRootNull =0;
    public  final String TAG = this.getClass().getSimpleName();
    public AutoFeedThread(AccessibilityService context, Map<String, String> record, AccessibilityParameters parameters){
        super(context,record,parameters);
        intiParam();
    }
    List<Wx008Data> wx008Datas;
    Wx008Data currentWx008Data;
    int loginIndex;
    private void intiParam(){
        AutoUtil.recordAndLog(record,"init");
        wx008Datas = DaoUtil.getWx008Datas();
        loginIndex = Integer.parseInt(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX));
    }
    @Override
    public Object call() {
        while (true){
            try {
            AutoUtil.sleep(500);
            if(AutoUtil.checkAction(record,"wx登陆完成"))  return null;
            LogUtil.d(TAG,Thread.currentThread().getName()+" "+record+" loginIndex:"+loginIndex);
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
            //不在wx界面，启动wx
            /*if(root.getPackageName().toString().indexOf("tencent")==-1){
                LogUtil.d(TAG,"not in the weixin view getPackageName2:"+root.getPackageName());
                AutoUtil.startWx();
            }*/
            NodeActionUtil.doClickByNodePathAndText(root,"微信无响应。要将其关闭吗？|确定","01","等待",record,"exception",500);

            ParseRootUtil.debugRoot(root);

                if(AutoUtil.checkAction(record,"init")||AutoUtil.checkAction(record,"wx登陆成功")){
                    AutoUtil.clearAppData();
                    LogUtil.d(TAG,"清除app数据");
                    AutoUtil.recordAndLog(record,"wx清除app数据");

                    currentWx008Data = wx008Datas.get(loginIndex);
                    currentWx008Data.setPhoneInfo(currentWx008Data.getDatas());

                    /*Wx008Data w = wx008Datas.get(loginIndex);
                    w.setPhoneInfo(w.getDatas());*/

                    //覆盖式写入文件
                    FileUtil.writeContent2FileForce("/sdcard/A_hyj_json/","phone.txt", JSON.toJSONString(currentWx008Data.getPhoneInfo()));
                    //读取文件
                    String con = FileUtil.readAll("/sdcard/A_hyj_json/phone.txt");
                    System.out.println("phoneInfo---->"+con);
                    //飞行模式
                    new SetAirPlaneModeThread(4000).start();
                    AutoUtil.sleep(5000);
                    AutoUtil.startWx();
                    AutoUtil.recordAndLog(record,"飞行模式&清除数据后启动微信");
                }
                autoLoginConfig(root,record);
                exceptionConfig(root,record);


            }catch (Exception e){
              LogUtil.logError(e);
            }

        }
    }

    //自动登录配置
    public boolean autoLoginConfig(AccessibilityNodeInfo root,Map<String,String> record){
        NodeActionUtil.doClickByNodePathAndText(root,"注册|语言","00","登录",record,"wx点击登陆1",500);

        boolean flag = false;
        String wxid = currentWx008Data.getWxId(),pwd = currentWx008Data.getWxPwd();
        //微信号为空用手机号登陆
        if(TextUtils.isEmpty(wxid)){
            String cnNum = currentWx008Data.getCnNum();
            if(cnNum!=null&&!"86".equals(cnNum)){
                selsectCn(root,cnNum);
            }
            if(cnNum==null||AutoUtil.checkAction(record,"wx选择国家")||AutoUtil.checkAction(record,"wx输入手机号")){
                String phone = currentWx008Data.getPhone();
                if("255".equals(cnNum)&&"255".equals(phone.substring(0,3))){
                    phone = phone.substring(3);
                }
                NodeActionUtil.doInputByNodePathAndText(root,"国家/地区|下一步|请填写手机号","00321",phone,record,"wx输入手机号",500);
                NodeActionUtil.doClickByNodePathAndText(root,"国家/地区|下一步|请填写手机号","0034","下一步",record,"wx下一步",1000);

            }
            NodeActionUtil.doInputByNodePathAndText(root,"手机号登录|用短信验证码登录","00331",pwd,record,"wx输入密码",500);
            NodeActionUtil.doClickByNodePathAndText(root,"手机号登录|用短信验证码登录","0035","登录",record,"wx点击登录",3000);
        }else {
            NodeActionUtil.doClickByNodePathAndText(root,"请填写手机号|手机号登录","0033","用微信号/QQ号/邮箱登录",record,"wx点击微信号/QQ号/邮箱登录");
            /**
             * 6.5.16版本
             */
            NodeActionUtil.doInputByNodePathAndText(root,"请填写微信号/QQ号/邮箱|微信号/QQ/邮箱登录","00311",wxid,record,"wx输入微信号",500);
            NodeActionUtil.doInputByNodePathAndText(root,wxid+"|微信号/QQ/邮箱登录","00321",pwd,record,"wx输入密码",1000);
            NodeActionUtil.doClickByNodePathAndText(root,wxid+"|微信号/QQ/邮箱登录","0034","登录",record,"wx点击登录",3000);
            /**
             * 6.6.1版本
             */
            NodeActionUtil.doInputByNodePathAndText(root,"请填写微信号/QQ号/邮箱|微信号/QQ/邮箱登录","00211",wxid,record,"wx输入微信号",500);
            NodeActionUtil.doInputByNodePathAndText(root,wxid+"|微信号/QQ/邮箱登录","00221",pwd,record,"wx输入密码",1000);
            NodeActionUtil.doClickByNodePathAndText(root,wxid+"|微信号/QQ/邮箱登录","0024","登录",record,"wx点击登录",3000);

        }
        NodeActionUtil.doClickByNodePathAndText(root,"提示|看看手机通讯录里谁在使用微信","02","否",record,"wx否通讯录",500);
        /*if(!AutoUtil.checkAction(record,"wx点击登陆1")){

        }*/
        //判断登陆成功
        if(NodeActionUtil.isContainsStrs(root,"通讯录|发现|我")&&!AutoUtil.checkAction(record,"飞行模式&清除数据后启动微信")){
            AutoUtil.recordAndLog(record,"wx登陆成功");
            LogUtil.login(loginIndex+" success",currentWx008Data.getPhone()+" "+currentWx008Data.getWxId()+" "+currentWx008Data.getWxPwd());
            AutoUtil.sleep(3000);
            if(loginIndex==wx008Datas.size()-1){
                AutoUtil.recordAndLog(record,"wx登陆完成");
                return flag;
            }
            loginIndex = loginIndex+1;
            AppConfigDao.saveOrUpdate(CommonConstant.APPCONFIG_START_LOGIN_INDEX,loginIndex+"");
        }
        return flag;
    }

    //异常情况处理
    public void exceptionConfig(AccessibilityNodeInfo root,Map<String,String> record){
        NodeActionUtil.doClickByNodePathAndText(root,"SIM卡工具包|尊敬的用户","03","确定",record,"exception确定尊敬的用户",500);
        NodeActionUtil.doClickByNodePathAndText(root,"有人正通过微信密码在|修改密码","01","忽略",record,"wx忽略设备登录你的微信",500);
        NodeActionUtil.doClickByNodePathAndText(root,"玩一个小游戏才是正经事|开始游戏","0020","进入微信",record,"wx进入微信",500);
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

}
