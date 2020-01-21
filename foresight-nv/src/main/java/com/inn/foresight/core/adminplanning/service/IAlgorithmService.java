package com.inn.foresight.core.adminplanning.service;

import java.util.List;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.adminplanning.model.Algorithm;

/**
 * The Interface IAlgorithmService.
 */
public interface IAlgorithmService extends IGenericService<Integer, Algorithm> {

	/**
	 * Search algorithm data.
	 *
	 * @param displayName the display name
	 * @return the list
	 * @throws RestException the rest exception
	 */
	public List<Algorithm> searchAlgorithmData(String displayName);

	/**
	 * Gets the all algorithms.
	 *
	 * @return the all algorithms
	 * @throws RestException the rest exception
	 */
	public List<Algorithm> getAllAlgorithms();

	/**
	 * Creates the algorithm.
	 *
	 * @param algorithmProperty the algorithm property
	 * @throws RestException the rest exception
	 */
	public void createAlgorithm(Algorithm algorithmProperty);

	/**
	 * Delete algorithm.
	 *
	 * @param algorithmId the algorithm id
	 * @throws RestException the rest exception
	 */
	public void deleteAlgorithm(Integer algorithmId);

	/**
	 * Update algorithm.
	 *
	 * @param algorithm the algorithm property
	 * @throws RestException the rest exception
	 */
	public void updateAlgorithm(Algorithm algorithm);

}
