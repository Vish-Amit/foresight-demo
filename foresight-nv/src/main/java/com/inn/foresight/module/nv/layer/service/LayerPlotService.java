package com.inn.foresight.module.nv.layer.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import com.inn.foresight.module.nv.layer.wrapper.BoundaryDataWrapper;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.inn.commons.hadoop.hbase.HBaseResult;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.maplayer.utils.GenericLayerUtil;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;
import com.inn.foresight.module.nv.layer.LayerPlotMicroServiceRestImpl;

public interface LayerPlotService {
	
	Logger logger = LogManager.getLogger(LayerPlotService.class);
	
	public List<List<String>> getLayerPlotData(String tableName, Integer zoom, List<String> columnList, Double nELat,
			Double nELng, Double sWLat, Double sWLng, String minDate, String maxDate,String postFix,String gridColName, String recordType, String mapType, String filterColName) throws ParseException;

	String getLayerData(List<String> columnList, String tableName, Integer zoom, Double nELat, Double nELng,
			Double sWLat, Double sWLng, String minDate, String maxDate, String postFix, String gridColName,String recordType, String mapType, String filterColName);

    List<List<String>> getBoundaryData(BoundaryDataWrapper wrapper);
    
    public String getResponseForCluster(String encryptedString);

	public Object getCoverageClusterHbaseData(String json) throws IOException;
}
