package defaultPackage.repository;

import defaultPackage.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    @Test
    void findByEmail_WhenUserExists_ShouldReturnUser() {
        User user = new User();
        user.setEmail("repo@test.com");
        user.setUsername("RepoUser");
        user.setPasswordHash("hashed_password");
        entityManager.persistAndFlush(user);
        Optional<User> found = userRepository.findByEmail("repo@test.com");
        assertTrue(found.isPresent());
        assertEquals("RepoUser", found.get().getUsername());
        assertEquals("repo@test.com", found.get().getEmail());
    }

    @Test
    void existsByEmail_WhenExists_ShouldReturnTrue() {
        User user = new User();
        user.setEmail("exists@test.com");
        user.setUsername("ExistsUser");
        user.setPasswordHash("hash");
        entityManager.persistAndFlush(user);
        assertTrue(userRepository.existsByEmail("exists@test.com"));
        assertFalse(userRepository.existsByEmail("notfound@test.com"));
    }

    @Test
    void save_ShouldGenerateIdAndTimestamp() {
        User user = new User();
        user.setEmail("new@test.com");
        user.setUsername("NewUser");
        user.setPasswordHash("hash");
        User saved = userRepository.save(user);
        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt());
        assertEquals("new@test.com", saved.getEmail());
        assertEquals("NewUser", saved.getUsername());
    }
}