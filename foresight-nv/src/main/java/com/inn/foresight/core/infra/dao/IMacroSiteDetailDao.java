package com.inn.foresight.core.infra.dao;

import java.util.List;
import java.util.Map;

import com.inn.commons.maps.LatLng;
import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.infra.model.MacroSiteDetail;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.utils.enums.Technology;
import com.inn.foresight.core.infra.utils.enums.Vendor;
import com.inn.foresight.core.infra.wrapper.MacroSitesDetailWrapper;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;
import com.inn.foresight.core.infra.wrapper.SiteDataWrapper;
import com.inn.foresight.core.infra.wrapper.SiteSummaryOverviewWrapper;

/**
 * The Interface IMacroSiteDetailDao.
 */
public interface IMacroSiteDetailDao extends IGenericDao<Integer, MacroSiteDetail> {

	/**
	 * Gets the macro site detail bysap id sec and band.
	 *
	 * @param sapid the sapid
	 * @param sectorId the sector id
	 * @param band the band
	 * @return the macro site detail bysap id sec and band
	 */
	NetworkElementWrapper getMacroSiteDetailBySapIdSecAndBand(String sapid, Integer sectorId, String band);

	/**
	 * Gets the macro site detail bysap id.
	 *
	 * @param sapId the sap id
	 * @return the macro site detail bysap id
	 */
	List<NetworkElementWrapper> getMacroSiteDetailBySapId(String sapId);

	/**
	 * Gets the site detail by site id sec and vendor.
	 *
	 * @param sapid the sapid
	 * @param sectorId the sector id
	 * @param band the band
	 * @param vendor the vendor
	 * @param technology the technology
	 * @param domain the domain
	 * @return the site detail by site id sec and vendor
	 */
	NetworkElementWrapper getMacroCellDetail(String sapid,
			Integer sectorId, String band, String vendor, String technology,
			String domain);

	/**
	 * Gets the site summary overview by band.
	 *
	 * @param neName the ne name
	 * @param neFrequency the ne frequency
	 * @param neStatus the ne status
	 * @return the site summary overview by band
	 */
	//SiteSummaryOverviewWrapper getSiteSummaryOverviewByBand(String neName, String neFrequency, String neStatus);

	/**
	 * Gets the NE cells by sap ids.
	 *
	 * @param sapIds the sap ids
	 * @return the NE cells by sap ids
	 */
	List<SiteDataWrapper> getNECellsBySapIds(List<String> sapIds);

	/**
	 * Gets the NE by sap ids.
	 *
	 * @param sapIds the sap ids
	 * @return the NE by sap ids
	 */
	List<MacroSiteDetail> getNEBySapIds(List<String> sapIds);

	/**
	 * Gets the antenna parameters by band.
	 *
	 * @param neName the ne name
	 * @param neFrequency the ne frequency
	 * @param neStatus the ne status
	 * @return the antenna parameters by band
	 */
	//SiteSummaryOverviewWrapper getAntennaParametersByBand(String neName, String neFrequency, String neStatus);

	List<String> getCellList(String geographyL4, String geographyName, Vendor vendor);
    
    //List<MacroSiteDetail> getSectorPropertyData(String neName, String neFrequency, String neStatus) ;

	List<MacroSiteDetail> getMacroSiteDetailsForCellLevel(List<NEType> neTypeList, List<String> neNameList, List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList,
			List<Technology> technologyList, List<Domain> domainList,Map<String, List<String>> geographyNames,List<String> neIdList);

	public SiteSummaryOverviewWrapper getRadioParametersByBand(String neName, String neFrequency, String neStatus);

	/*SiteSummaryOverviewWrapper getSiteSummaryOverviewByBandForSecondCarrier(String neName, String neFrequency,
			String neStatus);*/

	List<Integer> getSectorIdForSiteOverViewDetails(String neName, String neFrequency, String neStatus,
			String carrier);

	List<MacroSiteDetail> getMacroSiteDetailByEcgi(String ecgi);

	List<MacroSiteDetail> getMacroSiteDetailByMncMccCellId(Integer mcc, Integer mnc, String cellId);

	List<NetworkElementWrapper> getAllSapidCnum(String vendor, List<String> band);

	List<NetworkElementWrapper> getSapidCnumByGeographyLevelData(
			List<String> geographyName, String geographyType, String vendor,
			List<String> band);

	List<NetworkElementWrapper> getUploadedCustomCell(String vendor,
			List<String> listOfCells, String node);

	List<NetworkElementWrapper> getSiteDetailByLatLongAndBandWise(Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat, List<String> bandList, String domain, String vendor);

	List<NetworkElementWrapper> getCellDetailByVendorAndStatus(Vendor vendor, NEStatus status);

	/**
	 * @param bandList
	 * @param domain
	 * @param vendor
	 * @param geographyName
	 * @return
	 * @throws DaoException
	 */
	List<NetworkElementWrapper> getSiteDetailByGeographyL4(List<String> bandList, String domain, String vendor,
			String geographyName);

	List<String> getCarrierForNenameAndFrequency(String neName, String neFrequency, String neStatus);

	List<NetworkElement> getCellNameSpecificData(List<String> neIdList);
	
	List<String> getSmallCellByGeographyTypeAndCellType(String geographyType,
			String vendor, List<String> geographyValues);
	
	List<String> getSmallCellByGeographyTypeAndBandType(String geographyType,
			String smc, List<String> geographyValues, String band, String vendor);
	
	List<String> getSapidCnumByGeographyLevel(List<String> geographyName,
			String geographyType, String vendor);
	
	List<String> getSapidCnumByGeographyLevelAndBand(
			List<String> geographyName, String geographyType, String band,
			String vendor);
	
	List<String> getEnodeByGeographyTypeAndCellType(String geographyType,
			List<String> geographyValues, String vendor);
	
	List<MacroSitesDetailWrapper> getEcgiLocation();

	MacroSiteDetail getMacroSiteDetailByNeNameAndType(String neName, NEType neType);

	List<NetworkElementWrapper> getSiteDetailByNeName(String neName);


	LatLng getLocationByCGIAndPci(Integer cgi, Integer pci);

	/**
	 * @return
	 */
	List<Integer> getDistinctCellIdsByNeType(String neType);
	
	List<MacroSiteDetail> getSiteLocationByCGI(Integer cgi);

	List<MacroSiteDetail> getMacroSiteDetailByListNename(List<String> nename);

	/**
	 * @param listCellIds
	 * @return
	 */
	List<Integer> getDistinctSectorsByCellId(List<Integer> listCellIds);

	/**
	 * @return
	 */
	List<Integer> getAllDistinctSectors();


	
	List<Object[]> searchAllSites(String siteName, Integer llimit, Integer ulimit, List<Integer> cellIds,
			List<Integer> sectors, List<String> band, String vendor, String neType, String orderByKey, String order);

	/**
	 * @param siteName
	 * @param cellIds
	 * @param sectors
	 * @param band
	 * @param vendor
	 * @param neType
	 * @return
	 */
	Long countAllSites(String siteName, List<Integer> cellIds, List<Integer> sectors, List<String> band,
			String vendor, String neType);

	/**
	 * @return
	 */
	List<Integer> getDistinctCellIds();

	List<Integer> validateEnbid(Integer enbId);

	//List<NetworkElementWrapper> getMacroSiteDetailByNameAndType(String neName, String neType);

	List<Object[]> getBtsDetailByCgi(String cgi);

	List<Object[]> findAllOnairSites(String status, String neType);

	List<Object[]> getBtsNameByEcgi(String ecgi);
	
	List<Integer>getDistinctPCIBySiteIdAndBand(String neName ,List<String> bandList);

	List<Object[]> getSectorDetailReportData(String neFrequencyList, Map<String, List<String>> geographyNames,
			List<NEStatus> neStatusList, List<NEType> neTypeList, Double southWestLong, Double southWestLat,
			Double northEastLong, Double northEastLat);
	
}
