package com.inn.foresight.module.nv.dashboard.passive.model;

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
import org.hibernate.annotations.ParamDef;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.dashboard.passive.utils.NVPassiveConstants;

@NamedQuery(name = NVPassiveConstants.GET_MAKE_WISE_PASSIVE_DEVICE_COUNTS, query = "select new com.inn.foresight.module.nv.dashboard.passive.wrapper.NVPassiveDeviceWrapper(d.make, d.make, sum(d.sampleCount)) from NVPassiveDevice d  where d.make in (:make) group by d.make")

@NamedQuery(name = NVPassiveConstants.GET_MAKE_MODEL_WISE_PASSIVE_DEVICE_COUNTS, query = "select new com.inn.foresight.module.nv.dashboard.passive.wrapper.NVPassiveDeviceWrapper(d.make, concat(d.make,' ',d.model), d.model , sum(d.sampleCount)) from NVPassiveDevice d  where d.make in (:make) group by d.make, d.model")

@NamedQuery(name = NVPassiveConstants.GET_MAKE_MODEL_OS_WISE_PASSIVE_DEVICE_COUNTS, query = "select new com.inn.foresight.module.nv.dashboard.passive.wrapper.NVPassiveDeviceWrapper(concat(d.make,' ',d.model), concat(d.make,' ',d.model,' ',d.os), d.os , sum(d.sampleCount)) from NVPassiveDevice d  where d.make in (:make) group by d.make, d.model, d.os")

@NamedQuery(name = NVPassiveConstants.GET_TOP5_MAKE, query = "select d.make from NVPassiveDevice d  group by d.make order by sum(d.sampleCount) desc")

@FilterDef(name = NVPassiveConstants.RECORD_TYPE_SAMPLE_DATE_AND_SOURCE_FILTER_ALL, parameters = {
		@ParamDef(name = NVPassiveConstants.RECORD_TIME, type = "java.lang.String"),
		@ParamDef(name = NVPassiveConstants.RECORD_TYPE, type = "java.lang.String"),
		@ParamDef(name = NVPassiveConstants.SOURCE, type = "java.lang.String") })

@FilterDef(name = NVPassiveConstants.RECORD_TYPE_SAMPLE_DATE_AND_SOURCE_FILTER_L4, parameters = {
		@ParamDef(name = NVPassiveConstants.RECORD_TIME, type = "java.lang.String"),
		@ParamDef(name = NVPassiveConstants.RECORD_TYPE, type = "java.lang.String"),
		@ParamDef(name = NVPassiveConstants.GEOGRAPHY_ID, type = "java.lang.Integer"),
		@ParamDef(name = NVPassiveConstants.SOURCE, type = "java.lang.String") })

@FilterDef(name = NVPassiveConstants.RECORD_TYPE_SAMPLE_DATE_AND_SOURCE_FILTER_L3, parameters = {
		@ParamDef(name = NVPassiveConstants.RECORD_TIME, type = "java.lang.String"),
		@ParamDef(name = NVPassiveConstants.RECORD_TYPE, type = "java.lang.String"),
		@ParamDef(name = NVPassiveConstants.GEOGRAPHY_ID, type = "java.lang.Integer"),
		@ParamDef(name = NVPassiveConstants.SOURCE, type = "java.lang.String") })

@FilterDef(name = NVPassiveConstants.RECORD_TYPE_SAMPLE_DATE_AND_SOURCE_FILTER_L2, parameters = {
		@ParamDef(name = NVPassiveConstants.RECORD_TIME, type = "java.lang.String"),
		@ParamDef(name = NVPassiveConstants.RECORD_TYPE, type = "java.lang.String"),
		@ParamDef(name = NVPassiveConstants.GEOGRAPHY_ID, type = "java.lang.Integer"),
		@ParamDef(name = NVPassiveConstants.SOURCE, type = "java.lang.String") })

@FilterDef(name = NVPassiveConstants.RECORD_TYPE_SAMPLE_DATE_AND_SOURCE_FILTER_L1, parameters = {
		@ParamDef(name = NVPassiveConstants.RECORD_TIME, type = "java.lang.String"),
		@ParamDef(name = NVPassiveConstants.RECORD_TYPE, type = "java.lang.String"),
		@ParamDef(name = NVPassiveConstants.GEOGRAPHY_ID, type = "java.lang.Integer"),
		@ParamDef(name = NVPassiveConstants.SOURCE, type = "java.lang.String") })

@Filter(name = NVPassiveConstants.RECORD_TYPE_SAMPLE_DATE_AND_SOURCE_FILTER_ALL, condition = "nvpassivedashboardid_fk in (SELECT n.nvpassivedashboardid_pk from NVPassiveDashboard n where DATE_FORMAT(n.sampleDate,'%d%m%y') = :recordtime and n.recordType= :recordtype and n.geographyL4id_fk is null and n.source = :source)")

@Filter(name = NVPassiveConstants.RECORD_TYPE_SAMPLE_DATE_AND_SOURCE_FILTER_L4, condition = "nvpassivedashboardid_fk in (SELECT n.nvpassivedashboardid_pk from NVPassiveDashboard n where DATE_FORMAT(n.sampleDate,'%d%m%y') = :recordtime and n.recordType= :recordtype and n.geographyL4id_fk = :geographyId and n.source = :source)")

@Filter(name = NVPassiveConstants.RECORD_TYPE_SAMPLE_DATE_AND_SOURCE_FILTER_L3, condition = "nvpassivedashboardid_fk in (SELECT n.nvpassivedashboardid_pk from NVPassiveDashboard n where DATE_FORMAT(n.sampleDate,'%d%m%y') = :recordtime and n.recordType= :recordtype and n.geographyL4id_fk in (select gl4.geographyl4id_pk from GeographyL4 gl4 where gl4.geographyl3id_fk =:geographyId) and n.source = :source)")

@Filter(name = NVPassiveConstants.RECORD_TYPE_SAMPLE_DATE_AND_SOURCE_FILTER_L2, condition = "nvpassivedashboardid_fk in (SELECT n.nvpassivedashboardid_pk from NVPassiveDashboard n where DATE_FORMAT(n.sampleDate,'%d%m%y') = :recordtime and n.recordType= :recordtype and n.geographyL4id_fk in (select gl4.geographyl4id_pk from GeographyL4 gl4 where gl4.geographyl3id_fk in (select gl3.geographyl3id_pk from GeographyL3 gl3 where gl3.geographyl2id_fk =:geographyId)) and n.source = :source)")

@Filter(name = NVPassiveConstants.RECORD_TYPE_SAMPLE_DATE_AND_SOURCE_FILTER_L1, condition = "nvpassivedashboardid_fk in (SELECT n.nvpassivedashboardid_pk from NVPassiveDashboard n where DATE_FORMAT(n.sampleDate,'%d%m%y') = :recordtime and n.recordType= :recordtype and n.geographyL4id_fk in (select gl4.geographyl4id_pk from GeographyL4 gl4 where gl4.geographyl3id_fk in (select gl3.geographyl3id_pk from GeographyL3 gl3 where gl3.geographyl2id_fk in (select gl2.geographyLl2id_pk from GeographyL2 gl2 where gl2.geographyl1id_fk =:geographyId))) and n.source = :source)")

/** The Class NVPassiveDevice. */
@Entity
@Table(name = "NVPassiveDevice")
@XmlRootElement(name = "NVPassiveDevice")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NVPassiveDevice {

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "nvpassivedeviceid_pk")
	private Integer id;

	/** The passive dashboard. */
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "nvpassivedashboardid_fk", nullable = false)
	private NVPassiveDashboard passiveDashboard;

	/** The make. */
	@Basic
	@Column(name = "make")
	private String make;

	/** The model. */
	@Basic
	@Column(name = "model")
	private String model;

	/** The os. */
	@Basic
	@Column(name = "os")
	private String os;

	/** The sample count. */
	@Basic
	@Column(name = "samplecount")
	private Long sampleCount;

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
	 * Gets the passive dashboard.
	 *
	 * @return the passive dashboard
	 */
	public NVPassiveDashboard getPassiveDashboard() {
		return passiveDashboard;
	}

	/**
	 * Sets the passive dashboard.
	 *
	 * @param passiveDashboard
	 *            the new passive dashboard
	 */
	public void setPassiveDashboard(NVPassiveDashboard passiveDashboard) {
		this.passiveDashboard = passiveDashboard;
	}

	/**
	 * Gets the make.
	 *
	 * @return the make
	 */
	public String getMake() {
		return make;
	}

	/**
	 * Sets the make.
	 *
	 * @param make
	 *            the new make
	 */
	public void setMake(String make) {
		this.make = make;
	}

	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets the model.
	 *
	 * @param model
	 *            the new model
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Gets the os.
	 *
	 * @return the os
	 */
	public String getOs() {
		return os;
	}

	/**
	 * Sets the os.
	 *
	 * @param os
	 *            the new os
	 */
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * Gets the sample count.
	 *
	 * @return the sample count
	 */
	public Long getSampleCount() {
		return sampleCount;
	}

	/**
	 * Sets the sample count.
	 *
	 * @param sampleCount
	 *            the new sample count
	 */
	public void setSampleCount(Long sampleCount) {
		this.sampleCount = sampleCount;
	}

	@Override
	public String toString() {
		return "NVPassiveDevice [id=" + id + ", passiveDashboard=" + passiveDashboard + ", make=" + make + ", model="
				+ model + ", os=" + os + ", sampleCount=" + sampleCount + "]";
	}

}
