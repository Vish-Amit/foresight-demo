package com.inn.foresight.module.nv.profile.wrapper;

import java.io.Serializable;
import java.util.List;

import com.inn.core.generic.wrapper.RestWrapper;

/**
 * The Class NVProfileTemplate.
 *
 * @author innoeye
 * date - 26-Feb-2018 3:40:09 PM
 */
@RestWrapper
public class NVProfileTemplate implements Serializable{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The module. */
	private String module;
	
	/** The kpis. */
	private List<String> kpis;
	
	/** The event. */
	private List<String> event;
	
	/** The capturing frequency. */
	private List<Integer> capturingFrequency;
	
	/** The Signal threshold count. */
	private List<Integer> signalThresholdCount;
	
	/** The Signal threshold value. */
	private List<Integer> signalThresholdValue;
	
	/** The Operator. */
	private List<String> operator;
	
	/** The Technology. */
	private List<String> technology;
	
	/** The call event count. */
	private List<Integer> callEventCount;
	
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
	public List<Integer> getCapturingFrequency() {
		return capturingFrequency;
	}

	
	/**
	 * Sets the capturing frequency.
	 *
	 * @param capturingFrequency the new capturing frequency
	 */
	public void setCapturingFrequency(List<Integer> capturingFrequency) {
		this.capturingFrequency = capturingFrequency;
	}

	
	/**
	 * Gets the signal threshold count.
	 *
	 * @return the signal threshold count
	 */
	public List<Integer> getSignalThresholdCount() {
		return signalThresholdCount;
	}

	
	/**
	 * Sets the signal threshold count.
	 *
	 * @param signalThresholdCount the new signal threshold count
	 */
	public void setSignalThresholdCount(List<Integer> signalThresholdCount) {
		this.signalThresholdCount = signalThresholdCount;
	}

	
	/**
	 * Gets the signal threshold value.
	 *
	 * @return the signal threshold value
	 */
	public List<Integer> getSignalThresholdValue() {
		return signalThresholdValue;
	}

	
	/**
	 * Sets the signal threshold value.
	 *
	 * @param signalThresholdValue the new signal threshold value
	 */
	public void setSignalThresholdValue(List<Integer> signalThresholdValue) {
		this.signalThresholdValue = signalThresholdValue;
	}

	
	/**
	 * Gets the operator.
	 *
	 * @return the operator
	 */
	public List<String> getOperator() {
		return operator;
	}

	
	/**
	 * Sets the operator.
	 *
	 * @param operator the new operator
	 */
	public void setOperator(List<String> operator) {
		this.operator = operator;
	}

	
	/**
	 * Gets the technology.
	 *
	 * @return the technology
	 */
	public List<String> getTechnology() {
		return technology;
	}

	
	/**
	 * Sets the technology.
	 *
	 * @param technology the new technology
	 */
	public void setTechnology(List<String> technology) {
		this.technology = technology;
	}


	
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
	 * Gets the call event count.
	 *
	 * @return the call event count
	 */
	public List<Integer> getCallEventCount() {
		return callEventCount;
	}


	
	/**
	 * Sets the call event count.
	 *
	 * @param callEventCount the new call event count
	 */
	public void setCallEventCount(List<Integer> callEventCount) {
		this.callEventCount = callEventCount;
	}


	@Override
	public String toString() {
		return "NVProfileTemplate [module=" + module + ", kpis=" + kpis + ", event=" + event + ", capturingFrequency="
				+ capturingFrequency + ", signalThresholdCount=" + signalThresholdCount + ", signalThresholdValue="
				+ signalThresholdValue + ", operator=" + operator + ", technology=" + technology + ", callEventCount="
				+ callEventCount + "]";
	}
	
}
