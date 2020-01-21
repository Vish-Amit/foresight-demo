package com.inn.foresight.core.report.wrapper;

import java.util.Date;
import com.inn.core.generic.wrapper.RestWrapper;

@RestWrapper
public class ReportTemplateWrapper {

	/**
	 * The report id
	 */
	private Integer id;


	/**
	 * The report module
	 */
	private String module;

	/**
	 * The report reportMeasure
	 */
	private String reportMeasure;

	/**
	 * The report reportType
	 */
	private String reportType;

	/**
	 * The report geographyConfig
	 */
	private String geographyConfig;

	/** 
	 * The report reportConfig
	 */
	private String reportConfig;
	
	/**
	 * The report reportNameConfig
	 */
	private String reportNameConfig;

	/**
	 * The report builderClass
	 */
	private String builderClass;

	/**
	 * The report ondemand
	 */
	private Boolean ondemand;

	/**
	 * The report isSystem
	 */
	private Boolean isSystem;

	/**
	 * The report creationTime
	 */
	private Date creationTime;
	
	/**
	 * The report isEnabled
	 */
	private Boolean isEnabled;

	/**
	 * The report reportGenType
	 */
	private String reportGenType;

	/**
	 * The report generationConfig
	 */
	private String generationConfig;

	/**
	 * The report storageType
	 */
	private String storageType;

	/**
	 * The report downloadPath
	 */
	private String downloadPath;

	/**
	 * The report extraConfig
	 */
	private String extraConfig;
	
	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the module
	 */
	public String getModule() {
		return module;
	}

	/**
	 * @param module the module to set
	 */
	public void setModule(String module) {
		this.module = module;
	}

	/**
	 * @return the reportMeasure
	 */
	public String getReportMeasure() {
		return reportMeasure;
	}

	/**
	 * @param reportMeasure the reportMeasure to set
	 */
	public void setReportMeasure(String reportMeasure) {
		this.reportMeasure = reportMeasure;
	}

	/**
	 * @return the reportType
	 */
	public String getReportType() {
		return reportType;
	}

	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	
	/**
	 * @return the geographyConfig
	 */
	public String getGeographyConfig() {
		return geographyConfig;
	}

	/**
	 * @param geographyConfig the geographyConfig to set
	 */
	public void setGeographyConfig(String geographyConfig) {
		this.geographyConfig = geographyConfig;
	}

	/**
	 * @return the reportConfig
	 */
	public String getReportConfig() {
		return reportConfig;
	}

	/**
	 * @param reportConfig the reportConfig to set
	 */
	public void setReportConfig(String reportConfig) {
		this.reportConfig = reportConfig;
	}

	/**
	 * @return the reportNameConfig
	 */
	public String getReportNameConfig() {
		return reportNameConfig;
	}

	/**
	 * @param reportNameConfig the reportNameConfig to set
	 */
	public void setReportNameConfig(String reportNameConfig) {
		this.reportNameConfig = reportNameConfig;
	}

	/**
	 * @return the builderClass
	 */
	public String getBuilderClass() {
		return builderClass;
	}

	/**
	 * @param builderClass the builderClass to set
	 */
	public void setBuilderClass(String builderClass) {
		this.builderClass = builderClass;
	}

	/**
	 * @return the ondemand
	 */
	public Boolean getOndemand() {
		return ondemand;
	}

	/**
	 * @param ondemand the ondemand to set
	 */
	public void setOndemand(Boolean ondemand) {
		this.ondemand = ondemand;
	}

	/**
	 * @return the isSystem
	 */
	public Boolean getIsSystem() {
		return isSystem;
	}

	/**
	 * @param isSystem the isSystem to set
	 */
	public void setIsSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}

	/**
	 * @return the creationTime
	 */
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * @return the isEnabled
	 */
	public Boolean getIsEnabled() {
		return isEnabled;
	}

	/**
	 * @param isEnabled the isEnabled to set
	 */
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * @return the reportGenType
	 */
	public String getReportGenType() {
		return reportGenType;
	}

	/**
	 * @param reportGenType the reportGenType to set
	 */
	public void setReportGenType(String reportGenType) {
		this.reportGenType = reportGenType;
	}

	/**
	 * @return the generationConfig
	 */
	public String getGenerationConfig() {
		return generationConfig;
	}

	/**
	 * @param generationConfig the generationConfig to set
	 */
	public void setGenerationConfig(String generationConfig) {
		this.generationConfig = generationConfig;
	}

	/**
	 * @return the storageType
	 */
	public String getStorageType() {
		return storageType;
	}

	/**
	 * @param storageType the storageType to set
	 */
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	/**
	 * @return the downloadPath
	 */
	public String getDownloadPath() {
		return downloadPath;
	}

	/**
	 * @param downloadPath the downloadPath to set
	 */
	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	/**
	 * @return the extraConfig
	 */
	public String getExtraConfig() {
		return extraConfig;
	}

	/**
	 * @param extraConfig the extraConfig to set
	 */
	public void setExtraConfig(String extraConfig) {
		this.extraConfig = extraConfig;
	}

	/**
	 * 
	 */
	public ReportTemplateWrapper() {
		super();
	}

	/**
	 * @param id
	 * @param module
	 * @param reportMeasure
	 * @param reportType
	 */
	public ReportTemplateWrapper(Integer id, String module, String reportMeasure, String reportType) {
		super();
		this.id = id;
		this.module = module;
		this.reportMeasure = reportMeasure;
		this.reportType = reportType;
	}
	
	/**
	 * @param id
	 * @param module
	 * @param reportMeasure
	 * @param reportType
	 * @param geographyConfig
	 * @param reportConfig
	 * @param reportNameConfig
	 * @param builderClass
	 * @param ondemand
	 * @param isSystem
	 * @param creationTime
	 * @param isEnabled
	 * @param reportGenType
	 * @param generationConfig
	 * @param storageType
	 * @param downloadPath
	 * @param extraConfig
	 */
	public ReportTemplateWrapper(Integer id, String module, String reportMeasure, String reportType,
			String geographyConfig, String reportConfig, String reportNameConfig, String builderClass, Boolean ondemand,
			Boolean isSystem, Date creationTime, Boolean isEnabled, String reportGenType, String generationConfig,
			String storageType, String downloadPath, String extraConfig) {
		super();
		this.id = id;
		this.module = module;
		this.reportMeasure = reportMeasure;
		this.reportType = reportType;
		this.geographyConfig = geographyConfig;
		this.reportConfig = reportConfig;
		this.reportNameConfig = reportNameConfig;
		this.builderClass = builderClass;
		this.ondemand = ondemand;
		this.isSystem = isSystem;
		this.creationTime = creationTime;
		this.isEnabled = isEnabled;
		this.reportGenType = reportGenType;
		this.generationConfig = generationConfig;
		this.storageType = storageType;
		this.downloadPath = downloadPath;
		this.extraConfig = extraConfig;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ReportTemplateWrapper [id=" + id + ", module=" + module + ", reportMeasure=" + reportMeasure
				+ ", reportType=" + reportType + ", geographyConfig=" + geographyConfig + ", reportConfig="
				+ reportConfig + ", reportNameConfig=" + reportNameConfig + ", builderClass=" + builderClass
				+ ", ondemand=" + ondemand + ", isSystem=" + isSystem + ", creationTime=" + creationTime
				+ ", isEnabled=" + isEnabled + ", reportGenType=" + reportGenType + ", generationConfig="
				+ generationConfig + ", storageType=" + storageType + ", downloadPath=" + downloadPath
				+ ", extraConfig=" + extraConfig + "]";
	}

	


	


}
