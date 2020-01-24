package io.javaclasses.filehub.user;

/**
 * This exception signalizes a failed registration.
 *
 * <p>For example:
 *
 * <ol>
 * <li>the {@link RegisterUser} contains invalid login;
 * <li>the {@link RegisterUser} contains invalid password;
 * <li>the user already exists.
 * </ol>
 *
 * @see Registration#doHandle(RegisterUser) validation rules for registration
 */
public class RegistrationException extends RuntimeException {

    private static final long serialVersionUID = 0L;
    private static final String EXCEPTION_MESSAGE =
            "The registration of the user with login `%s` is failed. %s";

    private final String login;

    /**
     * Creates an instance of {@code RegistrationException}.
     *
     * @param login
     *         a user login
     * @param details
     *         additional details to describe the problem properly
     */
    public RegistrationException(String login, String details) {
        super(String.format(EXCEPTION_MESSAGE, login, details));
        this.login = login;
    }

    /**
     * Obtains the login of a user who attempted to register.
     */
    public String login() {
        return login;
    }
}
