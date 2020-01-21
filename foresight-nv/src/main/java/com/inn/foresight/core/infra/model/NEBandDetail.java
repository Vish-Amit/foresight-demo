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

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.utils.enums.NEStatus;

@NamedQueries({
		// @NamedQuery(name="getNeBandDetailBySite",query="select distinct new
		// com.inn.foresight.core.infra.wrapper.SiteDataWrapper(n.networkElement.neFrequency,n.networkElement.neName,n.enodeBId,n.networkElement.id)
		// from NEBandDetail n where n.networkElement.neName in (:neId)"),
		// @NamedQuery(name="checkForSitesIsOnAirOrNot",query="select nb from
		// NEBandDetail nb where nb.neStatus =
		// :neStatus and nb.networkElement.neName = :nename and nb.neFrequency = :band
		// and nb.networkElement.isDeleted =
		// false "),
		// @NamedQuery(name = "getNEIdAndSiteName", query = "select new
		// com.inn.foresight.core.infra.wrapper.NeDataWrapper(siteName,networkElement.neName)
		// from NEBandDetail where
		// siteName is not null"),
		@NamedQuery(name = "getNefrequencyForNename", query = "select distinct n.neFrequency from NEBandDetail n where n.networkElement.neName =:neName"),
		@NamedQuery(name = "getNetworkElementWrapperForNename", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.networkElement.neName,n.neFrequency,n.bandStatus,n.bandOnairDate,n.currentStage,n.backhauInfo,n.numberOfSector) from NEBandDetail n where n.networkElement.neName in(:neName)"),
		@NamedQuery(name = "searchNeBandDetailByNetworkElement", query = "select ne from NEBandDetail ne where ne.networkElement.id=:networkElementFk"),
		@NamedQuery(name = "updateBandStatusInNeBand", query = "update NEBandDetail ne set ne.bandStatus=:status where ne.networkElement.id=:riuNetworkElement"),
		@NamedQuery(name = "getOnAirSiteDetailsWithOnAirDate", query = "select min(nb.bandOnairDate) , nb.networkElement.neName , nb.networkElement.geographyL4.name,nb.networkElement.neLocation.address from NEBandDetail nb where DATE_FORMAT(nb.bandOnairDate,'%Y-%m-%d-%H-%i') >= :previousHour and DATE_FORMAT(nb.bandOnairDate,'%Y-%m-%d-%H-%i') < :currentHour and nb.networkElement.domain=:domain and nb.networkElement.vendor=:vendor and nb.networkElement.neStatus=:neStatus  and nb.networkElement.neType in (:neType) and nb.networkElement.isDeleted=0 group by nb.networkElement.neName , nb.networkElement.geographyL4.name"),
		@NamedQuery(name = "getOnAirSitesCountForDayMinusOne", query = "select count(distinct nb.networkElement.neName) from NEBandDetail nb where DATE_FORMAT(nb.bandOnairDate,'%Y-%m-%d') = :previousDate and nb.networkElement.domain=:domain and nb.networkElement.vendor=:vendor and nb.networkElement.neStatus = :neStatus and nb.networkElement.neType in (:neType) and nb.networkElement.isDeleted=0 "),
		@NamedQuery(name = "getOnAirSitesCountByDate", query = "select count(distinct nb.networkElement.neName) from NEBandDetail nb where nb.bandOnairDate is not null and nb.bandOnairDate < :finalDate and nb.networkElement.neType in (:neType) and nb.networkElement.isDeleted = 0 "),
		@NamedQuery(name = "getPlannedSitesCountByDate", query = "select count(distinct nb.networkElement.neName) from NEBandDetail nb where (nb.bandOnairDate is null or DATE(nb.bandOnairDate) >= :finalDate) and DATE(nb.networkElement.createdTime)< :finalDate and nb.networkElement.neType in (:neType) and nb.networkElement.isDeleted = 0 "),
		@NamedQuery(name = "getSitesCountNestatusAndGeographyWise", query = "select nb.networkElement.neStatus,count(distinct nb.networkElement.neName) from NEBandDetail nb where  nb.networkElement.domain=:domain and nb.networkElement.vendor=:vendor and nb.networkElement.neStatus in (:neStatus) and nb.networkElement.neType in (:neType) and nb.networkElement.isDeleted=0 group by nb.networkElement.neStatus "),
		// @NamedQuery(name="getMorphologyByCode",query="select distinct nb.morphology
		// from NEBandDetail nb where
		// nb.networkElement.neName=:btsName"),

})

	@FilterDefs({
	@FilterDef(name = "geographyGL4Filter", parameters = {
            @ParamDef(name = ForesightConstants.GEOGRAPHYVALUE, type = "java.lang.String")}),
	@FilterDef(name = "geographyGL3Filter", parameters = {
            @ParamDef(name = ForesightConstants.GEOGRAPHYVALUE, type = "java.lang.String")}),
	@FilterDef(name = "geographyGL2Filter", parameters = {
            @ParamDef(name = ForesightConstants.GEOGRAPHYVALUE, type = "java.lang.String")}),
	@FilterDef(name = "geographyGL1Filter", parameters = {
            @ParamDef(name = ForesightConstants.GEOGRAPHYVALUE, type = "java.lang.String")}),
			})

	@Filters({
		@Filter(name = "geographyGL4Filter", condition = " networkelementid_fk in (select n.networkelementid_pk from NetworkElement n inner join GeographyL4 l4 on n.geographyl4id_fk = l4.geographyl4id_pk where l4.name=:geographyValue ) "),
		@Filter(name = "geographyGL3Filter", condition = " networkelementid_fk in (select n.networkelementid_pk from NetworkElement n inner join GeographyL4 l4 on n.geographyl4id_fk = l4.geographyl4id_pk inner join  GeographyL3 l3 on l4.geographyl3id_fk= l3.geographyl3id_pk where l3.name=:geographyValue ) "),
		@Filter(name = "geographyGL2Filter", condition = " networkelementid_fk in (select n.networkelementid_pk from NetworkElement n inner join GeographyL4 l4 on n.geographyl4id_fk = l4.geographyl4id_pk inner join  GeographyL3 l3 on l4.geographyl3id_fk= l3.geographyl3id_pk inner join GeographyL2 l2 on l3.geographyl2id_fk = l2.geographyl2id_pk   where l2.name=:geographyValue ) "),
		@Filter(name = "geographyGL1Filter", condition = " networkelementid_fk in (select n.networkelementid_pk from NetworkElement n inner join GeographyL4 l4 on n.geographyl4id_fk = l4.geographyl4id_pk inner join  GeographyL3 l3 on l4.geographyl3id_fk= l3.geographyl3id_pk inner join GeographyL2 l2 on l3.geographyl2id_fk = l2.geographyl2id_pk   inner join GeographyL1 l1 on l2.geographyl1id_fk=l1.geographyl1id_pk where l1.name=:geographyValue ) "),
			})

@Entity
@Table(name = "NEBandDetail")
@XmlRootElement(name = "NEBandDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Audited
public class NEBandDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6004001949351386829L;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "nebanddetailid_pk")
	private Integer id;

	@Basic
	@Column(name = "nefrequency")
	private String neFrequency;

	/*
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "sitename") private String siteName;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "plannedlatitude") private Double plannedLatitude;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "plannedlongitude") private Double plannedLongitude;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "oamipv6add") private String oamIpv6Add;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "oamipv6subnetm") private String oamIpv6Subnetm;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "oamgipv6add") private String oamGIpv6Add;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "oamgipv6subnetm") private String oamGIpv6Subnetm;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "signalipv6add") private String signalIpv6Add;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "signalipv6subnetm") private String signalIpv6Subnetm;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "signalgipv6add") private String signalGIpv6Add;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "signalgipv6subnetm") private String signalGIpv6Subnetm;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "bearersaegwprigid") private String bearerSAEGWPriGId;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "beareripv6add") private String bearerIpv6Add;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "bandwidth") private String bandwidth;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "beareripv6subnetm") private String bearerIpv6Subnetm;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "bearergipv6add") private String bearerGIpv6Add;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "contactname") private String ContactName;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "contactnumber") private String ContactNumber;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "onroofstructureheight") private String OnroofStructureHeight;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "onroofstructuretype") private String OnroofStructureType;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "backhaulmedia") private String backHaulMedia;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "enodebpackage") private String enodeBPackage;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "enodebid") private String enodeBId;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "oamvlan") private String oamVlan;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "oamlsmrid") private String oamLsmrId;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "signalvlan") private String signalVlan;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "signalmmegroupid") private String signalMmeGroupId;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "bearervlan") private String bearerVlan;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "bearergipv6subnetm") private String bearerGIPV6SubnetMask;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "enodestatus") private String enodeStatus;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "imageurl") private String imageUrl;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "enodebtype") private String enodeBType;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "sectorbandwidth") private String sectorBandwidth;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "rjnetworkentityid") private String rjNetworkEntityId;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "emslive") private String emsLive;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "emslivedate") private Date emsLiveDate;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "totalapcount") private String totalApCount;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "adminstatus") private String adminStatus;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "buidlingrjid") private String buidlingrjid;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "emsipv6address") private String emsIPV6Address;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "emshostname") private String emsHostname;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "equipmentstatus") private String equipmentStatus;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "buildingname") private String buildingName;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "buildingtype") private String buildingType;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "buildingpriority") private String buildingPriority;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "siteaddress") private String siteAddress;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "emfcompliance") private String emfCompliance;
	 * 
	 * @Basic
	 * 
	 * @Column(name = "onairdate") private Date onAirDate;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "nestatus")
	 * 
	 * @Enumerated(EnumType.STRING) private NEStatus neStatus;
	 */

	@Basic
	@Column(name = "creationtime")
	private Date creationTime;

	@Basic
	@Column(name = "modificationtime")
	private Date modificationTime;

	/*
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "refsapid") private String refSapid;
	 */

	@Basic
	@Column(name = "numberofsector")
	private Integer numberOfSector;

	/*
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "maintenancezonecode") private String maintenanceZoneCode;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "maintenancezonename") private String maintenanceZoneName;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "operatorname") private String operatorName;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "emslivecounter") private Integer emsLiveCounter;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "sitecategory") private String siteCategory;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "decommissioneddate") private Date decommissionedDate;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "nonradiatingdate") private Date nonRadiatingDate;
	 */

	@JoinColumn(name = "networkelementid_fk", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private NetworkElement networkElement;

	@Basic
	@Column(name = "currentstage")
	private String currentStage;

	@Basic
	@Column(name = "taskprogress")
	private Integer taskPercentage;

	/*
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "cluttercategory") private String clutterCategory;
	 * 
	 * @Deprecated
	 * 
	 * @Basic
	 * 
	 * @Column(name = "morphology") private String morphology;
	 */

	@Basic
	@Column(name = "carrier")
	private String carrier;

	@Basic
	@Column(name = "bandstatus")
	@Enumerated(EnumType.STRING)
	private NEStatus bandStatus;

	@Basic
	@Column(name = "bandonairdate")
	private Date bandOnairDate;

	@Basic
	@Column(name = "tentativeonairdate")
	private Date tentativeOnairDate;

	@Basic
	@Column(name = "backhaulinfo")
	private String backhauInfo;

	@JoinColumn(name = "nedetailid_fk", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private NEDetail neDetail;

	public NEBandDetail() {
		super();
	}

	public NEBandDetail(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNeFrequency() {
		return neFrequency;
	}

	public void setNeFrequency(String neFrequency) {
		this.neFrequency = neFrequency;
	}

	/*
	 * @Deprecated public String getSiteName() { return siteName; }
	 * 
	 * @Deprecated public void setSiteName(String siteName) { this.siteName =
	 * siteName; }
	 * 
	 * @Deprecated public Double getPlannedLatitude() { return plannedLatitude; }
	 * 
	 * @Deprecated public void setPlannedLatitude(Double plannedLatitude) {
	 * this.plannedLatitude = plannedLatitude; }
	 * 
	 * @Deprecated public Double getPlannedLongitude() { return plannedLongitude; }
	 * 
	 * @Deprecated public void setPlannedLongitude(Double plannedLongitude) {
	 * this.plannedLongitude = plannedLongitude; }
	 * 
	 * @Deprecated public String getOamIpv6Add() { return oamIpv6Add; }
	 * 
	 * @Deprecated public void setOamIpv6Add(String oamIpv6Add) { this.oamIpv6Add =
	 * oamIpv6Add; }
	 * 
	 * @Deprecated public String getOamIpv6Subnetm() { return oamIpv6Subnetm; }
	 * 
	 * @Deprecated public void setOamIpv6Subnetm(String oamIpv6Subnetm) {
	 * this.oamIpv6Subnetm = oamIpv6Subnetm; }
	 * 
	 * @Deprecated public String getOamGIpv6Add() { return oamGIpv6Add; }
	 * 
	 * @Deprecated public void setOamGIpv6Add(String oamGIpv6Add) { this.oamGIpv6Add
	 * = oamGIpv6Add; }
	 * 
	 * @Deprecated public String getOamGIpv6Subnetm() { return oamGIpv6Subnetm; }
	 * 
	 * @Deprecated public void setOamGIpv6Subnetm(String oamGIpv6Subnetm) {
	 * this.oamGIpv6Subnetm = oamGIpv6Subnetm; }
	 * 
	 * @Deprecated public String getSignalIpv6Add() { return signalIpv6Add; }
	 * 
	 * @Deprecated public void setSignalIpv6Add(String signalIpv6Add) {
	 * this.signalIpv6Add = signalIpv6Add; }
	 * 
	 * @Deprecated public String getSignalIpv6Subnetm() { return signalIpv6Subnetm;
	 * }
	 * 
	 * @Deprecated public void setSignalIpv6Subnetm(String signalIpv6Subnetm) {
	 * this.signalIpv6Subnetm = signalIpv6Subnetm; }
	 * 
	 * @Deprecated public String getSignalGIpv6Add() { return signalGIpv6Add; }
	 * 
	 * @Deprecated public void setSignalGIpv6Add(String signalGIpv6Add) {
	 * this.signalGIpv6Add = signalGIpv6Add; }
	 * 
	 * @Deprecated public String getSignalGIpv6Subnetm() { return
	 * signalGIpv6Subnetm; }
	 * 
	 * @Deprecated public void setSignalGIpv6Subnetm(String signalGIpv6Subnetm) {
	 * this.signalGIpv6Subnetm = signalGIpv6Subnetm; }
	 * 
	 * @Deprecated public String getBearerSAEGWPriGId() { return bearerSAEGWPriGId;
	 * }
	 * 
	 * @Deprecated public void setBearerSAEGWPriGId(String bearerSAEGWPriGId) {
	 * this.bearerSAEGWPriGId = bearerSAEGWPriGId; }
	 * 
	 * @Deprecated public String getBearerIpv6Add() { return bearerIpv6Add; }
	 * 
	 * @Deprecated public void setBearerIpv6Add(String bearerIpv6Add) {
	 * this.bearerIpv6Add = bearerIpv6Add; }
	 * 
	 * public String getBearerIpv6Subnetm() { return bearerIpv6Subnetm; }
	 * 
	 * public void setBearerIpv6Subnetm(String bearerIpv6Subnetm) {
	 * this.bearerIpv6Subnetm = bearerIpv6Subnetm; }
	 * 
	 * public String getBearerGIpv6Add() { return bearerGIpv6Add; }
	 * 
	 * public void setBearerGIpv6Add(String bearerGIpv6Add) { this.bearerGIpv6Add =
	 * bearerGIpv6Add; }
	 * 
	 * @Deprecated public String getContactName() { return ContactName; }
	 * 
	 * @Deprecated public void setContactName(String contactName) { ContactName =
	 * contactName; }
	 * 
	 * @Deprecated public String getContactNumber() { return ContactNumber; }
	 * 
	 * @Deprecated public void setContactNumber(String contactNumber) {
	 * ContactNumber = contactNumber; }
	 * 
	 * @Deprecated public String getOnroofStructureHeight() { return
	 * OnroofStructureHeight; }
	 * 
	 * @Deprecated public void setOnroofStructureHeight(String
	 * onroofStructureHeight) { OnroofStructureHeight = onroofStructureHeight; }
	 * 
	 * @Deprecated public String getOnroofStructureType() { return
	 * OnroofStructureType; }
	 * 
	 * @Deprecated public void setOnroofStructureType(String onroofStructureType) {
	 * OnroofStructureType = onroofStructureType; }
	 * 
	 * @Deprecated public String getBackHaulMedia() { return backHaulMedia; }
	 * 
	 * @Deprecated public void setBackHaulMedia(String backHaulMedia) {
	 * this.backHaulMedia = backHaulMedia; }
	 * 
	 * @Deprecated public String getEnodeBPackage() { return enodeBPackage; }
	 * 
	 * @Deprecated public void setEnodeBPackage(String enodeBPackage) {
	 * this.enodeBPackage = enodeBPackage; }
	 * 
	 * @Deprecated public String getEnodeBId() { return enodeBId; }
	 * 
	 * @Deprecated public void setEnodeBId(String enodeBId) { this.enodeBId =
	 * enodeBId; }
	 * 
	 * @Deprecated public String getOamVlan() { return oamVlan; }
	 * 
	 * @Deprecated public void setOamVlan(String oamVlan) { this.oamVlan = oamVlan;
	 * }
	 * 
	 * @Deprecated public String getOamLsmrId() { return oamLsmrId; }
	 * 
	 * @Deprecated public void setOamLsmrId(String oamLsmrId) { this.oamLsmrId =
	 * oamLsmrId; }
	 * 
	 * @Deprecated public String getSignalVlan() { return signalVlan; }
	 * 
	 * @Deprecated public void setSignalVlan(String signalVlan) { this.signalVlan =
	 * signalVlan; }
	 * 
	 * @Deprecated public String getSignalMmeGroupId() { return signalMmeGroupId; }
	 * 
	 * @Deprecated public void setSignalMmeGroupId(String signalMmeGroupId) {
	 * this.signalMmeGroupId = signalMmeGroupId; }
	 * 
	 * @Deprecated public String getBearerVlan() { return bearerVlan; }
	 * 
	 * @Deprecated public void setBearerVlan(String bearerVlan) { this.bearerVlan =
	 * bearerVlan; }
	 * 
	 * @Deprecated public String getBearerGIPV6SubnetMask() { return
	 * bearerGIPV6SubnetMask; }
	 * 
	 * @Deprecated public void setBearerGIPV6SubnetMask(String
	 * bearerGIPV6SubnetMask) { this.bearerGIPV6SubnetMask = bearerGIPV6SubnetMask;
	 * }
	 * 
	 * @Deprecated public String getEnodeStatus() { return enodeStatus; }
	 * 
	 * @Deprecated public void setEnodeStatus(String enodeStatus) { this.enodeStatus
	 * = enodeStatus; }
	 * 
	 * @Deprecated public String getImageUrl() { return imageUrl; }
	 * 
	 * @Deprecated public void setImageUrl(String imageUrl) { this.imageUrl =
	 * imageUrl; }
	 * 
	 * @Deprecated public String getEnodeBType() { return enodeBType; }
	 * 
	 * @Deprecated public void setEnodeBType(String enodeBType) { this.enodeBType =
	 * enodeBType; }
	 * 
	 * @Deprecated public String getSectorBandwidth() { return sectorBandwidth; }
	 * 
	 * @Deprecated public void setSectorBandwidth(String sectorBandwidth) {
	 * this.sectorBandwidth = sectorBandwidth; }
	 * 
	 * @Deprecated public String getRjNetworkEntityId() { return rjNetworkEntityId;
	 * }
	 * 
	 * @Deprecated public void setRjNetworkEntityId(String rjNetworkEntityId) {
	 * this.rjNetworkEntityId = rjNetworkEntityId; }
	 * 
	 * @Deprecated public String getEmsLive() { return emsLive; }
	 * 
	 * @Deprecated public void setEmsLive(String emsLive) { this.emsLive = emsLive;
	 * }
	 * 
	 * @Deprecated public String getTotalApCount() { return totalApCount; }
	 * 
	 * @Deprecated public void setTotalApCount(String totalApCount) {
	 * this.totalApCount = totalApCount; }
	 * 
	 * @Deprecated public String getAdminStatus() { return adminStatus; }
	 * 
	 * @Deprecated public void setAdminStatus(String adminStatus) { this.adminStatus
	 * = adminStatus; }
	 * 
	 * @Deprecated public String getBuidlingrjid() { return buidlingrjid; }
	 * 
	 * @Deprecated public void setBuidlingrjid(String buidlingrjid) {
	 * this.buidlingrjid = buidlingrjid; }
	 * 
	 * @Deprecated public String getEmsIPV6Address() { return emsIPV6Address; }
	 * 
	 * @Deprecated public void setEmsIPV6Address(String emsIPV6Address) {
	 * this.emsIPV6Address = emsIPV6Address; }
	 * 
	 * @Deprecated public String getEmsHostname() { return emsHostname; }
	 * 
	 * @Deprecated public void setEmsHostname(String emsHostname) { this.emsHostname
	 * = emsHostname; }
	 * 
	 * @Deprecated public String getEquipmentStatus() { return equipmentStatus; }
	 * 
	 * @Deprecated public void setEquipmentStatus(String equipmentStatus) {
	 * this.equipmentStatus = equipmentStatus; }
	 * 
	 * @Deprecated public String getBuildingName() { return buildingName; }
	 * 
	 * @Deprecated public void setBuildingName(String buildingName) {
	 * this.buildingName = buildingName; }
	 * 
	 * @Deprecated public String getBuildingType() { return buildingType; }
	 * 
	 * @Deprecated public void setBuildingType(String buildingType) {
	 * this.buildingType = buildingType; }
	 * 
	 * @Deprecated public String getBuildingPriority() { return buildingPriority; }
	 * 
	 * @Deprecated public void setBuildingPriority(String buildingPriority) {
	 * this.buildingPriority = buildingPriority; }
	 * 
	 * @Deprecated public String getSiteAddress() { return siteAddress; }
	 * 
	 * @Deprecated public void setSiteAddress(String siteAddress) { this.siteAddress
	 * = siteAddress; }
	 * 
	 * @Deprecated public String getEmfCompliance() { return emfCompliance; }
	 * 
	 * @Deprecated public void setEmfCompliance(String emfCompliance) {
	 * this.emfCompliance = emfCompliance; }
	 * 
	 * public Date getOnAirDate() { return onAirDate; }
	 * 
	 * public void setOnAirDate(Date onAirDate) { this.onAirDate = onAirDate; }
	 * 
	 * @Deprecated public NEStatus getNeStatus() { return neStatus; }
	 * 
	 * @Deprecated public void setNeStatus(NEStatus neStatus) { this.neStatus =
	 * neStatus; }
	 */

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

	public NetworkElement getNetworkElement() {
		return networkElement;
	}

	public void setNetworkElement(NetworkElement networkElement) {
		this.networkElement = networkElement;
	}

	/*
	 * @Deprecated public String getRefSapid() { return refSapid; }
	 * 
	 * @Deprecated public void setRefSapid(String refSapid) { this.refSapid =
	 * refSapid; }
	 */

	public Integer getNumberOfSector() {
		return numberOfSector;
	}

	public void setNumberOfSector(Integer numberOfSector) {
		this.numberOfSector = numberOfSector;
	}

	/*
	 * @Deprecated public String getMaintenanceZoneCode() { return
	 * maintenanceZoneCode; }
	 * 
	 * @Deprecated public void setMaintenanceZoneCode(String maintenanceZoneCode) {
	 * this.maintenanceZoneCode = maintenanceZoneCode; }
	 * 
	 * @Deprecated public String getMaintenanceZoneName() { return
	 * maintenanceZoneName; }
	 * 
	 * @Deprecated public void setMaintenanceZoneName(String maintenanceZoneName) {
	 * this.maintenanceZoneName = maintenanceZoneName; }
	 * 
	 * @Deprecated public String getOperatorName() { return operatorName; }
	 * 
	 * @Deprecated public void setOperatorName(String operatorName) {
	 * this.operatorName = operatorName; }
	 * 
	 * @Deprecated public Integer getEmsLiveCounter() { return emsLiveCounter; }
	 * 
	 * @Deprecated public void setEmsLiveCounter(Integer emsLiveCounter) {
	 * this.emsLiveCounter = emsLiveCounter; }
	 * 
	 * @Deprecated public String getSiteCategory() { return siteCategory; }
	 * 
	 * @Deprecated public void setSiteCategory(String siteCategory) {
	 * this.siteCategory = siteCategory; }
	 * 
	 * @Deprecated public Date getDecommissionedDate() { return decommissionedDate;
	 * }
	 * 
	 * @Deprecated public void setDecommissionedDate(Date decommissionedDate) {
	 * this.decommissionedDate = decommissionedDate; }
	 * 
	 * @Deprecated public Date getNonRadiatingDate() { return nonRadiatingDate; }
	 * 
	 * @Deprecated public void setNonRadiatingDate(Date nonRadiatingDate) {
	 * this.nonRadiatingDate = nonRadiatingDate; }
	 */

	public String getCurrentStage() {
		return currentStage;
	}

	public void setCurrentStage(String currentStage) {
		this.currentStage = currentStage;
	}

	public Integer getTaskPercentage() {
		return taskPercentage;
	}

	public void setTaskPercentage(Integer taskPercentage) {
		this.taskPercentage = taskPercentage;
	}

	/*
	 * @Deprecated public Date getEmsLiveDate() { return emsLiveDate; }
	 * 
	 * @Deprecated public void setEmsLiveDate(Date emsLiveDate) { this.emsLiveDate =
	 * emsLiveDate; }
	 * 
	 * @Deprecated public String getClutterCategory() { return clutterCategory; }
	 * 
	 * @Deprecated public void setClutterCategory(String clutterCategory) {
	 * this.clutterCategory = clutterCategory; }
	 * 
	 * 
	 * @Deprecated public String getBandwidth() { return bandwidth; }
	 * 
	 * @Deprecated public void setBandwidth(String bandwidth) { this.bandwidth =
	 * bandwidth; }
	 * 
	 * @Deprecated public String getMorphology() { return morphology; }
	 * 
	 * @Deprecated public void setMorphology(String morphology) { this.morphology =
	 * morphology; }
	 */

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public NEStatus getBandStatus() {
		return bandStatus;
	}

	public void setBandStatus(NEStatus bandStatus) {
		this.bandStatus = bandStatus;
	}

	public Date getBandOnairDate() {
		return bandOnairDate;
	}

	public void setBandOnairDate(Date bandOnairDate) {
		this.bandOnairDate = bandOnairDate;
	}

	public Date getTentativeOnairDate() {
		return tentativeOnairDate;
	}

	public void setTentativeOnairDate(Date tentativeOnairDate) {
		this.tentativeOnairDate = tentativeOnairDate;
	}

	public String getBackhauInfo() {
		return backhauInfo;
	}

	public void setBackhauInfo(String backhauInfo) {
		this.backhauInfo = backhauInfo;
	}

	public NEDetail getNeDetail() {
		return neDetail;
	}

	public void setNeDetail(NEDetail neDetail) {
		this.neDetail = neDetail;
	}

	@Override
	public String toString() {
		return "NEBandDetail [id=" + id + ", neFrequency=" + neFrequency + ", creationTime=" + creationTime + ", modificationTime=" + modificationTime + ", numberOfSector=" + numberOfSector
				+ ", networkElement=" + networkElement + ", currentStage=" + currentStage + ", taskPercentage=" + taskPercentage + ", carrier=" + carrier + ", bandStatus=" + bandStatus
				+ ", bandOnairDate=" + bandOnairDate + ", tentativeOnairDate=" + tentativeOnairDate + ", backhauInfo=" + backhauInfo + ", neDetail=" + neDetail + "]";
	}

}
