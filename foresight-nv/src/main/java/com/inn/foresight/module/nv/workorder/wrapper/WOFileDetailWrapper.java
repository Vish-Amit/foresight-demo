package com.inn.foresight.module.nv.workorder.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping.ProcessStatus;

import java.util.List;

@RestWrapper
public class WOFileDetailWrapper {

	private Integer id;
	private String fileName;
	private String proccessFileName;
	private String filetype;
	private Boolean isDeleted;
	private ProcessStatus processedStatus;
	private String remark;
	private Long fileSize;
	// Set file sync time in this field while returning response
	private Long fileSyncTime;
	private Long processStartTime;
	private Long processEndTime;
	private String woName;
	private String recipeName;



	private Integer recipeId;
	// For Time Range filter on sync Time
	private Long startSyncTime;
	private Long endSyncTime;

	List<String> processedStatusList;


	public WOFileDetailWrapper(Integer id,Integer worecipeId){
		this.id=id;
		this.recipeId=worecipeId;

	}

	public WOFileDetailWrapper() {
	
	}

	public Integer getRecipeId() {
		return recipeId;
	}

	public void setRecipeId(Integer recipeId) {
		this.recipeId = recipeId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getProccessFileName() {
		return proccessFileName;
	}

	public void setProccessFileName(String proccessFileName) {
		this.proccessFileName = proccessFileName;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public Boolean getDeleted() {
		return isDeleted;
	}

	public void setDeleted(Boolean deleted) {
		isDeleted = deleted;
	}

	public ProcessStatus getProcessedStatus() {
		return processedStatus;
	}

	public void setProcessedStatus(ProcessStatus processedStatus) {
		this.processedStatus = processedStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Long getFileSyncTime() {
		return fileSyncTime;
	}

	public void setFileSyncTime(Long fileSyncTime) {
		this.fileSyncTime = fileSyncTime;
	}

	public Long getProcessStartTime() {
		return processStartTime;
	}

	public void setProcessStartTime(Long processStartTime) {
		this.processStartTime = processStartTime;
	}

	public Long getProcessEndTime() {
		return processEndTime;
	}

	public void setProcessEndTime(Long processEndTime) {
		this.processEndTime = processEndTime;
	}

	public String getWoName() {
		return woName;
	}

	public void setWoName(String woName) {
		this.woName = woName;
	}

	public String getRecipeName() {
		return recipeName;
	}

	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}

	public Long getStartSyncTime() {
		return startSyncTime;
	}

	public void setStartSyncTime(Long startSyncTime) {
		this.startSyncTime = startSyncTime;
	}

	public Long getEndSyncTime() {
		return endSyncTime;
	}

	public void setEndSyncTime(Long endSyncTime) {
		this.endSyncTime = endSyncTime;
	}

	public List<String> getProcessedStatusList() {
		return processedStatusList;
	}

	public void setProcessedStatusList(List<String> processedStatusList) {
		this.processedStatusList = processedStatusList;
	}

	@Override
	public String toString() {
		return "WOFileDetailWrapper{" + "id=" + id + ", fileName='" + fileName + '\'' + ", proccessFileName='"
				+ proccessFileName + '\'' + ", filetype='" + filetype + '\'' + ", isDeleted=" + isDeleted
				+ ", processedStatus=" + processedStatus + ", remark='" + remark + '\'' + ", fileSize=" + fileSize
				+ ", fileSyncTime=" + fileSyncTime + ", processStartTime=" + processStartTime + ", processEndTime="
				+ processEndTime + ", woName='" + woName + '\'' + ", recipeName='" + recipeName + '\''
				+ ", startSyncTime=" + startSyncTime + ", endSyncTime=" + endSyncTime + ", processedStatusList="
				+ processedStatusList + '}';
	}
}
