package com.inn.foresight.core.releasenote.rest;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.ws.rs.core.Context;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.releasenote.service.ReleaseNoteService;
import com.inn.foresight.core.releasenote.utils.ReleaseNoteWrapper;

@Path("/ReleaseNote")
@Produces("application/json")
@Consumes("application/json")
@Service("ReleaseNoteServiceRestImpl")
public class ReleaseNoteRestImpl  {

    /** The logger. */
    private Logger logger = LogManager.getLogger(ReleaseNoteRestImpl.class);

    
    @Autowired
    private ReleaseNoteService service;

    /** The context. */
    @Context
    private SearchContext context;

    @GET
    @Path("getAllReleaseNote")
    public List<ReleaseNoteWrapper> getAllReleaseNote(@QueryParam("searchText") String searchText) {
        try {
            logger.info("Inside getAllReleaseNote in rest");
            return service.getAllReleaseNote(searchText);
        } catch (RestException e) {
            logger.error("Error : {} ", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
    @POST
    @Path("uploadReleaseNote")
    @Consumes("multipart/form-data")
    public String uploadReleaseNote(@Multipart("file") InputStream inputStream,
                                      @QueryParam("name") String name,@QueryParam("version") String version,@QueryParam("comment") String comment,@QueryParam("fileName") String fileName,@QueryParam("releaseDate") long releaseDate) {
        try {
            return service.uploadReleaseNote(inputStream, name,version,comment,fileName,releaseDate);
        } catch (Exception e) {
            logger.error("Exception while uploadReleaseNote {}", Utils.getStackTrace(e));
            return null;
        }
    }
    @POST
    @Path("deleteReleaseNoteByIdList")
    public String deleteReleaseNoteByIdList(List<Integer> noteId) {
        try {
            logger.info("Inside deleteReleaseNoteByIdList in rest");
            return service.deleteReleaseNoteByIdList(noteId);
        } catch (RestException e) {
            logger.error("Error : {} ", ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
    
    @GET
    @Path("getDateOnServer/{timestamp}")
    public Map<String, Object> getDateOnServer(@PathParam("timestamp") Long timestamp) {
    	Map<String, Object> obj = new HashMap<String, Object>();
    	Date serverDate = new Date(timestamp);
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY hh:mm:ss"); 
    	obj.put("serverFormattedDate",simpleDateFormat.format(serverDate));
    	obj.put("serverDateTimestamp", serverDate.getTime());
    	return obj;
    }
    
}
