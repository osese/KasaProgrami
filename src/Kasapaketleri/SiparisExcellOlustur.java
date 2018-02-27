/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Kasapaketleri;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;


/**
 *
 * @author revolver
 */
public class SiparisExcellOlustur {
    Workbook wb;
    Sheet sheet;
    CreationHelper createHelper; 
    
    SiparisExcellOlustur(){
        wb = new HSSFWorkbook();
        sheet = wb.createSheet();
        
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 3000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 8000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 4000);
        
        
        createHelper = wb.getCreationHelper();
        CellStyle style = wb.createCellStyle();
        
        Font font = wb.createFont();
        font.setFontHeightInPoints((short)12);
        font.setFontName("Courier New");
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.BLACK.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setShrinkToFit(false);
        
        
        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue(createHelper.createRichTextString("Siparis No"));
        row.createCell(1).setCellValue(createHelper.createRichTextString("Yemek/Kahvaltı"));
        row.createCell(2).setCellValue(createHelper.createRichTextString("Fis No"));
        row.createCell(3).setCellValue(createHelper.createRichTextString("Kullanıcı"));
        row.createCell(4).setCellValue(createHelper.createRichTextString("Tarih"));
        row.createCell(5).setCellValue(createHelper.createRichTextString("Saat"));
        row.createCell(6).setCellValue(createHelper.createRichTextString("Urun adi"));
        row.createCell(7).setCellValue(createHelper.createRichTextString("Fiyat"));
        row.createCell(8).setCellValue(createHelper.createRichTextString("Durum"));
        
        for(Cell i : row){
            i.setCellStyle(style);
        }
    }
    
    public void setData(ArrayList<Siparis> siparisler){
        // First sheet
        Row row; 
        int i = 1;
        CellStyle dateStyle = wb.createCellStyle();
        CellStyle timeStyle = wb.createCellStyle();
        
        dateStyle.setDataFormat(
        createHelper.createDataFormat().getFormat("d/m/yyyy"));
        
        timeStyle.setDataFormat(
        createHelper.createDataFormat().getFormat("hh:mm:ss"));
        for(Siparis s : siparisler){
           for(Urun k : s.getUrunler()){
                row = sheet.createRow(i);
                row.createCell(0).setCellValue(s.getId());
                row.createCell(1).setCellValue(s.getTypeS());
                row.createCell(2).setCellValue(s.getFis_no());
                row.createCell(3).setCellValue(s.getUserId());
                Cell dateCell = row.createCell(4);
                dateCell.setCellValue(s.getDate().toString());
                dateCell.setCellStyle(dateStyle);
                
                Cell timeCell = row.createCell(5);
                timeCell.setCellValue(s.getTime().toString());
                timeCell.setCellStyle(timeStyle);
                
                row.createCell(6).setCellValue(k.getName());
                row.createCell(7).setCellValue(k.getPrice().doubleValue());
                row.createCell(8).setCellValue(s.getStatusS());
                i++;
           }
        }

    }
   
    public void kaydet(String filename) throws FileNotFoundException, IOException{
        FileOutputStream fileOut;
        fileOut = new FileOutputStream(filename);
        wb.write(fileOut);
        fileOut.close();
    }
}
