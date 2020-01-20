package com.inn.foresight.module.nv.layer3.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.http.HttpException;
import com.inn.commons.http.HttpGetRequest;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.unit.Duration;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.app.constants.AppConstants;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.inbuilding.utils.NVBuildingUtils;
import com.inn.foresight.module.nv.layer.LayerPlotUtils;
import com.inn.foresight.module.nv.layer.constants.LayerPlotConstants;
import com.inn.foresight.module.nv.layer3.constants.Layer3PPEConstant;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HbaseDao;
import com.inn.foresight.module.nv.layer3.dao.Layer3MetaDataDao;
import com.inn.foresight.module.nv.layer3.model.Layer3MetaData;
import com.inn.foresight.module.nv.layer3.service.ILayer3PPEService;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.layer3.wrapper.Layer3PPEWrapper;
import com.inn.foresight.module.nv.layer3.wrapper.Layer3StatisticsWrapper;
import com.inn.foresight.module.nv.report.RangeSlab;
import com.inn.foresight.module.nv.report.utils.ExcelReportUtils;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.utils.Statistics;
import com.inn.foresight.module.nv.report.utils.ZipUtils;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.workorder.dao.IWOFileDetailDao;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.wrapper.WOFileDetailWrapper;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;

@Service("Layer3PPEServiceImpl")
public class Layer3PPEServiceImpl extends NVLayer3Utils implements ILayer3PPEService {
	
	@Autowired
	private INVLayer3HbaseDao nvLayer3HbaseDao;

	@Autowired
	private IGenericWorkorderDao genericWorkorderDao;

	@Autowired
	private ILegendRangeDao legendRangeDao;

	@Autowired
	private IWORecipeMappingDao woRecipeMappingdao;

	@Autowired
	private Layer3MetaDataDao layer3MetaDataDao;

	@Autowired
	private INVLayer3DashboardService iNVLayer3DashboardService;

	@Autowired
	private IWOFileDetailDao iwoFileDetailDao;

	Layer3PPEServiceImpl() {
	}

	private Logger logger = LogManager.getLogger(Layer3PPEServiceImpl.class);

	@Override
	public Response getSignalMessageDetailMicroServiceUrl(String rowKey, String msgType, HttpServletRequest request) {
		logger.info("Going to getSignalMessageDetail for rowKey {} msgType {}   ", rowKey, msgType);
		try {
			Duration duration = Duration
					.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
			HttpGetRequest result = NVLayer3Utils.sendHttpGetRequest(getDropwizardUrlForLayer3PPE(request), true,
					duration);
			return Response.ok(result.getString()).build();
		} catch (Exception e) {
			logger.error("Exception in getSignalMessageDetail : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}

	@Override
	public Response processWOReportDumpMicroServiceUrl(Integer workorderId, Map<String, List<String>> map,
			HttpServletRequest request) {
		try {
			if (workorderId != null && map != null) {
				logger.info("Going to processWOReportDumpMicroServiceUrl Data for workorderId {} map {}  ", workorderId,
						map);
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
				String json = new Gson().toJson(map);
				StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
				String response = NVLayer3Utils
						.sendHttpPostRequest(getDropwizardUrlForLayer3PPE(request), httpEntity, true, duration)
						.getString();
				return Response.ok(response).build();
			} else {
				logger.error(QMDLConstant.INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in processWOReportDumpMicroServiceUrl : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@Override
	public Response getWOReportDumpMicroServiceUrl(HttpServletRequest request) {
		try {
			HttpResponse response = NVBuildingUtils.sendGetRequestWithoutTimeOut(getDropwizardUrlForLayer3PPE(request));
			Response.ResponseBuilder builder = Response.status(NVConstant.TWO_HUNDRED);
			builder = builder.entity(response.getEntity().getContent())
					.header(ReportConstants.CONTENT_TYPE, response.getEntity().getContentType())
					.header(ReportConstants.CONTENT_DISPOSITION,
							response.getFirstHeader(ReportConstants.CONTENT_DISPOSITION).getValue());
			return builder.build();
		} catch (Exception e) {
			logger.error("Exception in getWOReportDumpMicroServiceUrl : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@Override
	public Response getSignalMessageRecipeWiseMicroServiceUrl(Integer workorderId, Map<String, List<String>> map,
			String lastRowKey, String direction, HttpServletRequest request) {
		try {
			if (workorderId != null && map != null) {
				logger.info(
						"Going to get NVLayer3 getSignalMessageRecipeWise Data for workorderId {} lastRowKey {}   direction {}  map {}  ",
						workorderId, lastRowKey, direction, map);
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
				String json = new Gson().toJson(map);
				StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
				String response = NVLayer3Utils
						.sendHttpPostRequest(getDropwizardUrlForLayer3PPE(request), httpEntity, true, duration)
						.getString();
				return Response.ok(response).build();
			} else {
				logger.error(QMDLConstant.INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getSignalMessageRecipeWise : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@Override
	public Response getSignalMessagesForBinMicroServiceUrl(String rowPrefix, HttpServletRequest request) {
		try {
			if (rowPrefix != null && !StringUtils.isBlank(rowPrefix)) {
				logger.info("Going to get NVLayer3 getSignalMessageRecipeWise Data for rowPrefix {}  ", rowPrefix);
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
				HttpGetRequest result = NVLayer3Utils.sendHttpGetRequest(getDropwizardUrlForLayer3PPE(request), true,
						duration);
				return Response.ok(result.getString()).build();
			} else {
				logger.error(QMDLConstant.INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getSignalMessagesForBin : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}



	@Override
	public Response getDriveDetailForStatisticDataXlsMicroServiceUrl(Integer workorderId, Map<String, List<String>> map,
			HttpServletRequest request) {
		try {
			if (workorderId != null && map != null) {
				logger.info("Going to get NVLayer3 getDriveDetailForStatisticDataXls Data for workorderId {} map {}  ",
						workorderId, map);
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
				String json = new Gson().toJson(map);
				StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
				String response = NVLayer3Utils
						.sendHttpPostRequest(getDropwizardUrlForLayer3PPE(request), httpEntity, true, duration)
						.getString();
				return Response.ok(response).build();
			} else {
				logger.error(QMDLConstant.INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in getDriveDetailForStatisticDataXls : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}

	@Override
	public Response createSignalMessageRecipeWiseCsvMicroServiceUrl(Integer workorderId, Map<String, List<String>> map,
			HttpServletRequest request) {
		try {
			if (workorderId != null && map != null) {
				logger.info("Going to  createSignalMessageRecipeWiseCsv Data for workorderId {}  map {}  ", workorderId,
						map);
				Duration duration = Duration
						.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
				String json = new Gson().toJson(map);
				StringEntity httpEntity = new StringEntity(json, ContentType.APPLICATION_JSON);
				String response = NVLayer3Utils
						.sendHttpPostRequest(getDropwizardUrlForLayer3PPE(request), httpEntity, true, duration)
						.getString();
				return Response.ok(response).build();
			} else {
				logger.error(QMDLConstant.INVALID_PARAMETER);
				return Response.ok(INVALID_PARAMETER_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Exception in createSignalMessageRecipeWiseCsv : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}



	@Override
	public List<Get> getListForSummary(Integer workorderId, List<String> recipeId, List<String> columnList) {
		List<Get> getList = new ArrayList<>();
		Set<String> recipeIdSet = new HashSet<>(recipeId);
		for (String receipeId : recipeIdSet) {
			Get get = new Get(Bytes.toBytes(getRowKeyForLayer3PPE(workorderId, receipeId)));
			addColumnsToGet(columnList, get);
			getList.add(get);

		}
		return getList;
	}

	private void addColumnsToGet(List<String> columnList, Get get) {
		for (String column : columnList) {
			get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), column.getBytes());
		}
	}


	@Override
	public String getSignalMessageRecipeWise(Integer workorderId, String lastRowKey, String direction,
			List<String> recipeId, String message,List<String> fileId) {
		StringBuilder json = getIntialStringForJson();
		Boolean isData = (lastRowKey == null || lastRowKey.equalsIgnoreCase("null")) ? Boolean.FALSE : Boolean.TRUE;
		try {
			String tableName = ConfigUtils.getString(NVLayer3Constants.LAYER3_MESSAGES_DATA_TABLE);
			Set<String> prefixList = getPrefixListFromParamList(workorderId, recipeId,fileId);
			List<HBaseResult> resultList=new ArrayList<>();
			for (String prefix : prefixList) {
				List<HBaseResult> tempResultList = nvLayer3HbaseDao
						.scanQMDLDataFromHBaseWithPaginationForFramework(tableName, prefix, direction, lastRowKey);
				if (tempResultList != null && !tempResultList.isEmpty()) {
					resultList.addAll(tempResultList);
				}
			}

			if (!resultList.isEmpty()) {
				sortHbaseResultByTimestamp(resultList);
				if (lastRowKey != null && !lastRowKey.equalsIgnoreCase("ALL")) {
					if (!StringUtils.isEmpty(lastRowKey)) {
						resultList = resultList.stream()
								.filter(p -> p.getStringAsLong(QMDLConstant.TIMESTAMP) > Long
										.valueOf(lastRowKey.split(Symbol.UNDERSCORE_STRING)[1]))
								.limit(500L).collect(Collectors.toList());
					} else {
						resultList = resultList.stream().limit(500L).collect(Collectors.toList());
					}
				}
			}

			for (HBaseResult result : resultList) {
				isData = Boolean.TRUE;
				json.append(getParsedSignalMessageData(result, message));

			}

		} catch (Exception e) {
			logger.error("Exception in Getting Result from hbase {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
		return getSignalingMsgFinalResponse(json, isData);
	}

	private boolean isSearchTermAvailableInHBaseResult(HBaseResult result, String searchTerm) {
		return isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.B0C0_MSG), searchTerm)
				|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.B0C0_MSG_TYPE), searchTerm)
				|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.EMM_MESSAGE), searchTerm)
				|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.EMM_BEAN), searchTerm)
				|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.EMS_MESSAGE), searchTerm)
				|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.EMS_BEAN), searchTerm)
				|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.SIP_EVENT_MSG), searchTerm)
				|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.SIP_EVENT), searchTerm);
	}

	private String getParsedSignalMessageData(HBaseResult result, String searchTerm) {
		StringBuilder json = new StringBuilder();

		if (result.getString(QMDLConstant.SIP_EVENT) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			addValueToStringBuilder(json, "IMS");
			addValueToStringBuilder(json, result.getString(QMDLConstant.SIP_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.SIP_EVENT));
			addValueToStringBuilder(json, QMDLConstant.SIP_EVENT_MSG);
			addValueToStringBuilder(json, result.getRowKey());

			if (isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.SIP_EVENT_MSG), searchTerm)
					|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.SIP_EVENT), searchTerm)) {
				addValueToStringBuilder(json, ForesightConstants.ONE_IN_STRING);
			} else {
				addValueToStringBuilder(json, ForesightConstants.ZERO_IN_STRING);
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		if (result.getString(QMDLConstant.EMM_MESSAGE) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			addValueToStringBuilder(json, "EMM");
			addValueToStringBuilder(json, result.getString(QMDLConstant.EMM_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.EMM_MESSAGE));
			addValueToStringBuilder(json, QMDLConstant.EMM_BEAN);
			addValueToStringBuilder(json, result.getRowKey());

			if (isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.EMM_BEAN), searchTerm)
					|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.EMM_MESSAGE), searchTerm)) {
				addValueToStringBuilder(json, ForesightConstants.ONE_IN_STRING);
			} else {
				addValueToStringBuilder(json, ForesightConstants.ZERO_IN_STRING);
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		if (result.getString(QMDLConstant.EMS_MESSAGE) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			addValueToStringBuilder(json, "ESM");
			addValueToStringBuilder(json, result.getString(QMDLConstant.EMS_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.EMS_MESSAGE));
			addValueToStringBuilder(json, QMDLConstant.EMS_BEAN);
			addValueToStringBuilder(json, result.getRowKey());

			if (isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.EMS_BEAN), searchTerm)
					|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.EMS_MESSAGE), searchTerm)) {
				addValueToStringBuilder(json, ForesightConstants.ONE_IN_STRING);
			} else {
				addValueToStringBuilder(json, ForesightConstants.ZERO_IN_STRING);
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		if (result.getString(QMDLConstant.B0C0_MSG_TYPE) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			if (result.getString(QMDLConstant.B0C0_MSG_TYPE) != null
					&& result.getString(QMDLConstant.B0C0_MSG_TYPE).contains(QMDLConstant.LTE_ML1)
					&& result.getString(QMDLConstant.B0C0_MSG_TYPE).contains(QMDLConstant.MSG)) {
				addValueToStringBuilder(json, "MAC");
			} else {
				addValueToStringBuilder(json, "ERRC");
			}

			addValueToStringBuilder(json, result.getString(QMDLConstant.B0C0_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.B0C0_MSG_TYPE));
			addValueToStringBuilder(json, QMDLConstant.B0C0_MSG);
			addValueToStringBuilder(json, result.getRowKey());

			if (isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.B0C0_MSG_TYPE), searchTerm)
					|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.B0C0_MSG), searchTerm)) {
				addValueToStringBuilder(json, ForesightConstants.ONE_IN_STRING);
			} else {
				addValueToStringBuilder(json, ForesightConstants.ZERO_IN_STRING);
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		if (result.getString(QMDLConstant.LOGCODE_412F_MSG_TYPE) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			addValueToStringBuilder(json, "RRC");
			addValueToStringBuilder(json, result.getString(QMDLConstant.LOGCODE_412F_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.LOGCODE_412F_MSG_TYPE));
			addValueToStringBuilder(json, QMDLConstant.LOGCODE_412F_MSG);
			addValueToStringBuilder(json, result.getRowKey());

			if (isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.LOGCODE_412F_MSG), searchTerm)
					|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.LOGCODE_412F_MSG_TYPE), searchTerm)) {
				addValueToStringBuilder(json, ForesightConstants.ONE_IN_STRING);
			} else {
				addValueToStringBuilder(json, ForesightConstants.ZERO_IN_STRING);
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		if (result.getString(QMDLConstant.LOGCODE_512F_MSG_TYPE) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			addValueToStringBuilder(json, QMDLConstant.GSM_RR);
			addValueToStringBuilder(json, result.getString(QMDLConstant.LOGCODE_512F_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.LOGCODE_512F_MSG_TYPE));
			addValueToStringBuilder(json, QMDLConstant.LOGCODE_512F_MSG);
			addValueToStringBuilder(json, result.getRowKey());

			if (isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.LOGCODE_512F_MSG), searchTerm)
					|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.LOGCODE_512F_MSG_TYPE), searchTerm)) {
				addValueToStringBuilder(json, ForesightConstants.ONE_IN_STRING);
			} else {
				addValueToStringBuilder(json, ForesightConstants.ZERO_IN_STRING);
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		if (result.getString(QMDLConstant.LOGCODE_713A_MSG_TYPE) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			addValueToStringBuilder(json, QMDLConstant.UMTS_NAS);
			addValueToStringBuilder(json, result.getString(QMDLConstant.LOGCODE_713A_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.LOGCODE_713A_MSG_TYPE));
			addValueToStringBuilder(json, QMDLConstant.LOGCODE_713A_MSG);
			addValueToStringBuilder(json, result.getRowKey());

			if (isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.LOGCODE_713A_MSG), searchTerm)
					|| isValidHbaseColumnWithSearchTerm(result.getString(QMDLConstant.LOGCODE_713A_MSG_TYPE), searchTerm)) {
				addValueToStringBuilder(json, ForesightConstants.ONE_IN_STRING);
			} else {
				addValueToStringBuilder(json, ForesightConstants.ZERO_IN_STRING);
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		return json.toString();
	}

	private boolean isValidHbaseColumnWithSearchTerm(String result, String searchTerm) {
		return result != null && searchTerm != null && !searchTerm.equalsIgnoreCase("null")
				&& result.toUpperCase().contains(searchTerm.toUpperCase());
	}

	private String getSignalingMsgFinalResponse(StringBuilder json, Boolean isData) {
		if (isData) {
			addPostFixOfcsvIntoJson(json);
			return json.toString();
		} else {
			return QMDLConstant.NO_DATA_FOUND;
		}
	}

	@Override
	public String getSignalMessagesForBin(String binRowkey) {
		try {
			StringBuilder json = getIntialStringForJson();
			String tableName = ConfigUtils.getString(NVLayer3Constants.LAYER3_MESSAGES_DATA_TABLE);

			Scan scanAllMsg = NVLayer3Utils.getScanForSignalMessageForByRowKeyPrefix(binRowkey.substring(0, 13),
					SCAN_ROW_SEPERATOR);
			List<HBaseResult> allMsgHbaseResult = nvLayer3HbaseDao.scanQMDLDataFromHbase(tableName, scanAllMsg);
			logger.info("All msg size is {}", allMsgHbaseResult.size());
			sortHbaseResultByTimestamp(allMsgHbaseResult);

			Scan scanSignalMessageForBinByRowKeyPrefix = NVLayer3Utils.getScanForSignalMessageForByRowKeyPrefix(
					binRowkey.split(Symbol.UNDERSCORE_STRING)[0], SCAN_ROW_SEPERATOR);
			List<HBaseResult> hbaseResultsForBin = nvLayer3HbaseDao.scanQMDLDataFromHbase(tableName,
					scanSignalMessageForBinByRowKeyPrefix);
			sortHbaseResultByTimestamp(hbaseResultsForBin);
			logger.info("Bin msg size is {}", hbaseResultsForBin.size());

			List<HBaseResult> msgList = new ArrayList<HBaseResult>();

			if (!allMsgHbaseResult.isEmpty()) {
				if (!hbaseResultsForBin.isEmpty() && hbaseResultsForBin.size() > 0) {

					OUTER_LOOP: for (HBaseResult hBaseResult : allMsgHbaseResult) {
						if (hBaseResult.getRowKey().contains(binRowkey.split(Symbol.UNDERSCORE_STRING)[0])) {
							msgList.addAll(hbaseResultsForBin);
							break OUTER_LOOP;
						}
						msgList.add(hBaseResult);
					}

				if (allMsgHbaseResult.size() > msgList.size() + 100) {
					List<HBaseResult> afterBin100Msgs = new ArrayList<>();
					afterBin100Msgs = allMsgHbaseResult.subList(msgList.size(), msgList.size() + 100);
					msgList.addAll(afterBin100Msgs);
				} else if (allMsgHbaseResult.size() > msgList.size()) {
					List<HBaseResult> afterBin100Msgs = new ArrayList<>();
					afterBin100Msgs = allMsgHbaseResult.subList(msgList.size() - 1,
							msgList.size() - 1 + (allMsgHbaseResult.size() - msgList.size()));
					msgList.addAll(afterBin100Msgs);
				}

				if (!msgList.isEmpty()) {
					for (HBaseResult result : msgList) {
						json.append(getParsedSignalMessageDataAndHighLightBinMsg(result,
								binRowkey.split(Symbol.UNDERSCORE_STRING)[0]));
					}
				}
				logger.info("Hbase result size when bin have msgs {}", msgList.size());

				} else {
					msgList = allMsgHbaseResult.stream()
							.filter(p -> p.getStringAsLong(QMDLConstant.TIMESTAMP) < Long
									.valueOf(binRowkey.split(Symbol.UNDERSCORE_STRING)[1]))
							.collect(Collectors.toList());
					for (HBaseResult result : msgList) {
						json.append(getParsedSignalMessageDataAndHighLightBinMsg(result, null));
					}
					logger.info("Hbase result size before timestamp {}", msgList.size());

					msgList = allMsgHbaseResult.stream()
							.filter(p -> p.getStringAsLong(QMDLConstant.TIMESTAMP) >= Long
							.valueOf(binRowkey.split(Symbol.UNDERSCORE_STRING)[1]))
							.limit(100).collect(Collectors.toList());
					Boolean firstMsg = true;
					for (HBaseResult result : msgList) {
						json.append(getParsedSignalMessageDataAndHighLightFirst(result, firstMsg));
						firstMsg = false;
					}
					logger.info("Hbase result size after timestamp {}", msgList.size());
				}
			}

			addPostFixOfcsvIntoJson(json);
			return json.toString();
		} catch (Exception e) {
			logger.error("Exception in getSignalMessagesForBin {}   {}", binRowkey, Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
	}

	private String getParsedSignalMessageDataAndHighLightFirst(HBaseResult result, Boolean firstMsg) {
		StringBuilder json = new StringBuilder();
		if (result.getString(QMDLConstant.SIP_EVENT) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			addValueToStringBuilder(json, "IMS");
			addValueToStringBuilder(json, result.getString(QMDLConstant.SIP_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.SIP_EVENT));
			addValueToStringBuilder(json, QMDLConstant.SIP_EVENT_MSG);
			addValueToStringBuilder(json, result.getRowKey());
			if (firstMsg) {
				addValueToStringBuilder(json, "1");
			} else {
				addValueToStringBuilder(json, "0");
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		if (result.getString(QMDLConstant.EMM_MESSAGE) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			addValueToStringBuilder(json, "EMM");
			addValueToStringBuilder(json, result.getString(QMDLConstant.EMM_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.EMM_MESSAGE));
			addValueToStringBuilder(json, QMDLConstant.EMM_BEAN);
			addValueToStringBuilder(json, result.getRowKey());
			if (firstMsg) {
				addValueToStringBuilder(json, "1");
			} else {
				addValueToStringBuilder(json, "0");
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		if (result.getString(QMDLConstant.EMS_MESSAGE) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			addValueToStringBuilder(json, "ESM");
			addValueToStringBuilder(json, result.getString(QMDLConstant.EMS_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.EMS_MESSAGE));
			addValueToStringBuilder(json, QMDLConstant.EMS_BEAN);
			addValueToStringBuilder(json, result.getRowKey());
			if (firstMsg) {
				addValueToStringBuilder(json, "1");
			} else {
				addValueToStringBuilder(json, "0");
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		if (result.getString(QMDLConstant.B0C0_MSG_TYPE) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			if (result.getString(QMDLConstant.B0C0_MSG_TYPE) != null
					&& result.getString(QMDLConstant.B0C0_MSG_TYPE).contains("LTE ML1")
					&& result.getString(QMDLConstant.B0C0_MSG_TYPE).contains("MSG")) {
				addValueToStringBuilder(json, "MAC");
			} else {
				addValueToStringBuilder(json, "ERRC");
			}

			addValueToStringBuilder(json, result.getString(QMDLConstant.B0C0_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.B0C0_MSG_TYPE));
			addValueToStringBuilder(json, QMDLConstant.B0C0_MSG);
			addValueToStringBuilder(json, result.getRowKey());

			if (firstMsg) {
				addValueToStringBuilder(json, "1");
			} else {
				addValueToStringBuilder(json, "0");
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		if (result.getString(QMDLConstant.LOGCODE_412F_MSG_TYPE) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			addValueToStringBuilder(json, "RRC");
			addValueToStringBuilder(json, result.getString(QMDLConstant.LOGCODE_412F_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.LOGCODE_412F_MSG_TYPE));
			addValueToStringBuilder(json, QMDLConstant.LOGCODE_412F_MSG);
			addValueToStringBuilder(json, result.getRowKey());
			if (firstMsg) {
				addValueToStringBuilder(json, "1");
			} else {
				addValueToStringBuilder(json, "0");
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		if (result.getString(QMDLConstant.LOGCODE_512F_MSG_TYPE) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			addValueToStringBuilder(json, "GSM RR");
			addValueToStringBuilder(json, result.getString(QMDLConstant.LOGCODE_512F_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.LOGCODE_512F_MSG_TYPE));
			addValueToStringBuilder(json, QMDLConstant.LOGCODE_512F_MSG);
			addValueToStringBuilder(json, result.getRowKey());
			if (firstMsg) {
				addValueToStringBuilder(json, "1");
			} else {
				addValueToStringBuilder(json, "0");
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		if (result.getString(QMDLConstant.LOGCODE_713A_MSG_TYPE) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			addValueToStringBuilder(json, "UMTS NAS");
			addValueToStringBuilder(json, result.getString(QMDLConstant.LOGCODE_713A_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.LOGCODE_713A_MSG_TYPE));
			addValueToStringBuilder(json, QMDLConstant.LOGCODE_713A_MSG);
			addValueToStringBuilder(json, result.getRowKey());
			if (firstMsg) {
				addValueToStringBuilder(json, "1");
			} else {
				addValueToStringBuilder(json, "0");
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}		
		return json.toString();
	}

	private String getParsedSignalMessageDataAndHighLightBinMsg(HBaseResult result, String rowPrefix) {
		StringBuilder json = new StringBuilder();
		if (result.getString(QMDLConstant.SIP_EVENT) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			addValueToStringBuilder(json, "IMS");
			addValueToStringBuilder(json, result.getString(QMDLConstant.SIP_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.SIP_EVENT));
			addValueToStringBuilder(json, QMDLConstant.SIP_EVENT_MSG);
			addValueToStringBuilder(json, result.getRowKey());

			if (rowPrefix != null && result.getRowKey().contains(rowPrefix)) {
				addValueToStringBuilder(json, "1");
			} else {
				addValueToStringBuilder(json, "0");
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		if (result.getString(QMDLConstant.EMM_MESSAGE) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			addValueToStringBuilder(json, "EMM");
			addValueToStringBuilder(json, result.getString(QMDLConstant.EMM_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.EMM_MESSAGE));
			addValueToStringBuilder(json, QMDLConstant.EMM_BEAN);
			addValueToStringBuilder(json, result.getRowKey());
			if (rowPrefix != null && result.getRowKey().contains(rowPrefix)) {
				addValueToStringBuilder(json, "1");
			} else {
				addValueToStringBuilder(json, "0");
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		if (result.getString(QMDLConstant.EMS_MESSAGE) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			addValueToStringBuilder(json, "ESM");
			addValueToStringBuilder(json, result.getString(QMDLConstant.EMS_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.EMS_MESSAGE));
			addValueToStringBuilder(json, QMDLConstant.EMS_BEAN);
			addValueToStringBuilder(json, result.getRowKey());

			if (rowPrefix != null && result.getRowKey().contains(rowPrefix)) {
				addValueToStringBuilder(json, "1");
			} else {
				addValueToStringBuilder(json, "0");
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		if (result.getString(QMDLConstant.B0C0_MSG_TYPE) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			if (result.getString(QMDLConstant.B0C0_MSG_TYPE) != null
					&& result.getString(QMDLConstant.B0C0_MSG_TYPE).contains("LTE ML1")
					&& result.getString(QMDLConstant.B0C0_MSG_TYPE).contains("MSG")) {
				addValueToStringBuilder(json, "MAC");
			} else {
				addValueToStringBuilder(json, "ERRC");
			}

			addValueToStringBuilder(json, result.getString(QMDLConstant.B0C0_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.B0C0_MSG_TYPE));
			addValueToStringBuilder(json, QMDLConstant.B0C0_MSG);
			addValueToStringBuilder(json, result.getRowKey());

			if (rowPrefix != null && result.getRowKey().contains(rowPrefix)) {
				addValueToStringBuilder(json, "1");
			} else {
				addValueToStringBuilder(json, "0");
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		if (result.getString(QMDLConstant.LOGCODE_412F_MSG_TYPE) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			addValueToStringBuilder(json, "RRC");
			addValueToStringBuilder(json, result.getString(QMDLConstant.LOGCODE_412F_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.LOGCODE_412F_MSG_TYPE));
			addValueToStringBuilder(json, QMDLConstant.LOGCODE_412F_MSG);
			addValueToStringBuilder(json, result.getRowKey());

			if (rowPrefix != null && result.getRowKey().contains(rowPrefix)) {
				addValueToStringBuilder(json, "1");
			} else {
				addValueToStringBuilder(json, "0");
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		if (result.getString(QMDLConstant.LOGCODE_512F_MSG_TYPE) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			addValueToStringBuilder(json, "GSM RR");
			addValueToStringBuilder(json, result.getString(QMDLConstant.LOGCODE_512F_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.LOGCODE_512F_MSG_TYPE));
			addValueToStringBuilder(json, QMDLConstant.LOGCODE_512F_MSG);
			addValueToStringBuilder(json, result.getRowKey());

			if (rowPrefix != null && result.getRowKey().contains(rowPrefix)) {
				addValueToStringBuilder(json, "1");
			} else {
				addValueToStringBuilder(json, "0");
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		if (result.getString(QMDLConstant.LOGCODE_713A_MSG_TYPE) != null) {
			json.append(Symbol.BRACKET_OPEN_STRING);
			addValueToStringBuilder(json, result.getString(QMDLConstant.TIMESTAMP));
			addValueToStringBuilder(json, "UMTS NAS");
			addValueToStringBuilder(json, result.getString(QMDLConstant.LOGCODE_713A_DIRECTION));
			addValueToStringBuilder(json, result.getString(QMDLConstant.LOGCODE_713A_MSG_TYPE));
			addValueToStringBuilder(json, QMDLConstant.LOGCODE_713A_MSG);
			addValueToStringBuilder(json, result.getRowKey());

			if (rowPrefix != null && result.getRowKey().contains(rowPrefix)) {
				addValueToStringBuilder(json, "1");
			} else {
				addValueToStringBuilder(json, "0");
			}
			json.setLength(json.length() - QMDLConstant.STRING_EXTRA_LENTH);
			json.append(Symbol.BRACKET_CLOSE_STRING).append(Symbol.COMMA);
		}

		return json.toString();
	}

	@Override
	public String getSignalMessageDetail(String rowKey, String msgType) {
		try {
			String tableName = ConfigUtils.getString(NVLayer3Constants.LAYER3_MESSAGES_DATA_TABLE);
			HBaseResult hbaseResult = nvLayer3HbaseDao.getSignalMessageDetail(tableName, msgType, rowKey);
			if (hbaseResult != null) {
				return getJsonFromDetailMsg(hbaseResult.getString(msgType));
			}
		} catch (Exception e) {
			logger.error("Error in Getting getSignalMessageDetail from hbase {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
		return QMDLConstant.NO_RESULT_FOUND;
	}

	private String getJsonFromDetailMsg(String stringValue) {
		return QMDLConstant.RESULT + stringValue + QMDLConstant.MSG_POSTFIX;
	}

	@Override
	public String processWOReportDump(Integer workorderId, List<String> recipeIdList,List<String> fileIdList) {
		try {


			GenericWorkorder genericworkorder = genericWorkorderDao.findByPk(workorderId);
			Boolean isInbuildingWO = NVLayer3Utils.isInBuidlingRecord(genericworkorder);
			logger.info("Is isInbuildingWO WO {}", isInbuildingWO);			

			if (isInbuildingWO && genericworkorder.getIbOldWorkorderStatus()!= null && genericworkorder.getIbOldWorkorderStatus()) {
				List<String> operatorList = iNVLayer3DashboardService.getDriveRecipeDetail(workorderId)
						.get(NVLayer3Constants.OPERATOR);

				return iNVLayer3DashboardService.processWOReportDump(workorderId, recipeIdList, operatorList);
			}

			Boolean isStealthWO = genericworkorder.getTemplateType().name().equals(TemplateType.NV_STEALTH.name());

			logger.info("Going to process WO Report Dump: workorderId {}   recipeIdList {}", workorderId, recipeIdList);
			logger.info("Is isStealthWO WO {}", isStealthWO);

			Set<String> columnsList = new LinkedHashSet<String>();
			Set<String> csvHeaders = new LinkedHashSet<String>();
			csvHeaders.add("Date & Time");
			csvHeaders.addAll(COMMON_HBASE_HEADER);

			getListofAllHbaseColumnsForLayer3Framework(columnsList, csvHeaders, isStealthWO);

			logger.info("Getting hbase columns as: {}", columnsList);
			if (!columnsList.isEmpty()) {
				List<List<String>> driveData = getDriveDetailDataForCsvDump(workorderId, recipeIdList, columnsList,fileIdList);

				if (driveData != null && !driveData.isEmpty()) {

					SortedMap<Long, List<String>> finalDriveMap = new TreeMap<>();
					for (List<String> driveDataSingle : driveData) {
						if (!StringUtils.isEmpty(driveDataSingle.get(2))) {
							finalDriveMap.put(Long.valueOf(driveDataSingle.get(2)), driveDataSingle);
						}
					}
					String csvContent = processJsonToCsv(csvHeaders, finalDriveMap);

					csvContent=removeUnwantedStringFromResultForNeighbourDataDump(csvContent);

					if (csvContent.contains(AppConstants.INVALID_PARAMETER_JSON)) {
						return WO_REPORT_NO_DATA_AVAILABLE_JSON;
					}

					String fileName = REPORT_NAME_PREFIX + workorderId + Symbol.UNDERSCORE_STRING
							+ System.currentTimeMillis() + REPORT_FILE_EXTENSION;
					String filePath = ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH) + fileName;
					InputStream stream = IOUtils.toInputStream(csvContent, ENCODING_UTF_8);
					boolean isFileCopied = NVLayer3Utils.copyFileToLocalPath(filePath, stream);
					if (isFileCopied) {
						Map<String, String> finalresponseMap = new HashMap<>();
						finalresponseMap.put(FILE_NAME, fileName);
						return new Gson().toJson(finalresponseMap);
					} else {
						return NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON;
					}
				} else {
					return QMDLConstant.NO_RESULT_FOUND;
				}

			} else {
				return NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON;
			}

		} catch (IOException e) {
			logger.error("Error in executing processWOReportDump {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
	}

	public List<List<String>> getDriveDetailDataForCsvDump(Integer workorderId, List<String> recipeIdList,
			Set<String> columnsList, List<String> fileIdList) {

		String tableName = ConfigUtils.getString(NVLayer3Constants.LAYER3_DRIVE_DETAIL_TABLE);
		List<String> rowPrefixList = getPrefixListFromParamList(workorderId, recipeIdList,fileIdList).stream()
				.collect(Collectors.toList());
		Map<String, List<String>> map = new HashMap<>();
		logger.info("rowPrefixList : {}", rowPrefixList);
		map.put(Layer3PPEConstant.ADVANCE, rowPrefixList);
		Map<String, List<List<String>>> responseMap = (Map<String, List<List<String>>>) getPPEDataForMap(
				new ArrayList<String>(columnsList), map, tableName, Layer3PPEConstant.ADVANCE);
		List<List<String>> driveData = responseMap.get(Layer3PPEConstant.ADVANCE);
		return driveData;
	}

	private String processJsonToCsv(Set<String> csvHeaders, SortedMap<Long, List<String>> finalDriveMap) {
		StringBuilder builder = new StringBuilder();
		builder.append(csvHeaders.stream().collect(Collectors.joining(Symbol.COMMA_STRING)));
		builder.append(NEW_LINE_SEPERATOR);
		for (SortedMap.Entry<Long, List<String>> entry : finalDriveMap.entrySet()) {
			List<String> row = new ArrayList<>();
			row.add(getDateFromTimestamp(entry.getKey()));

			List<String> tempList = addNeighbourDatatoCsvDump(entry);

			row.addAll(tempList);
			builder.append(row.stream().collect(Collectors.joining(Symbol.COMMA_STRING)));
			builder.append(NEW_LINE_SEPERATOR);
		}
		return builder.toString();
	}

	public List<String> addNeighbourDatatoCsvDump(SortedMap.Entry<Long, List<String>> entry) {
		List<String> tempList = entry.getValue();
		String neighbourData = tempList.get(tempList.size() - 1);
		if (neighbourData.length() > 12) {
			StringBuilder finalAllNeighbourString = new StringBuilder();
			String[] neighbourDataArray = removeUnwantedStringFromNeighbourResult(neighbourData);
			for (String neighbourSingleData : neighbourDataArray) {
				String finalSingleNeighbourData = neighbourSingleData.substring(
						StringUtils.ordinalIndexOf(neighbourSingleData, Symbol.COMMA_STRING, 3) + 1,
						StringUtils.ordinalIndexOf(neighbourSingleData, Symbol.COMMA_STRING, 13));
				finalAllNeighbourString.append(finalSingleNeighbourData);
				finalAllNeighbourString.append(Symbol.COMMA_STRING);
			}

			tempList.remove(tempList.size() - 1);
			tempList.add(finalAllNeighbourString.toString());
		}
		return tempList;
	}

	private static String getDateFromDumpCSV(List<String> csvRow) {
		if (csvRow != null && csvRow.size() > DUMP_INDEX_TIMESTAMP) {
			String timeStamp = csvRow.get(DUMP_INDEX_TIMESTAMP);
			return getDateFromTimestamp(timeStamp);
		}
		return Symbol.EMPTY_STRING;
	}





	@Override
	public void getListofAllHbaseColumnsForLayer3Framework(Set<String> columnsList, Set<String> csvHeaders,
			Boolean isStealthWO) {
		try {
			columnsList.addAll(COMMON_HBASE_COLUMNS);

			List<String> notRequiredColumn = new ArrayList<String>(Arrays
					.asList(ConfigUtils.getString(LAYER3_NOT_REQURED_HBASE_COLUMNS).split(Symbol.UNDERSCORE_STRING)));

			Layer3PPEWrapper layer3ppeWrapper = new Layer3PPEWrapper();
			layer3ppeWrapper.setLayerType(Layer3PPEConstant.ADVANCE);

			@SuppressWarnings("unchecked")
			List<Layer3MetaData> kpiBuilderMetaList = (List<Layer3MetaData>) getKPIBuilderMeta(
					layer3ppeWrapper);
			for (Layer3MetaData kpiBuilderMeta : kpiBuilderMetaList) {
				if (kpiBuilderMeta.getIsRequiredOnUi()!=null && kpiBuilderMeta.getIsRequiredOnUi() && kpiBuilderMeta.getHbaseColumnName() != null
						&& kpiBuilderMeta.getKpiName() != null
						&& !notRequiredColumn.contains(kpiBuilderMeta.getHbaseColumnName())
						&& kpiBuilderMeta.getCategory() != null && (kpiBuilderMeta.getSourceType() == null
						|| !kpiBuilderMeta.getSourceType().equalsIgnoreCase("scanner"))) {

					if ((kpiBuilderMeta.getCategory().equalsIgnoreCase("KPI")
							|| kpiBuilderMeta.getCategory().equalsIgnoreCase("Event")
							|| kpiBuilderMeta.getCategory().equalsIgnoreCase("Handover Event")
							|| kpiBuilderMeta.getCategory().equalsIgnoreCase("Advance Event")
							|| kpiBuilderMeta.getCategory().equalsIgnoreCase("Advance Kpi")
							|| kpiBuilderMeta.getCategory().equalsIgnoreCase("LogcodeCount"))) {
						columnsList.add(kpiBuilderMeta.getHbaseColumnName());
						csvHeaders.add(kpiBuilderMeta.getKpiName());

					} else if (isStealthWO && kpiBuilderMeta.getCategory().equalsIgnoreCase("Abnormal Event")) {
						columnsList.add(kpiBuilderMeta.getHbaseColumnName());
						csvHeaders.add(kpiBuilderMeta.getKpiName());
					}				
				}				
			}
			columnsList.add("NeighbourData");
			csvHeaders.add("NB1 RSRP(dBm),NB1 RSRPRX0(dBm),NB1 RSRPRX1(dBm),NB1 RSRQ(dB),NB1 RSRQRX0(dB),NB1 RSRQRX1(dB),NB1 RSSI(dBm),NB1 RSSIRX0(dBm),NB1 RSSIRX1(dBm),NB1 PCI");
			csvHeaders.add("NB2 RSRP(dBm),NB2 RSRPRX0(dBm),NB2 RSRPRX1(dBm),NB2 RSRQ(dB),NB2 RSRQRX0(dB),NB2 RSRQRX1(dB),NB2 RSSI(dBm),NB2 RSSIRX0(dBm),NB2 RSSIRX1(dBm),NB2 PCI");
			csvHeaders.add("NB3 RSRP(dBm),NB3 RSRPRX0(dBm),NB3 RSRPRX1(dBm),NB3 RSRQ(dB),NB3 RSRQRX0(dB),NB3 RSRQRX1(dB),NB3 RSSI(dBm),NB3 RSSIRX0(dBm),NB3 RSSIRX1(dBm),NB3 PCI");
			csvHeaders.add("NB4 RSRP(dBm),NB4 RSRPRX0(dBm),NB4 RSRPRX1(dBm),NB4 RSRQ(dB),NB4 RSRQRX0(dB),NB4 RSRQRX1(dB),NB4 RSSI(dBm),NB4 RSSIRX0(dBm),NB4 RSSIRX1(dBm),NB4 PCI");
			csvHeaders.add("NB5 RSRP(dBm),NB5 RSRPRX0(dBm),NB5 RSRPRX1(dBm),NB5 RSRQ(dB),NB5 RSRQRX0(dB),NB5 RSRQRX1(dB),NB5 RSSI(dBm),NB5 RSSIRX0(dBm),NB5 RSSIRX1(dBm),NB5 PCI");
			csvHeaders.add("NB6 RSRP(dBm),NB6 RSRPRX0(dBm),NB6 RSRPRX1(dBm),NB6 RSRQ(dB),NB6 RSRQRX0(dB),NB6 RSRQRX1(dB),NB6 RSSI(dBm),NB6 RSSIRX0(dBm),NB6 RSSIRX1(dBm),NB6 PCI");
			csvHeaders.add("NB7 RSRP(dBm),NB7 RSRPRX0(dBm),NB7 RSRPRX1(dBm),NB7 RSRQ(dB),NB7 RSRQRX0(dB),NB7 RSRQRX1(dB),NB7 RSSI(dBm),NB7 RSSIRX0(dBm),NB7 RSSIRX1(dBm),NB7 PCI");
			csvHeaders.add("NB8 RSRP(dBm),NB8 RSRPRX0(dBm),NB8 RSRPRX1(dBm),NB8 RSRQ(dB),NB8 RSRQRX0(dB),NB8 RSRQRX1(dB),NB8 RSSI(dBm),NB8 RSSIRX0(dBm),NB8 RSSIRX1(dBm),NB8 PCI");
			csvHeaders.add("NB9 RSRP(dBm),NB9 RSRPRX0(dBm),NB9 RSRPRX1(dBm),NB9 RSRQ(dB),NB9 RSRQRX0(dB),NB9 RSRQRX1(dB),NB9 RSSI(dBm),NB9 RSSIRX0(dBm),NB9 RSSIRX1(dBm),NB9 PCI");

		} catch (Exception e) {
			logger.error("Getting Exception in getMapofHbaseColumnandKpiEventsName   {} ", Utils.getStackTrace(e));

		}

	}




	@Override
	public String createSignalMessageRecipeWise(Integer workOrderId, List<String> recipeId, List<String> fileId, String woName) {
		try {
			String tableName = ConfigUtils.getString(NVLayer3Constants.LAYER3_MESSAGES_DATA_TABLE);
			Set<String> prefixList = getPrefixListFromParamList(workOrderId, recipeId,fileId);
			List<HBaseResult> resultList = nvLayer3HbaseDao.scanQMDLDataFromHBaseForMSGCsv(tableName, prefixList);
			sortHbaseResultByTimestamp(resultList);
			StringBuilder csv = getCsvFromHbaseResult(resultList);
			return QMDLConstant.PATH_PREFIX_JSON + writeCsvToLocalDisk(csv,
					woName.replace(Symbol.SPACE_STRING, Symbol.EMPTY_STRING) + QMDLConstant.CSV_EXTENSION,
					ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH)) + QMDLConstant.PATH_POSTFIX_JSON;
		} catch (Exception e) {
			logger.error("Error in Getting getSignalingMessagesData from hbase {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
	}

	private String writeCsvToLocalDisk(StringBuilder csv, String fileName, String filePath) throws IOException {
		String fileAbsoultePath = filePath + Symbol.SLASH_FORWARD_STRING + fileName;
		try {
			File file = new File(fileAbsoultePath);
			try (FileWriter fileWriter = new FileWriter(file)) {

				fileWriter.write(csv.toString());
				fileWriter.flush();
			}
		} catch (Exception e) {
			logger.error("Error in writeCsvToLocalDisk{}  ", Utils.getStackTrace(e));
		}
		return fileName;
	}

	private StringBuilder getCsvFromHbaseResult(List<HBaseResult> resultList) {
		StringBuilder csv = new StringBuilder();
		String delimeter = ConfigUtils.getString(QMDLConstant.LAYER3_MSG_CSV_DELIMETER);
		addSignalingMsgHeader(csv, delimeter);
		for (HBaseResult result : resultList) {
			addValueToCsv(csv, getDateFromTimestamp(result.getString(QMDLConstant.TIMESTAMP)), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.TIMESTAMP), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.SIP_EVENT), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.SIP_EVENT_MSG), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.EMM_MESSAGE), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.EMM_BEAN), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.EMS_MESSAGE), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.EMS_BEAN), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.B0C0_MSG_TYPE), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.B0C0_MSG), delimeter);			
			addValueToCsv(csv, result.getString(QMDLConstant.LOGCODE_412F_MSG_TYPE), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.LOGCODE_412F_MSG), delimeter);		
			addValueToCsv(csv, result.getString(QMDLConstant.LOGCODE_512F_MSG_TYPE), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.LOGCODE_512F_MSG), delimeter);		
			addValueToCsv(csv, result.getString(QMDLConstant.LOGCODE_713A_MSG_TYPE), delimeter);
			addValueToCsv(csv, result.getString(QMDLConstant.LOGCODE_713A_MSG), delimeter);		
			csv.setLength(csv.length() - QMDLConstant.STRING_EXTRA_LENTH);
			csv.append(QMDLConstant.NEXT_LINE);
		}
		return csv;
	}

	private void addValueToCsv(StringBuilder csv, String stringValue, String delimeter) {
		if (stringValue != null) {
			if (stringValue.contains(delimeter)) {
				stringValue = stringValue.replace(delimeter, Symbol.EMPTY_STRING);
			}
			csv.append(stringValue);
		}
		csv.append(delimeter);
	}

	private void addSignalingMsgHeader(StringBuilder csv, String delimeter) {
		String header = ConfigUtils.getString(QMDLConstant.LAYER3_MSG_CSV_HEADER);
		csv.append(header.replace(Symbol.UNDERSCORE_STRING, delimeter));
		csv.append(QMDLConstant.NEXT_LINE);
	}

	@Override
	public Set<String> getPrefixListFromParamList(Integer workOrderId, List<String> recipeId, List<String> fileIdList) {
		Set<String> prefixList = new HashSet<>();
		if (recipeId.isEmpty() && fileIdList.isEmpty()) {
			prefixList.add(NVLayer3Utils.getRowkeyFromWorkOrderId(workOrderId));
		} else {
			if (fileIdList==null || fileIdList.isEmpty()) {
				for (String receipeId : recipeId) {
					prefixList.add(getRowKeyForLayer3PPE(workOrderId, receipeId));
				}
			} else {
				for (String receipeId : recipeId) {
					for (String fileId : fileIdList) {
						prefixList.add(getRowKeyForLayer3PPEForRecipeAndFile(workOrderId, receipeId, fileId));
					}
				}
			}
		}
		return prefixList;
	}

	@Override
	public Map<String, String> getNeighbourDataRecipeWise(Integer workorderId, List<String> recipeId,List<String> fileId) {
		Map<String, String> neighbourMap = new HashMap<>();
		try {
			String tableName = ConfigUtils.getString(NVLayer3Constants.LAYER3_DRIVE_DETAIL_TABLE);
			Set<String> prefixList = getPrefixListFromParamList(workorderId, recipeId,fileId);
			for (String prefix : prefixList) {
				List<HBaseResult> resultList = nvLayer3HbaseDao.scanNeighbourDataFromHBaseForFramework(tableName,
						prefix);
				for (HBaseResult result : resultList) {
					neighbourMap.put(result.getString(LAT_LNG_TIMESTAMP_COL), result.getString(NEIGHBOUR_DATA_COL));
				}
			}

			logger.info("Getting Neighbour Data Map size is:{}", neighbourMap.size());
		} catch (Exception e) {
			logger.error("Exception in Getting Result from hbase {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
		return neighbourMap;
	}

	public List<String> getPrefixListForNeighbourData(Integer workorderId, List<String> recipeId) {
		List<String> prefixList = new ArrayList<>();
		Set<String> recipeIdSet = new HashSet<>(recipeId);

		for (String receipeId : recipeIdSet) {
			String rowkey = getRowKeyForLayer3PPE(workorderId, receipeId);
			prefixList.add(rowkey);
		}
		return prefixList;
	}

	//// Excel call

	@Override
	public String getDriveDetailForStatisticData(Integer workorderId, List<String> recipeList,List<String> fileIdList, List<String> hbaseColumns,
			String fileName, Boolean isInbuilding, List<String> kpiNeighbourList) throws IOException {
		GenericWorkorder genericworkorder = genericWorkorderDao.findByPk(workorderId);
		Boolean isBenachmarkWO = genericworkorder.getTemplateType().name().equals(TemplateType.NV_BENCHMARK.name())
				|| genericworkorder.getTemplateType().name().equals(TemplateType.NV_IB_BENCHMARK.name());
		logger.info("Is Benachmark WO {}", isBenachmarkWO);
		logger.info("Inside method getDriveDetailForStatisticData for workOrder Id {} , recipes {}, fileIdList {}, hbaseColumns",
				workorderId,recipeList,fileIdList, hbaseColumns);

		if (hbaseColumns != null && !hbaseColumns.isEmpty()) {
			if (isBenachmarkWO && recipeList.size() > ReportConstants.INDEX_ONE) {
				return getoperatorWiseZipFilePath(hbaseColumns, workorderId, recipeList,fileIdList, fileName, isInbuilding,
						kpiNeighbourList);
			} else {
				XSSFWorkbook workbook = createDyanmicXlsReportForoperator(hbaseColumns, workorderId, recipeList,fileIdList,
						isInbuilding, kpiNeighbourList);
				String newFileName = getFileNameForDynamicReport(workorderId, recipeList, fileName,
						recipeList.get(ReportConstants.INDEX_ZER0));

				return writeWorksheetToExcel(workbook, newFileName);
			}

		} else {
			logger.info("hbaseColumns are null {} ", hbaseColumns);
			return null;
		}

	}

	private String getoperatorWiseZipFilePath(List<String> hbaseColumns, Integer workorderId, List<String> recipes,
			List<String> fileIdList, String fileName, Boolean isInbuilding, List<String> kpiNeighbourList) {
		String localDirPath = ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH) + Symbol.SLASH_FORWARD_STRING
				+ new Date().getTime() + Symbol.SLASH_FORWARD_STRING;
		ReportUtil.createDirectory(localDirPath);

		Map<String, List<String>> operatorRecipelistMap = getOperatorWiseRecipeMappingListMapFromWoId(workorderId);

		for (Entry<String, List<String>> entry : operatorRecipelistMap.entrySet()) {

			String newFileName = getFileNameForDynamicReport(workorderId, entry.getValue(), fileName, entry.getKey());

			try {
				XSSFWorkbook workbook = createDyanmicXlsReportForoperator(hbaseColumns, workorderId, entry.getValue(),fileIdList,
						isInbuilding, kpiNeighbourList);
				String absolutefilenamewithpath = localDirPath + Symbol.SLASH_FORWARD_STRING + newFileName;
				ExcelReportUtils.exportExcel(absolutefilenamewithpath, workbook);
			} catch (Exception e) {
				logger.error("Error while getting Json Data: {}" + Utils.getStackTrace(e));

			}
		}

		String zipFilePath = ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH) + Symbol.SLASH_FORWARD_STRING
				+ fileName + ReportConstants.DOT_ZIP;
		logger.info("zipFilePath {}", zipFilePath);
		try {
			ZipUtils.zip(new File(localDirPath).listFiles(), zipFilePath);
			return QMDLConstant.PATH_PREFIX_JSON + fileName + ReportConstants.DOT_ZIP + QMDLConstant.PATH_POSTFIX_JSON;
		} catch (IOException e) {
			logger.info("IOException inside the method getoperatorWiseZipFilePath{}", Utils.getStackTrace(e));
		}
		return ForesightConstants.FAILURE_JSON;
	}

	@Override
	public TreeMap<String, List<String>> getOperatorWiseRecipeMappingListMapFromWoId(Integer woId) {

		List<WORecipeMapping> woRecipeMappingList = woRecipeMappingdao.getWORecipeByGWOId(woId);
		TreeMap<String, List<String>> woRecipeMap = new TreeMap<>();

		for (WORecipeMapping woRecipe : woRecipeMappingList) {
			if (woRecipe.getOperator() != null && woRecipe.getStatus().equals(WORecipeMapping.Status.COMPLETED)) {
				List<String> recipeListTemp = new ArrayList<>();
				if (woRecipeMap.containsKey(woRecipe.getOperator())) {
					recipeListTemp = woRecipeMap.get(woRecipe.getOperator());
				}
				recipeListTemp.add(woRecipe.getId().toString());
				woRecipeMap.put(woRecipe.getOperator(), recipeListTemp);
			}
		}

		return woRecipeMap;
	}

	private String getFileNameForDynamicReport(Integer workorderId, List<String> recipes, String fileName,
			String operator) {
		String newFileName = null;
		if (fileName.isEmpty()) {
			newFileName = EXCELDATA + Symbol.UNDERSCORE_STRING + workorderId + Symbol.UNDERSCORE_STRING
					+ NVLayer3Utils.getStringFromSetValues(recipes) + Symbol.DOT + XLS_EXTENSION;
		} else {
			newFileName = fileName + Symbol.UNDERSCORE_STRING + operator + Symbol.DOT + XLS_EXTENSION;
		}
		return newFileName;
	}

	private XSSFWorkbook createDyanmicXlsReportForoperator(List<String> hbaseColumns, Integer workorderId,
			List<String> recipes, List<String> fileIdList, Boolean isInbuilding, List<String> neighbourIndexList) throws IOException {
		Map<String, String> neighbourDataMap = new HashMap<>();
		Map<String, ArrayList<Double>> kpiMap = new TreeMap<>();
		Map<Integer, List<String>> commonKpiMap = new TreeMap<>();
		Map<String, Layer3StatisticsWrapper> kpiCsvMap = new TreeMap<>();

		logger.info("Getting neighbourIndexList{}", new Gson().toJson(neighbourIndexList));

		Set<String> hbaseColumnsSet = new LinkedHashSet<>(hbaseColumns);

		List<List<String>> driveData = getDriveDetailDataForCsvDump(workorderId, recipes, hbaseColumnsSet,fileIdList);
		SortedMap<Long, List<String>> driveDetailTimestampwiseMap = new TreeMap<>();
		if (driveData != null && !driveData.isEmpty()) {
			for (List<String> driveDataSingle : driveData) {
				if (!StringUtils.isEmpty(driveDataSingle.get(2))) {
					driveDetailTimestampwiseMap.put(Long.valueOf(driveDataSingle.get(2)), driveDataSingle);
				}
			}

			if (neighbourIndexList != null && !neighbourIndexList.isEmpty()) {
				neighbourDataMap = getNeighbourDataRecipeWise(workorderId, recipes,fileIdList);
				putDataIntoKpiMapFromNeighbourData(neighbourIndexList, neighbourDataMap, kpiMap);
			}

			putDataIntoKpiMapDriveDetail(hbaseColumns, driveDetailTimestampwiseMap, kpiMap);

			if (isInbuilding != null && isInbuilding) {
				getCommonKpiMapForInbuilding(hbaseColumns, driveDetailTimestampwiseMap, commonKpiMap,
						neighbourIndexList, neighbourDataMap);

			} else {
				getCommonKpiMapFromDriveDetail(hbaseColumns, driveDetailTimestampwiseMap, commonKpiMap,
						neighbourIndexList, neighbourDataMap);
			}
			getKpiCsvMapFromDriveDetail(kpiMap, kpiCsvMap);

		}

		return createExcelForStatistics(kpiMap, kpiCsvMap, commonKpiMap, hbaseColumns, isInbuilding, neighbourIndexList,
				workorderId, recipes);
	}

	public String[] removeUnwantedStringFromResult(String result) {
		result = result.replace(DRIVE_START_STRING, DRIVE_NULL_STRING);
		result = result.replaceFirst(DRIVE_FIRST_STRING, DRIVE_NULL_STRING);
		result = result.replace(DRIVE_END_STRING, DRIVE_NULL_STRING);
		return result.split(DRIVE_RECORD_SEPERATOR);
	}


	public String removeUnwantedStringFromResultForNeighbourDataDump(String result) {
		result = result.replace(Symbol.BRACKET_OPEN_STRING, DRIVE_NULL_STRING);
		result = result.replace(Symbol.BRACKET_CLOSE_STRING, DRIVE_NULL_STRING);

		return result;
	}


	public String[] removeUnwantedStringFromNeighbourResult(String result) {
		if (result.length() > 4) {
			String temp = result.substring(TWO, result.length() - TWO);
			return temp.split(NEIGHBOUR_RECORD_SEPERATOR);
		}
		return null;
	}

	public void putDataIntoKpiMapFromNeighbourData(List<String> neighbourIndexList,
			Map<String, String> neighbourDataMap, Map<String, ArrayList<Double>> kpiMap) {
		for (String neighbourIn : neighbourIndexList) {
			ArrayList<Double> templist = new ArrayList<>();
			String[] neighbourIndex = neighbourIn.split(Symbol.UNDERSCORE_STRING);

			for (Map.Entry<String, String> entry : neighbourDataMap.entrySet()) {
				try {
					String[] neighbourDataArrayList = removeUnwantedStringFromNeighbourResult(entry.getValue());
					if (neighbourDataArrayList != null && NumberUtils.isDigits(neighbourIndex[NEIGHBOUR_NO_INDEX])
							&& neighbourDataArrayList.length > (getIntegerValue(neighbourIndex[NEIGHBOUR_NO_INDEX]))
							- 1) {
						String[] neighbourData = neighbourDataArrayList[((getIntegerValue(
								neighbourIndex[NEIGHBOUR_NO_INDEX])) - 1)].split(DRIVE_COMMA_SEPRATOR,
										ForesightConstants.MINUS_ONE);

						Double tempValue = NVLayer3Utils.getDoubleFromCsv(neighbourData,
								getIntegerValue(neighbourIndex[NEIGHBOUR_KPI_INDEX]));

						if (tempValue != null && !tempValue.isNaN()) {
							templist.add(tempValue);
						}
					}
				} catch (Exception e) {
					logger.error("Error in Getting getKpiMapFromDriveDetail {}  ", Utils.getStackTrace(e));
				}

			}
			String neighbourName = ("NB" + Symbol.SPACE_STRING
					+ (getIntegerValue(neighbourIndex[ForesightConstants.ZERO])) + Symbol.SPACE_STRING
					+ getNeighbourNameByIndex(getIntegerValue(neighbourIndex[ForesightConstants.ONE])));

			kpiMap.put(neighbourName, templist);

		}
	}

	private Integer getIntegerValue(String value) {
		if (!StringUtils.isEmpty(value)) {
			return Integer.parseInt(value);
		}
		return null;
	}


	public void getKpiCsvMapFromDriveDetail(Map<String, ArrayList<Double>> kpiMap,
			Map<String, Layer3StatisticsWrapper> kpiCsvMap) {
		if (kpiMap != null && !kpiMap.isEmpty()) {
			for (Entry<String, ArrayList<Double>> entry : kpiMap.entrySet()) {

				if (!(entry.getKey().equalsIgnoreCase("latitude") || entry.getKey().equalsIgnoreCase("longitude")
						|| entry.getKey().equalsIgnoreCase(LAT_LNG_TIMESTAMP))) {
					Layer3StatisticsWrapper layer3statisticswrapper = new Layer3StatisticsWrapper();
					List<Double> arrayDouble = entry.getValue();
					Statistics statistics = new Statistics(arrayDouble);
					layer3statisticswrapper.setMean(statistics.getMean());
					layer3statisticswrapper.setMedian(statistics.getMedian());
					layer3statisticswrapper.setMinimum(statistics.getMin());
					layer3statisticswrapper.setMaximum(statistics.getMax());
					layer3statisticswrapper.setCount(statistics.getSize());
					layer3statisticswrapper.setStandardDeviation(statistics.getStdDev());
					layer3statisticswrapper.setVariance(statistics.getVariance());
					layer3statisticswrapper.setKpiName(entry.getKey());
					kpiCsvMap.put(entry.getKey(), layer3statisticswrapper);
				}

			}
		}
	}

	public void getCommonKpiMapFromDriveDetail(List<String> hbaseColumns,
			SortedMap<Long, List<String>> driveDetailTimestampwiseMap, Map<Integer, List<String>> commonKpiMap,
			List<String> neighbourIndexList, Map<String, String> neighbourDataMap) {
		Integer count = ForesightConstants.ONE;
		String lat = "";
		String lng = "";

		for (SortedMap.Entry<Long, List<String>> entry : driveDetailTimestampwiseMap.entrySet()) {
			try {

				String lat2 = entry.getValue().get(ForesightConstants.ZERO);
				String lng2 = entry.getValue().get(ForesightConstants.ONE);

				if (count == ForesightConstants.ONE) {
					lat = entry.getValue().get(ForesightConstants.ZERO);
					lng = entry.getValue().get(ForesightConstants.ONE);
				}
				Double disbwpoint = NVLayer3Utils.getDistanceFromLatLng(getDoubleFromJson(lat), getDoubleFromJson(lng),
						getDoubleFromJson(lat2), getDoubleFromJson(lng2));

				if (count != ForesightConstants.ONE) {
					lat = entry.getValue().get(ForesightConstants.ZERO);
					lng = entry.getValue().get(ForesightConstants.ONE);
				}
				ArrayList<String> templist = new ArrayList<>();
				templist.add(count.toString());
				templist.add(NVLayer3Utils.getDateFromTimestamp(entry.getKey()));
				templist.add(disbwpoint.toString());
				templist.add(lat2);
				templist.add(lng2);
				int innerCount = 0;
				for (String KpiIndex : hbaseColumns) {
					if (innerCount > 2) {
						String temp = entry.getValue().get(innerCount);
						templist.add(getStringValueForExcel(temp));
					}
					innerCount++;
				}
				if (neighbourDataMap != null && !neighbourDataMap.isEmpty() && neighbourIndexList != null
						&& !neighbourIndexList.isEmpty()) {
					filterNeighbourDataAndInsertIntoList(neighbourIndexList, neighbourDataMap,
							entry.getKey().toString(), templist);
				}
				commonKpiMap.put(count, templist);
				count++;

			} catch (Exception e) {
				logger.error("Error in Getting getCommonKpiMapFromDriveDetail {}  ", Utils.getStackTrace(e));
			}

		}
	}

	public void filterNeighbourDataAndInsertIntoList(List<String> neighbourIndexList,
			Map<String, String> neighbourDataMap, String timestamp, List<String> templist) {
		for (String neighbourIn : neighbourIndexList) {
			if (neighbourDataMap.containsKey(timestamp)) {

				String[] neighbourIndex = neighbourIn.split(Symbol.UNDERSCORE_STRING);
				String[] neighbourDataArrayList = removeUnwantedStringFromNeighbourResult(
						neighbourDataMap.get(timestamp));
				if (neighbourDataArrayList != null
						&& neighbourDataArrayList.length > (getIntegerValue(neighbourIndex[NEIGHBOUR_NO_INDEX])) - 1) {
					String[] neighbourData = neighbourDataArrayList[((getIntegerValue(
							neighbourIndex[NEIGHBOUR_NO_INDEX])) - 1)].split(DRIVE_COMMA_SEPRATOR,
									ForesightConstants.MINUS_ONE);

					String value = getStringValueForExcel(
							neighbourData[getIntegerValue(neighbourIndex[NEIGHBOUR_KPI_INDEX])]);

					if (value != null) {
						templist.add(value);

					} else {
						templist.add(Symbol.HYPHEN_STRING);
					}

				} else {
					templist.add(Symbol.HYPHEN_STRING);
				}
			} else {
				templist.add(Symbol.HYPHEN_STRING);
			}
		}
	}

	public void putDataIntoKpiMapDriveDetail(List<String> hbaseColumns,
			SortedMap<Long, List<String>> driveDetailTimestampwiseMap, Map<String, ArrayList<Double>> kpiMap) {
		int indexCount = 0;
		for (String hbaseColumn : hbaseColumns) {
			if ((hbaseColumn.equalsIgnoreCase("latitude") || hbaseColumn.equalsIgnoreCase("longitude")
					|| hbaseColumn.equalsIgnoreCase(LAT_LNG_TIMESTAMP))) {
			} else {
				ArrayList<Double> templist = new ArrayList<>();
				for (SortedMap.Entry<Long, List<String>> entry : driveDetailTimestampwiseMap.entrySet()) {
					try {
						String temp = entry.getValue().get(indexCount);
						if (Utils.hasValidValue(temp) && NumberUtils.isCreatable(temp)
								&& com.inn.commons.lang.NumberUtils.isDigits(temp)) {
							templist.add(Double.valueOf(temp));
						}

					} catch (Exception e) {
						logger.error("Error in Getting getKpiMapFromDriveDetail {}  ", Utils.getStackTrace(e));
					}

				}
				kpiMap.put(hbaseColumn, templist);

			}
			indexCount++;
		}
	}

	public XSSFWorkbook createExcelForStatistics(Map<String, ArrayList<Double>> kpiMap,
			Map<String, Layer3StatisticsWrapper> kpiCsvMap, Map<Integer, List<String>> commonKpiMap,
			List<String> hbaseColumns, Boolean isInbuilding, List<String> neighbourIndexList, Integer workorderId,
			List<String> recipes) {
		logger.info("Started writing excel");

		XSSFWorkbook workbook = new XSSFWorkbook();

		// Series Formatted Data SHEET
		insertDataForSeriesIntoSheet(commonKpiMap, workbook, hbaseColumns, isInbuilding, neighbourIndexList);
		// Statistic Formatted Data SHEET
		insertDataForStatisticsIntoSheet(kpiCsvMap, workbook);
		// Histogram Formatted Data SHEET
		insertDataForHistogramSheet(kpiMap, workbook, hbaseColumns, workorderId, recipes);
		return workbook;
		// Write the output to a file
	}

	public String writeWorksheetToExcel(XSSFWorkbook workbook, String filename) {
		try {
			String destinationFilePath = ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH);
			String absolutefilenamewithpath = destinationFilePath + Symbol.SLASH_FORWARD_STRING + filename;
			ExcelReportUtils.exportExcel(absolutefilenamewithpath, workbook);
			logger.info("Finished writing excel at{}", filename);
			return QMDLConstant.PATH_PREFIX_JSON + filename + QMDLConstant.PATH_POSTFIX_JSON;
		} catch (IOException | RestException e) {
			logger.error("Error in writeWorksheetToExcel {}  ", Utils.getStackTrace(e));

		}
		return ForesightConstants.FAILURE_JSON;
	}

	public void setColumnSizeToAutoForExcelReport(Sheet sheet, int colstrt) {
		for (int i = NVConstant.ZERO_INT; i < colstrt + NVConstant.THIRTY; i++) {
			sheet.autoSizeColumn(i);

		}
	}

	private XSSFWorkbook insertDataForStatisticsIntoSheet(Map<String, Layer3StatisticsWrapper> kpiCsvMap,
			XSSFWorkbook workbook) {
		int colstrt = ReportConstants.INDEX_ZER0;
		Sheet sheet = workbook.createSheet(STATISTIC_FORMATTED_DATA);
		ExcelReportUtils.getCellStyleForNetVelocityReport(workbook, true, false);
		ExcelReportUtils.getCellStyleForNetVelocityReport(workbook, false, false);

		for (Entry<String, Layer3StatisticsWrapper> entry : kpiCsvMap.entrySet()) {
			Layer3StatisticsWrapper dataList = entry.getValue();
			if (dataList != null) {
				Object[] values = {
						// dataList.getKpiName(),
						dataList.getMean(),
						// dataList.getMode(),
						dataList.getMedian(), dataList.getMaximum(), dataList.getMinimum(), dataList.getCount(),
						dataList.getStandardDeviation(), dataList.getVariance() };
				String[] header1 = { STATISTIC };
				String[] header2 = { dataList.getKpiName() };

				workbook = ExcelReportUtils.writeDataColumnWise(sheet, workbook, colstrt, header1, true,
						ForesightConstants.ZERO);
				workbook = ExcelReportUtils.writeDataColumnWise(sheet, workbook, colstrt++, STATISTICS_SHEET_HEADER,
						false, ForesightConstants.ONE);

				workbook = ExcelReportUtils.writeDataColumnWise(sheet, workbook, colstrt, header2, true,
						ForesightConstants.ZERO);
				workbook = ExcelReportUtils.writeDataColumnWise(sheet, workbook, colstrt++, values, false,
						ForesightConstants.ONE);
				colstrt++;
			}

		}

		setColumnSizeToAutoForExcelReport(sheet, colstrt);
		return workbook;
	}

	private XSSFWorkbook insertDataForSeriesIntoSheet(Map<Integer, List<String>> commonKpiMap, XSSFWorkbook workbook,
			List<String> hbaseColumns, Boolean isInbuilding, List<String> neighbourIndexList) {
		int colstrt = ReportConstants.INDEX_ZER0;
		Sheet sheet = workbook.createSheet(SERIES_FORMATTED_DATA);

		CellStyle styleHeader = ExcelReportUtils.getCellStyleForNetVelocityReport(workbook, true, false);
		CellStyle styleNormal = ExcelReportUtils.getCellStyleForNetVelocityReport(workbook, false, false);

		Row headerRow2 = sheet.getRow(colstrt) != null ? sheet.getRow(colstrt) : sheet.createRow(colstrt);
		StringBuilder header2 = new StringBuilder();
		if (isInbuilding != null && isInbuilding) {
			header2.append(SERIES_SHEET_INBUILDING_HEADER);
		} else {
			header2.append(SERIES_SHEET_HEADER);

		}

		int count = 0;
		for (String KpiIndex : hbaseColumns) {
			if (count > 2) {
				header2.append(KpiIndex + Symbol.UNDERSCORE_STRING);
			}
			count++;
		}

		for (String neighbourIn : neighbourIndexList) {
			String[] neighbourIndex = neighbourIn.split(Symbol.UNDERSCORE_STRING);
			header2.append("NB" + Symbol.SPACE_STRING + (getIntegerValue(neighbourIndex[ForesightConstants.ZERO]))
					+ Symbol.SPACE_STRING
					+ getNeighbourNameByIndex(getIntegerValue(neighbourIndex[ForesightConstants.ONE]))
					+ Symbol.UNDERSCORE_STRING);
		}

		String[] headerseries = header2.toString().split(Symbol.UNDERSCORE_STRING);
		ExcelReportUtils.prepareColumnValue(headerRow2, colstrt, styleHeader, headerseries);

		colstrt = ForesightConstants.ZERO;
		int rowNum = ForesightConstants.ONE;
		for (Entry<Integer, List<String>> entry : commonKpiMap.entrySet()) {
			List<String> dataList = entry.getValue();
			Object[] data2 = dataList.toArray();

			Row row = sheet.getRow(rowNum) != null ? sheet.getRow(rowNum) : sheet.createRow(rowNum);
			ExcelReportUtils.prepareColumnValue(row, colstrt, styleNormal, data2);
			rowNum++;

		}
		setColumnSizeToAutoForExcelReport(sheet, colstrt);
		return workbook;
	}

	private XSSFWorkbook insertDataForHistogramSheet(Map<String, ArrayList<Double>> kpiMap, XSSFWorkbook workbook,
			List<String> hbaseColumns, Integer workorderId, List<String> recipes) {

		int colstrt = ForesightConstants.ZERO;
		Sheet sheet = workbook.createSheet(HISTOGRAM_FORMATTED_DATA);
		CellStyle styleHeader = ExcelReportUtils.getCellStyleForNetVelocityReport(workbook, true, false);
		CellStyle styleNormal = ExcelReportUtils.getCellStyleForNetVelocityReport(workbook, false, false);
		Row headerRow2 = sheet.getRow(colstrt) != null ? sheet.getRow(colstrt) : sheet.createRow(colstrt);

		Layer3PPEWrapper layer3ppeWrapper = new Layer3PPEWrapper();
		layer3ppeWrapper.setColumnNameList(hbaseColumns);
		layer3ppeWrapper.setLayerType(Layer3PPEConstant.ADVANCE);

		List<Layer3MetaData> kpiBuilderMetaList = (List<Layer3MetaData>) getKPIBuilderMeta(layer3ppeWrapper);
		Map<String, Layer3MetaData> kpiBuilderMetaMap = new TreeMap<>();

		for (Layer3MetaData kpiBuilderMeta : kpiBuilderMetaList) {
			if (kpiBuilderMeta.getHbaseColumnName() != null) {
				kpiBuilderMetaMap.put(kpiBuilderMeta.getHbaseColumnName(), kpiBuilderMeta);
			}

		}

		if (!kpiMap.isEmpty() && kpiMap.size() > 0 && !kpiBuilderMetaMap.isEmpty()) {

			List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(ReportConstants.SSVT_REPORT);

			for (Entry<String, ArrayList<Double>> entry : kpiMap.entrySet()) {
				try {
					int rowNum = ForesightConstants.ONE;
					Layer3MetaData kpiBuilderMeta = new Layer3MetaData();
					if (kpiBuilderMetaMap.containsKey(entry.getKey())) {
						kpiBuilderMeta = kpiBuilderMetaMap.get(entry.getKey());
					} else {
						kpiBuilderMeta.setKpiName(entry.getKey());
						if (entry.getKey().toLowerCase().contains(QMDLConstant.RSRP)
								|| entry.getKey().toLowerCase().contains(QMDLConstant.RSRQ)
								|| entry.getKey().toLowerCase().contains(QMDLConstant.RSSI)
								|| entry.getKey().toLowerCase().contains(QMDLConstant.SINR)) {
							kpiBuilderMeta.setChartType("kpiRange");
						}
					}

					if (kpiBuilderMeta.getChartType() != null
							&& kpiBuilderMeta.getChartType().equalsIgnoreCase("kpiRange")) {
						String[] headerseries = { RANGE, (kpiBuilderMeta.getKpiName() + Symbol.PARENTHESIS_OPEN + COUNT
								+ Symbol.PARENTHESIS_CLOSE) };
						ExcelReportUtils.prepareColumnValue(headerRow2, colstrt, styleHeader, headerseries);
						getPrintRangeWithCount(legendList, colstrt, sheet, styleNormal, entry, rowNum, kpiBuilderMeta);
						colstrt = colstrt + ForesightConstants.THREE;
					} else {
						String[] headerseries = { VALUE, (kpiBuilderMeta.getKpiName() + Symbol.PARENTHESIS_OPEN + COUNT
								+ Symbol.PARENTHESIS_CLOSE) };
						ExcelReportUtils.prepareColumnValue(headerRow2, colstrt, styleHeader, headerseries);
						getPrintValueswithCount(colstrt, sheet, styleNormal, entry, rowNum);
						colstrt = colstrt + ForesightConstants.THREE;
					}

					setColumnSizeToAutoForExcelReport(sheet, colstrt);

				} catch (Exception e) {
					logger.error("Getting error @insertDataForHistogramSheet method {}", e.getMessage());
				}
			}
		}
		return workbook;
	}

	public void getPrintRangeWithCount(List<LegendWrapper> legendList, int colstrt, Sheet sheet, CellStyle styleNormal,
			Entry<String, ArrayList<Double>> entry, int rowNum, Layer3MetaData kpiBuilderMeta) {

		Map<String, Integer> namevsKpiindexMap = new TreeMap<>();
		if (kpiBuilderMeta.getKpiName() != null) {
			if (kpiBuilderMeta.getKpiName().toLowerCase().contains(QMDLConstant.RSRP)) {
				namevsKpiindexMap.put(QMDLConstant.RSRP.toUpperCase(), 1);

			} else if (kpiBuilderMeta.getKpiName().toLowerCase().contains(QMDLConstant.RSRQ)) {
				namevsKpiindexMap.put(QMDLConstant.RSRQ.toUpperCase(), 1);

			} else if (kpiBuilderMeta.getKpiName().toLowerCase().contains(QMDLConstant.RSSI)) {
				namevsKpiindexMap.put(QMDLConstant.RSSI.toUpperCase(), 1);

			} else if (kpiBuilderMeta.getKpiName().toLowerCase().contains(QMDLConstant.SINR)) {
				namevsKpiindexMap.put(QMDLConstant.SINR.toUpperCase(), 1);

			} else {
				namevsKpiindexMap.put(kpiBuilderMeta.getKpiName(), 1);
			}

		}

		List<KPIWrapper> kpiList = ReportUtil.convertLegendsListToKpiWrapperList(legendList, namevsKpiindexMap);

		if (!kpiList.isEmpty()) {
			List<Double> kpiwisevalues = entry.getValue();
			for (Double value : kpiwisevalues) {
				for (RangeSlab rangeSlab : kpiList.get(0).getRangeSlabs()) {
					if (value > rangeSlab.getLowerLimit() && value < rangeSlab.getUpperLimit()) {

						if (rangeSlab.getCount() != null) {
							rangeSlab.setCount(rangeSlab.getCount() + 1);
						} else {
							rangeSlab.setCount(1);
						}

					}
				}

			}

			for (RangeSlab rangeSlab : kpiList.get(0).getRangeSlabs()) {
				Object[] data2 = { rangeSlab.getLowerLimit() + " to " + rangeSlab.getUpperLimit(),
						rangeSlab.getCount() };

				Row row = sheet.getRow(rowNum) != null ? sheet.getRow(rowNum) : sheet.createRow(rowNum);
				ExcelReportUtils.prepareColumnValue(row, colstrt, styleNormal, data2);
				rowNum++;
			}
		}
	}

	public void getPrintValueswithCount(int colstrt, Sheet sheet, CellStyle styleNormal,
			Entry<String, ArrayList<Double>> entry, int rowNum) {

		Map<Object, Long> rangeMap = entry.getValue().stream()
				.collect(Collectors.groupingBy(e -> e, Collectors.counting()));
		for (Entry<Object, Long> entry2 : rangeMap.entrySet()) {

			Object[] data2 = { entry2.getKey(), entry2.getValue() };

			Row row = sheet.getRow(rowNum) != null ? sheet.getRow(rowNum) : sheet.createRow(rowNum);
			ExcelReportUtils.prepareColumnValue(row, colstrt, styleNormal, data2);
			rowNum++;

		}

	}

	public String getKpiNameByIndex(String hbasecolumnname) {
		if (hbasecolumnname.contains(Symbol.UNDERSCORE_STRING)) {
			String[] neighbourIndex = hbasecolumnname.split(Symbol.UNDERSCORE_STRING);
			return "NB" + Symbol.SPACE_STRING + (getIntegerValue(neighbourIndex[ForesightConstants.ZERO]))
					+ Symbol.SPACE_STRING
					+ getNeighbourNameByIndex(getIntegerValue(neighbourIndex[ForesightConstants.ONE]));

		} else {
			return hbasecolumnname;
		}
	}

	public String[] getHeaderForExcelsReport() {
		String[] header = null;
		String csvHeaders = ConfigUtils.getString(NVConfigUtil.WO_REPORT_HEADERS);
		if (csvHeaders != null) {
			csvHeaders = csvHeaders.replace("Date & Time_", "");
			csvHeaders = csvHeaders.replace("Handover Intra-Success", "Handover Intra-Success_RowKey Prefix");
			header = csvHeaders.split(Symbol.UNDERSCORE_STRING);
		}
		return header;
	}

	public String getNeighbourNameByIndex(Integer kpiIndex) {
		String[] header = NVLayer3Constants.HEADER_NEIGHBOUR_DETAIL_NEW_FRAMEWORK.split(Symbol.UNDERSCORE_STRING);
		return getStringFromCsv(header, kpiIndex);
	}

	public String getNeighbourNameByIndexForMap(String hbasecolumnname) {

		if (hbasecolumnname.contains(Symbol.UNDERSCORE_STRING)) {
			String[] header = NVLayer3Constants.HEADER_NEIGHBOUR_DETAIL_NEW_FRAMEWORK.split(Symbol.UNDERSCORE_STRING);
			return getStringFromCsv(header, Integer.valueOf(hbasecolumnname.split(Symbol.UNDERSCORE_STRING)[1]));
		} else {
			return hbasecolumnname;
		}
	}

	public String getStringValueForExcel(String value) {
		if (value == null) {
			value = "-";
		}
		return value;
	}

	private void getCommonKpiMapForInbuilding(List<String> hbaseColumns,
			SortedMap<Long, List<String>> driveDetailTimestampwiseMap, Map<Integer, List<String>> commonKpiMap,
			List<String> neighbourIndexList, Map<String, String> neighbourDataMap) {
		Integer count = ForesightConstants.ONE;

		for (SortedMap.Entry<Long, List<String>> entry : driveDetailTimestampwiseMap.entrySet()) {

			try {

				ArrayList<String> templist = new ArrayList<>();
				templist.add(count.toString());
				templist.add(NVLayer3Utils.getDateFromTimestamp(entry.getKey()));

				int innerCount = 0;
				for (String KpiIndex : hbaseColumns) {

					if (innerCount > 2) {
						String temp = entry.getValue().get(innerCount);
						templist.add(getStringValueForExcel(temp));
					}
					innerCount++;
				}
				if (neighbourDataMap != null && !neighbourDataMap.isEmpty() && neighbourIndexList != null
						&& !neighbourIndexList.isEmpty()) {
					filterNeighbourDataAndInsertIntoList(neighbourIndexList, neighbourDataMap,
							entry.getKey().toString(), templist);
				}
				commonKpiMap.put(count, templist);
				count++;

			} catch (Exception e) {
				logger.error("Error in Getting getCommonKpiMapFromDriveDetail {}  ", Utils.getStackTrace(e));
			}

		}
	}

	@Override
	//	@Transactional
	public Object getKPIBuilderMeta(Layer3PPEWrapper layer3ppeWrapper) {
		List<Layer3MetaData> kpiBuilderMeta = layer3MetaDataDao.getLayer3MetaData(layer3ppeWrapper);

		if ((layer3ppeWrapper.getWorkorderId() != null)||layer3ppeWrapper.getWorkorderIds() != null) {			
			return getDynamicKPIAndEvents(layer3ppeWrapper, kpiBuilderMeta);
		} 

		else {
			return kpiBuilderMeta;
		}
	}


	@Override
	@Transactional
	public Object getLayer3MetaData(Layer3PPEWrapper layer3MetaData) {
		List<Layer3MetaData> layer3MetaDataList = layer3MetaDataDao.getLayer3MetaData(layer3MetaData);
		return layer3MetaDataList;
	}



	private List<Map<String, Object>> getDynamicKPIAndEvents(Layer3PPEWrapper layer3ppeWrapper,
			List<Layer3MetaData> kpiBuilderMeta) {
		List<Map<String, Object>> dynamicMetaList = new ArrayList<>();
		if (layer3ppeWrapper.getWorkorderId() != null && layer3ppeWrapper.getRecipeId() != null) {
			WORecipeMapping worecipeMapping = woRecipeMappingdao.findByPk(layer3ppeWrapper.getRecipeId());
			Set<String> kpiNameSet = new HashSet<>();
			String kpiNames = worecipeMapping.getKpi();
			if (kpiNames != null) {
				String events = worecipeMapping.getEvent();
				if (events != null) {
					kpiNames = kpiNames + Symbol.COMMA + events;
				}
				if (Layer3PPEConstant.SUMMARY.equalsIgnoreCase(layer3ppeWrapper.getLayerType())) {
					filterSummaryKPIName(kpiBuilderMeta, dynamicMetaList, kpiNames, kpiNameSet);
				}

				else if ((Layer3PPEConstant.ADVANCE.equalsIgnoreCase(layer3ppeWrapper.getLayerType()))) {
					filterAdvanceKPIName(kpiBuilderMeta, dynamicMetaList, kpiNames, kpiNameSet);

				}
			}
			return dynamicMetaList;
		} else if (layer3ppeWrapper.getWorkorderId() != null) {
			logger.info("workorder id is {}", layer3ppeWrapper.getWorkorderId());
			List<WORecipeMapping> woRecipeMappingList = woRecipeMappingdao
					.getWORecipeByGWOId(layer3ppeWrapper.getWorkorderId());
			Set<String> kpiNameSet = new HashSet<>();

			setDynamicKpiMetaList(layer3ppeWrapper, kpiBuilderMeta, dynamicMetaList, woRecipeMappingList, kpiNameSet);
		}
		else if (layer3ppeWrapper.getWorkorderIds() != null) {
			logger.info("workorder id is {}", layer3ppeWrapper.getWorkorderId());
			List<WORecipeMapping> woRecipeMappingList = woRecipeMappingdao
					.getWORecipeByGWOIds(layer3ppeWrapper.getWorkorderIds());
			Set<String> kpiNameSet = new HashSet<>();

			setDynamicKpiMetaList(layer3ppeWrapper, kpiBuilderMeta, dynamicMetaList, woRecipeMappingList, kpiNameSet);
		}
		return dynamicMetaList;
	}

	private void setDynamicKpiMetaList(Layer3PPEWrapper layer3ppeWrapper, List<Layer3MetaData> kpiBuilderMeta,
			List<Map<String, Object>> dynamicMetaList, List<WORecipeMapping> woRecipeMappingList,
			Set<String> kpiNameSet) {
		for (WORecipeMapping worecipeMapping : woRecipeMappingList) {
			String kpiNames = worecipeMapping.getKpi();

			if (kpiNames != null) {
				String events = worecipeMapping.getEvent();
				if (events != null) {
					kpiNames = kpiNames + Symbol.COMMA + events;
				}
				logger.info("KPI list for workorder id {} {}", kpiNames, worecipeMapping.getId());
				if (layer3ppeWrapper.getLayerType().equalsIgnoreCase(Layer3PPEConstant.SUMMARY)) {
					filterSummaryKPIName(kpiBuilderMeta, dynamicMetaList, kpiNames, kpiNameSet);

				} else if (layer3ppeWrapper.getLayerType().equalsIgnoreCase(Layer3PPEConstant.ADVANCE)) {
					filterAdvanceKPIName(kpiBuilderMeta, dynamicMetaList, kpiNames, kpiNameSet);
				}
			}
		}
	}

	private void filterAdvanceKPIName(List<Layer3MetaData> kpiBuilderMeta,
			List<Map<String, Object>> dynamicMetaList, String kpiNames, Set<String> kpiNameSet) {
		List<Layer3MetaData> filterList = kpiBuilderMeta.stream()
				.filter(r -> Arrays.asList(kpiNames.split(ForesightConstants.COMMA)).contains(r.getHbaseColumnName())
						&& r.getIsRequiredOnUi()!=null && r.getIsRequiredOnUi())
				.collect(Collectors.toList());

		for (Layer3MetaData metaWrapper : filterList) {
			if (!kpiNameSet.contains(metaWrapper.getHbaseColumnName())) {
				Map<String, Object> dynamicMetaMap = new HashMap<>();
				dynamicMetaMap.put(Layer3PPEConstant.KPI_NAME, metaWrapper.getKpiName());
				dynamicMetaMap.put(Layer3PPEConstant.CHART_TYPE, metaWrapper.getChartType());
				dynamicMetaMap.put(Layer3PPEConstant.UNIT, metaWrapper.getUnit());
				dynamicMetaMap.put(Layer3PPEConstant.LEGENDID_FK, metaWrapper.getLegendid_fk());
				dynamicMetaMap.put(Layer3PPEConstant.HBASE_COLUMN_NAME, metaWrapper.getHbaseColumnName());
				dynamicMetaMap.put(Layer3PPEConstant.TECH_HIER, metaWrapper.getTechHierarchy());
				if (metaWrapper.getTechHierarchy() != null
						&& !metaWrapper.getTechHierarchy().equalsIgnoreCase(Layer3PPEConstant.COMMON)) {
					dynamicMetaMap
					.put(Layer3PPEConstant.LAYER_NAME,
							(Layer3PPEConstant.N + metaWrapper.getKpiName()
							.replaceAll(ForesightConstants.UNDERSCORE, ForesightConstants.EMPTY)
							+ Layer3PPEConstant.L3).toUpperCase());
				}
				kpiNameSet.add(metaWrapper.getHbaseColumnName());
				dynamicMetaList.add(dynamicMetaMap);
			}
		}

	}

	private void filterSummaryKPIName(List<Layer3MetaData> kpiBuilderMeta,
			List<Map<String, Object>> dynamicMetaList, String kpiNames, Set<String> kpiNameSet) {

		List<Layer3MetaData> filterList = kpiBuilderMeta.stream()
				.filter(r -> Arrays.asList(kpiNames.split(ForesightConstants.COMMA)).contains(r.getHbaseColumnName())
						&& r.getIsRequiredOnUi()!=null && r.getIsRequiredOnUi() && r.getSummaryAggType() != null)
				.collect(Collectors.toList());
		for (Layer3MetaData metaWrapper : filterList) {
			if (!kpiNameSet.contains(metaWrapper.getHbaseColumnName()) && metaWrapper.getSummaryAggType() != null) {
				Map<String, Object> dynamicMetaMap = new HashMap<>();
				dynamicMetaMap.put(Layer3PPEConstant.KPI_NAME, metaWrapper.getKpiName());
				dynamicMetaMap.put(Layer3PPEConstant.HBASE_COLUMN_NAME,
						metaWrapper.getHbaseColumnName() + ForesightConstants.COLON + metaWrapper.getSummaryAggType());
				dynamicMetaMap.put(Layer3PPEConstant.CHART_TYPE, metaWrapper.getChartType());
				if (metaWrapper.getTechHierarchy() != null
						&& !metaWrapper.getTechHierarchy().equalsIgnoreCase(Layer3PPEConstant.COMMON)) {
					dynamicMetaMap
					.put(Layer3PPEConstant.LAYER_NAME,
							(Layer3PPEConstant.N + metaWrapper.getKpiName()
							.replaceAll(ForesightConstants.UNDERSCORE, ForesightConstants.EMPTY)
							+ Layer3PPEConstant.L3).toUpperCase());
				}
				dynamicMetaList.add(dynamicMetaMap);
				kpiNameSet.add(metaWrapper.getHbaseColumnName());
			}

		}
	}


	@Override
	@Transactional
	public String updateLayer3MetaData(Layer3MetaData layer3MetaData) {
		try {
			layer3MetaDataDao.updateLayer3MetaData(layer3MetaData);
			return AppConstants.SUCCESS_JSON;
		} catch (DaoException e) {
			logger.error("Exception while save or update KPI builder meta data {}", ExceptionUtils.getStackTrace(e));
		}
		return AppConstants.FAILURE_JSON;
	}

	@Override
	public Object getPPEDataForMap(List<String> columnsList, Map<String, List<String>> map, String tableName,
			String layerType) {
		Object responseData = null;
		logger.info("Getting parameters for PPE data tableName {} layerType {} ", tableName, layerType);
		String key = null;
		if (map != null && tableName != null && layerType.equalsIgnoreCase(Layer3PPEConstant.SUMMARY)) {
			Map<String, List<String>> response = new HashMap<>();
			Iterator<Entry<String, List<String>>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, List<String>> next = iterator.next();
				key = next.getKey();
				List<String> rowPrefixList = next.getValue();
				List<HBaseResult> ppeDataFromHbase = getPPEDataFromHbase(rowPrefixList, tableName);
				logger.info("response from hbase {}", ppeDataFromHbase.size());
				getSummaryViewDataList(ppeDataFromHbase, columnsList, key, response);

			}
			responseData = response;
		} else if (map != null && tableName != null && layerType.equalsIgnoreCase(Layer3PPEConstant.ADVANCE)) {
			Map<String, List<List<String>>> response = new HashMap<>();
			Iterator<Entry<String, List<String>>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				List<List<String>> dataList = new ArrayList<>();
				Entry<String, List<String>> next = iterator.next();
				key = next.getKey();
				List<String> rowPrefixList = next.getValue();
				List<HBaseResult> ppeDataFromHbase = getPPEDataFromHbase(rowPrefixList, tableName);
				logger.info("response from hbase {}", ppeDataFromHbase.size());
				getAdvanceViewDataList(ppeDataFromHbase, columnsList, dataList);
				response.put(key, dataList);

			}
			responseData = response;
		} else {
			return AppConstants.INVALID_PARAMETER_JSON;
		}

		return responseData;
	}

	private List<List<String>> getAdvanceViewDataList(List<HBaseResult> ppeDataFromHbase, List<String> columnsList,
			List<List<String>> response) {
		for (HBaseResult hbaseRow : ppeDataFromHbase) {
			List<String> result = new ArrayList<>();
			for (String column : columnsList) {
				String value = hbaseRow.getString(column);
				if (value != null) {
					result.add(value);
				} else {
					result.add(ForesightConstants.EMPTY);
				}
			}
			response.add(result);

		}

		return response;

	}

	private void getSummaryViewDataList(List<HBaseResult> ppeDataFromHbase, List<String> columnsList, String key,
			Map<String, List<String>> response) {
		List<String> resultList = new ArrayList<>();
		String aggType = null;
		if (ppeDataFromHbase.size() > ForesightConstants.ZERO) {
			for (String columnName : columnsList) {
				String[] split = columnName.split(ForesightConstants.COLON);
				String column = split[0];
				if (split.length > ForesightConstants.ONE) {
					aggType = split[1];
				}
				if (aggType != null) {
					switch (aggType) {
					case "AVG":
						resultList.add(getAverageValue(ppeDataFromHbase, column));
						break;

					case "SUM":
						resultList.add(getSumValue(ppeDataFromHbase, column));
						break;

					case "DOUBLESUM":
						resultList.add(getDoubleSumValue(ppeDataFromHbase, column));
						break;

					case "LATEST":
						resultList.add(getLatestValue(ppeDataFromHbase, column));
						break;

					case "WAVG":
						resultList.add(getWeightedAvgValue(ppeDataFromHbase, column));
						break;

					case "MAX":
						resultList.add(getMaxValue(ppeDataFromHbase, column));
						break;

					case "MIN":
						resultList.add(getMinValue(ppeDataFromHbase, column));
						break;

					case "COUNT":
						resultList.add(getTotalCount(ppeDataFromHbase, column));
						break;
					default:
						resultList.add(getValueFromHbase(ppeDataFromHbase, column));
						break;

					}
				}
			}
		}
		response.put(key, resultList);
	}

	private String getTotalCount(List<HBaseResult> ppeDataFromHbase, String column) {
		try {
			Long count = ppeDataFromHbase.stream().filter(f -> StringUtils.isNotBlank(f.getString(column))).count();
			return String.valueOf(count);
		} catch (Exception e) {
			// logger.error("Exception while getting count value {}",
			// ExceptionUtils.getStackTrace(e));
		}
		return ForesightConstants.EMPTY;
	}

	private String getValueFromHbase(List<HBaseResult> ppeDataFromHbase, String column) {

		try {
			String value = ppeDataFromHbase.stream()
					.filter(r -> StringUtils.isNotBlank(r.getString(Bytes.toBytes(column)))).map(r -> r.getString(column))
					.findAny().orElse(ForesightConstants.EMPTY);
			return String.valueOf(value);
		} catch (Exception e) {
			// logger.error("Exception while getting value {}",
			// ExceptionUtils.getStackTrace(e));
		}
		return ForesightConstants.EMPTY;
	}

	private String getMinValue(List<HBaseResult> ppeDataFromHbase, String column) {

		try {
			double min = ppeDataFromHbase.stream().filter(r ->  StringUtils.isNotBlank(r.getString(column)))
					.mapToDouble(r -> Double.valueOf(r.getString(column))).min().getAsDouble();
			return String.valueOf(LayerPlotUtils.roundDoubleValue(min));
		} catch (Exception e) {
			// logger.error("Exception while getting minimum value {}",
			// ExceptionUtils.getStackTrace(e));
		}
		return ForesightConstants.EMPTY;
	}

	private String getMaxValue(List<HBaseResult> ppeDataFromHbase, String column) {

		try {
			double max = ppeDataFromHbase.stream().filter(r ->  StringUtils.isNotBlank(r.getString(column)))
					.mapToDouble(r -> Double.valueOf(r.getString(column))).max().getAsDouble();
			return String.valueOf(LayerPlotUtils.roundDoubleValue(max));
		} catch (Exception e) {
			// logger.error("Exception while getting maximum value {}",
			// ExceptionUtils.getStackTrace(e));
		}
		return ForesightConstants.EMPTY;
	}

	private String getWeightedAvgValue(List<HBaseResult> ppeDataFromHbase, String column) {

		try {
			Double kpiAverage = ppeDataFromHbase.stream()
					.filter(x ->  StringUtils.isNotBlank(x.getString(column))
							&&  StringUtils.isNotBlank(x.getString(column.concat(LayerPlotConstants.COUNT_PREFIX))))
					.mapToDouble(v -> v.getStringAsDouble(column)
							* v.getStringAsLong(column.concat(LayerPlotConstants.COUNT_PREFIX)))
					.sum();
			Long kpiCount = ppeDataFromHbase.stream()
					.filter(x ->  StringUtils.isNotBlank(x.getString(column.concat(LayerPlotConstants.COUNT_PREFIX))))
					.mapToLong(v -> v.getStringAsLong(column.concat(LayerPlotConstants.COUNT_PREFIX))).sum();

			if (Utils.hasValue(kpiAverage) && Utils.hasValue(kpiCount) && !Double.isNaN(kpiAverage / kpiCount)) {
				return String.valueOf(LayerPlotUtils.roundDoubleValue(kpiAverage / kpiCount));

			}

		} catch (Exception e) {
			// logger.error("Exception while getting weighted average value {}",
			// ExceptionUtils.getStackTrace(e));
		}
		return ForesightConstants.EMPTY;

	}

	private String getLatestValue(List<HBaseResult> ppeDataFromHbase, String column) {
		String value = ForesightConstants.EMPTY;
		try {
			Long previousTimeStamp = 0L;
			for (HBaseResult singleResultSet : ppeDataFromHbase) {
				Long timestamp = singleResultSet.getTimestamp(Bytes.toBytes(column));
				if (timestamp != null && timestamp > previousTimeStamp) {
					value = singleResultSet.getString(column);
					previousTimeStamp = timestamp;
				}
			}
		} catch (Exception e) {
			// logger.error("Exception while getting latest value {}",
			// ExceptionUtils.getStackTrace(e));
		}
		return value;
	}

	private String getSumValue(List<HBaseResult> ppeDataFromHbase, String column) {
		try {
			Double sum = ppeDataFromHbase.stream().filter(v ->  StringUtils.isNotBlank(v.getString(column)))
					.mapToDouble(v -> v.getStringAsDouble(column)).sum();
			if ((sum != null)) {
				if(sum % 1 == 0){
					return String.valueOf(sum.longValue());
				} else {
					return String.valueOf(sum);
				}
			}
		} catch (Exception e) {
			//			logger.error("Exception while summing value {}", ExceptionUtils.getStackTrace(e));
		}
		return ForesightConstants.EMPTY;
	}

	private String getDoubleSumValue(List<HBaseResult> ppeDataFromHbase, String column) {
		try {
			double sum = ppeDataFromHbase.stream().filter(v ->  StringUtils.isNotBlank(v.getString(column)))
					.mapToDouble(v -> v.getStringAsDouble(column)).sum();
			if (Utils.hasValue(sum)) {
				return String.valueOf(LayerPlotUtils.roundDoubleValue(sum));
			}
		} catch (Exception e) {
			//			logger.error("Exception while summing value {}", ExceptionUtils.getMessage(e));
		}
		return ForesightConstants.EMPTY;
	}


	private String getAverageValue(List<HBaseResult> ppeDataFromHbase, String column) {
		try {
			double avg = ppeDataFromHbase.stream().filter(v ->  StringUtils.isNotBlank(v.getString(column)))
					.mapToDouble(v -> v.getStringAsDouble(column)).average().getAsDouble();
			if (Utils.hasValue(avg)) {
				return String.valueOf(LayerPlotUtils.roundDoubleValue(avg));
			}
		} catch (Exception e) {
			// logger.error("Exception while averaging value {}",
			// ExceptionUtils.getStackTrace(e));
		}
		return ForesightConstants.EMPTY;
	}

	@Override
	public List<HBaseResult> getPPEDataFromHbase(List<String> rowPrefixList, String tableName) {
		List<HBaseResult> response = new ArrayList<>();
		try {
			for (String rowPrefix : rowPrefixList) {
				Scan scan = new Scan();
				scan.setRowPrefixFilter(Bytes.toBytes(rowPrefix));
				List<HBaseResult> scanQMDLDataFromHbase = nvLayer3HbaseDao.scanQMDLDataFromHbase(tableName, scan);
				if(scanQMDLDataFromHbase != null && ! scanQMDLDataFromHbase.isEmpty()) {
					response.addAll(scanQMDLDataFromHbase);
				}
			}
		} catch (IOException e) {
			logger.error("Exception while getting data from hbase {}",ExceptionUtils.getStackTrace(e));
		}

		return response;
	}

	@Override
	public Object getPPEDataFromMicroService(Layer3PPEWrapper wrapper, String tableName) throws HttpException {
		try {
			String wrapperJson = new Gson().toJson(wrapper);
			StringEntity entity = new StringEntity(wrapperJson, ContentType.APPLICATION_JSON);
			String microServiceBaseURL = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue());
			String queryParam = "?tableName=" + tableName;
			String url = microServiceBaseURL + Layer3PPEConstant.PPE_MICRO_URL + queryParam;
			Duration duration = Duration
					.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
			logger.info("url for getPPEDataFromMicroService {}", url);
			return NVLayer3Utils.sendHttpPostRequest(url, entity, true, duration).getString();

		} catch (Exception e) {
			logger.error("Exception in getPPEDataFromMicroService : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(EXCEPTION_SOMETHING_WENT_WRONG).build();
		}

	}

	@Override
	public List<WOFileDetailWrapper> getLayer3FileProcessDetail(String lowerLimit, String upperLimit,
			WOFileDetailWrapper woFileDetailWrapper) {
		List<WOFileDetail> woFileDetailList = iwoFileDetailDao.findWOFileDetailwithCustomFilters(
				Integer.parseInt(lowerLimit), Integer.parseInt(upperLimit), woFileDetailWrapper);
		List<WOFileDetailWrapper> fileWrapperList = new ArrayList<>();
		if (Utils.isValidList(woFileDetailList)) {
			for (WOFileDetail woFileDetail : woFileDetailList) {
				if (checkifValidEntryFileCompletionWise(woFileDetailWrapper, woFileDetail)) {
					continue;
				}
				WOFileDetailWrapper fileDetailWrapper = new WOFileDetailWrapper();
				fileDetailWrapper.setId(woFileDetail.getId());
				fileDetailWrapper.setProcessedStatus(
						woFileDetail.getIsProcessed() != null && woFileDetail.getIsProcessed() ?
								WORecipeMapping.ProcessStatus.COMPLETED :
									woFileDetail.getWoRecipeMapping().getFileProcessStatus());
				fileDetailWrapper.setFileName(woFileDetail.getFileName());
				fileDetailWrapper.setWoName(woFileDetail.getWoRecipeMapping().getGenericWorkorder().getWorkorderName());
				fileDetailWrapper.setRecipeName(woFileDetail.getWoRecipeMapping().getRecipe().getName());
				fileDetailWrapper.setFileSyncTime(woFileDetail.getCreationTime().getTime());
				fileDetailWrapper.setDeleted(woFileDetail.getIsDeleted());
				fileDetailWrapper.setRemark(woFileDetail.getWoRecipeMapping().getRemark());
				Date processStartTime = woFileDetail.getWoRecipeMapping().getProcessStartTime();
				fileDetailWrapper.setProcessStartTime(processStartTime != null ? processStartTime.getTime() : null);
				Date processEndTime = woFileDetail.getWoRecipeMapping().getProcessEndTime();
				fileDetailWrapper.setProcessEndTime(processEndTime != null ? processEndTime.getTime() : null);
				fileDetailWrapper.setFileSize(woFileDetail.getFileSize());
				fileWrapperList.add(fileDetailWrapper);
			}
		}
		return fileWrapperList;
	}

	private boolean checkifValidEntryFileCompletionWise(WOFileDetailWrapper woFileDetailWrapper, WOFileDetail woFileDetail){
		return woFileDetailWrapper != null && Utils.isValidList(woFileDetailWrapper.getProcessedStatusList()) && !woFileDetailWrapper.getProcessedStatusList().contains(
				WORecipeMapping.ProcessStatus.COMPLETED.getValue()) && woFileDetail != null && woFileDetail.getIsProcessed() != null && woFileDetail.getIsProcessed();
	}

	@Override
	public Long getAllLayer3FilesCount(WOFileDetailWrapper wrapper) {
		return iwoFileDetailDao.getAllFilesCount(wrapper);
	}

	@Override
	@Transactional
	public Object deleteLayer3MetaData(Layer3MetaData layer3MetaData) {
		try {
			layer3MetaDataDao.deleteLayer3MetaData(layer3MetaData);
			return AppConstants.SUCCESS_JSON;
		} catch (DaoException e) {
			logger.error("Exception while save or deleteing KPI builder meta data {}", ExceptionUtils.getStackTrace(e));
		}
		return AppConstants.FAILURE_JSON;
	}


}
