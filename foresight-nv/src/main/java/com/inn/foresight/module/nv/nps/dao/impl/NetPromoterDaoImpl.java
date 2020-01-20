package com.inn.foresight.module.nv.nps.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.nps.constants.NetPromoterConstant;
import com.inn.foresight.module.nv.nps.dao.INetPromoterDao;
import com.inn.foresight.module.nv.nps.model.NPSAggDetail;
import com.inn.foresight.module.nv.nps.model.NetPromoterRaw;
import com.inn.foresight.module.nv.nps.model.NetPromoterWrapper;
import com.inn.foresight.module.nv.nps.utils.NetPromoterUtil;

/**
 * The Class NetPromoterDaoImpl.
 * 
 * @author innoeye
 */

@Repository("NetPromoterDaoImpl")
public class NetPromoterDaoImpl extends HibernateGenericDao<Integer, NetPromoterRaw> implements INetPromoterDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NetPromoterDaoImpl.class);

	/** Instantiates a new Net Promoter dao impl. */
	public NetPromoterDaoImpl() {
		super(NetPromoterRaw.class);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.inn.foresight.module.nv.nps.dao.INetPromoterDao#createNPSRawData(com.inn.
	 *      foresight.module.nv.nps.model.NetPromoterRaw)
	 */
	@Override
	public NetPromoterRaw createNPSRawData(NetPromoterRaw npswrapper) {
		logger.info("Going to insert Net Promoter Raw data");
		return super.create(npswrapper);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.inn.foresight.module.nv.nps.dao.INetPromoterDao#getNPSEventScoreData(java
	 *      .lang.Integer, java.lang.String, java.lang.String, java.lang.String,
	 *      java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NetPromoterWrapper> getNPSEventScoreData(Integer geographyId, String geographyType, String startDate,
			String endDate, Integer weekno, String operator, String technology) {
		List<NetPromoterWrapper> listOfData = new ArrayList<>();
		String processdate = startDate;
		try {

			logger.info("===1====technology===={}==", technology);
			CriteriaQuery<NetPromoterWrapper> criteriaQuery = getCriteriabuilderQuery(geographyType, processdate,
					weekno, operator, technology, geographyId);
			Query query = getEntityManager().createQuery(criteriaQuery);
			listOfData = query.getResultList();
			logger.info("===1====listSize===={}==", listOfData.size());
			return listOfData;
		} catch (Exception e) {
			logger.info("Exception in getNPSEventScoreData--> {} ", ExceptionUtils.getStackTrace(e));
			return listOfData;
		}

	}

	/**
	 * Gets the criteriabuilder query.
	 *
	 * @param geographyType the geography type
	 * @param processdate   the processdate
	 * @param weekno        the weekno
	 * @param operator      the operator
	 * @param technology    the technology
	 * @return the criteriabuilder query
	 */
	public CriteriaQuery<NetPromoterWrapper> getCriteriabuilderQuery(String geographyType, String processdate,
			Integer weekno, String operator, String technology, Integer geographyId) {

		List<Predicate> finalPredicates = new ArrayList<>();
		List<Selection<?>> listOfSelections = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<NetPromoterWrapper> criteriaQuery = criteriaBuilder.createQuery(NetPromoterWrapper.class);
		Root<NPSAggDetail> rootElement = criteriaQuery.from(NPSAggDetail.class);
		getPredicatesForCriteriaBuilder(criteriaBuilder, finalPredicates, rootElement, processdate, weekno, operator,
				technology, geographyId, geographyType);
		getSelectionsForCriteriaBuilder(criteriaBuilder, listOfSelections, rootElement);
		getMultiselectQuery(listOfSelections, finalPredicates, geographyType, criteriaQuery, rootElement);
		return criteriaQuery;

	}

	/**
	 * Gets the multiselect query.
	 *
	 * @param listOfSelections the list of selections
	 * @param finalPredicates  the final predicates
	 * @param criteriaBuilder  the criteria builder
	 * @param geographyType    the geography type
	 * @param criteriaQuery    the criteria query
	 * @param rootElement      the root element
	 * @return the multiselect query
	 */
	private void getMultiselectQuery(List<Selection<?>> listOfSelections, List<Predicate> finalPredicates,
			String geographyType, CriteriaQuery<NetPromoterWrapper> criteriaQuery, Root<NPSAggDetail> rootElement) {

		try {

			if (geographyType.equalsIgnoreCase(NetPromoterConstant.GEOGRAPHY_L0FK)) {
				criteriaQuery.multiselect(listOfSelections).where(finalPredicates.toArray(new Predicate[] {})).groupBy(
						rootElement.get(NetPromoterConstant.CUSTOMER_TYPE),
						rootElement.get(NetPromoterConstant.EVENT_TYPE));

			} else if (geographyType.equalsIgnoreCase(NetPromoterConstant.GEOGRAPHY_L4FK)) {

				criteriaQuery.multiselect(listOfSelections).where(finalPredicates.toArray(new Predicate[] {})).groupBy(
						rootElement.get(NetPromoterConstant.CUSTOMER_TYPE),
						rootElement.get(NetPromoterConstant.EVENT_TYPE));

			} else if (geographyType.equalsIgnoreCase(NetPromoterConstant.GEOGRAPHY_L3FK)) {

				criteriaQuery.multiselect(listOfSelections).where(finalPredicates.toArray(new Predicate[] {}))
						.groupBy(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk)
								.get(ForesightConstants.GEOGRAPHY_L3fk),rootElement.get(NetPromoterConstant.CUSTOMER_TYPE),
								rootElement.get(NetPromoterConstant.EVENT_TYPE));

			} else if (geographyType.equalsIgnoreCase(NetPromoterConstant.GEOGRAPHY_L2FK)) {
				criteriaQuery.multiselect(listOfSelections).where(finalPredicates.toArray(new Predicate[] {}))
						.groupBy(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk)
								.get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk),
								rootElement.get(NetPromoterConstant.CUSTOMER_TYPE),rootElement.get(NetPromoterConstant.EVENT_TYPE));

			} else if (geographyType.equalsIgnoreCase(NetPromoterConstant.GEOGRAPHY_L1FK)) {
				criteriaQuery.multiselect(listOfSelections).where(finalPredicates.toArray(new Predicate[] {}))
						.groupBy(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk)
								.get(ForesightConstants.GEOGRAPHY_L3fk).get(ForesightConstants.GEOGRAPHY_L2fk)
								.get(ForesightConstants.GEOGRAPHY_L1fk),rootElement.get(NetPromoterConstant.CUSTOMER_TYPE),
								rootElement.get(NetPromoterConstant.EVENT_TYPE));

			}
		} catch (Exception e) {
			logger.error("errro in eevent call {}" + Utils.getStackTrace(e));
		}

	}

	/**
	 * Gets the predicates for criteria builder.
	 *
	 * @param criteriaBuilder the criteria builder
	 * @param finalPredicates the final predicates
	 * @param rootElement     the root element
	 * @param processdate     the processdate
	 * @param weekno          the weekno
	 * @param operator        the operator
	 * @param technology      the technology
	 * @return the predicates for criteria builder
	 */
	private void getPredicatesForCriteriaBuilder(CriteriaBuilder criteriaBuilder, List<Predicate> finalPredicates,
			Root<NPSAggDetail> rootElement, String processdate, Integer weekno, String operator, String technology,
			Integer geographyId, String geographyType) {
		// .equals(technology)

		if (weekno == 0) {
			finalPredicates.add(criteriaBuilder.equal(rootElement.get(NetPromoterConstant.PROCESS_DATE), processdate));
		} else {
			finalPredicates.add(criteriaBuilder.equal(rootElement.get(NetPromoterConstant.WEEK_NO), weekno));
		}
		finalPredicates.add(criteriaBuilder.equal(rootElement.get(NetPromoterConstant.OPERATOR), operator));
		finalPredicates.add(criteriaBuilder.equal(rootElement.get(NetPromoterConstant.TECHNOLOGY), technology));
		setCriteriaForGeography(criteriaBuilder, finalPredicates, rootElement, geographyId, geographyType);
		finalPredicates
				.add(criteriaBuilder.equal(rootElement.get(NetPromoterConstant.KPI), NetPromoterConstant.KPI_ALL));

	}

	/**
	 * Gets the selections for criteria builder.
	 *
	 * @param criteriaBuilder  the criteria builder
	 * @param listOfSelections the list of selections
	 * @param finalPredicates  the final predicates
	 * @param rootElement      the root element
	 * @param isGeography      the is geography
	 * @return the selections for criteria builder
	 */
	private void getSelectionsForCriteriaBuilder(CriteriaBuilder criteriaBuilder, List<Selection<?>> listOfSelections,
			 Root<NPSAggDetail> rootElement) {

		Expression<Integer> sumOfCustomerCount = criteriaBuilder
				.sum(rootElement.get(NetPromoterConstant.CUSTOMER_COUNT));
		listOfSelections.add(rootElement.get(NetPromoterConstant.CUSTOMER_TYPE));
		listOfSelections.add(rootElement.get(NetPromoterConstant.EVENT_TYPE));
		listOfSelections.add(sumOfCustomerCount);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.inn.foresight.module.nv.nps.dao.INetPromoterDao#
	 *      getNPSMonthlyAnalysisDetail(java.lang.Integer, java.lang.String,
	 *      java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public List<NetPromoterWrapper> getNPSMonthlyAnalysisDetail(Integer geographyId, String geographyType,
			String startDate, String endDate, String operator, String technology) {
		List<NetPromoterWrapper> listOfData = new ArrayList<>();
		try {
			CriteriaQuery<NetPromoterWrapper> criteriaQuery = getCriteriabuilderQueryForNPSScoreAnalysis(geographyType,
					startDate, endDate, operator, technology, geographyId);
			Query query = getEntityManager().createQuery(criteriaQuery);
			listOfData = query.getResultList();
			logger.info("datalistSize {}", listOfData.size());
			return listOfData;
		} catch (Exception e) {
			logger.info("Exception in getNPSMonthlyAnalysisDetail--> {} ", ExceptionUtils.getStackTrace(e));
			return listOfData;
		}

	}

	/**
	 * Gets the criteriabuilder query for NPS score analysis.
	 *
	 * @param geographyType the geography type
	 * @param startDate     the start date
	 * @param endDate       the end date
	 * @param operator      the operator
	 * @param technology    the technology
	 * @return the criteriabuilder query for NPS score analysis
	 */
	public CriteriaQuery<NetPromoterWrapper> getCriteriabuilderQueryForNPSScoreAnalysis(String geographyType,
			String startDate, String endDate, String operator, String technology, Integer geographyId) {

		logger.info("inside getCriteriabuilderQuery");
		List<Predicate> finalPredicates = new ArrayList<>();
		List<Selection<?>> listOfSelections = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<NetPromoterWrapper> criteriaQuery = criteriaBuilder.createQuery(NetPromoterWrapper.class);

		Root<NPSAggDetail> rootElement = criteriaQuery.from(NPSAggDetail.class);

		getPredicatesForCriteriaBuilderForNPSScoreAnalysis(criteriaBuilder, finalPredicates, rootElement, startDate,
				endDate, operator, technology, geographyId, geographyType);

		getSelectionsForCriteriaBuilderForNPSScoreAnalysis(criteriaBuilder, listOfSelections, rootElement);

		getMultiselectQueryForNPSScoreAnalysis(listOfSelections, finalPredicates, criteriaBuilder, geographyType,
				criteriaQuery, rootElement);

		return criteriaQuery;

	}

	/**
	 * Gets the predicates for criteria builder for NPS score analysis.
	 *
	 * @param criteriaBuilder the criteria builder
	 * @param finalPredicates the final predicates
	 * @param rootElement     the root element
	 * @param startDate       the start date
	 * @param endDate         the end date
	 * @param operator        the operator
	 * @param technology      the technology
	 * @return the predicates for criteria builder for NPS score analysis
	 */
	private void getPredicatesForCriteriaBuilderForNPSScoreAnalysis(CriteriaBuilder criteriaBuilder,
			List<Predicate> finalPredicates, Root<NPSAggDetail> rootElement, String startDate, String endDate,
			String operator, String technology, Integer geographyId, String geographyType) {
		finalPredicates
				.add(criteriaBuilder.between(rootElement.get(NetPromoterConstant.PROCESS_DATE), endDate, startDate));
		finalPredicates.add(criteriaBuilder.equal(rootElement.get(NetPromoterConstant.OPERATOR), operator));
		finalPredicates.add(criteriaBuilder.equal(rootElement.get(NetPromoterConstant.TECHNOLOGY), technology));
		setCriteriaForGeography(criteriaBuilder, finalPredicates, rootElement, geographyId, geographyType);
		finalPredicates
				.add(criteriaBuilder.equal(rootElement.get(NetPromoterConstant.KPI), NetPromoterConstant.KPI_ALL));

	}

	/**
	 * Gets the selections for criteria builder for NPS score analysis.
	 *
	 * @param criteriaBuilder  the criteria builder
	 * @param listOfSelections the list of selections
	 * @param finalPredicates  the final predicates
	 * @param rootElement      the root element
	 * @return the selections for criteria builder for NPS score analysis
	 */
	private void getSelectionsForCriteriaBuilderForNPSScoreAnalysis(CriteriaBuilder criteriaBuilder,
			List<Selection<?>> listOfSelections, Root<NPSAggDetail> rootElement) {
		logger.info("======inside getSelectionsForCriteriaBuilder=======");
		Expression<Integer> sumOfCustomerCount = criteriaBuilder
				.sum(rootElement.get(NetPromoterConstant.CUSTOMER_COUNT));
		listOfSelections.add(sumOfCustomerCount);
		listOfSelections.add(rootElement.get(NetPromoterConstant.CUSTOMER_TYPE));
		listOfSelections.add(rootElement.get(NetPromoterConstant.PROCESS_DATE));
	}

	/**
	 * Gets the multiselect query for NPS score analysis.
	 *
	 * @param listOfSelections the list of selections
	 * @param finalPredicates  the final predicates
	 * @param criteriaBuilder  the criteria builder
	 * @param geographyType    the geography type
	 * @param criteriaQuery    the criteria query
	 * @param rootElement      the root element
	 * @return the multiselect query for NPS score analysis
	 */
	private void getMultiselectQueryForNPSScoreAnalysis(List<Selection<?>> listOfSelections,
			List<Predicate> finalPredicates, CriteriaBuilder criteriaBuilder, String geographyType,
			CriteriaQuery<NetPromoterWrapper> criteriaQuery, Root<NPSAggDetail> rootElement) {
		if (geographyType.equalsIgnoreCase(NetPromoterConstant.GEOGRAPHY_L0FK)
				|| geographyType.equalsIgnoreCase(NetPromoterConstant.GEOGRAPHY_L4FK)) {
			geographyL1AndL4GroupBy(listOfSelections, finalPredicates, criteriaBuilder, rootElement, criteriaQuery,
					null);
		} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L3fk)) {
			geographyL3GroupBy(listOfSelections, finalPredicates, criteriaBuilder, rootElement, criteriaQuery, null);
		} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L2fk)) {
			geographyL2GroupBy(listOfSelections, finalPredicates, criteriaBuilder, rootElement, criteriaQuery, null);
		} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L1fk)) {
			geographyL1GroupBy(listOfSelections, finalPredicates, criteriaBuilder, rootElement, criteriaQuery, null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NetPromoterWrapper> getNPSData(Integer geographyId, String geographyType, String startDate,
			String endDate, List<Integer> weekno, String operator, String technology, String callType) {

		List<NetPromoterWrapper> listOfData = new ArrayList<>();
		try {

			CriteriaQuery<NetPromoterWrapper> criteriaQuery = getCriteriabuilderQueryForNPSData(geographyType,
					startDate, endDate, weekno, operator, technology, callType, geographyId);
			Query query = getEntityManager().createQuery(criteriaQuery);
			listOfData = query.getResultList();
			logger.info("listSize {}", listOfData);
			return listOfData;
		} catch (Exception e) {
			logger.info("Exception in getNPSData--> {} ", ExceptionUtils.getStackTrace(e));

			return listOfData;
		}

	}

	public CriteriaQuery<NetPromoterWrapper> getCriteriabuilderQueryForNPSData(String geographyType, String startDate,
			String endDate, List<Integer> weekNo, String operator, String technology, String callType,
			Integer geographyId) {

		List<Predicate> finalPredicates = new ArrayList<>();
		List<Selection<?>> listOfSelections = new ArrayList<>();
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<NetPromoterWrapper> criteriaQuery = criteriaBuilder.createQuery(NetPromoterWrapper.class);

		Root<NPSAggDetail> rootElement = criteriaQuery.from(NPSAggDetail.class);
		getPredicatesForCriteriaBuilderForNPSData(criteriaBuilder, finalPredicates, rootElement, startDate, endDate,
				weekNo, operator, technology, callType, geographyId, geographyType);

		getSelectionsForCriteriaBuilderForNPSData(criteriaBuilder, listOfSelections, rootElement, callType);

		getMultiselectQueryForNPSData(listOfSelections, finalPredicates, criteriaBuilder, geographyType, criteriaQuery,
				rootElement);

		return criteriaQuery;

	}

	private void getPredicatesForCriteriaBuilderForNPSData(CriteriaBuilder criteriaBuilder,
			List<Predicate> finalPredicates, Root<NPSAggDetail> rootElement, String startDate, String endDate,
			List<Integer> weekNo, String operator, String technology, String callType, Integer geographyId,
			String geographyType) {

		if (callType.equalsIgnoreCase(NetPromoterConstant.DAILY)) {
			finalPredicates.add(
					criteriaBuilder.between(rootElement.get(NetPromoterConstant.PROCESS_DATE), endDate, startDate));
		} else {

			finalPredicates.add(rootElement.get(NetPromoterConstant.WEEK_NO).in(weekNo));
		}
		finalPredicates.add(criteriaBuilder.equal(rootElement.get(NetPromoterConstant.OPERATOR), operator));
		finalPredicates.add(criteriaBuilder.equal(rootElement.get(NetPromoterConstant.TECHNOLOGY), technology));
		setCriteriaForGeography(criteriaBuilder, finalPredicates, rootElement, geographyId, geographyType);
		finalPredicates
				.add(criteriaBuilder.equal(rootElement.get(NetPromoterConstant.KPI), NetPromoterConstant.KPI_ALL));
	}

	private void getSelectionsForCriteriaBuilderForNPSData(CriteriaBuilder criteriaBuilder,
			List<Selection<?>> listOfSelections, Root<NPSAggDetail> rootElement, String callType) {

		Expression<Double> sumOfRatingSum = criteriaBuilder.sum(rootElement.get(NetPromoterConstant.RATING_SUM));
		Expression<Double> sumOfCustomerCount = criteriaBuilder
				.sum(rootElement.get(NetPromoterConstant.CUSTOMER_COUNT));

		listOfSelections.add(criteriaBuilder.quot(sumOfRatingSum, sumOfCustomerCount));
		listOfSelections.add(criteriaBuilder.sum(rootElement.get(NetPromoterConstant.CUSTOMER_COUNT)));
		listOfSelections.add(rootElement.get(NetPromoterConstant.CUSTOMER_TYPE));
		if (callType.equalsIgnoreCase(NetPromoterConstant.DAILY)) {
			listOfSelections.add(rootElement.get(NetPromoterConstant.PROCESS_DATE));
		} else {

			listOfSelections.add(rootElement.get(NetPromoterConstant.WEEK_NO));
		}

	}

	private void getMultiselectQueryForNPSData(List<Selection<?>> listOfSelections, List<Predicate> finalPredicates,
			CriteriaBuilder criteriaBuilder, String geographyType, CriteriaQuery<NetPromoterWrapper> criteriaQuery,
			Root<NPSAggDetail> rootElement) {

		if (geographyType.equalsIgnoreCase(NetPromoterConstant.GEOGRAPHY_L0FK)
				|| geographyType.equalsIgnoreCase(NetPromoterConstant.GEOGRAPHY_L4FK)) {
			geographyL1AndL4GroupBy(listOfSelections, finalPredicates, criteriaBuilder, rootElement, criteriaQuery,
					null);
		} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L3fk)) {
			geographyL3GroupBy(listOfSelections, finalPredicates, criteriaBuilder, rootElement, criteriaQuery, null);
		} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L2fk)) {
			geographyL2GroupBy(listOfSelections, finalPredicates, criteriaBuilder, rootElement, criteriaQuery, null);
		} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L1fk)) {
			geographyL1GroupBy(listOfSelections, finalPredicates, criteriaBuilder, rootElement, criteriaQuery, null);
		}

	}

	@Override
	public List<NetPromoterWrapper> getKpiWiseData(Integer geographyId, String geographyType, String startDate,
			String endDate, String operator, String technology, String kpi, List<Integer> weekNo) {
		List<NetPromoterWrapper> listOfData = new ArrayList<>();
		logger.info("IN DAO NPS");
		try {
			CriteriaQuery<NetPromoterWrapper> criteriaQuery = getCriteriaBuilderQueryForKpi(geographyId, geographyType,
					startDate, endDate, weekNo, operator, technology, kpi);
			Query query = getEntityManager().createQuery(criteriaQuery);
			listOfData = query.getResultList();

			logger.info("listSize {}", listOfData);
			return listOfData;
			
		} catch (Exception e) {
			logger.error("getting error in kpi wise data {}", ExceptionUtils.getStackTrace(e));
		}
		return listOfData;
	}

	private CriteriaQuery<NetPromoterWrapper> getCriteriaBuilderQueryForKpi(Integer geographyId, String geographyType,
			String startDate, String endDate, List<Integer> weekNo, String operator, String technology, String kpi) {

		List<Predicate> predicates = new ArrayList<>();
		List<Selection<?>> selections = new ArrayList<>();
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<NetPromoterWrapper> criteriaQuery = cb.createQuery(NetPromoterWrapper.class);
		Root<NPSAggDetail> rootElement = criteriaQuery.from(NPSAggDetail.class);
		getPredicateForKpi(cb, predicates, rootElement, startDate,  weekNo, operator, technology, kpi,
				geographyId, geographyType);
		getKPISelectionsForCriteriaBuilder(cb, selections, rootElement, startDate, endDate);
		getMultiSelectionKPIQuery(selections, predicates, cb, geographyType, rootElement, criteriaQuery, weekNo);

		return criteriaQuery;
	}

	private void getPredicateForKpi(CriteriaBuilder cb, List<Predicate> predicates, Root<NPSAggDetail> rootElement,
			String startDate, List<Integer> weekno, String operator, String technology, String kpi,
			Integer geographyId, String geographyType) {

		if (weekno == null) {
			predicates.add(rootElement.get(NetPromoterConstant.PROCESS_DATE)
					.in(NetPromoterUtil.getLastSevenDays(startDate, ForesightConstants.SEVEN)));
		} else {
			predicates.add(rootElement.get(NetPromoterConstant.WEEK_NO).in(weekno));
		}

		if (kpi.equals(ForesightConstants.ALL)) {
			predicates.add(cb.isNotNull(rootElement.get(NetPromoterConstant.KPI_SUM)));
		}
		predicates.add(cb.equal(rootElement.get(NetPromoterConstant.KPI), kpi));
		predicates.add(cb.equal(rootElement.get(NetPromoterConstant.OPERATOR), operator));
		predicates.add(cb.equal(rootElement.get(NetPromoterConstant.TECHNOLOGY), technology));
		setCriteriaForGeography(cb, predicates, rootElement, geographyId, geographyType);
	}

	private void setCriteriaForGeography(CriteriaBuilder cb, List<Predicate> predicates, Root<NPSAggDetail> rootElement,
			Integer geographyId, String geographyType) {
		if (geographyType.equalsIgnoreCase(NetPromoterConstant.GEOGRAPHY_L4FK)) {
			predicates.add(cb.equal(rootElement.get(NetPromoterConstant.GEOGRAPHY_L4FK), geographyId));
		} else if (geographyType.equalsIgnoreCase(NetPromoterConstant.GEOGRAPHY_L3FK)) {
			predicates.add(cb.equal(
					rootElement.get(NetPromoterConstant.GEOGRAPHY_L4FK).get(NetPromoterConstant.GEOGRAPHY_L3FK),
					geographyId));
		} else if (geographyType.equalsIgnoreCase(NetPromoterConstant.GEOGRAPHY_L2FK)) {
			predicates.add(cb.equal(rootElement.get(NetPromoterConstant.GEOGRAPHY_L4FK)
					.get(NetPromoterConstant.GEOGRAPHY_L3FK).get(NetPromoterConstant.GEOGRAPHY_L2FK), geographyId));
		} else if (geographyType.equalsIgnoreCase(NetPromoterConstant.GEOGRAPHY_L1FK)) {
			predicates.add(cb.equal(
					rootElement.get(NetPromoterConstant.GEOGRAPHY_L4FK).get(NetPromoterConstant.GEOGRAPHY_L3FK)
							.get(NetPromoterConstant.GEOGRAPHY_L2FK).get(NetPromoterConstant.GEOGRAPHY_L1FK),
					geographyId));
		}
	}

	private void getKPISelectionsForCriteriaBuilder(CriteriaBuilder cb, List<Selection<?>> selections, Root<NPSAggDetail> root, String startDate, String endDate) {

		Expression<Integer> sumOfCustomerCount = cb.sum(root.get(NetPromoterConstant.CUSTOMER_COUNT));
		Expression<Integer> kpiSum = cb.sum(root.get(NetPromoterConstant.KPI_SUM));
		selections.add(root.get(NetPromoterConstant.CUSTOMER_TYPE));
		if (startDate.equalsIgnoreCase(endDate)) {
			selections.add(root.get(NetPromoterConstant.PROCESS_DATE));
		} else {
			selections.add(root.get(NetPromoterConstant.WEEK_NO));
		}

		selections.add(sumOfCustomerCount);
		selections.add(kpiSum);

	}

	private void getMultiSelectionKPIQuery(List<Selection<?>> selections, List<Predicate> predicates,
			CriteriaBuilder cb, String geographyType, Root<NPSAggDetail> rootElement,
			CriteriaQuery<NetPromoterWrapper> criteriaQuery, List<Integer> weekNo) {

		if (geographyType.equalsIgnoreCase(NetPromoterConstant.GEOGRAPHY_L0FK)
				|| geographyType.equalsIgnoreCase(NetPromoterConstant.GEOGRAPHY_L4FK)) {
			geographyL1AndL4GroupBy(selections, predicates, cb, rootElement, criteriaQuery, weekNo);
		} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L3fk)) {
			geographyL3GroupBy(selections, predicates, cb, rootElement, criteriaQuery, weekNo);
		} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L2fk)) {
			geographyL2GroupBy(selections, predicates, cb, rootElement, criteriaQuery, weekNo);
		} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L1fk)) {
			geographyL1GroupBy(selections, predicates, cb, rootElement, criteriaQuery, weekNo);
		}
	}

	private void geographyL1AndL4GroupBy(List<Selection<?>> selections, List<Predicate> predicates, CriteriaBuilder cb,
			Root<NPSAggDetail> rootElement, CriteriaQuery<NetPromoterWrapper> criteriaQuery, List<Integer> weekNo) {

		if (weekNo == null) {
			criteriaQuery.multiselect(selections).where(predicates.toArray(new Predicate[] {}))
					.groupBy(rootElement.get(NetPromoterConstant.CUSTOMER_TYPE),
							rootElement.get(NetPromoterConstant.PROCESS_DATE))
					.orderBy(cb.asc(rootElement.get(NetPromoterConstant.PROCESS_DATE)));
		} else {
			criteriaQuery.multiselect(selections).where(predicates.toArray(new Predicate[] {}))
					.groupBy(rootElement.get(NetPromoterConstant.CUSTOMER_TYPE),
							rootElement.get(NetPromoterConstant.WEEK_NO))
					.orderBy(cb.asc(rootElement.get(NetPromoterConstant.WEEK_NO)));
		}
	}

	private void geographyL3GroupBy(List<Selection<?>> selections, List<Predicate> predicates, CriteriaBuilder cb,
			Root<NPSAggDetail> rootElement, CriteriaQuery<NetPromoterWrapper> criteriaQuery, List<Integer> weekNo) {
		if (weekNo == null) {
			criteriaQuery.multiselect(selections).where(predicates.toArray(new Predicate[] {}))
					.groupBy(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk),
							rootElement.get(NetPromoterConstant.CUSTOMER_TYPE),
							rootElement.get(NetPromoterConstant.PROCESS_DATE))
					.orderBy(cb.asc(rootElement.get(NetPromoterConstant.PROCESS_DATE)));
		} else {
			criteriaQuery.multiselect(selections).where(predicates.toArray(new Predicate[] {}))
					.groupBy(rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk),
							rootElement.get(NetPromoterConstant.CUSTOMER_TYPE),
							rootElement.get(NetPromoterConstant.WEEK_NO))
					.orderBy(cb.asc(rootElement.get(NetPromoterConstant.WEEK_NO)));
		}
	}

	private void geographyL2GroupBy(List<Selection<?>> selections, List<Predicate> predicates, CriteriaBuilder cb,
			Root<NPSAggDetail> rootElement, CriteriaQuery<NetPromoterWrapper> criteriaQuery, List<Integer> weekNo) {
		if (weekNo == null) {
			criteriaQuery.multiselect(selections).where(predicates.toArray(new Predicate[] {})).groupBy(
					rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk)
							.get(ForesightConstants.GEOGRAPHY_L2fk),
					rootElement.get(NetPromoterConstant.CUSTOMER_TYPE),
					rootElement.get(NetPromoterConstant.PROCESS_DATE))
					.orderBy(cb.asc(rootElement.get(NetPromoterConstant.PROCESS_DATE)));
		} else {
			criteriaQuery.multiselect(selections).where(predicates.toArray(new Predicate[] {})).groupBy(
					rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk)
							.get(ForesightConstants.GEOGRAPHY_L2fk),
					rootElement.get(NetPromoterConstant.CUSTOMER_TYPE), rootElement.get(NetPromoterConstant.WEEK_NO))
					.orderBy(cb.asc(rootElement.get(NetPromoterConstant.WEEK_NO)));
		}
	}

	private void geographyL1GroupBy(List<Selection<?>> selections, List<Predicate> predicates, CriteriaBuilder cb,
			Root<NPSAggDetail> rootElement, CriteriaQuery<NetPromoterWrapper> criteriaQuery, List<Integer> weekNo) {
		if (weekNo == null) {
			criteriaQuery.multiselect(selections).where(predicates.toArray(new Predicate[] {}))
					.groupBy(
							rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk)
									.get(ForesightConstants.GEOGRAPHY_L2fk).get(ForesightConstants.GEOGRAPHY_L1fk),
							rootElement.get(NetPromoterConstant.CUSTOMER_TYPE),
							rootElement.get(NetPromoterConstant.PROCESS_DATE))
					.orderBy(cb.asc(rootElement.get(NetPromoterConstant.PROCESS_DATE)));
		} else {
			criteriaQuery.multiselect(selections).where(predicates.toArray(new Predicate[] {})).groupBy(
					rootElement.get(ForesightConstants.GEOGRAPHY_L4fk).get(ForesightConstants.GEOGRAPHY_L3fk)
							.get(ForesightConstants.GEOGRAPHY_L2fk).get(ForesightConstants.GEOGRAPHY_L1fk),
					rootElement.get(NetPromoterConstant.CUSTOMER_TYPE), rootElement.get(NetPromoterConstant.WEEK_NO))
					.orderBy(cb.asc(rootElement.get(NetPromoterConstant.WEEK_NO)));
		}
	}

	@Override
	public List<NPSAggDetail> getNPSAggData(String date, List<Integer> geographyL4List, List<String> operators) {
		logger.info("inside the method getNPSAggData Dao date [{}], operators [{}]", date, operators);

		SimpleDateFormat sampleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<NPSAggDetail> criteriaQuery = criteriaBuilder.createQuery(NPSAggDetail.class);
		List<Predicate> predicateList;
		List<NPSAggDetail> result = new ArrayList<>();
		try {
			predicateList = getPredicateList(sampleDateFormat.parse(date), geographyL4List, operators, criteriaBuilder,
					criteriaQuery.from(NPSAggDetail.class));

			criteriaQuery = addPredicateInCreateriaQuery(criteriaQuery, predicateList);
			result = getEntityManager().createQuery(criteriaQuery).getResultList();
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}
		logger.info("Result size response ::: {}", result.size());
		return result;
	}

	/**
	 * Adds the predicate in createria query.
	 *
	 * @param criteriaQuery the criteria query
	 * @param predicates    the predicates
	 * @return the criteria query
	 */
	private CriteriaQuery<NPSAggDetail> addPredicateInCreateriaQuery(CriteriaQuery<NPSAggDetail> criteriaQuery,
			List<Predicate> predicates) {
		criteriaQuery.where(predicates.toArray(new Predicate[] {}));
		return criteriaQuery;
	}

	/**
	 * Gets the predicate list.
	 *
	 * @param date            the date
	 * @param geographyL1     the geography L 1
	 * @param geographyL2     the geography L 2
	 * @param geographyL3     the geography L 3
	 * @param geographyL4     the geography L 4
	 * @param operators       the operators
	 * @param criteriaBuilder the criteria builder
	 * @param root            the root
	 * @return the predicate list
	 */
	private List<Predicate> getPredicateList(Date date, List<Integer> geographyL4List, List<String> operators,
			CriteriaBuilder criteriaBuilder, Root<NPSAggDetail> root) {

		List<Predicate> predicates = new ArrayList<>();

		if (date != null) {
			predicates.add(criteriaBuilder.equal(root.get("processdate").as(java.sql.Date.class), date));
		}

		if (geographyL4List != null && !geographyL4List.isEmpty()) {
			Expression<Integer> parentExpression = root.get("geographyL4");
			predicates.add(parentExpression.in(geographyL4List));
		}

		if (operators != null && !operators.isEmpty()) {
			Expression<String> parentExpression = root.get("operator");
			predicates.add(parentExpression.in(operators));
		}
		return predicates;
	}
}
