package com.inn.foresight.core.generic.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.dao.IOperatorDetailDao;
import com.inn.foresight.core.generic.model.OperatorDetail;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;

@Repository("OperatorDetailDaoImpl")
public class OperatorDetailDaoImpl extends HibernateGenericDao<Integer, OperatorDetail>  implements IOperatorDetailDao{

	@Autowired
	public OperatorDetailDaoImpl() {
		super(OperatorDetail.class);
	}

	Logger logger = LogManager.getLogger(OperatorDetailDaoImpl.class);

	@Override
	public List<OperatorDetail> getSearchWiseData(String searchType, String countryName, String module) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<OperatorDetail> criteriaQuery = criteriaBuilder.createQuery(OperatorDetail.class);
		Root<OperatorDetail> root = criteriaQuery.from(OperatorDetail.class);
		List<Predicate> predicateList = getPredicateList(searchType,countryName,module,criteriaBuilder, root);
		 addPredicateInCreateriaQuery(criteriaQuery, predicateList);
		List<OperatorDetail> resultList = getEntityManager().createQuery(criteriaQuery).getResultList();
		logger.info("Result Size ::: {}", resultList.size());
		return resultList;
	}
	

	@Override
	public OperatorDetail getOperatorDetailListByOperatorName(String operatorName) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<OperatorDetail> criteriaQuery = criteriaBuilder.createQuery(OperatorDetail.class);
		Root<OperatorDetail> root = criteriaQuery.from(OperatorDetail.class);
		List<Predicate> predicateList = getPredicateForOperator(operatorName,criteriaBuilder, root);
		addPredicateInCreateriaQuery(criteriaQuery, predicateList);
		OperatorDetail operatorDetail=new OperatorDetail();
		if(!getEntityManager().createQuery(criteriaQuery).getResultList().isEmpty()) {
			operatorDetail = getEntityManager().createQuery(criteriaQuery).getResultList().get(0); 		
			logger.info("Result from  getOperatorDetailListByOperatorName ::: {}", operatorDetail);			
		}
		return operatorDetail;
	}
	
	
	private List<Predicate> getPredicateForOperator(String operatorName, CriteriaBuilder criteriaBuilder,
			Root<OperatorDetail> root) {
		List<Predicate> predicates = new ArrayList<>();
		if(Utils.hasValidValue(operatorName))
		{
		predicates.add(criteriaBuilder.like(root.get("operator"), operatorName));
		}
		return predicates;
	}
	

	private List<Predicate> getPredicateList(String searchType, String countryName, String module, CriteriaBuilder criteriaBuilder,
			Root<OperatorDetail> root) {
		List<Predicate> predicates = new ArrayList<>();

		if (searchType.equalsIgnoreCase(ForesightConstants.COUNTRY_KEY) ||
				searchType.equalsIgnoreCase(ForesightConstants.OPERATOR_KEY)) {
			if(!countryName.equalsIgnoreCase(ForesightConstants.ALL)) {
				predicates.add(criteriaBuilder.like(root.get("country"), countryName));
			}
		}
		if (!module.equalsIgnoreCase(ForesightConstants.ALL)) {
			predicates.add(criteriaBuilder.like(root.get("module"), module));
		}
		return predicates;
	}
	public static CriteriaQuery<OperatorDetail> addPredicateInCreateriaQuery(
			CriteriaQuery<OperatorDetail> criteriaQuery, List<Predicate> predicates) {
		criteriaQuery.where(predicates.toArray(new Predicate[] {}));
		return criteriaQuery;
	}


	@Override
	public OperatorDetail getOperatorByMCCAndMNC(Integer mcc, Integer mnc) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<OperatorDetail> criteriaQuery = criteriaBuilder.createQuery(OperatorDetail.class);
		Root<OperatorDetail> root = criteriaQuery.from(OperatorDetail.class);
		List<Predicate> predicateList = getPredicateListForMCCAndMNC(mcc,mnc,criteriaBuilder, root);
		 addPredicateInCreateriaQuery(criteriaQuery, predicateList);
		OperatorDetail result= getEntityManager().createQuery(criteriaQuery).getSingleResult();
		logger.info("Result ::: {}", result);
		return result;

	}
	
	
	private List<Predicate> getPredicateListForMCCAndMNC(Integer mcc,Integer mnc ,CriteriaBuilder criteriaBuilder,
			Root<OperatorDetail> root) {
		List<Predicate> predicates = new ArrayList<>();
				predicates.add(criteriaBuilder.equal(root.get("mcc"), mcc));
				predicates.add(criteriaBuilder.equal(root.get("mnc"), mnc));
		return predicates;
	}
	

}
