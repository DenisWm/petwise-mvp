package com.petwise.domain.tutor;

import com.petwise.domain.Identifier;
import com.petwise.domain.utils.IDUtils;
import java.util.Objects;

/** Unique identifier for Tutor aggregate. */
public class TutorID extends Identifier<String> {

    private final String value;

    private TutorID(final String value) {
        this.value = value;
    }

    public static TutorID unique() {
        return new TutorID(IDUtils.uuid());
    }

    public static TutorID from(final String anId) {
        Objects.requireNonNull(anId, "'id' should not be null");
        return new TutorID(anId);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TutorID tutorID = (TutorID) o;
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
