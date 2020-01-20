package com.inn.foresight.module.nv.wpt.analytics.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.geography.model.GeographyL3;

/** The Class CategorySiteAggDetail. */


	@NamedQuery(name = "getWPTPieChartData", 
		query = "select new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTStatesWrapper(c.category.categoryName, "
			+ "c.avgResponseTime, c.totalResponseCount, DATE_FORMAT(c.creationDate, '%d%m%y%H%i')) from CategorySiteAggDetail c "
			+ "where DATE(c.creationDate) >= STR_TO_DATE(:fromDate,'%d%m%y') and DATE(c.creationDate) <= STR_TO_DATE(:toDate,'%d%m%y') "
			+ " and c.operator = :operator and c.networkType = :network and c.ipVersion = :configuration and c.operationIdentifier = :identifier "
			+ "and c.avgResponseTime is not null and c.totalResponseCount is not null")
	
	@NamedQuery(name = "getWPTPieChartStats", 
		query = "select new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTStatesWrapper(c.responseTimeStats, "
			+ "DATE_FORMAT(c.creationDate, '%d%m%y')) from CategorySiteAggDetail c  where DATE(c.creationDate) >= STR_TO_DATE(:fromDate,'%d%m%y') "	
			+ "and DATE(c.creationDate) <= STR_TO_DATE(:toDate,'%d%m%y') "
			+ "and c.operator = :operator and c.networkType = :network and c.ipVersion = :configuration "
			+ "and c.operationIdentifier = :identifier and c.category is null")

	@NamedQuery(name = "getWPTHistogramData", 
		query = "select new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTStatesWrapper(c.responseTimeStats, "
			+ "DATE_FORMAT(c.creationDate, '%d%m%y')) from CategorySiteAggDetail c  where DATE(c.creationDate) >= STR_TO_DATE(:fromDate,'%d%m%y') "
			+ "and DATE(c.creationDate) <= STR_TO_DATE(:toDate,'%d%m%y') "
			+ "and c.operator = :operator and c.networkType = :network and c.ipVersion = :configuration "
			+ "and c.operationIdentifier = :identifier and c.category is null")
	
	@NamedQuery(name = "getWPTScatterPlotData", 
		query = "select new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTStatesWrapper(c.scatterCharts, DATE_FORMAT(c.creationDate, '%d%m%y')) "
			+ "from CategorySiteAggDetail c  where DATE(c.creationDate) >= STR_TO_DATE(:fromDate,'%d%m%y') "
			+ "and DATE(c.creationDate) <= STR_TO_DATE(:toDate,'%d%m%y') "
			+ "and c.operator = :operator and c.networkType = :network and c.ipVersion = :configuration "
			+ "and c.operationIdentifier = :identifier and c.category is null")
	
	@NamedQuery(name = "getWPTSummaryData", 
		query = "select DATE(c.creationDate),c.website.siteName,(SUM(c.avgResponseTime*c.totalResponseCount)/SUM(c.totalResponseCount)),((SUM(c.successRate)/SUM(c.totalResponseCount))*100),c.ipVersion from CategorySiteAggDetail c "
			+ "where c.geographyL3 is null and c.operator is null and c.networkType is null and Date(c.creationDate) "
			+ "between DATE(STR_TO_DATE(:fromDate,'%d%m%Y')) and DATE(STR_TO_DATE(:toDate,'%d%m%Y')) and c.operationIdentifier = :identifier "
			+ "and UPPER(c.ipVersion) in ('IPV4','IPV6') group by DATE(c.creationDate),c.website.siteName, c.ipVersion order by DATE(c.creationDate),c.website.siteName")
	@NamedQuery(name = "getWPTDetailedViewDataCategoryWise" , query = "SELECT new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDetailedViewWrapper(c.category.id,SUM(c.totalCount),SUM(c.avgFirstDNSResolutionTime*c.totalFirstDNSTimeCount) / SUM(c.totalFirstDNSTimeCount),SUM(c.avgFirstByteResponseTime*c.totalFirstByteTimeCount) / SUM(c.totalFirstByteTimeCount), SUM(c.avgResponseTime*c.totalResponseCount) / SUM(c.totalResponseCount), SUM(c.avgTotalDNSResolutionTime*c.totalDNSTimeCount) / SUM(c.totalDNSTimeCount), SUM(c.avgPingTime*c.totalPingCount) / SUM(c.totalPingCount)) FROM CategorySiteAggDetail c where DATE(c.creationDate) between STR_TO_DATE(:fromDate,'%d%m%Y') and STR_TO_DATE(:toDate,'%d%m%Y') and c.operator=:operator and c.networkType=:network and c.ipVersion = :configuration and c.operationIdentifier = :identifier group by c.category.id")
	@NamedQuery(name = "getWPTDetailedViewDataSiteWise" , query = "select new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDetailedViewWrapper(c.website.siteName,SUM(c.totalCount),SUM(c.avgFirstDNSResolutionTime*c.totalFirstDNSTimeCount) / SUM(c.totalFirstDNSTimeCount),SUM(c.avgFirstByteResponseTime*c.totalFirstByteTimeCount) / SUM(c.totalFirstByteTimeCount), SUM(c.avgResponseTime*c.totalResponseCount) / SUM(c.totalResponseCount), SUM(c.avgTotalDNSResolutionTime*c.totalDNSTimeCount) / SUM(c.totalDNSTimeCount), SUM(c.avgPingTime*c.totalPingCount) / SUM(c.totalPingCount)) FROM CategorySiteAggDetail c where DATE(c.creationDate) between DATE(STR_TO_DATE(:fromDate,'%d%m%Y')) and DATE(STR_TO_DATE(:toDate,'%d%m%Y')) and c.operationIdentifier=:identifier and c.operator=:operator and c.networkType=:network and c.ipVersion = :configuration group by c.website.siteName")

	
	@FilterDef(name = "getDataForGivenGeographyL3", parameters = {
			@ParamDef(name = "geographyL3", type = "java.lang.String")})
	
	@FilterDef(name = "getDataForALLGeographyL3", parameters = {})
	
	@FilterDef(name = "getDataForGivenSite", parameters = {
			@ParamDef(name = "site", type = "java.lang.String")})
		

@Filters(value = {
	
		@Filter(name = "getDataForALLGeographyL3", condition = "categorySiteAggDetailId_PK in (select cs.categorySiteAggDetailId_PK from "
				+ "CategorySiteAggDetail cs where cs.geographyl3id_fk is null)"),
	
		@Filter(name = "getDataForGivenGeographyL3", condition = "categorySiteAggDetailId_PK in (select cs.categorySiteAggDetailId_PK from "
				+ "CategorySiteAggDetail cs inner join GeographyL3 g on cs.geographyl3id_fk = g.GeographyL3Id_pk where g.name = :geographyL3)"),
		
		@Filter(name = "getDataForGivenSite", condition = "categorySiteAggDetailId_PK in (select cs.categorySiteAggDetailId_PK from "
				+ "CategorySiteAggDetail cs inner join Website s on cs.websiteId_FK = s.websiteId_PK where UPPER(s.siteName) like concat('%',:site,'%'))"),
	
})



@Entity
@Table(name = "CategorySiteAggDetail")
@XmlRootElement(name = "CategorySiteAggDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class CategorySiteAggDetail implements Serializable{

	/** The Enum AggregationType. */
	public enum AggregationType {
		
		/** The site. */
		SITE, 
 /** The category. */
 CATEGORY
	}
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The id. */
	@Id
	@Column(name = "categorysiteaggdetailid_pk")
	private Integer id;
	
	/** The creation date. */
	@Basic
	@Column(name = "creationtime")
	private Date creationDate;
	
	/** The website. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "websiteid_fk", nullable = false)
	private Website website;
	
	/** The category. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "webcategoryid_fk", nullable = false)
	private WebCategory category;
	
	/** The geography L 3. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "geographyl3id_fk", nullable = false)
	private GeographyL3 geographyL3;
	
	/** The operation identifier. */
	@Basic
	@Column(name = "operationidentifier")
	@Enumerated(EnumType.STRING)
	private AggregationType operationIdentifier;
	
	/** The operator. */
	@Basic
	private String operator;
	
	/** The network type. */
	@Basic
	@Column(name = "networktype")
	private String networkType;
	
	/** The ip version. */
	@Basic
	@Column(name = "ipversion")
	private String ipVersion;
	
	/** The response time stats. */
	@Basic
	@Column(name = "responsetimestats")
	private String responseTimeStats;
	
	/** The scatter charts. */
	@Basic
	@Column(name = "scattercharts")
	private String scatterCharts;
	
	/** The avg response time. */
	@Basic
	@Column(name = "avgresponsetime")
	private Float avgResponseTime;
	
	/** The total response count. */
	@Basic
	@Column(name = "totalresponsecount")
	private Integer totalResponseCount;
	
	/** The avg first DNS resolution time. */
	@Basic
	@Column(name = "avgfirstdnsresolutiontime")
	private Float avgFirstDNSResolutionTime;
	
	/** The total first DNS time count. */
	@Basic
	@Column(name = "totalfirstdnstimecount")
	private Integer totalFirstDNSTimeCount;
	
	/** The avg total DNS resolution time. */
	@Basic
	@Column(name = "avgtotaldnsresolutiontime")
	private Float avgTotalDNSResolutionTime;
	
	/** The total DNS time count. */
	@Basic
	@Column(name = "totaldnstimecount")
	private Integer totalDNSTimeCount;

	/** The avg first byte response time. */
	@Basic
	@Column(name = "avgfirstbyteresponsetime")
	private Float avgFirstByteResponseTime;
	
	/** The total first byte time count. */
	@Basic
	@Column(name = "totalfirstbytetimecount")
	private Integer totalFirstByteTimeCount;
	
	/** The avg ping time. */
	@Basic
	@Column(name = "avgpingtime")
	private Float avgPingTime;
	
	/** The total ping count. */
	@Basic
	@Column(name = "totalpingcount")
	private Integer totalPingCount;
	
	/** The success rate. */
	@Basic
	@Column(name = "successrate")
	private Float successRate;
	
	/** The total count. */
	@Basic
	@Column(name = "totalcount")
	private Integer totalCount;

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
	 * Gets the creation date.
	 *
	 * @return the creation date
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets the creation date.
	 *
	 * @param creationDate the new creation date
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Gets the website.
	 *
	 * @return the website
	 */
	public Website getWebsite() {
		return website;
	}

	/**
	 * Sets the website.
	 *
	 * @param website the new website
	 */
	public void setWebsite(Website website) {
		this.website = website;
	}

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public WebCategory getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
	public void setCategory(WebCategory category) {
		this.category = category;
	}
	
	/**
	 * Gets the geography L 3.
	 *
	 * @return the geography L 3
	 */
	public GeographyL3 getGeographyL3() {
		return geographyL3;
	}
	
	/**
	 * Sets the geography L 3.
	 *
	 * @param geographyL3 the new geography L 3
	 */
	public void setGeographyL3(GeographyL3 geographyL3) {
		this.geographyL3 = geographyL3;
	}

	/**
	 * Gets the operation identifier.
	 *
	 * @return the operation identifier
	 */
	public AggregationType getOperationIdentifier() {
		return operationIdentifier;
	}

	/**
	 * Sets the operation identifier.
	 *
	 * @param operationIdentifier the new operation identifier
	 */
	public void setOperationIdentifier(AggregationType operationIdentifier) {
		this.operationIdentifier = operationIdentifier;
	}

	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * Sets the operator.
	 *
	 * @param operator the new operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * Gets the response time stats.
	 *
	 * @return the response time stats
	 */
	public String getResponseTimeStats() {
		return responseTimeStats;
	}

	/**
	 * Sets the response time stats.
	 *
	 * @param responseTimeStats the new response time stats
	 */
	public void setResponseTimeStats(String responseTimeStats) {
		this.responseTimeStats = responseTimeStats;
	}

	/**
	 * Gets the scatter charts.
	 *
	 * @return the scatter charts
	 */
	public String getScatterCharts() {
		return scatterCharts;
	}

	/**
	 * Sets the scatter charts.
	 *
	 * @param scatterCharts the new scatter charts
	 */
	public void setScatterCharts(String scatterCharts) {
		this.scatterCharts = scatterCharts;
	}

	/**
	 * Gets the avg response time.
	 *
	 * @return the avg response time
	 */
	public Float getAvgResponseTime() {
		return avgResponseTime;
	}

	/**
	 * Sets the avg response time.
	 *
	 * @param avgResponseTime the new avg response time
	 */
	public void setAvgResponseTime(Float avgResponseTime) {
		this.avgResponseTime = avgResponseTime;
	}

	/**
	 * Gets the total response count.
	 *
	 * @return the total response count
	 */
	public Integer getTotalResponseCount() {
		return totalResponseCount;
	}

	/**
	 * Sets the total response count.
	 *
	 * @param totalResponseCount the new total response count
	 */
	public void setTotalResponseCount(Integer totalResponseCount) {
		this.totalResponseCount = totalResponseCount;
	}

	/**
	 * Gets the avg first DNS resolution time.
	 *
	 * @return the avg first DNS resolution time
	 */
	public Float getAvgFirstDNSResolutionTime() {
		return avgFirstDNSResolutionTime;
	}

	/**
	 * Sets the avg first DNS resolution time.
	 *
	 * @param avgFirstDNSResolutionTime the new avg first DNS resolution time
	 */
	public void setAvgFirstDNSResolutionTime(Float avgFirstDNSResolutionTime) {
		this.avgFirstDNSResolutionTime = avgFirstDNSResolutionTime;
	}

	/**
	 * Gets the total first DNS time count.
	 *
	 * @return the total first DNS time count
	 */
	public Integer getTotalFirstDNSTimeCount() {
		return totalFirstDNSTimeCount;
	}

	/**
	 * Sets the total first DNS time count.
	 *
	 * @param totalFirstDNSTimeCount the new total first DNS time count
	 */
	public void setTotalFirstDNSTimeCount(Integer totalFirstDNSTimeCount) {
		this.totalFirstDNSTimeCount = totalFirstDNSTimeCount;
	}

	/**
	 * Gets the avg total DNS resolution time.
	 *
	 * @return the avg total DNS resolution time
	 */
	public Float getAvgTotalDNSResolutionTime() {
		return avgTotalDNSResolutionTime;
	}

	/**
	 * Sets the avg total DNS resolution time.
	 *
	 * @param avgTotalDNSResolutionTime the new avg total DNS resolution time
	 */
	public void setAvgTotalDNSResolutionTime(Float avgTotalDNSResolutionTime) {
		this.avgTotalDNSResolutionTime = avgTotalDNSResolutionTime;
	}

	/**
	 * Gets the total DNS time count.
	 *
	 * @return the total DNS time count
	 */
	public Integer getTotalDNSTimeCount() {
		return totalDNSTimeCount;
	}

	/**
	 * Sets the total DNS time count.
	 *
	 * @param totalDNSTimeCount the new total DNS time count
	 */
	public void setTotalDNSTimeCount(Integer totalDNSTimeCount) {
		this.totalDNSTimeCount = totalDNSTimeCount;
	}

	/**
	 * Gets the avg first byte response time.
	 *
	 * @return the avg first byte response time
	 */
	public Float getAvgFirstByteResponseTime() {
		return avgFirstByteResponseTime;
	}

	/**
	 * Sets the avg first byte response time.
	 *
	 * @param avgFirstByteResponseTime the new avg first byte response time
	 */
	public void setAvgFirstByteResponseTime(Float avgFirstByteResponseTime) {
		this.avgFirstByteResponseTime = avgFirstByteResponseTime;
	}

	/**
	 * Gets the total first byte time count.
	 *
	 * @return the total first byte time count
	 */
	public Integer getTotalFirstByteTimeCount() {
		return totalFirstByteTimeCount;
	}

	/**
	 * Sets the total first byte time count.
	 *
	 * @param totalFirstByteTimeCount the new total first byte time count
	 */
	public void setTotalFirstByteTimeCount(Integer totalFirstByteTimeCount) {
		this.totalFirstByteTimeCount = totalFirstByteTimeCount;
	}

	/**
	 * Gets the avg ping time.
	 *
	 * @return the avg ping time
	 */
	public Float getAvgPingTime() {
		return avgPingTime;
	}

	/**
	 * Sets the avg ping time.
	 *
	 * @param avgPingTime the new avg ping time
	 */
	public void setAvgPingTime(Float avgPingTime) {
		this.avgPingTime = avgPingTime;
	}

	/**
	 * Gets the total ping count.
	 *
	 * @return the total ping count
	 */
	public Integer getTotalPingCount() {
		return totalPingCount;
	}

	/**
	 * Sets the total ping count.
	 *
	 * @param totalPingCount the new total ping count
	 */
	public void setTotalPingCount(Integer totalPingCount) {
		this.totalPingCount = totalPingCount;
	}

	/**
	 * Gets the network type.
	 *
	 * @return the network type
	 */
	public String getNetworkType() {
		return networkType;
	}

	/**
	 * Sets the network type.
	 *
	 * @param networkType the new network type
	 */
	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	/**
	 * Gets the ip version.
	 *
	 * @return the ip version
	 */
	public String getIpVersion() {
		return ipVersion;
	}

	/**
	 * Sets the ip version.
	 *
	 * @param ipVersion the new ip version
	 */
	public void setIpVersion(String ipVersion) {
		this.ipVersion = ipVersion;
	}

	/**
	 * Gets the success rate.
	 *
	 * @return the success rate
	 */
	public Float getSuccessRate() {
		return successRate;
	}

	/**
	 * Sets the success rate.
	 *
	 * @param successRate the new success rate
	 */
	public void setSuccessRate(Float successRate) {
		this.successRate = successRate;
	}

	/**
	 * Gets the total count.
	 *
	 * @return the total count
	 */
	public Integer getTotalCount() {
		return totalCount;
	}
	
	/**
	 * Sets the total count.
	 *
	 * @param totalCount the new total count
	 */
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	
}
