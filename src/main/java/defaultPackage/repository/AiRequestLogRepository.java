package defaultPackage.repository;

import defaultPackage.entity.AiRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.UUID;

public interface AiRequestLogRepository extends JpaRepository<AiRequestLog, UUID> {
    long countByUser_IdAndCreatedAtAfter(UUID userId, LocalDateTime after);
    long countByUser_IdAndStatusAndCreatedAtAfter(UUID userId, String status, LocalDateTime after);
}