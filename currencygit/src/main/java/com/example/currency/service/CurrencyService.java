package com.example.currency.service;

import com.example.currency.dto.ExchangeApiResponse;
import com.example.currency.entity.ConversionLog;
import com.example.currency.repository.ConversionLogRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CurrencyService {

    private final ConversionLogRepository repo;
    private final ExternalRateClient external; // dış kur API'sini çağırdığın client (sen ne kullanıyorsan)

    public CurrencyService(ConversionLogRepository repo, ExternalRateClient external) {
        this.repo = repo;
        this.external = external;
    }

    /* Controller'ın çağırdığı METOTLAR */

    public ExchangeApiResponse getLatestRates(String base) {
        // senin mevcut implementasyonun (open.er-api vs.)
        return external.fetchLatest(base);
    }

    public Double convert(String base, String target, double amount) {
        // senin mevcut implementasyonun
        // 1) latest rate al
        // 2) amount * rate
        // 3) ConversionLog kaydet
        Double rate = external.getRate(base, target);
        if (rate == null) return null;

        double converted = amount * rate;
        // log kaydı (entity alan adlarına göre doldur)
        ConversionLog log = new ConversionLog();
        log.setBaseCode(base.toUpperCase());
        log.setTargetCode(target.toUpperCase());
        log.setRate(rate);
        log.setAmount(amount);
        log.setConverted(converted);
        log.setCreatedAt(LocalDateTime.now());
        repo.save(log);

        return converted;
    }

    // Tablo: son 'limit' kayıt (DESC)
    public List<ConversionLog> getRecentHistory(String base, String target, int limit) {
        return repo.findByBaseCodeAndTargetCodeOrderByCreatedAtDesc(
                base.toUpperCase(), target.toUpperCase(), PageRequest.of(0, limit)
        ).getContent();
    }

    // Grafik: son 'days' gün (ASC)
    public List<ConversionLog> getHistoryLastDays(String base, String target, int days) {
        LocalDateTime after = LocalDateTime.now().minusDays(days);
        return repo.findByBaseCodeAndTargetCodeAndCreatedAtGreaterThanEqualOrderByCreatedAtAsc(
                base.toUpperCase(), target.toUpperCase(), after
        );
    }
}
