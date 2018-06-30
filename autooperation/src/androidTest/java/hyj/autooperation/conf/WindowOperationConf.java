package hyj.autooperation.conf;

import java.util.HashMap;
import java.util.Map;

import hyj.autooperation.model.NodeInfo;
import hyj.autooperation.model.WindowNodeInfo;

/**
 * Created by Administrator on 2018/6/29 0029.
 */

public class WindowOperationConf {

    public static Map<String,WindowNodeInfo> getOperations(){
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

        WindowNodeInfo windowNodeInfo5 = new WindowNodeInfo("6.6.7","注册","拖动下方滑块完成拼图","自定义-过滑块");
        setOps(ops,windowNodeInfo5,null);

        WindowNodeInfo windowNodeInfo6 = new WindowNodeInfo("6.6.7","注册","联系符合以下条件的微信用户","异常-二维码出现");
        setOps(ops,windowNodeInfo5,null);


        return ops;
    }

    public static void setOps(Map<String,WindowNodeInfo> ops,WindowNodeInfo windowNodeInfo,NodeInfo nodeInfo){
        if(nodeInfo!=null)  windowNodeInfo.getNodeInfoList().add(nodeInfo);
        ops.put(windowNodeInfo.getMathWindowText(),windowNodeInfo);
    }

}
