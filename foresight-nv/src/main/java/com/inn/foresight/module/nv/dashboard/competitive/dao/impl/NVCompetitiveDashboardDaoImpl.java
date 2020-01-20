package com.inn.foresight.module.nv.dashboard.competitive.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.core.generic.utils.Utils;
import com.inn.foresight.module.nv.dashboard.competitive.constant.NVCompetitiveConstant;
import com.inn.foresight.module.nv.dashboard.competitive.dao.INVCompetitiveDashboardDao;
import com.inn.foresight.module.nv.dashboard.competitive.model.NVCompetitiveDashboard;

/**
 * The Class NVCompetitiveDashboardDaoImpl.
 */
@Repository("NVCompetitiveDashboardDaoImpl")
public class NVCompetitiveDashboardDaoImpl extends HibernateGenericDao<Integer, NVCompetitiveDashboard>
		implements INVCompetitiveDashboardDao {


	/**
	 * Instantiates a new NV competitive dashboard dao impl.
	 */
	public NVCompetitiveDashboardDaoImpl() {
		super(NVCompetitiveDashboard.class);
	}

	/** The logger. */
	private Logger logger = LoggerFactory.getLogger(NVCompetitiveDashboardDaoImpl.class);

	/**
	 * Gets the competitive samples.
	 *
	 * @param date the date
	 * @param geographyL1 the geography L 1
	 * @param geographyL2 the geography L 2
	 * @param geographyL3 the geography L 3
	 * @param geographyL4 the geography L 4
	 * @param operator the operator
	 * @return the competitive samples
	 */
	@Override
	public List<NVCompetitiveDashboard> getCompetitiveSamples(String date, String geographyL1, String geographyL2,
			String geographyL3, String geographyL4, List<String> operator,String technology,String appName) {
		logger.info("inside the method getCompetitiveSamples Dao date [{}], operators [{}] , applicationName {}", date, operator,appName);

		SimpleDateFormat sampleDateFormat = new SimpleDateFormat(NVCompetitiveConstant.DATE_FORMAT_yyyy_MM_dd);
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<NVCompetitiveDashboard> criteriaQuery = criteriaBuilder.createQuery(NVCompetitiveDashboard.class);
		List<Predicate> predicateList;
		List<NVCompetitiveDashboard> result = new ArrayList<>();;
		try {
			predicateList = getPredicateList(sampleDateFormat.parse(date), geographyL1, geographyL2, geographyL3, geographyL4, operator,technology,
					criteriaBuilder, criteriaQuery.from(NVCompetitiveDashboard.class),appName);
			criteriaQuery = addPredicateInCreateriaQuery(criteriaQuery, predicateList);
			result = getEntityManager().createQuery(criteriaQuery).getResultList();

		} catch (Exception e) {
			logger.error(Utils.getStackTrace(e));
		}
		logger.info("Result response size ::: {}", result.size());
		return result;
	}

	/**
	 * Adds the predicate in createria query.
	 *
	 * @param criteriaQuery the criteria query
	 * @param predicates the predicates
	 * @return the criteria query
	 */
	private CriteriaQuery<NVCompetitiveDashboard> addPredicateInCreateriaQuery(
			CriteriaQuery<NVCompetitiveDashboard> criteriaQuery, List<Predicate> predicates) {
		criteriaQuery.where(predicates.toArray(new Predicate[] {}));
		return criteriaQuery;
	}

	/**
	 * Gets the predicate list.
	 *
	 * @param date the date
	 * @param geographyL1 the geography L 1
	 * @param geographyL2 the geography L 2
	 * @param geographyL3 the geography L 3
	 * @param geographyL4 the geography L 4
	 * @param operators the operators
	 * @param technology 
	 * @param criteriaBuilder the criteria builder
	 * @param root the root
	 * @param appName 
	 * @return the predicate list
	 */
	private List<Predicate> getPredicateList(Date date, String geographyL1, String geographyL2, String geographyL3,
			String geographyL4, List<String> operators, String technology, CriteriaBuilder criteriaBuilder,
			Root<NVCompetitiveDashboard> root, String appName) {
		List<Predicate> predicates = new ArrayList<>();
		
		if (date != null) {
			predicates.add(criteriaBuilder.equal(root.get(NVCompetitiveConstant.CREATION_TIME).as(java.sql.Date.class), date));
		}

		if(Utils.isValidString(geographyL4)) {
			predicates.add(criteriaBuilder.equal(root.get(NVCompetitiveConstant.GEOGRAPHY_L4), Integer.parseInt(geographyL4)));
		}
		
		if (Utils.isValidString(geographyL3)) {
			predicates.add(criteriaBuilder.equal(root.get(NVCompetitiveConstant.GEOGRAPHY_L3), Integer.parseInt(geographyL3)));
		}
		
		if (Utils.isValidString(geographyL2)) {
			predicates.add(criteriaBuilder.equal(root.get(NVCompetitiveConstant.GEOGRAPHY_L2), Integer.parseInt(geographyL2)));
		}
		
		if (Utils.isValidString(geographyL1)) {
			predicates.add(criteriaBuilder.equal(root.get(NVCompetitiveConstant.GEOGRAPHY_L1), Integer.parseInt(geographyL1)));
		}
		
		if (operators != null && !operators.isEmpty()) {
			Expression<String> parentExpression = root.get(NVCompetitiveConstant.OPERATOR_KEY);
			predicates.add(parentExpression.in(operators));
		}
		
		if(technology != null) {
			predicates.add(criteriaBuilder.equal(root.get(NVCompetitiveConstant.TECHNOLOGY), technology));
		}

		if(Utils.hasValue(appName))
		{
			predicates.add(criteriaBuilder.equal(root.get(NVCompetitiveConstant.APPLICATION_TYPE),appName));
		}
		return predicates;
	}
}
