package com.inn.foresight.module.nv.layer3.service.parse;

import java.util.List;
import java.util.Map.Entry;

import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.report.customreport.ssvt.wrapper.SiteInfoWrapper;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;

public interface INVL3UpdationService {


	public void updateImeiIntoGeoMetaData(Layer3SummaryWrapper aggrigateWrapperData,
			Entry<String, List<WOFileDetail>> woFileDetail);

	public void updateDeviceIdAndCemsdataintoGeoMetaData(Layer3SummaryWrapper aggrigateWrapperData,
			Entry<String, List<WOFileDetail>> woFileDetail);

	public void updateReportStatusIntoGWOMeta(Layer3SummaryWrapper aggrigateWrapperData,
			Entry<String, List<WOFileDetail>> woFileDetail);
	
	public void updateSectorWiseSummaryIntoGeoMetaData(List<SiteInfoWrapper> siteInfoWrapper,GenericWorkorder genericWorkOrderId);

}
