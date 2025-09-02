package com.example.currency.dto;

import lombok.Data;
import java.util.Map;

@Data  // Lombok anotasyonu, getter-setter, toString, equals, hashCode ekler
public class ExchangeApiResponse {
    private String result;
    private String base_code;
    private Map<String, Double> rates;
}
