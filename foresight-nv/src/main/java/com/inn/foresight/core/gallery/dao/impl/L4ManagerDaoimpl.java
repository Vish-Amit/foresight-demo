package com.inn.foresight.core.gallery.dao.impl;

import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.gallery.dao.IL4ManagerDao;
import com.inn.foresight.core.gallery.model.L4Manager;

@Repository("L4ManagerDaoimpl")
public class L4ManagerDaoimpl extends HibernateGenericDao<Integer, L4Manager> implements IL4ManagerDao {

	
	public L4ManagerDaoimpl() {
		super(L4Manager.class);
	}
}
