package defaultPackage.integration.gigachat.decorator;

public abstract class AbstractPromptDecorator implements PromptDecorator {
    protected final PromptDecorator wrapped;
    public AbstractPromptDecorator(PromptDecorator wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public String decorate(String prompt) {
        return wrapped.decorate(prompt);
    }
}