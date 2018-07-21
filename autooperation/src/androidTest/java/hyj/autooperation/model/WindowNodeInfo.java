package hyj.autooperation.model;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/10 0010.
 * 表格可增删改查，
 * 表格内容排序，排序字段：wxVersion微信版本、actionNo动作序号、actionChildNo
 * 表格查询条件：wxVersion微信版本号、operation操作
 * 表格数据克隆，可根据微信版本号克隆一份数据，克隆出来数据除wxVersion字段不一样，其他一样
 */
public class WindowNodeInfo {
    private String wxVersion;//微信版本
    private String autoType;//自动操作类型，注册、养号;
    private String mathWindowText;//匹配窗口文本，多个用| 隔开
    private String operation;//最后点击行为动作描述
    private long operationSleepMs;//点击行为后休眠毫秒数
    private String windowOperationDesc;//窗口操作描述
    private boolean isWindowOperatonSucc;//窗口动作是否成功
    private String windowOperatonResultMsg;//执行结果标识
    private String remark;//节点备注

    private List<NodeInfo> nodeInfoList = new ArrayList<NodeInfo>();//当前窗口需要操作节点

    public WindowNodeInfo() {
    }
    public WindowNodeInfo(String wxVersion,String autoType,String mathWindowText,String operation){
        this.wxVersion = wxVersion;
        this.autoType = autoType;
        this.mathWindowText = mathWindowText;
        this.operation = operation;
    }

    public String getWindowOperationDesc() {
        return windowOperationDesc==null?"":windowOperationDesc;
    }

    public void setWindowOperationDesc(String windowOperationDesc) {
        this.windowOperationDesc = windowOperationDesc;
    }

    public String getWxVersion() {
        return wxVersion;
    }

    public void setWxVersion(String wxVersion) {
        this.wxVersion = wxVersion;
    }

    public String getAutoType() {
        return autoType;
    }

    public void setAutoType(String autoType) {
        this.autoType = autoType;
    }

    public String getMathWindowText() {
        return mathWindowText;
    }

    public void setMathWindowText(String mathWindowText) {
        this.mathWindowText = mathWindowText;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public long getOperationSleepMs() {
        return operationSleepMs;
    }

    public void setOperationSleepMs(long operationSleepMs) {
        this.operationSleepMs = operationSleepMs;
    }

    public boolean isWindowOperatonSucc() {
        return isWindowOperatonSucc;
    }

    public void setWindowOperatonSucc(boolean windowOperatonSucc) {
        isWindowOperatonSucc = windowOperatonSucc;
    }

    public String getWindowOperatonResultMsg() {
        return windowOperatonResultMsg==null?"":windowOperatonResultMsg;
    }

    public void setWindowOperatonResultMsg(String windowOperatonResultMsg) {
        this.windowOperatonResultMsg = windowOperatonResultMsg;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<NodeInfo> getNodeInfoList() {
        return nodeInfoList;
    }

    public void setNodeInfoList(List<NodeInfo> nodeInfoList) {
        this.nodeInfoList = nodeInfoList;
    }

    public String toString(){
        String nodesResult = autoType+"-"+windowOperationDesc;
        boolean allNodeOpeSuccFlag = true;
        for(NodeInfo nodeInfo:nodeInfoList){
            allNodeOpeSuccFlag = allNodeOpeSuccFlag & nodeInfo.isOperationSucc();//只要有一个是false,最后的结果返回false
            this.isWindowOperatonSucc = allNodeOpeSuccFlag;
            nodesResult = "-"+nodesResult+nodeInfo.toString();
        }
        return nodesResult.replaceAll("null","");
    }

}
