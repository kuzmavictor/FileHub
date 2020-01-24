package io.javaclasses.filehub.file;

import com.google.common.io.ByteSink;
import com.google.common.io.Files;
import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import io.javaclasses.filehub.storage.FileMetadataStorage;
import io.javaclasses.filehub.storage.FileStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A file storage implementation, which uses a file system to store the files.
 *
 * @see FileMetadataStorage
 */
@SuppressWarnings("UnstableApiUsage") // Guava's implementation seems stable.
public final class FileSystem implements FileStorage {

    private static final String SEPARATOR = File.separator;
    private static final Logger logger = LoggerFactory.getLogger(FileSystem.class);
    private final File rootFolder;

    /**
     * Creates a {@code FileSystem} instance using the obtained path as the root folder path.
     */
    public FileSystem(Path folderPath) {
        rootFolder = checkNotNull(folderPath).toFile();
    }

    /**
     * The file reading is allowed only
     * using the {@linkplain FileSystem#read(FileId) identifier of the file record} one by one.
     */
    @Override
    public Iterable<FileContentRecord> readAll() {
        throw new IllegalStateException("The reading of all files is impossible.");
    }

    @Override
    public Optional<FileContentRecord> read(FileId identifier) {
        checkNotNull(identifier);

        String rootFolderPath = this.rootFolder.getAbsolutePath();
        String filePath = rootFolderPath + SEPARATOR + identifier.value();
        File file = new File(filePath);

        try (InputStream fileStream = new FileInputStream(file)) {
            FileContentRecord record = new FileContentRecord(identifier, fileStream);
            return Optional.of(record);
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            return Optional.empty();
        }
    }

    // The message from caught exception is enough.
    @SuppressWarnings("ThrowInsideCatchBlockWhichIgnoresCaughtException")
    @Override
    public void write(FileContentRecord record) {
        checkNotNull(record);

        createRootFolder();
        try {
            String rootFolderPath = this.rootFolder.getAbsolutePath();
            String fullFilePath = rootFolderPath + SEPARATOR + record.id()
                                                                     .value();
            File newFile = new File(fullFilePath);
            ByteSink byteSink = Files.asByteSink(newFile);

            byteSink.writeFrom(record.file());
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            throw new FileSystemException("The file writing was failed.");
        }
    }

    // The message from caught exception is enough.
    @SuppressWarnings("ThrowInsideCatchBlockWhichIgnoresCaughtException")
    @Override
    public void clear() {
        try {
            MoreFiles.deleteDirectoryContents(rootFolder.toPath(),
                                              RecursiveDeleteOption.ALLOW_INSECURE);
        } catch (IOException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage());
            }
            throw new FileSystemException("The deleting of the user root folder failed.");
        }
    }

    @Override
    public void remove(FileId identifier) {
        checkNotNull(identifier);

        String rootFolderPath = rootFolder.getAbsolutePath();
        String fileLocation = identifier.value();

        String fullFilePath = rootFolderPath + SEPARATOR + fileLocation;
        File file = new File(fullFilePath);

        boolean isFileDeleted = file.delete();
        if (!isFileDeleted) {
            throw new FileSystemException("The deleting of the user file failed.");
        }
    }

    /**
     * Creates a root folder if it does not exist.
     *
     * @throws FileSystemException
     *         if the creation of the root folder is failed
     */
    // The message from caught exception is enough.
    @SuppressWarnings("ThrowInsideCatchBlockWhichIgnoresCaughtException")
    private void createRootFolder() {
        if (!rootFolder.exists()) {
            try {
                java.nio.file.Files.createDirectory(rootFolder.toPath());
            } catch (IOException e) {
                if (logger.isErrorEnabled()) {
                    logger.error(e.getMessage());
                }
                throw new FileSystemException("The creation of the user root folder failed.");
            }
        }
    }
}
