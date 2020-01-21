package com.inn.foresight.core.gallery.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.core.gallery.utils.OrganizationType;

@Entity
@Table(name = "L3Manager")
@XmlRootElement(name = "L3Manager")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class L3Manager implements Serializable {

	private static final long serialVersionUID = 2541589180405087327L;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "l3managerid_pk")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "contact")
	private String contact;

	@Column(name = "email")
	private String email;

	@Column(name = "organization")
	@Enumerated(EnumType.STRING)
	private OrganizationType organization;

	@Basic
	@Column(name = "deleted")
	private Boolean isDeleted;

	public L3Manager() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public OrganizationType getOrganization() {
		return organization;
	}

	public void setOrganization(OrganizationType organization) {
		this.organization = organization;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String toString() {
		return "L3Manager [id=" + id + ", name=" + name + ", contact=" + contact + ", email=" + email + ", organization=" + organization + ", isDeleted=" + isDeleted + "]";
	}

}