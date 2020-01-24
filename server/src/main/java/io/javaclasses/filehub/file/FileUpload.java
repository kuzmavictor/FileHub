package io.javaclasses.filehub.file;

import io.javaclasses.filehub.security.AuthenticatedUserProcess;
import io.javaclasses.filehub.storage.FileMetadataStorage;
import io.javaclasses.filehub.storage.FileStorage;
import io.javaclasses.filehub.storage.TokenStorage;
import io.javaclasses.filehub.user.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A process of file upload.
 */
public final class FileUpload extends AuthenticatedUserProcess<UploadFile, FileUploaded> {

    private static final Logger logger = LoggerFactory.getLogger(FileUpload.class);

    private final FileStorage fileStorage;
    private final FileMetadataStorage fileMetadataStorage;

    /**
     * Creates an instance of {@code FileUpload} process.
     *
     * @param tokenStorage
     *         storage of token records
     * @param fileStorage
     *         storage of files
     * @param fileMetadataStorage
     *         storage of files metadata
     */
    public FileUpload(TokenStorage tokenStorage,
                      FileStorage fileStorage,
                      FileMetadataStorage fileMetadataStorage) {
        super(tokenStorage);
        this.fileStorage = checkNotNull(fileStorage);
        this.fileMetadataStorage = checkNotNull(fileMetadataStorage);
    }

    /**
     * Handles the command of the user file upload.
     *
     * <p>The file must not exist in the current folder. Otherwise, the exception is thrown.
     *
     * @param command
     *         a command to be handled
     * @param userId
     *         an identifier of an authenticated user who uploads a file
     * @return the {@code FileUploaded} if the user file was successfully uploaded
     * @throws UploadFileException
     *         if the file already exists in the FileHub application
     */
    @SuppressWarnings("ThrowInsideCatchBlockWhichIgnoresCaughtException")
    @Override
    protected FileUploaded doHandle(UploadFile command, UserId userId) throws UploadFileException {
        checkNotNull(command);
        checkNotNull(userId);

        String fileName = command.name();
        FileId fileId = FileId.createId(fileName, command.parentFolder());
        Optional<FileMetadataRecord> fileMetadataRecord = fileMetadataStorage.read(fileId);
        boolean existInFileMetadataStorage = fileMetadataRecord.isPresent();

        if (existInFileMetadataStorage) {
            throw new UploadFileException(userId.value(), "Such a file already exists.");
        }
        try {
            writeFile(fileId, command, userId);
            return new FileUploaded(fileId);
        } catch (IOException e) {
            if(logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            throw new UploadFileException(userId.value(), "Failed to obtain file data.");
        }
    }

    /**
     * Writes a file to the file system and its metadata to the file metadata storage.
     */
    private void writeFile(FileId fileId, UploadFile command, UserId userId) throws IOException {
        long fileSize = command.fileSize();
        String fileName = command.name();
        FolderId parentFolder = command.parentFolder();

        FileMetadataRecord newFileMetadataRecord =
                new FileMetadataRecord(fileId, userId, fileName, parentFolder, fileSize);
        fileMetadataStorage.write(newFileMetadataRecord);

        FileContentRecord newFileContentRecord =
                new FileContentRecord(fileId, command.fileStream());
        fileStorage.write(newFileContentRecord);
    }
}
