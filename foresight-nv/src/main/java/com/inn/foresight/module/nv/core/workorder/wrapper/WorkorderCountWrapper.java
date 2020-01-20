package com.inn.foresight.module.nv.core.workorder.wrapper;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.Status;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;

@JpaWrapper
@RestWrapper
public class WorkorderCountWrapper {

	private String date;
	private String type;
	private Long count;
	private String workorderName;
	private String workorderId;
	private String assignedTo;
	private Status status;
	private TemplateType templateType;

	private Double completionPercentage;
	private String geographyL1Name;
	private String geographyL2Name;
	private String geographyL3Name;
	private String geographyL4Name;
	private String creationTime;


	public WorkorderCountWrapper() {
		super();
	}


	public WorkorderCountWrapper(String workorderName, String workorderId, Status status, String firstName,
			String lastName) {
		super();
		this.workorderName = workorderName;
		this.workorderId = workorderId;
		this.status = status;
		this.assignedTo = firstName + " " + lastName;
	}

	public WorkorderCountWrapper(Long count, String date) {
		super();
		this.date = date;
		this.count = count;
	}

	public WorkorderCountWrapper(Long count, Status status, TemplateType template) {
		super();
		this.templateType = template;
		this.count = count;
		this.status = status;
	}

	public WorkorderCountWrapper(Long count, String date, Status status) {
		super();
		this.count = count;
		this.status = status;
		this.date = date;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the count
	 */
	public Long getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(Long count) {
		this.count = count;
	}

	/**
	 * @return the workorderName
	 */
	public String getWorkorderName() {
		return workorderName;
	}

	/**
	 * @param workorderName
	 *            the workorderName to set
	 */
	public void setWorkorderName(String workorderName) {
		this.workorderName = workorderName;
	}

	/**
	 * @return the workorderId
	 */
	public String getWorkorderId() {
		return workorderId;
	}

	/**
	 * @param workorderId
	 *            the workorderId to set
	 */
	public void setWorkorderId(String workorderId) {
		this.workorderId = workorderId;
	}

	/**
	 * @return the assignedTo
	 */
	public String getAssignedTo() {
		return assignedTo;
	}

	/**
	 * @param assignedTo
	 *            the assignedTo to set
	 */
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return the templateType
	 */
	public TemplateType getTemplateType() {
		return templateType;
	}

	/**
	 * @param templateType
	 *            the templateType to set
	 */
	public void setTemplateType(TemplateType templateType) {
		this.templateType = templateType;
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

	
}
