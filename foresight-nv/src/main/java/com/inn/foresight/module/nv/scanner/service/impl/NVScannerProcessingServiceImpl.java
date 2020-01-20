package com.inn.foresight.module.nv.scanner.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.http.HttpResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.http.HttpException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.report.utils.ReportUtils;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HbaseDao;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.scanner.constant.NVScannerConstant;
import com.inn.foresight.module.nv.scanner.service.NVScannerProcessingService;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.recipe.dao.IRecipeDao;
import com.inn.foresight.module.nv.workorder.service.IWOFileDetailService;

@Service("NVScannerProcessingServiceImpl")
public class NVScannerProcessingServiceImpl implements NVScannerProcessingService {

	private static Logger logger = LogManager.getLogger(NVScannerProcessingServiceImpl.class);

	@Autowired
	IWOFileDetailService woFileDetailService;
	
	@Autowired
	INVLayer3HbaseDao nvLayer3HbaseDao;
	
	@Autowired
	IWORecipeMappingDao woRecipeMappingDao;
	
	@Autowired
	IRecipeDao recipeDao;
	
	@Override
	public Response getCSVDumpFromMicroService(Integer woId, Integer recipeId) throws HttpException, IOException {
		String microServiceBaseURL = ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue());
		String queryParam = "?workorderId=" + woId + "&recipeId=" + recipeId;
		String url = microServiceBaseURL + NVScannerConstant.SCANNER_CSV_DUMP_MICRO_URL + queryParam;

		logger.info("url for scanner dump download {}",url);
		HttpResponse response = ReportUtils.sendGetRequestWithoutTimeOut(url);

		Response.ResponseBuilder builder = Response.status(200);
		builder = builder.entity(response.getEntity().getContent())
				.header(ReportConstants.CONTENT_TYPE, response.getEntity().getContentType())
				.header(ReportConstants.CONTENT_DISPOSITION,
						response.getFirstHeader(ReportConstants.CONTENT_DISPOSITION).getValue());
		
		return builder.build();
	}

	public byte[] getScannerDumpDataFromHbase(Integer workorderId, Integer recipeId) {
		try {
		String recipeType = getRecipeType(recipeId);
		List<HBaseResult> scannerDataFromHbase = getScannerDataFromHbase(workorderId, recipeId);
		
		logger.info("Size of hbase data is {}",scannerDataFromHbase.size());
		 List<HBaseResult> sortedList = scannerDataFromHbase.stream().filter(f -> f.getString(NVScannerConstant.TIME_STAMP) != null)
					.sorted(Comparator.comparingLong(r -> Long.parseLong(r.getString(NVScannerConstant.TIME_STAMP))))
					.collect(Collectors.toList());
		if(recipeType != null && (recipeType.equalsIgnoreCase(NVScannerConstant.RECIPE_SIGNAL) || recipeType.equalsIgnoreCase(NVScannerConstant.RECIPE_RSSI))) {
			return  createDumpForSignalRecipe(sortedList,recipeType).getBytes();
		}
		else if(recipeType != null && recipeType.equalsIgnoreCase(NVScannerConstant.RECIPE_EPS)){
			return createDumpForEPSRecipe(sortedList).getBytes();
		}
		
		}
		catch(Exception e) {
			logger.error("Exception while create csv dump from hbase {}",ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	private String createDumpForEPSRecipe(List<HBaseResult> scannerDataFromHbase) {
		StringBuilder csvData = new StringBuilder();
		String baseHeaders = ConfigUtils.getString(NVConfigUtil.SCANNER_EPS_CSV_HEADER);
		baseHeaders = baseHeaders.replaceAll(ForesightConstants.SF_DATA_SEPARATOR, ForesightConstants.COMMA);
		csvData.append(baseHeaders);
		csvData.append(ForesightConstants.NEW_LINE);
		for(HBaseResult result :scannerDataFromHbase) {
		String timeStamp = result.getString(NVScannerConstant.TIME_STAMP) ;
		String latitude = result.getString(NVScannerConstant.LATITUDE);
		String longitude = result.getString(NVScannerConstant.LONGITUDE);
		String freq = result.getString(NVScannerConstant.FREQ);
		String rssi = result.getString(NVScannerConstant.RSSI);
		
		csvData.append(timeStamp != null ? NVLayer3Utils.getDateFromTimestamp(timeStamp) :ForesightConstants.EMPTY);
		csvData.append(ForesightConstants.COMMA);
		csvData.append(latitude != null ? latitude :ForesightConstants.EMPTY);
		csvData.append(ForesightConstants.COMMA);
		csvData.append(longitude != null ? longitude :ForesightConstants.EMPTY);
		csvData.append(ForesightConstants.COMMA);
		csvData.append(freq != null ? freq :ForesightConstants.EMPTY);
		csvData.append(ForesightConstants.COMMA);
		csvData.append(rssi != null ? rssi :ForesightConstants.EMPTY);
		csvData.append(ForesightConstants.NEW_LINE);
		}
		return csvData.toString();
		
	}

	private String createDumpForSignalRecipe(List<HBaseResult> scannerDataFromHbase, String recipeType) {
		StringBuilder csvData = new StringBuilder();
		
		String baseHeaders = ConfigUtils.getString(NVConfigUtil.SCANNER_CSV_HEADER);
		baseHeaders = baseHeaders.replaceAll(ForesightConstants.SF_DATA_SEPARATOR, ForesightConstants.COMMA);
		int dynamicColumnSize = ForesightConstants.ZERO;
		for (HBaseResult result : scannerDataFromHbase) {
		String rsrp = result.getString(NVScannerConstant.RSRP);
		int size = rsrp != null ? rsrp.replaceAll(NVScannerConstant.OPEN_BRACKTS, ForesightConstants.EMPTY).replaceAll(NVScannerConstant.CLOSE_BRACKETS, ForesightConstants.EMPTY).split(ForesightConstants.COMMA).length : ForesightConstants.ZERO;
			if(size >= dynamicColumnSize) {
				dynamicColumnSize = size;
			}
			setSignalRecipeDataIntoBuilder(csvData, result,recipeType,dynamicColumnSize);
			} 
		StringBuilder headers = new StringBuilder();
		headers.append(baseHeaders);
		logger.info("Size of rsrp range is {}",dynamicColumnSize);
		if (recipeType != null && recipeType.equalsIgnoreCase(NVScannerConstant.RECIPE_SIGNAL)) {
			for (int index = 1; index <= dynamicColumnSize; index++) {
				if (index == 1) {
					headers.append(ForesightConstants.COMMA);
				}
				headers.append(NVScannerConstant.FA1_REF_SIGNAL_WIDEBAND_TOP + index + " PSCH RSSI (dBm)");
				headers.append(ForesightConstants.COMMA);
				headers.append(NVScannerConstant.FA1_REF_SIGNAL_WIDEBAND_TOP + index + " SSCH RSSI (dBm)");
				headers.append(ForesightConstants.COMMA);
				headers.append(NVScannerConstant.FA1_REF_SIGNAL_WIDEBAND_TOP + index + " PCI");
				headers.append(ForesightConstants.COMMA);
				headers.append(NVScannerConstant.FA1_REF_SIGNAL_WIDEBAND_TOP + index + " RS_RP (dBm)");
				headers.append(ForesightConstants.COMMA);
				headers.append(NVScannerConstant.FA1_REF_SIGNAL_WIDEBAND_TOP + index + " RS_RQ (dB)");
				headers.append(ForesightConstants.COMMA);
				headers.append(NVScannerConstant.FA1_REF_SIGNAL_WIDEBAND_TOP + index + " RS_CINR (dB)");
				if (index != dynamicColumnSize) {
					headers.append(ForesightConstants.COMMA);
				}
			} 
		}
		else {
			headers.append(ForesightConstants.COMMA);
			headers.append("AVG RSSI");
		}
		csvData.insert(ForesightConstants.ZERO, headers);
		csvData.insert(headers.length() ,ForesightConstants.NEW_LINE);
		return csvData.toString();
		
	}

	private String getRecipeType(Integer recipeId) {
		WORecipeMapping woRecipeMapping = woRecipeMappingDao.findByPk(recipeId);
		String name = woRecipeMapping.getRecipe().getName();
		String recipeType = null;
		if (name.toUpperCase().contains("EPS")) {
			recipeType = NVScannerConstant.RECIPE_EPS;

		} else if (name.toUpperCase().contains("SIGNAL")) {

			recipeType = NVScannerConstant.RECIPE_SIGNAL;

		} else if (name.toUpperCase().contains("RSSI")) {

			recipeType = NVScannerConstant.RECIPE_RSSI;

		}
		return recipeType;
	}

	private String setSignalRecipeDataIntoBuilder(StringBuilder csvData, HBaseResult result,String recipeType, int dynamicColumnSize) {
		String timeStamp = result.getString(NVScannerConstant.TIME_STAMP) ;
		String latitude = result.getString(NVScannerConstant.LATITUDE);
		String longitude = result.getString(NVScannerConstant.LONGITUDE);
		String channel = result.getString(NVScannerConstant.CHANNEL);
		String status = result.getString(NVScannerConstant.STATUS);
		String pschRssi = result.getString(NVScannerConstant.PSCH_RSSI);
		String sschRssi = result.getString(NVScannerConstant.SSCH_RSSI);
		
		String pci = result.getString(NVScannerConstant.PCI);
		String rsrp = result.getString(NVScannerConstant.RSRP);
		String rsrq = result.getString(NVScannerConstant.RSRQ);
		String sinr = result.getString(NVScannerConstant.SINR);
		String rssi = result.getString(NVScannerConstant.RSSI);

		csvData.append(timeStamp != null ? NVLayer3Utils.getDateFromTimestamp(timeStamp) :ForesightConstants.EMPTY);
		csvData.append(ForesightConstants.COMMA);
		csvData.append(latitude != null ? latitude :ForesightConstants.EMPTY);
		csvData.append(ForesightConstants.COMMA);
		csvData.append(longitude != null ? longitude :ForesightConstants.EMPTY);
		csvData.append(ForesightConstants.COMMA);
		csvData.append(channel != null ? channel :ForesightConstants.EMPTY);
		csvData.append(ForesightConstants.COMMA);
		csvData.append(status != null ? status :ForesightConstants.EMPTY);
				
		if (recipeType != null && recipeType.equalsIgnoreCase(NVScannerConstant.RECIPE_SIGNAL)) {
			csvData.append(ForesightConstants.COMMA);
			csvData.append(pschRssi != null ? pschRssi : ForesightConstants.EMPTY);
			csvData.append(ForesightConstants.COMMA);
			csvData.append(sschRssi != null ? sschRssi : ForesightConstants.EMPTY);
			csvData.append(ForesightConstants.COMMA);
			
			String[] pciList = pci != null ? pci.replaceAll(NVScannerConstant.OPEN_BRACKTS, ForesightConstants.EMPTY).replaceAll(NVScannerConstant.CLOSE_BRACKETS, ForesightConstants.EMPTY) .split(ForesightConstants.COMMA) : null;

			String[] rsrpList = rsrp != null ? rsrp.replaceAll(NVScannerConstant.OPEN_BRACKTS, ForesightConstants.EMPTY).replaceAll(NVScannerConstant.CLOSE_BRACKETS, ForesightConstants.EMPTY) .split(ForesightConstants.COMMA) : null;

			String[] rsrqList = rsrq != null ? rsrq.replaceAll(NVScannerConstant.OPEN_BRACKTS, ForesightConstants.EMPTY).replaceAll(NVScannerConstant.CLOSE_BRACKETS, ForesightConstants.EMPTY) .split(ForesightConstants.COMMA) : null;

			String[] sinrList = sinr != null ? sinr.replaceAll(NVScannerConstant.OPEN_BRACKTS, ForesightConstants.EMPTY).replaceAll(NVScannerConstant.CLOSE_BRACKETS, ForesightConstants.EMPTY) .split(ForesightConstants.COMMA) : null;

			for(int rsrpMaxIndex = 0 ; rsrpMaxIndex < dynamicColumnSize ; rsrpMaxIndex++)
			{
				try {
					csvData.append(pciList != null ? pciList[rsrpMaxIndex] : ForesightConstants.EMPTY);
				} catch (ArrayIndexOutOfBoundsException e) {
					csvData.append(ForesightConstants.EMPTY);
				}
				csvData.append(ForesightConstants.COMMA);
				try {
					csvData.append(rsrpList != null ? rsrpList[rsrpMaxIndex] : ForesightConstants.EMPTY);
				} catch (ArrayIndexOutOfBoundsException e) {
					csvData.append(ForesightConstants.EMPTY);
				}
				csvData.append(ForesightConstants.COMMA);
				try {
					csvData.append(rsrqList != null ? rsrqList[rsrpMaxIndex] :  ForesightConstants.EMPTY);
				} catch (ArrayIndexOutOfBoundsException e) {
					csvData.append(ForesightConstants.EMPTY);
				}
				csvData.append(ForesightConstants.COMMA);
				try {
					csvData.append(sinrList != null ? sinrList[rsrpMaxIndex] : ForesightConstants.EMPTY);
				} catch (ArrayIndexOutOfBoundsException e) {
					csvData.append(ForesightConstants.EMPTY);
				}
				csvData.append(ForesightConstants.COMMA);
			
			}
			csvData.append(ForesightConstants.NEW_LINE);
		}
		else if (recipeType != null && recipeType.equalsIgnoreCase(NVScannerConstant.RECIPE_RSSI)) {
			csvData.append(ForesightConstants.COMMA);
			csvData.append(rssi != null ? rssi.replaceAll(NVScannerConstant.OPEN_BRACKTS, ForesightConstants.EMPTY)
					.replaceAll(NVScannerConstant.CLOSE_BRACKETS, ForesightConstants.EMPTY) : ForesightConstants.EMPTY);
			csvData.append(ForesightConstants.NEW_LINE);
		}
		return recipeType;
	}

	private List<HBaseResult> getScannerDataFromHbase(Integer workorderId, Integer recipeId) {
		List<String> rowPrefixList = getScannerRowPrefixList(workorderId, recipeId);
		String tableName = ConfigUtils.getString(NVLayer3Constants.LAYER3_DRIVE_DETAIL_TABLE);
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

	private List<String> getScannerRowPrefixList(Integer workorderId, Integer recipeId) {
		List<String> rowPrefixList = new ArrayList<>();
		String rowKeyPrefix=  NVLayer3Utils.getRowKeyForLayer3PPE(workorderId, recipeId.toString());
		rowPrefixList.add(rowKeyPrefix);
		return rowPrefixList;
	}
}
