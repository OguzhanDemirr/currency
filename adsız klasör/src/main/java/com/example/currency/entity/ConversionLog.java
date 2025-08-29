package com.example.currency.entity;

import jakarta.persistence.*;  // veya tek tek: Entity, Table, Id, GeneratedValue, Column

import java.time.LocalDateTime;

@Entity
@Table(name = "conversion_log")
public class ConversionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 8, nullable = false)
    private String baseCode;

    @Column(length = 8, nullable = false)
    private String targetCode;

    @Column(nullable = false)
    private Double rate;       // kullanılan kur

    @Column(nullable = false)
    private Double amount;     // giriş tutarı

    @Column(nullable = false)
    private Double converted;  // dönüşen tutar

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public ConversionLog() {}

    public ConversionLog(String baseCode, String targetCode, Double rate,
                         Double amount, Double converted, LocalDateTime createdAt) {
        this.baseCode = baseCode;
        this.targetCode = targetCode;
        this.rate = rate;
        this.amount = amount;
        this.converted = converted;
        this.createdAt = createdAt;
    }

    // getters & setters
    public Long getId() { return id; }
    public String getBaseCode() { return baseCode; }
    public void setBaseCode(String baseCode) { this.baseCode = baseCode; }
    public String getTargetCode() { return targetCode; }
    public void setTargetCode(String targetCode) { this.targetCode = targetCode; }
    public Double getRate() { return rate; }
    public void setRate(Double rate) { this.rate = rate; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public Double getConverted() { return converted; }
    public void setConverted(Double converted) { this.converted = converted; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
