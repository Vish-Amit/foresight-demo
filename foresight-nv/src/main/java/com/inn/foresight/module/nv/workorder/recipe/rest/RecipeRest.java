package com.inn.foresight.module.nv.workorder.recipe.rest;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;

import com.inn.foresight.module.nv.workorder.recipe.model.Recipe;
import com.inn.foresight.module.nv.workorder.recipe.wrapper.FTPDetailsWrapper;

public interface RecipeRest {

	@PreAuthorize("hasRole('ROLE_NV_RECIPE_viewRecipe')")
	@Secured("ROLE_NV_RECIPE_viewRecipe")
	Response findAllRecipe(Integer lLimit, Integer uLimit);

	@PreAuthorize("hasRole('ROLE_NV_RECIPE_createRecipe')")
	@Secured("ROLE_NV_RECIPE_createRecipe")
	Response createRecipe(Recipe strRecipe);

	@PreAuthorize("hasRole('ROLE_NV_RECIPE_editRecipe')")
	@Secured("ROLE_NV_RECIPE_editRecipe")
	Response updateRecipe(Recipe strRecipe);

	@PreAuthorize("hasRole('ROLE_NV_RECIPE_viewRecipe')")
	@Secured("ROLE_NV_RECIPE_viewRecipe")
	Response getTotalRecipeCount();
	
	@PreAuthorize("hasRole('ROLE_NV_RECIPE_viewRecipe')")
	@Secured("ROLE_NV_RECIPE_viewRecipe")
	Response getRecipeByCategory(List<String> category);

	@PreAuthorize("hasRole('ROLE_NV_RECIPE_deleteRecipe')")
	@Secured("ROLE_NV_RECIPE_deleteRecipe")
	Response deleteByPk(List<Integer> strRecipeIds);

	@PreAuthorize("hasRole('ROLE_NV_RECIPE_deleteRecipe')")
	@Secured("ROLE_NV_RECIPE_deleteRecipe")
	Response deleteRecipeById(Integer recipeId);

	Response checkRecipeMappedWithWO(Integer recipeId);

	Response completeRecipe(Integer woRecipeMappingId);

	Response createRecipeForMobile(String strRecipe);

	Response getWOFilesByRecipeMappingId(Integer recipeMappingId);

	String syncRecipeFiles(Integer wORecipeMappingId, Boolean isRetried, String fileType, InputStream inputFile,
			String fileName);

	Response validateFTPConnection(FTPDetailsWrapper ftpDetailsWrapper);

	@PreAuthorize("hasRole('ROLE_NV_WO_RECIPE_reopen')")
	@Secured("ROLE_NV_WO_RECIPE_reopen")
	Response reopenRecipeById(Integer woRecipeId, Integer workOrderId);

	Response getWODetailsZipFile(String filePath);

}
