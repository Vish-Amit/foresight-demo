package com.inn.foresight.core.adminplanning.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.adminplanning.model.Algorithm;

// TODO: Auto-generated Javadoc
/**
 * The Interface IAlgorithmDao.
 */
public interface IAlgorithmDao extends IGenericDao<Integer, Algorithm> {
	
	/**
	 * Gets the all algorithms.
	 *
	 * @param size the result size
	 * @param startLimit the start limit
	 * @return the all algorithms
	 * @throws DaoException the dao exception
	 */
	public List<Algorithm> getAllAlgorithms();


	/**
	 * Search algorithm data.
	 *
	 * @param displayName the display name
	 * @return the list
	 * @throws DaoException the dao exception
	 */
	public List<Algorithm> searchAlgorithmData(String displayName);

}

