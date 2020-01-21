package com.inn.foresight.core.livy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class Session.
 *
 * @author Harsh Pandya
 * 
 * @author Zafar
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Session {

	/** The id. */
	private String id;

	/** The state. */
	private String state;

	/** The kind. */
	private String kind;

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

	/**
	 * Gets the kind.
	 *
	 * @return the kind
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * Sets the kind.
	 *
	 * @param kind the new kind
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Session [id=" + id + ", state=" + state + ", kind=" + kind + "]";
	}

}
