package io.javaclasses.filehub.file;

import com.google.common.collect.Iterables;
import io.javaclasses.filehub.storage.AbstractInMemoryStorage;
import io.javaclasses.filehub.storage.FolderMetadataStorage;
import io.javaclasses.filehub.user.UserId;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An in-memory implementation of {@link FolderMetadataStorage}.
 */
public class InMemoryFolderMetadataStorage
        extends AbstractInMemoryStorage<FolderId, FolderMetadataRecord>
        implements FolderMetadataStorage {

    @Override
    public Iterable<FolderMetadataRecord> readFolderContent(UserId userId, FolderId folderId) {
        checkNotNull(userId);
        checkNotNull(folderId);

        Collection<FolderMetadataRecord> folderMetadataRecords = new ArrayList<>();
        records().forEach((record) -> {

            boolean isFolderIdEqual = folderId.equals(record.parentFolder());
            boolean isUserIdEqual = record.owner()
                                          .equals(userId);

            if (isFolderIdEqual && isUserIdEqual) {
                folderMetadataRecords.add(record);
            }
        });
        return Iterables.unmodifiableIterable(folderMetadataRecords);
    }
}
