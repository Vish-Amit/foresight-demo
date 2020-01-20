package com.inn.foresight.module.nv.dashboard.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

import com.inn.foresight.core.generic.utils.ForesightConstants;

/** The Class NvDashboardUtils. */
public class NvDashboardUtils {

	/**
	 * Gets the first and last week by date.
	 *
	 * @param endDate
	 *            the end date
	 * @param amount
	 *            the amount
	 * @return the first and last week by date
	 */
	public static Integer[] getFirstAndLastWeekByDate(Date endDate, Integer amount) {
		Integer[] firstLastWeek = new Integer[ForesightConstants.TWO];
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		Integer lastWeek = Integer.parseInt(getWeekAndYearByDate(calendar));
		calendar.setTime(DateUtils.addWeeks(endDate, amount));
		Integer firstWeek = Integer.parseInt(getWeekAndYearByDate(calendar));
		firstLastWeek[ForesightConstants.ZERO_INT] = firstWeek;
		firstLastWeek[ForesightConstants.ONE] = lastWeek;
		return firstLastWeek;
	}

	/**
	 * Gets the first and last month by date.
	 *
	 * @param endDate
	 *            the end date
	 * @param amount
	 *            the amount
	 * @return the first and last month by date
	 */
	public static Integer[] getFirstAndLastMonthByDate(Date endDate, Integer amount) {
		Integer[] firstLastMonth = new Integer[ForesightConstants.TWO];
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endDate);
		Integer lastMonth = Integer.parseInt(getMonthAndYearByDate(calendar));
		calendar.setTime(DateUtils.addMonths(endDate, amount));
		Integer firstMonth = Integer.parseInt(getMonthAndYearByDate(calendar));
		firstLastMonth[ForesightConstants.ZERO_INT] = firstMonth;
		firstLastMonth[ForesightConstants.ONE] = lastMonth;
		return firstLastMonth;
	}

	/**
	 * Gets the week and year by date.
	 *
	 * @param cal
	 *            the cal
	 * @return the week and year by date
	 */
	public static String getWeekAndYearByDate(Calendar cal) {
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		int weekNumber = cal.get(Calendar.WEEK_OF_YEAR);
		int monthNumber = cal.get(Calendar.MONTH);
		if (monthNumber == ForesightConstants.ELEVEN && weekNumber == ForesightConstants.ONE)
			weekNumber = ForesightConstants.FIFTY_THREE;
		int year = cal.get(Calendar.YEAR) % ForesightConstants.HUNDRED_VALUE;
		return (year + ForesightConstants.BLANK_STRING + ((weekNumber < ForesightConstants.TEN)
				? ForesightConstants.ZERO_IN_STRING : ForesightConstants.BLANK_STRING) + weekNumber);
	}

	/**
	 * Gets the month and year by date.
	 *
	 * @param cal
	 *            the cal
	 * @return the month and year by date
	 */
	public static String getMonthAndYearByDate(Calendar cal) {
		int year = cal.get(Calendar.YEAR) % ForesightConstants.HUNDRED_VALUE;
		int monthNumber = cal.get(Calendar.MONTH) + ForesightConstants.ONE;
		return (year + ForesightConstants.BLANK_STRING + ((monthNumber < ForesightConstants.TEN)
				? ForesightConstants.ZERO_IN_STRING : ForesightConstants.BLANK_STRING) + monthNumber);
	}

	/**
	 * Gets the date key list by call type.
	 *
	 * @param callType
	 *            the call type
	 * @param endDate
	 *            the end date
	 * @return the date key list by call type
	 */
	public static List<String> getDateKeyListByCallType(String callType, Date endDate) {
		List<String> dateList = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		Date date = new Date(endDate.getTime());

		if (callType.equalsIgnoreCase(NVDashboardConstants.DAY)) {
			for (int i = ForesightConstants.SIX; i >= ForesightConstants.ZERO_INT; i--) {
				DateFormat dateFormat = new SimpleDateFormat(NVDashboardConstants.DD_MM_YYYY);
				dateList.add(dateFormat.format(date));
				date = DateUtils.addDays(date, ForesightConstants.MINUS_ONE);
			}
		} else if (callType.equalsIgnoreCase(NVDashboardConstants.WEEK)) {
			cal.setFirstDayOfWeek(Calendar.MONDAY);
			for (int i = ForesightConstants.SIX; i >= ForesightConstants.ZERO_INT; i--) {
				cal.setTime(date);
				dateList.add(getWeekAndYearByDate(cal));
				date = DateUtils.addWeeks(date, ForesightConstants.MINUS_ONE);
			}
		} else if (callType.equalsIgnoreCase(NVDashboardConstants.MONTH)) {
			for (int i = ForesightConstants.SIX; i >= ForesightConstants.ZERO_INT; i--) {
				cal.setTime(date);
				dateList.add(getMonthAndYearByDate(cal));
				date = DateUtils.addMonths(date, ForesightConstants.MINUS_ONE);
			}
		}
		return dateList;
	}

	/**
	 * Gets the first date of month.
	 *
	 * @param date
	 *            the date
	 * @return the first date of month
	 */
	public Date getFirstDateOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, ForesightConstants.ZERO_INT);
		calendar.set(Calendar.DATE, ForesightConstants.ONE);
		return calendar.getTime();
	}

	/**
	 * Gets the date from string.
	 *
	 * @param stringEndDate
	 *            the string end date
	 * @return the date from string
	 */
	public static Date getDateFromString(String stringEndDate) {
		DateFormat dateFormat = new SimpleDateFormat(NVDashboardConstants.DD_MM_YYYY);
		Date endDate = null;
		try {
			endDate = dateFormat.parse(stringEndDate);
		} catch (ParseException e) {
			// empty
		}
		return endDate;
	}

	/**
	 * Gets the percentage.
	 *
	 * @param val
	 *            the val
	 * @param maxVal
	 *            the max val
	 * @return the percentage
	 */
	public static Double getPercentage(Double val, Double maxVal) {
		try {
			Double outOf = (double) ForesightConstants.HUNDRED;
			if (maxVal != ForesightConstants.ZERO)
				return (outOf * val) / maxVal;
		} catch (NumberFormatException ne) {
			// empty
		}
		return (double) ForesightConstants.ZERO;
	}

}
