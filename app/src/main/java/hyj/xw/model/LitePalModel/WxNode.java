package hyj.xw.model.LitePalModel;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/12/14.
 */

public class WxNode extends DataSupport {
    private String nodeId;
    private String nodeCode;//节点编码，自定
    private List<String> nodePath;
    private String nodeText;
    private String nodeDesc;
    private String wxVersion;//版本
    private String remark;//备注
    private Date createTime;
    private Date updateTime;


    public WxNode(String nodeCode, String nodeText) {
        this.nodeCode = nodeCode;
        this.nodeText = nodeText;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public List<String> getNodePath() {
        return nodePath;
    }

    public void setNodePath(List<String> nodePath) {
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

    public String getWxVersion() {
        return wxVersion;
    }

    public void setWxVersion(String wxVersion) {
        this.wxVersion = wxVersion;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
