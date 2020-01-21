package com.inn.foresight.core.ztepower.dao;

import java.util.List;
import java.util.Map;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.ztepower.model.ZTEPowerRegion;
import com.inn.foresight.core.ztepower.wrapper.ZTEPowerRequestWrapper;
import com.inn.foresight.core.ztepower.wrapper.ZTEPowerResultWrapper;

public interface IZTEPowerRegionDao extends IGenericDao<Integer, ZTEPowerRegion> {
	public List<ZTEPowerResultWrapper> getZTEPowerAreaWiseCount(ZTEPowerRequestWrapper wrapper);

	public List<ZTEPowerResultWrapper> getZTEPowerStationWiseCount(ZTEPowerRequestWrapper wrapper);

	public List<ZTEPowerResultWrapper> getZTEPowerDeviceWiseCount(ZTEPowerRequestWrapper wrapper);

	public Map<String, Object> getZTEPowerData(ZTEPowerRequestWrapper wrapper);

	public List<String> searchZTEPowerFields(ZTEPowerRequestWrapper wrapper);
}

