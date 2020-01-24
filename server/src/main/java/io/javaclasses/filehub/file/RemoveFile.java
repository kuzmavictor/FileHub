package io.javaclasses.filehub.file;

import com.google.errorprone.annotations.Immutable;
import io.javaclasses.filehub.Command;
import io.javaclasses.filehub.user.Token;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A command to remove a user file.
 */
@Immutable
public final class RemoveFile implements Command {

    private final Token userToken;
    private final FileId fileId;

    /**
     * Initializes a {@code RemoveFile} command.
     *
     * @param token
     *         a token of an authenticated user
     * @param id
     *         an identifier of the file to be removed
     */
    public RemoveFile(Token token, FileId id) {
        userToken = checkNotNull(token);
        fileId = checkNotNull(id);
    }

    @Override
    public Token token() {
        return userToken;
    }

    /**
     * Obtains the identifier of the file to be removed.
     */
    public FileId fileId() {
        return fileId;
    }
}
