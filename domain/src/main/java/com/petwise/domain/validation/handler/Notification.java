package com.petwise.domain.validation.handler;

import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.validation.Error;
import com.petwise.domain.validation.ValidationHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * Standard {@link ValidationHandler} implementation that accumulates errors without interrupting
 * the validation flow.
 *
 * <p>Use the static factory methods to create instances:
 * <ul>
 *   <li>{@link #create()} – empty notification</li>
 *   <li>{@link #create(Error)} – notification seeded with one error</li>
 *   <li>{@link #create(Throwable)} – notification seeded from an exception message</li>
 * </ul>
 *
 * <p>The {@link #validate(Validation)} method executes a block of logic and silently
 * absorbs any {@link DomainException} (appending its errors) or any other {@link Throwable}
 * (appending its message), keeping the caller's flow uninterrupted.
 */
public class Notification implements ValidationHandler {

    private final List<Error> errors;

    private Notification(final List<Error> errors) {
        this.errors = errors;
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
     * Creates a {@code Notification} seeded with the message from the given {@link Throwable}.
     *
     * @param t the throwable whose message will be the initial error
     * @return a new {@code Notification} containing one error
     */
    public static Notification create(final Throwable t) {
        return create(new Error(t.getMessage()));
    }

    /**
     * Creates a {@code Notification} seeded with the given {@link Error}.
     *
     * @param anError the initial error
     * @return a new {@code Notification} containing one error
     */
    public static Notification create(Error anError) {
        return new Notification(new ArrayList<>()).append(anError);
    }

    @Override
    public Notification append(Error anError) {
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
        } catch (DomainException ex) {
            this.errors.addAll(ex.getErrors());
        } catch (Throwable t) {
            this.errors.add(new Error(t.getMessage()));
        }
        return null;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }
}
