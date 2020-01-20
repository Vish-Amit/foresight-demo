package com.inn.foresight.module.nv.workorder.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpException;
import com.inn.commons.io.FileUtils;
import com.inn.commons.unit.Duration;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.workorder.constant.KMLParserConstant;
import com.inn.foresight.module.nv.workorder.service.IKMLParserService;
import com.inn.foresight.module.nv.workorder.utils.KMLParser;
import com.inn.foresight.module.nv.workorder.utils.KMLParserUtil;

@Service("KMLParserServiceImpl")
public class KMLParserServiceImpl extends KMLParserUtil implements IKMLParserService {

	private Logger logger = LogManager.getLogger(KMLParserServiceImpl.class);

	@Override
	public String parseKMLAndGetBoundary(HttpServletRequest request) {
		String response = null;
		String type = request.getParameter(KMLParserConstant.TYPE);
		logger.info("Going to parseKMLAndGetBoundary for type:{}", type);
		if (type != null) {
			List<FileItem> fileItemList = null;
			try {
				fileItemList = extractFileItemsFromHttpRequest(request);
			} catch (FileUploadException e) {
				logger.error("FileUploadException in parseKMLAndGetBoundary:{}", ExceptionUtils.getStackTrace(e));
			}
			if (fileItemList != null && (isValidKMLFile(type, fileItemList) || isValidTabFile(type, fileItemList))) {
				response = sendRequestToDropwizard(fileItemList);
			} else {
				response = KMLParserConstant.INVALID_FILE_JSON;
			}
		} else {
			response = KMLParserConstant.INVALID_PARAMETER_JSON;
		}
		return response;
	}

	private String sendRequestToDropwizard(List<FileItem> fileItemList) {
		String response = null;
		Duration duration = Duration
				.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
		MultipartEntityBuilder multipartEntity = KMLParserUtil.getMultiPartEntity(fileItemList);
		try {
			String url = KMLParserUtil.getDropwizardUrl(KMLParserConstant.PARSE_KML_AND_GET_BOUNDARY_URI);
			logger.info("Dropwizard URL to parseKMLAndGetBoundary :{} ", url);
			response = KMLParserUtil.sendHttpPostRequest(url, multipartEntity.build(), true, duration).getString();
			logger.info("parseKMLAndGetBoundary Done from dropwizard");

		} catch (HttpException e) {
			logger.error("HttpException in parseKMLAndGetBoundary:{}", ExceptionUtils.getStackTrace(e));
		}
		return response;
	}

	@Override
	public String parseTabAndKMLAndGetBoundary(HttpServletRequest httpServletRequest) {
		String response = null;
		List<FileItem> fileItem = null;

		try {
			fileItem = KMLParserUtil.extractFileItemsFromHttpRequest(httpServletRequest);
		} catch (FileUploadException e) {
			logger.error("FileUploadException in parseKMLAndGetBoundary: {} ", ExceptionUtils.getStackTrace(e));
		}
		if (fileItem != null && KMLParserConstant.MIN_FILES_COUNT_FOR_KML == fileItem.size()) {
			response = parseKMLAndGetBoundary(fileItem);
		} else if (fileItem != null && fileItem.size() >= KMLParserConstant.MIN_FILES_COUNT_FOR_TAB) {
			response = parserTabFilesAndGetBoundary(fileItem);
		} else {
			response = KMLParserConstant.INVALID_PARAMETER_JSON;
		}

		return response;
	}

	/**
	 * Parser tab and get boundary.
	 * 
	 * @param fileItem the file item
	 * @return the string
	 * @throws RestException
	 */

	private String parserTabFilesAndGetBoundary(List<FileItem> fileItem) {
		String filePath = ConfigUtils.getString(ConfigUtil.NV_WORKORDER_TAB_FILE_BASE_PATH);
		List<String> fileList = new ArrayList<>();
		fileItem.parallelStream().forEach(request -> {
			try {
				InputStream inputStream = request.getInputStream();
				String fileName = request.getFieldName().replace(Symbol.SPACE_STRING, Symbol.UNDERSCORE_STRING);
				byte[] buffer = new byte[inputStream.available()];
						while (inputStream.read(buffer) > ForesightConstants.ZERO) {
							logger.info("Getting file {}", filePath + fileName);
							File targetFile = new File(filePath + fileName);
							try (OutputStream outStream = new FileOutputStream(targetFile)) {
								outStream.write(buffer);
							}
						}
				fileList.add(filePath + fileName);
			} catch (IOException e) {
				logger.error("IOException in parserTabFilesAndGetBoundary : {} ", ExceptionUtils.getStackTrace(e));
			}
		});
		InputStream kmlInputStream = parseTabFileAndGetKML(fileList);
		return KMLParser.parseKML(kmlInputStream);
	}

	private InputStream parseTabFileAndGetKML(List<String> fileList) {
		InputStream kmlInputStream = executeShellScriptToParseTAB(fileList);
		fileList.add(fileList.get(ForesightConstants.ZERO).replace(KMLParserConstant.DOT_TAB_EXTENTION, KMLParserConstant.DOT_KML_EXTENTION));
		deleteAllFiles(fileList);
		return kmlInputStream;
	}

	private void deleteAllFiles(List<String> fileList) {
		fileList.parallelStream().forEach(fileName -> {
			File file = new File(fileName);
			boolean delete = file.delete();
			logger.info("File Deleted {}",delete);
		});
	}
//	private void deleteAllFiles(List<String> fileList) {
//		fileList.parallelStream().forEach(fileName -> {
//			File file = new File(fileName);
//			try {
//				FileUtils.deleteDirectory(file);
//			} catch (IOException e) {
//				logger.error("IOException inside deleteAllFiles {}", e.getMessage());
//			}
//		});
//	}

	private InputStream executeShellScriptToParseTAB(List<String> fileList) {
		String shellScript = ConfigUtils.getString(ConfigUtil.NV_WORKORDER_TAB_FILE_PARSE_SCRIPT);
		String tabFilePath = getTabFilePath(fileList);
		if (tabFilePath != null) {
			String kmlFilePath = tabFilePath
					.replace(KMLParserConstant.DOT_TAB_EXTENTION, KMLParserConstant.DOT_KML_EXTENTION)
					.replace(KMLParserConstant.DOT_TAB_EXTENTION_SMALL, KMLParserConstant.DOT_KML_EXTENTION);
			String[] cmd = { shellScript, kmlFilePath, tabFilePath };
			logger.info("Getting cmd {}", Arrays.toString(cmd));
			try {
				Process scriptExcution = Runtime.getRuntime().exec(cmd);
				scriptExcution.waitFor();
				return getKMLInputStream(kmlFilePath);
			} 
			catch (InterruptedException e) {
				logger.info("Getting InterruptedException {}", Utils.getStackTrace(e));
			    Thread.currentThread().interrupt();
				return null;
			}
			catch (IOException e) {
				logger.info("Getting IOException {}", Utils.getStackTrace(e));
				return null;
			} 
		} else {
			throw new RestException("TAB file not found.");
		}
	}

	private String getTabFilePath(List<String> fileList) {
		for (String file : fileList) {
			if (file.contains(KMLParserConstant.DOT_TAB_EXTENTION)
					|| file.contains(KMLParserConstant.DOT_TAB_EXTENTION_SMALL)) {
				return file;
			}
		}
		return null;
	}

	private InputStream getKMLInputStream(String kmlFilePath) {
		try {
			return new FileInputStream(kmlFilePath);
		} catch (FileNotFoundException e) {
			logger.info("File Does not Exist {}", kmlFilePath);
			return null;
		}
	}

	/**
	 * Parses the KML and get boundary.
	 * 
	 * @param fileItem the file item
	 * @return the string
	 * @throws RestException
	 */
	private String parseKMLAndGetBoundary(List<FileItem> fileItem) {
		String response = null;
		try {
			InputStream inputStream = fileItem.get(KMLParserConstant.FIRST_INDEX).getInputStream();
			response = KMLParser.parseKML(inputStream);
		} catch (IOException e) {
			logger.error("IOException in parseKMLAndGetBoundary : {} ", ExceptionUtils.getStackTrace(e));
		}
		return response;
	}
}
