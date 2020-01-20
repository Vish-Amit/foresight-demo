package com.inn.foresight.module.nv.workorder.stealth.kpi.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.module.nv.workorder.stealth.constants.StealthConstants;
import com.inn.foresight.module.nv.workorder.stealth.kpi.dao.ProbeDetailDao;
import com.inn.foresight.module.nv.workorder.stealth.kpi.model.ProbeDetail;
import com.inn.foresight.module.nv.workorder.stealth.kpi.wrapper.ProbeDetailWrapper;

@Repository("ProbeDetailDaoImpl")
public class ProbeDetailDaoImpl extends HibernateGenericDao<Integer, ProbeDetail> implements ProbeDetailDao  {

	public ProbeDetailDaoImpl() {
		super(ProbeDetail.class);
	}


	@Override
	public ProbeDetail findProbeDetailByKpi(Integer stealthTaskResultId, String kpi) {
		Query query = getEntityManager().createNamedQuery("getProbeDetailByKpi");
		query.setParameter(StealthConstants.ID, stealthTaskResultId);
		query.setParameter(StealthConstants.KPI, kpi);
		List<ProbeDetail> resultList = query.getResultList();
		
		if(CollectionUtils.isNotEmpty(resultList)) {
			return resultList.get(0);
		}
		
		return null;
	}

}
