package com.inn.foresight.module.nv.sitesuggestion.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.feedback.constants.ConsumerFeedbackConstant;
import com.inn.foresight.module.nv.sitesuggestion.dao.ISiteSuggestionDao;
import com.inn.foresight.module.nv.sitesuggestion.model.FriendlySiteSuggestion;

@Repository("SiteSuggestionDaoImpl")
public class SiteSuggestionDaoImpl extends HibernateGenericDao<Integer, FriendlySiteSuggestion>
implements ISiteSuggestionDao {

	private Logger logger = LogManager.getLogger(SiteSuggestionDaoImpl.class);

	public SiteSuggestionDaoImpl() {
		super(FriendlySiteSuggestion.class);
	}

	@Override
	public List<FriendlySiteSuggestion> getSiteAcquisitionLayerData(String fromDate, String toDate, String buildingType,
			String siteType) {
		logger.info("Going to Site Acquisition Layer Data::: fromDate {},toDate {},buildingType {},siteType {}",
				fromDate, toDate, buildingType, siteType);

		try {
			Query query = getEntityManager().createNamedQuery("getSiteAcquisitionLayerData");
			SimpleDateFormat formatter = new SimpleDateFormat(ForesightConstants.DATE_FORMAT_DDMMYY);
			Date minDate = formatter.parse(fromDate);
			Date maxDate = new Date(formatter.parse(toDate).getTime() + 86399999);
			query.setParameter(ConsumerFeedbackConstant.FROM_DATE, minDate);
			query.setParameter(ConsumerFeedbackConstant.TO_DATE, maxDate);

			applyBuildingTypeFilter(buildingType);
			applySiteTypeFilter(siteType);
			List<FriendlySiteSuggestion> response = query.getResultList();
			disableFilter(ConsumerFeedbackConstant.BUILDING_TYPE_FILTER);
			disableFilter(ConsumerFeedbackConstant.SITE_TYPE_FILTER);

			logger.info("Got Response from the db :: {}", response.size());
			return response;
		} catch (Exception e) {
			logger.error("Exception in getTopLocationsWithType, error = {}", ExceptionUtils.getStackTrace(e));
			return new ArrayList<>();
		}
	}

	private void applyBuildingTypeFilter(String buildingType) {
		if (Utils.hasValidValue(buildingType) && !buildingType.equalsIgnoreCase(ForesightConstants.ALL)) {
			logger.info("Going to apply buildingType filter");
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(ConsumerFeedbackConstant.BUILDING_TYPE_FILTER);
			filter.setParameter("buildingtype", ForesightConstants.MODULUS + buildingType + ForesightConstants.MODULUS);
		}
	}

	private void applySiteTypeFilter(String siteType) {
		if (Utils.hasValidValue(siteType) && !siteType.equalsIgnoreCase(ForesightConstants.ALL)) {
			logger.info("Going to apply siteType filter");
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(ConsumerFeedbackConstant.SITE_TYPE_FILTER);
			filter.setParameter("sitetype", ForesightConstants.MODULUS + siteType + ForesightConstants.MODULUS);
		}
	}

	private void disableFilter(String filterName) {
		if (Utils.hasValidValue(filterName)) {
			Session s = (Session) getEntityManager().getDelegate();
			s.disableFilter(filterName);
		}
	}

}
