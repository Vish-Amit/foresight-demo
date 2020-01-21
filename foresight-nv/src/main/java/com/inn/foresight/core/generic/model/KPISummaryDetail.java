package com.inn.foresight.core.generic.model;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.product.um.geography.model.GeographyL4;

/**
 * @author innoeye
 *
 */

@NamedQuery(name="getWeeklyKPIData", query="select DATE(ADDDATE(min(k.date), (-WEEKDAY(min(k.date))))), sum(k.sum)/sum(sampleCount0), NOT(MONTH(ADDDATE(DATE(ADDDATE(min(k.date), (-WEEKDAY(min(k.date))))), -7)) = month(DATE(ADDDATE(min(k.date), (-WEEKDAY(min(k.date))))))) from KPISummaryDetail k where module=:module AND technology = 'LTE' AND DATE(ADDDATE(k.date, (-WEEKDAY(k.date)))) BETWEEN :startMonday AND :latestMonday AND kpiname=:kpi AND band=:band GROUP BY DATE(ADDDATE(k.date, (-WEEKDAY(k.date))))")

@FilterDefs(value = {
    @FilterDef(name = "filterKPISummaryL4",
        parameters = {@ParamDef(name = "id", type = "java.lang.Integer")}),
    @FilterDef(name = "filterKPISummaryL3",
        parameters = {@ParamDef(name = "id", type = "java.lang.Integer")}),
    @FilterDef(name = "filterKPISummaryL2",
        parameters = {@ParamDef(name = "id", type = "java.lang.Integer")}),
    @FilterDef(name = "filterKPISummaryL1",
        parameters = {@ParamDef(name = "id", type = "java.lang.Integer")}),
    @FilterDef(name = "filterKPISummaryOperator",
    parameters = {@ParamDef(name = "operator", type = "java.lang.String")})})

@Filters(value = {@Filter(name = "filterKPISummaryL4", condition = "geographyl4id_fk=:id"),
    @Filter(name = "filterKPISummaryL3",
        condition = "geographyl4id_fk in (select g4.geographyl4id_pk from GeographyL3 g3 inner join GeographyL4 g4 on g3.geographyl3id_pk=g4.geographyl3id_fk and g3.geographyl3id_pk=:id)"),
    @Filter(name = "filterKPISummaryL2",
        condition = "geographyl4id_fk in (select g4.geographyl4id_pk from GeographyL2 g2 inner join GeographyL3 g3 on g2.geographyl2id_pk=g3.geographyl2id_fk inner join GeographyL4 g4 on g3.geographyl3id_pk=g4.geographyl3id_fk and g2.geographyl2id_pk=:id)"),
    @Filter(name = "filterKPISummaryL1",
        condition = "geographyl4id_fk in (select g4.geographyl4id_pk from GeographyL1 g1 inner join GeographyL2 g2 on g1.geographyl1id_pk=g2.geographyl1id_fk inner join GeographyL3 g3 on g2.geographyl2id_pk=g3.geographyl2id_fk inner join GeographyL4 g4 on g3.geographyl3id_pk=g4.geographyl3id_fk and g1.geographyl1id_pk=:id)"),
    @Filter(name = "filterKPISummaryOperator", condition = "operatorname=:operator")})


@XmlRootElement(name = "KPISummaryDetail")
@Entity
@Table(name = "KPISummaryDetail")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class KPISummaryDetail {

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "kpisummarydetailid_pk")
	private Integer id;

	/** The band. */
	@Basic
	@Column(name = "band")
	public String band;

	/** The datasource. */
	@Basic
	@Column(name = "datasource")
	public String datasource;

	/** The date. */
	@Basic
	@Column(name = "datasourcedate")
	public String date;

	/** The env. */
	@Basic
	@Column(name = "environment")
	public String env;

	/** The kpi name. */
	@Basic
	@Column(name = "kpi")
	public String kpiname;

	/** The avgvalue. */
	@Basic
	@Column(name = "sumofavgvalue")
	public long sum;

	/** The count. */
	@Basic
	@Column(name = "totalsample")
	public Double sampleCount0;

	/** The count. */
	@Basic
	@Column(name = "validsample")
	public Double sampleCount1;

	/** The range 0. */
	@Basic
	@Column(name = "range0")
	public long range0;

	/** The range 1. */
	@Basic
	@Column(name = "range1")
	public long range1;

	/** The range 2. */
	@Basic
	@Column(name = "range2")
	public long range2;

	/** The is latest. */
	@Basic
	@Column(name = "latest")
	public boolean isLatest;

	/** The is latest. */
	@Basic
	@Column(name = "module")
	public String module;

	/** The geography L 4. */
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "geographyl4id_fk", nullable = false)
	private GeographyL4 geographyL4;

	/** The is technology. */
	@Basic
	@Column(name = "technology")
	public String technology;
	
	/** The is operatorname. */
	@Basic
	@Column(name = "operatorname")
	public String operatorName;


	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
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
	 * Gets the band.
	 *
	 * @return the band
	 */
	public String getBand() {
		return band;
	}

	/**
	 * Sets the band.
	 *
	 * @param band
	 *            the new band
	 */
	public void setBand(String band) {
		this.band = band;
	}

	/**
	 * Gets the datasource.
	 *
	 * @return the datasource
	 */
	public String getDatasource() {
		return datasource;
	}

	/**
	 * Sets the datasource.
	 *
	 * @param datasource
	 *            the new datasource
	 */
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Gets the env.
	 *
	 * @return the env
	 */
	public String getEnv() {
		return env;
	}

	/**
	 * Sets the env.
	 *
	 * @param env
	 *            the new env
	 */
	public void setEnv(String env) {
		this.env = env;
	}

	/**
	 * Gets the kpi name.
	 *
	 * @return the kpi name
	 */
	public String getKpiName() {
		return kpiname;
	}

	/**
	 * Sets the kpi name.
	 *
	 * @param kpiName
	 *            the new kpi name
	 */
	public void setKpiName(String kpiName) {
		this.kpiname = kpiName;
	}

	/**
	 * Gets the avgvalue.
	 *
	 * @return the avgvalue
	 */
	public long getAvgvalue() {
		return sum;
	}

	/**
	 * Sets the avgvalue.
	 *
	 * @param avgvalue
	 *            the new avgvalue
	 */
	public void setAvgvalue(long sum) {
		this.sum = sum;
	}

	/**
	 * Gets the range 0.
	 *
	 * @return the range 0
	 */
	public long getRange0() {
		return range0;
	}

	/**
	 * Sets the range 0.
	 *
	 * @param range0
	 *            the new range 0
	 */
	public void setRange0(long range0) {
		this.range0 = range0;
	}

	/**
	 * Gets the range 1.
	 *
	 * @return the range 1
	 */
	public long getRange1() {
		return range1;
	}

	/**
	 * Sets the range 1.
	 *
	 * @param range1
	 *            the new range 1
	 */
	public void setRange1(long range1) {
		this.range1 = range1;
	}

	/**
	 * Gets the range 2.
	 *
	 * @return the range 2
	 */
	public long getRange2() {
		return range2;
	}

	/**
	 * Sets the range 2.
	 *
	 * @param range2
	 *            the new range 2
	 */
	public void setRange2(long range2) {
		this.range2 = range2;
	}

	/**
	 * Gets the geography L 4.
	 *
	 * @return the geography L 4
	 */
	public GeographyL4 getGeographyL4() {
		return geographyL4;
	}

	/**
	 * Sets the geography L 4.
	 *
	 * @param geographyL4
	 *            the new geography L 4
	 */
	public void setGeographyL4(GeographyL4 geographyL4) {
		this.geographyL4 = geographyL4;
	}

	public String getKpiname() {
		return kpiname;
	}

	public void setKpiname(String kpiname) {
		this.kpiname = kpiname;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Double getSampleCount0() {
		return sampleCount0;
	}

	public void setSampleCount0(Double sampleCount0) {
		this.sampleCount0 = sampleCount0;
	}

	public Double getSampleCount1() {
		return sampleCount1;
	}

	public void setSampleCount1(Double sampleCount1) {
		this.sampleCount1 = sampleCount1;
	}

	public boolean isIslatest() {
		return isLatest;
	}

	public void setIslatest(boolean islatest) {
		this.isLatest = islatest;
	}

	@Override
	public String toString() {
		return "KPISummaryDetail [id=" + id + ", band=" + band + ", datasource=" + datasource + ", date=" + date
				+ ", env=" + env + ", kpiname=" + kpiname + ", sum=" + sum + ", sampleCount0=" + sampleCount0
				+ ", sampleCount1=" + sampleCount1 + ", range0=" + range0 + ", range1=" + range1 + ", range2=" + range2
				+ ", isLatest=" + isLatest + ", module=" + module + ", geographyL4=" + geographyL4 + ", technology="
				+ technology + ", operatorName=" + operatorName + "]";
	}


}
