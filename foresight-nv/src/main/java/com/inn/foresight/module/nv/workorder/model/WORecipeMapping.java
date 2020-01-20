package com.inn.foresight.module.nv.workorder.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.workorder.constant.NVWorkorderConstant;
import com.inn.foresight.module.nv.workorder.recipe.model.Recipe;

/** The Class WORecipeMapping. */


		@NamedQuery(name = "getRecipeWOByWOName", query = "SELECT w from WORecipeMapping w WHERE w.genericWorkorder.workorderId in (:workorderId) "
				+ "AND w.genericWorkorder.templateType in(:workorderType) and w.genericWorkorder.deleted is false AND w.genericWorkorder.modificationTime "
				+ ">= :modifiedTime")

		@NamedQuery(name = "getRecipeWOByUser", query = "SELECT w from WORecipeMapping w WHERE w.genericWorkorder.assignedTo.userid = :userId")

		@NamedQuery(name = "getWORecipeByGWOId", query = "SELECT w from WORecipeMapping w WHERE w.genericWorkorder.id = :workorderId")
		@NamedQuery(name = "getWORecipeByGWOIds", query = "SELECT w from WORecipeMapping w WHERE w.genericWorkorder.id in(:workorderId)")

		@NamedQuery(name = "getWORecipeMappingById", query = "SELECT w from WORecipeMapping w WHERE w.id = :woRecipeMappingId")

		@NamedQuery(name = "getWORecipeMappingByGWOIdAndListofId", query = "SELECT w from WORecipeMapping w WHERE w.genericWorkorder.id = :workorderId AND w.id in (:recipeList)")

		@NamedQuery(name = "getWorkOrderDetailByImei", query = "SELECT new com.inn.foresight.module.nv.workorder.model.WORecipeMapping(w.id,w.genericWorkorder.id,w.woTime,w.woETime,w.imei,w.recipe.category,w.genericWorkorder.workorderId,w.genericWorkorder.workorderName) from WORecipeMapping w WHERE "
				+ "w.imei = :imei AND w.genericWorkorder.templateType= :templateType AND ((w.woTime >= :startTime AND  w.woTime <= :endTime) OR (w.woETime >= :startTime AND w.woETime <= :endTime))")

		@NamedQuery(name = "getWORecipeListByDateGeography", query = "SELECT new com.inn.foresight.module.nv.workorder.recipe.wrapper.WORecipeMappingWrapper(w.genericWorkorder.workorderId,w.genericWorkorder.workorderName,w.genericWorkorder.status,w.genericWorkorder.templateType,w.genericWorkorder.completionPercentage,w.genericWorkorder.geographyl1.name,w.genericWorkorder.geographyl2.name,w.genericWorkorder.geographyl3.name,w.genericWorkorder.geographyl4.name,DATE_FORMAT(w.genericWorkorder.creationTime,'%D-%M-%Y'),w.recipe.recipeId,w.recipe.name,b.user.firstName,b.user.lastName,w.status) from WORecipeMapping w ,BpmnTaskCandidate b  WHERE DATE_FORMAT(w.genericWorkorder.creationTime,'%d-%m-%y') =:date and w.genericWorkorder.workorderId=b.bpmnTask.bpmnWorkorder.workorderNo")
		
        @NamedQuery(name=  "getWoidsListByRecipeCount", query="SELECT new com.inn.foresight.module.nv.workorder.wrapper.WORecipeWrapper(w.genericWorkorder.id,count(w)) from WORecipeMapping w where w.status = 'COMPLETED' group by w.genericWorkorder.id")
        
        @NamedQuery(name = "getWoRecipeListByRecipeId", query = "SELECT w from WORecipeMapping w WHERE w.recipe.id=:recipeId")
       
        @NamedQuery(name = "getNonDeletedWOByRecipeId", query = "SELECT distinct w.genericWorkorder from WORecipeMapping w where w.recipe.id=:recipeId and w.genericWorkorder.deleted is false")
	@NamedQuery(name = "findWoRecipeMappingByWorkOrderId" , query = "select w from WORecipeMapping w where w.genericWorkorder.id = :workrorderId")



		@FilterDef(name = NVWorkorderConstant.WORKORDER_ID_FILTER, parameters = {
				@ParamDef(name = NVWorkorderConstant.WORKORDER_ID, type = "java.lang.Integer") })
		@FilterDef(name = "GeographyL1Filter", parameters = {
				@ParamDef(name = "geographyId", type = "java.lang.Integer") })

		@FilterDef(name = "GeographyL2Filter", parameters = {
				@ParamDef(name = "geographyId", type = "java.lang.Integer") })

		@FilterDef(name = "GeographyL3Filter", parameters = {
				@ParamDef(name = "geographyId", type = "java.lang.Integer") })

		@FilterDef(name = "GeographyL4Filter", parameters = {
				@ParamDef(name = "geographyId", type = "java.lang.Integer") })


@Filters(value = {

		@Filter(name = NVWorkorderConstant.WORKORDER_ID_FILTER, condition = "worecipemappingid_pk in (select w.worecipemappingid_pk from WORecipeMapping w "
				+ "where genericworkorderid_fk = :workorderId)"),
		@Filter(name = "GeographyL1Filter", condition = "genericworkorderid_fk in (select g.genericworkorderid_pk from GenericWorkorder g where g.geographyl1id_fk in (:geographyId))"),

		@Filter(name = "GeographyL2Filter", condition = "genericworkorderid_fk in (select g.genericworkorderid_pk from GenericWorkorder g where g.geographyl2id_fk in (:geographyId))"),

		@Filter(name = "GeographyL3Filter", condition = "genericworkorderid_fk in (select g.genericworkorderid_pk from GenericWorkorder g where g.geographyl3id_fk in (:geographyId))"),

		@Filter(name = "GeographyL4Filter", condition = "genericworkorderid_fk in (select g.genericworkorderid_pk from GenericWorkorder g where g.geographyl4id_fk in (:geographyId))"), 

       /* @Filter(name = "WoRecipeCountFilter", condition = "genericworkorderid_pk in (select result.woId from (select count(w) as recipecount, w.genericworkorderid_fk as woId from WORecipeMapping w where w.status = 'COMPLETED' group by w.genericworkorderid_fk) result where result.recipecount=(:value))"), })
*/
		})

@Entity
@Table(name = "WORecipeMapping")
@XmlRootElement(name = "WORecipeMapping")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class WORecipeMapping implements Serializable {

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
		REOPEN("REOPEN"),
		ON_HOLD("ON_HOLD");
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

		/**
		 * Sets the value.
		 *
		 * @param value
		 *            the new value
		 */
		void setValue(String value) {
			this.value = value;
		}}

	public enum ProcessStatus {
		IN_PROGRESS("IN_PROGRESS"), NOT_STARTED("NOT_STARTED"), COMPLETED("COMPLETED"), FAILED("FAILED");

		String value;

		private ProcessStatus(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		private void setValue(String value) {
			this.value = value;
		}
	}

	/** The id. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "worecipemappingid_pk")
	private Integer id;

	/** The generic workorder. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "genericworkorderid_fk", nullable = true)
	private GenericWorkorder genericWorkorder;

	/** The recipe. */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "recipeid_fk", nullable = true)
	private Recipe recipe;

	/** The status. */
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;

	/** The is retried. */
	@Column(name = "retried")
	private Boolean isRetried;

	/** The is retried. */
	@Column(name = "waittime")
	private Long waitTime;

	/** The is retried. */
	@Column(name = "maxtime")
	private Long maxTime;

	/** The is retried. */
	@Column(name = "iterationcount")
	private Integer iterationCount;

	@Column(name = "kpi")
	private String kpi;
		
	@Column(name = "kpi2g")
	private String kpi2G;
	
	@Column(name = "kpi3g")
	private String kpi3G;

	@Column(name = "eventcall")
	private String eventcall;

	@Column(name = "eventmobility")
	private String eventmobility;

	@Column(name = "event")
	private String event;

	
	

	@Column(name = "eventsms")
	private String eventsms;

	@Column(name = "eventdownload")
	private String eventdownload;

	@Column(name = "eventtrackarea")
	private String eventtrackarea;


	/** The woorder execution time. */
	@Column(name = "wotime")
	private Date woTime;

	@Column(name = "woetime")
	private Date woETime;

	@Column(name = "imei")
	private String imei;
	
	@Transient
	private String category;

	@Transient
	private String workorderId;

	@Transient
	private String workorderName;

	@Transient
	private Integer genericId;
	
	


	@Column(name="lpci")
	private Integer lPci;

	@Column(name="learfcn")
	private Integer lEarfcn;

	@Column(name="lband")
	private Integer lBand;

	@Column(name="lrat")
	private String lRat;
	
	@Column(name = "operator")
	private String operator;

	@Enumerated(EnumType.STRING)
	@Column(name = "fileprocessstatus")
	private ProcessStatus fileProcessStatus;

	@Column(name = "processstarttime")
	private Date processStartTime;

	@Column(name = "processendtime")
	private Date processEndTime;

	@Column(name = "remark")
	private String remark;
	
	@Column(name = "flowprocessstatus")
	private Boolean flowProcessStatus;

	

	public Boolean getFlowProcessStatus() {
		return flowProcessStatus;
	}

	public void setFlowProcessStatus(Boolean flowProcessStatus) {
		this.flowProcessStatus = flowProcessStatus;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
	
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getKpi2G() {
		return kpi2G;
	}

	public void setKpi2G(String kpi2g) {
		kpi2G = kpi2g;
	}

	public String getKpi3G() {
		return kpi3G;
	}

	public void setKpi3G(String kpi3g) {
		kpi3G = kpi3g;
	}

	public Integer getlPci() {
		return lPci;
	}

	public void setlPci(Integer lPci) {
		this.lPci = lPci;
	}

	public Integer getlEarfcn() {
		return lEarfcn;
	}

	public void setlEarfcn(Integer lEarfcn) {
		this.lEarfcn = lEarfcn;
	}

	public Integer getlBand() {
		return lBand;
	}

	public void setlBand(Integer lBand) {
		this.lBand = lBand;
	}

	public String getlRat() {
		return lRat;
	}

	public void setlRat(String lRat) {
		this.lRat = lRat;
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
	 * @param id
	 *            the new id
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
	 * @param genericWorkOrder
	 *            the new generic workorder
	 */
	public void setGenericWorkorder(GenericWorkorder genericWorkOrder) {
		this.genericWorkorder = genericWorkOrder;
	}

	/**
	 * Gets the recipe.
	 *
	 * @return the recipe
	 */
	public Recipe getRecipe() {
		return recipe;
	}

	/**
	 * Sets the recipe.
	 *
	 * @param recipe
	 *            the new recipe
	 */
	public void setRecipe(Recipe recipe) {
		this.recipe = recipe;
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
	 * @param status
	 *            the new status
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * Gets the checks if is retried.
	 *
	 * @return the checks if is retried
	 */
	public Boolean getIsRetried() {
		return isRetried;
	}

	/**
	 * Sets the checks if is retried.
	 *
	 * @param isRetried
	 *            the new checks if is retried
	 */
	public void setIsRetried(Boolean isRetried) {
		this.isRetried = isRetried;
	}

	public Long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(Long waitTime) {
		this.waitTime = waitTime;
	}

	public Long getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(Long maxTime) {
		this.maxTime = maxTime;
	}

	public Integer getIterationCount() {
		return iterationCount;
	}

	public void setIterationCount(Integer iterationCount) {
		this.iterationCount = iterationCount;
	}

	public String getKpi() {
		return kpi;
	}

	public void setKpi(String kpi) {
		this.kpi = kpi;
	}

	public String getEventmobility() {
		return eventmobility;
	}

	public void setEventmobility(String eventmobility) {
		this.eventmobility = eventmobility;
	}

	public String getEventcall() {
		return eventcall;
	}

	public void setEventcall(String eventcall) {
		this.eventcall = eventcall;
	}

	public Date getWoTime() {
		return woTime;
	}

	public void setWoTime(Date woTime) {
		this.woTime = woTime;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public Date getWoETime() {
		return woETime;
	}

	public void setWoETime(Date woETime) {
		this.woETime = woETime;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getWorkorderId() {
		return workorderId;
	}

	public void setWorkorderId(String workorderId) {
		this.workorderId = workorderId;
	}

	public String getWorkorderName() {
		return workorderName;
	}

	public void setWorkorderName(String workorderName) {
		this.workorderName = workorderName;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public String getEventsms() {
		return eventsms;
	}

	public void setEventsms(String eventsms) {
		this.eventsms = eventsms;
	}

	public String getEventdownload() {
		return eventdownload;
	}

	public void setEventdownload(String eventdownload) {
		this.eventdownload = eventdownload;
	}
	
	public String getEventtrackarea() {
		return eventtrackarea;
	}

	public void setEventtrackarea(String eventtrackarea) {
		this.eventtrackarea = eventtrackarea;
	}

	public ProcessStatus getFileProcessStatus() {
		return fileProcessStatus;
	}

	public void setFileProcessStatus(ProcessStatus fileProcessStatus) {
		this.fileProcessStatus = fileProcessStatus;
	}

	public Date getProcessStartTime() {
		return processStartTime;
	}

	public void setProcessStartTime(Date processStartTime) {
		this.processStartTime = processStartTime;
	}

	public Date getProcessEndTime() {
		return processEndTime;
	}

	public void setProcessEndTime(Date processEndTime) {
		this.processEndTime = processEndTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public WORecipeMapping(Integer id, Integer genericId, Date woTime, Date woETime, String imei, String category,
			String workorderId, String workorderName) {
		super();
		this.id = id;
		this.genericId = genericId;
		this.woTime = woTime;
		this.woETime = woETime;
		this.imei = imei;
		this.category = category;
		this.workorderId = workorderId;
		this.workorderName = workorderName;
	}
	
	public WORecipeMapping() {
		super();
	}

	@Override
	public String toString() {
		return "WORecipeMapping [id=" + id + ", genericWorkorder=" + genericWorkorder + ", recipe=" + recipe
				+ ", status=" + status + ", isRetried=" + isRetried + ", waitTime=" + waitTime + ", maxTime=" + maxTime
				+ ", iterationCount=" + iterationCount + ", kpi=" + kpi + ", kpi2G=" + kpi2G + ", kpi3G=" + kpi3G
				+ ", eventcall=" + eventcall + ", eventmobility=" + eventmobility + ", event=" + event + ", eventsms="
				+ eventsms + ", eventdownload=" + eventdownload + ", eventtrackarea=" + eventtrackarea + ", woTime="
				+ woTime + ", woETime=" + woETime + ", imei=" + imei + ", category=" + category + ", workorderId="
				+ workorderId + ", workorderName=" + workorderName + ", genericId=" + genericId + ", lPci=" + lPci
				+ ", lEarfcn=" + lEarfcn + ", lBand=" + lBand + ", lRat=" + lRat + ", operator=" + operator
				+ ", fileProcessStatus=" + fileProcessStatus + ", processStartTime=" + processStartTime
				+ ", processEndTime=" + processEndTime + ", remark=" + remark + ", flowProcessStatus="
				+ flowProcessStatus + "]";
	}		
	
}
