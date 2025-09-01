import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

public interface ConversionLogRepository extends JpaRepository<ConversionLog, Long> {

    // Liste tablosu: en yeni 10 kayıt (createdAt DESC)
    Page<ConversionLog> findByBaseCodeAndTargetCodeOrderByCreatedAtDesc(
        String baseCode, String targetCode, Pageable pageable
    );

    // Grafik: son N gün (createdAt >= after), zaman ekseni için ASC
    List<ConversionLog> findByBaseCodeAndTargetCodeAndCreatedAtGreaterThanEqualOrderByCreatedAtAsc(
        String baseCode, String targetCode, LocalDateTime after
    );
}
