package io.javaclasses.filehub.user;

import io.javaclasses.filehub.security.AuthenticatedUserProcess;
import io.javaclasses.filehub.storage.TokenStorage;
import io.javaclasses.filehub.storage.UserStorage;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A process of modifying a user avatar.
 */
public final class AvatarModification extends AuthenticatedUserProcess<ChangeAvatar, AvatarChanged> {

    private final UserStorage userStorage;

    /**
     * Creates an instance of {@code AvatarModification} process.
     *
     * @param tokenStorage
     *         a storage of token records
     * @param userStorage
     *         a storage of user records
     */
    public AvatarModification(TokenStorage tokenStorage, UserStorage userStorage) {
        super(tokenStorage);
        this.userStorage = checkNotNull(userStorage);
    }

    /**
     * Handles the command of the user avatar modification.
     *
     * @param command
     *         a command to change user avatar
     * @return the {@code AvatarChanged} if the user avatar modification process is completed
     *         successfully
     * @throws AvatarModificationException
     *         if there is no user with such user ID
     */
    @Override
    protected AvatarChanged doHandle(ChangeAvatar command, UserId userId)
            throws AvatarModificationException {
        checkNotNull(command);
        checkNotNull(userId);

        Optional<UserRecord> record = userStorage.read(userId);
        if (record.isPresent()) {
            UserRecord userRecord = record.get();
            UserRecord updatedUser = userRecord.updateAvatar(command.avatar());
            userStorage.write(updatedUser);
            return new AvatarChanged(userId);
        }
        throw new AvatarModificationException(userId);
    }
}
