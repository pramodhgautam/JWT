package com.nchl.merchantbusiness.service.impl;

import com.nchl.merchantbusiness.dto.AuthResponse;
import com.nchl.merchantbusiness.service.ReportService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private static final String REPORT_DIR = "/tmp/reports/";

    @Override
    public String writeCSV(AuthResponse response) throws IOException {
        Files.createDirectories(Paths.get(REPORT_DIR));

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Code,Status,Message,AccessToken,TokenType,RefreshToken,ExpiresIn\n");
        csvBuilder.append(String.join(",", List.of(
                response.getCode(),
                response.getStatus(),
                response.getMessage(),
                response.getData().getAccessToken(),
                response.getData().getTokenType(),
                response.getData().getRefreshToken(),
                String.valueOf(response.getData().getExpiresIn())
        )));

        Path csvPath = Paths.get(REPORT_DIR + "auth-report.csv");
        Files.writeString(csvPath, csvBuilder.toString());

        return csvPath.toString();
    }

    @Override
    public String writeExcel(AuthResponse response) throws IOException {
        Files.createDirectories(Paths.get(REPORT_DIR));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Auth Report");

        String[] headers = {"Code", "Status", "Message", "AccessToken", "TokenType", "RefreshToken", "ExpiresIn"};

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            headerRow.createCell(i).setCellValue(headers[i]);
        }

        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(response.getCode());
        dataRow.createCell(1).setCellValue(response.getStatus());
        dataRow.createCell(2).setCellValue(response.getMessage());
        dataRow.createCell(3).setCellValue(response.getData().getAccessToken());
        dataRow.createCell(4).setCellValue(response.getData().getTokenType());
        dataRow.createCell(5).setCellValue(response.getData().getRefreshToken());
        dataRow.createCell(6).setCellValue(response.getData().getExpiresIn());

        String excelPath = REPORT_DIR + "auth-report.xlsx";
        try (FileOutputStream fileOut = new FileOutputStream(excelPath)) {
            workbook.write(fileOut);
        }
        workbook.close();

        return excelPath;
    }
}
