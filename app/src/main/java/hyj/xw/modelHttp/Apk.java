package hyj.xw.modelHttp;


import java.io.Serializable;
import java.util.Date;

/**
 * t_apk
 * @author
 */
public class Apk implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * APK名称
     */
    private String name;

    /**
     * apk版本号
     */
    private String versionName;

    /**
     * apk版本号
     */
    private Long versionCode;

    /**
     * apk类型：1、APK，2、脚本
     */
    private Integer apkType;

    /**
     * apk保存路径
     */
    private String path;

    /**
     * 上传时间
     */
    private Date createTime;

    private boolean isEndInstallApk;

    public boolean isEndInstallApk() {
        return isEndInstallApk;
    }

    public void setEndInstallApk(boolean endInstallApk) {
        isEndInstallApk = endInstallApk;
    }

    /**
     * 上传人id
     */
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Long versionCode) {
        this.versionCode = versionCode;
    }

    public Integer getApkType() {
        return apkType;
    }

    public void setApkType(Integer apkType) {
        this.apkType = apkType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
