package io.javaclasses.filehub.file;

import com.google.common.collect.Iterables;
import io.javaclasses.filehub.storage.AbstractInMemoryStorage;
import io.javaclasses.filehub.storage.FileMetadataStorage;
import io.javaclasses.filehub.user.UserId;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An in-memory implementation of {@link FileMetadataStorage}.
 */
public final class InMemoryFileMetadataStorage
        extends AbstractInMemoryStorage<FileId, FileMetadataRecord>
        implements FileMetadataStorage {

    @Override
    public Iterable<FileMetadataRecord> readFolderContent(UserId userId, FolderId folderId) {
        checkNotNull(userId);
        checkNotNull(folderId);

        Collection<FileMetadataRecord> fileMetadataRecords = new ArrayList<>();
        records().forEach((record) -> {

            FolderId parentFolder = record.parentFolder();

            boolean isOwnerEqual = record.owner()
                                         .equals(userId);
            boolean isParentFolderEqual = parentFolder.equals(folderId);

            if (isParentFolderEqual && isOwnerEqual) {
                fileMetadataRecords.add(record);
            }
        });
        return Iterables.unmodifiableIterable(fileMetadataRecords);
    }
}
