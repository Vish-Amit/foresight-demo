package com.inn.foresight.module.nv.report.service.Inbuilding.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping.Status;

public class InbuildingReportUtil implements ReportConstants {
	
	private static Logger logger = LogManager.getLogger(InbuildingReportUtil.class);

	public static Map<String, Double> getFiveBandwidthCriteriaMap() {
		Map<String, Double> map = new HashMap<>();
		map.put(DL_THROUGHPUT, 20.0);
		map.put(UL_THROUGHPUT, 7.0);
		map.put(LATENCY, 50.0);
		map.put(JITTER, 40.0);
		return map;
	}

	public static Map<String, Double> getTwentyBandwidthCriteriaMap() {
		Map<String, Double> map = new HashMap<>();
		map.put(DL_THROUGHPUT, 80.0);
		map.put(UL_THROUGHPUT, 25.0);
		map.put(LATENCY, 50.0);
		map.put(JITTER, 40.0);
		return map;
	}

	public static Double getAverageOfKpi(List<String[]> drivedata, int index) {
		OptionalDouble avgValue = null;
		Double average = null;
		List<String> datalist = drivedata.stream().filter(x -> (x[index] != null && !x[index].isEmpty()))
				.map(x -> x[index]).collect(Collectors.toList());
		if (Utils.isValidList(datalist)) {
			avgValue = datalist.stream().mapToDouble(i -> Double.parseDouble(i)).average();
		}
		if (avgValue!=null && avgValue.isPresent()) {
			return avgValue.getAsDouble();
		}
		return average;
	}

	public static Double PeakValueOfKpi(List<String[]> drivedata, int index) {
		OptionalDouble MaxValue = null;
		Double max = null;
		List<String> datalist = drivedata.stream().filter(x -> (x[index] != null && !x[index].isEmpty()))
				.map(x -> x[index]).collect(Collectors.toList());
		if (Utils.isValidList(datalist)) {
			MaxValue = datalist.stream().mapToDouble(i -> Double.parseDouble(i)).max();
		}
		if (MaxValue!=null && MaxValue.isPresent()) {
			return MaxValue.getAsDouble();
		}
		return max;
	}

	public static String getDistinctKpiValues(List<String[]> driveData,Integer index) {
		String result=null;
		Set<String> dataSet = new HashSet<>();
		dataSet = driveData.stream()
					.filter(x -> (x[index] != null && !x[index].isEmpty()))
					.map(x -> x[index]).collect(Collectors.toSet());
		if (dataSet != null && !dataSet.isEmpty()) {
			result=NVLayer3Utils.getStringFromSetValues(dataSet).replace(Symbol.UNDERSCORE, Symbol.HYPHEN);
		}
		return result;
	}
	
	
	public static Double getAverageOfMinimumFiveKpiValues(List<String[]> drivedata, int index) {
		OptionalDouble avgValue = null;
		Double average = null;
		List<String> datalist = drivedata.stream().filter(x -> (x[index] != null && !x[index].isEmpty()))
				.map(x -> x[index]).collect(Collectors.toList());
		if (Utils.isValidList(datalist)) {
			avgValue = datalist.stream().mapToDouble(x -> Double.parseDouble(x)).sorted().limit(5).average();
		}
		if (avgValue !=null &&avgValue.isPresent()) {
			return avgValue.getAsDouble();
		}
		return average;
	}
	
	public static String addPassFailStatusToResult(Double result, Double theresholdvalue, String check,
			List<String> statusList) {
		String rate = "";
		if (result != null) {
			if (check.equalsIgnoreCase(ReportConstants.GREATER_THAN)) {
				if (result > theresholdvalue) {
					rate = "PASS"+"[ "+result+" ]";
					return rate;
				} else {
					statusList.add("FAIL");
					rate = "FAIL"+"[ "+result+" ]";
					return rate;
				}
			}
			if (check.equalsIgnoreCase(ReportConstants.LESS_THAN_SYMBOL)) {
				if (result < theresholdvalue) {
					rate = "PASS"+"[ "+result+" ]";
					return rate;
				} else {
					rate = "FAIL"+"[ "+result+" ]";
					statusList.add("FAIL");
					return rate;
				}
			}
		}
		return rate;
	}
	
	public static String getFileNameFromFilePath(String filePath) {
		if (filePath != null && (filePath.contains(ReportConstants.PDF_EXTENSION) || filePath.contains(
				ReportConstants.PPTX_EXTENSION))) {
			try {
				String fileName = filePath.substring(filePath.lastIndexOf(ReportConstants.FORWARD_SLASH) + 1,
						filePath.length());
				return fileName;
			} catch (Exception e) {
			}
		}
		return null;
	}
	
	public static <K, V> boolean validateMap(Map<K, V> dataMap, String key) {
		if (key != null) {
			return dataMap != null && !dataMap.isEmpty() && dataMap.containsKey(key) && dataMap.get(key) != null;
		} else {
			return dataMap != null && !dataMap.isEmpty();
		}
	}
	
	public static HttpResponse sendPostRequestWithoutTimeOut(String uri, StringEntity httpEntity) throws IOException {
		;
		HttpPost httpPost = new HttpPost(uri);
		logger.info("inside method sendPostRequestWithoutTimeOut with url : {} , stringentity :{}  ::::  {}", uri,httpEntity.getContent(),httpEntity.toString());
		try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
			httpPost.setEntity(httpEntity);
			httpPost.addHeader(ReportConstants.HEADER_X_API_KEY, ConfigUtils.getString(ReportConstants.NV_SSVT_SF_INTEGRATION_X_API_KEY));
			CloseableHttpResponse response = httpClient.execute(httpPost);
            
			int statusCode = response.getStatusLine().getStatusCode();
			logger.info("statusCode {} ", statusCode);
			if (statusCode != HttpStatus.SC_OK) {
				throw new RestException("Failed with HTTP error code : " + statusCode);
			}
			return response;
		} catch (Exception e) {
			logger.info("exception inside method sendPostRequestWithoutTimeOut :: {}", Utils.getStackTrace(e));
			throw new RestException(ReportConstants.EXCEPTION_ON_CONNECTION);
		}
	}
	
	public static String getBandFromMap(GenericWorkorder genericWorkorder) {
		String band= null;
		try {
			if (validateMap(genericWorkorder.getGwoMeta(), ReportConstants.KEY_BAND)) {
				 band = genericWorkorder.getGwoMeta().get(ReportConstants.KEY_BAND);
				 logger.info("band from gwometa {}", band);
				}
		} catch (Exception e) {
			logger.error("JsonProcessingException inside the method createReport {}", Utils.getStackTrace(e));
		}		
		return band;
	}
	
	public static List<WORecipeMapping> getFilteredRecipeMappingsIncredientsWise(
			List<WORecipeMapping> mobilityfilteredMappings, String... incredientName) {
		List<WORecipeMapping> filteredMappings = new ArrayList<>();
		try {
			if (Utils.isValidList(mobilityfilteredMappings)) {
				for (WORecipeMapping mapping : mobilityfilteredMappings) {
					if (Status.COMPLETED.equals(mapping.getStatus())) {
						JSONArray jsonArray = new JSONArray(mapping.getRecipe().getScriptJson());
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject object = (JSONObject) jsonArray.get(i);
							for (String incredient : incredientName) {
								if (object.has(ReportConstants.NAME)
										&& object.get(ReportConstants.NAME).toString().equalsIgnoreCase(incredient)) {
									filteredMappings.add(mapping);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.info("exception inside method getFilteredRecipeMappingsIncredientsWise : {} ",
					Utils.getStackTrace(e));
		}
		logger.info("size of mobility downloadmappings {}", filteredMappings.size());
		return filteredMappings;
	}
}
