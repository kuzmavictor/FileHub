package io.javaclasses.filehub.user;

import com.google.errorprone.annotations.Immutable;
import io.javaclasses.filehub.Command;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A command to change the user avatar.
 */
@Immutable
public final class ChangeAvatar implements Command {

    private final Token token;
    private final Avatar avatar;

    /**
     * Creates a user {@code ChangeAvatar} command.
     *
     * @param token
     *         a token of an authenticated user
     * @param avatar
     *         an updated avatar contents
     */
    public ChangeAvatar(Token token, Avatar avatar) {
        this.token = checkNotNull(token);
        this.avatar = checkNotNull(avatar);
    }

    /**
     * Returns the token of the user trying to modify her avatar.
     */
    @Override
    public Token token() {
        return token;
    }

    /**
     * Returns the contents of the new avatar.
     */
    public Avatar avatar() {
        return avatar;
    }
}
