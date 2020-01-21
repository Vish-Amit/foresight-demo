package com.inn.foresight.core.excel.dao.Impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.excel.dao.IExcelDao;
import com.inn.foresight.core.excel.model.ExcelData;


@Repository("ExcelDaoImpl")
public class ExcelDaoImpl extends HibernateGenericDao<Integer, ExcelData> implements IExcelDao{

	private Logger logger = LogManager.getLogger(ExcelDaoImpl.class);

	
	public ExcelDaoImpl() {
		super(ExcelData.class);
	}
	
	@Transactional
	@Override
	public void excelRW() {
		
		logger.debug("insert data in db");
	
		
	}

	

	@Transactional
	@Override
	public void updateExcel() {
		
		logger.debug("update data in db");
	
		
	}
	
	

	@Override
	public List<ExcelData> writeToExcel() {
		
		logger.debug("set data in excel");
		
		return getEntityManager().createNamedQuery("writeInToExcel").setMaxResults(50).getResultList();
				
		
	
	}
	
	
	@Override
	public List<ExcelData> criteriaDemo() {
		
		logger.debug("set data in excel");
		
		
		  EntityManager em = getEntityManager();
		  em.getTransaction().begin( );  
          CriteriaBuilder cb=em.getCriteriaBuilder();  
          CriteriaQuery<ExcelData> cq=cb.createQuery(ExcelData.class);  
            
         Root<ExcelData> ed=cq.from(ExcelData.class);  
           
         cq.select(ed.get("GN : "));   
           
          CriteriaQuery<ExcelData> select = cq.select(ed);  
          TypedQuery<ExcelData> q = em.createQuery(select);  
          List<ExcelData> list = q.getResultList();  

		  
		  return list;
				
		
	
	}

}
