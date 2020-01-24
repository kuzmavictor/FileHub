package io.javaclasses.filehub.file;

import com.google.errorprone.annotations.Immutable;
import io.javaclasses.filehub.Command;
import io.javaclasses.filehub.user.Token;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A command to remove a user folder.
 */
@Immutable
public final class RemoveFolder implements Command {

    private final Token token;
    private final FolderId folderId;

    /**
     * Initializes a {@code RemoveFolder} command.
     *
     * @param token
     *         a token of an authenticated user
     * @param folderId
     *         an identifier of the folder to be removed
     */
    public RemoveFolder(Token token, FolderId folderId) {
        this.token = checkNotNull(token);
        this.folderId = checkNotNull(folderId);
    }

    @Override
    public Token token() {
        return token;
    }

    /**
     * Obtains the identifier of the folder to be removed.
     */
    public FolderId folderId() {
        return folderId;
    }
}
