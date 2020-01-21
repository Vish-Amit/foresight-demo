package com.inn.foresight.core.ztepower.service;

import java.util.List;
import java.util.Map;

import com.inn.foresight.core.ztepower.wrapper.ZTEPowerRequestWrapper;
import com.inn.foresight.core.ztepower.wrapper.ZTEPowerResultWrapper;


public interface IZTEPowerService {

	List<ZTEPowerResultWrapper> getZTEPowerSummaryCount(ZTEPowerRequestWrapper wrapper);

	Map<String, Object> getZTEPowerData(ZTEPowerRequestWrapper wrapper);

	List<String> searchZTEPowerFields(ZTEPowerRequestWrapper wrapper);

	String deleteZTEPowerData(ZTEPowerRequestWrapper wrapper);

	

}
