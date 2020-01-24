package io.javaclasses.filehub.user;

import com.google.errorprone.annotations.Immutable;
import io.javaclasses.filehub.storage.RecordId;

import java.util.Objects;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A temporary identifier for FileHub user, which was authenticated.
 *
 * @implNote Token instances are always unique.
 */
@Immutable
public final class Token implements RecordId {

    private final String value;

    private Token(String value) {
        this.value = value;
    }

    /**
     * Generates a unique security hash which is contained within a {@code Token}.
     */
    public static Token generate() {
        return new Token(UUID.randomUUID()
                             .toString());
    }

    /**
     * Creates a {@code Token} instance from the existing value.
     */
    public static Token of(String value) {
        checkNotNull(value);
        return new Token(value);
    }

    /**
     * Returns a unique security hash value.
     */
    public String value() {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Token token = (Token) o;
        return Objects.equals(value, token.value);
    }

    @Override
    public String toString() {
        return "Token{" +
                "value='" + value + '\'' +
                '}';
    }
}
