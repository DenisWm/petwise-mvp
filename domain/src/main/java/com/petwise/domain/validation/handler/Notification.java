package com.petwise.domain.validation.handler;

import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.validation.Error;
import com.petwise.domain.validation.ValidationHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Standard {@link ValidationHandler} that accumulates errors without interrupting the validation
 * flow.
 *
 * <p>Use the static factories to create instances: {@link #create()}, {@link #create(Error)},
 * {@link #create(Throwable)}.
 */
@SuppressWarnings({
    "PMD.AvoidCatchingThrowable",
    "PMD.ShortVariable",
    "PMD.MethodArgumentCouldBeFinal",
    "PMD.ClassWithOnlyPrivateConstructorsShouldBeFinal",
    "PMD.OnlyOneReturn"
})
public final class Notification implements ValidationHandler {

    /** Accumulated validation errors. */
    private final List<Error> errors;

    private Notification(final List<Error> anErrors) {
        this.errors = anErrors;
    }

    /**
     * Creates an empty {@code Notification}.
     *
     * @return a new, empty {@code Notification}
     */
    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    /**
     * Creates a {@code Notification} seeded from a {@link Throwable}.
     *
     * @param aThrowable the throwable whose message is the initial error
     * @return a new {@code Notification} containing one error
     */
    public static Notification create(final Throwable aThrowable) {
        return create(new Error(aThrowable.getMessage()));
    }

    /**
     * Creates a {@code Notification} seeded with an {@link Error}.
     *
     * @param anError the initial error
     * @return a new {@code Notification} containing one error
     */
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
        return Collections.unmodifiableList(new ArrayList<>(this.errors));
    }
}
