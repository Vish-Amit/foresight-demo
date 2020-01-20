package com.inn.foresight.module.nv.dao;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.infra.model.NetworkElement;

import java.util.List;
import java.util.Map;

public interface SiteDetailDao extends IGenericDao<Integer, NetworkElement> {


    Map<String, String> getNENameByCgi(Integer cgi);

	List<Object[]> getTIReadySites(Long modificationTime);
}
