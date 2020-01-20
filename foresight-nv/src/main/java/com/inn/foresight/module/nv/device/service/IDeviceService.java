package com.inn.foresight.module.nv.device.service;

import java.util.Map;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.module.nv.device.model.Device;

/** The Interface IDeviceService. */
public interface IDeviceService  extends IGenericService<Integer, Device> {
    
    /**
     * Gets the device specification by model.
     *
     * @param model the model
     * @return the device specification by model
     */
    Device getDeviceSpecificationByModel(String model);
    
    /**
     * Gets the map of model and device.
     *
     * @return the map of model and device
     */
    Map<String, Device> getMapOfModelAndDevice();

    /**
      * Create Device model Data from GSM Arena
      * 
      */
    public void createAndUpdateDeviceData();
}
