package com.inn.foresight.core.generic.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.inn.core.generic.dao.impl.HibernateGenericDao;
import com.inn.foresight.core.generic.dao.IJobStatusDao;
import com.inn.foresight.core.generic.model.JobSatatusConstant.ProcessType;
import com.inn.foresight.core.generic.model.JobStatus;

/**
 * Class providing the methods for storing and updating the last record
 * processed for aggregation.
 */
@Repository("JobStatusDaoImpl")
public class JobStatusDaoImpl extends HibernateGenericDao<Long, JobStatus> implements IJobStatusDao {

	/**
	 * Instantiates a new job status dao impl.
	 */
	public JobStatusDaoImpl() {
		super(JobStatus.class);
	}
	
	/** The Constant logger. */
	private static final Logger logger=LogManager.getLogger(JobStatusDaoImpl.class);

	/**
	 * Method providing the row key for the last record processed for aggregation.
	 *
	 * @param constantType the constant type
	 * @return the latest record for type
	 */
	@Override
	public JobStatus getLatestRecordForType(String constantType) {
		JobStatus hbRecord=null;
		try{
			logger.info("Going to get job status for constantType [{}]",constantType);
			Query query = getEntityManager().createNamedQuery("getDetailForConstant").setParameter("typeconstant", constantType);
			hbRecord = (JobStatus) query.getSingleResult();
		}catch(NoResultException result){
			logger.error("JobStatus detail not found for type constant {} ",constantType);
		}
		return hbRecord;
	}


	/**
	 * Gets the constants detail.
	 *
	 * @param constantList the constant list
	 * @return the constants detail
	 */
	@Override
	public List<JobStatus>  getConstantsDetail(List<String> constantList) {
		List<JobStatus> statusList =null;
		try{
			List<ProcessType> enumList=new ArrayList<>();
			for(String constant:constantList){
				enumList.add(ProcessType.valueOf(ProcessType.class, constant));
			}
			Query query = getEntityManager().createNamedQuery("getTypeconstantDetails").setParameter("typeconstant", enumList );
			statusList = query.getResultList();
			logger.info("Getting number of detail list param size {}, result size {}",
					constantList.size(),statusList.size());
		}catch(NoResultException result){
			logger.error("JobStatus detail not found for type constant {} ",constantList);
		}
		return statusList;
	}

}
