package io.javaclasses.filehub.user;

import com.google.errorprone.annotations.Immutable;
import io.javaclasses.filehub.storage.RecordId;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A unique user identifier.
 */
@Immutable
public final class UserId implements RecordId {

    private final String login;

    /**
     * Creates a {@code UserId} instance.
     *
     * @param login
     *         a user login
     */
    public UserId(String login) {
        this.login = checkNotNull(login);
    }

    /**
     * Returns a user identifier.
     */
    public String value() {
        return login;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(login);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserId userId = (UserId) o;
        return Objects.equals(login, userId.login);
    }
}
