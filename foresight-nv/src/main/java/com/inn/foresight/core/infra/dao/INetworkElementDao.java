package com.inn.foresight.core.infra.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Tuple;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.infra.model.NEBandDetail;
import com.inn.foresight.core.infra.model.NETaskDetail;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.model.RANDetail;
import com.inn.foresight.core.infra.utils.enums.DayOneStatus;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.utils.enums.Technology;
import com.inn.foresight.core.infra.utils.enums.Vendor;
import com.inn.foresight.core.infra.wrapper.MicroSiteDataWrapper;
import com.inn.foresight.core.infra.wrapper.NEDetailWrapper;
import com.inn.foresight.core.infra.wrapper.NETaskDetailWrapper;
import com.inn.foresight.core.infra.wrapper.NetworkElementCellDetailWrapper;
import com.inn.foresight.core.infra.wrapper.NetworkElementOnAirJsiWrapper;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;
import com.inn.foresight.core.infra.wrapper.NetworkHierarchyDetail;
import com.inn.foresight.core.infra.wrapper.SiteCountWrapper;
import com.inn.foresight.core.infra.wrapper.SiteEmsDetailsWrapper;
import com.inn.foresight.core.infra.wrapper.SiteGeographicalDetail;
import com.inn.foresight.core.infra.wrapper.TotalSiteLayerWiseWrapper;
import com.inn.product.um.geography.model.GeographyL1;

/** The Interface INetworkElementDao. */
public interface INetworkElementDao extends IGenericDao<Integer, NetworkElement> {
	/**
	 * Gets the all site count zone wise.
	 *
	 * @return the all site count zone wise
	 * @throws DaoException the dao exception
	 */
	List<TotalSiteLayerWiseWrapper> getAllSiteCountZoneWise();

	/**
	 * Gets the NE for small site.
	 *
	 * @return the NE for small site
	 */
	List<NetworkElement> getNEForSmallSite();

	/**
	 * Gets the sap ids by geography L 4.
	 *
	 * @param geographyL4 the geography L 4
	 * @return the sap ids by geography L 4
	 */
	List<String> getSapIdsByGeographyL4(String geographyL4);

	/**
	 * Gets the location for cell id mnc mcc.
	 *
	 * @return the location for cell id mnc mcc
	 */
	List<Object[]> getLocationForCellIdMncMcc();

	/**
	 * Gets the site table data for geography L 2.
	 *
	 * @param southWestLong the south west long
	 * @param southWestLat  the south west lat
	 * @param northEastLong the north east long
	 * @param northEastLat  the north east lat
	 * @param progressState the progress state
	 * @param neType        the ne type
	 * @param neFrequencies the ne frequencies
	 * @return the site table data for geography L 2
	 * @throws DaoException the dao exception
	 */
	List<NetworkElementWrapper> getSiteTableDataForGeographyL2(Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat, String progressState, NEType neType, List<String> neFrequencies);

	/**
	 * Gets the site table data for geography L 3.
	 *
	 * @param southWestLong the south west long
	 * @param southWestLat  the south west lat
	 * @param northEastLong the north east long
	 * @param northEastLat  the north east lat
	 * @param progressState the progress state
	 * @param neType        the ne type
	 * @param neFrequencies the ne frequencies
	 * @return the site table data for geography L 3
	 * @throws DaoException the dao exception
	 */
	List<NetworkElementWrapper> getSiteTableDataForGeographyL3(Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat, String progressState, NEType neType, List<String> neFrequencies);

	/**
	 * Gets the site table data for geography L 4.
	 *
	 * @param southWestLong the south west long
	 * @param southWestLat  the south west lat
	 * @param northEastLong the north east long
	 * @param northEastLat  the north east lat
	 * @param progressState the progress state
	 * @param neType        the ne type
	 * @param neFrequencies the ne frequencies
	 * @return the site table data for geography L 4
	 * @throws DaoException the dao exception
	 */
	List<NetworkElementWrapper> getSiteTableDataForGeographyL4(Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat, String progressState, NEType neType, List<String> neFrequencies);

	/**
	 * Gets the site overview detail data.
	 *
	 * @param domain     the domain
	 * @param vendor     the vendor
	 * @param technology the technology
	 * @return the site overview detail data
	 * @throws DaoException the dao exception
	 */

	/**
	 * Gets the distinct band by domain vendor tech.
	 *
	 * @param domain     the domain
	 * @param vendor     the vendor
	 * @param technology the technology
	 * @return the distinct band by domain vendor tech
	 * @throws DaoException the dao exception
	 */
	List<Object[]> getDistinctBandByDomainVendorTech(String domain, String vendor, String technology);

	/**
	 * Gets the distinct version.
	 *
	 * @param domain the domain
	 * @param vendor the vendor
	 * @return the distinct version
	 * @throws DaoException the dao exception
	 */
	List<String> getDistinctVersion(String domain, String vendor);

	/**
	 * Gets the distinct vendor.
	 *
	 * @param domain          the domain
	 * @param technology      the technology
	 * @param softwareVersion the software version
	 * @return the distinct vendor
	 */
	List<Vendor> getDistinctVendor(String domain, String technology, String softwareVersion);

	/**
	 * Gets the distinct technology.
	 *
	 * @param domain          the domain
	 * @param vendor          the vendor
	 * @param softwareVersion the software version
	 * @return the distinct technology
	 */
	List<Technology> getDistinctTechnology(String domain, String vendor, String softwareVersion);

	/**
	 * Gets the distinct domain.
	 *
	 * @return the distinct domain
	 */
	List<Domain> getDistinctDomain();

	/**
	 * Gets the distinct info from network element.
	 *
	 * @return the distinct info from network element
	 * @throws DaoException the dao exception
	 */
	List<NetworkElementWrapper> getDistinctInfoFromNetworkElement();

	/**
	 * Gets the distinct soft ware version.
	 *
	 * @param domain     the domain
	 * @param vendor     the vendor
	 * @param technology the technology
	 * @return the distinct soft ware version
	 */
	List<String> getDistinctSoftWareVersion(String domain, String vendor, String technology);

	/**
	 * Gets the distinct vendor by domain.
	 *
	 * @param domain the domain
	 * @return the distinct vendor by domain
	 */
	List<Vendor> getDistinctVendorByDomain(Domain domain);

	/**
	 * Gets the NE by name domain vendor tech.
	 *
	 * @param nename     the nename
	 * @param domain     the domain
	 * @param vendor     the vendor
	 * @param technology the technology
	 * @return the NE by name domain vendor tech
	 */
	NetworkElement getNEByNameDomainVendorTech(String nename, String domain, String vendor, String technology);

	/**
	 * Gets the distinct SW version from network element.
	 *
	 * @param domain     the domain
	 * @param vendor     the vendor
	 * @param technology the technology
	 * @return the distinct SW version from network element
	 */
	List<Object> getDistinctSWVersionFromNetworkElement(String domain, String vendor, String technology);

	/**
	 * Gets the distinct NE type from network element.
	 *
	 * @param domain          the domain
	 * @param vendor          the vendor
	 * @param technology      the technology
	 * @param softwareVersion the software version
	 * @return the distinct NE type from network element
	 */
	List<Object> getDistinctNETypeFromNetworkElement(String domain, String vendor, String technology,
			String softwareVersion);

	/**
	 * Gets the distinct vendor from network element.
	 *
	 * @param domain the domain
	 * @return the distinct vendor from network element
	 */
	List<Object> getDistinctVendorFromNetworkElement(String domain);

	/**
	 * Gets the network element.
	 *
	 * @param netype          the netype
	 * @param geographyId     the geography id
	 * @param geoType         the geo type
	 * @param domain          the domain
	 * @param vendor          the vendor
	 * @param technology      the technology
	 * @param softwareVersion the software version
	 * @return the network element
	 */
	List<Object[]> getNetworkElement(String netype, Integer geographyId, String geoType, String domain, String vendor,
			String technology, String softwareVersion);

	/**
	 * Gets the NE info by ne id.
	 *
	 * @param domain          the domain
	 * @param vendor          the vendor
	 * @param softwareVersion the software version
	 * @param technology      the technology
	 * @param neId            the ne id
	 * @return the NE info by ne id
	 */
	List<NetworkElementWrapper> getNEInfoByNeId(String domain, String vendor, String softwareVersion, String technology,
			String neId);

	/**
	 * Gets the network elements for cell level detail.
	 *
	 * @param neTypeList      the ne type list
	 * @param neNameList      the ne name list
	 * @param neFrequencyList the ne frequency list
	 * @param neStatusList    the ne status list
	 * @param vendorList      the vendor list
	 * @param technologyList  the technology list
	 * @param domainList      the domain list
	 * @param viewportMap     the viewport map
	 * @param geographyNames  the geography names
	 * @param neIdList        the ne id list
	 * @return the network elements for cell level detail
	 * @throws DaoException the dao exception
	 */
	List<NetworkElement> getNetworkElementsForCellLevelDetail(List<NEType> neTypeList, List<String> neNameList,
			List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList,
			List<Technology> technologyList, List<Domain> domainList, Map<String, Double> viewportMap,
			Map<String, List<String>> geographyNames, List<String> neIdList);

	/**
	 * Gets the network element data by NE id.
	 *
	 * @param neId the ne id
	 * @return the network element data by NE id
	 * @throws DaoException the dao exception
	 */
	NetworkElement getNetworkElementDataByNEId(String neId);

	/**
	 * Gets the network elements for site level detail.
	 *
	 * @param neTypeList      the ne type list
	 * @param neNameList      the ne name list
	 * @param neFrequencyList the ne frequency list
	 * @param neStatusList    the ne status list
	 * @param vendorList      the vendor list
	 * @param technologyList  the technology list
	 * @param domainList      the domain list
	 * @param viewportMap     the viewport map
	 * @param geographyNames  the geography names
	 * @return the network elements for site level detail
	 * @throws DaoException the dao exception
	 */
	List<NetworkElement> getNetworkElementsForSiteLevelDetail(List<NEType> neTypeList, List<String> neNameList,
			List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList,
			List<Technology> technologyList, List<Domain> domainList, Map<String, Double> viewportMap,
			Map<String, List<String>> geographyNames);

	/**
	 * Gets the distinct technology from network element.
	 *
	 * @param domain the domain
	 * @param vendor the vendor
	 * @return the distinct technology from network element
	 */
	List<Object> getDistinctTechnologyFromNetworkElement(String domain, String vendor);

	/**
	 * Search all network element.
	 *
	 * @param siteName the site name
	 * @param llimit   the llimit
	 * @param ulimit   the ulimit
	 * @return the list
	 * @throws DaoException the dao exception
	 */
	List<Object[]> searchAllNetworkElement(String siteName, Integer llimit, Integer ulimit);

	/**
	 * Gets the site info by sap id.
	 *
	 * @param siteID the site ID
	 * @return the site info by sap id
	 */
	NetworkElementWrapper getSiteInfoBySapId(String siteID);

	/**
	 * Gets the site info by sap id.
	 *
	 * @param siteID the site ID
	 * @return the site GeographyL4 info by sap id
	 * @throws DaoException the dao exception
	 */
	NetworkElementWrapper getGeographyL4BySapId(String siteID);

	/**
	 * Gets the network element data for NE.
	 *
	 * @param neName the ne name
	 * @return the network element data for NE
	 * @throws DaoException the dao exception
	 */
	List<NEBandDetail> getNetworkElementDataForNE(String neName);

	/**
	 * Gets the aggregate count for wifi.
	 *
	 * @param neType          the ne type
	 * @param neStatus        the ne status
	 * @param southWestLong   the south west long
	 * @param southWestLat    the south west lat
	 * @param northEastLong   the north east long
	 * @param northEastLat    the north east lat
	 * @param geographyLevel  the geography level
	 * @param neFrequencyList the ne frequency list
	 * @param siteCategory    the site category
	 * @param taskStatus      the task status
	 * @return the aggregate count for wifi
	 * @throws DaoException the dao exception
	 */
	List<NetworkElementWrapper> getAggregateCountForWifi(List<NEType> neType, String neStatus, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, String geographyLevel,
			List<String> neFrequencyList, List<String> siteCategory, List<String> taskStatus);

	/**
	 * Gets the actual count for wifi.
	 *
	 * @param neType          the ne type
	 * @param neStatus        the ne status
	 * @param southWestLong   the south west long
	 * @param southWestLat    the south west lat
	 * @param northEastLong   the north east long
	 * @param northEastLat    the north east lat
	 * @param neFrequencyList the ne frequency list
	 * @param siteCategory    the site category
	 * @param taskStatus      the task status
	 * @return the actual count for wifi
	 * @throws DaoException the dao exception
	 */
	List<NetworkElementWrapper> getActualCountForWifi(List<NEType> neType, String neStatus, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, List<String> neFrequencyList,
			List<String> siteCategory, List<String> taskStatus);

	/**
	 * Gets the ne frequency by domain and vendor.
	 *
	 * @param domain the domain
	 * @param vendor the vendor
	 * @return the ne frequency by domain and vendor
	 * @throws DaoException the dao exception
	 */
	List<String> getNeFrequencyByDomainAndVendor(String domain, String vendor);

	/**
	 * Gets the alarm count circle wise.
	 *
	 * @param swLat the sw lat
	 * @param swLng the sw lng
	 * @param neLat the ne lat
	 * @param neLng the ne lng
	 * @return the alarm count circle wise
	 * @throws DaoException the dao exception
	 */
	List<TotalSiteLayerWiseWrapper> getAlarmCountCircleWise(Double swLat, Double swLng, Double neLat, Double neLng);

	/**
	 * Gets the NE detail by NE id.
	 *
	 * @param neId the ne id
	 * @return the NE detail by NE id
	 * @throws DaoException the dao exception
	 */
	NetworkElement getNEDetailByNEId(String neId);

	/**
	 * Gets the distinct domain from network element.
	 *
	 * @return the distinct domain from network element
	 */
	List<Object> getDistinctDomainFromNetworkElement();

	/**
	 * Gets the distinct technology by domain vendor.
	 *
	 * @param domain the domain
	 * @param vendor the vendor
	 * @return the distinct technology by domain vendor
	 */
	List<Technology> getDistinctTechnologyByDomainVendor(Domain domain, Vendor vendor);

	/**
	 * Gets the NE detail by NE name.
	 *
	 * @param neNameList the ne name list
	 * @return the NE detail by NE name
	 * @throws DaoException the dao exception
	 */
	List<NetworkElement> getNEDetailByNEName(List<String> neNameList);

	/**
	 * Gets the ems detail data for sites.
	 *
	 * @param neName the ne name
	 * @return the ems detail data for sites
	 */
	List<SiteEmsDetailsWrapper> getEmsDetailDataForSites(String neName);

	/**
	 * Gets the NE task detail for NE.
	 *
	 * @param neName the ne name
	 * @return the NE task detail for NE
	 */
	List<NETaskDetail> getNETaskDetailForNE(String neName);

	/**
	 * Gets the total site count.
	 *
	 * @param swLat the sw lat
	 * @param swLng the sw lng
	 * @param neLat the ne lat
	 * @param neLng the ne lng
	 * @return the total site count
	 * @throws DaoException the dao exception
	 */
	List<TotalSiteLayerWiseWrapper> getTotalSiteCount(Double swLat, Double swLng, Double neLat, Double neLng);

	/**
	 * Gets the alarm count city wise.
	 *
	 * @param swLat the sw lat
	 * @param swLng the sw lng
	 * @param neLat the ne lat
	 * @param neLng the ne lng
	 * @return the alarm count city wise
	 * @throws DaoException the dao exception
	 */
	List<TotalSiteLayerWiseWrapper> getAlarmCountCityWise(Double swLat, Double swLng, Double neLat, Double neLng);

	/**
	 * Gets the NE detail by NE id list.
	 *
	 * @param neIdList the ne id list
	 * @return the NE detail by NE id list
	 * @throws DaoException the dao exception
	 */
	List<NetworkElement> getNEDetailByNEIdList(List<String> neIdList);

	/**
	 * Gets the all NE of geography L 3.
	 *
	 * @return the all NE of geography L 3
	 * @throws DaoException the dao exception
	 */
	List<NetworkElement> getAllNEOfGeographyL3();

	/**
	 * Gets the count by georaphy and vendor.
	 *
	 * @param vendor         the vendor
	 * @param geographyName  the geography name
	 * @param geographyLevel the geography level
	 * @return the count by georaphy and vendor
	 * @throws DaoException the dao exception
	 */
	List<NetworkElementWrapper> getCountByGeoraphyAndVendor(Vendor vendor, List<String> geographyName,
			String geographyLevel);

	/**
	 * Gets the network element by domain vendor technology and N ename.
	 *
	 * @param nename     the nename
	 * @param domain     the domain
	 * @param vendor     the vendor
	 * @param technology the technology
	 * @return the network element by domain vendor technology and N ename
	 */
	NetworkElement getNetworkElementByDomainVendorTechnologyAndNEname(String nename, Domain domain, Vendor vendor,
			Technology technology);

	/**
	 * Gets the site overview detail data.
	 *
	 * @param neName the ne name
	 * @return the site overview detail data
	 * @throws DaoException the dao exception
	 */
	SiteGeographicalDetail getSiteOverviewDetailData(String neName);

	/**
	 * Gets the ne id count by geography.
	 *
	 * @param geoNameList the geo name list
	 * @param geoType     the geo type
	 * @return the ne id count by geography
	 */
	Integer getNeIdCountByGeography(List<String> geoNameList, String geoType);

	// List<String> advancedSearchToNeId(String neId, NEType neType);

	/**
	 * Gets the cell ids by geography L 4.
	 *
	 * @param geographyL4 the geography L 4
	 * @return the cell ids by geography L 4
	 */
	List<String> getCellIdsByGeographyL4(String geographyL4);

	/**
	 * Gets the NE data by boundary min max.
	 *
	 * @param minLat   the min lat
	 * @param maxLat   the max lat
	 * @param minLon   the min lon
	 * @param maxLon   the max lon
	 * @param neType   the ne type
	 * @param neStatus the ne status
	 * @return the NE data by boundary min max
	 */
	List<NetworkElement> getNEDataByBoundaryMinMax(Double minLat, Double maxLat, Double minLon, Double maxLon,
			List<NEType> neType, List<NEStatus> neStatus);

	/**
	 * Gets the distinct geography.
	 *
	 * @param geograhyLevel the geograhy level
	 * @param southWestLong the south west long
	 * @param southWestLat  the south west lat
	 * @param northEastLong the north east long
	 * @param northEastLat  the north east lat
	 * @return the distinct geography
	 */
	List<String> getDistinctGeography(String geograhyLevel, Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat);

	/**
	 * Gets the NE by domain vendor and L 4 name.
	 *
	 * @param domain the domain
	 * @param vendor the vendor
	 * @param l4Name the l 4 name
	 * @param neId   the ne id
	 * @return the NE by domain vendor and L 4 name
	 */
	List<NetworkElement> getNEByDomainVendorAndL4Name(String domain, String vendor, String l4Name, String neId);

	/**
	 * Gets the actual planned sites.
	 *
	 * @param neType          the ne type
	 * @param neStatus        the ne status
	 * @param southWestLong   the south west long
	 * @param southWestLat    the south west lat
	 * @param northEastLong   the north east long
	 * @param northEastLat    the north east lat
	 * @param neFrequencyList the ne frequency list
	 * @param siteCategory    the site category
	 * @param taskStatus      the task status
	 * @param vendor          the vendor
	 * @param technologies    the technologies
	 * @param morphology      the morphology
	 * @return the actual planned sites
	 */
	List<NetworkElementWrapper> getActualPlannedSites(String neType, String neStatus, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, List<String> neFrequencyList,
			List<String> siteCategory, List<String> taskStatus, List<Vendor> vendor, List<Technology> technologies,
			List<String> morphology);

	/**
	 * Gets the network element for aggregate layer count.
	 *
	 * @param neType          the ne type
	 * @param neStatus        the ne status
	 * @param southWestLong   the south west long
	 * @param southWestLat    the south west lat
	 * @param northEastLong   the north east long
	 * @param northEastLat    the north east lat
	 * @param geographyLevel  the geography level
	 * @param neFrequencyList the ne frequency list
	 * @param siteCategory    the site category
	 * @param taskStatus      the task status
	 * @param vendor          the vendor
	 * @param technologies    the technologies
	 * @param geographyList   the geography list
	 * @param morphology      the morphology
	 * @return the network element for aggregate layer count
	 */
	List<NetworkElementWrapper> getNetworkElementForAggregateLayerCount(String neType, String neStatus,
			Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat, String geographyLevel,
			List<String> neFrequencyList, List<String> siteCategory, List<String> taskStatus, List<Vendor> vendor,
			List<Technology> technologies, List<String> geographyList, List<String> morphology);

	/**
	 * Gets the network element for table view.
	 *
	 * @param neType          the ne type
	 * @param neStatus        the ne status
	 * @param southWestLong   the south west long
	 * @param southWestLat    the south west lat
	 * @param northEastLong   the north east long
	 * @param northEastLat    the north east lat
	 * @param geographyLevel  the geography level
	 * @param neFrequencyList the ne frequency list
	 * @param siteCategory    the site category
	 * @param taskStatus      the task status
	 * @param vendor          the vendor
	 * @param technologies    the technologies
	 * @param geographyList   the geography list
	 * @param morphology      the morphology
	 * @return the network element for table view
	 */
	List<NetworkElementWrapper> getNetworkElementForTableView(String neType, String neStatus, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, String geographyLevel,
			List<String> neFrequencyList, List<String> siteCategory, List<String> taskStatus, List<Vendor> vendor,
			List<Technology> technologies, List<String> geographyList, List<String> morphology);

	/**
	 * Gets the ne macro cell data.
	 *
	 * @param neName the ne name
	 * @param neType the ne type
	 * @return the ne macro cell data
	 */
	List<NetworkElement> getNeMacroCellData(String neName, NEType neType);

	/**
	 * Gets the network element for actual sites.
	 *
	 * @param neType          the ne type
	 * @param neStatus        the ne status
	 * @param southWestLong   the south west long
	 * @param southWestLat    the south west lat
	 * @param northEastLong   the north east long
	 * @param northEastLat    the north east lat
	 * @param neFrequencyList the ne frequency list
	 * @param siteCategory    the site category
	 * @param taskStatus      the task status
	 * @param criteria        the criteria
	 * @param failureRate     the failure rate
	 * @param vendor          the vendor
	 * @param technologies    the technologies
	 * @param morphology      the morphology
	 * @param geography       the geography
	 * @return the network element for actual sites
	 */
	List<NetworkElementWrapper> getNetworkElementForActualSites(String neType, String neStatus, Double southWestLong,
			Double southWestLat, Double northEastLong, Double northEastLat, List<String> neFrequencyList,
			List<String> siteCategory, List<String> taskStatus, String criteria, String failureRate,
			List<Vendor> vendor, List<Technology> technologies, List<String> morphology, String geography);

	/**
	 * Gets the distinct vendor.
	 *
	 * @return the distinct vendor
	 */
	List<Vendor> getDistinctVendor();

	/**
	 * Gets the distinct vendor by domain.
	 *
	 * @param domain the domain
	 * @return the distinct vendor by domain
	 */
	List<Vendor> getDistinctVendorByDomain(List<Domain> domain);

	/**
	 * Gets the technology by vendor.
	 *
	 * @param vendors the vendors
	 * @return the technology by vendor
	 */
	List<Technology> getTechnologyByVendor(List<Vendor> vendors);

	/**
	 * Gets the vendors by type.
	 *
	 * @param neType the ne type
	 * @return the vendors by type
	 */
	List<Vendor> getVendorsByType(List<NEType> neType);

	/**
	 * Gets the NE detail by sap id sec.
	 *
	 * @param neName   the ne name
	 * @param sectorId the sector id
	 * @param domain   the domain
	 * @param vendor   the vendor
	 * @return the NE detail by sap id sec
	 */
	List<MicroSiteDataWrapper> getNEDetailBySapIdSec(String neName, Integer sectorId, String domain, String vendor);

	/**
	 * Gets the ne id by ne name.
	 *
	 * @param neId the ne id
	 * @return the ne id by ne name
	 */
	Integer getNeIdByNeName(String neId);

	/**
	 * Gets the on air network element.
	 *
	 * @return the on air network element
	 */
	List<NetworkElement> getOnAirNetworkElement();

	/**
	 * Gets the NE detail by NE id and domain vendor.
	 *
	 * @param neIdList the ne id list
	 * @param domain   the domain
	 * @param vendor   the vendor
	 * @return the NE detail by NE id and domain vendor
	 */
	List<MicroSiteDataWrapper> getNEDetailByNEIdAndDomainVendor(List<String> neIdList, String domain, String vendor);

	/**
	 * Gets the NE detail by NE ids.
	 *
	 * @param neIdList the ne id list
	 * @return the NE detail by NE ids
	 */
	List<NetworkElement> getNEDetailByNEIds(List<String> neIdList);

	/**
	 * Gets the all child NE detail by NE id.
	 *
	 * @param neId the ne id
	 * @return the all child NE detail by NE id
	 * @throws Exception the exception
	 */
	List<NetworkElementCellDetailWrapper> getAllChildNEDetailByNEId(String neId) throws Exception;

	/**
	 * Gets the NE by node and geo name.
	 *
	 * @param neType  the ne type
	 * @param geoName the geo name
	 * @return the NE by node and geo name
	 */
	List<NetworkElementWrapper> getNEByNodeAndGeoName(List<NEType> neType, String geoName);

	/**
	 * Get latitude and longitude with NE and Pne.
	 *
	 * @return list{@NetworkElementWrapper}
	 */
	List<NetworkElementWrapper> getLatLngByNE();

	/**
	 * Gets the NE data by ne name and ne type.
	 *
	 * @param nename the nename
	 * @param netype the netype
	 * @return the NE data by ne name and ne type
	 */
	NetworkElement getNEDataByNeNameAndNeType(String nename, NEType netype);

	/**
	 * Gets the network element data for NE.
	 *
	 * @param neName the ne name
	 * @param neType the ne type
	 * @return the network element data for NE
	 */
	List<NEBandDetail> getNetworkElementDataForNE(String neName, String neType);

	/**
	 * Gets the network element by ne name.
	 *
	 * @param neName the ne name
	 * @return the network element by ne name
	 */
	SiteCountWrapper getNetworkElementByNeName(String neName);

	/**
	 * Gets the geography L 3 name by NE name.
	 *
	 * @param neName the ne name
	 * @return the geography L 3 name by NE name
	 */
	String getGeographyL3NameByNEName(String neName);

	/**
	 * Search ne name by ne type.
	 *
	 * @param neName the ne name
	 * @param neType the ne type
	 * @return the list
	 */
	List<String> searchNeNameByNeType(String neName, NEType neType);

	/* Fault Management specific method */

	/**
	 * Get domain,vendor and GeographyL3 by NeId.
	 *
	 * @param neId the ne id
	 * @return NetworkElementWrapper
	 */

	List<NetworkElementWrapper> getDomainVendorAndL3ByPk(Integer neId);

	/**
	 * Gets the domain vendor and L 3 by ne id.
	 *
	 * @param neId the ne id
	 * @return the domain vendor and L 3 by ne id
	 */
	List<NetworkElementWrapper> getDomainVendorAndL3ByNeId(String neId);

	/**
	 * Gets the TAC by ne name.
	 *
	 * @param neName the ne name
	 * @return the TAC by ne name
	 */
	String getTACByNeName(String neName);

	/**
	 * Gets the all child NE detail by network FK.
	 *
	 * @param networkElementIdFk the network element id fk
	 * @return the all child NE detail by network FK
	 */
	List<NetworkElementCellDetailWrapper> getAllChildNEDetailByNetworkFK(Integer networkElementIdFk);

	/**
	 * Gets the on air network element data.
	 *
	 * @return the on air network element data
	 */
	List<NetworkElementOnAirJsiWrapper> getOnAirNetworkElementData();

	/**
	 * Gets the site count L 1 wise.
	 *
	 * @param vendor the vendor
	 * @return the site count L 1 wise
	 */
	Map<String, Long> getSiteCountL1Wise(String vendor);

	/**
	 * Gets the network element data.
	 *
	 * @return the network element data
	 */

	List<NetworkElementWrapper> getNetworkElementData();

	/**
	 * Gets the parent wise cells.
	 *
	 * @return the parent wise cells
	 */
	List<NetworkElementCellDetailWrapper> getParentWiseCells();

	/**
	 * Gets the cells by site.
	 *
	 * @param cell the cell
	 * @return the cells by site
	 */
	List<NetworkElement> getCellsBySite(String cell);

	/**
	 * Gets the NE by node and other geography.
	 *
	 * @param neType  the ne type
	 * @param geoName the geo name
	 * @return the NE by node and other geography
	 */
	List<NetworkElementWrapper> getNEByNodeAndOtherGeography(List<NEType> neType, String geoName);

	/**
	 * Gets the sites by sales L 4.
	 *
	 * @param othergeographyname the othergeographyname
	 * @return the sites by sales L 4
	 */
	List<Object> getSitesBySalesL4(String othergeographyname);

	/**
	 * Gets the distinct L 1 by ne id.
	 *
	 * @param neIdList the ne id list
	 * @return the distinct L 1 by ne id
	 */
	List<GeographyL1> getDistinctL1ByNeId(List<Integer> neIdList);

	/**
	 * Gets the other geography neid by ne name domain vendor.
	 *
	 * @param neName the ne name
	 * @param domain the domain
	 * @param vendor the vendor
	 * @return the other geography neid by ne name domain vendor
	 */
	NetworkElementWrapper getOtherGeographyNeidByNeNameDomainVendor(String neName, String domain, String vendor);

	/**
	 * Gets the geography L 3 name ne id by NE name.
	 *
	 * @param neName the ne name
	 * @return the geography L 3 name ne id by NE name
	 */
	NetworkElementWrapper getGeographyL3NameNeIdByNEName(String neName);

	/**
	 * Gets the netype from network element.
	 *
	 * @param domain the domain
	 * @param vendor the vendor
	 * @return the netype from network element
	 */
	List<NEType> getNetypeFromNetworkElement(String domain, String vendor);

	/**
	 * Gets the domain vendor and L 3 by ne id list.
	 *
	 * @param neId the ne id
	 * @return the domain vendor and L 3 by ne id list
	 */
	List<NetworkElementWrapper> getDomainVendorAndL3ByNeIdList(List<String> neId);

	/**
	 * Gets the vendor and L 4 wise nename.
	 *
	 * @param L4Id   the l 4 id
	 * @param vendor the vendor
	 * @return the vendor and L 4 wise nename
	 */
	List<Map<String, String>> getVendorAndL4WiseNename(Integer L4Id, String vendor);

	/**
	 * Gets the NE list by type and geo ids.
	 *
	 * @param type        the type
	 * @param idList      the id list
	 * @param entityLevel the entity level
	 * @param domain      the domain
	 * @param vendor      the vendor
	 * @return the NE list by type and geo ids
	 */
	List<NetworkElement> getNEListByTypeAndGeoIds(String type, Set<String> idList, String entityLevel, String domain,
			String vendor);

	/**
	 * Gets the all sites count.
	 *
	 * @param siteName the site name
	 * @param cellIds  the cell ids
	 * @param sectors  the sectors
	 * @param bands    the bands
	 * @param vendor   the vendor
	 * @param neType   the ne type
	 * @return the all sites count
	 */
	Long getAllSitesCount(String siteName, List<Integer> cellIds, List<Integer> sectors, List<String> bands,
			Vendor vendor, NEType neType);

	/**
	 * Gets the network element by sap id.
	 *
	 * @param sapId the sap id
	 * @return the network element by sap id
	 */
	List<NetworkElement> getNetworkElementBySapId(List<String> sapId);

	/**
	 * Search NE detail.
	 *
	 * @param filterList     the filter list
	 * @param projectionList the projection list
	 * @param groupByList    the group by list
	 * @param orderByList    the order by list
	 * @param isDistinct     the is distinct
	 * @param viewportMap    the viewport map
	 * @return the list
	 */
	List<Tuple> searchNEDetail(Map<String, List<Map>> filterList, Map<String, List<String>> projectionList,
			List groupByList, List orderByList, boolean isDistinct, Map viewportMap);

	/**
	 * Search NE detail for site and cell level.
	 *
	 * @param tableName            the table name
	 * @param columnsFilterDetails the columns filter details
	 * @param projectionColumns    the projection columns
	 * @param groupByColumns       the group by columns
	 * @param orderByColumns       the order by columns
	 * @param isDistinctClause     the is distinct clause
	 * @param geographyDetails     the geography details
	 * @param viewportDetails      the viewport details
	 * @return the list
	 */
	List<Tuple> searchNEDetailForSiteAndCellLevel(String tableName, List columnsFilterDetails, List projectionColumns,
			List groupByColumns, List orderByColumns, boolean isDistinctClause, Map geographyDetails,
			Map viewportDetails);

	/**
	 * Gets the network elements for site and cell level detail.
	 *
	 * @param neTypeList      the ne type list
	 * @param neNameList      the ne name list
	 * @param neFrequencyList the ne frequency list
	 * @param neStatusList    the ne status list
	 * @param vendorList      the vendor list
	 * @param technologyList  the technology list
	 * @param domainList      the domain list
	 * @param viewportMap     the viewport map
	 * @param geographyNames  the geography names
	 * @param neIdList        the ne id list
	 * @return the network elements for site and cell level detail
	 */
	List<NetworkElement> getNetworkElementsForSiteAndCellLevelDetail(List<NEType> neTypeList, List<String> neNameList,
			List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList,
			List<Technology> technologyList, List<Domain> domainList, Map<String, Double> viewportMap,
			Map<String, List<Integer>> geographyNames, List<String> neIdList);

	/**
	 * Gets the wifi AP detail by floor id.
	 *
	 * @param floorIds the floor ids
	 * @return the wifi AP detail by floor id
	 */
	List<NetworkElement> getWifiAPDetailByFloorId(List<Integer> floorIds);

	/**
	 * Gets the network element by mac address.
	 *
	 * @param macAddress the mac address
	 * @return the network element by mac address
	 */
	List<NetworkElement> getNetworkElementByMacAddress(String macAddress);

	/**
	 * Gets the distinct NE frequency by NE name.
	 *
	 * @param neName the ne name
	 * @return the distinct NE frequency by NE name
	 */
	List<String> getDistinctNEFrequencyByNEName(String neName);

	/**
	 * Get NEid with site friendly Name.
	 *
	 * @return the site with friendly name
	 */

	Map<String, String> getSiteWithFriendlyName();

	List<Object[]> getSiteDetailReportData(List<String> neFrequencyList, Map<String, List<String>> geographyNames,
			List<NEStatus> neStatus, List<NEType> neType, Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat);

	List<String> getDistinctMorphology();

	NetworkElement searchSiteByName(String neName);

	/**
	 * @param enodeBid
	 * @return NetworkElement
	 */
	NetworkElement getNetworkElementByNEId(String enodeBid);

	NetworkElement getNetworkElementForRrhByEnodeBPk(Integer enodeBid);

	List<Integer> getEnodeBIdFromFrequencyAndParentNeName(List<String> neNameList, String frequency);

	NetworkElement getNetworkElementByNELocation(Integer nelocationid_fk);

	List<Object> executeQuery(String userQuery);

	List<NetworkElementWrapper> getNetworkElementByGeography(String geoLevel, String domain, Integer geographyPK);

	NetworkElementWrapper getNetworkElementCountByNetype();

	List<NetworkElement> getChildNEListByParentNE(Integer enodeBPk);

	List<NetworkElement> getSitesByGeography(List<NEType> neTypeList, List<String> neNameList,
			List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList,
			List<Technology> technologyList, List<Domain> domainList, Map<String, Double> viewportMap,
			Map<String, List<String>> geographyNames);

	NetworkElement searchNetworkElementBySiteID(String siteId);

	List<NetworkElement> getNeInfoByneFkAndNeTypeWithStatus(Integer networkFK, NEType neType,
			List<DayOneStatus> dayOneStatus);

	void updateCellAndSiteStatus(Integer id, NEStatus neStatus);

	List<String> getVduAndVcuByNeid(List<String> neids);

	List<MicroSiteDataWrapper> getNEDetailBySapId(String neName, String domain, String vendor);

	NetworkElement getNetworkElementByNeIdAndNetype(Integer parentId, NEType neType, String riuSerialNo);

	Integer getOnAirSitesCount(List<NEType> neType, String domain, String vendor);

	List<NEDetailWrapper> getNEDetail();

	NetworkElementWrapper getGeographyL1BySapId(String siteID);

	Map<String, Long> getSitesCountNestatusWise(List<NEType> neType);

	void initializeRelationship();

	Map<String, Set<String>> getAllChildbyNeId(String neid);

	Long getTotalNECountByGeography(String geoLevel, Integer id, NEType neType, Vendor vendor, Domain domain,
			List<NEStatus> statusList);

	Integer getCountForOnAirAndPlannedSites(String neType, String domain, String vendor, String status);

	List<NetworkHierarchyDetail> getSiteDetail(String netype, String neId);

	List<NetworkElement> getChildByNetworkElementByParent(Integer id);

	Set<String> getAllRelatedNodes(String neid, NEType macro, List<String> neStatus, Boolean isNename);

	Set<String> getAllParentNodes(String neid, NEType macro, List<String> neStatus, Boolean isNename);

	Set<String> getAllChildNodes(String neid, NEType macro, List<String> neStatus, Boolean isNename);

	List getSiteListingInfo(String type, String nename);

	List<NetworkElement> getNetworkElementbyParentId(Integer parentId);

	void initializeNeNameNeIdMap();

	Map<String, String[]> getNenameWiseNEDetail();

	List<NETaskDetailWrapper> getNETaskDetailData(String neName, String neType);

	List<RANDetail> getNetworkElementIdByNameAndCellNum(String sourceSiteId, Integer sourceCellId,
			String sourceFrequencyBand, String vendor, String domain);

	int updateNetworkElementStatusByVCUId(Integer vcuId, DayOneStatus status);

	NetworkElement searchSiteByFriendlyName(String friendlyname, NEType neType);

	List<NetworkElementWrapper> getNEStatusCount(List<NEType> neTypeList, List<Domain> domainList,
			Map<String, List<String>> geographyNames);

	List<NetworkElement> getChildFromParent(Integer netwokElementFk);

	/**
	 * @param id
	 * @param pmEmsId
	 */
	void updateCoreGeography(String domain, String vendor, String neType, String neId, String pmEmsId);

	/**
	 * @param domain
	 * @param vendor
	 * @param neType
	 * @return
	 */
	List<Object[]> getNetworkElementByNeType(String domain, String vendor, String neType);

	NetworkElement getNetworkElementByIPV4(String name);

	public List<Object[]> getNEFriendlyNameListByNENameList(List<String> networkElementsList);

	List<NetworkElement> getNetworkElementByNeType(List<NEType> neTypeList);

	/**
	 * Update in new transaction.
	 *
	 * @param networkElement the network element
	 * @return the network element
	 */
	NetworkElement updateInNewTransaction(NetworkElement networkElement);

	List<Tuple> searchNetworkElementDetails(Map<String, List<Map>> filters, Map<String, List<String>> projection,
			List groupByList, List orderByList, boolean isDistinct, Map viewportMap);

	NetworkElement findById(Integer nePk);

	NetworkElement getNetworkElementDataBySiteId(String siteId);

	List<NetworkElement> getNetworkElementListById(List<Integer> networkElementIdList);

	List<Tuple> sectorPropertiesDetails(Map<String, List<Map>> filters, Map<String, List<String>> projection,
			List groupByList, List orderByList, boolean isDistinct, Map viewportMap);

	List<NEType> getDistinctNeType(String domain);

	List<NetworkElement> getNetworkElementsDetailByGeoMapping(List<NEType> neTypeList, List<String> neNameList,
			List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList,
			List<Technology> technologyList, List<Domain> domainList, Map<String, Double> viewportMap,
			Map<String, List<String>> geographyNames, List<String> neIdList, String geoLevelKey);

	public Map<String, Map<String, Object>> getNECountByNETypeAndGeography(List<NEType> neTypeList, Integer geographyId,
			String geographyType);

	public Map<String, Object> getTotalSiteCountByGeoGraphyWise(String geoType, NEType neType, Vendor vendor,
			Domain domain, List<NEStatus> statusList, Set<String> geographyName);

	List<String> getMostParentNEIDByChildNEID(List<String> neIdList);

	Long getSiteCountByGC(Integer locationId);

	Integer getSiteCountForGCByNeStatus(String neType, String nelType, String nelId, String parentNeType,
			String neStatus);

	List<NetworkElementWrapper> getSitesInfoByGC(Integer locationid);

	List<NetworkElementWrapper> getDomainVendorAndL3ByMultiNEPk(List<Integer> pkIds);

	Long getSiteCountByLatLong(Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat,
			NEType macroVdu);

	List<Object[]> getSiteDetailForCustomerCare(List<Domain> domainList, NEStatus neStatus, List<NEType> neTypeList);

	public List<NetworkElement> getAllCellsData(Integer llimit, Integer ulimit);

	public List<NetworkElement> getNetworkElementByNELocationId(Integer nelocationid_fk, String neType, Integer llimit,
			Integer ulimit);

	List<Tuple> getNetworkElementDetails(Map<String, List<Map>> filters, Map<String, List<String>> projection,
			Integer lLimit, Integer uLimit, boolean isGroupBy, boolean isDistinct) throws DaoException;

	NetworkElement getNetworkElementBySlNoAndType(String serial, String neType);

	NetworkElement getNEByVCUNameAndCellNum(String neType, String vcuName, Integer cellNum);

	NetworkElement getNEDataByNeIdAndNeType(String neId, NEType netype);

	Integer getMaxCellNumByParentNEandNEType(Integer parentneid, String neType);

	List<Object[]> getTopologyGridViewFilterData(Map<String, Object> filterMap, Integer llimit, Integer ulimit);

	NetworkElement getNetworkElementByHostName(String hostname);

	NetworkElement searchNetworkElementByPhysicalSerialNo(String physicalSerialNo);
	
	List<String> getfilterByNename(String neName, NEType neType);

}
