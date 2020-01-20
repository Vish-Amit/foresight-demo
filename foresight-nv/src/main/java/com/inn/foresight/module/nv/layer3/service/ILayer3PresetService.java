package com.inn.foresight.module.nv.layer3.service;

//public class ILayer3PresetService {
//}


import java.util.List;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.module.nv.layer3.model.Layer3Preset;
import com.inn.product.um.user.model.User;

/** The Interface ILayer3PresetService. */
public interface ILayer3PresetService  extends IGenericService<Integer, Layer3Preset> {

	/**
	 * Creates the layer3preset.
	 *
	 * @param layer3Preset
	 *            the  layer3Preset
	 * @param user 
	 * @return the layer3Preset
	 * @throws RestException
	 *             the rest exception
	 */
	String createNVLayer3Preset(Layer3Preset layer3Preset, User user);
	
	/**
	 * Gets the Layer3Preset data by primary key.
	 *
	 * @return the all Layer3PresetDao  data
	 * @throws RestException the rest exception
	 */
	String getLayer3PresetDataByPK(int id);

	List<Layer3Preset> getLayer3PresetByUserId(int id, String presetId);

	
}

