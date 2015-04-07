package com.mead.android.crazymonkey.model;

@Deprecated
public class Phone {

	private String manufacturer;

	private String modelName;

	private String modelId;

	private String IMEI;

	private String IMSI;

	private String wifiMAC;

	public Phone() {

	}
	
	public String getIMEI() {
		return IMEI;
	}

	public String getIMSI() {
		return IMSI;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public String getModelId() {
		return modelId;
	}

	public String getModelName() {
		return modelName;
	}

	public String getWifiMAC() {
		return wifiMAC;
	}

	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}

	public void setIMSI(String iMSI) {
		IMSI = iMSI;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public void setWifiMAC(String wifiMAC) {
		this.wifiMAC = wifiMAC;
	}
}
