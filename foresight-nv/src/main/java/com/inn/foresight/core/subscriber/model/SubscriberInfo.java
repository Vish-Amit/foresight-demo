package com.inn.foresight.core.subscriber.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.user.model.User;

@Entity
@Table(name = "SubscriberInfo")
@XmlRootElement(name = "SubscriberInfo")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class SubscriberInfo implements Serializable {

	private static final long serialVersionUID = 3458851425336849375L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subscriberinfoid_pk")
	private Integer id;

	@Basic
	private String msisdn;

	@Basic
	private String imsi;

	@Basic
	private String category;

	@Basic
	private String type;

	@Basic
	@Column(name = "creationtime")
	private Date creationTime;

	@Basic
	@Column(name = "modificationtime")
	private Date modificationTime;

	@Basic
	@Column(name = "enabled")
	private Boolean isEnabled;

	@Basic
	@Column(name = "searchvalue")
	private String searchValue;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creatorid_fk", nullable = false)
	private User creator;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lastmodifierid_fk")
	private User lastModifier;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	@JsonIgnore
	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public User getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(User lastModifier) {
		this.lastModifier = lastModifier;
	}

	@Override
	public String toString() {
		return "SubscriberInfo [id=" + id + ", msisdn=" + msisdn + ", imsi=" + imsi + ", category=" + category + ", type=" + type + ", creationTime=" + creationTime + ", modificationTime="
				+ modificationTime + ", isEnabled=" + isEnabled + ", creator=" + creator + ", lastModifier=" + lastModifier + "]";
	}

}