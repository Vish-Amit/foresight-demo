package com.inn.foresight.module.nv.core.workorder.service;

import java.util.List;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;

/** The Interface IGenericWorkorderService. */
public interface IGenericWorkorderService  extends IGenericService<Integer, GenericWorkorder>{

	/**
	 * Creates the workorder.
	 *
	 * @param workorder the workorder
	 * @return the generic workorder
	 * @throws RestException the rest exception
	 */
	GenericWorkorder createWorkorder(GenericWorkorder workorder);

	/**
	 * Gets the workorder name list by WO name.
	 *
	 * @param workorderName the workorder name
	 * @return the workorder name list by WO name
	 * @throws RestException the rest exception
	 */
	List<String> getWorkorderIdListByWOId(String workorderName);

	/**
	 * Gets the workorder by WO name.
	 *
	 * @param workorderName the workorder name
	 * @return the workorder by WO name
	 * @throws RestException the rest exception
	 */
	GenericWorkorder getWorkorderByWOId(String workorderName);


}