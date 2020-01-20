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
 * The Class NVCompetitiveUser.
 */
@Entity
@Table(name = "NVCompetitiveUser")
@XmlRootElement(name = "NVCompetitiveUser")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NVCompetitiveUser {

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "nvcompetitiveuserid_pk")
	private Integer id;

	/** The user count. */
	@Column(name = "usercount")
	private Long userCount;

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

	/** The source type. */
	@Column(name = "sourcetype")
	private String sourceType;

	/** The creation time. */
	@Column(name = "creationtime")
	private Date creationTime;
	
	/** The modification time. */
	@Column(name = "modificationtime")
	private Date modificationTime;

	/** The os. */
	@Column(name = "os")
	private String os;

	/** The data count. */
	@Column(name = "datacount")
	private Long dataCount;
	
	/** The data voice count. */
	@Column(name = "datavoicecount")
	private Long dataVoiceCount;	
	
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

	/** The dl json. */
	@Column(name = "dljson")
	private String dlJson;
	
	/** The ul json. */
	@Column(name = "uljson")
	private String ulJson;
	
	/** The application name field. */
	@Column(name = "appname")
	private String appName;
	
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
	 * Gets the user count.
	 *
	 * @return the user count
	 */
	public Long getUserCount() {
		return userCount;
	}

	/**
	 * Sets the user count.
	 *
	 * @param userCount the new user count
	 */
	public void setUserCount(Long userCount) {
		this.userCount = userCount;
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
	 * Gets the os.
	 *
	 * @return the os
	 */
	public String getOs() {
		return os;
	}

	/**
	 * Sets the os.
	 *
	 * @param os the new os
	 */
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * Gets the data count.
	 *
	 * @return the data count
	 */
	public Long getDataCount() {
		return dataCount;
	}

	/**
	 * Sets the data count.
	 *
	 * @param dataCount the new data count
	 */
	public void setDataCount(Long dataCount) {
		this.dataCount = dataCount;
	}

	/**
	 * Gets the data voice count.
	 *
	 * @return the data voice count
	 */
	public Long getDataVoiceCount() {
		return dataVoiceCount;
	}

	/**
	 * Sets the data voice count.
	 *
	 * @param dataVoiceCount the new data voice count
	 */
	public void setDataVoiceCount(Long dataVoiceCount) {
		this.dataVoiceCount = dataVoiceCount;
	}

	/**
	 * Gets the rsrp json.
	 *
	 * @return the rsrp json
	 */
	public String getRsrpJson() {
		return rsrpJson;
	}

	/**
	 * Sets the rsrp json.
	 *
	 * @param rsrpJson the new rsrp json
	 */
	public void setRsrpJson(String rsrpJson) {
		this.rsrpJson = rsrpJson;
	}

	/**
	 * Gets the sinr json.
	 *
	 * @return the sinr json
	 */
	public String getSinrJson() {
		return sinrJson;
	}

	/**
	 * Sets the sinr json.
	 *
	 * @param sinrJson the new sinr json
	 */
	public void setSinrJson(String sinrJson) {
		this.sinrJson = sinrJson;
	}

	/**
	 * Gets the rsrq json.
	 *
	 * @return the rsrq json
	 */
	public String getRsrqJson() {
		return rsrqJson;
	}

	/**
	 * Sets the rsrq json.
	 *
	 * @param rsrqJson the new rsrq json
	 */
	public void setRsrqJson(String rsrqJson) {
		this.rsrqJson = rsrqJson;
	}

	/**
	 * Gets the rscp json.
	 *
	 * @return the rscp json
	 */
	public String getRscpJson() {
		return rscpJson;
	}

	/**
	 * Sets the rscp json.
	 *
	 * @param rscpJson the new rscp json
	 */
	public void setRscpJson(String rscpJson) {
		this.rscpJson = rscpJson;
	}

	/**
	 * Gets the rx level json.
	 *
	 * @return the rx level json
	 */
	public String getRxLevelJson() {
		return rxLevelJson;
	}

	/**
	 * Sets the rx level json.
	 *
	 * @param rxLevelJson the new rx level json
	 */
	public void setRxLevelJson(String rxLevelJson) {
		this.rxLevelJson = rxLevelJson;
	}

	/**
	 * Gets the dl json.
	 *
	 * @return the dl json
	 */
	public String getDlJson() {
		return dlJson;
	}

	/**
	 * Sets the dl json.
	 *
	 * @param dlJson the new dl json
	 */
	public void setDlJson(String dlJson) {
		this.dlJson = dlJson;
	}

	/**
	 * Gets the ul json.
	 *
	 * @return the ul json
	 */
	public String getUlJson() {
		return ulJson;
	}

	/**
	 * Sets the ul json.
	 *
	 * @param ulJson the new ul json
	 */
	public void setUlJson(String ulJson) {
		this.ulJson = ulJson;
	}


	
	/**
	 *Returns application Name
	 * @return
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * set application name
	 * @param appName
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	@Override
	public String toString() {
		return "NVCompetitiveUser [id=" + id + ", userCount=" + userCount + ", operator=" + operator + ", geographyL1="
				+ geographyL1 + ", geographyL2=" + geographyL2 + ", geographyL3=" + geographyL3 + ", geographyL4="
				+ geographyL4 + ", sourceType=" + sourceType + ", creationTime=" + creationTime + ", modificationTime="
				+ modificationTime + ", os=" + os + ", dataCount=" + dataCount + ", dataVoiceCount=" + dataVoiceCount
				+ ", rsrpJson=" + rsrpJson + ", sinrJson=" + sinrJson + ", rsrqJson=" + rsrqJson + ", rscpJson="
				+ rscpJson + ", rxLevelJson=" + rxLevelJson + ", dlJson=" + dlJson + ", ulJson=" + ulJson + ", appName="
				+ appName + "]";
	}
}
