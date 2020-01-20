package com.inn.foresight.module.nv.report.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.foresight.core.generic.utils.Utils;

public class ZoomFinder {
	private static Logger logger = LogManager.getLogger(ZoomFinder.class);
	
	 private ZoomFinder() {
		    throw new IllegalStateException("ZoomFinder Utility class");
		  }

	public static int findZoom(double maxLat, double minLat, double maxLon, double minLon) {
		int zoomLevel = ReportConstants.INDEX_ZER0;
		zoomLevel = getZoom(maxLat - minLat, maxLon - minLon);
		logger.info(" zoomLevel " + zoomLevel);
		return zoomLevel;
	}

	private static int getZoom(double latDiff, double lngDiff) {
		int zoomLevel = ReportConstants.INDEX_FOURTEEN;
		try {
			double maxDiff = (Math.abs(lngDiff) > Math.abs(latDiff)) ? Math.abs(lngDiff) : Math.abs(latDiff);
			if (maxDiff < ReportConstants.THREE_SIX_ZER0 / Math.pow(ReportConstants.INDEX_TWO, ReportConstants.TWENTY)) {
				zoomLevel = ReportConstants.TWENTY_ONE;
			} else {
				zoomLevel = (int) (-ReportConstants.INDEX_ONE * ((Math.log(maxDiff) / Math.log(ReportConstants.INDEX_TWO)) - (Math.log(ReportConstants.THREE_SIX_ZER0) / Math.log(ReportConstants.INDEX_TWO))));
				if (zoomLevel < ReportConstants.INDEX_ONE) {
                    zoomLevel = ReportConstants.INDEX_ONE;
                }
			}
			zoomLevel = zoomLevel + ReportConstants.INDEX_THREE;
			if (zoomLevel > ReportConstants.INDEX_EIGHTEEN) {
                zoomLevel = ReportConstants.INDEX_EIGHTEEN;
            }
			if (zoomLevel < ReportConstants.INDEX_FIVE) {
                zoomLevel = ReportConstants.INDEX_FIVE;
            }

		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}
		return zoomLevel;
	}
}