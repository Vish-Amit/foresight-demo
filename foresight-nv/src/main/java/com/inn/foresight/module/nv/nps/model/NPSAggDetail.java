package com.inn.foresight.module.nv.nps.model;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.geography.model.GeographyL4;
	

/** The Class NPSAggDetail. */
@XmlRootElement(name = "NPSAggDetail")
@Entity
@Table(name = "NPSAggDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })

public class NPSAggDetail {
	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "npsaggdetailid_pk")
	private Integer id;
	
	/** The geography L 4. */
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "geographyl4id_fk", nullable = false)
	private GeographyL4 geographyL4;

	/** The weekno. */
	@Basic
	@Column(name = "weekno")
	public Integer weekno;
	
	/** The customertype. */
	@Basic
	@Column(name = "customertype")
	public String customertype;
	
	/** The eventtype. */
	@Basic
	@Column(name = "eventtype")
	public String eventtype;
	
	/** The customercount. */
	@Basic
	@Column(name = "customercount")
	public Long customercount;
	
	/** The kpi. */
	@Basic
	@Column(name = "kpi")
	public String kpi;
	
	@Basic
	@Column(name = "ratingsum")
	public Double ratingsum;
	/** The operator. */
	@Basic
	@Column(name = "operator")
	public String operator;
	
	/** The technology. */
	@Basic
	@Column(name = "technology")
	public String technology;
	
	/** The processdate. */
	@Basic
	@Column(name = "processdate")
	public String processdate;
	
	@Basic
	@Column(name = "kpisum")
	public Double kpisum;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public GeographyL4 getGeographyL4() {
		return geographyL4;
	}

	public void setGeographyL4(GeographyL4 geographyL4) {
		this.geographyL4 = geographyL4;
	}

	public Integer getWeekno() {
		return weekno;
	}

	public void setWeekno(Integer weekno) {
		this.weekno = weekno;
	}

	public String getCustomertype() {
		return customertype;
	}

	public void setCustomertype(String customertype) {
		this.customertype = customertype;
	}

	public String getEventtype() {
		return eventtype;
	}

	public void setEventtype(String eventtype) {
		this.eventtype = eventtype;
	}

	public Long getCustomercount() {
		return customercount;
	}

	public void setCustomercount(Long customercount) {
		this.customercount = customercount;
	}

	public String getKpi() {
		return kpi;
	}

	public void setKpi(String kpi) {
		this.kpi = kpi;
	}

	public Double getRatingsum() {
		return ratingsum;
	}

	public void setRatingsum(Double ratingsum) {
		this.ratingsum = ratingsum;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getProcessdate() {
		return processdate;
	}

	public void setProcessdate(String processdate) {
		this.processdate = processdate;
	}

	public Double getKpisum() {
		return kpisum;
	}

	public void setKpisum(Double kpisum) {
		this.kpisum = kpisum;
	}
	
	
}
