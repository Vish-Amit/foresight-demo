package com.inn.foresight.module.nv.layer3.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.app.exceptions.InvalidEncryptionException;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HDFSDao;
import com.inn.foresight.module.nv.layer3.service.INVLayer3Service;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.workorder.dao.IWOFileDetailDao;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;
import com.inn.foresight.module.nv.workorder.recipe.service.IRecipeService;
import com.inn.foresight.module.nv.workorder.service.IWOFileDetailService;
import com.inn.foresight.module.nv.workorder.stealth.dao.IStealthTaskResultDao;
import com.inn.foresight.module.nv.workorder.stealth.model.StealthTaskResult;
import com.inn.foresight.module.nv.workorder.utils.NVWorkorderUtils;
import com.inn.foresight.module.nv.workorder.wrapper.FileResponseWrapper;

/**
 * The Class NVLayer3ServiceImpl.
 *
 * @aut
 * hor innoeye date - 29-Dec-2017 1:48:55 PM
 */
@Service("NVLayer3ServiceImpl")
public class NVLayer3ServiceImpl extends NVLayer3Utils implements INVLayer3Service {

	/**
	 * The logger.
	 */
	private Logger logger = LogManager.getLogger(NVLayer3ServiceImpl.class);

	/**
	 * The nv layer HDFS dao.
	 */
	@Autowired(required = false)
	private INVLayer3HDFSDao nvLayerHDFSDao;

	/**
	 * The wo file detail service.
	 */
	@Autowired
	private IWOFileDetailService woFileDetailService;
	@Autowired
	private IWOFileDetailDao woFileDetailDao;
	@Autowired
	private IRecipeService recipeService;

	@Autowired
	private IStealthTaskResultDao stealthTaskResultDao;
	@Autowired
	INVLayer3HDFSDao nvLayer3HDFSDao;

	/**
	 * Inserts file detail in WOFileDetail and Persist WO recipe QMDL/CSV files if
	 * FileDetails inserted successfully.
	 *
	 * @param mappingId
	 *            the mapping id
	 * @param isRetried
	 *            : true if Testing of Recipe is Retried, null otherwise
	 * @param fileStream
	 *            the file stream
	 * @param fileName
	 *            : Name of the QMDL or CSV File
	 * @param fileType
	 *            the file type
	 * @param localFilePath2 
	 * @return 
	 * @return file Path where file got save in hdfs
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws DaoException
	 *             Signals that an Dao exception has occurred.
	 * @throws RestException
	 *             Signals that an Business exception has occurred.
	 * @throws NumberFormatException
	 *             the number format exception
	 * @throws InvalidEncryptionException
	 *             the invalid encryption exception
	 */
	@Override
	public String syncWorkorderFile(Integer woRecipeMappingId, Boolean isRetried, InputStream fileStream,
			String fileName, String fileType, String taskId, String localFilePath)
					throws IOException, InvalidEncryptionException {

		logger.info("Inside syncWorkorderFile");
		fileName = NVWorkorderUtils.getfileNameForSynchedFile(fileName);

		String finalZipFilePath = localFilePath + fileName;

		logger.info("localFilePath is: {}    fileName is: {}", localFilePath, fileName);

		File file = new File(localFilePath);
		if (!file.exists()) {
			file.mkdirs();
		}

		Boolean isFileCopiedToLocal = NVLayer3Utils.copyFileToLocalPath(finalZipFilePath, fileStream);
		if (isFileCopiedToLocal.booleanValue()) {
			String jsonMsg = NVWorkorderUtils.vallidateZipFile(finalZipFilePath, localFilePath);

			logger.info("Going to vallidate ZipFile: {}", jsonMsg);
			if (jsonMsg.equals(NVLayer3Constants.SUCCESS)) {

				String hdfsFilePath = ConfigUtils.getString(ConfigEnum.NV_LAYER3_WO_FILE_HDFS_BASE_FILE_PATH.getValue())
						+ woRecipeMappingId + File.separator + fileName;
				
				String hdfsFileDir = ConfigUtils.getString(ConfigEnum.NV_LAYER3_WO_FILE_HDFS_BASE_FILE_PATH.getValue())
						+ woRecipeMappingId + File.separator;


				if (!nvLayerHDFSDao.isFileExist(hdfsFilePath)) {
					logger.info("File Does'nt Exist in HDFS. Going to insert");
					try {
						nvLayerHDFSDao.copyFileFromLocalToHDFS(finalZipFilePath, hdfsFileDir.trim());
					} catch (Exception e) {
						nvLayerHDFSDao.deleteFileFromHDFS(hdfsFilePath);
						throw new IOException(e);
					}
					woFileDetailService.insertWOFileDetail(woRecipeMappingId, fileName, hdfsFilePath, isRetried,
							fileType);
					recipeService.startRecipe(woRecipeMappingId, true, taskId);
				} else {
					updateDelationStatusofFileOrCreateFileDetail(woRecipeMappingId, fileName, taskId, hdfsFilePath,
							isRetried, fileType);
				}
				logger.info("Done syncWorkorderFile for woRecipeMappingId {}",woRecipeMappingId);

				return NVLayer3Constants.SUCCESS;
			} else {
				return jsonMsg;
			}
		}
		logger.info("Done syncWorkorderFile");
		return FAILURE;
	}

	private void updateDelationStatusofFileOrCreateFileDetail(Integer woRecipeMappingId, String fileName, String taskId,
			String hdfsFilePath, Boolean isRetried, String fileType) {
		try {
			WOFileDetail woFileDetail = woFileDetailDao.getFileDetailByWORecipeMappingAndFileName(woRecipeMappingId,
					fileName);
			if (woFileDetail != null) {
				woFileDetail.setIsDeleted(Boolean.FALSE);
				woFileDetail.setIsProcessed(Boolean.FALSE);
				woFileDetail.setProcessedLogFilePath(null);
				woFileDetail.setModificationTime(new Date());
				woFileDetailDao.update(woFileDetail);
				if (taskId != null) {
					recipeService.startRecipe(woRecipeMappingId, true, taskId);
				}
			}
		} catch (NoResultException | EmptyResultDataAccessException ne) {
			logger.error(
					"inside the method updateDelationStatusofFileOrCreateFileDetail NoResultException Gooing to create{}",
					ne.getMessage());
			woFileDetailService.insertWOFileDetail(woRecipeMappingId, fileName, hdfsFilePath, isRetried, fileType);

		}

	}

	@Override
	public void syncWorkOrderFileForBenchMarking(String woRecipeMappingIdList, Boolean isRetried, InputStream inputFile,
			String fileName, String fileType) throws IOException, InvalidEncryptionException {
		String[] woRecipeMapingIdSplit = woRecipeMappingIdList.split(Symbol.UNDERSCORE_STRING);
		String hdfsFilePath = ConfigUtils.getString(ConfigEnum.NV_LAYER3_WO_FILE_HDFS_BASE_FILE_PATH.getValue())
				+ woRecipeMappingIdList + File.separator + fileName;

		NVLayer3Utils.validateFilePath(hdfsFilePath);

		for (String woId : woRecipeMapingIdSplit) {
			woFileDetailService.insertWOFileDetail(Integer.parseInt(woId), fileName, hdfsFilePath, isRetried, fileType);
		}
		nvLayerHDFSDao.persistWORecipeQMDLFileToHDFS(inputFile, hdfsFilePath);
		logger.info("Done syncWorkOrderFileForBenchMarking");

	}

	@Override
	public String syncMultipleWorkorderFiles(HttpServletRequest httpServletRequest) {
		logger.info("Going to syncMultipleWorkorderFiles");
		String[] idAndType = null;
		String fileType = null;
		List<FileResponseWrapper> fileStatusList = new ArrayList<>();
		String localFilePath = null;
		try {
			Integer woRecipeMappingId = null;
			List<FileItem> fileItemList = NVLayer3Utils.extractFileItemsFromHttpRequest(httpServletRequest);
			if (fileItemList.isEmpty() || fileItemList.size() < NVConstant.THREE_INT) {
				return INVALID_PARAMETER_JSON;
			}
			logger.info("Multi part list size {}", fileItemList.size());
			idAndType = getWORecipeMappingIdAndFileType(fileItemList);
			woRecipeMappingId = Integer.parseInt(idAndType[ForesightConstants.ZERO]);
			fileType = idAndType[1];
			if (woRecipeMappingId == null || woRecipeMappingId.equals(NVConstant.ZERO_INT)) {
				return INVALID_WORECIPEMAPPING_ID;
			}
			if (fileType == null || fileType.equalsIgnoreCase("")) {
				return INVALID_FILE_TYPE;
			}
			logger.info("Going to syncMultipleWorkorderFiles for woRecipeMappingId : {}, fileType :{}",
					woRecipeMappingId, fileType);

			localFilePath = ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH)
					+ String.valueOf(new Date().getTime()) + ForesightConstants.FORWARD_SLASH;

			iterateAndSyncWorkorderFiles(fileType, woRecipeMappingId, fileItemList, localFilePath, fileStatusList);
			if (!fileStatusList.isEmpty()) {
				Map<String, List<FileResponseWrapper>> finalResponseMap = new HashMap<>();
				finalResponseMap.put(RESULT, fileStatusList);
				return new Gson().toJson(finalResponseMap);
			} else {
				return SUCCESS_JSON;
			}
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		} finally {
			try {
				FileUtils.deleteDirectory(new File(localFilePath));
			} catch (Exception e) {
				logger.error("Exception inside the method syncWorkorderFile while deletingDirectory {}",
						e.getMessage());
			}
		}
	}

	private void iterateAndSyncWorkorderFiles(String fileType, Integer woRecipeMappingId, List<FileItem> fileItemList,
			String localFilePath, List<FileResponseWrapper> fileStatusList) {
		for (FileItem request : fileItemList) {
			if (!WO_RECIPE_MAPPING_ID.equalsIgnoreCase(request.getFieldName())
					&& !FILE_TYPE.equalsIgnoreCase(request.getFieldName())) {
				try {
					String jsonMsg = syncWorkorderFile(woRecipeMappingId, false, request.getInputStream(),
							request.getFieldName(), fileType, null, localFilePath);
					if (!jsonMsg.equals(SUCCESS)) {
						FileResponseWrapper tempFileResponseWrapper = new FileResponseWrapper();
						tempFileResponseWrapper.setFileName(request.getFieldName());
						tempFileResponseWrapper.setStatus(jsonMsg);
						fileStatusList.add(tempFileResponseWrapper);
					}
				} catch (Exception e) {
					logger.error("Exception in syncMultipleWorkorderFiles {}", ExceptionUtils.getStackTrace(e));
					FileResponseWrapper tempFileResponseWrapper = new FileResponseWrapper();
					tempFileResponseWrapper.setFileName(request.getFieldName());
					tempFileResponseWrapper.setStatus(FAILURE);
					fileStatusList.add(tempFileResponseWrapper);
				}
			}
		}
	}

	private String[] getWORecipeMappingIdAndFileType(List<FileItem> fileItemList) {
		String[] idAndType = new String[ForesightConstants.TWO];
		fileItemList.stream().forEach(request -> {
			if (WO_RECIPE_MAPPING_ID.equalsIgnoreCase(request.getFieldName())) {
				idAndType[ForesightConstants.ZERO] = request.getString();
			}
			if (FILE_TYPE.equalsIgnoreCase(request.getFieldName())) {
				idAndType[ForesightConstants.ONE] = request.getString();
			}
		});
		return idAndType;
	}

	@Override
	public void syncStealthWorkorderFile(Integer stealthTaskResultId, InputStream fileStream, String fileName,
			String fileType) throws IOException, InvalidEncryptionException {
		logger.info("Inside syncStealthWorkorderFile");
		StealthTaskResult stealthTaskResult = stealthTaskResultDao.findByPk(stealthTaskResultId);
		Integer recipeMappingId = stealthTaskResult.getWoRecipeMapping().getId();
		if (recipeMappingId != null) {
			String hdfsFilePath = ConfigUtils.getString(ConfigEnum.NV_LAYER3_WO_FILE_HDFS_BASE_FILE_PATH.getValue())
					+ recipeMappingId + File.separator + fileName;

			NVLayer3Utils.validateFilePath(hdfsFilePath);

			if (!nvLayerHDFSDao.isFileExist(hdfsFilePath)) {
				logger.info("File Does'nt Exist in HDFS. Going to insert");
				nvLayerHDFSDao.persistWORecipeQMDLFileToHDFS(fileStream, hdfsFilePath);
				woFileDetailService.insertWOFileDetail(recipeMappingId, fileName, hdfsFilePath, false, fileType);
				// stealthTaskResult.setStatus(StealthConstants.Status.SUCCESS.name());
				// stealthTaskResultDao.update(stealthTaskResult);
			} else {
				updateDelationStatusofFileOrCreateFileDetail(recipeMappingId, fileName, null, hdfsFilePath, false,
						fileType);
			}
		}
		logger.info("Done syncWorkorderFile");
	}

}
