package io.javaclasses.filehub.user;

import com.google.errorprone.annotations.Immutable;

/**
 * The result of the user's avatar modification.
 */
@Immutable
public final class AvatarChanged {

    private final UserId userId;

    /**
     * Creates an {@code AvatarChanged} instance.
     *
     * @param id
     *         an identifier of the user, who changed the avatar
     */
    public AvatarChanged(UserId id) {
        userId = id;
    }

    /**
     * Obtains the user identifier.
     */
    public UserId userId() {
        return userId;
    }
}
