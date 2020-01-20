package com.inn.foresight.module.nv.workorder.stealth.kpi.dao;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.module.nv.workorder.stealth.kpi.model.ProbeDetail;

public interface ProbeDetailDao extends  IGenericDao<Integer, ProbeDetail>{



	ProbeDetail findProbeDetailByKpi(Integer stealthTaskResultId,String kpi);
	
	
	
}
