package com.inn.foresight.module.nv.webrtc.dao;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.client.Scan;

import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.foresight.module.nv.webrtc.wrapper.WebRTCCallDataWrapper;
import com.inn.foresight.module.nv.webrtc.wrapper.WebRTCCallSummaryDataWrapper;

public interface IWebRTCDao  {

	

	List<WebRTCCallSummaryDataWrapper> setHBaseDataIntoWrapper(Scan scan, String tableName, byte[] columnFamily) throws IOException;
	HBaseResult getResultByRowKey(String rowKey, String tableName, byte[] columnFamily) throws IOException;
	List<WebRTCCallDataWrapper> getViberCallData(String deviceId, Long startTime, Long endTime, 
			String callType);
	
	
}
