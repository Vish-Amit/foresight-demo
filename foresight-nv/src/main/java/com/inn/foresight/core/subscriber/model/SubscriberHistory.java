package com.inn.foresight.core.subscriber.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "SubscriberHistory")
@XmlRootElement(name = "SubscriberHistory")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class SubscriberHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "subscriberhistoryid_pk")
	private Integer id;

	@Column(name = "subscriberNo")
	private String subscriberNo;

	@Column(name = "searchBy")
	private String searchBy;

	@Column(name = "searchDateTime")
	private Date searchDateTime;

	@Column(name = "searchField")
	private String searchField;

	@Column(name = "searchValue")
	private String subscriberSearch;

	@Column(name = "accesstype")
	private String accessType;

	@Column(name = "executiverole")
	private String executiveRole;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSubscriberNo() {
		return subscriberNo;
	}

	public void setSubscriberNo(String subscriberNo) {
		this.subscriberNo = subscriberNo;
	}

	public String getSearchBy() {
		return searchBy;
	}

	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}

	public Date getSearchDateTime() {
		return searchDateTime;
	}

	public void setSearchDateTime(Date searchDateTime) {
		this.searchDateTime = searchDateTime;
	}

	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

	public String getSubscriberSearch() {
		return subscriberSearch;
	}

	public void setSubscriberSearch() {
		this.subscriberSearch = this.subscriberNo + this.searchBy;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getExecutiveRole() {
		return executiveRole;
	}

	public void setExecutiveRole(String executiveRole) {
		this.executiveRole = executiveRole;
	}

	@Override
	public String toString() {
		return "SubscriberHistory [id=" + id + ", subscriberNo=" + subscriberNo + ", searchBy=" + searchBy + ", searchDateTime=" + searchDateTime + ", searchField=" + searchField
				+ ", subscriberSearch=" + subscriberSearch + ", accessType=" + accessType + ", executiveRole=" + executiveRole + "]";
	}

}
