package com.inn.foresight.module.nv.layer3.service.parse;

import java.io.IOException;
import java.util.List;

import com.inn.foresight.module.nv.workorder.model.WOFileDetail;

public interface INVL3ParsingService {


	List<WOFileDetail> getUnprocessFileFromDataBase() throws IOException;

	String fileProcessWorkorderWise(Integer workorderId, Integer recipeId, String assignTo) throws IOException ;


}
