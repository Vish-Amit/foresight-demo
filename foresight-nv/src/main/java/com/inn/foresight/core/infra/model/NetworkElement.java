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
import com.inn.foresight.core.infra.utils.enums.DayOneStatus;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.utils.enums.ProgressState;
import com.inn.foresight.core.infra.utils.enums.SiteType;
import com.inn.foresight.core.infra.utils.enums.SmallCellStatus;
import com.inn.foresight.core.infra.utils.enums.Technology;
import com.inn.foresight.core.infra.utils.enums.Vendor;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.geography.model.SalesL4;

/**
 * The Class NetworkElement.
 * 
 * 
 * 
 */

@NamedQueries({
		@NamedQuery(name = "getCellsBySite", query = "select ne from NetworkElement ne where ne.networkElement in ( select id from NetworkElement n where n.neName =:neName ) and ne.domain = 'RAN'"),
		@NamedQuery(name = "getNeIdByNeName", query = "select id from NetworkElement where neId=:neId"),
		@NamedQuery(name = "getNEDetailByNEIds", query = "select n from NetworkElement n LEFT JOIN FETCH n.networkElement where upper(n.neId) in (:neId) AND n.isDeleted=0"),
		@NamedQuery(name = "getNEDetailByNEIdAndDomainVendor", query = "select new com.inn.foresight.core.infra.wrapper.MicroSiteDataWrapper(n.neId,n.geographyL4.geographyL3.name) from NetworkElement n where upper(n.neId) in (:neId) and UPPER(n.domain) =:domain and UPPER(n.vendor) =:vendor"),
		@NamedQuery(name = "getOnAirNetworkElement", query = "select n from NetworkElement n JOIN FETCH n.geographyL4 where n.neType in ('ODSC_SITE','MACRO','MACRO_CELL') and n.neStatus = 'ONAIR'"),
		@NamedQuery(name = "getNeIdByDomainAndVendor", query = "select n.neId from  NetworkElement n where n.domain =:domain and n.vendor =:vendor "),
		@NamedQuery(name = "getNEByParentId", query = "select ne from NetworkElement ne where ne.parentNE.id=:pid"),
		@NamedQuery(name = "getAllChildNEDetailByNEId", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementCellDetailWrapper(n.neId , n.neFrequency, n.vendor) from NetworkElement n where n.neId like (:neId) "),
		@NamedQuery(name = "getNEDetailBySapIdSec", query = "select  new com.inn.foresight.core.infra.wrapper.MicroSiteDataWrapper(n.neFrequency,n.neId,n.cellNum,m.neBandDetail.carrier)"
				+ " from NetworkElement n inner join RANDetail m on n.id = m.networkElement "
				+ " where n.neName like (:neName) and m.sector=:sectorId and UPPER(n.domain) =:domain and UPPER(n.vendor) =:vendor "),
		@NamedQuery(name = "getNEDetailBySapId", query = "select  new com.inn.foresight.core.infra.wrapper.MicroSiteDataWrapper(n.neFrequency,n.neId,n.cellNum,m.neBandDetail.carrier, m.sector)"
				+ " from NetworkElement n inner join RANDetail m on n.id = m.networkElement "
				+ " where n.neName like (:neName) and m.sector is not null and UPPER(n.domain) =:domain and UPPER(n.vendor) =:vendor "),

		// @NamedQuery(name = "getNEDetailBySapId", query = "select new
		// com.inn.foresight.core.infra.wrapper.MicroSiteDataWrapper(n.id,n.neName,m.cellId,m.cellName,n.neFrequency,m.sector,m.enbId,n.neType)
		// from NetworkElement n inner join MacroSiteDetail m where n.neName =:sapId and
		// n.networkElement.id is not
		// null"),

		@NamedQuery(name = "getNEBySapId", query = "select n from NetworkElement n where upper(n.neName)=:sapId and upper(n.domain)=:domain and upper(n.vendor)=:vendor and upper(n.technology)=:technology"),
		@NamedQuery(name = "getDistinctState", query = "select distinct n.geographyL4.geographyL3.geographyL2.name from NetworkElement n where n.geographyL4.geographyL3.geographyL2.geographyL1.name=:geographyL1 and n.geographyL4.geographyL3.geographyL2.name is not null and n.networkElement is null and n.neType='MACRO' order by  n.geographyL4.geographyL3.geographyL2.name "),
		@NamedQuery(name = "getDistinctCityByState", query = "select distinct n.geographyL4.geographyL3.name from NetworkElement n where n.geographyL4.geographyL3.geographyL2.name=:state and n.geographyL4.geographyL3.name is not null and  n.isDeleted=false  and n.networkElement is null  and n.neType='MACRO'  order by  n.geographyL4.geographyL3.name"),
		@NamedQuery(name = "getDistinctClusterByCity", query = "select distinct n.geographyL4.name from NetworkElement n where  n.geographyL4.geographyL3.name =:city and n.isDeleted=false and n.geographyL4.name is not null  and n.networkElement is null  and n.neType='MACRO'  order by n.geographyL4.name"),
		@NamedQuery(name = "getNEForIsDeletedFalse", query = "select n from NetworkElement n where n.isDeleted=false"),
		@NamedQuery(name = "searchSiteByName", query = "select n from NetworkElement n where n.neName=:neName  and n.networkElement is null"),
		@NamedQuery(name = "searchSiteByFriendlyName", query = "select n from NetworkElement n where n.friendlyname=:friendlyname  and n.networkElement is null and n.neType=:netype and n.latitude is not null and n.longitude is not null and n.isDeleted=0"),
		@NamedQuery(name = "getAllSitesCount", query = "select count(distinct n.neName) from NetworkElement n where n.neName like :siteName "),
		@NamedQuery(name = "getAllSitesCountByFilter", query = "select count(distinct n.neName) from NetworkElement n JOIN MacroSiteDetail m ON m.networkElement.id=n.id where n.neName like :siteName"),
		@NamedQuery(name = "getSapIDCnumBYGL1AndBand", query = "select n.neId from NetworkElement n where n.geographyL4.geographyL3.geographyL2.geographyL1.name=:geographyLevelName"),
		@NamedQuery(name = "getSapIDCnumBYGL2AndBand", query = "select n.neId from NetworkElement n where n.geographyL4.geographyL3.geographyL2.name=:geographyLevelName"),
		@NamedQuery(name = "getSapIDCnumBYGL3AndBand", query = "select n.neId from NetworkElement n where n.geographyL4.geographyL3.name=:geographyLevelName"),
		@NamedQuery(name = "getSapIDCnumBYGL4AndBand", query = "select n.neId from NetworkElement n where n.geographyL4.name=:geographyLevelName"),
		@NamedQuery(name = "getNetworkElementBySapid", query = "select n from NetworkElement n where n.neName in (:sapId) and n.isDeleted = false "),
		@NamedQuery(name = "getNetworkElementBySapIdList", query = "select n from NetworkElement n where n.neName in (:sapId) and n.isDeleted = false "),
		@NamedQuery(name = "getSiteTableDataForGeographyL2", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(ne.geographyL4.geographyL3.geographyL2.name,count(ne.id),ne.modifiedTime) from NetworkElement ne where ne.networkElement is null and ne.latitude >= :southWestLat and ne.latitude <= :northEastLat and ne.longitude >=:southWestLong and ne.longitude <=:northEastLong and ne.neStatus =:progressState and ne.neType =:neType group by ne.geographyL4.geographyL3.geographyL2.name,ne.modifiedTime"),
		@NamedQuery(name = "getSiteTableDataForGeographyL3", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(ne.geographyL4.geographyL3.name,count(ne.id),ne.modifiedTime) from NetworkElement ne where ne.networkElement is null and ne.latitude >= :southWestLat and ne.latitude <= :northEastLat and ne.longitude >=:southWestLong and ne.longitude <=:northEastLong and ne.neStatus =:progressState and ne.neType =:neType group by ne.geographyL4.geographyL3.name,ne.modifiedTime"),
		@NamedQuery(name = "getDistinctVersion", query = "select distinct n.softwareVersion from NetworkElement n where UPPER(n.domain)=:domain and UPPER(n.vendor)=:vendor and n.softwareVersion is not null and n.networkElement is not null"),
		@NamedQuery(name = "getDistinctDomain", query = "select distinct n.domain from NetworkElement n where n.domain is not null"),
		@NamedQuery(name = "getDistinctInfoFromNetworkElement", query = "select distinct new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.domain,n.vendor,n.technology) from NetworkElement n where n.domain is not null and n.vendor is not null and n.technology is not null and n.neType='MACRO' or n.neType = 'SMALL_CELL_OUTDOOR' "),
		@NamedQuery(name = "getDistinctVendorByDomains", query = "select distinct n.vendor from NetworkElement n where UPPER(n.domain) in (:domain) and n.vendor is not null "),
		@NamedQuery(name = "getDistinctVendorByDomain", query = "select distinct n.vendor from NetworkElement n where UPPER(n.domain)=:domain and n.vendor is not null "),
		@NamedQuery(name = "getNEByNameDomainVendorTech", query = "select n from NetworkElement n where n.neName in(:neName) and n.networkElement is null and upper(n.domain)=:domain and upper(n.vendor)=:vendor and upper(n.technology)=:technology "),
		@NamedQuery(name = "getDistinctSWVersionFromNetworkElement", query = "select distinct (n.softwareVersion) from NetworkElement n where n.softwareVersion is not null and n.isDeleted=false"),
		@NamedQuery(name = "getDistinctNETypeFromNetworkElement", query = "select distinct (n.neType) from NetworkElement n where n.neType is not null and n.isDeleted=false"),
		@NamedQuery(name = "getNetworkelementForMacro", query = "select n.id,n.neName from NetworkElement n where n.networkElement is null and n.isDeleted=false and neStatus=:neStatus"),
		@NamedQuery(name = "getNetworkelementForSmallCell", query = "select n.id,n.neName from NetworkElement n where n.isDeleted=false and neStatus=:neStatus"),
		@NamedQuery(name = "getDistinctVendorFromNetworkElement", query = "select distinct (n.vendor) from NetworkElement n where n.vendor is not null and n.isDeleted=false"),
		@NamedQuery(name = "getNEInfoByNeId", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.id,n.neId,n.domain,n.vendor,n.softwareVersion,n.neType,n.geographyL4.geographyL3.geographyL2.id,n.mnc,n.mcc,n.emsServer.emsname) from NetworkElement n where n.neStatus ='ONAIR' and n.neId=:neId"),
		@NamedQuery(name = "getDistinctDomainFromNetworkElement", query = "select distinct n.domain from NetworkElement n where n.domain is not null and n.isDeleted=false"),
		@NamedQuery(name = "getDistinctTechnologyFromNetworkElement", query = "select distinct n.technology from NetworkElement n where n.technology is not null"),
		@NamedQuery(name = "getNetworkElementByNEId", query = "select n from NetworkElement n where upper(n.neId) =:neId or upper(n.neName) =:neId and n.isDeleted=0"),
		@NamedQuery(name = "getNetworkElementByNEType", query = "select n.id,n.neId,n.neName,n.pmEmsId from NetworkElement n where n.isDeleted=0 and n.domain=:domain and n.vendor=:vendor and n.neType=:neType"),
		@NamedQuery(name = "getNetworkElementDataForNE", query = "select ne from NEBandDetail ne  "
				+ "where upper(ne.networkElement.neName)=:neName and ne.networkElement.neType=:neType and ne.networkElement.isDeleted=0"),
		@NamedQuery(name = "searchAllNetworkElement", query = "select n.neId,n.neFrequency,n.vendor,n.neType,n.neName,n.domain,n.technology from NetworkElement n where upper(n.neName) like :neName and upper(n.vendor)=:vendor"),
		@NamedQuery(name = "getNetworkSiteInfo", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.neId,n.neName,n.geographyL4.name,n.geographyL4.geographyL3.name,n.geographyL4.geographyL3.geographyL2.name,n.neType) from NetworkElement n where upper(n.neName) like :neName"),
		@NamedQuery(name = "getNeFrequencyByDomainAndVendor", query = "select distinct n.neFrequency from NetworkElement n where upper(n.domain)=:domain and upper(n.vendor)=:vendor and n.neFrequency is not null "),
		@NamedQuery(name = "getNEDetailByNEId", query = "select n from NetworkElement n where upper(n.neId) =:neId"),
		@NamedQuery(name = "getNETaskDetailForNE", query = "select n from NETaskDetail n where upper(n.neBandDetail.networkElement.neName)=:neName and n.neBandDetail.networkElement.neType=:neType order by n.neBandDetail.neFrequency,n.executionOrder asc"),
		@NamedQuery(name = "getAllSiteCountZoneWise", query = "SELECT new com.inn.foresight.core.infra.wrapper.TotalSiteLayerWiseWrapper(ne.geographyL4.geographyL3.geographyL2.geographyL1.name, COUNT(DISTINCT ne.neName)) FROM NetworkElement ne  where ne.isDeleted = 0 AND ne.networkElement IS NULL AND ne.geographyL4.geographyL3.geographyL2.geographyL1.name != NULL group by ne.geographyL4.geographyL3.geographyL2.geographyL1.name , ne.geographyL4.geographyL3.geographyL2.geographyL1.id "),
		@NamedQuery(name = "getAllSiteCountCircleWise", query = "SELECT new com.inn.foresight.core.infra.wrapper.TotalSiteLayerWiseWrapper(ne.geographyL4.geographyL3.geographyL2.name, COUNT(DISTINCT ne.neName),ne.geographyL4.geographyL3.geographyL2.latitude,ne.geographyL4.geographyL3.geographyL2.longitude ) FROM NetworkElement ne  where ne.isDeleted = 0 AND ne.networkElement IS NULL AND ne.geographyL4.geographyL3.geographyL2.name != NULL AND (ne.geographyL4.geographyL3.geographyL2.latitude BETWEEN :southWestLat AND :northEastLat ) and (ne.geographyL4.geographyL3.geographyL2.longitude BETWEEN :southWestLong AND :northEastLong )  group by ne.geographyL4.geographyL3.geographyL2.name , ne.geographyL4.geographyL3.geographyL2.id ,ne.geographyL4.geographyL3.geographyL2.latitude , ne.geographyL4.geographyL3.geographyL2.longitude"),
		@NamedQuery(name = "getAlarmCountCityWise", query = "SELECT new com.inn.foresight.core.infra.wrapper.TotalSiteLayerWiseWrapper(ne.geographyL4.geographyL3.name, COUNT(DISTINCT ne.neName),ne.geographyL4.geographyL3.latitude,ne.geographyL4.geographyL3.longitude ) FROM NetworkElement ne  where ne.isDeleted = 0 AND ne.networkElement IS NULL AND ne.geographyL4.geographyL3.name != NULL AND (ne.geographyL4.geographyL3.latitude BETWEEN :southWestLat AND :northEastLat ) and (ne.geographyL4.geographyL3.longitude BETWEEN :southWestLong AND :northEastLong )  group by ne.geographyL4.geographyL3.name , ne.geographyL4.geographyL3.id ,ne.geographyL4.geographyL3.latitude , ne.geographyL4.geographyL3.longitude"),
		@NamedQuery(name = "getTotalSiteCount", query = "SELECT new com.inn.foresight.core.infra.wrapper.TotalSiteLayerWiseWrapper(ne.geographyL4.name, COUNT(DISTINCT ne.neName) ,ne.geographyL4.latitude,ne.geographyL4.longitude) FROM NetworkElement ne  where ne.isDeleted = 0 AND ne.networkElement IS NULL AND ne.geographyL4.name != NULL AND (ne.geographyL4.latitude BETWEEN :southWestLat AND :northEastLat ) and (ne.geographyL4.longitude BETWEEN :southWestLong AND :northEastLong ) group by ne.geographyL4.name , ne.geographyL4.id ,ne.geographyL4.latitude , ne.geographyL4.longitude"),
		@NamedQuery(name = "getNEDetailByNEName", query = "select n from NetworkElement n where upper(n.neName) in (:neName)"),
		@NamedQuery(name = "getNEDetailByNEIdList", query = "select n from NetworkElement n left join fetch n.geographyL4 where upper(n.neId) in (:neId) or upper(n.neName) in(:neId)"),
		@NamedQuery(name = "getAllNEOfGeographyL3", query = "select n from NetworkElement n JOIN FETCH n.geographyL4 g"),
		// @NamedQuery(name = "getNEDetailByNEIdAndDomainVendor", query="select n from
		// NetworkElement n where
		// upper(n.neId) in (:neId) and UPPER(n.domain) =:domain and UPPER(n.vendor)
		// =:vendor"),
		@NamedQuery(name = "getNeIdCountByGeography", query = "select count(distinct n.neId) from NetworkElement n where n.geographyL4.name in(:geoNameList)"),
		@NamedQuery(name = "getDistinctVendorForSite", query = "select distinct n.vendor from NetworkElement n where n.vendor is not null and n.neType ='MACRO'"),
		@NamedQuery(name = "getCellIdsBygeographyL4Name", query = "select n.neId from NetworkElement n where n.geographyL4.name in(:geographyL4) and n.isDeleted=false and n.networkElement is not null"),
		@NamedQuery(name = "getNeNameListByNeNameAndNeType", query = "select distinct n.neId from NetworkElement n where upper(n.neName) like :neName and n.neType=:neType and n.isDeleted=0 and n.neStatus=:neStatus"),
		@NamedQuery(name = "getNEDataByBoundaryMinMax", query = "select n from NetworkElement n where n.latitude>=:minLat and n.latitude<=:maxLat and n.longitude>=:minLon and n.longitude<=:maxLon and n.neType in :neType and n.neStatus in :neStatus"),
		// @NamedQuery(name = "getNEDataByLatLon", query = "select n from NetworkElement
		// n where n.latitude>=:minLat and
		// n.latitude<=:maxLat and n.longitude>=:minLon and n.longitude<=:maxLon")
		@NamedQuery(name = "getGeographyL4BySapId", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.neId,n.neName,n.geographyL4.name,n.geographyL4.id,n.latitude,n.longitude) from NetworkElement n where upper(n.neName) like :neName"),
		@NamedQuery(name = "getGeographyL1BySapId", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.neId,n.neName,n.geographyL4.geographyL3.geographyL2.geographyL1.name,n.geographyL4.geographyL3.geographyL2.geographyL1.id,n.latitude,n.longitude) from NetworkElement n where upper(n.neName) like :neName"),
		@NamedQuery(name = "getTechnologyByVendor", query = "select distinct n.technology from NetworkElement n where n.technology is not null and n.vendor in(:vendors) "),
		@NamedQuery(name = "getVendorsByType", query = "select distinct n.vendor from NetworkElement n where n.vendor is not null and n.neType in(:neType) "),
		@NamedQuery(name = "getNetworkElementByName", query = "select n from NetworkElement n where n.neType = 'MACRO' and n.neName=:siteName"),
		@NamedQuery(name = "getNEByNodeAndGeoName", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(CAST(n.domain as string),CAST(n.vendor as string),n.neId,n.geographyL4.geographyL3.name) from NetworkElement n where n.neType IN (:neType) and UPPER(n.geographyL4.name)=UPPER(:geoName)"),
		@NamedQuery(name = "getLatLngByNE", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.neId,n.neName,n.latitude,n.longitude) from NetworkElement n where n.networkElement is not null"),
		@NamedQuery(name = "getNeMacroCellData", query = "select n from NetworkElement n where n.networkElement in (select ne.id from NetworkElement ne where ne.neType=:neType and ne.neName=:neName and ne.isDeleted=0) and n.isDeleted=0"),
		@NamedQuery(name = "getNEDataByNeNameAndNeType", query = "select n from NetworkElement n where n.neName=:nename and n.neType=:netype and n.latitude is not null and n.longitude is not null and n.isDeleted=0"),
		@NamedQuery(name = "getNEDataByNeIdAndNeType", query = "select n from NetworkElement n where n.neId=:neId and n.neType=:netype and n.latitude is not null and n.longitude is not null and n.isDeleted=0"),
		@NamedQuery(name = "getL3AndNeidByNeName", query = "select new com.inn.foresight.core.infra.wrapper.SiteCountWrapper(n.geographyL4.geographyL3.name,n.neId) from NetworkElement n where n.neName = (:neName)"),
		@NamedQuery(name = "getGeographyL3ByNeName", query = "select n.geographyL4.geographyL3.name from NetworkElement n  where n.neName=:neName"),
		@NamedQuery(name = "getDistinctL1ByNeId", query = "select distinct n.geographyL4.geographyL3.geographyL2.geographyL1 from NetworkElement n  where n.id in (:neIdList)"),
		@NamedQuery(name = "getTACByNeName", query = "select r.trackingArea from NetworkElement n inner join RANDetail r on n.id=r.networkElement.id  where n.neName like (:neName) and n.isDeleted=0 and r.trackingArea is not null group by n.networkElement,r.trackingArea order by count(r.trackingArea) desc"),
		@NamedQuery(name = "getDomainVendorAndL3ByNEPk", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.domain,n.vendor,geo4.geographyL3.id,n.neId,ng.otherGeography.id,n1.neId) from NetworkElement n LEFT OUTER JOIN n.networkElement AS n1 LEFT JOIN n.geographyL4 AS geo4 LEFT JOIN NEGeographyMapping ng on n.id=ng.networkElement.id WHERE n.id =:pkId"),
		@NamedQuery(name = "getDomainVendorAndL3ByNeId", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.domain,n.vendor,geo4.geographyL3.id,n.neId,ng.otherGeography.id,n1.neId, n.neType) from NetworkElement n LEFT OUTER JOIN n.networkElement AS n1 LEFT JOIN  n.geographyL4 AS geo4 LEFT JOIN NEGeographyMapping ng on n.id=ng.networkElement.id WHERE n.neId =:neId"),
		@NamedQuery(name = "getAllChildNEDetailByNetworkFK", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementCellDetailWrapper(n.neName , n.neFrequency, n.vendor) from NetworkElement n where n.networkElement.id = (:neFkId) "),
		@NamedQuery(name = "getOnAirNetworkElementData", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementOnAirJsiWrapper( n.neId, n.networkElement.neId, n.neName, n.networkElement.neName, n.latitude, n.longitude, n.neFrequency, n.domain, n.vendor, geol3.name, n.id, n.neStatus) from NetworkElement n LEFT JOIN n.networkElement n2 LEFT JOIN n.geographyL4 l4 LEFT JOIN n.geographyL4.geographyL3 geol3 where n.neType in ('ODSC_SITE','MACRO','MACRO_CELL') and n.neStatus = 'ONAIR' AND n.isDeleted = 0"),
		@NamedQuery(name = "getL4SiteCountDomainWise", query = "SELECT new com.inn.foresight.core.infra.wrapper.TotalSiteLayerWiseWrapper(ne.geographyL4.geographyL3.geographyL2.geographyL1.name, COUNT(DISTINCT ne.neName)) FROM NetworkElement ne  where ne.isDeleted = 0 AND ne.networkElement IS NULL AND ne.geographyL4.geographyL3.geographyL2.geographyL1.name != NULL AND ne.domain = :domain group by ne.geographyL4.geographyL3.geographyL2.geographyL1.name , ne.geographyL4.geographyL3.geographyL2.geographyL1.id "),
		@NamedQuery(name = "getL4SiteCountDomainAndVendorWise", query = "SELECT new com.inn.foresight.core.infra.wrapper.TotalSiteLayerWiseWrapper(ne.geographyL4.geographyL3.geographyL2.geographyL1.name, COUNT(DISTINCT ne.neName)) FROM NetworkElement ne  where ne.isDeleted = 0 AND ne.networkElement IS NULL AND ne.geographyL4.geographyL3.geographyL2.geographyL1.name != NULL AND ne.domain = :domain AND ne.vendor = :vendor group by ne.geographyL4.geographyL3.geographyL2.geographyL1.name , ne.geographyL4.geographyL3.geographyL2.geographyL1.id "),
		@NamedQuery(name = "getNetworkElementData", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.neType, (CASE WHEN n.networkElement is null THEN n.neId ELSE n.networkElement.neId END),n.neId,n.vendor,n.domain,n.latitude,n.longitude,n.geographyL4.name,n.geographyL4.geographyL3.name,n.geographyL4.geographyL3.geographyL2.name,n.geographyL4.geographyL3.geographyL2.geographyL1.name,n.neName,n.geographyL4.geographyL3.id) from NetworkElement n LEFT JOIN n.networkElement WHERE n.geographyL4.geographyL3.name IS NOT NULL"),
		@NamedQuery(name = "getParentWiseCells", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementCellDetailWrapper(n.neId, n.networkElement.neId, n.neName) from NetworkElement n where n.isDeleted = 0"),
		@NamedQuery(name = "getNEByNodeAndOtherGeography", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(CAST(n.domain as string),CAST(n.vendor as string),n.neId,nm.otherGeography.name) from NetworkElement n JOIN NEGeographyMapping nm on n.id = nm.networkElement.id where n.neType in (:neType) and UPPER(nm.otherGeography.name)=UPPER(:geoName)"),
		@NamedQuery(name = "getOtherGeographyNeidByNeNameDomainVendor", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(nm.otherGeography.name, nm.networkElement.neId, nm.networkElement.id) from NEGeographyMapping nm where nm.networkElement.neName=(:neName) and CAST(nm.networkElement.vendor as string)=(:vendor) and CAST(nm.networkElement.domain as string)=(:domain)"),
		@NamedQuery(name = "getGeographyL3NameNeIdByNEName", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.geographyL4.geographyL3.name, n.neId, n.id)  from NetworkElement n  where n.neName=:neName"),
		@NamedQuery(name = "getDomainVendorAndL3ByNeIdList", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.domain,n.vendor,geo4.geographyL3.id,n.neId) from NetworkElement n LEFT JOIN  GeographyL4 geo4 on n.geographyL4.id = geo4.id where n.neId in (:neId)"),

		/* HARSHIT */
		@NamedQuery(name = "getNetworkElementByNeId", query = "select new com.inn.foresight.core.infra.wrapper.NEDetailWrapper(n.neId,"
				+ " n.networkElement.neId,n.neName,n.networkElement.neName,n.neType,n.networkElement.neType,n.neStatus,"
				+ "n.networkElement.neStatus) from NetworkElement n where n.isDeleted=0 "),
		

		@NamedQuery(name = "getVendorAndL4WiseNename", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.domain,n.vendor,geo4.geographyL3.id,n.neId) from NetworkElement n LEFT JOIN  GeographyL4 geo4 on n.geographyL4.id = geo4.id where n.neId in (:neId)"),
		@NamedQuery(name = "getAPNDetailByFloorId", query = "select n from NetworkElement n JOIN FETCH n.floor f JOIN FETCH f.wing w JOIN FETCH w.building where f.id in (:floorId)"),
		@NamedQuery(name = "getNEByMacaddress", query = "select n from NetworkElement n where macaddress=:macaddress"),
		@NamedQuery(name = "getDistinctNEFrequencyByNEName", query = "select distinct n.neFrequency from NetworkElement n where n.networkElement.neName =:neName"),
		@NamedQuery(name = "getNEIdAndFriendlyName", query = "select new com.inn.foresight.core.infra.wrapper.NeDataWrapper(n.friendlyname,n.neId) from NetworkElement n where n.friendlyname is not null"),

		@NamedQuery(name = "getEnodeBIdFromFrequencyAndParentNeName", query = "select  ne.enbid from NetworkElement ne where ne.neFrequency=:frequency and ne.networkElement.neName in (:neList) "),
		@NamedQuery(name = "getNetypeFromNetworkElement", query = "select distinct (n.neType) from NetworkElement n where n.neType is not null and n.isDeleted=0"),
        
		/** distinct Netype. */
		@NamedQuery(name = "getDistinctNeType", query = "select  distinct ne.neType from NetworkElement as ne where ne.domain=:domain and ne.neType is not null"),

		/** Network Element Details. */
		@NamedQuery(name = "getNetworkElementDetailsByVendorAndEms", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(ne.neName,ne.ipv4,ne.neType,ne.vendor,ne.domain,ne.model,ne.softwareVersion) from NetworkElement ne inner join ne.emsServer e where ne.vendor=:vendor and ne.domain=:domain and e.emsname=:hostName"),

		/** Site Count. */
		@NamedQuery(name = "getSiteCountByNeType", query = "select ne.neType, count(ne.neType),ne.technology from NetworkElement as ne where ne.domain=:domain and ne.vendor=:vendor and ne.emsServer.emsname=:hostName group by ne.neType "),

		/** Vendor Count. */
		@NamedQuery(name = "getVendorCountByDomain", query = " select count(distinct n.vendor) from NetworkElement n where UPPER(n.domain)=:domain and n.vendor is not null "),

		/** Band wise Data. */
		@NamedQuery(name = "getBandWiseData", query = "select ne.neFrequency,count(distinct ne.neFrequency) from NetworkElement ne where ne.domain=:domain and ne.neFrequency is not null group by ne.neFrequency"),

		/** Macro cell count. */
		@NamedQuery(name = "getMacroCellCount", query = "select n.technology , count(distinct n.id), n.neFrequency from NetworkElement n where n.domain=:domain and n.neStatus is not null and  n.neFrequency is not null  and n.isDeleted=false and n.id is not null and n.neType !='ODSC_CELL' group by n.technology, n.neFrequency"),

		/** Small cell count. */
		@NamedQuery(name = "getSmallCellCount", query = "select n.technology , count(distinct n.id), n.neFrequency from NetworkElement n where n.domain=:domain and n.neStatus is not null and  n.neFrequency is not null  and n.isDeleted=false and n.id is not null and n.neType ='ODSC_CELL'  group by n.technology, n.neFrequency"),

		/** Enb Site count. */
		@NamedQuery(name = "getEnbSiteCount", query = "select n.technology ,  count(distinct n.id), n.neStatus from NetworkElement n where n.domain=:domain and n.neStatus is not null and  n.neStatus !='PLANNED' and n.isDeleted=false and n.id is not null  group by n.technology, n.neStatus"),

		/** Enb Site count Vendor Wise. */
		@NamedQuery(name = "getEnbSiteCountVendorWise", query = "select n.vendor, n.technology , count(distinct n.id), max(n.modifiedTime) from NetworkElement n where n.isDeleted=false and n.domain=:domain and n.vendor is not null and n.technology is not null and n.neStatus !='PLANNED' group by n.vendor"),

		/** Small cell count Vendor Wise. */
		@NamedQuery(name = "getSmallCellCountVendorWise", query = "select n.technology , count(distinct n.id), n.neFrequency from NetworkElement n where n.domain=:domain and n.neStatus is not null and  n.neFrequency is not null  and n.isDeleted=false and n.id is not null and n.neType ='ODSC_CELL'  group by n.technology, n.neFrequency,n.vendor,n.domain"),

		/** Macro cell count Vendor Wise. */
		@NamedQuery(name = "getMacroCellCountVendorWise", query = "select n.technology , count(distinct n.id), n.neFrequency from NetworkElement n where n.domain=:domain and n.neStatus is not null and  n.neFrequency is not null  and n.isDeleted=false and n.id is not null and n.neType !='ODSC_CELL'  group by n.technology, n.neFrequency,n.vendor,n.domain"),

		/** EMS count Vendor Wise. */
		@NamedQuery(name = "getEmsCountVendorWise", query = "select count(distinct n.emsServer.id),n.vendor from NetworkElement n where n.domain=:domain group by n.vendor"),

		@NamedQuery(name = "getEmsWiseSiteCount", query = "select n.emsServer.emsname,count(n.id),n.neStatus from NetworkElement n,EmsServer e where n.domain=:domain and n.vendor=:vendor and n.neStatus is not null and n.isDeleted=false and n.id is not null  and  n.emsServer.emsname is not null and neType in('GALLERY_SITE','PICO_SITE','IBS_SITE','SHOOTER_SITE','ODSC_SITE') group by n.emsServer.emsname,n.neStatus"),

		/** EMS wise NetworkElement Details. */
		@NamedQuery(name = "getEmsWiseNetworkElementDetails", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper (n.neName , n.neType ,n.neStatus,n.technology,case when n.duplex='DUAL' then 'FDD/TDD' else n.duplex end as duplex ,gl4.name,gl3.name,gl2.name,gl1.name, count(case when n.neFrequency='850' then 1 end) as FDDCount, count(case when n.neFrequency='2300' then 1 end) as TDDCount) from NetworkElement n join EmsServer e on n.emsServer.id=e.id left join GeographyL4 gl4 on (n.geographyL4.id=gl4.id) left join GeographyL3 gl3 on (gl4.geographyL3.id=gl3.id)  left join GeographyL2 gl2 on gl2.id=gl3.geographyL2.id left join GeographyL1 gl1 on gl1.id=gl2.geographyL1.id where n.domain=:domain and n.vendor=:vendor and n.emsServer.emsname=:hostName and n.neStatus is not null and n.isDeleted=false and n.neName is not null group by n.neName,n.neType,n.neStatus "),

		
		@NamedQuery(name = "searchByNEId", query = "select e from NetworkElement e where e.neId=:neId"),
		@NamedQuery(name = "searchByHostName", query = "select e from NetworkElement e where e.hostname=:hostname"),
		@NamedQuery(name = "getNetworkElementByNELocation", query = "select e from NetworkElement e where e.neLocation.id=:nelocationid_fk"),
		@NamedQuery(name = "searchByEnodeBPk", query = "select e from NetworkElement e where e.parentNE.id=:enodeBPk and netype = 'MACRO'"),
		@NamedQuery(name = "getNetworkElementByGeographyL1", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(SUM(Case when n.neType='MACRO' then 1 else 0 end),SUM(Case when n.neType='MACRO_CELL' then 1 else 0 end),n.geographyL4.geographyL3.geographyL2.geographyL1.name,n.geographyL4.geographyL3.geographyL2.geographyL1.latitude,geographyL4.geographyL3.geographyL2.geographyL1.longitude,n.geographyL4.geographyL3.geographyL2.geographyL1.id) "
				+ "from NetworkElement n where  n.domain = (:domain) and n.isDeleted=0  group by n.geographyL4.geographyL3.geographyL2.geographyL1.name"),
		@NamedQuery(name = "getNetworkElementByGeographyL2", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(SUM(Case when n.neType='MACRO' then 1 else 0 end),SUM(Case when n.neType='MACRO_CELL' then 1 else 0 end),geographyL4.geographyL3.geographyL2.name,geographyL4.geographyL3.geographyL2.latitude,geographyL4.geographyL3.geographyL2.longitude,geographyL4.geographyL3.geographyL2.id) "
				+ "from NetworkElement n where   n.domain = (:domain) and geographyL4.geographyL3.geographyL2.geographyL1.id= (:id) and n.isDeleted=0 group by geographyL4.geographyL3.geographyL2.name"),
		@NamedQuery(name = "getNetworkElementByGeographyL3", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(SUM(Case when n.neType='MACRO' then 1 else 0 end),SUM(Case when n.neType='MACRO_CELL' then 1 else 0 end),geographyL4.geographyL3.name,geographyL4.geographyL3.latitude,geographyL4.geographyL3.longitude,geographyL4.geographyL3.id) "
				+ "from NetworkElement n where  n.domain = (:domain) and geographyL4.geographyL3.geographyL2.id= (:id) and n.isDeleted=0  group by geographyL4.geographyL3.name"),
		@NamedQuery(name = "getNetworkElementByGeographyL4", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(SUM(Case when n.neType='MACRO' then 1 else 0 end),SUM(Case when n.neType='MACRO_CELL' then 1 else 0 end),geographyL4.name,geographyL4.latitude,geographyL4.longitude,geographyL4.id,'MACRO') "
				+ "from NetworkElement n where n.domain = (:domain)  and geographyL4.geographyL3.id= (:id) and n.isDeleted=0  group by geographyL4.name"),
		@NamedQuery(name = "getNetworkElementByMACRO", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(count(distinct n.networkElement.neId),count(distinct n.neId),n.networkElement.networkElement.networkElement.neId,n.networkElement.networkElement.networkElement.latitude,n.networkElement.networkElement.networkElement.longitude,n.networkElement.networkElement.networkElement.id,'MACRO_CELL')"
				+ "from NetworkElement n where  n.domain = (:domain)   and n.isDeleted=0 and n.networkElement.networkElement.networkElement.geographyL4.id= (:id) group by n.networkElement.networkElement.networkElement.neId"),
		@NamedQuery(name = "getNetworkElementByMacroCell", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(count(distinct n.networkElement.neId),count(distinct n.neId),n.networkElement.neId,n.latitude,n.longitude,n.networkElement.id) "
				+ "from NetworkElement n where  n.domain = (:domain)  and n.networkElement.networkElement.networkElement.id=(:neidpk) and n.isDeleted=0  group by n.networkElement.neId"),
		@NamedQuery(name = "getNetworkElementCountByNetype", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(SUM(case when n.neType='MACRO_ENB' then 1 else 0 end),"
				+ "SUM(case when n.neType='MACRO' then 1 else 0 end),SUM(case when n.neType='MACRO_CELL' then 1 else 0 end)) "
				+ "from NetworkElement n where  n.domain = (:domain) and n.isDeleted=0"),

		@NamedQuery(name = "getRouterCountDomainWise", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper( n.neType,count(n.id), n.technology,n.vendor,n.emsServer.emsname,n.emsServer.ip) from NetworkElement n where  UPPER(n.domain)=:domain  and n.emsServer.emsname is not null and n.isDeleted=0 and n.neType is not null and n.latitude is not null and n.longitude is not null and n.geographyL4 is not null and (n.neStatus not in ('DECOMMISSIONED','DISMENTAL') or n.neStatus is null) group by n.emsServer.emsname,n.neType,n.vendor"),
		@NamedQuery(name = "getSiteCountDataVendorWise", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.vendor,n.neStatus,n.emsServer.emsname,count(n.id))from NetworkElement n where n.domain=:domain and n.neStatus in (:neStatusList) and n.isDeleted=false and n.neType in (:neTypeList)  and n.technology=:technology and n.latitude is not null and n.longitude is not null and n.geographyL4 is not null and (n.neStatus not in ('DECOMMISSIONED','DISMENTAL') or n.neStatus is null) group by n.emsServer.emsname,n.vendor,n.neStatus"),
		@NamedQuery(name = "getChildNEListByParentNE", query = "select ne from NetworkElement ne where ne.parentNE.id=:parentNEid"),
		@NamedQuery(name = "getNeInfoByneFkAndNeTypeWithStatus", query = "select ne from NetworkElement ne where ne.networkElement.id=:networkFK and neType=:neType and status in (:dayOneStatus)"),
		@NamedQuery(name = "updateCellAndSiteStatus", query = "update NetworkElement n set n.neStatus=:neStatus where n.parentNE.id=:id and n.neType in (:neTypeList)"),
		@NamedQuery(name = "updatePmEmsId", query = "update NetworkElement n set n.pmEmsId=:pmEmsId where n.domain=:domain and n.vendor=:vendor and n.neType=:neType and n.neId=:neId"),
		@NamedQuery(name = "getVduAndVcuByNeid", query = "select distinct n.networkElement.neId from NetworkElement n where n.neId in (:neids) and n.isDeleted=0"),
		@NamedQuery(name = "searchByNeName", query = "select ne from NetworkElement ne where neName=:siteID"),
		@NamedQuery(name = "getOnAirSitesCount", query = "select count(distinct n.neName) from NetworkElement n where n.neType in(:neType) and n.domain=:domain and n.vendor=:vendor and n.neStatus = 'ONAIR' and n.isDeleted=0 "),
		@NamedQuery(name = "getSitesCountGroupByNestatus", query = "select n.neStatus,count(n.neStatus) from NetworkElement n where n.neType in(:neType) and n.neStatus is not null and n.isDeleted=0  group by n.neStatus "),
		@NamedQuery(name = "getNetworkElementByNeIdAndNetype", query = "select n from NetworkElement n where n.neType=:netype and n.parentNE.id=:parentNEid and n.neId=:riuSerialNo"),
		@NamedQuery(name = "getTotalNECountByGeographyL1", query = "select count(n.id) from NetworkElement n where n.neType =:neType and n.neStatus in(:statusList) and n.domain=:domain and n.vendor=:vendor and n.isDeleted=0 and n.geographyL4.geographyL3.geographyL2.geographyL1.id = (:id) "),
		@NamedQuery(name = "getTotalNECountByGeographyL2", query = "select count(n.id) from NetworkElement n where n.neType =:neType and n.neStatus in(:statusList) and n.domain=:domain and n.vendor=:vendor and n.isDeleted=0 and n.geographyL4.geographyL3.geographyL2.id = (:id) "),
		@NamedQuery(name = "getTotalNECountByGeographyL3", query = "select count(n.id) from NetworkElement n where n.neType =:neType and n.neStatus in(:statusList) and n.domain=:domain and n.vendor=:vendor and n.isDeleted=0 and n.geographyL4.geographyL3.id = (:id)"),
		@NamedQuery(name = "getTotalNECountByGeographyL4", query = "select count(n.id) from NetworkElement n where n.neType =:neType and n.neStatus in(:statusList) and n.domain=:domain and n.vendor=:vendor and n.isDeleted=0 and n.geographyL4.id = (:id)"),
		@NamedQuery(name = "getTotalNECountByPanGeography", query = "select count(n.id) from NetworkElement n where n.neType =:neType and n.neStatus in(:statusList) and n.domain=:domain and n.vendor=:vendor and n.isDeleted=0 "),
		@NamedQuery(name = "getChildByNetworkElementByParent", query = "select e from NetworkElement e where e.networkElement.id=:id"),
		@NamedQuery(name = "getEnodeBIdForNEName", query = "select distinct new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.networkElement.neName,n.enbid,n.duplex) from NetworkElement n where n.networkElement.neName in(:neName)"),
		@NamedQuery(name = "updateNetworkElementStatusByVCUId", query = "update NetworkElement n set n.status=:status where n.parentNE.id=:vcuId and n.isDeleted=false"),
		@NamedQuery(name = "getNETaskDetailDataByNameAndType", query = "select new com.inn.foresight.core.infra.wrapper.NETaskDetailWrapper(nt.neBandDetail.neFrequency,nt.taskName,nt.completionStatus,nt.taskStatus,nt.actualEndDate,nt.taskDay) from NETaskDetail nt where nt.neBandDetail.networkElement.isDeleted=0 and nt.neBandDetail.networkElement.neName=:neName and nt.neBandDetail.networkElement.neType=:neType group by nt.neBandDetail.neFrequency,nt.taskName,nt.completionStatus,nt.taskStatus,nt.actualEndDate,nt.taskDay order by nt.neBandDetail.neFrequency,nt.executionOrder desc "),
		@NamedQuery(name = "getNetworkElementIdByNameAndCellNum", query = "select r from  RANDetail r where r.networkElement.neName=:neName and r.networkElement.neFrequency=:sourceFrequencyBand and r.networkElement.vendor =:vendor  and r.networkElement.domain =:domain "),
		@NamedQuery(name = "getNEModelByNEId", query = "select  new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.neId,n.neName,n.model,n.mcc) from NetworkElement n where n.neId in(:neId)"),
		@NamedQuery(name = "getNetworkElementByIPV4", query = "select ne from NetworkElement ne where ne.ipv4 = :ipv4 and ne.isDeleted=false"),
		@NamedQuery(name = "getNEFriendlyNameListByNENameList", query = "select n.neName , n.friendlyname from NetworkElement n where n.neName in (:neNamesList)"),
		@NamedQuery(name = "getNEbyNETypeList", query = "select n from NetworkElement n where n.neType in (:neTypeList) and  n.isDeleted=0"),
		@NamedQuery(name = "getNetworkElementDataBySiteId", query = "select ne from NetworkElement ne where ne.neName = :siteId and ne.isDeleted=false and ne.latitude is not null and ne.longitude is not null and ne.geographyL4 is not null"),
		@NamedQuery(name = "getNetworkElementListById", query = "select ne from NetworkElement ne where ne.id in(:networkElementIdList) "),
		@NamedQuery(name = "getDomainVendorAndGeographyL3ByNeid", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.domain,n.vendor,n.geographyL3.id,n.neId,ng.otherGeography.id,n1.neId) from NetworkElement n LEFT OUTER JOIN n.networkElement AS n1 LEFT JOIN NEGeographyMapping ng on n.id=ng.networkElement.id WHERE n.neId =:neId"),
		@NamedQuery(name = "getDomainVendorAndGeographyL3ByNEPk", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.domain,n.vendor,n.geographyL3.id,n.neId,ng.otherGeography.id,n1.neId) from NetworkElement n LEFT OUTER JOIN n.networkElement AS n1 LEFT JOIN NEGeographyMapping ng on n.id=ng.networkElement.id WHERE n.id =:pkId"),
		@NamedQuery(name = "getMostParentNEIDByChildNEID", query = "select distinct ne.parentNE.neId from NetworkElement ne  WHERE ne.neId in (:neidList) and ne.isDeleted=false"),
		@NamedQuery(name = "getSiteCountByLocationId", query = "select count(site.id) from NetworkElement vbu,NetworkElement site where vbu.id = site.networkElement.id and vbu.neLocation.id=:locationId  and vbu.neType = 'MACRO_VDU' and vbu.isDeleted='false' and site.neStatus = 'ONAIR'"),
		@NamedQuery(name = "getSiteCountForGCByNeStatus", query = "select count(n.id) from NetworkElement n  WHERE n.parentNE.id  in (select ne.id from NetworkElement ne where ne.neLocation.nelType=:nelType and ne.neType=:neType and ne.neLocation.nelId=:nelId and ne.isDeleted=0) and n.neType=:parentNeType and n.neStatus=:neStatus and n.isDeleted=0"),
		@NamedQuery(name = "getSiteCountByLatLong", query = "select count(ne.id) from NetworkElement ne  WHERE ne.networkElement.neType=:neType and ne.isDeleted=false and ne.networkElement.neLocation.latitude >= :southWestLat and ne.networkElement.neLocation.latitude <= :northEastLat and ne.networkElement.neLocation.longitude >=:southWestLong and ne.networkElement.neLocation.longitude <=:northEastLong and ne.neStatus = 'ONAIR'"),
		@NamedQuery(name = "getDomainVendorAndL3ByMultiNEPk", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(n.domain,n.vendor,geo4.geographyL3.id,n.neId,ng.otherGeography.id,n1.neId) from NetworkElement n LEFT OUTER JOIN n.networkElement AS n1 LEFT JOIN n.geographyL4 AS geo4 LEFT JOIN NEGeographyMapping ng on n.id=ng.networkElement.id WHERE n.id IN (:pkId)"),
		@NamedQuery(name = "getSitesInfoByLocationId", query = "select new com.inn.foresight.core.infra.wrapper.NetworkElementWrapper(site.neName,site.neType,site.neStatus,site.domain,site.vendor,site.ipv4,count(cell.id)) from NetworkElement vbu,NetworkElement site,NetworkElement cell where vbu.id = site.networkElement.id and site.id = cell.networkElement.id and vbu.neLocation.id=:locationId  and vbu.neType = 'MACRO_VDU'  and vbu.isDeleted='false' and site.neStatus = 'ONAIR' group by site.neId"),
		@NamedQuery(name = "getNetworkElementByLatLng", query = "select ne from NetworkElement ne where ne.latitude = :latitude and ne.longitude =:longitude and ne.isDeleted=false and ne.propertyId is not null"),
		@NamedQuery(name = "getNENearBy80Meter", query = "select ne from NetworkElement ne where ST_Distance_Sphere(POINT(FN_GETVALIDLONG(:longitude),FN_GETVALIDLAT(:latitude)),POINT(FN_GETVALIDLONG(ne.longitude),FN_GETVALIDLAT(ne.latitude))) <= 10 and ne.isDeleted=false and ne.propertyId is not null and ne.latitude is not null and ne.longitude is not null and ne.neType='COR'"),
        @NamedQuery(name = "getAllCellsData", query = "select n from NetworkElement n where n.neType = 'MACRO_CELL' and n.neStatus = 'ONAIR' and n.isDeleted=false"),
        @NamedQuery(name = "getNetworkElementByNELocationId", query = "select ne from NetworkElement ne where ne.neType ='MACRO_CELL' and ne.neStatus = 'ONAIR' and ne.isDeleted=false and ne.parentNE.neLocation.id =:neLocationId and ne.parentNE.neType = 'MACRO_ENB'"),
		@NamedQuery(name = "getNetworkElementBySlNoAndType", query = "select n from NetworkElement n where n.neType = :neType and n.physicalSerialNumber = :slNo and n.isDeleted = false") ,
		
		@NamedQuery(name = "getMaxCellNumByParentNEandNEType", query = "select max(n.cellNum) from NetworkElement n where n.neType = :neType and n.parentNE.id=:parentneid and n.isDeleted = false ") ,
		
		
		@NamedQuery(name = "getNEByVCUNameAndCellNum", query = "select n from NetworkElement n where n.parentNE.neName=:vcuName and n.cellNum=:cellNum and n.isDeleted = false and n.neType = :neType"), 
		@NamedQuery(name = "getfilterByNeName", query = "select n.neName from NetworkElement n where n.neType=:neType and n.isDeleted=false and n.neName like concat(:neName,'%')")
		})


@FilterDefs({
		@FilterDef(name = "geographyL4IdFilter", parameters = {
				@ParamDef(name = "geographyL4", type = "java.lang.String") }),
		@FilterDef(name = "geographyL3IdFilter", parameters = {
				@ParamDef(name = "geographyL3", type = "java.lang.String") }),
		@FilterDef(name = "geographyL2IdFilter", parameters = {
				@ParamDef(name = "geographyL2", type = "java.lang.Integer") }),
		@FilterDef(name = "geographyL1IdFilter", parameters = {
				@ParamDef(name = "geographyL1", type = "java.lang.String") }),
		@FilterDef(name = "networkElementNeNameFilter", parameters = {
				@ParamDef(name = "neName", type = "java.lang.String") }),
		@FilterDef(name = "networkElementTechnologyFilter", parameters = {
				@ParamDef(name = "technology", type = "java.lang.String") }),
		@FilterDef(name = "networkElementNeTypeFilterForList", parameters = {
				@ParamDef(name = "neType", type = "java.lang.String") }),
		@FilterDef(name = "networkElementSoftwareVersionFilter", parameters = {
				@ParamDef(name = "swversion", type = "java.lang.String") }),
		@FilterDef(name = "networkElementDomainFilter", parameters = {
				@ParamDef(name = "domain", type = "java.lang.String") }),
		@FilterDef(name = "networkElementVendorFilter", parameters = {
				@ParamDef(name = "vendor", type = "java.lang.String") }),
		@FilterDef(name = "networkElementNeTypeFilterByLike", parameters = {
				@ParamDef(name = "neType", type = "java.lang.String") }),
		@FilterDef(name = "networkElementNeTypeFilter", parameters = {
				@ParamDef(name = "neType", type = "java.lang.String") }),
		@FilterDef(name = "networkElementGeoL1Filter", parameters = {
				@ParamDef(name = "geographyL1Id", type = "java.lang.Integer") }),
		@FilterDef(name = "networkElementGeoL2Filter", parameters = {
				@ParamDef(name = "geographyL2Id", type = "java.lang.Integer") }),
		@FilterDef(name = "networkElementGeoL3Filter", parameters = {
				@ParamDef(name = "geographyL3Id", type = "java.lang.Integer") }),
		@FilterDef(name = "networkElementGeoL4Filter", parameters = {
				@ParamDef(name = "geographyL4Id", type = "java.lang.Integer") }),
		@FilterDef(name = "networkElementVendorListFilter", parameters = {
				@ParamDef(name = "vendorList", type = "java.lang.String") }),

		@FilterDef(name = "networkElementNameInFilter", parameters = {
				@ParamDef(name = "neName", type = "java.lang.String") }),
		@FilterDef(name = "networkElementGeoL1NameInFilter", parameters = {
				@ParamDef(name = "geographyL1Name", type = "java.lang.String") }),
		@FilterDef(name = "networkElementGeoL2NameInFilter", parameters = {
				@ParamDef(name = "geographyL2Name", type = "java.lang.String") }),
		@FilterDef(name = "networkElementGeoL3NameInFilter", parameters = {
				@ParamDef(name = "geographyL3Name", type = "java.lang.String") }),
		@FilterDef(name = "networkElementGeoL4NameInFilter", parameters = {
				@ParamDef(name = "geographyL4Name", type = "java.lang.String") }), })

@Filters({
		@Filter(name = "geographyL4IdFilter", condition = " networkelementid_fk in (select n.networkelementid_pk from NetworkElement n , GeographyL4 gl4  where  gl4.geographyl4id_pk=n.geographyl4id_fk and  upper(gl4.name)=:geographyL4 ) "),
		@Filter(name = "geographyL3IdFilter", condition = " networkelementid_fk in (select n.networkelementid_pk from GeographyL3 g3 inner join GeographyL4 g4 on g3.geographyl3id_pk=g4.geographyl3id_fk inner join NetworkElement n on g4.geographyl4id_pk=n.geographyl4id_fk and upper(g3.name)=:geographyL3) "),
		@Filter(name = "geographyL2IdFilter", condition = " networkelementid_fk in (select n.networkelementid_pk from GeographyL2 g2 inner join GeographyL3 g3 on g2.geographyl2id_pk=g3.geographyl2id_fk inner join GeographyL4 g4 on g3.geographyl3id_pk=g4.geographyl3id_fk inner join NetworkElement n on g4.geographyl4id_pk=n.geographyl4id_fk and g2.geographyl2id_pk=:geographyL2) "),
		@Filter(name = "geographyL1IdFilter", condition = " networkelementid_fk in (select n.networkelementid_pk from GeographyL1 g1 inner join GeographyL2 g2 on g1.geographyl1id_pk=g2.geographyl1id_fk inner join GeographyL3 g3 on g2.geographyl2id_pk=g3.geographyl2id_fk inner join GeographyL4 g4 on g3.geographyl3id_pk=g4.geographyl3id_fk inner join NetworkElement n on g4.geographyl4id_pk=n.geographyl4id_fk and upper(g1.name)=:geographyL1 ) "),
		@Filter(name = "networkElementTechnologyFilter", condition = "technology=:technology"),
		@Filter(name = "networkElementSoftwareVersionFilter", condition = "upper(swversion)=:swversion"),
		@Filter(name = "networkElementDomainFilter", condition = "domain=:domain"),
		@Filter(name = "networkElementVendorFilter", condition = "vendor=:vendor"),
		@Filter(name = "networkElementNeTypeFilterByLike", condition = "neType like (:neType)"),
		@Filter(name = "networkElementNeTypeFilter", condition = "neType=:neType"),
		@Filter(name = "networkElementNeTypeFilterForList", condition = "neType in (:neType)"),
		@Filter(name = "networkElementNeNameFilter", condition = "neName in (:neName)"),
		@Filter(name = "networkElementGeoL4Filter", condition = " networkelementid_pk in (select n.networkelementid_pk from NetworkElement n , GeographyL4 gl4  where  gl4.geographyl4id_pk=n.geographyl4id_fk and  gl4.geographyl4id_pk=:geographyL4Id ) "),
		@Filter(name = "networkElementGeoL3Filter", condition = " networkelementid_pk in (select n.networkelementid_pk from GeographyL3 g3 inner join GeographyL4 g4 on g3.geographyl3id_pk=g4.geographyl3id_fk inner join NetworkElement n on g4.geographyl4id_pk=n.geographyl4id_fk and g3.geographyl3id_pk=:geographyL3Id) "),
		@Filter(name = "networkElementGeoL2Filter", condition = " networkelementid_pk in (select n.networkelementid_pk from GeographyL2 g2 inner join GeographyL3 g3 on g2.geographyl2id_pk=g3.geographyl2id_fk inner join GeographyL4 g4 on g3.geographyl3id_pk=g4.geographyl3id_fk inner join NetworkElement n on g4.geographyl4id_pk=n.geographyl4id_fk and g2.geographyl2id_pk=:gographyL2Id) "),
		@Filter(name = "networkElementGeoL1Filter", condition = " networkelementid_pk in (select n.networkelementid_pk from GeographyL1 g1 inner join GeographyL2 g2 on g1.geographyl1id_pk=g2.geographyl1id_fk inner join GeographyL3 g3 on g2.geographyl2id_pk=g3.geographyl2id_fk inner join GeographyL4 g4 on g3.geographyl3id_pk=g4.geographyl3id_fk inner join NetworkElement n on g4.geographyl4id_pk=n.geographyl4id_fk and g1.geographyl1id_pk=:geographyL1Id ) "),
		@Filter(name = "networkElementVendorListFilter", condition = "vendor in (:vendorList)"),

		@Filter(name = "networkElementNameInFilter", condition = " networkelementid_fk in (select n.networkelementid_pk from NetworkElement n where n.nename in (:neName))"),
		@Filter(name = "networkElementGeoL1NameInFilter", condition = " networkelementid_pk in (select n.networkelementid_pk from GeographyL1 g1 inner join GeographyL2 g2 on g1.geographyl1id_pk=g2.geographyl1id_fk inner join GeographyL3 g3 on g2.geographyl2id_pk=g3.geographyl2id_fk inner join GeographyL4 g4 on g3.geographyl3id_pk=g4.geographyl3id_fk inner join NetworkElement n on g4.geographyl4id_pk=n.geographyl4id_fk and g1.name in (:geographyL1Name)) "),
		@Filter(name = "networkElementGeoL2NameInFilter", condition = " networkelementid_pk in (select n.networkelementid_pk from GeographyL2 g2 inner join GeographyL3 g3 on g2.geographyl2id_pk=g3.geographyl2id_fk inner join GeographyL4 g4 on g3.geographyl3id_pk=g4.geographyl3id_fk inner join NetworkElement n on g4.geographyl4id_pk=n.geographyl4id_fk and g2.name in (:geographyL2Name)) "),
		@Filter(name = "networkElementGeoL3NameInFilter", condition = " networkelementid_pk in (select n.networkelementid_pk from GeographyL3 g3 inner join GeographyL4 g4 on g3.geographyl3id_pk=g4.geographyl3id_fk inner join NetworkElement n on g4.geographyl4id_pk=n.geographyl4id_fk and g3.name in (:geographyL3Name)) "),
		@Filter(name = "networkElementGeoL4NameInFilter", condition = " networkelementid_pk in (select n.networkelementid_pk from NetworkElement n , GeographyL4 gl4  where  gl4.geographyl4id_pk=n.geographyl4id_fk and  gl4.name in (:geographyL4Name) ) "), })
@Audited
@XmlRootElement(name = "NetworkElement")
@Entity
@Table(name = "NetworkElement")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NetworkElement implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2583717834312916965L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "networkelementid_pk")
	private Integer id;

	/** The network element. */
	@JoinColumn(name = "networkelementid_fk", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private NetworkElement networkElement;

	/** The network element. */
	@JoinColumn(name = "parentneid_fk", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private NetworkElement parentNE;

	/** The ne location. */
	@JoinColumn(name = "nelocationid_fk", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private NELocation neLocation;

	/** The ne source. */
	@Basic
	@Column(name = "nesource")
	private String neSource;

	/** The ne type. */
	@Basic
	@Column(name = "netype")
	@Enumerated(EnumType.STRING)
	private NEType neType;

	/** The ne name. */
	@Basic
	@Column(name = "nename")
	private String neName;

	/** The technology. */
	@Basic
	@Column(name = "technology")
	@Enumerated(EnumType.STRING)
	private Technology technology;

	/** The vendor. */
	@Basic
	@Column(name = "vendor")
	@Enumerated(EnumType.STRING)
	private Vendor vendor;

	/** The ne frequency. */
	@Basic
	@Column(name = "nefrequency")
	private String neFrequency;

	/** The is deleted. */
	@Basic
	@Column(name = "deleted")
	private Boolean isDeleted;

	/** The ne status. */
	@Basic
	@Column(name = "nestatus")
	@Enumerated(EnumType.STRING)
	private NEStatus neStatus;

	/** The latitude. */
	@Basic
	@Column(name = "latitude")
	private Double latitude;

	/** The longitude. */
	@Basic
	@Column(name = "longitude")
	private Double longitude;

	/** The geography L 4. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl4id_fk", nullable = true)
	private GeographyL4 geographyL4;

	/** The geography L 3. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl3id_fk", nullable = true)
	private GeographyL3 geographyL3;

	/** The geography L 2. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl2id_fk", nullable = true)
	private GeographyL2 geographyL2;

	/** The geography L 1. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl1id_fk", nullable = true)
	private GeographyL1 geographyL1;

	/** The created time. */
	@Basic
	@Column(name = "creationtime")
	private Date createdTime;

	/** The modified time. */
	@Basic
	@Column(name = "modificationtime")
	private Date modifiedTime;

	/** The ems server. */
	@JoinColumn(name = "emsserverid_fk", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private EmsServer emsServer;

	/** The domain. */
	@Basic
	@Column(name = "domain")
	@Enumerated(EnumType.STRING)
	private Domain domain;

	/** The software version. */
	@Basic
	@Column(name = "swversion")
	private String softwareVersion;

	/** The mnc. */
	@Basic
	@Column(name = "mnc")
	private Integer mnc;

	/** The mcc. */
	@Basic
	@Column(name = "mcc")
	private Integer mcc;

	/** The ne id. */
	@Basic
	@Column(name = "neid")
	private String neId;

	/** The pm ems id. */
	@Basic
	@Column(name = "pmemsid")
	private String pmEmsId;

	/** The fm ems id. */
	@Basic
	@Column(name = "fmemsid")
	private String fmEmsId;

	/** The cm ems id. */
	@Basic
	@Column(name = "cmemsid")
	private String cmEmsId;

	/** The ipv 4. */
	@Basic
	@Column(name = "ipv4")
	private String ipv4;

	/** The ipv 6. */
	@Basic
	@Column(name = "ipv6")
	private String ipv6;

	/** The model. */
	@Basic
	@Column(name = "model")
	private String model;

	/** The macaddress. */
	@Basic
	@Column(name = "macaddress")
	private String macaddress;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "salesl4id_fk", nullable = true)
	private SalesL4 salesL4;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "floorid_fk", nullable = true)
	private Floor floor;

	@Basic
	@Column(name = "cellnum")
	private Integer cellNum;

	@Basic
	@Column(name = "enbid")
	private Integer enbid;

	@Basic
	@Column(name = "ecgi")
	private String ecgi;

	@Basic
	@Column(name = "friendlyname")
	private String friendlyname;

	/** The duplex. */
	@Basic
	@Column(name = "duplex")
	private String duplex;

	@Basic
	@Enumerated(EnumType.STRING)
	@Column(name = "dayonestatus")
	private DayOneStatus status;

	@Basic
	@Column(name = "propertyid")
	private String propertyId;

	/** The radius */
	@Basic
	@Column(name = "radius")
	private Double radius;

	@Basic
	@Column(name = "nestage")
	private String neStage;

	@Basic
	@Column(name = "operationalstate")
	private String operationalState;

	@Basic
	@Column(name = "physicalserialnumber")
	private String physicalSerialNumber;

	@Column(name = "isvirtual")
	private Boolean virtual;

	@Column(name = "category")
	private String category;

	@Column(name = "secured")
	private Boolean secured;

	@Column(name = "hostname")
	private String hostname;

	@Column(name = "fqdn")
	private String fqdn;

	@Column(name="inservicedate")
	private Date inServiceDate;

	@Basic
	@Column(name="swbuildnumber")
	private String swBuildNumber;
	
	public Date getInServiceDate() {
		return inServiceDate;
	}

	public void setInServiceDate(Date inServiceDate) {
		this.inServiceDate = inServiceDate;
	}

	

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */

	public Integer getId() {
		return id;
	}

	public Floor getFloor() {
		return floor;
	}

	public void setFloor(Floor floor) {
		this.floor = floor;
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
	 * Gets the ne location.
	 *
	 * @return the ne location
	 */
	public NELocation getNeLocation() {
		return neLocation;
	}

	/**
	 * Sets the ne location.
	 *
	 * @param neLocation the new ne location
	 */
	public void setNeLocation(NELocation neLocation) {
		this.neLocation = neLocation;
	}

	/**
	 * Gets the ne source.
	 *
	 * @return the ne source
	 */
	public String getNeSource() {
		return neSource;
	}

	/**
	 * Sets the ne source.
	 *
	 * @param neSource the new ne source
	 */
	public void setNeSource(String neSource) {
		this.neSource = neSource;
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
	 * Gets the technology.
	 *
	 * @return the technology
	 */
	public Technology getTechnology() {
		return technology;
	}

	/**
	 * Sets the technology.
	 *
	 * @param technology the new technology
	 */
	public void setTechnology(Technology technology) {
		this.technology = technology;
	}

	/**
	 * Gets the ne frequency.
	 *
	 * @return the ne frequency
	 */
	public String getNeFrequency() {
		return neFrequency;
	}

	/**
	 * Sets the ne frequency.
	 *
	 * @param neFrequency the new ne frequency
	 */
	public void setNeFrequency(String neFrequency) {
		this.neFrequency = neFrequency;
	}

	/**
	 * Gets the checks if is deleted.
	 *
	 * @return the checks if is deleted
	 */
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	/**
	 * Sets the checks if is deleted.
	 *
	 * @param isDeleted the new checks if is deleted
	 */
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
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
	 * Gets the cluster.
	 *
	 * @return the cluster
	 */
	public GeographyL4 getCluster() {
		return geographyL4;
	}

	/**
	 * Sets the cluster.
	 *
	 * @param cluster the new cluster
	 */
	public void setCluster(GeographyL4 cluster) {
		this.geographyL4 = cluster;
	}

	/**
	 * Gets the created time.
	 *
	 * @return the created time
	 */
	public Date getCreatedTime() {
		return createdTime;
	}

	/**
	 * Sets the created time.
	 *
	 * @param createdTime the new created time
	 */
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	/**
	 * Gets the modified time.
	 *
	 * @return the modified time
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}

	/**
	 * Sets the modified time.
	 *
	 * @param modifiedTime the new modified time
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	/**
	 * Gets the ems server.
	 *
	 * @return the ems server
	 */
	public EmsServer getEmsServer() {
		return emsServer;
	}

	/**
	 * Sets the ems server.
	 *
	 * @param emsServer the new ems server
	 */
	public void setEmsServer(EmsServer emsServer) {
		this.emsServer = emsServer;
	}

	/**
	 * Gets the geography L 4.
	 *
	 * @return the geography L 4
	 */
	public GeographyL4 getGeographyL4() {
		return geographyL4;
	}

	/**
	 * Sets the geography L 4.
	 *
	 * @param geographyL4 the new geography L 4
	 */
	public void setGeographyL4(GeographyL4 geographyL4) {
		this.geographyL4 = geographyL4;
	}

	public GeographyL3 getGeographyL3() {
		return geographyL3;
	}

	public void setGeographyL3(GeographyL3 geographyL3) {
		this.geographyL3 = geographyL3;
	}

	public GeographyL2 getGeographyL2() {
		return geographyL2;
	}

	public void setGeographyL2(GeographyL2 geographyL2) {
		this.geographyL2 = geographyL2;
	}

	public GeographyL1 getGeographyL1() {
		return geographyL1;
	}

	public void setGeographyL1(GeographyL1 geographyL1) {
		this.geographyL1 = geographyL1;
	}

	public SalesL4 getSalesL4() {
		return salesL4;
	}

	public void setSalesL4(SalesL4 salesL4) {
		this.salesL4 = salesL4;
	}

	/**
	 * Gets the ne type.
	 *
	 * @return the ne type
	 */
	public NEType getNeType() {
		return neType;
	}

	/**
	 * Sets the ne type.
	 *
	 * @param neType the new ne type
	 */
	public void setNeType(NEType neType) {
		this.neType = neType;
	}

	/**
	 * Gets the software version.
	 *
	 * @return the software version
	 */
	public String getSoftwareVersion() {
		return softwareVersion;
	}

	/**
	 * Sets the software version.
	 *
	 * @param softwareVersion the new software version
	 */
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}

	/**
	 * Gets the mnc.
	 *
	 * @return the mnc
	 */
	public Integer getMnc() {
		return mnc;
	}

	/**
	 * Sets the mnc.
	 *
	 * @param mnc the new mnc
	 */
	public void setMnc(Integer mnc) {
		this.mnc = mnc;
	}

	/**
	 * Gets the mcc.
	 *
	 * @return the mcc
	 */
	public Integer getMcc() {
		return mcc;
	}

	/**
	 * Sets the mcc.
	 *
	 * @param mcc the new mcc
	 */
	public void setMcc(Integer mcc) {
		this.mcc = mcc;
	}

	/**
	 * Sets the pm ems id.
	 *
	 * @param pmEmsId the new pm ems id
	 */
	public void setPmEmsId(String pmEmsId) {
		this.pmEmsId = pmEmsId;
	}

	/**
	 * Gets the pm ems id.
	 *
	 * @return the pm ems id
	 */
	public String getPmEmsId() {
		return pmEmsId;
	}

	/**
	 * Sets the fm ems id.
	 *
	 * @param fmEmsId the new fm ems id
	 */
	public void setFmEmsId(String fmEmsId) {
		this.fmEmsId = fmEmsId;
	}

	/**
	 * Gets the fm ems id.
	 *
	 * @return the fm ems id
	 */
	public String getFmEmsId() {
		return fmEmsId;
	}

	/**
	 * Sets the cm ems id.
	 *
	 * @param cmEmsId the new cm ems id
	 */
	public void setCmEmsId(String cmEmsId) {
		this.cmEmsId = cmEmsId;
	}

	/**
	 * Gets the cm ems id.
	 *
	 * @return the cm ems id
	 */
	public String getCmEmsId() {
		return cmEmsId;
	}

	/**
	 * Gets the radius.
	 *
	 * @return the radius
	 */

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	/**
	 * Instantiates a new network element.
	 */
	public NetworkElement() {
		super();
	}

	/**
	 * Instantiates a new network element.
	 *
	 * @param sapid         the sapid
	 * @param frequencyBand the frequency band
	 * @param isDeleted     the is deleted
	 * @param city          the city
	 * @param latitude      the latitude
	 * @param longitude     the longitude
	 * @param siteType      the site type
	 * @param status        the status
	 * @param source        the source
	 * @param circle        the circle
	 * @param vendor        the vendor
	 */
	// macro
	public NetworkElement(String sapid, String frequencyBand, Boolean isDeleted, GeographyL3 city, Double latitude,
			Double longitude, String siteType, NEStatus status, String source, GeographyL2 circle, Vendor vendor) {
		this.neName = sapid;
		this.neFrequency = frequencyBand;
		this.isDeleted = isDeleted;
		this.latitude = latitude;
		this.longitude = longitude;
		if (siteType != null)
			this.neType = neType.valueOf(siteType);
		this.neStatus = status;
		this.neSource = source;
		this.vendor = vendor;
		this.createdTime = new Date();
		this.modifiedTime = new Date();
		this.technology = Technology.LTE;
	}

	/**
	 * Instantiates a new network element.
	 *
	 * @param sapid     the sapid
	 * @param neType    the ne type
	 * @param status    the status
	 * @param latitude  the latitude
	 * @param longitude the longitude
	 * @param city      the city
	 * @param cluster   the cluster
	 * @param circle    the circle
	 */
	public NetworkElement(String sapid, String neType, NEStatus status, Double latitude, Double longitude,
			GeographyL3 city, GeographyL4 cluster, GeographyL2 circle) {
		this.neName = sapid;
		if (neType != null)
			this.neType = NEType.valueOf(neType);
		this.neStatus = status;
		this.latitude = latitude;
		this.longitude = longitude;
		this.geographyL4 = cluster;
		this.createdTime = new Date();
		this.modifiedTime = new Date();
		this.technology = Technology.LTE;
		this.vendor = Vendor.SAMSUNG;
	}

	/**
	 * Instantiates a new network element.
	 *
	 * @param sapid     the sapid
	 * @param siteType  the site type
	 * @param city      the city
	 * @param latitude  the latitude
	 * @param longitude the longitude
	 * @param status    the status
	 * @param band      the band
	 * @param cluster   the cluster
	 * @param circle    the circle
	 */
	public NetworkElement(String sapid, com.inn.foresight.core.infra.utils.enums.SiteType siteType, GeographyL3 city,
			Double latitude, Double longitude, SmallCellStatus status, String band, GeographyL4 cluster,
			GeographyL2 circle) {
		this.neName = sapid;
		if (siteType != null) {
			this.neType = NEType.valueOf(siteType.toString());
		}
		this.geographyL4 = cluster;
		this.latitude = latitude;
		this.longitude = longitude;
		if (status != null) {
			this.neStatus = NEStatus.valueOf(status.toString());
		}
		this.neFrequency = band;
		this.createdTime = new Date();
		this.modifiedTime = new Date();
		this.technology = Technology.LTE;
		if (SiteType.SMALL_CELL_INDOOR.equals(siteType)) {
			this.vendor = Vendor.AIRSPAN;
		} else if (SiteType.SMALL_CELL_OUTDOOR.equals(siteType)) {
			this.vendor = Vendor.SAMSUNG;
		}
	}

	/**
	 * Instantiates a new network element.
	 *
	 * @param sapid         the sapid
	 * @param band          the band
	 * @param isDeleted     the is deleted
	 * @param city          the city
	 * @param latitude      the latitude
	 * @param longitude     the longitude
	 * @param siteType      the site type
	 * @param technology    the technology
	 * @param retSource     the ret source
	 * @param circle        the circle
	 * @param vendor        the vendor
	 * @param cluster       the cluster
	 * @param progressState the progress state
	 * @param cellId        the cell id
	 * @param sector        the sector
	 * @param cNum          the c num
	 * @param enodBId       the enod B id
	 */
	// sitedetail
	public NetworkElement(String sapid, Integer band, Boolean isDeleted, GeographyL3 city, Double latitude,
			Double longitude, String siteType, Technology technology, String retSource, GeographyL2 circle,
			Vendor vendor, GeographyL4 cluster, ProgressState progressState, Integer cellId, Integer sector,
			Integer cNum, Integer enodBId) {
		this.neName = sapid;
		if (band != null) {
			this.neFrequency = band.toString();
		}
		this.isDeleted = isDeleted;
		this.latitude = latitude;
		this.longitude = longitude;
		if (siteType != null)
			this.neType = NEType.valueOf(siteType);
		if (progressState != null) {
			this.neStatus = NEStatus.valueOf(progressState.toString());
		}
		this.neSource = retSource;
		this.vendor = vendor;
		this.createdTime = new Date();
		this.modifiedTime = new Date();
		this.technology = technology;
		this.geographyL4 = cluster;
	}

	/**
	 * Gets the vendor.
	 *
	 * @return the vendor
	 */
	public Vendor getVendor() {
		return vendor;
	}

	/**
	 * Sets the vendor.
	 *
	 * @param vendor the new vendor
	 */
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	/**
	 * Gets the domain.
	 *
	 * @return the domain
	 */
	public Domain getDomain() {
		return domain;
	}

	/**
	 * Sets the domain.
	 *
	 * @param domain the new domain
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	/**
	 * Gets the ne status.
	 *
	 * @return the ne status
	 */
	public NEStatus getNeStatus() {
		return neStatus;
	}

	/**
	 * Sets the ne status.
	 *
	 * @param neStatus the new ne status
	 */
	public void setNeStatus(NEStatus neStatus) {
		this.neStatus = neStatus;
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
	 * Gets the ipv 4.
	 *
	 * @return the ipv 4
	 */
	public String getIpv4() {
		return ipv4;
	}

	/**
	 * Sets the ipv 4.
	 *
	 * @param ipv4 the new ipv 4
	 */
	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}

	/**
	 * Gets the ipv 6.
	 *
	 * @return the ipv 6
	 */
	public String getIpv6() {
		return ipv6;
	}

	/**
	 * Sets the ipv 6.
	 *
	 * @param ipv6 the new ipv 6
	 */
	public void setIpv6(String ipv6) {
		this.ipv6 = ipv6;
	}

	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets the model.
	 *
	 * @param model the new model
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Gets the macaddress.
	 *
	 * @return the macaddress
	 */
	public String getMacaddress() {
		return macaddress;
	}

	/**
	 * Sets the macaddress.
	 *
	 * @param macaddress the new macaddress
	 */
	public void setMacaddress(String macaddress) {
		this.macaddress = macaddress;
	}

	public Integer getCellNum() {
		return cellNum;
	}

	public void setCellNum(Integer cellNum) {
		this.cellNum = cellNum;
	}

	public Integer getEnbid() {
		return enbid;
	}

	public void setEnbid(Integer enbid) {
		this.enbid = enbid;
	}

	public String getEcgi() {
		return ecgi;
	}

	public void setEcgi(String ecgi) {
		this.ecgi = ecgi;
	}

	public String getFriendlyname() {
		return friendlyname;
	}

	public void setFriendlyname(String friendlyname) {
		this.friendlyname = friendlyname;
	}

	public String getDuplex() {
		return duplex;
	}

	public void setDuplex(String duplex) {
		this.duplex = duplex;
	}

	public NetworkElement getParentNE() {
		return parentNE;
	}

	public void setParentNE(NetworkElement parentNE) {
		this.parentNE = parentNE;
	}

	public DayOneStatus getStatus() {
		return status;
	}

	public void setStatus(DayOneStatus status) {
		this.status = status;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getNeStage() {
		return neStage;
	}

	public void setNeStage(String neStage) {
		this.neStage = neStage;
	}

	public String getOperationalState() {
		return operationalState;
	}

	public void setOperationalState(String operationalState) {
		this.operationalState = operationalState;
	}

	public String getPhysicalSerialNumber() {
		return physicalSerialNumber;
	}

	public void setPhysicalSerialNumber(String physicalSerialNumber) {
		this.physicalSerialNumber = physicalSerialNumber;
	}

	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Boolean getVirtual() {
		return virtual;
	}

	public void setVirtual(Boolean virtual) {
		this.virtual = virtual;
	}

	public Boolean getSecured() {
		return secured;
	}

	public void setSecured(Boolean secured) {
		this.secured = secured;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getFqdn() {
		return fqdn;
	}

	public void setFqdn(String fqdn) {
		this.fqdn = fqdn;
	}
	
	public String getSwBuildNumber() {
		return swBuildNumber;
	}

	public void setSwBuildNumber(String swBuildNumber) {
		this.swBuildNumber = swBuildNumber;
	}

	@Override
	public String toString() {
		return "NetworkElement [id=" + id + ", networkElement=" + networkElement + ", parentNE=" + parentNE + ", neLocation=" + neLocation + ", neSource=" + neSource + ", neType=" + neType
				+ ", neName=" + neName + ", technology=" + technology + ", vendor=" + vendor + ", neFrequency=" + neFrequency + ", isDeleted=" + isDeleted + ", neStatus=" + neStatus + ", latitude="
				+ latitude + ", longitude=" + longitude + ", geographyL4=" + geographyL4 + ", geographyL3=" + geographyL3 + ", geographyL2=" + geographyL2 + ", geographyL1=" + geographyL1
				+ ", createdTime=" + createdTime + ", modifiedTime=" + modifiedTime + ", emsServer=" + emsServer + ", domain=" + domain + ", softwareVersion=" + softwareVersion + ", mnc=" + mnc
				+ ", mcc=" + mcc + ", neId=" + neId + ", pmEmsId=" + pmEmsId + ", fmEmsId=" + fmEmsId + ", cmEmsId=" + cmEmsId + ", ipv4=" + ipv4 + ", ipv6=" + ipv6 + ", model=" + model
				+ ", macaddress=" + macaddress + ", salesL4=" + salesL4 + ", floor=" + floor + ", cellNum=" + cellNum + ", enbid=" + enbid + ", ecgi=" + ecgi + ", friendlyname=" + friendlyname
				+ ", duplex=" + duplex + ", status=" + status + ", propertyId=" + propertyId + ", radius=" + radius + ", neStage=" + neStage + ", operationalState=" + operationalState
				+ ", physicalSerialNumber=" + physicalSerialNumber + ", virtual=" + virtual + ", category=" + category + ", secured=" + secured + ", hostname=" + hostname + ", fqdn=" + fqdn
				+ ", inServiceDate=" + inServiceDate + ", swBuildNumber=" + swBuildNumber + "]";
	}

	

}
