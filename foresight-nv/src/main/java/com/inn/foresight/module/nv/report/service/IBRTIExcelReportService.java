package com.inn.foresight.module.nv.report.service;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;

import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.report.wrapper.BRTIExcelReportWrapper;

public interface IBRTIExcelReportService {

	
	Map<String, BRTIExcelReportWrapper> createQuertelyExcelReportByCategory(List<Integer> list, String Category,
			String cityName);

	
	XSSFWorkbook generateCallTableAtGivenRowCol(Sheet sheet, XSSFWorkbook workbook,
			List<BRTIExcelReportWrapper> BRTIExcelReportWrapper, int rowstrt, int colstrt, String heading);

	XSSFWorkbook generateCallTableAtGivenRowColOnNetOffNet(Sheet sheet, XSSFWorkbook workbook,
			List<BRTIExcelReportWrapper> BRTIExcelReportWrapper, int rowstrt, int colstrt, String heading);

	
	XSSFWorkbook generateSMSTableAtGivenRowCol(Sheet sheet, XSSFWorkbook workbook,
			List<BRTIExcelReportWrapper> BRTIExcelReportWrapper, int rowstrt, int colstrt, String heading);

		XSSFWorkbook generateSmsTableAtGivenRowColOnnetOffnet(Sheet sheet, XSSFWorkbook workbook,
			List<BRTIExcelReportWrapper> BRTIExcelReportWrapper, int rowstrt, int colstrt, String heading);

	XSSFWorkbook writeDataColumnWise(Sheet sheet, XSSFWorkbook workbook, int rowstrt,
			int colstrt, String[] data3);
	

	

	Response createQuertelyExcelReport(List<Integer> woIdList, List<Integer> geoIdList, String filePath,
			JSONObject jsonMap);




	List<BRTIExcelReportWrapper> getStationaryDataList(boolean bool, List<BRTIExcelReportWrapper> finalStationaryList,
			List<GenericWorkorder> genricWoList, boolean isStationaryData);




	List<BRTIExcelReportWrapper> getBrtiDriveDataForWoIds(List<Integer> woIdList, boolean bool,
			List<GenericWorkorder> genricWoList, String location, String technology);


}
