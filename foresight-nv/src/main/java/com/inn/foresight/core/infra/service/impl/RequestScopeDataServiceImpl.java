package com.inn.foresight.core.infra.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.service.IRequestScopeDataService;

/**
 * The Class RequestScopeDataServiceImpl.
 */
@Service("RequestScopeDataServiceImpl")
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestScopeDataServiceImpl implements IRequestScopeDataService {

	/** The exception list. */
	private Map<String, Throwable> exceptionList;
	
	/** The logger. */
	private Logger logger = LogManager.getLogger(RequestScopeDataServiceImpl.class);

	/**
	 * Gets the exception list.
	 *
	 * @return the exception list
	 */
	@Override
	public Map<String, Throwable> getExceptionList() {
		return exceptionList;
	}

	/**
	 * Sets the exception list.
	 *
	 * @param exceptionList the exception list
	 */
	@Override
	public void setExceptionList(Map<String, Throwable> exceptionList) {
		logger.info("Inside  RequestScopeDataServiceImpl @Method setExceptionInList exceptionList  {} ", exceptionList);
		this.exceptionList = exceptionList;
		logger.info("Inside RequestScopeDataServiceImpl @Method setExceptionInList this.exceptionList  {} ", this.exceptionList);
	}

	/**
	 * Sets the exception in list.
	 *
	 * @param code the code
	 * @param exception the exception
	 */
	@Override
	public void setExceptionInList(String code, Throwable exception) {
		logger.info("Inside RequestScopeDataServiceImpl @Method setExceptionInList code  {} ", code + " exception  {} ", exception);
		if (this.exceptionList == null) {
			this.exceptionList = new HashMap<>();
		}
		exceptionList.put(code, exception);
		logger.info("Inside RequestScopeDataServiceImpl @Method setExceptionInList code exceptionList  {} ", this.exceptionList);
	}

	/**
	 * Sets the exception in list.
	 *
	 * @param code the code
	 * @param exception the exception
	 * @param row the row
	 */
	@Override
	public void setExceptionInList(String code, Throwable exception, Integer row) {
		logger.info("Inside RequestScopeDataServiceImpl @Method setExceptionInList code  {} ", code + " exception  {} ", exception + " row {}", row);
		if (this.exceptionList == null) {
			this.exceptionList = new HashMap<>();
		}
		exceptionList.put(code + ForesightConstants.COMMA + row, exception);
		logger.info("Inside RequestScopeDataServiceImpl @Method setExceptionInList code row exceptionList " + this.exceptionList);
	}

	/**
	 * Sets the exception in list.
	 *
	 * @param exceptionList the exception list
	 */
	@Override
	public void setExceptionInList(Map<String, Throwable> exceptionList) {
		logger.info("Inside RequestScopeDataServiceImpl @Method setExceptionInList exceptionList {} " , exceptionList);
		this.exceptionList = exceptionList;
		logger.info("Inside RequestScopeDataServiceImpl @Method setExceptionInList @---this.exceptionList " + this.exceptionList);
	}

	/**
	 * Instantiates a new request scope data service impl.
	 */
	public RequestScopeDataServiceImpl() {
		logger.info("Inside RequestScopeDataServiceImpl Constructor ");
	}

	/**
	 * Initialize list.
	 */
	@Override
	public void InitializeList() {
		logger.info("Inside RequestScopeDataServiceImpl @Method InitializeList ");
		this.exceptionList = new LinkedHashMap();
		logger.info("Inside RequestScopeDataServiceImpl @Method InitializeList after ");
	}

}
