package com.example.currency.job;

import com.example.currency.service.CurrencyService;




import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RateFetchJob {

    private final CurrencyService service;

    public RateFetchJob(CurrencyService service) {
        this.service = service;
    }

    // Varsayılanlar: USD -> TRY, miktar 1
    @Value("${app.scheduler.base:USD}")
    private String base;

    @Value("${app.scheduler.target:TRY}")
    private String target;

    @Value("${app.scheduler.amount:1.0}")
    private double amount;

    @Value("${app.scheduler.enabled:true}")
    private boolean enabled;
    
   
    // Her dakika başında (saniye 0'da) çalışır
    @Scheduled(cron = "0 * * * * *", zone = "Europe/Istanbul")
    public void fetchAndLog() {
        if (!enabled) return;
        // Zaten DB'ye yazan mevcut servisi kullan: convert()
        // convert(): API'den çeker, oranı bulur, ConversionLog kaydeder.
        service.convert(base, target, amount);
    }
}

