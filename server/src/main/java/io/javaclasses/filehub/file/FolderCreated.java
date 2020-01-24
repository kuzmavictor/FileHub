package io.javaclasses.filehub.file;

import com.google.errorprone.annotations.Immutable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The result of the user's folder creation.
 */
@Immutable
public final class FolderCreated {

    private final FolderId folderId;

    /**
     * Creates a {@code FolderCreated} instance.
     *
     * @param folderId
     *         an identifier of the folder
     */
    public FolderCreated(FolderId folderId) {
        this.folderId = checkNotNull(folderId);

    }

    /**
     * Obtains the identifier of the folder.
     */
    public FolderId folderId() {
        return folderId;
    }
}
