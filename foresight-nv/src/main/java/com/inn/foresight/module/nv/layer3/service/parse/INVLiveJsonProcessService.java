package com.inn.foresight.module.nv.layer3.service.parse;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.json.simple.parser.ParseException;

import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.QMDLLogCodeWrapper;

public interface INVLiveJsonProcessService {

	void processJsonFile(ZipFile zipFile, ZipEntry entry, TreeMap<Long, QMDLLogCodeWrapper> woParsedMap,
			Map<String, TreeMap<Long, Object>> kpiValuesMap, Layer3SummaryWrapper aggrigateWrapperData, Boolean isQmdl)
			throws IOException, ParseException;

}
