package defaultPackage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class HoroscopeGenerateRequest {

    @NotBlank(message = "Характеристика обязательна")
    @Size(min = 2, max = 500, message = "Характеристика должна быть от 2 до 500 символов")
    private String characteristic;

    @NotBlank(message = "Тональность обязательна")
    private String tone;

    @NotBlank(message = "Формальность обязательна")
    private String formality;

    @Min(0)
    @Max(100)
    private Integer absurdityLevel;

    public String getCharacteristic() { return characteristic; }
    public void setCharacteristic(String characteristic) { this.characteristic = characteristic; }

    public String getTone() { return tone; }
    public void setTone(String tone) { this.tone = tone; }

    public String getFormality() { return formality; }
    public void setFormality(String formality) { this.formality = formality; }

    public Integer getAbsurdityLevel() { return absurdityLevel; }
    public void setAbsurdityLevel(Integer absurdityLevel) { this.absurdityLevel = absurdityLevel; }
}