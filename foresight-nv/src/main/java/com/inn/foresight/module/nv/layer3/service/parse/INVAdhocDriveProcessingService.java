package com.inn.foresight.module.nv.layer3.service.parse;

import java.io.IOException;
import java.util.List;

import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NVL3CsvDataWrapper;

public interface INVAdhocDriveProcessingService {

	List<NVL3CsvDataWrapper> processAdhocFileForReport(Integer woId, Layer3SummaryWrapper aggrigateWrapperData)
			throws IOException;

}
