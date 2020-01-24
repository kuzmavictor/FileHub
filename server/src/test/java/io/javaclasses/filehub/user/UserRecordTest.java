package io.javaclasses.filehub.user;

import io.javaclasses.filehub.file.FolderId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.javaclasses.filehub.given.TestEnvironment.LOGIN;
import static io.javaclasses.filehub.given.TestEnvironment.PASSWORD;
import static io.javaclasses.filehub.given.TestEnvironment.SHORT_PASSWORD;
import static io.javaclasses.filehub.given.TestEnvironment.avatar;

@DisplayName("`UserRecord` should")
class UserRecordTest {

    private UserRecord userRecord;

    @BeforeEach
    void setUp() {
        UserId userId = new UserId(LOGIN);
        FolderId userRootFolder = FolderId.of(LOGIN);
        userRecord = new UserRecord(userId, PASSWORD, avatar(), userRootFolder);
    }

    @Test
    @DisplayName("confirm the equality of record password to the entered password")
    void confirmPasswordMatches() {
        assertThat(userRecord.passwordMatches(PASSWORD)).isTrue();
    }

    @Test
    @DisplayName("not confirm the equality of record password to the entered incorrect password")
    void notConfirmPasswordMatches() {
        assertThat(userRecord.passwordMatches(SHORT_PASSWORD)).isFalse();
    }
}
