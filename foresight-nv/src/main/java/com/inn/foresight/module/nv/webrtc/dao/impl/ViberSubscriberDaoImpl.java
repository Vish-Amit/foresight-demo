package com.inn.foresight.module.nv.webrtc.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.module.nv.webrtc.dao.IViberSubscriberDao;
import com.inn.foresight.module.nv.webrtc.model.ViberSubscriber;

@Repository("ViberSubscriberDaoImpl")
public class ViberSubscriberDaoImpl  extends HibernateGenericDao<Integer, ViberSubscriber> implements IViberSubscriberDao {

	private Logger logger = LogManager.getLogger(ViberSubscriberDaoImpl.class);
	
	public ViberSubscriberDaoImpl() {
		super(ViberSubscriber.class);
	}

	@Override
	public List<ViberSubscriber> getUserCount(String startDate, String endDate, String technology, String operator, String country, String nvModule, Boolean isLastSevenDayDataRequired) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<ViberSubscriber> criteriaQuery = criteriaBuilder.createQuery(ViberSubscriber.class);
		Root<ViberSubscriber> root = criteriaQuery.from(ViberSubscriber.class);
		List<Predicate> predicateList = getPredicateList(sdf.parse(startDate),sdf.parse(endDate), technology, operator, country, nvModule,
				criteriaBuilder, root, isLastSevenDayDataRequired);
		criteriaQuery = addPredicateInCreateriaQuery(criteriaQuery, predicateList);
		List<ViberSubscriber> resultList = getEntityManager().createQuery(criteriaQuery).getResultList();
		logger.info("Result Size ::: {}", resultList.size());
		return resultList;
	}

	private CriteriaQuery<ViberSubscriber> addPredicateInCreateriaQuery(CriteriaQuery<ViberSubscriber> criteriaQuery,
			List<Predicate> predicateList) {
		criteriaQuery.where(predicateList.toArray(new Predicate[] {}));
		return criteriaQuery;
	}

	private List<Predicate> getPredicateList(Date startDate, Date endDate, String technology, String operator, String country, String nvModule,
			CriteriaBuilder criteriaBuilder, Root<ViberSubscriber> root, Boolean isLastSevenDayDataRequired) {
		List<Predicate> predicates = new ArrayList<>();
		if (startDate != null && endDate != null && !isLastSevenDayDataRequired) {
			predicates.add(criteriaBuilder.between(root.<Date>get("creationDate"), startDate, endDate));
		}
		if (isLastSevenDayDataRequired) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DATE, -6);
			Date lastSevenDate = cal.getTime();
			predicates.add(criteriaBuilder.between(root.<Date>get("creationDate"), lastSevenDate, endDate));
		}
		if ( technology != null) {
			predicates.add(criteriaBuilder.equal(root.get("technology"), technology));
		}
		if ( operator != null) {
			predicates.add(criteriaBuilder.equal(root.get("operatorName"), operator));
		}
		if ( country != null) {
			predicates.add(criteriaBuilder.equal(root.get("countryName"), country));
		}
		if (nvModule != null) {
			predicates.add(criteriaBuilder.equal(root.get("nvModule"), nvModule));
		}
		return predicates;
	}
	
}
