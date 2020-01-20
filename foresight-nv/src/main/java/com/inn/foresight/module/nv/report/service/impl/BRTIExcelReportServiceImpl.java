package com.inn.foresight.module.nv.report.service.impl;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.report.service.IBRTIExcelReportService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.ExcelReportUtils;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.wrapper.BRTIExcelReportWrapper;

@Service("BRTIExcelReportServiceImpl")
@Transactional
public class BRTIExcelReportServiceImpl extends ReportUtil  implements IBRTIExcelReportService,ReportConstants {
	private Logger logger = LogManager.getLogger(BRTIExcelReportServiceImpl.class);
	
	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;
	@Autowired
	private IReportService reportService;
	@Autowired
	private IGenericWorkorderDao genericWrokOrderDao;

	@Override
	public Response createQuertelyExcelReport( List<Integer>woIdList ,List<Integer>geoIdList, String brtiDestinationFilePath, JSONObject jsonMap) {
		try {
			logger.info("BRTI_DESTINATION_FILE_PATH {}",brtiDestinationFilePath);
			boolean bool = true;
			List<BRTIExcelReportWrapper> finalStationaryList = new ArrayList<>();
			logger.info("inside the method createQuertelyExcelReport {}", woIdList);
			List<GenericWorkorder> genricWoList = genericWrokOrderDao.findByIds(woIdList);
			if (genricWoList != null) {
				genricWoList = genricWoList.stream().filter(x -> x.getCreationTime() != null)
						.collect(Collectors.toList());
				genricWoList.sort(Comparator.comparing(GenericWorkorder::getCreationTime));
				getStationaryDataList(bool, finalStationaryList, genricWoList,true);
			}
			
			List<String> locationList=reportService.getGegraphyNameList((String)jsonMap.get(ReportConstants.GEOGRAPHY_TYPE), geoIdList);
			String location=!locationList.isEmpty()?locationList.get(ForesightConstants.ZERO):ReportConstants.HYPHEN;
			String technology=jsonMap.get(ReportConstants.TECHNOLOGY)!=null?(String) jsonMap.get(ReportConstants.TECHNOLOGY):ReportConstants.ALL;
			List<BRTIExcelReportWrapper> drveDataList = getBrtiDriveDataForWoIds(woIdList, bool, genricWoList,location, technology);
			
			createExcelReport(drveDataList, finalStationaryList, brtiDestinationFilePath,jsonMap,location);
			return Response.ok(ForesightConstants.SUCCESS_JSON).build();
		}
		catch(Exception e) {
			logger.error("Exception in method createQuertelyExcelReport {}",Utils.getStackTrace(e));
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	@Override
	public List<BRTIExcelReportWrapper> getBrtiDriveDataForWoIds(List<Integer> woIdList, boolean bool,
			List<GenericWorkorder> genricWoList, String location, String technology) {
		List<BRTIExcelReportWrapper> drveDataList = new ArrayList<>();
		Map<String, String> driveDataMap = nvLayer3DashboardService.getBandWiseDataMapForCategory(woIdList,
				ReportConstants.RECIPE_CATEGORY_DRIVE, bool);
		logger.info("getBrtiDriveDataForWoIds data is {}", driveDataMap);
		if (driveDataMap != null) {
			drveDataList = convertMapToWrapper(driveDataMap, location, genricWoList, false);
			// Data is filtered out on the basis of selected technology 
			if (technology != null) {
				drveDataList = drveDataList.stream().filter(wrapper->(isValidTechData(wrapper,technology))).collect(Collectors.toList());
			}
			return drveDataList;
		}
		return drveDataList;
	}

	private boolean isValidTechData(BRTIExcelReportWrapper wrapper, String technology) {
		return(ReportConstants.TDD.equalsIgnoreCase(technology) && ReportConstants.TDD.equalsIgnoreCase(wrapper.getTransmissionMode()) 
				|| (ReportConstants.FDD.equalsIgnoreCase(technology) && ReportConstants.FDD.equalsIgnoreCase(wrapper.getTransmissionMode()))
				|| (ReportConstants.ALL.equalsIgnoreCase(technology)));
	}

	@Override
	public List<BRTIExcelReportWrapper> getStationaryDataList(boolean bool, List<BRTIExcelReportWrapper> finalStationaryList,
			List<GenericWorkorder> genricWoList,boolean isStationaryData) {
		Map<String, List<GenericWorkorder>> remarkMap = getRemarkWiseMap(genricWoList);
		if (remarkMap != null) {
			for (Entry<String, List<GenericWorkorder>> entry : remarkMap.entrySet()) {
				try {
					List<Integer> idList = entry.getValue().stream().map(GenericWorkorder::getId)
							.collect(Collectors.toList());
					Map<String, String> stationaryDataMap = nvLayer3DashboardService
							.getBandWiseDataMapForCategory(idList, ReportConstants.RECIPE_CATEGORY_STATIONARY, bool);
					if (stationaryDataMap != null) {
						List<BRTIExcelReportWrapper> stationaryDataList = convertMapToWrapper(stationaryDataMap,
								entry.getKey(), entry.getValue(), isStationaryData);
						finalStationaryList.addAll(stationaryDataList);
					}
				} catch (Exception e) {
					logger.info("Exception to find stationary data for Remark {}idList{}", entry.getKey(),
							entry.getValue());
				}
			}
		}
		logger.info("Inside getStationaryDataList returning wrapper {} ",finalStationaryList!=null?finalStationaryList.size():"finalStationaryList is null");
		return finalStationaryList;
	}

	private Map<String, List<GenericWorkorder>> getRemarkWiseMap(List<GenericWorkorder> genricWoList) {
		Map<String, List<GenericWorkorder>> remarkMap = null;
		try {
			genricWoList = replaceNullRemarkByHyphen(genricWoList);
			remarkMap = genricWoList.stream()
									.collect(Collectors.groupingBy(GenericWorkorder::getRemark, Collectors.toList()));
		} catch (Exception e) {
			logger.error("Exception inside the method getRemarkWiseMap {}", Utils.getStackTrace(e));
		}
		return remarkMap;
	}

	private List<GenericWorkorder> replaceNullRemarkByHyphen(List<GenericWorkorder> genricWoList) {
		List<GenericWorkorder> newGeoList=new ArrayList<>();
		for (GenericWorkorder genericWorkorder : genricWoList) {
			if(genericWorkorder.getRemark()!=null) {
				newGeoList.add(genericWorkorder);
			}
			else {
				genericWorkorder.setRemark(ReportConstants.HYPHEN);
				newGeoList.add(genericWorkorder);
			}
			
		}
		return newGeoList;
	}

	@Override
	public Map<String, BRTIExcelReportWrapper> createQuertelyExcelReportByCategory(List<Integer> list, String category,
			String cityName) {
		logger.info("inside the method createQuertelyExcelReport {}", list);
		Map<String, BRTIExcelReportWrapper> wrapperMap = new HashMap<>();
		Map<String, String> dataMap = nvLayer3DashboardService.getBandWiseDataMapForCategory(list, category, true);
		if (dataMap != null) {
			for (Entry<String, String> dataMapEntry : dataMap.entrySet()) {
				wrapperMap.put(dataMapEntry.getKey(), setDataToWrapper(dataMapEntry.getKey(), dataMapEntry.getValue(), cityName, null, false));
			}
		}
		logger.info("the wrapperMap is{}",wrapperMap);
		return wrapperMap;
	}


	private File createExcelReport(List<BRTIExcelReportWrapper> drveDataList,
			List<BRTIExcelReportWrapper> stationaryDataList, String brtiDestinationFIlePath, JSONObject jsonMap, String Location) {
		Integer rowIndex = ForesightConstants.ZERO;
		logger.info("inside the method  createExcelReport drveDataList {} stationaryDataList{}", drveDataList.size(),
				stationaryDataList.size());
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("BRTIQuertelyExcelReport");
		
		sheet.setDisplayGridlines(false);
		writeHedingInfoInReport(sheet, workbook, ReportConstants.NETWORK_PERFORMANNCE, "#FFFFFF", rowIndex,
				new CellRangeAddress(ForesightConstants.ZERO, ForesightConstants.ONE, ForesightConstants.ONE, NVConstant.THREE_INT), 20);
		writeHedingInfoInReport(sheet, workbook, getHedingForReport(jsonMap), "#FFFFFF",
				rowIndex += ForesightConstants.TWO, new CellRangeAddress(ForesightConstants.TWO, NVConstant.THREE_INT, ForesightConstants.ZERO, NVConstant.THREE_INT), 20);
		writeHedingInfoInReport(sheet, workbook, ReportConstants.HEADING_ONE, "#FF6100", rowIndex += ForesightConstants.TWO,
				new CellRangeAddress(4, 5, ForesightConstants.ZERO, NVConstant.THREE_INT), ForesightConstants.TEN);
		addCallHeadeToSheet(workbook, sheet, rowIndex += ForesightConstants.TWO, BRTI_QOERTLY_REPORT_CALL_HEADER);
		rowIndex = wirteDataInBRTIExcelReport(workbook, sheet, drveDataList, ++rowIndex);
		writeHedingInfoInReport(sheet, workbook,ReportConstants.HEADING_TWO , "#FF6100", rowIndex += ForesightConstants.TWO,
				new CellRangeAddress(rowIndex, rowIndex +ForesightConstants.ONE, ForesightConstants.ZERO, NVConstant.THREE_INT), ForesightConstants.TEN);
		addCallHeadeToSheet(workbook, sheet, rowIndex += ForesightConstants.TWO, BRTI_QOERTLY_REPORT_CALL_HEADER);
		rowIndex = wirteDataInBRTIExcelReport(workbook, sheet, stationaryDataList, ++rowIndex);
		writeHedingInfoInReport(sheet, workbook,ReportConstants.HEADING_THREE , "#FF6100", rowIndex += ForesightConstants.TWO,
				new CellRangeAddress(4, 5,ForesightConstants.ZERO, NVConstant.THREE_INT), 20);
		addCallHeadeToSheet(workbook, sheet, rowIndex += ForesightConstants.TWO, BRTI_QOERTLY_REPORT__SMS_HEADER);
		rowIndex = wirteSmsDataInReport(workbook, sheet, drveDataList, ++rowIndex);
		writeHedingInfoInReport(sheet, workbook,ReportConstants.HEADING_FOUR, "#FF6100", rowIndex += ForesightConstants.TWO,
				new CellRangeAddress(4, 5,ForesightConstants.ZERO, NVConstant.THREE_INT), 20);
		addCallHeadeToSheet(workbook, sheet, rowIndex += ForesightConstants.TWO, BRTI_QOERTLY_REPORT__SMS_HEADER);
		wirteSmsDataInReport(workbook, sheet, stationaryDataList, ++rowIndex);
		setAutoColumnWidthInsheet(sheet);
		try {
			String brtiReportExcelFilePath=brtiDestinationFIlePath+getFileName(jsonMap,Location);
			logger.info("BRTI_EXCEL_REPORT_FILE{}",brtiReportExcelFilePath);
			return ExcelReportUtils.exportExcel(brtiReportExcelFilePath, workbook);
		} catch (IOException | RestException e) {

			logger.error("Error inside the ,method createExcelReport {}", Utils.getStackTrace(e));
		}
		return null;

	}

	private String getFileName(JSONObject jsonMap, String location) {

		String baseHeading = "Q-";
		Integer quarter =ForesightConstants.ZERO;
		if ((jsonMap.get(ReportConstants.QUARTER) != null)) {
			quarter = ((Long) jsonMap.get(ReportConstants.QUARTER)).intValue();
			baseHeading += quarter;
		}
		if (jsonMap.get(ReportConstants.YEAR) != null) {
			baseHeading += ReportConstants.UNDERSCORE + jsonMap.get(ReportConstants.YEAR);
		}
		baseHeading += ReportConstants.UNDERSCORE + location+ReportConstants.XLS_EXTENSION;
		return baseHeading;

	}
	

	private void setAutoColumnWidthInsheet(XSSFSheet sheet) {
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		sheet.autoSizeColumn(ForesightConstants.TWO);
		sheet.autoSizeColumn(NVConstant.THREE_INT);
		sheet.autoSizeColumn(4);
		sheet.autoSizeColumn(5);
		sheet.autoSizeColumn(6);
		sheet.autoSizeColumn(7);
		sheet.autoSizeColumn(8);
		sheet.autoSizeColumn(9);
		sheet.autoSizeColumn(10);
		sheet.autoSizeColumn(11);
		sheet.autoSizeColumn(12);
	}

	private String getHedingForReport(JSONObject jsonMap) {
          String baseHeading="Periode :  Q-";
          Integer quarter=0;
          if((jsonMap.get(ReportConstants.QUARTER)!=null)){
        	  quarter=((Long) jsonMap.get(ReportConstants.QUARTER)).intValue();
        	  baseHeading+=quarter;
          }
          if(jsonMap.get(ReportConstants.YEAR)!=null){
        	  baseHeading+=ReportConstants.SPACE+jsonMap.get(ReportConstants.YEAR);
          }
          String qs=ReportUtil.getQuarterMonthByNo(quarter);
          if(qs!=null) {
          baseHeading+= ReportConstants.SPACE+qs;
          }
		return baseHeading;
	}

	private XSSFSheet writeHedingInfoInReport(XSSFSheet sheet, XSSFWorkbook workbook, String heading, String colorCode,
			int rowIndex, CellRangeAddress cellRange, int cellHight) {
		sheet.addMergedRegion(cellRange);
		XSSFFont fontHeader = ExcelReportUtils.getFontForExcelForHeader(workbook, true, ForesightConstants.TEN);
		XSSFCellStyle borderCellStyle = ExcelReportUtils.getCellStyle(workbook, Color.BLACK, Color.decode(colorCode),
				BorderStyle.THIN, fontHeader, true, true, true, true, HorizontalAlignment.LEFT);
		XSSFRow rowHeader = ExcelReportUtils.getNewRow(sheet, rowIndex, cellHight);
		ExcelReportUtils.createRow(rowHeader, borderCellStyle, heading);
		rowIndex++;
		logger.info("writeHedingInfoInReport {}", rowIndex);

		return sheet;
	}

	private Integer wirteDataInBRTIExcelReport(XSSFWorkbook workbook, XSSFSheet sheet,
			List<BRTIExcelReportWrapper> drveDataList, Integer rowNum) {
		logger.info("In method of wirteDataInBRTIExcelReport :{}", drveDataList.size());
		try {
			for (BRTIExcelReportWrapper brtiExcelReportWrapper : drveDataList) {
				if(brtiExcelReportWrapper.getTotalCall()!=null&&brtiExcelReportWrapper.getTotalCall()>0){
					rowNum = writecallDataInSheet(brtiExcelReportWrapper, workbook, sheet, rowNum);
				}
				if(brtiExcelReportWrapper.getTotalCallOnnet()!=null &&brtiExcelReportWrapper.getTotalCallOnnet()>0 )
				{
					rowNum = writecallOnNetDataInSheet(brtiExcelReportWrapper, workbook, sheet, rowNum);
				}
				if(brtiExcelReportWrapper.getTotalCallOffnet()!=null&&brtiExcelReportWrapper.getTotalCallOffnet()>0 ) {
				rowNum = writecallOffNetDataInSheet(brtiExcelReportWrapper, workbook, sheet, rowNum);
				}
		
			}
					logger.info("rowNum inside wirteDataInBRTIExcelReport={}" , rowNum);
		} catch (Exception e) {
			logger.error("Exception in Writing data for BRTI report {}", Utils.getStackTrace(e));
		}
		return rowNum;
	}

	private Integer wirteSmsDataInReport(XSSFWorkbook workbook, XSSFSheet sheet,
			List<BRTIExcelReportWrapper> drveDataList, Integer rowNum) {
		logger.info("In method of wirteSmsDataInReport :{}", drveDataList.size());
		try {
			for (BRTIExcelReportWrapper brtiExcelReportWrapper : drveDataList) {
				if(brtiExcelReportWrapper.getTotalSms()!=null&&brtiExcelReportWrapper.getTotalSms()>0) {
				rowNum = writeSmsDataInSheet(brtiExcelReportWrapper, workbook, sheet, rowNum);
				}
				if(brtiExcelReportWrapper.getTotalSmsOnnet()!=null && brtiExcelReportWrapper.getTotalSmsOnnet()>0) {
				rowNum = writeSmsOnNetDataInSheet(brtiExcelReportWrapper, workbook, sheet, rowNum);
				}
				if(brtiExcelReportWrapper.getTotalSmsOffnet()!=null&&brtiExcelReportWrapper.getTotalSmsOffnet()>0) {
				rowNum = writeSmsOffNetDataInSheet(brtiExcelReportWrapper, workbook, sheet, rowNum);
				}
			}
				logger.info("rowNum inside wirteDataInBRTIExcelReport={}",  rowNum);
		} catch (Exception e) {
			logger.error("Exception in Writing data for BRTI report {}", Utils.getStackTrace(e));
		}
		return rowNum;
	}

	private Integer writeSmsOffNetDataInSheet(BRTIExcelReportWrapper wrapper, XSSFWorkbook workbook,
			XSSFSheet sheet, Integer rowNum) {

		int colNum;
		XSSFCellStyle rowStyle = ExcelReportUtils.getCellStyle(workbook, Color.BLACK, Color.decode("#FFFFFF"),
				BorderStyle.THIN, null, true, true, true, true, HorizontalAlignment.LEFT);

			XSSFRow rowAll = ExcelReportUtils.getNewRow(sheet, rowNum, 30);
			colNum = setCommanFieldForReport(rowStyle, wrapper, rowAll, ReportConstants.OFF_NET);

			if (wrapper.getTotalSmsOffnet() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getTotalSmsOffnet().toString(), rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);
			colNum++;
			if (wrapper.getSmsDeliveredOffnetIn3min() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getSmsDeliveredOffnetIn3min().toString(),
						rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);

			colNum++;
			if (wrapper.getSmsDeliiveredOffnet() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getSmsDeliiveredOffnet().toString(), rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);

			if (wrapper.getSmsDeliveryRateOffnet() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getSmsDeliveryRateOffnet()+ReportConstants.PERCENT, rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);

			wrapper = ReportUtil.setInformationForSms(wrapper.getSmsDeliveryRateOffnet(), wrapper);
			colNum++;
			
			ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getInforamtion(), rowStyle);

			rowNum++;
		return rowNum;

	}

	private Integer writeSmsOnNetDataInSheet(BRTIExcelReportWrapper wrapper, XSSFWorkbook workbook,
			XSSFSheet sheet, Integer rowNum) {

		int colNum;
		XSSFCellStyle rowStyle = ExcelReportUtils.getCellStyle(workbook, Color.BLACK, Color.decode("#FFFFFF"),
				BorderStyle.THIN, null, true, true, true, true, HorizontalAlignment.LEFT);

			XSSFRow rowAll = ExcelReportUtils.getNewRow(sheet, rowNum, 30);
			colNum = setCommanFieldForReport(rowStyle, wrapper, rowAll, ReportConstants.ON_NET);
			if (wrapper.getTotalSmsOnnet() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getTotalSmsOnnet().toString(), rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);
			colNum++;
			if (wrapper.getSmsDeliveredOnnetIn3Min() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getSmsDeliveredOnnetIn3Min().toString(),
						rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);

			colNum++;
			if (wrapper.getSmsDeliveryRateOnnet() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getSmsDeliveryRateOnnet()+ReportConstants.PERCENT, rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);

			wrapper = ReportUtil.setInformationForSms(wrapper.getSmsDeliveryRateOnnet(), wrapper);
			colNum++;

			ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getInforamtion(), rowStyle);

			rowNum++;
		return rowNum;

	}

	private Integer writeSmsDataInSheet(BRTIExcelReportWrapper wrapper, XSSFWorkbook workbook,
			XSSFSheet sheet, Integer rowNum) {

		int colNum;
		XSSFCellStyle rowStyle = ExcelReportUtils.getCellStyle(workbook, Color.BLACK, Color.decode("#FFFFFF"),
				BorderStyle.THIN, null, true, true, true, true, HorizontalAlignment.CENTER);
	
			XSSFRow rowAll = ExcelReportUtils.getNewRow(sheet, rowNum, 30);
			colNum = setCommanFieldForReport(rowStyle, wrapper, rowAll, ReportConstants.ALL);

			if (wrapper.getTotalSms() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getTotalSms().toString(), rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);
			colNum++;
			if (wrapper.getTotalSmsDeliveredIn3Min() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getTotalSmsDeliveredIn3Min().toString(),
						rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);

			colNum++;
			if (wrapper.getSmsDeliveryRate() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getSmsDeliveryRate()+ReportConstants.PERCENT, rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);
			wrapper = ReportUtil.setInformationForSms(wrapper.getSmsDeliveryRate(), wrapper);
			colNum++;
			ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getInforamtion(), rowStyle);
			rowNum++;
		return rowNum;

	}

	private int setCommanFieldForReport(XSSFCellStyle rowStyle, BRTIExcelReportWrapper wrapper, XSSFRow rowAll,
			String type) {
		int colNum;
		colNum = ReportConstants.INDEX_ZER0;
		ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getFrequency(), rowStyle);
		colNum++;
		ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getSerivce() != null ? wrapper.getSerivce() : "SELULER",
				rowStyle);
		colNum++;

		ExcelReportUtils.addCloumnToRow(rowAll, colNum, type, rowStyle);
		colNum++;

		ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getStartDate() != null ? wrapper.getStartDate() : "-",
				rowStyle);
		colNum++;
		ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getEndDate() != null ? wrapper.getEndDate() : "-",
				rowStyle);
		colNum++;
		ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getPlace(), rowStyle);
		colNum++;
		return colNum;
	}

	private int writecallOffNetDataInSheet(BRTIExcelReportWrapper wrapper, XSSFWorkbook workbook,
			XSSFSheet sheet, int rowNum) {
		int colNum;
		XSSFCellStyle rowStyle = ExcelReportUtils.getCellStyle(workbook, Color.BLACK, Color.decode("#FFFFFF"),
				BorderStyle.THIN, null, true, true, true, true, HorizontalAlignment.CENTER);

			XSSFRow rowAll = ExcelReportUtils.getNewRow(sheet, rowNum, 30);
			colNum = setCommanFieldForReport(rowStyle, wrapper, rowAll, ReportConstants.OFF_NET);
			if (wrapper.getTotalCallOffnet() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getTotalCallOffnet().toString(), rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);
			colNum++;
			if (wrapper.getSuccessCallOffNet() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getSuccessCallOffNet().toString(), rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);
			colNum++;
		
			if (wrapper.getFailCallOffnet() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getFailCallOffnet().toString(), rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);

			colNum++;
			if (wrapper.getDropcallOffnet() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getDropcallOffnet().toString(), rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);

			colNum++;
			if (wrapper.getCallEsaRateOffnet() != null) {
				ExcelReportUtils.addCloumnToRow(rowAll, colNum,
						wrapper.getCallEsaRateOffnet() + ReportConstants.PERCENT, rowStyle);
			} else {
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);

			}
			colNum++;
			if (wrapper.getCallDropRateOffnet() != null) {
				ExcelReportUtils.addCloumnToRow(rowAll, colNum,
						wrapper.getCallDropRateOffnet() + ReportConstants.PERCENT, rowStyle);
			} else {
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);

			}
			colNum++;
			wrapper = ReportUtil.setInformationForCall(wrapper.getCallEsaRateOffnet(), wrapper.getCallDropRateOffnet(), wrapper);

			ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getInforamtion(), rowStyle);

			rowNum++;
		return rowNum;
	}

	private int writecallOnNetDataInSheet(BRTIExcelReportWrapper wrapper, XSSFWorkbook workbook,
			XSSFSheet sheet, int rowNum) {
		int colNum;
		XSSFCellStyle rowStyle = ExcelReportUtils.getCellStyle(workbook, Color.BLACK, Color.decode("#FFFFFF"),
				BorderStyle.THIN, null, true, true, true, true, HorizontalAlignment.CENTER);

			XSSFRow rowAll = ExcelReportUtils.getNewRow(sheet, rowNum, 30);
			colNum = setCommanFieldForReport(rowStyle, wrapper, rowAll, ReportConstants.ON_NET);

			if (wrapper.getTotalCallOnnet() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getTotalCallOnnet().toString(), rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);
			colNum++;
			if (wrapper.getSuccessCallOnNet() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getSuccessCallOnNet().toString(), rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);
			colNum++;
			if (wrapper.getFailCallOnnet() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getFailCallOnnet().toString(), rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);

			colNum++;
			if (wrapper.getDropcallOnnet() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getDropcallOnnet().toString(), rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);

			colNum++;

			if (wrapper.getCallEsaRateOnnet() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getCallEsaRateOnnet()+ReportConstants.PERCENT, rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);

			colNum++;
			if (wrapper.getCallDropRateOnnet() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getCallDropRateOnnet()+ReportConstants.PERCENT, rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);
			colNum++;
			wrapper = ReportUtil.setInformationForCall(wrapper.getCallEsaRateOnnet(), wrapper.getCallDropRateOnnet(), wrapper);

			ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getInforamtion(), rowStyle);

			rowNum++;
		return rowNum;
	}

	private int writecallDataInSheet(BRTIExcelReportWrapper wrapper, XSSFWorkbook workbook, XSSFSheet sheet,
			int rowNum) {
		int colNum;
		XSSFCellStyle rowStyle = ExcelReportUtils.getCellStyle(workbook, Color.BLACK, Color.decode("#FFFFFF"),
				BorderStyle.THIN, null, true, true, true, true, HorizontalAlignment.CENTER);

			XSSFRow rowAll = ExcelReportUtils.getNewRow(sheet, rowNum, 30);

			colNum = setCommanFieldForReport(rowStyle, wrapper, rowAll, ReportConstants.ALL);

			if (wrapper.getTotalCall() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getTotalCall().toString(), rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);
			colNum++;
			if (wrapper.getSuccessCall() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getSuccessCall().toString(), rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);
			colNum++;
			if (wrapper.getFailCall() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getFailCall().toString(), rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);

			colNum++;
			if (wrapper.getDropcall() != null)
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getDropcall().toString(), rowStyle);
			else
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);

			colNum++;
			if (wrapper.getCallEsaRate() != null) {
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getCallEsaRate() + ReportConstants.PERCENT,
						rowStyle);
			} else {
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);

			}
			colNum++;
			if (wrapper.getCallDropRate() != null) {
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getCallDropRate() + ReportConstants.PERCENT,
						rowStyle);
			} else {
				ExcelReportUtils.addCloumnToRow(rowAll, colNum, ReportConstants.HYPHEN, rowStyle);

			}
			wrapper = ReportUtil.setInformationForCall(wrapper.getCallEsaRate(), wrapper.getCallDropRate(), wrapper);
			colNum++;
			ExcelReportUtils.addCloumnToRow(rowAll, colNum, wrapper.getInforamtion(), rowStyle);

			rowNum++;
		return rowNum;
	}

	private XSSFSheet addCallHeadeToSheet(XSSFWorkbook workbook, XSSFSheet sheet, int rowIndex, String[] header) {
		logger.info("inside the method addCallHeadeToSheet rowIndex {}", rowIndex);
		try {
			XSSFFont fontHeader = ExcelReportUtils.getFontForExcelForHeader(workbook, true, ForesightConstants.TEN);
			fontHeader.setColor(IndexedColors.BLACK.getIndex());
			XSSFCellStyle borderCellStyle = ExcelReportUtils.getCellStyle(workbook, Color.BLACK,
					Color.decode("#FFB300"), BorderStyle.THIN, fontHeader, true, true, true, true,
					HorizontalAlignment.CENTER);

			XSSFRow rowHeader = ExcelReportUtils.getNewRow(sheet, rowIndex++, 40);
			ExcelReportUtils.createRow(rowHeader, borderCellStyle, header);
			setColumnwidthForReportData(sheet);
			return sheet;
		} catch (Exception e) {
			logger.error("Exception to add call header {}", Utils.getStackTrace(e));
		}
		return null;
	}

	private List<BRTIExcelReportWrapper> convertMapToWrapper(Map<String, String> driveDataMap, String location,
			List<GenericWorkorder> genricWoList, boolean isStationaryData) {
		List<BRTIExcelReportWrapper> list = new ArrayList<>();
		for (Entry<String, String> entery : driveDataMap.entrySet()) {
			BRTIExcelReportWrapper wrapper=setDataToWrapper(entery.getKey(), entery.getValue(), location, genricWoList,isStationaryData);
			if(wrapper!=null) {
					list.add(wrapper);
			}
		}

		return list;
	}

	private static void setColumnwidthForReportData(Sheet sheet) {
		for (int i =ForesightConstants.ZERO; i <= ReportConstants.INDEX_ZER0; i++)
			sheet.setColumnWidth(i, 3000);
	}

	private BRTIExcelReportWrapper setDataToWrapper(String key, String value, String cityName,
			List<GenericWorkorder> genricWoList, boolean isStationaryData) {
		try {
			logger.info("inside the method  setDataToWrapper key {},value {}", key, value);
			BRTIExcelReportWrapper wrapper = new BRTIExcelReportWrapper();
			setCommonParameterIntoWrapper(key, cityName, genricWoList, isStationaryData, wrapper);
			value = value.replace("[", "").replace("]", "");
			String[] summaryArray = value.split(ReportConstants.COMMA,-ReportConstants.INDEX_ONE);
			setSummaryDataIntoWrapper(wrapper, summaryArray);			
			
			return wrapper;
		} catch (Exception e) {
			logger.error("Exception inside the method setDataToWrapper {}", Utils.getStackTrace(e));
		}
		return null;
	}

	public void setSummaryDataIntoWrapper(BRTIExcelReportWrapper wrapper, String[] summaryArray) {
		setTimeandDateInformation(wrapper,NVLayer3Utils.getLongFromCsv(summaryArray, DriveHeaderConstants.SUMMARY_START_TIME_INDEX));
		wrapper.setAddress(NVLayer3Utils.getStringFromCsv(summaryArray, DriveHeaderConstants.SUMMARY_ADDRESS_INDEX));
		setCallDetailFromSummary(wrapper, summaryArray);
		setSMSDetailFromSummary(wrapper, summaryArray);
		setMOSDataFromSummary(wrapper, summaryArray);
		setCSTDataFromSummary(wrapper,summaryArray);
		setCallOnNetFromSummary(wrapper, summaryArray);
		setCallOffNetFromSummary(wrapper, summaryArray);
		setSMSOnnetFromSummary(wrapper, summaryArray);
		setSMSOffNetFromSummary(wrapper, summaryArray);
		setHTTPDetailFromSummary(wrapper,summaryArray);			
		setKPIDetailsFromSummary(wrapper, summaryArray);
		setTransmissionModeFromSummary(wrapper,summaryArray);
		// Need to add technology in wrapper
	}

	private void setCSTDataFromSummary(BRTIExcelReportWrapper wrapper, String[] summaryArray) {
		wrapper.setCallSetupTime(NVLayer3Utils.getDoubleFromCsv(summaryArray, DriveHeaderConstants.SUMMARY_CALL_SETUP_TIME));
		wrapper.setCallSetupTimeOffNet(NVLayer3Utils.getDoubleFromCsv(summaryArray, DriveHeaderConstants.SUMMARY_CALL_SETUP_TIME_OFF_NET));
		wrapper.setCallSetupTimeOnNet(NVLayer3Utils.getDoubleFromCsv(summaryArray, DriveHeaderConstants.SUMMARY_CALL_SETUP_TIME_ON_NET));
	}

	private void setMOSDataFromSummary(BRTIExcelReportWrapper wrapper, String[] summaryArray) {

		wrapper.setMeanOpinionScore(NVLayer3Utils.getDoubleFromCsv(summaryArray, DriveHeaderConstants.SUMMARY_INDEX_MOS_G711));
		wrapper.setMeanOpinionScoreOnNet(NVLayer3Utils.getDoubleFromCsv(summaryArray, DriveHeaderConstants.SUMMARY_INDEX_MOS_ON_NET_G711));
		wrapper.setMeanOpinionScoreOffNet(NVLayer3Utils.getDoubleFromCsv(summaryArray, DriveHeaderConstants.SUMMARY_INDEX_MOS_OFF_NET_G711));
	}

	public void setCommonParameterIntoWrapper(String key, String cityName, List<GenericWorkorder> genricWoList,
			boolean isStationaryData, BRTIExcelReportWrapper wrapper) {
		wrapper.setStationaryData(isStationaryData);
		wrapper.setFrequency(key);
		wrapper.setPlace(cityName);
		wrapper.setCityName(cityName);
		if (genricWoList != null) {
			setStartandEndDate(genricWoList, wrapper);
		}
	}

	public void setKPIDetailsFromSummary(BRTIExcelReportWrapper wrapper, String[] summaryArray) {
		wrapper.setHttpThroughputAvg(NVLayer3Utils.getDoubleFromCsv(summaryArray, DriveHeaderConstants.SUMMARY_DL_HTTP_AVG));
		wrapper.setNetworkLatency(NVLayer3Utils.getDoubleFromCsv(summaryArray, DriveHeaderConstants.SUMMARY_LATENCY_INDEX));
		wrapper.setPacketLoss(NVLayer3Utils.getDoubleFromCsv(summaryArray, DriveHeaderConstants.SUMMARY_PACKETLOSS));
	}

	public void setHTTPDetailFromSummary(BRTIExcelReportWrapper wrapper, String[] summaryArray) {
		wrapper.setHttpDownloadAttempt(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_HTTP_ATTEMPT));
		wrapper.setHttpDownloadSuccess(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_HTTP_SUCCESS));
		wrapper.setHttpDownloadTimeAvg(NVLayer3Utils.getDoubleFromCsv(summaryArray, DriveHeaderConstants.SUMMARY_HTTP_DOWNLOAD_TIME));
		if (wrapper.getHttpDownloadAttempt() != null && wrapper.getHttpDownloadSuccess()!= null) {
			wrapper.setHttpDlSuccessRate(
					ReportUtil.getPercentage(wrapper.getHttpDownloadSuccess(), wrapper.getHttpDownloadAttempt()));
		}
	}

	public void setSMSOffNetFromSummary(BRTIExcelReportWrapper wrapper, String[] summaryArray) {
		wrapper.setTotalSmsOffnet(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_SMS_ATTEMPT_OFF_NET));
		wrapper.setSmsDeliiveredOffnet(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_SMS_SUCCESFULL_OFF_NET));
		wrapper.setSmsDeliveredOffnetIn3min(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_SMS_DELIVERED_LESS_THAN_3_MIN_OFF_NET));
		wrapper.setSmsDeliveredSumOffNet(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_SMS_DELIVERED_SUM_OFF_NET));

		if (wrapper.getSmsDeliiveredOffnet() != null && wrapper.getTotalSmsOffnet() != null) {
			wrapper.setSmsDeliveryRateOffnet(
					ReportUtil.getPercentage(wrapper.getSmsDeliiveredOffnet(), wrapper.getTotalSmsOffnet()));
		}																					
		if(wrapper.getTotalSmsOffnet()!=null&&wrapper.getSmsDeliveredOffnetIn3min()!=null) {
			wrapper.setSmsoffNetRateIn3Min((ReportUtil.getPercentage(wrapper.getSmsDeliveredOffnetIn3min(), wrapper.getTotalSmsOffnet())));
		}
		if(wrapper.getSmsDeliveredSumOffNet()!=null&& wrapper.getTotalSmsOffnet()!=null && wrapper.getTotalSmsOffnet()!=ReportConstants.INDEX_ZER0) {
			wrapper.setSmsDeliverdOffNetAvgTime((double)wrapper.getSmsDeliveredSumOffNet()/wrapper.getTotalSmsOffnet());
		}
	}

	public void setSMSOnnetFromSummary(BRTIExcelReportWrapper wrapper, String[] summaryArray) {
		wrapper.setTotalSmsOnnet(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_SMS_ATTEMPT_ON_NET));
		wrapper.setSmsDeliiveredOnnet(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_SMS_SUCCESFULL_ON_NET));
		wrapper.setSmsDeliveredOnnetIn3Min(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_SMS_DELIVERED_LESS_THAN_3_MIN_ON_NET));
		wrapper.setSmsDeliveredSumOnNet(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_SMS_DELIVERED_SUM_ON_NET));
		if (wrapper.getSmsDeliiveredOnnet() != null && wrapper.getTotalSmsOnnet() != null) {
			wrapper.setSmsDeliveryRateOnnet(
					ReportUtil.getPercentage(wrapper.getSmsDeliiveredOnnet(), wrapper.getTotalSmsOnnet()));
		}
		if(wrapper.getTotalSmsOnnet()!=null&&wrapper.getSmsDeliveredOnnetIn3Min()!=null) {
			wrapper.setSmsOnNetRateIn3Min(ReportUtil.getPercentage(wrapper.getSmsDeliveredOnnetIn3Min(), wrapper.getTotalSmsOnnet()));
		}
		if(wrapper.getSmsDeliveredSumOnNet()!=null&& wrapper.getTotalSmsOnnet()!=null && wrapper.getTotalSmsOnnet()!=ReportConstants.INDEX_ZER0) {
			wrapper.setSmsDeliverdOnNetAvgTime((double)wrapper.getSmsDeliveredSumOnNet()/wrapper.getTotalSmsOnnet());
		}
	}

	public void setCallOffNetFromSummary(BRTIExcelReportWrapper wrapper, String[] summaryArray) {
		
		wrapper.setFailCallOffnet(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_CALL_FAILURE_OFF_NET));
		wrapper.setTotalCallOffnet(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_CALL_ATTEMPT_OFF_NET));
		wrapper.setDropcallOffnet(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_CALL_DROP_OFF_NET));
		wrapper.setSuccessCallOffNet(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_CALL_SUCCESS_OFF_NET));
		if (wrapper.getTotalCallOffnet() != null && wrapper.getFailCallOffnet() != null) {
			wrapper.setCallEsaRateOffnet(ReportUtil.getPercentage(
					wrapper.getSuccessCallOffNet(), wrapper.getTotalCallOffnet()));
		}
		if (wrapper.getTotalCallOffnet() != null && wrapper.getDropcallOffnet() != null) {
			wrapper.setCallDropRateOffnet(
					ReportUtil.getPercentage(wrapper.getDropcallOffnet(), wrapper.getTotalCallOffnet()));

		}
		
	}

	public void setCallOnNetFromSummary(BRTIExcelReportWrapper wrapper, String[] summaryArray) {
		wrapper.setFailCallOnnet(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_CALL_FAILURE_ON_NET));
		wrapper.setTotalCallOnnet(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_CALL_ATTEMPT_ON_NET));
		wrapper.setDropcallOnnet(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_CALL_DROP_ON_NET));
		wrapper.setSuccessCallOnNet(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_CALL_SUCCESS_ON_NET));
		
		if (wrapper.getTotalCallOnnet() != null && wrapper.getSuccessCallOnNet()!= null) {
			wrapper.setCallEsaRateOnnet(ReportUtil.getPercentage(
					wrapper.getSuccessCallOnNet(), wrapper.getTotalCallOnnet()));
		}
		if (wrapper.getTotalCallOnnet() != null && wrapper.getDropcallOnnet() != null) {
			wrapper.setCallDropRateOnnet(
					ReportUtil.getPercentage(wrapper.getDropcallOnnet(), wrapper.getTotalCallOnnet()));
		}
	}

	private void setSMSDetailFromSummary(BRTIExcelReportWrapper wrapper, String[] summaryArray) {
		wrapper.setTotalSms(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_SMS_ATTEMPT));
		wrapper.setSmsDeliivered(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_SMS_SUCCESS));
		wrapper.setTotalSmsDeliveredIn3Min(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_SMS_DELIVERED_IN_LESS_THAN_3_MIN));
		wrapper.setSmsDeliveredSum(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_SMS_DELIVEREY_SUM));
		if (wrapper.getSmsDeliivered() != null && wrapper.getTotalSms() != null) {
			wrapper.setSmsDeliveryRate(ReportUtil.getPercentage(wrapper.getSmsDeliivered(), wrapper.getTotalSms()));
		}
		
		if (wrapper.getSmsDeliveredSum() != null && wrapper.getSmsDeliivered() != null && wrapper.getSmsDeliivered()!=ReportConstants.INDEX_ZER0) {
			Double avg= ((double)wrapper.getSmsDeliveredSum()/wrapper.getSmsDeliivered());
			wrapper.setSmsDeliveredAvgTime(avg);
		}
		if(wrapper.getTotalSms()!=null&&wrapper.getTotalSmsDeliveredIn3Min()!=null) {
			wrapper.setSmsRateIn3Min(ReportUtil.getPercentage(wrapper.getTotalSmsDeliveredIn3Min(), wrapper.getTotalSms()));
		}
	}

	public void setCallDetailFromSummary(BRTIExcelReportWrapper wrapper, String[] summaryArray) {
			wrapper.setFailCall(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_CALL_FAILURE));
			wrapper.setTotalCall(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_TOTAL_CALL));
			wrapper.setDropcall(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_CALL_DROP));
			wrapper.setSuccessCall(NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_CALL_SUCCESS));
	
		if (wrapper.getTotalCall() != null && wrapper.getSuccessCall() != null) {
			wrapper.setCallEsaRate(ReportUtil.getPercentage(wrapper.getSuccessCall(),
					wrapper.getTotalCall()));
		}
		if (wrapper.getTotalCall() != null && wrapper.getDropcall() != null) {
			wrapper.setCallDropRate(ReportUtil.getPercentage(wrapper.getDropcall(), wrapper.getTotalCall()));
		}

	}

	private void setStartandEndDate(List<GenericWorkorder> genricWoList, BRTIExcelReportWrapper wrapper) {
		try {
			GenericWorkorder genricWorkOrder = genricWoList.get(0);
			wrapper.setStartDate(ReportUtil.parseDateToString(ReportConstants.DATE_FORMAT_DD_MM_YY_SS_AA,
					genricWorkOrder.getCreationTime()));
			wrapper.setEndDate(ReportUtil.parseDateToString(ReportConstants.DATE_FORMAT_DD_MM_YY_SS_AA,
					genricWoList.get(genricWoList.size() -ForesightConstants.ONE).getModificationTime()));
		}catch(Exception e) {
			logger.error("Exception inside the method setStartandEndDate {}",e.getMessage());
		}
	}

	private void setTimeandDateInformation(BRTIExcelReportWrapper wrapper, Long timeStamp) {
		try {
			Date date = new Date(timeStamp);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			wrapper.setDate(ReportUtil.parseDateToString("dd-mm-yy", date));
			wrapper.setDay(ReportUtil.getDayFromDayNo(cal.DAY_OF_WEEK - 1));
			wrapper.setHour(cal.HOUR_OF_DAY + "");
		} catch (Exception e) {
			logger.error("Exception inside the method  setTimeandDateInformation timeStamp {}", timeStamp);
		}

	}

	@Override
	public XSSFWorkbook generateCallTableAtGivenRowColOnNetOffNet(Sheet sheet, XSSFWorkbook workbook,
			List<BRTIExcelReportWrapper> brtiExcelReportWrapper, int rowstrt, int colstrt, String heading) {
		if(!brtiExcelReportWrapper.isEmpty()) {
			CellStyle styleDefault = getCellStyleForNetVelocityReport(workbook, false, false);
			CellStyle styleHeader = getHeaderStyleForNetVelocityReport(workbook, false, false);
			sheet.setDisplayGridlines(false);

			Row headerRow2 = sheet.getRow(rowstrt) != null ? sheet.getRow(rowstrt) : sheet.createRow(rowstrt);
			String[] header2 = { ReportConstants.HEADER_CITY, ReportConstants.HEADER_SERVICE, heading };
			prepareColumnValue(headerRow2, colstrt, styleHeader, header2);
			sheet.autoSizeColumn(colstrt);

			Row headerRow3 = sheet.getRow(1 + rowstrt) != null ? sheet.getRow(1 + rowstrt) : sheet.createRow(1 + rowstrt);
			prepareColumnValue(headerRow3,ForesightConstants.ONE + colstrt, styleHeader, BRTI_EXCEL_REPORT_CALL_HEADER);
			sheet.autoSizeColumn(colstrt);

			sheet.addMergedRegion(new CellRangeAddress(rowstrt,ForesightConstants.ONE + rowstrt, colstrt, colstrt));
			sheet.addMergedRegion(new CellRangeAddress(rowstrt,ForesightConstants.ONE + rowstrt,ForesightConstants.ONE + colstrt,ForesightConstants.ONE + colstrt));
			sheet.addMergedRegion(new CellRangeAddress(rowstrt, rowstrt, ForesightConstants.TWO + colstrt, BRTI_EXCEL_REPORT_CALL_HEADER.length + colstrt));
			sheet.autoSizeColumn(colstrt);
			// Create Other rows and cells with call data
			int rowNum =ForesightConstants.ONE + rowstrt;
			rowstrt=rowstrt+ForesightConstants.TWO;
			for (BRTIExcelReportWrapper ces : brtiExcelReportWrapper) {

				if(ces.getTotalCallOnnet()!=null && ces.getTotalCallOnnet()>0) {
					rowNum++;
					Row row = sheet.getRow(rowNum) != null ? sheet.getRow(rowNum) : sheet.createRow(rowNum);
					String[] dataonnet = { ces.getCityName()==null?"-":ces.getCityName(), ReportConstants.ON_NET,
							ces.getTotalCallOnnet() == null ? "-" : ces.getTotalCallOnnet().toString(),
									ces.getFailCallOnnet() == null ? "-" : ces.getFailCallOnnet().toString(),
											ces.getDropcallOnnet() == null ? "-" : ces.getDropcallOnnet().toString(),
													ces.getCallEsaRateOnnet() == null ? "-" : ces.getCallEsaRateOnnet()+ ReportConstants.PERCENT,
															ces.getCallDropRateOnnet() == null ? "-" : ces.getCallDropRateOnnet() + ReportConstants.PERCENT };

					int col = prepareColumnValue(row, colstrt, styleDefault, dataonnet);			
					sheet.autoSizeColumn(col-1);
					++rowstrt;
				}

				if(ces.getTotalCallOffnet()!=null && ces.getTotalCallOffnet()>0) {
					rowNum++;
					Row row = sheet.getRow(rowNum) != null ? sheet.getRow(rowNum) : sheet.createRow(rowNum);
					String[] dataoffnet = { ces.getCityName(),ReportConstants.OFF_NET,
							ces.getTotalCallOffnet() == null ? "-" : ces.getTotalCallOffnet().toString(),
									ces.getFailCallOffnet() == null ? "-" : ces.getFailCallOffnet().toString(),
											ces.getDropcallOffnet() == null ? "-" : ces.getDropcallOffnet().toString(),
													ces.getCallEsaRateOffnet() == null ? "-" : ces.getCallEsaRateOffnet()+ ReportConstants.PERCENT,
															ces.getCallDropRateOffnet() == null ? "-" : ces.getCallDropRateOffnet() + ReportConstants.PERCENT };
					int col = prepareColumnValue(row, colstrt, styleDefault, dataoffnet);
					sheet.autoSizeColumn(col-1);
					++rowstrt;
				}

				if(ces.getTotalCallOnnet()!=null && ces.getTotalCallOnnet()>0 && ces.getTotalCallOffnet()!=null && ces.getTotalCallOffnet()>0) {
					sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum -ForesightConstants.ONE, colstrt, colstrt));			
				}


			}
			sheet.autoSizeColumn(0);
		}
		return workbook;
	}

	@Override
	public XSSFWorkbook generateCallTableAtGivenRowCol(Sheet sheet, XSSFWorkbook workbook,
			List<BRTIExcelReportWrapper> brtiExcelReportWrapper, int rowstrt, int colstrt, String heading) {
		if(brtiExcelReportWrapper !=null && !brtiExcelReportWrapper.isEmpty()) {
			CellStyle styleDefault = getCellStyleForNetVelocityReport(workbook, false, false);
			CellStyle styleHeader = getHeaderStyleForNetVelocityReport(workbook, false, false);
			sheet.setDisplayGridlines(false);

			Row headerRow2 = sheet.getRow(rowstrt) != null ? sheet.getRow(rowstrt) : sheet.createRow(rowstrt);
			String[] header2 = { ReportConstants.HEADER_CITY, heading };
			prepareColumnValue(headerRow2, colstrt, styleHeader, header2);
			sheet.autoSizeColumn(colstrt);

			Row headerRow3 = sheet.getRow(1 + rowstrt) != null ? sheet.getRow(1 + rowstrt) : sheet.createRow(1 + rowstrt);
			sheet.autoSizeColumn(colstrt);

			int col = prepareColumnValue(headerRow3,ForesightConstants.ONE + colstrt, styleHeader, BRTI_EXCEL_REPORT__CALL_HEADER_TYPE2);
			sheet.autoSizeColumn(col-1);
			sheet.addMergedRegion(new CellRangeAddress(rowstrt,ForesightConstants.ONE + rowstrt, colstrt, colstrt));
			sheet.addMergedRegion(new CellRangeAddress(rowstrt, rowstrt,ForesightConstants.ONE + colstrt, 5 + colstrt));
			sheet.autoSizeColumn(colstrt);
			int rowNum =ForesightConstants.ONE + rowstrt;
			for (BRTIExcelReportWrapper ces : brtiExcelReportWrapper) {

				if(ces.getTotalCall()!=null && ces.getTotalCall()>0) {
					rowNum++;
					Row row = sheet.getRow(rowNum) != null ? sheet.getRow(rowNum) : sheet.createRow(rowNum);

					String[] data = { ces.getCityName()==null?"-":ces.getCityName(), ces.getTotalCall() == null ? "-" : ces.getTotalCall().toString(),
							ces.getFailCall() == null ? "-" : ces.getFailCall().toString(),
									ces.getDropcall() == null ? "-" : ces.getDropcall().toString(),
											ces.getCallEsaRate() == null ? "-" : ces.getCallEsaRate()+ ReportConstants.PERCENT,
													ces.getCallDropRate() == null ? "-" : ces.getCallDropRate() + ReportConstants.PERCENT };

					prepareColumnValue(row, colstrt, styleDefault, data);
				}

			}
		}
		return workbook;
	}

	@Override
	public XSSFWorkbook generateSmsTableAtGivenRowColOnnetOffnet(Sheet sheet, XSSFWorkbook workbook,
			List<BRTIExcelReportWrapper> brtiExcelReportWrapper, int rowstrt, int colstrt, String heading) {
		if(brtiExcelReportWrapper !=null && !brtiExcelReportWrapper.isEmpty()) {
			CellStyle styleDefault = getCellStyleForNetVelocityReport(workbook, false, false);
			CellStyle styleHeader = getHeaderStyleForNetVelocityReport(workbook, false, false);
			sheet.setDisplayGridlines(false);

			Row headerRow2 = sheet.getRow(rowstrt) != null ? sheet.getRow(rowstrt) : sheet.createRow(rowstrt);
			String[] header2 = {ReportConstants.HEADER_CITY,ReportConstants.HEADER_SERVICE, heading };
			prepareColumnValue(headerRow2, colstrt, styleHeader, header2);
			sheet.autoSizeColumn(colstrt);


			Row headerRow3 = sheet.getRow(1 + rowstrt) != null ? sheet.getRow(1 + rowstrt) : sheet.createRow(1 + rowstrt);


			int col = prepareColumnValue(headerRow3,ForesightConstants.ONE + colstrt, styleHeader, BRTI_EXCEL_REPORT__SMS_HEADER);
			sheet.autoSizeColumn(col-1);
			sheet.addMergedRegion(new CellRangeAddress(rowstrt,ForesightConstants.ONE + rowstrt, colstrt, colstrt));
			sheet.addMergedRegion(new CellRangeAddress(rowstrt,ForesightConstants.ONE + rowstrt,ForesightConstants.ONE + colstrt,ForesightConstants.ONE + colstrt));
			sheet.addMergedRegion(new CellRangeAddress(rowstrt, rowstrt, ForesightConstants.TWO + colstrt,  BRTI_EXCEL_REPORT__SMS_HEADER.length + colstrt));
			sheet.autoSizeColumn(colstrt);

			// Create Other rows and cells with call data
			int rowNum =ForesightConstants.ONE + rowstrt;
			rowstrt=rowstrt+ForesightConstants.TWO;
			for (BRTIExcelReportWrapper ces : brtiExcelReportWrapper) {
				if(ReportConstants.NATIONAL.equals(ces.getCityName())) {
					rowNum++;
					Row row = sheet.getRow(rowNum) != null ? sheet.getRow(rowNum) : sheet.createRow(rowNum);
					String[] data = { ces.getCityName()==null?"-":ces.getCityName(),ReportConstants.TOTAL,
							ces.getTotalSms() == null ? "-" : ces.getTotalSms().toString(),
									ces.getSmsDeliivered() == null ? "-" : ces.getSmsDeliivered().toString(),
											ces.getSmsDeliveryRate() == null ? "-" : ces.getSmsDeliveryRate()+ ReportConstants.PERCENT };
					prepareColumnValue(row, colstrt, styleDefault, data);
					++rowstrt;
				}else {

					if(ces.getTotalSmsOnnet()!=null && ces.getTotalSmsOnnet()>0) {
						rowNum++;
						Row row = sheet.getRow( rowNum) != null ? sheet.getRow(rowNum) : sheet.createRow(rowNum);
						String[] dataOnNet = { ces.getCityName()==null?"-":ces.getCityName(),ReportConstants.ON_NET,
								ces.getTotalSmsOnnet() == null ? "-" : ces.getTotalSmsOnnet().toString(),
										ces.getSmsDeliiveredOnnet() == null ? "-" : ces.getSmsDeliiveredOnnet().toString(),
												ces.getSmsDeliveryRateOnnet() == null ? "-" : ces.getSmsDeliveryRateOnnet()+ ReportConstants.PERCENT };
						prepareColumnValue(row, colstrt, styleDefault, dataOnNet);
						++rowstrt;
					}

					if(ces.getTotalSmsOffnet() != null && ces.getTotalSmsOffnet()>0) {
						rowNum++;
						Row row = sheet.getRow(rowNum) != null ? sheet.getRow(rowNum) : sheet.createRow(rowNum);

						String[] dataOffNet = { ces.getCityName()==null?"-":ces.getCityName(),ReportConstants.OFF_NET,
								ces.getTotalSmsOffnet() == null ? "-" : ces.getTotalSmsOffnet().toString(),
										ces.getSmsDeliiveredOffnet() == null ? "-" : ces.getSmsDeliiveredOffnet().toString(),
												ces.getSmsDeliveryRateOffnet() == null ? "-" : ces.getSmsDeliveryRateOffnet()+ ReportConstants.PERCENT };

						prepareColumnValue(row, colstrt, styleDefault, dataOffNet);

					}
					if(ces.getTotalSmsOnnet()!=null && ces.getTotalSmsOnnet()>0 && ces.getTotalSmsOffnet() != null && ces.getTotalSmsOffnet()>0) {
						sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum -1, colstrt, colstrt));

					}
				}

			}
		}
		return workbook;
	}

	@Override
	public XSSFWorkbook generateSMSTableAtGivenRowCol(Sheet sheet, XSSFWorkbook workbook,
			List<BRTIExcelReportWrapper> brtiExcelReportWrapper, int rowstrt, int colstrt, String heading) {
		if(brtiExcelReportWrapper.size()>0 && !brtiExcelReportWrapper.isEmpty()) {
			CreationHelper createHelper = workbook.getCreationHelper();
			CellStyle styleDefault = getCellStyleForNetVelocityReport(workbook, false, false);
			CellStyle styleHeader = getHeaderStyleForNetVelocityReport(workbook, false, false);
			sheet.setDisplayGridlines(false);

			Row headerRow2 = sheet.getRow(rowstrt) != null ? sheet.getRow(rowstrt) : sheet.createRow(rowstrt);
			String[] header2 = {ReportConstants.HEADER_CITY, heading };
			prepareColumnValue(headerRow2, colstrt, styleHeader, header2);
			sheet.autoSizeColumn(colstrt);


			Row headerRow3 = sheet.getRow(1 + rowstrt) != null ? sheet.getRow(1 + rowstrt) : sheet.createRow(1 + rowstrt);

			int col = prepareColumnValue(headerRow3,ForesightConstants.ONE + colstrt, styleHeader, BRTI_EXCEL_REPORT__SMS_HEADER_TYPE2);
			sheet.autoSizeColumn(col-1);

			sheet.addMergedRegion(new CellRangeAddress(rowstrt,ForesightConstants.ONE + rowstrt, colstrt, colstrt));
			// sheet.addMergedRegion(new
			sheet.addMergedRegion(new CellRangeAddress(rowstrt, rowstrt,ForesightConstants.ONE + colstrt, 3 + colstrt));
			sheet.autoSizeColumn(colstrt);

			// Create Other rows and cells with call data
			int rowNum =ForesightConstants.ONE + rowstrt;
			for (BRTIExcelReportWrapper ces : brtiExcelReportWrapper) {

				if(ces.getTotalSms()!=null && ces.getTotalSms()>0) {
					rowNum++;
					Row row = sheet.getRow(rowNum) != null ? sheet.getRow(rowNum) : sheet.createRow(rowNum);
					String[] data = { ces.getCityName()==null?"-":ces.getCityName(), ces.getTotalSms() == null ? "-" : ces.getTotalSms().toString(),
							ces.getSmsDeliivered() == null ? "-" : ces.getSmsDeliivered().toString(),
									ces.getSmsDeliveryRate() == null ? "-" : ces.getSmsDeliveryRate()+ ReportConstants.PERCENT };
					prepareColumnValue(row, colstrt, styleDefault, data);
				}

			}
		}
		return workbook;
	}

	public int prepareColumnValue(Row row, int col, CellStyle styleDefault, String... valueArr) {
		for (Object value : valueArr) {
			Cell valueCell1 = row.createCell(col);
			if (value != null && !ReportConstants.BLANK_STRING.equals(value.toString())
					&& !ReportConstants.SPACE.equals(value.toString()))
				valueCell1.setCellValue(value.toString());
			else
				valueCell1.setCellValue(ReportConstants.HIPHEN);
			valueCell1.setCellStyle(styleDefault);
			col++;
		}
		
		return col;
	}

	public int prepareColumnValue(Row row, int col, CellStyle styleDefault, Object value) {

		Cell valueCell1 = row.createCell(col);
		if (value != null && !ReportConstants.BLANK_STRING.equals(value.toString())
				&& !ReportConstants.SPACE.equals(value.toString()))
			valueCell1.setCellValue(value.toString());
		else
			valueCell1.setCellValue(ReportConstants.HIPHEN);
		valueCell1.setCellStyle(styleDefault);
		col++;

		return col;
	}

	@Override
	public XSSFWorkbook writeDataColumnWise(Sheet sheet, XSSFWorkbook workbook, int rowstrt, int colstrt,
			String[] dataArr) {
		CellStyle styleRedHeader = getRedHeaderStyleForNetVelocityReport(workbook, true, false);
		CellStyle styleDefault = getCellStyleNoBorderForNetVelocityReport(workbook, false, false);
		CellStyle stylePinkHeader = getLightPinkHeaderStyleForNetVelocityReport(workbook, true, false);
		sheet.setDisplayGridlines(false);
		int rowNum = rowstrt;
		int i =ForesightConstants.ZERO;
		for (String data : dataArr) {
			Row row = sheet.getRow(rowNum + i) != null ? sheet.getRow(rowNum + i) : sheet.createRow(rowNum + i);
			if (i ==ForesightConstants.ZERO) {
				prepareColumnValue(row, colstrt, styleRedHeader, data);
			} else if (i ==ForesightConstants.ONE || i == 7) {
				prepareColumnValue(row, colstrt, stylePinkHeader, data);
			} else {
				prepareColumnValue(row, colstrt, styleDefault, data);
			}
			++i;
		}
		return workbook;
	}

	public CellStyle getCellStyleForNetVelocityReport(XSSFWorkbook workbook, boolean isHeader, boolean isMergedRow) {
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFillBackgroundColor((short) 4);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());

		if (isMergedRow) {
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}
		return style;
	}

	public CellStyle getCellStyleNoBorderForNetVelocityReport(XSSFWorkbook workbook, boolean isHeader,
			boolean isMergedRow) {
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setFillBackgroundColor((short) 4);
		style.setFillBackgroundColor((short) 4);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
	
		return style;
	}

	public CellStyle getHeaderStyleForNetVelocityReport(XSSFWorkbook workbook, boolean isHeader, boolean isMergedRow) {

		CellStyle backgroundStyle = workbook.createCellStyle();

		backgroundStyle.setFillBackgroundColor(IndexedColors.BLUE.getIndex());
		backgroundStyle.setFillPattern(FillPatternType.BIG_SPOTS);
		backgroundStyle.setAlignment(HorizontalAlignment.CENTER);

		backgroundStyle.setBorderBottom(BorderStyle.THIN);
		backgroundStyle.setBottomBorderColor(IndexedColors.BLUE.getIndex());
		backgroundStyle.setBorderLeft(BorderStyle.THIN);
		backgroundStyle.setLeftBorderColor(IndexedColors.BLUE.getIndex());
		backgroundStyle.setBorderRight(BorderStyle.THIN);
		backgroundStyle.setRightBorderColor(IndexedColors.BLUE.getIndex());
		backgroundStyle.setBorderTop(BorderStyle.THIN);
		backgroundStyle.setTopBorderColor(IndexedColors.BLUE.getIndex());
		return backgroundStyle;
	}

	public CellStyle getRedHeaderStyleForNetVelocityReport(XSSFWorkbook workbook, boolean isHeader,
			boolean isMergedRow) {
		CellStyle backgroundStyle = workbook.createCellStyle();
		backgroundStyle.setFillBackgroundColor(IndexedColors.DARK_RED.getIndex());
		backgroundStyle.setFillPattern(FillPatternType.BIG_SPOTS);
		backgroundStyle.setAlignment(HorizontalAlignment.CENTER);

		return backgroundStyle;
	}

	public CellStyle getLightPinkHeaderStyleForNetVelocityReport(XSSFWorkbook workbook, boolean isHeader,
			boolean isMergedRow) {
		CellStyle backgroundStyle = workbook.createCellStyle();

		backgroundStyle.setFillBackgroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		backgroundStyle.setFillPattern(FillPatternType.BIG_SPOTS);
		backgroundStyle.setAlignment(HorizontalAlignment.CENTER);

		return backgroundStyle;
	}
	
	private void setTransmissionModeFromSummary(BRTIExcelReportWrapper wrapper, String[] summaryArray) {
		wrapper.setTransmissionMode(NVLayer3Utils.getStringFromCsv(summaryArray, DriveHeaderConstants.HBASE_TRANSMISSION_MODE_INDEX));
	}

}
