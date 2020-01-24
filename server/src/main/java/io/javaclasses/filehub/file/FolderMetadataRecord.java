package io.javaclasses.filehub.file;

import com.google.errorprone.annotations.Immutable;
import io.javaclasses.filehub.storage.FolderMetadataStorage;
import io.javaclasses.filehub.storage.Record;
import io.javaclasses.filehub.user.UserId;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A folder metadata record designed to be written and read from a {@link FolderMetadataStorage}.
 *
 * <p>Users can interact with folders' metadata. Although folders may not exist in the file system.
 */
@Immutable
public final class FolderMetadataRecord extends Record<FolderId> {

    private final UserId owner;
    private final String name;
    private final @Nullable FolderId parentFolder;
    private final LocalDateTime creationTime;

    /**
     * Creates a record instance.
     *
     * @param folderId
     *         an identifier of the folder
     * @param owner
     *         an identifier of an authenticated user who owns this folder
     * @param name
     *         a name of the folder
     * @param parentFolder
     *         a parent folder
     */
    public FolderMetadataRecord(FolderId folderId, UserId owner,
                                String name, FolderId parentFolder) {
        super(folderId);
        this.owner = checkNotNull(owner);
        this.name = checkNotNull(name);
        this.parentFolder = checkNotNull(parentFolder);
        this.creationTime = LocalDateTime.now(ZoneOffset.UTC);
    }

    /**
     * Creates a record instance if the folder is user root folder.
     *
     * <p>The parent folder is {@code null} in this case.
     *
     * @param folderId
     *         an identifier of the folder
     * @param owner
     *         an identifier of an authenticated user who owns this folder
     * @param name
     *         a name of the folder
     */
    public FolderMetadataRecord(FolderId folderId, UserId owner, String name) {
        super(folderId);
        this.owner = checkNotNull(owner);
        this.name = checkNotNull(name);
        this.parentFolder = null;
        this.creationTime = LocalDateTime.now(ZoneOffset.UTC);
    }

    /**
     * Obtains the identifier of the user who owns this folder.
     */
    public UserId owner() {
        return owner;
    }

    /**
     * Obtains the name of a folder.
     */
    public String name() {
        return name;
    }

    /**
     * Obtains the parent folder.
     */
    public @Nullable FolderId parentFolder() {
        return parentFolder;
    }

    /**
     * Obtains the folder creation time.
     */
    public LocalDateTime creationTime() {
        return creationTime;
    }
}
