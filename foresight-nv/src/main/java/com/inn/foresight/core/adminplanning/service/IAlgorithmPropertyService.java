package com.inn.foresight.core.adminplanning.service;

import java.util.List;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.adminplanning.model.AlgorithmProperty;
import com.inn.foresight.core.adminplanning.wrapper.AlgorithmResponse;

public interface IAlgorithmPropertyService extends IGenericService<Integer, AlgorithmProperty>{

	/**
	 * Gets the existing exceptions by Algorithm Name.
	 *
	 * @param algoId the algorithm name
	 * @return the existing exceptions
	 * @throws RestException the rest exception
	 */
	List<AlgorithmResponse> getExistingExceptions(Integer algoId);

	/**
	 * Gets the algorithm properties.
	 * @param id TODO
	 * @param exceptionId 
	 * @return the properties
	 * @throws RestException the rest exception
	 */
	List<AlgorithmProperty> getProperties(Integer id,String type, Integer exceptionId);


	void resetAlgorithmPropertyData(Integer algoId, String type, Integer exceptionId, String reason);


	void createProperties(Integer algorithmId, List<AlgorithmProperty> newExceptions);

	
	void updateProperties(List<AlgorithmProperty> propertyList, Integer id);

	List<AlgorithmProperty> getPropertyByName(String freezingPeriod);

	void deleteProperty(Integer propertyId);

	List<Object[]> getPropertyHistory(Integer propertyId);

	void addProperties(Integer algorithmId, List<AlgorithmProperty> properties);

	void updatePropertiesConfiguration(List<AlgorithmProperty> propertyList, Integer algorithmId);

	
}
