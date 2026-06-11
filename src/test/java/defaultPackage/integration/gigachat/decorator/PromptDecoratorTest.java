package defaultPackage.integration.gigachat.decorator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PromptDecoratorTest {

    @Test
    void absurdityDecorator_Low_ShouldAddRealistic() {
        PromptDecorator decorator = new AbsurdityDecorator(new BasePromptDecorator(), 20);
        String result = decorator.decorate("Тест.");
        assertTrue(result.contains("реалистичным"));
        assertTrue(result.contains("логика"));
    }

    @Test
    void absurdityDecorator_High_ShouldAddSurreal() {
        PromptDecorator decorator = new AbsurdityDecorator(new BasePromptDecorator(), 90);
        String result = decorator.decorate("Тест.");
        assertTrue(result.contains("сюрреализм"));
        assertTrue(result.contains("драконы"));
    }

    @Test
    void toneDecorator_Sarcastic_ShouldAddSarcasm() {
        PromptDecorator decorator = new ToneDecorator(new BasePromptDecorator(), "sarcastic");
        String result = decorator.decorate("Тест.");
        assertTrue(result.contains("саркастичный"));
        assertTrue(result.contains("IT-шутки"));
    }

    @Test
    void decoratorChain_ShouldCombineAll() {
        PromptDecorator decorator = new BasePromptDecorator();
        decorator = new ToneDecorator(decorator, "motivational");
        decorator = new AbsurdityDecorator(decorator, 80);
        decorator = new FormalityDecorator(decorator, "high");

        String result = decorator.decorate("Тест.");
        assertTrue(result.contains("Тест."));
        assertTrue(result.contains("мотивационный"));
        assertTrue(result.contains("офисные зомби"));
        assertTrue(result.contains("строгий официальный"));
    }
}