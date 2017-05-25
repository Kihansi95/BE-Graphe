package application;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.*;

public class CheckExcel {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		System.out.println("Test lecture et l'ecriture excel Excel...");
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet();
		XSSFRow row = sheet.createRow(0);
		XSSFCell firstCell = row.createCell(1);
		firstCell.setCellValue("Hello world");
		
		row = sheet.createRow(1);
		for(int i = 0; i < 10; i++)	{
			XSSFCell cell = row.createCell(i);
			cell.setCellValue(i);
		}
		
		workbook.write(new FileOutputStream("excel.xlsx"));
		workbook.close();
		
		System.out.println("Test fini, verifiez l'existance du fichier excel.xlsx");
	}

}
