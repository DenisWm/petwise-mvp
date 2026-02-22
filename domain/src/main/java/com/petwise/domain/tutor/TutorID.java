package com.petwise.domain.tutor;

import com.petwise.domain.Identifier;
import com.petwise.domain.utils.IDUtils;
import java.util.Objects;

/** Unique identifier for Tutor aggregate. */
@SuppressWarnings({
    "PMD.ClassWithOnlyPrivateConstructorsShouldBeFinal",
    "PMD.OnlyOneReturn",
    "PMD.ShortVariable",
    "PMD.ControlStatementBraces",
    "PMD.MethodArgumentCouldBeFinal",
    "PMD.LocalVariableCouldBeFinal"
})
public final class TutorID extends Identifier<String> {

    /** The raw UUID string value. */
    private final String value;

    private TutorID(final String aValue) {
        super();
        this.value = aValue;
    }

    /**
     * Creates a new unique TutorID.
     *
     * @return a new TutorID backed by a random UUID
     */
    public static TutorID unique() {
        return new TutorID(IDUtils.uuid());
    }

    /**
     * Creates a TutorID from an existing string value.
     *
     * @param anId the raw identifier string; must not be {@code null}
     * @return a TutorID wrapping the given value
     */
    @SuppressWarnings("PMD.ShortVariable")
    public static TutorID from(final String anId) {
        Objects.requireNonNull(anId, "'id' should not be null");
        return new TutorID(anId);
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
        final TutorID tutorID = (TutorID) other;
        return Objects.equals(value, tutorID.value);
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
