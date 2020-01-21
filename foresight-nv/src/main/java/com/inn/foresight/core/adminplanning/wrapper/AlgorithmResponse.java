package com.inn.foresight.core.adminplanning.wrapper;

import java.io.Serializable;

import com.inn.core.generic.wrapper.JpaWrapper;
import com.inn.core.generic.wrapper.RestWrapper;

@JpaWrapper
@RestWrapper
public class AlgorithmResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4893135196670664148L;

	private Integer id;

	private String type;

	private String name;

	/**
	 * Instantiates a new algorithm response for existing exception.
	 *
	 * @param id the id
	 * @param type the type
	 * @param name the name
	 */
	public AlgorithmResponse(Integer id, String type, String name) {
		this.id = id;
		this.name = name;
		this.type = type;
	}

	/*public AlgorithmResponse(GeographyL1 l1, GeographyL2 l2, GeographyL3 l3, GeographyL4 l4, OtherGeography otherGeo) {
		if (l1 != null) {
			this.id = l1.getId();
			this.name = l1.getName();
			this.type = AlgorithmPlanningConstant.L1;
		}
		if (l2 != null) {
			this.id = l2.getId();
			this.name = l2.getName();
			this.type = AlgorithmPlanningConstant.L2;
		}
		if (l3 != null) {
			this.id = l3.getId();
			this.name = l3.getName();
			this.type = AlgorithmPlanningConstant.L3;
		}
		if (l4 != null) {
			this.id = l4.getId();
			this.name = l4.getName();
			this.type = AlgorithmPlanningConstant.L4;
		}
		if (otherGeo != null) {
			this.id = otherGeo.getId();
			this.name = otherGeo.getName();
			this.type = AlgorithmPlanningConstant.OTHER;
		}
	}*/

	/**
	 * Instantiates a new algorithm response.
	 */
	public AlgorithmResponse() {

	}

	public Integer getId() {
		return id;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "AlgorithmResponse [id=" + id + ", type=" + type + ", name=" + name + "]";
	}

}
