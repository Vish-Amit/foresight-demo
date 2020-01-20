package com.inn.foresight.module.nv.report.service.brti;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.wrapper.BRTIExcelReportWrapper;

public class BRTIExcelReport {
	/** The logger. */
	private static Logger logger = LogManager.getLogger(BRTIExcelReport.class);

	private void BRTIExcelReport(){
		// Sonar Issue
	}
	
	public static File createSMSTestReport(List<String[]> smsTestList,String technology, String brtiDestinationFilePath) {
		logger.info("Inside method createSMSTestReport ");
		try(FileInputStream inputStream = new FileInputStream(new File(ConfigUtils.getString(ReportConstants.REPORT_TEMPLATE_PATH)+ReportConstants.BRTI_SMS_SHEET_NAME))) {
			Integer count = ReportConstants.INDEX_TWO;
			XSSFWorkbook wb = new XSSFWorkbook(inputStream);
			CellStyle styleDefault = getCellStyleForNetVelocityReport(wb, false);
			XSSFSheet sheet = wb.getSheetAt(ReportConstants.INDEX_ZER0);
			wb.setSheetName(wb.getSheetIndex(sheet), technology);
			if (smsTestList != null && !smsTestList.isEmpty()) {
				int serialNumber=1;
				logger.info("smsTestList  is not null and empty");
				for (String[] data : smsTestList) {
					Row row = sheet.createRow(count++);
					logger.info("Printing data for Row {} ",count-ReportConstants.INDEX_TWO);
					if(data!=null && data.length>ReportConstants.INDEX_ZER0){
						prepareColumnValue(row, styleDefault,serialNumber, data);
						serialNumber++;
						logger.info("Successfully writing Row Data for Sequnce {} ",count-ReportConstants.INDEX_TWO);
					}
				}
				logger.info("going to write data into Excel ");
				String fileName = brtiDestinationFilePath+ReportConstants.BRTI_SMS_SHEET_NAME;
				ReportUtil.getIfFileExists(fileName);
				return writeDataIntoExcel(fileName, wb);
			}
		} catch (IOException e) {
			logger.error("IOException occured inside method createSMSTestReport {} ",Utils.getStackTrace(e));
			throw new BusinessException(e);
		} catch (Exception e) {
			logger.error("Error occured inside method createSMSTestReport {}", Utils.getStackTrace(e));
			throw new BusinessException(e);
		}
		return null;
	}

	public static CellStyle getCellStyleForNetVelocityReport(XSSFWorkbook wb,boolean isMergedRow) {
		CellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setBorderBottom(BorderStyle.THICK);
		style.setBorderTop(BorderStyle.THICK);
		style.setBorderLeft(BorderStyle.THICK);
		style.setBorderRight(BorderStyle.THICK);
		if (isMergedRow) {
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}
		XSSFFont font= wb.createFont();
	    font.setFontHeightInPoints((short)10);
	    font.setFontName("Arial");
	    font.setColor(IndexedColors.WHITE.getIndex());
	    font.setBold(true);
	    font.setItalic(false);
	    style.setFont(font);
		return style;
	}

	public static  File writeDataIntoExcel(String excelFileName, XSSFWorkbook wb)
			throws IOException {
		FileOutputStream fileOut;
		File file = null;
		if(ReportUtil.getIfFileExists(excelFileName)==null){
			file = new File(excelFileName);
			boolean createNewFile = file.createNewFile();
			if(createNewFile) {
				logger.info("File Created Successfully {}",createNewFile);	
			}
		}
		fileOut = new FileOutputStream(excelFileName);
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
		return file;
	}

	public static int prepareColumnValue(Row row, CellStyle styleDefault, int serialNumber, String... valueArr) {
		int i = 1;
		for (Object value : valueArr) {
			value = i == 3 ? serialNumber : value;
			Cell valueCell1 = row.createCell(i);
			if (value != null && !ReportConstants.BLANK_STRING.equals(value.toString().trim())) {
				valueCell1.setCellValue(value.toString());
			} else {
				valueCell1.setCellValue(ReportConstants.HIPHEN);
			}
			valueCell1.setCellStyle(styleDefault);
			i++;
		}
		return i;
	}

	public static File createSMSReport(Map<String, List<String[]>> technolgyWiseSmsList, String fileName) {
		logger.info("Inside method create SmsExcelReport ");
		try {
			XSSFWorkbook wb = new XSSFWorkbook();
			CellStyle styleDefault = getCellStyleForNetVelocityReport(wb, false);
			int sheetCounter=0;
			for (Entry<String, List<String[]>> smsListMap : technolgyWiseSmsList.entrySet()) {
				Integer count = ReportConstants.INDEX_TWO;
				Sheet sheet = getStyledSheet(wb,sheetCounter);
				int serialNumber=1;
				if (smsListMap.getValue() != null && !smsListMap.getValue().isEmpty()) {
					logger.info("Going to write smsListMap for key {} ",smsListMap.getKey());
					for (String[] data : smsListMap.getValue()) {
						Row row = sheet.createRow(count++);
						logger.info("Printing data for Row {} ",count-ReportConstants.INDEX_TWO);
						if(data!=null && data.length>ReportConstants.INDEX_ZER0){
							prepareColumnValue(row, styleDefault,serialNumber, data);
							serialNumber++;
							logger.info("Successfully writing Row Data for Sequnce {} ",count-ReportConstants.INDEX_TWO);
						}
					}
					logger.info("going to write data into Excel ");
				}
				sheetCounter++;
			}
			return writeDataIntoExcel(fileName, wb);
		} catch (IOException e) {
			logger.error("IOException occured inside method createSMSReport {} ",Utils.getStackTrace(e));
			throw new BusinessException(e);
		} catch (Exception e) {
			logger.error("Error occured inside method createSMSReport {}", Utils.getStackTrace(e));
			throw new BusinessException(e);
		}
	}

	private static Sheet getStyledSheet(XSSFWorkbook wb, int sheetCounter) {
		Sheet sheet = wb.getSheetAt(sheetCounter);
		sheet.createFreezePane(ReportConstants.INDEX_ONE, ReportConstants.INDEX_TWO,ReportConstants.INDEX_ONE,ReportConstants.INDEX_TWO);
		sheet.setDisplayGridlines(false);
		return sheet;
	}
	
	public static void prepareRowValue(int colNo,int rowNo,Sheet sheet,CellStyle styleDefault,Object... valueArr) {
		for (Object value : valueArr) {
			Row row = sheet.getRow(rowNo);
			Cell valueCell1 = row.createCell(colNo);
			if (value != null && value!=ReportConstants.BLANK_STRING && value!=ReportConstants.SPACE){
				valueCell1.setCellValue(value.toString());
			}else{
				valueCell1.setCellValue(ReportConstants.HIPHEN);
			}
			valueCell1.setCellStyle(styleDefault);
			rowNo++;
		}
	}
	
	public static File createYearlyExcelReport(Map<String, List<BRTIExcelReportWrapper>> brtiExcelDataMap,String fileName){
		logger.info("Inside method createYearlyExcelReport ");
		try(FileInputStream inputStream = new FileInputStream(new File(ConfigUtils.getString(ReportConstants.REPORT_TEMPLATE_PATH)+ReportConstants.FORWARD_SLASH+ ReportConstants.BRTI_YEARLY_TEMPLATE))) {
			XSSFWorkbook wb = new XSSFWorkbook(inputStream);
			CellStyle styleDefault = getCellStyleForNetVelocityReport(wb, false);
			int sheetCounter=ReportConstants.INDEX_ZER0;
			int column = ReportConstants.FOUR;
			for (Entry<String, List<BRTIExcelReportWrapper>> brtiExcelData : brtiExcelDataMap.entrySet()) {
				Sheet sheet = getStyledSheet(wb,sheetCounter);
				createYearlySheetsInReport(sheet, styleDefault, column, brtiExcelData.getValue());
				sheetCounter++;
			}
			return writeDataIntoExcel(fileName, wb);
		} catch (IOException e) {
			logger.error("IOException occured inside method createSMSReport {} ",Utils.getStackTrace(e));
			throw new BusinessException(e);
		} catch (Exception e) {
			logger.error("Error occured inside method createSMSReport {}", Utils.getStackTrace(e));
			throw new BusinessException(e);
		}
	}
	
		public static void createYearlySheetsInReport(Sheet sheet,CellStyle styleDefault,int column, List<BRTIExcelReportWrapper> brtiDataWrapperList) {
		try {
			int rowNo = ReportConstants.FOUR;
			for (BRTIExcelReportWrapper wrapper : brtiDataWrapperList) {
				if(wrapper!=null){
					logger.info("wrapper.getPlace() {}  ,Address {} ",wrapper.getPlace() , wrapper.getAddress());
					if(wrapper.isStationaryData()){
						writeSatationaryData(column,rowNo,sheet,styleDefault,wrapper);
								}else{
						writeDriveData(column,rowNo,sheet,styleDefault,wrapper);
					}
					column++;
				}
			}
		} catch (Exception e) {
			logger.error("Error occured inside method createYearlyReport1 {}", Utils.getStackTrace(e));
			throw new BusinessException(e);
		}
	}

	private static void writeSatationaryData(int column, int rowNo, Sheet sheet, CellStyle styleDefault,BRTIExcelReportWrapper wrapper) {
        prepareRowValue(column,rowNo,sheet,styleDefault,wrapper.getPlace(),wrapper.getTotalCallOnnet(),null,wrapper.getDropcallOnnet(),ReportUtil.getPercentage(wrapper.getSuccessCallOnNet(), wrapper.getTotalCallOnnet()),
		ReportUtil.getPercentage(wrapper.getDropcallOnnet(), wrapper.getTotalCallOnnet()),wrapper.getCallSetupTimeOnNet(),wrapper.getMeanOpinionScoreOnNet(),wrapper.getTotalCallOffnet(),null,wrapper.getDropcallOffnet(),ReportUtil.getPercentage(wrapper.getSuccessCallOffNet(), wrapper.getTotalCallOffnet()),
		ReportUtil.getPercentage(wrapper.getDropcallOffnet(), wrapper.getTotalCallOffnet()),wrapper.getCallSetupTimeOffNet(),wrapper.getMeanOpinionScoreOffNet(),
		wrapper.getTotalCall(),null,wrapper.getDropcall(),ReportUtil.getPercentage(wrapper.getSuccessCall(), wrapper.getTotalCall()),
		ReportUtil.getPercentage(wrapper.getDropcall(), wrapper.getTotalCall()),wrapper.getCallSetupTime(),wrapper.getMeanOpinionScore(),
		wrapper.getTotalSmsOnnet(),wrapper.getSmsDeliveryRateOnnet(),wrapper.getSmsDeliveredOnnetIn3Min(),ReportUtil.getPercentage(wrapper.getSmsDeliveredOnnetIn3Min(), wrapper.getTotalSmsOnnet()),
		wrapper.getTotalSmsOffnet(),wrapper.getSmsDeliveryRateOffnet(),wrapper.getSmsDeliveredOffnetIn3min(),ReportUtil.getPercentage(wrapper.getSmsDeliveredOffnetIn3min(), wrapper.getTotalSmsOffnet()),
		wrapper.getTotalSms(),wrapper.getSmsDeliveryRate(),wrapper.getTotalSmsDeliveredIn3Min(),ReportUtil.getPercentage(wrapper.getTotalSmsDeliveredIn3Min(), wrapper.getTotalSms()),
		wrapper.getHttpDownloadAttempt(),wrapper.getHttpDownloadSuccess(),ReportUtil.getPercentage(wrapper.getHttpDownloadSuccess(), wrapper.getHttpDownloadAttempt()),wrapper.getHttpDownloadTimeAvg(),
		wrapper.getHttpThroughputAvg(),wrapper.getNetworkLatency(),wrapper.getPacketLoss(),null,null,null,null,null,null,null,null,null,null,null,null,null);
}

	private static void writeDriveData(int column, int rowNo, Sheet sheet, CellStyle styleDefault,
			BRTIExcelReportWrapper wrapper) {
		        prepareRowValue(column,rowNo,sheet,styleDefault,wrapper.getPlace(),"","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","","",
		        		wrapper.getTotalCall(),null,wrapper.getDropcall(),ReportUtil.getPercentage(wrapper.getSuccessCall(), wrapper.getTotalCall()),
				ReportUtil.getPercentage(wrapper.getDropcall(), wrapper.getTotalCall()),wrapper.getCallSetupTime(),wrapper.getMeanOpinionScore(),
				wrapper.getHttpDownloadAttempt(),wrapper.getHttpDownloadSuccess(),ReportUtil.getPercentage(wrapper.getHttpDownloadSuccess(), wrapper.getHttpDownloadAttempt()),wrapper.getHttpDownloadTimeAvg(),
				wrapper.getHttpThroughputAvg(),null);
	}
}
