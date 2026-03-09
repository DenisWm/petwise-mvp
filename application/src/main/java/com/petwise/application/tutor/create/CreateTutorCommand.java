package com.petwise.application.tutor.create;

/** Command for CreateTutorUseCase. */
public record CreateTutorCommand(String name, String email, String phone) {
    public static CreateTutorCommand with(
            final String name, final String email, final String phone) {
        return new CreateTutorCommand(name, email, phone);
    }
}
