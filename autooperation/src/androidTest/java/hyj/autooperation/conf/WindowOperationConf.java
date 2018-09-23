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


    public static Map<String,WindowNodeInfo> getOperations1(){
        Map<String,WindowNodeInfo> ops = new HashMap<String,WindowNodeInfo>();

        WindowNodeInfo windowNodeInfo1 = new WindowNodeInfo("6.6.7","注册","注册|语言","点击注册1");
        NodeInfo nodeInfo11 = new NodeInfo(1,"注册","","点击注册1");
        setOps(ops,windowNodeInfo1,nodeInfo11);

        WindowNodeInfo windowNodeInfo2 = new WindowNodeInfo("6.6.7","注册","点击上面的“注册”","自定义-点击注册2");
        setOps(ops,windowNodeInfo2,null);

        WindowNodeInfo windowNodeInfo3 = new WindowNodeInfo("6.6.7","注册","我已阅读并同意上述条款","自定义-点击同意条款下一步");
        setOps(ops,windowNodeInfo3,null);

        WindowNodeInfo windowNodeInfo31 = new WindowNodeInfo("6.6.7","注册","微信隐私保护指引|不同意","自定义-点击同意条款下一步6.6");
        setOps(ops,windowNodeInfo31,null);


        /*WindowNodeInfo windowNodeInfo3 = new WindowNodeInfo("6.6.7","注册","我已阅读并同意上述条款","点击同意条款下一步");
        NodeInfo nodeInfo31 = new NodeInfo(1,"","%我已阅读并同意上述条款","点击同意条款");
        NodeInfo nodeInfo32 = new NodeInfo(1,"","下一步","点击同意条款下一步");
        nodeInfo32.setNodeOperationSleepMs(2000);
        setOps(ops,windowNodeInfo3,nodeInfo31);
        setOps(ops,windowNodeInfo3,nodeInfo32);*/

        WindowNodeInfo windowNodeInfo4 = new WindowNodeInfo("6.6.7","注册","当前所在页面,安全校验","自定义-点击开始安全校验");
        //WindowNodeInfo windowNodeInfo4 = new WindowNodeInfo("6.6.7","注册","为了你的帐号安全，本次注册需要进行安全验证码校验","点击开始安全校验");
        //NodeInfo nodeInfo41 = new NodeInfo(1,"","开始","点击开始安全校验");
        setOps(ops,windowNodeInfo4,null);

        WindowNodeInfo windowNodeInfo5 = new WindowNodeInfo("6.6.7","注册","当前所在页面,微信安全","自定义-过滑块");
        //WindowNodeInfo windowNodeInfo5 = new WindowNodeInfo("6.6.7","注册","拖动下方滑块完成拼图","自定义-过滑块");
        setOps(ops,windowNodeInfo5,null);

        WindowNodeInfo windowNodeInfo6 = new WindowNodeInfo("6.6.7","注册","联系符合以下条件的微信用户","自定义-注册异常二维码出现");
        setOps(ops,windowNodeInfo6,null);

        WindowNodeInfo windowNodeInfo7 = new WindowNodeInfo("6.6.7","注册","已发送短信，下一步","自定义-发送短信");
        //WindowNodeInfo windowNodeInfo7 = new WindowNodeInfo("6.6.7","注册","发送短信后请回到本界面继续下一步","自定义-发送短信");
        setOps(ops,windowNodeInfo7,null);

        WindowNodeInfo windowNodeInfo8 = new WindowNodeInfo("6.6.7","注册","尚未收到你发送的短信验证码","自定义-尚未收到短信");
        setOps(ops,windowNodeInfo8,null);

        WindowNodeInfo windowNodeInfo9 = new WindowNodeInfo("6.6.7","注册","通讯录|发现","自定义-判断登录成功-结束");
        setOps(ops,windowNodeInfo9,null);

        WindowNodeInfo windowNodeInfo91 = new WindowNodeInfo("6.6.7","注册","本次登录已失效","自定义-登录异常");
        setOps(ops,windowNodeInfo91,null);

        WindowNodeInfo windowNodeInfo10 = new WindowNodeInfo("6.6.7","注册","该手机号已经绑定以上微信帐号","点击不是我的，继续注册");
        NodeInfo nodeInfo101 = new NodeInfo(1,"不是我的，继续注册","","点击不是我的，继续注册");
        setOps(ops,windowNodeInfo10,nodeInfo101);

        WindowNodeInfo windowNodeInfo100 = new WindowNodeInfo("6.6.7","注册","网络错误，请稍后再试","自定义-网络错误");
        setOps(ops,windowNodeInfo100,null);

        WindowNodeInfo windowNodeInfo64 = new WindowNodeInfo("6.6.7","注册","无响应。要将其关闭吗","自定义-无响应等待");
        setOps(ops,windowNodeInfo64,null);

        /*WindowNodeInfo windowNodeInfo9 = new WindowNodeInfo("6.6.7","注册","通讯录|发现","自定义-提取wxid-结束");
        setOps(ops,windowNodeInfo9,null);*/

        return ops;
    }
    public static Map<String,WindowNodeInfo> getOperations(){
        Map<String,WindowNodeInfo> ops = new HashMap<String,WindowNodeInfo>();

        WindowNodeInfo windowNodeInfo1 = new WindowNodeInfo("6.6.7","养号","注册|语言","点击登录1");
        NodeInfo nodeInfo11 = new NodeInfo(1,"登录","","点击登录1");
        setOps(ops,windowNodeInfo1,nodeInfo11);

        WindowNodeInfo windowNodeInfo01 = new WindowNodeInfo("6.6.7","养号","邀请好友辅助验证","点击邀请好友辅助验证");
        NodeInfo nodeInfo21 = new NodeInfo(1,"","邀请好友辅助验证%","点击邀请好友辅助验证");
        setOps(ops,windowNodeInfo01,nodeInfo21);

        WindowNodeInfo windowNodeInfo2 = new WindowNodeInfo("6.6.7","养号","请填写手机号|下一步","自定义-登录下一步");
        setOps(ops,windowNodeInfo2,null);

        WindowNodeInfo windowNodeInfo3 = new WindowNodeInfo("6.6.7","养号","用手机号登录|登录","自定义-输入账号密码");
        setOps(ops,windowNodeInfo3,null);

        WindowNodeInfo windowNodeInfo4 = new WindowNodeInfo("6.6.7","养号","帐号或密码错误","自定义-登录异常");
        setOps(ops,windowNodeInfo4,null);

        WindowNodeInfo windowNodeInfo41 = new WindowNodeInfo("6.6.7","养号","帐号的使用存在异常","自定义-登录异常");
        setOps(ops,windowNodeInfo41,null);

        WindowNodeInfo windowNodeInfo42 = new WindowNodeInfo("6.6.7","养号","你正在一台新设备登录微信","自定义-登录异常");
        setOps(ops,windowNodeInfo42,null);

        WindowNodeInfo windowNodeInfo43 = new WindowNodeInfo("6.6.7","养号","操作频率过快","自定义-登录异常");
        setOps(ops,windowNodeInfo43,null);

        WindowNodeInfo windowNodeInfo44 = new WindowNodeInfo("6.6.7","养号","该微信帐号因批量","自定义-登录异常");
        setOps(ops,windowNodeInfo44,null);

        WindowNodeInfo windowNodeInfo45 = new WindowNodeInfo("6.6.7","养号","该微信帐号因使用了微信外挂","自定义-登录异常");
        setOps(ops,windowNodeInfo45,null);

        WindowNodeInfo windowNodeInfo46 = new WindowNodeInfo("6.6.7","养号","本次登录已失效","自定义-登录异常");
        setOps(ops,windowNodeInfo46,null);

        WindowNodeInfo windowNodeInfo47 = new WindowNodeInfo("6.6.7","养号","系统检测到你的帐号有异常","自定义-登录异常");
        setOps(ops,windowNodeInfo47,null);

        WindowNodeInfo windowNodeInfo48 = new WindowNodeInfo("6.6.7","养号","你的微信号由于长期没有使用","自定义-登录异常");
        setOps(ops,windowNodeInfo48,null);

        WindowNodeInfo windowNodeInfo49 = new WindowNodeInfo("6.6.7","养号","该帐号长期未登录","自定义-登录异常");
        setOps(ops,windowNodeInfo49,null);

        WindowNodeInfo windowNodeInfo50 = new WindowNodeInfo("6.6.7","养号","刷公众号","自定义-登录异常");
        setOps(ops,windowNodeInfo50,null);

        WindowNodeInfo windowNodeInfo5 = new WindowNodeInfo("6.6.7","养号","通讯录|发现","自定义-判断登录成功-结束");
        setOps(ops,windowNodeInfo5,null);

        WindowNodeInfo windowNodeInfo60 = new WindowNodeInfo("6.6.7","养号","看看手机通讯录","自定义-随机界面");
        setOps(ops,windowNodeInfo60,null);

        WindowNodeInfo windowNodeInfo61 = new WindowNodeInfo("6.6.7","养号","如果对方登录成功，本设备将会被强制退出登录","自定义-随机界面");
        setOps(ops,windowNodeInfo61,null);

        WindowNodeInfo windowNodeInfo62 = new WindowNodeInfo("6.6.7","养号","微信团队邀请你参与内部体验","自定义-随机界面");
        setOps(ops,windowNodeInfo62,null);

        WindowNodeInfo windowNodeInfo71 = new WindowNodeInfo("6.6.7","养号","应急联系人","自定义-随机界面");
        setOps(ops,windowNodeInfo71,null);

        WindowNodeInfo windowNodeInfo64 = new WindowNodeInfo("6.6.7","养号","无响应。要将其关闭吗","自定义-无响应等待");
        setOps(ops,windowNodeInfo64,null);

        WindowNodeInfo windowNodeInfo63 = new WindowNodeInfo("6.6.7","养号","当前所在页面,微信安全","自定义-过滑块");
        setOps(ops,windowNodeInfo63,null);

        WindowNodeInfo windowNodeInfo65 = new WindowNodeInfo("6.6.7","养号","让好友发送","自定义-提取好友验证数字并发送广播");
        setOps(ops,windowNodeInfo65,null);


        /*WindowNodeInfo windowNodeInfo6 = new WindowNodeInfo("6.6.7","发圈","通讯录|发现","点击发现");
        NodeInfo nodeInfo61 = new NodeInfo(1,"发现","","点击发现");
        setOps(ops,windowNodeInfo6,nodeInfo61);

        WindowNodeInfo windowNodeInfo7 = new WindowNodeInfo("6.6.7","发圈","附近的人|朋友圈","点击朋友圈");
        NodeInfo nodeInfo71 = new NodeInfo(1,"朋友圈","","点击朋友圈");
        setOps(ops,windowNodeInfo7,nodeInfo71);*/

        WindowNodeInfo windowNodeInfo70 = new WindowNodeInfo("6.6.7","发圈","通讯录|发现","自定义-进入朋友圈");
        setOps(ops,windowNodeInfo70,null);

        WindowNodeInfo windowNodeInfo8 = new WindowNodeInfo("6.6.7","发圈","轻触更换主题照片","自定义-长按拍照分享");
        setOps(ops,windowNodeInfo8,null);

        WindowNodeInfo windowNodeInfo9 = new WindowNodeInfo("6.6.7","发圈","长按拍照按钮发文字，为内部体验功能","自定义-点我知道了");
        setOps(ops,windowNodeInfo9,null);

        WindowNodeInfo windowNodeInfo10 = new WindowNodeInfo("6.6.7","发圈","这一刻的想法...","自定义-输入发圈内容-结束");
        setOps(ops,windowNodeInfo10,null);

        WindowNodeInfo windowNodeInfo101 = new WindowNodeInfo("6.6.7","发圈","照片或视频","自定义-随机界面");
        setOps(ops,windowNodeInfo101,null);

        WindowNodeInfo windowNodeInfo11 = new WindowNodeInfo("6.6.7","提取wxid","通讯录|发现","自定义-提取wxid-结束");
        setOps(ops,windowNodeInfo11,null);

        WindowNodeInfo windowNodeInfo12 = new WindowNodeInfo("6.6.7","加好友","通讯录|发现","自定义-am启动加好友");
        setOps(ops,windowNodeInfo12,null);
        WindowNodeInfo windowNodeInfo13 = new WindowNodeInfo("6.6.7","加好友","我的微信号","自定义-点击加好友输入框");
        setOps(ops,windowNodeInfo13,null);
        WindowNodeInfo windowNodeInfo14 = new WindowNodeInfo("6.6.7","加好友","微信号/QQ号/手机号","自定义-输入微信号");
        setOps(ops,windowNodeInfo14,null);

        WindowNodeInfo windowNodeInfo15 = new WindowNodeInfo("6.6.7","加好友","添加到通讯录","点击添加到通讯录");
        NodeInfo nodeInfo151 = new NodeInfo(1,"添加到通讯录","","点击添加到通讯录");
        setOps(ops,windowNodeInfo15,nodeInfo151);

        WindowNodeInfo windowNodeInfo16 = new WindowNodeInfo("6.6.7","加好友","验证申请","自定义-点击发送-结束");//对方没有免验证情况
        setOps(ops,windowNodeInfo16,null);

        WindowNodeInfo windowNodeInfo17 = new WindowNodeInfo("6.6.7","加好友","发消息","点击发消息");
        NodeInfo nodeInfo171 = new NodeInfo(1,"发消息","","点击发消息");
        setOps(ops,windowNodeInfo17,nodeInfo171);

        WindowNodeInfo windowNodeInfo18 = new WindowNodeInfo("6.6.7","加好友","更多功能按钮，已折叠","自定义-输入发送内容-结束");
        setOps(ops,windowNodeInfo18,null);

        return ops;
    }



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
