package io.javaclasses.filehub.user;

import com.google.errorprone.annotations.Immutable;
import io.javaclasses.filehub.Command;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A command for the user registration process.
 */
@Immutable
public class RegisterUser implements Command {

    private final String login;
    private final String password;
    private final Avatar avatar;

    /**
     * Initializes a registration command.
     *
     * @param login
     *         a login for registration
     * @param password
     *         a user password
     * @param avatar
     *         a user avatar contents
     */
    public RegisterUser(String login, String password, Avatar avatar) {
        this.login = checkNotNull(login);
        this.password = checkNotNull(password);
        this.avatar = checkNotNull(avatar);
    }

    /**
     * Obtains string, containing user login.
     */
    public String login() {
        return login;
    }

    /**
     * Obtains string, containing user password.
     */
    public String password() {
        return password;
    }

    /**
     * Obtains an avatar contents of the registering user.
     */
    public Avatar avatar() {
        return avatar;
    }

    /**
     * Always returns {@code null}-token.
     */
    @Override
    public final Token token() {
        return null;
    }
}
