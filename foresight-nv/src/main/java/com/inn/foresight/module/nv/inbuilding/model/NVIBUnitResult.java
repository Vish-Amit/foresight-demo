package com.inn.foresight.module.nv.inbuilding.model;

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

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.model.Unit;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;

/**
 * The Class NVIBUnitResult.
 *
 * @author innoeye
 * date - 15-Mar-2018 3:55:50 PM
 */

	@NamedQuery(name = InBuildingConstants.GET_BUILDING_COUNT_GROUP_BY_GEOL1, query = "select new com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper(count(distinct n.unit.floor.wing.building),n.unit.floor.wing.building.geographyL4.geographyL3.geographyL2.geographyL1.name,n.unit.floor.wing.building.geographyL4.geographyL3.geographyL2.geographyL1.displayName,n.unit.floor.wing.building.geographyL4.geographyL3.geographyL2.geographyL1.latitude,n.unit.floor.wing.building.geographyL4.geographyL3.geographyL2.geographyL1.longitude) from NVIBUnitResult n  where n.unit.floor.wing.building.geographyL4.geographyL3.geographyL2.geographyL1.id in (select g.id from GeographyL1 g where g.latitude>=:SWLat and g.latitude<=:NELat and g.longitude>=:SWLng and g.longitude<=:NELng) and n.technology=:technology and n.unit.floor.wing.building.buildingType=:buildingType group by n.unit.floor.wing.building.geographyL4.geographyL3.geographyL2.geographyL1.name,n.unit.floor.wing.building.geographyL4.geographyL3.geographyL2.geographyL1.latitude,n.unit.floor.wing.building.geographyL4.geographyL3.geographyL2.geographyL1.longitude")
	@NamedQuery(name = InBuildingConstants.GET_BUILDING_COUNT_GROUP_BY_GEOL2, query = "select new com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper(count(distinct n.unit.floor.wing.building),n.unit.floor.wing.building.geographyL4.geographyL3.geographyL2.name,n.unit.floor.wing.building.geographyL4.geographyL3.geographyL2.displayName,n.unit.floor.wing.building.geographyL4.geographyL3.geographyL2.latitude,n.unit.floor.wing.building.geographyL4.geographyL3.geographyL2.longitude) from NVIBUnitResult n  where n.unit.floor.wing.building.geographyL4.geographyL3.geographyL2.id in (select g.id from GeographyL2 g where g.latitude>=:SWLat and g.latitude<=:NELat and g.longitude>=:SWLng and g.longitude<=:NELng) and n.technology=:technology and n.unit.floor.wing.building.buildingType=:buildingType group by n.unit.floor.wing.building.geographyL4.geographyL3.geographyL2.name,n.unit.floor.wing.building.geographyL4.geographyL3.geographyL2.latitude,n.unit.floor.wing.building.geographyL4.geographyL3.geographyL2.longitude")
	@NamedQuery(name = InBuildingConstants.GET_BUILDING_COUNT_GROUP_BY_GEOL3, query = "select new com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper(count(distinct n.unit.floor.wing.building),n.unit.floor.wing.building.geographyL4.geographyL3.name,n.unit.floor.wing.building.geographyL4.geographyL3.displayName,n.unit.floor.wing.building.geographyL4.geographyL3.latitude,n.unit.floor.wing.building.geographyL4.geographyL3.longitude) from NVIBUnitResult n  where n.unit.floor.wing.building.geographyL4.geographyL3.id in (select g.id from GeographyL3 g where g.latitude>=:SWLat and g.latitude<=:NELat and g.longitude>=:SWLng and g.longitude<=:NELng) and n.technology=:technology and n.unit.floor.wing.building.buildingType=:buildingType group by n.unit.floor.wing.building.geographyL4.geographyL3.name,n.unit.floor.wing.building.geographyL4.geographyL3.latitude,n.unit.floor.wing.building.geographyL4.geographyL3.longitude")
	@NamedQuery(name = InBuildingConstants.GET_BUILDING_COUNT_GROUP_BY_GEOL4, query = "select new com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper(count(distinct n.unit.floor.wing.building),n.unit.floor.wing.building.geographyL4.name,n.unit.floor.wing.building.geographyL4.displayName,n.unit.floor.wing.building.geographyL4.latitude,n.unit.floor.wing.building.geographyL4.longitude) from NVIBUnitResult n  where n.unit.floor.wing.building.geographyL4.id in (select g.id from GeographyL4 g where g.latitude>=:SWLat and g.latitude<=:NELat and g.longitude>=:SWLng and g.longitude<=:NELng) and n.technology=:technology and n.unit.floor.wing.building.buildingType=:buildingType group by n.unit.floor.wing.building.geographyL4.name,n.unit.floor.wing.building.geographyL4.latitude,n.unit.floor.wing.building.geographyL4.longitude")
	@NamedQuery(name = InBuildingConstants.GET_BUILDING_BY_VIEWPORT, query = "select new com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper(n.unit.floor.wing.building.id,n.unit.floor.wing.building.buildingName,n.unit.floor.wing.building.latitude,n.unit.floor.wing.building.longitude,sum(totalRsrp)/sum(countRsrp),sum(totalWifiRssi)/sum(countWifiRssi),n.unit.floor.wing.building.address,n.unit.floor.wing.building.buildingType) from NVIBUnitResult n where n.unit.floor.wing.building.latitude>=:SWLat and  n.unit.floor.wing.building.latitude<=:NELat and n.unit.floor.wing.building.longitude>=:SWLng and n.unit.floor.wing.building.longitude<=:NELng and n.technology=:technology and n.unit.floor.wing.building.buildingType=:buildingType group by n.unit.floor.wing.building.id,n.unit.floor.wing.building.buildingName,n.unit.floor.wing.building.latitude,n.unit.floor.wing.building.longitude")

	@NamedQuery(name = InBuildingConstants.GET_WING_BY_BUILDING, query = "select distinct new com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper(n.unit.floor.wing.id,n.unit.floor.wing.wingName) from NVIBUnitResult n where n.unit.floor.wing.building.id = :buildingId and n.technology=:technology")
	@NamedQuery(name = InBuildingConstants.GET_FLOOR_BY_WING, query = "select new com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper(n.unit.floor.id,n.unit.floor.floorName,sum(totalRsrp)/sum(countRsrp),sum(totalSinr)/sum(countSinr),sum(totalUl)/sum(countUl),sum(totalDl)/sum(countDl),sum(totalWifiRssi)/sum(countWifiRssi),sum(totalWifiSnr)/sum(countWifiSnr)) from NVIBUnitResult n where n.unit.floor.wing.id = :wingId  and n.technology=:technology group by n.unit.floor.id,n.unit.floor.floorName")
	@NamedQuery(name = InBuildingConstants.GET_UNIT_BY_FLOOR, query = "select new com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper(n.unit.id,n.unit.unitName,sum(totalRsrp)/sum(countRsrp),sum(totalSinr)/sum(countSinr),sum(totalUl)/sum(countUl),sum(totalDl)/sum(countDl),sum(totalWifiRssi)/sum(countWifiRssi),sum(totalWifiSnr)/sum(countWifiSnr)) from NVIBUnitResult n where n.unit.floor.id = :floorId and n.technology=:technology group by n.unit.id,n.unit.unitName")
	@NamedQuery(name = InBuildingConstants.GET_RECIPE_FILE_NAME_BY_UNIT, query = "select distinct new com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper(n.woRecipeMapping.id,concat(concat(concat(concat(concat(n.woRecipeMapping.recipe.name,'_',n.woRecipeMapping.id),'_',n.woRecipeMapping.genericWorkorder.createdBy.firstName),'_',n.woRecipeMapping.genericWorkorder.createdBy.lastName),'_',n.operator),'_',n.woRecipeMapping.recipe.creationTime),n.operator,n.woRecipeMapping.genericWorkorder.id) from NVIBUnitResult n where n.unit.id = :unitId and n.technology=:technology")
	@NamedQuery(name = InBuildingConstants.GET_IB_UNIT_RESULT_FOR_RECIPE, query = "select new com.inn.foresight.module.nv.report.wrapper.inbuilding.WalkTestSummaryWrapper(n.totalRsrp, n.countRsrp, n.countRsrpGreaterThan100Dbm, n.totalSinr, n.countSinr, n.countSinrGreaterThan12Db, n.totalDl, n.countDl, n.totalUl, n.countUl, n.pciStrongest, n.totalCqi, n.countCqi, n.totalMimo, n.countMimo, n.rrcInitiate, n.rrcSuccess, n.erabDrop, n.erabSuccess, n.handOverSuccess, n.handOverInitiate, n.countTDD, n.countFDD, n.unit.floor.wing.building.buildingName, n.unit.floor.wing.wingName, n.unit.floor.floorName, n.unit.unitName, n.woRecipeMapping.recipe.id, n.woRecipeMapping.recipe.name, n.woRecipeMapping.id,n.creationTime, n.countMos,n.totalMos,n.callInitiateCount,n.callSetupSuccessCount,n.callDropCount,n.totalPuschUl,n.countPuschUl,n.totalPdschDl,n.countPdschDl) from NVIBUnitResult n where n.woRecipeMapping.id IN (:woRecipeMappingId)")
	@NamedQuery(name = "getRecordByFloorIdAndTemplateType",query="select n from NVIBUnitResult n where n.unit.floor.id=:floorId and n.unit.floor.wing.building.id= :buildingId and n.woRecipeMapping.genericWorkorder.templateType in (:templateType) and n.operator is not null")
  
	@NamedQuery(name = "getUnitWiseWorkorder", query = "select distinct(n.woRecipeMapping.genericWorkorder) from NVIBUnitResult n where n.unit.id = :unitId and n.technology = :technology and n.woRecipeMapping.genericWorkorder.status in ('COMPLETED')")
	@NamedQuery(name = "getUnitWiseWorkorderCount", query = "select count(distinct n.woRecipeMapping.genericWorkorder.id) from NVIBUnitResult n where n.unit.id = :unitId and n.technology = :technology and n.woRecipeMapping.genericWorkorder.status in ('COMPLETED')")
	@NamedQuery(name = "getBuildingInFoByWoId", query = "select new com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper(n.unit.floor.wing.building.id,n.unit.floor.wing.building.buildingName,n.unit.floor.wing.building.buildingType,n.unit.floor.wing.building.address,n.unit.floor.wing.building.latitude,n.unit.floor.wing.building.longitude,count(n.unit.floor.wing),count(n.unit.floor),count(n.unit),sum(n.totalRsrp),sum(n.countRsrp))from NVIBUnitResult n  where n.woRecipeMapping.genericWorkorder.id=:woId")
	@NamedQuery(name = "getNVIbResultByRecipeId", query="select n from NVIBUnitResult n where n.woRecipeMapping.id=:woRecipeMappingId")
	@NamedQuery(name = "getNVIbResultByBuildingId", query="select new com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper(sum(totalRsrp)/sum(countRsrp),sum(totalSinr)/sum(countSinr),sum(totalUl)/sum(countUl),sum(totalDl)/sum(countDl),sum(totalWifiRssi)/sum(countWifiRssi),sum(totalWifiSnr)/sum(countWifiSnr),n.band) from NVIBUnitResult n where n.unit.floor.wing.building.id = :buildingId and (:floorId is null or n.unit.floor.id =:floorId ) group by n.band")
	@NamedQuery(name = "getKpiAvgByFloorId", query="select new com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper(sum(totalRsrp)/sum(countRsrp),sum(totalSinr)/sum(countSinr),sum(totalUl)/sum(countUl),sum(totalDl)/sum(countDl),sum(totalWifiRssi)/sum(countWifiRssi),sum(totalWifiSnr)/sum(countWifiSnr)) from NVIBUnitResult n where n.unit.floor.id = :floorId   group by n.unit.floor.id")
	@NamedQuery(name = "getDateWiseTestResultByBuildingId", query="select new com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper(sum(totalRsrp)/sum(countRsrp),sum(totalSinr)/sum(countSinr),sum(totalUl)/sum(countUl),sum(totalDl)/sum(countDl),sum(totalWifiRssi)/sum(countWifiRssi),sum(totalWifiSnr)/sum(countWifiSnr),n.band,DATE_FORMAT(creationTime,'%d-%m-%y')) from NVIBUnitResult n where n.unit.floor.wing.building.id =:buildingId and (:floorId is null or n.unit.floor.id =:floorId ) group by n.band ,DATE_FORMAT(creationTime,'%d-%m-%y') order by n.creationTime desc")

	
	// adding filterlist
	@FilterDef(name = "dateRangeFilter", parameters = {
			@ParamDef(name = "startDate", type = "java.util.Date"),@ParamDef(name = "endDate", type = "java.util.Date") })
	
	@FilterDef(name = "bandFilter", parameters = {
			@ParamDef(name = "band", type = "java.lang.Integer") })
	
	@Filter(name = "dateRangeFilter", condition = " DATE_FORMAT(creationtime,'%d-%m-%y') between  DATE_FORMAT(:startDate,'%d-%m-%y') and  DATE_FORMAT(:endDate,'%d-%m-%y')")
	
	@Filter(name = "bandFilter", condition = " band = (:band)")

@Entity
@XmlRootElement(name = "NVIBUnitResult")
@Table(name = "NVIBUnitResult")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NVIBUnitResult implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "nvibunitresultid_pk")
	private Integer id;

	/** The unit. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unitid_fk", nullable = true)
	private Unit unit;

	/** The technology. */
	@Column(name = "technology")
	private String technology;

	/** The operator. */
	@Column(name = "operator")
	private String operator;

	/** The creation time. */
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modification time. */
	@Column(name = "modificationtime")
	private Date modificationTime;

	/** The total dl. */
	
	@Column(name = "totaldl")
	private Double totalDl;

	/** The count dl. */
	@Column(name = "countdl")
	private Integer countDl;

	/** The total ul. */
	@Column(name = "totalul")
	private Double totalUl;

	/** The count ul. */
	@Column(name = "countul")
	private Integer countUl;

	/** The total sinr. */
	@Column(name = "totalsinr")
	private Double totalSinr;

	/** The count sinr. */
	@Column(name = "countsinr")
	private Integer countSinr;

	/** The total rssi. */
	@Column(name = "totalrssi")
	private Double totalRssi;

	/** The count rssi. */
	@Column(name = "countrssi")
	private Integer countRssi;

	/** The total rsrp. */
	@Column(name = "totalrsrp")
	private Double totalRsrp;

	/** The count rsrp. */
	@Column(name = "countrsrp")
	private Integer countRsrp;

	/** The total cqi. */
	@Column(name = "totalcqi")
	private Double totalCqi;

	/** The count cqi. */
	@Column(name = "countcqi")
	private Integer countCqi;
	
	/** The total mimo. */
	@Column(name = "totalmimo")
	private Integer totalMimo;

	/** The count mimo. */
	@Column(name = "countmimo")
	private Integer countMimo;

	/** The count TDD. */
	@Column(name = "counttdd")
	private Integer countTDD;

	/** The count FDD. */
	@Column(name = "countfdd")
	private Integer countFDD;

	/** The hand over initiate. */
	@Column(name = "handoverinitiate")
	private Integer handOverInitiate;

	/** The hand over success. */
	@Column(name = "handoversuccess")
	private Integer handOverSuccess;

	/** The erab drop. */
	@Column(name = "erabdrop")
	private Integer erabDrop;

	/** The erab success. */
	@Column(name = "erabsuccess")
	private Integer erabSuccess;

	/** The rrc initiate. */
	@Column(name = "rrcinitiate")
	private Integer rrcInitiate;

	/** The rrc success. */
	@Column(name = "rrcsuccess")
	private Integer rrcSuccess;

	/** The wo recipe mapping. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "worecipemappingid_fk", nullable = true)
	private WORecipeMapping woRecipeMapping;

	/** The count rsrp greater than 100 dbm. */
	@Column(name = "countrsrpgreaterthan100dbm")
	private Integer countRsrpGreaterThan100Dbm;
	
	/** The count sinr greater than 12 db. */
	@Column(name = "countsinrgreaterthan12db")
	private Integer countSinrGreaterThan12Db;

	/** The count dl greater than 5 mbps. */
	@Column(name = "countdlgreaterthan5mbps")
	private Integer countDlGreaterThan5Mbps;

	/** The pci. */
	@Column(name = "pci")
	private String pci;

	/** The pci strongest. */
	
	@Column(name = "pcistrongest")
	private Integer pciStrongest;

	/** The pci count. */
	
	@Column(name = "pcicount")
	private Integer pciCount;

	/** The cell id. */
	@Column(name = "cellid")
	private String cellId;
	
	/** The cell id strongest. */
	
	@Column(name = "cellIdstrongest")
	private Integer cellIdStrongest;

	/** The cell id count. */
	
	@Column(name = "cellidcount")
	private Integer cellIdCount;

	/** The e node BID. */
	
	@Column(name = "enodebid")
	private Integer eNodeBID;

	/** The sector id. */
	@Column(name = "sectorid")
	private String sectorId;
	
	/** The total wifi rssi. */
	@Column(name = "totalwifirssi")
	private Double totalWifiRssi;

	/** The count wifi rssi. */
	@Column(name = "countwifirssi")
	private Integer countWifiRssi;
	
	/** The total wifi snr. */
	@Column(name = "totalwifisnr")
	private Double totalWifiSnr;

	/** The count wifi snr. */
	
	@Column(name = "countwifisnr")
	private Integer countWifiSnr;
	
	/** The cntrssigrtthn 90 dbm. */
	
	@Column(name = "cntrssigrtthn90dbm")
	private Integer cntrssigrtthn90dbm;

	/** The cntsnrgrtthn 25 dbm. */
	
	@Column(name = "cntsnrgrtthn25dbm")
	private Integer cntsnrgrtthn25dbm;

	/** The wifi SSID. */
	@Column(name = "wifissid")
	private String wifiSSID;
	
	@Column(name = "assignto")
	private String assignto;


	
	@Column(name = "countmos")
	private Integer countMos;

	@Column(name = "totalmos")
	private Double totalMos;	

	
	@Column(name = "callinitiatecount")
	private Integer callInitiateCount;

	
	@Column(name = "callsetupsuccesscount")
	private Integer callSetupSuccessCount;

	
	@Column(name = "calldropcount")
	private Integer callDropCount;

	
	@Column(name = "callfailurecount")
	private Integer callFailureCount;

	
	@Column(name = "callsuccesscount")
	private Integer callSuccessCount;

	
	@Column(name = "countpuschul")
	private Integer countPuschUl;

	
	@Column(name = "totalpuschul")
	private Double totalPuschUl;		

	
	@Column(name = "countpdschdl")
	private Integer countPdschDl;

	
	@Column(name = "totalpdschdl")
	private Double totalPdschDl;	

	@Column(name = "band")
	private Integer band;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
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

	public Double getTotalDl() {
		return totalDl;
	}

	public void setTotalDl(Double totalDl) {
		this.totalDl = totalDl;
	}

	public Integer getCountDl() {
		return countDl;
	}

	public void setCountDl(Integer countDl) {
		this.countDl = countDl;
	}

	public Double getTotalUl() {
		return totalUl;
	}

	public void setTotalUl(Double totalUl) {
		this.totalUl = totalUl;
	}

	public Integer getCountUl() {
		return countUl;
	}

	public void setCountUl(Integer countUl) {
		this.countUl = countUl;
	}

	public Double getTotalSinr() {
		return totalSinr;
	}

	public void setTotalSinr(Double totalSinr) {
		this.totalSinr = totalSinr;
	}

	public Integer getCountSinr() {
		return countSinr;
	}

	public void setCountSinr(Integer countSinr) {
		this.countSinr = countSinr;
	}

	public Double getTotalRssi() {
		return totalRssi;
	}

	public void setTotalRssi(Double totalRssi) {
		this.totalRssi = totalRssi;
	}

	public Integer getCountRssi() {
		return countRssi;
	}

	public void setCountRssi(Integer countRssi) {
		this.countRssi = countRssi;
	}

	public Double getTotalRsrp() {
		return totalRsrp;
	}

	public void setTotalRsrp(Double totalRsrp) {
		this.totalRsrp = totalRsrp;
	}

	public Integer getCountRsrp() {
		return countRsrp;
	}

	public void setCountRsrp(Integer countRsrp) {
		this.countRsrp = countRsrp;
	}

	public Double getTotalCqi() {
		return totalCqi;
	}

	public void setTotalCqi(Double totalCqi) {
		this.totalCqi = totalCqi;
	}

	public Integer getCountCqi() {
		return countCqi;
	}

	public void setCountCqi(Integer countCqi) {
		this.countCqi = countCqi;
	}

	public Integer getTotalMimo() {
		return totalMimo;
	}

	public void setTotalMimo(Integer totalMimo) {
		this.totalMimo = totalMimo;
	}

	public Integer getCountMimo() {
		return countMimo;
	}

	public void setCountMimo(Integer countMimo) {
		this.countMimo = countMimo;
	}

	public Integer getCountTDD() {
		return countTDD;
	}

	public void setCountTDD(Integer countTDD) {
		this.countTDD = countTDD;
	}

	public Integer getCountFDD() {
		return countFDD;
	}

	public void setCountFDD(Integer countFDD) {
		this.countFDD = countFDD;
	}

	public Integer getHandOverInitiate() {
		return handOverInitiate;
	}

	public void setHandOverInitiate(Integer handOverInitiate) {
		this.handOverInitiate = handOverInitiate;
	}

	public Integer getHandOverSuccess() {
		return handOverSuccess;
	}

	public void setHandOverSuccess(Integer handOverSuccess) {
		this.handOverSuccess = handOverSuccess;
	}

	public Integer getErabDrop() {
		return erabDrop;
	}

	public void setErabDrop(Integer erabDrop) {
		this.erabDrop = erabDrop;
	}

	public Integer getErabSuccess() {
		return erabSuccess;
	}

	public void setErabSuccess(Integer erabSuccess) {
		this.erabSuccess = erabSuccess;
	}

	public Integer getRrcInitiate() {
		return rrcInitiate;
	}

	public void setRrcInitiate(Integer rrcInitiate) {
		this.rrcInitiate = rrcInitiate;
	}

	public Integer getRrcSuccess() {
		return rrcSuccess;
	}

	public void setRrcSuccess(Integer rrcSuccess) {
		this.rrcSuccess = rrcSuccess;
	}

	public WORecipeMapping getWoRecipeMapping() {
		return woRecipeMapping;
	}

	public void setWoRecipeMapping(WORecipeMapping woRecipeMapping) {
		this.woRecipeMapping = woRecipeMapping;
	}

	public Integer getCountRsrpGreaterThan100Dbm() {
		return countRsrpGreaterThan100Dbm;
	}

	public void setCountRsrpGreaterThan100Dbm(Integer countRsrpGreaterThan100Dbm) {
		this.countRsrpGreaterThan100Dbm = countRsrpGreaterThan100Dbm;
	}

	public Integer getCountSinrGreaterThan12Db() {
		return countSinrGreaterThan12Db;
	}

	public void setCountSinrGreaterThan12Db(Integer countSinrGreaterThan12Db) {
		this.countSinrGreaterThan12Db = countSinrGreaterThan12Db;
	}

	public Integer getCountDlGreaterThan5Mbps() {
		return countDlGreaterThan5Mbps;
	}

	public void setCountDlGreaterThan5Mbps(Integer countDlGreaterThan5Mbps) {
		this.countDlGreaterThan5Mbps = countDlGreaterThan5Mbps;
	}

	public String getPci() {
		return pci;
	}

	public void setPci(String pci) {
		this.pci = pci;
	}

	public Integer getPciStrongest() {
		return pciStrongest;
	}

	public void setPciStrongest(Integer pciStrongest) {
		this.pciStrongest = pciStrongest;
	}

	public Integer getPciCount() {
		return pciCount;
	}

	public void setPciCount(Integer pciCount) {
		this.pciCount = pciCount;
	}

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public Integer getCellIdStrongest() {
		return cellIdStrongest;
	}

	public void setCellIdStrongest(Integer cellIdStrongest) {
		this.cellIdStrongest = cellIdStrongest;
	}

	public Integer getCellIdCount() {
		return cellIdCount;
	}

	public void setCellIdCount(Integer cellIdCount) {
		this.cellIdCount = cellIdCount;
	}

	public Integer geteNodeBID() {
		return eNodeBID;
	}

	public void seteNodeBID(Integer eNodeBID) {
		this.eNodeBID = eNodeBID;
	}

	public String getSectorId() {
		return sectorId;
	}

	public void setSectorId(String sectorId) {
		this.sectorId = sectorId;
	}

	public Double getTotalWifiRssi() {
		return totalWifiRssi;
	}

	public void setTotalWifiRssi(Double totalWifiRssi) {
		this.totalWifiRssi = totalWifiRssi;
	}

	public Integer getCountWifiRssi() {
		return countWifiRssi;
	}

	public void setCountWifiRssi(Integer countWifiRssi) {
		this.countWifiRssi = countWifiRssi;
	}

	public Double getTotalWifiSnr() {
		return totalWifiSnr;
	}

	public void setTotalWifiSnr(Double totalWifiSnr) {
		this.totalWifiSnr = totalWifiSnr;
	}

	public Integer getCountWifiSnr() {
		return countWifiSnr;
	}

	public void setCountWifiSnr(Integer countWifiSnr) {
		this.countWifiSnr = countWifiSnr;
	}

	public Integer getCntrssigrtthn90dbm() {
		return cntrssigrtthn90dbm;
	}

	public void setCntrssigrtthn90dbm(Integer cntrssigrtthn90dbm) {
		this.cntrssigrtthn90dbm = cntrssigrtthn90dbm;
	}

	public Integer getCntsnrgrtthn25dbm() {
		return cntsnrgrtthn25dbm;
	}

	public void setCntsnrgrtthn25dbm(Integer cntsnrgrtthn25dbm) {
		this.cntsnrgrtthn25dbm = cntsnrgrtthn25dbm;
	}

	public String getWifiSSID() {
		return wifiSSID;
	}

	public void setWifiSSID(String wifiSSID) {
		this.wifiSSID = wifiSSID;
	}

	public String getAssignto() {
		return assignto;
	}

	public void setAssignto(String assignto) {
		this.assignto = assignto;
	}

	public Integer getCountMos() {
		return countMos;
	}

	public void setCountMos(Integer countMos) {
		this.countMos = countMos;
	}

	public Double getTotalMos() {
		return totalMos;
	}

	public void setTotalMos(Double totalMos) {
		this.totalMos = totalMos;
	}

	public Integer getCallInitiateCount() {
		return callInitiateCount;
	}

	public void setCallInitiateCount(Integer callInitiateCount) {
		this.callInitiateCount = callInitiateCount;
	}

	public Integer getCallSetupSuccessCount() {
		return callSetupSuccessCount;
	}

	public void setCallSetupSuccessCount(Integer callSetupSuccessCount) {
		this.callSetupSuccessCount = callSetupSuccessCount;
	}

	public Integer getCallDropCount() {
		return callDropCount;
	}

	public void setCallDropCount(Integer callDropCount) {
		this.callDropCount = callDropCount;
	}

	public Integer getCallFailureCount() {
		return callFailureCount;
	}

	public void setCallFailureCount(Integer callFailureCount) {
		this.callFailureCount = callFailureCount;
	}

	public Integer getCallSuccessCount() {
		return callSuccessCount;
	}

	public void setCallSuccessCount(Integer callSuccessCount) {
		this.callSuccessCount = callSuccessCount;
	}

	public Integer getCountPuschUl() {
		return countPuschUl;
	}

	public void setCountPuschUl(Integer countPuschUl) {
		this.countPuschUl = countPuschUl;
	}

	public Double getTotalPuschUl() {
		return totalPuschUl;
	}

	public void setTotalPuschUl(Double totalPuschUl) {
		this.totalPuschUl = totalPuschUl;
	}

	public Integer getCountPdschDl() {
		return countPdschDl;
	}

	public void setCountPdschDl(Integer countPdschDl) {
		this.countPdschDl = countPdschDl;
	}

	public Double getTotalPdschDl() {
		return totalPdschDl;
	}

	public void setTotalPdschDl(Double totalPdschDl) {
		this.totalPdschDl = totalPdschDl;
	}

	public Integer getBand() {
		return band;
	}

	public void setBand(Integer band) {
		this.band = band;
	}

	@Override
	public String toString() {
		return "NVIBUnitResult [id=" + id + ", unit=" + unit + ", technology=" + technology + ", operator=" + operator
				+ ", creationTime=" + creationTime + ", modificationTime=" + modificationTime + ", totalDl=" + totalDl
				+ ", countDl=" + countDl + ", totalUl=" + totalUl + ", countUl=" + countUl + ", totalSinr=" + totalSinr
				+ ", countSinr=" + countSinr + ", totalRssi=" + totalRssi + ", countRssi=" + countRssi + ", totalRsrp="
				+ totalRsrp + ", countRsrp=" + countRsrp + ", totalCqi=" + totalCqi + ", countCqi=" + countCqi
				+ ", totalMimo=" + totalMimo + ", countMimo=" + countMimo + ", countTDD=" + countTDD + ", countFDD="
				+ countFDD + ", handOverInitiate=" + handOverInitiate + ", handOverSuccess=" + handOverSuccess
				+ ", erabDrop=" + erabDrop + ", erabSuccess=" + erabSuccess + ", rrcInitiate=" + rrcInitiate
				+ ", rrcSuccess=" + rrcSuccess + ", woRecipeMapping=" + woRecipeMapping
				+ ", countRsrpGreaterThan100Dbm=" + countRsrpGreaterThan100Dbm + ", countSinrGreaterThan12Db="
				+ countSinrGreaterThan12Db + ", countDlGreaterThan5Mbps=" + countDlGreaterThan5Mbps + ", pci=" + pci
				+ ", pciStrongest=" + pciStrongest + ", pciCount=" + pciCount + ", cellId=" + cellId
				+ ", cellIdStrongest=" + cellIdStrongest + ", cellIdCount=" + cellIdCount + ", eNodeBID=" + eNodeBID
				+ ", sectorId=" + sectorId + ", totalWifiRssi=" + totalWifiRssi + ", countWifiRssi=" + countWifiRssi
				+ ", totalWifiSnr=" + totalWifiSnr + ", countWifiSnr=" + countWifiSnr + ", cntrssigrtthn90dbm="
				+ cntrssigrtthn90dbm + ", cntsnrgrtthn25dbm=" + cntsnrgrtthn25dbm + ", wifiSSID=" + wifiSSID
				+ ", assignto=" + assignto + ", countMos=" + countMos + ", totalMos=" + totalMos
				+ ", callInitiateCount=" + callInitiateCount + ", callSetupSuccessCount=" + callSetupSuccessCount
				+ ", callDropCount=" + callDropCount + ", callFailureCount=" + callFailureCount + ", callSuccessCount="
				+ callSuccessCount + ", countPuschUl=" + countPuschUl + ", totalPuschUl=" + totalPuschUl
				+ ", countPdschDl=" + countPdschDl + ", totalPdschDl=" + totalPdschDl + ", band=" + band + "]";
	}

}