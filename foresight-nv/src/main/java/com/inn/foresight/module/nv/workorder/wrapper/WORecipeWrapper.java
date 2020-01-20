package com.inn.foresight.module.nv.workorder.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.workorder.recipe.model.Recipe;
import com.inn.product.um.user.model.User;

/** The Class WORecipeWrapper. */
@JpaWrapper
public class WORecipeWrapper {
	
	/** The workorder id. */
	private Integer workorderId;
	
	/** The workorder name. */
	private String workorderName;
	
	/** The user list. */
	private List<User> userList;
	
	/** The creation date. */
	private Long creationDate;
	
	/** The modification date. */
	private Long modificationDate;
		
	/** The recipe list. */
	private List<Recipe> recipeList;
	
	/** The site id list. */
	private List<Integer> siteIdList;

	/** The template type. */
	private TemplateType templateType;
	
	private Long count;
	

	public WORecipeWrapper(Integer workorderId, Long count) {
		super();
		this.workorderId = workorderId;
		this.count = count;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	/**
	 * Gets the workorder id.
	 *
	 * @return the workorder id
	 */
	public Integer getWorkorderId() {
		return workorderId;
	}

	/**
	 * Sets the workorder id.
	 *
	 * @param workorderId the new workorder id
	 */
	public void setWorkorderId(Integer workorderId) {
		this.workorderId = workorderId;
	}

	/**
	 * Gets the workorder name.
	 *
	 * @return the workorder name
	 */
	public String getWorkorderName() {
		return workorderName;
	}

	/**
	 * Sets the workorder name.
	 *
	 * @param workorderName the new workorder name
	 */
	public void setWorkorderName(String workorderName) {
		this.workorderName = workorderName;
	}

	/**
	 * Gets the user list.
	 *
	 * @return the user list
	 */
	public List<User> getUserList() {
		return userList;
	}

	/**
	 * Sets the user list.
	 *
	 * @param userList the new user list
	 */
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	/**
	 * Gets the creation date.
	 *
	 * @return the creation date
	 */
	public Long getCreationDate() {
		return creationDate;
	}

	/**
	 * Sets the creation date.
	 *
	 * @param creationDate the new creation date
	 */
	public void setCreationDate(Long creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * Gets the modification date.
	 *
	 * @return the modification date
	 */
	public Long getModificationDate() {
		return modificationDate;
	}

	/**
	 * Sets the modification date.
	 *
	 * @param modificationDate the new modification date
	 */
	public void setModificationDate(Long modificationDate) {
		this.modificationDate = modificationDate;
	}

	/**
	 * Gets the recipe list.
	 *
	 * @return the recipe list
	 */
	public List<Recipe> getRecipeList() {
		return recipeList;
	}

	/**
	 * Sets the recipe list.
	 *
	 * @param recipeList the new recipe list
	 */
	public void setRecipeList(List<Recipe> recipeList) {
		this.recipeList = recipeList;
	}

	/**
	 * Gets the site id list.
	 *
	 * @return the site id list
	 */
	public List<Integer> getSiteIdList() {
		return siteIdList;
	}

	/**
	 * Sets the site id list.
	 *
	 * @param siteIdList the new site id list
	 */
	public void setSiteIdList(List<Integer> siteIdList) {
		this.siteIdList = siteIdList;
	}

	/**
	 * Gets the template type.
	 *
	 * @return the template type
	 */
	public TemplateType getTemplateType() {
		return templateType;
	}

	/**
	 * Sets the template type.
	 *
	 * @param templateType the new template type
	 */
	public void setTemplateType(TemplateType templateType) {
		this.templateType = templateType;
	}

	

	/**
	 * To string.
	 *
	 * @return the string
	 */
	
	@Override
	public String toString() {
		return "WORecipeWrapper [workorderId=" + workorderId + ", workorderName=" + workorderName + ", userList="
				+ userList + ", creationDate=" + creationDate + ", modificationDate=" + modificationDate
				+ ", recipeList=" + recipeList + ", siteIdList=" + siteIdList + ", templateType=" + templateType
				+ ", count=" + count + "]";
	}
	

	
}
