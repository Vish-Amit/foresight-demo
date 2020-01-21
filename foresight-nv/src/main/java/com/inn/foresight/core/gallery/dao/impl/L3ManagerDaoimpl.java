package com.inn.foresight.core.gallery.dao.impl;

import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.gallery.dao.IL3ManagerDao;
import com.inn.foresight.core.gallery.model.L3Manager;

@Repository("L3ManagerDaoimpl")
public class L3ManagerDaoimpl extends HibernateGenericDao<Integer, L3Manager> implements IL3ManagerDao {

	
	public L3ManagerDaoimpl() {
		super(L3Manager.class);
	}
}
