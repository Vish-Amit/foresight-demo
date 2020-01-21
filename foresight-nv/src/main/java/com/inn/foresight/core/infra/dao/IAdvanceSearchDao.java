package com.inn.foresight.core.infra.dao;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.infra.model.AdvanceSearch;

/** The Interface IAdvanceSearchDao. */
public interface IAdvanceSearchDao extends IGenericDao<Integer, AdvanceSearch> {
	/**
	 * Creates the in new transaction.
	 *
	 * @param entity the entity
	 * @return the advance search
	 * @throws DaoException the dao exception
	 */
	AdvanceSearch createInNewTransaction(AdvanceSearch entity);

	/**
	 * Gets the advance search by name.
	 *
	 * @param name the name
	 * @return the advance search by name
	 * @throws DaoException the dao exception
	 */
	List<AdvanceSearch> getAdvanceSearchByName(String name);

	/**
	 * Gets the advance search by type list.
	 *
	 * @param name the name
	 * @param type the type
	 * @return the advance search by type list
	 * @throws DaoException the dao exception
	 */
	List<AdvanceSearch> getAdvanceSearchByTypeList(String name, List<String> type, String levelType, List<Integer> geoId);

	/**
	 * Gets the google response.
	 *
	 * @param searchStr the search str
	 * @return the google response
	 */
	Map getGoogleResponse(String searchStr);

	AdvanceSearch getAdvanceSearchByTypeReference(String name);

	List<String> getDistinctTypeList();

	void deleteGalleryById(List<Integer> galleryId);
	
	List<AdvanceSearch> getAdvanceSearchByTypeListAndVendorType(String name, List<String> typeList, String levelType, List<Integer> geoId, List<String> vendorType, Map<String, List<String>> map)
			throws DaoException;


	AdvanceSearch getAdvanceSearchBySearchFieldNameAndTypeList(String name, List<String> type);
	
	public AdvanceSearch getAdvanceSearchConfigurationByTypeAndTypeReference(String type, Integer typeReference);

	

}