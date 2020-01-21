package com.inn.foresight.core.generic.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OperatorDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5223342666426956553L;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "operatordetailid_pk")
	private Integer id;

	@Column(name = "mcc")
	Integer mcc;

	@Column(name = "mnc")
	Integer mnc;

	@Column(name = "operator")
	String operator;

	@Column(name = "country")
	String country;

	@Column(name = "iso")
	String iso;

	@Column(name = "countrycode")
	String countryCode;

	@Column(name = "module")
	String module;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMcc() {
		return mcc;
	}

	public void setMcc(Integer mcc) {
		this.mcc = mcc;
	}

	public Integer getMnc() {
		return mnc;
	}

	public void setMnc(Integer mnc) {
		this.mnc = mnc;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIso() {
		return iso;
	}

	public void setIso(String iso) {
		this.iso = iso;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "OperatorDetail [id=" + id + ", mcc=" + mcc + ", mnc=" + mnc + ", operator=" + operator + ", country="
				+ country + ", iso=" + iso + ", countryCode=" + countryCode + ", module=" + module + "]";
	}
}
