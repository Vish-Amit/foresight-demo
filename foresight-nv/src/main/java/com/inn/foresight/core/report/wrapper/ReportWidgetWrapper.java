package com.inn.foresight.core.report.wrapper;

import java.util.Date;

import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.core.report.model.ReportWidget;
import com.inn.foresight.core.report.model.ReportWidget.Status;


/**
 * The Class ReportWidgetWrapper.
 */
@RestWrapper
public class ReportWidgetWrapper {

	/** The report name. */
	private String reportName;
	
	/** The report measure. */
	private String reportMeasure;
	
	/** The report type. */
	private String reportType;
	
	/** The configuration. */
	private String configuration;
	
	/** The creation time. */
	private Date creationTime;
	
	/** The modified time. */
	private Date modifiedTime;
	
	/** The creator. */
	private Integer creator;
	
	/** The data measure. */
	private Integer dataMeasure;
	
	/** The id. */
	private Integer id;
	
	/** The server instance. */
	private String serverInstance;
	
	/** The report creation time. */
	private Long reportCreationTime;
    
    /** The status. */
    private Status status; 
    
    /** The file path. */
    private String filePath;
    
    /** The pm id. */
    private Long pmId ;
    
    /** The domain. */
    private String domain;
    
    /** The vendor. */
    private String vendor;

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
	 * Gets the pm id.
	 *
	 * @return the pm id
	 */
	public Long getPmId() {
		return pmId;
	}

	/**
	 * Sets the pm id.
	 *
	 * @param pmId the new pm id
	 */
	public void setPmId(Long pmId) {
		this.pmId = pmId;
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
	 * Gets the creator.
	 *
	 * @return the creator
	 */
	public Integer getCreator() {
		return creator;
	}

	/**
	 * Sets the creator.
	 *
	 * @param creator the new creator
	 */
	public void setCreator(Integer creator) {
		this.creator = creator;
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
	 * Gets the data measure.
	 *
	 * @return the data measure
	 */
	public Integer getDataMeasure() {
		return dataMeasure;
	}

	/**
	 * Sets the data measure.
	 *
	 * @param dataMeasure the new data measure
	 */
	public void setDataMeasure(Integer dataMeasure) {
		this.dataMeasure = dataMeasure;
	}

	/**
	 * Sets the report creation time.
	 *
	 * @param reportCreationTime the new report creation time
	 */
	public void setReportCreationTime(Long reportCreationTime) {
		this.reportCreationTime = reportCreationTime;
	}
	
	/**
	 * Gets the report creation time.
	 *
	 * @return the report creation time
	 */
	public Long getReportCreationTime() {
		return reportCreationTime;
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
	 * Instantiates a new report widget wrapper.
	 *
	 * @param reportName the report name
	 * @param reportMeasure the report measure
	 * @param reportType the report type
	 * @param configuration the configuration
	 * @param creationTime the creation time
	 * @param modifiedTime the modified time
	 * @param creator the creator
	 * @param id the id
	 * @param reportCreationTime the report creation time
	 */
	public ReportWidgetWrapper(String reportName, String reportMeasure, String reportType, String configuration, Date creationTime, Date modifiedTime, Integer creator, Integer id,Date reportCreationTime) {
		super();
		this.reportName = reportName;
		this.reportMeasure = reportMeasure;
		this.reportType = reportType;
		this.configuration = configuration;
		this.creationTime = creationTime;
		this.modifiedTime = modifiedTime;
		this.creator = creator;
		this.id = id;
		if(reportCreationTime!=null)
			this.reportCreationTime=reportCreationTime.getTime();
	}


	/**
	 * Instantiates a new report widget wrapper.
	 *
	 * @param reportName the report name
	 * @param reportMeasure the report measure
	 * @param reportType the report type
	 * @param configuration the configuration
	 * @param creationTime the creation time
	 * @param modifiedTime the modified time
	 * @param creator the creator
	 * @param id the id
	 * @param reportCreationTime the report creation time
	 * @param filePath the file path
	 */
	// constructor used in Ran Configuration Dump report and also in query getReportsForUserByReportMeasures
	public ReportWidgetWrapper(String reportName, String reportMeasure, String reportType, String configuration, Date creationTime, Date modifiedTime, 
			Integer creator, Integer id,Date reportCreationTime, String filePath) {
		super();
		this.reportName = reportName;
		this.reportMeasure = reportMeasure;
		this.reportType = reportType;
		this.configuration = configuration;
		this.creationTime = creationTime;
		this.modifiedTime = modifiedTime;
		this.creator = creator;
		this.id = id;
		this.filePath = filePath;
		if(reportCreationTime!=null)
			this.reportCreationTime=reportCreationTime.getTime();
	}

	/**
	 * Instantiates a new report widget wrapper.
	 *
	 * @param reportName the report name
	 * @param reportMeasure the report measure
	 * @param reportType the report type
	 * @param configuration the configuration
	 * @param creationTime the creation time
	 * @param modifiedTime the modified time
	 * @param creator the creator
	 * @param id the id
	 * @param reportCreationTime the report creation time
	 * @param filePath the file path
	 * @param status the status
	 */
	//Building Coverage Netvelocity
	public ReportWidgetWrapper(String reportName,String reportMeasure,String reportType,String configuration,Date creationTime,Date modifiedTime,Integer creator,Integer id,Date reportCreationTime,String filePath,Status status){
		super();
		this.reportName = reportName;
		this.reportMeasure = reportMeasure;
		this.reportType = reportType;
		this.configuration = configuration;
		this.creationTime = creationTime;
		this.modifiedTime = modifiedTime;
		this.creator = creator;
		this.id = id;
		if(reportCreationTime!=null){
			this.reportCreationTime =reportCreationTime.getTime();
		}
		this.filePath = filePath;
		this.status = status;
	}
	
	/**
	 * Instantiates a new report widget wrapper.
	 *
	 * @param reportName the report name
	 * @param reportMeasure the report measure
	 * @param reportType the report type
	 * @param configuration the configuration
	 * @param creationTime the creation time
	 * @param modifiedTime the modified time
	 * @param creator the creator
	 * @param id the id
	 */
	public ReportWidgetWrapper(String reportName, String reportMeasure, String reportType, String configuration, Date creationTime, Date modifiedTime, Integer creator, Integer id) {
		super();
		this.reportName = reportName;
		this.reportMeasure = reportMeasure;
		this.reportType = reportType;
		this.configuration = configuration;
		this.creationTime = creationTime;
		this.modifiedTime = modifiedTime;
		this.creator = creator;
		this.id = id;
	}
	
	/**
	 * Instantiates a new report widget wrapper.
	 *
	 * @param reportName the report name
	 * @param reportMeasure the report measure
	 * @param reportType the report type
	 * @param configuration the configuration
	 * @param creationTime the creation time
	 * @param modifiedTime the modified time
	 * @param creator the creator
	 * @param id the id
	 * @param dataMeasure the data measure
	 */
	public ReportWidgetWrapper(String reportName, String reportMeasure, String reportType, String configuration, Date creationTime, Date modifiedTime, Integer creator, Integer id,
			Integer dataMeasure) {
		super();
		this.reportName = reportName;
		this.reportMeasure = reportMeasure;
		this.reportType = reportType;
		this.configuration = configuration;
		this.creationTime = creationTime;
		this.modifiedTime = modifiedTime;
		this.creator = creator;
		this.id = id;
		this.dataMeasure = dataMeasure;
		
	}
	
	

	/**
	 * Instantiates a new report widget wrapper.
	 *
	 * @param reportName the report name
	 * @param reportMeasure the report measure
	 * @param configuration the configuration
	 */
	public ReportWidgetWrapper(String reportName, String reportMeasure, String configuration) {
		super();
		this.reportName = reportName;
		this.reportMeasure = reportMeasure;
		this.configuration = configuration;
	}
	
	/**
	 * Instantiates a new report widget wrapper.
	 *
	 * @param reportName the report name
	 * @param reportMeasure the report measure
	 * @param reportType the report type
	 * @param configuration the configuration
	 * @param creationTime the creation time
	 * @param modifiedTime the modified time
	 * @param creator the creator
	 * @param id the id
	 * @param reportCreationTime the report creation time
	 * @param filePath the file path
	 * @param domain the domain
	 */
	public ReportWidgetWrapper(String reportName, String reportMeasure,
			String reportType, String configuration, Date creationTime,
			Date modifiedTime, Integer creator, Integer id,
			Date reportCreationTime, String filePath, String domain) {
		super();
		this.reportName = reportName;
		this.reportMeasure = reportMeasure;
		this.reportType = reportType;
		this.configuration = configuration;
		this.creationTime = creationTime;
		this.modifiedTime = modifiedTime;
		this.creator = creator;
		this.id = id;
		this.filePath = filePath;
		this.domain = domain;
		if(reportCreationTime!=null)
			this.reportCreationTime=reportCreationTime.getTime();
	}

	
	/**
	 * Instantiates a new report widget wrapper.
	 */
	public ReportWidgetWrapper() {
		super();

	}

	/**
	 * Instantiates a new report widget wrapper.
	 *
	 * @param reportName the report name
	 * @param reportMeasure the report measure
	 * @param reportType the report type
	 * @param configuration the configuration
	 * @param creationTime the creation time
	 * @param modifiedTime the modified time
	 * @param creator the creator
	 * @param id the id
	 * @param reportCreationTime the report creation time
	 * @param status the status
	 */
	/*
	 * This constructor is used in namedQuery - getConfiguraionAuditForUser in ReportWidget.java Class
	 * 
	 */
	public ReportWidgetWrapper(String reportName, String reportMeasure, String reportType,
			String configuration, Date creationTime, Date modifiedTime, Integer creator , Integer id,
			Date reportCreationTime, Status status) {
		super();
		this.reportName = reportName;
		this.reportMeasure = reportMeasure;
		this.reportType = reportType;
		this.configuration = configuration;
		this.creationTime = creationTime;
		this.modifiedTime = modifiedTime;
		this.creator = creator;
		this.id = id;
		if(reportCreationTime!=null)
			this.reportCreationTime=reportCreationTime.getTime();
		
		this.status=status;
		
	}
	
	//pciconflict
		/**
	 * Instantiates a new report widget wrapper.
	 *
	 * @param reportName the report name
	 * @param reportMeasure the report measure
	 * @param reportType the report type
	 * @param configuration the configuration
	 * @param creationTime the creation time
	 * @param modifiedTime the modified time
	 * @param id the id
	 * @param reportCreationTime the report creation time
	 * @param status the status
	 * @param filePath the file path
	 */
	//reportName, reportMeasure, reportType, configuration, creationTime, modifiedTime,id,reportCreationTime,status,filePath
		public ReportWidgetWrapper(String reportName, String reportMeasure,
				String reportType, String configuration, Date creationTime,
				Date modifiedTime, Integer id, Date reportCreationTime,Status status,String filePath) {
			super();
			this.reportName = reportName;
			this.reportMeasure = reportMeasure;
			this.reportType = reportType;
			this.configuration = configuration;
			this.creationTime = creationTime;
			this.modifiedTime = modifiedTime;
			this.id = id;
			if ( reportCreationTime !=null) {
				this.reportCreationTime = reportCreationTime.getTime();
			}
			this.status=status;
			this.filePath=filePath;
		}
	
	/**
	 * Instantiates a new report widget wrapper.
	 *
	 * @param reportName the report name
	 * @param reportMeasure the report measure
	 * @param reportType the report type
	 * @param configuration the configuration
	 * @param creationTime the creation time
	 * @param modifiedTime the modified time
	 * @param creator the creator
	 * @param id the id
	 * @param reportCreationTime the report creation time
	 * @param status the status
	 * @param filePath the file path
	 */
	//ODSC REPORT
	public ReportWidgetWrapper(String reportName, String reportMeasure, String reportType,
			String configuration, Date creationTime, Date modifiedTime, Integer creator , Integer id,
			Date reportCreationTime, Status status,String filePath) {
		super();
		this.reportName = reportName;
		this.reportMeasure = reportMeasure;
		this.reportType = reportType;
		this.configuration = configuration;
		this.creationTime = creationTime;
		this.modifiedTime = modifiedTime;
		this.creator = creator;
		this.id = id;
		if(reportCreationTime!=null)
			this.reportCreationTime=reportCreationTime.getTime();
		
		this.filePath=filePath;
		this.status=status;
		
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
	 * To report widget.
	 *
	 * @return the report widget
	 */
	public ReportWidget toReportWidget() {
		if(reportCreationTime!=null){
			ReportWidget reportWidget = new ReportWidget(reportName, reportMeasure, reportType, configuration,creationTime,modifiedTime,new Date(reportCreationTime));
			reportWidget.setFilePath(this.filePath);
			reportWidget.setDomain(this.domain);
			reportWidget.setServerInstance(this.serverInstance);
			reportWidget.setStatus(this.status);
			reportWidget.setVendor(this.vendor);
			return reportWidget;
		}else{
			ReportWidget reportWidget =new ReportWidget(reportName, reportMeasure, reportType, configuration,creationTime,modifiedTime,new Date());
			reportWidget.setFilePath(this.filePath);
			return reportWidget;
		}	
	}
	
	/**
	 * Instantiates a new report widget wrapper.
	 *
	 * @param reportName the report name
	 * @param filePath the file path
	 * @param pmId the pm id
	 * @param creationTime the creation time
	 */
	public ReportWidgetWrapper(String reportName, String filePath, Long pmId,Date creationTime) {
		super();
		this.reportName = reportName;
		this.filePath = filePath;
		this.pmId = pmId;
		this.creationTime = creationTime;
	}
	
	/**
	 * Instantiates a new report widget wrapper.
	 *
	 * @param reportName the report name
	 * @param reportMeasure the report measure
	 * @param reportType the report type
	 * @param creationTime the creation time
	 * @param modifiedTime the modified time
	 * @param creator the creator
	 * @param id the id
	 * @param reportCreationTime the report creation time
	 */
	public ReportWidgetWrapper(String reportName,String reportMeasure,String reportType,Date creationTime, Date modifiedTime,Integer creator,Integer id, Date reportCreationTime){
	this.reportName=reportName;
	this.reportMeasure=reportMeasure;
	this.reportType = reportType;
	this.creationTime =creationTime;
	this.modifiedTime = modifiedTime;
	this.creator =creator;
	this.id = id;
	if(reportCreationTime!=null)
		this.reportCreationTime=reportCreationTime.getTime();
	}
	
}