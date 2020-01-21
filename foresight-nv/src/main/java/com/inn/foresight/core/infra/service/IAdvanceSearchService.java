package com.inn.foresight.core.infra.service;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.model.AdvanceSearch;
import com.inn.foresight.core.infra.model.NetworkElement;

// TODO: Auto-generated Javadoc
/**
 * The Interface IAdvanceSearchService.
 */
public interface IAdvanceSearchService extends IGenericService<Integer, AdvanceSearch> {

	/**
	 * Search for map data.
	 *
	 * @param id   the id
	 * @param zoom
	 * @return the map
	 * @throws RestException the rest exception
	 */
	Object searchForMapData(Integer id, Integer zoom);

	/**
	 * Gets the advance search by name.
	 *
	 * @param name the name
	 * @return the advance search by name
	 * @throws RestException the rest exception
	 */
	public List<AdvanceSearch> getAdvanceSearchByName(String name);

	/**
	 * Gets the advance search by type list.
	 *
	 * @param name the name
	 * @param type the type
	 * @return the advance search by type list
	 * @throws RestException the rest exception
	 */
	List<AdvanceSearch> getAdvanceSearchByTypeList(String name, List<String> type);

	/**
	 * Gets the address from google.
	 *
	 * @param searchStr the search str
	 * @return the address from google
	 */
	public Map getAddressFromGoogle(String searchStr);

	/**
	 * Search to map data.
	 *
	 * @param id the id
	 * @return the object
	 * @throws RestException the rest exception
	 */
	Object searchToMapData(Integer id);

	/**
	 * Search to map data.
	 *
	 * @param name the name
	 * @param type the type
	 * @return the map
	 * @throws RestException the rest exception
	 */
	default Map searchToMapData(String name, String type) {
		return null;
	}

	/**
	 * Gets the advance search for IP topology.
	 *
	 * @param name the name
	 * @return the advance search for IP topology
	 * @throws RestException
	 */
	List<AdvanceSearch> getAdvanceSearchForIPTopology(String name);

	List<String> searchNeIdByNeType(String neId, String neType);

	Object searchDetails(Map<String, Object> map);

	List<AdvanceSearch> getAdvanceSearchByTypeListAndVendor(String name, Map<String, List<String>> map)
			throws RestException;

	void createAdvanceSearch(NetworkElement networkElement, String advanceSearchType);

	default void createAdvanceSearch(NetworkElement networkElement, String advanceSearchType, String vendor,
			String domain) {
	};
}
