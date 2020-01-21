package com.inn.foresight.core.maplayer.rest.impl;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import com.inn.foresight.core.maplayer.model.KPISummaryDataWrapper;
import com.inn.foresight.core.maplayer.model.KpiWrapper;
import com.inn.foresight.core.maplayer.service.IGenericKpiSummaryService;

/** The Class GenericKpiSummaryRestImpl. */
@Path("kpisummary")
@Produces("application/json")
@Consumes("application/json")
@Service("GenericKpiSummaryRestImpl")
public class GenericKpiSummaryRestImpl extends AbstractCXFRestService<Integer, Object> {

    public GenericKpiSummaryRestImpl() {
        super(Object.class);
    }
    /** The logger. */
    private Logger logger = LogManager.getLogger(GenericKpiSummaryRestImpl.class);

    @Autowired
    IGenericKpiSummaryService genericKpiSummaryService;

    @POST
    @Path("getKpiSummaryData")
    public String getKpiSummaryData(KPISummaryDataWrapper kpiSummaryDataWrapper, @QueryParam("NELat") Double northEastLat, @QueryParam("NELng") Double northEastLong,
            @QueryParam("SWLat") Double southWestLat, @QueryParam("SWLng") Double southWestLong, @QueryParam("tableName") String tableName, @QueryParam("columnName") String columnName,
            @QueryParam("geographyType") String geographyType, @QueryParam("zoomLevel") Integer zoomLevel, @QueryParam("columnFamily") String columnFamily,
            @QueryParam("identifier") String identifier) {
        logger.info("In getKpiSummaryData with params: {},{},{}", kpiSummaryDataWrapper, tableName, columnName);

        List<List<List<Double>>> polyList = null;
        KpiWrapper kpiSummarywrapper = null;
        List<String> geographyList = null;
        List<String> columnList = null;

        if (kpiSummaryDataWrapper != null) {
            polyList = kpiSummaryDataWrapper.getPolyList();
            kpiSummarywrapper = kpiSummaryDataWrapper.getKpiWrapper();
            geographyList = kpiSummaryDataWrapper.getGeographyList();
            columnList = kpiSummaryDataWrapper.getColumnList();
            return genericKpiSummaryService.getKpiSummaryData(columnList, polyList, kpiSummarywrapper, northEastLat, northEastLong, southWestLat, southWestLong, zoomLevel, tableName, columnName,
                    geographyType, columnFamily, geographyList, identifier);
        } else {
            logger.error("KPI Summary wrapper can't be null");
            throw new RestException("kpiSummaryDataWrapper can't be NULL");
        }

    }

    @Override
    public List<Object> search(Object entity) {
        return null;
    }

    @Override
    public Object findById(@NotNull Integer primaryKey) {
        return null;
    }

    @Override
    public List<Object> findAll() {
        return null;
    }

    @Override
    public Object create(@Valid Object anEntity) {
        return null;
    }

    @Override
    public Object update(@Valid Object anEntity) {
        return null;
    }

    @Override
    public boolean remove(@Valid Object anEntity) {
        return false;
    }

    @Override
    public void removeById(@NotNull Integer primaryKey) {}

    @Override
    public IGenericService<Integer, Object> getService() {
        return null;
    }

    @Override
    public SearchContext getSearchContext() {
        return null;
    }
}
