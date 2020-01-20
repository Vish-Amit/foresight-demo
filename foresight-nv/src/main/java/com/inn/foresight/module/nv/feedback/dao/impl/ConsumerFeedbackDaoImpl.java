package com.inn.foresight.module.nv.feedback.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.feedback.constants.ConsumerFeedbackConstant;
import com.inn.foresight.module.nv.feedback.dao.IConsumerFeedbackDao;
import com.inn.foresight.module.nv.feedback.model.ConsumerCustomFeedback;
import com.inn.foresight.module.nv.feedback.model.ConsumerFeedback;
import com.inn.foresight.module.nv.feedback.wrapper.ConsumerFeedbackWrapper;
import com.inn.foresight.module.nv.feedback.wrapper.CustomFeedbackRequestWrapper;
import com.inn.foresight.module.nv.feedback.wrapper.CustomFeedbackResponseWrapper;
import com.inn.product.systemconfiguration.utils.SystemConfigurationUtils;

/**
 * The Class ConsumerFeedbackDaoImpl.
 * 	
 * @author innoeye
 */
@Repository("ConsumerFeedbackDaoImpl")
public class ConsumerFeedbackDaoImpl extends HibernateGenericDao<Integer, ConsumerFeedback>  implements IConsumerFeedbackDao{

	/** The logger. */
	private Logger logger = LogManager.getLogger(ConsumerFeedbackDaoImpl.class);
	
	/**
	 * Creates the consumerFeedback.
	 *
	 * @param consumerFeedback the consumerFeedback
	 * @return the consumerFeedback
	 * @throws DaoException the dao exception
	 */
	@Override
	public ConsumerFeedback create(ConsumerFeedback consumerFeedback) {
		logger.info("Going to create feedback dao");
		return super.create(consumerFeedback);
	}
	
	/** Instantiates a new consumer feedback dao impl. */
	public ConsumerFeedbackDaoImpl() {
		super(ConsumerFeedback.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ConsumerFeedbackWrapper> getFeedBackAvgRating(Date date, String  level, Integer geographyId){
		try {
			logger.info("Going to get feedback avg rating with count");
			Query query = getEntityManager().createNamedQuery("getFeedBackAvgRating");
			query.setParameter(ConsumerFeedbackConstant.DATE, date);
			query.setParameter(ConsumerFeedbackConstant.OPERATOR_NAME, getDefaultOperator());
			String geographyFilterName = getGeographyFilterName(level);
			applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
			List<ConsumerFeedbackWrapper> consumerFeedBackList = query.getResultList();
			disableFilter(geographyFilterName);
			return consumerFeedBackList;
		} catch(Exception e) {
			logger.error("Exception in FeedBackAvgRating, error = [{}]", ExceptionUtils.getStackTrace(e));
			return new ArrayList<>();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsumerFeedbackWrapper> getFeedBackRatingWithCount(Date date, String  level, Integer geographyId){
		try {
			logger.info("Going to get feedbck rating with count" + date);
			Query query = getEntityManager().createNamedQuery("getFeedBackRatingWithCount");
			query.setParameter(ConsumerFeedbackConstant.DATE, date);
			query.setParameter(ConsumerFeedbackConstant.OPERATOR_NAME, getDefaultOperator());
			String geographyFilterName = getGeographyFilterName(level);
			applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
			List<ConsumerFeedbackWrapper> consumerFeedBackList = query.getResultList();
			disableFilter(geographyFilterName);
			return consumerFeedBackList;
		} catch(Exception e) {
			logger.error("Exception in getFeedBackRatingWithCount, error = {}", ExceptionUtils.getStackTrace(e));
			return new ArrayList<>();
		}	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsumerFeedbackWrapper> getDateWiseFeedBackCount(Date fromDate, Date toDate, String  level, Integer geographyId){
		try {
			logger.info("Going to get date wise feedback count");
			Query query = getEntityManager().createNamedQuery("getDateWiseFeedBackCount");
			query.setParameter(ConsumerFeedbackConstant.FROM_DATE, fromDate);
			query.setParameter(ConsumerFeedbackConstant.TO_DATE, toDate);
			query.setParameter(ConsumerFeedbackConstant.OPERATOR_NAME, getDefaultOperator());
			String geographyFilterName = getGeographyFilterName(level);
			applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
			
			List<ConsumerFeedbackWrapper> consumerFeedBackList = query.getResultList();
			disableFilter(geographyFilterName);
			return consumerFeedBackList;
		} catch(Exception e) {
			logger.error("Exception in getDateWiseFeedBackCount, error = {}", ExceptionUtils.getStackTrace(e));
			return new ArrayList<>();
		}	
	}
	
	@SuppressWarnings("unchecked")
	public List<ConsumerFeedbackWrapper> getDateWiseTestArea(String type, Date fromDate, Date toDate, String  level, Integer geographyId) {
		try {
			logger.info("Going to get dateWise testArea");
			Query query = getEntityManager().createNamedQuery("getDateWiseTestArea");
			query.setParameter(ConsumerFeedbackConstant.TYPE, type);
			query.setParameter(ConsumerFeedbackConstant.FROM_DATE, fromDate);
			query.setParameter(ConsumerFeedbackConstant.TO_DATE, toDate);
			query.setParameter(ConsumerFeedbackConstant.OPERATOR_NAME, getDefaultOperator());
			String geographyFilterName = getGeographyFilterName(level);
			applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
			List<ConsumerFeedbackWrapper> consumerFeedBackList = query.getResultList();
			disableFilter(geographyFilterName);
			return consumerFeedBackList;
		} catch(Exception e) {
			logger.error("Exception in getDateWiseTestArea, error = {}", ExceptionUtils.getStackTrace(e));
			return new ArrayList<>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ConsumerFeedbackWrapper> getDateWiseFeedback(String type, Date fromDate, Date toDate, String  level, Integer geographyId){
		try {
			logger.info("Going to get dateWise feedback");
			Query query = getEntityManager().createNamedQuery("getDateWiseFeedBack");
			query.setParameter(ConsumerFeedbackConstant.TYPE, type);
			query.setParameter(ConsumerFeedbackConstant.FROM_DATE, fromDate);
			query.setParameter(ConsumerFeedbackConstant.TO_DATE, toDate);
			query.setParameter(ConsumerFeedbackConstant.OPERATOR_NAME, getDefaultOperator());
			String geographyFilterName = getGeographyFilterName(level);
			applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
			List<ConsumerFeedbackWrapper> consumerFeedBackList = query.getResultList();
			disableFilter(geographyFilterName);
			return consumerFeedBackList;
		} catch(Exception e) {
			logger.error("Exception in getDateWiseFeedBack, error = {}", ExceptionUtils.getStackTrace(e));
			return new ArrayList<>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ConsumerFeedbackWrapper> getDateWiseVoiceCount(Date fromDate, Date toDate, String  level, Integer geographyId){
		try {
			logger.info("Going to get dateWise voiceCount");
			Query query = getEntityManager().createNamedQuery("getDateWiseVoiceCount");
			query.setParameter(ConsumerFeedbackConstant.FROM_DATE, fromDate);
			query.setParameter(ConsumerFeedbackConstant.TO_DATE, toDate);
			query.setParameter(ConsumerFeedbackConstant.OPERATOR_NAME, getDefaultOperator());
			String geographyFilterName = getGeographyFilterName(level);
			applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
			List<ConsumerFeedbackWrapper> consumerFeedBackList = query.getResultList();
			disableFilter(geographyFilterName);
			return consumerFeedBackList;
		} catch(Exception e) {
			logger.error("Exception in getDateWiseVoiceCount, error = {}", ExceptionUtils.getStackTrace(e));
			return new ArrayList<>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ConsumerFeedbackWrapper> getDateWiseDataCount(Date fromDate, Date toDate, String  level, Integer geographyId){
		try {
			logger.info("Going to get DateWise data count");
			Query query = getEntityManager().createNamedQuery("getDateWiseDataCount");
			query.setParameter(ConsumerFeedbackConstant.FROM_DATE, fromDate);
			query.setParameter(ConsumerFeedbackConstant.TO_DATE, toDate);
			query.setParameter(ConsumerFeedbackConstant.OPERATOR_NAME, getDefaultOperator());
			String geographyFilterName = getGeographyFilterName(level);
			applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
			
			List<ConsumerFeedbackWrapper> consumerFeedBackList = query.getResultList();
			disableFilter(geographyFilterName);
			return consumerFeedBackList;
		} catch(Exception e) {
			logger.error("Exception in getDateWiseDataCount, error = {}", ExceptionUtils.getStackTrace(e));
			return new ArrayList<>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ConsumerFeedbackWrapper> getDateWiseCoverageCount(Date fromDate, Date toDate, String  level, Integer geographyId){
		try {
			logger.info("Going to get DateWise CoverageCount");
			Query query = getEntityManager().createNamedQuery("getDateWiseCoverageCount");
			query.setParameter(ConsumerFeedbackConstant.FROM_DATE, fromDate);
			query.setParameter(ConsumerFeedbackConstant.TO_DATE, toDate);
			query.setParameter(ConsumerFeedbackConstant.OPERATOR_NAME, getDefaultOperator());
			String geographyFilterName = getGeographyFilterName(level);
			applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
			List<ConsumerFeedbackWrapper> consumerFeedBackList = query.getResultList();
			disableFilter(geographyFilterName);
			return consumerFeedBackList;
		} catch(Exception e) {
			logger.error("Exception in getDateWiseCoverageCount, error = {}", ExceptionUtils.getStackTrace(e));
			return new ArrayList<>();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ConsumerFeedbackWrapper> getTopFeedbackLocations(Date fromDate, Date toDate, Integer lLimit, Integer uLimit, String  level, Integer geographyId){
		try {
			logger.info("Going to get top feedback locations");
			Query query = getEntityManager().createNamedQuery("getTopFeedbackLocations");
			query.setParameter(ConsumerFeedbackConstant.FROM_DATE, fromDate);
			query.setParameter(ConsumerFeedbackConstant.TO_DATE, toDate);
			query.setParameter(ConsumerFeedbackConstant.OPERATOR_NAME, getDefaultOperator());
			
			if (lLimit != null && uLimit != null) {
				query.setFirstResult(lLimit);
				query.setMaxResults(uLimit);
			}
			String geographyFilterName = getGeographyFilterName(level);
			applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
			List<ConsumerFeedbackWrapper> consumerFeedBackList = query.getResultList();
			disableFilter(geographyFilterName);
			return consumerFeedBackList;
		} catch(Exception e) {
			logger.error("Exception in getTopFeedbackLocations, error = {}", ExceptionUtils.getStackTrace(e));
			return new ArrayList<>();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ConsumerFeedbackWrapper> getTopLocationsWithType(Date fromDate, Date toDate, Integer lLimit, Integer uLimit, String  level, Integer geographyId){
		try {
			logger.info("Going to get Top getTopLocationsWithType");
			Query query = getEntityManager().createNamedQuery("getTopLocationsWithType");
			query.setParameter(ConsumerFeedbackConstant.FROM_DATE, fromDate);
			query.setParameter(ConsumerFeedbackConstant.TO_DATE, toDate);
			query.setParameter(ConsumerFeedbackConstant.OPERATOR_NAME, getDefaultOperator());
			if (lLimit != null && uLimit != null) {
				query.setFirstResult(lLimit);
				query.setMaxResults(uLimit);
			}
			String geographyFilterName = getGeographyFilterName(level);
			applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
			List<ConsumerFeedbackWrapper> consumerFeedBackList = query.getResultList();
			disableFilter(geographyFilterName);
			return consumerFeedBackList;
		} catch(Exception e) {
			logger.error("Exception in getTopLocationsWithType, error = {}", ExceptionUtils.getStackTrace(e));
			return new ArrayList<>();
		}
	}

	private String getDefaultOperator() {
		return SystemConfigurationUtils.systemConfMap.get("DEFAULT_OPERATOR");
	}
	
	private String getGeographyFilterName(String geographyLevel) {
		if (geographyLevel != null) {
			if (geographyLevel.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L1)) {
				return "GWGeographyL1Filter";
			} else if (geographyLevel.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L2)) {
				return "GWGeographyL2Filter";
			} else if (geographyLevel.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L3)) {
				return "GWGeographyL3Filter";
			} else if (geographyLevel.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L4)) {
				return "GWGeographyL4Filter";
			} 
		}
		return null;
	}
	
	private void applyGeographyFilter(String filterName, List<Integer> geographyId) {
		if (filterName != null && geographyId != null) {
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(filterName);
			filter.setParameterList("geographyId", geographyId);
		}
	}
	
	private void disableFilter(String filterName) {
		if (Utils.hasValidValue(filterName)) {
			Session s = (Session) getEntityManager().getDelegate();
			s.disableFilter(filterName);
		}
	}
	
	@Override
	public List<ConsumerFeedback> getFeedBackLayerData(String fromDate, String toDate,String testArea,String feedbackType, String band, String kpi
			, Integer fromStarRating,Integer toStarRating, String searchType) {

		logger.info("Going to get FeedBack Layer Data for opeator {} fromDate {},toDate {},testArea {},feedbackType {},"
				+ "band {},kpi  {},fromStarRating {}, toStarRating {},searchType {}",getDefaultOperator(),fromDate,toDate,testArea,feedbackType,
				band,kpi,fromStarRating,toStarRating, searchType);
		
		try {
			Query query = getEntityManager().createNamedQuery("getLayerData");
			SimpleDateFormat formatter = new SimpleDateFormat(ForesightConstants.DATE_FORMAT_DDMMYY);
			Date minDate = formatter.parse(fromDate);
			Date maxDate = new Date(formatter.parse(toDate).getTime()+86399999);
			query.setParameter(ConsumerFeedbackConstant.FROM_DATE, minDate);
			query.setParameter(ConsumerFeedbackConstant.TO_DATE, maxDate);
			query.setParameter(ConsumerFeedbackConstant.OPERATOR_NAME, getDefaultOperator());
			
			applyTestAreaFilter(testArea);
			applyFeedbackTypeFilter(feedbackType);
			String bandFilter = applyBandFilter(band);
			String starRatingFilter = applyStarRatingFilter(kpi,fromStarRating,toStarRating);
			String applySearchTypeFilter = applySearchTypeFilter(searchType);
			List<ConsumerFeedback> response = query.getResultList();
			disableFilter(ConsumerFeedbackConstant.TEST_AREA_FILTER);
			disableFilter(ConsumerFeedbackConstant.FEEDBACK_TYPE_FILTER);
			disableFilter(bandFilter);
			disableFilter(starRatingFilter);
			disableFilter(applySearchTypeFilter);
			logger.info("Got Response from the db :: {}", response.size());
			return response;
		} catch (Exception e) {
			logger.error("Exception in getTopLocationsWithType, error = {}", ExceptionUtils.getStackTrace(e));
			return new ArrayList<>();
		}
	}
	
	private String applySearchTypeFilter(String searchType) {
		String name =null;
		if (Utils.hasValidValue(searchType) && !searchType.equalsIgnoreCase(ForesightConstants.ALL)) {
			logger.info("Going to apply filter for search type {}",searchType);
			Session s = (Session) getEntityManager().getDelegate();
			name = addVoiceFilter(searchType, name, s);
			name = addDataFilter(searchType, name, s);
			name = addCoverageFilter(searchType, name, s);
			name = addSkypeFilter(searchType, name, s);
			name = addViberFilter(searchType, name, s);
			name = addWhatsAppFilter(searchType, name, s);
			name = addRCSFilter(searchType, name, s);
			logger.info("Find Filter for search type {} is {}",searchType,name);
		}
		return name ;
	}

	private String addRCSFilter(String testArea, String name, Session s) {
		if (testArea.equalsIgnoreCase("voIpRcsUTMCToRcs")) {
			name = s.enableFilter(ConsumerFeedbackConstant.RCS_UNABLE_TO_MAKE_CALL_TO_OTHER_RCS).getName();
		} else if (testArea.equalsIgnoreCase("voIpRcsUTMCToNonRcs")) {
			name = s.enableFilter(ConsumerFeedbackConstant.RCS_UNABLE_TO_MAKE_CALL_TO_NON_RCS).getName();
		} else if (testArea.equalsIgnoreCase("voIpRcsUTMGroupCall")) {
			name = s.enableFilter(ConsumerFeedbackConstant.RCS_UNABLE_TO_MAKE_GROUPCALL).getName();
		} else if (testArea.equalsIgnoreCase("voIpRcsCallDisconnect")) {
			name = s.enableFilter(ConsumerFeedbackConstant.RCS_CALL_DISCONNECTED).getName();
		}else if (testArea.equalsIgnoreCase("voIpRcsPoorAudio")) {
			name = s.enableFilter(ConsumerFeedbackConstant.RCS_POOR_AUDIO).getName();
		}else if (testArea.equalsIgnoreCase("voIpRcsUTSMsgToRcs")) {
			name = s.enableFilter(ConsumerFeedbackConstant.RCS_UNABLE_TO_SEND_MSG_TO_RCS).getName();
		} else if (testArea.equalsIgnoreCase("voIpRcsUTSMsgToNonRcs")) {
			name = s.enableFilter(ConsumerFeedbackConstant.RCS_UNABLE_TO_SEND_MSG_TO_NON_RCS).getName();
		} else if (testArea.equalsIgnoreCase("voIpRcsUTMGroupChat")) {
			name = s.enableFilter(ConsumerFeedbackConstant.RCS_UNABLE_TO_MAKE_GROUP_CHAT).getName();
		}else if (testArea.equalsIgnoreCase("voIpRcsUTSMultimedia")) {
			name = s.enableFilter(ConsumerFeedbackConstant.RCS_UNABLE_TO_SEND_MULTIMEDIA).getName();
		}
		return name;
	}

	private String addWhatsAppFilter(String testArea, String name, Session s) {
		if (testArea.equalsIgnoreCase("voIpWhatsAppUnableToMakeCall")) {
			name = s.enableFilter(ConsumerFeedbackConstant.WHATSAPP_UNABLE_TO_MAKE_CALL).getName();
		} else if (testArea.equalsIgnoreCase("voIpWhatsAppCallDrop")) {
			name = s.enableFilter(ConsumerFeedbackConstant.WHATSAPP_CALL_DROP).getName();
		} else if (testArea.equalsIgnoreCase("voIpWhatsAppPoorAudio")) {
			name = s.enableFilter(ConsumerFeedbackConstant.WHATSAPP_POOR_AUDIO).getName();
		} else if (testArea.equalsIgnoreCase("voIpWhatsAppMute")) {
			name = s.enableFilter(ConsumerFeedbackConstant.WHATSAPP_MUTE).getName();
		}else if (testArea.equalsIgnoreCase("voIpWhatsAppOneWayAudio")) {
			name = s.enableFilter(ConsumerFeedbackConstant.WHATSAPP_ONE_WAY_AUDIO).getName();
		}
		return name;
	}

	private String addViberFilter(String testArea, String name, Session s) {
		if (testArea.equalsIgnoreCase("voIpViberUnableToMakeCall")) {
			name = s.enableFilter(ConsumerFeedbackConstant.VIBER_UNABLE_TO_MAKE_CALL).getName();
		} else if (testArea.equalsIgnoreCase("voIpViberCallDrop")) {
			name = s.enableFilter(ConsumerFeedbackConstant.VIBER_CALL_DROP).getName();
		} else if (testArea.equalsIgnoreCase("voIpViberPoorAudio")) {
			name = s.enableFilter(ConsumerFeedbackConstant.VIBER_POOR_AUDIO).getName();
		} else if (testArea.equalsIgnoreCase("voIpViberMute")) {
			name = s.enableFilter(ConsumerFeedbackConstant.VIBER_MUTE).getName();
		}else if (testArea.equalsIgnoreCase("voIpViberOneWayAudio")) {
			name = s.enableFilter(ConsumerFeedbackConstant.VIBER_ONE_WAY_AUDIO).getName();
		}
		return name;
	}

	private String addSkypeFilter(String testArea, String name, Session s) {
		if (testArea.equalsIgnoreCase("voIpSkypeUnableToMakeCall")) {
			name = s.enableFilter(ConsumerFeedbackConstant.SKYPE_UNABLE_TO_MAKE_CALL).getName();
		} else if (testArea.equalsIgnoreCase("voIpSkypeCallDrop")) {
			name = s.enableFilter(ConsumerFeedbackConstant.SKYPE_CALL_DROP).getName();
		} else if (testArea.equalsIgnoreCase("voIpSkypePoorAudio")) {
			name = s.enableFilter(ConsumerFeedbackConstant.SKYPE_POOR_AUDIO).getName();
		} else if (testArea.equalsIgnoreCase("voIpSkypeMute")) {
			name = s.enableFilter(ConsumerFeedbackConstant.SKYPE_MUTE).getName();
		}else if (testArea.equalsIgnoreCase("voIpSkypeOneWayAudio")) {
			name = s.enableFilter(ConsumerFeedbackConstant.SKYPE_ONE_WAY_AUDIO).getName();
		}
		return name;
	}

	private String addCoverageFilter(String testArea, String name, Session s) {
		if (testArea.equalsIgnoreCase("cvgpoorcvgind")) {
			name = s.enableFilter(ConsumerFeedbackConstant.CVG_POOR_COVERAGE_INDOOR).getName();
		} else if (testArea.equalsIgnoreCase("cvgPoorCvgOut")) {
			name = s.enableFilter(ConsumerFeedbackConstant.CVG_POOR_COVERAGE_OUTDOOR).getName();
		}
		return name;
	}

	private String addDataFilter(String testArea, String name, Session s) {
		if (testArea.equalsIgnoreCase("dataslowspeed")) {
			 name = s.enableFilter(ConsumerFeedbackConstant.DATA_SLOW_SPEED).getName();
		} else if (testArea.equalsIgnoreCase("dataunavletocon")) {
			name = s.enableFilter(ConsumerFeedbackConstant.DATA_UNABLE_TO_CONNECT).getName();
		}
		return name;
	}

	private String addVoiceFilter(String testArea, String name, Session s) {
		if (testArea.equalsIgnoreCase("vcunabletocall")) {
			name = s.enableFilter(ConsumerFeedbackConstant.VOICE_UNABLE_TO_MAKE_CALL).getName();
		} else if (testArea.equalsIgnoreCase("vccalldrop")) {
			name = s.enableFilter(ConsumerFeedbackConstant.VOICE_CALL_DROP).getName();
		} else if (testArea.equalsIgnoreCase("vcpooraudio")) {
			name = s.enableFilter(ConsumerFeedbackConstant.VOICE_POOR_AUDIO).getName();
		} else if (testArea.equalsIgnoreCase("voicemute")) {
			name = s.enableFilter(ConsumerFeedbackConstant.VOICE_MUTE).getName();
		} else if (testArea.equalsIgnoreCase("vconewyaudio")) {
			name = s.enableFilter(ConsumerFeedbackConstant.VOICE_ONE_WAY_AUDIO).getName();
		}
		return name;
	}

	private String applyStarRatingFilter(String kpi, Integer fromStarRating, Integer toStarRating) {
		if (Utils.hasValidValue(kpi) && !kpi.equalsIgnoreCase(ForesightConstants.ALL) && Utils.hasValue(fromStarRating) && 
				Utils.hasValue(toStarRating) ) {
			Session s = (Session) getEntityManager().getDelegate();
			if (kpi.equalsIgnoreCase(ConsumerFeedbackConstant.COVERAGE)) {
				Filter filter = s.enableFilter(ConsumerFeedbackConstant.COVERAGE_STAR_RATING_FILTER);
				filter.setParameter(ConsumerFeedbackConstant.FROM_STAR_RATING, fromStarRating).
				setParameter(ConsumerFeedbackConstant.TO_STAR_RATING,toStarRating);
				return ConsumerFeedbackConstant.COVERAGE_STAR_RATING_FILTER;
			} else if (kpi.equalsIgnoreCase(ConsumerFeedbackConstant.DATA)) {
				Filter filter = s.enableFilter(ConsumerFeedbackConstant.DATA_STAR_RATING_FILTER);
				filter.setParameter(ConsumerFeedbackConstant.FROM_STAR_RATING, fromStarRating).
				setParameter(ConsumerFeedbackConstant.TO_STAR_RATING,toStarRating);
				return ConsumerFeedbackConstant.DATA_STAR_RATING_FILTER;
			} else if (kpi.equalsIgnoreCase(ConsumerFeedbackConstant.VOICE)) {
				Filter filter = s.enableFilter(ConsumerFeedbackConstant.VOICE_STAR_RATING_FILTER);
				filter.setParameter(ConsumerFeedbackConstant.FROM_STAR_RATING, fromStarRating).
				setParameter(ConsumerFeedbackConstant.TO_STAR_RATING,toStarRating);
				return ConsumerFeedbackConstant.VOICE_STAR_RATING_FILTER;
			} else if (kpi.equalsIgnoreCase(ConsumerFeedbackConstant.VOIP)) {
				Filter filter = s.enableFilter(ConsumerFeedbackConstant.VOIP_STAR_RATING_FILTER);
				filter.setParameter(ConsumerFeedbackConstant.FROM_STAR_RATING, fromStarRating).
				setParameter(ConsumerFeedbackConstant.TO_STAR_RATING,toStarRating);
				return ConsumerFeedbackConstant.VOIP_STAR_RATING_FILTER;
			}
		}
		return null;
	}

	private String applyBandFilter(String band) {
		if (Utils.hasValidValue(band) && !band.equalsIgnoreCase(ForesightConstants.ALL)) {
			Session s = (Session) getEntityManager().getDelegate();
			if (band.equalsIgnoreCase(ConsumerFeedbackConstant.BAND_2300_FILTER)) {
				logger.info("Going to apply band 2300 filter");
				s.enableFilter(ConsumerFeedbackConstant.BAND_2300_FILTER);
				return ConsumerFeedbackConstant.BAND_2300_FILTER;
			} else if (band.equalsIgnoreCase(ConsumerFeedbackConstant.BAND_1800_FILTER)) {
				logger.info("Going to apply band 1800 filter");
				s.enableFilter(ConsumerFeedbackConstant.BAND_1800_FILTER);
				return ConsumerFeedbackConstant.BAND_1800_FILTER;
			} else if (band.equalsIgnoreCase(ConsumerFeedbackConstant.BAND_850_FILTER)) {
				logger.info("Going to apply band 850 filter");
				s.enableFilter(ConsumerFeedbackConstant.BAND_850_FILTER);
				return ConsumerFeedbackConstant.BAND_850_FILTER;
			}
		}
		return null;
	}

	private void applyFeedbackTypeFilter(String feedbackType) {
		if (Utils.hasValidValue(feedbackType) && !feedbackType.equalsIgnoreCase(ForesightConstants.ALL)) {
			logger.info("Going to apply feedback type filter");
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(ConsumerFeedbackConstant.FEEDBACK_TYPE_FILTER);
			filter.setParameter("feedbackType", ForesightConstants.MODULUS + feedbackType + ForesightConstants.MODULUS);
		}
	}

	private void applyTestAreaFilter(String testArea) {
		if (Utils.hasValidValue(testArea) && !testArea.equalsIgnoreCase(ForesightConstants.ALL)) {
			logger.info("Going to apply test type filter");
			Session s = (Session) getEntityManager().getDelegate();
			Filter filter = s.enableFilter(ConsumerFeedbackConstant.TEST_AREA_FILTER);
			filter.setParameter("testArea", ForesightConstants.MODULUS + testArea + ForesightConstants.MODULUS);
		}
	}
	
	@Override
	@Transactional
	public List<ConsumerFeedback> getPciWiseRating(Integer pci,Integer cellid) {
		Query query = getEntityManager().createNamedQuery("getPciWiseStarRating");
		query.setParameter(ConsumerFeedbackConstant.PCI, pci);
		query.setParameter(ConsumerFeedbackConstant.CELL_ID, cellid);
		List<ConsumerFeedback> response = query.getResultList();
		return response;
		
	}

	/** ECare Layer code */
	@Override
	public List<ConsumerFeedback> getLayerDataForECare(String locationType, String searchType, String kpi, Date fromDate, Date toDate, String orderBy,
			String orderType) {
		logger.info("Going to get ECare layer data for locationType: {}, searchType: {},kpi: {},fromDate: {},toDate: {},orderBy: {},orderType: {}",
				locationType, searchType, kpi, fromDate, toDate, orderBy, orderType);
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<ConsumerFeedback> criteriaQuery = criteriaBuilder.createQuery(ConsumerFeedback.class);
			Root<ConsumerFeedback> root = criteriaQuery.from(ConsumerFeedback.class);
			List<Predicate> finalPredicate = new ArrayList<>();

			commonPredicates(fromDate, toDate, criteriaBuilder, root, finalPredicate);
			locationTypePredicates(locationType, criteriaBuilder, root, finalPredicate);
			searchTypePredicates(searchType, criteriaBuilder, root, finalPredicate, kpi);
			populateOrderByCondition(orderBy, orderType, criteriaBuilder, criteriaQuery, root);

			criteriaQuery.where(finalPredicate.toArray(new Predicate[] {}));

			return getEntityManager().createQuery(criteriaQuery).getResultList();
		} catch (Exception e) {
			logger.error("Error in getting ECare layer data from DB for KPI : {} Exception : {}", kpi, Utils.getStackTrace(e));
			return new ArrayList<>();
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

	private void populateOrderByCondition(String orderBy, String orderType, CriteriaBuilder criteriaBuilder,
			CriteriaQuery<ConsumerFeedback> criteriaQuery, Root<ConsumerFeedback> root) {
		if (Utils.hasValidValue(orderBy) && Utils.hasValidValue(orderType)) {
			if (orderType.equalsIgnoreCase(ConsumerFeedbackConstant.ECARE_DESC)) {
				criteriaQuery.orderBy(criteriaBuilder.desc(root.get(orderBy)));
			} else {
				criteriaQuery.orderBy(criteriaBuilder.asc(root.get(orderBy)));
			}
		} else {
			criteriaQuery.orderBy(criteriaBuilder.desc(root.get(ConsumerFeedbackConstant.ECARE_FEEDBACKTIME)));
		}
	}

	private void searchTypePredicates(String searchType, CriteriaBuilder criteriaBuilder, Root<ConsumerFeedback> root, List<Predicate> finalPredicate,
			String kpi) {
		if (isValidKpi(kpi) && isValidDataForECareFilter(searchType)) {
			List<Predicate> searchTypePredicates = new ArrayList<>();
			List<String> searchTypeList = Arrays.asList(searchType.split(ForesightConstants.COMMA));
			searchTypeList.forEach(reason -> searchTypePredicates
					.add(criteriaBuilder.like(root.get(ConsumerFeedbackConstant.DATA), ForesightConstants.MODULUS + ForesightConstants.QUOTE + reason
							+ ConsumerFeedbackConstant.ECARE_TRUE_STRING_FOR_FILTER + ForesightConstants.MODULUS)));
			finalPredicate.add(criteriaBuilder.or(searchTypePredicates.toArray(new Predicate[] {})));
		}
	}

	private boolean isValidDataForECareFilter(String filterName) {
		return Utils.hasValidValue(filterName) && !filterName.equalsIgnoreCase(ConsumerFeedbackConstant.ECARE_ALL);
	}

	private void locationTypePredicates(String locationType, CriteriaBuilder criteriaBuilder, Root<ConsumerFeedback> root,
			List<Predicate> finalPredicate) {
		if (isValidDataForECareFilter(locationType)) {
			List<Predicate> locationTypePredicates = new ArrayList<>();
			List<String> locationTypeList = Arrays.asList(locationType.split(ForesightConstants.COMMA));
			locationTypeList.forEach(location -> locationTypePredicates
					.add(criteriaBuilder.like(root.get(ConsumerFeedbackConstant.DATA), ForesightConstants.MODULUS + ForesightConstants.QUOTE
							+ location + ConsumerFeedbackConstant.ECARE_TRUE_STRING_FOR_FILTER + ForesightConstants.MODULUS)));
			finalPredicate.add(criteriaBuilder.or(locationTypePredicates.toArray(new Predicate[] {})));
		}
	}

	private void commonPredicates(Date fromDate, Date toDate, CriteriaBuilder criteriaBuilder, Root<ConsumerFeedback> root,
			List<Predicate> finalPredicate) {
		finalPredicate
				.add(root.get(ConsumerFeedbackConstant.NV_MODULE).in(ConfigUtils.getStringList(ConsumerFeedbackConstant.ECARE_LAYER_NVMODULE_LIST)));
		finalPredicate.add(root.get(ConsumerFeedbackConstant.OPERATOR_NAME).in(getDefaultOperatorForECare()));
		finalPredicate.add(criteriaBuilder.between(root.get(ConsumerFeedbackConstant.ECARE_FEEDBACKTIME), fromDate, toDate));
	}

	@Override
	public List<ConsumerFeedback> getEcareDashboardData(List<Long> moduleCodeList, Integer geographyId, String level, String date) {
		try {
			Query query = getEntityManager().createNamedQuery("getFeedbackDashboard");
			SimpleDateFormat formatter = new SimpleDateFormat(ForesightConstants.DATE_FORMAT_DDMMYY);
			Date minDate = formatter.parse(date);
			logger.info("Filter for  Date {}",minDate);
			query.setParameter(ConsumerFeedbackConstant.DATE, minDate);
			query.setParameter(ConsumerFeedbackConstant.OPERATOR_NAME, getDefaultOperator());
			query.setParameter("nvmoduleCode",moduleCodeList);
			String geographyFilterName = getGeographyFilterName(level);
			applyGeographyFilter(geographyFilterName, Arrays.asList(geographyId));
			List<ConsumerFeedback> response = query.getResultList();
			disableFilter(geographyFilterName);
			logger.info("Got Response from the db :: {}", response.size());
			return response;
		} catch (ParseException e) {
			logger.info("No Data Found for the query:-");
		}
		return new ArrayList<>();
	}
}
