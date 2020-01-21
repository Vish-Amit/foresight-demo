
package com.inn.foresight.core.maplayer.dao.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.io.image.ImageUtils;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.maplayer.dao.IGenericKpiSummaryDao;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.wrapper.TileImageWrapper;

@Repository("GenericKpiSummaryDaoImpl")
public class GenericKpiSummaryDaoImpl extends AbstractHBaseDao implements IGenericKpiSummaryDao {

    @Autowired
    IGenericMapService genMapService;

    Logger logger = LogManager.getLogger(GenericKpiSummaryDaoImpl.class);

    @Override
    public List<TileImageWrapper> getHBaseDataFromGetList(List<Get> getList, String tableName, String column, String columnFamily) {
        List<TileImageWrapper> imageWrapperList = new ArrayList<>();

        List<HBaseResult> resultList = null;
        try {
            resultList = getResultByPool(getList, tableName, Bytes.toBytes(columnFamily));
        } catch (IOException e) {
            logger.error("Getting error in getHBaseDataFromGetList with trace {}", ExceptionUtils.getStackTrace(e));
        }
        for (HBaseResult result : resultList) {
            TileImageWrapper imagesWrapper = new TileImageWrapper();
            BufferedImage image = null;
            try {
                byte[] imgBytes = result.getValue(Bytes.toBytes(column));
                if (imgBytes != null) {
                    String rowkey = result.getRowKey();
                    String substring = rowkey.substring(ForesightConstants.TEN);
                    String[] splitArray = substring.split("(?<=\\G......)");
                    int[] tileIdArray = (int[]) ConvertUtils.convert(splitArray, Integer.TYPE);
                    image = ImageUtils.toBufferedImage(imgBytes);
                    imagesWrapper.setTileId(tileIdArray);
                    imagesWrapper.setImage(image);
                    imageWrapperList.add(imagesWrapper);
                }
            } catch (IOException e) {
                logger.error("Getting error in getHBaseDataFromGetList with trace {}", ExceptionUtils.getStackTrace(e));
            }
        }
        return imageWrapperList;
    }

    /*
     * @Override public List<HBaseResult> getHbaseResultForPrefixList(String tableName, List<String> tileIdPrefixList,
     * List<String> columns, String columnFamily) {
     * 
     * String listSpitSize = ConfigUtil.getConfigProp(ForesightConstants.KPI_SUMMARY_LIST_SPLIT_SIZE);
     * logger.info("Entered into @getHbaseResultForPrefixList method configProp:{}", listSpitSize); if
     * (Utils.checkForValueInString(listSpitSize)) { int splitSize = Integer.parseInt(listSpitSize); List<List<String>>
     * prefixPartition = Lists.partition(tileIdPrefixList, splitSize); List<Scan> scanList = new ArrayList<>();
     * List<HBaseResult> hbaseResult = new ArrayList<>(); try { for (List<String> prefixList : prefixPartition) { Scan
     * scan = genMapService.getScanForRowRange(prefixList, columns); scanList.add(scan); } if (scanList != null &&
     * !scanList.isEmpty()) { hbaseResult = scanResultByPool(scanList, tableName, Bytes.toBytes(columnFamily)); } }
     * catch (Exception e) { logger.error("Getting Error in getHbaseResultForPrefixList method {}",
     * ExceptionUtils.getStackTrace(e)); } return hbaseResult; } else { throw new
     * RestException("[KPI_SUMMARY_LIST_SPLIT_SIZE] config value is  null please create entry in config.properties "); }
     * }
     */
    @Override
    public List<HBaseResult> getHbaseResultForPrefixList(String tableName, List<String> tileIdPrefixList, List<String> columns, String columnFamily) {
        logger.info("Entered into @getHbaseResultForPrefixList method  tileIdPrefixList size :{}", tileIdPrefixList.size());
        List<Scan> scanList = new ArrayList<>();
        List<HBaseResult> hbaseResultList = new ArrayList<>();
        try {
            createScanListForPrefixList(tileIdPrefixList, columns, scanList);
            getHbaseResultListForScanList(tableName, columnFamily, scanList, hbaseResultList);
        } catch (Exception exception) {
            logger.error("Getting Error in getHbaseResultForPrefixList method {}", ExceptionUtils.getStackTrace(exception));
            throw new DaoException(ExceptionUtils.getMessage(exception));
        }
        logger.info("hbaseResult size :{}", hbaseResultList.size());
        return hbaseResultList;
    }

    private void getHbaseResultListForScanList(String tableName, String columnFamily, List<Scan> scanList, List<HBaseResult> hbaseResult) throws IOException {
        if (scanList != null && !scanList.isEmpty()) {
            String listSplitSize = ConfigUtil.getConfigProp(ForesightConstants.KPI_SUMMARY_LIST_SPLIT_SIZE);
            logger.info("list Split Size : {}", listSplitSize);
            if (Utils.checkForValueInString(listSplitSize)) {
                int partitionSize = Integer.parseInt(listSplitSize);
                List<List<Scan>> scanPartitionList = Lists.partition(scanList, partitionSize);
                for (List<Scan> scanSubList : scanPartitionList) {
                    hbaseResult.addAll(scanResultByPool(scanSubList, tableName, Bytes.toBytes(columnFamily)));
                }
            }
        } else {
            logger.warn("Scan list is NULL !!!!!");
        }
    }

    private void createScanListForPrefixList(List<String> tileIdPrefixList, List<String> columns, List<Scan> scanList) {
        for (String rowKey : tileIdPrefixList) {
            Scan scan = genMapService.getScanForSingleRowKeyPrefix(rowKey, columns);
            if (scan != null) {
                scanList.add(scan);
            }
        }
    }
}