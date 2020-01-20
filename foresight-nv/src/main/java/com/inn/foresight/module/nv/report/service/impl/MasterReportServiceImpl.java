package com.inn.foresight.module.nv.report.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.report.service.ILiveDriveReportService;
import com.inn.foresight.module.nv.report.service.IMasterReportService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.service.IStationaryReportService;
import com.inn.foresight.module.nv.report.utils.MergePDF;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.wrapper.RecipeMappingWrapper;
import com.inn.foresight.module.nv.report.wrapper.RemarkReportWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

@Service("MasterReportServiceImpl")
public class MasterReportServiceImpl implements IMasterReportService {

	private static final Logger logger = LogManager.getLogger(MasterReportServiceImpl.class);

	@Autowired
	private IAnalyticsRepositoryDao analyticsrepositoryDao;

	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;

	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;

	@Autowired
	private IStationaryReportService stationaryReportService;

	@Autowired
	private ILiveDriveReportService liveDriveReportService;

	@Autowired
	private IReportService reportService;

	@Override
	public Response execute(String json) {
		logger.info("Inside execute method for Mater Report with josn {} ", json);
		Integer analyticrepositoryId = null;
		try {
			Map<String, Object> jsonMap = reportService.getJsonDataMap(json);
			List<Integer> workorderIds = getWorkOrderIdForMasterReport(jsonMap);
			if (Utils.isValidList(workorderIds)) {
				analyticrepositoryId = (Integer) jsonMap.get(ReportConstants.ANALYTICS_REPOSITORY_ID);
				if(workorderIds != null  && workorderIds.get(ReportConstants.INDEX_ZER0) != null)
				{
				GenericWorkorder genericWorkorder = iGenericWorkorderDao
						.findByPk(workorderIds.get(ReportConstants.INDEX_ZER0));
				String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.MASTER
						+ ReportConstants.FORWARD_SLASH;
				boolean isFilesProcessed = reportService.getFileProcessedStatusForWorkorders(workorderIds);
				if (isFilesProcessed) {
					Map<String, RecipeMappingWrapper> map = nvLayer3DashboardService
							.getDriveRecipeDetailForMasterReport(workorderIds);

					Map<String, RecipeMappingWrapper> finalMap = getCategoryWiseRecipeMappinWrappermap(map);
					logger.info("finalMap : {}", finalMap);
					List<File> files = getAllFilesForMasterReport(workorderIds, genericWorkorder, filePath, finalMap);
					if (files.size() > ReportConstants.INDEX_ZER0) {
						File file = getMasteReport(analyticrepositoryId, files, genericWorkorder.getWorkorderId(),
								filePath);
						String hdfsFilePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH_HDFS)
								+ ReportConstants.MASTER + ReportConstants.FORWARD_SLASH;
						logger.info("HDFS file path is : {}", hdfsFilePath);
						reportService.genrateDocxReport(hdfsFilePath, file.getAbsolutePath(),
								ReportUtil.getFileNameForDoc(genericWorkorder.getWorkorderId(), analyticrepositoryId));
						if (workorderIds.size() > ReportConstants.INDEX_ONE) {
							return reportService.saveFileAndUpdateStatus(analyticrepositoryId, hdfsFilePath, null, file,
									null, NVWorkorderConstant.REPORT_INSTACE_ID);
						} else {
							return reportService.saveFileAndUpdateStatus(analyticrepositoryId, hdfsFilePath,
									genericWorkorder, file, null, NVWorkorderConstant.REPORT_INSTACE_ID);
						}
					}
					return Response.ok(ForesightConstants.FAILURE_JSON).build();
				}
				}
			}

		} catch (Exception e) {
			logger.error("Error inside the method execute method in MasterReportServiceImpl() {} ",
					Utils.getStackTrace(e));
			analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticrepositoryId, null, e.getMessage(),
					progress.Failed, null);
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	private List<Integer> getWorkOrderIdForMasterReport(Map<String, Object> jsonMap) {
		try {
			logger.info("inside the method getWorkOrderIdForMasterReport {}",jsonMap);
			if (jsonMap.get(ReportConstants.WORKORDER_ID)!=null) {
				return Arrays.asList( (Integer) jsonMap.get(ReportConstants.WORKORDER_ID));
			} else {
				return getWorkOrderIdForBRTI(jsonMap);
			}
		} catch (Exception e) {
			logger.error("Exception inside them getWorkOrderIdForMasterReport {}",Utils.getStackTrace(e));
		}
		return null;
	}

	private List<Integer> getWorkOrderIdForBRTI(Map<String, Object> jsonMap) {

		try {

			List<Integer> workOrderIds = jsonMap.get(ReportConstants.WORKORDER_IDS) != null
					? (ArrayList<Integer>) jsonMap.get(ReportConstants.WORKORDER_IDS)
					: null;
			logger.info("finding the workorder Id from workOrderIDs list {} ", workOrderIds);
			return workOrderIds;
		} catch (Exception e) {
			logger.error("Unable to find the workorder list Id form json map {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	private List<File> getAllFilesForMasterReport(List<Integer> workorderIds, GenericWorkorder genericWorkorder,
			String filePath, Map<String, RecipeMappingWrapper> finalMap) throws IOException {
		List<File> files = new ArrayList<>();
		for (Entry<String, RecipeMappingWrapper> wrapper : finalMap.entrySet()) {
			if (wrapper != null && wrapper.getKey() != null) {
				logger.info("Inside getAllFilesForMasterReport Category in Wrapper Is {} ", wrapper.getKey());
				Map<String, List<String>> recipeOperatorMap = getRecipeOperatorMap(wrapper.getValue());
				File file = getFileByRecipeType(recipeOperatorMap, wrapper.getKey(), workorderIds, genericWorkorder);
				if (file != null) {
					files.add(file);
				}
			}
		}
		reportService.generateRemarkReport(workorderIds.get(0), filePath, files,
				ConfigUtils.getString(NVConfigUtil.MASTER_REPORT_INTIAL_PAGE), new RemarkReportWrapper());
		if (files.size() > ReportConstants.INDEX_ZER0) {
			files.add(0, proceedToCreateIntialAndLastPage(genericWorkorder, ReportConstants.MASTER_INITIAL_PAGE,
					ReportConstants.INITIAL_PAGE, filePath));
			files.add(proceedToCreateIntialAndLastPage(genericWorkorder, ReportConstants.MASTER_THANK_YOU_PAGE,
					ReportConstants.THANK_YOU, filePath));
			logger.info("Inside getAllFilesForMasterReport merging FIles => {}", files);
		}
		return files;
	}

	private Map<String, RecipeMappingWrapper> getCategoryWiseRecipeMappinWrappermap(
			Map<String, RecipeMappingWrapper> map) {
		Map<String, RecipeMappingWrapper> finalMap = new HashMap<>();

		for (Entry<String, RecipeMappingWrapper> wrapper1 : map.entrySet()) {
			if (wrapper1.getKey().equalsIgnoreCase(ReportConstants.RECIPE_CATEGORY_STATIONARY)) {
				finalMap.put(wrapper1.getKey(), map.get(wrapper1.getKey()));
			} else {
				populateDriveCallRecipe(finalMap, wrapper1);
			}
		}
		logger.info("Inside getCategoryWiseRecipeMappinWrappermap returning Map Size : => {} ", finalMap.size());
		return finalMap;
	}

	private void populateDriveCallRecipe(Map<String, RecipeMappingWrapper> finalMap,
			Entry<String, RecipeMappingWrapper> wrapper1) {
		if (wrapper1.getKey().equalsIgnoreCase(ReportConstants.RECIPE_CATEGORY_DRIVE)
				|| wrapper1.getKey().equalsIgnoreCase(ReportConstants.CALL_SMALL)) {
			RecipeMappingWrapper oldwrapper = finalMap.get(ReportConstants.RECIPE_CATEGORY_DRIVE);
			if (oldwrapper != null) {
				List<String> list = oldwrapper.getRecpiList();
				list.addAll(wrapper1.getValue() != null ? wrapper1.getValue().getRecpiList() : null);
				oldwrapper.setRecpiList(list);
				finalMap.put(ReportConstants.RECIPE_CATEGORY_DRIVE, oldwrapper);
			} else {
				finalMap.put(ReportConstants.RECIPE_CATEGORY_DRIVE, wrapper1.getValue());
			}
		}
	}

	private Map<String, List<String>> getRecipeOperatorMap(RecipeMappingWrapper wrapper) {
		Map<String, List<String>> recipeOperatorMap = new HashMap<>();
		if (wrapper != null) {
			recipeOperatorMap.put(QMDLConstant.RECIPE, wrapper.getRecpiList());
			recipeOperatorMap.put(QMDLConstant.OPERATOR, wrapper.getOperatorList());
			return recipeOperatorMap;
		}
		return recipeOperatorMap;
	}

	private File proceedToCreateIntialAndLastPage(GenericWorkorder genericWorkorder, String mainFileName, String type,
			String filePath) {
		try {
			String reportAssetRepo = ConfigUtils.getString(NVConfigUtil.MASTER_REPORT_INTIAL_PAGE);
			logger.info("REPORT_ASSET_REPO{}", reportAssetRepo);
			Map<String, Object> imageMap = new HashMap<>();
			imageMap.put(ReportConstants.SUBREPORT_DIR, reportAssetRepo);
			imageMap.put(ReportConstants.IMAGE_PARAM_HEADER_BG, reportAssetRepo + ReportConstants.IMAGE_HEADER_BG);
			imageMap.put(ReportConstants.IMAGE_PARAM_HEADER_LOG, reportAssetRepo + ReportConstants.IMAGE_HEADER_LOG);
			imageMap.put(ReportConstants.IMAGE_PARAM_SCREEN_LOG, reportAssetRepo + ReportConstants.IMAGE_SCREEN_LOGO);
			imageMap.put(ReportConstants.IMAGE_PARAM_SCREEN_BG, reportAssetRepo + ReportConstants.IMAGE_SCREEN_BG);
			imageMap.put(ReportConstants.IMAGE_PARAM_THANK_YOU, reportAssetRepo + ReportConstants.THANK_YOU_IMG);
			imageMap.put(ReportConstants.DATE, ReportUtil.parseDateToString(
					ReportConstants.DATE_FORMAT_DD_SP_MM_YY, genericWorkorder.getModificationTime()));
			String destinationFileName = ReportUtil.getFileName(genericWorkorder.getWorkorderId() + type,
					genericWorkorder.getId(), filePath);
			JasperRunManager.runReportToPdfFile(reportAssetRepo + mainFileName, destinationFileName, imageMap);
			return ReportUtil.getIfFileExists(destinationFileName);

		} catch (JRException e) {
			logger.error("Exception inside the method proceedToCreateIntialAndLastPage{} ", Utils.getStackTrace(e));
		}
		return null;

	}

	private File getMasteReport(Integer analyticrepositoryId, List<File> files, String woName, String filePath) {
		String destinationFilePath = ReportUtil.getFileName(woName, analyticrepositoryId, filePath);
		MergePDF.mergeFiles(destinationFilePath, files);
		return ReportUtil.getIfFileExists(destinationFilePath);
	}

	private File getFileByRecipeType(Map<String, List<String>> recipeOperatorMap, String recipeType,
			List<Integer> workorderIds, GenericWorkorder genericWorkorder) throws IOException {
		logger.info("Inside method getFileByRecipeType fro recipeType {} ", recipeType);
		File file = null;
		try {
			if (recipeType.equalsIgnoreCase(ReportConstants.RECIPE_CATEGORY_DRIVE)
					|| recipeType.equalsIgnoreCase(ReportConstants.CALL_SMALL)) {
				logger.info("Inside getFileByRecipeType recipeOperatorMap is {} ,for category {} ", recipeOperatorMap,
						recipeType);
				return liveDriveReportService.getLiveDriveReportForMasterReport(workorderIds, recipeOperatorMap, true);
			} else if (recipeType.equalsIgnoreCase(ReportConstants.RECIPE_CATEGORY_STATIONARY)) {
				logger.info("Inside getFileByRecipeType recipeOperatorMap is {} ,for category {} ", recipeOperatorMap,
						recipeType);
				return stationaryReportService.getStationaryTestReportForMasterReport(workorderIds, genericWorkorder,
						recipeOperatorMap);
			}

		} catch (Exception e) {
			logger.error("Exception inside method getFileByRecipeType {} ", e.getMessage());
		}

		return file;
	}

}
