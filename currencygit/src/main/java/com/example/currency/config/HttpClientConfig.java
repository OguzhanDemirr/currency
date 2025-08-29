package com.example.currency.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.EnableScheduling; 

@EnableScheduling // <-- EKLE
@Configuration
public class HttpClientConfig {

    @Bean
    public RestTemplate restTemplate() {
       
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 saniye connection timeout
        factory.setReadTimeout(5000);    // 5 saniye read timeout

        return new RestTemplate(factory);
    }
}
