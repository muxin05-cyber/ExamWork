package defaultPackage.service;

import defaultPackage.entity.User;
import defaultPackage.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    private UserService userService;
    @BeforeEach
    void setUp() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void register_ShouldSaveUser() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setUsername("TestUser");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        User result = userService.register("test@example.com", "TestUser", "password123");
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void authenticate_WithValidPassword_ShouldReturnUser() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setEmail("test@example.com");
        user.setPasswordHash(encoder.encode("password123"));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        User result = userService.authenticate("test@example.com", "password123");
        assertNotNull(result);
    }

    @Test
    void authenticate_WithInvalidPassword_ShouldReturnNull() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setEmail("test@example.com");
        user.setPasswordHash(encoder.encode("password123"));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        User result = userService.authenticate("test@example.com", "wrong");
        assertNull(result);
    }
}