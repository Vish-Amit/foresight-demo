package com.inn.foresight.module.nv.report.utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.ws.rs.ForbiddenException;

import com.inn.commons.http.HttpUnsecureGetRequest;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math.DoubleUtils;
import org.apache.commons.math3.util.Precision;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.lang.NumberUtils;
import com.inn.commons.maps.Corner;
import com.inn.commons.maps.LatLng;
import com.inn.commons.maps.ZoomFinder;
import com.inn.commons.maps.geometry.PointUtils;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.report.RangeSlab;
import com.inn.foresight.module.nv.report.optimizedImage.ColorConstants;
import com.inn.foresight.module.nv.report.optimizedImage.GoogleMaps;
import com.inn.foresight.module.nv.report.optimizedImage.ImageCreator;
import com.inn.foresight.module.nv.report.parse.wrapper.Geography;
import com.inn.foresight.module.nv.report.parse.wrapper.GeographyParser;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveImageWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.KPIWrapper;
import com.inn.foresight.module.nv.report.workorder.wrapper.PCIWrapper;
import com.inn.foresight.module.nv.report.wrapper.BRTIExcelReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.CallEventStatistics;
import com.inn.foresight.module.nv.report.wrapper.GraphDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.GraphWrapper;
import com.inn.foresight.module.nv.report.wrapper.KPIStatsWrapper;
import com.inn.foresight.module.nv.report.wrapper.KPISummaryDataWrapper;
import com.inn.foresight.module.nv.report.wrapper.NVReportConfigurationWrapper;
import com.inn.foresight.module.nv.report.wrapper.RecipeMappingWrapper;
import com.inn.foresight.module.nv.report.wrapper.ReportDataHolder;
import com.inn.foresight.module.nv.report.wrapper.ReportSubWrapper;
import com.inn.foresight.module.nv.report.wrapper.SSVTReportWrapper;
import com.inn.foresight.module.nv.report.wrapper.SiteInformationWrapper;
import com.inn.foresight.module.nv.report.wrapper.benchmark.KpiRankWrapper;
import com.inn.foresight.module.nv.report.wrapper.benchmark.LegendListWrapper;
import com.inn.foresight.module.nv.report.wrapper.benchmark.VoiceStatsWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.LiveDriveVoiceAndSmsWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.QuickTestWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.YoutubeTestWrapper;
import com.inn.foresight.module.nv.report.wrapper.stealth.StealthWOMapDataWrapper;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.product.legends.utils.LegendWrapper;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;

/** The Class ReportUtil. */
public class ReportUtil implements ColorConstants {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(ReportUtil.class);
	private static String errorConvertingStringToDataList = "Exception inside method in converting from string to dataList {}";
	/** The rand. */
	static Random rand = new Random();

	static Integer counter = 0;

	/**
	 * Gets the formatted date.
	 *
	 * @param date the date
	 * @return the formatted date
	 */
	public static String getFormattedDate(Date date, String dateformat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
		return dateFormat.format(date);
	}

	/**
	 * Creates the directory of specified path as an argument.
	 *
	 * @param path the path
	 */
	public static void createDirectory(String path) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {
			logger.error("Error in creating directory path {} ", path);
		}
	}

	/**
	 * Gets the random color.
	 *
	 * @return the random color
	 */
	public static Color getRandomColor() {
		Float r = rand.nextFloat();
		Float g = rand.nextFloat();
		Float b = rand.nextFloat();
		return new Color(r, g, b);
	}

	/**
	 * Gets the zoom level.
	 *
	 * @param mapMinMaxLatLon the map min max lat lon
	 * @param lessByZoom      the less by zoom
	 * @return the zoom level
	 */
	public static int getZoomLevel(Map<String, Double> mapMinMaxLatLon, int lessByZoom) {
		int zoomLevel = INDEX_FOURTEEN;
		try {
			zoomLevel = ZoomFinder.findZoom(mapMinMaxLatLon.get(MAX_LAT), mapMinMaxLatLon.get(MIN_LAT),
					mapMinMaxLatLon.get(MAX_LON), mapMinMaxLatLon.get(MIN_LON));
			zoomLevel = zoomLevel - lessByZoom;
			logger.info("zoomLevel " + zoomLevel);
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}
		return zoomLevel;
	}

	/**
	 * Creates the mapof images.
	 *
	 * @param map          the map
	 * @param list         the list
	 * @param imagecreator the imagecreator
	 * @param path         the path
	 */
	public static void createMapofImages(Map<String, BufferedImage> map, List<KPIWrapper> list,
			ImageCreator imagecreator, String path, boolean isAtoll) {
		try {
			logger.info(" path  {}  " + path + "  imagecreator object  {}    " + imagecreator);
			createDirectory(path);
			for (KPIWrapper wrapper : list) {
				if (wrapper.isValidPlot() || isAtoll) {
					map.put(wrapper.getIndexKPI() + BLANK_STRING, imagecreator.getGooglemaps()[wrapper.getIndexKPI()].getImg());
				}
			}
			map.put(INDEX_ZER0 + BLANK_STRING, imagecreator.getGooglemaps()[INDEX_ZER0].getImg());
			populateMapofTerrainiImages(map, imagecreator);
			populateMapofSatelliteiImages(map, imagecreator);
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		} finally {
			for (KPIWrapper wrapper : list) {
				try {
					imagecreator.getGooglemaps()[wrapper.getIndexKPI()].getImg().flush();
				} catch (Exception e) {
					logger.error("Exception occured during flushing of images ");
				}
			}
		}
	}

	private static void populateMapofTerrainiImages(Map<String, BufferedImage> map, ImageCreator imagecreator) {
		int index = 0;
		for (GoogleMaps terrainMap : imagecreator.getTerrainmaps()) {
			try {
				map.put(TERRRAIN_VIEW + index, terrainMap.getImg());
				index++;
			} catch (Exception e) {
				logger.error("Exception inside method populateMapofSatelliteiImages {} ", e.getMessage());
			}
		}
	}

	private static void populateMapofSatelliteiImages(Map<String, BufferedImage> map, ImageCreator imagecreator) {
		int index = 0;
		for (GoogleMaps satelliteMap : imagecreator.getSatellitemaps()) {
			try {
				map.put(SATELLITE_VIEW + index, satelliteMap.getImg());
				index++;
			} catch (Exception e) {
				logger.error("Exception inside method populateMapofSatelliteiImages {} ", e.getMessage());
			}
		}
	}

	/**
	 * Gets the image rows.
	 *
	 * @param googleTileMin the google tile min
	 * @param googleTileMax the google tile max
	 * @return the image rows
	 */
	public static int getImageRows(int[] googleTileMin, int[] googleTileMax) {
		int rows = 1;
		try {
			rows = googleTileMax[0] - googleTileMin[0] + 1;
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}
		return rows;
	}

	public static boolean isValidPoint(String x, String y) {
		return x != null && y != null && !x.isEmpty() && !y.isEmpty();
	}

	public static BufferedImage sendUnSecureHttpsPostRequest(String url) throws Exception {
		//		HttpResponse response = null;
		//		HttpPost httpPost = new HttpPost(url);
		//		CloseableHttpClient httpclient = getHttpsClient();
		HttpUnsecureGetRequest httpUnsecureGetRequest = new HttpUnsecureGetRequest(url);
		Boolean doEnableProxy = ConfigUtils.getBoolean(NVConfigUtil.ENABLE_GOOGLE_MAPS_PROXY);
		if (doEnableProxy != null && doEnableProxy) {
			String HTTP_PROXY_IP = ConfigUtils.getString(NVConfigUtil.HTTP_PROXY_IP);
			String HTTP_PROXY_PORT = ConfigUtils.getString(NVConfigUtil.HTTP_PROXY_PORT);
			//						org.apache.http.HttpHost proxy = new org.apache.http.HttpHost(HTTP_PROXY_IP,
			//								Integer.parseInt(HTTP_PROXY_PORT));
			httpUnsecureGetRequest.setProxy(HTTP_PROXY_IP, Integer.parseInt(HTTP_PROXY_PORT));
			//						httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

		}
		try {
			//			response = httpclient.execute(httpPost);
			BufferedImage image = httpUnsecureGetRequest.getImage();

			//			byte[] byteArray = httpUnsecureGetRequest.getByteArray();
			Integer statusCode = httpUnsecureGetRequest.getStatusCode();
			//			Header[] responseHeaders = httpUnsecureGetRequest.getResponseHeaders();
			logger.info("Http get request status code: {}", statusCode);
			//			logger.info("Http get request response headers are: {}", responseHeaders);
			//			HttpEntity respons = response.getEntity();
			return image;

			//			response = httpclient.execute(httpPost);
			//			Integer statusCode = response.getStatusLine().getStatusCode();
			//			HttpEntity respons = response.getEntity();
			//			return respons.getContent();
		} catch (Exception e) {
			logger.error("Error while sending Request on: {},  Error: {}", url, e);
			throw e;
		}
	}

	/**
	 * Gets the buffered image.
	 *
	 * @param bytes the bytes
	 * @return the buffered image
	 * @throws Exception the exception
	 */
	public static BufferedImage getBufferedImage(byte[] bytes) {
		try {
			InputStream in = new ByteArrayInputStream(bytes);
			return ImageIO.read(in);
		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
			return null;
		}
	}

	/**
	 * Sets the values in map.
	 *
	 * @param list        the obj
	 * @param pciCountMap the pci count map
	 * @param colorlist
	 */
	public static void populatePciColorMap(List<SiteInformationWrapper> list, Map<Integer, PCIWrapper> pciCountMap,
			List<String> colorlist) {

		try {
			logger.info("Inside method populatePciColorMap");
			if (list != null && !list.isEmpty()) {
				list.forEach(siteWrapper -> {
					if (siteWrapper.getPci() != null) {

						Integer pci = siteWrapper.getPci();
						String colorvalue = colorlist.get(pci);
						Color color = Color.decode(colorvalue);

						PCIWrapper pciwrapperData = new PCIWrapper();
						pciwrapperData.setPCI(pci);
						pciwrapperData.setColor(color);
						pciwrapperData.setCount(0);
						pciwrapperData.setIsSampleData(false);
						/*
						 * pciwrapperData.setCgi(siteWrapper.getCgi());
						 * pciwrapperData.setCgiColor(color);
						 */
						pciCountMap.put(pci, pciwrapperData);
						/*
						 * if(cgiCountMap!=null) { cgiCountMap.put(siteWrapper.getCgi(),
						 * pciwrapperData); }
						 */
					}
				});
			}
			logger.info("Out of pci Loop");
		} catch (Exception e) {
			logger.debug("error in fetching pci in setValuesInMap method {} ", Utils.getStackTrace(e));
		}
	}

	public static HashMap<Integer, PCIWrapper> populatecgiColorMap(List<SiteInformationWrapper> list) {
		HashMap<Integer, PCIWrapper> cgiCountMap = new HashMap<>();
		try {
			if (list != null && !list.isEmpty()) {
				list.forEach(siteWrapper -> {
					if (siteWrapper.getCgi() != null) {
						Integer pci = siteWrapper.getCgi();
						Float r = rand.nextFloat();
						Float g = rand.nextFloat();
						Float b = rand.nextFloat();
						Color color = new Color(r, g, b);
						PCIWrapper pciwrapperData = new PCIWrapper();
						pciwrapperData.setPCI(pci);
						pciwrapperData.setColor(color);
						pciwrapperData.setCount(0);
						pciwrapperData.setIsSampleData(false);
						cgiCountMap.put(pci, pciwrapperData);

					}
				});
			}
		} catch (Exception e) {
			logger.debug("error in fetching pci in setValuesInMap method {} ", Utils.getStackTrace(e));
		}
		return cgiCountMap;
	}

	/**
	 * Convert string to json object.
	 *
	 * @param json the json
	 * @return the JSON object
	 * @throws Exception
	 */
	public static JSONObject convertStringToJsonObject(String json) {
		JSONParser jsonParser = new JSONParser();
		try {
			return (JSONObject) jsonParser.parse(json);
		} catch (ParseException e) {
			logger.error("Error inside the method convertStringToJsonObject for json {} ", Utils.getStackTrace(e));
			throw new BusinessException("Unable to Parse this json " + json);
		} catch (ClassCastException e) {
			logger.error("ClassCastException occured inside method convertStringToJsonObject for json {} ",
					Utils.getStackTrace(e));
			throw new BusinessException("ClassCastException Unable to Parse this json " + json);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * Write string into csv file.
	 *
	 * @param csv      the CSV Data
	 * @param fileName the File Name
	 * @return the File Name
	 * @throws RestException the rest exception
	 * @throws IOException   CSV File created from data
	 */
	public static String writeStringIntoCsvFile(String csv, String fileName) {
		logger.info("Writing Data to file at: {}", fileName);
		try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
			String fileDate = csv;
			fileOut.write(fileDate.getBytes());
			fileOut.flush();
		} catch (Exception ex) {
			logger.error("Error to write Csv {}", Utils.getStackTrace(ex));
			throw new BusinessException(ex.getMessage());
		}
		return fileName;
	}

	/**
	 * Convert CSV string to data list.
	 *
	 * @param data the data
	 * @return the list
	 */
	public static List<String[]> convertCSVStringToDataList(String data) {
		logger.info("inside the method convertCSVStringToDataList");
		List<String[]> arlist = new ArrayList<>();
		try {
			if (data != null) {
				String[] ar = data.split("]\\,\\[", -1);
				List<String> list = Arrays.asList(ar);
				for (String string : list) {
					String[] arr = string.replace("//]", "").replaceAll("\\[", "").replaceAll("]]", Symbol.EMPTY_STRING)
							.replace("]]\"}", "").split(",", -1);
					arlist.add(arr);
				}
			}
		} catch (Exception e) {
			logger.error(errorConvertingStringToDataList, e.getMessage());
		}
		return arlist;
	}
	
	

	/**
	 * Gets the minmax latl on drive list.
	 *
	 * @param dataKPIs the data KP is
	 * @param latIndex the lat index
	 * @param lonIndex the lon index
	 * @return the minmax latl on drive list
	 */
	public static Corner getminmaxLatlOnDriveList(List<String[]> dataKPIs, Integer latIndex, Integer lonIndex) {
		logger.info("Inside method getminmaxLatlOnDriveList with latIndex {} , lonIndex {}  ", latIndex, lonIndex);
		List<List<Double>> listofLatLng = getListOfLatLonfromCsvData(dataKPIs, latIndex, lonIndex);
		Corner corner = null;
		if (listofLatLng != null && !listofLatLng.isEmpty()) {
			corner = getCornerOfBoundary(listofLatLng);
		}
		return corner;
	}

	public static Corner getminmaxLatLongForStealthMap(List<StealthWOMapDataWrapper> stealthMapDataWrapper) {
		List<List<Double>> listofLatLng = getListOfLatLonfromStealthData(stealthMapDataWrapper);
		Corner corner = null;
		if (listofLatLng != null && !listofLatLng.isEmpty()) {
			corner = getCornerOfBoundary(listofLatLng);
		}
		return corner;
	}

	/**
	 * Gets the address by lat lon.
	 *
	 * @param latitute  the latitute
	 * @param longitute the longitute
	 * @return the address by lat lon
	 */
	public static String getAddressByLatLon(Double latitute, Double longitute) {
		String url = "https://maps.google.com/maps/api/geocode/json?latlng=" + latitute + "," + longitute
				+ "&sensor=true&key=" + ConfigUtils.getString(ConfigUtil.GOOGLE_MAP_KEY);
		String address = "";
		try {
			String msg = readData(new URL(url).openStream());
			address = getaddressFromJSONObject(msg);
			logger.info("getLocationAddress -{}" + address);
		} catch (Exception e) {
			logger.info("getLocationAddress getting Error-{}" + Utils.getStackTrace(e));
		}
		return address;
	}

	/**
	 * Convert legends list to kpi wrapper list.
	 *
	 * @param legendList  the legend list
	 * @param kpiIndexMap the kpi index map
	 * @return the list
	 */
		public static List<KPIWrapper> convertLegendsListToKpiWrapperList(List<LegendWrapper> legendList,
			Map<String, Integer> kpiIndexMap) {
		logger.info("Inside the method convertLegendsListToKpiWrapperList {} ",
				legendList != null ? legendList.size() : "legendList is Null");
		List<KPIWrapper> kpiWrapperList = new ArrayList<>();
		try {
			if (legendList != null) {
				Map<String, List<LegendWrapper>> dataMap = legendList.stream()
						.filter(x -> x != null && kpiIndexMap.containsKey(x.getKpiName()))
						.collect(Collectors.groupingBy(LegendWrapper::getKpiName, Collectors.toList()));

				dataMap.forEach((kpiname, list) -> {
					KPIWrapper wrapper = new KPIWrapper();
					wrapper.setKpiName(kpiname);
					Double maxValue = list.stream().filter(x -> x.getMaxValue() != null).max(Comparator.comparingDouble(LegendWrapper::getMaxValue)).get()
							.getMaxValue();
					Double minValue = list.stream().min(Comparator.comparingDouble(LegendWrapper::getMinValue)).get()
							.getMinValue();

					// THIS is index for data)
					if (kpiname.equalsIgnoreCase(IBWifiConstants.KEY_WIFI_DL_2_GHZ)
							|| kpiname.equalsIgnoreCase(IBWifiConstants.KEY_WIFI_DL_5_GHZ)) {
						wrapper.setIndexKPI(kpiIndexMap.get(DL_THROUGHPUT));
					} else if (kpiname.equalsIgnoreCase(IBWifiConstants.KEY_WIFI_UL_2_GHZ)
							|| kpiname.equalsIgnoreCase(IBWifiConstants.KEY_WIFI_UL_5_GHZ)) {
						wrapper.setIndexKPI(kpiIndexMap.get(UL_THROUGHPUT));
					} else {
						wrapper.setIndexKPI(kpiIndexMap.get(kpiname));
					}
					wrapper.setMinValue(minValue);
					if(maxValue!=null) {
					wrapper.setMaxValue(maxValue);
					}
					List<RangeSlab> rangeSlabs = new ArrayList<>();

					list.forEach(ranges -> {
						RangeSlab slab = new RangeSlab();
						slab.setLowerLimit(ranges.getMinValue());
						if(ranges.getMaxValue()!=null) {
						slab.setUpperLimit(ranges.getMaxValue());
					}
						slab.setColorCode(ranges.getColorCode());
						rangeSlabs.add(slab);
					});
					wrapper.setRangeSlabs(rangeSlabs);
					kpiWrapperList.add(wrapper);
				});
			}
		} catch (Exception e) {
			logger.error("Error inside the method convertLegendsListToKpiWrapperList {}", Utils.getStackTrace(e));
		}
		List<KPIWrapper> kpiWrapperList1 = kpiWrapperList.stream()
				.filter(kpiWrapper -> kpiWrapper.getIndexKPI() != null).collect(Collectors.toList());
		return kpiWrapperList1;
	}

	/**
	 * Gets the address from JSON object. in
	 * 
	 * @param addressJson the address json
	 * @return the address from JSON object
	 */
	private static String getaddressFromJSONObject(String addressJson) {
		org.json.JSONObject location;
		String localString = "";

		try {
			org.json.JSONObject jsonObject = new org.json.JSONObject(addressJson);
			location = jsonObject.getJSONArray("results").getJSONObject(1);
			localString = location.getString("formatted_address");
		} catch (org.json.JSONException e) {
			logger.error("Error in getaddressFromJSONObject {} ", Utils.getStackTrace(e));
		}
		return localString;
	}

	/**
	 * Gets the list of lat lonfrom csv data.
	 *
	 * @param dataKPIs the data KP is
	 * @param latIndex the lat index
	 * @param lonIndex the lon index
	 * @return the list of lat lonfrom csv data
	 */
	private static List<List<Double>> getListOfLatLonfromCsvData(List<String[]> dataKPIs, Integer latIndex,
			Integer lonIndex) {
		List<List<Double>> listofLatLng = null;
		if (dataKPIs != null) {
			listofLatLng = new ArrayList<>();
			for (String[] data : dataKPIs) {
				try {
					if (latIndex != null && lonIndex != null && data != null && data.length > latIndex
							&& data.length > lonIndex && data[latIndex] != null && data[lonIndex] != null
							&& StringUtils.isNotBlank(data[latIndex]) && StringUtils.isNotBlank(data[lonIndex])
							&& NVLayer3Utils.getDoubleFromCsv(data, latIndex) != 0.0
							&& NVLayer3Utils.getDoubleFromCsv(data, lonIndex) != 0.0) {
						List<Double> list = PointUtils.toList(new LatLng(NVLayer3Utils.getDoubleFromCsv(data, latIndex),
								NVLayer3Utils.getDoubleFromCsv(data, lonIndex)));
						listofLatLng.add(list);
					} else {
						logger.debug("Sufficient Data not available {} ", Arrays.toString(data));
					}
				} catch (Exception e) {
					logger.error("Error in getListOfLatLonfromCsvData  {} ", Utils.getStackTrace(e));
				}
			}
		}
		return listofLatLng;
	}

	private static List<List<Double>> getListOfLatLonfromStealthData(
			List<StealthWOMapDataWrapper> stealthMapDataWrapper) {
		List<List<Double>> listofLatLng = null;
		if (stealthMapDataWrapper != null && !stealthMapDataWrapper.isEmpty()) {
			listofLatLng = new ArrayList<>();
			for (StealthWOMapDataWrapper data : stealthMapDataWrapper) {
				try {
					if (data != null && data.getLatitude() != null && data.getLongitude() != null
							&& data.getLatitude() != 0.0 && data.getLongitude() != 0.0) {
						List<Double> list = PointUtils.toList(new LatLng(data.getLatitude(), data.getLongitude()));
						listofLatLng.add(list);
					}
				} catch (Exception e) {
					logger.error("Error in getListOfLatLonfromCsvData  {} ", Utils.getStackTrace(e));
				}
			}
		}
		return listofLatLng;
	}

	/**
	 * Gets the minmax latl on site list.
	 *
	 * @param list the list
	 * @return the minmax latl on site list
	 */
	public static Corner getminmaxLatlOnSiteList(List<SiteInformationWrapper> list) {
		if (list != null && !list.isEmpty()) {
			List<List<Double>> listOfLatLon = list.stream()
					.map(wrapper -> PointUtils.toList(new LatLng(wrapper.getLat(), wrapper.getLon())))
					.collect(Collectors.toList());
			return getCornerOfBoundary(listOfLatLon);
		}
		return null;
	}

	/**
	 * Gets the view port from multiple boundaries.
	 *
	 * @param boundaries the boundaries
	 * @return the view port as Corner from multiple boundaries
	 */
	public static Corner getViewPortFromMultipleBoundaries(List<List<List<List<Double>>>> boundaries) {
		Corner cornerB = null;
		if (boundaries != null && !boundaries.isEmpty()) {
			for (List<List<List<Double>>> boundary : boundaries) {
				Corner corner1 = getCornerOf3DBoundary(boundary);
				if (cornerB == null) {
					cornerB = corner1;
				}
				cornerB.reduce(corner1);
			}
		}
		return cornerB;
	}

	/**
	 * Checks if is valid string.
	 *
	 * @param string the string
	 * @return true, if is valid string
	 */
	public static boolean isValidString(String string) {
		final boolean VALID = true;
		if (string == null || BLANK_STRING.equals(string) || "null".equalsIgnoreCase(string)) {
			return !VALID;
		}
		return VALID;
	}

	/**
	 * Gets the view port map from corner object.
	 *
	 * @param corner the corner
	 * @return the view port map from corner object
	 */
	public static Map<String, Double> getviewPortMapFromCornerObject(Corner corner) {
		Map<String, Double> viewportMap = new HashMap<>();
		viewportMap.put(InfraConstants.SOUTHWEST_LATITUDE_KEY, corner.getMinLatitude());
		viewportMap.put(InfraConstants.SOUTHWEST_LONGITUDE_KEY, corner.getMinLongitude());
		viewportMap.put(InfraConstants.NORTHEAST_LATITUDE_KEY, corner.getMaxLatitude());
		viewportMap.put(InfraConstants.NORTHEAST_LONGITUDE_KEY, corner.getMaxLongitude());
		return viewportMap;
	}

	/**
	 * Gets the index data list.
	 *
	 * @param dataArrayList the data array list
	 * @param paramindex    the paramindex
	 * @return the index data list
	 */
	public static List<Double> getIndexDataList(List<String[]> dataArrayList, int paramindex) {
		List<Double> list = new ArrayList<>();
		for (String[] dataArray : dataArrayList) {
			Double value = NumberUtils.toDouble(dataArray[paramindex]);
			list.add(value);
		}
		return list;
	}

	/**
	 * Gets the k pi summary data wrapper for key.
	 *
	 * @param key             the key
	 * @param smmryData       the smmry data
	 * @param filterList      the kpi target value map
	 * @param dlSpeedtestList
	 * @param reportDataHolder 
	 * @return the k pi summary data wrapper for key
	 */
	public static KPISummaryDataWrapper getKPiSummaryDataWrapperForKey(String key, String[] smmryData,
			List<NVReportConfigurationWrapper> filterList, List<Double> dlSpeedtestList,
			ReportDataHolder reportDataHolder) {
		KPISummaryDataWrapper wrapper = new KPISummaryDataWrapper();
		try {
			wrapper.setTestName(key);
			wrapper.setTarget(
					(filterList != null && !filterList.isEmpty()) ? filterList.get(0).getTargetvalue() : null);
			NVReportConfigurationWrapper nvWrapper = (filterList != null && !filterList.isEmpty()) ? filterList.get(0)
					: null;
			if (nvWrapper != null) {
				switch (key) {
				case NETVELOCITY_SPEED_TEST:
					return getSpeedTestData(wrapper, nvWrapper, dlSpeedtestList);
				case RRC_CONNECTION_REQUEST_SUCCESS:
				case RRC_CONNECTION_SUCCESS_RATE:
					return getRRCSuccessRate(smmryData, wrapper, nvWrapper,
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.RRC_INITIATE),
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.RRC_SUCCESS));
				case VOLTE_ERAB_DROP_RATE:
					return getVolteErabDropRate(key, smmryData, wrapper, nvWrapper,
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_INITIATE),
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_DROP));
				case ERAB_DROP_RATE:
					return getErabDropRate1(key, smmryData, wrapper, nvWrapper,
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.INITIAL_ERAB_SUCCESS_COUNT),
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.RRC_DROPPED));
				case HANDOVER_SUCCESS_RATE:
					return getHandoverSuccessRate(smmryData, wrapper, nvWrapper,
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.HANDOVER_SUCCESS),
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.HANDOVER_INITIATE));
				case VOLTE_SETUP_SUCCESS_RATE:
					return getVolteSetupSucessRate1(smmryData, wrapper, nvWrapper,
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_INITIATE),
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_SETUP_SUCCESS));
				case VOLTE_CALL_DROP_RATE:
					return getVolteCallDropRate(smmryData, wrapper, nvWrapper,
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_INITIATE),
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_DROP));
				case AVERAGE_HANDOVER_INTERRUPTION_TIME:
					return getAverageHandoveInteruptionTime(smmryData, wrapper, nvWrapper,
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.HO_INTERRUPTION_TIME));
				case AVERAGE_SINR:
					return getAverageSinr(smmryData, wrapper, nvWrapper,
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.SINR));
				case VOLTE_VOICE_MEAN_OPINION_SOURCE:
					return getVolteVoiceMean(smmryData, wrapper, nvWrapper,
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.MOS_G711));
				case CROSS_FEEDER:
				case OVER_SHOOTING:
					return wrapper;
				default:
					break;
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside method getKPiSummaryDataWrapperForKey for key =={}  , {} ", key,
					Utils.getStackTrace(e));
		}
		return wrapper;
	}

	public static KPISummaryDataWrapper getKPiSummaryDataWrapperForKeyForReport(String key, String[] smmryData,
			List<NVReportConfigurationWrapper> filterList, List<Double> dlSpeedtestList,
			Map<String, Integer> summaryKpiIndexMap) {
		KPISummaryDataWrapper wrapper = new KPISummaryDataWrapper();
		try {
			wrapper.setTestName(key);
			wrapper.setTarget(
					(filterList != null && !filterList.isEmpty()) ? filterList.get(0).getTargetvalue() : null);
			NVReportConfigurationWrapper nvWrapper = (filterList != null && !filterList.isEmpty()) ? filterList.get(0)
					: null;
			switch (key) {
			case NETVELOCITY_SPEED_TEST:
				return getSpeedTestData(wrapper, nvWrapper, dlSpeedtestList);
			case RRC_CONNECTION_REQUEST_SUCCESS:
			case RRC_CONNECTION_SUCCESS_RATE:
				return getRRCSuccessRate(smmryData, wrapper, nvWrapper, summaryKpiIndexMap.get(RRC_INITIATE),
						summaryKpiIndexMap.get(RRC_SUCCESS));
			case VOLTE_ERAB_DROP_RATE:
				return getVolteErabDropRate(key, smmryData, wrapper, nvWrapper,
						summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_INITIATE),
						summaryKpiIndexMap.get(VOLTE_CALL_DROP_RATE));
			case ERAB_DROP_RATE:
				return getErabDropRate1(key, smmryData, wrapper, nvWrapper, summaryKpiIndexMap.get(ERAB_SUCCESS_RATE),
						summaryKpiIndexMap.get(ERAB_DROP_RATE));
			case HANDOVER_SUCCESS_RATE:
				return getHandoverSuccessRate(smmryData, wrapper, nvWrapper,
						summaryKpiIndexMap.get(SUM_UNDERSCORE + HANDOVER_SUCCESS),
						summaryKpiIndexMap.get(SUM_UNDERSCORE + HANDOVER_INITIATE));
			case VOLTE_SETUP_SUCCESS_RATE:
				return getVolteSetupSucessRate1(smmryData, wrapper, nvWrapper,
						summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_INITIATE),
						summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_SUCCESS));
			case VOLTE_CALL_DROP_RATE:
				return getVolteCallDropRate(smmryData, wrapper, nvWrapper,
						summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_INITIATE),
						summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_DROP));
			case AVERAGE_HANDOVER_INTERRUPTION_TIME:
				return getAverageHandoveInteruptionTime(smmryData, wrapper, nvWrapper,
						summaryKpiIndexMap.get(AVG_UNDERSCORE + HO_INTERRUPTION_TIME));
			case AVERAGE_SINR:
				return getAverageSinr(smmryData, wrapper, nvWrapper, summaryKpiIndexMap.get(AVG_UNDERSCORE + SINR));
			case VOLTE_VOICE_MEAN_OPINION_SOURCE:
				return getVolteVoiceMean(smmryData, wrapper, nvWrapper, summaryKpiIndexMap.get(AVG_UNDERSCORE + MOS));
			case CROSS_FEEDER:
			case OVER_SHOOTING:
				return wrapper;
			default:
				break;
			}
		} catch (Exception e) {
			logger.error("Exception inside method getKPiSummaryDataWrapperForKey for key {}  , {} ", key,
					Utils.getStackTrace(e));
		}
		return wrapper;
	}

	public static KPISummaryDataWrapper getKPiSummaryDataWrapperForSVVTAutomationReport(String key, String[] smmryData,
			List<NVReportConfigurationWrapper> filterList, List<Double> dlSpeedtestList,
			Map<String, String[]> recipeWiseSummaryMap, ReportDataHolder reportDataHolder) {
		KPISummaryDataWrapper wrapper = new KPISummaryDataWrapper();
		try {
			wrapper.setTestName(key);
			wrapper.setTarget(
					(filterList != null && !filterList.isEmpty()) ? filterList.get(0).getTargetvalue() : null);
			NVReportConfigurationWrapper nvWrapper = (filterList != null && !filterList.isEmpty()) ? filterList.get(0)
					: null;
			if (nvWrapper != null) {
				switch (key) {
				case NETVELOCITY_SPEED_TEST:
					return getSpeedTestData(wrapper, nvWrapper, dlSpeedtestList);
				case RRC_CONNECTION_REQUEST_SUCCESS:
				case RRC_CONNECTION_SUCCESS_RATE:
					return getRRCSuccessRate(recipeWiseSummaryMap.get(KEY_RRC), wrapper, nvWrapper,
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.RRC_INITIATE),
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.RRC_SUCCESS));
				case VOLTE_ERAB_DROP_RATE:
					return getVolteErabDropRate(key, recipeWiseSummaryMap.get(KEY_VOLTE), wrapper, nvWrapper,
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_INITIATE),
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_DROP));
				case ERAB_DROP_RATE:
					return getErabDropRate1(key, recipeWiseSummaryMap.get(KEY_RRC), wrapper, nvWrapper,
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.INITIAL_ERAB_SUCCESS_COUNT),
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.RRC_DROPPED));
				case HANDOVER_SUCCESS_RATE:
					return getHandoverSuccessRate(recipeWiseSummaryMap.get(FTP_DL), wrapper, nvWrapper,
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.HANDOVER_SUCCESS),
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.HANDOVER_INITIATE));
				case VOLTE_SETUP_SUCCESS_RATE:
					return getVolteSetupSucessRate1(recipeWiseSummaryMap.get(KEY_VOLTE), wrapper, nvWrapper,
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_INITIATE),
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_SETUP_SUCCESS));
				case VOLTE_CALL_DROP_RATE:
					return getVolteCallDropRate(smmryData, wrapper, nvWrapper,
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_INITIATE),
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.CALL_DROP));
				case AVERAGE_HANDOVER_INTERRUPTION_TIME:
					return getAverageHandoveInteruptionTime(smmryData, wrapper, nvWrapper,
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.HO_INTERRUPTION_TIME));
				case AVERAGE_SINR:
					return getAverageSinr(smmryData, wrapper, nvWrapper,
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.SINR));
				case VOLTE_VOICE_MEAN_OPINION_SOURCE:
					return getVolteVoiceMean(smmryData, wrapper, nvWrapper,
							reportDataHolder.getSummaryKpiIndexMap().get(ReportConstants.MOS_G711));
				case CROSS_FEEDER:
				case OVER_SHOOTING:
					return wrapper;
				default:
					break;
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside method getKPiSummaryDataWrapperForKey for key {}  , {} ", key,
					Utils.getStackTrace(e));
		}
		return wrapper;
	}

	private static KPISummaryDataWrapper getVolteVoiceMean(String[] smmryData, KPISummaryDataWrapper wrapper,
			NVReportConfigurationWrapper nvWrapper, Integer avgMosG711) {
		wrapper.setSource(DT_MOBILE);
		wrapper.setTestName("VoLTE Voice Mean Opinion Source");
		wrapper.setItem(INTEGRITY);
		try {

			if (avgMosG711 != null && smmryData.length >= avgMosG711 && StringUtils.isNotBlank(smmryData[avgMosG711])) {
				logger.debug("Inside method getVolteVoiceMean with value {}  ", smmryData[avgMosG711]);
				wrapper.setAchived(smmryData[avgMosG711]);
			}
			if (wrapper.getAchived() == null && wrapper.getAchived().isEmpty()) {
				wrapper.setStatus("-");
			} else {
				wrapper.setStatus(
						Double.parseDouble(wrapper.getAchived()) > Double.parseDouble(wrapper.getTarget()) ? PASS
								: FAIL);
			}
			wrapper.setAchived(wrapper.getAchived() != null ? wrapper.getAchived() : null);
			if (nvWrapper.getTargetvalue() != null) {
				wrapper.setTarget(">" + nvWrapper.getTargetvalue() + PERCENT);
			}

		} catch (Exception e) {
			logger.error("Error Inside ReportUtil getVolteVoiceMean() {}", e.getMessage());
		}

		wrapper.setTarget(nvWrapper.getTargetvalue() != null ? ">" + nvWrapper.getTargetvalue() + PERCENT : "-");
		return wrapper;
	}

	private static KPISummaryDataWrapper getAverageHandoveInteruptionTime(String[] smmryData,
			KPISummaryDataWrapper wrapper, NVReportConfigurationWrapper nvWrapper,
			Integer sAverageHandoverLatencyIndex) {
		wrapper.setSource(DT_MOBILE);
		wrapper.setTestName("Average Handover Interruption Time");
		wrapper.setItem(INTEGRITY);
		try {
			if (sAverageHandoverLatencyIndex != null && smmryData.length >= sAverageHandoverLatencyIndex
					&& StringUtils.isNotBlank(smmryData[sAverageHandoverLatencyIndex])) {
				wrapper.setAchived(smmryData[sAverageHandoverLatencyIndex]);
			}
			if (wrapper.getAchived() == null && wrapper.getAchived().isEmpty()) {
				wrapper.setStatus("-");
			} else {
				wrapper.setStatus(
						Double.parseDouble(wrapper.getAchived()) < Double.parseDouble(wrapper.getTarget()) ? PASS
								: FAIL);
			}
			wrapper.setTarget(nvWrapper.getTargetvalue() + "ms");

			wrapper.setAchived(wrapper.getAchived() + " ms");
		} catch (Exception e) {
			logger.error("Error Inside ReportUtil getAverageHandoveInteruptionTime() {}", e.getMessage());
		}

		wrapper.setTarget(nvWrapper.getTargetvalue() != null ? nvWrapper.getTargetvalue() + "ms" : "-");

		return wrapper;
	}

	private static KPISummaryDataWrapper getAverageSinr(String[] smmryData, KPISummaryDataWrapper wrapper,
			NVReportConfigurationWrapper nvWrapper, Integer sSinrIndex) {
		wrapper.setSource(DT_MOBILE);
		wrapper.setTestName("Average SINR ");
		wrapper.setItem(INTEGRITY);
		try {

			if (sSinrIndex != null && smmryData.length >= sSinrIndex && StringUtils.isNotBlank(smmryData[sSinrIndex])) {
				wrapper.setAchived(smmryData[sSinrIndex]);
			}
			if (wrapper.getAchived() == null && wrapper.getAchived().isEmpty()) {
				wrapper.setStatus("-");
			} else {
				wrapper.setStatus(
						Double.parseDouble(wrapper.getAchived()) > Double.parseDouble(wrapper.getTarget()) ? PASS
								: FAIL);
			}
			wrapper.setAchived(wrapper.getAchived() != null ? wrapper.getAchived() + "dB" : null);
			wrapper.setTarget(">" + nvWrapper.getTargetvalue() + "dB");

		} catch (Exception e) {
			logger.error("Error Inside ReportUtil getAverageSinr() {}", e.getMessage());
		}

		wrapper.setTarget(nvWrapper.getTargetvalue() != null ? ">" + nvWrapper.getTargetvalue() + "dB" : "-");
		return wrapper;
	}

	private static KPISummaryDataWrapper getVolteCallDropRate(String[] smmryData, KPISummaryDataWrapper wrapper,
			NVReportConfigurationWrapper nvWrapper, Integer sCallInitiateIndex, Integer sCallDropIndex) {
		wrapper.setSource(DT_MOBILE);
		wrapper.setTestName("VoLTE Call Drop Rate");
		wrapper.setItem(RETAINABILITY);

		try {
			if (sCallInitiateIndex != null && sCallDropIndex != null && smmryData != null
					&& !smmryData[sCallInitiateIndex].isEmpty() && !smmryData[sCallDropIndex].isEmpty()
					&& NumberUtils.isValidNumber(Double.parseDouble(smmryData[sCallInitiateIndex]))
					&& NumberUtils.isValidNumber(Double.parseDouble(smmryData[sCallDropIndex]))) {
				Double val = (Double.parseDouble(smmryData[sCallDropIndex]) * INDEX_HUNDRED)
						/ Double.parseDouble(smmryData[sCallInitiateIndex]);
				val = parseToFixedDecimalPlace(val, INDEX_TWO);
				wrapper.setAchived(val != null ? val + PERCENT : null);
				wrapper.setStatus((val < Double.parseDouble(wrapper.getTarget())) ? PASS : FAIL);
				if (nvWrapper.getTargetvalue() != null) {
					wrapper.setTarget(nvWrapper.getTargetvalue()!=null?"<" + nvWrapper.getTargetvalue() + PERCENT:Symbol.HYPHEN_STRING);
				}

			}
		} catch (Exception e) {
			wrapper.setTarget(nvWrapper.getTargetvalue()!=null?"<" + nvWrapper.getTargetvalue() + PERCENT:Symbol.HYPHEN_STRING);

			logger.error("Error in setting getVolteCallDropRate{} ", e.getMessage());
		}

		wrapper.setTarget(nvWrapper.getTargetvalue() != null ? ">" + nvWrapper.getTargetvalue() + PERCENT : "-");
		return wrapper;
	}

	private static KPISummaryDataWrapper getSpeedTestData(KPISummaryDataWrapper wrapper,
			NVReportConfigurationWrapper nvReportConfigurationWrapper, List<Double> dlSpeedtestList) {
		logger.info("Inside method setSpeedTestData for key , dlspeedTestList {} ", dlSpeedtestList);
		wrapper.setTestName("Netvelocity DownLink Speed Test");
		wrapper.setSource(DT_MOBILE);
		try {
			// boolean var = false;
			if (dlSpeedtestList != null && !dlSpeedtestList.isEmpty()) {
				for (Double dlvalue : dlSpeedtestList) {
					if (dlvalue != null) {
						dlvalue = parseToFixedDecimalPlace(dlvalue, 2);
						if (wrapper.getAchived() != null && !wrapper.getAchived().isEmpty()) {
							wrapper.setAchived(wrapper.getAchived() + dlvalue + FORWARD_SLASH);
						} else {
							wrapper.setAchived(dlvalue + FORWARD_SLASH);
						}
						if (wrapper.getTarget() != null
								&& org.apache.commons.lang3.math.NumberUtils.isCreatable(wrapper.getTarget())
								&& dlvalue > Double.parseDouble(wrapper.getTarget())) {
							// var = true;
							wrapper.setStatus(PASS);

						} else {
							wrapper.setStatus(FAIL);
						}

					} else {
						wrapper.setStatus(ReportConstants.N_SLASH_A);

					}
				}
			} else {
				wrapper.setStatus(ReportConstants.N_SLASH_A);

			}
		} catch (Exception e) {
			logger.error("Exception inside method getSpeedTestData {} ", Utils.getStackTrace(e));
		}
		if (wrapper.getAchived() != null) {
			wrapper.setAchived(
					wrapper.getAchived().substring(0, wrapper.getAchived().lastIndexOf(FORWARD_SLASH) - 1) + MBPS);
		}
		if (nvReportConfigurationWrapper != null) {
			wrapper.setTarget(nvReportConfigurationWrapper.getTargetvalue() != null
					? ">" + nvReportConfigurationWrapper.getTargetvalue() + " Mbps "
					: "-");
		}
		return wrapper;
	}

	/**
	 * Gets the handover success rate.
	 *
	 * @param smmryData              the smmry data
	 * @param wrapper                the wrapper
	 * @param sHandoverInitiateIndex
	 * @param sHandoverSuccessIndex
	 * @param nvWrapper
	 * @return the handover success rate
	 */
	public static KPISummaryDataWrapper getHandoverSuccessRate(String[] smmryData, KPISummaryDataWrapper wrapper,
			NVReportConfigurationWrapper nvWrapper, Integer sHandoverSuccessIndex, Integer sHandoverInitiateIndex) {
		wrapper.setSource(DT_MOBILE);
		wrapper.setTestName(HANDOVER_KEY);
		wrapper.setItem(MOBILITY);

		try {
			if (smmryData != null && sHandoverSuccessIndex != null && sHandoverInitiateIndex != null
					&& smmryData.length >= sHandoverSuccessIndex && smmryData.length >= sHandoverInitiateIndex
					&& StringUtils.isNotBlank(smmryData[sHandoverInitiateIndex])
					&& StringUtils.isNotBlank(smmryData[sHandoverSuccessIndex])
					&& NumberUtils.isValidNumber(Double.parseDouble(smmryData[sHandoverInitiateIndex]))
					&& NumberUtils.isValidNumber(Double.parseDouble(smmryData[sHandoverSuccessIndex]))) {
				try {
					Double val = (Double.parseDouble(smmryData[sHandoverSuccessIndex]) * INDEX_HUNDRED)
							/ Double.parseDouble(smmryData[sHandoverInitiateIndex]);
					val = parseToFixedDecimalPlace(val, INDEX_TWO);
					wrapper.setAchived(val != null ? val + PERCENT : null);
					logger.info("VALUE of kpi {} , target value {} ", val, wrapper.getTarget());
					if (wrapper.getTarget() != null) {
						wrapper.setStatus((val > Float.parseFloat(wrapper.getTarget())) ? PASS : FAIL);
					}
					wrapper.setSuccess(Integer.parseInt(smmryData[sHandoverSuccessIndex].trim()));
					wrapper.setTotal(Integer.parseInt(smmryData[sHandoverInitiateIndex]));
				} catch (NumberFormatException e) {

					logger.error("Exception inside the method getHandoverSuccessRate{}", Utils.getStackTrace(e));
				}
			}
			if (nvWrapper.getTargetvalue() != null) {
				wrapper.setTarget(nvWrapper.getTargetvalue()!=null?"> " + nvWrapper.getTargetvalue() + PERCENT:Symbol.HYPHEN_STRING);
			}
		} catch (Exception e) {
			wrapper.setTarget(nvWrapper.getTargetvalue()!=null?">" + nvWrapper.getTargetvalue() + PERCENT:Symbol.HYPHEN_STRING);

			logger.error("Exception inside the method getHandoverSuccessRate{}", Utils.getStackTrace(e));

		}

		return wrapper;
	}

	/**
	 * Gets the RRC success rate.
	 *
	 * @param smmryData        the smmry data
	 * @param wrapper          the wrapper
	 * @param wrapper
	 * @param indexRRCInitiate
	 * @param indexRRCSuccess
	 * @return the RRC success rate
	 */
	public static KPISummaryDataWrapper getRRCSuccessRate(String[] smmryData, KPISummaryDataWrapper wrapper,
			NVReportConfigurationWrapper nvWrapper, Integer indexRRCInitiate, Integer indexRRCSuccess) {
		wrapper.setTestName("RRC Connection Success Rate");

		wrapper.setSource(DT_MOBILE);
		wrapper.setItem(ACCESSIBILITY);
		try {
		if (smmryData!=null &&indexRRCInitiate != null && indexRRCSuccess != null && smmryData.length >= indexRRCInitiate
				&& smmryData.length >= indexRRCSuccess && StringUtils.isNotBlank(smmryData[indexRRCInitiate])
				&& StringUtils.isNotBlank(smmryData[indexRRCSuccess])
				&& NumberUtils.isValidNumber(Double.parseDouble(smmryData[indexRRCInitiate]))
				&& NumberUtils.isValidNumber(Double.parseDouble(smmryData[indexRRCSuccess]))) {
			Double val = (Double.parseDouble(smmryData[indexRRCSuccess]) * INDEX_HUNDRED)
					/ Double.parseDouble(smmryData[indexRRCInitiate]);
			val = parseToFixedDecimalPlace(val, INDEX_TWO);
			wrapper.setAchived(val != null ? val + PERCENT : null);
			if (wrapper.getTarget() != null) {
				wrapper.setStatus((val > Double.parseDouble(wrapper.getTarget())) ? PASS : FAIL);
			}
		}
		if (nvWrapper.getTargetvalue() != null) {
			wrapper.setTarget(nvWrapper.getTargetvalue() != null ? ">" + nvWrapper.getTargetvalue() + PERCENT : "-");
		}}
		catch(Exception e ) {
			wrapper.setTarget(nvWrapper.getTargetvalue() != null ? ">" + nvWrapper.getTargetvalue() + PERCENT : "-");

			logger.error("Exception inside the method getRRCSuccessRate {}",Utils.getStackTrace(e));
		}
		
		return wrapper;
	}

	/**
	 * Parses the Double value to fixed decimal place.
	 *
	 * @param value    the value
	 * @param fixPoint ,No of digits after decimal
	 * @return the parsed value
	 */
	public static Double parseToFixedDecimalPlace(Double value, Integer fixPoint) {
		if (value != null && NumberUtils.isValidNumber(value)) {
			if (fixPoint != null) {
				StringBuilder sb = new StringBuilder(HASH_DOT);
				for (Integer i = 0; i < fixPoint; i++) {
					sb.append(HASH);
				}
				return Double.parseDouble(new DecimalFormat(sb.toString()).format(value));
			} else {
				return Double.parseDouble(new DecimalFormat("##.##").format(value));
			}
		} else {
			return value;
		}
	}

	/**
	 * Checks if is valid K pi plot.
	 *
	 * @param value      the value
	 * @param kpiwrapper the kpiwrapper
	 * @return true, if is valid K pi plot
	 */
	public static boolean isValidKPiPlot(Object value, KPIWrapper kpiwrapper) {
		if ((value instanceof Double && NumberUtils.isValidNumber((Double) value)) || value instanceof Integer) {
			if (!kpiwrapper.isValidPlot()) {
				kpiwrapper.setValidPlot(true);
			}
			return true;
		}
		return false;
	}

	/**
	 * Gets the hbase column name by kpi name.
	 *
	 * @return the hbase column name by kpi name
	 */
	public static String getHbaseColumnNameByKpiName(KPIWrapper kpiwrapper) {
		switch (kpiwrapper.getKpiName()) {
		case ForesightConstants.RSRP:
		case FTP_DL_RSRP:
		case FTP_UL_RSRP:
		case HTTP_DL_RSRP:
		case HTTP_UL_RSRP:
		case IDLE_PLOT:
		case ROUTE:
			return RSRP.toLowerCase();
		case ForesightConstants.SINR:
		case FTP_DL_SINR:
		case FTP_UL_SINR:
		case HTTP_DL_SINR:
		case HTTP_UL_SINR:
			return SINR.toLowerCase();
		case UL:
		case FTP_UL_THROUGHPUT:
		case HTTP_UL_THROUGHPUT:
			return "ultpt";
		case FTP_DL_THROUGHPUT:
		case HTTP_DL_THROUGHPUT:
		case DL:
			return "dltpt";
		case CQI:
			return CQI.toLowerCase();
		case ForesightConstants.PCI:
			return ForesightConstants.PCI;
		case MCS:
			return MCS.toLowerCase();
		case MIMO:
			return "rnkIndx";
		case RSRQ:
			return RSRQ.toLowerCase();
		case RSSI:
			return RSSI.toLowerCase();
		// below three need to be updated when it stats calcualtion will start
		case JITTER:
			return "jitter";
		case LATENCY:
		case CALL_PLOT:
		case WEB_DOWNLOAD_DELAY:
			return "ltncy"; /// temporary of No use , stats needed, should not be empty
		case MOS:
			return QMDLConstant.INSTANTANEOUS_MOS.toLowerCase();
		case CA:
			return CA.toLowerCase();
		case SERVING_SYSTEM:
		case "TECHNOLOGY":
			return "rsrp";
		case PDSCH_THROUGHPUT:
			return "pdschthroughput";
		default:
			return kpiwrapper.getKpiName();
		}
	}
	
	public static String getHbaseColumnNameByKpiNameForReport(KPIWrapper kpiwrapper) {
		switch (kpiwrapper.getKpiName()) {
		case ForesightConstants.RSRP:
		case FTP_DL_RSRP:
		case FTP_UL_RSRP:
		case HTTP_DL_RSRP:
		case HTTP_UL_RSRP:
		case IDLE_PLOT:
		case ROUTE:
			return RSRP.toLowerCase();
		case ForesightConstants.SINR:
		case FTP_DL_SINR:
		case FTP_UL_SINR:
		case HTTP_DL_SINR:
		case HTTP_UL_SINR:
			return SINR.toLowerCase();
		case UL:
		case FTP_UL_THROUGHPUT:
		case HTTP_UL_THROUGHPUT:
			return "ultpt";
		case FTP_DL_THROUGHPUT:
		case HTTP_DL_THROUGHPUT:
		case DL:
			return "dltpt";
		case CQI:
			return CQI.toLowerCase();
		case ForesightConstants.PCI:
			return ForesightConstants.PCI;
		case MCS:
			return MCS.toLowerCase();
		case MIMO:
			return "rnkIndx";
		case RSRQ:
			return RSRQ.toLowerCase();
		case RSSI:
			return RSSI.toLowerCase();
		// below three need to be updated when it stats calcualtion will start
		case JITTER:
			return "jitter";
		case LATENCY:
		case CALL_PLOT:
		case WEB_DOWNLOAD_DELAY:
			return "ltncy"; /// temporary of No use , stats needed, should not be empty
		case MOS:
			return MOS;
		case CA:
			return CA.toLowerCase();
		case SERVING_SYSTEM:
		case "TECHNOLOGY":
			return "rsrp";
		case PDSCH_THROUGHPUT:
			return "pdschthroughput";
		default:
			return kpiwrapper.getKpiName();
		}
	}

	/**
	 * Method to return the file object, if file exist at given path.
	 *
	 * @param filePath the file path
	 * @return the if file exists
	 */
	public static File getIfFileExists(String filePath) {
		try {
			if (isValidString(filePath)) {
				File file = new File(filePath);
				if (file.exists() && !file.isDirectory()) {
					return file;
				}
			}
		} catch (Exception e) {
			logger.error("error on checkIfFileExists[{}], error={}  ", filePath, Utils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Gets the file name.
	 *
	 * @param workOrderName the work order name
	 * @param workOrderId   the work order id
	 * @return the file name
	 */
	public static String getFileName(String workOrderName, Integer workOrderId) {
		String destFileName = ConfigUtils.getString(SSVT_REPORT_PATH);
		File destDir = new File(destFileName);
		if (!destDir.isDirectory()) {
			destDir.mkdirs();
		}
		
		if(destFileName.contains(Symbol.PLUS_STRING)) {
			destFileName=destFileName.replace(Symbol.PLUS_STRING, Symbol.HYPHEN_STRING);
		}
		

		destFileName += workOrderName + UNDERSCORE + workOrderId + PDF_EXTENSION;
		return destFileName.replace(SPACE, UNDERSCORE);
	}

	/**
	 * Parse Date to String .
	 *
	 * @param pattern the pattern
	 * @param date    the date
	 * @return the string
	 */
	public static String parseDateToString(String pattern, Date date) {
		DateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.format(date);
		} catch (Exception e) {
			return format.format(new Date());
		}
	}

	/**
	 * Read data.
	 *
	 * @param rd the rd
	 * @return the string
	 */
	@SuppressWarnings("deprecation")
	public static String readData(InputStream rd) {
		try {
			return IOUtils.toString(rd, Charset.defaultCharset());
		} catch (IOException e) {
			logger.error("error in reading data: " + Utils.getStackTrace(e));
		} finally {
			IOUtils.closeQuietly(rd);
		}
		return "";
	}

	public static Map<String, Double> getViewPortMap(Corner corner) {
		Map<String, Double> viewportMap = new HashMap<>();
		if (corner != null) {
			viewportMap.put(InfraConstants.SW_LATITUDE, corner.getMinLatitude());
			viewportMap.put(InfraConstants.SW_LONGITUDE, corner.getMinLongitude());
			viewportMap.put(InfraConstants.NW_LAT, corner.getMaxLatitude());
			viewportMap.put(InfraConstants.NW_LONG, corner.getMaxLongitude());
		}
		return viewportMap;
	}

	public static Map<String, String> convertCSVStringToMap(String combineData) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		try {
			map = mapper.readValue(combineData, new TypeReference<Map<String, String>>() {
			});
		} catch (Exception e) {
			logger.error("Can't convert string json to map due to {} ", e.getMessage());
		}
		return map;
	}

	public static Map<String, String> convertCSVStringDataToMap(String combineData) {
		Map<String, String> reponseMap = new HashMap<>();
		try {
			reponseMap = new Gson().fromJson(combineData, new TypeToken<Map<String, String>>() {
			}.getType());
		} catch (Exception e) {
			logger.error("Can't convert string json to map due to {} ", Utils.getStackTrace(e));
		}
		return reponseMap;
	}

	public static List<String[]> convertCSVStringListToDataListStationary(List<String> dataList) {
		List<String[]> arrayList = new ArrayList<>();
		for (String data : dataList) {
			arrayList.add(convertCSVStringToDataListStationary(data));
		}
		return arrayList;
	}

	public static String[] convertCSVStringToDataListStationary(String data) {
		logger.info("insde the method convertCSVStringToDataListLiveDrive");
		String[] array = null;
		try {
			if (data != null) {
				data = data.replace("[", "");
				data = data.replace("]", "");
				String[] ar = data.split("]\\,\\[", -1);

				List<String> list = Arrays.asList(ar);
				for (String string : list) {
					array = string.replace(" ", "").split(COMMA, -INDEX_ONE);
				}
			}
		} catch (Exception e) {
			logger.error(errorConvertingStringToDataList, Utils.getStackTrace(e));
		}
		return array;
	}

	public static Double getPercentage(Integer result, Integer total) {
		Double proportion = null;
		try {
			if (result != null && total != null && total != INDEX_ZER0) {
				proportion = (result * 1.0) / total;
				return NVLayer3Utils.roundOffDouble(QMDLConstant.DOUBLEVALUE_ROUND, proportion * INDEX_HUNDRED);
			}
		} catch (Exception e) {
			logger.error("Error In getting Percentage {} ", Utils.getStackTrace(e));
		}
		return proportion;
	}

	public static Double getPercentage(Double result, Double total) {
		double proportion = 0.0;
		try {
			if (total != INDEX_ZER0) {
				proportion = (result * 1.0) / total;
			}

			return NVLayer3Utils.roundOffDouble(QMDLConstant.DOUBLEVALUE_ROUND, proportion * INDEX_HUNDRED);
		} catch (Exception e) {
			logger.warn("Exception in getPercentage result: " + result + " and total: " + total + ", EXCEPTION: "
					+ e.getMessage());
			return proportion;
		}
	}

	public static Double getAverage(Double total, Integer count) {
		Double result = null;
		if (total != null && count != null) {
			result = total / count;
		}
		return result;
	}

	public static List<KPIStatsWrapper> getVoiceKpiWrappersList(String[] summaryCSV,
			Map<String, Integer> summaryKpiIndexMap) {
		List<KPIStatsWrapper> kpiListWrapper = new ArrayList<>();
		try {
			getRSRPData(summaryCSV, kpiListWrapper, summaryKpiIndexMap);
			getSINRData(summaryCSV, kpiListWrapper, summaryKpiIndexMap);
			getRSRQData(summaryCSV, kpiListWrapper, summaryKpiIndexMap);
			getRSSIData(summaryCSV, kpiListWrapper, summaryKpiIndexMap);
		} catch (Exception e) {
			logger.error("Exception in getKpiWrappersList : ", Utils.getStackTrace(e));
		}

		return kpiListWrapper;
	}

	public static List<KPIStatsWrapper> getDataKpiWrappersList(String[] summaryCSV,
			Map<String, Integer> summaryKpiIndexMap) {
		List<KPIStatsWrapper> kpiListWrapper = new ArrayList<>();
		try {
			getJitterData(summaryCSV, kpiListWrapper, summaryKpiIndexMap);
			getLatencyData(summaryCSV, kpiListWrapper, summaryKpiIndexMap);
			getResponseTimeData(summaryCSV, kpiListWrapper, summaryKpiIndexMap);
			getConnectionSetupTime(summaryCSV, kpiListWrapper, summaryKpiIndexMap);
			getDLFTPData(summaryCSV, kpiListWrapper, summaryKpiIndexMap);
			getDLHTTPData(summaryCSV, kpiListWrapper, summaryKpiIndexMap);
			getULFTPData(summaryCSV, kpiListWrapper, summaryKpiIndexMap);
			getULHTTPData(summaryCSV, kpiListWrapper, summaryKpiIndexMap);
		} catch (Exception e) {
			logger.error("Exception in getKpiWrappersList : ", Utils.getStackTrace(e));
		}

		return kpiListWrapper;
	}

	private static void getConnectionSetupTime(String[] summaryCSV, List<KPIStatsWrapper> kpiListWrapper, Map<String, Integer> summaryKpiIndexMap) {
				kpiListWrapper.add(getDataWrapper(summaryCSV, CONNECTION_SETUP_TIME + MS,
						summaryKpiIndexMap.get(CONNECTION_SETUP_TIME), summaryKpiIndexMap.get(MIN_UNDERSCORE + CONNECTION_SETUP_TIME),
						summaryKpiIndexMap.get(MAX_UNDERSCORE + CONNECTION_SETUP_TIME), CONNECTION_SETUP_TIME));
	}

	private static void getDLFTPData(String[] summaryCSV, List<KPIStatsWrapper> kpiListWrapper,
			Map<String, Integer> summaryKpiIndexMap) {
		kpiListWrapper.add(getDataWrapper(summaryCSV, DL_FTP, summaryKpiIndexMap.get(DL_FTP),
				summaryKpiIndexMap.get(MIN_UNDERSCORE + DL_FTP), summaryKpiIndexMap.get(MAX_UNDERSCORE + DL_FTP),
				DL_FTP));
	}

	private static void getULFTPData(String[] summaryCSV, List<KPIStatsWrapper> kpiListWrapper,
			Map<String, Integer> summaryKpiIndexMap) {
		kpiListWrapper.add(getDataWrapper(summaryCSV, UL_FTP, summaryKpiIndexMap.get(UL_FTP),
				summaryKpiIndexMap.get(MIN_UNDERSCORE + UL_FTP), summaryKpiIndexMap.get(MAX_UNDERSCORE + UL_FTP),
				UL_FTP));
	}

	private static void getULHTTPData(String[] summaryCSV, List<KPIStatsWrapper> kpiListWrapper,
			Map<String, Integer> summaryKpiIndexMap) {
		kpiListWrapper.add(getDataWrapper(summaryCSV, UL_HTTP, summaryKpiIndexMap.get(UL_HTTP),
				summaryKpiIndexMap.get(MIN_UNDERSCORE + UL_HTTP), summaryKpiIndexMap.get(MAX_UNDERSCORE + UL_HTTP),
				UL_HTTP));
	}

	private static void getDLHTTPData(String[] summaryCSV, List<KPIStatsWrapper> kpiListWrapper,
			Map<String, Integer> summaryKpiIndexMap) {
		kpiListWrapper.add(getDataWrapper(summaryCSV, DL_HTTP, summaryKpiIndexMap.get(DL_HTTP),
				summaryKpiIndexMap.get(MIN_UNDERSCORE + DL_HTTP), summaryKpiIndexMap.get(MAX_UNDERSCORE + DL_HTTP),
				DL_HTTP));
	}

	private static void getRSRPData(String[] summaryCSV, List<KPIStatsWrapper> kpiListWrapper,
			Map<String, Integer> summaryKpiIndexMap) {
		kpiListWrapper.add(getDataWrapper(summaryCSV, RSRP + DBM_UNIT_REPORT, summaryKpiIndexMap.get(RSRP),
				summaryKpiIndexMap.get(MIN_UNDERSCORE + RSRP), summaryKpiIndexMap.get(MAX_UNDERSCORE + RSRP),
				HEADER_NAME_COVERAGE));
	}

	private static void getSINRData(String[] summaryCSV, List<KPIStatsWrapper> kpiListWrapper,
			Map<String, Integer> summaryKpiIndexMap) {
		kpiListWrapper.add(getDataWrapper(summaryCSV, SINR + DB_UNIT_REPORT, summaryKpiIndexMap.get(SINR),
				summaryKpiIndexMap.get(MIN_UNDERSCORE + SINR), summaryKpiIndexMap.get(MAX_UNDERSCORE + SINR),
				HEADER_NAME_COVERAGE));
	}

	private static void getRSRQData(String[] summaryCSV, List<KPIStatsWrapper> kpiListWrapper,
			Map<String, Integer> summaryKpiIndexMap) {
		kpiListWrapper.add(getDataWrapper(summaryCSV, RSRQ + DB_UNIT_REPORT, summaryKpiIndexMap.get(RSRQ),
				summaryKpiIndexMap.get(MIN_UNDERSCORE + RSRQ), summaryKpiIndexMap.get(MAX_UNDERSCORE + RSRQ),
				HEADER_NAME_COVERAGE));
	}

	private static void getRSSIData(String[] summaryCSV, List<KPIStatsWrapper> kpiListWrapper,
			Map<String, Integer> summaryKpiIndexMap) {
		kpiListWrapper.add(getDataWrapper(summaryCSV, RSSI + DBM_UNIT_REPORT, summaryKpiIndexMap.get(RSSI),
				summaryKpiIndexMap.get(MIN_UNDERSCORE + RSSI), summaryKpiIndexMap.get(MAX_UNDERSCORE + RSSI),
				HEADER_NAME_COVERAGE));
	}

	private static void getJitterData(String[] summaryCSV, List<KPIStatsWrapper> kpiListWrapper,
			Map<String, Integer> summaryKpiIndexMap) {
		kpiListWrapper.add(getDataWrapper(summaryCSV, JITTER + MS, summaryKpiIndexMap.get(JITTER),
				summaryKpiIndexMap.get(MIN_UNDERSCORE + JITTER), summaryKpiIndexMap.get(MAX_UNDERSCORE + JITTER),
				HEADER_NAME_DATA));
	}

	private static void getLatencyData(String[] summaryCSV, List<KPIStatsWrapper> kpiListWrapper,
			Map<String, Integer> summaryKpiIndexMap) {
		kpiListWrapper.add(getDataWrapper(summaryCSV, LATENCY + MS, summaryKpiIndexMap.get(LATENCY),
				summaryKpiIndexMap.get(MIN_UNDERSCORE + LATENCY), summaryKpiIndexMap.get(MAX_UNDERSCORE + LATENCY),
				HEADER_NAME_DATA));
	}

	private static void getResponseTimeData(String[] summaryCSV, List<KPIStatsWrapper> kpiListWrapper,
			Map<String, Integer> summaryKpiIndexMap) {
		kpiListWrapper.add(getDataWrapper(summaryCSV, WEB_DOWNLOAD_DELAY + MS,
				summaryKpiIndexMap.get(WEB_DOWNLOAD_DELAY), summaryKpiIndexMap.get(MIN_UNDERSCORE + WEB_DOWNLOAD_DELAY),
				summaryKpiIndexMap.get(MAX_UNDERSCORE + WEB_DOWNLOAD_DELAY), HEADER_NAME_DATA));
	}

	private static KPIStatsWrapper getDataWrapper(String[] summaryCSV, String kpiName, Integer indexAvg,
			Integer indexMin, Integer indexMax, String tableHeader) {
		KPIStatsWrapper kpiWrapper = new KPIStatsWrapper();
		try {
			kpiWrapper.setKPI(kpiName);
			if (checkIndexValue(indexAvg, summaryCSV)) {
				kpiWrapper.setAvg(round(Double.parseDouble(summaryCSV[indexAvg]), ReportConstants.TWO_DECIMAL_PLACES));
			}
			if (checkIndexValue(indexMin, summaryCSV)) {
				kpiWrapper.setMin(round(Double.parseDouble(summaryCSV[indexMin]), ReportConstants.TWO_DECIMAL_PLACES));
			}
			if (checkIndexValue(indexMax, summaryCSV)) {
				kpiWrapper.setMax(round(Double.parseDouble(summaryCSV[indexMax]), ReportConstants.TWO_DECIMAL_PLACES));
			}
			kpiWrapper.setTableHeader(tableHeader);
		} catch (Exception e) {
			logger.error("Exception in getDataWrapper : ", Utils.getStackTrace(e));
		}
		return kpiWrapper;
	}

	public static boolean checkIndexValue(Integer index, String[] summaryCSV) {
		boolean isValid = false;
		try {
			if (summaryCSV != null && index != null && summaryCSV.length > index && summaryCSV[index] != null
					&& !summaryCSV[index].isEmpty()
					&& NumberUtils.isValidNumber(Double.parseDouble(summaryCSV[index]))) {
				return !isValid;
			}
		} catch (Exception e) {
			logger.error("Exception in : checkIndexValue :{} ,  {}", summaryCSV[index], e.getMessage());
			return false;
		}
		return isValid;
	}

	public static boolean checkValidString(Integer index, String[] summaryCSV) {
		boolean isValid = false;
		try {
			if (summaryCSV[index] != null && !summaryCSV[index].isEmpty() && summaryCSV.length >= index) {
				return !isValid;
			}
		} catch (Exception e) {
			logger.error("Exception in : checkIndexValue : {}", e.getMessage());
		}
		return isValid;
	}

	public static List<String[]> filterDataByTestType(List<String[]> dataList,Integer testTypeIndex, String... testType) {
		List<String[]> newList = new ArrayList<>();
		for (String[] srArray : dataList) {
			if (checkValidString(testTypeIndex, srArray)) {
				for (String tyep : testType) {
					if (tyep.equalsIgnoreCase(srArray[testTypeIndex])) {
						newList.add(srArray);
					}
				}
			}
		}
		return newList;
	}

	public static boolean isValidDouble(Double value) {
		try {
			return value != null && !Double.isNaN(value);
		} catch (Exception e) {
			logger.error("getting exception on validate double = {}", value);
			return false;
		}
	}

	public static List<LiveDriveVoiceAndSmsWrapper> smsDataList(String[] summaryArray) {
		List<LiveDriveVoiceAndSmsWrapper> smsList = new ArrayList<>();
		try {
			if (checkIndexValue(DriveHeaderConstants.SUMMARY_SMS_ATTEMPT, summaryArray)) {
				// total Sms
				smsList.add(getSmsAttempts(summaryArray, DriveHeaderConstants.SUMMARY_SMS_ATTEMPT));
				smsList.add(getSmsDeliveredAvgPerSecond(summaryArray,
						DriveHeaderConstants.SUMMARY_SMS_DELIVEREY_AVG_TIME, DriveHeaderConstants.SUMMARY_SMS_ATTEMPT));
				smsList.add(getSmsDeliveredLessThan3Min(summaryArray,
						DriveHeaderConstants.SUMMARY_SMS_DELIVERED_IN_LESS_THAN_3_MIN));
				smsList.add(getSmsDeliveredPercentageLessThan3Min(summaryArray,
						DriveHeaderConstants.SUMMARY_SMS_SUCCESS, DriveHeaderConstants.SUMMARY_SMS_ATTEMPT));
				// On Net
				smsList.add(getSmsAttempts(summaryArray, DriveHeaderConstants.SUMMARY_SMS_ATTEMPT_ON_NET));
				smsList.add(
						getSmsDeliveredAvgPerSecond(summaryArray, DriveHeaderConstants.SUMMARY_SMS_DELIVERED_SUM_ON_NET,
								DriveHeaderConstants.SUMMARY_SMS_ATTEMPT_ON_NET));
				smsList.add(getSmsDeliveredLessThan3Min(summaryArray,
						DriveHeaderConstants.SUMMARY_SMS_DELIVERED_LESS_THAN_3_MIN_ON_NET));
				smsList.add(getSmsDeliveredPercentageLessThan3Min(summaryArray,
						DriveHeaderConstants.SUMMARY_SMS_DELIVERED_LESS_THAN_3_MIN_ON_NET,
						DriveHeaderConstants.SUMMARY_SMS_ATTEMPT_ON_NET));
				// off net
				smsList.add(getSmsAttempts(summaryArray, DriveHeaderConstants.SUMMARY_SMS_ATTEMPT_OFF_NET));
				smsList.add(getSmsDeliveredAvgPerSecond(summaryArray,
						DriveHeaderConstants.SUMMARY_SMS_DELIVERED_SUM_OFF_NET,
						DriveHeaderConstants.SUMMARY_SMS_ATTEMPT_OFF_NET));
				smsList.add(getSmsDeliveredLessThan3Min(summaryArray,
						DriveHeaderConstants.SUMMARY_SMS_DELIVERED_LESS_THAN_3_MIN_OFF_NET));
				smsList.add(getSmsDeliveredPercentageLessThan3Min(summaryArray,
						DriveHeaderConstants.SUMMARY_SMS_DELIVERED_LESS_THAN_3_MIN_OFF_NET,
						DriveHeaderConstants.SUMMARY_SMS_ATTEMPT_OFF_NET));
			}
		} catch (Exception e) {
			logger.error("Exception inside method SmsDataList {} ", e.getMessage());
		}
		logger.info("Going to return the SmsDataList {} ", smsList);
		return smsList;
	}

	public static List<LiveDriveVoiceAndSmsWrapper> smsDataListForReport(String[] summaryArray,
			Map<String, Integer> summaryKpiIndexMap) {
		List<LiveDriveVoiceAndSmsWrapper> smsList = new ArrayList<>();
		try {
			if (checkIndexValue(summaryKpiIndexMap.get(SMS_ATTEMPT), summaryArray)) {
				String smsAttempt = summaryArray[summaryKpiIndexMap.get(SMS_ATTEMPT)];
				if (org.apache.commons.lang3.math.NumberUtils.isCreatable(smsAttempt)
						&& Integer.parseInt(smsAttempt) > ReportConstants.INDEX_ZER0) {
					// total Sms
					smsList.add(getSmsAttempts(summaryArray, summaryKpiIndexMap.get(SMS_ATTEMPT)));
					smsList.add(getSmsDeliveredAvgPerSecond(summaryArray, summaryKpiIndexMap.get(SMS_DELIVERED),
							summaryKpiIndexMap.get(SMS_ATTEMPT)));
					smsList.add(getSmsDeliveredLessThan3Min(summaryArray,
							summaryKpiIndexMap.get(SMS_DELIVERED_LESS_THAN_3_MIN)));
					smsList.add(getSmsDeliveredPercentageLessThan3Min(summaryArray, summaryKpiIndexMap.get(SMS_SUCCESS),
							summaryKpiIndexMap.get(SMS_ATTEMPT)));
					// On Net
					smsList.add(getSmsAttempts(summaryArray, summaryKpiIndexMap.get(SMS_ATTEMPT_ON_NET)));
					smsList.add(getSmsDeliveredAvgPerSecond(summaryArray,
							summaryKpiIndexMap.get(SUM.toUpperCase() + UNDERSCORE + SMS_DELIVERED_ON_NET),
							summaryKpiIndexMap.get(SMS_ATTEMPT_ON_NET)));
					smsList.add(getSmsDeliveredLessThan3Min(summaryArray,
							summaryKpiIndexMap.get(SMS_DELIVERED_LESS_THAN_3_MIN_ON_NET)));
					smsList.add(getSmsDeliveredPercentageLessThan3Min(summaryArray,
							summaryKpiIndexMap.get(SMS_DELIVERED_LESS_THAN_3_MIN_ON_NET),
							summaryKpiIndexMap.get(SMS_ATTEMPT_ON_NET)));
					// off net
					smsList.add(getSmsAttempts(summaryArray, summaryKpiIndexMap.get(SMS_ATTEMPT_OFF_NET)));
					smsList.add(getSmsDeliveredAvgPerSecond(summaryArray,
							summaryKpiIndexMap.get(SUM.toUpperCase() + UNDERSCORE + SMS_DELIVERED_OFF_NET),
							summaryKpiIndexMap.get(SMS_ATTEMPT_OFF_NET)));
					smsList.add(getSmsDeliveredLessThan3Min(summaryArray,
							summaryKpiIndexMap.get(SMS_DELIVERED_LESS_THAN_3_MIN_OFF_NET)));
					smsList.add(getSmsDeliveredPercentageLessThan3Min(summaryArray,
							summaryKpiIndexMap.get(SMS_DELIVERED_LESS_THAN_3_MIN_OFF_NET),
							summaryKpiIndexMap.get(SMS_ATTEMPT_OFF_NET)));
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside method SmsDataList {} ", e.getMessage());
		}
		logger.debug("Going to return the SmsDataList {} ", smsList);
		return smsList;
	}

	public static List<LiveDriveVoiceAndSmsWrapper> callDataList(String[] summaryArray) {
		List<LiveDriveVoiceAndSmsWrapper> callList = new ArrayList<>();
		try {
			if(checkIndexValue(DriveHeaderConstants.SUMMARY_CALL_ATTEMPTS, summaryArray)){
				// Voice Data
				callList.add(getCallAttempts(summaryArray,DriveHeaderConstants.SUMMARY_CALL_ATTEMPTS));
				callList.add(getCallFailure(summaryArray,DriveHeaderConstants.SUMMARY_CALL_FAILURE));
				callList.add(getCallDropped(summaryArray,DriveHeaderConstants.SUMMARY_CALL_DROP));
				callList.add(getCallDroppedPercetage(summaryArray,DriveHeaderConstants.SUMMARY_CALL_DROP ,DriveHeaderConstants.SUMMARY_CALL_ATTEMPTS));
				callList.add(getCallSuccessPercentage(summaryArray,DriveHeaderConstants.SUMMARY_CALL_SUCCESS ,DriveHeaderConstants.SUMMARY_CALL_ATTEMPTS));
				callList.add(callSetupTime(summaryArray,DriveHeaderConstants.SUMMARY_CALL_SETUP_TIME));
				callList.add(getMosG77Avg(summaryArray));
					//  On Net
				callList.add(getCallAttempts(summaryArray,DriveHeaderConstants.SUMMARY_CALL_ATTEMPT_ON_NET));
				callList.add(getCallFailure(summaryArray,DriveHeaderConstants.SUMMARY_CALL_FAILURE_ON_NET));
				callList.add(getCallDropped(summaryArray,DriveHeaderConstants.SUMMARY_CALL_DROP_ON_NET));
				callList.add(getCallSuccessPercentage(summaryArray,DriveHeaderConstants.SUMMARY_CALL_SUCCESS_ON_NET,DriveHeaderConstants.SUMMARY_CALL_ATTEMPT_ON_NET));
				callList.add(getCallDroppedPercetage(summaryArray,DriveHeaderConstants.SUMMARY_CALL_DROP_ON_NET,DriveHeaderConstants.SUMMARY_CALL_ATTEMPT_ON_NET));
					// off net
				callList.add(getCallAttempts(summaryArray,DriveHeaderConstants.SUMMARY_CALL_ATTEMPT_OFF_NET));
				callList.add(getCallFailure(summaryArray,DriveHeaderConstants.SUMMARY_CALL_FAILURE_OFF_NET));
				callList.add(getCallDropped(summaryArray,DriveHeaderConstants.SUMMARY_CALL_DROP_OFF_NET));
				callList.add(getCallSuccessPercentage(summaryArray,DriveHeaderConstants.SUMMARY_CALL_SUCCESS_OFF_NET,DriveHeaderConstants.SUMMARY_CALL_ATTEMPT_OFF_NET));
				callList.add(getCallDroppedPercetage(summaryArray,DriveHeaderConstants.SUMMARY_CALL_DROP_OFF_NET,DriveHeaderConstants.SUMMARY_CALL_ATTEMPT_OFF_NET));
			}
		} catch (Exception e) {
			logger.error("Exception inside method callDataList {} ", e.getMessage());
		}
		logger.debug("Going to return the callDataList {} ", callList);
		return callList;
	}

	public static List<LiveDriveVoiceAndSmsWrapper> callDataListForReport(String[] summaryArray,
			Map<String, Integer> summaryKpiIndexMap) {
		List<LiveDriveVoiceAndSmsWrapper> callList = new ArrayList<>();
		try {
			if(checkIndexValue(summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_INITIATE), summaryArray)){
				String callinitiate = summaryArray[summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_INITIATE)];
				if(org.apache.commons.lang3.math.NumberUtils.isCreatable(callinitiate) && Integer.parseInt(callinitiate) > ReportConstants.INDEX_ZER0){				
				// Voice Data
				callList.add(getCallAttempts(summaryArray,summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_INITIATE)));
				callList.add(getCallFailure(summaryArray,summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_FAILURE)));
				callList.add(getCallDropped(summaryArray,summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_DROP)));
				callList.add(getCallDroppedPercetage(summaryArray,summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_DROP) ,summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_INITIATE)));
				callList.add(getCallSuccessPercentage(summaryArray,summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_SUCCESS) ,summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_INITIATE)));
				callList.add(callSetupTime(summaryArray,summaryKpiIndexMap.get(AVG_UNDERSCORE+CALL_SETUP_TIME)));
				callList.add(getMosG77AvgForReport(summaryArray, summaryKpiIndexMap));
					//  On Net
				callList.add(getCallAttempts(summaryArray,summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_ATTEMPT_ON_NET)));
				callList.add(getCallFailure(summaryArray,summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_FAILURE_ON_NET)));
				callList.add(getCallDropped(summaryArray,summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_DROP_ON_NET)));
				callList.add(getCallSuccessPercentage(summaryArray,summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_SUCCESS_ON_NET),summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_ATTEMPT_ON_NET)));
				callList.add(getCallDroppedPercetage(summaryArray,summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_DROP_ON_NET),summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_ATTEMPT_ON_NET)));
					// off net
				callList.add(getCallAttempts(summaryArray,summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_ATTEMPT_OFF_NET)));
				callList.add(getCallFailure(summaryArray,summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_FAILURE_OFF_NET)));
				callList.add(getCallDropped(summaryArray,summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_DROP_OFF_NET)));
				callList.add(getCallSuccessPercentage(summaryArray,summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_SUCCESS_OFF_NET),summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_ATTEMPT_OFF_NET)));
				callList.add(getCallDroppedPercetage(summaryArray,summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_DROP_OFF_NET),summaryKpiIndexMap.get(SUM_UNDERSCORE+CALL_ATTEMPT_OFF_NET)));
			}}
		} catch (Exception e) {
			logger.error("Exception inside method callDataList {} ", e.getMessage());
		}
		logger.debug("Going to return the callDataList {} ", callList);
		return callList;
	}

	private static LiveDriveVoiceAndSmsWrapper getMosG77Avg(String[] summaryArray) {
		LiveDriveVoiceAndSmsWrapper wrapper = new LiveDriveVoiceAndSmsWrapper();
		try {
			wrapper.setTest(DriveHeaderConstants.STATIC_TEST_OR_MOBILITY_TEST);
			wrapper.setTestType(DriveHeaderConstants.TOTAL_VOICE);
			wrapper.setTestedKpiName(DriveHeaderConstants.MEAN_OPINION_SCORE);
			wrapper.setTargetValue(DriveHeaderConstants.TARGET_MOS_VALUE);

			if (summaryArray[DriveHeaderConstants.SUMMARY_INDEX_MOS_G711] != null) {
				Double mosValue=NumberUtils.toDouble(summaryArray[DriveHeaderConstants.SUMMARY_INDEX_MOS_G711]);
				if (mosValue >= 3.5) {
					wrapper.setKpiStatus(DriveHeaderConstants.PASS);
				} else {
					wrapper.setKpiStatus(DriveHeaderConstants.FAIL);
				}
				if(!mosValue.isNaN()){
					wrapper.setIsTargetAchived(mosValue.toString());
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside method getMosG77Avg {} ",e.getMessage());
		}
		logger.info("Wrapper Returned for getMosG77Avg {} ",wrapper);
		return wrapper;
	}

	private static LiveDriveVoiceAndSmsWrapper getMosG77AvgForReport(String[] summaryArray, Map<String, Integer> summaryKpiIndexMap) {
		LiveDriveVoiceAndSmsWrapper wrapper = new LiveDriveVoiceAndSmsWrapper();
		try {
			wrapper.setTest(DriveHeaderConstants.STATIC_TEST_OR_MOBILITY_TEST);
			wrapper.setTestType(DriveHeaderConstants.TOTAL_VOICE);
			wrapper.setTestedKpiName(DriveHeaderConstants.MEAN_OPINION_SCORE);
			wrapper.setTargetValue(DriveHeaderConstants.TARGET_MOS_VALUE);

			logger.info("MOS value is : {}", summaryArray[summaryKpiIndexMap.get(MOS)] + "");
			if (summaryKpiIndexMap.containsKey(MOS) && NumberUtils.isParsable(
					summaryArray[summaryKpiIndexMap.get(MOS)])) {
				Double mosValue = NumberUtils.toDouble(summaryArray[summaryKpiIndexMap.get(MOS)]);
				if (NumberUtils.isValidNumber(mosValue)) {
					if (mosValue >= 3.5) {
						wrapper.setKpiStatus(DriveHeaderConstants.PASS);
					} else {
						wrapper.setKpiStatus(DriveHeaderConstants.FAIL);
					}
					if (mosValue != null) {
						Double roundValue =round(mosValue, ReportConstants.TWO_DECIMAL_PLACES);
						if(roundValue!=null) {
						wrapper.setIsTargetAchived(roundValue.toString());
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside method getMosG77AvgForReport {} ",Utils.getStackTrace(e));
		}
		logger.info("Wrapper Returned for getMosG77AvgForReport {} ",wrapper);
		return wrapper;
	}

	private static LiveDriveVoiceAndSmsWrapper getSmsDeliveredPercentageLessThan3Min(String[] summaryArray, int smsSucessIndex, int smsTotalIndex) {
		LiveDriveVoiceAndSmsWrapper wrapper = new LiveDriveVoiceAndSmsWrapper();
		try {
			wrapper.setTest(DriveHeaderConstants.STATIC_TEST_OR_MOBILITY_TEST);
			wrapper.setTestType(DriveHeaderConstants.TOTAL_SMS);
			wrapper.setTestedKpiName(DriveHeaderConstants.SMS_DELIVERED_LESS_THAN_3_MIN_PERCENTAGE);
			wrapper.setTargetValue(DriveHeaderConstants.TARGET_SMS_DELIVERED_PERCENTAGE);
			Double smsDeliveredPercentage = getPercentage(smsSucessIndex, smsTotalIndex, summaryArray);
			if (smsDeliveredPercentage != null) {
				if (summaryArray[smsTotalIndex] != null
						&& !ZERO.equalsIgnoreCase(summaryArray[smsTotalIndex])) {
					if (smsDeliveredPercentage >= NINTY) {
						wrapper.setKpiStatus(DriveHeaderConstants.PASS);
					} else {
						wrapper.setKpiStatus(DriveHeaderConstants.FAIL);
					}
				}
				wrapper.setIsTargetAchived(
						parseToFixedDecimalPlace(smsDeliveredPercentage, 2) + PERCENT);
			}
		} catch (Exception e) {
			logger.info("Excpetion in getSmsDeliveredLessThan3Min : ", e.getMessage());
		}
		return wrapper;
	}

	private static LiveDriveVoiceAndSmsWrapper getSmsDeliveredLessThan3Min(String[] summaryArray, Integer summarySmsDeliveredInLessThan3MinIndex) {
		LiveDriveVoiceAndSmsWrapper wrapper = new LiveDriveVoiceAndSmsWrapper();
		try {
			wrapper.setTest(DriveHeaderConstants.STATIC_TEST_OR_MOBILITY_TEST);
			wrapper.setTestType(DriveHeaderConstants.TOTAL_SMS);
			wrapper.setTestedKpiName(DriveHeaderConstants.SMS_DELIVERED_LESS_THAN_3_MIN);
			wrapper.setTargetValue(null);
			wrapper.setKpiStatus(null);
			if (checkIndexValue(summarySmsDeliveredInLessThan3MinIndex, summaryArray)) {
				wrapper.setIsTargetAchived(summaryArray[summarySmsDeliveredInLessThan3MinIndex]);
			}
		} catch (Exception e) {
			logger.info("Excpetion in getSmsDeliveredLessThan3Min : ", e.getMessage());
		}
		return wrapper;
	}

	private static LiveDriveVoiceAndSmsWrapper getSmsDeliveredAvgPerSecond(String[] summaryArray,
			Integer summarySmsDelivereyAvgTimendex, Integer smsAttemptIndex) {
		LiveDriveVoiceAndSmsWrapper wrapper = new LiveDriveVoiceAndSmsWrapper();
		try {
			wrapper.setTest(DriveHeaderConstants.STATIC_TEST_OR_MOBILITY_TEST);
			wrapper.setTestType(DriveHeaderConstants.TOTAL_SMS);
			wrapper.setTestedKpiName(DriveHeaderConstants.SMS_DELIVERED_AVERAGE_SECOND);
			wrapper.setTargetValue(null);
			wrapper.setKpiStatus(null);
			if (checkIndexValue(summarySmsDelivereyAvgTimendex, summaryArray)
					&& checkIndexValue(smsAttemptIndex, summaryArray)) {
				Double denominator = Double.parseDouble(summaryArray[smsAttemptIndex]);
				Double numerator = Double.parseDouble(summaryArray[summarySmsDelivereyAvgTimendex]);
				if (denominator != 0) {
					Double smsDelivered = numerator / denominator;
					if (NumberUtils.isValidNumber(smsDelivered)) {
						wrapper.setIsTargetAchived(String.valueOf(smsDelivered / THOUSNAND));
					}
				}
			}
		} catch (Exception e) {
			logger.info("Excpetion in getSmsDeliveredAvgPerSecond : ", e.getMessage());
		}
		return wrapper;
	}

	private static LiveDriveVoiceAndSmsWrapper getSmsAttempts(String[] summaryArray, Integer summarySmsAttemptIndex) {
		LiveDriveVoiceAndSmsWrapper wrapper = new LiveDriveVoiceAndSmsWrapper();
		try {
			wrapper.setTest(DriveHeaderConstants.STATIC_TEST_OR_MOBILITY_TEST);
			wrapper.setTestType(DriveHeaderConstants.TOTAL_SMS);
			wrapper.setTestedKpiName(DriveHeaderConstants.SMS_ATTEMPTS);
			wrapper.setTargetValue(null);
			wrapper.setKpiStatus(null);
			if (checkIndexValue(summarySmsAttemptIndex, summaryArray)) {
				wrapper.setIsTargetAchived(summaryArray[summarySmsAttemptIndex]);
			}
		} catch (Exception e) {
			logger.info("Excpetion in getSmsAttempts : ", e.getMessage());
		}
		return wrapper;
	}

	private static LiveDriveVoiceAndSmsWrapper getCallSuccessPercentage(String[] summaryArray, Integer indexCallSuccess,
			Integer indexCallAttempts) {
		LiveDriveVoiceAndSmsWrapper wrapper = new LiveDriveVoiceAndSmsWrapper();
		try {
			wrapper.setTest(DriveHeaderConstants.STATIC_TEST_OR_MOBILITY_TEST);
			wrapper.setTestType(DriveHeaderConstants.TOTAL_VOICE);
			wrapper.setTestedKpiName(DriveHeaderConstants.SUCCESS_CALL_PERCENTAGE);
			wrapper.setTargetValue(DriveHeaderConstants.TARGET_SUCCESS_CALL_PERCENTAGE);
			Double callSuccessPercentage = getPercentage(indexCallSuccess, indexCallAttempts, summaryArray);
			if (callSuccessPercentage != null) {
				if (summaryArray[indexCallAttempts] != null
						&& !ZERO.equalsIgnoreCase(summaryArray[indexCallAttempts])) {
					if (callSuccessPercentage >= NINTY) {
						wrapper.setKpiStatus(DriveHeaderConstants.PASS);
					} else {
						wrapper.setKpiStatus(DriveHeaderConstants.FAIL);
					}
				}
				wrapper.setIsTargetAchived(parseToFixedDecimalPlace(callSuccessPercentage, 2) + PERCENT);
			}
		} catch (Exception e) {
			logger.error("Exception inside method getCallSuccessPercentage {} ", e.getMessage());
		}
		logger.info("Wrapper Returned for getCallSuccessPercentage {} ", wrapper);
		return wrapper;
	}

	private static LiveDriveVoiceAndSmsWrapper callSetupTime(String[] summaryArray, Integer indexNumerator) {
		LiveDriveVoiceAndSmsWrapper wrapper = new LiveDriveVoiceAndSmsWrapper();
		try {
			wrapper.setTest(DriveHeaderConstants.STATIC_TEST_OR_MOBILITY_TEST);
			wrapper.setTestType(DriveHeaderConstants.TOTAL_VOICE);
			wrapper.setTestedKpiName(DriveHeaderConstants.CALL_SETUP_TIME);
			wrapper.setTargetValue(null);
			if(checkIndexValue(indexNumerator, summaryArray)) {
				Double round = round(Double.parseDouble(summaryArray[indexNumerator]), TWO_DECIMAL_PLACES);
				if(round!=null) {
				wrapper.setIsTargetAchived(round.toString());
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside method callSetupTime {} ", e.getMessage());
		}
		logger.info("Wrapper Returned for callSetupTime  {} ", wrapper);
		return wrapper;
	}

	private static LiveDriveVoiceAndSmsWrapper getCallFailure(String[] summaryArray, Integer indexCallFailure) {
		LiveDriveVoiceAndSmsWrapper wrapper = new LiveDriveVoiceAndSmsWrapper();
		try {
			wrapper.setTest(DriveHeaderConstants.STATIC_TEST_OR_MOBILITY_TEST);
			wrapper.setTestType(DriveHeaderConstants.TOTAL_VOICE);
			wrapper.setTestedKpiName(DriveHeaderConstants.FAILED_CALL);
			wrapper.setTargetValue(null);
			if (checkIndexValue(indexCallFailure, summaryArray)) {
				wrapper.setIsTargetAchived(summaryArray[indexCallFailure]);
			}
		} catch (Exception e) {
			logger.error("Exception inside method getCallFailure {} ", e.getMessage());
		}
		logger.info("Wrapper Returned for getCallFailure  {} ", wrapper);
		return wrapper;
	}

	private static LiveDriveVoiceAndSmsWrapper getCallDroppedPercetage(String[] summaryArray, Integer indexCallDropped,
			Integer indexCallAttempts) {
		LiveDriveVoiceAndSmsWrapper wrapper = new LiveDriveVoiceAndSmsWrapper();
		try {
			wrapper.setTest(DriveHeaderConstants.STATIC_TEST_OR_MOBILITY_TEST);
			wrapper.setTestType(DriveHeaderConstants.TOTAL_VOICE);
			wrapper.setTestedKpiName(DriveHeaderConstants.CALL_DROPPED_PERCENTAGE);
			wrapper.setTargetValue(DriveHeaderConstants.TARGET_DROPPED_CALL_PERCENTAGE);
			Double callDroppedPercentage = getPercentage(indexCallDropped, indexCallAttempts, summaryArray);
			if (callDroppedPercentage != null) {
				if (summaryArray[indexCallAttempts] != null
						&& !ZERO.equalsIgnoreCase(summaryArray[indexCallAttempts])) {
					if (callDroppedPercentage <= INDEX_FIVE) {
						wrapper.setKpiStatus(DriveHeaderConstants.PASS);
					} else {
						wrapper.setKpiStatus(DriveHeaderConstants.FAIL);
					}
				}
				wrapper.setIsTargetAchived(parseToFixedDecimalPlace(callDroppedPercentage, 2) + PERCENT);
			}
		} catch (Exception e) {
			logger.error("Exception inside method getCallDroppedPercetage {} ", e.getMessage());
		}
		logger.info("Wrapper Returned for getCallDroppedPercetage  {} ", wrapper);
		return wrapper;
	}

	private static LiveDriveVoiceAndSmsWrapper getCallAttempts(String[] summaryArray, Integer indexCallAttemps) {
		LiveDriveVoiceAndSmsWrapper wrapper = new LiveDriveVoiceAndSmsWrapper();
		try {
			wrapper.setTest(DriveHeaderConstants.STATIC_TEST_OR_MOBILITY_TEST);
			wrapper.setTestType(DriveHeaderConstants.TOTAL_VOICE);
			wrapper.setTestedKpiName(DriveHeaderConstants.CALL_ATTEMPTS);
			wrapper.setTargetValue(null);
			wrapper.setKpiStatus(null);
			if (checkIndexValue(indexCallAttemps, summaryArray)) {
				wrapper.setIsTargetAchived(summaryArray[indexCallAttemps]);
			}
		} catch (Exception e) {
			logger.error("Exception inside method getCallAttempts {} ", Utils.getStackTrace(e));
		}
		logger.info("Wrapper Returned for getCallAttempts  {} ", wrapper);
		return wrapper;
	}

	private static LiveDriveVoiceAndSmsWrapper getCallDropped(String[] summaryArray, Integer indexCallDropped) {
		LiveDriveVoiceAndSmsWrapper wrapper = new LiveDriveVoiceAndSmsWrapper();
		try {
			wrapper.setTest(DriveHeaderConstants.STATIC_TEST_OR_MOBILITY_TEST);
			wrapper.setTestType(DriveHeaderConstants.TOTAL_VOICE);
			wrapper.setTestedKpiName(DriveHeaderConstants.DROPPED_CALL);
			wrapper.setTargetValue(null);
			wrapper.setKpiStatus(null);
			if (checkIndexValue(indexCallDropped, summaryArray)) {
				wrapper.setIsTargetAchived(summaryArray[indexCallDropped]);
			}
		} catch (Exception e) {
			logger.error("Exception inside method getCallDropped {} ", Utils.getStackTrace(e));
		}
		logger.info("Wrapper Returned for getCallDropped  {} ", wrapper);
		return wrapper;
	}

	private static Double getPercentage(Integer indexNumerator, Integer indexDenominator, String[] csv) {
		Double numerator = null;
		Double denomintor = null;
		try {
			if (checkIndexValue(indexNumerator, csv) && checkIndexValue(indexDenominator, csv)) {
				numerator = Double.parseDouble(csv[indexNumerator]);
				denomintor = Double.parseDouble(csv[indexDenominator]);
				logger.info("numerator {} , denomintor {} ", numerator, denomintor);
				if (numerator != 0.0 && denomintor != 0.0) {
					return (numerator / denomintor) * INDEX_HUNDRED;
				} else {
					return 0.0;
				}
			}
		} catch (Exception e) {
			logger.info("Exception in getPercentage : {}/{}", numerator, denomintor);
		}

		return null;
	}

	public static List<YoutubeTestWrapper> getYouTubeTestDataWrapper(String youTubeData) {
		List<YoutubeTestWrapper> listYoutubeData = new ArrayList<>();
		List<String[]> listString = convertCSVStringToDataList(youTubeData);
		if (!listString.isEmpty()) {
			for (String[] csv : listString) {
				if (csv != null && csv.length > 0) {
					YoutubeTestWrapper wrapper = getYoutubeTestWrapper(csv);
					if (wrapper != null) {
						listYoutubeData.add(wrapper);
					}
				}
			}
		}
		return listYoutubeData;
	}

	private static YoutubeTestWrapper getYoutubeTestWrapper(String[] csv) {
		YoutubeTestWrapper wrapper = null;
		try {
			if (checkValidString(DriveHeaderConstants.INDEX_YT_URL, csv)
					&& !csv[DriveHeaderConstants.INDEX_YT_URL].isEmpty()) {
				wrapper = new YoutubeTestWrapper();
				wrapper.setVideoURL(csv[DriveHeaderConstants.INDEX_YT_URL]);
				setVideoData(csv, wrapper);
			} else {
				logger.warn("Url may be null or not a valid url {} ", csv[DriveHeaderConstants.INDEX_YT_URL]);
			}
			return wrapper;
		} catch (Exception e) {
			logger.error("error inside the method getYoutubeTestWrapper {}", e.getMessage());
		}
		return wrapper;
	}

	private static void setVideoData(String[] csv, YoutubeTestWrapper wrapper) {
		if (checkValidString(DriveHeaderConstants.INDEX_YT_RESOLUTION, csv)) {
			if (csv[DriveHeaderConstants.INDEX_YT_RESOLUTION].contains(UNDERSCORE)) {
				wrapper.setVideoResolution(csv[DriveHeaderConstants.INDEX_YT_RESOLUTION].replaceAll(UNDERSCORE, COMMA));
			} else {
				wrapper.setVideoResolution(csv[DriveHeaderConstants.INDEX_YT_RESOLUTION]);
			}
		}
		if (checkIndexValue(DriveHeaderConstants.INDEX_YT_DURATION_IN_SECOND, csv)) {
			wrapper.setVideoDuration(
					Integer.parseInt(csv[DriveHeaderConstants.INDEX_YT_DURATION_IN_SECOND]) / THOUSNAND);
		}
		if (checkIndexValue(DriveHeaderConstants.INDEX_YT_NO_OF_STALLING, csv)) {
			wrapper.setNoOfStalling(NumberUtils.toDouble(csv[DriveHeaderConstants.INDEX_YT_NO_OF_STALLING]).toString());
		}
		if (checkIndexValue(DriveHeaderConstants.INDEX_YT_TOTAL_BUFFERING_TIME, csv)) {
			wrapper.setTotalBufferTime(
					NumberUtils.toDouble(csv[DriveHeaderConstants.INDEX_YT_TOTAL_BUFFERING_TIME]).intValue());
		}
		if (checkIndexValue(DriveHeaderConstants.INDEX_YT_FREEZING_RATIO, csv)) {
			wrapper.setFreezingRatio(csv[DriveHeaderConstants.INDEX_YT_FREEZING_RATIO]);
		}
		if (checkIndexValue(DriveHeaderConstants.INDEX_YT_AVG_RSRP, csv)) {
			wrapper.setAvgRSRP(parseToFixedDecimalPlace(Double.parseDouble(csv[DriveHeaderConstants.INDEX_YT_AVG_RSRP]),
					INDEX_THREE));
		}
		if (checkIndexValue(DriveHeaderConstants.INDEX_YT_AVG_SINR, csv)) {
			wrapper.setAvgSINR(parseToFixedDecimalPlace(Double.parseDouble(csv[DriveHeaderConstants.INDEX_YT_AVG_SINR]),
					INDEX_THREE));
		}
	}

	public static List<QuickTestWrapper> getQuickTestDataWrapper(String quickTestData) {
		List<QuickTestWrapper> listYoutubeData = new ArrayList<>();
		List<String[]> listString = convertCSVStringToDataList(quickTestData);

		int testNo = 0;
		if (!listString.isEmpty()) {
			for (String[] csv : listString) {
				if (csv != null && csv.length > 0) {
					listYoutubeData.add(getSpeedTestWrapper(csv, ++testNo));
				}
			}
		}

		return listYoutubeData;
	}

	private static QuickTestWrapper getSpeedTestWrapper(String[] csv, Integer testNo) {
		QuickTestWrapper wrapper = new QuickTestWrapper();
		wrapper.setTestNo(testNo);
		if (checkIndexValue(DriveHeaderConstants.INDEX_QT_DL_THROUGHPUT, csv)) {
			wrapper.setDlThoughput(
					Precision.round(Double.parseDouble(csv[DriveHeaderConstants.INDEX_QT_DL_THROUGHPUT]), 2));
		}
		if (checkIndexValue(DriveHeaderConstants.INDEX_QT_UL_THROUGHPUT, csv)) {
			wrapper.setUlThoughput(
					Precision.round(Double.parseDouble(csv[DriveHeaderConstants.INDEX_QT_UL_THROUGHPUT]), 2));
		}
		if (checkIndexValue(DriveHeaderConstants.INDEX_QT_LATENCY, csv)) {
			wrapper.setLatency(Double.parseDouble(csv[DriveHeaderConstants.INDEX_QT_LATENCY]));
		}
		if (checkIndexValue(DriveHeaderConstants.INDEX_QT_AVG_RSRP, csv)) {
			wrapper.setAvgRSRP(Double.parseDouble(csv[DriveHeaderConstants.INDEX_QT_AVG_RSRP]));
		}
		if (checkIndexValue(DriveHeaderConstants.INDEX_QT_AVG_SINR, csv)) {
			logger.info("data  sinr {} ", csv[DriveHeaderConstants.INDEX_QT_AVG_SINR]);
			wrapper.setAvgSINR(Double.parseDouble(csv[DriveHeaderConstants.INDEX_QT_AVG_SINR]));
		}

		return wrapper;
	}

	public static List<GraphWrapper> setGraphDataForKpi(KPIWrapper kpiWrapper, List<Double> dataList) {
		logger.info("inside the method setGraphDataForKpi  {} ,totalcount {}", kpiWrapper.getKpiName(),
				kpiWrapper.getTotalCount());
		if(kpiWrapper != null && kpiWrapper.getTotalCount() != null && kpiWrapper.getTotalCount() > ForesightConstants.ZERO) {
			GraphWrapper graphWrapper = new GraphWrapper();
			List<GraphDataWrapper> graphDataList = new ArrayList<>();
			List<GraphWrapper> graphList = new ArrayList<>();
			Statistics staticstic = new Statistics(dataList);
			try {
				graphWrapper.setMax(staticstic.getMax());
				graphWrapper.setMin(staticstic.getMin());
				graphWrapper.setStDev(staticstic.getStdDev());
				graphWrapper.setVariance(staticstic.getVariance());
				graphWrapper.setMean(staticstic.getMean());
				graphWrapper.setKpiName(kpiWrapper.getKpiName());
				graphWrapper.setUnit(getUnitByKPiName(kpiWrapper.getKpiName()));
				getThresholdValue(graphWrapper, dataList, kpiWrapper);
				graphWrapper.setCount(kpiWrapper.getTotalCount());
				graphWrapper.setTargetValue(kpiWrapper.getTargetValue());
				setGraphDataToList(kpiWrapper, graphDataList, kpiWrapper.getRangeSlabs());
				graphDataList.sort(Comparator.comparing(GraphDataWrapper::getFrom).reversed());
				graphWrapper.setGraphDataList(graphDataList);
				graphList.add(graphWrapper);
				return graphList;
			} catch (Exception e) {
				logger.error("Exception inside the method setGraphDataForKpi {}", Utils.getStackTrace(e));
			}
		}
		return Collections.emptyList();
	}

	public static void getThresholdValue(GraphWrapper graphWrapper, List<Double> dataList, KPIWrapper kpiWrapper) {
		Long count = null;
		try {
			if (kpiWrapper.getTargetValue() != null) {
				count = dataList.stream().filter(value -> value > kpiWrapper.getTargetValue()).count();
				if (count != null) {
					graphWrapper.setThreshold(getPercentage(count.intValue(), kpiWrapper.getTotalCount()));
				}
			}
		} catch (Exception e) {
			logger.error(
					"Exception inside method getThresholdValue kpiName {} ,count  {} , kpiWrapper.getTotalCount() {} ",
					kpiWrapper.getKpiName(), count, kpiWrapper.getTotalCount());
		}
	}

	public static List<Double> convetArrayToList(List<String[]> arlist, Integer indexKPI) {
		try {
//			logger.info("convetArrayToList indexKPI===={}", indexKPI);
			List<Double> list = new ArrayList<>();
			if (arlist != null) {
				for (String[] ar : arlist) {
					if (indexKPI != null && ar.length >= indexKPI && ar[indexKPI] != null && !ar[indexKPI].isEmpty()) {
						list.add(Double.parseDouble(ar[indexKPI]));
					}
				}
			}

			return list;
		} catch (Exception e) {
			logger.error("Error in mapping the data from dataList ", e.getMessage());
		}
		return Collections.emptyList();
	}

	public static List<LatLng> convetArrayTolatLonList(List<String[]> arlist, Integer indexKPI) {
		try {
			logger.info("convetArrayToList indexKPI{}", indexKPI);
			List<LatLng> list = new ArrayList<>();
			if (arlist != null) {
				for (String[] ar : arlist) {
					if (ar.length >= indexKPI && ar[indexKPI] != null && !ar[indexKPI].isEmpty()) {
						list.add(new LatLng(Double.parseDouble(ar[indexKPI]), Double.parseDouble(ar[indexKPI + 1])));
					}
				}
			}
			return list;
		} catch (Exception e) {
			logger.error("Error in mapping the data from dataList ", e.getMessage());
		}
		return Collections.emptyList();
	}

	public static List<Double> convetArrayToListForCustomKpi(List<String[]> arlist, Integer indexKPI, String testType, Integer indexTestType) {
		try {
			logger.info("convetArrayToList indexKPI{}", indexKPI);
			List<Double> list = new ArrayList<>();
			for (String[] ar : arlist) {
				if (ar.length >= indexTestType
						&& ar[indexTestType] != null
						&& !ar[indexTestType].isEmpty()
						&& ar[indexTestType].equalsIgnoreCase(testType) && ar.length >= indexKPI
						&& !ar[indexKPI].isEmpty() && ar[indexKPI] != null) {
					list.add(Double.parseDouble(ar[indexKPI]));
				}
			}

			return list;
		} catch (Exception e) {
			logger.error("error is conveting array into list {}", Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	public static List<NVReportConfigurationWrapper> getNVReportconfiguratioinList(List<Object[]> objectList) {
		List<NVReportConfigurationWrapper> list = new ArrayList<>();
		logger.info("inside the method getNVReportconfiguratioinList");
		try {
			for (Object[] obj : objectList) {
				String vendor = obj[INDEX_ZER0] != null ? obj[INDEX_ZER0].toString() : " ";
				String operator = obj[INDEX_ONE] != null ? obj[INDEX_ONE].toString() : " ";
				String kpi = obj[INDEX_TWO] != null ? obj[INDEX_TWO].toString() : " ";

				String configuration = obj[INDEX_THREE] != null ? obj[INDEX_THREE].toString() : " ";
				String targetvalue = obj[INDEX_FOUR] != null ? obj[INDEX_FOUR].toString() : " ";
				list.add(new NVReportConfigurationWrapper(vendor, operator, kpi, configuration, targetvalue));
			}
		} catch (Exception e) {
			logger.error("Error inside the getNVReportconfiguratioinList {}", e.getMessage());
		}
		return list;
	}

	public static void setGraphDataToList(KPIWrapper kpiWrapper, List<GraphDataWrapper> graphDataList,
			List<RangeSlab> rangeList) {
		List<RangeSlab> filteredRangeList = rangeList.stream()
													 .filter(x -> x != null && x.getLowerLimit() != null)
													 .collect(Collectors.toList());
				filteredRangeList.sort(Comparator.comparing(RangeSlab::getLowerLimit).reversed());
		Double cdf = 0.0;
		for (RangeSlab rangeSlab : filteredRangeList) {
			try {
				GraphDataWrapper graphData = new GraphDataWrapper();
				Double parcent = Utils.getPercentage(rangeSlab.getCount(), kpiWrapper.getTotalCount());
				cdf += parcent;
				graphData.setPdfValue(parseToFixedDecimalPlace(parcent, 2));
				graphData.setCdfValue(parseToFixedDecimalPlace(cdf, 2));
				graphData.setFrom(rangeSlab.getLowerLimit());
				if(rangeSlab.getUpperLimit()!=null) {
					graphData.setTo(rangeSlab.getUpperLimit());
					}
				graphData.setCount(rangeSlab.getCount());
				graphData.setKpiName(kpiWrapper.getKpiName() + Symbol.SPACE + OPEN_BRACKET
						+ getUnitByKPiName(kpiWrapper.getKpiName().trim()) + CLOSED_BRACKET);
				graphDataList.add(graphData);
			} catch (Exception e) {
				logger.error("Exception during calculatio of graph data ", Utils.getStackTrace(e));
			}
		}
	}

	/**
	 * Gets the erab drop rate.
	 *
	 * @param key                the key
	 * @param smmryData          the smmry data
	 * @param wrapper            the wrapper
	 * @param nvWrapper
	 * @param sVolteDropIndex
	 * @param sCallInitiateIndex
	 * @return the erab drop rate
	 */
	public static KPISummaryDataWrapper getVolteErabDropRate(String key, String[] smmryData,
			KPISummaryDataWrapper wrapper, NVReportConfigurationWrapper nvWrapper, Integer sCallInitiateIndex,
			Integer sVolteDropIndex) {
		wrapper.setSource(DT_MOBILE);
		wrapper.setItem(RETAINABILITY);
		if (sCallInitiateIndex != null && sVolteDropIndex != null && smmryData.length >= sCallInitiateIndex
				&& StringUtils.isNotBlank(smmryData[sCallInitiateIndex])
				&& NumberUtils.isValidNumber(Double.parseDouble(smmryData[sCallInitiateIndex]))) {
			try {
				Double volteDropCount = null;
				if (StringUtils.isNotBlank(smmryData[sVolteDropIndex])
						&& NumberUtils.isValidNumber(Double.parseDouble(smmryData[sVolteDropIndex]))) {
					volteDropCount = Double.parseDouble(smmryData[sVolteDropIndex]);
				} else {
					volteDropCount = 0.0;
				}
				Double val = (volteDropCount * INDEX_HUNDRED) / Double.parseDouble(smmryData[sCallInitiateIndex]);
				val = parseToFixedDecimalPlace(val, INDEX_TWO);
				wrapper.setAchived(val != null ? val + PERCENT : null);
				wrapper.setStatus((val < Double.parseDouble(wrapper.getTarget())) ? PASS : FAIL);
			} catch (Exception e) {
				logger.info("Exception inside method getErabDropRate {} ", e.getMessage());
			}
		}
		wrapper.setTestName(key.contains("VOLTE") ? "VoLTE ERAB Drop Rate" : "ERAB Drop Rate");
		wrapper.setTarget(nvWrapper.getTargetvalue() != null ? "<" + nvWrapper.getTargetvalue() + PERCENT : "-");
		return wrapper;
	}

	/**
	 * Gets the erab drop rate.
	 *
	 * @param key               the key
	 * @param smmryData         the smmry data
	 * @param wrapper           the wrapper
	 * @param nvWrapper
	 * @param sErabDropIndex
	 * @param sErabSuccessIndex
	 * @return the erab drop rate
	 */
	public static KPISummaryDataWrapper getErabDropRate1(String key, String[] smmryData, KPISummaryDataWrapper wrapper,
			NVReportConfigurationWrapper nvWrapper, Integer sErabSuccessIndex, Integer sErabDropIndex) {
		wrapper.setSource(DT_MOBILE);
		wrapper.setItem(RETAINABILITY);

		if (sErabSuccessIndex != null && smmryData.length >= sErabSuccessIndex
				&& StringUtils.isNotBlank(smmryData[sErabSuccessIndex])
				&& NumberUtils.isValidNumber(Double.parseDouble(smmryData[sErabSuccessIndex]))) {
			try {
				Double erabDropCount = null;
				if (sErabDropIndex != null && smmryData.length >= sErabDropIndex
						&& StringUtils.isNotBlank(smmryData[sErabDropIndex])
						&& NumberUtils.isValidNumber(Double.parseDouble(smmryData[sErabDropIndex]))) {
					erabDropCount = Double.parseDouble(smmryData[sErabDropIndex]);
				} else {
					erabDropCount = 0.0;
				}
				Double val = (erabDropCount * INDEX_HUNDRED) / Double.parseDouble(smmryData[sErabSuccessIndex]);
				val = parseToFixedDecimalPlace(val, INDEX_TWO);
				wrapper.setAchived(val != null ? val + PERCENT : null);
				wrapper.setStatus((val < Double.parseDouble(wrapper.getTarget())) ? PASS : FAIL);
			} catch (Exception e) {
				logger.info("Exception inside method getErabDropRate1 {} ", e.getMessage());
			}
		}
		wrapper.setTestName(key.contains("VOLTE") ? "VoLTE ERAB Drop Rate" : "ERAB Drop Rate");
		wrapper.setTarget(nvWrapper.getTargetvalue() != null ? "<" + nvWrapper.getTargetvalue() + PERCENT : "-");
		return wrapper;
	}

	/**
	 * Gets the volte setup sucess rate.
	 *
	 * @param smmryData          the smmry data
	 * @param wrapper            the wrapper
	 * @param sCallSuccessIndex
	 * @param sCallInitiateIndex
	 * @param nvWrapper
	 * @return the volte setup sucess rate
	 */
	public static KPISummaryDataWrapper getVolteSetupSucessRate1(String[] smmryData, KPISummaryDataWrapper wrapper,
			NVReportConfigurationWrapper nvWrapper, Integer sCallInitiateIndex, Integer sCallSuccessIndex) {
		wrapper.setSource(DT_MOBILE);
		wrapper.setTestName("VoLTE Setup Success Rate");
		wrapper.setItem(ACCESSIBILITY);

		if (sCallInitiateIndex != null && smmryData.length >= sCallInitiateIndex
				&& StringUtils.isNotBlank(smmryData[sCallInitiateIndex])
				&& NumberUtils.isValidNumber(Double.parseDouble(smmryData[sCallInitiateIndex]))) {
			try {
				Double callSucessCount = 0.0;
				if (sCallSuccessIndex != null && smmryData.length >= sCallSuccessIndex
						&& StringUtils.isNotBlank(smmryData[sCallSuccessIndex])
						&& NumberUtils.isValidNumber(Double.parseDouble(smmryData[sCallSuccessIndex]))) {
					callSucessCount = Double.parseDouble(smmryData[sCallSuccessIndex]);
				}
				Double val = (callSucessCount * INDEX_HUNDRED) / Double.parseDouble(smmryData[sCallInitiateIndex]);
				val = parseToFixedDecimalPlace(val, INDEX_TWO);
				wrapper.setAchived(val != null ? val + PERCENT : null);
				wrapper.setStatus((val > Double.parseDouble(wrapper.getTarget())) ? PASS : FAIL);
			} catch (Exception e) {
				logger.error("Exception inside method getVolteSetupSucessRate1 {} ", e.getMessage());
			}
		}
		wrapper.setTarget(">" + nvWrapper.getTargetvalue() + PERCENT);
		return wrapper;
	}

	public static String getUnitByKPiName(String kpiName) {
		switch (kpiName) {
		case RSRP:
		case FTP_DL_RSRP:
		case FTP_UL_RSRP:
		case HTTP_DL_RSRP:
		case HTTP_UL_RSRP:
		case RSSI:
			return DBM;
		case SINR:
		case FTP_DL_SINR:
		case FTP_UL_SINR:
		case HTTP_DL_SINR:
		case HTTP_UL_SINR:
		case RSRQ:
			return DB;
		case UL:
		case DL:
		case HTTP_DL_THROUGHPUT:
		case FTP_DL_THROUGHPUT:
		case HTTP_UL_THROUGHPUT:
		case FTP_UL_THROUGHPUT:
		case PDSCH_THROUGHPUT:
		case MAC_DL_THROUGHPUT:
		case MAC_UL_THROUGHPUT:
			return MBPS;
		case JITTER:
		case LATENCY:
		case WEB_DOWNLOAD_DELAY:
		case HO_INTERRUPTION_TIME:
			return MS_UNIT_REPORT;
		default:
			return BLANK_STRING;
		}
	}

	public static String getChartTitle(String kpiName) {
		switch (kpiName) {
		case RSRP:
			return LiveDriveReportConstants.CHART_TITLE_RSRP;
		case SINR:
			return LiveDriveReportConstants.CHART_TITLE_SINR;
		case UL:
			return LiveDriveReportConstants.CHART_TITLE_UL;
		case FTP_UL_THROUGHPUT:
			return LiveDriveReportConstants.CHART_TITLE_FTP_UL;
		case HTTP_UL_THROUGHPUT:
			return LiveDriveReportConstants.CHART_TITLE_HTTP_UL;
		case DL:
			return LiveDriveReportConstants.CHART_TITLE_DL;
		case FTP_DL_THROUGHPUT:
			return LiveDriveReportConstants.CHART_TITLE_FTP_DL;
		case HTTP_DL_THROUGHPUT:
			return LiveDriveReportConstants.CHART_TITLE_HTTP_DL;
		case JITTER:
			return LiveDriveReportConstants.CHART_TITLE_JITTER;
		case LATENCY:
			return LiveDriveReportConstants.CHART_TITLE_LATENCY;
		case WEB_DOWNLOAD_DELAY:
			return LiveDriveReportConstants.CHART_TITLE_WEBDELAY;
		default:
			return BLANK_STRING;
		}
	}

	public static String getChartType(String kpiName) {
		switch (kpiName) {
		case RSRP:
			return LiveDriveReportConstants.CHART_TYPE_FOR_RSRP;
		case SINR:
			return LiveDriveReportConstants.CHART_TYPE_FOR_SINR;
		case UL:
			return LiveDriveReportConstants.CHART_TYPE_FOR_UL;
		case FTP_UL_THROUGHPUT:
			return LiveDriveReportConstants.CHART_TYPE_FOR_FTP_UL;
		case HTTP_UL_THROUGHPUT:
			return LiveDriveReportConstants.CHART_TYPE_FOR_HTTP_UL;
		case DL:
			return LiveDriveReportConstants.CHART_TITLE_DL;
		case FTP_DL_THROUGHPUT:
			return LiveDriveReportConstants.CHART_TYPE_FOR_FTP_DL;
		case HTTP_DL_THROUGHPUT:
			return LiveDriveReportConstants.CHART_TYPE_FOR_HTTP_DL;
		case JITTER:
			return LiveDriveReportConstants.CHART_TYPE_FOR_JITTER;
		case LATENCY:
			return LiveDriveReportConstants.CHART_TYPE_FOR_LATENCY;
		case WEB_DOWNLOAD_DELAY:
			return LiveDriveReportConstants.CHART_TYPE_FOR_WEBDELAY;
		default:
			return BLANK_STRING;
		}
	}

	public static String getGraphHeading(String kpiName) {
		switch (kpiName) {
		case RSRP:
			return LiveDriveReportConstants.GRAPH_HEADING_RSRP;
		case SINR:
			return LiveDriveReportConstants.GRAPH_HEADING_SINR;
		case DL:
			return LiveDriveReportConstants.GRAPH_HEADING_UL;
		case FTP_UL_THROUGHPUT:
			return LiveDriveReportConstants.GRAPH_HEADING_FTP_UL;
		case HTTP_UL_THROUGHPUT:
			return LiveDriveReportConstants.GRAPH_HEADING_HTTP_UL;
		case UL:
			return LiveDriveReportConstants.GRAPH_HEADING_DL;
		case FTP_DL_THROUGHPUT:
			return LiveDriveReportConstants.GRAPH_HEADING_FTP_DL;
		case HTTP_DL_THROUGHPUT:
			return LiveDriveReportConstants.GRAPH_HEADING_HTTP_DL;
		case JITTER:
			return LiveDriveReportConstants.GRAPH_HEADING_JITTER;
		case LATENCY:
			return LiveDriveReportConstants.GRAPH_HEADING_LATENCY;
		case WEB_DOWNLOAD_DELAY:
			return LiveDriveReportConstants.GRAPH_HEADING_WEBDL;
		default:
			return BLANK_STRING;
		}
	}

	public static Map<String, Object> getSectorWiseImageFromList(List<DriveDataWrapper> imageDataList) {
		Map<String, Object> sectorImage = new HashMap<>();
		logger.info("inside method getSectorWiseImageFromList {} ", imageDataList.size());
		int i = 1;
		for (DriveDataWrapper driveData : imageDataList) {
			try {
				InputStream in = new ByteArrayInputStream(driveData.getImg());
				BufferedImage buffredImage = ImageIO.read(in);
				ImageIO.write(buffredImage, JPG,
						new File(ConfigUtils.getString(SSVT_REPORT_PATH) + SECTOR + i + DOT_JPG));
				sectorImage.put(SECTOR + i, ConfigUtils.getString(SSVT_REPORT_PATH) + SECTOR + i + DOT_JPG);
				i++;
			} catch (Exception e) {
				logger.warn("Inside method getSectorWiseImageFromList {}", e.getMessage());
			}
		}
		logger.info("sectorImage {}", sectorImage);
		return sectorImage;
	}

	public static String getGeographyTableNameBYGeographyType(String geographyType) {
		if (geographyType != null) {
			switch (geographyType) {
			case "GeographyL4":
				return ConfigUtils.getString(ConfigUtil.GEOGRAPHYL4_TABLE);
			case "GeographyL3":
				return ConfigUtils.getString(ConfigUtil.GEOGRAPHYL3_TABLE);
			case "GeographyL2":
				return ConfigUtils.getString(ConfigUtil.GEOGRAPHYL2_TABLE);
			case "GeographyL1":
				return ConfigUtils.getString(ConfigUtil.GEOGRAPHYL1_TABLE);
			default:
				return null;
			}
		}
		return null;
	}

	public static List<LiveDriveVoiceAndSmsWrapper> getCallPlotDataList(String[] summaryData) {
		List<LiveDriveVoiceAndSmsWrapper> voiceAndSmsList = new ArrayList<>();
		if (checkIndexValue(DriveHeaderConstants.SUMMARY_CALL_ATTEMPTS, summaryData)) {
			voiceAndSmsList.add(getLiveDriveVoiceAndSmsWrapperForCallAndHandover(DriveHeaderConstants.INITIATE_CALL,
					DriveHeaderConstants.SUMMARY_CALL_ATTEMPTS, summaryData));
			voiceAndSmsList.add(getLiveDriveVoiceAndSmsWrapperForCallAndHandover(DriveHeaderConstants.DROPPED_CALL,
					DriveHeaderConstants.SUMMARY_CALL_DROP, summaryData));
			voiceAndSmsList.add(getLiveDriveVoiceAndSmsWrapperForCallAndHandover(DriveHeaderConstants.FAIL_CALL,
					DriveHeaderConstants.SUMMARY_CALL_FAILURE, summaryData));
			voiceAndSmsList.add(getLiveDriveVoiceAndSmsWrapperForCallAndHandover(DriveHeaderConstants.SUCCESS_CALL,
					DriveHeaderConstants.SUMMARY_CALL_SUCCESS, summaryData));
		}
		return voiceAndSmsList;
	}

	public static List<LiveDriveVoiceAndSmsWrapper> getCallPlotDataListForReport(String[] summaryData,
			Map<String, Integer> summaryKpiIndexMap) {
		List<LiveDriveVoiceAndSmsWrapper> voiceAndSmsList = new ArrayList<>();
		if (checkIndexValue(summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_INITIATE), summaryData)) {
			voiceAndSmsList.add(getLiveDriveVoiceAndSmsWrapperForCallAndHandover(DriveHeaderConstants.INITIATE_CALL,
					summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_INITIATE), summaryData));
			voiceAndSmsList.add(getLiveDriveVoiceAndSmsWrapperForCallAndHandover(DriveHeaderConstants.DROPPED_CALL,
					summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_DROP), summaryData));
			voiceAndSmsList.add(getLiveDriveVoiceAndSmsWrapperForCallAndHandover(DriveHeaderConstants.FAIL_CALL,
					summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_FAILURE), summaryData));
			voiceAndSmsList.add(getLiveDriveVoiceAndSmsWrapperForCallAndHandover(DriveHeaderConstants.SUCCESS_CALL,
					summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_SUCCESS), summaryData));
		}
		return voiceAndSmsList;
	}

	public static List<LiveDriveVoiceAndSmsWrapper> getHandoverPlotDataList(String[] summaryData) {
		List<LiveDriveVoiceAndSmsWrapper> voiceAndSmsList = new ArrayList<>();
		voiceAndSmsList.add(getLiveDriveVoiceAndSmsWrapperForCallAndHandover(HANDOVER_INITIATE,
				DriveHeaderConstants.SUMMARY_HANDOVER_INITIATE_INDEX, summaryData));
		voiceAndSmsList.add(getLiveDriveVoiceAndSmsWrapperForCallAndHandover(HANDOVER_FAILURE,
				DriveHeaderConstants.SUMMARY_HANDOVER_FAIL_INDEX, summaryData));
		voiceAndSmsList.add(getLiveDriveVoiceAndSmsWrapperForCallAndHandover(HANDOVER_SUCCESS,
				DriveHeaderConstants.SUMMARY_HANDOVER_SUCCESS_INDEX, summaryData));
		return voiceAndSmsList;
	}

	public static List<LiveDriveVoiceAndSmsWrapper> getHandoverPlotDataListForReport(String[] summaryData,
			Map<String, Integer> summaryKpiIndexMap) {
		List<LiveDriveVoiceAndSmsWrapper> voiceAndSmsList = new ArrayList<>();
		voiceAndSmsList.add(getLiveDriveVoiceAndSmsWrapperForCallAndHandover(HANDOVER_INITIATE,
				summaryKpiIndexMap.get(SUM_UNDERSCORE + HANDOVER_INITIATE), summaryData));
		voiceAndSmsList.add(getLiveDriveVoiceAndSmsWrapperForCallAndHandover(HANDOVER_FAILURE,
				summaryKpiIndexMap.get(SUM_UNDERSCORE + HANDOVER_FAILURE), summaryData));
		voiceAndSmsList.add(getLiveDriveVoiceAndSmsWrapperForCallAndHandover(HANDOVER_SUCCESS,
				summaryKpiIndexMap.get(SUM_UNDERSCORE + HANDOVER_SUCCESS), summaryData));
		return voiceAndSmsList;
	}

	private static LiveDriveVoiceAndSmsWrapper getLiveDriveVoiceAndSmsWrapperForCallAndHandover(String eventName,
			Integer index, String[] summaryData) {
		LiveDriveVoiceAndSmsWrapper wrapper = new LiveDriveVoiceAndSmsWrapper();
		try {
			wrapper.setTestedKpiName(eventName);
			if (checkIndexValue(index, summaryData)) {
				wrapper.setTargetValue(summaryData[index]);
			}
			logger.debug("returning wrapper: {}", wrapper.toString());
		} catch (Exception e) {
			logger.error("Error in getLiveDriveVoiceAndSmsWrapperForCall{}", e.getMessage());
		}
		return wrapper;
	}

	public static VoiceStatsWrapper getVoiceStatsbyOperator(String[] summaryData, String operator,
			Map<String, Integer> summaryKpiIndexMap) {
		logger.info("inside method getVoiceStatsbyOperator to calulate the stats for operator {} ", operator);
		VoiceStatsWrapper voiceStatWraper = new VoiceStatsWrapper();
		if (summaryData != null && summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_FAILURE) != null
				&& summaryData.length >= summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_FAILURE)) {
			voiceStatWraper.setOperator(operator);
			if (checkIndexValue(summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_INITIATE), summaryData)) {
				voiceStatWraper
						.setCallAttemptCount(summaryData[summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_INITIATE)]);
			}
			if (checkIndexValue(summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_SUCCESS), summaryData)) {
				voiceStatWraper
						.setCallConnectedCount(summaryData[summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_SUCCESS)]);
			}
			if (checkIndexValue(summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_DROP), summaryData)) {
				voiceStatWraper.setCallDroppedCount(summaryData[summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_DROP)]);
			}

			setCallSuccessAndDropRate(voiceStatWraper, summaryData, summaryKpiIndexMap);
		}

		return voiceStatWraper;
	}

	private static void setCallSuccessAndDropRate(VoiceStatsWrapper voiceStatWraper, String[] smmryData,
			Map<String, Integer> summaryKpiIndexMap) {
		if (voiceStatWraper.getCallAttemptCount() != null && !voiceStatWraper.getCallAttemptCount().isEmpty()) {
			try {
				Double percentage;
				percentage = round(getPercentage(summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_SUCCESS),
						summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_INITIATE), smmryData), TWO_DECIMAL_PLACES);

				if (percentage != null) {
					voiceStatWraper.setCallSetupSucessRate(percentage.toString());
				}

				percentage = round(getPercentage(summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_DROP),
						summaryKpiIndexMap.get(SUM_UNDERSCORE + CALL_INITIATE), smmryData), TWO_DECIMAL_PLACES);
				if (percentage != null) {
					voiceStatWraper.setCallDropRate(percentage.toString());
				}
			} catch (Exception e) {
				logger.error("Exception in Calculating Voice stats Percentage {} ", e.getMessage());
			}
		}
	}

	public static String[] convertSummaryToArray(String summaryData) {
		try {
			if (summaryData != null) {
				return summaryData.replaceAll("\\]", "").replaceAll("\\[", "").split(",");
			}
		} catch (Exception e) {
			logger.error("Exception to convertSummaryToArray {} ", e.getMessage());
		}
		return null;
	}

	public static Integer getDataIndexByKPiName(String kpiName) {
		switch (kpiName) {
		case FTP_DL_THROUGHPUT:
		case HTTP_DL_THROUGHPUT:
			return DriveHeaderConstants.INDEX_DL;
		case FTP_UL_THROUGHPUT:
		case HTTP_UL_THROUGHPUT:
			return DriveHeaderConstants.INDEX_UL;
		case FTP_DL_SINR:
		case HTTP_DL_SINR:
		case FTP_UL_SINR:
		case HTTP_UL_SINR:
			return DriveHeaderConstants.INDEX_SINR;
		case FTP_DL_RSRP:
		case HTTP_DL_RSRP:
		case FTP_UL_RSRP:
		case HTTP_UL_RSRP:
		case IDLE_PLOT:
			return DriveHeaderConstants.INDEX_RSRP;
		default:
			return null;
		}
	}

	/** / comment to check update. */
	public static String getTestTypeValueOnBasisOfKPi(String kpiName) {
		switch (kpiName) {
		case HTTP_DL_THROUGHPUT:
		case HTTP_DL_SINR:
		case HTTP_DL_RSRP:
			return HTTP_DOWNLOAD;
		case FTP_DL_THROUGHPUT:
		case FTP_DL_SINR:
		case FTP_DL_RSRP:
			return FTP_DOWNLOAD;
		case HTTP_UL_THROUGHPUT:
		case HTTP_UL_SINR:
		case HTTP_UL_RSRP:
			return HTTP_UPLOAD;
		case FTP_UL_THROUGHPUT:
		case FTP_UL_SINR:
		case FTP_UL_RSRP:
			return FTP_UPLOAD;
		case IDLE_PLOT:
			return IDLE;
		default:
			return null;
		}
	}

	public static boolean isTestTypeCheckRequired(String kpiName) {
		return FTP_DL_THROUGHPUT.equalsIgnoreCase(kpiName) || HTTP_DL_THROUGHPUT.equalsIgnoreCase(kpiName)
				|| FTP_UL_THROUGHPUT.equalsIgnoreCase(kpiName) || HTTP_UL_THROUGHPUT.equalsIgnoreCase(kpiName)
				|| FTP_DL_SINR.equalsIgnoreCase(kpiName) || HTTP_DL_SINR.equalsIgnoreCase(kpiName)
				|| FTP_UL_SINR.equalsIgnoreCase(kpiName) || HTTP_UL_SINR.equalsIgnoreCase(kpiName)
				|| FTP_DL_RSRP.equalsIgnoreCase(kpiName) || HTTP_DL_RSRP.equalsIgnoreCase(kpiName)
				|| FTP_UL_RSRP.equalsIgnoreCase(kpiName) || HTTP_UL_RSRP.equalsIgnoreCase(kpiName)
				|| IDLE_PLOT.equalsIgnoreCase(kpiName);
	}

	public static YoutubeTestWrapper getHttpDownLinkData(String httpDlLinkData) {
		YoutubeTestWrapper wrapper = null;
		logger.info(" inside the method getHttpDownLinkData {}", httpDlLinkData);
		if (httpDlLinkData != null) {
			try {
				wrapper = new YoutubeTestWrapper();
				httpDlLinkData = httpDlLinkData.replace("[", "");
				httpDlLinkData = httpDlLinkData.replace("]", "");
				String[] csv = httpDlLinkData.split(",");
				return setHttpDownloadLinkData(wrapper, csv);
			} catch (Exception e) {
				logger.error("Error inside the method getHttpDownLinkData {}", e.getMessage());
			}
		}
		return null;
	}

	private static YoutubeTestWrapper setHttpDownloadLinkData(YoutubeTestWrapper wrapper, String[] csv) {
		if (checkIndexValue(DriveHeaderConstants.INDEX_HTTP_DL_TOTAL_ATTEMPTS, csv)) {
			wrapper.setHttpDlAttempt(NumberUtils.toInteger(csv[DriveHeaderConstants.INDEX_HTTP_DL_TOTAL_ATTEMPTS]));
		}
		if (checkIndexValue(DriveHeaderConstants.INDEX_HTTP_DL_TOTAL_SUCCESS, csv)) {
			wrapper.setHttpDlSuccess(NumberUtils.toInteger(csv[DriveHeaderConstants.INDEX_HTTP_DL_TOTAL_SUCCESS]));
		}
		if (checkIndexValue(DriveHeaderConstants.INDEX_HTTP_DL_TOTAL_SUCCESS_RATE, csv)) {
			wrapper.setHttpDlSr(NumberUtils.toDouble(csv[DriveHeaderConstants.INDEX_HTTP_DL_TOTAL_SUCCESS_RATE]));
		}
		if (checkIndexValue(DriveHeaderConstants.INDEX_HTTP_DL_AVG_TIME, csv)) {
			wrapper.setHttpTimeToDownload(NumberUtils.toDouble(csv[DriveHeaderConstants.INDEX_HTTP_DL_AVG_TIME]));
		}
		if (checkIndexValue(DriveHeaderConstants.INDEX_HTTP_DL, csv)) {
			wrapper.setHttpThpAvg(NumberUtils.toDouble(csv[DriveHeaderConstants.INDEX_HTTP_DL]));
		}
		if (wrapper.getHttpDlAttempt() != null) {
			return wrapper;
		} else {
			return null;
		}
	}

	public static String getFileName(String workOrderName, Integer analyticId, String filePath) {
		String destFileName = filePath;
		File destDir = new File(destFileName);
		if (!destDir.isDirectory()) {
			destDir.mkdirs();
		}
		destFileName += workOrderName + UNDERSCORE + analyticId + PDF_EXTENSION;
		return destFileName.replace(SPACE, UNDERSCORE);
	}

	public static String getFileNameForDoc(String workOrderName, Integer workOrderId) {
		String workingDir = System.getProperty(USER_DIR);
		workingDir += FORWARD_SLASH + workOrderName + UNDERSCORE + workOrderId + ".doc";
		workingDir = workingDir.replace(SPACE, UNDERSCORE);
		logger.info("working directory with report path is {}", workingDir);
		return workingDir;
	}

	public static void fillDataInDocxExporter(Map<String, Object> imageMap, String reportAssetPath,
			JRBeanCollectionDataSource rfbeanColDataSource, String destinationFileName) throws JRException {
		String fileName = null;
		if (rfbeanColDataSource != null) {
			fileName = JasperFillManager.fillReportToFile(reportAssetPath, imageMap, rfbeanColDataSource);
		} else {
			fileName = JasperFillManager.fillReportToFile(reportAssetPath, imageMap);
		}
		if (fileName != null) {
			JRDocxExporter exporter = new JRDocxExporter();
			exporter.setExporterInput(new SimpleExporterInput(new File(fileName)));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new File(destinationFileName)));
//			exporter.setParameter(JRExporterParameter.INPUT_FILE, new File(fileName));
//			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(destinationFileName));
			exporter.exportReport();
		}
	}

	public static void fillDataInXlsxExporter(Map<String, Object> imageMap, String reportAssetPath,
			JRBeanCollectionDataSource rfbeanColDataSource, String destinationFileName, String[] sheetNames) throws JRException {
		String fileName = null;
		if (rfbeanColDataSource != null) {
			fileName = JasperFillManager.fillReportToFile(reportAssetPath, imageMap, rfbeanColDataSource);
		} else {
			fileName = JasperFillManager.fillReportToFile(reportAssetPath, imageMap);
		}
		
		if (fileName != null) {
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setExporterInput(new SimpleExporterInput(new File(fileName)));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(new File(destinationFileName)));
//			exporter.setParameter(JRExporterParameter.INPUT_FILE, new File(fileName));
//			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
//			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
//			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
//			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
//			exporter.setParameter(JRExporterParameter.OUTPUT_FILE, new File(destinationFileName));
//			exporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, sheetNames);

			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setOnePagePerSheet(true);
			configuration.setDetectCellType(true);
			configuration.setWhitePageBackground(false);
			configuration.setRemoveEmptySpaceBetweenRows(true);
			configuration.setSheetNames(sheetNames);
			exporter.setConfiguration(configuration);
			exporter.exportReport();
		}

	}

	public static List<GraphWrapper> setGraphDataKpiWise(KPIWrapper kpiWrapper, List<Double> dataList) {
		logger.info("inside the method setGraphDataKpiWise {}", kpiWrapper.getKpiName());
		GraphWrapper graphWrapper = new GraphWrapper();
		List<GraphDataWrapper> graphDataList = new ArrayList<>();
		List<GraphWrapper> graphList = new ArrayList<>();
		Statistics staticstic = new Statistics(dataList);
		try {
			graphWrapper.setMax(staticstic.getMax());
			graphWrapper.setMin(staticstic.getMin());
			graphWrapper.setMean(staticstic.getMean());
			graphWrapper.setKpiName(kpiWrapper.getKpiName() + Symbol.SPACE + OPEN_BRACKET
					+ getUnitByKPiName(kpiWrapper.getKpiName()).trim() + CLOSED_BRACKET);
			graphWrapper.setCount(kpiWrapper.getTotalCount());
			setGraphDataToList(kpiWrapper, graphDataList, kpiWrapper.getRangeSlabs());
			graphWrapper.setGraphDataList(graphDataList);
			graphList.add(graphWrapper);
			return graphList;
		} catch (Exception e) {
			logger.error("Exception inside the method setGraphDataKpiWise {}", Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	public static void convertPdfToDoc(String filePath) {
		logger.info("inside converPdfToDoc file path is {}", filePath);
		try {
			long waitTime = ONE_TWO_ZER0;
			String comd = SOFFICE_COMMAND;
			comd = comd + filePath;
			logger.info("comd is ==>{}", comd);
			Process p = Runtime.getRuntime().exec(comd);
			p.waitFor(waitTime, TimeUnit.SECONDS);
			p.destroy();
		} catch (IOException e) {
			logger.error("Exception inside the method converPdfToDoc {}", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception to converPdfToDoc {} ", Utils.getStackTrace(e));
		}
	}

	public static String getDayFromDayNo(int day) {
		String[] strDays = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thusday", "Friday", "Saturday" };
		return strDays[day];
	}

	public static Map<String, Color> getServingSystemColorMap(DriveImageWrapper driveImageWrapper) {
		Map<String, Color> servingColorMap = new HashMap<>();
		if (driveImageWrapper != null) {
			List<String> uniqueKeysOfTechBand = getUniqueCombinationsOfBandtech(driveImageWrapper);
			uniqueKeysOfTechBand.forEach(key -> servingColorMap.put(key,
					new Color(rand.nextInt(TWO_FIVE_ZER0), rand.nextInt(TWO_FIVE_ZER0), rand.nextInt(TWO_FIVE_ZER0))));
		}
		logger.info("Serving System Color Map Size {} , {} ", servingColorMap.size(),
				new Gson().toJson(servingColorMap));
		return servingColorMap;
	}

	public static Map<String, Color> getDlBandWidthColorMap(DriveImageWrapper driveImageWrapper) {

		List<String[]> dataKPIs = driveImageWrapper.getDataKPIs();
		Map<String, Color> dlBandwidthColorMap = new HashMap<>();
		if (driveImageWrapper != null && !driveImageWrapper.getDataKPIs().isEmpty()) {
			Integer dlBandwidthIndex = findKPIIndexFromKPIWrapper(driveImageWrapper, DL_BANWIDTH);

			List<String> dlBnadWidths = dataKPIs.stream()
					.filter(array -> dlBandwidthIndex != null && array.length > dlBandwidthIndex
							&& array[dlBandwidthIndex] != null)
					.map(array -> array[dlBandwidthIndex]).distinct().collect(Collectors.toList());
			dlBnadWidths.forEach(key -> dlBandwidthColorMap.put(key,
					new Color(rand.nextInt(TWO_FIVE_ZER0), rand.nextInt(TWO_FIVE_ZER0), rand.nextInt(TWO_FIVE_ZER0))));
		}
		logger.info("dlBnadWidths Color Map Size {} , {} ", dlBandwidthColorMap.size(),
				new Gson().toJson(dlBandwidthColorMap));
		return dlBandwidthColorMap;
	}

	private static List<String> getUniqueCombinationsOfBandtech(DriveImageWrapper driveImageWrapper) {
		List<String[]> dataKPIs = driveImageWrapper.getDataKPIs();
		Integer indexNetworkType = findKPIIndexFromKPIWrapper(driveImageWrapper, NETWORK_TYPE);
		Integer indexTechnology = findKPIIndexFromKPIWrapper(driveImageWrapper, DriveHeaderConstants.TECHNOLOGY);
		Integer indexBand = findKPIIndexFromKPIWrapper(driveImageWrapper, BAND);

		List<String[]> filterdData = dataKPIs.stream()
				.filter(e -> indexNetworkType != null && e != null && e.length > indexNetworkType)
				.filter(e -> indexNetworkType != null && e[indexNetworkType] != null
						&& !e[indexNetworkType].trim().isEmpty())
				.filter(e -> indexTechnology != null && e[indexTechnology] != null
						&& !e[indexTechnology].trim().isEmpty())
				.filter(e -> indexBand != null && e[indexBand] != null && !e[indexBand].trim().isEmpty())
				.collect(Collectors.toList());
		Map<String, List<String[]>> map = filterdData.stream().collect(
				Collectors.groupingBy(x -> groupByBandTechNwType(x, indexNetworkType, indexTechnology, indexBand)));
		logger.info("MapData  {} ", new Gson().toJson(map.keySet()));
		return new ArrayList<>(map.keySet());
	}

	public static Integer findKPIIndexFromKPIWrapper(DriveImageWrapper driveImageWrapper, String kpiName) {
		for (KPIWrapper wrapper : driveImageWrapper.getKpiWrappers()) {
			if (kpiName.equalsIgnoreCase(wrapper.getKpiName())) {
				return wrapper.getIndexKPI();
			}
		}

		return null;

	}

	private static String groupByBandTechNwType(String[] data, Integer indexNetworkType, Integer indexTechnology,
			Integer indexBand) {

		return (indexNetworkType != null && indexTechnology != null && indexBand != null)
				? (data[indexNetworkType] + UNDERSCORE + data[indexTechnology] + UNDERSCORE + data[indexBand])
				: null;
	}

	public static Map<String, Long> getServingSystemMapCount(DriveImageWrapper driveImageWrapper) {
		Integer indexNetworkType = findKPIIndexFromKPIWrapper(driveImageWrapper, NETWORK_TYPE);
		Integer indexTechnology = findKPIIndexFromKPIWrapper(driveImageWrapper, DriveHeaderConstants.TECHNOLOGY);
		Integer indexBand = findKPIIndexFromKPIWrapper(driveImageWrapper, BAND);

		List<String[]> filterdData = driveImageWrapper.getDataKPIs().stream()
				.filter(e -> indexNetworkType != null && e[indexNetworkType] != null
						&& !e[indexNetworkType].trim().isEmpty())
				.filter(e -> indexTechnology != null && e[indexTechnology] != null
						&& !e[indexTechnology].trim().isEmpty())
				.filter(e -> indexBand != null && e[indexBand] != null && !e[indexBand].trim().isEmpty())
				.collect(Collectors.toList());
		return filterdData.stream().collect(Collectors.groupingBy(
				x -> groupByBandTechNwType(x, indexNetworkType, indexTechnology, indexBand), Collectors.counting()));
	}

	public static Double getDoubleValue(String val) {
		if (val != null && !StringUtils.isEmpty(val)) {
			return Double.parseDouble(val);
		}
		return null;
	}

	public static List<CallEventStatistics> getCallDataForBRTI(String csv) {
		List<CallEventStatistics> listCallEventStatistics = null;
		try {
			listCallEventStatistics = new ArrayList<>();
			if (csv != null) {
				listCallEventStatistics = new ArrayList<>();
				String[] data = csv.split(Symbol.COMMA_STRING);
				if (checkIndexValue(DriveHeaderConstants.SUMMARY_CALL_ATTEMPTS, data)) {
					listCallEventStatistics
							.add(getCallDataWrapperForBRTI(data, DriveHeaderConstants.SUMMARY_CALL_ATTEMPT_ON_NET,
									DriveHeaderConstants.SUMMARY_CALL_DROP_ON_NET,
									DriveHeaderConstants.SUMMARY_CALL_FAILURE_ON_NET,
									DriveHeaderConstants.SUMMARY_CALL_SUCCESS_ON_NET, CALL_COUNT_ON_NET));
					listCallEventStatistics
							.add(getCallDataWrapperForBRTI(data, DriveHeaderConstants.SUMMARY_CALL_ATTEMPT_OFF_NET,
									DriveHeaderConstants.SUMMARY_CALL_DROP_OFF_NET,
									DriveHeaderConstants.SUMMARY_CALL_FAILURE_OFF_NET,
									DriveHeaderConstants.SUMMARY_CALL_SUCCESS_OFF_NET, CALL_COUNT_OFF_NET));
					listCallEventStatistics
							.add(getCallDataWrapperForBRTI(data, DriveHeaderConstants.SUMMARY_CALL_ATTEMPTS,
									DriveHeaderConstants.SUMMARY_CALL_DROP, DriveHeaderConstants.SUMMARY_CALL_FAILURE,
									DriveHeaderConstants.SUMMARY_CALL_SUCCESS, CALL_COUNT));
				}
			}
		} catch (Exception e) {
			logger.error("Error Inside getCallDataForBRTI {} ", Utils.getStackTrace(e));
		}
		return listCallEventStatistics;
	}

	private static CallEventStatistics getCallDataWrapperForBRTI(String[] data, Integer summaryCallAttempts,
			Integer summaryCallDrop, Integer summaryCallFailure, Integer summaryCallSuccess, String kpiName) {
		CallEventStatistics callEventStatistics = new CallEventStatistics();
		callEventStatistics.setKpiName(kpiName);
		callEventStatistics.setTestType(CALL_CAPITAL);
		callEventStatistics.setCallAttempt(checkIndexValue(summaryCallAttempts, data)
				? String.valueOf(NumberUtils.toDouble(data[summaryCallAttempts]).intValue())
				: null);
		callEventStatistics.setCallDrop(checkIndexValue(summaryCallDrop, data)
				? String.valueOf(NumberUtils.toDouble(data[summaryCallDrop]).intValue())
				: null);
		callEventStatistics.setCallFailure(checkIndexValue(summaryCallFailure, data)
				? String.valueOf(NumberUtils.toDouble(data[summaryCallFailure]).intValue())
				: null);
		callEventStatistics.setCallSuccess(checkIndexValue(summaryCallSuccess, data)
				? String.valueOf(NumberUtils.toDouble(data[summaryCallSuccess]).intValue())
				: null);
		callEventStatistics.setEsaRate(getESARate(summaryCallAttempts, summaryCallDrop, summaryCallFailure, data));
		callEventStatistics.setDropRate(getPercentage(summaryCallAttempts, summaryCallDrop, data));
		return callEventStatistics;
	}

	private static Double getESARate(Integer summaryCallAttempts, Integer summaryCallDrop, Integer summaryCallFailure,
			String[] csv) {
		Double callAttempt = null;
		Double callFailure = null;
		Double callDrop = null;
		try {
			if (checkIndexValue(summaryCallAttempts, csv)) {
				callAttempt = Double.parseDouble(csv[summaryCallAttempts]);
				callFailure = Double.parseDouble(csv[summaryCallFailure]);
				callDrop = Double.parseDouble(csv[summaryCallDrop]);
				callFailure = callFailure != null ? callFailure : 0.0;
				callDrop = callDrop != null ? callDrop : 0.0;
				logger.info("callFailure {} , CallDrop {}, callAttempt {} ", callFailure, callDrop, callAttempt);
				if (callAttempt != 0.0) {
					return ((callAttempt - (callFailure + callDrop)) / callAttempt) * HUNDRED;
				} else {
					return 0.0;
				}
			}
		} catch (Exception e) {
			logger.info("Exception in getPercentage : {}/{}", callFailure + callDrop, callAttempt);
		}

		return null;
	}

	public static Geography getGeographyObject(GeographyParser geographyParser) {
		try {
			if (geographyParser != null) {
				if (geographyParser.getGeographyL4() != null && !geographyParser.getGeographyL4().isEmpty()) {
					geographyParser.getGeographyL4().get(INDEX_ZER0).setGeographyType(ForesightConstants.GEOGRAPHY_L4);
					return geographyParser.getGeographyL4().get(INDEX_ZER0);
				} else if (geographyParser.getGeographyL3() != null && !geographyParser.getGeographyL3().isEmpty()) {
					geographyParser.getGeographyL3().get(INDEX_ZER0).setGeographyType(ForesightConstants.GEOGRAPHY_L3);
					return geographyParser.getGeographyL3().get(INDEX_ZER0);
				} else if (geographyParser.getGeographyL2() != null && !geographyParser.getGeographyL2().isEmpty()) {
					geographyParser.getGeographyL2().get(INDEX_ZER0).setGeographyType(ForesightConstants.GEOGRAPHY_L2);
					return geographyParser.getGeographyL2().get(INDEX_ZER0);
				} else if (geographyParser.getGeographyL1() != null && !geographyParser.getGeographyL1().isEmpty()) {
					geographyParser.getGeographyL1().get(INDEX_ZER0).setGeographyType(ForesightConstants.GEOGRAPHY_L1);
					return geographyParser.getGeographyL1().get(INDEX_ZER0);
				}
			}
		} catch (Exception e) {
			logger.error("Exception in finding the getGeography Object {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	public static Integer addValues(Integer integer1, Integer integer2) {
		if (integer1 == null) {
			if (integer2 != null) {
				return integer2;
			}
		} else if (integer2 != null) {
			return integer1 + integer2;
		} else {
			return integer1;
		}
		return null;
	}

	public static Double getAverageOfTwo(Double double1, Double double2) {
		logger.debug("Integer Add Values double1 {} , double2 {} ", double1, double2);
		try {
			if (double1 == null) {
				if (double2 != null) {
					return double2;
				}
			} else if (double2 != null) {
				return (double1 + double2) / 2;
			} else {
				return double1;
			}
		} catch (Exception e) {
			logger.error("Error in getAverageOfTwo {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	public static BRTIExcelReportWrapper mergeData(BRTIExcelReportWrapper brtiOldData,
			BRTIExcelReportWrapper brtiNewData) {
		BRTIExcelReportWrapper wrapper = new BRTIExcelReportWrapper();
		try {
			if (brtiOldData == null) {
				return brtiNewData;
			} else if (brtiNewData != null) {
				wrapper.setCityName(brtiNewData.getCityName());
				wrapper.setFrequency(brtiNewData.getFrequency());
				wrapper.setSerivce(brtiNewData.getSerivce());
				wrapper.setTotalSms(addValues(brtiNewData.getTotalSms(), brtiOldData.getTotalSms()));
				wrapper.setSmsDeliivered(addValues(brtiNewData.getSmsDeliivered(), brtiOldData.getSmsDeliivered()));
				wrapper.setSmsDeliveryRate(
						getAverageOfTwo(brtiNewData.getSmsDeliveryRate(), brtiOldData.getSmsDeliveryRate()));
				wrapper.setTotalSmsOnnet(addValues(brtiNewData.getTotalSmsOnnet(), brtiOldData.getTotalSmsOnnet()));
				wrapper.setSmsDeliiveredOnnet(
						addValues(brtiNewData.getSmsDeliiveredOnnet(), brtiOldData.getSmsDeliiveredOnnet()));
				wrapper.setSmsDeliveryRateOnnet(
						getAverageOfTwo(brtiNewData.getSmsDeliveryRateOnnet(), brtiOldData.getSmsDeliveryRateOnnet()));
				wrapper.setTotalSmsOffnet(addValues(brtiNewData.getTotalSmsOffnet(), brtiOldData.getTotalSmsOffnet()));
				wrapper.setSmsDeliiveredOffnet(
						addValues(brtiNewData.getSmsDeliiveredOffnet(), brtiOldData.getSmsDeliiveredOffnet()));
				wrapper.setSmsDeliveryRateOffnet(getAverageOfTwo(brtiNewData.getSmsDeliveryRateOffnet(),
						brtiOldData.getSmsDeliveryRateOffnet()));
				wrapper.setDropcall(addValues(brtiNewData.getDropcall(), brtiOldData.getDropcall()));
				wrapper.setFailCall(addValues(brtiNewData.getFailCall(), brtiOldData.getFailCall()));
				wrapper.setTotalCall(addValues(brtiNewData.getTotalCall(), brtiOldData.getTotalCall()));
				wrapper.setTotalCallOnnet(addValues(brtiNewData.getTotalCallOnnet(), brtiOldData.getTotalCallOnnet()));
				wrapper.setDropcallOnnet(addValues(brtiNewData.getDropcallOnnet(), brtiOldData.getDropcallOnnet()));
				wrapper.setFailCallOnnet(addValues(brtiNewData.getFailCallOnnet(), brtiOldData.getFailCallOnnet()));
				wrapper.setFailCallOffnet(addValues(brtiNewData.getFailCallOffnet(), brtiOldData.getFailCallOffnet()));
				wrapper.setTotalCallOffnet(
						addValues(brtiNewData.getTotalCallOffnet(), brtiOldData.getTotalCallOffnet()));
				wrapper.setDropcallOffnet(addValues(brtiNewData.getDropcallOffnet(), brtiOldData.getDropcallOffnet()));
				wrapper.setCallEsaRate(getAverageOfTwo(brtiNewData.getCallEsaRate(), brtiOldData.getCallEsaRate()));
				wrapper.setCallDropRate(getAverageOfTwo(brtiNewData.getCallDropRate(), brtiOldData.getCallDropRate()));
				return wrapper;
			} else {
				return brtiOldData;
			}
		} catch (Exception e) {
			logger.error("Error in Merging Data {} ", Utils.getStackTrace(e));
		}
		return wrapper;
	}

	public static void autoSizeColumns(Workbook workbook) {
		int numberOfSheets = workbook.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			if (sheet.getPhysicalNumberOfRows() > 0) {
				Row row = sheet.getRow(0);
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					int columnIndex = cell.getColumnIndex();
					sheet.autoSizeColumn(columnIndex);
				}
			}
		}
	}

	public static List<BRTIExcelReportWrapper> getExcelWrapperList(Map<String, BRTIExcelReportWrapper> cityMap) {
		ArrayList<BRTIExcelReportWrapper> excelList = new ArrayList<>();
		if (cityMap != null) {
			for (Entry<String, BRTIExcelReportWrapper> entry : cityMap.entrySet()) {
				if ((entry.getValue() != null && entry.getValue().getTotalCall() != null
						&& entry.getValue().getTotalCall() > 0)
						|| (entry.getValue().getTotalSms() != null && entry.getValue().getTotalSms() > 0)) {
					excelList.add(entry.getValue());
				}
			}
		}
		return excelList;
	}

	public static BRTIExcelReportWrapper setInformationForCall(Double callEsaRate, Double callDropRate,
			BRTIExcelReportWrapper wrapper) {
		logger.info("inside the method setInformationForCall callEsaRate {}, callDropRate {}", callEsaRate,
				callDropRate);
		if (callEsaRate != null && callDropRate != null) {
			if (callEsaRate >= 90.0 && callDropRate <= 5.0) {
				wrapper.setInforamtion("Ok");
			} else {
				wrapper.setInforamtion("Not Ok");
			}
		} else {
			wrapper.setInforamtion(HYPHEN);
		}
		return wrapper;
	}

	public static BRTIExcelReportWrapper setInformationForSms(Double smsDeliveryRate, BRTIExcelReportWrapper wrapper) {
		if (smsDeliveryRate != null) {
			if (smsDeliveryRate >= 75.0) {
				wrapper.setInforamtion("Ok");
			} else {
				wrapper.setInforamtion("Not Ok");
			}
		} else {
			wrapper.setInforamtion(HYPHEN);
		}
		return wrapper;
	}

	public static String getQuarterMonthByNo(int i) {
		switch (i) {
		case INDEX_ONE:
			return "(January - March)";
		case INDEX_TWO:
			return "(April - June)";
		case INDEX_THREE:
			return "(July - September)";
		case FOUR_INDEX:
			return "(October - December)";
		default:
			return null;
		}
	}

	public static InputStream getInputStreamFromBufferedImage(BufferedImage bufferedImage) {
		if (bufferedImage != null) {
			try {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, JPG, os);
				return new ByteArrayInputStream(os.toByteArray());
			} catch (Exception e) {
				logger.info("Unable to convert BUfferedImage to inputStream {} ", e.getMessage());
			}
		}
		return null;
	}

	public static String createZipFileOnPath(AnalyticsRepository analyticObj, String filePath) {
		String zipFilePath = ConfigUtils.getString(NV_REPORTS_PATH) + FORWARD_SLASH + analyticObj.getName() + UNDERSCORE
				+ analyticObj.getId() + DOT_ZIP;
		logger.info("zipFilePath {}", zipFilePath);
		File destDir = new File(filePath);

		try {
			ZipUtils.zip(destDir.listFiles(), zipFilePath);
		} catch (IOException e) {
			logger.error("unable to create Zip File {}", Utils.getStackTrace(e));
		}
		return zipFilePath;
	}

	public static List<String[]> getFilteredDataByIndexValue(List<String[]> driveData, Integer index, String value) {
		List<String[]> filteredData = driveData;
		if (driveData != null && !driveData.isEmpty()) {
			filteredData = driveData.stream().filter(Objects::nonNull).filter(e -> index < e.length).filter(
					array -> (array[index] != null && !array[index].isEmpty() && array[index].equalsIgnoreCase(value)))
					.collect(Collectors.toList());
		}
		return filteredData;
	}

	public static List<String[]> convertCSVStringToDataListStealth(String data) {
		logger.info("insde the method convertCSVStringToDataListLiveDrive");
		List<String[]> arlist = new ArrayList<>();
		try {
			String[] ar = data.split("\\n", -1);

			List<String> list = Arrays.asList(ar);
			for (String string : list) {
				String[] arr = string.split(",", -1);
				arlist.add(arr);
			}
			arlist.remove(INDEX_ZER0);
		} catch (Exception e) {
			logger.error("error to cast string to String Array{}", Utils.getStackTrace(e));
		}
		return arlist;
	}

	public static Double round(Double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		if (NumberUtils.isValidNumber(value)) {
			BigDecimal bd = BigDecimal.valueOf(value);
			bd = bd.setScale(places, RoundingMode.HALF_UP);
			return bd.doubleValue();
		}
		return null;
	}

	public static String[] getHeadersFromStealthRawData(String rawData) {
		String[] headers = null;
		try {
			String[] ar = rawData.split("\\n", 1);
			List<String> list = Arrays.asList(ar);
			for (String string : list) {
				String[] arr = string.split(",", -1);
				headers = arr;
			}
		} catch (Exception e) {
			logger.info("error to cast string to String Array{}", Utils.getStackTrace(e));
		}
		return headers;
	}

	public static BufferedImage getCombinedBufferedImageFromTiles(List<String> imagesList) {
		try {
			BufferedImage img = ImageIO.read(new File(imagesList.get(INDEX_ZER0)));
			BufferedImage result = new BufferedImage(img.getWidth() * imagesList.size() + INDEX_HUNDRED,
					img.getHeight() * imagesList.size() + INDEX_HUNDRED, BufferedImage.TYPE_INT_RGB);
			Graphics g = result.getGraphics();
			int x = INDEX_ZER0;
			int y = INDEX_ZER0;
			for (String image : imagesList) {
				BufferedImage bi = ImageIO.read(new File(image));
				g.drawImage(bi, x, y, null);
				x += TWO_FIVE_SIX;
				if (x > result.getWidth()) {
					x = 0;
					y += bi.getHeight();
				}
			}
			return result;
		} catch (IOException e) {
			logger.error("IOException inside method getCombinedBufferedImageFromTiles image {} ",
					Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception inside method Combine Tiles Image {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	public static boolean isAtollImageRequiredForKpi(String kpiName) {
		return RSRP.equalsIgnoreCase(kpiName);
	}

	public static String converToCSVStringFromArrayString(String json) {
		json = json.replace(NVLayer3Utils.REPORT_JSON_START_STRING, Symbol.EMPTY_STRING);
		json = json.replaceAll(NVLayer3Utils.REPORT_JSON_LINE_SEPERATOR, Symbol.LINE_SEPARATOR_STRING);
		json = json.replaceAll(NVLayer3Utils.REPORT_JSON_END_STRING, Symbol.EMPTY_STRING);
		json = json.replace(NVLayer3Utils.REPORT_JSON_ARRAY_START_STRING, Symbol.EMPTY_STRING);
		json = json.replace(NVLayer3Utils.REPORT_JSON_ARRAY_END_STRING, Symbol.EMPTY_STRING);
		json = json.replace(NVLayer3Utils.REPORT_JSON_ARRAY_LAST_BRACES, Symbol.EMPTY_STRING);
		json = json.replace(NVLayer3Utils.REPORT_JSON_ARRAY_LAST_BRACES_STEALTH_2, Symbol.EMPTY_STRING);
		return json;
	}

	public static String getFileNameFromFilePath(String filePath) {
		if (filePath != null && !StringUtils.isBlank(filePath)) {
			return filePath.substring(filePath.lastIndexOf(Symbol.SLASH_FORWARD_STRING) + 1);
		}
		return null;
	}

	public static Map<String, Long> getTechnologyCountMap(DriveImageWrapper driveImageWrapper) {

		Integer indexTechnology = findKPIIndexFromKPIWrapper(driveImageWrapper, DriveHeaderConstants.TECHNOLOGY);

		List<String[]> filterdData = driveImageWrapper.getDataKPIs().stream().filter(
				e -> indexTechnology != null && e[indexTechnology] != null && !e[indexTechnology].trim().isEmpty())
				.collect(Collectors.toList());
		return filterdData.stream()
				.collect(Collectors.groupingBy(arrayData -> arrayData[indexTechnology], Collectors.counting()));

	}

	public static Map<String, Long> getDlBandWidthCountMap(DriveImageWrapper driveImageWrapper) {
		Integer indexDLBandwidth = findKPIIndexFromKPIWrapper(driveImageWrapper, DL_BANWIDTH);
		List<String[]> filterdData = driveImageWrapper.getDataKPIs().stream().filter(
				e -> indexDLBandwidth != null && e[indexDLBandwidth] != null && !e[indexDLBandwidth].trim().isEmpty())
				.collect(Collectors.toList());
		return filterdData.stream().filter(x -> indexDLBandwidth != null)
				.collect(Collectors.groupingBy(arrayData -> arrayData[indexDLBandwidth], Collectors.counting()));

	}

	public static void deleteAllFilesFromDirectory(String filePath) {
		File file = new File(filePath);
		if (file.exists() && file.isDirectory()) {
			File[] filesList = file.listFiles();
			for (File singleFile : filesList) {
				if (singleFile.isDirectory() && singleFile.listFiles().length > INDEX_ZER0) {
					deleteAllFilesFromDirectory(singleFile.getAbsolutePath());
				} else {
					boolean delete = singleFile.delete();
					logger.info("Delete Files {}",delete);
				}
			}
		}
	}

	public static boolean isTimeStampInRange(Long minTime, Long maxTime, Long timestamp) {
		logger.info("MinTime: {}, Maxtime: {}, StealthTime: {}", minTime, maxTime, timestamp);
		return minTime != null && maxTime != null && timestamp != null && timestamp >= minTime && timestamp <= maxTime;
	}

	public static void deleteFilesFromLocal(List<String> listOfFilePath) {
		for (String filePath : listOfFilePath) {
			try {
				boolean delete = new File(filePath).delete();
				logger.info("File Delete {}",delete);
			} catch (Exception e) {
				logger.error("Unable to delete the file from local {} ", listOfFilePath);
			}
		}
	}

	public static Date getDateFromString(String date, String dateFormat) {
		try {
			return new SimpleDateFormat(dateFormat).parse(date);
		} catch (java.text.ParseException e) {
			logger.error("ParseException while formatting date {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	public static List<HBaseResult> filterStealthDataByDate(Long startDate, Long endDate,
			List<HBaseResult> stealthData) {
		if (startDate != null && endDate != null) {
			List<HBaseResult> filteredData = new ArrayList<>();
			for (HBaseResult result : stealthData) {
				String dbDate = result.getString(DATE.getBytes());
				if (isStealthDateInRange(startDate, endDate, dbDate)) {
					filteredData.add(result);
				}
			}
			return filteredData;
		} else {
			return stealthData;
		}
	}

	private static boolean isStealthDateInRange(Long minDate, Long maxDate, String stealthDate) {
		if (minDate != null && maxDate != null && org.apache.commons.lang3.math.NumberUtils.isCreatable(stealthDate)) {
			Date startDate = new Date(minDate);
			Date endDate = new Date(maxDate);
			Date dbDate = getDateFromString(stealthDate, DATE_FORMAT_STEALTH_HBASE);
			if (dbDate != null) {
				logger.info("minDate: {}, maxDate: {}, stealthDate: {}", startDate.getTime(), endDate.getTime(),
						dbDate.getTime());
				logger.info("Date check result: {}", dbDate.compareTo(startDate) >= DATE_COMPARISON_NUMBER
						&& dbDate.compareTo(endDate) <= DATE_COMPARISON_NUMBER);
				return dbDate.compareTo(startDate) >= DATE_COMPARISON_NUMBER
						&& dbDate.compareTo(endDate) <= DATE_COMPARISON_NUMBER;
			}
		}
		return false;
	}

	public static InputStream getLegendColorImageForBenchmark(String colorCode) {
		BufferedImage image = new BufferedImage(BENCHMARK_LEGEND_IMAGE_WIDTH, BENCHMARK_LEGEND_IMAGE_HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.decode(colorCode));
		graphics.fillRect(INDEX_ZER0, INDEX_ZER0, BENCHMARK_LEGEND_IMAGE_WIDTH, BENCHMARK_LEGEND_IMAGE_HEIGHT);
		return getInputStreamFromBufferedImage(image);
	}

	public static List<RangeSlab> getSortedRangeSlabList(List<RangeSlab> rangeSlabList) {
		if (rangeSlabList != null && !rangeSlabList.isEmpty()) {
			Collections.sort(rangeSlabList, (o1, o2) -> o1.getLowerLimit().compareTo(o2.getLowerLimit()));
		}
		return rangeSlabList;
	}

	public static String getStealthFormattedDate(String date, String hour, String type) {
		if (!StringUtils.isBlank(type) && type.equals(NVConstant.HOURLY) && !StringUtils.isBlank(hour)) {
			return date + Symbol.SPACE_STRING + hour + Symbol.COLON_STRING + STEALTH_HOURLY_DEFAULT_MINUTES;
		}
		return date;
	}

	public static String getDateFormatForStealthReport(String date, boolean isReportingFormat, String type) {
		if (!isReportingFormat) {
			return !StringUtils.isBlank(type) && type.equals(NVConstant.HOURLY) ? STEALTH_HOURLY_DATE_FORMAT
					: DATE_FORMAT_STEALTH_HBASE;
		} else {
			return !StringUtils.isBlank(type) && type.equals(NVConstant.HOURLY) ? STEALTH_DATE_FORMAT_FOR_REPORT
					: DATE_FORMAT_DD_MM_YY;
		}
	}

	public static Map<String, LegendListWrapper> getRangeWiseLegendListWrapperMap(List<KPIWrapper> kpiWrapperList,
			ReportSubWrapper subWrapper) {
		Map<String, LegendListWrapper> rangeLegendMap = new HashMap<>();
		int count = 0;
		int noOfOperator = kpiWrapperList.size();
		for (KPIWrapper wrapper : kpiWrapperList) {
			setOperatorToBenchmarkSubWrapper(subWrapper, count, wrapper.getOperatorName());
			List<RangeSlab> sortedRangeSlabList = getSortedRangeSlabList(wrapper.getRangeSlabs());
			for (RangeSlab rangeSlab : sortedRangeSlabList) {
				String range = rangeSlab.getUpperLimit() + " to " + rangeSlab.getLowerLimit();
				if (rangeLegendMap.containsKey(range)) {
					setOperatorValueToLegendWrapper(count, rangeLegendMap.get(range),
							getPercentage(rangeSlab.getCount(), wrapper.getTotalCount()));
				} else {
					LegendListWrapper legendListWrapper = new LegendListWrapper();
					legendListWrapper.setLegendValue(range);
					legendListWrapper.setRemark(getLegendRemarkForRangeColor(wrapper.getKpiName(), range));
					legendListWrapper.setColorImage(getLegendColorImageForBenchmark(rangeSlab.getColorCode()));
					setOperatorValueToLegendWrapper(count, legendListWrapper,
							round(getPercentage(rangeSlab.getCount(), wrapper.getTotalCount()), TWO_DECIMAL_PLACES));
					rangeLegendMap.put(range, legendListWrapper);
				}
			}
			if (count < noOfOperator) {
				count++;
			}
		}
		return rangeLegendMap;
	}

	private static void setOperatorToBenchmarkSubWrapper(ReportSubWrapper subWrapper, Integer count,
			String operatorName) {
		if (count != null) {
			switch (count) {
			case INDEX_ZER0:
				subWrapper.setKey1(operatorName);
				break;
			case INDEX_ONE:
				subWrapper.setKey2(operatorName);
				break;
			case INDEX_TWO:
				subWrapper.setKey3(operatorName);
				break;
			case INDEX_THREE:
				subWrapper.setKey4(operatorName);
				break;
			default:
				break;
			}
		}
	}

	private static void setOperatorValueToLegendWrapper(Integer count, LegendListWrapper legendWrapper, Double value) {
		if (count != null) {
			switch (count) {
			case INDEX_ZER0:
				legendWrapper.setOperator1Value(value + Symbol.EMPTY_STRING);
				break;
			case INDEX_ONE:
				legendWrapper.setOperator2Value(value + Symbol.EMPTY_STRING);
				break;
			case INDEX_TWO:
				legendWrapper.setOperator3Value(value + Symbol.EMPTY_STRING);
				break;
			case INDEX_THREE:
				legendWrapper.setOperator4Value(value + Symbol.EMPTY_STRING);
				break;
			default:
				break;
			}
		}
	}

	public static String getLegendRemarkForRangeColor(String kpiName, String range) {
		try {
			switch (kpiName) {
			case RSRP:
				Map<String, String> rsrpMap = new JsonMapParser<String, String>()
						.convertJsonToMap(RSRP_LEGEND_REMARK_JSON);
				return rsrpMap.containsKey(range) ? rsrpMap.get(range) : null;
			case SINR:
				Map<String, String> sinrMap = new JsonMapParser<String, String>()
						.convertJsonToMap(SINR_LEGEND_REMARK_JSON);
				return sinrMap.containsKey(range) ? sinrMap.get(range) : null;
			case DL_THROUGHPUT:
				Map<String, String> dlMap = new JsonMapParser<String, String>().convertJsonToMap(DL_LEGEND_REMARK_JSON);
				return dlMap.containsKey(range) ? dlMap.get(range) : null;
			case UL_THROUGHPUT:
				Map<String, String> ulMap = new JsonMapParser<String, String>().convertJsonToMap(UL_LEGEND_REMARK_JSON);
				return ulMap.containsKey(range) ? ulMap.get(range) : null;
			case CQI:
				Map<String, String> cqiMap = new JsonMapParser<String, String>()
						.convertJsonToMap(CQI_LEGEND_REMARK_JSON);
				return cqiMap.containsKey(range) ? cqiMap.get(range) : null;
			default:
				return null;
			}
		} catch (Exception e) {
			logger.error("Unable to get remark for kpi: {}, Range: {}, Error: {}", kpiName, range,
					Utils.getStackTrace(e));
		}
		return null;
	}

	public static List<KpiRankWrapper> getRankListForKpi(List<Double> comparisonValue, int index,
			Map<String, List<String[]>> operatorWiseDriveDataMap, Integer kpiIndex) {
		List<KpiRankWrapper> kpiRankWrapperList = new ArrayList<>();
		for (Entry<String, List<String[]>> operatorDataEntry : operatorWiseDriveDataMap.entrySet()) {
			KpiRankWrapper kpiRankWrapper = new KpiRankWrapper();
			kpiRankWrapper.setOperatorName(operatorDataEntry.getKey());
			Double kpiAverage = round(getKPiAverage(kpiIndex, operatorDataEntry.getValue()), TWO_DECIMAL_PLACES);
			kpiRankWrapper.setAvgKpi(kpiAverage != null ? kpiAverage.toString() : null);
			Double kpiPercentage = getKPiPercentage(comparisonValue, kpiIndex, operatorDataEntry.getValue(), index);
			kpiRankWrapper.setKpiPercent(kpiPercentage != null
					? round(TOTAL_PERCENT - kpiPercentage, TWO_DECIMAL_PLACES) + Symbol.EMPTY_STRING
					: null);
			kpiRankWrapperList.add(kpiRankWrapper);
		}
		Collections.sort(kpiRankWrapperList, getKpiPercentComparator());
		return kpiRankWrapperList;
	}

	public static Double getKPiAverage(int kpiDataIndex, List<String[]> jsonStream) {
		Long sampleCounts;
		Double smapleCountSum;
		List<String[]> filteredStream = jsonStream.stream().filter(array -> array.length > kpiDataIndex)
				.filter(array -> (array[kpiDataIndex] != null && !array[kpiDataIndex].isEmpty()))
				.collect(Collectors.toList());
		sampleCounts = (long) filteredStream.size();
		smapleCountSum = filteredStream.stream().map(array -> Double.parseDouble(array[kpiDataIndex]))
				.mapToDouble(Double::doubleValue).sum();
		if (sampleCounts != null && smapleCountSum != null && sampleCounts != 0) {
			return smapleCountSum / sampleCounts;
		}
		return null;
	}

	public static Double getKPiPercentage(List<Double> comparisonValue, int kpiDataIndex, List<String[]> jsonStream,
			final int index) {
		int totalSamples;
		int samplesInCriteria;
		Stream<String[]> filteredStream = jsonStream.stream().filter(array -> array.length > kpiDataIndex)
				.filter(array -> (array[kpiDataIndex] != null && !array[kpiDataIndex].isEmpty()
						&& NumberUtils.isParsable(array[kpiDataIndex])));
		List<String[]> reList = filteredStream.collect(Collectors.toList());
		totalSamples = reList.size();
		samplesInCriteria = reList.stream()
				.filter(array -> (Double.parseDouble(array[kpiDataIndex]) >= comparisonValue.get(index)))
				.collect(Collectors.toList()).size();
		if (totalSamples != 0) {
			return round(getPercentage(samplesInCriteria, totalSamples), TWO_DECIMAL_PLACES);
		}
		return null;
	}

	public static Comparator<KpiRankWrapper> getKpiPercentComparator() {
		return (wrapper1, wrapper2) -> {
			if (wrapper1 != null && NumberUtils.isParsable(wrapper1.getKpiPercent()) && wrapper2 != null
					&& NumberUtils.isParsable(wrapper2.getKpiPercent())) {
				Double percent1 = Double.parseDouble(wrapper1.getKpiPercent());
				Double percent2 = Double.parseDouble(wrapper2.getKpiPercent());
				return percent1.compareTo(percent2);
			}
			return -1;
		};
	}

	public static List<LegendListWrapper> getSortedKpiLegendList(String kpiName,
			List<LegendListWrapper> kpiLegendList) {
		switch (kpiName) {
		case RSRP:
		case SINR:
		case DL_THROUGHPUT:
		case UL_THROUGHPUT:
		case CQI:
		case MOS:
		case BANDWIDTH_DL:
			Collections.sort(kpiLegendList, getLegendListComparator());
			Collections.reverse(kpiLegendList);
			break;
		default:
			break;
		}
		return kpiLegendList;
	}

	private static Comparator<LegendListWrapper> getLegendListComparator() {
		return (wrapper1, wrapper2) -> {
			if (wrapper1 != null && wrapper2 != null) {
				Double range1 = Double
						.parseDouble(wrapper1.getLegendValue()
								.substring(INDEX_ZER0,
										wrapper1.getLegendValue().indexOf(BENCHMARK_RANGE_COMPARATOR_CHARACTER) - 1)
								.trim());
				Double range2 = Double
						.parseDouble(wrapper2.getLegendValue()
								.substring(INDEX_ZER0,
										wrapper2.getLegendValue().indexOf(BENCHMARK_RANGE_COMPARATOR_CHARACTER) - 1)
								.trim());
				return range1.compareTo(range2);
			}
			return -1;
		};
	}

	public static String writeStreamToFile(InputStream inputStream, String imagePath) {
		int cursor;
		try (FileOutputStream out = new FileOutputStream(imagePath)) {
			while ((cursor = inputStream.read()) != -1) {
				out.write(cursor);
			}
			return imagePath;
		} catch (Exception e) {
			logger.info("Exception in writeStreamToFile {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	public static Map<String, Long> getDriveTimeStampMap(List<String[]> csvDataArray, Map<String, Integer> kpiIndexMap) {
		Map<String, Long> timestampMap = new HashMap<>();
		try {
			List<Long> driveTimeStampList = csvDataArray.stream()
					.filter(s -> (s[kpiIndexMap.get(ReportConstants.TIMESTAMP)] != null && !s[kpiIndexMap.get(ReportConstants.TIMESTAMP)].isEmpty()))
					.map(s -> Long.parseLong(s[INDEX_TWO])).collect(Collectors.toList());
			Optional<Long> minTimeOptional = driveTimeStampList.stream().min(Comparator.naturalOrder());
			Optional<Long> maxTimeOptional = driveTimeStampList.stream().max(Comparator.naturalOrder());
			if (minTimeOptional.isPresent()) {
				Long minTimeStamp = minTimeOptional.get();
				timestampMap.put(START_TIMESTAMP, minTimeStamp);

			}
			if (maxTimeOptional.isPresent()) {
				Long maxTimeStamp = maxTimeOptional.get();
				timestampMap.put(END_TIMESTAMP, maxTimeStamp);
			}
			return timestampMap;
		} catch (Exception e) {
			logger.error("Exception inside method getDriveTimeStampMap {} ", e.getMessage());
		}
		return timestampMap;
	}

	public static Map<String, Long> getDriveTimeStampMapForReports(List<String[]> csvDataArray,
			Map<String, Integer> kpiIndexMap) {
		Map<String, Long> timestampMap = new HashMap<>();
		try {
			Integer indexTimestamp = kpiIndexMap.get(ReportConstants.TIMESTAMP);
			if (indexTimestamp != null) {
				List<Long> driveTimeStampList = csvDataArray.stream()
						.filter(s -> (s[indexTimestamp] != null && !s[indexTimestamp].isEmpty()))
						.map(s -> Long.parseLong(s[indexTimestamp])).collect(Collectors.toList());

				Optional<Long> minTimeOptional = driveTimeStampList.stream().min(Comparator.naturalOrder());
				Optional<Long> maxTimeOptional = driveTimeStampList.stream().max(Comparator.naturalOrder());
				if (minTimeOptional.isPresent()) {
					Long minTimeStamp = minTimeOptional.get();
					timestampMap.put(START_TIMESTAMP, minTimeStamp);

				}
				if (maxTimeOptional.isPresent()) {
					Long maxTimeStamp = maxTimeOptional.get();
					timestampMap.put(END_TIMESTAMP, maxTimeStamp);
				}
			}
		} catch (Exception e) {
			logger.error("Exception inside method getDriveTimeStampMap {} ", e.getMessage());
		}
		return timestampMap;
	}

	public static Corner getCornerOf3DBoundary(List<List<List<Double>>> boundary) {
		Corner corner3D = new Corner();
		if (CollectionUtils.isNotEmpty(boundary)) {
			Iterator var2 = boundary.iterator();

			while (var2.hasNext()) {
				List<List<Double>> list = (List) var2.next();
				Corner corner2D = getCornerOfBoundary(list);
				corner3D.reduce(corner2D);
			}
		}

		return corner3D;
	}

	public static Corner getCornerOfBoundary(List<List<Double>> boundary) {
		Double minLat = 90.0D;
		Double maxLat = -90.0D;
		Double minLon = 180.0D;
		Double maxLon = -180.0D;
		Iterator iterateClusterBoundary = boundary.iterator();

		while (iterateClusterBoundary.hasNext()) {
			List<Double> point = (List) iterateClusterBoundary.next();
			if (!CollectionUtils.isEmpty(point) && point.size() >= 2) {
				Double lat = DoubleUtils.toDouble((Number) point.get(1));
				Double lon = DoubleUtils.toDouble((Number) point.get(0));
				if (lat != null && lon != null && lat != 0.0D && lon != 0.0D) {
					if (lat < minLat) {
						minLat = lat;
					}

					if (lat > maxLat) {
						maxLat = lat;
					}

					if (lon < minLon) {
						minLon = lon;
					}

					if (lon > maxLon) {
						maxLon = lon;
					}
				}
			}
		}

		Corner corner = new Corner();
		corner.setMinLatitude(minLat);
		corner.setMaxLatitude(maxLat);
		corner.setMinLongitude(minLon);
		corner.setMaxLongitude(maxLon);
		return corner;
	}

	public static String getDataFromStealthHeaders(String headers, Integer index) {
		return NVLayer3Utils.getStringFromCsv(headers.split(Symbol.COMMA_STRING), index);
	}

	public static String addDateToStealthCSVData(String csvData) {
		StringBuilder builder = new StringBuilder();
		String[] csvRows = csvData.split(Symbol.LINE_SEPARATOR_STRING);
		for (String csvRow : csvRows) {
			String date = getDateFromDumpCSV(csvRow);
			StringBuilder sb = new StringBuilder();
			sb.append(date + NVLayer3Constants.CSV_COLUMN_SEPERATOR);
			sb.append(csvRow + NVLayer3Constants.NEW_LINE_SEPERATOR);
			builder.append(sb.toString());
		}
		return builder.toString();
	}

	private static String getDateFromDumpCSV(String csvRow) {
		String[] dumpData = csvRow.split(NVLayer3Constants.CSV_COLUMN_SEPERATOR);
		if (dumpData != null && dumpData.length > INDEX_STEALTH_CSV_DUMP_TIMESTAMP + 6) {
			String timeStamp = dumpData[INDEX_STEALTH_CSV_DUMP_TIMESTAMP + 6];
			return NVLayer3Utils.getDateFromTimestamp(timeStamp);
		}
		return Symbol.EMPTY_STRING;
	}

	public static String addHeadersDataToCsv(String headers, String csvContent) {
		StringBuilder builder = new StringBuilder();
		String[] csvRows = csvContent.split(Symbol.LINE_SEPARATOR_STRING);
		for (String csvRow : csvRows) {
			StringBuilder sb = new StringBuilder();
			sb.append(getDataFromStealthHeaders(headers, StealthConstants.STEALTH_HEADERS_VERSION_INDEX)
					+ NVLayer3Constants.CSV_COLUMN_SEPERATOR);
			sb.append(getDataFromStealthHeaders(headers, StealthConstants.STEALTH_HEADERS_DEVICE_ID_INDEX)
					+ NVLayer3Constants.CSV_COLUMN_SEPERATOR);
			sb.append(getDataFromStealthHeaders(headers, StealthConstants.STEALTH_HEADERS_MAKE_INDEX)
					+ NVLayer3Constants.CSV_COLUMN_SEPERATOR);
			sb.append(getDataFromStealthHeaders(headers, StealthConstants.STEALTH_HEADERS_MODEL_INDEX)
					+ NVLayer3Constants.CSV_COLUMN_SEPERATOR);
			sb.append(getDataFromStealthHeaders(headers, StealthConstants.STEALTH_HEADERS_OS_INDEX)
					+ NVLayer3Constants.CSV_COLUMN_SEPERATOR);
			sb.append(getDataFromStealthHeaders(headers, StealthConstants.STEALTH_HEADERS_DUAL_SIM_INDEX)
					+ NVLayer3Constants.CSV_COLUMN_SEPERATOR);
			sb.append(csvRow + NVLayer3Constants.NEW_LINE_SEPERATOR);
			builder.append(sb.toString());
		}
		return builder.toString();
	}

	public static List<SiteInformationWrapper> getSiteFromMap(Map<String, Object> siteDataMap) {
		return siteDataMap.get(ReportConstants.SITE_INFO_LIST) != null
				? (List<SiteInformationWrapper>) siteDataMap.get(ReportConstants.SITE_INFO_LIST)
				: null;

	}

	public static Map<String, RecipeMappingWrapper> getCategoryWiseRecipeMappinWrappermap(
			Map<String, RecipeMappingWrapper> map) {
		Map<String, RecipeMappingWrapper> finalMap = new HashMap<>();

		for (Entry<String, RecipeMappingWrapper> wrapper1 : map.entrySet()) {
			if (wrapper1.getKey().equalsIgnoreCase(ReportConstants.RECIPE_CATEGORY_STATIONARY)) {
				finalMap.put(wrapper1.getKey(), map.get(wrapper1.getKey()));
			} else {
				populateDriveCallRecipe(finalMap, wrapper1);
			}
		}
		logger.info("Inside getCategoryWiseRecipeMappinWrappermap returning Map Size : => {} ", finalMap.size());
		return finalMap;
	}

	public static void populateDriveCallRecipe(Map<String, RecipeMappingWrapper> finalMap,
			Entry<String, RecipeMappingWrapper> wrapper1) {
		if (wrapper1.getKey().equalsIgnoreCase(ReportConstants.RECIPE_CATEGORY_DRIVE)
				|| wrapper1.getKey().equalsIgnoreCase(ReportConstants.CALL_SMALL)) {
			RecipeMappingWrapper oldwrapper = finalMap.get(ReportConstants.RECIPE_CATEGORY_DRIVE);
			if (oldwrapper != null) {
				List<String> list = oldwrapper.getRecpiList();
				list.addAll(wrapper1.getValue() != null ? wrapper1.getValue().getRecpiList() : null);
				oldwrapper.setRecpiList(list);
				finalMap.put(ReportConstants.RECIPE_CATEGORY_DRIVE, oldwrapper);
			} else {
				finalMap.put(ReportConstants.RECIPE_CATEGORY_DRIVE, wrapper1.getValue());
			}
		}
	}

	public static String getTempFilePath() {
		return ConfigUtils.getString(ReportConstants.IMAGE_PATH_FOR_NV_REPORT)
				+ ReportUtil.getFormattedDate(new Date(), ReportConstants.DATE_FORMAT_DD_MM_YY_HH_SS)
				+ ReportConstants.FORWARD_SLASH;
	}

	public static void setTechnologyFromBand(GenericWorkorder workorderObj, SSVTReportWrapper mainWrapper) {
		if (workorderObj != null) {
			Map<String, String> geoMap = workorderObj.getGwoMeta();

			String band = geoMap.get("band");
			if (band != null && band.equalsIgnoreCase("2300")) {
				mainWrapper.setTechnology(ReportConstants.TDD);
			} else if (band != null && band.equalsIgnoreCase("850")) {
				mainWrapper.setTechnology(ReportConstants.FDD);
			}
		}
	}

	public static List<GraphWrapper> setGraphDataForKpi(KPIWrapper kpiWrapper, List<Double> dataList, String kpiName) {
		logger.info("inside the method setGraphDataForKpi  {} ,totalcount {}", kpiWrapper.getKpiName(),
				kpiWrapper.getTotalCount());
		GraphWrapper graphWrapper = new GraphWrapper();
		List<GraphDataWrapper> graphDataList = new ArrayList<>();
		List<GraphWrapper> graphList = new ArrayList<>();
		Statistics staticstic = new Statistics(dataList);
		try {
			graphWrapper.setMax(staticstic.getMax());
			graphWrapper.setMin(staticstic.getMin());
			graphWrapper.setStDev(staticstic.getStdDev());
			graphWrapper.setVariance(staticstic.getVariance());
			graphWrapper.setMean(staticstic.getMean());
			graphWrapper.setKpiName(kpiName);
			getThresholdValue(graphWrapper, dataList, kpiWrapper);
			graphWrapper.setCount(kpiWrapper.getTotalCount());
			graphWrapper.setTargetValue(kpiWrapper.getTargetValue());
			setGraphDataToList(kpiWrapper, graphDataList, kpiWrapper.getRangeSlabs());
			graphDataList.sort(Comparator.comparing(GraphDataWrapper::getFrom).reversed());
			graphWrapper.setGraphDataList(graphDataList);
			graphList.add(graphWrapper);
			return graphList;
		} catch (Exception e) {
			logger.error("Exception inside the method setGraphDataForKpi {}", Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}

	public static List<SiteInformationWrapper> filterNEDataByBand(List<SiteInformationWrapper> siteInfoList,
			String band) {
		try {
			return siteInfoList.stream()
					.filter(c -> c.getNeFrequency() != null && c.getNeFrequency().equalsIgnoreCase(band))
					.collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("Exception inside method filterNEDataByBand {}", Utils.getStackTrace(e));
		}
		return siteInfoList;
	}

	public static String findBandDetailByGWOMetaData(GenericWorkorder genricWorkOrder) {
		return genricWorkOrder.getGwoMeta().get(NVWorkorderConstant.BAND) != null
				? findBandByGWOMeta(genricWorkOrder.getGwoMeta().get(NVWorkorderConstant.BAND))
				: null;
	}

	public static String getTechnologyForBand(String band) {
		Integer bandValue = null;
		if (band != null && !StringUtils.isBlank(band)) {
			bandValue = Integer.parseInt(band);
		}
		if (bandValue != null) {
			if (bandValue <= 31) {
				return "FDD";
			} else {
				return "TDD";
			}
		}
		return null;
	}

	public static String findBandByGWOMeta(String band) {
		if (band.equalsIgnoreCase(FDD3)) {
			band = ForesightConstants.BAND1800;
		} else if (band.equalsIgnoreCase(FDD5)) {
			band = ForesightConstants.BAND850;
		} else if (band.equalsIgnoreCase(TDD40)) {
			band = ForesightConstants.BAND2300;
		} else {
			return band;
		}
		logger.info("Going to return band {}", band);
		return band;
	}

	public static void addTestTypeInEmptyFields(List<String[]> driveData, Map<String, Integer> kpiIndexMap) {
		String previousTestType = null;
		Integer indexTestType = kpiIndexMap.get(ReportConstants.TEST_TYPE);
		if (Utils.isValidList(driveData)) {
			for (String[] rowData : driveData) {
				if (rowData != null && indexTestType != null) {
					if (rowData.length > indexTestType && !StringUtils.isBlank(rowData[indexTestType])
							&& Utils.hasValidValue(rowData[indexTestType])) {
						previousTestType = rowData[indexTestType];
					} else if (previousTestType != null) {
						rowData[indexTestType] = previousTestType;
					}
				}
			}
		}
	}
	public static Map<String, String> convertStringToJsonObjectForGeography(String geographyConfig) {
		JSONParser jsonParser = new JSONParser();
		Map<String, String> mapOfGeography = new HashMap<>();
		try {
			logger.info("geographyConfig -->> {}", geographyConfig);
			JSONObject jsonObject = (JSONObject) jsonParser.parse(geographyConfig);
			logger.info("jsonObject -> {}", jsonObject);
			JSONParser jsonParseL4 = new JSONParser();
			JSONObject jsonGegraphyL4Object = (JSONObject) jsonParseL4.parse(
					jsonObject.get(ForesightConstants.GeographyL4).toString());
			logger.info("jsonGegraphyL4Object.get(0)  -> {}", jsonGegraphyL4Object.get("name"));
			if (jsonGegraphyL4Object.get("name") != null) {
				mapOfGeography.put(ForesightConstants.GeographyL4, String.valueOf(jsonGegraphyL4Object.get("name")));
			} else {
				JSONParser jsonParseL3 = new JSONParser();
				JSONObject jsonGegraphyL3Object = (JSONObject) jsonParseL3.parse(
						jsonObject.get(ForesightConstants.GeographyL3).toString());
				mapOfGeography.put(ForesightConstants.GeographyL3, String.valueOf(jsonGegraphyL3Object.get("name")));
			}
			return mapOfGeography;
		} catch (Exception e) {
			logger.error("Error inside the method convertStringToJsonObjectForGeography for json {} ",
					Utils.getStackTrace(e));
		}
		return null;
	}
	/**
	 * Check if file exists.
	 *
	 * @param destinationFileName
	 *            the destination file name
	 * @return the file
	 */
	public static File checkIfFileExists(String destinationFileName) {
		logger.debug("going to checkIfFileExists for name: " + destinationFileName);
		try {
			File file = new File(destinationFileName);
			if (file.exists() && !file.isDirectory()) {
				logger.debug("file coverage report exists FILE_NAME: " + destinationFileName);
				return file;
			}
		} catch (Exception e) {
			logger.error("Exception on checkIfFileExists: " + e.getMessage());
		}
		logger.debug("file does not exists hence going to return null from checkIfFileExists for file name: "
				+ destinationFileName);
		return null;
	}

	public static void addCustomIndexDataToDriveDataArray(List<String[]> driveDataList,
			Map<Integer, Integer> kpiRelicationMap) {
		for (String[] driveData : driveDataList) {
			if (driveData != null) {
				for (Entry<Integer, Integer> replicationMapEntry : kpiRelicationMap.entrySet()) {
					if (driveData.length > replicationMapEntry.getKey() && !StringUtils.isBlank(
							driveData[replicationMapEntry.getValue()])) {
						driveData[replicationMapEntry.getKey()] = driveData[replicationMapEntry.getValue()];
					}
				}
			}
		}
	}

	public static String getKpiSuccessRate(Integer attemptCount, Integer successCount, boolean needStatus,
			Double threshold) {
		Double successRate = ReportUtil.round(ReportUtil.getPercentage(successCount, attemptCount),
				ReportConstants.TWO_DECIMAL_PLACES);
		if (needStatus && NumberUtils.isValidNumber(threshold) && NumberUtils.isValidNumber(successRate)) {
			return successRate >= threshold ?
					ReportConstants.PASS :
					ReportConstants.FAIL + Symbol.PARENTHESIS_OPEN_STRING + successCount + Symbol.SLASH_FORWARD_STRING
							+ attemptCount + Symbol.PARENTHESIS_CLOSE_STRING;
		} else if (NumberUtils.isValidNumber(successRate)) {
			return successRate + Symbol.PARENTHESIS_OPEN_STRING + successCount + Symbol.SLASH_FORWARD_STRING
					+ attemptCount + Symbol.PARENTHESIS_CLOSE_STRING;
		}
		return null;
	}

	public static Comparator<String[]> getTimestampComparator(Integer timestampIndex) {
		return new Comparator<String[]>() {
			@Override
			public int compare(String[] o1, String[] o2) {
				Long o1Timestamp = Long.parseLong(o1[timestampIndex]);
				Long o2Timestamp = Long.parseLong(o2[timestampIndex]);
				return o1Timestamp.compareTo(o2Timestamp);
			}
		};
	}
	
	public static List<Get> getHandoverQueryList(Integer workorderId, List<String> recipeList,
			List<String> operatorList) {
		List<Get> getList = new ArrayList<>();
		for (String recipeId : recipeList) {
			String rowkey = NVLayer3Utils.getRowKeyForLayer3PPE(workorderId, recipeId);
			logger.info("rowkey =====" + rowkey);
			Get get = new Get(Bytes.toBytes(rowkey));
			get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(),
					LAYER3_REPORT_COLUMN_HANDOVER.getBytes());
			getList.add(get);

		}
		return getList;
	}

	public static String processJsonToCsv(List<String> csvHeaders, List<String[]> driveData, Map<String, Integer> kpiIndexMap) {
		StringBuilder builder = new StringBuilder();
		builder.append(csvHeaders.stream().collect(Collectors.joining(Symbol.COMMA_STRING)));
		builder.append(Symbol.LINE_SEPARATOR_STRING);
		for (String[] singleRow : driveData) {
			List<String> row = new ArrayList();
			if (singleRow != null && singleRow.length > INDEX_ZER0 && kpiIndexMap.containsKey(TIMESTAMP)) {
				String timestamp = singleRow[kpiIndexMap.get(TIMESTAMP)];
				if (NumberUtils.isParsable(timestamp)) {
					row.add(getFormattedDate(new Date(Long.parseLong(timestamp)), DATE_FORMAT_DD_MM_YY_SS_AA));
				}
			}
			Collections.addAll(row, singleRow);
			builder.append(row.stream().collect(Collectors.joining(Symbol.COMMA_STRING)));
			builder.append(Symbol.LINE_SEPARATOR_STRING);
		}
		return builder.toString();
	}

	public static List<String[]> addKPIInEmptyFields(List<String[]> driveData, Integer index) {
		
		List<String[]> backwordList =new ArrayList<>();
		List<String[]> newList =new ArrayList<>();

		String previousKpi = null;
		String firstValue=null;
		if (index != null && Utils.isValidList(driveData)) {
			for (String[] rowData : driveData) {
				if (rowData != null) {
					if (rowData.length > index && !com.inn.commons.lang.StringUtils.isBlank(rowData[index])
							&& !rowData[index].isEmpty()) {
						previousKpi = rowData[index];
						if (firstValue == null) {
							firstValue = previousKpi;
						}
						newList.add(rowData);
					} else if (previousKpi != null) {
						rowData[index] = previousKpi;
						newList.add(rowData);

					} else {
						backwordList.add(rowData);
					}
				}
			}
		}
		
		if (Utils.isValidList(backwordList)) {
			for (String[] strings : backwordList) {
				strings[index] = firstValue;
			}
			backwordList.addAll(newList);
			logger.info("inside method addKPIInEmptyFields going to return backwordList {}", backwordList.size());
			return backwordList;
		}
		else if(Utils.isValidList(newList)) {
			logger.info("inside method addKPIInEmptyFields going to return newList {}",newList.size());
			return newList;
		}
		else {
			logger.info("inside method addKPIInEmptyFields going to return dataList");

			return driveData;
		}
	}

	public static List<String[]> getHandoverDataListFromHBaseResult(List<HBaseResult> resultList) {
		logger.info("Inside Method getHandoverDataListFromHBaseResult with data resultList size: {}",
				resultList.size());
		List<String[]> handoverDataList = new ArrayList<>();
		if (Utils.isValidList(resultList)) {
			for (HBaseResult result : resultList) {
				if (result != null) {
					String data = result.getString(LAYER3_REPORT_COLUMN_HANDOVER);
					if (!StringUtils.isBlank(data) && !LAYER3_REPORT_COLUMN_HANDOVER.equalsIgnoreCase(data)) {
						logger.info("Single String result: {}", data);
						List<String[]> convertedData = convertCSVStringToDataListForHandoverData(data);
						logger.info("size of convertedData {}", convertedData.size());
						handoverDataList.addAll(convertedData);
					}
				}
			}
		}
		logger.info("Returning Final HO data List: {}", new Gson().toJson(handoverDataList));
		return handoverDataList;
	}
	
	private static List<String[]> convertCSVStringToDataListForHandoverData(String data) {
		logger.info("inside the method convertCSVStringToDataList");
		List<String[]> arList = new ArrayList<>();
		try {
			if (data != null) {
				String[] ar = data.split("], \\[", -1);
				List<String> list = Arrays.asList(ar);
				for (String string : list) {
					String[] arr = string.replaceAll("]]", "").replace("//]", "").replaceAll("\\[", "").split(",", -1);
					arList.add(arr);
				}
			}
		} catch (Exception e) {
			logger.error("errorConvertingStringToDataList {}", Utils.getStackTrace(e));
		}
		return arList;
	}

	public static void addPciInEmptyFields(List<String[]> driveData, Map<String, Integer> kpiIndexMap) {
		Integer previousPci = null;
		Integer indexPCI = kpiIndexMap.get(ReportConstants.PCI_PLOT);
		if (Utils.isValidList(driveData)) {
			for (String[] rowData : driveData) {
				if (rowData != null && indexPCI != null) {
					if (rowData.length > indexPCI && !com.inn.commons.lang.StringUtils.isBlank(rowData[indexPCI])
							&& org.apache.commons.lang3.math.NumberUtils.isCreatable(rowData[indexPCI])) {
						previousPci = Integer.parseInt(rowData[indexPCI]);
					} else if (previousPci != null) {
						rowData[indexPCI] = previousPci.toString();
					}
				}
			}
		}
	}
	
	public static void addTechnologyInEmptyFields(List<String[]> driveData, Map<String, Integer> kpiIndexMap) {
		String previousTech = null;
		Integer indexTech = kpiIndexMap.get(ReportConstants.TECHNOLOGY);
		if (Utils.isValidList(driveData)) {
			for (String[] rowData : driveData) {
				if (rowData != null && indexTech != null) {
					if (rowData.length > indexTech && rowData[indexTech] != null
							&& !com.inn.commons.lang.StringUtils.isBlank(rowData[indexTech])) {
						previousTech = rowData[indexTech];
					} else if (previousTech != null) {
						rowData[indexTech] = previousTech;
					}
				}
			}
		}
	}

	
	//Mutable constants moved hear to fix sonar issue
	protected static final String[] BRTI_EXCEL_REPORT_CALL_HEADER = { "  ", "#Call", "#Fail", "#Drop",
			"ESA Rate (≥ 90%)", "Drop Rate (≤5%)" };
	protected static final String[] BRTI_EXCEL_REPORT__CALL_HEADER_TYPE2 = { TOTAL, "Fail", "Drop", "ESA Rate (≥ 90%)",
	"Drop Rate (≤5%)" };
	protected static final String[] BRTI_EXCEL_REPORT__SMS_HEADER = { "  ", "#SMS", "Delivered",
	"Delivered Rate (≥ 90%)" };
	protected static final String[] BRTI_EXCEL_REPORT__SMS_HEADER_TYPE2 = { TOTAL, "Delivered",
	"Delivered Rate (≥ 90%)" };

	public static final List<String> BRTI_EXCEL_REPORT__TOTAL_TABLE_HEADER1 = Collections.unmodifiableList(Arrays.asList(new String[]{ "   ", "Voice Service", "Total", "Fail",
			"Drop", "ESA Rate (%)", "Drop Rate (%)", "SMS Service", TOTAL, "Delivered", "Delivered Rate (%)" }));
	public static final List<String> BRTI_EXCEL_REPORT__TOTAL_TABLE_HEADER2 = Collections.unmodifiableList(Arrays.asList(new String[]{ "DGPT Target", "   ", "   ", "  ", "   ",
			"ESA rate (≥ 90%)", "Drop Rate (≤ 5%)", "   ", "   ", "    ", "Delivery Rate (≥ 90%)" }));

	protected static final String[] BRTI_QOERTLY_REPORT__SMS_HEADER = { "Frequency", "Service", " Type ", "Start Date",
			"End Date", "Place", "Total SMS", "Sms Deliver <3 Min", "Delivery Rate >= 75%", "Information" };
	protected static final String[] BRTI_QOERTLY_SMS_HEADER = { "Operator", "Date & Time", "No", "#A", "#B",
			"Time duration untill success (s)", "Succcess OK or NOK", "Fail" };

	protected static final String[] RSRPRANGES = { "-40 to -90", "-90 to -95", "-95 to -100", "-100 to -105",
			"-105 to -113", "-113 to -140" };
	protected static final String[] SINRRANGES = { "30 to 25", "25 to 20", "20 to 10", "10 to 0", "0 to -2", "-2 to -6",
	"-6 to -20" };
	protected static final String[] DLULRANGES = { "500 to 26", "26 to 20", "20 to 10", "10 to 6", "6 to 2", "2 to 0" };
	protected static final String[] PDSCHPUSCH = { "500 to 26", "26 to 20", "20 to 10", "10 to 6", "6 to 2", "2 to 0" };
	protected static final String[] SCORE = { "Good", "Average", "Bad" };

	protected static final String[] BRTI_QOERTLY_REPORT_CALL_HEADER = { "Frequency", HEADER_SERVICE, " Type ",
			"Start Date", "End Date", "Place", "Total Call", "Success Call", "Fail Call", "Drop Call",
			"ESA Rate(>=90%)", "Drop Rate (<=5%)", "Information" };

}
