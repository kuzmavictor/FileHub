package io.javaclasses.filehub.file;

import com.google.errorprone.annotations.Immutable;
import io.javaclasses.filehub.storage.FileStorage;
import io.javaclasses.filehub.storage.Record;

import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A file data designed to be written and read from a {@link FileStorage}.
 *
 * <p>The user can't see the file binary contents.
 */
@Immutable
@SuppressWarnings("Immutable") // This class is effectively immutable.
public final class FileContentRecord extends Record<FileId> {

    private final InputStream fileStream;

    /**
     * Creates a {@code FileRecord} instance.
     *
     * @param identifier
     *         a file identifier
     * @param fileStream
     *         a file contents stream
     */
    public FileContentRecord(FileId identifier, InputStream fileStream) {
        super(identifier);
        this.fileStream = checkNotNull(fileStream);
    }

    /**
     * Obtains a file contents stream.
     */
    public InputStream file() {
        return fileStream;
    }
}
