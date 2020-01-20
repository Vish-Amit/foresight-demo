package com.inn.foresight.module.nv.layer3.rest;

import java.io.InputStream;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.commons.Validate;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.layer3.service.INVLayer3Service;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.product.security.utils.AuthenticationCommonUtil;

/**
 * The Class NVLayer3Rest.
 *
 * @author innoeye
 * date - 29-Dec-2017 1:53:44 PM
 */
@Path("/NVLayer3")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NVLayer3Rest extends NVLayer3Utils {

	/** The logger. */
	private Logger logger = LogManager.getLogger(NVLayer3Rest.class);
	
	/** The nv layer 3 service. */
	@Autowired
	private INVLayer3Service nvLayer3Service;
	
	/**
	 * Sync WO recipe QMDL/CSV files in HDFS and also insert WOFileDetail instance in DB.
	 *
	 * @param woRecipeMappingId the wo recipe mapping id whose file is to sync
	 * @param isRetried the flag to acknowledge that recipe is retried again
	 * @param fileType represents type of files present in the zip 
	 * @param inputFile the inputStream of zip file to sync
	 * @param fileName the file name
	 * @return the status of sync call
	 */
	@POST
	@Path("/syncWorkOrderFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String syncRecipeFiles(@Multipart(value = WO_RECIPE_MAPPING_ID) String encrWORecipeMappingId,
			@QueryParam(IS_RETRIED) Boolean isRetried, @QueryParam(FILE_TYPE) String fileType,
			@Multipart(value = ForesightConstants.FILE) InputStream inputFile,
			@Multipart(value = ForesightConstants.FILENAME) String fileName) {
		logger.info("Going to persit Recipe Files");
		try {
			Validate.checkNoneNull(encrWORecipeMappingId, inputFile, fileName, fileType);
			logger.info("fileName {} fileType {}", fileName,fileType);
			if ((fileName.endsWith(ZIP_FILE_EXTENTION))
					&& (StringUtils.equalsIgnoreCaseAny(fileType, ZIP_FILE_TYPE, CSV_FILE_TYPE, QMDL_FILE_TYPE))
					&& inputFile != null ) {
				Integer woRecipeMappingId = Integer.parseInt(AuthenticationCommonUtil.checkForValueDecryption(encrWORecipeMappingId));
			
				String localFilePath = ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH)
						+ String.valueOf(new Date().getTime()) + ForesightConstants.FORWARD_SLASH;

				nvLayer3Service.syncWorkorderFile(woRecipeMappingId, isRetried, inputFile, fileName, fileType,null,localFilePath);
			} else {
				logger.error("File is not of QMDL or CSV Type or inputstream is null.");
				throw new RestException(FILE_IS_NOT_OF_REQUIRED_TYPE);
			}
			logger.info("Done persit Recipe Files");
			return AuthenticationCommonUtil.checkForValueEncryption(SUCCESS_JSON,null);
		} catch (NullPointerException e) {
			logger.error("NullPointerException in syncRecipeFiles : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(WO_RECIPE_MAPPING_ID_IS_NULL,null);
		} catch (RestException e) {
			logger.error("RestException in syncRecipeFiles : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(e.getMessage(),null);
		} catch (IllegalArgumentException e) {
			logger.error("Illegal arguments while sync recipe files : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(FILE_IS_EMPTY_JSON,null);
		} catch (Exception e) {
			logger.error("Exception while  sync recipe files : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(FAILURE_JSON,null);
		}
	}

	
	/**
	 * Sync WO recipe QMDL/CSV files in HDFS and also insert WOFileDetail instance in DB.
	 *
	 * @param woRecipeMappingId the wo recipe mapping id whose file is to sync
	 * @param isRetried the flag to acknowledge that recipe is retried again
	 * @param fileType represents type of files present in the zip 
	 * @param inputFile the inputStream of zip file to sync
	 * @param fileName the file name
	 * @return the status of sync call
	 */
	@POST
	@Path("/syncWorkOrderFileForBenchMarking")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String syncWorkOrderFileForBenchMarking(@Multipart(value = WO_RECIPE_MAPPING_ID) String woRecipeMappingId,
			@QueryParam(IS_RETRIED) Boolean isRetried, @QueryParam(FILE_TYPE) String fileType,
			@Multipart(value = ForesightConstants.FILE) InputStream inputFile,
			@Multipart(value = ForesightConstants.FILENAME) String fileName) {
			
		logger.info("Going to persit syncWorkOrderFileForBenchMarking Files {} {}  {} ",woRecipeMappingId,fileType,fileName);
		try {
			Validate.checkNoneNull(woRecipeMappingId, inputFile, fileName, fileType);
			if ((fileName.endsWith(ZIP_FILE_EXTENTION))
					&& (StringUtils.equalsIgnoreCaseAny(fileType, ZIP_FILE_TYPE, CSV_FILE_TYPE, QMDL_FILE_TYPE))
					&& inputFile != null ) {
				nvLayer3Service.syncWorkOrderFileForBenchMarking(woRecipeMappingId, isRetried, inputFile, fileName, fileType);
			} else {
				logger.error("File is not of QMDL or CSV Type or inputstream is null.");
				throw new RestException(FILE_IS_NOT_OF_REQUIRED_TYPE);
			}
			logger.info("Done persit Recipe Files");
			return AuthenticationCommonUtil.checkForValueEncryption(SUCCESS_JSON,null);
		} catch (NullPointerException e) {
			logger.error("NullPointerException in syncRecipeFiles : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(WO_RECIPE_MAPPING_ID_IS_NULL,null);
		} catch (RestException e) {
			logger.error("RestException in syncRecipeFiles : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(e.getMessage(),null);
		} catch (IllegalArgumentException e) {
			logger.error("Exception in syncRecipeFiles : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(FILE_IS_EMPTY_JSON,null);
		} catch (Exception e) {
			logger.error("Exception in syncRecipeFiles : {}", ExceptionUtils.getStackTrace(e));
			return AuthenticationCommonUtil.checkForValueEncryption(FAILURE_JSON,null);
		}
	}
}