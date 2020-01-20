package com.inn.foresight.module.nv.app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.product.um.user.model.User;

/** The Class DeviceInfo. */

@NamedQuery(name = AppConstants.GET_DEVICEINFO_BY_APKID, query = "select d from DeviceInfo d where d.imsi = :imsi and d.imei = :imei and d.apkDetail.isDeleted = false")

@NamedQuery(name = AppConstants.GET_DEVICE_INFO_BY_USER_ID, query = "SELECT d FROM DeviceInfo d WHERE d.user.userid=:id ORDER BY modificationTime DESC")

@NamedQuery(name = AppConstants.GET_DEVICEINFO_BY_IMEI_AND_IMSI, query = "select d from DeviceInfo d where d.imei=:imei and d.imsi=:imsi ")

@NamedQuery(name = AppConstants.GET_DEVICEINFO_BY_IMSI, query = "select d from DeviceInfo d where  d.imsi=:imsi ORDER BY d.modificationTime DESC")

@NamedQuery(name = AppConstants.GET_DISTINCT_OS, query = "select distinct d.deviceOS from DeviceInfo d")

@NamedQuery(name = AppConstants.GET_DISTINCT_MAKE, query = "select distinct d.make from DeviceInfo d")

@NamedQuery(name = AppConstants.GET_DISTINCT_MODEL, query = "select distinct d.model from DeviceInfo d where d.make = :make")

@NamedQuery(name = AppConstants.GET_DEVICEINFO_BY_DEVICE_ID, query = "select d from DeviceInfo d where d.deviceId = :deviceId")
@NamedQuery(name = AppConstants.GET_DEVICEINFO_LIST_BY_IMEI_AND_IMSI, query = "select d from DeviceInfo d ")

@NamedQuery(name = AppConstants.GET_DEVICEINFO_BY_MSISDN, query = "select d from DeviceInfo d where  d.msisdn in (:msisdnList) and d.isRegistered = true")

@NamedQuery(name = AppConstants.GET_DEVICE_INFO_BY_USER_NAME, query = "SELECT new  com.inn.foresight.module.nv.app.wrapper.DeviceInfoWrapper(d.id,d.deviceId,d.modificationTime,d.make,d.model,d.appVersion,d.imei,d.imsi,d.userCustomName) FROM DeviceInfo d , NVDeviceData n ,SystemConfiguration s WHERE d.user.userName in (:username) and n.deviceInfo.id=d.id and s.name='DEFAULT_OPERATOR'  and upper(n.operator) like concat('%',upper(s.value),'%') ORDER BY d.modificationTime DESC")

@NamedQuery(name = AppConstants.GET_DEVICE_INFO_LIST_COUNT_BY_USER_NAME, query = "SELECT count (*) FROM DeviceInfo d, NVDeviceData n,SystemConfiguration s WHERE d.user.userName=:username and n.deviceInfo.id=d.id and s.name='DEFAULT_OPERATOR'  and upper(n.operator) like concat('%',upper(s.value),'%') ")

@NamedQuery(name = AppConstants.GET_DEVICE_ID_BY_IMEI, query = "select d.deviceId from DeviceInfo d where imei = :imei")

@NamedQuery(name = AppConstants.GET_DEVICE_LIST, query = "SELECT new  com.inn.foresight.module.nv.app.wrapper.DeviceInfoWrapper(d.id,d.deviceId,d.modificationTime,d.make,d.model,d.appVersion) FROM DeviceInfo d , NVDeviceData n ,SystemConfiguration s WHERE n.deviceInfo.id=d.id and s.name='DEFAULT_OPERATOR'  and upper(s.value)=upper(n.operator)  ORDER BY d.modificationTime DESC")

@NamedQuery(name = AppConstants.GET_DEVICE_INFO_LIST_COUNT, query = "SELECT count(*) FROM DeviceInfo d , NVDeviceData n ,SystemConfiguration s WHERE n.deviceInfo.id=d.id and s.name='DEFAULT_OPERATOR'  and upper(s.value)=upper(n.operator)  ORDER BY d.modificationTime DESC")
@NamedQuery(name = "getDeviceIdForImei", query = "select d from DeviceInfo d where d.imei = :imei")
@NamedQuery(name = AppConstants.GET_DEVICE_INFO_BY_MDN, query = "select d from DeviceInfo d where upper(d.source)=:source and d.msisdn=:msisdn")

@NamedQuery(name = NVConstant.GET_DEVICEINFO_BY_DEVICE_ID_LIST, query = "select d from DeviceInfo d where d.deviceId in (:deviceIdList)")
@NamedQuery(name = NVConstant.GET_DEVICEINFO_BY_APKID_AND_IMEI, query = "select d from DeviceInfo d where d.imei = :imei and d.apkDetail.isDeleted = false")

@FilterDef(name = DeviceConstant.DEVICE_INFO_IMEI_FILTER, parameters = {
		@ParamDef(name = "imei", type = "java.lang.String") })

@FilterDef(name = DeviceConstant.DEVICE_INFO_IMSI_FILTER, parameters = {
		@ParamDef(name = "imsi", type = "java.lang.String") })

@FilterDef(name = DeviceConstant.INSTALLED_FILTER, parameters = {
		@ParamDef(name = "isInstalled", type = "java.lang.Boolean") })

@Filters(value = {
		@Filter(name = DeviceConstant.DEVICE_INFO_IMEI_FILTER, condition = "deviceinfoid_pk in (select d.deviceinfoid_pk from DeviceInfo d "
				+ "where d.imei in (:imei))"),

		@Filter(name = DeviceConstant.DEVICE_INFO_IMSI_FILTER, condition = "deviceinfoid_pk in (select d.deviceinfoid_pk from DeviceInfo d "
				+ "where d.imsi in (:imsi))"),

		@Filter(name = DeviceConstant.INSTALLED_FILTER, condition = "isInstalled = :isInstalled"),

})

@Entity
@Table(name = "DeviceInfo")
@XmlRootElement(name = "DeviceInfo")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@DynamicUpdate
public class DeviceInfo implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "deviceinfoid_pk")
	private Integer id;

	/** The apk detail. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "apkdetailid_fk", nullable = false)
	private APKDetail apkDetail;

	/** The creator. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creatorid_fk", nullable = true)
	private User creator;

	/** The modifier. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lastModifierid_fk", nullable = true)
	private User modifier;

	/** The device id. */
	@Column(name = "deviceid")
	private String deviceId;

	/** The imsi. */
	@Column(name = "imsi")
	private String imsi;

	/** The imei. */
	@Column(name = "imei")
	private String imei;

	/** The creation time. */
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modification time. */
	@Column(name = "modificationtime")
	private Date modificationTime;

	/** The fcm key. */
	@Column(name = "fcmkey")
	private String fcmKey;

	/** The make. */
	@Column(name = "make")
	private String make;

	/** The model. */
	@Column(name = "model")
	private String model;

	/** The device OS. */
	@Column(name = "deviceos")
	private String deviceOS;

	/** The app version. */
	@Column(name = "appversion")
	private String appVersion;

	/** The license id. */
	@Transient
	private String licenseId;

	/** The client name. */
	@Transient
	private String clientName;

	/** The days to expiry. */
	@Transient
	private Integer daysToExpiry;

	/** The user. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid_fk", nullable = true)
	private User user;

	/** The old imei. */
	@Transient
	private String oldImei;

	/** The registrationdate . */
	@Column(name = "registrationdate")
	private Date registrationDate;

	@Column(name = "msisdn")
	private String msisdn;

	@Column(name = "deregistrationdate")
	private Date deregistrationDate;

	@Column(name = "remark")
	private String remark;

	@Column(name = "registered")
	private Boolean isRegistered;

	@Enumerated(javax.persistence.EnumType.STRING)
	@Column(name = "source")
	private Source source = Source.NETVELOCITY;

	/** The fcm key. */
	@Column(name = "clientfcmkey")
	private String clientFcmKey;

	@Column(name = "installed")
	private Boolean isInstalled = Boolean.TRUE;

	@Column(name = "usercustomname")
	private String userCustomName;

	@Column(name = "sdkappversion")
	private String sdkAppVersion;

	@Column(name = "appname")
	private String appName;

	@Column(name = "pseudoid")
	private String pseudoId;

	/** The Enum Status. */
	public enum Source {
		NETVELOCITY, BBM,
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the apk detail.
	 *
	 * @return the apk detail
	 */
	public APKDetail getApkDetail() {
		return apkDetail;
	}

	/**
	 * Sets the apk detail.
	 *
	 * @param apkDetail the new apk detail
	 */
	public void setApkDetail(APKDetail apkDetail) {
		this.apkDetail = apkDetail;
	}

	/**
	 * Gets the creator.
	 *
	 * @return the creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * Sets the creator.
	 *
	 * @param creator the new creator
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}

	/**
	 * Gets the modifier.
	 *
	 * @return the modifier
	 */
	public User getModifier() {
		return modifier;
	}

	/**
	 * Sets the modifier.
	 *
	 * @param modifier the new modifier
	 */
	public void setModifier(User modifier) {
		this.modifier = modifier;
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
	 * @param deviceId the new device id
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * Gets the imsi.
	 *
	 * @return the imsi
	 */
	public String getImsi() {
		return imsi;
	}

	/**
	 * Sets the imsi.
	 *
	 * @param imsi the new imsi
	 */
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	/**
	 * Gets the imei.
	 *
	 * @return the imei
	 */
	public String getImei() {
		return imei;
	}

	/**
	 * Sets the imei.
	 *
	 * @param imei the new imei
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * Gets the creation time.
	 *
	 * @return the creation time
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the creation time.
	 *
	 * @param creationTime the new creation time
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Gets the modification time.
	 *
	 * @return the modification time
	 */
	public Date getModificationTime() {
		return modificationTime;
	}

	/**
	 * Sets the modification time.
	 *
	 * @param modificationTime the new modification time
	 */
	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	/**
	 * Gets the fcm key.
	 *
	 * @return the fcm key
	 */
	public String getFcmKey() {
		return fcmKey;
	}

	/**
	 * Sets the fcm key.
	 *
	 * @param fcmKey the new fcm key
	 */
	public void setFcmKey(String fcmKey) {
		this.fcmKey = fcmKey;
	}

	/**
	 * Gets the make.
	 *
	 * @return the make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * Sets the make.
	 *
	 * @param make the new make
	 */
	public void setMake(String make) {
		this.make = make;
	}

	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets the model.
	 *
	 * @param model the new model
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Gets the device OS.
	 *
	 * @return the device OS
	 */
	public String getDeviceOS() {
		return deviceOS;
	}

	/**
	 * Sets the device OS.
	 *
	 * @param deviceOS the new device OS
	 */
	public void setDeviceOS(String deviceOS) {
		this.deviceOS = deviceOS;
	}

	/**
	 * Gets the app version.
	 *
	 * @return the app version
	 */
	public String getAppVersion() {
		return appVersion;
	}

	/**
	 * Sets the app version.
	 *
	 * @param appVersion the new app version
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
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
	 * @param licenseId the new license id
	 */
	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}

	/**
	 * Gets the client name.
	 *
	 * @return the client name
	 */
	public String getClientName() {
		return clientName;
	}

	/**
	 * Sets the client name.
	 *
	 * @param clientName the new client name
	 */
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	/**
	 * Gets the days to expiry.
	 *
	 * @return the days to expiry
	 */
	public Integer getDaysToExpiry() {
		return daysToExpiry;
	}

	/**
	 * Sets the days to expiry.
	 *
	 * @param daysToExpiry the new days to expiry
	 */
	public void setDaysToExpiry(Integer daysToExpiry) {
		this.daysToExpiry = daysToExpiry;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Gets the old imei.
	 *
	 * @return the old imei
	 */
	public String getOldImei() {
		return oldImei;
	}

	/**
	 * Sets the old imei.
	 *
	 * @param oldImei the new old imei
	 */
	public void setOldImei(String oldImei) {
		this.oldImei = oldImei;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the registrationDate
	 */
	public Date getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * @param registrationDate the registrationDate to set
	 */
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	/**
	 * @return the msisdn
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * @param msisdn the msisdn to set
	 */
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
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
	 * @return the isRegistered
	 */
	public Boolean getIsRegistered() {
		return isRegistered;
	}

	/**
	 * @param isRegistered the isRegistered to set
	 */
	public void setIsRegistered(Boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	/**
	 * @return the source
	 */
	public Source getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(Source source) {
		this.source = source;
	}

	/**
	 * @return the deregistrationDate
	 */
	public Date getDeregistrationDate() {
		return deregistrationDate;
	}

	/**
	 * @param deregistrationDate the deregistrationDate to set
	 */
	public void setDeregistrationDate(Date deregistrationDate) {
		this.deregistrationDate = deregistrationDate;
	}

	/**
	 * @return the clientFcmKey
	 */
	public String getClientFcmKey() {
		return clientFcmKey;
	}

	/**
	 * @param clientFcmKey the clientFcmKey to set
	 */
	public void setClientFcmKey(String clientFcmKey) {
		this.clientFcmKey = clientFcmKey;
	}

	/**
	 * @return the isInstalled
	 */
	public Boolean getIsInstalled() {
		return isInstalled;
	}

	/**
	 * @param isInstalled the isInstalled to set
	 */
	public void setIsInstalled(Boolean isInstalled) {
		this.isInstalled = isInstalled;
	}

	public String getUserCustomName() {
		return userCustomName;
	}

	public void setUserCustomName(String userCustomName) {
		this.userCustomName = userCustomName;
	}

	public String getSdkAppVersion() {
		return sdkAppVersion;
	}

	public void setSdkAppVersion(String sdkAppVersion) {
		this.sdkAppVersion = sdkAppVersion;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPseudoId() {
		return pseudoId;
	}

	public void setPseudoId(String pseudoId) {
		this.pseudoId = pseudoId;
	}

	@Override
	public String toString() {
		return "DeviceInfo [id=" + id + ", apkDetail=" + apkDetail + ", creator=" + creator + ", modifier=" + modifier
				+ ", deviceId=" + deviceId + ", imsi=" + imsi + ", imei=" + imei + ", creationTime=" + creationTime
				+ ", modificationTime=" + modificationTime + ", fcmKey=" + fcmKey + ", make=" + make + ", model="
				+ model + ", deviceOS=" + deviceOS + ", appVersion=" + appVersion + ", licenseId=" + licenseId
				+ ", clientName=" + clientName + ", daysToExpiry=" + daysToExpiry + ", user=" + user + ", oldImei="
				+ oldImei + ", registrationDate=" + registrationDate + ", msisdn=" + msisdn + ", deregistrationDate="
				+ deregistrationDate + ", remark=" + remark + ", isRegistered=" + isRegistered + ", source=" + source
				+ ", clientFcmKey=" + clientFcmKey + ", isInstalled=" + isInstalled + ", userCustomName="
				+ userCustomName + ", sdkAppVersion=" + sdkAppVersion + ", appName=" + appName + ", pseudoId="
				+ pseudoId + "]";
	}

}
