package com.inn.foresight.core.generic.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.inn.commons.lang.DateUtils;

/**
 * The Class DateUtil.
 */
public class DateUtil {
	
	
	/** The Constant DATE_DATEFORMAT. */
	public static final SimpleDateFormat DATE_DATEFORMAT = (SimpleDateFormat) DateFormat.getDateTimeInstance();
	
	/** The Constant DATE_TIME_DATEFORMAT. */
	public static final SimpleDateFormat DATE_TIME_DATEFORMAT = (SimpleDateFormat) DateFormat.getDateTimeInstance();
	
	public static SimpleDateFormat dateFormatYYYYMMDDDashSeperated = new SimpleDateFormat("yyyy-MM-dd");
	/** The Constant FULL_DATEFORMAT. */
	public static final SimpleDateFormat FULL_DATEFORMAT = (SimpleDateFormat) DateFormat.getDateTimeInstance();
	
	/** The Constant HOUR_MINUTE_DATEFORMAT. */
	public static final SimpleDateFormat HOUR_MINUTE_DATEFORMAT = (SimpleDateFormat) DateFormat.getDateTimeInstance();

	/** The date format MMDDYYYY slash seperated with time stamp. */
	public static SimpleDateFormat dateFormatMMDDYYYYSlashSeperatedWithTimeStamp = new SimpleDateFormat("MM/dd/yyyy HH:MM");
	

	static {
		DATE_DATEFORMAT.applyPattern("yyyy-MM-dd");
		DATE_TIME_DATEFORMAT.applyPattern("yyyy-MM-dd HH:mm:ss");
		FULL_DATEFORMAT.applyPattern("yyyy-MM-dd G HH:mm:ss.SSS z");
		HOUR_MINUTE_DATEFORMAT.applyPattern("HH:mm");
	}

	
	/**
	 * Parse {@link String} to {@link Date}.
	 *
	 * @param pattern the pattern
	 * @param date the date
	 * @return the date
	 * @throws ParseException the parse exception
	 */
	public static Date parseStringToDate(String pattern, String date) throws ParseException {
		return DateUtils.parse(pattern, date);
	}
	
	/**
	 * Parse Date to String .
	 *
	 * @param pattern the pattern
	 * @param date the date
	 * @return the string
	 */
	public static String parseDateToString(String pattern, Date date ){
		DateFormat format=new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * Subtract day from date.
	 *
	 * @param noOfDays the no of days
	 * @return the date
	 */
	public static Date subtractDayFromDate(int noOfDays)
	{
		Calendar date = new GregorianCalendar();
		date.add(Calendar.DATE, noOfDays);
		return date.getTime();
	}
	
	public static String getFirstDateFromWeekNumber(Integer year, Integer week, String format) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		WeekFields wf = WeekFields.of(Locale.getDefault());
		LocalDateTime ldt = LocalDateTime.now()
				.withYear(year)
				.with(wf.weekOfYear(), week)
				.with(wf.dayOfWeek(), 2);
		return ldt.format(formatter);
	}
	
	public static int getDifference( Date start, Date end) {
		long diff = end.getTime() - start.getTime();
		return (int) (diff / 86400000);
	}
	
	public static Date add( Date theDate, long value) {
		long dtValue = theDate.getTime();
		return new Date(dtValue + (value * 86400000));
	}
	
	
	public static List<String> getDateRange(Date startDate, Date endDate){
		List<String> dateList= new ArrayList<>();
		int difference = getDifference(startDate,endDate);
		for(int i=0 ; i <= difference ; i++){
			dateList.add(dateFormatYYYYMMDDDashSeperated.format(add(startDate,i)));
		}
	  return dateList;
	}
  
	
}

