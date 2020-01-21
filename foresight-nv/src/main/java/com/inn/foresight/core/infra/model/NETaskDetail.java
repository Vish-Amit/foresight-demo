package com.inn.foresight.core.infra.model;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.commons.encoder.AESUtils;

import net.bull.javamelody.Main;

@NamedQuery(name = "getRollOutStatusOfSiteByNameAndBand", query = "select nt from NETaskDetail nt where nt.neBandDetail.networkElement.neName = :name and nt.neBandDetail.neFrequency = :band order by nt.actualEndDate asc")
@NamedQuery(name = "getSiteStageTaskStatusBySiteId", query = "select ntd from NETaskDetail ntd where ntd.neBandDetail.networkElement.neName =:siteId AND ntd.neBandDetail.neFrequency=:band and  ntd.taskName=:taskName")

/**
 * The Class NETaskDetail.
 */
@Entity
@Table(name = "NETaskDetail")
@XmlRootElement(name = "NETaskDetail")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class NETaskDetail {

    /**
     * The id. *
     */
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @Column(name = "netaskdetailid_pk")
    private Integer id;

    /**
     * The network element.
     */
    @JoinColumn(name = "nebanddetailid_fk", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private NEBandDetail neBandDetail;

    /** The task name. */
    @Basic
    @Column(name = "name")
    private String taskName;

    /** The task status. */
    @Basic
    @Column(name = "status")
    private String taskStatus;

    /** The planned start date. */
    @Basic
    @Column(name = "plannedstartdate")
    private Date plannedStartDate;

    /** The planned end date. */
    @Basic
    @Column(name = "plannedenddate")
    private Date plannedEndDate;

    /** The actual start date. */
    @Basic
    @Column(name = "actualstartdate")
    private Date actualStartDate;

    /** The actual end date. */
    @Basic
    @Column(name = "actualenddate")
    private Date actualEndDate;

    /** The completion status. */
    @Basic
    @Column(name = "completionstatus")
    private String completionStatus;

    /** The execution order. */
    @Basic
    @Column(name = "executionorder")
    private Integer executionOrder;
    
    @Basic
    @Column(name = "taskday")
    private Integer taskDay;

    @JoinColumn(name = "netaskid_fk", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private NETask  neTask;
    
    public NETask getNeTask() {
		return neTask;
	}

	public void setNeTask(NETask neTask) {
		this.neTask = neTask;
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
     * Gets the task name.
     *
     * @return the task name
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Sets the task name.
     *
     * @param taskName the new task name
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * Gets the task status.
     *
     * @return the task status
     */
    public String getTaskStatus() {
        return taskStatus;
    }

    /**
     * Sets the task status.
     *
     * @param taskStatus the new task status
     */
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * Gets the planned start date.
     *
     * @return the planned start date
     */
    public Date getPlannedStartDate() {
        return plannedStartDate;
    }

    /**
     * Sets the planned start date.
     *
     * @param plannedStartDate the new planned start date
     */
    public void setPlannedStartDate(Date plannedStartDate) {
        this.plannedStartDate = plannedStartDate;
    }

    /**
     * Gets the planned end date.
     *
     * @return the planned end date
     */
    public Date getPlannedEndDate() {
        return plannedEndDate;
    }

    /**
     * Sets the planned end date.
     *
     * @param plannedEndDate the new planned end date
     */
    public void setPlannedEndDate(Date plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }

    /**
     * Gets the actual start date.
     *
     * @return the actual start date
     */
    public Date getActualStartDate() {
        return actualStartDate;
    }

    /**
     * Sets the actual start date.
     *
     * @param actualStartDate the new actual start date
     */
    public void setActualStartDate(Date actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    /**
     * Gets the actual end date.
     *
     * @return the actual end date
     */
    public Date getActualEndDate() {
        return actualEndDate;
    }

    /**
     * Sets the actual end date.
     *
     * @param actualEndDate the new actual end date
     */
    public void setActualEndDate(Date actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    /**
     * Gets the completion status.
     *
     * @return the completion status
     */
    public String getCompletionStatus() {
        return completionStatus;
    }

    /**
     * Sets the completion status.
     *
     * @param completionStatus the new completion status
     */
    public void setCompletionStatus(String completionStatus) {
        this.completionStatus = completionStatus;
    }

    public NEBandDetail getNeBandDetail() {
        return neBandDetail;
    }

    public void setNeBandDetail(NEBandDetail neBandDetail) {
        this.neBandDetail = neBandDetail;
    }

    public Integer getExecutionOrder() {
        return executionOrder;
    }

    public void setExecutionOrder(Integer executionOrder) {
        this.executionOrder = executionOrder;
    }

	public Integer getTaskDay() {
		return taskDay;
	}

	public void setTaskDay(Integer taskDay) {
		this.taskDay = taskDay;
	}

	@Override
	public String toString() {
		return "NETaskDetail [id=" + id + ", neBandDetail=" + neBandDetail + ", taskName=" + taskName + ", taskStatus="
				+ taskStatus + ", plannedStartDate=" + plannedStartDate + ", plannedEndDate=" + plannedEndDate
				+ ", actualStartDate=" + actualStartDate + ", actualEndDate=" + actualEndDate + ", completionStatus="
				+ completionStatus + ", executionOrder=" + executionOrder + ", taskDay=" + taskDay + ", neTask="
				+ neTask + "]";
	}
//public static void main(String[] args) {
//	System.out.println(AESUtils.encrypt("root"));
//}
	

}