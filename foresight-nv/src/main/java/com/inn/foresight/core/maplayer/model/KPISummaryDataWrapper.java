package com.inn.foresight.core.maplayer.model;

import java.util.List;

import com.inn.core.generic.wrapper.RestWrapper;

@RestWrapper
public class KPISummaryDataWrapper {

    private List<List<List<Double>>> polyList;
    private List<String> geographyList;
    private List<String> columnList;
    private KpiWrapper kpiWrapper;

    public List<String> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<String> columnList) {
        this.columnList = columnList;
    }

    public List<List<List<Double>>> getPolyList() {
        return polyList;
    }

    public void setPolyList(List<List<List<Double>>> polyList) {
        this.polyList = polyList;
    }

    public List<String> getGeographyList() {
        return geographyList;
    }

    public void setGeographyList(List<String> geographyList) {
        this.geographyList = geographyList;
    }

    public KpiWrapper getKpiWrapper() {
        return kpiWrapper;
    }

    public void setKpiWrapper(KpiWrapper kpiWrapper) {
        this.kpiWrapper = kpiWrapper;
    }

    @Override
    public String toString() {
        return "KPISummaryDataWrapper [polyList=" + polyList + ", geographyList=" + geographyList + ", kpiWrapper=" + kpiWrapper + "]";
    }
}
