package com.inn.foresight.core.infra.model;

import java.io.Serializable;

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

/**
 * The Class NELocationParameter.
 */

@NamedQuery(name = "getTotalFloorByNeLocationId", query = "select distinct nelp.value from NELocationParameter nelp where nelp.nelocation.id=:nelocationId and nelp.parameterName='totalFloor'")
@NamedQuery(name = "getParameterAndKeyByNeLocationId", query = "select nelp.parameterName,nelp.value from NELocationParameter nelp where nelp.nelocation.id=:nelocationId")
@NamedQuery(name="getNELocationParamByNELId",query="select nelp from NELocationParameter nelp where nelp.nelocation.id =: nelId")
@NamedQuery(name="getAllGCList",query="select nelp2.value,nelp.nelocation.nelId,nelp.nelocation.technology,nelp.nelocation.vendor,nelp.nelocation.domain,nelp.nelocation.address,nelp.nelocation.pincode,nelp.nelocation.locationCode,nelp.nelocation.friendlyName,nelp.nelocation.id,nelp.value,gl4.name from NELocationParameter nelp left join nelp.nelocation.geographyL4 gl4 join NELocationParameter nelp2 on nelp.nelocation.id=nelp2.nelocation.id where nelp.nelocation.nelType='GC'and  nelp2.parameterName='gcType' order by nelp.nelocation.modifiedTime desc")
@NamedQuery(name="getAllGCTotalCount",query="select count(nelp.id) from NELocationParameter nelp join nelp.nelocation.geographyL4 gl4 left join NELocationParameter nelp2 on nelp.nelocation.id=nelp2.nelocation.id where nelp.nelocation.nelType='GC' and  nelp2.parameterName='gcType'")
@NamedQuery(name="getParameterValue",query="select nelp.value from NELocationParameter nelp where nelp.nelocation.id =:locationId and nelp.parameterName=:paramName")
@NamedQuery(name="getNELocationParamByParameter",query="select nelp from NELocationParameter nelp where nelp.nelocation.id =:locationId and nelp.parameterName=:paramName")
@NamedQuery(name="getNELocationByParameter",query="select nelp.nelocation from NELocationParameter nelp where nelp.parameterName=:paramName and nelp.value=:paramValue and nelp.nelocation.nelType=:nelType")
@NamedQuery(name="getValuesByParametersAndNELocationID",query="select nlp from NELocationParameter nlp where nlp.nelocation.id=:pk and nlp.parameterName in (:parameterList)")
@NamedQuery(name="getNELocationParameterByParameterList",query="select nlp from NELocationParameter nlp where nlp.nelocation.id=:pk and nlp.parameterName in (:parameterList)")
@XmlRootElement(name = "NELocationParameter")
@Entity
@Table(name = "NELocationParameter")
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class NELocationParameter implements Serializable {
  
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7716188767441692595L;

	/** The id. */
	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "nelocationparameterid_pk")
	private Integer id;

	@Basic
	@Column(name = "parametername")
	private String parameterName;
	
	@Basic
	@Column(name = "value")
	private String value;
	
	@Basic
	@Column(name = "type")
	private String type;
	
	/** The geography L 4. */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nelocationid_fk")
	private NELocation nelocation;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public NELocation getNelocation() {
		return nelocation;
	}

	public void setNelocation(NELocation nelocation) {
		this.nelocation = nelocation;
	}

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NELocationParameter [id=");
        builder.append(id);
        builder.append(", parameterName=");
        builder.append(parameterName);
        builder.append(", value=");
        builder.append(value);
        builder.append(", type=");
        builder.append(type);
        builder.append(", nelocation=");
        builder.append(nelocation);
        builder.append("]");
        return builder.toString();
    }
}
