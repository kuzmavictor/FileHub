package io.javaclasses.filehub.file;

import com.google.errorprone.annotations.Immutable;
import io.javaclasses.filehub.Command;
import io.javaclasses.filehub.user.Token;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A command to upload the user file.
 */
@Immutable
@SuppressWarnings("Immutable") // This class is effectively immutable.
public final class UploadFile implements Command {

    private final Token token;
    private final InputStream fileStream;
    private final String fileName;
    private final FolderId parentFolder;

    /**
     * Creates a {@code UploadFile} command.
     *
     * @param token
     *         a token of an authenticated user who uploads a file
     * @param fileName
     *         an original file name
     * @param file
     *         a file contents
     * @param parentFolder
     *         a folder where the new file will be uploaded
     */
    public UploadFile(Token token, String fileName, byte[] file, FolderId parentFolder) {
        this.token = checkNotNull(token);
        this.fileName = checkNotNull(fileName);
        this.parentFolder = checkNotNull(parentFolder);
        checkNotNull(file);
        this.fileStream = new ByteArrayInputStream(file);
    }

    public UploadFile(Token token, String fileName, InputStream fileContent,
                      FolderId parentFolder) {
        this.token = checkNotNull(token);
        this.fileName = checkNotNull(fileName);
        this.parentFolder = checkNotNull(parentFolder);
        checkNotNull(fileContent);
        this.fileStream = fileContent;
    }

    /**
     * Obtains the contents stream of the new file.
     */
    public InputStream fileStream() {
        return fileStream;
    }

    /**
     * Obtains the size of a new file.
     */
    public long fileSize() throws IOException {
        return fileStream.available();
    }

    /**
     * Obtains the new file name.
     */
    public String name() {
        return fileName;
    }

    /**
     * Obtains the name of a parent folder.
     */
    public FolderId parentFolder() {
        return parentFolder;
    }

    @Override
    public Token token() {
        return token;
    }
}
