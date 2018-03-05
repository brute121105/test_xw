package hyj.xw.thread;

import android.accessibilityservice.AccessibilityService;
import android.provider.Settings;
import android.view.WindowContentFrameStats;
import android.view.accessibility.AccessibilityNodeInfo;

import com.alibaba.fastjson.JSON;

import java.util.Date;
import java.util.List;
import java.util.Map;

import hyj.xw.BaseThread;
import hyj.xw.GlobalApplication;
import hyj.xw.api.GetPhoneAndValidCodeThread;
import hyj.xw.api.ZYGetPhoneAndValidCodeThread;
import hyj.xw.common.CommonConstant;
import hyj.xw.conf.PhoneConf;
import hyj.xw.dao.AppConfigDao;
import hyj.xw.model.AccessibilityParameters;
import hyj.xw.model.LitePalModel.AppConfig;
import hyj.xw.model.LitePalModel.Wx008Data;
import hyj.xw.model.PhoneApi;
import hyj.xw.model.PhoneInfo;
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
    String hookPhoneStr;
    PhoneApi pa = new PhoneApi();
    private void intiParam(){
        AutoUtil.recordAndLog(record,"init");
        /**
         * 测试模式，不连vpn，不开启接码线程，写死手机号和验证码
         */
        /*AutoUtil.recordAndLog(record,"test");
        pa.setPhone("12698587856");
        pa.setValidCodeIsAvailavle(true);
        pa.setValidCode("5684");*/

        wx008Datas = DaoUtil.getWx008Datas();
        loginIndex = Integer.parseInt(AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_START_LOGIN_INDEX));
        cnNum = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_CN_NUM);
        String apiType = AppConfigDao.findContentByCode(CommonConstant.APPCONFIG_API_TYPE);
        if(apiType.contains("4")&&!AutoUtil.checkAction(record,"test")){//605换绑手机，需接吗
            new Thread(new ZYGetPhoneAndValidCodeThread(pa)).start();//志远
        }
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

            if((AutoUtil.checkAction(record,"wx连接成功")&&pa.isPhoneIsAvailavle())||AutoUtil.checkAction(record,"test")){
                AutoUtil.clearAppData();
                LogUtil.d(TAG,"清除app数据");
                AutoUtil.recordAndLog(record,"wx清除app数据");

                //覆盖式写入文件
                PhoneInfo phoneInfo = PhoneConf.createPhoneInfo();
                phoneInfo.setLineNumber(pa.getPhone());//获取到的手机号码
                hookPhoneStr = JSON.toJSONString(phoneInfo);
                FileUtil.writeContent2FileForce("/sdcard/A_hyj_json/","phone.txt",hookPhoneStr);
                //读取文件
                String con = FileUtil.readAll("/sdcard/A_hyj_json/phone.txt");
                System.out.println("phoneInfo---->"+con);

                AutoUtil.startWx();
            }

            NodeActionUtil.doClickByNodePathAndText(root,"微信无响应。要将其关闭吗？|确定","01","等待",record,"exception",500);

            ParseRootUtil.debugRoot(root);
            //注册
            if(AutoUtil.actionContains(record,"wx")||AutoUtil.checkAction(record,"test")){
                autoRegConfig(root,record);
            }
            //设置vpn
            if((AutoUtil.actionContains(record,"st")||AutoUtil.checkAction(record,"init"))&&!AutoUtil.checkAction(record,"test")){
                doVPN(root);
            }


            }catch (Exception e){
              LogUtil.logError(e);
            }

        }
    }

    //自动登录配置
    public boolean autoRegConfig(AccessibilityNodeInfo root,Map<String,String> record) {
         NodeActionUtil.doClickByNodePathAndText(root, "注册|语言", "01", "注册", record, "wx点击注册1", 500);
         NodeActionUtil.doClickByNodePathAndText(root, "Sign Up|Language", "01", "Sign Up", record, "wx点击注册1", 500);
        if(cnNum!=null&&!"86".equals(cnNum)){
            selsectCn(root,cnNum);
        }
        if(AutoUtil.checkAction(record,"wx选择国家")){
           /* NodeActionUtil.doInputByNodePathAndText(root,"点击上面的“注册”按钮|国家/地区","00201","nname",record,"wx输入昵称",500);
            NodeActionUtil.doInputByNodePathAndText(root,"点击上面的“注册”按钮|国家/地区","00231",pa.getPhone(),record,"wx输入手机",500);
            NodeActionUtil.doInputByNodePathAndText(root,"点击上面的“注册”按钮|国家/地区","00241","wwww12345",record,"wx输入密码",500);
            NodeActionUtil.doClickByNodePathAndText(root, "点击上面的“注册”按钮|国家/地区", "0025", "注册", record, "wx点击注册2", 500);*/

            NodeActionUtil.doInputByNodePathAndText(root,"Continuing means you've read and agreed to our  Terms of Service  and  Privacy Policy|Sign Up","00201","nname",record,"wx输入昵称",500);
            NodeActionUtil.doInputByNodePathAndText(root,"Continuing means you've read and agreed to our  Terms of Service  and  Privacy Policy|Sign Up","00231",pa.getPhone(),record,"wx输入手机",500);
            NodeActionUtil.doInputByNodePathAndText(root,"Continuing means you've read and agreed to our  Terms of Service  and  Privacy Policy|Sign Up","00241",createPwd(pa.getPhone()),record,"wx输入密码",500);
            NodeActionUtil.doClickByNodePathAndText(root, "Continuing means you've read and agreed to our  Terms of Service  and  Privacy Policy|Sign Up", "0025", "Sign Up", record, "wx点击注册2", 500);
        }
        /*NodeActionUtil.doClickByNodePathAndText(root, "微信隐私保护指引|不同意", "04", "同意", record, "wx点击同意", 500);
        NodeActionUtil.doClickByNodePathAndText(root, "为了你的帐号安全|安全校验", "000003", "开始", record, "wx点击开始安全校验", 500);*/

        NodeActionUtil.doClickByNodePathAndText(root, "INTRODUCTION|Don't Agree", "04", "Accept ", record, "wx点击同意", 500);
        NodeActionUtil.doClickByNodePathAndDesc(root, "Security Verification|Security check", "000003", "Start", record, "wx点击开始安全校验", 500);
        if(pa.isValidCodeIsAvailavle()){
            createRegData();//创建保存数据
            if(currentWx008Data.save()){
                LogUtil.d(TAG,"保存数据-->"+JSON.toJSONString(currentWx008Data));
            }
            NodeActionUtil.doInputByNodePathAndText(root,"Verification code sent via SMS|Verification Code","00221",pa.getValidCode(),record,"wx输入验证码",500);
            NodeActionUtil.doClickByNodePathAndText(root,"Verification code sent via SMS|Verification Code", "0024", "Next",record,"wx已输入验证码下一步", 500);
        }
        return false;
    }

    boolean clickFlag = false;
    private void selsectCn(AccessibilityNodeInfo root,String cn_num){
        System.out.println("cn_num--->"+cn_num);
        if(!"86".equals(cn_num)){
            //点击进入国家列表
            //AccessibilityNodeInfo cn1 = AutoUtil.findNodeInfosByText(root,"国家/地区");
            AccessibilityNodeInfo cn1 = AutoUtil.findNodeInfosByText(root,"China（+86）");
            if(cn1!=null){
                AccessibilityNodeInfo cnNode1 = ParseRootUtil.getNodePath(root,"00221");
                if(cnNode1!=null&&"China（+86）".equals(cnNode1.getText()+"")&&!AutoUtil.checkAction(record,"wx点击国家地区")){
                    AutoUtil.performClick(cnNode1,record,"wx点击国家地区");
                }
                return;
            }
            //国家号码遍历查找
            if(AutoUtil.checkAction(record,"wx点击国家地区")||AutoUtil.checkAction(record,"wx下滚")){
                if("20062".equals(cn_num)&&!clickFlag){
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

    String vpnIndex="1";
    private void doVPN(AccessibilityNodeInfo root){
        if(AutoUtil.checkAction(record,"init")){
            AutoUtil.recordAndLog(record,"st设置VPN");
            AutoUtil.opentActivity(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
            AutoUtil.sleep(500);
        }
        AccessibilityNodeInfo linkText = AutoUtil.findNodeInfosById(context.getRootInActiveWindow(),"android:id/summary");
        if(linkText!=null&&(linkText.getText().toString().equals("正在连接...")||linkText.getText().toString().equals("Connecting…"))){
            System.out.println("hyj--->正在连接..");
            AutoUtil.sleep(1000);
            return;
        }
        if(AutoUtil.checkAction(record,"st点击连接")){
            AccessibilityNodeInfo link =AutoUtil.findNodeInfosByText(context.getRootInActiveWindow(),"已连接");
            AccessibilityNodeInfo link1 =AutoUtil.findNodeInfosByText(context.getRootInActiveWindow(),"Connected");
            if(link!=null||link1!=null){
                AutoUtil.recordAndLog(record,"wx连接成功");
                //AutoUtil.startAppByPackName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                return;
            }
            AccessibilityNodeInfo linkText5 = AutoUtil.findNodeInfosById(context.getRootInActiveWindow(),"android:id/summary");
            if(linkText5!=null){
                System.out.println("linkText5-->"+linkText5.getText());
            }
            if(linkText5!=null&&(linkText5.getText().toString().equals("PPTP VPN")||linkText5.getText().toString().equals("失败")||linkText5.getText().toString().equals("Unsuccessful"))){
                AutoUtil.clickXY(522,738);
                AutoUtil.recordAndLog(record,"st点击连接");
                AutoUtil.sleep(2000);
                return;
            }
        }

        clickTextXY1(514,425,"st点击VPN","miui:id/action_bar_title","无线和网络",800);
        clickTextXY1(514,425,"st点击VPN","miui:id/action_bar_title","Wireless & networks",800);

        if(AutoUtil.checkAction(record,"st点击VPN")||AutoUtil.checkAction(record,"st弹出")||AutoUtil.checkAction(record,"st设置VPN")){

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
                AutoUtil.sleep(1000);
                System.out.println("hyj--->断开连接等待");
                AutoUtil.sleep(9000);
            }
            if(AutoUtil.checkAction(record,"st断开")){
                AutoUtil.clickXY(522,738);
                AutoUtil.recordAndLog(record,"st点击连接");
                AutoUtil.sleep(2000);
            }
        }
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

    private String createPwd(String phone){
        return "www23"+phone.substring(phone.length()-3);
    }

    private void createRegData(){
        currentWx008Data = new Wx008Data();
        currentWx008Data.setGuid(AutoUtil.getUUID());
        currentWx008Data.setPhone(pa.getPhone());
        currentWx008Data.setWxPwd(createPwd(pa.getPhone()));
        currentWx008Data.setCnNum(cnNum);
        currentWx008Data.setCreateTime(new Date());
        currentWx008Data.setPhoneStrs(hookPhoneStr);
    }
}
