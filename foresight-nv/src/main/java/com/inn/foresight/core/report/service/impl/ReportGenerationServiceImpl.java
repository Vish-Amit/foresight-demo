package com.inn.foresight.core.report.service.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.commons.Symbol;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.dao.IReportGenerationHBASEDao;
import com.inn.foresight.core.report.dao.IReportGenerationHDFSDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.core.report.service.IReportGenerationService;

@Service("ReportGenerationServiceImpl")
public class ReportGenerationServiceImpl implements IReportGenerationService {
	
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(ReportGenerationServiceImpl.class);

	@Autowired
	private IAnalyticsRepositoryDao analyticsRepositoryDao;

	@Autowired
	private IReportGenerationHDFSDao reportGenerationHdfsDao;
	
	@Autowired
	private IReportGenerationHBASEDao reportGenerationHbaseDao;
	
	private static String FILE_NOT_FOUND = "\"result\":\"File Not Found \"";
	
	
	@Override
	@Transactional
	public Response getReportById(Integer analyticsrepositoryId, String extension) throws IOException {

		logger.info("Going to get report data for analyticsrepositoryId {}", analyticsrepositoryId);
		AnalyticsRepository analyticsRepoObject = analyticsRepositoryDao.findByPk(analyticsrepositoryId);
		String filePath = analyticsRepoObject.getFilepath();
		if (analyticsRepoObject.getReportTemplate() != null) {
			return getFileReponseFromAnalyticObject(analyticsrepositoryId, analyticsRepoObject, filePath,
					analyticsRepoObject.getDownloadFileName(), extension);
		} else {
			logger.warn("analyticsRepoObject template null for analyticsrepositoryId {}", analyticsrepositoryId);
		}
		return getFileNotFoundResponse();
	}

	private Response getFileReponseFromAnalyticObject(Integer analyticsrepositoryId,
			AnalyticsRepository analyticsRepoObject, String filePath, String downloadFileName, String extension)
			throws IOException {
		String storageType = analyticsRepoObject.getReportTemplate().getStorageType();
		logger.info("storageType {}", storageType);

		if (isFilePathOfHDFS(filePath)) {
			logger.info("get filePath for {} , filePath {} updating storage type to HDFS", analyticsrepositoryId,
					filePath);
			storageType = ForesightConstants.HDFS_REPO_STORAGE_TYPE;
		}

		if (ForesightConstants.HBASE_REPO_STORAGE_TYPE.equalsIgnoreCase(storageType)) {
			return getReponseFromHbaseForReport(analyticsrepositoryId);
		} else if (ForesightConstants.HDFS_REPO_STORAGE_TYPE.equalsIgnoreCase(storageType)) {
			return getResponseFromHdfs(filePath, downloadFileName, extension);
		} else {
			logger.warn("Invalid storage type {}", storageType);
			return Response.ok(ForesightConstants.INVALID_STORAGE_TYPE).build();
		}
	}
	
	private Response getResponseFromHdfs(String filePath, String downloadFileName,
			String extension) {
		filePath = updateFilePathUsingExtension(filePath, extension);
		InputStream inputStream = reportGenerationHdfsDao.getInputStreamFromHdfs(filePath);
		Response.ResponseBuilder builder = Response.status(200);
		builder = builder.entity(inputStream)
				.header(ForesightConstants.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
				.header(ForesightConstants.CONTENT_DISPOSITION,
						ForesightConstants.ATTACHMENTFILENAME + getFileName(filePath, downloadFileName) + "\"");
		return builder.build();
	}
	private String updateFilePathUsingExtension(String filePath, String extension) {
		if (filePath.contains(Symbol.DOT_STRING) && extension != null) {
			filePath = (filePath
					.split(ForesightConstants.DOT_SPLIT_STRING)[ForesightConstants.FILE_NAME_WITHOUT_EXTENSION_INDEX])
					+ Symbol.DOT_STRING + extension;
			logger.info("Getting final file name {}  ", filePath);
		}
		return filePath;
	}
	private String getFileName(String filePath, String downloadFileName) {
		if (downloadFileName != null) {
			return downloadFileName;
		} else {
			return filePath.substring(filePath.lastIndexOf(Symbol.SLASH_FORWARD) + 1);
		}
	}


	
	private Response getReponseFromHbaseForReport(Integer analyticsrepositoryId) throws IOException {
		Response.ResponseBuilder builder = Response.status(200);
		HBaseResult hbaseResult = reportGenerationHbaseDao.getReportDataForRowKey(analyticsrepositoryId);
		if (hbaseResult != null && hbaseResult.getString("reportName") != null) {
			String filename = getFileNameFromHbaseResult(hbaseResult);
			if (filename != null) {
				builder = builder.entity(hbaseResult.getValue("value".getBytes()))
						.header(ForesightConstants.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
						.header(ForesightConstants.CONTENT_DISPOSITION,
								ForesightConstants.ATTACHMENTFILENAME + filename + "\"");
				return builder.build();
			}
		} else {
			logger.warn("Hbase result  null for analyticsrepositoryId {}", analyticsrepositoryId);
		}
		return getFileNotFoundResponse();
	}

	private String getFileNameFromHbaseResult(HBaseResult hbaseResult) {
		return hbaseResult.getString("reportName");
	}
	
	public static boolean isFilePathOfHDFS(String filePath) {
		if (filePath != null) {
			return StringUtils.countMatches(filePath, "/") > 3;
		}
		return false;
	}

	private Response getFileNotFoundResponse() {
		return Response.ok(FILE_NOT_FOUND).build();
	}

	@Override
	@Transactional
	public Response getReportByPath(String filePath,String downloadFileName, String extension) throws IOException {
			if (filePath != null) {
				logger.info("Going to get report data for filePath {}", filePath);
				return getResponseFromHdfs(filePath, downloadFileName, extension);
			} else {
				return getFileNotFoundResponse();
			}
		
	}
}
