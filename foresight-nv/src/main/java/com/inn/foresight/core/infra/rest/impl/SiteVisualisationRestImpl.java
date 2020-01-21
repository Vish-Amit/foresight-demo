package com.inn.foresight.core.infra.rest.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.rest.AbstractCXFRestService;
import com.inn.core.generic.service.IGenericService;
import com.inn.core.generic.utils.ExceptionUtil;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.infra.service.INEReportService;
import com.inn.foresight.core.infra.service.ISiteVisualizationService;
import com.inn.foresight.core.infra.wrapper.NETaskDetailWrapper;
import com.inn.foresight.core.infra.wrapper.SiteLayerSelection;

@Path("SiteVisualisation")
@Produces("application/json")
@Consumes("application/json")
public class SiteVisualisationRestImpl extends AbstractCXFRestService<Integer, Object> {

	public SiteVisualisationRestImpl() {
		super(Object.class);
	    }
	private Logger logger = LogManager.getLogger(SiteVisualisationRestImpl.class);

	private static final String SITE_VISUALIZATION_REST_IMPL = "SiteVisualisationRestImpl";
	@Override
	public List<Object> search(Object entity) {
		return new ArrayList<>();
	}

	@Override
	public Object findById(@NotNull Integer primaryKey) {
		return new Object();
	}

	@Override
	public List<Object> findAll() {
		return new ArrayList<>();
	}

	@Override
	public Object create(@Valid Object anEntity) {
		return new Object();
	}

	@Override
	public Object update(@Valid Object anEntity) {
		return new Object();
	}

	@Override
	public boolean remove(@Valid Object anEntity) {
		return false;
	}

	@Override
	public void removeById(@NotNull Integer primaryKey) {
		//
	}

	@Override
	public IGenericService<Integer, Object> getService() {
		return null;
	}

	@Override
	public SearchContext getSearchContext() {
		return null;
	}
	@Autowired
	private ISiteVisualizationService iSiteVisualizationService;
	
	@Autowired
	private INEReportService ineReportService;
	
	@POST
	@Path("searchCombinedSites")
	public Map searchSites(List<SiteLayerSelection> sitelayerselection, @QueryParam("SWLng") Double southWestLong, @QueryParam("SWLat") Double southWestLat, @QueryParam("NELng") Double northEastLong,
			@QueryParam("NELat") Double northEastLat, @QueryParam("zoomLevel") Integer zoomLevel) {
		try {
			if (southWestLong != null && southWestLat != null && northEastLong != null && northEastLat != null) {
				logger.info("Going to search combined sites for zoomLevel={} ", zoomLevel);
				return iSiteVisualizationService.searchSite(sitelayerselection, southWestLong, southWestLat, northEastLong, northEastLat, zoomLevel);
			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		} catch (DaoException | RestException e) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", SITE_VISUALIZATION_REST_IMPL, e));
		} catch (Exception e) {
			logger.error("error in searchCombinedSites, err = {}", Utils.getStackTrace(e));
		}
		return new HashMap();
	}

	
	@POST
	@Path("getSiteTableView")
	public List getSiteTableView(List<SiteLayerSelection> sitelayerselection, @QueryParam("SWLng") Double southWestLong, @QueryParam("SWLat") Double southWestLat,
			@QueryParam("NELng") Double northEastLong, @QueryParam("NELat") Double northEastLat, @QueryParam("zoomLevel") Integer zoomLevel) {
		try {
			if (southWestLong != null && southWestLat != null && northEastLong != null && northEastLat != null) {
				logger.info("Going to get  Sites for Table View zoomLevel={} ", zoomLevel);
				return iSiteVisualizationService.getSitesDetailForTableView(sitelayerselection, southWestLong, southWestLat, northEastLong, northEastLat, zoomLevel);
			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		} catch (DaoException | RestException e) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", "SiteVisualizationRestImpl", e));
		} catch (Exception e) {
			logger.error("error in  Table View , err = {}", Utils.getStackTrace(e));
		}
		return new ArrayList<>();
	}
	
	
	@POST
	@Path("getKPISummaryCount")
	public Map getKPISummaryCount(List<SiteLayerSelection> sitelayerselection, @QueryParam("SWLng") Double southWestLong, @QueryParam("SWLat") Double southWestLat,
			@QueryParam("NELng") Double northEastLong, @QueryParam("NELat") Double northEastLat, @QueryParam("zoomLevel") Integer zoomLevel) {
		try {
			if (southWestLong != null && southWestLat != null && northEastLong != null && northEastLat != null) {
				logger.info("Going to get  Sites for KPI Summary View zoomLevel={} ", zoomLevel);
				logger.info("Inside site rest");
				return iSiteVisualizationService.getKPISummaryData(sitelayerselection, southWestLong, southWestLat, northEastLong, northEastLat, zoomLevel);
			} else {
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		} catch (DaoException | RestException e) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", SITE_VISUALIZATION_REST_IMPL, e));
		} catch (Exception e) {
			logger.error("error in  Table View , err = {}", Utils.getStackTrace(e));
		}
		return new HashMap<>();
	}
	
	@POST
	@Path("searchNEDetail")
	public List searchNEDetail(List<SiteLayerSelection> siteLayerSelectionList) {
		try {
			logger.info("Going to search NE Details for input parameters.");
			if (siteLayerSelectionList != null && !siteLayerSelectionList.isEmpty()) {
				return iSiteVisualizationService.searchNEDetail(siteLayerSelectionList);
			}
		} catch (Exception exception) {
			logger.error("Error in searching NEDetail. Message : {}",exception.getMessage());
		}
		return new ArrayList();
	}
	
	@POST
	@Path("searchNetworkElementDetails")
	public List searchNetworkElementDetails(List<SiteLayerSelection> siteLayerSelectionList) {
		try {
			logger.info("Going to search NetworkElement Details for input parameters.");
			if (siteLayerSelectionList != null && !siteLayerSelectionList.isEmpty()) {
				return iSiteVisualizationService.searchNetworkElementDetails(siteLayerSelectionList);
			}
		} catch (Exception exception) {
			logger.error("Error in searching NetworkElement Details. Message : {}",exception.getMessage());
		}
		return new ArrayList();
	}
	
	@POST
	@Path("sectorPropertiesDetails")
	public List sectorPropertiesDetails(List<SiteLayerSelection> siteLayerSelectionList) {
		try {
			logger.info("inside sectorPropertiesDetails");
			if (siteLayerSelectionList != null && !siteLayerSelectionList.isEmpty()) {
				return iSiteVisualizationService.sectorPropertiesDetails(siteLayerSelectionList);
			}
		} catch (Exception exception) {
			logger.error("Error in searching NetworkElement Details. Message : {}",exception.getMessage());
		}
		return new ArrayList();
	}

	
	@POST
	@Path("getSiteSummaryOverviewByBand")
	public  List<Map> getSiteSummaryOverviewByBand(SiteLayerSelection siteLayerSelection) {
		logger.info("get Site Overview Detail Data.");
		try{
			if(siteLayerSelection != null)
				return iSiteVisualizationService.getSiteSummaryOverviewByBand(siteLayerSelection);
			else{
				throw new RestException(ForesightConstants.EXCEPTION_INVALID_PARAMS);
			}
		}catch(RestException exception){
			logger.error("Unable to get SiteOverviewDetails Exception  {} " , Utils.getStackTrace(exception));
			throw new RestException("No Data Found.");
		}catch(Exception exception){
			logger.error("Unable to get SiteOverviewDetails Exception {} " , Utils.getStackTrace(exception));
			throw new RestException("No Data Found.");
		}
	}
	@POST
	@Path("getSiteSummaryAntennaParametersByBand")
	public  List<Map> getSiteSummaryAntennaParametersByBand(SiteLayerSelection siteLayerSelection) {
		logger.info("get Site Summary Antenna Parameters .");
		try{
			if(siteLayerSelection != null)
				return iSiteVisualizationService.getSiteSummaryAntennaParametersByBand(siteLayerSelection);
			else{
				throw new RestException(ForesightConstants.EXCEPTION_INVALID_PARAMS);
			}
		}catch(RestException exception){
			logger.error("Unable to get Site Antenna Parameters Exception {} " , Utils.getStackTrace(exception));
			throw new RestException("No Data Found.");
		}catch(Exception exception){
			logger.error("Unable to get Site Antenna Parameters Exception {} " , Utils.getStackTrace(exception));
			throw new RestException("No Data Found.");
		}
	}
	@POST
	@Path("getSiteOverviewData")
	public Map<String,Map> getSiteOverviewData(SiteLayerSelection siteLayerSelection) {
		logger.info("get Site Overview Detail Data .");
		try{
			if(siteLayerSelection != null)
				return iSiteVisualizationService.getSiteOverviewData(siteLayerSelection);
			else{
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		}catch (RestException restException) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", "SiteVisualizationRestImpl", restException));
		}catch(Exception exception){
			logger.error("Unable to get SiteOverviewDetails Exception {} " , Utils.getStackTrace(exception));
			throw new RestException("No Data Found.");
		}
	}
	@POST
	@Path("getSiteGeographicDetailData")
	public Map getSiteGeographicDetailData(SiteLayerSelection siteLayerSelection) {
		logger.info("Going to  SiteGeographicDetail data. ");
		try{
			if(siteLayerSelection != null)
				return iSiteVisualizationService.getSiteGeographicalData(siteLayerSelection);
			else{
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		}catch (RestException restException) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", "SiteVisualizationRestImpl", restException));
		}catch(Exception exception){
			logger.error("Unable to get SiteGeographicDetail data Exception {} " , Utils.getStackTrace(exception));
			throw new RestException("No Data Found.");
		}
	}
	
	@POST
	@Path("getNETaskDetailData")
	public List<Map> getNETaskDetailData(SiteLayerSelection siteLayerSelection) {
		logger.info("Into the method to get NE Task Detail Data.");
		try {
			if(siteLayerSelection != null) {
		      return iSiteVisualizationService.getNETaskDetailData(siteLayerSelection);
			}else{
				throw new RestException(ForesightConstants.INVALID_PARAMETERS);
			}
		}
		catch(RestException re) {
			throw new RestException("No Data Found."); }
	}
	
	@POST
	@Path("generateSitePropertyReport")
	public Map<String, String> generateSitePropertyReport(List<Map<String,SiteLayerSelection>> list,@QueryParam("name") String name,@QueryParam("type") String type) {
		logger.info("Into the method for creating the report for sector property");
		try {
			return ineReportService.generateSitePropertyReport(list, name, type);
		} catch (RestException e) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", "SiteVisualizationRestImpl", e));
		} catch (Exception e) {
			logger.error("Error in creating the report {}  ", e.getStackTrace());
			throw new RestException("Unable to generate Site Property Report.");
		}
	}
	
	@POST
	@Path("generateSectorPropertyReport")
	public Map<String, String> generateSectorPropertyReport(List<Map<String,SiteLayerSelection>> list,@QueryParam("name") String name,@QueryParam("type") String type) {
		logger.info("Into the method for creating the report for sector property");
		try {
			return ineReportService.generateSectorPropertyReport(list, name, type);
		} catch (RestException e) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", "SiteVisualizationRestImpl", e));
		} catch (Exception e) {
			logger.error("Error in creating the report {} ", e.getStackTrace());
			throw new RestException("Unable to generate Sector Property Report.");
		}
	}
	
	@POST
	@Path("getCellsDataForRAN")
	public Map getCellsDataForRAN(SiteLayerSelection siteLayerSelection) {
		logger.info("Inside method getCellsDataForRAN : siteLayerSelection={}",siteLayerSelection);
		try {
			return iSiteVisualizationService.getCellsDataForRAN(siteLayerSelection);
		} catch (RestException e) {
			throw new RestException(ExceptionUtil.generateExceptionCode("Rest", "SiteVisualizationRestImpl", e));
		} catch (Exception e) {
			logger.error("Error in getCellsDataForRAN {} ", e.getStackTrace());
			throw new RestException("Unable to get Cells Data For RAN.");
		}
	}

	
	
}
