package com.inn.foresight.module.nv.inbuilding.service;

import java.util.List;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.inbuilding.model.NVIBUnitResult;
import com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper;
import com.inn.foresight.module.nv.layer3.qmdlParser.wrappers.Layer3SummaryWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.NVIBResultUnitWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.WalkTestSummaryWrapper;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;

/**
 * The Interface INVIBUnitResultService.
 *
 * @author innoeye
 * date - 15-Mar-2018 7:49:24 PM
 */
public interface INVIBUnitResultService
		extends IGenericService<Integer, NVIBUnitResult> {
	
	/**
	 * Search NVIB buildings.
	 *
	 * @param swLat the sw lat
	 * @param swLng the sw lng
	 * @param neLat the ne lat
	 * @param neLng the ne lng
	 * @param technology the technology
	 * @param buildingType the building type
	 * @param zoomLevel the zoom level
	 * @return the list
	 * @throws RestException the rest exception
	 */
	List<NVIBResultWrapper> searchNVIBBuildings(Double swLat, Double swLng,
			Double neLat, Double neLng, String technology, String buildingType,
			Integer zoomLevel);

	/**
	 * Gets the wings.
	 *
	 * @param buildingId the building id
	 * @param technology the technology
	 * @return the wings
	 * @throws RestException the rest exception
	 */
	List<NVIBResultWrapper> getWings(Integer buildingId, String technology);

	/**
	 * Gets the floors.
	 *
	 * @param wingId the wing id
	 * @param technology the technology
	 * @return the floors
	 * @throws RestException the rest exception
	 */
	List<NVIBResultWrapper> getFloors(Integer wingId, String technology);

	/**
	 * Gets the units.
	 *
	 * @param floorId the floor id
	 * @param technology the technology
	 * @return the units
	 * @throws RestException the rest exception
	 */
	List<NVIBResultWrapper> getUnits(Integer floorId, String technology);

	/**
	 * Gets the recipe file name.
	 *
	 * @param unitId the unit id
	 * @param technology the technology
	 * @return the recipe file name
	 * @throws RestException the rest exception
	 */
	List<NVIBResultWrapper> getRecipeFileName(Integer unitId, String technology);

	void updateRecordIntoInbuilding(WORecipeMapping woRecipeMapping, Layer3SummaryWrapper aggrigateWrapperData, String assignTo);

	List<WalkTestSummaryWrapper> getIBUnitResultForRecipe(List<Integer> woRecipeMappingId);

	List<NVIBUnitResult> getListOfWorkOrderIdByFloorIdAndTemplateType(Integer buildingId, Integer floorId,
			List<TemplateType> templateList);

	List<GenericWorkorder> getUnitWiseWorkorder(Integer unitId, String technology);

	String getUnitWiseWorkorderCount(Integer unitId, String technology);

	List<NVIBResultUnitWrapper> getWingLevelWiseIBUnitResult(Integer id, String type,String technology);



}
