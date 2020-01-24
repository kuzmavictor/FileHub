package io.javaclasses.filehub.user;

import io.javaclasses.filehub.AbstractFileHubTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth8.assertThat;
import static io.javaclasses.filehub.given.TestEnvironment.updatedAvatar;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("`AvatarModification` should")
class AvatarModificationTest extends AbstractFileHubTest {

    private AvatarModification avatarModification;

    @BeforeEach
    void setUp() {
        avatarModification = new AvatarModification(tokenStorage(), userStorage());
    }

    @Test
    @DisplayName("successfully update an avatar for authenticated user")
    void updateAvatarForAuthenticatedUser() {
        UserId registeredUserId = registerUser();
        assertThat(registeredUserId).isNotNull();

        Token token = authenticateUser();
        assertThat(token).isNotNull();

        ChangeAvatar changeAvatar = new ChangeAvatar(token, updatedAvatar());

        AvatarChanged avatarChanged = avatarModification.handle(changeAvatar);
        assertThat(avatarChanged).isNotNull();

        Optional<UserRecord> userRecord = userStorage().read(avatarChanged.userId());
        assertThat(userRecord).isPresent();

        userRecord.ifPresent(record -> {
            byte[] expected = updatedAvatar().contents();
            byte[] actual = record.avatar()
                                  .contents();
            assertThat(actual).isEqualTo(expected);
        });
    }

    @Test
    @DisplayName("fail if a user who tries to change her avatar does not exist in the storage")
    void failToChangeAvatarForNonexistentUser() {
        UserId registeredUserId = registerUser();
        assertThat(registeredUserId).isNotNull();

        Token token = authenticateUser();
        assertThat(token).isNotNull();

        ChangeAvatar changeAvatar = new ChangeAvatar(token, updatedAvatar());
        userStorage().clear();
        AvatarModificationException exception = assertThrows(AvatarModificationException.class,
                                                             () -> avatarModification.handle(
                                                                     changeAvatar));
        Optional<UserId> userId = userIdByToken(token);

        assertThat(userId).isPresent();
        userId.ifPresent(recordedUserId ->
                                 assertThat(exception.userId()
                                                     .value()).isEqualTo(recordedUserId.value()));
    }

    /**
     * Obtains a {@code UserId} by the token.
     */
    private Optional<UserId> userIdByToken(Token token) {
        Optional<TokenRecord> tokenRecord = tokenStorage().read(token);
        return tokenRecord.map(TokenRecord::userId);
    }
}
