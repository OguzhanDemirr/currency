package com.example.currency.repository;

import com.example.currency.entity.ConversionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ConversionLogRepository extends JpaRepository<ConversionLog, Long> {

    // Tablodaki liste için: en yeni N kayıt (DESC) + pageable
    Page<ConversionLog> findByBaseCodeAndTargetCodeOrderByCreatedAtDesc(
            String baseCode, String targetCode, Pageable pageable
    );

    // Grafik için: son X gün (createdAt >= after) ve zaman ekseni için artan sırada
    List<ConversionLog> findByBaseCodeAndTargetCodeAndCreatedAtGreaterThanEqualOrderByCreatedAtAsc(
            String baseCode, String targetCode, LocalDateTime after
    );
}
