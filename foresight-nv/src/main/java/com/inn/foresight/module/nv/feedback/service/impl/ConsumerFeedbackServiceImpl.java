package com.inn.foresight.module.nv.feedback.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.encoder.MobileEncoder;
import com.inn.commons.maps.Corner;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.grid.DegreeGrid;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.DateUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.feedback.constants.ConsumerFeedbackConstant;
import com.inn.foresight.module.nv.feedback.dao.ConsumerCustomFeedbackDao;
import com.inn.foresight.module.nv.feedback.dao.IConsumerFeedbackDao;
import com.inn.foresight.module.nv.feedback.model.ConsumerCustomFeedback;
import com.inn.foresight.module.nv.feedback.model.ConsumerFeedback;
import com.inn.foresight.module.nv.feedback.service.IConsumerFeedbackService;
import com.inn.foresight.module.nv.feedback.utils.ConsumerFeedbackUtils;
import com.inn.foresight.module.nv.feedback.wrapper.ConsumerFeedbackWrapper;
import com.inn.foresight.module.nv.feedback.wrapper.CustomFeedbackRequestWrapper;
import com.inn.foresight.module.nv.feedback.wrapper.CustomFeedbackResponseWrapper;
import com.inn.foresight.module.nv.profile.constants.NVProfileConstants;
import com.inn.foresight.module.nv.profile.model.NVProfileData;
import com.inn.foresight.module.nv.profile.service.INVProfileDataService;
import com.inn.product.security.utils.AuthenticationCommonUtil;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.model.SystemConfiguration;
import com.inn.product.systemconfiguration.service.SystemConfigurationService;
import com.inn.product.systemconfiguration.utils.SystemConfigurationUtils;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.geography.service.GeographyL4Service;

/**
 * The Class ConsumerFeedbackServiceImpl.
 * 
 * @author innoeye
 */

@Service("ConsumerFeedbackServiceImpl")
public class ConsumerFeedbackServiceImpl extends AbstractService<Integer, ConsumerFeedback>
		implements IConsumerFeedbackService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(ConsumerFeedbackServiceImpl.class);

	/** The consumer feedback dao. */
	@Autowired
	private IConsumerFeedbackDao iConsumerFeedbackDao;

	/** The service. */
	@Autowired
	private GeographyL4Service iGeographyL4Service;

	/** The i generic map service. */
	@Autowired
	private IGenericMapService iGenericMapService;

	/** The i system configuration service. */
	@Autowired
	private SystemConfigurationService iSystemConfigurationService;

	@Autowired
	private INVProfileDataService iNVProfileDataService;

	@Autowired
	private SystemConfigurationDao iSystemConfigurationDao;
	
	@Autowired
	private ConsumerCustomFeedbackDao consumerFeedbackEcareDao; 
	/**
	 * Sets the dao.
	 *
	 * @param dao
	 *            the new dao
	 */
	@Autowired
	public void setDao(IConsumerFeedbackDao dao) {
		super.setDao(dao);
		iConsumerFeedbackDao = dao;
	}

	/**
	 * Create ConsumerFeedback take encryptedString.
	 *
	 * @param encryptedString
	 *            the encrypted string
	 * @return the consumer feedback
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	@Transactional
	public ConsumerFeedback createFeedback(String encryptedString) {
		try {
			logger.info("in createFeedback From EncryptString");
			String decryptedJson = getDecryptedJson(encryptedString);
			ConsumerFeedback feedback = new ConsumerFeedback();
			ConsumerFeedbackWrapper feedBackWrapper = new Gson().fromJson(decryptedJson, ConsumerFeedbackWrapper.class);
			feedback = ConsumerFeedbackUtils.getConsumerFeedbackWrapper(feedBackWrapper, feedback);
			feedback.setOperatorName(getOperator(feedback));
			feedback.setGeographyL4(getGeographyL4(feedback));
			feedback.setNvProfileData(getNVProfileData(feedBackWrapper));
			ConsumerFeedback create = iConsumerFeedbackDao.create(feedback);
			Boolean isCustomFeedbackReq = ConfigUtils.getBoolean(NVConfigUtil.IS_CUSTOM_FEEDBACK_REQ);
			String nvModule = create.getNvModule();
			if (isCustomFeedbackReq.booleanValue() && nvModule != null && Arrays.asList(ConsumerFeedbackConstant.ecareFeedbackType).contains(nvModule)) {
				setFeedbackGridData(create);
			}
			return create;				
		} catch (DaoException e) {
			logger.info("Problem in creating feedback in DB{}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		} catch (Exception e) {
			logger.error("error in creating consumer feedback, error = {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	}
	
	private void setFeedbackGridData(ConsumerFeedback feedback) {
		String data = feedback.getData();
		if (Utils.isValidString(data)) {
			ConsumerCustomFeedback consumerFeedbackEcare = new ConsumerCustomFeedback();
			Map<String, String> convertStringToMap = convertJsonToMap(data);

			Double latitude = getDoubleValueFromMap(convertStringToMap.get(ConsumerFeedbackConstant.LATITUDE));
			Double longitude = getDoubleValueFromMap(convertStringToMap.get(ConsumerFeedbackConstant.LONGITUDE));

			if (Utils.isValidDouble(latitude) && Utils.isValidDouble(longitude)) {
				
				setGridData(latitude, longitude, consumerFeedbackEcare);
				consumerFeedbackEcare.setConsumerFeedback(feedback);
				
				Long feebackDate = Long.parseLong(convertStringToMap.get(ConsumerFeedbackConstant.FEEDBACKDATE));
				consumerFeedbackEcare.setFeedbackDate(new java.sql.Date(feebackDate));
				
				Boolean isDataFeedback = getBooleanValueFromMap(convertStringToMap.get(ConsumerFeedbackConstant.ECARE_IS_DATA_FEEDBACK_KEY));
				consumerFeedbackEcare.setIsDataFeedback(isDataFeedback);

				Boolean isVoiceFeedback = getBooleanValueFromMap(convertStringToMap.get(ConsumerFeedbackConstant.ECARE_IS_VOICE_FEEDBACK_KEY));
				consumerFeedbackEcare.setIsVoiceFeedback(isVoiceFeedback);

				String feedbackAddress = convertStringToMap.get(ConsumerFeedbackConstant.FEEDBACK_ADDRESS);
				consumerFeedbackEcare.setFeedbackAddress(feedbackAddress);

				Integer internetQualityRate = getIntValueFromMap(convertStringToMap.get(ConsumerFeedbackConstant.INTERNET_QUALITY_RATE));
				consumerFeedbackEcare.setInternetQualityRate(internetQualityRate);

				Integer callsQualityRate = getIntValueFromMap(convertStringToMap.get(ConsumerFeedbackConstant.CALLS_QUALITY_RATE));
				consumerFeedbackEcare.setCallsQualityRate(callsQualityRate);

				Boolean dataSlowConnection = getBooleanValueFromMap(convertStringToMap.get(ConsumerFeedbackConstant.ECARE_DATA_SLOW_CONNECTION_KEY));
				consumerFeedbackEcare.setDataSlowConnection(dataSlowConnection);

				Boolean dataNoConnection = getBooleanValueFromMap(convertStringToMap.get(ConsumerFeedbackConstant.ECARE_DATA_NO_CONNECTION_KEY));
				consumerFeedbackEcare.setDataNoConnection(dataNoConnection);

				Boolean dataVoiceCallMute = getBooleanValueFromMap(convertStringToMap.get(ConsumerFeedbackConstant.ECARE_VOICE_CALL_MUTE_KEY));
				consumerFeedbackEcare.setDataVoiceCallMute(dataVoiceCallMute);

				Boolean dataVoiceCallFailed = getBooleanValueFromMap(convertStringToMap.get(ConsumerFeedbackConstant.ECARE_VOICE_CALL_FAILED_KEY));
				consumerFeedbackEcare.setDataVoiceCallFailed(dataVoiceCallFailed);

				Boolean dataVoiceCallOnWay = getBooleanValueFromMap(convertStringToMap.get(ConsumerFeedbackConstant.ECARE_VOICE_CALL_ONE_WAY_KEY));
				consumerFeedbackEcare.setDataVoiceCallOneWay(dataVoiceCallOnWay);

				Boolean dataVoiceCallDegradation = getBooleanValueFromMap(
						convertStringToMap.get(ConsumerFeedbackConstant.ECARE_VOICE_CALL_DEGRADATION_KEY));
				consumerFeedbackEcare.setDataVoiceCallDegradation(dataVoiceCallDegradation);

				Boolean dataVoiceCallNotAvailable = getBooleanValueFromMap(
						convertStringToMap.get(ConsumerFeedbackConstant.ECARE_VOICE_CALL_NOT_AVAILABLE_KEY));
				consumerFeedbackEcare.setDataVoiceCallNotAvailable(dataVoiceCallNotAvailable);

				Boolean locationTypeIndoor = getBooleanValueFromMap(convertStringToMap.get(ConsumerFeedbackConstant.LOCATION_TYPE_INDOOR));
				consumerFeedbackEcare.setLocationTypeIndoor(locationTypeIndoor);

				Boolean locationTypeOutside = getBooleanValueFromMap(convertStringToMap.get(ConsumerFeedbackConstant.LOCATION_TYPE_OUTDOOR));
				consumerFeedbackEcare.setLocationTypeOutside(locationTypeOutside);

				Boolean locationTypeOnTheMove = getBooleanValueFromMap(convertStringToMap.get(ConsumerFeedbackConstant.LOCATION_TYPE_ON_THE_MOVE));
				consumerFeedbackEcare.setLocationTypeOnTheMove(locationTypeOnTheMove);

				updateConsumerFeedback(feedback, latitude, longitude, feebackDate);
				createCustomFeedback(consumerFeedbackEcare);
			}
		}
	}

	private void updateConsumerFeedback(ConsumerFeedback feedback, Double latitude, Double longitude, Long feebackDate) {
		try {
			feedback.setLatitude(latitude);
			feedback.setLongitude(longitude);
			feedback.setFeedbacktime(new java.sql.Date(feebackDate));
			iConsumerFeedbackDao.update(feedback);
		} catch (Exception e) {
			logger.error("Error in updating Consumer Feedback data for custom feedback Exception : {}", Utils.getStackTrace(e));
		}
	}

	
	public void createCustomFeedback(ConsumerCustomFeedback consumerFeedbackEcare) {
		try {
			consumerFeedbackEcareDao.create(consumerFeedbackEcare);
		} catch (Exception e) {
			logger.error("Error in creating custom feekback data Exception : {}", Utils.getStackTrace(e));
		}
	}

	private Integer getIntValueFromMap(String data) {
		if(Utils.isValidString(data)) {
			return Integer.valueOf(data);
		}
		return null;
		
	}

	private Boolean getBooleanValueFromMap(String data) {
		if(Utils.isValidString(data)) {
			return Boolean.valueOf(data);
		}
		return false;
		
	}

	private Double getDoubleValueFromMap(String data) {
		if(Utils.isValidString(data)) {
			return Double.valueOf(data);
		}
		return null;
		
	}

	private void setGridData(Double latitude, Double longitude, ConsumerCustomFeedback consumerFeedbackEcare) {
		Double countryLat =getCountryLat();
		Double countryLng =getCountryLng();
		String customGridJsonValue = iSystemConfigurationDao.getValueByName("CUSTOM_ZOOM_GRID");
	
		Map<String,Integer> customGridMap = new Gson().fromJson(customGridJsonValue, new TypeToken<Map<String,Integer>>() {
		}.getType());
		
		LatLng grid5 = new DegreeGrid(customGridMap.get("zoom5"), new LatLng(countryLat, countryLng))
		.getGrid(new LatLng(latitude, longitude));
		
		consumerFeedbackEcare.setZoom5(grid5.getLatitude() + ForesightConstants.UNDERSCORE + grid5.getLongitude());
		
		LatLng grid6 = new DegreeGrid(customGridMap.get("zoom6"), new LatLng(countryLat, countryLng))
				.getGrid(new LatLng(latitude, longitude));
		consumerFeedbackEcare.setZoom6(grid6.getLatitude() + ForesightConstants.UNDERSCORE + grid6.getLongitude());
		
		LatLng grid7 = new DegreeGrid(customGridMap.get("zoom7"), new LatLng(countryLat, countryLng))
				.getGrid(new LatLng(latitude, longitude));
		consumerFeedbackEcare.setZoom7(grid7.getLatitude() + ForesightConstants.UNDERSCORE + grid7.getLongitude());
		
		LatLng grid8 = new DegreeGrid(customGridMap.get("zoom8"), new LatLng(countryLat, countryLng))
				.getGrid(new LatLng(latitude, longitude));
		consumerFeedbackEcare.setZoom8(grid8.getLatitude() + ForesightConstants.UNDERSCORE + grid8.getLongitude());
		
		LatLng grid9 = new DegreeGrid(customGridMap.get("zoom9"), new LatLng(countryLat, countryLng))
				.getGrid(new LatLng(latitude, longitude));
		consumerFeedbackEcare.setZoom9(grid9.getLatitude() + ForesightConstants.UNDERSCORE + grid9.getLongitude());
		
		LatLng grid10 = new DegreeGrid(customGridMap.get("zoom10"), new LatLng(countryLat, countryLng))
				.getGrid(new LatLng(latitude, longitude));
		consumerFeedbackEcare.setZoom10(grid10.getLatitude() + ForesightConstants.UNDERSCORE + grid10.getLongitude());
		
		LatLng grid11 = new DegreeGrid(customGridMap.get("zoom11"), new LatLng(countryLat, countryLng))
				.getGrid(new LatLng(latitude, longitude));
		consumerFeedbackEcare.setZoom11(grid11.getLatitude() + ForesightConstants.UNDERSCORE + grid11.getLongitude());
		
		LatLng grid12 = new DegreeGrid(customGridMap.get("zoom12"), new LatLng(countryLat, countryLng))
				.getGrid(new LatLng(latitude, longitude));
		consumerFeedbackEcare.setZoom12(grid12.getLatitude() + ForesightConstants.UNDERSCORE + grid12.getLongitude());
		

		LatLng grid13 = new DegreeGrid(customGridMap.get("zoom13"), new LatLng(countryLat, countryLng))
				.getGrid(new LatLng(latitude, longitude));
		consumerFeedbackEcare.setZoom13(grid13.getLatitude() + ForesightConstants.UNDERSCORE + grid13.getLongitude());
		
		LatLng grid14 = new DegreeGrid(customGridMap.get("zoom14"), new LatLng(countryLat, countryLng))
				.getGrid(new LatLng(latitude, longitude));
		consumerFeedbackEcare.setZoom14(grid14.getLatitude() + ForesightConstants.UNDERSCORE + grid14.getLongitude());

	}

	private NVProfileData getNVProfileData(ConsumerFeedbackWrapper feedbackWrapper) {
		NVProfileData nvProfileData = null;
		try {
			if (feedbackWrapper.getProfileId() != null) {
				nvProfileData = iNVProfileDataService.getNVProfileByProfileId(feedbackWrapper.getProfileId());
				logger.info("Done nvProfileData in createFeedback");
			}
		} catch (RestException e) {
			logger.error("error in creating consumer feedback getNVProfileData, error = {}",
					ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return nvProfileData;
	}

	/**
	 * Gets the decrypted json.
	 *
	 * @param encryptedString
	 *            the encrypted string
	 * @return the decrypted json
	 * @throws RestException
	 *             the rest exception
	 */
	private String getDecryptedJson(String encryptedString) {
		String decryptedJson;
		try {
			decryptedJson = AuthenticationCommonUtil.checkForValueDecryption(encryptedString);
//			decryptedJson=MobileEncoder.decryptUsingCBC(encryptedString);
			logger.info("decrypted test {}",decryptedJson);
		} catch (Exception e) {
			logger.error("error in decrypting input string, error = {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return decryptedJson;
	}

	/**
	 * Get GeographyL4.
	 *
	 * @param consumerFeedback
	 *            the consumer feedback
	 * @return the geography L 4
	 */
	public GeographyL4 getGeographyL4(ConsumerFeedback consumerFeedback) {
		try {
			String geographyL4Name = getGeographyName(consumerFeedback);
			logger.info("geographyL4Name:{}", geographyL4Name);
				if (!Utils.isValidString(geographyL4Name)) {
					throw new RestException(ForesightConstants.GEOGRAPHY_NOT_FOUND);
				}
				return iGeographyL4Service.getGeographyL4ByName(geographyL4Name);
		} catch (Exception e) {
			logger.error("Getting Exception in tagging geography, error = {}", ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Gets the geography name.
	 *
	 * @param consumerFeedback
	 *            the consumer feedback
	 * @return the geography name
	 * @throws RestException
	 *             the rest exception
	 */
	@SuppressWarnings("serial")
	private String getGeographyName(ConsumerFeedback consumerFeedback) {
		String geograpghy = iGenericMapService.getGeographyDataByPoint(GenericMapUtils.getGeoColumnList(),
		        GenericMapUtils.GEOGRAPHY_TABLE_NAME, consumerFeedback.getLatitude(),
				consumerFeedback.getLongitude(), false,GenericMapUtils.L4_TYPE);
		List<List<String>> list;
		list = new Gson().fromJson(geograpghy, new TypeToken<List<List<String>>>() {
		}.getType());
		if (list.isEmpty()) {
			throw new RestException(ForesightConstants.GEOGRAPHY_NOT_FOUND);
		}
		return list	.get(ForesightConstants.ZERO).get(ForesightConstants.ZERO);
	}

	/**
	 * Gets the operator.
	 *
	 * @param wrapper
	 *            the wrapper
	 * @return the operator
	 * @throws RestException
	 *             the rest exception
	 */
	@SuppressWarnings("serial")
	private String getOperator(ConsumerFeedback wrapper) {
		List<SystemConfiguration> systemConfigurationList = iSystemConfigurationService.getSystemConfigurationByNameAndType(
				NVProfileConstants.OPERATOR, Arrays.asList(NVProfileConstants.NV_PROFILE_DATA));
		SystemConfiguration configuration = systemConfigurationList.get(NVProfileConstants.INDEX_ZERO);
		Map<String, String> operators = new Gson().fromJson(configuration.getValue(),
				new TypeToken<Map<String, String>>() {}.getType());
		return operators.get(wrapper.getMcc() + Symbol.UNDERSCORE_STRING + wrapper.getMnc());
	}

	@Override
	public Map<String, Map<String, Object>> getFeedbackAvgRating(Date fromDate, Date toDate, String  level, Integer geographyId) {
		try {
			Map<String, Map<String, Object>> feedbackMap = new HashMap<>();
			
			List<ConsumerFeedbackWrapper> feedbackCountList = iConsumerFeedbackDao.getFeedBackAvgRating(toDate, level, geographyId);
			logger.info("getFeedbackAvgRating feedbackCountList {}" , feedbackCountList.size());
			
			for (ConsumerFeedbackWrapper consumerFeedbackWrapper : feedbackCountList) {
				Map<String, Object> avgData = new HashMap<>();
				avgData.put(ConsumerFeedbackConstant.COUNT, consumerFeedbackWrapper.getCount());
				avgData.put(ConsumerFeedbackConstant.RATING, consumerFeedbackWrapper.getAvgStarRating());
				feedbackMap.put(consumerFeedbackWrapper.getFeedbackType(), avgData);	
			}

			Map<String, Map<String, Long>> feedbackDateWiseMap = getDateWiseFeedBackCount(fromDate, toDate, level, geographyId);
			logger.info("after getDateWiseFeedBackCount {}" , feedbackDateWiseMap);
			
			Map<String, Map<String, Long>> feedbackRatingData = getDateRangeWiseData(fromDate, toDate, feedbackDateWiseMap);
			logger.info("after getDateRangeWiseData {}" , feedbackRatingData);
			
	        feedbackMap = getFormatedRatingMap(feedbackRatingData, feedbackMap);
	        logger.info("after getFormatedRatingMap {}" , feedbackMap);
		
			return feedbackMap;
		} catch( Exception e) {
			logger.error("Getting Exception while getFeedBackRating, error = {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}
	
	public Map<String, Map<String, Object>> getFormatedRatingMap(Map<String, Map<String, Long>> feedbackRatingData, Map<String, Map<String, Object>> feedbackMap){
		try {
			Iterator<Map.Entry<String, Map<String, Long>>> itr = feedbackRatingData.entrySet().iterator();
	        while(itr.hasNext()){
	        	Map.Entry<String, Map<String, Long>> entry = itr.next();
	        	Map<String, Object> newMap = new HashMap<>();
	        	if(feedbackMap.get(entry.getKey()) != null) {
	        		newMap = feedbackMap.get(entry.getKey());
	        	}
	        	
	        	SortedMap<String, Long> sortedRatingMap = new TreeMap<>(feedbackRatingData.get(entry.getKey()));
				sortedRatingMap.putAll(feedbackRatingData.get(entry.getKey()));
	        	
				newMap.put(ConsumerFeedbackConstant.DATA, sortedRatingMap);
	        	feedbackMap.put(entry.getKey(), newMap);
	        }
		}catch(Exception e) {
			logger.error("Getting Exception while getFormatedRatingMap, error = {}", ExceptionUtils.getStackTrace(e));
		}
		return feedbackMap;
	}
	
	public List<Integer> getRatingRangeList(Integer startRating, Integer endRating){
		List<Integer> ratingList = new ArrayList<>();
		for (int i = startRating; i <= endRating; i++) {
			ratingList.add(i);
		}
		return ratingList;
	}
	
	@Override
	public Map<String, Map<Integer, Long>> getFeedbackRatingWithCount(Date date, String  level, Integer geographyId) {
		try {
			List<ConsumerFeedbackWrapper> ratingList = iConsumerFeedbackDao.getFeedBackRatingWithCount(date, level, geographyId);
			logger.info("ratingList for getFeedbackRatingWithCount : {}" , ratingList.size());
			
			Map<String, Map<Integer, Long>> feedbackRatingMap = new HashMap<>();

			for (ConsumerFeedbackWrapper consumerFeedbackWrapper : ratingList) {
				Map<Integer, Long> ratingMap = new HashMap<>();
				Long starRatingCount = NumberUtils.LONG_ZERO;
				Long starRatingDataCount = NumberUtils.LONG_ZERO;
				Long starRatingCvgCount = NumberUtils.LONG_ZERO;
				
				if(consumerFeedbackWrapper.getFeedbackType().equals(ConsumerFeedbackConstant.CAMPAIGN_FEEDBACK)) {
					
					if(consumerFeedbackWrapper.getStarRating() != null && consumerFeedbackWrapper.getStarRating() != ForesightConstants.ZERO) {
						if(feedbackRatingMap.get(ConsumerFeedbackConstant.CAMPAIGN_VOICE) != null) {
							ratingMap = feedbackRatingMap.get(ConsumerFeedbackConstant.CAMPAIGN_VOICE);
						}
						if(ratingMap.get(consumerFeedbackWrapper.getStarRating()) != null) {
							starRatingCount = ratingMap.get(consumerFeedbackWrapper.getStarRating());
						}
						starRatingCount = starRatingCount + consumerFeedbackWrapper.getStarRatingCount();
						ratingMap.put(consumerFeedbackWrapper.getStarRating(), starRatingCount);
						feedbackRatingMap.put(ConsumerFeedbackConstant.CAMPAIGN_VOICE, ratingMap);
					}
					
					if(consumerFeedbackWrapper.getStarRatingData() != null && consumerFeedbackWrapper.getStarRatingData() != ForesightConstants.ZERO) {
						ratingMap = new HashMap<>();
						if(feedbackRatingMap.get(ConsumerFeedbackConstant.CAMPAIGN_DATA) != null) {
							ratingMap = feedbackRatingMap.get(ConsumerFeedbackConstant.CAMPAIGN_DATA);
						}
						
						if(ratingMap.get(consumerFeedbackWrapper.getStarRatingData()) != null) {
							starRatingDataCount = ratingMap.get(consumerFeedbackWrapper.getStarRatingData());
						}
						starRatingDataCount = starRatingDataCount + consumerFeedbackWrapper.getStarRatingDataCount();
						
						ratingMap.put(consumerFeedbackWrapper.getStarRatingData(), starRatingDataCount);
						feedbackRatingMap.put(ConsumerFeedbackConstant.CAMPAIGN_DATA, ratingMap);
					}
					
					if(consumerFeedbackWrapper.getStarRatingCoverage() != null && consumerFeedbackWrapper.getStarRatingCoverage() != ForesightConstants.ZERO) {
						ratingMap = new HashMap<>();
						if(feedbackRatingMap.get(ConsumerFeedbackConstant.CAMPAIGN_COVERAGE) != null) {
							ratingMap = feedbackRatingMap.get(ConsumerFeedbackConstant.CAMPAIGN_COVERAGE);
						}
						
						if(ratingMap.get(consumerFeedbackWrapper.getStarRatingCoverage()) != null) {
							starRatingCvgCount = ratingMap.get(consumerFeedbackWrapper.getStarRatingCoverage());
						}
						starRatingCvgCount = starRatingCvgCount + consumerFeedbackWrapper.getStarRatingCvgCount();
						
						ratingMap.put(consumerFeedbackWrapper.getStarRatingCoverage(), starRatingCvgCount);
						feedbackRatingMap.put(ConsumerFeedbackConstant.CAMPAIGN_COVERAGE, ratingMap);
					}
				}
				
				if(consumerFeedbackWrapper.getFeedbackType().equals(ConsumerFeedbackConstant.EVENT_FEEDBACK)
						&& consumerFeedbackWrapper.getStarRating() != null && consumerFeedbackWrapper.getStarRating() != ForesightConstants.ZERO) {
					ratingMap = new HashMap<>();
					if(feedbackRatingMap.get(ConsumerFeedbackConstant.EVENT_VOICE) != null) {
						ratingMap = feedbackRatingMap.get(ConsumerFeedbackConstant.EVENT_VOICE);
					}
					
					if(ratingMap.get(consumerFeedbackWrapper.getStarRating()) != null) {
						starRatingCount = ratingMap.get(consumerFeedbackWrapper.getStarRating());
					}
					starRatingCount = starRatingCount + consumerFeedbackWrapper.getStarRatingCount();
					
					ratingMap.put(consumerFeedbackWrapper.getStarRating(), starRatingCount);
					feedbackRatingMap.put(ConsumerFeedbackConstant.EVENT_VOICE, ratingMap);
				}
			}
			logger.info("after getFeedbackRatingWithCount {}" , feedbackRatingMap);

			return getRatingWiseData(feedbackRatingMap);
	        
		} catch (Exception e) {
			logger.error("Getting Exception while getFeedbackRatingWithCount, error = {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}
	
	public Map<String, Map<Integer, Long>> getRatingWiseData(Map<String, Map<Integer, Long>> feedbackRatingMap) {
		Map<String, Map<Integer, Long>>  newFeedbackRatingMap = new HashMap<>();
		try {
			List<Integer> ratingRangeList = getRatingRangeList(ConsumerFeedbackConstant.RATING_ONE, ConsumerFeedbackConstant.RATING_FIVE);
			logger.info("ratingRangeList {}" , ratingRangeList);
			
	        Iterator<Map.Entry<String, Map<Integer, Long>>> itr = feedbackRatingMap.entrySet().iterator();
	        while(itr.hasNext()){
	        	Map.Entry<String, Map<Integer, Long>> entry = itr.next();
	            Map<Integer, Long> ratingMap = feedbackRatingMap.get(entry.getKey());
				for (Integer rating : ratingRangeList) {
					if(!ratingMap.containsKey(rating)) {
						ratingMap.put(rating, null);
					}
				}
				
				SortedMap<Integer, Long> sortedRatingMap = new TreeMap<>(ratingMap);
				sortedRatingMap.putAll(ratingMap);
				newFeedbackRatingMap.put(entry.getKey(), sortedRatingMap);
	        }
		}catch(Exception e) {
			logger.error("Getting Exception while getRatingWiseData, error = {}", ExceptionUtils.getStackTrace(e));
		}
		return newFeedbackRatingMap;
	}
	
	public Map<String, Map<String, Long>> getDateWiseFeedBackCount(Date fromDate, Date toDate, String  level, Integer geographyId) {
		Map<String, Map<String, Long>> feedbackMap = new HashMap<>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(ForesightConstants.DATE_FORMAT_yyyy_MM_dd);
			List<ConsumerFeedbackWrapper> feedbackCountListData = iConsumerFeedbackDao.getDateWiseFeedBackCount(fromDate, toDate,  level, geographyId);
			logger.info("getDateWiseFeedBackCount feedbackCountListData {}" , feedbackCountListData.size());
			
			for (ConsumerFeedbackWrapper consumerFeedbackWrapper : feedbackCountListData) {
				Map<String, Long> dataMap = new HashMap<>();
				if(feedbackMap.get(consumerFeedbackWrapper.getFeedbackType()) != null){
					dataMap = feedbackMap.get(consumerFeedbackWrapper.getFeedbackType());
				}
				dataMap.put(sdf.format(consumerFeedbackWrapper.getDate()), consumerFeedbackWrapper.getCount());
				feedbackMap.put(consumerFeedbackWrapper.getFeedbackType(), dataMap);
			}
			
			logger.info("getDateWiseFeedBackCount feedbackMap is {}", feedbackMap);
			return feedbackMap;
		} catch (Exception e) {
			logger.error("Getting Exception while getDateWiseFeedBackCount, error = {}", ExceptionUtils.getStackTrace(e));
			return feedbackMap;
		}
	}
	
	@Override
	public Map<String, Map<String, Object>> getDateWiseTestArea(String type, Date fromDate, Date toDate, String  level, Integer geographyId) {
		try {
			Map<String, Map<String, Long>> testAreaFeedBackMap = new HashMap<>();
			SimpleDateFormat sdf = new SimpleDateFormat(ForesightConstants.DATE_FORMAT_yyyy_MM_dd);
			List<ConsumerFeedbackWrapper> testAreaList = iConsumerFeedbackDao.getDateWiseTestArea(type, fromDate, toDate, level, geographyId);	
			logger.info("getDateWiseTestArea testAreaList {}" , testAreaList.size());
			
			for (ConsumerFeedbackWrapper consumerFeedbackWrapper : testAreaList) {
				if(consumerFeedbackWrapper.getDate().getTime() >= fromDate.getTime() && consumerFeedbackWrapper.getDate().getTime() <= toDate.getTime()) {
					Map<String, Long> testAreaMap = new HashMap<>();
					if(testAreaFeedBackMap.get(consumerFeedbackWrapper.getTestArea()) != null) {
						testAreaMap = testAreaFeedBackMap.get(consumerFeedbackWrapper.getTestArea());
					}
					testAreaMap.put(sdf.format(consumerFeedbackWrapper.getDate()), consumerFeedbackWrapper.getCount());
					testAreaFeedBackMap.put(consumerFeedbackWrapper.getTestArea(), testAreaMap);
				}	
			}
			logger.info("after testAreaFeedBackMap {}" , testAreaFeedBackMap);
   
			Map<String, Map<String, Object>> newTestAreaFeedbackMap = getDateWiseTrendMap(fromDate, toDate, testAreaFeedBackMap);
			logger.info("after getDateWiseTrendMap {}" , newTestAreaFeedbackMap);
			return newTestAreaFeedbackMap;
		} catch (Exception e) {
			logger.error("Getting Exception while getDateWiseTestArea, error = {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}
	
	@Override
	public Map<String, Map<String, Object>> getDateWiseFeedback(String type, Date fromDate, Date toDate, String  level, Integer geographyId) {
		try {
			List<ConsumerFeedbackWrapper> feedBackTrendList = iConsumerFeedbackDao.getDateWiseFeedback(type, fromDate, toDate, level, geographyId);
			Map<String, Map<String, Long>> feedBackTrendMap =  getDataCvgWiseTrendMap(fromDate, toDate, feedBackTrendList);
			logger.info("after getDataCvgWiseTrendMap {}" ,  feedBackTrendMap);
		
			return getDateWiseTrendMap(fromDate, toDate, feedBackTrendMap);
		} catch (Exception e) {
			logger.error("Getting Exception while getDateWiseFeedBack, error = {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}
	
	public Map<String, Map<String, Long>> getDataCvgWiseTrendMap(Date fromDate, Date toDate, List<ConsumerFeedbackWrapper> feedBackTrendList){
		Map<String, Map<String, Long>>  feedBackTrendMap = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat(ForesightConstants.DATE_FORMAT_yyyy_MM_dd);
		try {
			for (ConsumerFeedbackWrapper consumerFeedbackWrapper : feedBackTrendList) {
				if(consumerFeedbackWrapper.getDate().getTime() >= fromDate.getTime() && consumerFeedbackWrapper.getDate().getTime() <= toDate.getTime()) {
					Map<String, Long> voiceRatingMap = new HashMap<>();
					Map<String, Long> dataRatingMap = new HashMap<>();
					Map<String, Long> cvgRatingMap = new HashMap<>();
					if(feedBackTrendMap.get(ConsumerFeedbackConstant.VOICE) != null) {
						voiceRatingMap = feedBackTrendMap.get(ConsumerFeedbackConstant.VOICE);
					}
					if(feedBackTrendMap.get(ConsumerFeedbackConstant.DATA) != null) {
						dataRatingMap = feedBackTrendMap.get(ConsumerFeedbackConstant.DATA);
					}
					if(feedBackTrendMap.get(ConsumerFeedbackConstant.COVERAGE) != null) {
						cvgRatingMap = feedBackTrendMap.get(ConsumerFeedbackConstant.COVERAGE);
					}
					
					voiceRatingMap.put(sdf.format(consumerFeedbackWrapper.getDate()), consumerFeedbackWrapper.getStarRatingCount());
					dataRatingMap.put(sdf.format(consumerFeedbackWrapper.getDate()), consumerFeedbackWrapper.getStarRatingDataCount());
					cvgRatingMap.put(sdf.format(consumerFeedbackWrapper.getDate()), consumerFeedbackWrapper.getStarRatingCvgCount());
					
					feedBackTrendMap.put(ConsumerFeedbackConstant.VOICE, voiceRatingMap);
					feedBackTrendMap.put(ConsumerFeedbackConstant.DATA, dataRatingMap);
					feedBackTrendMap.put(ConsumerFeedbackConstant.COVERAGE, cvgRatingMap);
				}	
			}
			return feedBackTrendMap;
		}catch(Exception e) {
			logger.error("Getting Exception while getDataCvgWiseTrendMap, error = {}", ExceptionUtils.getStackTrace(e));	
			return feedBackTrendMap;
		}
	}
	
	public Map<String, Map<String, Object>> getDateWiseTrendMap(Date fromDate, Date toDate,Map<String, Map<String, Long>> feedBackTrendMap){
		Map<String, Map<String, Object>>  newFeedbackTrendMap = new HashMap<>();
		try {
			List<String> datelist = DateUtil.getDateRange(fromDate, toDate);
	        Iterator<Map.Entry<String, Map<String, Long>>> itr = feedBackTrendMap.entrySet().iterator(); 
	        while(itr.hasNext()){
	        	Map.Entry<String, Map<String, Long>> entry = itr.next();
	            Map<String, Long> ratingCount = feedBackTrendMap.get(entry.getKey());
				for (String dateString : datelist) {
					if(!ratingCount.containsKey(dateString)) {
						ratingCount.put(dateString, null);
					}
				}
				
				SortedMap<String, Long> sortedMap = new TreeMap<>(ratingCount);
				sortedMap.putAll(ratingCount);
		     
				Map<String, Object> dateKeyMap = new HashMap<>();
				dateKeyMap.put(ConsumerFeedbackConstant.DATA, sortedMap);
				newFeedbackTrendMap.put(entry.getKey(), dateKeyMap);
	        }
	        logger.info("In getDateWiseTrendMap {}" ,newFeedbackTrendMap);
	        return newFeedbackTrendMap;
		}catch(Exception e) {
			logger.error("Getting Exception while getDateWiseTrendMap, error = {}", ExceptionUtils.getStackTrace(e));
			return newFeedbackTrendMap;
		}
	}
	
	@Override
	public Map<String, Map<String, Map<String, Long>>> getVoiceDataCvgFeedback(Date fromDate, Date toDate, String  level, Integer geographyId) {
		try {
			Map<String, Map<String, Map<String, Long>>> feedbackMap = new HashMap<>();

			Map<String, Map<String, Long>> voiceDataMap =  getDateWiseVoiceCount(fromDate, toDate, level, geographyId);
	        feedbackMap.put(ConsumerFeedbackConstant.VOICE, voiceDataMap);
	        
	        logger.info("Voice : {}" ,feedbackMap);
	        
	        Map<String, Map<String, Long>> finalDataMap = getDateWiseDataCount(fromDate, toDate, level, geographyId);
	        feedbackMap.put(ConsumerFeedbackConstant.DATA, finalDataMap);
	        logger.info("Data {}" , feedbackMap);

	        Map<String, Map<String, Long>>  finalCvgMap = getDateWiseCoverageCount(fromDate, toDate, level, geographyId);
	        feedbackMap.put(ConsumerFeedbackConstant.COVERAGE, finalCvgMap);
	        
	        logger.info("Coverage {}" , feedbackMap.size());
			return feedbackMap;
		} catch (Exception e) {
			logger.error("Getting Exception while getDateWiseVoiceCount, error = {}", ExceptionUtils.getStackTrace(e));
			return new HashMap<>();
		}
	}
	
	public Map<String, Map<String, Long>> getDateWiseVoiceCount(Date fromDate, Date toDate, String level, Integer geographyId){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(ForesightConstants.DATE_FORMAT_yyyy_MM_dd);
			List<ConsumerFeedbackWrapper> voiceCountList = iConsumerFeedbackDao.getDateWiseVoiceCount(fromDate, toDate, level, geographyId);
			logger.info("voiceCountList {}" , voiceCountList.size());
	
			Map<String, Map<String, Long>> callMap = new HashMap<>();
			 
			 for (ConsumerFeedbackWrapper consumerFeedbackWrapper : voiceCountList) {
				 Map<String, Long> dateMap = new HashMap<>();
				 if(callMap.get(ConsumerFeedbackConstant.UNABLE_TO_MAKE_CALL) != null) {
					 dateMap = callMap.get(ConsumerFeedbackConstant.UNABLE_TO_MAKE_CALL);
				 } 
				 dateMap.put(sdf.format(consumerFeedbackWrapper.getDate()), consumerFeedbackWrapper.getUnableToMakeCallCount());
				 callMap.put(ConsumerFeedbackConstant.UNABLE_TO_MAKE_CALL, dateMap);
				 
				 dateMap = new HashMap<>();
				 if(callMap.get(ConsumerFeedbackConstant.CALL_DROP) != null) {
					 dateMap = callMap.get(ConsumerFeedbackConstant.CALL_DROP);
				 } 
				 dateMap.put(sdf.format(consumerFeedbackWrapper.getDate()), consumerFeedbackWrapper.getCallDropCount());
				 callMap.put(ConsumerFeedbackConstant.CALL_DROP, dateMap);
				 
				 dateMap = new HashMap<>();
				 if(callMap.get(ConsumerFeedbackConstant.POOR_AUDIO) != null) {
					 dateMap = callMap.get(ConsumerFeedbackConstant.POOR_AUDIO);
				 }
				 dateMap.put(sdf.format(consumerFeedbackWrapper.getDate()), consumerFeedbackWrapper.getPoorAudioCallCount());
				 callMap.put(ConsumerFeedbackConstant.POOR_AUDIO, dateMap);
				 
				 dateMap = new HashMap<>();
				 if(callMap.get(ConsumerFeedbackConstant.MUTE) != null) {
					 dateMap = callMap.get(ConsumerFeedbackConstant.MUTE);
				 }
				 dateMap.put(sdf.format(consumerFeedbackWrapper.getDate()), consumerFeedbackWrapper.getMuteCallCount());
				 callMap.put(ConsumerFeedbackConstant.MUTE, dateMap);
				 
				 dateMap = new HashMap<>();
				 if(callMap.get(ConsumerFeedbackConstant.ONE_WAY_AUDIO) != null) {
					 dateMap = callMap.get(ConsumerFeedbackConstant.ONE_WAY_AUDIO);
				 }
				 dateMap.put(sdf.format(consumerFeedbackWrapper.getDate()), consumerFeedbackWrapper.getOneWayAudioCount());
				 callMap.put(ConsumerFeedbackConstant.ONE_WAY_AUDIO, dateMap);
			}
			 
			return getDateRangeWiseData(fromDate, toDate, callMap);
		}catch (Exception e) {
			logger.error("Getting Exception while getDateWiseVoiceCount, error = {}", ExceptionUtils.getStackTrace(e));
			return new HashMap<>();
		}
	}
	
	public Map<String, Map<String, Long>> getDateWiseDataCount(Date fromDate, Date toDate, String level, Integer geographyId) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(ForesightConstants.DATE_FORMAT_yyyy_MM_dd);
			List<ConsumerFeedbackWrapper> dataCountList = iConsumerFeedbackDao.getDateWiseDataCount(fromDate, toDate, level, geographyId);
	        
			Map<String, Map<String, Long>> dataMap = new HashMap<>();
			
			for (ConsumerFeedbackWrapper consumerFeedbackWrapper : dataCountList) {
				 Map<String, Long> dateMap = new HashMap<>();
				 if(dataMap.get(ConsumerFeedbackConstant.DATA_SLOW_SPEED) != null) {
					 dateMap = dataMap.get(ConsumerFeedbackConstant.DATA_SLOW_SPEED);
				 } 
				 dateMap.put(sdf.format(consumerFeedbackWrapper.getDate()), consumerFeedbackWrapper.getDataSlowSpeedCount());
				 dataMap.put(ConsumerFeedbackConstant.DATA_SLOW_SPEED, dateMap);
				 
				 dateMap = new HashMap<>();
				 if(dataMap.get(ConsumerFeedbackConstant.DATA_UNABLE_TO_CONNECT) != null) {
					 dateMap = dataMap.get(ConsumerFeedbackConstant.DATA_UNABLE_TO_CONNECT);
				 } 
				 dateMap.put(sdf.format(consumerFeedbackWrapper.getDate()), consumerFeedbackWrapper.getDataUnAvailCount());
				 dataMap.put(ConsumerFeedbackConstant.DATA_UNABLE_TO_CONNECT, dateMap);
				 
			}
			
			 return getDateRangeWiseData(fromDate, toDate, dataMap);
		} catch (Exception e) {
			logger.error("Getting Exception while getDateWiseDataCount, error = {}", ExceptionUtils.getStackTrace(e));
			return new HashMap<>();
		}
	}
	
	public Map<String, Map<String, Long>> getDateWiseCoverageCount(Date fromDate, Date toDate,String level,Integer geographyId) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(ForesightConstants.DATE_FORMAT_yyyy_MM_dd);
	        List<ConsumerFeedbackWrapper> coverageCountList = iConsumerFeedbackDao.getDateWiseCoverageCount(fromDate, toDate, level, geographyId);
	        logger.info("coverageCountList {}" , coverageCountList.size());
			Map<String, Map<String, Long>> cvgMap = new HashMap<>();
			
			for (ConsumerFeedbackWrapper consumerFeedbackWrapper : coverageCountList) {
				 Map<String, Long> dateMap = new HashMap<>();
				 if(cvgMap.get(ConsumerFeedbackConstant.POOR_CVG_INDOORE) != null) {
					 dateMap = cvgMap.get(ConsumerFeedbackConstant.POOR_CVG_INDOORE);
				 }
				 dateMap.put(sdf.format(consumerFeedbackWrapper.getDate()), consumerFeedbackWrapper.getPoorCvgIndoorCount());
				 cvgMap.put(ConsumerFeedbackConstant.POOR_CVG_INDOORE, dateMap);
				 dateMap = new HashMap<>();
				 if(cvgMap.get(ConsumerFeedbackConstant.POOR_CVG_OUTDOOR) != null) {
					 dateMap = cvgMap.get(ConsumerFeedbackConstant.POOR_CVG_OUTDOOR);
				 }
				 dateMap.put(sdf.format(consumerFeedbackWrapper.getDate()), consumerFeedbackWrapper.getPoorCvgOutdoorCount());
				 cvgMap.put(ConsumerFeedbackConstant.POOR_CVG_OUTDOOR, dateMap);
			}
			
			logger.info("cvgMap is {}" , cvgMap);
			return getDateRangeWiseData(fromDate, toDate, cvgMap);
		} catch (Exception e) {
			logger.error("Getting Exception while getDateWiseCoverageCount, error = {}", ExceptionUtils.getStackTrace(e));
			return new HashMap<>();
		}
	}
	
	public Map<String, Map<String, Long>> getDateRangeWiseData(Date fromDate, Date toDate,Map<String, Map<String, Long>> mapData){
		try {
			List<String> datelist = DateUtil.getDateRange( fromDate, toDate);
			Map<String, Map<String, Long>> callMap = new HashMap<>();
	        Iterator<Map.Entry<String, Map<String, Long>>> itr = mapData.entrySet().iterator();
	         
	        while(itr.hasNext()){
	        	Map.Entry<String, Map<String, Long>> entry = itr.next();
	            Map<String, Long> configDataMap = mapData.get(entry.getKey());
				for (String dateString : datelist) {
					if(configDataMap.get(dateString) == null || configDataMap.get(dateString) == 0) {
						configDataMap.put(dateString, null);
					}
				}

				SortedMap<String, Long> sortedMap = new TreeMap<>(configDataMap);
				sortedMap.putAll(configDataMap);
				
				callMap.put(entry.getKey(), sortedMap);
	        }
	        logger.info("In getDateRangeWiseData {}" , callMap);
	        return callMap;
		}catch(Exception e) {
			logger.error("Getting Exception while getDateRangeWiseData, error = {}", ExceptionUtils.getStackTrace(e));
			return new HashMap<>(); 
		}
	}
	
	@Override
	public Map<String, List<Map<String, Object>>> getTopFeedbackLocations(Date fromDate, Date toDate, Integer lLimit, Integer uLimit, String  level, Integer geographyId) {
		try {
			Map<String, List<Map<String, Object>>> locationMap = new HashMap<>();
			
			List<Map<String, Object>> topLocationMapList = new ArrayList<>();
			
			List<ConsumerFeedbackWrapper> topLocationList = iConsumerFeedbackDao.getTopFeedbackLocations(fromDate, toDate, lLimit, uLimit, level, geographyId);
			for (ConsumerFeedbackWrapper consumerFeedbackWrapper : topLocationList) {
				Map<String, Object> topLocationMap = new HashMap<>();
				topLocationMap.put(ConsumerFeedbackConstant.COUNT, consumerFeedbackWrapper.getCount());
				topLocationMap.put(ConsumerFeedbackConstant.EVENT_COUNT, consumerFeedbackWrapper.getEventFeedbackCount());
				topLocationMap.put(ConsumerFeedbackConstant.CAMPAIGN_COUNT, consumerFeedbackWrapper.getCampaignFeedbackCount());
				topLocationMap.put(ConsumerFeedbackConstant.GEOGRAPHY, consumerFeedbackWrapper.getGeographyName());
				topLocationMap.put(ConsumerFeedbackConstant.LATITUDE, consumerFeedbackWrapper.getLatitude());
				topLocationMap.put(ConsumerFeedbackConstant.LONGITUDE, consumerFeedbackWrapper.getLongitude());	
				topLocationMapList.add(topLocationMap);
			}
			locationMap.put(ConsumerFeedbackConstant.ALL, topLocationMapList);
			
			topLocationList = iConsumerFeedbackDao.getTopLocationsWithType(fromDate, toDate, lLimit, uLimit, level, geographyId);
			List<Map<String, Object>> eventLocationMapList = new ArrayList<>();
			List<Map<String, Object>> campaignLocationMapList = new ArrayList<>();
			for (ConsumerFeedbackWrapper consumerFeedbackWrapper : topLocationList) {
				Map<String, Object> topLocationMap = new HashMap<>();
				topLocationMap.put(ConsumerFeedbackConstant.COUNT, consumerFeedbackWrapper.getCount());
				topLocationMap.put(ConsumerFeedbackConstant.GEOGRAPHY, consumerFeedbackWrapper.getGeographyName());
				topLocationMap.put(ConsumerFeedbackConstant.LATITUDE, consumerFeedbackWrapper.getLatitude());
				topLocationMap.put(ConsumerFeedbackConstant.LONGITUDE, consumerFeedbackWrapper.getLongitude());
				if(consumerFeedbackWrapper.getFeedbackType().equals(ConsumerFeedbackConstant.EVENT_FEEDBACK)) {
					eventLocationMapList.add(topLocationMap);
				} else if(consumerFeedbackWrapper.getFeedbackType().equals(ConsumerFeedbackConstant.CAMPAIGN_FEEDBACK)){
					campaignLocationMapList.add(topLocationMap);
				}
			}
			locationMap.put(ConsumerFeedbackConstant.EVENT_FEEDBACK, eventLocationMapList);
			locationMap.put(ConsumerFeedbackConstant.CAMPAIGN_FEEDBACK, campaignLocationMapList);
			
			return locationMap;
		} catch (Exception e) {
			logger.error("Getting Exception while getTopFeedbackLocations, error = {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	@Override
	public Object getLayerData(Integer zoom, Double nELat, Double nELng, Double sWLat, Double sWLng, String fromDate,
			String toDate,String testArea, String feedbackType, String band, String kpi, Integer fromStarRating,Integer toStarRating
			, String searchType) {
		
		List<Map<String,Object>> response = new ArrayList<>();
		List<ConsumerFeedback> feedbackRawList = iConsumerFeedbackDao.getFeedBackLayerData(fromDate, toDate,testArea,feedbackType
				,band,kpi,fromStarRating,toStarRating,searchType);
		List<ConsumerFeedback> filterFeedback = filterRecordByViewPort(nELat, nELng, sWLat, sWLng, feedbackRawList);
		logger.info("Size after view port filter {}", filterFeedback.size());
		Double countryLat =getCountryLat();
		Double countryLng =getCountryLng();
		Integer gridSize = getGridSize(zoom);
		logger.info("Found the country Lat {} , Country Lng {} and gridSize {}",countryLat,countryLng,gridSize);
		Map<String, List<ConsumerFeedback>> map = findGridWiseResponse(filterFeedback, countryLat, countryLng,
				gridSize);
		populateResult(zoom, response, map);
		logger.info("Final Response Size {}",response.size());
		return response;
	}

	private void populateResult(Integer zoom, List<Map<String, Object>> response,
			Map<String, List<ConsumerFeedback>> map) {
		for(Entry<String, List<ConsumerFeedback>> entry:map.entrySet())
		{
			Map<String,Object> gridResponse = new HashMap<>();
			String[] centroid = entry.getKey().split(",");
			int size = entry.getValue().stream().filter(x-> Utils.hasValue(x.getDeviceId())).
					map(ConsumerFeedback::getDeviceId).collect(Collectors.toSet()).size();
			gridResponse.put(ConsumerFeedbackConstant.COUNT, size);
			gridResponse.put(ConsumerFeedbackConstant.LATITUDE_KEY, centroid[0]);
			gridResponse.put(ConsumerFeedbackConstant.LONGITUDE_KEY, centroid[1]);
			if(zoom > 16)
			{
				Map<String, List<ConsumerFeedback>> deviceIdWiseMap = entry.getValue().stream().filter(x-> Utils.hasValue(x.getDeviceId())).collect(Collectors.groupingBy(ConsumerFeedback :: getDeviceId,
						Collectors.toList()));
				List<ConsumerFeedback> data = new ArrayList<>();
				for(Entry<String, List<ConsumerFeedback>> deviceData:deviceIdWiseMap.entrySet())
				{
					data.add(findLatestRecord(deviceData.getValue()));
				}
			gridResponse.put(ConsumerFeedbackConstant.DATA, data);
			}
			response.add(gridResponse);
		}
	}

	
	
	private ConsumerFeedback findLatestRecord(List<ConsumerFeedback> value) {
		Map<String, ConsumerFeedback> response = new HashMap<>();
		for (ConsumerFeedback feedback : value) {
			if (!response.isEmpty()) {
				Long oldTime = response.get("key").getFeedbacktime().getTime();
				Long newTime = feedback.getFeedbacktime().getTime();
				if (newTime > oldTime) {
					response.put("key", feedback);
				}
			} else {
				response.put("key", feedback);
			}
		}
		return response.get("key");
	}

	private Map<String, List<ConsumerFeedback>> findGridWiseResponse(List<ConsumerFeedback> filterFeedback,
			Double countryLat, Double countryLng, Integer gridSize) {
		Map<String, List<ConsumerFeedback>> map = new HashMap<>();
		for (ConsumerFeedback consumerFeedback : filterFeedback) {
			LatLng grid;
			grid = new DegreeGrid(gridSize, new LatLng(countryLat, countryLng))
					.getGrid(new LatLng(consumerFeedback.getLatitude(), consumerFeedback.getLongitude()));
			String gridId = grid.getLatitude() + ForesightConstants.COMMA + grid.getLongitude();
			if (map.containsKey(gridId)) {
				List<ConsumerFeedback> oldList = map.get(gridId);
				oldList.add(consumerFeedback);
				map.put(gridId, oldList);
			} else {
				List<ConsumerFeedback> newList = new ArrayList<>();
				newList.add(consumerFeedback);
				map.put(gridId, newList);
			}
		}
		return map;
	}

	private List<ConsumerFeedback> filterRecordByViewPort(Double nELat, Double nELng, Double sWLat, Double sWLng,
			List<ConsumerFeedback> feedbackRawList) {
	return feedbackRawList.stream()
				.filter(x -> x.getLatitude() != null && x.getLongitude() != null
						&& isValidRecord(nELat, nELng, sWLat, sWLng, x.getLatitude(), x.getLongitude()))
				.collect(Collectors.toList());
	}
	
	private Double getCountryLat() {
		try {
			return Double.parseDouble(SystemConfigurationUtils.systemConfMap.get("CountryLatitude"));
		} catch (Exception e) {
			return Double.parseDouble(iSystemConfigurationDao.getValueByName("CountryLatitude"));
		}
	}

	private Double getCountryLng() {
		try {
			return Double.parseDouble(SystemConfigurationUtils.systemConfMap.get("CountryLongitude"));
		} catch (Exception e) {
			return Double.parseDouble(iSystemConfigurationDao.getValueByName("CountryLongitude"));
		}
	}
	
	private Integer getGridSize(Integer zoom) {
		return Integer.parseInt(SystemConfigurationUtils.systemConfMap.get("ZOOM"+zoom));
	}

	private boolean isValidRecord(Double nELat, Double nELng, Double sWLat, Double sWLng, Double latitude,
			Double longitude) {
		Boolean isDataAddIntoList = true;
		if (latitude != null && latitude > nELat || latitude < sWLat) {

			
				isDataAddIntoList = false;
			
		}
		if (longitude != null && longitude > nELng || longitude < sWLng) {
			
				isDataAddIntoList = false;
			
		}
		return isDataAddIntoList;
	}
	
	/** API for ECare Feedback Data */
	@Override
	public List<Map<String, Object>> getLayerDataForECare(Integer zoom, Double nELat, Double nELng, Double sWLat, Double sWLng, String fromDate,
			String toDate, String band, String kpi, String searchType, String locationType, String orderBy, String orderType) {
		logger.info(
				"Going to get layer data for ECare  for zoom :{}, NELat : {}, NELng: {}, SWLat : {}, SWLng : {}, fromDate : {}, toDate : {}, band : {}, kpi : {}, searchType : {}, locationType :{},orderBy :{},orderType :{} ",
				zoom, nELat, nELng, sWLat, sWLng, fromDate, toDate, band, kpi, searchType, locationType, orderBy, orderType);
		try {
			if (!Utils.isValidString(fromDate) && !Utils.isValidString(toDate)) {
				Date date = new Date();
				toDate = getDefaultDate(date);
				fromDate = getDefaultDate(getPeriodForECare(date));
				logger.info("Defaul Ecare Period time toDate : {} and fromDate : {}", toDate, fromDate);
			}

			List<ConsumerFeedback> feedbackRawList = iConsumerFeedbackDao.getLayerDataForECare(locationType, searchType, kpi,
					Utils.parseStringToDate(fromDate, ForesightConstants.DATE_FORMAT_DDMMYY),
					Utils.parseStringToDate(toDate, ForesightConstants.DATE_FORMAT_DDMMYY), orderBy, orderType);

			Double countryLat = getCountryLat();
			Double countryLng = getCountryLng();
			Integer gridSize = getGridSize(zoom);
			logger.info("ECare Grid Found the country Lat {} , Country Lng {} and gridSize {}", countryLat, countryLng, gridSize);

			return populateResultForECare(zoom,
					findGridWiseResponse(filterRecordByViewPort(nELat, nELng, sWLat, sWLng, feedbackRawList), countryLat, countryLng, gridSize));

		} catch (Exception e) {
			logger.error("Error in getting ECare layer data Exception : {}", Utils.getStackTrace(e));
			return new ArrayList<>();
		}
	}

	private String getDefaultDate(Date date) {
		String df = ForesightConstants.DATE_FORMAT_DDMMYY;
		SimpleDateFormat sfd = new SimpleDateFormat(df);
		return sfd.format(date);
	}

	private Date getPeriodForECare(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, ConfigUtils.getInteger(ConsumerFeedbackConstant.PREVIOUS_DAYS_DATA_FOR_ECARE, -7));
		return cal.getTime();
	}

	private List<Map<String, Object>> populateResultForECare(Integer zoom, Map<String, List<ConsumerFeedback>> gridWiseDataMap) {
		List<Map<String, Object>> finalGridWiseData = new ArrayList<>();
		for (Entry<String, List<ConsumerFeedback>> entry : gridWiseDataMap.entrySet()) {
			Map<String, Object> gridData = new HashMap<>();
			String[] centroid = entry.getKey().split(ForesightConstants.COMMA);

			gridData.put(ConsumerFeedbackConstant.COUNT, entry.getValue().size());
			gridData.put(ConsumerFeedbackConstant.LATITUDE_KEY, centroid[0]);
			gridData.put(ConsumerFeedbackConstant.LONGITUDE_KEY, centroid[1]);

			if (zoom > ConfigUtils.getInteger(ConsumerFeedbackConstant.ECARE_ZOOM_LEVEL_FOR_SHOWING_DATA, 16)) {
				Map<String, List<ConsumerFeedback>> deviceIdWiseMap = entry.getValue().stream().filter(x -> Utils.hasValue(x.getDeviceId()))
						.collect(Collectors.groupingBy(ConsumerFeedback::getDeviceId, Collectors.toList()));
				gridData.put(ConsumerFeedbackConstant.DATA, populateECareData(deviceIdWiseMap));
			}
			finalGridWiseData.add(gridData);
		}
		logger.info("Final grid wise Data : {}", finalGridWiseData.size());
		return finalGridWiseData;
	}

	private List<ConsumerFeedback> populateECareData(Map<String, List<ConsumerFeedback>> gridWiseDataMap) {
		List<ConsumerFeedback> consumerFeedbackList = new ArrayList<>();
		try {
			gridWiseDataMap.entrySet().forEach(consumerData -> consumerFeedbackList.addAll(consumerData.getValue()));
			consumerFeedbackList.forEach(feedback -> populateECareFinalData(feedback));
		} catch (Exception e) {
			logger.error("Error in populating ECare Data Exception :  {}", Utils.getStackTrace(e));
		}
		return consumerFeedbackList;
	}

	private void populateECareFinalData(ConsumerFeedback feedback) {
		try {
			Map<String, Object> consumerECareData = convertStringToMap(feedback.getData());
			Map<String, Boolean> booleanMap = getBooleanKeyDataForECare(consumerECareData);
			
			populateLocationTypeDataForECare(consumerECareData, booleanMap,
					convertStringToMap(ConfigUtils.getString(ConsumerFeedbackConstant.ECARE_LOCATION_TYPE_DATA)));
			
			populateDataReasonForECare(consumerECareData, booleanMap,
					convertStringToMap(ConfigUtils.getString(ConsumerFeedbackConstant.ECARE_DATA_FEEDBACK_DATA)));
			
			populateVoiceReasonForECare(consumerECareData, booleanMap,
					convertStringToMap(ConfigUtils.getString(ConsumerFeedbackConstant.ECARE_VOICE_FEEDBACK_DATA)));
			
			feedback.setNvProfileData(null);
			feedback.setData(new Gson().toJson(consumerECareData));
		} catch (Exception e) {
			logger.error("Error in populating final data for Ecare layer Exception : {}", Utils.getStackTrace(e));
		}
	}

	private void populateVoiceReasonForECare(Map<String, Object> consumerECareData, Map<String, Boolean> booleanMap,
			Map<String, Object> voiceFeedbackConfigData) {
		if (isContainsKeyForECare(booleanMap, ConsumerFeedbackConstant.ECARE_IS_VOICE_FEEDBACK_KEY)) {
			populateReasonsString(booleanMap, voiceFeedbackConfigData, consumerECareData);
		}
	}

	private void populateDataReasonForECare(Map<String, Object> consumerECareData, Map<String, Boolean> booleanMap,
			Map<String, Object> dataFeedbackConfigData) {
		if (isContainsKeyForECare(booleanMap, ConsumerFeedbackConstant.ECARE_IS_DATA_FEEDBACK_KEY)) {
			populateReasonsString(booleanMap, dataFeedbackConfigData, consumerECareData);
		}
	}

	private boolean isContainsKeyForECare(Map<String, Boolean> booleanMap, String key) {
		return booleanMap.containsKey(key) && booleanMap.get(key).equals(true);
	}

	private void populateReasonsString(Map<String, Boolean> booleanMap, Map<String, Object> feedbackConfigData,
			Map<String, Object> consumerECareData) {
		List<String> reasonList = new ArrayList<>();
		booleanMap.entrySet().forEach(booleanDataMap -> {
			if (feedbackConfigData.containsKey(booleanDataMap.getKey()) && booleanDataMap.getValue().equals(true)) {
				reasonList.add((String) feedbackConfigData.get(booleanDataMap.getKey()));
			}
		});
		consumerECareData.put(ConsumerFeedbackConstant.ECARE_REASON_KEY,
				org.apache.commons.lang3.StringUtils.join(reasonList, ForesightConstants.COMMA + ForesightConstants.SPACE));
	}

	private void populateLocationTypeDataForECare(Map<String, Object> consumerECareData, Map<String, Boolean> booleanMap,
			Map<String, Object> locationTypeConfigData) {
		for (Entry<String, Boolean> booleanDataMap : booleanMap.entrySet()) {
			if (locationTypeConfigData.containsKey(booleanDataMap.getKey()) && booleanDataMap.getValue().equals(true)) {
				consumerECareData.put(ConsumerFeedbackConstant.ECARE_LOCATION_TYPE_KEY, locationTypeConfigData.get(booleanDataMap.getKey()));
			}
		}
	}

	private Map<String, Boolean> getBooleanKeyDataForECare(Map<String, Object> ecareData) {
		Map<String, Boolean> booleanMap = new HashMap<>();
		ecareData.forEach((key, value) -> {
			if (value.equals(true) || value.equals(false)) {
				booleanMap.put(key, Boolean.valueOf(value.toString()));
			}
		});
		return booleanMap;
	}

	@SuppressWarnings("serial")
	private Map<String, Object> convertStringToMap(String string) {
		if (Utils.isValidString(string)) {
			return new Gson().fromJson(string, new TypeToken<Map<String, Object>>() {
			}.getType());
		} else {
			return new HashMap<>();
		}
	}
	
	@SuppressWarnings("serial")
	private Map<String, String> convertJsonToMap(String string) {
		if (Utils.isValidString(string)) {
			return new Gson().fromJson(string, new TypeToken<Map<String, String>>() {
			}.getType());
		} else {
			return new HashMap<>();
		}
	}
	

	@Override
	public Map<String, Object> getDashboardDataForECare(Integer geographyId, String level, String date,
			String feedbackType) {
		List<Long> moduleCode = getModuleCodeMap(feedbackType);
		List<ConsumerFeedback> reponse = iConsumerFeedbackDao.getEcareDashboardData(moduleCode,geographyId,level,date);
		return populateResponse(reponse);
	}

	private Map<String, Object> populateResponse(List<ConsumerFeedback> reponse) {

		Map<String,Object> resultResponse = new HashMap<>();
		Map<String,Long> voiceIndoor = new HashMap<>();
		Map<String,Long> voiceOutdoor = new HashMap<>();
		Map<String,Long> voiceMoving = new HashMap<>();
		
		Map<String,Long> dataIndoor = new HashMap<>();
		Map<String,Long> dataOutdoor = new HashMap<>();
		Map<String,Long> dataMoving = new HashMap<>();
		
		List<Map<String, Object>> responseList = new Gson().fromJson(
				reponse.stream().map(x -> x.getData()).collect(Collectors.toList()).toString(),
				new TypeToken<List<Map<String, Object>>>() {
				}.getType());
		
		logger.info("Found the response map size :: {}",responseList.size());
		
		populateVoiceData(voiceIndoor, responseList,ConsumerFeedbackConstant.LOCATION_TYPE_INDOOR_KEY);
		populateVoiceData(voiceOutdoor, responseList,ConsumerFeedbackConstant.LOCATION_TYPE_OUTDOOR_KEY);
		populateVoiceData(voiceMoving, responseList,ConsumerFeedbackConstant.LOCATION_TYPE_MOVING_KEY);
		populateDataRecords(dataIndoor, responseList,ConsumerFeedbackConstant.LOCATION_TYPE_INDOOR_KEY);
		populateDataRecords(dataOutdoor, responseList,ConsumerFeedbackConstant.LOCATION_TYPE_OUTDOOR_KEY);
		populateDataRecords(dataMoving, responseList,ConsumerFeedbackConstant.LOCATION_TYPE_MOVING_KEY);

		resultResponse.put(ConsumerFeedbackConstant.VOICE_INDOOR_RESPONSE_KEY, voiceIndoor);
		resultResponse.put(ConsumerFeedbackConstant.VOICE_OUTDOOR_RESPONSE_KEY, voiceOutdoor);
		resultResponse.put(ConsumerFeedbackConstant.VOICE_MOVING_RESPONSE_KEY, voiceMoving);
		resultResponse.put(ConsumerFeedbackConstant.DATA_INDOOR_RESPONSE_KEY, dataIndoor);
		resultResponse.put(ConsumerFeedbackConstant.DATA_OUTDOOR_RESPONSE_KEY, dataOutdoor);
		resultResponse.put(ConsumerFeedbackConstant.DATA_MOVING_RESPONSE_KEY, dataMoving);

		return resultResponse;
	}

	private void populateDataRecords(Map<String, Long> genericMap, List<Map<String, Object>> responseList,
			String locationType) {
		
		Long dataSlowConn = responseList.stream().filter(
				x -> x.get(ConsumerFeedbackConstant.DATA_NO_CONNECTION) != null 
					&& x.get(locationType) != null && 
					 x.get(locationType).toString().equalsIgnoreCase("true") 
						&& x.get(ConsumerFeedbackConstant.DATA_NO_CONNECTION).toString().equalsIgnoreCase("true"))
				.count();
		
		
		Long dataNoConn = responseList.stream().filter(
				x -> x.get(ConsumerFeedbackConstant.DATA_SLOW_CONNECTION) != null 
					&& x.get(locationType) != null && 
					 x.get(locationType).toString().equalsIgnoreCase("true") 
						&& x.get(ConsumerFeedbackConstant.DATA_SLOW_CONNECTION).toString().equalsIgnoreCase("true"))
				.count();
		
		genericMap.put("dataSlowConn", dataSlowConn);
		genericMap.put("dataNoConn", dataNoConn);
	}
	
	private void populateVoiceData(Map<String, Long> genericMap, List<Map<String, Object>> responseList, String locationType) {
	
		Long vCallMute = responseList.stream().filter(
				x -> x.get(ConsumerFeedbackConstant.VOICE_CALL_MUTE) != null 
					&& x.get(locationType) != null && 
					 x.get(locationType).toString().equalsIgnoreCase("true") 
						&& x.get(ConsumerFeedbackConstant.VOICE_CALL_MUTE).toString().equalsIgnoreCase("true"))
				.count();
		
		
		Long vCallFailed = responseList.stream().filter(
				x -> x.get(ConsumerFeedbackConstant.VOICE_CALL_FAILED) != null 
					&& x.get(locationType) != null && 
					 x.get(locationType).toString().equalsIgnoreCase("true") 
						&& x.get(ConsumerFeedbackConstant.VOICE_CALL_FAILED).toString().equalsIgnoreCase("true"))
				.count();
		
		
		Long vCallOneWay = responseList.stream().filter(
				x -> x.get(ConsumerFeedbackConstant.VOICE_CALL_ONE_WAY) != null 
					&& x.get(locationType) != null && 
					 x.get(locationType).toString().equalsIgnoreCase("true") 
						&& x.get(ConsumerFeedbackConstant.VOICE_CALL_ONE_WAY).toString().equalsIgnoreCase("true"))
				.count();
		
		
		Long vCallDegradation = responseList.stream().filter(
				x -> x.get(ConsumerFeedbackConstant.ECARE_VOICE_CALL_DEGRADATION_KEY) != null 
					&& x.get(locationType) != null && 
					 x.get(locationType).toString().equalsIgnoreCase("true") 
						&& x.get(ConsumerFeedbackConstant.ECARE_VOICE_CALL_DEGRADATION_KEY).toString().equalsIgnoreCase("true"))
				.count();
		

		Long vCallNoiceAvailable = responseList.stream().filter(
				x -> x.get(ConsumerFeedbackConstant.VOICE_CALL_NOT_AVAILABLE) != null 
					&& x.get(locationType) != null && 
					 x.get(locationType).toString().equalsIgnoreCase("true") 
						&& x.get(ConsumerFeedbackConstant.VOICE_CALL_NOT_AVAILABLE).toString().equalsIgnoreCase("true"))
				.count();
		
		genericMap.put("vCallMute", vCallMute);
		genericMap.put("vCallFailed", vCallFailed);
		genericMap.put("vCallOneWay", vCallOneWay);
		genericMap.put("vCallDegradation", vCallDegradation);
		genericMap.put("vCallNotAvailable", vCallNoiceAvailable);
		
	}

	private List<Long> getModuleCodeMap(String feedbackType) {
		List<Long> list = new ArrayList<>();
		Map<Long, String> map = new Gson().fromJson(
				SystemConfigurationUtils.systemConfMap.get(ConsumerFeedbackConstant.NV_DASHBOARD_APP_MAPPING),
				new TypeToken<Map<Long, String>>() {
				}.getType());
		
		logger.info("Found the App Code map {}",map);
		for (Entry<Long, String> entry : map.entrySet()) {
			if (entry.getValue().equalsIgnoreCase(feedbackType)) {
				list.add(entry.getKey());
			}
		}
		logger.info("Going to return Code for {}",list);
		return list;
	}
	
	/** ECare call of custom feedback */
	@Override
	public List<Object> getCustomFeedbackData(CustomFeedbackRequestWrapper requestWrapper) {
		List<Object> finalList = new ArrayList<>();
		try {
			logger.info("Getting data for : {}", requestWrapper != null ? new Gson().toJson(requestWrapper) : null);
			setDefaultDateFilter(requestWrapper);
			List<CustomFeedbackResponseWrapper> recordsFromDB = consumerFeedbackEcareDao.getCustomFeedbackData(requestWrapper);
			populateCustomFeedbackData(requestWrapper, finalList, recordsFromDB);
		} catch (Exception e) {
			logger.error("Error in custom feedback data Exception : {}", Utils.getStackTrace(e));
		}
		return finalList;
	}

	private void setDefaultDateFilter(CustomFeedbackRequestWrapper requestWrapper) {
		if (!Utils.isValidString(requestWrapper.getFromDate()) && !Utils.isValidString(requestWrapper.getToDate())) {
			Date date = new Date();
			requestWrapper.setToDate(getDefaultDate(date));
			requestWrapper.setFromDate(getDefaultDate(getPeriodForECare(date)));
			logger.info("Defaul Ecare Period time toDate : {} and fromDate : {}", requestWrapper.getToDate(), requestWrapper.getFromDate());
		}
	}

	private void populateCustomFeedbackData(CustomFeedbackRequestWrapper requestWrapper, List<Object> finalList,
			List<CustomFeedbackResponseWrapper> recordsFromDB) {
		if(Utils.hasValue(requestWrapper.getZoomLevel())) {
			logger.info("Going to get custom feedback record for zoom : {}", requestWrapper.getZoomLevel());
			if (checkZoomLevelRangeForData(requestWrapper, ConsumerFeedbackConstant.ZOOM_LEVELS_FOR_GRIDWISE_DATA)) {
				getDataForGridLogic(requestWrapper.getZoomLevel(), finalList, recordsFromDB);
			} else if (checkZoomLevelRangeForData(requestWrapper, ConsumerFeedbackConstant.ZOOM_LEVELS_FOR_MEDIANWISE_DATA)) {
				getDataForMediaLogic(requestWrapper.getZoomLevel(), finalList, recordsFromDB);
			} else if (checkZoomLevelRangeForData(requestWrapper, ConsumerFeedbackConstant.ZOOM_LEVEL_FOR_ALL_DATA)) {
				getDataForVisualizing(requestWrapper, finalList, recordsFromDB);
			}
		}
	}

	private boolean checkZoomLevelRangeForData(CustomFeedbackRequestWrapper requestWrapper, String zoomLevelReason) {
		return requestWrapper.getZoomLevel() >= ConfigUtils.getIntegerList(zoomLevelReason).get(0)
				&& requestWrapper.getZoomLevel() <= ConfigUtils.getIntegerList(zoomLevelReason).get(1);
	}

	private void getDataForGridLogic(Integer zoomLevel, List<Object> finalList, List<CustomFeedbackResponseWrapper> list) {
		list.forEach(wrapper -> {
			try {
				List<Object> dataList = new ArrayList<>();
				DegreeGrid degreeGrid = getDegreeGridRefByZoom(zoomLevel);
				populateGridWiseLatLng(wrapper.getGrid(), dataList, degreeGrid);
				dataList.add(wrapper.getCount());
				finalList.add(dataList);
			} catch (Exception e) {
				logger.error("Error in populating grid wise data Exception : {}", Utils.getStackTrace(e));
			}
		});
	}

	private void populateGridWiseLatLng(String grid, List<Object> dataList, DegreeGrid degreeGrid) {
		Corner cornerForTopLeftGrid = degreeGrid
				.getCornerForTopLeftGrid(new LatLng(Double.parseDouble(grid.split(ForesightConstants.UNDERSCORE)[0]),
						Double.parseDouble(grid.split(ForesightConstants.UNDERSCORE)[1])));

		dataList.add(cornerForTopLeftGrid.getTopLeftPosition().getLatitude());
		dataList.add(cornerForTopLeftGrid.getTopLeftPosition().getLongitude());
		dataList.add(cornerForTopLeftGrid.getBottomRightPosition().getLatitude());
		dataList.add(cornerForTopLeftGrid.getBottomRightPosition().getLongitude());
	}

	private void getDataForMediaLogic(Integer zoomLevel, List<Object> finalList, List<CustomFeedbackResponseWrapper> list) {
		try {
			Map<String, List<CustomFeedbackResponseWrapper>> gridWiseData = list.stream().filter(x -> Utils.hasValue(x.getRating()))
					.sorted(Comparator.comparingInt(CustomFeedbackResponseWrapper::getRating))
					.collect(Collectors.groupingBy(CustomFeedbackResponseWrapper::getGrid, Collectors.toList()));
			logger.info("total number of records after  Group by rate and grid : {}", gridWiseData.size());

			for (Map.Entry<String, List<CustomFeedbackResponseWrapper>> entry : gridWiseData.entrySet()) {
				LinkedList<Long> medianList = new LinkedList<>();
				finalList.add(setCustomFeedbackCountList(zoomLevel, entry, medianList, getTotalCountAndMedianList(entry, medianList)));
			}
			logger.info("size of Final List after median calculating: {}", finalList.size());
		} catch (Exception e) {
			logger.error("Error in populating median data for feedback Exception : {}", Utils.getStackTrace(e));
		}
	}

	private Map<String, List<CustomFeedbackResponseWrapper>> getGridWiseCustomFeedback(CustomFeedbackRequestWrapper requestWrapper,
			List<CustomFeedbackResponseWrapper> list) {
		Map<String, List<CustomFeedbackResponseWrapper>> map = new HashMap<>();

		for (CustomFeedbackResponseWrapper responseFeedback : list) {
			if(!Utils.isValidString(responseFeedback.getReason())) {
				responseFeedback.setReason(null);
			}
			
			LatLng grid;
			grid = getDegreeGridRefByZoom(requestWrapper.getZoomLevel())
					.getGrid(new LatLng(responseFeedback.getLatitude(), responseFeedback.getLongitude()));

			String gridId = grid.getLatitude() + ForesightConstants.COMMA + grid.getLongitude();
			if (map.containsKey(gridId)) {
				List<CustomFeedbackResponseWrapper> oldList = map.get(gridId);
				oldList.add(responseFeedback);
				map.put(gridId, oldList);
			} else {
				List<CustomFeedbackResponseWrapper> newList = new ArrayList<>();
				newList.add(responseFeedback);
				map.put(gridId, newList);
			}
		}
		return map;
	}

	private void getDataForVisualizing(CustomFeedbackRequestWrapper requestWrapper, List<Object> finalList,
			List<CustomFeedbackResponseWrapper> recordsFromDB) {
		Map<String, List<CustomFeedbackResponseWrapper>> gridWiseCustomFeedback = getGridWiseCustomFeedback(requestWrapper, recordsFromDB);
		for (Entry<String, List<CustomFeedbackResponseWrapper>> entry : gridWiseCustomFeedback.entrySet()) {
			populateDataForFinalResult(finalList, entry);
		}
	}

	private void populateDataForFinalResult(List<Object> finalList,
			Entry<String, List<CustomFeedbackResponseWrapper>> entry) {
		Map<String, Object> gridData = new HashMap<>();
		String[] centroid = entry.getKey().split(ForesightConstants.COMMA);
		gridData.put(ConsumerFeedbackConstant.COUNT, entry.getValue().size());
		gridData.put(ConsumerFeedbackConstant.LATITUDE_KEY, centroid[0]);
		gridData.put(ConsumerFeedbackConstant.LONGITUDE_KEY, centroid[1]);

		List<CustomFeedbackResponseWrapper> consumerFeedbackList = new ArrayList<>();
		consumerFeedbackList.addAll(entry.getValue());
		logger.info("consumerFeedbackList : {}", consumerFeedbackList);
		gridData.put(ConsumerFeedbackConstant.DATA, consumerFeedbackList);
		finalList.add(gridData);
		logger.info("Final grid wise Data : {}", finalList.size());
	}

	private Double getTotalCountAndMedianList(Map.Entry<String, List<CustomFeedbackResponseWrapper>> entry, LinkedList<Long> medianList) {
		Double totalCount = 0.0;
		for (CustomFeedbackResponseWrapper wrapper : entry.getValue()) {
			totalCount += wrapper.getCount();
			for (int i = 0; i < wrapper.getCount(); i++) {
				medianList.add(Long.valueOf(wrapper.getRating()));
			}
		}
		return totalCount;
	}

	private List<Object> setCustomFeedbackCountList(Integer zoomLevel, Map.Entry<String, List<CustomFeedbackResponseWrapper>> entry,
			LinkedList<Long> medianList, Double totalCount) {
		List<Object> newList = new ArrayList<>();
		populateGridWiseLatLng(entry.getKey(), newList, getDegreeGridRefByZoom(zoomLevel));
		newList.add(totalCount.longValue());
		newList.add(medianList.get(getColorValueByMedianLogic(totalCount) - 1));
		return newList;
	}

	private DegreeGrid getDegreeGridRefByZoom(Integer zoomLevel) {
		Double countryLat = getCountryLat();
		Double countryLng = getCountryLng();
		Integer length = getDistanceForGrid(zoomLevel);
		logger.info("Found the country Lat {} , Country Lng {} and gridSize {}", countryLat, countryLng, length);
		return new DegreeGrid(length, new LatLng(countryLat, countryLng));
	}

	private Integer getDistanceForGrid(Integer zoomLevel) {
		String zoomGrid = SystemConfigurationUtils.systemConfMap.get(ConsumerFeedbackConstant.CUSTOM_ZOOM_GRID);
		logger.info("convertStringToMap(zoomGrid) {}", convertStringToMap(zoomGrid));
		return ((int) (double) convertStringToMap(zoomGrid).get(ConsumerFeedbackConstant.ZOOM + zoomLevel));

	}

	private Integer getColorValueByMedianLogic(Double totalCount) {
		Integer colorPosition = 0;
		if (totalCount % 2 == 0) {
			colorPosition = (int) Math.floor(totalCount / 2);
		} else {
			colorPosition = (int) Math.ceil(totalCount / 2);
		}
		return colorPosition;
	}

	@Override
	@Transactional
	public String createCustomFeedback() {
		try {
			logger.info("Going to create custom feedback data");
			 List<ConsumerFeedback> findAll = iConsumerFeedbackDao.findAll();
			 for (ConsumerFeedback create : findAll) {
				String nvModule = create.getNvModule();
				if (nvModule != null
						&& Arrays.asList(ConsumerFeedbackConstant.ecareFeedbackType).contains(nvModule)) {
					setFeedbackGridData(create);
				} 
			}
			 return AppConstants.SUCCESS_JSON;
		} catch (DaoException e) {
			logger.error("Exception while creating custom feedback data {}",ExceptionUtils.getStackTrace(e));
		}
		return  AppConstants.FAILURE_JSON;
	}

}
