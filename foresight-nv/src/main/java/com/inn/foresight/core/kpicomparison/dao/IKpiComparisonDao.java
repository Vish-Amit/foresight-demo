package com.inn.foresight.core.kpicomparison.dao;

import java.awt.image.BufferedImage;
import java.util.Map;

public interface IKpiComparisonDao {

	Map<String, Integer> getZoneMap();
	
	BufferedImage getClutterImage(String response);
	
	Map<String, Integer> getClutterColors(String name);
}
