package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;




/**
 * The Class MacroSiteDetail.
 */
@NamedQueries({
	//@NamedQuery(name ="validateEnbid", query = "select mac from MacroSiteDetail mac where mac.enbId =: enbId"),
	//@NamedQuery(name = "getSiteDetailByNeName", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(m.networkElement.id,m.sector,m.networkElement.neId,m.networkElement.networkElement.neName,m.cellId,m.networkElement.neFrequency,m.azimuth,m.antennaHeight,m.mechTilt,m.elecTilt,m.antennaType) from MacroSiteDetail m where m.networkElement.neStatus='ONAIR' and m.networkElement.isDeleted=false and m.networkElement.neName=:neName "),
	//@NamedQuery(name = "getMacroSiteDetailBySapIdSecAndBand", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(m.networkElement.id,m.sector,m.networkElement.neId,m.networkElement.networkElement.neName,m.cellId,m.networkElement.neFrequency,m.azimuth,m.antennaHeight,m.mechTilt,m.elecTilt,m.antennaType) from MacroSiteDetail m where m.networkElement.neStatus='ONAIR' and m.networkElement.isDeleted=false and m.networkElement.networkElement.neName=:neName and m.sector=:sector and m.networkElement.neFrequency=:band"),
	//@NamedQuery(name = "getMacroSiteDetailBySapId", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(m.networkElement.id,m.sector,m.networkElement.neId,m.networkElement.neName,m.cellId,m.networkElement.neFrequency,m.azimuth,m.antennaHeight,m.mechTilt,m.elecTilt,m.antennaType) from MacroSiteDetail m where m.networkElement.neStatus='ONAIR' and m.networkElement.isDeleted=false and m.networkElement.neName=:sapId and m.networkElement.networkElement.id is not null"),
	//@NamedQuery(name = "getSiteDetailBySiteIdSecAndVendor", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(msd.networkElement.id,msd.sector,msd.networkElement.neId,msd.networkElement.neName,msd.cellId,msd.networkElement.neFrequency,msd.azimuth,msd.antennaHeight,msd.mechTilt,msd.elecTilt,msd.antennaType) from MacroSiteDetail msd where msd.networkElement.neStatus='ONAIR' and msd.networkElement.isDeleted=false and msd.networkElement.neName=:neName and msd.sector=:sector and msd.networkElement.neFrequency=:band and UPPER(msd.networkElement.vendor)=:vendor and UPPER(msd.networkElement.technology)=:technology and UPPER(msd.networkElement.domain)=:domain"),		
	/*@NamedQuery(name = "getSiteDetailByLatLongAndBand", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.neName,n.vendor,n.neFrequency,m.cellName,m.cellId, n.neType,n.latitude,n.longitude,n.neStatus,m.azimuth,m.sector,n.domain,m.pci,m.carrier)  "
			+ "from NetworkElement n  inner join MacroSiteDetail m on n.id = m.networkElement.id where n.neStatus = 'ONAIR' and "
			+ " n.domain = :domain and n.vendor=:vendor and (n.latitude >=:southWestLat and n.latitude <=:northEastLat and n.longitude >=:southWestLong and n.longitude <=:northEastLong ) and n.neFrequency in (:bandList) and n.neType =:neType"),*/
	@NamedQuery(name = "getNEBySapIds", query = "select m from MacroSiteDetail m where m.networkElement.networkElement.neName in(:sapIds) and m.networkElement.networkElement is null"),
	//@NamedQuery(name = "getNECellsBySapids", query="select distinct new com.inn.foresight.core.infra.wrapper.SiteDataWrapper(m.cellId,m.enbId,m.networkElement.networkElement.neName,m.networkElement.neFrequency,m.networkElement.id) from MacroSiteDetail m where m.networkElement.networkElement.neName in (:sapIds)"),
	//@NamedQuery(name = "searchAllSites",query = "select m.networkElement.networkElement.neName,m.cellId,m.networkElement.neFrequency,m.networkElement.vendor,m.networkElement.neType,m.sector,m.networkElement.networkElement.neName,m.networkElement.domain,m.networkElement.technology from MacroSiteDetail m where m.networkElement.networkElement.neName like :siteName"),
	//@NamedQuery(name = "getCellListByGeogrphyL4" ,query ="select m.networkElement.neName from MacroSiteDetail m where m.networkElement.geographyL4.name =:geographyName and m.networkElement.vendor =:vendor and m.networkElement.neType ='MACRO_CELL' and m.cellId is not null"),
	/*@NamedQuery(name = "getSiteDetailByGeogrphyL4" ,query ="select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.networkElement.neName,n.vendor,n.neFrequency,m.cellName,m.cellId, n.neType,n.latitude,n.longitude,n.neStatus,m.azimuth,m.sector,n.domain,m.pci,m.carrier)  "
			+ "from NetworkElement n  inner join MacroSiteDetail m on n.id = m.networkElement.id where n.neStatus = 'ONAIR' and "
			+ " n.domain = :domain and n.vendor=:vendor and n.neFrequency in (:bandList) and n.geographyL4.name =:geographyName and n.neType =:neType and n.geographyL4 is not null"),*/
	/*@NamedQuery(name = "getSiteDetailByGeogrphyL4ForAllVendor" ,query ="select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.networkElement.neName,n.vendor,n.neFrequency,m.cellName,m.cellId, n.neType,n.latitude,n.longitude,n.neStatus,m.azimuth,m.sector,n.domain,m.pci,m.carrier)  "
			+ "from NetworkElement n  inner join MacroSiteDetail m on n.id = m.networkElement.id where n.latitude is not null and n.longitude is not null and n.neStatus = 'ONAIR' and "
			+ " n.domain = :domain and n.neFrequency in (:bandList) and n.geographyL4.name =:geographyName and n.neType =:neType and n.geographyL4 is not null"),*/
//	@NamedQuery(name = "getSectorPropertyData",query="Select m from MacroSiteDetail m where upper(m.networkElement.networkElement.neName)=:neName and m.networkElement.neFrequency=:neFrequency and upper(m.networkElement.neStatus)=:neStatus and m.networkElement.networkElement is not null "),
	
	//@NamedQuery(name = "getCellListByGeogrphyL3" ,query ="select distinct m.networkElement.neName from MacroSiteDetail m where m.networkElement.geographyL4.geographyL3.name =:geographyName and m.networkElement.vendor =:vendor and m.networkElement.neType ='MACRO_CELL' and m.cellId is not null"),
	//@NamedQuery(name = "getCellListByGeogrphyL2" ,query ="select distinct m.networkElement.neName from MacroSiteDetail m where m.networkElement.geographyL4.geographyL3.geographyL2.name =:geographyName and m.networkElement.vendor =:vendor and m.networkElement.neType ='MACRO_CELL' and m.cellId is not null"),
	//@NamedQuery(name = "getCellListByGeogrphyL1" ,query ="select distinct m.networkElement.neName from MacroSiteDetail m where m.networkElement.geographyL4.geographyL3.geographyL2.geographyL1.name =:geographyName and m.networkElement.vendor =:vendor and m.networkElement.neType ='MACRO_CELL' and m.cellId is not null"),
	//@NamedQuery(name = "getCellListByGeogrphyPan" ,query ="select distinct m.networkElement.neName from MacroSiteDetail m where m.networkElement.vendor =:vendor and m.networkElement.neType ='MACRO_CELL' and m.cellId is not null"),
	
	//@NamedQuery(name = "getAllSapIDCnum",query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(m.cellId,m.networkElement.networkElement.neName,m.networkElement.geographyL4.geographyL3.geographyL2.name) from MacroSiteDetail m where upper(m.networkElement.vendor)=:vendor and m.networkElement.neType =:neType and m.cellId is not null and m.networkElement.neFrequency in (:band) "),
	//@NamedQuery(name = "getSapIDCnumBYGL1Wise",query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(m.cellId,m.networkElement.networkElement.neName,m.networkElement.geographyL4.geographyL3.geographyL2.name) from MacroSiteDetail m where UPPER(m.networkElement.geographyL4.geographyL3.geographyL2.geographyL1.name) IN (:geographyLevelName) and upper(m.networkElement.vendor)=:vendor and m.networkElement.neType =:neType and m.cellId is not null and m.networkElement.neFrequency in (:band) "),
	//@NamedQuery(name = "getSapIDCnumBYGL2Wise",query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(m.cellId,m.networkElement.networkElement.neName,m.networkElement.geographyL4.geographyL3.geographyL2.name) from MacroSiteDetail m where UPPER(m.networkElement.geographyL4.geographyL3.geographyL2.name) IN (:geographyLevelName) and upper(m.networkElement.vendor)=:vendor and m.networkElement.neType =:neType and m.cellId is not null and m.networkElement.neFrequency in (:band) "),
	//@NamedQuery(name = "getSapIDCnumBYGL3Wise",query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(m.cellId,m.networkElement.networkElement.neName,m.networkElement.geographyL4.geographyL3.geographyL2.name) from MacroSiteDetail m where UPPER(m.networkElement.geographyL4.geographyL3.name) IN (:geographyLevelName) and upper(m.networkElement.vendor)=:vendor and m.networkElement.neType =:neType and m.cellId is not null and m.networkElement.neFrequency in (:band) "),
	//@NamedQuery(name = "getSapIDCnumBYGL4Wise",query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(m.cellId,m.networkElement.networkElement.neName,m.networkElement.geographyL4.geographyL3.geographyL2.name) from MacroSiteDetail m where UPPER(m.networkElement.geographyL4.name) IN (:geographyLevelName) and upper(m.networkElement.vendor)=:vendor and m.networkElement.neType =:neType and m.cellId is not null and m.networkElement.neFrequency in (:band) "),
	//@NamedQuery(name = "getUploadedCustomCell",query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(m.cellId,m.networkElement.networkElement.neName,m.networkElement.geographyL4.geographyL3.geographyL2.name,m.networkElement.geographyL4.geographyL3.name) from MacroSiteDetail m where m.networkElement.networkElement.neName||'_'||m.cellId in (:neIdList)  and upper(m.networkElement.vendor)=:vendor and m.networkElement.neType =:neType and m.cellId is not null "),
	//@NamedQuery(name = "getUploadedCustomENB",query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(m.cellId,m.networkElement.networkElement.neName,m.networkElement.geographyL4.geographyL3.geographyL2.name,m.networkElement.geographyL4.geographyL3.name) from MacroSiteDetail m where m.networkElement.networkElement.neName in (:neIdList)  and upper(m.networkElement.vendor)=:vendor and m.networkElement.neType =:neType and m.networkElement.neName is not null "),
	/*@NamedQuery(name = "getCellDetailByVendorAndStatus", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.id, m.cellId,"
			+ " m.neBandDetail.neFrequency, m.neBandDetail.neStatus, n.neType, n.networkElement.neName,"
			+ " n.technology, n.vendor, n.domain, n.neId, n.latitude, n.longitude, n.geographyL4.name) from MacroSiteDetail m inner join NetworkElement"
			+ " n on m.networkElement=n.id where n.neName is not null and m.cellId is not null and n.networkElement is not null and n.vendor=:vendor"
			+ " and m.neBandDetail.neStatus=:neStatus and n.isDeleted=0"),*/
	//@NamedQuery(name = "getCellNameSpecificData", query="select m.networkElement  from  MacroSiteDetail m where  concat(m.networkElement.networkElement.neName,'_',m.cellId) in :neIdList"),
	
	//foresight-micro-service
	//@NamedQuery(name="getSiteByGL2ForSmallCell",query="select distinct(concat(m.networkElement.neName,'_',m.cellName)) from MacroSiteDetail m where upper(m.networkElement.geographyL4.geographyL3.geographyL2.name) in (:geographyLevelName) and upper(m.networkElement.neType) like '%SMALL_CELL%'  and upper(m.networkElement.vendor)=:vendor "),
	//@NamedQuery(name="getSiteByGL4ForSmallCell",query="select distinct(concat(m.networkElement.neName,'_',m.cellName)) from MacroSiteDetail m where upper(m.networkElement.geographyL4.name) in (:geographyLevelName) and upper(m.networkElement.neType) like '%SMALL_CELL%'  and upper(m.networkElement.vendor)=:vendor "),

	//@NamedQuery(name="getpmSiteByGL2forSmallCell",query="select distinct(concat(m.networkElement.neName,'_',m.cellName)) from MacroSiteDetail m where upper(m.networkElement.geographyL4.geographyL3.geographyL2.name) in (:geographyLevelName) and m.networkElement.neFrequency=:neFrequency and upper(m.networkElement.neType) like '%SMALL_CELL%' and upper(m.networkElement.vendor)=:vendor"),
	//@NamedQuery(name="getPmSiteByGL4forSmallCell",query="select distinct(concat(m.networkElement.neName,'_',m.cellName)) from MacroSiteDetail m where  upper(m.networkElement.geographyL4.name) in (:geographyLevelName) and m.networkElement.neFrequency=:neFrequency and upper(m.networkElement.neType) like '%SMALL_CELL%' and upper(m.networkElement.vendor)=:vendor"),

	//@NamedQuery(name = "getSapIDCnumBYGL1",query = "select concat(m.networkElement.neName,'_',m.cellName) from MacroSiteDetail m where UPPER(m.networkElement.geographyL4.geographyL3.geographyL2.geographyL1.name) IN (:geographyLevelName) and upper(m.networkElement.vendor)=:vendor and m.networkElement.neName is not null and m.cellName is not null"),
	//@NamedQuery(name = "getSapIDCnumBYGL2",query = "select concat(m.networkElement.neName,'_',m.cellName) from MacroSiteDetail m where UPPER(m.networkElement.geographyL4.geographyL3.geographyL2.name) IN (:geographyLevelName) and upper(m.networkElement.vendor)=:vendor and m.networkElement.neName is not null and m.cellName is not null"),
	//@NamedQuery(name = "getSapIDCnumBYGL3",query = "select concat(m.networkElement.neName,'_',m.cellName) from MacroSiteDetail m where UPPER(m.networkElement.geographyL4.geographyL3.name) IN (:geographyLevelName) and upper(m.networkElement.vendor)=:vendor and m.networkElement.neName is not null and m.cellName is not null"),
	//@NamedQuery(name = "getSapIDCnumBYGL4",query = "select concat(m.networkElement.neName,'_',m.cellName) from MacroSiteDetail m where UPPER(m.networkElement.geographyL4.name) IN (:geographyLevelName) and upper(m.networkElement.vendor)=:vendor and m.networkElement.neName is not null and m.cellName is not null"),

//	@NamedQuery(name = "getSapIDCNumBYGL1AndBand",query = "select concat(m.networkElement.neName,'_',m.cellName) from MacroSiteDetail m where UPPER(m.networkElement.geographyL4.geographyL3.geographyL2.geographyL1.name) IN (:geographyLevelName) and m.networkElement.neFrequency=:neFrequency and upper(m.networkElement.vendor)=:vendor and m.networkElement.neName is not null and m.cellName is not null"),
//	@NamedQuery(name = "getSapIDCNumBYGL2AndBand",query = "select concat(m.networkElement.neName,'_',m.cellName) from MacroSiteDetail m where UPPER(m.networkElement.geographyL4.geographyL3.geographyL2.name) IN (:geographyLevelName) and m.networkElement.neFrequency=:neFrequency and upper(m.networkElement.vendor)=:vendor and m.networkElement.neName is not null and m.cellName is not null"),
//	@NamedQuery(name = "getSapIDCNumBYGL3AndBand",query = "select concat(m.networkElement.neName,'_',m.cellName) from MacroSiteDetail m where UPPER(m.networkElement.geographyL4.geographyL3.name) IN (:geographyLevelName) and m.networkElement.neFrequency=:neFrequency and upper(m.networkElement.vendor)=:vendor and m.networkElement.neName is not null and m.cellName is not null"),
//	@NamedQuery(name = "getSapIDCNumBYGL4AndBand",query = "select concat(m.networkElement.neName,'_',m.cellName) from MacroSiteDetail m where UPPER(m.networkElement.geographyL4.name) IN (:geographyLevelName) and m.networkElement.neFrequency=:neFrequency and upper(m.networkElement.vendor)=:vendor and m.networkElement.neName is not null and m.cellName is not null"),
	@NamedQuery(name = "getSapIdByGeographyL2forSite", query="select distinct m.networkElement.neName from MacroSiteDetail m where UPPER(m.networkElement.geographyL4.geographyL3.geographyL2.name) IN (:geographyL2) and upper(m.networkElement.vendor)=:vendor and m.networkElement.neName is not null and m.networkElement.geographyL4 is not null"),
	@NamedQuery(name = "getSapIdByGeographyL4forSite", query="select distinct m.networkElement.neName from MacroSiteDetail m where UPPER(m.networkElement.geographyL4.name) IN (:geographyL4)  and upper(m.networkElement.vendor)=:vendor and m.networkElement.neName is not null and m.networkElement.geographyL4 is not null"),
	//@NamedQuery(name = "getEcgiLocation",query="Select new com.inn.foresight.core.infra.wrapper.MacroSitesDetailWrapper(m.ecgi, m.networkElement.latitude, m.networkElement.longitude) from MacroSiteDetail m where m.ecgi IS NOT NULL"),
	@NamedQuery(name = "getMacroSiteByNeNameandType",query="select m from MacroSiteDetail m where m.networkElement.neName=:neName and m.networkElement.neType=:neType"),
	//@NamedQuery(name = "getDistinctCellIdsByNeType",query="select distinct m.cellId from MacroSiteDetail m where m.networkElement.neType=:neType order by m.cellId asc"),
	//@NamedQuery(name = "getDistinctCellIds",query="select distinct m.cellId from MacroSiteDetail m order by m.cellId asc"),
	//@NamedQuery(name = "getDistinctSectorsByCellId",query="select distinct m.sector from MacroSiteDetail m where m.cellId IN(:cellIds)"),
	@NamedQuery(name = "getAllDistinctSectors",query="select distinct m.sector from MacroSiteDetail m"),
	@NamedQuery(name = "getMacroSiteDetailByCGIandPci",query ="Select new com.inn.commons.maps.LatLng(m.networkElement.latitude,m.networkElement.longitude) from MacroSiteDetail m where m.pci=:pci and m.cgi=:cgi"),
	@NamedQuery(name = "getMacroSiteDetailByCGI",query ="Select m from MacroSiteDetail m where m.cgi=:cgi"),
	@NamedQuery(name = "getMacroSiteDetailByListNename",query ="Select m from MacroSiteDetail m where m.networkElement.networkElement.neName in (:nename) and m.networkElement.isDeleted=0 and m.networkElement.networkElement is not null"),
	@NamedQuery(name = "getDistinctPCIBySiteIdAndBand", query ="select distinct m.pci from MacroSiteDetail m where  m.networkElement.neFrequency in (:bandList) and m.networkElement.networkElement.neName =:neName")
})
@Entity
@Table(name = "MacroSiteDetail")
@XmlRootElement(name = "MacroSiteDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class MacroSiteDetail implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1206541838752815595L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "macrositedetailid_pk")
	private Integer id;
	
	/*@Basic
	@Column(name = "emslive")
	@Deprecated
	private String emsLive;

	@Basic
	@Column(name = "emslivedate")
	@Deprecated
	private Date emsLiveDate;

	*//** The non radiating date. *//*
	@Basic
	@Column(name = "nonradiatingdate")
	@Deprecated
	private Date nonRadiatingDate;*/
	
	@Basic
	@Column(name = "earfcn")
	private Integer earfcn;
	
	/** The antenna type. */
	@Basic
	@Column(name = "antennatype")
	private String antennaType;
	
	@Basic
	@Column(name = "antennamodel")
	private String antennaModel;
	
	
	
	@Basic
	@Column(name = "onairdate")
	private Date onAirDate;
	
	/** The mech tilt. */
	@Basic
	@Column(name = "mechtilt")
	private Integer mechTilt;

	/** The elec tilt. */
	@Basic
	@Column(name = "electilt")
	private Integer elecTilt;

	/** The azimuth. */
	@Basic
	@Column(name = "azimuth")
	private Integer azimuth;
	
	/** The antenna height. */
	@Basic
	@Column(name = "antennaheight")
	private Double antennaHeight;

	/** The pci. */
	@Basic
	@Column(name = "pci")
	private Integer pci;
	
	/** The cell id. */
	/*@Basic
	@Column(name = "cellid")
	@Deprecated
	private Integer cellId;

	*//** The enb id. *//*
	@Basic
	@Column(name = "enbid")
	@Deprecated
	private Integer enbId;*/
	
	/** The sector. */
	@Basic
	@Column(name = "sector")
	private Integer sector;
	
	/** The operational status. */
	@Basic
	@Column(name = "operationalstatus")
	private String operationalStatus;

	/** The admin state. */
	@Basic
	@Column(name = "adminState")
	private String adminState;
	
	/** The cell name. */
/*	@Basic
	@Column(name = "cellname")
	@Deprecated
	private String cellName;*/
	
	/** The network element. */
	@JoinColumn(name = "networkelementid_fk", nullable = false)
	@OneToOne(fetch = FetchType.LAZY)
	private NetworkElement networkElement;
	
	@JoinColumn(name = "nebanddetailid_fk", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private NEBandDetail neBandDetail;

	@Basic
	@Column(name = "tac")
	private String trackingArea;
	
	/** The ems live counter. */
	/*@Basic
	@Deprecated
	@Column(name = "emslivecounter")
	private Integer emsLiveCounter;

	*//** The lsrip. *//*
	@Basic
	@Column(name = "lsrip")
	@Deprecated
	private String lsrip;
	
	*//** The scope. *//*
	@Basic
	@Column(name = "ecgi")
	@Deprecated
	private String ecgi;
	
	@Basic
	@Deprecated
	@Column(name = "carrier")
	private String carrier;
	

	@Basic
	@Deprecated
	@Column(name = "radiusthreshold")
	private String radiusThreshold;*/
	
	@Basic
	@Column(name = "bandwidth")
	private String bandwidth;
	
	@Basic
	@Column(name = "txpower")
	private String txPower;
	
	/*@Basic
	@Deprecated
	@Column(name = "eirp")
	private String eirp;
	
	@Basic
	@Column(name = "dlearfcn")
	@Deprecated
	private Integer dlearfcn;
	
	@Basic
	@Column(name = "ulearfcn")
	@Deprecated
	private Integer ulearfcn;
	
	@Basic
	@Column(name = "feedercablelength")
	@Deprecated
	private Double feederCableLength;
	
	@Basic
	@Column(name = "opticcablelength")
	@Deprecated
	private Double opticCableLength;*/
	
	@Basic
	@Column(name = "antennagain")
	private String antennaGain;
	
	@Basic
	@Column(name = "antennavendor")
	private String antennaVendor;
	
	/*@Basic
	@Column(name = "totaltilt")
	@Deprecated
	private Double totalTilt;
*/	
	@Basic
	@Column(name = "horizontalbeamwidth")
	private Double horizontalBeamWidth;
	
	@Basic
	@Column(name = "verticalbeamwidth")
	private Double VerticalBeamWidth;
	
	/*@Basic
	@Column(name = "propagationmodel")
	@Deprecated
	private String propagationModel;
	
	@Basic
	@Column(name = "pilotchannel")
	@Deprecated
	private Integer pilotChannel;
	
	@Basic
	@Column(name = "rsrpthreshold")
	@Deprecated
	private Integer rsrpThreshold;
	
	@Basic
	@Column(name = "basechannelfreq")
	@Deprecated
	private Integer baseChannelFreq;
	
	@Basic
	@Column(name = "decommissioneddate")
	@Deprecated
	private Date decommissionedDate;*/
	
	/** The creation time. */
	@Basic
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modification time. */
	@Basic
	@Column(name = "modificationtime")
	private Date modificationTime;
	
	
	/*@Basic
	@Column(name = "centralfrequency")
	@Deprecated
	private Double centralFrequency;*/
	
	/** The cgi. */
	@Basic
	@Column(name = "cgi")
	private Integer cgi;
	
	@Basic
	@Column(name="minelectilt")
	private Integer minElectilt;
	
	@Basic
	@Column(name="maxelectilt")
	private Integer maxElectilt;
	
	@Basic
	@Column(name="retstatus")
	private Integer retstatus;

	public Integer getRetstatus() {
		return retstatus;
	}

	public void setRetstatus(Integer retstatus) {
		this.retstatus = retstatus;
	}

	public Integer getMinElectilt() {
		return minElectilt;
	}

	public void setMinElectilt(Integer minElectilt) {
		this.minElectilt = minElectilt;
	}

	public Integer getMaxElectilt() {
		return maxElectilt;
	}

	public void setMaxElectilt(Integer maxElectilt) {
		this.maxElectilt = maxElectilt;
	}

	/**
	 * Instantiates a new macro site detail.
	 */
	public MacroSiteDetail() {
		super();
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
	 * Gets the network element.
	 *
	 * @return the network element
	 */
	public NetworkElement getNetworkElement() {
		return networkElement;
	}

	/**
	 * Sets the network element.
	 *
	 * @param networkElement the new network element
	 */
	public void setNetworkElement(NetworkElement networkElement) {
		this.networkElement = networkElement;
	}


	/**
	 * Gets the ems live counter.
	 *
	 * @return the ems live counter
	 */
	/*@Deprecated
	public Integer getEmsLiveCounter() {
		return emsLiveCounter;
	}

	*//**
	 * Sets the ems live counter.
	 *
	 * @param emsLiveCounter the new ems live counter
	 *//*
	@Deprecated
	public void setEmsLiveCounter(Integer emsLiveCounter) {
		this.emsLiveCounter = emsLiveCounter;
	}

	*//**
	 * Gets the non radiating date.
	 *
	 * @return the non radiating date
	 *//*
	@Deprecated
	public Date getNonRadiatingDate() {
		return nonRadiatingDate;
	}

	*//**
	 * Sets the non radiating date.
	 *
	 * @param nonRadiatingDate the new non radiating date
	 *//*
	@Deprecated
	public void setNonRadiatingDate(Date nonRadiatingDate) {
		this.nonRadiatingDate = nonRadiatingDate;
	}

	*//**
	 * Gets the lsrip.
	 *
	 * @return the lsrip
	 *//*
	@Deprecated
	public String getLsrip() {
		return lsrip;
	}

	*//**
	 * Sets the lsrip.
	 *
	 * @param lsrip the new lsrip
	 *//*
	@Deprecated
	public void setLsrip(String lsrip) {
		this.lsrip = lsrip;
	}*/


	/**
	 * Gets the antenna height.
	 *
	 * @return the antenna height
	 */
	public Double getAntennaHeight() {
		return antennaHeight;
	}

	/**
	 * Sets the antenna height.
	 *
	 * @param antennaHeight the new antenna height
	 */
	public void setAntennaHeight(Double antennaHeight) {
		this.antennaHeight = antennaHeight;
	}


	/**
	 * Gets the antenna type.
	 *
	 * @return the antenna type
	 */
	public String getAntennaType() {
		return antennaType;
	}

	/**
	 * Sets the antenna type.
	 *
	 * @param antennaType the new antenna type
	 */
	public void setAntennaType(String antennaType) {
		this.antennaType = antennaType;
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
	 * Instantiates a new macro site detail.
	 *
	 * @param antennaHeight the antenna height
	 * @param antennaType the antenna type
	 * @param azimuth the azimuth
	 * @param elecTilt the elec tilt
	 * @param mechTilt the mech tilt
	 * @param networkElement the network element
	 */
	public MacroSiteDetail(String antennaHeight, String antennaType, Integer azimuth, Integer elecTilt, Integer mechTilt, NetworkElement networkElement) {
		if (antennaHeight != null) {
			this.antennaHeight = Double.valueOf(antennaHeight);
		}
		this.antennaType = antennaType;
		this.azimuth = azimuth;
		if (elecTilt != null) {
			this.elecTilt = elecTilt;
		}
		if (mechTilt != null) {
			this.mechTilt = mechTilt;
		}
		this.networkElement = networkElement;
	}

	/**
	 * Gets the sector.
	 *
	 * @return the sector
	 */
	public Integer getSector() {
		return sector;
	}

	/**
	 * Sets the sector.
	 *
	 * @param sector the new sector
	 */
	public void setSector(Integer sector) {
		this.sector = sector;
	}

	/**
	 * Gets the enb id.
	 *
	 * @return the enb id
	 */
	/*@Deprecated
	public Integer getEnbId() {
		return enbId;
	}

	*//**
	 * Sets the enb id.
	 *
	 * @param enbId the new enb id
	 *//*
	@Deprecated
	public void setEnbId(Integer enbId) {
		this.enbId = enbId;
	}*/

	/**
	 * Gets the operational status.
	 *
	 * @return the operational status
	 */
	public String getOperationalStatus() {
		return operationalStatus;
	}

	/**
	 * Sets the operational status.
	 *
	 * @param operationalStatus the new operational status
	 */
	public void setOperationalStatus(String operationalStatus) {
		this.operationalStatus = operationalStatus;
	}

	/**
	 * Gets the admin state.
	 *
	 * @return the admin state
	 */
	public String getAdminState() {
		return adminState;
	}

	/**
	 * Sets the admin state.
	 *
	 * @param adminState the new admin state
	 */
	public void setAdminState(String adminState) {
		this.adminState = adminState;
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
	 * Gets the cell name.
	 *
	 * @return the cell name
	 */
	/*@Deprecated
	public String getCellName() {
		return cellName;
	}

	*//**
	 * Sets the cell name.
	 *
	 * @param cellName the new cell name
	 *//*
	@Deprecated
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}*/

	public Integer getEarfcn() {
		return earfcn;
	}

	public void setEarfcn(Integer earfcn) {
		this.earfcn = earfcn;
	}

	public String getTrackingArea() {
		return trackingArea;
	}

	public void setTrackingArea(String trackingArea) {
		this.trackingArea = trackingArea;
	}

	/*@Deprecated
	public String getEcgi() {
		return ecgi;
	}

	@Deprecated
	public void setEcgi(String ecgi) {
		this.ecgi = ecgi;
	}*/

	public NEBandDetail getNeBandDetail() {
		return neBandDetail;
	}

	public void setNeBandDetail(NEBandDetail neBandDetail) {
		this.neBandDetail = neBandDetail;
	}

	/*@Deprecated
	public String getCarrier() {
		return carrier;
	}

	@Deprecated
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	@Deprecated
	public String getRadiusThreshold() {
		return radiusThreshold;
	}

	@Deprecated
	public void setRadiusThreshold(String radiusThreshold) {
		this.radiusThreshold = radiusThreshold;
	}

	@Deprecated
	public String getEmsLive() {
		return emsLive;
	}

	@Deprecated
	public void setEmsLive(String emsLive) {
		this.emsLive = emsLive;
	}

	@Deprecated
	public Date getEmsLiveDate() {
		return emsLiveDate;
	}

	@Deprecated
	public void setEmsLiveDate(Date emsLiveDate) {
		this.emsLiveDate = emsLiveDate;
	}*/

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getTxPower() {
		return txPower;
	}

	public void setTxPower(String txPower) {
		this.txPower = txPower;
	}

	/*@Deprecated
	public String getEirp() {
		return eirp;
	}

	@Deprecated
	public void setEirp(String eirp) {
		this.eirp = eirp;
	}

	@Deprecated
	public Integer getDlearfcn() {
		return dlearfcn;
	}

	@Deprecated
	public void setDlearfcn(Integer dlearfcn) {
		this.dlearfcn = dlearfcn;
	}

	@Deprecated
	public Integer getUlearfcn() {
		return ulearfcn;
	}

	@Deprecated
	public void setUlearfcn(Integer ulearfcn) {
		this.ulearfcn = ulearfcn;
	}

	@Deprecated
	public Double getFeederCableLength() {
		return feederCableLength;
	}

	@Deprecated
	public void setFeederCableLength(Double feederCableLength) {
		this.feederCableLength = feederCableLength;
	}

	@Deprecated
	public Double getOpticCableLength() {
		return opticCableLength;
	}

	@Deprecated
	public void setOpticCableLength(Double opticCableLength) {
		this.opticCableLength = opticCableLength;
	}*/

	public String getAntennaGain() {
		return antennaGain;
	}

	public void setAntennaGain(String antennaGain) {
		this.antennaGain = antennaGain;
	}

	public String getAntennaVendor() {
		return antennaVendor;
	}

	public void setAntennaVendor(String antennaVendor) {
		this.antennaVendor = antennaVendor;
	}

	/*@Deprecated
	public Double getTotalTilt() {
		return totalTilt;
	}

	@Deprecated
	public void setTotalTilt(Double totalTilt) {
		this.totalTilt = totalTilt;
	}*/

	public Double getHorizontalBeamWidth() {
		return horizontalBeamWidth;
	}

	public void setHorizontalBeamWidth(Double horizontalBeamWidth) {
		this.horizontalBeamWidth = horizontalBeamWidth;
	}

	public Double getVerticalBeamWidth() {
		return VerticalBeamWidth;
	}

	public void setVerticalBeamWidth(Double verticalBeamWidth) {
		VerticalBeamWidth = verticalBeamWidth;
	}

	/*@Deprecated
	public String getPropagationModel() {
		return propagationModel;
	}

	@Deprecated
	public void setPropagationModel(String propagationModel) {
		this.propagationModel = propagationModel;
	}

	@Deprecated
	public Integer getPilotChannel() {
		return pilotChannel;
	}

	@Deprecated
	public void setPilotChannel(Integer pilotChannel) {
		this.pilotChannel = pilotChannel;
	}
	
	@Deprecated
	public Integer getRsrpThreshold() {
		return rsrpThreshold;
	}

	@Deprecated
	public void setRsrpThreshold(Integer rsrpThreshold) {
		this.rsrpThreshold = rsrpThreshold;
	}

	@Deprecated
	public Integer getBaseChannelFreq() {
		return baseChannelFreq;
	}

	@Deprecated
	public void setBaseChannelFreq(Integer baseChannelFreq) {
		this.baseChannelFreq = baseChannelFreq;
	}

	@Deprecated
	public Date getDecommissionedDate() {
		return decommissionedDate;
	}

	@Deprecated
	public void setDecommissionedDate(Date decommissionedDate) {
		this.decommissionedDate = decommissionedDate;
	}

	@Deprecated
	public Integer getCellId() {
		return cellId;
	}

	@Deprecated
	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}*/

	public Date getOnAirDate() {
		return onAirDate;
	}

	public void setOnAirDate(Date onAirDate) {
		this.onAirDate = onAirDate;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	/*@Deprecated
	public Double getCentralFrequency() {
		return centralFrequency;
	}

	@Deprecated
	public void setCentralFrequency(Double centralFrequency) {
		this.centralFrequency = centralFrequency;
	}*/

	public Integer getCgi() {
		return cgi;
	}

	public void setCgi(Integer cgi) {
		this.cgi = cgi;
	}

	public String getAntennaModel() {
		return antennaModel;
	}

	public void setAntennaModel(String antennaModel) {
		this.antennaModel = antennaModel;
	}

	@Override
	public String toString() {
		return "MacroSiteDetail [id=" + id + ", earfcn=" + earfcn + ", antennaType=" + antennaType + ", antennaModel="
				+ antennaModel + ", onAirDate=" + onAirDate + ", mechTilt=" + mechTilt + ", elecTilt=" + elecTilt
				+ ", azimuth=" + azimuth + ", antennaHeight=" + antennaHeight + ", pci=" + pci + ", sector=" + sector
				+ ", operationalStatus=" + operationalStatus + ", adminState=" + adminState + ", networkElement="
				+ networkElement + ", neBandDetail=" + neBandDetail + ", trackingArea=" + trackingArea + ", bandwidth="
				+ bandwidth + ", txPower=" + txPower + ", antennaGain=" + antennaGain + ", antennaVendor="
				+ antennaVendor + ", horizontalBeamWidth=" + horizontalBeamWidth + ", VerticalBeamWidth="
				+ VerticalBeamWidth + ", creationTime=" + creationTime + ", modificationTime=" + modificationTime
				+ ", cgi=" + cgi + ", minElectilt=" + minElectilt + ", maxElectilt=" + maxElectilt + ", retstatus="
				+ retstatus + "]";
	}

	

	
}
