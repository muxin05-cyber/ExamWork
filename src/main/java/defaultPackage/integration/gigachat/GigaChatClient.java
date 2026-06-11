package defaultPackage.integration.gigachat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;

@Service
public class GigaChatClient {
    private final GigaChatProperties properties;
    private final GigaChatTokenService tokenService;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GigaChatClient(GigaChatProperties properties, GigaChatTokenService tokenService) {
        this.properties = properties;
        this.tokenService = tokenService;
        this.restTemplate = createRestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    private RestTemplate createRestTemplate() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HostnameVerifier allHostsValid = (hostname, session) -> true;
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            return new RestTemplate();
        } catch (Exception e) {
            return new RestTemplate();
        }
    }

    public String generateText(String systemPrompt, String userPrompt) {
        try {
            String token = tokenService.getAccessToken();
            ObjectNode body = objectMapper.createObjectNode();
            body.put("model", "GigaChat");
            body.put("temperature", 1.0);
            body.put("max_tokens", 2000);
            ArrayNode messages = objectMapper.createArrayNode();
            ObjectNode systemMessage = objectMapper.createObjectNode();
            systemMessage.put("role", "system");
            systemMessage.put("content", systemPrompt);
            messages.add(systemMessage);
            ObjectNode userMessage = objectMapper.createObjectNode();
            userMessage.put("role", "user");
            userMessage.put("content", userPrompt);
            messages.add(userMessage);
            body.set("messages", messages);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Accept", "application/json");

            HttpEntity<String> request = new HttpEntity<>(body.toString(), headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    properties.getApiUrl() + "/chat/completions",
                    request,
                    String.class
            );
            JsonNode json = objectMapper.readTree(response.getBody());
            JsonNode choices = json.get("choices");
            if (choices != null && choices.isArray() && choices.size() > 0) {
                JsonNode message = choices.get(0).get("message");
                if (message != null) {
                    String content = message.get("content").asText();
                    System.out.println("✅ GigaChat ответ получен: " +
                            content.substring(0, Math.min(50, content.length())) + "...");
                    return content;
                }
            }
            throw new RuntimeException("Пустой ответ от GigaChat");
        } catch (Exception e) {
            System.err.println("❌ Ошибка при обращении к GigaChat: " + e.getMessage());
            if (e.getMessage().contains("401") || e.getMessage().contains("Unauthorized")) {
                tokenService.resetToken();
            }
            throw new RuntimeException("Ошибка при обращении к GigaChat: " + e.getMessage(), e);
        }
    }
}