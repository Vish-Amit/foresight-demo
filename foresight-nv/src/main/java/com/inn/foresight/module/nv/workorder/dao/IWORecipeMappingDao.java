package com.inn.foresight.module.nv.workorder.dao;

import java.util.Date;
import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.core.generic.exceptions.application.DaoException;
import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.core.workorder.model.GenericWorkorder.TemplateType;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.WORecipeMappingWrapper;
import com.inn.foresight.module.nv.workorder.wrapper.WORecipeWrapper;

/** The Interface IWORecipeMappingDao. */
public interface IWORecipeMappingDao  extends IGenericDao<Integer, WORecipeMapping> {

	/**
	 * Gets the recipe wo by user.
	 *
	 * @param userId the user id
	 * @return the recipe wo by user
	 * @throws RestException the rest exception
	 */
	List<WORecipeMapping> getRecipeWoByUser(Integer userId);

	/**
	 * Gets the recipe wo by WO name.
	 *
	 * @param list the list
	 * @param templateType the template type
	 * @param modifiedTime the modified time
	 * @param workorderId the workorder id
	 * @return the recipe wo by WO name
	 * @throws RestException the rest exception
	 * @throws DaoException the dao exception
	 */
	List<WORecipeMapping> getRecipeWOByWOIdList(List<String> list, List<String> templateType, Long modifiedTime,
			Integer workorderId);

	/**
	 * Gets the WO recipe by GWO id.
	 *
	 * @param workorderId the workorder id
	 * @return the WO recipe by GWO id
	 * @throws DaoException the dao exception
	 */
	List<WORecipeMapping> getWORecipeByGWOId(Integer workorderId);

	/**
	 * Gets the WO recipe mapping by id.
	 *
	 * @param woRecipeMappingId the wo recipe mapping
	 * @return the WO recipe mapping by id
	 * @throws Exception the exception
	 */
	WORecipeMapping getWORecipeMappingById(Integer woRecipeMappingId);

	List<WORecipeMapping> geKpiAndEventString(Integer workorderId, List<Integer> recipeList);

	List<WORecipeMapping> getWorkOrderDetailByImei(String imei, Long startTime, Long endTime, TemplateType templateType);

	List<WORecipeMappingWrapper> getWORecipeMappingByGeographyAndDate(String geographyLevel,Integer geographyId, Date endDate);

	List<WORecipeWrapper> getWoIdsByforExecutedCountofRecipe();
	
	List<WORecipeMapping> getNonDeletedWOByRecipeId(Integer recipeId);
void updateWoRecipeMapping(WORecipeMapping recipeMapping);

	List<WORecipeMapping> getWoRecipeMappingByWorkOrderId(Integer workrorderId);

	List<WORecipeMapping> getWORecipeByGWOIds(List<Integer> workorderIds);

	
}
