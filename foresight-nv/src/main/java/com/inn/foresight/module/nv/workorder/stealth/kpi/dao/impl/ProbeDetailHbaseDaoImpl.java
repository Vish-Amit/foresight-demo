package com.inn.foresight.module.nv.workorder.stealth.kpi.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.commons.Validate;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.report.utils.StealthUtils;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.workorder.stealth.kpi.dao.ProbeDetailHbaseDao;
import com.inn.foresight.module.nv.workorder.stealth.kpi.wrapper.ProbeDetailWrapper;

@Repository("ProbeDetailHbaseDaoImpl")
public class ProbeDetailHbaseDaoImpl extends AbstractHBaseDao implements ProbeDetailHbaseDao {

	private Logger logger = LogManager.getLogger(ProbeDetailHbaseDaoImpl.class);

	@Override
	public String persistDataForProbe(ProbeDetailWrapper wrapper) {
		try {
			Validate.checkNoneNull(wrapper.getStealthTaskResultId(), wrapper.getTimeStamp());
			SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
			String rowkey = StealthUtils.getRowkeyForProbe(wrapper);
			String columnName = sdf.format(new Date(wrapper.getTimeStamp()));
			Put put = new Put(Bytes.toBytes(rowkey));
			String data = StealthUtils.getJsonForProbeColumn(wrapper);
			put.addColumn(Bytes.toBytes(ForesightConstants.HBASE_COLUMN_FAMILY), Bytes.toBytes(columnName),
					Bytes.toBytes(data));
			Table table = super.getTable(StealthConstants.PROBE_DETAIL_TABLE);
			table.put(put);
			return ForesightConstants.SUCCESS_JSON;
		} catch (Exception e) {
			logger.error("Exception in {}",Utils.getStackTrace(e));
			return ForesightConstants.FAILURE_JSON;
		}

	}

}
