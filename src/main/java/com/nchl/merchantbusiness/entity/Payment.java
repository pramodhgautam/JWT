package com.nchl.merchantbusiness.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BIGSERIAL")
    private Long id;

    @Column(nullable = false)
    private String transactionId;

    @Column(nullable = false)
    private String merchantId;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency = "NPR";

    @Column(nullable = false)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String qrCode;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    @Column(length = 500)
    private String description;
}
