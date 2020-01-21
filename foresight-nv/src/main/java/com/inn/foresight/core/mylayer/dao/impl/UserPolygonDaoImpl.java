package com.inn.foresight.core.mylayer.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.mylayer.dao.IUserPolygonDao;
import com.inn.foresight.core.mylayer.model.UserPolygon;
import com.inn.foresight.core.mylayer.utils.MyLayerConstants;

@Repository("UserPolygonDaoImpl")
public class UserPolygonDaoImpl extends HibernateGenericDao<Integer, UserPolygon> implements IUserPolygonDao {

	private Logger logger = LogManager.getLogger(UserPolygonDaoImpl.class);

	public UserPolygonDaoImpl() {
		super(UserPolygon.class);
	}

	@Override
	public boolean isPolygonExist(Integer userid, String polygonName) {
		boolean result = false;
		Long count = 0L;
		try {
			logger.info("Going to check if polygon {} already exist for userid {} ", polygonName,userid);
			Query query = getEntityManager().createNamedQuery("isPolygonExist");
			setParametersInQuery(null, null, query, userid, null, null,polygonName);
			count = (Long) query.getSingleResult();
			if (count > 0) {
				result = true;
			}
			logger.info("is polygon exist: {}", result);
		} catch (NoResultException noResultException) {
			logger.error("no polygon exist .Exception : {}", noResultException.getMessage());
		} catch (Exception exception) {
			logger.error("Error in finding polygon for userid {} Exception {} ", userid,ExceptionUtils.getStackTrace(exception));
		}
		return result;
	}

	@Override
	@Transactional
	public boolean deletePolygonById(Integer userid, Integer id) {
		try {
			logger.info("Going to delete polygon id {} for userid {}", id, userid);
			Query query = getEntityManager().createNamedQuery("deletePolygonById");
			setParametersInQuery(null, null, query, userid, null, id,null);
			query.executeUpdate();
			return true;
		} catch (Exception exception) {
			logger.error("Error in deleting polygon id {} for userid {} Exception {} ", userid, id,ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to delete polygon.");
		}
	}
     
	private Query setParametersInQuery(Integer lowerLimit, Integer upperLimit,Query query,Integer userid,String searchTerm, Integer id, String polygonName) {
		logger.info("going to set parameters in query .");
		if(lowerLimit != null && upperLimit != null && lowerLimit >= 0 && upperLimit > 0) {
			query.setMaxResults(upperLimit - lowerLimit);
			query.setFirstResult(lowerLimit);
		}
		if(userid != null) {
			query.setParameter(ForesightConstants.USERID, userid);
		}
		if(searchTerm != ForesightConstants.BLANK_STRING && searchTerm != null) {
			query.setParameter(MyLayerConstants.SEARCHTERM,ForesightConstants.MODULUS + searchTerm + ForesightConstants.MODULUS);
		}
		if(id != null) {
			query.setParameter(MyLayerConstants.POLYGON_ID, id);
		}
		if(polygonName != null) {
			query.setParameter(MyLayerConstants.POLYGON_NAME, polygonName);
		}
		return query;
	}
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<UserPolygon> getListOfPolygon(Integer userid, Integer lowerLimit, Integer upperLimit,String polygonSearch) {
		try {
			logger.info("Going to get list of polygons for userid {}", userid);
			String dynamicQuery = "select u from UserPolygon u where u.user.userid =:userId ";
			if (polygonSearch != ForesightConstants.BLANK_STRING && polygonSearch != null)
				dynamicQuery += " and u.name like :searchTerm ";
			dynamicQuery += " order by u.modifiedTime desc ";
			Query query = getEntityManager().createQuery(dynamicQuery);
			setParametersInQuery(lowerLimit, upperLimit, query, userid, polygonSearch,null,null);
			return query.getResultList();
		} catch (NoResultException noResultException) {
			logger.error("No polygons found.Exception : {} ", noResultException.getMessage());
			throw new RestException("No polygons found");
		} catch (Exception exception) {
			logger.error("Error in getting polygons for userid {} Exception {} ", userid,
					ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get polygons");
		}
	}

	@Override
	public Long getCountsOfPolygon(Integer userid, String polygonSearch) {
		try {
			logger.info("Going to get counts of polygons for userid {} and searchTerm {}", userid,polygonSearch);
			String dynamicQuery = "select count(u) from UserPolygon u where u.user.userid =:userId ";
			if (polygonSearch != ForesightConstants.BLANK_STRING && polygonSearch != null)
				dynamicQuery += " and u.name like :searchTerm ";
			dynamicQuery += " order by u.modifiedTime desc ";
			Query query = getEntityManager().createQuery(dynamicQuery);
			setParametersInQuery(null, null, query, userid, polygonSearch,null,null);
			return (Long) query.getSingleResult();
		} catch (NoResultException noResultException) {
			logger.error("no result found.Exception {} ", noResultException.getMessage());
			throw new RestException("No polygons found");
		} catch (Exception exception) {
			logger.error("Error in getting counts of polygons for userid {} Exception {} ", userid,	ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get counts of polygons");
		}
	}

	@Override
	public UserPolygon getPolygonById(Integer userid, Integer id) {
		try {
			Query query = getEntityManager().createNamedQuery("getPolygonById");
			setParametersInQuery(null, null, query, userid, null, id,null);
			return (UserPolygon) query.getSingleResult();
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
			throw new RestException("No polygon found");
		} catch (Exception exception) {
			logger.error("Error in getting polygon for id {} and for userid {} Exception {} ", id, userid,
					ExceptionUtils.getStackTrace(exception));
			throw new RestException("Unable to get polygon.");
		}
	}
	@Override
	public boolean isNewPolygonNameAlreadyExist(Integer userid, String polygonName, Integer id) {
		boolean result = false;
		Long count = 0L;
		try {
			logger.info("Going to get polygons for userid {} and name {} ", userid, polygonName);
			Query query = getEntityManager().createNamedQuery("isNewPolygonNameAlreadyExist");
			setParametersInQuery(null, null, query, userid, null, id, polygonName);
			count = (Long) query.getSingleResult();
			if (count > 0) {
				result = true;
			}
			logger.info("is polygon exist For Id {}, Result : {}", id, result);
		} catch (NoResultException noResultException) {
			logger.error("No result found Message {} ", noResultException.getMessage());
		} catch (Exception exception) {
			logger.error("Error in finding polygon names for userid {} Exception {} ", userid,
					ExceptionUtils.getStackTrace(exception));
		}
		return result;
	}
}
