package com.inn.foresight.core.ztepower.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.ztepower.model.ZTEPowerStation;

public interface IZTEPowerStationDao extends IGenericDao<Integer, ZTEPowerStation>{

	ZTEPowerStation getZTEPowerStationByStationId(Integer id);

	List<Integer> getNEIdListByStationId(Integer id);

}
