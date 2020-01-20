package com.inn.foresight.module.nv.layer3.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.layer3.model.Layer3MetaData;
import com.inn.foresight.module.nv.layer3.wrapper.Layer3PPEWrapper;

public interface Layer3MetaDataDao extends IGenericDao<Integer, Layer3MetaData> {
	void updateLayer3MetaData(Layer3MetaData layer3MetaData);
	List<Layer3MetaData> getLayer3MetaData(Layer3PPEWrapper layer3ppeWrapper);
	void deleteLayer3MetaData(Layer3MetaData layer3MetaData);
}
