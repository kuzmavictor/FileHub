package io.javaclasses.filehub;

import io.javaclasses.filehub.user.Token;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An abstract base for user queries to obtain the data stored in the FileHub application.
 */
public abstract class Query {

    private final Token token;

    /**
     * Initializes a {@code Query} instance.
     *
     * @param token
     *         a token of the user
     */
    protected Query(Token token) {
        this.token = checkNotNull(token);
    }

    /**
     * Obtains the security token of a user who attempts to send a query.
     */
    protected Token token() {
        return token;
    }
}
