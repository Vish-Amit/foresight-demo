package com.inn.foresight.core.infra.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.infra.model.GeographyMapping;

/**
 * The Interface IGeographyMappingDao.
 */
public interface IGeographyMappingDao extends IGenericDao<Integer, GeographyMapping> {

	/**
	 * Insert geography mapping data in oracle.
	 *
	 * @param geographyName the geography name
	 * @param duplicateName the duplicate name
	 * @param type the type
	 * @throws DaoException the dao exception
	 */
	void insertGeographyMappingDataInOracle(String geographyName, String duplicateName, String type);

	/**
	 * Gets the all geography name by geography name.
	 *
	 * @param geographyName the geography name
	 * @return the all geography name by geography name
	 * @throws DaoException the dao exception
	 */
	List<String> getAllGeographyNameByGeographyName(String geographyName);

}
