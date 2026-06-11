package defaultPackage.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ai_request_logs")
public class AiRequestLog {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "request_type", length = 50)
    private String requestType;
    @Column(name = "request_prompt", columnDefinition = "TEXT")
    private String requestPrompt;
    @Column(name = "response_summary", columnDefinition = "TEXT")
    private String responseSummary;
    @Column(name = "tokens_used")
    private Integer tokensUsed;
    @Column(name = "status", length = 30)
    private String status;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    public AiRequestLog() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getRequestType() { return requestType; }
    public void setRequestType(String requestType) { this.requestType = requestType; }
    public String getRequestPrompt() { return requestPrompt; }
    public void setRequestPrompt(String requestPrompt) { this.requestPrompt = requestPrompt; }
    public String getResponseSummary() { return responseSummary; }
    public void setResponseSummary(String responseSummary) { this.responseSummary = responseSummary; }
    public Integer getTokensUsed() { return tokensUsed; }
    public void setTokensUsed(Integer tokensUsed) { this.tokensUsed = tokensUsed; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}