package com.inn.foresight.core.report.wrapper;

/**
 * The Class CMReportWrapper.
 */
public class CMReportWrapper {
	
	/** The report name. */
	private String reportName;
	
	/** The report category. */
	private String reportCategory;
	
	/** The geo graphy level. */
	private String geoGraphyLevel;
	
	/** The created time. */
	private String createdTime;
	
	/** The report id. */
	private Integer reportId;
	
	
	/** The time stamp. */
	private Long timeStamp;
	
	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Gets the report name.
	 *
	 * @return the report name
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * Sets the report name.
	 *
	 * @param reportName the new report name
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	/**
	 * Gets the report category.
	 *
	 * @return the report category
	 */
	public String getReportCategory() {
		return reportCategory;
	}

	/**
	 * Sets the report category.
	 *
	 * @param reportCategory the new report category
	 */
	public void setReportCategory(String reportCategory) {
		this.reportCategory = reportCategory;
	}

	/**
	 * Gets the geo graphy level.
	 *
	 * @return the geo graphy level
	 */
	public String getGeoGraphyLevel() {
		return geoGraphyLevel;
	}

	/**
	 * Sets the geo graphy level.
	 *
	 * @param geoGraphyLevel the new geo graphy level
	 */
	public void setGeoGraphyLevel(String geoGraphyLevel) {
		this.geoGraphyLevel = geoGraphyLevel;
	}

	/**
	 * Gets the created time.
	 *
	 * @return the created time
	 */
	public String getCreatedTime() {
		return createdTime;
	}

	/**
	 * Sets the created time.
	 *
	 * @param createdTime the new created time
	 */
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * Gets the report id.
	 *
	 * @return the report id
	 */
	public Integer getReportId() {
		return reportId;
	}

	/**
	 * Sets the report id.
	 *
	 * @param reportId the new report id
	 */
	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CMReportWrapper [reportName=");
		builder.append(reportName);
		builder.append(", reportCategory=");
		builder.append(reportCategory);
		builder.append(", geoGraphyLevel=");
		builder.append(geoGraphyLevel);
		builder.append(", createdTime=");
		builder.append(createdTime);
		builder.append(", reportId=");
		builder.append(reportId);
		builder.append(", timeStamp=");
		builder.append(timeStamp);
		builder.append("]");
		return builder.toString();
	}
	
	
}
