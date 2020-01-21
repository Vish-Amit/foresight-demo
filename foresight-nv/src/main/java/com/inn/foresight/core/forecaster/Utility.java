package com.inn.foresight.core.forecaster;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.attachment.rest.impl.AttachmentRest;

public class Utility {
	private Utility() {

		super();
	}

	private static Logger logger = LogManager.getLogger(AttachmentRest.class);
	public static final Integer FIVE = 5;

	public static long convertDateIntoMilli(String date, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		long milliseconds = 0L;

		try {
			Date e = f.parse(date);
			milliseconds = e.getTime();
		} catch (ParseException arg5) {
			logger.error("Exception while convertDateIntoMilli {}", Utils.getStackTrace(arg5));
		}

		return milliseconds;
	}

	public static Date getDate(String date, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);

		try {
			return f.parse(date);
		} catch (ParseException arg3) {
			logger.error("Exception while convertDateIntoMilli {}", Utils.getStackTrace(arg3));

			return null;
		}
	}

	public static String getDateInString(Date date, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(date);
	}

	public static Date addDays(Date date, int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(FIVE, days);
		return cal.getTime();
	}

	public static Date subtractDays(Date date, int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(FIVE, -days);
		return cal.getTime();
	}

	public static String getDayofWeek(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat("EEE");
		return formatter.format(Long.valueOf(date.getTime()));
	}

	public static double calculateSD(double[] array) {
		DescriptiveStatistics stats = new DescriptiveStatistics();

		for (int i = 0; i < array.length; ++i) {
			stats.addValue(array[i]);
		}

		return stats.getStandardDeviation();
	}

	public static double findSecondLargest(double[] values) {
		double largest = values[0];
		double secondLargest = values[0];

		for (int i = 0; i < values.length; ++i) {
			if (values[i] > largest) {
				secondLargest = largest;
				largest = values[i];
			} else if (values[i] > secondLargest) {
				secondLargest = values[i];
			}
		}

		return secondLargest;
	}
}
