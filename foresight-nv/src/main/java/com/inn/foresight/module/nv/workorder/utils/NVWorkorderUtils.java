package com.inn.foresight.module.nv.workorder.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.data.time.Quarter;
import org.jfree.data.time.Year;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.inn.bpmn.model.BpmnTask;
import com.inn.bpmn.model.BpmnTaskCandidate;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpGetRequest;
import com.inn.commons.http.HttpPostRequest;
import com.inn.commons.lang.DateUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.unit.Duration;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.Status;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.recipe.model.Recipe;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.RecipeWrapper;
import com.inn.foresight.module.nv.workorder.wrapper.NVWOUserWrapper;
import com.inn.foresight.module.nv.workorder.wrapper.NVWorkorderWrapper;
import com.inn.product.security.utils.AuthenticationCommonUtil;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;
import com.inn.product.um.user.utils.UmConstants;

/** The Class NVWorkorderUtils. */
public class NVWorkorderUtils implements NVWorkorderConstant {

	private static Logger logger = LogManager.getLogger(NVWorkorderUtils.class);

	/**
	 * Gets All Recipe Ids From Table With LIKE query and gets The Number Present at
	 * the last of each recipe Name (RC-CSTM-31 will Give 31) and with this List of
	 * Number it finds The Max number and returns .
	 *
	 * @param recipeNameList the recipe name list
	 * @param recipeId       the recipe id
	 * @return Max recipe Number
	 */
	public static Integer getMaximumNumberFromList(List<String> idNameList, String partialId) {
		if (idNameList != null && !idNameList.isEmpty()) {
			Optional<? extends Integer> value = idNameList.stream()
					.map((Function<? super String, ? extends Integer>) currentId -> {
						Integer num = NumberUtils.INTEGER_ZERO;
						try {
							String strNum = StringUtils.substringAfterLast(StringUtils.upperCase(currentId),
									StringUtils.upperCase(partialId) + Symbol.HYPHEN_STRING);
							if (StringUtils.isNumeric(strNum)) {
								num = Integer.parseInt(strNum);
							}
						} catch (NumberFormatException e) {
							num = NumberUtils.INTEGER_ZERO;
						}
						return num;
					}).max(Integer::compare);
			if (value.isPresent()) {
				return value.get();
			}
		}
		return NumberUtils.INTEGER_ZERO;
	}

	/**
	 * Checks if Given Recipe is valis or not by verifying recipe, recipeId,
	 * recipeName and scriptJson contains values or not.
	 *
	 * @param recipe : Object of Recipe
	 * @return true if Recipe is valid, false otherwise
	 */
	public static boolean isValidRecipe(Recipe recipe) {
		boolean isValidRecipe = false;
		if (Utils.isNotNull(recipe) &&  StringUtils.isNotBlank(recipe.getRecipeId())
				&& StringUtils.isNotBlank(recipe.getName()) &&  StringUtils.isNotBlank(recipe.getScriptJson())) {
			isValidRecipe = true;
		}
		return isValidRecipe;
	}

	/**
	 * Creating BPMN Task Meta Data Map contains Task Status.
	 *
	 * @return Meta Data Map
	 */
	public static Map<String, Object> getTaskMetaMap() {
		Map<String, Object> map = new HashMap<>();
		map.put(STATUS, CLOSE);
		return map;
	}

	/**
	 * Creating Map For BPMN Workorder Meta Data contains Assigned User Name.
	 *
	 * @param userName : Name of the User, the Workorder is assigned to
	 * @return Meta Data Map
	 */
	public static Map<String, Object> getWorkorderMeta(String userName) {
		Map<String, Object> workorderMeta = new HashMap<>();
		workorderMeta.put(ASSIGNED_TO, userName);
		return workorderMeta;
	}

	/**
	 * Send http get request.
	 *
	 * @param url               the url
	 * @param isToEnableTimeOut the is to enable time out
	 * @param duration          the duration
	 * @return HttpGetRequest
	 */

	public static HttpGetRequest sendHttpGetRequest(String url, boolean isToEnableTimeOut, Duration duration) {
		HttpGetRequest httpGetRequest = new HttpGetRequest(url);
		if (duration != null) {
			httpGetRequest.setConnectionTimeout(duration);
			httpGetRequest.setEnableTimeout(isToEnableTimeOut);
		}
		return httpGetRequest;
	}

	/**
	 * Send http post request.
	 *
	 * @param url               the url
	 * @param httpEntity        the http entity
	 * @param isToEnableTimeOut the is to enable time out
	 * @param duration          the duration
	 * @return the http post request
	 */
	public static HttpPostRequest sendHttpPostRequest(String url, StringEntity httpEntity, boolean isToEnableTimeOut,
			Duration duration) {
		HttpPostRequest httpPostRequest = new HttpPostRequest(url, httpEntity);
		if (duration != null) {
			httpPostRequest.setConnectionTimeout(duration);
			httpPostRequest.setEnableTimeout(isToEnableTimeOut);
		}
		return httpPostRequest;
	}

	/**
	 * Gets the dropwizard url.
	 * 
	 * @param request the request
	 * @return the dropwizard url
	 */
	public static String getCustomGeographyDropwizardUrl(HttpServletRequest request, String apiCall) {
		String url = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL, Boolean.TRUE) + apiCall;
		String queryString = request.getQueryString();
		if (StringUtils.isNotEmpty(queryString)) {
			url += Symbol.QUESTION_MARK_STRING + queryString;
		}
		return url;
	}

	public static String getUserName(User user) {
        String userName = null;
        if (user != null) {
            if (user.getFirstName() != null) {
                userName = user.getFirstName();
                if (user.getLastName() != null)
                    userName = userName + Symbol.SPACE + user.getLastName();
            }
            return userName;
        }

        return Symbol.HASH_STRING;
	}

	/**
	 * Getting Map with Workorder Name as key and BpmnTask Id to respective
	 * Workorder.
	 * 
	 * @param list : List of BpmnTask
	 * @return BpmnTask Id Map
	 */
	public static Map<String, Integer> getBpmnTaskIdMap(List<BpmnTask> list,Integer taskId) {
		if(taskId!=null) {
			return list.stream()
					.filter(bpmnTask -> (bpmnTask != null && bpmnTask.getBpmnWorkorder().getWorkorderNo() != null
							&& bpmnTask.getId() != null&&taskId.equals(bpmnTask.getId())))
					.collect(Collectors.toMap(bpmnTask -> bpmnTask.getBpmnWorkorder().getWorkorderNo(), BpmnTask::getId));
		}else {
			Map<String, Integer>taskIdMap=new HashMap<>();
			list.forEach(bpmnTask->{
				if(bpmnTask != null && bpmnTask.getBpmnWorkorder().getWorkorderNo() != null
						&& bpmnTask.getId() != null) {
					taskIdMap.put( bpmnTask.getBpmnWorkorder().getWorkorderNo(), bpmnTask.getId());
				}
			});
			return taskIdMap;
		}
	}

	/**
	 * Getting Map with Workorder Name as key and BpmnTask Id to respective
	 * Workorder.
	 * 
	 * @param bpmnTaskList : List of BpmnTask
	 * @return BpmnTask Id Map
	 */
	public static Map<String, BpmnTaskCandidate> getBpmnTaskCandidateMap(List<BpmnTaskCandidate> list) {
		Map<String, BpmnTaskCandidate> mapToReturn = new HashMap<>();
//		Map<String, List<BpmnTaskCandidate>> map = list.stream().collect(Collectors.groupingBy(b->b.getBpmnTask().getBpmnWorkorder().getWorkorderNo()));
//		map.forEach((keyWorokderno,valList)->{
//			if (Utils.isValidList(valList)) {
//				if (valList.size() > ForesightConstants.INDEX_ONE) {
//					logger.info("size list greater than one {}: ", valList.size());
//					valList.sort(Comparator.comparing(b -> b.getBpmnTask()
//															.getModifiedTime()));
//					mapToReturn.put(keyWorokderno, valList.get(valList.size() - ForesightConstants.INDEX_ONE));
//				} else {
//					mapToReturn.put(keyWorokderno, valList.get(ForesightConstants.INDEX_ZERO));
//				}
//			}else {
//				logger.error("valList is null for workordernumber {}",keyWorokderno);
//			}
//		});
		for (BpmnTaskCandidate candidate : list) {
			mapToReturn.put(candidate.getBpmnTask().getBpmnWorkorder().getWorkorderNo(), candidate);
		}
		return mapToReturn;
	}

	/**
	 * It creates a list of WorkorderNames from list of BPMNWorkorder objects.
	 * 
	 * @param bpmnTaskList the bpmn task list
	 * @return List<String> containing all workOrder Names from List
	 */
	public static List<String> getWorkorderIdList(List<BpmnTask> bpmnTaskList) {
		return bpmnTaskList.stream().map(bpmnTask -> bpmnTask.getBpmnWorkorder().getWorkorderNo())
				.collect(Collectors.toList());
	}

	/**
	 * It creates a list of WorkorderNames from list of GenericWorkorder objects.
	 * 
	 * @param workorderList : List of GenericWorkorder
	 * @return List<String> containing all workOrder Names from List
	 */
	public static List<String> getWorkorderIdListFromGWO(List<GenericWorkorder> workorderList) {
		return workorderList.stream().map(GenericWorkorder::getWorkorderId).collect(Collectors.toList());
	}

	public static void setRecipeMeta(TemplateType templateType, Map<String, String> metaMap, List<RecipeWrapper> list) {
		Gson gson = new Gson();
		if (templateType.equals(TemplateType.NV_SSVT)|| templateType.equals(TemplateType.NV_INBUILDING)) {
			Map<Integer, Integer> recipePCIMap = getIntegerMetaMap(metaMap, RECIPE_PCI_MAP);
			Map<Integer, Integer> recipeRSRPMap = getIntegerMetaMap(metaMap, RECIPE_RSRP_MAP);
			Map<Integer, Integer> recipeSinrMap = getIntegerMetaMap(metaMap, RECIPE_SINR_MAP);
			Map<Integer, Double> recipeDLMap = getDoubleMetaMap(metaMap, RECIPE_DL_MAP);
			Map<Integer, Double> recipeULMap = getDoubleMetaMap(metaMap, RECIPE_UL_MAP);
			Map<Integer, Double> recipeJitterMap = getDoubleMetaMap(metaMap, RECIPE_JITTER_MAP);
			Map<Integer, Double> recipeLatencyMap = getDoubleMetaMap(metaMap, RECIPE_LATENCY_MAP);
			Map<Integer, Double> recipeBrowseMap = getDoubleMetaMap(metaMap, RECIPE_BROWSE_MAP);
			Map<Integer, Double> recipeDLCriteriaMap = getDoubleMetaMap(metaMap, RECIPE_DL_CRITERIA_MAP);
			Map<Integer, Double> recipeULCriteriaMap = getDoubleMetaMap(metaMap, RECIPE_UL_CRITERIA_MAP);
			
			for (RecipeWrapper recipeWrapper : list) {
				recipePCIMap.put(recipeWrapper.getWoRecipeMappingId(), recipeWrapper.getPci());
				recipeRSRPMap.put(recipeWrapper.getWoRecipeMappingId(), recipeWrapper.getRsrpThreshold());
				recipeSinrMap.put(recipeWrapper.getWoRecipeMappingId(), recipeWrapper.getSinrThreshold());
				recipeDLMap.put(recipeWrapper.getWoRecipeMappingId(), recipeWrapper.getDlThreshold());
				recipeULMap.put(recipeWrapper.getWoRecipeMappingId(), recipeWrapper.getUlThreshold());
				recipeJitterMap.put(recipeWrapper.getWoRecipeMappingId(), recipeWrapper.getJitterThreshold());
				recipeLatencyMap.put(recipeWrapper.getWoRecipeMappingId(), recipeWrapper.getLatencyThreshold());
				recipeBrowseMap.put(recipeWrapper.getWoRecipeMappingId(), recipeWrapper.getBrowseThreshold());
				recipeDLCriteriaMap.put(recipeWrapper.getWoRecipeMappingId(), recipeWrapper.getDlThresholdCriteria());
				recipeULCriteriaMap.put(recipeWrapper.getWoRecipeMappingId(), recipeWrapper.getUlThresholdCriteria());
			}
			setThresholdValues(metaMap, gson, recipePCIMap, recipeRSRPMap, recipeSinrMap, recipeDLMap, recipeULMap,
					recipeJitterMap, recipeLatencyMap);
			if (!recipeBrowseMap.isEmpty()) {
				metaMap.put(RECIPE_BROWSE_MAP, gson.toJson(recipeBrowseMap));
			}
			if (!recipeDLCriteriaMap.isEmpty()) {
				metaMap.put(RECIPE_DL_CRITERIA_MAP, gson.toJson(recipeDLCriteriaMap));
			}
			if (!recipeULCriteriaMap.isEmpty()) {
				metaMap.put(RECIPE_UL_CRITERIA_MAP, gson.toJson(recipeULCriteriaMap));
			}
			
			
		} 
		if (templateType.equals(TemplateType.NV_INBUILDING) || templateType.equals(TemplateType.NV_IB_BENCHMARK)
				|| templateType.equals(TemplateType.NV_ADHOC_IB)) {
			Map<Integer, Integer> recipeUnitIdMap = getIntegerMetaMap(metaMap, RECIPE_UNIT_ID_MAP);
			Map<Integer, Boolean> recipeFloorplanMap = getBooleanMetaMap(metaMap, RECIPE_FLOORPLAN_MAP);
			Map<Integer, String> recipeSmallCellMap = getStringMetaMap(metaMap, RECIPE_CELL_NAME_MAP);
			for (RecipeWrapper recipeWrapper : list) {
				if (recipeWrapper.getUnitId() != null) {
					recipeUnitIdMap.put(recipeWrapper.getWoRecipeMappingId(), recipeWrapper.getUnitId());
				}
				if (recipeWrapper.getToUseFloorplan() != null) {
					recipeFloorplanMap.put(recipeWrapper.getWoRecipeMappingId(), recipeWrapper.getToUseFloorplan());
				}
				if(recipeWrapper.getCellName()!=null) {
						recipeSmallCellMap.put(recipeWrapper.getWoRecipeMappingId(),recipeWrapper.getCellName());
				}
			}	
			
			if(!recipeSmallCellMap.isEmpty()) {
				metaMap.put(RECIPE_CELL_NAME_MAP, gson.toJson(recipeSmallCellMap));
			}
			if (!recipeUnitIdMap.isEmpty()) {
				metaMap.put(RECIPE_UNIT_ID_MAP, gson.toJson(recipeUnitIdMap));
			}
			if (!recipeFloorplanMap.isEmpty()) {
				metaMap.put(RECIPE_FLOORPLAN_MAP, gson.toJson(recipeFloorplanMap));
			}
			
		}else if(templateType.equals(TemplateType.NV_OPENDRIVE)){
			Map<Integer, Integer> recipeSinrMap = getIntegerMetaMap(metaMap, RECIPE_SINR_MAP);
			Map<Integer, Integer> recipeMaxSinrMap = getIntegerMetaMap(metaMap, RECIPE_MAX_SINR_MAP);
			Map<Integer, String> recipeRemarkMap = getStringMetaMap(metaMap, RECIPE_REMARK_MAP);
			for (RecipeWrapper recipeWrapper : list) {
				recipeSinrMap.put(recipeWrapper.getWoRecipeMappingId(), recipeWrapper.getSinrThreshold());
				recipeMaxSinrMap.put(recipeWrapper.getWoRecipeMappingId(), recipeWrapper.getMaxSINRThreshold());
				recipeRemarkMap.put(recipeWrapper.getWoRecipeMappingId(), recipeWrapper.getRemark());
			}
			if (!recipeSinrMap.isEmpty()) {
				metaMap.put(RECIPE_SINR_MAP, gson.toJson(recipeSinrMap));
			}
			if (!recipeMaxSinrMap.isEmpty()) {
				metaMap.put(RECIPE_MAX_SINR_MAP, gson.toJson(recipeMaxSinrMap));
			}
			if (!recipeRemarkMap.isEmpty()) {
				metaMap.put(RECIPE_REMARK_MAP, gson.toJson(recipeRemarkMap));
			}
		}
		else if(templateType.equals(TemplateType.SCANNING_RECEIVER)) {
			Map<Integer, String> scannerReqMap = getStringMetaMap(metaMap, SCANNER_REQUEST);
			Map<Integer, String> channelListMap = getStringMetaMap(metaMap, CHANNEL_LIST);
			for (RecipeWrapper recipeWrapper : list) {
				scannerReqMap.put(recipeWrapper.getWoRecipeMappingId(), recipeWrapper.getScannerRequest());
				channelListMap.put(recipeWrapper.getWoRecipeMappingId(), recipeWrapper.getChannelList());
			}
			if (!scannerReqMap.isEmpty()) {
				metaMap.put(SCANNER_REQUEST, gson.toJson(scannerReqMap));
			}
			if (!channelListMap.isEmpty()) {
				metaMap.put(CHANNEL_LIST, gson.toJson(channelListMap));
			}
			
		}
		
		
		if (templateType.equals(TemplateType.NV_BENCHMARK) || templateType.equals(TemplateType.NV_IB_BENCHMARK)
				|| templateType.equals(TemplateType.NV_INBUILDING)|| templateType.equals(TemplateType.NV_ADHOC_IB)) {
			Map<Integer, String> recipeOperatorMap = getStringMetaMap(metaMap, RECIPE_OPERATOR_MAP);
			for (RecipeWrapper recipeWrapper : list) {
				if (recipeWrapper.getOperator() != null) {
					recipeOperatorMap.put(recipeWrapper.getWoRecipeMappingId(), recipeWrapper.getOperator());
				}
			}
			if (!recipeOperatorMap.isEmpty()) {
				metaMap.put(RECIPE_OPERATOR_MAP, gson.toJson(recipeOperatorMap));
			}
		}
	}

	private static void setThresholdValues(Map<String, String> metaMap, Gson gson, Map<Integer, Integer> recipePCIMap,
			Map<Integer, Integer> recipeRSRPMap, Map<Integer, Integer> recipeSinrMap, Map<Integer, Double> recipeDLMap,
			Map<Integer, Double> recipeULMap, Map<Integer, Double> recipeJitterMap,
			Map<Integer, Double> recipeLatencyMap) {
		if (!recipePCIMap.isEmpty()) {
			metaMap.put(RECIPE_PCI_MAP, gson.toJson(recipePCIMap));
		}
		if (!recipeRSRPMap.isEmpty()) {
			metaMap.put(RECIPE_RSRP_MAP, gson.toJson(recipeRSRPMap));
		}
		
		if (!recipeSinrMap.isEmpty()) {
			metaMap.put(RECIPE_SINR_MAP, gson.toJson(recipeSinrMap));
		}
		if (!recipeDLMap.isEmpty()) {
			metaMap.put(RECIPE_DL_MAP, gson.toJson(recipeDLMap));
		}
		
		if (!recipeULMap.isEmpty()) {
			metaMap.put(RECIPE_UL_MAP, gson.toJson(recipeULMap));
		}
		
		if (!recipeJitterMap.isEmpty()) {
			metaMap.put(RECIPE_JITTER_MAP, gson.toJson(recipeJitterMap));
		}
		
		if (!recipeLatencyMap.isEmpty()) {
			metaMap.put(RECIPE_LATENCY_MAP, gson.toJson(recipeLatencyMap));
		}
	}

	private static Map<Integer, Integer> getIntegerMetaMap(Map<String, String> metaMap, String key) {
		if (Utils.hasValidValue(metaMap.get(key))) {
			return new Gson().fromJson(metaMap.get(key), new TypeToken<HashMap<Integer, Integer>>() {
			}.getType());
		}
		return new HashMap<>();
	}
	
	private static Map<Integer, Double> getDoubleMetaMap(Map<String, String> metaMap, String key) {
		if (Utils.hasValidValue(metaMap.get(key))) {
			return new Gson().fromJson(metaMap.get(key), new TypeToken<HashMap<Integer, Double>>() {
			}.getType());
		}
		return new HashMap<>();
	}

	private static Map<Integer, Boolean> getBooleanMetaMap(Map<String, String> metaMap, String key) {
		if (Utils.hasValidValue(metaMap.get(key))) {
			return new Gson().fromJson(metaMap.get(key), new TypeToken<HashMap<Integer, Boolean>>() {
			}.getType());
		}
		return new HashMap<>();
	}

	private static Map<Integer, String> getStringMetaMap(Map<String, String> metaMap, String key) {
		try {
			if (Utils.hasValidValue(metaMap.get(key))) {
				return new Gson().fromJson(metaMap.get(key), new TypeToken<HashMap<Integer, String>>() {
				}.getType());
			}
			
		} catch (JsonSyntaxException e) {
		logger.error("Exception while parse meta into map for string {} and Exception",Utils.hasValidValue(metaMap.get(key)),ExceptionUtils.getStackTrace(e));
		}
		return new HashMap<>();
	}

	public static void setCustomGeographyMeta(Map<String, String> metaMap, List<RecipeWrapper> list) {
		Gson gson = new Gson();
		Map<Integer, Integer> recipeCustomGeographyMap = getIntegerMetaMap(metaMap, RECIPE_CUSTOM_GEOGRAPHY_MAP);
		for (RecipeWrapper recipeWrapper : list) {
			if (recipeWrapper.getCustomGeographyId() != null) {
				recipeCustomGeographyMap.put(recipeWrapper.getWoRecipeMappingId(),
						recipeWrapper.getCustomGeographyId());
			}
		}
		if (!recipeCustomGeographyMap.isEmpty()) {
			metaMap.put(RECIPE_CUSTOM_GEOGRAPHY_MAP, gson.toJson(recipeCustomGeographyMap));
		}
	}

	public static void getRecipeMeta(RecipeWrapper recipeWrapper, Map<String, String> metaMap,
			TemplateType templateType) {
		Gson gson = new Gson();
		if (Utils.hasValidValue(metaMap.get(RECIPE_CUSTOM_GEOGRAPHY_MAP))) {
			Map<Integer, Integer> recipeCustomGeographyMap = gson.fromJson(metaMap.get(RECIPE_CUSTOM_GEOGRAPHY_MAP),
					new TypeToken<HashMap<Integer, Integer>>() {
					}.getType());
			recipeWrapper.setCustomGeographyId(recipeCustomGeographyMap.get(recipeWrapper.getWoRecipeMappingId()));
		}
		if (templateType.equals(TemplateType.NV_SSVT)|| templateType.equals(TemplateType.NV_INBUILDING)) {
			if (Utils.hasValidValue(metaMap.get(RECIPE_PCI_MAP))) {
				Map<Integer, Integer> recipePCIMap = getIntegerMetaMap(metaMap, RECIPE_PCI_MAP);
				recipeWrapper.setPci(recipePCIMap.get(recipeWrapper.getWoRecipeMappingId()));
			}
			if (Utils.hasValidValue(metaMap.get(RECIPE_RSRP_MAP))) {
				Map<Integer, Integer> recipeRSRPMap = getIntegerMetaMap(metaMap, RECIPE_RSRP_MAP);
				recipeWrapper.setRsrpThreshold(recipeRSRPMap.get(recipeWrapper.getWoRecipeMappingId()));
			}
			if (Utils.hasValidValue(metaMap.get(RECIPE_SINR_MAP))) {
				Map<Integer, Integer> recipeSINRMap = getIntegerMetaMap(metaMap, RECIPE_SINR_MAP);
				recipeWrapper.setSinrThreshold(recipeSINRMap.get(recipeWrapper.getWoRecipeMappingId()));
			}
			
			if (Utils.hasValidValue(metaMap.get(RECIPE_DL_MAP))) {
				Map<Integer, Double> recipeDlMap = getDoubleMetaMap(metaMap, RECIPE_DL_MAP);
				recipeWrapper.setDlThreshold(recipeDlMap.get(recipeWrapper.getWoRecipeMappingId()));
			}
			if (Utils.hasValidValue(metaMap.get(RECIPE_UL_MAP))) {
				Map<Integer, Double> recipeULMap = getDoubleMetaMap(metaMap, RECIPE_UL_MAP);
				recipeWrapper.setUlThreshold(recipeULMap.get(recipeWrapper.getWoRecipeMappingId()));
			}
			
			if (Utils.hasValidValue(metaMap.get(RECIPE_LATENCY_MAP))) {
				Map<Integer, Double> recipeLatencyMap = getDoubleMetaMap(metaMap, RECIPE_LATENCY_MAP);
				recipeWrapper.setLatencyThreshold(recipeLatencyMap.get(recipeWrapper.getWoRecipeMappingId()));
			}
			if (Utils.hasValidValue(metaMap.get(RECIPE_JITTER_MAP))) {
				Map<Integer, Double> recipeJitterMap = getDoubleMetaMap(metaMap, RECIPE_JITTER_MAP);
				recipeWrapper.setJitterThreshold(recipeJitterMap.get(recipeWrapper.getWoRecipeMappingId()));
			}
			if (Utils.hasValidValue(metaMap.get(RECIPE_BROWSE_MAP))) {
				Map<Integer, Double> recipeBrowseMap = getDoubleMetaMap(metaMap, RECIPE_BROWSE_MAP);
				recipeWrapper.setBrowseThreshold(recipeBrowseMap.get(recipeWrapper.getWoRecipeMappingId()));
			}
			if (Utils.hasValidValue(metaMap.get(RECIPE_DL_CRITERIA_MAP))) {
				Map<Integer, Double> recipeDLCriteriaMap = getDoubleMetaMap(metaMap, RECIPE_DL_CRITERIA_MAP);
				recipeWrapper.setDlThresholdCriteria(recipeDLCriteriaMap.get(recipeWrapper.getWoRecipeMappingId()));
			}
			if (Utils.hasValidValue(metaMap.get(RECIPE_UL_CRITERIA_MAP))) {
				Map<Integer, Double> recipeULCriteriaMap = getDoubleMetaMap(metaMap, RECIPE_UL_CRITERIA_MAP);
				recipeWrapper.setUlThresholdCriteria(recipeULCriteriaMap.get(recipeWrapper.getWoRecipeMappingId()));
			}
			
			
			
		} 
		if (templateType.equals(TemplateType.NV_INBUILDING) || templateType.equals(TemplateType.NV_ADHOC_IB)
				|| templateType.equals(TemplateType.NV_IB_BENCHMARK)||templateType.equals(TemplateType.INBUILDING_WORKFLOW)||
				templateType.equals(TemplateType.NV_SSVT_IBC_QUICK)||templateType.equals(TemplateType.NV_SSVT_IBC_FULL)) {
			if (Utils.hasValidValue(metaMap.get(RECIPE_UNIT_ID_MAP))) {
				Map<Integer, Integer> recipeUnitIdMap = getIntegerMetaMap(metaMap, RECIPE_UNIT_ID_MAP);
				recipeWrapper.setUnitId(recipeUnitIdMap.get(recipeWrapper.getWoRecipeMappingId()));
				if( templateType.equals(TemplateType.NV_SSVT_IBC_FULL) && metaMap.get( NVWorkorderConstant.TO_USE_FLOORPLAN) != null) {
					recipeWrapper.setToUseFloorplan(metaMap.get(NVWorkorderConstant.TO_USE_FLOORPLAN).equalsIgnoreCase("true") ? true : false);
				}
			}
			if (Utils.hasValidValue(metaMap.get(RECIPE_FLOORPLAN_MAP))) {
				Map<Integer, Boolean> recipeFloorplanMap = getBooleanMetaMap(metaMap, RECIPE_FLOORPLAN_MAP);
				recipeWrapper.setToUseFloorplan(recipeFloorplanMap.get(recipeWrapper.getWoRecipeMappingId()));
			}
			if (Utils.hasValidValue(metaMap.get(RECIPE_PCI_MAP))) {
				Map<Integer, Integer> recipePCIMap = getIntegerMetaMap(metaMap, RECIPE_PCI_MAP);
				recipeWrapper.setPci(recipePCIMap.get(recipeWrapper.getWoRecipeMappingId()));
			}
			
			if (Utils.hasValidValue(metaMap.get(RECIPE_CELL_NAME_MAP))) {
				Map<Integer, String> recipeCellNameMap = getStringMetaMap(metaMap, RECIPE_CELL_NAME_MAP);
				recipeWrapper.setCellName(recipeCellNameMap .get(recipeWrapper.getWoRecipeMappingId()));
			}
			
			
		} else if(templateType.equals(TemplateType.NV_OPENDRIVE)){
			if (Utils.hasValidValue(metaMap.get(RECIPE_SINR_MAP))) {
				Map<Integer, Integer> recipeSINRMap = getIntegerMetaMap(metaMap, RECIPE_SINR_MAP);
				recipeWrapper.setSinrThreshold(recipeSINRMap.get(recipeWrapper.getWoRecipeMappingId()));
			}
			if (Utils.hasValidValue(metaMap.get(RECIPE_MAX_SINR_MAP))) {
				Map<Integer, Integer> recipeMaxSINRMap = getIntegerMetaMap(metaMap, RECIPE_MAX_SINR_MAP);
				recipeWrapper.setMaxSINRThreshold(recipeMaxSINRMap.get(recipeWrapper.getWoRecipeMappingId()));
			}
			if (Utils.hasValidValue(metaMap.get(RECIPE_REMARK_MAP))) {
				Map<Integer, String> recipeRemarkMap = getStringMetaMap(metaMap, RECIPE_REMARK_MAP);
				recipeWrapper.setRemark(recipeRemarkMap.get(recipeWrapper.getWoRecipeMappingId()));
			}
		}
		
		else if (templateType.equals(TemplateType.SCANNING_RECEIVER)) {
			if (Utils.hasValidValue(metaMap.get(SCANNER_REQUEST))) {
				Map<Integer, String> scannerReqMap = getStringMetaMap(metaMap, SCANNER_REQUEST);
				recipeWrapper.setScannerRequest(scannerReqMap.get(recipeWrapper.getWoRecipeMappingId()));
			}
			if (Utils.hasValidValue(metaMap.get(CHANNEL_LIST))) {
				Map<Integer, String> channelListMap = getStringMetaMap(metaMap, CHANNEL_LIST);
				recipeWrapper.setChannelList(channelListMap.get(recipeWrapper.getWoRecipeMappingId()));
			}
		} 
		
		if (templateType.equals(TemplateType.NV_BENCHMARK) || templateType.equals(TemplateType.NV_IB_BENCHMARK)
				|| templateType.equals(TemplateType.NV_INBUILDING)||templateType.equals(TemplateType.INBUILDING_WORKFLOW)
				|| templateType.equals(TemplateType.NV_ADHOC_IB) && Utils.hasValidValue(metaMap.get(RECIPE_OPERATOR_MAP))) {

			Map<Integer, String> recipeUnitIdMap = getStringMetaMap(metaMap, RECIPE_OPERATOR_MAP);
			recipeWrapper.setOperator(recipeUnitIdMap.get(recipeWrapper.getWoRecipeMappingId()));

		}
	}

	public static List<Status> getStatusList(List<String> list) {
		List<Status> statusList = new ArrayList<>();
		for (String obj : list) {
			statusList.add(Status.valueOf(obj));
		}
		return statusList;
	}

	public static List<TemplateType> getTemplateList(List<String> list) {
		List<TemplateType> statusList = new ArrayList<>();
		for (String obj : list) {
			statusList.add(TemplateType.valueOf(obj));
		}
		return statusList;
	}

	/**
	 * Returns the Module Name from of Particular TemplateType For Notification .
	 * 
	 * @param templateType
	 * @return String moduleName
	 */
	public static String getModuleName(TemplateType templateType) {
		String moduleName = null;
		if (templateType.equals(TemplateType.NV_CLOT)) {
			moduleName = NVWorkorderConstant.CLOT;
		} else if (templateType.equals(TemplateType.NV_SSVT)||templateType.equals(TemplateType.NV_SSVT_FULL)||templateType.equals(TemplateType.NV_SSVT_QUICK)) {
			moduleName = NVWorkorderConstant.SHAKEDOWN;
		} else if (templateType.equals(TemplateType.NV_LIVE_DRIVE)) {
			moduleName = NVWorkorderConstant.LIVE_DRIVE;
		} else if (templateType.equals(TemplateType.NV_OPENDRIVE)) {
			moduleName = NVWorkorderConstant.OPEN_DRIVE;
		} else if (templateType.equals(TemplateType.NV_INBUILDING)) {
			moduleName = NVWorkorderConstant.IN_BUILDING;
		} else if (templateType.equals(TemplateType.NV_STATIONARY)) {
			moduleName = NVWorkorderConstant.STATIONARY;
		}else if(templateType.equals(TemplateType.INBUILDING_WORKFLOW)) {
			moduleName = NVWorkorderConstant.IN_BUILDING_WORKFLOW;
		}else if(templateType.equals(TemplateType.SCANNING_RECEIVER)) {
			moduleName = NVWorkorderConstant.SCANNING_REQUEST_MESSAGE;
		} else if (templateType.equals(TemplateType.NV_SSVT_IBC_QUICK)) {
			moduleName = NVWorkorderConstant.NV_SSVT_IBC_QUICK_PREFIX;
		} else if (templateType.equals(TemplateType.NV_SSVT_IBC_FULL)) {
			moduleName = NVWorkorderConstant.NV_SSVT_IBC_FULL_PREFIX;
		}
		return moduleName;
	}

	/**
	 * Returns A new recipe Object after setting RecipeId from Specified
	 * RecipeWrapper.
	 * 
	 * @param recipeWrapper the recipe wrapper
	 * @return Recipe object
	 */
	public static Recipe getRecipeFromWrapper(RecipeWrapper recipeWrapper) {
		Recipe recipe = new Recipe();
		recipe.setId(recipeWrapper.getId());
		return recipe;
	}

	public static Map<String, String> castMapFromEncryptedString(String mapStr) throws Exception {
		return new Gson().fromJson(AuthenticationCommonUtil.checkForValueDecryption(mapStr), new TypeToken<HashMap<String, String>>() {
		}.getType());
	}
	
	

	public static Map<String, Object> getEncryptedString(String mapStr) throws Exception {
		return new Gson().fromJson(AuthenticationCommonUtil.checkForValueDecryption(mapStr), new TypeToken<HashMap<String, Object>>() {
		}.getType());
	}

	public static Integer parseInt(String str) {
		if (Utils.hasValidValue(str)) {
			return Integer.parseInt(str);
		}
		return null;
	}

	public static Long parseLong(String str) {
		if (Utils.hasValidValue(str)) {
			return Long.parseLong(str);
		}
		return null;
	}

	public static Double parseDouble(String str) {
		if (Utils.hasValidValue(str)) {
			return Double.parseDouble(str);
		}
		return null;
	}

	public static void setWorkorderMeta(NVWorkorderWrapper wrapper, Integer customGeographyId) {
		Map<String, String> metaMap = wrapper.getGeoWoMetaMap();
		if (wrapper.getTemplateType().equals(TemplateType.NV_STATIONARY)) {
			metaMap.put(CUSTOM_GEOGRAPHY_ID, customGeographyId.toString());
		}
		wrapper.setGeoWoMetaMap(metaMap);
	}

	public static List<NVWOUserWrapper> getCurrentUserList() {
		List<NVWOUserWrapper> userList = new ArrayList<>();
		NVWOUserWrapper userWrapper = new NVWOUserWrapper();
		userWrapper.setUserName(UserContextServiceImpl.getUserInContext().getUserName());
		userList.add(userWrapper);
		return userList;
	}

	public static Map<String, String> getGegraphyTaggedMap(String geographyLevel, String geographyId,
			String geographyName) {
		Map<String, String> geographyMap = null;
		geographyMap = new HashMap<>();
		geographyMap.put(NVWorkorderConstant.GEOGRAPHY_TYPE, geographyLevel);
		geographyMap.put(NVWorkorderConstant.GEOGRAPHY_ID, geographyId);
		geographyMap.put(NVWorkorderConstant.GEOGRAPHY_NAME, geographyName);
		return geographyMap;
	}

	public static String getWorkorderId(TemplateType templateType, String geographyName) {
		StringBuilder workorderId = new StringBuilder(WORKORDER_ID_PREFIX).append(Symbol.HYPHEN);
		workorderId.append(getTemplateTypePrefix(templateType)).append(Symbol.HYPHEN);
		workorderId.append(getDatePrefix()).append(Symbol.HYPHEN);
		workorderId.append(geographyName);
		return workorderId.toString();
	}

	private static String getDatePrefix() {
		Date date = new Date();
		return DateUtils.getWeekNumber(date) + com.inn.core.generic.utils.Utils.getStringDateByFormat(date, "YYYY");
	}

	private static String getTemplateTypePrefix(TemplateType templateType) {
		if (templateType.equals(TemplateType.NV_SSVT)) {
			return SSVT_PREFIX;
		} else if (templateType.equals(TemplateType.NV_CLOT)) {
			return CLOT_PREFIX;
		} else if (templateType.equals(TemplateType.NV_LIVE_DRIVE)) {
			return LIVE_DRIVE_PREFIX;
		} else if (templateType.equals(TemplateType.NV_STATIONARY)) {
			return STATIONARY_PREFIX;
		} else if (templateType.equals(TemplateType.NV_OPENDRIVE)) {
			return OPEN_DRIVE_PREFIX;
		} else if (templateType.equals(TemplateType.NV_ADHOC_OD)) {
			return OPEN_DRIVE_PREFIX;
		} else if (templateType.equals(TemplateType.NV_BRTI)) {
			return BRTI_PREFIX;
		} else if (templateType.equals(TemplateType.NV_ADHOC_BRTI_DRIVE)) {
			return BRTI_PREFIX;
		} else if (templateType.equals(TemplateType.NV_ADHOC_BRTI_ST)) {
			return BRTI_PREFIX;
		} else if (templateType.equals(TemplateType.NV_INBUILDING)) {
			return IN_BUILDING_PREFIX;
		} else if (templateType.equals(TemplateType.NV_BENCHMARK)) {
			return BENCHMARK_PREFIX;
		} else if (templateType.equals(TemplateType.NV_ADHOC_IB)) {
			return IN_BUILDING_PREFIX;
		} else if (templateType.equals(TemplateType.NV_ADHOC_LD)) {
			return LIVE_DRIVE_PREFIX;
		} else if (templateType.equals(TemplateType.NV_IB_BENCHMARK)) {
			return IN_BUILDING_PREFIX;
		} else if (templateType.equals(TemplateType.NV_COMPLAINTS)) {
			return CUSTOMER_COMPLAINTS_PREFIX;
		} else if (templateType.equals(TemplateType.NV_SSVT_QUICK)) {
			return SF_SSVT_PREFIX;
		} else if (templateType.equals(TemplateType.NV_SSVT_FULL)) {
			return SF_SSVT_PREFIX_FULL;
		}else if (templateType.equals(TemplateType.NV_SSVT_IBC_QUICK)) {
			return NV_SSVT_IBC_QUICK_PREFIX;
		}else if (templateType.equals(TemplateType.NV_SSVT_IBC_FULL)) {
			return NV_SSVT_IBC_FULL_PREFIX;
		}
		return null;
	}

	public static void setStartAndEndDate(NVWorkorderWrapper wrapper) {
		Calendar cal = Calendar.getInstance();
		wrapper.setStartDate(cal.getTime().getTime());
		cal.add(Calendar.DAY_OF_MONTH, 1);
		wrapper.setEndDate(cal.getTime().getTime());
	}

	public static List<String> getBoundaryColumnList(String columnName) {
		ArrayList<String> list = new ArrayList<>();
		list.add(columnName);
		list.add(GenericMapUtils.GEO_COL_NAME);
		return list;
	}

	public static String getBoundaryColumnName(String zoom) {
		if (zoom == null || zoom.equals(ZOOM_18)) {
			return GenericMapUtils.GEO_COL_COORDINATES;
		}
		return GenericMapUtils.GEO_COL_COORDINATES + zoom;
	}

	public static boolean verifyWorkorder(NVWorkorderWrapper wrapper) {
		Map<String, String> metaMap = wrapper.getGeoWoMetaMap();
		if (Utils.hasValue(wrapper.getTemplateType()) && Utils.isValidList(wrapper.getUserList())
				&& Utils.hasValue(metaMap) && Utils.hasValidValue(metaMap.get(GEOGRAPHY_TYPE))
				&& isParsebleInteger(metaMap.get(GEOGRAPHY_ID)) && Utils.hasValue(wrapper.getStartDate())
				&& Utils.hasValue(wrapper.getEndDate()) && Utils.hasValidValue(wrapper.getWorkorderName())
				&& Utils.hasValidValue(wrapper.getWorkorderId())) {
			for (NVWOUserWrapper woUser : wrapper.getUserList()) {
				if (woUser != null) {
					if (!Utils.isValidList(woUser.getRecipeList())) {
						return false;
					}
				}
			}
			return isValidTemplateType(wrapper.getTemplateType(), metaMap);
		}
		return false;
	}

	private static boolean isValidTemplateType(TemplateType woTemplate, Map<String, String> metaMap) {
		switch (woTemplate) {
		case NV_SSVT:
			return (isParsebleInteger(metaMap.get(SITE_ID)) && metaMap.get(BAND) != null);
		case NV_INBUILDING:
			return (isParsebleInteger(metaMap.get(BUILDING_ID)));
		case NV_OPENDRIVE:
		case NV_STATIONARY:
		case NV_LIVE_DRIVE:
		case NV_BRTI:
		case NV_CLOT:
		case NV_COMPLAINTS:
		case NV_BENCHMARK:
		case SCANNING_RECEIVER:
		case NV_STATIC_PROBE:
			return true;
		default:
			break;
		}
		return false;
	}

	public static boolean isParsebleInteger(String value) {
		if (Utils.hasValidValue(value)) {
			try {
				Integer.parseInt(value);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return false;
	}

	public static String getWorkorderMessageForUser(TemplateType template, String user) {
		return String.format(NVWorkorderConstant.USER_WORKORDER_NOTIFICATION_MESSAGE, user, getModuleName(template));
	}

	public static List<RecipeWrapper> getRecipeWrapperList(NVWorkorderWrapper wrapper, List<Recipe> recipeList) {
		List<RecipeWrapper> recipeWrapperList = new ArrayList<>();
		if (wrapper.getOperatorList() != null && Utils.isValidList(wrapper.getOperatorList())) {
			for (String operator : wrapper.getOperatorList()) {
				createRecipeWrapper(wrapper, recipeList, recipeWrapperList, operator);
			}
		} else {
			createRecipeWrapper(wrapper, recipeList, recipeWrapperList, null);
		}
		return recipeWrapperList;
	}

	public static void createRecipeWrapper(NVWorkorderWrapper wrapper, List<Recipe> recipeList,
			List<RecipeWrapper> recipeWrapperList, String operator) {
		for (Recipe recipe : recipeList) {
			if (wrapper.getTechnology() != null && wrapper.getTechnology().equalsIgnoreCase(WIFI)) {
				if (recipe.getCategory().contains(WIFI)) {
					addRecipeIntoList(wrapper, recipeWrapperList, operator, recipe);
				}
			} else if (wrapper.getTechnology() != null && wrapper.getTechnology().equalsIgnoreCase(LTE)) {
				if (!recipe.getCategory().contains(WIFI)) {
					addRecipeIntoList(wrapper, recipeWrapperList, operator, recipe);
				}
			} else {
				addRecipeIntoList(wrapper, recipeWrapperList, operator, recipe);
			}
		}
	}

	private static void addRecipeIntoList(NVWorkorderWrapper wrapper, List<RecipeWrapper> recipeWrapperList,
			String operator, Recipe recipe) {
		RecipeWrapper recipeWrapper = new RecipeWrapper();
		recipeWrapper.setId(recipe.getId());
		recipeWrapper.setOperator(operator);
		recipeWrapper.setUnitId(wrapper.getUnitId());
		recipeWrapperList.add(recipeWrapper);
	}

	public static Map<String, Double> getNESWLatLongByCenterLatLong(Map<String, Double> latLongMap) {
		Map<String, Double> neswLatLongMap = new HashMap<>();
		if (latLongMap != null) {
			Double latitude = latLongMap.get(KEY_LATITUDE);
			Double longitude = latLongMap.get(KEY_LONGITUDE);
			Double distance = latLongMap.get(KEY_LAT_LONG_DISTANCE);
			double neLong = longitude
					- Math.toDegrees(distance / EARTH_RADIUS / Math.cos(180 - Math.toRadians(latitude)));
			double swLong = longitude
					+ Math.toDegrees(distance / EARTH_RADIUS / Math.cos(180 - Math.toRadians(latitude)));
			double neLat = latitude + Math.toDegrees(distance / EARTH_RADIUS);
			double swLat = latitude - Math.toDegrees(distance / EARTH_RADIUS);

			neswLatLongMap.put(InfraConstants.SW_LATITUDE, swLat);
			neswLatLongMap.put(InfraConstants.NW_LAT, neLat);
			neswLatLongMap.put(InfraConstants.SW_LONGITUDE, swLong);
			neswLatLongMap.put(InfraConstants.NW_LONG, neLong);
			
			logger.info("View Port Map {}", new Gson().toJson(neswLatLongMap));

		}

		return neswLatLongMap;
	}

	private static Map<String, Double> getMapforLatLong(Double latitude, Double longitude) {
		Map<String, Double> latLongMap = new HashMap<>();
		latLongMap.put(KEY_LATITUDE, latitude);
		latLongMap.put(KEY_LONGITUDE, longitude);
		return latLongMap;
	}

	/**
	 * Gets the device WO notification payload.
	 * 
	 * @param deviceId    the device id
	 * @param workOrderId the work order id
	 * @param module      the module
	 * @param message     the message
	 * @return the device WO notification payload
	 */
	public static Map<String, String> getDeviceWONotificationPayload(String deviceId, Integer workOrderId,
			String module, String message) {
		Map<String, String> payLoad = getWONotificationPayload(module, message);
		payLoad.put(NVWorkorderConstant.KEY_NOTIFICATION_DEVICEID, deviceId);
		payLoad.put(NVWorkorderConstant.KEY_NOTIFICATION_WORKORDERID, String.valueOf(workOrderId));
		return payLoad;
	}

	/**
	 * Gets the WO notification payload.
	 * 
	 * @param module  the module
	 * @param message the message
	 * @return the WO notification payload
	 */
	public static Map<String, String> getWONotificationPayload(String module, String message) {
		Map<String, String> payLoad = new HashMap<>();
		payLoad.put(KEY_MODULE_NAME, module);
		payLoad.put(KEY_NOTIFICATION_MESSAGE, message);
		return payLoad;
	}

	public static Map<String, String> getWOReassignedNotificationPayload(String module, String workorderId) {
		Map<String, String> payLoad = new HashMap<>();
		payLoad.put(KEY_MODULE_NAME, module);
		payLoad.put(KEY_NOTIFICATION_MESSAGE, String.format(WORKORDER_REASSIGN_NOTIFICATION_MESSAGE, workorderId));
		return payLoad;
	}

	public static Map<String, String> getWODeleteNotificationPayload(String module, String workorderId) {
		Map<String, String> payLoad = new HashMap<>();
		payLoad.put(KEY_MODULE_NAME, module);
		payLoad.put(KEY_NOTIFICATION_MESSAGE, String.format(WORKORDER_DELETE_NOTIFICATION_MESSAGE, workorderId));
		return payLoad;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map getWorkorderIdJSON(GenericWorkorder genericWorkorder, List<Boolean> fileProcessedStatusList) {
		Map map = new HashMap<>();
		map.put(VALUE, genericWorkorder.getId());
		map.put(NAME, genericWorkorder.getWorkorderId());
		map.put(WO_NAME, genericWorkorder.getWorkorderName());
		map.put(CREATION_TIME, genericWorkorder.getCreationTime());
		map.put(END_TIME, genericWorkorder.getCompletionTime());
		map.put(MODIFIED_TIME, genericWorkorder.getModificationTime());
		map.put(REMARK, genericWorkorder.getRemark());
		map.put(TYPE_DRIVE, genericWorkorder.getTemplateType().toString());
		if(fileProcessedStatusList.contains(false)) {
			map.put(WO_STATUS, false);	
		}
		else {
			map.put(WO_STATUS, true);
		}
		setGeographyDataInMap(map, genericWorkorder);
		return map;
	}

	private static void setGeographyDataInMap(Map<String, String> map, GenericWorkorder genericWorkorder) {

		if (genericWorkorder.getGeographyl4() != null) {
			map.put(GEOGRAPHY_NAME, genericWorkorder.getGeographyl4().getName());
			map.put(GEOGRAPHY_ID, genericWorkorder.getGeographyl4().getId().toString());
			map.put(GEOGRAPHY_TYPE, ReportConstants.GEOGRAPHYL4);
		} else if (genericWorkorder.getGeographyl3() != null) {
			map.put(GEOGRAPHY_NAME, genericWorkorder.getGeographyl3().getName());
			map.put(GEOGRAPHY_ID, genericWorkorder.getGeographyl3().getId().toString());
			map.put(GEOGRAPHY_TYPE, ReportConstants.GEOGRAPHYL3);
		} else if (genericWorkorder.getGeographyl2() != null) {
			map.put(GEOGRAPHY_NAME, genericWorkorder.getGeographyl2().getName());
			map.put(GEOGRAPHY_ID, genericWorkorder.getGeographyl2().getId().toString());
			map.put(GEOGRAPHY_TYPE, ReportConstants.GEOGRAPHYL2);
		} else if (genericWorkorder.getGeographyl1() != null) {
			map.put(GEOGRAPHY_NAME, genericWorkorder.getGeographyl1().getName());
			map.put(GEOGRAPHY_ID, genericWorkorder.getGeographyl1().getId().toString());
			map.put(GEOGRAPHY_TYPE, ReportConstants.GEOGRAPHYL1);
		}
	}

	public static boolean copyFileToLocalPath(String filePath, InputStream dataStream) throws IOException {
		File file = new File(filePath);
		try (FileOutputStream outputStream = new FileOutputStream(file)) {
			int bufferSize;
			byte[] buffer = new byte[FILE_BUFFER_SIZE];
			while ((bufferSize = dataStream.read(buffer)) > DATA_STREAM_END_VALUE) {
				outputStream.write(buffer, OUTPUT_STREAM_START_OFFSET, bufferSize);
			}
			return true;
		} catch (Exception e) {
			logger.error("Exception inside the method copyFileToLocalPath {}", Utils.getStackTrace(e));
		}
		return false;

	}

	public static List<Integer> getConvertedList(List<String> valueList) {
		List<Integer> list = null;
		if (Utils.isValidList(valueList)) {
			list = valueList.stream().filter(Objects::nonNull).map(Integer::parseInt)
					.collect(Collectors.toList());
		}
		return list;
	}

	public static Map<String, Long> getStartEndTimeFromPeriodInfo(List<Integer> quarterList, List<Integer> yearList) {
		Long startTime = null;
		Long endTime = null;
		Map<String, Long> timeStampMap = new HashMap<>();
		if (quarterList != null && (!quarterList.isEmpty())) {
			for (int index = 0; index < quarterList.size(); index++) {
				Quarter quarter = new Quarter(quarterList.get(index), yearList.get(0));
				if (startTime != null && endTime != null) {
					startTime = startTime > quarter.getFirstMillisecond() ? quarter.getFirstMillisecond() : startTime;
					endTime = endTime < quarter.getLastMillisecond() ? quarter.getLastMillisecond() : endTime;
				} else {
					startTime = quarter.getFirstMillisecond();
					endTime = quarter.getLastMillisecond();
				}
			}
			timeStampMap.put(NVWorkorderConstant.START_TIMESTAMP, startTime);
			timeStampMap.put(NVWorkorderConstant.END_TIMESTAMP, endTime);
			return timeStampMap;
		} else {
			for (int index = 0; index < yearList.size(); index++) {
				Year year = new Year(yearList.get(index));
				if (startTime != null && endTime != null) {
					startTime = startTime > year.getFirstMillisecond() ? year.getFirstMillisecond() : startTime;
					endTime = endTime < year.getLastMillisecond() ? year.getLastMillisecond() : endTime;
				} else {
					startTime = year.getFirstMillisecond();
					endTime = year.getLastMillisecond();
				}
			}
			timeStampMap.put(NVWorkorderConstant.START_TIMESTAMP, startTime);
			timeStampMap.put(NVWorkorderConstant.END_TIMESTAMP, endTime);
			return timeStampMap;
		}
	}

	public static List<Integer> getgeographyIdList(List<String> list) {
		List<Integer> statusList = new ArrayList<>();
		if (list != null) {
			for (String obj : list) {
				statusList.add(Integer.parseInt(obj));
			}
		}
		return statusList;
	}

	public static List<Integer> getListByValues(Integer value) {
		if (value != null) {
			List<Integer> list = new ArrayList<>();
			list.add(value);
			return list;
		}
		return new ArrayList<>();
	}

	public static String getOperatorNameFromFileName(String fileName) {
		if (fileName.contains(ForesightConstants.DOLLER)) {
			String[] splitedFile = fileName.split("\\$");
			splitedFile = splitedFile[ForesightConstants.INDEX_ONE].split(Symbol.UNDERSCORE_STRING);
			if (splitedFile.length > QMDLConstant.OPERATOR_INDEX) {
				return splitedFile[ForesightConstants.INDEX_FOUR];
			}
		} else {
			String[] splitedFile = fileName.split(Symbol.UNDERSCORE_STRING);
			if (splitedFile.length > QMDLConstant.OPERATOR_INDEX) {
				return splitedFile[QMDLConstant.OPERATOR_INDEX];
			}
		}
		return null;
	}

	public static String getLevelType(String levelType) {
		if (UmConstants.L1.equalsIgnoreCase(levelType)) {
			return UmConstants.L1;
		} else if (UmConstants.L2.equalsIgnoreCase(levelType)) {
			return UmConstants.L2;
		} else if (UmConstants.L3.equalsIgnoreCase(levelType)) {
			return UmConstants.L3;
		} else if (UmConstants.L4.equalsIgnoreCase(levelType)) {
			return UmConstants.L4;
		} else if (UmConstants.NHQ.equalsIgnoreCase(levelType)) {
			return UmConstants.NHQ;
		} else {
			return UmConstants.L1;
		}
	}

	public static String createPartialWorkorderId(String bandType, String reportType) {
		StringBuilder sb = new StringBuilder();
		sb.append("WO");
		if(reportType != null) {
			sb.append(Symbol.HYPHEN).append(reportType);
		}
		sb.append(Symbol.HYPHEN).append(NVConstant.DATE_FORMAT_WW_YYYY.format(new Date()));
		
		if(bandType != null) {
			sb.append(Symbol.HYPHEN).append(bandType);
		}
				
		return sb.toString();
	}

	public static String escapeSpecialCharactersForCriteria(String value) {
		return value
				.replace("_",   HIBERNATE_ESCAPE_CHAR + "_")
				.replace("%",   HIBERNATE_ESCAPE_CHAR + "%");

	}
	public static Map<String, Integer> getPCIAzimuthMap(List<SiteInformationWrapper> siteWrapper,Map<String, String> geoWoMetaMap){
		
		if (siteWrapper != null && !siteWrapper.isEmpty()) {
			Map<String, Integer> pciAzimuthMap = new HashMap<>();
		List<Integer> earfcnList = siteWrapper.stream().distinct()
				.filter(s -> s.getEarfcn() != null).map(s -> s.getEarfcn())
				.collect(Collectors.toList());
		geoWoMetaMap.put(NVWorkorderConstant.EARFCN,
				String.valueOf(StringUtils.join(earfcnList, ForesightConstants.COMMA)));

		Map<Integer, List<SiteInformationWrapper>> siteInfoWrapperMap = siteWrapper.stream()
				.distinct().filter(s -> s.getPci() != null)
				.collect(Collectors.groupingBy(x -> x.getPci()));
		siteInfoWrapperMap.forEach((pci, siteWrapperList) -> {
			pciAzimuthMap.put(pci.toString(),
					com.inn.commons.lang.NumberUtils.toInteger(siteWrapperList.stream()
							.filter(c -> c.getAzimuth() != null
									&& !c.getAzimuth().equalsIgnoreCase(ForesightConstants.DASH))
							.findFirst().orElse(new SiteInformationWrapper()).getAzimuth()));
		});
		return pciAzimuthMap;
	}
		return null;
	}

	public static String getfileNameForSynchedFile(String fileName) {
		try {
			fileName = fileName.replace(NVLayer3Constants.ZIP_FILE_EXTENTION, Symbol.EMPTY_STRING);

			if(fileName.contains(Symbol.PARENTHESIS_OPEN_STRING)) {				
				fileName = StringUtils.substringBeforeLast(fileName, Symbol.PARENTHESIS_OPEN_STRING);
			}
			if(fileName.contains(Symbol.BRACKET_OPEN_STRING)) {				
				fileName = StringUtils.substringBeforeLast(fileName, Symbol.BRACKET_OPEN_STRING);
			}
			
			
			if (fileName.contains(Symbol.UNDERSCORE_STRING)) {
				String[] splitedFile = fileName.split(Symbol.UNDERSCORE_STRING);
				if (splitedFile.length > ForesightConstants.THREE) {

					if (isVallidTimeStamp(splitedFile[splitedFile.length - ForesightConstants.THREE])
							&& isVallidTimeStamp(splitedFile[splitedFile.length - ForesightConstants.TWO])
							&& isVallidTimeStamp(splitedFile[splitedFile.length - ForesightConstants.ONE])) {

						fileName = StringUtils.substringBeforeLast(fileName, Symbol.UNDERSCORE_STRING)
								+ Symbol.UNDERSCORE + System.currentTimeMillis() + NVLayer3Constants.ZIP_FILE_EXTENTION;

						return fileName;

					}
				}
			}
			fileName = fileName + Symbol.UNDERSCORE + System.currentTimeMillis() + NVLayer3Constants.ZIP_FILE_EXTENTION;
			return fileName;

		} catch (Exception e) {
			logger.error("Exception inside method getfileNameForSynchedFile: {}", Utils.getStackTrace(e));
		}
		return fileName;
	}

	public static Boolean isVallidTimeStamp(String timestamp) {
		try {
			if (!StringUtils.isBlank(timestamp)) {
				Date date1Jan2016 = new Date();
				date1Jan2016.setTime(NVLayer3Constants.TIMESTAMP_1JAN_2016);

				Date date = new Date();
				date.setTime(Long.parseLong(timestamp));

				if (date.after(date1Jan2016)) {
					return true;
				} else {
					return false;
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	public static String vallidateZipFile(String filePath, String localFilePath) {
		logger.info("Going to vallidate ZipFile {}  ", filePath);
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(filePath);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				logger.info("Going to process zip file {}  ", entry.getName());
				if (entry.isDirectory()) {
					return NVLayer3Constants.CONTAINS_DIRECTORY_MESSAGE;

				} else if (entry.getName().contains(Symbol.SLASH_BACKWARD_STRING)
						|| entry.getName().contains(Symbol.SLASH_FORWARD_STRING)) {
					return NVLayer3Constants.CONTAINS_DIRECTORY_MESSAGE;

				} else if (entry.getName().toLowerCase().endsWith(NVLayer3Constants.ZIP_FILE_EXTENTION)) {
					return NVLayer3Constants.CONTAINS_ZIP_MESSAGE;
				} else {
					try {

						NVLayer3Utils.copyFileToLocalPath(localFilePath + entry.getName(),
								zipFile.getInputStream(entry));
					} catch (Exception e) {
						logger.error("Error inside vallidateZipFile in synched file {} ", Utils.getStackTrace(e));
						return NVLayer3Constants.CORRUPTED_ZIP_MESSAGE;
					}
				}

			}
			return NVLayer3Constants.SUCCESS;
		} catch (Exception e) {
			logger.error("Error inside method vallidateZipFile  {} ", Utils.getStackTrace(e));
			return NVLayer3Constants.CORRUPTED_ZIP_MESSAGE;
		} finally {
			if (zipFile != null) {
				try {
					zipFile.close();
				} catch (IOException e) {
					logger.error("Error inside vallidateZipFile in closing zip stream  {} ", Utils.getStackTrace(e));

				}
			}
		}

	}

}
