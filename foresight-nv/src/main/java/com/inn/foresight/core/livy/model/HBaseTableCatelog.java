

package com.inn.foresight.core.livy.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class HBaseTableCatelog.
 * 
 * @author Zafar
 */
@NamedQueries({
	@NamedQuery(name = "getCatalogByName", query = "select c from HBaseTableCatelog c where tablename=:name")
})

@Entity
@Table(name = "HBaseTableCatelog")
@XmlRootElement(name = "HBaseTableCatelog")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })

public class HBaseTableCatelog implements Serializable{
	
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4084875109500857320L;
	
	
	
	
	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "hbasetablecatelogid_pk")
	private Long id;
	
	/** The name. */
	@Basic
	@Column(name = "tablename")
	private String name;
	
	/** The catalog. */
	@Basic
	@Column(name = "catalog")
	private String catalog;

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the catalog.
	 *
	 * @return the catalog
	 */
	public String getCatalog() {
		return catalog;
	}

	/**
	 * Sets the catalog.
	 *
	 * @param catalog the new catalog
	 */
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	/**
	 * Instantiates a new h base table catelog.
	 *
	 * @param id the id
	 * @param name the name
	 * @param catalog the catalog
	 */
	public HBaseTableCatelog(Long id, String name, String catalog) {
		super();
		this.id = id;
		this.name = name;
		this.catalog = catalog;
	}
	
	/**
	 * Instantiates a new h base table catelog.
	 */
	public HBaseTableCatelog() {
		super();
		
	}
	
	
	
}

