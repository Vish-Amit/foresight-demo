package com.inn.foresight.module.nv.layer3.service.parse;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.hadoop.hbase.client.Put;

import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.QMDLLogCodeWrapper;

public interface INVL3BuildingJsonParserService {

	void processInBuildingFileData(ZipFile zipFile, ZipEntry fileEntry, TreeMap<Long, QMDLLogCodeWrapper> woParsedMap,
			Boolean isQMDL, Map<String, Put> imagePutMap, Layer3SummaryWrapper aggrigateWrapperData, Map<String, TreeMap<Long, Object>> kpiValuesMap, List<String> processedFiles, String localFilePath) throws Exception;

}
