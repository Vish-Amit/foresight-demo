package com.inn.foresight.core.gallery.dao.impl;

import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.gallery.dao.IL1ManagerDao;
import com.inn.foresight.core.gallery.model.L1Manager;

@Repository("L1ManagerDaoimpl")
public class L1ManagerDaoimpl extends HibernateGenericDao<Integer, L1Manager> implements IL1ManagerDao {

	
	public L1ManagerDaoimpl() {
		super(L1Manager.class);
	}
}
