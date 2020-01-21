package com.inn.foresight.core.infra.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.core.infra.model.RANDetail;
import com.inn.foresight.core.infra.wrapper.NetworkElementWrapper;
import com.inn.foresight.core.infra.wrapper.SiteSummaryOverviewWrapper;
import com.inn.foresight.core.infra.wrapper.WifiWrapper;

public interface IRANDetailDao extends IGenericDao<Integer, RANDetail> {

    List<Tuple> getCellsDataForVisualisation(Map<String, Double> viewPortMap, Map<String, List<Map>> filters,
            Map<String, List<String>> projection, List distinctEntity);

    List<Tuple> getCellsDataForRAN(Map<String, Double> viewPortMap, Map<String, List<Map>> filters,
            Map<String, List<String>> projection, Boolean distinct) throws DaoException;

    List<NetworkElementWrapper> getMacroSiteDetailByNameAndType(String neName, String neType);

    SiteSummaryOverviewWrapper getSiteSummaryOverviewByBand(String neName, String neFrequency, String neStatus);

    SiteSummaryOverviewWrapper getSiteSummaryOverviewByBandForSecondCarrier(String neName, String neFrequency,
            String neStatus);

    SiteSummaryOverviewWrapper getAntennaParametersByBand(String neName, String neFrequency, String neStatus);

    RANDetail searchByRrhSerialNo(String rrhSerialNo);

    List<Object[]> getBtsDetailByCgi(String cgi);

    List<Object[]> findAllOnairSites(String neStatus, List<String> neTypeList, List<String> domainList);

    List<Object[]> getBtsNameByEcgi(String ecgi);

	RANDetail getRanDetailByNename(String nename);
	
	WifiWrapper getWIFIAPDetail(String macAddress);

	List<RANDetail> getSectorPropertyData(String neName, String neFrequency, String neStatus, String neType);

}
