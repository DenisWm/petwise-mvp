package com.petwise.domain.pet;

import com.petwise.domain.Identifier;
import com.petwise.domain.utils.IDUtils;
import java.util.Objects;

/** Unique identifier for Pet entity. */
@SuppressWarnings({
    "PMD.ClassWithOnlyPrivateConstructorsShouldBeFinal",
    "PMD.OnlyOneReturn",
    "PMD.ShortVariable",
    "PMD.ControlStatementBraces",
    "PMD.MethodArgumentCouldBeFinal",
    "PMD.LocalVariableCouldBeFinal"
})
public final class PetID extends Identifier<String> {

    private final String value;

    private PetID(final String aValue) {
        super();
        this.value = aValue;
    }

    public static PetID unique() {
        return new PetID(IDUtils.uuid());
    }

    @SuppressWarnings("PMD.ShortVariable")
    public static PetID from(final String anId) {
        Objects.requireNonNull(anId, "'id' should not be null");
        return new PetID(anId);
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
        final PetID petID = (PetID) other;
        return Objects.equals(value, petID.value);
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
