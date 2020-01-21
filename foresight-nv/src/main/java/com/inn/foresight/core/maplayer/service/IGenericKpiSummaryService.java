package com.inn.foresight.core.maplayer.service;

import java.util.List;

import com.inn.foresight.core.maplayer.model.GenKpiSummaryWrapper;
import com.inn.foresight.core.maplayer.model.KpiWrapper;

public interface IGenericKpiSummaryService {

    String getKpiSummaryData(GenKpiSummaryWrapper kpiWrapper);

    String getPredictionKpiSummaryData(GenKpiSummaryWrapper kpiWrapper);

    String getKpiSummaryDataByGeographyList(GenKpiSummaryWrapper kpiSummaryWrapper);

    String getKpiSummaryDataByPolyList(GenKpiSummaryWrapper kpiWrapper);

    String getPredictionKpiSummaryDataByPolyList(GenKpiSummaryWrapper kpiWrapper);

    String getDriveKpiSummaryData(GenKpiSummaryWrapper kpiWrapper);

    String getRowKeyDate(String date);

    String getKpiSummaryData(List<String> columnList, List<List<List<Double>>> polyList, KpiWrapper kpiSummaryWrapper, Double northEastLat, Double northEastLong, Double southWestLat,
            Double southWestLong, Integer zoomLevel, String tableName, String columnName, String geographyType, String columnFamily, List<String> geographyList, String identifier);
}
