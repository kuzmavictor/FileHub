package io.javaclasses.filehub.file;

import com.google.errorprone.annotations.Immutable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The result of the file uploading.
 */
@Immutable
public final class FileUploaded {

    private final FileId fileId;

    /**
     * Creates a {@code FileUploaded} instance.
     *
     * @param id
     *         an identifier of the file which was uploaded
     */
    public FileUploaded(FileId id) {
        this.fileId = checkNotNull(id);
    }

    /**
     * Obtains an identifier of the uploaded file.
     */
    public FileId fileId() {
        return fileId;
    }
}
