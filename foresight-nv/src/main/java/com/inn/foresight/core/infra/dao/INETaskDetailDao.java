package com.inn.foresight.core.infra.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.infra.model.NETaskDetail;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;

public interface INETaskDetailDao extends IGenericDao<Integer, NETaskDetail> {

    List<NETaskDetail> getNETaskDetail(List<NEType> neTypeList, List<String> neFrequencyList, List<NEStatus> neStatusList, List<String> neNameList, List<String> neStagesList);

    List<NETaskDetail> getRollOutStatusOfSite(String name, String band);

    NETaskDetail getSiteTaskStatusBySiteIdAndBand(String siteId, String band, String taskName);

}
