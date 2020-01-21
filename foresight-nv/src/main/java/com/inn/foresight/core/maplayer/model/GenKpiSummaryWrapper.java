package com.inn.foresight.core.maplayer.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.inn.core.generic.wrapper.RestWrapper;
@RestWrapper
public class GenKpiSummaryWrapper {

    /** the identifier */
    private List<List<List<Double>>> polyList;

    /** the geographyList */
    private String identifier;

    /** the reportMap */
    private Map<String, Double> reportMap;

    /** the geographyList */
    private List<String> geographyList;

    /** the geographyList */
    private List<String> columnList;

    /** The dimension. */
    private String columnFamily;

    /** The dimension. */
    private String dimension;

    /** The toDate name. */
    private String toDate;

    /** The fromDate name. */
    private String fromDate;

    /** The kpi name. */
    private String kpi;

    /** The band name. */
    private String band;

    /** The siteStatus name. */
    private String siteStatus;

    /** The table name. */
    private String tableName;

    /** The column. */
    private String column;

    /** The zoom. */
    private Integer zoom;

    /** The ne lat. */
    private Double neLat;

    /** The ne lng. */
    private Double neLng;

    /** The sw lat. */
    private Double swLat;

    /** The sw lng. */
    private Double swLng;

    /** The legend id. */
    private Integer legendId;

    /** The min. */
    private Double min;

    /** The max. */
    private Double max;

    /** The avg. */
    private Double avg;

    /** The record count. */
    private Long recordCount = 0L;

    /** The colour. */
    private String colour;

    /** the geographyType */
    private String geographyType;

    private List<Double> validValueList = new ArrayList<>();

    /** The ranges. */
    LinkedList<GenericKpiRange> ranges = new LinkedList<>();
    /** The ranges. */
    List<GenericKpiRange> rangesList = new ArrayList<>();

    private ReportWrapper report;

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

    /**
     * @return
     */
    public String getKpi() {
        return kpi;
    }

    /**
     * @param kpi
     */
    public void setKpi(String kpi) {
        this.kpi = kpi;
    }

    /**
     * @return
     */
    public String getBand() {
        return band;
    }

    /**
     * @param band
     */
    public void setBand(String band) {
        this.band = band;
    }

    /**
     * @return
     */
    public String getSiteStatus() {
        return siteStatus;
    }

    /**
     * @return
     */
    public void setSiteStatus(String siteStatus) {
        this.siteStatus = siteStatus;
    }

    /**
     * Gets the table name.
     *
     * @return the table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets the table name.
     *
     * @param tableName the new table name
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public ReportWrapper getReport() {
        return report;
    }

    public void setReport(ReportWrapper report) {
        this.report = report;
    }

    /**
     * Gets the column.
     *
     * @return the column
     */
    public String getColumn() {
        return column;
    }

    /**
     * Sets the column.
     *
     * @param column the new column
     */
    public void setColumn(String column) {
        this.column = column;
    }

    /**
     * Gets the zoom.
     *
     * @return the zoom
     */
    public Integer getZoom() {
        return zoom;
    }

    /**
     * Sets the zoom.
     *
     * @param zoom the new zoom
     */
    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    /**
     * Gets the nw lat.
     *
     * @return the nw lat
     */

    /**
     * Gets the legend id.
     *
     * @return the legend id
     */
    public Integer getLegendId() {
        return legendId;
    }

    public Double getNeLat() {
        return neLat;
    }

    public void setNeLat(Double neLat) {
        this.neLat = neLat;
    }

    public Double getNeLng() {
        return neLng;
    }

    public void setNeLng(Double neLng) {
        this.neLng = neLng;
    }

    public Double getSwLat() {
        return swLat;
    }

    public void setSwLat(Double swLat) {
        this.swLat = swLat;
    }

    public Double getSwLng() {
        return swLng;
    }

    public void setSwLng(Double swLng) {
        this.swLng = swLng;
    }

    /**
     * Sets the legend id.
     *
     * @param legendId the new legend id
     */
    public void setLegendId(Integer legendId) {
        this.legendId = legendId;
    }

    /**
     * Gets the min.
     *
     * @return the min
     */
    public Double getMin() {
        return min;
    }

    /**
     * Sets the min.
     *
     * @param min the new min
     */
    public void setMin(Double min) {
        this.min = min;
    }

    /**
     * Gets the max.
     *
     * @return the max
     */
    public Double getMax() {
        return max;
    }

    /**
     * Sets the max.
     *
     * @param max the new max
     */
    public void setMax(Double max) {
        this.max = max;
    }

    /**
     * Gets the avg.
     *
     * @return the avg
     */
    public Double getAvg() {
        return avg;
    }

    /**
     * Sets the avg.
     *
     * @param avg the new avg
     */
    public void setAvg(Double avg) {
        this.avg = avg;
    }

    /**
     * Gets the ranges.
     *
     * @return the ranges
     */
    public LinkedList<GenericKpiRange> getRanges() {
        return ranges;
    }

    /**
     * Sets the ranges.
     *
     * @param ranges the new ranges
     */
    public void setRanges(LinkedList<GenericKpiRange> ranges) {
        this.ranges = ranges;
    }

    /**
     * Adds the ranges.
     *
     * @param ranges the ranges
     */
    public void addRanges(GenericKpiRange ranges) {
        this.ranges.add(ranges);
    }

    /**
     * Gets the record count.
     *
     * @return the record count
     */
    public long getRecordCount() {
        return recordCount;
    }

    /**
     * Sets the record count.
     *
     * @param recordCount the new record count
     */
    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    /**
     * Return Average value.
     */
    public void calculateAvg() {
        if (avg != null && recordCount != 0 && avg != 0) {
            avg = avg / recordCount;
        }
    }

    /**
     * Gets the colour.
     *
     * @return the colour
     */
    public String getColour() {
        return colour;
    }

    /**
     * Sets the colour.
     *
     * @param colour the new colour
     */

    public void setColour(String colour) {
        this.colour = colour;
    }

    /**
     * Get the Geography Type
     *
     * @return geographyType
     */
    public String getGeographyType() {
        return geographyType;
    }

    /**
     * set the geography Type
     *
     * @return
     */
    public void setGeographyType(String geographyType) {
        this.geographyType = geographyType;
    }

    /**
     * Update avg value.
     *
     * @param value the value
     */
    public void updateAvgValue(Double value) {
        if (avg == null) {
            avg = value;
        } else {
            avg = avg + value;
        }

    }

    /**
     * Update count.
     *
     * @param count the count
     */
    public void updateCount(Long count) {
        if (recordCount == null) {
            recordCount = count;
        } else {
            recordCount = recordCount + count;
        }

    }

    public List<Double> getValidValueList() {
        return validValueList;
    }

    public void setValidValueList(List<Double> validValueList) {
        this.validValueList = validValueList;
    }

    // Need to Verify this method
    public void updateValidValue(Double rangeMinVal, Double rangeMaxVal, Double value) {
        if (rangeMinVal <= value && rangeMaxVal > value) {
            validValueList.add(value);
        }
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public List<String> getGeographyList() {
        return geographyList;
    }

    public void setGeographyList(List<String> geographyList) {
        this.geographyList = geographyList;
    }

    @Override
    public String toString() {
        return "GenKpiSummaryWrapper [polyList=" + polyList + ", identifier=" + identifier + ", reportMap=" + reportMap + ", geographyList=" + geographyList + ", columnFamily=" + columnFamily
                + ", dimension=" + dimension + ", toDate=" + toDate + ", fromDate=" + fromDate + ", kpi=" + kpi + ", band=" + band + ", siteStatus=" + siteStatus + ", tableName=" + tableName
                + ", column=" + column + ", zoom=" + zoom + ", neLat=" + neLat + ", neLng=" + neLng + ", swLat=" + swLat + ", swLng=" + swLng + ", legendId=" + legendId + ", min=" + min + ", max="
                + max + ", avg=" + avg + ", recordCount=" + recordCount + ", colour=" + colour + ", geographyType=" + geographyType + ", validValueList=" + validValueList + ", ranges=" + ranges
                + ", report=" + report + "]";
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getColumnFamily() {
        return columnFamily;
    }

    public void setColumnFamily(String columnFamily) {
        this.columnFamily = columnFamily;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Map<String, Double> getReportMap() {
        return reportMap;
    }

    public void setReportMap(Map<String, Double> reportMap) {
        this.reportMap = reportMap;
    }

    public List<GenericKpiRange> getRangesList() {
        return rangesList;
    }

    public void setRangesList(List<GenericKpiRange> rangesList) {
        this.rangesList = rangesList;
    }

}
