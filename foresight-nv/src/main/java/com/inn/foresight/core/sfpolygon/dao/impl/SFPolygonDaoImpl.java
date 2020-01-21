package com.inn.foresight.core.sfpolygon.dao.impl;

import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.sfpolygon.dao.SFPolygonDao;
import com.inn.foresight.core.sfpolygon.model.SFPolygon;

/**
 * The Class SFPolygonDaoImpl.
 */
@Repository("SFPolygonDaoImpl")
public class SFPolygonDaoImpl extends HibernateGenericDao<Integer, SFPolygon>
    implements SFPolygonDao {

  /** The logger. */
  private Logger logger = LogManager.getLogger(SFPolygonDaoImpl.class);

  /**
   * Instantiates a new sf polygon dao impl.
   */
  public SFPolygonDaoImpl() {
    super(SFPolygon.class);
  }

  @Override
  public Integer getSBMPolygonCount() {
    logger.info("Inside getSBMPolygonCount");
    TypedQuery<Integer> query = getEntityManager().createNamedQuery("sbmPolyCount", Integer.class);
    return query.getSingleResult();
  }

  @Override
  public Integer getNBMPolygonCount() {
    logger.info("Inside getNBMPolygonCount");
    TypedQuery<Integer> query = getEntityManager().createNamedQuery("nbmPolyCount", Integer.class);
    return query.getSingleResult();
  }

  @Override
  public List<SFPolygon> getSBMPolygonList() {
    logger.info("Inside getSBMPolygonList, getting valid sf polygon list");
    TypedQuery<SFPolygon> query =
        getEntityManager().createNamedQuery("sbmPolygonDetails", SFPolygon.class);
    return query.getResultList();
  }

  @Override
  public List<SFPolygon> getNBMPolygonList() {
    logger.info("Inside getNBMPolygonList, getting valid sf polygon list");
    TypedQuery<SFPolygon> query =
        getEntityManager().createNamedQuery("nbmPolygonDetails", SFPolygon.class);
    return query.getResultList();
  }

  @Override
  public SFPolygon getPolygonByPolyId(String polyId) {
    logger.info("Inside getPolygonByPolyId, polyId {}", polyId);
    TypedQuery<SFPolygon> query =
        getEntityManager().createNamedQuery("polygonByPolyId", SFPolygon.class);
    query.setParameter("polyId", polyId);
    return query.getSingleResult();
  }

  @Override
  public Integer updateSFPolygonStatus(String polyId, String geography) {
    logger.info("Inside updateSFPolygonStatus, polyId {}, geography {}", polyId, geography);
    try {
      Query query = getEntityManager().createNamedQuery("updatePolygonStatus");
      query.setParameter("polyId", polyId);
      query.setParameter("geography", geography);
      return query.executeUpdate();
    } catch (Exception exception) {
      logger.error("Exception inside updateSFPolygonStatus, due to  {} ",
          ExceptionUtils.getStackTrace(exception));
    }
    throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
  }
}
