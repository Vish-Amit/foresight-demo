package com.inn.foresight.module.nv.webrtc.dao;

import java.text.ParseException;
import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.webrtc.model.ViberSubscriber;

public interface IViberSubscriberDao  extends IGenericDao<Integer, ViberSubscriber>{
	
	List<ViberSubscriber> getUserCount(String startDate, String endDate, String technology, String operator, String country, String nvModule, Boolean isLastSevenDayDataRequired) throws ParseException;

}
