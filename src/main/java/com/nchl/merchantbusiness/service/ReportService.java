package com.nchl.merchantbusiness.service;

import com.nchl.merchantbusiness.dto.AuthResponse;
import java.io.IOException;

public interface ReportService {
    String writeCSV(AuthResponse response) throws IOException;
    String writeExcel(AuthResponse response) throws IOException;
}
