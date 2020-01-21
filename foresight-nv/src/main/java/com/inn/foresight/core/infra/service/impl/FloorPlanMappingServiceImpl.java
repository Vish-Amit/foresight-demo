package com.inn.foresight.core.infra.service.impl;

import java.util.Date;

import javax.persistence.NoResultException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inn.core.generic.service.impl.AbstractService;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.IFloorPlanMappingDao;
import com.inn.foresight.core.infra.dao.IUnitDataDao;
import com.inn.foresight.core.infra.model.FloorPlanMapping;
import com.inn.foresight.core.infra.service.IFloorPlanMappingService;

@Service("FloorPlanMappingServiceImpl")
public class FloorPlanMappingServiceImpl extends AbstractService<Integer, FloorPlanMapping> implements IFloorPlanMappingService {
	private Logger logger = LogManager.getLogger(FloorPlanMappingServiceImpl.class);
	@Autowired
	private IFloorPlanMappingDao iFloorPlanMappingDao;
	@Autowired
	private IUnitDataDao iUnitDataDao;

	@Autowired
	public void setDao(IFloorPlanMappingDao iFloorPlanMappingDao) {
		super.setDao(iFloorPlanMappingDao);
		this.iFloorPlanMappingDao = iFloorPlanMappingDao;
	}

	
	@Override
	@Transactional
	public void updateFloorPlanMapping(Integer unitId,String templateType) {
		logger.info("Going to updateFloorPlanMapping for Unit id {} ,templateType {}", unitId,templateType);
		FloorPlanMapping floorPlanMapping = null;
			try {
				floorPlanMapping = iFloorPlanMappingDao.getFloorplanMappingByUnitIdAndTemplate(unitId, templateType);
				floorPlanMapping.setModificationTime(new Date());
				floorPlanMapping.setIsApproved(null);
				iFloorPlanMappingDao.update(floorPlanMapping);
			} catch (NoResultException | EmptyResultDataAccessException e) {
				logger.error("NoResultException in updateFloorPlanMapping {}", Utils.getStackTrace(e));
				floorPlanMapping = new FloorPlanMapping();
				floorPlanMapping.setUnit(iUnitDataDao.findByPk(unitId));
				floorPlanMapping.setTemplateType(templateType);
				floorPlanMapping.setCreationTime(new Date());
				floorPlanMapping.setModificationTime(new Date());
				floorPlanMapping.setIsApproved(null);
				logger.info("Going to create floorPlanMapping {}",floorPlanMapping);
				floorPlanMapping=iFloorPlanMappingDao.create(floorPlanMapping);
				logger.info("floorPlanMapping creation done.......{}",floorPlanMapping);
			}catch (Exception e) {
				logger.error("Exception in updateFloorPlanMapping {}", Utils.getStackTrace(e));
			}
		
	}

}
