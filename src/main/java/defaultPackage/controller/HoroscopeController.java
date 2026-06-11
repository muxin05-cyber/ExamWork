package defaultPackage.controller;

import defaultPackage.dto.CompareRequest;
import defaultPackage.dto.HoroscopeGenerateRequest;
import defaultPackage.dto.HoroscopeResponse;
import defaultPackage.entity.User;
import defaultPackage.repository.UserRepository;
import defaultPackage.security.JwtService;
import defaultPackage.service.HoroscopeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/horoscope")
public class HoroscopeController {
    private final HoroscopeService horoscopeService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public HoroscopeController(HoroscopeService horoscopeService,
                               JwtService jwtService,
                               UserRepository userRepository) {
        this.horoscopeService = horoscopeService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    private User getCurrentUser(String authHeader) {
        String token = authHeader.substring(7);
        String userId = jwtService.getUserIdFromToken(token);
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }

    @PostMapping("/generate")
    public ResponseEntity<HoroscopeResponse> generate(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody HoroscopeGenerateRequest request) {
        User user = getCurrentUser(authHeader);
        HoroscopeResponse response = horoscopeService.createRequest(user, request);
        response = horoscopeService.generateHoroscope(response.getId(), user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<HoroscopeResponse>> getHistory(
            @RequestHeader("Authorization") String authHeader) {
        User user = getCurrentUser(authHeader);
        List<HoroscopeResponse> history = horoscopeService.getUserHistory(user);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HoroscopeResponse> getRequest(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id) {
        User user = getCurrentUser(authHeader);
        HoroscopeResponse response = horoscopeService.getRequest(id, user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/save")
    public ResponseEntity<HoroscopeResponse> saveResult(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id) {
        User user = getCurrentUser(authHeader);
        HoroscopeResponse response = horoscopeService.saveResult(id, user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/regenerate")
    public ResponseEntity<HoroscopeResponse> regenerate(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id,
            @RequestBody HoroscopeGenerateRequest request) {
        User user = getCurrentUser(authHeader);
        HoroscopeResponse response = horoscopeService.createRequest(user, request);
        response = horoscopeService.generateHoroscope(response.getId(), user);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRequest(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id) {
        User user = getCurrentUser(authHeader);
        horoscopeService.deleteRequest(id, user);
        return ResponseEntity.ok().body("{\"message\": \"Удалено\"}");
    }

    @PostMapping("/compare")
    public ResponseEntity<List<HoroscopeResponse>> compareHoroscopes(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody CompareRequest request) {
        User user = getCurrentUser(authHeader);
        List<HoroscopeResponse> responses = horoscopeService.compareRequests(
                request.getId1(), request.getId2(), user);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/history/similar")
    public ResponseEntity<List<HoroscopeResponse>> getSimilarRequests(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String characteristic) {
        User user = getCurrentUser(authHeader);
        List<HoroscopeResponse> similar = horoscopeService.getSimilarRequests(user, characteristic);
        return ResponseEntity.ok(similar);
    }
}