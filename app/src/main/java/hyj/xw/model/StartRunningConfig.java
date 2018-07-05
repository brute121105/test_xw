package hyj.xw.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import hyj.xw.common.FilePathCommon;
import hyj.xw.util.FileUtil;

/**
 * Created by Administrator on 2018/7/4 0004.
 */

public class StartRunningConfig {
    private String zcOryh;//注册或养号
    private int connNetType;//连接网络类型 1 vpn 2 流量 3 不切换网络
    private List<String> otherOperationNames;//登录成功后执行什么动作，注册 养号
    private String loginResult;

    public String getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(String loginResult) {
        this.loginResult = loginResult;
    }

    public String getZcOryh() {
        return zcOryh;
    }
    public void setZcOryh(String zcOryh) {
        this.zcOryh = zcOryh;
    }

    public int getConnNetType() {
        return connNetType;
    }

    public void setConnNetType(int connNetType) {
        this.connNetType = connNetType;
    }

    public List<String> getOtherOperationNames() {
        return otherOperationNames;
    }

    public void setOtherOperationNames(List<String> otherOperationNames) {
        this.otherOperationNames = otherOperationNames;
    }
}
