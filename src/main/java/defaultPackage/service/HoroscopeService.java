package defaultPackage.service;

import defaultPackage.dto.HoroscopeGenerateRequest;
import defaultPackage.dto.HoroscopeResponse;
import defaultPackage.entity.HoroscopeRequest;
import defaultPackage.entity.User;
import defaultPackage.repository.HoroscopeRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HoroscopeService {
    private final HoroscopeRequestRepository requestRepository;
    public HoroscopeService(HoroscopeRequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    public HoroscopeResponse createRequest(User user, HoroscopeGenerateRequest request) {
        HoroscopeRequest entity = new HoroscopeRequest();
        entity.setUser(user);
        entity.setCharacteristic(request.getCharacteristic());
        entity.setTone(request.getTone());
        entity.setFormality(request.getFormality());
        entity.setAbsurdityLevel(request.getAbsurdityLevel());
        entity.setStatus("DRAFT");
        HoroscopeRequest saved = requestRepository.save(entity);
        return toResponse(saved);
    }

    public HoroscopeResponse generateHoroscope(UUID requestId, User user) {
        HoroscopeRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Запрос не найден"));

        if (!request.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Доступ запрещён");
        }
        request.setGeneralForecast("✨ Общий прогноз для «" + request.getCharacteristic() +
                "»: Сегодня отличный день для новых начинаний! Звёзды благоволят вам.");
        request.setCareerBlock("💼 Карьерный прогноз: Вас ждёт повышение, если будете усердно работать. " +
                "Начальство заметит ваши старания.");
        request.setDangerousDays("⚠️ Опасные дни: Остерегайтесь понедельников и дедлайнов. " +
                "Не подписывайте важные документы в пятницу вечером.");
        request.setWhatNotToDo("🚫 Что не делать: Не откладывайте дела на завтра. " +
                "Не спорьте с тимлидом при полной луне.");
        request.setStatus("COMPLETED");
        request.setCompletedAt(LocalDateTime.now());

        HoroscopeRequest saved = requestRepository.save(request);
        return toResponse(saved);
    }

    public List<HoroscopeResponse> getUserHistory(User user) {
        return requestRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public HoroscopeResponse getRequest(UUID requestId, User user) {
        HoroscopeRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Запрос не найден"));

        if (!request.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Доступ запрещён");
        }

        return toResponse(request);
    }

    public HoroscopeResponse saveResult(UUID requestId, User user) {
        HoroscopeRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Запрос не найден"));

        if (!request.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Доступ запрещён");
        }

        request.setStatus("SAVED");
        HoroscopeRequest saved = requestRepository.save(request);
        return toResponse(saved);
    }

    public void deleteRequest(UUID requestId, User user) {
        HoroscopeRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Запрос не найден"));

        if (!request.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Доступ запрещён");
        }

        requestRepository.delete(request);
    }

    public List<HoroscopeResponse> compareRequests(UUID id1, UUID id2, User user) {
        HoroscopeRequest request1 = requestRepository.findById(id1)
                .orElseThrow(() -> new RuntimeException("Первый запрос не найден"));
        HoroscopeRequest request2 = requestRepository.findById(id2)
                .orElseThrow(() -> new RuntimeException("Второй запрос не найден"));

        if (!request1.getUser().getId().equals(user.getId()) ||
                !request2.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Доступ запрещён");
        }

        return List.of(toResponse(request1), toResponse(request2));
    }

    public List<HoroscopeResponse> getSimilarRequests(User user, String characteristic) {
        return requestRepository
                .findByUserAndCharacteristicOrderByCreatedAtDesc(user, characteristic)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private HoroscopeResponse toResponse(HoroscopeRequest entity) {
        return new HoroscopeResponse(
                entity.getId(),
                entity.getCharacteristic(),
                entity.getTone(),
                entity.getFormality(),
                entity.getAbsurdityLevel(),
                entity.getStatus(),
                entity.getGeneralForecast(),
                entity.getCareerBlock(),
                entity.getDangerousDays(),
                entity.getWhatNotToDo(),
                entity.getCreatedAt(),
                entity.getCompletedAt()
        );
    }
}