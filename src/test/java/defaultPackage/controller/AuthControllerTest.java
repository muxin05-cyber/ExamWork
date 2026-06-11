package defaultPackage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import defaultPackage.dto.LoginRequest;
import defaultPackage.dto.RegisterRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private String uniqueEmail;

    @BeforeEach
    void setUp() {
        uniqueEmail = "test_" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";
    }

    @Test
    void register_Valid_ShouldReturnToken() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setEmail(uniqueEmail);
        req.setUsername("TestUser");
        req.setPassword("password123");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("TestUser"));
    }

    @Test
    void login_ValidCredentials_ShouldReturnToken() throws Exception {
        // Сначала регистрируем
        RegisterRequest regReq = new RegisterRequest();
        regReq.setEmail(uniqueEmail);
        regReq.setUsername("LoginUser");
        regReq.setPassword("password123");
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regReq)))
                .andExpect(status().isOk());
        LoginRequest loginReq = new LoginRequest();
        loginReq.setEmail(uniqueEmail);
        loginReq.setPassword("password123");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("LoginUser"));
    }

    @Test
    void login_InvalidPassword_ShouldReturn401() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail(uniqueEmail);
        req.setPassword("wrong_password");
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getHistory_WithoutToken_ShouldReturn403() throws Exception {
        mockMvc.perform(get("/api/horoscope/history"))
                .andExpect(status().isForbidden());
    }

    @Test
    void getHistory_WithValidToken_ShouldReturn200() throws Exception {
        RegisterRequest regReq = new RegisterRequest();
        regReq.setEmail(uniqueEmail);
        regReq.setUsername("TokenUser");
        regReq.setPassword("password123");

        String response = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(regReq)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = objectMapper.readTree(response).get("token").asText();
        mockMvc.perform(get("/api/horoscope/history")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}