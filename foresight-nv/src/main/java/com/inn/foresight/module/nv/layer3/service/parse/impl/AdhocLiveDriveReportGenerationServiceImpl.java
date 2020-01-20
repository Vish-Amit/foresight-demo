package com.inn.foresight.module.nv.layer3.service.parse.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.NVL3CsvDataWrapper;
import com.inn.foresight.module.nv.layer3.service.parse.IAdhocLiveDriveReportGenerationService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;

/**
 * The Class AdhocLiveDriveReportGenerationServiceImpl.
 *
 * @author innoeye date - May 18,2018 15:23:23 PM
 */
@Service("AdhocLiveDriveReportGenerationServiceImpl")
public class AdhocLiveDriveReportGenerationServiceImpl extends NVLayer3Utils
		implements IAdhocLiveDriveReportGenerationService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(AdhocLiveDriveReportGenerationServiceImpl.class);

	@Override
	public String generateCSVReportForLiveDrive(Integer workorderId, List<NVL3CsvDataWrapper> csvWrapperList, String filePath,String fileName) {
		try {
			logger.debug("Inside method generateCSVReportForLiveDrive. WorkorderId:{}, CsvWrapperList:{}",workorderId,  new Gson().toJson(csvWrapperList));
			 fileName = copyCsvToLocalPath(getCsvfromListOfWrapper(csvWrapperList), filePath,fileName);
			if (!StringUtils.isBlank(fileName)) {
				Map<String, String> responseMap = new HashMap<>();
				responseMap.put(FILE_NAME, fileName);
				return new Gson().toJson(responseMap);
			} else {
				return EXCEPTION_SOMETHING_WENT_WRONG_JSON;
			}
		} catch (Exception e) {
			logger.error("Error in method generateCSVReportForLiveDrive: {}", ExceptionUtils.getStackTrace(e));
			throw new BusinessException(EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
	}

	private String getCsvfromListOfWrapper(List<NVL3CsvDataWrapper> csvWrapperList) {
		StringBuilder builder = new StringBuilder();
		String csvHeaders = getCsvHeadersFromConfig();
		builder.append(csvHeaders);
		builder.append(NEW_LINE_SEPERATOR);
		for (NVL3CsvDataWrapper csvWrapper : csvWrapperList) {
			builder.append(csvWrapper.processDataCSVForLiveDrive() + NEW_LINE_SEPERATOR);
		}
		return builder.toString();
	}

	private String getCsvHeadersFromConfig() {
		String csvHeaders = ConfigUtils.getString(NVConfigUtil.LIVE_DRIVE_CSV_HEADERS);
		return csvHeaders.replaceAll(CONFIG_HEADER_SEPERATOR, CSV_HEADER_SEPERATOR);
	}

	private String copyCsvToLocalPath(String csv, String directoryPath, String fileName) throws IOException {
		 fileName =fileName+ Symbol.UNDERSCORE_STRING + System.currentTimeMillis() + REPORT_FILE_EXTENSION;
		String filePath = directoryPath + fileName;
		logger.debug("Going to copy csv to local path csv:{}, Local Path:{}", csv,filePath);
		InputStream stream = IOUtils.toInputStream(csv, ENCODING_UTF_8);
		boolean isFileCopied = copyFileToLocalPath(filePath, stream);
		return isFileCopied ? fileName : null;
	}

}
