package com.inn.foresight.core.infra.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.service.ITabFileParserService;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.wrapper.GeographyBoundaryPolygonWrapper;
import com.inn.foresight.core.infra.wrapper.GeographyBoundaryWrapper;

/**
 * The Class TabFileParserServiceImpl.
 */
@Service("TabFileParserServiceImpl")
public class TabFileParserServiceImpl extends AbstractService<Integer, Object> implements ITabFileParserService {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(TabFileParserServiceImpl.class);

	/**
	 * Parses the tab file for boundaries.
	 *
	 * @param filePath
	 *            the file path
	 * @return the map
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public Map<String, GeographyBoundaryWrapper> parseTabFileForBoundaries(String filePath) {
		logger.info("Going to parse TabFile for Boundary for filePath {} ", filePath);
		Map<String, GeographyBoundaryWrapper> tabFileParserMap = null;
		try {
			if (filePath != null && !filePath.isEmpty()) {
				ArrayList<Double> coords = null;
				List<List<Double>> coordinatesList = null;
				List<String> coordinatesArrayList = null;
				List<Double> coordinateVillageList = null;
				List<List<List<Double>>> polygon3d = null;
				GeographyBoundaryWrapper geographyBoundaryWrapper = null;
				tabFileParserMap = new HashMap<>();
				GeographyBoundaryPolygonWrapper geographyBoundaryPolygonWrapper = null;
				List<GeographyBoundaryPolygonWrapper> geographyBoundaryPolygonWrappers = null;
				InputStream inputStream = new FileInputStream(filePath);
				Reader reader = new InputStreamReader(inputStream, InfraConstants.UTF_8_ENCODE);
				InputSource inputSource = new InputSource(reader);
				inputSource.setEncoding(InfraConstants.UTF_8_ENCODE);

				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = dBuilder.parse(inputSource);
				document.getDocumentElement().normalize();
				NodeList placemarkNodeList = document.getElementsByTagName(InfraConstants.PLACEMARK_TAG);
				if (placemarkNodeList != null && placemarkNodeList.getLength() > 0) {
					for (int placemarkKey = 0; placemarkKey < placemarkNodeList.getLength(); placemarkKey++) {
						try {
							geographyBoundaryWrapper = new GeographyBoundaryWrapper();
							Node placemarkNode = placemarkNodeList.item(placemarkKey);
							if (placemarkNode.getNodeType() == Node.ELEMENT_NODE) {
								Element placeMarkElement = (Element) placemarkNode;
								NodeList extendedData = placeMarkElement.getElementsByTagName(InfraConstants.EXTENDEDDATA_TAG);
								Element extendedDataElement = (Element) extendedData.item(0);
								NodeList schemaData = extendedDataElement.getElementsByTagName(InfraConstants.SCHEMADATA_TAG);
								Element schemaDataElement = (Element) schemaData.item(0);
								NodeList simpleData = schemaDataElement.getElementsByTagName(InfraConstants.SIMPLEDATA_TAG);
								for (int simpleDataKey = 0; simpleDataKey < simpleData.getLength(); simpleDataKey++) {
									Element simpleDataElement = null;
									try {
										simpleDataElement = (Element) simpleData.item(simpleDataKey);

										simpleDataElement.getElementsByTagName(InfraConstants.BLANK_STRING);
										String attribute = simpleDataElement.getAttribute(InfraConstants.BOUNDARY_NAME);
										String textContent = simpleData.item(simpleDataKey).getTextContent();
										setGeographiesForAllBoundary(attribute, textContent, geographyBoundaryWrapper);
									} catch (Exception exception) {
										logger.warn("Error in set Geographies For Boundary {} Message {} ", simpleDataElement, exception.getMessage());
									}
								}
								NodeList polygonList = placeMarkElement.getElementsByTagName(InfraConstants.POLYGON_TAG);
								if (polygonList != null && polygonList.getLength() > 0) {
									geographyBoundaryPolygonWrappers = new ArrayList<>();
									populatePolygon3DDataForAllBoundary(polygonList, geographyBoundaryPolygonWrappers, geographyBoundaryPolygonWrapper, geographyBoundaryWrapper, coordinatesArrayList,
											coordinatesList, coords, polygon3d);
								} else {
									NodeList pointList = placeMarkElement.getElementsByTagName(InfraConstants.POINT_TAG);
									if (pointList != null && pointList.getLength() > 0) {
										geographyBoundaryPolygonWrappers = new ArrayList<>();
										for (int pointKey = 0; pointKey < pointList.getLength(); pointKey++) {
											try {
												geographyBoundaryPolygonWrapper = new GeographyBoundaryPolygonWrapper();
												Node polygon = pointList.item(pointKey);
												Element polygonElement = (Element) polygon;
												int len = polygonElement.getElementsByTagName(InfraConstants.COORDINATES).getLength();
												List<Double> polygon3DList = new ArrayList<>();
												for (int index = 0; index < len; index++) {
													try {
														String coordinateContent = polygonElement.getElementsByTagName(InfraConstants.COORDINATES).item(index).getTextContent();
														if (coordinateContent.contains(InfraConstants.SPACE_KML)) {
															generatePolygonForBoundaries(coordinateContent, coordinatesList, coordinatesArrayList, coords, polygon3d);
														} else if (coordinateContent.contains(InfraConstants.COMMA)) {
															coordinateVillageList = new ArrayList<>();
															String coordinates[] = coordinateContent.split(InfraConstants.COMMA);
															coordinatesArrayList = new ArrayList<>(Arrays.asList(coordinates));
															if (coordinatesArrayList.size() >= 2) {
																coordinateVillageList.add(Double.parseDouble(coordinates[0].trim()));
																coordinateVillageList.add(Double.parseDouble(coordinates[1].trim()));
																polygon3DList.addAll(coordinateVillageList);
																geographyBoundaryPolygonWrapper.setPolygonVillageList(polygon3DList);
																geographyBoundaryPolygonWrappers.add(geographyBoundaryPolygonWrapper);
																geographyBoundaryWrapper.setGeographyBoundaryPolygonWrappers(geographyBoundaryPolygonWrappers);
															}
														}
													} catch (Exception exception) {
														logger.warn("Error in getting point for Boundary Messgae {} ", exception.getMessage());
													}
												}
											} catch (Exception exception) {
												logger.warn("Error in getting point for Boundary Messgae {} ", exception.getMessage());
											}
										}
									}
								}
							}
							GeographyBoundaryWrapper boundaryWrapper = tabFileParserMap.get(geographyBoundaryWrapper.getCheckKey());
							if (boundaryWrapper != null) {
								List<GeographyBoundaryPolygonWrapper> previousPolygonWrapper = boundaryWrapper.getGeographyBoundaryPolygonWrappers();
								previousPolygonWrapper.addAll(geographyBoundaryWrapper.getGeographyBoundaryPolygonWrappers());
								boundaryWrapper.setGeographyBoundaryPolygonWrappers(previousPolygonWrapper);
								tabFileParserMap.put(geographyBoundaryWrapper.getCheckKey(), boundaryWrapper);
							} else {
								tabFileParserMap.put(geographyBoundaryWrapper.getCheckKey(), geographyBoundaryWrapper);
							}
						} catch (Exception exception) {
							logger.warn("Error in getting PlacemarkNode For {} ", placemarkKey);
						}
					}
				} else {
					logger.warn("No placemarkNodeList Found for {} ", filePath);
				}
			} else {
				throw new RestException("Unable to parseTabFile for Boundary for filePath " + filePath);
			}
		} catch (Exception exception) {
			logger.info("Unable to parseTab File for filePath {} Exception {} ", filePath, ExceptionUtils.getStackTrace(exception));
		}
		return tabFileParserMap;
	}

	/**
	 * Sets the geographies for all boundary.
	 *
	 * @param attribute
	 *            the attribute
	 * @param textContent
	 *            the text content
	 * @param geographyBoundaryWrapper
	 *            the geography boundary wrapper
	 */
	private static void setGeographiesForAllBoundary(String attribute, String textContent, GeographyBoundaryWrapper geographyBoundaryWrapper) {
		if (attribute.trim().toUpperCase().equals(InfraConstants.OBJECTID_1.trim().toUpperCase())) {
			geographyBoundaryWrapper.setObjectId1(textContent);
			geographyBoundaryWrapper.setCheckKey(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.PROVINSI.trim().toUpperCase())) {
			geographyBoundaryWrapper.setProvinsi(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.PENDUDUK.trim().toUpperCase())) {
			geographyBoundaryWrapper.setPenduduk(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.LUAS.trim().toUpperCase())) {
			geographyBoundaryWrapper.setLuas(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.KEPADATAN.trim().toUpperCase())) {
			geographyBoundaryWrapper.setKepadatan(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.PSTN.trim().toUpperCase())) {
			geographyBoundaryWrapper.setPstn(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.SHAPE_LENGTH.trim().toUpperCase())) {
			geographyBoundaryWrapper.setShapeLength(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.SHAPE_LENGTH_1.trim().toUpperCase())) {
			geographyBoundaryWrapper.setShapeLength1(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.SHAPE_AREA.trim().toUpperCase())) {
			geographyBoundaryWrapper.setShapeArea(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.KECAMATAN.trim().toUpperCase())) {
			geographyBoundaryWrapper.setKecamatan(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.KABKOT.trim().toUpperCase())) {
			geographyBoundaryWrapper.setKabkot(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.OBJECTID.trim().toUpperCase())) {
			geographyBoundaryWrapper.setObjectId(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.DESA.trim().toUpperCase())) {
			geographyBoundaryWrapper.setDesa(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.JMLPDDK.trim().toUpperCase())) {
			geographyBoundaryWrapper.setJmlpddk(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.CLUSTER.trim().toUpperCase())) {
			geographyBoundaryWrapper.setCluster(textContent);
			geographyBoundaryWrapper.setCheckKey(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.REGION.trim().toUpperCase())) {
			geographyBoundaryWrapper.setRegion(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.CITY.trim().toUpperCase())) {
			geographyBoundaryWrapper.setCity(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.CLUSTER_2.trim().toUpperCase())) {
			geographyBoundaryWrapper.setCluster2(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.SITES.trim().toUpperCase())) {
			geographyBoundaryWrapper.setSites(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.ON_AIR_KML.trim().toUpperCase())) {
			geographyBoundaryWrapper.setOnair(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.OA_KML.trim().toUpperCase())) {
			geographyBoundaryWrapper.setOa(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.CLUSTER_PAC.trim().toUpperCase())) {
			geographyBoundaryWrapper.setClusterPac(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.OPTIMIZATION_STATUS.trim().toUpperCase())) {
			geographyBoundaryWrapper.setOptimizationStatus(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.AREANAME.trim().toUpperCase())) {
			geographyBoundaryWrapper.setAreaName(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.KML_AREA_CLUSTER.trim().toUpperCase())) {
			geographyBoundaryWrapper.setCheckKey(textContent);
			geographyBoundaryWrapper.setAreaCluster(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.KML_RF_CLUSTER.trim().toUpperCase())) {
			geographyBoundaryWrapper.setCheckKey(textContent);
			geographyBoundaryWrapper.setRfCluster(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.KML_PIC.trim().toUpperCase())) {
			geographyBoundaryWrapper.setPic(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.KML_LTE_QUARTERLY.trim().toUpperCase())) {
			geographyBoundaryWrapper.setLteQuarterly(textContent);
			geographyBoundaryWrapper.setCheckKey(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.CLUSTER_SALES_KML.trim().toUpperCase())) {
			geographyBoundaryWrapper.setSaleCluster(textContent);
			geographyBoundaryWrapper.setCheckKey(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.COLOUR_KML.trim().toUpperCase())) {
			geographyBoundaryWrapper.setColour(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.ID.trim().toUpperCase())) {
			geographyBoundaryWrapper.setId(textContent);
			geographyBoundaryWrapper.setCheckKey(textContent);
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.NE_LATITUDE_KEY.trim().toUpperCase())) {
			if (textContent != null && !textContent.isEmpty()) {
				geographyBoundaryWrapper.setLatitude(Double.parseDouble(textContent));
			}
		} else if (attribute.trim().toUpperCase().equals(InfraConstants.NE_LONGITUDE_KEY.trim().toUpperCase())) {
			if (textContent != null && !textContent.isEmpty()) {
				geographyBoundaryWrapper.setLongitude(Double.parseDouble(textContent));
			}
		}
	}

	/**
	 * Populate polygon 3 D data for all boundary.
	 *
	 * @param polygonList
	 *            the polygon list
	 * @param geographyBoundaryPolygonWrappers
	 *            the geography boundary polygon wrappers
	 * @param geographyBoundaryPolygonWrapper
	 *            the geography boundary polygon wrapper
	 * @param geographyBoundaryWrapper
	 *            the geography boundary wrapper
	 * @param coordinatesArrayList
	 *            the coordinates array list
	 * @param coordinatesList
	 *            the coordinates list
	 * @param coords
	 *            the coords
	 * @param polygon3d
	 *            the polygon 3 d
	 */
	private static void populatePolygon3DDataForAllBoundary(NodeList polygonList, List<GeographyBoundaryPolygonWrapper> geographyBoundaryPolygonWrappers,
			GeographyBoundaryPolygonWrapper geographyBoundaryPolygonWrapper, GeographyBoundaryWrapper geographyBoundaryWrapper, List<String> coordinatesArrayList, List<List<Double>> coordinatesList,
			ArrayList<Double> coords, List<List<List<Double>>> polygon3d) {
		for (int polygonKey = 0; polygonKey < polygonList.getLength(); polygonKey++) {
			try {
				geographyBoundaryPolygonWrapper = new GeographyBoundaryPolygonWrapper();
				Node polygon = polygonList.item(polygonKey);
				Element polygonElement = (Element) polygon;
				int len = polygonElement.getElementsByTagName(InfraConstants.COORDINATES).getLength();
				polygon3d = new ArrayList<>();
				for (int index = 0; index < len; index++) {
					String coordinateContent = polygonElement.getElementsByTagName(InfraConstants.COORDINATES).item(index).getTextContent();
					generatePolygonForBoundaries(coordinateContent, coordinatesList, coordinatesArrayList, coords, polygon3d);
				}
				geographyBoundaryPolygonWrapper.setPolygon3DList(polygon3d);
				geographyBoundaryPolygonWrappers.add(geographyBoundaryPolygonWrapper);
			} catch (Exception exception) {
				logger.warn("Error in populate Polygon for Boundary Message {} ", exception.getMessage());
			}
		}
		geographyBoundaryWrapper.setGeographyBoundaryPolygonWrappers(geographyBoundaryPolygonWrappers);
	}

	/**
	 * Generate polygon for boundaries.
	 *
	 * @param coordinateContent
	 *            the coordinate content
	 * @param coordinatesList
	 *            the coordinates list
	 * @param coordinatesArrayList
	 *            the coordinates array list
	 * @param coords
	 *            the coords
	 * @param polygon3d
	 *            the polygon 3 d
	 */
	private static void generatePolygonForBoundaries(String coordinateContent, List<List<Double>> coordinatesList, List<String> coordinatesArrayList, ArrayList<Double> coords,
			List<List<List<Double>>> polygon3d) {
		String coordinates[] = coordinateContent.split(InfraConstants.SPACE_KML);
		coordinatesList = new ArrayList<>();
		coordinatesArrayList = new ArrayList<>(Arrays.asList(coordinates));
		coordinatesArrayList = Utils.removeEmptyStringFromList(coordinatesArrayList);
		if (coordinatesArrayList.size() >= 4) {
			for (int y = 0; y < coordinatesArrayList.size(); y++) {
				try {
					String latLongCoords[] = coordinatesArrayList.get(y).split(InfraConstants.COMMA);
					coords = new ArrayList<>();
					if (latLongCoords.length == 2) {
						coords.add(Double.parseDouble(latLongCoords[0].trim()));
						coords.add(Double.parseDouble(latLongCoords[1].trim()));
						coordinatesList.add(coords);
					}
				} catch (Exception exception) {
					logger.warn("Error in generating polygon for Boundaries Message {} ", exception.getMessage());
				}
			}
			polygon3d.add(coordinatesList);
		}
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws RestException
	 *             the rest exception
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		System.out.println("in start");
		TabFileParserServiceImpl ser = new TabFileParserServiceImpl();
		System.out.println(ser.parseTabFileForRailwayboundaries("/home/ist/smartfren/Boundaries/Railway test/solo_railway_test.kml"));
		System.out.println("end");
	}

	private static void populatePolygon3DDataForSalesCluster(NodeList polygonList, List<GeographyBoundaryPolygonWrapper> geographyBoundaryPolygonWrappers,
			GeographyBoundaryPolygonWrapper geographyBoundaryPolygonWrapper, GeographyBoundaryWrapper geographyBoundaryWrapper, List<String> coordinatesArrayList, List<List<Double>> coordinatesList,
			ArrayList<Double> coords, List<List<List<Double>>> polygon3d) {
		for (int polygonKey = 0; polygonKey < polygonList.getLength(); polygonKey++) {
			try {
				geographyBoundaryPolygonWrapper = new GeographyBoundaryPolygonWrapper();
				Node polygon = polygonList.item(polygonKey);
				Element polygonElement = (Element) polygon;
				int len = polygonElement.getElementsByTagName(InfraConstants.COORDINATES).getLength();
				polygon3d = new ArrayList<>();
				for (int index = 0; index < len; index++) {
					String coordinateContent = polygonElement.getElementsByTagName(InfraConstants.COORDINATES).item(index).getTextContent();
					generatePolygonForSalesCluster(coordinateContent, coordinatesList, coordinatesArrayList, coords, polygon3d);
				}
				geographyBoundaryPolygonWrapper.setPolygon3DList(polygon3d);
				geographyBoundaryPolygonWrappers.add(geographyBoundaryPolygonWrapper);
			} catch (Exception exception) {
				logger.warn("Error in populate Polygon for Boundary Message {} ", exception.getMessage());
			}
		}
		geographyBoundaryWrapper.setGeographyBoundaryPolygonWrappers(geographyBoundaryPolygonWrappers);
	}

	private static void generatePolygonForSalesCluster(String coordinateContent, List<List<Double>> coordinatesList, List<String> coordinatesArrayList, ArrayList<Double> coords,
			List<List<List<Double>>> polygon3d) {
		String coordinates[] = coordinateContent.split(InfraConstants.SPACE_KML);
		coordinatesList = new ArrayList<>();
		coordinatesArrayList = new ArrayList<>(Arrays.asList(coordinates));
		coordinatesArrayList = Utils.removeEmptyStringFromList(coordinatesArrayList);
		if (coordinatesArrayList.size() > 0) {
			for (int i = 0; i < coordinatesArrayList.size(); i++) {
				try {
					String latLongCoords[] = coordinatesArrayList.get(i).split(InfraConstants.COMMA);
					coords = new ArrayList<>();
					if (latLongCoords.length == 3) {
						coords.add(Double.parseDouble(latLongCoords[0].trim()));
						coords.add(Double.parseDouble(latLongCoords[1].trim()));
						coords.add(Double.parseDouble(latLongCoords[2].trim()));
						coordinatesList.add(coords);
					}
				} catch (Exception exception) {
					logger.warn("Error in generating polygon for Boundaries Message {} ", exception.getMessage());
				}
			}
			polygon3d.add(coordinatesList);
		}
	}

	public Map<String, GeographyBoundaryWrapper> parseKMLForBoundariesByJsoup(String filePath) {
		logger.info("Going to parse KML for Boundaries By Jsoup for filePath {} ", filePath);
		Map<String, GeographyBoundaryWrapper> KmlParserMap = null;
		try {
			if (filePath != null && !filePath.isEmpty()) {
				ArrayList<Double> coords = null;
				List<List<Double>> coordinatesList = null;
				List<String> coordinatesArrayList = null;
				List<List<List<Double>>> polygon3d = null;
				GeographyBoundaryWrapper geographyBoundaryWrapper = null;
				KmlParserMap = new HashMap<>();
				GeographyBoundaryPolygonWrapper geographyBoundaryPolygonWrapper = null;
				List<GeographyBoundaryPolygonWrapper> geographyBoundaryPolygonWrappers = null;
				InputStream inputStream = new FileInputStream(filePath);
				Reader reader = new InputStreamReader(inputStream, InfraConstants.UTF_8_ENCODE);
				InputSource inputSource = new InputSource(reader);
				inputSource.setEncoding(InfraConstants.UTF_8_ENCODE);

				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = dBuilder.parse(filePath);
				document.getDocumentElement().normalize();
				NodeList placemarkNodeList = document.getElementsByTagName(InfraConstants.PLACEMARK_TAG);
				if (placemarkNodeList != null && placemarkNodeList.getLength() > 0) {
					for (int placemarkKey = 0; placemarkKey < placemarkNodeList.getLength(); placemarkKey++) {
						try {
							geographyBoundaryWrapper = new GeographyBoundaryWrapper();
							Node placemarkNode = placemarkNodeList.item(placemarkKey);
							if (placemarkNode.getNodeType() == Node.ELEMENT_NODE) {
								Element placeMarkElement = (Element) placemarkNode;
								String htmlContent = placeMarkElement.getElementsByTagName(InfraConstants.DESCRIPTION_KML).item(0).getTextContent();
								String content = Jsoup.parse(htmlContent).text();
								if (content.contains(InfraConstants.COLOUR_KML)) {
									htmlContent = StringUtils.substringAfterLast(Jsoup.parse(htmlContent).text(), InfraConstants.COLOUR_KML).trim();
									content = StringUtils.substringBeforeLast(content, InfraConstants.COLOUR_KML).trim();
									geographyBoundaryWrapper.setColour(htmlContent);
									if (content.contains(InfraConstants.CLUSTER_SALES_KML)) {
										content = StringUtils.substringAfterLast(content, InfraConstants.CLUSTER_SALES_KML).trim();
										if (content != null && !content.isEmpty() && !content.equals(ForesightConstants.BLANK_STRING)) {
											geographyBoundaryWrapper.setSaleCluster(content);
											geographyBoundaryWrapper.setCheckKey(content);
										}
									}
								}
								NodeList polygonList = placeMarkElement.getElementsByTagName(InfraConstants.POLYGON_TAG);
								if (polygonList != null && polygonList.getLength() > 0) {
									geographyBoundaryPolygonWrappers = new ArrayList<>();
									populatePolygon3DDataForSalesCluster(polygonList, geographyBoundaryPolygonWrappers, geographyBoundaryPolygonWrapper, geographyBoundaryWrapper, coordinatesArrayList,
											coordinatesList, coords, polygon3d);
								}
							}
							GeographyBoundaryWrapper boundaryWrapper = KmlParserMap.get(geographyBoundaryWrapper.getCheckKey());
							if (boundaryWrapper != null) {
								List<GeographyBoundaryPolygonWrapper> previousPolygonWrapper = boundaryWrapper.getGeographyBoundaryPolygonWrappers();
								if (previousPolygonWrapper != null) {
									previousPolygonWrapper.addAll(geographyBoundaryWrapper.getGeographyBoundaryPolygonWrappers());
									boundaryWrapper.setGeographyBoundaryPolygonWrappers(previousPolygonWrapper);
									KmlParserMap.put(geographyBoundaryWrapper.getCheckKey(), boundaryWrapper);
								}
							} else {
								KmlParserMap.put(geographyBoundaryWrapper.getCheckKey(), geographyBoundaryWrapper);
							}
						} catch (Exception exception) {
							logger.warn("Error in getting PlacemarkNode {} For Exception {} ", placemarkKey, ExceptionUtils.getStackTrace(exception));
						}
					}
				} else {
					logger.warn("No placemarkNodeList Found for {} ", filePath);
				}
			} else {
				throw new RestException("Unable to parseKMLFile for Boundary for filePath " + filePath);
			}
		} catch (Exception exception) {
			logger.info("Unable to parseKML File for filePath {} Exception {} ", filePath, ExceptionUtils.getStackTrace(exception));
		}
		return KmlParserMap;
	}

	public Map<Integer, List<List<List<Double>>>> parseAscFileForClutterBoundaries(String filePath) {
		logger.info("Going to parse Asc file for clutter Boundaries for {} ", filePath);
		Map<Integer, List<List<List<Double>>>> ascFileMap = null;
		if (filePath != null) {
			File fileName = new File(filePath);
			ascFileMap = new HashMap<>();
			if (fileName.exists()) {
				if (fileName.length() > 0) {
					List<List<List<Double>>> multiPolygonList = null;
					List<List<Double>> polygonList = null;
					Integer checkLineCount = 0;
					List<Double> coordinate = null;
					try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
						String splitLine = null;
						boolean isPolygon = false;
						while ((splitLine = reader.readLine()) != null) {
							try {
								if (splitLine.length() > 35) {
									isPolygon = true;
									checkLineCount = Integer.parseInt(splitLine.split(InfraConstants.SPACE_KML)[0]);
									if (ascFileMap.get(checkLineCount) != null) {
										multiPolygonList = ascFileMap.get(checkLineCount);
										polygonList = new ArrayList<>();
										continue;
									} else {
										multiPolygonList = new ArrayList<>();
										polygonList = new ArrayList<>();
										continue;
									}
								} else {
									coordinate = new ArrayList<>();
									coordinate.add(Double.parseDouble(splitLine.split(InfraConstants.SPACE_KML)[0]));
									coordinate.add(Double.parseDouble(splitLine.split(InfraConstants.SPACE_KML)[1]));
									polygonList.add(coordinate);
									if (isPolygon) {
										multiPolygonList.add(polygonList);
										ascFileMap.put(checkLineCount, multiPolygonList);
										isPolygon = false;
									}
								}
							} catch (Exception exception) {
								logger.error("Unable to parse ASC file for clutter Boundaries {} ", ExceptionUtils.getStackTrace(exception));
							}
						}
					} catch (Exception exception) {
						logger.error("Unable to parse ASC file for clutter Boundaries {} ", ExceptionUtils.getStackTrace(exception));
					}
				}
			}
		} else {
			throw new RestException("filepath can not " + filePath);
		}
		return ascFileMap;
	}

	/*
	 * parse KML file to get coordinates
	 * 
	 * @param filepath the path of File
	 * 
	 * @return Map
	 */
	public Map<String, GeographyBoundaryWrapper> parseTabFileForRailwayboundaries(String filePath) {
		logger.info("Going to parse TabFile for Railway Boundary for filePath {} ", filePath);
		Map<String, GeographyBoundaryWrapper> KmlParserMap = null;
		GeographyBoundaryWrapper geographyBoundaryWrapper = null;
		GeographyBoundaryPolygonWrapper geographyBoundaryPolygonWrapper = null;
		List<GeographyBoundaryPolygonWrapper> geographyBoundaryPolygonWrappers = null;
		List<List<List<Double>>> polygon3d = null;
		try {
			if (filePath != null && !filePath.isEmpty()) {
				KmlParserMap = new HashMap<>();
				InputStream inputStream = new FileInputStream(filePath);
				Reader reader = new InputStreamReader(inputStream, InfraConstants.UTF_8_ENCODE);
				InputSource inputSource = new InputSource(reader);

				inputSource.setEncoding(InfraConstants.UTF_8_ENCODE);
				DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = documentBuilderFactory.newDocumentBuilder();
				Document document = dBuilder.parse(filePath);
				document.getDocumentElement().normalize();
				NodeList placemarkNodeList = document.getElementsByTagName(InfraConstants.PLACEMARK_TAG);
				if (placemarkNodeList != null && placemarkNodeList.getLength() > 0) {
					for (int placemarkKey = 0; placemarkKey < placemarkNodeList.getLength(); placemarkKey++) {
						polygon3d = new ArrayList<>();
						try {
							geographyBoundaryWrapper = new GeographyBoundaryWrapper();
							geographyBoundaryPolygonWrappers = new ArrayList<>();
							Node placemarkNode = placemarkNodeList.item(placemarkKey);
							if (placemarkNode.getNodeType() == Node.ELEMENT_NODE) {
								Element placeMarkElement = (Element) placemarkNode;
								String description = placeMarkElement.getElementsByTagName(InfraConstants.DESCRIPTION_KML).item(0).getTextContent();
								String fileName = document.getElementsByTagName(InfraConstants.BOUNDARY_NAME).item(0).getTextContent();
								String key = InfraConstants.BLANK_STRING;
								if (fileName != null && !fileName.isEmpty()) {
									key = fileName + InfraConstants.UNDERSCORE + (placemarkKey + 1);
								} else {
									key = description + InfraConstants.UNDERSCORE + (placemarkKey + 1);
								}
								String cord = placeMarkElement.getElementsByTagName(InfraConstants.COORDINATES).item(0).getTextContent();
								if (key != null && !key.isEmpty()) {
									geographyBoundaryWrapper.setCheckKey(key);
								}
								polygon3d = generate3DPolygonForRailwayBoundaries(cord);
								if (polygon3d != null && !polygon3d.isEmpty()) {
									geographyBoundaryPolygonWrapper = new GeographyBoundaryPolygonWrapper();
									geographyBoundaryPolygonWrapper.setPolygon3DList(polygon3d);
									geographyBoundaryPolygonWrappers.add(geographyBoundaryPolygonWrapper);
									geographyBoundaryWrapper.setGeographyBoundaryPolygonWrappers(geographyBoundaryPolygonWrappers);
									String mapKey = geographyBoundaryWrapper.getCheckKey();
									KmlParserMap.put(mapKey, geographyBoundaryWrapper);
								}
							}
						} catch (Exception exception) {
							logger.warn("Error in getting PlacemarkNode {} For Exception {} ", placemarkKey, exception.getMessage());
						}
					}
				} else {
					logger.warn("No placemarkNodeList Found for {} ", filePath);
				}

			} else {
				throw new RestException("Unable to parseKMLFile for Boundary for filePath " + filePath);
			}
		} catch (Exception exception) {
			logger.info("Unable to parseKML File for filePath {} Exception {} ", filePath, exception.getMessage());
		}
		return KmlParserMap;
	}

	/*
	 * generate polygon for RailwayBoundaries
	 * 
	 * @param coordinate of the place mark
	 * 
	 * @return 3D List of Polygon
	 */
	private static List<List<List<Double>>> generate3DPolygonForRailwayBoundaries(String coordinate) {
		List<List<Double>> coordinatesList = new ArrayList<>();
		List<String> coordinatesArrayList = null;
		ArrayList<Double> coords = null;
		List<List<List<Double>>> polygon3d = null;
		try {
			polygon3d = new ArrayList<>();
			String coordinates[] = coordinate.split(InfraConstants.SPACE_KML);
			coordinatesArrayList = new ArrayList<>(Arrays.asList(coordinates));
			coordinatesArrayList = Utils.removeEmptyStringFromList(coordinatesArrayList);
			if (coordinatesArrayList.size() > 0) {
				for (int i = 0; i < coordinatesArrayList.size(); i++) {
					try {
						String latLongCoords[] = coordinatesArrayList.get(i).split(InfraConstants.COMMA);
						coords = new ArrayList<>();
						if (latLongCoords.length == 2) {
							coords.add(Double.parseDouble(latLongCoords[0].trim()));
							coords.add(Double.parseDouble(latLongCoords[1].trim()));
							coordinatesList.add(coords);
						}
					} catch (Exception exception) {
						logger.warn("Error in generating polygon for Railway Boundaries Message {} ", exception.getMessage());
					}

				}
				polygon3d.add(coordinatesList);
			}
		} catch (Exception exception) {
			logger.warn("Error in generating polygon for Railway Boundaries Message {} ", exception.getMessage());
		}
		return polygon3d;
	}
}
