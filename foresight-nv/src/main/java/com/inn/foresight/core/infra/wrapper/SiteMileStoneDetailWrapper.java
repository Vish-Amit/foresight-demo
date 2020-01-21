package com.inn.foresight.core.infra.wrapper;

import com.inn.core.generic.wrapper.RestWrapper;

@RestWrapper
public class SiteMileStoneDetailWrapper {

	/** The band. */
	private String band;
	
	/** The site stage. */
	private String siteStage;
	
	/** The task completion status. */
	private String taskCompletionStatus;
	
	/** The task status. */
	private String taskStatus;
	
	/** The task completion date. */
	private String taskCompletionDate;
	
	/** The taskName. */
	private String taskName;
	
	/** The task days. */
	private Integer taskDays;

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public String getSiteStage() {
		return siteStage;
	}

	public void setSiteStage(String siteStage) {
		this.siteStage = siteStage;
	}

	public String getTaskCompletionStatus() {
		return taskCompletionStatus;
	}

	public void setTaskCompletionStatus(String taskCompletionStatus) {
		this.taskCompletionStatus = taskCompletionStatus;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getTaskCompletionDate() {
		return taskCompletionDate;
	}

	public void setTaskCompletionDate(String taskCompletionDate) {
		this.taskCompletionDate = taskCompletionDate;
	}

	public Integer getTaskDays() {
		return taskDays;
	}

	public void setTaskDays(Integer taskDays) {
		this.taskDays = taskDays;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	@Override
	public String toString() {
		return "SiteMileStoneDetailWrapper [band=" + band + ", siteStage=" + siteStage + ", taskCompletionStatus="
				+ taskCompletionStatus + ", taskStatus=" + taskStatus + ", taskCompletionDate=" + taskCompletionDate
				+ ", taskName=" + taskName + ", taskDays=" + taskDays + "]";
	}
	
}
