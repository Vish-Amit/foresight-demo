package com.inn.foresight.module.nv.feedback.dao;

import java.util.Date;
import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.feedback.model.ConsumerFeedback;
import com.inn.foresight.module.nv.feedback.wrapper.ConsumerFeedbackWrapper;

/**
 * The Interface IConsumerFeedbackDao.
 * @author innoeye
 */
public interface IConsumerFeedbackDao extends IGenericDao<Integer, ConsumerFeedback> {

	List<ConsumerFeedbackWrapper> getFeedBackAvgRating(Date date, String  level, Integer geographyId);
	
	List<ConsumerFeedbackWrapper> getFeedBackRatingWithCount(Date date, String  level, Integer geographyId);
	
	List<ConsumerFeedbackWrapper> getDateWiseFeedBackCount(Date fromDate, Date toDate, String  level, Integer geographyId);
	
	List<ConsumerFeedbackWrapper> getDateWiseTestArea(String type, Date fromDate, Date toDate, String  level, Integer geographyId);
	
	List<ConsumerFeedbackWrapper> getDateWiseFeedback(String type, Date fromDate, Date toDate, String  level, Integer geographyId);
	
	List<ConsumerFeedbackWrapper> getDateWiseVoiceCount(Date fromDate, Date toDate, String  level, Integer geographyId);
	
	List<ConsumerFeedbackWrapper> getDateWiseDataCount(Date fromDate, Date toDate, String  level, Integer geographyId);
	
	List<ConsumerFeedbackWrapper> getDateWiseCoverageCount(Date fromDate, Date toDate, String  level, Integer geographyId);
	
	List<ConsumerFeedbackWrapper> getTopFeedbackLocations(Date fromDate, Date toDate, Integer lLimit, Integer uLimit, String  level, Integer geographyId);

	List<ConsumerFeedbackWrapper> getTopLocationsWithType(Date fromDate, Date toDate, Integer lLimit, Integer uLimit,
			String level, Integer geographyId);

	List<ConsumerFeedback> getFeedBackLayerData(String fromDate, String toDate, String testArea, String feedbackType,
			String band, String kpi, Integer fromStarRating, Integer toStarRating, String searchType);

	List<ConsumerFeedback> getPciWiseRating(Integer pci, Integer cellid);
	
	List<ConsumerFeedback> getLayerDataForECare(String locationType, String searchType, String kpi, Date fromDate, Date toDate, String orderBy,
			String orderType);

	List<ConsumerFeedback> getEcareDashboardData(List<Long> moduleCode, Integer geographyId, String level, String date);

}
