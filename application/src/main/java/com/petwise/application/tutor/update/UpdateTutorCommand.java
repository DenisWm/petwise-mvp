package com.petwise.application.tutor.update;

/**
 * Command for UpdateTutorUseCase.
 *
 * @param id the tutor ID (required)
 * @param name the tutor name (required)
 * @param email the tutor email (optional)
 * @param phone the tutor phone (optional)
 */
public record UpdateTutorCommand(String id, String name, String email, String phone) {

    public static UpdateTutorCommand with(
            final String id, final String name, final String email, final String phone) {
        return new UpdateTutorCommand(id, name, email, phone);
    }
}
