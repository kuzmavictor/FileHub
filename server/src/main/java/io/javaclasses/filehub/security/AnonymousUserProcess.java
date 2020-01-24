package io.javaclasses.filehub.security;

import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import io.javaclasses.filehub.Command;
import io.javaclasses.filehub.FileHubProcess;
import io.javaclasses.filehub.storage.TokenStorage;
import io.javaclasses.filehub.user.Token;
import io.javaclasses.filehub.user.UserId;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * A base for processes which can be executed only by an anonymous user.
 *
 * @param <C>
 *         a type of user command
 * @param <R>
 *         a type of process execution result
 */
public abstract class AnonymousUserProcess<C extends Command, R> extends FileHubProcess<C, R> {

    private static final String EXCEPTION_MESSAGE =
            "The process `%s` cannot be executed by an authenticated user `%s`.";

    protected AnonymousUserProcess(TokenStorage storage) {
        super(storage);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Allows the current process execution only if a user token does not exist.
     */
    @OverridingMethodsMustInvokeSuper
    @Override
    public void checkCanHandle(Command command) {
        @Nullable Token token = command.token();
        if (token != null) {
            throw new ProcessExecutionNotAllowedException(token, this.getClass());
        }
    }

    /**
     * Throws {@code IllegalStateException}
     * if an authenticated user tries to execute an anonymous process.
     */
    @Override
    protected final R doHandle(C command, UserId userId) {
        String message = String.format(EXCEPTION_MESSAGE, this.getClass(), userId);
        throw new IllegalStateException(message);
    }
}
