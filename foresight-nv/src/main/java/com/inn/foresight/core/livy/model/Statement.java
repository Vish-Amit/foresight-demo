package com.inn.foresight.core.livy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The Class Statement.
 *
 * @author Harsh Pandya
 * 
 * @author Zafar
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Statement {
	
	/** The id. */
	private String id;
	
	/** The code. */
	private String code;
	
	/** The output. */
	private StatementOutput output;
	
	/** The state. */
	private String state;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Sets the code.
	 *
	 * @param code the new code
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * Gets the output.
	 *
	 * @return the output
	 */
	public StatementOutput getOutput() {
		return output;
	}
	
	/**
	 * Sets the output.
	 *
	 * @param output the new output
	 */
	public void setOutput(StatementOutput output) {
		this.output = output;
	}
	
	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	
	/**
	 * Sets the state.
	 *
	 * @param state the new state
	 */
	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "Statement [id=" + id + ", code=" + code + ", output=" + output + ", state=" + state + "]";
	}
}