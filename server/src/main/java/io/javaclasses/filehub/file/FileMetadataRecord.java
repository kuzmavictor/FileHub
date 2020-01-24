package io.javaclasses.filehub.file;

import com.google.errorprone.annotations.Immutable;
import io.javaclasses.filehub.storage.FileMetadataStorage;
import io.javaclasses.filehub.storage.Record;
import io.javaclasses.filehub.user.UserId;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A file metadata record designed to be written and read from a {@link FileMetadataStorage}.
 *
 * <p>The user can see only the file metadata.
 */
@Immutable
public final class FileMetadataRecord extends Record<FileId> {

    private final UserId owner;
    private final long size;
    private final String name;
    private final FolderId parentFolder;
    private final LocalDateTime uploadingTime;

    /**
     * Creates a {@code FileMetadataRecord} instance.
     *
     * @param fileId
     *         an identifier of the file record
     * @param owner
     *         an identifier of an authenticated user who owns this file
     * @param name
     *         a name of the file
     * @param parentFolder
     *         an identifier of the parent folder
     * @param size
     *         a file size
     */
    public FileMetadataRecord(FileId fileId, UserId owner, String name,
                              FolderId parentFolder, long size) {
        super(fileId);
        this.owner = checkNotNull(owner);
        this.name = checkNotNull(name);
        this.parentFolder = checkNotNull(parentFolder);
        this.size = size;
        this.uploadingTime = LocalDateTime.now(ZoneOffset.UTC);
    }

    /**
     * Obtains the file owner identifier.
     */
    public UserId owner() {
        return owner;
    }

    /**
     * Obtains the size of a file.
     */
    public long size() {
        return size;
    }

    /**
     * Obtains the filename.
     */
    public String name() {
        return name;
    }

    /**
     * Obtains the identifier of a parent folder.
     */
    public FolderId parentFolder() {
        return parentFolder;
    }

    /**
     * Obtains the file uploading time.
     */
    public LocalDateTime uploadingTime() {
        return uploadingTime;
    }
}
