package com.inn.foresight.core.infra.wrapper;

import java.util.Date;
import java.util.Map;

import com.inn.commons.lang.DateUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.Technology;
import com.inn.foresight.core.infra.utils.enums.Vendor;

/** The Class SiteCountWrapper. */
@JpaWrapper
public class SiteCountWrapper {
	/** The lsmr count. */
	private Long emsCount;

	/** The enb count. */
	private Long enbCount;

	/** The cell count. */
	private Long cellCount;

	/** The import date. */
	private String importDate;

	/** The import time. */
	private String importTime;

	/** The vendor. */
	private String vendor;

	/** The band. */
	private String band;

	/** The site type. */
	private String siteType;

	/** The lsmr host name. */
	private String emsHostName;

	/** The lsmr capacity. */
	private String emsCapacity;

	/** The utilization. */
	private Double utilization;

	/** The is master. */
	private Boolean isMaster;

	/** The is active. */
	private Boolean isActive;

	/** The technology. */
	private String technology;

	/** The vendor count. */
	private Long vendorCount;

	/** The master site id. */
	private Integer masterSiteId;

	/** The sapid. */
	private String sapid;

	/** The ne name. */
	private String neName;

	/** The ne id. */
	private String neId;

	/** The geography L 4. */
	private String geographyL4;

	/** The geography L 3. */
	private String geographyL3;

	/** The geography L 2. */
	private String geographyL2;

	/** The geography L 1. */
	private String geographyL1;

	/** The cell name. */
	private String cellName;

	/** The enb id. */
	private Integer enbId;

	/** The cellid. */
	private Integer cellid;

	/** The display name. */
	private String displayName;

	/** The row key. */
	private String rowKey;

	/** The band wise count. */
	Map<String, Map<String, Long>> bandWiseCount;

	/** The macrobwcount. */
	Map<String, Long> macrobwcount;

	/** The idscbwcount. */
	Map<String, Long> idscbwcount;

	/** The odscbwcount. */
	Map<String, Long> odscbwcount;

	/** The cnum. */
	private String cnum;

	/** The parse time. */
	private Long parseTime;

	/** The ne type. */
	private String neType;

	/** The ne status. */
	private String neStatus;

	/** The enb count map. */
	private Map<String, Long> enbCountMap;

	/** Instantiates a new site count wrapper. */
	public SiteCountWrapper() {
	}

	/**
	 * Gets the site type.
	 *
	 * @return the site type
	 */
	public String getSiteType() {
		return siteType;
	}

	/**
	 * Sets the site type.
	 *
	 * @param siteType the new site type
	 */
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	/**
	 * Gets the ems count.
	 *
	 * @return the ems count
	 */
	public Long getEmsCount() {
		return emsCount;
	}

	/**
	 * Sets the ems count.
	 *
	 * @param emsCount the new ems count
	 */
	public void setEmsCount(Long emsCount) {
		this.emsCount = emsCount;
	}

	/**
	 * Gets the enb count.
	 *
	 * @return the enb count
	 */
	public Long getEnbCount() {
		return enbCount;
	}

	/**
	 * Sets the enb count.
	 *
	 * @param enbCount the new enb count
	 */
	public void setEnbCount(Long enbCount) {
		this.enbCount = enbCount;
	}

	/**
	 * Gets the cell count.
	 *
	 * @return the cell count
	 */
	public Long getCellCount() {
		return cellCount;
	}

	/**
	 * Sets the cell count.
	 *
	 * @param cellCount the new cell count
	 */
	public void setCellCount(Long cellCount) {
		this.cellCount = cellCount;
	}

	/**
	 * Gets the display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name.
	 *
	 * @param displayName the new display name
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the ne name.
	 *
	 * @return the ne name
	 */
	public String getNeName() {
		return neName;
	}

	/**
	 * Sets the ne name.
	 *
	 * @param neName the new ne name
	 */
	public void setNeName(String neName) {
		this.neName = neName;
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
	 * Gets the ne status.
	 *
	 * @return the ne status
	 */
	public String getNeStatus() {
		return neStatus;
	}

	/**
	 * Sets the ne status.
	 *
	 * @param neStatus the new ne status
	 */
	public void setNeStatus(String neStatus) {
		this.neStatus = neStatus;
	}

	/**
	 * Gets the enb count map.
	 *
	 * @return the enb count map
	 */
	public Map<String, Long> getEnbCountMap() {
		return enbCountMap;
	}

	/**
	 * Sets the enb count map.
	 *
	 * @param enbCountMap the enb count map
	 */
	public void setEnbCountMap(Map<String, Long> enbCountMap) {
		this.enbCountMap = enbCountMap;
	}

	/**
	 * Gets the ne id.
	 *
	 * @return the ne id
	 */
	public String getNeId() {
		return neId;
	}

	/**
	 * Sets the ne id.
	 *
	 * @param neId the new ne id
	 */
	public void setNeId(String neId) {
		this.neId = neId;
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
	 * Gets the cell name.
	 *
	 * @return the cell name
	 */
	public String getCellName() {
		return cellName;
	}

	/**
	 * Sets the cell name.
	 *
	 * @param cellName the new cell name
	 */
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	/**
	 * Gets the import date.
	 *
	 * @return the import date
	 */
	public String getImportDate() {
		return importDate;
	}

	/**
	 * Sets the import date.
	 *
	 * @param importDate the new import date
	 */
	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}

	/**
	 * Gets the vendor.
	 *
	 * @return the vendor
	 */
	public String getVendor() {
		return vendor;
	}

	/**
	 * Sets the vendor.
	 *
	 * @param vendor the new vendor
	 */
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * Gets the band.
	 *
	 * @return the band
	 */
	public String getBand() {
		return band;
	}

	/**
	 * Sets the band.
	 *
	 * @param band the new band
	 */
	public void setBand(String band) {
		this.band = band;
	}

	/**
	 * Gets the ems host name.
	 *
	 * @return the ems host name
	 */
	public String getEmsHostName() {
		return emsHostName;
	}

	/**
	 * Sets the ems host name.
	 *
	 * @param emsHostName the new ems host name
	 */
	public void setEmsHostName(String emsHostName) {
		this.emsHostName = emsHostName;
	}

	/**
	 * /** Gets the utilization.
	 *
	 * @return the utilization
	 */
	public Double getUtilization() {
		return utilization;
	}

	/**
	 * Sets the utilization.
	 *
	 * @param utilization the new utilization
	 */
	public void setUtilization(Double utilization) {
		this.utilization = utilization;
	}

	/**
	 * Gets the import time.
	 *
	 * @return the import time
	 */
	public String getImportTime() {
		return importTime;
	}

	/**
	 * Sets the import time.
	 *
	 * @param importTime the new import time
	 */
	public void setImportTime(String importTime) {
		this.importTime = importTime;
	}

	/**
	 * Gets the checks if is master.
	 *
	 * @return the checks if is master
	 */
	public Boolean getIsMaster() {
		return isMaster;
	}

	/**
	 * Sets the checks if is master.
	 *
	 * @param isMaster the new checks if is master
	 */
	public void setIsMaster(Boolean isMaster) {
		this.isMaster = isMaster;
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
	 * Gets the master site id.
	 *
	 * @return the master site id
	 */
	public Integer getMasterSiteId() {
		return masterSiteId;
	}

	/**
	 * Sets the master site id.
	 *
	 * @param masterSiteId the new master site id
	 */
	public void setMasterSiteId(Integer masterSiteId) {
		this.masterSiteId = masterSiteId;
	}

	/**
	 * Gets the sapid.
	 *
	 * @return the sapid
	 */
	public String getSapid() {
		return sapid;
	}

	/**
	 * Sets the sapid.
	 *
	 * @param sapid the new sapid
	 */
	public void setSapid(String sapid) {
		this.sapid = sapid;
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
	 * Gets the vendor count.
	 *
	 * @return the vendor count
	 */
	public Long getVendorCount() {
		return vendorCount;
	}

	/**
	 * Sets the vendor count.
	 *
	 * @param vendorCount the new vendor count
	 */
	public void setVendorCount(Long vendorCount) {
		this.vendorCount = vendorCount;
	}

	/**
	 * Gets the ems capacity.
	 *
	 * @return the ems capacity
	 */
	public String getEmsCapacity() {
		return emsCapacity;
	}

	/**
	 * Sets the ems capacity.
	 *
	 * @param emsCapacity the new ems capacity
	 */
	public void setEmsCapacity(String emsCapacity) {
		this.emsCapacity = emsCapacity;
	}

	/**
	 * Gets the enb id.
	 *
	 * @return the enb id
	 */
	public Integer getEnbId() {
		return enbId;
	}

	/**
	 * Sets the enb id.
	 *
	 * @param enbId the new enb id
	 */
	public void setEnbId(Integer enbId) {
		this.enbId = enbId;
	}

	/**
	 * Gets the cellid.
	 *
	 * @return the cellid
	 */
	public Integer getCellid() {
		return cellid;
	}

	/**
	 * Sets the cellid.
	 *
	 * @param cellid the new cellid
	 */
	public void setCellid(Integer cellid) {
		this.cellid = cellid;
	}

	/**
	 * Gets the macrobwcount.
	 *
	 * @return the macrobwcount
	 */
	public Map<String, Long> getMacrobwcount() {
		return macrobwcount;
	}

	/**
	 * Gets the row key.
	 *
	 * @return the row key
	 */
	public String getRowKey() {
		return rowKey;
	}

	/**
	 * Sets the row key.
	 *
	 * @param rowKey the new row key
	 */
	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	/**
	 * Gets the band wise count.
	 *
	 * @return the band wise count
	 */
	public Map<String, Map<String, Long>> getBandWiseCount() {
		return bandWiseCount;
	}

	/**
	 * Sets the band wise count.
	 *
	 * @param bandWiseCount the band wise count
	 */
	public void setBandWiseCount(Map<String, Map<String, Long>> bandWiseCount) {
		this.bandWiseCount = bandWiseCount;
	}

	/**
	 * Sets the macrobwcount.
	 *
	 * @param macrobwcount the macrobwcount
	 */
	public void setMacrobwcount(Map<String, Long> macrobwcount) {
		this.macrobwcount = macrobwcount;
	}

	/**
	 * Gets the idscbwcount.
	 *
	 * @return the idscbwcount
	 */
	public Map<String, Long> getIdscbwcount() {
		return idscbwcount;
	}

	/**
	 * Sets the idscbwcount.
	 *
	 * @param idscbwcount the idscbwcount
	 */
	public void setIdscbwcount(Map<String, Long> idscbwcount) {
		this.idscbwcount = idscbwcount;
	}

	/**
	 * Gets the odscbwcount.
	 *
	 * @return the odscbwcount
	 */
	public Map<String, Long> getOdscbwcount() {
		return odscbwcount;
	}

	/**
	 * Sets the odscbwcount.
	 *
	 * @param odscbwcount the odscbwcount
	 */
	public void setOdscbwcount(Map<String, Long> odscbwcount) {
		this.odscbwcount = odscbwcount;
	}

	/**
	 * Gets the ne type.
	 *
	 * @return the ne type
	 */
	public String getNeType() {
		return neType;
	}

	/**
	 * Sets the ne type.
	 *
	 * @param neType the new ne type
	 */
	public void setNeType(String neType) {
		this.neType = neType;
	}

	/**
	 * Gets the cnum.
	 *
	 * @return the cnum
	 */
	public String getCnum() {
		return cnum;
	}

	/**
	 * Sets the cnum.
	 *
	 * @param cnum the new cnum
	 */
	public void setCnum(String cnum) {
		this.cnum = cnum;
	}

	/**
	 * Gets the parses the time.
	 *
	 * @return the parses the time
	 */
	public Long getParseTime() {
		return parseTime;
	}

	/**
	 * Sets the parses the time.
	 *
	 * @param parseTime the new parses the time
	 */
	public void setParseTime(Long parseTime) {
		this.parseTime = parseTime;
	}

	/**
	 * Instantiates a new site count wrapper.
	 *
	 * @param geographyL3 the geography L 3
	 * @param neId        the ne id
	 */
	public SiteCountWrapper(String geographyL3, String neId) {
		this.geographyL3 = geographyL3;
		this.neId = neId;
	}

	/**
	 * Instantiates a new site count wrapper.
	 *
	 * @param sapid        the sapid
	 * @param masterSiteId the master site id
	 */
	public SiteCountWrapper(String sapid, Integer masterSiteId) {
		this.masterSiteId = masterSiteId;
		this.sapid = sapid;
	}

	/**
	 * Instantiates a new site count wrapper.
	 * 
	 * @param cellCount the cell count
	 * @param enbCount  the enb count
	 */
	public SiteCountWrapper(Long cellCount, Long enbCount) {
		this.cellCount = cellCount;
		this.enbCount = enbCount;
	}

	/**
	 * Instantiates a new site count wrapper.
	 *
	 * @param band     the band
	 * @param enbCount the enb count
	 */
	public SiteCountWrapper(String band, Long enbCount) {
		this.band = band;
		this.enbCount = enbCount;
	}

	/**
	 * Instantiates a new site count wrapper.
	 *
	 * @param string       the string
	 * @param lsmrHostName the lsmr host name
	 * @param band         the band
	 * @param enbCount     the enb count
	 */
	public SiteCountWrapper(String string, String lsmrHostName, String band, Long enbCount) {
		this.enbCount = enbCount;
		this.band = band;
		this.emsHostName = lsmrHostName;
	}

	/**
	 * Instantiates a new site count wrapper.
	 *
	 * @param lsmrHostName the lsmr host name
	 * @param cellCount    the cell count
	 * @param enbCount     the enb count
	 */
	public SiteCountWrapper(String lsmrHostName, Long cellCount, Long enbCount) {
		this.enbCount = enbCount;
		this.cellCount = cellCount;
		this.emsHostName = lsmrHostName;
	}

	/**
	 *  
	 * GetSitesCountVendorWise. GetVendorWiseNodeCountForOtherRAN Instantiates a new
	 * site count wrapper.
	 *
	 * @param emsCount the ems count
	 * @param vendor       the vendor
	 * @param enbCount     the enb count
	 * @param neStatus     the ne status
	 * @param modifiedTime the modified time
	 */
	public SiteCountWrapper(Long emsCount, Vendor vendor, Long enbCount, NEStatus neStatus, Date modifiedTime) {
		this.emsCount = emsCount;
		if (vendor != null) {
			this.vendor = vendor.toString();
		}
		this.enbCount = enbCount;
		if (neStatus != null) {
			this.neStatus = neStatus.toString();
		}
		if (modifiedTime != null) {
			this.parseTime = modifiedTime.getTime();
			this.importDate = DateUtils.format(InfraConstants.DATE_MONTH_YEAR_CAPS, modifiedTime);
			this.importTime = DateUtils.format(ForesightConstants.TIME_FORMATE_HH_MM_A, modifiedTime);
		}
	}

	/**
	 * Instantiates a new site count wrapper.
	 *
	 * @param vendor       the vendor
	 * @param hostname     the hostname
	 * @param enbCount     the enb count
	 * @param neStatus     the ne status
	 * @param modifiedTime the modified time
	 */
	/** GetSitesCountEMSWise. */
	public SiteCountWrapper(Vendor vendor, String hostname, Long enbCount, NEStatus neStatus, Date modifiedTime) {
		if (vendor != null) {
			this.vendor = vendor.toString();
		}
		this.emsHostName = hostname;
		this.enbCount = enbCount;
		if (neStatus != null) {
			this.neStatus = neStatus.toString();
		}
		if (modifiedTime != null) {
			this.parseTime = modifiedTime.getTime();
			this.importDate = DateUtils.format(InfraConstants.DATE_MONTH_YEAR_CAPS, modifiedTime);
			this.importTime = DateUtils.format(ForesightConstants.TIME_FORMATE_HH_MM_A, modifiedTime);
		}
	}

	/**
	 * GetSitesCountTechnologyWise Instantiates a new site count wrapper.
	 *
	 * @param technology  the technology
	 * @param vendorCount the vendor count
	 * @param enbCount    the enb count
	 */
	public SiteCountWrapper(Technology technology, Long vendorCount, Long enbCount) {
		this.enbCount = enbCount;
		this.vendorCount = vendorCount;
		if (technology != null) {
			this.technology = technology.toString();
		}
	}

	/** 
	 * GetEMSWiseNodeCountsForOtherRAN Instantiates a new site count wrapper.
	 *
	 * @param vendor       the vendor
	 * @param hostname     the hostname
	 * @param enbCount     the enb count
	 * @param modifiedTime the modified time
	 */
	public SiteCountWrapper(Vendor vendor, String hostname, Long enbCount, Date modifiedTime) {
		if (vendor != null) {
			this.vendor = vendor.toString();
		}
		this.emsHostName = hostname;
		this.enbCount = enbCount;
		if (modifiedTime != null) {
			this.parseTime = modifiedTime.getTime();
			this.importDate = DateUtils.format(InfraConstants.DATE_MONTH_YEAR_CAPS, modifiedTime);
			this.importTime = DateUtils.format(ForesightConstants.TIME_FORMATE_HH_MM_A, modifiedTime);
		}
	}
	/**
	 * Named Query getVendorWiseEMSCount
	 * @param vendor
	 * @param technology
	 * @param emsCount
	 */
	public SiteCountWrapper(Vendor vendor, Technology technology , Long emsCount) {
		this.vendor = vendor.toString();
		this.technology = technology.toString();
		this.emsCount = emsCount;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SiteCountWrapper [emsCount=");
		builder.append(emsCount);
		builder.append(", enbCount=");
		builder.append(enbCount);
		builder.append(", cellCount=");
		builder.append(cellCount);
		builder.append(", importDate=");
		builder.append(importDate);
		builder.append(", importTime=");
		builder.append(importTime);
		builder.append(", vendor=");
		builder.append(vendor);
		builder.append(", band=");
		builder.append(band);
		builder.append(", siteType=");
		builder.append(siteType);
		builder.append(", emsHostName=");
		builder.append(emsHostName);
		builder.append(", emsCapacity=");
		builder.append(emsCapacity);
		builder.append(", utilization=");
		builder.append(utilization);
		builder.append(", isMaster=");
		builder.append(isMaster);
		builder.append(", isActive=");
		builder.append(isActive);
		builder.append(", technology=");
		builder.append(technology);
		builder.append(", vendorCount=");
		builder.append(vendorCount);
		builder.append(", masterSiteId=");
		builder.append(masterSiteId);
		builder.append(", sapid=");
		builder.append(sapid);
		builder.append(", neName=");
		builder.append(neName);
		builder.append(", neId=");
		builder.append(neId);
		builder.append(", geographyL4=");
		builder.append(geographyL4);
		builder.append(", geographyL3=");
		builder.append(geographyL3);
		builder.append(", geographyL2=");
		builder.append(geographyL2);
		builder.append(", geographyL1=");
		builder.append(geographyL1);
		builder.append(", cellName=");
		builder.append(cellName);
		builder.append(", enbId=");
		builder.append(enbId);
		builder.append(", cellid=");
		builder.append(cellid);
		builder.append(", displayName=");
		builder.append(displayName);
		builder.append(", rowKey=");
		builder.append(rowKey);
		builder.append(", bandWiseCount=");
		builder.append(bandWiseCount);
		builder.append(", macrobwcount=");
		builder.append(macrobwcount);
		builder.append(", idscbwcount=");
		builder.append(idscbwcount);
		builder.append(", odscbwcount=");
		builder.append(odscbwcount);
		builder.append(", cnum=");
		builder.append(cnum);
		builder.append(", parseTime=");
		builder.append(parseTime);
		builder.append(", neType=");
		builder.append(neType);
		builder.append(", neStatus=");
		builder.append(neStatus);
		builder.append(", enbCountMap=");
		builder.append(enbCountMap);
		builder.append("]");
		return builder.toString();
	}
}