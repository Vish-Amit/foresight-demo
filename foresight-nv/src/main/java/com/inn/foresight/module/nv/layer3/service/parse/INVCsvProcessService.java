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

public interface INVCsvProcessService {

	void processNonQMDLDataFromCsv(ZipFile zipFile,ZipEntry entry,TreeMap<Long, QMDLLogCodeWrapper> woParsedMap, Map<String, TreeMap<Long, Object>> kpiValuesMap, Layer3SummaryWrapper aggrigateWrapperData, Map<String, Put> imagePutList, List<String> processedFiles, String localFilePath) throws IOException;

	void processDataForStats(List<NVL3CsvDataWrapper> csvDataHolderList, Map<String, TreeMap<Long, Object>> kpiMap);

	void processCsvDataForLayer3(List<NVL3CsvDataWrapper> csvDataHolderList,
			TreeMap<Long, QMDLLogCodeWrapper> woParsedMap, Map<String, TreeMap<Long, Object>> kpiValuesMap,
			Layer3SummaryWrapper aggrigateWrapperData);

	
	
	List<NVL3CsvDataWrapper> processCsvFileFromZip(ZipFile zipFile, ZipEntry entry,
			Layer3SummaryWrapper aggrigateWrapperData, Map<String, Put> imagePutMap, List<String> processedFiles,
			String localFilePath) throws IOException;

}
