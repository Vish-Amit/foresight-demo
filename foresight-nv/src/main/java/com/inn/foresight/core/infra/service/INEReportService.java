package com.inn.foresight.core.infra.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.inn.foresight.core.infra.wrapper.SiteLayerSelection;


public interface INEReportService {

	byte[] getFileByPath(String fileName);

	Map<String, String> getMappingForGeographies();

	void setColumnWidthForReport(XSSFSheet sheet);

	Map<String, String> generateSiteDetailReport(Double southWestLong, Double southWestLat, Double northEastLong,
			Double northEastLat, SiteLayerSelection filterConfiguration) throws Exception;

	Map<String, String> generateSectorDetailReport(Double southWestLong, Double southWestLat, Double northEastLong,
			Double northEastLat, SiteLayerSelection filterConfiguration);

	Map<String, String> generateSectorPropertyReport(List<Map<String, SiteLayerSelection>> list, String name, String type);

	Map<String, String> generateSitePropertyReport(List<Map<String, SiteLayerSelection>> list, String name,String type);

}
