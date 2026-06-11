package defaultPackage.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class HoroscopeResponse {
    private UUID id;
    private String characteristic;
    private String tone;
    private String formality;
    private Integer absurdityLevel;
    private String status;
    private String generalForecast;
    private String careerBlock;
    private String dangerousDays;
    private String whatNotToDo;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String forDate;

    public HoroscopeResponse(UUID id, String characteristic, String tone, String formality, Integer absurdityLevel,
                             String status, String generalForecast, String careerBlock, String dangerousDays,
                             String whatNotToDo, LocalDateTime createdAt, LocalDateTime completedAt) {
        this.id = id;
        this.characteristic = characteristic;
        this.tone = tone;
        this.formality = formality;
        this.absurdityLevel = absurdityLevel;
        this.status = status;
        this.generalForecast = generalForecast;
        this.careerBlock = careerBlock;
        this.dangerousDays = dangerousDays;
        this.whatNotToDo = whatNotToDo;
        this.createdAt = createdAt;
        this.completedAt = completedAt;
        this.forDate = createdAt != null ? createdAt.toLocalDate().toString() : "";
    }

    public UUID getId() { return id; }
    public String getCharacteristic() { return characteristic; }
    public String getTone() { return tone; }
    public String getFormality() { return formality; }
    public Integer getAbsurdityLevel() { return absurdityLevel; }
    public String getStatus() { return status; }
    public String getGeneralForecast() { return generalForecast; }
    public String getCareerBlock() { return careerBlock; }
    public String getDangerousDays() { return dangerousDays; }
    public String getWhatNotToDo() { return whatNotToDo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public String getForDate() { return forDate; }
}