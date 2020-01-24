package io.javaclasses.filehub;

import io.javaclasses.filehub.storage.TokenStorage;
import io.javaclasses.filehub.user.Token;
import io.javaclasses.filehub.user.TokenExpiredException;
import io.javaclasses.filehub.user.TokenRecord;
import io.javaclasses.filehub.user.UserId;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An abstract base for processes which can be executed by user and
 * modify the data stored in the FileHub application.
 *
 * @param <C>
 *         a type of user command to be handled by the current process
 * @param <R>
 *         a type of process execution result
 */
public abstract class FileHubProcess<C extends Command, R> {

    private final TokenStorage tokenStorage;

    protected FileHubProcess(TokenStorage storage) {
        tokenStorage = checkNotNull(storage);
    }

    /**
     * Checks if a command can be handled by current process.
     *
     * @param command
     *         a user command
     */
    protected abstract void checkCanHandle(Command command);

    /**
     * Checks if the user has permission to send a command which will be handled by this process.
     *
     * @param command
     *         a command, which will be handled if the user has enough permissions
     * @return the process execution result
     */
    public R handle(C command) {
        checkNotNull(command);
        checkCanHandle(command);
        Token token = command.token();
        if (token == null) {
            return doHandle(command);
        }
        Optional<TokenRecord> record = tokenStorage.read(token);
        checkArgument(record.isPresent());

        checkToken(record.get());

        TokenRecord tokenRecord = record.get();
        UserId userId = tokenRecord.userId();
        return doHandle(command, userId);
    }

    /**
     * Checks if the user token is expired.
     *
     * @param record
     *         the record to be checked
     */
    private void checkToken(TokenRecord record) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime tokenExpirationTime = record.whenExpires();

        if (tokenExpirationTime.isBefore(now)) {
            tokenStorage.remove(record.id());
            throw new TokenExpiredException(record.id());
        }
    }

    /**
     * Handles the command which is sent by an anonymous user.
     *
     * @param command
     *         a command to be handled
     * @return the process execution result
     */
    protected abstract R doHandle(C command);

    /**
     * Handles the command which is sent by an authenticated user.
     *
     * @param command
     *         a command to be handled
     * @param userId
     *         an identifier of an authenticated user
     * @return the process execution result
     */
    protected abstract R doHandle(C command, UserId userId);
}
