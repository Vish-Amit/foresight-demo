package com.inn.foresight.module.nv.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.generic.utils.Utils;
@Service("NVHbaseGenericDao")
public class NVHbaseGenericDao extends AbstractHBaseDao{


	/** The logger. */
	
	public static final Logger logger = LogManager.getLogger(NVHbaseGenericDao.class);
	private static final String TABLE_NAME_LOG = "getting  Result Scanner For Filter for table ";
	private static final String ERROR_MSG = "error in getting result in method getResultForRowKey";

	/**
	 * Insert.
	 *
	 * @param Put2 the put 2
	 * @param tableName the table name
	 * @return
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean insert(Put put, String tableName) throws IOException {
		logger.info("Calling method : insert() & TableName : {}" , tableName);
		try {
			final Table hTable =  getTable(tableName);
			hTable.put(put);
			hTable.close();
			return true;
		} catch (final Exception e) {
			logger.info("Exception in insert data into hbase {} ",Utils.getStackTrace(e));
			return false;
		}
	}





	/**
	 * Gets the result list for filter.
	 *
	 * @param filterList the filter list
	 * @param tableName the table name
	 * @param maxResultSize the max result size
	 * @param columFamily the colum family
	 * @param date the date
	 * @param priority the priority
	 * @return the result list for filter
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Result> getResultListForFilter(FilterList filterList,
			String tableName, long maxResultSize, byte[] columFamily,
			String date, String priority) throws IOException {
		Table hTable = null;
		ResultScanner resultScanner = null;
		final List<Result> results = new ArrayList<>();
		try {
			hTable = getTable(tableName);
			final Scan scan = new Scan();
			scan.setFilter(filterList);

			if (columFamily != null) {
				scan.addFamily(columFamily);
			}
			resultScanner = hTable.getScanner(scan);
			for (final Result result : resultScanner) {
				results.add(result);
			}

		} finally {
			if (resultScanner != null) {
				resultScanner.close();
			}
			if (hTable != null) {
				hTable.close();
			}
		}
		return results;
	}

	/**
	 * Gets the result list for filterbyid.
	 *
	 * @param tileId the tile id
	 * @param tableName the table name
	 * @param startRow the start row
	 * @param maxResultSize the max result size
	 * @param startTime the start time
	 * @param endTime the end time
	 * @param columFamily the colum family
	 * @param priority the priority
	 * @return the result list for filterbyid
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("deprecation")
	public List<Result> getResultListForFilterbyid(String tileId,
			String tableName, String startRow, long maxResultSize,
			long startTime, long endTime, byte[] columFamily, String priority)
			throws IOException {
		logger.info("startTime  {} endTime {}", startTime, endTime);
		Table hTable = null;
		ResultScanner resultScanner = null;
		final ArrayList<Object> filters = new ArrayList<>();
		SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(
				Bytes.toBytes("raw"), Bytes.toBytes("priority"),
				CompareOperator.EQUAL, new BinaryComparator(
						Bytes.toBytes(priority)));
		final PrefixFilter idFilter = new PrefixFilter(Bytes.toBytes(tileId));
		filters.add(singleColumnValueFilter);
		filters.add(idFilter);
		try {
			hTable = getTable(tableName);
			final Scan scan = new Scan();
			if (columFamily != null) {
				scan.addFamily(columFamily);
			}
			if (startRow != null) {
				scan.withStartRow(Bytes.toBytes(startRow));
			}
			if (maxResultSize != 0) {
				scan.setMaxResultSize(maxResultSize);
			}
			if (startTime != 0 && endTime != 0) {
				scan.setTimeRange(startTime, endTime);
			}
			resultScanner = hTable.getScanner(scan);
			final ArrayList<Result> resultList = new ArrayList<>();
			for (final Result result : resultScanner) {
				if (result == null) {
                    continue;
                }
				resultList.add(result);
			}
			return resultList;
		} finally {
			if (resultScanner != null) {
				resultScanner.close();
			}
			if (hTable != null) {
				hTable.close();
			}
		}
	}

	/**
	 * Gets the result list for filterbyid only.
	 *
	 * @param tileId the tile id
	 * @param tableName the table name
	 * @param startRow the start row
	 * @param maxResultSize the max result size
	 * @param startTime the start time
	 * @param endTime the end time
	 * @param columFamily the colum family
	 * @return the result list for filterbyid only
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("deprecation")
	public List<Result> getResultListForFilterbyidOnly(String tileId,
			String tableName, String startRow, long maxResultSize,
			long startTime, long endTime, byte[] columFamily)
			throws IOException {
		logger.info("startTime {}  endTime {}",startTime, endTime);
		Table hTable = null;
		ResultScanner resultScanner = null;
		final ArrayList<Object> filters = new ArrayList<>();
		 SingleColumnValueFilter singleColumnValueFilter = new SingleColumnValueFilter(
				Bytes.toBytes("raw"), Bytes.toBytes("priority"),
				CompareOperator.NOT_EQUAL, new BinaryComparator(
						Bytes.toBytes("0")));
		final PrefixFilter idFilter = new PrefixFilter(Bytes.toBytes(tileId));
		filters.add(singleColumnValueFilter);
		filters.add(idFilter);
	
		try {
			hTable = getTable(tableName);
			final Scan scan = new Scan();
		
			if (columFamily != null) {
				scan.addFamily(columFamily);
			}
			if (startRow != null) {
				scan.withStartRow(Bytes.toBytes(startRow));
			}
			if (maxResultSize != 0) {
				scan.setMaxResultSize(maxResultSize);
			}
			if (startTime != 0 && endTime != 0) {
				scan.setTimeRange(startTime, endTime);
			}
			resultScanner = hTable.getScanner(scan);
			final ArrayList<Result> resultList = new ArrayList<>();
			for (final Result result : resultScanner) {
				if (result == null) {
                    continue;
                }
				resultList.add(result);
			}
			return resultList;
		} finally {
			if (resultScanner != null) {
				resultScanner.close();
			}
			if (hTable != null) {
				hTable.close();
			}
		}
	}




	/**
	 * Get row key of data and return Result.
	 *
	 * @param get the get
	 * @param tableName the table name
	 * @param startTime the start time
	 * @param endTime the end time
	 * @param columFamily the colum family
	 * @return the result list for row key
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Result getResultListForRowKey(Get get, String tableName,
			long startTime, long endTime, byte[] columFamily)
			throws IOException {
		logger.info("tableName {}",tableName);

		Table hTable = null;
		try {
			hTable = getTable(tableName);

			if (columFamily != null) {
				get.addFamily(columFamily);
			}
			if (startTime != 0 && endTime != 0) {
				get.setTimeRange(startTime, endTime);
			}
			return hTable.get(get);

		} finally {
			if (hTable != null) {
				hTable.close();
			}
		}
	}

	/**
	 * Gets the result list for filter.
	 *
	 * @param filterList the filter list
	 * @param tableName the table name
	 * @param maxResultSize the max result size
	 * @param columFamily the colum family
	 * @return the result list for filter
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Result> getResultListForFilter(FilterList filterList,
			String tableName, long maxResultSize, byte[] columFamily)
			throws IOException {
		Table hTable = null;
		ResultScanner resultScanner = null;
		final List<Result> results = new ArrayList<>();

		try {
			hTable = getTable(tableName);
			final Scan scan = new Scan();
			scan.setFilter(filterList);

			if (columFamily != null) {
				scan.addFamily(columFamily);
			}
			resultScanner = hTable.getScanner(scan);
			for (final Result result : resultScanner) {
				results.add(result);
			}

		} finally {
			if (resultScanner != null) {
				resultScanner.close();
			}
			if (hTable != null) {
				hTable.close();
			}
		}
		return results;
	}

	/**
	 * Gets the result for rowkey list.
	 *
	 * @param rowKeyList the row key list
	 * @param tablname the tablname
	 * @return the result for rowkey list
	 * @throws RestException             This method returns BufferedImages as response on the basis
	 *             of rowkey in hbase
	 */

	public  Result[] getResultForRowkeyList(List<Get> rowKeyList, String tablname) {
		Table hTable = null;
		try {
			hTable = getTable(tablname);
			 return hTable.get(rowKeyList);
		} catch (final Exception e) {
			logger.error(ERROR_MSG
					+ e.getMessage());

			throw new RestException(
					ERROR_MSG
							+ Utils.getStackTrace(e));
		} finally {
			if (hTable != null) {
				try {
					hTable.close();
				} catch (final IOException e) {
					logger.error("error closing table {}",Utils.getStackTrace(e));
				}
			}

		}

	}

	/**
	 * Gets the result list for prefix filter.
	 *
	 * @param rowkey the rowkey
	 * @param tableName the table name
	 * @param startTime the start time
	 * @param endTime the end time
	 * @param columFamily the colum family
	 * @return the result list for prefix filter
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Result> getResultListForPrefixFilter(String rowkey,
			String tableName, long startTime, long endTime, byte[] columFamily)
			throws IOException {
		logger.info("startTime {}   endTime {}" ,startTime, endTime);
		Table hTable = null;
		ResultScanner resultScanner = null;
		final PrefixFilter idFilter = new PrefixFilter(Bytes.toBytes(rowkey));

		try {
			hTable = getTable(tableName);
			final Scan scan = new Scan();
			scan.setFilter(idFilter);
			if (columFamily != null) {
				scan.addFamily(columFamily);
			}
			if (startTime != 0 && endTime != 0) {
				scan.setTimeRange(startTime, endTime);
			}

			resultScanner = hTable.getScanner(scan);
			final ArrayList<Result> resultList = new ArrayList<>();
			for (final Result result : resultScanner) {
				if (result == null) {
                    continue;
                }
				resultList.add(result);
			}
			return resultList;
		} finally {
			if (resultScanner != null) {
				resultScanner.close();
			}
			if (hTable != null) {
				hTable.close();
			}
		}
	}

	/**
	 * Gets the result list for filter with time range.
	 *
	 * @param scan the scan
	 * @param preFix the pre fix
	 * @param hTable the h table
	 * @param columFamily the colum family
	 * @param endTime the end time
	 * @param startTime the start time
	 * @return the result list for filter with time range
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Result> getResultListForFilterWithTimeRange(Scan scan,
			String preFix, Table hTable, byte[] columFamily, long endTime,
			long startTime) throws IOException {
		ResultScanner resultScanner = null;
		
		List<Result> results = new ArrayList<>();
		try {
			if(preFix!=null)
			{
				byte[] startRow = Bytes.toBytes(preFix);
				byte[] endRow = new byte[startRow.length + 1];
				System.arraycopy(startRow, 0, endRow, 0, startRow.length);
				endRow[startRow.length] = (byte) 255;
				scan.withStartRow(startRow);
				scan.withStopRow(endRow);
			}
			if (endTime > 0 && startTime > 0) {
				 scan.setTimeRange(startTime, endTime);

			}
			resultScanner = hTable.getScanner(scan);
			
			if (resultScanner != null) {
				for (Result result : resultScanner) {	
					results.add(result);
				}
			} else {
				logger.error("getiing error in scan {}", resultScanner);
			}
		} catch (Exception e) {
			logger.error("getting the error {}", Utils.getStackTrace(e));
		} finally {
			if (resultScanner != null) {
				resultScanner.close();
			}
		}
		return results;
	}

	/**
	 * Gets the result list for filter with time range test.
	 *
	 * @param scan the scan
	 * @param preFix1 the pre fix 1
	 * @param hTable the h table
	 * @param columFamily the colum family
	 * @param endTime the end time
	 * @param startTime the start time
	 * @param lastRowKey the last row key
	 * @return the result list for filter with time range test
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Result> getResultListForFilterWithTimeRangeTest(Scan scan,
			String preFix1, HTable hTable, byte[] columFamily, long endTime,
			long startTime,String lastRowKey) throws IOException {
		ResultScanner resultScanner = null;
		List<Result> results = new ArrayList<>();
		try {
			byte[] startRow = Bytes.toBytes(lastRowKey);
			byte[] endRow = new byte[startRow.length + 1];
			System.arraycopy(startRow, 0, endRow, 0, startRow.length);
			endRow[startRow.length] = (byte) 255;
			scan.withStartRow(startRow);
			scan.withStopRow(endRow);

			if (endTime > 0 && startTime > 0) {
				scan.setTimeRange(startTime, endTime);

			}
			resultScanner = hTable.getScanner(scan);

			if (resultScanner != null) {
				for (Result result : resultScanner) {
					results.add(result);
				}
			} else {
				logger.error("getiing error in scan {}", resultScanner);
			}
		} catch (Exception e) {
			logger.error("getting the error {}", Utils.getStackTrace(e));
		} finally {
			if (resultScanner != null) {
				resultScanner.close();
			}
		}
		return results;
	}


	/**
	 * Gets the result for rowkey.
	 *
	 * @param rowKey the row key
	 * @param tablname the tablname
	 * @return the result for rowkey
	 */
	public Result getResultForRowkey(Get rowKey, String tablname) {
		Table hTable = null;
		try {
			hTable = getTable(tablname);
			return hTable.get(rowKey);
		} catch (final Exception e) {
			logger.error("error in getting result in method getResultForRowKey "
					+ e.getMessage());

		} finally {
			if (hTable != null) {
				try {
					hTable.close();
				} catch (final IOException e) {
					logger.error("error closing table");
				}
			}

		}
		return null;


	}

	/**
	 * Gets the result list for scan.
	 *
	 * @param scan the scan
	 * @param tableName the table name
	 * @return the result list for scan
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Result> getResultListForScan(Scan scan ,String tableName)
			throws IOException {
		Table hTable = null;
		ResultScanner resultScanner = null;
		final List<Result> results = new ArrayList<>();
		try {
			hTable = getTable(tableName);
			resultScanner = hTable.getScanner(scan);
			for (Result result : resultScanner) {
				results.add(result);
			}
		}catch(Exception e){
			logger.error("Error in getting data with trace {}",Utils.getStackTrace(e));
		}
			finally {

			if (resultScanner != null) {
				resultScanner.close();
			}
			if (hTable != null) {
				hTable.close();
			}
		}
		return results;
	}

	/**
	 * Gets the result list for prefix filter.
	 *
	 * @param tableName the table name
	 * @param fiList the fi list
	 * @param columFamily the colum family
	 * @return the result list for prefix filter
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Result> getResultListForPrefixFilter(String tableName, FilterList fiList, byte[] columFamily)
			throws IOException {

		Table hTable = null;
		ResultScanner resultScanner = null;

		try {
			hTable = getTable(tableName);
			final Scan scan = new Scan();
			if (columFamily != null) {
				scan.addFamily(columFamily);
			}
			if (fiList != null) {
				scan.setFilter(fiList);
			}

			resultScanner = hTable.getScanner(scan);
			final ArrayList<Result> resultList = new ArrayList<>();
			for (final Result result : resultScanner) {
				if (result == null) {
                    continue;
                }
				resultList.add(result);
			}
			return resultList;
		} finally {
			if (resultScanner != null) {
				resultScanner.close();
			}
			if (hTable != null) {
				hTable.close();
			}
		}
	}


/**
 * Gets the result list for prefix filter list.
 *
 * @param rowkeys the rowkeys
 * @param tableName the table name
 * @param startTime the start time
 * @param endTime the end time
 * @param columFamily the colum family
 * @return the result list for prefix filter list
 * @throws IOException Signals that an I/O exception has occurred.
 */
public List<Result> getResultListForPrefixFilterList(List<String> rowkeys,
			String tableName, long startTime, long endTime, byte[] columFamily)
			throws IOException {
		logger.info("startTime {} endTime {}" ,startTime, endTime);
		Table hTable = null;
		ResultScanner resultScanner = null;
		 FilterList filters = new FilterList(FilterList.Operator.MUST_PASS_ONE);
	     for (String key:rowkeys) {
	    	 filters.addFilter(new PrefixFilter(Bytes.toBytes(key)));
		}
	   try {
			hTable = getTable(tableName);
			final Scan scan = new Scan();
			if (columFamily != null) {
				scan.addFamily(columFamily);
			}
				scan.setFilter(filters);

			if (startTime != 0 && endTime != 0) {
				scan.setTimeRange(startTime, endTime);
			}

			resultScanner = hTable.getScanner(scan);
			final ArrayList<Result> resultList = new ArrayList<>();
			for (final Result result : resultScanner) {
				if (result == null) {
                    continue;
                }
				resultList.add(result);
			}
			return resultList;
		} finally {
			if (resultScanner != null) {
				resultScanner.close();
			}
			if (hTable != null) {
				hTable.close();
			}
		}
	}



/**
 * Gets the result list for filter.
 *
 * @param filter the filter
 * @param tableName the table name
 * @param startRow the start row
 * @param maxResultSize the max result size
 * @param startTime the start time
 * @param endTime the end time
 * @param columFamily the colum family
 * @return the result list for filter
 * @throws IOException Signals that an I/O exception has occurred.
 */
public List<Result> getResultListForFilter(FilterList filter,
		String tableName, String startRow, long maxResultSize,
		long startTime, long endTime, byte[] columFamily)
		throws IOException {
	logger.info(TABLE_NAME_LOG +"{}", tableName);

	Table hTable = null;
	ResultScanner resultScanner = null;
	try {
		hTable = getTable(tableName);
		Scan scan = new Scan();

		if (filter != null) {
			scan.setFilter(filter);
		}

		if (columFamily != null) {
			scan.addFamily(columFamily);
		}

		if (startRow != null) {
			scan.withStartRow(Bytes.toBytes(startRow));
		}

		if (maxResultSize != 0) {
			scan.setMaxResultSize(maxResultSize);
		}

		if (startTime != 0 && endTime != 0) {
			scan.setTimeRange(startTime, endTime);
		}

		resultScanner = hTable.getScanner(scan);

		List<Result> resultList = new ArrayList<>();

		// adding ROW key to List
		for (Result result : resultScanner) {
			if (result != null) {
				resultList.add(result);
			}
		}

		return resultList;

	} finally {
		if (resultScanner != null) {
			resultScanner.close();
		}
		if (hTable != null) {
			hTable.close();
		}
	}
}

/**
 * Gets the result list for row key.
 *
 * @param get the get
 * @param tableName the table name
 * @param startRow the start row
 * @param maxResultSize the max result size
 * @param startTime the start time
 * @param endTime the end time
 * @param columFamily the colum family
 * @return ResultScanner provide the Result Scanner for the Filter
 * @throws IOException Signals that an I/O exception has occurred.
 */

public List<Result> getResultListForRowKey(Get get, String tableName,
		String startRow, long maxResultSize, long startTime, long endTime,
		byte[] columFamily) throws IOException {
	logger.info(TABLE_NAME_LOG + tableName);
	List<Result> resultList = new ArrayList<>();
	Table hTable = null;
	try {
		hTable = getTable(tableName);

		if (columFamily != null) {
			get.addFamily(columFamily);
		}
		if (startTime != 0 && endTime != 0) {
			get.setTimeRange(startTime, endTime);
		}

		Result result = hTable.get(get);
		resultList.add(result);
		return resultList;

	} finally {
		if (hTable != null) {
			hTable.close();
		}
	}
}

/**
 * Insert images into hbase by rowkey.
 *
 * @param images the images
 * @param family the family
 * @param qualifier the qualifier
 * @param table the table
 * @param date the date
 * @return true, if successful
 */
public boolean insertImagesIntoHbaseByRowkey(Map<String, byte[]> images,
		byte[] family, byte[] qualifier, String table, String date) {

	//getting List<Put> from Map<String, byte[]> images
	List<Put> putList = getPutListForImages(images, family, qualifier);

	try {
		Table htable = getTable(table);

		htable.put(putList);

	} catch (IOException e) {

		logger.info("error while inserting data into hbase {}",
				Utils.getStackTrace(e));
		return false;
	}
	return true;
}

/**
 * Gets the put list for images.
 *
 * @param images the images
 * @param family the family
 * @param qualifier the qualifier
 * @param date the date
 * @return the put list for images
 */
private List<Put> getPutListForImages(Map<String, byte[]> images,
		byte[] family, byte[] qualifier) {

	List<Put> putList = new ArrayList<>();
	Put put = null;

	Set<Map.Entry<String, byte[]>> imageEntry = images.entrySet();

	for (Entry<String, byte[]> entry : imageEntry) {

		String rowkey = entry.getKey();

		byte[] image = entry.getValue();
		put = new Put(Bytes.toBytes(rowkey));
		put.addColumn(family, qualifier, image);
		putList.add(put);
	}

	return putList;
}

/**
 * Gets the result list for scan.
 *
 * @param scan the scan
 * @param tableName the table name
 * @param cfAlarm the cf alarm
 * @return the result list for scan
 * @throws IOException Signals that an I/O exception has occurred.
 */
public List<Result> getResultListForScan(Scan scan ,String tableName, byte[] cfAlarm) throws IOException {
	Table hTable = null;
	ResultScanner resultScanner = null;
	final List<Result> results = new ArrayList<>();
	try {
		hTable = getTable(tableName);
		resultScanner = hTable.getScanner(scan);
		for (Result result : resultScanner) {
			results.add(result);
		}
	}catch(Exception e){
		logger.error("Error in getting hbase data : {}",Utils.getStackTrace(e));
	}
		finally {

		if (resultScanner != null) {
			resultScanner.close();
		}
		if (hTable != null) {
			hTable.close();
		}
	}
	return results;
}

/**
 * Gets the result from scan.
 *
 * @param scan the scan
 * @param tableName the table name
 * @param columFamily the colum family
 * @return the result from scan
 * @throws IOException Signals that an I/O exception has occurred.
 */
public ResultScanner getResultFromScan(Scan scan, String tableName,
		byte[] columFamily) throws IOException {
	logger.info(TABLE_NAME_LOG + tableName);

	Table hTable = null;
	try {
		hTable = getTable(tableName);

		if (columFamily != null) {
			scan.addFamily(columFamily);
		}

		return hTable.getScanner(scan);
	} catch (Exception e) {
		logger.error("error while getting data from table {}",
				Utils.getStackTrace(e));

	}
	return null;
}



}
