package com.inn.foresight.core.mylayer.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.mylayer.dao.IKmlProcessorDao;
import com.inn.foresight.core.mylayer.model.KmlProcessor;
import com.inn.foresight.core.mylayer.utils.KmlProcessorWrapper;
import com.inn.foresight.core.mylayer.utils.MyLayerConstants;

@Repository("KmlProcessorDaoImpl")
public class KmlProcessorDaoImpl extends HibernateGenericDao<Integer, KmlProcessor> implements IKmlProcessorDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(KmlProcessorDaoImpl.class);

	public KmlProcessorDaoImpl() {
		super(KmlProcessor.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<KmlProcessorWrapper> getKmlData(Integer userid, Integer upperLimit, Integer lowerLimit) {
		logger.info("Finding Kml list by user {}", userid);
		try {
			Query query = getEntityManager().createNamedQuery("getKmlData").setParameter(ForesightConstants.USERID, userid);
			if (lowerLimit != null && upperLimit != null) {
				query.setFirstResult(lowerLimit);
				query.setMaxResults(upperLimit - lowerLimit );
			}
			return query.getResultList();
		} catch (NoResultException noResultException) {
			logger.warn("No kml detail found Message: " + noResultException.getMessage());
			throw new RestException("No Data Found");
		} catch (Exception exception) {
			logger.error("Error in getting kml details Exception: " + ExceptionUtils.getStackTrace(exception));
			throw new RestException("No Data Found");
		}
	}

	@Override
	public KmlProcessor getKMLById(Integer userid,Integer id)
	{
		logger.info("going to get kml by id: {}",id);
		try
		{
			Query query=getEntityManager().createNamedQuery("getKMLById");
			query.setParameter(ForesightConstants.ID, id);
			query.setParameter(ForesightConstants.USERID, userid);
			return (KmlProcessor) query.getSingleResult();
		} catch (NoResultException noResultException) {
			logger.error("No KML FOUND. Exception: {} ", noResultException.getMessage());
			throw new RestException(MyLayerConstants.DATA_NOT_FOUND);
		} catch (Exception exception) {
			logger.error("Error in getting kml for id {} and for userid {} Exception {} ", id, userid,
					ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get kml.");
		}
	}

	@Override
	@Transactional
	public boolean deleteKMLDetails(Integer userid, Integer id) {
		try {
			logger.info("Going to delete KML detail for id {} and for userid {}", id, userid);
			Query query = getEntityManager().createNamedQuery("deleteKMLDetails");
			query.setParameter(ForesightConstants.ID, id);
			query.setParameter(ForesightConstants.USERID, userid);
			query.executeUpdate();
			return true;
		}catch(Exception exception)
		{
			logger.error("Error in deleting KML details for id {} and for userid {} Exception {} ",userid,ExceptionUtils.getStackTrace(exception));
			throw new RestException("Not able to delete KML details.");
		}
	}

	@Override
	public KmlProcessor getKmlDataById(Integer id)
	{
		try
		{
			Query query=getEntityManager().createNamedQuery("getKmlDataById");
			query.setParameter(ForesightConstants.ID, id);
			return (KmlProcessor) query.getSingleResult();
		}catch(NoResultException noResultException)
		{
			logger.error("No DATA found Message {} ",noResultException.getMessage());
			throw new RestException(MyLayerConstants.DATA_NOT_FOUND);
		}catch(Exception exception)
		{
			logger.error("Error in getting kml data for id {} Exception {} ",id,ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get kml data.");
		}
	}

	@SuppressWarnings({ "unchecked" })
	@Transactional
	@Override
	public List<KmlProcessor> getListOfKMLBySearchTerm(Integer userid,Integer lowerLimit,Integer upperLimit,String kmlSearch)
	{
		try
		{
			logger.info("Going to get list of KML for userid {}",userid);
			String dynamicQuery = "select k from KmlProcessor k where k.userid.userid =:userId ";
			if (kmlSearch != ForesightConstants.BLANK_STRING)
				dynamicQuery += " and k.kmlName like :kmlSearch ";
			dynamicQuery += " order by k.modifiedTime desc ";
			Query query = getEntityManager().createQuery(dynamicQuery);
			query.setParameter(ForesightConstants.USERID, userid);
			query.setParameter(InfraConstants.KML_SEARCH,ForesightConstants.MODULUS + kmlSearch + ForesightConstants.MODULUS);

			if (lowerLimit != null && upperLimit != null && lowerLimit >= 0 && upperLimit > 0) {
			query.setMaxResults(upperLimit - lowerLimit);
			query.setFirstResult(lowerLimit);
			}

			return query.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("No KML LIST found Message {} ", noResultException.getMessage());
			throw new RestException("No kml found");
		} catch (Exception exception) {
			logger.error("Error in getting kml for userid {} Exception {} ", userid, ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get kml");
		}
	}

	@Override
	public Long getCountsOfKML(Integer userid, String kmlSearch) {
		try {
			logger.info("Going to get list of kml for userid {}", userid);
			String dynamicQuery = "select count(k) from KmlProcessor k where k.userid.userid =:userId  ";
			if (kmlSearch != ForesightConstants.BLANK_STRING && kmlSearch != null)
				dynamicQuery += " and k.kmlName like :kmlSearch ";
			dynamicQuery += " order by k.modifiedTime desc ";
			Query query = getEntityManager().createQuery(dynamicQuery);
			query.setParameter(ForesightConstants.USERID, userid);
			if (kmlSearch != ForesightConstants.BLANK_STRING && kmlSearch != null)
				query.setParameter(InfraConstants.KML_SEARCH,ForesightConstants.MODULUS + kmlSearch + ForesightConstants.MODULUS);
			return (Long) query.getSingleResult();
		} catch (NoResultException noResultException) {
			logger.error("No COUNTS OF KML FOUND Message {} ", noResultException.getMessage());
			throw new RestException("No kml found");
		} catch (Exception exception) {
			logger.error("Error in getting counts of kml for userid {} Exception {} ", userid,
					ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get counts of kml");
		}
	}

	@Override
	public boolean isKMLExist(Integer userid, String kmlName) {
		boolean result = false;
		Long count = 0L;
		try {
			logger.info("Going to get KML for userid {} and name {}", userid, kmlName);
			Query query = getEntityManager().createNamedQuery("isKMLExist");
			query.setParameter(ForesightConstants.USERID, userid);
			query.setParameter(ForesightConstants.KML_NAME, kmlName);
			count = (Long) query.getSingleResult();
			if (count > 0) {
				result = true;
			}
			logger.info("is kml exist: {}", result);
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
		} catch (Exception exception) {
			logger.error("Error in getting KML for userid {} Exception {} ", userid, ExceptionUtils.getStackTrace(exception));
		}
		return result;
	}

	@Override
	public List<String> getAllKMLNames(Integer userid, Integer id) {
		try {
			logger.info("Going to get kml names for userid {}", userid);
			Query query = getEntityManager().createNamedQuery("getAllKMLNames");
			query.setParameter(ForesightConstants.USERID, userid);
			query.setParameter(ForesightConstants.ID, id);
			return query.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No result found.");
		} catch (Exception exception) {
			logger.error("Error in getting KML names for userid {} Exception {} ", userid,
					ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get KML names.");
		}
	}
}
