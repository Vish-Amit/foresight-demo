package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.core.infra.utils.enums.NEStatus;

@NamedQueries({
        @NamedQuery(name = "getSectorPropertyData", query = "select r from RANDetail r where r.networkElement.isDeleted=0 and upper(r.networkElement.networkElement.neName)=:neName and r.networkElement.neFrequency=:neFrequency and upper(r.networkElement.neStatus)=:neStatus and upper(r.networkElement.networkElement.neType)=:neType  "),
        @NamedQuery(name = "searchByRrhSerialNo", query = "select r from RANDetail r where r.networkElement.neId=:rrhSerialNo"),
        @NamedQuery(name = "getRANDetailBynename", query = "select r from RANDetail r where r.networkElement.neName=:nename"),
        @NamedQuery(name = "getRANDetailByMacaddress", query = "select new com.inn.foresight.core.infra.wrapper.WifiWrapper(r.networkElement.neId,r.networkElement.neName,r.networkElement.neStatus,r.networkElement.latitude,r.networkElement.longitude,r.channel,r.networkElement.ipv4) from RANDetail r where r.networkElement.macaddress=:macaddress")
})

@Entity
@Table(name = "RANDetail")
@XmlRootElement(name = "RANDetail")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Audited
public class RANDetail implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1206541838752815595L;

    /** The id. */
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "randetailid_pk")
    private Integer id;

    @Basic
    @Column(name = "earfcn")
    private Integer earfcn;

    /** The antenna type. */
    @Basic
    @Column(name = "antennatype")
    private String antennaType;

    @Basic
    @Column(name = "antennamodel")
    private String antennaModel;

    /** The mech tilt. */
    @Basic
    @Column(name = "mechtilt")
    private Integer mechTilt;

    /** The elec tilt. */
    @Basic
    @Column(name = "electilt")
    private Integer elecTilt;

    /** The azimuth. */
    @Basic
    @Column(name = "azimuth")
    private Integer azimuth;

    /** The antenna height. */
    @Basic
    @Column(name = "antennaheight")
    private Double antennaHeight;

    /** The pci. */
    @Basic
    @Column(name = "pci")
    private Integer pci;

    /** The sector. */
    @Basic
    @Column(name = "sector")
    private Integer sector;

    /** The operational status. */
    @Basic
    @Column(name = "operationalstatus")
    private String operationalStatus;

    /** The admin state. */
    @Basic
    @Column(name = "adminState")
    private String adminState;

    /** The network element. */
    @JoinColumn(name = "networkelementid_fk", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private NetworkElement networkElement;

    @JoinColumn(name = "nebanddetailid_fk", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private NEBandDetail neBandDetail;

    @Basic
    @Column(name = "tac")
    private String trackingArea;

    @Basic
    @Column(name = "txpower")
    private String txPower;

    @Basic
    @Column(name = "dlearfcn")
    private Integer dlearfcn;

    @Basic
    @Column(name = "ulearfcn")
    private Integer ulearfcn;

    @Basic
    @Column(name = "antennagain")
    private String antennaGain;

    @Basic
    @Column(name = "antennavendor")
    private String antennaVendor;

    @Basic
    @Column(name = "horizontalbeamwidth")
    private Double horizontalBeamWidth;

    @Basic
    @Column(name = "verticalbeamwidth")
    private Double VerticalBeamWidth;

    /** The creation time. */
    @Basic
    @Column(name = "creationtime")
    private Date creationTime;

    /** The modification time. */
    @Basic
    @Column(name = "modificationtime")
    private Date modificationTime;

    /** The cgi. */
    @Basic
    @Column(name = "cgi")
    private Integer cgi;

    @Basic
    @Column(name = "minelectilt")
    private Integer minElectilt;

    @Basic
    @Column(name = "maxelectilt")
    private Integer maxElectilt;

    @Basic
    @Column(name = "cellonairdate")
    private Date cellOnairDate;

    @Basic
    @Column(name = "bandwidth")
    private String bandwidth;
    
    /**  The retstatus **/
    @Basic
    @Column(name="retstatus")
    private Boolean retstatus;

    @Basic
	@Column(name = "foresightonairdate")
	private Date foresightOnAirDate;

    @Basic
    @Column(name = "channel")
    private Integer channel;
    
    @Basic
    @Column(name = "rsi")
    private Integer rsi;
    
	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public Boolean getRetstatus() {
		return retstatus;
	}

	public void setRetstatus(Boolean retstatus) {
		this.retstatus = retstatus;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEarfcn() {
        return earfcn;
    }

    public void setEarfcn(Integer earfcn) {
        this.earfcn = earfcn;
    }

    public String getAntennaType() {
        return antennaType;
    }

    public void setAntennaType(String antennaType) {
        this.antennaType = antennaType;
    }

    public String getAntennaModel() {
        return antennaModel;
    }

    public void setAntennaModel(String antennaModel) {
        this.antennaModel = antennaModel;
    }

    public Integer getMechTilt() {
        return mechTilt;
    }

    public void setMechTilt(Integer mechTilt) {
        this.mechTilt = mechTilt;
    }

    public Integer getElecTilt() {
        return elecTilt;
    }

    public void setElecTilt(Integer elecTilt) {
        this.elecTilt = elecTilt;
    }

    public Integer getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(Integer azimuth) {
        this.azimuth = azimuth;
    }

    public Double getAntennaHeight() {
        return antennaHeight;
    }

    public void setAntennaHeight(Double antennaHeight) {
        this.antennaHeight = antennaHeight;
    }

    public Integer getPci() {
        return pci;
    }

    public void setPci(Integer pci) {
        this.pci = pci;
    }

    public Integer getSector() {
        return sector;
    }

    public void setSector(Integer sector) {
        this.sector = sector;
    }

    public String getOperationalStatus() {
        return operationalStatus;
    }

    public void setOperationalStatus(String operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

    public String getAdminState() {
        return adminState;
    }

    public void setAdminState(String adminState) {
        this.adminState = adminState;
    }

    public NetworkElement getNetworkElement() {
        return networkElement;
    }

    public void setNetworkElement(NetworkElement networkElement) {
        this.networkElement = networkElement;
    }

    public NEBandDetail getNeBandDetail() {
        return neBandDetail;
    }

    public void setNeBandDetail(NEBandDetail neBandDetail) {
        this.neBandDetail = neBandDetail;
    }

    public String getTrackingArea() {
        return trackingArea;
    }

    public void setTrackingArea(String trackingArea) {
        this.trackingArea = trackingArea;
    }

    public String getTxPower() {
        return txPower;
    }

    public void setTxPower(String txPower) {
        this.txPower = txPower;
    }

    public Integer getDlearfcn() {
        return dlearfcn;
    }

    public void setDlearfcn(Integer dlearfcn) {
        this.dlearfcn = dlearfcn;
    }

    public Integer getUlearfcn() {
        return ulearfcn;
    }

    public void setUlearfcn(Integer ulearfcn) {
        this.ulearfcn = ulearfcn;
    }

    public String getAntennaGain() {
        return antennaGain;
    }

    public void setAntennaGain(String antennaGain) {
        this.antennaGain = antennaGain;
    }

    public String getAntennaVendor() {
        return antennaVendor;
    }

    public void setAntennaVendor(String antennaVendor) {
        this.antennaVendor = antennaVendor;
    }

    public Double getHorizontalBeamWidth() {
        return horizontalBeamWidth;
    }

    public void setHorizontalBeamWidth(Double horizontalBeamWidth) {
        this.horizontalBeamWidth = horizontalBeamWidth;
    }

    public Double getVerticalBeamWidth() {
        return VerticalBeamWidth;
    }

    public void setVerticalBeamWidth(Double verticalBeamWidth) {
        VerticalBeamWidth = verticalBeamWidth;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(Date modificationTime) {
        this.modificationTime = modificationTime;
    }

    public Integer getCgi() {
        return cgi;
    }

    public void setCgi(Integer cgi) {
        this.cgi = cgi;
    }

    public Integer getMinElectilt() {
        return minElectilt;
    }

    public void setMinElectilt(Integer minElectilt) {
        this.minElectilt = minElectilt;
    }

    public Integer getMaxElectilt() {
        return maxElectilt;
    }

    public void setMaxElectilt(Integer maxElectilt) {
        this.maxElectilt = maxElectilt;
    }

    public Date getCellOnairDate() {
        return cellOnairDate;
    }

    public void setCellOnairDate(Date cellOnairDate) {
        this.cellOnairDate = cellOnairDate;
    }

    public String getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(String bandwidth) {
        this.bandwidth = bandwidth;
    }

	public Date getForesightOnAirDate() {
		return foresightOnAirDate;
	}

	public void setForesightOnAirDate(Date foresightOnAirDate) {
		this.foresightOnAirDate = foresightOnAirDate;
	}

	public Integer getRsi() {
		return rsi;
	}

	public void setRsi(Integer rsi) {
		this.rsi = rsi;
	}

	@Override
	public String toString() {
		return "RANDetail [id=" + id + ", earfcn=" + earfcn + ", antennaType=" + antennaType + ", antennaModel="
				+ antennaModel + ", mechTilt=" + mechTilt + ", elecTilt=" + elecTilt + ", azimuth=" + azimuth
				+ ", antennaHeight=" + antennaHeight + ", pci=" + pci + ", sector=" + sector + ", operationalStatus="
				+ operationalStatus + ", adminState=" + adminState + ", networkElement=" + networkElement
				+ ", neBandDetail=" + neBandDetail + ", trackingArea=" + trackingArea + ", txPower=" + txPower
				+ ", dlearfcn=" + dlearfcn + ", ulearfcn=" + ulearfcn + ", antennaGain=" + antennaGain
				+ ", antennaVendor=" + antennaVendor + ", horizontalBeamWidth=" + horizontalBeamWidth
				+ ", VerticalBeamWidth=" + VerticalBeamWidth + ", creationTime=" + creationTime + ", modificationTime="
				+ modificationTime + ", cgi=" + cgi + ", minElectilt=" + minElectilt + ", maxElectilt=" + maxElectilt
				+ ", cellOnairDate=" + cellOnairDate + ", bandwidth=" + bandwidth + ", retstatus=" + retstatus
				+ ", foresightOnAirDate=" + foresightOnAirDate + ", channel=" + channel + ", rsi=" + rsi + "]";
	}

	
	

}
