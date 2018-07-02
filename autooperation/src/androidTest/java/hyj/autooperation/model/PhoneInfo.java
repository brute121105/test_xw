package hyj.autooperation.model;

/**
 * Created by Administrator on 2018/1/4.
 */

public class PhoneInfo {
    private String deviceId;//序列号
    private String androidId;//android_id
    private String lineNumber;//手机号码
    private String simSerialNumber;//手机卡序列号
    private String subscriberId;//IMSI
    private String simCountryIso;//手机卡国家

    private String simOperator;//运营商
    private String simOperatorName;//运营商名字
    private String networkCountryIso;//国家iso代码
    private String networkOperator;//网络运营商类型
    private String networkOperatorName;//网络类型名

    private int networkType;//网络类型
    private int phoneType;//手机类型
    private int simState;//手机卡状态

    private String macAddress;
    private String routeName;
    private String routeAddress;

    private String release;//系统版本 6.0.1
    private String sdk;//系统版本值
    private String brand;//品牌
    private String model;//型号
    private String buildId;//ID
    private String display;//display

    private String productName;//产品名
    private String manufacturer;//制造商
    private String device;//设备名
    private String hardware;//硬件
    private String fingerprint;//指纹
    private String serialno;//串口序列号；;
    private String blueAddress;//蓝牙地址
    private String CPU_ABI;//系统架构1
    private String CPU_ABI2;//系统架构2
    private String radioVersion;//固定版本

    private String BUILD_TAGS;
    private String BUILD_TYPE;
    private String BUILD_USER;

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteAddress() {
        return routeAddress;
    }

    public void setRouteAddress(String routeAddress) {
        this.routeAddress = routeAddress;
    }

    public String getBUILD_TAGS() {
        return BUILD_TAGS;
    }

    public void setBUILD_TAGS(String BUILD_TAGS) {
        this.BUILD_TAGS = BUILD_TAGS;
    }

    public String getBUILD_USER() {
        return BUILD_USER;
    }

    public void setBUILD_USER(String BUILD_USER) {
        this.BUILD_USER = BUILD_USER;
    }

    public String getBUILD_TYPE() {
        return BUILD_TYPE;
    }

    public void setBUILD_TYPE(String BUILD_TYPE) {
        this.BUILD_TYPE = BUILD_TYPE;
    }

    public String getCPU_ABI() {
        return CPU_ABI;
    }

    public void setCPU_ABI(String CPU_ABI) {
        this.CPU_ABI = CPU_ABI;
    }

    public String getCPU_ABI2() {
        return CPU_ABI2;
    }

    public void setCPU_ABI2(String CPU_ABI2) {
        this.CPU_ABI2 = CPU_ABI2;
    }

    public String getRadioVersion() {
        return radioVersion;
    }

    public void setRadioVersion(String radioVersion) {
        this.radioVersion = radioVersion;
    }

    public String getBlueAddress() {
        return blueAddress;
    }

    public void setBlueAddress(String blueAddress) {
        this.blueAddress = blueAddress;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getSimSerialNumber() {
        return simSerialNumber;
    }

    public void setSimSerialNumber(String simSerialNumber) {
        this.simSerialNumber = simSerialNumber;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getSimCountryIso() {
        return simCountryIso;
    }

    public void setSimCountryIso(String simCountryIso) {
        this.simCountryIso = simCountryIso;
    }

    public String getSimOperator() {
        return simOperator;
    }

    public void setSimOperator(String simOperator) {
        this.simOperator = simOperator;
    }

    public String getSimOperatorName() {
        return simOperatorName;
    }

    public void setSimOperatorName(String simOperatorName) {
        this.simOperatorName = simOperatorName;
    }

    public String getNetworkCountryIso() {
        return networkCountryIso;
    }

    public void setNetworkCountryIso(String networkCountryIso) {
        this.networkCountryIso = networkCountryIso;
    }

    public String getNetworkOperator() {
        return networkOperator;
    }

    public void setNetworkOperator(String networkOperator) {
        this.networkOperator = networkOperator;
    }

    public String getNetworkOperatorName() {
        return networkOperatorName;
    }

    public void setNetworkOperatorName(String networkOperatorName) {
        this.networkOperatorName = networkOperatorName;
    }

    public int getNetworkType() {
        return networkType;
    }

    public void setNetworkType(int networkType) {
        this.networkType = networkType;
    }

    public int getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(int phoneType) {
        this.phoneType = phoneType;
    }

    public int getSimState() {
        return simState;
    }

    public void setSimState(int simState) {
        this.simState = simState;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }
}
