package com.inn.foresight.module.nv.workorder.recipe.service.impl;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.inn.bpmn.dao.IBPMNWOTemplateDao;
import com.inn.bpmn.dao.IBpmnTaskCandidateDao;
import com.inn.bpmn.model.BPMNWOTemplate;
import com.inn.bpmn.model.BPMNWorkorder;
import com.inn.bpmn.model.BpmnTaskCandidate;
import com.inn.bpmn.service.IActivitiService;
import com.inn.bpmn.utils.enums.WOCategory;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.dao.IReportTemplateDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.core.report.model.AnalyticsRepository.RepositoryType;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.core.report.model.ReportTemplate;
import com.inn.foresight.core.report.service.IAnalyticsRepositoryService;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.core.workorder.service.IGenericWorkorderService;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HDFSDao;
import com.inn.foresight.module.nv.layer3.service.INVHbaseService;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.IWOFileDetailDao;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping.Status;
import com.inn.foresight.module.nv.workorder.recipe.dao.IRecipeDao;
import com.inn.foresight.module.nv.workorder.recipe.model.Recipe;
import com.inn.foresight.module.nv.workorder.recipe.service.IRecipeService;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.FTPDetailsWrapper;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.RecipeWrapper;
import com.inn.foresight.module.nv.workorder.utils.NVWorkorderUtils;
import com.inn.product.security.utils.AuthenticationCommonUtil;
import com.inn.product.um.user.model.User;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

/** The Class RecipeServiceImpl. */
@Service("RecipeServiceImpl")
public class RecipeServiceImpl extends NVWorkorderUtils implements IRecipeService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(RecipeServiceImpl.class);

	/** Instance of WORecipeMappingDaoImpl. */
	@Autowired
	private IWORecipeMappingDao woRecipeMappingDao;

	/** The dao. */
	@Autowired
	private IRecipeDao dao;

	/** The NV Layer3 HDFS dao. */
	@Autowired
	private INVLayer3HDFSDao nvLayer3HDFSDao;

	/** Instance of GenericWorkorderDaoImpl. */
	@Autowired
	private IGenericWorkorderDao genericWorkorderDao;

	/** Instance of GenericWorkorderDaoImpl. */
	@Autowired
	private IWOFileDetailDao woFileDetailDao;

	@Autowired
	private IReportTemplateDao reportTemplateDao;

	@Autowired
	private IAnalyticsRepositoryService analyticsRepositoryService;

	/** Instance of BpmnTaskCandidateDaoImpl. */
	@Autowired
	private IBpmnTaskCandidateDao bpmnTaskCandidateDao;
	@Autowired
	IGenericWorkorderService genericWorkorderService;
	@Autowired
	private IAnalyticsRepositoryDao analyticsRepositoryDao;
	/** Instance of ActivitiServiceImpl. */
	@Autowired
	private IActivitiService activitiService;
	@Autowired
	private IBPMNWOTemplateDao bpmnWOTemplateDao;

	@Autowired
	private INVHbaseService nvHbaseService;


	/**
	 * Inserts new recipe In Table.
	 * 
	 * @param recipe the recipe
	 * @return Newly created Recipe Object
	 * @throws RestException the rest exception
	 */
	@Override
	@Transactional
	public Recipe createRecipe(Recipe strRecipe) {
		try {
			if (isValidRecipe(strRecipe)) {
				strRecipe.setCreator(UserContextServiceImpl.getUserInContext());
				strRecipe.setRecipeId(getRecipeId(strRecipe));
				strRecipe.setCreationTime(new Date());
				strRecipe.setModificationTime(new Date());
				strRecipe.setSource(NVWorkorderConstant.SERVER);
				strRecipe.setDeleted(false);
				return dao.create(strRecipe);
			} else {
				throw new RestException(INVALID_RECIPE_DETAILS);
			}
		} catch (DaoException e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException();
		}
	}

	/**
	 * Updates recipe In Table.
	 * 
	 * @param recipe the recipe
	 * @return Updated created Recipe Object
	 * @throws RestException the rest exception
	 */
	@Override
	@Transactional
	public String updateRecipe(Recipe strRecipe) {
		try {
			if (isValidRecipe(strRecipe) && !(strRecipe.getType().equals(NVWorkorderConstant.PREDEFINED))) {
				String message = checkRecipeMappedWithWO(strRecipe.getId());
				if (!NVWorkorderConstant.SUCCESS_JSON.equals(message)) {
					return message;
				}
				Recipe newRecipe = dao.findByPk(strRecipe.getId());
				newRecipe.setModificationTime(new Date());
				newRecipe.setScriptJson(strRecipe.getScriptJson());
				newRecipe.setName(strRecipe.getName());
				newRecipe.setDescription(strRecipe.getDescription());
				newRecipe.setDeleted(false);
				dao.update(newRecipe);
				return NVWorkorderConstant.SUCCESS_JSON;
			} else {
				return NVWorkorderConstant.RESULT_EXCEPTION_SOMETHING_WENT_WRONG_JSON;
			}
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			return NVWorkorderConstant.RESULT_EXCEPTION_SOMETHING_WENT_WRONG_JSON;
		}
	}

	/**
	 * Gets All Recipe Id Between Provided Limits.
	 * 
	 * @param lLimit the l limit
	 * @param uLimit the u limit
	 * @return Recipe objects between provided lLimit and uLimit
	 * @throws RestException the rest exception
	 */
	@Override
	public List<RecipeWrapper> findAllRecipe(Integer lLimit, Integer uLimit) {
		logger.info("Inside method : findAllRecipe()");
		try {
			return dao.findAllRecipes(lLimit, uLimit);
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * Gets Max Recipe Number Present at The end Of recipeId in From Db.
	 * 
	 * @param recipe the recipe
	 * @return Generated Recipe Id String
	 * @throws RestException the rest exception
	 * @Example Provided Recipe Id : RC-CSTM, Recipe in DB : RC-CSTM-1, New Recipe
	 *          Id : RC-CSTM-2
	 */
	private String getRecipeId(Recipe recipe) {
		try {
			Integer number = getMaximumNumberFromList(dao.getRecipeIdList(recipe.getRecipeId()), recipe.getRecipeId());
			StringBuilder builder = new StringBuilder();
			builder.append(recipe.getRecipeId()).append(Symbol.HYPHEN).append(number + NumberUtils.INTEGER_ONE);
			return builder.toString();
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * Returns count of all Recipes in Db.
	 * 
	 * @return Long Total Recipe count in DB
	 * @throws RestException the RestException
	 */
	@Override
	public Long getTotalRecipeCount() {
		try {
			return dao.getTotalRecipeCount();
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	/**
	 * Returns List of Recipe by Category.
	 * 
	 * @param category
	 * @return List of Recipe By category and By User
	 * @throws RestException the RestException
	 */
	@Override
	public List<RecipeWrapper> getRecipeByCategory(List<String> strCategory) {
		logger.info("Going to Fetch Recipe List By Category @Service");
		try {
			return dao.getRecipeByCategory(strCategory);
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(
					ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, NVWorkorderConstant.RECIPE, e));
		}
	}

	@Override
	@Transactional
	public String deleteByPk(List<Integer> strRecipeIds) {
		try {
			if (dao.deleteByPk(strRecipeIds) > 0) {
				return NVWorkorderConstant.SUCCESS_JSON;
			} else {
				return NVWorkorderConstant.FAILURE_JSON;
			}
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(
					ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, NVWorkorderConstant.RECIPE, e));
		}
	}

	@Override
	@Transactional
	public String deleteRecipeById(Integer recipeId) {
		try {
			String message = checkRecipeMappedWithWO(recipeId);
			if (!NVWorkorderConstant.SUCCESS_JSON.equals(message)) {
				return message;
			}

			Recipe recipe = dao.findByPk(recipeId);
			if (recipe != null && !(recipe.getType().equals(NVWorkorderConstant.PREDEFINED))) {
				logger.info("deleted recipe Id = " + recipe.getRecipeId());
				recipe.setModificationTime(new Date());
				recipe.setDeleted(true);
				dao.update(recipe);
				return NVWorkorderConstant.SUCCESS_JSON;
			} else {
				return NVWorkorderConstant.RESULT_EXCEPTION_SOMETHING_WENT_WRONG_JSON;
			}
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			return NVWorkorderConstant.RESULT_EXCEPTION_SOMETHING_WENT_WRONG_JSON;
		}
	}

	@Override
	public String checkRecipeMappedWithWO(Integer recipeId) {
		try {
			List<WORecipeMapping> wORecipeMapping = woRecipeMappingDao.getNonDeletedWOByRecipeId(recipeId);
			if (wORecipeMapping != null && wORecipeMapping.isEmpty()) {
				return NVWorkorderConstant.SUCCESS_JSON;
			}
			logger.info("checkRecipeMappedWithWO dao" + recipeId);
			return NVWorkorderConstant.RECIPE_ASSIGNED_TO_WO_JSON;
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			return NVWorkorderConstant.RESULT_EXCEPTION_SOMETHING_WENT_WRONG_JSON;
		}
	}

	/**
	 * Update Status to COMPLETED of Recipe corresponds to provided WORecipeMapping
	 * Id.
	 * 
	 * @param woRecipeMappingId the wo recipe mapping id
	 * @return Success Message if Status updated, Failure Message otherwise
	 * @throws RestException
	 */
	@Override
	@Transactional
	public String completeRecipe(Integer woRecipeMappingId) {
		try {
			WORecipeMapping woRecipeMapping = woRecipeMappingDao.findByPk(woRecipeMappingId);
			boolean isRecipeComplete =  isRecipeCompleteInProgressRecipe(woRecipeMapping);
			
			if(woRecipeMapping.getStatus().equals(Status.INPROGRESS) || woRecipeMapping.getStatus().equals(Status.REOPEN) ) {
				if (isRecipeComplete) {
					completeRecipe(woRecipeMappingId, woRecipeMapping);
				}
		
				else {
					return RECIPE_FILE_NOT_FOUND;
				}
			}
			
			else if (!woRecipeMapping.getStatus().equals(Status.COMPLETED)) {
				completeRecipe(woRecipeMappingId, woRecipeMapping);
			} 
			
			else {
				return RECIPE_COMPLETED_JSON;
			}
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, "Recipe", e));
		}
		return SUCCESS_JSON;
	}
@Override
	public boolean isRecipeCompleteInProgressRecipe(WORecipeMapping woRecipeMapping) {
		boolean isRecipeComplete = false;
		if (woRecipeMapping != null) {
			List<WOFileDetail> fileDetailByWORecipeMappingId = woFileDetailDao
					.getFileDetailByWORecipeMappingId(woRecipeMapping.getId());
			for (WOFileDetail woFileDetail : fileDetailByWORecipeMappingId) {
				if (woFileDetail != null) {
					String filePath =  woFileDetail.getFilePath() ;
					Boolean isDeleted = woFileDetail.getIsDeleted();
					if (filePath != null && (isDeleted == null || (!isDeleted))) {
						isRecipeComplete = true;
					} 
				}
				else {
					isRecipeComplete = false;
				}
			} 
		}
		return isRecipeComplete;
	}

	private void completeRecipe(Integer woRecipeMappingId, WORecipeMapping woRecipeMapping) {
		GenericWorkorder genericWorkorder = woRecipeMapping.getGenericWorkorder();
		deleteUnnecessaryRecipes(genericWorkorder, woRecipeMappingId);
		if(checkIsToInsertConfig(genericWorkorder)) {
			insertRequestForProcessing(woRecipeMapping);
		}
		genericWorkorder.setModificationTime(new Date());
		updateCompletionPercentage(genericWorkorder);
		woRecipeMapping.setStatus(Status.COMPLETED);
		woRecipeMappingDao.update(woRecipeMapping);
		genericWorkorderDao.update(genericWorkorder);
	}

	private boolean checkIsToInsertConfig(GenericWorkorder genericWorkorder) {
		String isToinsert=ConfigUtils.getString("IS_TO_INSERT_FILE_FOR_PROCESSING");
		return genericWorkorder != null && genericWorkorder.getTemplateType() != null
				&& ReportConstants.TRUE.equalsIgnoreCase(isToinsert);
	}


	private void deleteUnnecessaryRecipes(GenericWorkorder genericWorkorder, Integer woRecipeMappingId) {
		if (genericWorkorder.getTemplateType().equals(TemplateType.NV_ADHOC_LD)
				|| genericWorkorder.getTemplateType().equals(TemplateType.NV_ADHOC_BRTI_DRIVE)
				|| genericWorkorder.getTemplateType().equals(TemplateType.NV_ADHOC_BRTI_ST)
				|| genericWorkorder.getTemplateType().equals(TemplateType.NV_ADHOC_OD)
				|| genericWorkorder.getTemplateType().equals(TemplateType.NV_ADHOC_BRTI)) {
			List<WORecipeMapping> list = woRecipeMappingDao.getWORecipeByGWOId(genericWorkorder.getId());
			for (WORecipeMapping recipe : list) {
				if (!recipe.getId().equals(woRecipeMappingId)) {
					woRecipeMappingDao.delete(recipe);
				}
			}
		}
	}

	private void insertRequestForProcessing(WORecipeMapping woRecipeMapping) {
		logger.info(
				"inside insertRequestForProcessing going to insert wo report config data for workorderId : {}  {}  ",
				woRecipeMapping.getGenericWorkorder().getId(), woRecipeMapping.getId());
		JSONObject reportConfigJsonObj = new JSONObject();
		try {
			reportConfigJsonObj.put(WORKORDER_ID, woRecipeMapping.getGenericWorkorder().getId());
			reportConfigJsonObj.put(RECIPE_ID, woRecipeMapping.getId());
			BpmnTaskCandidate candidate = getBPMNTaskCandidate(woRecipeMapping.getGenericWorkorder().getWorkorderId());
			if (candidate != null) {
				reportConfigJsonObj.put(ASSIGNED_TO, getUserName(candidate.getUser()));
			}
			AnalyticsRepository analyticsRepositiryObj = new AnalyticsRepository();
			analyticsRepositiryObj.setReportConfig(reportConfigJsonObj.toString());
			analyticsRepositiryObj.setName(woRecipeMapping.getId() + Symbol.UNDERSCORE
					+ woRecipeMapping.getGenericWorkorder().getTemplateType().name());
			analyticsRepositiryObj.setProgress(progress.In_Progress);
			analyticsRepositiryObj.setRepositoryType(RepositoryType.CUSTOM_REPORT);
			analyticsRepositiryObj.setModuleName(NVConstant.MODULE_NAME);
			ReportTemplate reportTemplate=null;
			if(woRecipeMapping.getGenericWorkorder().getTemplateType().name().equalsIgnoreCase(TemplateType.SCANNING_RECEIVER.name()))
			{
				 reportTemplate= reportTemplateDao
						.getReportTemplateByReportType(TemplateType.SCANNING_RECEIVER.name());
			}
			else {
			reportTemplate = reportTemplateDao
					.getReportTemplateByReportType(NVWorkorderConstant.RECIPE_PARSING_TEMPLATE_NAME);
			}
			logger.info("going to createCustomReportRepository template: {}  ", reportTemplate.getReportType());
			analyticsRepositoryService.createCustomReportRepository(reportTemplate.getId(), analyticsRepositiryObj);
		} catch (JSONException e) {
			logger.error("JSONException in insertReportConfigData {}", Utils.getStackTrace(e));
			throw new RestException("Invalid workorderId");
		}

	}

	private BpmnTaskCandidate getBPMNTaskCandidate(String workorderId) {
		List<String> workorderIdList = new ArrayList<>();
		workorderIdList.add(workorderId);
		List<BpmnTaskCandidate> bpmnTaskCandidateList = bpmnTaskCandidateDao
				.getBPMNTaskCandidateListByWONoList(workorderIdList);
		Map<String, BpmnTaskCandidate> map = getBpmnTaskCandidateMap(bpmnTaskCandidateList);
		return map.get(workorderId);
	}

	/**
	 * Updates Completion Percentage On each Recipe Complete.
	 * 
	 * @param genericWorkorder
	 * @throws DaoException
	 * @throws RestException
	 */
	private void updateCompletionPercentage(GenericWorkorder genericWorkorder) {
		List<WORecipeMapping> list = woRecipeMappingDao.getWORecipeByGWOId(genericWorkorder.getId());
		genericWorkorder.setCompletionPercentage(
				Utils.roundOff(getCompletionPercentage(getCompletedRecipeCount(list), list.size()), 1));
	}

	private Integer getCompletedRecipeCount(List<WORecipeMapping> list) {
		Integer count = 0;
		for (WORecipeMapping recipe : list) {
			if (recipe.getStatus().equals(Status.COMPLETED)) {
				count++;
			}
		}
		return ++count;
	}

	/**
	 * Calculates Total completion Percentage Of a workorder.
	 * 
	 * @param oldPercent
	 * @param totalCount
	 * @return Double
	 * @throws RestException
	 */
	private static Double getCompletionPercentage(Integer count, Integer total) {
		try {
			return ((double) (count * CENT) / total);
		} catch (ArithmeticException e) {
			throw new RestException("Exception in calculating completion percentage.");
		}
	}

	@Override
	@Transactional
	public String startRecipe(Integer woRecipeMappingId, Boolean isUserAuthorized, String taskId) {
		try {
			WORecipeMapping woRecipeMapping = woRecipeMappingDao.findByPk(woRecipeMappingId);
			GenericWorkorder workorder = woRecipeMapping.getGenericWorkorder();
			if (!workorder.getStatus().equals(GenericWorkorder.Status.COMPLETED)
					&& !woRecipeMapping.getStatus().equals(Status.COMPLETED)) {
				if (isValidUser(workorder, taskId) || isUserAuthorized) {
					workorder.setStatus(GenericWorkorder.Status.INPROGRESS);
					workorder.setModificationTime(new Date());
					genericWorkorderDao.update(workorder);
					woRecipeMapping.setStatus(Status.INPROGRESS);
					woRecipeMappingDao.update(woRecipeMapping);
				} else {
					return WORKORDER_NOT_AASIGNED_JSON;
				}
			} else {
				return WORKORDER_COMPLETED_JSON;
			}
		} catch (Exception e) {
			logger.error(NVWorkorderConstant.EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, "Recipe", e));
		}
		return SUCCESS_JSON;
	}

	private boolean isValidUser(GenericWorkorder workorder, String taskId) {
		if (taskId != null) {
			List<Integer> taskIdList = new ArrayList<>();
			taskIdList.add(Integer.parseInt(taskId));
			List<BpmnTaskCandidate> bpmnTaskCandidateList = bpmnTaskCandidateDao
					.getBPMNTaskCandidateListByTaskIdList(taskIdList);
			return checkValidCandidate(bpmnTaskCandidateList);

		} else {
			List<BpmnTaskCandidate> bpmnTaskCandidateList = bpmnTaskCandidateDao
					.getBPMNTaskCandidateListByWONoList(Arrays.asList(workorder.getWorkorderId()));
			return checkValidCandidate(bpmnTaskCandidateList);
		}
	}

	private boolean checkValidCandidate(List<BpmnTaskCandidate> bpmnTaskCandidateList) {
		try {
		if (bpmnTaskCandidateList != null && !bpmnTaskCandidateList.isEmpty()) {
			BpmnTaskCandidate bpmnTaskCandidate = bpmnTaskCandidateList.get(0);
			Integer currentUserId = UserContextServiceImpl.getUserInContext().getUserid();
			logger.info("currentUserId : {} , bpmnTaskCandidate userID : {} ", currentUserId,
					bpmnTaskCandidate.getUser()!=null?bpmnTaskCandidate.getUser().getUserid():null);
			if (bpmnTaskCandidate.getUser()!=null&&bpmnTaskCandidate.getUser().getUserid().equals(currentUserId)) {
				return true;
			}
		}
		}catch(Exception e) {
			logger.error("Exception inside the method checkValidCandidate {}",Utils.getStackTrace(e));
		}
		return false;

	}

	/**
	 * Inserts new recipe In Table.
	 * 
	 * @param Encrypted String recipe
	 * @return Newly created Recipe Object
	 * @throws RestException the rest exception
	 */
	@Override
	@Transactional
	public Recipe createRecipeForMobile(String strRecipe) {
		try {
			Recipe recipe = new Gson().fromJson(AuthenticationCommonUtil.checkForValueDecryption(strRecipe), Recipe.class);
			if (isValidRecipe(recipe)) {
				recipe.setCreator(UserContextServiceImpl.getUserInContext());
				recipe.setRecipeId(getRecipeId(recipe));
				recipe.setCreationTime(new Date());
				recipe.setModificationTime(new Date());
				recipe.setSource(NVWorkorderConstant.MOBILE);
				recipe.setDeleted(false);
				return dao.create(recipe);
			} else {
				throw new RestException(INVALID_RECIPE_DETAILS);
			}
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e);
		}
	}

	/**
	 * Get WO Files By Recipe Mapping Id.
	 * 
	 * @param recipeMappingId recipe mapping id
	 * @return List of WO file details
	 */
	@Override
	@Transactional
	public List<WOFileDetail> getWOFilesByRecipeMappingId(Integer recipeMappingId) {
		return woFileDetailDao.findFileByRecipeMappingId(recipeMappingId);
	}

	/**
	 * Get WO Details Zip File.
	 * 
	 * @param fileName file name to download.
	 * @return byte[] for file.
	 */
	@Override
	public byte[] getWODetailsZipFile(String filePath) {
		byte[] fileData = null;
		try {
			String hdfsFilePath = ConfigUtils.getString(ConfigEnum.NV_LAYER3_WO_FILE_HDFS_BASE_FILE_PATH) + filePath;
			fileData = nvLayer3HDFSDao.getFileFromHDFS(hdfsFilePath);
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e);
		}
		return fileData;
	}

	@Override
	@Transactional
	public List<Recipe> findAllByRecipeId(List<String> recipeIdList) {
		try {
			return dao.findAllByRecipeId(recipeIdList);
		} catch (Exception e) {
			logger.error(EXCEPTION_LOGGER, ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	@Override
	public FTPDetailsWrapper validateFTPConnection(FTPDetailsWrapper ftpDetailsWrapper) {
		FTPClient ftpClient = new FTPClient();
		FTPDetailsWrapper ftpResultWrapper = new FTPDetailsWrapper();
		try {
			ftpClient.setConnectTimeout(5000);
			ftpClient.connect(ftpDetailsWrapper.getHostName(), 21);

			if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				ftpResultWrapper.setHostName(NVWorkorderConstant.SUCCESS);
				if (ftpClient.login(ftpDetailsWrapper.getUserName(), ftpDetailsWrapper.getPassword())) {
					ftpResultWrapper.setUserName(NVWorkorderConstant.SUCCESS);
					ftpResultWrapper.setPassword(NVWorkorderConstant.SUCCESS);
					if ((ftpDetailsWrapper.getDownloadFilePath() != null)
							&& ftpClient.changeWorkingDirectory(ftpDetailsWrapper.getDownloadFilePath())) {
						ftpResultWrapper.setDownloadFilePath(NVWorkorderConstant.SUCCESS);
						if (ftpClient.listFiles(ftpDetailsWrapper.getDownloadFileName()).length > 0) {
							ftpResultWrapper.setDownloadFileName(NVWorkorderConstant.SUCCESS);
						}
					}
					if ((ftpDetailsWrapper.getUploadFilePath() != null)
							&& ftpClient.changeWorkingDirectory(ftpDetailsWrapper.getUploadFilePath())) {
						ftpResultWrapper.setUploadFilePath(NVWorkorderConstant.SUCCESS);
						if (ftpClient.listFiles(ftpDetailsWrapper.getUploadFileName()).length > 0) {
							ftpResultWrapper.setUploadFileName(NVWorkorderConstant.SUCCESS);
						}
					}
				}
			}
		} catch (SocketException e) {
			logger.info("error while creating SocketException" + e);
		} catch (IOException e) {
			logger.info("error while creating IOException" + e);
		} catch (Exception e) {
			logger.info("error while creating Exception" + e);
		}
		return ftpResultWrapper;
	}

	@Override
	@Transactional
	public Response reopenRecipeById(Integer woRecipeId, Integer workorderId,boolean isToDeleteFile) {
		logger.trace("inside the method reopenRecipeById{}", woRecipeId);
		try {
			List<WORecipeMapping> recipeList = woRecipeMappingDao.getWORecipeByGWOId(workorderId);
			logger.debug("recipeList {}", recipeList);
			List<WORecipeMapping> inProgressWoRecpiList = recipeList.stream().filter(x -> x.getId().equals(woRecipeId))
					.collect(Collectors.toList());
			List<WOFileDetail> woFileDetailList = woFileDetailDao.findFileByRecipeMappingId(woRecipeId);
			logger.debug("inProgressWoRecpiList {}", inProgressWoRecpiList);
			for (WOFileDetail woFileDetail : woFileDetailList) {
				logger.debug("woFileDetail {}", woFileDetail);
				if (woFileDetail.getProcessedLogFilePath() != null
						&& woFileDetail.getProcessedLogFilePath().contains(QMDLConstant.PROCESSED_FILES)) {
					nvLayer3HDFSDao.deleteFileFromHDFSRecursively(woFileDetail.getProcessedLogFilePath());
				}
				if(isToDeleteFile) {
					woFileDetail.setIsDeleted(true);
				}
				else {
					woFileDetail.setIsProcessed(false);
				}
				woFileDetailDao.update(woFileDetail);

			}
			for (WORecipeMapping woRecipeMapping : inProgressWoRecpiList) {
				if (isToDeleteFile) {
					woRecipeMapping.setStatus(Status.NOT_STARTED);

				}
				else {
					woRecipeMapping.setStatus(Status.REOPEN);
				}
				woRecipeMapping.setFlowProcessStatus(false);
				woRecipeMapping.setProcessStartTime(null);
				woRecipeMapping.setProcessEndTime(null);				
			
				woRecipeMappingDao.update(woRecipeMapping);
				reopenWorkOrderById(woRecipeMapping.getGenericWorkorder().getId(), recipeList);
				nvHbaseService.deletePreviousDataForLayer3(workorderId, woRecipeMapping.getId());
			}
			return Response.ok(SUCCESS_JSON).build();
		} catch (DaoException de) {
			logger.error("DaoException in method reopenRecipeById {} msg{}", woRecipeId, de.getMessage());
		} catch (RestException e) {
			logger.error("RestException in method reopenRecipeById {} msg{}", woRecipeId, e.getMessage());

		}
		return Response.ok(FAILURE_JSON).build();

	}

	private GenericWorkorder reopenWorkOrderById(Integer workOrderId, List<WORecipeMapping> recipeList) {
		logger.trace("Inside the methdo reopenWorkOrderById {}", workOrderId);
		try {
			GenericWorkorder workOrder = genericWorkorderDao.findByPk(workOrderId);
			if (workOrder.getStatus().equals(GenericWorkorder.Status.COMPLETED)) {
				logger.info("inside the completed workorder condition {}", workOrder.getStatus());
				BpmnTaskCandidate bpmntaskCanditate = getBPMNTaskCandidate(workOrder.getWorkorderId());
				logger.info("bpmntaskCanditate {}", bpmntaskCanditate);
				if (bpmntaskCanditate != null && bpmntaskCanditate.getUser() != null) {
					createBPMNWorkorder(workOrder.getWorkorderId(), workOrder.getTemplateType(),
							bpmntaskCanditate.getUser());

				}
				updateReportConfigForWorkorder(workOrder);

			}
			updateWorkorder(workOrder, workOrder.getGwoMeta(), recipeList);
			logger.info("Workorder Successfully Reopen for id {}", workOrderId);
			return workOrder;
		}

		catch (NumberFormatException ne) {
			logger.error("NumberFormatException to reopen workorder id {} message {}", workOrderId, ne.getMessage());

		} catch (DaoException de) {
			logger.error("DaoException to reopen workorder id {} message {}", workOrderId, de.getMessage());

		} catch (Exception de) {
			logger.error("Exception to reopen workorder id {} message {}", workOrderId, de.getMessage());

		}

		return null;

	}

	private void updateReportConfigForWorkorder(GenericWorkorder workOrder) {
		if (workOrder != null) {
			Map<String, String> gwoMeta = workOrder.getGwoMeta();
			if (gwoMeta != null && gwoMeta.get(NVWorkorderConstant.REPORT_INSTANCE_ID) != null) {
				AnalyticsRepository repository = analyticsRepositoryDao
						.findByPk(Integer.valueOf(gwoMeta.get(NVWorkorderConstant.REPORT_INSTANCE_ID)));
				if (repository != null) {
					repository.setDeleted(true);
					analyticsRepositoryDao.update(repository);
				}
				gwoMeta.remove(NVWorkorderConstant.REPORT_INSTANCE_ID);
				workOrder.setGwoMeta(gwoMeta);

			}
		}
	}

	private void updateWorkorder(GenericWorkorder workOrder, Map<String, String> gwoMeta,
			List<WORecipeMapping> recipeList) {
		if (recipeList != null && !recipeList.isEmpty()) {
			Integer completeRecipe = recipeList.stream()
					.filter(x -> x.getStatus()
							.equals(com.inn.foresight.module.nv.workorder.model.WORecipeMapping.Status.COMPLETED))
					.collect(Collectors.toList()).size();
			logger.info("completeRecipe {} ,recipeList.size(){}", completeRecipe, recipeList.size());
			workOrder.setCompletionPercentage(Utils.getPercentage(completeRecipe, recipeList.size()));
		}
		workOrder.setGwoMeta(gwoMeta);
		workOrder.setStatus(com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.Status.REOPEN);
		workOrder.setModificationTime(new Date());
		genericWorkorderDao.update(workOrder);
	}

	private void createBPMNWorkorder(String workorderName, TemplateType templateType, User user) {
		BPMNWorkorder workorder = new BPMNWorkorder();

		BPMNWOTemplate woTemplate = bpmnWOTemplateDao.getDeployedBPMWOTemplateByNameAndCategory(templateType.getValue(),
				WOCategory.NV_WORKORDER.getValue());

		workorder.setWorkorderNo(workorderName);
		workorder.setBpmnWOTemplate(woTemplate);
		workorder.setBpmnWOCategory(woTemplate.getBpmnWOCategory());
		workorder.setProgressPCT(NumberUtils.INTEGER_ZERO);
		workorder.setWoCategory(WOCategory.NV_WORKORDER);
		workorder.setDeleted(false);

		List<BPMNWorkorder> list = new ArrayList<>();
		list.add(workorder);
		activitiService.invokeBPMNWorkorderMultipleProcess(list, getWorkorderMeta(user.getUserName()));
	}

}
