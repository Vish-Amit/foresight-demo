package com.inn.foresight.core.infra.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.Vendor;
@JpaWrapper
public class NetworkElementOnAirJsiWrapper {

    private String neid;
    private String pneid;
    private String neName;
    private String pneName;
    private Double lattitude;
    private Double longitude;
    private String NeFrequency;
    private Domain domain;
    private Vendor vendor;
    private String geographyL3Name;
    private Integer id;
    private NEStatus neStatus;
    
    public NetworkElementOnAirJsiWrapper(String neid, String pneid, String neName, String pneName, Double lattitude,
            Double longitude, String neFrequency,Domain domain,Vendor vendor,String geographyL3Name, Integer id, NEStatus neStatus) {
        super();
        this.neid = neid;
        this.pneid = pneid;
        this.neName = neName;
        this.pneName = pneName;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.NeFrequency = neFrequency;
        this.domain = domain;
        this.vendor = vendor;
        this.geographyL3Name = geographyL3Name;
        this.neStatus = neStatus;
        this.id = id;
    }

    
    public String getNeid() {
        return neid;
    }

    
    public void setNeid(String neid) {
        this.neid = neid;
    }

    
    public String getPneid() {
        return pneid;
    }

    
    public void setPneid(String pneid) {
        this.pneid = pneid;
    }

    
    public String getNeName() {
        return neName;
    }

    
    public void setNeName(String neName) {
        this.neName = neName;
    }

    
    public String getPneName() {
        return pneName;
    }

    
    public void setPneName(String pneName) {
        this.pneName = pneName;
    }

    
    public Double getLattitude() {
        return lattitude;
    }

    
    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    
    public Double getLongitude() {
        return longitude;
    }

    
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    
    public String getNeFrequency() {
        return NeFrequency;
    }

    
    public void setNeFrequency(String neFrequency) {
        NeFrequency = neFrequency;
    }

    
    public Domain getDomain() {
        return domain;
    }


    
    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    
    public Vendor getVendor() {
        return vendor;
    }


    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    
    public String getGeographyL3Name() {
        return geographyL3Name;
    }

    public void setGeographyL3Name(String geographyL3Name) {
        this.geographyL3Name = geographyL3Name;
    }

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public NEStatus getNeStatus() {
        return neStatus;
    }
    
    public void setNeStatus(NEStatus neStatus) {
        this.neStatus = neStatus;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NetworkElementOnAirJsiWrapper [neid=");
        builder.append(neid);
        builder.append(", pneid=");
        builder.append(pneid);
        builder.append(", neName=");
        builder.append(neName);
        builder.append(", pneName=");
        builder.append(pneName);
        builder.append(", lattitude=");
        builder.append(lattitude);
        builder.append(", longitude=");
        builder.append(longitude);
        builder.append(", NeFrequency=");
        builder.append(NeFrequency);
        builder.append(", domain=");
        builder.append(domain);
        builder.append(", vendor=");
        builder.append(vendor);
        builder.append(", geographyL3Name=");
        builder.append(geographyL3Name);
        builder.append(", id=");
        builder.append(id);
        builder.append(", neStatus=");
        builder.append(neStatus);
        builder.append("]");
        return builder.toString();
    }

}
