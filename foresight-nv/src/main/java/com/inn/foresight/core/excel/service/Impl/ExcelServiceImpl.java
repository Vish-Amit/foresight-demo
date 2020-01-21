package com.inn.foresight.core.excel.service.Impl;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.excel.dao.IExcelDao;
import com.inn.foresight.core.excel.model.ExcelData;
import com.inn.foresight.core.excel.service.IExcelService;

@Service("ExcelServiceImpl")
public class ExcelServiceImpl extends AbstractService<Integer, ExcelData> implements IExcelService {

	

	@Autowired
	private IExcelDao iexceldao;
	
	private ExcelServiceImpl excelserviceimpl;
	
	
	private Logger logger = LogManager.getLogger(ExcelServiceImpl.class);

	/*
	 * public static void main(String[] args) { ExcelServiceImpl l=new
	 * ExcelServiceImpl(); l.excelRW();
	 * 
	 * }
	 */
	@Transactional
	@Override
	public void excelRW() {
		try {

			Workbook wb = WorkbookFactory.create(new File("/home/ist/Downloads/GroupCenter.xlsx"));
			Sheet sheet = wb.getSheetAt(0);

			int rowStart = sheet.getFirstRowNum();
			int rowEnd = sheet.getLastRowNum();

			ExcelData dd = new ExcelData();

			for (int i = rowStart + 1; i < rowEnd; i++) {
				Row row = sheet.getRow(i);

				for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
					Cell cell = row.getCell(j);

					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:
						System.out.print(cell.getNumericCellValue() + " \t\t ");
						break;

					case Cell.CELL_TYPE_STRING:
						System.out.print(cell.getStringCellValue() + " \t\t ");
						break;
					}

					switch (j) {

					case 0:
						dd.setGn(cell.getStringCellValue());
						break;

					case 1:
						dd.setLongitude(cell.getNumericCellValue());
						break;

					case 2:
						dd.setLatitude(cell.getNumericCellValue());
						break;

					case 3:
						dd.setRowkey(cell.getNumericCellValue());
						break;

					case 4:
						dd.setGeography(cell.getNumericCellValue());
						break;
					}
					dd.setCreationTime(new Date());
					dd.setModificationTime(new Date());
				}
				System.out.println("\n");

				iexceldao.create(dd);

				/* System.out.println(dd); */

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	@Override
	public void writeToExcel() {

		HSSFWorkbook workbook = new HSSFWorkbook();

		HSSFSheet sheet = workbook.createSheet("sheet1");
		int rownum = 0;
		List<ExcelData> ll = iexceldao.writeToExcel();
		Row header = sheet.createRow(rownum++);

		CellStyle style1 = workbook.createCellStyle();
		style1.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		style1.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style1.setBorderBottom(CellStyle.BORDER_THIN);
		style1.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style1.setBorderLeft(CellStyle.BORDER_THIN);
		style1.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style1.setBorderRight(CellStyle.BORDER_THIN);
		style1.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style1.setBorderTop(CellStyle.BORDER_THIN);
		style1.setTopBorderColor(IndexedColors.BLACK.getIndex());
       
		
		Cell cell1 = header.createCell(0);
		cell1.setCellValue("Uid");
		sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 1));
		cell1.setCellStyle(style1);

		Cell cell2 = header.createCell(2);
		cell2.setCellValue("GN");
		sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 2, 3));
		cell2.setCellStyle(style1);

		Cell cell3 = header.createCell(4);
		cell3.setCellValue("Longitude");
		sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 4, 5));
		cell3.setCellStyle(style1);

		Cell cell4 = header.createCell(6);
		cell4.setCellValue("Latitude");
		sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 6, 7));
		cell4.setCellStyle(style1);

		Cell cell5 = header.createCell(8);
		cell5.setCellValue("Rowkey");
		sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 8, 9));
		cell5.setCellStyle(style1);

		Cell cell6 = header.createCell(10);
		cell6.setCellValue("GeoGraphy");
		sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 10, 11));
		cell6.setCellStyle(style1);

		Cell cell7 = header.createCell(12);
		cell7.setCellValue("Creation Time");
		sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 12, 13));
		cell7.setCellStyle(style1);
		sheet.autoSizeColumn(2);
		Cell cell8 = header.createCell(14);
		cell8.setCellValue("Modification Time");
		sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 14, 15));
		cell8.setCellStyle(style1);

		
		Font font = workbook.createFont();
		font.setFontName("Arial");
		font.setFontHeightInPoints((short)20);
		font.setStrikeout(true);
		font.setColor((short) 24);
	    font.setBoldweight ( Font.BOLDWEIGHT_BOLD );

		
		
	      style1.setFont(font);
		   
		for (ExcelData excelData : ll) {

			Row row = sheet.createRow(rownum);

			for (int i = 0, j = 0; i < 16; i = i + 2, j++) {
				Cell cell = row.createCell(i);
				
				sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rownum, rownum, i, i + 1));
				switch (i == 0 ? i : (i - j)) {

				case 0:

					cell.setCellValue(excelData.getId());
			
					break;

				case 1:
					cell.setCellValue(excelData.getGn());
					break;

				case 2:
					cell.setCellValue(excelData.getLongitude());
					break;

				case 3:
					cell.setCellValue(excelData.getLatitude());

					break;

				case 4:
					cell.setCellValue(excelData.getRowkey());

					break;

				case 5:
					cell.setCellValue(excelData.getGeography());

					break;

				case 6:

					String pattern = "yyyy-MM-dd HH-mm-ss";
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

					String date = simpleDateFormat.format(excelData.getCreationTime());

					cell.setCellValue(date);

					break;

				case 7:
					String pattern1 = "yyyy-MM-dd HH-mm-ss";
					SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(pattern1);

					String date1 = simpleDateFormat1.format(excelData.getModificationTime());
					cell.setCellValue(date1);

					break;
				}
				logger.debug("inserted " + excelData.getId());

			}
			rownum++;
		}

		try {

			
sheet.setDefaultRowHeight((short)22);
			FileOutputStream out = new FileOutputStream(new File("/home/ist/Downloads/writeexcel.xlsx"));
			
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


// -------------------------------------------------------------------------------------------------------------

	@Override
	public void criteriaDemo(){
		
		
		List<ExcelData> myGNList = iexceldao.criteriaDemo();
		
		for(ExcelData ll: myGNList)
		{
			logger.debug("GN :  " + ll.getGn());
	
		}
		

		
		
	}




















}