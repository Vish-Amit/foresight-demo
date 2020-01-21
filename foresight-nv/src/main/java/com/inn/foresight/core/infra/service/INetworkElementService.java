package com.inn.foresight.core.infra.service;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;
import com.inn.foresight.core.infra.wrapper.PBServiceInfoWrapper;

/**
 * The Interface INetworkElementService.
 */
public interface INetworkElementService extends IGenericService<Integer, NetworkElement> {

	/**
	 * Gets the network element map by ne name.
	 *
	 * @return the network element map by ne name
	 */
	Map<String, NetworkElement> getNetworkElementMapByNeName();

	/**
	 * Gets the distinct info from network element.
	 *
	 * @return the distinct info from network element
	 * @throws RestException
	 *             the rest exception
	 */
	List<Map<String, Object>> getDistinctInfoFromNetworkElement();

	List<Object> getDistinctDataFromNetworkElement(String searchType, String domain, String vendor, String technology,
			String softwareVersion);

	List<Map<String, Object>> getNetworkElement(String netype, Integer geographyId, String geoType, String domain,
			String vendor, String technology, String softwareVersion);

	NetworkElementWrapper getSiteInfoBySapId(String siteID);

	NetworkElementWrapper getGeographyL4BySapId(String siteID);

	List<String> getNeFrequencyByDomainAndVendor(String domain, String vendor);

	NetworkElementWrapper getNEDetailsByNEId(String neId, String key);

	List<NetworkElementWrapper> getNEDetailByNEName(List<String> neNameList);

	PBServiceInfoWrapper getSmallCellsAndWifiAPForPBService(List<NEType> neTypeList);

	List<String> getSapidsByGeographyL4(String geographyL4);

	Map<NEType, Long> getCountByGeoraphyAndVendor(String vendor, List<String> geographyName, String type);

	Map<NEType, Long> getCountByGeoraphyAndVendor(String vendor, String geographyName, String type);

	List<String> getCellIdsByGeographyL4(String geographyL4);

	List<String> getSitesBySalesL4(String othergeographyname);

	List<Map<String, String>> getVendorAndL4WiseNename(Integer L4Id, String vendor);

	List<String> getDistinctMorphology();

	void initializeRelationship();

	List<NetworkElementWrapper> getNEStatusCount(List<NEType> neTypeList, List<Domain> domainList,
			Map<String, List<String>> geographyNames);

	List<NetworkElementWrapper> getSitesInfoByGC(Integer locationid);

	void getAllChildbyNeId();

	List<String> getfilterByNename(String neName, NEType neType);

	public void selectAllFromNE();


	

}
