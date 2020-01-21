package com.inn.foresight.core.infra.wrapper;

import java.util.Date;
import java.util.Map;

import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.utils.enums.Vendor;

/**
 * The Class SiteGeographicalDetail.
 */
@RestWrapper
public class SiteGeographicalDetail {

	/** The sapid. */
	private String sapid;
	
	/** The site name. */
	private String siteName;
	
	/** The r 4 g state. */
	private String r4gState;
	
	/** The mp name. */
	private String mpName;
	
	/** The status. */
	private NEStatus status;
	
	/** The ref sapid. */
	private String refSapid;
	
	/** The morphology. */
	private String morphology;
	
	/** The site category. */
	private String siteCategory;
	
	/** The ne type. */
	private NEType neType;
	
	/** The ne id. */
	private String neId;
	
	/** The onair date. */
	private Map onairDate;
	
	/** The sector bandwidth. */
	private String sectorBandwidth;
	
	/** The latitude. */
	private Double latitude;
	
	/** The longitude. */
	private Double longitude;
	
	/** The tower type. */
	private String towerType;
	
	/** The ip name. */
	private String ipName;
	
	/** The backhaul media. */
	private String backhaulMedia;
	
	/** The vendor. */
	private Vendor vendor;
	
	/** The candidate id. */
	private String candidateId;
	
	/** The zone. */
	private String zone;
	
	/** The circle. */
	private String circle;
	
	/** The state. */
	private String state;
	
	/** The district name. */
	private String districtName;
	
	/** The district code. */
	private String districtCode;
	
	/** The mp code. */
	private String mpCode;
	
	/** The taluka name. */
	private String talukaName;
	
	/** The taluka code. */
	private String talukaCode;
	
	/** The village name. */
	private String villageName;
	
	/** The village census code. */
	private String villageCensusCode;

	/** The mcc. */
	private Integer mcc;
	
	/** The mnc. */
	private Integer mnc;
	
	/** The tracking area. */
	private String trackingArea;
	
	/** The enode B id. */
	private String enodeBId;
	
	/** The number of sector. */
	private Integer numberOfSector;
	
	/** The ecgi. */
	private String ecgi;
	
	/** The cell id. */
	private Integer cellId;
	
	/** The pci. */
	private Integer pci;
	
	/** The sector. */
	private Integer sector;
	
	/** The cell name. */
	private String cellName;
	
	/** The bandwidth. */
	private Double bandwidth;
	
	/** The antenna type. */
	private String antennaType;
	
	/** The antenna model. */
	private String antennaModel;
	
	/** The antenna vendor. */
	private String antennaVendor;
	
	/** The azimuth. */
	private Integer azimuth;
	
	/** The antenna height. */
	private Double antennaHeight;
	
	/** The elec tilt. */
	private Double elecTilt;
	
	/** The mech tilt. */
	private Double mechTilt;
	
	/** The propagation model. */
	private String propagationModel;
	
	/** The tx power. */
	private String txPower;
	
	/** The eirp. */
	private String eirp;
	
	/** The pilot channel tx power. */
	private Double pilotChannelTxPower;
	
	/** The clutter category. */
	private String clutterCategory;
	
	/** The radius threshold. */
	private Integer radiusThreshold;
	
	/** The rsrp threshold. */
	private Double rsrpThreshold;
	
	/** The base channel freq. */
	private Double baseChannelFreq;
	
	/** The priority site. */
	private String prioritySite;
	
	/** The band. */
	private String band;

	/** The phase. */
	private String phase;

	/** The alpha bandwidth. */
	private String alphaBandwidth;
	
	/** The beta bandwidth. */
	private String betaBandwidth;
	
	/** The gamma bandwidth. */
	private String gammaBandwidth;
	
	/** The alpha on air time. */
	private Date alphaOnAirTime;
	
	/** The beta on air time. */
	private Date betaOnAirTime;
	
	/** The gamma on air time. */
	private Date gammaOnAirTime;
	
	/** The live ems. */
	private Map liveEms;
	
	/** The execute ATP 11 B. */
	private Map executeATP11B;
	
	/** The site address. */
	private String siteAddress;
	
	/** The progress state. */
	private Map progressState;

	/** The morphology name. */
	private String morphologyName;
	
	private String geographyL1;
	
	private String geographyL2;
	
	private String geographyL3;
	
	private String geographyL4;
	
	private String displayNameL1;
	
	private String displayNameL2;
	
	private String displayNameL3;
	
	private String displayNameL4;
	
	private String geographyL3Code;
	
	private String geographyL4Code;
	
	private String siteId;
	
	private String onairDate2300;
	
	private String onairDate1800;
	
	private String onairDate850;
	
	private String sectorBandwidth2300;
	
	private String sectorBandwidth1800;
	
	private String sectorBandwidth850;
	
	private String neStatus2300;
	
	private String neStatus1800;
	
	private String neStatus850;
	
	private String siteType;
	
	private Map sectorMap;
	
	private String plannedBand;

	private String plannedLatitude;
	
	private String plannedLongitude;
	
	private NEStatus alphaStatus;
	
	private NEStatus betaStatus;
	
	private NEStatus gammaStatus;
	
    private String alphaAdditionalBandwidth;
	
	/** The beta bandwidth. */
	private String betaAdditionalBandwidth;
	
	/** The gamma bandwidth. */
	private String gammaAdditionalBandwidth;
  private Date alphaAdditionalOnAirTime;
	
	/** The beta addition on air time. */
	private Date betaAdditionalOnAirTime;
	
	/** The gamma addition on air time. */
	private Date gammaAdditionalOnAirTime;
private NEStatus alphaAdditionalStatus;
	
	private NEStatus betaAdditionalStatus;
	
	private NEStatus gammaAdditionalStatus;
		/** The check alpha add sector id 4. */
	private Boolean checkAlphaAddSectorId4;
	
	/** The check beta add sector id 5. */
	private Boolean checkBetaAddSectorId5;
	
	/** The check gamma add sector id 6. */
	private Boolean checkGammaAddSectorId6;
	
	private String operationalStatus;
	
	private String adminState;
	
	private String ip;
	
	/**
	 * Instantiates a new site geographical detail.
	 */
	public SiteGeographicalDetail() {
		
	}

	public String getDisplayNameL1() {
		return displayNameL1;
	}

	public void setDisplayNameL1(String displayNameL1) {
		this.displayNameL1 = displayNameL1;
	}

	public String getDisplayNameL2() {
		return displayNameL2;
	}

	public void setDisplayNameL2(String displayNameL2) {
		this.displayNameL2 = displayNameL2;
	}

	public String getDisplayNameL3() {
		return displayNameL3;
	}

	public void setDisplayNameL3(String displayNameL3) {
		this.displayNameL3 = displayNameL3;
	}

	public String getDisplayNameL4() {
		return displayNameL4;
	}

	public void setDisplayNameL4(String displayNameL4) {
		this.displayNameL4 = displayNameL4;
	}
	public String getSapid() {
		return sapid;
	}

	public void setSapid(String sapid) {
		this.sapid = sapid;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getR4gState() {
		return r4gState;
	}

	public void setR4gState(String r4gState) {
		this.r4gState = r4gState;
	}

	public String getMpName() {
		return mpName;
	}

	public void setMpName(String mpName) {
		this.mpName = mpName;
	}

	public NEStatus getStatus() {
		return status;
	}

	public void setStatus(NEStatus status) {
		this.status = status;
	}

	public String getRefSapid() {
		return refSapid;
	}

	public void setRefSapid(String refSapid) {
		this.refSapid = refSapid;
	}

	public String getMorphology() {
		return morphology;
	}

	public void setMorphology(String morphology) {
		this.morphology = morphology;
	}

	public String getSiteCategory() {
		return siteCategory;
	}

	public void setSiteCategory(String siteCategory) {
		this.siteCategory = siteCategory;
	}

	public NEType getNeType() {
		return neType;
	}

	public void setNeType(NEType neType) {
		this.neType = neType;
	}

	public String getNeId() {
		return neId;
	}

	public void setNeId(String neId) {
		this.neId = neId;
	}

	public Map getOnairDate() {
		return onairDate;
	}

	public void setOnairDate(Map onairDate) {
		this.onairDate = onairDate;
	}

	public String getSectorBandwidth() {
		return sectorBandwidth;
	}

	public void setSectorBandwidth(String sectorBandwidth) {
		this.sectorBandwidth = sectorBandwidth;
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

	public String getTowerType() {
		return towerType;
	}

	public void setTowerType(String towerType) {
		this.towerType = towerType;
	}

	public String getIpName() {
		return ipName;
	}

	public void setIpName(String ipName) {
		this.ipName = ipName;
	}

	public String getBackhaulMedia() {
		return backhaulMedia;
	}

	public void setBackhaulMedia(String backhaulMedia) {
		this.backhaulMedia = backhaulMedia;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}


	public String getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(String candidateId) {
		this.candidateId = candidateId;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getCircle() {
		return circle;
	}

	public void setCircle(String circle) {
		this.circle = circle;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public String getMpCode() {
		return mpCode;
	}

	public void setMpCode(String mpCode) {
		this.mpCode = mpCode;
	}

	public String getTalukaName() {
		return talukaName;
	}

	public void setTalukaName(String talukaName) {
		this.talukaName = talukaName;
	}

	public String getTalukaCode() {
		return talukaCode;
	}

	public void setTalukaCode(String talukaCode) {
		this.talukaCode = talukaCode;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public String getVillageCensusCode() {
		return villageCensusCode;
	}

	public void setVillageCensusCode(String villageCensusCode) {
		this.villageCensusCode = villageCensusCode;
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

	public String getTrackingArea() {
		return trackingArea;
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	public String getEnodeBId() {
		return enodeBId;
	}

	public void setEnodeBId(String enodeBId) {
		this.enodeBId = enodeBId;
	}

	public Integer getNumberOfSector() {
		return numberOfSector;
	}

	public void setNumberOfSector(Integer numberOfSector) {
		this.numberOfSector = numberOfSector;
	}

	public String getEcgi() {
		return ecgi;
	}

	public void setEcgi(String ecgi) {
		this.ecgi = ecgi;
	}

	public Integer getCellId() {
		return cellId;
	}

	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}

	public Integer getPci() {
		return pci;
	}

	public void setPci(Integer pci) {
		this.pci = pci;
	}

	public Integer getSector() {
		return sector;
	}

	public void setSector(Integer sector) {
		this.sector = sector;
	}

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public Double getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(Double bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getAntennaType() {
		return antennaType;
	}

	public void setAntennaType(String antennaType) {
		this.antennaType = antennaType;
	}

	public String getAntennaModel() {
		return antennaModel;
	}

	public void setAntennaModel(String antennaModel) {
		this.antennaModel = antennaModel;
	}

	public String getAntennaVendor() {
		return antennaVendor;
	}

	public void setAntennaVendor(String antennaVendor) {
		this.antennaVendor = antennaVendor;
	}

	public Integer getAzimuth() {
		return azimuth;
	}

	public void setAzimuth(Integer azimuth) {
		this.azimuth = azimuth;
	}

	public Double getAntennaHeight() {
		return antennaHeight;
	}

	public void setAntennaHeight(Double antennaHeight) {
		this.antennaHeight = antennaHeight;
	}

	public Double getElecTilt() {
		return elecTilt;
	}

	public void setElecTilt(Double elecTilt) {
		this.elecTilt = elecTilt;
	}

	public Double getMechTilt() {
		return mechTilt;
	}

	public void setMechTilt(Double mechTilt) {
		this.mechTilt = mechTilt;
	}

	public String getPropagationModel() {
		return propagationModel;
	}

	public void setPropagationModel(String propagationModel) {
		this.propagationModel = propagationModel;
	}

	public String getTxPower() {
		return txPower;
	}

	public void setTxPower(String txPower) {
		this.txPower = txPower;
	}

	public String getEirp() {
		return eirp;
	}

	public void setEirp(String eirp) {
		this.eirp = eirp;
	}

	public Double getPilotChannelTxPower() {
		return pilotChannelTxPower;
	}

	public void setPilotChannelTxPower(Double pilotChannelTxPower) {
		this.pilotChannelTxPower = pilotChannelTxPower;
	}

	public String getClutterCategory() {
		return clutterCategory;
	}

	public void setClutterCategory(String clutterCategory) {
		this.clutterCategory = clutterCategory;
	}

	public Integer getRadiusThreshold() {
		return radiusThreshold;
	}

	public void setRadiusThreshold(Integer radiusThreshold) {
		this.radiusThreshold = radiusThreshold;
	}

	public Double getRsrpThreshold() {
		return rsrpThreshold;
	}

	public void setRsrpThreshold(Double rsrpThreshold) {
		this.rsrpThreshold = rsrpThreshold;
	}

	public Double getBaseChannelFreq() {
		return baseChannelFreq;
	}

	public void setBaseChannelFreq(Double baseChannelFreq) {
		this.baseChannelFreq = baseChannelFreq;
	}

	public String getPrioritySite() {
		return prioritySite;
	}

	public void setPrioritySite(String prioritySite) {
		this.prioritySite = prioritySite;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getAlphaBandwidth() {
		return alphaBandwidth;
	}

	public void setAlphaBandwidth(String alphaBandwidth) {
		this.alphaBandwidth = alphaBandwidth;
	}

	public String getBetaBandwidth() {
		return betaBandwidth;
	}

	public void setBetaBandwidth(String betaBandwidth) {
		this.betaBandwidth = betaBandwidth;
	}

	public String getGammaBandwidth() {
		return gammaBandwidth;
	}

	public void setGammaBandwidth(String gammaBandwidth) {
		this.gammaBandwidth = gammaBandwidth;
	}


	public Date getAlphaOnAirTime() {
		return alphaOnAirTime;
	}

	public void setAlphaOnAirTime(Date alphaOnAirTime) {
		this.alphaOnAirTime = alphaOnAirTime;
	}

	public Date getBetaOnAirTime() {
		return betaOnAirTime;
	}

	public void setBetaOnAirTime(Date betaOnAirTime) {
		this.betaOnAirTime = betaOnAirTime;
	}

	public Date getGammaOnAirTime() {
		return gammaOnAirTime;
	}

	public void setGammaOnAirTime(Date gammaOnAirTime) {
		this.gammaOnAirTime = gammaOnAirTime;
	}

	public Map getLiveEms() {
		return liveEms;
	}

	public void setLiveEms(Map liveEms) {
		this.liveEms = liveEms;
	}

	public Map getExecuteATP11B() {
		return executeATP11B;
	}

	public void setExecuteATP11B(Map executeATP11B) {
		this.executeATP11B = executeATP11B;
	}

	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public Map getProgressState() {
		return progressState;
	}

	public void setProgressState(Map progressState) {
		this.progressState = progressState;
	}

	public String getMorphologyName() {
		return morphologyName;
	}

	public void setMorphologyName(String morphologyName) {
		this.morphologyName = morphologyName;
	}

	public String getGeographyL1() {
		return geographyL1;
	}

	public void setGeographyL1(String geographyL1) {
		this.geographyL1 = geographyL1;
	}

	public String getGeographyL2() {
		return geographyL2;
	}

	public void setGeographyL2(String geographyL2) {
		this.geographyL2 = geographyL2;
	}

	public String getGeographyL3() {
		return geographyL3;
	}

	public void setGeographyL3(String geographyL3) {
		this.geographyL3 = geographyL3;
	}

	public String getGeographyL4() {
		return geographyL4;
	}

	public void setGeographyL4(String geographyL4) {
		this.geographyL4 = geographyL4;
	}

	public String getGeographyL3Code() {
		return geographyL3Code;
	}

	public void setGeographyL3Code(String geographyL3Code) {
		this.geographyL3Code = geographyL3Code;
	}

	public String getGeographyL4Code() {
		return geographyL4Code;
	}

	public void setGeographyL4Code(String geographyL4Code) {
		this.geographyL4Code = geographyL4Code;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getOnairDate2300() {
		return onairDate2300;
	}

	public void setOnairDate2300(String onairDate2300) {
		this.onairDate2300 = onairDate2300;
	}

	public String getOnairDate1800() {
		return onairDate1800;
	}

	public void setOnairDate1800(String onairDate1800) {
		this.onairDate1800 = onairDate1800;
	}

	public String getOnairDate850() {
		return onairDate850;
	}

	public void setOnairDate850(String onairDate850) {
		this.onairDate850 = onairDate850;
	}

	public String getSectorBandwidth2300() {
		return sectorBandwidth2300;
	}

	public void setSectorBandwidth2300(String sectorBandwidth2300) {
		this.sectorBandwidth2300 = sectorBandwidth2300;
	}

	public String getSectorBandwidth1800() {
		return sectorBandwidth1800;
	}

	public void setSectorBandwidth1800(String sectorBandwidth1800) {
		this.sectorBandwidth1800 = sectorBandwidth1800;
	}

	public String getSectorBandwidth850() {
		return sectorBandwidth850;
	}

	public void setSectorBandwidth850(String sectorBandwidth850) {
		this.sectorBandwidth850 = sectorBandwidth850;
	}

	public String getNeStatus2300() {
		return neStatus2300;
	}

	public void setNeStatus2300(String neStatus2300) {
		this.neStatus2300 = neStatus2300;
	}

	public String getNeStatus1800() {
		return neStatus1800;
	}

	public void setNeStatus1800(String neStatus1800) {
		this.neStatus1800 = neStatus1800;
	}

	public String getNeStatus850() {
		return neStatus850;
	}

	public void setNeStatus850(String neStatus850) {
		this.neStatus850 = neStatus850;
	}

	public String getSiteType() {
		return siteType;
	}

	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}

	public Map getSectorMap() {
		return sectorMap;
	}

	public void setSectorMap(Map sectorMap) {
		this.sectorMap = sectorMap;
	}

	public String getPlannedBand() {
		return plannedBand;
	}

	public void setPlannedBand(String plannedBand) {
		this.plannedBand = plannedBand;
	}

	public String getPlannedLatitude() {
		return plannedLatitude;
	}

	public void setPlannedLatitude(String plannedLatitude) {
		this.plannedLatitude = plannedLatitude;
	}

	public String getPlannedLongitude() {
		return plannedLongitude;
	}

	public void setPlannedLongitude(String plannedLongitude) {
		this.plannedLongitude = plannedLongitude;
	}

	public NEStatus getAlphaStatus() {
		return alphaStatus;
	}

	public void setAlphaStatus(NEStatus alphaStatus) {
		this.alphaStatus = alphaStatus;
	}

	public NEStatus getBetaStatus() {
		return betaStatus;
	}

	public void setBetaStatus(NEStatus betaStatus) {
		this.betaStatus = betaStatus;
	}

	public NEStatus getGammaStatus() {
		return gammaStatus;
	}

	public void setGammaStatus(NEStatus gammaStatus) {
		this.gammaStatus = gammaStatus;
	}

	public String getAlphaAdditionalBandwidth() {
		return alphaAdditionalBandwidth;
	}

	public void setAlphaAdditionalBandwidth(String alphaAdditionalBandwidth) {
		this.alphaAdditionalBandwidth = alphaAdditionalBandwidth;
	}

	public String getBetaAdditionalBandwidth() {
		return betaAdditionalBandwidth;
	}

	public void setBetaAdditionalBandwidth(String betaAdditionalBandwidth) {
		this.betaAdditionalBandwidth = betaAdditionalBandwidth;
	}

	public String getGammaAdditionalBandwidth() {
		return gammaAdditionalBandwidth;
	}

	public void setGammaAdditionalBandwidth(String gammaAdditionalBandwidth) {
		this.gammaAdditionalBandwidth = gammaAdditionalBandwidth;
	}

	public Date getAlphaAdditionalOnAirTime() {
		return alphaAdditionalOnAirTime;
	}

	public void setAlphaAdditionalOnAirTime(Date alphaAdditionalOnAirTime) {
		this.alphaAdditionalOnAirTime = alphaAdditionalOnAirTime;
	}

	public Date getBetaAdditionalOnAirTime() {
		return betaAdditionalOnAirTime;
	}

	public void setBetaAdditionalOnAirTime(Date betaAdditionalOnAirTime) {
		this.betaAdditionalOnAirTime = betaAdditionalOnAirTime;
	}

	public Date getGammaAdditionalOnAirTime() {
		return gammaAdditionalOnAirTime;
	}

	public void setGammaAdditionalOnAirTime(Date gammaAdditionalOnAirTime) {
		this.gammaAdditionalOnAirTime = gammaAdditionalOnAirTime;
	}

	public NEStatus getAlphaAdditionalStatus() {
		return alphaAdditionalStatus;
	}

	public void setAlphaAdditionalStatus(NEStatus alphaAdditionalStatus) {
		this.alphaAdditionalStatus = alphaAdditionalStatus;
	}

	public NEStatus getBetaAdditionalStatus() {
		return betaAdditionalStatus;
	}

	public void setBetaAdditionalStatus(NEStatus betaAdditionalStatus) {
		this.betaAdditionalStatus = betaAdditionalStatus;
	}

	public NEStatus getGammaAdditionalStatus() {
		return gammaAdditionalStatus;
	}

	public void setGammaAdditionalStatus(NEStatus gammaAdditionalStatus) {
		this.gammaAdditionalStatus = gammaAdditionalStatus;
	}

	public Boolean getCheckAlphaAddSectorId4() {
		return checkAlphaAddSectorId4;
	}

	public void setCheckAlphaAddSectorId4(Boolean checkAlphaAddSectorId4) {
		this.checkAlphaAddSectorId4 = checkAlphaAddSectorId4;
	}

	public Boolean getCheckBetaAddSectorId5() {
		return checkBetaAddSectorId5;
	}

	public void setCheckBetaAddSectorId5(Boolean checkBetaAddSectorId5) {
		this.checkBetaAddSectorId5 = checkBetaAddSectorId5;
	}

	public Boolean getCheckGammaAddSectorId6() {
		return checkGammaAddSectorId6;
	}

	public void setCheckGammaAddSectorId6(Boolean checkGammaAddSectorId6) {
		this.checkGammaAddSectorId6 = checkGammaAddSectorId6;
	}
	
	public String getOperationalStatus() {
		return operationalStatus;
	}

	public void setOperationalStatus(String operationalStatus) {
		this.operationalStatus = operationalStatus;
	}

	public String getAdminState() {
		return adminState;
	}

	public void setAdminState(String adminState) {
		this.adminState = adminState;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public String toString() {
		return "SiteGeographicalDetail [sapid=" + sapid + ", siteName=" + siteName + ", r4gState=" + r4gState
				+ ", mpName=" + mpName + ", status=" + status + ", refSapid=" + refSapid + ", morphology=" + morphology
				+ ", siteCategory=" + siteCategory + ", neType=" + neType + ", neId=" + neId + ", onairDate="
				+ onairDate + ", sectorBandwidth=" + sectorBandwidth + ", latitude=" + latitude + ", longitude="
				+ longitude + ", towerType=" + towerType + ", ipName=" + ipName + ", backhaulMedia=" + backhaulMedia
				+ ", vendor=" + vendor + ", candidateId=" + candidateId + ", zone=" + zone + ", circle=" + circle
				+ ", state=" + state + ", districtName=" + districtName + ", districtCode=" + districtCode + ", mpCode="
				+ mpCode + ", talukaName=" + talukaName + ", talukaCode=" + talukaCode + ", villageName=" + villageName
				+ ", villageCensusCode=" + villageCensusCode + ", mcc=" + mcc + ", mnc=" + mnc + ", trackingArea="
				+ trackingArea + ", enodeBId=" + enodeBId + ", numberOfSector=" + numberOfSector + ", ecgi=" + ecgi
				+ ", cellId=" + cellId + ", pci=" + pci + ", sector=" + sector + ", cellName=" + cellName
				+ ", bandwidth=" + bandwidth + ", antennaType=" + antennaType + ", antennaModel=" + antennaModel
				+ ", antennaVendor=" + antennaVendor + ", azimuth=" + azimuth + ", antennaHeight=" + antennaHeight
				+ ", elecTilt=" + elecTilt + ", mechTilt=" + mechTilt + ", propagationModel=" + propagationModel
				+ ", txPower=" + txPower + ", eirp=" + eirp + ", pilotChannelTxPower=" + pilotChannelTxPower
				+ ", clutterCategory=" + clutterCategory + ", radiusThreshold=" + radiusThreshold + ", rsrpThreshold="
				+ rsrpThreshold + ", baseChannelFreq=" + baseChannelFreq + ", prioritySite=" + prioritySite + ", band="
				+ band + ", phase=" + phase + ", alphaBandwidth=" + alphaBandwidth + ", betaBandwidth=" + betaBandwidth
				+ ", gammaBandwidth=" + gammaBandwidth + ", alphaOnAirTime=" + alphaOnAirTime + ", betaOnAirTime="
				+ betaOnAirTime + ", gammaOnAirTime=" + gammaOnAirTime + ", liveEms=" + liveEms + ", executeATP11B="
				+ executeATP11B + ", siteAddress=" + siteAddress + ", progressState=" + progressState
				+ ", morphologyName=" + morphologyName + ", geographyL1=" + geographyL1 + ", geographyL2=" + geographyL2
				+ ", geographyL3=" + geographyL3 + ", geographyL4=" + geographyL4 + ", geographyL3Code="
				+ geographyL3Code + ", geographyL4Code=" + geographyL4Code + ", siteId=" + siteId + ", onairDate2300="
				+ onairDate2300 + ", onairDate1800=" + onairDate1800 + ", onairDate850=" + onairDate850
				+ ", sectorBandwidth2300=" + sectorBandwidth2300 + ", sectorBandwidth1800=" + sectorBandwidth1800
				+ ", sectorBandwidth850=" + sectorBandwidth850 + ", neStatus2300=" + neStatus2300 + ", neStatus1800="
				+ neStatus1800 + ", neStatus850=" + neStatus850 + ", siteType=" + siteType + ", sectorMap=" + sectorMap
				+ ", plannedBand=" + plannedBand + ", plannedLatitude=" + plannedLatitude + ", plannedLongitude="
				+ plannedLongitude + ", alphaStatus=" + alphaStatus + ", betaStatus=" + betaStatus + ", gammaStatus="
				+ gammaStatus + ", alphaAdditionalBandwidth=" + alphaAdditionalBandwidth + ", betaAdditionalBandwidth="
				+ betaAdditionalBandwidth + ", gammaAdditionalBandwidth=" + gammaAdditionalBandwidth
				+ ", alphaAdditionalOnAirTime=" + alphaAdditionalOnAirTime + ", betaAdditionalOnAirTime="
				+ betaAdditionalOnAirTime + ", gammaAdditionalOnAirTime=" + gammaAdditionalOnAirTime
				+ ", alphaAdditionalStatus=" + alphaAdditionalStatus + ", betaAdditionalStatus=" + betaAdditionalStatus
				+ ", gammaAdditionalStatus=" + gammaAdditionalStatus + ", checkAlphaAddSectorId4="
				+ checkAlphaAddSectorId4 + ", checkBetaAddSectorId5=" + checkBetaAddSectorId5
				+ ", checkGammaAddSectorId6=" + checkGammaAddSectorId6 + ", operationalStatus=" + operationalStatus
				+ ", adminState=" + adminState + ", ip=" + ip + "]";
	}
	
}
