package com.inn.foresight.module.nv.core.workorder.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.core.workorder.constants.GenericWorkorderConstants;
import com.inn.product.um.geography.model.GeographyL1;
import com.inn.product.um.geography.model.GeographyL2;
import com.inn.product.um.geography.model.GeographyL3;
import com.inn.product.um.geography.model.GeographyL4;
import com.inn.product.um.user.model.User;



/** The Class GenericWorkorder. */

	@NamedQuery(name = "getWorkorderIdListFromWOId", 
		query = "SELECT g.workorderId FROM GenericWorkorder g WHERE g.workorderId Like CONCAT(:workorderId, '%')")
	
	@NamedQuery(name = "getWorkorderByWOId", 
		query = "SELECT g FROM GenericWorkorder g WHERE g.workorderId = :workorderId")
	
	@NamedQuery(name = "findAllNVWorkorders", 
		query = "SELECT g FROM GenericWorkorder g WHERE g.templateType in (:templateList) AND g.status in (:statusList) and "
				+ "(case when (g.templateType in ('NV_ADHOC_LD','NV_ADHOC_BRTI','NV_ADHOC_IB','NV_IB_BENCHMARK','NV_ADHOC_BRTI_DRIVE','NV_ADHOC_BRTI_ST','NV_ADHOC_OD') and g.status = 'NOT_STARTED') "
				+ "then false else true end) = true and g.deleted is false ORDER BY g.modificationTime DESC ")
	
	@NamedQuery(name = "getTotalCountForNVWO",
		query = "SELECT count(g.id) FROM GenericWorkorder g where g.templateType in (:templateList) and g.status in (:statusList) and "
				+ "(case when (g.templateType in ('NV_ADHOC_LD','NV_ADHOC_BRTI','NV_ADHOC_IB','NV_IB_BENCHMARK','NV_ADHOC_BRTI_DRIVE','NV_ADHOC_BRTI_ST','NV_ADHOC_OD') and g.status = 'NOT_STARTED') "
				+ "then false else true end) = true and g.deleted is false")

	
    @NamedQuery(name="findAllWorkorderWithinTimeRange",
    	query = "SELECT g FROM GenericWorkorder g WHERE g.templateType in (:templateList) AND g.status in (:statusList) AND (g.creationTime >= :startTime AND g.creationTime <= :endTime ) and g.deleted is false ORDER BY g.modificationTime DESC")

    @NamedQuery(name=GenericWorkorderConstants.GET_ALL_WO_COUNT_BY_DATE_QUERY,query ="SELECT count(g.id) FROM GenericWorkorder g WHERE g.templateType in (:templateList) and DATE_FORMAT(g.creationTime,'%d-%m-%y') =:date and g.deleted=0")

    @NamedQuery(name=GenericWorkorderConstants.GET_ADHOC_WO_DAYWISE_COUNT_QUERY,query="SELECT new com.inn.foresight.module.nv.core.workorder.wrapper.WorkorderCountWrapper(count(g.id),DATE_FORMAT(g.creationTime,'%d-%m-%y')) FROM GenericWorkorder g WHERE g.templateType in (:templateList) and date(g.creationTime) between :startDate and :endDate and g.deleted=0 group by DATE_FORMAT(g.creationTime,'%d-%m-%y')")
    
    @NamedQuery(name = GenericWorkorderConstants.GET_WO_COUNT_BY_TEMPLATE_TYPE_AND_STATUS_BY_DATE_QUERY, query="SELECT new com.inn.foresight.module.nv.core.workorder.wrapper.WorkorderCountWrapper(count(g.id),g.status,g.templateType) FROM GenericWorkorder g WHERE g.templateType in (:templateList) and g.status in (:statusList) and DATE_FORMAT(g.creationTime,'%d-%m-%y') =:date  and g.deleted=0 group by g.templateType,g.status")
    
    @NamedQuery(name = GenericWorkorderConstants.GET_DAY_WISE_WO_COUNT_BY_STATUS_QUERY, query="SELECT new com.inn.foresight.module.nv.core.workorder.wrapper.WorkorderCountWrapper(count(g.id),DATE_FORMAT(g.creationTime,'%d-%m-%y'),g.status) FROM GenericWorkorder g WHERE g.templateType  in (:templateList) and g.status in (:statusList) and date(g.creationTime) between :startDate and :endDate and g.deleted=0 group by DATE_FORMAT(g.creationTime,'%d-%m-%y'),g.status")
    
    @NamedQuery(name =GenericWorkorderConstants.GET_DAY_WISE_ASSIGNED_WO_COUNT_QUERY,query="SELECT new com.inn.foresight.module.nv.core.workorder.wrapper.WorkorderCountWrapper(count(g.id),DATE_FORMAT(g.creationTime,'%d-%m-%y')) FROM GenericWorkorder g WHERE g.templateType in (:templateList) and date(g.creationTime) between :startDate and :endDate and g.deleted=0 group by DATE_FORMAT(g.creationTime,'%d-%m-%y')")
  
	 @NamedQuery(name=GenericWorkorderConstants.GET_DUE_WO_DAY_WISE_COUNT_QUERY,query="SELECT new com.inn.foresight.module.nv.core.workorder.wrapper.WorkorderCountWrapper(count(g.id),DATE_FORMAT(g.dueDate,'%d-%m-%y')) FROM GenericWorkorder g,BpmnTaskCandidate bpmnTaskCandidate WHERE date(g.dueDate) between :startDate AND :endDate and g.status in (:statusList)  and g.deleted=0 and g.workorderId=bpmnTaskCandidate.bpmnTask.bpmnWorkorder.workorderNo group by DATE_FORMAT(g.dueDate,'%d-%m-%y')")
	
    @NamedQuery(name=GenericWorkorderConstants.GET_DUE_WO_LIST_BY_DATE_QUERY,query="SELECT new com.inn.foresight.module.nv.core.workorder.wrapper.WorkorderCountWrapper(gw.workorderName,gw.workorderId,gw.status,bpmnTaskCandidate.user.firstName,bpmnTaskCandidate.user.lastName) FROM GenericWorkorder gw, BpmnTaskCandidate bpmnTaskCandidate WHERE DATE_FORMAT(gw.dueDate,'%d-%m-%y') =:date and gw.deleted=0 and gw.status in (:statusList) and gw.workorderId=bpmnTaskCandidate.bpmnTask.bpmnWorkorder.workorderNo")
    
    @NamedQuery(name = GenericWorkorderConstants.GET_WO_FOR_REPORT_BY_DATE_QUERY,query ="SELECT gw FROM GenericWorkorder gw WHERE DATE_FORMAT(gw.creationTime,'%d-%m-%y') =:date")

	@NamedQuery(name= "getGenericWOForUser", query="Select g from GenericWorkorder g  where g.assignedTo.userName=:userName and g.status in ('INPROGRESS','NOT_STARTED')")
	
	@NamedQuery(name = "getWorkorderByIds",query = "SELECT g FROM GenericWorkorder g WHERE g.id in(:workorderIds)")

	@NamedQuery(name = "findAllWorkorder",
	query = "SELECT g FROM GenericWorkorder g join g.createdBy u inner join  UserRole ur  inner join ur.role r join r.permission p where p.name='WEB_LOGIN_PERMISSION'  order by g.modificationTime desc ")

	@NamedQuery(name = "getTotalWorkorderCount",
	query = "SELECT count(g) FROM GenericWorkorder g join g.createdBy u inner join  UserRole ur inner join ur.role r join r.permission p where p.name='WEB_LOGIN_PERMISSION'  order by g.modificationTime desc ")

	@NamedQuery(name = "getWorkorderByWOIdWithUser",
	query = "SELECT g FROM GenericWorkorder g join fetch g.assignedTo WHERE g.id = :id")

	@NamedQuery(name = "getWorkorderByWOIdWithGeographyL3",
			query = "SELECT g FROM GenericWorkorder g join fetch g.geographyl3 WHERE g.id = :id")

		@FilterDef(name = "SearchFilter", parameters = { @ParamDef(name = "searchString", type = "java.lang.String") })

		@FilterDef(name = "GWGeographyL1Filter", parameters = {
				@ParamDef(name = "geographyId", type = "java.lang.Integer") })

		@FilterDef(name = "GWGeographyL2Filter", parameters = {
				@ParamDef(name = "geographyId", type = "java.lang.Integer") })

		@FilterDef(name = "GWGeographyL3Filter", parameters = {
				@ParamDef(name = "geographyId", type = "java.lang.Integer") })

		@FilterDef(name = "GWGeographyL4Filter", parameters = {
				@ParamDef(name = "geographyId", type = "java.lang.Integer") })
		
		@FilterDef(name = "GWCreatorFilter", parameters = {
				@ParamDef(name = "creatorId", type = "java.lang.Integer") })
		
		@FilterDef(name = "GWOMetaFilter", parameters = {
				@ParamDef(name = "entitytype", type = "java.lang.String"),
				@ParamDef(name = "entityvalue", type = "java.lang.String")})
		
		@FilterDef(name = "ArchivedFilter", parameters = {
				@ParamDef(name = "isArchived", type = "java.lang.Boolean") })
	
		@FilterDef(name = GenericWorkorderConstants.WO_ID_FILTER, parameters = {
			@ParamDef(name = "woId", type = "java.lang.String") })

		@FilterDef(name = GenericWorkorderConstants.WO_NAME_FILTER, parameters = {
			@ParamDef(name = "woName", type = "java.lang.String") })

		@FilterDef(name = GenericWorkorderConstants.REMARK_FILTER, parameters = {
			@ParamDef(name = "remark", type = "java.lang.String") })
	
		@FilterDef(name = GenericWorkorderConstants.ASSIGNED_TO_FILTER, parameters = {
			@ParamDef(name = "assignedTo", type = "java.lang.String")})
	
		@FilterDef(name = GenericWorkorderConstants.ASSIGNED_BY_FILTER, parameters = {
			@ParamDef(name = "assignedBy", type = "java.lang.String") })

	
	
	
		@Filters(value = {
		@Filter(name = "SearchFilter", condition = "genericworkorderid_pk in (select g.genericworkorderid_pk from GenericWorkorder g "
				+ "where g.workordername like (:searchString) or g.workorderid like (:searchString) or g.remark like (:searchString))"),

		@Filter(name = "GWGeographyL1Filter", condition = "geographyl1id_fk in (:geographyId)"),

		@Filter(name = "GWGeographyL2Filter", condition = "geographyl2id_fk in (:geographyId)"),

		@Filter(name = "GWGeographyL3Filter", condition = "geographyl3id_fk in (:geographyId)"),

		@Filter(name = "GWGeographyL4Filter", condition = "geographyl4id_fk in (:geographyId)"),
		
		@Filter(name = "GWCreatorFilter", condition = "creatorid_fk = :creatorId"),
		
		@Filter(name = "GWOMetaFilter", condition = "genericworkorderid_pk in (select g1.genericworkorderid_fk from  GWOMeta g1 where g1.entitytype=(:entitytype)  and g1.entityvalue in (:entityvalue) )"),
	
		@Filter(name = "ArchivedFilter", condition = "archived = :isArchived"),
		
		
		@Filter(name = GenericWorkorderConstants.WO_ID_FILTER, condition = "workorderid like (:woId)"),
		@Filter(name = GenericWorkorderConstants.WO_NAME_FILTER, condition = "workordername like (:woName)"),
		@Filter(name = GenericWorkorderConstants.REMARK_FILTER, condition = "remark like (:remark)"),
		@Filter(name = GenericWorkorderConstants.ASSIGNED_TO_FILTER, condition = "(assignedtoid_fk in ( select u.userid_pk from User u where u.userName like (:assignedTo) or CONCAT(u.firstname,u.lastname) like (:assignedTo)) or "
				+ "genericworkorderid_pk in (select g1.genericworkorderid_fk from  WOUserMapping g1 where g1.userid_fk in (select u.userid_pk from User u where u.userName like (:assignedTo)  or CONCAT(u.firstname,u.lastname) like (:assignedTo))))"),
		@Filter(name = GenericWorkorderConstants.ASSIGNED_BY_FILTER, condition = "creatorid_fk in ( select u.userid_pk from User u where u.userName like (:assignedBy)  or CONCAT(u.firstname,u.lastname) like (:assignedBy))"),		
})

@Entity
@XmlRootElement(name = "GenericWorkorder")
@Table(name = "GenericWorkorder")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class GenericWorkorder implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Enum Status. */
	public enum Status {

		/** The new. */
		NEW("NEW"),
		/** The not started. */
		NOT_STARTED("NOT_STARTED"),
		/** The inprogress. */
		INPROGRESS("IN_PROGRESS"),
		/** The completed. */
		COMPLETED("COMPLETED"),
		/** The Reopen. */
		REOPEN("REOPEN"),
		
		ON_HOLD("ON_HOLD"),
		
		/** The expired. */
		EXPIRED("EXPIRED");
		
		/** The value. */
		private String value;

		/**
		 * Instantiates a new status.
		 *
		 * @param value
		 *            the value
		 */
		private Status(String value) {
			this.value = value;
		}

		/** Instantiates a new status. */
		private Status() {
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public String getValue() {
			return value;
		}
	}
	
	/** The Enum TemplateType. */
	public enum TemplateType {
		INBUILDING_WORKFLOW("INBUILDINGWORKFLOW"),
        NV_SSVT_QUICK("SSVTQUICK"),
        NV_SSVT_FULL("SSVTFULL"),
        NV_SSVT_IBC_QUICK("IBCQUICK"),
        NV_SSVT_IBC_FULL("IBCFULL"),
		/** The nv ssvt. */
		NV_SSVT("NVSSVT"),
		/** The nv live drive. */
		NV_LIVE_DRIVE("NVLIVEDRIVE"),
		/** The nv drive. */
		NV_ADHOC_LD(GenericWorkorderConstants.NVADHOC),
		/** The nv drive. */
		NV_DRIVE("NV_DRIVE"),
		/** The nv clot. */
		NV_CLOT("NVCLOT"),
		/** The nv opendrive. */
		NV_OPENDRIVE("NVOPENDRIVE"),
		/** The nv opendrive. */
		NV_ADHOC_OD(GenericWorkorderConstants.NVADHOC),
		/** The nv stationary. */
		NV_STATIONARY("NVSTATIONARY"),
		/** The nv inbuilding. */
		NV_INBUILDING("NVINBUILDING"),
		/** The nv brti. */
		NV_ADHOC_IB(GenericWorkorderConstants.NVADHOC),
		/** The nv brti. */
		NV_BRTI("NVBRTI"),
		/** The nv brti. */
		NV_ADHOC_BRTI_DRIVE(GenericWorkorderConstants.NVADHOC),
		/** The nv brti. */
		NV_ADHOC_BRTI_ST("NVADHOC"),
		/** The benchmark. */
		NV_BENCHMARK("NVBENCHMARK"),
		/** The NV InBuilding benchmark. */
		NV_IB_BENCHMARK(GenericWorkorderConstants.NVADHOC),
		/** The nv complaints. */
		NV_COMPLAINTS("NVCOMPLAINTS"),
		/** The nv complaints. */
		NV_STEALTH("NVSTEALTH"),
		
		/** The nv adhoc brti. */
		NV_ADHOC_BRTI("NV_ADHOC_BRTI"),
		/** The cm comparision with other. */
		CM_COMPARISION_WITH_OTHER("CM_COMPARISION_WITH_OTHER"),
		/** The cm comparision with self. */
		CM_COMPARISION_WITH_SELF("CM_COMPARISION_WITH_SELF"),
		
		SCANNING_RECEIVER("SCANNING_RECEIVER"),
		
		NV_STATIC_PROBE(GenericWorkorderConstants.NVADHOC);
		/** The value. */
		private String value;

		/**
		 * Instantiates a new template type.
		 *
		 * @param value
		 *            the value
		 */
		private TemplateType(String value) {
			this.value = value;
		}

		/** Instantiates a new template type. */
		private TemplateType() {
		}

		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		public String getValue() {
			return value;
		}
	}

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "genericworkorderid_pk")
	private Integer id;
	
	/** The workorder name. */
	@Basic
	@Column(name = "workorderid")
	private String workorderId;

	/** The workorder name. */
	@Basic
	@Column(name = "workordername")
	private String workorderName;

	/** The template type. */
	@Enumerated(EnumType.STRING)
	@Column(name = "templatetype")
	private TemplateType templateType;

	/** The status. */
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;
	
	/** The description. */
	@Basic
	@Column(name = "description")
	private String description;
	
	/** The description. */
	@Basic
	@Column(name = "remark")
	private String remark;

	/** The completion percentage. */
	@Column(name = "completionpercentage")
	private Double completionPercentage;
	
	/** The creation time. */
	
	@Column(name = "creationtime")
	private Date creationTime;
	
	/** The modification time. */
	@Column(name = "modificationtime")
	private Date modificationTime;

	/** The completion time. */
	@Column(name = "completiontime")
	private Date completionTime;
	
	/** The due date. */
	@Column(name = "duedate")
	private Date dueDate;

	/** The due date. */
	@Column(name = "startdate")
	private Date startDate;
	
	/** The created by. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creatorid_fk", nullable = true)
	private User createdBy;

	/** The modified by. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lastmodifierid_fk", nullable = true)
	private User modifiedBy;

	/** The assigned to. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "assignedtoid_fk", nullable = true)
	private User assignedTo;
	
	/** The gwo meta. */
	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyColumn(name = "entitytype")
	@Column(name = "entityvalue")
	@CollectionTable(name = "GWOMeta", joinColumns = @JoinColumn(name = "genericworkorderid_fk"))
	private Map<String, String> gwoMeta;	

	/** The assigned to. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl1id_fk", nullable = true)
	private GeographyL1 geographyl1;
	
	/** The assigned to. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl2id_fk", nullable = true)
	private GeographyL2 geographyl2;
	
	/** The assigned to. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl3id_fk", nullable = true)
	private GeographyL3 geographyl3;
	
	/** The assigned to. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geographyl4id_fk", nullable = true)
	private GeographyL4 geographyl4;

	/** The modification time. */
	@Column(name = "deleted")
	private Boolean deleted;

	/** The isarchived. */
	@Column(name = "archived")
	private Boolean isArchived=Boolean.FALSE;
	
	@Column(name = "flowstatus", nullable = true)
	private Boolean flowStatus;
	
	@Column(name = "iboldframeworkstatus")
	private Boolean ibOldWorkorderStatus=false;
	
	
	
	public Boolean getIbOldWorkorderStatus() {
		return ibOldWorkorderStatus;
	}

	public void setIbOldWorkorderStatus(Boolean ibOldWorkorderStatus) {
		this.ibOldWorkorderStatus = ibOldWorkorderStatus;
	}

	public Boolean getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(Boolean flowStatus) {
		this.flowStatus = flowStatus;
	}

	/**
	 * Gets the isarchived.
	 *
	 * @return the isarchived
	 */
	public Boolean getIsarchived() {
		return isArchived;
	}

	/**
	 * Sets the isarchived.
	 *
	 * @param isarchived the new isarchived
	 */
	public void setIsarchived(Boolean isarchived) {
		this.isArchived = isarchived;
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
	 * Gets the workorder id.
	 *
	 * @return the workorder id
	 */
	public String getWorkorderId() {
		return workorderId;
	}

	/**
	 * Sets the workorder id.
	 *
	 * @param workorderId the new workorder id
	 */
	public void setWorkorderId(String workorderId) {
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
	 * Gets the status.
	 *
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(Status status) {
		this.status = status;
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
	 * Gets the completion percentage.
	 *
	 * @return the completion percentage
	 */
	public Double getCompletionPercentage() {
		return completionPercentage;
	}

	/**
	 * Sets the completion percentage.
	 *
	 * @param completionPercentage the new completion percentage
	 */
	public void setCompletionPercentage(Double completionPercentage) {
		this.completionPercentage = completionPercentage;
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
	 * Gets the completion time.
	 *
	 * @return the completion time
	 */
	public Date getCompletionTime() {
		return completionTime;
	}

	/**
	 * Sets the completion time.
	 *
	 * @param completionTime the new completion time
	 */
	public void setCompletionTime(Date completionTime) {
		this.completionTime = completionTime;
	}

	/**
	 * Gets the due date.
	 *
	 * @return the due date
	 */
	public Date getDueDate() {
		return dueDate;
	}

	/**
	 * Sets the due date.
	 *
	 * @param dueDate the new due date
	 */
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * Gets the created by.
	 *
	 * @return the created by
	 */
	public User getCreatedBy() {
		return createdBy;
	}

	/**
	 * Sets the created by.
	 *
	 * @param createdBy the new created by
	 */
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * Gets the modified by.
	 *
	 * @return the modified by
	 */
	public User getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * Sets the modified by.
	 *
	 * @param modifiedBy the new modified by
	 */
	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * Gets the assigned to.
	 *
	 * @return the assigned to
	 */
	public User getAssignedTo() {
		return assignedTo;
	}

	/**
	 * Sets the assigned to.
	 *
	 * @param assignedTo the new assigned to
	 */
	public void setAssignedTo(User assignedTo) {
		this.assignedTo = assignedTo;
	}

	/**
	 * Gets the gwo meta.
	 *
	 * @return the gwo meta
	 */
	public Map<String, String> getGwoMeta() {
		return gwoMeta;
	}

	/**
	 * Sets the gwo meta.
	 *
	 * @param gwoMeta the gwo meta
	 */
	public void setGwoMeta(Map<String, String> gwoMeta) {
		this.gwoMeta = gwoMeta;
	}

	/**
	 * Gets the start date.
	 *
	 * @return the start date
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Sets the start date.
	 *
	 * @param startDate the new start date
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the geographyl 1.
	 *
	 * @return the geographyl 1
	 */
	public GeographyL1 getGeographyl1() {
		return geographyl1;
	}

	/**
	 * Sets the geographyl 1.
	 *
	 * @param geographyl1 the new geographyl 1
	 */
	public void setGeographyl1(GeographyL1 geographyl1) {
		this.geographyl1 = geographyl1;
	}

	/**
	 * Gets the geographyl 2.
	 *
	 * @return the geographyl 2
	 */
	public GeographyL2 getGeographyl2() {
		return geographyl2;
	}

	/**
	 * Sets the geographyl 2.
	 *
	 * @param geographyl2 the new geographyl 2
	 */
	public void setGeographyl2(GeographyL2 geographyl2) {
		this.geographyl2 = geographyl2;
	}

	/**
	 * Gets the geographyl 3.
	 *
	 * @return the geographyl 3
	 */
	public GeographyL3 getGeographyl3() {
		return geographyl3;
	}

	/**
	 * Sets the geographyl 3.
	 *
	 * @param geographyl3 the new geographyl 3
	 */
	public void setGeographyl3(GeographyL3 geographyl3) {
		this.geographyl3 = geographyl3;
	}

	/**
	 * Gets the geographyl 4.
	 *
	 * @return the geographyl 4
	 */
	public GeographyL4 getGeographyl4() {
		return geographyl4;
	}

	/**
	 * Sets the geographyl 4.
	 *
	 * @param geographyl4 the new geographyl 4
	 */
	public void setGeographyl4(GeographyL4 geographyl4) {
		this.geographyl4 = geographyl4;
	}

	/**
	 * Gets the remark.
	 *
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * Sets the remark.
	 *
	 * @param remark the new remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/** Instantiates a new generic workorder. */
	public GenericWorkorder() {
		super();
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

	@Override
	public String toString() {
		return "GenericWorkorder [id=" + id + ", workorderId=" + workorderId + ", workorderName=" + workorderName
				+ ", templateType=" + templateType + ", status=" + status + ", description=" + description + ", remark="
				+ remark + ", completionPercentage=" + completionPercentage + ", creationTime=" + creationTime
				+ ", modificationTime=" + modificationTime + ", completionTime=" + completionTime + ", dueDate="
				+ dueDate + ", startDate=" + startDate + ", createdBy=" + createdBy + ", modifiedBy=" + modifiedBy
				+ ", assignedTo=" + assignedTo + ", gwoMeta=" + gwoMeta + ", geographyl1=" + geographyl1
				+ ", geographyl2=" + geographyl2 + ", geographyl3=" + geographyl3 + ", geographyl4=" + geographyl4
				+ ", deleted=" + deleted + ", isArchived=" + isArchived + ", flowStatus=" + flowStatus + "]";
	}
	
}
