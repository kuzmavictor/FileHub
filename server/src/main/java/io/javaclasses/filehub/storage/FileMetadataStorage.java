package io.javaclasses.filehub.storage;

import io.javaclasses.filehub.file.FileId;
import io.javaclasses.filehub.file.FileMetadataRecord;
import io.javaclasses.filehub.file.FolderId;
import io.javaclasses.filehub.user.UserId;

/**
 * Storage, which contains information about files.
 */
public interface FileMetadataStorage extends Storage<FileId, FileMetadataRecord> {

    /**
     * Reads all files metadata records within the specific folder.
     *
     * @param userId
     *         the identifier of the user
     * @param folderId
     *         the identifier of the folder
     */
    Iterable<FileMetadataRecord> readFolderContent(UserId userId, FolderId folderId);
}
