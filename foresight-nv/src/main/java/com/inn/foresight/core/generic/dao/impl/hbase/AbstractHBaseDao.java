package com.inn.foresight.core.generic.dao.impl.hbase;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.BufferedMutator;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.commons.hadoop.hbase.HBaseClient;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.hadoop.hbase.ResultIterator;
import com.inn.commons.hadoop.hbase.rest.HBaseRestClient;
import com.inn.commons.hadoop.hbase.rest.Row;
import com.inn.commons.hadoop.phoenix.PhoenixClient;
import com.inn.commons.hadoop.phoenix.PhoenixCloseableResultIterator;
import com.inn.commons.hadoop.phoenix.PhoenixResultIterator;
import com.inn.commons.hadoop.phoenix.Query;
import com.inn.commons.http.HttpException;
import com.inn.core.generic.utils.Monitorable;
import com.inn.foresight.core.generic.utils.Utils;

/**
 * HBase common functionality
 *
 * @author Nimit Agrawal
 */
public abstract class AbstractHBaseDao implements Monitorable {

    /** The logger */
    private Logger logger = LogManager.getLogger(AbstractHBaseDao.class);

    /**
     * HBase Client
     */
    @Autowired
    private HBaseClient hbaseClient;

    /**
     * Phoenix Client
     */
    @Autowired
    private PhoenixClient phoenixClient;

    /**
     * HBase Rest Client
     */
    @Autowired
    private HBaseRestClient restClient;

    /**
     * Aggregation Client
     */
    private AggregationClient aggregationClient;

    /**
     * Return {@link Table} instance for given tableName by using connection cache.
     */
    public Table getTable(String tableName) throws IOException {
        return hbaseClient.getTable(tableName);
    }

    public Connection createConnection() throws IOException {
        return hbaseClient.createConnection();
    }

    /**
     * Iterate result by using connection cache.
     */
    public void iterateResultByPool(Scan scan, String tableName, byte[] columnFamily, ResultIterator resultIterator)
            throws IOException {
        hbaseClient.iterateResultByPool(scan, tableName, columnFamily, resultIterator);
    }

    /**
     * Iterate result by using connection cache.
     */
    public void iterateResultByPool(List<Scan> scans, String tableName, byte[] columnFamily,
            ResultIterator resultIterator) throws IOException {
        hbaseClient.iterateResultByPool(scans, tableName, columnFamily, resultIterator);
    }

    public HBaseResult getResult(Get get, String tableName, byte[] columnFamily) throws IOException {
        return hbaseClient.getResult(get, tableName, columnFamily);
    }

    /**
     * Scan result by using connection cache.
     */
    public List<HBaseResult> scanResultByPool(Scan scan, String tableName, byte[] columnFamily) throws IOException {
        return hbaseClient.scanResultByPool(scan, tableName, columnFamily);
    }

    /**
     * Scan result by using connection cache.
     */
    public List<HBaseResult> scanResultByPool(List<Scan> scans, String tableName, byte[] columnFamily)
            throws IOException {
        return hbaseClient.scanResultByPool(scans, tableName, columnFamily);
    }

    /**
     * Execute get on HBase table by using connection cache.
     */
    public HBaseResult getResultByPool(Get get, String tableName, byte[] columnFamily) throws IOException {
        return hbaseClient.getResultByPool(get, tableName, columnFamily);
    }

    /**
     * Execute get on HBase table by using connection cache.
     */
    public List<HBaseResult> getResultByPool(List<Get> gets, String tableName, byte[] columnFamily) throws IOException {
        return hbaseClient.getResultByPool(gets, tableName, columnFamily);
    }

    /**
     * Insert bulk {@link Put} into HBase table. If using multiple times, it is recommended to use
     * {@link BufferedMutator} for this.
     */
    public void insert(String tableName, List<Put> puts) throws IOException {
        hbaseClient.insert(tableName, puts);
    }

    public List<Row> getResultByRest(String tableName, String prefix, String... column) throws HttpException {
        return restClient.getResultByRest(tableName, prefix, column);
    }

    /**
     * Create connection with Apache Phoenix.
     */
    public java.sql.Connection createPhoenixConnection() throws SQLException {
        return phoenixClient.createConnection();
    }

    /**
     * Execute query on Phoenix.
     */
    public void executePhoenixQuery(Query query, PhoenixResultIterator resultIterator) throws SQLException {
        phoenixClient.executePhoenixQuery(query, resultIterator);
    }

    /**
     * Execute query on Phoenix. Could be close when {@link PhoenixCloseableResultIterator}.
     */
    public void executePhoenixQuery(Query query, PhoenixCloseableResultIterator resultIterator) throws SQLException {
        phoenixClient.executePhoenixQuery(query, resultIterator);
    }

    public Long getRowCount(Scan scan, String tableName) throws Throwable {
        return aggregationClient.rowCount(TableName.valueOf(tableName), null, scan);
    }

    public boolean delete(String tableName, String rowkey) throws IOException {
        logger.info("Going to delete data into table {} and rowkey {}", tableName, rowkey);
        Table table = null;
        try {
            table = getTable(tableName);
            Delete deleteRow = new Delete(Bytes.toBytes(rowkey));
            table.delete(deleteRow);
            return true;
        } finally {
            if (table != null) {
                table.close();

            }
        }
    }

    public void insertBoundaryIntoHbase(String rowKey, Map<String, String> keyValueMap, String tableName)
            throws IOException {
        Table table = null;
        logger.info("Going to insert data into table {} and rowkey {}", tableName, rowKey);
        try {
            table = getTable(tableName);
            Put put = new Put(Bytes.toBytes(rowKey));
            for (Entry<String, String> entry : keyValueMap.entrySet()) {
                if (entry.getValue() != null)
                    put.addColumn(Bytes.toBytes("r"), Bytes.toBytes(entry.getKey()), Bytes.toBytes(entry.getValue()));
            }
            table.put(put);
        } finally {
            if (table != null) {
                table.close();
            }
        }
    }
    
  public boolean deleteFromRowPreFilter(String tableName,String rowPreFix) {
      logger.info("inside deleteFromRowPreFilter rowPreFix: {} tableName: {}",rowPreFix,tableName);
		try {
			Table table = getTable(tableName);
			byte[] startRow = Bytes.toBytes(rowPreFix);
			byte[] stopRow = Bytes.toBytes(rowPreFix);
			stopRow[stopRow.length - 1]++; 
			Scan scan = new Scan(startRow, stopRow);
			ResultScanner sc = table.getScanner(scan);
			
			for (Result r : sc) {
				Delete deleteRow = new Delete(r.getRow());
				table.delete(deleteRow);
			}
		} catch (IOException e) {
			logger.error("Error Inside deleteFromRowPreFilter rowPreFix : {}  tableName: {}  \n {}",rowPreFix, tableName,Utils.getStackTrace(e));
			return false;
		}
		 return true;
		
	}

}
