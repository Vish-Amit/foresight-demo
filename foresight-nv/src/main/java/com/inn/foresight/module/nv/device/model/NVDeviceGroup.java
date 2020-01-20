package com.inn.foresight.module.nv.device.model;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.device.constant.DeviceConstant;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.user.model.User;

/**
 * The Class NVDeviceGroup.
 *
 * @author innoeye
 * date - 23-Apr-2018 7:21:39 PM
 */
	@NamedQuery(name = DeviceConstant.GET_ALL_DEVICE_GROUP , query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper(n.id, n.groupId, n.name,n.geographyL4.geographyL3.name, n.creationTime, n.modificationTime, n.locationType,n.address,n.latitude,n.longitude)  from NVDeviceGroup n")
	@NamedQuery(name = DeviceConstant.GET_ALL_DEVICE_GROUP_COUNT , query = "select count(n) from NVDeviceGroup n where n.geographyL4.geographyL3.name is not null")
	@NamedQuery(name = DeviceConstant.GET_DEVICE_GROUP_WITHIN_VIEW_PORT , query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper(n.id, n.groupId, n.name,n.creationTime, n.modificationTime, n.locationType,n.address,n.latitude,n.longitude)  from NVDeviceGroup n where n.latitude>=:SWLat and n.latitude<=:NELat and n.longitude>=:SWLng and n.longitude<=:NELng")
	@NamedQuery(name = DeviceConstant.GET_ALL_DEVICE_GROUP_COUNT_GROUP_BY_GEOL1, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper(count(distinct n),n.geographyL4.geographyL3.geographyL2.geographyL1.name,n.geographyL4.geographyL3.geographyL2.geographyL1.latitude,n.geographyL4.geographyL3.geographyL2.geographyL1.longitude,n.geographyL4.geographyL3.geographyL2.geographyL1.id) from NVDeviceGroup n  where n.geographyL4.geographyL3.geographyL2.geographyL1.id in (select g.id from GeographyL1 g where g.latitude>=:SWLat and g.latitude<=:NELat and g.longitude>=:SWLng and g.longitude<=:NELng) and Date(n.modificationTime) <= Date(:endDate) group by n.geographyL4.geographyL3.geographyL2.geographyL1.name,n.geographyL4.geographyL3.geographyL2.geographyL1.latitude,n.geographyL4.geographyL3.geographyL2.geographyL1.longitude,n.geographyL4.geographyL3.geographyL2.geographyL1.id")
	@NamedQuery(name = DeviceConstant.GET_ALL_DEVICE_GROUP_COUNT_GROUP_BY_GEOL2, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper(count(distinct n),n.geographyL4.geographyL3.geographyL2.name,n.geographyL4.geographyL3.geographyL2.latitude,n.geographyL4.geographyL3.geographyL2.longitude,n.geographyL4.geographyL3.geographyL2.id) from NVDeviceGroup n  where n.geographyL4.geographyL3.geographyL2.id in (select g.id from GeographyL2 g where g.latitude>=:SWLat and g.latitude<=:NELat and g.longitude>=:SWLng and g.longitude<=:NELng) and Date(n.modificationTime) <= Date(:endDate) group by n.geographyL4.geographyL3.geographyL2.name,n.geographyL4.geographyL3.geographyL2.latitude,n.geographyL4.geographyL3.geographyL2.longitude,n.geographyL4.geographyL3.geographyL2.id")
	@NamedQuery(name = DeviceConstant.GET_ALL_DEVICE_GROUP_COUNT_GROUP_BY_GEOL3, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper(count(distinct n),n.geographyL4.geographyL3.name,n.geographyL4.geographyL3.latitude,n.geographyL4.geographyL3.longitude,n.geographyL4.geographyL3.id) from NVDeviceGroup n  where n.geographyL4.geographyL3.id in (select g.id from GeographyL3 g where g.latitude>=:SWLat and g.latitude<=:NELat and g.longitude>=:SWLng and g.longitude<=:NELng) and Date(n.modificationTime) <= Date(:endDate) group by n.geographyL4.geographyL3.name,n.geographyL4.geographyL3.latitude,n.geographyL4.geographyL3.longitude,n.geographyL4.geographyL3.id")
	@NamedQuery(name = DeviceConstant.GET_ALL_DEVICE_GROUP_COUNT_GROUP_BY_GEOL4, query = "select new com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper(count(distinct n),n.geographyL4.name,n.geographyL4.latitude,n.geographyL4.longitude,n.geographyL4.id) from NVDeviceGroup n  where n.geographyL4.id in (select g.id from GeographyL4 g where g.latitude>=:SWLat and g.latitude<=:NELat and g.longitude>=:SWLng and g.longitude<=:NELng) and Date(n.modificationTime) <= Date(:endDate) group by n.geographyL4.name,n.geographyL4.latitude,n.geographyL4.longitude,n.geographyL4.id")
	@NamedQuery(name = DeviceConstant.GET_ALL_DEVICE_GROUP_BY_VIEWPORT, query = "select distinct  new com.inn.foresight.module.nv.device.wrapper.NVDeviceGroupWrapper(n.id, n.groupId, n.name,n.locationType,n.address,n.latitude,n.longitude) from NVDeviceGroup n where n.latitude>=:SWLat and n.latitude<=:NELat and n.longitude>=:SWLng and n.longitude<=:NELng and Date(n.modificationTime) <= Date(:endDate) ")

@Entity
@Table(name = "NVDeviceGroup")
@XmlRootElement(name = "NVDeviceGroup")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NVDeviceGroup implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "nvdevicegroupid_pk")
	private Integer id;
	
	/** The group id. */
	@Column(name = "groupid")
	private String groupId;
	
	/** The name. */
	@Column(name = "name")
	private String name;
	
	/** The geographyL1. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl1id_fk", nullable = false)
	private GeographyL1 geographyL1;

	/** The geographyL2. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl2id_fk", nullable = false)
	private GeographyL2 geographyL2;

	/** The geographyL3. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl3id_fk", nullable = false)
	private GeographyL3 geographyL3;

	/** The geographyL4. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl4id_fk", nullable = false)
	private GeographyL4 geographyL4;
	
	/** The creation time. */
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modification time. */
	@Column(name = "modificationtime")
	private Date modificationTime;
	
	/** The latitude. */
	@Column(name = "latitude")
	private Double latitude;

	/** The longitude. */
	@Column(name = "longitude")
	private Double longitude;
	
	/** The location type. */
	@Column(name = "locationtype")
	private String locationType;
	
	/** The address. */
	@Column(name = "locationinfo")
	private String address;
	
	/** The creator. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creatorid_fk", nullable = false)
	private User creator;

	/** Instantiates a new NV device group. */
	public NVDeviceGroup() {
		super();
	}

	/**
	 * Construct NVDeviceGroup object.
	 *
	 * @param deviceGroup the device group
	 */
	public NVDeviceGroup(NVDeviceGroup deviceGroup) {
		super();
		this.name = deviceGroup.getName();
		this.creationTime = new Date();
		this.modificationTime = new Date();
		this.latitude = deviceGroup.getLatitude(); 
		this.longitude = deviceGroup.getLongitude();
		this.locationType = deviceGroup.getLocationType();
		this.address = deviceGroup.getAddress();
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
	 * Gets the group id.
	 *
	 * @return the group id
	 */
	public String getGroupId() {
		return groupId;
	}

	/**
	 * Sets the group id.
	 *
	 * @param groupId the new group id
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
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
	 * Gets the location type.
	 *
	 * @return the location type
	 */
	public String getLocationType() {
		return locationType;
	}

	/**
	 * Sets the location type.
	 *
	 * @param locationType the new location type
	 */
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	/**
	 * Gets the address.
	 *
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the address.
	 *
	 * @param address the new address
	 */
	public void setAddress(String address) {
		this.address = address;
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
	 * Sets the creator.
	 *
	 * @param creator the new creator
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
}
