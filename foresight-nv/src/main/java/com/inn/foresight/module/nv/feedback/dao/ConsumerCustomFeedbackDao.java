package com.inn.foresight.module.nv.feedback.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.feedback.model.ConsumerCustomFeedback;
import com.inn.foresight.module.nv.feedback.wrapper.CustomFeedbackRequestWrapper;
import com.inn.foresight.module.nv.feedback.wrapper.CustomFeedbackResponseWrapper;

public interface ConsumerCustomFeedbackDao extends IGenericDao<Integer, ConsumerCustomFeedback> {

	List<CustomFeedbackResponseWrapper> getCustomFeedbackData(CustomFeedbackRequestWrapper requestWrapper);

}
