package com.inn.foresight.core.excel.service;

import com.inn.core.generic.service.IGenericService;
import com.inn.foresight.core.excel.model.ExcelData;

public interface IExcelService extends IGenericService<Integer, ExcelData> {

	public void excelRW();
	
	
	public void writeToExcel();


	void criteriaDemo();
	
}
