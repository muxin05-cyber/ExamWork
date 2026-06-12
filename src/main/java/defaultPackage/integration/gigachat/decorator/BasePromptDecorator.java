package defaultPackage.integration.gigachat.decorator;

public class BasePromptDecorator implements PromptDecorator {
    @Override
    public String decorate(String prompt) {
        return prompt;
    }
}
