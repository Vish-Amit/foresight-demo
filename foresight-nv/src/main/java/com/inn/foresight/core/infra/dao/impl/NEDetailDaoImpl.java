package com.inn.foresight.core.infra.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.INetworkElementDao;
import com.inn.foresight.core.infra.dao.NEDetailDao;
import com.inn.foresight.core.infra.model.NEDetail;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.Vendor;
import com.inn.foresight.core.infra.wrapper.NEDetailDataWrapper;
	
@Repository("NEDetailDaoImpl")
public class NEDetailDaoImpl extends HibernateGenericDao<Integer, NEDetail> implements NEDetailDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NEDetailDaoImpl.class);

	@Autowired
	INetworkElementDao networkElementDao;
	
	/**
	 * Instantiates a new NEDetail dao impl.
	 */
	public NEDetailDaoImpl() {
		super(NEDetail.class);
	}

	@Override
	public List<String> getDistinctVIPCategories() {
		logger.debug("getting distinct VIP categories");
		return getEntityManager().createNamedQuery("getDistinctVIPCategories", String.class).getResultList();
	}

	@Override
	public String getMorphologyByCode(String btsName) {
		logger.info("Going to get morphology by btsName : {}", btsName);
		String morphology = null;
		try {
			logger.info("Going to get morphology by code ");
			if (btsName != null) {
				Query query = getEntityManager().createNamedQuery("getMorphologyByCode").setParameter("btsName", btsName);
				morphology = (String) query.getSingleResult();
				logger.info(" morphology : {}", morphology);
			}
		} catch (PersistenceException | NullPointerException exception) {
			logger.error("PersistenceException | NullPointerException caught while getting distinct Morphology count {}", Utils.getStackTrace(exception));
		} catch (Exception exception) {
			logger.error("Exception caught while getting distinct Morphology count  {}", Utils.getStackTrace(exception));
		}
		return morphology;
	}
	
	@Override
	public List<String> getListOfVIPNetworkElement(List<String> neNameList, String domain, List<Vendor> vendorList) {
		logger.info("inside getListOfVIPNetworkElement with domain:{},vendor:{},Size of Nenamelist:{}", domain,
				vendorList, neNameList.size());
		List<String> resultList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getListOfVIPNetworkElementFromNeNameList");
			query.setParameter(ForesightConstants.NENAME_LIST, neNameList);
			query.setParameter(ForesightConstants.KEY_VENDOR, vendorList);
			query.setParameter(ForesightConstants.DOMAIN_SMALL, Domain.valueOf(domain));
			query.setParameter(ForesightConstants.CATEGORY,
					Arrays.asList(ForesightConstants.KEY_VIP, "Gallery", "Corporate SMG", "Airport",
							"VIP | Corporate SMG", "VVIP", "Sales Complaint\'s Place | HP Center", "VIP | Airport",
							"Sales + Gallery", "Sales Complaint\'s Place", "Common Complaint\'s Place | VIP",
							"High Revenue >100 Mio", "VVIP Rumah Pak FOW","|VIP|"));
			resultList = query.getResultList();
			logger.info("Size of resultList :" + resultList.size());
		} catch (Exception e) {
			logger.error("Exception while getting ListOfVIP Nenames Error:{}", ExceptionUtils.getStackTrace(e));
		}
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<NEDetailDataWrapper> getSiteParameterList(String searchValue, Integer llimit, Integer ulimit) {
		logger.info("Going to get site parameter site list  by search value : {} , llimit : {} and ulimit : {}", searchValue, llimit, ulimit);
		List<NEDetailDataWrapper> siteList = new ArrayList<>();
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NEDetailDataWrapper> criteriaQuery = criteriaBuilder.createQuery(NEDetailDataWrapper.class);
			Root<NEDetail> root = criteriaQuery.from(NEDetail.class);
			Join<NEDetail, NetworkElement> neRoot = root.join(InfraConstants.NE_NETWORKELEMENT_KEY, JoinType.INNER);
			criteriaQuery.select(criteriaBuilder.construct(NEDetailDataWrapper.class, neRoot.get(InfraConstants.NENAME), root.get(ForesightConstants.CATEGORY)));
			List<Predicate> predicateList = new ArrayList<>();
			predicateList.add(criteriaBuilder.equal(neRoot.get(InfraConstants.ISDELETED_STATUS), ForesightConstants.ZERO));
			predicateList.add(criteriaBuilder.isNull(neRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)));
			setSiteNameInWhereClause(searchValue, criteriaBuilder, neRoot, predicateList);
			criteriaQuery.where(predicateList.toArray(new Predicate[] {}));
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get(InfraConstants.MODIFICATION_TIME_KEY)));
			
			Query query = getEntityManager().createQuery(criteriaQuery);
			setPagination(llimit, ulimit, query);
			siteList = query.getResultList();
			logger.info("site class list size : {}", siteList.size());
		} catch (Exception e) {
			logger.error("Error in getting decommission site list : {}", Utils.getStackTrace(e));
		}
		return siteList;
	}

	private void setSiteNameInWhereClause(String searchValue, CriteriaBuilder criteriaBuilder, Join<NEDetail, NetworkElement> neRoot, List<Predicate> predicateList) {
		if (Utils.checkForValueInString(searchValue)) {
			predicateList.add(criteriaBuilder.like(criteriaBuilder.upper(neRoot.get(InfraConstants.NENAME)), ForesightConstants.PERCENT + searchValue.toUpperCase() + ForesightConstants.PERCENT));
		}
	}

	@Override
	public NEDetailDataWrapper getSiteDetailByNeName(String neName) {
		logger.info("Going to get site detail by site name : {}", neName);
		NEDetailDataWrapper siteWrapper = new NEDetailDataWrapper();
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NEDetailDataWrapper> criteriaQuery = criteriaBuilder.createQuery(NEDetailDataWrapper.class);
			Root<NEDetail> root = criteriaQuery.from(NEDetail.class);
			Join<NEDetail, NetworkElement> neRoot = root.join(InfraConstants.NE_NETWORKELEMENT_KEY, JoinType.INNER);
			criteriaQuery.select(criteriaBuilder.construct(NEDetailDataWrapper.class, neRoot.get(InfraConstants.NENAME), root.get(ForesightConstants.CATEGORY)));
			List<Predicate> predicateList = new ArrayList<>();
			predicateList.add(criteriaBuilder.equal(neRoot.get(InfraConstants.ISDELETED_STATUS), ForesightConstants.ZERO));
			predicateList.add(criteriaBuilder.isNull(neRoot.get(InfraConstants.NE_NETWORKELEMENT_KEY)));
			predicateList.add(criteriaBuilder.equal(criteriaBuilder.upper(neRoot.get(InfraConstants.NENAME)), neName.toUpperCase()));
			criteriaQuery.where(predicateList.toArray(new Predicate[] {}));
			siteWrapper = getEntityManager().createQuery(criteriaQuery).getSingleResult();
			logger.info("Site class list size : {}", siteWrapper);
		} catch (NoResultException noResult) {
			logger.error("Error in getting site data by Exception : {}", noResult.getMessage());
		} catch (Exception e) {
			logger.error("Error in getting site list : {}", Utils.getStackTrace(e));
		}
		return siteWrapper;
	}

	private void setPagination(Integer llimit, Integer ulimit, Query query) {
		logger.info("Going to set pagination ");
		if (llimit != null && ulimit != null && llimit >= 0 && ulimit > 0) {
			query.setMaxResults(ulimit - llimit + 1);
			query.setFirstResult(llimit);
		}
	}

	@Override
	public Boolean updateSiteClassByNeName(String neName, String category) {
		logger.info("Going to update site class :{}  by site name : {} ", category, neName);
		int count = 0;
		try {
			NetworkElement element = networkElementDao.searchSiteByName(neName);
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaUpdate<NEDetail> updateCategory = criteriaBuilder.createCriteriaUpdate(NEDetail.class);
			Root<NEDetail> root = updateCategory.from(NEDetail.class);
			updateCategory.set(root.get(ForesightConstants.CATEGORY), category);
			if (element != null) {
				updateCategory.where(criteriaBuilder.equal(root.get(InfraConstants.NE_NETWORKELEMENT_KEY), element.getId()));
			}
			count = getEntityManager().createQuery(updateCategory).executeUpdate();
			if (count != 0) {
				logger.info("Category updated successfully");
				return true;
			}
		} catch (NoResultException noResult) {
			logger.error("Error in updating site class by Exception : {}", noResult.getMessage());
		} catch (Exception e) {
			logger.error("Error in updating site class by site name : {}", Utils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public List<Object []> getClutterAndRadiusByNeName(String neName) {
		logger.info("Going to Get Clutrer And Radius By  site name : {} ", neName);
		List<Object[]> list = null;
		try
		{
			Query query =getEntityManager().createNamedQuery("getClutterAndRadiusByNeName");
			query.setParameter(ForesightConstants.NE_NAME, neName);
			list= query.getResultList();
			logger.info("List size : {}", list.size());
			
		}
		catch(Exception e)
		{
			logger.error("Error In Getting Radius and Clutter by Nename : {} ", Utils.getStackTrace(e));
		}
		return list;
	}
	
	
	

}
