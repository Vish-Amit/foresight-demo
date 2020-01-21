package com.inn.foresight.core.infra.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NamedQueries({
		@NamedQuery(name = "getvalueByNameAndWeekNo", query = "select DATE_FORMAT(STR_TO_DATE(j.value,'%Y-%m-%d'),'%d%m%Y') from JobHistory j where j.name=:name and j.weekno=:weekno"),
		@NamedQuery(name = "getvalueByName", query = "select max(date_format(str_to_date(s.value,'%Y-%m-%d'),'%Y-%m-%d')) from JobHistory s where s.name =:name"),
		@NamedQuery(name = "getCurrentAvailableTileDate", query = "select date_format(max(str_to_date(j.value,'%Y-%m-%d')),'%d-%m-%Y') from JobHistory j where j.name =:key and str_to_date(j.value,'%Y-%m-%d') <= str_to_date(:date,'%Y-%m-%d')"),
		@NamedQuery(name = "getLatestAvailableTileDate", query = "select date_format(max(str_to_date(j.value,'%Y-%m-%d')),'%d-%m-%Y') from JobHistory j where j.name =:key and str_to_date(j.value,'%Y-%m-%d')>= str_to_date(:startDate,'%Y-%m-%d') and str_to_date(j.value,'%Y-%m-%d')<= str_to_date(:endDate,'%Y-%m-%d')"),
		@NamedQuery(name = "getLatestAvailableTileDateByendDate", query = "select date_format(max(str_to_date(j.value,'%Y-%m-%d')),'%d-%m-%Y') from JobHistory j where j.name =:key and str_to_date(j.value,'%Y-%m-%d')<= str_to_date(:endDate,'%Y-%m-%d')"),
		// @NamedQuery(name = "getLatestAvailableTileDate", query = "select
		// date_format(max(str_to_date(j.value,'%d-%m-%Y')),'%d-%m-%Y') from JobHistory
		// j where j.name =:key and (case when (:startDate IS null) then
		// str_to_date(j.value,'%d-%m-%Y') <= FROM_UNIXTIME(CAST(:endDate as int)/1000)
		// else str_to_date(j.value,'%d-%m-%Y') >= FROM_UNIXTIME(CAST(:startDate as
		// int)/1000) and str_to_date(j.value,'%d-%m-%Y')<= FROM_UNIXTIME(CAST(:endDate
		// as int)/1000) end)")
		@NamedQuery(name = "getMaxValueByKeyAndWeekNo", query = "select max(j.value) as date from JobHistory j where j.name=:name"),
		@NamedQuery(name = "getListOfDateByName", query = "select j from JobHistory j where j.name =:name"),
		@NamedQuery(name = "getvalueListByName", query = "select j.value from JobHistory j where j.name =:name order by  j.creationTime desc"),

})

@Entity
@Table(name = "JobHistory")
@XmlRootElement(name = "JobHistory")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class JobHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5851819554930082828L;
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "jobhistoryid_pk")
	private Integer id;

	/** The name. */
	@Column(name = "weekno")
	private String weekno;

	/** The name. */
	@Column(name = "name")
	private String name;

	/** The   value. */
	@Column(name = "value")
	private String value;

	public JobHistory(Integer id, String weekno, String name, String value, Date creationTime, Date modificationTime) {
		super();
		this.id = id;
		this.weekno = weekno;
		this.name = name;
		this.value = value;
		this.creationTime = creationTime;
		this.modificationTime = modificationTime;
	}
	
	public JobHistory(Integer id, String name) {
		super();
		this.id = id;
		
		this.name = name;
		
	}
	

	public JobHistory() {
		super();
	}


	/** The creation time. */
	@Column(name = "creationTime")
	private Date creationTime;

	/** The modification time. */
	@Column(name = "modificationTime")
	private Date modificationTime;

	public JobHistory(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWeekno() {
		return weekno;
	}

	public void setWeekno(String weekno) {
		this.weekno = weekno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(Date modificationTime) {
		this.modificationTime = modificationTime;
	}

	@Override
	public String toString() {
		return "JobHistory [id=" + id + ", weekno=" + weekno + ", name=" + name + ", value=" + value + ", creationTime="
				+ creationTime + ", modificationTime=" + modificationTime + "]";
	}

}
