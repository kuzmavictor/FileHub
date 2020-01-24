package io.javaclasses.filehub.user;

import io.javaclasses.filehub.AbstractFileHubTest;
import io.javaclasses.filehub.Command;
import io.javaclasses.filehub.security.AnonymousUserProcess;
import io.javaclasses.filehub.security.AuthenticatedUserProcess;
import io.javaclasses.filehub.security.ProcessExecutionNotAllowedException;
import io.javaclasses.filehub.storage.TokenStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;
import static io.javaclasses.filehub.given.TestEnvironment.LOGIN;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("`Process security` should")
class FileHubProcessSecurityTest extends AbstractFileHubTest {

    private AnonymousOnlyUserProcess anonymousProcess;
    private NonAnonymousUserProcess nonAnonymousProcess;

    @BeforeEach
    void setUp() {
        anonymousProcess = new AnonymousOnlyUserProcess(tokenStorage());
        nonAnonymousProcess = new NonAnonymousUserProcess(tokenStorage());
    }

    @Test
    @DisplayName("fail if an anonymous process handles command with non-null token")
    void failCheckCanAnonymousProcessHandleCommandWithNonNullToken() {
        Command commandWithToken = new CommandWithToken(Token.generate());

        assertThrows(ProcessExecutionNotAllowedException.class,
                     () -> anonymousProcess.handle(commandWithToken));
    }

    @Test
    @DisplayName("fail if non-anonymous process handles command with null token")
    void failCheckCanNonAnonymousProcessHandleCommandWithNullToken() {
        Command commandWithoutToken = new CommandWithoutToken();

        assertThrows(ProcessExecutionNotAllowedException.class,
                     () -> nonAnonymousProcess.handle(commandWithoutToken));
    }

    @Test
    @DisplayName("pass successfully if an anonymous process handles command with null token")
    void passCheckCanAnonymousProcessHandleCommandWithoutToken() {
        Command commandWithoutToken = new CommandWithoutToken();
        Token anonymousToken = anonymousProcess.handle(commandWithoutToken);

        assertThat(anonymousToken).isNotNull();
    }

    @Test
    @DisplayName("pass successfully if non-anonymous process handles command with non-null token")
    void passCheckCanNonAnonymousProcessHandleCommandWithToken() {
        Command commandWithToken = new CommandWithToken(Token.generate());
        UserId userId = new UserId(LOGIN);
        TokenRecord tokenRecord = new TokenRecord(commandWithToken.token(), userId);

        tokenStorage().write(tokenRecord);

        Token notAnonymousToken = nonAnonymousProcess.handle(commandWithToken);

        assertThat(notAnonymousToken).isNotNull();
    }

    /**
     * A command with non-null token.
     */
    private static final class CommandWithToken implements Command {

        private final Token token;

        private CommandWithToken(Token token) {
            this.token = token;
        }

        @Override
        public Token token() {
            return token;
        }
    }

    /**
     * A command with null token.
     */
    private static final class CommandWithoutToken implements Command {

        @Override
        public Token token() {
            return null;
        }
    }

    /**
     * An anonymous only process.
     */
    private static final class AnonymousOnlyUserProcess extends AnonymousUserProcess<Command, Token> {

        private AnonymousOnlyUserProcess(TokenStorage storage) {
            super(storage);
        }

        @Override
        public Token doHandle(Command command) {
            return Token.generate();
        }
    }

    /**
     * A non-anonymous process.
     */
    private static final class NonAnonymousUserProcess extends AuthenticatedUserProcess<Command, Token> {

        private NonAnonymousUserProcess(TokenStorage storage) {
            super(storage);
        }

        @Override
        protected Token doHandle(Command command, UserId userId) {
            return Token.generate();
        }
    }
}
