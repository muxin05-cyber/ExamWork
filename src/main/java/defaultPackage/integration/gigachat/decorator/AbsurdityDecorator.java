package defaultPackage.integration.gigachat.decorator;

public class AbsurdityDecorator extends AbstractPromptDecorator {
    private final int absurdityLevel;
    public AbsurdityDecorator(PromptDecorator wrapped, int absurdityLevel) {
        super(wrapped);
        this.absurdityLevel = absurdityLevel;
    }

    @Override
    public String decorate(String prompt) {
        String base = super.decorate(prompt);
        String absurdityInstruction;

        if (absurdityLevel <= 30) {
            absurdityInstruction = " Будь реалистичным и прагматичным. " +
                    "Никакой магии — только логика и здравый смысл. " +
                    "Предсказания должны звучать как советы опытного ментора.";
        } else if (absurdityLevel <= 60) {
            absurdityInstruction = " Добавь немного неожиданных метафор и лёгкого сюрреализма. " +
                    "Разрешены: говорящие девайсы, ожившие дедлайны, " +
                    "предсказания от лица кофе-машины.";
        } else if (absurdityLevel <= 80) {
            absurdityInstruction = " Добавь много абсурда! Разрешены: летающие коты-программисты, " +
                    "межгалактические merge-конфликты, офисные зомби, " +
                    "дедлайны из параллельной вселенной. " +
                    "Реальность — это баг, а баги — это фичи.";
        } else {
            absurdityInstruction = " Максимальный уровень абсурда! Полный сюрреализм! " +
                    "Разрешено ВСЁ: летающие котлы с кодом, " +
                    "драконы в продакшене, рекурсивные сны компилятора, " +
                    "хороводы багов вокруг спринтера, " +
                    "тайные общества дедлайнов, пожирающих время. " +
                    "Логика не требуется. Чем безумнее — тем лучше!";
        }
        return base + absurdityInstruction;
    }
}