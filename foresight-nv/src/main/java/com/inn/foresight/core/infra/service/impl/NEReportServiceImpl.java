package com.inn.foresight.core.infra.service.impl;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.maps.Corner;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.geometry.BoundaryUtils;
import com.inn.commons.maps.geometry.gis.GIS2DPolygon;
import com.inn.commons.maps.geometry.gis.GISGeometry;
import com.inn.commons.maps.geometry.gis.GISPoint;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.ApplicationContextProvider;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.dao.IMacroSiteDetailDao;
import com.inn.foresight.core.infra.dao.INEBandDetailDao;
import com.inn.foresight.core.infra.dao.INetworkElementDao;
import com.inn.foresight.core.infra.service.INEReportService;
import com.inn.foresight.core.infra.service.INESiteDetailService;
import com.inn.foresight.core.infra.service.INEVisualizationService;
import com.inn.foresight.core.infra.service.ISiteVisualizationService;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.InfraUtils;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.wrapper.SectorSummaryWrapper;
import com.inn.foresight.core.infra.wrapper.SiteConnectionPointWrapper;
import com.inn.foresight.core.infra.wrapper.SiteGeographicalDetail;
import com.inn.foresight.core.infra.wrapper.SiteLayerSelection;
import com.inn.foresight.core.report.utils.ReportUtils;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.model.SystemConfiguration;

@Service("NEReportServiceImpl")
public class NEReportServiceImpl implements INEReportService {

	private static Logger logger = LogManager.getLogger(NEReportServiceImpl.class);
	
	@Autowired
	private INEVisualizationService ineVisualizationService; 
	
	@Autowired
	private SystemConfigurationDao iSystemConfigurationDao; 
	
	@Autowired
	private IMacroSiteDetailDao iMacroSiteDetailDao; 
	
	@Autowired
	private INEBandDetailDao ineBandDetailDao;
	
	@Autowired
	private INetworkElementDao iNetworkElementDao;
	
	String[] enbSheetSecondHeader = { "Host Name", "Network Type", "IP Address" };
	
	@Autowired(required=false)
	@Qualifier("NESiteDetailServiceImpl")
	private INESiteDetailService ineSiteDetailService;
	
	public static final String DEFAULT_COLOR = "#FFFFFF";
	
	public static final String DATE_FORMAT = "ddMMyyyy";

	/** The Constant GREEN. */
	public static final String GREEN = "#C5E0B4";
	/** The Constant ORANGE. */
	public static final String ORANGE = "#FFE699";

	/** The Constant PINK. */
	public static final String PINK = "#F2C09E";

	/** The Constant SKY_BLUE. */
	public static final String SKY_BLUE = "#DEEBF7";
	
	public static final String OVERVIEW = "Overview";
	
	public static final String SITES_REPORT_PATH = "SITES_REPORT_PATH";
	
	   private Map<String, LinkedList<String>> getSecondHeaderForOverview( SiteGeographicalDetail siteGeographicalDetail) {
		   LinkedList<String> secondHeaderList = new LinkedList<>();
		   LinkedList<String> secondSubHeaderList = new LinkedList<>();
		   secondHeaderList.add(InfraConstants.ALPHA);
		   addBlankString(secondHeaderList, 1);
		   secondHeaderList.add(InfraConstants.BETA);
		   addBlankString(secondHeaderList, 1);
		   secondHeaderList.add(InfraConstants.GAMMA);
		   addBlankString(secondHeaderList, 1);
		   secondSubHeaderList= getSecondSubHeaderForOverview();
		   Map<String, LinkedList<String>> map=getSecondHeaderForAdditionalSector(secondHeaderList,siteGeographicalDetail,secondSubHeaderList);
		   return map;
	   }
	   private LinkedList<String> addBlankString(LinkedList<String> headerList,Integer noOfIteration){
		   for(int i=0;i<=noOfIteration;i++) {
			   headerList.add(ForesightConstants.BLANK_STRING);
		 }
		   return headerList;
	   }
	   private Map<String,LinkedList<String>> getSecondHeaderForAdditionalSector(LinkedList<String> secondHeaderList,SiteGeographicalDetail siteGeographicalDetail,LinkedList<String> secondSubHeaderList) {
		   if(siteGeographicalDetail.getCheckAlphaAddSectorId4() != null) {
			   secondHeaderList.add(InfraConstants.ALPHA_ADDITIONAL);
			   addBlankString(secondHeaderList, 1);
			   secondSubHeaderList= getSecondSubHeaderData(secondSubHeaderList);
		   }
		   if(siteGeographicalDetail.getCheckBetaAddSectorId5() != null ) {
			   secondHeaderList.add(InfraConstants.BETA_ADDITIONAL);
			   addBlankString(secondHeaderList, 1);
			   secondSubHeaderList= getSecondSubHeaderData(secondSubHeaderList);
			  }
		   if(siteGeographicalDetail.getCheckGammaAddSectorId6() != null ) {
			   secondHeaderList.add(InfraConstants.GAMMA_ADDITIONAL);
			   addBlankString(secondHeaderList, 1);
			   secondSubHeaderList= getSecondSubHeaderData(secondSubHeaderList);
			  }
		   Map<String,LinkedList<String>> map=getMapOfHeadersForOverview(secondHeaderList,secondSubHeaderList);
		   return map;
	   }
	   private Map<String,LinkedList<String>> getMapOfHeadersForOverview(LinkedList<String> secondHeaderList,LinkedList<String> secondSubHeaderList) {
		   Map<String,LinkedList<String>> map=new HashMap<>();
		   map.put("SecondHeader", secondHeaderList);
		   map.put("SecondSubHeader", secondSubHeaderList);
		   return map;
	   }
	   private LinkedList<String> getSecondSubHeaderForOverview() {
		   LinkedList<String> secondSubHeaderList = new LinkedList<>();
		   for(int i=0;i<=2;i++) {
			   secondSubHeaderList= getSecondSubHeaderData(secondSubHeaderList);
		   }
		   return secondSubHeaderList;
	   }


	private LinkedList<String> getSecondSubHeaderData(LinkedList<String> secondSubHeaderList) {
		secondSubHeaderList.add(ForesightConstants.STATUS_CAMEL);
		   secondSubHeaderList.add(ForesightConstants.DATE_CAMEL);
		   secondSubHeaderList.add(ForesightConstants.BANDWIDTH_CAMAL_CASE);
		   return secondSubHeaderList;
	}
	   private void generateRFParameterSheet(XSSFWorkbook workbook, String neName, String neFrequency, Map<String, List<SectorSummaryWrapper>> siteSummaryOverviewWrapper, Map<String, XSSFCellStyle> style) throws Exception {
		   logger.info("Going to create RF Parameter sheet for neName {} ,neFrequency {} ",neName,neFrequency);
		   try {
			   int rowNum = 0;
			   XSSFSheet sheet = workbook.createSheet("RF Parameters");
			   String [] firstHeader = {"RF Parameters","","","","","",""};
			   String[] secondHeader = { "", "Alpha","Beta","Gamma","Alpha Additional","Beta Additional","Gamma Additional"};
				   
			   setHeightOfRow(rowNum, sheet,20);
			   ReportUtils.createRow(sheet.createRow(rowNum), style.get("greenHeader"), false, sheet, firstHeader);
			   rowNum++;
			   setHeightOfRow(rowNum, sheet,20);
			   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.YELLOWHEADER), false, sheet, secondHeader);
			   rowNum++;
			   mergeColumnsForReport(sheet, firstHeader, 0);
			
			   if(siteSummaryOverviewWrapper.get(InfraConstants.RADIO_PARAMETER) != null ) {
				   List<SectorSummaryWrapper> overviewParameters =siteSummaryOverviewWrapper.get(InfraConstants.RADIO_PARAMETER);
				   for (SectorSummaryWrapper sectorSummaryWrapper : overviewParameters) {
					   if(sectorSummaryWrapper.getParameter().equalsIgnoreCase(InfraConstants.CLUTTER_CATEGORY)) {
					   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet, InfraConstants.CLUTTER_CATEGORY,
							   sectorSummaryWrapper.getAlpha() != null ?  sectorSummaryWrapper.getAlpha() :ForesightConstants.HIPHEN, 
							   sectorSummaryWrapper.getBeta() != null ?  sectorSummaryWrapper.getBeta() :ForesightConstants.HIPHEN, 
							   sectorSummaryWrapper.getGamma() != null ?  sectorSummaryWrapper.getGamma() :ForesightConstants.HIPHEN,
							   sectorSummaryWrapper.getAddAlpha() != null ?  sectorSummaryWrapper.getAddAlpha() :ForesightConstants.HIPHEN, 
							   sectorSummaryWrapper.getAddBeta() != null ?  sectorSummaryWrapper.getAddBeta() :ForesightConstants.HIPHEN, 
							   sectorSummaryWrapper.getAddGamma() != null ?  sectorSummaryWrapper.getAddGamma() :ForesightConstants.HIPHEN);
							   rowNum++;
					   }
 					   if(sectorSummaryWrapper.getParameter().equalsIgnoreCase(InfraConstants.PROPAGATION_MODEL)) {
						   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet, InfraConstants.PROPAGATION_MODEL,
								   sectorSummaryWrapper.getAlpha() != null ?  sectorSummaryWrapper.getAlpha() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getBeta() != null ?  sectorSummaryWrapper.getBeta() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getGamma() != null ?  sectorSummaryWrapper.getGamma() :ForesightConstants.HIPHEN,
										   sectorSummaryWrapper.getAddAlpha() != null ?  sectorSummaryWrapper.getAddAlpha() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getAddBeta() != null ?  sectorSummaryWrapper.getAddBeta() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getAddGamma() != null ?  sectorSummaryWrapper.getAddGamma() :ForesightConstants.HIPHEN);
						   rowNum++;
			    	   }
 					  if(sectorSummaryWrapper.getParameter().equalsIgnoreCase(InfraConstants.TX_POWER)) {
						   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet, InfraConstants.TX_POWER,
								   sectorSummaryWrapper.getAlpha() != null ?  sectorSummaryWrapper.getAlpha() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getBeta() != null ?  sectorSummaryWrapper.getBeta() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getGamma() != null ?  sectorSummaryWrapper.getGamma() :ForesightConstants.HIPHEN,
										   sectorSummaryWrapper.getAddAlpha() != null ?  sectorSummaryWrapper.getAddAlpha() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getAddBeta() != null ?  sectorSummaryWrapper.getAddBeta() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getAddGamma() != null ?  sectorSummaryWrapper.getAddGamma() :ForesightConstants.HIPHEN);
						   rowNum++;
			    	   }
 					 if(sectorSummaryWrapper.getParameter().equalsIgnoreCase(InfraConstants.EIRP)) {
						   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet, InfraConstants.EIRP,
								   sectorSummaryWrapper.getAlpha() != null ?  sectorSummaryWrapper.getAlpha() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getBeta() != null ?  sectorSummaryWrapper.getBeta() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getGamma() != null ?  sectorSummaryWrapper.getGamma() :ForesightConstants.HIPHEN,
										   sectorSummaryWrapper.getAddAlpha() != null ?  sectorSummaryWrapper.getAddAlpha() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getAddBeta() != null ?  sectorSummaryWrapper.getAddBeta() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getAddGamma() != null ?  sectorSummaryWrapper.getAddGamma() :ForesightConstants.HIPHEN);
						   rowNum++;
			    	   }
 					if(sectorSummaryWrapper.getParameter().equalsIgnoreCase(InfraConstants.PILOT_CHANNEL_TXPOWER)) {
						   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet, InfraConstants.PILOT_CHANNEL_TXPOWER,
								   sectorSummaryWrapper.getAlpha() != null ?  sectorSummaryWrapper.getAlpha() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getBeta() != null ?  sectorSummaryWrapper.getBeta() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getGamma() != null ?  sectorSummaryWrapper.getGamma() :ForesightConstants.HIPHEN,
										   sectorSummaryWrapper.getAddAlpha() != null ?  sectorSummaryWrapper.getAddAlpha() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getAddBeta() != null ?  sectorSummaryWrapper.getAddBeta() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getAddGamma() != null ?  sectorSummaryWrapper.getAddGamma() :ForesightConstants.HIPHEN);
						   rowNum++;
			    	   }
 					if(sectorSummaryWrapper.getParameter().equalsIgnoreCase(InfraConstants.RADIUS_THRESHOLD)) {
						   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet, InfraConstants.RADIUS_THRESHOLD,
								   sectorSummaryWrapper.getAlpha() != null ?  sectorSummaryWrapper.getAlpha() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getBeta() != null ?  sectorSummaryWrapper.getBeta() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getGamma() != null ?  sectorSummaryWrapper.getGamma() :ForesightConstants.HIPHEN,
										   sectorSummaryWrapper.getAddAlpha() != null ?  sectorSummaryWrapper.getAddAlpha() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getAddBeta() != null ?  sectorSummaryWrapper.getAddBeta() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getAddGamma() != null ?  sectorSummaryWrapper.getAddGamma() :ForesightConstants.HIPHEN);
						   rowNum++;
			    	   }
 					if(sectorSummaryWrapper.getParameter().equalsIgnoreCase(InfraConstants.RSRP_THRESHOLD)) {
						   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet, InfraConstants.RSRP_THRESHOLD,
								   sectorSummaryWrapper.getAlpha() != null ?  sectorSummaryWrapper.getAlpha() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getBeta() != null ?  sectorSummaryWrapper.getBeta() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getGamma() != null ?  sectorSummaryWrapper.getGamma() :ForesightConstants.HIPHEN,
										   sectorSummaryWrapper.getAddAlpha() != null ?  sectorSummaryWrapper.getAddAlpha() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getAddBeta() != null ?  sectorSummaryWrapper.getAddBeta() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getAddGamma() != null ?  sectorSummaryWrapper.getAddGamma() :ForesightConstants.HIPHEN);
						   rowNum++;
			    	   }
 					if(sectorSummaryWrapper.getParameter().equalsIgnoreCase(InfraConstants.BASE_CHANNEL_FREQ)) {
						   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet, InfraConstants.BASE_CHANNEL_FREQ,
								   sectorSummaryWrapper.getAlpha() != null ?  sectorSummaryWrapper.getAlpha() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getBeta() != null ?  sectorSummaryWrapper.getBeta() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getGamma() != null ?  sectorSummaryWrapper.getGamma() :ForesightConstants.HIPHEN,
										   sectorSummaryWrapper.getAddAlpha() != null ?  sectorSummaryWrapper.getAddAlpha() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getAddBeta() != null ?  sectorSummaryWrapper.getAddBeta() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getAddGamma() != null ?  sectorSummaryWrapper.getAddGamma() :ForesightConstants.HIPHEN);
						   rowNum++;
			    	   }
 					if(sectorSummaryWrapper.getParameter().equalsIgnoreCase(InfraConstants.CARRIER)) {
						   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet, InfraConstants.CARRIER,
								   sectorSummaryWrapper.getAlpha() != null ?  sectorSummaryWrapper.getAlpha() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getBeta() != null ?  sectorSummaryWrapper.getBeta() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getGamma() != null ?  sectorSummaryWrapper.getGamma() :ForesightConstants.HIPHEN,
										   sectorSummaryWrapper.getAddAlpha() != null ?  sectorSummaryWrapper.getAddAlpha() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getAddBeta() != null ?  sectorSummaryWrapper.getAddBeta() :ForesightConstants.HIPHEN, 
										   sectorSummaryWrapper.getAddGamma() != null ?  sectorSummaryWrapper.getAddGamma() :ForesightConstants.HIPHEN);
						   rowNum++;
			    	   }
				   }
			   }
  	           sheet.setColumnWidth(0, 4200);
			   sheet.setColumnWidth(1, 6000);
			   sheet.setColumnWidth(2, 6000);
			   sheet.setColumnWidth(3, 6000);
		   } catch (Exception exception) {
			   logger.error("Unable to create Overviewsheet", ExceptionUtils.getStackTrace(exception));
		   }
	   }
	   
	   public static Map<String, XSSFCellStyle> createStylesForReport(Workbook wb) {
			Map<String, XSSFCellStyle> styles = new HashMap<>();
			XSSFCellStyle style;

			Color color1 = Color.decode(InfraConstants.ORANGE_COLOR_CODE);
			Color color2 = Color.decode(InfraConstants.PINK_COLOR_CODE);
			Color color3 = Color.decode(InfraConstants.YELLOW_COLOR_CODE);
			Color color4 = Color.decode(InfraConstants.GREEN_COLOR_CODE);
			Color color5 = Color.decode(InfraConstants.BLUE_COLOR_CODE);

			XSSFColor orange = new XSSFColor(color1);
			XSSFColor pink = new XSSFColor(color2);
			XSSFColor yellow = new XSSFColor(color3);
			XSSFColor green = new XSSFColor(color4);
			XSSFColor blue = new XSSFColor(color5);

			XSSFFont headerFont = (XSSFFont) wb.createFont();
			headerFont.setFontHeightInPoints((short) 11);
			headerFont.setFontName(ForesightConstants.FONTNAME);

			XSSFFont dataFont = (XSSFFont) wb.createFont();
			dataFont.setFontHeightInPoints((short) 10);
			dataFont.setFontName(ForesightConstants.FONTNAME);

			XSSFFont boldFont = (XSSFFont) wb.createFont();
			boldFont.setFontHeightInPoints((short) 11);
			boldFont.setFontName(ForesightConstants.FONTNAME);
			boldFont.setBold(true);

			style = (XSSFCellStyle) wb.createCellStyle();
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			style.setFillForegroundColor(orange);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style.setFont(headerFont);
			style.setWrapText(true);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style.setBorderColor(BorderSide.LEFT, new XSSFColor(new java.awt.Color(0, 0, 0)));

			style.setBorderColor(BorderSide.RIGHT, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.TOP, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.BOTTOM, new XSSFColor(new java.awt.Color(0, 0, 0)));
			styles.put(ForesightConstants.ORANGEHEADER, style);

			style = (XSSFCellStyle) wb.createCellStyle();
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			style.setFillForegroundColor(pink);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style.setFont(dataFont);
			style.setWrapText(true);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style.setBorderColor(BorderSide.LEFT, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.RIGHT, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.TOP, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.BOTTOM, new XSSFColor(new java.awt.Color(0, 0, 0)));
			styles.put(InfraConstants.PINK_HEADER, style);

			style = (XSSFCellStyle) wb.createCellStyle();
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			style.setFillForegroundColor(yellow);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style.setFont(dataFont);
			style.setWrapText(true);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style.setBorderColor(BorderSide.LEFT, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.RIGHT, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.TOP, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.BOTTOM, new XSSFColor(new java.awt.Color(0, 0, 0)));
			styles.put(ForesightConstants.YELLOWHEADER, style);

			style = (XSSFCellStyle) wb.createCellStyle();
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			style.setFillForegroundColor(green);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style.setFont(dataFont);
			style.setWrapText(true);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style.setBorderColor(BorderSide.LEFT, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.RIGHT, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.TOP, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.BOTTOM, new XSSFColor(new java.awt.Color(0, 0, 0)));
			styles.put(InfraConstants.GREEN_HEADER, style);

			style = (XSSFCellStyle) wb.createCellStyle();
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			style.setFillForegroundColor(yellow);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style.setFont(headerFont);
			style.setWrapText(true);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style.setBorderColor(BorderSide.LEFT, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.RIGHT, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.TOP, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.BOTTOM, new XSSFColor(new java.awt.Color(0, 0, 0)));
			styles.put(InfraConstants.YELLOWBOLD_HEADER, style);

			style = (XSSFCellStyle) wb.createCellStyle();
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			style.setFillForegroundColor(blue);
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style.setFont(dataFont);
			style.setWrapText(true);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style.setBorderColor(BorderSide.LEFT, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.RIGHT, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.TOP, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.BOTTOM, new XSSFColor(new java.awt.Color(0, 0, 0)));
			styles.put(ForesightConstants.BLUEHEADER, style);

			style = (XSSFCellStyle) wb.createCellStyle();
			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
			style.setFont(dataFont);
			style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
			style.setBorderRight(XSSFCellStyle.BORDER_THIN);
			style.setBorderTop(XSSFCellStyle.BORDER_THIN);
			style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
			style.setBorderColor(BorderSide.LEFT, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.RIGHT, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.TOP, new XSSFColor(new java.awt.Color(0, 0, 0)));
			style.setBorderColor(BorderSide.BOTTOM, new XSSFColor(new java.awt.Color(0, 0, 0)));
			styles.put(ForesightConstants.DATACELL_KEY, style);
			return styles;
		}
	   
	   private String createFileNameForSectorPropertyReport(String neName, String  neFrequency) {
		   String excelFileName = null;
		   logger.info("get file name for sector property report nename : {}, nefrequency : {}",neName,neFrequency);
		   excelFileName =  InfraConstants.SECTOR_PROPERTY;
		   if(neName != null) {
		   excelFileName+= ForesightConstants.UNDERSCORE + neName;
		   } if (neFrequency != null) {
			   excelFileName += ForesightConstants.UNDERSCORE + neFrequency.toUpperCase() + ForesightConstants.MEGA_HERTZ;
		   }
		   excelFileName += ForesightConstants.EXCEL_EXTENSION;
		   logger.debug("generated Sector Property report file name = {}", excelFileName);
		   return excelFileName;
	   }
	   private String createFileNameForSitePropertyReport(String neName) {
		   String excelFileName = null;
		   excelFileName =  InfraConstants.SITE_PROPERTY  ;
		   if(neName != null) {
			   excelFileName += ForesightConstants.UNDERSCORE + neName;
		   }
		   excelFileName += ForesightConstants.EXCEL_EXTENSION;
		   logger.debug("generated Sector Property report file name = {}", excelFileName);
		   return excelFileName;
	   }

	   private LinkedList<String> getFirstHeaderOverview(List<String> neFrequencyList,Map secondHeaderMap) {
		   LinkedList<String> firstHeader = new LinkedList<>();
		   if (neFrequencyList != null && !neFrequencyList.isEmpty()) {
			   for (String neFrequency : neFrequencyList) {
				   firstHeader.add(neFrequency + ForesightConstants.SPACE + InfraConstants.BAND_CAMEL);
				   firstHeader.add(ForesightConstants.BLANK_STRING);
				   firstHeader.add(ForesightConstants.BLANK_STRING);
				   if(secondHeaderMap.get(InfraConstants.FOURTHSECTOR + ForesightConstants.UNDERSCORE + InfraConstants.PROGRESSSTATE_CAP+ForesightConstants.UNDERSCORE+neFrequency) != null) {
					   firstHeader.add(InfraConstants.FOURTH_SECTOR + ForesightConstants.SPACE + neFrequency + ForesightConstants.SPACE + InfraConstants.BAND_CAMEL);
					   firstHeader.add(ForesightConstants.BLANK_STRING);
					   firstHeader.add(ForesightConstants.BLANK_STRING);
				 }
			   }
			}
		   return firstHeader;
	   }

	   private LinkedList<String> getSecondHeaderOverview(List<String> neFrequencyList,Map secondHeaderMap) {
		 
		   LinkedList<String> secondHeader = new LinkedList<>();
		   if (neFrequencyList != null && !neFrequencyList.isEmpty()) {
			   for (String neFrequency : neFrequencyList) {
				   getSecondSubHeaderData(secondHeader);
				   if(secondHeaderMap.get(InfraConstants.FOURTHSECTOR + ForesightConstants.UNDERSCORE + InfraConstants.PROGRESSSTATE_CAP+ForesightConstants.UNDERSCORE+neFrequency) != null) {
						 getSecondSubHeaderData(secondHeader);
			}
			   }
		   }
		   return secondHeader;
	   }

	   private LinkedList<String> getThirdHeaderOverview(Map secondHeaderMap, List<String> neFrequencyList) {
		   LinkedList<String> thirdHeader = new LinkedList<>();
		   if(neFrequencyList != null && !neFrequencyList.isEmpty()) {
			   for (String neFrequency : neFrequencyList) {
				   thirdHeader.add(secondHeaderMap.get(InfraConstants.PROGRESSSTATE_CAP+ForesightConstants.UNDERSCORE+neFrequency) != null ? secondHeaderMap.get(InfraConstants.PROGRESSSTATE_CAP+ForesightConstants.UNDERSCORE+neFrequency).toString() : ForesightConstants.HIPHEN);
				   thirdHeader.add(secondHeaderMap.get(InfraConstants.ONAIRDATE+ForesightConstants.UNDERSCORE+neFrequency) != null ? secondHeaderMap.get(InfraConstants.ONAIRDATE+ForesightConstants.UNDERSCORE+neFrequency).toString() : ForesightConstants.HIPHEN);
				   thirdHeader.add(secondHeaderMap.get(InfraConstants.BANDWIDTH+ForesightConstants.UNDERSCORE+neFrequency) != null ? secondHeaderMap.get(InfraConstants.BANDWIDTH+ForesightConstants.UNDERSCORE+neFrequency).toString() : ForesightConstants.HIPHEN);
			       if(secondHeaderMap.get(InfraConstants.FOURTHSECTOR + ForesightConstants.UNDERSCORE + InfraConstants.PROGRESSSTATE_CAP+ForesightConstants.UNDERSCORE+neFrequency) != null) {
			    	   thirdHeader.add(secondHeaderMap.get(InfraConstants.FOURTHSECTOR + ForesightConstants.UNDERSCORE + InfraConstants.PROGRESSSTATE_CAP+ForesightConstants.UNDERSCORE+neFrequency) != null ? secondHeaderMap.get(InfraConstants.FOURTHSECTOR + ForesightConstants.UNDERSCORE + InfraConstants.PROGRESSSTATE_CAP+ForesightConstants.UNDERSCORE+neFrequency).toString() : ForesightConstants.HIPHEN);
					   thirdHeader.add(secondHeaderMap.get(InfraConstants.FOURTHSECTOR + ForesightConstants.UNDERSCORE+InfraConstants.ONAIRDATE+ForesightConstants.UNDERSCORE+neFrequency) != null ? secondHeaderMap.get(InfraConstants.FOURTHSECTOR + ForesightConstants.UNDERSCORE+InfraConstants.ONAIRDATE+ForesightConstants.UNDERSCORE+neFrequency).toString() : ForesightConstants.HIPHEN);
					    thirdHeader.add(secondHeaderMap.get(InfraConstants.FOURTHSECTOR +ForesightConstants.UNDERSCORE+ InfraConstants.BANDWIDTH+ForesightConstants.UNDERSCORE+neFrequency) != null ? secondHeaderMap.get(InfraConstants.FOURTHSECTOR +ForesightConstants.UNDERSCORE+ InfraConstants.BANDWIDTH+ForesightConstants.UNDERSCORE+neFrequency).toString() : ForesightConstants.HIPHEN); 
			       }
			}
		   }
		   
		   return thirdHeader;
	   }

	   private void generateSiteMilestones(XSSFWorkbook workbook,Map<String, XSSFCellStyle> style,SiteLayerSelection siteLayerSelection) throws Exception {
		 int rowNum = 0;
			XSSFSheet sheet = workbook.createSheet(InfraConstants.SITE_MILESTONES);
			ISiteVisualizationService iSiteVisualizationService = ApplicationContextProvider.getApplicationContext().getBean(ISiteVisualizationService.class);
			List<Map> map=iSiteVisualizationService.getNETaskDetailData(siteLayerSelection);
			String siteOverviewHeaders=iSystemConfigurationDao.getValueByNameAndType("SITE_PROPERTY_REPORT","SITE_PROPERTY_MILESTONE_HEADER");
		  	if(siteOverviewHeaders != null) {
		     Map<String, String> jsonMap = new HashMap<String, String>();
		  		ObjectMapper mapper = new ObjectMapper();
		        jsonMap = mapper.readValue(siteOverviewHeaders,new TypeReference<Map<String, String>>(){});
		        List<String> firstHeaderList=new ArrayList(jsonMap.keySet());	
				String[] firstHeader = firstHeaderList.toArray(new String[0]);
				setHeightOfRow(rowNum, sheet,20);
				ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.ORANGEHEADER), false, sheet, firstHeader);
				rowNum++;
				if(map != null && !map.isEmpty()) {
				for (Map data : map) {
					XSSFRow row=sheet.createRow(rowNum); 
					row = getRow(rowNum, sheet);
					Integer colNum=0;
				for (String header : firstHeaderList) {
					if(jsonMap.get(header) != null) {
					String columnKey=jsonMap.get(header).toString();
					if(columnKey.contains("Date")) {
					ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colNum,data.get(columnKey) != null ?InfraUtils.getSiteTaskDateForSectorProperty((Date)data.get(columnKey),true).toString() : ForesightConstants.HIPHEN);
					}else {
					ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colNum,data.get(columnKey) != null ? data.get(columnKey).toString() : ForesightConstants.HIPHEN); 
					}
					colNum++;
					}
				}
				rowNum++;
				}
				}
				
				rowNum+=2;
	         }
				setColumnWidthForReport(sheet);

		   
		   
		   
		   
		   
		   /*if (neType.equalsIgnoreCase(InfraConstants.MACRO_CELL)) {
				neType = InfraConstants.MACRO;
			}
			if (neType.equalsIgnoreCase(InfraConstants.IBS_CELL)) {
				neType = InfraConstants.IBS_SITE;
			}
			if (neType.equalsIgnoreCase(InfraConstants.ODSC_CELL)) {
				neType = InfraConstants.ODSC_SITE;
			}
			if (neType.equalsIgnoreCase(InfraConstants.PICO_CELL)) {
				neType = InfraConstants.PICO_SITE;
			}
			if (neType.equalsIgnoreCase(InfraConstants.SHOOTER_CELL)) {
				neType = InfraConstants.SHOOTER_SITE;
			}
			if (neType.equalsIgnoreCase(InfraConstants.GALLERY_CELL)) {
				neType = InfraConstants.GALLERY_SITE;
			}
			logger.info("inside generateSiteMilestones.neName:{},neType:{}",neName,neType);
		   List<NETaskDetailWrapper> neTaskDetailWrapperList=iNetworkElementDao.getNETaskDetailData(neName, neType);
		   logger.info("neTaskDetailWrapperList : {}",neTaskDetailWrapperList.size());
		   String[] firstHeader = { "Ne Frequency", "Task Name", "Completion Date","Aging(No. of days)"};
		   setHeightOfRow(rowNum, sheet, 60);
		   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.ORANGEHEADER), false, sheet, firstHeader);
		   rowNum++;
		   if(neTaskDetailWrapperList != null && !neTaskDetailWrapperList.isEmpty()) {
		   for (NETaskDetailWrapper neTaskDetailWrapper : neTaskDetailWrapperList) {
			ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet,
					neTaskDetailWrapper.getNeFrequency() != null ? neTaskDetailWrapper.getNeFrequency() : ForesightConstants.HIPHEN,
					neTaskDetailWrapper.getTaskName() != null ? neTaskDetailWrapper.getTaskName() : ForesightConstants.HIPHEN,
					neTaskDetailWrapper.getActualEndDate() != null ? InfraUtils.getSiteTaskDateForSectorProperty(neTaskDetailWrapper.getActualEndDate(),true) : ForesightConstants.HIPHEN,
					neTaskDetailWrapper.getTaskDay() != null ? neTaskDetailWrapper.getTaskDay() : ForesightConstants.HIPHEN);
			   rowNum += 1;
		}
	   }
		   setColumnWidthForReport(sheet);*/
		  }

	private void setHeightOfRow(int rowNum, XSSFSheet sheet,Integer height) {
		sheet.createRow(rowNum).setHeightInPoints(height);
	}

	   private String checkNullStringforReport(String value) {
		   String REPLACEMENT_DASH = InfraConstants.DASH;
		   if (value != null && !value.isEmpty() && !value.equalsIgnoreCase(ForesightConstants.NULL_STRING)
				   && !value.equals(ForesightConstants.HIPHEN) && !value.equals(ForesightConstants.DOT)) {
			   return value.replace(InfraConstants.COMMA, InfraConstants.SPACE);
		   } else {
			   return REPLACEMENT_DASH;
		   }
	   }

	   @Override
	   public void setColumnWidthForReport(XSSFSheet sheet) {
		   for (int i = 0; i <= 60; i++) {
			   try {
				   sheet.setColumnWidth(i, 4000);
			   } catch (Exception e) {
				   logger.error("error in setting Column Width for RanDetail Sheet Column number {}", i);
			   }
		   }
	   }

	   private void mergeColumnsForReport(XSSFSheet sheet, String Array[], Integer headerRow) {
		   logger.debug("inside createMergedColumns for Sheet Creation");
		   int lower = 0;
		   int upper = 0;
		   for (int i = 1; i < Array.length; i++) {
			   try {
				   if (!Array[i].isEmpty() == true) {
					   upper = i - 1;
					   sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, lower, upper));
					   lower = i;
				   }
				   if (i + 1 == Array.length) {
					   upper = i;
					   sheet.addMergedRegion(new CellRangeAddress(headerRow, headerRow, lower, upper));
				   }
			   } catch (Exception e) {
				   logger.info("Error in merging column for Sheet Exception {}", ExceptionUtils.getStackTrace(e));
			   }
		   }
	   }
	   
	   private void mergeColumnsForEMSSheet(XSSFSheet sheet) {
		   logger.debug("inside mergeColumnsForEMSSheet for Sheet Creation");
		   sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
		   sheet.addMergedRegion(new CellRangeAddress(5, 5, 0, 7));
		   sheet.addMergedRegion(new CellRangeAddress(10, 10, 0, 5));
		   sheet.addMergedRegion(new CellRangeAddress(15, 15, 0, 5));
	   }

	   private List<String> getSheetNameFromSystemConfiguration(String name,String type) {
		  logger.info("get sheet parameters from systemconfiguration name : {} type : {}",name,type);
		  try {
		  SystemConfiguration systemConfiguration= iSystemConfigurationDao.getSystemConfigurationDetailForReport(name, type);
		  String value=systemConfiguration.getValue();
		  value=value.replaceAll("[\\[\\](){}]", ForesightConstants.BLANK_STRING);
		  value=value.replaceAll("\"", ForesightConstants.BLANK_STRING);
		  if(value != null) {
		  List<String> sheetName=Arrays.asList(value.split(ForesightConstants.COMMA));
		  logger.info("sheet name for report : {}",sheetName);
		  return sheetName;
		  }else {
			  logger.info("no value is found for name : {} and type : {}",name,type);
		  }
		 
		  }catch(Exception exception) {
			  logger.error("Error in getting Sheet parameters .Exception {}", ExceptionUtils.getStackTrace(exception));
		  }
		
		return null;
		
	   }   
@Override	
public Map<String,String> getMappingForGeographies() {
	Map<String, String> map = new HashMap<>();
	try {
		map.put(ForesightConstants.GEOGRAPHY_L1, iSystemConfigurationDao.getValueByNameAndType(ForesightConstants.GEOGRAPHY, ForesightConstants.GEOGRAPHY_L1));
		map.put(ForesightConstants.GEOGRAPHY_L2, iSystemConfigurationDao.getValueByNameAndType(ForesightConstants.GEOGRAPHY, ForesightConstants.GEOGRAPHY_L2));
		map.put(ForesightConstants.GEOGRAPHY_L3, iSystemConfigurationDao.getValueByNameAndType(ForesightConstants.GEOGRAPHY, ForesightConstants.GEOGRAPHY_L3));
		map.put(ForesightConstants.GEOGRAPHY_L4, iSystemConfigurationDao.getValueByNameAndType(ForesightConstants.GEOGRAPHY, ForesightConstants.GEOGRAPHY_L4));
	} catch (Exception exception) {
		logger.error("Exception in getting Mapping for sector Information {} ", ExceptionUtils.getStackTrace(exception));

	}
	return map;
}
@Override
public byte[] getFileByPath(String fileName) {
	logger.info("Going to get file by name: {}", fileName);
	byte[] fileLength = null;
	byte[] toReturn = null;
	String filePath = System.getProperty(ForesightConstants.TOMCAT_PATH) + ForesightConstants.FORWARD_SLASH + ConfigUtils.getString(ConfigEnum.SITE_REPORT_FILE_PATH.getValue());
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	File file = new File(filePath + fileName);
	try(FileInputStream fis=new FileInputStream(file);) {
		fileLength = new byte[(int) file.length()];
		fis.read(fileLength);
		baos.write(fileLength);
		toReturn = baos.toByteArray();
		file.delete();
		new File(filePath).delete();
		fis.close();
		return toReturn;
	} catch (Exception e) {
		logger.error("Error in Getting file Message{} Exception {} ", e.getMessage(), ExceptionUtils.getStackTrace(e));
	}
	return toReturn;
}

private List<Object[]> getDataForPolygon(List<List<List<Double>>> polygons, List<String> frequencyList, List<NEType> neTypeList,List<NEStatus> neStatusList) {
	logger.info("inside getDataForPolygon.");
	Map<String, Double> viewportMap = new HashMap<>();
	List<Object[]>  siteDetailData=new ArrayList<>();
	try {
	logger.info("No. of polygons are {}", polygons.size());
	for (List<List<Double>> polygon : polygons) {
	GISGeometry gispolygon = new GIS2DPolygon(polygon);
	Corner bounds = BoundaryUtils.getCornerOfBoundary(polygon);
	Double minlat = bounds.getMinLatitude();
	Double maxlat = bounds.getMaxLatitude();
	Double minlon = bounds.getMinLongitude();
	Double maxlon = bounds.getMaxLongitude();
	viewportMap.put(InfraConstants.SOUTHWEST_LATITUDE_KEY, minlat);
	viewportMap.put(InfraConstants.NORTHEAST_LATITUDE_KEY, maxlat);
	viewportMap.put(InfraConstants.SOUTHWEST_LONGITUDE_KEY, minlon);
	viewportMap.put(InfraConstants.NORTHEAST_LONGITUDE_KEY, maxlon);
	List<Object[]>  siteDetailDataList=iNetworkElementDao.getSiteDetailReportData(frequencyList,null, neStatusList, neTypeList,viewportMap.get(InfraConstants.SOUTHWEST_LONGITUDE_KEY), viewportMap.get(InfraConstants.SOUTHWEST_LATITUDE_KEY),viewportMap.get(InfraConstants.NORTHEAST_LONGITUDE_KEY),viewportMap.get(InfraConstants.NORTHEAST_LATITUDE_KEY));
	logger.info("Number of sites under Polygon {}", siteDetailDataList.size());
	if(siteDetailDataList != null && !siteDetailDataList.isEmpty()) {
	for (Object[] neBandDetail : siteDetailDataList) {
	try {
		if(neBandDetail != null) {
			if(neBandDetail[7] != null && neBandDetail[8] != null)	{
	if (gispolygon.contains(new GISPoint(new LatLng(Double.valueOf(neBandDetail[7].toString()), Double.valueOf(neBandDetail[8].toString()))))) {
		siteDetailData.add(neBandDetail);
		}
	}
	}
	} catch (Exception exception) {
	logger.error("Unable to get count of kpi for object {} due to exception {}", neBandDetail, Utils.getStackTrace(exception));
	}
	
	}
	}
	logger.info("siteDetailData size for polygon : {}",siteDetailData.size());
	}

	} catch (Exception exception) {
	logger.error("Unable to get kpimapcount of polygons due to exception {}", Utils.getStackTrace(exception));
	}
	return siteDetailData;
}

private String createFileNameForSiteDetailReport() {
	   String excelFileName = null;
	   SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);  
	   excelFileName =  InfraConstants.SITE_DETAIL_REPORT + ForesightConstants.UNDERSCORE + formatter.format(new Date()) ;
	   excelFileName += ForesightConstants.EXCEL_EXTENSION;
	   logger.info("Filename : {}", excelFileName);
	   return excelFileName;
}
private String createFileNameForSectorDetailReport() {
	   String excelFileName = null;
	   SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);  
	   excelFileName = InfraConstants.SECTOR_DETAIL_REPORT + ForesightConstants.UNDERSCORE + formatter.format(new Date()) ;
	   excelFileName += ForesightConstants.EXCEL_EXTENSION;
	   logger.info("Filename : {}", excelFileName);
	   return excelFileName;
}
  private Map<String, String> getHeaderForSiteAndSectorReport() {
  	Map<String, String> map = new HashMap<>();
  	try {
  		String siteDetailheaders=iSystemConfigurationDao.getValueByNameAndType(InfraConstants.SITE_REPORT, InfraConstants.SITE_DETAIL_HEADER);
  		String sectorDetailheaders=iSystemConfigurationDao.getValueByNameAndType(InfraConstants.SECTOR_REPORT, InfraConstants.SECTOR_DETAIL_HEADER);
  		if(siteDetailheaders != null) {
  	    map.put(InfraConstants.SITE_DETAIL_HEADER, siteDetailheaders);
  		}
  		if(sectorDetailheaders != null) {
	    map.put(InfraConstants.SECTOR_DETAIL_HEADER, sectorDetailheaders);
  		}
  	} catch (Exception exception) {
  		logger.error("Exception in getting Mapping for sector Information {} ", ExceptionUtils.getStackTrace(exception));

  	}
  	return map;
  }
  private void setNefrequencyValueInHeaderForSiteDetail(List<String> firstHeaderList,List<String> secondHeaderListTemp,Map<String,List<String>> map,String header,List<String> neFrequencyList) {
	  if(neFrequencyList != null && !neFrequencyList.isEmpty()) {
	  for (String neFrequency : neFrequencyList) {
			firstHeaderList.add(neFrequency);
			firstHeaderList=addSpaceInFirstHeader(map,header,firstHeaderList);
			secondHeaderListTemp.addAll(map.get(header));
		}
  }
  }
  private void setValueInHeaderForSiteDetail(List<String> firstHeaderList,List<String> secondHeaderListTemp,Map<String,List<String>> map,String header) {
	  firstHeaderList.add(header);
	  firstHeaderList=addSpaceInFirstHeader(map,header,firstHeaderList);
	  secondHeaderListTemp.addAll(map.get(header));
  }
  private List<String> addSpaceInFirstHeader( Map<String,List<String>> map,String header,List<String> firstHeaderList){
	  for(int i=1;i<(map.get(header).size());i++) {
  		firstHeaderList.add(ForesightConstants.BLANK_STRING);
  	}
	  return firstHeaderList;
  }
  private Map getMapForHeaders(JSONArray jsonArray) {
	  Map<String,List<String>> map=new LinkedHashMap<String,List<String>>();
	  for(int i=0;i<jsonArray.length();i++) {
	    	try {
	    	JSONObject jsonObject = (JSONObject) jsonArray.get(i);
	    	String list=jsonObject.get(InfraConstants.SUBHEADERS).toString();
	    	List<String> subheaderList=getListFromJSON(list);      
	        map.put(jsonObject.get(InfraConstants.HEADER_NAME).toString(), subheaderList);
	    	}catch(Exception e) {
	    	
	    	}
	    }
	return map;
  }
  private List<String> getListFromJSON(String json) {
	  List<String> list=new ArrayList<>();
	  json=json.replaceAll("[\\[\\](){}]", ForesightConstants.BLANK_STRING);
	  json=json.replaceAll("\"", ForesightConstants.BLANK_STRING);
	  list=Arrays.asList(json.split(ForesightConstants.COMMA));
	  return list;
  }
  @SuppressWarnings("unchecked")
private Map<String,List<String>> getFirstAndSecondHeaderForSiteReport( Map<String, String> map,List<String> neFrequencyList,Map<String,String> geographyMap) {
	  String json=map.get(InfraConstants.SITE_DETAIL_HEADER);
	  List<String> headerList = new ArrayList<String>();
	  List<String> firstHeaderList = new ArrayList<String>();
	  List<String> secondHeaderListTemp = new ArrayList<String>();
	  List<String> secondHeaderList = new ArrayList<String>();
	  Map<String,List<String>> headerMap=new HashMap<>();
	  try {
	    JSONArray jsonArray = new JSONArray(json);
	    headerMap=getMapForHeaders(jsonArray);
	    headerList.addAll(headerMap.keySet());
	    for (String header : headerList) {
	    	
	    	if(header.equalsIgnoreCase(ForesightConstants.NE_FRFEQUENCY)) {
	    		setNefrequencyValueInHeaderForSiteDetail(firstHeaderList,secondHeaderListTemp,headerMap,header,neFrequencyList);
	    		
	    	}else {
	    		setValueInHeaderForSiteDetail(firstHeaderList,secondHeaderListTemp,headerMap,header);
	    		
	    	}
		}
	    secondHeaderListTemp.forEach(header->secondHeaderList.add(header.replaceAll("[\\(\\)\\[\\]\\{\\}]", ForesightConstants.BLANK_STRING)));
	    
	 } catch (JSONException e) {
	 e.printStackTrace();
	 }
	  headerMap.put(InfraConstants.FIRST_HEADER, firstHeaderList);
	  headerMap.put(InfraConstants.SECOND_HEADER, secondHeaderList);
	return headerMap;
  }
  private Map<String,List<String>> getFirstAndSecondHeaderForSectorReport( Map<String, String> map,List<String> neFrequencyList,Map<String,String> geographyMap) {
	  String json=map.get(InfraConstants.SECTOR_DETAIL_HEADER);
	  List<String> headerList = new ArrayList<String>();
	  List<String> firstHeaderList = new ArrayList<String>();
	  List<String> secondHeaderListTemp = new ArrayList<String>();
	  List<String> secondHeaderList = new ArrayList<String>();
	  Map<String,List<String>> headerMap=new HashMap<>();
	  try {
	    JSONArray jsonArray = new JSONArray(json);
	    headerMap=getMapForHeaders(jsonArray);
	    headerList.addAll(headerMap.keySet());
	    for (String header : headerList) {
	    	
	    		setValueInHeaderForSiteDetail(firstHeaderList,secondHeaderListTemp,headerMap,header);
		}
	    secondHeaderListTemp.forEach(header->secondHeaderList.add(header.replaceAll("[\\(\\)\\[\\]\\{\\}]", ForesightConstants.BLANK_STRING)));
	    
	 } catch (JSONException e) {
	 e.printStackTrace();
	 }
	  headerMap.put(InfraConstants.FIRST_HEADER, firstHeaderList);
	  headerMap.put(InfraConstants.SECOND_HEADER, secondHeaderList);
	return headerMap;
  }
  
@Override
  public Map<String, String> generateSiteDetailReport(Double southWestLong,Double southWestLat, Double northEastLong,Double northEastLat,SiteLayerSelection filterConfiguration) throws Exception {
		   logger.info("going to generate SiteDetail sheet.");
		   XSSFWorkbook workbook = new XSSFWorkbook();
		   Map<String, String> fileMap = new HashMap<>();
		   Map<String, List<String>> geographyNames = new HashMap<>();
		   Map<String,List<Map>> filters=new HashMap<>();
		   List<NEType> neTypeList = new ArrayList<>();
		   List<NEStatus> neStatusList = new ArrayList<>();
		   List<String> neFrequencyList = new ArrayList<>();
		   Map<String, XSSFCellStyle> style = createStylesForReport(workbook);
		   String filePath = null;
		   String fileName=createFileNameForSiteDetailReport();
		   try {
		   Map<String,String> geographyMap=getMappingForGeographies();
		   Map<String, String> map=getHeaderForSiteAndSectorReport();
		   if(filterConfiguration.getFilters() != null) {
		   filters = filterConfiguration.getFilters();
		   geographyNames=getGeographyNameFromWrapper(filters);
		   neTypeList=getneTypeFromMap(filters);
		   neStatusList=getneStatusFromMap(filters);
		   neFrequencyList=getneFrequencyFromMap(filters);
		   }
		   logger.info("geographynames: {},neTypeList: {},neStatusList: {},neFrequencyList: {}",geographyNames,neTypeList,neStatusList,neFrequencyList);
		   Map<String, List<String>> headerMap=getFirstAndSecondHeaderForSiteReport(map, neFrequencyList, geographyMap);
		   List<String> firstHeaderList = headerMap.get(InfraConstants.FIRST_HEADER);
		   List<String> secondHeaderList =  headerMap.get(InfraConstants.SECOND_HEADER);
		  if(filterConfiguration.getPolygon() != null && !filterConfiguration.getPolygon().isEmpty()) {
			  List<List<List<Double>>> polygons=filterConfiguration.getPolygon();
			  List<Object[]> networkElementList=getDataForPolygon(polygons,neFrequencyList,neTypeList,neStatusList);
			  getSiteDetailReportForPolygon(workbook, style, geographyMap, firstHeaderList,secondHeaderList,networkElementList);
				 
		  }else {
			  if(geographyNames != null && !geographyNames.isEmpty()) {
				  generateSheetForSiteDetailReport(workbook, style, geographyMap, firstHeaderList,secondHeaderList,null, null,null,null, geographyNames,neTypeList,neStatusList,neFrequencyList);
				  }else {
					  generateSheetForSiteDetailReport(workbook, style, geographyMap, firstHeaderList,secondHeaderList,southWestLong, southWestLat,northEastLong,northEastLat, null,neTypeList,neStatusList,neFrequencyList);
			 }
		   }
		   createDataSourceSheetForSiteDetail(workbook,style,geographyMap,secondHeaderList);
		   filePath = InfraUtils.exportExcelFile(workbook,ConfigUtils.getString(ConfigEnum.SITE_REPORT_FILE_PATH.getValue()), fileName, true);
		   logger.info("The uri is as follows -> "+filePath);
		   fileMap.put(InfraConstants.KEY_FILENAME, filePath);
		   } catch (Exception exception) {
		   logger.error("Unable to create Overviewsheet", Utils.getStackTrace(exception));
		   }
		return fileMap;
	}
  private void createDataSourceSheetForSiteDetail(XSSFWorkbook workbook,Map<String, XSSFCellStyle> style,Map<String,String> geographyMap,List<String> secondHeaderList) {
		logger.info("Going to create data source sheet for site details");
		XSSFSheet sheet = (XSSFSheet) workbook.createSheet(InfraConstants.DATA_SOURCES);
		int rowNum = 0;
		String[] firstRowData = { "Field Name", "Data Source" };
		secondHeaderList = secondHeaderList.stream().distinct().collect(Collectors.toList());
		try {
			XSSFRow firstRow = (XSSFRow) sheet.createRow(rowNum);
			ReportUtils.createRow(firstRow, style.get(ForesightConstants.BLUEHEADER), false, sheet, firstRowData);
			rowNum++;
			XSSFRow row = null;
			if(secondHeaderList != null && !secondHeaderList.isEmpty()) {
			for (String secondHeader : secondHeaderList) {
				row = (XSSFRow) sheet.createRow(rowNum);
				ReportUtils.createRow(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, secondHeader);
				rowNum++;
			}
			}
			int colIndex=1;
			for(int i=1;i<rowNum;i++) {
			row = getRow(i, sheet);
			Cell c=row.getCell(0);
			String cellvalue=c.getStringCellValue();
			if(cellvalue.equalsIgnoreCase("Site ID")  || 
					cellvalue.equalsIgnoreCase("Site Category") || cellvalue.equalsIgnoreCase("Host Name") || cellvalue.equalsIgnoreCase("IP Address")  || cellvalue.equalsIgnoreCase("CM") || cellvalue.equalsIgnoreCase("Vendor ID") || cellvalue.equalsIgnoreCase("Old Vendor ID") || cellvalue.equalsIgnoreCase("Property ID") ||
					cellvalue.equalsIgnoreCase("Nominal Site Location")) {
			ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colIndex, InfraConstants.CM);
			}
			if(cellvalue.equalsIgnoreCase("Source") || cellvalue.equalsIgnoreCase("Source Date")) {
			ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colIndex, InfraConstants.CM + ForesightConstants.FORWARD_SLASH + InfraConstants.PM + ForesightConstants.FORWARD_SLASH + InfraConstants.SITEFORGE);
			}
			if(cellvalue.equalsIgnoreCase("Site Status") || cellvalue.equalsIgnoreCase("SF ID") || cellvalue.equalsIgnoreCase("Site Type") || cellvalue.equalsIgnoreCase("Site Vendor") || cellvalue.equalsIgnoreCase("Latitude") ||
					cellvalue.equalsIgnoreCase("Longitude") || cellvalue.equalsIgnoreCase("Site Location") || cellvalue.equalsIgnoreCase("Site On-Air Date") || cellvalue.equalsIgnoreCase("Band Availability") || 
					cellvalue.equalsIgnoreCase("Band Status") || cellvalue.equalsIgnoreCase("Band Status Date") || cellvalue.equalsIgnoreCase("Backhaul Media") ||  cellvalue.equalsIgnoreCase("Site Name") || cellvalue.equalsIgnoreCase("Site Address") 
					|| cellvalue.equalsIgnoreCase("Site Class") || cellvalue.equalsIgnoreCase("Contact Person Name") || cellvalue.equalsIgnoreCase("Contact Person Number")) {
				ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colIndex,  InfraConstants.CM + ForesightConstants.FORWARD_SLASH + InfraConstants.SITEFORGE);
				}
			
			
			if(cellvalue.equalsIgnoreCase("Tower Site Type") || cellvalue.equalsIgnoreCase("Province Code") || cellvalue.equalsIgnoreCase("LTE City") || cellvalue.equalsIgnoreCase("Site Rollout Scenario") || cellvalue.equalsIgnoreCase("TX Site Class") ||
					cellvalue.equalsIgnoreCase("TX Topology") || cellvalue.equalsIgnoreCase("Island") || cellvalue.equalsIgnoreCase("Province") || cellvalue.equalsIgnoreCase("Tx Vendor") || 
					cellvalue.equalsIgnoreCase("Capacity_W37") || cellvalue.equalsIgnoreCase("BTS_IP") || cellvalue.equalsIgnoreCase("Phase") ||  cellvalue.equalsIgnoreCase("Project Milestone")) {
				ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colIndex,   InfraConstants.SITEFORGE);
				}
			
			
			
			if(cellvalue.equalsIgnoreCase("EMS Live Status") || cellvalue.equalsIgnoreCase("EMS Live Date") || cellvalue.equalsIgnoreCase("Non-Radiating Status") || cellvalue.equalsIgnoreCase("Non-Radiating Date") ||
					cellvalue.equalsIgnoreCase("PM") || cellvalue.equalsIgnoreCase("Non-Radiating Status") || cellvalue.equalsIgnoreCase("Decommissioned Status") || 
					cellvalue.equalsIgnoreCase("Decommissioned Date")) {
			ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colIndex, InfraConstants.PM);
			}
			if(cellvalue.equalsIgnoreCase("FM")) {
			ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colIndex, InfraConstants.FM);
			}
			if(cellvalue.equalsIgnoreCase("Site Lifecycle Status")  ||
					cellvalue.equalsIgnoreCase("Tentative Onair Date")) {
			ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colIndex, InfraConstants.SITEFORGE);
			} 
			if(cellvalue.equalsIgnoreCase("No of Sectors") || cellvalue.equalsIgnoreCase("Morphology") || cellvalue.equalsIgnoreCase(geographyMap.get("GeographyL1")) || cellvalue.equalsIgnoreCase(geographyMap.get("GeographyL2")) ||
					cellvalue.equalsIgnoreCase(geographyMap.get("GeographyL3")) || cellvalue.equalsIgnoreCase(geographyMap.get("GeographyL4"))) {
			ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colIndex, InfraConstants.FORESIGHT);
			} 
			}
			setColumnWidthForReport(sheet);
		} catch (Exception e) {
			logger.error("Exception in sheet creation for site detail {}", Utils.getStackTrace(e));
		}
	
	}
  private void generateSheetForSiteDetailReport(XSSFWorkbook workbook, Map<String, XSSFCellStyle> style,Map<String,String> geographyMap,List<String> firstHeaderList,List<String> secondHeaderList,Double southWestLong, Double southWestLat, Double northEastLong,Double northEastLat,Map<String, List<String>> geographyNames,List<NEType> neTypeList,List<NEStatus> neStatusList,List<String> neFrequencyList)
				   throws Exception {
	   logger.info("going to generate site detail sheet.");
	   try {
		   XSSFSheet sheet = workbook.createSheet(InfraConstants.SITE_DETAIL);
		   int rowNum = 0;
		   XSSFRow row=null;
		   Integer counter = 1;
		   String[] firstHeader = firstHeaderList.toArray(new String[0]);
		   String[] secondHeader = secondHeaderList.toArray(new String[0]);
		   try { 
			   row=sheet.createRow(rowNum);
		   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.ORANGEHEADER), false, sheet, firstHeader);
		   row.setHeightInPoints(20);
		   rowNum++;
		   row=sheet.createRow(rowNum);
		   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.YELLOWHEADER), false, sheet, secondHeader);
		   row.setHeightInPoints(40);
		   rowNum++;
		   mergeColumnsForReport(sheet, firstHeader, 0);
		   List<Object[]>  siteDetailDataList=iNetworkElementDao.getSiteDetailReportData(neFrequencyList,geographyNames, neStatusList, neTypeList,southWestLong, southWestLat, northEastLong,northEastLat);
		   logger.info("siteDetailDataList size: "+siteDetailDataList.size());
		   if (siteDetailDataList != null && siteDetailDataList.size() > 0) {
				for(Object[] rowData : siteDetailDataList){
					row=sheet.createRow(rowNum);
					 ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet,
					 rowData[0] != null ? rowData[0].toString() : ForesightConstants.HIPHEN,
					 rowData[1] != null ? rowData[1].toString() : ForesightConstants.HIPHEN,
					 rowData[2] != null ? rowData[2].toString() : ForesightConstants.HIPHEN,
				     rowData[3] != null ? rowData[3].toString() : ForesightConstants.HIPHEN,
					 rowData[4] != null ? rowData[4].toString() : ForesightConstants.HIPHEN,
					 rowData[5] != null ? rowData[5].toString() : ForesightConstants.HIPHEN,
					 rowData[6] != null ? rowData[6].toString() : ForesightConstants.HIPHEN,
					 rowData[7] != null ? rowData[7].toString() : ForesightConstants.HIPHEN,
					 rowData[8] != null ? rowData[8].toString() : ForesightConstants.HIPHEN,
					 rowData[9] != null ? rowData[9].toString() : ForesightConstants.HIPHEN,
					 rowData[10] != null ? rowData[10].toString() : ForesightConstants.HIPHEN);
							 writeDataInSiteDetailReport(rowData, rowNum, sheet, style);																	
					 	rowNum++;
					   counter++;
				}
		   }
		   
		   
		   setColumnWidthForReport(sheet);
		  
	   } catch (Exception exception) {
		   logger.error("Unable to create Overviewsheet", ExceptionUtils.getStackTrace(exception));
	   }
	   }catch (Exception exception) {
		   logger.error("Unable to create Overviewsheet", Utils.getStackTrace(exception));
	}
  }
  private void getSiteDetailReportForPolygon(XSSFWorkbook workbook, Map<String, XSSFCellStyle> style,Map<String,String> geographyMap,List<String> firstHeaderList,List<String> secondHeaderList,List<Object[]>  siteDetailDataList)
		   throws Exception {
logger.info("going to generate site detail sheet.");
try {
  XSSFSheet sheet = workbook.createSheet("SiteDetail");
  int rowNum = 0;
  XSSFRow row=null;
  Integer counter = 1;
  String[] firstHeader = firstHeaderList.toArray(new String[0]);
  String[] secondHeader = secondHeaderList.toArray(new String[0]);
  try { 
	   row=sheet.createRow(rowNum);
  ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.ORANGEHEADER), false, sheet, firstHeader);
  row.setHeightInPoints(20);
  rowNum++;
  row=sheet.createRow(rowNum);
  ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.YELLOWHEADER), false, sheet, secondHeader);
  row.setHeightInPoints(40);
  rowNum++;
  mergeColumnsForReport(sheet, firstHeader, 0);
  if (siteDetailDataList != null && siteDetailDataList.size() > 0) {
		for(Object[] rowData : siteDetailDataList){
			row=sheet.createRow(rowNum);
			 ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet,
			 rowData[0] != null ? rowData[0].toString() : ForesightConstants.HIPHEN,
			 rowData[1] != null ? rowData[1].toString() : ForesightConstants.HIPHEN,
			 rowData[2] != null ? rowData[2].toString() : ForesightConstants.HIPHEN,
		     rowData[3] != null ? rowData[3].toString() : ForesightConstants.HIPHEN,
			 rowData[4] != null ? rowData[4].toString() : ForesightConstants.HIPHEN,
			 rowData[5] != null ? rowData[5].toString() : ForesightConstants.HIPHEN,
			 rowData[6] != null ? rowData[6].toString() : ForesightConstants.HIPHEN,
			 rowData[7] != null ? rowData[7].toString() : ForesightConstants.HIPHEN,
			 rowData[8] != null ? rowData[8].toString() : ForesightConstants.HIPHEN,
			 rowData[9] != null ? rowData[9].toString() : ForesightConstants.HIPHEN,
			 rowData[10] != null ? rowData[10].toString() : ForesightConstants.HIPHEN);
					 writeDataInSiteDetailReport(rowData, rowNum, sheet, style);																	
			 	rowNum++;
			   counter++;
		}
  }
  
  
  setColumnWidthForReport(sheet);
 
} catch (Exception exception) {
  logger.error("Unable to create Overviewsheet", ExceptionUtils.getStackTrace(exception));
}
}catch (Exception exception) {
  logger.error("Unable to create Overviewsheet", Utils.getStackTrace(exception));
}
}
  private void writeDataInSiteDetailReport(Object[] rowData,int rowNum,XSSFSheet sheet,Map<String, XSSFCellStyle> style) {
	  
	  XSSFRow row=sheet.getRow(rowNum);
	  for(int colIndex =11;colIndex<rowData.length;colIndex++) {
		  try {
	  ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colIndex, rowData[colIndex] != null ? rowData[colIndex].toString() : ForesightConstants.HIPHEN);
		  }catch(Exception exception) {
			  exception.printStackTrace();
		  }
	  }
	  
  }
    private Map<String, List<String>> getGeographyNameFromWrapper(Map<String,List<Map>> filters) {
  	  Map<String, List<String>> geographyNames = new HashMap<>();
  	List<Map> listOfMap=new ArrayList<>();
  	List<String> geographyList=new ArrayList<>();
  	if(filters.get(InfraConstants.GEOGRAPHYL1_TABLE) != null) {
  		listOfMap=filters.get(InfraConstants.GEOGRAPHYL1_TABLE);
  		for (Map map : listOfMap) {
  			if(map.get(InfraConstants.NE_VALUE_KEY) != null) {
  				geographyList=(List<String>) map.get(InfraConstants.NE_VALUE_KEY);
  			}
		}
  		 geographyNames.put(InfraConstants.GEOGRAPHYL1_TABLE,geographyList);
  	  }
  	  if(filters.get(InfraConstants.GEOGRAPHYL2_TABLE) != null) {
  		listOfMap=filters.get(InfraConstants.GEOGRAPHYL2_TABLE);
  		for (Map map : listOfMap) {
  			if(map.get(InfraConstants.NE_VALUE_KEY) != null) {
  				geographyList=(List<String>) map.get(InfraConstants.NE_VALUE_KEY);
  			}
		}
  		 geographyNames.put(InfraConstants.GEOGRAPHYL2_TABLE,geographyList);
  	  }
  	if(filters.get(InfraConstants.GEOGRAPHYL3_TABLE) != null) {
  		listOfMap=filters.get(InfraConstants.GEOGRAPHYL3_TABLE);
  		for (Map map : listOfMap) {
  			if(map.get(InfraConstants.NE_VALUE_KEY) != null) {
  				geographyList=(List<String>) map.get(InfraConstants.NE_VALUE_KEY);
  			}
		}
  		 geographyNames.put(InfraConstants.GEOGRAPHYL3_TABLE,geographyList);
  	  }
  	if(filters.get(InfraConstants.GEOGRAPHYL4_TABLE) != null) {
  		listOfMap=filters.get(InfraConstants.GEOGRAPHYL4_TABLE);
  		for (Map map : listOfMap) {
  			if(map.get(InfraConstants.NE_VALUE_KEY) != null) {
  				geographyList=(List<String>) map.get(InfraConstants.NE_VALUE_KEY);
  			}
		}
  		 geographyNames.put(InfraConstants.GEOGRAPHYL4_TABLE,geographyList);
  	  }
  	   return geographyNames;
    }
    private List<NEType> getneTypeFromMap(Map<String,List<Map>> filters) {
    	logger.info("going to get netype from map.");
    	List<String> statusList=new ArrayList<>();
    	List<NEType> neTypeList=new ArrayList<>();
    	if(filters.get(InfraConstants.NETWORKELEMENT_TABLE) != null) {
			 List<Map> finalMap=new ArrayList<>();
			 finalMap=filters.get(InfraConstants.NETWORKELEMENT_TABLE);
			 for (Map map : finalMap) {
				 if(map.get(InfraConstants.NE_LABELTYPE_KEY) != null) {
				 String labelType=map.get(InfraConstants.NE_LABELTYPE_KEY).toString();
				 if(labelType.equalsIgnoreCase(InfraConstants.NE_TYPE)) {
					 if(map.get(InfraConstants.NE_OPERATION_KEY).toString().equalsIgnoreCase(InfraConstants.IN_OPERATOR)) {
					 List<String> statusList1 = (List<String>)map.get(InfraConstants.NE_VALUE_KEY);
					 for (String status : statusList1) {
						 if(status.equalsIgnoreCase(InfraConstants.MACRO_CELL))
							 statusList.add(InfraConstants.MACRO);
						 else if(status.equalsIgnoreCase(InfraConstants.PICO_CELL))
								statusList.add(InfraConstants.PICO_SITE);
						 else if(status.equalsIgnoreCase(InfraConstants.IBS_CELL))
								statusList.add(InfraConstants.IBS_SITE);
						 else if(status.equalsIgnoreCase(InfraConstants.SHOOTER_CELL))
								statusList.add(InfraConstants.SHOOTER_SITE);
						 else if(status.equalsIgnoreCase(InfraConstants.ODSC_CELL))
								statusList.add(InfraConstants.ODSC_SITE);
						 else if(status.equalsIgnoreCase(InfraConstants.IDSC_CELL))
								statusList.add(InfraConstants.IDSC_SITE);
						 else if(status.equalsIgnoreCase(InfraConstants.GALLERY_CELL))
								statusList.add(InfraConstants.GALLERY_SITE);
						 else
						 statusList.add(status);
					}
					 }else if(map.get(InfraConstants.NE_OPERATION_KEY).toString().equalsIgnoreCase(InfraConstants.EQUALS_OPERATOR)) {
						 if(map.get(InfraConstants.NE_VALUE_KEY).toString().equalsIgnoreCase(InfraConstants.MACRO_CELL))
							 statusList.add(InfraConstants.MACRO);
						 else if(map.get(InfraConstants.NE_VALUE_KEY).toString().equalsIgnoreCase(InfraConstants.PICO_CELL))
								statusList.add(InfraConstants.PICO_SITE);
						 else if(map.get(InfraConstants.NE_VALUE_KEY).toString().equalsIgnoreCase(InfraConstants.IBS_CELL))
								statusList.add(InfraConstants.IBS_SITE);
						 else if(map.get(InfraConstants.NE_VALUE_KEY).toString().equalsIgnoreCase(InfraConstants.SHOOTER_CELL))
								statusList.add(InfraConstants.SHOOTER_SITE);
						 else if(map.get(InfraConstants.NE_VALUE_KEY).toString().equalsIgnoreCase(InfraConstants.ODSC_CELL))
								statusList.add(InfraConstants.ODSC_SITE);
						 else if(map.get(InfraConstants.NE_VALUE_KEY).toString().equalsIgnoreCase(InfraConstants.IDSC_CELL))
								statusList.add(InfraConstants.IDSC_SITE);
						 else if(map.get(InfraConstants.NE_VALUE_KEY).toString().equalsIgnoreCase(InfraConstants.GALLERY_CELL))
								statusList.add(InfraConstants.GALLERY_SITE);
						 else
						 statusList.add(map.get(InfraConstants.NE_VALUE_KEY).toString());
					 }else
						 logger.info("Invalid operations for NEType.");
				 }
				 }
			}
		
		}
    	if(statusList != null && !statusList.isEmpty()) {
    		for (String status : statusList) {
    			neTypeList.add(NEType.valueOf(status));
			}
    	}
  		return neTypeList;
    }
    private List<NEType> getneTypeForSectorDetailReport(Map<String,List<Map>> filters) {
    	List<String> statusList=new ArrayList<>();
    	List<NEType> neTypeList=new ArrayList<>();
    	if(filters.get(InfraConstants.NETWORKELEMENT_TABLE) != null) {
			 List<Map> finalMap=new ArrayList<>();
			 finalMap=filters.get(InfraConstants.NETWORKELEMENT_TABLE);
			 for (Map map : finalMap) {
				 if(map.get(InfraConstants.NE_LABELTYPE_KEY) != null) {
				 String labelType=map.get(InfraConstants.NE_LABELTYPE_KEY).toString();
				 if(labelType.equalsIgnoreCase(InfraConstants.NE_TYPE)) {
					 if(map.get(InfraConstants.NE_OPERATION_KEY).toString().equalsIgnoreCase(InfraConstants.IN_OPERATOR))
					 statusList = (List<String>)map.get(InfraConstants.NE_VALUE_KEY);
					 else if(map.get(InfraConstants.NE_OPERATION_KEY).toString().equalsIgnoreCase(InfraConstants.EQUALS_OPERATOR))
						 statusList.add(map.get(InfraConstants.NE_VALUE_KEY).toString());
					 else
						 logger.info("Invalid operations for NEType.");
				 }
				 }
			}
		
		}
    	if(statusList != null && !statusList.isEmpty()) {
    		for (String status : statusList) {
    			neTypeList.add(NEType.valueOf(status));
			}
    	}
  		return neTypeList;
    }
    private List<NEStatus> getneStatusFromMap(Map<String,List<Map>> filters) {
  	  List<NEStatus> neStatusList = new ArrayList<>();
  		List<String> neStatusTemp = new ArrayList<>();
  		if(filters.get(InfraConstants.NEBANDDETAIL_TABLE) != null) {
			 List<Map> finalMap=new ArrayList<>();
			 finalMap=filters.get(InfraConstants.NEBANDDETAIL_TABLE);
			 for (Map map : finalMap) {
				 if(map.get(InfraConstants.NE_LABELTYPE_KEY) != null) {
				 String labelType=map.get(InfraConstants.NE_LABELTYPE_KEY).toString();
				 if(labelType.equalsIgnoreCase(InfraConstants.NE_BANDSTATUS_KEY)) {
					 
					 if(map.get(InfraConstants.NE_OPERATION_KEY).toString().equalsIgnoreCase(InfraConstants.IN_OPERATOR))
						 neStatusTemp = (List<String>)map.get(InfraConstants.NE_VALUE_KEY);
						 else if(map.get(InfraConstants.NE_OPERATION_KEY).toString().equalsIgnoreCase(InfraConstants.EQUALS_OPERATOR))
							 neStatusTemp.add(map.get(InfraConstants.NE_VALUE_KEY).toString());
						 else
							 logger.info("Invalid operations for NEStatus.");
					 
				 }
				 }
			}
		
		}
  		if(filters.get(InfraConstants.NETWORKELEMENT_TABLE) != null) {
			 List<Map> finalMap=new ArrayList<>();
			 finalMap=filters.get(InfraConstants.NETWORKELEMENT_TABLE);
			 for (Map map : finalMap) {
				 if(map.get(InfraConstants.NE_LABELTYPE_KEY) != null) {
				 String labelType=map.get(InfraConstants.NE_LABELTYPE_KEY).toString();
				 if(labelType.equalsIgnoreCase(InfraConstants.NE_STATUS)) {
					 
					 if(map.get(InfraConstants.NE_OPERATION_KEY).toString().equalsIgnoreCase(InfraConstants.IN_OPERATOR))
						 neStatusTemp = (List<String>)map.get(InfraConstants.NE_VALUE_KEY);
						 else if(map.get(InfraConstants.NE_OPERATION_KEY).toString().equalsIgnoreCase(InfraConstants.EQUALS_OPERATOR))
							 neStatusTemp.add(map.get(InfraConstants.NE_VALUE_KEY).toString());
						 else
							 logger.info("Invalid operations for NEStatus.");
					 
				 }
				 }
			}
		
		}
  		if(neStatusTemp != null && !neStatusTemp.isEmpty()) {
  			for (String neStatus : neStatusTemp) {
  				neStatusList.add(NEStatus.valueOf(neStatus));
			}
  		}
  			
  	return neStatusList;
    }
    
  private List<String> getneFrequencyFromMap( Map<String,List<Map>> filters) {
  	  List<String> neFrequencyList = new ArrayList<>();
  	if(filters.get(InfraConstants.NEBANDDETAIL_TABLE) != null) {
		 List<Map> finalMap=new ArrayList<>();
		 finalMap=filters.get(InfraConstants.NEBANDDETAIL_TABLE);
		 for (Map map : finalMap) {
			 if(map.get(InfraConstants.NE_LABELTYPE_KEY) != null) {
			 String labelType=map.get(InfraConstants.NE_LABELTYPE_KEY).toString();
			 if(labelType.equalsIgnoreCase(InfraConstants.NE_FREQUENCY)) {
				 if(map.get(InfraConstants.NE_OPERATION_KEY).toString().equalsIgnoreCase(InfraConstants.IN_OPERATOR))
					 neFrequencyList =(List<String>)map.get(InfraConstants.NE_VALUE_KEY);
					 else if(map.get(InfraConstants.NE_OPERATION_KEY).toString().equalsIgnoreCase(InfraConstants.EQUALS_OPERATOR))
						 neFrequencyList.add(map.get(InfraConstants.NE_VALUE_KEY).toString());
					 else
						 logger.info("Invalid operations for neFrequency.");
				 
			 }
			 }
		}
	
	}
 	if(filters.get(InfraConstants.NETWORKELEMENT_TABLE) != null) {
		 List<Map> finalMap=new ArrayList<>();
		 finalMap=filters.get(InfraConstants.NETWORKELEMENT_TABLE);
		 for (Map map : finalMap) {
			 if(map.get(InfraConstants.NE_LABELTYPE_KEY) != null) {
			 String labelType=map.get(InfraConstants.NE_LABELTYPE_KEY).toString();
			 if(labelType.equalsIgnoreCase(InfraConstants.NE_FREQUENCY)) {
				 if(map.get(InfraConstants.NE_OPERATION_KEY).toString().equalsIgnoreCase(InfraConstants.IN_OPERATOR))
					 neFrequencyList =(List<String>)map.get(InfraConstants.NE_VALUE_KEY);
					 else if(map.get(InfraConstants.NE_OPERATION_KEY).toString().equalsIgnoreCase(InfraConstants.EQUALS_OPERATOR))
						 neFrequencyList.add(map.get(InfraConstants.NE_VALUE_KEY).toString());
					 else
						 logger.info("Invalid operations for neFrequency.");
				 
			 }
			 }
		}
	
	}
  	return neFrequencyList;
  }
  
  @Override
  public Map<String, String> generateSectorDetailReport(Double southWestLong,Double southWestLat, Double northEastLong,Double northEastLat,SiteLayerSelection filterConfiguration) {
		logger.info("going to generate SectorDetail sheet.");
		 XSSFWorkbook workbook = new XSSFWorkbook();
		   Map<String, String> fileMap = new HashMap<>();
		   Map<String, List<String>> geographyNames = new HashMap<>();
		   Map<String,List<Map>> filters=new HashMap<>();
		   List<NEType> neTypeList = new ArrayList<>();
		   List<NEStatus> neStatusList = new ArrayList<>();
		   List<String> neFrequencyList = new ArrayList<>();
		   Map<String, XSSFCellStyle> style = createStylesForReport(workbook);
		   String filePath = null;
		   String fileName=createFileNameForSectorDetailReport();
		   try {

           Map<String, String> map=getHeaderForSiteAndSectorReport();
		   Map<String,String> geographyMap=getMappingForGeographies();
		   if(filterConfiguration.getFilters() != null) {
		   filters = filterConfiguration.getFilters();
		   geographyNames=getGeographyNameFromWrapper(filters);
		   neTypeList=getneTypeForSectorDetailReport(filters);
		   neStatusList=getneStatusFromMap(filters);
		   neFrequencyList=getneFrequencyFromMap(filters);
		   }
		   Map<String, List<String>> headerMap=getFirstAndSecondHeaderForSectorReport(map, neFrequencyList, geographyMap);
		   List<String> firstHeaderList = headerMap.get(InfraConstants.FIRST_HEADER);
		   List<String> secondHeaderList =  headerMap.get(InfraConstants.SECOND_HEADER);
		   String[] firstHeader= firstHeaderList.toArray(new String[0]);
		   String[] secondHeader = secondHeaderList.toArray(new String[0]);
		   if(filterConfiguration.getPolygon() != null) {
				  List<List<List<Double>>> polygons=filterConfiguration.getPolygon();
				  
				  for (String neFrequency : neFrequencyList) {
					  List<Object[]> sectorDetailDataList =getDataForSectorDetailReportForPolygon(polygons, neFrequency, neTypeList, neStatusList);
					logger.info("final data for sector detail report : {}",sectorDetailDataList.size());
					  generateSheetForSectorDetailReport(workbook, style, geographyMap, southWestLong, southWestLat, northEastLong, northEastLat,  neFrequency,neStatusList,neTypeList,firstHeader,secondHeader,sectorDetailDataList);
					}
				    List<String> SecondHeaderList=Arrays.asList(secondHeader);
					createDataSourceSheetForSectorDetail(workbook, style, geographyMap,SecondHeaderList);
					
			  }
			
			filePath = InfraUtils.exportExcelFile(workbook,
					ConfigUtils.getString(ConfigEnum.SITE_REPORT_FILE_PATH.getValue()), fileName, true);
			logger.info("The uri is as follows -> " + filePath);
			fileMap.put(InfraConstants.KEY_FILENAME, filePath);

		} catch (Exception exception) {
			logger.error("Unable to create Overviewsheet", Utils.getStackTrace(exception));
		}
		return fileMap;
	}
  private List<Object[]> getDataForSectorDetailReportForPolygon(List<List<List<Double>>> polygons, String frequency, List<NEType> neTypeList,List<NEStatus> neStatusList) {
		logger.info("inside getDataForSectorDetailReportForPolygon.");
		Map<String, Double> viewportMap = new HashMap<>();
		List<Object[]>  siteDetailData=new ArrayList<>();
		try {
		logger.info("No. of polygons are {}", polygons.size());
		for (List<List<Double>> polygon : polygons) {
		GISGeometry gispolygon = new GIS2DPolygon(polygon);
		Corner bounds = BoundaryUtils.getCornerOfBoundary(polygon);
		Double minlat = bounds.getMinLatitude();
		Double maxlat = bounds.getMaxLatitude();
		Double minlon = bounds.getMinLongitude();
		Double maxlon = bounds.getMaxLongitude();
		viewportMap.put(InfraConstants.SOUTHWEST_LATITUDE_KEY, minlat);
		viewportMap.put(InfraConstants.NORTHEAST_LATITUDE_KEY, maxlat);
		viewportMap.put(InfraConstants.SOUTHWEST_LONGITUDE_KEY, minlon);
		viewportMap.put(InfraConstants.NORTHEAST_LONGITUDE_KEY, maxlon);
		logger.info("viewport : {}",viewportMap);
		logger.info("frequency : {}",frequency);
		logger.info("neStatusList : {}",neStatusList);
		logger.info("neTypeList : {}",neTypeList);
		List<Object[]> sectorDetailDataList = iMacroSiteDetailDao.getSectorDetailReportData(frequency, null, neStatusList, neTypeList,viewportMap.get(InfraConstants.SOUTHWEST_LONGITUDE_KEY), viewportMap.get(InfraConstants.SOUTHWEST_LATITUDE_KEY),viewportMap.get(InfraConstants.NORTHEAST_LONGITUDE_KEY),viewportMap.get(InfraConstants.NORTHEAST_LATITUDE_KEY));
		
        logger.info("sectorDetailDataList : {}",sectorDetailDataList.size());
		if(sectorDetailDataList != null && !sectorDetailDataList.isEmpty()) {
		for (Object[] neBandDetail : sectorDetailDataList) {
		try {
			if(neBandDetail != null) {
				if(neBandDetail[0] != null && neBandDetail[1] != null)	{
		if (gispolygon.contains(new GISPoint(new LatLng(Double.valueOf(neBandDetail[0].toString()), Double.valueOf(neBandDetail[1].toString()))))) {
			siteDetailData.add(neBandDetail);
			}
		}
		}
		} catch (Exception exception) {
		logger.error("Unable to get count of kpi for object {} due to exception {}", neBandDetail, Utils.getStackTrace(exception));
		}
		
		}
		}
		logger.info("siteDetailData size for polygon : {}",siteDetailData.size());
		}

		} catch (Exception exception) {
		logger.error("Unable to get kpimapcount of polygons due to exception {}", Utils.getStackTrace(exception));
		}
		return siteDetailData;
	}
	
	@SuppressWarnings("unused")
	private void generateSheetForSectorDetailReport(XSSFWorkbook workbook, Map<String, XSSFCellStyle> style,
			Map<String, String> geographyMap,
			Double southWestLong, Double southWestLat, Double northEastLong, Double northEastLat,
			 String neFrequency,List<NEStatus> neStatusList,List<NEType> neTypeList,String[] firstHeader ,String[] secondHeader,List<Object[]> sectorDetailDataList ) throws Exception {
		logger.info("going to generate sector detail sheet.");
		try {
			XSSFSheet sheet = workbook.createSheet(neFrequency+ForesightConstants.UNDERSCORE + InfraConstants.BAND_CAMEL );
			int rowNum = 0;
			Map<String, List<String>> geographyNames = new HashMap<>();
			Map<String, Double> viewportMap = new HashMap<>();
			Map kpiMap = new HashMap<>();
			Integer counter = 1;
			try {
				setHeightOfRow(rowNum, sheet, 40);
				ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.ORANGEHEADER), false, sheet,
						firstHeader);
				rowNum++;
				setHeightOfRow(rowNum, sheet, 60);
				ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.YELLOWHEADER), false, sheet,
						secondHeader);
				rowNum++;
				mergeColumnsForReport(sheet, firstHeader, 0);
				if (sectorDetailDataList != null && sectorDetailDataList.size() > 0) {
					for (Object[] rowData : sectorDetailDataList) {
						try {
						XSSFRow row = sheet.createRow(rowNum);
						ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY),
								false, sheet);
						writeDataInSectorDetailReport(rowData, rowNum, sheet, style);

						rowNum++;
						counter++;
						}catch(Exception exception) {
							exception.printStackTrace();
						}
					}

				}
				setColumnWidthForReport(sheet);

			} catch (Exception exception) {
				logger.error("Unable to create Overviewsheet", ExceptionUtils.getStackTrace(exception));
			}
		} catch (Exception exception) {
			logger.error("Unable to create Overviewsheet", Utils.getStackTrace(exception));
		}
	}
	private void writeDataInSectorDetailReport(Object[] rowData, int rowNum, XSSFSheet sheet,
			Map<String, XSSFCellStyle> style) {
     try {
		XSSFRow row = sheet.getRow(rowNum);
		int colNumber=0;
		for (int colIndex = 2; colIndex < rowData.length; colIndex++) {
			try {
				ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colNumber,
						rowData[colIndex] != null ? rowData[colIndex].toString() : ForesightConstants.HIPHEN);
				colNumber ++;
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}catch (Exception exception) {
		exception.printStackTrace();
	}
}
	private void createDataSourceSheetForSectorDetail(XSSFWorkbook workbook, Map<String, XSSFCellStyle> style,
			Map<String, String> geographyMap, List<String> secondHeaderList) {
		logger.info("Going to create data source sheet for site details");
		XSSFSheet sheet = (XSSFSheet) workbook.createSheet(InfraConstants.DATA_SOURCES);
		int rowNum = 0;
		String[] firstRowData = { "Field Name", "Data Source" };

		try {
			XSSFRow firstRow = (XSSFRow) sheet.createRow(rowNum);
			ReportUtils.createRow(firstRow, style.get(ForesightConstants.BLUEHEADER), false, sheet, firstRowData);
			rowNum++;
			XSSFRow row = null;
			if(secondHeaderList != null && !secondHeaderList.isEmpty()) {
			for (String secondHeader : secondHeaderList) {
				row = (XSSFRow) sheet.createRow(rowNum);

				if (rowNum != 29 || rowNum != 30 || rowNum != 31 || rowNum != 32 || rowNum != 33 || rowNum != 34
						|| rowNum != 35 || rowNum != 36 || rowNum != 37) {
					ReportUtils.createRow(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, secondHeader);
					rowNum++;
				}
			}
		}
			int colIndex = 1;
			for (int i = 1; i < rowNum; i++) {
				row = getRow(i, sheet);
				Cell c = row.getCell(0);

				String cellvalue = c.getStringCellValue();
				if (cellvalue.equalsIgnoreCase("SITE ID") || cellvalue.equalsIgnoreCase("CM")) {
					ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet,
							colIndex, InfraConstants.CM);
				}
				if (cellvalue.equalsIgnoreCase("SITE TYPE") || cellvalue.equalsIgnoreCase("SITE VENDOR")
						|| cellvalue.equalsIgnoreCase("Cell Location") || cellvalue.equalsIgnoreCase("Cell Status")
						|| cellvalue.equalsIgnoreCase("eNodeB ID") || cellvalue.equalsIgnoreCase("MCC")
						|| cellvalue.equalsIgnoreCase("MNC") || cellvalue.equalsIgnoreCase("Tracking Area")
						|| cellvalue.equalsIgnoreCase("Cell On Air Date") || cellvalue.equalsIgnoreCase("Sector Id")
						|| cellvalue.equalsIgnoreCase("Cell Id") || cellvalue.equalsIgnoreCase("Bandwidth")
						|| cellvalue.equalsIgnoreCase("ECGI") || cellvalue.equalsIgnoreCase("PCI")
						|| cellvalue.equalsIgnoreCase("Azimuth") || cellvalue.equalsIgnoreCase("Tx Power")
						|| cellvalue.equalsIgnoreCase("DL EARFCN") || cellvalue.equalsIgnoreCase("UL EARFCN")
						|| cellvalue.equalsIgnoreCase("Antenna Type") || cellvalue.equalsIgnoreCase("Antenna Height")
						|| cellvalue.equalsIgnoreCase("Antenna Gain") || cellvalue.equalsIgnoreCase("Electrical Tilt")
						|| cellvalue.equalsIgnoreCase("Mechanical tilt")
						|| cellvalue.equalsIgnoreCase("Horizontal Beamwidth")
						|| cellvalue.equalsIgnoreCase("Vertical Beamwidth")
						|| cellvalue.equalsIgnoreCase("Operational STATUS")
						|| cellvalue.equalsIgnoreCase("Administrative State")
						|| cellvalue.equalsIgnoreCase("De-Commissioned Site")
						|| cellvalue.equalsIgnoreCase("De-Commissioned Site Date")
						|| cellvalue.equalsIgnoreCase("Source") || cellvalue.equalsIgnoreCase("Source Date")
						|| cellvalue.equalsIgnoreCase("Cell Onair Date")
						|| cellvalue.equalsIgnoreCase("Antenna Vendor")) {

					ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet,
							colIndex, InfraConstants.CM + ForesightConstants.FORWARD_SLASH + InfraConstants.SITEFORGE);
				}
				if (cellvalue.equalsIgnoreCase("EMS_LIVE") || cellvalue.equalsIgnoreCase("EMS Live Date")
						|| cellvalue.equalsIgnoreCase("EMS_LIVE Counter")  
						|| cellvalue.equalsIgnoreCase("EMS Live Status") 
						|| cellvalue.equalsIgnoreCase("EMS Live Counter")
						|| cellvalue.equalsIgnoreCase("Non-Radiating Status")
						|| cellvalue.equalsIgnoreCase("Non-Radiating Date")
						|| cellvalue.equalsIgnoreCase("Non-Radiating Counter") || cellvalue.equalsIgnoreCase("PM")
						|| cellvalue.equalsIgnoreCase("FM") || cellvalue.equalsIgnoreCase("EMS_LIVE Date")) {
					ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet,
							colIndex, InfraConstants.PM);
				}
				if (cellvalue.equalsIgnoreCase("RRU Configuration")	|| cellvalue.equalsIgnoreCase("Antenna Port")) {
					ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet,
							colIndex,InfraConstants.SITEFORGE);
				}
				if (cellvalue.equalsIgnoreCase("Morphology")
						|| cellvalue.equalsIgnoreCase(geographyMap.get("GeographyL1"))
						|| cellvalue.equalsIgnoreCase(geographyMap.get("GeographyL2"))
						|| cellvalue.equalsIgnoreCase(geographyMap.get("GeographyL3"))
						|| cellvalue.equalsIgnoreCase(geographyMap.get("GeographyL4"))) {
					ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet,
							colIndex, InfraConstants.FORESIGHT);
				}

			}

			setColumnWidthForReport(sheet);
		} catch (Exception e) {
			logger.error("Exception in sheet creation for site detail {}", Utils.getStackTrace(e));
		}
	}
	private void generateEnodeBDetailSheet(XSSFWorkbook workbook,Map<String, XSSFCellStyle> style,String neName,String neType)
					   throws Exception {
		   logger.info("going to generate EnodeB detail sheet.");
		   try {
			   int rowNum = 0;
			   XSSFRow row =null;
			   Integer colNum=4;
			   XSSFSheet sheet = workbook.createSheet(InfraConstants.ENODEB_DETAILS);
			 createFirstAndSecondHeaderForEnodeB(style, rowNum, colNum, sheet);
			 SiteConnectionPointWrapper siteConnectionPointWrapper= ineVisualizationService.getSiteConnectionPointDetails(neName, neType);
			   if(siteConnectionPointWrapper != null) {
				   Map vcuMap=siteConnectionPointWrapper.getVcuMap();
				   if(vcuMap != null) {
					   rowNum=2;
					   List<Map> vcuMapDataList=(List<Map>) vcuMap.get(InfraConstants.VCU);
					   if(checkListOfMap(vcuMapDataList)) {
					   fillVCUDetailsOnSheet(style, rowNum, sheet, vcuMapDataList);
				   }
					  
				  }
				   Map vduMap=siteConnectionPointWrapper.getVduMap();
					if(vduMap != null) {
						 List<Map> vduMapDataList=(List<Map>) vduMap.get(InfraConstants.VDU);
						 rowNum=2;
						 colNum=4;
						 if(checkListOfMap(vduMapDataList))
						 {
							  if(checkListOfMap(vduMapDataList)) {
							 for (Map map : vduMapDataList) {
								 row = getRow(rowNum, sheet);
								 colNum=4;
								 if(row != null) {
									 colNum=fillVDUDetail(style, sheet, row, colNum, map);
									 rowNum++;
								 }else {
									 sheet.createRow(rowNum);
									 row = getRow(rowNum, sheet);
									 colNum=fillVDUDetail(style, sheet, row, colNum, map);
								   rowNum ++;
							}
						 }
						 }
					}   
						
				   }
			   }
			   mergeColumnsForEnodebDetailSheet(sheet);
			   setWidthForEnodeBDetailSheet(sheet);
		   } catch (Exception exception) {
			   logger.error("Unable to create EnodeB Detail Sheet.Exception : {}", ExceptionUtils.getStackTrace(exception));
		   }
	   }


	private void createFirstAndSecondHeaderForEnodeB(Map<String, XSSFCellStyle> style, int rowNum, Integer colNum,
			XSSFSheet sheet) {
		XSSFRow row;
		String[] vcuFirstHeader = { InfraConstants.VCU_INFORMATION ,ForesightConstants.BLANK_STRING,ForesightConstants.BLANK_STRING};
			 setHeightOfRow(rowNum, sheet,20);
			 ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.YELLOWHEADER), false, sheet, vcuFirstHeader);
			 rowNum++;
			 ReportUtils.createRow(sheet.createRow(rowNum), style.get(InfraConstants.GREEN_HEADER), false, sheet, enbSheetSecondHeader);
			 rowNum++;
			 rowNum=0;
			 row = getRow(rowNum, sheet);
			 if(row != null) {
			colNum=writeValueOnCell(style.get(ForesightConstants.YELLOWHEADER), sheet, row, colNum,InfraConstants.VDU_INFORMATION);
			colNum=writeValueOnCell(style.get(ForesightConstants.YELLOWHEADER), sheet, row, colNum,ForesightConstants.BLANK_STRING);
			colNum=writeValueOnCell(style.get(ForesightConstants.YELLOWHEADER), sheet, row, colNum,ForesightConstants.BLANK_STRING);
			 rowNum++;
			 }else {
				 String[] firstHeader = { InfraConstants.VDU_INFORMATION ,ForesightConstants.BLANK_STRING,ForesightConstants.BLANK_STRING};
				 ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.YELLOWHEADER), false, sheet, firstHeader);
				 rowNum++;
			 }
			  row = getRow(rowNum, sheet);
			 colNum=4;
			 if(row != null) {
			 colNum= writeValueOnCell(style.get(InfraConstants.GREEN_HEADER), sheet, row, colNum,InfraConstants.HOST_NAME); 
			 colNum= writeValueOnCell(style.get(InfraConstants.GREEN_HEADER), sheet, row, colNum,InfraConstants.NETWORK_TYPE);
			 colNum=writeValueOnCell(style.get(InfraConstants.GREEN_HEADER), sheet, row, colNum,InfraConstants.IP_ADDRESS);
			 rowNum++;
			 }else {
				 ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.YELLOWHEADER), false, sheet, enbSheetSecondHeader);
				 rowNum++;
			 }
	}


	private Integer fillVDUDetail(Map<String, XSSFCellStyle> style, XSSFSheet sheet, XSSFRow row, Integer colNum,Map map) {
		 colNum=writeValueOnCell(style.get(ForesightConstants.DATACELL_KEY), sheet, row, colNum,map.get(InfraConstants.HOSTNAME_KEY) != null ? map.get(InfraConstants.HOSTNAME_KEY).toString() : ForesightConstants.HIPHEN); 
		 colNum= writeValueOnCell(style.get(ForesightConstants.DATACELL_KEY), sheet, row, colNum,map.get(InfraConstants.INSTANCENAME_KEY) != null ? map.get(InfraConstants.INSTANCENAME_KEY).toString() : ForesightConstants.HIPHEN);
		 colNum= writeValueOnCell(style.get(ForesightConstants.DATACELL_KEY), sheet, row, colNum,map.get(InfraConstants.IPADDRESS_KEY) != null ? map.get(InfraConstants.IPADDRESS_KEY).toString() : ForesightConstants.HIPHEN);
	return colNum;
	}


	private Integer writeValueOnCell(XSSFCellStyle style, XSSFSheet sheet, XSSFRow row, Integer colNum,String value) {
		ReportUtils.writeValueOnCell(row, style, false, sheet,colNum,value);
		 colNum++;
		 return colNum;
	}


	private void fillVCUDetailsOnSheet(Map<String, XSSFCellStyle> style, int rowNum, XSSFSheet sheet,List<Map> vcuMapDataList) {
		 try {
		for (Map map : vcuMapDataList) {
			   try {
				   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet,
						   map.get(InfraConstants.HOSTNAME_KEY) != null ? checkNullStringforReport(map.get(InfraConstants.HOSTNAME_KEY).toString()) : ForesightConstants.HIPHEN, 
						   map.get(InfraConstants.INSTANCENAME_KEY) != null ? checkNullStringforReport(map.get(InfraConstants.INSTANCENAME_KEY).toString()) : ForesightConstants.HIPHEN,
						   map.get(InfraConstants.IPADDRESS_KEY) != null ? checkNullStringforReport(map.get(InfraConstants.IPADDRESS_KEY).toString()) : ForesightConstants.HIPHEN);
				   rowNum += 1;
			   }catch(Exception exception) {
				   logger.error("Exception in creating row creation .Exception : {}", Utils.getStackTrace(exception));
			   }
			
		}
	}catch(Exception exception) {
		logger.error("Unable to fill vcu Detail on Sheet.Exception : {}", ExceptionUtils.getStackTrace(exception));
	}
	}
	private void mergeColumnsForEnodebDetailSheet(XSSFSheet sheet) {
		 sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
		   sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 6));
	   }
   private void setWidthForEnodeBDetailSheet(XSSFSheet sheet) {
	   sheet.setColumnWidth(0, 4000);
	   sheet.setColumnWidth(1, 4000);
	   sheet.setColumnWidth(2, 7000);
	   sheet.setColumnWidth(4, 4000);
	   sheet.setColumnWidth(5, 4000);
	   sheet.setColumnWidth(6, 6500);
	   } 
   @Override
   public Map<String, String> generateSectorPropertyReport(List<Map<String,SiteLayerSelection>> list,String name,String type) {
		logger.info("going to get Sector Property Report Report");
		
		try {
			Map<String, String> fileMap = new HashMap<>();
			fileMap.put(InfraConstants.KEY_FILENAME, createReportForSectorProperty(list,name,type));
		} catch (Exception e) {
			logger.error("Execption in generating sector property Report.Exception: {}", ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
   
   private String createReportForSectorProperty(List<Map<String,SiteLayerSelection>> list,String name,String type) throws IOException, Exception {
	   logger.info("Going to create the Bandwise report");
	   try {
		   XSSFWorkbook workbook = new XSSFWorkbook();
		   Map<String, XSSFCellStyle> style = createStylesForReport(workbook);
		   Map map=new HashMap<>();
		   SiteLayerSelection overviewWrapper=new SiteLayerSelection();
		   SiteLayerSelection antennaParameterWrapper=new SiteLayerSelection();
		   List<String> sheetParameters=getSheetNameFromSystemConfiguration(name, type);
		   for (Map<String,SiteLayerSelection> json : list) {
		    if(json != null) {
			if(json.get(InfraConstants.OVERVIEW) != null) {
				overviewWrapper=json.get(InfraConstants.OVERVIEW);
			    map=getNameAndFrequencyFromWrapper(overviewWrapper);
			}
			if(json.get(InfraConstants.ANTENNA_PARAMETERS) != null) {
				antennaParameterWrapper=json.get(InfraConstants.ANTENNA_PARAMETERS);
			}
			  }
		}
			ISiteVisualizationService iSiteVisualizationService = ApplicationContextProvider.getApplicationContext().getBean(ISiteVisualizationService.class);
		   Map<String,String> geographyMap=getMappingForGeographies();
		   if(sheetParameters != null && !(sheetParameters.isEmpty())) {
			  for (String parameter : sheetParameters) {
			   if(parameter.equalsIgnoreCase(InfraConstants.OVERVIEW)) {
				   List<Map> listOfMap=new ArrayList<>();
				   try {
					   try {
    					   listOfMap = iSiteVisualizationService.getSiteSummaryOverviewByBand(overviewWrapper);
    					   }catch(Exception exception) {
    						   logger.error("Unable to gt Site Summary Overview detail .Exception:{} ", exception.getMessage());
    					   }
					   generateOverviewSheetBandwise(workbook, listOfMap, style,geographyMap);
				       logger.info("overview sheet created successfully.");
				   } catch (Exception exception) {
					   logger.error("Unable to create Siteoverviewsheet", ExceptionUtils.getStackTrace(exception));
				   }
			   }
               if(parameter.equalsIgnoreCase(InfraConstants.ANTENNA_PARAMETERS)) {
            	   List<Map> listOfMap=new ArrayList<>();
            	   try {
    				   try {
    					   listOfMap = iSiteVisualizationService.getSiteSummaryAntennaParametersByBand(antennaParameterWrapper);
    					   }catch(Exception exception) {
    						   logger.error("Unable to get Antenna Parameter detail.Exception:{}", exception.getMessage());
    					   }
    				   generateAntennaParameterSheet(workbook, listOfMap, style);
    				   logger.info("Antenna Parameter sheet created successfully.");
            	   } catch (Exception exception) {
    				   logger.error("Unable to create Siteoverviewsheet", ExceptionUtils.getStackTrace(exception));
    			   }
			   }
               
		   }
		   }
		   String fileName = createFileNameForSectorPropertyReport(map.get("neName").toString(), map.get("neFrequency").toString());
		   logger.info("File created and filename is :{}" + fileName);
		   String filePath = InfraUtils.exportExcelFile(workbook,ConfigUtils.getString(ConfigEnum.SITE_REPORT_FILE_PATH.getValue()), fileName, true);
		   return filePath;
	   } catch (Exception exception) {
		   logger.info("Unable to generate site property Report.Exception: {}", ExceptionUtils.getStackTrace(exception));
		   throw new RestException("No Data Found");
	   }
   }
   private Map getNameAndFrequencyFromWrapper(SiteLayerSelection overviewWrapper) {
	   Map<String,List<Map>> filterMap= overviewWrapper.getFilters();
	   Map newMap=new HashMap<>();
	   newMap= getNeFrequencyFromMap(filterMap, newMap);
	   List<Map> listOfParentNEMap=filterMap.get("ParentNetworkElement");
	   newMap= getNenameFromFilters(filterMap, newMap,listOfParentNEMap);
	  return newMap;
   }
private Map getNeFrequencyFromMap(Map<String, List<Map>> filterMap, Map newMap) {
	List<Map> listOfNEMap=filterMap.get("NetworkElement");
	  for (Map neMap : listOfNEMap) {
		if(neMap.get("LABEL_TYPE") != null ) {
			String neFrequency=(String) neMap.get("LABEL_TYPE");
			if(neFrequency.equalsIgnoreCase("neFrequency")) {
				newMap.put("neFrequency", neMap.get("VALUE").toString());
				break;
			}
		}
	}
	  return newMap;
}
private Map getNenameFromFilters(Map<String, List<Map>> filterMap, Map newMap,List<Map> listOfParentNEMap) {
	  for (Map parentNEMap : listOfParentNEMap) {
		if(parentNEMap.get("LABEL_TYPE") != null ) {
			if(parentNEMap.get("LABEL_TYPE").toString().equalsIgnoreCase("neName")) {
				newMap.put("neName", parentNEMap.get("VALUE").toString());
				break;
			}
		}
	}
	  return newMap;
}
private Map getNeTypeFromFilters(Map<String, List<Map>> filterMap, Map newMap,List<Map> listOfParentNEMap) {
	  for (Map parentNEMap : listOfParentNEMap) {
		if(parentNEMap.get("LABEL_TYPE") != null ) {
			if(parentNEMap.get("LABEL_TYPE").toString().equalsIgnoreCase("neType")) {
				newMap.put("neType", parentNEMap.get("VALUE").toString());
				break;
			}
		}
	}
	  return newMap;
}
   private void generateOverviewSheetBandwise(XSSFWorkbook workbook,  List<Map> listOfMap,Map<String, XSSFCellStyle> style, Map<String,String> geographyMap) throws Exception {
		  logger.info("going to create overview sheet .");
		   try {
			   int rowNum = 0;
			   XSSFSheet sheet = workbook.createSheet(InfraConstants.OVERVIEW);
			   
			   SiteGeographicalDetail siteGeographicalDetail = new SiteGeographicalDetail();
			   siteGeographicalDetail = getSectorPropertyData(listOfMap, siteGeographicalDetail);
			   if (siteGeographicalDetail != null) {
				   String [] firstHeader = {"Site Id","Site Name",geographyMap.get(ForesightConstants.GEOGRAPHY_L2),geographyMap.get(ForesightConstants.GEOGRAPHY_L4),"Site Address","eNodeB ID","No of Sectors","Backhaul Info"};
				   Map<String,LinkedList<String>> headerMap = getSecondHeaderForOverview(siteGeographicalDetail);
				   String[] secondHeader = headerMap.get("SecondHeader").toArray(new String[0]);
				   String[] secondSubHeader = headerMap.get("SecondSubHeader").toArray(new String[0]);
				   
                   setHeightOfRow(rowNum, sheet,20);
				   rowNum = createRowForHeader(rowNum, sheet, firstHeader, style.get(ForesightConstants.YELLOWHEADER));
				   rowNum = fillFirstHeaderDataInOverview(style, rowNum, sheet, siteGeographicalDetail);
				   setHeightOfRow(rowNum, sheet,20);
				   rowNum = createRowForHeader(rowNum, sheet, secondHeader, style.get(ForesightConstants.BLUEHEADER));
				   setHeightOfRow(rowNum, sheet,20);
				   rowNum = createRowForHeader(rowNum, sheet, secondSubHeader, style.get(ForesightConstants.YELLOWHEADER));
				   fillDataForFirstThreeSector(style, rowNum, sheet, siteGeographicalDetail);
    			   Integer colIndex=9;
    			   
			   fillAddSectorData(style, rowNum, sheet, siteGeographicalDetail, colIndex);
			   mergeColumnsForReport(sheet, secondHeader, 3);
			   rowNum+=2;
			   }
			   if(checkListOfMap(listOfMap)) {
			   String overviewDetailHeaders=iSystemConfigurationDao.getValueByNameAndType("SECTOR_PROPERTY_REPORT","SECTOR_PROPERTY_OVERVIEW_HEADER");
			   fillCellDataInSheet(listOfMap, sheet, style, rowNum,overviewDetailHeaders);
			   }
		   } catch (Exception exception) {
			   logger.error("Unable to create Overviewsheet", ExceptionUtils.getStackTrace(exception));
		   }
	   }


private boolean checkListOfMap(List<Map> listOfMap) {
	return listOfMap != null && !listOfMap.isEmpty();
}


private SiteGeographicalDetail getSectorPropertyData(List<Map> listOfMap,SiteGeographicalDetail siteGeographicalDetail) {
	try {
		   if(checkListOfMap(listOfMap) && listOfMap.get(0) != null) {
			  Map map= getParentDataFromMap(listOfMap.get(0));
		   siteGeographicalDetail = ineVisualizationService.getSectorPropertyDataByBand(map.get("neName").toString(),map.get("neFrequency").toString(),map.get("neStatus").toString(),map.get("neType").toString());
		   }
	}catch(Exception exception) {
		   logger.info("unable to get data for overview sheet : {} ", ExceptionUtils.getStackTrace(exception));
	   }
	return siteGeographicalDetail;
}

   private Map getParentDataFromMap(Map map) {
	   Map newMap=new HashMap<>();
	   if(map.get("parentneName") != null) {
		   newMap.put("neName", map.get("parentneName").toString());
	   }
	   if(map.get("neFrequency") != null) {
		   newMap.put("neFrequency", map.get("neFrequency").toString());
	   }
	   if(map.get("parentneStatus") != null) {
		   newMap.put("neStatus", map.get("parentneStatus").toString());
	   }
	   if(map.get("parentneType") != null) {
		   newMap.put("neType", map.get("parentneType").toString());
	   }
	   return newMap;
   }

private void fillAddSectorData(Map<String, XSSFCellStyle> style, int rowNum, XSSFSheet sheet,
		SiteGeographicalDetail siteGeographicalDetail, Integer colIndex) {
	XSSFRow row;
	if(siteGeographicalDetail.getCheckAlphaAddSectorId4() != null) {
		  row = getRow(rowNum, sheet); 
		  colIndex = fillAddSectorStatus(style,sheet, siteGeographicalDetail.getAlphaAdditionalStatus(), colIndex, row);
		  colIndex = fillAddSectorDate(style, sheet, siteGeographicalDetail.getAlphaAdditionalOnAirTime(), colIndex, row);	  
		  colIndex = fillAddSectorBandwidth(style, sheet, siteGeographicalDetail.getAlphaAdditionalBandwidth(), colIndex, row);
	   }
   if(siteGeographicalDetail.getCheckBetaAddSectorId5() != null ) {
		  row = getRow(rowNum, sheet); 
		  colIndex = fillAddSectorStatus(style,sheet, siteGeographicalDetail.getBetaAdditionalStatus(), colIndex, row);
		  colIndex = fillAddSectorDate(style, sheet, siteGeographicalDetail.getBetaAdditionalOnAirTime(), colIndex, row);	  
		  colIndex = fillAddSectorBandwidth(style, sheet, siteGeographicalDetail.getBetaAdditionalBandwidth(), colIndex, row);
		 
	   
   }
   if(siteGeographicalDetail.getCheckGammaAddSectorId6() != null) {
		  row = getRow(rowNum, sheet); 
		  colIndex = fillAddSectorStatus(style,sheet, siteGeographicalDetail.getGammaAdditionalStatus(), colIndex, row);
		  colIndex = fillAddSectorDate(style, sheet,  siteGeographicalDetail.getGammaAdditionalOnAirTime(), colIndex, row);	  
		  colIndex = fillAddSectorBandwidth(style, sheet, siteGeographicalDetail.getGammaAdditionalBandwidth(), colIndex, row);
   }
}


private XSSFRow getRow(int rowNum, XSSFSheet sheet) {
	XSSFRow row;
	row=sheet.getRow(rowNum);
	return row;
}


private Integer fillAddSectorBandwidth(Map<String, XSSFCellStyle> style, XSSFSheet sheet,String bandwidth, Integer colIndex, XSSFRow row) {
	ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colIndex,bandwidth != null ? bandwidth : ForesightConstants.HIPHEN);
	colIndex++;
	return colIndex;
}


private Integer fillAddSectorDate(Map<String, XSSFCellStyle> style, XSSFSheet sheet,Date date, Integer colIndex, XSSFRow row) {
	ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colIndex,date != null ? InfraUtils.getSiteTaskDateForSectorProperty(date,true) : ForesightConstants.HIPHEN);
	  colIndex++;
	return colIndex;
}


private Integer fillAddSectorStatus(Map<String, XSSFCellStyle> style,XSSFSheet sheet, NEStatus neStatus, Integer colIndex,XSSFRow row) {
	ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colIndex,neStatus != null ? neStatus.toString() : ForesightConstants.HIPHEN);
	colIndex++;
	return colIndex;
}

private void fillDataForFirstThreeSector(Map<String, XSSFCellStyle> style, int rowNum, XSSFSheet sheet,
		SiteGeographicalDetail siteGeographicalDetail) {
	ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet,
			   siteGeographicalDetail.getAlphaStatus() != null ? siteGeographicalDetail.getAlphaStatus() : ForesightConstants.HIPHEN,
			   siteGeographicalDetail.getAlphaOnAirTime() != null ? InfraUtils.getSiteTaskDateForSectorProperty(siteGeographicalDetail.getAlphaOnAirTime(),true) : ForesightConstants.HIPHEN,
			   siteGeographicalDetail.getAlphaBandwidth() != null ? siteGeographicalDetail.getAlphaBandwidth() : ForesightConstants.HIPHEN ,
			   siteGeographicalDetail.getBetaStatus() != null ? siteGeographicalDetail.getBetaStatus() : ForesightConstants.HIPHEN,
			   siteGeographicalDetail.getBetaOnAirTime() != null ? InfraUtils.getSiteTaskDateForSectorProperty(siteGeographicalDetail.getBetaOnAirTime(),true) : ForesightConstants.HIPHEN,
			   siteGeographicalDetail.getBetaBandwidth() != null ? siteGeographicalDetail.getBetaBandwidth() : ForesightConstants.HIPHEN,
			   siteGeographicalDetail.getGammaStatus() != null ? siteGeographicalDetail.getGammaStatus() : ForesightConstants.HIPHEN,
			   siteGeographicalDetail.getGammaOnAirTime() != null ? InfraUtils.getSiteTaskDateForSectorProperty(siteGeographicalDetail.getGammaOnAirTime(),true) : ForesightConstants.HIPHEN,
			   siteGeographicalDetail.getGammaBandwidth() != null ? siteGeographicalDetail.getGammaBandwidth() : ForesightConstants.HIPHEN);
}


private int createRowForHeader(int rowNum, XSSFSheet sheet, String[] firstHeader, XSSFCellStyle styleType) {
	ReportUtils.createRow(sheet.createRow(rowNum), styleType, false, sheet, firstHeader);
	   rowNum++;
	return rowNum;
}


private int fillFirstHeaderDataInOverview(Map<String, XSSFCellStyle> style, int rowNum, XSSFSheet sheet,
		SiteGeographicalDetail siteGeographicalDetail) {
	ReportUtils.createRow
	   (sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet,
	   checkNullStringforReport(siteGeographicalDetail.getSapid()), 
	   checkNullStringforReport(siteGeographicalDetail.getSiteName()),
	   checkNullStringforReport(siteGeographicalDetail.getGeographyL2()),
	   checkNullStringforReport(siteGeographicalDetail.getGeographyL4()),
	   checkNullStringforReport(siteGeographicalDetail.getSiteAddress()),
	   siteGeographicalDetail.getEnodeBId() != null ? siteGeographicalDetail.getEnodeBId() : ForesightConstants.HIPHEN,
	   siteGeographicalDetail.getNumberOfSector() != null ? siteGeographicalDetail.getNumberOfSector() : ForesightConstants.HIPHEN,
	   siteGeographicalDetail.getBackhaulMedia() != null ? siteGeographicalDetail.getBackhaulMedia() : ForesightConstants.HIPHEN
	);
	   rowNum+=2;
	return rowNum;
}
   private void generateAntennaParameterSheet(XSSFWorkbook workbook,  List<Map> listOfMap,Map<String, XSSFCellStyle> style) throws Exception {
	   logger.info("Going to create Antenna Parameter sheet.");
	   try {
		   int rowNum= 0;
		   XSSFSheet sheet = workbook.createSheet(InfraConstants.ANTENNA_PARAMETERS);
		   String antennaDetailHeaders=iSystemConfigurationDao.getValueByNameAndType("SECTOR_PROPERTY_REPORT","SECTOR_PROPERTY_ANTENNA_DETAIL_HEADER");
		   fillCellDataInSheet(listOfMap, sheet, style, rowNum,antennaDetailHeaders);
	   } catch (Exception exception) {
		   logger.error("Unable to create Overviewsheet {}", ExceptionUtils.getStackTrace(exception));
	   }
   }
   
   private void fillCellDataInSheet( List<Map> listOfMap, XSSFSheet sheet,Map<String, XSSFCellStyle> style, int rowNum,String sectorPropertyHeaders) {
	   if(checkListOfMap(listOfMap)) {
		  if(sectorPropertyHeaders != null) {
		        Map<String, String> jsonMap = getMapFromJSON(sectorPropertyHeaders);
		       if(jsonMap != null) {
		        List<String> firstHeaderList=new ArrayList(jsonMap.keySet());	
				String[] firstHeader = firstHeaderList.toArray(new String[0]);
				setHeightOfRow(rowNum, sheet,20);
				ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.YELLOWHEADER), false, sheet, firstHeader);
				rowNum++;
				for (Map map : listOfMap) {
					Integer colNum=0;
					XSSFRow row=sheet.createRow(rowNum); 
					row = getRow(rowNum, sheet);
					if(map != null) {
						for (String header : firstHeaderList) {
							if(jsonMap.get(header) != null) {
								String columnKey=jsonMap.get(header).toString();
								ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colNum,map.get(columnKey) != null ? map.get(columnKey).toString() : ForesightConstants.HIPHEN); 
								colNum++;
							}
						}
						}
				rowNum++;
				}
				setColumnWidthForReport(sheet);
	  		}
	  		}
		  }
   }
   @Override
   public Map<String, String> generateSitePropertyReport(List<Map<String,SiteLayerSelection>> list,String name,String type) {
		logger.info("going to get site Property Report Report");
		Map<String, String> fileMap = new HashMap<>();
		try {
			fileMap.put(InfraConstants.KEY_FILENAME, createReportForSiteProperty(list,name,type));
		} catch (Exception e) {
			logger.error("Execption in generating site property Report.Exception: {}", ExceptionUtils.getStackTrace(e));
		}
		return fileMap;
	}
   
   private String createReportForSiteProperty(List<Map<String,SiteLayerSelection>> list, String name,String type) throws IOException, Exception {
	   logger.info("Going to create the Site Property report.");
	   try {
		  
		   Map map=new HashMap<>();
		   SiteLayerSelection overviewWrapper=new SiteLayerSelection();
		   SiteLayerSelection geographicalDetailWrapper=new SiteLayerSelection();
		   SiteLayerSelection siteMilestoneWrapper=new SiteLayerSelection();
		   
		   for (Map<String,SiteLayerSelection> json : list) {
			    if(json != null) {
				if(json.get(InfraConstants.OVERVIEW) != null) {
					overviewWrapper=json.get(InfraConstants.OVERVIEW);
					 map = getNameAndNetypeFromMap(map, overviewWrapper);
				}
				if(json.get(InfraConstants.GEOGRAPHICAL_DETAILS) != null) {
					geographicalDetailWrapper=json.get(InfraConstants.GEOGRAPHICAL_DETAILS);
				}
				if(json.get(InfraConstants.SITE_MILESTONES) != null) {
					siteMilestoneWrapper=json.get(InfraConstants.SITE_MILESTONES);
				}
				  }
			}
		   
		   String excelFileName = createFileNameForSitePropertyReport(map.get("neName").toString());
		   XSSFWorkbook workbook = new XSSFWorkbook();
		   Map<String, XSSFCellStyle> style = createStylesForReport(workbook);
		   List<String> sheetParameters=getSheetNameFromSystemConfiguration(name, type);
		   Map<String,String> geographyMap=getMappingForGeographies();
		   if(sheetParameters != null && !(sheetParameters.isEmpty())) {
		   for (String parameter : sheetParameters) {
			if(parameter.equalsIgnoreCase(InfraConstants.OVERVIEW)) {
				try {
				
					   generateSiteOverviewSheet(workbook,style,geographyMap,overviewWrapper,map);
				  logger.info("overview sheet created successfully.");
				   } catch (Exception exception) {
					   logger.error("Unable to create Siteoverviewsheet", ExceptionUtils.getStackTrace(exception));
				   }
				logger.info("geographical detail wrapper after overview: {}",geographicalDetailWrapper);
				   
			}
			if(parameter.equalsIgnoreCase(InfraConstants.GEOGRAPHICAL_DETAILS)) {
				try {
					generateGeographicalDetailsSheet(workbook,style,geographyMap,geographicalDetailWrapper);
					   logger.info("Geographical detail sheet created successfully.");
				   } catch (Exception exception) {
					   logger.error("Unable to create Siteoverviewsheet", ExceptionUtils.getStackTrace(exception));
				   }
			}
			if(parameter.equalsIgnoreCase(InfraConstants.SITE_MILESTONES)) {
				 try {
					generateSiteMilestones(workbook,style,siteMilestoneWrapper);
					   logger.info("Site Milestones sheet created successfully.");
				   } catch (Exception exception) {
					   logger.error("Unable to create Siteoverviewsheet", ExceptionUtils.getStackTrace(exception));
				   }
			}
			if(parameter.equalsIgnoreCase("SITES ENODEB DETAILS")) {
				try {
					generateEnodeBDetailSheet(workbook,style,map.get("neName").toString(),map.get("neType").toString());
				  logger.info("enodebdetail sheet created successfully.");
				   } catch (Exception exception) {
					   logger.error("Unable to create Siteoverviewsheet", ExceptionUtils.getStackTrace(exception));
				   }
			}
		   }
	   }
		   String filePath = InfraUtils.exportExcelFile(workbook,ConfigUtils.getString(ConfigEnum.SITE_REPORT_FILE_PATH.getValue()), excelFileName, true);
		   logger.info("The uri is as follows -> "+filePath);
		   
		   return filePath;
	   } catch (Exception exception) {
		   logger.info("Unable to generate Sector Property Report: Exception: {}", ExceptionUtils.getStackTrace(exception));
		   throw new RestException("No Data Found");
	   }
   }
private Map getNameAndNetypeFromMap(Map map, SiteLayerSelection overviewWrapper) {
	Map<String,List<Map>> filterMap= overviewWrapper.getFilters();
	if(filterMap != null) {
		List<Map> listOfParentNEMap=filterMap.get("NetworkElement");
		map= getNenameFromFilters(filterMap, map,listOfParentNEMap);
		map= getNeTypeFromFilters(filterMap, map,listOfParentNEMap);
	}
	return map;
}
  
   private void generateSiteOverviewSheet(XSSFWorkbook workbook,Map<String, XSSFCellStyle> style, Map<String,String> geographyMap, SiteLayerSelection overviewWrapper, Map detail)
				   throws Exception {
	   logger.info("going to generate overview sheet.");
	   try {
		   int rowNum = 0;
		   Map secondHeaderMap=null;
		   XSSFSheet sheet = workbook.createSheet(OVERVIEW);
		   ISiteVisualizationService iSiteVisualizationService = ApplicationContextProvider.getApplicationContext().getBean(ISiteVisualizationService.class);
		   Map<String,Map> map=iSiteVisualizationService.getSiteOverviewData(overviewWrapper);
		  if(map != null) {
		   secondHeaderMap=map.get("sectorMap");
		  }
		  Map neSiteDetailMap=new HashMap<>();
		   String siteOverviewHeaders=iSystemConfigurationDao.getValueByNameAndType("SITE_PROPERTY_REPORT","SITE_PROPERTY_OVERVIEW_HEADER");
		  		if(siteOverviewHeaders != null) {
		        Map<String, String> jsonMap = getMapFromJSON(siteOverviewHeaders);
		       
		        List<String> firstHeaderList=new ArrayList(jsonMap.keySet());	
				   if(ineSiteDetailService !=null) {
					   neSiteDetailMap = ineSiteDetailService.getBBULatLongOfSite(detail.get("neName").toString(),detail.get("neType").toString());
					   }
		  		 String[] firstHeader = firstHeaderList.toArray(new String[0]);
					setHeightOfRow(rowNum, sheet,20);
					ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.YELLOWHEADER), false, sheet, firstHeader);
					rowNum++;
					
					Integer colNum=0;
				XSSFRow row=sheet.createRow(rowNum); 
				row = getRow(rowNum, sheet);
				if(map != null) {
					for (String header : firstHeaderList) {
					try {
					if(jsonMap.get(header) != null) {
						String columnKey=jsonMap.get(header).toString();
						if(columnKey.equalsIgnoreCase("bbuLatitude") || columnKey.equalsIgnoreCase("bbuLongitude")) {
							if(neSiteDetailMap !=null && neSiteDetailMap.size()>0 ) {
								ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colNum,neSiteDetailMap.get(columnKey)!=null ? neSiteDetailMap.get(columnKey).toString() : ForesightConstants.HIPHEN);
							}
						}else {
						ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colNum,map.get(columnKey) != null ? String.valueOf(map.get(columnKey)) : ForesightConstants.HIPHEN); 
							}
						colNum++;
					}
				}catch(Exception exception) {
					logger.error("unable to get data from map for headername: {},Exception: {}",header,exception.getStackTrace());
				}
				}
				}
				
				rowNum+=2;
	   }
	           setColumnWidthForReport(sheet);
	           
	           List<String> neFrequencyList=ineBandDetailDao.getNefrequencyForNename(detail.get("neName").toString());
			  try {
				  if(secondHeaderMap != null && neFrequencyList != null) {
			  String[] secondHeader = getFirstHeaderOverview(neFrequencyList,secondHeaderMap).toArray(new String[0]);
			  setHeightOfRow(rowNum, sheet,20);
			  ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.YELLOWHEADER), false, sheet, secondHeader);
			   mergeColumnsForReport(sheet, secondHeader, 3);
			   rowNum++;
			   String[] secondSubHeader = getSecondHeaderOverview(neFrequencyList,secondHeaderMap).toArray(new String[0]);
			   setHeightOfRow(rowNum, sheet,20);
						   
			   rowNum=createRowForHeader(rowNum, sheet, secondSubHeader, style.get(InfraConstants.GREEN_HEADER));
			   String[] thirdHeader = getThirdHeaderOverview(secondHeaderMap,neFrequencyList).toArray(new String[0]);
			   setHeightOfRow(rowNum, sheet,20);
			   rowNum=createRowForHeader(rowNum, sheet, thirdHeader, style.get(ForesightConstants.DATACELL_KEY));
			   rowNum += 2;
			  }
			  }catch(Exception exception) {
				  logger.error("unable to get nefrequency.,Exception : {}",ExceptionUtils.getStackTrace(exception));
			  }
			  
		
	   } catch (Exception exception) {
		   logger.error("Unable to create Overviewsheet", ExceptionUtils.getStackTrace(exception));
	   }
   }
private Map<String, String> getMapFromJSON(String siteOverviewHeaders) {
	try {
	Map<String, String> jsonMap = new HashMap<String, String>();
	  		ObjectMapper mapper = new ObjectMapper();
	        jsonMap = mapper.readValue(siteOverviewHeaders,new TypeReference<Map<String, String>>(){});
	return jsonMap;
	}catch(Exception exception) {
		logger.error("unable to parse json.Exception : {}",exception.getMessage());
	}
	return null;
}
   private void generateGeographicalDetailsSheet(XSSFWorkbook workbook, Map<String, XSSFCellStyle> style, Map<String,String> geographyMap,SiteLayerSelection geographicalDetailWrapper) throws Exception {
	   logger.info("going to generate geographical detail sheet.wrapper:{}",geographicalDetailWrapper);
	try {
		int rowNum = 0;
		XSSFSheet sheet = workbook.createSheet("GeographicalDetails");
		 ISiteVisualizationService iSiteVisualizationService = ApplicationContextProvider.getApplicationContext().getBean(ISiteVisualizationService.class);
		   Map map=iSiteVisualizationService.getSiteGeographicalData(geographicalDetailWrapper);
		   logger.info("site geographical map: {}",map);
		
		   String siteOverviewHeaders=iSystemConfigurationDao.getValueByNameAndType("SITE_PROPERTY_REPORT","SITE_PROPERTY_GEOGRAPHICAL_HEADER");
	  		if(siteOverviewHeaders != null) {
	        Map<String, String> jsonMap = new HashMap<String, String>();
	  		ObjectMapper mapper = new ObjectMapper();
	        jsonMap = mapper.readValue(siteOverviewHeaders,new TypeReference<Map<String, String>>(){});
	        List<String> firstHeaderList=new ArrayList(jsonMap.keySet());	
			String[] firstHeader = firstHeaderList.toArray(new String[0]);
			setHeightOfRow(rowNum, sheet,20);
			ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.YELLOWHEADER), false, sheet, firstHeader);
			rowNum++;
				
			Integer colNum=0;
			XSSFRow row=sheet.createRow(rowNum); 
			row = getRow(rowNum, sheet);
			if(map != null) {
			for (String header : firstHeaderList) {
				if(jsonMap.get(header) != null) {
				String columnKey=jsonMap.get(header).toString();
				ReportUtils.writeValueOnCell(row, style.get(ForesightConstants.DATACELL_KEY), false, sheet, colNum,map.get(columnKey) != null ? map.get(columnKey).toString() : ForesightConstants.HIPHEN); 
				colNum++;
				}
			}
			}
			
			rowNum+=2;
         }
			setColumnWidthForReport(sheet);

	} catch (Exception exception) {
		
		logger.error("Unable to create Overviewsheet", ExceptionUtils.getStackTrace(exception));
	}
}
}