package com.petwise.domain.validation.handler;

import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.validation.Error;
import com.petwise.domain.validation.ValidationHandler;
import java.util.ArrayList;
import java.util.List;

/** Standard {@link ValidationHandler} that accumulates errors without interrupting the flow. */
@SuppressWarnings({
    "PMD.AvoidCatchingThrowable",
    "PMD.ShortVariable",
    "PMD.MethodArgumentCouldBeFinal",
    "PMD.ClassWithOnlyPrivateConstructorsShouldBeFinal",
    "PMD.OnlyOneReturn"
})
public final class Notification implements ValidationHandler {
    private final List<Error> errors;

    private Notification(final List<Error> anErrors) {
        this.errors = anErrors;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Throwable aThrowable) {
        return create(new Error(aThrowable.getMessage()));
    }

    public static Notification create(final Error anError) {
        return new Notification(new ArrayList<>()).append(anError);
    }

    @Override
    public Notification append(final Error anError) {
        this.errors.add(anError);
        return this;
    }

    @Override
    public ValidationHandler append(final ValidationHandler aHandler) {
        this.errors.addAll(aHandler.getErrors());
        return this;
    }

    @Override
    public <T> T validate(final Validation<T> aValidation) {
        try {
            return aValidation.validate();
        } catch (final DomainException ex) {
            this.errors.addAll(ex.getErrors());
        } catch (final Throwable throwable) { // NOPMD
            this.errors.add(new Error(throwable.getMessage()));
        }
        return null;
    }

    @Override
    public List<Error> getErrors() {
        return List.copyOf(this.errors);
    }
}
