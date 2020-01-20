package com.inn.foresight.module.nv.workorder.recipe.wrapper;

import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.product.um.user.model.User;

/** The Class RecipeWrapper. */
@JsonInclude(Include.NON_NULL)
@RestWrapper
@JpaWrapper
public class RecipeWrapper {

	/** The id. */
	private Integer id;

	/** The recipe id. */
	private String recipeId;

	/** The name. */
	private String name;

	/** The type. */
	private String type;

	/** The status. */
	private String status;
	
	/** The description. */
	private String description;

	/** The category. */
	private String category;
	
	/** The creation time. */
	private Date creationTime;
	
	/** The modification time. */
	private Date modificationTime;

	/** The creator. */
	private User creator;

	/** The script json. */
	private JSONArray scriptJson;
	
	/** The wo recipe mapping id. */
	private Integer woRecipeMappingId;
	
	/** The azimuth. */
	private Integer azimuth;
	
	/** The pci. */
	private Integer pci;
	
	/** The Unit Id List. */
	private Integer unitId;

	private String firstName;

	private String lastName;
	
	private Boolean loop;
	
	private Integer iterationCount;

	private Long maxTime;

	private Long waitTime;
	
	private Integer customGeographyId;
	
	private String operator;
	
	private Boolean toUseFloorplan;
	
	private Integer rsrpThreshold;
	
	private Integer sinrThreshold;
	
	private Double dlThreshold;
	private Double ulThreshold;
	private Double jitterThreshold;
	private Double latencyThreshold;

	private Double browseThreshold;
	private Double dlThresholdCriteria;
	private Double ulThresholdCriteria;
	
	

	private Integer maxSINRThreshold;
	
	private Boolean isReportAvailable;
	
	private String wingName;
	private String floorName;
	private String unitName;
	private Boolean logFileDownload;
	
	private String recipeWappingId;

	private String remark;
	
	private String displayOperatorName;
	
	
	private String scannerRequest;
	
	private String channelList;
	
	private String channelSet;
	
	private String cellName;
	
	

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public Double getDlThreshold() {
		return dlThreshold;
	}

	public void setDlThreshold(Double dlThreshold) {
		this.dlThreshold = dlThreshold;
	}

	public Double getUlThreshold() {
		return ulThreshold;
	}

	public void setUlThreshold(Double ulThreshold) {
		this.ulThreshold = ulThreshold;
	}

	public Double getJitterThreshold() {
		return jitterThreshold;
	}

	public void setJitterThreshold(Double jitterThreshold) {
		this.jitterThreshold = jitterThreshold;
	}

	public Double getLatencyThreshold() {
		return latencyThreshold;
	}

	public void setLatencyThreshold(Double latencyThreshold) {
		this.latencyThreshold = latencyThreshold;
	}

	public Boolean getLogFileDownload() {
		return logFileDownload;
	}

	public void setLogFileDownload(Boolean logFileDownload) {
		this.logFileDownload = logFileDownload;
	}

	/**
	 * Instantiates a new recipe wrapper.
	 *
	 * @param id the id
	 * @param recipeId the recipe id
	 * @param name the name
	 * @param type the type
	 * @param status the status
	 * @param description the description
	 * @param category the category
	 * @param creationTime the creation time
	 * @param modificationTime the modification time
	 * @param creator the creator
	 * @param scriptJson the script json
	 * @param woRecipeMappingId the wo recipe mapping id
	 */
	public RecipeWrapper(Integer id, String recipeId, String name, String type, String status, String description,
			String category, JSONArray scriptJson,
			Integer woRecipeMappingId) {
		super();
		this.id = id;
		this.recipeId = recipeId;
		this.name = name;
		this.type = type;
		this.status = status;
		this.description = description;
		this.category = category;
		this.scriptJson = scriptJson;
		this.woRecipeMappingId = woRecipeMappingId;
	}
	
	/**
	 * Instantiates a new recipe wrapper.
	 *
	 * @param id the id
	 * @param recipeId the recipe id
	 * @param name the name
	 * @param type the type
	 * @param status the status
	 * @param description the description
	 * @param category the category
	 * @param creationTime the creation time
	 * @param modificationTime the modification time
	 * @param creator the creator
	 * @param scriptJson the script json
	 * @param woRecipeMappingId the wo recipe mapping id
	 * @param azimuth the azimuth
	 * @param pci the pci
	 */
	public RecipeWrapper(Integer id, String recipeId, String name, String type, String status, String description,
			String category, Date creationTime, Date modificationTime, User creator, JSONArray scriptJson,
			Integer woRecipeMappingId, Integer azimuth, Integer pci, Long waitTime, Long maxTime, 
			Integer iterationCount, Boolean loop, String firstName, String lastName, Boolean isReportAvailable) {
		super();
		this.id = id;
		this.recipeId = recipeId;
		this.name = name;
		this.type = type;
		this.status = status;
		this.description = description;
		this.category = category;
		this.creationTime = creationTime;
		this.modificationTime = modificationTime;
		this.creator = creator;
		this.scriptJson = scriptJson;
		this.woRecipeMappingId = woRecipeMappingId;
		this.azimuth = azimuth;
		this.pci = pci;
		this.waitTime = waitTime;
		this.maxTime = maxTime;
		this.iterationCount = iterationCount;
		this.loop = loop;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isReportAvailable = isReportAvailable;
	}

	

	public RecipeWrapper(Integer id, String recipeId, String name, String type, String description,
			String category, Date creationTime, Date modificationTime, String firstName, String lastName, String scriptJson) throws ParseException {
		super();
		this.id = id;
		this.recipeId = recipeId;
		this.name = name;
		this.type = type;
		this.status = status;
		this.description = description;
		this.category = category;
		this.creationTime = creationTime;
		this.modificationTime = modificationTime;
		this.firstName = firstName;
		this.lastName = lastName;
		this.scriptJson = (JSONArray) new JSONParser().parse(scriptJson);
	}

	/** Instantiates a new recipe wrapper. */
	public RecipeWrapper() {
		super();
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
	 * Gets the recipe id.
	 *
	 * @return the recipe id
	 */
	public String getRecipeId() {
		return recipeId;
	}

	/**
	 * Sets the recipe id.
	 *
	 * @param recipeId the new recipe id
	 */
	public void setRecipeId(String recipeId) {
		this.recipeId = recipeId;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
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
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
	public void setCategory(String category) {
		this.category = category;
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
	 * Gets the script json.
	 *
	 * @return the script json
	 */
	public JSONArray getScriptJson() {
		return scriptJson;
	}

	/**
	 * Sets the script json.
	 *
	 * @param scriptJson the new script json
	 */
	public void setScriptJson(JSONArray scriptJson) {
		this.scriptJson = scriptJson;
	}

	/**
	 * Gets the wo recipe mapping id.
	 *
	 * @return the wo recipe mapping id
	 */
	public Integer getWoRecipeMappingId() {
		return woRecipeMappingId;
	}

	/**
	 * Sets the wo recipe mapping id.
	 *
	 * @param woRecipeMappingId the new wo recipe mapping id
	 */
	public void setWoRecipeMappingId(Integer woRecipeMappingId) {
		this.woRecipeMappingId = woRecipeMappingId;
	}

	/**
	 * Gets the azimuth.
	 *
	 * @return the azimuth
	 */
	public Integer getAzimuth() {
		return azimuth;
	}


	/**
	 * Sets the azimuth.
	 *
	 * @param azimuth the new azimuth
	 */
	public void setAzimuth(Integer azimuth) {
		this.azimuth = azimuth;
	}


	/**
	 * Gets the pci.
	 *
	 * @return the pci
	 */
	public Integer getPci() {
		return pci;
	}


	/**
	 * Sets the pci.
	 *
	 * @param pci the new pci
	 */
	public void setPci(Integer pci) {
		this.pci = pci;
	}	

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Boolean getLoop() {
		return loop;
	}

	public void setLoop(Boolean loop) {
		this.loop = loop;
	}

	public Integer getIterationCount() {
		return iterationCount;
	}

	public void setIterationCount(Integer iterationCount) {
		this.iterationCount = iterationCount;
	}

	public Long getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(Long maxTime) {
		this.maxTime = maxTime;
	}

	public Long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(Long waitTime) {
		this.waitTime = waitTime;
	}
	
	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	
	public Integer getCustomGeographyId() {
		return customGeographyId;
	}

	public void setCustomGeographyId(Integer customGeographyId) {
		this.customGeographyId = customGeographyId;
	}
	
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	/**
	 * @return the isToUseFloorplan
	 */
	public Boolean getToUseFloorplan() {
		return toUseFloorplan;
	}

	/**
	 * @param isToUseFloorplan the isToUseFloorplan to set
	 */
	public void setToUseFloorplan(Boolean toUseFloorplan) {
		this.toUseFloorplan = toUseFloorplan;
	}
	
	public Integer getRsrpThreshold() {
		return rsrpThreshold;
	}

	public void setRsrpThreshold(Integer rsrpThreshold) {
		this.rsrpThreshold = rsrpThreshold;
	}

	public Integer getSinrThreshold() {
		return sinrThreshold;
	}

	public void setSinrThreshold(Integer sinrThreshold) {
		this.sinrThreshold = sinrThreshold;
	}

	public Boolean getIsReportAvailable() {
		return isReportAvailable;
	}

	public void setIsReportAvailable(Boolean isReportAvailable) {
		this.isReportAvailable = isReportAvailable;
	}

	/**
	 * @return the wingName
	 */
	public String getWingName() {
		return wingName;
	}

	/**
	 * @param wingName the wingName to set
	 */
	public void setWingName(String wingName) {
		this.wingName = wingName;
	}

	/**
	 * @return the floorName
	 */
	public String getFloorName() {
		return floorName;
	}

	/**
	 * @param floorName the floorName to set
	 */
	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}

	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	

	public String getRecipeWappingId() {
		return recipeWappingId;
	}

	public void setRecipeWappingId(String recipeWappingId) {
		this.recipeWappingId = recipeWappingId;
	}

	public Integer getMaxSINRThreshold() {
		return maxSINRThreshold;
	}

	public void setMaxSINRThreshold(Integer maxSINRThreshold) {
		this.maxSINRThreshold = maxSINRThreshold;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	

	
	public String getDisplayOperatorName() {
		return displayOperatorName;
	}

	public void setDisplayOperatorName(String displayOperatorName) {
		this.displayOperatorName = displayOperatorName;
	}
	
	

	public String getScannerRequest() {
		return scannerRequest;
	}

	public void setScannerRequest(String scannerRequest) {
		this.scannerRequest = scannerRequest;
	}

	public String getChannelList() {
		return channelList;
	}

	public void setChannelList(String channelList) {
		this.channelList = channelList;
	}
	
	

	public String getChannelSet() {
		return channelSet;
	}

	public void setChannelSet(String channelSet) {
		this.channelSet = channelSet;
	}

	public Double getBrowseThreshold() {
		return browseThreshold;
	}

	public void setBrowseThreshold(Double browseThreshold) {
		this.browseThreshold = browseThreshold;
	}

	public Double getDlThresholdCriteria() {
		return dlThresholdCriteria;
	}

	public void setDlThresholdCriteria(Double dlThresholdCriteria) {
		this.dlThresholdCriteria = dlThresholdCriteria;
	}

	public Double getUlThresholdCriteria() {
		return ulThresholdCriteria;
	}

	public void setUlThresholdCriteria(Double ulThresholdCriteria) {
		this.ulThresholdCriteria = ulThresholdCriteria;
	}

	@Override
	public String toString() {
		return "RecipeWrapper [id=" + id + ", recipeId=" + recipeId + ", name=" + name + ", type=" + type + ", status="
				+ status + ", description=" + description + ", category=" + category + ", creationTime=" + creationTime
				+ ", modificationTime=" + modificationTime + ", creator=" + creator + ", scriptJson=" + scriptJson
				+ ", woRecipeMappingId=" + woRecipeMappingId + ", azimuth=" + azimuth + ", pci=" + pci + ", unitId="
				+ unitId + ", firstName=" + firstName + ", lastName=" + lastName + ", loop=" + loop
				+ ", iterationCount=" + iterationCount + ", maxTime=" + maxTime + ", waitTime=" + waitTime
				+ ", customGeographyId=" + customGeographyId + ", operator=" + operator + ", toUseFloorplan="
				+ toUseFloorplan + ", rsrpThreshold=" + rsrpThreshold + ", sinrThreshold=" + sinrThreshold
				+ ", dlThreshold=" + dlThreshold + ", ulThreshold=" + ulThreshold + ", jitterThreshold="
				+ jitterThreshold + ", latencyThreshold=" + latencyThreshold + ", browseThreshold=" + browseThreshold
				+ ", dlThresholdCriteria=" + dlThresholdCriteria + ", ulThresholdCriteria=" + ulThresholdCriteria
				+ ", maxSINRThreshold=" + maxSINRThreshold + ", isReportAvailable=" + isReportAvailable + ", wingName="
				+ wingName + ", floorName=" + floorName + ", unitName=" + unitName + ", logFileDownload="
				+ logFileDownload + ", recipeWappingId=" + recipeWappingId + ", remark=" + remark
				+ ", displayOperatorName=" + displayOperatorName + ", scannerRequest=" + scannerRequest
				+ ", channelList=" + channelList + ", channelSet=" + channelSet + ", cellName=" + cellName + "]";
	}





}
