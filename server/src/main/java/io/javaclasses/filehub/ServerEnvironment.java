package io.javaclasses.filehub;

import com.google.common.io.Files;
import io.javaclasses.filehub.file.FileSystem;
import io.javaclasses.filehub.file.InMemoryFileMetadataStorage;
import io.javaclasses.filehub.file.InMemoryFolderMetadataStorage;
import io.javaclasses.filehub.storage.FileMetadataStorage;
import io.javaclasses.filehub.storage.FileStorage;
import io.javaclasses.filehub.storage.FolderMetadataStorage;
import io.javaclasses.filehub.storage.TokenStorage;
import io.javaclasses.filehub.storage.UserStorage;
import io.javaclasses.filehub.user.InMemoryTokenStorage;
import io.javaclasses.filehub.user.InMemoryUserStorage;

import java.io.File;
import java.nio.file.Paths;

/**
 * An environment of the current server instance.
 *
 * @implNote Its instance is created once per JVM.
 */
public final class ServerEnvironment {

    private static final ServerEnvironment SERVER_ENVIRONMENT = new ServerEnvironment();

    private final UserStorage userStorage;
    private final TokenStorage tokenStorage;
    private final FolderMetadataStorage folderMetadataStorage;
    private final FileMetadataStorage fileMetadataStorage;
    private final FileStorage fileStorage;

    private ServerEnvironment() {
        this.userStorage = new InMemoryUserStorage();
        this.tokenStorage = new InMemoryTokenStorage();
        this.fileMetadataStorage = new InMemoryFileMetadataStorage();
        this.folderMetadataStorage = new InMemoryFolderMetadataStorage();
        File rootFolder = Files.createTempDir();
        this.fileStorage = new FileSystem(rootFolder.toPath());
    }

    /**
     * Returns the server environment instance.
     */
    public static ServerEnvironment instance() {
        return SERVER_ENVIRONMENT;
    }

    /**
     * Returns storage of user records.
     */
    public UserStorage userStorage() {
        return this.userStorage;
    }

    /**
     * Returns storage of token records.
     */
    public TokenStorage tokenStorage() {
        return this.tokenStorage;
    }

    /**
     * Returns storage with files metadata.
     */
    public FileMetadataStorage fileMetadataStorage() {
        return fileMetadataStorage;
    }

    /**
     * Returns storage with folders metadata.
     */
    public FolderMetadataStorage folderMetadataStorage() {
        return folderMetadataStorage;
    }

    /**
     * Returns storage with file contents.
     */
    public FileStorage fileStorage() {
        return fileStorage;
    }
}
