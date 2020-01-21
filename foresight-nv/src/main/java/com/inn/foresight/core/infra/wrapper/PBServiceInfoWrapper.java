package com.inn.foresight.core.infra.wrapper;

import java.io.Serializable;
import java.util.List;

public class PBServiceInfoWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer count;
	private List<String> serviceData;

	public PBServiceInfoWrapper() {

	}

	public PBServiceInfoWrapper(Integer count, List<String> serviceData) {
		this.count = count;
		this.serviceData = serviceData;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<String> getServiceData() {
		return serviceData;
	}

	public void setServiceData(List<String> serviceData) {
		this.serviceData = serviceData;
	}

	@Override
	public String toString() {
		return "PBServiceInfoWrapper [count=" + count + ", serviceData=" + serviceData + "]";
	}
}
