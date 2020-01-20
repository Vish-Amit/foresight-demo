package com.inn.foresight.module.nv.layer3.service.parse.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.inn.commons.Symbol;
import com.inn.commons.hadoop.hbase.HBasePut;
import com.inn.commons.lang.MapUtils;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.layer3.constants.QMDLConstant;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.QMDLLogCodeWrapper;
import com.inn.foresight.module.nv.layer3.service.parse.INVLayer3StatsService;
import com.inn.foresight.module.nv.layer3.service.parse.enums.CarrierIndex;
import com.inn.foresight.module.nv.layer3.service.parse.enums.KPIDataType;
import com.inn.foresight.module.nv.layer3.utils.NVLayer3Utils;

/**
 * The Class NVLayer3StatsServiceImpl.
 *
 * @author innoeye
 * date - 23-Jan-2018 5:25:02 PM
 */
@Service("NVLayer3StatsServiceImpl")
public class NVLayer3StatsServiceImpl extends QMDLConstant implements INVLayer3StatsService {


private final Logger logger = LogManager.getLogger(NVLayer3StatsServiceImpl.class);
	/**
	 * Creates the stats put list for kpi map.
	 *
	 * @param kpiValuesMap contains kpi wise Values map
	 * @param rowPrefix the rowkey prefix for kpi stats table
	 * @return the list
	 */
	@Override
	public List<Put> createStatsPutListForKpiMap(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			String rowPrefix) {
		List<Put> putList = new ArrayList<>();
		
		//putList.addAll(getKPiStatsWithkeyFromMapData(kpiValuesMap, rowPrefix, putList));
		return getKPiStatsFromMapData(kpiValuesMap, rowPrefix, putList);
	}

/**	Private List<Put> getKPiStatsWithkeyFromMapData(Map<String, TreeMap<Long, Object>> kpiValuesMap, String rowPrefix,
			List<Put> putList) {
		for (Entry<String, TreeMap<Long, Object>> kpiWiseMap : kpiValuesMap.entrySet()) {
				if (MapUtils.isNotEmpty(kpiWiseMap.getValue())) {
					HBasePut hBasePut = new HBasePut(Bytes.toBytes(rowPrefix + kpiWiseMap.getKey()),COLUMN_FAMILY.getBytes());
					hBasePut.addAsString(RANGE_STATS,getStatsFromKpiMap(kpiWiseMap.getKey(), kpiWiseMap.getValue()));
					putList.add(hBasePut.get());
				}
		}
		return putList;
	}*/

	private List<Put> getKPiStatsFromMapData(Map<String, TreeMap<Long, Object>> kpiValuesMap, String rowPrefix,
			List<Put> putList) {
		for (Entry<String, TreeMap<Long, Object>> kpiWiseMap : kpiValuesMap.entrySet()) {
				if (MapUtils.isNotEmpty(kpiWiseMap.getValue())) {
					HBasePut hBasePut = new HBasePut(Bytes.toBytes(rowPrefix + kpiWiseMap.getKey()),COLUMN_FAMILY.getBytes());
					hBasePut.addAsString(RANGE_STATS,getStatsFromKpiMap(kpiWiseMap.getKey(), kpiWiseMap.getValue()));
					putList.add(hBasePut.get());
				}
		}
		return putList;
	}

	/**
	 * Gets the stats from kpi sample map.
	 *
	 * @param kpi the kpi name
	 * @param map the kpi sample map
	 * @return the stats from kpi sample map
	 */
	private String getStatsFromKpiMap(String kpi, TreeMap<Long, Object> map) {

		switch (kpi) {
			case UL_THROUGHTPUT :
				return processStats(map, UL_THROUGHTPUT_MAX, UL_THROUGHTPUT_MIN,
						WINDOWS_SIZE_CSV, DECIMAL_PATTERN_FOR_INT, KPIDataType.DOUBLE);

			case DL_THROUGHTPUT :
				return processStats(map, DL_THROUGHTPUT_MAX, DL_THROUGHTPUT_MIN,
						WINDOWS_SIZE_CSV, DECIMAL_PATTERN_FOR_INT, KPIDataType.DOUBLE);

			case LATENCY :
				return processStats(map, LATENCY_MAX, LATENCY_MIN,
						WINDOWS_SIZE_CSV, DECIMAL_PATTERN_FOR_INT, KPIDataType.DOUBLE);
			case SINR :
				return processStats(map, SINR_MAX, SINR_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);

			case RSSI :
				return processStats(map, RSSI_MAX, RSSI_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);

			case RSRP :
				return processStats(map, RSRP_MAX, RSRP_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);

			case RSRQ :
				return processStats(map, RSRQ_MAX, RSRQ_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);
			case SINR_RX1 :
				return processStats(map, SINR_MAX, SINR_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);

			case RSSI_RX1 :
				return processStats(map, RSSI_MAX, RSSI_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);

			case RSRP_RX1 :
				return processStats(map, RSRP_MAX, RSRP_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);

			case RSRQ_RX1 :
				return processStats(map, RSRQ_MAX, RSRQ_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);
			case SINR_RX0 :
				return processStats(map, SINR_MAX, SINR_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);

			case RSSI_RX0 :
				return processStats(map, RSSI_MAX, RSSI_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);

			case RSRP_RX0 :
				return processStats(map, RSRP_MAX, RSRP_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);

			case RSRQ_RX0 :
				return processStats(map, RSRQ_MAX, RSRQ_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);
			case RSCP_3G :
				return processStats(map, RSCP_MAX, RSCP_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);
			case RSSI_3G :
				return processStats(map, RSSI_3G_MAX, RSSI_3G_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);
			case ECIO_3G :
				return processStats(map, ECIO_MAX, ECIO_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);
			case RXLEV_2G :
				return processStats(map, RXLEV_MAX, RXLEV_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);
			case RXQUAL_2G :
				return processStats(map, RXQUAL_MAX, RXQUAL_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);

			case PHYSICAL_CELL_ID :
				return processStatsWithKey(map);
			case BCCH_2G :
				return processStatsWithKey(map);
			case BSIC_2G :
				return processStatsWithKey(map);

			case PUSCH_TX_POWER :
				return processStats(map, PUSCH_TX_POWER_MAX, PUSCH_TX_POWER_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);

			case JITTER :
				return processStats(map, JITTER_MAX,JITTER_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);


			case RESPONSE_TIME :
				return processStats(map, RESPONSE_TIME_MAX,RESPONSE_TIME_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);

			case NUM_RBS :
				return processStats(map, NUMRB_MAX, NUMRB_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);

			case MOS :
				return processStats(map,MOS_MAX, MOS_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);
			
			case INSTANTANEOUS_MOS :
				return processStats(map,MOS_MAX, MOS_MIN,
						WINDOWS_SIZE_B193, DECIMAL_PATTERN, KPIDataType.DOUBLE);
			case MCS :
				return processStatsWithKey(map);

			case TRACKING_AREA_CODE :
				return processStatsWithKey(map);

			case CARRIER_INDEX :
				return processStatsWithKey(map);

			case CQI :
				return processStatsWithKey(map);
			case TIMING_ADVANCE:
				return processStatsWithKey(map);
				
			case OUT_OF_SYNC_BLER :
				return processStatsWithKey(map);

			case UL_EARFCN :
				return processStatsWithKey(map);
			case DL_EARFCN :
				return processStatsWithKey(map);
				
			case DL_BANDWIDTH :
				return processStatsWithKey(map);
			
			case UL_BANDWIDTH :
				return processStatsWithKey(map);
			
			case RANK_INDEX :
				return processStatsWithKey(map);

			case SPATIAL_RANK :
				return processStatsWithKey(map);

			case PMI_INDEX :
				return processStatsWithKey(map);
			
			case CA_TYPE :
				return processStatsWithKey(map);
				
			case PDSCH_THROUGHPUT :
			case PDSCH_THROUGHPUT_PCELL :
			case PDSCH_THROUGHPUT_SCELL1 :
			case PDSCH_THROUGHPUT_SCELL2 :
			case PDSCH_THROUGHPUT_SCELL3 :
			case PDSCH_THROUGHPUT_SCELL4 :
			case PDSCH_THROUGHPUT_SCELL5 :
				return processStats(map, PDSCH_THROUGHTPUT_MAX, PDSCH_THROUGHTPUT_MIN,
						WINDOWS_SIZE_CSV, DECIMAL_PATTERN_FOR_INT, KPIDataType.DOUBLE);
		}
		return Arrays.toString(new Long[EMPTY_ARRAY_SIZE]);
	}

	private String processStatsWithKey(TreeMap<Long, Object> map) {
		TreeMap<Object, Integer> statsMap = new TreeMap<>();
		for (Entry<Long, Object> mapEntry : map.entrySet()) {
			Object key = mapEntry.getValue();
			if (statsMap.containsKey(key)) {
				
				statsMap.put(key, statsMap.get(key) + QMDLConstant.RECORD_COUNT);
			} else {
				statsMap.put(key, QMDLConstant.RECORD_COUNT);
			}
		}
		return new Gson().toJson(statsMap);
	}

	/**
	 * Process stats based on kpi sample map , its ranges , window size & decimal pattern.
	 *
	 * @param map the kpi sample map
	 * @param max the max range for kpi
	 * @param min the min range for kpi
	 * @param windowSize the window size for stats
	 * @param pattern the decimal pattern for stats
	 * @param dataType the data type of kpi value
	 * @return the long[] the stats array
	 */
	private String processStats(NavigableMap<Long, Object> map, Integer max, Integer min, Integer windowSize, String pattern,
			KPIDataType dataType) {
		if (dataType.equals(KPIDataType.DOUBLE)) {
				return genrateKPIStats(map, max, min, windowSize, pattern);
		} else if (dataType.equals(KPIDataType.INTEGER)) {
			return genrateKPIStats(map, max, min,KPIDataType.INTEGER);
		} else if (dataType.equals(KPIDataType.LONG)) {
			return genrateKPIStats(map, max, min,KPIDataType.LONG);
		}
		return Arrays.toString(new Long[EMPTY_ARRAY_SIZE]);
	}

	/**
	 * Genrate KPI stats for kpi having double data type.
	 *
	 * @param map the kpi sample map
	 * @param max the max range for kpi
	 * @param min the min range for kpi
	 * @param windowSize the window size for stats
	 * @param pattern the decimal pattern for stats
	 * @return the long[] the stats array
	 */
	private String genrateKPIStats(NavigableMap<Long, Object> map, Integer max, Integer min, Integer windowSize,
			String pattern) {
		Long[] stats = new Long[(max - min + ForesightConstants.ONE) * windowSize];
		Double precision = NumberUtils.DOUBLE_ONE / windowSize;
		Map<Double, Long> statsMap = createStatsMap(map, pattern);
		Double current = Double.valueOf(min);
		for (int i = NumberUtils.INTEGER_ZERO; i < (max - min + ForesightConstants.ONE) * windowSize; i++) {
			try {
				current = NVLayer3Utils.roundOffDouble(pattern, current);

				Long count = statsMap.get(current);
				stats[i] = count != null ? count : NumberUtils.LONG_ZERO;
				current += precision;
			} catch (ParseException e) {
				logger.error("Error in parsing value {}   error {}",current,Utils.getStackTrace(e));
			}
		}
		return Arrays.toString(stats);
	}

	/**
	 * Creates the stats map for kpi having double data type.
	 *
	 * @param map the kpi sample map
	 * @param format the decimal format
	 * @return the stats map
	 */
	private Map<Double, Long> createStatsMap(NavigableMap<Long, Object> map, String pattern){
		Map<Double, Long> statsMap = new HashMap<>();
		for (Entry<Long, Object> entry : map.entrySet()) {
			try {
				Double key = NVLayer3Utils.roundOffDouble(pattern,((double[]) entry.getValue())[KPI_AVG_INDEX]);
				if (statsMap.containsKey(key)) {
					statsMap.put(key, statsMap.get(key) + NumberUtils.LONG_ONE);
				} else {
					statsMap.put(key, NumberUtils.LONG_ONE);
				}
			} catch (ParseException e) {
				logger.error("Error in parsing value {}  error ",entry.getValue(),Utils.getStackTrace(e));
			}
		}
		return statsMap;
	}

	/**
	 * Genrate KPI stats for kpi having integer/long data type.
	 *
	 * @param map the kpi sample map
	 * @param max the max range for kpi
	 * @param min the min range for kpi
	 * @param dataType the data type of kpi value
	 * @return the long[] the stats array
	 */
	private String genrateKPIStats(NavigableMap<Long, Object> map, Integer max, Integer min, KPIDataType dataType) {
		Long[] stats = new Long[(max - min+1)];
		Map<Number, Long> statsMap = createStatsMap(map);
		Integer current = min;
		for (int i = NumberUtils.INTEGER_ZERO; i < (max - min + ForesightConstants.ONE); i++) {
			Long count ;
			if(dataType.equals(KPIDataType.INTEGER)) {
				count = statsMap.get(current);
			} else {
				count = statsMap.get(current.longValue());
			}
			stats[i] = count != null ? count : NumberUtils.LONG_ZERO;
			current += NumberUtils.INTEGER_ONE;
		}
		return Arrays.toString(stats);
	}

	/**
	 * Creates the stats map for kpi having double data type.
	 *
	 * @param map the kpi sample map
	 * @return the stats map
	 */
	private Map<Number, Long> createStatsMap(NavigableMap<Long, Object> map) {
		Map<Number, Long> statsMap = new HashMap<>();
		for (Entry<Long, Object> entry : map.entrySet()) {
			Number key =(Number)entry.getValue();
			if (statsMap.containsKey(key)) {
				statsMap.put(key, statsMap.get(key) + NumberUtils.LONG_ONE);
			} else {
				statsMap.put(key, NumberUtils.LONG_ONE);
			}
		}
		return statsMap;
	}

	/**
	 * Agg signal param.
	 *
	 * @param key the key
	 * @param value the value
	 * @param kpiMap the kpi map
	 */
	@Override
	public void aggSignalParam(Long key, Double value, NavigableMap<Long, Object> kpiMap) {
		if (value != null && key != null && kpiMap!=null) {
			if (kpiMap.containsKey(key)) {
				double[] wrapper = (double[]) kpiMap.get(key);
				double total = value + (wrapper[KPI_COUNT_INDEX] * wrapper[KPI_AVG_INDEX]);
				wrapper[KPI_COUNT_INDEX] += NumberUtils.LONG_ONE;
				wrapper[KPI_AVG_INDEX] = total / wrapper[KPI_COUNT_INDEX];
			} else {
				double[] wrapper = new double[KPI_ARRAY_LIST_SIZE];
				wrapper[KPI_COUNT_INDEX] = NumberUtils.LONG_ONE;
				wrapper[KPI_AVG_INDEX] = value / wrapper[KPI_COUNT_INDEX];
				kpiMap.put(key, wrapper);
			}
		}
	}

	/**
	 * Agg signal param.
	 *
	 * @param key the key
	 * @param value the value
	 * @param kpiMap the kpi map
	 */
	@Override
	public void aggSignalParam(Long key,Long value,NavigableMap<Long, Object> kpiMap){
		if (kpiMap.containsKey(key)) {
			double[] wrapper = (double[]) kpiMap.get(key);
			double total = value + (wrapper[KPI_COUNT_INDEX] * wrapper[KPI_AVG_INDEX]);
			wrapper[KPI_COUNT_INDEX] += NumberUtils.LONG_ONE;
			wrapper[KPI_AVG_INDEX] = total / wrapper[KPI_COUNT_INDEX];
		} else {
			double[] wrapper = new double[KPI_ARRAY_LIST_SIZE];
			wrapper[KPI_COUNT_INDEX] = NumberUtils.LONG_ONE;
			wrapper[KPI_AVG_INDEX] = value / wrapper[KPI_COUNT_INDEX];
			kpiMap.put(key, wrapper);
		}
	}

	@Override
	public void generateStatsDataFromAggrigatedData(TreeMap<Long, QMDLLogCodeWrapper> woParsedMap, Map<String, TreeMap<Long, Object>> kpiValuesMap) {
		for(Entry<Long, QMDLLogCodeWrapper> qmdlDataEntry:woParsedMap.entrySet()){
			QMDLLogCodeWrapper qmdlData = qmdlDataEntry.getValue();
			generateQMDLDataStats(kpiValuesMap, qmdlData);
			generateCsvDataStats(kpiValuesMap, qmdlData);
			generateMosANDCAStats(kpiValuesMap, qmdlData);
			generateInstantaneousMosData(kpiValuesMap, qmdlData);
		}
		
	}

	private void generateInstantaneousMosData(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {

			if(qmdlData.getInstantaneousMos()!=null){
			TreeMap<Long, Object> instMosMap = kpiValuesMap.containsKey(QMDLConstant.INSTANTANEOUS_MOS)?kpiValuesMap.get(QMDLConstant.INSTANTANEOUS_MOS):new TreeMap<>();
			aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getInstantaneousMos()),instMosMap);
			kpiValuesMap.put(QMDLConstant.INSTANTANEOUS_MOS, instMosMap);
			}		
	}

	private void generateMosANDCAStats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {

		if(qmdlData.getFinalMosG711()!=null){
		TreeMap<Long, Object> mosMap = kpiValuesMap.containsKey(QMDLConstant.MOS)?kpiValuesMap.get(QMDLConstant.MOS):new TreeMap<>();
		aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getFinalMosG711()),mosMap);
		kpiValuesMap.put(QMDLConstant.MOS, mosMap);
		}
		
		if(qmdlData.getCaType()!=null){
			TreeMap<Long, Object> dlEarfcn = kpiValuesMap.containsKey(QMDLConstant.CA_TYPE)?kpiValuesMap.get(QMDLConstant.CA_TYPE):new TreeMap<>();
			dlEarfcn.put(qmdlData.getTimeStamp(), qmdlData.getCaType());
			kpiValuesMap.put(QMDLConstant.CA_TYPE, dlEarfcn);
		}
	}

	private void generateQMDLDataStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		generateRsrpStats(kpiValuesMap, qmdlData);
		generateRsrpRx0Stats(kpiValuesMap, qmdlData);
		generateRsrpRx1Stats(kpiValuesMap, qmdlData);
		generateRsrqStats(kpiValuesMap, qmdlData);
		generateRsrqRx0Stats(kpiValuesMap, qmdlData);
		generateRsrqRx1Stats(kpiValuesMap, qmdlData);
		generateRssiStats(kpiValuesMap, qmdlData);
		generateRssiRx0Stats(kpiValuesMap, qmdlData);
		generateRssiRx1Stats(kpiValuesMap, qmdlData);
		generateSINRStats(kpiValuesMap, qmdlData);
		generateSINRRx0Stats(kpiValuesMap, qmdlData);
		generateSINRRx1Stats(kpiValuesMap, qmdlData);
		generateDlEarfcnStats(kpiValuesMap, qmdlData);
		generateDlBandwidthStats(kpiValuesMap, qmdlData);
		generateUlBandWidthStats(kpiValuesMap, qmdlData);		
		generateTrackingAreaCodeStats(kpiValuesMap, qmdlData);
		generateUlEarfcnStats(kpiValuesMap, qmdlData);
		generateRankIndexStats(kpiValuesMap, qmdlData);
		generateCarrierIndexStats(kpiValuesMap, qmdlData);			
		generateCqiStats(kpiValuesMap, qmdlData);
		generateSpatialIndexStats(kpiValuesMap, qmdlData);
		generatePMIIndexStats(kpiValuesMap, qmdlData);
		generateNumRBStats(kpiValuesMap, qmdlData);
		generatePuschTxPowerStats(kpiValuesMap, qmdlData);	
		generateBlerStats(kpiValuesMap, qmdlData);
		generateTimingAdvanceStats(kpiValuesMap, qmdlData);
		generateMcsStats(kpiValuesMap, qmdlData);
		generatePciStats(kpiValuesMap, qmdlData);
		generateRSCPStats(kpiValuesMap,qmdlData);
		generateECIOStats(kpiValuesMap,qmdlData);
		generateRSSI3GStats(kpiValuesMap,qmdlData);
		generateRXLevStats(kpiValuesMap,qmdlData);
		generateRXQualStats(kpiValuesMap,qmdlData);
		generateBcchStats(kpiValuesMap,qmdlData);
		generateBsicStats(kpiValuesMap,qmdlData);
		generatePDSHCHThrougputStats(kpiValuesMap,qmdlData);
		generatePDSHCHThrougputPCellStats(kpiValuesMap,qmdlData);
		generatePDSHCHThrougputSCell1Stats(kpiValuesMap,qmdlData);
		generatePDSHCHThrougputSCell2Stats(kpiValuesMap,qmdlData);
		generatePDSHCHThrougputSCell3Stats(kpiValuesMap,qmdlData);
		generatePDSHCHThrougputSCell4Stats(kpiValuesMap,qmdlData);
		generatePDSHCHThrougputSCell5Stats(kpiValuesMap,qmdlData);
	}
	private void generatePDSHCHThrougputSCell5Stats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getPdschThroughputSecCell5()!=null){
			TreeMap<Long,Object> pdschThroughputDataMap=kpiValuesMap.containsKey(QMDLConstant.PDSCH_THROUGHPUT_SCELL5)?kpiValuesMap.get(QMDLConstant.PDSCH_THROUGHPUT_SCELL5):new TreeMap<>();
			aggSignalParam(qmdlData.getTimeStamp(), NVLayer3Utils.getAvgFromArray(qmdlData.getPdschThroughputSecCell5()), pdschThroughputDataMap);
			kpiValuesMap.put(QMDLConstant.PDSCH_THROUGHPUT_SCELL5, pdschThroughputDataMap);
		}		
	}

	private void generatePDSHCHThrougputSCell4Stats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getPdschThroughputSecCell4()!=null){
			TreeMap<Long,Object> pdschThroughputDataMap=kpiValuesMap.containsKey(QMDLConstant.PDSCH_THROUGHPUT_SCELL4)?kpiValuesMap.get(QMDLConstant.PDSCH_THROUGHPUT_SCELL4):new TreeMap<>();
			aggSignalParam(qmdlData.getTimeStamp(), NVLayer3Utils.getAvgFromArray(qmdlData.getPdschThroughputSecCell4()), pdschThroughputDataMap);
			kpiValuesMap.put(QMDLConstant.PDSCH_THROUGHPUT_SCELL4, pdschThroughputDataMap);
		}		
	}

	private void generatePDSHCHThrougputSCell3Stats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getPdschThroughputSecCell3()!=null){
			TreeMap<Long,Object> pdschThroughputDataMap=kpiValuesMap.containsKey(QMDLConstant.PDSCH_THROUGHPUT_SCELL3)?kpiValuesMap.get(QMDLConstant.PDSCH_THROUGHPUT_SCELL3):new TreeMap<>();
			aggSignalParam(qmdlData.getTimeStamp(), NVLayer3Utils.getAvgFromArray(qmdlData.getPdschThroughputSecCell3()), pdschThroughputDataMap);
			kpiValuesMap.put(QMDLConstant.PDSCH_THROUGHPUT_SCELL3, pdschThroughputDataMap);
		}		
	}

	private void generatePDSHCHThrougputSCell2Stats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getPdschThroughputSecCell2()!=null){
			TreeMap<Long,Object> pdschThroughputDataMap=kpiValuesMap.containsKey(QMDLConstant.PDSCH_THROUGHPUT_SCELL2)?kpiValuesMap.get(QMDLConstant.PDSCH_THROUGHPUT_SCELL2):new TreeMap<>();
			aggSignalParam(qmdlData.getTimeStamp(), NVLayer3Utils.getAvgFromArray(qmdlData.getPdschThroughputSecCell2()), pdschThroughputDataMap);
			kpiValuesMap.put(QMDLConstant.PDSCH_THROUGHPUT_SCELL2, pdschThroughputDataMap);
		}		
	}

	private void generatePDSHCHThrougputSCell1Stats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getPdschThroughputSecCell1()!=null){
			TreeMap<Long,Object> pdschThroughputDataMap=kpiValuesMap.containsKey(QMDLConstant.PDSCH_THROUGHPUT_SCELL1)?kpiValuesMap.get(QMDLConstant.PDSCH_THROUGHPUT_SCELL1):new TreeMap<>();
			aggSignalParam(qmdlData.getTimeStamp(), NVLayer3Utils.getAvgFromArray(qmdlData.getPdschThroughputSecCell1()), pdschThroughputDataMap);
			kpiValuesMap.put(QMDLConstant.PDSCH_THROUGHPUT_SCELL1, pdschThroughputDataMap);
		}		
	}

	private void generatePDSHCHThrougputPCellStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getPdschThroughputPriCell()!=null){
			TreeMap<Long,Object> pdschThroughputDataMap=kpiValuesMap.containsKey(QMDLConstant.PDSCH_THROUGHPUT_PCELL)?kpiValuesMap.get(QMDLConstant.PDSCH_THROUGHPUT_PCELL):new TreeMap<>();
			aggSignalParam(qmdlData.getTimeStamp(), NVLayer3Utils.getAvgFromArray(qmdlData.getPdschThroughputPriCell()), pdschThroughputDataMap);
			kpiValuesMap.put(QMDLConstant.PDSCH_THROUGHPUT_PCELL, pdschThroughputDataMap);
		}		
	}

	private void generatePDSHCHThrougputStats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
			if(qmdlData.getPdschThroughput()!=null){
				TreeMap<Long,Object> pdschThroughputDataMap=kpiValuesMap.containsKey(QMDLConstant.PDSCH_THROUGHPUT)?kpiValuesMap.get(QMDLConstant.PDSCH_THROUGHPUT):new TreeMap<>();
				aggSignalParam(qmdlData.getTimeStamp(), NVLayer3Utils.getAvgFromArray(qmdlData.getPdschThroughput()), pdschThroughputDataMap);
				kpiValuesMap.put(QMDLConstant.PDSCH_THROUGHPUT, pdschThroughputDataMap);
			}
	}

	private void generateRXLevStats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getRxLev()!=null){
		TreeMap<Long, Object> rxLevMap = kpiValuesMap.containsKey(QMDLConstant.RXLEV_2G)?kpiValuesMap.get(QMDLConstant.RXLEV_2G):new TreeMap<>();
		aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getRxLev()),rxLevMap);
		kpiValuesMap.put(QMDLConstant.RXLEV_2G, rxLevMap);
		}
	}

	private void generateRXQualStats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getRxQual()!=null){
		TreeMap<Long, Object> rxQualMap = kpiValuesMap.containsKey(QMDLConstant.RXQUAL_2G)?kpiValuesMap.get(QMDLConstant.RXQUAL_2G):new TreeMap<>();
		aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getRxQual()),rxQualMap);
		kpiValuesMap.put(QMDLConstant.RXQUAL_2G, rxQualMap);
		}
	}

	private void generateBcchStats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getbCCHChannel()!=null){
			TreeMap<Long, Object> bcchMap = kpiValuesMap.containsKey(QMDLConstant.BCCH_2G)
					? kpiValuesMap.get(QMDLConstant.BCCH_2G) : new TreeMap<>();
					bcchMap.put(qmdlData.getTimeStamp(), qmdlData.getbCCHChannel());
			kpiValuesMap.put(QMDLConstant.BCCH_2G, bcchMap);
		}
	}

	private void generateBsicStats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getbSIC()!=null){
			TreeMap<Long, Object> bsicMap = kpiValuesMap.containsKey(QMDLConstant.BSIC_2G)
					? kpiValuesMap.get(QMDLConstant.BSIC_2G) : new TreeMap<>();
					bsicMap.put(qmdlData.getTimeStamp(), qmdlData.getbSIC());
			kpiValuesMap.put(QMDLConstant.BSIC_2G, bsicMap);
		}
	}

	private void generateRSSI3GStats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getRssi()!=null){
		TreeMap<Long, Object> rssi3GMap = kpiValuesMap.containsKey(QMDLConstant.RSSI_3G)?kpiValuesMap.get(QMDLConstant.RSSI_3G):new TreeMap<>();
		aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getRssi()),rssi3GMap);
		kpiValuesMap.put(QMDLConstant.RSSI_3G, rssi3GMap);
		}
	}

	private void generateECIOStats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getEcio()!=null){
		TreeMap<Long, Object> ecio3GMap = kpiValuesMap.containsKey(QMDLConstant.ECIO_3G)?kpiValuesMap.get(QMDLConstant.ECIO_3G):new TreeMap<>();
		aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getEcio()),ecio3GMap);
		kpiValuesMap.put(QMDLConstant.ECIO_3G, ecio3GMap);
		}
	}

	private void generateRSCPStats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getRscp()!=null){
		TreeMap<Long, Object> rscp3gMap = kpiValuesMap.containsKey(QMDLConstant.RSCP_3G)?kpiValuesMap.get(QMDLConstant.RSCP_3G):new TreeMap<>();
		aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getRscp()),rscp3gMap);
		kpiValuesMap.put(QMDLConstant.RSCP_3G, rscp3gMap);
		}
	}

	private void generateSINRRx1Stats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getsINRRx1Data()!=null){
			TreeMap<Long, Object> sinrRx1Map = kpiValuesMap.containsKey(QMDLConstant.SINR_RX1)?kpiValuesMap.get(QMDLConstant.SINR_RX1):new TreeMap<>();
			aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getsINRRx1Data()),sinrRx1Map);
			kpiValuesMap.put(QMDLConstant.SINR_RX1, sinrRx1Map);
		}
	}

	private void generateSINRRx0Stats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getsINRRx0Data()!=null){
			TreeMap<Long, Object> sinrRx0Map = kpiValuesMap.containsKey(QMDLConstant.SINR_RX0)?kpiValuesMap.get(QMDLConstant.SINR_RX0):new TreeMap<>();
			aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getsINRRx0Data()),sinrRx0Map);
			kpiValuesMap.put(QMDLConstant.SINR_RX0, sinrRx0Map);
		}
	}

	private void generateRssiRx1Stats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getrSSIRx1Data()!=null){
		TreeMap<Long, Object> rssiRx1Map = kpiValuesMap.containsKey(QMDLConstant.RSSI_RX1)?kpiValuesMap.get(QMDLConstant.RSSI_RX1):new TreeMap<>();
		aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getrSSIRx1Data()),rssiRx1Map);
		kpiValuesMap.put(QMDLConstant.RSSI_RX1, rssiRx1Map);
		}
	}

	private void generateRssiRx0Stats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getrSSIRx0Data()!=null){
		TreeMap<Long, Object> rssiRx0Map = kpiValuesMap.containsKey(QMDLConstant.RSSI_RX0)?kpiValuesMap.get(QMDLConstant.RSSI_RX0):new TreeMap<>();
		aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getrSSIRx0Data()),rssiRx0Map);
		kpiValuesMap.put(QMDLConstant.RSSI_RX0, rssiRx0Map);
		}
	}

	private void generateRsrqRx1Stats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getrSRQRx1Data()!=null){
		TreeMap<Long, Object> rsrqRx1Map = kpiValuesMap.containsKey(QMDLConstant.RSRQ_RX1)?kpiValuesMap.get(QMDLConstant.RSRQ_RX1):new TreeMap<>();
		aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getrSRQRx1Data()),rsrqRx1Map);
		kpiValuesMap.put(QMDLConstant.RSRQ_RX1, rsrqRx1Map);
		}
	}

	private void generateRsrqRx0Stats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getrSRQRx0Data()!=null){
		TreeMap<Long, Object> rsrqRx0Map = kpiValuesMap.containsKey(QMDLConstant.RSRQ_RX0)?kpiValuesMap.get(QMDLConstant.RSRQ_RX0):new TreeMap<>();
		aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getrSRQRx0Data()),rsrqRx0Map);
		kpiValuesMap.put(QMDLConstant.RSRQ_RX0, rsrqRx0Map);
		}
	}

	private void generateRsrpRx1Stats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getrSRPRx1Data()!=null){
		TreeMap<Long, Object> rsrpRX1Map = kpiValuesMap.containsKey(QMDLConstant.RSRP_RX1)?kpiValuesMap.get(QMDLConstant.RSRP_RX1):new TreeMap<>();
		aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getrSRPRx1Data()),rsrpRX1Map);
		kpiValuesMap.put(QMDLConstant.RSRP_RX1, rsrpRX1Map);
		}
	}

	private void generateRsrpRx0Stats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getrSRPRx0Data()!=null){
		TreeMap<Long, Object> rsrpRX0Map = kpiValuesMap.containsKey(QMDLConstant.RSRP_RX0)?kpiValuesMap.get(QMDLConstant.RSRP_RX0):new TreeMap<>();
		aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getrSRPRx0Data()),rsrpRX0Map);
		kpiValuesMap.put(QMDLConstant.RSRP_RX0, rsrpRX0Map);
		}
	}

	private void generateSINRStats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getsINRData()!=null){
			TreeMap<Long, Object> sinrMap = kpiValuesMap.containsKey(QMDLConstant.SINR)?kpiValuesMap.get(QMDLConstant.SINR):new TreeMap<>();
			aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getsINRData()),sinrMap);
			kpiValuesMap.put(QMDLConstant.SINR, sinrMap);
			}
		
	}

	private  void generateCsvDataStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		geneateDlthroughPutStats(kpiValuesMap, qmdlData);
		generateUlThroughtPutStats(kpiValuesMap, qmdlData);
		generateLatencyStats(kpiValuesMap, qmdlData);
		generateJitterStats(kpiValuesMap, qmdlData);
		generateResponseTimeStats(kpiValuesMap, qmdlData);
	}

	private  void generateResponseTimeStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getAvgReponseTime()!=null){
			TreeMap<Long,Object> responseTime=kpiValuesMap.containsKey(QMDLConstant.RESPONSE_TIME)?kpiValuesMap.get(QMDLConstant.RESPONSE_TIME):new TreeMap<>();
			aggSignalParam(qmdlData.getTimeStamp(), NVLayer3Utils.getAvgFromArray(qmdlData.getAvgReponseTime()), responseTime);
			kpiValuesMap.put(QMDLConstant.RESPONSE_TIME,responseTime);
		}
	}

	private  void generateJitterStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getAvgJitter()!=null){
			TreeMap<Long,Object> jitter=kpiValuesMap.containsKey(QMDLConstant.JITTER)?kpiValuesMap.get(QMDLConstant.JITTER):new TreeMap<>();
			
			aggSignalParam(qmdlData.getTimeStamp(), NVLayer3Utils.getAvgFromArray(qmdlData.getAvgJitter()), jitter);
			kpiValuesMap.put(QMDLConstant.JITTER,jitter);
		}
	}

	private  void generateLatencyStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getLatency()!=null){
			TreeMap<Long,Object> latency=kpiValuesMap.containsKey(QMDLConstant.LATENCY)?kpiValuesMap.get(QMDLConstant.LATENCY):new TreeMap<>();
			aggSignalParam(qmdlData.getTimeStamp(), NVLayer3Utils.getAvgFromArray(qmdlData.getLatency()), latency);
			kpiValuesMap.put(QMDLConstant.LATENCY, latency);
			
		}
	}

	private  void generateUlThroughtPutStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getUlThroughPut()!=null){
			TreeMap<Long,Object> ulDataMap=kpiValuesMap.containsKey(QMDLConstant.UL_THROUGHTPUT)?kpiValuesMap.get(QMDLConstant.UL_THROUGHTPUT):new TreeMap<>();
			aggSignalParam(qmdlData.getTimeStamp(), NVLayer3Utils.getAvgFromArray(qmdlData.getUlThroughPut()), ulDataMap);
			kpiValuesMap.put(QMDLConstant.UL_THROUGHTPUT, ulDataMap);
			
		}
	}

	private  void geneateDlthroughPutStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getDlThroughPut()!=null){
			TreeMap<Long,Object> dlDataMap=kpiValuesMap.containsKey(QMDLConstant.DL_THROUGHTPUT)?kpiValuesMap.get(QMDLConstant.DL_THROUGHTPUT):new TreeMap<>();
			aggSignalParam(qmdlData.getTimeStamp(), NVLayer3Utils.getAvgFromArray(qmdlData.getDlThroughPut()), dlDataMap);
			kpiValuesMap.put(QMDLConstant.DL_THROUGHTPUT, dlDataMap);
		}
	}

	private  void generatePciStats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getPci()!=null) {
			TreeMap<Long, Object> pciMap = kpiValuesMap.containsKey(QMDLConstant.PHYSICAL_CELL_ID)
					? kpiValuesMap.get(QMDLConstant.PHYSICAL_CELL_ID) : new TreeMap<>();
			pciMap.put(qmdlData.getTimeStamp(), qmdlData.getPci());
			kpiValuesMap.put(QMDLConstant.PHYSICAL_CELL_ID, pciMap);
		}
	}

	private void generateMcsStats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if (qmdlData.getMcs() != null) {
			TreeMap<Long, Object> mcs = kpiValuesMap.containsKey(QMDLConstant.MCS) ? kpiValuesMap.get(QMDLConstant.MCS)
					: new TreeMap<>();
			mcs.put(qmdlData.getTimeStamp(), qmdlData.getMcs());
			kpiValuesMap.put(QMDLConstant.MCS, mcs);
		}
	}

	private  void generateTimingAdvanceStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if (qmdlData.getTimingAdvance() != null) {
		TreeMap<Long, Object> timingAdvance = kpiValuesMap.containsKey(QMDLConstant.TIMING_ADVANCE)
				? kpiValuesMap.get(QMDLConstant.TIMING_ADVANCE) : new TreeMap<>();
				timingAdvance.put(qmdlData.getTimeStamp(), qmdlData.getTimingAdvance());
				kpiValuesMap.put(QMDLConstant.TIMING_ADVANCE, timingAdvance);
			}
	}

	private  void generateBlerStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if (qmdlData.getOutOfSyncBler() != null) {	
		TreeMap<Long, Object> bler = kpiValuesMap.containsKey(QMDLConstant.OUT_OF_SYNC_BLER)
				? kpiValuesMap.get(QMDLConstant.OUT_OF_SYNC_BLER) : new TreeMap<>();
				bler.put(qmdlData.getTimeStamp(), NVLayer3Utils.getAvgFromArray(qmdlData.getOutOfSyncBler()));
				kpiValuesMap.put(QMDLConstant.OUT_OF_SYNC_BLER, bler);
			}
	}

	private  void generatePuschTxPowerStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if (qmdlData.getPuschTxPower() != null) {
			TreeMap<Long, Object> pushTxPowerMap = kpiValuesMap.containsKey(QMDLConstant.PUSCH_TX_POWER)
					? kpiValuesMap.get(QMDLConstant.PUSCH_TX_POWER) : new TreeMap<>();
			aggSignalParam(qmdlData.getTimeStamp(), NVLayer3Utils.getAvgFromArray(qmdlData.getPuschTxPower()),
					pushTxPowerMap);
			kpiValuesMap.put(QMDLConstant.PUSCH_TX_POWER, pushTxPowerMap);
		}
	}

	private  void generateNumRBStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if (qmdlData.getNumRB() != null) {
			TreeMap<Long, Object> numRb = kpiValuesMap.containsKey(QMDLConstant.NUM_RBS)
					? kpiValuesMap.get(QMDLConstant.NUM_RBS) : new TreeMap<>();
					aggSignalParam(qmdlData.getTimeStamp(), NVLayer3Utils.getAvgFromArray(qmdlData.getNumRB()),
							numRb);
			/* numRb.put(qmdlData.getTimeStamp(),qmdlData.getNumRB()); */
			kpiValuesMap.put(QMDLConstant.NUM_RBS, numRb);
		}
	}

	private void generatePMIIndexStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if (qmdlData.getIndexPMI() != null) {
			TreeMap<Long, Object> pMIIndex = kpiValuesMap.containsKey(QMDLConstant.PMI_INDEX)
					? kpiValuesMap.get(QMDLConstant.PMI_INDEX) : new TreeMap<>();
			
			pMIIndex.put(qmdlData.getTimeStamp(), qmdlData.getIndexPMI());
			kpiValuesMap.put(QMDLConstant.PMI_INDEX, pMIIndex);
		}
	}

	private void generateSpatialIndexStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if (qmdlData.getSpatialRank() != null
				&& qmdlData.getSpatialRank().contains(Symbol.SPACE_STRING)) {
			TreeMap<Long, Object> spatialIndex = kpiValuesMap.containsKey(QMDLConstant.SPATIAL_RANK)
					? kpiValuesMap.get(QMDLConstant.SPATIAL_RANK) : new TreeMap<>();
			try {
				spatialIndex.put(qmdlData.getTimeStamp(),
						Integer.parseInt(qmdlData.getSpatialRank().split(Symbol.SPACE_STRING)[1]));
			} catch (Exception e) {
				logger.error("Error in setDataB126Version103BeanIntoKpiMap spatialRank : {} trace : {}",
						qmdlData.getSpatialRank(), ExceptionUtils.getStackTrace(e));
			}
			kpiValuesMap.put(QMDLConstant.SPATIAL_RANK, spatialIndex);
		}
	}

	private void generateCqiStats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getCqiCwo()!=null){
		TreeMap<Long, Object> cqiMap = kpiValuesMap.containsKey(QMDLConstant.CQI) ? kpiValuesMap.get(QMDLConstant.CQI)
				: new TreeMap<>();
		cqiMap.put(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArrayAndReturnIntegerValue(qmdlData.getCqiCwo()));
		kpiValuesMap.put(QMDLConstant.CQI, cqiMap);
		}
	}

	private void generateCarrierIndexStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getCarrierIndex()!=null){
		TreeMap<Long, Object> carrierIndex = kpiValuesMap.containsKey(QMDLConstant.CARRIER_INDEX)
				? kpiValuesMap.get(QMDLConstant.CARRIER_INDEX) : new TreeMap<>();
				carrierIndex.put(qmdlData.getTimeStamp(),
						CarrierIndex.valueOf(qmdlData.getCarrierIndex().toUpperCase()).getValue());
				kpiValuesMap.put(QMDLConstant.CARRIER_INDEX, carrierIndex);
		}
	}

	private void generateRankIndexStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if (qmdlData.getRankIndex() != null) {
			TreeMap<Long, Object> rankIndex = kpiValuesMap.containsKey(QMDLConstant.RANK_INDEX)
					? kpiValuesMap.get(QMDLConstant.RANK_INDEX) : new TreeMap<>();
			rankIndex.put(qmdlData.getTimeStamp(), qmdlData.getRankIndex());
			kpiValuesMap.put(QMDLConstant.RANK_INDEX, rankIndex);
		}
	}

	private void generateUlEarfcnStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getUlEarfcn()!=null){
		TreeMap<Long, Object> ulEarfcn = kpiValuesMap.containsKey(QMDLConstant.UL_EARFCN)
				? kpiValuesMap.get(QMDLConstant.UL_EARFCN) : new TreeMap<>();
				ulEarfcn.put(qmdlData.getTimeStamp(), qmdlData.getUlEarfcn());
				kpiValuesMap.put(QMDLConstant.UL_EARFCN, ulEarfcn);
		}
	}

	private void generateTrackingAreaCodeStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getTracking_Area_Code()!=null){
		TreeMap<Long, Object> tacMap = kpiValuesMap.containsKey(QMDLConstant.TRACKING_AREA_CODE)
				? kpiValuesMap.get(QMDLConstant.TRACKING_AREA_CODE) : new TreeMap<>();
				tacMap.put(qmdlData.getTimeStamp(), qmdlData.getTracking_Area_Code());
				kpiValuesMap.put(QMDLConstant.TRACKING_AREA_CODE, tacMap);
		}
	}

	private void generateUlBandWidthStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getUl_Bandwidth()!=null){
		TreeMap<Long, Object> ulBandwidth = kpiValuesMap.containsKey(QMDLConstant.UL_BANDWIDTH)
				? kpiValuesMap.get(QMDLConstant.UL_BANDWIDTH) : new TreeMap<>();
				ulBandwidth.put(qmdlData.getTimeStamp(), qmdlData.getUl_Bandwidth());
				kpiValuesMap.put(QMDLConstant.UL_BANDWIDTH, ulBandwidth);
		}
	}

	private void generateDlBandwidthStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getDl_Bandwidth()!=null){
		TreeMap<Long, Object> dlBandwidth = kpiValuesMap.containsKey(QMDLConstant.DL_BANDWIDTH)
				? kpiValuesMap.get(QMDLConstant.DL_BANDWIDTH) : new TreeMap<>();
				dlBandwidth.put(qmdlData.getTimeStamp(), qmdlData.getDl_Bandwidth());
				kpiValuesMap.put(QMDLConstant.DL_BANDWIDTH, dlBandwidth);
		}
	}

	private  void generateDlEarfcnStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
	if(qmdlData.getDlEarfcn()!=null){
		TreeMap<Long, Object> dlEarfcn = kpiValuesMap.containsKey(QMDLConstant.DL_EARFCN)?kpiValuesMap.get(QMDLConstant.DL_EARFCN):new TreeMap<>();
		dlEarfcn.put(qmdlData.getTimeStamp(), qmdlData.getDlEarfcn());
		kpiValuesMap.put(QMDLConstant.DL_EARFCN, dlEarfcn);
	}
	}

	private  void generateRssiStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getrSSIData()!=null){
		TreeMap<Long, Object> rssiMap = kpiValuesMap.containsKey(QMDLConstant.RSSI)?kpiValuesMap.get(QMDLConstant.RSSI):new TreeMap<>();
		aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getrSSIData()),rssiMap);
		kpiValuesMap.put(QMDLConstant.RSSI, rssiMap);
		}
	}

	private  void generateRsrqStats(Map<String, TreeMap<Long, Object>> kpiValuesMap, QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getMeasureRSRQData()!=null){
		TreeMap<Long, Object> rsrqMap = kpiValuesMap.containsKey(QMDLConstant.RSRQ)?kpiValuesMap.get(QMDLConstant.RSRQ):new TreeMap<>();
		aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getMeasureRSRQData()),rsrqMap);
		kpiValuesMap.put(QMDLConstant.RSRQ, rsrqMap);
		}
	}

	private  void generateRsrpStats(Map<String, TreeMap<Long, Object>> kpiValuesMap,
			QMDLLogCodeWrapper qmdlData) {
		if(qmdlData.getMeasureRSRPData()!=null){
		TreeMap<Long, Object> rsrpMap = kpiValuesMap.containsKey(QMDLConstant.RSRP)?kpiValuesMap.get(QMDLConstant.RSRP):new TreeMap<>();
		aggSignalParam(qmdlData.getTimeStamp(),NVLayer3Utils.getAvgFromArray(qmdlData.getMeasureRSRPData()),rsrpMap);
		kpiValuesMap.put(QMDLConstant.RSRP, rsrpMap);
		}
	}

	
	
}