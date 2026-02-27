package com.petwise.domain.appointment;

import com.petwise.domain.Identifier;
import com.petwise.domain.utils.IDUtils;
import java.util.Objects;

/** Unique identifier for Appointment aggregate. */
@SuppressWarnings({
    "PMD.ClassWithOnlyPrivateConstructorsShouldBeFinal",
    "PMD.OnlyOneReturn",
    "PMD.ShortVariable",
    "PMD.ControlStatementBraces",
    "PMD.MethodArgumentCouldBeFinal",
    "PMD.LocalVariableCouldBeFinal"
})
public final class AppointmentID extends Identifier<String> {

    /** The raw UUID string value. */
    private final String value;

    private AppointmentID(final String aValue) {
        super();
        this.value = aValue;
    }

    /**
     * Creates a new unique AppointmentID.
     *
     * @return a new AppointmentID backed by a random UUID
     */
    public static AppointmentID unique() {
        return new AppointmentID(IDUtils.uuid());
    }

    /**
     * Creates an AppointmentID from an existing string value.
     *
     * @param anId the raw identifier string; must not be {@code null}
     * @return an AppointmentID wrapping the given value
     */
    @SuppressWarnings("PMD.ShortVariable")
    public static AppointmentID from(final String anId) {
        Objects.requireNonNull(anId, "'id' should not be null");
        return new AppointmentID(anId);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final AppointmentID appointmentID = (AppointmentID) other;
        return Objects.equals(value, appointmentID.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
