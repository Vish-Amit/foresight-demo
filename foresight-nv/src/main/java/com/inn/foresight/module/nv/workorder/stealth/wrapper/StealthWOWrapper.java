package com.inn.foresight.module.nv.workorder.stealth.wrapper;

import java.util.Date;
import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.foresight.module.nv.report.utils.StealthUtils;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;

@JpaWrapper
public class StealthWOWrapper { 
	
	private Long count;
	private String status;
	private Long time;

	
	private String date;

	private String acknowledgement;
	private String assinedTo;
	
	/** For hbase columns. */
	private Double latitude;
	private Double longitude;
	private String deviceId;
	private String cescore;
	private Integer workorderId;
	private Integer taskId;
	private Integer dateToInt;
	private Integer enodeBId;
	private List<Integer>taskList;
	private List<StealthWOWrapper>stealthWOList;
	private String geographyL2Name;
	private String geographyL3Name;
	private String geographyL4Name;
	private Long dateToTimestamp;
	private Boolean isInstalled;
	private String uniqueId;
	private Boolean postLogin;
	
	public StealthWOWrapper(Long count, String acknowledgement,String date,String status) {
		super();
		this.count = count;
		this.acknowledgement = acknowledgement;
		this.date=date;
		this.status=status;
	}
	
	public StealthWOWrapper(Long count, Date time,String status) {
		super();
		this.count = count;
		this.status = status;
		this.time=time.getTime();
	}
	
	public StealthWOWrapper(Long count, Date time,String status,Boolean postLogin) {
		super();
		this.count = count;
		this.status = status;
		this.time=time.getTime();
		this.postLogin=postLogin;
	}
	
	
	/** Hourly. */
	public StealthWOWrapper(Long count, String time,String status) {
		super();
		this.count = count;
		this.status = status;
		this.time=StealthUtils.getTimestampFromDate(time, StealthConstants.DATE_FORMAT_HOUR);
	}
	
	public StealthWOWrapper(Long count, String time,String status,Boolean postLogin) {
		super();
		this.count = count;
		this.status = status;
		this.time=StealthUtils.getTimestampFromDate(time, StealthConstants.DATE_FORMAT_HOUR);
		this.postLogin= postLogin;
	}
	
	
	public StealthWOWrapper(Long count, Integer workorderId) {
		super();
		this.count = count;
		this.workorderId=workorderId;
	}
	
	public StealthWOWrapper(Long count, Date time,String status, Integer taskId,Boolean isInstalled) {
		super();
		this.count = count;
		this.status = status;
		this.time=time.getTime();
		this.taskId = taskId;
		this.isInstalled=isInstalled;
	}
	/** Hourly. */
	public StealthWOWrapper(Long count, String time,String status, Integer taskId,Boolean isInstalled) {
		super();
		this.count = count;
		this.status = status;
		this.time=StealthUtils.getTimestampFromDate(time, StealthConstants.DATE_FORMAT_HOUR);
		this.taskId = taskId;
		this.isInstalled=isInstalled;
	}
	
	public StealthWOWrapper(Date time,String status, Integer taskId) {
		super();
		this.status = status;
		this.time=time.getTime();
		this.taskId = taskId;
	}
	
   /** Stealth map visualization 1. */
	public StealthWOWrapper(Long count, Double latKey, Double longKey,  List<Integer> taskList,String score,String gl4,String gl3,String gl2) {
		this.count=count;
		this.latitude=latKey;
		this.longitude=longKey;
		this.taskList=taskList;
		this.cescore=score;
		this.geographyL4Name=gl4;
		this.geographyL3Name=gl3;
		this.geographyL2Name=gl2;
	}
	/** Stealth map visualization 2. */
		public StealthWOWrapper(Long count, Double latKey, Double longKey,String score,String gl4,String gl3,String gl2, List<StealthWOWrapper> stealthWOList) {
			this.count=count;
			this.latitude=latKey;
			this.longitude=longKey;
			this.stealthWOList=stealthWOList;
			this.cescore=score;
			this.geographyL4Name=gl4;
			this.geographyL3Name=gl3;
		}
			
		
	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public StealthWOWrapper() {
		
	}

	/**
	 * @return the acknowledgement
	 */
	public String getAcknowledgement() {
		return acknowledgement;
	}

	/**
	 * @param acknowledgement
	 *            the acknowledgement to set
	 */
	public void setAcknowledgement(String acknowledgement) {
		this.acknowledgement = acknowledgement;
	}

	/**
	 * @return the assinedTo
	 */
	public String getAssinedTo() {
		return assinedTo;
	}

	/**
	 * @param assinedTo
	 *            the assinedTo to set
	 */
	public void setAssinedTo(String assinedTo) {
		this.assinedTo = assinedTo;
	}

	
	
	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
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
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
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
	 * @return the cescore
	 */
	public String getCescore() {
		return cescore;
	}

	/**
	 * @param cescore the cescore to set
	 */
	public void setCescore(String cescore) {
		this.cescore = cescore;
	}

	/**
	 * @return the workorderId
	 */
	public Integer getWorkorderId() {
		return workorderId;
	}

	/**
	 * @param workorderId the workorderId to set
	 */
	public void setWorkorderId(Integer workorderId) {
		this.workorderId = workorderId;
	}

	/**
	 * @return the taskId
	 */
	public Integer getTaskId() {
		return taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the enodeBId
	 */
	public Integer getEnodeBId() {
		return enodeBId;
	}

	/**
	 * @param enodeBId the enodeBId to set
	 */
	public void setEnodeBId(Integer enodeBId) {
		this.enodeBId = enodeBId;
	}

	/**
	 * @return the taskList
	 */
	public List<Integer> getTaskList() {
		return taskList;
	}

	/**
	 * @param taskList the taskList to set
	 */
	public void setTaskList(List<Integer> taskList) {
		this.taskList = taskList;
	}

	/**
	 * @return the geographyL2Name
	 */
	public String getGeographyL2Name() {
		return geographyL2Name;
	}

	/**
	 * @param geographyL2Name the geographyL2Name to set
	 */
	public void setGeographyL2Name(String geographyL2Name) {
		this.geographyL2Name = geographyL2Name;
	}

	/**
	 * @return the geographyL3Name
	 */
	public String getGeographyL3Name() {
		return geographyL3Name;
	}

	/**
	 * @param geographyL3Name the geographyL3Name to set
	 */
	public void setGeographyL3Name(String geographyL3Name) {
		this.geographyL3Name = geographyL3Name;
	}

	/**
	 * @return the geographyL4Name
	 */
	public String getGeographyL4Name() {
		return geographyL4Name;
	}

	/**
	 * @param geographyL4Name the geographyL4Name to set
	 */
	public void setGeographyL4Name(String geographyL4Name) {
		this.geographyL4Name = geographyL4Name;
	}
	

	public Integer getDateToInt() {
		return dateToInt;
	}

	public void setDateToInt(Integer dateToInt) {
		this.dateToInt = dateToInt;
	}

	/**
	 * @return the stealthWOList
	 */
	public List<StealthWOWrapper> getStealthWOList() {
		return stealthWOList;
	}

	/**
	 * @return the dateToTimestamp
	 */
	public Long getDateToTimestamp() {
		return dateToTimestamp;
	}

	/**
	 * @param dateToTimestamp the dateToTimestamp to set
	 */
	public void setDateToTimestamp(Long dateToTimestamp) {
		this.dateToTimestamp = dateToTimestamp;
	}

	/**
	 * @param stealthWOList the stealthWOList to set
	 */
	public void setStealthWOList(List<StealthWOWrapper> stealthWOList) {
		this.stealthWOList = stealthWOList;
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
	
	public Boolean getPostLogin() {
		return postLogin;
	}

	public void setPostLogin(Boolean postLogin) {
		this.postLogin = postLogin;
	}

	@Override
	public String toString() {
		return "StealthWOWrapper [count=" + count + ", status=" + status + ", time=" + time + ", date=" + date
				+ ", acknowledgement=" + acknowledgement + ", assinedTo=" + assinedTo + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", deviceId=" + deviceId + ", cescore=" + cescore + ", workorderId="
				+ workorderId + ", taskId=" + taskId + ", dateToInt=" + dateToInt + ", enodeBId=" + enodeBId
				+ ", taskList=" + taskList + ", stealthWOList=" + stealthWOList + ", geographyL2Name=" + geographyL2Name
				+ ", geographyL3Name=" + geographyL3Name + ", geographyL4Name=" + geographyL4Name + ", dateToTimestamp="
				+ dateToTimestamp + ", isInstalled=" + isInstalled + ", uniqueId=" + uniqueId + ", postLogin="
				+ postLogin + "]";
	}
	
}
