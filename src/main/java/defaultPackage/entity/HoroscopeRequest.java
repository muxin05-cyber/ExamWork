package defaultPackage.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "horoscope_requests")
public class HoroscopeRequest {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "characteristic", nullable = false, length = 500)
    private String characteristic;

    @Column(name = "tone", length = 50)
    private String tone;

    @Column(name = "formality", length = 50)
    private String formality;

    @Column(name = "absurdity_level")
    private Integer absurdityLevel;

    @Column(name = "status", length = 30)
    private String status;

    @Column(name = "general_forecast", columnDefinition = "TEXT")
    private String generalForecast;

    @Column(name = "career_block", columnDefinition = "TEXT")
    private String careerBlock;

    @Column(name = "dangerous_days", columnDefinition = "TEXT")
    private String dangerousDays;

    @Column(name = "what_not_to_do", columnDefinition = "TEXT")
    private String whatNotToDo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public HoroscopeRequest() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
        this.status = "DRAFT";
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getCharacteristic() { return characteristic; }
    public void setCharacteristic(String characteristic) { this.characteristic = characteristic; }

    public String getTone() { return tone; }
    public void setTone(String tone) { this.tone = tone; }

    public String getFormality() { return formality; }
    public void setFormality(String formality) { this.formality = formality; }

    public Integer getAbsurdityLevel() { return absurdityLevel; }
    public void setAbsurdityLevel(Integer absurdityLevel) { this.absurdityLevel = absurdityLevel; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getGeneralForecast() { return generalForecast; }
    public void setGeneralForecast(String generalForecast) { this.generalForecast = generalForecast; }

    public String getCareerBlock() { return careerBlock; }
    public void setCareerBlock(String careerBlock) { this.careerBlock = careerBlock; }

    public String getDangerousDays() { return dangerousDays; }
    public void setDangerousDays(String dangerousDays) { this.dangerousDays = dangerousDays; }

    public String getWhatNotToDo() { return whatNotToDo; }
    public void setWhatNotToDo(String whatNotToDo) { this.whatNotToDo = whatNotToDo; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}