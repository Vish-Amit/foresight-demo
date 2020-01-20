package com.inn.foresight.module.nv.layer.wrapper;

import java.util.List;

public class BoundaryDataWrapper {


    List<String> columnList ;
    String tableName ="Geography";
    Double nELat;
    Double nELng;
    Double sWLat;
    Double sWLng;
    Boolean isExact=false;
    String subPath;
    String area;



    Integer zoomLevel;

    public Integer getZoomLevel() {
        return zoomLevel;
    }

    public void setZoomLevel(Integer zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public List<String> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<String> columnList) {
        this.columnList = columnList;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Double getnELat() {
        return nELat;
    }

    public void setnELat(Double nELat) {
        this.nELat = nELat;
    }

    public Double getnELng() {
        return nELng;
    }

    public void setnELng(Double nELng) {
        this.nELng = nELng;
    }

    public Double getsWLat() {
        return sWLat;
    }

    public void setsWLat(Double sWLat) {
        this.sWLat = sWLat;
    }

    public Double getsWLng() {
        return sWLng;
    }

    public void setsWLng(Double sWLng) {
        this.sWLng = sWLng;
    }

    public Boolean getExact() {
        return isExact;
    }

    public void setExact(Boolean exact) {
        isExact = exact;
    }

    public String getSubPath() {
        return subPath;
    }

    public void setSubPath(String subPath) {
        this.subPath = subPath;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


    @Override
    public String toString() {
        return "BoundaryDataWrapper{" +
                "columnList=" + columnList +
                ", tableName='" + tableName + '\'' +
                ", nELat=" + nELat +
                ", nELng=" + nELng +
                ", sWLat=" + sWLat +
                ", sWLng=" + sWLng +
                ", isExact=" + isExact +
                ", subPath='" + subPath + '\'' +
                ", area='" + area + '\'' +
                ", zoomLevel=" + zoomLevel +
                '}';
    }
}
