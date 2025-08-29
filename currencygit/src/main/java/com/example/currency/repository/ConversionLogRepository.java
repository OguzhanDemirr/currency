package com.example.currency.repository;

import com.example.currency.entity.ConversionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversionLogRepository extends JpaRepository<ConversionLog, Long> {
    List<ConversionLog> findTop10ByBaseCodeIgnoreCaseAndTargetCodeIgnoreCaseOrderByCreatedAtDesc(
            String baseCode, String targetCode
    );
}
