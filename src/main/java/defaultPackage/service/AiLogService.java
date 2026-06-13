package defaultPackage.service;

import defaultPackage.entity.AiRequestLog;
import defaultPackage.entity.User;
import defaultPackage.repository.AiRequestLogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AiLogService {
    private final AiRequestLogRepository logRepository;
    @Value("${horoscope.daily-limit:20}")
    private int dailyLimit;

    public AiLogService(AiRequestLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public boolean canMakeRequest(UUID userId) {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        long count = logRepository.countByUser_IdAndCreatedAtAfter(userId, todayStart);
        return count < dailyLimit;
    }

    public long getRemainingRequests(UUID userId) {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        long count = logRepository.countByUser_IdAndCreatedAtAfter(userId, todayStart);
        return Math.max(0, dailyLimit - count);
    }

    public long getDailyLimit() {
        return dailyLimit;
    }

    public void logSuccess(User user, String requestType, String requestPrompt,
                           String responseSummary, Integer tokensUsed) {
        AiRequestLog log = new AiRequestLog();
        log.setUser(user);
        log.setRequestType(requestType);
        log.setRequestPrompt(truncate(requestPrompt, 1000));
        log.setResponseSummary(truncate(responseSummary, 500));
        log.setTokensUsed(tokensUsed);
        log.setStatus("SUCCESS");
        logRepository.save(log);
    }

    public void logFailure(User user, String requestType, String requestPrompt, String errorMessage) {
        AiRequestLog log = new AiRequestLog();
        log.setUser(user);
        log.setRequestType(requestType);
        log.setRequestPrompt(truncate(requestPrompt, 1000));
        log.setResponseSummary(truncate(errorMessage, 500));
        log.setTokensUsed(0);
        log.setStatus("FAILED");
        logRepository.save(log);
    }

    private String truncate(String text, int maxLength) {
        if (text == null) return null;
        return text.length() > maxLength ? text.substring(0, maxLength) + "..." : text;
    }
}