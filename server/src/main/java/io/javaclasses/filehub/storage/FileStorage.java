package io.javaclasses.filehub.storage;

import io.javaclasses.filehub.file.FileContentRecord;
import io.javaclasses.filehub.file.FileId;

/**
 * Storage, which contains file records.
 */
public interface FileStorage extends Storage<FileId, FileContentRecord> {

}
