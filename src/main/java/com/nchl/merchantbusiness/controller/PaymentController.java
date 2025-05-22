package com.nchl.merchantbusiness.controller;

import com.nchl.merchantbusiness.entity.Payment;
import com.nchl.merchantbusiness.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> createPayment(
            @RequestParam String merchantId,
            @RequestParam String customerId,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String description) {

        Payment payment = paymentService.createPayment(merchantId, customerId, amount, description);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/{transactionId}/complete")
    public ResponseEntity<Payment> completePayment(@PathVariable String transactionId) {
        Payment payment = paymentService.completePayment(transactionId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<Payment> getPayment(@PathVariable String transactionId) {
        Payment payment = paymentService.getPayment(transactionId);
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/{transactionId}/qr")
    public ResponseEntity<String> getPaymentQR(@PathVariable String transactionId) {
        Payment payment = paymentService.getPayment(transactionId);
        return ResponseEntity.ok(payment.getQrCode());
    }
}
