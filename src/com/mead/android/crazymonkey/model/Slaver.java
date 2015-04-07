package com.mead.android.crazymonkey.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Slaver {

	private String slaverMAC;

	private String slaverIP;

	private String vpnMAC;

	private String vpnIP;

	public Slaver() {

	}

	public String getSlaverIP() {
		return slaverIP;
	}

	public String getSlaverMAC() {
		return slaverMAC;
	}

	public String getVpnIP() {
		return vpnIP;
	}

	public String getVpnMAC() {
		return vpnMAC;
	}

	public void setSlaverIP(String slaverIP) {
		this.slaverIP = slaverIP;
	}

	public void setSlaverMAC(String slaverMAC) {
		this.slaverMAC = slaverMAC;
	}

	public void setVpnIP(String vpnIP) {
		this.vpnIP = vpnIP;
	}

	public void setVpnMAC(String vpnMAC) {
		this.vpnMAC = vpnMAC;
	}
}
