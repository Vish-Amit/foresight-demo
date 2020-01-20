package com.inn.foresight.module.nv.workorder.recipe.rest.impl;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.Validate;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.service.INVLayer3Service;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.recipe.model.Recipe;
import com.inn.foresight.module.nv.workorder.recipe.rest.RecipeRest;
import com.inn.foresight.module.nv.workorder.recipe.service.IRecipeService;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.FTPDetailsWrapper;

/** The Class RecipeRestImpl. */
@Path("/Recipe")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Service("RecipeRestImpl")
public class RecipeRestImpl implements RecipeRest {

	/** The logger. */
	private Logger logger = LogManager.getLogger(RecipeRestImpl.class);

	/** The service. */
	@Autowired
	private IRecipeService service;
	
	/** The nv layer 3 service. */
	@Autowired
	private INVLayer3Service nvLayer3Service;

	/**
	 * Inserts a new Recipe In Table.
	 *
	 * @param recipe
	 *            object
	 * @return Json of Created Recipe
	 */
	@POST
	@Path("createRecipe")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response createRecipe(Recipe strRecipe) {
		logger.info("Inside method : createRecipe() {}", strRecipe);

		if (strRecipe != null) {
			try {
				return Response.ok(service.createRecipe(strRecipe)).build();
			} catch (RestException e) {
				return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
			}
		}
		return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
	}

	/**
	 * Updates An Existing Recipe.
	 *
	 * @param recipe
	 *            object
	 * @return Json of Update Recipe
	 * @throws RestException
	 *             the rest exception
	 */
	@POST
	@Path("updateRecipe")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response updateRecipe(Recipe strRecipe) {
		logger.info("Inside method : createRecipe() {}", strRecipe);
		if (strRecipe != null && strRecipe.getId() != null) {
			return Response.ok(service.updateRecipe(strRecipe)).build();
		}
		return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
	}

	/**
	 * Get All the Recipe between the provided Limits (lLimit and uLimit).
	 *
	 * @param lLimit
	 *            the l limit
	 * @param uLimit
	 *            the u limit
	 * @return json of Recipe objects between provided lLimit and uLimit
	 * @throws RestException
	 *             the rest exception
	 */
	@GET
	@Path("findAllRecipe/{llimit}/{ulimit}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response findAllRecipe(@PathParam("llimit") Integer lLimit, @PathParam("ulimit") Integer uLimit) {
		if (lLimit != null && uLimit != null && uLimit > lLimit) {
			return Response.ok(service.findAllRecipe(lLimit, uLimit)).build();
		}
		return Response.ok(NVWorkorderConstant.INVALID_LIMITS_JSON).build();
	}

	/**
	 * Returns count of all Recipes in Db.
	 *
	 *
	 * @return Total Recipe count in DB
	 * @throws RestException
	 *             the RestException
	 */
	@GET
	@Path("getTotalRecipeCount")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getTotalRecipeCount() {

		return Response.ok(service.getTotalRecipeCount()).build();
	}

	/**
	 * Returns List of Recipe by Category.
	 *
	 *
	 * @return List of Recipe By category and By User
	 * @throws RestException
	 *             the RestException
	 */
	@POST
	@Path("getRecipeByCategory")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getRecipeByCategory(List<String> category) {
		logger.info("Going to Fetch Recipe List By Category @Service by Category : {} ", category);
		if (category != null) {
			return Response.ok(service.getRecipeByCategory(category)).build();
		}
		return Response.ok(NVWorkorderConstant.INVALID_PARAMETERS).build();
	}

	@POST
	@Path("deleteByPk")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response deleteByPk(List<Integer> strRecipeIds) {
		logger.info("Going to Delete recipe by Pk {}", strRecipeIds);
		return Response.ok(service.deleteByPk(strRecipeIds)).build();
	}
	
	@GET
	@Path("deleteRecipeById")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response deleteRecipeById(@QueryParam(NVWorkorderConstant.RECEIPE_ID)  Integer recipeId) {
		logger.info("Going to Delete recipe by recipeId {}", recipeId);
		return Response.ok(service.deleteRecipeById(recipeId)).build();
	}
	
	@GET
	@Path("checkRecipeMappedWithWO")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response checkRecipeMappedWithWO(@QueryParam(NVWorkorderConstant.RECEIPE_ID)  Integer recipeId) {
		logger.info("Going to Check recipe assigned to wo by recipeId {}", recipeId);
		return Response.ok(service.checkRecipeMappedWithWO(recipeId)).build();
	}

	/**
	 * Update Status to COMPLETED of Recipe corresponds to provided WORecipeMapping
	 * Id.
	 * 
	 * @param woRecipeMappingId
	 *            : WORecipeMapping Id
	 * @return Success Message if Status updated, Failure Message otherwise
	 */
	@GET
	@Path("completeRecipe")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public Response completeRecipe(@QueryParam(NVWorkorderConstant.RECEIPE_ID) Integer woRecipeMappingId) {

		logger.info("Going to complete Recipe for WORecipeMapping Id {}", woRecipeMappingId);

		if (woRecipeMappingId != null) {
			try {
				return Response.ok(service.completeRecipe(woRecipeMappingId)).build();
			} catch (Exception e) {
				return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
			}
		}
		return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
	}

	/**
	 * Inserts a new Recipe In Table.
	 *
	 * @param recipe
	 *            object
	 * @return Json of Created Recipe
	 */
	@POST
	@Path("createRecipeForMobile")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	@Override
	public Response createRecipeForMobile(String strRecipe) {
		logger.info("Inside method : createRecipeForMobile() {}", strRecipe);

		if (Utils.hasValidValue(strRecipe)) {
			try {
				return Response.ok(service.createRecipeForMobile(strRecipe)).build();
			} catch (RestException e) {
				return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
			}
		}
		return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
	}

	/**
	 * Get WO Files By Recipe Mapping Id.
	 *
	 * @param recipeMappingId
	 *            recipe mapping id
	 * @return Json of list of WO file details as Response
	 */
	@GET
	@Path("getWOFilesByRecipeMappingId")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response getWOFilesByRecipeMappingId(@QueryParam(NVWorkorderConstant.RECIPE_MAPPING_ID) Integer recipeMappingId) {
		logger.info("Inside method : getFilesByRecipeMappingId() {}", recipeMappingId);
		if (recipeMappingId != null) {
			try {
				return Response.ok(service.getWOFilesByRecipeMappingId(recipeMappingId)).build();
			} catch (RestException e) {
				return Response.ok(NVWorkorderConstant.FAILURE_JSON).build();
			}
		}
		return Response.ok(NVWorkorderConstant.INVALID_PARAMETER_JSON).build();
	}

	@GET
	@Path("/getWODetailsZipFile")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Override
	public Response getWODetailsZipFile(@QueryParam(NVWorkorderConstant.FILE_PATH) String filePath) {
		try {
			Response.ResponseBuilder builder = Response.status(200);
			String fileName = filePath.substring(filePath.lastIndexOf(NVWorkorderConstant.FILE_DIRECTORY_SEPERATOR) + 1,
					filePath.length());
			builder = builder.entity(service.getWODetailsZipFile(filePath))
					.header(ReportConstants.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM)
					.header(ReportConstants.CONTENT_DISPOSITION, NVWorkorderConstant.CONTENT_DISPOSITION_PREFIX
							+ fileName + NVWorkorderConstant.CONTENT_DISPOSITION_SUFFIX);
			return builder.build();
		} catch (Exception e) {
			logger.error("Exception in getDriveDetailReceipeWise : {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
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
	@Path("/syncWorkOrderFile")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Override
	public String syncRecipeFiles(@Multipart(value = NVWorkorderConstant.WO_RECIPE_MAPPING_ID) Integer wORecipeMappingId,
			@QueryParam(NVLayer3Constants.IS_RETRIED) Boolean isRetried, @QueryParam(NVLayer3Constants.FILE_TYPE) String fileType,
			@Multipart(value = ForesightConstants.FILE) InputStream inputFile,
			@Multipart(value = ForesightConstants.FILENAME) String fileName) {
		logger.info("Going to persit Recipe Files");
		
		String localFilePath = ConfigUtils.getString(NVConfigUtil.LAYER3_DUMP_FILE_PATH)
				+ String.valueOf(new Date().getTime()) + ForesightConstants.FORWARD_SLASH;

		try {
			Validate.checkNoneNull(wORecipeMappingId, inputFile, fileName, fileType);
			logger.info("fileName {} fileType {}", fileName,fileType);
			if ((fileName.endsWith(NVLayer3Constants.ZIP_FILE_EXTENTION))
					&& (StringUtils.equalsIgnoreCaseAny(fileType, NVLayer3Constants.ZIP_FILE_TYPE, NVLayer3Constants.CSV_FILE_TYPE, NVLayer3Constants.QMDL_FILE_TYPE))
					&& inputFile != null ) {
				nvLayer3Service.syncWorkorderFile(wORecipeMappingId, isRetried, inputFile, fileName, fileType,null,localFilePath);
			} else {
				logger.error("File is not of QMDL or CSV Type or inputstream is null fileName or fileType or inputFile");
				throw new RestException(NVLayer3Constants.FILE_IS_NOT_OF_REQUIRED_TYPE);
			}
			logger.info("Done persit Recipe Files");
			return NVLayer3Constants.SUCCESS_JSON;
		} catch (NullPointerException e) {
			logger.error("NullPointerException in syncRecipeFiles : {}", ExceptionUtils.getStackTrace(e));
			return NVLayer3Constants.WO_RECIPE_MAPPING_ID_IS_NULL;
		} catch (RestException e) {
			logger.error("RestException in syncRecipeFiles : {}", ExceptionUtils.getStackTrace(e));
			return e.getMessage();
		} catch (IllegalArgumentException e) {
			logger.error("Exception in syncRecipeFiles : {}", ExceptionUtils.getStackTrace(e));
			return NVWorkorderConstant.FILE_IS_EMPTY_JSON;
		} catch (Exception e) {
			logger.error("Exception in syncRecipeFiles : {}", ExceptionUtils.getStackTrace(e));
			return NVLayer3Constants.FAILURE_JSON;
		}finally {
			try {
				FileUtils.deleteDirectory(new File(localFilePath));
			} catch (Exception e) {
				logger.error("Exception inside the method syncWorkorderFile while deletingDirectory {}",
						e.getMessage());
			}
		}
	}

	
	/**
	 * ValidateFTPConnection is used to validate FTP connection.
	 * 
	 * @param ftpDetailsWrapper
	 * @return
	 */
	@POST
	@Path("/validateFTPConnection")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response validateFTPConnection(FTPDetailsWrapper ftpDetailsWrapper) {
		try {
			logger.info("Inside Method validateFTPConnection");
			return Response.ok(service.validateFTPConnection(ftpDetailsWrapper)).build();
		} catch (Exception e) {
			logger.error("Exception in validateFTPConnection {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(QMDLConstant.EXCEPTION_SOMETHING_WENT_WRONG_JSON).build();
		}
	}
	
	
	
	
	
	@GET
	@Path("/reopenRecipe/{woRecipeId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public Response reopenRecipeById(@PathParam("woRecipeId") Integer woRecipeId,@QueryParam("workOrderId") Integer workOrderId) {
		try {
			if (Utils.hasValidValue(String.valueOf(woRecipeId))&&Utils.hasValidValue(String.valueOf(workOrderId))) {
				return  service.reopenRecipeById(woRecipeId,workOrderId,false);
				
			} else {
				return Response.ok(InBuildingConstants.INVALID_PARAMETER_JSON).build();
			}
		} catch (RestException e) {
			logger.error("Error while dreopenRecipeById woRecipeId {}: {}",woRecipeId ,ExceptionUtils.getMessage(e));
			return Response.ok(InBuildingConstants.EXCEPTION_SOMETHING_WENT_WRONG).build();
		}
	}
}
