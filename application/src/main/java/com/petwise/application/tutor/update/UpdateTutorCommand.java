package com.petwise.application.tutor.update;

/** Command for UpdateTutorUseCase. */
@SuppressWarnings("PMD.ShortVariable")
public record UpdateTutorCommand(String id, String name, String email, String phone) {

    public static UpdateTutorCommand with(
            final String id, final String name, final String email, final String phone) {
        return new UpdateTutorCommand(id, name, email, phone);
    }
}
