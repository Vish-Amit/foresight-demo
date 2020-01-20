package com.inn.foresight.module.nv.layer3.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.hadoop.hbase.client.Get;

import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.http.HttpException;
import com.inn.foresight.module.nv.layer3.model.Layer3MetaData;
import com.inn.foresight.module.nv.layer3.wrapper.Layer3PPEWrapper;
import com.inn.foresight.module.nv.workorder.wrapper.WOFileDetailWrapper;

public interface ILayer3PPEService {

	String getSignalMessageRecipeWise(Integer workorderId, String lastRowKey, String direction, List<String> recipeId,
			String message, List<String> fileId);

	List<Get> getListForSummary(Integer workorderId, List<String> recipeId, List<String> columnList);

	String getSignalMessageDetail(String rowKey, String msgType);

	String createSignalMessageRecipeWise(Integer workOrderId, List<String> recipeId, List<String> fileId,
			String woName);

	String getDriveDetailForStatisticData(Integer workorderId, List<String> recipes, List<String> fileId,
			List<String> hbaseColumns, String fileName, Boolean isInbuilding, List<String> kpiNeighbourList)
					throws IOException;

	Object getPPEDataForMap(List<String> columnsList, Map<String, List<String>> map, String tableName,
			String layerType);

	Object getPPEDataFromMicroService(Layer3PPEWrapper wrapper, String tableName) throws HttpException;

	String getSignalMessagesForBin(String rowPrefix);

	TreeMap<String, List<String>> getOperatorWiseRecipeMappingListMapFromWoId(Integer woId);

	Object getKPIBuilderMeta(Layer3PPEWrapper layer3ppeWrapper);

	String processWOReportDump(Integer workorderId, List<String> recipeIdList, List<String> fileId);

	Response getSignalMessageDetailMicroServiceUrl(String rowKey, String msgType, HttpServletRequest request);

	Response processWOReportDumpMicroServiceUrl(Integer workorderId, Map<String, List<String>> map,
			HttpServletRequest request);

	Response getWOReportDumpMicroServiceUrl(HttpServletRequest request);

	Response getSignalMessageRecipeWiseMicroServiceUrl(Integer workorderId, Map<String, List<String>> map,
			String lastRowKey, String direction, HttpServletRequest request);

	Response getSignalMessagesForBinMicroServiceUrl(String rowPrefix, HttpServletRequest request);

	Response getDriveDetailForStatisticDataXlsMicroServiceUrl(Integer workorderId, Map<String, List<String>> map,
			HttpServletRequest request);

	Response createSignalMessageRecipeWiseCsvMicroServiceUrl(Integer workorderId, Map<String, List<String>> map,
			HttpServletRequest request);

	void getListofAllHbaseColumnsForLayer3Framework(Set<String> columnsList, Set<String> csvHeaders,
			Boolean isStealthWO);

	List<WOFileDetailWrapper> getLayer3FileProcessDetail(String lowerLimit, String upperLimit,
			WOFileDetailWrapper woFileDetailWrapper);

	Long getAllLayer3FilesCount(WOFileDetailWrapper wrapper);

	String updateLayer3MetaData(Layer3MetaData layer3MetaData);

	Object deleteLayer3MetaData(Layer3MetaData layer3MetaData);

	Object getLayer3MetaData(Layer3PPEWrapper layer3MetaData);

	Set<String> getPrefixListFromParamList(Integer workOrderId, List<String> recipeId, List<String> fileIdList);

	Map<String, String> getNeighbourDataRecipeWise(Integer workorderId, List<String> recipeId, List<String> fileId);

	List<HBaseResult> getPPEDataFromHbase(List<String> rowPrefixList, String tableName);

}
