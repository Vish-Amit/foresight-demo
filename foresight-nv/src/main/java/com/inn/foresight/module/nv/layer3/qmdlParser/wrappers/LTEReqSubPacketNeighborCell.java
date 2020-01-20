package com.inn.foresight.module.nv.layer3.qmdlParser.wrappers;


/** LTE Connected NeburMeasReqSubpacket. */

public class LTEReqSubPacketNeighborCell {

    /** The cell id. */
    private Integer cellId;

    /** The cp type. */
    private String cpType;

    /** The enb tx ant. */
    private Integer enbTxAnt;

    /** The ttl enable. */
    private Integer ttlEnable;

    /** The ftl enable. */
    private Integer ftlEnable;

    /** The ftl cumu freq offset. */
    private Long ftlCumuFreqOffset;

    /** The frame bdry ref time 0. */
    private Long frameBdryRefTime0;

    /** The frame bdry ref time 1. */
    private Long frameBdryRefTime1;

    /** The total time adj cir 1. */
    private Integer totalTimeAdjCir1;

    /** The total time adj cir 0. */
    private Integer totalTimeAdjCir0;

    /**
     * Gets the cell id.
     *
     * @return the cell id
     */
    public Integer getCellId() {
        return cellId;
    }

    /**
     * Sets the cell id.
     *
     * @param cellId the new cell id
     */
    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    /**
     * Gets the cp type.
     *
     * @return the cp type
     */
    public String getCpType() {
        return cpType;
    }

    /**
     * Sets the cp type.
     *
     * @param cpType the new cp type
     */
    public void setCpType(String cpType) {
        this.cpType = cpType;
    }

    /**
     * Gets the enb tx ant.
     *
     * @return the enb tx ant
     */
    public Integer getEnbTxAnt() {
        return enbTxAnt;
    }

    /**
     * Sets the enb tx ant.
     *
     * @param enbTxAnt the new enb tx ant
     */
    public void setEnbTxAnt(Integer enbTxAnt) {
        this.enbTxAnt = enbTxAnt;
    }

    /**
     * Gets the ttl enable.
     *
     * @return the ttl enable
     */
    public Integer getTtlEnable() {
        return ttlEnable;
    }

    /**
     * Sets the ttl enable.
     *
     * @param ttlEnable the new ttl enable
     */
    public void setTtlEnable(Integer ttlEnable) {
        this.ttlEnable = ttlEnable;
    }

    /**
     * Gets the ftl enable.
     *
     * @return the ftl enable
     */
    public Integer getFtlEnable() {
        return ftlEnable;
    }

    /**
     * Sets the ftl enable.
     *
     * @param ftlEnable the new ftl enable
     */
    public void setFtlEnable(Integer ftlEnable) {
        this.ftlEnable = ftlEnable;
    }

    /**
     * Gets the ftl cumu freq offset.
     *
     * @return the ftl cumu freq offset
     */
    public Long getFtlCumuFreqOffset() {
        return ftlCumuFreqOffset;
    }

    /**
     * Sets the ftl cumu freq offset.
     *
     * @param ftlCumuFreqOffset the new ftl cumu freq offset
     */
    public void setFtlCumuFreqOffset(Long ftlCumuFreqOffset) {
        this.ftlCumuFreqOffset = ftlCumuFreqOffset;
    }

    /**
     * Gets the frame bdry ref time 0.
     *
     * @return the frame bdry ref time 0
     */
    public Long getFrameBdryRefTime0() {
        return frameBdryRefTime0;
    }

    /**
     * Sets the frame bdry ref time 0.
     *
     * @param frameBdryRefTime0 the new frame bdry ref time 0
     */
    public void setFrameBdryRefTime0(Long frameBdryRefTime0) {
        this.frameBdryRefTime0 = frameBdryRefTime0;
    }

    /**
     * Gets the frame bdry ref time 1.
     *
     * @return the frame bdry ref time 1
     */
    public Long getFrameBdryRefTime1() {
        return frameBdryRefTime1;
    }

    /**
     * Sets the frame bdry ref time 1.
     *
     * @param frameBdryRefTime1 the new frame bdry ref time 1
     */
    public void setFrameBdryRefTime1(Long frameBdryRefTime1) {
        this.frameBdryRefTime1 = frameBdryRefTime1;
    }

    /**
     * Gets the total time adj cir 1.
     *
     * @return the total time adj cir 1
     */
    public Integer getTotalTimeAdjCir1() {
        return totalTimeAdjCir1;
    }

    /**
     * Sets the total time adj cir 1.
     *
     * @param totalTimeAdjCir1 the new total time adj cir 1
     */
    public void setTotalTimeAdjCir1(Integer totalTimeAdjCir1) {
        this.totalTimeAdjCir1 = totalTimeAdjCir1;
    }

    /**
     * Gets the total time adj cir 0.
     *
     * @return the total time adj cir 0
     */
    public Integer getTotalTimeAdjCir0() {
        return totalTimeAdjCir0;
    }

    /**
     * Sets the total time adj cir 0.
     *
     * @param totalTimeAdjCir0 the new total time adj cir 0
     */
    public void setTotalTimeAdjCir0(Integer totalTimeAdjCir0) {
        this.totalTimeAdjCir0 = totalTimeAdjCir0;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "cellId=" + cellId +
                ", cpType=" + cpType +
                ", enbTxAnt=" + enbTxAnt +
                ", ttlEnable=" + ttlEnable +
                ", ftlEnable=" + ftlEnable +
                ", ftlCumuFreqOffset=" + ftlCumuFreqOffset +
                ", frameBdryRefTime0=" + frameBdryRefTime0 +
                ", frameBdryRefTime1=" + frameBdryRefTime1 +
                ", totalTimeAdjCir1=" + totalTimeAdjCir1 +
                ", totalTimeAdjCir0=" + totalTimeAdjCir0;
    }
}
