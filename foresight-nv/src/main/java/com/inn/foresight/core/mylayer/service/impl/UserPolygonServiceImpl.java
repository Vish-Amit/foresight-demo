package com.inn.foresight.core.mylayer.service.impl;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.maps.geometry.MensurationUtils;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.utils.InfraConstants;
import com.inn.foresight.core.infra.utils.InfraUtils;
import com.inn.foresight.core.mylayer.dao.IUserPolygonDao;
import com.inn.foresight.core.mylayer.model.UserPolygon;
import com.inn.foresight.core.mylayer.service.IUserPolygonService;
import com.inn.product.um.user.model.User;

@Service("UserPolygonServiceImpl")
public class UserPolygonServiceImpl extends AbstractService<Integer, UserPolygon> implements IUserPolygonService {
	private Logger logger = LogManager.getLogger(UserPolygonServiceImpl.class);

	@Autowired
	private IUserPolygonDao iUserPolygonDao;

	@Autowired
	public void setDao(IUserPolygonDao iDao) {
		super.setDao(iDao);
		this.iUserPolygonDao = iDao;
	}

	@Override
	public UserPolygon insertPolygonDetail(User user, UserPolygon userPolygon) {
		Integer userid = user.getUserid();
		if (userid != null) {
			boolean isPolygonExist = iUserPolygonDao.isPolygonExist(userid, userPolygon.getName());
			if (!isPolygonExist) {
				logger.info("going to insert polygon details.");
				if (userPolygon.getName() != null && userPolygon.getPolygonPoint() != null) {
					try {
						logger.info("Going to insert polygon details for userid {} ", userid);
						userPolygon.setUser(user);
						userPolygon.setIsdeleted(false);
						userPolygon.setModifiedTime(new Date());
						userPolygon.setCreatedTime(new Date());
						userPolygon = iUserPolygonDao.create(userPolygon);
					} catch (Exception exception) {
						logger.error("unable to insert polygon: {}", ExceptionUtils.getStackTrace(exception));
						throw new RestException("Unable to create " + exception.getMessage());
					}
				} else {
					throw new RestException("INvalid Polygon Name.");
				}
			} else {
				throw new RestException("Polygon name already exist.");
			}
		} else {
			throw new RestException("Invalid user");
		}
		return userPolygon;
	}

	@Override
	public UserPolygon updatePolygonPoint(User user, UserPolygon userPolygon) {
		logger.info("Going to update polygon detail for userid {}", user.getUserid());
		UserPolygon userPolygonPersisted = iUserPolygonDao.getPolygonById(user.getUserid(), userPolygon.getId());
		if (userPolygonPersisted != null) {
			userPolygonPersisted.setUser(user);
			userPolygonPersisted.setIsdeleted(false);
			userPolygonPersisted.setModifiedTime(new Date());
			if(userPolygon.getPolygonPoint() != null) {
			userPolygonPersisted.setPolygonPoint(userPolygon.getPolygonPoint());
			}
            return super.update(userPolygonPersisted);
		} else {
			throw new RestException("Unable to update polygon detail");
		}
	}

	@Override
	@Transactional
	public boolean deletePolygonById(Integer userid, Integer id) {
		try {
			logger.info("Going to delete polygon for id {} and for userid {}", id, userid);
			return iUserPolygonDao.deletePolygonById(userid, id);
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public List<Map> getListOfPolygon(Integer userid, Integer lowerLimit, Integer upperLimit,String polygonSearch) {
		logger.info("Going to get list of polygons for userid {}", userid);
		List<UserPolygon> polygons = iUserPolygonDao.getListOfPolygon(userid, lowerLimit, upperLimit,polygonSearch);
		List<Map> parameters = new ArrayList<>();
		if (polygons != null && !polygons.isEmpty()) {
			logger.info("{} polygons found for userid {}", polygons.size(), userid);
			return getPolygonPropertiesMap(polygons, parameters);
		} else {
			return parameters;
		}
	}
    @SuppressWarnings({ "rawtypes" })
	private List<Map> getPolygonPropertiesMap(List<UserPolygon> polygons,List<Map> parameters){
    for (UserPolygon userPolygon : polygons) {
		if (userPolygon != null) {
			try {
				Map map = getPolygonPropertiesInMap(userPolygon);
				parameters.add(map);
			} catch (Exception exception) {
				logger.error("Error in putting polygon parameters in map Message {}", exception.getMessage());
			}
		}
	}
	return parameters;
}

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private Map getPolygonPropertiesInMap(UserPolygon userPolygon) {
		Map map = new HashMap();
		try {
			if (userPolygon != null) {
				double area = 0;
				map.put(ForesightConstants.ID, userPolygon.getId());
				map.put(ForesightConstants.NAME, userPolygon.getName());
				map.put(InfraConstants.COMMENTS,(userPolygon.getComments() != null ? userPolygon.getComments() : ForesightConstants.HIPHEN));
				map.put(InfraConstants.POLYGON_POINT,(userPolygon.getPolygonPoint() != null ? userPolygon.getPolygonPoint()	: ForesightConstants.HIPHEN));
				String polygonPoint = userPolygon.getPolygonPoint();
				Integer zoomLevel = userPolygon.getZoomLevel();
				area = calculateAreaForPolygonPoint(polygonPoint, zoomLevel);
				map.put(ForesightConstants.AREA, area);
				map.put(ForesightConstants.COLORCODE,(userPolygon.getColorCode() != null ? userPolygon.getColorCode() : ForesightConstants.HIPHEN));
				map.put(ForesightConstants.ZOOM_LEVEL,(userPolygon.getZoomLevel() != null ? userPolygon.getZoomLevel() : ForesightConstants.HIPHEN));
				map.put(InfraConstants.POLYGON_PROPERTIES,(userPolygon.getPolygonProperties() != null ? userPolygon.getPolygonProperties() : ForesightConstants.HIPHEN));
				if (userPolygon.getCreatedTime() != null)
					map.put(ForesightConstants.CREATEDTIME, userPolygon.getCreatedTime());
				if (userPolygon.getModifiedTime() != null)
					map.put(ForesightConstants.MODIFIEDTIME, userPolygon.getModifiedTime());
			}
		} catch (Exception exception) {
			logger.error("Error in getting map of polygons Message {}", exception.getMessage());
			throw new RestException("unable to get polygons.");
		}
		return map;
	}

	private double calculateAreaForPolygonPoint(String polygonPoint, Integer zoomLevel) {
		double area = 0;
		if (polygonPoint != null && zoomLevel != null) {
			try {
				List<List<Double>> boundary = InfraUtils.getBoundary(polygonPoint);
				List<List<List<Double>>> boundaries = new ArrayList<>();
				boundaries.add(boundary);
				area = MensurationUtils.calculateExactAreaForZoom(boundaries, zoomLevel);
			} catch (Exception exception) {
				logger.error("invalid parameters: {}", Utils.getStackTrace(exception));
			}
		}
		return area;
	}

	@Override
	public Long getCountsOfPolygon(Integer userid, String searchTerm) {
		logger.info("Going to get list of polygons for userid {}", userid);
		Long polygons = 0L;
		try {
			polygons = iUserPolygonDao.getCountsOfPolygon(userid, searchTerm);
			if (polygons != null) {
				return polygons;
			} else {
				logger.error("Error in getting counts of polygons for userid: {},searchterm : {}",userid,searchTerm);
			}
		} catch (Exception exception) {
			logger.error("Error in getting counts of polygons Message {}", exception.getMessage());
			throw new RestException("unable to get polygons.");
		}
		return polygons;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getPolygonById(Integer userid, Integer id) {
		logger.info("Going to get polygon detail for id {} and for userid {}", id, userid);
		try {

			UserPolygon userPolygon = iUserPolygonDao.getPolygonById(userid, id);
			if (userPolygon != null) {
					return getPolygonPropertiesInMap(userPolygon);
			}
		} catch (RestException restException) {
			throw new RestException(restException.getMessage());
		}
		return null;
	}

	@Override
	public String exportPolygonInKML(Integer id) {
		if (id != null) {
			UserPolygon userPolygon = super.findById(id);
			if (userPolygon != null) {
				if (userPolygon.getPolygonPoint() != null) {
					List<Double[]> array = parsePolyLineString(userPolygon.getPolygonPoint());
					String result = generateKMLFile(array, userPolygon);
					if (result != null) {
						return result;
					} else {
						throw new RestException("Something  went wrong");
					}
				} else {
					throw new RestException("Invalid polygon points");
				}
			} else {
				throw new RestException("Invalid Polygon.");
			}
		}
		return null;
	}

	private String generateKMLFile(List<Double[]> kpiValues, UserPolygon userPolygon) {
		logger.info("going to generate ml file.");
		try {
			String filePath = ConfigUtils.getString(ForesightConstants.EXCEL_REPORT_PATH);

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			TransformerFactory tranFactory = TransformerFactory.newInstance();
			Transformer aTransformer = tranFactory.newTransformer();
			aTransformer.setOutputProperty(OutputKeys.INDENT, InfraConstants.YES_SMALLCASE);

			Document doc = builder.newDocument();
			Element kml = doc.createElement(InfraConstants.KML_SMALLCASE);
			kml.setAttribute(InfraConstants.XMLNS, "http://earth.google.com/kml/2.1");
			doc.appendChild(kml);

			Element document = doc.createElement(InfraConstants.DOCUMENT);
			kml.appendChild(document);

			Element description = doc.createElement(InfraConstants.DESCRIPT);
			description.setTextContent(userPolygon.getComments());
			document.appendChild(description);

			Element placemark = doc.createElement(ForesightConstants.PLACEMARK);
			document.appendChild(placemark);

			Element name = doc.createElement(ForesightConstants.NAME);
			placemark.appendChild(name);
			name.setTextContent(userPolygon.getName());

			Element polygon = doc.createElement(ForesightConstants.POLYGON);
			placemark.appendChild(polygon);

			Element outerBoundary = doc.createElement(InfraConstants.OUTERBOUNDARYIS);
			polygon.appendChild(outerBoundary);

			Element linearRing = doc.createElement(InfraConstants.LINEARRING_CAMEL);
			outerBoundary.appendChild(linearRing);

			Element extrude = doc.createElement(InfraConstants.EXTRUDE);
			extrude.setTextContent(ForesightConstants.STRING_ONE);
			linearRing.appendChild(extrude);

			Element altitudeMode = doc.createElement(InfraConstants.ALTITUDEMODE);
			linearRing.appendChild(altitudeMode);
			altitudeMode.setTextContent(InfraConstants.RELATIVETOGROUND);

			Element coordinates = doc.createElement(ForesightConstants.COORDINATES);
			linearRing.appendChild(coordinates);

			for (Double kpi[] : kpiValues) {
				coordinates.appendChild(doc.createTextNode(kpi[0] + ForesightConstants.COMMA + kpi[1]
						+ ForesightConstants.COMMA + 0 + ForesightConstants.SPACE));
			}

			Source source = new DOMSource(doc);
			StringWriter out = new StringWriter();
			Result result = new StreamResult(out);
				userPolygon.setKmlUploadPath(filePath + userPolygon.getName() + ForesightConstants.KML_EXTENSION);
				super.update(userPolygon);

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			transformer.transform(source, result);
			return out.toString();

		} catch (TransformerException | ParserConfigurationException e) {
			throw new RestException(e);
		}
	}

	@Override
	public String getKmlFileName(Integer id) {
		String fileName = null;
		if (id != null) {
			UserPolygon userPolygon = super.findById(id);
			if (userPolygon != null) {
				fileName = userPolygon.getName();
				return fileName + ForesightConstants.KML_EXTENSION;
			} else {
				throw new RestException("Invalid polygon");
			}
		} else {
			throw new RestException("Invalid Polygon ID.");
		}
	}

	private List<Double[]> parsePolyLineString(String polygon) {
		List<Double[]> latlngList = new ArrayList<>();
		String plannedCoordinate[] = polygon.split(ForesightConstants.COMMA);
		int counter = 0;
		try {
			while (counter < plannedCoordinate.length) {
				Double[] latLong = new Double[2];
				latLong[0] = Double.parseDouble(
						plannedCoordinate[counter].replaceAll("[^0-9.-]", ForesightConstants.BLANK_STRING));
				latLong[1] = Double.parseDouble(
						plannedCoordinate[counter + 1].replaceAll("[^0-9.-]", ForesightConstants.BLANK_STRING));
				counter = counter + 2;
				latlngList.add(latLong);
			}
		} catch (Exception e) {
			logger.info("Error while parsing polyline string  {}", ExceptionUtils.getStackTrace(e));
		}
		return latlngList;
	}

	@Override
	public UserPolygon updatePolygonProperties(Integer userid, UserPolygon userPolygon) {
		logger.info("Going to update polygon detail for userid {}", userid);
		UserPolygon userPolygonPersisted = null;
		if(userPolygon.getName() != null && userPolygon.getId() != null) {
		boolean isPolygonExistForId = iUserPolygonDao.isNewPolygonNameAlreadyExist(userid, userPolygon.getName(),userPolygon.getId());
		if (!isPolygonExistForId) {
			userPolygonPersisted = iUserPolygonDao.getPolygonById(userid, userPolygon.getId());
			if(userPolygonPersisted != null) {
				updatePolygonProperty(userPolygonPersisted,userPolygon);
			}
		}
		else
			throw new RestException("polygon name already exist.");
		} else {
			throw new RestException("Unable to update polygon detail");
		}
		return userPolygonPersisted;
	}

	private UserPolygon updatePolygonProperty(UserPolygon userPolygonPersisted,UserPolygon userPolygon) {

		userPolygonPersisted.setName(userPolygon.getName());
		if (userPolygon.getPolygonProperties() != null) {
			userPolygonPersisted.setPolygonProperties(userPolygon.getPolygonProperties());
		}
		if (userPolygon.getComments() != null) {
			userPolygonPersisted.setComments(userPolygon.getComments());
		}
		userPolygonPersisted.setModifiedTime(new Date());
		return super.update(userPolygonPersisted);
		
	}
}
