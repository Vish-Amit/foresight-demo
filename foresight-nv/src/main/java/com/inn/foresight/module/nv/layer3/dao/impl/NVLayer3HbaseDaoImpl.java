package com.inn.foresight.module.nv.layer3.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.Validate;
import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseClient;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.lang.StringUtils;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.module.nv.NVConfigUtil;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.dao.INVLayer3HbaseDao;

/**
 * The Class AbstractHDFSDao.
 *
 * @author innoeye date - 29-Dec-2017 4:53:39 PM
 */
@Service("NVLayer3HbaseDaoImpl")
public class NVLayer3HbaseDaoImpl extends AbstractHBaseDao implements INVLayer3HbaseDao{


	/** The logger. */
	private static Logger logger = LogManager.getLogger(NVLayer3HbaseDaoImpl.class);

	@Autowired
	private HBaseClient hbaseClient;

	@Override
	public List<HBaseResult> getSignalKPIStats(List<Get> getList) throws IOException {
		logger.info("Going to get NVLayer3 Messages");
		List<HBaseResult> result = getResultByPool(getList,
				ConfigUtils.getString(NVLayer3Constants.QMDL_KPI_TABLE), QMDLConstant.COLUMN_FAMILY.getBytes());
		Validate.checkNotEmpty(result,"No result Found for current request");
		return result;
	}


	@Override
	public HBaseResult getQMDLDataFromHBase(Get get, String tableName) throws IOException {
		if (get != null && tableName != null) {
			return getResultByPool(get, tableName, QMDLConstant.COLUMN_FAMILY.getBytes());
		}
		throw new BusinessException(QMDLConstant.INVALID_ARGUMENT);
	}

	@Override
	public List<HBaseResult> scanQMDLDataFromHBaseWithPagination(String tableName, String workOderId, String direction,Long timeStamp)
			throws IOException {
		Scan scan = new Scan();
		addColumnsIntoLayer3SignalMsg(scan);
		FilterList filter = addFilterToLayer3SignalMsg(workOderId, direction, timeStamp);
		scan.setFilter(filter);
		return scanResultByPool(scan, tableName, QMDLConstant.COLUMN_FAMILY.getBytes());
	}

	private FilterList addFilterToLayer3SignalMsg(String workOderId, String direction, Long timeStamp) {
		FilterList filter = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		filter.addFilter(new PrefixFilter((workOderId+QMDLConstant.DELIMETER).getBytes()));
		filter.addFilter(new PageFilter(500L));
		if (timeStamp != null) {
			String lastRowKey = workOderId+QMDLConstant.DELIMETER+timeStamp;
			if (direction.equals(NVLayer3Constants.FORWARD)) {
				filter.addFilter(new RowFilter(CompareOperator.GREATER, new BinaryComparator(lastRowKey.getBytes())));
			} else {
				filter.addFilter(new RowFilter(CompareOperator.LESS, new BinaryComparator(lastRowKey.getBytes())));
			}
		}
		return filter;
	}

	private void addColumnsIntoLayer3SignalMsg(Scan scan) {
		scan.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(),QMDLConstant.TIMESTAMP.getBytes() );
		scan.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(),QMDLConstant.SIP_EVENT.getBytes() );
		scan.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.EMM_MESSAGE.getBytes());
		scan.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.EMS_MESSAGE.getBytes());
		scan.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.B0C0_MSG_TYPE.getBytes());
		scan.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.B0C0_MSG.getBytes());
		scan.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.EMM_BEAN.getBytes());
		scan.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.EMS_BEAN.getBytes());
		scan.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.SIP_EVENT_MSG.getBytes());
		scan.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.DIRECTION.getBytes());
	}	

	private void addColumnsIntoLayer3NeighbourDataForFrameWork(Scan scan) {
		scan.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(),NVLayer3Constants.NEIGHBOUR_DATA_COL.getBytes() );		
		scan.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(),NVLayer3Constants.LAT_LNG_TIMESTAMP_COL.getBytes() );		

	}	

	private void addColumnsIntoLayer3NeighbourData(Scan scan) {
		scan.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(),QMDLConstant.NEIGHBOUR.getBytes() );		
	}	

	@Override
	public HBaseResult getSignalMessageDetail(String tableName, String msgType, String rowKey) throws IOException {
		Get get=new Get(rowKey.getBytes());
		get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), msgType.getBytes());
		if (tableName != null) {
			return getResultByPool(get, tableName, QMDLConstant.COLUMN_FAMILY.getBytes());
		}
		throw new BusinessException(QMDLConstant.INVALID_ARGUMENT);

	}

	@Override
	public HBaseResult getKpiStatsData(String tableName, String rangeStats, String workorderId, String kpi) throws IOException {
		String key=workorderId+kpi;
		Get get=new Get(key.getBytes());

		get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), rangeStats.getBytes());
		if (tableName != null) {
			return getResultByPool(get, tableName, QMDLConstant.COLUMN_FAMILY.getBytes());
		}
		throw new BusinessException(QMDLConstant.INVALID_ARGUMENT);
	}


	/**
	 * Insert QMDL data into hbase.
	 *
	 * @param putList the put list
	 * @param tableName the table name
	 * @return the string
	 * @throws IOException
	 * @throws DaoException the dao exception
	 */
	@Override
	public String insertQMDLDataIntoHbase(List<Put> putList,String tableName) throws IOException {
		if(putList!=null && !putList.isEmpty()){
			insert(tableName,putList);
			return QMDLConstant.SUCCESS;
		}else{
			logger.warn("Getting Empty List {} ",tableName);
		}
		return QMDLConstant.FAILURE;
	}

	@Override
	public List<HBaseResult> getQMDLDataFromHBase(List<Get> getList, String tableName) throws IOException {
		if (getList != null && !getList.isEmpty()&& tableName != null) {
			return getResultByPool(getList, tableName, QMDLConstant.COLUMN_FAMILY.getBytes());
		}
		throw new BusinessException(QMDLConstant.INVALID_ARGUMENT);
	}

	@Override
	public List<HBaseResult> scanQMDLDataFromHBaseWithPagination(String tableName, String prefix, String direction,
			String lastRowKey) throws IOException {
		Scan scan = new Scan();
		addColumnsIntoLayer3SignalMsg(scan);
		FilterList filter = addFilterToLayer3SignalMsg(prefix, direction, lastRowKey);
		scan.setFilter(filter);
		return scanQMDLDataFromHbase(tableName, scan);
	}

	@Override
	public List<HBaseResult> scanQMDLDataFromHBaseWithPaginationForFramework(String tableName, String prefix, String direction,
			String lastRowKey) throws IOException {
		Scan scan = new Scan();
		scan.setRowPrefixFilter(Bytes.toBytes(prefix));
		return scanQMDLDataFromHbase(tableName, scan);
	}


	@Override
	public List<HBaseResult> scanNeighbourDataFromHBaseForFramework(String tableName, String prefix) throws IOException {
		Scan scan = new Scan();
		addColumnsIntoLayer3NeighbourDataForFrameWork(scan);
		scan.setRowPrefixFilter(Bytes.toBytes(prefix));
		return scanQMDLDataFromHbase(tableName,scan);
	}

	@Override
	public List<HBaseResult> scanNeighbourDataFromHBase(String tableName, String prefix) throws IOException {
		Scan scan = new Scan();
		addColumnsIntoLayer3NeighbourData(scan);
		scan.setRowPrefixFilter(Bytes.toBytes(prefix));
		return scanQMDLDataFromHbase(tableName, scan);
	}




	private FilterList addFilterToLayer3SignalMsg(String prefix, String direction, String lastRowKey) {
		FilterList filter = new FilterList(FilterList.Operator.MUST_PASS_ALL);
		filter.addFilter(new PrefixFilter((prefix).getBytes()));

		if (lastRowKey != null && !lastRowKey.equalsIgnoreCase("ALL")) {		
			filter.addFilter(new PageFilter(500L));
			if (!StringUtils.isEmpty(lastRowKey)) {
				if (direction.equals(NVLayer3Constants.FORWARD)) {
					filter.addFilter(new RowFilter(CompareOperator.GREATER,
							new BinaryComparator(lastRowKey.getBytes())));
				} else {
					filter.addFilter(new RowFilter(CompareOperator.LESS,
							new BinaryComparator(lastRowKey.getBytes())));
				}
			}
		}

		return filter;
	}



	@Override
	public List<HBaseResult> scanQMDLDataFromHBaseForMSGCsv(String tableName, Set<String> prefixList) throws IOException {
		List<HBaseResult> arrayList = new ArrayList<>();
		for (String prefix : prefixList) {
			Scan scan = new Scan();
			scan.setRowPrefixFilter(Bytes.toBytes(prefix));
			arrayList.addAll(scanQMDLDataFromHbase(tableName, scan));
		}
		return arrayList;
	}

	@Override
	public List<HBaseResult> scanQMDLDataFromHbase(String tableName, Scan scan) throws IOException {
		logger.info("Inside scanQMDLDataFromHbase : {}",tableName);
		if (scan != null && tableName != null) {
			String isConnectionPoolReq = ConfigUtils.getString(NVConfigUtil.IS_CONNPOOL_REQ);
			logger.info("Connection pool config value {}",isConnectionPoolReq);
			if (Boolean.parseBoolean(isConnectionPoolReq)) {

				return scanResultByPool(scan, tableName, QMDLConstant.COLUMN_FAMILY.getBytes());
			}
			else {
				return hbaseClient.scanResult(scan, tableName, QMDLConstant.COLUMN_FAMILY.getBytes());
			}
		}
		throw new BusinessException(QMDLConstant.INVALID_ARGUMENT);
	}

}
