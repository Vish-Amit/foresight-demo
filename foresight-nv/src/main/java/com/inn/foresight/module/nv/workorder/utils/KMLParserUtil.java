package com.inn.foresight.module.nv.workorder.utils;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.http.HttpPostRequest;
import com.inn.commons.unit.Duration;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.workorder.constant.KMLParserConstant;

public class KMLParserUtil  {
	private static Logger logger = LogManager.getLogger(KMLParserUtil.class);

	public static MultipartEntityBuilder getMultiPartEntity(List<FileItem> fileItemList) {
		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

		fileItemList.parallelStream()
					.forEach(e -> {
						try {
							multipartEntityBuilder.addPart(e.getFieldName(),
									new InputStreamBody(e.getInputStream(), ForesightConstants.FILE));
						} catch (IOException e1) {
							logger.error("IOException in getMultiPartEntity... {}", ExceptionUtils.getStackTrace(e1));
						}
					});

		return multipartEntityBuilder;
	}

	/**
	 * Extract file items from http request.
	 * 
	 * @param request
	 *            the HttpServletRequest
	 * @return the list of FileItem
	 * @throws FileUploadException
	 *             the file upload exception
	 */
	public static List<FileItem> extractFileItemsFromHttpRequest(HttpServletRequest request)
			throws FileUploadException {
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		return fileUpload.parseRequest(request);
	}

	public static String getDropwizardUrl(String parseKmlAndGetBoundaryUri) {
		return ConfigUtils.getString(ConfigEnum.MICRO_SERVICE_BASE_URL.getValue())
				+ ConfigUtils	.getString(ConfigEnum.DROPWIZARD_URL_KML_PARSER, Boolean.TRUE)
								.concat(parseKmlAndGetBoundaryUri);
	}

	/**
	 * Send http post request.
	 *
	 * @param url
	 *            the url
	 * @param reqEntity
	 *            the http entity
	 * @param isToEnableTimeOut
	 *            the is to enable time out
	 * @param duration
	 *            the duration
	 * @return the http post request
	 */
	public static HttpPostRequest sendHttpPostRequest(String url, HttpEntity reqEntity, boolean isToEnableTimeOut,
			Duration duration) {
		HttpPostRequest httpPostRequest = new HttpPostRequest(url, reqEntity);
		if (duration != null) {
			httpPostRequest.setConnectionTimeout(duration);
			httpPostRequest.setEnableTimeout(isToEnableTimeOut);
		}
		return httpPostRequest;
	}

	public boolean isValidTabFile(String type, List<FileItem> fileItemList) {
		return KMLParserConstant.FILE_TYPE_TAB.equalsIgnoreCase(type) && fileItemList.size() >= KMLParserConstant.MIN_FILES_COUNT_FOR_TAB
				&& isValidAllTabFiles(fileItemList);
	}

	public boolean isValidAllTabFiles(List<FileItem> fileItemList) {
		Integer count = 0;
		for (FileItem request : fileItemList) {
			if (request	.getFieldName()
						.contains(KMLParserConstant.DOT_DAT_EXTENTION)
					|| request	.getFieldName()
								.contains(KMLParserConstant.DOT_ID_EXTENTION)
					|| request	.getFieldName()
								.contains(KMLParserConstant.DOT_MAP_EXTENTION)
					|| request	.getFieldName()
								.contains(KMLParserConstant.DOT_TAB_EXTENTION)
					||request	.getFieldName()
								.contains(KMLParserConstant.DOT_DAT_EXTENTION_SMALL)
					|| request	.getFieldName()
								.contains(KMLParserConstant.DOT_ID_EXTENTION_SMALL)
					|| request	.getFieldName()
								.contains(KMLParserConstant.DOT_MAP_EXTENTION_SMALL)
					|| request	.getFieldName()
								.contains(KMLParserConstant.DOT_TAB_EXTENTION_SMALL)) {
				count++;
			} 
		}
		return count.equals(KMLParserConstant.MIN_FILES_COUNT_FOR_TAB);
	}

	public boolean isValidKMLFile(String type, List<FileItem> fileItemList) {
		return KMLParserConstant.FILE_TYPE_KML.equalsIgnoreCase(type) && fileItemList.size() == KMLParserConstant.MIN_FILES_COUNT_FOR_KML
				&& (fileItemList.get(KMLParserConstant.FIRST_INDEX)
								.getFieldName()
								.contains(KMLParserConstant.DOT_KML_EXTENTION)
						|| fileItemList	.get(KMLParserConstant.FIRST_INDEX)
										.getFieldName()
										.contains(KMLParserConstant.DOT_KML_EXTENTION_SMALL));
	}

}
