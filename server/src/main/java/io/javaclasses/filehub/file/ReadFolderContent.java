package io.javaclasses.filehub.file;

import com.google.errorprone.annotations.Immutable;
import io.javaclasses.filehub.Query;
import io.javaclasses.filehub.user.Token;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A query to read the content of the required folder.
 */
@Immutable
public final class ReadFolderContent extends Query {

    private final String folderId;

    /**
     * Initializes a {@code ReadFolderContent} query.
     *
     * @param token
     *         a token of an authenticated user
     * @param folderId
     *         an identifier of the required folder
     */
    public ReadFolderContent(Token token, String folderId) {
        super(token);
        this.folderId = checkNotNull(folderId);
    }

    /**
     * Obtains an identifier of the folder.
     */
    public String folderId() {
        return folderId;
    }
}
