package com.inn.foresight.module.nv.reportgeneration.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonParseException;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.report.dao.INVReportHbaseDao;
import com.inn.foresight.module.nv.report.dao.INVReportHdfsDao;
import com.inn.foresight.module.nv.report.optimizedImage.GoogleMaps;
import com.inn.foresight.module.nv.report.service.INVInBuildingReportService;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.reportgeneration.constants.NVReportGenerationConstants;
import com.inn.foresight.module.nv.reportgeneration.dao.INVReportGenerationDao;
import com.inn.foresight.module.nv.reportgeneration.service.INVReportGenerationService;
import com.inn.foresight.module.nv.reportgeneration.wrapper.NVReportWrapper;
import com.inn.foresight.module.nv.workorder.wrapper.NVWorkorderWrapper;

@Service("NVReportGenerationServiceImpl")
@EnableAsync
public class NVReportGenerationServiceImpl implements INVReportGenerationService {
	/** The logger. */
	private Logger logger = LogManager.getLogger(NVReportGenerationServiceImpl.class);

	@Autowired
	private ApplicationContext context;

	/** The nv reportgeneration from hbase dao. */

	@Autowired
	private INVReportGenerationDao iReportGenerationDao;

	@Autowired
	private IAnalyticsRepositoryDao analyticsRepositoryDao;

	@Autowired
	private INVReportHdfsDao invReportHdfsDao;

	@Autowired
	private INVInBuildingReportService inBuildingReportService;

	@Autowired
	private INVReportHbaseDao invReportHbaseDao;

	/** Public ReportGenerationServiceImpl(){ }. */

	@Override
	public NVReportWrapper getReportData(Integer analyticsrepositoryId) throws Exception {

		logger.info("Going to get report data for analyticsrepositoryId {}", analyticsrepositoryId);
		NVReportWrapper reportWrapper = new NVReportWrapper();

		AnalyticsRepository analyticsRepoObject = analyticsRepositoryDao.findByPk(analyticsrepositoryId);

		String storageType = null;
		String filePath = analyticsRepoObject.getFilepath();

		if (analyticsRepoObject.getReportTemplate() != null) {
			storageType = analyticsRepoObject.getReportTemplate().getStorageType();

			logger.info("storageType {}", storageType);

			if ("HBASE".equalsIgnoreCase(storageType)) {

				HBaseResult hbaseResult = iReportGenerationDao.getReportDataForRowKey(analyticsrepositoryId);

				if (hbaseResult != null
						&& hbaseResult.getString(NVReportGenerationConstants.NV_REPORT_NAME) != null) {

					reportWrapper.setReportName(hbaseResult.getString(NVReportGenerationConstants.NV_REPORT_NAME));
					reportWrapper.setReportType(hbaseResult.getString(NVReportGenerationConstants.NV_REPORT_FORMAT));
					reportWrapper.setFile(hbaseResult.getValue(NVReportGenerationConstants.NV_REPORT_CONTENT.getBytes()));
				} else {

					logger.info("Hbase reuslt null");
				}
			} else if ("HDFS".equalsIgnoreCase(storageType)) {

				final byte[] report = invReportHdfsDao.getReportFromHdfs(analyticsrepositoryId, filePath);
				reportWrapper.setFile(report);
				reportWrapper.setReportName(filePath.substring(filePath.lastIndexOf('/') + 1));

			} else {
				logger.error("Invalid storage type {}", storageType);

			}

		} else {
			logger.error("analyticsRepoObject template null ");

		}
		return reportWrapper;

	}

	@SuppressWarnings("unchecked")
	@Override
	@Async("reportTaskExecutor")
	@Transactional
	public void generateReport(String json) throws Exception {
		logger.info("Inside method generateReport with Json {}  ", json);
		Map<String, Object> jsonMap = new ObjectMapper().readValue(json, HashMap.class);
		Integer analyticsrepositoryId = (Integer) jsonMap.get(ForesightConstants.ANALYTICAL_REPORT_KEY);
		AnalyticsRepository analyticsRepository = analyticsRepositoryDao.findByPk(analyticsrepositoryId);
		if (analyticsRepository != null && analyticsRepository.getReportConfig() != null) {
			JSONObject jsonObject;
			try {
				jsonObject = ReportUtil.convertStringToJsonObject(analyticsRepository.getReportConfig().replaceAll("\'", "\""));
				jsonObject.put(ForesightConstants.ANALYTICAL_REPORT_KEY, analyticsrepositoryId);
				logger.info("jsonObject {} ", jsonObject);
				if (analyticsRepository.getReportTemplate() != null) {
					goingToInvokeReportGenerationClassMethod(analyticsrepositoryId, analyticsRepository, jsonObject);
				}
			} catch (JsonParseException e) {
				logger.info("Exception inside method generateReport {} ", Utils.getStackTrace(e));
				throw new BusinessException("Json parser Exception for " + analyticsRepository.getReportConfig());
			} catch (ClassCastException e) {
				throw new BusinessException(
						"ClassCastException Unable to Parse this json " + analyticsRepository.getReportConfig());
			} catch (Exception e) {
				throw e;
			}
		} else {
			throw new BusinessException("Inavlid Report Instance Id " + analyticsrepositoryId.intValue());
		}
	}

	private void goingToInvokeReportGenerationClassMethod(Integer analyticsrepositoryId,
			AnalyticsRepository analyticsRepository, JSONObject jsonObject) {
		try {
			logger.info("BuilderClass for analyticsrepository Id is {} ",
					analyticsRepository.getReportTemplate().getBuilderClass());
			Class<?> classObj = Class.forName(analyticsRepository.getReportTemplate().getBuilderClass());
			Object newObject = context.getBean(classObj);
			logger.info("newObject.getName: {},{}",classObj.getName(),classObj.hashCode());
			Method methodToExecute = classObj.getMethod("execute", String.class);
			methodToExecute.setAccessible(true);
			methodToExecute.invoke(newObject, jsonObject.toJSONString());
			
		} catch (NoSuchMethodException | SecurityException e) {
			logger.error(
					"NoSuchMethodException | SecurityException occured inside method generateReport for analyticsrepositoryId {} , {} ",
					analyticsrepositoryId, Utils.getStackTrace(e));
		} catch (ClassNotFoundException e) {
			logger.error(
					"ClassNotFoundException occured inside method generateReport for analyticsrepositoryId {}  ,{}",
					analyticsrepositoryId, e.getMessage());
		} catch (Exception e) {
			logger.error("Exception occured inside method for analyticsRepositoryId {} , {} ",
					analyticsrepositoryId, Utils.getStackTrace(e));
		}
	}

	@Override
	public Response getPdfReportByInstanceId(Integer analyticsrepositoryId) {
		logger.info("Inside method getPdfReportByInstanceId for instance id  {} ", analyticsrepositoryId);
		File file = invReportHbaseDao.getPDFReportByAnalyticsRepositoryId(analyticsrepositoryId);
		if (file != null) {
			Response.ResponseBuilder builder = Response.status(NVReportGenerationConstants.INDEX_TW0_HUNDRED);
			return builder.entity(file).header("Content-Type", "application/pdf").build();
		} else {
			return Response.ok("{\"result\":\"Report Not Found for this id \"}").build();
		}

	}

	@Override
	public Response getInBuildingReport(String json) {
		logger.info("Going to get In Building Report for json {} ", json);
		return inBuildingReportService.createIBReport(json);
	}

	@Override
	public Response getInBuildingReport(Integer recipeId, String operator, Integer inbuildingid, Integer workorderId, String technology) {
		logger.info("Going to get In Building Report for json  {} ,{}  ,{} ,{} ", recipeId, operator, inbuildingid,
				workorderId);
		return inBuildingReportService.createIBReport(recipeId, operator, inbuildingid, workorderId, technology);
	}

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
	@Override
	@Transactional
	public String getDownloadFileName(Integer analyticsrepositoryId, String extension) {
		logger.info("inside the method getDownloadFileName {} extension {}",analyticsrepositoryId,extension);
		AnalyticsRepository analyticsRepository=analyticsRepositoryDao.findByPk(analyticsrepositoryId);
		String fileName= getFileName(analyticsRepository.getFilepath(), analyticsRepository.getDownloadFileName(),extension) ;
		 logger.info("fileName == {}",fileName);
		 return fileName;
		
	}

	private Response getFileReponseFromAnalyticObject(Integer analyticsrepositoryId,
			AnalyticsRepository analyticsRepoObject, String filePath, String downloadFileName, String extension)
			throws IOException {
		String storageType = analyticsRepoObject.getReportTemplate().getStorageType();
		logger.info("storageType {}", storageType);

		if (isFilePathOfHDFS(filePath)) {
			logger.info("get filePath for {} , filePath {} updating storage type to HDFS", analyticsrepositoryId,
					filePath);
			storageType = NVLayer3Constants.HDFS_RECORD_CONSTANT;
		}

		if (NVLayer3Constants.HBASE_RECORD_CONSTANT.equalsIgnoreCase(storageType)) {
			return getReponseFromHbaseForReport(analyticsrepositoryId);
		} else if (NVLayer3Constants.HDFS_RECORD_CONSTANT.equalsIgnoreCase(storageType)) {
			return getResponseFromHdfs(analyticsrepositoryId, filePath, downloadFileName, extension);
		} else {
			logger.warn("Invalid storage type {}", storageType);
			return Response.ok(NVLayer3Constants.INVALID_STORAGE_TYPE).build();
		}
	}
	

	public static boolean isFilePathOfHDFS(String filePath) {
		if (filePath != null) {
			return StringUtils.countMatches(filePath, "/") > NVReportGenerationConstants.INDEX_THREE;
		}
		return false;
	}

	private Response getResponseFromHdfs(Integer analyticsrepositoryId, String filePath, String downloadFileName,
			String extension) {
		filePath = updateFilePathUsingExtension(filePath, extension);
		byte[] reporData = invReportHdfsDao.getInputStreamFromHdfs(analyticsrepositoryId, filePath);
		Response.ResponseBuilder builder = Response.status(NVReportGenerationConstants.INDEX_TW0_HUNDRED);
		builder = builder.entity(reporData)
				.header(ForesightConstants.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
				.header(ForesightConstants.CONTENT_DISPOSITION,
						NVLayer3Constants.ATTACHMENT_FILE + getFileName(filePath, downloadFileName,extension) + "\"");
		return builder.build();
	}

	private String updateFilePathUsingExtension(String filePath, String extension) {
		if (filePath.contains(Symbol.DOT_STRING) && extension != null) {
			filePath = (filePath
					.split(ReportConstants.DOT_SPLIT_STRING)[ReportConstants.FILE_NAME_WITHOUT_EXTENSION_INDEX])
					+ Symbol.DOT_STRING + extension;
			logger.info("Getting final file name {}  ", filePath);
		}
		return filePath;
	}

	private String getFileName(String filePath, String downloadFileName, String extension) {
		if (downloadFileName != null) {
			if (extension != null) {
				downloadFileName = (downloadFileName
						.split(ReportConstants.DOT_SPLIT_STRING)[ReportConstants.FILE_NAME_WITHOUT_EXTENSION_INDEX])
						+ Symbol.DOT_STRING + extension;

			}
			return downloadFileName;
		} else {
			return filePath.substring(filePath.lastIndexOf(Symbol.SLASH_FORWARD) + 1);
		}
	}

	private Response getReponseFromHbaseForReport(Integer analyticsrepositoryId) throws IOException {
		Response.ResponseBuilder builder = Response.status(NVReportGenerationConstants.INDEX_TW0_HUNDRED);
		HBaseResult hbaseResult = iReportGenerationDao.getReportDataForRowKey(analyticsrepositoryId);
		if (hbaseResult != null && hbaseResult.getString(NVReportGenerationConstants.NV_REPORT_NAME) != null) {
			String filename = getFileNameFromHbaseResult(hbaseResult);
			if (filename != null) {
				builder = builder.entity(hbaseResult.getValue(NVReportGenerationConstants.NV_REPORT_CONTENT.getBytes()))
						.header(ForesightConstants.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
						.header(ForesightConstants.CONTENT_DISPOSITION,
								NVLayer3Constants.ATTACHMENT_FILE + filename + "\"");
				return builder.build();
			}
		} else {
			logger.warn("Hbase result  null for analyticsrepositoryId {}", analyticsrepositoryId);
		}
		return getFileNotFoundResponse();
	}

	private Response getFileNotFoundResponse() {
		return Response.ok(NVLayer3Constants.FILE_NOT_FOUND).build();
	}

	private String getFileNameFromHbaseResult(HBaseResult hbaseResult) {
		return hbaseResult.getString(NVReportGenerationConstants.NV_REPORT_NAME);
	}

	@Override
	public String saveImagefromGoogleApi() {
		logger.info("Inside method saveImagefromGoogleApi ");
		try {
			String url = "https://maps.googleapis.com/maps/api/staticmap?center=22.78,75.7&zoom=15&size=304x304&KEY=";
			url+=ConfigUtils.getString(ConfigUtil.GOOGLE_MAP_KEY);
			logger.info("url to hit  {} ", url);
			BufferedImage image = new GoogleMaps().getBufferredImageFromURL(url);
			ImageIO.write(image, "jpg", new File(ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT)
					+ ReportConstants.FORWARD_SLASH + "temp" + ReportConstants.DOT_JPG));
			return ForesightConstants.SUCCESS_JSON;
		} catch (Exception e) {
			logger.error("Exception inside method saveImagefromGoogleApi {} ", Utils.getStackTrace(e));
		}
		return ForesightConstants.FAILURE_JSON;
	}

	@Override
	public Response getInBuildingReportFloorWise(Integer floorId, Integer inbuildingid, String technology) {
		return inBuildingReportService.createIBReportByFloor(inbuildingid, floorId, technology);
	}
	@Override
	public boolean removeRejectedData(NVWorkorderWrapper nvWorkorderWrapper) {
		try {
			logger.info("inside method removeRejectedData nvWorkorderWrapper: {}",nvWorkorderWrapper.toString());
			String tableName = ConfigUtils.getString(ReportConstants.NV_LAYER3_DRIVE_TABLE);
			String rowKey = NVLayer3Utils.getRowKeyForLayer3(nvWorkorderWrapper.getId(),
					nvWorkorderWrapper.getOperatorName(), nvWorkorderWrapper.getWoRecipeMappingId().toString());
			logger.info("tableName: {}  rowKey: {}",tableName,rowKey);
			return invReportHbaseDao.delete(tableName, rowKey);

		} catch (IOException e) {
			logger.error("Exception inside the method removeRejectedData  {} ", Utils.getStackTrace(e));
			return false;
		}
	}
}
