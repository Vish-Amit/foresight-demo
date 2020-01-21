package com.inn.foresight.core.report.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.user.model.User;

@NamedQueries({
		@NamedQuery(name = "getInProcessRepositories", query = " select repo from AnalyticsRepository repo where progress='In_Progress' and deleted<>1 and repositorytype !='CUSTOM_REPORT' ORDER BY repo.modifiedTime"),
		@NamedQuery(name = "getReportInfoByName", query = " select repo from AnalyticsRepository repo where repo.name=:FileName")
		
})

@FilterDefs(value = {
		@FilterDef(name="getRepositoryForCurrentUser",parameters={@ParamDef(name = "userId", type = "java.lang.Integer"),@ParamDef(name = "roleIds", type = "java.lang.Integer")}),
})
@Filters(value = {
		@Filter(name="getRepositoryForCurrentUser", condition="analyticsrepositoryid_pk in(select distinct d.analyticsrepositoryid_pk  from AnalyticsRepository d where d.deleted <> 1 and (d.creatorid_fk =:userId or d.analyticsrepositoryid_pk in (select rshare.analyticsrepositoryid_fk from RepositoryShare rshare where( rshare.userid_fk=:userId and rshare.type='User') or (rshare.type='Role' and rshare.roleid_fk in (:roleIds) ))))"),
})

@Entity
@Table(name="AnalyticsRepository")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AnalyticsRepository {

	/** dashboard

	 /** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(columnDefinition="INT",name="analyticsrepositoryid_pk")
	private Integer id;

	private String category;

	private String filepath;

	@Column(name = "name")
	private String name;
	
	@Column(name = "downloadfilename")
	private String downloadFileName;

	@Column(name = "deleted")
	private Boolean deleted;

	@Column(name = "dashboardid")
	private Integer dashboardId;

	@Column(name = "templateid")
	private Integer templateId;


	@Column(name = "scheduled")
	private Boolean scheduled;
	
	@Basic
	@Column(name = "modificationtime", insertable = true, updatable = true)
	private Date modifiedTime;

	@Basic
	@Column(name = "creationtime", insertable = true, updatable = false)
	private Date createdTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lastmodifierid_fk")
	protected User lastModifier;

	@Column(name="progress")
	@Enumerated(EnumType.STRING)
	private progress progress;

	@Column(name="repositorytype")
	@Enumerated(EnumType.STRING)
	private RepositoryType repositoryType;

	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "reporttemplateid_fk")
	private ReportTemplate reportTemplate;
	
	@Basic
//	@Lob
	@Column(name = "geographyconfig",columnDefinition = "json")
	private String geographyConfig;

	@Basic	
//	@Lob
	@Column(name = "reportconfig", columnDefinition = "json")
	private String reportConfig;
	
	@Column(name = "storagetype")
	private String storageType;

	@Column(name = "queuestatus")
	private Boolean queueStatus;
	
	@Column(name = "modulename")
	private String moduleName;
	
	@Transient
	private String generatedReportPath;

	@Transient
	private String cachedReportTime;

	public String getGeneratedReportPath() {
		return generatedReportPath;
	}

	public void setGeneratedReportPath(String generatedReportPath) {
		this.generatedReportPath = generatedReportPath;
	}

	public String getCachedReportTime() {
		return cachedReportTime;
	}

	public void setCachedReportTime(String cachedReportTime) {
		this.cachedReportTime = cachedReportTime;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	
	public Boolean getIsScheduled() {
		return scheduled;
	}

	public void setIsScheduled(Boolean isScheduled) {
		this.scheduled = isScheduled;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Integer getDashboardId() {
		return dashboardId;
	}

	public void setDashboardId(Integer dashboardId) {
		this.dashboardId = dashboardId;
	}

	public static enum progress{
		In_Progress,
		Generated,
		Failed
	}
	/**
	 * The Enum RepositoryType.
	 */
	public static enum RepositoryType {

		/** The Report. */
		REPORT,
		/** The Analytics. */
		ANALYTICS,
		/** The Grid. */
		GRID,
		/** The Chart. */
		CHART,
		/** The custom report. */
		CUSTOM_REPORT
	}

	public RepositoryType getRepositoryType() {
		return repositoryType;
	}

	public void setRepositoryType(RepositoryType repositoryType) {
		this.repositoryType = repositoryType;
	}

	public progress getProgress() {
		return progress;
	}

	public void setProgress(progress progress) {
		this.progress = progress;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creatorid_fk", updatable = false)
	private User creator;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public User getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(User lastModifier) {
		this.lastModifier = lastModifier;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	public String getGeographyConfig() {
		if(geographyConfig!=null) {
			return geographyConfig.replaceAll("\"","'");
		}
		return geographyConfig;
	}

	public void setGeographyConfig(String geographyConfig) {
		this.geographyConfig = geographyConfig;
	}

	public String getReportConfig() {
		if(reportConfig!=null) {
			return reportConfig.replaceAll("\"","'");
		}
		return reportConfig;
	}

	public void setReportConfig(String reportConfig) {
		this.reportConfig = reportConfig;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Boolean getScheduled() {
		return scheduled;
	}

	public void setScheduled(Boolean scheduled) {
		this.scheduled = scheduled;
	}

	public ReportTemplate getReportTemplate() {
		return reportTemplate;
	}

	public void setReportTemplate(ReportTemplate reportTemplate) {
		this.reportTemplate = reportTemplate;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public String getDownloadFileName() {
		return downloadFileName;
	}

	public void setDownloadFileName(String downloadFileName) {
		this.downloadFileName = downloadFileName;
	}

	public Boolean getQueueStatus() {
		return queueStatus;
	}

	public void setQueueStatus(Boolean queueStatus) {
		this.queueStatus = queueStatus;
	}

	@Override
	public String toString() {
		return "AnalyticsRepository [id=" + id + ", category=" + category + ", filepath=" + filepath
				+ ", name=" + name + ", deleted=" + deleted + ", dashboardId=" + dashboardId
				+ ", templateId=" + templateId + ", scheduled=" + scheduled + ", modifiedTime=" + modifiedTime
				+ ", createdTime=" + createdTime + ", lastModifier=" + lastModifier + ", progress=" + progress
				+ ", repositoryType=" + repositoryType + ", reportTemplate=" + reportTemplate + ", geographyConfig="
				+ geographyConfig + ", reportConfig=" + reportConfig + ", storageType=" + storageType + ", creator="
				+ creator + "]";
	}
	
}
