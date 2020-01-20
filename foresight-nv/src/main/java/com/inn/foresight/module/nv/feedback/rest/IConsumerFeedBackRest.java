package com.inn.foresight.module.nv.feedback.rest;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import com.inn.core.generic.exceptions.application.RestException;

/** The Interface IConsumerFeedBackRest. */
public interface IConsumerFeedBackRest {
	
	/**
	 * Creates the feedback.
	 *
	 * @param encryptedString the encrypted string
	 * @return the string
	 * @throws RestException the rest exception
	 */
	String createFeedback(String encryptedString) ;

	Response getDashboardDataForEcare(String level, Integer geographyId, String date, String feedbackType);

	List<Map<String, Object>> getLayerDataForECare(Integer zoom, Double nELat, Double nELng, Double sWLat, Double sWLng,
			String fromDate, String toDate, String band, String kpi, String searchType, String locationType, String orderBy, String orderType);

	Response getFeedbackLayerData(Integer zoom, Double nELat, Double nELng, Double sWLat, Double sWLng, String fromDate,
			String toDate, String testArea, String feedbackType, String band, String kpi, Integer fromStarRating,
			Integer toStarRating, String searchType);

	Response getTopFeedbackLocations(Long fromDate, Long toDate, Integer lLimit, Integer uLimit, String level,
			Integer geographyId);

	Response getVoiceDataCvgFeedback(Long fromDate, Long toDate, String level, Integer geographyId);

	Response getDateWiseFeedback(String type, Long fromDate, Long toDate, String level, Integer geographyId);

	Response getDateWiseTestArea(String type, Long fromDate, Long toDate, String level, Integer geographyId);

	Response getFeedbackRatingWithCount(Long date, String level, Integer geographyId);

	Response getFeedbackAvgRating(Long fromDate, Long toDate, String level, Integer geographyId);

	List<Object> getCustomFeedbackData(Integer zoom, Double nELat, Double nELng, Double sWLat, Double sWLng, String fromDate, String toDate,
			String band, String kpi, String searchType, String locationType, String orderBy, String orderType);

	String createCustomFeedback();
}
