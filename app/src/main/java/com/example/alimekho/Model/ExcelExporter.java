package com.example.alimekho.Model;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExporter {
    private Workbook workbook;
    private Sheet sheet;
    private String fileName;
    private Context context;
    public ExcelExporter(Context context) {
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Data");
        fileName = "data.xlsx";
        this.context = context;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setHeader(List<String> headerData) {
        Row headerRow = sheet.createRow(0);
        int columnCount = 0;
        for (String header : headerData) {
            Cell cell = headerRow.createCell(columnCount++);
            cell.setCellValue(header);
        }
    }

    public void exportToExcel(List<String[]> data) {
        int rowCount = 1;
        for (String[] rowData : data) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            for (String cellData : rowData) {
                Cell cell = row.createCell(columnCount++);
                cell.setCellValue(cellData);
            }
        }

        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.close();
            Log.d(TAG, "OKKKKKKKKK");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error export to xlsx");
        }
    }
}