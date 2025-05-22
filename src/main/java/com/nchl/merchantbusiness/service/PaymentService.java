package com.nchl.merchantbusiness.service;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.nchl.merchantbusiness.entity.Payment;
import com.nchl.merchantbusiness.exception.PaymentException;
import com.nchl.merchantbusiness.repository.PaymentRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public Payment createPayment(String merchantId, String customerId, BigDecimal amount, String description) {
        Payment payment = new Payment();
        payment.setTransactionId(generateTransactionId());
        payment.setMerchantId(merchantId);
        payment.setCustomerId(customerId);
        payment.setAmount(amount);
        payment.setDescription(description);
        payment.setStatus("PENDING");

        payment.setQrCode(generateValidNepalPayQR(
                merchantId,
                "Your Merchant Name",
                payment.getTransactionId(),
                new BigDecimal(String.valueOf(amount)),
                "INV" + System.currentTimeMillis()
        ));

        return paymentRepository.save(payment);
    }
    @Transactional
    public Payment completePayment(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new PaymentException("PAYMENT_NOT_FOUND", "Payment not found with ID: " + transactionId));

        if (!"PENDING".equals(payment.getStatus())) {
            throw new PaymentException("PAYMENT_ALREADY_PROCESSED", "Payment is already processed");
        }

        payment.setStatus("COMPLETED");
        payment.setCompletedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public Payment getPayment(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new PaymentException("PAYMENT_NOT_FOUND", "Payment not found with ID: " + transactionId));
    }

    private String generateTransactionId() {
        return "NPL" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }

    private String generateValidNepalPayQR(String merchantId,
                                           String merchantName,
                                           String transactionId,
                                           BigDecimal amount,
                                           String invoiceReference) {
        try {

            String qrContent = String.format(
                    "nepalpay://pay?pid=%s&pn=%s&am=%s&tid=%s&tr=%s",
                    merchantId,
                    URLEncoder.encode(merchantName, StandardCharsets.UTF_8.toString()),
                    amount.setScale(2, RoundingMode.HALF_UP).toString(),
                    transactionId,
                    invoiceReference
            );

            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints.put(EncodeHintType.MARGIN, 2);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(
                    qrContent,
                    BarcodeFormat.QR_CODE,
                    300, 300, hints
            );

            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            return "data:image/png;base64," +
                    Base64.getEncoder().encodeToString(pngOutputStream.toByteArray());
        } catch (Exception e) {
            throw new PaymentException("QR_GENERATION_FAILED", "Failed to generate QR code", e);
        }
    }

}