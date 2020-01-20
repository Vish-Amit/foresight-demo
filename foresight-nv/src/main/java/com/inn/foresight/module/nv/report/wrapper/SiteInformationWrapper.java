package com.inn.foresight.module.nv.report.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.RestWrapper;

/**
 * The Class SiteInformationWrapper.
 */
@RestWrapper public class SiteInformationWrapper {

	/**
	 * The site name.
	 */
	private String siteName;

	/**
	 * The enb name.
	 */
	private String enbName;

	/**
	 * The cell name.
	 */
	private String cellName;

	/**
	 * The pci.
	 */
	private Integer pci;

	/**
	 * The tac.
	 */
	private String tac;

	/**
	 * The lat.
	 */
	private Double lat;

	/**
	 * The lon.
	 */
	private Double lon;

	/**
	 * The actul azimuth.
	 */
	private Integer actualAzimuth;

	/**
	 * The plan azimuth.
	 */
	private Integer planAzimuth;

	/**
	 * The ant hight.
	 */
	private Double antHight;
	private Double designedAntHight;
	private Double groundToRooftopAntHight;
	private Double rooftopToAntAntHight;

	/**
	 * The m tilt.
	 */
	private String mTilt;

	/**
	 * The e tilt.
	 */
	private String eTilt;

	/**
	 * The city name.
	 */
	private String cityName;
	private Integer sector;
	private Integer cellId;

	private String nename;

	private String operationalStatus;

	private Boolean isNeighbourSite = false;

	private String designedMTilt;
	private String actualFddETilt;
	private String designedFddETilt;
	private String actualTddETilt;
	private String designedTddETilt;
	private String ret;
	String azimuth;
	String neId;
	String neStatus;
	String alarmstatus;
	String technology;
	String antenaModel;

	String neFrequency;
	String region;
	String cluster;
	String siteType;
	String antennaType;
	String design;
	String sfId;
	String ecgi;
	String zone;

	Integer earfcn;

	private Integer cgi;
	private Double txPower;
	private Double horizontalBeamWidth;

	private Integer mcc;
	private Integer mnc;

	private List<Object[]> listofObject;

	private String bandwidth;

	private String cellLayer;

	private String siteAcceptanceStatus;

	private Boolean isSiteAvailable;
	private Integer dlEarfcn;
	private String friendlyName;

	private Double siteAvailabilityRate;

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGeographyId() {
		return geographyId;
	}

	public void setGeographyId(Integer geographyId) {
		this.geographyId = geographyId;
	}

	private Integer geographyId;



	public Double getTxPower() {
		return txPower;
	}

	public void setTxPower(Double txpower) {
		this.txPower = txpower;
	}

	public Double getHorizontalBeamWidth() {
		return horizontalBeamWidth;
	}

	public void setHorizontalBeamWidth(Double horizontalBeamWidth) {
		this.horizontalBeamWidth = horizontalBeamWidth;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public String getAntennaType() {
		return antennaType;
	}

	public void setAntennaType(String antennaType) {
		this.antennaType = antennaType;
	}

	public String getDesign() {
		return design;
	}

	public void setDesign(String design) {
		this.design = design;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getAntenaModel() {
		return antenaModel;
	}

	public void setAntenaModel(String antenaModel) {
		this.antenaModel = antenaModel;
	}

	public Boolean getIsNeighbourSite() {
		return isNeighbourSite;
	}

	public void setIsNeighbourSite(Boolean isNeighbourSite) {
		this.isNeighbourSite = isNeighbourSite;
	}

	public Integer getCellId() {
		return cellId;
	}

	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}

	public void setTac(String tac) {
		this.tac = tac;
	}

	/**
	 * Instantiates a new site information wrapper.
	 */
	public SiteInformationWrapper() {
		super();
	}

	/**
	 * Instantiates a new site information wrapper.
	 *
	 * @param latitude  the latitude
	 * @param longitude the longitude
	 * @param azimuth   the azimuth
	 * @param pci       the pci
	 * @param cellid    the cellid
	 */
	public SiteInformationWrapper(Double latitude, Double longitude, Integer azimuth, Integer pci, Integer cellid) {
		super();
		this.lat = latitude;
		this.lon = longitude;
		this.actualAzimuth = azimuth;
		this.pci = pci;
	}

	public Integer getSector() {
		return sector;
	}

	public void setSector(Integer sector) {
		this.sector = sector;
	}

	/**
	 * Gets the city name.
	 *
	 * @return the city name
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * Sets the city name.
	 *
	 * @param cityName the new city name
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * Gets the site name.
	 *
	 * @return the site name
	 */
	public String getSiteName() {
		return siteName;
	}

	/**
	 * Sets the site name.
	 *
	 * @param siteName the new site name
	 */
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	/**
	 * Gets the enb name.
	 *
	 * @return the enb name
	 */
	public String getEnbName() {
		return enbName;
	}

	/**
	 * Sets the enb name.
	 *
	 * @param enbName the new enb name
	 */
	public void setEnbName(String enbName) {
		this.enbName = enbName;
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
	 * Gets the tac.
	 *
	 * @return the tac
	 */
	public String getTac() {
		return tac;
	}

	/**
	 * Gets the lat.
	 *
	 * @return the lat
	 */
	public Double getLat() {
		return lat;
	}

	/**
	 * Sets the lat.
	 *
	 * @param lat the new lat
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}

	/**
	 * Gets the lon.
	 *
	 * @return the lon
	 */
	public Double getLon() {
		return lon;
	}

	/**
	 * Sets the lon.
	 *
	 * @param lon the new lon
	 */
	public void setLon(Double lon) {
		this.lon = lon;
	}

	/**
	 * Gets the actul azimuth.
	 *
	 * @return the actul azimuth
	 */
	public Integer getActualAzimuth() {
		return actualAzimuth;
	}

	/**
	 * Sets the actual azimuth.
	 *
	 * @param actualAzimuth the new actul azimuth
	 */
	public void setActualAzimuth(Integer actualAzimuth) {
		this.actualAzimuth = actualAzimuth;
	}

	/**
	 * Gets the plan azimuth.
	 *
	 * @return the plan azimuth
	 */
	public Integer getPlanAzimuth() {
		return planAzimuth;
	}

	/**
	 * Sets the plan azimuth.
	 *
	 * @param planAzimuth the new plan azimuth
	 */
	public void setPlanAzimuth(Integer planAzimuth) {
		this.planAzimuth = planAzimuth;
	}

	/**
	 * Gets the ant hight.
	 *
	 * @return the ant hight
	 */
	public Double getAntHight() {
		return antHight;
	}

	/**
	 * Sets the ant hight.
	 *
	 * @param antHight the new ant hight
	 */
	public void setAntHight(Double antHight) {
		this.antHight = antHight;
	}

	/**
	 * Gets the m tilt.
	 *
	 * @return the m tilt
	 */
	public String getmTilt() {
		return mTilt;
	}

	/**
	 * Sets the m tilt.
	 *
	 * @param mTilt the new m tilt
	 */
	public void setmTilt(String mTilt) {
		this.mTilt = mTilt;
	}

	/**
	 * Gets the e tilt.
	 *
	 * @return the e tilt
	 */
	public String geteTilt() {
		return eTilt;
	}

	/**
	 * Sets the e tilt.
	 *
	 * @param eTilt the new e tilt
	 */
	public void seteTilt(String eTilt) {
		this.eTilt = eTilt;
	}

	public String getNename() {
		return nename;
	}

	public void setNename(String nename) {
		this.nename = nename;
	}

	public String getOperationalStatus() {
		return operationalStatus;
	}

	public void setOperationalStatus(String operationalStatus) {
		this.operationalStatus = operationalStatus;
	}

	public Double getDesignedAntHight() {
		return designedAntHight;
	}

	public void setDesignedAntHight(Double designedAntHight) {
		this.designedAntHight = designedAntHight;
	}

	public Double getGroundToRooftopAntHight() {
		return groundToRooftopAntHight;
	}

	public void setGroundToRooftopAntHight(Double groundToRooftopAntHight) {
		this.groundToRooftopAntHight = groundToRooftopAntHight;
	}

	public Double getRooftopToAntAntHight() {
		return rooftopToAntAntHight;
	}

	public void setRooftopToAntAntHight(Double rooftopToAntAntHight) {
		this.rooftopToAntAntHight = rooftopToAntAntHight;
	}

	public String getDesignedMTilt() {
		return designedMTilt;
	}

	public void setDesignedMTilt(String designedMTilt) {
		this.designedMTilt = designedMTilt;
	}

	public String getActualFddETilt() {
		return actualFddETilt;
	}

	public void setActualFddETilt(String actualFddETilt) {
		this.actualFddETilt = actualFddETilt;
	}

	public String getDesignedFddETilt() {
		return designedFddETilt;
	}

	public void setDesignedFddETilt(String designedFddETilt) {
		this.designedFddETilt = designedFddETilt;
	}

	public String getActualTddETilt() {
		return actualTddETilt;
	}

	public void setActualTddETilt(String actualTddETilt) {
		this.actualTddETilt = actualTddETilt;
	}

	public String getDesignedTddETilt() {
		return designedTddETilt;
	}

	public void setDesignedTddETilt(String designedTddETilt) {
		this.designedTddETilt = designedTddETilt;
	}

	public String getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(String azimuth) {
		this.azimuth = azimuth;
	}

	public String getNeId() {
		return neId;
	}

	public void setNeId(String neId) {
		this.neId = neId;
	}

	public String getNeStatus() {
		return neStatus;
	}

	public void setNeStatus(String neStatus) {
		this.neStatus = neStatus;
	}

	public String getAlarmstatus() {
		return alarmstatus;
	}

	public void setAlarmstatus(String alarmstatus) {
		this.alarmstatus = alarmstatus;
	}

	public String getNeFrequency() {
		return neFrequency;
	}

	public void setNeFrequency(String neFrequency) {
		this.neFrequency = neFrequency;
	}

	public String getSfId() {
		return sfId;
	}

	public void setSfId(String sfId) {
		this.sfId = sfId;
	}

	public String getEcgi() {
		return ecgi;
	}

	public void setEcgi(String ecgi) {
		this.ecgi = ecgi;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getRet() {
		return ret;
	}

	public void setRet(String ret) {
		this.ret = ret;
	}

	public List<Object[]> getListofObject() {
		return listofObject;
	}

	public void setListofObject(List<Object[]> listofObject) {
		this.listofObject = listofObject;
	}

	public Boolean getNeighbourSite() {
		return isNeighbourSite;
	}

	public void setNeighbourSite(Boolean neighbourSite) {
		isNeighbourSite = neighbourSite;
	}

	public Integer getEarfcn() {
		return earfcn;
	}

	public void setEarfcn(Integer earfcn) {
		this.earfcn = earfcn;
	}

	public Integer getCgi() {
		return cgi;
	}

	public void setCgi(Integer cgi) {
		this.cgi = cgi;
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

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getCellLayer() {
		return cellLayer;
	}

	public void setCellLayer(String cellLayer) {
		this.cellLayer = cellLayer;
	}

	public String getSiteAcceptanceStatus() {
		return siteAcceptanceStatus;
	}

	public void setSiteAcceptanceStatus(String siteAcceptanceStatus) {
		this.siteAcceptanceStatus = siteAcceptanceStatus;
	}

	public Boolean isSiteAvailable() {
		return isSiteAvailable;
	}

	public void setSiteAvailable(Boolean siteAvailable) {
		isSiteAvailable = siteAvailable;
	}

	public Integer getDlEarfcn() {
		return dlEarfcn;
	}

	public void setDlEarfcn(Integer dlEarfcn) {
		this.dlEarfcn = dlEarfcn;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	public Double getSiteAvailabilityRate() {
		return siteAvailabilityRate;
	}

	public void setSiteAvailabilityRate(Double siteAvailabilityRate) {
		this.siteAvailabilityRate = siteAvailabilityRate;
	}

	@Override
	public String toString() {
		return "SiteInformationWrapper{" + "siteName='" + siteName + '\'' + ", enbName='" + enbName + '\''
				+ ", cellName='" + cellName + '\'' + ", pci=" + pci + ", tac='" + tac + '\'' + ", lat=" + lat + ", lon="
				+ lon + ", actualAzimuth=" + actualAzimuth + ", planAzimuth=" + planAzimuth + ", antHight=" + antHight
				+ ", designedAntHight=" + designedAntHight + ", groundToRooftopAntHight=" + groundToRooftopAntHight
				+ ", rooftopToAntAntHight=" + rooftopToAntAntHight + ", mTilt='" + mTilt + '\'' + ", eTilt='" + eTilt
				+ '\'' + ", cityName='" + cityName + '\'' + ", sector=" + sector + ", cellId=" + cellId + ", nename='"
				+ nename + '\'' + ", operationalStatus='" + operationalStatus + '\'' + ", isNeighbourSite="
				+ isNeighbourSite + ", designedMTilt='" + designedMTilt + '\'' + ", actualFddETilt='" + actualFddETilt
				+ '\'' + ", designedFddETilt='" + designedFddETilt + '\'' + ", actualTddETilt='" + actualTddETilt + '\''
				+ ", designedTddETilt='" + designedTddETilt + '\'' + ", ret='" + ret + '\'' + ", azimuth='" + azimuth
				+ '\'' + ", neId='" + neId + '\'' + ", neStatus='" + neStatus + '\'' + ", alarmstatus='" + alarmstatus
				+ '\'' + ", technology='" + technology + '\'' + ", antenaModel='" + antenaModel + '\''
				+ ", neFrequency='" + neFrequency + '\'' + ", region='" + region + '\'' + ", cluster='" + cluster + '\''
				+ ", siteType='" + siteType + '\'' + ", antennaType='" + antennaType + '\'' + ", design='" + design
				+ '\'' + ", sfId='" + sfId + '\'' + ", ecgi='" + ecgi + '\'' + ", zone='" + zone + '\'' + ", earfcn="
				+ earfcn + ", cgi=" + cgi + ", txPower=" + txPower + ", horizontalBeamWidth=" + horizontalBeamWidth
				+ ", mcc=" + mcc + ", mnc=" + mnc + ", listofObject=" + listofObject + ", bandwidth='" + bandwidth
				+ '\'' + ", cellLayer='" + cellLayer + '\'' + ", siteAcceptanceStatus='" + siteAcceptanceStatus + '\''
				+ ", isSiteAvailable=" + isSiteAvailable + ", dlEarfcn=" + dlEarfcn + ", friendlyName='" + friendlyName
				+ '\'' + ", siteAvailabilityRate=" + siteAvailabilityRate + '}';
	}
}
