package com.inn.foresight.module.nv.sitesuggestion.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
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
import com.inn.foresight.module.nv.feedback.constants.ConsumerFeedbackConstant;
import com.inn.product.um.user.model.User;

@NamedQuery(name = "getSiteAcquisitionLayerData", query = "select c from FriendlySiteSuggestion c "
		+ "where c.buildingType IS NOT NULL and Date(c.timestamp) between Date(:fromDate) and Date(:toDate)")




@FilterDef(name = ConsumerFeedbackConstant.BUILDING_TYPE_FILTER, parameters = {
		@ParamDef(name = "buildingtype", type = "java.lang.String") })

@FilterDef(name = ConsumerFeedbackConstant.SITE_TYPE_FILTER, parameters = {
		@ParamDef(name = "sitetype", type = "java.lang.String") })




@Filters(value = {
		@Filter(name = ConsumerFeedbackConstant.BUILDING_TYPE_FILTER, condition = "buildingtype like (:buildingtype)"),
		@Filter(name = ConsumerFeedbackConstant.SITE_TYPE_FILTER, condition = "sitetype like (:sitetype)"),

})






@Entity
@Table(name = "FriendlySiteSuggestion")
@XmlRootElement(name = "FriendlySiteSuggestion")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class FriendlySiteSuggestion implements Serializable{
	private static final long serialVersionUID = 1088089440029039646L;
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "friendlysitesuggestion_pk")
	private Integer id;
	
	@Column(name = "deviceid")
	private String deviceId;
	
	@Column(name = "latitude")
	private Double latitude;
	
	@Column(name = "longitude")
	private Double longitude;
	
	@Column(name = "buildingname")
	private String buildingName;
	
	@Column(name = "sitetype")
	private String siteType;
	
	@Column(name = "buildingtype")
	private String buildingType;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "contactpersonname")
	private String contactPersonName;
	
	@Column(name = "contactpersonnumber")
	private String contactPersonNumber;
	
	@Column(name = "filepath")
	private String filepath;
	
	@Column(name = "refferalname")
	private String refferalName;
	
	@Column(name = "refferalcontactnumber")
	private String refferalContactNumber;
	
	@Column(name = "refferalemailid")
	private String refferalEmailId;

	@Column(name = "timestamp")
	private Date timestamp;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid_fk", nullable = true)
	private User userId;
	
	@Column(name = "appversion")
	private String appVersion;
	
	@Column(name = "mcc")
	private Integer mcc;
	
	@Column(name = "mnc")
	private Integer mnc;
	
	@Column(name = "tac")
	private Integer tac;
	
	@Column(name = "lac")
	private Integer lac;
	
	@Column(name = "pci")
	private Integer pci;
	
	@Column(name = "psc")
	private Integer psc;
	
	@Column(name = "networktype")
	private String networkType;
	
	@Column(name = "operator")
	private String operator;
	
	@Column(name = "autotimeenable")
	private Boolean autoTimeEnable;

	@Column(name = "remark")
	private String remark;
	
	
	@Column(name = "creationtime")
	private Date creationTime;
	
	@Column(name = "modificationtime")
	private Date modificationTime;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
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

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getBuildingType() {
		return buildingType;
	}

	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactPersonName() {
		return contactPersonName;
	}

	public void setContactPersonName(String contactPersonName) {
		this.contactPersonName = contactPersonName;
	}

	public String getContactPersonNumber() {
		return contactPersonNumber;
	}

	public void setContactPersonNumber(String contactPersonNumber) {
		this.contactPersonNumber = contactPersonNumber;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getRefferalName() {
		return refferalName;
	}

	public void setRefferalName(String refferalName) {
		this.refferalName = refferalName;
	}

	public String getRefferalContactNumber() {
		return refferalContactNumber;
	}

	public void setRefferalContactNumber(String refferalContactNumber) {
		this.refferalContactNumber = refferalContactNumber;
	}

	public String getRefferalEmailId() {
		return refferalEmailId;
	}

	public void setRefferalEmailId(String refferalEmailId) {
		this.refferalEmailId = refferalEmailId;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public User getUserId() {
		return userId;
	}

	public void setUserId(User userId) {
		this.userId = userId;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public Integer getMcc() {
		return mcc;
	}

	public void setMcc(Integer mcc) {
		this.mcc = mcc;
	}

	public Integer getMnc() {
		return mnc;
	}

	public void setMnc(Integer mnc) {
		this.mnc = mnc;
	}

	public Integer getTac() {
		return tac;
	}

	public void setTac(Integer tac) {
		this.tac = tac;
	}

	public Integer getLac() {
		return lac;
	}

	public void setLac(Integer lac) {
		this.lac = lac;
	}

	public Integer getPci() {
		return pci;
	}

	public void setPci(Integer pci) {
		this.pci = pci;
	}

	public Integer getPsc() {
		return psc;
	}

	public void setPsc(Integer psc) {
		this.psc = psc;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Boolean getAutoTimeEnable() {
		return autoTimeEnable;
	}

	public void setAutoTimeEnable(Boolean autoTimeEnable) {
		this.autoTimeEnable = autoTimeEnable;
	}

	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	@Override
	public String toString() {
		return "FriendlySiteSuggestion [id=" + id + ", deviceId=" + deviceId + ", latitude=" + latitude + ", longitude="
				+ longitude + ", buildingName=" + buildingName + ", siteType=" + siteType + ", buildingType="
				+ buildingType + ", address=" + address + ", contactPersonName=" + contactPersonName
				+ ", contactPersonNumber=" + contactPersonNumber + ", filepath=" + filepath + ", refferalName="
				+ refferalName + ", refferalContactNumber=" + refferalContactNumber + ", refferalEmailId="
				+ refferalEmailId + ", timestamp=" + timestamp + ", userId=" + userId + ", appVersion=" + appVersion
				+ ", mcc=" + mcc + ", mnc=" + mnc + ", tac=" + tac + ", lac=" + lac + ", pci=" + pci + ", psc=" + psc
				+ ", networkType=" + networkType + ", operator=" + operator + ", autoTimeEnable=" + autoTimeEnable
				+ ", remark=" + remark + ", creationTime=" + creationTime + ", modificationTime=" + modificationTime
				+ "]";
	}

	

		
	
}
