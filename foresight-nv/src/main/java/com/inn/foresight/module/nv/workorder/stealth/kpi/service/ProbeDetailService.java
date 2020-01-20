package com.inn.foresight.module.nv.workorder.stealth.kpi.service;

import com.inn.foresight.module.nv.workorder.stealth.kpi.wrapper.ProbeDetailWrapper;

public interface ProbeDetailService {

	String updateEventOccurance(ProbeDetailWrapper wrapper);

	
	
	String persistDataIntoHbaseMicroService(ProbeDetailWrapper wrapper);


}
