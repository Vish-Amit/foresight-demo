package com.inn.foresight.module.nv.workorder.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.product.um.user.model.User;

@NamedQuery(name = "getWOUserByGWOId", query = "SELECT w from WOUserMapping w WHERE w.genericWorkorder.id = :workorderId and w.genericWorkorder.status in ('INPROGRESS','NOT_STARTED')")

@NamedQuery(name = "getWOUseByUserId", query = "SELECT w from WOUserMapping w WHERE w.user.userid = :userId and w.genericWorkorder.status in ('INPROGRESS','NOT_STARTED')")

/**
 * The Class WOUserMapping.
 */
@Entity
@Table(name = "WOUserMapping")
@XmlRootElement(name = "WOUserMapping")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler"})
public class WOUserMapping implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wousermappingid_pk")
	private Integer id;

	/** The generic workorder. */
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "genericworkorderid_fk", nullable = true)
	private GenericWorkorder genericWorkorder;

	/** The user. */
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "userid_fk", nullable = true)
	private User user;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the generic workorder.
	 *
	 * @return the generic workorder
	 */
	public GenericWorkorder getGenericWorkorder() {
		return genericWorkorder;
	}

	/**
	 * Sets the generic workorder.
	 *
	 * @param genericWorkorder the new generic workorder
	 */
	public void setGenericWorkorder(GenericWorkorder genericWorkorder) {
		this.genericWorkorder = genericWorkorder;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the new user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WOUserMapping [id=" + id + ", genericWorkorder=" + genericWorkorder + ", user=" + user + "]";
	}
	
	
}
