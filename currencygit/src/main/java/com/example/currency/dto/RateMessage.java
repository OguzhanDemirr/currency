package com.example.currency.dto;

import java.time.Instant;

public class RateMessage {
  private String base;
  private String target;
  private double rate;
  private double amount;
  private double converted;
  private Instant createdAt;

  public RateMessage() {}
  public RateMessage(String base, String target, double rate, double amount, double converted, Instant createdAt) {
    this.base = base; this.target = target; this.rate = rate;
    this.amount = amount; this.converted = converted; this.createdAt = createdAt;
  }
  public String getBase() { return base; }
  public String getTarget() { return target; }
  public double getRate() { return rate; }
  public double getAmount() { return amount; }
  public double getConverted() { return converted; }
  public Instant getCreatedAt() { return createdAt; }
}
