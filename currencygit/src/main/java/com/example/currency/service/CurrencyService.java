import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class CurrencyService {
    private final ConversionLogRepository repo;
    public CurrencyService(ConversionLogRepository repo) { this.repo = repo; }

    // Son 'limit' kayıt (DESC)
    public List<ConversionLog> getRecentHistory(String base, String target, int limit) {
        return repo.findByBaseCodeAndTargetCodeOrderByCreatedAtDesc(
                base, target, PageRequest.of(0, limit)
        ).getContent();
    }

    // Son 'days' gün (ASC)
    public List<ConversionLog> getHistoryLastDays(String base, String target, int days) {
        LocalDateTime after = LocalDateTime.now().minusDays(days);
        return repo.findByBaseCodeAndTargetCodeAndCreatedAtGreaterThanEqualOrderByCreatedAtAsc(
                base, target, after
        );
    }
}
