package com.inn.foresight.module.nv.workorder.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.data.time.Quarter;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.bpmn.dao.IBPMNWOTemplateDao;
import com.inn.bpmn.dao.IBPMNWorkorderDao;
import com.inn.bpmn.dao.IBpmnTaskCandidateDao;
import com.inn.bpmn.dao.IBpmnTaskDao;
import com.inn.bpmn.model.BPMNWOTemplate;
import com.inn.bpmn.model.BPMNWorkorder;
import com.inn.bpmn.model.BPMNWorkorder.CompletionStatus;
import com.inn.bpmn.model.BpmnTask;
import com.inn.bpmn.model.BpmnTaskCandidate;
import com.inn.bpmn.service.IActivitiService;
import com.inn.bpmn.service.IBpmnTaskCandidateService;
import com.inn.bpmn.utils.constants.BpmnConstants;
import com.inn.bpmn.utils.enums.WOCategory;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpException;
import com.inn.commons.lang.NumberUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.geometry.gis.GIS3DPolygon;
import com.inn.commons.maps.geometry.gis.GISGeometry;
import com.inn.commons.unit.Duration;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.Constants;
import com.inn.foresight.core.generic.utils.DateUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.dao.INetworkElementDao;
import com.inn.foresight.core.infra.dao.IUnitDataDao;
import com.inn.foresight.core.infra.model.Unit;
import com.inn.foresight.core.infra.service.IBuildingDataService;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.wrapper.NETaskDetailWrapper;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;
import com.inn.foresight.core.infra.wrapper.SiteSelectionWrapper;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.core.report.dao.IReportTemplateDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.core.report.model.AnalyticsRepository.RepositoryType;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.core.report.model.ReportTemplate;
import com.inn.foresight.core.report.service.IAnalyticsRepositoryService;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.app.dao.IDeviceInfoDao;
import com.inn.foresight.module.nv.app.model.DeviceInfo;
import com.inn.foresight.module.nv.core.workorder.dao.IGWOMetaDao;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GWOMeta;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.core.workorder.service.IGenericWorkorderService;
import com.inn.foresight.module.nv.device.model.NVDeviceData;
import com.inn.foresight.module.nv.device.service.INVDeviceDataService;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HDFSDao;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification;
import com.inn.foresight.module.nv.pushnotification.model.PushNotification.ModuleName;
import com.inn.foresight.module.nv.pushnotification.service.IPushNotificationAsyncService;
import com.inn.foresight.module.nv.pushnotification.service.IPushNotificationService;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.service.ISiteDetailService;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.IWOFileDetailDao;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.dao.IWOUserMappingDao;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping.Status;
import com.inn.foresight.module.nv.workorder.model.WOUserMapping;
import com.inn.foresight.module.nv.workorder.recipe.model.Recipe;
import com.inn.foresight.module.nv.workorder.recipe.service.IRecipeService;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.RecipeWrapper;
import com.inn.foresight.module.nv.workorder.service.ICustomGeographyService;
import com.inn.foresight.module.nv.workorder.service.INVWorkorderService;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants.Acknowledgement;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants.AssigmentType;
import com.inn.foresight.module.nv.workorder.stealth.dao.IStealthTaskDetailDao;
import com.inn.foresight.module.nv.workorder.stealth.model.StealthTaskDetail;
import com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOParameters;
import com.inn.foresight.module.nv.workorder.utils.NVWorkorderUtils;
import com.inn.foresight.module.nv.workorder.wrapper.NVSiteWrapper;
import com.inn.foresight.module.nv.workorder.wrapper.NVWOUserWrapper;
import com.inn.foresight.module.nv.workorder.wrapper.NVWorkorderWrapper;
import com.inn.foresight.module.nv.workorder.wrapper.WORecipeWrapper;
import com.inn.product.security.utils.AuthenticationCommonUtil;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.model.SystemConfiguration;
import com.inn.product.um.geography.dao.GeographyL1Dao;
import com.inn.product.um.geography.dao.GeographyL2Dao;
import com.inn.product.um.geography.dao.GeographyL3Dao;
import com.inn.product.um.geography.dao.GeographyL4Dao;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.user.dao.UserDao;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

/** The Class NVWorkorderServiceImpl. */
@Service("NVWorkorderServiceImpl")
public class NVWorkorderServiceImpl extends NVWorkorderUtils implements INVWorkorderService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NVWorkorderServiceImpl.class);

	/** Instance of WORecipeMappingDaoImpl. */
	@Autowired
	private IWORecipeMappingDao woRecipeMappingDao;

	/** Instance of GenericWorkorderServiceImpl. */
	@Autowired
	private IGenericWorkorderService genericWorkorderService;

	/** Instance of GenericWorkorderDaoImpl. */
	@Autowired
	private IGenericWorkorderDao genericWorkorderDao;

	/** Instance of ActivitiServiceImpl. */
	@Autowired
	private IActivitiService activitiService;

	/** The report template dao. */
	@Autowired
	private IReportTemplateDao reportTemplateDao;

	/** The analytics repository service. */
	@Autowired
	private IAnalyticsRepositoryService analyticsRepositoryService;

	/** Instance of BPMNWOTemplateDaoImpl. */
	@Autowired
	private IBPMNWOTemplateDao bpmnWOTemplateDao;

	/** Instance of BpmnTaskDaoImpl. */
	@Autowired
	private IBpmnTaskDao bpmnTaskDao;

	/** Instance of BpmnWorkorderDaoImpl. */
	@Autowired
	private IBPMNWorkorderDao bpmnWorkorderDao;

	/** Instance of BpmnTaskCandidateDaoImpl. */
	@Autowired
	private IBpmnTaskCandidateDao bpmnTaskCandidateDao;

	/** Instance of BpmnTaskCandidateServiceImpl. */
	@Autowired
	private IBpmnTaskCandidateService bpmnTaskCandidateService;

	/** Instance of UserDaoImpl. */
	@Autowired
	private UserDao userDao;

	/** The custom geography service. */
	@Autowired
	private ICustomGeographyService customGeographyService;

	/** The push notification service. */
	@Autowired
	private IPushNotificationService pushNotificationService;

	/** The device info dao. */
	@Autowired
	private IDeviceInfoDao deviceInfoDao;

	/** The generic map service. */
	@Autowired
	private IGenericMapService genericMapService;

	/** The geography L 1 dao. */
	@Autowired
	private GeographyL1Dao geographyL1Dao;

	/** The geography L 2 dao. */
	@Autowired
	private GeographyL2Dao geographyL2Dao;

	/** The geography L 3 dao. */
	@Autowired
	private GeographyL3Dao geographyL3Dao;

	/** The geography L 4 dao. */
	@Autowired
	private GeographyL4Dao geographyL4Dao;

	/** The building data service. */
	@Autowired
	private IBuildingDataService buildingDataService;

	/** The i system configuration dao. */
	@Autowired
	private SystemConfigurationDao iSystemConfigurationDao;

	/** The recipe service. */
	@Autowired
	private IRecipeService recipeService;

	/** The wo file detail dao. */
	@Autowired
	private IWOFileDetailDao woFileDetailDao;

	/** The nv device data service. */
	@Autowired
	private INVDeviceDataService nvDeviceDataService;

	@Autowired
	private IStealthTaskDetailDao stealthTaskDetailDao;

	@Autowired
	private IUnitDataDao iUnitDataDao;

	@Autowired
	private IWOFileDetailDao iWOFileDetailDao;
	@Autowired
	private INVLayer3HDFSDao nvLayer3HDFSDao;

	@Autowired
	IPushNotificationAsyncService pushNotificationAsyncService;

	@Autowired
	private ISiteDetailService siteDetailService;

	@Autowired
	private IWOUserMappingDao woUserMappingDao;

	@Autowired
	private INetworkElementDao networkElementDao;

    @Autowired
    private IGWOMetaDao igwoMetaDao;
    
    @Autowired
    SystemConfigurationDao systemConfigurationDao;
    

	/**
	 * Creates Work Order for each user for given Template Type and Recipe details
	 * from Web and assigns the to given Users.
	 *
	 * @param wrapper {@link NVWorkorderWrapper} wrapper that holds detail for
	 *                workorder creation
	 * @return Success Message if Workorder created, Failure Message otherwise
	 */
	@Override
	@Transactional
	public String createWorkorderFromWeb(NVWorkorderWrapper wrapper) {
		Integer workorderSuccessCount = createWorkorderForUser(wrapper, true).size();
		if (workorderSuccessCount != null && !workorderSuccessCount.equals(NumberUtils.INTEGER_ZERO)) {
			return SUCCESS_JSON;
		}
		return FAILURE_JSON;
	}

	/**
	 * Creates NV Workorder for Stealth and Recipe details from Web assigns them to
	 * given Device.
	 *
	 * @param wrapper {@link NVWorkorderWrapper} wrapper that holds detail for
	 *                workorder creation
	 * @return Success Message if Workorder created, Failure Message otherwise
	 */
	@Override
	@Transactional
	public String createWorkorderForDevice(NVWorkorderWrapper wrapper) {
		try {
			logger.info("Going to create Stealth WO for Device {}", wrapper);
			GenericWorkorder workorder = createGenericWorkorder(wrapper,null);
			logger.info("Going to createWORecipeMapping {}", workorder);

			createWORecipeMapping(workorder, wrapper.getRecipeList());
			logger.info("Going to createNotificationForAllDevices");

			createNotificationForAllDevices(wrapper.getDeviceIdList(), workorder, wrapper.getGeoWoMetaMap(), true,false);

		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			return FAILURE_JSON;
		}
		return SUCCESS_JSON;
	}

	/**
	 * Creates the notification for all devices.
	 *
	 * @param deviceIdList the device id list
	 * @param isPostLogin

	 * @throws RestException the rest exception
	 * @throws DaoException
	 */
	private void createNotificationForAllDevices(List<Integer> deviceIdList, GenericWorkorder workorder,
			Map<String, String> gWOMeta, Boolean isNotificationRequired, Boolean isPostLogin) {
		List<NVDeviceData> deviceList = nvDeviceDataService.getNVDeviceDataByDeviceInfo(deviceIdList);
		if (deviceList != null && !deviceList.isEmpty()) {

			SystemConfiguration sysConf = iSystemConfigurationDao
					.getSystemConfigurationDetailForReport("DEFAULT_OPERATOR", "NVProfileData");

			if (sysConf != null && sysConf.getValue() != null && gWOMeta.get(StealthConstants.IS_RANDOMIZATION) != null
					&& gWOMeta.get(StealthConstants.GWO_FREQUENCY_KEY) != null
					&& StealthConstants.WO_TYPE_ONETIME
							.equalsIgnoreCase(gWOMeta.get(StealthConstants.GWO_FREQUENCY_KEY))
					&& Boolean.valueOf(gWOMeta.get(StealthConstants.IS_RANDOMIZATION))) {
				createStealthSplittedWO(workorder, gWOMeta, isNotificationRequired, deviceList, sysConf,isPostLogin);
			} else if (sysConf != null && sysConf.getValue() != null) {
				createStealthWO(workorder, gWOMeta, isNotificationRequired, deviceList, sysConf,isPostLogin);
			} else {
				logger.info("Default operator not found ");
			}
		} else {
			logger.info("Device data not available , deviceList: {}", deviceList);
		}
	}

	private void createStealthSplittedWO(GenericWorkorder workorder, Map<String, String> gWOMeta,
			Boolean isNotificationRequired, List<NVDeviceData> list, SystemConfiguration sysConf,Boolean isPostLogin) {

		logger.info("Going to allocate ramdomized execution time");
		String operator = sysConf.getValue().trim();
		List<PushNotification> pushNotificationList = new ArrayList();
		Integer splitSize = Integer.valueOf(gWOMeta.get(StealthConstants.SPLIT_KEY));
		Map<Long, List<NVDeviceData>> nvDeviceMap = getNVDeviceSplittedList(list, splitSize, gWOMeta);
		for (Entry<Long, List<NVDeviceData>> nvDeviceList : nvDeviceMap.entrySet()) {
			Long startTime = nvDeviceList.getKey();
			List<NVDeviceData> deviceSubList = nvDeviceList.getValue();
			for (NVDeviceData deviceData : deviceSubList) {
				try {
					logger.error("Going to create deviceData task {} ", deviceData);
					if (operator.equalsIgnoreCase(deviceData.getOperator())) {
						Map<String, String> payLoad = getDeviceWONotificationPayload(
								deviceData.getDeviceInfo().getDeviceId(), workorder.getId(),
								workorder.getTemplateType().name(),
								NVWorkorderConstant.DEVICE_WORKORDER_NOTIFICATION_MESSAGE);
						StealthTaskDetail taskDetail = createStealthWOTask(deviceData, workorder, startTime,isPostLogin);
						payLoad.put(NVWorkorderConstant.KEY_NOTIFICATION_STEALTH_TASK,
								String.valueOf(taskDetail.getId()));
						payLoad.put(StealthConstants.IS_ACKNOWLEDGED, gWOMeta.get(StealthConstants.IS_ACKNOWLEDGED));
						payLoad.put(StealthConstants.IS_DETAILS, gWOMeta.get(StealthConstants.IS_DETAILS));
						pushNotificationList.add(createNotification(deviceData.getDeviceInfo(),
								workorder.getTemplateType(), new Gson().toJson(payLoad), isNotificationRequired));
					} else {
						logger.info("WO not created for Device as Operator is not correct deviceData {} ",
								deviceData.getId());
					}
				} catch (Exception e) {
					logger.error("Unable to create  Stealth Task for device {} ", Utils.getStackTrace(e));
				}
			}
		}
		pushNotificationAsyncService.sendNotificationsAndUpdateStatus(pushNotificationList);
	}

	private Map<Long, List<NVDeviceData>> getNVDeviceSplittedList(List<NVDeviceData> deviceList, Integer splitSize,
			Map<String, String> gWOMeta) {
		Long startTime = Long.valueOf(gWOMeta.get(StealthConstants.START_TIME));
		Integer index = 0;
		Map<Long, List<NVDeviceData>> spllitedMap = new HashMap<>();
		List<Long> randomizedTimeList = getSplittedStartTimeStamp(gWOMeta);
		if (deviceList.size() < splitSize) {
			spllitedMap.put(startTime, deviceList);
		} else {
			List<List<NVDeviceData>> partitionList = findPartitionedList(deviceList.size(), deviceList, splitSize);
			logger.info("Found the splitted device List size {}", partitionList.size());
			for (List<NVDeviceData> innerList : partitionList) {
				List<NVDeviceData> subList = new ArrayList<>(innerList);
				if (index >= splitSize) {
					index = 0;
				}
				if (spllitedMap.containsKey(randomizedTimeList.get(index))) {
					List<NVDeviceData> list = spllitedMap.get(randomizedTimeList.get(index));
					list.addAll(subList);
					spllitedMap.put(randomizedTimeList.get(index), list);
				} else {
					spllitedMap.put(randomizedTimeList.get(index), subList);
				}
				index++;
			}
		}
		logger.info("Found the splitted device list size {}", spllitedMap.size());
		return spllitedMap;
	}

	private List<List<NVDeviceData>> findPartitionedList(int deviceListSize, List<NVDeviceData> deviceList,
			Integer splitSize) {
		logger.info("Found deviceList size {} and splitted Size {}", deviceListSize, splitSize);
		if (splitSize > 0 && deviceListSize % splitSize != 0) {
			int a = 0;
			if (deviceListSize > splitSize) {
				a = splitSize;
			} else {
				a = deviceListSize + 1;
			}
			return Lists.partition(deviceList, a);
		} else if (deviceListSize == splitSize) {
			return Lists.partition(deviceList, deviceListSize);
		} else {
			return Lists.partition(deviceList, splitSize);
		}
	}

	public List<Long> getSplittedStartTimeStamp(Map<String, String> gWOMeta) {
		Long startTime = Long.valueOf(gWOMeta.get(StealthConstants.START_TIME));
		Long executionTime = Long.valueOf(gWOMeta.get(StealthConstants.EXECUTION_TIME));
		Integer splits = Integer.valueOf(gWOMeta.get(StealthConstants.SPLIT_KEY));
		List<Long> splittedStartTime = new ArrayList<>();
		if (Utils.hasValue(executionTime) && Utils.hasValue(splits) && executionTime > startTime
				&& splits > ForesightConstants.ZERO) {
			Long timeLapse = (executionTime - startTime) / splits;
			for (int i = 0; i < splits; i++) {
				splittedStartTime.add(startTime);
				startTime = startTime + timeLapse;
			}
		} else {
			logger.info("Found execution time {} less than starttime {}", executionTime, startTime);
		}
		logger.info("Found the randomization timestamp list size {} and data {}", splittedStartTime.size(),
				splittedStartTime);
		return splittedStartTime;
	}

	private void createStealthWO(GenericWorkorder workorder, Map<String, String> gWOMeta,
			Boolean isNotificationRequired, List<NVDeviceData> list, SystemConfiguration sysConf, Boolean isPostLogin) {

		logger.info("Going to allocate normal execution time");

		String operator = sysConf.getValue().trim();
		List<PushNotification> pushNotificationList = new ArrayList();
		for (NVDeviceData deviceData : list) {
			try {
				logger.info("Going to create deviceData task {} ", deviceData);
				if (deviceData.getOperator().contains(operator)) {
					Map<String, String> payLoad = getDeviceWONotificationPayload(
							deviceData.getDeviceInfo().getDeviceId(), workorder.getId(),
							workorder.getTemplateType().name(),
							NVWorkorderConstant.DEVICE_WORKORDER_NOTIFICATION_MESSAGE);
					StealthTaskDetail taskDetail = createStealthWOTask(deviceData, workorder, null,isPostLogin);
					payLoad.put(NVWorkorderConstant.KEY_NOTIFICATION_STEALTH_TASK, String.valueOf(taskDetail.getId()));
					payLoad.put(StealthConstants.IS_ACKNOWLEDGED, gWOMeta.get(StealthConstants.IS_ACKNOWLEDGED));
					payLoad.put(StealthConstants.IS_DETAILS, gWOMeta.get(StealthConstants.IS_DETAILS));

					List<NVWorkorderWrapper> nvWorkorderWrappers = new ArrayList<>();
					StealthWOParameters stealthWOParameters = new StealthWOParameters();
					stealthWOParameters.setDeviceId(deviceData.getDeviceInfo().getDeviceId());
					fetchStealthWorkorderByWOId(nvWorkorderWrappers, stealthWOParameters, workorder.getId());
					payLoad.put("wrapper", AuthenticationCommonUtil.checkForValueEncryption(new Gson().toJson(nvWorkorderWrappers), null));
					pushNotificationList.add(createNotification(deviceData.getDeviceInfo(), workorder.getTemplateType(),
							new Gson().toJson(payLoad), isNotificationRequired));
				} else {
					logger.info("WO not created for Device as Operator is not correct deviceData {} ",
							deviceData.getId());
				}
			} catch (Exception e) {
				logger.error("Unable to create  Stealth Task for device {} ", Utils.getStackTrace(e));
			}
		}
		pushNotificationAsyncService.sendNotificationsAndUpdateStatus(pushNotificationList);
	}

	private void addWorkorderWrapperIntoPayLoad(GenericWorkorder workorder, NVDeviceData deviceData,
			Map<String, String> payLoad) {
		List<StealthWOParameters> stealthWOParametersList = new ArrayList<>();
		StealthWOParameters stealthWOParameters = new StealthWOParameters();
		stealthWOParameters.setWoId(workorder.getId());
		stealthWOParameters.setDeviceId(deviceData.getDeviceInfo().getDeviceId());
		stealthWOParametersList.add(stealthWOParameters);
		List<NVWorkorderWrapper> fetchStealthWorkOrder = fetchStealthWorkOrder(stealthWOParametersList);
		if (fetchStealthWorkOrder != null) {
			payLoad.put(NVWorkorderConstant.WORKORDER_WRAPPER, new Gson().toJson(fetchStealthWorkOrder));
		}
	}

	private StealthTaskDetail createStealthWOTask(NVDeviceData deviceData, GenericWorkorder workorder,
			Long splittedCreationTime, Boolean isPostLogin) {
		try {
			logger.info("Going to create entry in StealthTaskDetail");
			StealthTaskDetail stealthTaskDetail = new StealthTaskDetail();
			stealthTaskDetail.setNvDeviceData(deviceData);
			stealthTaskDetail.setEndTime(workorder.getDueDate());
			stealthTaskDetail.setGenericWorkorder(workorder);
			stealthTaskDetail.setAcknowledgement(StealthConstants.STATUS_PENDING);
			if (splittedCreationTime != null) {
				stealthTaskDetail.setStartTime(new Date(splittedCreationTime));
			} else {
				stealthTaskDetail.setStartTime(workorder.getStartDate());
			}
			if (isPostLogin) {
				stealthTaskDetail.setPostLogin(ForesightConstants.TRUE);
			}
			stealthTaskDetail.setCreationTime(new Date());
			stealthTaskDetail.setModificationTime(new Date());
			stealthTaskDetail = stealthTaskDetailDao.create(stealthTaskDetail);
			logger.info("Entry created in StealthTaskDetail");

			return stealthTaskDetail;
		} catch (Exception exception) {
			logger.error("Unable to create Stealth task for {} Exception {}", deviceData,
					Utils.getStackTrace(exception));
			throw new RestException("Unable to create Stealth task");
		}
	}

	/**
	 * Creates Work Order for given Template Type and Recipe details from Mobile
	 * assigned to current User logined from Mobile.
	 *
	 * @param strWorkorder : Encrypted String holds the wrapper with work order
	 *                     details
	 * @return Success Message if Workorder created, Failure Message otherwise
	 * @throws RestException the rest exception
	 */
	@Override
	@Transactional
	public NVWorkorderWrapper createWorkorderFromMobile(String strWorkorder) {
		try {
			NVWorkorderWrapper wrapper = new Gson()
					.fromJson(AuthenticationCommonUtil.checkForValueDecryption(strWorkorder), NVWorkorderWrapper.class);
			logger.info("wrapper for createWorkorderFromMobile : {} ", wrapper);
			prepareWOCreationRequest(wrapper);
			NVWOUserWrapper woUser = new NVWOUserWrapper();
			woUser.setRecipeList(createAndGetRecipes(wrapper));
			logger.info("Getting Workorder wrapper for mobile {}", new Gson().toJson(wrapper));
			return getWorkorderDetailsByWOId(
					createSingleWorkorder(UserContextServiceImpl.getUserInContext(), woUser, wrapper, false), null);
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Creates the workorder for user.
	 *
	 * @param wrapper the wrapper
	 * @return the list
	 */
	@Override
	public List<Integer> createWorkorderForUser(NVWorkorderWrapper wrapper, Boolean isNotificationRequired) {
		List<Integer> workorderIdList = new ArrayList<>();
		logger.info("wrapper getUserList {}", wrapper.getUserList().toArray());
		for (NVWOUserWrapper woUser : wrapper.getUserList()) {
			try {
				User user = userDao.findByUserName(woUser.getUserName());
				if (user != null) {
					workorderIdList.add(createSingleWorkorder(user, woUser, wrapper, isNotificationRequired));
				}
			} catch (Exception e) {
				logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			}
		}
		return workorderIdList;
	}

	/**
	 * Creates the a single Work Order for given User
	 *
	 * @param user    : the user to which work order will be assigned
	 * @param woUser  : holds drive routes or staionary point for respected user
	 * @param wrapper : NVworkorder wrapper holds details of Work Order to be
	 *                created
	 * @return Generic Workorder id of work order created
	 * @throws RestException the rest exception
	 * @throws DaoException  the dao exception
	 */
	private Integer createSingleWorkorder(User user, NVWOUserWrapper woUser, NVWorkorderWrapper wrapper,
			Boolean isNotificationRequired) {
		GenericWorkorder workorder = createGenericWorkorder(wrapper,user);
		logger.info("GenericWorkorder created for workorderID : {}", workorder.getWorkorderId());
		createBPMNWorkorder(workorder.getWorkorderId(), workorder.getTemplateType(), user);
		createWORecipeMapping(workorder, woUser.getRecipeList());
		try {
			sendNotificationToDevice(user, isNotificationRequired, workorder);
		} catch (NoResultException e) {
			logger.info(AppConstants.NO_RECORD_FOUND_LOGGER);
		} catch (Exception e) {
			logger.error("Unable to create notification with trace : {}", ExceptionUtils.getStackTrace(e));
		}
		return workorder.getId();
	}

	@Override
	public void sendNotificationToDevice(User user, Boolean isNotificationRequired, GenericWorkorder workorder) {
		Map<String, String> payLoad = getWONotificationPayload(workorder.getTemplateType().name(),
				NVWorkorderUtils.getWorkorderMessageForUser(workorder.getTemplateType(), user.getFirstName()));

		PushNotification notification = createNotification(deviceInfoDao.getDeviceInfoByUser(user.getUserid()),
				workorder.getTemplateType(), new Gson().toJson(payLoad), isNotificationRequired);

		pushNotificationAsyncService.sendNotificationAndUpdateStatus(notification);

	}

	/**
	 * Creates BPMN Workorder and Task with given details and assigned it to given
	 * User.
	 *
	 * @param workorderName : Name of workorder created in GenericWorkorder
	 * @param templateType  Ex(SSVT, OPEN_DRIVE ...)
	 * @param user          : User to which workorder will be assigned
	 * @throws DaoException the dao exception
	 */
	private void createBPMNWorkorder(String workorderName, TemplateType templateType, User user) {
		BPMNWorkorder workorder = new BPMNWorkorder();

		BPMNWOTemplate woTemplate = bpmnWOTemplateDao.getDeployedBPMWOTemplateByNameAndCategory(templateType.getValue(),
				WOCategory.NV_WORKORDER.getValue());

		workorder.setWorkorderNo(workorderName);
		workorder.setBpmnWOTemplate(woTemplate);
		workorder.setBpmnWOCategory(woTemplate.getBpmnWOCategory());
		workorder.setProgressPCT(NumberUtils.INTEGER_ZERO);
		workorder.setWoCategory(WOCategory.NV_WORKORDER);
		workorder.setDeleted(false);

		List<BPMNWorkorder> list = new ArrayList<>();
		list.add(workorder);
		activitiService.invokeBPMNWorkorderMultipleProcess(list, getWorkorderMeta(user.getUserName()));
	}

	/**
	 * Creates the notification of work order just created for given template type
	 * with given payload message.
	 *
	 * @param deviceInfo    the device info
	 * @param templateType  the template type
	 * @param payLoad       the pay load
	 */
	private PushNotification createNotification(DeviceInfo deviceInfo, TemplateType templateType, String payLoad,
			Boolean isNotificationRequired) {
		if (isNotificationRequired) {
			logger.info("Going to create Notification of Workorder Created for Template Type {} ", templateType);
			try {
				PushNotification notification = new PushNotification();
				notification.setDeviceInfo(deviceInfo);
				notification.setStatus(com.inn.foresight.module.nv.pushnotification.model.PushNotification.Status.NEW);
				notification.setCreationTime(new Date());
				notification.setModuleName(ModuleName.valueOf(templateType.toString()));
				notification.setNotificationPayLoad(payLoad);
				return pushNotificationService.createNotification(notification);
			} catch (Exception e) {
				logger.error("Notification can not be created. Error : {}", e.getMessage());
			}

		} else {
			logger.info("Notification reqiured is false for device info  {} ",
					deviceInfo != null ? deviceInfo.getDeviceId() : null);

		}
		return null;
	}

	/**
	 * Inserts WORecipeMapping One-By-One.
	 *
	 * @param workorder         the workorder
    the custom geography id
	 * @throws DaoException the dao exception
	 */
	private List<WORecipeMapping> createWORecipeMapping(GenericWorkorder workorder, List<RecipeWrapper> recipeList) {
		List<RecipeWrapper> recipeWrapperList = new ArrayList<>();
		List<WORecipeMapping> recipeMappingList = new ArrayList<>();
		for (RecipeWrapper recipeWrapper : recipeList) {
			WORecipeMapping woRecipeMapping = new WORecipeMapping();
			woRecipeMapping.setRecipe(getRecipeFromWrapper(recipeWrapper));
			woRecipeMapping.setGenericWorkorder(workorder);
			woRecipeMapping.setStatus(Status.NOT_STARTED);
			woRecipeMapping.setMaxTime(recipeWrapper.getMaxTime());
			woRecipeMapping.setWaitTime(recipeWrapper.getWaitTime());
			woRecipeMapping.setIterationCount(recipeWrapper.getIterationCount());
			woRecipeMapping.setFileProcessStatus(WORecipeMapping.ProcessStatus.NOT_STARTED);
			WORecipeMapping recipeMapping = woRecipeMappingDao.create(woRecipeMapping);
			if (workorder.getTemplateType().equals(GenericWorkorder.TemplateType.SCANNING_RECEIVER)) {
				recipeWrapper.setScannerRequest(recipeWrapper.getScannerRequest());
				recipeWrapper.setChannelList(recipeWrapper.getChannelList());
			}
			recipeWrapper.setWoRecipeMappingId(recipeMapping.getId());
			recipeWrapperList.add(recipeWrapper);
			recipeMappingList.add(recipeMapping);
		}
		updateRecipeMeta(recipeWrapperList, workorder);
		logger.info("Workorder Recipe Mapping created.");
		return recipeMappingList;
	}

	/**
	 * Update Recipe Level Meta Data in GWOMeta
	 *
	 * @param list              : List of RecipeWrapper contains meta data detail
	 *                          like PCI,Unit Id, Operator etc.
	 * @param workorder         : Object of GenericWorkorder
	 * @throws DaoException : the dao exception
	 */
	private void updateRecipeMeta(List<RecipeWrapper> list, GenericWorkorder workorder) {
		TemplateType templateType = workorder.getTemplateType();
		Map<String, String> metaMap = workorder.getGwoMeta();
		setRecipeMeta(templateType, metaMap, list);
		setCustomGeographyMeta(metaMap, list);
		if (templateType.compareTo(TemplateType.NV_STEALTH) == 0) {
			metaMap.put(NVConstant.WO_RECIPE_ID, String.valueOf(list.get(0).getWoRecipeMappingId()));
		}
		workorder.setGwoMeta(metaMap);
		genericWorkorderDao.update(workorder);
	}

	/**
	 * Creates GenericWorkorder Object and Insert.
	 *
	 * @param wrapper : Object of NVWorkorderWrapper contain workorder basic details
	 * @param assignedUser 
	 * @return Newly Created GenericWorkOrder Objects
	 * @throws RestException : the rest exception
	 * @throws DaoException
	 */
	private GenericWorkorder createGenericWorkorder(NVWorkorderWrapper wrapper, User assignedUser) {
		GenericWorkorder workorder = new GenericWorkorder();
		workorder.setWorkorderName(wrapper.getWorkorderName());
		workorder.setDescription(wrapper.getDescription());
		workorder.setRemark(wrapper.getRemark());
		workorder.setWorkorderId(getWorkorderId(wrapper.getWorkorderId()));
		workorder.setTemplateType(wrapper.getTemplateType());
		if(wrapper.getEndDate()!=null) {
		workorder.setDueDate(new Date(wrapper.getEndDate()));
		}
		workorder.setStartDate(new Date(wrapper.getStartDate()));
		workorder.setGwoMeta(wrapper.getGeoWoMetaMap());
		if (wrapper.getAssignedBy() != null) {
			User user = userDao.findByUserName(wrapper.getAssignedBy());
			workorder.setCreatedBy(user);
			workorder.setModifiedBy(user);
		}

		if (assignedUser != null) {
			try {
				workorder.setAssignedTo(assignedUser);
			} catch (Exception exception) {
				logger.error("Exception in createGenericWorkorder {}", ExceptionUtils.getStackTrace(exception));
			}
		} else if (wrapper.getAssignedTo() != null) {
			try {
				User user = userDao.findByUserName(wrapper.getAssignedTo());
				workorder.setAssignedTo(user);
			} catch (Exception exception) {
				logger.error("Exception in createGenericWorkorder {}", ExceptionUtils.getStackTrace(exception));
			}
		}
		setGeography(workorder, wrapper.getGeoWoMetaMap());
		GenericWorkorder createWorkorder = genericWorkorderService.createWorkorder(workorder);

		if (wrapper.getGeoWoMetaMap().containsKey(NVWorkorderConstant.MULTI_USER)) {
			String multiUserStr = wrapper.getGeoWoMetaMap().get(NVWorkorderConstant.MULTI_USER);
			if (!StringUtils.isEmpty(multiUserStr)) {
				List<String> userList = new Gson().fromJson(multiUserStr, new TypeToken<List<String>>() {
				}.getType());
				userList.forEach(userName -> {
					User user = userDao.findByUserName(userName);
					WOUserMapping woUserMapping = new WOUserMapping();
					woUserMapping.setGenericWorkorder(createWorkorder);
					woUserMapping.setUser(user);
					woUserMappingDao.create(woUserMapping);
				});
			}
		}

		return createWorkorder;
	}

	private void setGeography(GenericWorkorder workorder, Map<String, String> geoWoMetaMap) {
		logger.info("Going to get Geography for GenericWorkorder geography type: {} ",
				geoWoMetaMap.get(GEOGRAPHY_TYPE));
		if (geoWoMetaMap.get(GEOGRAPHY_TYPE) != null) {
			if (geoWoMetaMap.get(GEOGRAPHY_TYPE).equalsIgnoreCase(GEOGRAPHYL1)) {
				GeographyL1 geographyL1 = geographyL1Dao.findByPk(parseInt(geoWoMetaMap.get(GEOGRAPHY_ID)));
				if (geographyL1 != null) {
					workorder.setGeographyl1(geographyL1);
				} else {
					throw new RestException(INVALID_GEOGRAPHY);
				}
			} else if (geoWoMetaMap.get(GEOGRAPHY_TYPE).equalsIgnoreCase(GEOGRAPHYL2)) {
				GeographyL2 geographyL2 = geographyL2Dao.findByPk(parseInt(geoWoMetaMap.get(GEOGRAPHY_ID)));
				workorder.setGeographyl2(geographyL2);
				if (geographyL2 != null) {
					workorder.setGeographyl1(geographyL2.getGeographyL1());
				} else {
					throw new RestException(INVALID_GEOGRAPHY);
				}
			} else if (geoWoMetaMap.get(GEOGRAPHY_TYPE).equalsIgnoreCase(GEOGRAPHYL3)) {
				GeographyL3 geographyL3 = geographyL3Dao.findByPk(parseInt(geoWoMetaMap.get(GEOGRAPHY_ID)));
				workorder.setGeographyl3(geographyL3);
				if (geographyL3 != null) {
					workorder.setGeographyl2(geographyL3.getGeographyL2());
					if (geographyL3.getGeographyL2() != null) {
						workorder.setGeographyl1(geographyL3.getGeographyL2().getGeographyL1());
					}
				} else {
					throw new RestException(INVALID_GEOGRAPHY);
				}
			} else if (geoWoMetaMap.get(GEOGRAPHY_TYPE).equalsIgnoreCase(GEOGRAPHYL4)) {
				GeographyL4 geographyL4 = geographyL4Dao.findByPk(parseInt(geoWoMetaMap.get(GEOGRAPHY_ID)));
				workorder.setGeographyl4(geographyL4);
				if (geographyL4 != null) {
					workorder.setGeographyl3(geographyL4.getGeographyL3());
					if (geographyL4.getGeographyL3() != null) {
						workorder.setGeographyl2(geographyL4.getGeographyL3().getGeographyL2());
						if (geographyL4.getGeographyL3().getGeographyL2() != null) {
							workorder.setGeographyl1(geographyL4.getGeographyL3().getGeographyL2().getGeographyL1());
						}

					}
				} else {
					throw new RestException(INVALID_GEOGRAPHY);
				}
			} else {
				throw new RestException("Invalid Geography Level");
			}
		}
	}

	/**
	 * Gets a new Workorder Id by partial workorder id making it unique by attaching
	 * a number to partial workorder Id.
	 *
	 * @param partialWorkorderId : the partial workorder id
	 * @return Newly created Unique Work Order Id
	 * @throws RestException the rest exception
	 * @Example Provided WoId : WO-NV Found Max WoId : WO-NV-10 New WO ID : WO-NV-11
	 */
	private String getWorkorderId(String partialWorkorderId) {
		Integer number = getMaximumNumberFromList(genericWorkorderService.getWorkorderIdListByWOId(partialWorkorderId),
				partialWorkorderId);
		StringBuilder builder = new StringBuilder();
		builder.append(partialWorkorderId).append(Symbol.HYPHEN).append(number + NumberUtils.INTEGER_ONE);
		logger.info("builderid is {}",builder.toString());
		return builder.toString();
	}

	/**
	 * Gets All Workorder Created For Provided User or workorder Id.
	 *
	 * @param strWorkorderId  the str workorder id
	 * @param templateType    the template type
	 * @param strModifiedTime the str modified time
	 * @return Collection<WORecipeWrapper>
	 * @throws RestException the rest exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Collection<NVWorkorderWrapper> getWorkorderDetailsByUser(String strWorkorderId, String templateType,
			String strModifiedTime) {
		logger.info("Inside Method getWorkorderDetailsByUser()");
		try {
			String bpmnWOTemplateName = bpmnWOTemplateDao
					.getBPMNWOTemplateByName(TemplateType.valueOf(templateType).getValue()).getName();
			List<BpmnTask> bpmnTaskList = bpmnTaskCandidateDao.getBPMNTaskListByAssignedUserId(
					UserContextServiceImpl.getUserInContext().getUserid(), com.inn.bpmn.model.BpmnTask.Status.Completed,
					bpmnWOTemplateName);
			List<String> workorderIdList = getWorkorderIdList(bpmnTaskList);
			if (!workorderIdList.isEmpty()) {
				List<WORecipeMapping> woRecipeMappingList = woRecipeMappingDao.getRecipeWOByWOIdList(workorderIdList,
						Arrays.asList(templateType), parseLong(strModifiedTime), parseInt(strWorkorderId));

				return getNVWorkorderMap(woRecipeMappingList, getBpmnTaskIdMap(bpmnTaskList, null)).values();
			}
			return CollectionUtils.EMPTY_COLLECTION;
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Collection<NVWorkorderWrapper> getWorkorderDetailsByTemplateList(String strWorkorderId,
			List<String> templateType, Long strModifiedTime) {
		logger.info("Inside Method getWorkorderDetailsByUser()");
		try {
			List<String> bpmnWOTemplateNames = getBpmnTemplateNameList(templateType);
			List<BpmnTask> bpmnTaskList = new ArrayList<>();
			for (String bpmnWOTemplateName : bpmnWOTemplateNames) {
				bpmnTaskList.addAll(bpmnTaskCandidateDao.getBPMNTaskListByAssignedUserId(
						UserContextServiceImpl.getUserInContext().getUserid(),
						com.inn.bpmn.model.BpmnTask.Status.Completed, bpmnWOTemplateName));
			}
			List<String> workorderIdList = getWorkorderIdList(bpmnTaskList);
			logger.info("workorderIdList {}", workorderIdList);
			if (!workorderIdList.isEmpty()) {
				List<WORecipeMapping> woRecipeMappingList = woRecipeMappingDao.getRecipeWOByWOIdList(workorderIdList,
						templateType, strModifiedTime, parseInt(strWorkorderId));

				return getNVWorkorderMap(woRecipeMappingList, getBpmnTaskIdMap(bpmnTaskList, null)).values();
			}
			return CollectionUtils.EMPTY_COLLECTION;

		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}

	}

	private List<String> getBpmnTemplateNameList(List<String> templateType) {
		List<String> bpmnWOTemplateNames = new ArrayList<>();
		for (String template : templateType) {
			logger.info("TemplateType.valueOf(template).getValue() {} ", TemplateType.valueOf(template).getValue());
			BPMNWOTemplate bpmnTemplate = bpmnWOTemplateDao
					.getBPMNWOTemplateByName(TemplateType.valueOf(template).getValue());
			if (bpmnTemplate != null) {
				bpmnWOTemplateNames.add(bpmnTemplate.getName());
			}
		}
		return bpmnWOTemplateNames;
	}

	/**
	 * Return Map of NVWorkorderWrapper with Workorder Name as key.
	 *
	 * @param woRecipeMappingList : WoRecipeMapping List
	 * @param taskIdMap           : Map of BpmnTask Id with Workorder Name as key.
	 * @return Map of NVWorkorderWrapper
	 * @throws ParseException        the parse exception
	 * @throws NumberFormatException the number format exception
	 * @throws RestException         the rest exception
	 */
	private Map<String, NVWorkorderWrapper> getNVWorkorderMap(List<WORecipeMapping> woRecipeMappingList,
			Map<String, Integer> taskIdMap) throws ParseException {
		Map<String, NVWorkorderWrapper> map = new HashMap<>();
		if (woRecipeMappingList != null && !woRecipeMappingList.isEmpty()) {
			List<Integer> processedWORecipemappingList = iWOFileDetailDao.getProccesedLogFileMappingList(
					woRecipeMappingList.get(ForesightConstants.ZERO).getGenericWorkorder().getId());

			for (WORecipeMapping woRecipeMappping : woRecipeMappingList) {
				String key = woRecipeMappping.getGenericWorkorder().getWorkorderId();
				NVWorkorderWrapper wrapper = getNVWorkorderWrapper(key, map, woRecipeMappping,
						processedWORecipemappingList);
				if (taskIdMap != null) {
					wrapper.setTaskId(taskIdMap.get(key));
				}
				map.put(key, wrapper);
			}
		}
		return map;
	}







	private Map<String, NVWorkorderWrapper> getNVWorkorderMapForStealth(List<WORecipeMapping> woRecipeMappingList,
			Map<String, Integer> taskIdMap) throws ParseException {
		Map<String, NVWorkorderWrapper> map = new HashMap<>();
		if (woRecipeMappingList != null && !woRecipeMappingList.isEmpty()) {
			for (WORecipeMapping woRecipeMappping : woRecipeMappingList) {
				String key = woRecipeMappping.getGenericWorkorder().getWorkorderId();
				NVWorkorderWrapper wrapper = getNVWorkorderWrapperForStealth(key, map, woRecipeMappping);
				if (taskIdMap != null) {
					wrapper.setTaskId(taskIdMap.get(key));
				}
				map.put(key, wrapper);
			}
		}
		return map;
	}



	/**
	 * Adds All Recipes To its Corresponding WorkOrder and Return a WORecipeWrapper.
	 *
	 * @param key                          WorkorderName
	 * @param map                          the map
	 * @param woRecipeMapping              the wo recipe mapping
	 * @param processedWORecipemappingList
	 * @return WORecipeWrapper
	 * @throws ParseException        the parse exception
	 * @throws NumberFormatException the number format exception
	 * @throws RestException         the rest exception
	 */
	private NVWorkorderWrapper getNVWorkorderWrapper(String key, Map<String, NVWorkorderWrapper> map,
			WORecipeMapping woRecipeMapping, List<Integer> processedWORecipemappingList) throws ParseException {

		GenericWorkorder genericWorkorder = woRecipeMapping.getGenericWorkorder();
		Map<String, String> metaMap = genericWorkorder.getGwoMeta();
		TemplateType templateType = genericWorkorder.getTemplateType();

		if (map.containsKey(key)) {
			NVWorkorderWrapper wrapper = map.get(key);
			wrapper.getRecipeList()
					.add(getRecipeWrapper(woRecipeMapping, metaMap, templateType, processedWORecipemappingList));
			return wrapper;
		} else {
			NVWorkorderWrapper wrapper = new NVWorkorderWrapper();
			List<RecipeWrapper> list = new ArrayList<>();
			list.add(getRecipeWrapper(woRecipeMapping, metaMap, templateType, processedWORecipemappingList));
			wrapper.setId(genericWorkorder.getId());
			wrapper.setWorkorderId(genericWorkorder.getWorkorderId());
			wrapper.setStatus(genericWorkorder.getStatus().getValue());
			wrapper.setWorkorderName(genericWorkorder.getWorkorderName());
			wrapper.setDescription(genericWorkorder.getDescription());
			wrapper.setRemark(genericWorkorder.getRemark());
			wrapper.setCreationDate(genericWorkorder.getCreationTime().getTime());
			wrapper.setCompletionPercentage(genericWorkorder.getCompletionPercentage());
			if(genericWorkorder.getDueDate()!=null) {
			wrapper.setDueDate(genericWorkorder.getDueDate().getTime());
			}
			wrapper.setModificationDate(genericWorkorder.getModificationTime().getTime());
			wrapper.setRecipeList(list);
			wrapper.setTemplateType(genericWorkorder.getTemplateType());
			getWorkorderMeta(wrapper, metaMap);
			return wrapper;
		}
	}






	private NVWorkorderWrapper getNVWorkorderWrapperForStealth(String key, Map<String, NVWorkorderWrapper> map,
			WORecipeMapping woRecipeMapping) throws ParseException {

		GenericWorkorder genericWorkorder = woRecipeMapping.getGenericWorkorder();
		Map<String, String> metaMap = genericWorkorder.getGwoMeta();

		if (map.containsKey(key)) {
			NVWorkorderWrapper wrapper = map.get(key);
			wrapper.getRecipeList()
					.add(getRecipeWrapperForStealth(woRecipeMapping, metaMap));
			return wrapper;
		} else {
			NVWorkorderWrapper wrapper = new NVWorkorderWrapper();
			List<RecipeWrapper> list = new ArrayList<>();
			list.add(getRecipeWrapperForStealth(woRecipeMapping, metaMap));
			wrapper.setId(genericWorkorder.getId());
			wrapper.setWorkorderId(genericWorkorder.getWorkorderId());
			wrapper.setStatus(genericWorkorder.getStatus().getValue());
			wrapper.setWorkorderName(genericWorkorder.getWorkorderName());
			wrapper.setCreationDate(genericWorkorder.getCreationTime().getTime());
			wrapper.setStartTime(parseLong(metaMap.get(START_TIME)));
			wrapper.setDuration(parseLong(metaMap.get(DURATION)));
			if(genericWorkorder.getDueDate()!=null) {
			wrapper.setDueDate(genericWorkorder.getDueDate().getTime());
			}
			wrapper.setModificationDate(genericWorkorder.getModificationTime().getTime());
			wrapper.setRecipeList(list);
			return wrapper;
		}
	}



	private RecipeWrapper getRecipeWrapperForStealth(WORecipeMapping woRecipeMapping, Map<String, String> metaMap) throws ParseException {
		Recipe recipe = woRecipeMapping.getRecipe();
		RecipeWrapper recipeWrapper = null;
		JSONParser parser = new JSONParser();
		recipeWrapper = new RecipeWrapper(recipe.getId(), recipe.getRecipeId(), recipe.getName(), null,
				woRecipeMapping.getStatus().getValue(), null, null,
				null, null, null,
				(JSONArray) parser.parse(recipe.getScriptJson()), woRecipeMapping.getId(), null, null,
				null, null, null,
				null,
				null, null, null);
		return recipeWrapper;
	}
	/**
	 * Adds all Meta data to the workOrderWrapper Like PCI_AZIMUTH, SITE_INFO etc.
	 *
	 * @param wrapper the wrapper
	 * @param metaMap the meta map
	 * @return NVWorkorderWrapper object is Returned after adding All meta info.
	 * @throws NumberFormatException the number format exception
	 * @throws RestException         the rest exception
	 */
	private void getWorkorderMeta(NVWorkorderWrapper wrapper, Map<String, String> metaMap) {
		if (Utils.hasValidValue(metaMap.get(SITE_INFO))) {
			wrapper.setSiteWrapper(new Gson().fromJson(metaMap.get(SITE_INFO), NVSiteWrapper.class));
		}
		if (Utils.hasValidValue(metaMap.get(PCI_AZIMUTH_MAP))) {
			wrapper.setPciAzimuthMap(
					new Gson().fromJson(metaMap.get(PCI_AZIMUTH_MAP), new TypeToken<HashMap<Integer, Integer>>() {
					}.getType()));
		}
		Integer buildingId = parseInt((metaMap.get(BUILDING_ID)));
		if (buildingId != null) {
			wrapper.setBuildingWrapper(buildingDataService.getBuildingDetailsByPk(buildingId));
		}
		if (Utils.hasValidValue(metaMap.get(WALK_TEST_REPORT_INSTACE_ID))) {
			wrapper.setWalkTestReportInstanceId(parseInt((metaMap.get(WALK_TEST_REPORT_INSTACE_ID))));
		}

		wrapper.setGeographyId(parseInt(metaMap.get(GEOGRAPHY_ID)));
		wrapper.setCustomGeographyId(parseInt(metaMap.get(CUSTOM_GEOGRAPHY_ID)));
		wrapper.setStartTime(parseLong(metaMap.get(START_TIME)));
		wrapper.setDuration(parseLong(metaMap.get(DURATION)));
		wrapper.setReportInstanceId(parseInt((metaMap.get(REPORT_INSTANCE_ID))));
		wrapper.setGeographyType(metaMap.get(GEOGRAPHY_TYPE));
		wrapper.setTechnology(metaMap.get(TECHNOLOGY));
		wrapper.setOperatorName(metaMap.get(OPERATOR_NAME));
		wrapper.setWoSource(metaMap.get(WO_SOURCE));
		wrapper.setBand(metaMap.get(BAND));
		wrapper.setWoMapType(metaMap.get(WO_MAP_TYPE));
		if (metaMap.containsKey(WO_ROUTE_TYPE)) {
			wrapper.setWoRouteType(metaMap.get(WO_ROUTE_TYPE));
		}
      wrapper.setIsSiteAcceptance(metaMap.get(IS_SITE_ACCEPTANCE));
	}

	/**
	 * Creates RecipeWrapper from Recipe Object.
	 *
	 * @param woRecipeMapping this contains Recipe details for a Workorder
	 * @param metaMap         the meta map
	 * @param templateType    the template type
	 * @return Created RecipeWrapper
	 * @throws ParseException the parse exception
	 */
	private RecipeWrapper getRecipeWrapper(WORecipeMapping woRecipeMapping, Map<String, String> metaMap,
			TemplateType templateType, List<Integer> processedWORecipemappingList) throws ParseException {
		Recipe recipe = woRecipeMapping.getRecipe();
		RecipeWrapper recipeWrapper = null;
		JSONParser parser = new JSONParser();
		boolean isReportAvailable = false;
		if (processedWORecipemappingList != null && woRecipeMapping.getId() != null) {
			isReportAvailable = processedWORecipemappingList.contains(woRecipeMapping.getId());
		}
		recipeWrapper = new RecipeWrapper(recipe.getId(), recipe.getRecipeId(), recipe.getName(), recipe.getType(),
				woRecipeMapping.getStatus().getValue(), recipe.getDescription(), recipe.getCategory(),
				recipe.getCreationTime(), recipe.getModificationTime(), null,
				(JSONArray) parser.parse(recipe.getScriptJson()), woRecipeMapping.getId(), null, null,
				woRecipeMapping.getWaitTime(), woRecipeMapping.getMaxTime(), woRecipeMapping.getIterationCount(),
				woRecipeMapping.getWaitTime() != null ? Boolean.TRUE : Boolean.FALSE,
				recipe.getCreator().getFirstName(), recipe.getCreator().getLastName(), isReportAvailable);
		getRecipeMeta(recipeWrapper, metaMap, templateType);
		setBuildingDetail(recipeWrapper);
		if (processedWORecipemappingList != null && !processedWORecipemappingList.isEmpty()) {
			recipeWrapper.setLogFileDownload(processedWORecipemappingList.contains(woRecipeMapping.getId()));
		}

		return recipeWrapper;
	}

	private void setBuildingDetail(RecipeWrapper recipeWrapper) {
		if (recipeWrapper.getUnitId() != null) {
			logger.info("Going to Building detail of recipe for unit id:{}", recipeWrapper.getUnitId());
			try {
				Unit unit = iUnitDataDao.findByPk(recipeWrapper.getUnitId());
				if (unit != null) {
					recipeWrapper.setUnitName(unit.getUnitName());
					if (unit.getFloor() != null) {
						recipeWrapper.setFloorName(unit.getFloor().getFloorName());
						if (unit.getFloor().getWing() != null) {
							recipeWrapper.setWingName(unit.getFloor().getWing().getWingName());
						}
					}
				}
			} catch (Exception e) {
				logger.error("Exception in setBuildingDetail for recipe :{}", ExceptionUtils.getStackTrace(e));
			}
		}
	}

	/**
	 * Complete BPMNWorkorder as well as GenericWorkorder by Completing Task by
	 * given TaskId.
	 *
	 * @return Success Message if Task Completed, Failure Message otherwise
	 */
	@Override
	@Transactional
	public Object completeWorkorderByTaskId(Integer taskId) {
		logger.info("task id for workorder complete {}", taskId);
		try {
			Map<String, String> taskMeta = new HashMap<>();
			BpmnTask bpmnTask = bpmnTaskDao.findByPk(taskId);

			if (!bpmnTask.getStatus().equals(com.inn.bpmn.model.BpmnTask.Status.Completed)) {
				BPMNWorkorder bpmnWorkoder = bpmnTask.getBpmnWorkorder();
				BpmnTaskCandidate bpmnTaskCandidate = bpmnTaskCandidateDao
						.getBpmnTaskCandidatesByBpmnTaskId(bpmnTask.getId()).get(ForesightConstants.ZERO);
				User assignedTo = bpmnTaskCandidate.getUser();

				GenericWorkorder genericWorkorder = genericWorkorderService
						.getWorkorderByWOId(bpmnWorkoder.getWorkorderNo());

				List<WORecipeMapping> woRecipeByGWOIdList = woRecipeMappingDao
						.getWORecipeByGWOId(genericWorkorder.getId());
				List<Boolean> isCompleteWO = new ArrayList<>();
				for (WORecipeMapping woRecipeMapping : woRecipeByGWOIdList) {
					Status recipeStatus = woRecipeMapping.getStatus();
					if (recipeStatus.equals(Status.INPROGRESS) || recipeStatus.equals(Status.REOPEN)) {
						boolean recipeCompleteInProgressRecipe = recipeService
								.isRecipeCompleteInProgressRecipe(woRecipeMapping);
						isCompleteWO.add(recipeCompleteInProgressRecipe);
					} else {
						if (woRecipeMapping.getStatus().equals(Status.COMPLETED)) {
							isCompleteWO.add(true);
						}
					}
				}
				if (!(isCompleteWO.contains(false))) {
					genericWorkorder.setStatus(GenericWorkorder.Status.COMPLETED);
					genericWorkorder.setModificationTime(new Date());
					activitiService.completeBPMNWorkorderTask(bpmnTask, getTaskMetaMap(), taskMeta);
					genericWorkorder.setModificationTime(new Date());
					genericWorkorderDao.update(genericWorkorder);
					completeAllSyncedRecipes(genericWorkorder);
					return insertWOReportConfigData(genericWorkorder.getId(), genericWorkorder.getTemplateType(),
							assignedTo.getFirstName() + Symbol.SPACE + assignedTo.getLastName(), genericWorkorder);
				} else {
					return FILE_NOT_AVAILABLE;
				}

			} else {
				return WORKORDER_COMPLETED_JSON;
			}
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			return FAILURE_JSON;
		}
	}

	/**
	 * Complete all synced recipes.
	 *
	 * @param genericWorkorder the generic workorder
	 * @throws DaoException  the dao exception
	 * @throws RestException the rest exception
	 */
	@Override
	public void completeAllSyncedRecipes(GenericWorkorder genericWorkorder) {
		List<WOFileDetail> fileList = woFileDetailDao.getFileDetailByWorkOrderId(genericWorkorder.getId());
		for (WOFileDetail file : fileList) {
			if (file != null && file.getWoRecipeMapping() != null) {
				recipeService.completeRecipe(file.getWoRecipeMapping().getId());
			}
		}
	}

	@Async
	@Override
	public void generateReport(AnalyticsRepository reportInstance) throws HttpException {
		String url = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue())
				+ ConfigUtils.getString(ConfigEnum.DROPWIZARD_NV_WORKORDER_REPORT_GENERATION_URL);
		StringEntity entity = new StringEntity(String.format(REPORT_INSTANCE_ID_JSON, reportInstance.getId()),
				ContentType.APPLICATION_JSON);
		Duration duration = Duration
				.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
		logger.info("Getting Report Dropwizard URL {}, json {}", url,
				String.format(REPORT_INSTANCE_ID_JSON, reportInstance.getId()));
		sendHttpPostRequest(url, entity, true, duration).getString();
	}

	/**
	 * Accepts llimit and ulimit and returns List of NVWorkorderWrapper Objects if
	 * currently logged in user has permissions.
	 *
	 * @param lLimit       the l limit
	 * @param uLimit       the u limit
	 * @param statusList   the status list
	 * @param templateList the template list
	 * @param searchString the search string
	 * @return List of NVWorkorderWrapper Objects
	 * @throws RestException the rest exception
	 */
	@Override
	public List<NVWorkorderWrapper> findAllWorkorder(Integer lLimit, Integer uLimit,
			List<GenericWorkorder.Status> statusList, List<TemplateType> templateList, String searchString,
			Boolean isArchive) {
		List<NVWorkorderWrapper> list = new ArrayList<>();
		try {
			logger.info("Inside method findAllWorkorder {} {} {} {}", statusList, templateList, searchString,
					isArchive);
			List<GenericWorkorder> workorderList = genericWorkorderDao.findAllNVWorkorder(lLimit, uLimit, templateList,
					statusList, searchString, null, null, null, isArchive);
			if (workorderList != null && !workorderList.isEmpty()) {
				List<BpmnTaskCandidate> bpmnTaskCandidateList = bpmnTaskCandidateDao
						.getBPMNTaskCandidateListByWONoList(getWorkorderIdListFromGWO(workorderList));
				Map<String, BpmnTaskCandidate> candiateMap = getBpmnTaskCandidateMap(bpmnTaskCandidateList);
				for (GenericWorkorder genericWorkorder : workorderList) {
					BpmnTaskCandidate candidate = candiateMap.get(genericWorkorder.getWorkorderId());
					list.add(getNVWokorderWrapper(genericWorkorder, candidate));
				}
			}
			
			return list;
		} catch (Exception e) {
			logger.error("Error in getting all workorders {} {} {} {} {} ", statusList, templateList, searchString,
					isArchive, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Creates NvWorkordrWrapper From GenericWorkorder and BpmnTaskCandidate object.
	 *
	 * @param genericWorkorder  the generic workorder
	 * @param bpmnTaskCandidate the bpmn task candidate
	 * @return NVWorkorderWrapper object
	 */
	private NVWorkorderWrapper getNVWokorderWrapper(GenericWorkorder genericWorkorder,
			BpmnTaskCandidate bpmnTaskCandidate) {
		NVWorkorderWrapper wrapper = new NVWorkorderWrapper();
		wrapper.setId(genericWorkorder.getId());
		wrapper.setWorkorderName(genericWorkorder.getWorkorderName());
		wrapper.setDescription(genericWorkorder.getDescription());
		wrapper.setRemark(genericWorkorder.getRemark());
		wrapper.setWorkorderId(genericWorkorder.getWorkorderId());
		wrapper.setTemplateType(genericWorkorder.getTemplateType());
		wrapper.setAssignedBy(getUserName(genericWorkorder.getCreatedBy()));
		if(genericWorkorder.getDueDate()!=null) {
		wrapper.setDueDate(genericWorkorder.getDueDate().getTime());
		}
		wrapper.setStartDate(genericWorkorder.getStartDate().getTime());
		wrapper.setModificationDate(genericWorkorder.getModificationTime().getTime());
		wrapper.setCompletionPercentage(genericWorkorder.getCompletionPercentage());
		wrapper.setStatus(genericWorkorder.getStatus().getValue());
		
		wrapper.setIbOldWorkorderStatus(genericWorkorder.getIbOldWorkorderStatus());
		if (genericWorkorder.getGwoMeta().containsKey(NVConstant.KEY_TECHNOLOGY)) {
			wrapper.setTechnology(genericWorkorder.getGwoMeta().get(NVConstant.KEY_TECHNOLOGY));
		}
		setValuesForStealthWorkorder(genericWorkorder, wrapper);

		setCustomGeographyIdIntoWrapper(genericWorkorder, wrapper);
		if (bpmnTaskCandidate != null) {
			wrapper.setAssignedTo(getUserName(bpmnTaskCandidate.getUser()));
			wrapper.setTaskId(bpmnTaskCandidate.getBpmnTask().getId());
		}
		
		return wrapper;
	}

	private void setCustomGeographyIdIntoWrapper(GenericWorkorder genericWorkorder, NVWorkorderWrapper wrapper) {
		if (genericWorkorder.getGwoMeta().containsKey(NVWorkorderConstant.RECIPE_CUSTOM_GEOGRAPHY_MAP)) {
			String recipeCustomGeographyString = genericWorkorder.getGwoMeta()
					.get(NVWorkorderConstant.RECIPE_CUSTOM_GEOGRAPHY_MAP);
			logger.info("customGeographyMap is {}", recipeCustomGeographyString);
			ObjectMapper mapper = new ObjectMapper();

			Map<String, Integer> recipeCustomGeographyMap = new HashMap<>();

			try {
				recipeCustomGeographyMap = mapper.readValue(recipeCustomGeographyString,
						new TypeReference<Map<String, Integer>>() {
						});
				Optional<String> optional = recipeCustomGeographyMap.keySet().stream().findFirst();
				if (optional.isPresent()) {
					String key = optional.get();
					wrapper.setCustomGeographyId(recipeCustomGeographyMap.get(key));
				}

			} catch (Exception e) {
				logger.error("Error to set customGeography {}", Utils.getStackTrace(e));
			}
		}
	}

	private void setValuesForStealthWorkorder(GenericWorkorder genericWorkorder, NVWorkorderWrapper wrapper) {
		if (genericWorkorder.getGwoMeta().containsKey(NVConstant.WO_FREQUENCY)) {
			wrapper.setStealthWOFrequency(genericWorkorder.getGwoMeta().get(NVConstant.WO_FREQUENCY));
			if (genericWorkorder.getAssignedTo() != null) {

				wrapper.setAssignedTo(getUserName(genericWorkorder.getAssignedTo()));
			} else {

				String multiUser = genericWorkorder.getGwoMeta().getOrDefault(NVWorkorderConstant.MULTI_USER, null);

				if (multiUser != null) {
					List<String> multiUserList = new Gson().fromJson(multiUser, new TypeToken<List<String>>() {
					}.getType());
					wrapper.setAssignedTo(String.join(",", multiUserList));
				}

			}
		}
		
		if(genericWorkorder.getTemplateType().name().equalsIgnoreCase(GenericWorkorder.TemplateType.NV_STEALTH.name())) {
			String isLayer3Execution = genericWorkorder.getGwoMeta().getOrDefault(NVConstant.LAYER3_EXECUTION_KEY, "false");
			String woFrequency= genericWorkorder.getGwoMeta().getOrDefault(NVConstant.WO_FREQUENCY, null);
			String isProbe= genericWorkorder.getGwoMeta().getOrDefault(StealthConstants.IS_PROBE_TEST, null);
			Map<String,String> requiredFieldsMap=new HashMap();
			requiredFieldsMap.put(NVConstant.LAYER3_EXECUTION_KEY, isLayer3Execution);
			requiredFieldsMap.put(StealthConstants.IS_PROBE_TEST, isProbe);
			requiredFieldsMap.put(NVConstant.WO_FREQUENCY, woFrequency);
			wrapper.setMetaData(requiredFieldsMap);
		}
		
	}

	/**
	 * Gets Total Workorder count.
	 *
	 * @param statusList   the status list
	 * @param templateList the template list
	 * @return Integer Workorder Count for of logged in Permission
	 * @throws RestException the rest exception
	 */
	@Override
	public Long getTotalWorkorderCount(List<GenericWorkorder.Status> statusList, List<TemplateType> templateList,
			String searchString, Boolean isArchived) {
		try {
			return genericWorkorderDao.getTotalWorkorderCount(templateList, statusList, searchString, isArchived);
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response getGeographyBoundryForMobile(String strGeoData) {
		try {
			Map<String, String> geoMap = new Gson().fromJson(
					AuthenticationCommonUtil.checkForValueDecryption(strGeoData),
					new TypeToken<HashMap<String, String>>() {
					}.getType());

			if (StringUtils.isNotEmpty(geoMap.get(GEOGRAPHY_TYPE))
					&& StringUtils.isNotEmpty(geoMap.get(GEOGRAPHY_ID))) {

				String geograhyType = geoMap.get(GEOGRAPHY_TYPE);
				Integer geographyId = Integer.parseInt(geoMap.get(GEOGRAPHY_ID));
				String zoom = geoMap.get(ZOOM_LEVEL);
				Double latitude = parseDouble(geoMap.get(LATITUDE));
				Double longitude = parseDouble(geoMap.get(LONGITUDE));
				
				Double bufferSize = parseDouble(geoMap.get(BUFFER_SIZE));
				if(bufferSize == null) {
					// 0.0005 means 5 km
					bufferSize = parseDouble(ConfigUtils.getString(NVConfigUtil.BUFFER_SIZE));
				}
				
				String templateType = geoMap.get(TEMPLATE_TYPE);
				
				logger.info("getting geograhyType {}, geographyType {}, latitude {}, longitude {}, zoom {}",
						geograhyType, geographyId, latitude, longitude, zoom);
				if (geograhyType.equalsIgnoreCase(NVWorkorderConstant.CUSTOM_GEOGRAPHY)) {
					String url = String.format(
							ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL, Boolean.TRUE) + ConfigUtils
									.getString(ConfigEnum.DROPWIZARD_HBASE_GET_CUSTOM_BOUNDARY_URL, Boolean.TRUE),
							geographyId);
					logger.info("Getting url {}", url);
					return Response.ok(AuthenticationCommonUtil
							.checkForValueEncryption(customGeographyService.getCustomGeography(geographyId, url), null))
							.build();
				} else if (geograhyType.equalsIgnoreCase(NVWorkorderConstant.GEOGRAPHYL1)) {
					GeographyL1 geographyL1 = geographyL1Dao.getGeographyL1ById(geographyId);
					return getBoundaryResponseObject(geographyL1.getName(), GenericMapUtils.L1_TYPE, latitude,
							longitude, zoom,bufferSize,templateType);
				} else if (geograhyType.equalsIgnoreCase(NVWorkorderConstant.GEOGRAPHYL2)) {
					GeographyL2 geographyL2 = geographyL2Dao.getGeographyL2ById(geographyId);
					return getBoundaryResponseObject(geographyL2.getName(), GenericMapUtils.L2_TYPE, latitude,
							longitude, zoom,bufferSize,templateType);
				} else if (geograhyType.equalsIgnoreCase(NVWorkorderConstant.GEOGRAPHYL3)) {
					GeographyL3 geographyL3 = geographyL3Dao.getGeographyL3ById(geographyId);
					return getBoundaryResponseObject(geographyL3.getName(), GenericMapUtils.L3_TYPE, latitude,
							longitude, zoom,bufferSize,templateType);
				} else if (geograhyType.equalsIgnoreCase(NVWorkorderConstant.GEOGRAPHYL4)) {
					GeographyL4 geographyL4 = geographyL4Dao.getGeographyL4ById(geographyId);
					return getBoundaryResponseObject(geographyL4.getName(), GenericMapUtils.L4_TYPE, latitude,
							longitude, zoom,bufferSize,templateType);
				} else {
					throw new RestException(ForesightConstants.INVALID_PARAMETERS);
				}
			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}

		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Adds Missing fields in Workorder wrapper to create Workorder.
	 *
	 * @param wrapper the wrapper
	 * @throws RestException the rest exception
	 */
	private void prepareWOCreationRequest(NVWorkorderWrapper wrapper) {
		try {
			Map<String, String> geographyMap = getGeographyIdByLatLng(wrapper.getLatitude(), wrapper.getLongitude(),
					wrapper.getTemplateType(), wrapper.getMcc(), wrapper.getMnc());
			logger.info("geographyMap in prepareWOCreationRequest : {} ", geographyMap);
			wrapper.setWorkorderId(getWorkorderId(wrapper.getTemplateType(), geographyMap.get(GEOGRAPHY_NAME)));
			setStartAndEndDate(wrapper);
			wrapper.setGeoWoMetaMap(getMetaMapForMobileWO(geographyMap, wrapper));
		} catch (Exception e) {
			logger.error("Exception Inside prepareWOCreationrequest {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(e);
		}

	}

	/**
	 * Gets the meta map for mobile WO.
	 *
	 * @param gwoMetaMap the gwo meta map
	 * @param wrapper    the wrapper
	 * @return the meta map for mobile WO
	 */
	private Map<String, String> getMetaMapForMobileWO(Map<String, String> gwoMetaMap, NVWorkorderWrapper wrapper) {
		Map<String, String> map = new HashMap<>();
		map.put(GEOGRAPHY_ID, gwoMetaMap.get(GEOGRAPHY_ID));
		map.put(GEOGRAPHY_TYPE, gwoMetaMap.get(GEOGRAPHY_TYPE));
		map.put(BAND_TYPE, wrapper.getBandType());
		map.put(TECHNOLOGY, wrapper.getTechnology());
		if (wrapper.getBuildingId() != null) {
			map.put(BUILDING_ID, wrapper.getBuildingId().toString());
		}
		map.put(WO_SOURCE, MOBILE);
		return map;
	}

	/**
	 * Creates the recipes.
	 *
	 * @param wrapper the wrapper
	 * @throws DaoException  the dao exception
	 * @throws RestException the rest exception
	 */
	private List<RecipeWrapper> createAndGetRecipes(NVWorkorderWrapper wrapper) {
		List<String> recipeIdList = getRecipeIdList(wrapper);
		List<Recipe> recipeList = recipeService.findAllByRecipeId(recipeIdList);
		return getRecipeWrapperList(wrapper, recipeList);
	}

	/**
	 * Gets the recipe id list.
	 *
	 * @param wrapper the wrapper
	 * @return the recipe id list
	 * @throws DaoException the dao exception
	 */
	private List<String> getRecipeIdList(NVWorkorderWrapper wrapper) {
		if (wrapper.getRecipeIdList() == null) {
			return getDefaultRecipeIdList(wrapper.getTemplateType());
		}
		return wrapper.getRecipeIdList();
	}

	/**
	 * Gets the default recipe id list.
	 *
	 * @param templateType the template type
	 * @return the default recipe id list
	 * @throws DaoException the dao exception
	 */
	private List<String> getDefaultRecipeIdList(TemplateType templateType) {
		String mapJson = iSystemConfigurationDao.getSystemConfigurationByType(NV_DEFAULT_RECIPE_MAP)
				.get(ForesightConstants.ZERO).getValue();
		Map<String, List<String>> map = new Gson().fromJson(mapJson, new TypeToken<HashMap<String, List<String>>>() {
		}.getType());
		return map.get(templateType.toString());
	}

	/**
	 * Gets the geography id by lat lng.
	 *
	 * @param latitude     the latitude
	 * @param longitude    the longitude
	 * @param templateType the template type

	 * @return the geography id by lat lng
	 * @throws RestException the rest exception
	 * @throws DaoException
	 */
	public Map<String, String> getGeographyIdByLatLng(Double latitude, Double longitude, TemplateType templateType,
			String mcc, String mnc) {
		Map<String, String> geographyMap = null;
		try {
			GeographyL4 geographyL4 = genericMapService.getGeographyL4LatLng(latitude, longitude);
			if (Utils.isNotNull(geographyL4)) {
				geographyMap = getGegraphyTaggedMap(ForesightConstants.GEOGRAPHY_L4, geographyL4.getId().toString(),
						geographyL4.getName());
			} else {
				GeographyL3 geographyL3 = genericMapService.getGeographyL3LatLng(latitude, longitude);
				if (Utils.isNotNull(geographyL3)) {
					geographyMap = getGegraphyTaggedMap(ForesightConstants.GEOGRAPHY_L3, geographyL3.getId().toString(),
							geographyL3.getName());
				} else {
					GeographyL2 geographyL2 = genericMapService.getGeographyL2LatLng(latitude, longitude);
					if (Utils.isNotNull(geographyL2)) {
						geographyMap = getGegraphyTaggedMap(ForesightConstants.GEOGRAPHY_L2,
								geographyL2.getId().toString(), geographyL2.getName());
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in getGeographyIdByLatLng : {} ", ExceptionUtils.getStackTrace(e));
			return createWithDefaultGeography(geographyMap, mcc, mnc);
		}
		return geographyMap;
	}

	private Map<String, String> createWithDefaultGeography(Map<String, String> geographyMap, String mcc, String mnc) {
		if (Utils.hasValidValue(mcc) && Utils.hasValidValue(mnc)) {
			logger.info("Getting mcc {}, mnc {}", mcc, mnc);
			String validMCCAndMNC = iSystemConfigurationDao.getSystemConfigurationByType(NV_VALID_MCC_MNC_LIST)
					.get(ForesightConstants.ZERO).getValue();
			if (validMCCAndMNC.contains(mcc + Symbol.UNDERSCORE + mnc)) {
				GeographyL1 geographyL1 = geographyL1Dao.findAll().get(ForesightConstants.ZERO);
				return getGegraphyTaggedMap(ForesightConstants.GEOGRAPHY_L1, geographyL1.getId().toString(),
						geographyL1.getName());
			}
		}
		return geographyMap;
	}

	@Override
	public NVWorkorderWrapper getWorkorderDetailsByWOId(Integer workorderId, Integer taskId) {
		try {
			if (workorderId != null) {
				GenericWorkorder genericWorkorder = genericWorkorderDao.findByPk(workorderId);
				List<WORecipeMapping> woRecipeMappingList = null;
				if (genericWorkorder.getTemplateType().equals(TemplateType.NV_STEALTH)
						&& genericWorkorder.getGwoMeta().containsKey(NVConstant.WO_RECIPE_ID)) {
					woRecipeMappingList = new ArrayList<>();
					woRecipeMappingList.add(woRecipeMappingDao
							.findByPk(Integer.parseInt(genericWorkorder.getGwoMeta().get(NVConstant.WO_RECIPE_ID))));
				} else {
					woRecipeMappingList = woRecipeMappingDao.getWORecipeByGWOId(workorderId);
				}

				List<User> userWrapperList = getUsers(genericWorkorder);
				NVWorkorderWrapper nvWorkorderWrapper = getNVWorkorderMap(woRecipeMappingList,
						getBpmnTaskIdMapForWorkorderId(genericWorkorder.getWorkorderId(), taskId))
								.get(genericWorkorder.getWorkorderId());
				nvWorkorderWrapper.setMetaData(genericWorkorder.getGwoMeta());
				if (userWrapperList != null && !userWrapperList.isEmpty()) {
					nvWorkorderWrapper.setUserName(userWrapperList.get(ForesightConstants.ZERO).getUserName());
				}
				setCustomGeographyIdIntoWrapper(genericWorkorder, nvWorkorderWrapper);
				return nvWorkorderWrapper;
			}
			return null;
		} catch (Exception e) {
			logger.error("Exception In getting getWorkorderDetailsByWOId : {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	private List<User> getUsers(GenericWorkorder genericWorkorder) {
		List<User> userWrapperList = new ArrayList<>();
		List<GenericWorkorder> workorderList = new ArrayList<>();
		if (genericWorkorder != null) {
			workorderList.add(genericWorkorder);

			if (workorderList != null && !workorderList.isEmpty()) {

				List<BPMNWorkorder> bpmnWorkorders = bpmnWorkorderDao.getBpmWoListByGenericWoName(
						getWorkorderIdListFromGWO(workorderList).get(ForesightConstants.ZERO));

				if (bpmnWorkorders != null && !bpmnWorkorders.isEmpty()) {
					List<Integer> userIdPk = bpmnTaskCandidateDao
							.getAssignedUsersIdByWorkorderId(bpmnWorkorders.get(ForesightConstants.ZERO).getId());

					if (userIdPk != null && !userIdPk.isEmpty()) {
						userWrapperList = userDao.getUsersByIds(userIdPk);
					}
				}
			}
		}
		return userWrapperList;
	}

	@Override
	@Transactional
	public NVWorkorderWrapper getWorkorderDetailsByDeviceId(Integer workorderId, String deviceId, Date date) {
		try {
			NVDeviceData device = nvDeviceDataService.findNVDeviceDataByDeviceId(deviceId);
			Integer nvDeviceId = device.getId();
			if (workorderId == null) {
				StealthTaskDetail taskDetail = stealthTaskDetailDao.getStealthTaskDetailByDeviceId(nvDeviceId, date);
				if (taskDetail != null)
					workorderId = taskDetail.getGenericWorkorder().getId();
				else
					throw new RestException("Invalid Parameter");
			}
			GenericWorkorder genericWorkorder = genericWorkorderDao.findByPk(workorderId);

			List<WORecipeMapping> woRecipeMappingList = woRecipeMappingDao.getWORecipeByGWOId(workorderId);
			NVWorkorderWrapper nvWorkorderWrapper = getNVWorkorderMap(woRecipeMappingList,
					getBpmnTaskIdMapForWorkorderId(genericWorkorder.getWorkorderId(), null))
							.get(genericWorkorder.getWorkorderId());
			nvWorkorderWrapper.setMetaData(genericWorkorder.getGwoMeta());
			return nvWorkorderWrapper;

		} catch (NoResultException | EmptyResultDataAccessException e1) {
			logger.error("Exception In getting getWorkorderDetailsByDeviceId : {}", "NO RESULT(S) FOUND");
			throw new NoResultException(e1.getMessage());
		}
		catch (Exception e) {
			logger.error("Exception In getting getWorkorderDetailsByDeviceId : {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 *
	 * @param workorderId
	 * @param taskId      for workflow workrders [optional{ssvtquick,full,ib-wf}]
	 * @return
	 */
	public Map<String, Integer> getBpmnTaskIdMapForWorkorderId(String workorderId, Integer taskId) {

		List<BPMNWorkorder> bpmnWorkorderList = bpmnWorkorderDao.getBpmWoListByGenericWoName(workorderId);
		if (bpmnWorkorderList != null && bpmnWorkorderList.size() > ForesightConstants.ZERO_INT) {
			List<BpmnTask> bpmnTaskList = bpmnTaskDao
					.getBpmnTaskByWorkorderId(bpmnWorkorderList.get(ForesightConstants.ZERO_INT).getId());
			return getBpmnTaskIdMap(bpmnTaskList, taskId);
		}
		return null;
	}

	@Override
	@Transactional
	public AnalyticsRepository insertWOReportConfigData(Integer workorderId, TemplateType templateType, String user,
			GenericWorkorder genericWorkorder) {
		logger.info("inside insertWOReportConfigData going to insert wo report config data for workorderId : {}",
				workorderId);
		try {
			JSONObject reportConfigJsonObj = getJsonObjectByTemplate(templateType, workorderId, user, genericWorkorder);

			AnalyticsRepository analyticsRepositiryObj = new AnalyticsRepository();
			analyticsRepositiryObj.setReportConfig(reportConfigJsonObj.toString());
			analyticsRepositiryObj.setName(workorderId + "-" + templateType.toString());
			analyticsRepositiryObj.setProgress(progress.In_Progress);
			analyticsRepositiryObj.setRepositoryType(RepositoryType.CUSTOM_REPORT);
			analyticsRepositiryObj.setModuleName(NVConstant.MODULE_NAME);
			String template = getTemplateTypeWithSSVTCheck(templateType.toString());
			ReportTemplate reportTemplate = reportTemplateDao.getReportTemplateByReportType(template);
			logger.info("going to createCustomReportRepository template: " + reportTemplate.toString());
			return analyticsRepositoryService.createCustomReportRepository(reportTemplate.getId(),
					analyticsRepositiryObj);
		} catch (JSONException e) {
			logger.error("JSONException in insertReportConfigData {}", Utils.getStackTrace(e));
			throw new RestException("Invalid workorderId");
		}
	}

	private String getTemplateTypeWithSSVTCheck(String templateType) {
		if (NVWorkorderConstant.NV_SSVT.equals(templateType)) {
			return NVWorkorderConstant.SSVT_TEMPLATE_TYPE_JSON;
		}
		return templateType;
	}

	private JSONObject getJsonObjectByTemplate(TemplateType templateType, Integer workorderId, String user,

			GenericWorkorder genericWorkorder) {
		JSONObject reportConfigJsonObj = new JSONObject();
		if (templateType.equals(TemplateType.NV_ADHOC_BRTI_ST) || templateType.equals(TemplateType.NV_ADHOC_BRTI_DRIVE)
				|| templateType.equals(TemplateType.NV_BRTI)) {
			reportConfigJsonObj = getJsonObjectForBRTIReport(genericWorkorder);
		} else {
			reportConfigJsonObj.put(WORKORDER_ID, workorderId);
			reportConfigJsonObj.put(ASSIGNED_TO, user);
		}
		return reportConfigJsonObj;
	}

	private JSONObject getJsonObjectForBRTIReport(GenericWorkorder genericWorkorder) {
		JSONObject reportConfigJsonObj;
		String json = QMDLConstant.BRTI_REPORT_JSON;
		reportConfigJsonObj = new JSONObject(json);
		JSONObject reportJson = reportConfigJsonObj.getJSONObject(QMDLConstant.BRTI_DATA_KEY);
		Quarter quarter = new Quarter(genericWorkorder.getCreationTime());
		if (genericWorkorder.getGeographyl3() != null) {
			reportJson.put(QMDLConstant.GEOGRAPHY_LIST,
					NVWorkorderUtils.getListByValues(genericWorkorder.getGeographyl3().getId()));
		}
		reportJson.put(QMDLConstant.WORKORDER_LIST, NVWorkorderUtils.getListByValues(genericWorkorder.getId()));
		reportConfigJsonObj.put(QMDLConstant.YEAR_KEY, quarter.getYearValue());
		reportConfigJsonObj.put(QMDLConstant.QUATER_KEY, quarter.getQuarter());
		return reportConfigJsonObj;
	}

	/**
	 * Gets the boundary response object.
	 *
	 * @param geographyName  the geography name
	 * @param geographyLevel the geography level
	 * @param templateType 
	 * @param bufferSize 
	 * @return the boundary response object
	 */
	private Response getBoundaryResponseObject(String geographyName, String geographyLevel, Double latitude,
			Double longitude, String zoom, Double bufferSize,String templateType) {
		List<String> geographyNameList = new ArrayList<>();
		geographyNameList.add(geographyName);
		String boundaryColumnName = getBoundaryColumnName(zoom);
		List<Map<String, String>> resultList = genericMapService.getBoundaryDataByGeographyNames(geographyNameList,
		GenericMapUtils.GEOGRAPHY_TABLE_NAME, getBoundaryColumnList(boundaryColumnName), "", geographyLevel);
		logger.info("Done Getting Geography Boundary");
		if (resultList != null) {
			LatLng latLng = new LatLng(latitude, longitude);
			List<Map<String, String>> singleResultList = new ArrayList<>();
			for (Map<String, String> result : resultList) {
				String boundary3D = result.get(boundaryColumnName.split(Symbol.COLON_STRING)[1]);
				if (latitude != null && longitude != null && GenericMapUtils.checkLatLngInBoundary(boundary3D,
						latLng)) {
					singleResultList.add(result);
				}
				if (bufferSize != null && templateType != null && templateType.equalsIgnoreCase(NV_CLOT)) {
					addBufferBoundaryIntoMap(result, boundary3D,bufferSize);
					singleResultList.add(result);
				}
			}
			if (!singleResultList.isEmpty()) {
				return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(singleResultList, null))
						.build();
			}
		}
		return Response.ok(AuthenticationCommonUtil.checkForValueEncryption(resultList, null)).build();
	}

	private void addBufferBoundaryIntoMap(Map<String, String> result, String boundary3D, Double bufferSize) {
		List<List<List<Double>>> boundary =
		          new Gson().fromJson(boundary3D, new TypeToken<List<List<List<Double>>>>() {}.getType());
		GISGeometry geom = new GIS3DPolygon(boundary);
		List<List<List<Double>>> list = geom.putBuffer(bufferSize).getMultipolygonFromGeomerty().get(0);
		result.put("bcord", list.toString());
	}
	
	@Override
	@Transactional(readOnly = true)
	public NVWorkorderWrapper getWorkorderDetailsByWOIdFromMobile(Integer workorderId) {
		return getWorkorderDetailsByWOId(workorderId, null);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<NetworkElementWrapper> getSitesFromLatLong(String latLongData, String neStatusJson, String neTypeJson)
			throws IOException {
		HashMap dataMap = new ObjectMapper().readValue(latLongData, HashMap.class);
		List<NetworkElementWrapper> siteDataList = new ArrayList<>();
		ArrayList neStatusList = null;
		ArrayList neTypeList = null;
		if (neStatusJson != null) {
			neStatusList = new ObjectMapper().readValue(neStatusJson, ArrayList.class);
		}
		if (neTypeJson != null) {
			neTypeList = new ObjectMapper().readValue(neTypeJson, ArrayList.class);
		}

		Map<String, Double> neswLatLongMap = NVWorkorderUtils.getNESWLatLongByCenterLatLong(dataMap);

		if (neswLatLongMap != null && !neswLatLongMap.isEmpty()) {
			siteDataList = siteDetailService.getSitesFromViewPort(neswLatLongMap, neStatusList, neTypeList, 10);

			return (List<NetworkElementWrapper>) siteDataList;
		}
		return siteDataList;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private SiteSelectionWrapper getDefaultSiteSelectionWrapper() {
		SiteSelectionWrapper siteSelectionWrapper = new SiteSelectionWrapper();
		Map macroPlanned = new HashMap<>();
		macroPlanned.put("displaySite", ForesightConstants.TEN);
		macroPlanned.put("isNonScoped", false);
		macroPlanned.put("isScoped", true);
		macroPlanned.put("progressState", "ONAIR");
		macroPlanned.put("siteType", "MACRO_CELL");
		macroPlanned.put("type", "PCI");
		JSONParser jsonParser = new JSONParser();
		try {
			SystemConfiguration sysConf = iSystemConfigurationDao.getSystemConfigurationDetailForReport("Band", "BAND");
			if (sysConf != null && sysConf.getValue() != null) {
				Map<String, List<String>> map = (Map<String, List<String>>) jsonParser.parse(sysConf.getValue());
				List<String> band = map.get(InfraConstants.MACRO.toLowerCase());
				if (band != null && !band.isEmpty())
					macroPlanned.put(InfraConstants.BANDS, band);
				else
					logger.info("Macro Site Band list not  found in System Configutation");
			} else {
				logger.info("Band information not found in System Configuration");
			}
		} catch (Exception exception) {
			logger.info("Error in getting band information from System Configuration");
			throw new RestException("Unable to get Band Info");
		}
		siteSelectionWrapper.setMacroOnair(macroPlanned);
		return siteSelectionWrapper;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public List<Map> getWorkorderListByGeography(String geographyLevel, Integer geographyId,
			List<GenericWorkorder.Status> statusList, List<TemplateType> templateList, String remark) {
		List<Map> list = new ArrayList<>();
		try {
			List<GenericWorkorder> workorderList = genericWorkorderDao.findAllNVWorkorder(null, null, templateList,
					statusList, remark, geographyLevel, geographyId, null, null);
			for (GenericWorkorder genericWorkorder : workorderList) {
				List<WOFileDetail> fileDetailByWorkOrderId = woFileDetailDao.getFileDetailByWorkOrderId(genericWorkorder.getId())
						.stream().filter(f -> (f.getIsDeleted() != null && !(f.getIsDeleted()))).collect(Collectors.toList());
				List<Boolean> fileProcessedStatusList = getFileProcessedStatusList(fileDetailByWorkOrderId);
				list.add(getWorkorderIdJSON(genericWorkorder,fileProcessedStatusList));
			}
			return list;
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	private List<Boolean>  getFileProcessedStatusList(List<WOFileDetail> fileDetailByWorkOrderId) {
		List<Boolean> flowStatusList =  new ArrayList<>();
		for(WOFileDetail woFileDetail : fileDetailByWorkOrderId) {
			flowStatusList.add(getProcessedFileStatus(woFileDetail));
		}
		return flowStatusList;
	}

	private Boolean getProcessedFileStatus(WOFileDetail woFileDetail) {
		Boolean isProcessed = woFileDetail.getIsProcessed();
		logger.info("File processed status isProcessed {}",isProcessed);
		if(isProcessed != null &&  isProcessed) {
		return true;
		}
		else {
			return false;
		}
	}

	@Override
	@Transactional
	public List<RecipeWrapper> addRecipeIntoWO(NVWorkorderWrapper wrapper) {
		List<RecipeWrapper> list = new ArrayList<>();
		try {
			GenericWorkorder workorder = genericWorkorderDao.findByPk(wrapper.getId());
			TemplateType templateType = workorder.getTemplateType();
			wrapper.setTemplateType(templateType);
			List<WORecipeMapping> recipeMappingList = createWORecipeMapping(workorder, createAndGetRecipes(wrapper));
			for (WORecipeMapping recipeMapping : recipeMappingList) {
				list.add(getRecipeWrapper(recipeMapping, workorder.getGwoMeta(), templateType, null));
			}
			workorder.setModificationTime(new Date());
			genericWorkorderDao.update(workorder);
			return list;
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	@Override
	public String findFilePartByRecipeMappingId(String woRecipeMappingId) {
		Integer woId;
		try {
			woId = Integer.valueOf(AuthenticationCommonUtil.checkForValueDecryption(woRecipeMappingId));

			logger.info("Going to find File By RecipeMappingId  : {}", woId);
			List<WOFileDetail> list = woFileDetailDao.findFileByRecipeMappingId(woId);
			logger.info("received list findFileByRecipeMappingId{}", list.size());
			if (!list.isEmpty()) {
				return addFilePartsIntoJson(list);
			} else {
				return NVWorkorderConstant.EMPTY_RESULT_JSON;
			}
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}

	}

	public String addFilePartsIntoJson(List<WOFileDetail> list) {
		Set<String> filepart = new HashSet<>();
		getFilePartsName(list, filepart);
		Map<String, Set<String>> filemap = new HashMap<>();
		filemap.put(NVWorkorderConstant.RESULT, filepart);
		return new Gson().toJson(filemap);
	}

	public void getFilePartsName(List<WOFileDetail> list, Set<String> filepart) {
		for (WOFileDetail file : list) {
			String fileName = file.getFileName();
			if (fileName != null) {
				String[] fileList = fileName.split(Symbol.UNDERSCORE_STRING);
				if (NVWorkorderConstant.FILE_PART_INDEX < fileList.length) {
					filepart.add(fileList[NVWorkorderConstant.FILE_PART_INDEX]);

				}
			}
		}
	}

	@Override
	@Transactional
	public String deleteFileByRecipeMappingId(String woRecipeMappingId) {
		try {

			Gson gson = new Gson();
			List<Integer> woIdRecipeMappingList = gson.fromJson(
					AuthenticationCommonUtil.checkForValueDecryption(woRecipeMappingId),
					new TypeToken<List<Integer>>() {
					}.getType());
			List<WOFileDetail> list = woFileDetailDao.findFileByRecipeMappingIdList(woIdRecipeMappingList);
			logger.info("received list findFileByRecipeMappingId   {}", list.size());
			return updateFilemarkasDeleted(list);
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}

	}

	public String updateFilemarkasDeleted(List<WOFileDetail> list) {
		if (!list.isEmpty()) {
			return updateFileParaIsDeleted(list);
		} else {

			return FILE_NOT_FOUND_FOR_ID;
		}
	}

	public String updateFileParaIsDeleted(List<WOFileDetail> list) {
		try {
			for (WOFileDetail file : list) {
				file.setIsDeleted(true);
				if (file.getProcessedLogFilePath() != null) {
					nvLayer3HDFSDao.deleteFileFromHDFS(file.getProcessedLogFilePath());
				}
				woFileDetailDao.update(file);
			}
			return RESULT_SUCCESS_JSON;
		} catch (DaoException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			return RESULT_FAILURE_JSON;
		}
	}

	@Override
	@Transactional
	public List<String> geKpiAndEventString(Integer workrorderId, List<Integer> recipeList) {
		try {
			List<WORecipeMapping> list = woRecipeMappingDao.geKpiAndEventString(workrorderId, recipeList);
			List<String> kpiEventsList = new ArrayList<>();
			for (WORecipeMapping singlerecipe : list) {
				getListofStringfromString(kpiEventsList, singlerecipe.getKpi());
				getListofStringfromString(kpiEventsList, singlerecipe.getKpi3G());
				getListofStringfromString(kpiEventsList, singlerecipe.getKpi2G());
				getListofStringfromString(kpiEventsList, singlerecipe.getEventmobility());
				getListofStringfromString(kpiEventsList, singlerecipe.getEventcall());
				getListofStringfromString(kpiEventsList, singlerecipe.getEventdownload());
				getListofStringfromString(kpiEventsList, singlerecipe.getEventsms());
				getListofStringfromString(kpiEventsList, singlerecipe.getEventtrackarea());
			}
			logger.debug("Getting result of kpiandevents.toString() {}", kpiEventsList.toString());
			return kpiEventsList;
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());

		}

	}

	public void getListofStringfromString(List<String> kpiEventsList, String kpiEventString) {
		if (kpiEventString != null) {
			String[] kpiarray = kpiEventString.split(Symbol.COMMA_STRING);
			kpiEventsList.addAll(Arrays.asList(kpiarray));
		}
	}

	@Override
	@Transactional
	public String getWorkOrderDetailByImei(String imei, Long startTime, Long endTime, TemplateType templateType) {
		try {
			List<WORecipeMapping> list = woRecipeMappingDao.getWorkOrderDetailByImei(imei, startTime, endTime,
					templateType);
			if (list != null) {
				return new Gson().toJson(list);
			}
		} catch (DaoException e) {
			logger.error("Error in getting result for imei {}  ", ExceptionUtils.getStackTrace(e));
			return RESULT_EXCEPTION_SOMETHING_WENT_WRONG_JSON;
		}
		return EMPTY_RESULT_JSON;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public List<Map> getWorkorderListByGeographyOfPeriod(String geographyLevel, Integer geographyId,
			List<GenericWorkorder.Status> statusList, List<TemplateType> templateList, List<Integer> quarterList,
			List<Integer> yearList) {
		List<Map> list = new ArrayList<>();
		try {
			Map<String, Long> startEndTimeMap = NVWorkorderUtils.getStartEndTimeFromPeriodInfo(quarterList, yearList);
			logger.info("startEndTimeMap Data {} ", startEndTimeMap);
			List<GenericWorkorder> workorderList = genericWorkorderDao.findAllWorkorder(null, null, templateList,
					statusList, null, geographyLevel, Arrays.asList(geographyId), null,
					startEndTimeMap.get(NVWorkorderConstant.START_TIMESTAMP),
					startEndTimeMap.get(NVWorkorderConstant.END_TIMESTAMP));
			logger.info("workorderList Size {} ", workorderList != null ? workorderList.size() : null);
			for (GenericWorkorder genericWorkorder : workorderList) {
				Map map = getWorkorderDetailJSON(genericWorkorder, geographyLevel);
				if (map != null) {
					list.add(map);
				}
			}
			return list;
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	@Override
	public String updateWorkorderDetails(NVWorkorderWrapper wrapper) {
		GenericWorkorder workorder = genericWorkorderDao.findByPk(wrapper.getId());
		workorder.setRemark(wrapper.getRemark());
		genericWorkorderDao.update(workorder);
		return SUCCESS_JSON;
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public List<Map> getWorkorderListByGeographyOfPeriod(String geographyLevel, List<Integer> geographyId,
			List<GenericWorkorder.Status> statusList, List<TemplateType> templateList, List<Integer> quarterList,
			List<Integer> yearList, String technology) {
		List<Map> list = new ArrayList<>();
		try {
			Map<String, Long> startEndTimeMap = NVWorkorderUtils.getStartEndTimeFromPeriodInfo(quarterList, yearList);
			logger.info("startEndTimeMap Data {} , technology {} ", startEndTimeMap, technology);
			List<GenericWorkorder> workorderList = genericWorkorderDao.findAllWorkorder(null, null, templateList,
					statusList, null, geographyLevel, geographyId, null,
					startEndTimeMap.get(NVWorkorderConstant.START_TIMESTAMP),
					startEndTimeMap.get(NVWorkorderConstant.END_TIMESTAMP), ReportConstants.BAND_TYPE, technology);
			logger.info("workorderList Size {} ", workorderList != null ? workorderList.size() : null);
			workorderList = workorderList != null
					? getFilteredGenericWorkOrderListOfRecipeCount1(workorderList, ReportConstants.INDEX_ONE)
					: null;
			for (GenericWorkorder genericWorkorder : workorderList) {
				Map map = getWorkorderDetailJSON(genericWorkorder, geographyLevel);
				if (map != null) {
					list.add(map);
				}
			}
			return list;
		} catch (Exception e) {
			logger.error("Unable to find the list of workOrder in method getWorkorderListByGeographyOfPeriod {} ",
					Utils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	private List<GenericWorkorder> getFilteredGenericWorkOrderListOfRecipeCount1(List<GenericWorkorder> workorderList,
			int count) {
		try {
			List<GenericWorkorder> filterdList = new ArrayList<>();
			List<WORecipeWrapper> woIds = woRecipeMappingDao.getWoIdsByforExecutedCountofRecipe();
			List<Integer> filterdWoIds = woIds.stream().filter(wrapper -> (wrapper.getCount() == count))
					.map(WORecipeWrapper::getWorkorderId).collect(Collectors.toList());
			workorderList.stream().forEach(woObj -> {
				if (filterdWoIds.contains(woObj.getId()))
					filterdList.add(woObj);
			});
			logger.debug("Input workOrder List {} ,filterdWoIds ",
					workorderList.stream().map(GenericWorkorder::getId).collect(Collectors.toList()), filterdWoIds);
			return filterdList;
		} catch (Exception e) {
			logger.error("Unable to Filter out the workorderids of count {} , {} ", count, e.getMessage());
		}
		return workorderList;
	}

	@Override
	@Transactional
	public String reassignWorkorder(Integer taskId, String userName, String remark) {
		BpmnTask bpmnTask = bpmnTaskDao.findByPk(taskId);
		if (!bpmnTask.getStatus().equals(com.inn.bpmn.model.BpmnTask.Status.Completed)) {
			User oldUser = bpmnTaskCandidateDao.getBpmnTaskCandidatesByBpmnTaskId(taskId).get(ForesightConstants.ZERO)
					.getUser();
			BPMNWorkorder bpmnWorkoder = bpmnTask.getBpmnWorkorder();
			GenericWorkorder genericWorkorder = genericWorkorderService
					.getWorkorderByWOId(bpmnWorkoder.getWorkorderNo());
			bpmnTaskCandidateService.setAssigneeType(BpmnConstants.ASSIGNEE, userName, taskId, null, null, null, null);
			genericWorkorder.setRemark(remark);
			genericWorkorder.setModificationTime(new Date());
			
			User user = userDao.findByUserName(userName);
			genericWorkorder.setAssignedTo(user);
			genericWorkorderDao.update(genericWorkorder);

			try {
				pushNotificationAsyncService.sendNotificationAndUpdateStatus(
						createNotification(deviceInfoDao.getDeviceInfoByUser(oldUser.getUserid()),
								genericWorkorder.getTemplateType(),
								new Gson().toJson(getWOReassignedNotificationPayload(
										genericWorkorder.getTemplateType().name(), genericWorkorder.getWorkorderId())),
								true));
				pushNotificationAsyncService.sendNotificationAndUpdateStatus(
						createNotification(deviceInfoDao.getDeviceInfoByUser(user.getUserid()),
								genericWorkorder.getTemplateType(),
								new Gson()
										.toJson(getWONotificationPayload(genericWorkorder.getTemplateType().name(),
												NVWorkorderUtils.getWorkorderMessageForUser(
														genericWorkorder.getTemplateType(), user.getFirstName()))),
								true));
			} catch (Exception e) {
				logger.error("Unable to send Notification. Exception {} ", Utils.getStackTrace(e));
			}
		}
		return SUCCESS_JSON;
	}

	@Override
	public String deleteWorkorder(Integer taskId) {
		BpmnTask bpmnTask = bpmnTaskDao.findByPk(taskId);
		if (!bpmnTask.getStatus().equals(com.inn.bpmn.model.BpmnTask.Status.Completed)) {
			User user = bpmnTaskCandidateDao.getBpmnTaskCandidatesByBpmnTaskId(taskId).get(ForesightConstants.ZERO)
					.getUser();
			BPMNWorkorder bpmnWorkoder = bpmnTask.getBpmnWorkorder();
			bpmnWorkoder.setDeleted(true);
			GenericWorkorder genericWorkorder = genericWorkorderService
					.getWorkorderByWOId(bpmnWorkoder.getWorkorderNo());
			genericWorkorder.setDeleted(true);
			genericWorkorder.setModificationTime(new Date());
			bpmnWorkorderDao.update(bpmnWorkoder);
			genericWorkorderDao.update(genericWorkorder);
			try {
				pushNotificationAsyncService.sendNotificationAndUpdateStatus(
						createNotification(deviceInfoDao.getDeviceInfoByUser(user.getUserid()),
								genericWorkorder.getTemplateType(),
								new Gson().toJson(getWODeleteNotificationPayload(
										genericWorkorder.getTemplateType().name(), genericWorkorder.getWorkorderId())),
								true));

			} catch (Exception e) {
				logger.error("Unable to send Notification. Exception {} ", Utils.getStackTrace(e));
			}
		}
		return SUCCESS_JSON;
	}

	@Override
	public List<NVWorkorderWrapper> getWorkorderStatusByTaskIdList(String encrTaskIdList) {
		List<NVWorkorderWrapper> list = new ArrayList<>();
		try {
			List<Integer> taskIdList = new Gson().fromJson(
					AuthenticationCommonUtil.checkForValueDecryption(encrTaskIdList), new TypeToken<List<Integer>>() {
					}.getType());
			List<BpmnTaskCandidate> taskCandidateList = bpmnTaskCandidateDao
					.getBPMNTaskCandidateListByTaskIdList(taskIdList);
			Integer currentUserId = UserContextServiceImpl.getUserInContext().getUserid();
			;

			for (BpmnTaskCandidate taskCandidate : taskCandidateList) {
				if (taskCandidate.getBpmnTask().getBpmnWorkorder().getDeleted()) {
					list.add(getWrapperWithUpdatedStatus(taskCandidate, DELETED_STATUS));
				} else if (!currentUserId.equals(taskCandidate.getUser().getUserid())) {
					list.add(getWrapperWithUpdatedStatus(taskCandidate, REASSIGNED_STATUS));
				} else if (taskCandidate.getBpmnTask().getBpmnWorkorder().getCompletionStatus()
						.equals(CompletionStatus.Completed)) {
					list.add(getWrapperWithUpdatedStatus(taskCandidate, COMPLETED_STATUS));
				}
			}
			Boolean isWoRemoveReq = ConfigUtils.getBoolean(NVConfigUtil.IS_WO_REMOVE_REQ);
			if(isWoRemoveReq.booleanValue()) {
				String value = systemConfigurationDao.getValueByName("NV_SSVT_REMOVE_TASKID_LIST");
				List<Integer> taskList = new Gson().fromJson(value, new TypeToken<List<Integer>>() {}.getType());
				for(Integer taskId : taskList) {
					NVWorkorderWrapper wrapper = new NVWorkorderWrapper();
					wrapper.setTaskId(taskId);
					wrapper.setStatus(DELETED_STATUS);
					list.add(wrapper);
				}
				
			}
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
		return list;
	}
	private NVWorkorderWrapper getWrapperWithUpdatedStatus(BpmnTaskCandidate taskCandidate, String status) {
		NVWorkorderWrapper wrapper = new NVWorkorderWrapper();
		wrapper.setTaskId(taskCandidate.getBpmnTask().getId());
		wrapper.setStatus(status);
		return wrapper;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Map getWorkorderDetailJSON(GenericWorkorder genericWorkorder, String geographyLevel) {
		try {
			Map map = new HashMap<>();
			map.put(VALUE, genericWorkorder.getId());
			map.put(NAME, genericWorkorder.getWorkorderId());
			map.put(REMARK, genericWorkorder.getRemark());
			map.put(CREATION_TIME, genericWorkorder.getCreationTime());
			map.put(END_TIME, genericWorkorder.getCompletionTime());
			map.put(MODIFIED_TIME, genericWorkorder.getModificationTime());
			map.put(TYPE_DRIVE, genericWorkorder.getTemplateType().toString());
			map.put(ReportConstants.BAND_TYPE, getBandTypeDetail(genericWorkorder.getGwoMeta()));
			if (!geographyLevel.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L3)) {
				if (genericWorkorder.getGeographyl3() != null) {
					map.put(GEOGRAPHY_NAME, getGeographyNameByLevel(genericWorkorder, geographyLevel));
					map.put(ForesightConstants.GEOGRAPHY_L3,
							getGeographyNameByLevel(genericWorkorder, ForesightConstants.GEOGRAPHY_L3));
					map.put(GEOGRAPHYL3_ID, genericWorkorder.getGeographyl3().getId());
					if (genericWorkorder.getGeographyl4() != null) {
						map.put(ForesightConstants.GEOGRAPHY_L4,
								getGeographyNameByLevel(genericWorkorder, ForesightConstants.GEOGRAPHY_L4));
						map.put(GEOGRAPHYL4_ID, genericWorkorder.getGeographyl4().getId());
					}

				} else {
					return null;
				}
			}
			return map;
		} catch (Exception e) {
			return null;
		}
	}

	private String getBandTypeDetail(Map<String, String> gwoMeta) {
		try {
			return gwoMeta.get(ReportConstants.BAND_TYPE);
		} catch (Exception e) {
			logger.debug("Unable to get Band Detail for bandType {} ", e.getMessage());
		}
		return null;
	}

	private String getGeographyNameByLevel(GenericWorkorder genericWorkorder, String geographyLevel) {
		if (geographyLevel != null) {
			if (geographyLevel.equals(ForesightConstants.GEOGRAPHY_L1)) {
				return genericWorkorder.getGeographyl1().getName();
			} else if (geographyLevel.equals(ForesightConstants.GEOGRAPHY_L2)) {
				return genericWorkorder.getGeographyl2().getName();
			} else if (geographyLevel.equals(ForesightConstants.GEOGRAPHY_L3)) {
				return genericWorkorder.getGeographyl3().getName();
			} else if (geographyLevel.equals(ForesightConstants.GEOGRAPHY_L4)) {
				return genericWorkorder.getGeographyl4().getName();
			}
		}
		return null;
	}

	@Override
	@Transactional
	public List<NVWorkorderWrapper> fetchStealthWorkOrder(List<StealthWOParameters> list) {
		try {
			logger.info("Going to Fetch Workorder details for {} workorders", list);
			List<NVWorkorderWrapper> nvWorkorderWrappers = new ArrayList<>();
			if (list != null && !list.isEmpty()) {
				for (StealthWOParameters stealthWOParameters : list) {
					if (stealthWOParameters != null) {
						fetchStealthWO(nvWorkorderWrappers, stealthWOParameters);
					}
				}
			}
			logger.info("Total Workorder Fetched {} ", nvWorkorderWrappers.size());
			return nvWorkorderWrappers;
		} catch (Exception e) {
			logger.error("Exception In getting getWorkorderDetailsByWOId : {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	private void fetchStealthWO(List<NVWorkorderWrapper> nvWorkorderWrappers, StealthWOParameters stealthWOParameters) {
		try {
			Integer woId = stealthWOParameters.getWoId();
			if (woId != null) {
				fetchStealthWorkorderByWOId(nvWorkorderWrappers, stealthWOParameters, woId);
			} else {
				fectStealthByDeviceId(nvWorkorderWrappers, stealthWOParameters);
			}
		} catch (Exception exception) {
			logger.error("Error in fetching workorder {} exception {} ", stealthWOParameters.getWoId(),
					Utils.getStackTrace(exception));
		}
	}

	private void fectStealthByDeviceId(List<NVWorkorderWrapper> nvWorkorderWrappers,
			StealthWOParameters stealthWOParameters) {
		if (stealthWOParameters.getDeviceId() != null) {
			logger.info("Going to get workorder for device id {} ", stealthWOParameters.getDeviceId());
			List<GenericWorkorder> workOrders = stealthTaskDetailDao.getWOListByDeviceIdAndModificationtime(
					stealthWOParameters.getDeviceId(), 0L,stealthWOParameters.getUserId());

			List<String> woIdList = new ArrayList<>();
			if (workOrders != null && !workOrders.isEmpty()) {
				logger.info("Found the workorder list size {}", workOrders.size());
				addWoDetails(nvWorkorderWrappers, stealthWOParameters, workOrders, woIdList);
			}
			
			
			autoCreateWOForUserandGeography(nvWorkorderWrappers, stealthWOParameters, woIdList);
		}
	}

	private void addWoDetails(List<NVWorkorderWrapper> nvWorkorderWrappers, StealthWOParameters stealthWOParameters,
			List<GenericWorkorder> workOrders, List<String> woIdList) {
		Integer woId;
		logger.info("Total workorder fetched  {}", workOrders.size());
		for (GenericWorkorder genericWorkorder : workOrders) {
			woId = genericWorkorder.getId();
			try {
				woIdList.add(genericWorkorder.getWorkorderId());
				if (genericWorkorder != null && genericWorkorder.getGwoMeta() != null
						&& genericWorkorder.getGwoMeta().get(StealthConstants.ASSIGNMENT_TYPE) != null
						&& genericWorkorder.getGwoMeta().get(StealthConstants.ASSIGNMENT_TYPE)
								.equalsIgnoreCase(StealthConstants.AssigmentType.ASSIGN_TO_USER.name())) {

				}
				addWorkOrderDetails(stealthWOParameters, genericWorkorder, nvWorkorderWrappers);
			} catch (Exception exception) {
				logger.warn("unable to send workorder {}  exceptipn {} ", woId, Utils.getStackTrace(exception));
			}
		}
	}

	private void fetchStealthWorkorderByWOId(List<NVWorkorderWrapper> nvWorkorderWrappers,
			StealthWOParameters stealthWOParameters, Integer woId) {
		GenericWorkorder genericWorkorder = genericWorkorderDao.findByPk(woId);
		if (genericWorkorder != null) {
			if (genericWorkorder.getStatus() != null
					&& (!genericWorkorder.getStatus().name().equalsIgnoreCase(Status.COMPLETED.name()))) {
				logger.info("Generic wo fetched {} ", genericWorkorder);
				addWorkOrderDetails(stealthWOParameters, genericWorkorder, nvWorkorderWrappers);
			} else {
				logger.info("Workorder is already Completed woId {} ", woId);
			}
		}
	}

	private void autoCreateWOForUserandGeography(List<NVWorkorderWrapper> nvWorkorderWrappers,
			StealthWOParameters stealthWOParameters, List<String> woIdList) {
		try {
			logger.info("Going for  auto creation of stealth wo for stealthWOParameters {} ", stealthWOParameters);
			if (stealthWOParameters.getUserId() != null) {
				Integer userId = stealthWOParameters.getUserId();
				User user = userDao.findByPk(userId);
				if (user != null && user.getUserName() != null) {

					List<WOUserMapping> list = woUserMappingDao.findByUserId(user.getUserid());
					List<GenericWorkorder> workorders = list.stream().map(c -> c.getGenericWorkorder())
							.collect(Collectors.toList());
					if (CollectionUtils.isEmpty(list)) {
						workorders.addAll(genericWorkorderDao.getGenericWOForUser(user.getUserName()));
					}

					if (workorders != null && !workorders.isEmpty()) {
						logger.info("Total workorder fetched for User {} ", stealthWOParameters.getUserId());
						SystemConfiguration sysConf = iSystemConfigurationDao
								.getSystemConfigurationDetailForReport("DEFAULT_OPERATOR", "NVProfileData");
						if (sysConf != null && sysConf.getValue() != null) {
							String operator = sysConf.getValue();
							DateFormat dateFormat = DateUtil.DATE_TIME_DATEFORMAT;
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(dateFormat.parse(dateFormat.format(new Date())));
							for (GenericWorkorder genericWorkorder : workorders) {
								Map<String, String> meta = genericWorkorder.getGwoMeta();
								if (genericWorkorder != null && !woIdList.contains(genericWorkorder.getWorkorderId())) {
									Calendar startDate = Calendar.getInstance();
									Date date = genericWorkorder.getStartDate();
									startDate.setTime(dateFormat.parse(dateFormat.format(date)));
									String assigment = meta.get(StealthConstants.ASSIGNMENT_TYPE);
									String isProbeTest = meta.get(StealthConstants.IS_PROBE_TEST);
									
								 if (assigment.equals(AssigmentType.ASSIGN_TO_USER.name())) {
										if (("ONETIME".equalsIgnoreCase(meta.get("woFrequency"))
												&& calendar.after(startDate))
												|| (NVConstant.HOURLY.equalsIgnoreCase(meta.get("woFrequency"))
														&& calendar.after(startDate))) {
											logger.info(
													"Start time has already passed so Workorder will not be assigned ");
											continue;
										}
									}
										
										NVDeviceData nvDeviceData = nvDeviceDataService
												.getDeviceByDeviceId(stealthWOParameters.getDeviceId());
										List<Integer> nvDeviceList = new ArrayList<>();
										if (nvDeviceData != null && nvDeviceData.getDeviceInfo() != null
												&& operator.equalsIgnoreCase(nvDeviceData.getOperator().trim())) {
											nvDeviceList.add(nvDeviceData.getDeviceInfo().getId());
											createNotificationForAllDevices(nvDeviceList, genericWorkorder, meta,
													false,true);
											woIdList.add(genericWorkorder.getWorkorderId());
											addWorkOrderDetails(stealthWOParameters, genericWorkorder,
													nvWorkorderWrappers);
										} else {
											logger.info("Operator is not valid to assign Workorder");
										}
									}
								
							}
						} else {
							logger.info("Default Operator not found  so cannot assign Stealth WO");
						}
					} else {
						logger.info("user Info not found for {} ", stealthWOParameters);
					}
				}
			}
		} catch (Exception exception) {
			logger.error("Unable to add assign auto Stealth WO {}", Utils.getStackTrace(exception));
		}
	}
	
	
	

	private void addWorkOrderDetails(StealthWOParameters stealthWOParameters, GenericWorkorder genericWorkorder,
			List<NVWorkorderWrapper> nvWorkorderWrappers) {
		try {
			logger.info("Inside mehtod @addWorkOrderDetails");
			String deviceId = stealthWOParameters.getDeviceId();
			Integer woId = genericWorkorder.getId();
			NVDeviceData device = nvDeviceDataService.findNVDeviceDataByDeviceId(deviceId);
			if (device != null) {
				Integer nvDeviceId = device.getId();
				List<WORecipeMapping> woRecipeMappingList = new ArrayList();

				String recipeId = genericWorkorder.getGwoMeta().getOrDefault(NVConstant.WO_RECIPE_ID, null);

				if (recipeId == null) {
					woRecipeMappingList = woRecipeMappingDao.getWORecipeByGWOId(woId);
				} else {
					woRecipeMappingList.add(woRecipeMappingDao.getWORecipeMappingById(Integer.valueOf(recipeId)));
				}

				Map<String, Integer> bpmnTaskIdMap = getBpmnTaskIdMapForWorkorderId(genericWorkorder.getWorkorderId(),
						null);

				NVWorkorderWrapper nvWorkorderWrapper = getNVWorkorderMapForStealth(woRecipeMappingList, bpmnTaskIdMap)
						.get(genericWorkorder.getWorkorderId());

				Map<String, String> meta = genericWorkorder.getGwoMeta();
				nvWorkorderWrapper.setMetaData(meta);
				StealthTaskDetail stealthTaskDetail = stealthTaskDetailDao.getStealthTaskByDeviceAndWOId(nvDeviceId,
						woId);

				allotRandomizedTime(nvWorkorderWrapper, meta, stealthTaskDetail);

				if (stealthTaskDetail != null) {
					if (stealthTaskDetail.getAcknowledgement().equalsIgnoreCase(Acknowledgement.REJECT.name())) {
						logger.info("Workorder is already rejected by device  , deviceId{}  woId{}", nvDeviceId, woId);
						return;
					}
					nvWorkorderWrapper.setStealthTaskId(stealthTaskDetail.getId());
					nvWorkorderWrapper.setAcknowledgement(stealthTaskDetail.getAcknowledgement());
				}
				nvWorkorderWrappers.add(nvWorkorderWrapper);
			}
		} catch (NoResultException | EmptyResultDataAccessException e) {
			logger.error("NoResultException in addWorkOrderDetails :{} ", e.getMessage());
		}
		catch (Exception exception) {
			logger.error("Error adding workorder details in list {} ", Utils.getStackTrace(exception));
		}
	}

	private void allotRandomizedTime(NVWorkorderWrapper nvWorkorderWrapper, Map<String, String> meta,
			StealthTaskDetail stealthTaskDetail) {
		if (meta.get(StealthConstants.IS_RANDOMIZATION) != null && meta.get(StealthConstants.GWO_FREQUENCY_KEY) != null
				&& meta.get(StealthConstants.IS_RANDOMIZATION).equalsIgnoreCase(ForesightConstants.TRUE_STRING)
				&& StealthConstants.WO_TYPE_ONETIME.equalsIgnoreCase(meta.get(StealthConstants.GWO_FREQUENCY_KEY))) {
			logger.info("Going to set randomized allocated time");
			nvWorkorderWrapper.setStartTime(stealthTaskDetail.getStartTime().getTime());
		}
	}

	@Override
	public Response deleteFilesFromWoFileDetail(List<Integer> idList) {
		try {
			List<WOFileDetail> list = woFileDetailDao.getFileDetailByIds(idList);
			return Response.ok(updateFilemarkasDeleted(list)).build();
		} catch (Exception e) {
			logger.error("deleteFilesFromWoFileDetail", ExceptionUtils.getStackTrace(e));
		}
		return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();

	}

	@Override
	public Response updatePresetIdForWorkorder(Integer presetId, Integer workorderId) {
		try {
			insertOrUpdateEntityValueInGWOMeta(NVConstant.PRESET_ID, presetId, workorderId, null);

		} catch (Exception e) {
			logger.info("Error while updating preset id: {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(InBuildingConstants.FAILURE_JSON).build();
		}
		return Response.ok(InBuildingConstants.SUCCESS_JSON).build();
	}

	@Override
	public GenericWorkorder updateWorkorderArchivedStatus(Integer workorderId, Boolean isArchive) {
		GenericWorkorder genericWorkorder = null;
		try {
			genericWorkorder = genericWorkorderDao.findByPk(workorderId);
			if (genericWorkorder != null) {
				genericWorkorder.setIsarchived(isArchive);
				genericWorkorder = genericWorkorderDao.update(genericWorkorder);
				logger.info("updated archive status for workorderid : {} ,isArchive: {} ", workorderId, isArchive);
			}
		} catch (DaoException e) {
			logger.error("Error in getting data : {}", Utils.getStackTrace(e));
		}

		return genericWorkorder;
	}

	@Override
	public void insertOrUpdateEntityValueInGWOMeta(String entityType, Object entityValue, Integer workorderId,
			String woMapType) {
		logger.info("Going to insert/update value of entityType: {} in GWOMeta", entityType);
		GenericWorkorder genericWorkorder = null;
		try {
			genericWorkorder = genericWorkorderDao.findByPk(workorderId);
			if (genericWorkorder != null) {
				Map<String, String> gwoMeta = genericWorkorder.getGwoMeta();
				if (gwoMeta == null) {
					gwoMeta = new HashMap<>();
				}
				if (woMapType != null) {
					gwoMeta.put(NVWorkorderConstant.WO_MAP_TYPE, woMapType);
				}
				if (entityType != null && entityValue != null) {

					gwoMeta.put(entityType, String.valueOf(entityValue));
					logger.info("Entity value updated in GWOMeta successfully for entityType: {}", entityType);

					genericWorkorder.setGwoMeta(gwoMeta);
					genericWorkorderDao.update(genericWorkorder);
				} else {
					logger.info("Invalid input parameters: entityType: {}, entityValue: {}", entityType, entityValue);
					throw new RestException(Constants.INVALID_PARAMETERS);
				}
			} else {
				throw new RestException("Unable to get data for generic work order id: " + workorderId);
			}
		} catch (DaoException daoException) {
			logger.error("Error while updating workorder meta data: {}", daoException.getMessage());
			throw new RestException("Unable to update meta  data for workorderId: " + workorderId);
		} catch (Exception e) {
			logger.error("Error while updating workorder meta data: {}", ExceptionUtils.getStackTrace(e));
			throw new RestException("Unable to update meta  data for workorderId: " + workorderId);
		}
	}

	@Override
	public Response updateRecipeCustomGeographyMapInGWOMeta(String workOrderId, Integer customGeographyId,
			String woMapType) {
		logger.info("Going to update custom geography id: {} for generic wo id: {}", customGeographyId, workOrderId);
		try {
			if (workOrderId != null && customGeographyId != null) {
				GenericWorkorder genericWorkorder = genericWorkorderDao.getWorkorderByWOId(workOrderId);
				List<WORecipeMapping> woRecipeMappingList = woRecipeMappingDao
						.getWORecipeByGWOId(genericWorkorder.getId());
				if (woRecipeMappingList != null && !woRecipeMappingList.isEmpty()) {
					Map<String, Integer> recipeCustomGeographyMap = new HashMap<>();
					for (WORecipeMapping woRecipeMapping : woRecipeMappingList) {
						recipeCustomGeographyMap.put(String.valueOf(woRecipeMapping.getId()), customGeographyId);
					}
					String recipeVsCustomGeographyJson = new Gson().toJson(recipeCustomGeographyMap);
					insertOrUpdateEntityValueInGWOMeta(NVWorkorderConstant.RECIPE_CUSTOM_GEOGRAPHY_MAP,
							recipeVsCustomGeographyJson, genericWorkorder.getId(), woMapType);
				}
				return Response.ok(InBuildingConstants.SUCCESS_JSON).build();

			}
			return Response.ok(Constants.INVALID_PARAMETERS).build();
		} catch (DaoException daoException) {
			logger.error("Error while updating Recipe vs CustomGeographyMap in GWOMeta: {} ", daoException.getMessage(),
					ExceptionUtils.getStackTrace(daoException));
			throw new RestException(daoException.getMessage());
		} catch (RestException e) {
			logger.error("Error while updating Recipe vs CustomGeographyMap in GWOMeta: {} ", e.getMessage());
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("Error while updating Recipe vs CustomGeographyMap in GWOMeta: {}",
					ExceptionUtils.getStackTrace(e));
			throw new RestException("Unable to update Custom Geography in GWO Meta :" + workOrderId);
		}
	}

	@Override
	public Response checkIfReportAvailabel(String workorderId) {
		logger.info("inside the method checkIfReportAvailabel for  workorderId id is {}", workorderId);
		try {
			GenericWorkorder genericWorkorder = genericWorkorderDao.getWorkorderByWOId(workorderId);
			Map<String, String> metaMap = genericWorkorder.getGwoMeta();
			if (metaMap != null && metaMap.containsKey(NVWorkorderConstant.REPORT_INSTACE_ID)) {
				logger.info("meta map is {}", metaMap);
				return Response.ok(metaMap).build();
			} else if (metaMap != null && metaMap.containsKey(NVWorkorderConstant.WALK_TEST_REPORT_INSTACE_ID)) {
				logger.info("meta map is {}", metaMap);
				return Response.ok(metaMap).build();
			}
			return Response.ok(FAILURE_JSON).build();

		} catch (Exception e) {
			logger.error("Exception inside the method checkIfReportAvailabel {}", Utils.getStackTrace(e));
			return Response.ok(FAILURE_JSON).build();

		}
	}

	@Override
	@Transactional
	public GenericWorkorder updateStatusInGenericWorkorder(String workorderId) {
		GenericWorkorder genericWorkorder = genericWorkorderService.getWorkorderByWOId(workorderId);
		logger.info("genericWorkorder is {}", genericWorkorder);
		genericWorkorder.setStatus(com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.Status.COMPLETED);
		return genericWorkorderDao.update(genericWorkorder);
	}

	@Override
	@Transactional
	public Boolean updateHoldStatusInGenericWorkorder(String workorderId, String remark) {
		logger.info("value of workorder id and remark is {} {}", workorderId, remark);
		GenericWorkorder genericWorkorder;
		try {
			genericWorkorder = genericWorkorderService.getWorkorderByWOId(workorderId);
			Integer woIdPk = genericWorkorder.getId();
			logger.info("Getting workorder id pk is {}", woIdPk);
			List<WORecipeMapping> woRecipeMappingByWorkOrderId = woRecipeMappingDao
					.getWoRecipeMappingByWorkOrderId(woIdPk);
			for (WORecipeMapping worecipemapping : woRecipeMappingByWorkOrderId) {
				if (worecipemapping.getStatus() != Status.COMPLETED) {
					worecipemapping.setStatus(Status.ON_HOLD);
					woRecipeMappingDao.update(worecipemapping);
				}
			}
			genericWorkorder.setStatus(GenericWorkorder.Status.ON_HOLD);
			genericWorkorder.setRemark(remark);
//			genericWorkorder.setCompletionPercentage(NumberUtils.DOUBLE_ZERO);
			genericWorkorder.setModificationTime(new Date());

			genericWorkorderDao.update(genericWorkorder);
			return ForesightConstants.TRUE;
		} catch (DaoException e) {
			logger.info("Exception inside updateHoldStatusInGenericWorkorder {}", ExceptionUtils.getStackTrace(e));
		}
		return ForesightConstants.FALSE;
	}

	@Override
	@Transactional
	public Response WOReopenByWOId(String workorderId) {
		logger.info("inside method WOReopenByWOId ,value of workorder id {}", workorderId);
		GenericWorkorder genericWorkorder;
		try {
			genericWorkorder = genericWorkorderService.getWorkorderByWOId(workorderId);
			Integer woIdPk = genericWorkorder.getId();
			logger.info("getting workorder id pk is {}", woIdPk);
			List<WORecipeMapping> woRecipeMappingByWorkOrderId = woRecipeMappingDao
					.getWoRecipeMappingByWorkOrderId(woIdPk);
			for (WORecipeMapping worecipemapping : woRecipeMappingByWorkOrderId) {
				logger.info("Status for WORecipeMapping {} {}", worecipemapping.getStatus(), worecipemapping.getId());
				if (worecipemapping.getStatus() != Status.COMPLETED) {
					worecipemapping.setStatus(Status.REOPEN);
					woRecipeMappingDao.update(worecipemapping);
				}
			}
			genericWorkorder.setStatus(GenericWorkorder.Status.REOPEN);
			genericWorkorder.setModificationTime(new Date());
			genericWorkorderDao.update(genericWorkorder);
			return Response.ok(NVWorkorderConstant.SUCCESS_JSON).build();
		} catch (DaoException e) {
			logger.info("Exception inside WOReopenByWOId {}", ExceptionUtils.getStackTrace(e));
		}
		return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();

	}

	@Override
	@Transactional
	public Map<String, String> createWorkorderForSSVT(NETaskDetailWrapper neWrapper) {
		Map<String, String> map = new HashMap<>();
		List<RecipeWrapper> listOfRecipes;
		try {
			if (neWrapper != null && neWrapper.getConfigurationMap() != null
					&& !neWrapper.getConfigurationMap().isEmpty()) {

				Map<String, String> jsonMap = neWrapper.getConfigurationMap();
				NVWorkorderWrapper wrapper = new NVWorkorderWrapper();
				SiteInformationWrapper networkElement = siteDetailService.getNetworkElementByNename(neWrapper.getSiteId());

				if (networkElement != null && networkElement.getSiteName()!=null && networkElement.getNeStatus().equalsIgnoreCase(NEStatus.ONAIR.name()
				)) {
					logger.info("Site Found {}", networkElement.getSiteName());
					updateWorkorderWrapper(jsonMap, wrapper, neWrapper);
						wrapper.setSiteId(networkElement.getId().toString());

						Map<String, String> geoWoMetaMap = new HashMap<>();
						geoWoMetaMap.put(NVWorkorderConstant.WO_SOURCE, NVWorkorderConstant.SERVER);
						geoWoMetaMap.put(NVWorkorderConstant.GEOGRAPHY_TYPE, NVWorkorderConstant.GEOGRAPHYL4);
						geoWoMetaMap.put(NVWorkorderConstant.GEOGRAPHY_ID,
								String.valueOf(networkElement.getGeographyId()));
						geoWoMetaMap.put(NVWorkorderConstant.IS_SITE_ACCEPTANCE,
								jsonMap.get(NVWorkorderConstant.IS_SITE_ACCEPTANCE));
						geoWoMetaMap.put(NVWorkorderConstant.PROJECT_ID, jsonMap.get(NVWorkorderConstant.PROJECT_ID));
						geoWoMetaMap.put(NVWorkorderConstant.TASK_STATUS, neWrapper.getTaskStatus());
						geoWoMetaMap.put(NVWorkorderConstant.SITE_ID, networkElement.getId().toString());
						geoWoMetaMap.put(NVWorkorderConstant.BAND, "FDD3");
						logger.info("getting data for site name  {}", neWrapper.getSiteId());
						List<SiteInformationWrapper> siteInfoWrapperList = siteDetailService
								.getMacroSiteDetailsForCellLevelForReport(null, null,
										Arrays.asList(new String[] { neWrapper.getSiteId() }), null, false, false, true);
						List<SiteInformationWrapper> siteWrapper = siteInfoWrapperList.stream()
								.filter(s -> s.getNeFrequency() != null && s.getPci() != null
										&& s.getNeFrequency().equalsIgnoreCase(NVWorkorderConstant.BAND_1800))
								.collect(Collectors.toList());
						logger.info("siteWrapper: {}", siteWrapper);

						Map<String,Integer> pciAzimuthMap= NVWorkorderUtils.getPCIAzimuthMap(siteWrapper, geoWoMetaMap);
						geoWoMetaMap.put(NVWorkorderConstant.PCI_AZIMUTH_MAP, new Gson().toJson(pciAzimuthMap).toString());
							Map<String, Object> siteInfo = new HashMap<>();
							siteInfo.put(NVWorkorderConstant.SITENAME, siteWrapper.get(0).getSiteName());
							siteInfo.put(NVWorkorderConstant.SITELATITUDE, siteWrapper.get(0).getLat());
							siteInfo.put(NVWorkorderConstant.SITELONGITUDE, siteWrapper.get(0).getLon());
							siteInfo.put(NVWorkorderConstant.NETYPE, networkElement.getSiteType());

							geoWoMetaMap.put(NVWorkorderConstant.SITE_INFO, new Gson().toJson(siteInfo));

							wrapper.setTemplateType(TemplateType.NV_SSVT);
							wrapper.setWorkorderId(NVWorkorderUtils.createPartialWorkorderId(NVWorkorderConstant.FDD3,
									NVWorkorderConstant.SSVT));

							String configurationJson = iSystemConfigurationDao.getValueByName("SSVT_SF_CONFIGURATION");
							Map<String, List<RecipeWrapper>> configurationMap = new Gson().fromJson(configurationJson,
									new TypeToken<Map<String, List<RecipeWrapper>>>() {
									}.getType());
							if (neWrapper.getTaskName().equalsIgnoreCase(ForesightConstants.ATP_1C) || neWrapper.getTaskName().equalsIgnoreCase(NVConstant.ODSC_ATP_1C)) {
								listOfRecipes = configurationMap.get(NVWorkorderConstant.SSVT_1C);
								logger.info("Getting recipes for SSVT 1C {}", listOfRecipes);
							} else {
								listOfRecipes = configurationMap.get("SSVT_1E");
								logger.info("Getting recipes for SSVT 1E {}", listOfRecipes);
							}

							NVWOUserWrapper userWrapper = new NVWOUserWrapper();
							userWrapper.setUserName(wrapper.getAssignedTo());
							List<RecipeWrapper> listOfRecipeWrapper = getRecipeWrapperList(pciAzimuthMap,
									listOfRecipes);

							wrapper.setGeoWoMetaMap(geoWoMetaMap);
							userWrapper.setRecipeList(listOfRecipeWrapper);
							wrapper.setUserList(Arrays.asList(new NVWOUserWrapper[] { userWrapper }));

							List<Integer> workorderIdList = createWorkorderForUser(wrapper, true);
							List<GenericWorkorder> workorders = workorderIdList.stream()
									.map(s -> genericWorkorderDao.findByPk(s)).collect(Collectors.toList());

							Optional<GenericWorkorder> findFirst = workorders.stream().findFirst();
							map.put(neWrapper.getSiteId(), NVWorkorderConstant.SUCCESS);
							if(findFirst.isPresent())
							{
								String workorderId = findFirst.get().getWorkorderId();
								map.put(NVWorkorderConstant.WORKORDER_ID,workorderId);
							}
						} else {
							map.put(neWrapper.getSiteId(), NVWorkorderConstant.FAILURE);
						}
			} else {
				if (neWrapper!= null && neWrapper.getSiteId() != null) {
					map.put(neWrapper.getSiteId(), NVWorkorderConstant.FAILURE);
				}
			}
			}
		 catch (Exception e) {
			logger.info("Exception inside create workorder for SSVT {}", ExceptionUtils.getStackTrace(e));
		}
		return map;
	}


	private List<RecipeWrapper> getRecipeWrapperList(Map<String, Integer> pciAzimuthMap,
													 List<RecipeWrapper> recipeWrapperList) {
		List<RecipeWrapper> listOfRecipes = new ArrayList<>();
		if (listOfRecipes != null) {
			recipeWrapperList.forEach(recipe -> {
				if (recipe.getCategory().equalsIgnoreCase(QMDLConstant.STATIONARY)) {
					pciAzimuthMap.forEach((key, value) -> {
						RecipeWrapper recipeForSSVT = new RecipeWrapper();

						recipeForSSVT.setId(recipe.getId());
						recipeForSSVT.setRecipeId(recipe.getRecipeId());
						recipeForSSVT.setIterationCount(recipe.getIterationCount());
						recipeForSSVT.setWaitTime(recipe.getWaitTime());
						recipeForSSVT.setPci(Integer.valueOf(key));
						recipeForSSVT.setAzimuth(value);
						recipeForSSVT.setRsrpThreshold(recipe.getRsrpThreshold());
						recipeForSSVT.setSinrThreshold(recipe.getSinrThreshold());
						recipeForSSVT.setLatencyThreshold(recipe.getLatencyThreshold());
						recipeForSSVT.setDlThreshold(recipe.getDlThreshold());
						recipeForSSVT.setUlThreshold(recipe.getUlThreshold());
						recipeForSSVT.setJitterThreshold(recipe.getJitterThreshold());
						recipeForSSVT.setBrowseThreshold(recipe.getBrowseThreshold());
						recipeForSSVT.setDlThresholdCriteria(recipe.getDlThresholdCriteria());
						recipeForSSVT.setUlThresholdCriteria(recipe.getUlThresholdCriteria());
						listOfRecipes.add(recipeForSSVT);
					});
				} else {
					RecipeWrapper recipeForSSVT = new RecipeWrapper();
					recipeForSSVT.setId(recipe.getId());
					recipeForSSVT.setRecipeId(recipe.getRecipeId());
					recipeForSSVT.setIterationCount(recipe.getIterationCount());
					recipeForSSVT.setWaitTime(recipe.getWaitTime());
					recipeForSSVT.setRsrpThreshold(recipe.getRsrpThreshold());
					recipeForSSVT.setSinrThreshold(recipe.getSinrThreshold());
					recipeForSSVT.setLatencyThreshold(recipe.getLatencyThreshold());
					recipeForSSVT.setDlThreshold(recipe.getDlThreshold());
					recipeForSSVT.setUlThreshold(recipe.getUlThreshold());
					recipeForSSVT.setJitterThreshold(recipe.getJitterThreshold());
					recipeForSSVT.setBrowseThreshold(recipe.getBrowseThreshold());
					recipeForSSVT.setDlThresholdCriteria(recipe.getDlThresholdCriteria());
					recipeForSSVT.setUlThresholdCriteria(recipe.getUlThresholdCriteria());

					listOfRecipes.add(recipeForSSVT);
				}
			});
		}
		return listOfRecipes;
	}
	
	@Override
	public Object findFilteredWOList(NVWorkorderWrapper wrapper, Integer lLimit, Integer uLimit, Boolean isArchive) {
		List<NVWorkorderWrapper> list = new ArrayList<>();
		if (wrapper != null) {
			if (lLimit != null && uLimit != null && uLimit > lLimit) {
				List<GenericWorkorder> workorderList = genericWorkorderDao.findFilteredWOList(wrapper, lLimit, uLimit,
						isArchive);
				if (workorderList != null && !workorderList.isEmpty()) {
					List<BpmnTaskCandidate> bpmnTaskCandidateList = bpmnTaskCandidateDao
							.getBPMNTaskCandidateListByWONoList(getWorkorderIdListFromGWO(workorderList));
					Map<String, BpmnTaskCandidate> candiateMap = getBpmnTaskCandidateMap(bpmnTaskCandidateList);
					for (GenericWorkorder genericWorkorder : workorderList) {
						BpmnTaskCandidate candidate = candiateMap.get(genericWorkorder.getWorkorderId());
						list.add(getNVWokorderWrapper(genericWorkorder, candidate));
					}
					return list;
				}
			} else {
				return genericWorkorderDao.getCountByFilter(wrapper, isArchive);
			}
		}
		return list;
	}
	
	void updateWorkorderWrapper(Map<String, String> jsonMap,NVWorkorderWrapper wrapper,NETaskDetailWrapper neWrapper){
		String assignedBy = userDao.findUserByUsernameOrEmail(jsonMap.get(NVWorkorderConstant.ASSIGNED_BY),
				jsonMap.get(NVWorkorderConstant.ASSIGNED_BY)).getUserName();
		String assignedTo = userDao.findUserByUsernameOrEmail(jsonMap.get(NVWorkorderConstant.ASSIGNED_TO),
				jsonMap.get(NVWorkorderConstant.ASSIGNED_TO)).getUserName();

		if (assignedBy != null && assignedTo != null) {

			wrapper.setAssignedBy(assignedBy);
			wrapper.setAssignedTo(assignedTo);
			wrapper.setStartDate(neWrapper.getStartDate());
			wrapper.setEndDate(neWrapper.getEndDate());
			wrapper.setDueDate(Long.parseLong(jsonMap.get(NVWorkorderConstant.DUE_DATE)));
			wrapper.setWorkorderName(jsonMap.get(NVWorkorderConstant.WORKORDER_NAME));
		}
	}

	@Override
	public GWOMeta getGWOMetaByRecipeId(Integer recipeId) {
		WORecipeMapping recipeMapping = woRecipeMappingDao.findByPk(recipeId);
		
		GenericWorkorder workorderObj = recipeMapping.getGenericWorkorder();

        GWOMeta gwoMeta = igwoMetaDao.getGwoMetaDataByGenericWorkorderIdforReport(workorderObj.getId(), recipeId+"_recipeSummary");
        return gwoMeta;
    }
	
	
	@Override
	@Transactional
	public GWOMeta prepareGWOMetaData(GWOMeta gwoMetaResponse, JSONObject jsonObject, 
							GenericWorkorder workorderObj, Integer recipeId) throws DaoException {
		
		   if(gwoMetaResponse != null) {
	        	gwoMetaResponse.setEntityValue(jsonObject.toString());
	        	return igwoMetaDao.update(gwoMetaResponse);
	        } else {
	        	gwoMetaResponse = new GWOMeta();
	        	gwoMetaResponse.setGenericWorkOrder(workorderObj);
	        	gwoMetaResponse.setEntityType(recipeId+"_recipeSummary");
	        	gwoMetaResponse.setEntityValue(jsonObject.toString());
	        	return igwoMetaDao.create(gwoMetaResponse);
	        }
	}
}
