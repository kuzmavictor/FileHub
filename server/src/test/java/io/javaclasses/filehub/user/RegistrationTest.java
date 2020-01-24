package io.javaclasses.filehub.user;

import io.javaclasses.filehub.AbstractFileHubTest;
import io.javaclasses.filehub.file.FolderId;
import io.javaclasses.filehub.file.FolderMetadataRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.IntStream;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static io.javaclasses.filehub.given.TestEnvironment.LOGIN;
import static io.javaclasses.filehub.given.TestEnvironment.PASSWORD;
import static io.javaclasses.filehub.given.TestEnvironment.SHORT_LOGIN;
import static io.javaclasses.filehub.given.TestEnvironment.SHORT_PASSWORD;
import static io.javaclasses.filehub.given.TestEnvironment.avatar;
import static io.javaclasses.filehub.given.TestEnvironment.longPassword;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("`Registration` should")
class RegistrationTest extends AbstractFileHubTest {

    private Registration registration;

    @BeforeEach
    void setUp() {
        registration = new Registration(tokenStorage(), userStorage(), folderMetadataStorage());
    }

    @Test
    @DisplayName("successfully register users")
    void registerMultipleUsers() {
        IntStream.range(1, 10)
                 .forEach((i) -> {
                     String login = LOGIN + i;
                     String password = PASSWORD + i;

                     RegisterUser registerUser = new RegisterUser(login, password, avatar());
                     UserId userId = registration.handle(registerUser);
                     Optional<UserRecord> user = userStorage().read(userId);

                     assertThat(user).isPresent();
                     user.ifPresent(record -> assertTrue(record.passwordMatches(password)));

                     FolderId folderId = FolderId.createRootFolderId(login);
                     Optional<FolderMetadataRecord> record = folderMetadataStorage().read(folderId);
                     assertThat(record).isPresent();
                 });
    }

    @Test
    @DisplayName("trim leading and trailing spaces from the login and password during the registration")
    void registerUserWithTrimmedLoginAndPasswordSpaces() {
        String space = " ";
        String login = space + LOGIN + space;
        String password = space + PASSWORD + space;

        RegisterUser registerUser = new RegisterUser(login, password, avatar());
        UserId userId = registration.handle(registerUser);
        Optional<UserRecord> user = userStorage().read(userId);

        assertThat(user).isPresent();

        user.ifPresent(record -> assertTrue(record.passwordMatches(password.trim())));

        FolderId folderId = FolderId.createRootFolderId(login.trim());
        Optional<FolderMetadataRecord> record = folderMetadataStorage().read(folderId);
        assertThat(record).isPresent();
    }

    @Test
    @DisplayName("fail if such user exists")
    void failToRegisterAlreadyExistingUser() {
        UserId userId = new UserId(LOGIN);
        FolderId userRootFolder = FolderId.of(LOGIN);

        UserRecord userRecord = new UserRecord(userId, PASSWORD, avatar(), userRootFolder);
        userStorage().write(userRecord);
        RegisterUser registerUser = new RegisterUser(LOGIN, PASSWORD, avatar());

        RegistrationException exception = assertThrows(RegistrationException.class,
                                                       () -> registration.handle(registerUser));
        assertThat(exception.login()).isEqualTo(LOGIN);
    }

    @Test
    @DisplayName("fail if user login consists of less than 6 characters")
    void failToRegisterUserWithShortLogin() {
        RegisterUser registerUser = new RegisterUser(SHORT_LOGIN, PASSWORD, avatar());

        RegistrationException exception = assertThrows(RegistrationException.class,
                                                       () -> registration.handle(registerUser));
        assertThat(exception.login()).isEqualTo(SHORT_LOGIN);
    }

    @Test
    @DisplayName("fail if user login consists of more than 255 characters")
    void failToRegisterUserWithLongLogin() {
        String incorrectLogin = longPassword();
        RegisterUser registerUser = new RegisterUser(incorrectLogin, PASSWORD, avatar());

        RegistrationException exception = assertThrows(RegistrationException.class,
                                                       () -> registration.handle(registerUser));
        assertThat(exception.login()).isEqualTo(incorrectLogin);
    }

    @Test
    @DisplayName("fail if user password consists of less than 6 characters")
    void failToRegisterUserWithShortPassword() {
        RegisterUser registerUser = new RegisterUser(LOGIN, SHORT_PASSWORD, avatar());

        RegistrationException exception = assertThrows(RegistrationException.class,
                                                       () -> registration.handle(registerUser));
        assertThat(exception.login()).isEqualTo(LOGIN);
    }

    @Test
    @DisplayName("fail if user password consists of more than 255 characters")
    void failToRegisterUserWithLongPassword() {
        String incorrectPassword = longPassword();
        RegisterUser registerUser = new RegisterUser(LOGIN, incorrectPassword, avatar());

        RegistrationException exception = assertThrows(RegistrationException.class,
                                                       () -> registration.handle(registerUser));
        assertThat(exception.login()).isEqualTo(LOGIN);
    }
}
