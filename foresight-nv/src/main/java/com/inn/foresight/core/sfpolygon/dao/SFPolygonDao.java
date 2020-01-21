package com.inn.foresight.core.sfpolygon.dao;

import java.util.List;
import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.sfpolygon.model.SFPolygon;


/**
 * The Interface SFPolygonDao.
 */
public interface SFPolygonDao extends IGenericDao<Integer, SFPolygon> {

  Integer getSBMPolygonCount();

  Integer getNBMPolygonCount();

  List<SFPolygon> getSBMPolygonList();

  List<SFPolygon> getNBMPolygonList();

  SFPolygon getPolygonByPolyId(String polyId);

  Integer updateSFPolygonStatus(String polyId, String geography);
}
