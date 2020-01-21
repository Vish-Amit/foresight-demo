package com.inn.foresight.core.infra.service;

import java.util.Date;
import java.util.List;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.model.PortInventory;

public interface IPortInventoryService  extends IGenericService<Integer, PortInventory>{


	List<String> getPortsByRouter(String routerName);

	List<String> getAllActivePortsByRouter(String domain, String vendor,String neid, Date currentDate);

	List<String> getActivePortsByRouterPortType(String domain, String vendor, String neid, String portType);

	List<String> getPhysicalAndLogicalPortsByRouter(String neid);
}
