package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.user.model.User;


@XmlRootElement(name = "MNEAudit")
@Entity
@Table(name = "MNEAudit")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class MNEAudit implements Serializable {
	
	private static final long serialVersionUID = 4948446171918293485L;

	@Id
	@Column(name = "mneauditid_pk")
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "creationtime")
	private Date creationTime;

	@Column(name = "modificationtime")
	private Date modificationTime;
	
	@JoinColumn(name = "networkelementid_fk", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private NetworkElement networkElement;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creatorid_fk", nullable = true)
	private User createdBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lastmodifierid_fk", nullable = true)
	private User modifiedBy;
	
	@Lob
	@Column(name ="history")
	private String history;
	
	@Column(name="remark")
	private String remark;
	

	
	public Integer getId() {
		return id;
	}

	
	public void setId(Integer id) {
		this.id = id;
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

	
	public NetworkElement getNetworkElement() {
		return networkElement;
	}

	
	public void setNetworkElement(NetworkElement networkElement) {
		this.networkElement = networkElement;
	}

	
	public User getCreatedBy() {
		return createdBy;
	}

	
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	
	public User getModifiedBy() {
		return modifiedBy;
	}

	
	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}


	public String getRemark() {
		return remark;
	}

	@Override
	public String toString() {
		return "MNEAudit [id=" + id + ", creationTime=" + creationTime + ", modificationTime=" + modificationTime
				+ ", networkElement=" + networkElement + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
				+ ", history=" + history + ", remark=" + remark + "]";
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


}
