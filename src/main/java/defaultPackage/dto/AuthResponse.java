package defaultPackage.dto;

import java.util.UUID;

public class AuthResponse {
    private String token;
    private UUID userId;
    private String username;

    public AuthResponse(String token, UUID userId, String username) {
        this.token = token;
        this.userId = userId;
        this.username = username;
    }

    public String getToken() { return token; }
    public UUID getUserId() { return userId; }
    public String getUsername() { return username; }
}