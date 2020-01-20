package com.inn.foresight.module.nv.workorder.wrapper;

import java.util.List;

import com.inn.foresight.module.nv.workorder.recipe.wrapper.RecipeWrapper;

public class NVWOUserWrapper {

	private String userName;
	
	private List<RecipeWrapper> recipeList;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<RecipeWrapper> getRecipeList() {
		return recipeList;
	}

	public void setRecipeList(List<RecipeWrapper> recipeList) {
		this.recipeList = recipeList;
	}

	@Override
	public String toString() {
		return "NVWOUserWrapper [userName=" + userName + ", recipeList=" + recipeList + "]";
	}

	
}
