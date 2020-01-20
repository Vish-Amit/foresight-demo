package com.inn.foresight.module.nv.report.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.report.service.INVInBuildingReportService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.service.ISSVTReportService;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;

@Service("IBCWalkTestReportServiceImp")
public class IBCWalkTestReportServiceImp implements IIBCWalkTestReportService{

	private Logger logger = LogManager.getLogger(IBCWalkTestReportServiceImp.class);

	@Autowired
	private INVInBuildingReportService nvInBuildingReportService;
	@Autowired 
	private ISSVTReportService iSSVTReportService;
	@Autowired
	private IReportService reportService;
	
	@Autowired
	private SystemConfigurationDao iSystemConfigurationDao;
	
	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;
	@Autowired
	private IAnalyticsRepositoryDao analyticsrepositoryDao;

	@Override
	public Response execute(String json) {
		Integer analyticsrepoId = null;
        logger.info("Going to generate IBC WalkTest Report {}",json);
		try {
			Map<String, Object> jsonMap = reportService.getJsonDataMap(json);
			Integer workorderId = (Integer) jsonMap.get(ReportConstants.WORKORDER_ID);
			GenericWorkorder workorderObj = iGenericWorkorderDao.findByPk(workorderId);
			String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.SSVT
					+ ReportConstants.FORWARD_SLASH + workorderObj.getTemplateType().name()
					+ ReportConstants.FORWARD_SLASH + workorderObj.getGwoMeta().get(ReportConstants.SITE_ID)
					+ ReportConstants.FORWARD_SLASH;
			ReportUtil.createDirectory(filePath);
			analyticsrepoId = (Integer) jsonMap.get(ReportConstants.ANALYTICS_REPOSITORY_ID);
			String mapJson = iSystemConfigurationDao
					.getSystemConfigurationByType(ReportConstants.NV_IBC_WALKTEST_REPORT_RECIPEID_MAP)
					.get(ReportConstants.INDEX_ZER0).getValue();
			Map<String, String> recipeIDMap = ReportUtil.convertCSVStringToMap(mapJson);
			Map<String, Map<String, List<String>>> recipeWiseIDMap = iSSVTReportService
					.getRecipeMapForRecipeId(workorderId, recipeIDMap);
			Map<String, List<String>> recipeOperatorMap = getRecipeOperatorMap(recipeWiseIDMap);
			List<String> recipeList = recipeOperatorMap.get(QMDLConstant.RECIPE);
			List<Integer> idList = recipeList.stream().mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
			File file = nvInBuildingReportService.createIBReportForRecipeId(idList,
					recipeOperatorMap.get(QMDLConstant.OPERATOR), workorderId, null, analyticsrepoId);
			if (file != null) {
				reportService.saveFileAndUpdateStatus(analyticsrepoId, filePath, workorderObj, file, file.getName(),
						NVWorkorderConstant.WALK_TEST_REPORT_INSTACE_ID);
			} else {
				analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Something Went Wrong",
						progress.Failed, null);
			}
		} catch (Exception e) {
			logger.error("Exception in generating IBC WalkTest report {}",Utils.getStackTrace(e));
			analyticsrepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId, null, "Something Went Wrong",
					progress.Failed, null);  
		}
		return null;
	}

	private Map<String, List<String>> getRecipeOperatorMap(Map<String, Map<String, List<String>>> recipeWiseIDMap) {
		Map<String, List<String>> recipeOperatorMap = new HashMap<>();
		for (Entry<String, Map<String, List<String>>> entry : recipeWiseIDMap.entrySet()) {
			Map<String, List<String>> map = entry.getValue();
			if (recipeOperatorMap.containsKey(QMDLConstant.RECIPE)) {
				List<String> recipeList = recipeOperatorMap.get(QMDLConstant.RECIPE);
				recipeList.addAll(map.get(QMDLConstant.RECIPE));
				recipeOperatorMap.put(QMDLConstant.RECIPE, recipeList);
			} else {
				recipeOperatorMap.put(QMDLConstant.RECIPE, map.get(QMDLConstant.RECIPE));

			}
			
			if(recipeOperatorMap.containsKey(QMDLConstant.OPERATOR)){
				List<String> operatorList=recipeOperatorMap.get(QMDLConstant.OPERATOR);
				operatorList.addAll(map.get(QMDLConstant.OPERATOR));
				recipeOperatorMap.put(QMDLConstant.OPERATOR, operatorList);
			}
			else{
				recipeOperatorMap.put(QMDLConstant.OPERATOR, map.get(QMDLConstant.OPERATOR));

			}
		}
		return recipeOperatorMap;
	}

}
