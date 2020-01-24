package io.javaclasses.filehub.file;

import io.javaclasses.filehub.security.AuthenticatedUserProcess;
import io.javaclasses.filehub.storage.FileMetadataStorage;
import io.javaclasses.filehub.storage.FileStorage;
import io.javaclasses.filehub.storage.TokenStorage;
import io.javaclasses.filehub.user.UserId;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A process of file removing.
 */
public final class FileRemoving extends AuthenticatedUserProcess<RemoveFile, FileId> {

    private final FileMetadataStorage fileMetadataStorage;
    private final FileStorage fileStorage;

    /**
     * Creates an instance of {@code FileRemoving} process.
     *
     * @param tokenStorage
     *         storage of token records
     * @param fileMetadataStorage
     *         storage of files metadata
     * @param fileStorage
     *         storage of files
     */
    public FileRemoving(TokenStorage tokenStorage, FileMetadataStorage fileMetadataStorage,
                        FileStorage fileStorage) {
        super(tokenStorage);
        this.fileMetadataStorage = checkNotNull(fileMetadataStorage);
        this.fileStorage = checkNotNull(fileStorage);
    }

    @Override
    protected FileId doHandle(RemoveFile removeFile, UserId userId) {

        FileId fileId = removeFile.fileId();
        fileMetadataStorage.remove(fileId);
        fileStorage.remove(fileId);

        return fileId;
    }
}
