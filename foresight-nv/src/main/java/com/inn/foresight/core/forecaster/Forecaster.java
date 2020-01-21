package com.inn.foresight.core.forecaster;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.commons.lang.DateUtils;

public class Forecaster {
	public static final String FORECAST_LENGTH = "FORECAST_LENGTH";
	public static final String INCLUDE_HISTORY_IN_RESULT = "INCLUDE_HISTORY";
	public static final String WEEKEND_DAYS = "WEEKEND_DAYS";
	public static final String INPUT_DATE_FORMAT = "INPUT_DATE_FORMAT";
	public static final String OUTPUT_DATE_FORMAT = "OUTPUT_DATE_FORMAT";
	public static final String AGGREGATION = "AGGREGATION";
	public static final String DAILY = "DAILY";
	public static final String HOURLY = "HOURLY";

	/** The logger. */
	private Logger logger = LogManager.getLogger(Forecaster.class);

	
	public LinkedHashMap<String, Double> forecastL(LinkedHashMap<String, Double> history, Properties properties)
			throws ForecasterException {
		if (history != null && history.size() != 0) {
			if (properties != null && properties.size() != 0) {
				int forecastedLength = Integer.parseInt(properties.getProperty(FORECAST_LENGTH));
				String inputDateFormat = properties.getProperty(INPUT_DATE_FORMAT);
				String outputDateFormat = properties.getProperty(OUTPUT_DATE_FORMAT);
				boolean includeHistory = Boolean.valueOf(properties.getProperty(INCLUDE_HISTORY_IN_RESULT, "false"))
						.booleanValue();
				String aggregation = properties.getProperty(AGGREGATION);
				String weekends = properties.getProperty(WEEKEND_DAYS);
				logger.info(
						"forecastedLength:{}, inputDateFormat:{}, outputDateFormat:{},includeHistory:{},aggregation:{} , weekends:{}",
						forecastedLength, inputDateFormat, outputDateFormat, includeHistory, aggregation, weekends);

				if (inputDateFormat != null && !inputDateFormat.trim().equals("")) {
					LinkedHashMap forecasted_result = new LinkedHashMap();
					SimpleRegression simpleRegression = new SimpleRegression();
					long last_hist_date = 0L;
					Set dates = history.keySet();
					Iterator next_date = dates.iterator();

					while (next_date.hasNext()) {
						String secMaxSigma = (String) next_date.next();
						double value = ((Double) history.get(secMaxSigma)).doubleValue();
						last_hist_date = Utility.convertDateIntoMilli(secMaxSigma, inputDateFormat);
						simpleRegression.addData((double) last_hist_date, value);
					}

					if (includeHistory) {
						forecasted_result.putAll(history);
					}

					for (int arg16 = 1; arg16 <= forecastedLength; ++arg16) {
						long arg18 = 0;
						if (DAILY.equalsIgnoreCase(aggregation))
							arg18 = DateUtils.addDays(new Date(last_hist_date), arg16).getTime();
						else
							arg18 = DateUtils.addHours(new Date(last_hist_date), arg16).getTime();

						double output = simpleRegression.predict((double) arg18);
						String dateKey = Utility.getDateInString(new Date(arg18), outputDateFormat);
						forecasted_result.put(dateKey, Double.valueOf(output));
					}

					if (weekends != null) {
						double arg17 = this.calculateSecondHighestSigma(history, weekends, inputDateFormat);
						this.applyVarianceOnForecastedResult(forecasted_result, arg17);
					}
					return forecasted_result;
				} else {
					throw new ForecasterException("Date format can\'t be null or empty");
				}
			} else {
				throw new ForecasterException("Properties can\'t be null or empty");
			}
		} else {
			throw new ForecasterException("Historical data can\'t be null or empty");
		}
	}

	private double calculateSecondHighestSigma(LinkedHashMap<String, Double> history, String weekends,
			String dateFormat) {
		HashMap week_day_wise_data_points = new HashMap();
		Set dates = history.keySet();
		Iterator week_days = dates.iterator();

		String week_day;
		while (week_days.hasNext()) {
			String sd_values = (String) week_days.next();
			week_day = Utility.getDayofWeek(Utility.getDate(sd_values, dateFormat), "EEE");
			if (!weekends.contains(week_day)) {
				if (!week_day_wise_data_points.containsKey(week_day)) {
					ArrayList hist_data_points = new ArrayList();
					hist_data_points.add((Double) history.get(sd_values));
					week_day_wise_data_points.put(week_day, hist_data_points);
				} else {
					List hist_data_points1 = (List) week_day_wise_data_points.get(week_day);
					hist_data_points1.add((Double) history.get(sd_values));
				}
			}
		}

		ArrayList sd_values1 = new ArrayList();
		Set week_days1 = week_day_wise_data_points.keySet();
		Iterator hist_data_points2 = week_days1.iterator();

		while (hist_data_points2.hasNext()) {
			week_day = (String) hist_data_points2.next();
			List day_list = (List) week_day_wise_data_points.get(week_day);
			sd_values1.add(Double.valueOf(Utility
					.calculateSD(ArrayUtils.toPrimitive((Double[]) day_list.toArray(new Double[day_list.size()])))));
		}

		return Utility.findSecondLargest(
				ArrayUtils.toPrimitive((Double[]) sd_values1.toArray(new Double[sd_values1.size()])));
	}

	private void applyVarianceOnForecastedResult(LinkedHashMap<String, Double> org_forecasted_result, double variance) {
		Random random = new Random();
		Set keys = org_forecasted_result.keySet();
		Iterator arg6 = keys.iterator();

		while (arg6.hasNext()) {
			String date = (String) arg6.next();
			double value = ((Double) org_forecasted_result.get(date)).doubleValue() + random.nextGaussian() * variance;
			org_forecasted_result.put(date, Double.valueOf(value));
		}

	}
}
