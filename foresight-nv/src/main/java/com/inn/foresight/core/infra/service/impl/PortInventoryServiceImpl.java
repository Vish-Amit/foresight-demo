package com.inn.foresight.core.infra.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IPortInventoryDao;
import com.inn.foresight.core.infra.model.PortInventory;
import com.inn.foresight.core.infra.service.IPortInventoryService;


@Service("PortInventoryServiceImpl")
public class PortInventoryServiceImpl extends AbstractService<Integer, PortInventory>implements IPortInventoryService{

	/** The logger. */
	private Logger logger = LogManager.getLogger(PortInventoryServiceImpl.class);
	@Autowired
	IPortInventoryDao iportInventoryDao;

	@Override
	public List<String> getPortsByRouter(String neid) {
		return iportInventoryDao.getAllPorts(neid);
	}
	
	@Override
	public List<String> getAllActivePortsByRouter(String domain, String vendor, String neid, Date currentDate) {
		List<String> allPorts = iportInventoryDao.getAllActivePhysicalPorts(domain, vendor, neid, currentDate);
		return allPorts;
	}
	
	@Override
	public List<String> getActivePortsByRouterPortType(String domain, String vendor, String neid, String portType) {
		List<String> allPorts = iportInventoryDao.getActivePhysicalPortsByPortType(domain, vendor, neid, portType);
		return allPorts;
	}
	
	@Override
	public List<String> getPhysicalAndLogicalPortsByRouter(String neid) {
		List<String> allPorts =new ArrayList<>();
		List<Object[]> list = iportInventoryDao.getPhysicalAndLogicalPortsByRouter(neid);
		if(Utils.isValidList(list)) {
			for(Object[] objects:list) {
				if(objects[ForesightConstants.INDEX_ZERO]!=null) {
					allPorts.add(String.valueOf(objects[ForesightConstants.INDEX_ZERO]));
				}
				if(objects[ForesightConstants.INDEX_ONE]!=null) {
					allPorts.add(String.valueOf(objects[ForesightConstants.INDEX_ONE]));
				}
			}
		}
		if(Utils.isValidList(allPorts)) {
			allPorts=allPorts.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
		}
		return allPorts;
	}
}
