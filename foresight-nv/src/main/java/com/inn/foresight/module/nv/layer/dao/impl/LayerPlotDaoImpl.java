package com.inn.foresight.module.nv.layer.dao.impl;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.client.Scan;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.maplayer.dao.IGenericMapDao;
import com.inn.foresight.module.nv.layer.dao.LayerPlotDao;
import com.inn.foresight.module.nv.layer.service.impl.LayerPlotServiceImpl;

@Repository("LayerPlotDaoImpl")
public class LayerPlotDaoImpl  extends AbstractHBaseDao implements LayerPlotDao{
	
	Logger logger = LogManager.getLogger(LayerPlotDaoImpl.class);

	@Override
	public List<HBaseResult> getDataFromHbase(Scan scan, String tableName, String columnFamily) throws IOException {
		return super.scanResultByPool(scan, tableName, columnFamily.getBytes());
	}

}
