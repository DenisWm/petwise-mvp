package com.petwise.domain.validation;

import java.util.Objects;

/**
 * Abstract base class for domain validators.
 *
 * <p>Concrete validators encapsulate all invariant-checking logic for a given entity or value
 * object. They append {@link Error}s to the injected {@link ValidationHandler} rather than throwing
 * exceptions, which allows every broken rule to be reported at once.
 *
 * <p>Example:
 *
 * <pre>{@code
 * public class TutorValidator extends Validator {
 *     public TutorValidator(Tutor tutor, ValidationHandler handler) {
 *         super(handler);
 *         this.tutor = tutor;
 *     }
 *
 *     {@literal @}Override
 *     public void validate() {
 *         checkNameConstraints();
 *     }
 * }
 * }</pre>
 */
public abstract class Validator {

    /** The handler that accumulates validation errors. */
    private final ValidationHandler validationHandler;

    /**
     * Constructs a Validator bound to the given {@link ValidationHandler}.
     *
     * @param handler the handler that will accumulate errors; must not be {@code null}
     * @throws NullPointerException if {@code handler} is {@code null}
     */
    public Validator(final ValidationHandler handler) {
        this.validationHandler =
                Objects.requireNonNull(handler, "validationHandler should not be null");
    }

    /**
     * Executes all validation checks and appends any violated constraints to the {@link
     * ValidationHandler}.
     */
    public abstract void validate();

    /**
     * Returns the {@link ValidationHandler} used to collect errors.
     *
     * @return the validation handler; never {@code null}
     */
    public ValidationHandler getValidationHandler() {
        return validationHandler;
    }
}
