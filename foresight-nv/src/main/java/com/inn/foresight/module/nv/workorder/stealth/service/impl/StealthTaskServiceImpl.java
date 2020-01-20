package com.inn.foresight.module.nv.workorder.stealth.service.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.commons.lang.StringUtils;
import com.inn.commons.maps.LatLng;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.dao.impl.hbase.AbstractHBaseDao;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.wrapper.SiteDataWrapper;
import com.inn.foresight.module.nv.NVConstant;
import com.inn.foresight.module.nv.core.workorder.dao.impl.IGenericWorkorderDao;
import com.inn.foresight.module.nv.report.utils.StealthUtils;
import com.inn.foresight.module.nv.service.ISiteDetailService;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.workorder.stealth.dao.IStealthTaskHbaseDao;
import com.inn.foresight.module.nv.workorder.stealth.service.IStealthTaskService;
import com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOWrapper;
import com.inn.foresight.module.nv.workorder.wrapper.EnodeBWrapper;
import com.inn.product.systemconfiguration.utils.SystemConfigurationUtils;
import com.inn.product.um.geography.dao.GeographyL4Dao;
import com.inn.product.um.geography.model.GeographyL4;

@Service("StealthTaskServiceImpl")
public class StealthTaskServiceImpl extends AbstractHBaseDao implements IStealthTaskService {
	/** The logger. */
	private static final Logger logger = LogManager.getLogger(StealthTaskServiceImpl.class);
	@Autowired
	private IStealthTaskHbaseDao iStealthTaskHbaseDao;
	@Autowired
	private ISiteDetailService siteDetailService;
	@Autowired
	private GeographyL4Dao iGeographyL4Dao;
	@Autowired
	private IGenericWorkorderDao iGenericWorkorderDao;

	@Override
	public List<StealthWOWrapper> getStealthKPISummary(Integer workorderId,Integer zoomLevel,Long startTime, Long endTime) {
		logger.info("Going to getStealthKPIByWorkorderId : {} ,date : {}", workorderId, startTime);
		List<HBaseResult> list = iStealthTaskHbaseDao.getStealthKPIByWorkorderId(StealthUtils.getScanObj(workorderId));
		logger.info("list is ===>{}", list);
		if (list != null && !list.isEmpty()) {
			LatLng latLng=getReferenceLatLng();
			String type=iGenericWorkorderDao.findByPk(workorderId).getGwoMeta().get(NVConstant.WO_FREQUENCY);
			logger.info("type------------------------>{} ", type);
			return StealthUtils.getStealthKPIsResponse(list,startTime,latLng,zoomLevel,endTime,type);
		}
		return null;
	}
	private LatLng getReferenceLatLng() {
		Double latitude=Double.parseDouble(SystemConfigurationUtils.systemConfMap.get(StealthConstants.REFERENCE_LAT_KEY));
		Double longitude=Double.parseDouble(SystemConfigurationUtils.systemConfMap.get(StealthConstants.REFERENCE_LONG_KEY));
		logger.info("country latitude {}",latitude);
		logger.info("country longitude {}",longitude);
		return new LatLng(latitude,longitude);
	}

	@Override
	public Map<String, Object> getKPIDistributionAndCount(String tableName, String type, String name, Long timeStamp,
			Integer enodeBID,Long endTime) {
		
		logger.info("inside  method getKPIDistributionAndCount tableName {} type {} timeStamp{} ",tableName,type,timeStamp);
		if (endTime != null) {

			List<String> dateList = StealthUtils.getDateRange(new Date(timeStamp), new Date(endTime),
					StealthConstants.DATE_FORMATE);
			List<String> prefixList = getPrefixForEnodeBDashBoard(type, name, enodeBID, dateList);
			logger.info("prefixList ==={}",prefixList);
			List<HBaseResult> list = iStealthTaskHbaseDao
					.getResultListForPreFixList(StealthUtils.getColumnListForENBSummary(), tableName, prefixList);
			return StealthUtils.getKPIDistributionAndCountResponse(list);

		}
		else {
			String prefix = getPrefixForEnodeBDashBoard(type, name, enodeBID, timeStamp);
			logger.info("inside  method getKPIDistributionAndCount prefix {} ", prefix);
			List<HBaseResult> list = iStealthTaskHbaseDao
					.getResultListForPreFix(StealthUtils.getColumnListForENBSummary(), tableName, prefix);
			return StealthUtils.getKPIDistributionAndCountResponse(list);
		}
	}

	private Integer getIdByType(String type, String name) {
		if (StealthConstants.TYPE_GEO_L4.equalsIgnoreCase(type)) {
			try {
				GeographyL4 geographyL4 = iGeographyL4Dao.getGeographyL4ByName(name);
				if (geographyL4 != null) {
					return geographyL4.getId();
				}
			} catch (DaoException e) {
				logger.error("DaoException in getIdByType {} ", ExceptionUtils.getStackTrace(e));
			}
		} else if (StealthConstants.TYPE_SITE.equalsIgnoreCase(type)) {
			List<SiteDataWrapper> list = siteDetailService.getNECellsBySapIds(Arrays.asList(name));
			if (!list.isEmpty()) {
				return list	.get(ForesightConstants.ZERO)
							.getEnodeBId();
			}
		}
		return null;
	}

	@Override
	public List<EnodeBWrapper> getCellWiseData(String tableName, String type, String name,Integer enodeBId, Long startTime,Long endTime) {
		logger.info("Going to get Cell wise Data for enodeBId {} type {} name {}  and time {} ", enodeBId,type,name,startTime);
		try {
			if (((type != null && name != null) || enodeBId != null) && startTime != null
					&& startTime > ForesightConstants.ZERO) {
				List<HBaseResult> list = null;
				if (endTime != null) {
					List<String> dateList = StealthUtils.getDateRange(new Date(startTime), new Date(endTime),
							StealthConstants.DATE_FORMATE);
					List<String> prefixList = getPrefixForEnodeBDashBoard(type, name, enodeBId, dateList);
					logger.info("prefixList ==={}", prefixList);
					list = iStealthTaskHbaseDao.getResultListForPreFixList(StealthUtils.getColumnListForCellSummary(),
							tableName, prefixList);

				} else {
					String prefix = getPrefixForEnodeBDashBoard(type, name, enodeBId, startTime);
					list = iStealthTaskHbaseDao.getResultListForPreFix(StealthUtils.getColumnListForCellSummary(),
							tableName, prefix);
				}
				if (list != null && !list.isEmpty()) {
					return getEnodeBWrapperFromResultSet(list);
				} else {
					logger.info("No Data Found for enodebid {} type {} name {} and timestamp {} ", enodeBId, type, name,
							startTime);
					throw new IllegalArgumentException(StealthConstants.NO_DATA_AVAILABLE_JSON);
				}
			} else {
				logger.info("Invalid parameters enodebid {} type {} name {}  and timestamp {} ", enodeBId, type, name,
						startTime);
				throw new IllegalArgumentException(StealthConstants.INVALID_PARAMETER_JSON);
			}
		}  catch (Exception exception) {
			logger.error("Unable to get Cell Summary Data for EnodebId {} type {} name {} and time {} : Exception {} ", enodeBId,type,name, startTime,Utils.getStackTrace(exception));
			throw new IllegalArgumentException(StealthConstants.FAILURE_JSON);
		}
	}


	private List<EnodeBWrapper> getEnodeBWrapperFromResultSet(List<HBaseResult> list) {
		if (list != null && !list.isEmpty()) {
			List<EnodeBWrapper> wrappers = new ArrayList<>();
			for (HBaseResult hBaseResult : list) {
				try {
					EnodeBWrapper wrapper = new EnodeBWrapper();
					wrapper.setScore(hBaseResult.getString(StealthConstants.COLUMN_SCORE));
					wrapper.setAvgRsrp(hBaseResult.getStringAsDouble(StealthConstants.AVERAGE_RSRP_KEY));
					wrapper.setAvgRsrq(hBaseResult.getStringAsDouble(StealthConstants.AVERAGE_RSRQ_KEY));
					wrapper.setAvgDl(hBaseResult.getStringAsDouble(StealthConstants.AVERAGE_DL_KEY));
					wrapper.setAvgUl(hBaseResult.getStringAsDouble(StealthConstants.AVERAGE_UL_KEY));
					wrapper.setAvgSinr(hBaseResult.getStringAsDouble(StealthConstants.AVERAGE_SINR_KEY));
					wrapper.setCgi(hBaseResult.getString(StealthConstants.CGI_KEY));
					wrappers.add(wrapper);
				} catch (Exception exception) {
					logger.error("Error in adding data {} ", Utils.getStackTrace(exception));
				}
			}

			return getCgiWiseAggrigatedData(wrappers);
		} else {
			logger.info("No Data Found");
			throw new RestException("No Data found ");
		}
	}

	private List<EnodeBWrapper> getCgiWiseAggrigatedData(List<EnodeBWrapper> wrappers) {
		List<EnodeBWrapper> dataList = new ArrayList<>();
		if (wrappers != null && !wrappers.isEmpty()) {
			try {
				Map<String, List<EnodeBWrapper>> cgiWiseMap = wrappers.stream()
						.collect(Collectors.groupingBy(EnodeBWrapper::getCgi));
				for (Entry<String, List<EnodeBWrapper>> entry : cgiWiseMap.entrySet()) {
					if (Utils.isValidList(entry.getValue())) {
						EnodeBWrapper wrapper = new EnodeBWrapper();
						wrapper.setCgi(entry.getKey());
						wrapper.setScore(entry.getValue().get(ForesightConstants.ZERO).getScore());
						OptionalDouble optRsrp = entry.getValue().stream()
								.filter(obj -> Objects.nonNull(obj.getAvgRsrp())).mapToDouble(EnodeBWrapper::getAvgRsrp)
								.average();
						if (optRsrp.isPresent()) {
							wrapper.setAvgRsrp(optRsrp.getAsDouble());
						}

						OptionalDouble optRsrq = entry.getValue().stream()
								.filter(obj -> Objects.nonNull(obj.getAvgRsrq())).mapToDouble(EnodeBWrapper::getAvgRsrq)
								.average();
						if (optRsrq.isPresent()) {
							wrapper.setAvgRsrq(optRsrq.getAsDouble());
						}

						OptionalDouble optSinr = entry.getValue().stream()
								.filter(obj -> Objects.nonNull(obj.getAvgSinr())).mapToDouble(EnodeBWrapper::getAvgSinr)
								.average();
						if (optSinr.isPresent()) {
							wrapper.setAvgSinr(optSinr.getAsDouble());
						}

						OptionalDouble optDl = entry.getValue().stream().filter(obj -> Objects.nonNull(obj.getAvgDl()))
								.mapToDouble(EnodeBWrapper::getAvgDl).average();
						if (optDl.isPresent()) {
							wrapper.setAvgDl(optDl.getAsDouble());
						}

						OptionalDouble optUl = entry.getValue().stream().filter(obj -> Objects.nonNull(obj.getAvgUl()))
								.mapToDouble(EnodeBWrapper::getAvgUl).average();
						if (optUl.isPresent()) {
							wrapper.setAvgUl(optUl.getAsDouble());
						}

						dataList.add(wrapper);
					}
				}
			} catch (Exception e) {
				logger.error("exception inside the method  getCgiWiseAggrigatedData {}", Utils.getStackTrace(e));
			}
		}
		return dataList;
		
	}
	private String getPrefixForEnodeBDashBoard(String type, String name, Integer enodeBId, Long timeStamp) {
		if (enodeBId != null && timeStamp!=null && timeStamp >ForesightConstants.ZERO) {
			return StealthUtils.getPrefixValue(enodeBId, timeStamp);
		} else {
			if(StealthConstants.TYPE_GEO_L4.equalsIgnoreCase(type)) {
				Integer id= getIdByType(type, name);
				logger.info("Got id for type: {} ,id :{}",type,id);
				return StealthUtils.getGeographyL4Prefix(id, timeStamp);
			}else if(StealthConstants.TYPE_SITE.equalsIgnoreCase(type)){
			    return StealthUtils.getPrefixValueForSite(name, timeStamp);
			}
		}
		return null;
	}

	private List<String> getPrefixForEnodeBDashBoard(String type, String name, Integer enodeBId,List<String>dateList) {
	List<String>preFixList=new ArrayList<>();
		for (String date : dateList) {
			if (enodeBId != null) {
				preFixList.add(date + String.valueOf(enodeBId));

			} else {
				if (StealthConstants.TYPE_GEO_L4.equalsIgnoreCase(type)) {
					Integer id = getIdByType(type, name);
					logger.info("Got id for type: {} ,id :{}", type, id);
					preFixList.add(date + StringUtils.reverse(StringUtils.leftPad(String.valueOf(id), 7, "0")));
				} else if (StealthConstants.TYPE_SITE.equalsIgnoreCase(type)) {
					preFixList.add(date + name);

				}
			}
		}
		
		return preFixList;
	}



	

	@Override
	public Map<String, Object> getTopEnodeBDetails(String tableName, String type,
			String name,Long timeStamp) {
		String prefix = null;
		Integer id = getIdByType(type, name);
		prefix = StealthUtils.getGeographyL4Prefix(id,timeStamp);
		List<HBaseResult> list = iStealthTaskHbaseDao.getResultListForPreFix(StealthUtils.getTopWorstColumnList(), tableName, prefix);
		return StealthUtils.getTopAndWorstEnodeBResponse(list);
	}
	
	
	@Override
	public Map<String, Object> getTopEnodeBDetailsByDateRang(String tableName, String type,
			String name, Long startTime, Long endTime) {
		List<String> dateList = StealthUtils.getDateRange(new Date(startTime), new Date(endTime),
				StealthConstants.DATE_FORMATE);
	
		List<String> prefixList = getPrefixForEnodeBDashBoard(type, name, null, dateList);
	
	/*	for (String date : dateList) {
			Integer id = getIdByType(type, name);
			String prefix = date + StringUtils.reverse(StringUtils.leftPad(String.valueOf(id), 7, "0"));*/
			List<HBaseResult> list = iStealthTaskHbaseDao.getResultListForPreFixList(StealthUtils.getTopWorstColumnList(),
					tableName, prefixList);

		//}

		logger.info("The  date wise map is {}", list.size());

		return StealthUtils.getTopAndWorstSiteBResponse(list);
	}

	public Map<Long, String> getRowPrefixList(String rowprefix,String startDate,String endDate) throws ParseException{
		
		Map<Long,String> response = new HashMap<>();
		Set<String> prefix = new HashSet<>();
		SimpleDateFormat sdf=new SimpleDateFormat("ddMMyy");
		Date start= sdf.parse(startDate);
		Date end =sdf.parse(endDate);
		Calendar c=Calendar.getInstance();
		Calendar endCal=Calendar.getInstance();
		c.setTime(start);
		endCal.setTime(end);
		while(c.before(endCal)) {
			response.put(c.getTime().getTime(), rowprefix.concat(sdf.format(c.getTime())));
		prefix.add(rowprefix.concat(sdf.format(c.getTime())));
		c.add(Calendar.DATE,1);
		}
		response.put(end.getTime(), rowprefix.concat(endDate));
		return response;
	}

	@Override
	public Map<Long, List<String>> getDashboardDataFromHbase(List<String> columnList, String tableName,String startDate, 
			String rowkeyPrefix,String endDate) throws IOException {
		Map<Long, List<String>> responseMap = new HashMap<>();
		try {
			Scan scan = new Scan();
			Map<Long, String> rowPrefixList = getRowPrefixList(rowkeyPrefix, startDate, endDate);
			for(Entry<Long, String> prefixMap:rowPrefixList.entrySet())
			{
				List<String> resultList = new ArrayList<>();
				String prefix = prefixMap.getValue();
				logger.info("prefix value is {}", prefix);
				scan.setRowPrefixFilter(Bytes.toBytes(prefix));
				List<HBaseResult> hbaseResultList = scanResultByPool(scan, tableName,
						Bytes.toBytes(ForesightConstants.HBASE_COLUMN_FAMILY));
				logger.info("size of hbase data is  {}", hbaseResultList.size());
				for (HBaseResult hbaseresult : hbaseResultList) {
					for (String columnName : columnList) {
						String column = columnName.split(ForesightConstants.COLON)[ForesightConstants.ONE];
						String columnValue = Utils.hasValidValue(hbaseresult.getString(column))
								? hbaseresult.getString(column)
								: ForesightConstants.EMPTY;
						resultList.add(columnValue);
					}
				}
				if (hbaseResultList.size() == 0) {
					for (String col : columnList) {
						resultList.add(ForesightConstants.EMPTY);
					}
				}
				responseMap.put(prefixMap.getKey(), resultList);
			}
		} catch (Exception e) {
			logger.info("Error in getting data from hbase for dashbaord {}",ExceptionUtils.getStackTrace(e));
			
		}
		return responseMap;
	}

}
