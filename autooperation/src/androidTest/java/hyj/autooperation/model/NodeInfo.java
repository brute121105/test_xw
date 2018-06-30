package hyj.autooperation.model;

/**
 * Created by Administrator on 2018/6/29 0029.
 */

public class NodeInfo {
    private String nodeId;//节点id
    private int nodeType;//节点类型，1 按钮 2 输入框 3 异常窗口 4 开关按钮 5 文本，判断是否存在节点 6 长按按钮 7 获取指定路径节点文本，并存入inputText
    private String nodePath;//节点路径
    private String nodeText;//节点文本
    private String nodeDesc;//节点描述文本
    private String nodeOperation;//节点操作
    private long nodeOperationSleepMs;//节点操作
    private String nodefindResult;//节点查找结果
    private String nodeInputText;//输入文本
    private String nodeRemark;//节点备注
    private boolean isOperationSucc;//节点操作是否成功
    private String nodeOperationResultMsg;//操作节点结果消息

    public NodeInfo(){

    }

    public NodeInfo(int nodeType,String nodeText,String nodeDesc,String nodeOperation){
        this.nodeType = nodeType;
        this.nodeText = nodeText;
        this.nodeDesc = nodeDesc;
        this.nodeOperation = nodeOperation;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public long getNodeOperationSleepMs() {
        return nodeOperationSleepMs;
    }

    public void setNodeOperationSleepMs(long nodeOperationSleepMs) {
        this.nodeOperationSleepMs = nodeOperationSleepMs;
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

    public String getNodeOperation() {
        return nodeOperation;
    }

    public void setNodeOperation(String nodeOperation) {
        this.nodeOperation = nodeOperation;
    }

    public String getNodefindResult() {
        return nodefindResult;
    }

    public void setNodefindResult(String nodefindResult) {
        this.nodefindResult = nodefindResult;
    }

    public String getNodeInputText() {
        return nodeInputText;
    }

    public void setNodeInputText(String nodeInputText) {
        this.nodeInputText = nodeInputText;
    }

    public String getNodeRemark() {
        return nodeRemark;
    }

    public void setNodeRemark(String nodeRemark) {
        this.nodeRemark = nodeRemark;
    }

    public boolean isOperationSucc() {
        return isOperationSucc;
    }

    public void setOperationSucc(boolean operationSucc) {
        isOperationSucc = operationSucc;
    }

    public String getNodeOperationResultMsg() {
        return nodeOperationResultMsg;
    }

    public void setNodeOperationResultMsg(String nodeOperationResultMsg) {
        this.nodeOperationResultMsg = nodeOperationResultMsg;
    }
    public String toString(){
        String str = nodeOperation+"【"+nodeDesc+nodeText+nodeInputText+"】-"+isOperationSucc;
        return str;
    }
}
