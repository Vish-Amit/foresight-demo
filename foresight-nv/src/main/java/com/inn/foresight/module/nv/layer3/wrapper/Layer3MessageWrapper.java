package com.inn.foresight.module.nv.layer3.wrapper;


/**
 * @author innoeye
 * date - 10-Jan-2018 12:44:27 PM
 */
public class Layer3MessageWrapper {

	private String timeStamp;
	private String id;
	private String latitude;
	private String longitude;
	private String encodedMsg;
	private String decodedMsg;
	private String pci;
	private String channelType;
	private String handOverTime;
	private String targetPCI;
	private String lastRowKey;

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLatitude() {
		return latitude;
	}


	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getEncodedMsg() {
		return encodedMsg;
	}

	public void setEncodedMsg(String encodedMsg) {
		this.encodedMsg = encodedMsg;
	}

	public String getDecodedMsg() {
		return decodedMsg;
	}

	public void setDecodedMsg(String decodedMsg) {
		this.decodedMsg = decodedMsg;
	}

	public String getPci() {
		return pci;
	}

	public void setPci(String pci) {
		this.pci = pci;
	}

	public String getChannelType() {
		return channelType;
	}

	public void setChannelType(String channelType) {
		this.channelType = channelType;
	}

	public String getHandOverTime() {
		return handOverTime;
	}

	public void setHandOverTime(String handOverTime) {
		this.handOverTime = handOverTime;
	}

	public String getTargetPCI() {
		return targetPCI;
	}

	public void setTargetPCI(String targetPCI) {
		this.targetPCI = targetPCI;
	}

	public String getLastRowKey() {
		return lastRowKey;
	}

	public void setLastRowKey(String lastRowKey) {
		this.lastRowKey = lastRowKey;
	}

}
