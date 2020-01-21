package com.inn.foresight.core.infra.dao.impl;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.lang.StringUtils;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.infra.constants.InBuildingConstants;
import com.inn.foresight.core.infra.dao.BuildingHBaseDao;

@Repository("BuildingHBaseDataDaoImpl")
public class BuildingHBaseDaoImpl extends AbstractHBaseDao implements BuildingHBaseDao {

	/** The logger. */
	private Logger logger = LogManager.getLogger(BuildingHBaseDaoImpl.class);

	@Override
	public byte[] getFloorPlanByRowkey(String rowkey, String kpi) {
	    logger.info("getFloorPlan rowkey:{} , kpi:{}",rowkey,kpi);
		Get get = new Get(Bytes.toBytes(rowkey));
		byte[] byteArray = null;
		try {
			HBaseResult result = getResultByPool(get, InBuildingConstants.TABLE_IBFlOORPLAN,
					InBuildingConstants.CF_IBFlOORPLAN);
			if (result != null) {
				if (StringUtils.isNotEmpty(kpi)) {
					String column = StringUtils.join(kpi, InBuildingConstants.IMAGE);
					logger.info("getFloorPlan column :{} ",column);
					return result.getValue(column.getBytes());
				} else {
					return result.getValue(InBuildingConstants.FP_IMAGE.getBytes());
				}
			}
		} catch (IOException ex) {
			logger.error("Exception while getting floor plan,  {} ", ex.getMessage());
		}
		return byteArray;
	}

	@Override
	public String getFloorLegendByKpi(String rowkey, String kpi) {
		Get get = new Get(Bytes.toBytes(rowkey));
		String legend = null;
		try {
			HBaseResult result = getResultByPool(get, InBuildingConstants.TABLE_IBFlOORPLAN,
					InBuildingConstants.CF_IBFlOORPLAN);
			if (result != null) {
				String column = StringUtils.join(kpi, InBuildingConstants.LEGEND);
				return result.getString(column);
			}
		} catch (IOException e) {
			logger.error("Exception while getting legend ,  {} ", e.getMessage());
		}
		return legend;
	}

	@Override
	public String getPredictionKpiByRowkey(String rowkey) {
		Get get = new Get(Bytes.toBytes(rowkey));
		String predictionKpi = null;
		try {
			HBaseResult result = getResultByPool(get, InBuildingConstants.TABLE_IBFlOORPLAN,
					InBuildingConstants.CF_IBFlOORPLAN);
			if (result != null) {
				predictionKpi = result.getString(InBuildingConstants.PTYPE);
			} else {
				return predictionKpi;
			}
		} catch (IOException e) {
			logger.error("Exception while getting prediction kpi ,  {} ", e.getMessage());
		}
		return predictionKpi;
	}
	
	@Override
	public String getBoundsByRowkey(String rowkey) {
		Get get = new Get(Bytes.toBytes(rowkey));
		String bounds = null;
		try {
			HBaseResult result = getResultByPool(get, InBuildingConstants.TABLE_IBFlOORPLAN,
					InBuildingConstants.CF_IBFlOORPLAN);
			if (result != null) {
				bounds = result.getString(InBuildingConstants.FPB);
			} else {
				return bounds;
			}
		} catch (IOException e) {
			logger.error("Exception while getting bounds ,  {} ", e.getMessage());
		}
		return bounds;
	}

}