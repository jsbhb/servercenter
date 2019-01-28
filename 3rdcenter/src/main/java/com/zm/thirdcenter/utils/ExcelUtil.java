package com.zm.thirdcenter.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.zm.thirdcenter.pojo.CarrierModel;

public class ExcelUtil {
	
	private static ExcelUtil instance = null;


	public static ExcelUtil instance() {
		if (instance == null) {
			instance = new ExcelUtil();
		}
		
		return instance;
	}
	

	@SuppressWarnings("static-access")
	private static String getValue(XSSFCell xssfRow) {
		if (xssfRow.getCellType() == xssfRow.CELL_TYPE_BOOLEAN) {
			return String.valueOf(xssfRow.getBooleanCellValue());
		} else if (xssfRow.getCellType() == xssfRow.CELL_TYPE_NUMERIC) {
			return String.valueOf(xssfRow.getNumericCellValue());
		} else {
			return String.valueOf(xssfRow.getStringCellValue());
		}
	}

	@SuppressWarnings("static-access")
	private static String getValue(HSSFCell hssfCell) {
		if (hssfCell == null) {
			return "";
		}
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf(hssfCell.getNumericCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_STRING) {
			return String.valueOf(hssfCell.getStringCellValue());
		} else {
			return "";
		}
	}

	/**
	 * read the Excel file
	 * 
	 * @param path
	 *            the path of the Excel file
	 * @return
	 * @throws IOException
	 */
	private List<CarrierModel> readExcel(String path) throws IOException {
		if (path == null || "".equals(path)) {
			return null;
		} else {
			String postfix = path.substring(path.lastIndexOf(".") + 1,
					path.length());
			if (!"".equals(postfix)) {
				if ("xls".equals(postfix)) {
					return readXls(path);
				} else if ("xlsx".equals(postfix)) {
					return readXlsx(path);
				}
			} else {
				return null;
			}
		}
		return null;
	}

	/**
	 * Read the Excel 2010
	 * 
	 * @param path
	 *            the path of the excel file
	 * @return
	 * @throws IOException
	 */
	private List<CarrierModel> readXlsx(String path) throws IOException {
		List<CarrierModel> list = new ArrayList<CarrierModel>();
		InputStream is = this.getClass().getResourceAsStream(path);
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
		CarrierModel hSModel = null;
		// Read the Sheet
		XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
		if (xssfSheet == null) {
			return null;
		}
		
		// Read the Row
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			if (xssfRow != null) {
				hSModel = new CarrierModel();
				XSSFCell carrierID = xssfRow.getCell(0);
				XSSFCell carrierName = xssfRow.getCell(1);
				
				
				hSModel.setCarrierID(getValue(carrierID).trim());
				hSModel.setCarrierName(getValue(carrierName).trim());
				list.add(hSModel);

			}
		}
		return list;
	}

	/**
	 * Read the Excel 2003-2007
	 * 
	 * @param path
	 *            the path of the Excel
	 * @return
	 * @throws IOException
	 */
	private List<CarrierModel> readXls(String path) throws IOException {
		List<CarrierModel> list = new ArrayList<CarrierModel>();
		InputStream is = this.getClass().getResourceAsStream(path);
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
		CarrierModel hSModel = null;
		// Read the Sheet
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
		if (hssfSheet == null) {
			return null;
		}
		// Read the Row
		for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
			HSSFRow hssfRow = hssfSheet.getRow(rowNum);
			if (hssfRow != null) {
				hSModel = new CarrierModel();
				HSSFCell carrierID = hssfRow.getCell(0);
				HSSFCell carrierName = hssfRow.getCell(1);

				hSModel.setCarrierID(getValue(carrierID).trim());
				hSModel.setCarrierName(getValue(carrierName).trim());
			}
			list.add(hSModel);
		}
		return list;
	}
	
	public  List<CarrierModel> getCache(String path){
		try {
			return readExcel(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
