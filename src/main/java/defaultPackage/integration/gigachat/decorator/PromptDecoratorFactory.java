package defaultPackage.integration.gigachat.decorator;

import org.springframework.stereotype.Component;

@Component
public class PromptDecoratorFactory {
    public PromptDecorator createDecorator(int absurdityLevel, String tone, String formality) {
        PromptDecorator decorator = new BasePromptDecorator();
        decorator = new ToneDecorator(decorator, tone);
        decorator = new AbsurdityDecorator(decorator, absurdityLevel);
        decorator = new FormalityDecorator(decorator, formality);
        return decorator;
    }
}