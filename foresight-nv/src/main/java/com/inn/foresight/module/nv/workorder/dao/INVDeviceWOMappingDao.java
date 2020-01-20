package com.inn.foresight.module.nv.workorder.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.device.wrapper.NVDeviceDataWrapper;
import com.inn.foresight.module.nv.workorder.stealth.model.NVDeviceWOMapping;

public interface INVDeviceWOMappingDao extends IGenericDao<Integer, NVDeviceWOMapping> {

	/**
	 * Gets the device list for work order.
	 *
	 * @param date the date
	 * @param date2 the date 2
	 * @param filterAndValueMap the filter and value map
	 * @param lLimit the l limit
	 * @param uLimit the u limit
	 * @return the device list for work order
	 */
	List<NVDeviceDataWrapper> getDeviceListForWorkOrder(Date date, Date date2, Map<String, List> filterAndValueMap,
			Integer lLimit, Integer uLimit);

	/**
	 * Gets the device list count for work order.
	 *
	 * @param date the date
	 * @param date2 the date 2
	 * @param filterAndValueMap the filter and value map
	 * @return the device list count for work order
	 * @throws DaoException the dao exception
	 */
	Long getDeviceListCountForWorkOrder(Date date, Date date2, Map<String, List> filterAndValueMap);


}
