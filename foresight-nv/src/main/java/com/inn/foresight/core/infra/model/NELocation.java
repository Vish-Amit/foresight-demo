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

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NELType;
import com.inn.foresight.core.infra.utils.enums.Technology;
import com.inn.foresight.core.infra.utils.enums.Vendor;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;

/**
 * The Class NELocation.
 */

@NamedQueries({ @NamedQuery(name = "checkNELocationByNEId", query = "select e from NELocation e where e.nelId=:nelId"),
		@NamedQuery(name = "searchNELocationByNELIdAndNELType", query = "select ne from NELocation ne where ne.nelType in (:nelTypeList) and ne.nelId like :searchNELId"),
		@NamedQuery(name = "getNELocationDataList", query = "select new com.inn.foresight.core.infra.wrapper.NELocationWrapper(il.latitude,il.longitude,il.nelId,il.geographyL4.name,il.geographyL4.displayName,il.address,il.nelType,il.id,il.friendlyName,il.nelStatus,il.locationCode) from NELocation il  left join il.geographyL4 where il.latitude >= :southWestLat and il.latitude <= :northEastLat and il.longitude >=:southWestLong and il.longitude <=:northEastLong and il.nelType=:nelType"),
		@NamedQuery(name = "getNELocationListByLocationType", query = "select il.id,il.nelId from NELocation il where il.nelType=:nelType order by il.nelId asc"),
		@NamedQuery(name = "getNELocationDetailsByName", query = "select inv from NELocation inv where nelId =:name"),
		@NamedQuery(name = "getSearchNELocationName", query = "select il.id,il.nelId  from NELocation il where il.isDeleted=false and UPPER(il.nelId) like concat('%',:name,'%') and il.nelType in (:nelType) "),
		@NamedQuery(name = "getNELocationDetailsByNameAndType", query = "select inv from NELocation inv where inv.nelId =:name and inv.nelType=:type"),
		@NamedQuery(name = "getNeLocationByLocationCodeListAndType", query = "select nl from NELocation nl where nelType=:neType and locationCode in (:locationCodeList)"),
		@NamedQuery(name = "getNELocationByNEType", query ="select nl from NELocation nl where nelType=:neType and nelid in (:nelIdList)"),
		@NamedQuery(name = "getNELocationDetailByNELType", query = "select il.nelId,il.friendlyName from NELocation il where il.nelType=:nelType order by il.nelId asc"),
		@NamedQuery(name = "getNeLocationAndParamterByType", query = "select nel.id,nel.nelId,nel.locationCode,nelp.parameterName,nelp.value,nel.nelStatus,nel.createdTime,nel.modifiedTime from NELocation nel left join NELocationParameter nelp on nelp.nelocation.id=nel.id where nel.nelType=:nelType"),
		@NamedQuery(name = "getTotalNelocationCountByType", query = "select count(*) from NELocation nel where nel.nelType=:nelType"),
		@NamedQuery(name = "getGCCountByStatusAndGeography", query = "select new com.inn.foresight.core.infra.wrapper.NELocationWrapper(nel.nelStatus,nel.geographyL1.name, count(nel.nelId)) from NELocation nel where nel.nelType in (:nelType) and nel.nelStatus in (:nelStatus) group by nel.nelStatus,nel.geographyL1.name,nel.nelType"),
		@NamedQuery(name = "getGCDetailsByStatusAndGeography", query = "select new com.inn.foresight.core.infra.wrapper.NELocationWrapper(nel.id,nel.nelId,nel.nelStatus, nel.address,COUNT(distinct vcu.neId), COUNT(distinct vdu.neId), COUNT(distinct macro.neId)) from NELocation nel left join GeographyL1 l1  on nel.geographyL1.id = l1.id left join"
				+ " NetworkElement vcu on vcu.neLocation.id = nel.id and vcu.neType =:vcuNEType  AND vcu.isDeleted=0  left join  NetworkElement vdu on vcu.id = vdu.networkElement.id  AND vdu.neType =:vduNEType AND vdu.isDeleted=0 left join NetworkElement macro on  vdu.id = macro.networkElement.id AND macro.neType =:macroNEType AND macro.isDeleted=0 "
				+ "where l1.name=:geographyName and nel.nelType in (:nelType) and nel.nelStatus in (:nelStatus) group by nel.nelStatus,nel.nelId, nel.nelStatus,nel.address"),
		@NamedQuery(name = "filterByLocationCode", query = "select nel.locationCode from NELocation nel where nel.isDeleted=false and locationCode like concat(:locationcode,'%')"),
		@NamedQuery(name = "getNELocationByLocationCode", query = "select nel from NELocation nel where nel.locationCode=:locationCode")
})
		
@XmlRootElement(name = "NELocation")
@Entity
@Table(name = "NELocation")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class NELocation implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7716188767441692595L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "nelocationid_pk")
	private Integer id;

	/** The geography L 4. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl4id_fk", nullable = true)
	private GeographyL4 geographyL4;

	/** The nel type. */
	@Basic
	@Column(name = "neltype")
	@Enumerated(EnumType.STRING)
	private NELType nelType;

	/** The nel id. */
	@Basic
	@Column(name = "nelid")
	private String nelId;

	/** The technology. */
	@Basic
	@Column(name = "technology")
	@Enumerated(EnumType.STRING)
	private Technology technology;

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

	/** The latitude. */
	@Basic
	@Column(name = "latitude")
	private Double latitude;

	/** The longitude. */
	@Basic
	@Column(name = "longitude")
	private Double longitude;

	/** The created time. */
	@Basic
	@Column(name = "creationtime")
	private Date createdTime;

	/** The modified time. */
	@Basic
	@Column(name = "modificationtime")
	private Date modifiedTime;

	@Basic
	@Column(name = "address")
	private String address;

	@Basic
	@Column(name = "pincode")
	private Integer pincode;

	@Basic
	@Column(name = "locationcode")
	private String locationCode;

	@Basic
	@Column(name = "friendlyname")
	private String friendlyName;

	/** The geography L 3. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl3id_fk", nullable = true)
	private GeographyL3 geographyL3;

	/** The geography L 2. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl2id_fk", nullable = true)
	private GeographyL2 geographyL2;

	/** The geography L 1. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl1id_fk", nullable = true)
	private GeographyL1 geographyL1;

	/** The nel status. */
	@Basic
	@Column(name = "nelstatus")
	@Enumerated(EnumType.STRING)
	private NELStatus nelStatus;
	
	@Basic
	@Column(name = "deleted")
	private Boolean isDeleted = false;

	public enum NELStatus {
		DRAFTED, INSTANTIATED, REGISTERED, CONFIGURED, PLANNED, TORCOMPLETED, CVIMCOMPLETED, OSCOMPLETED, GCREADY,
		INSERVICE;
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
	 * Gets the latitude.
	 *
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude.
	 *
	 * @param latitude the new latitude
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Gets the longitude.
	 *
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude.
	 *
	 * @param longitude the new longitude
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
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
	 * Gets the nel type.
	 *
	 * @return the nel type
	 */
	public NELType getNelType() {
		return nelType;
	}

	/**
	 * Sets the nel type.
	 *
	 * @param nelType the new nel type
	 */
	public void setNelType(NELType nelType) {
		this.nelType = nelType;
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
	 * Gets the technology.
	 *
	 * @return the technology
	 */
	public Technology getTechnology() {
		return technology;
	}

	/**
	 * Sets the technology.
	 *
	 * @param technology the new technology
	 */
	public void setTechnology(Technology technology) {
		this.technology = technology;
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
	 * Gets the nel id.
	 *
	 * @return the nel id
	 */
	public String getNelId() {
		return nelId;
	}

	/**
	 * Sets the nel id.
	 *
	 * @param nelId the new nel id
	 */
	public void setNelId(String nelId) {
		this.nelId = nelId;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getPincode() {
		return pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	public GeographyL3 getGeographyL3() {
		return geographyL3;
	}

	public void setGeographyL3(GeographyL3 geographyL3) {
		this.geographyL3 = geographyL3;
	}

	public GeographyL2 getGeographyL2() {
		return geographyL2;
	}

	public void setGeographyL2(GeographyL2 geographyL2) {
		this.geographyL2 = geographyL2;
	}

	public GeographyL1 getGeographyL1() {
		return geographyL1;
	}

	public void setGeographyL1(GeographyL1 geographyL1) {
		this.geographyL1 = geographyL1;
	}

	public NELStatus getNelStatus() {
		return nelStatus;
	}

	public void setNelStatus(NELStatus nelStatus) {
		this.nelStatus = nelStatus;
	}
	
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NELocation [id=");
		builder.append(id);
		builder.append(", geographyL4=");
		builder.append(geographyL4);
		builder.append(", nelType=");
		builder.append(nelType);
		builder.append(", nelId=");
		builder.append(nelId);
		builder.append(", technology=");
		builder.append(technology);
		builder.append(", vendor=");
		builder.append(vendor);
		builder.append(", domain=");
		builder.append(domain);
		builder.append(", latitude=");
		builder.append(latitude);
		builder.append(", longitude=");
		builder.append(longitude);
		builder.append(", createdTime=");
		builder.append(createdTime);
		builder.append(", modifiedTime=");
		builder.append(modifiedTime);
		builder.append(", address=");
		builder.append(address);
		builder.append(", pincode=");
		builder.append(pincode);
		builder.append(", locationCode=");
		builder.append(locationCode);
		builder.append(", friendlyName=");
		builder.append(friendlyName);
		builder.append(", geographyL3=");
		builder.append(geographyL3);
		builder.append(", geographyL2=");
		builder.append(geographyL2);
		builder.append(", geographyL1=");
		builder.append(geographyL1);
		builder.append(", nelStatus=");
		builder.append(nelStatus);
		builder.append(", isDeleted=");
		builder.append(isDeleted);
		builder.append("]");
		return builder.toString();
	}

	

}
