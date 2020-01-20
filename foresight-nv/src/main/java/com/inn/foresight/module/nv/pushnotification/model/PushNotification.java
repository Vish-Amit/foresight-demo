package com.inn.foresight.module.nv.pushnotification.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.pushnotification.constants.PushNotificationConstants;
import com.inn.foresight.module.nv.pushnotification.notificationfile.model.NotificationFile;
import com.inn.product.um.user.model.User;

/** The Class PushNotification. */

@NamedQuery(name = PushNotificationConstants.GET_ALL_NOTIFICATION_BY_NOTIFICATIONFILE_COUNT, query = "select count(*) from PushNotification p where p.notificationFile=:notificationFilePlaceHolder")
@NamedQuery(name = PushNotificationConstants.GET_ALL_NEW_AND_FAILED_NOTIFICATION, query = "select p from PushNotification p where p.status='NEW'")
@NamedQuery(name = PushNotificationConstants.GET_ALL_NOTIFICATION_BY_NOTIFICATIONFILE, query = "select p from PushNotification p where p.notificationFile=:notificationFilePlaceHolder")



@Entity
@XmlRootElement(name = "PushNotification")
@Table(name = "PushNotification")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class PushNotification {

	/** The Enum Status. */
	public enum Status {
		/** The pending. */
		NEW,
		/** The completed. */
		SENT,
		/** The started. */
		FAILED
	}
	/** The Enum Status. */
	public enum OS {
		ANDROID,
		IOS,
	}

	/** The Enum ModuleName. */
	public enum ModuleName {
		INBUILDING_WORKFLOW("INBUILDINGWORKFLOW"),
		NV_SSVT_QUICK("SSVTQUICK"),
	    NV_SSVT_FULL("SSVTFULL"),
		/** The nv ssvt. */
		NV_SSVT("NVSSVT"),
		/** The nv live drive. */
		NV_LIVE_DRIVE("NVLIVEDRIVE"),
		/** The nv drive. */
		NV_DRIVE("NVDRIVE"),
		/** The nv clot. */
		NV_CLOT("NVCLOT"),

		/** The nv opendrive. */
		NV_OPENDRIVE("NVOPENDRIVE"),
		/** The nv stationary. */
		NV_STATIONARY("NVSTATIONARY"),
		/** The nv inbuilding. */
		NV_INBUILDING("NVINBUILDING"),
		/** The nv BRTI. */
		NV_BRTI("NVBRTI"),

		/** The nv feedback. */
		NV_FEEDBACK("NV_FEEDBACK"),
		
		NV_STEALTH("NV_STEALTH"),
		
		NV_CUSTOMER_CARE("NV_CUSTOMER_CARE"),
		
		PULSE("PULSE"),
		
		
		TRIBE("TRIBE"),
		
		SCANNING_RECEIVER("SCANNING_RECEIVER");

		/** The value. */
		private String value;

		/**
		 * Instantiates a new module name.
		 *
		 * @param value
		 *            the value
		 */
		private ModuleName(String value) {
			this.value = value;
		}

		/** Instantiates a new module name. */
		private ModuleName() {
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Sets the value.
		 *
		 * @param value
		 *            the new value
		 */
		void setValue(String value) {
			this.value = value;
		}
	}

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "pushnotificationid_pk")
	private Integer id;

	/** The scheduled Time on which notification should be sent. */
	@Column(name = "scheduledtime")
	private Date scheduledTime;

	/** The execution Time on which notification is sent/Processed. */
	@Column(name = "executiontime")
	private Date executionTime;

	/** The status. */
	@Enumerated(EnumType.STRING)
	private Status status = Status.NEW;

	
	
	/** Creation time of notification. */
	@Column(name = "creationtime")
	private Date creationTime;

	/** The created by. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creatorid_fk", nullable = true)
	private User createdBy;

	/** The Device Info. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "deviceinfoid_fk", nullable = true)
	private DeviceInfo deviceInfo;

	/** The Device Info. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "notificationfileid_fk", nullable = true)
	private NotificationFile notificationFile;

	/** The module name. */
	@Basic
	@Enumerated(javax.persistence.EnumType.STRING)
	@Column(name = "modulename")
	private ModuleName moduleName;

	/** The notification message. */
	@Basic
	@Column(name = "notificationmessage")
	private String notificationMessage;
	
	
	/** The notification message. */
	@Basic
	@Column(name = "notificationpayload")
	private String notificationPayLoad;

	/** The notification message. */
	@Basic
	@Column(name = "remark")
	private String remark;
	
	@Basic
    @Column(name="recieved")
	Boolean recieved;
	
	@Basic
	@Enumerated(javax.persistence.EnumType.STRING)
	@Column(name = "os")
	private OS os=OS.ANDROID;
	
    @Basic
	@Column (name="useclientfcm")
	Boolean useClientFcmKey=Boolean.FALSE;
    
    
	@Basic
	@Column(name = "livelocationdetail")
	private String liveLocationDetail;
	/**
	 * Gets the notification file.
	 *
	 * @return the notificationFile
	 */
	public NotificationFile getNotificationFile() {
		return notificationFile;
	}

	/**
	 * Sets the notification file.
	 *
	 * @param notificationFile
	 *            the notificationFile to set
	 */
	public void setNotificationFile(NotificationFile notificationFile) {
		this.notificationFile = notificationFile;
	}

	/**
	 * Gets the notification message.
	 *
	 * @return the notificationMessage
	 */
	public String getNotificationMessage() {
		return notificationMessage;
	}

	/**
	 * Sets the notification message.
	 *
	 * @param notificationMessage
	 *            the notificationMessage to set
	 */
	public void setNotificationMessage(String notificationMessage) {
		this.notificationMessage = notificationMessage;
	}

	/**
	 * Gets the device info.
	 *
	 * @return the deviceInfo
	 */
	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	/**
	 * Sets the device info.
	 *
	 * @param deviceInfo
	 *            the deviceInfo to set
	 */
	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
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
	 * Gets the scheduled time.
	 *
	 * @return the scheduled time
	 */
	public Date getScheduledTime() {
		return scheduledTime;
	}

	/**
	 * Sets the scheduled time.
	 *
	 * @param scheduledTime
	 *            the new scheduled time
	 */
	public void setScheduledTime(Date scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	/**
	 * Gets the execution time.
	 *
	 * @return the execution time
	 */
	public Date getExecutionTime() {
		return executionTime;
	}

	/**
	 * Sets the execution time.
	 *
	 * @param executionTime
	 *            the new execution time
	 */
	public void setExecutionTime(Date executionTime) {
		this.executionTime = executionTime;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status
	 *            the new status
	 */
	public void setStatus(Status status) {
		this.status = status;
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
	 * @param creationTime
	 *            the new creation time
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Gets the created by.
	 *
	 * @return the created by
	 */
	public User getCreatedBy() {
		return createdBy;
	}

	/**
	 * Sets the created by.
	 *
	 * @param createdBy
	 *            the new created by
	 */
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Gets the module name.
	 *
	 * @return the module name
	 */
	public ModuleName getModuleName() {
		return moduleName;
	}

	/**
	 * Sets the module name.
	 *
	 * @param moduleName
	 *            the new module name
	 */
	public void setModuleName(ModuleName moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * Gets the notification pay load.
	 *
	 * @return the notification pay load
	 */
	public String getNotificationPayLoad() {
		return notificationPayLoad;
	}
	
	/**
	 * Sets the notification pay load.
	 *
	 * @param notificationPayLoad the new notification pay load
	 */
	public void setNotificationPayLoad(String notificationPayLoad) {
		this.notificationPayLoad = notificationPayLoad;
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
	 * @return the recieved
	 */
	public Boolean isRecieved() {
		return recieved;
	}

	/**
	 * @param recieved the recieved to set
	 */
	public void setRecieved(Boolean recieved) {
		this.recieved = recieved;
	}

	/**
	 * @return the useClientFcmKey
	 */
	public Boolean getUseClientFcmKey() {
		return useClientFcmKey;
	}

	/**
	 * @param useClientFcmKey the useClientFcmKey to set
	 */
	public void setUseClientFcmKey(Boolean useClientFcmKey) {
		this.useClientFcmKey = useClientFcmKey;
	}

	/**
	 * @return the os
	 */
	public OS getOs() {
		return os;
	}

	/**
	 * @param os the os to set
	 */
	public void setOs(OS os) {
		this.os = os;
	}

	/**
	 * @return the liveLocationDetail
	 */
	public String getLiveLocationDetail() {
		return liveLocationDetail;
	}

	/**
	 * @param liveLocationDetail the liveLocationDetail to set
	 */
	public void setLiveLocationDetail(String liveLocationDetail) {
		this.liveLocationDetail = liveLocationDetail;
	}
	
}
