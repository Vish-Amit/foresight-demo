package com.inn.foresight.core.gallery.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.QueryTimeoutException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.gallery.dao.IGalleryDetailDao;
import com.inn.foresight.core.gallery.model.GalleryDetail;
import com.inn.foresight.core.gallery.utils.GalleryDetailConstants;
import com.inn.foresight.core.gallery.utils.GalleryDetailUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.utils.InfraUtils;

@Repository("GalleryDetailDaoImpl")
public class GalleryDetailDaoImpl extends HibernateGenericDao<Integer, GalleryDetail> implements IGalleryDetailDao {

	private Logger logger = LogManager.getLogger(GalleryDetailDaoImpl.class);

	public GalleryDetailDaoImpl() {
		super(GalleryDetail.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GalleryDetail> getGallerySmileForVisualization(Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat) {
		List<GalleryDetail> galleryDetails = new ArrayList<>();
		logger.info("Going to get Gallery and Smile data for Visualization by southWestLong {} southWestLat {} northEastLong {} northEastLat {} ", southWestLong, southWestLat, northEastLong,
				northEastLat);
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<GalleryDetail> criteriaQuery = criteriaBuilder.createQuery(GalleryDetail.class);
			Root<GalleryDetail> root = criteriaQuery.from(GalleryDetail.class);
			GalleryDetailUtils.getSelectionForVisulisation(criteriaQuery,root);
			Map<String, Double> viewportMap = InfraUtils.getViewPortMap(southWestLong, southWestLat, northEastLong, northEastLat);
			List<Predicate> predicates = GalleryDetailUtils.getPredicatesForGalleryDetailViewPorts(criteriaBuilder, root, viewportMap);
			predicates.add(criteriaBuilder.isTrue(root.get(GalleryDetailConstants.GALLERY_ENABLED)));
			predicates.add(criteriaBuilder.isTrue(root.get(GalleryDetailConstants.GALLERY_IS_DELETED)));
			GalleryDetailUtils.getWhereClauseInCriteriaQuery(criteriaQuery, predicates);
			Query query = getEntityManager().createQuery(criteriaQuery);
			galleryDetails = query.getResultList();
		} catch (QueryTimeoutException queryTimeoutException) {
			logger.error("QueryTimeoutException occured in getting Gallery and Smile for visualization by southWestLong {} southWestLat{} northEastLong {} northEastLat {} Exception {} ",
					southWestLong, southWestLat, northEastLong, northEastLat, ExceptionUtils.getStackTrace(queryTimeoutException));
			throw new DaoException("Unable to visualize data for Gallery and Smile" + ExceptionUtils.getStackTrace(queryTimeoutException));
		} catch (Exception exception) {
			logger.error("Exception occured in getting Gallery and Smile for visualization by southWestLong {} southWestLat{} northEastLong {} northEastLat {} Exception {} ", southWestLong,
					southWestLat, northEastLong, northEastLat, ExceptionUtils.getStackTrace(exception));
			throw new DaoException("Unable to visualize data for Gallery and Smile By geographyName " + ExceptionUtils.getStackTrace(exception));
		}
		return galleryDetails;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GalleryDetail> getGallerySmileDataList(Integer llimit, Integer ulimit) {
		logger.info("Going to fetch Gallery and Smile Data List");
		List<GalleryDetail> gallerysmile = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getGalleryDetailDataList");
			if (llimit != null && ulimit != null && llimit >= 0 && ulimit > 0) {
				query.setMaxResults(ulimit - llimit + 1);
				query.setFirstResult(llimit);
			}
			gallerysmile = query.getResultList();
		} catch (Exception exception) {
			logger.error("Error while geting Gallery and Smile Data List {}", ExceptionUtils.getStackTrace(exception));
		}
		return gallerysmile;
	}

	@Override
	public int updateGalleryStatus(List<Integer> idList, Boolean enabled) {
		logger.info("Going to change gallary status to {} for the given list of id's {}", enabled, idList);
		int value = 0;
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaUpdate<GalleryDetail> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(GalleryDetail.class);
			Root<GalleryDetail> root = criteriaUpdate.from(GalleryDetail.class);
			criteriaUpdate.set(root.get(GalleryDetailConstants.GALLERY_ENABLED), enabled);
			Expression<Integer> parentExpression = root.get(GalleryDetailConstants.ID);
			Predicate parentPredicate = parentExpression.in(idList);
			criteriaUpdate.where(parentPredicate);
			Query query = getEntityManager().createQuery(criteriaUpdate);
			value = query.executeUpdate();
			logger.info("total no. of update record : {}", value);
		} catch (NoResultException noResultException) {
			logger.error("No results were found");
		} catch (QueryTimeoutException queryTimeoutException) {
			logger.error("Query was enable to execute in specified time");
		} catch (Exception e) {
			logger.error("Error while updating Gallery  status by id {} ", ExceptionUtils.getStackTrace(e));
		}
		return value;
	}

	@Override
	public Long getGallerySmileCount() {
		logger.info("Inside getGallerySmileCount method ");
		try {
			Query query = getEntityManager().createNamedQuery("getGalleryDetailCount");
			return (Long) query.getSingleResult();
		} catch (Exception exception) {
			logger.error("Error while geting Gallery and Smile Count {}", ExceptionUtils.getStackTrace(exception));
		}
		return null;
	}

	@Override
	public Long getGallerySmileCountBySearchName(String name) {
		logger.info("Going to count Gallery and Smile by searching through name");
		try {
			Query query = getEntityManager().createNamedQuery("getsearchByNameGalleryCount").setParameter(GalleryDetailConstants.GALLERY_NAME,
					ForesightConstants.PERCENT + name + ForesightConstants.PERCENT);
			return (Long) query.getSingleResult();
		} catch (Exception exception) {
			logger.error("error while geting Gallery and Smile Count {}", ExceptionUtils.getStackTrace(exception));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GalleryDetail> searchByName(String name, Integer llimit, Integer ulimit) {
		logger.info("Inside searchByName method");
		List<GalleryDetail> list = new ArrayList<>();
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<GalleryDetail> criteriaQuery = criteriaBuilder.createQuery(GalleryDetail.class);
			Root<GalleryDetail> root = criteriaQuery.from(GalleryDetail.class);
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(criteriaBuilder.like(root.get(GalleryDetailConstants.GALLERY_NAME), ForesightConstants.PERCENT + name + ForesightConstants.PERCENT));
			criteriaQuery.where(predicates.toArray(new Predicate[] {}));
			Query query = getEntityManager().createQuery(criteriaQuery);
			if (llimit != null && ulimit != null && llimit >= 0 && ulimit > 0) {
				query.setMaxResults(ulimit - llimit + 1);
				query.setFirstResult(llimit);
			}
			list = query.getResultList();
		} catch (Exception exception) {
			logger.error("Error while geting Gallery and Smile Data {}", ExceptionUtils.getStackTrace(exception));
		}
		return list;
	}

}
