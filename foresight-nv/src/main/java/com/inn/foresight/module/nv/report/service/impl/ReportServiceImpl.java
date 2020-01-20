package com.inn.foresight.module.nv.report.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.ws.rs.core.Response;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.lang.NumberUtils;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.Corner;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.model.NeighbourCellDetail;
import com.inn.foresight.core.infra.service.INeighbourCellDetailService;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.core.report.model.AnalyticsRepository.progress;
import com.inn.foresight.module.fm.core.service.AlarmService;
import com.inn.foresight.module.fm.core.wrapper.AlarmDataWrapper;
import com.inn.foresight.module.fm.layer.service.AlarmLayerService;
import com.inn.foresight.module.fm.layer.wrapper.NEHaveAlarm;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.layer3.constants.Layer3PPEConstant;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HDFSDao;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HbaseDao;
import com.inn.foresight.module.nv.layer3.service.ILayer3PPEService;
import com.inn.foresight.module.nv.layer3.service.INVLayer3DashboardService;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.layer3.wrapper.Layer3PPEWrapper;
import com.inn.foresight.module.nv.report.constant.ReportIndexWrapper;
import com.inn.foresight.module.nv.report.constant.ReportSummaryIndexWrapper;
import com.inn.foresight.module.nv.report.dao.INVReportHbaseDao;
import com.inn.foresight.module.nv.report.dao.impl.NVReportHdfsDaoImpl;
import com.inn.foresight.module.nv.report.ib.utils.floorplandrawer.FloorPlanJsonParser;
import com.inn.foresight.module.nv.report.parse.wrapper.Geography;
import com.inn.foresight.module.nv.report.parse.wrapper.GeographyParser;
import com.inn.foresight.module.nv.report.service.IMapImagesService;
import com.inn.foresight.module.nv.report.service.IReportService;
import com.inn.foresight.module.nv.report.utils.DriveHeaderConstants;
import com.inn.foresight.module.nv.report.utils.LegendUtil;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.wrapper.CustomerComplaintSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.GraphDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.KPISummaryDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.MessageDetailWrapper;
import com.inn.foresight.module.nv.report.wrapper.NVReportConfigurationWrapper;
import com.inn.foresight.module.nv.report.wrapper.PSDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.RecipeMappingWrapper;
import com.inn.foresight.module.nv.report.wrapper.RemarkDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.RemarkReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.ReportSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.SSVTReportSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.SSVTReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.LiveDriveVoiceAndSmsWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.QuickTestWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.YoutubeTestWrapper;
import com.inn.foresight.module.nv.service.ISiteDetailService;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.IWORecipeMappingDao;
import com.inn.foresight.module.nv.workorder.model.CustomGeography.GeographyType;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.service.ICustomGeographyService;
import com.inn.foresight.module.nv.workorder.service.IWOFileDetailService;
import com.inn.foresight.module.nv.workorder.wrapper.CustomGeographyWrapper;
import com.inn.product.legends.dao.ILegendRangeDao;
import com.inn.product.legends.utils.LegendWrapper;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;
import com.inn.product.systemconfiguration.model.SystemConfiguration;
import com.inn.product.um.geography.dao.GeographyL1Dao;
import com.inn.product.um.geography.dao.GeographyL2Dao;
import com.inn.product.um.geography.dao.GeographyL3Dao;
import com.inn.product.um.geography.dao.GeographyL4Dao;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;



@Service("ReportServiceImpl")
public class ReportServiceImpl implements IReportService, ReportConstants {
	/** The logger. */
	private Logger logger = LogManager.getLogger(ReportServiceImpl.class);

	/** The mapper. */
	ObjectMapper mapper = new ObjectMapper();

	/**@Override
    public INVLayer3DashboardService getService() {
        return nvLayer3DashboardService;
    }*/

	@Autowired
	private INVLayer3DashboardService nvLayer3DashboardService;
	@Autowired
	private IMapImagesService mapImageService;
	@Autowired
	private IAnalyticsRepositoryDao analyticsRepositoryDao;
	
	@Autowired
	private NVReportHdfsDaoImpl nVReportHdfsDao;
	@Autowired
	private ILegendRangeDao legendRangeDao;
	@Autowired
	private ISiteDetailService siteDetailService;
	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;
	@Autowired
	private INVLayer3HbaseDao nvLayer3HbaseDao;

	@Autowired
	private GeographyL1Dao iGeographyL1Dao;

	@Autowired
	private GeographyL2Dao iGeographyL2Dao;

	@Autowired
	private GeographyL3Dao iGeographyL3Dao;

	@Autowired
	private GeographyL4Dao iGeographyL4Dao;

	@Autowired
	private INeighbourCellDetailService neighbourCellDetailService;

	@Autowired
	private SystemConfigurationDao iSystemConfigurationDao;

	@Autowired
	private INVLayer3HDFSDao nvLayer3HDFSDao;
	@Autowired
	private IGenericMapService iGenericMapService;

	@Autowired
	private ICustomGeographyService customGeographyService;
	
	@Autowired
	private INVReportHbaseDao nVReportHbaseDao;

    @Autowired
	private IWOFileDetailService wofileDetailService;

	/** The alarm layer service. */
	@Autowired
	private AlarmLayerService alarmLayerService;
	@Autowired
	private AlarmService alarmService;
	@Autowired
	private ILayer3PPEService layer3PPEService;
	@Autowired
	IWORecipeMappingDao recipeMappingDao ;
	/**
	 * Sets the kpi states intokpi list.
	 *
	 * @param kpiList
	 *            the kpi list
	 * @param workOrderId
	 *            the work order id
	 * @param recepiList
	 * @param operatorList
	 * @return the list
	 */
	@Override
	public List<KPIWrapper> setKpiStatesIntokpiList(List<KPIWrapper> kpiList, Integer workOrderId,
			List<String> recepiList, List<String> operatorList) {
		List<KPIWrapper> list = new ArrayList<>();
		kpiList.forEach(kpiWrapper -> {
			try {
				if(kpiWrapper.getKpiName().equalsIgnoreCase(CA)){
					kpiWrapper.setKpiStatMap(getMergedMapData(workOrderId,ReportUtil.getHbaseColumnNameByKpiName(kpiWrapper), recepiList, operatorList));
					list.add(kpiWrapper);
				} else {
					kpiWrapper.setKpiStats(getKPiStatsDataFromHbase(workOrderId,
							ReportUtil.getHbaseColumnNameByKpiName(kpiWrapper), recepiList, operatorList));
					list.add(kpiWrapper);
				}
			} catch (Exception e) {
				logger.error("Exception inside method setKpiStatesIntokpiList  for kpi Name {} ",kpiWrapper.getKpiName());
			}
		});
//		logger.info("kpiList is ====={}", new Gson().toJson(kpiList));

		return list;
	}

	@Override
	public List<KPIWrapper> setKpiStatesIntokpiListForReport(List<KPIWrapper> kpiList, Integer workOrderId,
			List<String> recepiList, List<String> operatorList) {
		List<KPIWrapper> list = new ArrayList<>();
		kpiList.forEach(kpiWrapper -> {
			try {
				if(kpiWrapper.getKpiName().equalsIgnoreCase(CA)){
					kpiWrapper.setKpiStatMap(getMergedMapData(workOrderId,ReportUtil.getHbaseColumnNameByKpiName(kpiWrapper), recepiList, operatorList));
					list.add(kpiWrapper);
				} else {
					kpiWrapper.setKpiStats(getKPiStatsDataFromHbase(workOrderId,
							ReportUtil.getHbaseColumnNameByKpiNameForReport(kpiWrapper), recepiList, operatorList));
					list.add(kpiWrapper);
				}
			} catch (Exception e) {
				logger.error("Exception inside method setKpiStatesIntokpiList  for kpi Name {} ",kpiWrapper.getKpiName());
			}
		});
//		logger.info("kpiList is ====={}", new Gson().toJson(kpiList));

		return list;
	}
	
	@Override
	public ReportSubWrapper getRemarkDataForReport(Integer workorderId,Map<String, List<String>> map){
		logger.info("inside the method getRemarkDataForReport workorderid {}",workorderId);
		ReportSubWrapper reportSubWrapper = new ReportSubWrapper();
		List<RemarkDataWrapper>remarkDataList=new ArrayList<>();
		List<RemarkDataWrapper>testSkipDataList=new ArrayList<>();

		try {
			List<RemarkDataWrapper> dataList = nvLayer3DashboardService.getRemarkTestSkipFromLayer3Report(workorderId,
					map.get(RECIPE), map.get(OPERATOR));

			for (RemarkDataWrapper remarkDataWrapper : dataList) {
				addRemarkDataToList(remarkDataList, remarkDataWrapper);
				addTestSkipDataToList(testSkipDataList, remarkDataWrapper);

			}
			reportSubWrapper.setRemarkDataList(remarkDataList);
			reportSubWrapper.setTestSkipDataList(testSkipDataList);

		} catch (BusinessException e) {
			logger.error("BusinessException inside the method getRemarkDataForReport");
		}
		return reportSubWrapper;

	}
	@Override
	public File generateRemarkReport(Integer workorderId, String filePath, List<File> files,String jasperPath, RemarkReportWrapper mainWrapper) {
		Map<String, List<String>> recipeOperatorMap = nvLayer3DashboardService.getDriveRecipeDetail(workorderId);
		List<ReportSubWrapper>list=new ArrayList<>();
		logger.debug("inside the method generateRemarkReport for workorderId  {}", workorderId);
		try {
			if (recipeOperatorMap != null) {
				boolean tocreate = false;
				ReportSubWrapper reportSubWrapper = getRemarkDataForReport(workorderId,
						recipeOperatorMap);
				if (reportSubWrapper.getRemarkDataList() != null
						&& reportSubWrapper.getRemarkDataList().size() > INDEX_ZER0) {
					tocreate = true;
					mainWrapper.setIsremarkData(TRUE);
				}
				if (reportSubWrapper.getTestSkipDataList() != null
						&& reportSubWrapper.getTestSkipDataList().size() > INDEX_ZER0) {
					tocreate = true;
					mainWrapper.setIstestSkip(TRUE);

				}
				if (tocreate) {
					list.add(reportSubWrapper);
					mainWrapper.setRemarkDataList(list);
					return proceedToCreateRemarkReport(workorderId, filePath,
							REMARK_REPORT,mainWrapper,jasperPath);
				}
			}
		} catch (Exception e) {
			logger.error("Exception to generateRemarkReport {} ", e.getMessage());
		}
		return null;
	}

	private File proceedToCreateRemarkReport(Integer workorderId, String filePath, String remarkReport, RemarkReportWrapper mainWrapper, String jasperPath) {
		try {
			String reportAssetRepo = jasperPath + REMARK_JASPER_FOLDER + FORWARD_SLASH;
			logger.info("REPORT_ASSET_REPO{}" , reportAssetRepo);
			List<RemarkReportWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(mainWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);

			Map<String, Object> imageMap = new HashMap<>();
			imageMap.put(SUBREPORT_DIR, reportAssetRepo);
			imageMap.put(IMAGE_PARAM_HEADER_LOGO, reportAssetRepo +IMAGE_HEADER_LOGO);
			imageMap.put(IMAGE_PARAM_HEADER_BG, reportAssetRepo + IMAGE_HEADER_BG);
			imageMap.put(IMAGE_PARAM_HEADER_LOG, reportAssetRepo + IMAGE_HEADER_LOG);
			String destinationFileName = ReportUtil.getFileName(workorderId + remarkReport,
					workorderId,filePath);
			JasperRunManager.runReportToPdfFile(reportAssetRepo + REMARK_REPORT_JASPER, destinationFileName, imageMap,rfbeanColDataSource);
			return ReportUtil.getIfFileExists(destinationFileName);

		} catch (JRException e) {
			logger.error("Exception inside the method proceedToCreateRemarkReport{} ",Utils.getStackTrace(e));
		}
		return null;
	}

	private void addTestSkipDataToList(List<RemarkDataWrapper> testSkipDataList, RemarkDataWrapper remarkDataWrapper) {
		if(remarkDataWrapper.getTestSkipData()!=null) {
			List<String[]> remarkDataArray = ReportUtil
					.convertCSVStringToDataList(remarkDataWrapper.getTestSkipData());
			setTestSkipDataToWrapper(remarkDataArray,testSkipDataList);

		}
	}

	private void addRemarkDataToList(List<RemarkDataWrapper> remarkDataList, RemarkDataWrapper remarkDataWrapper) {
		if (remarkDataWrapper.getRemarkData() != null) {
			//logger.info("remarkDataWrapper =={}",remarkDataWrapper.getRemarkData());
			List<String[]> remarkDataArray = ReportUtil
					.convertCSVStringToDataList(remarkDataWrapper.getRemarkData());
			setRemarkDataToWrapper(remarkDataArray,remarkDataList);

		}
	}

	private void setTestSkipDataToWrapper(List<String[]> remarkDataArray, List<RemarkDataWrapper> testSkipDataList) {
		for (String[] remarkDataWrapper : remarkDataArray) {
			RemarkDataWrapper wrapper = new RemarkDataWrapper();
			if (remarkDataWrapper.length >= INDEX_ZER0) {
				wrapper.setRecipeName(remarkDataWrapper[ INDEX_ZER0]);
			}
			if (remarkDataWrapper.length >= INDEX_ONE) {
				wrapper.setTestStatus(remarkDataWrapper[INDEX_ONE]);
			}
			if (remarkDataWrapper.length >= INDEX_TWO) {
				wrapper.setTestType(remarkDataWrapper[INDEX_TWO]);
			}
			if (remarkDataWrapper.length >=INDEX_THREE) {
				wrapper.setFaliuerCause(remarkDataWrapper[INDEX_THREE].replace("]]", ""));
			}
			testSkipDataList.add(wrapper);
		}

	}

	private void setRemarkDataToWrapper(List<String[]> remarkDataArray, List<RemarkDataWrapper> remarkDataList) {
		for (String[] remarkDataWrapper : remarkDataArray) {
			RemarkDataWrapper wrapper=new RemarkDataWrapper();
			if (remarkDataWrapper.length >= INDEX_ZER0) {
				wrapper.setRecipeName(remarkDataWrapper[INDEX_ZER0]);
			}
			if (remarkDataWrapper.length >=INDEX_ONE) {
				wrapper.setTestType(remarkDataWrapper[INDEX_ONE]);
			}
			if (remarkDataWrapper.length >=INDEX_TWO) {

				wrapper.setRemark(remarkDataWrapper[INDEX_TWO].replace("]]", ""));
			}

			remarkDataList.add(wrapper);
		}
	}

	/**
	 * Gets the k pi stats data from hbase.
	 *
	 * @param workOrderId
	 *            the work order id
	 * @param kpiname
	 *            the kpiname
	 * @return the k pi stats data from hbase
	 */
	private String[] getKPiStatsDataFromHbase(Integer workOrderId, String kpiname, List<String> recepiList,
			List<String> operatorList) {
		String[] kpistats = null;
		try {
			String kpistat = nvLayer3DashboardService.getKpiStatsRecipeDataForReport(workOrderId, kpiname, recepiList,
					operatorList);
//			logger.info(KPINAME_KPISTATS,kpiname ,kpistat);
			Map<String, String[]> map = mapper.readValue(kpistat, new TypeReference<Map<String, String[]>>() {
			});
			kpistats = map.get(RESULT);
//			logger.info("kpistats {} ", kpistats != null ? Arrays.toString(kpistats) : null);
			return kpistats;
		} catch (Exception e) {
			logger.error("Exception inside method getKPiStatsDataFromHbase for kpiName {}  , {} ",kpiname, e.getMessage());
		}
		return kpistats;
	}

	/**
	 * Sets the summary data.
	 *
	 * @param dataKPIs
	 *            the data KP is
	 * @param listOfKPidata
	 *            the list of K pidata
	 * @param operator
	 * @param reportType
	 * @return the KPI summary data from csv data
	 */

	@Override
	public List<KPISummaryDataWrapper> getKPISummaryDataForWorkOrderId(List<Integer> workOrderIds, List<String[]> dataKPIs,
			List<KPIWrapper> listOfKPidata, String[] summaryData, String reportType, String operator) {
		logger.info("Inside method getKPISummaryDataForWorkOrderId  for id {} , reportType {} , operator {}  ", workOrderIds,reportType, operator);
		List<Object[]> objectList=iGenericWorkorderDao.getNVReportConfiguration(reportType);
		List<NVReportConfigurationWrapper> nvWrapperList =ReportUtil.getNVReportconfiguratioinList(objectList);
	    return getKpiDataForSummary(dataKPIs, listOfKPidata,nvWrapperList);
	}

	/**
	 * Gets the Summary data for Kpi.
	 *
	 * @param dataKPIs
	 *            the data KP is
	 * @param listOfKPidata
	 *            the list of K pidata
	 * @param nvWrapperList
	 * @return the kpi data for summary
	 */
	private List<KPISummaryDataWrapper> getKpiDataForSummary(List<String[]> dataKPIs, List<KPIWrapper> listOfKPidata,
			List<NVReportConfigurationWrapper> nvWrapperList) {
		List<KPISummaryDataWrapper> listofSummaryData = new ArrayList<>();
		for (KPIWrapper kpiwrapper : listOfKPidata) {

			try {
				List<NVReportConfigurationWrapper> filterList = nvWrapperList.stream().filter(wrapper->wrapper.getKpi().equalsIgnoreCase(kpiwrapper.getKpiName())).collect(Collectors.toList());
				if(filterList!=null && !filterList.isEmpty()){
					kpiwrapper.setTargetValue(Double.parseDouble(filterList.get(0).getTargetvalue()));
				}
				if (kpiwrapper.getTargetValue() != null) {
					List<String[]> tempList = dataKPIs.stream()
							.filter(dataArray -> dataArray.length > kpiwrapper.getIndexKPI() && ((dataArray[kpiwrapper.getIndexKPI()] != null) 
									&& !dataArray[kpiwrapper.getIndexKPI()].isEmpty()))
							.collect(Collectors.toList());
					logger.info("tempList Size {} , for kpi {} ", tempList != null ? tempList.size() : null,
							kpiwrapper.getIndexKPI());
					if (tempList != null && tempList.size() > ForesightConstants.ZERO) {

						KPISummaryDataWrapper wrapper = new KPISummaryDataWrapper();

						List<Double> list = ReportUtil.getIndexDataList(tempList, kpiwrapper.getIndexKPI());

						long count = list.stream().filter(value -> value > kpiwrapper.getTargetValue()).count();

						Double achivedValue = (count * HUNDRED) / list.size();
						achivedValue = ReportUtil.parseToFixedDecimalPlace(achivedValue, INDEX_TWO);
						wrapper.setTestName(
								kpiwrapper.getKpiName().replaceAll(UNDERSCORE, SPACE)
								+ GREATER_THAN + kpiwrapper.getTargetValue()
								+ ForesightConstants.SPACE + OPEN_BRACKET
								+ PERCENT + CLOSED_BRACKET);
						wrapper.setAchived(achivedValue.toString());
						wrapper.setTarget(N_SLASH_A);
						wrapper.setStatus(N_SLASH_A);
						wrapper.setKpiName(kpiwrapper.getKpiName());
						wrapper.setSource(
								DT_MOBILE.replace(UNDERSCORE, SPACE));
						listofSummaryData.add(wrapper);
					}
				}
			} catch (NumberFormatException e) {
				logger.error("NumberFormatException occured inside method getKpiDataForSummary {} ",Utils.getStackTrace(e));
			} catch (Exception e) {
				logger.error("Exception inside method getKpiDataForSummary {} ", Utils.getStackTrace(e));
			}
		}
		return listofSummaryData;
	}

	/**
	 * Method return the kpi Legend Images   .
	 *
	 * @param list the list
	 * @return retuns the Map with kpiName as key and BufferedImage as value
	 */
	@Override
	public HashMap<String, BufferedImage> getLegendImages(List<KPIWrapper> list,  List<String[]> dataKPIs, Integer testTypeIndex) {
		logger.info("Inside method getLegendImages to save the image on disk {} ", list != null ? list.size() : null);
		HashMap<String, BufferedImage> imageMap = new HashMap<>();
		if (dataKPIs != null) {
			list = LegendUtil.populateLegendData(list, dataKPIs, testTypeIndex);
		}
		for (KPIWrapper wrapper : list) {
			try {
				if (wrapper.getKpiName().equalsIgnoreCase(MIMO)) {
					imageMap.put(LEGEND + UNDERSCORE + wrapper.getIndexKPI(),
							LegendUtil.getLegendImage(
									wrapper.getKpiName().replace(UNDERSCORE, SPACE),
									wrapper.getKpiStats()));
				} else if (wrapper.getKpiName().equalsIgnoreCase(CA) && wrapper.getKpiStatMap()!=null) {
					imageMap.put(LEGEND + UNDERSCORE + wrapper.getIndexKPI(),
							LegendUtil.writeKpiDataFromMap(wrapper.getKpiStatMap(), wrapper.getKpiName()
									.replace(UNDERSCORE, SPACE)));
				} else if (wrapper.getKpiName().equalsIgnoreCase(SERVING_SYSTEM)) {

				} else {
					String unit = ReportUtil.getUnitByKPiName(wrapper.getKpiName());
					String kpiUnit = (unit != null && !unit.isEmpty()) ? SPACE
							+ OPEN_BRACKET + unit + CLOSED_BRACKET
							: BLANK_STRING;
					imageMap.put(LEGEND + UNDERSCORE + wrapper.getIndexKPI(),
							LegendUtil.getLegendImage(wrapper.getTotalCount(),
									wrapper.getKpiName().replace(UNDERSCORE, SPACE)
									+ kpiUnit,
									wrapper.getRangeSlabs()));
				}
			} catch (Exception e) {
				logger.error("Exception occured in generating legend Image {} ", Utils.getStackTrace(e));
			}
		}
		logger.info("Going to return the legendImage MAp of Size {} ",imageMap.size());
		return imageMap;
	}

	@Override
	public HashMap<String, BufferedImage> getLegendImagesForReport(List<KPIWrapper> list,  List<String[]> dataKPIs, Map<String, Integer> kpiIndexMap) {
		logger.info("Inside method getLegendImages to save the image on disk {} ", list != null ? list.size() : null);
		HashMap<String, BufferedImage> imageMap = new HashMap<>();
		if (dataKPIs != null) {
			list = LegendUtil.populateLegendData(list, dataKPIs, kpiIndexMap.get(TEST_TYPE));
		}
		for (KPIWrapper wrapper : list) {
			try {
				if (wrapper.getKpiName().equalsIgnoreCase(MIMO)) {
					logger.debug("inside MIMO image map");
					imageMap.put(LEGEND + UNDERSCORE + wrapper.getIndexKPI(),
							LegendUtil.getLegendImageForMimo(dataKPIs, kpiIndexMap));
				} else if (wrapper.getKpiName().equalsIgnoreCase(CA) && wrapper.getKpiStatMap()!=null) {
					imageMap.put(LEGEND + UNDERSCORE + wrapper.getIndexKPI(),
							LegendUtil.writeKpiDataFromMap(wrapper.getKpiStatMap(), wrapper.getKpiName()
									.replace(UNDERSCORE, SPACE)));
				} else if (wrapper.getKpiName().equalsIgnoreCase(SERVING_SYSTEM)) {

				} else {
					String unit = ReportUtil.getUnitByKPiName(wrapper.getKpiName());
					String kpiUnit = (unit != null && !unit.isEmpty()) ? SPACE
							+ OPEN_BRACKET + unit + CLOSED_BRACKET
							: BLANK_STRING;
					imageMap.put(LEGEND + UNDERSCORE + wrapper.getIndexKPI(),
							LegendUtil.getLegendImage(wrapper.getTotalCount(),
									wrapper.getKpiName().replace(UNDERSCORE, SPACE)
									+ kpiUnit,
									wrapper.getRangeSlabs()));
				}
			} catch (Exception e) {
				logger.error("Exception occured in generating legend Image {} ", Utils.getStackTrace(e));
			}
		}
		logger.info("Going to return the legendImage MAp of Size {} ",imageMap.size());
		return imageMap;
	}

	@Override
	public String getDriveData(Integer workorderId, Map<String, List<String>> map){
		String combineData=null;
		try {
			
			combineData = nvLayer3DashboardService.getDriveDetailReceipeWise(workorderId,map.get(RECIPE), map.get(OPERATOR));
			
			Map<String, String> dataMap = ReportUtil.convertCSVStringToMap(combineData);
			return dataMap.get(RESULT);
		} catch (BusinessException e) {
			logger.error("BusinessException occured inside method getDriveData for workorderId {} , recipeMap {} , msg {}  ",workorderId,map,e.getMessage());
		}
		return combineData;
	}

		
	@SuppressWarnings("unchecked")
	@Override
	public List<String[]> getDriveDataForReport(Integer workorderId, List<String> recipeList, List<String> kpiList){
		try {
			List<String> rowPrefixList = layer3PPEService.getPrefixListFromParamList(workorderId, recipeList,null).stream().collect(Collectors.toList());
			return getAdvanceKpiDataFromPrefixList(kpiList, rowPrefixList);
			}
		catch(Exception e) {
			logger.error("Error inside the method getSummaryData==== {}",Utils.getStackTrace(e));
		}
		return null;
	}
	@Override
	public List<String[]> getDriveDataForReport(List<Integer> workorderIds,List<String> recipeList, List<String> kpiList){
		try {
			List<String> prefixList = getRowPrefixForWorkorderIds(workorderIds,recipeList);
			//List<String> rowPrefixList = layer3PPEService.getPrefixListFromParamList(workorderId, recipeList).stream().collect(Collectors.toList());
			return getAdvanceKpiDataFromPrefixList(kpiList, prefixList);
			}
		catch(Exception e) {
			logger.error("Error inside the method getSummaryData==== {}",Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<String[]> getDriveDataForScanner(List<Integer> workorderIds,List<String> recipeList, List<String> kpiList){
		try {
			List<String> prefixList = getRowPrefixForWorkorderIds(workorderIds,recipeList);
			//List<String> rowPrefixList = layer3PPEService.getPrefixListFromParamList(workorderId, recipeList).stream().collect(Collectors.toList());
			return getAdvanceKpiDataFromPrefixListForScanner(kpiList, prefixList);
		}
		catch(Exception e) {
			logger.error("Error inside the method getSummaryData==== {}",Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<String[]> getDriveDataRecipeWiseTaggedForReport(List<Integer> workorderIds, List<String> recipeList,
			List<String> kpiList, Map<String, Integer> kpiIndexMap) {
		try {
			List<String> prefixList = getRowPrefixForWorkorderIds(workorderIds, recipeList);
			//List<String> rowPrefixList = layer3PPEService.getPrefixListFromParamList(workorderId, recipeList).stream().collect(Collectors.toList());
			Map<String, List<String>> prefixMap = prefixList.stream()
															.filter(StringUtils::isNotBlank)
															.collect(Collectors.toMap(x -> x,
																	x -> Arrays.asList(new String[] { x })));
			logger.info("PrefixMap is: {}", new Gson().toJson(prefixMap));
			return getAdvanceKpiDataFromPrefixList(kpiList, prefixMap, kpiIndexMap);
		} catch (Exception e) {
			logger.error("Error inside the method getSummaryData==== {}", Utils.getStackTrace(e));
		}
		return null;
	}

	private List<String> getRowPrefixForWorkorderIds(List<Integer> workorderIds, List<String> recipeList) {
		List<WORecipeMapping> mappings = recipeMappingDao.getWORecipeByGWOIds(workorderIds);
		List<String> prefixList = new ArrayList<>();

		for (WORecipeMapping woRecipeMapping : mappings) {
			if (recipeList.contains(String.valueOf(woRecipeMapping.getId()))) {
				prefixList.add(NVLayer3Utils.getRowKeyForLayer3PPE(woRecipeMapping.getGenericWorkorder().getId(),
						String.valueOf(woRecipeMapping.getId())));
			}
		}
		return prefixList;
	}

	private List<String[]> getAdvanceKpiDataFromPrefixList(List<String> kpiList, List<String> rowPrefixList) {
		Map<String,List<String>> map = new HashMap<>();
		logger.info("rowPrefixList : {}", rowPrefixList);
		map.put(Layer3PPEConstant.ADVANCE, rowPrefixList);
		Map<String, List<List<String>>> responseMap = (Map<String, List<List<String>>>)layer3PPEService.getPPEDataForMap(kpiList, map, ConfigUtils.getString("LAYER3_DRIVE_DETAIL_TABLE"),Layer3PPEConstant.ADVANCE);
		return getSortedDataList(responseMap.get(Layer3PPEConstant.ADVANCE).stream().map(m -> m.stream().toArray(String[]::new)).collect(Collectors.toList()), kpiList.indexOf(
				ReportConstants.DB_TIMESTAMP_KEY));
	}

	private List<String[]> getAdvanceKpiDataFromPrefixListForScanner(List<String> kpiList, List<String> rowPrefixList) {
		Map<String,List<String>> map = new HashMap<>();
		logger.info("rowPrefixList : {}", rowPrefixList);
		map.put(Layer3PPEConstant.ADVANCE, rowPrefixList);
		Map<String, List<List<String>>> responseMap = (Map<String, List<List<String>>>)layer3PPEService.getPPEDataForMap(kpiList, map, ConfigUtils.getString("LAYER3_DRIVE_DETAIL_TABLE"),Layer3PPEConstant.ADVANCE);
		return responseMap.get(Layer3PPEConstant.ADVANCE).stream().map(m -> m.stream().toArray(String[]::new)).collect(Collectors.toList());
	}

	private List<String[]> getAdvanceKpiDataFromPrefixList(List<String> kpiList, Map<String, List<String>> rowPrefixMap,
			Map<String, Integer> kpiIndexMap) {
		Map<String, List<List<String>>> responseMap = (Map<String, List<List<String>>>) layer3PPEService.getPPEDataForMap(
				kpiList, rowPrefixMap, ConfigUtils.getString("LAYER3_DRIVE_DETAIL_TABLE"), Layer3PPEConstant.ADVANCE);
		List<String[]> data = new ArrayList<>();
		for (Entry<String, List<List<String>>> responseEntry : responseMap.entrySet()) {
			List<String[]> entryData = responseEntry.getValue()
													.stream()
													.map(m -> m.stream().toArray(String[]::new))
													.collect(Collectors.toList());
			List<String[]> sortedDataList= getSortedDataList(entryData, kpiList.indexOf(ReportConstants.DB_TIMESTAMP_KEY));
			ReportUtil.addPciInEmptyFields(sortedDataList, kpiIndexMap);
//			ReportUtil.addTestTypeInEmptyFields(sortedDataList, kpiIndexMap);
			ReportUtil.addKPIInEmptyFields(sortedDataList, kpiIndexMap.get(ReportConstants.TEST_TYPE));
			data.addAll(sortedDataList);
		}
		return data;
		//return getSortedDataList(data, kpiList.indexOf(ReportConstants.DB_TIMESTAMP_KEY));
	}

	@Override
	public HashMap<String, String> getImagesForReport(List<KPIWrapper> kpiList, DriveImageWrapper driveImageWrapper,
			List<DriveDataWrapper> imageDataList, String operator,String reportType) throws IOException {
		List<Double[]> pinLatLonList = null;
		if (imageDataList != null) {
			pinLatLonList = imageDataList	.stream()
					.map(driveDataWrapper -> new Double[] { driveDataWrapper.getLongitude(),
							driveDataWrapper.getLatitutde() })
					.collect(Collectors.toList());
		}
		modifyIndexOfCustomKpis(kpiList);
		HashMap<String, BufferedImage> bufferdImageMap = mapImageService.getDriveImages(driveImageWrapper,pinLatLonList);
		String imagePath = ConfigUtils.getString(IMAGE_PATH_FOR_NV_REPORT) + reportType
				+ FORWARD_SLASH
				+ ReportUtil.getFormattedDate(new Date(), DATE_FORMAT_DD_MM_YY_HH_SS)
				+ FORWARD_SLASH+((operator!=null)?operator.replace(SPACE, BLANK_STRING):BLANK_STRING);
	//	logger.info("Image PAth finally  is {} ",imagePath);
		return mapImageService.saveDriveImages(bufferdImageMap, imagePath, false);
	}

	@Override
	public String getDriveData(Integer workOrderId, List<String> recipeList, List<String> operatorList) {
		String combineData=null;
		try {
			combineData = nvLayer3DashboardService.getDriveDetailReceipeWise(workOrderId,recipeList,operatorList);
			Map<String, String> dataMap = ReportUtil.convertCSVStringToMap(combineData);
			return dataMap.get(RESULT);
		} catch (BusinessException e) {
			logger.error("BusinessException occured inside method getDriveData for workorderId {} , recipeList {} , operatorList {} , msg {}",
					e.getMessage(),workOrderId,recipeList,operatorList);
		}
		return combineData;
	}

	@Override
	public String getSummaryData(Integer workorderId, Map<String, List<String>> map){
		try {
			logger.info("WORKORDER_ID {} , RECIPE LIST {} , OPERATOR LIST {} ",workorderId,map.get(RECIPE), map.get(OPERATOR));
			
			String summaryData = nvLayer3DashboardService.getDriveSummaryReceipeWise(workorderId,
					map.get(RECIPE), map.get(OPERATOR));
			logger.error("summaryData ===={}",summaryData);
			//Map<String, String> summaryMap = ReportUtil.convertCSVStringToMap(summaryData);
			Map<String, Object> summaryMap = mapper.readValue(summaryData, new TypeReference<Map<String, Object>>() {});
			Map<String, String> dataMap = (Map<String, String>) summaryMap.get(RESULT);
			return dataMap.get(SUMMARY);
		}catch(Exception e) {
			logger.error("Error inside the method getSummaryData {}",Utils.getStackTrace(e));
		}
		return null;
	}

	
	@Override
	public String getSummaryData(Integer workOrderId, List<String> recipeList, List<String> operatorList) {
		logger.info("Inside method getSummaryData for workOrderId {} , recipeList {} , operatorList {} ",workOrderId,recipeList,operatorList);
		try {
	//		logger.info("workOrderId : {},  recipeList : {}, operatorList : {}",workOrderId, recipeList,operatorList);
			String summaryData = nvLayer3DashboardService.getDriveSummaryReceipeWiseOld(workOrderId,recipeList,operatorList);
			Map<String, String> summaryMap = ReportUtil.convertCSVStringToMap(summaryData);
	//    	logger.info("getSummaryData : {} , {}",summaryData, summaryMap);
			return summaryMap.get(RESULT);
		}catch(Exception e) {
			logger.error("Error inside the method getSummaryData {} ",Utils.getStackTrace(e));
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String[] getSummaryDataForReport(Integer workorderId,  List<String> recipeList, List<String> columnsList){
		try {
			List<String> rowPrefixList = layer3PPEService.getPrefixListFromParamList(workorderId, recipeList,null).stream().collect(Collectors.toList());
			Map<String,List<String>> map = new HashMap<>();
			logger.info("rowPrefixList : {}", rowPrefixList);
			map.put(Layer3PPEConstant.SUMMARY, rowPrefixList);
			Map<String, List<String>> response = (Map<String, List<String>>)layer3PPEService.getPPEDataForMap(columnsList, map, ConfigUtils.getString(LAYER3_SUMMARY_TABLE),Layer3PPEConstant.SUMMARY);
			String summaryData = response.get(Layer3PPEConstant.SUMMARY).toString();
			logger.info("workorderId: {}, REport summary data : {}",workorderId, summaryData.length());
			return ReportUtil.convertCSVStringToDataListStationary(summaryData);
			}
		catch(Exception e) {
			logger.error("Error inside the method getSummaryData {}",Utils.getStackTrace(e));
		}
		return null;
	}
	@Override
	public String[] getSummaryDataForReport(List<Integer> workorderIds,  List<String> recipeList, List<String> columnsList){
		try {
			List<String> rowPrefixList = getRowPrefixForWorkorderIds(workorderIds, recipeList);
			Map<String,List<String>> map = new HashMap<>();
			logger.info("rowPrefixList : {}", rowPrefixList);
			map.put(Layer3PPEConstant.SUMMARY, rowPrefixList);
			Map<String, List<String>> response = (Map<String, List<String>>)layer3PPEService.getPPEDataForMap(columnsList, map, ConfigUtils.getString(LAYER3_SUMMARY_TABLE),Layer3PPEConstant.SUMMARY);
			String summaryData = response.get(Layer3PPEConstant.SUMMARY).toString();
	//		logger.info("workorderId: {}, REport summary data : {}",workorderId, summaryData.length());
			return ReportUtil.convertCSVStringToDataListStationary(summaryData);
			}
		catch(Exception e) {
			logger.error("Error inside the method getSummaryData {}",Utils.getStackTrace(e));
		}
		return null;
	}
	
	@Override
	public List<KPIDataWrapper> populateKPiAggData(String smryData,List<KPIWrapper> listOfKpiWrapper) {
		try {
	//		logger.debug("smryData : {},  listOfKpiWrapper : {} ",smryData,new Gson().toJson(listOfKpiWrapper));
			String[] summaryData = smryData.replaceAll("\\]", "").replaceAll("\\[", "").split(COMMA);
			List<KPIDataWrapper> listOfKpiDataWrapper1 = new ArrayList<>();
			KPIDataWrapper kpiwrapper = null;
			for (KPIWrapper wrapper : listOfKpiWrapper) {
				switch(wrapper.getKpiName()){
				case DriveHeaderConstants.RSRP:
					kpiwrapper= getKPiWrapper(wrapper,summaryData,DriveHeaderConstants.SUMMARY_RSRP_INDEX,DriveHeaderConstants.SUMMARY_MIN_RSRP_INDEX,DriveHeaderConstants.SUMMARY_MAX_RSRP_INDEX);
					break;
				case DriveHeaderConstants.SINR:
					kpiwrapper= getKPiWrapper(wrapper,summaryData,DriveHeaderConstants.SUMMARY_SINR_INDEX,DriveHeaderConstants.SUMMARY_MIN_SINR_INDEX,DriveHeaderConstants.SUMMARY_MAX_SINR_INDEX);
					break;
				case DriveHeaderConstants.UL_THROUGHPUT:
					kpiwrapper= getKPiWrapper(wrapper,summaryData,DriveHeaderConstants.SUMMARY_UL_INDEX,DriveHeaderConstants.SUMMARY_MIN_UL_INDEX,DriveHeaderConstants.SUMMARY_MAX_UL_INDEX);
					break;
				case DriveHeaderConstants.DL_THROUGHPUT:
					kpiwrapper= getKPiWrapper(wrapper,summaryData,DriveHeaderConstants.SUMMARY_DL_INDEX,DriveHeaderConstants.SUMMARY_MIN_DL_INDEX,DriveHeaderConstants.SUMMARY_MAX_DL_INDEX);
					break;
				case DriveHeaderConstants.JITTER:
					kpiwrapper= getKPiWrapper(wrapper,summaryData,DriveHeaderConstants.SUMMARY_JITTER_INDEX,DriveHeaderConstants.SUMMARY_MIN_JITTER_INDEX,DriveHeaderConstants.SUMMARY_MAX_JITTER_INDEX);
					break;
				case DriveHeaderConstants.LATENCY:
					kpiwrapper= getKPiWrapper(wrapper,summaryData,DriveHeaderConstants.SUMMARY_LATENCY_INDEX,DriveHeaderConstants.SUMMARY_MIN_LATENCY_INDEX,DriveHeaderConstants.SUMMARY_MAX_LATENCY_INDEX);
					break;
				case DriveHeaderConstants.MOS:
					kpiwrapper= getKPiWrapper(wrapper,summaryData,DriveHeaderConstants.SUMMARY_MOS_INDEX,DriveHeaderConstants.SUMMARY_MIN_MOS_INDEX,DriveHeaderConstants.SUMMARY_MAX_MOS_INDEX);
					break;
				default:
					kpiwrapper=null;
					break;
				}
	//			logger.debug("kpiwrapper data {} ",kpiwrapper);
				listOfKpiDataWrapper1.add(kpiwrapper);
			}
//			logger.debug("listOfKpiDataWrapper : {} ",new Gson().toJson(listOfKpiDataWrapper1));
			return listOfKpiDataWrapper1;
		} catch (Exception e) {
			logger.error("Exception inside the method setSummaryData{}", e.getMessage());
		}
		return Collections.emptyList();
	}

	private KPIDataWrapper getKPiWrapper(KPIWrapper wrapper, String[] summaryData, Integer avgKpiIndex,
			Integer minkpiIndex, Integer maxkpiIndex) {
		KPIDataWrapper kpidataWrapper = new KPIDataWrapper();
		try {
			kpidataWrapper.setKpiName(wrapper.getKpiName());
			kpidataWrapper.setKpiId(wrapper.getIndexKPI());
			kpidataWrapper.setAvgValue(StringUtils.isBlank(summaryData[avgKpiIndex])?null:Double.parseDouble(summaryData[avgKpiIndex]));
			kpidataWrapper.setMinValue(StringUtils.isBlank(summaryData[minkpiIndex])?null:Double.parseDouble(summaryData[minkpiIndex]));
			kpidataWrapper.setMaxValue(StringUtils.isBlank(summaryData[maxkpiIndex])?null:Double.parseDouble(summaryData[maxkpiIndex]));
		} catch (Exception e) {
			logger.error("Error in getKPiWrapper methdo for kpi {} , {} ", wrapper.getKpiName(),
					Utils.getStackTrace(e));
		}
		return kpidataWrapper;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getJsonDataMap(String json){
		Map<String, Object> jsonMap=null;
		try {
			jsonMap = new ObjectMapper().readValue(json, HashMap.class);
			Integer analyticsrepoId = (Integer) jsonMap.get(ForesightConstants.ANALYTICAL_REPORT_KEY);
			logger.info("analyticsid {}", analyticsrepoId);
			AnalyticsRepository analyticrepositoryObj = analyticsRepositoryDao.findByPk(analyticsrepoId);
			logger.info("analyticsrepositoryId {} , {}  ",analyticsrepoId,analyticrepositoryObj.getReportConfig());
			String reportConfig =  analyticrepositoryObj.getReportConfig()!=null?analyticrepositoryObj.getReportConfig().replaceAll("\'", "\""):null;
			Map<String, Object> configMap = new ObjectMapper().readValue(reportConfig, HashMap.class);
			logger.info("AnalyticsRepository jsonObject {} ",configMap);
			Integer workOrderId = (Integer) configMap.get(WORKORDER_ID);
			Integer prevWorkorderId = (Integer) configMap.get(PREV_WORKORDER_ID);
			Integer quickWorkorderId = (Integer) configMap.get(QUICK_WORKORDER_ID);
			String geographyConfig = analyticrepositoryObj.getGeographyConfig()!=null?analyticrepositoryObj.getGeographyConfig().replaceAll("\'", "\""):null;
			GeographyParser geographyParser = new Gson().fromJson(geographyConfig, GeographyParser.class);

			Geography geography = ReportUtil.getGeographyObject(geographyParser);
			List<Integer> workOrderIds = configMap.get(WORKORDER_IDS)!=null?(ArrayList<Integer>)configMap.get(WORKORDER_IDS):null;
			List<Integer> prevWorkorderIds = configMap.get(PREV_WORKORDER_IDS)!=null?(ArrayList<Integer>)configMap.get(PREV_WORKORDER_IDS):null;
			logger.info("Going to invoke report generation method for prev workOrderId {} , prevWorkorderId {} ,workOrderIds {},prevWorkorderIds {}",workOrderId,prevWorkorderId,workOrderIds,prevWorkorderIds);
			jsonMap.put(WORKORDER_ID, workOrderId);
			jsonMap.put(PREV_WORKORDER_ID, prevWorkorderId);
			jsonMap.put(ForesightConstants.ANALYTICAL_REPORT_KEY, analyticsrepoId);
			jsonMap.put(WORKORDER_IDS, workOrderIds);
			jsonMap.put(QUICK_WORKORDER_ID, quickWorkorderId);
			if(geography!=null){
				jsonMap.put(CITY, geography.getName());
				jsonMap.put(GEOGRAPHY_TYPE, geography.getGeographyType());
				jsonMap.put(GEOGRAPHY_NAME,geography.getName());
				jsonMap.put(GEOGRAPHY_ID, geography.getId());
			}
			jsonMap.put(PREV_WORKORDER_IDS, prevWorkorderIds);
			jsonMap.put(NAME, analyticrepositoryObj.getName());
		} catch (Exception e) {
			logger.error("Exception inside method getJsonDataMap {} ",Utils.getStackTrace(e));
		}
		return jsonMap;
	}

	public List<String> getGegraphyNameList(String geoType, List<Integer> geoIds) {
		List<String> geoNamesList = new ArrayList<>();
		logger.info("Inside getGegraphyNameList {}  {}", geoType, geoIds);
		if (geoType != null && geoIds != null) {
			if (geoType.equalsIgnoreCase(GEOGRAPHYL1)) {
				List<GeographyL1> l1List = iGeographyL1Dao.getGeographyL1ListByIds(geoIds);
				if (l1List != null && !l1List.isEmpty()) {
					geoNamesList = l1List.stream().filter(Objects::nonNull).map(GeographyL1::getName)
							.collect(Collectors.toList());
				}
			}
			if (geoType.equalsIgnoreCase(GEOGRAPHYL2)) {
				List<GeographyL2> l2List = iGeographyL2Dao.getGeographyL2ListByIds(geoIds);
				if (l2List != null && !l2List.isEmpty()) {
					geoNamesList = l2List.stream().filter(Objects::nonNull).map(GeographyL2::getName)
							.collect(Collectors.toList());
				}
			}
			if (geoType.equalsIgnoreCase(GEOGRAPHYL3)) {
				List<GeographyL3> l3List = iGeographyL3Dao.getGeographyL3ListByIds(geoIds);
				if (l3List != null && !l3List.isEmpty()) {
					geoNamesList = l3List.stream().filter(Objects::nonNull).map(GeographyL3::getName)
							.collect(Collectors.toList());
				}
			}
			if (geoType.equalsIgnoreCase(GEOGRAPHYL4)) {
				List<GeographyL4> l4List = iGeographyL4Dao.getGeographyL4ListByIds(geoIds);
				if (l4List != null && !l4List.isEmpty()) {
					geoNamesList = l4List.stream().filter(Objects::nonNull).map(GeographyL4::getName)
							.collect(Collectors.toList());
				}
			}
		}
		return geoNamesList;
	}

	@Override
	@Transactional
	public Response saveFile(Integer analyticsrepoId,  String filePath, File file) {
		if (file != null) {
			filePath+=file.getName();
			if (nVReportHdfsDao.saveFileToHdfs(file, filePath)) {
				logger.info("File saved Successfully ");
				analyticsRepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId,filePath,HDFS,progress.Generated,null);
				logger.info("Status is updated finally ");
				logger.info("Going to delete file from Local if exist {} ",filePath);
        		deleteFileIfExist(filePath);
				return Response.ok(ForesightConstants.SUCCESS_JSON).build();
			} else {
				return Response.ok(ForesightConstants.FAILURE_JSON).build();
			}
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	@Override
	@Transactional
	public List<SiteInformationWrapper> getSiteDataByDataList(List<String[]> arlist) {
		Map<String, Double> viewportMap =null;
		try {
			viewportMap = getViewPortFromDataList(arlist);
			return siteDetailService.getMacroSiteDetailsForCellLevelForReport(viewportMap, null, null, null, false, true, true);
		} catch (Exception e) {
			logger.error("Exception inside method getSiteDataForReportByDataList for view Port {}, errMsg {}  ",viewportMap,Utils.getStackTrace(e));
			return new ArrayList<>();
		}
	}
	
	@Override
	@Transactional
	public List<SiteInformationWrapper> getSiteDataForReportByDataList(List<String[]> arlist, Map<String, Integer> kpiIndexMap) {
		Map<String, Double> viewportMap =null;
		try {
			viewportMap = getViewPortFromDataList(arlist, kpiIndexMap);
			return siteDetailService.getMacroSiteDetailsForCellLevelForReport(viewportMap, null, null, null, false, true, true);
		} catch (Exception e) {
			logger.error("Exception inside method getSiteDataForReportByDataList for view Port {}, errMsg {}  ",viewportMap,Utils.getStackTrace(e));
			return new ArrayList<>();
		}
	}

	private Map<String, Double> getViewPortFromDataList(List<String[]> arlist, Map<String, Integer> kpiIndexMap) {
		Corner	corner = ReportUtil.getminmaxLatlOnDriveList(arlist, kpiIndexMap.get(LATITUDE), kpiIndexMap.get(LONGITUDE));
		Map<String, Double> viewportMap = ReportUtil.getViewPortMap(corner);
		logger.info("ViewPortMap for Site data: {}", viewportMap != null ? new Gson().toJson(viewportMap) : "View port is null");
		return viewportMap;
	}
	
	private Map<String, Double> getViewPortFromDataList(List<String[]> arlist) {
		Corner	corner = ReportUtil.getminmaxLatlOnDriveList(arlist, DriveHeaderConstants.INDEX_LAT,DriveHeaderConstants.INDEX_LON);
		Map<String, Double> viewportMap = ReportUtil.getViewPortMap(corner);
		logger.info("ViewPortMap for Site data: {}", viewportMap != null ? new Gson().toJson(viewportMap) : "View port is null");
		return viewportMap;
	}

	@Override
	public List<KPIWrapper> getKPiStatsDataList(Integer workorderId,Map<String, List<String>> map,Map<String, Integer> kpiIndexMap, String legendAppliedto) {
		try {
			List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(legendAppliedto);
		return  ReportUtil.convertLegendsListToKpiWrapperList(legendList,kpiIndexMap);
		} catch (Exception e) {
			logger.error("Exception occured inside method getKPiStatsDataList for workorderId {} , {} ",workorderId,Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	@Override
	public List<YoutubeTestWrapper> getYouTubeData(Integer workorderId,
			Map<String, List<String>> map) {
		List<String> listRecipe = map.get(RECIPE);
		List<String> listOperator = map.get(OPERATOR);
		String youTubeData = null;
		List<YoutubeTestWrapper> listYoutubeData = new ArrayList<>();
		logger.info("getYouTubeDataRecipeWise Recipe List Details : Recipes {}, operators {} ",listRecipe.toString(),listOperator.toString());
		int index=0;
		for (String recipeId : listRecipe) {
			try {
				youTubeData = nvLayer3DashboardService.getYoutubeDataFromLayer3Report(workorderId, listOperator.get(index),recipeId);
				logger.info("youTubeData {} , for operator {} , recipe {} ",youTubeData,recipeId,listOperator.get(index));
				if (youTubeData != null && !youTubeData.isEmpty()) {
					listYoutubeData.addAll(ReportUtil.getYouTubeTestDataWrapper(youTubeData));
				}
			} catch (BusinessException e) {
				logger.error("Exception in getYouTubeDataRecipeWise : {}", Utils.getStackTrace(e));
			}
		//	logger.info("List of listYoutubeData  {} ",listYoutubeData);
		}
		logger.info("listYoutubeData Data Size for workOrderId {} , is  {} , data {} ",workorderId,listYoutubeData.size(),listYoutubeData);
		return listYoutubeData;
	}

	@Override
	public List<YoutubeTestWrapper> getYouTubeDataByRecipeWise(Integer workorderId,String recipe,String operator) {
		logger.info("Inside method getYouTubeDataByRecipeWise for workorderId {} ,Recipe {} , operator {} , ",workorderId,recipe,operator);
		String youTubeData = null;
		List<YoutubeTestWrapper> listYoutubeData = new ArrayList<>();
		try {
			youTubeData = nvLayer3DashboardService.getYoutubeDataFromLayer3Report(workorderId, operator,recipe);
			logger.info("youTubeData {} , recipe {} , for operator {}  ",youTubeData,recipe,operator);
			if (youTubeData != null && !youTubeData.isEmpty()) {
				listYoutubeData.addAll(ReportUtil.getYouTubeTestDataWrapper(youTubeData));
			}
		} catch (BusinessException e) {
			logger.error("Exception inside method getYouTubeDataByRecipeWise : {}", Utils.getStackTrace(e));
		}
		//logger.info("listYoutubeData Data Size for workOrderId  {} , recipeId  is  {} , data {} ",workorderId,recipe,listYoutubeData);
		return listYoutubeData;
	}

	@Override
	public List<QuickTestWrapper> getQuickTestDataRecipeWise(Integer workorderId,String recipe,String operator) {
		logger.info("Inside method getQuickTestDataRecipeWise for workorderId {} ,Recipe {} , operator {} , ",workorderId,recipe,operator);
		String quickTestData = null;
		List<QuickTestWrapper> listSpeedTest = new ArrayList<>();
		try {
			quickTestData = nvLayer3DashboardService.getQuickTestDataFromLayer3Report(workorderId, operator,recipe);
			if (quickTestData != null && !quickTestData.isEmpty()) {
				listSpeedTest.addAll(ReportUtil.getQuickTestDataWrapper(quickTestData));
			}
	//		logger.info("quickTestData : " + quickTestData);
		} catch (BusinessException e) {
			logger.error("Exception in getQuickTestDataRecipeWise : {}", Utils.getStackTrace(e));
		}
		logger.info("listSpeedTest Data Size for workOrderId  {} , recipeId  is  {} , data {} ",workorderId,recipe,listSpeedTest);
		return listSpeedTest;
	}

	@Override
	public List<YoutubeTestWrapper> getYouTubeDataByRecipeForListOfOperators(Integer workorderId, String recipe,
			List<String> operatorList) {
		logger.info("Inside method getYouTubeDataByRecipeWise for workorderId {} ,Recipe {} , operatorList {} , ",
				workorderId, recipe, operatorList);
		String youTubeData = null;
		List<YoutubeTestWrapper> listYoutubeData = new ArrayList<>();
		operatorList = operatorList.stream().distinct().collect(Collectors.toList());
		for (String operator : operatorList) {
			try {
				youTubeData = nvLayer3DashboardService.getYoutubeDataFromLayer3Report(workorderId, operator, recipe);
				logger.info("youTubeData {} , recipe {} , for operator {}  ", youTubeData, recipe, operator);
				if (youTubeData != null && !youTubeData.isEmpty()) {
					listYoutubeData.addAll(ReportUtil.getYouTubeTestDataWrapper(youTubeData));
				}
			} catch (BusinessException e) {
				logger.error("Exception inside method getYouTubeDataByRecipeWise : {}", Utils.getStackTrace(e));
			}
		}
	//	logger.debug("listYoutubeData Data Size for workOrderId  {} , recipeId  is  {} , data {} ", workorderId, recipe,
	//			listYoutubeData);
		return listYoutubeData;
	}

	@Override
	public List<QuickTestWrapper> getQuickTestDataRecipeForListOfOperators(Integer workorderId, String recipe,
			List<String> operatorList) {
		logger.info("Inside method getQuickTestDataRecipeWise for workorderId {} ,Recipe {} , operatorList {} , ",
				workorderId, recipe, operatorList);
		String quickTestData = null;
		List<QuickTestWrapper> listSpeedTest = new ArrayList<>();
		operatorList = operatorList.stream().distinct().collect(Collectors.toList());
		for (String operator : operatorList) {
			try {
				quickTestData = nvLayer3DashboardService.getQuickTestDataFromLayer3Report(workorderId, operator,
						recipe);
				if (quickTestData != null && !quickTestData.isEmpty()) {
					listSpeedTest.addAll(ReportUtil.getQuickTestDataWrapper(quickTestData));
				}
		//		logger.info("quickTestData : " + quickTestData);
			} catch (BusinessException e) {
				logger.error("Exception in getQuickTestDataRecipeWise : {}", Utils.getStackTrace(e));
			}
		}
		logger.debug("listSpeedTest Data Size for workOrderId  {} , recipeId  is  {} , data {} ", workorderId, recipe,
				listSpeedTest);
		return listSpeedTest;
	}

	@Override
	public Map<String, List<String>> getRecipeOperatorMap(String category, RecipeMappingWrapper wrapper) {
		Map<String, List<String>> recipeOperatorMap =new HashMap<>();
		if(wrapper!=null){
			recipeOperatorMap.put(RECIPE, wrapper.getRecpiList());
			recipeOperatorMap.put(OPERATOR, wrapper.getOperatorList());
			return recipeOperatorMap;
		}
		return recipeOperatorMap;
	}

	@Override
	public List<KPIWrapper> modifyIndexOfCustomKpisForReport(List<KPIWrapper> kpiList, Map<String, Integer> kpiIndexMap) {
		for (KPIWrapper kpiwrapper : kpiList) {
			if(kpiwrapper.getKpiName().equalsIgnoreCase(FTP_DL_THROUGHPUT)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(FTP_DL_THROUGHPUT));
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(FTP_UL_THROUGHPUT)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(FTP_UL_THROUGHPUT));
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(HTTP_DL_THROUGHPUT)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(HTTP_DL_THROUGHPUT));
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(HTTP_UL_THROUGHPUT)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(HTTP_UL_THROUGHPUT));
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(FTP_DL_RSRP)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(FTP_DL_RSRP));
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(FTP_UL_RSRP)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(FTP_UL_RSRP));
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(HTTP_DL_RSRP)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(HTTP_DL_RSRP));
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(HTTP_UL_RSRP)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(HTTP_UL_RSRP));
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(FTP_DL_SINR)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(FTP_DL_SINR));
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(FTP_UL_SINR)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(FTP_UL_SINR));
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(HTTP_DL_SINR)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(HTTP_DL_SINR));
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(HTTP_UL_SINR)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(HTTP_UL_SINR));
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(CALL_PLOT)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(CALL_PLOT));
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(HANDOVER_PLOT)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(HANDOVER_PLOT));
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(IDLE_PLOT)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(IDLE_PLOT));
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(SERVING_SYSTEM)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(SERVING_SYSTEM));
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(DriveHeaderConstants.TECHNOLOGY)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(DriveHeaderConstants.TECHNOLOGY));
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(DL_BANWIDTH)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(DL_BANWIDTH));
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(RESELECTION_PLOT)){
				kpiwrapper.setIndexKPI(kpiIndexMap.get(RESELECTION_PLOT));
			}
		}
		return kpiList;
	}
	
	@Override
	public List<KPIWrapper> modifyIndexOfCustomKpis(List<KPIWrapper> kpiList) {
		for (KPIWrapper kpiwrapper : kpiList) {
			if(kpiwrapper.getKpiName().equalsIgnoreCase(FTP_DL_THROUGHPUT)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.FTP_DL_IMAGE_INDEX);
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(FTP_UL_THROUGHPUT)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.FTP_UL_IMAGE_INDEX);
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(HTTP_DL_THROUGHPUT)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.HTTP_DL_IMAGE_INDEX);
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(HTTP_UL_THROUGHPUT)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.HTTP_UL_IMAGE_INDEX);
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(FTP_DL_RSRP)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.FTP_DL_RSRP_IMAGE_INDEX);
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(FTP_UL_RSRP)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.FTP_UL_RSRP_IMAGE_INDEX);
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(HTTP_DL_RSRP)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.HTTP_DL_RSRP_IMAGE_INDEX);
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(HTTP_UL_RSRP)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.HTTP_UL_RSRP_IMAGE_INDEX);
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(FTP_DL_SINR)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.FTP_DL_SINR_IMAGE_INDEX);
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(FTP_UL_SINR)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.FTP_UL_SINR_IMAGE_INDEX);
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(HTTP_DL_SINR)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.HTTP_DL_SINR_IMAGE_INDEX);
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(HTTP_UL_SINR)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.HTTP_UL_SINR_IMAGE_INDEX);
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(CALL_PLOT)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.CALL_PLOT_INDEX);
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(HANDOVER_PLOT)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.HANDOVER_PLOT_INDEX);
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(IDLE_PLOT)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.IDLE_PLOT_INDEX);
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(SERVING_SYSTEM)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.SERVING_SYSTEM_INDEX);
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(TECHNOLOGY)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.TECHNOLOGY_INDEX);
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(DriveHeaderConstants.BANDWIDTH_DL)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.BANDWIDTH_DL_INDEX);
			}else if(kpiwrapper.getKpiName().equalsIgnoreCase(RESELECTION_PLOT)){
				kpiwrapper.setIndexKPI(DriveHeaderConstants.RESELECTION_PLOT_INDEX);
			}
		}
		return kpiList;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public Response saveFileAndUpdateStatus(Integer analyticsrepoId,  String filePath,
			GenericWorkorder genericWorkorder, File file,String downLoadFileName,String reportInstanceKey) {
		if (file != null) {
			filePath+=file.getName();
			if (nVReportHdfsDao.saveFileToHdfs(file, filePath)) {
				logger.info("File saved Successfully ");
				analyticsRepositoryDao.updateStatusInAnalyticsRepository(analyticsrepoId,filePath,HDFS,progress.Generated,downLoadFileName);
				logger.info("Status in analytics repository is updated ");
				if(genericWorkorder!=null) {
					Map<String, String> geoMap = genericWorkorder.getGwoMeta();
					geoMap.put(reportInstanceKey, analyticsrepoId.toString());
					genericWorkorder.setGwoMeta(geoMap);
					try {
						iGenericWorkorderDao.update(genericWorkorder);
					} catch (DaoException e) {
						logger.error("Error inside the method{}",Utils.getStackTrace(e));
					}
				}
				logger.info("Going to delete file from Local if exist {} ",file.getAbsolutePath());
				deleteFileIfExist(file.getAbsolutePath());
				return Response.ok(ForesightConstants.SUCCESS_JSON).build();
			} else {
				return Response.ok(ForesightConstants.FAILURE_JSON).build();
			}
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}

	@Override
	public boolean deleteFileIfExist(String absoluteFilePath){
		try {
			Path filePath = Paths.get(absoluteFilePath);
			File file  = new File(absoluteFilePath);
			if(file.exists()){
				Files.delete(filePath);
				return true;
			}
		} catch (Exception e) {
			logger.error("Unable to Delete the file from filePath {} , issue is {} ",absoluteFilePath,e.getMessage());
		}
		return false;
	}

	public List<KPIWrapper> getKPiStatsDataList(Integer workorderId,List<String> recipeList,List<String> operatorList,Map<String, Integer> kpiIndexMap,String legendAppliedto) {
		try {
			List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(legendAppliedto);
			return  ReportUtil.convertLegendsListToKpiWrapperList(legendList,kpiIndexMap);
		} catch (Exception e) {
			logger.error("Exception in finding the getKPiStatsDataList for workOrder Id {} ,{} ",workorderId,Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	@Override
	public File getRecipeWiseKpiFile(String reportType,Integer workOrderId,String recipeId,String operator, SSVTReportWrapper mainRecipeWrapper,String appliedto)
			throws IOException, IllegalAccessException, NoSuchFieldException {
		logger.info("Inside method getRecipeWiseKpiFile for reportType {},  woId {} ,recipeId {} ,operator {}",reportType,workOrderId,recipeId,operator);
		Set<String> dynamicKpis = getDynamicKpiName(Arrays.asList(workOrderId), recipeId, Layer3PPEConstant.ADVANCE);
		List<String> fetchKPIList = ReportIndexWrapper.getLiveDriveKPIs().stream().filter(k -> dynamicKpis.contains(k)).collect(Collectors.toList());
		Map<String, Integer> kpiIndexMap = ReportIndexWrapper.getLiveDriveKPIIndexMap(fetchKPIList);
		List<String> fetchSummaryKPIList = ReportSummaryIndexWrapper.getLiveDriveKPIs();
		Map<String, Integer> summaryKpiIndexMap = ReportSummaryIndexWrapper
				.getLiveDriveKPIIndexMap(fetchSummaryKPIList);
		List<String[]> arlist = getDriveDataRecipeWiseTaggedForReport(Arrays.asList(workOrderId),
				Arrays.asList(recipeId), fetchKPIList, kpiIndexMap);
		String[] summaryData = getSummaryDataForReport(workOrderId, Arrays.asList(recipeId),
				fetchSummaryKPIList);
		List<KPIWrapper> kpiList  = getKPiStatsDataList(workOrderId, Arrays.asList(recipeId), Arrays.asList(operator), kpiIndexMap, appliedto);
		kpiList = LegendUtil.populateLegendData(kpiList, arlist, kpiIndexMap.get(ReportConstants.TEST_TYPE));
		DriveImageWrapper driveImageWrapper = new DriveImageWrapper(arlist, kpiIndexMap.get(ReportConstants.LATITUDE),
				kpiIndexMap.get(ReportConstants.LONGITUDE), kpiIndexMap.get(ReportConstants.PCI_PLOT), kpiList, getSiteInfoByReportType(workOrderId,arlist,kpiIndexMap), null);

		SSVTReportSubWrapper subWrapper = populateGraphData(kpiList, arlist,kpiIndexMap);
		
		subWrapper.setEarfcnList(setDataForEarfcnGraph(arlist, kpiIndexMap));
		
		setCallData(summaryData, subWrapper, summaryKpiIndexMap);

    	List<DriveDataWrapper> imageDataList = new ArrayList<>();
		List<SSVTReportSubWrapper> subWrapperList = new ArrayList<>();
		subWrapperList.add(subWrapper);
		mainRecipeWrapper.setSubWrapperList(subWrapperList);
		HashMap<String, String> imageMap = getImageMapForReport(kpiList, driveImageWrapper, imageDataList, operator,reportType,kpiIndexMap);
		HashMap<String, Object> finalMap = prepareImageMap(imageMap, kpiIndexMap);
		File file = getFileForEachRecipe(recipeId, mainRecipeWrapper,finalMap);
		logger.info("Going to return the file with absolute Path {} ",file!=null?file.getAbsolutePath():null);
		return file;

	}

	private void setCallData(String[] summary, SSVTReportSubWrapper subWrapper, Map<String, Integer> summaryKpiIndexMap) {
		try {
			List<LiveDriveVoiceAndSmsWrapper> callList = ReportUtil.callDataListForReport(summary, summaryKpiIndexMap);
			List<LiveDriveVoiceAndSmsWrapper> callDataList = ReportUtil.getCallPlotDataListForReport(summary,summaryKpiIndexMap);

			if (!callList.isEmpty()) {
				subWrapper.setCallDataList(callList);
			}

			subWrapper.setCallPlotDataList(callDataList);
		} catch (Exception e) {
			logger.error("Exception inside the method setCallData{}", Utils.getStackTrace(e));

		}
	}
	@Override
	public List<GraphDataWrapper> setDataForEarfcnGraph(List<String[]> arlist, Map<String, Integer> kpiIndexMap) {
		logger.info("inside the method  setDataForEarfcn");
		List<GraphDataWrapper> earfcnList = new ArrayList<>();
		try {
			Double cdf = 0.0;
			Integer indexEarfcn = kpiIndexMap.containsKey(ReportConstants.EARFCN) ? kpiIndexMap.get(ReportConstants.EARFCN) : DriveHeaderConstants.INDEX_DL_EARFCN;
		      List<Double> earfcnDataList= ReportUtil.convetArrayToList(arlist, indexEarfcn);
		     Map<Object, Long> earfcnCountMap=earfcnDataList.stream().collect(
		    	        Collectors.groupingBy(o -> o,Collectors.counting()));
			Integer totalCount = earfcnDataList.size();
			for (Entry<Object, Long> map : earfcnCountMap.entrySet()) {
				GraphDataWrapper graphData = new GraphDataWrapper();
				Double pdf = Utils.getPercentage(map.getValue().intValue(), totalCount);
				cdf += pdf;
				graphData.setPdfValue(pdf);
				graphData.setCdfValue(cdf);
				graphData.setCount(map.getValue().intValue());
				graphData.setKpiName(map.getKey().toString());
				earfcnList.add(graphData);
			}
			logger.info("earfcnList is " + earfcnList);
			return earfcnList;
		} catch (Exception e) {
			logger.error("Error inside the method setDataForEarfcn {}", Utils.getStackTrace(e));
		}
		return earfcnList;
	}

	public File getFileForEachRecipe(String recipeId, SSVTReportWrapper mainRecipeWrapper, HashMap<String, Object> imageMap) {
		logger.info("imageMap Data for recipeId {} is  {} ",recipeId,imageMap);
		try {
			String reportAssetRepo = ConfigUtils.getString(SSVT_RECIPE_REPORT_JASPER_FOLDER_PATH);
			String destinationFileName=ConfigUtils.getString(NV_REPORTS_PATH)+SSVT+FORWARD_SLASH;
			List<SSVTReportWrapper> dataSourceList = new ArrayList<>();
			dataSourceList.add(mainRecipeWrapper);
			JRBeanCollectionDataSource rfbeanColDataSource = new JRBeanCollectionDataSource(dataSourceList);
			setstaticimageToMap(reportAssetRepo, imageMap);
			destinationFileName += RECIPE_STR + recipeId + PDF_EXTENSION;
			logger.info("destinationFileName {}",destinationFileName);
			JasperRunManager.runReportToPdfFile(reportAssetRepo + MAIN_JASPER,
					destinationFileName,imageMap , rfbeanColDataSource);
			logger.info("Report Created successfully  ");
			return ReportUtil.getIfFileExists(destinationFileName);
		} catch (Exception e) {
			logger.error("@proceedToCreateSSVTReport getting err={}", Utils.getStackTrace(e));
		}
		logger.info(
				"@proceedToCreateSSVTReport going to return null as there has been some problem in generating report");
		return null;
	}

	

	private void setstaticimageToMap(String reportAssetRepo, Map<String, Object> finalMap) {
		finalMap.put(SUBREPORT_DIR, reportAssetRepo);
		finalMap.put(LOGO_CLIENT_KEY, reportAssetRepo + LOGO_CLIENT_IMG);
        finalMap.put(LOGO_NV_KEY, reportAssetRepo + LOGO_NV_IMG);
		finalMap.put(ConfigUtils.getString(ReportConstants.NV_REPORT_LOGO_CLIENT_KEY), reportAssetRepo + LOGO_CLIENT_IMG);
	}

	private HashMap<String, String> getImageMapForReport(List<KPIWrapper> kpiList, DriveImageWrapper driveImageWrapper,
			List<DriveDataWrapper> imageDataList, String operator, String reportType, Map<String, Integer> kpiIndexMap) throws IOException{
		try {
			List<Double[]> pinLatLonList = null;
			if (imageDataList != null && imageDataList.size()>INDEX_ZER0) {
				pinLatLonList = imageDataList	.stream()
						.map(driveDataWrapper -> new Double[] { driveDataWrapper.getLongitude(),
								driveDataWrapper.getLatitutde() })
						.collect(Collectors.toList());
			}
//			kpiList = modifyIndexOfCustomKpis(kpiList);
			HashMap<String, BufferedImage> bufferdImageMap = mapImageService.getDriveImagesForReport(driveImageWrapper,pinLatLonList,kpiIndexMap);
			bufferdImageMap.putAll(getLegendImagesForReport(kpiList, driveImageWrapper.getDataKPIs(), kpiIndexMap));
			String imagePath = ConfigUtils.getString(IMAGE_PATH_FOR_NV_REPORT) + reportType
					+ FORWARD_SLASH
					+ ReportUtil.getFormattedDate(new Date(), DATE_FORMAT_DD_MM_YY_HH_SS)
					+ FORWARD_SLASH+((operator!=null)?operator.replace(SPACE, BLANK_STRING):BLANK_STRING);
			logger.info("finally Image Map of method getImageMapForReport is {}",imagePath);
			return mapImageService.saveDriveImages(bufferdImageMap, imagePath, false);
		} catch (Exception e) {
			logger.error("Exception inside the method getImageMapForReport {}",Utils.getStackTrace(e));
		}
		return new HashMap<>();

	}

	private SSVTReportSubWrapper populateGraphData(List<KPIWrapper> kpiList, List<String[]> arlist,
			Map<String, Integer> kpiIndexMap) {
		SSVTReportSubWrapper subWrapper = new SSVTReportSubWrapper();
		for (KPIWrapper kpiWrapper : kpiList) {
			populateGraphDataForkpi(subWrapper, kpiWrapper, arlist, kpiIndexMap);
		}
		return subWrapper;
	}

	private SSVTReportSubWrapper populateGraphDataForkpi(SSVTReportSubWrapper subWrapper, KPIWrapper kpiWrapper,
			List<String[]> arlist, Map<String, Integer> kpiIndexMap) {
		logger.info("inside the method populateGraphDataForkpi");
		try {
			if (RSRP.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setRsrpList(ReportUtil.setGraphDataForKpi(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));

			} else if (SINR.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setSinrList(ReportUtil.setGraphDataForKpi(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));

			} else if (DL.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setDlList(ReportUtil.setGraphDataForKpi(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));

			} else if (UL.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setUlList(ReportUtil.setGraphDataForKpi(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));

			}
			
			else if (FTP_DL_RSRP.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setFtpRsrpDlList(
						ReportUtil.setGraphDataForKpi(kpiWrapper, ReportUtil.convetArrayToListForCustomKpi(arlist,
								kpiIndexMap.get(ReportConstants.RSRP), FTP_DOWNLOAD, kpiIndexMap.get(ReportConstants.TEST_TYPE))));

			} else if (HTTP_DL_RSRP.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setHttpRsrpDlList(
						ReportUtil.setGraphDataForKpi(kpiWrapper, ReportUtil.convetArrayToListForCustomKpi(arlist,
								kpiIndexMap.get(ReportConstants.RSRP), HTTP_DOWNLOAD, kpiIndexMap.get(ReportConstants.TEST_TYPE))));

			} else if (FTP_DL_SINR.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setFtpSinrDlList(
						ReportUtil.setGraphDataForKpi(kpiWrapper, ReportUtil.convetArrayToListForCustomKpi(arlist,
								kpiIndexMap.get(ReportConstants.SINR), FTP_DOWNLOAD, kpiIndexMap.get(ReportConstants.TEST_TYPE))));

			} else if (HTTP_DL_SINR.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setHttpSinrDlList(
						ReportUtil.setGraphDataForKpi(kpiWrapper, ReportUtil.convetArrayToListForCustomKpi(arlist,
								kpiIndexMap.get(ReportConstants.SINR), HTTP_DOWNLOAD, kpiIndexMap.get(ReportConstants.TEST_TYPE))));

			}

			else if (FTP_UL_RSRP.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setFtpRsrpUlList(
						ReportUtil.setGraphDataForKpi(kpiWrapper, ReportUtil.convetArrayToListForCustomKpi(arlist,
								kpiIndexMap.get(ReportConstants.RSRP), FTP_UPLOAD, kpiIndexMap.get(ReportConstants.TEST_TYPE))));

			} else if (HTTP_UL_RSRP.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setHttpRsrpUlList(
						ReportUtil.setGraphDataForKpi(kpiWrapper, ReportUtil.convetArrayToListForCustomKpi(arlist,
								kpiIndexMap.get(ReportConstants.RSRP), HTTP_UPLOAD, kpiIndexMap.get(ReportConstants.TEST_TYPE))));

			} else if (FTP_UL_SINR.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setFtpSinrUlList(
						ReportUtil.setGraphDataForKpi(kpiWrapper, ReportUtil.convetArrayToListForCustomKpi(arlist,
								kpiIndexMap.get(ReportConstants.SINR), FTP_UPLOAD, kpiIndexMap.get(ReportConstants.TEST_TYPE))));

			} else if (HTTP_UL_SINR.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setHttpSinrUlList(
						ReportUtil.setGraphDataForKpi(kpiWrapper, ReportUtil.convetArrayToListForCustomKpi(arlist,
								kpiIndexMap.get(ReportConstants.SINR), HTTP_UPLOAD, kpiIndexMap.get(ReportConstants.TEST_TYPE))));

			}

		} catch (Exception e) {
			logger.error("Exception inside populateGraphDataForkpi {}", Utils.getStackTrace(e));
		}
		return subWrapper;
	}

	@SuppressWarnings("unchecked")
	private List<SiteInformationWrapper> getSiteInfoByReportType(Integer workOrderId, List<String[]> arlist,
			Map<String, Integer> kpiIndexMap) {
		GenericWorkorder genericWorkorder = null;
		try {
			genericWorkorder = iGenericWorkorderDao.findByPk(workOrderId);
		} catch (DaoException e) {
			logger.error("Error inside the method getSiteInfoByReportType{}",e.getMessage());
		}
		if(genericWorkorder!=null) {
			Map<String,Object> siteDataMap = getSiteDataForReport(genericWorkorder,null,null,arlist, false, false, kpiIndexMap);
			return  ReportUtil.filterNEDataByBand(ReportUtil.getSiteFromMap(siteDataMap), ReportUtil.findBandDetailByGWOMetaData(genericWorkorder));

			//return siteDataMap.get(SITE_INFO_LIST)!=null?(List<SiteInformationWrapper>) siteDataMap.get(SITE_INFO_LIST):null;
		}
		return Collections.emptyList();
	}

	@Override
	@Transactional(readOnly  = true)
	public Map<String, Object> getSiteDataForReport(GenericWorkorder genericWorkorder, Long startTimeStamp,
			Long endTimeStamp, List<String[]> csvDataArray, boolean needOnlyOnAirSites, boolean needOnlyMacroCells) {
		Map<String, Object> siteDataMap = new HashMap<>();

		List<SiteInformationWrapper> sitInfoWrapperList = new ArrayList<>();
		try {
			Map<String, String> geoMetaDataMap = genericWorkorder.getGwoMeta();
			logger.info("geoMetaDataMap is {}", geoMetaDataMap);
			String neName = getSiteNameFromWOMetaData(geoMetaDataMap);

			sitInfoWrapperList = siteDetailService.getMacroSiteDetailsForCellLevelForReport(null, null,
					Arrays.asList(neName), null, false, needOnlyMacroCells, needOnlyOnAirSites);

			if (TemplateType.NV_SSVT_QUICK.name().equalsIgnoreCase(genericWorkorder.getTemplateType().toString())
					|| TemplateType.NV_SSVT_FULL.name()
							.equalsIgnoreCase(genericWorkorder.getTemplateType().toString())) {
				logger.error("inside the automation tempate type condition {}",genericWorkorder.getTemplateType().toString());
				sitInfoWrapperList = filterFrequencyWiseCellData(sitInfoWrapperList,
						geoMetaDataMap.get(NVWorkorderConstant.BAND));
			} 
			else {
				List<Integer> pciList = getPciListFromWoMetaData(geoMetaDataMap);

				sitInfoWrapperList = convetMacroSiteDetailToSiteInfo(sitInfoWrapperList, pciList);
			}
			// List<NEHaveAlarm> listOfNeAlarms =
			// getSiteInfoListWithAlarmStatus(macroSiteDetailList,startTimeStamp,endTimeStamp);

			if (startTimeStamp != null && endTimeStamp != null) {
				List<AlarmDataWrapper> listOfNeAlarms = getSiteInfoListWithAlarmStatus(sitInfoWrapperList,
						startTimeStamp, endTimeStamp);
				// logger.info("listOfNeAlarms Data {} ", new Gson().toJson(listOfNeAlarms));
				List<String> listOfNeIdsHaveAlarm = listOfNeAlarms != null
						? listOfNeAlarms.stream().map(AlarmDataWrapper::getNeId).collect(Collectors.toList())
						: null;
				// logger.info("listOfNeIdsHaveAlarm {} ", listOfNeIdsHaveAlarm);
				sitInfoWrapperList = taggedAlarmStatusInSite(sitInfoWrapperList, listOfNeIdsHaveAlarm);
				siteDataMap.put(NE_ALARM_DATA, listOfNeAlarms);
			}
			// getNeighburSiteDataList(sitInfoWrapperList);
			if (csvDataArray != null) {
				Map<String, Double> viewportMap = getViewPortFromDataList(csvDataArray);
				List<SiteInformationWrapper> driveViewPortSiteDataList = siteDetailService
						.getMacroSiteDetailsForCellLevelForReport(viewportMap, null, null, null, true,
								needOnlyMacroCells, needOnlyOnAirSites);
				if (driveViewPortSiteDataList != null) {
					logger.info("driveViewPortSiteDataList {}", driveViewPortSiteDataList.size());
					sitInfoWrapperList.addAll(driveViewPortSiteDataList);
				}
			}
			siteDataMap.put(SITE_INFO_LIST, sitInfoWrapperList);
			return siteDataMap;
		} catch (Exception e) {
			logger.error("Exception inside the Method getSiteDataForReprt{} ", Utils.getStackTrace(e));
		}
		return Collections.emptyMap();
	}

	private List<SiteInformationWrapper> filterFrequencyWiseCellData(List<SiteInformationWrapper> sitInfoWrapperList,
			String band) {

		if (band != null) {
			logger.error("insdie the method filterFrequencyWiseCellData {} ", band);
			List<SiteInformationWrapper> siteInfoList = new ArrayList<>();
			try {
				sitInfoWrapperList.forEach(siteDetail -> {
					if (band.equalsIgnoreCase(siteDetail.getNeFrequency())) {
						siteInfoList.add(siteDetail);
					}
				});
			} catch (Exception e) {
				logger.error("error inside the method filterFrequencyWiseCellData {}", Utils.getStackTrace(e));
			}
			logger.debug("Going to return the siteInfo List Of Size {} ", siteInfoList.size());
			return siteInfoList;
		} else {
			return sitInfoWrapperList;
		}

	}

	@Override
	@Transactional(readOnly  = true)
	public Map<String,Object> getSiteDataForReport(GenericWorkorder genericWorkorder,Long startTimeStamp,Long endTimeStamp,List<String[]> csvDataArray, boolean needOnlyOnAirSites, boolean needOnlyMacroCells, Map<String, Integer> kpiIndexMap) {
		Map<String,Object> siteDataMap = new HashMap<>();

		List<SiteInformationWrapper> sitInfoWrapperList = new ArrayList<>();
		try {
			Map<String, String> geoMetaDataMap = genericWorkorder.getGwoMeta();
			logger.info("geoMetaDataMap is {}", geoMetaDataMap);
			String neName = getSiteNameFromWOMetaData(geoMetaDataMap);
			List<Integer> pciList = getPciListFromWoMetaData(geoMetaDataMap);
			//		logger.info("pciList {}", pciList);

			sitInfoWrapperList = siteDetailService.getMacroSiteDetailsForCellLevelForReport(null, null,
					Arrays.asList(neName), null, false, needOnlyMacroCells, needOnlyOnAirSites);
			sitInfoWrapperList = convetMacroSiteDetailToSiteInfo(sitInfoWrapperList, pciList);

			//List<NEHaveAlarm> listOfNeAlarms = getSiteInfoListWithAlarmStatus(macroSiteDetailList,startTimeStamp,endTimeStamp);

			if (startTimeStamp != null && endTimeStamp != null) {
				List<AlarmDataWrapper> listOfNeAlarms = getSiteInfoListWithAlarmStatus(sitInfoWrapperList,
						startTimeStamp, endTimeStamp);
				//			logger.info("listOfNeAlarms Data {} ", new Gson().toJson(listOfNeAlarms));
				List<String> listOfNeIdsHaveAlarm = listOfNeAlarms != null
						? listOfNeAlarms.stream().map(AlarmDataWrapper::getNeId).collect(Collectors.toList())
						: null;
				//			logger.info("listOfNeIdsHaveAlarm {} ", listOfNeIdsHaveAlarm);
				sitInfoWrapperList = taggedAlarmStatusInSite(sitInfoWrapperList, listOfNeIdsHaveAlarm);
				siteDataMap.put(NE_ALARM_DATA, listOfNeAlarms);
			}
			//getNeighburSiteDataList(sitInfoWrapperList);
			if (csvDataArray != null) {
				Map<String, Double> viewportMap = getViewPortFromDataList(csvDataArray, kpiIndexMap);
				List<SiteInformationWrapper> driveViewPortSiteDataList = siteDetailService
						.getMacroSiteDetailsForCellLevelForReport(viewportMap, null, null, null, true, needOnlyMacroCells, needOnlyOnAirSites);
				if (driveViewPortSiteDataList != null) {
					logger.info("driveViewPortSiteDataList {}", driveViewPortSiteDataList.size());
					sitInfoWrapperList.addAll(driveViewPortSiteDataList);
				}
			}
			siteDataMap.put(SITE_INFO_LIST, sitInfoWrapperList);
			return siteDataMap;
		} catch (Exception e) {
			logger.error("Exception inside the Method getSiteDataForReprt{} ", Utils.getStackTrace(e));
		}
		return Collections.emptyMap();
	}

	private void getNeighburSiteDataList(List<SiteInformationWrapper> sitInfoWrapperList) {
		List<String> neNamewithCellIdList = sitInfoWrapperList	.stream()
																.map(wrapper -> (wrapper.getSiteName()
																/*
																 * + UNDERSCORE +
																 * wrapper.getCellId()
																 */))
																.collect(Collectors.toList());
	//	logger.info("neNamewithCellIdList Data {} ", neNamewithCellIdList);
		if (neNamewithCellIdList != null && neNamewithCellIdList.size() > INDEX_ZER0) {
			List<SiteInformationWrapper> neighbourSiteList = getFirstTierNieghbourSites(neNamewithCellIdList);
//			logger.info("neighbourSiteList  Size {} , Data {} ",
//					neighbourSiteList != null ? neighbourSiteList.size() : null,
//					new Gson().toJson(neighbourSiteList));
			if (neighbourSiteList != null)
				sitInfoWrapperList.addAll(neighbourSiteList);
		}
	}

	private List<AlarmDataWrapper> getSiteInfoListWithAlarmStatus(List<SiteInformationWrapper> siteDetailList,Long startTimeStamp,Long endTimeStamp) {
		try {
			return getListOfNEHavingAlarmDetails(siteDetailList, startTimeStamp, endTimeStamp);
		} catch (Exception e) {
			logger.error("Exception inside method getSiteInfoListWithAlarmStatus {} ",e.getMessage());
		}
		return null;
	}

	/**
	 * Gets the site name from WO meta data.
	 *
	 * @param geoMetaDataMap
	 *            the geo meta data map
	 * @return the site name from WO meta data
	 */
	private String getSiteNameFromWOMetaData(Map<String, String> geoMetaDataMap) {
		String neName = null;
		try {
			String siteDetailsJson = geoMetaDataMap.get(NVWorkorderConstant.SITE_INFO);
			logger.info("siteDetailsJson {} ", siteDetailsJson);
			JSONObject jsonObject = ReportUtil.convertStringToJsonObject(siteDetailsJson);
			logger.info("jsonObject {} ", jsonObject.toString());
			neName = (String) jsonObject.get("siteName");
		} catch (Exception e) {
			logger.error("Inside method getSiteNameFromWOMetaData {} ", Utils.getStackTrace(e));
		}
		return neName;
	}

	/**
	 * Gets the pci list from wo meta data.
	 *
	 * @param geoMetaDataMap
	 *            the geo meta data map
	 * @return the pci list from wo meta data
	 */
	@Override
	public List<Integer> getPciListFromWoMetaData(Map<String, String> geoMetaDataMap) {
		logger.info("inside the getPciListFromWoMetaData");
		try {
			List<Integer> list = new ArrayList<>();
			String json = geoMetaDataMap.get(NVWorkorderConstant.RECIPE_PCI_MAP);

			Map<String, Integer> map = mapper.readValue(json, new TypeReference<Map<String, Integer>>() {
			});
			for (Integer i : map.values()) {
				list.add(i);
			}
			return list;
		} catch (JsonParseException e) {
			logger.error("JsonParseException inside the method getPciListFromWoMetaData{} ", e.getMessage());
		} catch (JsonMappingException e) {
			logger.error("JsonMappingException inside the method getPciListFromWoMetaData{} ", e.getMessage());
		} catch (IOException e) {
			logger.error("IOException inside the method getPciListFromWoMetaData{} ", e.getMessage());
		}

		catch (Exception e) {
			logger.error("Exception inside the method getPciListFromWoMetaData{} ", e.getMessage());
		}
		return Collections.emptyList();
	}

	/**
	 * Convet micro site detail to site info.
	 *
	 * @param siteDetailList
	 *            the macro site detail
	 * @param pciList
	 * @return the siteInfo list site list is use for both jasper and image creation
	 */
	private List<SiteInformationWrapper> convetMacroSiteDetailToSiteInfo(List<SiteInformationWrapper> siteDetailList,
			List<Integer> pciList) {
		logger.info("insdie the method convetMicroSiteDetailToSiteInfo ");
		List<SiteInformationWrapper> siteInfoList = new ArrayList<>();
		try {
			siteDetailList.forEach(siteDetail-> {
				if (pciList.contains(siteDetail.getPci())) {
					siteInfoList.add(siteDetail);
				}
			});
		} catch (Exception e) {
			logger.error("error inside the method convetMicroSiteDetailToSiteInfo {}", Utils.getStackTrace(e));
		}
		logger.debug("Going to return the siteInfo List Of Size {} ",siteInfoList.size());
		return siteInfoList;
	}

	@Override
	public List<KPIWrapper> getListOfKpisWithRanges(Map<String, Integer> kpiIndexMap,String legendAppliedto) {
		try {
			List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(legendAppliedto);
			List<KPIWrapper> kpiList = ReportUtil.convertLegendsListToKpiWrapperList(legendList,kpiIndexMap);
			return modifyIndexOfCustomKpis(kpiList);
		} catch (Exception e) {
			logger.error("Exception occured inside method getListOfKpisWithRanges for legendAppliedto {}, {} ",legendAppliedto,Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	private HashMap<String, Object> prepareImageMap(HashMap<String, String> imagemap, Map<String, Integer> kpiIndexMap) {
		logger.info("inside the method prepareImageMap");
		Map<String, Object> map = new HashMap<>();
		try {
			map.put(IMAGE_RSRP, imagemap.get(kpiIndexMap.get(ReportConstants.RSRP) + ""));
			map.put(IMAGE_RSRP_LEGEND, imagemap
					.get(LEGEND + UNDERSCORE + kpiIndexMap.get(ReportConstants.RSRP)));

			map.put(IMAGE_SINR, imagemap.get(kpiIndexMap.get(ReportConstants.SINR) + ""));
			map.put(IMAGE_SINR_LEGEND, imagemap
					.get(LEGEND + UNDERSCORE + kpiIndexMap.get(ReportConstants.SINR)));
			map.put(IMAGE_DL, imagemap.get(kpiIndexMap.get(ReportConstants.DL_THROUGHPUT) + ""));
			map.put(IMAGE_DL_LEGEND,
					imagemap.get(LEGEND + UNDERSCORE + kpiIndexMap.get(ReportConstants.DL_THROUGHPUT)));

			map.put(IMAGE_UL, imagemap.get(kpiIndexMap.get(ReportConstants.UL_THROUGHPUT) + ""));
			map.put(IMAGE_UL_LEGEND,
					imagemap.get(LEGEND + UNDERSCORE + kpiIndexMap.get(ReportConstants.UL_THROUGHPUT) + ""));
			map.put(IMAGE_PCI, imagemap.get(kpiIndexMap.get(ReportConstants.PCI_PLOT) + ""));
			map.put(IMAGE_SITE, imagemap.get(SITE_IMAGE));
			map.put(IMAGE_PCI_LEGEND, imagemap.get(PCI_LEGEND));
			map.put(JUSTFICATION_IMG, imagemap.get(SATELLITE_VIEW3));
			///HTTP and FTTP
			map.put(IMAGE_SINR_HTTP_DL, imagemap.get(kpiIndexMap.get(ReportConstants.HTTP_DL_SINR) + ""));
			map.put(IMAGE_SINR_HTTP_DL_LEGEND, imagemap.get(LEGEND + UNDERSCORE+kpiIndexMap.get(ReportConstants.HTTP_DL_SINR)));
			map.put(IMAGE_SINR_HTTP_UL, imagemap.get(kpiIndexMap.get(ReportConstants.HTTP_UL_SINR) + ""));
			map.put(IMAGE_SINR_HTTP_UL_LEGEND, imagemap.get(LEGEND + UNDERSCORE+kpiIndexMap.get(ReportConstants.HTTP_UL_SINR)));

			map.put(IMAGE_SINR_FTP_DL, imagemap.get(kpiIndexMap.get(ReportConstants.FTP_DL_SINR) + ""));
			map.put(IMAGE_SINR_FTP_DL_LEGEND, imagemap.get(LEGEND + UNDERSCORE+kpiIndexMap.get(ReportConstants.FTP_DL_SINR)));
			map.put(IMAGE_SINR_FTP_UL, imagemap.get(kpiIndexMap.get(ReportConstants.FTP_UL_SINR) + ""));
			map.put(IMAGE_SINR_FTP_UL_LEGEND, imagemap.get(LEGEND + UNDERSCORE+kpiIndexMap.get(ReportConstants.FTP_UL_SINR)));

			map.put(IMAGE_RSRP_HTTP_DL, imagemap.get(kpiIndexMap.get(ReportConstants.HTTP_DL_RSRP) + ""));
			map.put(IMAGE_RSRP_HTTP_DL_LEGEND, imagemap.get(LEGEND + UNDERSCORE+kpiIndexMap.get(ReportConstants.HTTP_DL_RSRP)));
			map.put(IMAGE_RSRP_HTTP_UL, imagemap.get(kpiIndexMap.get(ReportConstants.HTTP_UL_RSRP) + ""));
			map.put(IMAGE_RSRP_HTTP_UL_LEGEND, imagemap.get(LEGEND + UNDERSCORE+kpiIndexMap.get(ReportConstants.HTTP_UL_RSRP)));

			map.put(IMAGE_RSRP_FTP_DL, imagemap.get(kpiIndexMap.get(ReportConstants.FTP_DL_RSRP) + ""));
			map.put(IMAGE_RSRP_FTP_DL_LEGEND, imagemap.get(LEGEND + UNDERSCORE+kpiIndexMap.get(ReportConstants.FTP_DL_RSRP)));
			map.put(IMAGE_RSRP_FTP_UL, imagemap.get(kpiIndexMap.get(ReportConstants.FTP_UL_RSRP) + ""));
			map.put(IMAGE_RSRP_FTP_UL_LEGEND, imagemap.get(LEGEND + UNDERSCORE+kpiIndexMap.get(ReportConstants.FTP_UL_RSRP)));
			map.put(IMAGE_FC, imagemap.get(kpiIndexMap.get(ReportConstants.DL_EARFCN) + ""));
			map.put(IMAGE_FC_LEGEND, imagemap.get(DL_EARFCN));
			map.put(IMAGE_CGI, imagemap.get(kpiIndexMap.get(DriveHeaderConstants.CGI) + ""));
			map.put(IMAGE_CGI_LEGEND, imagemap.get(DriveHeaderConstants.CGI));
			map.put(CALL_PLOT, imagemap.get(kpiIndexMap.get(ReportConstants.CALL_PLOT) + ""));
		
		} catch (Exception e) {
			logger.error("Error inside the method prepareImageMap{}", e.getMessage());
		}
	//	logger.info("image map is {}", new Gson().toJson(map));

		return (HashMap<String, Object>) map;
	}

	@Override
	public List<KPIWrapper> setKpiStatesIntokpiList(List<KPIWrapper> kpiList, List<Integer> woIds) {
		logger.info(" kpiList SIzE is {}, names {}  ",kpiList!=null?kpiList.size():null,kpiList!=null?kpiList.stream().map(KPIWrapper::getKpiName).collect(Collectors.toList()):null);
		List<String> rowkeyList = getRowKeyListOfKpiStatsForWoIds(woIds);
		if(kpiList!=null && !kpiList.isEmpty()){
			kpiList.forEach(wrapper -> {
				try {
					String kpiName = ReportUtil.getHbaseColumnNameByKpiName(wrapper);
					String statsData = nvLayer3DashboardService.getKpiStatsDataForGetListOfKpi(getTheGetListforEachKpi(rowkeyList,kpiName),kpiName);
				//	logger.debug(KPINAME_KPISTATS,kpiName ,statsData);
					Map<String, String[]> map = mapper.readValue(statsData, new TypeReference<Map<String, String[]>>() {
					});
					String[] kpistats = map.get(RESULT);
				//	logger.debug("kpistats {} ", kpistats != null ? Arrays.toString(kpistats) : null);
					wrapper.setKpiStats(kpistats);
				}  catch (IOException e) {
					logger.error("IOException inside method setKpiStatesIntokpiList {} ",Utils.getStackTrace(e));
				}catch (Exception e) {
					logger.error("Exception inside the method setKpiStatesIntokpiList {} ",Utils.getStackTrace(e));
				}
			});
		}
		return kpiList;
	}

	private List<Get> getTheGetListforEachKpi(List<String> rowkeyList, String kpiName) {
		List<Get> getList = new ArrayList<>();
		for (String rowkey : rowkeyList) {
			Get get = new Get(Bytes.toBytes(rowkey + kpiName));
			get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.RANGE_STATS.getBytes());
			getList.add(get);
		}
		return getList;
	}

	private List<String> getRowKeyListOfKpiStatsForWoIds(List<Integer> woIds) {
		List<String> rowkeyList = new ArrayList<>();
		for (Integer workOrdrId : woIds) {
			Map<String, List<String>> recipeOperatorMap = nvLayer3DashboardService.getDriveRecipeDetail(workOrdrId);
			if (recipeOperatorMap != null) {
				rowkeyList.addAll(nvLayer3DashboardService.getRowkeyListForWoIds(workOrdrId,
						recipeOperatorMap.get(RECIPE), recipeOperatorMap.get(OPERATOR)));
			}
		}
		logger.info("RowKeyList for Stats Data {} ",rowkeyList);
		return rowkeyList;
	}

	private ReportSubWrapper populateGraphDataKpiWise(ReportSubWrapper subWrapper, KPIWrapper kpiWrapper,
			List<String[]> arlist) {
		logger.debug("inside the method populateGraphDataKpiWise for kpi {} ",kpiWrapper.getKpiName());
		try {
			if (RSRP.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setRsrpList(ReportUtil.setGraphDataKpiWise(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));
			} else if (SINR.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setSinrList(ReportUtil.setGraphDataKpiWise(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));
			} else if (DL.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setDlList(ReportUtil.setGraphDataKpiWise(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));
			} else if (UL.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setUlList(ReportUtil.setGraphDataKpiWise(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));

			}else if (CQI.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setCqiList(ReportUtil.setGraphDataKpiWise(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));

			}else if (MOS.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setMosList(ReportUtil.setGraphDataKpiWise(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));

			}else if (MIMO.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setRiList(ReportUtil.setGraphDataKpiWise(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));

			}else if (DL_EARFCN.equalsIgnoreCase(kpiWrapper.getKpiName())) {
				subWrapper.setEarfcnList(ReportUtil.setGraphDataKpiWise(kpiWrapper,
						ReportUtil.convetArrayToList(arlist, kpiWrapper.getIndexKPI())));

			}
		} catch (Exception e) {
			logger.error("Exception inside populateGraphDataKpiWise {}", Utils.getStackTrace(e));
		}
		return subWrapper;
	}

	@Override
	public ReportSubWrapper getGraphDataWrapper(List<KPIWrapper> kpiList,List<String[]> arlist) {
		ReportSubWrapper subWrapper = new ReportSubWrapper();
		for (KPIWrapper kpiWrapper : kpiList) {
			populateGraphDataKpiWise(subWrapper, kpiWrapper, arlist);
		}
		return subWrapper;
	}

	@Override
	public void genrateDocxReport(String hdfsPath, String pdfFilePath, String docFilePath) {
		File docFile = null;
		try {
			logger.info("docFilePath is {}", docFilePath);
			ReportUtil.convertPdfToDoc(pdfFilePath);
			if (docFilePath != null) {
				docFile = new File(docFilePath);
				hdfsPath += docFile.getName();
				logger.info("genrateDocxReport Docfile {} , HdfsPath{} ", docFile.getAbsolutePath(), hdfsPath);
				
					if( docFile.getName() != null && docFile.exists()) {
						nVReportHdfsDao.saveFileToHdfs(docFile, hdfsPath);
						logger.info("docfile saved to hdfs");
					}
				
			}
		} catch (Exception e) {
			logger.error("Error in generating Docx Report {}", e.getMessage());
		} finally {
			if (docFile != null) {
				deleteFileIfExist(docFile.getAbsolutePath());
			}
		}
	}

	@Override
	public Map<String, Integer> getMergedMapData(Integer workorderId, String kpi,List<String> recepiList, List<String> operatorList){
		List<Get> getList = nvLayer3DashboardService.getListForKpiStats(workorderId, recepiList, operatorList, kpi);
		String table = ConfigUtils.getString(NVLayer3Constants.QMDL_KPI_TABLE);
		try {
			List<HBaseResult> hbaseResultList = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
			Map<String,Integer> kpiCounterMap  = getAggregatedMapData(hbaseResultList,kpi);
		//	logger.info("kpiCounterMap Data for kpi  {} , is {} ",kpi,new Gson().toJson(kpiCounterMap));
			return kpiCounterMap;
		} catch (IOException e) {
			logger.error("Error in Getting getKpiStatsRecipeDataForReport Result {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		} catch (Exception e) {
			logger.error("Exception inside method getMergedMapData for kpi {} , msg is {} ",kpi,Utils.getStackTrace(e));
		}
		return Collections.emptyMap();
	}

	@SuppressWarnings("unchecked")
	private Map<String, Integer> getAggregatedMapData(List<HBaseResult> hbaseResultList, String kpiName) throws IOException {
		logger.info("Inside method getAggregatedMapData for hbaseResultList of Size {} ",hbaseResultList!=null?hbaseResultList.size():null);
		Map<String, Integer> peakDLvaluesMap = new HashMap<>();
		for (HBaseResult result : hbaseResultList) {
			try {
				String json = result.getString("rs");
			//	logger.info("Json Data of kpi  {} , is {}, is {} ",kpiName,++count,json);
				if(json!=null){
					Map<String, Integer> peakDLMap = new ObjectMapper().readValue(json, HashMap.class);
			//		logger.info("Carrier Aggregation Data {} ",peakDLMap);
					peakDLMap.forEach((k, v) -> peakDLvaluesMap.merge(k, v, Integer::sum));
				}
			//	logger.info("carrier Aggregation Data  {} ",peakDLvaluesMap);
			} catch (Exception e) {
				logger.error("Exception insdie method getAggregatedMapData {} ",Utils.getStackTrace(e));
			}
		}
		return peakDLvaluesMap;

	}

	@Override
	public List<SiteInformationWrapper> getSiteDatabyViewPortForTechnology(List<String[]> arlist, String technology) {
		try {
			Corner corner = ReportUtil.getminmaxLatlOnDriveList(arlist, DriveHeaderConstants.INDEX_LAT,
					DriveHeaderConstants.INDEX_LON);
			Map<String, Double> viewportMap = ReportUtil.getViewPortMap(corner);
			return siteDetailService.getMacroSiteDetailsForCellLevelForReport(viewportMap, Arrays.asList(technology),
					null, null, false, true, true);
		} catch (Exception e) {
			logger.error("Exception inside the method setSiteDataForReport {}", Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	@Override
	public List<SiteInformationWrapper> getFirstTierNieghbourSites(List<String> neNameCellIdList) {
		List<SiteInformationWrapper> siteInfoList = new ArrayList<>();
		Map<String, List<String>> cellWiseNeighbourSiteMap = getCellWiseNeighbourSites(neNameCellIdList);
	//	logger.info("cellWiseNeighbourSiteMap Data {} ", cellWiseNeighbourSiteMap);
		cellWiseNeighbourSiteMap.forEach((key, value) -> {
			try {
	//			logger.info("neNameList Data {} " + value);
				siteInfoList.addAll(
						siteDetailService.getMacroSiteDetailsForCellLevelForReport(null, null, value, null, true, true, true));
			} catch (Exception e) {
				logger.error("Exception in calculating the first tier Neighbour {} ", Utils.getStackTrace(e));
			}
		});
		return siteInfoList;
	}

	private Map<String, List<String>> getCellWiseNeighbourSites(List<String> neNameCellIdList) {
		Map<String,List<String>> cellWiseNeighbourSiteMap =  new HashMap<>();// need to change and use the api that will be developed by sudeep sir
		Map<String,List<NeighbourCellDetail>> cellWiseNeighborInfoMap = neighbourCellDetailService.getNeighbourCellDetailsForSourceCells(neNameCellIdList,getAutoCellWeekNo());
		if(cellWiseNeighborInfoMap!=null && cellWiseNeighborInfoMap.size()>INDEX_ZER0){
			cellWiseNeighborInfoMap.forEach((key,value)->
			cellWiseNeighbourSiteMap.put(key, value.stream().map(NeighbourCellDetail::getSapid1).collect(Collectors.toList()))
					);
		}
		return cellWiseNeighbourSiteMap;
	}

	/** EnodebID-cellId - (ecgi - MCC-MNC-CGI). */

	private Integer getAutoCellWeekNo() {
		Integer weekNo =null;
		try {
			List<SystemConfiguration> listOfSystemConfig = iSystemConfigurationDao.getSystemConfigurationByName(AUTO_CELL_RANGE_MAX_WEEK);
			logger.info("listOfSystemConfig {} ",listOfSystemConfig);
			if(listOfSystemConfig !=null && !listOfSystemConfig.isEmpty()){
				for (SystemConfiguration systemConfigObj : listOfSystemConfig) {
					if(systemConfigObj.getValue()!=null){
						weekNo = Integer.parseInt(systemConfigObj.getValue());
						logger.info("Going to return the Auto cell Range week no {} ",weekNo);
						return weekNo;
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception occured inside method Auto cell week no calculation {} ",Utils.getStackTrace(e));
		}
		return weekNo;
	}

	@Override
	public List<PSDataWrapper> getPsDataWrapperFromSummaryData(String[] summaryArray) {
		List<PSDataWrapper> list = new ArrayList<>();
		PSDataWrapper wrapper = null;
		if (summaryArray != null && summaryArray.length > INDEX_ZER0) {

			Integer httpAttempt = NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_HTTP_ATTEMPT);
			if(httpAttempt!=null) {
				wrapper = new PSDataWrapper();
				wrapper.setHttpDownloadAttempt(httpAttempt);
				wrapper.setHttpDownloadSuccess(
						NVLayer3Utils.getInteger(summaryArray, DriveHeaderConstants.SUMMARY_HTTP_SUCCESS));
				wrapper.setHttpDownloadTimeAvg(
						NVLayer3Utils.getDoubleFromCsv(summaryArray, DriveHeaderConstants.SUMMARY_HTTP_DOWNLOAD_TIME));
				if (wrapper.getHttpDownloadAttempt() != null && wrapper.getHttpDownloadSuccess() != null) {
					wrapper.setHttpDlSuccessRate(ReportUtil.getPercentage(wrapper.getHttpDownloadSuccess(),
							wrapper.getHttpDownloadAttempt()));
				}
				wrapper.setHttpThroughputAvg(
						NVLayer3Utils.getDoubleFromCsv(summaryArray, DriveHeaderConstants.SUMMARY_DL_HTTP_AVG));
				wrapper.setNetworkLatency(
						NVLayer3Utils.getDoubleFromCsv(summaryArray, DriveHeaderConstants.SUMMARY_LATENCY_INDEX));
				wrapper.setPacketLoss(
						NVLayer3Utils.getDoubleFromCsv(summaryArray, DriveHeaderConstants.SUMMARY_PACKETLOSS));
				logger.info("PS Data {} ", new Gson().toJson(wrapper));
				list.add(wrapper);
			}
			return list;
		}
		logger.info("Returning Ps List data Wrapper {} ", list.toString());
		return list;
	}
	
	@Override
	public List<PSDataWrapper> getPsDataWrapperFromSummaryDataForReport(String[] summaryArray, Map<String, Integer> summaryKpiIndexMap) {
		List<PSDataWrapper> list = new ArrayList<>();
		PSDataWrapper wrapper = null;
		if (summaryArray != null && summaryArray.length > INDEX_ZER0) {

			Integer httpAttempt = NVLayer3Utils.getInteger(summaryArray, summaryKpiIndexMap.get(SUM_UNDERSCORE+HTTP_ATTEMPT));
			if(httpAttempt!=null && httpAttempt > 0) {
				wrapper = new PSDataWrapper();
				wrapper.setHttpDownloadAttempt(httpAttempt);
				wrapper.setHttpDownloadSuccess(
						NVLayer3Utils.getInteger(summaryArray, summaryKpiIndexMap.get(SUM_UNDERSCORE+HTTP_SUCCESS)));
				wrapper.setHttpDownloadTimeAvg(
						NVLayer3Utils.getDoubleFromCsv(summaryArray, summaryKpiIndexMap.get(SUM_UNDERSCORE+HTTP_DOWNLOAD)));
				if (wrapper.getHttpDownloadAttempt() != null && wrapper.getHttpDownloadSuccess() != null) {
					wrapper.setHttpDlSuccessRate(ReportUtil.getPercentage(wrapper.getHttpDownloadSuccess(),
							wrapper.getHttpDownloadAttempt()));
				}
				wrapper.setHttpThroughputAvg(
						NVLayer3Utils.getDoubleFromCsv(summaryArray, summaryKpiIndexMap.get(AVG_UNDERSCORE+DL_HTTP)));
				wrapper.setNetworkLatency(
						NVLayer3Utils.getDoubleFromCsv(summaryArray, summaryKpiIndexMap.get(AVG_UNDERSCORE+LATENCY)));
				wrapper.setPacketLoss(
						NVLayer3Utils.getDoubleFromCsv(summaryArray, summaryKpiIndexMap.get(AVG_UNDERSCORE+PACKET_LOSS)));
			//	logger.info("PS Data {} ", new Gson().toJson(wrapper));
				list.add(wrapper);
			}
			return list;
		}
		logger.info("Returning Ps List data Wrapper {} ", list.toString());
		return list;
	}

	@Override
	public List<KPIWrapper> setKpiStatesIntokpiListForWoList(List<KPIWrapper> kpiList, List<Integer> workOrderIds,
			List<String> recepiList, List<String> operatorList) {
		List<KPIWrapper> list = new ArrayList<>();
		kpiList.forEach(kpiWrapper -> {
			try {
				if(kpiWrapper.getKpiName().equalsIgnoreCase(CA)){
					kpiWrapper.setKpiStatMap(getMergedMapDataForWoList(workOrderIds,ReportUtil.getHbaseColumnNameByKpiName(kpiWrapper), recepiList, operatorList));
					list.add(kpiWrapper);
				}else{
					kpiWrapper.setKpiStats(getKPiStatsDataFromHbaseForWoList(workOrderIds,
							ReportUtil.getHbaseColumnNameByKpiName(kpiWrapper), recepiList, operatorList));
					//setAverageValueOfKpi(kpiWrapper);
					list.add(kpiWrapper);
				}
			} catch (Exception e) {
				logger.error("Exception inside method setKpiStatesIntokpiList  for kpi Name {} ",kpiWrapper.getKpiName());
			}
		});
		logger.info("kpiList is ====={}", new Gson().toJson(kpiList));

		return list;
	}

	private void setAverageValueOfKpi(KPIWrapper kpiWrapper) {
		if(kpiWrapper.getKpiStats()!=null){
			OptionalDouble averageValue =  Stream.of(kpiWrapper.getKpiStats()).filter(val->val!=null).mapToDouble(Double::valueOf).average();
			kpiWrapper.setAverageValue(averageValue.isPresent() ? averageValue.getAsDouble() : null); 
			logger.info("kpiWrapper.setAverageValue {} ",kpiWrapper.getAverageValue());
		}
	}

	@Override
	public Map<String, Integer> getMergedMapDataForWoList(List<Integer> workorderIds, String kpi,List<String> recepiList, List<String> operatorList){
		List<Get> getList = nvLayer3DashboardService.getListForKpiStats(workorderIds, recepiList, operatorList, kpi);
		String table = ConfigUtils.getString(NVLayer3Constants.QMDL_KPI_TABLE);
		try {
			List<HBaseResult> hbaseResultList = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
			return  getAggregatedMapData(hbaseResultList,kpi);
		
		} catch (IOException e) {
			logger.error("Error in Getting getKpiStatsRecipeDataForReport Result {}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		} catch (Exception e) {
			logger.error("Exception inside method getMergedMapData for kpi {} , msg is {} ",kpi,Utils.getStackTrace(e));
		}
		return Collections.emptyMap();
	}

	private String[] getKPiStatsDataFromHbaseForWoList(List<Integer> workOrderIds, String kpiname, List<String> recepiList,
			List<String> operatorList) {
		String[] kpistats = null;
		try {
			String kpistat = nvLayer3DashboardService.getKpiStatsRecipeDataForReportForWoList(workOrderIds, kpiname, recepiList,
					operatorList);
//			logger.info(KPINAME_KPISTATS,kpiname ,kpistat);
			Map<String, String[]> map = mapper.readValue(kpistat, new TypeReference<Map<String, String[]>>() {
			});
			kpistats = map.get(RESULT);
//			logger.info("kpistats {} ", kpistats != null ? Arrays.toString(kpistats) : null);
			return kpistats;
		} catch (Exception e) {
			logger.error("Exception inside method getKPiStatsDataFromHbase for kpiName {}  , {} ",kpiname, e.getMessage());
		}
		return kpistats;
	}

	@Override
	public String getDriveData(List<Integer> workOrderIds, List<String> recipeList, List<String> operatorList) {
		String combineData=null;
		try {
			combineData = nvLayer3DashboardService.getDriveDetailReceipeWiseforWoIds(workOrderIds,recipeList,operatorList);
			Map<String, String> dataMap = ReportUtil.convertCSVStringToMap(combineData);
			return dataMap.get(RESULT);
		} catch (BusinessException e) {
			logger.error("Exception inside method  for workOrderIds {} ,recipeList {} , operatorList {} ,msg {} ",
					workOrderIds,recipeList,operatorList,e.getMessage());
		}
		return combineData;
	}

	@Override
	public String getSummaryDataForWoIds(List<Integer> workOrderIds, List<String> recipeList, List<String> operatorList) {
		logger.info("Inside method getSummaryData for workOrderId {} , recipeList {} , operatorList {} ",workOrderIds,recipeList,operatorList);
		try {
			logger.info("workOrderId : {},  recipeList : {}, operatorList : {}",workOrderIds, recipeList,operatorList);
			String summaryData = nvLayer3DashboardService.getDriveSummaryReceipeWiseForWoIds(workOrderIds,recipeList,operatorList);
			Map<String, String> summaryMap = ReportUtil.convertCSVStringToMap(summaryData);
		//	logger.info("getSummaryData : {} , {}",summaryData, summaryMap);
			return summaryMap.get(RESULT);
		}catch(Exception e) {
			logger.error("Error inside the method getSummaryData {} ",Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public String getFloorplanImg(Integer workorderId, String recipeId, String opName,
			DriveDataWrapper driveDataWrapper) throws IOException {
		String floorPlanImagePath = ConfigUtils.getString(IB_BENCHMARK_REPORT_FLOOR_PLAN_PATH)
				+ IB_REPORT_IMAGE_NAME_PREFIX + workorderId + UNDERSCORE + opName
				+ UNDERSCORE + recipeId + UNDERSCORE + System.currentTimeMillis()
				+ IMAGE_FILE_EXTENSION;
		logger.info("floorPlanImagePath {}", floorPlanImagePath);

		BufferedImage imgPath = getBgImage(driveDataWrapper.getJson(), driveDataWrapper);

		if (imgPath != null) {
			ImageIO.write(imgPath, JPG, new File(floorPlanImagePath));
		}

		return floorPlanImagePath;
	}

	private BufferedImage getBgImage(String floorplanJson, DriveDataWrapper driveDataWrapper) {
		try {
			BufferedImage imBuff = null;
			boolean isbgImageAvailable = FloorPlanJsonParser.isImagePickedFromGallery(floorplanJson);
			if (isbgImageAvailable) {
				logger.info("driveDataWrapper.getFilePath() {}", driveDataWrapper.getFilePath());
				try (ZipInputStream zipSteam = nvLayer3HDFSDao.getZipSteamFromPath(driveDataWrapper.getFilePath(),
						FLOORPLANNAME + DOT_ZIP)) {

					ZipEntry entry = zipSteam.getNextEntry();
					while (entry != null) {
						if (entry.getName().contains(BACKGROUNDIMAGE)) {
							logger.info("inside the FloorPlanImage image  ");
							imBuff = ImageIO.read(zipSteam);
						}
						entry = zipSteam.getNextEntry();
					}
				} catch (Exception e) {
					logger.error("Error in processing file {}   ", Utils.getStackTrace(e));
				}
			}
			return imBuff;
		} catch (Exception e) {
			logger.error("Exception inside method getBgImagePath {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<KPIWrapper> getKpiWrapperList(Integer workorderId, List<String> recipe, String opName, String appliedTo) {
		List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(appliedTo);
		 return  ReportUtil.convertLegendsListToKpiWrapperList(legendList,
				DriveHeaderConstants.getKpiIndexMap());
	}

	@Override
	public List<KPIWrapper> getKpiWrapperListForReport(Integer workorderId, List<String> recipe, String opName, String appliedTo, Map<String, Integer> kpiIndexMap) {
		List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(appliedTo);
		return  ReportUtil.convertLegendsListToKpiWrapperList(legendList,
				kpiIndexMap);
	}
	@Override
	public List<KPIWrapper> getKPiStatsDataList(List<Integer> workorderId, Map<String, List<String>> map,
			Map<String, Integer> kpiIndexMap, String legendAppliedto) {
		try {
			List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(legendAppliedto);
			List<KPIWrapper> kpiList = ReportUtil.convertLegendsListToKpiWrapperList(legendList,kpiIndexMap);
			kpiList = modifyIndexOfCustomKpis(kpiList);
			return setKpiStatesIntokpiListForWoList(kpiList, workorderId, map.get(RECIPE),map.get(OPERATOR));
		} catch (Exception e) {
			logger.error("Exception occured inside method getKPiStatsDataList for workorderId {} , {} ",workorderId,Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	@Override
	public String getGeographyBoundaryByLevelAndName(String name, String level) {
		logger.info("inside the method getGeographyNameFromWorkOrder name and level {} {}", name, level);
		if (GEOGRAPHYL4.equalsIgnoreCase(level)) {
			return getL4BoundaryByName(name);
		} else if (GEOGRAPHYL3.equalsIgnoreCase(level)) {
			return getL3BoundaryByName(name);
		}
		return null;
	}

	private String getL3BoundaryByName(String name) {
		List<Map<String, String>> boundarMap;
		boundarMap = iGenericMapService.getBoundaryDataByGeographyNamesMS(Arrays.asList(name),
				GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.getColumnListForQuery(), null,GenericMapUtils.L3_TYPE);
		if (boundarMap != null && !boundarMap.isEmpty()) {
			return boundarMap.get(0).get(GenericMapUtils.COORDINATES);
		}
		return null;
	}

	private String getL4BoundaryByName(String name) {
		List<Map<String, String>> boundarMap;
		boundarMap = iGenericMapService.getBoundaryDataByGeographyNamesMS(Arrays.asList(name),
				GenericMapUtils.GEOGRAPHY_TABLE_NAME, GenericMapUtils.getColumnListForQuery(), null,GenericMapUtils.L4_TYPE);
		if (boundarMap != null && !boundarMap.isEmpty()) {
			return boundarMap.get(0).get(GenericMapUtils.COORDINATES);
		}
		return null;
	}

	@Override
	public  CustomGeographyWrapper getCustomGeographyRoute(String recipeCustomGeoMap) {
		//Map<String, String> gwoMeta = genericWorkorder.getGwoMeta();
		if(recipeCustomGeoMap!=null){
			try {
				String json =recipeCustomGeoMap;// gwoMeta.get(KEY_RECIPE_CUSTOM_GEO_MAP);
				if(json!=null){
					@SuppressWarnings("unchecked")
					Map<String,Integer> recipeWiseGeographyIdMap = new ObjectMapper().readValue(json, HashMap.class);
					if(recipeWiseGeographyIdMap!=null){
						Collection<Integer> rouetIds = recipeWiseGeographyIdMap.values();
						List<Integer> routeIdList = new ArrayList<>(rouetIds);
						if(routeIdList.size()>INDEX_ZER0){
							return customGeographyService.getCustomGeography(routeIdList.get(INDEX_ZER0), GeographyType.LINE);
						}
					}
				}
			} catch (Exception e) {
				logger.error("Unable to find the Custom geography route {} ",Utils.getStackTrace(e));
			}
		}
		return null;
	}

	/**
	 * Sets the site related information.
	 *
	 * @param wrapper the wrapper
	 * @param jsonMap the json map
	 */
	@Transactional
	@Override
	public CustomerComplaintSubWrapper setSiteRelatedInformation(CustomerComplaintSubWrapper wrapper,
			Map<String, Object> jsonMap) {
		Map<String, List<String>> geographyMap = getGeographyMapFromInput(jsonMap);
		logger.info("Geography Data map info {} ", geographyMap);
		try {
			List<SiteInformationWrapper> siteDetailList = siteDetailService.getMacroSiteDetailsForCellLevelForReport(
					null, null, null, geographyMap, false, true, false);
			List<String> listOfNeIdsHaveAlarm = getListOfNeidsHavingAlarm(siteDetailList);
	//		logger.info("listOfNeIdsHaveAlarm {} ", listOfNeIdsHaveAlarm);
			goingToSetOnAirSiteDetails(wrapper, siteDetailList);
			logger.info("macroSiteDetailList Size {} ", siteDetailList.size());
			if (Utils.isValidList(siteDetailList)) {
				List<SiteInformationWrapper> listOfCellsWiseData = setCellWiseInfo(wrapper, siteDetailList);
				logger.info("listOfCellsWiseData {} ", listOfCellsWiseData.size());
				wrapper.setCellWiseSiteList(listOfCellsWiseData);
				List<SiteInformationWrapper> lsitOfSitesWithoutAlarmStatus = getListOfSiteDataGroupBySFId(
						listOfCellsWiseData);
				logger.info("lsitOfSitesWithoutAlarmStatus size {} ", lsitOfSitesWithoutAlarmStatus.size());
				List<SiteInformationWrapper> lsitOfSitesWithAlarmStatus = taggedAlarmStatusInSite(
						lsitOfSitesWithoutAlarmStatus, listOfNeIdsHaveAlarm);
				wrapper.setSiteList(lsitOfSitesWithAlarmStatus);
			}
		} catch (Exception e) {
			logger.error("Exception inside method getSiteDataForReportByDataList  , errMsg {}", Utils.getStackTrace(e));
		}
		logger.info("CustomerComplaintSubWrapper data of sites {} ", new Gson().toJson(wrapper.getSiteList()));
		return wrapper;
	}

	/**
	 * Gets the geography map from input.
	 *
	 * @param jsonMap the json map
	 * @return the geography map from input
	 */
	private Map<String, List<String>> getGeographyMapFromInput(Map<String, Object> jsonMap) {
		Map<String,List<String>> geographyMap = new HashMap<>();
		List<String> geographyNames = new ArrayList<>();
		if(jsonMap.get(GEOGRAPHY_TYPE)!=null && jsonMap.get(GEOGRAPHY_NAME)!=null
				&& jsonMap.get(GEOGRAPHY_ID)!=null ){
			geographyNames.add(jsonMap.get(GEOGRAPHY_NAME).toString());
			geographyMap.put(jsonMap.get(GEOGRAPHY_TYPE).toString(), geographyNames);
			return geographyMap;
		}
		return null;
	}

	/**
	 * Gets the list of neids having alarm.
	 *
	 * @param networkElementList the network element list
	 * @return the list of neids having alarm
	 */
	//  New FM Alarm Integration
	private List<String> getListOfNeidsHavingAlarm(List<SiteInformationWrapper> networkElementList) {
		try {
			
			List<String> neIds = networkElementList	.stream()
					.filter(obj -> obj != null).filter(obj->obj.getNeId()!=null)
					.map(SiteInformationWrapper::getNeId)
					.collect(Collectors.toList());
			/* List<String> neIds = new ArrayList<>(); neIds.add("NJKT_0029"); */
		//	logger.info("List of NeIds to get the Alarm Status {} ", neIds);
			List<NEHaveAlarm> lsitOfNEAlarms = alarmLayerService.isSiteHaveAlarm(neIds, null, null, null, null, null,null,null, null);
			logger.info("lsitOfNEAlarms Data Size {} , {} ", lsitOfNEAlarms != null ? lsitOfNEAlarms.size() : null,lsitOfNEAlarms);
			return lsitOfNEAlarms!=null?lsitOfNEAlarms.stream().map(NEHaveAlarm::getNeId).collect(Collectors.toList()):null;
		} catch (Exception e) {
			logger.error("Error occured inside method getListOfNeidsHavingAlarm {} ",Utils.getStackTrace(e));
		}
		return null;
	}
	
	
	private List<AlarmDataWrapper> getListOfNEHavingAlarmDetails(List<SiteInformationWrapper> siteDetailList,Long startTimeStamp,Long endTimeStamp) {
		List<AlarmDataWrapper> alarmHistoryDataList = new ArrayList<>();
		try {
			if (Utils.isValidList(siteDetailList)) {
				List<String> neIds = siteDetailList	.stream()
													.filter(obj -> obj != null)
													.filter(obj -> obj.getNeId() != null)
													.map(SiteInformationWrapper::getNeId)
													.collect(Collectors.toList());
				logger.info("List of NeIds to get the Alarm Status ==> {} ", neIds);
				String parentNeId = siteDetailList	.get(INDEX_ZER0)
													.getNeId();
		//		logger.info("parentNeId from NewtworkElement {} ", parentNeId);
				alarmHistoryDataList = alarmService.getSiteHistoryForCustomerCare(INDEX_ZER0,
						INDEX_HUNDRED, parentNeId, null, startTimeStamp, endTimeStamp, null, null);
				/*
				 * for (String neId : neIds) { alarmHistoryData =
				 * alarmService.getSiteHistoryForCustomerCare(INDEX_ZER0,
				 * INDEX_HUNDRED, parentNeId, neId, startTimeStamp,
				 * endTimeStamp); if(alarmHistoryData!=null){
				 * alarmHistoryDataList.addAll(alarmHistoryData); } }
				 */
			}
			return alarmHistoryDataList;
//			return alarmLayerService.isSiteHaveAlarm(neIds,startTimeStamp,endTimeStamp, null, null);
		} catch (Exception e) {
			logger.error("Error occured inside method getListOfNeidsHavingAlarm {} ", Utils.getStackTrace(e));
		}
		return null;
	}


	/**
	 * Going to set on air site details.
	 *
	 * @param wrapper the wrapper
	 * @param siteDetailList the network element list
	 */
	private void goingToSetOnAirSiteDetails(CustomerComplaintSubWrapper wrapper,
			List<SiteInformationWrapper> siteDetailList) {
		wrapper.setNoOfSites(siteDetailList.size());
		try {
			if(wrapper.getNoOfSites()!=0){
				Long onAirCount = siteDetailList.stream()
													.filter(obj->obj.getNeStatus()!=null)
													.filter(obj -> obj.getNeStatus().toString().equalsIgnoreCase(NEStatus.ONAIR.toString()))
													.count();
		//		logger.info("onAir Site Count {} ",onAirCount);
				Double onAirSitePercent = (onAirCount.intValue() * HUNDRED) / wrapper.getNoOfSites();
				wrapper.setOnAirSitePercentage(onAirSitePercent);
			}else{
				wrapper.setOnAirSitePercentage(0.0);	
			}
		} catch (Exception e) {
			logger.error("Unable to calculate the percentage of on air site  {} ",Utils.getStackTrace(e));
		}
	}

	/**
	 * Sets the cell wise info.
	 *
	 * @param wrapper the wrapper
	 * @param siteDetailList the macro site detail list
	 * @return the list
	 */
	private List<SiteInformationWrapper> setCellWiseInfo(CustomerComplaintSubWrapper wrapper,List<SiteInformationWrapper> siteDetailList) {
		wrapper.setNoOfCells(siteDetailList.size());
		try {
			Long onAirCellCount = siteDetailList.stream().filter(obj->NEStatus.ONAIR.toString().equalsIgnoreCase(obj.getNeStatus())).count();

			logger.info("onAir Cell Count {} ",onAirCellCount);
			Double onAirCellpercent = (onAirCellCount.intValue() * HUNDRED) / wrapper.getNoOfCells();
			wrapper.setOnAirCellPercentage(onAirCellpercent);
		} catch (Exception e) {
			logger.error("Exception inside method setCellWiseInfo {} ",Utils.getStackTrace(e));
		}
		return siteDetailList;
	}
	
		/**
	 * Gets the list of site data group by SF id.
	 *
	 * @param listOfCellsWiseData the list of cells wise data
	 * @return the list of site data group by SF id
	 */
	private List<SiteInformationWrapper> getListOfSiteDataGroupBySFId(List<SiteInformationWrapper> listOfCellsWiseData) {
		Map<String,List<SiteInformationWrapper>> siteInfoGroupBySiteName = listOfCellsWiseData.stream().collect(Collectors.groupingBy(SiteInformationWrapper::getSiteName));
		//logger.info("siteInfoGroupBySiteName {} ",new Gson().toJson(siteInfoGroupBySiteName));
		List<SiteInformationWrapper> finalSiteList  = new ArrayList<>();
		if(!siteInfoGroupBySiteName.isEmpty()){
			siteInfoGroupBySiteName.forEach((Key,Value)->{
				finalSiteList.add(getSiteResultForEachSite(Value));
			});
		}
	//	logger.info("Final SiteData List {} ",finalSiteList);
		return finalSiteList;
	}

	/**
	 * Tagged alarm status in site.
	 *
	 * @param listOfSitesWithoutAlarmStatus the lsit of sites without alarm status
	 * @param listOfNeIdsHaveAlarm the list of ne ids have alarm
	 * @return the list
	 */
	// One Doubt is here ,To check if site having two sector with alarm, what
	// will be the final status of site
	private List<SiteInformationWrapper> taggedAlarmStatusInSite(
			List<SiteInformationWrapper> listOfSitesWithoutAlarmStatus, List<String> listOfNeIdsHaveAlarm) {
		if (listOfNeIdsHaveAlarm != null && !listOfNeIdsHaveAlarm.isEmpty()) {
			for (int index = INDEX_ZER0; index < listOfSitesWithoutAlarmStatus.size(); index++) {
				if (listOfNeIdsHaveAlarm.contains(listOfSitesWithoutAlarmStatus.get(index).getNeId())) {
					listOfSitesWithoutAlarmStatus.get(index).setAlarmstatus(CLEAR);
				} else {
					listOfSitesWithoutAlarmStatus.get(index).setAlarmstatus(NOT_CLEAR);
				}
			}
		}
		logger.info("lsitOfSitesWithAlarmStatus data {} ", new Gson().toJson(listOfSitesWithoutAlarmStatus));
		return listOfSitesWithoutAlarmStatus;
	}
	
	/**
	 * Gets the site result for each site.
	 *
	 * @param value the value
	 * @return the site result for each site
	 */
	private SiteInformationWrapper getSiteResultForEachSite(List<SiteInformationWrapper> value) {
		SiteInformationWrapper wrapper = value.get(INDEX_ZER0);
		if (value.size() > INDEX_ONE) {
			IntStream.range(INDEX_ONE, value.size()).forEach(index -> {
				if(value.get(index).getAzimuth()!=null && !ForesightConstants.NULL_STRING.equals(value.get(index).getAzimuth())){
				wrapper.setAzimuth((wrapper.getAzimuth() != null && !ForesightConstants.NULL_STRING.equals(wrapper.getAzimuth()))
						? wrapper.getAzimuth() + ForesightConstants.FORWARD_SLASH + value.get(index).getAzimuth()
						: value.get(index).getAzimuth());
				}if(value.get(index).geteTilt()!=null && !ForesightConstants.NULL_STRING.equals(value.get(index).geteTilt())){
				wrapper.seteTilt((wrapper.geteTilt() != null && !ForesightConstants.NULL_STRING.equals(wrapper.geteTilt()))
						? wrapper.geteTilt() + ForesightConstants.FORWARD_SLASH + value.get(index).geteTilt()
						: value.get(index).geteTilt());
				}if(value.get(index).getmTilt()!=null && !ForesightConstants.NULL_STRING.equals(value.get(index).getmTilt())){
				wrapper.setmTilt((wrapper.getmTilt() != null && !ForesightConstants.NULL_STRING.equals(wrapper.getmTilt()))
						? wrapper.getmTilt() + ForesightConstants.FORWARD_SLASH + value.get(index).getmTilt()
						: value.get(index).getmTilt());
				}
			});
		}
		return wrapper;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void saveKpiSummaryData(GenericWorkorder workorderObj, List<KPISummaryDataWrapper> kpiSummaryDataList) {
		if (kpiSummaryDataList != null) {
			try {
				String json = new Gson().toJson(kpiSummaryDataList);
				if (workorderObj.getGwoMeta() != null) {
					workorderObj.getGwoMeta().put(KPI_SUMMARY, json);
					iGenericWorkorderDao.update(workorderObj);

				}
				logger.info("kpiSummary is successfully inserted");
			} catch (Exception e) {
				logger.error("Exception inside method saveKpiSummaryData {} ", Utils.getStackTrace(e));
			}
		}
	}

	@Override
	public boolean getFileProcessedStatusForWorkorders(List<Integer> workorderIdList) {
		if(Utils.isValidList(workorderIdList)) {
			List<WOFileDetail> wofileDetailList = wofileDetailService.getFileDetailByWorkOrderIdList(workorderIdList);
			logger.info("Method to return true , when all files are processed for the workorder ");
			if (Utils.isValidList(wofileDetailList)) {
				logger.info("wofileDetailList Size {} ", wofileDetailList.size());
				for (WOFileDetail wofileObj : wofileDetailList) {
					if (wofileObj.getIsProcessed() != null && !wofileObj.getIsProcessed()) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean getFileProcessedStatusForRecipeAndWorkorder(Integer workorderId, Integer recipeId) {
		if (workorderId != null && recipeId != null) {
			List<WOFileDetail> wofileDetailList = wofileDetailService.getUnProcessFileDetailByWorkOrderId(workorderId,
					recipeId);
			logger.info("Method to return true , when all files are processed for the workorder ");
			if (Utils.isValidList(wofileDetailList)) {
				return false;
			} else if(wofileDetailList != null && wofileDetailList.isEmpty()){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean getFileDetailByRecipeMappingId(Integer recipeId) {
		if (recipeId != null) {
			List<WOFileDetail> wofileDetailList = wofileDetailService.getFileDetailByRecipeMappingId(recipeId);
			if (Utils.isValidList(wofileDetailList)) {
				logger.info("wofileDetailList Size {} ", wofileDetailList.size());
				for (WOFileDetail wofileObj : wofileDetailList) {
					if (wofileObj.getIsProcessed() != null && !wofileObj.getIsProcessed()) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public List<KPISummaryDataWrapper> getKPISummaryDataForWorkOrderId(Integer workOrderID, List<String[]> dataKPIs,
			List<KPIWrapper> listOfKPidata, String[] summaryData, String reportType, String operator,
			List<Double> dlSpeedtestList, Map<String, List<String>> map, Map<String, Integer> kpiIndexMap,  Map<String, Integer> summaryKpiIndexMap) {
		logger.info("Inside method getKPISummaryDataForWorkOrderId  for id {}  ", workOrderID);
		List<Object[]> objectList = iGenericWorkorderDao.getNVReportConfiguration(reportType);
		List<NVReportConfigurationWrapper> nvWrapperList = ReportUtil.getNVReportconfiguratioinList(objectList);
		List<KPISummaryDataWrapper> listofSummaryData = getKpiDataForSummary(dataKPIs, listOfKPidata, nvWrapperList);
		if(dataKPIs != null) {
			listofSummaryData.addAll(getSummaryDataListOfSamples(dataKPIs, nvWrapperList, kpiIndexMap));
		}
		if ((reportType.equalsIgnoreCase(SSVTQUICK)
				|| reportType.equalsIgnoreCase(SSVTFULL)) && map != null) {
			listofSummaryData.addAll(getDlPeakValuesBandWise(workOrderID, map));
		}
		try {
			List<String> summaryFieldArrayList = getSummaryFieldKeysList(reportType);
			for (String key : summaryFieldArrayList) {
				List<NVReportConfigurationWrapper> filterList = nvWrapperList.stream()
						.filter(wrapper -> wrapper.getKpi().equalsIgnoreCase(key)).collect(Collectors.toList());
				listofSummaryData
						.add(ReportUtil.getKPiSummaryDataWrapperForKeyForReport(key, summaryData, filterList, dlSpeedtestList, summaryKpiIndexMap));
			}
		} catch (Exception e) {
			logger.error("Error during Casting of Summary data {} ", Utils.getStackTrace(e));
		}
		logger.info("Going to return the KPi Summary data of list Size {} ", listofSummaryData.size());
		return listofSummaryData;
	}

	private List<String> getSummaryFieldKeysList(String reportType) {
		List<String> summaryFeildArrayList = new ArrayList<>(
				Arrays.asList(ReportConstants.ERAB_DROP_RATE, ReportConstants.HANDOVER_SUCCESS_RATE,
						ReportConstants.VOLTE_SETUP_SUCCESS_RATE, ReportConstants.VOLTE_ERAB_DROP_RATE
				 ));
		if (reportType.equalsIgnoreCase(ReportConstants.SSVT) || reportType.equalsIgnoreCase(ReportConstants.NVSSVT)
				|| reportType.equalsIgnoreCase(ReportConstants.SSVTQUICK)
				|| reportType.equalsIgnoreCase(ReportConstants.SSVTFULL)) {
			summaryFeildArrayList.add(ReportConstants.NETVELOCITY_SPEED_TEST);
			summaryFeildArrayList.add(ReportConstants.RRC_CONNECTION_SUCCESS_RATE);
		} else {
			summaryFeildArrayList.add(ReportConstants.RRC_CONNECTION_REQUEST_SUCCESS);
			summaryFeildArrayList.add(ReportConstants.AVERAGE_HANDOVER_INTERRUPTION_TIME);
			summaryFeildArrayList.add(ReportConstants.AVERAGE_SINR);
			summaryFeildArrayList.add(ReportConstants.VOLTE_VOICE_MEAN_OPINION_SOURCE);
			summaryFeildArrayList.add(ReportConstants.VOLTE_CALL_DROP_RATE);
		}
		return summaryFeildArrayList;
	}


	private List<KPISummaryDataWrapper> getSummaryDataListOfSamples(List<String[]> dataKPIs,
			List<NVReportConfigurationWrapper> nvWrapperList, Map<String, Integer> kpiIndexMap) {
		List<KPISummaryDataWrapper> kpiSummaryList = new ArrayList<>();
		for (NVReportConfigurationWrapper wrapper : nvWrapperList) {
			if (wrapper.getKpi().contains("CASE") || wrapper.getKpi().contains("CASE1")) {
				kpiSummaryList.add(getsummaryDataforKpi(dataKPIs, wrapper, kpiIndexMap));
			}
		}
		logger.info("kpiSummaryList Size {}  , data {} ", kpiSummaryList.size(), kpiSummaryList.toString());
		return kpiSummaryList;
	}

	private KPISummaryDataWrapper getsummaryDataforKpi(List<String[]> dataKPIs, NVReportConfigurationWrapper wrapper, Map<String, Integer> kpiIndexMap) {
		logger.info("Inside method  getsummaryDataforKpi for kpi {} ", wrapper.getConfiguration());
		Integer technologyIndex = kpiIndexMap.get(DriveHeaderConstants.TECHNOLOGY);
		String[] kpiPart = wrapper.getConfiguration().split(ReportConstants.SPACE);
		String tech = kpiPart[3];
		String dlbandwidth = kpiPart[4];
		String ulOrdl = kpiPart[6].replace(ReportConstants.OPEN_BRACKET, ReportConstants.BLANK_STRING)
				.replace(ReportConstants.CLOSED_BRACKET, ReportConstants.BLANK_STRING);
		logger.info(" tech {} , dlbandwidth {} , ulOrdl {} ", tech, dlbandwidth, ulOrdl);
		switch (ulOrdl) {
		case "DL":
			return getSummaryDataforDLUL(dataKPIs, wrapper, tech, dlbandwidth, kpiIndexMap.get(DL_BANWIDTH),
					kpiIndexMap.get(PDSCH_THROUGHPUT), kpiPart[0], technologyIndex);
		case "PDSCH":
			return getSummaryDataforDLUL(dataKPIs, wrapper, tech, dlbandwidth, kpiIndexMap.get(DL_BANWIDTH),
					kpiIndexMap.get(PDSCH_THROUGHPUT), kpiPart[0], technologyIndex);
		case "UL":
			return getSummaryDataforDLUL(dataKPIs, wrapper, tech, dlbandwidth, kpiIndexMap.get(UL_BANWIDTH),
					kpiIndexMap.get(UL_THROUGHPUT), kpiPart[0], technologyIndex);
		default:
			return null;
		}
	}

	private KPISummaryDataWrapper getSummaryDataforDLUL(List<String[]> dataKPIs, NVReportConfigurationWrapper wrapper,
			String tech, String dlbandwidth, Integer bandWidthIndex, Integer indexOfkpi, String criteria, Integer technologyIndex) {
		KPISummaryDataWrapper kpiWrapper = new KPISummaryDataWrapper();
		double count = 0.0;
		double totalCount = 0.0;
		try {
			if (bandWidthIndex != null && indexOfkpi != null && dataKPIs != null) {
				for (String[] data : dataKPIs) {
					if (data != null && technologyIndex != null && data.length > technologyIndex && data[technologyIndex] != null
							&& data[bandWidthIndex] != null && data[technologyIndex].equalsIgnoreCase(tech)
							&& data[bandWidthIndex]	.replace(" ", ReportConstants.BLANK_STRING)
													.replace("MHz", ReportConstants.BLANK_STRING)
													.equalsIgnoreCase(dlbandwidth)) {
//						
						if (data[indexOfkpi] != null && !data[indexOfkpi].isEmpty() && data.length > indexOfkpi) {
							totalCount++;
							if (NumberUtils.toDouble(data[indexOfkpi]) > NumberUtils.toDouble(
									wrapper.getTargetvalue())) {
								count++;
							}
						}
					}
				} 
			}
			if (count != 0.0 && totalCount != 0.0) {
				Double achivedValue = (count * 100) / (totalCount);
				kpiWrapper.setAchived(String.valueOf(ReportUtil.parseToFixedDecimalPlace(achivedValue, 2)));
			}
			if (kpiWrapper.getAchived() != null && !kpiWrapper.getAchived().isEmpty()) {
				if (criteria.contains("<")) {
					String compValue = StringUtils.substringBetween(criteria, "<", "%");
					kpiWrapper.setStatus((Double.parseDouble(kpiWrapper.getAchived()) < Double.parseDouble(compValue))
							? ReportConstants.PASS
							: ReportConstants.FAIL);
				} else if (criteria.contains(">")) {
					String compValue = StringUtils.substringBetween(criteria, ">", "%");
					kpiWrapper.setStatus((Double.parseDouble(kpiWrapper.getAchived()) > Double.parseDouble(compValue))
							? ReportConstants.PASS
							: ReportConstants.FAIL);
				}
				kpiWrapper.setAchived(kpiWrapper.getAchived() + ReportConstants.PERCENT);
			}
		} catch (Exception e) {
			logger.error("Exception insdie method getSummaryDataforKPi {} , {}  ", wrapper.getConfiguration(),
					Utils.getStackTrace(e));
		}
		kpiWrapper.setItem(ReportConstants.INTEGRITY);
		kpiWrapper.setTestName(wrapper.getConfiguration());
		kpiWrapper.setTarget((wrapper.getKpi().equalsIgnoreCase("CASE") ? ("<" + wrapper.getTargetvalue())
				: (">" + wrapper.getTargetvalue())) + ReportConstants.MBPS);
		logger.info("Going to retun kpi wrapper for kpi {} , {} ", wrapper.getConfiguration(), kpiWrapper.toString());
		return kpiWrapper;
	}

	@Override
	public List<List<List<Double>>> getAllCustomRoutesOfPrePost(List<Integer> previousWoId, List<Integer> currentWoId) {
		List<Integer> listOfAllWOIds = new ArrayList<>();
		listOfAllWOIds.addAll(previousWoId);
		listOfAllWOIds.addAll(currentWoId);
		return getAllCustomRoutes(listOfAllWOIds);
		
	}
	@SuppressWarnings("serial")
	private List<List<List<Double>>> getAllCustomRoutes(List<Integer> workorderIds) {
		List<List<List<Double>>> driveRoute = new ArrayList<>();
		List<GenericWorkorder> listOfGenericWO = iGenericWorkorderDao.findByIds(workorderIds);
		for(int index=0;index<listOfGenericWO.size();index++){
			if(listOfGenericWO.get(index).getGwoMeta()!=null){
				String json = listOfGenericWO.get(index).getGwoMeta().get(ReportConstants.KEY_RECIPE_CUSTOM_GEO_MAP);
				CustomGeographyWrapper customGeography = getCustomGeographyRoute(json);
				logger.debug("customGeography Data {} ",new Gson().toJson(customGeography));
				if(customGeography!=null && customGeography.getBoundary()!=null){
					List<List<List<Double>>> boundary = new Gson().fromJson(customGeography.getBoundary(),new TypeToken<List<List<List<Double>>>>() {}.getType());
					driveRoute.addAll(boundary);
				}
			}
		}
		logger.info("Custom Geography List Size for workorderids {} , is {} ",workorderIds,driveRoute.size());
		return driveRoute;
	}
	
	@Override
	@Transactional(readOnly  = true)
	public List<SiteInformationWrapper> getSiteDataForWorkorder(GenericWorkorder genericWorkorder) {

		List<SiteInformationWrapper> sitInfoWrapperList = new ArrayList<>();
		try {
			Map<String, String> geoMetaDataMap = genericWorkorder.getGwoMeta();
		//	logger.info("geoMetaDataMap is {}", geoMetaDataMap);
			String neName = getSiteNameFromWOMetaData(geoMetaDataMap);
			List<Integer> pciList = getPciListFromWoMetaData(geoMetaDataMap);
		//	logger.info("pciList {}", pciList);

			sitInfoWrapperList = siteDetailService.getMacroSiteDetailsForCellLevelForReport(null, null,
					Arrays.asList(neName), null, false, false, false);
			return convetMacroSiteDetailToSiteInfo(sitInfoWrapperList, pciList);
		} catch (Exception e) {
			logger.error("Exception inside the method getSiteDataForWorkorder {}",Utils.getStackTrace(e));
		}
		return sitInfoWrapperList;
	}

	@Override
	@Transactional(readOnly  = true)
	public List<SiteInformationWrapper> getSiteDataForSSVTReport(GenericWorkorder genericWorkorder) {

		List<SiteInformationWrapper> sitInfoWrapperList = new ArrayList<>();
		try {
			Map<String, String> geoMetaDataMap = genericWorkorder.getGwoMeta();
			//	logger.info("geoMetaDataMap is {}", geoMetaDataMap);
			String neName = getSiteNameFromWOMetaData(geoMetaDataMap);
			//	logger.info("pciList {}", pciList);

			sitInfoWrapperList = siteDetailService.getMacroSiteDetailsForCellLevelForReport(null, null,
					Arrays.asList(neName), null, false, false, true);
			return sitInfoWrapperList;
		} catch (Exception e) {
			logger.error("Exception inside the method getSiteDataForWorkorder {}",e.getMessage());
		}
		return sitInfoWrapperList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getDynamicKpiName(List<Integer> workorderIds, String recipeId, String layerType) {
		Layer3PPEWrapper wrapper = new Layer3PPEWrapper(workorderIds, layerType);
		if(recipeId != null) {
			wrapper.setRecipeId(Integer.valueOf(recipeId));
		}
		List<Map<String,Object>> dynamicMetaList = (List<Map<String, Object>>) layer3PPEService.getKPIBuilderMeta(wrapper);
		return dynamicMetaList.stream().map(map -> (String)map.get(Layer3PPEConstant.HBASE_COLUMN_NAME)).collect(Collectors.toSet());
		
	}

	@Override
	public List<KPIDataWrapper> populateKPiAggData(String[] smryData,List<KPIWrapper> listOfKpiWrapper,Map<String, Integer> summaryKpiIndexMap) {

		try {
	//		logger.debug("smryData : {},  listOfKpiWrapper : {} ",smryData,new Gson().toJson(listOfKpiWrapper));
			List<KPIDataWrapper> listOfKpiDataWrapper1 = new ArrayList<>();
			KPIDataWrapper kpiwrapper = null;
			for (KPIWrapper wrapper : listOfKpiWrapper) {
				switch(wrapper.getKpiName()){
				case DriveHeaderConstants.RSRP:
					kpiwrapper= getKPiWrapper(wrapper,smryData,summaryKpiIndexMap.get(ReportConstants.RSRP),summaryKpiIndexMap.get(ReportConstants.MIN_UNDERSCORE+ReportConstants.RSRP),summaryKpiIndexMap.get(ReportConstants.MAX_UNDERSCORE+ReportConstants.RSRP));
					break;
				case DriveHeaderConstants.SINR:
					kpiwrapper= getKPiWrapper(wrapper,smryData,summaryKpiIndexMap.get(ReportConstants.SINR),summaryKpiIndexMap.get(ReportConstants.MIN_UNDERSCORE+ReportConstants.SINR),summaryKpiIndexMap.get(ReportConstants.MAX_UNDERSCORE+ReportConstants.SINR));
					break;
				case DriveHeaderConstants.UL_THROUGHPUT:
					kpiwrapper= getKPiWrapper(wrapper,smryData,summaryKpiIndexMap.get(ReportConstants.UL_THROUGHPUT),summaryKpiIndexMap.get(ReportConstants.MIN_UNDERSCORE+ReportConstants.UL_THROUGHPUT),summaryKpiIndexMap.get(ReportConstants.MAX_UNDERSCORE+ReportConstants.UL_THROUGHPUT));
					break;
				case DriveHeaderConstants.DL_THROUGHPUT:
					kpiwrapper= getKPiWrapper(wrapper,smryData,summaryKpiIndexMap.get(ReportConstants.DL_THROUGHPUT),summaryKpiIndexMap.get(ReportConstants.MIN_UNDERSCORE+ReportConstants.DL_THROUGHPUT),summaryKpiIndexMap.get(ReportConstants.MAX_UNDERSCORE+ReportConstants.DL_THROUGHPUT));
					break;
				case DriveHeaderConstants.JITTER:
					kpiwrapper= getKPiWrapper(wrapper,smryData,summaryKpiIndexMap.get(ReportConstants.JITTER),summaryKpiIndexMap.get(ReportConstants.MIN_UNDERSCORE+ReportConstants.JITTER),summaryKpiIndexMap.get(ReportConstants.MAX_UNDERSCORE+ReportConstants.JITTER));
					break;
				case DriveHeaderConstants.LATENCY:
					kpiwrapper= getKPiWrapper(wrapper,smryData,summaryKpiIndexMap.get(ReportConstants.LATENCY),summaryKpiIndexMap.get(ReportConstants.MIN_UNDERSCORE+ReportConstants.LATENCY),summaryKpiIndexMap.get(ReportConstants.MAX_UNDERSCORE+ReportConstants.LATENCY));
					break;
				case DriveHeaderConstants.MOS:
					kpiwrapper= getKPiWrapper(wrapper,smryData,summaryKpiIndexMap.get(ReportConstants.MOS),summaryKpiIndexMap.get(ReportConstants.MIN_UNDERSCORE+ReportConstants.MOS),summaryKpiIndexMap.get(ReportConstants.MAX_UNDERSCORE+ReportConstants.MOS));
					break;
				default:
					kpiwrapper=null;
					break;
				}
		//		logger.debug("kpiwrapper data {} ",kpiwrapper);
				listOfKpiDataWrapper1.add(kpiwrapper);
			}
			logger.debug("listOfKpiDataWrapper : {} ",new Gson().toJson(listOfKpiDataWrapper1));
			return listOfKpiDataWrapper1;
		} catch (Exception e) {
			logger.error("Exception inside the method setSummaryData{}", e.getMessage());
		}
		return Collections.emptyList();
	}
	
	@Override
	public HashMap<String, String> getImagesForReport(List<KPIWrapper> kpiList, DriveImageWrapper driveImageWrapper,
			List<DriveDataWrapper> imageDataList, String operator,String reportType,Map<String, Integer> kpiIndexMap) throws IOException {
		List<Double[]> pinLatLonList = null;
		logger.info("operator {},kpiList {},",operator,kpiList.size());
		if (imageDataList != null) {
			pinLatLonList = imageDataList	.stream()
					.map(driveDataWrapper -> new Double[] { driveDataWrapper.getLongitude(),
							driveDataWrapper.getLatitutde() })
					.collect(Collectors.toList());
		}
		modifyIndexOfCustomKpisForReport(kpiList,kpiIndexMap);
		HashMap<String, BufferedImage> bufferdImageMap = mapImageService.getDriveImagesForReport(driveImageWrapper,pinLatLonList,kpiIndexMap);
		String imagePath = ConfigUtils.getString(IMAGE_PATH_FOR_NV_REPORT) + reportType
				+ FORWARD_SLASH
				+ ReportUtil.getFormattedDate(new Date(), DATE_FORMAT_DD_MM_YY_HH_SS)
				+ FORWARD_SLASH+((operator!=null)?operator.replace(SPACE, BLANK_STRING):BLANK_STRING);
		//logger.info("Image PAth finally  is {} ",imagePath);
		return mapImageService.saveDriveImages(bufferdImageMap, imagePath, false);
	}
	
	@Override
	public List<YoutubeTestWrapper> getYouTubeDataForReport(List<String[]> driveData,
			Map<String, Integer> kpiIndexMap) {
		logger.info("Inside method getYoutubeDataForReport");
		List<YoutubeTestWrapper> YoutubeDataList = new ArrayList<>();
		Map<String, List<String[]>> youtubeUrlDataMap = new HashMap<>();
		List<String[]> listarray = new ArrayList<>();
		String videoURL = null;
		for (String[] data : driveData) {
			if (ReportUtil.checkValidString(kpiIndexMap.get(ReportConstants.VIDEO_URL), data)
					&& !data[kpiIndexMap.get(ReportConstants.VIDEO_URL)].equalsIgnoreCase("FETCHING_VIDEO")) {
				videoURL = data[kpiIndexMap.get(ReportConstants.VIDEO_URL)];
				if (youtubeUrlDataMap.containsKey(videoURL)) {
					List<String[]> previousdata = youtubeUrlDataMap.get(videoURL);
					previousdata.add(data);
					youtubeUrlDataMap.replace(videoURL, previousdata);
				} else {
					listarray.add(data);
					youtubeUrlDataMap.put(videoURL, listarray);
					listarray = new ArrayList<>();
				}
			}
		}
		///youtubeUrlDataMap map of youtube url & drivedata //////////////////////////
		return getAggYoutubeData(youtubeUrlDataMap, kpiIndexMap);
	}
	
	public List<YoutubeTestWrapper> getAggYoutubeData(Map<String, List<String[]>> youtubeUrlDataMap,
			Map<String, Integer> kpiIndexMap) {
		List<YoutubeTestWrapper> youtubewrapperlist = new ArrayList<>();
		logger.info("Inside method getAggYoutubeData");
		if (youtubeUrlDataMap != null && !youtubeUrlDataMap.isEmpty()) {
			for (Entry<String, List<String[]>> urlwisemap : youtubeUrlDataMap.entrySet()) {
				YoutubeTestWrapper wrapper = new YoutubeTestWrapper();
				Map<String, Double> MinMaxMap = null;
				Set<String> videores = new HashSet<>();
				double prersrp = 0.0, presinr = 0.0;

				OptionalDouble avgRSRP = urlwisemap.getValue().stream().filter(x -> (x[kpiIndexMap.get(RSRP)] != null &&! x[kpiIndexMap.get(RSRP)].isEmpty()))
						.map(x -> x[kpiIndexMap.get(RSRP)]).collect(Collectors.toList()).stream()
						.mapToDouble(i -> Double.parseDouble(i)).average();
				wrapper.setAvgRSRP(avgRSRP.isPresent() ? avgRSRP.getAsDouble() : null);

				OptionalDouble avgSINR = urlwisemap.getValue().stream().filter(x -> (x[kpiIndexMap.get(SINR)] != null &&! x[kpiIndexMap.get(SINR)].isEmpty()))
						.map(x -> x[kpiIndexMap.get(SINR)]).collect(Collectors.toList()).stream()
						.mapToDouble(i -> Double.parseDouble(i)).average();
				wrapper.setAvgSINR(avgSINR.isPresent() ? avgSINR.getAsDouble() : null);

				for (String[] drivedata : urlwisemap.getValue()) {

					wrapper.setVideoURL(urlwisemap.getKey());

					if (ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.NUM_OF_STALLING), drivedata)) {
						wrapper.setNoOfStalling(drivedata[kpiIndexMap.get(ReportConstants.NUM_OF_STALLING)]);
					}

					if (ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.FREEZING_RATIO), drivedata)) {
						wrapper.setFreezingRatio(drivedata[kpiIndexMap.get(ReportConstants.FREEZING_RATIO)]);
					}

					if (ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.VIDEO_DURATION), drivedata)) {
						Long num = Long.parseLong(drivedata[kpiIndexMap.get(ReportConstants.VIDEO_DURATION)]);
						Long seconds = TimeUnit.MILLISECONDS.toSeconds(num);
						wrapper.setVideoDuration(Integer.valueOf(seconds.intValue()));
					}

					if (ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.TOTAL_BUFFER_TIME), drivedata)) {
						long totalbuffertime = Math.round(
								Double.parseDouble(drivedata[kpiIndexMap.get(ReportConstants.TOTAL_BUFFER_TIME)]));
						Long seconds = TimeUnit.MILLISECONDS.toSeconds(totalbuffertime);
						wrapper.setTotalBufferTime(Integer.valueOf(seconds.intValue()));
					}

					if (ReportUtil.checkIndexValue(kpiIndexMap.get(ReportConstants.VIDEO_RESOLUTION), drivedata)) {
						videores.add(drivedata[kpiIndexMap.get(ReportConstants.VIDEO_RESOLUTION)]);
						wrapper.setVideoResolution(NVLayer3Utils.getStringFromSetValues(videores)
								.replace(Symbol.UNDERSCORE, Symbol.HYPHEN));
					}

				}
				youtubewrapperlist.add(wrapper);
			}
		}
		logger.info("size of youtubedatalist {}", youtubewrapperlist != null ? youtubewrapperlist.size() : null);
		return youtubewrapperlist;
	}

	private List<String[]> getSortedDataList(List<String[]> driveData, Integer timestampIndex) {
		logger.info("Timestamp index: {}, Data size: {}", timestampIndex, driveData.size());
		List<String[]> data = driveData.stream()
									   .filter(c -> c != null && c.length > timestampIndex && NumberUtils.isParsable(
											   c[timestampIndex]))
									   .collect(Collectors.toList());
		Collections.sort(data, ReportUtil.getTimestampComparator(timestampIndex));
		return data;
	}
	
	@Override
	public List<String[]> getHandoverDataFromHbase(Integer workorderId, List<String> recipeList) {
		try {
		logger.info(
				"Inside Method getHandoverDataFromHbase with data=> workorderid: {}, recipeList: {}",
				workorderId, recipeList);
		if(workorderId != null && Utils.isValidList(recipeList)) {
		//	Set<String> operatorSet = new HashSet<>(operatorList);
			String table = ConfigUtils.getString(NVLayer3Constants.LAYER3_REPORT_TABLE);
			List<Get> getList = ReportUtil.getHandoverQueryList(workorderId, recipeList,
					null);
			List<HBaseResult> resultList = nvLayer3HbaseDao.getQMDLDataFromHBase(getList, table);
//			logger.info("resultList ==={}",resultList);
			return ReportUtil.getHandoverDataListFromHBaseResult(resultList);
		}}catch(Exception e) {
			logger.info("exception in getting handover data from hbase {}", Utils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	@Override
	public List<KPIWrapper> getKPIList(Map<String, Integer> kpiIndexMap,String reportType) {
		List<LegendWrapper> legendList = legendRangeDao.findAllLegendRangesAppliedTo(reportType);
		List<KPIWrapper> kpiList = ReportUtil.convertLegendsListToKpiWrapperList(legendList, kpiIndexMap);
		kpiList = modifyIndexOfCustomKpisForReport(kpiList, kpiIndexMap);
		return kpiList;
	}

	@Override
	public List<DriveDataWrapper> getSpeedTestDatafromHbase(Integer workorderId, List<String> receipeList) {
		logger.info("going to getSpeedTestDatafromHbase  id {}",workorderId);
		 List<DriveDataWrapper> speedtestDataList = new ArrayList<>();
		int index=0;
		for (String recipe : receipeList) {
			for (Integer i = 0; i < 10; i++) {
				    List<String> rowPrefixList = layer3PPEService.getPrefixListFromParamList(workorderId, Arrays.asList(recipe),null).stream().collect(Collectors.toList());
				    String rowkey = rowPrefixList.get(0)+i;
					logger.info(" rowKey for Speed test data {} ", rowPrefixList);
					Get get = new Get(Bytes.toBytes(rowkey));
					String tableName = ConfigUtils.getString(NVLayer3Constants.LAYER3_REPORT_TABLE);
					try {
						nVReportHbaseDao.setDataInDriveDataWrapperList(speedtestDataList, get, tableName);
					} catch (Exception e) {
						logger.error("Error Exception in Getting Result from hbase {}  ", Utils.getStackTrace(e));
					}
			}
			index++;
		}
		logger.info("Going to return the speedtestDataList of Size {} ", speedtestDataList.size());

		return speedtestDataList;
	}
	@Override
	public List<KPISummaryDataWrapper> getDlPeakValuesBandWise(Integer workorderId, Map<String, List<String>> map) {
		List<KPISummaryDataWrapper> kpiSmmryList = new ArrayList<>();
		try {
			if (map != null) {
				Map<String, Double> peakvalueData = getPeakValueData(workorderId, map);
				logger.info("peakvalueData {} ", peakvalueData);
				if (peakvalueData != null && peakvalueData.size() > 0) {
					for (Entry<String, Double> peakDLvalueMap : peakvalueData.entrySet()) {
						kpiSmmryList.add(getPeakValueRow(ReportConstants.PEAK_VALUE_SINGLE_SAMPLE,
								ReportConstants.DT_MOBILE, peakDLvalueMap));
					}
				}
			}
		} catch (BusinessException e) {
			logger.error("Exception inside methdo getDlPeakValuesBandWise {} ", Utils.getStackTrace(e));
		}
		return kpiSmmryList;
	}
	@Override
	public  KPISummaryDataWrapper getPeakValueRow(String testName, String source,
			Entry<String, Double> peakDLvalueMap) {
		KPISummaryDataWrapper wrapper = new KPISummaryDataWrapper();
		try {
			wrapper.setTestName(testName);
			wrapper.setSource(source);
			String key = peakDLvalueMap.getKey().replace(ReportConstants.SPACE, ReportConstants.BLANK_STRING);
			wrapper.setTarget(key.replace(ReportConstants.COMMA, ReportConstants.SPACE));
			if (peakDLvalueMap.getValue() != null) {
				wrapper.setAchived(peakDLvalueMap.getValue() + ReportConstants.SPACE + ReportConstants.MBPS);
				if (peakDLvalueMap.getKey().contains("5")) {
					wrapper.setStatus((peakDLvalueMap.getValue() > 27.0) ? ReportConstants.PASS : ReportConstants.FAIL);
					wrapper.setTarget(wrapper.getTarget() + ">27 Mbps");
				} else if (peakDLvalueMap.getKey().contains("10")) {
					wrapper.setStatus((peakDLvalueMap.getValue() > 50.0) ? ReportConstants.PASS : ReportConstants.FAIL);
					wrapper.setTarget(wrapper.getTarget() + ">50 Mbps");
				} else if (peakDLvalueMap.getKey().contains("20")) {
					wrapper.setStatus((peakDLvalueMap.getValue() > 75.0) ? ReportConstants.PASS : ReportConstants.FAIL);
					wrapper.setTarget(wrapper.getTarget() + ">75 Mbps");
				}
			}
		} catch (Exception e) {
			logger.info("Exception inside method getPeakValueRow {} ", e.getMessage());
		}
		return wrapper;
	}
	@Override
	public List<MessageDetailWrapper> getLayer3MessagesDataForReport(Integer workorderid, List<String> recipeList, List<String> kpiList1) {
		String table = ConfigUtils.getString(NVLayer3Constants.LAYER3_EVENT_TABLE);
		logger.info("table name -->  {}", table);
		List<String> rowPrefixList = layer3PPEService.getPrefixListFromParamList(workorderid, recipeList, null).stream()
				.collect(Collectors.toList());
		logger.info("rowprefixlist {}", rowPrefixList);
		try {
			List<HBaseResult> resultList = layer3PPEService.getPPEDataFromHbase(rowPrefixList, table);
			logger.info("hbase result list size ==={}", resultList.size());
			List<MessageDetailWrapper> list  = getMessageDataListFromHBaseResult(resultList);
			list.sort(Comparator.comparing(MessageDetailWrapper::getTimeStamp));
			return list;
		} catch (Exception e) {
			logger.error(" getLayer3MessagesDataForReport ===={}",Utils.getStackTrace(e));
		}
		return null;
	}
	
	private List<MessageDetailWrapper> getMessageDataListFromHBaseResult(List<HBaseResult> resultList) {
		List<MessageDetailWrapper> list =  new ArrayList<>();
		if (Utils.isValidList(resultList)) {
			for (HBaseResult result : resultList) {
				if (result != null) {
					setMessageDataToWrapper(result, list);
				}
			}
		}
		logger.info("Returning Final Message data List: {}", list.size());
		return list;
	}
	
	private void setMessageDataToWrapper(HBaseResult result,List<MessageDetailWrapper> list) {
		MessageDetailWrapper wrapper = new MessageDetailWrapper();
		String decodemsg = result.getString("decodedMsg");
		if (decodemsg!=null&&!StringUtils.isBlank(decodemsg)) {
			decodemsg=decodemsg.replaceAll("\\$", "\\\n");
			wrapper.setDecodedMsg(decodemsg);
		}		
		String sipmsg = result.getString("sipMessage");
		if (sipmsg!=null &&!StringUtils.isBlank(sipmsg)) {
			wrapper.setSipMessage(sipmsg);
		}		
		String rrcConnectionRequest = result.getString("rrcConnectionRequest");
		if (rrcConnectionRequest!=null &&!StringUtils.isBlank(rrcConnectionRequest)) {
			wrapper.setRrcConnectionRequest(rrcConnectionRequest);
		}
		String rrcConnectionSetup = result.getString("rrcConnectionSetupComplete");
		if (rrcConnectionSetup!=null &&! StringUtils.isBlank(rrcConnectionSetup)) {
			wrapper.setRrcConnectionSetup(rrcConnectionSetup);
		}
		String attachRequest = result.getString("attachRequest");
		if (attachRequest!=null &&!StringUtils.isBlank(attachRequest)) {
			wrapper.setAttachRequest(attachRequest);
		}
		String attachComplete = result.getString("attachComplete");
		if (attachComplete !=null &&! StringUtils.isBlank(attachComplete)) {
			wrapper.setAttachComplete(attachComplete);
		}
		String detachRequest = result.getString("detachRequest");
		if (detachRequest !=null &&!StringUtils.isBlank(detachRequest)) {
			wrapper.setDetachRequest(detachRequest);
		}
		String detachAccept = result.getString("detachAccept");
		if (detachAccept !=null && !StringUtils.isBlank(detachAccept)) {
			wrapper.setDetachAccept(detachAccept);
		}
		String rrcConnectionRelease = result.getString("rrcConnectionRelease");
		if (rrcConnectionRelease !=null &&! StringUtils.isBlank(rrcConnectionRelease)) {
			wrapper.setRrcConnectionRelease(rrcConnectionRelease);
		}
		String csfbMoCallAttempt = result.getString("csfbMoCallAttempt");
		if (csfbMoCallAttempt !=null &&! StringUtils.isBlank(csfbMoCallAttempt)) {
			wrapper.setCsfbMoCallAttempt(csfbMoCallAttempt);
		}
		String csfbMoCallSetupSuccess = result.getString("csfbMoCallSetupSuccess");
		if (csfbMoCallSetupSuccess!=null &&!StringUtils.isBlank(csfbMoCallSetupSuccess)) {
			wrapper.setCsfbMoCallSetupSuccess(csfbMoCallSetupSuccess);
		}
		String csfbMtCallAttempt = result.getString("csfbMtCallAttempt");
		if (csfbMtCallAttempt !=null &&! StringUtils.isBlank(csfbMtCallAttempt)) {
			wrapper.setCsfbMtCallAttempt(csfbMtCallAttempt);
		}
		String csfbMtCallSetupSuccess = result.getString("csfbMtCallSetupSuccess");
		if (csfbMtCallSetupSuccess !=null && !StringUtils.isBlank(csfbMtCallSetupSuccess)) {
			wrapper.setCsfbMtCallSetupSuccess(csfbMtCallSetupSuccess);
		}
		String volteMTCallAttempts = result.getString("volteMTCallAttempts");
		if (volteMTCallAttempts!=null && !StringUtils.isBlank(volteMTCallAttempts)) {
			wrapper.setVolteMTCallAttempts(volteMTCallAttempts);
		}
		String volteMTCallSetup = result.getString("volteMTCallSetup");
		if (volteMTCallSetup!=null && !StringUtils.isBlank(volteMTCallSetup)) {
			wrapper.setVolteMTCallSetup(volteMTCallSetup);
		}
		String volteMOCallAttempts = result.getString("volteMOCallAttempts");
		if (volteMOCallAttempts!=null && !StringUtils.isBlank(volteMOCallAttempts)) {
			wrapper.setVolteMOCallAttempts(volteMOCallAttempts);
		}
		String volteMOCallSetup = result.getString("volteMOCallSetup");
		if (volteMOCallAttempts!=null &&! StringUtils.isBlank(volteMOCallSetup)) {
			wrapper.setVolteMOCallSetup(volteMOCallSetup);
		}
		String tauAccept = result.getString("tauAccept");
		if (tauAccept !=null && !StringUtils.isBlank(tauAccept)) {
			wrapper.setTauAccept(tauAccept);
		}
		
		String fastReturnTime = result.getString("fastReturnTime2G");
		if (fastReturnTime!=null && !StringUtils.isBlank(fastReturnTime)) {
				wrapper.setFastReturnTime(fastReturnTime);		
		}
		
		String pci = result.getString("PCI");
		if (pci!=null &&! StringUtils.isBlank(pci)) {
			wrapper.setPCI(pci);
		}
		String attachLatency = result.getString("attachLatency");
		if (attachLatency !=null && !StringUtils.isBlank(attachLatency)) {
			wrapper.setAttachLatency(attachLatency);
		}
		String detachTime = result.getString("DetachTime");
		if (detachTime !=null && !StringUtils.isBlank(detachTime)) {
			wrapper.setDetachTime(detachTime);
		}
		String csfbMoCallSetupTime = result.getString("csfbMoCallSetupTime");
		if (csfbMoCallSetupTime != null && !StringUtils.isBlank(csfbMoCallSetupTime)) {
			wrapper.setCsfbMoCallSetupTime(csfbMoCallSetupTime);

		}
		String csfbMtCallSetupTime = result.getString("csfbMtCallSetupTime");
		if (csfbMtCallSetupTime != null && !StringUtils.isBlank(csfbMtCallSetupTime)) {
			wrapper.setCsfbMtCallSetupTime(csfbMtCallSetupTime);

		}
		String mtCallConnectionSetupTime = result.getString("mtCallConnectionSetupTime");
		if (mtCallConnectionSetupTime != null &&! StringUtils.isBlank(mtCallConnectionSetupTime)) {
			wrapper.setMtCallConnectionSetupTime(mtCallConnectionSetupTime);
		}
		String moCallConnectionSetupTime = result.getString("moCallConnectionSetupTime");
		if (moCallConnectionSetupTime !=null &&! StringUtils.isBlank(moCallConnectionSetupTime)) {
				wrapper.setMoCallConnectionSetupTime(moCallConnectionSetupTime);			
		}
		String rrcConnectionSetupTime = result.getString("rrcConnectionSetupTime");
		if (rrcConnectionSetupTime != null &&! StringUtils.isBlank(rrcConnectionSetupTime)) {
			wrapper.setRrcConnectionSetupTime(rrcConnectionSetupTime);
		}
		String channelRelease = result.getString("channelRelease");
		if (channelRelease !=null && !StringUtils.isBlank(channelRelease)) {
			wrapper.setChannelRelease(channelRelease);
		}
		String timeStamp = result.getString("timeStamp");
		if (timeStamp != null && !StringUtils.isBlank(timeStamp)) {
			wrapper.setTimeStamp(NumberUtils.toLong(timeStamp));
		}
		if(wrapper.getTimeStamp()!=null) {
		list.add(wrapper);	
		}
	}
	
	public Comparator<MessageDetailWrapper> getTimestampComparator() {
		return new Comparator<MessageDetailWrapper>() {
			@Override
			public int compare(MessageDetailWrapper o1, MessageDetailWrapper o2) {
				if (o1.getTimeStamp() != null && o2.getTimeStamp() != null) {
					Long o1Timestamp = o1.getTimeStamp();
					Long o2Timestamp = o1.getTimeStamp();
					return o1Timestamp.compareTo(o2Timestamp);
				}
				return -1;
			}
		};
	}
	

	@Override
	public Map<String, Double> getPeakValueData(Integer workorderId, Map<String, List<String>> map) {
		try {
			logger.info("WORKORDER_ID {} , RECIPE LIST {} , OPERATOR LIST {} ", workorderId,
					map.get(QMDLConstant.RECIPE), map.get(QMDLConstant.OPERATOR));
			return nvLayer3DashboardService.getSummaryPeakValueReceipeWise(workorderId, map.get(QMDLConstant.RECIPE),
					map.get(QMDLConstant.OPERATOR));
		} catch (Exception e) {
			logger.error("Error inside the method getPeakValueData {}", Utils.getStackTrace(e));
		}
		return null;
	}
}
