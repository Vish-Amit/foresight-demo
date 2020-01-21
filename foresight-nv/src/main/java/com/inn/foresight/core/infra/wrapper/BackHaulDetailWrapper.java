package com.inn.foresight.core.infra.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;

@JpaWrapper
public class BackHaulDetailWrapper {
	private String hubSiteName;
	private String neType;
	private String connectedenb;
	private String deviceid;
	private String devicefamily;
	private String commstate;
	private String state;
	private String systemoid;
	private String netmask;
	private String neName;
	private Double capacity;
	
	
	public BackHaulDetailWrapper() {
		super();
	}
	//RAN Access Report
	public BackHaulDetailWrapper(String neName,Double capacity) {
		super();
		this.neName=neName;
		this.capacity=capacity;
	}
	public BackHaulDetailWrapper(String hubSiteName, String neType, String connectedenb, String deviceid,
			String devicefamily, String commstate, String state, String systemoid, String netmask) {
		super();
		this.hubSiteName = hubSiteName;
		this.neType = neType;
		this.connectedenb = connectedenb;
		this.deviceid = deviceid;
		this.devicefamily = devicefamily;
		this.commstate = commstate;
		this.state = state;
		this.systemoid = systemoid;
		this.netmask = netmask;
	}
	public String getHubSiteName() {
		return hubSiteName;
	}
	public void setHubSiteName(String hubSiteName) {
		this.hubSiteName = hubSiteName;
	}
	public String getNeType() {
		return neType;
	}
	public void setNeType(String neType) {
		this.neType = neType;
	}
	public String getConnectedenb() {
		return connectedenb;
	}
	public void setConnectedenb(String connectedenb) {
		this.connectedenb = connectedenb;
	}
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public String getDevicefamily() {
		return devicefamily;
	}
	public void setDevicefamily(String devicefamily) {
		this.devicefamily = devicefamily;
	}
	public String getCommstate() {
		return commstate;
	}
	public void setCommstate(String commstate) {
		this.commstate = commstate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSystemoid() {
		return systemoid;
	}
	public void setSystemoid(String systemoid) {
		this.systemoid = systemoid;
	}
	public String getNetmask() {
		return netmask;
	}
	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}
	public String getNeName() {
		return neName;
	}
	public void setNeName(String neName) {
		this.neName = neName;
	}
	public Double getCapacity() {
		return capacity;
	}
		public void setCapacity(Double capacity) {
		this.capacity = capacity;
	}
	@Override
	public String toString() {
		return "BackHaulDetailWrapper [hubSiteName=" + hubSiteName + ", neType=" + neType + ", connectedenb="
				+ connectedenb + ", deviceid=" + deviceid + ", devicefamily=" + devicefamily + ", commstate="
				+ commstate + ", state=" + state + ", systemoid=" + systemoid + ", netmask=" + netmask + ", neName="
				+ neName + ", capacity=" + capacity + "]";
	}
	

	
}
