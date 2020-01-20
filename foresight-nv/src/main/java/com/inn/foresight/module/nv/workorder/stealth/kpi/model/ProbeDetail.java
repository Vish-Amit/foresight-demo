package com.inn.foresight.module.nv.workorder.stealth.kpi.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.workorder.stealth.model.StealthTaskResult;


@NamedQueries({
	@NamedQuery(name=StealthConstants.GET_PROBE_DETAIL_BY_KPI , query="select p from ProbeDetail p where p.taskResult.id=:id and kpi=:kpi")
})
@Entity
@Table(name = "ProbeDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class ProbeDetail {

	
	
	
	
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name="probedetailid_pk")
	private Integer id;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "stealthtaskresultid_fk")
	private StealthTaskResult taskResult;

	@Column(name = "rsrp")
	private Double rsrp;

	@Column(name = "sinr")
	private Double sinr;
	
	@Column(name = "pci")
	private Integer pci;
	
	@Column(name = "cellid")
	private Integer cellId;
	
	@Column(name = "enodebid")
	private Integer enodeBId;

	@Column(name = "cgi")
	private Integer cgi;

	@Column(name = "kpi")
	private String kpi;

	@Column(name = "value")
	private Double value;

	@Column(name = "modificationtime")
	private Date modificationTime;

	@Column(name = "creationtime")
	private Date creationTime;

	@Column(name = "address")
	private String address;

	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;

	@Column(name="notified")
	private Boolean isNotified=false;
	
	@Column(name="counter")
	private Integer counter;
	
	@Column(name="lastnotifiedtime")
	private Date lastNotifiedTime;
	
	
	public Date getLastNotifiedTime() {
		return lastNotifiedTime;
	}

	public void setLastNotifiedTime(Date lastNotifiedTime) {
		this.lastNotifiedTime = lastNotifiedTime;
	}

	public Integer getCounter() {
		return counter;
	}

	public void setCounter(Integer counter) {
		this.counter = counter;
	}

	public StealthTaskResult getTaskResult() {
		return taskResult;
	}

	public void setTaskResult(StealthTaskResult taskResult) {
		this.taskResult = taskResult;
	}

	public Double getRsrp() {
		return rsrp;
	}
	

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public void setRsrp(Double rsrp) {
		this.rsrp = rsrp;
	}

	public Double getSinr() {
		return sinr;
	}

	public void setSinr(Double sinr) {
		this.sinr = sinr;
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

	public Integer getEnodeBId() {
		return enodeBId;
	}

	public void setEnodeBId(Integer enodeBId) {
		this.enodeBId = enodeBId;
	}

	public Integer getCgi() {
		return cgi;
	}

	public void setCgi(Integer cgi) {
		this.cgi = cgi;
	}


	public Date getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getKpi() {
		return kpi;
	}

	public void setKpi(String kpi) {
		this.kpi = kpi;
	}

	
	
	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Boolean getIsNotified() {
		return isNotified;
	}

	public void setIsNotified(Boolean isNotified) {
		this.isNotified = isNotified;
	}

	@Override
	public String toString() {
		return "ProbeDetail [id=" + id + ", taskResult=" + taskResult + ", rsrp=" + rsrp + ", sinr=" + sinr + ", pci="
				+ pci + ", cellId=" + cellId + ", enodeBId=" + enodeBId + ", cgi=" + cgi + ", kpi=" + kpi + ", value="
				+ value + ", modificationTime=" + modificationTime + ", creationTime=" + creationTime + ", address="
				+ address + ", latitude=" + latitude + ", longitude=" + longitude + ", isNotified=" + isNotified
				+ ", counter=" + counter + ", lastNotifiedTime=" + lastNotifiedTime + "]";
	}


}
