package com.inn.foresight.core.infra.wrapper;

import java.util.Date;

import com.inn.commons.lang.NumberUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.utils.enums.Technology;
import com.inn.foresight.core.infra.utils.enums.Vendor;

/** The Class SiteDataWrapper. */
public class SiteDataWrapper {
	/** The geographyL4Name. */
	private String geographyL4Name;

	/** The display name. */
	private String displayName;

	/** The latitude. */
	private Double latitude;

	/** The longitude. */
	private Double longitude;

	/** The azimuth. */
	private Integer azimuth;

	/** The sapid. */
	private String sapId;

	/** The cellid. */
	private Integer cellId;

	/** The circle. */
	private String geographyL2Name;

	/** The pci. */
	private Integer pci;

	/** The antenna height. */
	private String antennaHeight;

	/** The band. */
	private Integer band;

	/** The elec tilt. */
	private Integer elecTilt;

	/** The mech tilt. */
	private Integer mechTilt;

	/** The antena model. */
	private String antenaModel;

	/** The band class. */
	private String bandClass;

	/** The band width. */
	private String bandWidth;

	/** The hz beam width. */
	private Double hzBeamWidth;

	/** The tx power. */
	private String txPower;

	/** The city. */
	private String geographyL3Name;

	/** The enode B id. */
	private Integer enodeBId;

	/** The ne type. */
	private String neType;

	/** The sector id. */
	private Integer sectorId;

	/** The id. */
	private Integer id;

	/** The row key. */
	private String rowKey;

	/** The date. */
	private Date date;

	/** The last parse date. */
	private String lastParseDate;

	/** The last parse time. */
	private String lastParseTime;

	/** The last parse time stamp. */
	private Long lastParseTimeStamp;

	/** The geographyL1Name. */
	private String geographyL1Name;

	/** The domain. */
	private String domain;

	/** The vendor. */
	private String vendor;

	/** The technology. */
	private String technology;

	/** Instantiates a new site data wrapper. */
	public SiteDataWrapper() {
	}

	/**
	 * GetSmallCellSiteDetail,getMacroCellDetail,getSCSDetailAndRowkeyBySector,getMSDetailAndRowkeyBySector,getSCSDetailAndRowkeyByNEId,getMSDetailAndRowkeyByNEId*.
	 *
	 * @param modifiedTime the modified time
	 * @param rowkey       the rowkey
	 * @param id           the id
	 * @param neId         the ne id
	 * @param neName       the ne name
	 * @param cellId       the cell id
	 * @param neFrequency  the ne frequency
	 * @param sector       the sector
	 * @param enodeBId     the enode B id
	 * @param neType       the ne type
	 * @param city         the city
	 * @param circle       the circle
	 */
	public SiteDataWrapper(Date modifiedTime, String rowkey, Integer id, String neId, String neName, Integer cellId,
			String neFrequency, Integer sector, Integer enodeBId, NEType neType, String city, String circle) {
		this.date = modifiedTime;
		this.displayName = neName;
		this.sapId = neId;
		this.cellId = cellId;
		this.sectorId = sector;
		this.id = id;
		this.geographyL3Name = city;
		this.geographyL2Name = circle;
		this.rowKey = rowkey;
		this.enodeBId = enodeBId;
		if (neType != null) {
			this.neType = neType.toString();
		}
		if (NumberUtils.isParsable(neFrequency)) {
			this.band = Integer.valueOf(neFrequency);
		}
		if (modifiedTime != null) {
			this.lastParseDate = Utils.parseDateToStringFormat(modifiedTime,
					ForesightConstants.DATE_FORMATE_DD_MMM_YYYY);
			this.lastParseTime = Utils.parseDateToStringFormat(modifiedTime, ForesightConstants.TIME_FORMATE_HH_MM_A);
			this.lastParseTimeStamp = modifiedTime.getTime();
		}
		this.bandClass = Utils.getDivisionDuplexByBand(neFrequency);
	}

	/**
	 * Instantiates a new site data wrapper.
	 *
	 * @param latitude      the latitude
	 * @param longitude     the longitude
	 * @param azimuth       the azimuth
	 * @param sapid         the sapid
	 * @param cellid        the cellid
	 * @param sectorid      the sectorid
	 * @param pci           the pci
	 * @param antennaHeight the antenna height
	 * @param band          the band
	 */
	public SiteDataWrapper(Double latitude, Double longitude, Integer azimuth, String sapid, int cellid, int sectorid,
			Integer pci, String antennaHeight, Integer band) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.azimuth = azimuth;
		this.sapId = sapid;
		this.cellId = cellid;
		this.sectorId = sectorid;
		this.pci = pci;
		this.antennaHeight = antennaHeight;
		this.band = band;
	}

	/**
	 * Instantiates a new site data wrapper.
	 *
	 * @param latitude      the latitude
	 * @param longitude     the longitude
	 * @param azimuth       the azimuth
	 * @param sapid         the sapid
	 * @param cellid        the cellid
	 * @param sectorid      the sectorid
	 * @param pci           the pci
	 * @param antennaHeight the antenna height
	 * @param band          the band
	 * @param elecTilt      the elec tilt
	 * @param mechTilt      the mech tilt
	 * @param antenaModel   the antena model
	 * @param bandClass     the band class
	 */
	public SiteDataWrapper(Double latitude, Double longitude, Integer azimuth, String sapid, int cellid, int sectorid,
			Integer pci, String antennaHeight, Integer band, Integer elecTilt, Integer mechTilt, String antenaModel,
			String bandClass) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.azimuth = azimuth;
		this.sapId = sapid;
		this.cellId = cellid;
		this.sectorId = sectorid;
		this.pci = pci;
		this.antennaHeight = antennaHeight;
		this.band = band;
		this.elecTilt = elecTilt;
		this.mechTilt = mechTilt;
		this.antenaModel = antenaModel;
		this.bandClass = bandClass;
	}

	/**
	 * Instantiates a new site data wrapper.
	 *
	 * @param latitude      the latitude
	 * @param longitude     the longitude
	 * @param azimuth       the azimuth
	 * @param sapid         the sapid
	 * @param cellid        the cellid
	 * @param sectorid      the sectorid
	 * @param pci           the pci
	 * @param antennaHeight the antenna height
	 * @param band          the band
	 * @param elecTilt      the elec tilt
	 * @param mechTilt      the mech tilt
	 * @param antenaModel   the antena model
	 * @param bandClass     the band class
	 * @param bandWidth     the band width
	 */
	public SiteDataWrapper(Double latitude, Double longitude, Integer azimuth, String sapid, int cellid, int sectorid,
			Integer pci, String antennaHeight, Integer band, Integer elecTilt, Integer mechTilt, String antenaModel,
			String bandClass, String bandWidth) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.azimuth = azimuth;
		this.sapId = sapid;
		this.cellId = cellid;
		this.sectorId = sectorid;
		this.pci = pci;
		this.antennaHeight = antennaHeight;
		this.band = band;
		this.elecTilt = elecTilt;
		this.mechTilt = mechTilt;
		this.antenaModel = antenaModel;
		this.bandClass = bandClass;
		this.bandWidth = bandWidth;
	}

	/**
	 * Instantiates a new site data wrapper.
	 *
	 * @param latitude      the latitude
	 * @param longitude     the longitude
	 * @param azimuth       the azimuth
	 * @param sapid         the sapid
	 * @param cellid        the cellid
	 * @param sectorid      the sectorid
	 * @param pci           the pci
	 * @param antennaHeight the antenna height
	 * @param band          the band
	 * @param elecTilt      the elec tilt
	 * @param mechTilt      the mech tilt
	 * @param antenaModel   the antena model
	 * @param bandClass     the band class
	 * @param bandWidth     the band width
	 * @param hzBeamWidth   the hz beam width
	 * @param txPower       the tx power
	 */
	public SiteDataWrapper(Double latitude, Double longitude, Integer azimuth, String sapid, int cellid, int sectorid,
			Integer pci, String antennaHeight, Integer band, Integer elecTilt, Integer mechTilt, String antenaModel,
			String bandClass, String bandWidth, Double hzBeamWidth, String txPower) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.azimuth = azimuth;
		this.sapId = sapid;
		this.cellId = cellid;
		this.sectorId = sectorid;
		this.pci = pci;
		this.antennaHeight = antennaHeight;
		this.band = band;
		this.elecTilt = elecTilt;
		this.mechTilt = mechTilt;
		this.antenaModel = antenaModel;
		this.bandClass = bandClass;
		this.bandWidth = bandWidth;
		this.hzBeamWidth = hzBeamWidth;
		this.txPower = txPower;
	}

	/**
	 * Instantiates a new site data wrapper.
	 *
	 * @param neName      the ne name
	 * @param displayName the display name
	 * @param geoL1       the geo L 1
	 * @param geoL2       the geoL2
	 * @param geoL3       the geoL3
	 * @param geoL4       the geoL4
	 */
	public SiteDataWrapper(String neName, String displayName, String geoL1, String geoL2, String geoL3, String geoL4) {
		this.sapId = neName;
		this.geographyL1Name = geoL1;
		this.geographyL2Name = geoL2;
		this.geographyL3Name = geoL3;
		this.geographyL4Name = geoL4;
		this.displayName = displayName;
	}

	/**
	 * Instantiates a new site data wrapper.
	 *
	 * @param latitude  the latitude
	 * @param longitude the longitude
	 * @param azimuth   the azimuth
	 * @param sapid     the sapid
	 * @param cellid    the cellid
	 * @param sectorid  the sectorid
	 * @param circle    the circle
	 */
	public SiteDataWrapper(Double latitude, Double longitude, Integer azimuth, String sapid, int cellid, int sectorid,
			String circle) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.azimuth = azimuth;
		this.sapId = sapid;
		this.cellId = cellid;
		this.sectorId = sectorid;
		this.geographyL2Name = circle;
	}

	/**
	 * Instantiates a new site data wrapper.
	 *
	 * @param sapid    the sapid
	 * @param sectorid the sectorid
	 */
	public SiteDataWrapper(String sapid, int sectorid) {
		this.sapId = sapid;
		this.sectorId = sectorid;
	}

	/**
	 * GetNetworkElementDetailByNEPk *.
	 *
	 * @param modifiedTime    the modified time
	 * @param rowkey          the rowkey
	 * @param id              the id
	 * @param neId            the ne id
	 * @param neName          the ne name
	 * @param neType          the ne type
	 * @param geographyL3Name the geography L 3 name
	 * @param geographyL2Name the geography L 2 name
	 * @param neFrequency     the ne frequency
	 */
	public SiteDataWrapper(Date modifiedTime, String rowkey, Integer id, String neId, String neName, NEType neType,
			String geographyL3Name, String geographyL2Name, String neFrequency) {
		this.date = modifiedTime;
		this.displayName = neName;
		this.sapId = neId;
		this.id = id;
		this.geographyL3Name = geographyL3Name;
		this.geographyL2Name = geographyL2Name;
		this.rowKey = rowkey;
		if (neType != null) {
			this.neType = neType.toString();
		}
		if (modifiedTime != null) {
			this.lastParseDate = Utils.parseDateToStringFormat(modifiedTime,
					ForesightConstants.DATE_FORMATE_DD_MMM_YYYY);
			this.lastParseTime = Utils.parseDateToStringFormat(modifiedTime, ForesightConstants.TIME_FORMATE_HH_MM_A);
			this.lastParseTimeStamp = modifiedTime.getTime();
		}
		this.band = neFrequency != null ? Integer.parseInt(neFrequency) : null;
		this.bandClass = Utils.getDivisionDuplexByBand(neFrequency);
	}

	/**
	 * GetNetworkElementDetailForMacroSite *.
	 *
	 * @param modifiedTime    the modified time
	 * @param rowkey          the rowkey
	 * @param id              the id
	 * @param neId            the ne id
	 * @param neName          the ne name
	 * @param enodeBId        the enode B id
	 * @param neType          the ne type
	 * @param geographyL3Name the geography L 3 name
	 * @param geographyL2Name the geography L 2 name
	 */
	public SiteDataWrapper(Date modifiedTime, String rowkey, Integer id, String neId, String neName, String enodeBId,
			NEType neType, String geographyL3Name, String geographyL2Name) {
		this.date = modifiedTime;
		this.displayName = neName;
		this.sapId = neId;
		this.id = id;
		this.geographyL3Name = geographyL3Name;
		this.geographyL2Name = geographyL2Name;
		this.rowKey = rowkey;
		if (enodeBId != null) {
			this.enodeBId = Integer.valueOf(enodeBId);
		}
		if (neType != null) {
			this.neType = neType.toString();
		}
		if (modifiedTime != null) {
			this.lastParseDate = Utils.parseDateToStringFormat(modifiedTime,
					ForesightConstants.DATE_FORMATE_DD_MMM_YYYY);
			this.lastParseTime = Utils.parseDateToStringFormat(modifiedTime, ForesightConstants.TIME_FORMATE_HH_MM_A);
			this.lastParseTimeStamp = modifiedTime.getTime();
		}
	}

	/**
	 * GetNESiteDetail *.
	 *
	 * @param modifiedTime    the modified time
	 * @param rowkey          the rowkey
	 * @param id              the id
	 * @param neId            the ne id
	 * @param neName          the ne name
	 * @param enodeBId        the enode B id
	 * @param neType          the ne type
	 * @param geographyL3Name the geography L 3 name
	 * @param geographyL2Name the geography L 2 name
	 * @param band            the band
	 */
	public SiteDataWrapper(Date modifiedTime, String rowkey, Integer id, String neId, String neName, String enodeBId,
			NEType neType, String geographyL3Name, String geographyL2Name, String band) {
		this.date = modifiedTime;
		this.displayName = neName;
		this.sapId = neId;
		this.id = id;
		this.geographyL3Name = geographyL3Name;
		this.geographyL2Name = geographyL2Name;
		this.rowKey = rowkey;
		if (enodeBId != null) {
			this.enodeBId = Integer.valueOf(enodeBId);
		}
		if (neType != null) {
			this.neType = neType.toString();
		}
		if (modifiedTime != null) {
			this.lastParseDate = Utils.parseDateToStringFormat(modifiedTime,
					ForesightConstants.DATE_FORMATE_DD_MMM_YYYY);
			this.lastParseTime = Utils.parseDateToStringFormat(modifiedTime, ForesightConstants.TIME_FORMATE_HH_MM_A);
			this.lastParseTimeStamp = modifiedTime.getTime();
		}
		this.bandClass = Utils.getDivisionDuplexByBand(band);
	}

	/**
	 * Instantiates a new site data wrapper.
	 *
	 * @param band     the band
	 * @param neName   the ne name
	 * @param enodeBId the enode B id
	 * @param id       the id
	 */
	// forTrialWotogetSites
	public SiteDataWrapper(String band, String neName, String enodeBId, Integer id) {
		this.band = band != null ? Integer.parseInt(band) : null;
		this.sapId = neName;
		this.enodeBId = enodeBId != null ? Integer.parseInt(enodeBId) : null;
		this.id = id;
	}

	/**
	 * Instantiates a new site data wrapper.
	 *
	 * @param cellId   the cell id
	 * @param enodeBId the enode B id
	 * @param neName   the ne name
	 * @param band     the band
	 * @param id       the id
	 */
	// forTrialWotogetCells
	public SiteDataWrapper(Integer cellId, Integer enodeBId, String neName, String band, Integer id) {
		this.cellId = cellId;
		this.enodeBId = enodeBId;
		this.sapId = neName;
		this.band = band != null ? Integer.parseInt(band) : null;
		this.id = id;
	}

	/**
	 * Instantiates a new site data wrapper.
	 *
	 * @param modifiedTime the modified time
	 * @param rowkey       the rowkey
	 * @param id           the id
	 * @param neId         the ne id
	 * @param neName       the ne name
	 * @param neType       the ne type
	 * @param domain       the domain
	 */
	// (cm.modifiedTime,cm.rowkey,n.id,n.neId, n.neName,n.neType,n.domain) from
	// CMHbaseIndex cm inner join NetworkElement n on n.id=cm.networkElement.id
	// where n.neId=:neId and n.isDeleted=false and upper(n.vendor)=:vendor and
	// upper(n.technology)=:technology and upper(n.domain)=:domain order by
	// cm.modifiedTime"),
	public SiteDataWrapper(Date modifiedTime, String rowkey, Integer id, String neId, String neName, NEType neType,
			Domain domain) {
		this.date = modifiedTime;
		this.displayName = neName;
		this.sapId = neId;
		this.id = id;
		this.rowKey = rowkey;
		if (neType != null) {
			this.neType = neType.toString();
		}
		if (modifiedTime != null) {
			this.lastParseDate = Utils.parseDateToStringFormat(modifiedTime,
					ForesightConstants.DATE_FORMATE_DD_MMM_YYYY);
			this.lastParseTime = Utils.parseDateToStringFormat(modifiedTime, ForesightConstants.TIME_FORMATE_HH_MM_A);
			this.lastParseTimeStamp = modifiedTime.getTime();
		}
		if (domain != null) {
			this.domain = domain.displayName();
		}
	}

	/**
	 * GetNESiteDetail Instantiates a new site data wrapper.
	 *
	 * @param modifiedTime    the modified time
	 * @param rowkey          the rowkey
	 * @param id              the id
	 * @param neId            the ne id
	 * @param neName          the ne name
	 * @param enodeBId        the enode B id
	 * @param neType          the ne type
	 * @param geographyL3Name the geography L 3 name
	 * @param geographyL2Name the geography L 2 name
	 * @param band            the band
	 * @param domain          the domain
	 * @param vendor          the vendor
	 * @param technology      the technology
	 */
	public SiteDataWrapper(Date modifiedTime, String rowkey, Integer id, String neId, String neName, Integer enodeBId,
			NEType neType, String geographyL3Name, String geographyL2Name, String band, Domain domain, Vendor vendor,
			Technology technology) {
		this.date = modifiedTime;
		this.displayName = neName;
		this.sapId = neId;
		this.id = id;
		this.geographyL3Name = geographyL3Name;
		this.geographyL2Name = geographyL2Name;
		this.rowKey = rowkey;
		if (enodeBId != null) {
			this.enodeBId = enodeBId;
		}
		if (neType != null) {
			this.neType = neType.toString();
		}
		if (modifiedTime != null) {
			this.lastParseDate = Utils.parseDateToStringFormat(modifiedTime,
					ForesightConstants.DATE_FORMATE_DD_MMM_YYYY);
			this.lastParseTime = Utils.parseDateToStringFormat(modifiedTime, ForesightConstants.TIME_FORMATE_HH_MM_A);
			this.lastParseTimeStamp = modifiedTime.getTime();
		}
		this.bandClass = Utils.getDivisionDuplexByBand(band);
		if (domain != null) {
			this.domain = domain.toString();
		}
		if (vendor != null) {
			this.vendor = vendor.toString();
		}
		if (technology != null) {
			this.technology = technology.toString();
		}
	}

	/**
	 * Instantiates a new site data wrapper.
	 *
	 * @param modifiedTime the modified time
	 * @param rowkey       the rowkey
	 * @param id           the id
	 * @param neId         the ne id
	 * @param neName       the ne name
	 * @param cellId       the cell id
	 * @param neFrequency  the ne frequency
	 * @param sector       the sector
	 * @param enodeBId     the enode B id
	 * @param neType       the ne type
	 * @param city         the city
	 * @param circle       the circle
	 * @param domain       the domain
	 * @param vendor       the vendor
	 * @param technology   the technology
	 */
	public SiteDataWrapper(Date modifiedTime, String rowkey, Integer id, String neId, String neName, Integer cellId,
			String neFrequency, Integer sector, Integer enodeBId, NEType neType, String city, String circle,
			Domain domain, Vendor vendor, Technology technology) {
		this.date = modifiedTime;
		this.displayName = neName;
		this.sapId = neId;
		this.cellId = cellId;
		this.sectorId = sector;
		this.id = id;
		this.geographyL3Name = city;
		this.geographyL2Name = circle;
		this.rowKey = rowkey;
		this.enodeBId = enodeBId;
		if (neType != null) {
			this.neType = neType.toString();
		}
		if (NumberUtils.isParsable(neFrequency)) {
			this.band = Integer.valueOf(neFrequency);
		}
		if (modifiedTime != null) {
			this.lastParseDate = Utils.parseDateToStringFormat(modifiedTime,
					ForesightConstants.DATE_FORMATE_DD_MMM_YYYY);
			this.lastParseTime = Utils.parseDateToStringFormat(modifiedTime, ForesightConstants.TIME_FORMATE_HH_MM_A);
			this.lastParseTimeStamp = modifiedTime.getTime();
		}
		this.bandClass = Utils.getDivisionDuplexByBand(neFrequency);
		if (domain != null) {
			this.domain = domain.toString();
		}
		if (vendor != null) {
			this.vendor = vendor.toString();
		}
		if (technology != null) {
			this.technology = technology.toString();
		}
	}

	/**
	 * Gets the date.
	 * 
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date the new date
	 */
	public void setDate(Date date) {
		this.date = date;
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
	 * Gets the enode B id.
	 *
	 * @return the enode B id
	 */
	public Integer getEnodeBId() {
		return enodeBId;
	}

	/**
	 * Sets the enode B id.
	 *
	 * @param enodeBId the new enode B id
	 */
	public void setEnodeBId(Integer enodeBId) {
		this.enodeBId = enodeBId;
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
	 * Gets the sector id.
	 *
	 * @return the sector id
	 */
	public Integer getSectorId() {
		return sectorId;
	}

	/**
	 * Sets the sector id.
	 *
	 * @param sectorId the new sector id
	 */
	public void setSectorId(Integer sectorId) {
		this.sectorId = sectorId;
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
	 * Gets the hz beam width.
	 *
	 * @return the hz beam width
	 */
	public Double getHzBeamWidth() {
		return hzBeamWidth;
	}

	/**
	 * Sets the hz beam width.
	 *
	 * @param hzBeamWidth the new hz beam width
	 */
	public void setHzBeamWidth(Double hzBeamWidth) {
		this.hzBeamWidth = hzBeamWidth;
	}

	/**
	 * Gets the tx power.
	 *
	 * @return the tx power
	 */
	public String getTxPower() {
		return txPower;
	}

	/**
	 * Sets the tx power.
	 *
	 * @param txPower the new tx power
	 */
	public void setTxPower(String txPower) {
		this.txPower = txPower;
	}

	/**
	 * Gets the band width.
	 *
	 * @return the band width
	 */
	public String getBandWidth() {
		return bandWidth;
	}

	/**
	 * Sets the band width.
	 *
	 * @param bandWidth the new band width
	 */
	public void setBandWidth(String bandWidth) {
		this.bandWidth = bandWidth;
	}

	/**
	 * Gets the band class.
	 *
	 * @return the band class
	 */
	public String getBandClass() {
		return bandClass;
	}

	/**
	 * Sets the band class.
	 *
	 * @param bandClass the new band class
	 */
	public void setBandClass(String bandClass) {
		this.bandClass = bandClass;
	}

	/**
	 * Gets the antena model.
	 *
	 * @return the antena model
	 */
	public String getAntenaModel() {
		return antenaModel;
	}

	/**
	 * Sets the antena model.
	 *
	 * @param antenaModel the new antena model
	 */
	public void setAntenaModel(String antenaModel) {
		this.antenaModel = antenaModel;
	}

	/**
	 * Gets the elec tilt.
	 *
	 * @return the elec tilt
	 */
	public Integer getElecTilt() {
		return elecTilt;
	}

	/**
	 * Sets the elec tilt.
	 *
	 * @param elecTilt the new elec tilt
	 */
	public void setElecTilt(Integer elecTilt) {
		this.elecTilt = elecTilt;
	}

	/**
	 * Gets the mech tilt.
	 *
	 * @return the mech tilt
	 */
	public Integer getMechTilt() {
		return mechTilt;
	}

	/**
	 * Sets the mech tilt.
	 *
	 * @param mechTilt the new mech tilt
	 */
	public void setMechTilt(Integer mechTilt) {
		this.mechTilt = mechTilt;
	}

	/**
	 * Gets the band.
	 *
	 * @return the band
	 */
	public Integer getBand() {
		return band;
	}

	/**
	 * Sets the band.
	 *
	 * @param band the new band
	 */
	public void setBand(Integer band) {
		this.band = band;
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
	 * Gets the azimuth.
	 *
	 * @return the azimuth
	 */
	public Integer getAzimuth() {
		return azimuth;
	}

	/**
	 * Sets the azimuth.
	 *
	 * @param azimuth the new azimuth
	 */
	public void setAzimuth(Integer azimuth) {
		this.azimuth = azimuth;
	}

	/**
	 * Gets the sapid.
	 *
	 * @return the sapid
	 */
	public String getSapId() {
		return sapId;
	}

	/**
	 * Sets the sapid.
	 *
	 * @param sapId the new sap id
	 */
	public void setSapId(String sapId) {
		this.sapId = sapId;
	}

	/**
	 * Gets the cellid.
	 *
	 * @return the cellid
	 */
	public Integer getCellId() {
		return cellId;
	}

	/**
	 * Sets the cellid.
	 *
	 * @param cellId the new cell id
	 */
	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}

	/**
	 * Gets the app center.
	 *
	 * @return the app center
	 */
	public String getGeographyL4Name() {
		return geographyL4Name;
	}

	/**
	 * Sets the app center.
	 *
	 * @param geographyL4Name the new geography L 4 name
	 */
	public void setGeographyL4Name(String geographyL4Name) {
		this.geographyL4Name = geographyL4Name;
	}

	/**
	 * Gets the circle.
	 *
	 * @return the circle
	 */
	public String getGeographyL2Name() {
		return geographyL2Name;
	}

	/**
	 * Sets the circle.
	 *
	 * @param geographyL2Name the new geography L 2 name
	 */
	public void setGeographyL2Name(String geographyL2Name) {
		this.geographyL2Name = geographyL2Name;
	}

	/**
	 * Gets the pci.
	 *
	 * @return the pci
	 */
	public Integer getPci() {
		return pci;
	}

	/**
	 * Sets the pci.
	 *
	 * @param pci the new pci
	 */
	public void setPci(Integer pci) {
		this.pci = pci;
	}

	/**
	 * Gets the antenna height.
	 *
	 * @return the antenna height
	 */
	public String getAntennaHeight() {
		return antennaHeight;
	}

	/**
	 * Sets the antenna height.
	 *
	 * @param antennaHeight the new antenna height
	 */
	public void setAntennaHeight(String antennaHeight) {
		this.antennaHeight = antennaHeight;
	}

	/**
	 * Gets the city.
	 *
	 * @return the city
	 */
	public String getGeographyL3Name() {
		return geographyL3Name;
	}

	/**
	 * Sets the city.
	 *
	 * @param geographyL3Name the new geography L 3 name
	 */
	public void setGeographyL3Name(String geographyL3Name) {
		this.geographyL3Name = geographyL3Name;
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
	 * Gets the last parse date.
	 *
	 * @return the last parse date
	 */
	public String getLastParseDate() {
		return lastParseDate;
	}

	/**
	 * Sets the last parse date.
	 *
	 * @param lastParseDate the new last parse date
	 */
	public void setLastParseDate(String lastParseDate) {
		this.lastParseDate = lastParseDate;
	}

	/**
	 * Gets the last parse time.
	 *
	 * @return the last parse time
	 */
	public String getLastParseTime() {
		return lastParseTime;
	}

	/**
	 * Sets the last cm fetch time.
	 *
	 * @param lastParseTime the lastParseTime to set
	 */
	public void setLastParseTime(String lastParseTime) {
		this.lastParseTime = lastParseTime;
	}

	/**
	 * Gets the geography L 1 name.
	 *
	 * @return the geography L 1 name
	 */
	public String getGeographyL1Name() {
		return geographyL1Name;
	}

	/**
	 * Sets the geography L 1 name.
	 *
	 * @param geographyL1Name the new geography L 1 name
	 */
	public void setGeographyL1Name(String geographyL1Name) {
		this.geographyL1Name = geographyL1Name;
	}

	/**
	 * Gets the last parse time stamp.
	 *
	 * @return the last parse time stamp
	 */
	public Long getLastParseTimeStamp() {
		return lastParseTimeStamp;
	}

	/**
	 * Sets the last parse time stamp.
	 *
	 * @param lastParseTimeStamp the new last parse time stamp
	 */
	public void setLastParseTimeStamp(Long lastParseTimeStamp) {
		this.lastParseTimeStamp = lastParseTimeStamp;
	}

	/**
	 * Gets the domain.
	 *
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the domain.
	 *
	 * @param domain the new domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SiteDataWrapper [geographyL4Name=");
		builder.append(geographyL4Name);
		builder.append(", displayName=");
		builder.append(displayName);
		builder.append(", latitude=");
		builder.append(latitude);
		builder.append(", longitude=");
		builder.append(longitude);
		builder.append(", azimuth=");
		builder.append(azimuth);
		builder.append(", sapId=");
		builder.append(sapId);
		builder.append(", cellId=");
		builder.append(cellId);
		builder.append(", geographyL2Name=");
		builder.append(geographyL2Name);
		builder.append(", pci=");
		builder.append(pci);
		builder.append(", antennaHeight=");
		builder.append(antennaHeight);
		builder.append(", band=");
		builder.append(band);
		builder.append(", elecTilt=");
		builder.append(elecTilt);
		builder.append(", mechTilt=");
		builder.append(mechTilt);
		builder.append(", antenaModel=");
		builder.append(antenaModel);
		builder.append(", bandClass=");
		builder.append(bandClass);
		builder.append(", bandWidth=");
		builder.append(bandWidth);
		builder.append(", hzBeamWidth=");
		builder.append(hzBeamWidth);
		builder.append(", txPower=");
		builder.append(txPower);
		builder.append(", geographyL3Name=");
		builder.append(geographyL3Name);
		builder.append(", enodeBId=");
		builder.append(enodeBId);
		builder.append(", neType=");
		builder.append(neType);
		builder.append(", sectorId=");
		builder.append(sectorId);
		builder.append(", id=");
		builder.append(id);
		builder.append(", rowKey=");
		builder.append(rowKey);
		builder.append(", date=");
		builder.append(date);
		builder.append(", lastParseDate=");
		builder.append(lastParseDate);
		builder.append(", lastParseTime=");
		builder.append(lastParseTime);
		builder.append(", lastParseTimeStamp=");
		builder.append(lastParseTimeStamp);
		builder.append(", domain=");
		builder.append(domain);
		builder.append(", vendor=");
		builder.append(vendor);
		builder.append(", technology=");
		builder.append(technology);
		builder.append("]");
		return builder.toString();
	}
}
