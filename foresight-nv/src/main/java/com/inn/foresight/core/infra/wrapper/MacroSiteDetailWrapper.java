package com.inn.foresight.core.infra.wrapper;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.inn.core.generic.wrapper.RestWrapper;
import com.inn.foresight.core.infra.utils.enums.Domain;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;
import com.inn.foresight.core.infra.utils.enums.Technology;
import com.inn.foresight.core.infra.utils.enums.Vendor;
@RestWrapper
@JsonInclude(Include.NON_NULL)
public class MacroSiteDetailWrapper {
	private List<NEType> neTypeList;
	private List<String> neNameList;
	private List<String> neFrequencyList;
	private List<NEStatus> neStatusList;
	private List<Vendor> vendorList;
	private List<Technology> technologyList;
	private List<Domain> domainList;
	private Map<String, List<String>> geographyNames;
	
	public List<NEType> getNeTypeList() {
		return neTypeList;
	}
	public void setNeTypeList(List<NEType> neTypeList) {
		this.neTypeList = neTypeList;
	}
	public List<String> getNeNameList() {
		return neNameList;
	}
	public void setNeNameList(List<String> neNameList) {
		this.neNameList = neNameList;
	}
	public List<String> getNeFrequencyList() {
		return neFrequencyList;
	}
	public void setNeFrequencyList(List<String> neFrequencyList) {
		this.neFrequencyList = neFrequencyList;
	}
	public List<NEStatus> getNeStatusList() {
		return neStatusList;
	}
	public void setNeStatusList(List<NEStatus> neStatusList) {
		this.neStatusList = neStatusList;
	}
	public List<Vendor> getVendorList() {
		return vendorList;
	}
	public void setVendorList(List<Vendor> vendorList) {
		this.vendorList = vendorList;
	}
	public List<Technology> getTechnologyList() {
		return technologyList;
	}
	public void setTechnologyList(List<Technology> technologyList) {
		this.technologyList = technologyList;
	}
	public List<Domain> getDomainList() {
		return domainList;
	}
	public void setDomainList(List<Domain> domainList) {
		this.domainList = domainList;
	}
	public Map<String, List<String>> getGeographyNames() {
		return geographyNames;
	}
	public void setGeographyNames(Map<String, List<String>> geographyNames) {
		this.geographyNames = geographyNames;
	}
	
}
