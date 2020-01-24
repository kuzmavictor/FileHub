package io.javaclasses.filehub.file;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.ZoneOffset;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An item of data stored in a folder.
 *
 * <p>The item may be of file or folder type.
 */
public final class FolderItem {

    @SerializedName("id")
    @Expose
    private final String id;

    @SerializedName("name")
    @Expose
    private final String name;

    @SerializedName("owner")
    @Expose
    private final String owner;

    @SerializedName("type")
    @Expose
    private final FolderItemType type;

    @SerializedName("uploadDate")
    @Expose
    private final long uploadDate;

    @SerializedName("size")
    @Expose
    private final long fileSize;

    /**
     * Creates a {@code FolderItem} instance based on folder metadata.
     *
     * @param record
     *         a folder metadata record
     */
    FolderItem(FolderMetadataRecord record) {
        checkNotNull(record);
        this.id = record.id()
                        .value();
        this.name = record.name();
        this.owner = record.owner()
                           .value();
        this.type = FolderItemType.FOLDER;
        this.uploadDate = record.creationTime()
                                .toInstant(ZoneOffset.UTC)
                                .toEpochMilli();
        this.fileSize = 0;
    }

    /**
     * Creates a {@code FolderItem} instance based on file metadata.
     *
     * @param record
     *         a file metadata record
     */
    FolderItem(FileMetadataRecord record) {
        checkNotNull(record);
        this.id = record.id()
                        .value();
        this.name = record.name();
        this.owner = record.owner()
                           .value();
        this.type = FolderItemType.FILE;
        this.uploadDate = record.uploadingTime()
                                .toInstant(ZoneOffset.UTC)
                                .toEpochMilli();
        this.fileSize = record.size();
    }

    /**
     * Obtains the unique identifier of the folder item.
     */
    public String id() {
        return id;
    }

    /**
     * Obtains the name of the folder item.
     */
    public String name() {
        return name;
    }

    /**
     * Obtains the owner identifier.
     */
    public String owner() {
        return owner;
    }

    /**
     * Obtains the type of the folder item.
     */
    public FolderItemType type() {
        return type;
    }

    /**
     * Obtains the timestamp of the upload date.
     */
    public long uploadDate() {
        return uploadDate;
    }

    /**
     * Obtains the size of the folder item.
     */
    public long fileSize() {
        return fileSize;
    }
}
