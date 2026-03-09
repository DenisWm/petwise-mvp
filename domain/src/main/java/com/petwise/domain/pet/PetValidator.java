package com.petwise.domain.pet;

import com.petwise.domain.validation.Error;
import com.petwise.domain.validation.ValidationHandler;
import com.petwise.domain.validation.Validator;
import java.time.LocalDate;

/** Validator for Pet entity. */
@SuppressWarnings("PMD.OnlyOneReturn")
public final class PetValidator extends Validator {

    private static final int NAME_MAX_LENGTH = 255;
    private static final int SPECIES_MAX_LENGTH = 255;
    private static final int BREED_MAX_LENGTH = 255;
    private static final int NOTES_MAX_LENGTH = 1000;

    private final Pet pet;

    public PetValidator(final Pet aPet, final ValidationHandler handler) {
        super(handler);
        this.pet = aPet;
    }

    @Override
    public void validate() {
        checkTutorIdConstraints();
        checkNameConstraints();
        checkOptionalTextConstraints();
        checkBirthDateConstraints();
    }

    private void checkTutorIdConstraints() {
        if (pet.getTutorId() == null) {
            this.getValidationHandler().append(new Error("'tutorId' should not be null"));
        }
    }

    private void checkNameConstraints() {
        final var name = this.pet.getName();
        if (name == null) {
            this.getValidationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if (name.isBlank()) {
            this.getValidationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final int length = name.trim().length();
        if (length > NAME_MAX_LENGTH) {
            this.getValidationHandler()
                    .append(new Error("'name' must be between 1 and 255 characters"));
        }
    }

    private void checkOptionalTextConstraints() {
        checkOptionalTextField(pet.getSpecies(), SPECIES_MAX_LENGTH, "species");
        checkOptionalTextField(pet.getBreed(), BREED_MAX_LENGTH, "breed");
        checkOptionalTextField(pet.getNotes(), NOTES_MAX_LENGTH, "notes");
    }

    private void checkOptionalTextField(
            final String value, final int maxLength, final String field) {
        if (value == null) {
            return;
        }
        if (value.isBlank()) {
            this.getValidationHandler().append(new Error("'" + field + "' should not be empty"));
            return;
        }
        if (value.trim().length() > maxLength) {
            this.getValidationHandler()
                    .append(
                            new Error(
                                    "'"
                                            + field
                                            + "' must be between 1 and "
                                            + maxLength
                                            + " characters"));
        }
    }

    private void checkBirthDateConstraints() {
        final LocalDate birthDate = pet.getBirthDate();
        if (birthDate == null) {
            return;
        }
        if (birthDate.isAfter(LocalDate.now())) {
            this.getValidationHandler().append(new Error("'birthDate' must not be in the future"));
        }
    }
}
