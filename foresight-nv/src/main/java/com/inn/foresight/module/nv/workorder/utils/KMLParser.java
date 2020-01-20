package com.inn.foresight.module.nv.workorder.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.module.nv.workorder.constant.KMLParserConstant;

public class KMLParser   {
	private KMLParser() {
		
	}
	private static final Logger logger = LogManager.getLogger(KMLParser.class);

	public static String parseKML(InputStream inputStream)  {
		List<List<List<Double>>> boundaryList = null;
		boundaryList = parseKMLFileAndGetBoundary(inputStream);
		String result;
		if (!boundaryList.isEmpty()) {
			result = new Gson().toJson(boundaryList);
		} else {
			result = KMLParserConstant.BOUNDARY_LIST_IS_EMPTY;
		}
		return String.format(KMLParserConstant.BOUNDARY_LIST_JSON, result);

	}

	private static List<List<List<Double>>> parseKMLFileAndGetBoundary(InputStream inputStream) {
		logger.info("Going to parse KML File");
		List<List<List<Double>>> boundaryList = new ArrayList<>();
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();

		try {
			domFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, Boolean.TRUE);
			domFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD,ForesightConstants.FALSE);
			domFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA,ForesightConstants.FALSE);
			domFactory.setNamespaceAware(true);
		} catch (ParserConfigurationException e1) {
			logger.info("IOException in closing inputStream :{}", ExceptionUtils.getStackTrace(e1));
		}
		
		DocumentBuilder builder = null;
		try {
			Document doc = null;
			builder = domFactory.newDocumentBuilder();
			doc = builder.parse(inputStream);
			doc	.getDocumentElement()
				.normalize();
			String rootNode = doc	.getDocumentElement()
									.getNodeName();
			NodeList nodeList = doc.getElementsByTagName(rootNode);
			NodeList element = ((Element) nodeList.item(KMLParserConstant.FIRST_INDEX)).getElementsByTagName(KMLParserConstant.COORDINATES_TAG);
			int length = element.getLength();
			logger.info("Coordinates length:{}", length);
			if (length > KMLParserConstant.FIRST_INDEX) {
				for (int i = KMLParserConstant.FIRST_INDEX; i < length; i++) {
					String coordinates = element.item(i)
												.getChildNodes()
												.item(KMLParserConstant.FIRST_INDEX)
												.getNodeValue()
												.trim();
					boundaryList.add(parseCoordinates(coordinates));
				}
			} else {
				logger.info("Invalid KML File");
				throw new RestException(KMLParserConstant.INVALID_KML_FILE_JSON);
			}

		} catch (SAXException e) {
			logger.info("SAXException:{}", ExceptionUtils.getStackTrace(e));
			throw new RestException(KMLParserConstant.INVALID_KML_FILE_JSON);
		} catch (IOException e) {
			logger.info("IOException:{}", ExceptionUtils.getStackTrace(e));
		} catch (ParserConfigurationException e) {
			logger.info("ParserConfigurationException:{}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtils.getRootCauseMessage(e));
		}finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				logger.info("IOException in closing inputStream :{}", ExceptionUtils.getStackTrace(e));
			}
		}
		logger.info("boundaryList  size:{}", boundaryList.size());
		return boundaryList;
	}

	private static List<List<Double>> parseCoordinates(String coordinates) {
		List<List<Double>> latLngList = new ArrayList<>();
		String newVal=(Arrays.asList(coordinates.split(KMLParserConstant.NEW_LINE_DELIMETER)).toString());
		newVal=newVal.replace("[", "").replace("]", "");
		List<String> coordinatesArray =Arrays.asList( newVal.split(" "));
		coordinatesArray.stream()
						.forEach(coordinateObject -> {
							List<Double> latLng = new ArrayList<>();
							String[] list = coordinateObject.split(",");
							if(list.length>1) {
								latLng.add(Double.parseDouble(list[0]));
								latLng.add(Double.parseDouble(list[1]));
								latLngList.add(latLng);
							}
						});
		return latLngList;
	}

}
