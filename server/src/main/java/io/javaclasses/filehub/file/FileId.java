package io.javaclasses.filehub.file;

import com.google.errorprone.annotations.Immutable;
import io.javaclasses.filehub.storage.RecordId;

import java.util.Objects;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.hash.Hashing.sha256;

/**
 * A unique file identifier.
 *
 * <p>The folder can contain files with unique names only. The file ID is formed using
 * the file location and its name.
 */
@Immutable
public final class FileId implements RecordId {

    private final String id;

    /**
     * Creates a {@code FileId} instance.
     *
     * @param identifier
     *         a file identifier value
     */
    private FileId(String identifier) {
        this.id = checkNotNull(identifier);
    }

    /**
     * Creates a {@code FileId} instance based on the identifier value.
     *
     * @param identifier
     *         a file identifier value
     */
    public static FileId of(String identifier) {
        return new FileId(identifier);
    }

    /**
     * Creates a {@code FileId} instance based on the filename and parent folder identifier.
     *
     * @param filename
     *         a name of the file
     * @param parentFolderId
     *         an identifier of a parent folder
     */
    public static FileId createId(String filename, FolderId parentFolderId) {
        checkNotNull(filename);
        checkNotNull(parentFolderId);

        String hashableValue = parentFolderId.value() + filename;

        return new FileId(sha256().hashString(hashableValue, UTF_8)
                                  .toString());
    }

    /**
     * Obtains the file identifier value.
     */
    public String value() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileId fileId = (FileId) o;
        return Objects.equals(id, fileId.id);
    }
}
