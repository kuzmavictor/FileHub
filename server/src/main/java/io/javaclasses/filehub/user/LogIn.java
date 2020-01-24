package io.javaclasses.filehub.user;

import com.google.errorprone.annotations.Immutable;
import io.javaclasses.filehub.Command;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A log in command for the user authentication process.
 */
@Immutable
public final class LogIn implements Command {

    private final String login;
    private final String password;

    /**
     * Creates a user {@code LogIn} command.
     *
     * @param login
     *         a user login
     * @param password
     *         a user password
     */
    public LogIn(String login, String password) {
        this.login = checkNotNull(login);
        this.password = checkNotNull(password);
    }

    /**
     * Returns a string, containing user login.
     */
    public String login() {
        return login;
    }

    /**
     * Returns a string, containing user password.
     */
    public String password() {
        return password;
    }

    /**
     * Always returns {@code null}-token.
     */
    @Override
    public Token token() {
        return null;
    }
}
