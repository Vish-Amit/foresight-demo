package com.inn.foresight.module.nv.wpt.analytics.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.inn.foresight.module.nv.wpt.analytics.constants.WPTAnalyticsConstants;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;

/** The Class WPTDashboardDetail. */

	@NamedQuery(name = "getTargetIpVersionCount", 
			query = "select w.ipv4Count, w.ipv6Count,w.totalCount,w.id from WPTDashboardDetail w where DATE(w.recordTime) = STR_TO_DATE(:date,'%d%m%y') "
			+ "and w.processType = :processType and w.operator = :operator and w.technology = :technology "
			+ "and w.recordType = 'SITEWISE' and w.website is null and w.imeiCount is null")
	
	@NamedQuery(name = "getTotalRecordsCounts", 
		query = "select new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDayWiseCountWrapper(DATE_FORMAT(w.recordTime, '%d%m%y'), "
			+ "w.totalCount) from WPTDashboardDetail w where w.operator = :operator and w.technology = :technology "
			+ "and w.recordType = 'SITEWISE' and w.website is null and DATE_FORMAT(w.recordTime, '%d%m%y') in :timeConstantList "
			+ "and w.processType = :processType and w.imeiCount is null")
	
	@NamedQuery(name = "getTotalDeviceCounts", 
		query = "select new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDayWiseCountWrapper(DATE_FORMAT(w.recordTime, '%d%m%y'), "
			+ "w.imeiCount) from WPTDashboardDetail w where w.operator = :operator and w.technology = :technology and w.recordType = 'SITEWISE' "
			+ "and w.website is null and DATE_FORMAT(w.recordTime, '%d%m%y') in :timeConstantList and w.processType = :processType "
			+ "and w.imeiCount is not null")
	
	@NamedQuery(name = "getTestedURLCounts", 
		query = "select new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDayWiseCountWrapper(DATE_FORMAT(w.recordTime, '%d%m%y'), "
			+ "count(distinct w.website.id)) from WPTDashboardDetail w where w.operator = :operator and w.technology = :technology "
			+ "and w.recordType = 'SITEWISE' and DATE_FORMAT(w.recordTime, '%d%m%y') in :timeConstantList and w.processType = :processType "
			+ "group by DATE_FORMAT(w.recordTime, '%d%m%y')")

	@NamedQuery(name = "getTraceRouteAndTTFBData", 
		query = "select new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDashboardKpiWrapper(DATE_FORMAT(w.recordTime, '%d%m%y'), "
			+ "(sum(avgTTFB * ttfbCount) / sum(ttfbCount)), (sum(avgTDNS * tdnsCount) / sum(tdnsCount)), w.operator, w.ipVersion, "
			+ "w.geographyL1.id, w.geographyL2.id, w.geographyL3.id, w.geographyL4.id) "
			+ "from WPTDashboardDetail w where w.technology = (:technology) and w.category.id in (:category) "
			+ "and w.location.id in (:location) and w.recordType = 'CATEGORYWISE' "
			+ "and DATE_FORMAT(w.recordTime, '%d%m%y') in (:timeConstantList) and w.processType = :processType "
			+ "group by DATE_FORMAT(w.recordTime, '%d%m%y'), w.geographyL1.id, w.geographyL2.id, w.geographyL3.id, w.geographyL4.id, "
			+ "w.operator, w.ipVersion order by DATE_FORMAT(w.recordTime, '%d%m%y')")
	
	@NamedQuery(name = "getTTFBAndTDNSData", 
		query = "select new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDashboardKpiWrapper(DATE_FORMAT(w.recordTime, '%d%m%y'), "
			+ "(sum(avgTraceTime * traceTimeCount) / sum(traceTimeCount)),  (sum(avgTTFB * ttfbCount) / sum(ttfbCount)), w.operator, w.ipVersion, "
			+ "w.geographyL1.id, w.geographyL2.id, w.geographyL3.id, w.geographyL4.id) "
			+ "from WPTDashboardDetail w where w.technology = (:technology) and w.category.id in (:category) "
			+ "and w.location.id in (:location) and w.recordType = 'CATEGORYWISE' "
			+ "and DATE_FORMAT(w.recordTime, '%d%m%y') in (:timeConstantList) and w.processType = :processType "
			+ "group by DATE_FORMAT(w.recordTime, '%d%m%y'), w.geographyL1.id, w.geographyL2.id, w.geographyL3.id, w.geographyL4.id, "
			+ "w.operator, w.ipVersion order by DATE_FORMAT(w.recordTime, '%d%m%y')")
	
	@NamedQuery(name = "getTTFBCategoryWiseData", 
		query = "select new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDashboardKpiWrapper(DATE_FORMAT(w.recordTime, '%d%m%y'), "
			+ "min(minTTFB),  max(maxTTFB), w.category.categoryName) "
			+ "from WPTDashboardDetail w where w.technology = (:technology) and w.category.id in (:category) "
			+ "and w.location.id in (:location) and w.recordType = 'CATEGORYWISE' "
			+ "and DATE_FORMAT(w.recordTime, '%d%m%y') in (:timeConstantList) and w.processType = :processType "
			+ "group by DATE_FORMAT(w.recordTime, '%d%m%y'), w.category.categoryName order by DATE_FORMAT(w.recordTime, '%d%m%y')")

	@NamedQuery(name = "getTopTestedURLCounts", 
		query = "select new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.TopURLDetailWrapper(w.totalCount, w.website.siteName,"
			+ "w.avgHopCount, w.avgTTL) from WPTDashboardDetail w where DATE(w.recordTime) = STR_TO_DATE(:date, '%d%m%y') and w.processType = :processType and " 
			+ "w.operator = :operator and w.technology = :technology and w.recordType = 'SITEWISE' and w.website is not null order by w.totalCount desc")
	
	@NamedQuery(name = "getTopPingCounts", 
		query = "select new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.TopURLDetailWrapper(w.pingCount, w.website.siteName) "
			+ "from WPTDashboardDetail w where DATE(w.recordTime) = STR_TO_DATE(:date, '%d%m%y') and w.processType = :processType and " 
			+ "w.operator = :operator and w.technology = :technology and w.recordType = 'SITEWISE' and w.website is not null "
			+ "and w.pingCount is not null order by w.pingCount desc")
	
	@NamedQuery(name = "getTopTestedURLDetails", 
		query = "select new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.TopURLDetailWrapper(w.website.siteName, w.totalCount, w.avgTTL, w.avgTTFB,"
			+ "w.avgFDNS,w.avgTDNS,w.avgPing,w.avgPageSize) from WPTDashboardDetail w where DATE(w.recordTime) = STR_TO_DATE(:date, '%d%m%y') and w.processType = :processType "
			+ "and w.operator = :operator and w.technology = :technology and w.recordType = 'SITEWISE' and w.website is not null order by w.totalCount desc")
	
	@NamedQuery(name = "getTopPingDetails", 
	query = "select new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.TopURLDetailWrapper(w.website.siteName, w.pingCount, w.avgTTL, w.avgTTFB,"
		+ "w.avgFDNS,w.avgTDNS,w.avgPing,w.avgPageSize) from WPTDashboardDetail w where  DATE(w.recordTime) = STR_TO_DATE(:date, '%d%m%y') and w.processType = :processType "
		+ "and w.operator = :operator and w.technology = :technology and w.pingCount is not null and w.recordType = 'SITEWISE' and w.website is not null order by w.pingCount desc")
	
	@NamedQuery(name = "getTotalPingCount", 
	query = "select sum(w.pingCount) from WPTDashboardDetail w where  DATE(w.recordTime) = STR_TO_DATE(:date, '%d%m%y') "
			+ "and w.processType = :processType and w.operator = :operator and w.technology = :technology and w.pingCount is not null "
			+ "and w.recordType = 'SITEWISE' and w.website is not null and w.pingCount is not null")
	
	@NamedQuery(name = "getTotalTestedURLCount", 
	query = "select sum(w.totalCount) from WPTDashboardDetail w where DATE(w.recordTime) = STR_TO_DATE(:date, '%d%m%y') and w.processType = :processType "
		+ "and w.operator = :operator and w.technology = :technology and w.recordType = 'SITEWISE' and w.website is not null")
	
	@NamedQuery(name = "getAvgTTFBDetail", 
		query = "select new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTDashboardKpiWrapper(DATE_FORMAT(w.recordTime, '%d%m%y'), "
				+ "w.avgTTFB) from WPTDashboardDetail w where w.processType = :processType and w.operator = :operator "
				+ "and w.technology = :technology and w.recordType = 'SITEWISE' and w.website is null "
				+ "and DATE_FORMAT(w.recordTime, '%d%m%y') in :timeConstantList and w.imeiCount is null")
	
	@NamedQuery(name = "getHTTPAndHTTPSCount", 
		query = "select new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.WPTHTTPStatsWrapper(DATE_FORMAT(w.recordTime, '%d%m%y'), "
				+ "sum(w.totalCount),  w.website.isHttps) from WPTDashboardDetail w where w.processType = :processType "
				+ "and DATE_FORMAT(w.recordTime, '%d%m%y') in :timeConstantList and w.operator = :operator and w.technology = :technology "
				+ "and w.recordType = 'SITEWISE' and w.website is not null group by DATE_FORMAT(w.recordTime, '%d%m%y'), w.website.isHttps")
	
	@NamedQuery(name = "getAnalysisData", 
		query = "select new com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.TopURLDetailWrapper(DATE_FORMAT(w.recordTime, '%d%m%y'),w.operator,w.ipVersion,w.geographyL1.id,w.geographyL2.id,w.geographyL3.id,w.geographyL4.id,"
			+ "(SUM(w.avgHopCount*w.hopCount)/SUM(w.hopCount)),(SUM(w.avgTTL*w.ttlCount)/SUM(w.ttlCount)),(SUM(w.avgTTFB*w.ttfbCount)/SUM(w.ttfbCount)),(SUM(w.avgFDNS*w.fdnsCount)/SUM(w.fdnsCount)),"
			+ "(SUM(w.avgTDNS*w.tdnsCount)/SUM(w.tdnsCount)),(SUM(w.avgPing*w.pingCount)/SUM(w.pingCount))) from WPTDashboardDetail w where w.processType = :processType and w.technology = :technology "
			+ "and DATE_FORMAT(w.recordTime, '%d%m%y') in :timeConstantList and w.location.id in (:location) and w.recordType = 'CATEGORYWISE' and w.category.id in (:category) "
			+ "group by DATE_FORMAT(w.recordTime, '%d%m%y'),w.operator,w.ipVersion,w.geographyL4.id,w.geographyL3.id,w.geographyL2.id,w.geographyL1.id")


	@FilterDef(name =  WPTAnalyticsConstants.GEOGRAPHYL1_FILTER, parameters = {
			@ParamDef(name = "geographyId", type = "java.lang.Integer")})
	
	@FilterDef(name =  WPTAnalyticsConstants.GEOGRAPHYL2_FILTER, parameters = {
			@ParamDef(name = "geographyId", type = "java.lang.Integer")})
	
	@FilterDef(name = WPTAnalyticsConstants.GEOGRAPHYL3_FILTER, parameters = {
			@ParamDef(name = "geographyId", type = "java.lang.Integer")})
	
	@FilterDef(name =  WPTAnalyticsConstants.GEOGRAPHYL4_FILTER, parameters = {
			@ParamDef(name = "geographyId", type = "java.lang.Integer")})

	@FilterDef(name = WPTAnalyticsConstants.OPERATOR_FILTER, parameters = {
			@ParamDef(name = "operator", type = "java.lang.String")})
	
	@FilterDef(name = WPTAnalyticsConstants.GEOGRAPHYL3_TECHNOLOGY_FILTER, parameters = {
			@ParamDef(name = "comparatorTechList", type = "java.lang.String")})
	
	@FilterDef(name = WPTAnalyticsConstants.OPERATOR_TECHNOLOGY_FILTER, parameters = {
			@ParamDef(name = "comparatorTechList", type = "java.lang.String")})
	
	@FilterDef(name = WPTAnalyticsConstants.ALL_GEOGRAPHY_FILTER, parameters = {})
	
	@FilterDef(name = WPTAnalyticsConstants.GEOGRAPHY_LEVEL_FILTER, parameters = {
			@ParamDef(name = "geographyLevel", type = "java.lang.String")})
	

@Filters(value = {
	
	@Filter(name = WPTAnalyticsConstants.GEOGRAPHYL1_FILTER, 
		condition = "wptdashboarddetailid_pk in (select f.wptdashboarddetailid_pk from WPTDashboardDetail f "
					+ "where f.geographyl1id_fk in (:geographyId) and f.geographyl2id_fk is null and f.geographyl3id_fk is null"
					+ "and f.geographyl4id_fk is null)"),
	
	@Filter(name = WPTAnalyticsConstants.GEOGRAPHYL2_FILTER, 
		condition = "wptdashboarddetailid_pk in (select f.wptdashboarddetailid_pk from WPTDashboardDetail f "
					+ "where f.geographyl2id_fk in (:geographyId) and f.geographyl3id_fk is null and f.geographyl4id_fk is null)"),
	
	@Filter(name = WPTAnalyticsConstants.GEOGRAPHYL3_FILTER, 
		condition = "wptdashboarddetailid_pk in (select f.wptdashboarddetailid_pk from WPTDashboardDetail f "
					+ "where f.geographyl3id_fk in (:geographyId) and f.geographyl4id_fk is null)"),
	
	@Filter(name = WPTAnalyticsConstants.GEOGRAPHYL4_FILTER, 
		condition = "wptdashboarddetailid_pk in (select f.wptdashboarddetailid_pk from WPTDashboardDetail f "
					+ "where f.geographyl4id_fk in (:geographyId))"),
	
	@Filter(name = WPTAnalyticsConstants.GEOGRAPHYL3_TECHNOLOGY_FILTER, 
		condition = "wptdashboarddetailid_pk in (select f.wptdashboarddetailid_pk from WPTDashboardDetail f "
					+ "where concat(f.geographyl3id_fk,'_',f.ipVersion) in (:comparatorTechList) "
					+ "and f.geographyl4id_fk is null)"),
	
	@Filter(name = WPTAnalyticsConstants.OPERATOR_FILTER, 
	condition = "wptdashboarddetailid_pk in (select f.wptdashboarddetailid_pk from WPTDashboardDetail f "
				+ "where f.operator in (:operator))"),
	
	@Filter(name = WPTAnalyticsConstants.OPERATOR_TECHNOLOGY_FILTER, 
		condition = "wptdashboarddetailid_pk in (select f.wptdashboarddetailid_pk from WPTDashboardDetail f "
					+ "where concat(f.operator,'_',f.ipVersion) in (:comparatorTechList))"),
	
	@Filter(name = WPTAnalyticsConstants.ALL_GEOGRAPHY_FILTER, 
		condition = "wptdashboarddetailid_pk in (select f.wptdashboarddetailid_pk from WPTDashboardDetail f "
					+ "where f.geographyl1id_fk is null and f.geographyl2id_fk is null and f.geographyl3id_fk is null "
					+ "and f.geographyl4id_fk is null)"),
	
	@Filter(name = WPTAnalyticsConstants.GEOGRAPHY_LEVEL_FILTER, 
		condition = "wptdashboarddetailid_pk in (select f.wptdashboarddetailid_pk from WPTDashboardDetail f "
					+ "where f.geographylevel in (:geographyLevel))"),
})

@Entity
@Table(name = "WPTDashboardDetail")
@XmlRootElement(name = "WPTDashboardDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class WPTDashboardDetail implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@Column(name = "wptdashboarddetailid_pk")
	private Integer id;
																																																																																																																																																																																																																																																																																																																																																																																																																																																
	/** The creation time. */
	@Basic
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modified time. */
	@Basic
	@Column(name = "modifiedtime")
	private Date modifiedTime;

	/** The record time. */
	@Basic
	@Column(name = "recordtime")
	private Date recordTime;
	
	/** The geography L 1. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "geographyl1id_fk", nullable = true)
	private GeographyL1 geographyL1;
	
	/** The geography L 2. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "geographyl2id_fk", nullable = true)
	private GeographyL2 geographyL2;
	
	/** The geography L 3. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "geographyl3id_fk", nullable = true)
	private GeographyL3 geographyL3;
	
	/** The geography L 4. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "geographyl4id_fk", nullable = true)
	private GeographyL4 geographyL4;

	/** The location. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "wptlocationid_fk", nullable = true)
	private WPTLocation location;

	/** The category. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "webcategoryid_fk", nullable = true)
	private WebCategory category;
	
	/** The website. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "websiteid_fk", nullable = true)
	private Website website;
	
	/** The operator. */
	@Column(name = "operator")
	private String operator;

	/** The technology. */
	@Column(name = "technology")
	private String technology;

	/** The record type. */
	@Column(name = "recordtype")
	private String recordType;
	
	/** The process type. */
	@Column(name = "processtype")
	private String processType;
	
	/** The geography level. */
	@Column(name = "geographylevel")
	private String geographyLevel;
	
	/** The avg TTL. */
	@Column(name = "avgttl")
	private Double avgTTL;

	/** The ttl count. */
	@Column(name = "ttlcount")
	private Long ttlCount;

	/** The avg TTFB. */
	@Column(name = "avgttfb")
	private Double avgTTFB;

	/** The ttfb count. */
	@Column(name = "ttfbcount")
	private Long ttfbCount;

	/** The avg FDNS. */
	@Column(name = "avgfdns")
	private Double avgFDNS;

	/** The fdns count. */
	@Column(name = "fdnscount")
	private Long fdnsCount;

	/** The avg TDNS. */
	@Column(name = "avgtdns")
	private Double avgTDNS;

	/** The tdns count. */
	@Column(name = "tdnscount")
	private Long tdnsCount;

	/** The avg ping. */
	@Column(name = "avgping")
	private Double avgPing;

	/** The ping count. */
	@Column(name = "pingcount")
	private Long pingCount;

	/** The avg page size. */
	@Column(name = "avgpagesize")
	private Double avgPageSize;

	/** The page size count. */
	@Column(name = "pagesizecount")
	private Long pageSizeCount;

	/** The total count. */
	@Column(name = "totalcount")
	private Long totalCount;

	/** The avg hop count. */
	@Column(name = "avghopcount")
	private Long avgHopCount;

	/** The ipv 4 count. */
	@Column(name = "ipv4count")
	private Long ipv4Count;

	/** The ipv 6 count. */
	@Column(name = "ipv6count")
	private Long ipv6Count;

	/** The imei count. */
	@Column(name = "imeicount")
	private Long imeiCount;
	
	/** The min TTFB. */
	@Column(name = "minttfb")
	private Double minTTFB;
	
	/** The max TTFB. */
	@Column(name = "maxttfb")
	private Double maxTTFB; 
	
	/** The ip version. */
	@Column(name = "ipversion")
	private String ipVersion;

	/** The hop count. */
	@Column(name = "hopcount")
	private Long hopCount;
	
	/** The trace time count. */
	@Column(name = "tracetimecount")
	private Long traceTimeCount;
	
	/** The avg trace time. */
	@Column(name = "avgtracetime")
	private Double avgTraceTime;
	
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
	 * Gets the record time.
	 *
	 * @return the record time
	 */
	public Date getRecordTime() {
		return recordTime;
	}

	/**
	 * Sets the record time.
	 *
	 * @param recordTime the new record time
	 */
	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	/**
	 * Gets the geography L 1.
	 *
	 * @return the geography L 1
	 */
	public GeographyL1 getGeographyL1() {
		return geographyL1;
	}

	/**
	 * Sets the geography L 1.
	 *
	 * @param geographyL1 the new geography L 1
	 */
	public void setGeographyL1(GeographyL1 geographyL1) {
		this.geographyL1 = geographyL1;
	}

	/**
	 * Gets the geography L 2.
	 *
	 * @return the geography L 2
	 */
	public GeographyL2 getGeographyL2() {
		return geographyL2;
	}

	/**
	 * Sets the geography L 2.
	 *
	 * @param geographyL2 the new geography L 2
	 */
	public void setGeographyL2(GeographyL2 geographyL2) {
		this.geographyL2 = geographyL2;
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
	 * Gets the geography L 4.
	 *
	 * @return the geography L 4
	 */
	public GeographyL4 getGeographyL4() {
		return geographyL4;
	}

	/**
	 * Sets the geography L 4.
	 *
	 * @param geographyL4 the new geography L 4
	 */
	public void setGeographyL4(GeographyL4 geographyL4) {
		this.geographyL4 = geographyL4;
	}

	/**
	 * Gets the location.
	 *
	 * @return the location
	 */
	public WPTLocation getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 *
	 * @param location the new location
	 */
	public void setLocation(WPTLocation location) {
		this.location = location;
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
	 * Gets the technology.
	 *
	 * @return the technology
	 */
	public String getTechnology() {
		return technology;
	}

	/**
	 * Sets the technology.
	 *
	 * @param technology the new technology
	 */
	public void setTechnology(String technology) {
		this.technology = technology;
	}

	/**
	 * Gets the record type.
	 *
	 * @return the record type
	 */
	public String getRecordType() {
		return recordType;
	}

	/**
	 * Sets the record type.
	 *
	 * @param recordType the new record type
	 */
	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	/**
	 * Gets the process type.
	 *
	 * @return the process type
	 */
	public String getProcessType() {
		return processType;
	}

	/**
	 * Sets the process type.
	 *
	 * @param processType the new process type
	 */
	public void setProcessType(String processType) {
		this.processType = processType;
	}

	/**
	 * Gets the geography level.
	 *
	 * @return the geography level
	 */
	public String getGeographyLevel() {
		return geographyLevel;
	}

	/**
	 * Sets the geography level.
	 *
	 * @param geographyLevel the new geography level
	 */
	public void setGeographyLevel(String geographyLevel) {
		this.geographyLevel = geographyLevel;
	}

	/**
	 * Gets the serialversionuid.
	 *
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Gets the avg TTL.
	 *
	 * @return the avg TTL
	 */
	public Double getAvgTTL() {
		return avgTTL;
	}

	/**
	 * Sets the avg TTL.
	 *
	 * @param avgTTL the new avg TTL
	 */
	public void setAvgTTL(Double avgTTL) {
		this.avgTTL = avgTTL;
	}

	/**
	 * Gets the ttl count.
	 *
	 * @return the ttl count
	 */
	public Long getTtlCount() {
		return ttlCount;
	}

	/**
	 * Sets the ttl count.
	 *
	 * @param ttlCount the new ttl count
	 */
	public void setTtlCount(Long ttlCount) {
		this.ttlCount = ttlCount;
	}

	/**
	 * Gets the avg TTFB.
	 *
	 * @return the avg TTFB
	 */
	public Double getAvgTTFB() {
		return avgTTFB;
	}

	/**
	 * Sets the avg TTFB.
	 *
	 * @param avgTTFB the new avg TTFB
	 */
	public void setAvgTTFB(Double avgTTFB) {
		this.avgTTFB = avgTTFB;
	}

	/**
	 * Gets the ttfb count.
	 *
	 * @return the ttfb count
	 */
	public Long getTtfbCount() {
		return ttfbCount;
	}

	/**
	 * Sets the ttfb count.
	 *
	 * @param ttfbCount the new ttfb count
	 */
	public void setTtfbCount(Long ttfbCount) {
		this.ttfbCount = ttfbCount;
	}

	/**
	 * Gets the avg FDNS.
	 *
	 * @return the avg FDNS
	 */
	public Double getAvgFDNS() {
		return avgFDNS;
	}

	/**
	 * Sets the avg FDNS.
	 *
	 * @param avgFDNS the new avg FDNS
	 */
	public void setAvgFDNS(Double avgFDNS) {
		this.avgFDNS = avgFDNS;
	}

	/**
	 * Gets the fdnscount.
	 *
	 * @return the fdnscount
	 */
	public Long getFdnscount() {
		return fdnsCount;
	}

	/**
	 * Sets the fdnscount.
	 *
	 * @param fdnscount the new fdnscount
	 */
	public void setFdnscount(Long fdnscount) {
		this.fdnsCount = fdnscount;
	}

	/**
	 * Gets the avg TDNS.
	 *
	 * @return the avg TDNS
	 */
	public Double getAvgTDNS() {
		return avgTDNS;
	}

	/**
	 * Sets the avg TDNS.
	 *
	 * @param avgTDNS the new avg TDNS
	 */
	public void setAvgTDNS(Double avgTDNS) {
		this.avgTDNS = avgTDNS;
	}

	/**
	 * Gets the tdns count.
	 *
	 * @return the tdns count
	 */
	public Long getTdnsCount() {
		return tdnsCount;
	}

	/**
	 * Sets the tdns count.
	 *
	 * @param tdnsCount the new tdns count
	 */
	public void setTdnsCount(Long tdnsCount) {
		this.tdnsCount = tdnsCount;
	}

	/**
	 * Gets the avg ping.
	 *
	 * @return the avg ping
	 */
	public Double getAvgPing() {
		return avgPing;
	}

	/**
	 * Sets the avg ping.
	 *
	 * @param avgPing the new avg ping
	 */
	public void setAvgPing(Double avgPing) {
		this.avgPing = avgPing;
	}

	/**
	 * Gets the ping count.
	 *
	 * @return the ping count
	 */
	public Long getPingCount() {
		return pingCount;
	}

	/**
	 * Sets the ping count.
	 *
	 * @param pingCount the new ping count
	 */
	public void setPingCount(Long pingCount) {
		this.pingCount = pingCount;
	}

	/**
	 * Gets the avg page size.
	 *
	 * @return the avg page size
	 */
	public Double getAvgPageSize() {
		return avgPageSize;
	}

	/**
	 * Sets the avg page size.
	 *
	 * @param avgPageSize the new avg page size
	 */
	public void setAvgPageSize(Double avgPageSize) {
		this.avgPageSize = avgPageSize;
	}

	/**
	 * Gets the page size count.
	 *
	 * @return the page size count
	 */
	public Long getPageSizeCount() {
		return pageSizeCount;
	}

	/**
	 * Sets the page size count.
	 *
	 * @param pageSizeCount the new page size count
	 */
	public void setPageSizeCount(Long pageSizeCount) {
		this.pageSizeCount = pageSizeCount;
	}

	/**
	 * Gets the total count.
	 *
	 * @return the total count
	 */
	public Long getTotalCount() {
		return totalCount;
	}

	/**
	 * Sets the total count.
	 *
	 * @param totalCount the new total count
	 */
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * Gets the avg hop count.
	 *
	 * @return the avg hop count
	 */
	public Long getAvgHopCount() {
		return avgHopCount;
	}

	/**
	 * Sets the avg hop count.
	 *
	 * @param avgHopCount the new avg hop count
	 */
	public void setAvgHopCount(Long avgHopCount) {
		this.avgHopCount = avgHopCount;
	}

	/**
	 * Gets the ipv 4 count.
	 *
	 * @return the ipv 4 count
	 */
	public Long getIpv4Count() {
		return ipv4Count;
	}

	/**
	 * Sets the ipv 4 count.
	 *
	 * @param ipv4Count the new ipv 4 count
	 */
	public void setIpv4Count(Long ipv4Count) {
		this.ipv4Count = ipv4Count;
	}

	/**
	 * Gets the ipv 6 count.
	 *
	 * @return the ipv 6 count
	 */
	public Long getIpv6Count() {
		return ipv6Count;
	}

	/**
	 * Sets the ipv 6 count.
	 *
	 * @param ipv6Count the new ipv 6 count
	 */
	public void setIpv6Count(Long ipv6Count) {
		this.ipv6Count = ipv6Count;
	}

	/**
	 * Gets the imei count.
	 *
	 * @return the imei count
	 */
	public Long getImeiCount() {
		return imeiCount;
	}

	/**
	 * Sets the imei count.
	 *
	 * @param imeiCount the new imei count
	 */
	public void setImeiCount(Long imeiCount) {
		this.imeiCount = imeiCount;
	}
	
	/**
	 * Gets the min TTFB.
	 *
	 * @return the min TTFB
	 */
	public Double getMinTTFB() {
		return minTTFB;
	}

	/**
	 * Sets the min TTFB.
	 *
	 * @param minTTFB the new min TTFB
	 */
	public void setMinTTFB(Double minTTFB) {
		this.minTTFB = minTTFB;
	}

	/**
	 * Gets the max TTFB.
	 *
	 * @return the max TTFB
	 */
	public Double getMaxTTFB() {
		return maxTTFB;
	}
	
	/**
	 * Sets the max TTFB.
	 *
	 * @param maxTTFB the new max TTFB
	 */
	public void setMaxTTFB(Double maxTTFB) {
		this.maxTTFB = maxTTFB;
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
	 * Gets the hop count.
	 *
	 * @return the hop count
	 */
	public Long getHopCount() {
		return hopCount;
	}
	
	/**
	 * Sets the hop count.
	 *
	 * @param hopCount the new hop count
	 */
	public void setHopCount(Long hopCount) {
		this.hopCount = hopCount;
	}

	/**
	 * Gets the trace time count.
	 *
	 * @return the trace time count
	 */
	public Long getTraceTimeCount() {
		return traceTimeCount;
	}

	/**
	 * Sets the trace time count.
	 *
	 * @param traceTimeCount the new trace time count
	 */
	public void setTraceTimeCount(Long traceTimeCount) {
		this.traceTimeCount = traceTimeCount;
	}

	/**
	 * Gets the avg trace time.
	 *
	 * @return the avg trace time
	 */
	public Double getAvgTraceTime() {
		return avgTraceTime;
	}

	/**
	 * Sets the avg trace time.
	 *
	 * @param avgTraceTime the new avg trace time
	 */
	public void setAvgTraceTime(Double avgTraceTime) {
		this.avgTraceTime = avgTraceTime;
	}

}
