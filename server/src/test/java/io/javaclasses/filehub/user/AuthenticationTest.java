package io.javaclasses.filehub.user;

import io.javaclasses.filehub.AbstractFileHubTest;
import io.javaclasses.filehub.file.FolderId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static io.javaclasses.filehub.given.TestEnvironment.LOGIN;
import static io.javaclasses.filehub.given.TestEnvironment.PASSWORD;
import static io.javaclasses.filehub.given.TestEnvironment.SHORT_LOGIN;
import static io.javaclasses.filehub.given.TestEnvironment.SHORT_PASSWORD;
import static io.javaclasses.filehub.given.TestEnvironment.avatar;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("`Authentication` should")
class AuthenticationTest extends AbstractFileHubTest {

    private Authentication authentication;

    @BeforeEach
    void setUp() {
        authentication = new Authentication(userStorage(), tokenStorage());
    }

    @Test
    @DisplayName("successfully authenticate user")
    void authenticateUserSuccessfully() {
        FolderId userRootFolder = FolderId.of(LOGIN);
        UserId userId = new UserId(LOGIN);

        UserRecord userRecord = new UserRecord(userId, PASSWORD, avatar(), userRootFolder);

        userStorage().write(userRecord);
        LogIn loginUser = new LogIn(LOGIN, PASSWORD);
        Token token = authentication.handle(loginUser);

        assertThat(token).isNotNull();

        Optional<TokenRecord> tokenRecord = tokenStorage().read(token);

        assertThat(tokenRecord).isPresent();
        assertEquals(userRecord.id()
                               .value(), tokenRecord.get()
                                                    .userId()
                                                    .value());
    }

    @Test
    @DisplayName("fail if user does not exist")
    void failToAuthenticateNonRegisteredUser() {
        UserId userId = new UserId(LOGIN);
        FolderId userRootFolder = FolderId.of(LOGIN);

        UserRecord userRecord = new UserRecord(userId, PASSWORD, avatar(), userRootFolder);
        userStorage().write(userRecord);
        LogIn loginUser = new LogIn(SHORT_LOGIN, PASSWORD);

        AuthenticationException exception = assertThrows(AuthenticationException.class,
                                                         () -> authentication.handle(loginUser));
        assertThat(exception.login()).isEqualTo(SHORT_LOGIN);
    }

    @Test
    @DisplayName("fail if password is incorrect")
    void failToAuthenticateWithIncorrectPassword() {
        UserId userId = new UserId(LOGIN);
        FolderId userRootFolder = FolderId.of(LOGIN);

        UserRecord userRecord = new UserRecord(userId, PASSWORD, avatar(), userRootFolder);
        userStorage().write(userRecord);
        LogIn loginUser = new LogIn(LOGIN, SHORT_PASSWORD);

        AuthenticationException exception = assertThrows(AuthenticationException.class,
                                                         () -> authentication.handle(loginUser));
        assertThat(exception.login()).isEqualTo(LOGIN);
    }
}
