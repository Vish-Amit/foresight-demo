package com.inn.foresight.module.nv.profile.wrapper;

import java.io.Serializable;
import java.util.List;


/**
 * The Class NVProfileTemplate.
 *
 * @author innoeye
 * date - 26-Feb-2018 6:48:18 PM
 */
public class NVProfile implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The module. */
	private String module;
	
	/** The passive enabled. */
	private Boolean passiveEnabled;
	
	/** The kpis. */
	private List<String> kpis;
	
	/** The event. */
	private List<String> event;
	
	/** The capturing frequency. */
	private Integer capturingFrequency;
	
	/** The Signal threshold count. */
	private Integer signalThresholdCount;
	
	/** The Signal threshold value. */
	private Integer signalThresholdValue;
	
	/** The Operator. */
	private String operator;
	
	/** The Technology. */
	private String technology;
	
	/** The call event count. */
	private Integer callEventCount;
	
	private boolean syncOnWiFi;
	private Integer daysForSync;

	
	/**
	 * Gets the module.
	 *
	 * @return the module
	 */
	public String getModule() {
		return module;
	}

	
	/**
	 * Sets the module.
	 *
	 * @param module the new module
	 */
	public void setModule(String module) {
		this.module = module;
	}

	
	/**
	 * Checks if is passive enabled.
	 *
	 * @return true, if is passive enabled
	 */
	public Boolean isPassiveEnabled() {
		return passiveEnabled;
	}

	
	/**
	 * Sets the passive enabled.
	 *
	 * @param passiveEnabled the new passive enabled
	 */
	public void setPassiveEnabled(Boolean passiveEnabled) {
		this.passiveEnabled = passiveEnabled;
	}

	
	/**
	 * Gets the kpis.
	 *
	 * @return the kpis
	 */
	public List<String> getKpis() {
		return kpis;
	}

	
	/**
	 * Sets the kpis.
	 *
	 * @param kpis the new kpis
	 */
	public void setKpis(List<String> kpis) {
		this.kpis = kpis;
	}

	
	/**
	 * Gets the event.
	 *
	 * @return the event
	 */
	public List<String> getEvent() {
		return event;
	}

	
	/**
	 * Sets the event.
	 *
	 * @param event the new event
	 */
	public void setEvent(List<String> event) {
		this.event = event;
	}

	
	/**
	 * Gets the capturing frequency.
	 *
	 * @return the capturing frequency
	 */
	public Integer getCapturingFrequency() {
		return capturingFrequency;
	}

	
	/**
	 * Sets the capturing frequency.
	 *
	 * @param capturingFrequency the new capturing frequency
	 */
	public void setCapturingFrequency(Integer capturingFrequency) {
		this.capturingFrequency = capturingFrequency;
	}

	
	/**
	 * Gets the signal threshold count.
	 *
	 * @return the signal threshold count
	 */
	public Integer getSignalThresholdCount() {
		return signalThresholdCount;
	}

	
	/**
	 * Sets the signal threshold count.
	 *
	 * @param signalThresholdCount the new signal threshold count
	 */
	public void setSignalThresholdCount(Integer signalThresholdCount) {
		this.signalThresholdCount = signalThresholdCount;
	}

	
	/**
	 * Gets the signal threshold value.
	 *
	 * @return the signal threshold value
	 */
	public Integer getSignalThresholdValue() {
		return signalThresholdValue;
	}

	
	/**
	 * Sets the signal threshold value.
	 *
	 * @param signalThresholdValue the new signal threshold value
	 */
	public void setSignalThresholdValue(Integer signalThresholdValue) {
		this.signalThresholdValue = signalThresholdValue;
	}

	
	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	
	/**
	 * Sets the operator.
	 *
	 * @param operator the new operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	
	/**
	 * Gets the technology.
	 *
	 * @return the technology
	 */
	public String getTechnology() {
		return technology;
	}

	
	/**
	 * Sets the technology.
	 *
	 * @param technology the new technology
	 */
	public void setTechnology(String technology) {
		this.technology = technology;
	}


	
	/**
	 * Gets the call event count.
	 *
	 * @return the call event count
	 */
	public Integer getCallEventCount() {
		return callEventCount;
	}


	
	/**
	 * Sets the call event count.
	 *
	 * @param callEventCount the new call event count
	 */
	public void setCallEventCount(Integer callEventCount) {
		this.callEventCount = callEventCount;
	}


	/**
	 * @return the syncOnWiFi
	 */
	public boolean isSyncOnWiFi() {
		return syncOnWiFi;
	}


	/**
	 * @param syncOnWiFi the syncOnWiFi to set
	 */
	public void setSyncOnWiFi(boolean syncOnWiFi) {
		this.syncOnWiFi = syncOnWiFi;
	}


	/**
	 * @return the daysForSync
	 */
	public Integer getDaysForSync() {
		return daysForSync;
	}


	/**
	 * @param daysForSync the daysForSync to set
	 */
	public void setDaysForSync(Integer daysForSync) {
		this.daysForSync = daysForSync;
	}


	@Override
	public String toString() {
		return "NVProfile [module=" + module + ", passiveEnabled=" + passiveEnabled + ", kpis=" + kpis + ", event="
				+ event + ", capturingFrequency=" + capturingFrequency + ", signalThresholdCount="
				+ signalThresholdCount + ", signalThresholdValue=" + signalThresholdValue + ", operator=" + operator
				+ ", technology=" + technology + ", callEventCount=" + callEventCount + ", syncOnWiFi=" + syncOnWiFi
				+ ", daysForSync=" + daysForSync + "]";
	}

}
