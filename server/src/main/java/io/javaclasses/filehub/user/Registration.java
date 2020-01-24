package io.javaclasses.filehub.user;

import io.javaclasses.filehub.file.FolderId;
import io.javaclasses.filehub.file.FolderMetadataRecord;
import io.javaclasses.filehub.security.AnonymousUserProcess;
import io.javaclasses.filehub.storage.FolderMetadataStorage;
import io.javaclasses.filehub.storage.TokenStorage;
import io.javaclasses.filehub.storage.UserStorage;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A process of registering a new user in the FileHub application.
 */
public final class Registration extends AnonymousUserProcess<RegisterUser, UserId> {

    private final UserStorage userStorage;
    private final FolderMetadataStorage folderMetadataStorage;

    /**
     * Creates an instance of {@code Registration} process.
     *
     * @param userStorage
     *         storage of user records
     * @param folderMetadataStorage
     *         storage of folders metadata
     */
    public Registration(TokenStorage tokenStorage, UserStorage userStorage,
                        FolderMetadataStorage folderMetadataStorage) {
        super(tokenStorage);
        this.userStorage = checkNotNull(userStorage);
        this.folderMetadataStorage = checkNotNull(folderMetadataStorage);
    }

    /**
     * Handles registration process.
     *
     * @param command
     *         a command to register a user
     * @return the {@code UserId} if the registration process is completed successfully
     * @throws RegistrationException
     *         if there was a user with the same login registered in the system, or
     *         a user tries to pass invalid login or password values
     * @implNote The password and login in the command should be from 6 to 255 characters.
     *         All leading and trailing spaces in user password and login are removed.
     */
    @Override
    protected UserId doHandle(RegisterUser command) throws RegistrationException {
        checkNotNull(command);
        String login = command.login()
                              .trim();
        String password = command.password()
                                 .trim();

        if (login.length() < 6 || login.length() > 255) {
            throw new RegistrationException(login,
                                            "The login length should be from 6 to 255 characters.");
        }

        if (password.length() < 6 || password.length() > 255) {
            throw new RegistrationException(login,
                                            "The password length should be from 6 to 255 characters.");
        }

        UserId registeredUserId = new UserId(login);
        Optional<UserRecord> user = userStorage.read(registeredUserId);

        if (user.isPresent()) {
            throw new RegistrationException(login, "User already exists.");
        }

        Avatar userAvatar = command.avatar();
        FolderId userRootFolder = createUserFolder(login, registeredUserId);
        UserRecord userRecord = new UserRecord(registeredUserId, password, userAvatar,
                                               userRootFolder);
        userStorage.write(userRecord);

        return registeredUserId;
    }

    /**
     * Creates a new root folder for the registered user.
     *
     * <p>The user root folder name matches the user login.
     */
    private FolderId createUserFolder(String rootFolderName, UserId userId) {
        FolderId folderId = FolderId.createRootFolderId(rootFolderName);
        FolderMetadataRecord folderMetadataRecord =
                new FolderMetadataRecord(folderId, userId, rootFolderName);
        folderMetadataStorage.write(folderMetadataRecord);
        return folderId;
    }
}
