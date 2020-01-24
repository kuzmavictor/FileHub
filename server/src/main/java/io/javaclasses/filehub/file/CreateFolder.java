package io.javaclasses.filehub.file;

import com.google.errorprone.annotations.Immutable;
import io.javaclasses.filehub.Command;
import io.javaclasses.filehub.user.Token;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A command to create a new folder.
 */
@Immutable
public final class CreateFolder implements Command {

    private final Token token;
    private final String name;
    private final FolderId parentFolderId;

    /**
     * Initializes a {@code CreateFolder} command.
     *
     * @param token
     *         a token of an authenticated user
     * @param name
     *         a name of a new folder
     * @param parentFolderId
     *         a location of the new folder
     */
    public CreateFolder(Token token, String name, FolderId parentFolderId) {
        this.token = checkNotNull(token);
        this.name = checkNotNull(name);
        this.parentFolderId = checkNotNull(parentFolderId);
    }

    /**
     * Obtains the new folder name.
     */
    public String name() {
        return name;
    }

    /**
     * Obtains the location of the new folder.
     */
    public FolderId parentFolderId() {
        return parentFolderId;
    }

    @Override
    public Token token() {
        return token;
    }
}
