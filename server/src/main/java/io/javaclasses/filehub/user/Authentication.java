package io.javaclasses.filehub.user;

import io.javaclasses.filehub.security.AnonymousUserProcess;
import io.javaclasses.filehub.storage.TokenStorage;
import io.javaclasses.filehub.storage.UserStorage;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A process of authenticating users in FileHub application.
 */
public final class Authentication extends AnonymousUserProcess<LogIn, Token> {

    private final UserStorage userStorage;
    private final TokenStorage tokenStorage;

    /**
     * Creates an instance of {@code Authentication} process.
     *
     * @param userStorage
     *         a storage of user records
     * @param tokenStorage
     *         a storage of token records
     */
    public Authentication(UserStorage userStorage, TokenStorage tokenStorage) {
        super(tokenStorage);
        this.userStorage = checkNotNull(userStorage);
        this.tokenStorage = checkNotNull(tokenStorage);
    }

    /**
     * Handles user attempts to log in.
     *
     * @param command
     *         a command to log in
     * @return a {@code Token} generated for this user, if the authentication process is completed
     *         successfully
     * @throws AuthenticationException
     *         if the user does not exist or a user password is incorrect
     */
    @Override
    protected Token doHandle(LogIn command) throws AuthenticationException {
        checkNotNull(command);
        String userLogin = command.login();
        UserId userID = new UserId(userLogin);
        Optional<UserRecord> user = userStorage.read(userID);

        if (user.isPresent()) {
            boolean passwordMatches = user.get()
                                          .passwordMatches(command.password());

            if (passwordMatches) {
                Token token = Token.generate();
                TokenRecord tokenRecord = new TokenRecord(token, user.get()
                                                                     .id());
                tokenStorage.write(tokenRecord);
                return token;
            } else {
                throw new AuthenticationException(userLogin, "Invalid credentials.");
            }
        }
        throw new AuthenticationException(userLogin, "Such user does not exist.");
    }
}
