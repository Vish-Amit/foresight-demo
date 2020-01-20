package com.inn.foresight.module.nv.app.wrapper;

import java.util.Date;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;

/**
 * The Class DeviceInfoWrapper.
 *
 * @author innoeye date - 22-Feb-2018 8:48:22 PM
 */

@JpaWrapper
@RestWrapper
public class DeviceInfoWrapper {

	/** The device id. */
	private String deviceId;

	/** The license id. */
	private String licenseId;

	/** The u id. */
	private Integer uId;

	/** The status. */
	private String status;

	/** The reason. */
	private String reason;

	private Long modificationTime;
	private String make;
	private String model;
	private String appVersion;
	private Integer id;
	private String imsi;
	private String imei;
	private Integer expiryTime;
	private Long expiryTimeInMillis;
  
	private String userCustomName;


	private Long count;
	private Integer licenceMasterId;
	
	public DeviceInfoWrapper(Long count,Integer licenceMasterId) {
		super();
		this.count=count;
		this.licenceMasterId=licenceMasterId;
	}

	/**
	 * Instantiates a new device info wrapper.
	 *
	 * @param deviceId
	 *            the device id
	 * @param licenseId
	 *            the license id
	 * @param expiryTime
	 *            the expiry time
	 * @param uId
	 *            the u id
	 * @param status
	 *            the status
	 */
	
	public DeviceInfoWrapper(String deviceId, String licenseId, Integer expiryTime,Long expiryTimeInMillis, Integer uId, String status) {
		super();
		this.deviceId = deviceId;
		this.licenseId = licenseId;
		this.expiryTime = expiryTime;
		this.uId = uId;
		this.status = status;
		this.expiryTimeInMillis=expiryTimeInMillis;
	}

	/**
	 * Instantiates a new device info wrapper.
	 *
	 * @param deviceId
	 *            the device id
	 * @param licenseId
	 *            the license id
	 * @param expiryTime
	 *            the expiry time
	 * @param uId
	 *            the u id
	 * @param status
	 *            the status
	 * @param reason
	 *            the reason
	 */
	public DeviceInfoWrapper(String deviceId, String licenseId, Integer expiryTime, Integer uId, String status,
			String reason) {
		super();
		this.deviceId = deviceId;
		this.licenseId = licenseId;
		this.expiryTime = expiryTime;
		this.uId = uId;
		this.status = status;
		this.reason = reason;
	}

	/**
	 * Instantiates a new device info wrapper.
	 *
	 * @param status
	 *            the status
	 * @param reason
	 *            the reason
	 */
	public DeviceInfoWrapper(String status, String reason) {
		super();
		this.status = status;
		this.reason = reason;
	}

	public DeviceInfoWrapper(Integer id,String deviceId, Date modificationTime, String make, String model, String appVersion) {
		super();
		this.id=id;
		this.deviceId = deviceId;
		if (modificationTime != null) {
			this.modificationTime = modificationTime.getTime();
		}
		this.make = make;
		this.model = model;
		this.appVersion = appVersion;
	}
	
	
	
	public DeviceInfoWrapper(Integer id,String deviceId, Date modificationTime, String make, String model, String appVersion,String imei,String imsi) {
		super();
		this.id=id;
		this.deviceId = deviceId;
		if (modificationTime != null) {
			this.modificationTime = modificationTime.getTime();
		}
		this.make = make;
		this.model = model;
		this.appVersion = appVersion;
		this.imsi=imsi;
		this.imei=imei;
	}

	
	public DeviceInfoWrapper(Integer id,String deviceId, Date modificationTime, String make, String model, String appVersion,String imei,String imsi,String userCustomName) {
		super();
		this.id=id;
		this.deviceId = deviceId;
		if (modificationTime != null) {
			this.modificationTime = modificationTime.getTime();
		}
		this.make = make;
		this.model = model;
		this.appVersion = appVersion;
		this.imsi=imsi;
		this.imei=imei;
		this.userCustomName=userCustomName;
	}

	
	
	
	public String getUserCustomName() {
		return userCustomName;
	}

	public void setUserCustomName(String userCustomName) {
		this.userCustomName = userCustomName;
	}

	/**
	 * Gets the device id.
	 *
	 * @return the device id
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * Sets the device id.
	 *
	 * @param deviceId
	 *            the new device id
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * Gets the license id.
	 *
	 * @return the license id
	 */
	public String getLicenseId() {
		return licenseId;
	}

	/**
	 * Sets the license id.
	 *
	 * @param licenseId
	 *            the new license id
	 */
	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}


	/**
	 * Gets the u id.
	 *
	 * @return the u id
	 */
	public Integer getuId() {
		return uId;
	}

	/**
	 * Sets the u id.
	 *
	 * @param uId
	 *            the new u id
	 */
	public void setuId(Integer uId) {
		this.uId = uId;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status
	 *            the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the reason.
	 *
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * Sets the reason.
	 *
	 * @param reason
	 *            the new reason
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return the modificationTime
	 */
	public Long getModificationTime() {
		return modificationTime;
	}

	/**
	 * @param modificationTime the modificationTime to set
	 */
	public void setModificationTime(Long modificationTime) {
		this.modificationTime = modificationTime;
	}

	/**
	 * @return the make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * @param make the make to set
	 */
	public void setMake(String make) {
		this.make = make;
	}

	/**
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return the appVersion
	 */
	public String getAppVersion() {
		return appVersion;
	}

	/**
	 * @param appVersion the appVersion to set
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * @return the expiryTime
	 */
	public Integer getExpiryTime() {
		return expiryTime;
	}

	/**
	 * @param expiryTime the expiryTime to set
	 */
	public void setExpiryTime(Integer expiryTime) {
		this.expiryTime = expiryTime;
	}

	/**
	 * @return the expiryTimeInMillis
	 */
	public Long getExpiryTimeInMillis() {
		return expiryTimeInMillis;
	}

	/**
	 * @param expiryTimeInMillis the expiryTimeInMillis to set
	 */
	public void setExpiryTimeInMillis(Long expiryTimeInMillis) {
		this.expiryTimeInMillis = expiryTimeInMillis;
	}

	/**
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Long count) {
		this.count = count;
	}

	/**
	 * @return the licenceMasterId
	 */
	public Integer getLicenceMasterId() {
		return licenceMasterId;
	}

	/**
	 * @param licenceMasterId the licenceMasterId to set
	 */
	public void setLicenceMasterId(Integer licenceMasterId) {
		this.licenceMasterId = licenceMasterId;
	}

	
	
	
	
}
