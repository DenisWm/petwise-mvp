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
    private final String value;

    private TutorID(final String aValue) {
        super();
        this.value = aValue;
    }

    public static TutorID unique() {
        return new TutorID(IDUtils.uuid());
    }

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
