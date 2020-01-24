package io.javaclasses.filehub;

import io.javaclasses.filehub.file.QueryExecutionForbiddenException;
import io.javaclasses.filehub.storage.TokenStorage;
import io.javaclasses.filehub.user.TokenExpiredException;
import io.javaclasses.filehub.user.TokenRecord;
import io.javaclasses.filehub.user.UserId;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An abstract base for data obtaining processes from the FileHub application.
 *
 * @param <Q>
 *         a type of user query to be executed
 * @param <R>
 *         a type of query execution result
 */
public abstract class View<Q extends Query, R> {

    private final TokenStorage tokenStorage;

    /**
     * Creates an instance of the {@code View}.
     *
     * @param tokenStorage
     *         storage of token records
     */
    protected View(TokenStorage tokenStorage) {
        this.tokenStorage = checkNotNull(tokenStorage);
    }

    /**
     * Initializes the process of query execution.
     *
     * @param query
     *         a query to be executed
     * @return a query execution result
     * @throws QueryExecutionForbiddenException
     *         if the user is not authenticated to send the query
     */
    public R execute(Q query) throws QueryExecutionForbiddenException {
        checkNotNull(query);

        TokenRecord tokenRecord = tokenStorage
                .read(query.token())
                .orElseThrow(() -> {
                    String message =
                            String.format("User must be authenticated to execute the query `%s`",
                                          query.getClass());
                    return new QueryExecutionForbiddenException(message);
                });
        checkToken(tokenRecord);

        return execute(query, tokenRecord.userId());
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
     * Executes a user query.
     *
     * @param query
     *         a query to be executed
     * @param userId
     *         an identifier of a user who sends the query
     * @return the query execution result
     */
    protected abstract R execute(Q query, UserId userId);
}
