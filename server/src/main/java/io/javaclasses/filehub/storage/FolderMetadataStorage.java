package io.javaclasses.filehub.storage;

import io.javaclasses.filehub.file.FolderId;
import io.javaclasses.filehub.file.FolderMetadataRecord;
import io.javaclasses.filehub.user.UserId;

/**
 * Storage, which contains information about folders.
 */
public interface FolderMetadataStorage extends Storage<FolderId, FolderMetadataRecord> {

    /**
     * Reads all folders' metadata records within the specific folder.
     *
     * @param userId
     *         the identifier of the user
     * @param folderId
     *         the identifier of the folder
     */
    Iterable<FolderMetadataRecord> readFolderContent(UserId userId, FolderId folderId);
}
