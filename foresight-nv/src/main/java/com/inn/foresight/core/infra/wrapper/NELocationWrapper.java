package com.inn.foresight.core.infra.wrapper;

import com.inn.foresight.core.infra.model.NELocation.NELStatus;
import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.core.infra.utils.enums.NELType;

@RestWrapper
@JpaWrapper

public class NELocationWrapper {

	/** The latitude. */
	private Double latitude;

	/** The longitude. */
	private Double longitude;

	/** The name. */
	private String name;

	/** The count. */
	private Long count;

	/** The geography name. */
	private String geographyName;

	/** The geography display name. */
	private String geographyDisplayName;

	/** The address. */
	private String address;

	/** The locationType. */
	private String locationType;

	/** The locationCode. */
	private String locationCode;

	/** The id. */
	private Integer id;

	/** The status. */
	private String status;

	/** The friendlyname. */
	private String friendlyname;
	
	private String nelId;
	
	private Long vduCount;
	
	private Long vcuCount;
	
	private String gcCompletionTime;
	

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public String getGeographyName() {
		return geographyName;
	}

	public void setGeographyName(String geographyName) {
		this.geographyName = geographyName;
	}

	public String getGeographyDisplayName() {
		return geographyDisplayName;
	}

	public void setGeographyDisplayName(String geographyDisplayName) {
		this.geographyDisplayName = geographyDisplayName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFriendlyname() {
		return friendlyname;
	}

	public void setFriendlyname(String friendlyname) {
		this.friendlyname = friendlyname;
	}

	public String getNelId() {
		return nelId;
	}

	public void setNelId(String nelId) {
		this.nelId = nelId;
	}

	public Long getVduCount() {
		return vduCount;
	}

	public void setVduCount(Long vduCount) {
		this.vduCount = vduCount;
	}

	public Long getVcuCount() {
		return vcuCount;
	}

	public void setVcuCount(Long vcuCount) {
		this.vcuCount = vcuCount;
	}

	public String getGcCompletionTime() {
		return gcCompletionTime;
	}

	public void setGcCompletionTime(String gcCompletionTime) {
		this.gcCompletionTime = gcCompletionTime;
	}

	public NELocationWrapper() {
		super();
	}
	
	public NELocationWrapper(Integer id, String nelId, NELStatus status, String address, Long vcuCount, Long vduCount,Long count) {
		super();
		this.id = id;
		this.nelId = nelId;
		this.status = status != null ? status.name() : null;
		this.address = address;
		this.vduCount = vduCount;
		this.vcuCount = vcuCount;
		this.count = count;
	}
	
	public NELocationWrapper(NELStatus status, String geographyName, Long count) {
		super();
		this.status = status != null ? status.name() : null;
		this.geographyName = geographyName;
		
		this.count = count;
	}

	public NELocationWrapper(Double latitude, Double longitude, Long count, String geographyName) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.count = count;
		this.geographyName = geographyName;
	}

	public NELocationWrapper(Double latitude, Double longitude, Long count, String geographyName,String geographyDisplayName) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.count = count;
		this.geographyName = geographyName;
		this.geographyDisplayName=geographyDisplayName;
	}
	
	public NELocationWrapper(Double latitude, Double longitude, Long count, String geographyName,String geographyDisplayName,Integer id) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.count = count;
		this.geographyName = geographyName;
		this.geographyDisplayName=geographyDisplayName;
		this.id=id;
	}

	public NELocationWrapper(Double latitude, Double longitude, String name, String geographyName, String geographyDisplayName, String address,
			NELType locationType, Integer id, String friendlyname, NELStatus status, String locationCode) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.name = name;
		this.geographyName = geographyName;
		this.geographyDisplayName = geographyDisplayName;
		this.address = address;
		this.locationType =locationType!=null? locationType.name():null;
		this.id = id;
		this.friendlyname = friendlyname;
		this.status = status != null ? status.name() : null;
		this.locationCode = locationCode;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NELocationWrapper [latitude=");
		builder.append(latitude);
		builder.append(", longitude=");
		builder.append(longitude);
		builder.append(", name=");
		builder.append(name);
		builder.append(", count=");
		builder.append(count);
		builder.append(", geographyName=");
		builder.append(geographyName);
		builder.append(", geographyDisplayName=");
		builder.append(geographyDisplayName);
		builder.append(", address=");
		builder.append(address);
		builder.append(", locationType=");
		builder.append(locationType);
		builder.append(", locationCode=");
		builder.append(locationCode);
		builder.append(", id=");
		builder.append(id);
		builder.append(", status=");
		builder.append(status);
		builder.append(", friendlyname=");
		builder.append(friendlyname);
		builder.append(", nelId=");
		builder.append(nelId);
		builder.append(", vduCount=");
		builder.append(vduCount);
		builder.append(", vcuCount=");
		builder.append(vcuCount);
		builder.append(", gcCompletionTime=");
		builder.append(gcCompletionTime);
		builder.append("]");
		return builder.toString();
	}
}
