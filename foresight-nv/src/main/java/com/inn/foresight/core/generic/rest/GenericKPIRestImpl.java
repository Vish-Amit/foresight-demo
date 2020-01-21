package com.inn.foresight.core.generic.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.foresight.core.generic.service.IGenericKPIService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.wrapper.GenericCvFtWrapper;

/**
 * @author innoeye
 *
 */
@Path("/GenericKpirest")
@Produces("application/json")
@Consumes("application/json")
@Service("GenericKPIRestImpl")
public class GenericKPIRestImpl {

	Logger logger = LogManager.getLogger(GenericKPIRestImpl.class);

	/** The service. */
	@Autowired
	private IGenericKPIService service;

	@GET
	@Path("getDataForClusterviewAndFloatingtable")
	public List<GenericCvFtWrapper> getDataForClusterviewAndFloatingtable(
			@QueryParam(ForesightConstants.SWLON) Double southWestLong,
			@QueryParam(ForesightConstants.SWLAT) Double southWestLat,
			@QueryParam(ForesightConstants.NELON) Double northEastLong,
			@QueryParam(ForesightConstants.NELAT) Double northEastLat,
			@QueryParam(ForesightConstants.GEOGRAPHYTYPE) String geographyType,
			@QueryParam(ForesightConstants.MIN_DATE) String mindate,
			@QueryParam(ForesightConstants.MAX_DATE) String maxdate, 
			@QueryParam(ForesightConstants.KPIKEY) String kpi,
			@QueryParam(ForesightConstants.DATASOURCE) String datasource,
			@QueryParam(ForesightConstants.BAND_HBASE) String band,
			@QueryParam(ForesightConstants.recordtype) String recordtype,
			@QueryParam(ForesightConstants.MODULE) String module,
			@QueryParam(ForesightConstants.ISLATEST) boolean islatest,
			@QueryParam(ForesightConstants.TECHNOLOGY) String technology,
			@QueryParam(ForesightConstants.OPERATOR_KEY) String operatorName){
		logger.info(
				"SWlat : {}, SWLong: {},NElat : {},SWlon : {},geographyType : {}, kpi : {}, band : {},datasource : {}, "
				+ "mindate: {}  ,maxdate :{},recordtype: {},module : {} islatest : {} technology : {} operatorName : {}",
				southWestLat, southWestLong, northEastLat, northEastLong, geographyType, kpi, band, datasource, mindate,
				maxdate, recordtype, module, islatest,technology,operatorName);
		return service.getDataForClusterviewAndFloatingtable(southWestLat, southWestLong, northEastLat, northEastLong,
				geographyType, kpi, band, datasource, mindate, maxdate, recordtype, module, islatest,technology,operatorName);
	}

}
