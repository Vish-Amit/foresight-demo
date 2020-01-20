package com.inn.foresight.module.nv.workorder.stealth.kpi.dao;

import com.inn.foresight.module.nv.workorder.stealth.kpi.wrapper.ProbeDetailWrapper;

public interface ProbeDetailHbaseDao {
	
	
	String persistDataForProbe(ProbeDetailWrapper wrapper);

}
