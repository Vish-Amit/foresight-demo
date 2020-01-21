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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.geography.model.SalesL1;
import com.inn.product.um.geography.model.SalesL2;
import com.inn.product.um.geography.model.SalesL3;
import com.inn.product.um.geography.model.SalesL4;

/**
 * The Class AdvanceSearch.
 */
@NamedQueries({
		@NamedQuery(name = "getAdvanceSearchByName", query = "select a from AdvanceSearch a where UPPER(a.name) like concat('%',:name,'%')  order by a.priorityValue,a.name"),
		@NamedQuery(name = "getAdvanceSearchByTypeList", query = "select distinct a from AdvanceSearch a  join a.advanceSearchConfiguration advSearchConfig where UPPER(a.name) like concat('%',:name,'%') and advSearchConfig.type in (:typeList) order by a.priorityValue,a.name "),
		@NamedQuery(name = "getAdvanceSearchByTypeReference", query = "select a from AdvanceSearch a where a.name=:name"),
		@NamedQuery(name = "getDistinctType", query = "select distinct advSearchConfig.type from AdvanceSearch a  join a.advanceSearchConfiguration advSearchConfig "),
		@NamedQuery(name = "getAdvanceSearchByTypeListAndVendorType", query = "select distinct a from AdvanceSearch a  join a.advanceSearchConfiguration advSearchConfig where UPPER(a.name) like concat('%',:name,'%') and advSearchConfig.type in (:typeList) order by a.priorityValue,a.name "),
		@NamedQuery(name = "getAdvanceSearchConfigurationByTypeAndTypeReference", query = "select a from AdvanceSearch a where a.advanceSearchConfiguration.type=:type and a.typereference=:typereference"),


})

@XmlRootElement(name = "AdvanceSearch")
@Entity
@Table(name = "AdvanceSearch")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class AdvanceSearch implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 10826114649039415L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "advancesearchid_pk")
	private Integer id;

	/** The name. */
	@Basic
	@Column(name = "searchfieldname")
	private String name;

	/** The priority value. */
	@Basic
	@Column(name = "priorityvalue")
	private Integer priorityValue;

	/** The creation time. */
	@Basic
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modification time. */
	@Basic
	@Column(name = "modificationtime")
	private Date modificationTime;

	/** The typereference. */
	@Basic
	@Column(name = "typereference")
	private Integer typereference;

	/** The row key prefix. Used for search geography data. */
	@Basic
	@Column(name = "rowkeyprefix")
	private String rowKeyPrefix;

	/** The geography L 1. */
	@Basic
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl1id_fk")
	private GeographyL1 geographyL1;

	/** The geography L 2. */
	@Basic
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl2id_fk")
	private GeographyL2 geographyL2;

	/** The geography L 3. */
	@Basic
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl3id_fk")
	private GeographyL3 geographyL3;

	/** The geography L 4. */
	@Basic
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl4id_fk")
	private GeographyL4 geographyL4;

	/** The sales geography L 1. */
	@Basic
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "salesl1id_fk")
	private SalesL1 salesL1;

	/** The sales geography L 2. */
	@Basic
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "salesl2id_fk")
	private SalesL2 salesL2;

	/** The sales geography L 3. */
	@Basic
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "salesl3id_fk")
	private SalesL3 salesL3;

	/** The sales geography L 4. */
	@Basic
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "salesl4id_fk")
	private SalesL4 salesL4;
	
	@Basic
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "advancesearchconfigurationid_fk")
	private AdvanceSearchConfiguration advanceSearchConfiguration;

	@Basic
	@Column(name = "vendor")
	private String vendor;
	

	@Basic
	@Column(name = "domain")
	private String domain;
	
	@Basic
	@Column(name="displayName")
	private String displayName;

	/**
	 * Gets the typereference.
	 *
	 * @return the typereference
	 */
	public Integer getTypereference() {
		return typereference;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Sets the typereference.
	 *
	 * @param typereference the new typereference
	 */
	public void setTypereference(Integer typereference) {
		this.typereference = typereference;
	}

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
	 * Gets the priority value.
	 *
	 * @return the priority value
	 */
	@JsonIgnore
	public Integer getPriorityValue() {
		return priorityValue;
	}

	/**
	 * Sets the priority value.
	 *
	 * @param priorityValue the new priority value
	 */
	public void setPriorityValue(Integer priorityValue) {
		this.priorityValue = priorityValue;
	}

	/**
	 * Gets the creation time.
	 *
	 * @return the creation time
	 */
	@JsonIgnore
	public Date getCreationTime() {
		return creationTime;
	}

	/**
	 * Sets the creation time.
	 *
	 * @param creationTime the new creation time
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * Gets the modification time.
	 *
	 * @return the modification time
	 */
	@JsonIgnore
	public Date getModificationTime() {
		return modificationTime;
	}

	/**
	 * Sets the modification time.
	 *
	 * @param modificationTime the new modification time
	 */
	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	/**
	 * Gets the row key prefix.
	 *
	 * @return the row key prefix
	 */
	public String getRowKeyPrefix() {
		return rowKeyPrefix;
	}

	/**
	 * Sets the row key prefix.
	 *
	 * @param rowKeyPrefix the new row key prefix
	 */
	public void setRowKeyPrefix(String rowKeyPrefix) {
		this.rowKeyPrefix = rowKeyPrefix;
	}

	@JsonIgnore
	public GeographyL1 getGeographyL1() {
		return geographyL1;
	}

	public void setGeographyL1(GeographyL1 geographyL1) {
		this.geographyL1 = geographyL1;
	}

	@JsonIgnore
	public GeographyL2 getGeographyL2() {
		return geographyL2;
	}

	public void setGeographyL2(GeographyL2 geographyL2) {
		this.geographyL2 = geographyL2;
	}

	@JsonIgnore
	public GeographyL3 getGeographyL3() {
		return geographyL3;
	}

	public void setGeographyL3(GeographyL3 geographyL3) {
		this.geographyL3 = geographyL3;
	}

	@JsonIgnore
	public GeographyL4 getGeographyL4() {
		return geographyL4;
	}

	public void setGeographyL4(GeographyL4 geographyL4) {
		this.geographyL4 = geographyL4;
	}

	@JsonIgnore
	public SalesL1 getSalesL1() {
		return salesL1;
	}

	public void setSalesL1(SalesL1 salesL1) {
		this.salesL1 = salesL1;
	}

	@JsonIgnore
	public SalesL2 getSalesL2() {
		return salesL2;
	}

	public void setSalesL2(SalesL2 salesL2) {
		this.salesL2 = salesL2;
	}
	
	@JsonIgnore
	public SalesL3 getSalesL3() {
		return salesL3;
	}

	public void setSalesL3(SalesL3 salesL3) {
		this.salesL3 = salesL3;
	}

	@JsonIgnore
	public SalesL4 getSalesL4() {
		return salesL4;
	}

	public void setSalesL4(SalesL4 salesL4) {
		this.salesL4 = salesL4;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public AdvanceSearchConfiguration getAdvanceSearchConfiguration() {
		return advanceSearchConfiguration;
	}

	public void setAdvanceSearchConfiguration(AdvanceSearchConfiguration advanceSearchConfiguration) {
		this.advanceSearchConfiguration = advanceSearchConfiguration;
	}
	

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Override
	public String toString() {
		return "AdvanceSearch [id=" + id + ", name=" + name + ", priorityValue=" + priorityValue + ", creationTime=" + creationTime + ", modificationTime=" + modificationTime + ", typereference="
				+ typereference + ", rowKeyPrefix=" + rowKeyPrefix + ", geographyL1=" + geographyL1 + ", geographyL2=" + geographyL2 + ", geographyL3=" + geographyL3 + ", geographyL4=" + geographyL4
				+ ", salesL1=" + salesL1 + ", salesL2=" + salesL2 + ", salesL3=" + salesL3 + ", salesL4=" + salesL4 + ", advanceSearchConfiguration=" + advanceSearchConfiguration + ", vendor="
				+ vendor + ", domain=" + domain + "]";
	}

}
