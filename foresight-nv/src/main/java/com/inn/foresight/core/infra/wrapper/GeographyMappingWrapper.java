package com.inn.foresight.core.infra.wrapper;

public class GeographyMappingWrapper {

    private Integer geographyL1Id;
    private Integer geographyL2Id;
    private Integer geographyL3Id;

    public GeographyMappingWrapper(Integer geographyL1Id, Integer geographyL2Id, Integer geographyL3Id) {
        super();
        this.geographyL1Id = geographyL1Id;
        this.geographyL2Id = geographyL2Id;
        this.geographyL3Id = geographyL3Id;
    }

    public Integer getGeographyL1Id() {
        return geographyL1Id;
    }

    public void setGeographyL1Id(Integer geographyL1Id) {
        this.geographyL1Id = geographyL1Id;
    }

    public Integer getGeographyL2Id() {
        return geographyL2Id;
    }

    public void setGeographyL2Id(Integer geographyL2Id) {
        this.geographyL2Id = geographyL2Id;
    }

    public Integer getGeographyL3Id() {
        return geographyL3Id;
    }

    public void setGeographyL3Id(Integer geographyL3Id) {
        this.geographyL3Id = geographyL3Id;
    }

}