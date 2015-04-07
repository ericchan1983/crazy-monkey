package com.mead.android.crazymonkey.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Device {

	// 系统架构（请保留下划线，下划线隔开的为两个数据）
	private String ARCH = "armeabi-v7a_armeabi";
	// 品牌
	private String BRAND = "google";
	// 品牌
	private String DEVICE = "maguro";
	// 指纹
	private String FINGERPRINT = "google/takju/maguro:4.3/JWR66Y/776638:user/release-keys";
	// 硬件
	private String HARDWARE = "tuna";
	// 制造商
	private String MANUFACTURER = "samsung";
	// 型号
	private String MODEL = "Galaxy Nexus";
	// 产品名
	private String PRODUCT = "takju";
	// 系统版本
	private String RELEASE = "4.3";	
	// 系统版本值
	private String SDK = "18";
	// gprs wifi none
	private String connect_mode = "0";
	// 密度 1.0
	private String density = "2.0";
	// 160	
	private String densityDpi = "320";
	// no messages
	private String get = "I9250XXLJ1";
	// 无线路由器地址
	private String getBSSID = "9c:21:6a:e0:84:ca";
	// IMEI
	private String getDeviceId = "351554052632941";
	// 基站位置（需要手动更改）
	private String getJiZhan = "43016_11021269";
	// 手机号码
	private String getLine1Number = "13499122278";
	// mac地址
	private String getMacAddress = "64:c0:84:79:b7:cf";
	// 屏幕分辨率
	private String getMetrics = "720x1184";	
	// 国家iso代码
	private String getNetworkCountryIso = "cn";
	// 网络类型
	/*
	 * 460 06 (unknown) China
	 * 460 00 China Mobile China
	 * 460 02 China Mobile China
	 * 460 07 China Mobile China
	 * 460 03 China Telecom China
	 * 460 05 China Telecom China
	 * 460 20 China Tietong China
	 * 460 01 China Unicom China
	 */
	private String getNetworkOperator = "46000";
	// 网络类型名
	private String getNetworkOperatorName = "中国移动";
	// 网络类型
	/*
	 * NETWORK_TYPE_CDMA 4
	 * NETWORK_TYPE_EDGE 2
	 * NETWORK_TYPE_EHRPD 14
	 * NETWORK_TYPE_EVDO_0 5
	 * NETWORK_TYPE_EVDO_A 6
	 * NETWORK_TYPE_EVDO_B 12
	 * NETWORK_TYPE_GPRS 1
	 * NETWORK_TYPE_HSDPA 8
	 * NETWORK_TYPE_HSPA 10
	 * NETWORK_TYPE_HSPAP 15
	 * NETWORK_TYPE_HSUPA 9
	 * NETWORK_TYPE_IDEN 11
	 * NETWORK_TYPE_LTE 13
	 * NETWORK_TYPE_UMTS 3
	 * NETWORK_TYPE_UNKNOWN 0
	 */
	private String getNetworkType = "1";
	// 手机类型
	/*
	 * PHONE_TYPE_GSM 1
	 * PHONE_TYPE_NONE 0
	 * PHONE_TYPE_SIP 3
	 * PHONE_TYPE_CDMA 2
	 */
	private String getPhoneType = "1";

	// 固件版本
	private String getRadioVersion = "I9250XXLJ1";
	// 无线路由器名
	private String getSSID = "home";
	// 手机卡国家
	private String getSimCountryIso = "cn";
	// 运营商
	private String getSimOperator = "46000";
	// 运营商名字
	private String getSimOperatorName = "中国移动";
	// 手机卡序列号
	private String getSimSerialNumber = "89860315331900403897";

	// 手机卡状态 SIM_OK 0 SIM_NO -1 SIM_UNKNOW -2
	private String getSimState = "0";
	// android_id 9774d56d682e549b
	private String getString = "05ec66a01f58f1a0";
	// IMSI
	private String getSubscriberId = "460000925417854";
	// gps 位置
	private String gps = null;
	// 位置模拟类型
	private String location_mode = "0";
	// 缩放比例
	private String scaledDensity = "2.0";
	// cpu型号
	private String setCpuName = "Tuna";
	// 签名
	private String sign = "718E95ABAA307C583CFC5A9EAA5FB73E";
	// 横向 160.0
	private String xdpi = "315.31033";
	// 纵向 160.0
	private String ydpi = "318.7451";

	public Device() {
		super();
	}
	
	@JsonProperty("ARCH")
	public String getARCH() {
		return ARCH;
	}

	@JsonProperty("ARCH")
	public void setARCH(String aRCH) {
		ARCH = aRCH;
	}

	@JsonProperty("BRAND")
	public String getBRAND() {
		return BRAND;
	}

	@JsonProperty("BRAND")
	public void setBRAND(String bRAND) {
		BRAND = bRAND;
	}

	@JsonProperty("DEVICE")
	public String getDEVICE() {
		return DEVICE;
	}

	@JsonProperty("DEVICE")
	public void setDEVICE(String dEVICE) {
		DEVICE = dEVICE;
	}

	@JsonProperty("FINGERPRINT")
	public String getFINGERPRINT() {
		return FINGERPRINT;
	}

	@JsonProperty("FINGERPRINT")
	public void setFINGERPRINT(String fINGERPRINT) {
		FINGERPRINT = fINGERPRINT;
	}

	@JsonProperty("HARDWARE")
	public String getHARDWARE() {
		return HARDWARE;
	}

	@JsonProperty("HARDWARE")
	public void setHARDWARE(String hARDWARE) {
		HARDWARE = hARDWARE;
	}

	@JsonProperty("MANUFACTURER")
	public String getMANUFACTURER() {
		return MANUFACTURER;
	}

	@JsonProperty("MANUFACTURER")
	public void setMANUFACTURER(String mANUFACTURER) {
		MANUFACTURER = mANUFACTURER;
	}

	@JsonProperty("MODEL")
	public String getMODEL() {
		return MODEL;
	}

	@JsonProperty("MODEL")
	public void setMODEL(String mODEL) {
		MODEL = mODEL;
	}

	@JsonProperty("PRODUCT")
	public String getPRODUCT() {
		return PRODUCT;
	}

	@JsonProperty("PRODUCT")
	public void setPRODUCT(String pRODUCT) {
		PRODUCT = pRODUCT;
	}

	@JsonProperty("RELEASE")
	public String getRELEASE() {
		return RELEASE;
	}

	@JsonProperty("RELEASE")
	public void setRELEASE(String rELEASE) {
		RELEASE = rELEASE;
	}

	@JsonProperty("SDK")
	public String getSDK() {
		return SDK;
	}

	@JsonProperty("SDK")
	public void setSDK(String sDK) {
		SDK = sDK;
	}

	@JsonProperty("connect_mode")
	public String getConnect_mode() {
		return connect_mode;
	}

	@JsonProperty("connect_mode")
	public void setConnect_mode(String connect_mode) {
		this.connect_mode = connect_mode;
	}

	@JsonProperty("density")
	public String getDensity() {
		return density;
	}

	@JsonProperty("density")
	public void setDensity(String density) {
		this.density = density;
	}

	@JsonProperty("densityDpi")
	public String getDensityDpi() {
		return densityDpi;
	}

	@JsonProperty("densityDpi")
	public void setDensityDpi(String densityDpi) {
		this.densityDpi = densityDpi;
	}

	@JsonProperty("get")
	public String getGet() {
		return get;
	}

	@JsonProperty("get")
	public void setGet(String get) {
		this.get = get;
	}

	@JsonProperty("getBSSID")
	public String getGetBSSID() {
		return getBSSID;
	}

	@JsonProperty("getBSSID")
	public void setGetBSSID(String getBSSID) {
		this.getBSSID = getBSSID;
	}

	@JsonProperty("getDeviceId")
	public String getGetDeviceId() {
		return getDeviceId;
	}

	@JsonProperty("getDeviceId")
	public void setGetDeviceId(String getDeviceId) {
		this.getDeviceId = getDeviceId;
	}

	@JsonProperty("getJiZhan")
	public String getGetJiZhan() {
		return getJiZhan;
	}

	@JsonProperty("getJiZhan")
	public void setGetJiZhan(String getJiZhan) {
		this.getJiZhan = getJiZhan;
	}

	@JsonProperty("getLine1Number")
	public String getGetLine1Number() {
		return getLine1Number;
	}

	@JsonProperty("getLine1Number")
	public void setGetLine1Number(String getLine1Number) {
		this.getLine1Number = getLine1Number;
	}

	@JsonProperty("getMacAddress")
	public String getGetMacAddress() {
		return getMacAddress;
	}

	@JsonProperty("getMacAddress")
	public void setGetMacAddress(String getMacAddress) {
		this.getMacAddress = getMacAddress;
	}

	@JsonProperty("getMetrics")
	public String getGetMetrics() {
		return getMetrics;
	}

	@JsonProperty("getMetrics")
	public void setGetMetrics(String getMetrics) {
		this.getMetrics = getMetrics;
	}

	@JsonProperty("getNetworkCountryIso")
	public String getGetNetworkCountryIso() {
		return getNetworkCountryIso;
	}

	@JsonProperty("getNetworkCountryIso")
	public void setGetNetworkCountryIso(String getNetworkCountryIso) {
		this.getNetworkCountryIso = getNetworkCountryIso;
	}

	@JsonProperty("getNetworkOperator")
	public String getGetNetworkOperator() {
		return getNetworkOperator;
	}

	@JsonProperty("getNetworkOperator")
	public void setGetNetworkOperator(String getNetworkOperator) {
		this.getNetworkOperator = getNetworkOperator;
	}

	@JsonProperty("getNetworkOperatorName")
	public String getGetNetworkOperatorName() {
		return getNetworkOperatorName;
	}

	@JsonProperty("getNetworkOperatorName")
	public void setGetNetworkOperatorName(String getNetworkOperatorName) {
		this.getNetworkOperatorName = getNetworkOperatorName;
	}

	@JsonProperty("getNetworkType")
	public String getGetNetworkType() {
		return getNetworkType;
	}

	@JsonProperty("getNetworkType")
	public void setGetNetworkType(String getNetworkType) {
		this.getNetworkType = getNetworkType;
	}

	@JsonProperty("getPhoneType")
	public String getGetPhoneType() {
		return getPhoneType;
	}

	@JsonProperty("getPhoneType")
	public void setGetPhoneType(String getPhoneType) {
		this.getPhoneType = getPhoneType;
	}

	@JsonProperty("getRadioVersion")
	public String getGetRadioVersion() {
		return getRadioVersion;
	}

	@JsonProperty("getRadioVersion")
	public void setGetRadioVersion(String getRadioVersion) {
		this.getRadioVersion = getRadioVersion;
	}

	@JsonProperty("getSSID")
	public String getGetSSID() {
		return getSSID;
	}

	@JsonProperty("getSSID")
	public void setGetSSID(String getSSID) {
		this.getSSID = getSSID;
	}

	@JsonProperty("getSimCountryIso")
	public String getGetSimCountryIso() {
		return getSimCountryIso;
	}

	@JsonProperty("getSimCountryIso")
	public void setGetSimCountryIso(String getSimCountryIso) {
		this.getSimCountryIso = getSimCountryIso;
	}

	@JsonProperty("getSimOperator")
	public String getGetSimOperator() {
		return getSimOperator;
	}

	@JsonProperty("getSimOperator")
	public void setGetSimOperator(String getSimOperator) {
		this.getSimOperator = getSimOperator;
	}

	@JsonProperty("getSimOperatorName")
	public String getGetSimOperatorName() {
		return getSimOperatorName;
	}

	@JsonProperty("getSimOperatorName")
	public void setGetSimOperatorName(String getSimOperatorName) {
		this.getSimOperatorName = getSimOperatorName;
	}

	@JsonProperty("getSimSerialNumber")
	public String getGetSimSerialNumber() {
		return getSimSerialNumber;
	}

	@JsonProperty("getSimSerialNumber")
	public void setGetSimSerialNumber(String getSimSerialNumber) {
		this.getSimSerialNumber = getSimSerialNumber;
	}

	@JsonProperty("getSimState")
	public String getGetSimState() {
		return getSimState;
	}

	@JsonProperty("getSimState")
	public void setGetSimState(String getSimState) {
		this.getSimState = getSimState;
	}

	@JsonProperty("getString")
	public String getGetString() {
		return getString;
	}

	@JsonProperty("getString")
	public void setGetString(String getString) {
		this.getString = getString;
	}

	@JsonProperty("getSubscriberId")
	public String getGetSubscriberId() {
		return getSubscriberId;
	}

	@JsonProperty("getSubscriberId")
	public void setGetSubscriberId(String getSubscriberId) {
		this.getSubscriberId = getSubscriberId;
	}

	@JsonProperty("gps")
	public String getGps() {
		return gps;
	}

	@JsonProperty("gps")
	public void setGps(String gps) {
		this.gps = gps;
	}

	@JsonProperty("location_mode")
	public String getLocation_mode() {
		return location_mode;
	}

	@JsonProperty("location_mode")
	public void setLocation_mode(String location_mode) {
		this.location_mode = location_mode;
	}

	@JsonProperty("scaledDensity")
	public String getScaledDensity() {
		return scaledDensity;
	}

	@JsonProperty("scaledDensity")
	public void setScaledDensity(String scaledDensity) {
		this.scaledDensity = scaledDensity;
	}

	@JsonProperty("setCpuName")
	public String getSetCpuName() {
		return setCpuName;
	}

	@JsonProperty("setCpuName")
	public void setSetCpuName(String setCpuName) {
		this.setCpuName = setCpuName;
	}

	@JsonProperty("sign")
	public String getSign() {
		return sign;
	}

	@JsonProperty("sign")
	public void setSign(String sign) {
		this.sign = sign;
	}

	@JsonProperty("xdpi")
	public String getXdpi() {
		return xdpi;
	}

	@JsonProperty("xdpi")
	public void setXdpi(String xdpi) {
		this.xdpi = xdpi;
	}

	@JsonProperty("ydpi")
	public String getYdpi() {
		return ydpi;
	}

	@JsonProperty("ydpi")
	public void setYdpi(String ydpi) {
		this.ydpi = ydpi;
	}

}
