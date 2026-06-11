package defaultPackage.integration.gigachat.decorator;

public class FormalityDecorator extends AbstractPromptDecorator {
    private final String formality;

    public FormalityDecorator(PromptDecorator wrapped, String formality) {
        super(wrapped);
        this.formality = formality;
    }

    @Override
    public String decorate(String prompt) {
        String base = super.decorate(prompt);
        String formalityInstruction = switch (formality) {
            case "low" -> " Общайся на «ты», используй разговорную речь, сленг и мемы. " +
                    "Пиши так, будто переписываешься с другом в Telegram.";
            case "medium" -> " Используй умеренно-формальный стиль. " +
                    "Обращайся на «Вы», но без излишнего официоза. " +
                    "Золотая середина между дружеским чатом и корпоративным письмом.";
            case "high" -> " Используй строгий официальный стиль. " +
                    "Обращайся исключительно на «Вы». " +
                    "Пиши как дипломат или корпоративный юрист. " +
                    "Никакого сленга и панибратства.";
            default -> "";
        };
        return base + formalityInstruction;
    }
}