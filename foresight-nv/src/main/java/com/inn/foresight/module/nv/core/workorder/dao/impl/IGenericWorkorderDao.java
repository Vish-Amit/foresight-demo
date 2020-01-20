package com.inn.foresight.module.nv.core.workorder.dao.impl;

import java.util.Date;
import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.Status;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.core.workorder.wrapper.WorkorderCountWrapper;
import com.inn.foresight.module.nv.workorder.wrapper.NVWorkorderWrapper;

/** The Interface IGenericWorkorderDao. */
public interface IGenericWorkorderDao extends IGenericDao<Integer, GenericWorkorder> {

	/**
	 * Gets the workorder name list.
	 *
	 * @param workorderName
	 *            the workorder name
	 * @return the workorder name list
	 * @throws DaoException
	 *             the dao exception
	 */
	List<String> getWorkorderIdListFromWOId(String workorderName);

	/**
	 * Gets the workorder by WO name.
	 *
	 * @param workorderName
	 *            the workorder name
	 * @return the workorder by WO name
	 * @throws DaoException
	 *             the dao exception
	 */
	GenericWorkorder getWorkorderByWOId(String workorderName);

	Long getTotalWorkorderCount(List<TemplateType> templateList, List<Status> statusList,String searchString,Boolean isArchived);



	List<GenericWorkorder> findAllWorkorder(Integer lLimit, Integer uLimit, List<TemplateType> templateList,
			List<Status> statusList, String searchString, String geographyLevel, List<Integer> geographyId, Integer creatorId,
			Long startTimestamp, Long emdTimestamp);

	Long getAllWorkorderCount(String geographyLevel, Integer geographyId, Date startDate, Date endDate,
			List<TemplateType> templateList);

	List<WorkorderCountWrapper> getADHOCWorkorderDayWiseCount(String geographyLevel, Integer geographyId,
			Date startDate, Date endDate, List<TemplateType> templateList);

	List<WorkorderCountWrapper> getWOCountByTemplateTypeAndStatus(String geographyLevel, Integer geographyId,
			Date startDate, Date endDate, List<TemplateType> woibTemplateList, List<Status> list);

	List<WorkorderCountWrapper> getDayWiseWOCountByStatus(String geographyLevel, Integer geographyId,
			Date startDate, Date endDate, List<TemplateType> templateTypeList, List<Status> list);

	List<WorkorderCountWrapper> getDayWiseAssignedWOCount(String geographyLevel, Integer geographyId,
			Date startDate, Date endDate, List<TemplateType> woTemplateAssignedToUser);

	List<WorkorderCountWrapper> getDueWorkorderDayWiseCount(String geographyLevel, Integer geographyId,
			Date startDate, Date endDate, List<Status> statusList);

	List<WorkorderCountWrapper> getDueWorkorderList(String geographyLevel, Integer geographyId, Date startDate,
			Date endDate, List<Status> statusList, Integer llimit, Integer ulimit);

	List<GenericWorkorder> findAllWorkorder(Integer lLimit, Integer uLimit, List<TemplateType> templateList,
			List<Status> statusList, String searchString, String geographyLevel, List<Integer> geographyId,
			Integer creatorId, Long startTimestamp, Long emdTimestamp, String entityType, String entityValue);
	List<GenericWorkorder> findAllNVWorkorder(Integer lLimit, Integer uLimit, List<TemplateType> templateList,
			List<Status> statusList, String searchString, String geographyLevel, Integer geographyId, Integer creatorId,Boolean isArchive);
	List<GenericWorkorder> getWorkorderListForReport(String geographyLevel, Integer geographyId, Date date);

	List<GenericWorkorder> getGenericWOForUser(String userName);

	List<GenericWorkorder> findByIds(List<Integer> workorderIds);

	List<Object[]> getNVReportConfiguration(String reportType);

	List<Integer> getListOfWorkOrderIdByBuildingId(Integer buildingId, String technology);
	
	List<Integer> getWorkOrderIdListByBuildingId(Integer buildingId);

	List<GenericWorkorder> findAllWorkorder(Integer lLimit, Integer uLimit, List<TemplateType> templateList,
			List<Status> statusList, String searchString, String geographyLevel, Integer geographyId, Integer creatorId,
			Long startTimestamp, Long endTimestamp);
	
	GenericWorkorder findByPkWithUser(Integer entityPk);

	GenericWorkorder findByPkWithGeographyL3(Integer entityPk);

	List<GenericWorkorder> findFilteredWOList(NVWorkorderWrapper wrapper, Integer lLimit, Integer uLimit,
			Boolean isArchive);

	Long getCountByFilter(NVWorkorderWrapper wrapper, Boolean isArchive);
	
	List<GenericWorkorder> getWoIdListForDateRange(Long fromdate,Long todate,TemplateType templateType);

}