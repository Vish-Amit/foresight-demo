package com.inn.foresight.module.nv.report.optimizedImage;

import java.awt.Color;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.foresight.module.nv.report.RangeSlab;
import com.inn.foresight.module.nv.report.utils.ReportConstants;

public class ColorFinder implements ColorConstants {
	private static final Logger logger = LogManager.getLogger(ColorFinder.class);
	private final HashMap<Object, Object> hints;

	public ColorFinder() {
		hints = new HashMap<>();
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	}

	public static Color getDriveColor(List<RangeSlab> rangeSlabs, Double value) {
		try {
			if (value != null && rangeSlabs != null && !rangeSlabs.isEmpty()) {
				for (RangeSlab range : rangeSlabs) {
					if (range.getLowerLimit() != null && range.getUpperLimit() != null && 
							value > range.getLowerLimit() && value <= range.getUpperLimit()) {
							return Color.decode(range.getColorCode());
					}
					if (range.getLowerLimit() != null && range.getUpperLimit() == null && 
							value > range.getLowerLimit()) {
							return Color.decode(range.getColorCode());
					}
				}
			}
		} catch (Exception e) {
			logger.error("Error inside method getDriveColor  for range slabs {}, value {} ", rangeSlabs, value);
		}
		return Color.GRAY;
	}
	
		public static Color getMimoColor(Integer value) {
		logger.debug("Going to get the mimo color for value {} ",value);
		try {
			if (value.equals(0)) {
				return Color.BLACK;
			}else if(value.equals(1)){
				return Color.RED;
			}else if(value.equals(2)){
				return Color.BLUE;
			} else if(value.equals(3)){
				return Color.GREEN;
			} else if(value.equals(4)){
				return Color.ORANGE;
			}
		} catch (Exception e) {
			logger.error("Error inside method getDriveColor  for value {} ", value);
		}
		return Color.GRAY;
	}
	
		public static Color getCarrierAggregationColor(String value) {
//		logger.debug("Going to get the Carrier Aggregation color for value { } ",value);
		try {
			if ("LTE CA 2 CCs".equalsIgnoreCase(value)) {
				return Color.ORANGE;
			}else if("LTE CA 3 CCs".equalsIgnoreCase(value)){
				return Color.YELLOW;
			}else if("LTE CA 4 CCs".equalsIgnoreCase(value)){
				return Color.GREEN;
			}else if("LTE CA 5 CCs".equalsIgnoreCase(value)){
				return ColorConstants.LIGHTGREEN;
			}else if("LTE".equalsIgnoreCase(value)){
				return Color.RED;
			}else if("Disabled".equalsIgnoreCase(value)){
				return Color.BLACK;
			}
		} catch (Exception e) {
			logger.error("Error inside method getCarrierAggregationColor  for value {} ", value);
		}
		return Color.GRAY;
	}
	
	public static Color getServingSystemColor(String technology, String band, String networkType) {
		logger.debug("Going to get the Serving Sytem color for technology {} , band {}  , networkType {} ", technology,
				band, networkType);
		try {
			if (ReportConstants.TECHNOLOGY_LTE.equalsIgnoreCase(networkType)) {
				return getColorForLTETechnology(technology, band);
			}else if (ReportConstants.NETWORKTYPE_GSM.equalsIgnoreCase(networkType) || ReportConstants.NETWORKTYPE_UMTS.equalsIgnoreCase(networkType)) {
				return ColorConstants.BLACK;
			}
		} catch (Exception e) {
			logger.error("Error inside method getServingSystemColor " );
		}
		return Color.GRAY;
	}

	private static Color getColorForLTETechnology(String technology, String band) {
		if (ReportConstants.FDD.equalsIgnoreCase(technology)) {
			if ("850".equalsIgnoreCase(band)) {
				return ColorConstants.RED;
			} else if ("1800".equalsIgnoreCase(band)) {
				return ColorConstants.LIGHTGREEN;
			} else if ("2100".equalsIgnoreCase(band)) {
				return ColorConstants.SKYBLUE;
			}
		} else if (ReportConstants.TDD.equalsIgnoreCase(technology)) {
			if ("850".equalsIgnoreCase(band)) {
				return ColorConstants.YELLOW;
			} else if ("1800".equalsIgnoreCase(band)) {
				return ColorConstants.DARKGREEN;
			} else if ("2100".equalsIgnoreCase(band)) {
				return ColorConstants.DARKBLUE;
			}
		}
		return Color.GRAY;
	}

	public static void main(String[] args) {
		Color color = Color.GRAY;

		if (!Color.GRAY.equals(color)) {
			logger.info(" true ");
		} else {
			logger.info("false");
		}
	}
	
			public static Color getTechnologyColor(String tech) {
//		logger.debug("Going to get the mimo technology for value { } ",tech);
		try {
			if(tech!=null){
			if (ReportConstants.TECHNOLOGY_FDD.equalsIgnoreCase(tech)) {
				return Color.BLUE;
			}else if(ReportConstants.TECHNOLOGY_TDD.equalsIgnoreCase(tech)){
				return Color.RED;
			}
		  }
		} catch (Exception e) {
			logger.error("Error inside method getTechnologyColor  for value {} ",tech );
		}
		return Color.GRAY;
	}
}
