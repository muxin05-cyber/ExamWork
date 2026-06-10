package defaultPackage.controller;

import defaultPackage.dto.AuthResponse;
import defaultPackage.dto.LoginRequest;
import defaultPackage.dto.RegisterRequest;
import defaultPackage.entity.User;
import defaultPackage.security.JwtService;
import defaultPackage.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("{\"error\": \"Email уже занят\"}");
        }

        User user = userService.register(
                request.getEmail(),
                request.getUsername(),
                request.getPassword()
        );

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userService.authenticate(request.getEmail(), request.getPassword());

        if (user == null) {
            return ResponseEntity.status(401).body("{\"error\": \"Неверные учетные данные\"}");
        }

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getUsername()));
    }
}