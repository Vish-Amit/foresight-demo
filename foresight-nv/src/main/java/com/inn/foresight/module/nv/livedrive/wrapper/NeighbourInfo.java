package com.inn.foresight.module.nv.livedrive.wrapper;

/** Created by innoeye on 4/8/16. */
public class NeighbourInfo {

    private Integer pci;
    private Integer psc;
    private Integer cellId;
    private Integer rsrp;
    private Integer rscp;
    private Integer rxLevel;

    public Integer getPsc() {
        return psc;
    }

    public void setPsc(Integer psc) {
        this.psc = psc;
    }

    public Integer getPci() {
        return pci;
    }

    public void setPci(Integer pci) {
        this.pci = pci;
    }

    public Integer getCellId() {
        return cellId;
    }

    public void setCellId(Integer cellId) {
        this.cellId = cellId;
    }

    public Integer getRsrp() {
        return rsrp;
    }

    public void setRsrp(Integer rsrp) {
        this.rsrp = rsrp;
    }

    public Integer getRscp() {
        return rscp;
    }

    public void setRscp(Integer rscp) {
        this.rscp = rscp;
    }

    public Integer getRxLevel() {
        return rxLevel;
    }

    public void setRxLevel(Integer rxLevel) {
        this.rxLevel = rxLevel;
    }

}
