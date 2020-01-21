package com.inn.foresight.core.infra.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.INEBandDetailDao;
import com.inn.foresight.core.infra.dao.INELocationDao;
import com.inn.foresight.core.infra.dao.INETaskDetailDao;
import com.inn.foresight.core.infra.dao.INetworkElementDao;
import com.inn.foresight.core.infra.model.NEBandDetail;
import com.inn.foresight.core.infra.model.NETaskDetail;
import com.inn.foresight.core.infra.model.NetworkElement;
import com.inn.foresight.core.infra.service.INETaskDetailService;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.wrapper.NETaskDetailWrapper;
import com.inn.foresight.core.infra.wrapper.RRHDetailWrapper;

/** The Class NETaskDetailServiceImpl. */
@Service("NETaskDetailServiceImpl")
public class NETaskDetailServiceImpl extends AbstractService<Integer, NETaskDetail> implements INETaskDetailService {
	/** The logger. */
	private Logger logger = LogManager.getLogger(NETaskDetailServiceImpl.class);

	/** The ine task detail dao. */
	@Autowired
	private INETaskDetailDao ineTaskDetailDao;

	/** The network element dao. */
	@Autowired
	INetworkElementDao networkElementDao;

	/** The ne band detail dao. */
	@Autowired
	INEBandDetailDao neBandDetailDao;

	/** The ne location dao. */
	@Autowired
	INELocationDao neLocationDao;

	/**
	 * Gets the distinct stages for planned sites.
	 *
	 * @return the distinct stages for planned sites
	 */
	@Override
	@Transactional
	public List<String> getDistinctStagesForPlannedSites() {
		List stage = new ArrayList<>();
		try {
			logger.info("Going to get Distinct Stage of Planned Sites");
			List<NEType> neTypeList = new ArrayList<>();
			neTypeList.add(NEType.MACRO);
			List<String> neFrequencyList = new ArrayList<>();
			neFrequencyList.add(InfraConstants.BAND2300_STR);
			neFrequencyList.add(InfraConstants.BAND1800_STR);
			neFrequencyList.add(InfraConstants.BAND850_STR);
			List<NEStatus> neStatusList = new ArrayList<>();
			neStatusList.add(NEStatus.PLANNED);
			List<NETaskDetail> list = ineTaskDetailDao.getNETaskDetail(neTypeList, neFrequencyList, neStatusList, null,
					null);
			stage = list.stream().filter(distinctByKey(NETaskDetail::getTaskName)).collect(Collectors.toList());
		} catch (Exception exception) {
			logger.error("Unable to get the distinct Statges of Sites {} ");
			throw new RestException(ForesightConstants.INVALID_PARAMETERS);
		}
		return stage;
	}

	/**
	 * Distinct by key.
	 *
	 * @param <T> the generic type
	 * @param keyExtractor the key extractor
	 * @return the predicate
	 */
	private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		try {
			Map<Object, Boolean> seen = new ConcurrentHashMap<>();
			return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
		} catch (Exception exception) {
			logger.error("error in finding distinctByKey  Exception {} ", Utils.getStackTrace(exception));
		}
		return null;
	}

	/**
	 * Gets the site task status by site id and band.
	 *
	 * @param siteId     the site id
	 * @param taskName   the task name
	 * @param band       the band
	 * @param taskStatus the task status
	 * @return the site task status by site id and band
	 */
	@Override
	public boolean getSiteTaskStatusBySiteIdAndBand(String siteId, String taskName, String band, String taskStatus) {
		logger.info("inside @getSiteStageTaskStatusBySiteId siteId : {}, taskName : {},band : {} ", siteId, taskName,
				band);
		NETaskDetail neTask = ineTaskDetailDao.getSiteTaskStatusBySiteIdAndBand(siteId, band, taskName);
		return neTask != null && taskStatus.equalsIgnoreCase(neTask.getTaskStatus());
	}

	/**
	 * Gets the site task by site id and band.
	 *
	 * @param siteId     the site id
	 * @param taskName   the task name
	 * @param band       the band
	 * @param taskStatus the task status
	 * @return the site task by site id and band
	 */
	@Override
	public NETaskDetail getSiteTaskBySiteIdAndBand(String siteId, String taskName, String band, String taskStatus) {
		logger.info("inside @getSiteStageTaskStatusBySiteId siteId : {}, taskName : {},band : {} ", siteId, taskName, band);
		return ineTaskDetailDao.getSiteTaskStatusBySiteIdAndBand(siteId, band, taskName);
	}

	/**
	 * Update NE task detail.
	 *
	 * @param siteId      the site id
	 * @param riuSerialNo the riu serial no
	 * @param band        the band
	 * @param taskStatus  the task status
	 * @param taskName    the task name
	 * @param rrhList     the rrh list
	 * @param startDate   the start date
	 * @param endDate     the end date
	 * @param triggerTime the trigger time
	 * @return the map
	 */
	@Override
	@Transactional
	public Map<String, String> updateNETaskDetail(String siteId, String riuSerialNo, String band, String taskStatus, String taskName, List<RRHDetailWrapper> rrhList, Long startDate, Long endDate, Long triggerTime) {
		logger.info("inside updateTaskStatusInNETaskDetail method with siteId :{} and riuSerialNo :{},band :{},Taskstatus :{} ,taskName :{} ", siteId, riuSerialNo, band, taskStatus, taskName);
		Map<String, String> responseMap = new HashMap<>();
		try {
			NETaskDetail neTask = ineTaskDetailDao.getSiteTaskStatusBySiteIdAndBand(siteId, band, taskName);
			if (neTask == null) {
				NetworkElement neElement = networkElementDao.searchNetworkElementBySiteID(siteId);
				if (neElement != null) {
					NEBandDetail neBandDEtail = neBandDetailDao.checkNetworkElementInNeBandDetail(neElement.getId());
					neTask = new NETaskDetail();
					neTask.setTaskName(taskName);
					neTask.setTaskStatus(taskStatus);
					Date startTime = startDate != null ? new Date(startDate) : null;
					neTask.setActualStartDate(startTime);
					Date endTime = endDate != null ? new Date(endDate) : null;
					neTask.setActualEndDate(endTime);
					Date timeToTrigger = triggerTime != null ? new Date(triggerTime) : null;
					neTask.setPlannedStartDate(timeToTrigger);
					neTask.setNeBandDetail(neBandDEtail);
					ineTaskDetailDao.create(neTask);
				} else {
					logger.info("NO Record Found ...");
					responseMap.put(siteId, ForesightConstants.FAILURE);
					return responseMap;
				}
			} else {
				logger.info("Already existed in NETaskDetail");
				neTask.setTaskStatus(taskStatus);
				Date timeToTrigger = triggerTime != null ? new Date(triggerTime) : null;
				neTask.setPlannedStartDate(timeToTrigger);
				Date startTime = startDate != null ? new Date(startDate) : null;
				neTask.setActualStartDate(startTime);
				Date endTime = endDate != null ? new Date(endDate) : null;
				neTask.setActualEndDate(endTime);
				ineTaskDetailDao.update(neTask);
			}
			responseMap.put(siteId, ForesightConstants.SUCCESS);
			return responseMap;
		} catch (Exception e) {
			logger.info("Exception Occured while inserting TaskStatus in NETaskDeatil :{}", e.getMessage());
			throw new BusinessException("Exception Occured while inserting TaskStatus in NETaskDeatil");
		}
	}

	/**
	 * Update ne task detail by site and frequency.
	 *
	 * @param neDetailWrapper the ne detail wrapper
	 * @return the map
	 */
	@Override
	public Map<String, String> updateNeTaskDetailBySiteAndFrequency(NETaskDetailWrapper neTaskWrapper) {
		logger.info("inside @updateNETaskDetail method");
		try {
			return updateNETaskDetail(neTaskWrapper.getSiteId(), neTaskWrapper.getRiuSerialNo(), InfraConstants.BAND_1800, neTaskWrapper.getTaskStatus(), neTaskWrapper.getTaskName(), neTaskWrapper.getRrhList(), neTaskWrapper.getStartDate(), neTaskWrapper.getEndDate(), neTaskWrapper.getTriggerTime());
		} catch (Exception exception) {
			logger.error("Exception occured inside @updateNETaskDetail method :{}", ExceptionUtils.getStackTrace(exception));
			throw new BusinessException(ExceptionUtils.getMessage(exception));
		}
	}
	
	/**
	 * Update network element status by pk.
	 *
	 * @param nePk   the ne pk
	 * @param status the status
	 * @return the string
	 */
	@Override
	@Transactional
	public String updateNetworkElementStatusByPk(Integer nePk, String status) {
		logger.info("Going to update networkElement : {} with status : {}", nePk, status);
		try {
			NetworkElement networkElement = networkElementDao.findById(nePk);
			logger.info("networkElement Found : {}", networkElement.getId());
			if (status.equals(NEStatus.PLANNED.displayName())) {
				networkElement.setNeStage(NEStatus.PLANNED.toString());
			} else if (status.equals(NEStatus.INSERVICE.displayName())) {
				networkElement.setNeStage(NEStatus.INSERVICE.toString());
			} else if (status.equals(NEStatus.READYFORSERVICE.displayName())) {
				networkElement.setNeStage(NEStatus.READYFORSERVICE.toString());
			} else {
				return ForesightConstants.FAILURE_JSON;
			}
			networkElement.setModifiedTime(new Date());
			networkElement = networkElementDao.updateInNewTransaction(networkElement);
			logger.info("Network Element Successfully updated  Ne Name:{}   Status : {}", networkElement.getNeName(), networkElement.getNeStatus());
			return ForesightConstants.SUCCESS_JSON;
		} catch (Exception e) {
			logger.error("Error in updating Network Element : {}", Utils.getStackTrace(e));
			return ForesightConstants.FAILURE_JSON;
		}
	}
}