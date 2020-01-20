package com.inn.foresight.module.nv.workorder.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.inn.bpmn.model.BpmnTaskCandidate;
import com.inn.commons.Symbol;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.Status;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.core.workorder.wrapper.WorkorderCountWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.product.um.user.model.User;

public class WorkorderDashboardUtils implements NVWorkorderConstant {

	/** WO+Adhoc. */
	public static List<TemplateType> getAllDriveTemplateList() {
		List<TemplateType> templateTypeList = new ArrayList<>();
		templateTypeList.add(TemplateType.NV_COMPLAINTS);
		templateTypeList.add(TemplateType.NV_STATIONARY);
		templateTypeList.add(TemplateType.NV_OPENDRIVE);
		templateTypeList.add(TemplateType.NV_BRTI);
		templateTypeList.add(TemplateType.NV_CLOT);
		templateTypeList.add(TemplateType.NV_LIVE_DRIVE);
		templateTypeList.add(TemplateType.NV_SSVT);
		templateTypeList.add(TemplateType.NV_BENCHMARK);
		templateTypeList.add(TemplateType.NV_ADHOC_LD);
		templateTypeList.add(TemplateType.NV_ADHOC_BRTI_DRIVE);
		templateTypeList.add(TemplateType.NV_ADHOC_BRTI_ST);
		templateTypeList.add(TemplateType.NV_ADHOC_OD);
		return templateTypeList;
	}

	public static List<TemplateType> getWODriveTemplateList() {
		List<TemplateType> templateTypeList = new ArrayList<>();
		templateTypeList.add(TemplateType.NV_COMPLAINTS);
		templateTypeList.add(TemplateType.NV_STATIONARY);
		templateTypeList.add(TemplateType.NV_OPENDRIVE);
		templateTypeList.add(TemplateType.NV_BRTI);
		templateTypeList.add(TemplateType.NV_CLOT);
		templateTypeList.add(TemplateType.NV_LIVE_DRIVE);
		templateTypeList.add(TemplateType.NV_SSVT);
		templateTypeList.add(TemplateType.NV_BENCHMARK);
		return templateTypeList;
	}

	public static List<TemplateType> getWOIBTemplateList() {
		List<TemplateType> templateTypeList = new ArrayList<>();
		templateTypeList.add(TemplateType.NV_INBUILDING);
		return templateTypeList;
	}

	public static List<TemplateType> getAllIBTemplateList() {
		List<TemplateType> templateTypeList = new ArrayList<>();
		templateTypeList.add(TemplateType.NV_INBUILDING);
		templateTypeList.add(TemplateType.NV_IB_BENCHMARK);
		templateTypeList.add(TemplateType.NV_ADHOC_IB);
		return templateTypeList;
	}

	public static List<TemplateType> getAdhocDriveTemplateList() {
		List<TemplateType> templateTypeList = new ArrayList<>();
		templateTypeList.add(TemplateType.NV_ADHOC_LD);
		templateTypeList.add(TemplateType.NV_ADHOC_BRTI_DRIVE);
		templateTypeList.add(TemplateType.NV_ADHOC_BRTI_ST);
		templateTypeList.add(TemplateType.NV_ADHOC_OD);
		return templateTypeList;
	}

	public static List<TemplateType> getAdhocIBTemplateList() {
		List<TemplateType> templateTypeList = new ArrayList<>();
		templateTypeList.add(TemplateType.NV_ADHOC_IB);
		templateTypeList.add(TemplateType.NV_IB_BENCHMARK);
		return templateTypeList;
	}

	public static List<Status> getWOStatusList() {
		List<Status> statusList = new ArrayList<>();
		statusList.add(Status.NOT_STARTED);
		statusList.add(Status.INPROGRESS);
		statusList.add(Status.COMPLETED);
		return statusList;
	}

	public static List<Status> getDueWOStatusList() {
		List<Status> statusList = new ArrayList<>();
		statusList.add(Status.NOT_STARTED);
		statusList.add(Status.INPROGRESS);
		return statusList;
	}

	public static List<TemplateType> getWOTemplateAssignedToDevice() {
		List<TemplateType> templateTypeList = new ArrayList<>();
		templateTypeList.add(TemplateType.NV_STEALTH);
		return templateTypeList;
	}

	public static String getDateString(Date date,String formate) {
		return new SimpleDateFormat(formate).format(date);
	}

	public static Map<String, Long> getMapFromWrapper(List<WorkorderCountWrapper> list) {
		Map<String, Long> woMap = new HashMap<>();

		if (list != null) {
			woMap = list.stream()
						.collect(Collectors.toMap(WorkorderCountWrapper::getDate, WorkorderCountWrapper::getCount));
		}
		return woMap;
	}

	public static Map<String, Map<String, Long>> getWOTemplateMapFromWrapper(List<WorkorderCountWrapper> woCountList) {
		Map<String, Map<String, Long>> woMap = new HashMap<>();

		Map<TemplateType, List<WorkorderCountWrapper>> groupByTemplate = woCountList.stream()
																					.collect(Collectors.groupingBy(
																							WorkorderCountWrapper::getTemplateType));
		groupByTemplate.forEach((key, value) -> {
			Map<String, Long> subMap = new HashMap<>();
			value	.parallelStream()
					.forEach(val -> {
						subMap.put(val	.getStatus()
										.getValue(),
								val.getCount());
					});
			woMap.put(key.toString(), subMap);
		});
		return woMap;
	}

	public static Map<String, Map<String, Long>> getWODateMapFromWrapper(List<WorkorderCountWrapper> woCountList) {
		Map<String, Map<String, Long>> woMap = new HashMap<>();

		Map<String, List<WorkorderCountWrapper>> groupByTemplate = woCountList	.stream()
																				.collect(Collectors.groupingBy(
																						WorkorderCountWrapper::getDate));
		groupByTemplate.forEach((key, value) -> {
			Map<String, Long> subMap = new HashMap<>();
			value	.parallelStream()
					.forEach(val -> {
						subMap.put(val	.getStatus()
										.getValue(),
								val.getCount());
					});
			woMap.put(key, subMap);
		});
		return woMap;
	}

	public static String getCsvDataFromWrapper(List<WorkorderCountWrapper> workorderReportWrappper) {
		StringBuilder csvdata = new StringBuilder();
		String header = NVWorkorderConstant.WORKORDER_DASHBOARD_HEADER + NVWorkorderConstant.NEWL_LINE;
		csvdata.append(header);
		if (workorderReportWrappper != null) {
			String csvContent = getFileDetail(workorderReportWrappper);
			csvdata.append(csvContent);
		}
		return csvdata.toString();
	}

	public static String getFileDetail(List<WorkorderCountWrapper> workorderReportWrappper) {
		StringBuilder csv = new StringBuilder();
		for (WorkorderCountWrapper recipe : workorderReportWrappper) {
			csv	.append(checknull(recipe.getWorkorderId()))
				.append(",")
				.append(checknull(recipe.getWorkorderName()))
				.append(",")
				.append(checknull(recipe.getStatus()
										.toString()
										.replaceAll("_", " ")))
				.append(",")
				.append(checknull(getTemplateNameForReport(recipe	.getTemplateType()
																	.toString())))
				.append(",")
				.append(checknull(recipe.getAssignedTo()).toString())
				.append(",")
				.append(recipe.getCreationTime())
				.append(",")
				.append(checknull(recipe.getCompletionPercentage()))
				.append(",")
				.append(recipe.getGeographyL1Name() != null ? recipe.getGeographyL1Name() : "-")
				.append(",")
				.append(recipe.getGeographyL2Name() != null ? recipe.getGeographyL2Name() : "-")
				.append(",")
				.append(recipe.getGeographyL3Name() != null ? recipe.getGeographyL3Name() : "-")
				.append(",")
				.append(recipe.getGeographyL4Name() != null ? recipe.getGeographyL4Name() : "-")
				.append(NVWorkorderConstant.NEWL_LINE);
		}
		return csv.toString();
	}

	public static Object checknull(Object value) {
		if (value == null) {
			return "-";
		}
		return value;
	}

	public static List<TemplateType> getTemplateListFromString(String templateType) {
		List<String> list = Arrays.asList(templateType.split(","));
		List<TemplateType> templateTypeList = new ArrayList<>();
		list.parallelStream()
			.forEach(temp -> {
				templateTypeList.add(TemplateType.valueOf(temp));
			});
		return templateTypeList;
	}

	public static String getTemplateNameForReport(String templateName) {
		if (TemplateType.NV_IB_BENCHMARK.equals(TemplateType.valueOf(templateName))) {
			return IB_ADHOC_BENCHMARK;
		} else if (TemplateType.NV_INBUILDING.equals(TemplateType.valueOf(templateName))) {
			return INBUILDING;
		} else if (TemplateType.NV_ADHOC_IB.equals(TemplateType.valueOf(templateName))) {
			return ADHOC_IN_BUILDING;
		} else if (TemplateType.NV_ADHOC_BRTI_DRIVE.equals(TemplateType.valueOf(templateName))
				|| TemplateType.NV_ADHOC_BRTI_ST.equals(TemplateType.valueOf(templateName))) {
			return ADHOC_BRTI;
		} else if (TemplateType.NV_ADHOC_LD.equals(TemplateType.valueOf(templateName))) {
			return ADHOC_LIVE_DRIVE;
		} else if (TemplateType.NV_ADHOC_OD.equals(TemplateType.valueOf(templateName))) {
			return ADHOC_OPEN_DRIVE;
		} else if (TemplateType.NV_BENCHMARK.equals(TemplateType.valueOf(templateName))) {
			return BENCHMARK;
		} else if (TemplateType.NV_BRTI.equals(TemplateType.valueOf(templateName))) {
			return BRTI;
		} else if (TemplateType.NV_CLOT.equals(TemplateType.valueOf(templateName))) {
			return CLUSTER_OPTIMIZATION;
		} else if (TemplateType.NV_COMPLAINTS.equals(TemplateType.valueOf(templateName))) {
			return CUSTOMER_COMPLAINTS;
		} else if (TemplateType.NV_DRIVE.equals(TemplateType.valueOf(templateName))
				|| TemplateType.NV_LIVE_DRIVE.equals(TemplateType.valueOf(templateName))) {
			return LIVE_DRIVE;
		} else if (TemplateType.NV_OPENDRIVE.equals(TemplateType.valueOf(templateName))) {
			return OPEN_DRIVE;
		} else if (TemplateType.NV_SSVT.equals(TemplateType.valueOf(templateName))) {
			return SSVT;
		} else if (TemplateType.NV_STATIONARY.equals(TemplateType.valueOf(templateName))) {
			return STATIONARY;
		} else if (TemplateType.NV_STEALTH.equals(TemplateType.valueOf(templateName))) {
			return STEALTH;
		}
		return templateName;
	}
	/**
	 * It creates a list of WorkorderNames from list of GenericWorkorder objects.
	 * @param workorderList : List of GenericWorkorder
	 * @return List<String> containing all workOrder Names from List
	 */
	public static List<String> getWorkorderIdListFromGWO(List<GenericWorkorder> workorderList) {
		return workorderList.stream().map(workorder -> workorder.getWorkorderId())
				.collect(Collectors.toList());
	}
	/**
	 * Getting Map with Workorder Name as key and BpmnTask Id to respective Workorder.
	 * @param bpmnTaskList : List of BpmnTask
	 * @return BpmnTask Id Map
	 */
	public static Map<String, BpmnTaskCandidate> getBpmnTaskCandidateMap(List<BpmnTaskCandidate> list) {
		Map<String, BpmnTaskCandidate> map = new HashMap<>();
		for(BpmnTaskCandidate candidate : list) {
			map.put(candidate.getBpmnTask().getBpmnWorkorder().getWorkorderNo(), candidate);
		}
		return map;
	}
	public static String getUserName(User user) {
		if(user != null) {
			return user.getFirstName() + Symbol.SPACE + user.getLastName();
		}
		return Symbol.HASH_STRING;
	}
}
