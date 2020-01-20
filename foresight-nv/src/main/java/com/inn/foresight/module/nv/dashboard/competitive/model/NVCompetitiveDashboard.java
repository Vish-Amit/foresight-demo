package com.inn.foresight.module.nv.dashboard.competitive.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class NVCompetitiveDashboard.
 */
@Entity
@Table(name = "NVCompetitiveDashboard")
@XmlRootElement(name = "NVCompetitiveDashboard")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NVCompetitiveDashboard {

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "nvcompetitivedashboardid_pk")
	private Integer id;

	/** The technology. */
	@Column(name = "technology")
	private String technology;

	/** The operator. */
	@Column(name = "operator")
	private String operator;

	/** The geography L 1. */
	@Column(name = "geographyl1id_fk")
	private Integer geographyL1;

	/** The geography L 2. */
	@Column(name = "geographyl2id_fk")
	private Integer geographyL2;

	/** The geography L 3. */
	@Column(name = "geographyl3id_fk")
	private Integer geographyL3;

	/** The geography L 4. */
	@Column(name = "geographyl4id_fk")
	private Integer geographyL4;

	/** The rsrp json. */
	@Column(name = "rsrpjson")
	private String rsrpJson;

	/** The sinr json. */
	@Column(name = "sinrjson")
	private String sinrJson;

	/** The rsrq json. */
	@Column(name = "rsrqjson")
	private String rsrqJson;
	
	/** The rscp json. */
	@Column(name = "rscpjson")
	private String rscpJson;
	
	/** The rx level json. */
	@Column(name = "rxleveljson")
	private String rxLevelJson;
	
	/** The source type. */
	@Column(name = "sourcetype")
	private String sourceType;

	/** The creation time. */
	@Column(name = "creationtime")
	private Date creationTime;
	
	/** The modification time. */
	@Column(name = "modificationtime")
	private Date modificationTime;

	/** The make. */
	@Column(name = "makejson")
	private String make;
	
	/** The jitter json. */
	@Column(name = "jitterjson")
	private String jitterJson;

	/** The pckt lost json. */
	@Column(name = "pcktlossjson")
	private String pcktLostJson;

	/** The latency json. */
	@Column(name = "latencyjson")
	private String latencyJson;

	/** The dl json. */
	@Column(name = "dljsonLTE")
	private String dlJsonLTE;


	/** The ul json. */
	@Column(name = "uljsonLTE")
	private String ulJsonLTE;


	/** The dl json. */
	@Column(name = "dljson3G")
	private String dlJson3G;

	/** The ul json. */
	@Column(name = "uljson3G")
	private String ulJson3G;
	/** The dl json. */
	@Column(name = "dljson2G")
	private String dlJson2G;

	/** The ul json. */
	@Column(name = "uljson2G")
	private String ulJson2G;
	
	/** The coverage count. */
	@Column(name = "coveragecount")
	private Long coverageCount;

	/** The no coverage count. */
	@Column(name = "nocoveragecount")
	private Long noCoverageCount;

	/** The wifi count. */
	@Column(name = "wificount")
	private Long wifiCount;

	/** The airplane mode count. */
	@Column(name = "airplanemodecount")
	private Long airplaneModeCount;
	
	
	@Column(name = "dns")
	private String dns;
	
	/** The ttl count. */
	@Column(name = "ttl")
	private String ttl;
	
	/** The ttl count. */
	@Column(name = "buffertime")
	private String buffertime;
	
	/** The youtubedl count. */
	@Column(name = "youtubedl")
	private String youtubedl;
	
	/** The application name field. */
	@Column(name = "appname")
	private String appName;
	
 	public String getYoutubedl() {
		return youtubedl;
	}

	public void setYoutubedl(String youtubedl) {
		this.youtubedl = youtubedl;
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
	 * Gets the geography L 1.
	 *
	 * @return the geography L 1
	 */
	public Integer getGeographyL1() {
		return geographyL1;
	}

	/**
	 * Sets the geography L 1.
	 *
	 * @param geographyL1 the new geography L 1
	 */
	public void setGeographyL1(Integer geographyL1) {
		this.geographyL1 = geographyL1;
	}

	/**
	 * Gets the geography L 2.
	 *
	 * @return the geography L 2
	 */
	public Integer getGeographyL2() {
		return geographyL2;
	}

	/**
	 * Sets the geography L 2.
	 *
	 * @param geographyL2 the new geography L 2
	 */
	public void setGeographyL2(Integer geographyL2) {
		this.geographyL2 = geographyL2;
	}

	/**
	 * Gets the geography L 3.
	 *
	 * @return the geography L 3
	 */
	public Integer getGeographyL3() {
		return geographyL3;
	}

	/**
	 * Sets the geography L 3.
	 *
	 * @param geographyL3 the new geography L 3
	 */
	public void setGeographyL3(Integer geographyL3) {
		this.geographyL3 = geographyL3;
	}

	/**
	 * Gets the geography L 4.
	 *
	 * @return the geography L 4
	 */
	public Integer getGeographyL4() {
		return geographyL4;
	}

	/**
	 * Sets the geography L 4.
	 *
	 * @param geographyL4 the new geography L 4
	 */
	public void setGeographyL4(Integer geographyL4) {
		this.geographyL4 = geographyL4;
	}

	/**
	 * Gets the rsrp.
	 *
	 * @return the rsrp
	 */
	public String getRsrp() {
		return rsrpJson;
	}

	/**
	 * Sets the rsrp.
	 *
	 * @param rsrp the new rsrp
	 */
	public void setRsrp(String rsrp) {
		this.rsrpJson = rsrp;
	}

	/**
	 * Gets the sinr.
	 *
	 * @return the sinr
	 */
	public String getSinr() {
		return sinrJson;
	}

	/**
	 * Sets the sinr.
	 *
	 * @param sinr the new sinr
	 */
	public void setSinr(String sinr) {
		this.sinrJson = sinr;
	}

	/**
	 * Gets the rsrq.
	 *
	 * @return the rsrq
	 */
	public String getRsrq() {
		return rsrqJson;
	}

	/**
	 * Sets the rsrq.
	 *
	 * @param rsrq the new rsrq
	 */
	public void setRsrq(String rsrq) {
		this.rsrqJson = rsrq;
	}

	/**
	 * Gets the rscp.
	 *
	 * @return the rscp
	 */
	public String getRscp() {
		return rscpJson;
	}

	/**
	 * Sets the rscp.
	 *
	 * @param rscp the new rscp
	 */
	public void setRscp(String rscp) {
		this.rscpJson = rscp;
	}

	/**
	 * Gets the rx level.
	 *
	 * @return the rx level
	 */
	public String getRxLevel() {
		return rxLevelJson;
	}

	

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
	}

	public String getTtl() {
		return ttl;
	}

	public void setTtl(String ttl) {
		this.ttl = ttl;
	}

	public String getBuffertime() {
		return buffertime;
	}

	public void setBuffertime(String buffertime) {
		this.buffertime = buffertime;
	}

	/**
	 * Sets the rx level.
	 *
	 * @param rxLevel the new rx level
	 */
	public void setRxLevel(String rxLevel) {
		this.rxLevelJson = rxLevel;
	}

	/**
	 * Gets the source type.
	 *
	 * @return the source type
	 */
	public String getSourceType() {
		return sourceType;
	}

	/**
	 * Sets the source type.
	 *
	 * @param sourceType the new source type
	 */
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
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
	 * Gets the modification time.
	 *
	 * @return the modification time
	 */
	public Date getModificationTime() {
		return modificationTime;
	}

	/**
	 * Sets the modification time.
	 *
	 * @param modificationTime the new modification time
	 */
	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	/**
	 * Gets the make.
	 *
	 * @return the make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * Sets the make.
	 *
	 * @param make the new make
	 */
	public void setMake(String make) {
		this.make = make;
	}

	/**
	 * Gets the jitter.
	 *
	 * @return the jitter
	 */
	public String getJitter() {
		return jitterJson;
	}

	/**
	 * Sets the jitter.
	 *
	 * @param jitter the new jitter
	 */
	public void setJitter(String jitter) {
		this.jitterJson = jitter;
	}

	/**
	 * Gets the packet lost.
	 *
	 * @return the packet lost
	 */
	public String getPacketLost() {
		return pcktLostJson;
	}

	/**
	 * Sets the packet lost.
	 *
	 * @param packetLost the new packet lost
	 */
	public void setPacketLost(String packetLost) {
		this.pcktLostJson = packetLost;
	}

	/**
	 * Gets the latency.
	 *
	 * @return the latency
	 */
	public String getLatency() {
		return latencyJson;
	}

	/**
	 * Sets the latency.
	 *
	 * @param latency the new latency
	 */
	public void setLatency(String latency) {
		this.latencyJson = latency;
	}

	/**
	 * Gets the dl.
	 *
	 * @return the dl
	 */
	public String getDlJsonLTE() {
		return dlJsonLTE;
	}

	/**
	 * Sets the dl.
	 *
	 * @param dl the new dl
	 */
	public void setDlJsonLTE(String dl) {
		this.dlJsonLTE = dl;
	}

	/**
	 * Gets the ul.
	 *
	 * @return the ul
	 */
	public String getUlJsonLTE() {
		return ulJsonLTE;
	}

	/**
	 * Sets the ul.
	 *
	 * @param ul the new ul
	 */
	public void setUlJsonLTE(String ul) {
		this.ulJsonLTE = ul;
	}	

	public String getDlJson3G() {
		return dlJson3G;
	}

	public void setDlJson3G(String dlJson3G) {
		this.dlJson3G = dlJson3G;
	}

	public String getUlJson3G() {
		return ulJson3G;
	}

	public void setUlJson3G(String ulJson3G) {
		this.ulJson3G = ulJson3G;
	}

	public String getDlJson2G() {
		return dlJson2G;
	}

	public void setDlJson2G(String dlJson2G) {
		this.dlJson2G = dlJson2G;
	}

	public String getUlJson2G() {
		return ulJson2G;
	}

	public void setUlJson2G(String ulJson2G) {
		this.ulJson2G = ulJson2G;
	}

	/**
	 * Gets the coverage count.
	 *
	 * @return the coverage count
	 */
	public Long getCoverageCount() {
		return coverageCount;
	}

	/**
	 * Sets the coverage count.
	 *
	 * @param coverageCount the new coverage count
	 */
	public void setCoverageCount(Long coverageCount) {
		this.coverageCount = coverageCount;
	}

	/**
	 * Gets the no coverage count.
	 *
	 * @return the no coverage count
	 */
	public Long getNoCoverageCount() {
		return noCoverageCount;
	}

	/**
	 * Sets the no coverage count.
	 *
	 * @param noCoverageCount the new no coverage count
	 */
	public void setNoCoverageCount(Long noCoverageCount) {
		this.noCoverageCount = noCoverageCount;
	}

	/**
	 * Gets the wifi count.
	 *
	 * @return the wifi count
	 */
	public Long getWifiCount() {
		return wifiCount;
	}

	/**
	 * Sets the wifi count.
	 *
	 * @param wifiCount the new wifi count
	 */
	public void setWifiCount(Long wifiCount) {
		this.wifiCount = wifiCount;
	}

	/**
	 * Gets the airplane mode count.
	 *
	 * @return the airplane mode count
	 */
	public Long getAirplaneModeCount() {
		return airplaneModeCount;
	}

	/**
	 * Sets the airplane mode count.
	 *
	 * @param airplaneModeCount the new airplane mode count
	 */
	public void setAirplaneModeCount(Long airplaneModeCount) {
		this.airplaneModeCount = airplaneModeCount;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Override
	public String toString() {
		return "NVCompetitiveDashboard [id=" + id + ", technology=" + technology + ", operator=" + operator
				+ ", geographyL1=" + geographyL1 + ", geographyL2=" + geographyL2 + ", geographyL3=" + geographyL3
				+ ", geographyL4=" + geographyL4 + ", rsrpJson=" + rsrpJson + ", sinrJson=" + sinrJson + ", rsrqJson="
				+ rsrqJson + ", rscpJson=" + rscpJson + ", rxLevelJson=" + rxLevelJson + ", sourceType=" + sourceType
				+ ", creationTime=" + creationTime + ", modificationTime=" + modificationTime + ", make=" + make
				+ ", jitterJson=" + jitterJson + ", pcktLostJson=" + pcktLostJson + ", latencyJson=" + latencyJson
				+ ", dlJsonLTE=" + dlJsonLTE + ", ulJsonLTE=" + ulJsonLTE + ", dlJson3G=" + dlJson3G + ", ulJson3G="
				+ ulJson3G + ", dlJson2G=" + dlJson2G + ", ulJson2G=" + ulJson2G + ", coverageCount=" + coverageCount
				+ ", noCoverageCount=" + noCoverageCount + ", wifiCount=" + wifiCount + ", airplaneModeCount="
				+ airplaneModeCount + ", dns=" + dns + ", ttl=" + ttl + ", buffertime=" + buffertime + ", youtubedl="
				+ youtubedl + ", appName=" + appName + "]";
	}	
}
