package com.inn.foresight.module.nv.layer3.service.parse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.hadoop.hbase.client.Put;

import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NVL3CsvDataWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.QMDLLogCodeWrapper;

/** The Interface INVLayer3CsvParseService. */
public interface INVL3CsvProcessService {

	void processCsvFile(ZipFile zipFile,ZipEntry entry,TreeMap<Long, QMDLLogCodeWrapper> woParsedMap, Map<String, TreeMap<Long, Object>> kpiValuesMap, Layer3SummaryWrapper aggrigateWrapperData, Map<String, Put> imagePutList, List<String> processedFiles, String localFilePath) throws IOException;

	int getStringTypeByNetworkAndTest(String testType, String networkType);

	void processCsvDataForLayer3(List<NVL3CsvDataWrapper> csvDataHolderList,
			TreeMap<Long, QMDLLogCodeWrapper> woParsedMap, Map<String, TreeMap<Long, Object>> kpiValuesMap, Layer3SummaryWrapper aggrigateWrapperData);

	void processDataForStats(List<NVL3CsvDataWrapper> csvDataHolderList, Map<String, TreeMap<Long, Object>> kpiMap);

}
