package com.inn.foresight.module.nv.livedrive.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.livedrive.constants.LiveDriveConstant;
import com.inn.foresight.module.nv.livedrive.wrapper.TrackPosition;
import com.inn.foresight.module.nv.livedrive.wrapper.TrackSpan;
import com.inn.foresight.module.nv.livedrive.wrapper.TrackSpanSummaryWrapper;
import com.inn.foresight.module.nv.livedrive.wrapper.TrackSpanWrapper;

/**
 * @author Innoeye
 */
public class LiveDriveUtil extends LiveDriveConstant {

	
    double sum = 0;
   double temp = 0;
    int totalCount = 0;
	    double pdf = 0;
	 double cdf = 0;
	 static  DecimalFormat decimalFormat = new DecimalFormat("####.##");

	/** The logger. */
	private static Logger logger = LogManager.getLogger(LiveDriveUtil.class);

	/**
	 * Gets the week number for live drive.
	 *
	 * @param date
	 *            the date
	 * @return the week number for live drive
	 */
	public static int getWeekNumberForLiveDrive(Date date) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.setMinimalDaysInFirstWeek(4);
		return cal.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * Gets the twelve week before date.
	 *
	 * @return the twelve week before date
	 */
	public static Date getTwelveWeekBeforeDate() {
		GregorianCalendar dayBeforeThisWeek = new GregorianCalendar();
		int dayFromMonday = (dayBeforeThisWeek.get(Calendar.DAY_OF_WEEK) + 7 - Calendar.MONDAY) % 7;
		dayBeforeThisWeek.add(Calendar.DATE, -dayFromMonday - 77);
		dayBeforeThisWeek.set(Calendar.HOUR, 0);
		dayBeforeThisWeek.set(Calendar.AM_PM, 0);

		dayBeforeThisWeek.set(Calendar.MINUTE, 0);
		dayBeforeThisWeek.set(Calendar.SECOND, 0);
		dayBeforeThisWeek.set(Calendar.MILLISECOND, 0);
		return dayBeforeThisWeek.getTime();
	}

	/**
	 * Checks if is valid json object.
	 *
	 * @param detail
	 *            the detail
	 * @return true, if is valid json object
	 */
	public static boolean isValidJsonObject(String detail) {
		boolean valid = false;
		try {
			new JSONObject(detail);
			valid = true;
		} catch (JSONException ex) {
			valid = false;
		}
		return valid;
	}


	/**
	 * Gets the geography column list.
	 *
	 * @return the geography column list
	 */
	public static List<String> getGeoColumnList() {
		List<String> columnList = new ArrayList<>();
		columnList.add(GEOGRAPHY_CF + GEOGRAPHY_NAME);
		return columnList;
	}

	public static String getGeographyName(String response) {
		List<String> geographyList = getGeographyListFromResponse(response);
		String name = null;
		try {
			if (geographyList != null && geographyList.size() > ForesightConstants.ZERO_VALUE) {
				for (String geography : geographyList) {
					name = geography;
				}
			}
		} catch (Exception e) {
			logger.error("getting error in geograohy name {}", Utils.getStackTrace(e));
		}
		return name;
	}

	/**
	 * Get List of Geography.
	 * 
	 * @param response
	 * @return list
	 */
	public static List<String> getGeographyListFromResponse(String response) {
		List<String> geographyList = null;
		try {
			if (response != null) {
				geographyList = new ArrayList<>();
				List<List<String>> boundaryData = getBoundaryDataOfViewPort(response);
				logger.info("boundaryData size:{}", boundaryData.size());
				if (boundaryData.size() > ForesightConstants.ZERO_VALUE) {
					for (List<String> list : boundaryData) {
						geographyList.add(list.get(ForesightConstants.INDEX_ZERO));
					}
				}
			} else {
				logger.info("Response is null");
			}
		} catch (Exception e) {
			logger.error("getting error in coverting response  {}", Utils.getStackTrace(e));
		}
		return geographyList;
	}

	@SuppressWarnings("serial")
	public static List<List<String>> getBoundaryDataOfViewPort(String response) {
		List<List<String>> boundaryData = new ArrayList<>();
		try {
			return new Gson().fromJson(response, new TypeToken<List<List<String>>>() {
			}.getType());
		} catch (JsonSyntaxException e) {
			logger.error("Json Exception while getBoundaryDataOfViewPort:{}", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception while getBoundaryDataOfViewPort:{}", Utils.getStackTrace(e));
		}
		return boundaryData;
	}

	public static String convertJsonToCsv(TrackSpanSummaryWrapper spanSummaryWrapper) {
		StringBuilder liveDriveCsv = new StringBuilder();
		logger.info("size of the responded wrapper {}",spanSummaryWrapper.getTrackSpanWrappers().size());
		try {
			liveDriveCsv.append("{" + "\"result\"" + ": \"[");
			if (spanSummaryWrapper != null && spanSummaryWrapper.getTrackSpanWrappers() != null
					&& !spanSummaryWrapper.getTrackSpanWrappers().isEmpty()) {

				for (int i = 0; i < spanSummaryWrapper.getTrackSpanWrappers().size(); i++) {
					TrackSpanWrapper trackSpanWrapper = spanSummaryWrapper.getTrackSpanWrappers().get(i);
					logger.info("size of the responded trackSpanWrapper {}",trackSpanWrapper.getSpanlist().size());

					for (int j = 0; j < trackSpanWrapper.getSpanlist().size(); j++) {
						TrackSpan trackSpan = trackSpanWrapper.getSpanlist().get(j);
						logger.info("size of the responded trackSpan {}",trackSpan.getPositions().size());

						for (int k = 0; k < trackSpan.getPositions().size(); k++) {
							TrackPosition trackPosition = trackSpan.getPositions().get(k);
							liveDriveCsv.append("[");

							appendCommaSeparator(liveDriveCsv, trackSpanWrapper, trackSpan, k, trackPosition);

							liveDriveCsv.append(trackSpan.getMspanid() + "],");// 38
						}
					}
				}
				liveDriveCsv.replace(liveDriveCsv.length() - 2, liveDriveCsv.length(), "]");
			}
		} catch (Exception e) {
			logger.error("Exception inside convertJsonToCsv  {} ",Utils.getStackTrace(e));
		}
		liveDriveCsv.append("]\"}");
		return liveDriveCsv.toString();
	}

	private static void appendCommaSeparator(StringBuilder liveDriveCsv, TrackSpanWrapper trackSpanWrapper,
			TrackSpan trackSpan, int k, TrackPosition trackPosition) {
		if (trackPosition.getLat() != null)
			liveDriveCsv.append(
					String.valueOf(trackPosition.getLat()) + LiveDriveConstant.COMMA_SEPARATOR);// 0
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		if (trackPosition.getLng() != null)
			liveDriveCsv.append(
					String.valueOf(trackPosition.getLng()) + LiveDriveConstant.COMMA_SEPARATOR);// 1
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		liveDriveCsv.append(trackPosition.getCapturingTime() + LiveDriveConstant.COMMA_SEPARATOR);// 2

		if (trackPosition.getPci() != null)
			liveDriveCsv.append(decimalFormat.format(trackPosition.getPci())
					+ LiveDriveConstant.COMMA_SEPARATOR);// 3
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		if (trackPosition.getRssi() != null)
			liveDriveCsv.append(decimalFormat.format(trackPosition.getRssi())
					+ LiveDriveConstant.COMMA_SEPARATOR);// 4
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		if (trackPosition.getRsrp() != null)
			liveDriveCsv.append(decimalFormat.format(trackPosition.getRsrp())
					+ LiveDriveConstant.COMMA_SEPARATOR);// 5
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		if (trackPosition.getRsrq() != null)
			liveDriveCsv.append(decimalFormat.format(trackPosition.getRsrq())
					+ LiveDriveConstant.COMMA_SEPARATOR);// 6
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		if (trackPosition.getSinr() != null)
			liveDriveCsv.append(decimalFormat.format(trackPosition.getSinr())
					+ LiveDriveConstant.COMMA_SEPARATOR);// 7
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 8
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 9
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 10
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 11
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 12
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 13
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 14
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 15
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 16
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 17
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 18
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 19
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 20

		appendComma(liveDriveCsv, trackSpanWrapper, trackSpan, k, trackPosition);
	}

	private static void appendComma(StringBuilder liveDriveCsv, TrackSpanWrapper trackSpanWrapper, TrackSpan trackSpan,
			int k, TrackPosition trackPosition) {
		if (trackPosition.getDlAvg() != null)
			liveDriveCsv.append(decimalFormat.format(trackPosition.getDlAvg())
					+ LiveDriveConstant.COMMA_SEPARATOR);// 21
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		if (trackPosition.getUlAvg() != null)
			liveDriveCsv.append(decimalFormat.format(trackPosition.getUlAvg())
					+ LiveDriveConstant.COMMA_SEPARATOR);// 22
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		if (trackPosition.getPingValueHolder() != null
				&& trackPosition.getPingValueHolder().getAvgLatency() != null)
			liveDriveCsv
					.append(decimalFormat.format(trackPosition.getPingValueHolder().getAvgLatency())
							+ LiveDriveConstant.COMMA_SEPARATOR);// 23
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 24

		if (trackPosition.getEventLogStatus() != null
				&& trackPosition.getEventLogStatus().equalsIgnoreCase(LiveDriveConstant.FAIL))
			liveDriveCsv.append(LiveDriveConstant.ONE + LiveDriveConstant.COMMA_SEPARATOR);// 25
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		if (trackPosition.getEventLogStatus() != null
				&& trackPosition.getEventLogStatus().equalsIgnoreCase(LiveDriveConstant.PASS))
			liveDriveCsv.append(LiveDriveConstant.ONE + LiveDriveConstant.COMMA_SEPARATOR);// 26
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 27
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 28
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 29
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 30
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 31
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 32
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 33

		subAppendComma(liveDriveCsv, trackSpanWrapper, trackSpan, k, trackPosition);
	}

	private static void subAppendComma(StringBuilder liveDriveCsv, TrackSpanWrapper trackSpanWrapper,
			TrackSpan trackSpan, int k, TrackPosition trackPosition) {
		if (trackPosition.getScript() != null)
			liveDriveCsv.append(trackPosition.getScript() + LiveDriveConstant.COMMA_SEPARATOR);// 34
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		if (trackPosition.getCellId() != null)
			liveDriveCsv.append(trackPosition.getCellId() + LiveDriveConstant.COMMA_SEPARATOR);// 35
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		if (trackPosition.getNetworkProvider() != null)
			liveDriveCsv
					.append(trackPosition.getNetworkProvider() + LiveDriveConstant.COMMA_SEPARATOR);// 36
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		if (k == trackSpan.getPositions().size() - 1) {
			if (trackSpanWrapper.getStatus() != null)
				liveDriveCsv
						.append(trackSpanWrapper.getStatus() + LiveDriveConstant.COMMA_SEPARATOR);// 37
			else
				liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);
		} else {
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);
		}
	}

	public static String getSummaryCSV(TrackSpanSummaryWrapper spanSummaryWrapper) {

		StringBuilder liveDriveCsv = new StringBuilder();
		try {
			liveDriveCsv.append("{" + "\"result\"" + ": \"[");
			if (spanSummaryWrapper != null && spanSummaryWrapper.getTrackSpanWrappers() != null
					&& !spanSummaryWrapper.getTrackSpanWrappers().isEmpty()) {
				int tswSize = spanSummaryWrapper.getTrackSpanWrappers().size() - 1;
				TrackSpanWrapper trackSpanWrapper = spanSummaryWrapper.getTrackSpanWrappers().get(tswSize);
				int tsSize = trackSpanWrapper.getSpanlist().size() - 1;
				TrackSpan trackSpan = trackSpanWrapper.getSpanlist().get(tsSize);
				int tpSize = trackSpan.getPositions().size() - 1;
				TrackPosition trackPosition = trackSpan.getPositions().get(tpSize);

				getSummaryCsvCommaSeparator(liveDriveCsv, trackSpanWrapper, trackPosition);
			}
		} catch (Exception e) {
			logger.error("Exception inside getSummaryCSV  {} ",Utils.getStackTrace(e));
		}
		liveDriveCsv.append("]\"}");
		return liveDriveCsv.toString();
	}

	private static void getSummaryCsvCommaSeparator(StringBuilder liveDriveCsv, TrackSpanWrapper trackSpanWrapper,
			TrackPosition trackPosition) {
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 0
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 1
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 2
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 3
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 4
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 5
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 6
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 7
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 8
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 9
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 10
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 11
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 12
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 13
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 14
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 15
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 16
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 17
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 18
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 19
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 20
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 21
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 22
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 23
		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 24
		commaAppender(liveDriveCsv, trackSpanWrapper, trackPosition);
	}

	private static void commaAppender(StringBuilder liveDriveCsv, TrackSpanWrapper trackSpanWrapper,
			TrackPosition trackPosition) {
		if (trackSpanWrapper.getNvAppVersion() != null)
			liveDriveCsv.append(trackSpanWrapper.getNvAppVersion() + LiveDriveConstant.COMMA_SEPARATOR);// 25
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		if (trackSpanWrapper.getManufactureBy() != null)
			liveDriveCsv.append(trackSpanWrapper.getManufactureBy() + LiveDriveConstant.COMMA_SEPARATOR);// 26
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		if (trackSpanWrapper.getModel() != null)
			liveDriveCsv.append(trackSpanWrapper.getModel() + LiveDriveConstant.COMMA_SEPARATOR);// 27
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		if (trackPosition.getNetworkType() != null)
			liveDriveCsv.append(trackPosition.getNetworkType() + LiveDriveConstant.COMMA_SEPARATOR);// 28
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		kpiCommaSeparator(liveDriveCsv, trackSpanWrapper, trackPosition);
	}

	private static void kpiCommaSeparator(StringBuilder liveDriveCsv, TrackSpanWrapper trackSpanWrapper,
			TrackPosition trackPosition) {
		if (trackPosition.getNetworkProvider() != null)
			liveDriveCsv.append(trackPosition.getNetworkProvider() + LiveDriveConstant.COMMA_SEPARATOR);// 29
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		if (trackSpanWrapper.getAndroidOs() != null)
			liveDriveCsv.append(trackSpanWrapper.getAndroidOs() + LiveDriveConstant.COMMA_SEPARATOR);// 30
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		if (trackPosition.getCellId() != null)
			liveDriveCsv.append(trackPosition.getCellId() + LiveDriveConstant.COMMA_SEPARATOR);// 31
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		if (trackPosition.getBand() != null)
			liveDriveCsv.append(trackPosition.getBand() + LiveDriveConstant.COMMA_SEPARATOR);// 32
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 33

		if (trackPosition.getScript() != null)
			liveDriveCsv.append(trackPosition.getScript() + LiveDriveConstant.COMMA_SEPARATOR);// 34
		else
			liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);

		liveDriveCsv.append(LiveDriveConstant.COMMA_SEPARATOR);// 35
	}
	public static String createIntoJson(String msg, Object value) {
		String json = null;
		if (msg != null && value != null) {
			json = "{\n" + "\"result\" : \"" + msg + value.toString() + "\"\n" + "}";
		} else {
			json = "{\n" + "\"result\" : \"" + msg + " " + null + "\"\n" + "}";
		}
		return json;
	}
}
