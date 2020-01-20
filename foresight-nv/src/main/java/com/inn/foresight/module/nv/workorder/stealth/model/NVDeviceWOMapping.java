package com.inn.foresight.module.nv.workorder.stealth.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.device.model.NVDeviceData;

@Entity
@Table(name = "NVDeviceWOMapping")
@XmlRootElement(name = "NVDeviceWOMapping")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NVDeviceWOMapping implements Serializable{

	
	
	public NVDeviceWOMapping() {
		super();
	}

	public NVDeviceWOMapping(Date startTime, Date endTime, NVDeviceData deviceData, GenericWorkorder genericWorkorder) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.deviceData = deviceData;
		this.genericWorkorder = genericWorkorder;
	}

	private static final long serialVersionUID = 1L;
	
	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "nvdevicewomappingid_pk")
	private Integer id;
	
	@Column(name = "starttime")
	private Date startTime;
	
	@Column(name = "endtime")
	private Date endTime;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nvdeviceid_fk", nullable = false)
	private NVDeviceData deviceData;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "genericworkorderid_fk", nullable = true)
	private GenericWorkorder genericWorkorder;



	
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public NVDeviceData getDeviceData() {
		return deviceData;
	}

	public void setDeviceData(NVDeviceData deviceData) {
		this.deviceData = deviceData;
	}

	public GenericWorkorder getGenericWorkorder() {
		return genericWorkorder;
	}

	public void setGenericWorkorder(GenericWorkorder genericWorkorder) {
		this.genericWorkorder = genericWorkorder;
	}	
	
}
