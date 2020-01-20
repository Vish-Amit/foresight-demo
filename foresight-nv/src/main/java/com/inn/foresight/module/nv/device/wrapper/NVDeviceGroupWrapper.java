package com.inn.foresight.module.nv.device.wrapper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.inn.commons.Symbol;
import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.module.nv.device.model.NVDeviceGroup;

/**
 * The Class NVDeviceGroupWrapper.
 *
 * @author innoeye
 * date - 24-Apr-2018 3:35:33 PM
 */

@JpaWrapper
@RestWrapper
public class NVDeviceGroupWrapper implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private Integer id;
	
	/** The group id. */
	private String groupId;
	
	/** The name. */
	private String name;
	
	/** The geography L 1. */
	private String geographyL1;

	/** The geography L 2. */
	private String geographyL2;

	/** The geography L 3. */
	private String geographyL3;

	/** The geography L 4. */
	private String geographyL4;
	
	/** The creation time. */
	private Date creationTime;

	/** The modification time. */
	private Date modificationTime;
	
	/** The latitude. */
	private Double latitude;

	/** The longitude. */
	private Double longitude;
	
	/** The location type. */
	private String locationType;
	
	/** The address. */
	private String address;
	
	/** The count. */
	private Long count;
	
	/** The devices. */
	private List<NVDeviceDataWrapper> devices;
	
	/** The creator. */
	private String creator;
	
	private String city;
	
	


	/** Instantiates a new NV device group wrapper. */
	public NVDeviceGroupWrapper() {
		super();
	}
	
	/**
	 * Instantiates a new NV device group wrapper.
	 *
	 * @param count the count
	 * @param name the name
	 * @param latitude the latitude
	 * @param longitude the longitude
	 * @param id the id
	 */
	public NVDeviceGroupWrapper(Long count, String name,
			Double latitude, Double longitude, Integer id) {
		super();
		this.count = count;
		this.id = id;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
	}


	/**
	 * Instantiates a new NV device group wrapper.
	 *
	 * @param id the id
	 * @param groupId the group id
	 * @param name the name
	 * @param creationTime the creation time
	 * @param modificationTime the modification time
	 * @param locationType the location type
	 * @param address the address
	 * @param latitude the latitude
	 * @param longitude the longitude
	 */
	public NVDeviceGroupWrapper(Integer id, String groupId, String name,
			Date creationTime, Date modificationTime, String locationType,
			String address, Double latitude, Double longitude) {
		super();
		this.id = id;
		this.groupId = groupId;
		this.name = name;
		this.creationTime = creationTime;
		this.modificationTime = modificationTime;
		this.locationType = locationType;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	
	/**
	 * Instantiates a new NV device group wrapper.
	 *
	 * @param id the id
	 * @param groupId the group id
	 * @param name the name
	 * @param city the city
	 */
	public NVDeviceGroupWrapper(Integer id, String groupId, String name, String city,
			Date creationTime, Date modificationTime, String locationType,
			String address, Double latitude, Double longitude) {
		super();
		this.id = id;
		this.groupId = groupId;
		this.name = name;
		this.city = city;
		this.creationTime = creationTime;
		this.modificationTime = modificationTime;
		this.locationType = locationType;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
	}



	/**
	 * Construct NVDeviceGroupWrapper object.
	 *
	 * @param deviceGroup the device group
	 */
	public NVDeviceGroupWrapper(NVDeviceGroup deviceGroup) {
		super();
		this.id = deviceGroup.getId();
		this.groupId = deviceGroup.getGroupId();
		this.name = deviceGroup.getName();
		this.geographyL1 = deviceGroup.getGeographyL1() != null ? deviceGroup.getGeographyL1().getName() : null;
		this.geographyL2 = deviceGroup.getGeographyL2() != null ? deviceGroup.getGeographyL2().getName() : null;
		this.geographyL3 = deviceGroup.getGeographyL3() != null ? deviceGroup.getGeographyL3().getName() : null;
		this.geographyL4 = deviceGroup.getGeographyL4() != null ? deviceGroup.getGeographyL4().getName() : null;
		this.creationTime = deviceGroup.getCreationTime();
		this.modificationTime = deviceGroup.getModificationTime();
		this.latitude = deviceGroup.getLatitude(); 
		this.longitude = deviceGroup.getLongitude();
		this.locationType = deviceGroup.getLocationType();
		this.address = deviceGroup.getAddress();
		this.creator = creator != null ? deviceGroup.getCreator().getFirstName() + Symbol.SPACE_STRING + deviceGroup.getCreator().getLastName() : null;
	}
	
	/**
	 * Instantiates a new NV device group wrapper.
	 *
	 * @param id the id
	 * @param groupId the group id
	 * @param name the name
	 * @param locationType the location type
	 * @param address the address
	 * @param latitude the latitude
	 * @param longitude the longitude
	 */
	public NVDeviceGroupWrapper(Integer id, String groupId, String name,
			String locationType,String address, Double latitude, Double longitude) {
		super();
		this.id = id;
		this.groupId = groupId;
		this.name = name;
		this.locationType = locationType;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
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
	public String getGeographyL1() {
		return geographyL1;
	}



	/**
	 * Sets the geography L 1.
	 *
	 * @param geographyL1 the new geography L 1
	 */
	public void setGeographyL1(String geographyL1) {
		this.geographyL1 = geographyL1;
	}



	/**
	 * Gets the geography L 2.
	 *
	 * @return the geography L 2
	 */
	public String getGeographyL2() {
		return geographyL2;
	}



	/**
	 * Sets the geography L 2.
	 *
	 * @param geographyL2 the new geography L 2
	 */
	public void setGeographyL2(String geographyL2) {
		this.geographyL2 = geographyL2;
	}



	/**
	 * Gets the geography L 3.
	 *
	 * @return the geography L 3
	 */
	public String getGeographyL3() {
		return geographyL3;
	}



	/**
	 * Sets the geography L 3.
	 *
	 * @param geographyL3 the new geography L 3
	 */
	public void setGeographyL3(String geographyL3) {
		this.geographyL3 = geographyL3;
	}



	/**
	 * Gets the geography L 4.
	 *
	 * @return the geography L 4
	 */
	public String getGeographyL4() {
		return geographyL4;
	}



	/**
	 * Sets the geography L 4.
	 *
	 * @param geographyL4 the new geography L 4
	 */
	public void setGeographyL4(String geographyL4) {
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
	 * Gets the devices.
	 *
	 * @return the devices
	 */
	public List<NVDeviceDataWrapper> getDevices() {
		return devices;
	}



	/**
	 * Sets the devices.
	 *
	 * @param devices the new devices
	 */
	public void setDevices(List<NVDeviceDataWrapper> devices) {
		this.devices = devices;
	}



	/**
	 * Gets the creator.
	 *
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}



	/**
	 * Sets the creator.
	 *
	 * @param creator the new creator
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}

	/**
	 * Sets the count.
	 *
	 * @param count the new count
	 */
	public void setCount(Long count) {
		this.count = count;
	}
	

	public String getCity() {
		return city;
	}

	/**
	 * Sets the city.
	 *
	 * @param city the new city
	 */
	public void setCity(String city) {
		this.city = city;
	}

}
