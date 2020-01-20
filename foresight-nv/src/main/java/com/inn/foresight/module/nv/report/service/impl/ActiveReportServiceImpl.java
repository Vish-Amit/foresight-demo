package com.inn.foresight.module.nv.report.service.impl;

import java.util.Date;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.configuration.ConfigUtils;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.dao.IAnalyticsRepositoryDao;
import com.inn.foresight.core.report.model.AnalyticsRepository;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.report.dao.INVReportHbaseDao;
import com.inn.foresight.module.nv.report.service.IActiveReportService;
import com.inn.foresight.module.nv.report.utils.ActiveReportUtil;
import com.inn.foresight.module.nv.report.utils.ReportConstants;
import com.inn.foresight.module.nv.report.utils.ReportUtil;
import com.inn.foresight.module.nv.report.wrapper.APReportWrapper;



@Service("ActiveReportServiceImpl")
public class ActiveReportServiceImpl implements IActiveReportService{
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(ActiveReportServiceImpl.class);
	
	@Autowired
	private IAnalyticsRepositoryDao analyticsrepositoryDao ;
	
	@Autowired
	private INVReportHbaseDao nvReportHbaseDao;
	
	@Override
	@SuppressWarnings("unchecked")
	public Response execute(String json) {
		logger.info("Going to execute method for json {} ", json);
		String filePath = ConfigUtils.getString(ReportConstants.NV_REPORTS_PATH) + ReportConstants.ACTIVE
				+ ReportConstants.FORWARD_SLASH+new Date().getTime()+ReportConstants.FORWARD_SLASH;
		ReportUtil.createDirectory(filePath);
		try {
			//Map<String, Object> jsonMap = new ObjectMapper().readValue(json, HashMap.class);
			JSONObject jsonObj=ReportUtil.convertStringToJsonObject(json);
			Long analyticsrepoId = (Long) jsonObj.get(ForesightConstants.ANALYTICAL_REPORT_KEY);
			AnalyticsRepository analyticObj = analyticsrepositoryDao.findByPk(analyticsrepoId.intValue());
			String assignTo = (String) jsonObj.get(NVLayer3Constants.ASSIGN_TO_JSON_KEY);
			return createActiveReport(analyticObj, assignTo, jsonObj, filePath);
			//return createBRTIReportsByReportType(analyticObj, assignTo, jsonObj, filePath);
		} catch (Exception e) {
			logger.info("Unable to Generathe the report {} ",Utils.getStackTrace(e));
			Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
		return Response.ok(ForesightConstants.FAILURE_JSON).build();
	}
	
	
	
	private Response createActiveReport(AnalyticsRepository analyticObj, String assignTo, JSONObject jsonObj,
			String filePath) {
		getActiveReportDataFromHbase(getScanObject(jsonObj));
		
		
		return null;
	}



	private void getActiveReportDataFromHbase(Scan scan) {
		List<HBaseResult> hbaseResultList = nvReportHbaseDao.getActiveReportDataFromHbase(scan,ReportConstants.ACTIVE_TABLE_NAME,ReportConstants.COLUMN_FAMILY_R);
		List<APReportWrapper> reportWrapperList = ActiveReportUtil.convertHbaseResultListToWrapper(hbaseResultList);
		
	}


	public Scan getScanObject(JSONObject jsonObj) {
		Scan scan =new Scan();
		try {
			logger.info("TIMESTAMP 1 {} ,2 {} ",jsonObj.get(ReportConstants.START_TIMESTAMP).toString(),jsonObj.get(ReportConstants.END_TIMESTAMP).toString());
			//FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
			Filter columnValueFilter = new SingleColumnValueFilter(Bytes
			        .toBytes(ReportConstants.COLUMN_FAMILY_R), Bytes.toBytes(ActiveReportUtil.getGeographyColumnNameInHbase(jsonObj.get(ReportConstants.GEOGRAPHY_TYPE).toString())), 
			        CompareOperator.EQUAL, Bytes.toBytes(jsonObj.get(ReportConstants.GEOGRAPHY_NAME).toString()));
			scan.setFilter(columnValueFilter);
			scan.setTimeRange(Long.parseLong(jsonObj.get(ReportConstants.START_TIMESTAMP).toString()), Long.parseLong(jsonObj.get(ReportConstants.END_TIMESTAMP).toString()));
			//scan.addColumn(Bytes.toBytes(ReportConstants.COLUMN_FAMILY_R),Bytes.toBytes(ActiveReportUtil.getGeographyColumnNameInHbase(jsonObj.get(ReportConstants.GEOGRAPHY_TYPE).toString())));
		} catch (NumberFormatException e) {
			logger.error("ooNumberFormatException occured inside method {} ",Utils.getStackTrace(e));
		} catch (Exception e) {
			logger.error("Unable to get the scan object {} ",Utils.getStackTrace(e));
		}
		return scan;
		
	}
	
	



}
