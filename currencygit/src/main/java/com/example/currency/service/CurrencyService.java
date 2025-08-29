package com.example.currency.service;

import com.example.currency.dto.ExchangeApiResponse;
import com.example.currency.entity.ConversionLog;
import com.example.currency.repository.ConversionLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CurrencyService {

    private final RestTemplate restTemplate;
    private final ConversionLogRepository logRepo;

    public CurrencyService(RestTemplate restTemplate, ConversionLogRepository logRepo) {
        this.restTemplate = restTemplate;
        this.logRepo = logRepo;
    }

    // Harici API'den kurları çek
    public ExchangeApiResponse getLatestRates(String base) {
        try {
            String url = "https://open.er-api.com/v6/latest/" + base.toUpperCase();
            return restTemplate.getForObject(url, ExchangeApiResponse.class);
        } catch (RestClientException ex) {
            return null;
        }
    }

    // Belirli bir miktarı dönüştür + DB'ye kaydet
    public Double convert(String base, String target, double amount) {
        ExchangeApiResponse resp = getLatestRates(base);
        if (resp == null || resp.getRates() == null) return null;

        Double rate = resp.getRates().get(target.toUpperCase());
        if (rate == null) return null;

        double converted = amount * rate;

        // DB'ye log kaydı ekle
        ConversionLog log = new ConversionLog(
                base.toUpperCase(),
                target.toUpperCase(),
                rate,
                amount,
                converted,
                LocalDateTime.now()
        );
        logRepo.save(log);

        return converted;
    }

    
    public List<ConversionLog> serviceHistory(String base, String target) {
        return logRepo.findTop10ByBaseCodeIgnoreCaseAndTargetCodeIgnoreCaseOrderByCreatedAtDesc(
                base, target
        );
    }
}
