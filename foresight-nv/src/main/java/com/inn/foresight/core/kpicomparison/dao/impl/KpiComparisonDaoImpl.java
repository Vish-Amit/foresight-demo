package com.inn.foresight.core.kpicomparison.dao.impl;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.io.image.ImageUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.kpicomparison.dao.IKpiComparisonDao;
import com.inn.foresight.core.kpicomparison.utils.KpiComparisonConstants;
import com.inn.product.systemconfiguration.dao.SystemConfigurationDao;

@Service("KpiComparisonDaoImpl")
public class KpiComparisonDaoImpl extends AbstractHBaseDao implements IKpiComparisonDao {
	@Autowired
	SystemConfigurationDao dao;
	private Logger logger = LogManager.getLogger(KpiComparisonDaoImpl.class);

	@Override
	public Map<String, Integer> getZoneMap() {
		logger.info("Inside getZoneMap() in KpiComparisonDaoImpl ");
		Map<String, Integer> zoneMap = new HashMap<>();
		try {
			String zone = dao.getSystemConfigurationByName(KpiComparisonConstants.KEY_TO_GET_ZONE).get(0).getValue();

			return parseResponse(zone);
		} catch (Exception exception) {
			logger.error("Exception in zone map in getZoneMap() {} ", Utils.getStackTrace(exception));
			throw new DaoException(ExceptionUtils.getStackTrace(exception));
		}
	}

	@Override
	public BufferedImage getClutterImage(String rowkey) {
		logger.info("Inside getClutterImage() in KpiComparisonDaoImpl rowkey {}", rowkey);
		try {
			HBaseResult hbaseResult = getResultByPool(new Get(Bytes.toBytes(rowkey)),
					ConfigUtils.getString(KpiComparisonConstants.TABLE_MORPHOLOGY_BOUNDARY),
					Bytes.toBytes(ConfigUtils.getString(KpiComparisonConstants.MORPHOLOGY_BOUNDARY_COLUMN_FAMILY)));

			if (hbaseResult != null) {
				byte[] clutterImage = hbaseResult.getValue(
						Bytes.toBytes(ConfigUtils.getString(KpiComparisonConstants.MORPHOLOGY_BOUNDARY_COLUMN_NAME)));
				return ImageUtils.toBufferedImage(clutterImage);
			}
		} catch (Exception e) {
			logger.error("Exception while getting clutter image in getClutterImage() {} ", Utils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	private Map<String, Integer> parseResponse(String response) {
		logger.info("Inside parseResponse() in KpiComparisonDaoImpl zone is {}", response);
		Map<String, Integer> responseMap = new HashMap<>();
		try {
			return new Gson().fromJson(response, new TypeToken<Map<String, Integer>>() {
			}.getType());
		} catch (JsonSyntaxException e) {
			logger.error("Exception while parsing zone map in parseResponse() :{}", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Exception while parsing zone map parseResponse():{}", Utils.getStackTrace(e));
		}
		return responseMap;
	}

	public Map<String, Integer> getClutterColors(String name) {
		Map<String, Integer> clutterColor = new HashMap<>();
		try {
			String color = dao.getSystemConfigurationByName(name).get(0).getValue();
			return parseResponse(color);
		} catch (Exception e) {
			logger.error("Exception while getting clutter colors in getClutterColors() {} ", Utils.getStackTrace(e));
			throw new DaoException(ExceptionUtils.getStackTrace(e));
		}

	}
}
