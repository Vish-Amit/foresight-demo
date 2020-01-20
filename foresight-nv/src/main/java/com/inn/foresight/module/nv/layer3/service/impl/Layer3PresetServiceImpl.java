package com.inn.foresight.module.nv.layer3.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.module.nv.layer3.constants.NVLayer3Constants;
import com.inn.foresight.module.nv.layer3.dao.ILayer3PresetDao;
import com.inn.foresight.module.nv.layer3.model.Layer3Preset;
import com.inn.foresight.module.nv.layer3.service.ILayer3PresetService;
import com.inn.product.um.user.model.User;

@Service("Layer3PresetServiceImpl")
public class Layer3PresetServiceImpl extends AbstractService<Integer, Layer3Preset> implements ILayer3PresetService{
	
	/** The i Layer3Preset dao. */
	@Autowired
	private ILayer3PresetDao iLayer3PresetDao;

	/**
	 * Sets the dao.
	 *
	 * @param dao the new dao
	 */
	@Autowired
	public void setDao(ILayer3PresetDao dao) {
		super.setDao(dao);
		iLayer3PresetDao = dao;
	}

	
	/**
	 * Creates the layer3preset user wise.
	 *
	 * @param layer3Preset
	 *            object of layer3Preset
	 * @return the layer3Preset
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	@Transactional
	public String createNVLayer3Preset(Layer3Preset layer3Preset, User user) {
		try {
			layer3Preset.setUser(user);
			if(layer3Preset.getKpiNameList()!=null){
				layer3Preset.setKpiname(new Gson().toJson(layer3Preset.getKpiNameList()));
			}
			if(layer3Preset.getEventNameList()!=null){
				layer3Preset.setEventName(new Gson().toJson(layer3Preset.getEventNameList()));
			}
			
			iLayer3PresetDao.createLayer3Preset(layer3Preset);
			return NVLayer3Constants.SUCCESS_JSON;
		} catch (DaoException e) {
			throw new RestException(e.getMessage());
		}
	}
	
	/**
	 * Gets the all Layer3Preset data by user id.
	 *
	 * @return the all Layer3PresetDao  data
	 * @throws RestException the rest exception
	 */
	@Override
	public List<Layer3Preset> getLayer3PresetByUserId(int id,String presetId)  {
			try {
				List<Layer3Preset> presetRawList= iLayer3PresetDao.getLayer3PresetByUserId(id,presetId);
				return convertStringToListAndSetIntoWrapper(presetRawList);
			} catch (DaoException e) {
				throw new RestException(e.getMessage());
			}
	}
	
	
	private List<Layer3Preset> convertStringToListAndSetIntoWrapper(List<Layer3Preset> presetRawList) {
		if(presetRawList==null || presetRawList.isEmpty()){
			return presetRawList;
		}
		Gson gson=new Gson();
		TypeToken<List<String>> token = new TypeToken<List<String>>() {};
		for(Layer3Preset preset :presetRawList){
			setEventStringToList(gson, token, preset);
			setKpiNameToList(gson, token, preset);
		}
		return presetRawList;
	}


	private void setKpiNameToList(Gson gson, TypeToken<List<String>> token, Layer3Preset preset) {
		if(preset.getKpiname()!=null){
		List<String> kpiName = gson.fromJson(preset.getKpiname(), token.getType());
		preset.setKpiNameList(kpiName);
		preset.setKpiname(null);
		}
	}


	private void setEventStringToList(Gson gson, TypeToken<List<String>> token, Layer3Preset preset) {
		if(preset.getEventName()!=null){
		List<String> eventList = gson.fromJson(preset.getEventName(), token.getType());
		preset.setEventNameList(eventList);
		preset.setEventName(null);
		}
	}


	/**
	 * Gets the Layer3Preset json by primary key.
	 *
	 * @return the all Layer3PresetDao  data
	 * @throws RestException the rest exception
	 */
	@Override
	public String getLayer3PresetDataByPK(int id) {
		try {
			return iLayer3PresetDao.getLayer3PresetDataByPK(id);
		} catch (DaoException e) {
			throw new RestException(e.getMessage());
		}
	}
	
}

