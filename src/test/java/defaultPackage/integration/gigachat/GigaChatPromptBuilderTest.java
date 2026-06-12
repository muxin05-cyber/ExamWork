package defaultPackage.integration.gigachat;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GigaChatPromptBuilderTest {
    private final GigaChatPromptBuilder builder = new GigaChatPromptBuilder();
    @Test
    void parseResponse_ValidJson_ShouldReturnAllFields() {
        String json = "{" +
                "\"generalForecast\": \"День хорош\"," +
                "\"careerBlock\": \"Работайте усердно\"," +
                "\"dangerousDays\": \"Остерегайтесь среды\"," +
                "\"whatNotToDo\": \"Не спорьте с тимлидом\"" +
                "}";

        GigaChatResponse result = builder.parseResponse(json);
        assertEquals("День хорош", result.getGeneralForecast());
        assertEquals("Работайте усердно", result.getCareerBlock());
        assertEquals("Остерегайтесь среды", result.getDangerousDays());
        assertEquals("Не спорьте с тимлидом", result.getWhatNotToDo());
    }
    @Test
    void parseResponse_WithMarkdownBlocks_ShouldCleanAndParse() {
        String json = "```json\n" +
                "{\"generalForecast\": \"Ок\", \"careerBlock\": \"Ок\"," +
                " \"dangerousDays\": \"Ок\", \"whatNotToDo\": \"Ок\"}\n" +
                "```";
        GigaChatResponse result = builder.parseResponse(json);
        assertEquals("Ок", result.getGeneralForecast());
        assertEquals("Ок", result.getCareerBlock());
    }

    @Test
    void parseResponse_InvalidJson_ShouldReturnRawText() {
        String text = "Просто текст, не JSON";
        GigaChatResponse result = builder.parseResponse(text);
        assertEquals("Просто текст, не JSON", result.getGeneralForecast());
        assertEquals("", result.getCareerBlock());
        assertEquals("", result.getDangerousDays());
        assertEquals("", result.getWhatNotToDo());
    }

    @Test
    void buildSystemPrompt_ShouldContainAllRequiredFields() {
        String prompt = builder.buildSystemPrompt("sarcastic", "low");
        assertTrue(prompt.contains("generalForecast"));
        assertTrue(prompt.contains("careerBlock"));
        assertTrue(prompt.contains("dangerousDays"));
        assertTrue(prompt.contains("whatNotToDo"));
        assertTrue(prompt.contains("саркастичный"));
        assertTrue(prompt.contains("разговорную речь"));
    }
}