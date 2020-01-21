package com.inn.foresight.core.gallery.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.user.model.User;

 @NamedQuery(name = "getGalleryDetailDataList", query = "select g from GalleryDetail g order by g.modificationtime desc")
 @NamedQuery(name = "getGalleryDetailCount", query = "select count(g.id) from GalleryDetail g")
 @NamedQuery(name = "getsearchByNameGalleryCount", query = "select count(g.id) from GalleryDetail g where name like :name")
 @NamedQuery(name = "updateGalleryStatusByIdList", query = "update GalleryDetail g set g.enabled=:enabled where g.id in (:idList)")



@Entity
@Table(name = "GalleryDetail")
@XmlRootElement(name = "GalleryDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class GalleryDetail implements Serializable {

	private static final long serialVersionUID = -5943516947112017124L;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "gallerydetailid_pk")
	private Integer id;

	@Basic
	private String regional;
	@Basic
	private String name;
	@Basic
	private String address;
	@Basic
	private Double latitude;
	@Basic
	private Double longitude;
	@Basic
	private String liveStatus;

	@Basic
	@Column(name = "creationtime")
	private Date creationTime;

	@Basic
	@Column(name = "modificationtime")
	private Date modificationtime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creatorid_fk")
	private User creator;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lastmodifierid_fk")
	private User lastModifier;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "l1managerid_fk")
	private L1Manager l1Manager;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "l2managerid_fk")
	private L2Manager l2Manager;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "l3managerid_fk")
	private L3Manager l3Manager;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "l4managerid_fk")
	private L4Manager l4Manager;

	@Basic
	@Column(name = "enabled")
	private Boolean enabled;

	@Basic
	@Column(name = "grade")
	private String grade;

	@Basic
	@Column(name = "mondaytofridaytime")
	private String mondayToFridayTime;

	@Basic
	@Column(name = "saturdaytime")
	private String saturdayTime;

	@Basic
	@Column(name = "sundaytime")
	private String sundayTime;

	@Basic
	private String geography;
	@Basic
	private String space;
	@Basic
	private String type;
	@Basic
	private String distributor;
	@Basic
	private Integer smile;

	@Basic
	private String status;

	@Basic
	@Column(name = "startoperation")
	private String startOperation;

	@Transient
	private String contactName;

	@Basic
	@Column(name = "deleted")
	private Boolean isDeleted;

	@Column(name = "manual")
	private Boolean isManual;

	@Transient
	private String Modifiername;

	public GalleryDetail(String regional, String name, String address, Double latitude, Double longitude, String contactName, String mondayToFridayTime, String saturdayTime, String sundayTime,
			String type) {
		super();
		this.regional = regional;
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.contactName = contactName;
		this.mondayToFridayTime = mondayToFridayTime;
		this.saturdayTime = saturdayTime;
		this.sundayTime = sundayTime;
		this.type = type;
	}

	public GalleryDetail() {
	}

	public String getLiveStatus() {
		return liveStatus;
	}

	public void setLiveStatus(String liveStatus) {
		this.liveStatus = liveStatus;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMondayToFridayTime() {
		return mondayToFridayTime;
	}

	public void setMondayToFridayTime(String mondayToFridayTime) {
		this.mondayToFridayTime = mondayToFridayTime;
	}

	public String getSaturdayTime() {
		return saturdayTime;
	}

	public void setSaturdayTime(String saturdayTime) {
		this.saturdayTime = saturdayTime;
	}

	public String getSundayTime() {
		return sundayTime;
	}

	public void setSundayTime(String sundayTime) {
		this.sundayTime = sundayTime;
	}

	public L1Manager getL1Manager() {
		return l1Manager;
	}

	public void setL1Manager(L1Manager l1Manager) {
		this.l1Manager = l1Manager;
	}

	public L2Manager getL2Manager() {
		return l2Manager;
	}

	public void setL2Manager(L2Manager l2Manager) {
		this.l2Manager = l2Manager;
	}

	public L3Manager getL3Manager() {
		return l3Manager;
	}

	public void setL3Manager(L3Manager l3Manager) {
		this.l3Manager = l3Manager;
	}

	public L4Manager getL4Manager() {
		return l4Manager;
	}

	public void setL4Manager(L4Manager l4Manager) {
		this.l4Manager = l4Manager;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getDistributor() {
		return distributor;
	}

	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}

	public Integer getSmile() {
		return smile;
	}

	public void setSmile(Integer smile) {
		this.smile = smile;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRegional() {
		return regional;
	}

	public void setRegional(String regional) {
		this.regional = regional;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModificationtime() {
		return modificationtime;
	}

	public void setModificationtime(Date modificationtime) {
		this.modificationtime = modificationtime;
	}

	@JsonIgnore
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@JsonIgnore
	public User getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(User lastModifier) {
		this.lastModifier = lastModifier;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getGeography() {
		return geography;
	}

	public void setGeography(String geography) {
		this.geography = geography;
	}

	public String getSpace() {
		return space;
	}

	public void setSpace(String space) {
		this.space = space;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStartOperation() {
		return startOperation;
	}

	public void setStartOperation(String startOperation) {
		this.startOperation = startOperation;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Boolean getIsManual() {
		return isManual;
	}

	public void setIsManual(Boolean isManual) {
		this.isManual = isManual;
	}

	public String getModifiername() {
		return Modifiername;
	}

	public void setModifiername(String modifiername) {
		Modifiername = modifiername;
	}

	@Override
	public String toString() {
		return "GalleryDetail [id=" + id + ", regional=" + regional + ", name=" + name + ", address=" + address + ", latitude=" + latitude + ", longitude=" + longitude + ", liveStatus=" + liveStatus
				+ ", creationTime=" + creationTime + ", modificationtime=" + modificationtime + ", creator=" + creator + ", lastModifier=" + lastModifier + ", l1Manager=" + l1Manager + ", l2Manager="
				+ l2Manager + ", l3Manager=" + l3Manager + ", l4Manager=" + l4Manager + ", enabled=" + enabled + ", grade=" + grade + ", mondayToFridayTime=" + mondayToFridayTime + ", saturdayTime="
				+ saturdayTime + ", sundayTime=" + sundayTime + ", geography=" + geography + ", space=" + space + ", type=" + type + ", distributor=" + distributor + ", smile=" + smile + ", status="
				+ status + ", startOperation=" + startOperation + ", contactName=" + contactName + ", isDeleted=" + isDeleted + ", isManual=" + isManual + ", Modifiername=" + Modifiername + "]";
	}

}
