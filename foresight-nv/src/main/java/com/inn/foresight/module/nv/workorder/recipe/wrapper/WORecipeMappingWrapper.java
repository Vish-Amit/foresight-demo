package com.inn.foresight.module.nv.workorder.recipe.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.Status;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;

@JpaWrapper
public class WORecipeMappingWrapper {

	private String workorderId;
	private String workorderName;
	private Status workorderStatus;
	private TemplateType workorderType;
	private Double completionPercentage;
	private String geographyL1Name;
	private String geographyL2Name;
	private String geographyL3Name;
	private String geographyL4Name;
	private String creationTime;
	private String recipeId;
	private String recipeName;
	private WORecipeMapping.Status recipeStatus;
	private String firstName;
	private String lastName;
	public WORecipeMappingWrapper(String workorderId, String workorderName, Status workorderStatus, TemplateType workorderType,
			Double completionPercentage, String geographyL1Name, String geographyL2Name, String geographyL3Name, String geographyL4Name,
			String creationTime, String recipeId, String recipeName, String firstName, String lastName, WORecipeMapping.Status recipeStatus) {
		super();
		this.workorderId = workorderId;
		this.workorderName = workorderName;
		this.workorderStatus = workorderStatus;
		this.workorderType = workorderType;
		this.completionPercentage = completionPercentage;
		this.geographyL1Name = geographyL1Name;
		this.geographyL2Name = geographyL2Name;
		this.geographyL3Name = geographyL3Name;
		this.geographyL4Name = geographyL4Name;
		this.creationTime = creationTime;
		this.recipeId = recipeId;
		this.recipeName = recipeName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.recipeStatus = recipeStatus;
	}
	/**
	 * @return the workorderId
	 */
	public String getWorkorderId() {
		return workorderId;
	}
	/**
	 * @param workorderId the workorderId to set
	 */
	public void setWorkorderId(String workorderId) {
		this.workorderId = workorderId;
	}
	/**
	 * @return the workorderName
	 */
	public String getWorkorderName() {
		return workorderName;
	}
	/**
	 * @param workorderName the workorderName to set
	 */
	public void setWorkorderName(String workorderName) {
		this.workorderName = workorderName;
	}
	/**
	 * @return the completionPercentage
	 */
	public Double getCompletionPercentage() {
		return completionPercentage;
	}
	/**
	 * @param completionPercentage the completionPercentage to set
	 */
	public void setCompletionPercentage(Double completionPercentage) {
		this.completionPercentage = completionPercentage;
	}
	/**
	 * @return the geographyL1Name
	 */
	public String getGeographyL1Name() {
		return geographyL1Name;
	}
	/**
	 * @param geographyL1Name the geographyL1Name to set
	 */
	public void setGeographyL1Name(String geographyL1Name) {
		this.geographyL1Name = geographyL1Name;
	}
	/**
	 * @return the geographyL2Name
	 */
	public String getGeographyL2Name() {
		return geographyL2Name;
	}
	/**
	 * @param geographyL2Name the geographyL2Name to set
	 */
	public void setGeographyL2Name(String geographyL2Name) {
		this.geographyL2Name = geographyL2Name;
	}
	/**
	 * @return the geographyL3Name
	 */
	public String getGeographyL3Name() {
		return geographyL3Name;
	}
	/**
	 * @param geographyL3Name the geographyL3Name to set
	 */
	public void setGeographyL3Name(String geographyL3Name) {
		this.geographyL3Name = geographyL3Name;
	}
	/**
	 * @return the geographyL4Name
	 */
	public String getGeographyL4Name() {
		return geographyL4Name;
	}
	/**
	 * @param geographyL4Name the geographyL4Name to set
	 */
	public void setGeographyL4Name(String geographyL4Name) {
		this.geographyL4Name = geographyL4Name;
	}
	/**
	 * @return the creationTime
	 */
	public String getCreationTime() {
		return creationTime;
	}
	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	/**
	 * @return the recipeId
	 */
	public String getRecipeId() {
		return recipeId;
	}
	/**
	 * @param recipeId the recipeId to set
	 */
	public void setRecipeId(String recipeId) {
		this.recipeId = recipeId;
	}
	/**
	 * @return the recipeNAme
	 */
	public String getRecipeName() {
		return recipeName;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Status getWorkorderStatus() {
		return workorderStatus;
	}
	public void setWorkorderStatus(Status workorderStatus) {
		this.workorderStatus = workorderStatus;
	}
	public WORecipeMapping.Status getRecipeStatus() {
		return recipeStatus;
	}
	public void setRecipeStatus(WORecipeMapping.Status recipeStatus) {
		this.recipeStatus = recipeStatus;
	}
	public void setRecipeName(String recipeName) {
		this.recipeName = recipeName;
	}
	public TemplateType getWorkorderType() {
		return workorderType;
	}
	public void setWorkorderType(TemplateType workorderType) {
		this.workorderType = workorderType;
	}
	
}
