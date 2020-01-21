package com.inn.foresight.core.infra.dao.impl;

import java.util.Arrays;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.infra.dao.IJobHistoryDao;
import com.inn.foresight.core.infra.model.JobHistory;
import com.inn.product.security.auth.model.BasicAuthenticationDetail;

@Repository("JobHistoryDaoImpl")
public class JobHistoryDaoImpl extends HibernateGenericDao<Integer, JobHistory> implements IJobHistoryDao {

	private static final Expression<?> OABSPA = null;
	/** The logger. */
	private Logger logger = LogManager.getLogger(JobHistoryDaoImpl.class);

	/**
	 * Instantiates a new system configuration dao impl.
	 */
	public JobHistoryDaoImpl() {
		super(JobHistory.class);
	}

	public String getvalueByNameAndWeekNo(String name, String weekno) {
		String value1 = null;
		try {
			Query query = getEntityManager().createNamedQuery("getvalueByNameAndWeekNo");
			query.setParameter("name", name);
			query.setParameter("weekno", weekno);
			value1 = (String) query.getSingleResult();
			logger.info("value {}", value1);
		} catch (NoResultException e) {
			logger.warn("NoResultException in getvalueByNameAndWeekNo() in dao class{},message{}", e.getClass(), e.getMessage());
		} catch (Exception e) {
			logger.error("error in getvalueByNameAndWeekNo in dao class{},message{}", ExceptionUtils.getStackTrace(e));
		}
		return value1;
	}

	/**
	 * Gets the Job History name and type.
	 *
	 * @return the Job History name and type
	 * 
	 */
	@Override
	public String getvalueByName(String name) {
		String value = null;
		try {
			Query query = getEntityManager().createNamedQuery("getvalueByName");
			query.setParameter("name", name);
			value = (String) query.getSingleResult();
		} catch (NoResultException e) {
			logger.warn("NoResultException in getvalueByName() in dao class{},message{}", e.getClass(), e.getMessage());
		} catch (Exception e) {
			logger.error("error in getvalueByName in dao class{},message{}", ExceptionUtils.getStackTrace(e));
		}
		return value;
	}

	/**
	 * Gets the date < givendate Gets the Job History name(key) and value(date).
	 *
	 * @return the date < givendate
	 * @throws RestException
	 * 
	 */
	@Override
	public String getLastAvailableTileDateByDateAndKey(String key, String date) {
		String value = null;
		logger.info("inside getLastAvailableTileDataHistoryByDateAndKey Dao with date {} ", date);
		try {
			Query query = getEntityManager().createNamedQuery("getCurrentAvailableTileDate");
			query.setParameter(ForesightConstants.SMALL_KEY, key);
			query.setParameter(ForesightConstants.DATE, date);
			logger.info("query :{}", query);
			value = (String) query.getResultList().get(0);
			logger.info("value :{}", value);
		} catch (NoResultException ex) {
			logger.warn("NO ENTITY FOUND");
		} catch (Exception e) {
			logger.error("error in getLastAvailableTileDataHistoryByDateAndKey  {} ", ExceptionUtils.getStackTrace(e));
		}
		return value;
	}

	/**
	 * Gets the latest available tile date.
	 *
	 * @param startdate
	 *            the startdate
	 * @param enddate
	 *            the enddate
	 * @param key
	 *            the key
	 * @return the latest available tile date
	 * @throws RestException
	 *             the rest exception
	 */
	@Override
	public String getLatestAvailableTileDate(String startDate, String endDate, String key) {
		String value = null;
		logger.info("inside getLatestAvailableTileDate Dao  startDate {} ,endDate{}, key {} ", startDate, endDate, key);
		try {
			if (startDate != null && endDate != null && key != null) {
				Query query = getEntityManager().createNamedQuery("getLatestAvailableTileDate");
				query.setParameter(ForesightConstants.START_DATE, startDate);
				query.setParameter(ForesightConstants.END_DATE, endDate);
				query.setParameter(ForesightConstants.SMALL_KEY, key);
				logger.info("query :{}", query);
				value = (String) query.getResultList().get(0);
			} else if (endDate != null && key != null) {
				logger.info("inside getLatestAvailableTileDateByendDate Dao endDate{}, key {} ", endDate, key);
				Query query = getEntityManager().createNamedQuery("getLatestAvailableTileDateByendDate");
				query.setParameter(ForesightConstants.END_DATE, endDate);
				query.setParameter(ForesightConstants.SMALL_KEY, key);
				logger.info("query :{}", query);
				value = (String) query.getResultList().get(0);
				logger.info("value :{}", value);
			}
		} catch (NoResultException ex) {
			logger.warn("NO ENTITY FOUND");
		} catch (Exception e) {
			logger.error("error in getLatestAvailableTileDate  {} ", ExceptionUtils.getStackTrace(e));
		}
		return value;
	}

	@Override
	public String getMaxValueByKeyAndDate(String key) {
		logger.info("@Dao Inside getMaxValueByKeyAndDate @class {}  Key {} ,weekNo{}", this.getClass().getSimpleName(), key);
		try {
			String value = ForesightConstants.BLANK_STRING;
			if (key != null) {
				Query query = getEntityManager().createNamedQuery("getMaxValueByKeyAndWeekNo");
				query.setParameter("name", key);
				// query.setParameter("weekno", weekNo);
				value = (String) query.getSingleResult();
				logger.info("value {}", value);
			}
			return value;
		} catch (NoResultException ex) {
			logger.warn("NO ENTITY FOUND");
			throw new RestException(ForesightConstants.EXCEPTION_DATA_MEASURE_FAILURE);
		} catch (Exception e) {
			logger.error("Error while fetching record getMaxValueByKeyAndDate  {} ", ExceptionUtils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_DATA_MEASURE_FAILURE);
		}
	}

	@Override
	public List<JobHistory> getListOfDateByName(String name) {
		try {
			TypedQuery<JobHistory> query = getEntityManager().createNamedQuery("getListOfDateByName", JobHistory.class);
			query.setParameter("name", name);
			return query.getResultList();
		} catch (NoResultException e) {
			logger.warn("NoResultException in getListByName() in dao class{},message{}", e.getClass(), e.getMessage());
		} catch (Exception e) {
			logger.error("error in getListByName in dao class{},message{}", ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getWeekNoAndValueByName(String name) {
		logger.info("Going to get week no and value by name : {}", name);
		List<Object[]> data = null;
		try {
			String sqlQuery = "select jh.weekno,jh.value from JobHistory jh where jh.name=:name order by jh.weekno desc ";
			Query query = getEntityManager().createQuery(sqlQuery).setParameter("name", name);
			data = query.getResultList();
		} catch (Exception ex) {
			logger.error("error in getting latest date from system configuration:{}", ExceptionUtils.getStackTrace(ex));
		}
		return data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getvalueListByName(String name, String limit) {
		List<String> value = null;
		try {
			Query query = getEntityManager().createNamedQuery("getvalueListByName");
			query.setParameter("name", name);
			value = query.setMaxResults(Integer.valueOf(limit)).getResultList();
		} catch (NoResultException e) {
			logger.warn("NoResultException in getvalueListByName() in dao class{},message{}", e.getClass(), e.getMessage());
		} catch (Exception e) {
			logger.error("error in getvalueListByName in dao class{},message{}", ExceptionUtils.getStackTrace(e));
		}
		return value;
	}

	@Override
	public JobHistory getJobhistoryByNameandValue(String name, String value) {
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<JobHistory> criteriaQuery = criteriaBuilder.createQuery(JobHistory.class);
			Root<JobHistory> root = criteriaQuery.from(JobHistory.class);
			criteriaQuery.where(criteriaBuilder.equal(root.get("name"), name), criteriaBuilder.and(criteriaBuilder.equal(root.get("value"), value)));
			Query query = getEntityManager().createQuery(criteriaQuery);
			return (JobHistory) query.getSingleResult();
		} catch (NoResultException noResultException) {
			throw new DaoException(noResultException.getMessage());
		} catch (Exception exception) {
			throw new DaoException(exception.getMessage());
		}
	}

	@Override
	public void selectAllFromNE() {
		logger.info("select All From NE {}");
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<JobHistoryWrapper> criteriaQuery = criteriaBuilder.createQuery(JobHistoryWrapper.class);
			Root<JobHistory> root = criteriaQuery.from(JobHistory.class);

			// criteriaQuery.multiselect(criteriaBuilder.max(root.get("userid")));

			/*
			 * criteriaQuery.multiselect(criteriaBuilder.sum(root.get("userid")));
			 * logger.info("data  : {} ",
			 * getEntityManager().createQuery(criteriaQuery).getSingleResult());
			 */

			// criteriaQuery.multiselect(criteriaBuilder.min(root.get("userid")),
			// criteriaBuilder.max(root.get("userid")));

			criteriaQuery.where(criteriaBuilder.between(root.get("id"), 10, 20));
			criteriaQuery.select(criteriaBuilder.construct(JobHistoryWrapper.class, criteriaBuilder.min(root.get("id")), criteriaBuilder.max(root.get("id")), root.get("name")));
			Query query= getEntityManager().createQuery(criteriaQuery).setMaxResults(1);
			logger.info("data  : {} ",query.getSingleResult());

			/*
			 * criteriaQuery.multiselect(criteriaBuilder.max(root.get("userid")));
			 * 
			 * logger.info("data  : {} ",
			 * getEntityManager().createQuery(criteriaQuery).getSingleResult());
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void selectAllFromNAD() {
		logger.info("select All From NE {}");
		try {
			CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
			CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
			Root<BasicAuthenticationDetail> root = criteriaQuery.from(BasicAuthenticationDetail.class);

			/*
			 * //1 Subquery<String> countUserSubQuery1 =
			 * criteriaQuery.subquery(String.class); Root<BasicAuthenticationDetail>
			 * subQueryUserRoot1 = countUserSubQuery1.from(BasicAuthenticationDetail.class);
			 * countUserSubQuery1.select(subQueryUserRoot1.get("username"));
			 * 
			 * countUserSubQuery1.where(criteriaBuilder.like(root.get("username"),
			 * "%nitin%"));
			 */
			Subquery<Long> countUserSubQuery2 = criteriaQuery.subquery(Long.class);
			Root<BasicAuthenticationDetail> subQueryUserRoot2 = countUserSubQuery2.from(BasicAuthenticationDetail.class);

			countUserSubQuery2.where(criteriaBuilder.equal(subQueryUserRoot2.get("enabled"), 1));
			((Query) countUserSubQuery2.select(criteriaBuilder.count(subQueryUserRoot2.get("username")))).setMaxResults(1);

			Subquery<Long> countUserSubQuery3 = criteriaQuery.subquery(Long.class);
			Root<BasicAuthenticationDetail> subQueryUserRoot3 = countUserSubQuery3.from(BasicAuthenticationDetail.class);

			countUserSubQuery3.where(criteriaBuilder.equal(subQueryUserRoot3.get("enabled"), 0));
			((Query) countUserSubQuery3.select(criteriaBuilder.count(subQueryUserRoot3.get("username")))).setMaxResults(1);

			Subquery<Long> countUserSubQuery4 = criteriaQuery.subquery(Long.class);
			Root<BasicAuthenticationDetail> subQueryUserRoot4 = countUserSubQuery4.from(BasicAuthenticationDetail.class);

			countUserSubQuery4.where(criteriaBuilder.equal(subQueryUserRoot4.get("locked"), 1));
			((Query) countUserSubQuery4.select(criteriaBuilder.count(subQueryUserRoot4.get("username")))).setMaxResults(1);

			Subquery<Long> countUserSubQuery5 = criteriaQuery.subquery(Long.class);
			Root<BasicAuthenticationDetail> subQueryUserRoot5 = countUserSubQuery5.from(BasicAuthenticationDetail.class);

			countUserSubQuery5.where(criteriaBuilder.equal(subQueryUserRoot5.get("locked"), 0));
		((Query) countUserSubQuery5.select(criteriaBuilder.count(subQueryUserRoot5.get("username")))).setMaxResults(1);
		
		
		Subquery<Long> countUserSubQuery6 = criteriaQuery.subquery(Long.class);
		Root<BasicAuthenticationDetail> subQueryUserRoot6 = countUserSubQuery6.from(BasicAuthenticationDetail.class);
	countUserSubQuery6.select(criteriaBuilder.sum(countUserSubQuery2.getSelection(),countUserSubQuery3.getSelection()));


	Subquery<Long> countUserSubQuery7 = criteriaQuery.subquery(Long.class);
	Root<BasicAuthenticationDetail> subQueryUserRoot7 = countUserSubQuery7.from(BasicAuthenticationDetail.class);
countUserSubQuery7.select(criteriaBuilder.sum(countUserSubQuery4.getSelection(),countUserSubQuery5.getSelection()));

			
			criteriaQuery.multiselect(countUserSubQuery2.getSelection(),countUserSubQuery3.getSelection(),countUserSubQuery6.getSelection(),
					countUserSubQuery4.getSelection(),countUserSubQuery5.getSelection(),countUserSubQuery7.getSelection());
			/*
			 * criteriaQuery.where(criteriaBuilder.in(root.get("username")).value(
			 * countUserSubQuery1.select(subQueryUserRoot1.get("username"))));
			 * criteriaQuery.multiselect(root.get("username"),root.get("enabled"));
			 */
			Query query = getEntityManager().createQuery(criteriaQuery).setMaxResults(1);
			List ll=query.getResultList();
			for(Object obj:ll)
				logger.info("data  : {}", Arrays.toString((long[]) obj));
			/*
			 * List <Object[]> lis = for(Object[] ll:lis) { logger.info("data  : {}", ll);}
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
