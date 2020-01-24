package io.javaclasses.filehub.file;

import io.javaclasses.filehub.security.AuthenticatedUserProcess;
import io.javaclasses.filehub.storage.FolderMetadataStorage;
import io.javaclasses.filehub.storage.TokenStorage;
import io.javaclasses.filehub.user.UserId;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A process of folder creation.
 */
public final class FolderCreation extends AuthenticatedUserProcess<CreateFolder, FolderCreated> {

    private final FolderMetadataStorage folderMetadataStorage;

    /**
     * Creates an instance of {@code FolderCreation} process.
     *
     * @param folderMetadataStorage
     *         storage of folders metadata
     * @param tokenStorage
     *         storage of token records
     */
    public FolderCreation(FolderMetadataStorage folderMetadataStorage, TokenStorage tokenStorage) {
        super(tokenStorage);
        this.folderMetadataStorage = checkNotNull(folderMetadataStorage);
    }

    /**
     * Handles a user command of folder creation.
     *
     * <p>Only authenticated users can create a folder.
     *
     * @param command
     *         a command to be handled
     * @param userId
     *         an identifier of an authenticated user who creates a folder
     * @return the {@code FolderCreated} if the user folder was successfully created
     * @throws FolderCreationException
     *         if such a folder already exists in the current path
     */
    @Override
    protected FolderCreated doHandle(CreateFolder command, UserId userId)
            throws FolderCreationException {
        checkNotNull(command);
        checkNotNull(userId);

        String folderName = command.name();
        FolderId parentFolderId = command.parentFolderId();

        FolderId folderId = FolderId.createId(folderName, parentFolderId);
        Optional<FolderMetadataRecord> folderMetadataRecord = folderMetadataStorage.read(folderId);

        if (folderMetadataRecord.isPresent()) {
            String details =
                    String.format("The folder with a name '%s' exists in the storage.", folderName);
            throw new FolderCreationException(userId.value(), details);
        }

        FolderMetadataRecord newFolderMetadataRecord =
                new FolderMetadataRecord(folderId, userId, folderName, parentFolderId);
        folderMetadataStorage.write(newFolderMetadataRecord);
        return new FolderCreated(folderId);
    }
}
