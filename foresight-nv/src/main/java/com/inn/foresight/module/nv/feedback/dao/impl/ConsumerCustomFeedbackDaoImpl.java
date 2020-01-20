package com.inn.foresight.module.nv.feedback.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.Case;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.feedback.constants.ConsumerFeedbackConstant;
import com.inn.foresight.module.nv.feedback.dao.ConsumerCustomFeedbackDao;
import com.inn.foresight.module.nv.feedback.model.ConsumerCustomFeedback;
import com.inn.foresight.module.nv.feedback.model.ConsumerFeedback;
import com.inn.foresight.module.nv.feedback.wrapper.CustomFeedbackRequestWrapper;
import com.inn.foresight.module.nv.feedback.wrapper.CustomFeedbackResponseWrapper;
import com.inn.product.systemconfiguration.utils.SystemConfigurationUtils;

@Repository("ConsumerCustomFeedbackDaoImpl")
public class ConsumerCustomFeedbackDaoImpl extends HibernateGenericDao<Integer, ConsumerCustomFeedback>  implements ConsumerCustomFeedbackDao {

	private Logger logger = LogManager.getLogger(ConsumerCustomFeedbackDaoImpl.class);

	
	/** Instantiates a new consumer feedback dao impl. */
	public ConsumerCustomFeedbackDaoImpl() {
		super(ConsumerCustomFeedback.class);
	}
	
	@Override
	public ConsumerCustomFeedback create(ConsumerCustomFeedback consumerCustomFeedback) {
		logger.info("Going to create feedback dao");
		return super.create(consumerCustomFeedback);
	}

	/** Custom Consumer Feedback */
	@SuppressWarnings("unchecked")
	@Override
	public List<CustomFeedbackResponseWrapper> getCustomFeedbackData(CustomFeedbackRequestWrapper requestWrapper) {
		logger.info("Going to get custom feedback data for : {}", requestWrapper != null ? new Gson().toJson(requestWrapper) : null);
		List<CustomFeedbackResponseWrapper> resultList = new ArrayList<>();
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<CustomFeedbackResponseWrapper> criteriaQuery = criteriaBuilder.createQuery(CustomFeedbackResponseWrapper.class);
			Root<ConsumerCustomFeedback> childFeedback = criteriaQuery.from(ConsumerCustomFeedback.class);
			Join<ConsumerCustomFeedback, ConsumerFeedback> parentFeedback = childFeedback.join(ConsumerFeedbackConstant.CONSUMER_FEEDBACK,
					JoinType.INNER);

			List<Predicate> finalPredicates = null;
			if (requestWrapper != null) {
				selectClauseBasedOnZoomLevel(requestWrapper, criteriaBuilder, criteriaQuery, childFeedback, parentFeedback);

				finalPredicates = new ArrayList<>();
				viewPortPredicates(requestWrapper, criteriaBuilder, parentFeedback, finalPredicates);
				timeDurationPredicates(getDateFromString(requestWrapper.getFromDate()), getDateFromString(requestWrapper.getToDate()), criteriaBuilder,
						parentFeedback, finalPredicates);
				eCareTypePredicates(parentFeedback, finalPredicates);
				feedbackKpiPredicates(requestWrapper, criteriaBuilder, childFeedback, finalPredicates);
				feedbackReasonPredicates(requestWrapper.getKpi(), requestWrapper.getSearchType(), criteriaBuilder, childFeedback, finalPredicates);
				locationTypePredicates(requestWrapper, criteriaBuilder, childFeedback, finalPredicates);
				orderByClause(requestWrapper.getOrderBy(), requestWrapper.getOrderType(), criteriaBuilder, criteriaQuery, parentFeedback);

				if (checkZoomLevelRangeForData(requestWrapper.getZoomLevel(), ConsumerFeedbackConstant.ZOOM_LEVEL_FOR_ALL_DATA_GRIDWISE)) {
					groupByClause(requestWrapper.getZoomLevel(), criteriaQuery, childFeedback, requestWrapper.getKpi());
				}
				criteriaQuery.where(finalPredicates.toArray(new Predicate[] {}));
			}

			Query query = getEntityManager().createQuery(criteriaQuery);
			resultList = query.getResultList();

		} catch (Exception e) {
			logger.error("Error in getting custom feedback details by filter Exception : {}", Utils.getStackTrace(e));
			return new ArrayList<>();
		}
		return resultList;
	}

	private void selectClauseBasedOnZoomLevel(CustomFeedbackRequestWrapper requestWrapper, CriteriaBuilder criteriaBuilder,
			CriteriaQuery<CustomFeedbackResponseWrapper> criteriaQuery, Root<ConsumerCustomFeedback> childFeedback,
			Join<ConsumerCustomFeedback, ConsumerFeedback> parentFeedback) {
		if (checkZoomLevelRangeForData(requestWrapper.getZoomLevel(), ConsumerFeedbackConstant.ZOOM_LEVEL_FOR_ALL_DATA_GRIDWISE)) {
			logger.info("Select for less than or equals 14 {}", requestWrapper.getZoomLevel());
			selectClauseforGridWiseData(requestWrapper, criteriaBuilder, criteriaQuery, childFeedback);
		} else {
			logger.info("Select for greater than 14 {}", requestWrapper.getZoomLevel());
			selectClauseforAllData(requestWrapper.getKpi(), requestWrapper.getLocationType(), criteriaBuilder, criteriaQuery, childFeedback,
					parentFeedback);
		}
	}

	private void selectClauseforGridWiseData(CustomFeedbackRequestWrapper requestWrapper, CriteriaBuilder criteriaBuilder,
			CriteriaQuery<CustomFeedbackResponseWrapper> criteriaQuery, Root<ConsumerCustomFeedback> childFeedback) {
		criteriaQuery.select(criteriaBuilder.construct(CustomFeedbackResponseWrapper.class,
				criteriaBuilder.count(childFeedback.get(ForesightConstants.ID)), childFeedback.get(getGridKeyByZoom(requestWrapper.getZoomLevel())),
				childFeedback.get(getRatingKeyByKpi(requestWrapper.getKpi()))));
	}

	private void selectClauseforAllData(String kpi, String locationType, CriteriaBuilder criteriaBuilder,
			CriteriaQuery<CustomFeedbackResponseWrapper> criteriaQuery, Root<ConsumerCustomFeedback> childFeedback,
			Join<ConsumerCustomFeedback, ConsumerFeedback> parentFeedback) {
		criteriaQuery.select(criteriaBuilder.construct(CustomFeedbackResponseWrapper.class,
				selectReasonFilterByKpi(kpi, criteriaBuilder, childFeedback), locationTypeCase(locationType, criteriaBuilder, childFeedback),
				parentFeedback.get(ConsumerFeedbackConstant.LATITUDE), parentFeedback.get(ConsumerFeedbackConstant.LONGITUDE), childFeedback.get(ConsumerFeedbackConstant.FEEDBACK_ADDRESS), parentFeedback.get(ConsumerFeedbackConstant.DEVICEID),
				parentFeedback.get(ConsumerFeedbackConstant.NV_MODULE), parentFeedback.get(ConsumerFeedbackConstant.VERSIONNAME), childFeedback.get(getRatingKeyByKpi(kpi)),
				childFeedback.get(ConsumerFeedbackConstant.FEEDBACKDATE), parentFeedback.get(ConsumerFeedbackConstant.MAKE), parentFeedback.get(ConsumerFeedbackConstant.MODEL)));
	}

	private void viewPortPredicates(CustomFeedbackRequestWrapper requestWrapper, CriteriaBuilder criteriaBuilder,
			Join<ConsumerCustomFeedback, ConsumerFeedback> parentFeedback, List<Predicate> finalPredicates) {
		if (requestWrapper.getsWLat() != null && requestWrapper.getnELat() != null && requestWrapper.getsWLng() != null
				&& requestWrapper.getnELng() != null) {
			finalPredicates
					.add(criteriaBuilder.greaterThanOrEqualTo(parentFeedback.get(ConsumerFeedbackConstant.LATITUDE), requestWrapper.getsWLat()));
			finalPredicates.add(criteriaBuilder.lessThanOrEqualTo(parentFeedback.get(ConsumerFeedbackConstant.LATITUDE), requestWrapper.getnELat()));
			finalPredicates
					.add(criteriaBuilder.greaterThanOrEqualTo(parentFeedback.get(ConsumerFeedbackConstant.LONGITUDE), requestWrapper.getsWLng()));
			finalPredicates.add(criteriaBuilder.lessThanOrEqualTo(parentFeedback.get(ConsumerFeedbackConstant.LONGITUDE), requestWrapper.getnELng()));
		}
	}

	private void timeDurationPredicates(Date fromDate, Date toDate, CriteriaBuilder criteriaBuilder,
			Join<ConsumerCustomFeedback, ConsumerFeedback> parentFeedback, List<Predicate> finalPredicates) {
		finalPredicates.add(criteriaBuilder.between(parentFeedback.get(ConsumerFeedbackConstant.ECARE_FEEDBACKTIME), fromDate, toDate));
	}

	private void eCareTypePredicates(Join<ConsumerCustomFeedback, ConsumerFeedback> parentFeedback, List<Predicate> finalPredicates) {
		finalPredicates.add(parentFeedback.get(ConsumerFeedbackConstant.NV_MODULE)
				.in(ConfigUtils.getStringList(ConsumerFeedbackConstant.ECARE_LAYER_NVMODULE_LIST)));
		finalPredicates.add(parentFeedback.get(ConsumerFeedbackConstant.OPERATOR_NAME).in(getDefaultOperatorForECare()));
	}

	private void feedbackKpiPredicates(CustomFeedbackRequestWrapper requestWrapper, CriteriaBuilder criteriaBuilder,
			Root<ConsumerCustomFeedback> childFeedback, List<Predicate> predicates) {
		if (isValidKpi(requestWrapper.getKpi())) {
			predicates.add(criteriaBuilder.equal(childFeedback.get(getFeedbackTypeByKpi(requestWrapper.getKpi())), ForesightConstants.TRUE));
		}
	}

	private void feedbackReasonPredicates(String kpi, String searchType, CriteriaBuilder criteriaBuilder, Root<ConsumerCustomFeedback> childFeedback,
			List<Predicate> predicates) {
		if (isValidKpi(kpi) && isValidDataForECareFilter(searchType)) {
			List<Predicate> searchTypePredicates = new ArrayList<>();
			List<Predicate> noneSearchTypePredicates = new ArrayList<>();
			List<String> searchTypeList = Arrays.asList(searchType.split(ForesightConstants.COMMA));
			setReasonPredicates(kpi, criteriaBuilder, childFeedback, searchTypePredicates, noneSearchTypePredicates, searchTypeList);
			populateFinalReasonPredicate(criteriaBuilder, predicates, searchTypePredicates, noneSearchTypePredicates);
			
		}
	}

	private void populateFinalReasonPredicate(CriteriaBuilder criteriaBuilder, List<Predicate> predicates, List<Predicate> searchTypePredicates,
			List<Predicate> noneSearchTypePredicates) {
		if (Utils.isValidList(noneSearchTypePredicates) && Utils.isValidList(searchTypePredicates)) {
			predicates.add(criteriaBuilder.or(criteriaBuilder.or(searchTypePredicates.toArray(new Predicate[] {})),
					criteriaBuilder.and(noneSearchTypePredicates.toArray(new Predicate[] {}))));
		} else if (Utils.isValidList(noneSearchTypePredicates)) {
			predicates.add(criteriaBuilder.and(noneSearchTypePredicates.toArray(new Predicate[] {})));

		} else if (Utils.isValidList(searchTypePredicates)) {
			predicates.add(criteriaBuilder.or(searchTypePredicates.toArray(new Predicate[] {})));
		}
	}

	private void setReasonPredicates(String kpi, CriteriaBuilder criteriaBuilder, Root<ConsumerCustomFeedback> childFeedback,
			List<Predicate> searchTypePredicates, List<Predicate> noneSearchTypePredicates, List<String> searchTypeList) {
		searchTypeList.forEach(reason -> {
			if (reason.equalsIgnoreCase(ForesightConstants.NONE)) {
				setNoFeedbackReasonPredicates(kpi, criteriaBuilder, childFeedback, noneSearchTypePredicates);
			} else {
				searchTypePredicates.add(criteriaBuilder.equal(childFeedback.get(reason), ForesightConstants.TRUE));
			}
		});
	}

	private void setNoFeedbackReasonPredicates(String kpi, CriteriaBuilder criteriaBuilder, Root<ConsumerCustomFeedback> childFeedback,
			List<Predicate> noneSearchTypePredicates) {
		if (kpi.equalsIgnoreCase(ConsumerFeedbackConstant.ECARE_DATA)) {
			Map<String, Object> dataReasonColumns = convertStringToMap(ConfigUtils.getString(ConsumerFeedbackConstant.ECARE_DATA_FEEDBACK_DATA));
			dataReasonColumns.forEach((key, value) -> {
				noneSearchTypePredicates.add(criteriaBuilder.or(criteriaBuilder.equal(childFeedback.get(key), ForesightConstants.FALSE),
						criteriaBuilder.isNull(childFeedback.get(key))));
			});
		}
		if (kpi.equalsIgnoreCase(ConsumerFeedbackConstant.ECARE_VOICE)) {
			Map<String, Object> dataReasonColumns = convertStringToMap(ConfigUtils.getString(ConsumerFeedbackConstant.ECARE_VOICE_FEEDBACK_DATA));
			dataReasonColumns.forEach((key, value) -> {
				noneSearchTypePredicates.add(criteriaBuilder.or(criteriaBuilder.equal(childFeedback.get(key), ForesightConstants.FALSE),
						criteriaBuilder.isNull(childFeedback.get(key))));
			});
		}
	}
	
	private void locationTypePredicates(CustomFeedbackRequestWrapper requestWrapper, CriteriaBuilder criteriaBuilder,
			Root<ConsumerCustomFeedback> childFeedback, List<Predicate> predicates) {
		if (isValidDataForECareFilter(requestWrapper.getLocationType())) {
			List<Predicate> locationTypePredicates = new ArrayList<>();
			List<String> locationTypeList = Arrays.asList(requestWrapper.getLocationType().split(ForesightConstants.COMMA));
			locationTypeList.forEach(
					locationType -> locationTypePredicates.add(criteriaBuilder.equal(childFeedback.get(locationType), ForesightConstants.TRUE)));
			predicates.add(criteriaBuilder.or(locationTypePredicates.toArray(new Predicate[] {})));
		}
	}

	private void orderByClause(String orderBy, String orderType, CriteriaBuilder criteriaBuilder,
			CriteriaQuery<CustomFeedbackResponseWrapper> criteriaQuery, Join<ConsumerCustomFeedback, ConsumerFeedback> parentFeedback) {
		if (Utils.hasValue(orderBy) && Utils.hasValue(orderType)) {
			if (orderType.equalsIgnoreCase(ConsumerFeedbackConstant.ECARE_DESC)) {
				criteriaQuery.orderBy(criteriaBuilder.desc(parentFeedback.get(orderBy)));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.asc(parentFeedback.get(orderBy)));
			}
		} else {
			criteriaQuery.orderBy(criteriaBuilder.desc(parentFeedback.get(ConsumerFeedbackConstant.ECARE_FEEDBACKTIME)));
		}
	}

	private void groupByClause(Integer zoomLevel, CriteriaQuery<CustomFeedbackResponseWrapper> criteriaQuery,
			Root<ConsumerCustomFeedback> childFeedback, String kpi) {
		List<Expression<?>> grouping = new ArrayList<>();
		if (checkZoomLevelRangeForData(zoomLevel, ConsumerFeedbackConstant.ZOOM_LEVELS_FOR_MEDIANWISE_DATA)) {
			grouping.add(childFeedback.get(getRatingKeyByKpi(kpi)));
		}
		grouping.add(childFeedback.get(getGridKeyByZoom(zoomLevel)));
		criteriaQuery.groupBy(grouping.toArray(new Expression[] {}));
	}

	@SuppressWarnings("serial")
	private Map<String, Object> convertStringToMap(String string) {
		if (Utils.hasValue(string)) {
			return new Gson().fromJson(string, new TypeToken<Map<String, Object>>() {
			}.getType());
		} else {
			return new HashMap<>();
		}
	}

	private Date getDateFromString(String strDate) {
		return com.inn.core.generic.utils.Utils.parseStringToDate(strDate, ForesightConstants.DATE_FORMAT_DDMMYY);
	}

	private Case<Object> selectReasonFilterByKpi(String kpi, CriteriaBuilder criteriaBuilder, Root<ConsumerCustomFeedback> childFeedback) {
		List<Expression<Object>> expressionList = new ArrayList<>();
		if (kpi.equalsIgnoreCase(ConsumerFeedbackConstant.ECARE_DATA)) {
			Map<String, Object> dataReasonColumns = convertStringToMap(ConfigUtils.getString(ConsumerFeedbackConstant.ECARE_DATA_FEEDBACK_DATA));
			dataReasonColumns.forEach((key, value) -> {
				expressionList.add(criteriaBuilder.selectCase().when(criteriaBuilder.equal(childFeedback.get(key), true), value).otherwise(""));
			});
		}
		if (kpi.equalsIgnoreCase(ConsumerFeedbackConstant.ECARE_VOICE)) {
			Map<String, Object> dataReasonColumns = convertStringToMap(ConfigUtils.getString(ConsumerFeedbackConstant.ECARE_VOICE_FEEDBACK_DATA));
			dataReasonColumns.forEach((key, value) -> {
				expressionList.add(criteriaBuilder.selectCase().when(criteriaBuilder.equal(childFeedback.get(key), true), value).otherwise(""));
			});
		}
		return criteriaBuilder.selectCase().when(criteriaBuilder.equal(childFeedback.get(getFeedbackTypeByKpi(kpi)), true),
				criteriaBuilder.function(ConsumerFeedbackConstant.CONCAT, String.class, expressionList.toArray(new Expression[] {})));
	}

	private Expression<Object> locationTypeCase(String locationType, CriteriaBuilder criteriaBuilder, Root<ConsumerCustomFeedback> childFeedback) {
		Map<String, Object> locationTypeData = convertStringToMap(ConfigUtils.getString(ConsumerFeedbackConstant.ECARE_LOCATION_TYPE_DATA));
		return criteriaBuilder.selectCase()
				.when(criteriaBuilder.equal(childFeedback.get(ConsumerFeedbackConstant.LOCATION_TYPE_INDOOR), true), locationTypeData.get(ConsumerFeedbackConstant.LOCATION_TYPE_INDOOR))
				.when(criteriaBuilder.equal(childFeedback.get(ConsumerFeedbackConstant.LOCATION_TYPE_OUTDOOR), true), locationTypeData.get(ConsumerFeedbackConstant.LOCATION_TYPE_OUTDOOR))
				.when(criteriaBuilder.equal(childFeedback.get(ConsumerFeedbackConstant.LOCATION_TYPE_ON_THE_MOVE), true), locationTypeData.get(ConsumerFeedbackConstant.LOCATION_TYPE_ON_THE_MOVE));
	}

	private String getFeedbackTypeByKpi(String kpi) {
		if (kpi.equalsIgnoreCase(ConsumerFeedbackConstant.ECARE_DATA)) {
			return ConsumerFeedbackConstant.ECARE_IS_DATA_FEEDBACK_KEY;
		} else if (kpi.equalsIgnoreCase(ConsumerFeedbackConstant.ECARE_VOICE)) {
			return ConsumerFeedbackConstant.ECARE_IS_VOICE_FEEDBACK_KEY;
		} else {
			return null;
		}
	}

	private String getRatingKeyByKpi(String kpi) {
		if (kpi.equalsIgnoreCase(ConsumerFeedbackConstant.ECARE_DATA)) {
			return ConsumerFeedbackConstant.INTERNET_QUALITY_RATE;
		} else if (kpi.equalsIgnoreCase(ConsumerFeedbackConstant.ECARE_VOICE)) {
			return ConsumerFeedbackConstant.CALLS_QUALITY_RATE;
		} else {
			return null;
		}
	}

	private List<String> getDefaultOperatorForECare() {
		if (ConfigUtils.getBoolean(ConsumerFeedbackConstant.ECARE_LAYER_OPERATOR_LIST_ENABLED)) {
			List<String> list = ConfigUtils.getStringList(ConsumerFeedbackConstant.ECARE_LAYER_OPERATOR_LIST);
			if (Utils.isValidList(list)) {
				while (list.remove(null) || list.remove(ForesightConstants.BLANK_STRING))
					;
			}
			return list;
		} else {
			return Arrays.asList(getDefaultOperator());
		}
	}

	private boolean isValidKpi(String kpi) {
		return (Utils.hasValidValue(kpi)
				&& (kpi.equalsIgnoreCase(ConsumerFeedbackConstant.ECARE_DATA) || kpi.equalsIgnoreCase(ConsumerFeedbackConstant.ECARE_VOICE)));
	}

	private String getDefaultOperator() {
		return SystemConfigurationUtils.systemConfMap.get("DEFAULT_OPERATOR");
	}

	private boolean isValidDataForECareFilter(String filterName) {
		return Utils.hasValidValue(filterName) && !filterName.equalsIgnoreCase(ConsumerFeedbackConstant.ECARE_ALL);
	}

	private String getGridKeyByZoom(Integer zoomLevel) {
		StringBuilder str = new StringBuilder();
		str.append(ConsumerFeedbackConstant.ZOOM);
		str.append(zoomLevel);
		return str.toString();
	}

	private boolean checkZoomLevelRangeForData(Integer zoomLevel, String zoomLevelReason) {
		return zoomLevel >= ConfigUtils.getIntegerList(zoomLevelReason).get(0) && zoomLevel <= ConfigUtils.getIntegerList(zoomLevelReason).get(1);
	}

}
