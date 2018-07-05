package hyj.autooperation.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hyj.autooperation.model.NodeInfo;
import hyj.autooperation.model.WindowNodeInfo;

/**
 * Created by Administrator on 2018/6/29 0029.
 */

public class WindowOperationConf {

    public static final String sendPhoneMsgUrl = "http://112.124.31.14:8060/SendSms?token=87BBCED60A9A4801AE8E270E030DCF93289E02C6BAACCD94&requestid=fujiantest";

    public static Map<String,WindowNodeInfo> getOperations1(){
        Map<String,WindowNodeInfo> ops = new HashMap<String,WindowNodeInfo>();

        WindowNodeInfo windowNodeInfo1 = new WindowNodeInfo("6.6.7","注册","注册|登录","点击注册1");
        NodeInfo nodeInfo11 = new NodeInfo(1,"注册","","点击注册1");
        setOps(ops,windowNodeInfo1,nodeInfo11);

        WindowNodeInfo windowNodeInfo2 = new WindowNodeInfo("6.6.7","注册","点击上面的“注册”","自定义-点击注册2");
        setOps(ops,windowNodeInfo2,null);

        WindowNodeInfo windowNodeInfo3 = new WindowNodeInfo("6.6.7","注册","我已阅读并同意上述条款","点击同意条款下一步");
        NodeInfo nodeInfo31 = new NodeInfo(1,"","%我已阅读并同意上述条款","点击同意条款");
        NodeInfo nodeInfo32 = new NodeInfo(1,"","下一步","点击同意条款下一步");
        nodeInfo32.setNodeOperationSleepMs(2000);
        setOps(ops,windowNodeInfo3,nodeInfo31);
        setOps(ops,windowNodeInfo3,nodeInfo32);

        WindowNodeInfo windowNodeInfo4 = new WindowNodeInfo("6.6.7","注册","为了你的帐号安全，本次注册需要进行安全验证码校验","点击开始安全校验");
        NodeInfo nodeInfo41 = new NodeInfo(1,"","开始","点击开始安全校验");
        setOps(ops,windowNodeInfo4,nodeInfo41);

        //WindowNodeInfo windowNodeInfo5 = new WindowNodeInfo("6.6.7","注册","拖动下方滑块完成拼图","自定义-过滑块");
        //setOps(ops,windowNodeInfo5,null);

        WindowNodeInfo windowNodeInfo6 = new WindowNodeInfo("6.6.7","注册","联系符合以下条件的微信用户","自定义-注册异常二维码出现");
        setOps(ops,windowNodeInfo6,null);

        WindowNodeInfo windowNodeInfo7 = new WindowNodeInfo("6.6.7","注册","发送短信后请回到本界面继续下一步","自定义-发送短信");
        setOps(ops,windowNodeInfo7,null);

        WindowNodeInfo windowNodeInfo8 = new WindowNodeInfo("6.6.7","注册","尚未收到你发送的短信验证码","自定义-尚未收到短信");
        setOps(ops,windowNodeInfo8,null);

        WindowNodeInfo windowNodeInfo9 = new WindowNodeInfo("6.6.7","注册","通讯录|发现","自定义-提取wxid-结束");
        setOps(ops,windowNodeInfo9,null);

        return ops;
    }
    public static Map<String,WindowNodeInfo> getOperations(){
        Map<String,WindowNodeInfo> ops = new HashMap<String,WindowNodeInfo>();

        WindowNodeInfo windowNodeInfo1 = new WindowNodeInfo("6.6.7","养号","注册|登录","点击登录1");
        NodeInfo nodeInfo11 = new NodeInfo(1,"登录","","点击登录1");
        setOps(ops,windowNodeInfo1,nodeInfo11);

        WindowNodeInfo windowNodeInfo2 = new WindowNodeInfo("6.6.7","养号","请填写手机号|下一步","自定义-登录下一步");
        setOps(ops,windowNodeInfo2,null);

        WindowNodeInfo windowNodeInfo3 = new WindowNodeInfo("6.6.7","养号","用手机号登录|登录","自定义-输入账号密码");
        setOps(ops,windowNodeInfo3,null);

        WindowNodeInfo windowNodeInfo4 = new WindowNodeInfo("6.6.7","养号","帐号或密码错误","自定义-登录异常");
        setOps(ops,windowNodeInfo4,null);

        WindowNodeInfo windowNodeInfo5 = new WindowNodeInfo("6.6.7","养号","通讯录|发现","自定义-判断登录成功-结束");
        setOps(ops,windowNodeInfo5,null);


        WindowNodeInfo windowNodeInfo6 = new WindowNodeInfo("6.6.7","发圈","通讯录|发现","点击发现");
        NodeInfo nodeInfo61 = new NodeInfo(1,"发现","","点击发现");
        setOps(ops,windowNodeInfo6,nodeInfo61);

        WindowNodeInfo windowNodeInfo7 = new WindowNodeInfo("6.6.7","发圈","附近的人|看一看","点击朋友圈");
        NodeInfo nodeInfo71 = new NodeInfo(1,"朋友圈","","点击朋友圈");
        setOps(ops,windowNodeInfo7,nodeInfo71);

        WindowNodeInfo windowNodeInfo8 = new WindowNodeInfo("6.6.7","发圈","轻触更换主题照片|拍照分享","自定义-长按拍照分享");
        setOps(ops,windowNodeInfo8,null);

        WindowNodeInfo windowNodeInfo9 = new WindowNodeInfo("6.6.7","发圈","长按拍照按钮发文字，为内部体验功能","自定义-点我知道了");
        setOps(ops,windowNodeInfo9,null);

        WindowNodeInfo windowNodeInfo10 = new WindowNodeInfo("6.6.7","发圈","这一刻的想法...","自定义-输入发圈内容-结束");
        setOps(ops,windowNodeInfo10,null);

        WindowNodeInfo windowNodeInfo11 = new WindowNodeInfo("6.6.7","提取wxid","通讯录|发现","自定义-提取wxid-结束");
        setOps(ops,windowNodeInfo11,null);

        return ops;
    }

    /*public static List<WindowNodeInfo> getAllOtherOperation(){
        List<WindowNodeInfo> ops = new ArrayList<WindowNodeInfo>();

        WindowNodeInfo windowNodeInfo0 = new WindowNodeInfo("6.6.7","发圈","通讯录|发现","点击发现");
        NodeInfo nodeInfo01 = new NodeInfo(1,"发现","","点击发现");
        windowNodeInfo0.getNodeInfoList().add(nodeInfo01);
        ops.add(windowNodeInfo0);

        WindowNodeInfo windowNodeInfo1 = new WindowNodeInfo("6.6.7","发圈","附近的人|看一看","点击朋友圈");
        NodeInfo nodeInfo11 = new NodeInfo(1,"朋友圈","","点击朋友圈");
        windowNodeInfo1.getNodeInfoList().add(nodeInfo11);
        ops.add(windowNodeInfo1);

        WindowNodeInfo windowNodeInfo3 = new WindowNodeInfo("6.6.7","发圈","轻触更换主题照片|拍照分享","自定义-长按拍照分享");
        //NodeInfo nodeInfo31 = new NodeInfo(6,"","拍照分享","长按拍照分享");
        //windowNodeInfo3.getNodeInfoList().add(nodeInfo31);
        ops.add(windowNodeInfo3);

        WindowNodeInfo windowNodeInfo2 = new WindowNodeInfo("6.6.7","发圈","长按拍照按钮发文字，为内部体验功能","自定义-点我知道了");
        ops.add(windowNodeInfo2);

        WindowNodeInfo windowNodeInfo4 = new WindowNodeInfo("6.6.7","发圈","这一刻的想法...","自定义-输入发圈内容-结束");
        ops.add(windowNodeInfo4);

        return ops;
    }*/

   /* public static List<WindowNodeInfo> getOtherOperationByAutoType(String autoType) {
        List<WindowNodeInfo> result = new ArrayList<WindowNodeInfo>();
        List<WindowNodeInfo> allOps = getAllOtherOperation();
        for(WindowNodeInfo windowNodeInfo:allOps){
            if(autoType.equals(windowNodeInfo.getAutoType())){
                result.add(windowNodeInfo);
            }
        }
        return result;
    }*/

    public static Map<String,WindowNodeInfo> getOperatioByAutoType(String autoType){
        if("注册".equals(autoType)) return getOperations1();
        Map<String,WindowNodeInfo> result = new HashMap<String,WindowNodeInfo>();
        Map<String,WindowNodeInfo> allOpes = getOperations();
        for(String key:allOpes.keySet()){
            if(autoType.equals(allOpes.get(key).getAutoType())){
                result.put(key,allOpes.get(key));
            }
        }
        return result;
    }

    public static void setOps(Map<String,WindowNodeInfo> ops,WindowNodeInfo windowNodeInfo,NodeInfo nodeInfo){
        if(nodeInfo!=null)  windowNodeInfo.getNodeInfoList().add(nodeInfo);
        ops.put(windowNodeInfo.getAutoType()+"-"+windowNodeInfo.getMathWindowText(),windowNodeInfo);
    }

}
