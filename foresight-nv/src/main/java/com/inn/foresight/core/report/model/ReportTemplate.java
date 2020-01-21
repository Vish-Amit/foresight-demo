package com.inn.foresight.core.report.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.htrace.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.user.model.User;

@NamedQueries({
	@NamedQuery(name ="getAllReportTemplate", query = "select r from ReportTemplate r where r.isDeleted=false and r.isEnabled=true"),
	@NamedQuery(name ="getReportTemplateByReportType", query = "select r from ReportTemplate r where r.reportType like concat('%',:reportType,'%') and r.isDeleted=false"),	
	@NamedQuery(name ="getAllModule", query = "select distinct r.module from ReportTemplate r where r.isDeleted=false"),
	@NamedQuery(name ="getAllMeasure", query = "select distinct r.reportMeasure from ReportTemplate r where r.isDeleted=false"),
	@NamedQuery(name ="getAllMeasureByModule", query = "select distinct r.reportMeasure from ReportTemplate r where r.module=(:module) and r.isDeleted=false"),
	@NamedQuery(name ="getAllReportTypeByMeasure", query = "select distinct r.reportType from ReportTemplate r where r.reportMeasure=(:reportMeasure) and r.isDeleted=false"),
	@NamedQuery(name ="getAllTemplateReportType",query="select distinct r.reportType from ReportTemplate r where r.isDeleted=false"),
	@NamedQuery(name ="deleteReportTemplateById",query="delete from ReportTemplate r where r.id=(:id) and r.isDeleted=false"),
	@NamedQuery(name ="getAllReportTemplateByID", query = "select r from ReportTemplate r where r.id=:id and r.isDeleted=false and r.isEnabled=true "),
	@NamedQuery(name ="getReportTemplateByModule", query = "select r from ReportTemplate r where r.isDeleted=false and r.isEnabled=false and module=:moduleName"),
	@NamedQuery(name ="getReportMeasureByModule", query = "select distinct r.reportMeasure from ReportTemplate r where r.isDeleted=false  and module=:reportModule"),
	@NamedQuery(name ="getReportCategoryByModuleAndMeasure", query = "select distinct r.reportType from ReportTemplate r where r.isDeleted=false  and r.module=:reportModule and r.reportMeasure=:reportMeasure"),
	@NamedQuery(name ="getReportTemplateByModuleMeasureAndCategory", query = "select new com.inn.foresight.core.report.wrapper.ReportTemplateWrapper (r.id, r.module,r.reportMeasure, r.reportType,r.geographyConfig, r.reportConfig, r.reportNameConfig, r.builderClass, r.ondemand,r.isSystem, r.creationTime, r.isEnabled, r.reportGenType, r.generationConfig,r.storageType, r.downloadPath, r.extraConfig) from ReportTemplate r where r.isDeleted=false and  r.module=:reportModule and r.reportMeasure=:reportMeasure and r.reportType=:reportCategory"),
	
})

@Entity
@Table(name = "ReportTemplate")
@XmlRootElement(name = "ReportTemplate")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class ReportTemplate implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "reporttemplateid_pk")
	private Integer id;

	@Basic
	@Column(name="module")
	private String module;

	@Basic
	@Column(name="reportmeasure")
	private String reportMeasure;
	
	@Basic
	@Column(name="reporttype")
	private String reportType;
	
	@Basic
	@Lob
	@Column(name="geographyconfig")
	private String geographyConfig;
	
	@Basic
	@Lob
	@Column(name="reportconfig")
	private String reportConfig;
	
	@Basic
	@Lob
	@Column(name="reportnameconfig")
	private String reportNameConfig;
	
	@Basic
	@Column(name="builderclass")
	private String builderClass;

	@Basic
	@Column(name="ondemand")
	private Boolean ondemand;

	@Basic
	@Column(name="system")
	private Boolean isSystem;

	@Basic
	@Column(name="creationtime")
	private Date creationTime;
	
	/** The creator. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid_fk", nullable = false)
	@JsonIgnore
	private User creator;
	
	@Basic
	@Column(name="deleted")
	private Boolean isDeleted;
	
	@Basic
	@Column(name="enabled")
	private Boolean isEnabled;
	
	@Basic
	@Column(name="reportgentype")
	private String reportGenType;
	
	@Basic
	@Column(name="generationconfig")
	private String generationConfig;
	
	@Basic
	@Column(name="storagetype")
	private String storageType;
	
	@Basic
	@Column(name="downloadpath")
	private String downloadPath;
	
	@Basic
	@Lob
	@Column(name="extraconfig")
	private String extraConfig;

	public String getExtraConfig() {
		/*if(extraConfig!=null) {
			return extraConfig.replaceAll("\"","'");
		}*/
		return extraConfig;
	}

	public void setExtraConfig(String extraConfig) {
		this.extraConfig = extraConfig;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getReportMeasure() {
		return reportMeasure;
	}

	public void setReportMeasure(String reportMeasure) {
		this.reportMeasure = reportMeasure;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
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

	public String getReportNameConfig() {
		if(reportNameConfig!=null) {
			return reportNameConfig.replaceAll("\"","'");
		}
		return reportNameConfig;
	}

	public void setReportNameConfig(String reportNameConfig) {
		this.reportNameConfig = reportNameConfig;
	}

	public String getBuilderClass() {
		return builderClass;
	}

	public void setBuilderClass(String builderClass) {
		this.builderClass = builderClass;
	}

	public Boolean getOndemand() {
		return ondemand;
	}

	public void setOndemand(Boolean ondemand) {
		this.ondemand = ondemand;
	}
	
	public Boolean isSystem() {
		return isSystem;
	}
	
	public void setSystem(Boolean isSystem) {
		this.isSystem = isSystem;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getReportGenType() {
		return reportGenType;
	}

	public void setReportGenType(String reportGenType) {
		this.reportGenType = reportGenType;
	}

	public String getGenerationConfig() {
		return generationConfig;
	}

	public void setGenerationConfig(String generationConfig) {
		this.generationConfig = generationConfig;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnable) {
		this.isEnabled = isEnable;
	}

	@Override
	public String toString() {
		return "ReportTemplate [id=" + id + ", module=" + module + ", reportMeasure=" + reportMeasure + ", reportType=" + reportType + ", geographyConfig=" + geographyConfig + ", reportConfig="
				+ reportConfig + ", reportNameConfig=" + reportNameConfig + ", builderClass=" + builderClass + ", ondemand=" + ondemand + ", isSystem=" + isSystem + ", creationTime="
				+ creationTime + ", creator=" + creator +  ", storageType=" + storageType + ", isDeleted=" + isDeleted + "]";
	}

	public ReportTemplate() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
