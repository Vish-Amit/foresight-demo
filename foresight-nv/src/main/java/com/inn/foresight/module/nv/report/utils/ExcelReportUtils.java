package com.inn.foresight.module.nv.report.utils;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.inn.core.generic.exceptions.application.RestException;


public class ExcelReportUtils {
	
	private static Logger logger = LogManager.getLogger(ExcelReportUtils.class);
	

	private ExcelReportUtils() {
		super();
	}
	
	public static String getStringForExcelCell(String str, String replacement) {
		if (ReportUtil.isValidString(str)) {
			return str;
		}
		return replacement;
	}
	public static XSSFFont getFontForExcel(XSSFWorkbook wb, boolean isBold,
			Integer height) {
		XSSFFont font = wb.createFont();
		font.setBold(isBold);
		font.setFontHeight(height);
		return font;
	}

	public static XSSFFont getFontForHeaderExcel(SXSSFWorkbook wb,
			boolean isBold, Integer height) {
		XSSFFont font = (XSSFFont) wb.createFont();
		font.setBold(isBold);
		font.setFontHeight(height);
		font.setColor(IndexedColors.WHITE.index);
		return font;
	}

	public static XSSFFont getFontForExcel(SXSSFWorkbook wb, boolean isBold,
			Integer height) {
		XSSFFont font = (XSSFFont) wb.createFont();
		font.setBold(isBold);
		font.setFontHeight(height);
		font.setColor(IndexedColors.WHITE.getIndex());
		return font;
	}
	public static XSSFFont getFontForExcel(SXSSFWorkbook wb, boolean isBold,
			Integer height,short color) {
		XSSFFont font = (XSSFFont) wb.createFont();
		font.setBold(isBold);
		font.setFontHeight(height);
		font.setColor(color);
		return font;
	}
	public static Font getFontForExcel(Workbook wb) {
		Font font = wb.createFont();
		font.setBold(true);
		return font;
	}

	public static HSSFFont getFontForRichString(Workbook wb) {
		HSSFFont font = (HSSFFont) wb.createFont();
		font.setBold(true);
		return font;
	}

	public static String getStringForExcelCell(Object obj, String replacement) {
		if (obj != null) {
			String actualString = (String) obj;
			if (ReportUtil.isValidString(actualString))
				return actualString;
		}
		return replacement;
	}
	
	
	public static XSSFCellStyle getCellStyle(Workbook wb, Color bordercolor,
			Color cellForeground, BorderStyle borderStyle, XSSFFont font,
			boolean left, boolean right, boolean top, boolean bottom,
			HorizontalAlignment alignCenter) {
		XSSFColor clr = new XSSFColor(bordercolor, null);
		XSSFCellStyle style = (XSSFCellStyle) wb.createCellStyle();
		if (left) {
			style.setLeftBorderColor(clr);
			style.setBorderLeft(borderStyle);
		}
		if (right) {
			style.setBorderRight(borderStyle);
			style.setRightBorderColor(clr);
		}
		if (top) {
			style.setBorderTop(borderStyle);
			style.setTopBorderColor(clr);
		}
		if (bottom) {
			style.setBorderBottom(borderStyle);
			style.setBottomBorderColor(clr);
		}
		style.setAlignment(alignCenter);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		if (cellForeground != null) {
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFColor foreClr = new XSSFColor(cellForeground, null);
			style.setFillForegroundColor(foreClr);
		}
		if (font != null) {
			style.setFont(font);
		}
		style.setWrapText(true);
		return style;
	}
	
	
	public static XSSFRow addCloumnToRow(XSSFRow rowOverall, int colNum, String cellValue, XSSFCellStyle style) {
		XSSFCell cell = rowOverall.createCell(colNum);
		Long checkData = 0L;
		try {
			checkData = Long.parseLong(cellValue);
			cell.setCellValue(checkData);
		} catch (NumberFormatException e) {
			cell.setCellValue(cellValue);
		}
		cell.setCellStyle(style);
		return rowOverall;
	}
	
	public static File exportExcel(String excelFileName, XSSFWorkbook wb)
			throws IOException {
		try {
			File file = new File(excelFileName);
			FileOutputStream fileOut = new FileOutputStream(file);
			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
			return file;
		} catch (Exception e) {
			throw new RestException("File Not Generated");
		}
		
	}
	public static XSSFRow getNewRow(XSSFSheet sheet, Integer index, Integer cellHeight) {
		XSSFRow row = sheet.createRow(index);
		if (cellHeight != null)
			row.setHeightInPoints(cellHeight);
		return row;
	}
	
	public static XSSFFont getFontForExcelForHeader(XSSFWorkbook wb, boolean isBold,
			Integer height) {
		XSSFFont font = wb.createFont();
		font.setBold(isBold);
		font.setFontName(ReportConstants.CALIBRI);
		font.setFontHeight(height);
		return font;
	}
	
	public static int createRow(Row row, CellStyle cellStyle,
			 String... values) {
		int i = 0;
		for (i = 0; i < values.length; i++) {
			try {
				Cell cell = row.createCell(i);
				writeValue(cell, values[i]);
				cell.setCellStyle(cellStyle);
			} catch (Exception e) {
				logger.error("Exception inside method createRow {} ",e.getMessage());
			}
		}
		return i;
	}
	
	private static void writeValue(Cell cell, Object object) {
		if (object == null) {
			cell.setCellValue(ReportConstants.HIPHEN);
		}
		else if (object instanceof String) {
			cell.setCellValue((String) object);
		}
		else if (object instanceof Double) {
			cell.setCellValue((Double) object);
		}
		else if (object instanceof Integer) {
			cell.setCellValue((Integer) object);
		}
		else if (object instanceof Long) {
			cell.setCellValue((Long) object);
		}
		else if (object instanceof Float) {
			cell.setCellValue((Float) object);
		}
		else if (object instanceof Date) {
			cell.setCellValue((Date) object);
		}
	}
	
	public static XSSFSheet addCallHeadeToSheet(XSSFWorkbook workbook, XSSFSheet sheet, int rowIndex, String[] header) {
		try {
			XSSFFont fontHeader = ExcelReportUtils.getFontForExcelForHeader(workbook, true, ReportConstants.TEN);
			fontHeader.setColor(IndexedColors.BLACK.getIndex());
			XSSFCellStyle borderCellStyle = ExcelReportUtils.getCellStyle(workbook, Color.BLACK,
					Color.decode("#FFB300"), BorderStyle.THIN, fontHeader, true, true, true, true,
					HorizontalAlignment.CENTER);

			XSSFRow rowHeader = ExcelReportUtils.getNewRow(sheet, rowIndex++, ReportConstants.FOURTY);
			ExcelReportUtils.createRow(rowHeader, borderCellStyle, header);
			setColumnwidthForReportData(sheet);
			return sheet;
		} catch (Exception e) {
			logger.error("Exception inside method  addCallHeadeToSheet {} ",e.getMessage());
		}
		return null;
	}
	
	private static void setColumnwidthForReportData(Sheet sheet) {
		for (int i = ReportConstants.INDEX_ZER0; i <= ReportConstants.INDEX_ZER0; i++)
			sheet.setColumnWidth(i, ReportConstants.THREE_THOUSAND);
	}
	
	public static int prepareColumnValue(Row row, int col, CellStyle styleDefault, Object value) {

		Cell valueCell1 = row.createCell(col);
		if (value != null &&  !ReportConstants.BLANK_STRING.equals(value.toString())
				&&  !ReportConstants.SPACE.equals(value.toString()))
			valueCell1.setCellValue(value.toString());
		else
			valueCell1.setCellValue(ReportConstants.HIPHEN);
		valueCell1.setCellStyle(styleDefault);
		col++;

		return col;
	}


	public static int prepareColumnValue(Row row, int col, CellStyle styleDefault, Object... valueArr) {
		for (Object value : valueArr) {
			Cell valueCell1 = row.createCell(col);
			if (value != null &&  !ReportConstants.BLANK_STRING.equals(value.toString())
					&&  !ReportConstants.SPACE.equals(value.toString()))
				valueCell1.setCellValue(value.toString());
			else
				valueCell1.setCellValue(ReportConstants.HIPHEN);
			valueCell1.setCellStyle(styleDefault);
			col++;
		}

		return col;
	}
	
	
	public static XSSFWorkbook writeDataColumnWise(Sheet sheet, XSSFWorkbook workbook,int colstrt,
			Object[] data3,boolean isHeader,int rowNum) {
		CellStyle styleRedHeader = getCellStyleForNetVelocityReport(workbook,isHeader, false);
		sheet.setDisplayGridlines(true);
		
		int i = 0;
		for (Object data : data3) {
			Row row = sheet.getRow(rowNum + i) != null ? sheet.getRow(rowNum + i) : sheet.createRow(rowNum + i);

			ExcelReportUtils.prepareColumnValue(row, colstrt, styleRedHeader, data);
			rowNum++;
		}
		return workbook;
	}

	
	public static CellStyle getCellStyleForNetVelocityReport(XSSFWorkbook wb,boolean isHeader, boolean isMergedRow) {
		CellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		if (isMergedRow) {
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}
		XSSFFont font= wb.createFont();
	    font.setFontHeightInPoints((short)ReportConstants.TEN);
	    font.setFontName("Arial");	 
	    font.setColor(IndexedColors.BLACK.getIndex());	    
		if(isHeader) {
	    font.setBold(true);
	    font.setItalic(false);
		}
	    style.setFont(font);
	    
		return style;
	}
	
	

}
