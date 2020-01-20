package com.inn.foresight.module.nv.layer3.service.parse;

import java.util.List;

import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NVL3CsvDataWrapper;

/**
 * The Interface IAdhocLiveDriveReportGenerationService.
 *
 * @author innoeye date - May 18,2018 15:24:24 PM
 */
public interface IAdhocLiveDriveReportGenerationService {

	String generateCSVReportForLiveDrive(Integer workorderId, List<NVL3CsvDataWrapper> csvWrapperList, String filePath, String fileName);

}
