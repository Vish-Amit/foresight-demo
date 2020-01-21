package com.inn.foresight.core.infra.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.model.RANDetail;
import com.inn.foresight.core.infra.model.SmallCellSiteDetail;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;

/**
 * The Interface ISmallCellSiteDetailDao.
 */
public interface ISmallCellSiteDetailDao extends IGenericDao<Integer, SmallCellSiteDetail> {

	/**
	 * Gets the small site by site id sec and vendor.
	 *
	 * @param neType the ne type
	 * @param sapid the sapid
	 * @param band the band
	 * @param vendor the vendor
	 * @param technology the technology
	 * @param domain the domain
	 * @return the small site by site id sec and vendor
	 */
	NetworkElementWrapper getSmallCellSiteDetail(String neType,
			String sapid, String band, String vendor, String technology,
			String domain);

	/**
	 * Gets the small site by sap id and band.
	 *
	 * @param sapId the sap id
	 * @param band the band
	 * @param sector the sector
	 * @return the small site by sap id and band
	 */
	NetworkElementWrapper getSmallSiteBySapIdAndBand(String sapId, String band,Integer sector);

	/**
	 * Gets the small site by sap id.
	 *
	 * @param sapId the sap id
	 * @return the small site by sap id
	 */
	List<NetworkElementWrapper> getSmallSiteBySapId(String sapId);


	List<NetworkElementWrapper> getNEIdForTracePort(Integer clusterId);

	Integer getSmallCellEnbIdByNEId(
			Integer networkElementId);

	List<NetworkElementWrapper> getSmallCellDetailByGeographyL4(List<String> bandList, String domain, String vendor,
			String geographyName);

	List<RANDetail> getSmallCellSiteDetails(String neName, String neType);

	List<NetworkElement> getNetworkElementBySmallCells(String domain,
			String vendor, String neType, List<String> cellsList);

	/**
	 * @param neType
	 * @return
	 */
	List<Integer> getDistinctSmallCellSiteCellIds(String neType);

}