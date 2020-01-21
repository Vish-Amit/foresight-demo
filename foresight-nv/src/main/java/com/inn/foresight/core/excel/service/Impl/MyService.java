package com.inn.foresight.core.excel.service.Impl;

import java.io.File;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.inn.foresight.core.infra.model.UserData;

public class MyService {

	public static void main(String[] args) {
		MyService nw = new MyService();
		nw.excelRW();

	}

	public void excelRW() {
		try {
		 
			 EntityManagerFactory emf=Persistence.createEntityManagerFactory("userdata");  
		        javax.persistence.EntityManager em=emf.createEntityManager();  
			
			Workbook wb = WorkbookFactory.create(new File("/home/ist/Downloads/GroupCenter.xlsx"));
			Sheet sheet = wb.getSheetAt(0);

			int rowStart = sheet.getFirstRowNum();
			int rowEnd = sheet.getLastRowNum();

			UserData dd = new UserData();

			for (int i = rowStart + 1; i < rowEnd; i++) {
				Row row = sheet.getRow(i);

				for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
					Cell cell = row.getCell(j);

					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:
						System.out.print(cell.getNumericCellValue() + " \t\t ");
						break;

					case Cell.CELL_TYPE_STRING:
						System.out.print(cell.getStringCellValue() + " \t\t ");
						break;
					}

					switch (j) {

					case 0:
						dd.setGn(cell.getStringCellValue());
						break;

					case 1:
						dd.setLongitude(cell.getNumericCellValue());
						break;

					case 2:
						dd.setLatitude(cell.getNumericCellValue());
						break;

					case 3:
						dd.setRowkey(cell.getNumericCellValue());
						break;

					case 4:
						dd.setGeography(cell.getNumericCellValue());
						break;
					}
				}
				System.out.println("\n");
				   em.persist(dd); 
				//System.out.println(dd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
