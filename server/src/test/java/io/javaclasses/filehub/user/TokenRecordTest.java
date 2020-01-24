package io.javaclasses.filehub.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static io.javaclasses.filehub.given.TestEnvironment.LOGIN;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("`TokenRecord` should")
class TokenRecordTest {

    private TokenRecord tokenRecord;

    @BeforeEach
    void setUp() {
        UserId userId = new UserId(LOGIN);
        Token token = Token.generate();
        tokenRecord = new TokenRecord(token, userId);
    }

    @Test
    @DisplayName("set the expiration date in a month from a moment of record creation")
    void whenExpires() {
        LocalDateTime today = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime tokenExpirationTime = tokenRecord.whenExpires();

        LocalDateTime secondAfterTokenExpirationDate = today.plus(1, ChronoUnit.MONTHS)
                                                            .plus(1, ChronoUnit.SECONDS);

        LocalDateTime secondBeforeTokenExpirationDate = today.plus(1, ChronoUnit.MONTHS)
                                                             .minus(1, ChronoUnit.SECONDS);
        assertTrue(tokenExpirationTime.isBefore(secondAfterTokenExpirationDate));
        assertTrue(tokenExpirationTime.isAfter(secondBeforeTokenExpirationDate));
    }
}
