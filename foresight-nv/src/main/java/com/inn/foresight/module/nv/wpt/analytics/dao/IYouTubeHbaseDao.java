package com.inn.foresight.module.nv.wpt.analytics.dao;

import java.util.List;

import org.apache.hadoop.hbase.filter.Filter;

import com.inn.foresight.module.nv.wpt.analytics.utils.wrapper.YouTubeRawDataWrapper;

public interface IYouTubeHbaseDao {


	List<YouTubeRawDataWrapper> scanDataWithTimeRange(Long minStamp, Long maxStamp, List<Filter> filters)
			throws Exception;

}
