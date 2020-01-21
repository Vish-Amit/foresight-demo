package com.inn.foresight.core.adminplanning.model;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.google.gson.Gson;
import com.inn.foresight.core.adminplanning.util.AlgorithmPlanningConstant;
import com.inn.foresight.core.adminplanning.wrapper.UIConfiguration;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Views.TabView;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.user.model.User;

/**
 * The Class AlgorithmProperty.
 */
@NamedQueries({
		@NamedQuery(name = "getExistingL1Exceptions", query = "select DISTINCT new com.inn.foresight.core.adminplanning.wrapper.AlgorithmResponse(geographyL1Fk.id,'"
				+ AlgorithmPlanningConstant.L1
				+ "',geographyL1Fk.name) from AlgorithmProperty a where geographyL1Fk is not null and a.algorithm.id = :id"),
		@NamedQuery(name = "getExistingL2Exceptions", query = "select DISTINCT new com.inn.foresight.core.adminplanning.wrapper.AlgorithmResponse(geographyL2Fk.id,'"
				+ AlgorithmPlanningConstant.L2
				+ "',geographyL2Fk.name) from AlgorithmProperty a where geographyL2Fk is not null and a.algorithm.id = :id"),
		@NamedQuery(name = "getExistingL3Exceptions", query = "select DISTINCT new com.inn.foresight.core.adminplanning.wrapper.AlgorithmResponse(geographyL3Fk.id,'"
				+ AlgorithmPlanningConstant.L3
				+ "',geographyL3Fk.name) from AlgorithmProperty a where geographyL3Fk is not null and a.algorithm.id = :id"),
		@NamedQuery(name = "getExistingL4Exceptions", query = "select DISTINCT new com.inn.foresight.core.adminplanning.wrapper.AlgorithmResponse(geographyL4Fk.id,'"
				+ AlgorithmPlanningConstant.L4
				+ "',geographyL4Fk.name) from AlgorithmProperty a where geographyL4Fk is not null and a.algorithm.id = :id"),
		@NamedQuery(name = "resetProperties", query = "select a from AlgorithmProperty a where a.algorithm.id = :id and (a.geographyL1Fk.id in (:l1List) or a.geographyL2Fk.id in (:l2List) or a.geographyL3Fk.id in (:l3List) or a.geographyL4Fk.id in (:l4List))"),
		@NamedQuery(name = "getProperties", query = "select a from AlgorithmProperty a where a.algorithm.id = :algorithmId"),
		@NamedQuery(name = "getAlgorithPropertyByName", query = "select a from AlgorithmProperty a where a.name = :name"),
		@NamedQuery(name= "getAlgorithmPropertyByIdAndName",query = "select a from  AlgorithmProperty a where a.name = :name and a.algorithm.id = :id "),
		@NamedQuery(name = "getAlgorithPropertyByNameAndAlgorithmName", query = "select a from AlgorithmProperty a where a.name = :algorithmPropertyName and a.algorithm.name = :algorithmName"),
		})      
		
@FilterDef(name = "APPANFilter")
@Filter(name = "APPANFilter", condition = "geographyl1id_fk is null and geographyl2id_fk is null and geographyl3id_fk is null and geographyl4id_fk is null")

@FilterDef(name = "APdeletedFilter", parameters = { @ParamDef(name = "deleted", type = "java.lang.Boolean") })
@Filter(name = "APdeletedFilter", condition = "deleted = :deleted")

@FilterDef(name = "APgeographyL1Filter", parameters = { @ParamDef(name = "id", type = "java.lang.Integer") })
@Filter(name = "APgeographyL1Filter", condition = "geographyl1id_fk = :id")

@FilterDef(name = "APgeographyL2Filter", parameters = { @ParamDef(name = "id", type = "java.lang.Integer") })
@Filter(name = "APgeographyL2Filter", condition = "geographyl2id_fk = :id")

@FilterDef(name = "APgeographyL3Filter", parameters = { @ParamDef(name = "id", type = "java.lang.Integer") })
@Filter(name = "APgeographyL3Filter", condition = "geographyl3id_fk = :id")

@FilterDef(name = "APgeographyL4Filter", parameters = { @ParamDef(name = "id", type = "java.lang.Integer") })
@Filter(name = "APgeographyL4Filter", condition = "geographyl4id_fk = :id")

@XmlRootElement(name = "AlgorithmProperty")
@Entity
//@Audited
@Table(name = "AlgorithmProperty")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class AlgorithmProperty implements Serializable {

	/** The Constant serialVersionUID. */

	private static final long serialVersionUID = -1575772688740187585L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@JsonView(value = { TabView.class })
	@Column(name = "algorithmpropertyid_pk")
	private Integer id;

	/** The algorithm. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "algorithmid_fk")
	@JsonBackReference
	@JsonIgnore
	private Algorithm algorithm;

	/** The geography L 1 fk. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "geographyl1id_fk")
	private GeographyL1 geographyL1Fk;

	/** The geography L 2 fk. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "geographyl2id_fk")
	private GeographyL2 geographyL2Fk;

	/** The geography L 3 fk. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "geographyl3id_fk")
	private GeographyL3 geographyL3Fk;

	/** The geography L 4 fk. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "geographyl4id_fk")
	private GeographyL4 geographyL4Fk;

	/** The deleted. */
	@Basic
	@Audited
	@Column(name = "deleted")
	private Boolean deleted;

	/** The name. */
	@Basic
	@Column(name = "name")
	private String name;
	
	/** The previous value. */
	@Basic
	@Audited
	@Column(name = "previousvalue")
	private String previousValue;

	/** The value. */
	@Basic
	@Audited
	@Column(name = "value")
	private String value;

	/** The default value. */
	@Basic
	@Column(name = "defaultValue")
	private String defaultValue;

	/** The creation time. */
	@Basic
	@Column(name = "creationtime")
	private Date creationTime;

	/** The modification time. */
	@Basic
	@Audited
	@Column(name = "modificationtime")
	private Date modificationTime;

	/** The modifier. */
	@ManyToOne(fetch = FetchType.LAZY)
	@Audited
	@JoinColumn(name = "lastmodifierid_fk")
	private User modifier;

	/** The reason. */
	@Basic
	@Audited
	private String reason;

	/** The description. */
	@Basic
	@Column(name = "description")
	private String description;

	/** The UI configuration. */
	@Basic
	@Column(name = "uiconfiguration")
	private String uiConfiguration;

	/** The configuration. */
	@Transient
	@JsonProperty
	private UIConfiguration configuration;

	/**
	 * Read UI configuration.
	 *
	 * @return the UI configuration
	 */
	public UIConfiguration getConfiguration() {
		if (this.uiConfiguration != null && this.configuration == null) {
			this.configuration = new Gson().fromJson(this.uiConfiguration, UIConfiguration.class);
		}
		return this.configuration;
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
	 * Gets the ui configuration.
	 *
	 * @return the ui configuration
	 */
	@JsonIgnore
	public String getUiConfiguration() {
		return uiConfiguration;
	}

	/**
	 * Sets the ui configuration.
	 *
	 * @param uiConfiguration the new ui configuration
	 */
	public void setUiConfiguration(String uiConfiguration) {
		this.uiConfiguration = uiConfiguration;
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
	 * Gets the geography L 1 fk.
	 *
	 * @return the geography L 1 fk
	 */
	@JsonIgnore
	@JsonProperty
	public GeographyL1 getGeographyL1Fk() {
		return geographyL1Fk;
	}

	/**
	 * Sets the geography L 1 fk.
	 *
	 * @param geographyL1Fk the new geography L 1 fk
	 */
	public void setGeographyL1Fk(GeographyL1 geographyL1Fk) {
		this.geographyL1Fk = geographyL1Fk;
	}

	/**
	 * Gets the geography L 2 fk.
	 *
	 * @return the geography L 2 fk
	 */
	@JsonIgnore
	@JsonProperty
	public GeographyL2 getGeographyL2Fk() {
		return geographyL2Fk;
	}

	/**
	 * Sets the geography L 2 fk.
	 *
	 * @param geographyL2Fk the new geography L 2 fk
	 */
	public void setGeographyL2Fk(GeographyL2 geographyL2Fk) {
		this.geographyL2Fk = geographyL2Fk;
	}

	/**
	 * Gets the geography L 3 fk.
	 *
	 * @return the geography L 3 fk
	 */
	@JsonIgnore
	@JsonProperty
	public GeographyL3 getGeographyL3Fk() {
		return geographyL3Fk;
	}

	/**
	 * Sets the geography L 3 fk.
	 *
	 * @param geographyL3Fk the new geography L 3 fk
	 */
	public void setGeographyL3Fk(GeographyL3 geographyL3Fk) {
		this.geographyL3Fk = geographyL3Fk;
	}

	/**
	 * Gets the geography L 4 fk.
	 *
	 * @return the geography L 4 fk
	 */
	@JsonIgnore
	@JsonProperty
	public GeographyL4 getGeographyL4Fk() {
		return geographyL4Fk;
	}

	/**
	 * Sets the geography L 4 fk.
	 *
	 * @param geographyL4Fk the new geography L 4 fk
	 */
	public void setGeographyL4Fk(GeographyL4 geographyL4Fk) {
		this.geographyL4Fk = geographyL4Fk;
	}

	/**
	 * Gets the default value.
	 *
	 * @return the default value
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * Sets the default value.
	 *
	 * @param defaultValue the new default value
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * Gets the creation time.
	 *
	 * @return the creation time
	 */
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
	 * Gets the modifier.
	 *
	 * @return the modifier
	 */
	@JsonIgnore
	public User getModifier() {
		return modifier;
	}

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		if (this.modifier != null) {
			return this.modifier.getFirstName() + ForesightConstants.SPACE + this.modifier.getLastName();
		}
		return null;
	}

	/**
	 * Sets the modifier.
	 *
	 * @param modifier the new modifier
	 */
	public void setModifier(User modifier) {
		this.modifier = modifier;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the deleted.
	 *
	 * @return the deleted
	 */
	public Boolean getDeleted() {
		return deleted;
	}

	/**
	 * Sets the deleted.
	 *
	 * @param deleted the new deleted
	 */
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
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
	 * Gets the previous value.
	 *
	 * @return the previous value
	 */
	public String getPreviousValue() {
		return previousValue;
	}

	/**
	 * Sets the previous value.
	 *
	 * @param previousValue the new previous value
	 */
	public void setPreviousValue(String previousValue) {
		this.previousValue = previousValue;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the modification time.
	 *
	 * @return the modification time
	 */
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
	 * Gets the reason.
	 *
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * Sets the reason.
	 *
	 * @param reason the new reason
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * Sets the configuration.
	 *
	 * @param configuration the new configuration
	 */
	public void setConfiguration(UIConfiguration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Sets the algorithm.
	 *
	 * @param algorithm the new algorithm
	 */
	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
	}
	
	/**
	 * Gets the algorithm id.
	 *
	 * @return the algorithm id
	 */
	@JsonIgnore
	public Integer getAlgorithmId() {
		if (this.algorithm != null) {
			return this.algorithm.getId();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AlgorithmProperty [id=" + id + ", deleted=" + deleted + ", name=" + name + ", previousValue=" + previousValue + ", value=" + value + ", defaultValue=" + defaultValue + ", creationTime=" + creationTime + ", modificationTime=" + modificationTime + ", reason=" + reason + ", description=" + description + ", uiConfiguration=" + uiConfiguration + "]";
	}

}
