package com.inn.foresight.core.report.utils;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderFormatting;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.DateUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.wrapper.ReportInfoWrapper;

/**
 * The Class ReportUtils.
 */
public class ReportUtils extends ForesightConstants {

	/** The logger. */
	static Logger logger = LogManager.getLogger(ReportUtils.class);
	public static final Double CELL_EDGE_THRES = -113.0;

	/**
	 * Sets the value in cell.
	 *
	 * @param row
	 *            the row
	 * @param value
	 *            the value
	 * @param columnCount
	 *            the column count
	 * @param style
	 *            the style
	 * @return the cell
	 */
	public static Cell setValueInCell(Row row, String value, int columnCount, CellStyle style) {
		Cell cell = row.createCell(columnCount);
		if (value != null)
			cell.setCellValue(value);

		else
			cell.setCellValue(HIPHEN);
		if (style != null) {
			cell.setCellStyle(style);
		}
		return cell;
	}

	/**
	 * Sets the value in cell.
	 *
	 * @param row
	 *            the row
	 * @param value
	 *            the value
	 * @param columnCount
	 *            the column count
	 * @return the cell
	 */
	public static Cell setValueInCell(Row row, Cell value, int columnCount) {
		Cell cell = row.createCell(columnCount);
		if (value != null)
			cell.setCellValue(value.toString());
		else
			cell.setCellValue(HIPHEN);
		return cell;
	}

	/**
	 * Sets the column width.
	 *
	 * @param sheet
	 *            the sheet
	 * @param columns
	 *            the columns
	 * @param width
	 *            the width
	 */
	public static void setColumnWidth(Sheet sheet, List<Integer> columns, Integer width) {
		for (Integer column : columns) {
			sheet.setColumnWidth(column, width);
		}
	}

	/**
	 * Sets the column width.
	 *
	 * @param sheet
	 *            the sheet
	 * @param columns
	 *            the columns
	 * @param width
	 *            the width
	 */
	public static void setColumnWidth(SXSSFSheet sheet, List<Integer> columns, Integer width) {
		for (Integer column : columns) {
			sheet.setColumnWidth(column, width);
		}
	}

	/**
	 * Sets the value in cell.
	 *
	 * @param row
	 *            the row
	 * @param value
	 *            the value
	 * @param columnCount
	 *            the column count
	 * @return the cell
	 */
	public static Cell setValueInCell(Row row, String value, int columnCount) {
		Cell cell = row.createCell(columnCount);
		if (value != null || Utils.hasValue(value))
			cell.setCellValue(value);
		else
			cell.setCellValue(HIPHEN);
		return cell;
	}

	/**
	 * Sets the value in cell.
	 *
	 * @param row
	 *            the row
	 * @param value
	 *            the value
	 * @param columnCount
	 *            the column count
	 * @return the SXSSF cell
	 */
	public static SXSSFCell setValueInCell(SXSSFRow row, String value, int columnCount) {
		SXSSFCell cell = (SXSSFCell) row.createCell(columnCount);
		if (value != null)
			cell.setCellValue(value);
		else
			cell.setCellValue(HIPHEN);
		return cell;
	}

	/**
	 * Creates the align style.
	 *
	 * @param workBook
	 *            the work book
	 * @param isHeading
	 *            the is heading
	 * @param alignment
	 *            the alignment
	 * @return the cell style
	 */
	public static CellStyle createAlignStyle(Workbook workBook, boolean isHeading, short alignment) {
		CellStyle style = workBook.createCellStyle();
		style.setAlignment(alignment);
		if (isHeading) {
			Font font = workBook.createFont();
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			style.setFont(font);
		}
		return style;
	}

	/**
	 * Sets the value in cell.
	 *
	 * @param row
	 *            the row
	 * @param style
	 *            the style
	 * @param values
	 *            the values
	 */
	public static void setValueInCell(Row row, CellStyle style, Object... values) {
		for (int i = 0; i < values.length; ++i) {
			Cell cell = row.createCell(i);
			if (values[i] != null)
				cell.setCellValue(values[i].toString());
			else
				cell.setCellValue(HIPHEN);
			if (style != null) {
				cell.setCellStyle(style);
			}
		}
	}

	/**
	 * Make row bold.
	 *
	 * @param workBook
	 *            the work book
	 * @param row
	 *            the row
	 */
	public static void makeRowBold(Workbook workBook, Row row) {
		CellStyle style = workBook.createCellStyle();
		Font font = workBook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);

		for (int i = 0; i < row.getLastCellNum(); i++)
			if (row.getCell(i) != null) {
				row.getCell(i).setCellStyle(style);
			}
	}

	/**
	 * Export excel.
	 *
	 * @param excelFileName
	 *            the excel file name
	 * @param wb
	 *            the wb
	 * @return the file
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static File exportExcel(String excelFileName, XSSFWorkbook wb) throws FileNotFoundException, IOException {
		FileOutputStream fileOut = new FileOutputStream(excelFileName);
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
		return new File(excelFileName);
	}

	/**
	 * Gets the file by name.
	 *
	 * @param fileName
	 *            the file name
	 * @return the file by name
	 */
	public static byte[] getFileByName(String fileName) {
		logger.info("Going to get file by name {}", fileName);
		byte[] b = null;
		byte[] toReturn = null;
		String filePath = Utils.getValidPath(ConfigUtils.getString(ConfigEnum.RAN_EXCEL_FILE.getValue()));
		FileInputStream fis = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			File file = new File(filePath + fileName);
			b = new byte[(int) file.length()];
			fis = new FileInputStream(file);
			fis.read(b);
			baos.write(b);
			toReturn = baos.toByteArray();
			file.delete();
			return toReturn;
		} catch (Exception e) {
			logger.error("Exception in Getting file err{} ", Utils.getStackTrace(e));
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				// empty
			}
		}
		return toReturn;
	}

	/**
	 * Creates the row.
	 *
	 * @param row
	 *            the row
	 * @param cellStyle
	 *            the cell style
	 * @param isAutosizeEnable
	 *            the is autosize enable
	 * @param sheet
	 *            the sheet
	 * @param values
	 *            the values
	 * @return the int
	 */
	public static int createRow(Row row, CellStyle cellStyle, boolean isAutosizeEnable, Sheet sheet, Object... values) {
		int i = 0;
		for (i = 0; i < values.length; i++) {
			try {
				Cell cell = row.createCell(i);
				writeValue(cell, values[i]);
				cell.setCellStyle(cellStyle);
			} catch (Exception e) {
				logger.error("error in creating rows for Excel");
			}
		}
		return i;
	}

	/**
	 * Write value.
	 *
	 * @param cell
	 *            the cell
	 * @param object
	 *            the object
	 */
	public static void writeValue(Cell cell, Object object) {
		if (object == null) {
			cell.setCellValue(HIPHEN);
		} else if (object instanceof String) {
			cell.setCellValue((String) object);
		} else if (object instanceof Double) {
			cell.setCellValue((Double) object);
		} else if (object instanceof Integer) {
			cell.setCellValue((Integer) object);
		} else if (object instanceof Long) {
			cell.setCellValue((Long) object);
		} else if (object instanceof Float) {
			cell.setCellValue((Float) object);
		} else if (object instanceof Date) {
			cell.setCellValue((Date) object);
		}
	}

	/**
	 * Sets the value in cell.
	 *
	 * @param row
	 *            the row
	 * @param value
	 *            the value
	 * @param columnCount
	 *            the column count
	 * @param workBook
	 *            the work book
	 * @return the cell
	 */
	public static Cell setValueInCell(Row row, String value, int columnCount, XSSFWorkbook workBook) {
		Cell cell = row.createCell(columnCount);
		if (value != null)
			cell.setCellValue(value);
		else
			cell.setCellValue(HIPHEN);

		XSSFCellStyle style = workBook.createCellStyle();
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		Font font = workBook.createFont();
		font.setFontName("calibri");
		font.setFontHeightInPoints((short) 11);
		style.setFont(font);
		cell.setCellStyle(style);
		return cell;
	}

	/**
	 * Gets the SXSSF work book.
	 *
	 * @return the SXSSF work book
	 */
	public static SXSSFWorkbook getSXSSFWorkBook() {
		XSSFWorkbook wb_template = new XSSFWorkbook();
		return new SXSSFWorkbook(wb_template);
	}

	/**
	 * Gets the cell style map.
	 *
	 * @param wb
	 *            the wb
	 * @return the cell style map
	 */
	public static Map<String, XSSFCellStyle> getCellStyleMap(SXSSFWorkbook wb) {
		Integer FONT_SIZE_VALUE_NORMAL = 11;
		Integer FONT_SIZE_VALUE_HEADER = 12;
		Map<String, XSSFCellStyle> styleMap = new HashMap<>();
		XSSFFont fontHeader = getFontForExcel(wb, true, FONT_SIZE_VALUE_HEADER);
		XSSFCellStyle headerCellStyle = getCellStyle(wb, Color.BLACK, null, BorderStyle.THIN, fontHeader, true, true,
				true, true, XSSFCellStyle.ALIGN_CENTER);
		XSSFFont fontValue = getFontForExcel(wb, false, FONT_SIZE_VALUE_NORMAL);
		XSSFCellStyle valueCellStyle = getCellStyle(wb, Color.BLACK, null, BorderStyle.THIN, fontValue, true, true,
				true, true, XSSFCellStyle.ALIGN_CENTER);
		styleMap.put("headerCellStyle", headerCellStyle);
		styleMap.put("valueCellStyle", valueCellStyle);
		XSSFCellStyle headerCellStyleBlank = getCellStyle(wb, Color.BLACK, null, null, null, false, false, false, false,
				XSSFCellStyle.ALIGN_CENTER);
		styleMap.put("headerCellStyleBlank", headerCellStyleBlank);
		return styleMap;
	}

	/**
	 * Gets the font for excel.
	 *
	 * @param wb
	 *            the wb
	 * @param isBold
	 *            the is bold
	 * @param height
	 *            the height
	 * @return the font for excel
	 */
	public static XSSFFont getFontForExcel(SXSSFWorkbook wb, boolean isBold, Integer height) {
		XSSFFont font = (XSSFFont) wb.createFont();
		font.setBold(isBold);
		font.setFontHeight(height);
		return font;
	}

	/**
	 * Gets the cell style.
	 *
	 * @param wb
	 *            the wb
	 * @param bordercolor
	 *            the bordercolor
	 * @param cellForeground
	 *            the cell foreground
	 * @param borderStyle
	 *            the border style
	 * @param font
	 *            the font
	 * @param left
	 *            the left
	 * @param right
	 *            the right
	 * @param top
	 *            the top
	 * @param bottom
	 *            the bottom
	 * @param alignCenter
	 *            the align center
	 * @return the cell style
	 */
	public static XSSFCellStyle getCellStyle(Workbook wb, Color bordercolor, Color cellForeground,
			BorderStyle borderStyle, XSSFFont font, boolean left, boolean right, boolean top, boolean bottom,
			short alignCenter) {
		XSSFColor clr = new XSSFColor(bordercolor);
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
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		if (cellForeground != null) {
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFColor foreClr = new XSSFColor(cellForeground);
			style.setFillForegroundColor(foreClr);
		}
		if (font != null) {
			style.setFont(font);
		}
		style.setWrapText(true);
		return style;
	}

	/**
	 * Gets the new row.
	 *
	 * @param sheet
	 *            the sheet
	 * @param index
	 *            the index
	 * @param cellHeight
	 *            the cell height
	 * @return the new row
	 */
	public static SXSSFRow getNewRow(SXSSFSheet sheet, Integer index, Integer cellHeight) {
		SXSSFRow row = (SXSSFRow) sheet.createRow(index);
		if (cellHeight != null)
			row.setHeightInPoints(cellHeight);
		return row;
	}

	/**
	 * Creates the row.
	 *
	 * @param row
	 *            the row
	 * @param cellStyle
	 *            the cell style
	 * @param isAutosizeEnable
	 *            the is autosize enable
	 * @param sheet
	 *            the sheet
	 * @param values
	 *            the values
	 */
	public static void createRow(SXSSFRow row, CellStyle cellStyle, boolean isAutosizeEnable, SXSSFSheet sheet,
			Object... values) {
		int i = 0;
		for (i = 0; i < values.length; i++) {
			try {
				SXSSFCell cell = (SXSSFCell) row.createCell(i);
				writeValue(cell, values[i]);
				if (isAutosizeEnable) {
					sheet.autoSizeColumn(i);
				}
				cell.setCellStyle(cellStyle);
			} catch (Exception e) {
				logger.error("error in creating SXSSFRow rows for Excel");
			}
		}
	}

	/**
	 * Write data into excel.
	 *
	 * @param excelFileName
	 *            the excel file name
	 * @param wb
	 *            the wb
	 * @return the byte[]
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static byte[] writeDataIntoExcel(String excelFileName, SXSSFWorkbook wb) throws IOException {
		logger.info("Going to writedata in ByteArrayOutputStream");
		FileOutputStream fileOut = new FileOutputStream(excelFileName);
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
		File file = new File(excelFileName);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[(int) file.length()];
		FileInputStream fis = new FileInputStream(file);
		try {
			fis.read(b);
			baos.write(b);
			file.delete();
		} finally {
			fis.close();
		}
		return baos.toByteArray();
	}

	/**
	 * Write data into excel.
	 *
	 * @param excelFileName
	 *            the excel file name
	 * @param wb
	 *            the wb
	 * @return the byte[]
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static byte[] writeDataIntoExcel(String excelFileName, XSSFWorkbook wb) throws IOException {
		logger.info("Going to writedata in ByteArrayOutputStream");
		FileOutputStream fileOut = new FileOutputStream(excelFileName);
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
		File file = new File(excelFileName);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[(int) file.length()];
		FileInputStream fis = new FileInputStream(file);
		try {
			fis.read(b);
			baos.write(b);
			file.delete();
		} finally {
			fis.close();
		}
		return baos.toByteArray();
	}

	/**
	 * Gets the new row.
	 *
	 * @param sheet
	 *            the sheet
	 * @param index
	 *            the index
	 * @param cellHeight
	 *            the cell height
	 * @return the new row
	 */
	public static Row getNewRow(Sheet sheet, Integer index, Integer cellHeight) {
		Row row = sheet.createRow(index);
		if (cellHeight != null)
			row.setHeightInPoints(cellHeight);
		return row;
	}

	/**
	 * Gets the font for excel for header.
	 *
	 * @param wb
	 *            the wb
	 * @param isBold
	 *            the is bold
	 * @param height
	 *            the height
	 * @return the font for excel for header
	 */
	public static XSSFFont getFontForExcelForHeader(SXSSFWorkbook wb, boolean isBold, Integer height) {
		XSSFFont font = (XSSFFont) wb.createFont();
		font.setBold(isBold);
		font.setFontName("Arial");
		font.setFontHeight(height);
		return font;
	}

	/**
	 * Gets the cell style.
	 *
	 * @param wb
	 *            the wb
	 * @param bordercolor
	 *            the bordercolor
	 * @param cellForeground
	 *            the cell foreground
	 * @param borderStyle
	 *            the border style
	 * @param font
	 *            the font
	 * @param left
	 *            the left
	 * @param right
	 *            the right
	 * @param top
	 *            the top
	 * @param bottom
	 *            the bottom
	 * @param alignCenter
	 *            the align center
	 * @return the cell style
	 */
	public static XSSFCellStyle getCellStyle(SXSSFWorkbook wb, Color bordercolor, Color cellForeground,
			BorderStyle borderStyle, XSSFFont font, boolean left, boolean right, boolean top, boolean bottom,
			short alignCenter) {
		XSSFColor clr = new XSSFColor(bordercolor);
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
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		if (cellForeground != null) {
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFColor foreClr = new XSSFColor(cellForeground);
			style.setFillForegroundColor(foreClr);
		}
		if (font != null) {
			style.setFont(font);
		}
		style.setWrapText(true);
		return style;
	}

	/**
	 * Gets the cell style.
	 *
	 * @param heading
	 *            the heading
	 * @param workbook
	 *            the workbook
	 * @return the cell style
	 */
	public static CellStyle getCellStyle(boolean heading, Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);

		if (heading) {
			Font font = workbook.createFont();
			font.setBoldweight(Font.BOLDWEIGHT_BOLD);
			style.setFont(font);
		}
		return style;
	}

	/**
	 * Export excel SXSSF workbook.
	 *
	 * @param excelFileName
	 *            the excel file name
	 * @param wb
	 *            the wb
	 * @return the file
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws RestException
	 *             the rest exception
	 */
	public static File exportExcelSXSSFWorkbook(String excelFileName, SXSSFWorkbook wb)
			throws FileNotFoundException, IOException {
		try {
			File file = new File(excelFileName);
			FileOutputStream fileOut = new FileOutputStream(file);
			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
			return file;
		} catch (Exception e) {
			logger.error("error in writing file, error = {}", Utils.getStackTrace(e));
		}
		throw new RestException("File Not Generated");
	}

	/**
	 * Sets the BG colour in specific column.
	 *
	 * @param workBook
	 *            the work book
	 * @param row
	 *            the row
	 * @param backgroudColor
	 *            the backgroud color
	 * @param fillPattern
	 *            the fill pattern
	 * @param columnNo
	 *            the column no
	 */
	public static void setBGColourInSpecificColumn(Workbook workBook, Row row, short backgroudColor, short fillPattern,
			Integer[] columnNo) {
		CellStyle style = workBook.createCellStyle();
		style.setFillBackgroundColor(backgroudColor);
		style.setFillPattern(fillPattern);
		Font font = workBook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		for (int i = 0; i < columnNo.length; i++)
			row.getCell(columnNo[i]).setCellStyle(style);
	}

	/**
	 * Export and delete excel.
	 *
	 * @param fileName
	 *            the file name
	 * @param workbook
	 *            the workbook
	 * @return the byte[]
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static byte[] exportAndDeleteExcel(String fileName, Workbook workbook)
			throws FileNotFoundException, IOException {
		FileOutputStream fileOut = new FileOutputStream(fileName);
		workbook.write(fileOut);
		fileOut.flush();
		fileOut.close();

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		File file = new File(fileName);
		byte[] b = new byte[(int) file.length()];
		FileInputStream fis = new FileInputStream(file);
		try {
			fis.read(b);
			baos.write(b);
			file.delete();
		} finally {
			fis.close();
		}

		return baos.toByteArray();
	}

	/**
	 * Parses the config to wrapper.
	 *
	 * @param configuration
	 *            the configuration
	 * @return the report info wrapper
	 */
	public static ReportInfoWrapper parseConfigToWrapper(String configuration) {
		String configJson = "";
		try {
			if (Utils.isValidString(configuration)) {
				configJson = configuration.replace("'", "\"");
				Gson gson = new Gson();
				ReportInfoWrapper wrapper = gson.fromJson(configJson, ReportInfoWrapper.class);
				wrapper.setStartDate(Utils.parseTimeStampToDate(wrapper.getStartTime()));
				wrapper.setEndDate(Utils.parseTimeStampToDate(wrapper.getEndTime()));
				if (Utils.isValidString(wrapper.getStartDateStr()) && Utils.isValidString(wrapper.getEndDateStr())) {
					wrapper.setStartDate(Utils.parseStringToDate(wrapper.getStartDateStr(), DATE_FORMAT_dd_MM_yyyy));
					wrapper.setEndDate(Utils.parseStringToDate(wrapper.getEndDateStr(), DATE_FORMAT_dd_MM_yyyy));
				}
				return wrapper;
			}
		} catch (Exception e) {
			logger.error("parseConfigToWrapper Exception for {} : {}", configJson, e.getMessage());
		}
		return null;
	}

	/**
	 * Format date.
	 *
	 * @param date
	 *            the date
	 * @param format
	 *            the format
	 * @return the string
	 */
	public static String formatDate(Date date, String format) {
		String newDate = null;
		if (date != null) {
			try {
				SimpleDateFormat format2 = new SimpleDateFormat(format);
				newDate = format2.format(date);
			} catch (Exception e) {
				logger.info("formatDate Exception: " + e.getMessage());
			}

		}
		return newDate;
	}

	/**
	 * Sets the colour in specific column.
	 *
	 * @param workBook
	 *            the work book
	 * @param row
	 *            the row
	 * @param backgroudColor
	 *            the backgroud color
	 * @param fillPattern
	 *            the fill pattern
	 * @param columnNo
	 *            the column no
	 */
	public static void setColourInSpecificColumn(Workbook workBook, Row row, short backgroudColor, short fillPattern,
			Integer[] columnNo) {
		CellStyle style = workBook.createCellStyle();
		style.setFillBackgroundColor(backgroudColor);
		style.setFillPattern(fillPattern);
		Font font = workBook.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		for (int i = 0; i < columnNo.length; i++) {
			row.getCell(columnNo[i]).setCellStyle(style);
		}
	}

	/**
	 * Creates the row.
	 *
	 * @param row
	 *            the row
	 * @param cellStyle
	 *            the cell style
	 * @param isAutosizeEnable
	 *            the is autosize enable
	 * @param sheet
	 *            the sheet
	 * @param values
	 *            the values
	 */
	// For Used To all inside CM Module
	public static void createRow(XSSFRow row, CellStyle cellStyle, boolean isAutosizeEnable, XSSFSheet sheet,
			Object... values) {
		for (int i = 0; i < values.length; i++) {
			try {
				XSSFCell cell = row.createCell(i);
				cell.setCellValue(values[i] == null ? HIPHEN : values[i].toString());
				cell.setCellStyle(cellStyle);
				if (isAutosizeEnable) {
					sheet.autoSizeColumn(i);
				}
			} catch (Exception e) {
				// empty
			}
		}
	}

	/**
	 * Creates the header.
	 *
	 * @param row
	 *            the row
	 * @param cellStyle
	 *            the cell style
	 * @param isAutosizeEnable
	 *            the is autosize enable
	 * @param sheet
	 *            the sheet
	 * @param columnWidth
	 *            the column width
	 * @param values
	 *            the values
	 */
	public static void createHeader(XSSFRow row, CellStyle cellStyle, boolean isAutosizeEnable, XSSFSheet sheet,
			Integer columnWidth, Object... values) {
		for (int i = 0; i < values.length; i++) {
			try {
				XSSFCell cell = row.createCell(i);
				cell.setCellValue(values[i] == null ? HIPHEN : values[i].toString());
				cell.setCellStyle(cellStyle);
				if (isAutosizeEnable) {
					sheet.autoSizeColumn(i);
				} else if (columnWidth != null) {
					sheet.setColumnWidth(i, columnWidth);
				}
			} catch (Exception e) {
				// empty
			}
		}
	}

	/**
	 * Gets the font for excel.
	 *
	 * @param wb
	 *            the wb
	 * @param isBold
	 *            the is bold
	 * @param height
	 *            the height
	 * @return the font for excel
	 */
	public static XSSFFont getFontForExcel(XSSFWorkbook wb, boolean isBold, Integer height) {
		XSSFFont font = wb.createFont();
		font.setBold(isBold);
		font.setFontHeight(height);
		return font;
	}

	/**
	 * Gets the new row.
	 *
	 * @param sheet
	 *            the sheet
	 * @param index
	 *            the index
	 * @param cellHeight
	 *            the cell height
	 * @return the new row
	 */
	public static XSSFRow getNewRow(XSSFSheet sheet, Integer index, Integer cellHeight) {
		XSSFRow row = sheet.createRow(index);
		if (cellHeight != null)
			row.setHeightInPoints(cellHeight);
		return row;
	}

	/**
	 * Gets the cell style.
	 *
	 * @param wb
	 *            the wb
	 * @param bordercolor
	 *            the bordercolor
	 * @param cellForeground
	 *            the cell foreground
	 * @param borderStyle
	 *            the border style
	 * @param font
	 *            the font
	 * @param left
	 *            the left
	 * @param right
	 *            the right
	 * @param top
	 *            the top
	 * @param bottom
	 *            the bottom
	 * @param alignCenter
	 *            the align center
	 * @return the cell style
	 */
	public static XSSFCellStyle getCellStyle(XSSFWorkbook wb, Color bordercolor, Color cellForeground,
			BorderStyle borderStyle, XSSFFont font, boolean left, boolean right, boolean top, boolean bottom,
			short alignCenter) {
		XSSFColor clr = new XSSFColor(bordercolor);
		XSSFCellStyle style = wb.createCellStyle();
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
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		if (cellForeground != null) {
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFColor foreClr = new XSSFColor(cellForeground);
			style.setFillForegroundColor(foreClr);
		}
		if (font != null) {
			style.setFont(font);
		}
		style.setWrapText(true);
		return style;
	}

	/**
	 * Export excel.
	 *
	 * @param excelFileName
	 *            the excel file name
	 * @param wb
	 *            the wb
	 * @return the file
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws RestException
	 *             the rest exception
	 */
	public static File exportExcel(String excelFileName, Workbook wb) throws FileNotFoundException, IOException {
		try (FileOutputStream fileOut = new FileOutputStream(new File(excelFileName))) {
			wb.write(fileOut);
			fileOut.flush();
		} catch (Exception e) {
			logger.error("error in writing file, error = {}", Utils.getStackTrace(e));
		}
		throw new RestException("File Not Generated");
	}

	public static File exportExcelWB(String excelFileName, Workbook wb) throws FileNotFoundException, IOException {
		FileOutputStream fileOut = new FileOutputStream(excelFileName);
		wb.write(fileOut);
		fileOut.flush();
		fileOut.close();
		return new File(excelFileName);
	}

	/**
	 * Gets the cell style.
	 *
	 * @param wb
	 *            the wb
	 * @param bordercolor
	 *            the bordercolor
	 * @param cellForeground
	 *            the cell foreground
	 * @param font
	 *            the font
	 * @param left
	 *            the left
	 * @param right
	 *            the right
	 * @param top
	 *            the top
	 * @param bottom
	 *            the bottom
	 * @return the cell style
	 */
	public static CellStyle getCellStyle(Workbook wb, IndexedColors bordercolor, Short cellForeground, Font font,
			boolean left, boolean right, boolean top, boolean bottom) {
		short clr = bordercolor == null ? IndexedColors.BLACK.getIndex() : bordercolor.getIndex();
		CellStyle style = wb.createCellStyle();
		if (left) {
			style.setLeftBorderColor(clr);
			style.setBorderLeft(BorderFormatting.BORDER_THIN);
		}
		if (right) {
			style.setBorderRight(BorderFormatting.BORDER_THIN);
			style.setRightBorderColor(clr);
		}
		if (top) {
			style.setBorderTop(BorderFormatting.BORDER_THIN);
			style.setTopBorderColor(clr);
		}
		if (bottom) {
			style.setBorderBottom(BorderFormatting.BORDER_THIN);
			style.setBottomBorderColor(clr);
		}
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		if (cellForeground != null) {
			style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			style.setFillForegroundColor(cellForeground);
		}
		if (font != null) {
			style.setFont(font);
		}
		style.setWrapText(true);
		return style;
	}

	/**
	 * Write value on cell.
	 *
	 * @param row
	 *            the row
	 * @param cellStyle
	 *            the cell style
	 * @param isAutosizeEnable
	 *            the is autosize enable
	 * @param sheet
	 *            the sheet
	 * @param index
	 *            the index
	 * @param value
	 *            the value
	 * @return the int
	 */
	public static int writeValueOnCell(Row row, CellStyle cellStyle, boolean isAutosizeEnable, Sheet sheet, int index,
			Integer value) {
		try {
			Cell cell = row.createCell(index);
			writeValue(cell, value);
			cell.setCellStyle(cellStyle);

		} catch (Exception e) {
			logger.error("error in writeValueOnCell" + Utils.getStackTrace(e));
		}
		return index;
	}

	/**
	 * Write value on cell.
	 *
	 * @param row
	 *            the row
	 * @param cellStyle
	 *            the cell style
	 * @param isAutosizeEnable
	 *            the is autosize enable
	 * @param sheet
	 *            the sheet
	 * @param i
	 *            the i
	 * @param value
	 *            the value
	 * @return the int
	 */
	public static int writeValueOnCell(Row row, CellStyle cellStyle, boolean isAutosizeEnable, Sheet sheet, int i,
			String value) {
		try {
			String REPLACEMENT_DASH = "-";
			Cell cell = row.createCell(i);
			cell.setCellValue(value == null ? REPLACEMENT_DASH : value);
			cell.setCellStyle(cellStyle);
			if (isAutosizeEnable) {
				sheet.autoSizeColumn(i);
			}
		} catch (Exception e) {
			logger.error("error in creating rows for Excel");
		}
		return i;
	}

	/**
	 * Write value on cell.
	 *
	 * @param row
	 *            the row
	 * @param cellStyle
	 *            the cell style
	 * @param isAutosizeEnable
	 *            the is autosize enable
	 * @param sheet
	 *            the sheet
	 * @param index
	 *            the index
	 * @param value
	 *            the value
	 * @return the int
	 */
	public static int writeValueOnCell(Row row, CellStyle cellStyle, boolean isAutosizeEnable, Sheet sheet, int index,
			Double value) {
		try {
			Cell cell = row.createCell(index);
			if (Utils.isValidDouble(value)) {
				writeValue(cell, value);
				cell.setCellStyle(cellStyle);
			} else
				cell.setCellValue(DASH);
			cell.setCellStyle(cellStyle);
		} catch (Exception e) {
			logger.error("error in writeValueOnCell" + Utils.getStackTrace(e));
		}
		return index;
	}

	/**
	 * Gets the font for excel.
	 *
	 * @param wb
	 *            the wb
	 * @param isBold
	 *            the is bold
	 * @param height
	 *            the height
	 * @return the font for excel
	 */
	public static Font getFontForExcel(Workbook wb, boolean isBold, Integer height) {
		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		return font;
	}

	/**
	 * Gets the max.
	 *
	 * @param values
	 *            the values
	 * @return the max
	 */
	public static Double getMax(Double... values) {
		Arrays.sort(values);
		return values[values.length - 1];
	}

	/**
	 * Creates the row map.
	 *
	 * @param row
	 *            the row
	 * @param wb
	 *            the wb
	 * @param isAutosizeEnable
	 *            the is autosize enable
	 * @param sheet
	 *            the sheet
	 * @param date
	 *            the date
	 * @param time
	 *            the time
	 * @param circle
	 *            the circle
	 * @param jiCenter
	 *            the ji center
	 * @param band
	 *            the band
	 * @param cellName
	 *            the cell name
	 * @param kpiValueMap
	 *            the kpi value map
	 * @param kpiDetailMap
	 *            the kpi detail map
	 * @param fontRED
	 *            the font RED
	 * @param fontBLACK
	 *            the font BLACK
	 * @param cellStyleRED
	 *            the cell style RED
	 * @param cellStyleBLACK
	 *            the cell style BLACK
	 * @param fontValue
	 *            the font value
	 * @param kpiValueMapHeader
	 *            the kpi value map header
	 * @return the int
	 */
	public static int createRowMap(Row row, XSSFWorkbook wb, boolean isAutosizeEnable, Sheet sheet, String date,
			String time, String circle, String jiCenter, String band, String cellName, Map<String, Double> kpiValueMap,
			Map<String, String> kpiDetailMap, Font fontRED, Font fontBLACK, XSSFCellStyle cellStyleRED,
			XSSFCellStyle cellStyleBLACK, XSSFFont fontValue, List<String> kpiValueMapHeader) {
		int i = 0;
		Boolean redCheck = false;

		Cell celldate = row.createCell(i++);
		writeValue(celldate, date);
		Cell cellTime = row.createCell(i++);
		writeValue(cellTime, time);
		Cell cellCircle = row.createCell(i++);
		writeValue(cellCircle, circle);
		Cell cellappCenter = row.createCell(i++);
		writeValue(cellappCenter, jiCenter);
		if (band != null) {
			Cell cellBand = row.createCell(i++);
			writeValue(cellBand, band);
		}
		Cell setcellName = row.createCell(i++);
		writeValue(setcellName, cellName);

		for (String key : kpiValueMapHeader) {

			try {
				Cell cell = row.createCell(i++);
				redCheck = isThresholdCritical(kpiValueMap.get(key), String.valueOf(kpiDetailMap.get(key)));
				if (redCheck) {
					cell.setCellStyle(cellStyleRED);
					writeValue(cell, kpiValueMap.get(key));
				} else {
					cell.setCellStyle(cellStyleBLACK);
					writeValue(cell, kpiValueMap.get(key));
				}

			} catch (Exception e) {
				logger.error("error in creating rows for Excel");
			}
		}
		return i;
	}

	public static int createRowMapForReport(Row row, XSSFWorkbook wb, boolean isAutosizeEnable, Sheet sheet,
			String date, String time, String gl1, String gl2, String gl3, String gl4, String enodeb, String cellName,
			String band, Map<String, Double> kpiValueMap, Map<String, String> kpiDetailMap, Font fontRED,
			Font fontBLACK, XSSFCellStyle cellStyleRED, XSSFCellStyle cellStyleBLACK, XSSFFont fontValue,
			List<String> kpiValueMapHeader, String domain, CellStyle valueCellStyle) {
		int i = 0;
		Boolean redCheck = false;

		Cell celldate = row.createCell(i++);
		celldate.setCellStyle(valueCellStyle);
		writeValue(celldate, date);

		Cell cellTime = row.createCell(i++);
		cellTime.setCellStyle(valueCellStyle);
		writeValue(cellTime, time);

		Cell cellGl1 = row.createCell(i++);
		cellGl1.setCellStyle(valueCellStyle);
		writeValue(cellGl1, gl1);

		Cell cellGl2 = row.createCell(i++);
		cellGl2.setCellStyle(valueCellStyle);
		writeValue(cellGl2, gl2);

		Cell cellGl3 = row.createCell(i++);
		cellGl3.setCellStyle(valueCellStyle);
		writeValue(cellGl3, gl3);

		Cell cellGl4 = row.createCell(i++);
		cellGl4.setCellStyle(valueCellStyle);
		writeValue(cellGl4, gl4);

		Cell setenodeb = row.createCell(i++);
		setenodeb.setCellStyle(valueCellStyle);
		writeValue(setenodeb, enodeb);

		if (!domain.equalsIgnoreCase("MICROWAVE")) {
			Cell setcellName = row.createCell(i++);
			setcellName.setCellStyle(valueCellStyle);
			writeValue(setcellName, cellName);

			Cell cellBand = row.createCell(i++);
			cellBand.setCellStyle(valueCellStyle);
			writeValue(cellBand, band);
		}

		for (String key : kpiValueMapHeader) {

			try {
				Cell cell = row.createCell(i++);
				redCheck = isThresholdCritical(kpiValueMap.get(key), String.valueOf(kpiDetailMap.get(key)));
				if (redCheck) {
					cell.setCellStyle(cellStyleRED);
					writeValue(cell, kpiValueMap.get(key));
				} else {
					cell.setCellStyle(cellStyleBLACK);
					writeValue(cell, kpiValueMap.get(key));
				}

			} catch (Exception e) {
				logger.error("error in creating rows for Excel");
			}
		}
		return i;
	}

	/**
	 * Checks if is threshold critical.
	 *
	 * @param value
	 *            the value
	 * @param rangeValue
	 *            the range value
	 * @return true, if is threshold critical
	 */
	private static boolean isThresholdCritical(Double value, String rangeValue) {

		if (value == null)
			value = 0.0;
		if (rangeValue != null && rangeValue.length() > 0) {
			if (rangeValue.contains(">=")) {
				if (value >= Double.parseDouble(rangeValue.replace(">= ", ""))) {
					return true;
				} else
					return false;
			} else if (rangeValue.contains("<=")) {
				if (value <= Double.parseDouble(rangeValue.replace("<= ", ""))) {
					return true;
				} else
					return false;
			} else if (rangeValue.contains(">")) {
				if (value > Double.parseDouble(rangeValue.replace("> ", ""))) {
					return true;
				} else
					return false;
			} else if (rangeValue.contains("<")) {
				if (value < Double.parseDouble(rangeValue.replace("< ", ""))) {
					return true;
				} else
					return false;
			} else if (rangeValue.contains("=")) {
				if (value == Double.parseDouble(rangeValue.replace("= ", ""))) {
					return true;
				} else
					return false;
			} else if (rangeValue.contains("between")) {
				if (value >= Double.parseDouble(rangeValue.replace("between#", "").split("to")[0])
						&& value <= Double.parseDouble(rangeValue.replace("between#", "").split("to")[1])) {
					return true;
				} else
					return false;
			} else
				return false;
		}
		return false;
	}

	/**
	 * Creates the row with cell index.
	 *
	 * @param row
	 *            the row
	 * @param cellStyle
	 *            the cell style
	 * @param isAutosizeEnable
	 *            the is autosize enable
	 * @param sheet
	 *            the sheet
	 * @param values
	 *            the values
	 * @return the int
	 */
	public static int createRowWithCellIndex(XSSFRow row, CellStyle cellStyle, boolean isAutosizeEnable,
			XSSFSheet sheet, String... values) {
		int i = 0;
		for (i = 0; i < values.length; i++) {
			try {
				Cell cell = row.createCell(i);
				writeValue(cell, values[i]);
				cell.setCellStyle(cellStyle);

			} catch (Exception e) {
				logger.error("error in creating rows for Excel");
			}
		}
		return i;
	}

	/**
	 * Stringify.
	 *
	 * @param intValue
	 *            the int value
	 * @return the string
	 */
	public static String stringify(Integer intValue) {
		try {
			if (Utils.isValidString(intValue + ""))
				return intValue + "";
		} catch (Exception e) {
			logger.info("getStringValue Exception: " + e.getMessage());
		}
		return null;
	}

	/**
	 * Gets the absolute file path.
	 *
	 * @param customizedFilePath
	 *            the customized file path
	 * @return the absolute file path
	 */
	public static String getAbsoluteFilePath(String customizedFilePath) {
		return ConfigUtils.getString(ConfigEnum.REPORT_BASE_DIRECTORY) + customizedFilePath;
	}

	/**
	 * Gets the bytes.
	 *
	 * @param file
	 *            the file
	 * @return the bytes
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static byte[] getBytes(File file) throws IOException {
		return Files.readAllBytes(file.toPath());
	}

	/**
	 * Gets the if file exists.
	 *
	 * @param filePath
	 *            the file path
	 * @return the if file exists
	 */
	public static File getIfFileExists(String filePath) {
		try {
			if (Utils.isValidString(filePath)) {
				File file = new File(filePath);
				if (file.exists())
					return file;
			}
		} catch (Exception e) {
			logger.error("error on checkIfFileExists[{}], error={}  ", filePath, Utils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Gets the date using days.
	 *
	 * @param date
	 *            the date
	 * @param dayAfter
	 *            the day after
	 * @param dayBefore
	 *            the day before
	 * @return the date using days
	 */
	public static Date getDateUsingDays(Date date, int dayAfter, int dayBefore) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			if (dayAfter != 0) {
				cal.add(Calendar.DATE, dayAfter);
			} else {
				cal.add(Calendar.DATE, (-1) * dayBefore);
			}
			return cal.getTime();

		} catch (Exception e) {
			logger.info("@getDateUsingDays going to return yesterday's date err = {}", Utils.getStackTrace(e));
			return getDateUsingDays(new Date(), 0, 1);
		}
	}

	/**
	 * Gets the start and end date of week.
	 *
	 * @param date
	 *            the date
	 * @param stratsFrom
	 *            the strats from
	 * @return the start and end date of week
	 */
	public static Date[] getStartAndEndDateOfWeek(Date date, int startsFrom) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.add(Calendar.DATE, -1);
		c1.setFirstDayOfWeek(startsFrom);
		Calendar first = (Calendar) c1.clone();
		first.add(Calendar.DAY_OF_WEEK, first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));

		Calendar last = (Calendar) first.clone();
		last.add(Calendar.DAY_OF_YEAR, 6);

		return new Date[] { first.getTime(), last.getTime() };
	}

	/**
	 * Gets the start and end date from week for pre post.
	 *
	 * @param weekNo
	 *            the week no
	 * @return the start and end date from week for pre post
	 * @throws ParseException
	 *             the parse exception
	 */
	public static Date[] getStartAndEndDateFromWeekForPrePost(String weekNo) throws ParseException {
		Date date = DateUtil.parseStringToDate(DATE_FORMAT_YYYYww, weekNo);
		return getStartAndEndDateOfWeek(ReportUtils.getDateUsingDays(date, 1, 0), Calendar.SUNDAY);
	}

	public static HttpResponse sendGetRequestWithoutTimeOut(String uri) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(uri);
		try {
			logger.info("Going to hit URL : {}", uri);
			CloseableHttpResponse response = httpClient.execute(httpGet);

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw new RestException("Failed with HTTP error code : " + statusCode);
			}
			logger.info("Return Response Code : {}", statusCode);
			return response;
		} catch (Exception e) {
			throw new RestException(EXCEPTION_ON_CONNECTION);
		} finally {
			/*
			 * httpGet.releaseConnection(); IOUtils.closeQuietly(httpClient);
			 */
		}

	}

	/**
	 * Write response.
	 *
	 * @param resp
	 *            the resp
	 * @param fileStream
	 *            the file stream
	 * @return the http servlet response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static HttpServletResponse writeResponse(HttpServletResponse resp, InputStream fileStream)
			throws IOException {
		ServletOutputStream outStream = null;
		try {
			int inByte;
			resp.setStatus(200);
			outStream = resp.getOutputStream();
			while ((inByte = fileStream.read()) != -1) {
				outStream.write(inByte);
			}
		} catch (Exception e) {
			logger.error("getting error on writing data to file");
		} finally {
			try {
				fileStream.close();
				if (outStream != null) {
					outStream.close();
				}
			} catch (Exception e2) {

			}
		}
		return resp;
	}

	/**
	 * Write response to excel file.
	 *
	 * Used in reports.jsp
	 * 
	 * @param resp
	 *            the resp
	 * @param fileStream
	 *            the file stream
	 * @param fileName
	 *            the file name
	 * @return the http servlet response
	 * @throws RestException
	 *             the rest exception
	 */

	public static HttpServletResponse writeResponseToExcelFile(HttpServletResponse resp, InputStream fileStream,
			String contentDisposition, String contentType) {
		try {
			logger.debug("going to write this response =  {}", contentDisposition);
			resp = writeResponse(resp, fileStream);
			resp.setHeader(CONTENT_TYPE, contentType);
			resp.setHeader("Content-Disposition", contentDisposition);
		} catch (Exception e) {
			logger.error("getting error on writing data to file");
			throw new RestException("Error on converting into json");
		}
		return resp;
	}

	/**
	 * Gets the value from cell.
	 *
	 * @param cell
	 *            the cell
	 * @return the value from cell
	 */
	public static Object getValueFromCell(Cell cell) {
		if (cell != null) {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				return cell.getNumericCellValue();
			case Cell.CELL_TYPE_STRING:
				return cell.getStringCellValue();
			case Cell.CELL_TYPE_BOOLEAN:
				return cell.getBooleanCellValue();
			}
		}
		return ForesightConstants.BLANK_STRING;
	}

	public static Double getRadius(Double txPower, int frequency) {
		Double txPowerinWatt = Math.pow(10, (txPower / 10)) / 1000.0;
		Double p_Cell_Edge = Math.pow(10, (CELL_EDGE_THRES / 10.0)) / 1000.0;
		Double FSPL = 10 * Math.log10(txPowerinWatt / p_Cell_Edge);
		Double redius = Math.pow(10, (FSPL + 27.55 - (20.0 * Math.log10(frequency * 1000.0))) / 20.0);
		return redius;
	}

	public static void deleteGeneratedFile(String filePath) {
		if (Utils.hasValidValue(filePath)) {
			File file = new File(filePath);

			if (file.exists() && file.isDirectory()) {
				File[] listFiles = file.listFiles();
				for (File tempFile : listFiles) {
					if (tempFile.delete()) {
						logger.info("Files deleted which are in sub-folder");
					}
				}
				if (file.delete())
					logger.info("All files deleted sucessfully with folder {}", filePath);
			} else {
				logger.info("Path doesn't exists {}", filePath);
			}

		}

	}

}
