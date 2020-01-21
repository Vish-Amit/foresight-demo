package com.inn.foresight.core.infra.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.infra.model.EmsServer;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.EMSType;
import com.inn.foresight.core.infra.utils.enums.Vendor;

/**
 * The Interface IEmsServerDao.
 */
public interface IEmsServerDao extends IGenericDao<Integer, EmsServer> {

	/**
	 * Find all in transaction.
	 *
	 * @return the list
	 * @throws DaoException the dao exception
	 */
	List<EmsServer> findAllInTransaction();

	/**
	 * Gets the ems server by name.
	 *
	 * @param name the name
	 * @return the ems server by name
	 * @throws DaoException the dao exception
	 */
	EmsServer getEmsServerByName(String name);

	List<Object[]> getEmsServer(Integer id, String emsType, String geoType,
			String domain, String vendor, String technology);

	List<EmsServer> getAllEmsServerByEmsType(Integer geographyId, String emsType , String geoType,String domain,String vendor,String technology);

	EmsServer getEmsServerByIpAndEmsType(String ip, String emsType);

	List<Object[]> getEmsNameAndIpByDomainAndVendor(String domain, String vendor, String technology);

	List<String> getDistinctEmsName();

	List<String> getDistinctEmsIP();

	List<EmsServer> getEmsServerByVendorDomainAndEmsType(Vendor vendor, Domain domain, EMSType emsType);

}
