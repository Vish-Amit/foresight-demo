package com.inn.foresight.module.nv.layer3.service.parse;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.apache.hadoop.hbase.client.Put;

import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.QMDLLogCodeWrapper;

/**
 * The Interface INVLayer3StatsService.
 *
 * @author innoeye
 * date - 23-Jan-2018 5:28:13 PM
 */
public interface INVLayer3StatsService {

	/**
	 * Creates the stats put list for kpi map.
	 *
	 * @param kpiValuesMap the kpi values map
	 * @param rowPrefix the row prefix
	 * @return the list
	 */
	List<Put> createStatsPutListForKpiMap(Map<String, TreeMap<Long, Object>> kpiValuesMap, String rowPrefix);

	/**
	 * Agg signal param.
	 *
	 * @param key the key
	 * @param value the value
	 * @param kpiMap the kpi map
	 */
	void aggSignalParam(Long key, Long value, NavigableMap<Long, Object> kpiMap);

	/**
	 * Agg signal param.
	 *
	 * @param key the key
	 * @param value the value
	 * @param kpiMap the kpi map
	 */
	void aggSignalParam(Long key, Double value, NavigableMap<Long, Object> kpiMap);

	void generateStatsDataFromAggrigatedData(TreeMap<Long, QMDLLogCodeWrapper> woParsedMap,
			Map<String, TreeMap<Long, Object>> kpiValuesMap);



}
