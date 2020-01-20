package com.inn.foresight.module.nv.workorder.stealth.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpException;
import com.inn.commons.http.HttpPostRequest;
import com.inn.commons.unit.Duration;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.maplayer.service.impl.GenericMapServiceImpl;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.dao.IReportTemplateDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.core.report.model.AnalyticsRepository.RepositoryType;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.core.report.model.ReportTemplate;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.Status;
import com.inn.foresight.module.nv.device.dao.INVDeviceDataDao;
import com.inn.foresight.module.nv.device.model.NVDeviceData;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification.ModuleName;
import com.inn.foresight.module.nv.pushnotification.service.IPushNotificationAsyncService;
import com.inn.foresight.module.nv.report.utils.StealthUtils;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants.Acknowledgement;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants.WOTestResult;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants.WOTestStatus;
import com.inn.foresight.module.nv.workorder.stealth.dao.IStealthTaskDetailDao;
import com.inn.foresight.module.nv.workorder.stealth.dao.IStealthTaskResultDao;
import com.inn.foresight.module.nv.workorder.stealth.kpi.dao.IDriveTestKPIDao;
import com.inn.foresight.module.nv.workorder.stealth.kpi.model.DriveTestKPI;
import com.inn.foresight.module.nv.workorder.stealth.kpi.wrapper.DriveTestKPIWrapper;
import com.inn.foresight.module.nv.workorder.stealth.model.StealthTaskDetail;
import com.inn.foresight.module.nv.workorder.stealth.model.StealthTaskResult;
import com.inn.foresight.module.nv.workorder.stealth.service.IStealthTaskDetailService;
import com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthTaskDetailWrapper;
import com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOParameters;
import com.inn.product.security.utils.AuthenticationCommonUtil;

@Service("StealthTaskDetailServiceImpl")
public class StealthTaskDetailServiceImpl extends AbstractService<Integer, StealthTaskDetail>implements IStealthTaskDetailService {

	private Logger logger = LogManager.getLogger(StealthTaskDetailServiceImpl.class);

	@Autowired
	private IStealthTaskDetailDao stealthTaskDetailDao;

	@Autowired
	private INVDeviceDataDao deviceDataDao;

	@Autowired
	private IStealthTaskResultDao stealthTaskResultDao;

	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;

	@Autowired
	private IPushNotificationAsyncService iPushNotificationAsyncService;
	@Autowired 
	private IDriveTestKPIDao driveTestKPIDao;
	
	@Autowired
	private IWORecipeMappingDao woRecipeMappingDao;

	@Autowired
	private IReportTemplateDao reportTemplateDao;

	@Autowired
	private IAnalyticsRepositoryDao iAnalyticsRepositoryDao;

	@Transactional
	@Override
	public String updateStealthTaskDetails(String encryptedWOTestJson) {
		logger.info("Going to update stealth task details encryptedWOTestJson: {}", encryptedWOTestJson);

		try {
			DriveTestKPIWrapper deviceWOTestWrapper = decryptNVDeviceWOTestJson(encryptedWOTestJson);
			StealthTaskDetailWrapper stealthTaskWrapper = extractStealthTaskWrapperFromNVWoTestWrapper(deviceWOTestWrapper);
			return AuthenticationCommonUtil.checkForValueEncryption(updateStealthTaskDetails(stealthTaskWrapper),null);
		} catch (Exception e) {
			logger.error("Error in updateStealthTaskDetails===: {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(ForesightConstants.FAILURE_JSON,null);
		}
	}

	private String updateStealthTaskDetails(StealthTaskDetailWrapper stealthTaskWrapper) {
		logger.info("Going to update Stealth Task Record in database: {}", stealthTaskWrapper);
		try {
		Integer stealthTaskId = stealthTaskWrapper.getId();
		Long startTime = stealthTaskWrapper.getStartTime();
		logger.info("Fetching stealth task detail for task Id: {}", stealthTaskId);
		StealthTaskDetail stealthTaskDetail = stealthTaskDetailDao.findByPk(stealthTaskId);
		StealthTaskResult taskresult = null;					
		if(stealthTaskDetail!=null && stealthTaskDetail.getGenericWorkorder().getGwoMeta().get("woFrequency").equalsIgnoreCase("HOURLY")) {
			Integer hour=StealthUtils.getHourFromTimestamp(startTime);
			logger.info("Hourly workorder  hour {} ", hour);
			taskresult = stealthTaskResultDao.getStealthTaskResultByStealthTaskDetailId(stealthTaskId,new Date(startTime),hour);
		}else if(stealthTaskDetail!=null){
			 taskresult = stealthTaskResultDao.getStealthTaskResultByStealthTaskDetailId(stealthTaskId,new Date(startTime),null);
		}
		if(stealthTaskDetail != null && (stealthTaskDetail.getStartTime().getTime() <= stealthTaskWrapper.getStartTime())) {
		if (taskresult != null) {
				updateStartAndEndTimeForTask(stealthTaskWrapper, taskresult);
				updateStealthTaskDetailsFromWrapper(stealthTaskWrapper, taskresult);
				taskresult = stealthTaskResultDao.update(taskresult);
				createEntryInDriveTestKPI(taskresult);
				logger.info("Stealth Task Details Updated: {}", taskresult);
				return ForesightConstants.SUCCESS_JSON;
			} else {
				logger.info("No task result found for stealth task detail id: {}", stealthTaskId);
				logger.info("Going to create entry in StealthTaskResult");
				StealthTaskResult newtaskresult = new StealthTaskResult();
				updateStealthTaskDetailsFromWrapper(stealthTaskWrapper, newtaskresult);
				
				newtaskresult.setCreationTime(new Date(stealthTaskWrapper.getStartTime()));
				newtaskresult.setModificationTime(new Date());
				newtaskresult.setStartTime(new Date(stealthTaskWrapper.getStartTime()));
				newtaskresult.setStealthTaskDetail(stealthTaskDetail);

				newtaskresult = stealthTaskResultDao.create(newtaskresult);
				createEntryInDriveTestKPI(newtaskresult);
				logger.info("new entry inserted in StealthTaskResult : {}", newtaskresult);
				return ForesightConstants.SUCCESS_JSON;
			}
		
		}
		else{
			return ForesightConstants.SUCCESS_JSON;
		}
		}	catch(Exception e) {
			logger.error("Error while update following task detail in database: {} ",Utils.getStackTrace(e));
			return ForesightConstants.FAILURE_JSON;
		
		}
	}

	
	private void createEntryInDriveTestKPI(StealthTaskResult taskresult) {
		DriveTestKPI driveTestKPI = new DriveTestKPI();
		driveTestKPI.setTaskResult(taskresult);
		driveTestKPI.setStartTime(taskresult.getStartTime());
		driveTestKPI.setEndTime(taskresult.getEndTime());
		driveTestKPI.setCreationTime(new Date());
		driveTestKPI.setModificationTime(new Date());
		driveTestKPI.setStatus(WOTestStatus.SYNCED.name());
		driveTestKPI = driveTestKPIDao.create(driveTestKPI);
		logger.info("Entry inserted in DriveTestKPI with id: {}",driveTestKPI.getId());
		}
	private StealthTaskDetailWrapper extractStealthTaskWrapperFromNVWoTestWrapper(DriveTestKPIWrapper deviceWOTestWrapper) {
		StealthTaskDetailWrapper stealthTaskWrapper = new StealthTaskDetailWrapper();
		if (deviceWOTestWrapper != null) {
			stealthTaskWrapper.setId(deviceWOTestWrapper.getDeviceWOTestId());
			stealthTaskWrapper.setStatus(deviceWOTestWrapper.getTestResult());
			stealthTaskWrapper.setRemark(deviceWOTestWrapper.getRemark());
			stealthTaskWrapper.setOperator(deviceWOTestWrapper.getOperator());
			stealthTaskWrapper.setTechnology(deviceWOTestWrapper.getTechnology());
			stealthTaskWrapper.setTotalIteration(deviceWOTestWrapper.getTotalIteration());
			stealthTaskWrapper.setCompletedIteration(deviceWOTestWrapper.getCompletedIteration());
			stealthTaskWrapper.setStartTime(deviceWOTestWrapper.getTestStartTime());
			stealthTaskWrapper.setEndTime(deviceWOTestWrapper.getTestEndTime());
		}
		return stealthTaskWrapper;
	}

	@Transactional
	@Override
	public String updateAcknowledgement(String encryptedTaskJsonList) {

		try {
			List<StealthWOParameters> taskList = decryptWOTaskJsonList(encryptedTaskJsonList);
			if (taskList != null && !taskList.isEmpty()) {
				for (StealthWOParameters stealthTaskWrapper : taskList) {
					Integer woId = null;
					String deviceId = null;
					try {
						if (stealthTaskWrapper != null) {
							woId = stealthTaskWrapper.getWoId();
							deviceId = stealthTaskWrapper.getDeviceId();
							logger.info("Going to update acknowledgement: {}", stealthTaskWrapper);
							logger.info("Fetching stealth task from database for deviecId==: {}, woId===: {} ", deviceId,
									woId);

							NVDeviceData nvDevice = deviceDataDao.getDeviceByDeviceId(deviceId);
							StealthTaskDetail taskDetail = stealthTaskDetailDao.getStealthTaskByDeviceAndWOId(
									nvDevice.getId(), woId);
							
							updateDeviceAcknowledgement(stealthTaskWrapper, taskDetail);
							taskDetail = stealthTaskDetailDao.update(taskDetail);
							logger.info("Acknowledgement is Updated for Stealth Task Detail id: {}",
									taskDetail.getId());
							
							if(taskDetail.getStartTime() != null && taskDetail.getStartTime().getTime() < System.currentTimeMillis()) {
							updateResultStatusForDelayedAcknowledgement(taskDetail);
							}
						}
					} catch (Exception e) {
						logger.error("Error while update acknowledgement for woId: {}, deviceId: {}, {}  ", woId, deviceId,ExceptionUtils.getStackTrace(e));
					}
				}
				return AuthenticationCommonUtil.checkForValueEncryption(ForesightConstants.SUCCESS_JSON,null);
			}
			return AuthenticationCommonUtil.checkForValueEncryption(ForesightConstants.INVALID_PARAMETERS_JSON,null);
		} catch (Exception e) {
			logger.error("Error in updateStealthTaskDetails: {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(NVWorkorderConstant.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	private void updateResultStatusForDelayedAcknowledgement(StealthTaskDetail taskDetail) {
		logger.info("Going to update result status for delayed acknowledgement: {}", taskDetail);
		Map<Integer, StealthTaskResult> frequencyWiseRecords;
		Map<String, String> gwoMeta;
		String frequency;
		List<StealthTaskResult> resultList = stealthTaskResultDao.getStealthResultListByTaskId(taskDetail.getId());
	
			gwoMeta = taskDetail.getGenericWorkorder().getGwoMeta();
			if (gwoMeta != null && !gwoMeta.isEmpty()) {
				frequency = gwoMeta.get(NVConstant.WO_FREQUENCY);
				String isProbeTest = gwoMeta.get(StealthConstants.IS_PROBE_TEST);
				
				if(isProbeTest!=null&&isProbeTest.equalsIgnoreCase(ForesightConstants.TRUE_STRING)) {
					logger.info("Ignoring Workorder {}  for delayed ack for probe",taskDetail.getGenericWorkorder().getId());
				}
				else { 
					frequencyWiseRecords = arrangeResultFrequencyWise(frequency, resultList);
					insertOrUpdateResultStatusForDelayedAcknowledgement(frequency,taskDetail, frequencyWiseRecords);
			}
		}
	}

	private void insertOrUpdateResultStatusForDelayedAcknowledgement(String frequency, StealthTaskDetail taskDetail,
			Map<Integer, StealthTaskResult> frequencyWiseRecords) {
		Calendar calendar = Calendar.getInstance();
		if (taskDetail.getStartTime() != null && taskDetail.getEndTime() != null) {

			if (NVConstant.HOURLY.equalsIgnoreCase(frequency)) {
				insertOrUpdateResultStatusForHourlyInterval(taskDetail, frequencyWiseRecords, calendar);
			} else if (NVConstant.FREQUENCY_ONETIME.equalsIgnoreCase(frequency)) {
				insertOrUpdateResultStatusForOneTimeWO(taskDetail, frequencyWiseRecords);
			} else if (NVConstant.FREQUENCY_DAILY.equalsIgnoreCase(frequency)) {
				insertOrUpdateResultStatusForDailyInterval(taskDetail, frequencyWiseRecords, calendar);
			} else if (NVConstant.FREQUENCY_WEEKLY.equalsIgnoreCase(frequency)) {
				insertOrUpdateResultStatusForWeeklyInterval(taskDetail, frequencyWiseRecords, calendar);
			}

		} else {
			logger.warn("Invalid time interval: startTime: {}, endTime: {}", taskDetail.getStartTime(), taskDetail.getEndTime());
		}
	}

	private void insertOrUpdateResultStatusForWeeklyInterval(StealthTaskDetail taskDetail, Map<Integer, StealthTaskResult> frequencyWiseRecords,
			Calendar calendar) {
		Long startTime = taskDetail.getStartTime().getTime();
		Long endTime = taskDetail.getEndTime().getTime();
		Long currentTime = System.currentTimeMillis();
			
		for(Long week = startTime; week <= endTime; week += StealthConstants.ONE_WEEK_INTERVAL) {
			if(week < currentTime) {
				calendar.setTime(new Date(week));
				StealthTaskResult taskResult = frequencyWiseRecords.get(calendar.get(Calendar.WEEK_OF_YEAR));
				String resultStatus = taskResult!=null?taskResult.getStatus():null;
				if (resultStatus == null || resultStatus.equalsIgnoreCase(WOTestStatus.INPROGRESS.toString())) {
					resultStatus = StealthConstants.WOTestStatus.FAILURE.toString();
					insertOrUpdateResultStatus(taskDetail, taskResult, resultStatus, StealthConstants.REMARK_DELAYED_ACKNOWLEDGEMENT,week, week + StealthConstants.ONE_WEEK_INTERVAL);
				}
				
			}
		}
	}

	private void insertOrUpdateResultStatusForDailyInterval(StealthTaskDetail taskDetail, Map<Integer, StealthTaskResult> frequencyWiseRecords,
			Calendar calendar) {
		Long startTime = taskDetail.getStartTime().getTime();
		Long endTime = taskDetail.getEndTime().getTime();
		Long currentTime = System.currentTimeMillis();
			
		for(Long day = startTime; day <= endTime; day += StealthConstants.ONE_DAY_INTERVAL) {
			if(day < currentTime) {
				calendar.setTime(new Date(day));
				StealthTaskResult taskResult = frequencyWiseRecords.get(calendar.get(Calendar.DAY_OF_YEAR));
				String resultStatus = taskResult!=null?taskResult.getStatus():null;
				if (resultStatus == null || resultStatus.equalsIgnoreCase(WOTestStatus.INPROGRESS.toString())) {
					resultStatus = StealthConstants.WOTestStatus.FAILURE.toString();
					insertOrUpdateResultStatus(taskDetail, taskResult, resultStatus, StealthConstants.REMARK_DELAYED_ACKNOWLEDGEMENT, day, day + StealthConstants.ONE_DAY_INTERVAL);
				}
				
			}
		}
	}

	private void insertOrUpdateResultStatusForOneTimeWO(StealthTaskDetail taskDetail, Map<Integer, StealthTaskResult> frequencyWiseRecords) {
		Long startTime = taskDetail.getStartTime().getTime();
		startTime+=StealthConstants.FIFTEEN_MINUTE_INTERVAL;
		Long currentTime = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(startTime);

		if (startTime < currentTime) {
			StealthTaskResult taskResult = frequencyWiseRecords.get(calendar.get(Calendar.DAY_OF_YEAR));
			
			String resultStatus = taskResult!=null?taskResult.getStatus():null;
			if (resultStatus == null || resultStatus.equalsIgnoreCase(WOTestStatus.INPROGRESS.toString())) {
				resultStatus = StealthConstants.WOTestStatus.FAILURE.toString();
				insertOrUpdateResultStatus(taskDetail, taskResult, resultStatus, StealthConstants.REMARK_DELAYED_ACKNOWLEDGEMENT,
						taskDetail.getStartTime().getTime(), taskDetail.getEndTime().getTime());
			}
			
		}
	}

	private void insertOrUpdateResultStatusForHourlyInterval(StealthTaskDetail taskDetail, Map<Integer, StealthTaskResult> frequencyWiseRecords,
			Calendar calendar) {

		Long startTime = taskDetail.getStartTime().getTime();
		Long endTime = taskDetail.getEndTime().getTime();
		Long currentTime = System.currentTimeMillis();

		for (Long hour = startTime; hour <= endTime; hour += StealthConstants.ONE_HOUR_INTERVAL) {
			if (hour < currentTime) {
				calendar.setTime(new Date(hour));
				StealthTaskResult taskResult = frequencyWiseRecords.get(calendar.get(Calendar.HOUR_OF_DAY));
				
				
				String resultStatus = taskResult!=null?taskResult.getStatus():null;
				if (resultStatus == null || resultStatus.equalsIgnoreCase(WOTestStatus.INPROGRESS.toString())) {
					resultStatus = StealthConstants.WOTestStatus.FAILURE.toString();
					insertOrUpdateResultStatus(taskDetail, taskResult, resultStatus, StealthConstants.REMARK_DELAYED_ACKNOWLEDGEMENT, hour,
							hour + StealthConstants.ONE_HOUR_INTERVAL);
				}
				
		
			}
		}
	}

	private void insertOrUpdateResultStatus(StealthTaskDetail taskDetail, StealthTaskResult taskResult, String resultStatus, String remark,Long startTime, Long endTime) {
		if(taskResult == null) {
			taskResult = new StealthTaskResult();
			taskResult.setStealthTaskDetail(taskDetail);
			taskResult.setStatus(resultStatus);
			taskResult.setRemark(remark);
			taskResult.setCreationTime(new Date());
			taskResult.setModificationTime(new Date());
			taskResult.setStartTime(new Date(startTime));
			taskResult.setEndTime(new Date(endTime));
			taskResult = stealthTaskResultDao.create(taskResult);
			logger.info("Entry inserted for task result with delayed acknowledgment, result id: {}",taskResult.getId());
		}else {
			taskResult.setStatus(resultStatus);
			taskResult.setRemark(remark);
			
			taskResult.setModificationTime(new Date());
			taskResult = stealthTaskResultDao.update(taskResult);
			logger.info("Entry updated for task result with delayed acknowledgment, result id: {}",taskResult.getId());
		}
	}

	private Map<Integer, StealthTaskResult> arrangeResultFrequencyWise(String frequency, List<StealthTaskResult> resultList) {
		Map<Integer, StealthTaskResult> frequencyWiseRecords = new HashMap<>();
		try {
		Calendar calendar = Calendar.getInstance();
		for (StealthTaskResult result : resultList) {
			calendar.setTime(result.getStartTime());
			if (NVConstant.HOURLY.equalsIgnoreCase(frequency)) {
				frequencyWiseRecords.put(calendar.get(Calendar.HOUR_OF_DAY), result);
			} else if (NVConstant.FREQUENCY_ONETIME.equalsIgnoreCase(frequency)) {
				frequencyWiseRecords.put(calendar.get(Calendar.DAY_OF_YEAR), result);
			} else if (NVConstant.FREQUENCY_DAILY.equalsIgnoreCase(frequency)) {
				frequencyWiseRecords.put(calendar.get(Calendar.DAY_OF_YEAR), result);
			} else if (NVConstant.FREQUENCY_WEEKLY.equalsIgnoreCase(frequency)) {
				frequencyWiseRecords.put(calendar.get(Calendar.WEEK_OF_YEAR), result);
			}
		}
		}catch(Exception e) {
			logger.error("Error while setting result frequency wise: {} ",e.getMessage());
		}
		return frequencyWiseRecords;
	}

	private void updateStealthTaskDetailsFromWrapper(StealthTaskDetailWrapper stealthTaskWrapper,
			StealthTaskResult taskResult) {
		if (stealthTaskWrapper.getStatus() != null) {
			taskResult.setStatus(stealthTaskWrapper.getStatus());
		}
		
		//In Case of Success there should be entry in WORecipeMapping and analyticsRepository
		if(stealthTaskWrapper.getStatus()!=null && stealthTaskWrapper.getStatus().equalsIgnoreCase(WOTestResult.SUCCESS.name())){
			WORecipeMapping woRecipeMapping = taskResult.getWoRecipeMapping();
			if(woRecipeMapping!=null){
				woRecipeMapping.setStatus(com.inn.foresight.module.nv.workorder.model.WORecipeMapping.Status.COMPLETED);				
			}
			
		}
		
		taskResult.setRemark(stealthTaskWrapper.getRemark()!=null?stealthTaskWrapper.getRemark():ForesightConstants.DASH);

		if(stealthTaskWrapper.getOperator() != null) {
			taskResult.setOperator(stealthTaskWrapper.getOperator());
		}
		if(stealthTaskWrapper.getTechnology() != null) {
			taskResult.setTechnology(stealthTaskWrapper.getTechnology());
		}
		if(stealthTaskWrapper.getTotalIteration() != null) {
			taskResult.setTotalIteration(stealthTaskWrapper.getTotalIteration());
		}
		if(stealthTaskWrapper.getCompletedIteration() != null) {
			taskResult.setCompletedIteration(stealthTaskWrapper.getCompletedIteration());
		}

	}
	
	
	private AnalyticsRepository createCustomReportRepository(Integer templateId, AnalyticsRepository repository) {
		try {
    		logger.info("going to create repo for report template id: {}",templateId);
			ReportTemplate template = reportTemplateDao.findByPk(templateId);
			if(repository != null) {
				if(repository.getReportConfig()!=null) {
					repository.setReportConfig(repository.getReportConfig().replace("'", "\""));
				}
				repository.setReportTemplate(template);
				repository.setCreatedTime(new Date());
				repository.setModifiedTime(new Date());
				repository.setTemplateId(templateId);
				repository.setRepositoryType(RepositoryType.CUSTOM_REPORT);
				repository.setProgress(progress.In_Progress);
				repository.setQueueStatus(false);
				repository.setScheduled(false);
				repository.setDeleted(false);
				//setCreateFilePathForRepoCreator(repository,template);
				
			    return iAnalyticsRepositoryDao.create(repository);
			}
		} catch (Exception e) {
			logger.error("Exception in createCustomReportRepository, err = {}",ExceptionUtils.getStackTrace(e));
		}
    	throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);

		
	}

	private void updateDeviceAcknowledgement(StealthWOParameters stealthTaskWrapper, StealthTaskDetail taskDetail) {
		String acknowledgement = stealthTaskWrapper.getAcknowledgement();
		if (acknowledgement != null && taskDetail != null) {
			taskDetail.setModificationTime(new Date());
			if (ForesightConstants.TRUE_CAPS.equalsIgnoreCase(acknowledgement)) {
				taskDetail.setAcknowledgement(Acknowledgement.ACCEPT.name());
			} else if (ForesightConstants.FALSE_CAPS.equalsIgnoreCase(acknowledgement)) {
				taskDetail.setAcknowledgement(Acknowledgement.REJECT.name());
			}
		} else {
			logger.error("invalid ack status or taskId {},{}", acknowledgement, taskDetail);
		}
	}

	private StealthWOParameters decryptWOTaskJson(String encryptedWOTaskJson)  {
		try {
			if (encryptedWOTaskJson != null) {
				String woTaskJson = AuthenticationCommonUtil.checkForValueDecryption(encryptedWOTaskJson);
				logger.info("decryptedJson: {}", woTaskJson);
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(woTaskJson, StealthWOParameters.class);
			} 
		} catch (Exception e) {
			logger.error("Error while decrypting stealth task json to Wrapper ===: {}", Utils.getStackTrace(e));
		}
		return null;
	}

	private List<StealthWOParameters> decryptWOTaskJsonList(String encryptedWOTaskJsonList)  {
		try {
			if (encryptedWOTaskJsonList != null) {
				String woTaskJsonList = AuthenticationCommonUtil.checkForValueDecryption(encryptedWOTaskJsonList);
				logger.info("decryptedWOTaskJsonList: {}", woTaskJsonList);
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(woTaskJsonList,
						new TypeReference<List<StealthWOParameters>>() {
						});
			} 
		} catch (Exception e) {
			logger.error("Error while decrypting stealth task json to Wrapper: {}", Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	private DriveTestKPIWrapper decryptNVDeviceWOTestJson(String encryptedWOTestJson) {
		try {
			if (encryptedWOTestJson != null) {
				String woTestJson = AuthenticationCommonUtil.checkForValueDecryption(encryptedWOTestJson);
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(woTestJson, DriveTestKPIWrapper.class);
			} 

		} catch (Exception e) {
			logger.error("Error while decrypting WO Test Json to Wrapper: {}", Utils.getStackTrace(e));
			throw new RestException("Unable to decrypt WO Test Data");
		}
		return null;

	}

	private void updateStartAndEndTimeForTask(StealthTaskDetailWrapper stealthTaskWrapper,
			StealthTaskResult taskResult) {
		try {
			if(stealthTaskWrapper.getStartTime() != null) {
			taskResult.setStartTime(new Date(stealthTaskWrapper.getStartTime()));
			}
			if(stealthTaskWrapper.getEndTime() != null) {
				taskResult.setEndTime(new Date(stealthTaskWrapper.getEndTime()));
			}
		} catch (Exception e) {
			logger.error("Error while update start & end time for record: ", ExceptionUtils.getStackTrace(e));
		}
	}


	public boolean isAnyTestPending(Date dueDate, Date currentDate) {
		if (dueDate.compareTo(currentDate) > ForesightConstants.ZERO) {
			logger.info("Some test are pending for Work Order till Due Date: " + dueDate);
			return true;
		} else {
			logger.info("All Tests are performed for Work Order !!!");
			return false;
			
		}
	}
	@Override
	@Transactional
	public String fetchStealthWOTaskId(String encryptedDeviceId, String encryptedWoId) {
		try {
			Integer woId = null;
			if (Utils.hasValidValue(AuthenticationCommonUtil.checkForValueDecryption(encryptedWoId))) {
				woId = Integer.valueOf(AuthenticationCommonUtil.checkForValueDecryption(encryptedWoId));

				logger.info("Fetching stealth task from database for deviecId: {}, woId: {} ",
						AuthenticationCommonUtil.checkForValueDecryption(encryptedDeviceId), woId);

				NVDeviceData nvDevice = deviceDataDao.getDeviceByDeviceId(
						AuthenticationCommonUtil.checkForValueDecryption(encryptedDeviceId));
				StealthTaskDetail taskDetail = stealthTaskDetailDao.getStealthTaskByDeviceAndWOId(nvDevice.getId(),
						woId);
				logger.info("Stealth Task Id to return is: {}", taskDetail.getId());
				StealthTaskDetailWrapper taskWrapper = new StealthTaskDetailWrapper();
				taskWrapper.setId(taskDetail.getId());
				return new Gson().toJson(taskWrapper);

			} else {
				return ForesightConstants.INVALID_PARAMETERS_JSON;
			}

		} catch (Exception e) {
			logger.error("Error in startStealthWOTask: {}", Utils.getStackTrace(e));
			throw new RestException(NVWorkorderConstant.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	@Override
	@Transactional
	public String checkWOStatus(String encryptedTaskJson) {
		logger.info("Going to check work order status");
		StealthWOParameters stealthTaskWrapper = null;
		StealthWOParameters response = new StealthWOParameters();
		try {
			Integer woId = null;
			String deviceId = null;
			stealthTaskWrapper = decryptWOTaskJson(encryptedTaskJson);
			if (stealthTaskWrapper != null) {
				woId = stealthTaskWrapper.getWoId();
				deviceId = stealthTaskWrapper.getDeviceId();
				logger.info("Request Parameters: {}", stealthTaskWrapper);
				logger.info("Fetching stealth task from database for deviecId: {}, woId: {} ", deviceId, woId);
				NVDeviceData nvDevice = deviceDataDao.getDeviceByDeviceId(deviceId);
				StealthTaskDetail taskDetail = stealthTaskDetailDao.getStealthTaskByDeviceAndWOId(nvDevice.getId(),woId);

				updateAcknowledmentForDevice(taskDetail);
				updateResponseForCheckWOStatus(stealthTaskWrapper, response, taskDetail);

				if (response.getResponse() != null && response.getResponse().equalsIgnoreCase(ForesightConstants.SUCCESS_JSON)) {
					createTaskResult(stealthTaskWrapper,response);
					updateGenericWorkorderStatus(taskDetail.getGenericWorkorder());	
				}
			} 
		} catch (Exception e) {
			logger.error("Error while checking WO status: for {}, {}", stealthTaskWrapper,
					ExceptionUtils.getStackTrace(e));
		}
		return new Gson().toJson(response);
	}

	private void updateAcknowledmentForDevice(StealthTaskDetail taskDetail) {
		taskDetail.setAcknowledgement(Acknowledgement.ACCEPT.name());
		stealthTaskDetailDao.update(taskDetail);
	}

	private void updateGenericWorkorderStatus(GenericWorkorder genericWorkorder) {
		if (genericWorkorder != null) {
			logger.info("Going to update GenericWorkorder Status woId {} ", genericWorkorder.getId());
			if (genericWorkorder.getStatus() != null) {
				try {
					String status = genericWorkorder.getStatus().name();
					if (!status.equalsIgnoreCase(Status.COMPLETED.name())
						&& !status.equalsIgnoreCase(Status.EXPIRED.name())
						&& !status.equalsIgnoreCase(Status.INPROGRESS.name())) {
						genericWorkorder.setStatus(GenericWorkorder.Status.INPROGRESS);
						iGenericWorkorderDao.update(genericWorkorder);
					}
				} catch (Exception exception) {
					logger.error("Unable to update GenericWorkorder woId {}   Exception {} ", genericWorkorder.getId(),Utils.getStackTrace(exception));
				}
			}
		} else {
			logger.error("Generic Workorder detail not found ");
		}

	}

	private void createTaskResult(StealthWOParameters parameters, StealthWOParameters response) {
		try {
			logger.info("Going to Check Status for Stealth parameter {} ", parameters);
			if (parameters != null && parameters.getWoId() != null && parameters.getStealthTaskId() != null) {

				GenericWorkorder genericWorkorder = iGenericWorkorderDao.findByPk(parameters.getWoId());
				if (genericWorkorder != null) {
					StealthTaskResult taskResult = getStealthTaskByWOIdAndTaskId(genericWorkorder.getGwoMeta(), parameters);
					if (taskResult != null) {
						logger.info("Already created ");
						response.setStealthTaskResultId(taskResult.getId());
						taskResult.setRemark(ForesightConstants.DASH);
						taskResult.setStatus(WOTestStatus.INPROGRESS.name());
						stealthTaskResultDao.update(taskResult);
					} else {
						logger.info("Going to Create for Stealth Task Result ");
						StealthTaskDetail taskDetail = stealthTaskDetailDao.findByPk(parameters.getStealthTaskId());
						taskResult = new StealthTaskResult();
						taskResult.setStatus(WOTestStatus.INPROGRESS.name());
						
						taskResult.setStealthTaskDetail(taskDetail);
						taskResult.setStartTime(new Date(parameters.getTimestamp()));
						taskResult.setCreationTime(new Date());
						taskResult.setModificationTime(new Date());
						
						setWoRecipeForLayer3Execution(genericWorkorder, taskResult);
						taskResult = stealthTaskResultDao.create(taskResult);
						response.setStealthTaskResultId(taskResult.getId());
						logger.info("Created stealth Result {} ", taskResult);

					}
				}
			}
		} catch (Exception exception) {
			logger.error("Error in Fetching Stealth WO Status for {}  exception {} ", parameters,
					Utils.getStackTrace(exception));
			throw new RestException(exception.getMessage());
		}
	}

	private void setWoRecipeForLayer3Execution(GenericWorkorder genericWorkorder, StealthTaskResult taskResult) {
		
		String layer3Execution = genericWorkorder.getGwoMeta().compute(NVConstant.LAYER3_EXECUTION_KEY,(k,v) -> v);
		String isProbe = genericWorkorder.getGwoMeta().compute(NVConstant.LAYER3_IS_PROBE,(k,v) -> v);
		if ((layer3Execution != null && layer3Execution.equalsIgnoreCase(ForesightConstants.TRUE_LOWERCASE)) || (isProbe!=null&& isProbe.equalsIgnoreCase(ForesightConstants.TRUE_LOWERCASE))) {
			WORecipeMapping woRecipe = new WORecipeMapping();
			String recipeId = genericWorkorder.getGwoMeta().compute(NVConstant.WO_RECIPE_ID, (k, v) -> v);
			if (recipeId != null && taskResult.getWoRecipeMapping() == null) {
				WORecipeMapping woRecipeMapping = woRecipeMappingDao.getWORecipeMappingById(Integer.valueOf(recipeId));
				woRecipe.setRecipe(woRecipeMapping.getRecipe());
				woRecipe.setGenericWorkorder(genericWorkorder);
				woRecipe.setStatus(com.inn.foresight.module.nv.workorder.model.WORecipeMapping.Status.INPROGRESS);
				woRecipe = woRecipeMappingDao.create(woRecipe);
				taskResult.setWoRecipeMapping(woRecipe);
			}
		}
	}

	private StealthTaskResult getStealthTaskByWOIdAndTaskId(Map<String,String> gwoMeta, StealthWOParameters parameters) {
		StealthTaskResult taskResult = null;
		String woFrequency=gwoMeta.get("woFrequency");
		String isProbe=gwoMeta.get("isProbe");
		try {

				if (woFrequency.equalsIgnoreCase("HOURLY")) {
					Integer hour = StealthUtils.getHourFromTimestamp(parameters.getTimestamp());
					logger.info("Hourly workorder  hour {} ", hour);
					taskResult = stealthTaskResultDao.getStealthTaskResultForHourlyWO(parameters.getWoId(),
							parameters.getStealthTaskId(), new Date(parameters.getTimestamp()), hour);
				} else if (woFrequency.equalsIgnoreCase("ONETIME")) {
					List<StealthTaskResult> stealthResultList = stealthTaskResultDao.getStealthResultListByTaskId(parameters.getStealthTaskId());
					if (CollectionUtils.isNotEmpty(stealthResultList)) {
						taskResult = stealthResultList.get(ForesightConstants.ZERO);
					}
				} else {
					taskResult = stealthTaskResultDao.getStealthTaskResultByWOAndStealthTask(parameters.getWoId(),
							parameters.getStealthTaskId(), new Date(parameters.getTimestamp()));
				}
			}
		catch(Exception e) {
			logger.info("No Result Found {} ",e.getMessage());
		}
		return taskResult;
	}
	
	private void updateResponseForCheckWOStatus(StealthWOParameters stealthTaskWrapper, StealthWOParameters response,
			StealthTaskDetail taskDetail) {
		if (taskDetail != null) {
			logger.info("Checking WO Status in task detail object: {}", taskDetail);
			GenericWorkorder workorder = taskDetail.getGenericWorkorder();
			if (workorder != null) {
				String status = workorder.getStatus().name();
				if (status != null) {
					if (!status.equalsIgnoreCase(Status.COMPLETED.name())
							&& !status.equalsIgnoreCase(Status.EXPIRED.name())) {
						response.setStatus(status);
						response.setResponse(ForesightConstants.SUCCESS_JSON);
					} else {
						response.setStatus(status);
						response.setResponse(ForesightConstants.FAILURE_JSON);
						response.setRemark(StealthConstants.WORKORDER_EXPIRED_OR_COMPLETED);
					}
				} else {
					logger.info("Invalid task status: {} ", status);
				}
			}
		} else {
			logger.info("Unable to : {}", stealthTaskWrapper);
		}
	}

	@Override
	@Transactional
	public String completeStealthWorkorder(Integer workorderId) {
		logger.info("Going to completeStealthWorkorder for Workorder :{}", workorderId);
		String responseToReturn = null;
		try {
			GenericWorkorder genericWorkorder = iGenericWorkorderDao.findByPk(workorderId);
			if (genericWorkorder != null) {
				Map<String, String> gwoMeta = genericWorkorder.getGwoMeta();
				gwoMeta.put(StealthConstants.KEY_WORKORDER_STATUS, StealthConstants.WO_STATUS_STOPPED);
				genericWorkorder.setGwoMeta(gwoMeta);
				genericWorkorder.setModificationTime(new Date());
				genericWorkorder.setStatus(Status.COMPLETED);
				iGenericWorkorderDao.update(genericWorkorder);
				sendNotificationToDevice(workorderId);
				responseToReturn = StealthConstants.SUCCESS_JSON;
			}
		} catch (Exception e) {
			responseToReturn = StealthConstants.FAILURE_JSON;
			logger.error("Exception in completeStealthWorkorder: {}", ExceptionUtils.getStackTrace(e));
		}
		return responseToReturn;
	}

	private void sendNotificationToDevice(Integer workorderId) {
		List<StealthTaskDetail> stealthTaskDetailList;
		try {
			stealthTaskDetailList = stealthTaskDetailDao.getStealthTasksByWorkorderId(workorderId);
			if (stealthTaskDetailList != null && !stealthTaskDetailList.isEmpty()) {
				createNotification(stealthTaskDetailList, workorderId);
			}
		} catch (Exception e) {
			logger.error("Exception in sendNotificationToDevice for stealth: {}", ExceptionUtils.getStackTrace(e));
		}

	}

	private void createNotification(List<StealthTaskDetail> stealthTaskDetailList, Integer workorderId) {
		Map<String, String> payLoad = new HashMap<>();
		
		List<PushNotification> list=new ArrayList();
		payLoad.put(NVWorkorderConstant.KEY_MODULE_NAME, ModuleName.NV_STEALTH.name());
		payLoad.put(NVWorkorderConstant.KEY_NOTIFICATION_MESSAGE, StealthConstants.WO_STATUS_STOPPED);
		payLoad.put(NVWorkorderConstant.KEY_NOTIFICATION_WORKORDERID, String.valueOf(workorderId));
		stealthTaskDetailList	.stream()
								.filter(st -> st != null && st.getNvDeviceData() != null)
								.forEach(st -> {
									PushNotification pushNotification = new PushNotification();
									pushNotification.setDeviceInfo(st	.getNvDeviceData()
																		.getDeviceInfo());
									pushNotification.setCreationTime(new Date());
									pushNotification.setModuleName(ModuleName.NV_STEALTH);
									if (st.getGenericWorkorder() != null && st	.getGenericWorkorder()
																				.getModificationTime() != null) {
										payLoad.put(NVWorkorderConstant.KEY_WO_COMPLETION_TIME,
												String.valueOf(st	.getGenericWorkorder()
																	.getModificationTime()
																	.getTime()));
									}
									pushNotification.setNotificationPayLoad(new Gson().toJson(payLoad));
									pushNotification.setStatus(
											com.inn.foresight.module.nv.pushnotification.model.PushNotification.Status.NEW);
									try {
										list.add(pushNotification);
										
									} catch (RestException e) {
										logger.error("Exception in createNotification for stealth: {}",
												ExceptionUtils.getStackTrace(e));
									}
								});
		
		iPushNotificationAsyncService.sendNotificationsAndUpdateStatus(list);
	}
	

	@Override
	public String getStealthKPISummary(Integer workorderId, Long stratTime,Long endTime,Integer zoomLevel)  {
		return sendRequestToDropwizard(getStealthKPISummaryURL(workorderId, stratTime,endTime,zoomLevel));
	}
	
	public String getStealthKPISummaryURL(Integer workorderId, Long stratTime,Long endTime, Integer zoomLevel) {
		return StealthUtils.getDropwizardUrl(
				StealthConstants.GET_STEALTH_KPI_URI.concat("workorderId=" + workorderId)
				.concat("&startTime=" + stratTime).concat("&endTime=" + endTime).concat("&zoomLevel="+zoomLevel));
	}
    
	private String sendRequestToDropwizard(String url) {
		String response = null;
		try {
			Duration duration = Duration.minutes(
					Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));

			
			logger.info("DropWizard URL :{}", url);
			response = StealthUtils	.sendHttpGetRequest(url, true, duration)
										.getString();
			logger.info("Done response from dropwizard");
		} catch (HttpException e1) {
			logger.error("HttpException in getStealthKPIByWorkorderId ", ExceptionUtils.getStackTrace(e1));
			throw new RestException(StealthConstants.EXCEPTION_PROBLEM_IN_DROPWIZARD);
		}
		return response;
	}

	@Override
	@Transactional
	public String fetchStealthTaskIdForDeviceReport(String deviceId, String workorderId) {
		try {
			Integer woId = null;
			if (!StringUtils.isBlank(workorderId) && !StringUtils.isBlank(deviceId)) {
				woId = Integer.valueOf(workorderId);

				NVDeviceData nvDevice = deviceDataDao.getDeviceByDeviceId(deviceId);
				StealthTaskDetail taskDetail = stealthTaskDetailDao.getStealthTaskByDeviceAndWOId(nvDevice.getId(),
						woId);
				logger.info("Stealth Task Id to return is: {}", taskDetail.getId());
				return taskDetail.getId().toString();
			} else {
				return null;
			}

		} catch (Exception e) {
			logger.error("Error in startStealthWOTask: {}", Utils.getStackTrace(e));
			throw new RestException(NVWorkorderConstant.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	@Override
	@Transactional
	public String updateStealthTaskDetailList(String encryptedTaskJsonList) {
		List<String> responseList = new ArrayList<>();
		logger.info("Going to update stealth task detail list encryptedTaskJsonList: {}", encryptedTaskJsonList);
		Gson gson = new Gson();
		try {
			List<StealthTaskDetailWrapper> taskList = extractStealthTaskListFromNVWoTestJson(encryptedTaskJsonList);
			if( !taskList.isEmpty()) {
			for (StealthTaskDetailWrapper stealthTaskWrapper : taskList) {
				responseList.add(updateStealthTaskDetails(stealthTaskWrapper));
			}
			}else {
				logger.info("Unable to extract stealth task list from input json: {}",taskList);
			}
		} catch (Exception e) {
			logger.error("Error in updateStealthTaskDetails: {}", ExceptionUtils.getStackTrace(e));
			logger.error("Final Response of updateStealthTaskDetailList: {}",gson.toJson(responseList));
			return AuthenticationCommonUtil.checkForValueEncryption(gson.toJson(responseList),null);
		}
		logger.error("Final Response of updateStealthTaskDetailList: {}",gson.toJson(responseList));
		return AuthenticationCommonUtil.checkForValueEncryption(gson.toJson(responseList),null);
	}

	
	private List<StealthTaskDetailWrapper> extractStealthTaskListFromNVWoTestJson(String encryptedTaskJsonList) {
		List<StealthTaskDetailWrapper> taskList = new ArrayList<>();
		List<DriveTestKPIWrapper> woTestWrapperList = decryptWOTestJsonList(encryptedTaskJsonList);
		if(woTestWrapperList != null && !woTestWrapperList.isEmpty()) {
		for(DriveTestKPIWrapper woTestWrapper : woTestWrapperList) {
			try {
			StealthTaskDetailWrapper stealthTaskWrapper = extractStealthTaskWrapperFromNVWoTestWrapper(woTestWrapper);
				taskList.add(stealthTaskWrapper);
			}catch(Exception e) {
				logger.error("Error while extracting task wrapper: {}",e.getMessage());
			}
		}
		}else {
			logger.info("WO Test Wrapper List is null or empty: {}",woTestWrapperList);
		}
		return taskList;
	}

	private List<DriveTestKPIWrapper> decryptWOTestJsonList(String encryptedWOTestJsonList)  {

		try {
			if (encryptedWOTestJsonList != null) {
				String woTestJsonList = AuthenticationCommonUtil.checkForValueDecryption(encryptedWOTestJsonList);
				logger.info("decryptedWOTestJsonList: {}", woTestJsonList);
				ObjectMapper mapper = new ObjectMapper();
				return mapper.readValue(woTestJsonList, new TypeReference<List<DriveTestKPIWrapper>>() {
				});
			} 
		} catch (Exception e) {
			logger.error("Error while decrypting stealth task json to Wrapper: {}", Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	@Override
	public String getKPIDistributionAndCount(String tableName, String type, String name, Long timeStamp,
			Integer enodeBId,Long endTime) {
		return sendRequestToDropwizard(getCEMDropwizardURL(StealthConstants.GET_KPI_DISTRIBUTION_AND_COUNT_URI,tableName, type,name,timeStamp,enodeBId,endTime));
	}

	

	@Override
	public String getCellWiseData(String tableName, String type, String name, Long timeStamp, Integer enodeBId,Long endTime)
			 {
		return sendRequestToDropwizard(getCEMDropwizardURL(StealthConstants.GET_CELL_WISE_DATA_URI,tableName, type,name,timeStamp,enodeBId,endTime));
	}

	@Override
	public String getTopEnodeBDetails(String tableName, String type, String name, Long timeStamp,Long endTime)  {
		return sendRequestToDropwizard(getCEMDropwizardURL(StealthConstants.GET_TOP_AND_WORST_ENODEB_URI,tableName, type,name,timeStamp,null,endTime));

	}

	private  String getCEMDropwizardURL(String uri,String tableName, String type, String name, Long timeStamp,
			Integer enodeBId,Long endTime) {
		String url=uri.concat("tableName=" + tableName);
		if(type!=null) {
			url=url.concat("&type="+type);
		}if(name!=null) {
			url=url.concat("&name="+name).trim().replaceAll(" ", "%20");
		}if(timeStamp!=null) {
			url=url.concat("&timeStamp="+timeStamp);
		}if(enodeBId!=null) {
			url=url.concat("&enodeBId="+enodeBId);
		}
		if(endTime!=null) {
			url=url.concat("&endTime="+endTime);
		}
		return StealthUtils.getDropwizardUrl(url);
	}

	@Override
	public String getDashboardDataFromHbase(List<String> hbaseColumns,String tableName,String rowkeyPrefix, String startDate,String endDate) throws HttpException {
		String columns = new Gson().toJson(hbaseColumns);
		logger.info("Column name  for dashboard {}", columns);
		StringEntity entity = new StringEntity(columns, ContentType.APPLICATION_JSON);
		List<String> valueList =  new ArrayList<>();
		valueList.add(tableName);
		valueList.add(rowkeyPrefix);
		valueList.add(startDate);
		valueList.add(endDate);
		String query = GenericMapUtils.createGenericQuery(valueList , ForesightConstants.TABLENAME,ForesightConstants.ROWKEY_PREFIX,
				ForesightConstants.START_DATE,ForesightConstants.END_DATE);
		
		logger.info("final URL is {} ",query);
		logger.info("request parameters for service {} , {}, {},{} ",tableName,rowkeyPrefix,startDate,endDate);
		String restCall = ConfigUtils.getString(ConfigEnum.DROPWIZARD_URL_FOR_STEALTH_KPI);
		restCall = restCall + "getDashboardDataFromHbase";
		String dropwizardUrl = new GenericMapServiceImpl().getDropwizardUrl(query, restCall);
		
		return new HttpPostRequest(dropwizardUrl, entity).getString();
	}
}
