package com.inn.foresight.module.nv.device.wrapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.foresight.module.nv.device.model.Device;
import com.inn.foresight.module.nv.profile.wrapper.NVProfileRequestWrapper;

/** The Class NVDeviceDataWrapper. */
@JpaWrapper
@RestWrapper
public class NVDeviceDataWrapper {

	/** The count. */
	private Long count;

	/** The geographyId. */
	private Integer geographyId;

	/** The average TTL. */
	private Double avgTTL;

	/** The average TDNS. */
	private Double avgTDNS;

	/** The average TTFB. */
	private Double avgTTFB;

	/** The average FDNS. */
	private Double avgFDNS;

	/** The modification time. */
	private Date modificationTime;

	/** The geography L 1. */
	private String geographyL1;

	/** The geography L 2. */
	private String geographyL2;

	/** The geography L 3. */
	private String geographyL3;

	/** The geography L 4. */
	private String geographyL4;

	/** The address. */
	private String address;

	/** The group id. */
	private String groupId;

	/** The latitude. */
	private Double latitude;

	/** The longitude. */
	private Double longitude;

	/** The id. */
	private Integer id;

	/** The device id. */
	private String deviceId;

	/** The make. */
	private String make;

	/** The model. */
	private String model;

	/** The imei. */
	private String imei;

	/** The imsi. */
	private String imsi;

	/** The device os. */
	private String deviceOs;

	/** The app version. */
	private String appVersion;

	/** The last modified time. */
	private Long lastModifiedTime;

	/** The geography name. */
	private String geographyName;

	/** The operator. */
	private String operator;

	/** The module. */
	private String module;

	/** The specification. */
	private Device specification;

	/** The total iteration count. */
	private Integer totalIterationCount;

	/** The battery level. */
	private Double batteryLevel;

	/** The location type. */
	private String locationType;

	/** The location info. */
	private String locationInfo;

	/** The msisdn. */
	private String msisdn;

	/** The lastrechargedate. */
	private Long lastRechargeDate;

	/** The rechargeplan. */
	private String rechargePlan;

	/** The validity. */
	private String validity;

	/** The nextrechargedate. */
	private Long nextRechargeDate;

	/** The MCC. */
	private Integer mcc;

	/** The MNC. */
	private Integer mnc;

	/** Device Type. */
	private String deviceType;

	/** The completed iteration count. */
	private Integer completedIterationCount;

	/** The remark. */
	private String remark;

	/** The technology. */
	private String technology;

	/** The start time. */
	private Date startTime;

	/** The end time. */
	private Date endTime;

	/** The source. */
	private String source;

	/** The user type. */
	private String userType;

	private String acknowledgement;

	private Boolean isAssigned;

	private Integer taskId;

	private String status;

	private Integer pushNotificationId;

	private String locationReason;
	private String operatorName;
	private Integer cellId;
	private Integer pci;
	private Integer tac;
	private Integer lac;
	private Integer psc;
	/** Signal Parsm. */
	private Integer rxLevel;
	private Integer rxquality;
	private Integer rsrp;
	private Integer rsrq;
	private Integer rssi;
	private Double sinr;
	private Integer rscp;
	private Integer ecNo;
	private Integer ecIo;

	private String connectionType;
	private String band;
	private String networkType;
	private String networkSubtype;
	private String firstName;
	private String lastName;
	private String logFileDownload;
	
	private String userCustomName;


	public String getUserCustomName() {
		return userCustomName;
	}

	public void setUserCustomName(String userCustomName) {
		this.userCustomName = userCustomName;
	}

	private List<Integer> fileList=new ArrayList<>();


	public List<Integer> getFileList() {
		return fileList;
	}

	public void setFileList(List<Integer> fileList) {
		this.fileList = fileList;
	}



	public String getLogFileDownload() {
		return logFileDownload;
	}

	public void setLogFileDownload(String logFileDownload) {
		this.logFileDownload = logFileDownload;
	}

	private List<NVDeviceDataWrapper>nvDevices;
	
	private Integer woRecipeMappingId;

	
	public Integer getWoRecipeMappingId() {
		return woRecipeMappingId;
	}

	public void setWoRecipeMappingId(Integer woRecipeMappingId) {
		this.woRecipeMappingId = woRecipeMappingId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/** The modification time. */
	private Date creationTime;

	private Boolean isInstalled;

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Gets the mcc.
	 *
	 * @return the mcc
	 */
	public Integer getMcc() {
		return mcc;
	}

	/**
	 * Sets the mcc.
	 *
	 * @param mcc
	 *            the new mcc
	 */
	public void setMcc(Integer mcc) {
		this.mcc = mcc;
	}

	/**
	 * Gets the mnc.
	 *
	 * @return the mnc
	 */
	public Integer getMnc() {
		return mnc;
	}

	/**
	 * Sets the mnc.
	 *
	 * @param mnc
	 *            the new mnc
	 */
	public void setMnc(Integer mnc) {
		this.mnc = mnc;
	}

	/**
	 * Gets the battery level.
	 *
	 * @return the battery level
	 */
	public Double getBatteryLevel() {
		return batteryLevel;
	}

	/**
	 * Sets the battery level.
	 *
	 * @param batteryLevel
	 *            the new battery level
	 */
	public void setBatteryLevel(Double batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	/** Instantiates a new NV device data wrapper. */
	public NVDeviceDataWrapper() {

	}

	public String getAcknowledgement() {
		return acknowledgement;
	}

	public void setAcknowledgement(String acknowledgement) {
		this.acknowledgement = acknowledgement;
	}

	public Boolean getIsAssigned() {
		return isAssigned;
	}

	public void setIsAssigned(Boolean isAssigned) {
		this.isAssigned = isAssigned;
	}

	/**
	 * Gets the technology.
	 *
	 * @return the technology
	 */
	public String getTechnology() {
		return technology;
	}

	/**
	 * Sets the technology.
	 *
	 * @param technology
	 *            the new technology
	 */
	public void setTechnology(String technology) {
		this.technology = technology;
	}

	/**
	 * Gets the end time.
	 *
	 * @return the end time
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * Sets the end time.
	 *
	 * @param endTime
	 *            the new end time
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * Gets the start time.
	 *
	 * @return the start time
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * Sets the start time.
	 *
	 * @param startTime
	 *            the new start time
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public NVDeviceDataWrapper( Double latitude, Double longitude,String locationReason,Integer cellId,Integer mcc,Integer mnc) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.locationReason=locationReason;
		this.cellId=cellId;
		this.mcc=mcc;
		this.mnc=mnc;
	}
	
	/**
	 * Instantiates a new NV device data wrapper.
	 *
	 * @param count
	 *            the count
	 * @param geographyName
	 *            the geography name
	 * @param latitude
	 *            the latitude
	 * @param longitude
	 *            the longitude
	 * @param geographyId
	 *            the geography id
	 */
	public NVDeviceDataWrapper(Long count, String geographyName, Double latitude, Double longitude,
			Integer geographyId) {
		super();
		this.count = count;
		this.geographyName = geographyName;
		this.latitude = latitude;
		this.longitude = longitude;
		this.geographyId = geographyId;
	}
	
	public NVDeviceDataWrapper(Long count,Double latitude, Double longitude,
			List<NVDeviceDataWrapper> nvDevices) {
		super();
		this.count = count;
		this.latitude = latitude;
		this.longitude = longitude;
		this.nvDevices = nvDevices;
	}

	/**
	 * Instantiates a new NV device data wrapper.
	 *
	 * @param deviceId
	 *            the device id
	 * @param make
	 *            the make
	 * @param model
	 *            the model
	 * @param imei
	 *            the imei
	 * @param deviceOs
	 *            the device os
	 * @param appVersion
	 *            the app version
	 * @param lastModifiedTime
	 *            the last modified time
	 * @param geographyName
	 *            the geography name
	 * @param operator
	 *            the operator
	 * @param module
	 *            the module
	 * @param imsi
	 *            the imsi
	 * @param source
	 *            the source
	 * @param isEnterprise
	 *            the is enterprise
	 */
	// Changes for new fields addition[source,isEnterprise]
	public NVDeviceDataWrapper(String deviceId, String make, String model, String imei, String deviceOs,
			String appVersion, Date lastModifiedTime, String geographyName, String operator, String module, String imsi,
			String source, Boolean isEnterprise) {
		super();
		this.deviceId = deviceId;
		this.make = make;
		this.model = model;
		this.imei = imei;
		this.imsi = imsi;
		this.deviceOs = deviceOs;
		this.appVersion = appVersion;
		this.lastModifiedTime = lastModifiedTime.getTime();
		this.geographyName = geographyName;
		this.operator = operator;
		this.module = module;
		this.source = source;
		this.userType = isEnterprise != null && isEnterprise ? DeviceConstant.ENTERPRISE_USER
				: DeviceConstant.CONSUMER_USER;
	}
	/** Changes for DeviceManager user name addition. */
	public NVDeviceDataWrapper(String deviceId, String make, String model, String imei, String deviceOs,
			String appVersion, Date lastModifiedTime, String geographyName, String operator, String module, String imsi,
			String source, Boolean isEnterprise,String firstName,String lastName) {
		super();
		this.deviceId = deviceId;
		this.make = make;
		this.model = model;
		this.imei = imei;
		this.imsi = imsi;
		this.deviceOs = deviceOs;
		this.appVersion = appVersion;
		this.lastModifiedTime = lastModifiedTime.getTime();
		this.geographyName = geographyName;
		this.operator = operator;
		this.module = module;
		this.source = source;
		this.userType = isEnterprise != null && isEnterprise ? DeviceConstant.ENTERPRISE_USER
				: DeviceConstant.CONSUMER_USER;
		this.firstName=firstName;
		this.lastName=lastName;
	}
	/** Changes for Get device for viewport. */
		public NVDeviceDataWrapper(String deviceId, String make, String model, String imei, String deviceOs,
				String appVersion, Date lastModifiedTime, String geographyName, String operator, String module, String imsi,
				String source, Boolean isEnterprise,Double latitude ,Double longitude) {
			super();
			this.deviceId = deviceId;
			this.make = make;
			this.model = model;
			this.imei = imei;
			this.imsi = imsi;
			this.deviceOs = deviceOs;
			this.appVersion = appVersion;
			this.lastModifiedTime = lastModifiedTime.getTime();
			this.geographyName = geographyName;
			this.operator = operator;
			this.module = module;
			this.source = source;
			this.userType = isEnterprise != null && isEnterprise ? DeviceConstant.ENTERPRISE_USER
					: DeviceConstant.CONSUMER_USER;
			this.latitude=latitude;
			this.longitude=longitude;
		}
	
	/**
	 * Instantiates a new NV device data wrapper.
	 *
	 * @param deviceId
	 *            the device id
	 * @param make
	 *            the make
	 * @param model
	 *            the model
	 * @param imei
	 *            the imei
	 * @param imsi
	 *            the imsi
	 * @param deviceOs
	 *            the device os
	 * @param appVersion
	 *            the app version
	 * @param modificationTime
	 *            the modification time
	 * @param geographyName
	 *            the geography name
	 * @param operator
	 *            the operator
	 * @param module
	 *            the module
	 * @param source
	 *            the source
	 * @param isEnterprise
	 *            the is enterprise
	 */

	public NVDeviceDataWrapper(String deviceId, String make, String model, String imei, String imsi, String deviceOs,
			String appVersion, Date modificationTime, String geographyName, String operator, String module,
			String source, Boolean isEnterprise) {

		super();
		this.deviceId = deviceId;
		this.make = make;
		this.model = model;
		this.imei = imei;
		this.imsi = imsi;
		this.deviceOs = deviceOs;
		this.appVersion = appVersion;
		this.modificationTime = modificationTime;
		this.geographyName = geographyName;
		this.operator = operator;
		this.module = module;
		this.source = source;
		this.userType = isEnterprise != null && isEnterprise ? DeviceConstant.ENTERPRISE_USER
				: DeviceConstant.CONSUMER_USER;

	}
	/** Get device by device id. */
	public NVDeviceDataWrapper(String deviceId, String make, String model, String imei, String imsi, String deviceOs,
			String appVersion, Date modificationTime, String geographyName, String operator, String module,
			String source, Boolean isEnterprise,Double latitude,Double longitude) {

		super();
		this.deviceId = deviceId;
		this.make = make;
		this.model = model;
		this.imei = imei;
		this.imsi = imsi;
		this.deviceOs = deviceOs;
		this.appVersion = appVersion;
		this.modificationTime = modificationTime;
		this.geographyName = geographyName;
		this.operator = operator;
		this.module = module;
		this.source = source;
		this.userType = isEnterprise != null && isEnterprise ? DeviceConstant.ENTERPRISE_USER
				: DeviceConstant.CONSUMER_USER;
		this.latitude=latitude;
		this.longitude=longitude;

	}

	/**
	 * Instantiates a new NV device data wrapper.
	 *
	 * @param id
	 *            the id
	 * @param deviceId
	 *            the device id
	 * @param make
	 *            the make
	 * @param model
	 *            the model
	 * @param imei
	 *            the imei
	 * @param deviceOs
	 *            the device os
	 * @param appVersion
	 *            the app version
	 * @param lastModifiedTime
	 *            the last modified time
	 * @param geographyName
	 *            the geography name
	 * @param operator
	 *            the operator
	 * @param module
	 *            the module
	 * @param imsi
	 *            the imsi
	 */
	public NVDeviceDataWrapper(Integer id, String deviceId, String make, String model, String imei, String deviceOs,
			String appVersion, Date lastModifiedTime, String geographyName, String operator, String module,
			String imsi) {
		super();
		this.id = id;
		this.deviceId = deviceId;
		this.make = make;
		this.model = model;
		this.imei = imei;
		this.imsi = imsi;
		this.deviceOs = deviceOs;
		this.appVersion = appVersion;
		this.lastModifiedTime = lastModifiedTime.getTime();
		this.geographyName = geographyName;
		this.operator = operator;
		this.module = module;
	}

	/**
	 * Instantiates a new NV device data wrapper.
	 *
	 * @param modificationTime
	 *            the modification time
	 * @param avgTTL
	 *            the avg TTL
	 * @param avgTDNS
	 *            the avg TDNS
	 * @param avgTTFB
	 *            the avg TTFB
	 * @param avgFDNS
	 *            the avg FDNS
	 * @param make
	 *            the make
	 * @param model
	 *            the model
	 * @param imei
	 *            the imei
	 * @param geographyL3
	 *            the geography L 3
	 * @param geographyL4
	 *            the geography L 4
	 * @param latitude
	 *            the latitude
	 * @param longitude
	 *            the longitude
	 * @param operator
	 *            the operator
	 * @param address
	 *            the address
	 * @param groupId
	 *            the group id
	 * @param deviceId
	 *            the device id
	 */
	public NVDeviceDataWrapper(Date modificationTime, Double avgTTL, Double avgTDNS, Double avgTTFB, Double avgFDNS,
			String make, String model, String imei, String geographyL3, String geographyL4, Double latitude,
			Double longitude, String operator, String address, String groupId, String deviceId) {
		super();
		this.modificationTime = modificationTime;
		this.avgTTL = avgTTL;
		this.avgTDNS = avgTDNS;
		this.avgTTFB = avgTTFB;
		this.avgFDNS = avgFDNS;
		this.geographyL3 = geographyL3;
		this.geographyL4 = geographyL4;
		this.address = address;
		this.groupId = groupId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.make = make;
		this.model = model;
		this.imei = imei;
		this.operator = operator;
		this.deviceId = deviceId;
	}

	/**
	 * Instantiates a new NV device data wrapper.
	 *
	 * @param id
	 *            the id
	 * @param startTime
	 *            the start time
	 * @param endTime
	 *            the end time
	 * @param operator
	 *            the operator
	 * @param technology
	 *            the technology
	 * @param totalIterationCount
	 *            the total iteration count
	 * @param completedIterationCount
	 *            the completed iteration count
	 * @param remark
	 *            the remark
	 */
	// daywise
	public NVDeviceDataWrapper(Integer id, Date startTime, Date endTime, String operator, String technology,
			Integer totalIterationCount, Integer completedIterationCount, String remark) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.operator = operator;
		this.technology = technology;
		this.totalIterationCount = totalIterationCount;
		this.completedIterationCount = completedIterationCount;
		this.remark = remark;

	}

	/**
	 * Instantiates a new NV device data wrapper.
	 *
	 * @param id
	 *            the id
	 * @param startTime
	 *            the start time
	 * @param endTime
	 *            the end time
	 * @param operator
	 *            the operator
	 * @param technology
	 *            the technology
	 * @param avgTTL
	 *            the avg TTL
	 * @param avgTTFB
	 *            the avg TTFB
	 * @param avgFDNS
	 *            the avg FDNS
	 * @param avgTDNS
	 *            the avg TDNS
	 */
	// daywise
	public NVDeviceDataWrapper(Integer id, Date startTime, Date endTime, String operator, String technology,
			Double avgTTL, Double avgTTFB, Double avgFDNS, Double avgTDNS) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
		this.operator = operator;
		this.technology = technology;
		this.avgTTL = avgTTL;
		this.avgTTFB = avgTTFB;
		this.avgFDNS = avgFDNS;
		this.avgTDNS = avgTDNS;

	}

	/**
	 * Instantiates a new NV device data wrapper.
	 *
	 * @param avgTTL
	 *            the avg TTL
	 * @param avgTDNS
	 *            the avg TDNS
	 * @param avgTTFB
	 *            the avg TTFB
	 * @param avgFDNS
	 *            the avg FDNS
	 * @param make
	 *            the make
	 * @param model
	 *            the model
	 * @param imei
	 *            the imei
	 * @param geographyL3
	 *            the geography L 3
	 * @param geographyL4
	 *            the geography L 4
	 * @param latitude
	 *            the latitude
	 * @param longitude
	 *            the longitude
	 * @param operator
	 *            the operator
	 * @param address
	 *            the address
	 * @param groupId
	 *            the group id
	 * @param deviceId
	 *            the device id
	 */
	public NVDeviceDataWrapper(Double avgTTL, Double avgTTFB, Double avgTDNS, Double avgFDNS, String make, String model,
			String imei, String geographyL3, String geographyL4, Double latitude, Double longitude, String operator,
			String address, String groupId, String deviceId) {
		super();
		this.avgTTL = avgTTL;
		this.avgTDNS = avgTDNS;
		this.avgTTFB = avgTTFB;
		this.avgFDNS = avgFDNS;
		this.geographyL3 = geographyL3;
		this.geographyL4 = geographyL4;
		this.address = address;
		this.groupId = groupId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.make = make;
		this.model = model;
		this.imei = imei;
		this.operator = operator;
		this.deviceId = deviceId;
	}

	/**
	 * Instantiates a new NV device data wrapper.
	 *
	 * @param make
	 *            the make
	 * @param model
	 *            the model
	 * @param imei
	 *            the imei
	 * @param geographyL3
	 *            the geography L 3
	 * @param geographyL4
	 *            the geography L 4
	 * @param latitude
	 *            the latitude
	 * @param longitude
	 *            the longitude
	 * @param address
	 *            the address
	 * @param groupId
	 *            the group id
	 * @param deviceId
	 *            the device id
	 */
	public NVDeviceDataWrapper(String make, String model, String imei, String geographyL3, String geographyL4,
			Double latitude, Double longitude, String address, String groupId, String deviceId) {
		super();
		this.geographyL3 = geographyL3;
		this.geographyL4 = geographyL4;
		this.address = address;
		this.groupId = groupId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.make = make;
		this.model = model;
		this.imei = imei;
		this.deviceId = deviceId;
	}

	/**
	 * Instantiates a new NV device data wrapper.
	 *
	 * @param deviceId
	 *            the device id
	 * @param make
	 *            the make
	 * @param model
	 *            the model
	 * @param imei
	 *            the imei
	 * @param imsi
	 *            the imsi
	 * @param deviceOs
	 *            the device os
	 * @param appVersion
	 *            the app version
	 * @param modificationTime
	 *            the modification time
	 * @param geographyName
	 *            the geography name
	 * @param operator
	 *            the operator
	 * @param module
	 *            the module
	 * @param batteryLevel
	 *            the battery level
	 * @param latitude
	 *            the latitude
	 * @param longitude
	 *            the longitude
	 * @param locationType
	 *            the location type
	 * @param locationInfo
	 *            the location info
	 */
	public NVDeviceDataWrapper(String deviceId, String make, String model, String imei, String imsi, String deviceOs,
			String appVersion, Date modificationTime, String geographyName, String operator, String module,
			Double batteryLevel, Double latitude, Double longitude, String locationType, String locationInfo) {

		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.deviceId = deviceId;
		this.make = make;
		this.model = model;
		this.imei = imei;
		this.imsi = imsi;
		this.deviceOs = deviceOs;
		this.appVersion = appVersion;
		this.modificationTime = modificationTime;
		this.geographyName = geographyName;
		this.operator = operator;
		this.module = module;
		this.batteryLevel = batteryLevel;
		this.locationType = locationType;
		this.locationInfo = locationInfo;

	}

	/**
	 * Instantiates a new NV device data wrapper.
	 *
	 * @param deviceId
	 *            the device id
	 * @param make
	 *            the make
	 * @param model
	 *            the model
	 * @param imei
	 *            the imei
	 * @param imsi
	 *            the imsi
	 * @param deviceOs
	 *            the device os
	 * @param appVersion
	 *            the app version
	 * @param modificationTime
	 *            the modification time
	 * @param geographyName
	 *            the geography name
	 * @param operator
	 *            the operator
	 * @param module
	 *            the module
	 * @param batteryLevel
	 *            the battery level
	 * @param latitude
	 *            the latitude
	 * @param longitude
	 *            the longitude
	 * @param locationType
	 *            the location type
	 * @param locationInfo
	 *            the location info
	 * @param lastRechargeDate
	 *            the last recharge date
	 * @param rechargePlan
	 *            the recharge plan
	 * @param validity
	 *            the validity
	 * @param nextRechargeDate
	 *            the next recharge date
	 * @param groupId
	 *            the group id
	 * @param msisdn
	 *            the msisdn
	 * @param deviceType
	 *            the device type
	 */
	public NVDeviceDataWrapper(String deviceId, String make, String model, String imei, String imsi, String deviceOs,
			String appVersion, Date modificationTime, String geographyName, String operator, String module,
			Double batteryLevel, Double latitude, Double longitude, String locationType, String locationInfo,
			Date lastRechargeDate, String rechargePlan, String validity, Date nextRechargeDate, String groupId,
			String msisdn, String deviceType) {

		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.deviceId = deviceId;
		this.make = make;
		this.model = model;
		this.imei = imei;
		this.imsi = imsi;
		this.deviceOs = deviceOs;
		this.appVersion = appVersion;
		this.modificationTime = modificationTime;
		this.geographyName = geographyName;
		this.operator = operator;
		this.module = module;
		this.batteryLevel = batteryLevel;
		this.locationType = locationType;
		this.locationInfo = locationInfo;
		this.lastRechargeDate = (lastRechargeDate != null ? lastRechargeDate.getTime() : null);
		this.rechargePlan = rechargePlan;
		this.validity = validity;
		this.nextRechargeDate = (nextRechargeDate != null ? nextRechargeDate.getTime() : null);
		this.groupId = groupId;
		this.msisdn = msisdn;
		this.deviceType = deviceType;
	}

	/**
	 * Instantiates a new NV device data wrapper.
	 *
	 * @param make
	 *            the make
	 * @param model
	 *            the model
	 * @param imei
	 *            the imei
	 * @param imsi
	 *            the imsi
	 * @param groupId
	 *            the group id
	 * @param address
	 *            the address
	 * @param geographyName
	 *            the geography name
	 * @param msisdn
	 *            the msisdn
	 * @param appVersion
	 *            the app version
	 * @param lastRechargeDate
	 *            the last recharge date
	 * @param rechargePlan
	 *            the recharge plan
	 * @param validity
	 *            the validity
	 * @param nextRechargeDate
	 *            the next recharge date
	 */
	public NVDeviceDataWrapper(String make, String model, String imei, String imsi, String groupId, String address,
			String geographyName, String msisdn, String appVersion, Date lastRechargeDate, String rechargePlan,
			String validity, Date nextRechargeDate) {
		super();
		this.address = address;
		this.groupId = groupId;
		this.make = make;
		this.model = model;
		this.imei = imei;
		this.imsi = imsi;
		this.appVersion = appVersion;
		this.geographyName = geographyName;
		this.msisdn = msisdn;
		this.lastRechargeDate = (lastRechargeDate != null ? lastRechargeDate.getTime() : null);
		this.rechargePlan = rechargePlan;
		this.validity = validity;
		this.nextRechargeDate = (nextRechargeDate != null ? nextRechargeDate.getTime() : null);
	}

	/**
	 * Instantiates a new NV device data wrapper.
	 *
	 * @param remark
	 *            the remark
	 * @param startTime
	 *            the start time
	 */
	public NVDeviceDataWrapper(String remark, Date startTime) {
		this.remark = remark;
		this.startTime = startTime;
	}

	/**
	 * Construct NVDeviceDataWrapper object.
	 *
	 * @param wrapper
	 *            the wrapper
	 */
	public NVDeviceDataWrapper(NVProfileRequestWrapper wrapper) {
		super();
		this.latitude = wrapper.getLatitude();
		this.longitude = wrapper.getLongitude();
		this.deviceId = wrapper.getDeviceId();
		this.make = wrapper.getMake();
		this.model = wrapper.getModel();
		this.deviceOs = wrapper.getOs();
		this.appVersion = wrapper.getAppVersion();
		this.module = wrapper.getModule();
		this.mcc = wrapper.getMcc();
		this.mnc = wrapper.getMnc();
		if("Apple".equalsIgnoreCase(this.make)) {
			this.deviceType="IOS";
		}else {
			this.deviceType="ANDROID";
		}
	}

	public NVDeviceDataWrapper(Integer id, String make, String model, String imei, String appVersion,
			Date lastModifiedTime, String geographyName) {
		super();
		this.id = id;
		this.make = make;
		this.model = model;
		this.imei = imei;
		this.appVersion = appVersion;
		this.lastModifiedTime = lastModifiedTime.getTime();
		this.geographyName = geographyName;
	}

	public NVDeviceDataWrapper(String deviceId, String make, String model, String imei, String imsi, String deviceOs,
			String appVersion, Date modificationTime, String geographyName, String operator, String module,

			String source, Boolean isEnterprise,String acknowledgement, Integer taskId, Date creationTime, String status) {
		super();
		this.deviceId = deviceId;
		this.make = make;
		this.model = model;
		this.imei = imei;
		this.imsi = imsi;
		this.deviceOs = deviceOs;
		this.appVersion = appVersion;
		this.modificationTime = modificationTime;
		this.geographyName = geographyName;
		this.operator = operator;
		this.module = module;
		this.source = source;
		this.userType = isEnterprise != null && isEnterprise ? DeviceConstant.ENTERPRISE_USER
				: DeviceConstant.CONSUMER_USER;
		this.acknowledgement = acknowledgement;
		this.taskId = taskId;
		this.creationTime = creationTime;
		this.status = status;

	}
	public NVDeviceDataWrapper(String deviceId, String make, String model, String imei, String imsi, String deviceOs,
			String appVersion, Date modificationTime, String geographyName, String operator, String module,

			String source, Boolean isEnterprise,String acknowledgement, Integer taskId, Date creationTime, String status, String remark) {
		super();
		this.deviceId = deviceId;
		this.make = make;
		this.model = model;
		this.imei = imei;
		this.imsi = imsi;
		this.deviceOs = deviceOs;
		this.appVersion = appVersion;
		this.modificationTime = modificationTime;
		this.geographyName = geographyName;
		this.operator = operator;
		this.module = module;
		this.source = source;
		this.userType = isEnterprise != null && isEnterprise ? DeviceConstant.ENTERPRISE_USER
				: DeviceConstant.CONSUMER_USER;
		this.acknowledgement = acknowledgement;
		this.taskId = taskId;
		this.creationTime = creationTime;
		this.status = status;
		this.remark = remark;
	}

	
	
	public NVDeviceDataWrapper(String deviceId, String make, String model, String imei, String imsi, String deviceOs,
			String appVersion, Date modificationTime, String operator, String module,

			String source, Boolean isEnterprise,String acknowledgement, Integer taskId, Date startTime, String status, String remark,Boolean isInstalled) {
		super();
		this.deviceId = deviceId;
		this.make = make;
		this.model = model;
		this.imei = imei;
		this.imsi = imsi;
		this.deviceOs = deviceOs;
		this.appVersion = appVersion;
		this.modificationTime = modificationTime;
		this.operator = operator;
		this.module = module;
		this.source = source;
		this.userType = isEnterprise != null && isEnterprise ? DeviceConstant.ENTERPRISE_USER
				: DeviceConstant.CONSUMER_USER;
		this.acknowledgement = acknowledgement;
		this.taskId = taskId;
		this.startTime = startTime; 
		this.status = status;
		this.remark = remark;
		this.isInstalled=isInstalled;

	}
	
	
	public NVDeviceDataWrapper(String deviceId, String make, String model, String imei, String imsi, String deviceOs,
			String appVersion, Date modificationTime, String operator, String module,

			String source, Boolean isEnterprise,String acknowledgement, Integer taskId, Date startTime, String status, String remark,Boolean isInstalled,Integer woRecipeMappingId,String userCustomName) {
		super();
		this.deviceId = deviceId;
		this.make = make;
		this.model = model;
		this.imei = imei;
		this.imsi = imsi;
		this.deviceOs = deviceOs;
		this.appVersion = appVersion;
		this.modificationTime = modificationTime;
		this.operator = operator;
		this.module = module;
		this.source = source;
		this.userType = isEnterprise != null && isEnterprise ? DeviceConstant.ENTERPRISE_USER
				: DeviceConstant.CONSUMER_USER;
		this.acknowledgement = acknowledgement;
		this.taskId = taskId;
		this.startTime = startTime; 
		this.status = status;
		this.remark = remark;
		this.isInstalled=isInstalled;
		this.woRecipeMappingId=woRecipeMappingId;
		this.userCustomName=userCustomName;
	}

	
	/**
	 * Gets the device id.
	 *
	 * @return the deviceId
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * Sets the device id.
	 *
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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
	 * @param make
	 *            the make to set
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
	 * @param model
	 *            the model to set
	 */
	public void setModel(String model) {
		this.model = model;
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
	 * @param imei
	 *            the imei to set
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * Gets the device os.
	 *
	 * @return the deviceOs
	 */
	public String getDeviceOs() {
		return deviceOs;
	}

	/**
	 * Sets the device os.
	 *
	 * @param deviceOs
	 *            the deviceOs to set
	 */
	public void setDeviceOs(String deviceOs) {
		this.deviceOs = deviceOs;
	}

	/**
	 * Gets the app version.
	 *
	 * @return the appVersion
	 */
	public String getAppVersion() {
		return appVersion;
	}

	/**
	 * Sets the app version.
	 *
	 * @param appVersion
	 *            the appVersion to set
	 */
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	/**
	 * Gets the last modified time.
	 *
	 * @return the lastModifiedTime
	 */
	public Long getLastModifiedTime() {
		return lastModifiedTime;
	}

	/**
	 * Sets the last modified time.
	 *
	 * @param lastModifiedTime
	 *            the lastModifiedTime to set
	 */
	public void setLastModifiedTime(Long lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
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
	 * @param modificationTime
	 *            the new modification time
	 */
	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * Sets the operator.
	 *
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * Gets the geography L 2.
	 *
	 * @return the geography L 2
	 */
	public String getGeographyL2() {
		return geographyL2;
	}

	/**
	 * Sets the geography L 2.
	 *
	 * @param geographyL2
	 *            the new geography L 2
	 */
	public void setGeographyL2(String geographyL2) {
		this.geographyL2 = geographyL2;
	}

	/**
	 * Gets the geography L 3.
	 *
	 * @return the geography L 3
	 */
	public String getGeographyL3() {
		return geographyL3;
	}

	/**
	 * Sets the geography L 3.
	 *
	 * @param geographyL3
	 *            the new geography L 3
	 */
	public void setGeographyL3(String geographyL3) {
		this.geographyL3 = geographyL3;
	}

	/**
	 * Gets the geography L 4.
	 *
	 * @return the geography L 4
	 */
	public String getGeographyL4() {
		return geographyL4;
	}

	/**
	 * Sets the geography L 4.
	 *
	 * @param geographyL4
	 *            the new geography L 4
	 */
	public void setGeographyL4(String geographyL4) {
		this.geographyL4 = geographyL4;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address
	 *            the new address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Gets the group id.
	 *
	 * @return the group id
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * Sets the group id.
	 *
	 * @param groupId
	 *            the new group id
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * Gets the location type.
	 *
	 * @return the location type
	 */
	public String getLocationType() {
		return locationType;
	}

	/**
	 * Sets the location type.
	 *
	 * @param locationType
	 *            the new location type
	 */
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	/**
	 * Gets the location info.
	 *
	 * @return the location info
	 */
	public String getLocationInfo() {
		return locationInfo;
	}

	/**
	 * Sets the location info.
	 *
	 * @param locationInfo
	 *            the new location info
	 */
	public void setLocationInfo(String locationInfo) {
		this.locationInfo = locationInfo;
	}

	/**
	 * Gets the module.
	 *
	 * @return the module
	 */
	public String getModule() {
		return module;
	}

	/**
	 * Sets the module.
	 *
	 * @param module
	 *            the module to set
	 */
	public void setModule(String module) {
		this.module = module;
	}

	/**
	 * Gets the specification.
	 *
	 * @return the specification
	 */
	public Device getSpecification() {
		return specification;
	}

	/**
	 * Sets the specification.
	 *
	 * @param specification
	 *            the specification to set
	 */
	public void setSpecification(Device specification) {
		this.specification = specification;
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
	 * @param id
	 *            the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the geography name.
	 *
	 * @return the geography name
	 */
	public String getGeographyName() {
		return geographyName;
	}

	/**
	 * Sets the geography name.
	 *
	 * @param geographyName
	 *            the new geography name
	 */
	public void setGeographyName(String geographyName) {
		this.geographyName = geographyName;
	}

	/**
	 * Sets the imsi.
	 *
	 * @param imsi
	 *            the imsi to set
	 */
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}

	/**
	 * Sets the count.
	 *
	 * @param count
	 *            the count to set
	 */
	public void setCount(Long count) {
		this.count = count;
	}

	/**
	 * Gets the geography id.
	 *
	 * @return the geographyId
	 */
	public Integer getGeographyId() {
		return geographyId;
	}

	/**
	 * Sets the geography id.
	 *
	 * @param geographyId
	 *            the geographyId to set
	 */
	public void setGeographyId(Integer geographyId) {
		this.geographyId = geographyId;
	}

	/**
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude.
	 *
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * Gets the geography L 1.
	 *
	 * @return the geography L 1
	 */
	public String getGeographyL1() {
		return geographyL1;
	}

	/**
	 * Sets the geography L 1.
	 *
	 * @param geographyL1
	 *            the new geography L 1
	 */
	public void setGeographyL1(String geographyL1) {
		this.geographyL1 = geographyL1;
	}

	/**
	 * Gets the avg TTL.
	 *
	 * @return the avg TTL
	 */
	public Double getAvgTTL() {
		return avgTTL;
	}

	/**
	 * Sets the avg TTL.
	 *
	 * @param avgTTL
	 *            the new avg TTL
	 */
	public void setAvgTTL(Double avgTTL) {
		this.avgTTL = avgTTL;
	}

	/**
	 * Gets the avg TDNS.
	 *
	 * @return the avg TDNS
	 */
	public Double getAvgTDNS() {
		return avgTDNS;
	}

	/**
	 * Sets the avg TDNS.
	 *
	 * @param avgTDNS
	 *            the new avg TDNS
	 */
	public void setAvgTDNS(Double avgTDNS) {
		this.avgTDNS = avgTDNS;
	}

	/**
	 * Gets the avg TTFB.
	 *
	 * @return the avg TTFB
	 */
	public Double getAvgTTFB() {
		return avgTTFB;
	}

	/**
	 * Sets the avg TTFB.
	 *
	 * @param avgTTFB
	 *            the new avg TTFB
	 */
	public void setAvgTTFB(Double avgTTFB) {
		this.avgTTFB = avgTTFB;
	}

	/**
	 * Gets the avg FDNS.
	 *
	 * @return the avg FDNS
	 */
	public Double getAvgFDNS() {
		return avgFDNS;
	}

	/**
	 * Sets the avg FDNS.
	 *
	 * @param avgFDNS
	 *            the new avg FDNS
	 */
	public void setAvgFDNS(Double avgFDNS) {
		this.avgFDNS = avgFDNS;
	}

	/**
	 * Gets the total iteration count.
	 *
	 * @return the total iteration count
	 */
	public Integer getTotalIterationCount() {
		return totalIterationCount;
	}

	/**
	 * Sets the total iteration count.
	 *
	 * @param totalIterationCount
	 *            the new total iteration count
	 */
	public void setTotalIterationCount(Integer totalIterationCount) {
		this.totalIterationCount = totalIterationCount;
	}

	/**
	 * Gets the completed iteration count.
	 *
	 * @return the completed iteration count
	 */
	public Integer getCompletedIterationCount() {
		return completedIterationCount;
	}

	/**
	 * Sets the completed iteration count.
	 *
	 * @param completedIterationCount
	 *            the new completed iteration count
	 */
	public void setCompletedIterationCount(Integer completedIterationCount) {
		this.completedIterationCount = completedIterationCount;
	}

	/**
	 * Gets the remark.
	 *
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * Sets the remark.
	 *
	 * @param remark
	 *            the new remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * Gets the msisdn.
	 *
	 * @return the msisdn
	 */
	public String getMsisdn() {
		return msisdn;
	}

	/**
	 * Sets the msisdn.
	 *
	 * @param msisdn
	 *            the new msisdn
	 */
	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	/**
	 * Gets the lastrechargedate.
	 *
	 * @return the lastrechargedate
	 */
	public Long getLastRechargeDate() {
		return lastRechargeDate;
	}

	/**
	 * Sets the lastrechargedate.
	 *
	 * @param lastrechargedate
	 *            the new lastrechargedate
	 */
	public void setLastRechargeDate(Long lastrechargedate) {
		this.lastRechargeDate = lastrechargedate;
	}

	/**
	 * Gets the rechargeplan.
	 *
	 * @return the rechargeplan
	 */
	public String getRechargePlan() {
		return rechargePlan;
	}

	/**
	 * Sets the rechargeplan.
	 *
	 * @param rechargeplan
	 *            the new rechargeplan
	 */
	public void setRechargePlan(String rechargeplan) {
		this.rechargePlan = rechargeplan;
	}

	/**
	 * Gets the validity.
	 *
	 * @return the validity
	 */
	public String getValidity() {
		return validity;
	}

	/**
	 * Sets the validity.
	 *
	 * @param validity
	 *            the new validity
	 */
	public void setValidity(String validity) {
		this.validity = validity;
	}

	/**
	 * Gets the nextrechargedate.
	 *
	 * @return the nextrechargedate
	 */
	public Long getNextRechargeDate() {
		return nextRechargeDate;
	}

	/**
	 * Sets the nextrechargedate.
	 *
	 * @param nextrechargedate
	 *            the new nextrechargedate
	 */
	public void setNextRechargeDate(Long nextrechargedate) {
		this.nextRechargeDate = nextrechargedate;
	}

	/**
	 * Gets the device type.
	 *
	 * @return the device type
	 */
	public String getDeviceType() {
		return deviceType;
	}

	/**
	 * Sets the device type.
	 *
	 * @param deviceType
	 *            the new device type
	 */
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Sets the source.
	 *
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * Gets the user type.
	 *
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * Sets the user type.
	 *
	 * @param userType
	 *            the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the pushNotificationId
	 */
	public Integer getPushNotificationId() {
		return pushNotificationId;
	}

	/**
	 * @param pushNotificationId
	 *            the pushNotificationId to set
	 */
	public void setPushNotificationId(Integer pushNotificationId) {
		this.pushNotificationId = pushNotificationId;
	}

	/**
	 * @return the locationReason
	 */
	public String getLocationReason() {
		return locationReason;
	}

	/**
	 * @param locationReason
	 *            the locationReason to set
	 */
	public void setLocationReason(String locationReason) {
		this.locationReason = locationReason;
	}

	/**
	 * @return the operatorName
	 */
	public String getOperatorName() {
		return operatorName;
	}

	/**
	 * @param operatorName
	 *            the operatorName to set
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	/**
	 * @return the cellId
	 */
	public Integer getCellId() {
		return cellId;
	}

	/**
	 * @param cellId
	 *            the cellId to set
	 */
	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}

	/**
	 * @return the pci
	 */
	public Integer getPci() {
		return pci;
	}

	/**
	 * @param pci
	 *            the pci to set
	 */
	public void setPci(Integer pci) {
		this.pci = pci;
	}

	/**
	 * @return the tac
	 */
	public Integer getTac() {
		return tac;
	}

	/**
	 * @param tac
	 *            the tac to set
	 */
	public void setTac(Integer tac) {
		this.tac = tac;
	}

	/**
	 * @return the lac
	 */
	public Integer getLac() {
		return lac;
	}

	/**
	 * @param lac
	 *            the lac to set
	 */
	public void setLac(Integer lac) {
		this.lac = lac;
	}

	/**
	 * @return the psc
	 */
	public Integer getPsc() {
		return psc;
	}

	/**
	 * @param psc
	 *            the psc to set
	 */
	public void setPsc(Integer psc) {
		this.psc = psc;
	}

	/**
	 * @return the rxLevel
	 */
	public Integer getRxLevel() {
		return rxLevel;
	}

	/**
	 * @param rxLevel
	 *            the rxLevel to set
	 */
	public void setRxLevel(Integer rxLevel) {
		this.rxLevel = rxLevel;
	}

	/**
	 * @return the rxquality
	 */
	public Integer getRxquality() {
		return rxquality;
	}

	/**
	 * @param rxquality
	 *            the rxquality to set
	 */
	public void setRxquality(Integer rxquality) {
		this.rxquality = rxquality;
	}

	/**
	 * @return the rsrp
	 */
	public Integer getRsrp() {
		return rsrp;
	}

	/**
	 * @param rsrp
	 *            the rsrp to set
	 */
	public void setRsrp(Integer rsrp) {
		this.rsrp = rsrp;
	}

	/**
	 * @return the rsrq
	 */
	public Integer getRsrq() {
		return rsrq;
	}

	/**
	 * @param rsrq
	 *            the rsrq to set
	 */
	public void setRsrq(Integer rsrq) {
		this.rsrq = rsrq;
	}

	/**
	 * @return the rssi
	 */
	public Integer getRssi() {
		return rssi;
	}

	/**
	 * @param rssi
	 *            the rssi to set
	 */
	public void setRssi(Integer rssi) {
		this.rssi = rssi;
	}

	/**
	 * @return the sinr
	 */
	public Double getSinr() {
		return sinr;
	}

	/**
	 * @param sinr
	 *            the sinr to set
	 */
	public void setSinr(Double sinr) {
		this.sinr = sinr;
	}

	/**
	 * @return the rscp
	 */
	public Integer getRscp() {
		return rscp;
	}

	/**
	 * @param rscp
	 *            the rscp to set
	 */
	public void setRscp(Integer rscp) {
		this.rscp = rscp;
	}

	/**
	 * @return the ecNo
	 */
	public Integer getEcNo() {
		return ecNo;
	}

	/**
	 * @param ecNo
	 *            the ecNo to set
	 */
	public void setEcNo(Integer ecNo) {
		this.ecNo = ecNo;
	}

	/**
	 * @return the ecIo
	 */
	public Integer getEcIo() {
		return ecIo;
	}

	/**
	 * @param ecIo
	 *            the ecIo to set
	 */
	public void setEcIo(Integer ecIo) {
		this.ecIo = ecIo;
	}

	/**
	 * @return the connectionType
	 */
	public String getConnectionType() {
		return connectionType;
	}

	/**
	 * @param connectionType the connectionType to set
	 */
	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	/**
	 * @return the band
	 */
	public String getBand() {
		return band;
	}

	/**
	 * @param band the band to set
	 */
	public void setBand(String band) {
		this.band = band;
	}

	/**
	 * @return the networkType
	 */
	public String getNetworkType() {
		return networkType;
	}

	/**
	 * @param networkType the networkType to set
	 */
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	/**
	 * @return the networkSubtype
	 */
	public String getNetworkSubtype() {
		return networkSubtype;
	}

	/**
	 * @param networkSubtype the networkSubtype to set
	 */
	public void setNetworkSubtype(String networkSubtype) {
		this.networkSubtype = networkSubtype;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	/**
	 * @return the nvDevices
	 */
	public List<NVDeviceDataWrapper> getNvDevices() {
		return nvDevices;
	}

	/**
	 * @param nvDevices the nvDevices to set
	 */
	public void setNvDevices(List<NVDeviceDataWrapper> nvDevices) {
		this.nvDevices = nvDevices;
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


	@Override
	public String toString() {
		return "NVDeviceDataWrapper [count=" + count + ", geographyId=" + geographyId + ", avgTTL=" + avgTTL
				+ ", avgTDNS=" + avgTDNS + ", avgTTFB=" + avgTTFB + ", avgFDNS=" + avgFDNS + ", modificationTime="
				+ modificationTime + ", geographyL1=" + geographyL1 + ", geographyL2=" + geographyL2 + ", geographyL3="
				+ geographyL3 + ", geographyL4=" + geographyL4 + ", address=" + address + ", groupId=" + groupId
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", id=" + id + ", deviceId=" + deviceId
				+ ", make=" + make + ", model=" + model + ", imei=" + imei + ", imsi=" + imsi + ", deviceOs=" + deviceOs
				+ ", appVersion=" + appVersion + ", lastModifiedTime=" + lastModifiedTime + ", geographyName="
				+ geographyName + ", operator=" + operator + ", module=" + module + ", specification=" + specification
				+ ", totalIterationCount=" + totalIterationCount + ", batteryLevel=" + batteryLevel + ", locationType="
				+ locationType + ", locationInfo=" + locationInfo + ", msisdn=" + msisdn + ", lastRechargeDate="
				+ lastRechargeDate + ", rechargePlan=" + rechargePlan + ", validity=" + validity + ", nextRechargeDate="
				+ nextRechargeDate + ", mcc=" + mcc + ", mnc=" + mnc + ", deviceType=" + deviceType
				+ ", completedIterationCount=" + completedIterationCount + ", remark=" + remark + ", technology="
				+ technology + ", startTime=" + startTime + ", endTime=" + endTime + ", source=" + source
				+ ", userType=" + userType + ", acknowledgement=" + acknowledgement + ", isAssigned=" + isAssigned
				+ ", taskId=" + taskId + ", status=" + status + ", pushNotificationId=" + pushNotificationId
				+ ", locationReason=" + locationReason + ", operatorName=" + operatorName + ", cellId=" + cellId
				+ ", pci=" + pci + ", tac=" + tac + ", lac=" + lac + ", psc=" + psc + ", rxLevel=" + rxLevel
				+ ", rxquality=" + rxquality + ", rsrp=" + rsrp + ", rsrq=" + rsrq + ", rssi=" + rssi + ", sinr=" + sinr
				+ ", rscp=" + rscp + ", ecNo=" + ecNo + ", ecIo=" + ecIo + ", connectionType=" + connectionType
				+ ", band=" + band + ", networkType=" + networkType + ", networkSubtype=" + networkSubtype
				+ ", firstName=" + firstName + ", lastName=" + lastName + ", logFileDownload=" + logFileDownload
				+ ", userCustomName=" + userCustomName + ", fileList=" + fileList + ", nvDevices=" + nvDevices
				+ ", woRecipeMappingId=" + woRecipeMappingId + ", creationTime=" + creationTime + ", isInstalled="
				+ isInstalled + "]";
	}
}
