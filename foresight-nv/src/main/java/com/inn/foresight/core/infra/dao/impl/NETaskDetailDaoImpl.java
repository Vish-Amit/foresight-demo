package com.inn.foresight.core.infra.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.dao.INETaskDetailDao;
import com.inn.foresight.core.infra.model.NETaskDetail;
import com.inn.foresight.core.infra.utils.InfraUtils;
import com.inn.foresight.core.infra.utils.enums.NEStatus;
import com.inn.foresight.core.infra.utils.enums.NEType;

@Repository("NETaskDetailDaoImpl")
public class NETaskDetailDaoImpl extends HibernateGenericDao<Integer, NETaskDetail> implements INETaskDetailDao {

    public NETaskDetailDaoImpl() {
        super(NETaskDetail.class);
    }

    private Logger logger = LogManager.getLogger(NETaskDetailDaoImpl.class);

    @Override
    public List<NETaskDetail> getNETaskDetail(List<NEType> neTypeList, List<String> neFrequencyList, List<NEStatus> neStatusList, List<String> neNameList, List<String> neStagesList) {
        logger.info("Going to get NETaskDetail for NEType : {}, NEFrequency : {}, NEStatus : {}", neTypeList, neFrequencyList, neStatusList);
        try {
            CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<NETaskDetail> criteriaQuery = criteriaBuilder.createQuery(NETaskDetail.class);
            Root<NETaskDetail> neTaskDetail = criteriaQuery.from(NETaskDetail.class);
            List<Predicate> predicates = InfraUtils.getPredicatesForNETaskDetail(criteriaBuilder, neTaskDetail, neTypeList, neNameList, neFrequencyList, neStatusList, neStagesList);
            criteriaQuery.select(neTaskDetail).where(predicates.toArray(new Predicate[]{}));
            Query query = getEntityManager().createQuery(criteriaQuery);
            return query.getResultList();
        } catch (NoResultException noResultException) {
            logger.error("NoResultException caught while getting NETaskDetail for NEType : {}, NEFrequency : {}, NEStatus : {} Exception : {}", neTypeList, neFrequencyList, neStatusList,
                    Utils.getStackTrace(noResultException));
            throw new DaoException("Unable to get NETaskDetail data  for specific parameters.");
        } catch (PersistenceException persistenceException) {
            logger.error("PersistenceException caught while getting NETaskDetail for NEType : {}, NEFrequency : {}, NEStatus : {} Exception : {}", neTypeList, neFrequencyList, neStatusList,
                    Utils.getStackTrace(persistenceException));
            throw new DaoException("Unable to get NETaskDetail data for specific parameters.");
        } catch (Exception exception) {
            logger.error("Error in getting NETaskDetail for NEType : {}, NEFrequency : {}, NEStatus : {} Exception : {}", neTypeList, neFrequencyList, neStatusList, Utils.getStackTrace(exception));
            throw new DaoException("Unable to get NETaskDetail data for specific parameters.");

        }
    }

    @Override
    public List<NETaskDetail> getRollOutStatusOfSite(String name, String band) {
        logger.info("Going to get NETaskDetail for getRollOutStatusOfSite : ");
        try {
            Query query = getEntityManager().createNamedQuery("getRollOutStatusOfSiteByNameAndBand");
            query.setParameter("name", name);
            query.setParameter("band", band);

            return query.getResultList();
        } catch (NoResultException noResultException) {
            logger.error("Error in getting NETaskDetail dataList : ", Utils.getStackTrace(noResultException));
        }
        return null;
    }

    @Override
    public NETaskDetail getSiteTaskStatusBySiteIdAndBand(String siteId, String band, String taskName) {
        try {
            logger.info("inside @getSiteStageTaskStatusBySiteId method siteId : {}, band : {} ,taskName {} ", siteId, band, taskName);
            TypedQuery<NETaskDetail> query = getEntityManager().createNamedQuery("getSiteStageTaskStatusBySiteId", NETaskDetail.class);
            query.setParameter("siteId", siteId);
            query.setParameter("band", band);
            query.setParameter("taskName", taskName);
            return query.getSingleResult();
        } catch (NoResultException exception) {
            logger.warn("No Result found for given siteId :{} ,band : {}, taskName : {}, Message :{}", ExceptionUtils.getMessage(exception));
        } catch (Exception exception) {
            logger.error("Exception occured while getting result from Database :{}", ExceptionUtils.getStackTrace(exception));
            throw new DaoException(ExceptionUtils.getMessage(exception));
        }
        return null;
    }

}
