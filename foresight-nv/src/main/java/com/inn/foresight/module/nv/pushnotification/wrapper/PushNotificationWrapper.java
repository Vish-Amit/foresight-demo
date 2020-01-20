package com.inn.foresight.module.nv.pushnotification.wrapper;

import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification.ModuleName;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification.OS;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification.Status;

@RestWrapper
public class PushNotificationWrapper {
	private Status status = Status.NEW;
	private Integer creatorId;
	private String deviceId;
	private String imsi;
	private String imei;
	private ModuleName moduleName;
	private String notificationPayLoad;
	private String remark;
	private Integer pushnotificationId;
	private Boolean isToUseClientFCMKey;
	private OS mobileOsType;
	
	
	
	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	/**
	 * @return the creatorId
	 */
	public Integer getCreatorId() {
		return creatorId;
	}
	/**
	 * @param creatorId the creatorId to set
	 */
	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}
	/**
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}
	/**
	 * @param deviceId the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	/**
	 * @return the moduleName
	 */
	public ModuleName getModuleName() {
		return moduleName;
	}
	/**
	 * @param moduleName the moduleName to set
	 */
	public void setModuleName(ModuleName moduleName) {
		this.moduleName = moduleName;
	}
	/**
	 * @return the notificationPayLoad
	 */
	public String getNotificationPayLoad() {
		return notificationPayLoad;
	}
	/**
	 * @param notificationPayLoad the notificationPayLoad to set
	 */
	public void setNotificationPayLoad(String notificationPayLoad) {
		this.notificationPayLoad = notificationPayLoad;
	}
	/**
	 * @return the imsi
	 */
	public String getImsi() {
		return imsi;
	}
	/**
	 * @param imsi the imsi to set
	 */
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	/**
	 * @return the imei
	 */
	public String getImei() {
		return imei;
	}
	/**
	 * @param imei the imei to set
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the pushnotificationId
	 */
	public Integer getPushnotificationId() {
		return pushnotificationId;
	}
	/**
	 * @param pushnotificationId the pushnotificationId to set
	 */
	public void setPushnotificationId(Integer pushnotificationId) {
		this.pushnotificationId = pushnotificationId;
	}
	
	public PushNotificationWrapper(String deviceId, ModuleName moduleName, String notificationPayLoad) {
		super();
		this.deviceId = deviceId;
		this.moduleName = moduleName;
		this.notificationPayLoad = notificationPayLoad;
	}
	
	public PushNotificationWrapper() {
		this.status = Status.NEW;
	}
	/**
	 * @return the isToUseClientFCMKey
	 */
	public Boolean getIsToUseClientFCMKey() {
		return isToUseClientFCMKey;
	}
	/**
	 * @param isToUseClientFCMKey the isToUseClientFCMKey to set
	 */
	public void setIsToUseClientFCMKey(Boolean isToUseClientFCMKey) {
		this.isToUseClientFCMKey = isToUseClientFCMKey;
	}
	/**
	 * @return the mobileOsType
	 */
	public OS getMobileOsType() {
		return mobileOsType;
	}
	/**
	 * @param mobileOsType the mobileOsType to set
	 */
	public void setMobileOsType(OS mobileOsType) {
		this.mobileOsType = mobileOsType;
	}

	
}
