package com.inn.foresight.core.infra.service.impl;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.ISmallCellSiteDetailDao;
import com.inn.foresight.core.infra.model.RANDetail;
import com.inn.foresight.core.infra.model.SmallCellSiteDetail;
import com.inn.foresight.core.infra.service.INEReportService;
import com.inn.foresight.core.infra.service.ISmallCellSiteDetailService;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.InfraUtils;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.wrapper.SmallCellSiteDetailWrapper;
import com.inn.foresight.core.report.utils.ReportUtils;
/**
 * The Class SmallCellSiteDetailServiceImpl.
 */
@Service("SmallCellSiteDetailServiceImpl")
public class SmallCellSiteDetailServiceImpl extends AbstractService<Integer, SmallCellSiteDetail> implements ISmallCellSiteDetailService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(SmallCellSiteDetailServiceImpl.class);

	/** The small cell site detail dao. */
	@Autowired
	private ISmallCellSiteDetailDao smallCellSiteDetailDao;

	@Autowired
	private INEReportService ineReportService;
	/**
	 * Sets the dao.
	 *
	 * @param smallCellSiteDetailDao the new dao
	 */
	public void setDao(ISmallCellSiteDetailDao smallCellSiteDetailDao) {
		super.setDao(smallCellSiteDetailDao);
		this.smallCellSiteDetailDao = smallCellSiteDetailDao;
	}
	@Override
	@Transactional
	 public SmallCellSiteDetailWrapper getSmallCellSiteDetails(String neName) {
		 SmallCellSiteDetailWrapper smallCellSiteDetailWrapper= new SmallCellSiteDetailWrapper();
		 try{
			 logger.info("netype: {}",NEType.valueOf(InfraConstants.ODSC_CELL));
			 List<RANDetail> smallCellSiteDetail = smallCellSiteDetailDao.getSmallCellSiteDetails(neName, InfraConstants.ODSC_CELL);
			
			 if(smallCellSiteDetail.get(0) == null){
				 logger.info("data is null");
				 return smallCellSiteDetailWrapper; 
			 }
			 
			 smallCellSiteDetailWrapper =  getSmallCellSiteDetailWrapper(smallCellSiteDetail.get(0));
		
		 }catch(Exception exception){
			 logger.error("Exception occure in getSmallCellSiteDetails : {}",Utils.getStackTrace(exception));
		 }
		return smallCellSiteDetailWrapper;
		 
	 }
	private SmallCellSiteDetailWrapper getSmallCellSiteDetailWrapper(RANDetail smallCellSiteDetail){
		
		 SmallCellSiteDetailWrapper smallCellSiteDetailWrapper =  new SmallCellSiteDetailWrapper();
		 try{
			 if(smallCellSiteDetail != null) {
				 if(smallCellSiteDetail.getNetworkElement() != null) {
					 if(smallCellSiteDetail.getNetworkElement().getNeLocation() != null) {
					 if(smallCellSiteDetail.getNetworkElement().getNeLocation().getAddress() != null)
						 smallCellSiteDetailWrapper.setAddress(smallCellSiteDetail.getNetworkElement().getNeLocation().getAddress());
					 }
					 if(smallCellSiteDetail.getNetworkElement().getLatitude() != null)
						 smallCellSiteDetailWrapper.setLatitude(smallCellSiteDetail.getNetworkElement().getLatitude());
					 if(smallCellSiteDetail.getNetworkElement().getLongitude() != null)
						 smallCellSiteDetailWrapper.setLongitude(smallCellSiteDetail.getNetworkElement().getLongitude());
					 if(smallCellSiteDetail.getNetworkElement().getVendor() != null)
						 smallCellSiteDetailWrapper.setVendor(smallCellSiteDetail.getNetworkElement().getVendor());
					 if(smallCellSiteDetail.getNetworkElement().getMcc() != null)
						 smallCellSiteDetailWrapper.setMcc(smallCellSiteDetail.getNetworkElement().getMcc());
					 if(smallCellSiteDetail.getNetworkElement().getMnc() != null)
						 smallCellSiteDetailWrapper.setMnc(smallCellSiteDetail.getNetworkElement().getMnc());
					 if(smallCellSiteDetail.getNetworkElement().getNeName() != null)
						 smallCellSiteDetailWrapper.setCellName(smallCellSiteDetail.getNetworkElement().getNeName());
					 if(smallCellSiteDetail.getNetworkElement().getEnbid() != null)
						 smallCellSiteDetailWrapper.setEnodeBId(smallCellSiteDetail.getNetworkElement().getEnbid());
					 if(smallCellSiteDetail.getNetworkElement().getEcgi() != null)
						 smallCellSiteDetailWrapper.setEcgi(smallCellSiteDetail.getNetworkElement().getEcgi());
					 if(smallCellSiteDetail.getNetworkElement().getNetworkElement() != null) {
						 if(smallCellSiteDetail.getNetworkElement().getNetworkElement().getNeName() != null)
							 smallCellSiteDetailWrapper.setSiteId(smallCellSiteDetail.getNetworkElement().getNetworkElement().getNeName());
						 if(smallCellSiteDetail.getNetworkElement().getNetworkElement().getNeName() != null)
							 smallCellSiteDetailWrapper.setNeName(smallCellSiteDetail.getNetworkElement().getNetworkElement().getNeName());
					 }
					if(smallCellSiteDetail.getNetworkElement().getNeStatus() != null)
						 smallCellSiteDetailWrapper.setSiteStatus(smallCellSiteDetail.getNetworkElement().getNeStatus().name());
					 if(smallCellSiteDetail.getNetworkElement().getNeStatus() != null)
						 smallCellSiteDetailWrapper.setSmallCellStatus(smallCellSiteDetail.getNetworkElement().getNeStatus().name());
				 if(smallCellSiteDetail.getNetworkElement().getEmsServer() != null) {
					 if(smallCellSiteDetail.getNetworkElement().getEmsServer().getEmsName() != null) {
					 smallCellSiteDetailWrapper.setEmsname(smallCellSiteDetail.getNetworkElement().getEmsServer().getEmsName());
					 smallCellSiteDetailWrapper.setEmsid(smallCellSiteDetail.getNetworkElement().getEmsServer().getEmsName());
					 }if(smallCellSiteDetail.getNetworkElement().getEmsServer().getIp() != null)
					 smallCellSiteDetailWrapper.setIp(smallCellSiteDetail.getNetworkElement().getEmsServer().getIp());
						
				 }
				 
				 
				 
				 }
				 smallCellSiteDetailWrapper.setAntennaType(smallCellSiteDetail.getAntennaType());
				 smallCellSiteDetailWrapper.setAntennaHeight(smallCellSiteDetail.getAntennaHeight());
				 smallCellSiteDetailWrapper.setAntennaGain(smallCellSiteDetail.getAntennaGain());
				 smallCellSiteDetailWrapper.setAntennaModel(smallCellSiteDetail.getAntennaModel());
				 smallCellSiteDetailWrapper.setVerticalBeamWidth(smallCellSiteDetail.getVerticalBeamWidth());
				 smallCellSiteDetailWrapper.setHorizontalBeamWidth(smallCellSiteDetail.getHorizontalBeamWidth());
				 if(smallCellSiteDetail.getNeBandDetail() != null) {
					 if(smallCellSiteDetail.getNeBandDetail().getBackhauInfo() != null)
						 smallCellSiteDetailWrapper.setBackHaulMedia(smallCellSiteDetail.getNeBandDetail().getBackhauInfo());
					 if(smallCellSiteDetail.getNeBandDetail().getCurrentStage() != null)
						 smallCellSiteDetailWrapper.setCurrentStage(smallCellSiteDetail.getNeBandDetail().getCurrentStage());
					if(smallCellSiteDetail.getNeBandDetail().getNetworkElement() != null) {
					 if(smallCellSiteDetail.getNeBandDetail().getNetworkElement().getFriendlyname() != null)
						 smallCellSiteDetailWrapper.setSiteName(smallCellSiteDetail.getNeBandDetail().getNetworkElement().getFriendlyname());
					}
					if(smallCellSiteDetail.getNeBandDetail().getNeDetail() != null) {
						if(smallCellSiteDetail.getNeBandDetail().getNeDetail().getContactName() != null)
							 smallCellSiteDetailWrapper.setContactName(smallCellSiteDetail.getNeBandDetail().getNeDetail().getContactName());
						 if(smallCellSiteDetail.getNeBandDetail().getNeDetail().getContactNumber() != null)
							 smallCellSiteDetailWrapper.setContactNumber(smallCellSiteDetail.getNeBandDetail().getNeDetail().getContactNumber());
					 if(smallCellSiteDetail.getNeBandDetail().getNeDetail().getCategory() != null)
						 smallCellSiteDetailWrapper.setSiteCategory(smallCellSiteDetail.getNeBandDetail().getNeDetail().getCategory());
				 }
				 }
				 if(smallCellSiteDetail.getEarfcn() != null)
					 smallCellSiteDetailWrapper.setEarfcn(smallCellSiteDetail.getEarfcn());
				 if(smallCellSiteDetail.getAntennaHeight() != null)
					 smallCellSiteDetailWrapper.setHeight(smallCellSiteDetail.getAntennaHeight());
				 
				 if(smallCellSiteDetail.getPci() != null)
					 smallCellSiteDetailWrapper.setPci(smallCellSiteDetail.getPci());
				 if(smallCellSiteDetail.getTxPower() != null)
					 smallCellSiteDetailWrapper.setTxpower(smallCellSiteDetail.getTxPower());
				 if(smallCellSiteDetail.getAzimuth() != null)
					 smallCellSiteDetailWrapper.setAzimuth(smallCellSiteDetail.getAzimuth());
				 if(smallCellSiteDetail.getElecTilt() != null)
					 smallCellSiteDetailWrapper.setElectricalTilt(smallCellSiteDetail.getElecTilt());
				 if(smallCellSiteDetail.getMechTilt() != null)
					 smallCellSiteDetailWrapper.setMechTilt(smallCellSiteDetail.getMechTilt());
				 if(smallCellSiteDetail.getTrackingArea() != null)
					 smallCellSiteDetailWrapper.setTrackingArea(smallCellSiteDetail.getTrackingArea());
				
			 }
		 }catch(Exception e){
			 logger.error("Exception occure in getSmallCellSiteDetailWrapper : {}",Utils.getStackTrace(e));
		 } 
		 return smallCellSiteDetailWrapper ;
	 }
	@Override
	@Transactional
	public byte[] getReportForSmallCellSiteSummary(String neName,String excelFileName) {
		logger.info("going to get Small Cell Site data in excel sheet by neName {}", neName);
		 int rowNum = 0;
		 XSSFWorkbook workBook = new XSSFWorkbook();
			try {
				List<RANDetail> smallCellSiteDetailList = smallCellSiteDetailDao.getSmallCellSiteDetails(neName, InfraConstants.ODSC_CELL);
				if(smallCellSiteDetailList != null && !smallCellSiteDetailList.isEmpty()) {
				if(smallCellSiteDetailList.get(0) != null) {
						RANDetail smallCellSiteDetail=smallCellSiteDetailList.get(0);
				Sheet sheet = workBook.createSheet(ForesightConstants.SMALLCELL_SITE);
				Map<String, XSSFCellStyle> style= createStylesForReport(workBook);
				String SMALLCELL_SITE_SUMMARY[] = { "Site Name", "Site Id", "Site Address", "Latitude", "Longitude",  "SmallCell height", "Site Status", "Backhaul Media","Contact Person Name","Contact Person Number","Total Small Cells","On-Air Small Cells" };
				sheet.createRow(rowNum).setHeightInPoints(40);
				   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.ORANGEHEADER), false, sheet, SMALLCELL_SITE_SUMMARY);
				   rowNum++;
				   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet,
						   smallCellSiteDetail.getNeBandDetail() != null && smallCellSiteDetail.getNeBandDetail().getNetworkElement() != null && smallCellSiteDetail.getNeBandDetail().getNetworkElement().getFriendlyname() != null ? smallCellSiteDetail.getNeBandDetail().getNetworkElement().getFriendlyname() : ForesightConstants.HIPHEN,
						   smallCellSiteDetail.getNetworkElement() != null && smallCellSiteDetail.getNetworkElement().getNetworkElement() != null && smallCellSiteDetail.getNetworkElement().getNetworkElement().getNeName() != null ? smallCellSiteDetail.getNetworkElement().getNetworkElement().getNeName() : ForesightConstants.HIPHEN,
						   smallCellSiteDetail.getNetworkElement() != null && smallCellSiteDetail.getNetworkElement().getNeLocation() != null && smallCellSiteDetail.getNetworkElement().getNeLocation().getAddress() != null && !smallCellSiteDetail.getNetworkElement().getNeLocation().getAddress().equalsIgnoreCase(ForesightConstants.BLANK_STRING) ? smallCellSiteDetail.getNetworkElement().getNeLocation().getAddress() : ForesightConstants.HIPHEN ,
						   smallCellSiteDetail.getNetworkElement() != null && smallCellSiteDetail.getNetworkElement().getLatitude() != null ? smallCellSiteDetail.getNetworkElement().getLatitude() : ForesightConstants.HIPHEN,
						   smallCellSiteDetail.getNetworkElement() != null && smallCellSiteDetail.getNetworkElement().getLongitude() != null ? smallCellSiteDetail.getNetworkElement().getLongitude() : ForesightConstants.HIPHEN,
						   smallCellSiteDetail.getAntennaHeight() != null ? smallCellSiteDetail.getAntennaHeight().toString() : ForesightConstants.HIPHEN,
						   smallCellSiteDetail.getNeBandDetail() != null && smallCellSiteDetail.getNeBandDetail().getBandStatus() != null ? smallCellSiteDetail.getNeBandDetail().getBandStatus().toString() : ForesightConstants.HIPHEN   ,
						   smallCellSiteDetail.getNeBandDetail() != null && smallCellSiteDetail.getNeBandDetail().getBackhauInfo() != null ? smallCellSiteDetail.getNeBandDetail().getBackhauInfo() : ForesightConstants.HIPHEN,
						   smallCellSiteDetail.getNeBandDetail() != null &&	 smallCellSiteDetail.getNeBandDetail().getNeDetail() != null &&  smallCellSiteDetail.getNeBandDetail().getNeDetail().getContactName() != null ? smallCellSiteDetail.getNeBandDetail().getNeDetail().getContactName() : ForesightConstants.HIPHEN,
						   smallCellSiteDetail.getNeBandDetail() != null &&	 smallCellSiteDetail.getNeBandDetail().getNeDetail() != null &&  smallCellSiteDetail.getNeBandDetail().getNeDetail().getContactNumber() != null ? smallCellSiteDetail.getNeBandDetail().getNeDetail().getContactNumber() : ForesightConstants.HIPHEN,
						   ForesightConstants.HIPHEN,ForesightConstants.HIPHEN
						   );
				   setColumnWidthForReport(sheet);
				
				}
			}
				InfraUtils.exportExcelFile(workBook,ConfigUtils.getString(ConfigEnum.SITE_REPORT_FILE_PATH.getValue()), excelFileName, true);
				byte[] result=ineReportService.getFileByPath(excelFileName);
				return result;
			}
			catch (Exception exception) {
				logger.error("Error in downloading Small Cell Site Detail Report Message {} Exception {} ", exception.getMessage(), Utils.getStackTrace(exception));
				throw new RestException("Error in downloading Small Cell Site Detail Report");
			}
			
			
		}
	public static Map<String, XSSFCellStyle> createStylesForReport(Workbook wb) {
		Map<String, XSSFCellStyle> styles = new HashMap<>();
		XSSFCellStyle style;

		Color color1 = Color.decode(InfraConstants.ORANGE_COLOR_CODE);

		XSSFColor orange = new XSSFColor(color1);

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
	
	@Override
	@Transactional
	public byte[] getReportForSmallCellSiteParameter(String neName,String excelFileName) {
		logger.info("going to get SmallCell Parameter Details in Excel Sheet for neName {}", neName);
		try {
			List<RANDetail> smallCellSiteDetailList = smallCellSiteDetailDao.getSmallCellSiteDetails(neName, InfraConstants.ODSC_CELL);
			XSSFWorkbook workBook = new XSSFWorkbook();
			createSheetForRanDetails(workBook, smallCellSiteDetailList.get(0));
//			createSheetForSmallCellOam(workBook, smallCellSiteDetailList.get(0));
//			createSheetForSmallCellSignaling(workBook, smallCellSiteDetailList.get(0));
//			createSheetForSmallCellBearer(workBook, smallCellSiteDetailList.get(0));
			createSheetForEmsDetails(workBook, smallCellSiteDetailList.get(0));
			createSheetForSmallCellInfo(workBook, smallCellSiteDetailList.get(0));
			InfraUtils.exportExcelFile(workBook,ConfigUtils.getString(ConfigEnum.SITE_REPORT_FILE_PATH.getValue()), excelFileName, true);
			byte[] result=ineReportService.getFileByPath(excelFileName);
			return result;
		}
		catch (Exception e) {
			logger.error("Error in downloading site properties sheet Message {} Exception {} ", e.getMessage(), Utils.getStackTrace(e));
			throw new RestException("Error in downloading smallcell Parameters report");
		}
	}
	private Workbook createSheetForRanDetails(Workbook workBook,RANDetail  smallCellSiteDetail) {
		logger.info("going to create ran detail sheet. ");
		 int rowNum = 0;
			if(smallCellSiteDetail != null) {
			Sheet sheet = workBook.createSheet(ForesightConstants.RAN_DETAILS);
			Map<String, XSSFCellStyle> style= createStylesForReport(workBook);
			String RAN_DETAILS_KEYS[] = { "Tracking Area", "eNodeB Package", "EARFCN", "Vendor",  "MCC", "MNC" };
			sheet.createRow(rowNum).setHeightInPoints(40);
			   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.ORANGEHEADER), false, sheet, RAN_DETAILS_KEYS);
			   rowNum++;
			   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet,
					   smallCellSiteDetail.getTrackingArea() != null ? smallCellSiteDetail.getTrackingArea() : ForesightConstants.HIPHEN,
					   ForesightConstants.HIPHEN,
					   smallCellSiteDetail.getEarfcn() != null ? smallCellSiteDetail.getEarfcn() : ForesightConstants.HIPHEN ,
					   smallCellSiteDetail.getNetworkElement() != null && smallCellSiteDetail.getNetworkElement().getVendor() != null ? smallCellSiteDetail.getNetworkElement().getVendor().toString() : ForesightConstants.HIPHEN,
					   smallCellSiteDetail.getNetworkElement() != null && smallCellSiteDetail.getNetworkElement().getMcc() != null ? smallCellSiteDetail.getNetworkElement().getMcc() : ForesightConstants.HIPHEN,
					   smallCellSiteDetail.getNetworkElement() != null && smallCellSiteDetail.getNetworkElement().getMnc() != null ? smallCellSiteDetail.getNetworkElement().getMnc() : ForesightConstants.HIPHEN);
			   setColumnWidthForReport(sheet);
			}	   
		logger.info("ran detail sheet created successfully.");
		return workBook;
	}
	
	private Workbook createSheetForSmallCellOam(Workbook workBook, RANDetail smallCellSiteDetail) {
		logger.info("going to create Small Cell Oam sheet. ");
		Sheet sheet = workBook.createSheet(ForesightConstants.SMALL_CELL_OAM);
		 int rowNum = 0;
			if(smallCellSiteDetail != null) {
			Map<String, XSSFCellStyle> style= createStylesForReport(workBook);
			String OAM_DETAILS_KEYS[] = { ForesightConstants.OAM_VLAN, ForesightConstants.OAM_GATEWAY_IPV6_ADDRESS, ForesightConstants.OAM_IP };
			sheet.createRow(rowNum).setHeightInPoints(40);
			   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.ORANGEHEADER), false, sheet, OAM_DETAILS_KEYS);
			   rowNum++;
			   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet,
					   ForesightConstants.HIPHEN,  ForesightConstants.HIPHEN,ForesightConstants.HIPHEN);
			   setColumnWidthForReport(sheet);
			}
		setColumnWidthForReport(sheet);
		logger.info("Small Cell Oam sheet created successfully.");
		return workBook;
	}
	private Workbook createSheetForSmallCellSignaling(Workbook workBook, RANDetail smallCellSiteDetail) {
		logger.info("Going to create Small Cell Signaling sheet.");
		Sheet sheet = workBook.createSheet(ForesightConstants.SMALL_CELL_SIGNALING);
		int rowNum = 0;
		if(smallCellSiteDetail != null) {
		Map<String, XSSFCellStyle> style= createStylesForReport(workBook);
		String SIGNALING_DETAILS_KEYS[] = { ForesightConstants.SIGNALING_VLAN, ForesightConstants.SIGNALING_GATEWAY_IPV6_ADDRESS, ForesightConstants.SIGNALING_IP };
		sheet.createRow(rowNum).setHeightInPoints(40);
		   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.ORANGEHEADER), false, sheet, SIGNALING_DETAILS_KEYS);
		   rowNum++;
		   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet,
				   ForesightConstants.HIPHEN, ForesightConstants.HIPHEN,ForesightConstants.HIPHEN);
		   setColumnWidthForReport(sheet);
		}
		setColumnWidthForReport(sheet);
		logger.info("Small Cell Signaling sheet created successfully.");
		return workBook;
	}
	private Workbook createSheetForSmallCellBearer(Workbook workBook, RANDetail smallCellSiteDetail) {
		logger.info("Going to create Small Cell Bearer sheet.");
		Sheet sheet = workBook.createSheet(ForesightConstants.SMALL_CELL_BEARER);
		int rowNum = 0;
		if(smallCellSiteDetail != null) {
		Map<String, XSSFCellStyle> style= createStylesForReport(workBook);
		String BEARER_DETAILS_KEYS[] = { ForesightConstants.BEARER_VLAN, ForesightConstants.BEARER_GATEWAY_IPV6_ADDRESS, ForesightConstants.BEARER_IP};
		sheet.createRow(rowNum).setHeightInPoints(40);
		   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.ORANGEHEADER), false, sheet, BEARER_DETAILS_KEYS);
		   rowNum++;
		   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet,
				   ForesightConstants.HIPHEN,ForesightConstants.HIPHEN,ForesightConstants.HIPHEN);
		   setColumnWidthForReport(sheet);
		}
		setColumnWidthForReport(sheet);
		logger.info("Small Cell Bearer sheet created successfully.");
		return workBook;
	}
	
	private Workbook createSheetForEmsDetails(Workbook workBook, RANDetail smallCellSiteDetail) {
		logger.info("Going to create EMS Details sheet.");
		Sheet sheet = workBook.createSheet(ForesightConstants.EMS_DETAIL);
		int rowNum = 0;
		if(smallCellSiteDetail != null) {
		Map<String, XSSFCellStyle> style= createStylesForReport(workBook);
		String EMS_DETAILS_KEYS[] = { "EMS ID",ForesightConstants.EMS_IPV6_ADDRESS, ForesightConstants.EMS_HOST_NAME};
		sheet.createRow(rowNum).setHeightInPoints(40);
		   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.ORANGEHEADER), false, sheet, EMS_DETAILS_KEYS);
		   rowNum++;
		   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet,
				   smallCellSiteDetail.getNetworkElement() != null && smallCellSiteDetail.getNetworkElement().getEmsServer() != null && smallCellSiteDetail.getNetworkElement().getEmsServer().getEmsName() != null ? smallCellSiteDetail.getNetworkElement().getEmsServer().getEmsName() : ForesightConstants.HIPHEN,
				   smallCellSiteDetail.getNetworkElement() != null && smallCellSiteDetail.getNetworkElement().getEmsServer() != null && smallCellSiteDetail.getNetworkElement().getEmsServer().getIp() != null ? smallCellSiteDetail.getNetworkElement().getEmsServer().getIp() : ForesightConstants.HIPHEN,
				   smallCellSiteDetail.getNetworkElement() != null && smallCellSiteDetail.getNetworkElement().getEmsServer() != null && smallCellSiteDetail.getNetworkElement().getEmsServer().getEmsName() != null ? smallCellSiteDetail.getNetworkElement().getEmsServer().getEmsName() : ForesightConstants.HIPHEN
					);
		   setColumnWidthForReport(sheet);
		}
		setColumnWidthForReport(sheet);
		logger.info("EMS Details sheet created successfully.");
		return workBook;
	}
	private Workbook createSheetForSmallCellInfo(Workbook workBook, RANDetail smallCellSiteDetail) {
		logger.info("Going to create SmallCell Info sheet.");
		
		int rowNum = 0;
		if(smallCellSiteDetail != null) {
		Sheet sheet = workBook.createSheet(ForesightConstants.SMALL_CELL_INFORMATION);
		Map<String, XSSFCellStyle> style= createStylesForReport(workBook);
		String SMALL_CELL_INFORMATION_KEYS[] = { "Small Cell Name", "eNodeB ID","ECGI","PCI","Tx Power","Azimuth",
				"Mech. Tilt", "Elec. Tilt","Total Tilt","Base Channel Frequency","Antenna Type","Antenna Make",
				"Antenna Gain","Antenna Model","Vertical Beamwidth","Horizontal Beamwidth"};
     		sheet.createRow(rowNum).setHeightInPoints(40);
		   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.ORANGEHEADER), false, sheet, SMALL_CELL_INFORMATION_KEYS);
		   rowNum++;
		   ReportUtils.createRow(sheet.createRow(rowNum), style.get(ForesightConstants.DATACELL_KEY), false, sheet,
				   smallCellSiteDetail.getNetworkElement() != null && smallCellSiteDetail.getNetworkElement().getNetworkElement() != null && smallCellSiteDetail.getNetworkElement().getNetworkElement().getNeName() != null ? smallCellSiteDetail.getNetworkElement().getNetworkElement().getNeName() : ForesightConstants.HIPHEN,
				   smallCellSiteDetail.getNetworkElement() != null && smallCellSiteDetail.getNetworkElement().getEnbid() != null ? smallCellSiteDetail.getNetworkElement().getEnbid() : ForesightConstants.HIPHEN,
				   smallCellSiteDetail.getNetworkElement() != null && smallCellSiteDetail.getNetworkElement().getEcgi() != null ? smallCellSiteDetail.getNetworkElement().getEcgi() : ForesightConstants.HIPHEN,
				   smallCellSiteDetail.getPci() != null ? smallCellSiteDetail.getPci() : ForesightConstants.HIPHEN,
				  smallCellSiteDetail.getTxPower() != null ? smallCellSiteDetail.getTxPower() : ForesightConstants.HIPHEN,
				  smallCellSiteDetail.getAzimuth() != null ? smallCellSiteDetail.getAzimuth() : ForesightConstants.HIPHEN,   
				  smallCellSiteDetail.getMechTilt() != null ? smallCellSiteDetail.getMechTilt() : ForesightConstants.HIPHEN,
				  smallCellSiteDetail.getElecTilt() != null ? smallCellSiteDetail.getElecTilt() : ForesightConstants.HIPHEN,   
				  ForesightConstants.HIPHEN,ForesightConstants.HIPHEN,
				  smallCellSiteDetail.getAntennaType() != null ? smallCellSiteDetail.getAntennaType()  : ForesightConstants.HIPHEN,
				  ForesightConstants.HIPHEN,
				  smallCellSiteDetail.getAntennaGain() != null ? smallCellSiteDetail.getAntennaGain()  : ForesightConstants.HIPHEN,
				  smallCellSiteDetail.getAntennaModel() != null ? smallCellSiteDetail.getAntennaModel()  : ForesightConstants.HIPHEN,
				  smallCellSiteDetail.getVerticalBeamWidth() != null ? smallCellSiteDetail.getVerticalBeamWidth()  : ForesightConstants.HIPHEN,
				  smallCellSiteDetail.getHorizontalBeamWidth() != null ? smallCellSiteDetail.getHorizontalBeamWidth()  : ForesightConstants.HIPHEN
				   );
		   setColumnWidthForReport(sheet);
		}		
		logger.info("SmallCell Info sheet created successfully.");
		return workBook;
	}
	  private void setColumnWidthForReport(Sheet sheet) {
		   logger.info("going to set Column Width for Sheet");
		   for (int i = 0; i <= 20; i++) {
			   try {
				   sheet.setColumnWidth(i, 4000);
			   } catch (Exception e) {
				   logger.error("error in setting Column Width for Small Cell Report number {}", i);
			   }
		   }
	   }
}
