package com.inn.foresight.module.nv.feedback.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.module.nv.feedback.model.ConsumerFeedback;
import com.inn.foresight.module.nv.feedback.wrapper.CustomFeedbackRequestWrapper;
import com.inn.foresight.module.nv.feedback.wrapper.CustomFeedbackResponseWrapper;



/**
 * The Interface IConsumerFeedbackService.
 * @author innoeye
 */
public interface IConsumerFeedbackService extends IGenericService<Integer, ConsumerFeedback> {
	/**
	 * Creates the feedback.
	 *
	 * @param encryptedString the encrypted string
	 * @return the consumer feedback
	 * @throws RestException the rest exception
	 */
	ConsumerFeedback createFeedback(String encryptedString);

	Map<String, Map<String, Object>> getFeedbackAvgRating(Date fromDate, Date toDate, String  level, Integer geographyId);
	
	Map<String, Map<Integer, Long>> getFeedbackRatingWithCount(Date date, String  level, Integer geographyId);
	
	Map<String, Map<String, Object>> getDateWiseTestArea(String type, Date fromDate, Date toDate, String  level, Integer geographyId);
	
	Map<String, Map<String, Object>> getDateWiseFeedback(String type, Date fromDate, Date toDate, String  level, Integer geographyId);
	
	Map<String, Map<String, Map<String, Long>>> getVoiceDataCvgFeedback(Date fromDate, Date toDate, String  level, Integer geographyId);
	
	Map<String, List<Map<String, Object>>> getTopFeedbackLocations(Date fromDate, Date toDate, Integer lLimit, Integer uLimit, String  level, Integer geographyId);

	Object getLayerData(Integer zoom, Double nELat, Double nELng, Double sWLat, Double sWLng, String minDate,
			String maxDate, String testArea, String feedbackType, String band, String kpi, Integer fromStarRating, Integer toStarRating, String searchType);
	
	List<Map<String, Object>> getLayerDataForECare(Integer zoom, Double nELat, Double nELng, Double sWLat, Double sWLng, String fromDate,
			String toDate, String band, String kpi, String searchType, String locationType, String orderBy, String orderType);

	Map<String, Object> getDashboardDataForECare(Integer geographyId, String level, String date,
			String feedbackType);

	List<Object> getCustomFeedbackData(CustomFeedbackRequestWrapper requestWrapper);

	String createCustomFeedback();

}
