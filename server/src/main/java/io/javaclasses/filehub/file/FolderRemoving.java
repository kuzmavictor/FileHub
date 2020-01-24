package io.javaclasses.filehub.file;

import io.javaclasses.filehub.security.AuthenticatedUserProcess;
import io.javaclasses.filehub.storage.FolderMetadataStorage;
import io.javaclasses.filehub.storage.TokenStorage;
import io.javaclasses.filehub.user.UserId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A process of folder removing.
 */
public final class FolderRemoving extends AuthenticatedUserProcess<RemoveFolder, FolderId> {

    private final FolderMetadataStorage folderMetadataStorage;

    /**
     * Creates an instance of {@code FolderRemoving} process.
     *
     * @param tokenStorage
     *         storage of token records
     * @param folderMetadataStorage
     *         storage of folders metadata
     */
    public FolderRemoving(TokenStorage tokenStorage, FolderMetadataStorage folderMetadataStorage) {
        super(tokenStorage);
        this.folderMetadataStorage = checkNotNull(folderMetadataStorage);
    }

    @Override
    protected FolderId doHandle(RemoveFolder removeFolder, UserId userId) {
        FolderId removableFolder = removeFolder.folderId();

        folderMetadataStorage.remove(removableFolder);
        return removableFolder;
    }
}
