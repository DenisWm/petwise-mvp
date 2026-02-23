package com.petwise.application.tutor.update;

/**
 * Command for UpdateTutorUseCase.
 *
 * @param id the tutor ID (required)
 * @param name the tutor name (required)
 * @param email the tutor email (optional)
 * @param phone the tutor phone (optional)
 */
@SuppressWarnings("PMD.ShortVariable")
public record UpdateTutorCommand(String id, String name, String email, String phone) {

    /**
     * Factory method for creating an update command.
     *
     * @param id the tutor ID
     * @param name the tutor name
     * @param email the tutor email
     * @param phone the tutor phone
     * @return a new {@code UpdateTutorCommand}
     */
    public static UpdateTutorCommand with(
            final String id, final String name, final String email, final String phone) {
        return new UpdateTutorCommand(id, name, email, phone);
    }
}
