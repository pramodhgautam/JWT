package com.nchl.merchantbusiness.controller;

import com.nchl.merchantbusiness.dto.APIResponse;
import com.nchl.merchantbusiness.dto.AuthResponse;
import com.nchl.merchantbusiness.service.ReportService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/auth/save")
    public ResponseEntity<String> saveReports(@RequestBody AuthResponse response) {
        try {
            String csvPath = reportService.writeCSV(response);
            String excelPath = reportService.writeExcel(response);

            return ResponseEntity.ok("CSV saved to: " + csvPath + "\nExcel saved to: " + excelPath);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to write report: " + e.getMessage());
        }
    }
}


//    @PostMapping("/auth/csv")
//    public ResponseEntity<FileSystemResource> downloadCSV(@RequestBody AuthResponse response) throws IOException {
//        StringBuilder csvBuilder = new StringBuilder();
//        csvBuilder.append("Code,Status,Message,AccessToken,TokenType,RefreshToken,ExpiresIn\n");
//        csvBuilder.append(String.join(",", List.of(
//                response.getCode(),
//                response.getStatus(),
//                response.getMessage(),
//                response.getData().getAccessToken(),
//                response.getData().getTokenType(),
//                response.getData().getRefreshToken(),
//                String.valueOf(response.getData().getExpiresIn())
//        )));
//
//        Path filePath = Files.createTempFile("auth-report-", ".csv");
//        Files.writeString(filePath, csvBuilder.toString());
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=auth_report.csv")
//                .contentType(MediaType.parseMediaType("text/csv"))
//                .body(new FileSystemResource(filePath));
//    }
//
//    @PostMapping("/auth/excel")
//    public ResponseEntity<FileSystemResource> downloadExcel(@RequestBody AuthResponse response) throws IOException {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Auth Report");
//
//        Row headerRow = sheet.createRow(0);
//        String[] headers = {"Code", "Status", "Message", "AccessToken", "TokenType", "RefreshToken", "ExpiresIn"};
//        for (int i = 0; i < headers.length; i++) {
//            headerRow.createCell(i).setCellValue(headers[i]);
//        }
//
//        Row dataRow = sheet.createRow(1);
//        dataRow.createCell(0).setCellValue(response.getCode());
//        dataRow.createCell(1).setCellValue(response.getStatus());
//        dataRow.createCell(2).setCellValue(response.getMessage());
//        dataRow.createCell(3).setCellValue(response.getData().getAccessToken());
//        dataRow.createCell(4).setCellValue(response.getData().getTokenType());
//        dataRow.createCell(5).setCellValue(response.getData().getRefreshToken());
//        dataRow.createCell(6).setCellValue(response.getData().getExpiresIn());
//
//        Path filePath = Files.createTempFile("auth-report-", ".xlsx");
//        try (FileOutputStream fileOut = new FileOutputStream(filePath.toFile())) {
//            workbook.write(fileOut);
//        }
//        workbook.close();
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=auth_report.xlsx")
//                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
//                .body(new FileSystemResource(filePath));
//    }


