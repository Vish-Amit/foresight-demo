package com.inn.foresight.module.nv.workorder.stealth.dao;

import java.util.Date;
import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.workorder.stealth.model.StealthTaskDetail;
import com.inn.foresight.module.nv.workorder.stealth.wrapper.StealthWOWrapper;

public interface IStealthTaskDetailDao  extends IGenericDao<Integer, StealthTaskDetail>{
List<StealthWOWrapper> getStealthWOWrapperListByWOId(Integer woId);
	/**
	 * Gets the devices by work order id.
	 *
	 * @param id the id
	 * @return the devices by work order id
	 * @throws DaoException the dao exception
	 */

	String ACKNOWLEDGEMENT="acknowledgement";
	String CREATION_TIME="creationTime";
	
	StealthTaskDetail getStealthTaskDetailByDeviceId(Integer deviceId, Date date);

	StealthTaskDetail getStealthTaskByDeviceAndWOId(Integer deviceId, Integer woId) throws Exception;


	List<Integer> getNVDeviceListByWOId(Integer woId);

	List<GenericWorkorder> getWOListByDeviceIdAndModificationtime(String deviceId,Long modificationTime,Integer userId);

    List<StealthTaskDetail>getStealthTasksByWorkorderId(Integer workorderId);

	List<StealthWOWrapper> getAcknowledgementSummary(Integer woId,String type);
}
