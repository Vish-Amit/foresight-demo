package com.inn.foresight.core.gallery.dao.impl;

import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.gallery.dao.IL2ManagerDao;
import com.inn.foresight.core.gallery.model.L2Manager;

@Repository("L2ManagerDaoimpl")
public class L2ManagerDaoimpl extends HibernateGenericDao<Integer, L2Manager> implements IL2ManagerDao {

	
	public L2ManagerDaoimpl() {
		super(L2Manager.class);
	}
}
