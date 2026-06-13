package defaultPackage.integration.gigachat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class GigaChatPromptBuilder {
    public String buildSystemPrompt() {
        StringBuilder sb = new StringBuilder();
        sb.append("Ты — генератор структурированных гороскопов. ");
        sb.append("Отвечай строго в формате JSON с полями: ");
        sb.append("generalForecast (общий прогноз, 2-3 предложения), ");
        sb.append("careerBlock (карьерный прогноз, 2-3 предложения), ");
        sb.append("dangerousDays (опасные дни, 1-2 предложения), ");
        sb.append("whatNotToDo (что не делать, 1-2 предложения). ");
        sb.append("Не добавляй никакого текста вне JSON.");
        return sb.toString();
    }

    public GigaChatResponse parseResponse(String jsonResponse) {
        try {
            String cleanJson = jsonResponse
                    .replaceAll("```json", "")
                    .replaceAll("```", "")
                    .trim();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root;
            try {
                root = mapper.readTree(cleanJson);
            } catch (Exception e) {
                throw new RuntimeException("Invalid JSON: " + cleanJson);
            }
            if (root.has("choices") && root.get("choices").isArray() && root.get("choices").size() > 0) {
                JsonNode firstChoice = root.get("choices").get(0);
                if (firstChoice.has("message") && firstChoice.get("message").has("content")) {
                    String content = firstChoice.get("message").get("content").asText();
                    try {
                        root = mapper.readTree(content);
                    } catch (Exception ex) {
                        GigaChatResponse fallback = new GigaChatResponse();
                        fallback.setGeneralForecast(content);
                        return fallback;
                    }
                }
            }
            GigaChatResponse response = new GigaChatResponse();
            response.setGeneralForecast(root.has("generalForecast") ? root.get("generalForecast").asText() : "");
            response.setCareerBlock(root.has("careerBlock") ? root.get("careerBlock").asText() : "");
            response.setDangerousDays(root.has("dangerousDays") ? root.get("dangerousDays").asText() : "");
            response.setWhatNotToDo(root.has("whatNotToDo") ? root.get("whatNotToDo").asText() : "");
            return response;
        } catch (Exception e) {
            GigaChatResponse fallback = new GigaChatResponse();
            fallback.setGeneralForecast(jsonResponse);
            fallback.setCareerBlock("");
            fallback.setDangerousDays("");
            fallback.setWhatNotToDo("");
            return fallback;
        }
    }
}