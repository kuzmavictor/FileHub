package io.javaclasses.filehub.user;

/**
 * This exception is thrown if the authentication process is failed.
 *
 * <p> For example:
 * <ol>
 * <li> the {@link LogIn} command contains an incorrect password;
 * <li> user does not exist.
 * </ol>
 */
public final class AuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 0L;
    private static final String EXCEPTION_MESSAGE = "The user with login `%s` failed to authenticate. %s";

    private final String login;

    /**
     * Initializes an exception.
     *
     * @param login
     *         a user login
     * @param details
     *         additional details to describe the problem properly
     */
    public AuthenticationException(String login, String details) {
        super(String.format(EXCEPTION_MESSAGE, login, details));
        this.login = login;
    }

    /**
     * Obtains the login of a user who attempted to authenticate.
     */
    public String login() {
        return login;
    }
}
