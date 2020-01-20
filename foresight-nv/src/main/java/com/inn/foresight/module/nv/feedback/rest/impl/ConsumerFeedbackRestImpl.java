package com.inn.foresight.module.nv.feedback.rest.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.module.nv.feedback.model.ConsumerFeedback;
import com.inn.foresight.module.nv.feedback.rest.IConsumerFeedBackRest;
import com.inn.foresight.module.nv.feedback.service.IConsumerFeedbackService;
import com.inn.foresight.module.nv.feedback.wrapper.CustomFeedbackRequestWrapper;

/**
 * The Class ConsumerFeedbackRestImpl.
 * 
 * @author innoeye
 */

@Path("/ConsumerFeedback")
@Service("ConsumerFeedbackRestImpl")
public class ConsumerFeedbackRestImpl implements IConsumerFeedBackRest {

	/** The logger. */
	private Logger logger = LogManager.getLogger(ConsumerFeedbackRestImpl.class);

	@Autowired
	private IConsumerFeedbackService consumerFeedbackService;

	/** 
	 * @param encryptedString
	 * @return plain text response
	 * @throws RestException
	 */
	@Override
	@POST
	@Path("createFeedback")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.TEXT_PLAIN)
	public String createFeedback(String encryptedString) {
		if (encryptedString != null) {
			try {
				ConsumerFeedback feedback = consumerFeedbackService.createFeedback(encryptedString);
				if (feedback != null) {
					return ForesightConstants.SUCCESS_JSON;
				} else {
					return ForesightConstants.FAILURE_JSON;
				}
			} catch (RestException e) {
				logger.error("error in createFeedback, error msg = {}", ExceptionUtils.getStackTrace(e));
				return ExceptionUtils.getMessage(e);
			}
		}
		return ForesightConstants.EXCEPTION_INVALID_PARAMS;
	}
	
	@Override
	@GET
	@Path("createCustomFeedback")
	@Produces(MediaType.APPLICATION_JSON)
	public String createCustomFeedback() {
		try {
			return consumerFeedbackService.createCustomFeedback();
		} catch (RestException e) {
			logger.error("error in createFeedback, error msg = {}", ExceptionUtils.getStackTrace(e));
			return ExceptionUtils.getMessage(e);
		}
	}

	@GET
	@Path("getFeedbackAvgRating")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public Response getFeedbackAvgRating(@QueryParam("fromDate") Long fromDate, @QueryParam("toDate") Long toDate, @QueryParam("level") String level,
			@QueryParam("geographyId") Integer geographyId) {
		try {
			if(fromDate == null || toDate == null) {
				return Response.ok(ForesightConstants.INVALID_PARAMETERS_JSON).build();
			}
			
			return Response.ok(consumerFeedbackService.getFeedbackAvgRating(new Date(fromDate), new Date(toDate),level, geographyId)).build();
		}catch(Exception e) {
			logger.error("Error while get feedback rating, error msg = {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}
	
	@GET
	@Path("getFeedbackRatingWithCount")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public Response getFeedbackRatingWithCount(@QueryParam("date") Long date, @QueryParam("level") String level,
			@QueryParam("geographyId") Integer geographyId) {
		try {
			if(date == null) {
				return Response.ok(ForesightConstants.INVALID_PARAMETERS_JSON).build();
			}
			
			return Response.ok(consumerFeedbackService.getFeedbackRatingWithCount(new Date(date), level, geographyId)).build();
		}catch(Exception e) {
			logger.error("Error while get feedback Rating with Count, error msg = {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}
	
	@GET
	@Path("getDateWiseTestArea")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public Response getDateWiseTestArea(@QueryParam("type") String type, @QueryParam("fromDate") Long fromDate, @QueryParam("toDate") Long toDate, @QueryParam("level") String level,
			@QueryParam("geographyId") Integer geographyId) {
		try {
			if(type == null || fromDate == null || toDate == null) {
				return Response.ok(ForesightConstants.INVALID_PARAMETERS_JSON).build();
			}
			
			return Response.ok(consumerFeedbackService.getDateWiseTestArea(type, new Date(fromDate), new Date(toDate), level, geographyId)).build();
		}catch(Exception e) {
			logger.error("Error while get DateWise TestArea, error msg = {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}
	
	@GET
	@Path("getDateWiseFeedback")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public Response getDateWiseFeedback(@QueryParam("type") String type, @QueryParam("fromDate") Long fromDate, @QueryParam("toDate") Long toDate, @QueryParam("level") String level,
			@QueryParam("geographyId") Integer geographyId) {
		try {
			if(type == null || fromDate == null || toDate == null) {
				return Response.ok(ForesightConstants.INVALID_PARAMETERS_JSON).build();
			}
			return Response.ok(consumerFeedbackService.getDateWiseFeedback(type, new Date(fromDate), new Date(toDate), level, geographyId)).build();
		}catch(Exception e) {
			logger.error("Error while get dateWise feedback, error msg = {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}
	
	@GET
	@Path("getVoiceDataCvgFeedback")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public Response getVoiceDataCvgFeedback(@QueryParam("fromDate") Long fromDate, @QueryParam("toDate") Long toDate, @QueryParam("level") String level,
			@QueryParam("geographyId") Integer geographyId) {
		try {
			if(fromDate == null || toDate == null) {
				return Response.ok(ForesightConstants.INVALID_PARAMETERS_JSON).build();
			}
			
			return Response.ok(consumerFeedbackService.getVoiceDataCvgFeedback(new Date(fromDate), new Date(toDate),level, geographyId)).build();
		}catch(Exception e) {
			logger.error("Error while get dateWise voiceCount, error msg = {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}
	
	@GET
	@Path("getTopFeedbackLocations")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public Response getTopFeedbackLocations(@QueryParam("fromDate") Long fromDate, @QueryParam("toDate") Long toDate,
			@QueryParam("lLimit") Integer lLimit, @QueryParam("uLimit") Integer uLimit, @QueryParam("level") String level,
			@QueryParam("geographyId") Integer geographyId) {
		try {
			if(fromDate == null || toDate == null) {
				return Response.ok(ForesightConstants.INVALID_PARAMETERS_JSON).build();
			}
			return Response.ok(consumerFeedbackService.getTopFeedbackLocations(new Date(fromDate), new Date(toDate), lLimit, uLimit, level, geographyId)).build();
		}catch(Exception e) {
			logger.error("Error while get Top Feedback Locations, error msg = {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}
	
	@GET
	@Path("getFeedbackLayerData")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public Response getFeedbackLayerData(@QueryParam("zoomLevel") Integer zoom, @QueryParam("NELat") Double nELat, @QueryParam("NELng") Double nELng,
			@QueryParam("SWLat") Double sWLat, @QueryParam("SWLng") Double sWLng, @QueryParam("fromDate") String fromDate,
			@QueryParam("toDate") String toDate,@QueryParam("testArea") String testArea,@QueryParam("feedbackType") String feedbackType,
			@QueryParam("band") String band,@QueryParam("kpi") String kpi,@QueryParam("fromStarRating") Integer fromStarRating
			,@QueryParam("toStarRating") Integer toStarRating,@QueryParam("searchType") String searchType) {
		try {
			if ((fromDate == null || fromDate.equalsIgnoreCase("null"))
					&& (toDate == null || toDate.equalsIgnoreCase("null"))) {
				Date date = new Date();
				String df = ForesightConstants.DATE_FORMAT_DDMMYY;
				SimpleDateFormat sfd = new SimpleDateFormat(df);
				fromDate = sfd.format(date);
				toDate = sfd.format(date);
			}
			if (Utils.hasValidValue(fromDate) || Utils.hasValidValue(toDate)) {
				return Response.ok(consumerFeedbackService.getLayerData(zoom, nELat, nELng, sWLat, sWLng, fromDate,
						toDate, testArea, feedbackType, band, kpi, fromStarRating, toStarRating, searchType)).build();
			} else {
				return Response.ok(ForesightConstants.INVALID_PARAMETERS_JSON).build();
			}
		} catch (Exception e) {
			logger.error("Error while getting Feedback Layer Data = {}", ExceptionUtils.getStackTrace(e));
			return Response.ok(ForesightConstants.FAILURE_JSON).build();
		}
	}

	@GET
	@Path("getLayerDataForECare")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public List<Map<String, Object>> getLayerDataForECare(@QueryParam("zoomLevel") Integer zoom, @QueryParam("NELat") Double nELat,
			@QueryParam("NELng") Double nELng, @QueryParam("SWLat") Double sWLat, @QueryParam("SWLng") Double sWLng,
			@QueryParam("fromDate") String fromDate, @QueryParam("toDate") String toDate, @QueryParam("band") String band,
			@QueryParam("kpi") String kpi, @QueryParam("searchType") String searchType, @QueryParam("locationType") String locationType,
			@QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType) {
		if (Utils.hasValue(sWLat) && Utils.hasValue(nELat) && Utils.hasValue(sWLng) && Utils.hasValue(nELng)
				&& Utils.checkForValueInString(kpi)) {
			return consumerFeedbackService.getLayerDataForECare(zoom, nELat, nELng, sWLat, sWLng, fromDate, toDate, band, kpi, searchType,
					locationType, orderBy, orderType);
		} else {
			throw new RestException(ForesightConstants.INVALID_PARAMETERS_JSON);
		}
	}
		
	@GET
	@Path("getDashboardDataForEcare")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public Response getDashboardDataForEcare(@QueryParam("level") String level,
			@QueryParam("geographyId") Integer geographyId,
			@QueryParam("date") String date,@QueryParam("feedbackType") String feedbackType) {
		if (Utils.hasValidValue(date) && Utils.hasValidValue(level) 
				&& Utils.hasValue(geographyId) && Utils.hasValidValue(feedbackType)) {
			return Response.ok(consumerFeedbackService.getDashboardDataForECare(geographyId,level,date,feedbackType)).build();
		
		} else {
			throw new RestException(ForesightConstants.INVALID_PARAMETERS_JSON);
		}
	}

	@GET
	@Path("getCustomFeedbackData")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Override
	public List<Object> getCustomFeedbackData(@QueryParam("zoomLevel") Integer zoom, @QueryParam("NELat") Double nELat,
			@QueryParam("NELng") Double nELng, @QueryParam("SWLat") Double sWLat, @QueryParam("SWLng") Double sWLng,
			@QueryParam("fromDate") String fromDate, @QueryParam("toDate") String toDate, @QueryParam("band") String band,
			@QueryParam("kpi") String kpi, @QueryParam("searchType") String searchType, @QueryParam("locationType") String locationType,
			@QueryParam("orderBy") String orderBy, @QueryParam("orderType") String orderType) {
		if (Utils.hasValue(sWLat) && Utils.hasValue(nELat) && Utils.hasValue(sWLng) && Utils.hasValue(nELng) && Utils.checkForValueInString(kpi)
				&& Utils.hasValue(zoom)) {

			CustomFeedbackRequestWrapper requestWrapper = settingRequestWrapper(zoom, nELat, nELng, sWLat, sWLng, fromDate, toDate, band, kpi,
					searchType, locationType, orderBy, orderType);

			return consumerFeedbackService.getCustomFeedbackData(requestWrapper);
		} else {
			throw new RestException(ForesightConstants.INVALID_PARAMETERS_JSON);
		}
	}

	private CustomFeedbackRequestWrapper settingRequestWrapper(Integer zoom, Double nELat, Double nELng, Double sWLat, Double sWLng, String fromDate,
			String toDate, String band, String kpi, String searchType, String locationType, String orderBy, String orderType) {
		CustomFeedbackRequestWrapper requestWrapper = new CustomFeedbackRequestWrapper();
		requestWrapper.setZoomLevel(zoom);
		requestWrapper.setnELat(nELat);
		requestWrapper.setnELng(nELng);
		requestWrapper.setsWLat(sWLat);
		requestWrapper.setsWLng(sWLng);
		requestWrapper.setFromDate(fromDate);
		requestWrapper.setToDate(toDate);
		requestWrapper.setBand(band);
		requestWrapper.setKpi(kpi);
		requestWrapper.setSearchType(searchType);
		requestWrapper.setLocationType(locationType);
		requestWrapper.setOrderBy(orderBy);
		requestWrapper.setOrderType(orderType);
		return requestWrapper;
	}

}
