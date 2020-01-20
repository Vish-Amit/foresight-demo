package com.inn.foresight.module.nv.report.service.impl;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.inn.commons.Symbol;
import com.inn.commons.lang.NumberUtils;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.report.ib.image.BufferedImageEditor;
import com.inn.foresight.module.nv.report.ib.image.PointF;
import com.inn.foresight.module.nv.report.optimizedImage.ColorFinder;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.LegendUtil;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.PCIWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.InbuildingPointWrapper;

public class InBuildingHeatMapGenerator implements DriveHeaderConstants {
	private Logger logger = LogManager.getLogger(InBuildingHeatMapGenerator.class);

	private Random rand = new Random();


	private static InBuildingHeatMapGenerator instance;

	public static InBuildingHeatMapGenerator getInstance() {
		if (instance == null) {
			instance = new InBuildingHeatMapGenerator();
		}
		return instance;
	}

	public Map<String, String> generateHeatMaps(List<String[]> dataList, String folderPath, String imgFloorPlann,
			List<KPIWrapper> kpiList) {
		try {
			Map<String, String> heatMaps = new HashMap<>();
			HashMap<Integer, PCIWrapper> pciCountMap = new HashMap<>();
			HashMap<Integer, PCIWrapper> cgiCountMap = new HashMap<>();

			Map<String, Long> newearfcncountMap = new HashMap<>();
			Map<String, Color> newearfcncolorMap = new HashMap<>();
			List<Integer> pciList = new ArrayList<>();
			List<Integer> cgiList = new ArrayList<>();

			Map<String, List<PointF>> kpiDataMap = getKpiDataMap(dataList, imgFloorPlann, folderPath, kpiList, DriveHeaderConstants.INDEX_DL,
					DriveHeaderConstants.INDEX_UL, DriveHeaderConstants.INDEX_XPOINT, DriveHeaderConstants.INDEX_YPOINT, DriveHeaderConstants.INDEX_TEST_TYPE);

			for (Entry<String, List<PointF>> map : kpiDataMap.entrySet()) {
				if ((map.getKey().equalsIgnoreCase(ReportConstants.PCI_PLOT))) {
					pciList = getPciList(dataList,INDEX_PCI, DriveHeaderConstants.INDEX_XPOINT, DriveHeaderConstants.INDEX_YPOINT);
					pciCountMap = preparePciColorMap(pciList);
					List<Color> i = getColorList(pciCountMap, pciList);
					BufferedImageEditor editor = new BufferedImageEditor(imgFloorPlann);
					editor.drawPath(map.getValue(), i);
					String fileName = map.getKey() + ReportConstants.UNDERSCORE + new Date().getTime();
					File img = editor.finalizeImageToOutput(folderPath + File.separator + fileName + ReportConstants.DOT_PNG,
							BufferedImageEditor.OUTPUT_FORMAT_JPEG);
					heatMaps.put(map.getKey(), img.getAbsolutePath());
				}
				else if ((map.getKey().equalsIgnoreCase(DriveHeaderConstants.CGI))) {
					cgiList = getPciList(dataList,INDEX_CELLID, DriveHeaderConstants.INDEX_XPOINT, DriveHeaderConstants.INDEX_YPOINT);
					cgiCountMap = preparePciColorMap(cgiList);
					List<Color> i = getColorList(cgiCountMap, cgiList);
					BufferedImageEditor editor = new BufferedImageEditor(imgFloorPlann);
					editor.drawPath(map.getValue(), i);
					String fileName = map.getKey() + ReportConstants.UNDERSCORE + new Date().getTime();
					File img = editor.finalizeImageToOutput(folderPath + File.separator + fileName +  ReportConstants.DOT_PNG,
							BufferedImageEditor.OUTPUT_FORMAT_JPEG);
					heatMaps.put(map.getKey(), img.getAbsolutePath());
					ImageIO.write(LegendUtil.getCustomImage(cgiList.size(), cgiCountMap, null, CGI, null), ReportConstants.JPG,
							new File(folderPath + ReportConstants.FORWARD_SLASH + "cgi_legends" + ReportConstants.DOT_PNG));
					heatMaps.put(CGI+Symbol.UNDERSCORE_STRING+ReportConstants.KEY_LEGENDS,
							folderPath + ReportConstants.FORWARD_SLASH + "cgi_legends" + ReportConstants.DOT_PNG);
				
				}
				else if ((map.getKey().equalsIgnoreCase(ReportConstants.CALL_PLOT))) {
					generateHeatMapsForCallData(dataList,folderPath, imgFloorPlann, map.getKey(),heatMaps,
							INDEX_CALL_INITIATE, INDEX_CALL_FAIL, INDEX_CALL_SUCCESS, INDEX_CALL_DROP, INDEX_XPOINT, INDEX_YPOINT);
				}

				else if ((map.getKey().equalsIgnoreCase(ReportConstants.DL_EARFCN))) {
					setEarfcnPlot(dataList, folderPath, imgFloorPlann, heatMaps, newearfcncountMap, newearfcncolorMap,
							map, INDEX_DL_EARFCN, INDEX_XPOINT, INDEX_YPOINT);
				} else {

					File file = getHeatMapImage(folderPath, imgFloorPlann, map.getValue(), map.getKey());
					if (file != null) {
						heatMaps.put(map.getKey(), file.getAbsolutePath());
					}
				}
			}
			setCustomImagesInMap(pciList.size(), heatMaps, pciCountMap, newearfcncountMap, newearfcncolorMap,
					folderPath);

			return heatMaps;
		} catch (Exception e) {
			logger.error("Error inside the method generateHeatMapForSingleChannel {}", e.getMessage());
		}
		return null;
	}

	

	public Map<String, String> generateHeatMapsForReport(List<String[]> dataList, String folderPath, String imgFloorPlann,
														 List<KPIWrapper> kpiList, Map<String, Integer> kpiIndexMap) {
		try {
			Map<String, String> heatMaps = new HashMap<>();
			HashMap<Integer, PCIWrapper> pciCountMap = new HashMap<>();
			HashMap<Integer, PCIWrapper> cgiCountMap = new HashMap<>();

			Map<String, Long> newearfcncountMap = new HashMap<>();
			Map<String, Color> newearfcncolorMap = new HashMap<>();
			List<Integer> pciList = new ArrayList<>();
			List<Integer> cgiList = new ArrayList<>();

			Map<String, List<PointF>> kpiDataMap = getKpiDataMap(dataList, imgFloorPlann, folderPath, kpiList, kpiIndexMap.get(ReportConstants.DL_THROUGHPUT),
					kpiIndexMap.get(ReportConstants.UL_THROUGHPUT), kpiIndexMap.get(ReportConstants.X_POINT),
					kpiIndexMap.get(ReportConstants.Y_POINT), kpiIndexMap.get(ReportConstants.TEST_TYPE));
			for (Entry<String, List<PointF>> map : kpiDataMap.entrySet()) {
				if ((map.getKey().equalsIgnoreCase(ReportConstants.PCI_PLOT) && kpiIndexMap.get(ReportConstants.PCI_PLOT) != null)) {
					pciList = getPciList(dataList,kpiIndexMap.get(ReportConstants.PCI_PLOT), kpiIndexMap.get(ReportConstants.X_POINT), kpiIndexMap.get(ReportConstants.Y_POINT));
					pciCountMap = preparePciColorMap(pciList);
					List<Color> i = getColorList(pciCountMap, pciList);
					BufferedImageEditor editor = new BufferedImageEditor(imgFloorPlann);
					editor.drawPath(map.getValue(), i);
					String fileName = map.getKey() + ReportConstants.UNDERSCORE + new Date().getTime();
					File img = editor.finalizeImageToOutput(folderPath + File.separator + fileName + ReportConstants.DOT_PNG,
							BufferedImageEditor.OUTPUT_FORMAT_JPEG);
					heatMaps.put(map.getKey(), img.getAbsolutePath());
				} else if ((map.getKey().equalsIgnoreCase(DriveHeaderConstants.CGI)) && kpiIndexMap.get(DriveHeaderConstants.CGI) != null) {
					cgiList = getPciList(dataList,kpiIndexMap.get(DriveHeaderConstants.CGI), kpiIndexMap.get(ReportConstants.X_POINT), kpiIndexMap.get(ReportConstants.Y_POINT));
					cgiCountMap = preparePciColorMap(cgiList);
					List<Color> i = getColorList(cgiCountMap, cgiList);
					BufferedImageEditor editor = new BufferedImageEditor(imgFloorPlann);
					editor.drawPath(map.getValue(), i);
					String fileName = map.getKey() + ReportConstants.UNDERSCORE + new Date().getTime();
					File img = editor.finalizeImageToOutput(folderPath + File.separator + fileName +  ReportConstants.DOT_PNG,
							BufferedImageEditor.OUTPUT_FORMAT_JPEG);
					heatMaps.put(map.getKey(), img.getAbsolutePath());
					ImageIO.write(LegendUtil.getCustomImage(cgiList.size(), cgiCountMap, null, CGI, null), ReportConstants.JPG,
							new File(folderPath + ReportConstants.FORWARD_SLASH + "cgi_legends" + ReportConstants.DOT_PNG));
					heatMaps.put(CGI+Symbol.UNDERSCORE_STRING+ReportConstants.KEY_LEGENDS,
							folderPath + ReportConstants.FORWARD_SLASH + "cgi_legends" + ReportConstants.DOT_PNG);

				} else if ((map.getKey().equalsIgnoreCase(ReportConstants.CALL_PLOT))) {
					generateHeatMapsForCallData(dataList,folderPath, imgFloorPlann, map.getKey(),heatMaps,
								kpiIndexMap.get(ReportConstants.CALL_INITIATE), kpiIndexMap.get(ReportConstants.CALL_FAILURE),
								kpiIndexMap.get(ReportConstants.CALL_SUCCESS), kpiIndexMap.get(ReportConstants.CALL_DROP),
								kpiIndexMap.get(ReportConstants.X_POINT), kpiIndexMap.get(ReportConstants.Y_POINT));

				} else if ((map.getKey().equalsIgnoreCase(ReportConstants.DL_EARFCN)) && kpiIndexMap.get(ReportConstants.DL_EARFCN) != null) {
					setEarfcnPlot(dataList, folderPath, imgFloorPlann, heatMaps, newearfcncountMap, newearfcncolorMap,
							map, kpiIndexMap.get(ReportConstants.DL_EARFCN), kpiIndexMap.get(ReportConstants.X_POINT),
							kpiIndexMap.get(ReportConstants.Y_POINT));
				} else {

					File file = getHeatMapImage(folderPath, imgFloorPlann, map.getValue(), map.getKey());
					if (file != null) {
						heatMaps.put(map.getKey(), file.getAbsolutePath());
					}
				}
			}
			setCustomImagesInMap(pciList.size(), heatMaps, pciCountMap, newearfcncountMap, newearfcncolorMap,
					folderPath);

			return heatMaps;
		} catch (Exception e) {
			logger.error("Error inside the method generateHeatMapForSingleChannel {}", Utils.getStackTrace(e));
		}
		return null;
	}

	public Map<String, String> generateHeatMapsForHandover(List<String[]> dataList, String folderPath,
			String imgFloorPlann, Map<String, Integer> kpiIndexMap, String key) {
		try {
			Map<String, String> heatMaps = new HashMap<>();
			Integer handoverInitiateIndex = kpiIndexMap.get(QMDLConstant.HANDOVER_INITIATE);
			Integer handoverFailureIndex = kpiIndexMap.get(QMDLConstant.HANDOVER_FAILURE);
			Integer handoverSuccessIndex = kpiIndexMap.get(QMDLConstant.HANDOVER_SUCCESS);
			List<PointF> hoSuccessList = getPointListForEventKpi(dataList, handoverSuccessIndex, INDEX_XPOINT, INDEX_YPOINT, Color.green,handoverFailureIndex,handoverSuccessIndex);
			
			List<PointF> hoFauilreList = getPointListForEventKpi(dataList, handoverFailureIndex, INDEX_XPOINT, INDEX_YPOINT,  Color.red,handoverSuccessIndex,handoverInitiateIndex);
			
			List<PointF> hoIntiateList = getPointListForEventKpi(dataList, handoverInitiateIndex, INDEX_XPOINT, INDEX_YPOINT, Color.blue,handoverSuccessIndex,handoverFailureIndex);
			BufferedImageEditor editor = new BufferedImageEditor(imgFloorPlann);
		
			if(Utils.isValidList(hoIntiateList)) {
				List<Color> hoInColorList = hoIntiateList.stream().map(PointF::getColor).collect(Collectors.toList());

		     	editor.drawPath(hoIntiateList, hoInColorList, 16);
			}
			
			if(Utils.isValidList(hoFauilreList)) {
				logger.info("hoFauilreList {}",hoFauilreList);
				List<Color> hoFauilreColorList = hoFauilreList.stream().map(PointF::getColor).collect(Collectors.toList());

			editor.drawPath(hoFauilreList, hoFauilreColorList, 13);
			}

			if(Utils.isValidList(hoSuccessList)) {
				List<Color> hoSuccessColorList = hoSuccessList.stream().map(PointF::getColor).collect(Collectors.toList());

			editor.drawPath(hoSuccessList, hoSuccessColorList, 10);
			}
			
			String fileName = key + ReportConstants.UNDERSCORE + new Date().getTime();
			File img = editor.finalizeImageToOutput(folderPath + File.separator + fileName + ".jpeg",
					BufferedImageEditor.OUTPUT_FORMAT_JPEG);
			heatMaps.put(key, img.getAbsolutePath());
			return heatMaps;

		}

		catch (Exception e) {
			logger.error("Error inside the method generateHeatMapsForHandover {}", Utils.getStackTrace(e));
		}
		return null;
	}
	
	public Map<String, String> generateHeatMapsForCallData(List<String[]> dataList, String folderPath,
			String imgFloorPlann, String key, Map<String, String> heatMaps, Integer callInitiateIndex, Integer callFailureIndex,
			Integer callSuccessIndex, Integer callDropIndex, Integer xPointIndex, Integer yPointIndex) {
		logger.info("Inside method generateHeatMapsForCallData ");
		try {

			List<PointF> callSuccessList = getPointListForEventKpi(dataList, callSuccessIndex, xPointIndex, yPointIndex, Color.green,callInitiateIndex,callFailureIndex,callDropIndex);

			List<PointF> callFauilreList = getPointListForEventKpi(dataList, callFailureIndex, xPointIndex, yPointIndex, Color.yellow,callInitiateIndex,callSuccessIndex,callDropIndex);

			List<PointF> callIntiateList = getPointListForEventKpi(dataList, callInitiateIndex, xPointIndex, yPointIndex, Color.blue,callFailureIndex,callSuccessIndex,callDropIndex);

			List<PointF> callDropList = getPointListForEventKpi(dataList, callDropIndex, xPointIndex, yPointIndex, Color.red,callInitiateIndex,callFailureIndex,callSuccessIndex);

			BufferedImageEditor editor = new BufferedImageEditor(imgFloorPlann);

			if (Utils.isValidList(callIntiateList)) {
//				logger.info("callIntiateList {}", callIntiateList);
				List<Color> callInitiateColorList = callIntiateList.stream().map(PointF::getColor)
						.collect(Collectors.toList());

				editor.drawPath(callIntiateList, callInitiateColorList, 20);
			}

			if (Utils.isValidList(callFauilreList)) {
//				logger.info("callFauilreList {}", callFauilreList);
				List<Color> callFauilreColorList = callFauilreList.stream().map(PointF::getColor)
						.collect(Collectors.toList());

				editor.drawPath(callFauilreList, callFauilreColorList, 15);
			}

			if (Utils.isValidList(callSuccessList)) {
//				logger.info("callSuccessList {}", callSuccessList);
				List<Color> callSuccessColorList = callSuccessList.stream().map(PointF::getColor)
						.collect(Collectors.toList());

				editor.drawPath(callSuccessList, callSuccessColorList, 18);
			}

			if (Utils.isValidList(callDropList)) {
//				logger.info("callDropList {}", callDropList);
				List<Color> callDropColorList = callDropList.stream().map(PointF::getColor)
						.collect(Collectors.toList());

				editor.drawPath(callDropList, callDropColorList, 10);
			}
			String fileName = key + ReportConstants.UNDERSCORE + new Date().getTime();
			File img = editor.finalizeImageToOutput(folderPath + File.separator + fileName + ".jpeg",
					BufferedImageEditor.OUTPUT_FORMAT_JPEG);

			heatMaps.put(key, img.getAbsolutePath());
			return heatMaps;

		}

		catch (Exception e) {
			logger.error("Error inside the method generateHeatMapsForCallData {}", Utils.getStackTrace(e));
		}
		return null;
	}
	

	public String drawNoAcessImage(String floorplanImg, String imagePath, DriveDataWrapper wrapper,
			InbuildingPointWrapper pointWrapper) {
		BufferedImageEditor editer = new BufferedImageEditor(floorplanImg);
		if (wrapper != null && wrapper.getxPoint() != null && wrapper.getyPoint() != null
				&& pointWrapper.getDisX() != null && pointWrapper.getDisY() != null) {
			editer.drawCircleWithOutFill(wrapper.getxPoint().intValue() + pointWrapper.getDisX(),
					wrapper.getyPoint().intValue() + pointWrapper.getDisY(), Color.RED, 40);
			editer.finalizeImageToOutput(imagePath, BufferedImageEditor.OUTPUT_FORMAT_PNG);
		}
		return imagePath;
	}

	public String getSurveyPathWithColor(List<String[]> dataList, Color color, String folderPath,
										 String imgFloorPlann, Integer xPointIndex, Integer yPointIndex) {
		List<PointF> list = new ArrayList<>();
		List<Color> colors = new ArrayList<>();
		for (String[] value : dataList) {
			if (yPointIndex != null && value.length > yPointIndex) {
				if (xPointIndex != null && !StringUtils.isBlank(value[xPointIndex])
						&& !StringUtils.isBlank(value[yPointIndex])) {
					list.add(new PointF(Double.parseDouble(value[xPointIndex]),
							Double.parseDouble(value[yPointIndex])));
					colors.add(color);
				}
			}
		}
		BufferedImageEditor editor = new BufferedImageEditor(imgFloorPlann);
		if(CollectionUtils.isNotEmpty(colors)) {
			editor.drawPath(list, colors);
		}
		File img = editor.finalizeImageToOutput(
				folderPath + File.separator + "IB_SURVEY_PATH_" + System.currentTimeMillis() + ".png",
				BufferedImageEditor.OUTPUT_FORMAT_PNG);
		return img.getAbsolutePath();
	}

	private void setEarfcnPlot(List<String[]> dataList, String folderPath, String imgFloorPlann,
							   Map<String, String> heatMaps, Map<String, Long> newearfcncountMap, Map<String, Color> newearfcncolorMap,
							   Entry<String, List<PointF>> map, Integer indexDlEarfcn, Integer indexXpoint, Integer indexYpoint) {
		try {
			List<String> earfcnList = getEarfcDataList(dataList, indexDlEarfcn, indexXpoint, indexYpoint);
			HashMap<String, PCIWrapper> earfcnCountMap = prepareEarfcnMap(earfcnList);
			List<Color> colors = getEarfcnColorList(earfcnCountMap, earfcnList);
			BufferedImageEditor editor = new BufferedImageEditor(imgFloorPlann);
			editor.drawPath(map.getValue(), colors);
			String fileName = map.getKey() + "_" + new Date().getTime();
			File img = editor.finalizeImageToOutput(folderPath + File.separator + fileName + ".png",
					BufferedImageEditor.OUTPUT_FORMAT_PNG);
			heatMaps.put(map.getKey(), img.getAbsolutePath());
			logger.info("earfcnCountMap {}", earfcnCountMap);
			for (Entry<String, PCIWrapper> entry : earfcnCountMap.entrySet()) {
				newearfcncountMap.put(entry.getValue().getEarfcn().toString(), entry.getValue().getCount().longValue());
			}
			Map<Long, Color> colorMap = earfcnCountMap.values().stream()
					.collect(Collectors.toMap(PCIWrapper::getEarfcn, PCIWrapper::getColor));

			for (Entry<Long, Color> entry : colorMap.entrySet()) {
				newearfcncolorMap.put(entry.getKey().toString(), entry.getValue());
			}
		} catch (Exception e) {
			logger.info("Exception calculate earfcn {}", Utils.getStackTrace(e));
		}

	}

	private List<Color> getColorList(HashMap<Integer, PCIWrapper> pciCountMap, List<Integer> pciList) {
		List<Color> colorList = new ArrayList<>();
		for (Integer pci : pciList) {
			PCIWrapper wrapper = pciCountMap.get(pci);
			colorList.add(wrapper.getColor());
		}
		return colorList;
	}

	private List<Color> getEarfcnColorList(HashMap<String, PCIWrapper> pciCountMap, List<String> earfcnList) {
		List<Color> colorList = new ArrayList<>();
		for (String earfcn : earfcnList) {
			PCIWrapper wrapper = pciCountMap.get(earfcn);
			colorList.add(wrapper.getColor());
		}
		return colorList;
	}

	private LinkedHashMap<String, PCIWrapper> prepareEarfcnMap(List<String> paramList) {
		LinkedHashMap<String, PCIWrapper> mPciColorMap = new LinkedHashMap<>();
		logger.info("paramList in prepareEarfcnMap {}", paramList);
		for (String earfcn : paramList) {
			if (earfcn != null) {
				if (mPciColorMap.containsKey(earfcn)) {
					PCIWrapper wrapper = mPciColorMap.get(earfcn);
					wrapper.setCount(wrapper.getCount() + 1);
					wrapper.setColor(wrapper.getColor());
					wrapper.setEarfcn(NumberUtils.toLong(earfcn));
					mPciColorMap.put(earfcn, wrapper);
				} else {
					PCIWrapper pciwrapperData = new PCIWrapper();
					pciwrapperData.setEarfcn(NumberUtils.toLong(earfcn));
					pciwrapperData.setColor(generateRandomColor());

					pciwrapperData.setCount(1);
					mPciColorMap.put(earfcn, pciwrapperData);
				}
			}

		}
		logger.info("mPciColorMap{}", mPciColorMap);
		return mPciColorMap;
	}

	private Color generateRandomColor() {
		float r = rand.nextFloat();
		float g = rand.nextFloat();
		float b = rand.nextFloat();
		return new Color(r, g, b);
	}

	private List<Integer> getPciList(List<String[]> dataList, Integer index, Integer xpointIndex, Integer ypointIndex) {
		List<Integer> list = new ArrayList<>();
		for (String[] data : dataList) {
			if (xpointIndex != null && ypointIndex != null &&
					ReportUtil.isValidPoint(data[xpointIndex], data[ypointIndex]) && data[index] != null
					&& !data[index].isEmpty()) {
				try {
					list.add(NumberUtils.toInteger(data[index]));
				} catch (NumberFormatException nm) {
					logger.error(" NumberFormatException");

				} catch (Exception e) {
					logger.error("Exception inside the method getPciList {}", Utils.getStackTrace(e));
				}
			}
		}
		return list;
	}

	private List<String> getEarfcDataList(List<String[]> dataList, Integer indexDlEarfcn, Integer indexXpoint, Integer indexYpoint) {
		List<String> list = new ArrayList<>();
		for (String[] data : dataList) {
			if (ReportUtil.isValidPoint(data[indexXpoint], data[indexYpoint]) && indexDlEarfcn != null && data[indexDlEarfcn] != null
					&& !data[indexDlEarfcn].isEmpty()) {
				try {
					logger.info("earfcn is {}", data[indexDlEarfcn]);
					list.add((data[indexDlEarfcn]));
				} catch (NumberFormatException nm) {
					logger.error(" NumberFormatException");

				} catch (Exception e) {
					logger.error("Exception inside the method getPciList {}", Utils.getStackTrace(e));
				}
			}
		}
		return list;
	}

	private File getHeatMapImage(String folderPath, String imgFloorPlann, List<PointF> pointList, String key) {
		try {
			BufferedImageEditor editor = new BufferedImageEditor(imgFloorPlann);
			List<Color> colorList = pointList.stream().map(PointF::getColor).collect(Collectors.toList());
			editor.drawPath(pointList, colorList);
			String fileName = key + "_" + new Date().getTime();
			return editor.finalizeImageToOutput(folderPath + File.separator + fileName + ".png",
					BufferedImageEditor.OUTPUT_FORMAT_PNG);
		} catch (Exception e) {
			logger.error(" error inside the method getHeatMaps{} ", Utils.getStackTrace(e));
		}
		return null;
	}

	private Map<String, List<PointF>> getKpiDataMap(List<String[]> dataList, String imgFloorPlann, String folderPath,
				List<KPIWrapper> kpiList, Integer dlIndex, Integer ulIndex, Integer xPointIndex, Integer yPointIndex, Integer testTypeIndex) {
		logger.info("inside the method getKpiDataMap ");
		Map<String, List<PointF>> kpiDataMap = new HashMap<>();
		try {
//			logger.info("get point list with kpiWrapper list: {}",kpiList);
			for (KPIWrapper kpiWrapper : kpiList) {
				List<PointF> pointList = getPointList(kpiWrapper, dataList, dlIndex, ulIndex, xPointIndex, yPointIndex, testTypeIndex);
				if (pointList != null && (!pointList.isEmpty())) {
					kpiDataMap.put(kpiWrapper.getKpiName(), pointList);
					kpiDataMap.put(ReportConstants.CALL_PLOT, null);
				}
			}
		} catch (Exception e) {
			logger.error("Error inside the method getKpiDataMap{}", ExceptionUtils.getStackTrace(e));
		}
		return kpiDataMap;
	}

	private HashMap<Integer, PCIWrapper> preparePciColorMap(List<Integer> paramList) {
		HashMap<Integer, PCIWrapper> mPciColorMap = new HashMap<>();
//		logger.info("paramList   ===={}", paramList);
		for (Integer pci : paramList) {
			if (mPciColorMap.containsKey(pci)) {
				PCIWrapper wrapper = mPciColorMap.get(pci);
				wrapper.setCount(wrapper.getCount() + 1);
				mPciColorMap.put(pci, wrapper);
			} else {
				PCIWrapper pciwrapperData = new PCIWrapper();
				pciwrapperData.setPCI(pci);
				pciwrapperData.setColor(generateRandomColor());
				pciwrapperData.setCount(1);
				mPciColorMap.put(pci, pciwrapperData);
			}
		}
		return mPciColorMap;
	}

	private List<PointF> getPointList(KPIWrapper kpiWrapper, List<String[]> dataList, Integer dlIndex, Integer ulIndex, Integer xPointIndex, Integer yPointIndex, Integer testTypeIndex) {

		if (ReportConstants.FTP_DL_THROUGHPUT.equalsIgnoreCase(kpiWrapper.getKpiName())) {

			return getPointListForCustomKpi(kpiWrapper, dataList, ReportConstants.FTP_DOWNLOAD, dlIndex, xPointIndex, yPointIndex, testTypeIndex);
		} else if (ReportConstants.FTP_UL_THROUGHPUT.equalsIgnoreCase(kpiWrapper.getKpiName())) {
			return getPointListForCustomKpi(kpiWrapper, dataList, ReportConstants.FTP_UPLOAD, ulIndex, xPointIndex, yPointIndex, testTypeIndex);

		} else if (ReportConstants.HTTP_UL_THROUGHPUT.equalsIgnoreCase(kpiWrapper.getKpiName())) {
			return getPointListForCustomKpi(kpiWrapper, dataList, ReportConstants.HTTP_UPLOAD, ulIndex, xPointIndex, yPointIndex, testTypeIndex);

		} else if (ReportConstants.HTTP_DL_THROUGHPUT.equalsIgnoreCase(kpiWrapper.getKpiName())) {
			return getPointListForCustomKpi(kpiWrapper, dataList, ReportConstants.HTTP_DOWNLOAD, dlIndex, xPointIndex, yPointIndex, testTypeIndex);

		} else {
			return getPointListForKpi(kpiWrapper, dataList, xPointIndex, yPointIndex);
		}
	}

	private List<PointF> getPointListForCustomKpi(KPIWrapper kpiWrapper, List<String[]> dataList, String testType,
				Integer dataIndex, Integer xPointIndex, Integer yPointIndex, Integer testTypeIndex) {
		logger.info("getPointListForCustomKpi {}", testType);
		List<PointF> pointList = new ArrayList<>();
		boolean flag = false;
		for (String data[] : dataList) {
			if (xPointIndex != null && yPointIndex != null && ReportUtil.isValidPoint(data[xPointIndex], data[yPointIndex])) {
				try {
					if (testTypeIndex != null && data[testTypeIndex] != null
							&& !data[testTypeIndex].isEmpty()
							&& data[testTypeIndex].equalsIgnoreCase(testType)) {
						pointList.add(new PointF(NumberUtils.toDouble(data[xPointIndex]),
								NumberUtils.toDouble(data[yPointIndex]), ColorFinder.getDriveColor(
										kpiWrapper.getRangeSlabs(), NumberUtils.toDouble(data[dataIndex]))));
						flag = true;
					} else {
						pointList.add(new PointF(NumberUtils.toDouble(data[xPointIndex]),
								NumberUtils.toDouble(data[yPointIndex]), Color.GRAY));
					}

				} catch (Exception e) {
					logger.error("Exception in getting point list for custom kpi: {}",e.toString());
				}
			}
		}
		return getPointListIfValid(flag, pointList);
	}

	private List<PointF> getPointListIfValid(boolean flag, List<PointF> pointList) {
		if (flag) {
			return pointList;
		}
		return null;
	}

	private List<PointF> getPointListForKpi(KPIWrapper kpiWrapper, List<String[]> dataList, Integer xPointIndex, Integer yPointIndex) {
		List<PointF> pointList = new ArrayList<>();
		boolean flag = false;

		for (String data[] : dataList) {
			if (xPointIndex != null && yPointIndex != null && ReportUtil.isValidPoint(data[xPointIndex], data[yPointIndex])) {
				try {
					if (data != null && kpiWrapper != null && data.length > kpiWrapper.getIndexKPI()
							&& data[kpiWrapper.getIndexKPI()] != null && !data[kpiWrapper.getIndexKPI()].isEmpty()) {
						Integer ri = 0;
						if (data[kpiWrapper.getIndexKPI()].length() > 4) {
							ri = NumberUtils.toInteger(data[kpiWrapper.getIndexKPI()].substring(4).trim());
						} else if(NumberUtils.isDigits(data[kpiWrapper.getIndexKPI()])) {
							ri = NumberUtils.toInteger(data[kpiWrapper.getIndexKPI()]);
						}
						
						if (kpiWrapper.getKpiName().equalsIgnoreCase(ReportConstants.MIMO)) {
							pointList.add(new PointF(NumberUtils.toDouble(data[xPointIndex]),
									NumberUtils.toDouble(data[yPointIndex]),
									ColorFinder.getMimoColor(ri)));
						} else {
							pointList.add(new PointF(NumberUtils.toDouble(data[xPointIndex]),
									NumberUtils.toDouble(data[yPointIndex]),
									ColorFinder.getDriveColor(kpiWrapper.getRangeSlabs(),
											NumberUtils.toDouble(data[kpiWrapper.getIndexKPI()]))));
						}
						flag = true;

					} else {
						pointList.add(new PointF(NumberUtils.toDouble(data[xPointIndex]),
								NumberUtils.toDouble(data[yPointIndex]), Color.GRAY));
					}

				}

				catch (Exception e) {
					logger.error("Exception inside the method getPointListForKpi {}",Utils.getStackTrace(e));
				}
			}
		}
		return getPointListIfValid(flag, pointList);

	}

	private List<PointF> getPointListForEventKpi(List<String[]> dataList, Integer index, Integer xPointIndex, Integer yPointIndex, Color color, Integer ...indexsDataToEscape) {
		List<PointF> pointList = new ArrayList<>();

		for (String[] data : dataList) {
//			logger.info("data iis {} index {}", data, index);
			if (data != null && ReportUtil.isValidPoint(data[xPointIndex], data[yPointIndex])) {

				try {
					if (index != null && data.length > index && data[index] != null && !data[index].isEmpty()&&!data[index].equalsIgnoreCase("0")) {
						pointList.add(new PointF(NumberUtils.toDouble(data[xPointIndex]),
								NumberUtils.toDouble(data[yPointIndex]), color));
					} else if (isTOValidIndexToEscape(data, indexsDataToEscape)) {

					} else {
						pointList.add(new PointF(NumberUtils.toDouble(data[xPointIndex]),
								NumberUtils.toDouble(data[yPointIndex]), Color.gray));
					}
				} catch (Exception e) {
					logger.error("getPointListForHandOver === {}", Utils.getStackTrace(e));
				}

			}

		}
//		logger.info("pointList {}",pointList);
		return pointList;
	}

	private boolean isTOValidIndexToEscape(String[] data, Integer[] indexsDataToEscape) {
		for(Integer index: indexsDataToEscape) {
			if (index != null && data.length > index && data[index] != null && !data[index].isEmpty()&&!data[index].equalsIgnoreCase("0")) {
				return true ;
			}
		}
		return false;
	}

	private void setCustomImagesInMap(Integer total, Map<String, String> heatMaps,
			HashMap<Integer, PCIWrapper> pciCountMap, Map<String, Long> earfcnCountMap,
			Map<String, Color> earfcnColorMap, String folderPath) {
		logger.info("Inside method setCustomImagesInMap for PCI , EARFCN Images with total value {} folderPath {} ",
				total, folderPath);
		try {

			ImageIO.write(LegendUtil.getCustomImage(total, pciCountMap, null, "LTE PCI", null), ReportConstants.JPG,
					new File(folderPath + ReportConstants.FORWARD_SLASH + "pci_legends" + ReportConstants.DOT_JPG));
			heatMaps.put(ReportConstants.KEY_LEGENDS,
					folderPath + ReportConstants.FORWARD_SLASH + "pci_legends" + ReportConstants.DOT_JPG);
			ImageIO.write(LegendUtil.getCustomImage(null, null, earfcnCountMap, "EARFCN", earfcnColorMap),
					ReportConstants.JPG,
					new File(folderPath + ReportConstants.FORWARD_SLASH + "earfcn_legends" + ReportConstants.DOT_JPG));
			heatMaps.put(ReportConstants.DL_EARFCN + ReportConstants.UNDERSCORE + ReportConstants.KEY_LEGENDS,
					folderPath + ReportConstants.FORWARD_SLASH + "earfcn_legends" + ReportConstants.DOT_JPG);

		} catch (Exception e) {
			logger.error("Exception in method to set Pci legend image in Map {} ", e.getMessage());
		}

	}

	public Map<String, String> generatePCIHeatMapForIBBenchmark(Map<String, List<String[]>> operatorDataMap,
										String folderPath, String imgFloorPlann, List<KPIWrapper> kpiList, Map<String, Integer> kpiIndexMap) {
		logger.info("inside the method generatePCIHeatMapForIBBenchmark operatorDataMap{} kpiList{}",
				operatorDataMap.size(), kpiList.size());
		try {
			Map<String, String> heatMaps = new HashMap<>();
			Map<String, HashMap<Integer, PCIWrapper>> allPCICountMap = new HashMap<>();
			Map<String, Long> newearfcncountMap = new HashMap<>();
			Map<String, Color> newearfcncolorMap = new HashMap<>();
			List<Integer> allPCIList = new ArrayList<>();
			for (Entry<String, List<String[]>> dataList : operatorDataMap.entrySet()) {
				List<Integer> pciList = new ArrayList<>();
				HashMap<Integer, PCIWrapper> pciCountMap = new HashMap<>();
				Map<String, List<PointF>> kpiDataMap = getKpiDataMap(dataList.getValue(), imgFloorPlann, folderPath,
						kpiList, kpiIndexMap.get(ReportConstants.DL_THROUGHPUT), kpiIndexMap.get(ReportConstants.UL_THROUGHPUT),
						kpiIndexMap.get(ReportConstants.X_POINT), kpiIndexMap.get(ReportConstants.Y_POINT), kpiIndexMap.get(ReportConstants.TEST_TYPE));
				for (Entry<String, List<PointF>> map : kpiDataMap.entrySet()) {
					if ((map.getKey().equalsIgnoreCase(ReportConstants.PCI_PLOT))) {
						pciList = getPciList(dataList.getValue(),kpiIndexMap.get(ReportConstants.PCI_PLOT), kpiIndexMap.get(ReportConstants.X_POINT),
								kpiIndexMap.get(ReportConstants.Y_POINT));
						allPCIList.addAll(pciList);
						pciCountMap = preparePciColorMap(pciList);
						allPCICountMap.put(dataList.getKey(), pciCountMap);
						List<Color> i = getColorList(pciCountMap, pciList);
						BufferedImageEditor editor = new BufferedImageEditor(imgFloorPlann);
						editor.drawPath(map.getValue(), i);
						String fileName = map.getKey() + ReportConstants.UNDERSCORE + dataList.getKey();
						File img = editor
								.finalizeImageToOutput(
										folderPath + File.separator + fileName + ReportConstants.UNDERSCORE
												+ System.currentTimeMillis() + ".png",
										BufferedImageEditor.OUTPUT_FORMAT_PNG);
						heatMaps.put(fileName, img.getAbsolutePath());
					}

				}
			}
			logger.info("pciList.size()  {} pciCountMap{}", allPCIList.size(), allPCICountMap);
			setCustomImagesInMapForIBBenchmark(allPCIList.size(), heatMaps, allPCICountMap, newearfcncountMap,
					newearfcncolorMap, folderPath);

			return heatMaps;
		} catch (Exception e) {
			logger.error("Error inside the method generatePCIHeatMapForIBBenchmark {}", Utils.getStackTrace(e));
		}
		return null;
	}

	private void setCustomImagesInMapForIBBenchmark(Integer total, Map<String, String> heatMaps,
			Map<String, HashMap<Integer, PCIWrapper>> pciCountMap, Map<String, Long> earfcnCountMap,
			Map<String, Color> earfcnColorMap, String folderPath) {
		logger.info("Inside method setCustomImagesInMap for PCI , EARFCN Images with total value {} folderPath {} ",
				total, folderPath);
		try {

			ImageIO.write(LegendUtil.getCustomImageForIBBenchmark(total, pciCountMap, null, "LTE PCI", null),
					ReportConstants.JPG, new File(folderPath + ReportConstants.FORWARD_SLASH
							+ ReportConstants.IB_BENCHMARK_PCI_LEGEND + ReportConstants.DOT_JPG));
			heatMaps.put(ReportConstants.KEY_LEGENDS, folderPath + ReportConstants.FORWARD_SLASH
					+ ReportConstants.IB_BENCHMARK_PCI_LEGEND + ReportConstants.DOT_JPG);
			ImageIO.write(LegendUtil.getCustomImage(null, null, earfcnCountMap, "EARFCN", earfcnColorMap),
					ReportConstants.JPG,
					new File(folderPath + ReportConstants.FORWARD_SLASH + "earfcn_legends" + ReportConstants.DOT_JPG));
			heatMaps.put(ReportConstants.DL_EARFCN + ReportConstants.UNDERSCORE + ReportConstants.KEY_LEGENDS,
					folderPath + ReportConstants.FORWARD_SLASH + "earfcn_legends" + ReportConstants.DOT_JPG);

		} catch (Exception e) {
			logger.error("Exception in method to set Pci legend image in Map {} ", e.getMessage());
		}

	}
	
	public File getRouteImage(List<String[]> driveData, String floorPlanImage,
			String folderPath, Map<String, Integer> kpiIndexMap) {
		List<PointF> pointList = new ArrayList<>();
		if (kpiIndexMap.get(ReportConstants.X_POINT) != null && kpiIndexMap.get(ReportConstants.Y_POINT) != null) {
			for (String data[] : driveData) {
				if (ReportUtil.isValidPoint(data[kpiIndexMap.get(ReportConstants.X_POINT)],
						data[kpiIndexMap.get(ReportConstants.Y_POINT)])) {
					pointList.add(new PointF(NumberUtils.toDouble(data[kpiIndexMap.get(ReportConstants.X_POINT)]),
							NumberUtils.toDouble(data[kpiIndexMap.get(ReportConstants.Y_POINT)]), Color.RED));
				}

			}
			return getHeatMapImage(folderPath, floorPlanImage, pointList, ReportConstants.ROUTE);

		}
		return null;

	}

	public File generateSpeedTestImage(List<String[]> driveData, String floorPlanImage, KPIWrapper kpiWrapperForPath,
									   String imageDirectory, Map<String, Integer> kpiIndexMap) {
//		logger.info("Inside generateSpeedTestImage kpiWrapper: {}", kpiWrapperForPath.toString());
		List<String[]> filteredDriveData = driveData.stream()
				.filter(data -> data != null && kpiIndexMap.get(ReportConstants.SPEED_TEST_PIN_NUMBER) != null
						&& data.length > kpiIndexMap.get(ReportConstants.SPEED_TEST_PIN_NUMBER)
						&& StringUtils.isBlank(data[kpiIndexMap.get(ReportConstants.SPEED_TEST_PIN_NUMBER)]))
				.collect(Collectors.toList());
		List<PointF> pointList = getPointList(kpiWrapperForPath, filteredDriveData, kpiIndexMap.get(ReportConstants.DL_THROUGHPUT),
				kpiIndexMap.get(ReportConstants.UL_THROUGHPUT), kpiIndexMap.get(ReportConstants.X_POINT), kpiIndexMap.get(ReportConstants.Y_POINT),
				kpiIndexMap.get(ReportConstants.TEST_TYPE));
		if (pointList != null && !pointList.isEmpty()) {
			BufferedImageEditor editor = new BufferedImageEditor(floorPlanImage);
			List<Color> colorList = pointList.stream().map(PointF::getColor).collect(Collectors.toList());
			editor.drawPath(pointList, colorList);
			List<String[]> speedTestPinList = driveData.stream()
					.filter(data -> data != null && data.length > kpiIndexMap.get(ReportConstants.SPEED_TEST_PIN_NUMBER)
							&& !StringUtils.isBlank(data[kpiIndexMap.get(ReportConstants.SPEED_TEST_PIN_NUMBER)]))
					.collect(Collectors.toList());
			for (String[] singleRow : speedTestPinList) {
				if (NumberUtils.isParsable(singleRow[kpiIndexMap.get(ReportConstants.X_POINT)])
						&& NumberUtils.isParsable(singleRow[kpiIndexMap.get(ReportConstants.Y_POINT)])) {
					editor.drawCircle(
							new PointF(Double.parseDouble(singleRow[kpiIndexMap.get(ReportConstants.X_POINT)]),
									Double.parseDouble(singleRow[kpiIndexMap.get(ReportConstants.Y_POINT)])),
							Color.BLACK, CIRCLE_RADIUS_IB_WIFI_SPEED_TEST_PIN);
					editor.drawText(
							new PointF(Double.parseDouble(singleRow[kpiIndexMap.get(ReportConstants.X_POINT)]) + TEXT_IN_CIRCLE_X_DIFFERENCE,
									Double.parseDouble(singleRow[kpiIndexMap.get(ReportConstants.Y_POINT)]) + TEXT_IN_CIRCLE_Y_DIFFERENCE),
							singleRow[kpiIndexMap.get(ReportConstants.SPEED_TEST_PIN_NUMBER)], Color.WHITE, TEXT_SIZE_IB_WIFI_SPEED_TEST_PIN);
				}
			}
			String fileName = "SPEED_TEST_" + new Date().getTime();
			return editor.finalizeImageToOutput(imageDirectory + File.separator + fileName + ".png",
					BufferedImageEditor.OUTPUT_FORMAT_PNG);
		}

		return null;
	}

	public Map<String, String> generateHeatMapsForKpi(List<String[]> dataList, String folderPath, String imgFloorPlann,
			KPIWrapper kpiWrapper, Map<String, Integer> kpiIndexMap) {
		Map<String, String> heatMaps = new HashMap<>();
		List<PointF> pointList = getPointListForKpi(kpiWrapper, dataList, kpiIndexMap.get(ReportConstants.X_POINT),
				kpiIndexMap.get(ReportConstants.Y_POINT));
		if (pointList != null && (!pointList.isEmpty())) {
			File file = getHeatMapImage(folderPath, imgFloorPlann, pointList, kpiWrapper.getKpiName());
			if (file != null) {
				heatMaps.put(kpiWrapper.getKpiName(), file.getAbsolutePath());
			}
		}
		return heatMaps;

	}
		
		
	
}
