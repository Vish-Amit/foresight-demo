package com.inn.foresight.core.livy.model;


/**
 * The Class StatementOutput.
 * 
 * @author Zafar
 */
public class StatementOutput {
	
	/** The data. */
	private Object data;
	
	/** The execution count. */
	private String execution_count;
	
	/** The status. */
	private String status;
	
	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public Object getData() {
		return data;
	}
	
	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(Object data) {
		this.data = data;
	}
	
	/**
	 * Gets the execution count.
	 *
	 * @return the execution count
	 */
	public String getExecution_count() {
		return execution_count;
	}
	
	/**
	 * Sets the execution count.
	 *
	 * @param execution_count the new execution count
	 */
	public void setExecution_count(String execution_count) {
		this.execution_count = execution_count;
	}
	
	/**
	 * Gets the status.
	 *
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * Sets the status.
	 *
	 * @param status the new status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "StatementOutput [data=" + data + ", execution_count=" + execution_count + ", status=" + status + "]";
	}
}