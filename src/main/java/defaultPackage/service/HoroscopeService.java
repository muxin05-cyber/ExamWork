package defaultPackage.service;

import defaultPackage.dto.HoroscopeGenerateRequest;
import defaultPackage.dto.HoroscopeResponse;
import defaultPackage.entity.HoroscopeRequest;
import defaultPackage.entity.User;
import defaultPackage.integration.gigachat.GigaChatClient;
import defaultPackage.integration.gigachat.GigaChatPromptBuilder;
import defaultPackage.integration.gigachat.GigaChatResponse;
import defaultPackage.integration.gigachat.decorator.PromptDecorator;
import defaultPackage.integration.gigachat.decorator.PromptDecoratorFactory;
import defaultPackage.repository.HoroscopeRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HoroscopeService {

    private final HoroscopeRequestRepository requestRepository;
    private final GigaChatClient gigaChatClient;
    private final GigaChatPromptBuilder promptBuilder;
    private final PromptDecoratorFactory decoratorFactory;
    private final AiLogService aiLogService;

    public HoroscopeService(HoroscopeRequestRepository requestRepository,
                            GigaChatClient gigaChatClient,
                            GigaChatPromptBuilder promptBuilder,
                            PromptDecoratorFactory decoratorFactory,
                            AiLogService aiLogService) {
        this.requestRepository = requestRepository;
        this.gigaChatClient = gigaChatClient;
        this.promptBuilder = promptBuilder;
        this.decoratorFactory = decoratorFactory;
        this.aiLogService = aiLogService;
    }

    public boolean canMakeRequest(UUID userId) {
        return aiLogService.canMakeRequest(userId);
    }

    public long getRemainingRequests(UUID userId) {
        return aiLogService.getRemainingRequests(userId);
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

        if (!aiLogService.canMakeRequest(user.getId())) {
            request.setStatus("FAILED");
            request.setGeneralForecast("Достигнут лимит запросов на сегодня (20). Попробуйте завтра.");
            requestRepository.save(request);
            throw new RuntimeException("Достигнут лимит запросов на сегодня (20). Попробуйте завтра.");
        }

        try {
            String systemPrompt = promptBuilder.buildSystemPrompt(
                    request.getTone(), request.getFormality());

            String baseUserPrompt = "Сгенерируй гороскоп для: «" +
                    request.getCharacteristic() + "». " +
                    "Верни ТОЛЬКО JSON, без дополнительного текста.";

            PromptDecorator decorator = decoratorFactory.createDecorator(
                    request.getAbsurdityLevel(),
                    request.getTone(),
                    request.getFormality()
            );
            String decoratedUserPrompt = decorator.decorate(baseUserPrompt);

            String aiResponse = gigaChatClient.generateText(systemPrompt, decoratedUserPrompt);
            GigaChatResponse parsed = promptBuilder.parseResponse(aiResponse);

            request.setGeneralForecast(parsed.getGeneralForecast());
            request.setCareerBlock(parsed.getCareerBlock());
            request.setDangerousDays(parsed.getDangerousDays());
            request.setWhatNotToDo(parsed.getWhatNotToDo());
            request.setStatus("COMPLETED");
            request.setCompletedAt(LocalDateTime.now());

            aiLogService.logSuccess(
                    user,
                    "GENERATE",
                    decoratedUserPrompt,
                    aiResponse != null ? aiResponse.substring(0, Math.min(200, aiResponse.length())) : "",
                    null
            );

        } catch (Exception e) {
            aiLogService.logFailure(user, "GENERATE", request.getCharacteristic(), e.getMessage());
            request.setStatus("FAILED");
            request.setGeneralForecast("Ошибка генерации: " + e.getMessage());
        }

        HoroscopeRequest saved = requestRepository.save(request);
        return toResponse(saved);
    }

    public List<HoroscopeResponse> getUserHistory(User user) {
        return requestRepository.findByUserOrderByCreatedAtDesc(user)
                .stream().map(this::toResponse).collect(Collectors.toList());
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
        return toResponse(requestRepository.save(request));
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
        HoroscopeRequest r1 = requestRepository.findById(id1)
                .orElseThrow(() -> new RuntimeException("Первый запрос не найден"));
        HoroscopeRequest r2 = requestRepository.findById(id2)
                .orElseThrow(() -> new RuntimeException("Второй запрос не найден"));
        if (!r1.getUser().getId().equals(user.getId()) || !r2.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Доступ запрещён");
        }
        return List.of(toResponse(r1), toResponse(r2));
    }

    public List<HoroscopeResponse> getSimilarRequests(User user, String characteristic) {
        return requestRepository
                .findByUserAndCharacteristicOrderByCreatedAtDesc(user, characteristic)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private HoroscopeResponse toResponse(HoroscopeRequest entity) {
        return new HoroscopeResponse(
                entity.getId(), entity.getCharacteristic(), entity.getTone(),
                entity.getFormality(), entity.getAbsurdityLevel(), entity.getStatus(),
                entity.getGeneralForecast(), entity.getCareerBlock(),
                entity.getDangerousDays(), entity.getWhatNotToDo(),
                entity.getCreatedAt(), entity.getCompletedAt()
        );
    }
}