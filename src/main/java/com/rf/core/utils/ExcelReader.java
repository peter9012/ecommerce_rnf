package com.rf.core.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReader {

	private static XSSFSheet ExcelWSheet;

	private static XSSFWorkbook ExcelWBook;

	private static XSSFCell Cell;

	private static final Logger logger = LogManager.getLogger(ExcelReader.class
			.getName());

	public static Object[][] getTableArray(String FilePath, String SheetName)
			throws Exception {

		Object[][] tabArray = null;

		try {

			FileInputStream ExcelFile = new FileInputStream(FilePath);

			// Access the required test data sheet

			ExcelWBook = new XSSFWorkbook(ExcelFile);

			//ExcelWSheet = ExcelWBook.getSheet(SheetName);
			ExcelWSheet = ExcelWBook.getSheetAt(0);

			int startRow = 1;

			int startCol = 0;

			int ci, cj;

			int totalRows = ExcelWSheet.getLastRowNum();
			
			int totalCols = ExcelWSheet.getRow(1).getLastCellNum();

			tabArray = new String[totalRows][totalCols];

			ci = 0;

			for (int i = startRow; i <= totalRows; i++, ci++) {

				cj = 0;

				for (int j = startCol; j < totalCols; j++, cj++) {
					try {
						tabArray[ci][cj] = getCellData(i, j);
					} catch (Exception e) {
						break;
					}

				}
			}

		}

		catch (FileNotFoundException e) {

			logger.info("Could not find the Excel sheet");

			e.printStackTrace();

		}

		catch (IOException e) {

			logger.info("Could not read the Excel sheet");

			e.printStackTrace();

		}

		return (tabArray);

	}

	public static Object getCellData(int RowNum, int ColNum) throws Exception {

		try {

			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);

			int dataType = Cell.getCellType();
			
			if (dataType == 3) {

				return "";

			} else if (dataType == 0) {
				Object CellData = Cell.getRawValue();
				return CellData;

			} else {

				Object CellData = Cell.getStringCellValue();
				return CellData;
			}

		} catch (Exception e) {

			System.out.println(e.getMessage());

			throw (e);

		}
	}

}