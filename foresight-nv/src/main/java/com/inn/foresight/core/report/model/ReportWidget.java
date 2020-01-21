package com.inn.foresight.core.report.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.core.report.wrapper.ReportWidgetWrapper;
import com.inn.product.um.user.model.User;

/**
 * The Class ReportWidget.
 */
@NamedQueries({
	
		@NamedQuery(name ="getAllReportTypeByReportMeasure", query = "select distinct r.reportType from ReportWidget r where r.reportMeasure=(:reportMeasure)"),
		@NamedQuery(name ="getAllReportByReportMeasure", query = "select r from ReportWidget r where r.reportMeasure in (:reportMeasure)"),
		@NamedQuery(name ="getAllReportMeasure", query = "select distinct r.reportMeasure from ReportWidget r"),
		@NamedQuery(name ="getReportById", query = "select r from ReportWidget r where r.id =:id"),
		@NamedQuery(name="getTotalReportsCountBySearch",query="select count(u.id) from ReportWidget u where u.reportMeasure in (:measure) and  LOWER(u.reportName) like :reportname  and u.isDeleted=false"),
		
		@NamedQuery(name="getTotalReportsCountBySearchAndUser",query="select count(u.id) from ReportWidget u where (u.reportMeasure in (:measure) and  LOWER(u.reportName) like :reportname  and u.creator.userid=:userId  and u.isDeleted=false)"),
		@NamedQuery(name="getTotalReportsCountBySearchAndUserAndType",query="select count(u.id) from ReportWidget u where (u.reportMeasure in (:measure) and  LOWER(u.reportName) like :reportname  and (u.creator.userid=:userId or u.reportMeasure =:scheduledPmReport or u.reportMeasure =:scheduledExReport )  and u.isDeleted=false)"),
		
		@NamedQuery(name="getReportsSearchByName",query="select u from ReportWidget u  where u.reportMeasure in (:measure) and  LOWER(u.reportName) like :reportname  and u.isDeleted=false order by u.modifiedTime desc"),
		
		@NamedQuery(name="getReportsSearchByNameAndUser",query="select u from ReportWidget u  where (u.reportMeasure in (:measure) and  LOWER(u.reportName) like :reportname and u.creator.userid=:userId  and u.isDeleted=false) order by u.modifiedTime desc"),
		@NamedQuery(name="getReportsSearchByNameAndUserAndType",query="select u from ReportWidget u  where (u.reportMeasure in (:measure) and  LOWER(u.reportName) like :reportname and (u.creator.userid=:userId or u.reportMeasure =:scheduledPmReport or u.reportMeasure =:scheduledExReport )  and u.isDeleted=false) order by u.modifiedTime desc"),

		@NamedQuery(name = "softDeleteWidgetById", query = "update  ReportWidget r set r.isDeleted=1 where r.id =:id "),
		@NamedQuery(name="getAllReportForCMComparision", query="select r from com.inn.foresight.core.report.model.ReportWidget r where r.reportMeasure in(:reportMeasure) and r.creator.userid=:userId and r.isDeleted=false order by r.modifiedTime desc"),
		@NamedQuery(name="getPmOnDemandReportList",query="select u from ReportWidget u where (u.reportMeasure='Performance Report_ONDEMAND' or u.reportMeasure='Exception Report_ONDEMAND') and u.isDeleted=false and u.domain in (:Domain) and u.status=:status and u.serverInstance !='A31' "
				+ "order by u.id"),
		@NamedQuery(name = "updateStatusByIdAndStatusAndModifiedTime", query = "update  ReportWidget r set r.status=:status,r.modifiedTime=:modifiedTime where r.id=:id"),		
		@NamedQuery(name = "updateStatusByIdAndStatus", query = "update  ReportWidget r set r.status=:status where r.id=:id"),
		@NamedQuery(name="getAllPmReport",query="select u from ReportWidget u where (u.reportMeasure='Performance Report' or u.reportMeasure='Exception Report') and u.isDeleted=false and u.domain ='RAN' order by u.modifiedTime desc"),
		@NamedQuery(name="getAllWidgetReportType",query="select distinct r.reportType from ReportWidget r "),
		@NamedQuery(name="getAllScheduledPMReport",query="select u from ReportWidget u where (u.reportMeasure='Performance Report' or u.reportMeasure='Exception Report') and u.isDeleted=false and u.domain in (:domain) order by u.modifiedTime "),
		@NamedQuery(name="getAllScheduledPMReportDomainVendor",query="select u from ReportWidget u where u.reportMeasure in :reportMeasure and u.isDeleted=false and u.domain = :domain and u.vendor = :vendor order by u.modifiedTime "),

//		@NamedQuery(name = "getAllQueryBuilder", query = "select new com.inn.foresight.module.cm.wrapper.QueryBuilderWrapper(q.id, q.reportName, q.reportType,q.domain, q.vendor,q.creator.email,q.creationTime,q.filePath,q.modifiedTime,q.modifier.email) from ReportWidget q where q.creator.userid=:loginuser Order by q.creationTime desc"),
//		@NamedQuery(name = "getCountForAllQueryBuilder", query = "select count(q) from ReportWidget q where q.modifier.userName=:loginuser"),

//		@NamedQuery(name = "getAllQueryBuilder", query = "select new com.inn.foresight.core.report.wrapper.QueryBuilderWrapper(q.id, q.reportName, q.reportType,q.domain, q.vendor,q.creator.email,q.creationTime,q.filePath,q.modifiedTime,q.modifier.email, q.configuration) from ReportWidget q where q.creator.userid=:loginuser and q.reportMeasure = 'QUERYBUILDER' and q.vendor = :vendor and q.isDeleted = 0 Order by q.creationTime desc"),
		@NamedQuery(name = "getCountForAllQueryBuilder", query = "select count(q) from ReportWidget q where q.modifier.userName=:loginuser and q.reportMeasure = 'QUERYBUILDER' and q.vendor = :vendor and q.isDeleted = 0"),
		@NamedQuery(name = "softDeleteWidgetByIds", query = "update ReportWidget r set r.isDeleted=1 where r.id in(:id)"),
		
		@NamedQuery(name = "getAllDistinctDomains", query="select distinct g.domain from ReportWidget g where (g.reportMeasure in (:measure)) and g.generatedType=:generatedType "),
		@NamedQuery(name = "getAllDistinctVendors", query="select distinct g.vendor from ReportWidget g where (g.reportMeasure in (:measure)) and g.generatedType=:generatedType and g.domain like :domain "),
		
		@NamedQuery(name = "getAllDistinctNodes", query="select distinct g.node from ReportWidget g where (g.reportMeasure in (:measure)) and g.generatedType=:generatedType and g.domain like :domain and g.vendor like :vendor "),
		
		@NamedQuery(name = "getAllDistinctLevels", query="select distinct g.reportLevel from ReportWidget g where (g.reportMeasure in (:measure)) and g.generatedType=:generatedType and g.domain like :domain and g.vendor like :vendor and g.duration like :duration and g.frequency like :frequency"),
		@NamedQuery(name = "getAllDistinctGeographys", query="select distinct g.geography from ReportWidget g where (g.reportMeasure in (:measure)) and g.generatedType=:generatedType and g.domain like :domain and g.vendor like :vendor and g.duration like :duration and g.frequency like :frequency and g.reportLevel like :reportLevel"),
		@NamedQuery(name="getPMReportByMeasureAndDomain",query="select u from ReportWidget u where u.reportMeasure in :reportMeasure and u.isDeleted=false and u.domain in (:Domain) and u.status=:status and u.serverInstance !='A31' "
				+ "order by u.id"),
		@NamedQuery(name="getAllTrenScheduledPMReport",query="select u from ReportWidget u where u.reportMeasure in :reportMeasure and u.isDeleted=false and u.domain in (:domain) order by u.modifiedTime "),
		
})

@FilterDefs({ @FilterDef(name = "reportMeasureFilter", parameters = { @ParamDef(name = "reportMeasure", type = "java.lang.String") }), })
@Filters({ @Filter(name = "reportMeasureFilter", condition = "reportMeasure like :reportMeasure"), })

@Entity
@Table(name = "ReportWidget")
@XmlRootElement(name = "ReportWidget")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })

public class ReportWidget implements Serializable { 

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5408284644091234333L;
	
	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "reportWidgetId_PK")
	private Integer id;
	
	/** The report name. */
	@Basic
	@Column(name="reportname")
	private String reportName;
	
	/** The report measure. */
	@Basic
	@Column(name="reportmeasure")
	private String reportMeasure;
	
	/** The report type. */
	@Basic
	@Column(name="reporttype")
	private String reportType;
	
	/** The configuration. */
	@Basic
	@Column(name="configuration")
	@Lob
	private String configuration;
	
	/** The creation time. */
	@Basic
	@Column(name="creationtime")
	private Date creationTime;
	
	/** The report creation time. */
	@Basic
	@Column(name="reportcreationtime")
	private Date reportCreationTime;
	
	/** The modified time. */
	@Basic
	@Column(name="modificationtime")
	private Date modifiedTime;
	
	/** The file path. */
	@Basic
	@Column(name="filepath")
	private String filePath;
	
	
	/** The is deleted. */
	@Basic
	@Column(name="deleted")
	private Boolean isDeleted;
	
	/** The vendor. */
	@Basic
	@Column(name="vendor")
	private String vendor;
	

	/** The domain. */
	@Basic
	@Column(name="domain")
	private String domain;
	
	/** The server instance. */
	@Basic
	@Column(name="serverinstance")
	private String serverInstance;
	
	/** The status. */
	@Enumerated(EnumType.STRING)
	private Status status;
	
	/** The generated report id. */
	@Basic
	@Column(name="generatedreportid")
	private Integer generatedReportId;
	
	@Basic
	@Column(name="downloadrequest")
	private Boolean downloadRequest;
	
	

	/**
	 * The Enum Status.
	 */
	public enum Status{/** The in queque. */
IN_QUEUE,/** The created. */
CREATED,/** The mail sent. */
MAIL_SENT,/** The in progress. */
IN_PROGRESS,/** The not created. */
NOT_CREATED,
In_Progress,
FAILED}
	
	/** The creator. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userid_fk", nullable = false)
	private User creator;
	
	/** The modifier. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "modifierid_fk", nullable = true)
	private User modifier;
	
	/**
	 * Instantiates a new report widget.
	 */
	
	
	/** The configuration. */
	@Basic
	@Column(name="flowconfiguration")
	@Lob
	private String flowConfiguration;
	
	/** The duration. */
	@Basic
	private String duration;
	
	@Basic
	private String frequency;
	
	@Basic
	private String geography;
	
	@Basic
	@Column(name="reportlevel")
	private String reportLevel;
	
	/** The node. */
	@Basic
	private String node;
	
	@Basic
	@Column(name="generatedtype")
	private String generatedType;
	
	public ReportWidget(){
		super();
	}
	
	

	/**
	 * Instantiates a new report widget.
	 *
	 * @param reportName the report name
	 * @param reportMeasure the report measure
	 * @param reportType the report type
	 * @param configuration the configuration
	 * @param creationTime the creation time
	 * @param modifiedTime the modified time
	 * @param reportCreationTime the report creation time
	 */
	public ReportWidget(String reportName, String reportMeasure, String reportType, String configuration,
			Date creationTime,Date modifiedTime,Date reportCreationTime) {
		super();
		this.reportName = reportName;
		this.reportMeasure = reportMeasure;
		this.reportType = reportType;
		this.configuration = configuration;
		this.creationTime=creationTime;
		this.modifiedTime=modifiedTime;
		this.reportCreationTime = reportCreationTime;
	}

	/**
	 * Instantiates a new report widget.
	 *
	 * @param reportName the report name
	 * @param reportMeasure the report measure
	 * @param reportType the report type
	 * @param configuration the configuration
	 * @param createdTime the created time
	 * @param modifiedTime the modified time
	 * @param user the user
	 */
	public ReportWidget(String reportName, String reportMeasure, String reportType, String configuration,
			Date createdTime, Date modifiedTime, User user) {

		super();
		this.reportName = reportName;
		this.reportMeasure = reportMeasure;
		this.reportType = reportType;
		this.configuration = configuration;
		this.creationTime = createdTime;
		this.modifiedTime = modifiedTime;
		this.creator = user;
	}

	/**
	 * Instantiates a new report widget.
	 *
	 * @param reportName the report name
	 * @param reportMeasure the report measure
	 * @param reportType the report type
	 * @param configuration the configuration
	 * @param createdTime the created time
	 * @param reportCreationTime the report creation time
	 * @param modifiedTime the modified time
	 * @param filePath the file path
	 * @param isDeleted the is deleted
	 * @param dataMeasure the data measure
	 * @param creator the creator
	 * @param modifier the modifier
	 * @param id the id
	 */
	/*public ReportWidget(String reportName, String reportMeasure, String reportType, String configuration,
			Date createdTime, Date reportCreationTime, Date modifiedTime, String filePath, Boolean isDeleted,
			DataMeasure dataMeasure, User creator, User modifier, Integer id) {
		super();
		this.reportName = reportName;
		this.reportMeasure = reportMeasure;
		this.reportType = reportType;
		this.configuration = configuration;
		this.creationTime = createdTime;
		this.reportCreationTime = reportCreationTime;
		this.modifiedTime = modifiedTime;
		this.filePath = filePath;
		this.isDeleted = isDeleted;
		this.creator = creator;
		this.modifier = modifier;
		this.id = id;
	}*/

	/**
	 * Instantiates a new report widget.
	 *
	 * @param configuration the configuration
	 * @param reportType the report type
	 * @param reportName the report name
	 * @param reportMeasure the report measure
	 */
	public ReportWidget(String configuration, String reportType, String reportName, String reportMeasure) {
		super();
		this.reportName = reportName;
		this.configuration = configuration;
		this.reportMeasure = reportMeasure;
		this.reportType = reportType;
	}
	
	/**
	 * To report widget wrapper.
	 *
	 * @return the report widget wrapper
	 */
	public ReportWidgetWrapper toReportWidgetWrapper() {
		ReportWidgetWrapper wrapper = new ReportWidgetWrapper(reportName, reportMeasure, reportType, configuration, creationTime, modifiedTime, creator.getUserid(), id,reportCreationTime);
		wrapper.setFilePath(this.filePath);
		wrapper.setStatus(this.status);
		return wrapper;
	}

	/**
	 * To report widget wrapper for coverage improv repo.
	 *
	 * @return the report widget wrapper
	 */
	public ReportWidgetWrapper toReportWidgetWrapperForCoverageImprovRepo() {
		return new ReportWidgetWrapper(reportName, reportMeasure, reportType, configuration, creationTime, modifiedTime, creator.getUserid(), id,reportCreationTime,status);
	}
	
	/**
	 * To geo bond report widget wrapper for pci conflict.
	 *
	 * @return the report widget wrapper
	 */
	public ReportWidgetWrapper toGeoBondReportWidgetWrapperForPciConflict() {
		return new ReportWidgetWrapper(reportName, reportMeasure, reportType, configuration, creationTime, modifiedTime,id,reportCreationTime,status,filePath);
	}
	
	/**
	 * To report widget wrapper for decongestion repo.
	 *
	 * @return the report widget wrapper
	 */
	public ReportWidgetWrapper toReportWidgetWrapperForDecongestionRepo() {
		return new ReportWidgetWrapper(reportName, reportMeasure, reportType, configuration, creationTime, modifiedTime, creator.getUserid(), id,reportCreationTime,filePath);
	}
	
	/**
	 * To report widget wrapper for energy analytics.
	 *
	 * @return the report widget wrapper
	 */
	public ReportWidgetWrapper toReportWidgetWrapperForEnergyAnalytics() {
		return new ReportWidgetWrapper(reportName, reportMeasure, reportType, creationTime, modifiedTime, creator.getUserid(), id,reportCreationTime);
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
	 * Gets the report measure.
	 *
	 * @return the report measure
	 */
	public String getReportMeasure() {
		return reportMeasure;
	}

	/**
	 * Sets the report measure.
	 *
	 * @param reportMeasure the new report measure
	 */
	public void setReportMeasure(String reportMeasure) {
		this.reportMeasure = reportMeasure;
	}

	/**
	 * Gets the report type.
	 *
	 * @return the report type
	 */
	public String getReportType() {
		return reportType;
	}

	/**
	 * Sets the report type.
	 *
	 * @param reportType the new report type
	 */
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	/**
	 * Gets the configuration.
	 *
	 * @return the configuration
	 */
	public String getConfiguration() {
		return configuration;
	}

	/**
	 * Sets the configuration.
	 *
	 * @param configuration the new configuration
	 */
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
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
	 * Gets the modified time.
	 *
	 * @return the modified time
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}

	/**
	 * Sets the modified time.
	 *
	 * @param modifiedTime the new modified time
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	/**
	 * Gets the report creation time.
	 *
	 * @return the report creation time
	 */
	public Date getReportCreationTime() {
		return reportCreationTime;
	}

	/**
	 * Sets the report creation time.
	 *
	 * @param reportCreationTime the new report creation time
	 */
	public void setReportCreationTime(Date reportCreationTime) {
		this.reportCreationTime = reportCreationTime;
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
	 * Gets the checks if is deleted.
	 *
	 * @return the checks if is deleted
	 */
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * Sets the checks if is deleted.
	 *
	 * @param isDeleted the new checks if is deleted
	 */
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
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
	 * @param vendor the new vendor
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
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
	 * @param domain the new domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Gets the server instance.
	 *
	 * @return the server instance
	 */
	public String getServerInstance() {
		return serverInstance;
	}

	/**
	 * Sets the server instance.
	 *
	 * @param serverInstance the new server instance
	 */
	public void setServerInstance(String serverInstance) {
		this.serverInstance = serverInstance;
	}

	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * Gets the generated report id.
	 *
	 * @return the generated report id
	 */
	public Integer getGeneratedReportId() {
		return generatedReportId;
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
	 * Sets the generated report id.
	 *
	 * @param generatedReport_Id the new generated report id
	 */
	public void setGeneratedReportId(Integer generatedReportId) {
		this.generatedReportId = generatedReportId;
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
	 * Gets the modifier.
	 *
	 * @return the modifier
	 */
	public User getModifier() {
		return modifier;
	}

	/**
	 * Sets the modifier.
	 *
	 * @param modifier the new modifier
	 */
	public void setModifier(User modifier) {
		this.modifier = modifier;
	}

	

	public String getFlowConfiguration() {
		return flowConfiguration;
	}



	public void setFlowConfiguration(String flowConfiguration) {
		this.flowConfiguration = flowConfiguration;
	}

	public String getDuration() {
		return duration;
	}


	public void setDuration(String duration) {
		this.duration = duration;
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



	public String getNode() {
		return node;
	}



	public void setNode(String node) {
		this.node = node;
	}


	public String getGeneratedType() {
		return generatedType;
	}


	public void setGeneratedType(String generatedType) {
		this.generatedType = generatedType;
	}
	
	public String getFrequency() {
		return frequency;
	}



	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Boolean getDownloadRequest() {
		return downloadRequest;
	}



	public void setDownloadRequest(Boolean downloadRequest) {
		this.downloadRequest = downloadRequest;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReportWidget [id=");
		builder.append(id);
		builder.append(", reportName=");
		builder.append(reportName);
		builder.append(", reportMeasure=");
		builder.append(reportMeasure);
		builder.append(", reportType=");
		builder.append(reportType);
		builder.append(", configuration=");
		builder.append(configuration);
		builder.append(", creationTime=");
		builder.append(creationTime);
		builder.append(", reportCreationTime=");
		builder.append(reportCreationTime);
		builder.append(", modifiedTime=");
		builder.append(modifiedTime);
		builder.append(", filePath=");
		builder.append(filePath);
		builder.append(", isDeleted=");
		builder.append(isDeleted);
		builder.append(", vendor=");
		builder.append(vendor);
		builder.append(", domain=");
		builder.append(domain);
		builder.append(", serverInstance=");
		builder.append(serverInstance);
		builder.append(", status=");
		builder.append(status);
		builder.append(", generatedReportId=");
		builder.append(generatedReportId);
		builder.append(", downloadRequest=");
		builder.append(downloadRequest);
		builder.append(", creator=");
		builder.append(creator);
		builder.append(", modifier=");
		builder.append(modifier);
		builder.append(", flowConfiguration=");
		builder.append(flowConfiguration);
		builder.append(", duration=");
		builder.append(duration);
		builder.append(", frequency=");
		builder.append(frequency);
		builder.append(", geography=");
		builder.append(geography);
		builder.append(", reportLevel=");
		builder.append(reportLevel);
		builder.append(", node=");
		builder.append(node);
		builder.append(", generatedType=");
		builder.append(generatedType);
		builder.append("]");
		return builder.toString();
	}
	
} 
