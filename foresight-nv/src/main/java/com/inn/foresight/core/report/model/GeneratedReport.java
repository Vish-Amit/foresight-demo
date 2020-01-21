package com.inn.foresight.core.report.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;
import org.hibernate.envers.NotAudited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.user.model.User;


/**
 * The Class GeneratedReport.
 */
@NamedQueries({

	
	@NamedQuery(name = "getGeneratedReportBySearchNameAndUser", query="select g from GeneratedReport g where ((g.reportType=:reportType1 or g.reportType=:reportType2 or g.reportType=:reportType3  or g.reportType=:reportType4) and LOWER(g.reportName) like :reportname and g.creator.userid=:userId) order by g.id desc"),
	@NamedQuery(name = "getGeneratedReportBySearchName", query="select g from GeneratedReport g where (g.reportType=:reportType1 or g.reportType=:reportType2 or g.reportType=:reportType3  or g.reportType=:reportType4) and LOWER(g.reportName) like :reportname  order by g.id desc"),
	
	@NamedQuery(name = "getGeneratedReportBySearchNameAndUserAndType", query="select g from GeneratedReport g where (((g.reportType=:reportType1 or g.reportType=:reportType2 or g.reportType=:reportType3 or g.reportType=:reportType4) and (g.creator.userid=:userId or g.generatedType=:generatedType)) and LOWER(g.reportName) like :reportname) order by g.id desc"),
	
	
	
	@NamedQuery(name = "getGeneratedReportBySearchNameAndDate", query="select g from GeneratedReport g where (g.reportType in (:reportTypeList) ) and (DATE(g.generatedDate) BETWEEN :fromDate AND :toDate) and LOWER(g.reportName) like :reportname and g.generatedType=:generatedType and LOWER(g.node) like :node and LOWER(g.duration) like :duration and LOWER(g.reportLevel) like :reportLevel  and LOWER(g.geography) like :geography and g.domain like :domain and g.vendor like :vendor order by g.id desc"),
	@NamedQuery(name = "getGeneratedReportBySearchNameAndUserAndTypeAndDate", query="select g from GeneratedReport g where (((g.reportType in (:reportTypeList) ) and (g.creator.userid=:userId or g.generatedType=:generatedType)) and (DATE(g.generatedDate) BETWEEN :fromDate AND :toDate) and LOWER(g.reportName) like :reportname) and g.generatedType=:generatedType and LOWER(g.node) like :node  and LOWER(g.duration) like :duration and LOWER(g.reportLevel) like :reportLevel and LOWER(g.geography) like :geography and g.domain like :domain and g.vendor like :vendor order by g.id desc"),
	@NamedQuery(name = "getGeneratedReportBySearchNameAndUserAndDate", query="select g from GeneratedReport g where ((g.reportType in (:reportTypeList) ) and (DATE(g.generatedDate) BETWEEN :fromDate AND :toDate) and LOWER(g.reportName) like :reportname and g.creator.userid=:userId) and g.generatedType=:generatedType and LOWER(g.node) like :node and LOWER(g.duration) like :duration and LOWER(g.reportLevel) like :reportLevel and LOWER(g.geography) like :geography and g.domain like :domain and g.vendor like :vendor order by g.id desc"),
	
	
	
	@NamedQuery(name = "getGeneratedReportCountBySearchByUser", query="select count(g.id) from GeneratedReport g where (g.reportType=:reportType1 or g.reportType=:reportType2  or g.reportType=:reportType3 or g.reportType=:reportType4) and LOWER(g.reportName) like :reportname and g.creator.userid=:userId  "),
	@NamedQuery(name = "getGeneratedReportCountBySearchByUserAndType", query="select count(g.id) from GeneratedReport g where (((g.reportType=:reportType1 or g.reportType=:reportType2 or g.reportType=:reportType3 or g.reportType=:reportType4) and (g.creator.userid=:userId or g.generatedType=:generatedType)) and LOWER(g.reportName) like :reportname) order by g.id desc"),
	
	@NamedQuery(name = "getGeneratedReportCountBySearch", query="select count(g.id) from GeneratedReport g where (g.reportType=:reportType1 or g.reportType=:reportType2  or g.reportType=:reportType3 or g.reportType=:reportType4) and LOWER(g.reportName) like :reportname"),
	@NamedQuery(name = "deleteGeneratedReportById", query = "delete from GeneratedReport g where g.id=:id"),
	
	
	@NamedQuery(name = "getGeneratedReportCountBySearchAndDate", query="select count(g.id) from GeneratedReport g where (g.reportType in (:reportTypeList)) and (DATE(g.generatedDate) BETWEEN :fromDate AND :toDate) and g.generatedType=:generatedType and LOWER(g.reportName) like :reportname and LOWER(g.node) like :node and LOWER(g.duration) like :duration and LOWER(g.reportLevel) like :reportLevel and LOWER(g.geography) like :geography and g.domain like :domain and g.vendor like :vendor"),
	@NamedQuery(name = "getGeneratedReportCountBySearchByUserAndTypeAndDate", query="select count(g.id) from GeneratedReport g where (((g.reportType in (:reportTypeList)) and (g.creator.userid=:userId and g.generatedType=:generatedType)) and (DATE(g.generatedDate) BETWEEN :fromDate AND :toDate) and g.generatedType=:generatedType and LOWER(g.reportName) like (:reportname)) and LOWER(g.node) like :node and LOWER(g.duration) like :duration and LOWER(g.reportLevel) like :reportLevel and LOWER(g.geography) like :geography and g.domain like :domain and g.vendor like :vendor"),
	@NamedQuery(name = "getGeneratedReportCountBySearchAndTypeAndDate", query="select count(g.id) from GeneratedReport g where (((g.reportType in (:reportTypeList)) and (g.generatedType=:generatedType)) and (DATE(g.generatedDate) BETWEEN :fromDate AND :toDate) and g.generatedType=:generatedType and LOWER(g.reportName) like (:reportname)) and LOWER(g.node) like :node and LOWER(g.duration) like :duration and LOWER(g.reportLevel) like :reportLevel and LOWER(g.geography) like :geography and g.domain like :domain and g.vendor like :vendor"),
	@NamedQuery(name = "getGeneratedReportCountBySearchByUserAndDate", query="select count(g.id) from GeneratedReport g where (g.reportType in (:reportTypeList)) and (DATE(g.generatedDate) BETWEEN :fromDate AND :toDate) and g.generatedType=:generatedType and LOWER(g.reportName) like (:reportname) and g.creator.userid=:userId and LOWER(g.node) like :node and LOWER(g.duration) like :duration and LOWER(g.reportLevel) like :reportLevel and LOWER(g.geography) like :geography and g.domain like :domain and g.vendor like :vendor "),

	@NamedQuery(name = "getAllDistinctDomain", query="select distinct g.domain from GeneratedReport g where (g.reportType in (:reportTypeList)) and (DATE(g.generatedDate) BETWEEN :fromDate AND :toDate) and g.generatedType=:generatedType "),
	@NamedQuery(name = "getAllDistinctVendor", query="select distinct g.vendor from GeneratedReport g where (g.reportType in (:reportTypeList)) and (DATE(g.generatedDate) BETWEEN :fromDate AND :toDate) and g.generatedType=:generatedType and g.domain like :domain "),
	
	@NamedQuery(name = "getAllDistinctNode", query="select distinct g.node from GeneratedReport g where (g.reportType in (:reportTypeList)) and (DATE(g.generatedDate) BETWEEN :fromDate AND :toDate) and g.generatedType=:generatedType and g.domain like :domain and g.vendor like :vendor "),
	
	@NamedQuery(name = "getAllDistinctLevel", query="select distinct g.reportLevel from GeneratedReport g where (g.reportType in (:reportTypeList)) and (DATE(g.generatedDate) BETWEEN :fromDate AND :toDate) and g.generatedType=:generatedType and g.domain like :domain and g.vendor like :vendor and g.duration like :duration "),
	@NamedQuery(name = "getAllDistinctGeography", query="select distinct g.geography from GeneratedReport g where (g.reportType in (:reportTypeList)) and (DATE(g.generatedDate) BETWEEN :fromDate AND :toDate) and g.generatedType=:generatedType and g.domain like :domain and g.vendor like :vendor and g.duration like :duration and g.reportLevel like :reportLevel"),
	@NamedQuery(name = "updateProgressStateById", query = "update GeneratedReport g set g.progressState=:progressState where g.id=:id"),
})


@Entity
@Table(name = "GeneratedReport")
@XmlRootElement(name = "GeneratedReport")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })

public class GeneratedReport implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "generatedReportId_PK")
	@Id
	private Integer id;
	
	/** The report name. */
	@Basic
	private String reportName;
	
	/** The generated date. */
	@Basic
	private Date generatedDate;
	
	/** The file path. */
	@Basic
	private String filePath;
	
	/** The duration. */
	@Basic
	private String duration;

	/** The report type. */
	@Enumerated(EnumType.STRING)
	private ReportType reportType;

	/**
	 * The Enum ReportType.
	 */
	public enum ReportType {

		/** The performance report. */
		PERFORMANCE_REPORT, 
		/** The exception report. */
		EXCEPTION_REPORT,
		WPCREPORT,
		/** The performance bts. */
		PERFORMANCE_BTS,
		/** The custom report. */
		CUSTOM_REPORT,
		/** The trend report. */
		TREND_REPORT;

	}

	/** The modified time. */
	@Basic
	private Date modificationtime;

	/** The report search. */
	private String reportSearch;
	
	
	/** The domain. */
	@Basic
	private String domain;
	
	/** The vendor. */
	@Basic
	private String vendor;
	
	/** The node. */
	@Basic
	private String node;
	
	/** The subscriber. */
	@Basic
	private String subscriber;
	
	/** The generated type. */
	@Basic
	private String generatedType;

	/** The file size. */
	@Basic
	private String fileSize;

	/** The progress state. */
	@Basic
	private String progressState;
	
	@Basic
	private String geography;
	
	@Basic
	private String reportLevel;
	
	/**
	 * Instantiates a new generated report.
	 */
	public GeneratedReport() {
		super();
	}

	/**
	 * Instantiates a new generated report.
	 *
	 * @param reportType the report type
	 * @param filePath the file path
	 * @param reportName the report name
	 * @param generatedDate the generated date
	 */
	public GeneratedReport(ReportType reportType, String filePath, String reportName, Date generatedDate) {
		this.reportType = reportType;
		this.filePath = filePath;
		this.reportName = reportName;
		this.generatedDate = generatedDate;
	}
	
	/**
	 * *
	 * 
	 * Getters and setters of userSearch.
	 *
	 * @return the report search
	 */

	@JsonIgnore
	public String getReportSearch() {
		return reportSearch;
	}

	/**
	 * Sets the report search.
	 *
	 * @param reportSearch the new report search
	 */
	public void setReportSearch(String reportSearch) {
		this.reportSearch = reportSearch;
	}


	/** The creator. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId_FK", nullable = false)
	private User creator;
	
	/** The shared widget. */
	@OneToMany(mappedBy="generatedReport", cascade = CascadeType.REMOVE,  fetch = FetchType.EAGER)
	@NotAudited


	/**
	 * Update report search.
	 */
	@PostPersist
	@PrePersist
	@PreUpdate
	public void updateReportSearch() {
		StringBuilder builder = new StringBuilder();
		if (reportType != null) {
			builder.append(reportType);
		}
		if (reportName != null) {
			builder.append(reportName);
		}
		if (filePath != null) {
			builder.append(filePath);
		}
		reportSearch = builder.toString();
		reportSearch = StringUtils.replace(reportSearch, " ", "").toLowerCase();
	}

	/**
	 * Gets the modified time.
	 *
	 * @return the modified time
	 */
	public Date getModifiedTime() {
		return modificationtime;
	}

	/**
	 * Sets the modified time.
	 *
	 * @param modifiedTime the new modified time
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modificationtime = modifiedTime;
	}

	/**
	 * Gets the checks if is read.
	 *
	 * @return the checks if is read
	 */
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
	 * Gets the generated date.
	 *
	 * @return the generated date
	 */
	public Date getGeneratedDate() {
		return generatedDate;
	}

	/**
	 * Sets the generated date.
	 *
	 * @param generatedDate the new generated date
	 */
	public void setGeneratedDate(Date generatedDate) {
		this.generatedDate = generatedDate;
	}

	/**
	 * Gets the file path.
	 *
	 * @return the file path
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * Sets the file path.
	 *
	 * @param filePath the new file path
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Gets the duration.
	 *
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}

	/**
	 * Sets the duration.
	 *
	 * @param duration the new duration
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}

	/**
	 * Gets the report type.
	 *
	 * @return the report type
	 */
	public ReportType getReportType() {
		return reportType;
	}

	/**
	 * Sets the report type.
	 *
	 * @param reportType the new report type
	 */
	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}


	/**
	 * Gets the domain.
	 *
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the domain.
	 *
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Gets the vendor.
	 *
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * Sets the vendor.
	 *
	 * @param vendor the vendor to set
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * Gets the node.
	 *
	 * @return the node
	 */
	public String getNode() {
		return node;
	}

	/**
	 * Sets the node.
	 *
	 * @param node the node to set
	 */
	public void setNode(String node) {
		this.node = node;
	}

	/**
	 * Gets the creator.
	 *
	 * @return the creator
	 */
	@JsonIgnore
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
	 * Gets the subscriber.
	 *
	 * @return the subscriber
	 */
	public String getSubscriber() {
		return subscriber;
	}

	/**
	 * Sets the subscriber.
	 *
	 * @param subscriber the new subscriber
	 */
	public void setSubscriber(String subscriber) {
		this.subscriber = subscriber;
	}

	/**
	 * Gets the modificationtime.
	 *
	 * @return the modificationtime
	 */
	public Date getModificationtime() {
		return modificationtime;
	}

	/**
	 * Sets the modificationtime.
	 *
	 * @param modificationtime the new modificationtime
	 */
	public void setModificationtime(Date modificationtime) {
		this.modificationtime = modificationtime;
	}

	/**
	 * Gets the generated type.
	 *
	 * @return the generated type
	 */
	public String getGeneratedType() {
		return generatedType;
	}

	/**
	 * Sets the generated type.
	 *
	 * @param generatedType the new generated type
	 */
	public void setGeneratedType(String generatedType) {
		this.generatedType = generatedType;
	}

	/**
	 * Gets the file size.
	 *
	 * @return the file size
	 */
	public String getFileSize() {
		return fileSize;
	}

	/**
	 * Sets the file size.
	 *
	 * @param fileSize the new file size
	 */
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * Gets the progress state.
	 *
	 * @return the progress state
	 */
	public String getProgressState() {
		return progressState;
	}

	/**
	 * Sets the progress state.
	 *
	 * @param progressState the new progress state
	 */
	public void setProgressState(String progressState) {
		this.progressState = progressState;
	}

	public String getGeography() {
		return geography;
	}

	public void setGeography(String geography) {
		this.geography = geography;
	}

	public String getReportLevel() {
			return reportLevel;
	}

	 public void setReportLevel(String reportLevel) {
			this.reportLevel = reportLevel;
	 }


	@Override
	public String toString() {
		return "GeneratedReport [id=" + id + ", reportName=" + reportName
				+ ", generatedDate=" + generatedDate + ", filePath=" + filePath
				+ ", duration=" + duration + ", reportType=" + reportType
				+ ", modificationtime=" + modificationtime + ", reportSearch="
				+ reportSearch + ", domain=" + domain + ", vendor=" + vendor
				+ ", node=" + node + ", subscriber=" + subscriber
				+ ", generatedType=" + generatedType + ", fileSize=" + fileSize
				+ ", progressState=" + progressState + ", geography="
				+ geography + ", reportLevel=" + reportLevel + ", creator=" + creator + "]";
	}
	

}
