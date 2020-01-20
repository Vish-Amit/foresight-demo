package com.inn.foresight.module.nv.core.workorder.constants;

public class GenericWorkorderConstants {
	
	
	private GenericWorkorderConstants() {
		
	}
	public static final String GET_ALL_WO_COUNT_BY_DATE_QUERY = "getAllWorkorderCountByDate";
	public static final String GET_ADHOC_WO_DAYWISE_COUNT_QUERY = "getADHOCWorkorderDayWiseCount";
	public static final String GET_WO_COUNT_BY_STATUS = "getWorkorderCountByStatus";
	public static final String GET_WO_COUNT_BY_TEMPLATE_TYPE_AND_STATUS_BY_DATE_QUERY="getWOCountByTemplateTypeAndStatusByDate";
	public static final String GET_DAY_WISE_WO_COUNT_BY_STATUS_QUERY="getDayWiseWOCountByStatus";
	public static final String GET_DUE_WO_DAY_WISE_COUNT_QUERY="getDueWorkorderDayWiseCount";
	public static final String GET_DUE_WO_LIST_BY_DATE_QUERY="getDueWorkorderListByDate";
	public static final String GET_DAY_WISE_ASSIGNED_WO_COUNT_QUERY = "getDayWiseAssignedWOCount";
	public static final String GET_WO_FOR_REPORT_BY_DATE_QUERY="getWOListForReportByDate";
	public static final String FIND_ALL_WOKRORDER_WITHIN_TIMERANGE="findAllWorkorderWithinTimeRange";
	public static final String TEMPLATE_LIST = "templateList";
	public static final String STATUS_LIST = "statusList";
	public static final String START_DATE = "startDate";
	public static final String START_TIME = "startTime";
	
	public static final String END_DATE = "endDate";
	public static final String END_TIME = "endTime";
	public static final String DATE = "date";
	
	public static final String TDD = "TDD";
	public static final String FDD = "FDD";
	public static final String GENERIC_WORKORDER="GenericWorkorder";
	public static final String ARCHIVED_FILTER="ArchivedFilter";
	public static final String IS_ARCHIVED="isArchived";
	public static final String GW_CREATOR_FILTER="GWCreatorFilter";
	public static final String SEARCH_FILTER="SearchFilter";
	public static final String NVADHOC="NVADHOC";
	
	public static final String WO_ID_FILTER="WoIdFilter";
	public static final String WO_NAME_FILTER="WoNameFilter";
	public static final String REMARK_FILTER="RemarkFilter";
	public static final String ASSIGNED_TO_FILTER = "AssignedToFilter";
	public static final String ASSIGNED_BY_FILTER = "AssignedByFilter";
	
	
}
