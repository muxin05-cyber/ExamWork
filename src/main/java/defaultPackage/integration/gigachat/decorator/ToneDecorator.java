package defaultPackage.integration.gigachat.decorator;

public class ToneDecorator extends AbstractPromptDecorator {
    private final String tone;
    public ToneDecorator(PromptDecorator wrapped, String tone) {
        super(wrapped);
        this.tone = tone;
    }

    @Override
    public String decorate(String prompt) {
        String base = super.decorate(prompt);
        String toneInstruction = switch (tone) {
            case "sarcastic" -> " Используй саркастичный, ироничный стиль. Добавь IT-шутки, " +
                    "пассивную агрессию и намёки на то, что пользователь сам виноват в своих проблемах. " +
                    "Шути про баги, дедлайны и конфликты.";
            case "motivational" -> " Используй мотивационный, вдохновляющий стиль. " +
                    "Будь энергичным коучем. Обещай повышение, просветление и успех. " +
                    "Используй восклицательные знаки и позитивные метафоры.";
            case "mystical" -> " Используй мистический, таинственный стиль. " +
                    "Говори загадками, упоминай звёзды, планеты и космические сущности. " +
                    "Создавай атмосферу тайны и древней магии.";
            case "serious" -> " Используй серьёзный, деловой стиль. " +
                    "Пиши как бизнес-аналитик или корпоративный консультант. " +
                    "Никаких шуток, только факты и профессиональные рекомендации.";
            default -> "";
        };
        return base + toneInstruction;
    }
}