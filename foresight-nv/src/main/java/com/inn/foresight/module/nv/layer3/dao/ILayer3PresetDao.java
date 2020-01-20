package com.inn.foresight.module.nv.layer3.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.layer3.model.Layer3Preset;

public interface ILayer3PresetDao extends IGenericDao<Integer, Layer3Preset> {

	
	
	/**
	 * Create  Layer3 Preset Data.
	 * 
	 * @param Layer3Preset
	 *              layer3PresetData
	 * @return the Layer3 Preset Data
	 * @throws DaoException
	 *             the dao exception
	 */
	Layer3Preset createLayer3Preset(Layer3Preset layer3PresetData);
		
	
	/**
	 * Gets the get All Layer3 Preset Data By Preset primary key.
	 *
	 * @param id
	 *           the Id
	 * @return the All Layer3 Preset Data List
	 * @throws DaoException
	 *              the dao exception
	 */
	String getLayer3PresetDataByPK(Integer id);

	List<Layer3Preset> getLayer3PresetByUserId(Integer userid, String presetId);
	

}
