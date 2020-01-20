package com.inn.foresight.module.nv.workorder.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.bpmn.dao.IBpmnTaskCandidateDao;
import com.inn.bpmn.model.BpmnTaskCandidate;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.wrapper.WorkorderCountWrapper;
import com.inn.foresight.module.nv.dashboard.utils.NVDashboardConstants;
import com.inn.foresight.module.nv.reportgeneration.wrapper.NVReportWrapper;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.service.IWorkorderDashboardService;
import com.inn.foresight.module.nv.workorder.utils.WorkorderDashboardUtils;

@Service("WorkorderDashboardServiceImpl")
public class WorkorderDashboardServiceImpl extends WorkorderDashboardUtils implements IWorkorderDashboardService {

	@Autowired
	IGenericWorkorderDao iGenericWorkorderDao;

	@Autowired
	IWORecipeMappingDao iWORecipeMappingDao;

	@Autowired
	private IBpmnTaskCandidateDao bpmnTaskCandidateDao;

	/** The logger. */
	private Logger logger = LogManager.getLogger(WorkorderDashboardServiceImpl.class);

	@Override
	public Map<String, Object> getWOCount(String type, Long currentDate, String geographyLevel, Integer geographyId) {
		if (TYPE_ADHOC.equalsIgnoreCase(type)) {
			return getADHOCWorkorderDayWiseCount(currentDate, geographyLevel, geographyId);
		} else if (TYPE_WORKORDER.equalsIgnoreCase(type)) {
			return getAllWorkorderCount(currentDate, geographyLevel, geographyId);
		}
		return null;
	}

	@Override
	public Map<String, Object> getAllWorkorderCount(Long currentDate, String geographyLevel, Integer geographyId) {
		Map<String, Object> woMap = new HashMap<>();
		Date endDate = new Date(currentDate);
		Date startDate = DateUtils.addDays(endDate, NVDashboardConstants.MINUSSIX);
		try {

			Long driveWOCount = iGenericWorkorderDao.getAllWorkorderCount(geographyLevel, geographyId, startDate,
					endDate, getAllDriveTemplateList());
			Long ibWOCount = iGenericWorkorderDao.getAllWorkorderCount(geographyLevel, geographyId, startDate, endDate,
					getAllIBTemplateList());
			woMap.put(DRIVE_COUNT, driveWOCount);
			woMap.put(IN_BUILDING_COUNT, ibWOCount);
		} catch (DaoException e) {
			logger.error("DaoException in getAllWorkorderCount: {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}

		return woMap;
	}

	@Override
	public Map<String, Object> getADHOCWorkorderDayWiseCount(Long currentDate, String geographyLevel,
			Integer geographyId) {
		Map<String, Object> woMap = new HashMap<>();
		Date endDate = new Date(currentDate);
		Date startDate = DateUtils.addDays(endDate, NVDashboardConstants.MINUSSIX);
		try {
			List<WorkorderCountWrapper> deriveCountList = iGenericWorkorderDao.getADHOCWorkorderDayWiseCount(
					geographyLevel, geographyId, startDate, endDate, getAdhocDriveTemplateList());
			List<WorkorderCountWrapper> buildingCountList = iGenericWorkorderDao.getADHOCWorkorderDayWiseCount(
					geographyLevel, geographyId, startDate, endDate, getAdhocIBTemplateList());
			woMap.put(TYPE_INBUILDING, getMapFromWrapper(buildingCountList));
			woMap.put(TYPE_DRIVE, getMapFromWrapper(deriveCountList));
		} catch (DaoException e) {
			logger.error("DaoException in getADHOCWorkorderDayWiseCount: {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return woMap;
	}

	@Override
	public Map<String, Map<String, Long>> getWorkorderCountByStatus(String type, String templateType, Long currentDate,
			String geographyLevel, Integer geographyId) {
		Date endDate = new Date(currentDate);
		Date startDate = DateUtils.addDays(endDate, NVDashboardConstants.MINUSSIX);
		List<WorkorderCountWrapper> woCountList = null;
		Map<String, Map<String, Long>> woMap = new HashMap<>();
		try {
			if (TYPE_INBUILDING.equalsIgnoreCase(type)) {
				if (TEMPLATE_TYPE_ALL.equalsIgnoreCase(templateType)) {
					woCountList = iGenericWorkorderDao.getWOCountByTemplateTypeAndStatus(geographyLevel, geographyId,
							startDate, endDate, getAllIBTemplateList(), getWOStatusList());

					woMap = getWOTemplateMapFromWrapper(woCountList);
				} else {
					woCountList = iGenericWorkorderDao.getDayWiseWOCountByStatus(geographyLevel, geographyId, startDate,
							endDate, getTemplateListFromString(templateType), getWOStatusList());
					woMap = getWODateMapFromWrapper(woCountList);
				}
			} else if (TYPE_DRIVE.equalsIgnoreCase(type)) {
				if (TEMPLATE_TYPE_ALL.equalsIgnoreCase(templateType)) {
					woCountList = iGenericWorkorderDao.getWOCountByTemplateTypeAndStatus(geographyLevel, geographyId,
							startDate, endDate, getAllDriveTemplateList(), getWOStatusList());
					woMap = getWOTemplateMapFromWrapper(woCountList);
				} else {
					woCountList = iGenericWorkorderDao.getDayWiseWOCountByStatus(geographyLevel, geographyId, startDate,
							endDate, getTemplateListFromString(templateType), getWOStatusList());
					woMap = getWODateMapFromWrapper(woCountList);
				}
			}
		} catch (DaoException e) {
			logger.error("DaoException in getWorkorderCountByStatus: {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return woMap;
	}

	@Override
	public Map<String, Object> getWorkorderCountByAssignedType(String assignedType, Long currentDate,
			String geographyLevel, Integer geographyId) {
		Map<String, Object> woMap = new HashMap<>();
		Date endDate = new Date(currentDate);
		Date startDate = DateUtils.addDays(endDate, NVDashboardConstants.MINUSSIX);
		try {
			if (ASSIGNED_TYPE_USER.equalsIgnoreCase(assignedType)) {
				List<WorkorderCountWrapper> buildingCountList = iGenericWorkorderDao.getDayWiseAssignedWOCount(
						geographyLevel, geographyId, startDate, endDate, getAllIBTemplateList());
				List<WorkorderCountWrapper> driveList = iGenericWorkorderDao.getDayWiseAssignedWOCount(geographyLevel,
						geographyId, startDate, endDate, getAllDriveTemplateList());
				woMap.put(TYPE_INBUILDING, getMapFromWrapper(buildingCountList));
				woMap.put(TYPE_DRIVE, getMapFromWrapper(driveList));
			} else if (ASSIGNED_TYPE_DEVICE.equalsIgnoreCase(assignedType)) {
				List<WorkorderCountWrapper> list = iGenericWorkorderDao.getDayWiseAssignedWOCount(geographyLevel,
						geographyId, startDate, endDate, getWOTemplateAssignedToDevice());
				woMap.put(ASSIGNED_TYPE_DEVICE, getMapFromWrapper(list));
			}
		} catch (DaoException e) {
			logger.error("DaoException in getWorkorderCountByAssignedType: {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return woMap;
	}

	@Override
	public Map<String, Long> getDueWorkorderDayWiseCount(Long currentDate, String geographyLevel, Integer geographyId) {
		Date endDate = new Date(currentDate);
		Date startDate = DateUtils.addDays(endDate, NVDashboardConstants.MINUSSIX);
		List<WorkorderCountWrapper> list;
		try {
			list = iGenericWorkorderDao.getDueWorkorderDayWiseCount(geographyLevel, geographyId, startDate, endDate,
					getDueWOStatusList());
		} catch (DaoException e) {
			throw new RestException(ExceptionUtils.getMessage(e));
		}
		return getMapFromWrapper(list);
	}

	@Override
	public List<WorkorderCountWrapper> getDueWorkorderList(Long currentDate, String geographyLevel, Integer geographyId,
			Integer llimit, Integer ulimit) {
		Date endDate = new Date(currentDate);
		Date startDate = DateUtils.addDays(endDate, NVDashboardConstants.MINUSSIX);
		try {
			return iGenericWorkorderDao.getDueWorkorderList(geographyLevel, geographyId, startDate, endDate,
					getDueWOStatusList(), llimit, ulimit);
		} catch (DaoException e) {
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	}

	@Override
	@Transactional
	public NVReportWrapper getWorkorderReportData(Long currentDate, String geographyLevel, Integer geographyId) {
		Date date = new Date(currentDate);
		try {
			List<GenericWorkorder> workorderList = iGenericWorkorderDao.getWorkorderListForReport(geographyLevel,
					geographyId, date);
			List<WorkorderCountWrapper> workorderReportWrappper = new ArrayList<>();

			logger.info("Done GenericWorkorder list size for report : {}", workorderList.size());
			if (!workorderList.isEmpty()) {
				List<BpmnTaskCandidate> bpmnTaskCandidateList = bpmnTaskCandidateDao.getBPMNTaskCandidateListByWONoList(
						getWorkorderIdListFromGWO(workorderList));
				logger.info("bpmnTaskCandidateList list size:{}"+bpmnTaskCandidateList.size());
				Map<String, BpmnTaskCandidate> candiateMap = getBpmnTaskCandidateMap(bpmnTaskCandidateList);
				for (GenericWorkorder genericWorkorder : workorderList) {
					BpmnTaskCandidate candidate = candiateMap.get(genericWorkorder.getWorkorderId());
					workorderReportWrappper.add(getWorkorderCountWrapper(genericWorkorder, candidate));
				}
			}
			logger.info("Done WorkorderCountWrapper list size for report: {}", workorderReportWrappper.size());
			String workorderCsvData = WorkorderDashboardUtils.getCsvDataFromWrapper(workorderReportWrappper);
			NVReportWrapper reportWrapper = new NVReportWrapper();
			reportWrapper.setFile(workorderCsvData.getBytes());
			reportWrapper.setReportName("Workorder_Report_" + WorkorderDashboardUtils.getDateString(date,DD_MM_YY) + ".csv");
			return reportWrapper;

		} catch (DaoException e) {
			logger.error("DaoException in getWorkorderCount: {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * Creates NvWorkordrWrapper From GenericWorkorder and BpmnTaskCandidate object.
	 * 
	 * @param genericWorkorder
	 *            the generic workorder
	 * @param bpmnTaskCandidate
	 *            the bpmn task candidate
	 * @return NVWorkorderWrapper object
	 */
	private WorkorderCountWrapper getWorkorderCountWrapper(GenericWorkorder genericWorkorder,
			BpmnTaskCandidate bpmnTaskCandidate) {
		WorkorderCountWrapper wrapper = new WorkorderCountWrapper();
		wrapper.setWorkorderId(genericWorkorder.getWorkorderId());
		wrapper.setWorkorderName(genericWorkorder.getWorkorderName());
		wrapper.setTemplateType(genericWorkorder.getTemplateType());
		wrapper.setCompletionPercentage(genericWorkorder.getCompletionPercentage());
		wrapper.setStatus(genericWorkorder.getStatus());
		wrapper.setCreationTime(getDateString(genericWorkorder.getCreationTime(),CAPS_DD_MM_YY));
		
		if(genericWorkorder.getGeographyl1()!=null) {
		wrapper.setGeographyL1Name(genericWorkorder	.getGeographyl1()
													.getName());
		}
		if(genericWorkorder.getGeographyl2()!=null) {
		wrapper.setGeographyL2Name(genericWorkorder	.getGeographyl2()
													.getName());
		}
		if(genericWorkorder.getGeographyl3()!=null) {
		wrapper.setGeographyL3Name(genericWorkorder	.getGeographyl3()
													.getName());
		}
		if(genericWorkorder.getGeographyl4()!=null) {
		wrapper.setGeographyL4Name(genericWorkorder	.getGeographyl4()
													.getName());
		}

		if (bpmnTaskCandidate != null) {
			wrapper.setAssignedTo(getUserName(bpmnTaskCandidate.getUser()));

		}
		return wrapper;
	}

}
