package com.nchl.merchantbusiness.controller;

import com.nchl.merchantbusiness.utilities.QrCodeReader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qr")
public class QrController {

    @PostMapping("/decode")
    public ResponseEntity<String> decodeQrCode(@RequestBody String base64QrCode) {
        try {
            String content = QrCodeReader.readQrCode(base64QrCode);
            return ResponseEntity.ok(content);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error decoding QR: " + e.getMessage());
        }
    }
}
