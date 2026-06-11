package defaultPackage.integration.gigachat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.time.Instant;

@Service
public class GigaChatTokenService {
    private final GigaChatProperties properties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private String accessToken;
    private Instant tokenExpiry;
    public GigaChatTokenService(GigaChatProperties properties) {
        this.properties = properties;
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
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
            return new RestTemplate();
        } catch (Exception e) {
            return new RestTemplate();
        }
    }
    public String getAccessToken() {
        if (accessToken != null && Instant.now().isBefore(tokenExpiry)) {
            return accessToken;
        }
        try {
            String authKey = properties.getClientId();

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("scope", properties.getScope());
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set("Authorization", "Bearer " + authKey);
            headers.set("RqUID", java.util.UUID.randomUUID().toString());

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    properties.getAuthUrl(), request, String.class);
            JsonNode json = objectMapper.readTree(response.getBody());
            accessToken = json.get("access_token").asText();
            if (json.has("expires_in")) {
                long expiresIn = json.get("expires_in").asLong();
                tokenExpiry = Instant.now().plusSeconds(expiresIn - 60);
            } else if (json.has("expires_at")) {
                long expiresAt = json.get("expires_at").asLong();
                tokenExpiry = Instant.ofEpochMilli(expiresAt).minusSeconds(60);
            } else {
                tokenExpiry = Instant.now().plusSeconds(1800 - 60);
            }
            return accessToken;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения токена GigaChat: " + e.getMessage(), e);
        }
    }

    public void resetToken() {
        accessToken = null;
        tokenExpiry = null;
    }
}