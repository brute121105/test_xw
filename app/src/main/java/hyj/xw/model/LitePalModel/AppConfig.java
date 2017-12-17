package hyj.xw.model.LitePalModel;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by asus on 2017/12/17.
 */

public class AppConfig extends DataSupport {
    private String configCode;
    private String configContent;
    private Date createTime;
    private Date modifyTime;
    private String remark;


    public AppConfig() {
    }

    public AppConfig(String configCode, String configContent) {
        this.configCode = configCode;
        this.configContent = configContent;
    }

    public String getConfigCode() {
        return configCode;
    }

    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    public String getConfigContent() {
        return configContent;
    }

    public void setConfigContent(String configContent) {
        this.configContent = configContent;
    }


    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
