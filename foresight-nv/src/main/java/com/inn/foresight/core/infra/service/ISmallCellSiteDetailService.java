package com.inn.foresight.core.infra.service;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.infra.model.SmallCellSiteDetail;
import com.inn.foresight.core.infra.wrapper.SmallCellSiteDetailWrapper;

/**
 * The Interface ISmallCellSiteDetailService.
 */
public interface ISmallCellSiteDetailService extends IGenericService<Integer, SmallCellSiteDetail> {

	SmallCellSiteDetailWrapper getSmallCellSiteDetails(String neName);

	byte[] getReportForSmallCellSiteSummary(String neName,String excelFileName);

	byte[] getReportForSmallCellSiteParameter(String neName,String excelFileName);

}
