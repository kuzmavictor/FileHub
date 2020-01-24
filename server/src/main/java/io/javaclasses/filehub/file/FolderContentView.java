package io.javaclasses.filehub.file;

import io.javaclasses.filehub.View;
import io.javaclasses.filehub.storage.FileMetadataStorage;
import io.javaclasses.filehub.storage.FolderMetadataStorage;
import io.javaclasses.filehub.storage.TokenStorage;
import io.javaclasses.filehub.user.UserId;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A process of obtaining content from the folder.
 */
public final class FolderContentView extends View<ReadFolderContent, FolderContent> {

    private final FileMetadataStorage fileMetadataStorage;
    private final FolderMetadataStorage folderMetadataStorage;

    /**
     * Creates an instance of {@code FolderContentView} process.
     *
     * @param tokenStorage
     *         storage of token records
     * @param fileMetadataStorage
     *         storage of file metadata records
     * @param folderMetadataStorage
     *         storage of folder metadata records
     */
    public FolderContentView(TokenStorage tokenStorage,
                             FileMetadataStorage fileMetadataStorage,
                             FolderMetadataStorage folderMetadataStorage) {
        super(tokenStorage);
        this.fileMetadataStorage = checkNotNull(fileMetadataStorage);
        this.folderMetadataStorage = checkNotNull(folderMetadataStorage);
    }

    /**
     * Executes a user query to obtain content for the requested folder.
     *
     * @param query
     *         a query to be executed
     * @return a folder content if successful
     */
    @Override
    public final FolderContent execute(ReadFolderContent query, UserId userId) {
        checkNotNull(query);

        FolderId folderId = FolderId.of(query.folderId());

        Iterable<FolderMetadataRecord> folders =
                folderMetadataStorage.readFolderContent(userId, folderId);

        Iterable<FileMetadataRecord> files =
                fileMetadataStorage.readFolderContent(userId, folderId);

        ArrayList<FolderItem> folderItems = new ArrayList<>();

        addFolders(folders, folderItems);
        addFiles(files, folderItems);

        return new FolderContent(folderItems);
    }

    private static void addFolders(Iterable<FolderMetadataRecord> folders,
                                   Collection<FolderItem> folderItems) {
        folders.forEach(folderRecord -> {
            FolderItem folderItem = new FolderItem(folderRecord);
            folderItems.add(folderItem);
        });
    }

    private static void addFiles(Iterable<FileMetadataRecord> files,
                                 Collection<FolderItem> folderItems) {
        files.forEach(fileRecord -> {
            FolderItem folderItem = new FolderItem(fileRecord);
            folderItems.add(folderItem);
        });
    }
}
