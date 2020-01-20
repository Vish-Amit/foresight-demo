package com.inn.foresight.module.nv.workorder.wrapper;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.core.infra.wrapper.BuildingWrapper;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.RecipeWrapper;


/** The Class NVWorkorderWrapper. */
@JsonInclude(Include.NON_EMPTY)
@RestWrapper
public class NVWorkorderWrapper {
	
	/** The workorder PK. */
	private Integer id;

	/** The workorder id. */
	private String workorderId;

	/** The workorder name. */
	private String workorderName;

	/** The task id. */
	private Integer taskId;

	/** The status. */
	private String status;

	/** The operator name. */
	private String operatorName;
	
	/** The technology. */
	private String technology;

	/** The user list. */
	private List<NVWOUserWrapper> userList;
	
	/** The user list. */
	private List<Integer > deviceIdList;

	/** The assigned To. */
	private String assignedTo;

	/** The assigned By. */
	private String assignedBy;

	/** The creation date. */
	private Long creationDate;
	
	/** The start date. */
	private Long startDate;
	
	/** The end date. */
	private Long endDate;

	/** The modification date. */
	private Long modificationDate;
	
	/** The template type. */
	private TemplateType templateType;

	/** The due date. */
	private Long dueDate;

	/** The modification date. */
	private Double completionPercentage;
	
	/** The geo wo meta map. */
	private Map<String, String> geoWoMetaMap;
	
	/** The site id. */
	private String siteId;
	
	/** The site id. */
	private Integer earfcn;

	/** The site wrapper. */
	private NVSiteWrapper siteWrapper;
	
	/** The site wrapper. */
	private BuildingWrapper buildingWrapper;

	/** The recipe list. */
	private List<RecipeWrapper> recipeList;

	/** The geography type. */
	private String geographyType;

	/** The geography id. */
	private Integer geographyId;
	
	/** The geography id. */
	private Integer customGeographyId;
	
	/** The geography id. */
	private Integer reportInstanceId;
	
	private String woSource;
	
	private String description;
	
	private String remark;
	
	private Double latitude;
	
	private Double longitude;
	
	private Integer buildingId;
	
	private Integer unitId;
	
	private List<String> operatorList;	
	
	private List<String> recipeIdList;	
	
	private Map<Integer, Integer> pciAzimuthMap;	
	
	private Long startTime;
	
	private Long duration;
	
	private String bandType;
	
	private String mcc;
	
	private String mnc;
	
	private Map<String, String> metaData;
	
	private Integer stealthTaskId;
	
	private String stealthTaskStatus;
	
	private String acknowledgement;
	private String band;
	private String stealthWOFrequency;
	
	private Boolean logFileDownload;

	
	private Boolean isFloorPlanApproved;
	private String woMapType ;
	private String woRouteType ;
	
	
	private String userName;
	
	private List<Integer>unitIdList;
	
	private String testType;
	

	/** The Quick WorkorderId id. */
	private String quickWorkorderId;
	private Integer woRecipeMappingId;



	private String isSiteAcceptance;

	private Boolean ibOldWorkorderStatus;
	
	public Boolean getIbOldWorkorderStatus() {
		return ibOldWorkorderStatus;
	}

	public void setIbOldWorkorderStatus(Boolean ibOldWorkorderStatus) {
		this.ibOldWorkorderStatus = ibOldWorkorderStatus;
	}

	public String getIsSiteAcceptance() {
		return isSiteAcceptance;
	}

	public void setIsSiteAcceptance(String isSiteAcceptance) {
		this.isSiteAcceptance = isSiteAcceptance;
	}

	/**
	 * @return the woRecipeMappingId
	 */
	public Integer getWoRecipeMappingId() {
		return woRecipeMappingId;
	}

	/**
	 * @param woRecipeMappingId the woRecipeMappingId to set
	 */
	public void setWoRecipeMappingId(Integer woRecipeMappingId) {
		this.woRecipeMappingId = woRecipeMappingId;
	}

	/**
	 * @return the quickWorkorderId
	 */
	public String getQuickWorkorderId() {
		return quickWorkorderId;
	}

	/**
	 * @param quickWorkorderId the quickWorkorderId to set
	 */
	public void setQuickWorkorderId(String quickWorkorderId) {
		this.quickWorkorderId = quickWorkorderId;
	}


	private Integer walkTestReportInstanceId;
		
	private List<String> statusList;
	
	private List<String> template;

	public Boolean getLogFileDownload() {
		return logFileDownload;
	}

	public void setLogFileDownload(Boolean logFileDownload) {
		this.logFileDownload = logFileDownload;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the workorder id.
	 *
	 * @return the workorder id
	 */
	public String getWorkorderId() {
		return workorderId;
	}

	/**
	 * Sets the workorder id.
	 *
	 * @param workorderId the new workorder id
	 */
	public void setWorkorderId(String workorderId) {
		this.workorderId = workorderId;
	}

	/**
	 * Gets the workorder name.
	 *
	 * @return the workorder name
	 */
	public String getWorkorderName() {
		return workorderName;
	}

	/**
	 * Sets the workorder name.
	 *
	 * @param workorderName the new workorder name
	 */
	public void setWorkorderName(String workorderName) {
		this.workorderName = workorderName;
	}

	/**
	 * Gets the task id.
	 *
	 * @return the task id
	 */
	public Integer getTaskId() {
		return taskId;
	}

	/**
	 * Sets the task id.
	 *
	 * @param taskId the new task id
	 */
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
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
	 * Gets the operator name.
	 *
	 * @return the operator name
	 */
	public String getOperatorName() {
		return operatorName;
	}

	/**
	 * Sets the operator name.
	 *
	 * @param operatorName the new operator name
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	/**
	 * Gets the user list.
	 *
	 * @return the user list
	 */
	public List<NVWOUserWrapper> getUserList() {
		return userList;
	}

	/**
	 * Sets the user list.
	 *
	 * @param userList the new user list
	 */
	public void setUserList(List<NVWOUserWrapper> userList) {
		this.userList = userList;
	}

	/**
	 * Gets the creation date.
	 *
	 * @return the creation date
	 */
	public Long getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets the creation date.
	 *
	 * @param creationDate the new creation date
	 */
	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Gets the modification date.
	 *
	 * @return the modification date
	 */
	public Long getModificationDate() {
		return modificationDate;
	}

	/**
	 * Sets the modification date.
	 *
	 * @param modificationDate the new modification date
	 */
	public void setModificationDate(Long modificationDate) {
		this.modificationDate = modificationDate;
	}

	/**
	 * Gets the site id.
	 *
	 * @return the site id
	 */
	public String getSiteId() {
		return siteId;
	}

	/**
	 * Sets the site id.
	 *
	 * @param siteId the new site id
	 */
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	/**
	 * Gets the template type.
	 *
	 * @return the template type
	 */
	public TemplateType getTemplateType() {
		return templateType;
	}

	/**
	 * Sets the template type.
	 *
	 * @param templateType the new template type
	 */
	public void setTemplateType(TemplateType templateType) {
		this.templateType = templateType;
	}

	/**
	 * Gets the due date.
	 *
	 * @return the due date
	 */
	public Long getDueDate() {
		return dueDate;
	}

	/**
	 * Sets the due date.
	 *
	 * @param dueDate the new due date
	 */
	public void setDueDate(Long dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * Gets the geo wo meta map.
	 *
	 * @return the geo wo meta map
	 */
	public Map<String, String> getGeoWoMetaMap() {
		return geoWoMetaMap;
	}

	/**
	 * Sets the geo wo meta map.
	 *
	 * @param geoWoMetaMap the geo wo meta map
	 */
	public void setGeoWoMetaMap(Map<String, String> geoWoMetaMap) {
		this.geoWoMetaMap = geoWoMetaMap;
	}

	/**
	 * Gets the site wrapper.
	 *
	 * @return the site wrapper
	 */
	public NVSiteWrapper getSiteWrapper() {
		return siteWrapper;
	}

	/**
	 * Sets the site wrapper.
	 *
	 * @param siteWrapper the new site wrapper
	 */
	public void setSiteWrapper(NVSiteWrapper siteWrapper) {
		this.siteWrapper = siteWrapper;
	}

	/**
	 * Gets the recipe list.
	 *
	 * @return the recipe list
	 */
	public List<RecipeWrapper> getRecipeList() {
		return recipeList;
	}

	/**
	 * Sets the recipe list.
	 *
	 * @param recipeList the new recipe list
	 */
	public void setRecipeList(List<RecipeWrapper> recipeList) {
		this.recipeList = recipeList;
	}

	/**
	 * Gets the geography type.
	 *
	 * @return the geography type
	 */
	public String getGeographyType() {
		return geographyType;
	}

	/**
	 * Sets the geography type.
	 *
	 * @param geographyType the new geography type
	 */
	public void setGeographyType(String geographyType) {
		this.geographyType = geographyType;
	}

	/**
	 * Gets the geography id.
	 *
	 * @return the geography id
	 */
	public Integer getGeographyId() {
		return geographyId;
	}

	/**
	 * Sets the geography id.
	 *
	 * @param geographyId the new geography id
	 */
	public void setGeographyId(Integer geographyId) {
		this.geographyId = geographyId;
	}

	/**
	 * Gets the wo source.
	 *
	 * @return the wo source
	 */
	public String getWoSource() {
		return woSource;
	}

	/**
	 * Sets the wo source.
	 *
	 * @param woSource the new wo source
	 */
	public void setWoSource(String woSource) {
		this.woSource = woSource;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	public String getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(String assignedBy) {
		this.assignedBy = assignedBy;
	}
	
	public Double getCompletionPercentage() {
		return completionPercentage;
	}

	public void setCompletionPercentage(Double completionPercentage) {
		this.completionPercentage = completionPercentage;
	}
	
	public Long getStartDate() {
		return startDate;
	}

	public void setStartDate(Long startDate) {
		this.startDate = startDate;
	}

	public Long getEndDate() {
		return endDate;
	}

	public void setEndDate(Long endDate) {
		this.endDate = endDate;
	}

	public Integer getCustomGeographyId() {
		return customGeographyId;
	}

	public void setCustomGeographyId(Integer customGeographyId) {
		this.customGeographyId = customGeographyId;
	}

	public Integer getEarfcn() {
		return earfcn;
	}

	public void setEarfcn(Integer earfcn) {
		this.earfcn = earfcn;
	}

	public BuildingWrapper getBuildingWrapper() {
		return buildingWrapper;
	}

	public void setBuildingWrapper(BuildingWrapper buildingWrapper) {
		this.buildingWrapper = buildingWrapper;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public List<String> getOperatorList() {
		return operatorList;
	}

	public void setOperatorList(List<String> operatorList) {
		this.operatorList = operatorList;
	}

	public Integer getReportInstanceId() {
		return reportInstanceId;
	}

	public void setReportInstanceId(Integer reportInstanceId) {
		this.reportInstanceId = reportInstanceId;
	}

	public Map<Integer, Integer> getPciAzimuthMap() {
		return pciAzimuthMap;
	}

	public void setPciAzimuthMap(Map<Integer, Integer> pciAzimuthMap) {
		this.pciAzimuthMap = pciAzimuthMap;
	}

	public Integer getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Integer buildingId) {
		this.buildingId = buildingId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public List<Integer> getDeviceIdList() {
		return deviceIdList;
	}

	public void setDeviceIdList(List<Integer> deviceIdList) {
		this.deviceIdList = deviceIdList;
	}

	public List<String> getRecipeIdList() {
		return recipeIdList;
	}

	public void setRecipeIdList(List<String> recipeIdList) {
		this.recipeIdList = recipeIdList;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBandType() {
		return bandType;
	}

	public void setBandType(String bandType) {
		this.bandType = bandType;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMnc() {
		return mnc;
	}

	public void setMnc(String mnc) {
		this.mnc = mnc;
	}

	public Map<String, String> getMetaData() {
		return metaData;
	}

	public void setMetaData(Map<String, String> metaData) {
		this.metaData = metaData;
	}

	
	
	public String getStealthTaskStatus() {
		return stealthTaskStatus;
	}

	public void setStealthTaskStatus(String stealthTaskStatus) {
		this.stealthTaskStatus = stealthTaskStatus;
	}

	public Integer getStealthTaskId() {
		return stealthTaskId;
	}

	public void setStealthTaskId(Integer stealthTaskId) {
		this.stealthTaskId = stealthTaskId;
	}

	/**
	 * @return the acknowledgement
	 */
	public String getAcknowledgement() {
		return acknowledgement;
	}

	/**
	 * @param acknowledgement the acknowledgement to set
	 */
	public void setAcknowledgement(String acknowledgement) {
		this.acknowledgement = acknowledgement;
	}
	/**
	 * @return the stealthWOFrequency
	 */
	public String getStealthWOFrequency() {
		return stealthWOFrequency;
	}

	/**
	 * @param stealthWOFrequency the stealthWOFrequency to set
	 */
	public void setStealthWOFrequency(String stealthWOFrequency) {
		this.stealthWOFrequency = stealthWOFrequency;
	}

	public Boolean getIsFloorPlanApproved() {
		return isFloorPlanApproved;
	}

	public void setIsFloorPlanApproved(Boolean isFloorPlanApproved) {
		this.isFloorPlanApproved = isFloorPlanApproved;
	}

	
	public String getWoMapType() {
		return woMapType;
	}

	public void setWoMapType(String woMapType) {
		this.woMapType = woMapType;
	}
	
	
	
	public String getWoRouteType() {
		return woRouteType;
	}

	public void setWoRouteType(String woRouteType) {
		this.woRouteType = woRouteType;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<Integer> getUnitIdList() {
		return unitIdList;
	}
	public void setUnitIdList(List<Integer> unitIdList) {
		this.unitIdList = unitIdList;
	}
	
	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}
	
	public Integer getWalkTestReportInstanceId() {
		return walkTestReportInstanceId;
	}

	public void setWalkTestReportInstanceId(Integer walkTestReportInstanceId) {
		this.walkTestReportInstanceId = walkTestReportInstanceId;
	}
	
	public List<String> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<String> statusList) {
		this.statusList = statusList;
	}

	public List<String> getTemplate() {
		return template;
	}

	public void setTemplate(List<String> template) {
		this.template = template;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "NVWorkorderWrapper [id=" + id + ", workorderId=" + workorderId + ", workorderName=" + workorderName
				+ ", taskId=" + taskId + ", status=" + status + ", operatorName=" + operatorName + ", technology="
				+ technology + ", userList=" + userList + ", deviceIdList=" + deviceIdList + ", assignedTo="
				+ assignedTo + ", assignedBy=" + assignedBy + ", creationDate=" + creationDate + ", startDate="
				+ startDate + ", endDate=" + endDate + ", modificationDate=" + modificationDate + ", templateType="
				+ templateType + ", dueDate=" + dueDate + ", completionPercentage=" + completionPercentage
				+ ", geoWoMetaMap=" + geoWoMetaMap + ", siteId=" + siteId + ", earfcn=" + earfcn + ", siteWrapper="
				+ siteWrapper + ", buildingWrapper=" + buildingWrapper + ", recipeList=" + recipeList
				+ ", geographyType=" + geographyType + ", geographyId=" + geographyId + ", customGeographyId="
				+ customGeographyId + ", reportInstanceId=" + reportInstanceId + ", woSource=" + woSource
				+ ", description=" + description + ", remark=" + remark + ", latitude=" + latitude + ", longitude="
				+ longitude + ", buildingId=" + buildingId + ", unitId=" + unitId + ", operatorList=" + operatorList
				+ ", recipeIdList=" + recipeIdList + ", pciAzimuthMap=" + pciAzimuthMap + ", startTime=" + startTime
				+ ", duration=" + duration + ", bandType=" + bandType + ", mcc=" + mcc + ", mnc=" + mnc + ", metaData="
				+ metaData + ", stealthTaskId=" + stealthTaskId + ", stealthTaskStatus=" + stealthTaskStatus
				+ ", acknowledgement=" + acknowledgement + ", band=" + band + ", stealthWOFrequency="
				+ stealthWOFrequency + ", logFileDownload=" + logFileDownload + ", isFloorPlanApproved="
				+ isFloorPlanApproved + ", woMapType=" + woMapType + ", userName=" + userName + ", unitIdList="
				+ unitIdList + ", testType=" + testType + ", quickWorkorderId=" + quickWorkorderId
				+ ", woRecipeMappingId=" + woRecipeMappingId + ", walkTestReportInstanceId=" + walkTestReportInstanceId
				+ ", statusList=" + statusList + ", template=" + template + "]";
	}

	
}