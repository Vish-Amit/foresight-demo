package com.inn.foresight.core.infra.dao.impl;

import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.infra.dao.MNEAuditDao;
import com.inn.foresight.core.infra.model.MNEAudit;

@Repository("MNEAuditDaoImpl")
public class MNEAuditDaoImpl extends HibernateGenericDao<Long, MNEAudit> implements MNEAuditDao{

	public MNEAuditDaoImpl() {
		super(MNEAudit.class);
	}

}
