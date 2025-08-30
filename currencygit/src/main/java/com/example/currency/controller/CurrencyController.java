package com.example.currency.controller;

import com.example.currency.dto.ExchangeApiResponse;
import com.example.currency.entity.ConversionLog;
import com.example.currency.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    private final CurrencyService service;

    public CurrencyController(CurrencyService service) {
        this.service = service;
    }
    
    @GetMapping("/latest")
    public ResponseEntity<?> latest(@RequestParam String base) {
        return getLatest(base); // mevcut /latest/{base} metodunu çağır
    }

    // 1) Taban para birimine göre tüm kurları getir
    // Örnek: GET /api/currency/latest/USD
    @GetMapping("/latest/{base}")
    public ResponseEntity<?> getLatest(@PathVariable String base) {
        ExchangeApiResponse resp = service.getLatestRates(base);
        if (resp == null) {
            return ResponseEntity.status(502).body("Harici API’den yanıt alınamadı");
        }
        if (!"success".equalsIgnoreCase(resp.getResult())) {
            return ResponseEntity.badRequest().body(resp);
        }
        return ResponseEntity.ok(resp);
    }

    // 2) Dönüştürme endpoint’i
    // Örnek: GET /api/currency/convert?base=USD&target=TRY&amount=100
    @GetMapping("/convert")
    public ResponseEntity<?> convert(
            @RequestParam String base,
            @RequestParam String target,
            @RequestParam double amount
    ) {
        Double result = service.convert(base, target, amount);
        if (result == null) {
            return ResponseEntity.badRequest().body("Kur bulunamadı ya da API hatası");
        }
        return ResponseEntity.ok(new ConversionResponse(base, target, amount, result));
    }

    // 3) Son 10 işlemi getir (history)
    // Örnek: GET /api/currency/history?base=USD&target=TRY
    @GetMapping("/history")
    public ResponseEntity<?> history(
            @RequestParam String base,
            @RequestParam String target
    ) {
        List<ConversionLog> logs = service.serviceHistory(base, target);
        return ResponseEntity.ok(logs);
    }

    // Basit bir inline response modeli
    static class ConversionResponse {
        public String base;
        public String target;
        public double amount;
        public double converted;

        public ConversionResponse(String base, String target, double amount, double converted) {
            this.base = base.toUpperCase();
            this.target = target.toUpperCase();
            this.amount = amount;
            this.converted = converted;
        }
    }
}

