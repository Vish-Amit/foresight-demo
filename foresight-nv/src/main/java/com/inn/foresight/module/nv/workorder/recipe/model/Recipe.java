package com.inn.foresight.module.nv.workorder.recipe.model;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.user.model.User;

/** The Class Recipe. */
@NamedQueries({
	
	

		@NamedQuery(name = "getListOfRecipeIds",
			query = "SELECT r.recipeId FROM Recipe r where r.recipeId Like CONCAT(:recipeId, '%')"),

		@NamedQuery(name = "findAllRecipe",
			query = "SELECT  new com.inn.foresight.module.nv.workorder.recipe.wrapper.RecipeWrapper("
					+ "rc.id, rc.recipeId, rc.name, rc.type, rc.description, rc.category, rc.creationTime, rc.modificationTime, rc.creator.firstName, rc.creator.lastName, rc.scriptJson)"
					+ " FROM Recipe rc where rc.source!='Mobile' AND rc.deleted is false order by rc.modificationTime desc"),

		@NamedQuery(name = "getTotalRecipeCount",
			query = "SELECT count(r.id) FROM Recipe r where r.source!='Mobile' AND r.deleted is false "),
		
		@NamedQuery(name = "getRecipeByCategory",
			query = "SELECT new com.inn.foresight.module.nv.workorder.recipe.wrapper.RecipeWrapper("
					+ "r.id, r.recipeId, r.name, r.type, r.description, r.category, r.creationTime, r.modificationTime, r.creator.firstName, r.creator.lastName, r.scriptJson)"
					+ " FROM Recipe r WHERE r.category IN (:recipeCategory) AND r.source!='Mobile' AND r.deleted is false order by r.modificationTime desc"),
		
		@NamedQuery(name = "deleteAllByPk",
			query = "DELETE FROM Recipe r where r.id IN (:recipeIdPk) AND r.type != 'PREDEFINED'"),
		
		@NamedQuery(name = "findAllByPkList",
			query = "select r FROM Recipe r where r.id in (:recipeIdPk)"),
		
		@NamedQuery(name = "findAllByRecipeId",
			query = "select r FROM Recipe r where r.recipeId in (:recipeIdList)"),

		@NamedQuery(name = "findAllByRecipeIdList", query = "select new com.inn.foresight.module.nv.workorder.recipe.wrapper.RecipeWrapper(r.id, r.recipeId, r.name, r.type, r.description, r.category, r.creationTime, r.modificationTime, r.creator.firstName, r.creator.lastName, r.scriptJson) FROM Recipe r where r.recipeId in (:recipeIdList)")

})

@Entity
@Table(name = "Recipe")
@XmlRootElement(name = "Recipe")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Recipe implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "recipeid_pk")
	private Integer id;

	/** The recipe id. */
	@Column(name = "recipeid")
	private String recipeId;

	/** The name. */
	@Column(name = "name")
	private String name;

	/** The type. */
	@Column(name = "type")
	private String type;

	/** The description. */
	@Column(name = "description")
	private String description;

	/** The category. */
	@Column(name = "category")
	private String category;

	/** The creation time. */
	@Column(name = "creationtime")
	private Date creationTime;

	
	
	/** The modification time. */
	@Column(name = "modificationtime")
	private Date modificationTime;

	/** The creator. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creatorid_fk")
	private User creator;

	/** The script json. */
	@Lob
	private String scriptJson;
	
	/** The creation time. */
	@Column(name = "source")
	private String source;
	
	/** The creation time. */
	@Column(name = "deleted")
	private Boolean deleted;
	
	
	/** The binning Type */	
	@Column(name = "binningtype")
	private String binningType;


	/** The binning Parameter */	
	@Column(name = "binningparameter")
	private Integer binningParameter;


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
	 * @param id
	 *            the new id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the recipe id.
	 *
	 * @return the recipe id
	 */
	public String getRecipeId() {
		return recipeId;
	}

	/**
	 * Sets the recipe id.
	 *
	 * @param recipeId
	 *            the new recipe id
	 */
	public void setRecipeId(String recipeId) {
		this.recipeId = recipeId;
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
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @param description
	 *            the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category.
	 *
	 * @param category
	 *            the new category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Gets the creator.
	 *
	 * @return the creator
	 */
	public User getCreator() {
		return creator;
	}

	/**
	 * Sets the creator.
	 *
	 * @param creator
	 *            the new creator
	 */
	public void setCreator(User creator) {
		this.creator = creator;
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
	 * @param creationTime
	 *            the new creation time
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
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
	 * @param modificationTime
	 *            the new modification time
	 */
	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	/**
	 * Gets the script json.
	 *
	 * @return the script json
	 */
	public String getScriptJson() {
		return scriptJson;
	}

	/**
	 * Sets the script json.
	 *
	 * @param scriptJson
	 *            the new script json
	 */
	public void setScriptJson(String scriptJson) {
		this.scriptJson = scriptJson;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the deleted
	 */
	public Boolean getDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getBinningType() {
		return binningType;
	}

	public void setBinningType(String binningType) {
		this.binningType = binningType;
	}

	public Integer getBinningParameter() {
		return binningParameter;
	}

	public void setBinningParameter(Integer binningParameter) {
		this.binningParameter = binningParameter;
	}
	
	
	

	
}
