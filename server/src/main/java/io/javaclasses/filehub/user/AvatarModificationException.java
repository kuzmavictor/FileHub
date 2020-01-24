package io.javaclasses.filehub.user;

/**
 * This exception is thrown if the user failed to modify her avatar.
 *
 * <p>For example, the user who attempted to modify her avatar does not exist.
 */
public class AvatarModificationException extends RuntimeException {

    private static final long serialVersionUID = 8292592490941081413L;
    private static final String MESSAGE = "The user with login `%s` cannot modify the avatar.";

    private final UserId userId;

    /**
     * Initializes an exception.
     *
     * @param userId
     *         a identifier of a user who attempted to modify the avatar
     */
    public AvatarModificationException(UserId userId) {
        super(String.format(MESSAGE, userId.value()));
        this.userId = userId;
    }

    /**
     * Obtains the identifier of a user who attempted to modify the avatar.
     */
    public UserId userId() {
        return userId;
    }
}
