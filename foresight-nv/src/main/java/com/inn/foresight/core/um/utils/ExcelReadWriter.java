package com.inn.foresight.core.um.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.um.utils.wrapper.BulkUserInformationWrapper;
import com.inn.product.security.wrapper.AccessLevelWrapper;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.geography.model.OtherGeography;
import com.inn.product.um.geography.model.SalesL1;
import com.inn.product.um.geography.model.SalesL2;
import com.inn.product.um.geography.model.SalesL3;
import com.inn.product.um.geography.model.SalesL4;
import com.inn.product.um.role.model.Role;
import com.inn.product.um.role.utils.wrapper.UserRoleGeographyDetails;
import com.inn.product.um.user.utils.UmConstants;
import com.inn.product.um.user.utils.wrapper.UserIntegrationWrapper;
import com.inn.product.um.user.utils.wrapper.UserPersonalDetailWrapper;

public class ExcelReadWriter {
	
	/** The logger. */
	private static Logger logger = LogManager.getLogger(ExcelReadWriter.class);
	private static final String SEPARATOR = ",";
	
	//static String headerData =ConfigUtils.getString(UmConstants.BULKUPLOAD_HEADER);
	public static final String BULKUPLOAD_WRITE_HEADER="BULKUPLOAD_WRITE_HEADER"; 
	
	static List<UserIntegrationWrapper> readData(InputStream inputStream) throws Exception {
		List<UserIntegrationWrapper> usr = new ArrayList<UserIntegrationWrapper>();
		try {
			Workbook wb = WorkbookFactory.create(inputStream);
			Sheet sheet = wb.getSheetAt(0);
			int rowStart = sheet.getFirstRowNum() + 1;
			int rowEnd = sheet.getLastRowNum();

			for (int i = rowStart; i <= rowEnd; i++) {

				Row row = sheet.getRow(i);
				UserPersonalDetailWrapper user = new UserPersonalDetailWrapper();

				if (row.getCell(0) != null) {
					user.setUserName(row.getCell(0).getStringCellValue());
				}

				if (row.getCell(1) != null) {
					user.setFirstName(row.getCell(1).getStringCellValue());
				}

				if (row.getCell(2) != null) {
					user.setMiddleName(row.getCell(2).getStringCellValue());
				}
				if (row.getCell(3) != null) {
					user.setLastName(row.getCell(3).getStringCellValue());
				}
				
				if (row.getCell(6) != null) {
					user.setEmail(row.getCell(6).getStringCellValue());
				}
				((UserIntegrationWrapper) usr).setUserPersonalInfo(user);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return usr;
	}

	
	public static String writeData(List<BulkUserInformationWrapper> usersList,String filePath) throws JSONException {
		logger.info("Inside writeData");
		 String date = new SimpleDateFormat(UmConstants.YYYY_MM_DD_HH_MM).format(new Date());
		 String path="CREATED_FILE_"+date+".xlsx";
		 String targetPath=System.getProperty("catalina.base")+"/temp/"+path;
		XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Created_User");
		FileOutputStream out=null;
		int rowNum = 1;
		int headerCount = 0;
		List<String> headerList = Arrays.asList(ConfigUtils.getString(BULKUPLOAD_WRITE_HEADER).split(","));
		Row header = sheet.createRow(0);
		Row row =null;
		for (String head : headerList) {
			header.createCell(headerCount++).setCellValue(head);
		}
		header.createCell(headerCount).setCellValue("Status");
		for (BulkUserInformationWrapper userIntegrationWrapper2 : usersList) {
			UserPersonalDetailWrapper dataNew = userIntegrationWrapper2.getUserPersonalInfo();
			Map<String, List<UserRoleGeographyDetails>> userRoleGeography = userIntegrationWrapper2.getRoleMap();
			row = sheet.createRow(rowNum);
			row.createCell(0).setCellValue(dataNew.getFirstName());
			row.createCell(1).setCellValue(dataNew.getMiddleName());
			row.createCell(2).setCellValue(dataNew.getLastName());
			
			
			if(!userIntegrationWrapper2.getRoleMap().isEmpty()) {
			for (String SPs : userIntegrationWrapper2.getRoleMap().keySet()) {
				logger.info("key name : {} ", SPs);
				List<UserRoleGeographyDetails> list = userRoleGeography.get(SPs);
				logger.info("list size : {} ", list.size());
				logger.info("list : {} ", list);
				
				for (UserRoleGeographyDetails userRoleGeographyDetails : list) {
					logger.info("userRoleGeographyDetails : {} ", userRoleGeographyDetails);
					if (dataNew != null) {
						row = sheet.createRow(rowNum);
						row.createCell(0).setCellValue(dataNew.getFirstName());
						row.createCell(1).setCellValue(dataNew.getMiddleName());
						row.createCell(2).setCellValue(dataNew.getLastName());
						if ("true".equalsIgnoreCase(ConfigUtils.getString("BULKUPLOAD_ACCESS_LEVEL"))) {
							writeDataWithAccessLevel(userIntegrationWrapper2, dataNew, SPs, userRoleGeographyDetails,
									row);
							row.createCell(4).setCellValue(userIntegrationWrapper2.getOrgName());
							row.createCell(5).setCellValue(userIntegrationWrapper2.getAuthtype());
							row.createCell(6).setCellValue(dataNew.getEmail());
							row.createCell(7).setCellValue(dataNew.getUserName());
							
							row.createCell(8).setCellValue(dataNew.getContactNumber());
							if (dataNew.getUserAddress() != null && dataNew.getUserAddress().getAddressLine1() != null
									&& !dataNew.getUserAddress().getAddressLine1().isEmpty()) {
								row.createCell(9).setCellValue(dataNew.getUserAddress().getAddressLine1());
							}
							row.createCell(20).setCellValue(userIntegrationWrapper2.getMessage());
						} else {
							writeDataIntoFile(userIntegrationWrapper2, dataNew, SPs, userRoleGeographyDetails, row);
							
						}
						rowNum++;
					}
				}
				
			}
			}else {
				
			row.createCell(4).setCellValue(userIntegrationWrapper2.getOrgName());
			row.createCell(5).setCellValue(userIntegrationWrapper2.getAuthtype());
			row.createCell(6).setCellValue(dataNew.getEmail());
			row.createCell(7).setCellValue(dataNew.getUserName());
			row.createCell(8).setCellValue(dataNew.getContactNumber());
			row.createCell(19).setCellValue(userIntegrationWrapper2.getMessage());
			
			}
		}
		try {
			if (filePath != null) {
				out = writeFileIntoFolder(filePath, path);
			} else {
				out = new FileOutputStream(new File(targetPath));
			}
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			logger.error("Error while  creating  user request : {}", Utils.getStackTrace(e));
		}

		return path;
	}


	public static FileOutputStream writeFileIntoFolder(String filePath, String path) throws FileNotFoundException {
		FileOutputStream out;
		String dirPath = new File(new File(filePath).getParent()).getParent() + "/";
		File directory = new File(dirPath+"/CREATED_FILES_"+new SimpleDateFormat("ddMMYYYY").format(new Date()));
		if (!directory.exists()) {
			directory.mkdir();
			out = new FileOutputStream(dirPath +"/"+path);
		} else {
			out = new FileOutputStream(dirPath +"/"+path);
		}
		return out;
	}


	private static void writeDataIntoFile(BulkUserInformationWrapper userIntegrationWrapper2,
			UserPersonalDetailWrapper dataNew, String SPs, UserRoleGeographyDetails userRoleGeographyDetails, Row row) {
		List<GeographyL1> l1List;
		List<GeographyL2> l2List;
		List<GeographyL3> l3List;
		List<GeographyL4> l4List;
		
		List<SalesL1> salesL1List;
		List<SalesL2> salesL2List;
		List<SalesL3> salesL3List;
		List<SalesL4> salesL4List;
		
		
		List<OtherGeography> otherGeographies;
		Role role;
		if (dataNew.getOrgAuthenticationType() != null
				&& dataNew.getOrgAuthenticationType().getOrganization() != null
				&& dataNew.getOrgAuthenticationType().getOrganization().getName() != null
				&& !dataNew.getOrgAuthenticationType().getOrganization().getName().isEmpty()) {
			row.createCell(3)
					.setCellValue(dataNew.getOrgAuthenticationType().getOrganization().getName());
		}
		if (dataNew.getOrgAuthenticationType() != null) {
			row.createCell(4)
					.setCellValue(dataNew.getOrgAuthenticationType().getAuthenticationtype());
		}
		row.createCell(5).setCellValue(dataNew.getEmail());
		row.createCell(6).setCellValue(dataNew.getUserName());
		
		row.createCell(7).setCellValue(dataNew.getContactNumber());

		if (dataNew.getUserAddress() != null && dataNew.getUserAddress().getAddressLine1() != null
				&& !dataNew.getUserAddress().getAddressLine1().isEmpty()) {
			row.createCell(8).setCellValue(dataNew.getUserAddress().getAddressLine1());
		}
		
		

		role = userRoleGeographyDetails.getRole();
		logger.info("writing role : {} ", role);
		//if (role != null && role.getRoleName() != null && !role.getRoleName().isEmpty()) {

		if(role.getRoleName()!=null&&!role.getRoleName().isEmpty()) {
		if ("SP_FORESIGHT".equalsIgnoreCase(SPs)) {
			row.createCell(9).setCellValue("FORESIGHT ROLE");
		} else if ("SP_SITEFORGE".equalsIgnoreCase(SPs)) {
			row.createCell(9).setCellValue("SITEFORGE ROLE");
		}
		}

		
			row.createCell(10).setCellValue(role.getWorkSpace().getName());
			row.createCell(11).setCellValue(role.getTeam().getName());

			row.createCell(12).setCellValue(role.getLevelType());
			
			if(userRoleGeographyDetails.getRole().getLevelType()!=null&&!userRoleGeographyDetails.getRole().getLevelType().isEmpty()) {
			if (userRoleGeographyDetails.getRole().getLevelType().equalsIgnoreCase("L1")) {
				row.createCell(12).setCellValue(ConfigUtils.getString("BULKUPLOAD_ZONE").trim());
			}
			if (userRoleGeographyDetails.getRole().getLevelType().equalsIgnoreCase("SALES_L1")) {
				row.createCell(12).setCellValue(ConfigUtils.getString("BULKUPLOAD_SALES_REGION").trim());
			}
			if (userRoleGeographyDetails.getRole().getLevelType().equalsIgnoreCase("L2")) {
				row.createCell(12).setCellValue(ConfigUtils.getString("BULKUPLOAD_CIRCLE").trim());
			}
			if (userRoleGeographyDetails.getRole().getLevelType().equalsIgnoreCase("SALES_L2")) {
				row.createCell(12).setCellValue(ConfigUtils.getString("BULKUPLOAD_SALES_CLUSTER").trim());
			}
			if (userRoleGeographyDetails.getRole().getLevelType().equalsIgnoreCase("L3")||userRoleGeographyDetails.getRole().getLevelType().equalsIgnoreCase("SALES_L3")) {
				row.createCell(12).setCellValue(ConfigUtils.getString("BULKUPLOAD_CITY").trim());
			}
			if (userRoleGeographyDetails.getRole().getLevelType().equalsIgnoreCase("L4")||userRoleGeographyDetails.getRole().getLevelType().equalsIgnoreCase("SALES_L3")) {
				row.createCell(12)
						.setCellValue(ConfigUtils.getString("BULKUPLOAD_NETWORK_CLUSTER").trim());
			}
			
		   }

			
			row.createCell(13).setCellValue(userRoleGeographyDetails.getRole().getRoleName());

			
			if(role!=null&&role.getWorkSpace()!=null&&ForesightConstants.SALES.equalsIgnoreCase(role.getGeoType()))
			{

				if (userRoleGeographyDetails.getSalesl1() != null
						&& !userRoleGeographyDetails.getSalesl1().isEmpty()) {
					salesL1List = userRoleGeographyDetails.getSalesl1();
					row.createCell(14).setCellValue(salesL1List.get(0).getName());
				} else {
					row.createCell(14).setCellValue(userIntegrationWrapper2.getL1());
				}

				if (userRoleGeographyDetails.getSalesl2() != null
						&& !userRoleGeographyDetails.getSalesl2().isEmpty()) {
					salesL2List = userRoleGeographyDetails.getSalesl2();
					row.createCell(15).setCellValue(salesL2List.get(0).getName());
				} else {
					row.createCell(15).setCellValue(userIntegrationWrapper2.getL2());
				}

				if (userRoleGeographyDetails.getSalesl3() != null
						&& !userRoleGeographyDetails.getSalesl3().isEmpty()) {
					salesL3List = userRoleGeographyDetails.getSalesl3();
					row.createCell(16).setCellValue(salesL3List.get(0).getName());
				} else {
					row.createCell(16).setCellValue(userIntegrationWrapper2.getL3());
				}

				if (userRoleGeographyDetails.getSalesl4() != null
						&& !userRoleGeographyDetails.getSalesl4().isEmpty()) {
					salesL4List = userRoleGeographyDetails.getSalesl4();
					row.createCell(17).setCellValue(salesL4List.get(0).getName());
				} else {
					row.createCell(17).setCellValue(userIntegrationWrapper2.getL4());
				}
				if(role.getLevelType().equalsIgnoreCase(ForesightConstants.OTHER)||role.getLevelType().equalsIgnoreCase(ForesightConstants.UNIT)) {
				if (userRoleGeographyDetails.getOtherGeography() != null
						&& !userRoleGeographyDetails.getOtherGeography().isEmpty()) {
					otherGeographies = userRoleGeographyDetails.getOtherGeography();
					row.createCell(18).setCellValue(otherGeographies.get(0).getName());
				} else {
					row.createCell(18).setCellValue(userIntegrationWrapper2.getOtherGeography());
				}
				}
			
			}else {
			if (userRoleGeographyDetails.getGeographyL1() != null
					&& !userRoleGeographyDetails.getGeographyL1().isEmpty()) {
				l1List = userRoleGeographyDetails.getGeographyL1();
				row.createCell(14).setCellValue(l1List.get(0).getName());
			} else {
				row.createCell(14).setCellValue(userIntegrationWrapper2.getL1());
			}

			if (userRoleGeographyDetails.getGeographyL2() != null
					&& !userRoleGeographyDetails.getGeographyL2().isEmpty()) {
				l2List = userRoleGeographyDetails.getGeographyL2();
				row.createCell(15).setCellValue(l2List.get(0).getName());
			} else {
				row.createCell(15).setCellValue(userIntegrationWrapper2.getL2());
			}

			if (userRoleGeographyDetails.getGeographyL3() != null
					&& !userRoleGeographyDetails.getGeographyL3().isEmpty()) {
				l3List = userRoleGeographyDetails.getGeographyL3();
				row.createCell(16).setCellValue(l3List.get(0).getName());
			} else {
				row.createCell(16).setCellValue(userIntegrationWrapper2.getL3());
			}

			if (userRoleGeographyDetails.getGeographyL4() != null
					&& !userRoleGeographyDetails.getGeographyL4().isEmpty()) {
				l4List = userRoleGeographyDetails.getGeographyL4();
				row.createCell(17).setCellValue(l4List.get(0).getName());
			} else {
				row.createCell(17).setCellValue(userIntegrationWrapper2.getL4());
			}
			
			if(role.getLevelType().equalsIgnoreCase(ForesightConstants.OTHER)||role.getLevelType().equalsIgnoreCase(ForesightConstants.UNIT)) {
			if (userRoleGeographyDetails.getOtherGeography() != null
					&& !userRoleGeographyDetails.getOtherGeography().isEmpty()) {
				otherGeographies = userRoleGeographyDetails.getOtherGeography();
				row.createCell(18).setCellValue(otherGeographies.get(0).getName());
			} else {
				row.createCell(18).setCellValue(userIntegrationWrapper2.getOtherGeography());
			}
			}
			
	} 
			
			
			
			
			
		
		row.createCell(19).setCellValue(userIntegrationWrapper2.getMessage());
	}


	private static void writeDataWithAccessLevel(BulkUserInformationWrapper userIntegrationWrapper2,
			UserPersonalDetailWrapper dataNew, String SPs, UserRoleGeographyDetails userRoleGeographyDetails, Row row) {
		List<GeographyL1> l1List;
		List<GeographyL2> l2List;
		List<GeographyL3> l3List;
		List<GeographyL4> l4List;
		List<SalesL1> salesL1List;
		List<SalesL2> salesL2List;
		List<SalesL3> salesL3List;
		List<SalesL4> salesL4List;
		List<OtherGeography> otherGeographies;
		Role role;
		row.createCell(3).setCellValue(getBulkAccessLevel(dataNew.getAccessLevel(),SPs));
		if (dataNew.getOrgAuthenticationType() != null
				&& dataNew.getOrgAuthenticationType().getOrganization() != null
				&& dataNew.getOrgAuthenticationType().getOrganization().getName() != null
				&& !dataNew.getOrgAuthenticationType().getOrganization().getName().isEmpty()) {
			row.createCell(4)
					.setCellValue(dataNew.getOrgAuthenticationType().getOrganization().getName());
		}
		if (dataNew.getOrgAuthenticationType() != null) {
			row.createCell(5)
					.setCellValue(dataNew.getOrgAuthenticationType().getAuthenticationtype());
		}
		row.createCell(6).setCellValue(dataNew.getEmail());
		row.createCell(7).setCellValue(dataNew.getUserName());
		
		row.createCell(8).setCellValue(dataNew.getContactNumber());
		if (dataNew.getUserAddress() != null && dataNew.getUserAddress().getAddressLine1() != null
				&& !dataNew.getUserAddress().getAddressLine1().isEmpty()) {
			row.createCell(9).setCellValue(dataNew.getUserAddress().getAddressLine1());
		}
		role = userRoleGeographyDetails.getRole();
		logger.info("writing role : {} ", role);
		//if (role != null && role.getRoleName() != null && !role.getRoleName().isEmpty()) {

		if(role.getRoleName()!=null&&!role.getRoleName().isEmpty()) {
			if ("SP_FORESIGHT".equalsIgnoreCase(SPs)) {
				row.createCell(10).setCellValue("FORESIGHT ROLE");
			} else if ("SP_SITEFORGE".equalsIgnoreCase(SPs)) {
				row.createCell(10).setCellValue("SITEFORGE ROLE");
			}
			}
		    
			row.createCell(11).setCellValue(role.getWorkSpace().getName());
			row.createCell(12).setCellValue(role.getTeam().getName());

			row.createCell(13).setCellValue(role.getLevelType());
			
			if(userRoleGeographyDetails.getRole().getLevelType()!=null) {
			if (userRoleGeographyDetails.getRole().getLevelType().equalsIgnoreCase("L1")) {
				row.createCell(13).setCellValue(ConfigUtils.getString("BULKUPLOAD_ZONE").trim());
			}
			if (userRoleGeographyDetails.getRole().getLevelType().equalsIgnoreCase("SALES_L1")) {
				row.createCell(12).setCellValue(ConfigUtils.getString("BULKUPLOAD_SALES_REGION").trim());
			}
			if (userRoleGeographyDetails.getRole().getLevelType().equalsIgnoreCase("L2")) {
				row.createCell(13).setCellValue(ConfigUtils.getString("BULKUPLOAD_CIRCLE").trim());
			}
			if (userRoleGeographyDetails.getRole().getLevelType().equalsIgnoreCase("SALES_L2")) {
				row.createCell(12).setCellValue(ConfigUtils.getString("BULKUPLOAD_SALES_CLUSTER").trim());
			}
			if (userRoleGeographyDetails.getRole().getLevelType().equalsIgnoreCase("L3")||userRoleGeographyDetails.getRole().getLevelType().equalsIgnoreCase("SALES_L3")) {
				row.createCell(13).setCellValue(ConfigUtils.getString("BULKUPLOAD_CITY").trim());
			}
			if (userRoleGeographyDetails.getRole().getLevelType().equalsIgnoreCase("L4")||userRoleGeographyDetails.getRole().getLevelType().equalsIgnoreCase("SALES_L4")) {
				row.createCell(13)
						.setCellValue(ConfigUtils.getString("BULKUPLOAD_NETWORK_CLUSTER").trim());
			}
			
			
			}
			
			row.createCell(14).setCellValue(userRoleGeographyDetails.getRole().getRoleName());

			
			
			if(!userIntegrationWrapper2.getLevel().equalsIgnoreCase("NHQ")&&!userIntegrationWrapper2.getLevel().equalsIgnoreCase("SALES_NHQ")) {
				
				if(role!=null&&role.getWorkSpace()!=null&&ForesightConstants.SALES.equalsIgnoreCase(role.getGeoType()))
				{

					if (userRoleGeographyDetails.getSalesl1() != null
							&& !userRoleGeographyDetails.getSalesl1().isEmpty()) {
						salesL1List = userRoleGeographyDetails.getSalesl1();
						row.createCell(15).setCellValue(salesL1List.get(0).getName());
					} else {
						row.createCell(15).setCellValue(userIntegrationWrapper2.getL1());
					}

					if (userRoleGeographyDetails.getSalesl2() != null
							&& !userRoleGeographyDetails.getSalesl2().isEmpty()) {
						salesL2List = userRoleGeographyDetails.getSalesl2();
						row.createCell(16).setCellValue(salesL2List.get(0).getName());
					} else {
						row.createCell(16).setCellValue(userIntegrationWrapper2.getL2());
					}

					if (userRoleGeographyDetails.getSalesl3() != null
							&& !userRoleGeographyDetails.getSalesl3().isEmpty()) {
						salesL3List = userRoleGeographyDetails.getSalesl3();
						row.createCell(17).setCellValue(salesL3List.get(0).getName());
					} else {
						row.createCell(17).setCellValue(userIntegrationWrapper2.getL3());
					}

					if (userRoleGeographyDetails.getSalesl4() != null
							&& !userRoleGeographyDetails.getSalesl4().isEmpty()) {
						salesL4List = userRoleGeographyDetails.getSalesl4();
						row.createCell(18).setCellValue(salesL4List.get(0).getName());
					} else {
						row.createCell(18).setCellValue(userIntegrationWrapper2.getL4());
					}
				
				}else {
				
			if (userRoleGeographyDetails.getGeographyL1() != null
					&& !userRoleGeographyDetails.getGeographyL1().isEmpty()) {
				l1List = userRoleGeographyDetails.getGeographyL1();
				row.createCell(15).setCellValue(l1List.get(0).getName());
			} else {
				row.createCell(15).setCellValue(userIntegrationWrapper2.getL1());
			}

			if (userRoleGeographyDetails.getGeographyL2() != null
					&& !userRoleGeographyDetails.getGeographyL2().isEmpty()) {
				l2List = userRoleGeographyDetails.getGeographyL2();
				row.createCell(16).setCellValue(l2List.get(0).getName());
			} else {
				row.createCell(16).setCellValue(userIntegrationWrapper2.getL2());
			}

			if (userRoleGeographyDetails.getGeographyL3() != null
					&& !userRoleGeographyDetails.getGeographyL3().isEmpty()) {
				l3List = userRoleGeographyDetails.getGeographyL3();
				row.createCell(17).setCellValue(l3List.get(0).getName());
			} else {
				row.createCell(17).setCellValue(userIntegrationWrapper2.getL3());
			}

			if (userRoleGeographyDetails.getGeographyL4() != null
					&& !userRoleGeographyDetails.getGeographyL4().isEmpty()) {
				l4List = userRoleGeographyDetails.getGeographyL4();
				row.createCell(18).setCellValue(l4List.get(0).getName());
			} else {
				row.createCell(18).setCellValue(userIntegrationWrapper2.getL4());
			}
			
			}
			if (userRoleGeographyDetails.getOtherGeography() != null
					&& !userRoleGeographyDetails.getOtherGeography().isEmpty()) {
				otherGeographies = userRoleGeographyDetails.getOtherGeography();
				row.createCell(19).setCellValue(otherGeographies.get(0).getName());
			} else {
				row.createCell(19).setCellValue(userIntegrationWrapper2.getOtherGeography());
			}
			
	}
			
		//}
		row.createCell(20).setCellValue(userIntegrationWrapper2.getMessage());
	}

	public static List<Map<String, String>> getExcelList(InputStream inputStream,String headerData) throws Exception {
		logger.info("Inside getExcelList");
		Workbook wb = getExcelFile(inputStream);
		List<Map<String, String>> dataList = new ArrayList<Map<String, String>>();
		Map<Integer, String> header = new HashMap<Integer, String>();
		Map<String, String> dataMap = null;
		String fHeader="";
		//String headerData =ConfigUtils.getString(UmConstants.BULKUPLOAD_HEADER);
		logger.info("header : {} ",headerData);
		StringBuilder sb = new StringBuilder();
		String[] arg = headerData.split(",");
		for (int i = 0; i < arg.length; i++) {
			if (!arg[i].equals(""))
				header.put(i, arg[i]);
		}

		for (Row row : wb.getSheetAt(0)) {
			logger.info("ROW NO :  {} ",row.getRowNum());
			logger.info("========NEW ROW=======");
			dataMap = new HashMap<String, String>();
			if (row.getRowNum() == 0) {
				
				for (Cell cell : row) {
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						sb.append(cell.getRichStringCellValue().getString());
						sb.append(SEPARATOR);
					}
				}
				   fHeader = sb.toString();
				  //Remove last separator
				  if (fHeader.endsWith(SEPARATOR)) {
				   fHeader = fHeader.substring(0, fHeader.length() - SEPARATOR.length());
				  }
				logger.info("HEADER DATA ======"+dataMap);
			}
			dataMap.put("header",fHeader.toString());
			for (Cell cell : row) {
				logger.info("header====>{}",fHeader);
				
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
				if (header.get(cell.getColumnIndex()) != null) {
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						dataMap.put(header.get(cell.getColumnIndex()), cell.getRichStringCellValue().getString());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						    cell.setCellType(Cell.CELL_TYPE_STRING);
							dataMap.put(header.get(cell.getColumnIndex()), cell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_FORMULA:
						dataMap.put(header.get(cell.getColumnIndex()), cell.getCellFormula());
						break;
					}
				}
			}
			}
			logger.info("dataMap : "+dataMap);
			if(!dataMap.isEmpty()&&dataMap!=null&&dataMap.containsKey("First Name")||dataMap.containsKey("User Name"))
			dataList.add(dataMap);
		}
		return dataList;
	}

	public static Workbook getExcelFile(InputStream inputStream) throws Exception {
		try {
			Workbook wb = WorkbookFactory.create(inputStream);
			return wb;
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException();
		} catch (Exception ex) {
			throw new Exception();
		}
	}
	
	public static String getBulkAccessLevel(Map<String, AccessLevelWrapper> accessLevel, String SPs) {
		logger.info("Inside getBulkAccessLevel");
		AccessLevelWrapper accessLevelWrapper = new AccessLevelWrapper();
		StringBuilder access = new StringBuilder();
		String accesssType = ",";
		String prefix = "";

		if (accessLevel != null && !accessLevel.isEmpty()) {

			if ("SP_FORESIGHT".equalsIgnoreCase(SPs)) {
				accessLevelWrapper = accessLevel.get("SP_FORESIGHT");
				if (accessLevelWrapper.getWeb_internet()) {
					access.append("web internet").append(accesssType);
				}
				if (accessLevelWrapper.getWeb_intranet()) {
					access.append("web intranet").append(accesssType);
				}
				if (accessLevelWrapper.getMobile_internet()) {
					access.append("mobile internet").append(accesssType);
				}
				if (accessLevelWrapper.getMobile_intranet()) {
					access.append("mobile intranet").append(accesssType);
				}
			} else {

				accessLevelWrapper = accessLevel.get("SP_SITEFORGE");
				if (accessLevelWrapper.getWeb_internet()) {
					access.append("web internet").append(accesssType);
				}
				if (accessLevelWrapper.getWeb_intranet()) {
					access.append("web intranet").append(accesssType);
				}
				if (accessLevelWrapper.getMobile_internet()) {
					access.append("mobile internet").append(accesssType);
				}
				if (accessLevelWrapper.getMobile_intranet()) {
					access.append("mobile intranet").append(accesssType);
				}
			}
		}
		if (!access.toString().isEmpty())
			access.deleteCharAt(access.length() - 1);
		return access.toString();
	}

}
