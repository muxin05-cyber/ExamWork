package defaultPackage.integration.gigachat;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "gigachat")
public class GigaChatProperties {
    private String apiUrl = "https://gigachat.devices.sberbank.ru/api/v1";
    private String authUrl = "https://ngw.devices.sberbank.ru:9443/api/v2/oauth";
    private String clientId;
    private String scope = "GIGACHAT_API_PERS";
    public String getApiUrl() { return apiUrl; }
    public void setApiUrl(String apiUrl) { this.apiUrl = apiUrl; }

    public String getAuthUrl() { return authUrl; }
    public void setAuthUrl(String authUrl) { this.authUrl = authUrl; }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }

    public String getScope() { return scope; }
    public void setScope(String scope) { this.scope = scope; }
}