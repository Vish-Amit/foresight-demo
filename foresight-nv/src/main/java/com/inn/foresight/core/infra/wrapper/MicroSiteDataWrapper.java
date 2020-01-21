package com.inn.foresight.core.infra.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.foresight.core.infra.utils.enums.NEType;
@JpaWrapper
public class MicroSiteDataWrapper {

	private Double latitude;
	private Double longitude;
	private Integer azimuth;
	private String sapid;
	private Integer cellid;
	private String cNum;
	private String circle;
	private String regonalCenter;
	private String rowKey;
	private String city;
	private Integer id;
	private String band;
	private Integer enodeBId;
	private String neType;
	private Integer sector;
	private String zone;
	private String displayName;
	private String carrier;

	public MicroSiteDataWrapper(String band, String sapid, String cellName, Integer cellId, Integer enbId, String circleName,
			String zoneName, String cityName, String clusterName) {
		this.band = band;
		this.sapid = sapid;
		this.cNum = cellName;
		this.cellid = cellId;
		this.enodeBId = enbId;
		this.circle = circleName;
		this.zone = zoneName;
		this.city = cityName;
		this.regonalCenter = clusterName;
	}

	public MicroSiteDataWrapper(Integer id, String neName, Integer cellId, String cellName, String neFrequency,
			Integer sector, Integer enodeBId, NEType neType, String geographyL2, String geographyL3,
			String geographyL4) {
		this.sapid = neName;
		this.cellid = cellId;
		this.cNum = cellName;
		this.band = neFrequency;
		this.enodeBId = enodeBId;
		this.neType = "" + neType;
		this.sector = sector;
		this.id = id;
		this.circle = geographyL2;
		this.city = geographyL3;
		this.regonalCenter = geographyL4;
	}

	public MicroSiteDataWrapper(Integer id, String neName, Integer cellId, String cellName, String neFrequency,
			Integer sector, Integer enodeBId, NEType neType) {
		this.sapid = neName;
		this.cellid = cellId;
		this.cNum = cellName;
		this.band = neFrequency;
		this.enodeBId = enodeBId;
		this.neType = "" + neType;
		this.sector = sector;
		this.id = id;

	}

	/** getNEDetailAndRowKey **/
	public MicroSiteDataWrapper(String rowkey, Integer id, String neId, String neName, Integer cellId, String neFrequency,
			Integer sector, Integer enodeBId, NEType neType, String city, String circle) {
		this.displayName = neName;
		this.sapid = neId;
		this.cellid = cellId;
		this.band = neFrequency;
		this.enodeBId = enodeBId;
		if (neType != null) {
            this.neType = neType.toString();
        }
		this.sector = sector;
		this.id = id;
		this.city = city;
		this.circle = circle;
		this.rowKey = rowkey;
	}

	public MicroSiteDataWrapper(Integer id, String neId, String neName, Integer cellId, String neFrequency, Integer sector,
			Integer enodeBId, NEType neType, String city, String circle) {
		this.displayName = neName;
		this.sapid = neId;
		this.cellid = cellId;
		this.band = neFrequency;
		this.enodeBId = enodeBId;
		if (neType != null) {
            this.neType = neType.toString();
        }
		this.sector = sector;
		this.id = id;
		this.city = city;
		this.circle = circle;
	}

	public MicroSiteDataWrapper(Integer id, String neName, String displayName, Integer cellId, String cellName,
			String neFrequency, Integer sector, Integer enodeBId, NEType neType, String rowKey, String cityName,
			String circleName) {
		this.sapid = neName;
		this.displayName = displayName;
		this.cellid = cellId;
		this.cNum = cellName;
		this.band = neFrequency;
		this.enodeBId = enodeBId;
		this.neType = "" + neType;
		this.sector = sector;
		this.id = id;
		this.rowKey = rowKey;
		this.city = cityName;
		this.circle = circleName;

	}

	public MicroSiteDataWrapper(String neName, String circle, String city, String regonalCenter) {
		this.sapid = neName;
		this.circle = circle;
		this.city = city;
		this.regonalCenter = regonalCenter;

	}

	// getNEDetailByRowKeys
	public MicroSiteDataWrapper(String rowKey, String cityName, String circleName) {
		this.rowKey = rowKey;
		this.city = cityName;
		this.circle = circleName;
	}

	//Ran Performance Graph
	public MicroSiteDataWrapper(String band, String sapid,  Integer cellId, String carrier) {
		this.band = band;
		this.sapid = sapid;
		this.cellid = cellId;
		this.carrier = carrier;
	}
	
	//Ran Performance Graph2
	public MicroSiteDataWrapper(String band, String sapid,  Integer cellId, String carrier,Integer sector) {
		this.band = band;
		this.sapid = sapid;
		this.cellid = cellId;
		this.carrier = carrier;
		this.sector=sector;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSector() {
		return sector;
	}

	public void setSector(Integer sector) {
		this.sector = sector;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getNeType() {
		return neType;
	}

	public void setNeType(String neType) {
		this.neType = neType;
	}

	public MicroSiteDataWrapper() {
		super();
	}

	public Integer getEnodeBId() {
		return enodeBId;
	}

	public void setEnodeBId(Integer enodeBId) {
		this.enodeBId = enodeBId;
	}

	public String getRowKey() {
		return rowKey;
	}

	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	public MicroSiteDataWrapper(String sapid, Integer sectorid) {
		super();
		this.sapid = sapid;
		this.cNum = "" + sectorid;
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

	public Integer getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(Integer azimuth) {
		this.azimuth = azimuth;
	}

	public String getSapid() {
		return sapid;
	}

	public void setSapid(String sapid) {
		this.sapid = sapid;
	}

	public Integer getCellid() {
		return cellid;
	}

	public void setCellid(Integer cellid) {
		this.cellid = cellid;
	}

	@JsonIgnore
	public String getCircle() {
		return circle;
	}

	@JsonIgnore
	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getcNum() {
		return cNum;
	}

	public void setcNum(String cNum) {
		this.cNum = cNum;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getRegonalCenter() {
		return regonalCenter;
	}

	public void setRegonalCenter(String regonalCenter) {
		this.regonalCenter = regonalCenter;
	}

	
	public MicroSiteDataWrapper(String neid, String geographyName) {
		super();
		this.sapid = neid;
		this.regonalCenter = geographyName;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SiteDataWrapper [latitude=");
		builder.append(latitude);
		builder.append(", longitude=");
		builder.append(longitude);
		builder.append(", azimuth=");
		builder.append(azimuth);
		builder.append(", sapid=");
		builder.append(sapid);
		builder.append(", cellid=");
		builder.append(cellid);
		builder.append(", cNum=");
		builder.append(cNum);
		builder.append(", circle=");
		builder.append(circle);
		builder.append(", regonalCenter=");
		builder.append(regonalCenter);
		builder.append(", rowKey=");
		builder.append(rowKey);
		builder.append(", city=");
		builder.append(city);
		builder.append(", id=");
		builder.append(id);
		builder.append(", band=");
		builder.append(band);
		builder.append(", enodeBId=");
		builder.append(enodeBId);
		builder.append(", neType=");
		builder.append(neType);
		builder.append(", sector=");
		builder.append(sector);
		builder.append(", zone=");
		builder.append(zone);
		builder.append(", displayName=");
		builder.append(displayName);
		builder.append(", carrier=");
		builder.append(carrier);
		builder.append("]");
		return builder.toString();
	}
}