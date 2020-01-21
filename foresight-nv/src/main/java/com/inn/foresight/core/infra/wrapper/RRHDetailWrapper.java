package com.inn.foresight.core.infra.wrapper;


public class RRHDetailWrapper {

    public RRHDetailWrapper() {

    }
    private Integer sectorId;
    private String rrhModelNo;
    private String rrhSerialNo; 
    private Integer cellId;
    private Integer azimuth;
    private Integer etilt;
    private Integer mtilt;
    private Double buildingHeight;
    private Double antennaHeight;

     
    public Integer getCellId() {
		return cellId;
	}

	public void setCellId(Integer cellId) {
		this.cellId = cellId;
	}

	public String getRrhModelNo() {
        return rrhModelNo;
    }

    public void setRrhModelNo(String rrhModelNo) {
        this.rrhModelNo = rrhModelNo;
    }

    public String getRrhSerialNo() {
        return rrhSerialNo;
    }

    public void setRrhSerialNo(String rrhSerialNo) {
        this.rrhSerialNo = rrhSerialNo;
    }

    public void setSectorId(Integer sectorId) {
        this.sectorId = sectorId;
    }

    public Integer getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(Integer azimuth) {
        this.azimuth = azimuth;
    }

    public Integer getEtilt() {
        return etilt;
    }

    public void setEtilt(Integer etilt) {
        this.etilt = etilt;
    }

    public Integer getMtilt() {
        return mtilt;
    }

    public void setMtilt(Integer mtilt) {
        this.mtilt = mtilt;
    }

    public Double getBuildingHeight() {
		return buildingHeight;
	}

	public void setBuildingHeight(Double buildingHeight) {
		this.buildingHeight = buildingHeight;
	}

	public Double getAntennaHeight() {
		return antennaHeight;
	}

	public void setAntennaHeight(Double antennaHeight) {
		this.antennaHeight = antennaHeight;
	}

	public int getSectorId() {
        return sectorId;
    }

    public void setSectorId(int sectorId) {
        this.sectorId = sectorId;
    }

    public String getModelNo() {
        return rrhModelNo;
    }

    public void setModelNo(String modelNo) {
        this.rrhModelNo = modelNo;
    }

    @Override
    public String toString() {
        return "RRHDetailWrapper [sectorId=" + sectorId + ", modelNo=" + rrhModelNo + ", azimuth=" + azimuth + ", etilt=" + etilt + ", mtilt=" + mtilt + ", buildingHeight=" + buildingHeight
                + ", antennaHeight=" + antennaHeight + "]";
    }

}
