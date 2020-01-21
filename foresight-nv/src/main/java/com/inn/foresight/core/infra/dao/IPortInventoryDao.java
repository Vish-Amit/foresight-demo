package com.inn.foresight.core.infra.dao;

import java.util.Date;
import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.infra.model.PortInventory;

public interface IPortInventoryDao  extends IGenericDao<Integer, PortInventory>{
	List<String> getAllPorts(String neid);

	List<String> getAllActivePhysicalPorts(String domain, String vendor, String neid, Date currentDate);

	List<String> getActivePhysicalPortsByPortType(String domain, String vendor, String neid, String portType);

	List<Object[]> getAllActivePhysicalPortByRouters(String domain,
			String vendor, List<String> neNames);

	List<Object[]> getPhysicalAndLogicalPortsByRouter(String neid);

}
