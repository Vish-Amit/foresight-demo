package com.inn.foresight.module.nv.workorder.stealth.service.impl;

import java.util.*;

import com.inn.foresight.module.nv.workorder.wrapper.WOFileDetailWrapper;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.report.utils.StealthUtils;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.IWOFileDetailDao;
import com.inn.foresight.module.nv.workorder.stealth.dao.IStealthTaskDetailDao;
import com.inn.foresight.module.nv.workorder.stealth.dao.IStealthTaskResultDao;
import com.inn.foresight.module.nv.workorder.stealth.model.StealthTaskResult;
import com.inn.foresight.module.nv.workorder.stealth.service.IStealthTaskResultService;
import com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOWrapper;

@Service("StealthTaskResultServiceImpl")
public class StealthTaskResultServiceImpl extends AbstractService<Integer, StealthTaskResult> implements IStealthTaskResultService {

	private Logger logger = LogManager.getLogger(StealthTaskResultServiceImpl.class);

	@Autowired
	IStealthTaskResultDao iStealthTaskResultDao;

	@Autowired
	IStealthTaskDetailDao iStealthTaskDetailDao;
	
	@Autowired
	IGenericWorkorderDao iGenericWorkorderDao;
	
	@Autowired
	IWOFileDetailDao woFileDetailDao;
	
	@Override
	public StealthTaskResult getStealthTaskResultByStealthTaskDetailId(Integer stealthTaskDetailId, Long startTime)  {
		logger.info("going to get stealth task result for task detail id: {}", stealthTaskDetailId);
		try {
			return iStealthTaskResultDao.getStealthTaskResultByStealthTaskDetailId(stealthTaskDetailId,new Date(startTime),null);
		} catch (Exception e) {
			logger.error("Error in getStealthTaskResultByStealthTaskDetailId: {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ForesightConstants.UNABLE_TO_PROCESS_REQUEST);
		}
	}

	@Override
	public List<NVDeviceDataWrapper> getStealthDeviceListByWOId(Integer workorderId) {
		try {
			logger.info("Going to fetch Stealth Device List  for woId {} ", workorderId);
			List<NVDeviceDataWrapper> stealthDeviceWrapperList = iStealthTaskResultDao.getStealthDeviceWrapperListByWOId(workorderId);
			List<WOFileDetailWrapper> processedLogFileMappingList = woFileDetailDao.getProccesedFilesWithRecipe(workorderId);
			Map<Integer,List<Integer>> map=new HashMap<>();
			if(processedLogFileMappingList !=null && !processedLogFileMappingList.isEmpty()) {
			 processedLogFileMappingList.forEach(wrapper->{
				if(map.get(wrapper.getRecipeId())!=null){
				map.get(wrapper.getRecipeId()).add(wrapper.getId());
				}
				else{
					List<Integer> fileIdList=new ArrayList<>();
					fileIdList.add(wrapper.getId());
					map.put(wrapper.getRecipeId(),fileIdList);
				}
			 });
			 stealthDeviceWrapperList.stream().forEach(wrapper-> {
				 if(map.get(wrapper.getWoRecipeMappingId())!=null) {
					 wrapper.setFileList(map.get(wrapper.getWoRecipeMappingId()));
					 wrapper.setLogFileDownload("true");
				 }
			 });
			}

			return stealthDeviceWrapperList;
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	@Override
	public List<Map<String,Long>> getStealthWoSummary(Integer workorderId) {
		try {
			GenericWorkorder genericWorkorder = iGenericWorkorderDao.findByPk(workorderId);
			String type = genericWorkorder.getGwoMeta().get("woFrequency");
		
			logger.info("Going to getStealthWoSummary for woId: {} type: {}",workorderId,type);
			List<StealthWOWrapper> resultList = iStealthTaskResultDao.getStatusSummary(workorderId,type);
			StealthWOWrapper deviceCountWrapper = iStealthTaskResultDao.getDeviceCount(workorderId);
			List<StealthWOWrapper> taskList =iStealthTaskDetailDao.getAcknowledgementSummary(workorderId,type);
			logger.info("resultList: {}",resultList);
			logger.info("taskList: {}",taskList);
			logger.info("Device Count: {}",deviceCountWrapper);
			return StealthUtils.getStealthResponseToReturnForSummary(taskList, resultList,type,deviceCountWrapper);
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
		}
		return Collections.emptyList();
	}
}
