package com.inn.foresight.core.infra.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.infra.model.FloorPlanMapping;

public interface IFloorPlanMappingDao extends IGenericDao<Integer, FloorPlanMapping>{
 public FloorPlanMapping getFloorplanMappingByUnitIdAndTemplate(Integer unitId,String template);
 public List<FloorPlanMapping>getFloorplanMappingByUnitId(Integer unitId);
}
