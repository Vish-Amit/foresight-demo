package com.inn.foresight.module.nv.webrtc.dao;

import java.text.ParseException;
import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.webrtc.model.ViberDashboard;

public interface IViberDashboardDao extends IGenericDao<Integer, ViberDashboard>{

    List<ViberDashboard> getViberDashboardData(String startDate, String endDate, String technology, String operator, String country, String nvModule, Boolean isLastSevenDayDataRequired)
            throws ParseException;
}
