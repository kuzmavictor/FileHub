package io.javaclasses.filehub.security;

import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import io.javaclasses.filehub.Command;
import io.javaclasses.filehub.FileHubProcess;
import io.javaclasses.filehub.storage.TokenStorage;
import io.javaclasses.filehub.user.Token;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A base for processes which can be executed only by an authenticated user.
 *
 * @param <C>
 *         a type of the user command
 * @param <R>
 *         a type of the process execution result
 */
public abstract class AuthenticatedUserProcess<C extends Command, R> extends FileHubProcess<C, R> {

    private static final String EXCEPTION_MESSAGE =
            "The process `%s` cannot be executed by an anonymous user.";

    protected AuthenticatedUserProcess(TokenStorage storage) {
        super(storage);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Allows the current process execution only if the user is authenticated.
     */
    @OverridingMethodsMustInvokeSuper
    @Override
    protected final void checkCanHandle(Command command) {
        @Nullable Token token = command.token();
        if (token == null) {
            throw new ProcessExecutionNotAllowedException(this.getClass());
        }
    }

    /**
     * Throws {@code IllegalStateException}
     * if an anonymous user tries to execute a non-anonymous process.
     */
    @Override
    protected final R doHandle(C command) {
        String message = String.format(EXCEPTION_MESSAGE, this.getClass());
        throw new IllegalStateException(message);
    }
}
