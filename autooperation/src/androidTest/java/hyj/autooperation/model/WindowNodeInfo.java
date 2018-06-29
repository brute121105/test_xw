package hyj.autooperation.model;


/**
 * Created by Administrator on 2018/6/10 0010.
 * 表格可增删改查，
 * 表格内容排序，排序字段：wxVersion微信版本、actionNo动作序号、actionChildNo
 * 表格查询条件：wxVersion微信版本号、operation操作
 * 表格数据克隆，可根据微信版本号克隆一份数据，克隆出来数据除wxVersion字段不一样，其他一样
 */
public class WindowNodeInfo {
    private String guid;
    private String wxVersion;//微信版本
    private String operation;//操作;
    private String windowText;//窗口文本
    private double actionNo;//动作序号
    private int actionChildNo;//动作子序号
    private String actionDesc;//点击行为动作描述
    private long actionSleepMs;//点击行为后休眠毫秒数
    private String actionGroupTag;//分组标签
    private int nodeType;//节点类型，1 按钮 2 输入框 3 异常窗口 4 开关按钮 5 文本，判断是否存在节点 6 长按按钮 7 获取指定路径节点文本，并存入inputText
    private String nodeId;//节点id
    private String nodePath;//节点路径
    private String nodeText;//节点文本
    private String nodeDesc;//节点描述
    private String remark;//备注
    private String findNodeResult;//节点查找结果
    private String inputText;//输入文本
    private boolean actionResultFlag;//执行结果标识
    private String actionResultMsg;//执行结果标识
    private int retryFlag;//再次出现界面  是否重复点击  0 重复点击，1 不重复点击

    public WindowNodeInfo() {
    }
    public WindowNodeInfo(String nodeId, String nodeText, String nodePath, String nodeDesc) {
        this.nodeId = nodeId;
        this.nodeText = nodeText;
        this.nodePath = nodePath;
        this.nodeDesc = nodeDesc;
    }

    public WindowNodeInfo(String operation, String actionGroupTag, String actionDesc) {
        this.operation = operation;
        this.actionGroupTag = actionGroupTag;
        this.actionDesc = actionDesc;
    }

    public WindowNodeInfo(String operation, String windowText, int nodeType, String actionGroupTag, String actionDesc, String nodeText, String nodePath) {
        this.windowText = windowText;
        this.operation = operation;
        this.actionGroupTag = actionGroupTag;
        this.actionDesc = actionDesc;
        this.nodeType = nodeType;
        this.nodePath = nodePath;
        this.nodeText = nodeText;
    }
    public WindowNodeInfo(String operation, String windowText, int nodeType, String actionGroupTag, String actionDesc, String nodeText, String nodePath, String nodeDesc) {
        this.windowText = windowText;
        this.operation = operation;
        this.actionGroupTag = actionGroupTag;
        this.actionDesc = actionDesc;
        this.nodeType = nodeType;
        this.nodePath = nodePath;
        this.nodeText = nodeText;
        this.nodeDesc = nodeDesc;
    }


    public String getActionGroupTag() {
        return actionGroupTag;
    }

    public void setActionGroupTag(String actionGroupTag) {
        this.actionGroupTag = actionGroupTag;
    }



    public boolean isActionResultFlag() {
        return actionResultFlag;
    }

    public void setActionResultFlag(boolean actionResultFlag) {
        this.actionResultFlag = actionResultFlag;
    }


    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getWxVersion() {
        return wxVersion;
    }

    public void setWxVersion(String wxVersion) {
        this.wxVersion = wxVersion;
    }

    public String getWindowText() {
        return windowText;
    }

    public void setWindowText(String windowText) {
        this.windowText = windowText;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public double getActionNo() {
        return actionNo;
    }

    public void setActionNo(double actionNo) {
        this.actionNo = actionNo;
    }

    public String getActionDesc() {
        return actionDesc;
    }

    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodePath() {
        return nodePath;
    }

    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    public String getNodeText() {
        return nodeText;
    }

    public void setNodeText(String nodeText) {
        this.nodeText = nodeText;
    }

    public String getNodeDesc() {
        return nodeDesc;
    }

    public void setNodeDesc(String nodeDesc) {
        this.nodeDesc = nodeDesc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getActionChildNo() {
        return actionChildNo;
    }

    public void setActionChildNo(int actionChildNo) {
        this.actionChildNo = actionChildNo;
    }

    public long getActionSleepMs() {
        return actionSleepMs;
    }

    public void setActionSleepMs(long actionSleepMs) {
        this.actionSleepMs = actionSleepMs;
    }

    public String getFindNodeResult() {
        return findNodeResult;
    }

    public void setFindNodeResult(String findNodeResult) {
        this.findNodeResult = findNodeResult;
    }

    public int getRetryFlag() {
        return retryFlag;
    }

    public void setRetryFlag(int retryFlag) {
        this.retryFlag = retryFlag;
    }

    public String getActionResultMsg() {
        return actionResultMsg;
    }

    public void setActionResultMsg(String actionResultMsg) {
        this.actionResultMsg = actionResultMsg;
    }

    public String getActionMsg(){
        return (actionNo+"-"+operation+"-"+actionDesc+"【"+nodeText+inputText+"】-").replaceAll("null","").replaceAll("【】","");
    }
}
