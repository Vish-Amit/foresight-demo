package com.inn.foresight.core.excel.dao;

import java.util.List;

import com.inn.core.generic.dao.IGenericDao;
import com.inn.foresight.core.excel.model.ExcelData;

public interface IExcelDao extends IGenericDao<Integer, ExcelData>{

	public  void excelRW() ;
	
	public void updateExcel() ;
	
	public List<ExcelData> writeToExcel();

	public List<ExcelData> criteriaDemo();
}
