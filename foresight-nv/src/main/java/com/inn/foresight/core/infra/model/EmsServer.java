package com.inn.foresight.core.infra.model;

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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.EMSType;
import com.inn.foresight.core.infra.utils.enums.Vendor;

/**
 * The Class EmsServer.
 */
@NamedQueries({
		@NamedQuery(name = "getEmsServerByName", query = "select e from EmsServer e where upper(e.emsname)=:name"),
		@NamedQuery(name = "getIpListFromEmsServer", query = "select i.id,i.ip from EmsServer i"),
		@NamedQuery(name = "getAllEmsServerByEmsType", query = "select e from EmsServer e "),
		@NamedQuery(name = "getEmsServerByIpAndEmsType", query = "select e from EmsServer e where e.dns=:dns "),
		@NamedQuery(name = "getAllEmsWhosTokenAboutToExpire", query = "select e from EmsServer e "),
		@NamedQuery(name = "getEmsServerByDomainAndVendor", query = "select e from EmsServer e where upper(e.domain)=:domain and upper(e.vendor)=:vendor"),
		@NamedQuery(name = "getEmsNameAndIpByDomainAndVendor", query = "select e.emsname,e.ip from EmsServer e where e.isdeleted=false and e.isActive=true and e.emsType='CM_FILE_SFTP'"),
		@NamedQuery(name = "getDistinctEmsNameList", query = "select distinct e.emsname from EmsServer e where e.isdeleted=false and e.emsname is not null"),
		@NamedQuery(name = "getDistinctEmsIPList", query = "select distinct e.ip from EmsServer e where e.isdeleted=false and e.ip is not null"),
		@NamedQuery(name = "getDistinctEmsType", query = "select distinct e.emsType from EmsServer e where e.isdeleted=false and e.emsType is not null"),
		@NamedQuery(name = "getEmsServerByVendorDomainAndEmsType", query = "select e from EmsServer e where e.domain=:domain and e.vendor=:vendor and e.emsType=:emsType and e.isdeleted=false")

})

@FilterDef(name = "emsServerDomainFilter", parameters = { @ParamDef(name = "domain", type = "java.lang.String") })
@FilterDef(name = "emsServerVendorFilter", parameters = { @ParamDef(name = "vendor", type = "java.lang.String") })
@FilterDef(name = "emsServerEmstypeFilter", parameters = { @ParamDef(name = "emsType", type = "java.lang.String") })

@Filter(name = "emsServerDomainFilter", condition = "domain=:domain")
@Filter(name = "emsServerVendorFilter", condition = "vendor=:vendor")
@Filter(name = "emsServerEmstypeFilter", condition = "emsType=:emsType")

@Entity
@Table(name = "EMSServer")
@XmlRootElement(name = "EMSServer")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Audited
public class EmsServer implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5094048436608399203L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "emsserverid_pk")
	private Integer id;

	/** The host name. */
	@Basic
	@Column(name = "emsname")
	private String emsname;
	
	@Basic
	@Column(name = "hostname")
	private String hostname;
	
	/** The user name. */
	@Basic
	@Column(name = "username")
	private String userName;

	/** The pass word. */
	@Basic
	@Column(name = "password")
	private String passWord;

	/** The ip. */
	@Basic
	@Column(name = "ip")
	private String ip;

	/** The vendor. */
	@Basic
	@Column(name = "vendor")
	@Enumerated(EnumType.STRING)
	private Vendor vendor;

	/** The domain. */
	@Basic
	@Column(name = "domain")
	@Enumerated(EnumType.STRING)
	private Domain domain;

	/** The is active. */
	@Basic
	@Column(name = "active")
	private Boolean isActive;

	/** The created time. */
	@Basic
	@Column(name = "creationtime")
	private Date createdTime;

	/** The modified time. */
	@Basic
	@Column(name = "modificationtime")
	private Date modifiedTime;

	/** The ems type. */
	@Basic
	@Column(name = "emstype")
	@Enumerated(EnumType.STRING)
	private EMSType emsType;

	/** The port. */
	@Basic
	@Column(name = "port")
	private String port;

	/** The path. */
	@Basic
	@Column(name = "path")
	private String path;

	/** The integration type. */
	@Basic
	@Column(name = "integrationtype")
	private String integrationType;

	@Basic
	@Column(name = "deleted")
	private Boolean isdeleted;

	@Basic
	@JoinColumn(name = "masteremsserverid_fk", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private EmsServer masterEmsServer;

	@Basic
	@JoinColumn(name = "executionemsserverid_fk", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private EmsServer executionEmsServer;

	/** The heartbeat ip. */
	@Basic
	@Column(name = "heartbeatip")
	private String heartBeatIp;

	/** The hdfs file path. */
	@Basic
	@Column(name = "hdfsfilepath")
	private String hdfsFilePath;

	@Basic
	@Column(name = "authtoken")
	private String authtoken;

	@Basic
	@Column(name = "tokenexpirytime")
	private Date tokenexpirytime;

	@Basic
	@Column(name = "tokenissuedtime")
	private Date tokenissuedtime;

	@Basic
	@Column(name = "maxcellcapacity")
	private Long maxCellCapacity;

	@Basic
	@Column(name = "latitude")
	private Double latitude;

	/** The longitude. */
	@Basic
	@Column(name = "longitude")
	private Double longitude;

	@Basic
	@Column(name = "communicationid")
	private String communicationId;
	
	@Basic
	@Column(name = "nmsid")
	private String nmsid;
	
	@Basic
	@Column(name = "dns")
	private String dns;

	@Basic
	@Column(name = "fmusername")
	private String fmuserName;

	/** The pass word. */
	@Basic
	@Column(name = "fmpassword")
	private String fmpassWord;
	
	
	@Basic
	@Column(name = "pmusername")
	private String pmuserName;

	/** The pass word. */
	@Basic
	@Column(name = "pmpassword")
	private String pmpassWord;
	
	@Basic
	@Column(name = "cmusername")
	private String cmuserName;

	/** The pass word. */
	@Basic
	@Column(name = "cmpassword")
	private String cmpassWord;
	
	
	@Basic
	@Column(name = "pmbasepath")
	private String pmbasepath;

	@Basic
	@Column(name = "cmbasepath")
	private String cmbasepath;
	
	@Basic
	@Column(name = "pmhdfsfilepath")
	private String pmhdfsfilePath;

	
	@Basic
	@Column(name = "cmhdfsfilepath")
	private String cmhdfsfilePath;

	
	public String getEmsname() {
		return emsname;
	}

	public void setEmsname(String emsname) {
		this.emsname = emsname;
	}

	public Date getTokenissuedtime() {
		return tokenissuedtime;
	}

	public void setTokenissuedtime(Date tokenissuedtime) {
		this.tokenissuedtime = tokenissuedtime;
	}

	public Date getTokenexpirytime() {
		return tokenexpirytime;
	}

	public void setTokenexpirytime(Date tokenexpirytime) {
		this.tokenexpirytime = tokenexpirytime;
	}

	public String getAuthtoken() {
		return authtoken;
	}

	public void setAuthtoken(String authtoken) {
		this.authtoken = authtoken;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getIntegrationType() {
		return integrationType;
	}

	public void setIntegrationType(String integrationType) {
		this.integrationType = integrationType;
	}

	public Boolean getIsdeleted() {
		return isdeleted;
	}

	public void setIsdeleted(Boolean isdeleted) {
		this.isdeleted = isdeleted;
	}

	public EmsServer getMasterEmsServer() {
		return masterEmsServer;
	}

	public void setMasterEmsServer(EmsServer masterEmsServer) {
		this.masterEmsServer = masterEmsServer;
	}

	public EmsServer getExecutionEmsServer() {
		return executionEmsServer;
	}

	public void setExecutionEmsServer(EmsServer executionEmsServer) {
		this.executionEmsServer = executionEmsServer;
	}

	public String getHeartBeatIp() {
		return heartBeatIp;
	}

	public void setHeartBeatIp(String hearteatIp) {
		this.heartBeatIp = hearteatIp;
	}

	public String getHdfsFilePath() {
		return hdfsFilePath;
	}

	public void setHdfsFilePath(String hdfsFilePath) {
		this.hdfsFilePath = hdfsFilePath;
	}

	public Long getMaxCellCapacity() {
		return maxCellCapacity;
	}

	public void setMaxCellCapacity(Long maxCellCapacity) {
		this.maxCellCapacity = maxCellCapacity;
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
	 * Gets the EMS name.
	 *
	 * @return the EMS name
	 */
	public String getEmsName() {
		return emsname;
	}

	/**
	 * Sets the host name.
	 *
	 * @param emsname the new host name
	 */
	public void setEmsName(String emsname) {
		this.emsname = emsname;
	}

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the pass word.
	 *
	 * @return the pass word
	 */
	public String getPassWord() {
		return passWord;
	}

	/**
	 * Sets the pass word.
	 *
	 * @param passWord the new pass word
	 */
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	/**
	 * Gets the ip.
	 *
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * Sets the ip.
	 *
	 * @param ip the new ip
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * Gets the checks if is active.
	 *
	 * @return the checks if is active
	 */
	public Boolean getIsActive() {
		return isActive;
	}

	/**
	 * Sets the checks if is active.
	 *
	 * @param isActive the new checks if is active
	 */
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * Gets the created time.
	 *
	 * @return the created time
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * Sets the created time.
	 *
	 * @param createdTime the new created time
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
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
	 * Gets the vendor.
	 *
	 * @return the vendor
	 */
	public Vendor getVendor() {
		return vendor;
	}

	/**
	 * Sets the vendor.
	 *
	 * @param vendor the new vendor
	 */
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	/**
	 * Gets the ems type.
	 *
	 * @return the ems type
	 */
	public EMSType getEmsType() {
		return emsType;
	}

	/**
	 * Sets the ems type.
	 *
	 * @param emsType the new ems type
	 */
	public void setEmsType(EMSType emsType) {
		this.emsType = emsType;
	}

	/**
	 * Gets the domain.
	 *
	 * @return the domain
	 */
	public Domain getDomain() {
		return domain;
	}

	/**
	 * Sets the domain.
	 *
	 * @param domain the new domain
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getCommunicationId() {
		return communicationId;
	}

	public void setCommunicationId(String communicationId) {
		this.communicationId = communicationId;
	}
	
	public String getNmsid() {
		return nmsid;
	}

	public void setNmsid(String nmsid) {
		this.nmsid = nmsid;
	}

	public String getDns() {
		return dns;
	}

	
	public void setDns(String dns) {
		this.dns = dns;
	}
	
	
	public String getFmuserName() {
		return fmuserName;
	}
	
	public void setFmuserName(String fmuserName) {
		this.fmuserName = fmuserName;
	}

	public String getFmpassWord() {
		return fmpassWord;
	}

	public void setFmpassWord(String fmpassWord) {
		this.fmpassWord = fmpassWord;
	}

	
	public String getPmuserName() {
		return pmuserName;
	}

	
	public void setPmuserName(String pmuserName) {
		this.pmuserName = pmuserName;
	}

	
	public String getPmpassWord() {
		return pmpassWord;
	}

	
	public void setPmpassWord(String pmpassWord) {
		this.pmpassWord = pmpassWord;
	}

	
	public String getCmuserName() {
		return cmuserName;
	}

	
	public void setCmuserName(String cmuserName) {
		this.cmuserName = cmuserName;
	}

	
	public String getCmpassWord() {
		return cmpassWord;
	}

	
	public void setCmpassWord(String cmpassWord) {
		this.cmpassWord = cmpassWord;
	}
	
	
	public String getPmbasepath() {
		return pmbasepath;
	}

	
	public void setPmbasepath(String pmbasepath) {
		this.pmbasepath = pmbasepath;
	}

	
	public String getCmbasepath() {
		return cmbasepath;
	}

	
	public void setCmbasepath(String cmbasepath) {
		this.cmbasepath = cmbasepath;
	}

	
	public String getPmhdfsfilePath() {
		return pmhdfsfilePath;
	}

	
	public void setPmhdfsfilePath(String pmhdfsfilePath) {
		this.pmhdfsfilePath = pmhdfsfilePath;
	}

	
	public String getCmhdfsfilePath() {
		return cmhdfsfilePath;
	}

	
	public void setCmhdfsfilePath(String cmhdfsfilePath) {
		this.cmhdfsfilePath = cmhdfsfilePath;
	}
	

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EmsServer [id=");
		builder.append(id);
		builder.append(", emsname=");
		builder.append(emsname);
		builder.append(", hostname=");
		builder.append(hostname);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", passWord=");
		builder.append(passWord);
		builder.append(", ip=");
		builder.append(ip);
		builder.append(", vendor=");
		builder.append(vendor);
		builder.append(", domain=");
		builder.append(domain);
		builder.append(", isActive=");
		builder.append(isActive);
		builder.append(", createdTime=");
		builder.append(createdTime);
		builder.append(", modifiedTime=");
		builder.append(modifiedTime);
		builder.append(", geographyL1=");
		builder.append(", emsType=");
		builder.append(emsType);
		builder.append(", maxCellCapacity=");
		builder.append(maxCellCapacity);
		builder.append(", latitude=");
		builder.append(latitude);
		builder.append(", longitude=");
		builder.append(longitude);
		builder.append(", communicationId=");
		builder.append(communicationId);
		builder.append("]");
		return builder.toString();
	}
}
