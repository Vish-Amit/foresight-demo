package com.inn.foresight.core.adminplanning.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.adminplanning.model.AlgorithmProperty;
import com.inn.foresight.core.adminplanning.wrapper.AlgorithmResponse;


/**
 * The Interface IAlgorithmPropertyDao.
 */
public interface IAlgorithmPropertyDao extends IGenericDao<Integer, AlgorithmProperty>{

	/**
	 * Gets the existing exceptions.
	 *
	 * @param algoId the algo id
	 * @return the existing exceptions
	 * @throws DaoException the dao exception
	 */
	List<AlgorithmResponse> getExistingExceptions(Integer algoId);
	
	/**
	 * Gets the PAN properties.
	 *
	 * @param algoId the algo id
	 * @return the PAN properties
	 * @throws DaoException the dao exception
	 */
	List<AlgorithmProperty> getPANProperties(Integer algoId);
	
	/**
	 * Gets the properties for exception.
	 *
	 * @param algorithmId the algorithm id
	 * @param exceptionId the exception id
	 * @param type the type
	 * @return the properties for exception
	 * @throws DaoException the dao exception
	 */
	List<AlgorithmProperty> getPropertiesForException(Integer algorithmId, Integer exceptionId, String type);

	/**
	 * Gets the properties for exception.
	 *
	 * @param algoRequestList the algo request list
	 * @param algoId the algo id
	 * @return the properties for exception
	 * @throws DaoException the dao exception
	 */
	List<AlgorithmProperty> getPropertiesForException(List<AlgorithmResponse> algoRequestList,Integer algoId);

	/**
	 * Gets the property by name.
	 *
	 * @param propertyName the property name
	 * @return the property by name
	 * @throws DaoException the dao exception
	 */
	List<AlgorithmProperty> getPropertyByName(String propertyName);

	/**
	 * Gets the property history.
	 *
	 * @param propertyId the property id
	 * @return the property history
	 * @throws DaoException the dao exception
	 */
	List<Object[]> getPropertyHistory(Integer propertyId);

	/**
	 * Gets the property by name and id.
	 *
	 * @param id the id
	 * @param name the name
	 * @return the property by name and id
	 * @throws DaoException the dao exception
	 */
	List<AlgorithmProperty> getPropertyByNameAndId(Integer id, String name);

	AlgorithmProperty getAlgorithPropertyByNameAndAlgorithmName(String algorithmName, String propertyName);

	
	
}	
