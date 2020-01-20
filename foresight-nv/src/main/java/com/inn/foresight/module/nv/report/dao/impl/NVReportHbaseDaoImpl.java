package com.inn.foresight.module.nv.report.dao.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.core.generic.exceptions.application.BusinessException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.generic.utils.ConfigUtil;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;
import com.inn.foresight.module.nv.report.dao.INVReportHbaseDao;
import com.inn.foresight.module.nv.report.utils.LiveDriveReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.workorder.wrapper.DriveDataWrapper;

@Service("NVReportHbaseDaoImpl")
public class NVReportHbaseDaoImpl extends AbstractHBaseDao implements INVReportHbaseDao,LiveDriveReportConstants {
	private Logger logger = LogManager.getLogger(NVReportHbaseDaoImpl.class);

	@Override
	public DriveDataWrapper getDataFromHbase(String id) {
		logger.info("going to get data from hbase id {}",id);
		DriveDataWrapper dataWrapper=new DriveDataWrapper();
		Get get = new Get(Bytes.toBytes(id));
		get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.JSONSTRING.getBytes());
		get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.SUMMARY_JSON.getBytes());
		String tableName = ConfigUtils.getString(NVLayer3Constants.QMDL_DATA_TABLE);
		try {
			HBaseResult hbaseResults =  getResultByPool(get, tableName, QMDLConstant.COLUMN_FAMILY.getBytes());
			dataWrapper.setSummarydata(hbaseResults.getString(QMDLConstant.SUMMARY_JSON));
			dataWrapper.setQmdlData(hbaseResults.getString(QMDLConstant.JSONSTRING));
			return dataWrapper;
		} catch (Exception e) {
			logger.error("Error IOException in Getting Result from hbase {}  ", Utils.getStackTrace(e));
		}
		return null;
	}
	@Override
	public List<DriveDataWrapper> getImageDataFromHbase(String id,Integer size){
		logger.info("going to get data from hbase id {}",id);
		List<DriveDataWrapper>list=new ArrayList<>();
		for (int i = 0; i < size; i++) {
			Get get = new Get(Bytes.toBytes(id+i));
			String tableName = ConfigUtils.getString(NVLayer3Constants.LAYER3_REPORT_TABLE);
			try {
				HBaseResult hbaseResults =  getResultByPool(get, tableName, QMDLConstant.COLUMN_FAMILY.getBytes());
				if(hbaseResults!=null){
					logger.info("hbaseResults {}",hbaseResults.get().size());
				setSpeedTestImgData(list, hbaseResults);
				}
			} catch (Exception e) {
				logger.error("Error Exception in Getting Result from hbase {}  ", Utils.getStackTrace(e));
			}
		}

		return list;
	}

	@Override
	public boolean saveFileToHbase(File file,Integer reportInstanceId) {
		logger.info("Inside method saveFileToHbase for reportInstanceId {} ",reportInstanceId);
		try {
			byte[] fileData =org.apache.commons.io.FileUtils.readFileToByteArray(file);
			Put put =new Put(Bytes.toBytes(reportInstanceId.toString()));
			put.addColumn(Bytes.toBytes(ReportConstants.NVREPORT_COLUMN_FAMILY), Bytes.toBytes("reportFormat"), Bytes.toBytes(ReportConstants.PDF));
			put.addColumn(Bytes.toBytes(ReportConstants.NVREPORT_COLUMN_FAMILY), Bytes.toBytes(ReportConstants.REPORT_NAME), Bytes.toBytes(file.getName()));
			put.addColumn(Bytes.toBytes(ReportConstants.NVREPORT_COLUMN_FAMILY), Bytes.toBytes(ReportConstants.VALUE), fileData);
			insertFileToHbase(put,ReportConstants.NVREPORT_TABLE);
			logger.info("save file in table  {}",ReportConstants.NVREPORT_TABLE);
			return true;
		} catch (IOException e) {
			logger.error("IOException inside method saveFileToHbase {} ",Utils.getStackTrace(e));
		}catch(Exception e){
			logger.error("Exception inside method saveFileToHbase {} ",Utils.getStackTrace(e));
		}
		return false;
	}
	@Override
	public List<DriveDataWrapper> getNoAcessDataFromLayer3Report(Integer workorderId, String operator, String recipeId)
			 {
		try {
			List<DriveDataWrapper>list=new ArrayList<>();

			List<HBaseResult> resultList=getResultByPool(getRowKeyList(workorderId, operator, recipeId),
					ConfigUtils.getString(NVLayer3Constants.LAYER3_REPORT_TABLE), QMDLConstant.COLUMN_FAMILY.getBytes());
		    for (HBaseResult hBaseResult : resultList) {
		    	DriveDataWrapper dataWrapper=new DriveDataWrapper();
				dataWrapper.setxPoint(hBaseResult.getStringAsDouble(QMDLConstant.XPOINT.getBytes()));
				dataWrapper.setyPoint(hBaseResult.getStringAsDouble(QMDLConstant.YPOINT.getBytes()));
				dataWrapper.setImageName(hBaseResult.getString(QMDLConstant.IMAGE_FILE.getBytes()));
				dataWrapper.setFilePath(hBaseResult.getString(QMDLConstant.FILE_PATH.getBytes()));
			  list.add(dataWrapper);
		    }
		    return list;
		} catch (Exception e) {
			logger.error("Error in Getting Result from hbase {arg0}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
	}

	@Override
	public List<DriveDataWrapper> getNoAcessDataFromLayer3Report(Integer workorderId, String recipeId)
	{
		try {
			List<DriveDataWrapper>list=new ArrayList<>();
			byte[] rowKeyPrefix = Bytes.toBytes(NVLayer3Utils.getRowkeyFromWorkOrderId(workorderId)
					+ NVLayer3Utils.getRowkeyFromWorkOrderId(recipeId));
			
			Scan scan = new Scan();
			scan.setRowPrefixFilter(rowKeyPrefix);

			List<HBaseResult> resultList = scanResultByPool(scan, ConfigUtils.getString(
					NVLayer3Constants.LAYER3_REPORT_TABLE), QMDLConstant.COLUMN_FAMILY.getBytes());
			
			for (HBaseResult hBaseResult : resultList) {
				DriveDataWrapper dataWrapper=new DriveDataWrapper();
				dataWrapper.setxPoint(hBaseResult.getStringAsDouble(QMDLConstant.XPOINT.getBytes()));
				dataWrapper.setyPoint(hBaseResult.getStringAsDouble(QMDLConstant.YPOINT.getBytes()));
				dataWrapper.setImageName(hBaseResult.getString(QMDLConstant.IMAGE_FILE.getBytes()));
				dataWrapper.setFilePath(hBaseResult.getString(QMDLConstant.FILE_PATH.getBytes()));
				
				list.add(dataWrapper);
			}
			return list;
		} catch (Exception e) {
			logger.error("Error in Getting Result from hbase {arg0}  ", Utils.getStackTrace(e));
			throw new BusinessException(NVLayer3Constants.EXCEPTION_SOMETHING_WENT_WRONG_JSON);
		}
	}

	private Get getNoAccessData(String rowKey) {
		Get get = new Get(Bytes.toBytes(rowKey));
		get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.XPOINT.getBytes());
		get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.YPOINT.getBytes());

		get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.IMAGE_FILE.getBytes());
		get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(),QMDLConstant.FILE_PATH.getBytes());
		return get;
	}

	private List<Get> getRowKeyList(Integer workorderId, String operator, String recipeId) {
		List<Get>getList=new ArrayList<>();
		for (Integer i = 0; i < 50; i++) {
			String rowKey = NVLayer3Utils.getRowkeyFromWorkOrderId(workorderId) + operator
					+ NVLayer3Utils.getRowkeyFromWorkOrderId(recipeId,"0")+i;
			getList.add(getNoAccessData(rowKey));
		}
		return getList;
	}

	@Override
	public String getKpiStatsFromHbase(String id,String kpiName) {
		logger.info("going to get data from hbase id ,kpiName{} ,{}",id,kpiName);
		Get get = new Get(Bytes.toBytes(id+kpiName.toLowerCase()));
		get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.RANGE_STATS.getBytes());
		String tableName = ConfigUtils.getString(ConfigUtil.QMDL_KPI_TABLE);
		try {
			HBaseResult hbaseResults =  getResultByPool(get, tableName, QMDLConstant.COLUMN_FAMILY.getBytes());
			return hbaseResults.getString(QMDLConstant.RANGE_STATS);
		} catch (IOException e) {
			logger.error("IOException insdie method getKpiStatsFromHbase {} ", Utils.getStackTrace(e));
		}catch (Exception e) {
			logger.error("Error inside method getKpiStatsFromHbase {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public String getSummaryDataFromHbaseforWorkOrderId(String workorderId) {
		logger.info("Inside method to fetch the summary data from hbase for woId ",workorderId);
		Get get = new Get(Bytes.toBytes(workorderId));
		get.addColumn(QMDLConstant.COLUMN_FAMILY.getBytes(), QMDLConstant.SUMMARY_JSON.getBytes());
		String tableName = ConfigUtils.getString(NVLayer3Constants.QMDL_DATA_TABLE);
		try {
			HBaseResult hbaseResults =  getResultByPool(get, tableName, QMDLConstant.COLUMN_FAMILY.getBytes());
			return hbaseResults.getString(QMDLConstant.SUMMARY_JSON);
		} catch (IOException e) {
			logger.error("IOException insdie method getSummaryDataFromHbaseforWorkOrderId {} ", Utils.getStackTrace(e));
		}catch (Exception e) {
			logger.error("Error inside method getSummaryDataFromHbaseforWorkOrderId {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public void insertFileToHbase(Put nvReportPut, String nvreportTable){
		if (nvReportPut != null) {
			try {
				insert(nvReportPut, nvreportTable);
			} catch (IOException e) {
				logger.error("IOException in uploadFloorPlan : {} ", ExceptionUtils.getStackTrace(e));
			} catch (Exception e) {
				logger.error("Exception in uploadFloorPlan : {} ", ExceptionUtils.getStackTrace(e));
			}
		} else {
			throw new BusinessException("nvReportPut object is null ");
		}
	}

	/**
	 * Insert.
	 *
	 * @param Put2 the put 2
	 * @param tableName the table name
	 * @return
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean insert(Put put, String tableName) throws IOException {
		logger.info("Calling method : insert() & TableName : " + tableName);
		try {
			final Table hTable =  getTable(tableName);
			hTable.put(put);
			hTable.close();
			return true;
		} catch (final Exception e) {
			logger.error("Exception inside the method insert {}",e.getMessage());
			return false;
		}
	}

		@Override
	public File getPDFReportByAnalyticsRepositoryId(Integer analyticsRepositoryId) {
		logger.info("Inside method getPDFReportByReportInstanceId for Id {} ",analyticsRepositoryId);
		Get get = new Get(Bytes.toBytes(analyticsRepositoryId.toString()));
		get.addColumn(ReportConstants.NVREPORT_COLUMN_FAMILY.getBytes(), "value".getBytes());
		get.addColumn(ReportConstants.NVREPORT_COLUMN_FAMILY.getBytes(), "reportName".getBytes());
		String tableName = ReportConstants.NVREPORT_TABLE;
		try {
			HBaseResult hbaseResults =  getResultByPool(get, tableName, ReportConstants.NVREPORT_COLUMN_FAMILY.getBytes());
			logger.info("hbaseResults data {} ",hbaseResults!=null?hbaseResults:null);
			if(hbaseResults!=null) {
			byte[] pdfBytes = hbaseResults.getValue("value".getBytes());
			String fileName = hbaseResults.getString("reportName".getBytes());
			logger.info("fileName is  {} " ,fileName);
				try (OutputStream out = new FileOutputStream( fileName)) {
					out.write(pdfBytes);

					String finalPath = fileName;
					if (ReportUtil.getIfFileExists(finalPath) != null) {
						logger.info("Yes file Exists At Path {} ", finalPath);
						return ReportUtil.getIfFileExists(finalPath);
					} else {
						return null;
					}
				}
			}
		} catch (IOException e) {
			logger.error("IOException insdie method getSummaryDataFromHbaseforWorkOrderId {} ", Utils.getStackTrace(e));
		}catch (Exception e) {
			logger.error("Error inside method getSummaryDataFromHbaseforWorkOrderId {} ", Utils.getStackTrace(e));
		}
		return null;
	}

	/**
	 * Gets the result for drive result by task id.
	 *
	 * @param workOrderId
	 *            the task id
	 * @return List of Result
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public HBaseResult getResultForDriveResultByTaskId(Integer workOrderId) {
		logger.info("inside getResultForDriveResultByTaskId taskId : {}",workOrderId);
		String taskIdValue = ReportConstants.BLANK_STRING;

		if (workOrderId != null) {
			taskIdValue = String.valueOf(workOrderId);
		}
		try {
			Get getLiveDriveData = new Get(Bytes.toBytes(taskIdValue));
			HBaseResult result = getResultByPool(getLiveDriveData,
					ConfigUtils
					.getString(LIVE_DRIVE_TABLE),
			null);
			logger.info("inside getResultForDriveResultByTaskId result : {}",result);
			return result;
		} catch (Exception e) {
			logger.error("unable to get data {}", e.getMessage());
			throw new RestException("unable to get data  " + e.getMessage());
		}
	}

	private String getRowKeyForLayer3(Integer workorderId, String operatorName, String receipeId,String paddingvalue) {
		return NVLayer3Utils.getRowkeyFromWorkOrderId(workorderId) + operatorName
				+ NVLayer3Utils.getRowkeyFromWorkOrderId(receipeId)+paddingvalue;
	}

	@Override
	public List<DriveDataWrapper> getSpeedTestDatafromHbase(Integer workorderId, List<String> receipeList,
			List<String> operatorList) {
		logger.info("going to getSpeedTestDatafromHbase  id {}",workorderId);
		 List<DriveDataWrapper> speedtestDataList = new ArrayList<>();
		int index=0;
		for (String recipe : receipeList) {
			for (Integer i = 0; i < 10; i++) {
				if (operatorList != null && operatorList.size() > i) {
					String rowKey = getRowKeyForLayer3(workorderId, operatorList.get(index), recipe, i.toString());
					logger.info(" rowKey for Speed test data {} ", rowKey);
					Get get = new Get(Bytes.toBytes(rowKey));
					String tableName = ConfigUtils.getString(NVLayer3Constants.LAYER3_REPORT_TABLE);
					try {
						setDataInDriveDataWrapperList(speedtestDataList, get, tableName);
					} catch (Exception e) {
						logger.error("Error Exception in Getting Result from hbase {}  ", Utils.getStackTrace(e));
					}
				}
			}
			index++;
		}
		logger.info("Going to return the speedtestDataList of Size {} ", speedtestDataList.size());

		return speedtestDataList;
	}
	
	@Override
	public void setDataInDriveDataWrapperList(List<DriveDataWrapper> speedtestDataList, Get get, String tableName)
			throws IOException {
		HBaseResult hbaseResults = getResultByPool(get, tableName, QMDLConstant.COLUMN_FAMILY.getBytes());
		if (hbaseResults != null) {
			logger.info("hbaseResults {}", hbaseResults.get().size());
			if (hbaseResults.getValue(QMDLConstant.IMAGE_FILE.getBytes()) != null) {
				setSpeedTestImgData(speedtestDataList, hbaseResults);
			}
		}
	}
	private void setSpeedTestImgData(List<DriveDataWrapper> speedtestDataList, HBaseResult hbaseResults) {
		DriveDataWrapper dataWrapper=new DriveDataWrapper();
		dataWrapper.setImg(hbaseResults.getValue(QMDLConstant.IMAGE_FILE.getBytes()));
		dataWrapper.setPci(hbaseResults.getStringAsInteger(QMDLConstant.PHYSICAL_CELL_ID.getBytes()));
		dataWrapper.setCellid(hbaseResults.getStringAsInteger(QMDLConstant.CELL_ID.getBytes()));
		dataWrapper.setLatitutde(hbaseResults.getStringAsDouble(QMDLConstant.LATITUDE.getBytes()));
		dataWrapper.setLongitude(hbaseResults.getStringAsDouble(QMDLConstant.LONGITUDE.getBytes()));
		dataWrapper.setDltpt(hbaseResults.getStringAsDouble(QMDLConstant.DL_THROUGHTPUT.getBytes()));
		speedtestDataList.add(dataWrapper);
	}
	@Override
	public List<HBaseResult> getActiveReportDataFromHbase(Scan scan, String tableName, String columnFamily) {
		try {
			List<HBaseResult> hbaseResults = scanResultByPool(scan, tableName, columnFamily.getBytes());
			if (hbaseResults != null) {
				logger.info("hbaseResults Size for active/Passive is {}", hbaseResults.size());
				return hbaseResults;
			}
		} catch (IOException e) {
			logger.error("Getting IOException Result from Hbase {}  ", Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Getting Exception in Result from hbase {}  ", Utils.getStackTrace(e));
		}
		return Collections.emptyList();
	}
}
