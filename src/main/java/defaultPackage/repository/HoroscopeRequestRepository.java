package defaultPackage.repository;

import defaultPackage.entity.HoroscopeRequest;
import defaultPackage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface HoroscopeRequestRepository extends JpaRepository<HoroscopeRequest, UUID> {

    List<HoroscopeRequest> findByUserOrderByCreatedAtDesc(User user);
    List<HoroscopeRequest> findByUserAndStatusOrderByCreatedAtDesc(User user, String status);
    List<HoroscopeRequest> findByUserAndCharacteristicOrderByCreatedAtDesc(User user, String characteristic);
}