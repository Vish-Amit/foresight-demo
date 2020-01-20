package com.inn.foresight.module.nv.layer3.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.product.um.user.model.User;

/** The Class Layer3Preset. */
@NamedQueries({
	@NamedQuery(name = NVLayer3Constants.GET_LAYER3PRESET_BY_USERID, query = "select new com.inn.foresight.module.nv.layer3.model.Layer3Preset(d.id,d.presetName,d.kpiname,d.eventName,d.type,d.presetId)  from Layer3Preset d where d.user.userid=:userid and d.presetId =:presetId"),
	@NamedQuery(name = NVLayer3Constants.GET_LAYER3VISUALIZATION_BY_ID, query = "select d.layer3visualization  from Layer3Preset d where d.id=:id")

})
@Entity
@Table(name = "Layer3Preset")
@XmlRootElement(name = "Layer3Preset")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class Layer3Preset implements Serializable {



	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "layer3presetid_pk")
	private Integer id;


	/** The presetName. */
	@Basic
	
	@Column(name = "presetname")
	private String presetName;


	/** The KPI. */
	@Basic
	
	@Column(name = "kpiname")
	private String kpiname;


	/** The Event. */
	@Basic
	
	@Column(name = "eventname")
	private String eventName;

	/** The layer3visualization. */
	@Basic
	
	@Column(name = "layer3visualization")
	@Lob
	private String layer3visualization;

	/** The Userid_fk. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userid_fk", nullable = false)
	private User user;



	/** The Type of Preset. */
	@Basic
	@Column(name = "type")
	private String type;

	@Basic
	@Column(name = "presetid")
	private String presetId;

	@Transient
	private List<String> kpiNameList;

	@Transient
	private List<String> eventNameList;


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
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * Gets the presetName.
	 *
	 * @return the presetName
	 */
	public String getPresetName() {
		return presetName;
	}

	/**
	 * Sets the presetName.
	 *
	 * @param presetName the presetName to set
	 */
	public void setPresetName(String presetName) {
		this.presetName = presetName;
	}

	/**
	 * Gets the kpiname.
	 *
	 * @return the kpiname
	 */
	public String getKpiname() {
		return kpiname;
	}

	/**
	 * Sets the kpiname.
	 *
	 * @param kpiname the kpiname to set
	 */
	public void setKpiname(String kpiname) {
		this.kpiname = kpiname;
	}

	/**
	 * Gets the eventName.
	 *
	 * @return the eventName
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * Sets the eventName.
	 *
	 * @param eventName the eventName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}


	public String getLayer3visualization() {
		return layer3visualization;
	}

	public void setLayer3visualization(String layer3visualization) {
		this.layer3visualization = layer3visualization;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}




	public String getPresetId() {
		return presetId;
	}

	public void setPresetId(String presetId) {
		this.presetId = presetId;
	}

	/**
	 * Instantiates a new Layer3Preset model.
	 *
	 * @param Id
	 *            the layer3preset Id
	 * @param presetName
	 *            the preset Name
	 * @param kpiname
	 *            the kpi Name
	 * @param eventName
	 *            the event Name
	 * @param type
	 *            the type of preset
	 */
	public Layer3Preset(Integer id, String presetName, String kpiname, String eventName, String type, String presetId) {
		super();
		this.id = id;
		this.presetName = presetName;
		this.kpiname = kpiname;
		this.eventName = eventName;
		this.type = type;
		this.presetId = presetId;
	}

	public Layer3Preset() {
		super();
	}

	public List<String> getKpiNameList() {
		return kpiNameList;
	}

	public void setKpiNameList(List<String> kpiNameList) {
		this.kpiNameList = kpiNameList;
	}

	public List<String> getEventNameList() {
		return eventNameList;
	}

	public void setEventNameList(List<String> eventNameList) {
		this.eventNameList = eventNameList;
	}


}