package com.inn.foresight.core.report.rest.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.report.model.ReportWidget;
import com.inn.foresight.core.report.service.IReportWidgetService;
import com.inn.foresight.core.report.wrapper.ReportWidgetWrapper;

/**
 * The Class ReportWidgetRestImpl.
 */
@Path("/ReportWidget")
@Produces("application/json")
@Consumes("application/json")
@Service("ReportWidgetRestImpl")
public class ReportWidgetRestImpl extends AbstractCXFRestService<Integer, ReportWidget> {

	/**
	 * Instantiates a new report widget rest impl.
	 */
	public ReportWidgetRestImpl() {
		super(ReportWidget.class);
	}

	/** The logger. */
	private Logger logger = LogManager.getLogger(ReportWidgetRestImpl.class);

	/** The service. */
	@Autowired
	private IReportWidgetService service;

	/**
	 * Search.
	 *
	 * @param entity the entity
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("search")
	@Override
	public List<ReportWidget> search(@QueryParam("") ReportWidget entity) {
		logger.info("Searching  widget entity : {} ", entity);
		try {
			return service.search(entity);
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Find by id.
	 *
	 * @param id the id
	 * @return the report widget
	 * @throws RestException the rest exception
	 */
	@GET
	@Override
	@Path("findById")
	public ReportWidget findById(@QueryParam("id") Integer id) {
		logger.info("Finding ReportWidget by id {} ", id);
		try {
			return service.findById(id);
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Find all.
	 *
	 * @return the list
	 * @throws RestException the rest exception
	 */
	@Override
	public List<ReportWidget> findAll() {
		try {
			return service.findAll();
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Update.
	 *
	 * @param reportWidget the report widget
	 * @return the report widget
	 * @throws RestException the rest exception
	 */
	@Override
	public ReportWidget update(ReportWidget reportWidget) {
		logger.info("Updating  widget by an entity :{} ", reportWidget);
		try {
			return service.update(reportWidget);
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Removes the.
	 *
	 * @param reportWidget the report widget
	 * @return true, if successful
	 * @throws RestException the rest exception
	 */
	@Override
	public boolean remove(ReportWidget reportWidget) {
		logger.info("Removing  widget record by an entity :{}", reportWidget);
		try {
			service.remove(reportWidget);
			return true;
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	@Override
	public IGenericService<Integer, ReportWidget> getService() {
		return service;
	}

	/**
	 * Softremovereportbyid.
	 *
	 * @param id the id
	 * @return the map
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("softremovereportbyid/{id}")
	public Map<String, String> softremovereportbyid(@PathParam("id") Integer id) {
		Map<String, String> map = new HashMap<>();
		if (id != null) {
			logger.info("Going to soft  remove report by id {} ", id);
			String status;
			try {
				status = service.softDeleteWidgetById(id);
			} catch (RestException e) {
				throw new RestException(e);
			}
			map.put("status", status);
			return map;
		} else {
			logger.info("Parameters   id : {} ", id);
			throw new RestException(ForesightConstants.INVALID_PARAMETERS);
		}
	}

	/**
	 * Save CM comparision report.
	 *
	 * @param reportWidgetWrapper the report widget wrapper
	 * @return the report widget wrapper
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("saveCMComparisionReport")
	public ReportWidgetWrapper saveCMComparisionReport(ReportWidgetWrapper reportWidgetWrapper) {
		logger.info("save  saveCMComparisionReport reports by entity :{} ", reportWidgetWrapper);
		ReportWidgetWrapper wrapper = service.saveCMComparisionReport(reportWidgetWrapper);
		if (wrapper != null)
			return wrapper;
		else
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
	}

	/**
	 * Creates the report.
	 *
	 * @param reportWidgetWrappe the report widget wrapper
	 * @return the report widget wrapper
	 * @throws RestException the rest exception
	 */
	@POST
	@Path("createReport")
	public ReportWidgetWrapper createReport(ReportWidgetWrapper reportWidgetWrapper) {
		try {
			logger.info("going to createReport reports by entity : {} ", reportWidgetWrapper);
			ReportWidgetWrapper wrapper = service.createReport(reportWidgetWrapper);
			if (wrapper != null)
				return wrapper;
			else
				throw new RestException(ForesightConstants.EXCEPTION_UNABLE_TO_CREATE_REPORT);
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			throw new RestException(ForesightConstants.EXCEPTION_UNABLE_TO_CREATE_REPORT);
		}
	}

	/**
	 * Creates the.
	 *
	 * @param anEntity the an entity
	 * @return the report widget
	 * @throws RestException the rest exception
	 */
	@Override
	public ReportWidget create(ReportWidget anEntity) {
		return null;
	}

	/**
	 * Removes the by id.
	 *
	 * @param id the primary key
	 * @throws RestException the rest exception
	 */
	@GET
	@Path("removeById")
	@Override
	public void removeById(@QueryParam("id")Integer id) {
		logger.info("Removing Widget record by Id :{}" , id);
		try {
			service.removeById(id);
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		}
	}

	/**
	 * Gets the search context.
	 *
	 * @return the search context
	 */
	@Override
	public SearchContext getSearchContext() {
		return null;
	}

	@GET
	@Path("getReportWidgetByFilter")
	public List<ReportWidget> getReportWidgetByFilter(@QueryParam("reportName") String reportName,@QueryParam("reportMeasure") String reportMeasure, @QueryParam("reportType") String reportType,
			@QueryParam("date") Long date,@QueryParam("lowerLimit") Integer lowerLimit,@QueryParam("upperLimit") Integer upperLimit) {
		try {
			return service.getReportWidgetByFilter(reportName,reportMeasure, reportType, date,lowerLimit,upperLimit);
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getReportWidgetByFilter : {} ",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	@GET
	@Path("getAllReportMeasure")
	public List<String> getAllReportMeasure() {
		try {
			return service.getAllReportMeasure();
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getAllReportMeasure : {} ",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("getAllReportTypeByReportMeasure/{reportMeasure}")
	public List<String> getAllReportTypeByReportMeasure(@PathParam("reportMeasure") String reportMeasure) {
		try {
			return service.getAllReportTypeByReportMeasure(reportMeasure);
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getAllReportTypeByReportMeasure : {} ",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
	
	@GET
	@Path("getReportWidgetCountByFilter")
	public Long getReportWidgetCountByFilter(@QueryParam("reportName") String reportName,@QueryParam("reportMeasure") String reportMeasure, @QueryParam("reportType") String reportType,
			@QueryParam("date") Long date) {
		try {
			return service.getReportWidgetCountByFilter(reportName,reportMeasure, reportType, date);
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getReportWidgetCountByFilter : {} ",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}

	@GET
	@Path("getAllWidgetReportType")
	public List<String> getAllWidgetReportType() {
		try {
			return service.getAllWidgetReportType();
		} catch (RestException e) {
			throw new RestException(e.getMessage());
		} catch (Exception e) {
			logger.error("Exception in getAllWidgetReportType : {} ",Utils.getStackTrace(e));
			throw new RestException(ForesightConstants.EXCEPTION_SOMETHING_WENT_WRONG);
		}
	}
}
