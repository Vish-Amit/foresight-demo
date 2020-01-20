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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.device.model.NVDeviceData;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;



@NamedQueries({
        @NamedQuery(name ="getStealthTasksByWorkorderId",query="select s from StealthTaskDetail s join fetch s.nvDeviceData  nvd join fetch nvd.deviceInfo where s.genericWorkorder.id=:id"),
        
		@NamedQuery(name = StealthConstants.GET_STEALTH_TASK_BY_DEVICE_AND_WO_ID, query = "select task from StealthTaskDetail task where task.genericWorkorder.id = :workorderId and task.nvDeviceData.id = :deviceId order by creationtime desc"),

		@NamedQuery(name = StealthConstants.GET_STEALTH_TASK_DETAIL_BY_DEVICE_ID, query = "Select s from StealthTaskDetail s where s.nvDeviceData.id=:deviceId and s.acknowledgement=:acknowledgement and date(s.creationTime)=date(:creationTime) order by s.creationTime desc"),

		@NamedQuery(name="getNVDeviceListByWOId",query ="select distinct s.nvDeviceData.id from  StealthTaskDetail s where s.genericWorkorder.id=:woId "),
		
		@NamedQuery(name="getWOListByDeviceId", query="select distinct s.genericWorkorder from  StealthTaskDetail s,WOUserMapping wo where s.nvDeviceData.deviceInfo.deviceId=:deviceId and s.genericWorkorder.status in ('INPROGRESS','NOT_STARTED') and (:modificationTime is null or s.genericWorkorder.modificationTime>=:modificationTime) and s.genericWorkorder.id=wo.genericWorkorder.id and (:userId is null or wo.user.userid=:userId)")

})


@Entity
@Table(name = "StealthTaskDetail")
@XmlRootElement(name = "StealthTaskDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class StealthTaskDetail implements Serializable {
	private static final long serialVersionUID = 1938996333612386108L;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "stealthtaskdetailid_pk")
	private Integer id;

	@Column(name = "starttime")
	private Date startTime;

	@Column(name = "endtime")
	private Date endTime;

	@Column(name = "creationtime")
	private Date creationTime;

	/** The modification time. */
	@Column(name = "modificationtime")
	private Date modificationTime;

	@Column(name = "acknowledgement")
	private String acknowledgement;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "genericworkorderid_fk", nullable = true)
	private GenericWorkorder genericWorkorder;

	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "nvdeviceid_fk", nullable = true)
	private NVDeviceData nvDeviceData;
	
	@Column(name = "postlogin")
	private Boolean postLogin=Boolean.FALSE;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public String getAcknowledgement() {
		return acknowledgement;
	}

	public void setAcknowledgement(String acknowledgement) {
		this.acknowledgement = acknowledgement;
	}

	public GenericWorkorder getGenericWorkorder() {
		return genericWorkorder;
	}

	public void setGenericWorkorder(GenericWorkorder genericWorkorder) {
		this.genericWorkorder = genericWorkorder;
	}

	public NVDeviceData getNvDeviceData() {
		return nvDeviceData;
	}

	public void setNvDeviceData(NVDeviceData nvDeviceData) {
		this.nvDeviceData = nvDeviceData;
	}
	
	public Boolean getPostLogin() {
		return postLogin;
	}

	public void setPostLogin(Boolean postLogin) {
		this.postLogin = postLogin;
	}

	@Override
	public String toString() {
		return "StealthTaskDetail [id=" + id + ", startTime=" + startTime + ", endTime=" + endTime + ", creationTime="
				+ creationTime + ", modificationTime=" + modificationTime + ", acknowledgement=" + acknowledgement
				+ ", genericWorkorder=" + genericWorkorder + ", nvDeviceData=" + nvDeviceData + ", postLogin="
				+ postLogin + "]";
	}
}
