package com.inn.foresight.module.nv.workorder.stealth.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;



/**
 * The Class StealthTaskResult.
 */
@NamedQueries({
	
	@NamedQuery(name="getStealthTaskResultByWOIdandTask",query="Select s from StealthTaskResult s where s.stealthTaskDetail.id=:stealthTaskDetail and s.stealthTaskDetail.genericWorkorder.id=:woId  and date(s.startTime)=date(:date)"),
	@NamedQuery(name="getStealthTaskResultByStealthTaskDetailId",query="select taskResult from StealthTaskResult taskResult where  taskResult.stealthTaskDetail.id = :stealthTaskDetailId and Date(taskResult.startTime) = Date(:startTime) order by taskResult.creationTime desc"),
	@NamedQuery(name = "getStealthTaskResultByWorkorderId", query = "SELECT new com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOWrapper(count(sr.id),sr.stealthTaskDetail.acknowledgement,case when sr.creationTime is null then DATE_FORMAT(sr.stealthTaskDetail.creationTime,'%d-%m-%Y') else DATE_FORMAT(sr.creationTime,'%d-%m-%Y') end ,sr.status) FROM StealthTaskResult sr RIGHT JOIN sr.stealthTaskDetail WHERE sr.stealthTaskDetail.genericWorkorder.id=:id  group by case when sr.creationTime is null then DATE_FORMAT(sr.stealthTaskDetail.creationTime,'%d-%m-%Y') else DATE_FORMAT(sr.creationTime,'%d-%m-%Y') end  ,sr.stealthTaskDetail.acknowledgement,sr.status"),
	@NamedQuery(name = "getStealthDeviceWrapperListByWOId", query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper(td.stealthTaskDetail.nvDeviceData.deviceInfo.deviceId,td.stealthTaskDetail.nvDeviceData.deviceInfo.make,td.stealthTaskDetail.nvDeviceData.deviceInfo.model,td.stealthTaskDetail.nvDeviceData.deviceInfo.imei,td.stealthTaskDetail.nvDeviceData.deviceInfo.imsi,td.stealthTaskDetail.nvDeviceData.deviceInfo.deviceOS,td.stealthTaskDetail.nvDeviceData.deviceInfo.appVersion,td.stealthTaskDetail.nvDeviceData.modificationTime,td.stealthTaskDetail.nvDeviceData.operator,td.stealthTaskDetail.nvDeviceData.module,td.stealthTaskDetail.nvDeviceData.source,td.stealthTaskDetail.nvDeviceData.isEnterprise,td.stealthTaskDetail.acknowledgement,td.stealthTaskDetail.id,td.startTime,td.status,td.remark,td.isInstalled,td.woRecipeMapping.id,td.stealthTaskDetail.nvDeviceData.deviceInfo.userCustomName) from StealthTaskResult td RIGHT JOIN td.stealthTaskDetail where td.stealthTaskDetail.genericWorkorder.id=:id order by td.startTime desc"), 
	
	@NamedQuery(name = "getAcknowledgementSummary",query = "select new com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOWrapper(count(taskDetail.acknowledgement), Date(taskDetail.modificationTime),taskDetail.acknowledgement,taskDetail.postLogin) from StealthTaskDetail taskDetail where taskDetail.genericWorkorder.id=:woId group by Date(taskDetail.modificationTime),taskDetail.acknowledgement,taskDetail.postLogin order by taskDetail.modificationTime asc"),
	@NamedQuery(name = "getStatusSummary",query = "select new com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOWrapper(count(taskResult.status), Date(taskResult.startTime),taskResult.status,taskResult.stealthTaskDetail.id,taskResult.isInstalled) from StealthTaskResult taskResult where taskResult.stealthTaskDetail.genericWorkorder.id=:woId group by Date(taskResult.startTime),taskResult.status "),
	@NamedQuery(name = "getStatusSummaryForReport",query = "select new com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOWrapper(Date(taskResult.startTime),taskResult.status,taskResult.stealthTaskDetail.id) from StealthTaskResult taskResult where taskResult.stealthTaskDetail.genericWorkorder.id=:woId and taskResult.stealthTaskDetail.id in (:taskIdList)"),
	
	@NamedQuery(name = "getHourlyAcknowledgementSummary",query = "select new com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOWrapper(count(taskDetail.acknowledgement),DATE_FORMAT(taskDetail.modificationTime,'%H-%d-%m-%y'),taskDetail.acknowledgement,taskDetail.postLogin) from StealthTaskDetail taskDetail where taskDetail.genericWorkorder.id=:woId group by DATE_FORMAT(taskDetail.modificationTime,'%H-%d-%m-%y'),taskDetail.acknowledgement,taskDetail.postLogin order by taskDetail.modificationTime asc"),
	@NamedQuery(name = "getHourlyStatusSummary",query = "select new com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOWrapper(count(taskResult.status), DATE_FORMAT(taskResult.startTime,'%H-%d-%m-%y'),taskResult.status,taskResult.stealthTaskDetail.id,taskResult.isInstalled) from StealthTaskResult taskResult where taskResult.stealthTaskDetail.genericWorkorder.id=:woId group by DATE_FORMAT(taskResult.startTime,'%H-%d-%m-%y'),taskResult.status"),
	@NamedQuery(name = "getStealthTaskResultForHourlyWO",query="Select s from StealthTaskResult s where s.stealthTaskDetail.id=:stealthTaskDetail and s.stealthTaskDetail.genericWorkorder.id=:woId  and date(s.startTime)=date(:date) and hour(s.startTime)=:hour"),
	@NamedQuery(name = "getStealthResultListByTaskId",query="select result from StealthTaskResult result where result.stealthTaskDetail.id = (:taskId)"),
	
	@NamedQuery(name = "getAssignedDeviceCount",query = "select new com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOWrapper(count(taskDetail),taskDetail.genericWorkorder.id) from StealthTaskDetail taskDetail where taskDetail.genericWorkorder.id=:woId and taskDetail.postLogin is false")
	
})
@FilterDefs({
	@FilterDef(name = "StealthHourFilter", parameters = {
			@ParamDef(name = "hour", type = "java.lang.Integer") }),
})

@Filters(value = {
	@Filter(name = "StealthHourFilter", condition = " hour(startTime)=:hour  ")
})


@Entity
@Table(name = "StealthTaskResult")
@XmlRootElement(name = "StealthTaskResult")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class StealthTaskResult implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1352669211684538084L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "stealthtaskresultid_pk")
	private Integer id;

	/** The start time. */
	@Column(name = "starttime")
	private Date startTime;

	/** The end time. */
	@Column(name = "endtime")
	private Date endTime;

	/** The creation time. */
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modification time. */
	@Column(name = "modificationtime")
	private Date modificationTime;

	/** The status. */
	@Column(name = "status")
	private String status;

	/** The remark. */
	@Column(name = "remark")
	private String remark;

	/** The stealth task detail. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "stealthtaskdetailid_fk", nullable = true)
	private StealthTaskDetail stealthTaskDetail;

	/** The wo recipe mapping. */
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "worecipemappingid_fk", nullable = true)
	private WORecipeMapping woRecipeMapping;

	/** The operator. */
	@Column(name = "operator")
	private String	operator;
	
	/** The technology. */
	@Column(name = "technology")
	private String	technology;
	
	/** The total iteration. */
	@Column(name = "totaliteration")
	private Integer	totalIteration;
	
	/** The completed iteration. */
	@Column(name = "completediteration")
	private Integer	completedIteration;
	
	/** The is installed. */
	@Column(name = "installed")
	private Boolean isInstalled=Boolean.TRUE;

	
	
	
	
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
	 * @param startTime the new start time
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
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
	 * @param endTime the new end time
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @param remark the new remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * Gets the stealth task detail.
	 *
	 * @return the stealth task detail
	 */
	public StealthTaskDetail getStealthTaskDetail() {
		return stealthTaskDetail;
	}

	/**
	 * Sets the stealth task detail.
	 *
	 * @param stealthTaskDetail the new stealth task detail
	 */
	public void setStealthTaskDetail(StealthTaskDetail stealthTaskDetail) {
		this.stealthTaskDetail = stealthTaskDetail;
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
	 * @param operator the new operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
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
	 * @param technology the new technology
	 */
	public void setTechnology(String technology) {
		this.technology = technology;
	}

	/**
	 * Gets the total iteration.
	 *
	 * @return the total iteration
	 */
	public Integer getTotalIteration() {
		return totalIteration;
	}

	/**
	 * Sets the total iteration.
	 *
	 * @param totalIteration the new total iteration
	 */
	public void setTotalIteration(Integer totalIteration) {
		this.totalIteration = totalIteration;
	}

	/**
	 * Gets the completed iteration.
	 *
	 * @return the completed iteration
	 */
	public Integer getCompletedIteration() {
		return completedIteration;
	}

	/**
	 * Sets the completed iteration.
	 *
	 * @param completedIteration the new completed iteration
	 */
	public void setCompletedIteration(Integer completedIteration) {
		this.completedIteration = completedIteration;
	}

	/**
	 * Gets the checks if is installed.
	 *
	 * @return the isInstalled
	 */
	public Boolean getIsInstalled() {
		return isInstalled;
	}

	/**
	 * Sets the checks if is installed.
	 *
	 * @param isInstalled the isInstalled to set
	 */
	public void setIsInstalled(Boolean isInstalled) {
		this.isInstalled = isInstalled;
	}

	/**
	 * Gets the wo recipe mapping.
	 *
	 * @return the wo recipe mapping
	 */
	public WORecipeMapping getWoRecipeMapping() {
		return woRecipeMapping;
	}

	/**
	 * Sets the wo recipe mapping.
	 *
	 * @param woRecipeMapping the new wo recipe mapping
	 */
	public void setWoRecipeMapping(WORecipeMapping woRecipeMapping) {
		this.woRecipeMapping = woRecipeMapping;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StealthTaskResult [id=" + id + ", startTime=" + startTime + ", endTime=" + endTime + ", creationTime="
				+ creationTime + ", modificationTime=" + modificationTime + ", status=" + status + ", remark=" + remark
				+ ", stealthTaskDetail=" + stealthTaskDetail + ", woRecipeMapping=" + woRecipeMapping + ", operator="
				+ operator + ", technology=" + technology + ", totalIteration=" + totalIteration
				+ ", completedIteration=" + completedIteration + ", isInstalled=" + isInstalled + "]";
	}

	

}
