package com.inn.foresight.module.nv.inbuilding.dao;

import java.util.Date;
import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.inbuilding.model.NVIBUnitResult;
import com.inn.foresight.module.nv.inbuilding.wrapper.NVIBResultWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.NVIBResultUnitWrapper;
import com.inn.foresight.module.nv.report.wrapper.inbuilding.WalkTestSummaryWrapper;

/**
 * The Interface INVIBUnitResultDao.
 *
 * @author innoeye
 * date - 15-Mar-2018 7:44:54 PM
 */
public interface INVIBUnitResultDao
		extends IGenericDao<Integer, NVIBUnitResult>  {

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
	 * @throws DaoException the dao exception
	 */
	List<NVIBResultWrapper> searchNVIBBuildings(Double swLat,
			Double swLng, Double neLat, Double neLng, String technology,
			String buildingType, Integer zoomLevel);

	/**
	 * Gets the wings.
	 *
	 * @param buildingId the building id
	 * @param technology the technology
	 * @return the wings
	 * @throws DaoException the dao exception
	 */
	List<NVIBResultWrapper> getWings(Integer buildingId,
			String technology);

	/**
	 * Gets the floors.
	 *
	 * @param wingId the wing id
	 * @param technology the technology
	 * @return the floors
	 * @throws DaoException the dao exception
	 */
	List<NVIBResultWrapper> getFloors(Integer wingId, String technology);

	/**
	 * Gets the units.
	 *
	 * @param floorId the floor id
	 * @param technology the technology
	 * @return the units
	 * @throws DaoException the dao exception
	 */
	List<NVIBResultWrapper> getUnits(Integer floorId, String technology);

	/**
	 * Gets the recipe file name.
	 *
	 * @param unitId the unit id
	 * @param technology the technology
	 * @return the recipe file name
	 * @throws DaoException the dao exception
	 */
	List<NVIBResultWrapper> getRecipeFileName(Integer unitId, String technology);

	/**
	 * Gets the list of work order id by floor id and template type.
	 *
	 * @param buildingId the building id
	 * @param floorId the floor id
	 * @param templateList the template list
	 * @return the list of work order id by floor id and template type
	 */
	List<NVIBUnitResult> getListOfWorkOrderIdByFloorIdAndTemplateType(Integer buildingId, Integer floorId,
			List<TemplateType> templateList);

	/**
	 * Gets the IB unit result for recipe.
	 *
	 * @param woRecipeMappingId the wo recipe mapping id
	 * @return the IB unit result for recipe
	 */
	List<WalkTestSummaryWrapper> getIBUnitResultForRecipe(List<Integer> woRecipeMappingId);

	/**
	 * Insert unit result.
	 *
	 * @param nvResult the nv result
	 */
	void insertUnitResult(NVIBUnitResult nvResult);

	/**
	 * Gets the unit wise workorder count.
	 *
	 * @param unitId the unit id
	 * @param technology the technology
	 * @return the unit wise workorder count
	 * @throws Exception the exception
	 */
	Long getUnitWiseWorkorderCount(Integer unitId, String technology);


	/**
	 * Gets the unit wise workorder.
	 *
	 * @param unitId the unit id
	 * @param technology the technology
	 * @return the unit wise workorder
	 * @throws Exception the exception
	 */
	List<GenericWorkorder> getUnitWiseWorkorder(Integer unitId, String technology);


	/**
	 * Gets the building in fo by wo id.
	 *
	 * @param woId the wo id
	 * @return the building in fo by wo id
	 */
	NVIBResultWrapper getBuildingInFoByWoId(Integer woId);


	/**
	 * Gets the wing level wise IB unit result.
	 *
	 * @param id the id
	 * @return the wing level wise IB unit result
	 */
	List<NVIBResultUnitWrapper> getWingLevelWiseIBUnitResult(Integer id);

	/**
	 * Gets the wing level wise IB unit result.
	 *
	 * @param id the id
	 * @param type the type
	 * @param technology the technology
	 * @return the wing level wise IB unit result
	 */
	List<NVIBResultUnitWrapper> getWingLevelWiseIBUnitResult(Integer id, String type,String technology);

	/**
	 * Gets the NVIB unit result by wo recipe id.
	 *
	 * @param worecipeMappingId the worecipe mapping id
	 * @return the NVIB unit result by wo recipe id
	 */
	NVIBUnitResult getNVIBUnitResultByWoRecipeId(Integer worecipeMappingId) ;

	List<NVIBResultWrapper> getNVIbResultByBuildingId(Integer buildingId, Integer floorId, Date startDate, Date endDate);

	NVIBResultWrapper getKpiAvgByFloorId(Integer floorId, Integer band, Date startTime, Date endTime);

	List<NVIBResultWrapper> getDateWiseTestResultByBuildingId(Integer buildingId, Integer floorId);

}
