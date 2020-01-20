package com.inn.foresight.module.nv.webrtc.model;

import java.util.List;

public interface IViberLayerDao {

	




	List<ViberLayer> getViberLayerData(List<String> tileIdList, Integer zoom, String mediaType, String callDirection,
			String releaseType, String os, Long startTime, Long endTime, String minValue, String maxValue,
			String layerType);
	
}
