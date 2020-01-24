package io.javaclasses.filehub.user;

/**
 * This exception is thrown when the user with the expired token tries to send a command.
 */
public class TokenExpiredException extends RuntimeException {

    private static final long serialVersionUID = 0L;

    /**
     * Initializes an exception instance.
     *
     * @param token
     *         a token of the user
     */
    public TokenExpiredException(Token token) {
        super(String.format("The token '%s' is expired.", token));
    }
}
