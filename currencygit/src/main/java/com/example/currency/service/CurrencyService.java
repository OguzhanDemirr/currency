package com.example.currency.service;

import com.example.currency.dto.ExchangeApiResponse;
import com.example.currency.dto.RateMessage;               // <-- eklendi
import com.example.currency.entity.ConversionLog;
import com.example.currency.repository.ConversionLogRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate; // <-- eklendi
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;                                     // <-- eklendi
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class CurrencyService {

    private final ConversionLogRepository repo;
    private final RestTemplate restTemplate;
    private final SimpMessagingTemplate broker;              // <-- eklendi

    // open.er-api.com endpoint’i
    private static final String LATEST_URL = "https://open.er-api.com/v6/latest/{base}";

    public CurrencyService(ConversionLogRepository repo,
                           RestTemplate restTemplate,
                           SimpMessagingTemplate broker) {   // <-- eklendi
        this.repo = repo;
        this.restTemplate = restTemplate;
        this.broker = broker;                                 // <-- eklendi
    }

    /** Harici API'den latest kurları getir */
    public ExchangeApiResponse getLatestRates(String base) {
        String b = base == null ? "USD" : base.toUpperCase(Locale.ROOT);
        return restTemplate.getForObject(LATEST_URL, ExchangeApiResponse.class, b);
    }

    /** Dönüşüm yap + log kaydet, converted sonucu döndür + WS yayını yap */
    public Double convert(String base, String target, double amount) {
        String b = base.toUpperCase(Locale.ROOT);
        String t = target.toUpperCase(Locale.ROOT);

        ExchangeApiResponse resp = getLatestRates(b);
        if (resp == null || resp.getRates() == null) return null;

        Map<String, Double> rates = resp.getRates();
        Double rate = rates.get(t);
        if (rate == null) return null;

        double converted = amount * rate;

        // Log kaydı
        ConversionLog log = new ConversionLog();
        log.setBaseCode(b);
        log.setTargetCode(t);
        log.setRate(rate);
        log.setAmount(amount);
        log.setConverted(converted);
        log.setCreatedAt(LocalDateTime.now());
        repo.save(log);

        // --- WebSocket yayını (/topic/rates) ---
        try {
            broker.convertAndSend(
                "/topic/rates",
                new RateMessage(b, t, rate, amount, converted, Instant.now())
            );
        } catch (Exception ignore) {
            // WS başarısız olsa bile iş akışını bozma
        }

        return converted;
    }

    /** Tablo için: son 'limit' kayıt (en yeni → eski) */
    public List<ConversionLog> getRecentHistory(String base, String target, int limit) {
        String b = base.toUpperCase(Locale.ROOT);
        String t = target.toUpperCase(Locale.ROOT);
        return repo
                .findByBaseCodeAndTargetCodeOrderByCreatedAtDesc(b, t, PageRequest.of(0, limit))
                .getContent();
    }

    /** Grafik için: son 'days' gün (eski → yeni) */
    public List<ConversionLog> getHistoryLastDays(String base, String target, int days) {
        String b = base.toUpperCase(Locale.ROOT);
        String t = target.toUpperCase(Locale.ROOT);
        LocalDateTime after = LocalDateTime.now().minusDays(days);
        return repo
                .findByBaseCodeAndTargetCodeAndCreatedAtGreaterThanEqualOrderByCreatedAtAsc(b, t, after);
    }
}
