package com.inn.foresight.module.nv.report.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.inn.commons.lang.NumberUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.report.RangeSlab;
import com.inn.foresight.module.nv.report.optimizedImage.ColorFinder;
import com.inn.foresight.module.nv.report.optimizedImage.ImageCreator;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.PCIWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;

import javax.imageio.ImageIO;

/** The Class LegendUtil. */
public class LegendUtil {

	/** The hints. */
	private static HashMap<Object, Object> hints;
	private static String errorInWritingDataInImage = "Error in writing data in legend image {}";
	private static String logRangeSlabSize = "RangeSlab Size for kpiName {} , is {} ";
	private static String errorImsideMethodwriteDataInImage = "Exception inside method writeDataInImage {} ";
	private static String errorGettingPciMapValue = "error in getting values from PCIMap {} ";
	private static String exceptionInFetchingDataFromEarcnMap = "Exception occured in fetching data from earfcnCountMap {}";
	private static String exceptionInWritingRangeWiseData = "Exception in drawing Bubble on legend image {} ";

	/** The logger. */
	private static Logger logger = LogManager.getLogger(LegendUtil.class);

	private LegendUtil() {
	}

	static {
		hints = new HashMap<>();
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		hints.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);

	}

	static DecimalFormat df = new DecimalFormat(ReportConstants.HASH_DOT_HASHHASH);

	/**
	 * Populate KPI Legends data.
	 *
	 * @param list          the list
	 * @param indexTestType
	 * @param dataKPIs
	 * @return the list of KPiWrapper
	 */
	public static List<KPIWrapper> populateLegendData(List<KPIWrapper> list, List<String[]> dataKPIs,
			Integer indexTestType) {
		logger.debug("Inside method populateLegendData for list of Size {} ", list != null ? list.size() : null);
//		logger.debug("DATA before populating {} ", new Gson().toJson(list));
//		List<KPIWrapper> kpiStatsNullList = list.stream().filter(kpistatsData -> kpistatsData.getKpiStats() == null)
//				.collect(Collectors.toList());
//		list = list.stream().filter(kpistatsData -> kpistatsData.getKpiStats() != null).collect(Collectors.toList());
		for (KPIWrapper wrapper : list) {
			logger.debug("Going to set the stats for kpi Name {}", wrapper.getKpiName());
			if (wrapper.getRangeSlabs() != null) {
				List<RangeSlab> rangeList = wrapper.getRangeSlabs();
				rangeList.sort(Comparator.comparing(RangeSlab::getLowerLimit).reversed());

				for (int i = 0; i < rangeList.size(); i++) {
					try {
//						List<String> kpiStatList = Arrays.asList(wrapper.getKpiStats()).stream()
//								.collect(Collectors.toList());
						getRangeWiseCountByKpi(dataKPIs, wrapper, rangeList.get(i), i, rangeList.size(), indexTestType);
					} catch (Exception e) {
						logger.error("Exception inside method populateLegendData for  rslab {} , {}  ",
								rangeList.get(i), Utils.getStackTrace(e));
					}
				}
				if(ReportConstants.MOS.equalsIgnoreCase(wrapper.getKpiName())){
					addRangeSlabForGrayMosValues(rangeList, dataKPIs, wrapper.getIndexKPI());
				}
				try {
					Integer totalCount = wrapper.getRangeSlabs().stream()
							.filter(rangeSlab -> rangeSlab.getCount() != null).mapToInt(RangeSlab::getCount).sum();
					wrapper.setTotalCount(totalCount);
				} catch (Exception e) {
					logger.error("Exception inside the populateLegendData  for kpi Name {} , logs {} ",
							wrapper.getKpiName(), e.getMessage());
				}
			}
		}
//		list.addAll(kpiStatsNullList);
		logger.debug("DATA After populating {} ", new Gson().toJson(list));
		return list;
	}

	private static void addRangeSlabForGrayMosValues(List<RangeSlab> rangeList, List<String[]> dataKPIs, Integer indexMos) {
		Long noDataList = dataKPIs.stream().filter(Objects::nonNull)
											.map(e -> indexMos < e.length ? e[indexMos] : null)
											.filter(e -> e.trim().length() <= 0).count();

		RangeSlab noDataRangeSlab = new RangeSlab();
		noDataRangeSlab.setColorCode("#808080");
		noDataRangeSlab.setCount(noDataList != null ? noDataList.intValue() : null);
		rangeList.add(noDataRangeSlab);
	}

	private static void getRangeWiseCountByKpi(List<String[]> dataKPIs, KPIWrapper wrapper, RangeSlab rslab,
			Integer index, Integer size, Integer indexTestType) {

		if (DriveHeaderConstants.DL_THROUGHPUT.equalsIgnoreCase(wrapper.getKpiName())) {
			 getRangeWiseCount(rslab, dataKPIs, wrapper.getIndexKPI(),index,size);
		}else if(ReportConstants.FTP_DL_THROUGHPUT.equalsIgnoreCase(wrapper.getKpiName())){
			getRangeWiseCount(rslab, dataKPIs, wrapper.getIndexKPI(),ReportConstants.FTP_DOWNLOAD, indexTestType);
		}else if(ReportConstants.HTTP_DL_THROUGHPUT.equalsIgnoreCase(wrapper.getKpiName())){
			getRangeWiseCount(rslab, dataKPIs, wrapper.getIndexKPI(),ReportConstants.HTTP_DOWNLOAD, indexTestType);
		}else if (DriveHeaderConstants.UL_THROUGHPUT.equalsIgnoreCase(wrapper.getKpiName())) {
			getRangeWiseCount(rslab, dataKPIs, wrapper.getIndexKPI(),index,size);
		}else if(ReportConstants.FTP_UL_THROUGHPUT.equalsIgnoreCase(wrapper.getKpiName())){
			getRangeWiseCount(rslab, dataKPIs, wrapper.getIndexKPI(),ReportConstants.FTP_UPLOAD, indexTestType);
		}else if(ReportConstants.HTTP_UL_THROUGHPUT.equalsIgnoreCase(wrapper.getKpiName())){
			getRangeWiseCount(rslab, dataKPIs, wrapper.getIndexKPI(),ReportConstants.HTTP_UPLOAD, indexTestType);
		}else if(ReportConstants.HTTP_DL_RSRP.equalsIgnoreCase(wrapper.getKpiName())){
			getRangeWiseCount(rslab, dataKPIs, wrapper.getIndexKPI(),ReportConstants.HTTP_DOWNLOAD, indexTestType);
		}else if(ReportConstants.FTP_DL_RSRP.equalsIgnoreCase(wrapper.getKpiName())){
			getRangeWiseCount(rslab, dataKPIs, wrapper.getIndexKPI(),ReportConstants.FTP_DOWNLOAD ,indexTestType);
		}else if(ReportConstants.FTP_UL_RSRP.equalsIgnoreCase(wrapper.getKpiName())){
			getRangeWiseCount(rslab, dataKPIs, wrapper.getIndexKPI(),ReportConstants.FTP_UPLOAD, indexTestType);
		}else if(ReportConstants.HTTP_UL_RSRP.equalsIgnoreCase(wrapper.getKpiName())){
			getRangeWiseCount(rslab, dataKPIs, wrapper.getIndexKPI(),ReportConstants.HTTP_UPLOAD, indexTestType);
		}else if(ReportConstants.HTTP_DL_SINR.equalsIgnoreCase(wrapper.getKpiName())){
			getRangeWiseCount(rslab, dataKPIs, wrapper.getIndexKPI(),ReportConstants.HTTP_DOWNLOAD, indexTestType);
		}else if(ReportConstants.FTP_DL_SINR.equalsIgnoreCase(wrapper.getKpiName())){
			getRangeWiseCount(rslab, dataKPIs, wrapper.getIndexKPI(),ReportConstants.FTP_DOWNLOAD, indexTestType);
		}else if(ReportConstants.FTP_UL_SINR.equalsIgnoreCase(wrapper.getKpiName())){
			getRangeWiseCount(rslab, dataKPIs, wrapper.getIndexKPI(),ReportConstants.FTP_UPLOAD, indexTestType);
		}else if(ReportConstants.HTTP_UL_SINR.equalsIgnoreCase(wrapper.getKpiName())){
			getRangeWiseCount(rslab, dataKPIs, wrapper.getIndexKPI(),ReportConstants.HTTP_UPLOAD, indexTestType);
		}else if (ReportConstants.PDSCH_THROUGHPUT.equalsIgnoreCase(wrapper.getKpiName())) {
			getRangeWiseCount(rslab, dataKPIs, wrapper.getIndexKPI(),index,size);
		}else if (ReportConstants.PUSCH_THROUGHPUT.equalsIgnoreCase(wrapper.getKpiName())) {
			getRangeWiseCount(rslab, dataKPIs, wrapper.getIndexKPI(),index,size);
		}else if(wrapper.getIndexKPI() != null){
			//getRangeWiseCount(rslab, kpiStatList, wrapper);
			getRangeWiseCount(rslab, dataKPIs, wrapper.getIndexKPI(), index, size);
		} else {
			logger.debug("Unable to calculate the stats for kpi {} ", wrapper.getKpiName());
		}
	}

	private static RangeSlab getRangeWiseCount(RangeSlab rslab, List<String[]> driveData, Integer indexDl,
			String testType, Integer indexTestType) {
		logger.debug("Inside method getRangeWiseCount for rsalb {} , indexdl {} ,testType {} ", rslab, indexDl,
				testType);
		try {
			List<Double> filterdList = driveData.stream().filter(Objects::nonNull)
					.filter(e -> testType.equalsIgnoreCase(e[indexTestType]))
					.map(e -> indexDl < e.length ? e[indexDl] : null).filter(e -> e != null && e.trim().length() > 0)
					.map(Double::valueOf).filter(val -> (val > rslab.getLowerLimit() && val <= rslab.getUpperLimit()))
					.collect(Collectors.toList());
			rslab.setCount(filterdList.size());
			logger.debug("Size for testType {} ,indexdl {} , is {} ", testType, indexDl, filterdList.size());
			return rslab;
		} catch (Exception e) {
			logger.warn("Unable to to calcualte the range count of index {} , {}  ", indexDl, e.getMessage());
		}
		return rslab;
	}

	private static RangeSlab getRangeWiseCount(RangeSlab rslab, List<String[]> dataKPIs, Integer kpiIndex,
			Integer index, Integer size) {
		logger.debug("Inside method getRangeWiseCount for rsalb {} , indexdl {} ", rslab, kpiIndex);
		try {
			if (index.equals((size - 1))) {
				logger.info("inside last value {}", kpiIndex);
				List<Double> filterdList = dataKPIs.stream().filter(Objects::nonNull)
						.map(e -> kpiIndex < e.length ? e[kpiIndex] : null)
						.filter(e -> e != null && e.trim().length() > 0).map(Double::valueOf)
						.filter(val -> (rslab.getUpperLimit() != null ? (val >= rslab.getLowerLimit() && val <= rslab.getUpperLimit()) : val >= rslab.getLowerLimit()))
						.collect(Collectors.toList());
				rslab.setCount(filterdList.size());
			} else {
				List<Double> filterdList = dataKPIs.stream().filter(Objects::nonNull)
						.map(e -> kpiIndex < e.length ? e[kpiIndex] : null)
						.filter(e -> e != null && e.trim().length() > 0).map(Double::valueOf)
						.filter(val -> (rslab.getUpperLimit() != null ? (val >= rslab.getLowerLimit() && val <= rslab.getUpperLimit()) : val >= rslab.getLowerLimit()))
						.collect(Collectors.toList());
				rslab.setCount(filterdList.size());
				logger.debug("Size for indexdl {} , is {} ", kpiIndex, filterdList.size());

			}
			return rslab;
		} catch (Exception e) {
			logger.warn("Exception inside method getRangeWiseCount msg {} ", e.getMessage());
		}
		return rslab;
	}

	private static RangeSlab getRangeWiseCount(RangeSlab rslab, List<String> kpiStatList, KPIWrapper wrapper) {
		try {
			Double startIndex = rslab.getLowerLimit() - wrapper.getMinValue();
			Double endIndex = rslab.getUpperLimit() - (wrapper.getMinValue().intValue());
			List<String> subList = kpiStatList.subList(startIndex.intValue(), endIndex.intValue());
			List<Integer> slabList = subList.stream().map(Integer::valueOf).collect(Collectors.toList());
			Integer sumOfcount = slabList.stream().mapToInt(i -> i).sum();
			rslab.setCount(sumOfcount);
		} catch (Exception e) {
			logger.debug("Exception inside method getRangeWiseCount {} ", e.getMessage());
		}
		return rslab;
	}

	/**
	 * Get the legend image.
	 *
	 * @param total   the total
	 * @param kpiName the kpi name
	 * @param list    the list
	 * @return the legend image for given KPi
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static BufferedImage getLegendImage(Integer total, String kpiName, List<RangeSlab> list) {
		logger.debug("Inside method getLegendImage for kpi {} ", kpiName);
		int size = list.size();
		BufferedImage image = new BufferedImage(ReportConstants.FIVE_ONE_TWO, ReportConstants.TWO_HUNDRED_TWENTY,
				BufferedImage.TYPE_INT_RGB);
		DecimalFormat df = new DecimalFormat(ReportConstants.HASH_DOT_HASHHASH);
		try {
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			writeHeading(kpiName, size, graphics);
			int j = ReportConstants.INDEX_THIRTY;
			int i = ReportConstants.INDEX_THIRTY;
			writeDataRangeSlabwise(total, kpiName, list, df, graphics, j, i);
		} catch (Exception e) {
			logger.error("Exception inside method getLegendImage {} ", Utils.getStackTrace(e));
		}
		return image;
	}

	private static void writeDataRangeSlabwise(Integer total, String kpiName, List<RangeSlab> list, DecimalFormat df,
			Graphics2D graphics, int j, int i) {
		try {
			logger.debug(logRangeSlabSize, list.size(), kpiName);
			for (RangeSlab rangeSlab : list) {
				if (rangeSlab != null) {
					j = writeDataInImage(rangeSlab, total, i, j, graphics, df, kpiName);
				}
			}
		} catch (Exception e) {
			logger.error("Error in method writeDataRangeSlabwise {} ", Utils.getStackTrace(e));
		}
	}

	/**
	 * Write data in image.
	 *
	 * @param rangeSlab the range slab
	 * @param total     the total
	 * @param i         the i
	 * @param j         the j
	 * @param graphics  the graphics
	 * @param df        the df
	 * @param kpiName
	 * @return the int
	 */
	private static int writeDataInImage(RangeSlab rangeSlab, Integer total, int i, int j, Graphics2D graphics,
			DecimalFormat df, String kpiName) {
		String range = null;
		j = j + ReportConstants.FIFTEEN;
		try {
			graphics.setColor(Color.decode(rangeSlab.getColorCode()));
			graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
					ReportConstants.TEN);
			graphics.setColor(Color.BLACK);
			if (kpiName.contains("2GHZ")) {
				kpiName = kpiName.replace("2GHZ", "(2.4 GHz)").replace("WIFI ", "");
			} else if (kpiName.contains("5GHZ")) {
				kpiName = kpiName.replace("5GHZ", "(5 GHz)").replace("WIFI ", "");
			}
			/*String range = rangeSlab.getLowerLimit() + ReportConstants.LESS_THAN_SYMBOL + ForesightConstants.EQUALS
					+ ReportConstants.SPACE + kpiName.replace(ReportConstants.THROUGHPUT, ReportConstants.THP)
					+ ReportConstants.SPACE + ReportConstants.LESS_THAN_SYMBOL + rangeSlab.getUpperLimit();
		*/	
			if(rangeSlab.getUpperLimit()!=null) {
	             range = rangeSlab.getUpperLimit() + ReportConstants.LESS_THAN_SYMBOL + ForesightConstants.EQUALS
					+ ReportConstants.SPACE + kpiName.replace(ReportConstants.THROUGHPUT, ReportConstants.THP)
					+ ReportConstants.SPACE + ReportConstants.GREATER_THAN + rangeSlab.getLowerLimit();
			} else {
				if(rangeSlab.getLowerLimit() != null) {
					range = kpiName.replace(ReportConstants.THROUGHPUT, ReportConstants.THP) + ReportConstants.SPACE + ReportConstants.GREATER_THAN + rangeSlab.getLowerLimit();
				} else {
					range = "NO DATA AVAILABLE";
				}
			}
		
			
			if (rangeSlab.getCount() != null && total != 0 && (rangeSlab.getCount() != ReportConstants.INDEX_ZER0)) {
				graphics.drawString(range + ReportConstants.DOUBLE_SPACE + ReportConstants.OPEN_BRACKET
						+ rangeSlab.getCount() + ReportConstants.CLOSED_BRACKET + ReportConstants.DOUBLE_SPACE
						+ df.format((rangeSlab.getCount() * ReportConstants.INDEX_HUNDRED) / (total * 1.0))
						+ ReportConstants.PERCENT, i, j);
			} else {
				graphics.drawString(range + ReportConstants.DOUBLE_SPACE + ReportConstants.OPEN_BRACKET
						+ ReportConstants.INDEX_ZER0 + ReportConstants.CLOSED_BRACKET + ReportConstants.DOUBLE_SPACE
						+ ReportConstants.ZERO_DOT_ZEROZERO + ReportConstants.PERCENT, i, j);
			}
		} catch (Exception e) {
			logger.error("Exception occured inside method writeDataInImage {} ", Utils.getStackTrace(e));
		}
		return j;
		}

	/**
	 * Write data in image.
	 *
	 * @param rangeSlab the range slab
	 * @param total     the total
	 * @param i         the i
	 * @param j         the j
	 * @param graphics  the graphics
	 * @param kpiName
	 * @return the int
	 */
	private static int writeDataInImage(RangeSlab rangeSlab, int i, int j, Graphics2D graphics, String kpiName) {
		j = j + ReportConstants.IB_BENCHMARK_LEGEND_Y_SPACING;
		try {
			graphics.setColor(Color.decode(rangeSlab.getColorCode()));
			graphics.fillOval(i - ReportConstants.TWENTY, j - 17, ReportConstants.TWENTY, ReportConstants.TWENTY);
			graphics.setColor(Color.BLACK);
			if (kpiName.contains("2GHZ")) {
				kpiName = kpiName.replace("2GHZ", "(2.4 GHz)").replace("WIFI ", "");
			} else if (kpiName.contains("5GHZ")) {
				kpiName = kpiName.replace("5GHZ", "(5 GHz)").replace("WIFI ", "");
			}
			String range = rangeSlab.getUpperLimit() + ReportConstants.LESS_THAN_SYMBOL + ForesightConstants.EQUALS
					+ ReportConstants.SPACE + kpiName.replace("THROUGHPUT", "THP") + ReportConstants.SPACE
					+ ReportConstants.GREATER_THAN + rangeSlab.getLowerLimit();
			graphics.setFont(new Font(ReportConstants.DEFAULT, Font.PLAIN, 18));
			graphics.drawString(range, i + 5, j);
		} catch (Exception e) {
			logger.error(errorImsideMethodwriteDataInImage, Utils.getStackTrace(e));
		}
		return j;
	}

	private static void writePciLegendData(Integer total, Map<Integer, PCIWrapper> pCIMap, DecimalFormat df,
			Graphics2D graphics, int j, int i, int col) {
		try {
			pCIMap = getSortedMapByCount(pCIMap);
			Map<Integer, PCIWrapper> pciRevereMap = reverseMap(pCIMap);
			for (Map.Entry<Integer, PCIWrapper> entry : pciRevereMap.entrySet()) {
				Integer pciCountzero = entry.getValue().getCount();
				if ((pciCountzero != ReportConstants.INDEX_ZER0) && pciCountzero != null) {
					++col;
					drawBubble(total, df, graphics, j, entry, i);
//					i += ReportConstants.ONE_HUNDRED_FIFTEEN;
					i += 150;
					if ((col % ReportConstants.INDEX_THREE) == 0) {
						i = ReportConstants.INDEX_THIRTY;
						j += ReportConstants.TEN;
//						j += ReportConstants.TWENTY;
					}
				}
			}
		} catch (Exception e) {
			logger.error(errorGettingPciMapValue, Utils.getStackTrace(e));
		}
	}

	private static Map<Integer, PCIWrapper> reverseMap(Map<Integer, PCIWrapper> toReverseMap) {
		LinkedHashMap<Integer, PCIWrapper> reveredMap = new LinkedHashMap<>();
		try {
			List<Integer> reverseOrderKeys = new ArrayList<>(toReverseMap.keySet());
			Collections.reverse(reverseOrderKeys);
			reverseOrderKeys.forEach(key -> reveredMap.put(key, toReverseMap.get(key)));
			return reveredMap;
		} catch (Exception e) {
			logger.error("Exception in reversing the Pci Map ", e.getMessage());
		}
		return toReverseMap;
	}

	private static Map<Integer, PCIWrapper> getSortedMapByCount(Map<Integer, PCIWrapper> pCIMap) {
		if (pCIMap != null && pCIMap.size() > 0) {
			return pCIMap.entrySet().stream()
					.sorted((key, wrapper) -> key.getValue().getCount().compareTo(wrapper.getValue().getCount()))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (key, wrapper) -> key,
							LinkedHashMap::new));
		}
		return pCIMap;
	}

	/**
	 * Draw bubble.
	 *
	 * @param total    the total
	 * @param df       the df
	 * @param graphics the graphics
	 * @param j        the j
	 * @param entry    the entry
	 * @param i
	 * @return the next Line Index value
	 * @throws Exception the exception
	 */
	private static int drawBubble(Integer total, DecimalFormat df, Graphics2D graphics, int j,
			Map.Entry<Integer, PCIWrapper> entry, int i) {
		try {
			graphics.setColor(entry.getValue().getColor());
			graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
					ReportConstants.TEN);
			graphics.setColor(Color.BLACK);
			String pci = Integer.toString(entry.getValue().getPCI());
			if (pci.length() < ReportConstants.INDEX_THREE) {
				pci = StringUtils.leftPad(pci, ReportConstants.INDEX_THREE, "0");
			}
			if (total != ReportConstants.INDEX_ZER0 && (entry.getValue().getCount() != ReportConstants.INDEX_ZER0)) {
				graphics.drawString(pci + ReportConstants.DOUBLE_SPACE + ReportConstants.OPEN_BRACKET
						+ entry.getValue().getCount() + ReportConstants.CLOSED_BRACKET + ReportConstants.DOUBLE_SPACE
						+ df.format((entry.getValue().getCount() * ReportConstants.INDEX_HUNDRED) / (total * 1.0))
						+ ReportConstants.PERCENT, i, j);
			} else {
				graphics.drawString(pci + ReportConstants.DOUBLE_SPACE + ReportConstants.OPEN_BRACKET
						+ entry.getValue().getCount() + ReportConstants.CLOSED_BRACKET + ReportConstants.DOUBLE_SPACE
						+ ReportConstants.ZERO_DOT_ZEROZERO + ReportConstants.PERCENT, i, j);
			}
		} catch (Exception e) {
			logger.error("Exception in drawBubble method {} ", Utils.getStackTrace(e));
		}
		return j;
	}

	/**
	 * Write kpiName as heading on legeng Image.
	 *
	 * @param kpiName  the kpi name
	 * @param fontSize the size
	 * @param graphics the graphics
	 * @throws Exception the exception
	 */
	public static void writeHeading(String kpiName, int size, Graphics2D graphics) throws Exception {
		try {
			logger.info("size is {} ", size);
			graphics.setColor(Color.WHITE);
			graphics.fillRect(ReportConstants.INDEX_ZER0, ReportConstants.INDEX_ZER0, ReportConstants.FIVE_ONE_TWO,
					ReportConstants.TWO_HUNDRED_TWENTY);
			graphics.addRenderingHints(hints);
			graphics.setColor(Color.BLACK);
			graphics.setFont(new Font(ReportConstants.FONT_TIMES_NEW_ROMAN, Font.PLAIN, ReportConstants.FIFTEEN));
			graphics.drawString(kpiName, ReportConstants.TEN, ReportConstants.FIFTEEN);
			graphics.setFont(new Font(ReportConstants.FONT_TIMES_NEW_ROMAN, Font.PLAIN, ReportConstants.TEN));
		} catch (NullPointerException e) {
			logger.error("NullPointerException inside method writeHeading {} ", e.getMessage());
		} catch (Exception e) {
			logger.error("Exception inside method writeHeading {} ", e.getMessage());
		}
	}

	public static BufferedImage getCustomImage(Integer total, Map<Integer, PCIWrapper> pciCountMap,
			Map<String, Long> earfcnCountMap, String key, Map<String, Color> earfcnColorMap) {
		int mapSize = 0;
		if (key.equalsIgnoreCase(ReportConstants.LTE_SPACE_PCI) || key.equalsIgnoreCase(DriveHeaderConstants.CGI)) {
			mapSize = pciCountMap.size();
		} else if (key.equalsIgnoreCase(ReportConstants.EARFCN) && earfcnCountMap != null) {
			mapSize = earfcnCountMap.size();
		}

		BufferedImage image = new BufferedImage(ReportConstants.FIVE_ONE_TWO, ReportConstants.TWO_HUNDRED_TWENTY,
				BufferedImage.TYPE_INT_RGB);
		DecimalFormat df = new DecimalFormat(ReportConstants.HASH_DOT_HASHHASH);
		try {
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			writeHeading(key, mapSize, graphics);
			int j = ReportConstants.INDEX_THIRTY;
			int i = ReportConstants.INDEX_THIRTY;
			int col = ReportConstants.INDEX_ZER0;
			if (key.equalsIgnoreCase(ReportConstants.LTE_SPACE_PCI)) {
				writePciLegendData(total, pciCountMap, df, graphics, j, i, col);
			} else if (key.equalsIgnoreCase(DriveHeaderConstants.CGI)) {
				if (!pciCountMap.isEmpty()) {
					total = pciCountMap.values().stream().mapToInt(PCIWrapper::getCount).sum();
				}
				writePciLegendData(total, pciCountMap, df, graphics, j, i, col);
			} else if (key.equalsIgnoreCase(ReportConstants.EARFCN) && earfcnCountMap != null) {
				writeEarfcnLegendData(earfcnCountMap, df, graphics, j, i, col, earfcnColorMap);
			}
		} catch (Exception e) {
			logger.error(errorGettingPciMapValue, Utils.getStackTrace(e));
		}
		return image;
	}

	private static void writeEarfcnLegendData(Map<String, Long> earfcnCountMap, DecimalFormat df, Graphics2D graphics,
			int j, int i, int col, Map<String, Color> earfcnColorMap) {
		try {
			earfcnCountMap.remove("");
			earfcnCountMap.remove(" ");
			Long totalCount = earfcnCountMap.values().stream().mapToLong(Number::longValue).sum();
			for (Entry<String, Long> entry : earfcnCountMap.entrySet()) {
				++col;
				try {
					graphics.setColor(earfcnColorMap.get(entry.getKey()));
					graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
							ReportConstants.TEN);
					graphics.setColor(Color.BLACK);
					if (totalCount != ReportConstants.INDEX_ZER0 && (entry.getValue() != ReportConstants.INDEX_ZER0)) {
						graphics.drawString(entry.getKey() + ReportConstants.DOUBLE_SPACE + ReportConstants.OPEN_BRACKET
								+ entry.getValue() + ReportConstants.CLOSED_BRACKET + ReportConstants.DOUBLE_SPACE
								+ df.format((entry.getValue() * ReportConstants.INDEX_HUNDRED) / (totalCount * 1.0))
								+ ReportConstants.PERCENT, i, j);
					} else {
						graphics.drawString(entry.getKey() + ReportConstants.DOUBLE_SPACE + ReportConstants.OPEN_BRACKET
								+ entry.getValue() + ReportConstants.CLOSED_BRACKET + ReportConstants.DOUBLE_SPACE
								+ ReportConstants.ZERO_DOT_ZEROZERO + ReportConstants.PERCENT, i, j);
					}
				} catch (Exception e) {
					logger.error(exceptionInWritingRangeWiseData, Utils.getStackTrace(e));
				}
				i += ReportConstants.ONE_HUNDRED_SEVENTY;
				if ((col % ReportConstants.INDEX_THREE) == 0) {
					i = ReportConstants.INDEX_THIRTY;
					j += ReportConstants.TEN;
				}
			}
		} catch (Exception e) {
			logger.error(exceptionInFetchingDataFromEarcnMap, Utils.getStackTrace(e));
		}
	}

	public static BufferedImage getLegendImage(String kpiName, String[] kpiStats) {
		BufferedImage image = new BufferedImage(ReportConstants.FIVE_ONE_TWO, ReportConstants.TWO_HUNDRED_TWENTY,
				BufferedImage.TYPE_INT_RGB);
		try {
			logger.debug("inside method for getLegendImage for kpiName {} , kpiStats {} ", kpiName,
					kpiStats != null ? kpiStats.length : null);
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			writeHeading(kpiName, 10, graphics);
			int j = ReportConstants.INDEX_THIRTY;
			int i = ReportConstants.INDEX_THIRTY;
			try {
				if (kpiStats != null && kpiStats.length > 0) {
					List<Integer> list = Stream.of(kpiStats).map(e -> new Integer(e)).collect(Collectors.toList());
					Integer totalCount = list.stream().mapToInt(value -> value).sum();
					for (int m = 0; m < kpiStats.length; m++) {
						j += 15;
						logger.info("for rank Index  {} , value is {}  ", m, Integer.parseInt(kpiStats[m]));
						graphics.setColor(ColorFinder.getMimoColor(m));
						graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
								ReportConstants.TEN);
						graphics.setColor(Color.BLACK);
						if (totalCount != null && totalCount != 0) {
							Double percent = ReportUtil.parseToFixedDecimalPlace(
									((Double.parseDouble(kpiStats[m]) * 100.0) / totalCount), 3);
							graphics.drawString(m + ReportConstants.SPACE + ReportConstants.OPEN_BRACKET + kpiStats[m]
									+ ReportConstants.CLOSED_BRACKET + ReportConstants.SPACE + percent
									+ ReportConstants.PERCENT, i, j);
						} else {
							graphics.drawString(m + ReportConstants.SPACE + ReportConstants.OPEN_BRACKET + kpiStats[m]
									+ ReportConstants.CLOSED_BRACKET + ReportConstants.SPACE
									+ ReportConstants.ZERO_DOT_ZEROZERO + ReportConstants.PERCENT, i, j);

						}
					}
				}
			} catch (Exception e) {
				logger.error(errorInWritingDataInImage + Utils.getStackTrace(e));
			}
		} catch (Exception e) {
			logger.error(errorInWritingDataInImage + Utils.getStackTrace(e));
		}

		return image;

	}

	/**
	 * Get the legend image for InBuilding Benchmark.
	 *
	 * @param total   the total
	 * @param kpiName the kpi name
	 * @param list    the list
	 * @return the legend image for given KPi
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static BufferedImage getLegendImageForIBBenchmark(Integer total, String kpiName, List<RangeSlab> list)
			throws IOException {
		BufferedImage image = new BufferedImage(ReportConstants.IB_BENCHMARK_LEGEND_IMAGE_WIDTH,
				ReportConstants.IB_BENCHMARK_LEGEND_IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		int count = 0;
		try {
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(ReportConstants.INDEX_ZER0, ReportConstants.INDEX_ZER0,
					ReportConstants.IB_BENCHMARK_LEGEND_IMAGE_WIDTH, ReportConstants.IB_BENCHMARK_LEGEND_IMAGE_HEIGHT);
			int j = ReportConstants.INDEX_THIRTY;
			int i = ReportConstants.IB_BENCHMARK_LEGEND_X_SPACING;
			try {
				logger.debug(logRangeSlabSize, list.size(), kpiName);
				for (RangeSlab rangeSlab : list) {
					++count;
					j = writeDataInImage(rangeSlab, i, j, graphics, kpiName);
				}
			} catch (Exception e) {
				logger.error(errorInWritingDataInImage + Utils.getStackTrace(e));
			}
		} catch (Exception e) {
			logger.error("error in Writing heading for legemd image " + Utils.getStackTrace(e));
		}
		return image;
	}

	public static BufferedImage writeKpiDataFromMap(Map<String, Integer> kpiCountMap, String kpiName) {
		logger.info("Writitng kpi Data from Map {} ", kpiCountMap != null ? kpiCountMap.size() : null);
		try {
			BufferedImage image = new BufferedImage(ReportConstants.FIVE_ONE_TWO, ReportConstants.TWO_HUNDRED_TWENTY,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			writeHeading(kpiName, kpiCountMap.size(), graphics);
			int j = ReportConstants.INDEX_THIRTY;
			int i = ReportConstants.INDEX_THIRTY;
			int col = ReportConstants.INDEX_ZER0;
			logger.info("After Filtering Data Size {} ", kpiCountMap != null ? kpiCountMap.size() : null);
			Long totalCount = kpiCountMap.values().stream().mapToLong(Number::longValue).sum();
			for (Entry<String, Integer> entry : kpiCountMap.entrySet()) {
				++col;
				try {
					graphics.setColor(ColorFinder.getCarrierAggregationColor(entry.getKey()));
					graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
							ReportConstants.TEN);
					graphics.setColor(Color.BLACK);
					if (totalCount != ReportConstants.INDEX_ZER0
							&& (!entry.getValue().equals(new Integer(ReportConstants.INDEX_ZER0)))) {
						graphics.drawString(entry.getKey() + ReportConstants.DOUBLE_SPACE + ReportConstants.OPEN_BRACKET
								+ entry.getValue() + ReportConstants.CLOSED_BRACKET + ReportConstants.DOUBLE_SPACE
								+ df.format((entry.getValue() * ReportConstants.INDEX_HUNDRED) / (totalCount * 1.0))
								+ ReportConstants.PERCENT, i, j);
					} else {
						graphics.drawString(entry.getKey() + ReportConstants.DOUBLE_SPACE + ReportConstants.OPEN_BRACKET
								+ entry.getValue() + ReportConstants.CLOSED_BRACKET + ReportConstants.DOUBLE_SPACE
								+ ReportConstants.ZERO_DOT_ZEROZERO + ReportConstants.PERCENT, i, j);
					}
				} catch (Exception e) {
					logger.error(exceptionInWritingRangeWiseData, Utils.getStackTrace(e));
				}
				i += ReportConstants.ONE_HUNDRED_SEVENTY;
				if ((col % ReportConstants.INDEX_THREE) == 0) {
					i = ReportConstants.INDEX_THIRTY;
					j += ReportConstants.TEN;
				}
			}
			return image;
		} catch (Exception e) {
			logger.error(exceptionInFetchingDataFromEarcnMap, Utils.getStackTrace(e));
		}
		return null;
	}

	public static BufferedImage writeServingDataFromMap(Map<String, Long> servingCountMap,
			Map<String, Color> servingColorMap, String kpiName) {
		logger.info("Writitng kpi Data from Map {} ", servingCountMap != null ? servingCountMap.size() : null);
		try {
			BufferedImage image = new BufferedImage(ReportConstants.FIVE_ONE_TWO, ReportConstants.TWO_HUNDRED_TWENTY,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			writeHeading(kpiName, servingCountMap.size(), graphics);
			int j = ReportConstants.INDEX_THIRTY;
			int i = ReportConstants.INDEX_THIRTY;
			int col = ReportConstants.INDEX_ZER0;
			logger.info("After Filtering Data Size {} ", servingCountMap != null ? servingCountMap.size() : null);
			Long totalCount = servingCountMap.values().stream().mapToLong(Number::longValue).sum();
			for (Entry<String, Long> entry : servingCountMap.entrySet()) {
				++col;
				try {

					if (kpiName.equalsIgnoreCase(DriveHeaderConstants.TECHNOLOGY)) {
						graphics.setColor(ColorFinder.getTechnologyColor(entry.getKey()));
					} else {
						graphics.setColor(servingColorMap.get(entry.getKey()));
					}
					graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
							ReportConstants.TEN);
					graphics.setColor(Color.BLACK);
					if (totalCount != ReportConstants.INDEX_ZER0 && (entry.getValue() != ReportConstants.INDEX_ZER0)) {
						graphics.drawString(entry.getKey().replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE)
								+ ReportConstants.DOUBLE_SPACE + ReportConstants.OPEN_BRACKET + entry.getValue()
								+ ReportConstants.CLOSED_BRACKET + ReportConstants.DOUBLE_SPACE
								+ df.format((entry.getValue() * ReportConstants.INDEX_HUNDRED) / (totalCount * 1.0))
								+ ReportConstants.PERCENT, i, j);
					} else {
						graphics.drawString(entry.getKey().replaceAll(ReportConstants.UNDERSCORE, ReportConstants.SPACE)
								+ ReportConstants.DOUBLE_SPACE + ReportConstants.OPEN_BRACKET + entry.getValue()
								+ ReportConstants.CLOSED_BRACKET + ReportConstants.DOUBLE_SPACE
								+ ReportConstants.ZERO_DOT_ZEROZERO + ReportConstants.PERCENT, i, j);
					}
				} catch (Exception e) {
					logger.error(exceptionInWritingRangeWiseData, Utils.getStackTrace(e));
				}
				i += ReportConstants.ONE_HUNDRED_SEVENTY;
				if ((col % ReportConstants.INDEX_THREE) == 0) {
					i = ReportConstants.INDEX_THIRTY;
					j += ReportConstants.TEN;
				}
			}
			return image;
		} catch (Exception e) {
			logger.error("error in getting values from  Serving Count Map {}", Utils.getStackTrace(e));
		}
		return null;
	}

	public static BufferedImage getCustomImageForIBBenchmark(Integer total,
			Map<String, HashMap<Integer, PCIWrapper>> pciCountMap, Map<String, Long> earfcnCountMap, String key,
			Map<String, Color> earfcnColorMap) {
		logger.info("Inside method getCustomImage with total key {} ", key);
		int mapSize = 0;
		if (key.equalsIgnoreCase("LTE PCI")) {
			mapSize = pciCountMap.size();
		} else if (key.equalsIgnoreCase("EARFCN") && earfcnCountMap != null) {
			mapSize = earfcnCountMap.size();
		}
		logger.info("mapSize for key {}  ,is   {} ", key, mapSize);
		BufferedImage image = new BufferedImage(ReportConstants.IB_BENCHMARK_LEGEND_IMAGE_WIDTH,
				ReportConstants.IB_BENCHMARK_LEGEND_IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		DecimalFormat df = new DecimalFormat(ReportConstants.HASH_DOT_HASHHASH);
		try {
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(ReportConstants.INDEX_ZER0, ReportConstants.INDEX_ZER0,
					ReportConstants.IB_BENCHMARK_LEGEND_IMAGE_WIDTH, ReportConstants.IB_BENCHMARK_LEGEND_IMAGE_HEIGHT);
			int j = ReportConstants.INDEX_THIRTY;
			int i = ReportConstants.INDEX_THIRTY;
			int col = ReportConstants.INDEX_ZER0;
			if (key.equalsIgnoreCase("LTE PCI")) {
				writePciLegendDataForIBBenchmark(total, pciCountMap, df, graphics, j, i, col);
			} else if (key.equalsIgnoreCase("EARFCN") && earfcnCountMap != null) {
				writeEarfcnLegendData(earfcnCountMap, df, graphics, j, i, col, earfcnColorMap);
			}
		} catch (Exception e) {
			logger.error(errorGettingPciMapValue, Utils.getStackTrace(e));
		}
		return image;
	}

	private static void writePciLegendDataForIBBenchmark(Integer total,
			Map<String, HashMap<Integer, PCIWrapper>> pCIMap, DecimalFormat df, Graphics2D graphics, int j, int i,
			int col) {
		logger.info("Inside method writePciLegendData with total value  {} ", total);
		try {
			for (Entry<String, HashMap<Integer, PCIWrapper>> operatorPCIEntry : pCIMap.entrySet()) {
				graphics.setFont(new Font(ReportConstants.DEFAULT, Font.PLAIN, 18));
				graphics.setColor(Color.BLACK);
				graphics.drawString(operatorPCIEntry.getKey().toUpperCase(), i, j);
				j += ReportConstants.INDEX_THIRTY;
				Map<Integer, PCIWrapper> pciMap = getSortedMapByCount(operatorPCIEntry.getValue());
				Map<Integer, PCIWrapper> pciRevereMap = reverseMap(pciMap);
				for (Map.Entry<Integer, PCIWrapper> entry : pciRevereMap.entrySet()) {
					Integer pciCountzero = entry.getValue().getCount();
					if ((!pciCountzero.equals(0)) && pciCountzero != null) {
						++col;
						drawBubbleForIBBenchmark(df, graphics, j, entry, i);
						i += ReportConstants.IB_BM_PCI_LEGEND_MARGIN;
						if ((col % ReportConstants.INDEX_TWO) == 0) {
							i = ReportConstants.INDEX_THIRTY;
							j += ReportConstants.INDEX_THIRTY;
						}
					}
				}
				i = ReportConstants.INDEX_THIRTY;
				j += ReportConstants.INDEX_THIRTY;
			}
		} catch (Exception e) {
			logger.error(errorGettingPciMapValue, Utils.getStackTrace(e));
		}
	}

	private static int drawBubbleForIBBenchmark(DecimalFormat df, Graphics2D graphics, int j,
			Map.Entry<Integer, PCIWrapper> entry, int i) throws Exception {
		try {
			graphics.setColor(entry.getValue().getColor());
			graphics.fillOval(i - ReportConstants.TWENTY, j - 17, ReportConstants.TWENTY, ReportConstants.TWENTY);
			graphics.setColor(Color.BLACK);

			String pci = Integer.toString(entry.getValue().getPCI() + ReportConstants.THOUSNAND)
					.substring(ReportConstants.INDEX_ONE);
			graphics.setFont(new Font(ReportConstants.DEFAULT, Font.PLAIN, 18));
			graphics.drawString(pci + ReportConstants.DOUBLE_SPACE + ReportConstants.OPEN_BRACKET
					+ entry.getValue().getCount() + ReportConstants.CLOSED_BRACKET, i + 5, j);
		} catch (Exception e) {
			logger.error(exceptionInWritingRangeWiseData, Utils.getStackTrace(e));
		}
		return j;
	}

	public static BufferedImage getLegendImageForNVDashboard(String kpiName, List<RangeSlab> list) {
		BufferedImage image = new BufferedImage(ReportConstants.NVDASHBOARD_LEGEND_IMAGE_WIDTH,
				ReportConstants.NVDASHBOARD_LEGEND_IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		int count = ReportConstants.INDEX_ZER0;
		try {
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(ReportConstants.INDEX_ZER0, ReportConstants.INDEX_ZER0,
					ReportConstants.NVDASHBOARD_LEGEND_IMAGE_WIDTH, ReportConstants.NVDASHBOARD_LEGEND_IMAGE_HEIGHT);
			int j = ReportConstants.INDEX_SIX;
			int i = ReportConstants.INDEX_THIRTY;
			try {
				logger.debug(logRangeSlabSize, list.size(), kpiName);
				for (RangeSlab rangeSlab : list) {
					count++;
					if (count < ReportConstants.INDEX_TWO) {
						writeDataForNVDashboard(rangeSlab, i, j, graphics);
					} else if (count == ReportConstants.INDEX_TWO) {
						i += 150;
						j = writeDataForNVDashboard(rangeSlab, i, j, graphics);
						count = ReportConstants.INDEX_ZER0;
						i = ReportConstants.INDEX_THIRTY;
					}
				}
			} catch (Exception e) {
				logger.error(errorInWritingDataInImage + ExceptionUtils.getStackTrace(e));
			}
		} catch (Exception e) {
			logger.error(errorInWritingDataInImage + ExceptionUtils.getStackTrace(e));
		}
		return image;
	}

	private static int writeDataForNVDashboard(RangeSlab rangeSlab, int i, int j, Graphics2D graphics) {
		j = j + ReportConstants.TWENTY_FOUR;
		try {
			graphics.setColor(Color.decode(rangeSlab.getColorCode()));
			graphics.fillOval(i - ReportConstants.INDEX_EIGHTEEN, j - ReportConstants.FIFTEEN,
					ReportConstants.INDEX_EIGHTEEN, ReportConstants.INDEX_EIGHTEEN);
			graphics.setColor(Color.BLACK);
			String range = rangeSlab.getLowerLimit() + " to " + rangeSlab.getUpperLimit();
			graphics.setFont(new Font(ReportConstants.CALIBRI, Font.PLAIN, ReportConstants.TWELVE));
			graphics.drawString(range, i + ReportConstants.INDEX_NINE, j);
		} catch (Exception e) {
			logger.error(errorImsideMethodwriteDataInImage, ExceptionUtils.getStackTrace(e));
		}
		return j;
	}

	/**
	 * Get the legend image.
	 *
	 * @param total   the total
	 * @param kpiName the kpi name
	 * @param list    the list
	 * @return the legend image for given KPi
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static BufferedImage getAtollLegendImage(String kpiName, List<RangeSlab> list) throws IOException {
		logger.debug("Inside method getLegendImage for kpi {} ", kpiName);
		int size = list.size();
		BufferedImage image = new BufferedImage(ReportConstants.FIVE_ONE_TWO, ReportConstants.TWO_HUNDRED_TWENTY,
				BufferedImage.TYPE_INT_RGB);
		int count = 0;
		try {
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			writeHeading(kpiName, size, graphics);
			int j = ReportConstants.INDEX_THIRTY;
			int i = ReportConstants.INDEX_THIRTY;
			try {
				logger.debug(logRangeSlabSize, list.size(), kpiName);
				for (RangeSlab rangeSlab : list) {
					if (rangeSlab != null) {
						++count;
						j = writeDataInAtollImage(rangeSlab, i, j, graphics, kpiName);
					}
				}
			} catch (Exception e) {
				logger.error(errorInWritingDataInImage + Utils.getStackTrace(e));
			}
		} catch (Exception e) {
			logger.error(errorInWritingDataInImage + Utils.getStackTrace(e));
		}
		return image;
	}

	/**
	 * Write data in image.
	 *
	 * @param rangeSlab the range slab
	 * @param total     the total
	 * @param i         the i
	 * @param j         the j
	 * @param graphics  the graphics
	 * @param df        the df
	 * @param kpiName
	 * @return the int
	 */
	private static int writeDataInAtollImage(RangeSlab rangeSlab, int i, int j, Graphics2D graphics, String kpiName) {
		j = j + ReportConstants.FIFTEEN;
		try {
			graphics.setColor(Color.decode(rangeSlab.getColorCode()));
			graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
					ReportConstants.TEN);
			graphics.setColor(Color.BLACK);
			String range = rangeSlab.getLowerLimit() + ReportConstants.LESS_THAN_SYMBOL + ForesightConstants.EQUALS
					+ ReportConstants.SPACE + kpiName.replace(ReportConstants.THROUGHPUT, ReportConstants.THP)
					+ ReportConstants.SPACE + ReportConstants.LESS_THAN_SYMBOL + rangeSlab.getUpperLimit();
			graphics.drawString(range, i, j);
		} catch (Exception e) {
			logger.error(errorImsideMethodwriteDataInImage, Utils.getStackTrace(e));
		}
		return j;
	}

	/**
	 * //////////////////////////////////Site Database Status
	 * Images//////////////////////////////////////////////.
	 */

	public static BufferedImage getSiteDatabaseCustomImage(Integer total,
			Map<String, List<SiteInformationWrapper>> statusWiseSiteMap, String key) {
		int mapSize = ReportConstants.TWENTY;
		BufferedImage image = new BufferedImage(ReportConstants.FIVE_ONE_TWO, ReportConstants.TWO_HUNDRED_TWENTY,
				BufferedImage.TYPE_INT_RGB);
		DecimalFormat df = new DecimalFormat(ReportConstants.HASH_DOT_HASHHASH);
		try {
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			writeHeading(key, mapSize, graphics);
			int j = ReportConstants.INDEX_THIRTY;
			int i = ReportConstants.INDEX_THIRTY;
			int col = ReportConstants.INDEX_ZER0;
			writeSiteStatusData(statusWiseSiteMap, df, graphics, j, i, col, total);
		} catch (Exception e) {
			logger.error(errorGettingPciMapValue, Utils.getStackTrace(e));
		}
		return image;
	}

	private static void writeSiteStatusData(Map<String, List<SiteInformationWrapper>> statusWiseSiteMapp,
			DecimalFormat df, Graphics2D graphics, int j, int i, int col, Integer total) {
		for (Entry<String, List<SiteInformationWrapper>> entry : statusWiseSiteMapp.entrySet()) {
			++col;
			try {
				graphics.setColor(ImageCreator.getColorBySiteStatus(entry.getKey()));
				graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
						ReportConstants.TEN);
				graphics.setColor(Color.BLACK);
				if (entry.getValue() != null && entry.getValue().size() > ReportConstants.INDEX_ZER0) {
					graphics.drawString(entry.getKey() + ReportConstants.DOUBLE_SPACE + ReportConstants.OPEN_BRACKET
							+ entry.getValue().size() + ReportConstants.CLOSED_BRACKET + ReportConstants.DOUBLE_SPACE
							+ df.format((entry.getValue().size() * ReportConstants.INDEX_HUNDRED) / (total * 1.0))
							+ ReportConstants.PERCENT, i, j);
				} else {
					graphics.drawString(entry.getKey() + ReportConstants.DOUBLE_SPACE + ReportConstants.OPEN_BRACKET
							+ entry.getValue().size() + ReportConstants.CLOSED_BRACKET + ReportConstants.DOUBLE_SPACE
							+ ReportConstants.ZERO_DOT_ZEROZERO + ReportConstants.PERCENT, i, j);
				}
			} catch (Exception e) {
				logger.error(exceptionInWritingRangeWiseData, Utils.getStackTrace(e));
			}
			j += ReportConstants.TEN;
		}
	}

	public static BufferedImage getLegendImageForVoLTECodec(List<String[]> dataKPIs, Map<String, Integer> kpiIndexMap,
			Map<String, Color> codecColorMap){
		BufferedImage image = new BufferedImage(ReportConstants.FIVE_ONE_TWO, ReportConstants.TWO_HUNDRED_TWENTY,
				BufferedImage.TYPE_INT_RGB);
		try {
			if(kpiIndexMap != null && kpiIndexMap.containsKey(ReportConstants.VOLTE_CODEC)) {
				List<String> dataList = dataKPIs.stream()
												.filter(x -> !StringUtils.isBlank(x[kpiIndexMap.get(ReportConstants.VOLTE_CODEC)]))
												.map(x -> x[kpiIndexMap.get(ReportConstants.VOLTE_CODEC)])
												.collect(Collectors.toList());

				Map<String, Long> kpiCountMap = dataList.stream().collect(Collectors.groupingBy(x -> x, Collectors.counting()));
				Graphics2D graphics = (Graphics2D) image.getGraphics();
				writeHeading(ReportConstants.VOLTE_CODEC.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING), 0,
						graphics);

				int j = ReportConstants.FOURTY;
				int i = ReportConstants.INDEX_THIRTY;
				fillLegendForCodecValues(kpiCountMap, codecColorMap, graphics, i, j, dataList.size());
			}
		} catch (Exception e) {
			logger.error(errorInWritingDataInImage, Utils.getStackTrace(e));
		}
		return image;
	}

	private static void fillLegendForCodecValues(Map<String, Long> kpiCountMap, Map<String, Color> codecColorMap,
			Graphics2D graphics, int i, int j, Integer totalCount) {
		if(kpiCountMap != null && !kpiCountMap.isEmpty() && codecColorMap != null && !codecColorMap.isEmpty()) {
			for(Entry<String, Long> kpiCountEntry : kpiCountMap.entrySet()) {
				if(codecColorMap.containsKey(kpiCountEntry.getKey())) {
					graphics.setColor(codecColorMap.get(kpiCountEntry.getKey()));
				}
				graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
						ReportConstants.TEN);
				graphics.setColor(Color.BLACK);
				graphics.drawString(kpiCountEntry.getKey() + Symbol.SPACE_STRING + getKpiPercentageAndCount(kpiCountMap,
						kpiCountEntry.getKey(), totalCount), i, j);
				j += ReportConstants.TWENTY;
			}
		}
	}

	public static BufferedImage getLegendImageForMimo(List<String[]> dataKPIs, Map<String, Integer> kpiIndexMap){
		BufferedImage image = new BufferedImage(ReportConstants.FIVE_ONE_TWO, ReportConstants.TWO_HUNDRED_TWENTY,
				BufferedImage.TYPE_INT_RGB);
		try {
			if(kpiIndexMap != null && kpiIndexMap.containsKey(ReportConstants.RI)) {
				List<String> dataList = dataKPIs.stream()
												.filter(x -> !StringUtils.isBlank(x[kpiIndexMap.get(ReportConstants.RI)]))
												.map(x -> x[kpiIndexMap.get(ReportConstants.RI)])
												.collect(Collectors.toList());

				Map<String, Long> kpiCountMap = dataList.stream().collect(Collectors.groupingBy(x -> x, Collectors.counting()));

				Graphics2D graphics = (Graphics2D) image.getGraphics();
				writeHeading(ReportConstants.MIMO, 0, graphics);
				int j = ReportConstants.FOURTY;
				int i = ReportConstants.INDEX_THIRTY;
				graphics.setColor(Color.BLACK);
				graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
						ReportConstants.TEN);
				graphics.setColor(Color.BLACK);
				graphics.drawString(ReportConstants.MIMO_RANK_0.toUpperCase() + Symbol.SPACE_STRING + getKpiPercentageAndCount(
						kpiCountMap, ReportConstants.MIMO_RANK_0, dataList.size()), i, j);
				j += ReportConstants.TWENTY;
				graphics.setColor(Color.RED);
				graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
						ReportConstants.TEN);
				graphics.setColor(Color.BLACK);
				graphics.drawString(ReportConstants.MIMO_RANK_1.toUpperCase() + Symbol.SPACE_STRING + getKpiPercentageAndCount(
						kpiCountMap, ReportConstants.MIMO_RANK_1, dataList.size()), i, j);
				j += ReportConstants.TWENTY;
				graphics.setColor(Color.BLUE);
				graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
						ReportConstants.TEN);
				graphics.setColor(Color.BLACK);
				graphics.drawString(ReportConstants.MIMO_RANK_2.toUpperCase() + Symbol.SPACE_STRING + getKpiPercentageAndCount(
						kpiCountMap, ReportConstants.MIMO_RANK_2, dataList.size()), i, j);
				j += ReportConstants.TWENTY;
				graphics.setColor(Color.GREEN);
				graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
						ReportConstants.TEN);
				graphics.setColor(Color.BLACK);
				graphics.drawString(ReportConstants.MIMO_RANK_3.toUpperCase() + Symbol.SPACE_STRING + getKpiPercentageAndCount(
						kpiCountMap, ReportConstants.MIMO_RANK_3, dataList.size()), i, j);
				j += ReportConstants.TWENTY;
				graphics.setColor(Color.ORANGE);
				graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
						ReportConstants.TEN);
				graphics.setColor(Color.BLACK);
				graphics.drawString(ReportConstants.MIMO_RANK_4.toUpperCase() + Symbol.SPACE_STRING + getKpiPercentageAndCount(
						kpiCountMap, ReportConstants.MIMO_RANK_4, dataList.size()), i, j);
			}
		} catch (Exception e) {
			logger.error(errorInWritingDataInImage, Utils.getStackTrace(e));
		}
		return image;
	}

	public static BufferedImage getLegendImageForDlModulationType(List<String[]> dataKPIs, Map<String, Integer> kpiIndexMap){
		BufferedImage image = new BufferedImage(ReportConstants.FIVE_ONE_TWO, ReportConstants.TWO_HUNDRED_TWENTY,
				BufferedImage.TYPE_INT_RGB);
		try {
			if(kpiIndexMap != null && kpiIndexMap.containsKey(ReportConstants.DL_MODULATION_TYPE)) {
				List<String> dataList = dataKPIs.stream()
												.filter(x -> !StringUtils.isBlank(x[kpiIndexMap.get(ReportConstants.DL_MODULATION_TYPE)]))
												.map(x -> x[kpiIndexMap.get(ReportConstants.DL_MODULATION_TYPE)])
												.collect(Collectors.toList());

				Map<String, Long> kpiCountMap = dataList.stream().collect(Collectors.groupingBy(x -> x, Collectors.counting()));
				Graphics2D graphics = (Graphics2D) image.getGraphics();
				writeHeading(ReportConstants.DL_MODULATION_TYPE.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING), 0,
						graphics);
				int j = ReportConstants.FOURTY;
				int i = ReportConstants.INDEX_THIRTY;
				graphics.setColor(Color.decode("#FC8056"));
				graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
						ReportConstants.TEN);
				graphics.setColor(Color.BLACK);
				graphics.drawString(ReportConstants.DL_MODULATION_QPSK + Symbol.SPACE_STRING + getKpiPercentageAndCount(kpiCountMap,
						ReportConstants.DL_MODULATION_QPSK, dataList.size()), i, j);
				j += ReportConstants.TWENTY;
				graphics.setColor(Color.decode("#FA5656"));
				graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
						ReportConstants.TEN);
				graphics.setColor(Color.BLACK);
				graphics.drawString(ReportConstants.DL_MODULATION_16_QAM + Symbol.SPACE_STRING + getKpiPercentageAndCount(
						kpiCountMap, ReportConstants.DL_MODULATION_16_QAM, dataList.size()), i, j);
				j += ReportConstants.TWENTY;
				graphics.setColor(Color.decode("#58BFFB"));
				graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
						ReportConstants.TEN);
				graphics.setColor(Color.BLACK);
				graphics.drawString(ReportConstants.DL_MODULATION_64_QAM + Symbol.SPACE_STRING + getKpiPercentageAndCount(
						kpiCountMap, ReportConstants.DL_MODULATION_64_QAM, dataList.size()), i, j);
				j += ReportConstants.TWENTY;
				graphics.setColor(Color.decode("#20BF55"));
				graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
						ReportConstants.TEN);
				graphics.setColor(Color.BLACK);
				graphics.drawString(ReportConstants.DL_MODULATION_256_QAM + Symbol.SPACE_STRING + getKpiPercentageAndCount(
						kpiCountMap, ReportConstants.DL_MODULATION_256_QAM, dataList.size()), i, j);
			}
		} catch (Exception e) {
			logger.error(errorInWritingDataInImage, Utils.getStackTrace(e));
		}
		return image;
	}

	public static BufferedImage getLegendImageForCallEvents(boolean isCallSetupPlot, List<String[]> dataList, Map<String, Integer> kpiIndexMap) {
		BufferedImage image = new BufferedImage(ReportConstants.TWO_HUNDRED_TWENTY, ReportConstants.TWO_HUNDRED_TWENTY,
				BufferedImage.TYPE_INT_RGB);
		try {
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			String headingName = null;
			if (isCallSetupPlot) {
				headingName = ReportConstants.CALL_SETUP_PLOT.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING);
			} else {
				headingName = ReportConstants.CALL_PLOT.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING);
			}
			writeHeading(headingName, 0, graphics);
			int j = ReportConstants.FOURTY;
			int i = ReportConstants.INDEX_THIRTY;
			graphics.setColor(Color.BLUE);
			graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
					ReportConstants.TEN);
			graphics.setColor(Color.BLACK);
			String initiateCount = getKpiCountFromData(dataList, kpiIndexMap.get(ReportConstants.CALL_INITIATE));
			graphics.drawString(ReportConstants.CALL_INITIATE.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING)
															 .toUpperCase() + initiateCount, i, j);
			j += ReportConstants.TWENTY;
			if (!isCallSetupPlot) {
				graphics.setColor(Color.RED);
				graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
						ReportConstants.TEN);
				graphics.setColor(Color.BLACK);
				String dropCount = getKpiCountFromData(dataList, kpiIndexMap.get(ReportConstants.CALL_DROP));
				graphics.drawString(ReportConstants.CALL_DROP.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING)
															 .toUpperCase() + dropCount, i, j);
				j += ReportConstants.TWENTY;
				graphics.setColor(Color.ORANGE);
				graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
						ReportConstants.TEN);
				graphics.setColor(Color.BLACK);
				String failureCount = getKpiCountFromData(dataList, kpiIndexMap.get(ReportConstants.CALL_FAILURE));
				graphics.drawString(
						ReportConstants.CALL_FAILURE.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING)
													.toUpperCase() + failureCount, i, j);
				j += ReportConstants.TWENTY;
				graphics.setColor(Color.GREEN);
				graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
						ReportConstants.TEN);
				graphics.setColor(Color.BLACK);
				String successCount = getKpiCountFromData(dataList, kpiIndexMap.get(ReportConstants.CALL_SUCCESS));
				graphics.drawString(
						ReportConstants.CALL_SUCCESS.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING)
													.toUpperCase() + successCount, i, j);
			} else {
				graphics.setColor(Color.GREEN);
				graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
						ReportConstants.TEN);
				graphics.setColor(Color.BLACK);
				String setupSuccessCount = getKpiCountFromData(dataList, kpiIndexMap.get(ReportConstants.CALL_SETUP_SUCCESS));
				graphics.drawString(
						ReportConstants.CALL_SETUP_SUCCESS.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING)
														  .toUpperCase() + setupSuccessCount, i, j);
			}
		} catch (Exception e) {
			logger.error(errorInWritingDataInImage, Utils.getStackTrace(e));
		}
		return image;
	}

	public static BufferedImage getLegendImageForHandoverEvents(List<String[]> dataList, Map<String, Integer> kpiIndexMap) {
		BufferedImage image = new BufferedImage(ReportConstants.TWO_HUNDRED_TWENTY, ReportConstants.TWO_HUNDRED_TWENTY,
				BufferedImage.TYPE_INT_RGB);
		try {
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			writeHeading(ReportConstants.HANDOVER_PLOT.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING), 0,
					graphics);
			int j = ReportConstants.FOURTY;
			int i = ReportConstants.INDEX_THIRTY;
			graphics.setColor(Color.BLUE);
			graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
					ReportConstants.TEN);
			graphics.setColor(Color.BLACK);
			String initiateCount = getKpiCountFromData(dataList, kpiIndexMap.get(ReportConstants.HANDOVER_INITIATE));
			graphics.drawString(
					ReportConstants.HANDOVER_INITIATE.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING)
													 .toUpperCase() + initiateCount, i, j);
			j += ReportConstants.TWENTY;
			graphics.setColor(Color.RED);
			graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
					ReportConstants.TEN);
			graphics.setColor(Color.BLACK);
			String failureCount = getKpiCountFromData(dataList, kpiIndexMap.get(ReportConstants.HANDOVER_FAILURE));
			graphics.drawString(
					ReportConstants.HANDOVER_FAILURE.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING)
													.toUpperCase() + failureCount, i, j);
			j += ReportConstants.TWENTY;
			graphics.setColor(Color.GREEN);
			graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
					ReportConstants.TEN);
			graphics.setColor(Color.BLACK);
			String successCount = getKpiCountFromData(dataList, kpiIndexMap.get(ReportConstants.HANDOVER_SUCCESS));
			graphics.drawString(
					ReportConstants.HANDOVER_SUCCESS.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING)
													.toUpperCase() + successCount, i, j);
		} catch (Exception e) {
			logger.error(errorInWritingDataInImage, Utils.getStackTrace(e));
		}
		return image;
	}

	private static String getKpiCountFromData(List<String[]> dataList,
			Integer kpiIndex) {
		List<Double> indexDataList = ReportUtil.convetArrayToList(dataList, kpiIndex);
		Double kpiCount = indexDataList.stream().mapToDouble(x -> x).sum();
		return kpiCount != null ? Symbol.SPACE_STRING + Symbol.PARENTHESIS_OPEN_STRING + kpiCount.intValue() + Symbol.PARENTHESIS_CLOSE_STRING : Symbol.EMPTY_STRING;
	}

	public static BufferedImage getLegendImageForRRCEvents(List<String[]> dataList, Map<String, Integer> kpiIndexMap) {
		BufferedImage image = new BufferedImage(ReportConstants.TWO_HUNDRED_TWENTY, ReportConstants.TWO_HUNDRED_TWENTY,
				BufferedImage.TYPE_INT_RGB);
		try {
			Graphics2D graphics = (Graphics2D) image.getGraphics();
			writeHeading(ReportConstants.ERAB_PLOT.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING), 0,
					graphics);
			int j = ReportConstants.FOURTY;
			int i = ReportConstants.INDEX_THIRTY;
			graphics.setColor(Color.RED);
			graphics.fillOval(i - ReportConstants.TWENTY, j - ReportConstants.TEN, ReportConstants.TEN,
					ReportConstants.TEN);
			graphics.setColor(Color.BLACK);
			String erabCount = getKpiCountFromData(dataList, kpiIndexMap.get(ReportConstants.RRC_DROPPED));
			graphics.drawString(
					ReportConstants.ERAB_DROP.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING)
													 .toUpperCase() + erabCount, i, j);
			j += ReportConstants.TWENTY;
			String initiateCount = getKpiCountFromData(dataList, kpiIndexMap.get(ReportConstants.ERAB_REQUEST));
			graphics.drawString(
					ReportConstants.ERAB_REQUEST.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING)
													 .toUpperCase() + initiateCount, i, j);
			j += ReportConstants.TWENTY;
			String successCount = getKpiCountFromData(dataList, kpiIndexMap.get(ReportConstants.ERAB_SUCCESS));
			graphics.drawString(
					ReportConstants.ERAB_SUCCESS.replaceAll(Symbol.UNDERSCORE_STRING, Symbol.SPACE_STRING)
												.toUpperCase() + successCount, i, j);
		} catch (Exception e) {
			logger.error(errorInWritingDataInImage, Utils.getStackTrace(e));
		}
		return image;
	}

	private static String getKpiPercentageAndCount(Map<String, Long> kpiCountMap, String kpiName, Integer totalCount) {
		if (!StringUtils.isBlank(kpiName) && kpiCountMap.containsKey(kpiName)) {
			Long kpiCount = kpiCountMap.get(kpiName);
			return Symbol.PARENTHESIS_OPEN_STRING + kpiCount + Symbol.PARENTHESIS_CLOSE_STRING + Symbol.SPACE_STRING
					+ ReportUtil.round(ReportUtil.getPercentage(kpiCount.intValue(), totalCount),
					ReportConstants.TWO_DECIMAL_PLACES) + Symbol.PERCENT_STRING;
		}
		return Symbol.EMPTY_STRING;
	}

	public static void main(String[] args) {
		Map<String, Color> map = new HashMap<>();
		map.put("-140", Color.BLUE);
		map.put("-125", Color.RED);
		map.put("-115", Color.GREEN);
		map.put("-110", Color.ORANGE);
		map.put("-105", Color.YELLOW);
		map.put("-80", Color.CYAN);
		map.put("-60", Color.GRAY);
		map.put("-40", null);
		BufferedImage legendStripForValueAndColor = getLegendStripForValueAndColor(map);
		try {
			ImageIO.write(legendStripForValueAndColor, "jpg", new File("/home/ist/Desktop" + ReportConstants.FORWARD_SLASH + "sampleLegend" + ReportConstants.DOT_JPG));
		} catch (IOException e) {
			logger.info("Exception inside main method {} ", Utils.getStackTrace(e));
		}

	}

	public static BufferedImage getLegendStripForValueAndColor(Map<String, Color> colorValueMap) {
		BufferedImage image = new BufferedImage(720, 30, BufferedImage.TYPE_INT_RGB);
		if (colorValueMap != null && !colorValueMap.isEmpty()) {
			try {
				Graphics2D graphics = (Graphics2D) image.getGraphics();
				graphics.setColor(Color.WHITE);
				graphics.fillRect(ReportConstants.INDEX_ZER0, ReportConstants.INDEX_ZER0, 720, 30);
				int j = 0;
				int i = 10;
				TreeMap<String, Color> sortedMap = new TreeMap<>(new Comparator<String>() {
					@Override
					public int compare(String s, String t1) {
						if (NumberUtils.isParsable(s) && NumberUtils.isParsable(t1)) {
							Integer n1 = Integer.parseInt(s);
							Integer n2 = Integer.parseInt(t1);
							return n1.compareTo(n2);
						}
						return -1;
					}
				});
				sortedMap.putAll(colorValueMap);
				for (Entry<String, Color> colorEntry : sortedMap.entrySet()) {
					if (colorEntry.getValue() != null) {
						graphics.setColor(colorEntry.getValue());
						graphics.fillRect(i, j, 100, 15);
					}
					graphics.setColor(Color.BLACK);
					graphics.setFont(new Font(ReportConstants.FONT_TIMES_NEW_ROMAN, Font.PLAIN, 10));
					graphics.drawString(colorEntry.getKey(), i - 10, j + 27);
					i += 100;
				}
			} catch (Exception e) {
				logger.error(errorInWritingDataInImage, Utils.getStackTrace(e));
			}
		}
		return image;
	}

}
