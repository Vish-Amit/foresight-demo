package com.inn.foresight.module.nv.device.dao;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.device.model.Device;

/** The Interface IDeviceDao. */
public interface IDeviceDao  extends IGenericDao<Integer, Device>{
    
    /**
     * Gets the device specification by model.
     *
     * @param model the model
     * @return the device specification by model
     */
    Device getDeviceSpecificationByModel(String model);
    
    /**
     * Gets the all device list.
     *
     * @return the all device list
     */
    List<Device> getAllDeviceList();
    
    /**
     * Gets the map of model and device.
     *
     * @return the map of model and device
     */
    Map<String, Device> getMapOfModelAndDevice();

	Device getDeviceDetailByDeviceName(String modelName);

}
