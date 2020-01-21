package com.inn.foresight.core.infra.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.inn.commons.lang.DateUtils;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.DateUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.INEBandDetailDao;
import com.inn.foresight.core.infra.model.NEBandDetail;
import com.inn.foresight.core.infra.model.NETaskDetail;
import com.inn.foresight.core.infra.utils.CriteriaUtils;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.InfraUtils;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.utils.enums.Technology;
import com.inn.foresight.core.infra.utils.enums.Vendor;
import com.inn.foresight.core.infra.wrapper.NeDataWrapper;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;
import com.inn.foresight.core.infra.wrapper.SiteDataWrapper;

@Repository("NEBandDetailDaoImpl")
public class NEBandDetailDaoImpl extends HibernateGenericDao<Integer, NEBandDetail> implements INEBandDetailDao {

	public NEBandDetailDaoImpl() {
		super(NEBandDetail.class);
	}

	private Logger logger = LogManager.getLogger(NEBandDetailDaoImpl.class);

	private Map<String, NeDataWrapper> nEMapping;

	@Override
	@SuppressWarnings("unchecked")
	public List<NEBandDetail> getNEBandDetails(List<NEType> neTypeList, List<String> neNameList, List<String> neFrequencyList, List<NEStatus> neStatusList, List<Vendor> vendorList,
			List<Technology> technologyList, List<Domain> domainList, Map<String, Double> viewportMap, Map<String, List<String>> geographyNames) {
		try {
			logger.info("Going to get NEBandDetail for neType : {} , neFrequency : {} , neStatus: {} , vendor : {} ", neTypeList, neFrequencyList, neStatusList, vendorList);
			neNameList = InfraUtils.convertListElements(neNameList, InfraConstants.LIST_TOUPPERCASE);
			neFrequencyList = InfraUtils.convertListElements(neFrequencyList, InfraConstants.LIST_TOUPPERCASE);
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<NEBandDetail> criteriaQuery = criteriaBuilder.createQuery(NEBandDetail.class);
			Root<NEBandDetail> neBandDetail = criteriaQuery.from(NEBandDetail.class);
			List<Predicate> predicates = InfraUtils.getPredicatesForNEBandDetail(criteriaBuilder, neBandDetail, neTypeList, neNameList, neFrequencyList, neStatusList, vendorList, technologyList,
					domainList, geographyNames, viewportMap);
			predicates.add(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LATITUDE_KEY).isNotNull());
			predicates.add(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_LONGITUDE_KEY).isNotNull());
			criteriaQuery.select(neBandDetail).where(predicates.toArray(new Predicate[] {}));
			criteriaQuery.groupBy(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NENAME_KEY), neBandDetail.get(InfraConstants.NE_BANDSTATUS_KEY));
			criteriaQuery.orderBy(criteriaBuilder.asc(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY).get(InfraConstants.NE_NENAME_KEY)));
			Query query = getEntityManager().createQuery(criteriaQuery);
			return query.getResultList();
		} catch (NullPointerException nullPointerException) {
			logger.error("NullPointerException caught while getting data from NEBandDetail Exception : {}", Utils.getStackTrace(nullPointerException));
			throw new DaoException("Unable to get NEBandDetail  data for specific parameters.");
		} catch (IllegalArgumentException illegalArgumentException) {
			logger.error("IllegalArgumentException caught while getting data from NEBandDetail Exception : {}", Utils.getStackTrace(illegalArgumentException));
			throw new DaoException("Unable  to get NEBandDetail data for specific parameters.");
		} catch (NoResultException noResultException) {
			logger.error("NoResultException caught while getting data from NEBandDetail Exception : {}", Utils.getStackTrace(noResultException));
			throw new DaoException("Unable to  get NEBandDetail data for specific  parameters.");
		} catch (PersistenceException persistenceException) {
			logger.error("PersistenceException caught while getting data from NEBandDetail Exception : {}", Utils.getStackTrace(persistenceException));
			throw new DaoException("Unable to get NEBandDetail data for specific parameters.");
		} catch (Exception exception) {
			logger.error("Exception caught  while getting data from NEBandDetail Exception : {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get NEBandDetail data for specific parameters.");
		}
	}

	@Override
	public List<SiteDataWrapper> getNeBandDetailBySite(List<String> neId) {
		logger.info("Going to get NEBandDetail {}", neId);
		List<SiteDataWrapper> siteDetaiList = null;
		try {
			Query query = getEntityManager().createNamedQuery("getNeBandDetailBySite").setParameter("neId", neId);
			return query.getResultList();
		} catch (PersistenceException ex) {
			logger.error("Exception caught while getting data from NEBandDetail Exception : {}", Utils.getStackTrace(ex));
		}
		return siteDetaiList;
	}

	@Override
	public List<NEBandDetail> checkForSitesIsOnAirOrNot(NEStatus status, String siteName, String band) {
		logger.info("Going to get checkForSitesIsOnAirOrNot @status:{} , @siteName:{} , @band:{}", status, siteName, band);
		List<NEBandDetail> neBandDetail = null;
		try {
			Query query = getEntityManager().createNamedQuery("checkForSitesIsOnAirOrNot").setParameter("neStatus", status).setParameter("nename", siteName).setParameter("band", band);
			return query.getResultList();
		} catch (PersistenceException ex) {
			logger.error("Exception caught while getting data from NEBandDetail Exception : {}", Utils.getStackTrace(ex));
		}
		return neBandDetail;
	}

	@Override
	public String getMorphologyByCode(String btsName) {
		String morphology = null;
		try {
			logger.info("Going to get morphology by code ");
			if (btsName != null) {
				Query query = getEntityManager().createNamedQuery("getMorphologyByCode").setParameter("btsName", btsName);
				return (String) query.getSingleResult();
			}
		} catch (PersistenceException | NullPointerException exception) {
			logger.error("Exception caught while getting distinct Morphology count {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to getMorphology");
		} catch (Exception exception) {
			logger.error("Exception caught while getting distinct Morphology count  {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get Morphology");
		}
		return null;
	}

	@Override
	public List<NeDataWrapper> getNeData() {
		List<NeDataWrapper> resultList = new ArrayList<>();
		try {
			Query query = getEntityManager().createNamedQuery("getNEIdAndSiteName");
			resultList = query.getResultList();
		} catch (Exception e) {
			logger.error("getting error in neName and SiteName , exception is : {}", Utils.getStackTrace(e));
		}
		return resultList;
	}

	@Override
	public void intializeNeMap() {
		nEMapping = new HashMap<>();
		List<NeDataWrapper> neData = getNeData();
		for (NeDataWrapper netElement : neData) {
			nEMapping.put(netElement.getpNeId(), netElement);
		}
		logger.info("NE data map size is : {}", nEMapping.size());
	}

	@Override
	public Map<String, NeDataWrapper> getNeMap() {
		if (MapUtils.isEmpty(nEMapping)) {
			intializeNeMap();
		}
		return nEMapping;
	}

	@Override
	public List<String> getNefrequencyForNename(String neName) {
		List<String> neFrequencyList = new ArrayList<>();
		try {
			logger.info("Going to get distinct morphology");
			Query query = getEntityManager().createNamedQuery("getNefrequencyForNename");
			query.setParameter("neName", neName);
			neFrequencyList = query.getResultList();
			logger.info(" Distinct nefrequecy For nename : {}", neFrequencyList);
			if (neFrequencyList != null)
				return neFrequencyList;
		} catch (PersistenceException | NullPointerException exception) {
			logger.error("Exception caught while getting distinct nefrequency count {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get Distinct Morphology");
		} catch (Exception exception) {
			logger.error("Exception caught while getting distinct nefrequency count  {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get Distinct Morphology");
		}
		return neFrequencyList;

	}

	@Override
	public List<NetworkElementWrapper> getNetworkElementWrapperForNename(List<String> neName) {
		List<NetworkElementWrapper> networkElementWrapperList = new ArrayList<>();
		try {
			logger.info("Going to get networkElementWrapper for nename");
			Query query = getEntityManager().createNamedQuery("getNetworkElementWrapperForNename");
			query.setParameter("neName", neName);
			return query.getResultList();
		} catch (PersistenceException | NullPointerException exception) {
			logger.error("Exception caught while getting wrapper list {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get wrapper");
		} catch (Exception exception) {
			logger.error("Exception caught while getting wrapper list  {}", Utils.getStackTrace(exception));
			throw new DaoException("Unable to get wrapper");
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Tuple> getSiteBandDetail(Map<String, Double> viewMap, Map<String, List<Map>> filters, Map<String, List<String>> projection, List<String> distinctEntity, Boolean isGroupBy,
			List<String> geographyList, Boolean distinctValue) {
		Map criteriaMap = new HashMap<>();
		Map<String, Root> rootMap = new HashMap<>();
		try {
			logger.info("Going to get sites count bandwise filters {},projection {} ", filters, projection);
			List<Predicate> predicateList = new ArrayList<>();
			List<Selection<?>> selection = new ArrayList<>();
			List<Expression<?>> expression = new ArrayList<>();
			distinctEntity = InfraUtils.getDistinctEntity(distinctEntity, filters, projection);
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
			Root<NEBandDetail> neBandDetail = criteriaQuery.from(NEBandDetail.class);
			distinctEntity.remove(InfraConstants.NEBANDDETAIL_TABLE);
			rootMap = CriteriaUtils.getRootMap(distinctEntity, criteriaQuery);

			predicateList = CriteriaUtils.getPredicates(criteriaBuilder, neBandDetail, predicateList, filters.get(InfraConstants.NEBANDDETAIL_TABLE));
			selection = CriteriaUtils.getSelectionForSites(neBandDetail, projection.get(InfraConstants.NEBANDDETAIL_TABLE), selection);
			expression = CriteriaUtils.getExpressionForSites(neBandDetail, projection.get(InfraConstants.NEBANDDETAIL_TABLE), expression, isGroupBy);
			criteriaMap = CriteriaUtils.getNECriteriaForBandDetail(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE), viewMap, criteriaBuilder, predicateList, criteriaMap, neBandDetail, filters,
					selection, isGroupBy, expression, projection, criteriaQuery, distinctEntity);

			filters.remove(InfraConstants.NETWORKELEMENT_TABLE);
			projection.remove(InfraConstants.NETWORKELEMENT_TABLE);
			filters.remove(InfraConstants.NEBANDDETAIL_TABLE);
			projection.remove(InfraConstants.NEBANDDETAIL_TABLE);

			predicateList = CriteriaUtils.getCriteriaParametersForBand(viewMap, criteriaBuilder, (List<Predicate>) criteriaMap.get("PREDICATE"), filters, neBandDetail, criteriaQuery, geographyList,
					criteriaMap, rootMap);
			predicateList = CriteriaUtils.getCriteriaPredicates(criteriaBuilder, rootMap, predicateList, filters);
			selection = CriteriaUtils.getSelectionForCriteriaBuilder(rootMap, projection, (List<Selection<?>>) criteriaMap.get("SELECTION"));
			expression = CriteriaUtils.getExpressionForCriteriaBuilder(rootMap, projection, (List<Expression<?>>) criteriaMap.get("EXPRESSION"), isGroupBy);

			if (isGroupBy) {
				selection.add(criteriaBuilder.count(neBandDetail.get("networkElement")).alias("networkElement"));
				criteriaQuery.groupBy(expression.toArray(new Expression<?>[] {}));
			}
			if (selection != null && !selection.isEmpty()) {
				criteriaQuery.select(criteriaBuilder.tuple(selection.toArray(new Selection<?>[] {})));
			}
			if (predicateList != null && !predicateList.isEmpty()) {
				criteriaQuery.where(predicateList.toArray(new Predicate[] {}));
			}

			Query query = getEntityManager().createQuery(criteriaQuery);

			List<Tuple> tuples = query.getResultList();
			return tuples;
		} catch (Exception exception) {
			logger.error("Exception caught while getting data from NetworkElement Exception : {}", Utils.getStackTrace(exception));
			throw new DaoException("Data Not Recieved For Site Visualisation");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Tuple> getSitesDataForVisualisation(Map<String, Double> viewPortMap, Map<String, List<Map>> filters, Map<String, List<String>> projection, List<String> distinctEntity,
			Boolean isGroupBy, List<String> geographyList) {
		Map criteriaMap = new HashMap<>();
		Map<String, Root> rootMap = new HashMap<>();
		try {
			logger.info("Going to get sites data visualization");
			List<Predicate> predicateList = new ArrayList<>();
			List<Selection<?>> selection = new ArrayList<>();
			List<Expression<?>> expression = new ArrayList<>();
			distinctEntity = InfraUtils.getDistinctEntity(distinctEntity, filters, projection);

			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
			Root<NEBandDetail> neBandDetail = criteriaQuery.from(NEBandDetail.class);
			distinctEntity.remove(InfraConstants.NEBANDDETAIL_TABLE);
			rootMap = CriteriaUtils.getRootMap(distinctEntity, criteriaQuery);

			predicateList = CriteriaUtils.getPredicates(criteriaBuilder, neBandDetail, predicateList, filters.get(InfraConstants.NEBANDDETAIL_TABLE));
			selection = CriteriaUtils.getSelectionForSites(neBandDetail, projection.get(InfraConstants.NEBANDDETAIL_TABLE), selection);
			expression = CriteriaUtils.getExpressionForSites(neBandDetail, projection.get(InfraConstants.NEBANDDETAIL_TABLE), expression, isGroupBy);
			criteriaMap = CriteriaUtils.getNECriteriaForSearch(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE), viewPortMap, criteriaBuilder, predicateList, criteriaMap, neBandDetail, filters,
					selection, isGroupBy, expression, projection, criteriaQuery, distinctEntity);

			filters.remove(InfraConstants.NETWORKELEMENT_TABLE);
			projection.remove(InfraConstants.NETWORKELEMENT_TABLE);
			filters.remove(InfraConstants.NEBANDDETAIL_TABLE);
			projection.remove(InfraConstants.NEBANDDETAIL_TABLE);

			predicateList = CriteriaUtils.getCriteriaParametersForBand(viewPortMap, criteriaBuilder, (List<Predicate>) criteriaMap.get(InfraConstants.PREDICATE), filters, neBandDetail, criteriaQuery,
					geographyList, criteriaMap, rootMap);
			predicateList = CriteriaUtils.getCriteriaPredicates(criteriaBuilder, rootMap, predicateList, filters);
			selection = CriteriaUtils.getSelectionForCriteriaBuilder(rootMap, projection, (List<Selection<?>>) criteriaMap.get(InfraConstants.SELECTION));
			expression = CriteriaUtils.getExpressionForCriteriaBuilder(rootMap, projection, (List<Expression<?>>) criteriaMap.get(InfraConstants.EXPRESSION), isGroupBy);

			if (isGroupBy) {
				selection.add(criteriaBuilder.count(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY)).alias(InfraConstants.NE_NETWORKELEMENT_KEY));
				criteriaQuery.groupBy(expression.toArray(new Expression<?>[] {}));
			}
			if (selection != null && !selection.isEmpty()) {
				criteriaQuery.select(criteriaBuilder.tuple(selection.toArray(new Selection<?>[] {})));
			}
			if (predicateList != null && !predicateList.isEmpty()) {
				criteriaQuery.where(predicateList.toArray(new Predicate[] {}));
			}

			Query query = getEntityManager().createQuery(criteriaQuery);

			List<Tuple> tuples = query.getResultList();
			return tuples;
		} catch (Exception exception) {
			logger.error("Exception caught while getting data from NetworkElement Exception : {}", Utils.getStackTrace(exception));
			throw new DaoException("Data Not Recieved For Site Visualisation");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Tuple> getSitesPlannedDataForVisualisation(Map<String, Double> viewPortMap, Map<String, List<Map>> filters, Map<String, List<String>> projection, List<String> distinctEntity,
			Boolean isGroupBy, List<String> geographyList) {
		try {
			logger.info("Going to get sites data visualization for planned sites {} ,projection {}", filters, projection);
			List<Predicate> predicateList = new ArrayList<>();
			List<Selection<?>> selection = new ArrayList<>();
			List<Expression<?>> expression = new ArrayList<>();
			Map criteriaMap = new HashMap<>();
			Map<String, Root> rootMap = new HashMap<>();
			filters.remove(InfraConstants.RANDETAIL_TABLE);
			projection.remove(InfraConstants.RANDETAIL_TABLE);
			distinctEntity = InfraUtils.getDistinctEntity(distinctEntity, filters, projection);

			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
			Root<NEBandDetail> neBandDetail = criteriaQuery.from(NEBandDetail.class);
			distinctEntity.remove(InfraConstants.NEBANDDETAIL_TABLE);
			rootMap = CriteriaUtils.getRootMap(distinctEntity, criteriaQuery);

			predicateList = CriteriaUtils.getPredicates(criteriaBuilder, neBandDetail, predicateList, filters.get(InfraConstants.NEBANDDETAIL_TABLE));
			selection = CriteriaUtils.getSelectionForSites(neBandDetail, projection.get(InfraConstants.NEBANDDETAIL_TABLE), selection);
			expression = CriteriaUtils.getExpressionForSites(neBandDetail, projection.get(InfraConstants.NEBANDDETAIL_TABLE), expression, isGroupBy);
			criteriaMap = CriteriaUtils.getNESearchForPlannedSites(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE), viewPortMap, criteriaBuilder, predicateList, criteriaMap, neBandDetail, filters,
					selection, isGroupBy, expression, projection, criteriaQuery, distinctEntity);

			filters.remove(InfraConstants.NETWORKELEMENT_TABLE);
			projection.remove(InfraConstants.NETWORKELEMENT_TABLE);
			filters.remove(InfraConstants.NEBANDDETAIL_TABLE);
			projection.remove(InfraConstants.NEBANDDETAIL_TABLE);

			predicateList = CriteriaUtils.getCriteriaParametersForBand(viewPortMap, criteriaBuilder, (List<Predicate>) criteriaMap.get(InfraConstants.PREDICATE), filters, neBandDetail, criteriaQuery,
					geographyList, criteriaMap, rootMap);
			predicateList = CriteriaUtils.getCriteriaPredicates(criteriaBuilder, rootMap, predicateList, filters);
			selection = CriteriaUtils.getSelectionForCriteriaBuilder(rootMap, projection, (List<Selection<?>>) criteriaMap.get(InfraConstants.SELECTION));

			expression = CriteriaUtils.getExpressionForCriteriaBuilder(rootMap, projection, (List<Expression<?>>) criteriaMap.get(InfraConstants.EXPRESSION), isGroupBy);

			if (isGroupBy) {
				selection.add(criteriaBuilder.count(neBandDetail.get(InfraConstants.NE_NETWORKELEMENT_KEY)).alias(InfraConstants.NE_NETWORKELEMENT_KEY));
				criteriaQuery.groupBy(expression.toArray(new Expression<?>[] {}));
			}
			if (selection != null && !selection.isEmpty()) {
				criteriaQuery.select(criteriaBuilder.tuple(selection.toArray(new Selection<?>[] {})));
			}
			if (predicateList != null && !predicateList.isEmpty()) {
				criteriaQuery.where(predicateList.toArray(new Predicate[] {}));
			}

			Query query = getEntityManager().createQuery(criteriaQuery);
			List<Tuple> tuples = query.getResultList();
			logger.info("Data recieved For planned site and size is {} ", tuples.size());
			return tuples;
		} catch (Exception exception) {
			logger.error("Exception caught while getting data from NetworkElement Exception : {}", Utils.getStackTrace(exception));
			throw new DaoException("Data Not Recieved For Site Visualisation");
		}
	}

	@Override
	public NEBandDetail checkNetworkElementInNeBandDetail(int networkElementFk) {
		logger.info("inside checkNetworkElementInNeBandDetail method and networkElementFk is :{}", networkElementFk);
		NEBandDetail neBandDetail = null;
		try {
			TypedQuery<NEBandDetail> query = getEntityManager().createNamedQuery("searchNeBandDetailByNetworkElement", NEBandDetail.class);
			query.setParameter("networkElementFk", networkElementFk);
			neBandDetail = query.getSingleResult();
		} catch (NoResultException nre) {
			logger.error("NO Record Found for networkElement fk :{}  Exception :{}", networkElementFk, nre.getMessage());
		} catch (Exception e) {
			logger.error("getting exception while fetching NebandDetail :{}", ExceptionUtils.getStackTrace(e));
			throw new DaoException("getting exception while fetching NebandDetail");
		}
		return neBandDetail;
	}

	@Override
	public List<Object> executeQuery(String userQuery) {
		logger.info("Going to execute user query, {}", userQuery);
		try {
			return getEntityManager().createQuery(userQuery, Object.class).getResultList();
		} catch (Exception exception) {
			logger.error("Inside executeQuery, {}", ExceptionUtils.getStackTrace(exception));
		}
		return new ArrayList<>();
	}

	@Override
	public void updateNeBandStatusByNetworkElement(Integer id, NEStatus status) {
		logger.info("inside updateNebandStatus method having riu id :{}", id);
		try {
			Query query = getEntityManager().createNamedQuery("updateBandStatusInNeBand").setParameter("riuNetworkElement", id).setParameter("status", status);
			int updateRecordCount = query.executeUpdate();
			logger.info("update record :{}", updateRecordCount);
		} catch (Exception e) {
			logger.error("Getting Error while updating band Status in neBandDetail");
			throw new BusinessException("Exception Occured while updating NeBand Status");
		}

	}

	@Override
	public List<Object[]> getOnAirSiteDetailsWithOnAirDate(String neStatus, List<NEType> neType, String domain, String vendor, String previousDate,String currentDate,String geographyType,String geographyValue) {
		logger.info("inside getOnAirSiteDetailsWithOnAirDate NEStatus {}, NEType {}, Domain {}, Vendor {} , previousDate {}", neStatus, neType, domain, vendor, previousDate);
		try {
			List<Object[]> result = new ArrayList<>();
			Query query = getEntityManager().createNamedQuery("getOnAirSiteDetailsWithOnAirDate");
			query.setParameter("domain", Domain.valueOf(domain));
			query.setParameter("vendor", Vendor.valueOf(vendor));
			query.setParameter("neType", neType);
			query.setParameter("neStatus", NEStatus.valueOf(neStatus));
			query.setParameter("previousHour", previousDate);
			query.setParameter("currentHour", currentDate);
			enableGeographyFilters(geographyType, geographyValue);
			result = query.getResultList();
			disableGeographyFilters();
			return result;
		} catch (Exception e) {
			logger.error("Getting Error in getOnAirSiteDetailsWithOnAirDate {}", Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public Integer getOnAirSitesCountForDayMinusOne(List<NEType> neType, String domain, String vendor, String neStatus, String previousDate) {
		Integer siteCount = 0;
		try {
			Query query = getEntityManager().createNamedQuery("getOnAirSitesCountForDayMinusOne").setParameter("neType", neType).setParameter("domain", Domain.valueOf(domain))
					.setParameter("vendor", Vendor.valueOf(vendor)).setParameter("neStatus", NEStatus.valueOf(neStatus)).setParameter("previousDate", previousDate);
			return ((Long) query.getSingleResult()).intValue();
		} catch (Exception e) {
			logger.error("Exception inside method getOnAirSitesCount StackTrace {}", Utils.getStackTrace(e));
		}
		return siteCount;
	}

	@Override
	public Map<String, Long> getSitesCountByNestatus(List<NEType> neType, String date) {
		Map<String, Long> sitesCountNestatusWise = new HashMap<>();
		try {
			Date date1 = DateUtil.parseStringToDate("yyyy-MM-dd", date);
			Date finalDate = DateUtils.addDays(date1, 1);
			logger.info("finalDate : {}", finalDate);
			Query query = getEntityManager().createNamedQuery("getOnAirSitesCountByDate").setParameter("neType", neType).setParameter("finalDate", finalDate);
			Long onAirSitesCountList = (Long) query.getSingleResult();
			sitesCountNestatusWise.put("ONAIR", onAirSitesCountList);
			Query query1 = getEntityManager().createNamedQuery("getPlannedSitesCountByDate").setParameter("neType", neType).setParameter("finalDate", finalDate);
			Long plannedSitesCountList = (Long) query1.getSingleResult();
			sitesCountNestatusWise.put("PLANNED", plannedSitesCountList);

		} catch (Exception e) {
			logger.error("Exception in getSitesCountByNestatus - {}", Utils.getStackTrace(e));
		}
		return sitesCountNestatusWise;
	}
	
	@Override
	public Map<String, Long> getSitesCountNestatusWiseAndGeographyWise(List<NEType> neType, String domain, String vendor, List<String> neStatus,String geographyType,String geographyValue) {
		Map<String, Long> sitesCountNestatusWise = new HashMap<>();
		try {
			Query query = getEntityManager().createNamedQuery("getSitesCountNestatusAndGeographyWise").setParameter("neType", neType).setParameter("domain", Domain.valueOf(domain))
					.setParameter("vendor", Vendor.valueOf(vendor)).setParameter("neStatus", neStatus.stream().map(NEStatus::valueOf).collect(Collectors.toList()));
			enableGeographyFilters(geographyType, geographyValue);
			List<Object[]> siteCountList = query.getResultList();
			disableGeographyFilters();
			for (Object obj[] : siteCountList) {
				sitesCountNestatusWise.put(String.valueOf((NEStatus) obj[0]), (Long) obj[1]);
			}
		} catch (Exception e) {
			logger.error("Exception inside method getOnAirSitesCount StackTrace {}", Utils.getStackTrace(e));
		}
		return sitesCountNestatusWise;
	}
	
	private void enableGeographyFilters(String geographyType,String geographyValue) {
		logger.info("Site On Air Details Geography filter for Geography -   {}", geographyValue);
			Session s = (Session) getEntityManager().getDelegate();
			if(ForesightConstants.L4.equalsIgnoreCase(geographyType)) {
				s.enableFilter("geographyGL4Filter").setParameter(ForesightConstants.GEOGRAPHYVALUE, geographyValue);
			}else if(ForesightConstants.L3.equalsIgnoreCase(geographyType)){
				s.enableFilter("geographyGL3Filter").setParameter(ForesightConstants.GEOGRAPHYVALUE, geographyValue);
			}else if(ForesightConstants.L2.equalsIgnoreCase(geographyType)) {
				s.enableFilter("geographyGL2Filter").setParameter(ForesightConstants.GEOGRAPHYVALUE, geographyValue);
			}else if(ForesightConstants.L1.equalsIgnoreCase(geographyType)) {
				s.enableFilter("geographyGL1Filter").setParameter(ForesightConstants.GEOGRAPHYVALUE, geographyValue);
			}

	}
	
	private void disableGeographyFilters() {
		Session s = (Session) getEntityManager().getDelegate();
		s.disableFilter("geographyGL4Filter");
		s.disableFilter("geographyGL3Filter");
		s.disableFilter("geographyGL2Filter");
		s.disableFilter("geographyGL1Filter");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Tuple> getSitesOverviewDataForVisualisation(Map<String, List<Map>> filters, Map<String, List<String>> projection) {
		Map criteriaMap = new HashMap<>();
		List<String> distinctEntity = new ArrayList<>();
		Map<String, Root> rootMap = new HashMap<>();
		try {
			logger.info("Going to get sites data visualization");
			List<Predicate> predicateList = new ArrayList<>();
			List<Selection<?>> selection = new ArrayList<>();
			List<Expression<?>> expression = new ArrayList<>();
			distinctEntity = InfraUtils.getDistinctEntity(distinctEntity, filters, projection);

			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
			Root<NEBandDetail> neBandDetail = criteriaQuery.from(NEBandDetail.class);
			distinctEntity.remove(InfraConstants.NEBANDDETAIL_TABLE);
			
			rootMap = CriteriaUtils.getRootMap(distinctEntity, criteriaQuery);
			
			criteriaMap = CriteriaUtils.getNECriteriaForData(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE),criteriaBuilder, predicateList, criteriaMap, neBandDetail, filters,selection, expression, projection, criteriaQuery, distinctEntity);
            filters.remove(InfraConstants.NETWORKELEMENT_TABLE);
			projection.remove(InfraConstants.NETWORKELEMENT_TABLE);
			filters.remove(InfraConstants.NEBANDDETAIL_TABLE);
			projection.remove(InfraConstants.NEBANDDETAIL_TABLE);
			
            predicateList = CriteriaUtils.getCriteriaParametersForBand(null, criteriaBuilder, (List<Predicate>) criteriaMap.get(InfraConstants.PREDICATE), filters, neBandDetail, criteriaQuery,null, criteriaMap, rootMap);
			
            predicateList = CriteriaUtils.getCriteriaPredicates(criteriaBuilder, rootMap, predicateList, filters);
			
            
            selection = CriteriaUtils.getSelectionForCriteriaBuilder(rootMap, projection, (List<Selection<?>>) criteriaMap.get(InfraConstants.SELECTION));
			expression = CriteriaUtils.getExpressionForCB(rootMap, projection, (List<Expression<?>>) criteriaMap.get(InfraConstants.EXPRESSION));
			if (selection != null && !selection.isEmpty()) {
				criteriaQuery.select(criteriaBuilder.tuple(selection.toArray(new Selection<?>[] {})));
			}
			if (predicateList != null && !predicateList.isEmpty()) {
				criteriaQuery.where(predicateList.toArray(new Predicate[] {}));
			}

			Query query = getEntityManager().createQuery(criteriaQuery);

			List<Tuple> tuples = query.getResultList();
			return tuples;
		} catch (Exception exception) {
			logger.error("Exception caught while getting data from NetworkElement Exception : {}", Utils.getStackTrace(exception));
			throw new DaoException("Data Not Recieved For Site Visualisation");
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Tuple> getNETaskDetailDataForVisualisation(Map<String, List<Map>> filters, Map<String, List<String>> projection,Boolean isGroupBy) {
		Map criteriaMap = new HashMap<>();
		List<String> distinctEntity = new ArrayList<>();
		Map<String, Root> rootMap = new HashMap<>();
		try {
			logger.info("Going to get sites data visualization");
			logger.info("filters for netaskdetail: {}",filters);
			logger.info("projection for netaskdetail: {}",projection);
			List<Predicate> predicateList = new ArrayList<>();
			List<Selection<?>> selection = new ArrayList<>();
			List<Expression<?>> expression = new ArrayList<>();
			distinctEntity = InfraUtils.getDistinctEntity(distinctEntity, filters, projection);
			logger.info("distinctEntity: {}",distinctEntity);
			
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createQuery(Tuple.class);
			Root<NETaskDetail> neTaskDetail = criteriaQuery.from(NETaskDetail.class);
			distinctEntity.remove(InfraConstants.NETASKDETAIL_TABLE);
			rootMap = CriteriaUtils.getRootMap(distinctEntity, criteriaQuery);
			predicateList = CriteriaUtils.getPredicates(criteriaBuilder, neTaskDetail, predicateList, filters.get(InfraConstants.NETASKDETAIL_TABLE));
            selection = CriteriaUtils.getSelectionForSites(neTaskDetail, projection.get(InfraConstants.NETASKDETAIL_TABLE), selection);
            expression = CriteriaUtils.getExpressionForSites(neTaskDetail, projection.get(InfraConstants.NETASKDETAIL_TABLE), expression, isGroupBy);
			Root<NEBandDetail> neBandDetailRoot=rootMap.get("NEBandDetail");
            criteriaMap = CriteriaUtils.getNECriteriaForNETaskDetailData(rootMap.get(InfraConstants.NETWORKELEMENT_TABLE),
					criteriaBuilder, predicateList, criteriaMap, neTaskDetail, filters,selection, expression, projection,
					criteriaQuery, distinctEntity,neBandDetailRoot);

			filters.remove(InfraConstants.NETWORKELEMENT_TABLE);
			projection.remove(InfraConstants.NETWORKELEMENT_TABLE);
			filters.remove(InfraConstants.NETASKDETAIL_TABLE);
			projection.remove(InfraConstants.NETASKDETAIL_TABLE);

			predicateList = CriteriaUtils.getCriteriaPredicates(criteriaBuilder, rootMap, predicateList, filters);
			selection = CriteriaUtils.getSelectionForCriteriaBuilder(rootMap, projection, (List<Selection<?>>) criteriaMap.get(InfraConstants.SELECTION));
			expression = CriteriaUtils.getExpressionForCriteriaBuilder(rootMap, projection, (List<Expression<?>>) criteriaMap.get(InfraConstants.EXPRESSION), isGroupBy);

			if (isGroupBy) {
				criteriaQuery.groupBy(expression.toArray(new Expression<?>[] {}));
			}
			if (selection != null && !selection.isEmpty()) {
				criteriaQuery.select(criteriaBuilder.tuple(selection.toArray(new Selection<?>[] {})));
			}
			if (predicateList != null && !predicateList.isEmpty()) {
				criteriaQuery.where(predicateList.toArray(new Predicate[] {}));
			}
			criteriaQuery.orderBy(criteriaBuilder.asc(neBandDetailRoot.get(InfraConstants.NE_NEFREQUENCY_KEY)),criteriaBuilder.desc(neTaskDetail.get("executionOrder")));
			Query query = getEntityManager().createQuery(criteriaQuery);
			List<Tuple> tuples = query.getResultList();
			return tuples;
		} catch (Exception exception) {
			logger.error("Exception caught while getting data from NetworkElement Exception : {}", Utils.getStackTrace(exception));
			throw new DaoException("Data Not Recieved For Site Visualisation");
		}
	}

}
