package io.javaclasses.filehub.user;

import io.javaclasses.filehub.storage.Record;
import io.javaclasses.filehub.storage.Storage;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

/**
 * A token data designed to be written and read from a {@linkplain Storage storage}.
 *
 * <p>Whenever a token expires, it is deleted.
 */
public final class TokenRecord extends Record<Token> {

    private final UserId userId;
    private final LocalDateTime whenExpires = calculateExpirationDate();

    /**
     * Creates a new {@code TokenRecord} instance.
     *
     * @param token
     *         a temporary user identifier
     * @param userId
     *         a user identifier
     */
    public TokenRecord(Token token, UserId userId) {
        super(token);
        this.userId = userId;
    }

    /**
     * Calculates a date which defines token record lifetime.
     */
    private static LocalDateTime calculateExpirationDate() {
        ZoneId zoneId = ZoneOffset.UTC;
        LocalDateTime today = LocalDateTime.now(zoneId);
        LocalDateTime whenExpires = today.plus(1, ChronoUnit.MONTHS);
        return whenExpires;
    }

    /**
     * Returns a user identifier.
     */
    public UserId userId() {
        return userId;
    }

    /**
     * Returns a token expiration time point.
     */
    public LocalDateTime whenExpires() {
        return whenExpires;
    }
}
