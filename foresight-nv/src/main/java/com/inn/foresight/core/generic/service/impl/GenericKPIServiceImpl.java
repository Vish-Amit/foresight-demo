package com.inn.foresight.core.generic.service.impl;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inn.foresight.core.generic.dao.IGenericKPIDao;
import com.inn.foresight.core.generic.service.IGenericKPIService;
import com.inn.foresight.core.generic.utils.ForesightConstants;
import com.inn.foresight.core.generic.utils.Utils;
import com.inn.foresight.core.generic.wrapper.GenericCvFtWrapper;
import com.inn.foresight.core.maplayer.service.IGenericMapService;
import com.inn.foresight.core.maplayer.utils.GenericMapUtils;

@Service("GenericKPIServiceImpl")
public class GenericKPIServiceImpl implements IGenericKPIService {

    private Logger logger = LogManager.getLogger(GenericKPIServiceImpl.class);

    @Autowired
    private IGenericKPIDao kpidao;

    @Autowired
    private IGenericMapService mapService;

	@Override
	public List<GenericCvFtWrapper> getDataForClusterviewAndFloatingtable(Double southWestLat, Double southWestLong, Double northEastLat, Double northEastLong, String geographyType, String kpi,
			String band, String datasource, String mindate, String maxdate, String environment, String module, boolean isLatest,String technology,String operatorName) {
		try {
			if (southWestLat != null && southWestLong != null && northEastLat != null && northEastLong != null && kpi != null && band != null && mindate != null && maxdate != null
					&& geographyType != null) {
				List<String> daterange = new ArrayList<>();

				if (Utils.isValidString(mindate) && Utils.isValidString(maxdate)) {
					daterange = Utils.getFormattedDateForTable(mindate, maxdate);
				}
				
				String geographyjson = getGeographyNamebylatlon(geographyType, ForesightConstants.FIFTEEN, northEastLat, northEastLong, southWestLat, southWestLong).replaceAll("[\\[\\](){}]", "");

				Type listType = new TypeToken<List<String>>() {
				}.getType();

				List<String> geographyList = new Gson().fromJson(ForesightConstants.OPEN_SQUARE_BRACKET + geographyjson + ForesightConstants.CLOSED_SQUARE_BRACKET, listType);
				Set<String> geographyNameSet = geographyList.stream().collect(Collectors.toSet());

				List<GenericCvFtWrapper> listOfData = kpidao.getDataForClusterviewAndFloatingTable(band, environment, datasource, daterange, module, isLatest, kpi, geographyType, geographyNameSet,technology,operatorName);

				tagDateAndBand(listOfData, band, mindate, kpi);
				logger.info("listOfData size -> {}", listOfData.size());
				return listOfData;
			}
		} catch (ParseException e) {
			logger.info("Error in Service Method --> {}", ExceptionUtils.getStackTrace(e));
		}
		return new ArrayList<>();
	}

	private void tagDateAndBand(List<GenericCvFtWrapper> listOfData, String band, String date, String kpi) {
		for (GenericCvFtWrapper genericCvFtWrapper : listOfData) {
			genericCvFtWrapper.setBand(band);
			genericCvFtWrapper.setDate(date);
			genericCvFtWrapper.setKpiName(kpi);
			genericCvFtWrapper.setValidevents(genericCvFtWrapper.getValidevents() == null ? 0 : genericCvFtWrapper.getValidevents());
		}
	}

	public String getGeographyNamebylatlon(String geographyType, Integer zoom, Double nELat, Double nELng, Double sWLat, Double sWLng) {
		if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L4fk)) {
			return mapService.getBoundaryData(GenericMapUtils.getGeoColumnList(),GenericMapUtils.GEOGRAPHY_TABLE_NAME, nELat, nELng, sWLat, sWLng, false,GenericMapUtils.L4_TYPE,null);
		} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L3fk)) {
		    return mapService.getBoundaryData(GenericMapUtils.getGeoColumnList(),GenericMapUtils.GEOGRAPHY_TABLE_NAME, nELat, nELng, sWLat, sWLng, false,GenericMapUtils.L3_TYPE,null);
		} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L2fk)) {
            return mapService.getBoundaryData(GenericMapUtils.getGeoColumnList(),GenericMapUtils.GEOGRAPHY_TABLE_NAME, nELat, nELng, sWLat, sWLng, false,GenericMapUtils.L2_TYPE,null);
		} else if (geographyType.equalsIgnoreCase(ForesightConstants.GEOGRAPHY_L1fk)) {
            return mapService.getBoundaryData(GenericMapUtils.getGeoColumnList(),GenericMapUtils.GEOGRAPHY_TABLE_NAME, nELat, nELng, sWLat, sWLng, false,GenericMapUtils.L1_TYPE,null);
		}
		return null;
	}

}
