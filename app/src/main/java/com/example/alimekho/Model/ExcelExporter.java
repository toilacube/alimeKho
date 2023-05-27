package com.example.alimekho.Model;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

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

    public void exportToExcel(List<Object[]> data) {
        int rowCount = 1;
        for (Object[] rowData : data) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            for (Object cellData : rowData) {
                Cell cell = row.createCell(columnCount++);
                setCellValue(cell, cellData);
            }
        }

        try {
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.close();
            Toast.makeText(context, "Xuất thành công", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "OKKKKKKKKK");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Thất bại: " + e.toString(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error export to xlsx");
        }
    }

    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof java.util.Date) {
            cell.setCellValue((java.util.Date) value);
            CellStyle cellStyle = workbook.createCellStyle();
            CreationHelper creationHelper = workbook.getCreationHelper();
            cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-mm-dd"));
            cell.setCellStyle(cellStyle);
        } else {
            cell.setCellValue(value.toString());
        }
    }
}