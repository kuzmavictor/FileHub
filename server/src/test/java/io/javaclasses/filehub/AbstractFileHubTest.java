package io.javaclasses.filehub;

import io.javaclasses.filehub.storage.FileMetadataStorage;
import io.javaclasses.filehub.storage.FolderMetadataStorage;
import io.javaclasses.filehub.storage.TokenStorage;
import io.javaclasses.filehub.storage.UserStorage;
import io.javaclasses.filehub.user.Authentication;
import io.javaclasses.filehub.user.LogIn;
import io.javaclasses.filehub.user.RegisterUser;
import io.javaclasses.filehub.user.Registration;
import io.javaclasses.filehub.user.Token;
import io.javaclasses.filehub.user.UserId;
import org.junit.jupiter.api.AfterEach;

import static io.javaclasses.filehub.given.TestEnvironment.LOGIN;
import static io.javaclasses.filehub.given.TestEnvironment.PASSWORD;
import static io.javaclasses.filehub.given.TestEnvironment.avatar;

/**
 * An abstract base for FileHub tests.
 *
 * <p>Sets up an environment for tests.
 */
public abstract class AbstractFileHubTest {

    private final ServerEnvironment serverEnvironment = ServerEnvironment.instance();

    protected final UserStorage userStorage() {
        return serverEnvironment.userStorage();
    }

    protected final TokenStorage tokenStorage() {
        return serverEnvironment.tokenStorage();
    }

    protected final FileMetadataStorage fileMetaDataStorage() {
        return serverEnvironment.fileMetadataStorage();
    }

    protected final FolderMetadataStorage folderMetadataStorage() {
        return serverEnvironment.folderMetadataStorage();
    }

    @AfterEach
    void tearDown() {
        userStorage().clear();
        tokenStorage().clear();
        fileMetaDataStorage().clear();
        folderMetadataStorage().clear();
    }

    /**
     * Registers a new user.
     */
    protected final UserId registerUser() {
        RegisterUser registerUser = new RegisterUser(LOGIN, PASSWORD, avatar());
        Registration registration = new Registration(tokenStorage(), userStorage(),
                                                     folderMetadataStorage());
        return registration.handle(registerUser);
    }

    /**
     * Authenticates a user and obtains her token.
     */
    protected final Token authenticateUser() {
        Authentication authentication = new Authentication(userStorage(), tokenStorage());
        LogIn logIn = new LogIn(LOGIN, PASSWORD);
        return authentication.handle(logIn);
    }
}
