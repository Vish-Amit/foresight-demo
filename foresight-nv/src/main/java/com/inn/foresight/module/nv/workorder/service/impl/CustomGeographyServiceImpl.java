package com.inn.foresight.module.nv.workorder.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.activiti.editor.language.json.converter.util.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.http.HttpException;
import com.inn.commons.unit.Duration;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ConfigEnum;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.customgeography.constants.CustomGeographyConstants;
import com.inn.foresight.module.nv.customgeography.dao.ICustomGeographyHbaseDao;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.dao.ICustomGeographyDao;
import com.inn.foresight.module.nv.workorder.model.CustomGeography;
import com.inn.foresight.module.nv.workorder.model.CustomGeography.GeographyType;
import com.inn.foresight.module.nv.workorder.service.ICustomGeographyService;
import com.inn.foresight.module.nv.workorder.utils.NVWorkorderUtils;
import com.inn.foresight.module.nv.workorder.wrapper.CustomGeographyWrapper;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.geography.service.GeographyL4Service;
import com.inn.product.um.user.service.impl.UserContextServiceImpl;

/** The Class CustomGeographyServiceImpl. */
@Service("CustomGeographyServiceImpl")
public class CustomGeographyServiceImpl implements ICustomGeographyService {

	/** The logger. */
	private Logger logger = LogManager.getLogger(CustomGeographyServiceImpl.class);

	/** The Instance of CustomGeographyServiceImpl. */
	@Autowired
	private ICustomGeographyDao customGeographyDao;
	
	/** The Instance of GenericMapServiceImpl. */
	@Autowired
	private IGenericMapService genericMapService;
	
	/** The Intance of GeographyL4ServiceImpl. */
	@Autowired
	private GeographyL4Service geographyL4Service;
	@Autowired
	private ICustomGeographyHbaseDao customeGeographyHbasedao;


	
	/**
	 * Creates a Custom Geography row In Custom Geography Table and stores Boundary latlongs in Hbase  .
	 * @param wrapper the wrapper
	 * @return newly created CustomGeography row in Db.
	 * @throws RestException the rest exception
	 * @throws DaoException 
	 */
	@Override
	@Transactional
	public CustomGeography createCustomGeography(CustomGeographyWrapper wrapper, HttpServletRequest request) {
		Integer id = null;
		try {
			if(wrapper.getId()!=null) {
				updateDeleteStatus(wrapper.getId());
			}
			CustomGeography customGeography = getCustomGeography(wrapper);
			customGeography.setGeographyL4(getGeography(wrapper));
			CustomGeography result = customGeographyDao.create(customGeography);
			wrapper.setId(result.getId());
			saveBoundaryInHbase(wrapper, request);
			return result;
		} catch (Exception e) {
			logger.error("Inside @class CustomGeographyServiceImpl @Method createCustomGeography Exception {}",
					ExceptionUtils.getStackTrace(e));
			deleteCustomGeography(id);
			throw new RestException(
					ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, NVWorkorderConstant.CUSTOM_GEOGRAPHY, e));
		}
	}

	private void updateDeleteStatus(Integer id) {
		try {
		CustomGeography customeGeography = customGeographyDao.findByPk(id);
			if (customeGeography != null) {
				customeGeography.setDeleted(Boolean.TRUE);
				customGeographyDao.update(customeGeography);
			}
		}catch(Exception e) {
			logger.error("Exception inside updateDeleteStatus {} ",ExceptionUtils.getStackTrace(e));
		}
	}

	private GeographyL4 getGeography(CustomGeographyWrapper wrapper) {
		String result = genericMapService.getGeographyDataByPoint(GenericMapUtils.getGeoColumnList(),
		        GenericMapUtils.GEOGRAPHY_TABLE_NAME, wrapper.getStartLatitude(), wrapper.getStartLongitude(),
				false,GenericMapUtils.L4_TYPE);
		List<List<String>> list = new Gson().fromJson(result, new TypeToken<List<List<String>>>() {}.getType());
		if (CollectionUtils.isEmpty(list)) {
			throw new RestException(ForesightConstants.GEOGRAPHY_NOT_FOUND);
		}
		String geographyL4Name = list.get(ForesightConstants.ZERO).get(ForesightConstants.ZERO);
		logger.info("Getting Geography {}", geographyL4Name);
		return geographyL4Service.getGeographyL4ByName(geographyL4Name);
	}

	private void deleteCustomGeography(Integer id) {
		if(id != null) {
			try {
				customGeographyDao.deleteByPk(id);
			} catch (DaoException e) {
				logger.error("Inside @class CustomGeographyServiceImpl @Method deleteCustomGeography Exception {}",
						ExceptionUtils.getStackTrace(e));
				throw new RestException(
						ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, "CustomGeography", e));
			}
		}
	}

	private CustomGeography getCustomGeography(CustomGeographyWrapper wrapper) {
		CustomGeography customGeography = new CustomGeography();
		customGeography.setLandmark(wrapper.getLandmark());
		customGeography.setName(wrapper.getName());
		customGeography.setStartLatitude(wrapper.getStartLatitude());
		customGeography.setStartLongitude(wrapper.getStartLongitude());
		customGeography.setEndLatitude(wrapper.getEndLatitude());
		customGeography.setEndLongitude(wrapper.getEndLongitude());
		customGeography.setType(wrapper.getType());
		customGeography.setCreationTime(new Date());
		customGeography.setModifiedTime(new Date());
		customGeography.setCreator(UserContextServiceImpl.getUserInContext());
		customGeography.setRouteType(wrapper.getRouteType());
		return customGeography;
	}
	
	/**
	 * It saves CustomBoundary Latlongs and Boundary Name In Hbase.
	 * @param customGeography the custom geography
	 * @param wrapper the wrapper
	 * @throws RestException the rest exception
	 * @throws HttpException 
	 */
	private void saveBoundaryInHbase(CustomGeographyWrapper wrapper, HttpServletRequest request) 
			throws HttpException {		
		String url = NVWorkorderUtils.getCustomGeographyDropwizardUrl(request,ConfigUtils.getString(NVConfigUtil.CREATE_CUSTOM_GEOGRAPHY_URL));
		Duration duration = Duration.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
		StringEntity entity = new StringEntity(new Gson().toJson(wrapper), ContentType.APPLICATION_JSON);
		logger.info("Getting Dropwizard URL {}", url);
		NVLayer3Utils.sendHttpPostRequest(url, entity, true, duration).getString();
	}

	@Override
	public List<CustomGeographyWrapper> findAllCustomGeography(Integer geographyId, String geographyType,
			List<GeographyType> typeList) {
		try{
			return customGeographyDao.findAllCustomGeography(geographyId, geographyType, typeList);
		} catch(Exception e){
			logger.error("Inside findAllCustomGeography() Getting Exception {}", ExceptionUtils.getStackTrace(e));
			throw new RestException(ExceptionUtil.generateExceptionCode(ForesightConstants.SERVICE, "CustomGeography", e));
		}
	}

	@Override
	public CustomGeographyWrapper getCustomGeography(Integer geographyId, String url) throws HttpException {
		CustomGeography customGeography = customGeographyDao.findByPk(geographyId);
		Duration duration = Duration.minutes(Integer.parseInt(ConfigUtils.getString(ConfigEnum.TIMEOUT_VALUE.getValue())));
		url = url + Symbol.SLASH_FORWARD_STRING + customGeography.getType();
		logger.info("Getting Dropwizard URL with customGeography type{}", url);
		
		String result = NVWorkorderUtils.sendHttpGetRequest(url, true, duration).getString();
		CustomGeographyWrapper wrapper = new Gson().fromJson(result, CustomGeographyWrapper.class);
		wrapper.setRouteType(customGeography.getRouteType());
		wrapper.setId(customGeography.getId());
		return wrapper;
	}
	@Override
	public String createCustomGeography(CustomGeographyWrapper wrapper) {
		try {
			Put put = getPut(wrapper);
			customeGeographyHbasedao.insertCustomBoundary(ConfigUtils.getString(CustomGeographyConstants.TABLE_CUSTOM_BOUNDARY),
					Arrays.asList(put));
			return ForesightConstants.SUCCESS_JSON;
		} catch (Exception e) {
			logger.error("Inside @class CustomGeographyServiceImpl @Method saveBoundryInHbase Exception {}",
					ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Creating Put Object for inserting Boundary in Hbase.
	 * @param customGeography the custom geography
	 * @param wrapper the wrapper
	 * @return the put
	 */
	private Put getPut(CustomGeographyWrapper wrapper) {
		Put put = new Put(Bytes.toBytes(getRowKey(wrapper.getId(), wrapper.getType())));
		Gson gson = new Gson();
		put.addColumn(Bytes.toBytes(CustomGeographyConstants.COULMN_FAMILY_R), Bytes.toBytes(CustomGeographyConstants.NAME),
				Bytes.toBytes(wrapper.getName()));
		put.addColumn(Bytes.toBytes(CustomGeographyConstants.COULMN_FAMILY_R), Bytes.toBytes(CustomGeographyConstants.LANDMARK),
				Bytes.toBytes(wrapper.getLandmark()));
		put.addColumn(Bytes.toBytes(CustomGeographyConstants.COULMN_FAMILY_R), Bytes.toBytes(CustomGeographyConstants.TYPE),
				Bytes.toBytes(wrapper.getType().getValue()));
		put.addColumn(Bytes.toBytes(CustomGeographyConstants.COULMN_FAMILY_R), Bytes.toBytes(CustomGeographyConstants.START_COORDINATES),
				Bytes.toBytes(gson.toJson(getCoordinateList(wrapper.getStartLongitude(), wrapper.getStartLatitude()))));
		put.addColumn(Bytes.toBytes(CustomGeographyConstants.COULMN_FAMILY_R), Bytes.toBytes(CustomGeographyConstants.END_COORDINATES),
				Bytes.toBytes(gson.toJson(getCoordinateList(wrapper.getEndLongitude(), wrapper.getEndLatitude()))));
		put.addColumn(Bytes.toBytes(CustomGeographyConstants.COULMN_FAMILY_R), Bytes.toBytes(CustomGeographyConstants.BOUNDARY),
				Bytes.toBytes(wrapper.getBoundary()));
		put.addColumn(Bytes.toBytes(CustomGeographyConstants.COULMN_FAMILY_R), Bytes.toBytes(CustomGeographyConstants.ROUTE_POINTS),
				Bytes.toBytes(wrapper.getRoutePoints()));
		if (wrapper.getBoundaryPoints() != null) {
			put.addColumn(Bytes.toBytes(CustomGeographyConstants.COULMN_FAMILY_R),
					Bytes.toBytes(CustomGeographyConstants.BOUNDARY_POINTS),
					Bytes.toBytes(wrapper.getBoundaryPoints()));
		}
		return put;
	}

	private List<Double> getCoordinateList(Double longitude, Double latitude) {
		List<Double> list = new ArrayList<>();
		list.add(longitude);
		list.add(latitude);
		return list;
	}

	private String getRowKey(Integer id, GeographyType type) {
		String rowKey =  type.getValue() + id;
		logger.info("Getting Row Key : {}", rowKey);
		return rowKey;
	}

	@Override
	public CustomGeographyWrapper getCustomGeography(Integer gegraphyId, GeographyType type) {
		try {
			HBaseResult result = customeGeographyHbasedao.getCustomBoundaryFromHbase(ConfigUtils.getString(CustomGeographyConstants.TABLE_CUSTOM_BOUNDARY),
					CustomGeographyConstants.COULMN_FAMILY_R, getRowKey(gegraphyId, type));
			CustomGeographyWrapper wrapper = getCustomgeographyWrapper(result);
			wrapper.setType(type);
			return wrapper;
		} catch (IOException e) {
			logger.error("Inside @class CustomBoundaryServiceImpl @Method getCustomBoundary Exception {}",
					ExceptionUtils.getStackTrace(e));
			throw new RestException(e.getMessage());
		}
	}

	private CustomGeographyWrapper getCustomgeographyWrapper(HBaseResult result) {
		CustomGeographyWrapper wrapper = new CustomGeographyWrapper();
		Gson gson = new Gson();
		if(result != null) {
			wrapper.setName(result.getString(Bytes.toBytes(CustomGeographyConstants.NAME)));
			wrapper.setLandmark(result.getString(Bytes.toBytes(CustomGeographyConstants.LANDMARK)));
			wrapper.setBoundary(result.getString(Bytes.toBytes(CustomGeographyConstants.BOUNDARY)));
			wrapper.setRoutePoints(result.getString(Bytes.toBytes(CustomGeographyConstants.ROUTE_POINTS)));
			wrapper.setBoundaryPoints(result.getString(Bytes.toBytes(CustomGeographyConstants.BOUNDARY_POINTS)));
			List<Double> startList = gson.fromJson(result.getString(Bytes.toBytes(CustomGeographyConstants.START_COORDINATES)),
					new TypeToken<List<Double>>() {}.getType());
			List<Double> endList = gson.fromJson(result.getString(Bytes.toBytes(CustomGeographyConstants.END_COORDINATES)),
					new TypeToken<List<Double>>() {}.getType());
			if(startList != null) {
				wrapper.setStartLatitude(startList.get(ForesightConstants.ONE));
				wrapper.setStartLongitude(startList.get(ForesightConstants.ZERO));
			}
			if(endList != null) {
				wrapper.setEndLatitude(endList.get(ForesightConstants.ONE));
				wrapper.setEndLongitude(endList.get(ForesightConstants.ZERO));
			}
		}
		return wrapper;
	}

}
