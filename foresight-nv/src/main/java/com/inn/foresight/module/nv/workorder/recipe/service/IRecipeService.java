package com.inn.foresight.module.nv.workorder.recipe.service;

import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.security.access.prepost.PreAuthorize;

import com.inn.core.generic.exceptions.application.RestException;
import com.inn.foresight.module.nv.workorder.model.WOFileDetail;
import com.inn.foresight.module.nv.workorder.model.WORecipeMapping;
import com.inn.foresight.module.nv.workorder.recipe.model.Recipe;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.FTPDetailsWrapper;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.RecipeWrapper;

/** The Interface IRecipeService. */
public interface IRecipeService {

	/**
	 * Find all recipe.
	 *
	 * @param lLimit the l limit
	 * @param uLimit the u limit
	 * @return the list
	 * @throws RestException the rest exception
	 */
	List<RecipeWrapper> findAllRecipe(Integer lLimit, Integer uLimit);

	/**
	 * Creates the recipe.
	 *
	 * @param recipe the recipe
	 * @return the recipe
	 * @throws RestException the rest exception
	 */
	Recipe createRecipe(Recipe strRecipe);

	/**
	 * Update recipe.
	 *
	 * @param recipe the recipe
	 * @return the recipe
	 * @throws RestException the rest exception
	 */
	String updateRecipe(Recipe strRecipe);

	/**
	 * Returns count of all Recipes in Db.
	 *
	 *
	 * @return Long Total Recipe count in DB
	 * @throws RestException the rest exception
	 */
	Long getTotalRecipeCount();

	/**
	 * Returns  List of Recipe by Category.
	 * @param category 
	 *
	 *
	 * @return  List of Recipe By category and By User
	 * @throws RestException
	 *             the RestException
	 */
	List<RecipeWrapper> getRecipeByCategory(List<String> category);

	String deleteByPk(List<Integer> strRecipeIds);
	
	String deleteRecipeById(Integer recipeId);
	
	String checkRecipeMappedWithWO(Integer recipeId);

	/**
	 * Update Status to COMPLETED of Recipe corresponds to provided WORecipeMapping Id.
	 * @param woRecipeMappingId : WORecipeMapping Id
	 * @return Success Message if Status updated, Failure Message otherwise
	 */
	String completeRecipe(Integer woRecipeMappingId);


	/**
	 * Update Status to INPROGRESS of Recipe corresponds to provided WORecipeMapping Id.
	 * @param woRecipeMappingId : WORecipeMapping Id
	 * @return Success Message if Status updated, Failure Message otherwise
	 */
	String startRecipe(Integer woRecipeMappingId, Boolean isUserAuthorized, String taskId);

	/**
	 * Creates the recipe for Mobile.
	 *
	 * @param string Encrypted Recipe json
	 * @return the recipe
	 * @throws RestException the rest exception
	 */
	Recipe createRecipeForMobile(String strRecipe);
	
	/**
	 * Get WO Files By Recipe Mapping Id.
	 *
	 * @param recipeMappingId recipe mapping id
	 * @return List of WO file details
	 */
	List<WOFileDetail> getWOFilesByRecipeMappingId(Integer recipeMappingId);
	
	/**
	 * Get WO Details Zip File.
	 *
	 * @param fileName file name to download.
	 * @return byte[] for file.
	 */
	byte[] getWODetailsZipFile(String filePath);

	List<Recipe> findAllByRecipeId(List<String> recipeIdList);

	FTPDetailsWrapper validateFTPConnection(FTPDetailsWrapper ftpDetailsWrapper);



	Response reopenRecipeById(Integer woRecipeId, Integer workorderId,boolean isToDeleteFile);

	boolean isRecipeCompleteInProgressRecipe(WORecipeMapping woRecipeMapping);


}
